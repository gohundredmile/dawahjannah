package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.core.content.FileProvider
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import com.example.util.ExcludedIslamicLifeTopics

@JsonClass(generateAdapter = true)
data class GitHubReleaseInfo(
    val tagName: String,
    val versionName: String,
    val releaseNotes: String,
    val downloadUrl: String?,
    val publishedAt: String,
    val hasNewerVersion: Boolean,
    val announcement: String? = null,
    val isFromAppUpdatesJson: Boolean = false,
    val isContentUpdateOnly: Boolean = false,
    val remoteVersionCode: Int = 0,
    val isSameVersionInstalled: Boolean = false
)

@JsonClass(generateAdapter = true)
data class RemoteContentBundle(
    val version: Int,
    val versionName: String = "1.0.9",
    val tagName: String = "v1.0.9",
    val releaseTitle: String = "",
    val releaseNotes: String = "",
    val announcement: String?,
    val extraDuas: List<RemoteDuaItem> = emptyList(),
    val isApplied: Boolean = true,
    val appliedAt: Long = System.currentTimeMillis(),
    val sourceDescription: String = "GitHub OTA",
    val contentHash: String = "",
    val lastUpdated: String = ""
)

@JsonClass(generateAdapter = true)
data class RemoteDuaItem(
    val id: String,
    val category: String = "",
    val titleBn: String,
    val arabic: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val reference: String,
    val virtuesBn: String = "",
    val serialNumberBn: String = "",
    val repetitionOrTimeBn: String = "",
    val detailsBn: String = "",
    val targetSectionId: String = ""
)

/**
 * GitHub Update Manager:
 * Handles checking releases from GitHub, comparing semver versions and versionCodes,
 * downloading or launching APK installations, and fetching over-the-air content bundles with CDN cache busting.
 */
class GitHubUpdateManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .build()

    // Dedicated high-speed download client with long timeouts and streaming resilience for large APKs (25-50MB)
    private val downloadClient = OkHttpClient.Builder()
        .connectTimeout(35, TimeUnit.SECONDS)
        .readTimeout(180, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .build()

    private val prefs = context.getSharedPreferences("github_update_prefs", Context.MODE_PRIVATE)

    // Configurable repository with SharedPreferences persistence (default: gohundredmile/dawahjannah)
    var repoOwner: String
        get() {
            val saved = prefs.getString("repo_owner", "gohundredmile") ?: "gohundredmile"
            return if (saved == "dawah-app" || saved.isBlank()) "gohundredmile" else saved
        }
        set(value) = prefs.edit().putString("repo_owner", value.trim()).apply()

    var repoName: String
        get() {
            val saved = prefs.getString("repo_name", "dawahjannah") ?: "dawahjannah"
            return if (saved == "dawah-to-jannah" || saved.isBlank()) "dawahjannah" else saved
        }
        set(value) = prefs.edit().putString("repo_name", value.trim()).apply()

    fun updateRepoConfig(owner: String, repo: String) {
        prefs.edit()
            .putString("repo_owner", owner.trim())
            .putString("repo_name", repo.trim())
            .apply()
    }

    val installedAppVersionName: String
        get() = try {
            val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            pInfo.versionName ?: com.example.BuildConfig.VERSION_NAME
        } catch (_: Exception) {
            com.example.BuildConfig.VERSION_NAME
        }

    val installedAppVersionCode: Long
        get() = try {
            val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode.toLong()
            }
        } catch (_: Exception) {
            com.example.BuildConfig.VERSION_CODE.toLong()
        }

    val bundledVersionName: String by lazy {
        try {
            val stream = context.assets.open("app-updates.json")
            val content = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(content)
            json.optString("versionName", "1.0.9").removePrefix("v").removePrefix("V").trim()
        } catch (_: Exception) {
            "1.0.9"
        }
    }

    val bundledVersionCode: Int by lazy {
        try {
            val stream = context.assets.open("app-updates.json")
            val content = stream.bufferedReader().use { it.readText() }
            val json = JSONObject(content)
            json.optInt("versionCode", 109)
        } catch (_: Exception) {
            109
        }
    }

    var appliedVersionCode: Int
        get() {
            val saved = prefs.getInt("applied_version_code", 0)
            val bundled = bundledVersionCode
            if (saved == 0 || bundled > saved) {
                prefs.edit().putInt("applied_version_code", bundled).apply()
                return bundled
            }
            return saved
        }
        set(value) = prefs.edit().putInt("applied_version_code", value).apply()

    var appliedContentVersion: String
        get() {
            val saved = prefs.getString("applied_content_version", null)
            val bundled = bundledVersionName
            if (saved == null || isVersionNewer(bundled, saved)) {
                prefs.edit().putString("applied_content_version", bundled).apply()
                return bundled
            }
            return saved
        }
        set(value) = prefs.edit().putString("applied_content_version", value.trim()).apply()

    var appliedContentHash: String
        get() = prefs.getString("applied_content_hash", "") ?: ""
        set(value) = prefs.edit().putString("applied_content_hash", value).apply()

    var appliedLastUpdated: String
        get() = prefs.getString("applied_last_updated", "") ?: ""
        set(value) = prefs.edit().putString("applied_last_updated", value).apply()

    val currentAppVersion: String
        get() = appliedContentVersion

    val installedVersionCode: Long by lazy {
        try {
            val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                pInfo.longVersionCode
            } else {
                @Suppress("DEPRECATION")
                pInfo.versionCode.toLong()
            }
        } catch (_: Exception) {
            140L
        }
    }

    val installedVersionName: String by lazy {
        try {
            val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
            } else {
                @Suppress("DEPRECATION")
                context.packageManager.getPackageInfo(context.packageName, 0)
            }
            pInfo.versionName ?: "1.4.0"
        } catch (_: Exception) {
            "1.4.0"
        }
    }

    fun calculateHash(content: String): String {
        return try {
            val md = java.security.MessageDigest.getInstance("MD5")
            val bytes = md.digest(content.toByteArray(Charsets.UTF_8))
            bytes.joinToString("") { "%02x".format(it) }
        } catch (_: Exception) {
            content.hashCode().toString()
        }
    }

    /**
     * Builds candidate URLs with cache-busting timestamp parameter to bypass GitHub CDN's 5-minute cache.
     * Includes direct Git API endpoint (zero edge cache delay), jsDelivr, and GitHub raw with HEAD.
     */
    private fun getCandidateUrls(owner: String, repo: String): List<String> {
        val ts = System.currentTimeMillis()
        return listOf(
            // 1. Direct GitHub Raw with HEAD & main branch (instant, avoids GitHub API 60 req/hr rate limit)
            "https://raw.githubusercontent.com/$owner/$repo/main/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/HEAD/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/main/app/src/main/assets/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/master/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/master/app/src/main/assets/app-updates.json?ts=$ts",
            // 2. jsDelivr CDN with @main / @latest
            "https://cdn.jsdelivr.net/gh/$owner/$repo@main/app-updates.json?ts=$ts",
            "https://cdn.jsdelivr.net/gh/$owner/$repo@latest/app-updates.json?ts=$ts",
            // 3. GitHub Git contents API
            "https://api.github.com/repos/$owner/$repo/contents/app-updates.json",
            "https://api.github.com/repos/$owner/$repo/contents/app/src/main/assets/app-updates.json"
        )
    }

    /**
     * Checks GitHub Releases API and raw app-updates.json on repository.
     * Evaluates both so in-app OTA content updates are detected even if no new full APK release was drafted.
     */
    suspend fun checkLatestRelease(
        owner: String = repoOwner,
        repo: String = repoName
    ): Result<GitHubReleaseInfo> = withContext(Dispatchers.IO) {
        var gitHubReleaseInfo: GitHubReleaseInfo? = null
        var otaUpdateInfo: GitHubReleaseInfo? = null

        // 1. Check raw app-updates.json (for content updates / announcements)
        val otaResult = fetchRawAppUpdatesJson(owner, repo)
        if (otaResult.isSuccess) {
            otaUpdateInfo = otaResult.getOrNull()
        }

        // 2. Check GitHub Releases API (for full APK binary releases)
        try {
            val releaseUrls = listOf(
                "https://api.github.com/repos/$owner/$repo/releases/latest",
                "https://api.github.com/repos/$owner/$repo/releases?per_page=3"
            )

            for (releaseUrl in releaseUrls) {
                if (gitHubReleaseInfo != null) break
                try {
                    val releaseReq = Request.Builder()
                        .url(releaseUrl)
                        .header("Accept", "application/vnd.github.v3+json")
                        .header("User-Agent", "DawahToJannah-Android-App")
                        .header("Cache-Control", "no-cache, no-store, must-revalidate")
                        .header("Pragma", "no-cache")
                        .build()

                    val releaseResp = client.newCall(releaseReq).execute()
                    if (releaseResp.isSuccessful) {
                        val bodyString = releaseResp.body?.string()
                        if (!bodyString.isNullOrBlank()) {
                            val releaseObj = if (bodyString.trimStart().startsWith("[")) {
                                val array = JSONArray(bodyString)
                                if (array.length() > 0) array.getJSONObject(0) else null
                            } else {
                                JSONObject(bodyString)
                            }

                            if (releaseObj != null) {
                                val tagName = releaseObj.optString("tag_name", "v1.0.0")
                                val releaseName = releaseObj.optString("name", tagName)
                                val releaseNotes = releaseObj.optString("body", "কোনো বিবরণ উল্লেখ নেই।")
                                val publishedAt = releaseObj.optString("published_at", "")

                                var apkDownloadUrl: String? = null
                                val assetsArray = releaseObj.optJSONArray("assets")
                                if (assetsArray != null) {
                                    // 1. Strictly prioritize app-release.apk as marked in GitHub Releases
                                    for (i in 0 until assetsArray.length()) {
                                        val asset = assetsArray.getJSONObject(i)
                                        val name = asset.optString("name", "")
                                        if (name.equals("app-release.apk", ignoreCase = true) ||
                                            (name.endsWith(".apk", ignoreCase = true) && name.contains("release", ignoreCase = true))) {
                                            val dl = asset.optString("browser_download_url", "")
                                            if (dl.isNotBlank()) {
                                                apkDownloadUrl = dl
                                                break
                                            }
                                        }
                                    }
                                    // 2. Fallback to app-debug.apk or any .apk if release apk is not present
                                    if (apkDownloadUrl == null) {
                                        for (i in 0 until assetsArray.length()) {
                                            val asset = assetsArray.getJSONObject(i)
                                            val name = asset.optString("name", "")
                                            if (name.endsWith(".apk", ignoreCase = true)) {
                                                val dl = asset.optString("browser_download_url", "")
                                                if (dl.isNotBlank()) {
                                                    apkDownloadUrl = dl
                                                    break
                                                }
                                            }
                                        }
                                    }
                                }
                                if (apkDownloadUrl == null) {
                                    apkDownloadUrl = "https://github.com/$owner/$repo/releases/download/$tagName/app-release.apk"
                                }

                                val cleanVersion = tagName.removePrefix("v").removePrefix("V").split(" ")[0].trim()
                                val parsedCode = parseVersionToCode(cleanVersion).toInt()
                                val hasNewer = isVersionNewer(cleanVersion, installedAppVersionName) || (parsedCode.toLong() > installedAppVersionCode)
                                val isSameVersion = cleanVersion.equals(installedAppVersionName.trim().removePrefix("v").removePrefix("V"), ignoreCase = true) ||
                                        (parsedCode > 0 && parsedCode.toLong() == installedAppVersionCode)
                                val isSameOrOlder = isSameVersion || !hasNewer

                                gitHubReleaseInfo = GitHubReleaseInfo(
                                    tagName = tagName,
                                    versionName = releaseName,
                                    releaseNotes = releaseNotes,
                                    downloadUrl = apkDownloadUrl,
                                    publishedAt = publishedAt,
                                    hasNewerVersion = hasNewer,
                                    announcement = null,
                                    isFromAppUpdatesJson = false,
                                    isContentUpdateOnly = false,
                                    remoteVersionCode = parsedCode,
                                    isSameVersionInstalled = isSameOrOlder
                                )
                            }
                        }
                    }
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // Ignored
        }

        // 2b. Rate-limit fallback: check latest release via redirect from https://github.com/$owner/$repo/releases/latest
        if (gitHubReleaseInfo == null) {
            try {
                val noRedirectClient = client.newBuilder().followRedirects(false).build()
                val req = Request.Builder()
                    .url("https://github.com/$owner/$repo/releases/latest")
                    .header("User-Agent", "DawahToJannah-Android-App")
                    .build()
                val resp = noRedirectClient.newCall(req).execute()
                val location = resp.header("Location") ?: ""
                if (location.contains("/releases/tag/")) {
                    val tag = location.substringAfterLast("/")
                    val cleanVersion = tag.removePrefix("v").removePrefix("V").split(" ")[0].trim()
                    val parsedCode = parseVersionToCode(cleanVersion).toInt()
                    val hasNewer = isVersionNewer(cleanVersion, installedAppVersionName) || (parsedCode.toLong() > installedAppVersionCode)
                    val isSameVersion = cleanVersion.equals(installedAppVersionName.trim().removePrefix("v").removePrefix("V"), ignoreCase = true) ||
                            (parsedCode > 0 && parsedCode.toLong() == installedAppVersionCode)
                    val isSameOrOlder = isSameVersion || !hasNewer
                    val directApk = "https://github.com/$owner/$repo/releases/download/$tag/app-release.apk"

                    gitHubReleaseInfo = GitHubReleaseInfo(
                        tagName = tag,
                        versionName = "দা'ওয়াহ টু জান্নাহ - সংস্করণ $cleanVersion",
                        releaseNotes = "গিটহাব রিলিজ $cleanVersion থেকে সরাসরি সংগৃহীত।",
                        downloadUrl = directApk,
                        publishedAt = "",
                        hasNewerVersion = hasNewer,
                        announcement = null,
                        isFromAppUpdatesJson = false,
                        isContentUpdateOnly = false,
                        remoteVersionCode = parsedCode,
                        isSameVersionInstalled = isSameOrOlder
                    )
                }
            } catch (_: Exception) {}
        }

        // 3. Determine best response:
        val ghVer = gitHubReleaseInfo?.tagName?.removePrefix("v")?.removePrefix("V")?.split(" ")?.get(0)?.trim() ?: ""
        val otaVer = otaUpdateInfo?.versionName?.removePrefix("v")?.removePrefix("V")?.split(" ")?.get(0)?.trim() ?: ""

        // If GitHub release has a newer version than installed app, or newer/equal version to OTA, prioritize GitHub binary release
        if (gitHubReleaseInfo != null) {
            val ghIsNewerThanInstalled = gitHubReleaseInfo.hasNewerVersion
            val ghIsNewerThanOta = isVersionNewer(ghVer, otaVer)
            val ghIsEqualToOta = ghVer.isNotBlank() && ghVer.equals(otaVer, ignoreCase = true)

            if (ghIsNewerThanInstalled || ghIsNewerThanOta || (ghIsEqualToOta && !gitHubReleaseInfo.isSameVersionInstalled)) {
                val safeUrl = resolveDirectApkUrl(gitHubReleaseInfo.downloadUrl)
                return@withContext Result.success(gitHubReleaseInfo.copy(downloadUrl = safeUrl))
            }

            if (gitHubReleaseInfo.isSameVersionInstalled && (otaUpdateInfo == null || !otaUpdateInfo.hasNewerVersion)) {
                val safeUrl = resolveDirectApkUrl(gitHubReleaseInfo.downloadUrl)
                return@withContext Result.success(gitHubReleaseInfo.copy(downloadUrl = safeUrl))
            }
        }

        // If OTA has newer version or newer content
        if (otaUpdateInfo != null && otaUpdateInfo.hasNewerVersion) {
            val downloadUrl = resolveDirectApkUrl(gitHubReleaseInfo?.downloadUrl ?: otaUpdateInfo.downloadUrl)
            return@withContext Result.success(
                otaUpdateInfo.copy(downloadUrl = downloadUrl)
            )
        }

        // If GitHub release was fetched
        if (gitHubReleaseInfo != null) {
            val safeUrl = resolveDirectApkUrl(gitHubReleaseInfo.downloadUrl)
            return@withContext Result.success(gitHubReleaseInfo.copy(downloadUrl = safeUrl))
        }

        // If OTA info was fetched
        if (otaUpdateInfo != null) {
            val safeUrl = resolveDirectApkUrl(otaUpdateInfo.downloadUrl)
            return@withContext Result.success(otaUpdateInfo.copy(downloadUrl = safeUrl))
        }

        // Fallback: Check local bundled asset so user always sees valid info
        val localBundled = getLocalBundledUpdates()
        if (localBundled != null) {
            val localHasNewer = (localBundled.remoteVersionCode > installedAppVersionCode) ||
                    isVersionNewer(localBundled.versionName, installedAppVersionName) ||
                    (localBundled.announcement != null && localBundled.announcement != prefs.getString("applied_announcement_id", ""))
            return@withContext Result.success(
                localBundled.copy(
                    hasNewerVersion = localHasNewer,
                    isSameVersionInstalled = !localHasNewer,
                    releaseNotes = localBundled.releaseNotes + "\n\n(লোকাল অফলাইন মোড: গিটহাব সংযোগ না পাওয়া গেলেও লোকাল প্যাকেজ ডাটা সম্পূর্ণ প্রস্তুত।)"
                )
            )
        }

        Result.failure(
            Exception(
                "গিটহাব সার্ভার বা লোকাল এসেটে পৌঁছানো যায়নি। অনুগ্রহ করে ইন্টারনেট সংযোগ চেক করুন।"
            )
        )
    }

    /**
     * Fetches and parses app-updates.json from repository with cache-busting
     */
    private fun fetchRawAppUpdatesJson(owner: String, repo: String): Result<GitHubReleaseInfo> {
        val candidateUrls = getCandidateUrls(owner, repo)
        var lastErr: Exception? = null

        for (url in candidateUrls) {
            try {
                val reqBuilder = Request.Builder()
                    .url(url)
                    .header("User-Agent", "DawahToJannah-Android-App")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0")
                    .header("Pragma", "no-cache")

                if (url.contains("api.github.com")) {
                    reqBuilder.header("Accept", "application/vnd.github.v3.raw")
                }

                val resp = client.newCall(reqBuilder.build()).execute()
                if (resp.isSuccessful) {
                    val body = resp.body?.string()
                    if (!body.isNullOrBlank()) {
                        val parsed = parseAppUpdatesJson(JSONObject(body), owner, repo, rawContentString = body)
                        return Result.success(parsed)
                    }
                }
            } catch (e: Exception) {
                lastErr = e
            }
        }
        return Result.failure(lastErr ?: Exception("app-updates.json নথিতে পৌঁছানো যায়নি।"))
    }

    /**
     * Parses the standardized app-updates.json format.
     * Accurately detects ANY newly added content, modified hashes, or updated lastUpdated dates
     * even if the author forgot to bump the semver version numbers!
     */
    fun parseAppUpdatesJson(
        json: JSONObject,
        owner: String,
        repo: String,
        rawContentString: String? = null
    ): GitHubReleaseInfo {
        val versionCode = json.optInt("versionCode", 109)
        val versionName = json.optString("versionName", "1.0.9")
        val tagName = json.optString("tagName", "v$versionName")
        val releaseTitle = json.optString("releaseTitle", "দা'ওয়াহ টু জান্নাহ - সংস্করণ $versionName")
        val releaseNotes = json.optString("releaseNotes", "নিয়মিত আপডেট ও নতুন কনটেন্ট সংযোজন।")
        val publishedAt = json.optString("publishedAt", "")
        val apkDownloadUrl = json.optString("apkDownloadUrl", "https://github.com/$owner/$repo/releases")

        // Parse announcement
        val announcementObj = json.optJSONObject("announcement")
        val announcementId = announcementObj?.optString("id", "") ?: ""
        val announcementText = when {
            json.has("announcement") -> {
                val ann = json.opt("announcement")
                if (ann is JSONObject) {
                    val title = ann.optString("title", "").trim()
                    val msg = ann.optString("message", "").trim()
                    if (title.isNotEmpty() && msg.isNotEmpty()) "$title\n$msg" else msg.ifEmpty { title }
                } else {
                    ann?.toString()
                }
            }
            else -> null
        }

        val contentUpdates = json.optJSONObject("contentUpdates")
        val lastUpdated = contentUpdates?.optString("lastUpdated", "") ?: ""
        val remoteHash = if (!rawContentString.isNullOrBlank()) calculateHash(rawContentString) else ""

        val cleanVersion = versionName.removePrefix("v").removePrefix("V").trim()
        val hasNewerVer = isVersionNewer(cleanVersion, installedAppVersionName)
        val hasHigherCode = versionCode > installedAppVersionCode
        val isSameOrOlder = (versionCode <= installedAppVersionCode && !isVersionNewer(cleanVersion, installedAppVersionName))

        // Compare content hashes
        val hasDifferentHash = remoteHash.isNotBlank() && appliedContentHash.isNotBlank() && remoteHash != appliedContentHash

        // Check if remote has extra or different count of duas
        val remoteDuasCount = contentUpdates?.optJSONArray("newDuas")?.length() ?: 0
        val currentDuasCount = getPersistedDownloadedUpdates()?.extraDuas?.size ?: 0
        val hasDifferentDuasCount = remoteDuasCount != currentDuasCount

        // Check if lastUpdated date has been modified
        val hasNewerLastUpdated = lastUpdated.isNotBlank() && appliedLastUpdated.isNotBlank() && lastUpdated != appliedLastUpdated

        // Check if announcement is newly activated
        val hasNewAnnouncement = announcementId.isNotBlank() && announcementId != prefs.getString("applied_announcement_id", "")

        val hasNewer = hasNewerVer || hasHigherCode || hasDifferentHash ||
                (hasDifferentDuasCount && remoteDuasCount > 0) ||
                hasNewerLastUpdated || hasNewAnnouncement

        return GitHubReleaseInfo(
            tagName = tagName,
            versionName = releaseTitle,
            releaseNotes = releaseNotes,
            downloadUrl = apkDownloadUrl,
            publishedAt = publishedAt,
            hasNewerVersion = hasNewer,
            announcement = announcementText,
            isFromAppUpdatesJson = true,
            isContentUpdateOnly = true,
            remoteVersionCode = versionCode,
            isSameVersionInstalled = isSameOrOlder && !hasDifferentHash && !hasNewAnnouncement && !hasNewerLastUpdated
        )
    }

    /**
     * Returns the local bundled app-updates.json for offline testing or pre-deployment preview
     */
    fun getLocalBundledUpdates(): GitHubReleaseInfo? {
        return try {
            val stream = context.assets.open("app-updates.json")
            val content = stream.bufferedReader().use { it.readText() }
            parseAppUpdatesJson(JSONObject(content), repoOwner, repoName, rawContentString = content)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Downloads over-the-air content and saves it persistently inside the app's local storage.
     * Uses cache-busting HTTP headers and query params.
     * If remote fetch fails or is offline, intelligently syncs from local bundled asset.
     */
    suspend fun downloadAndApplyContentUpdates(
        owner: String = repoOwner,
        repo: String = repoName,
        allowLocalAssetFallback: Boolean = true
    ): Result<RemoteContentBundle> = withContext(Dispatchers.IO) {
        try {
            var rawJsonString: String? = null
            var sourceDescription = "গিটহাব অনলাইন (GitHub OTA)"
            var lastErrorMessage: String? = null

            // 1. Try remote candidate URLs first
            val candidateUrls = getCandidateUrls(owner, repo)
            for (url in candidateUrls) {
                try {
                    val reqBuilder = Request.Builder()
                        .url(url)
                        .header("User-Agent", "DawahToJannah-Android-App")
                        .header("Cache-Control", "no-cache, no-store, must-revalidate, max-age=0")
                        .header("Pragma", "no-cache")

                    if (url.contains("api.github.com")) {
                        reqBuilder.header("Accept", "application/vnd.github.v3.raw")
                    }

                    val response = client.newCall(reqBuilder.build()).execute()
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        if (!body.isNullOrBlank()) {
                            rawJsonString = body
                            sourceDescription = "গিটহাব ক্লাউড ওটিএ ($owner/$repo)"
                            break
                        }
                    } else {
                        lastErrorMessage = "HTTP ${response.code} (${response.message})"
                    }
                } catch (e: Exception) {
                    lastErrorMessage = e.localizedMessage ?: e.message
                }
            }

            // 2. Fallback to bundled asset if remote unavailable OR if allowLocalAssetFallback is true
            if (rawJsonString == null) {
                try {
                    val stream = context.assets.open("app-updates.json")
                    rawJsonString = stream.bufferedReader().use { it.readText() }
                    sourceDescription = if (allowLocalAssetFallback) "ইন-অ্যাপ বান্ডেল এসেট (লোকাল সিঙ্ক)" else "ইন-অ্যাপ প্যাকেজ এসেট (অফলাইন মোড)"
                } catch (e: Exception) {
                    return@withContext Result.failure(Exception("লোকাল এসেট কনটেন্ট পড়া যায়নি: ${e.message} (রিমোট ত্রুটি: $lastErrorMessage)"))
                }
            }

            val bundle = parseBundleFromJsonString(rawJsonString, sourceDescription)
                ?: return@withContext Result.failure(Exception("আপডেট কনটেন্ট ডাটা পার্স করতে ব্যর্থ হয়েছে।"))

            // Persist to internal app storage
            try {
                val file = java.io.File(context.filesDir, "downloaded_content_updates.json")
                file.writeText(rawJsonString)
            } catch (_: Exception) {
            }

            // Record applied versions and hash in SharedPreferences
            appliedContentVersion = bundle.versionName
            appliedVersionCode = bundle.version
            appliedContentHash = bundle.contentHash
            appliedLastUpdated = bundle.lastUpdated

            val json = JSONObject(rawJsonString)
            val annId = json.optJSONObject("announcement")?.optString("id", "") ?: ""
            prefs.edit()
                .putString("applied_tag_name", bundle.tagName)
                .putString("applied_announcement_id", annId)
                .putLong("last_download_time", System.currentTimeMillis())
                .apply()

            Result.success(bundle)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reads locally persisted updates from filesDir or bundled asset.
     * Compares version codes & content hashes: If bundled asset in the APK is newer or modified,
     * it automatically upgrades to the bundled asset so the app is never stuck on stale content.
     */
    fun getPersistedDownloadedUpdates(): RemoteContentBundle? {
        try {
            val bundledBundle = getBundledAssetBundle()
            val savedBundle = getSavedDownloadedBundle()

            // If bundled APK has a higher version code, newer content hash, or more duas than saved disk cache,
            // upgrade immediately to bundled asset!
            if (bundledBundle != null && savedBundle != null) {
                val savedHasExcluded = savedBundle.extraDuas.any { 
                    ExcludedIslamicLifeTopics.isExcluded(it.category) || ExcludedIslamicLifeTopics.isExcluded(it.titleBn) 
                }
                val bundledIsNewer = bundledBundle.version > savedBundle.version ||
                        savedHasExcluded ||
                        isVersionNewer(bundledBundle.versionName, savedBundle.versionName) ||
                        (bundledBundle.contentHash.isNotBlank() && bundledBundle.contentHash != savedBundle.contentHash)

                if (bundledIsNewer) {
                    // Update cache file with newer bundled content
                    try {
                        val stream = context.assets.open("app-updates.json")
                        val content = stream.bufferedReader().use { it.readText() }
                        val file = java.io.File(context.filesDir, "downloaded_content_updates.json")
                        file.writeText(content)
                    } catch (_: Exception) {
                    }
                    appliedContentVersion = bundledBundle.versionName
                    appliedVersionCode = bundledBundle.version
                    appliedContentHash = bundledBundle.contentHash
                    appliedLastUpdated = bundledBundle.lastUpdated
                    return bundledBundle
                }
                return savedBundle
            }

            if (savedBundle != null) return savedBundle
            if (bundledBundle != null) return bundledBundle

            return null
        } catch (_: Exception) {
            return null
        }
    }

    private fun getBundledAssetBundle(): RemoteContentBundle? {
        return try {
            val stream = context.assets.open("app-updates.json")
            val content = stream.bufferedReader().use { it.readText() }
            parseBundleFromJsonString(content, "ইন-অ্যাপ বান্ডেল")
        } catch (_: Exception) {
            null
        }
    }

    private fun getSavedDownloadedBundle(): RemoteContentBundle? {
        return try {
            val file = java.io.File(context.filesDir, "downloaded_content_updates.json")
            if (!file.exists()) return null
            val content = file.readText()
            parseBundleFromJsonString(content, "সংরক্ষিত ওটিএ ক্যাশ")
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Forces clearing of the downloaded cache file and reloads fresh bundled content
     */
    fun forceReloadAndResync(): RemoteContentBundle? {
        try {
            val file = java.io.File(context.filesDir, "downloaded_content_updates.json")
            if (file.exists()) {
                file.delete()
            }
            prefs.edit()
                .remove("applied_content_version")
                .remove("applied_version_code")
                .remove("applied_content_hash")
                .remove("applied_last_updated")
                .remove("applied_announcement_id")
                .apply()

            val bundled = getBundledAssetBundle()
            if (bundled != null) {
                appliedContentVersion = bundled.versionName
                appliedVersionCode = bundled.version
                appliedContentHash = bundled.contentHash
                appliedLastUpdated = bundled.lastUpdated
                // Write back fresh
                try {
                    val stream = context.assets.open("app-updates.json")
                    val content = stream.bufferedReader().use { it.readText() }
                    file.writeText(content)
                } catch (_: Exception) {
                }
            }
            return bundled
        } catch (_: Exception) {
            return null
        }
    }

    private fun parseBundleFromJsonString(rawJsonString: String, source: String): RemoteContentBundle? {
        return try {
            val json = JSONObject(rawJsonString)
            val version = json.optInt("versionCode", json.optInt("version", 109))
            val versionName = json.optString("versionName", bundledVersionName)
            val tagName = json.optString("tagName", "v$versionName")
            val releaseTitle = json.optString("releaseTitle", "দা'ওয়াহ টু জান্নাহ - সংস্করণ $versionName")
            val releaseNotes = json.optString("releaseNotes", "নিয়মিত আপডেট ও নতুন কনটেন্ট সংযোজন।")

            val announcementText = when {
                json.has("announcement") -> {
                    val ann = json.opt("announcement")
                    if (ann is JSONObject) {
                        val title = ann.optString("title", "").trim()
                        val msg = ann.optString("message", "").trim()
                        if (title.isNotEmpty() && msg.isNotEmpty()) "$title\n$msg" else msg.ifEmpty { title }
                    } else {
                        ann?.toString()
                    }
                }
                else -> null
            }

            val extraDuas = mutableListOf<RemoteDuaItem>()
            val contentUpdates = json.optJSONObject("contentUpdates")
            val lastUpdated = contentUpdates?.optString("lastUpdated", "") ?: ""
            val duasArray = contentUpdates?.optJSONArray("newDuas") ?: json.optJSONArray("extraDuas")
            if (duasArray != null) {
                for (i in 0 until duasArray.length()) {
                    val d = duasArray.getJSONObject(i)
                    val cat = d.optString("category", "অন্যান্য দোয়া")
                    val title = d.optString("titleBn", "")
                    if (ExcludedIslamicLifeTopics.isExcluded(cat) || ExcludedIslamicLifeTopics.isExcluded(title)) {
                        continue
                    }
                    extraDuas.add(
                        RemoteDuaItem(
                            id = d.optString("id", "dua_remote_$i"),
                            category = cat,
                            titleBn = title,
                            arabic = d.optString("arabic", ""),
                            pronunciationBn = d.optString("pronunciationBn", ""),
                            meaningBn = d.optString("meaningBn", ""),
                            reference = d.optString("reference", ""),
                            virtuesBn = d.optString("virtuesBn", d.optString("fojilotBn", "")),
                            serialNumberBn = d.optString("serialNumberBn", ""),
                            repetitionOrTimeBn = d.optString("repetitionOrTimeBn", ""),
                            detailsBn = d.optString("detailsBn", ""),
                            targetSectionId = d.optString("targetSectionId", "")
                        )
                    )
                }
            }

            RemoteContentBundle(
                version = version,
                versionName = versionName,
                tagName = tagName,
                releaseTitle = releaseTitle,
                releaseNotes = releaseNotes,
                announcement = announcementText,
                extraDuas = extraDuas,
                isApplied = true,
                sourceDescription = source,
                contentHash = calculateHash(rawJsonString),
                lastUpdated = lastUpdated
            )
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Resolves direct APK download URL from GitHub Release or raw assets.
     * Strictly targets 'app-release.apk' as marked in GitHub release assets.
     */
    fun resolveDirectApkUrl(inputUrl: String?): String {
        val rawUrl = inputUrl?.trim() ?: ""
        var releaseUrl = if (rawUrl.contains("app-debug.apk", ignoreCase = true)) {
            rawUrl.replace("app-debug.apk", "app-release.apk", ignoreCase = true)
        } else {
            rawUrl
        }
        val owner = repoOwner
        val repo = repoName

        if (releaseUrl.isBlank()) {
            return "https://github.com/$owner/$repo/releases/latest/download/app-release.apk"
        }

        if (releaseUrl.contains("/tag/")) {
            releaseUrl = releaseUrl.replace("/tag/", "/download/") + "/app-release.apk"
        }

        if (!releaseUrl.endsWith(".apk", ignoreCase = true) && !releaseUrl.contains(".apk?", ignoreCase = true)) {
            releaseUrl = when {
                releaseUrl.contains("releases/download/") -> "$releaseUrl/app-release.apk".replace("//app-release.apk", "/app-release.apk")
                else -> "https://github.com/$owner/$repo/releases/latest/download/app-release.apk"
            }
        }

        // Strictly prioritize app-release.apk
        if (releaseUrl.contains("app-debug.apk", ignoreCase = true)) {
            releaseUrl = releaseUrl.replace("app-debug.apk", "app-release.apk", ignoreCase = true)
        }

        return releaseUrl
    }

    /**
     * Searches GitHub Releases API prioritizing live app-release.apk asset browser_download_url
     */
    suspend fun discoverLiveApkUrl(owner: String = repoOwner, repo: String = repoName): String? = withContext(Dispatchers.IO) {
        // 1. Check latest release - strictly prioritize app-release.apk
        try {
            val req = Request.Builder()
                .url("https://api.github.com/repos/$owner/$repo/releases/latest")
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "DawahToJannah-Android-App")
                .header("Cache-Control", "no-cache")
                .build()
            val resp = client.newCall(req).execute()
            if (resp.isSuccessful) {
                val body = resp.body?.string()
                if (!body.isNullOrBlank()) {
                    val json = JSONObject(body)
                    val assets = json.optJSONArray("assets")
                    if (assets != null) {
                        // Pass 1: find app-release.apk
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            val name = asset.optString("name", "")
                            if (name.equals("app-release.apk", ignoreCase = true) ||
                                (name.endsWith(".apk", ignoreCase = true) && name.contains("release", ignoreCase = true))) {
                                val downloadUrl = asset.optString("browser_download_url", "")
                                if (downloadUrl.isNotBlank()) {
                                    return@withContext downloadUrl
                                }
                            }
                        }
                        // Pass 2: fallback to any apk
                        for (i in 0 until assets.length()) {
                            val asset = assets.getJSONObject(i)
                            val name = asset.optString("name", "")
                            if (name.endsWith(".apk", ignoreCase = true)) {
                                val downloadUrl = asset.optString("browser_download_url", "")
                                if (downloadUrl.isNotBlank()) {
                                    return@withContext downloadUrl
                                }
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {}

        // 2. Check recent releases for app-release.apk
        try {
            val req = Request.Builder()
                .url("https://api.github.com/repos/$owner/$repo/releases?per_page=5")
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "DawahToJannah-Android-App")
                .header("Cache-Control", "no-cache")
                .build()
            val resp = client.newCall(req).execute()
            if (resp.isSuccessful) {
                val body = resp.body?.string()
                if (!body.isNullOrBlank()) {
                    val releases = JSONArray(body)
                    for (i in 0 until releases.length()) {
                        val rel = releases.getJSONObject(i)
                        val assets = rel.optJSONArray("assets")
                        if (assets != null) {
                            for (j in 0 until assets.length()) {
                                val asset = assets.getJSONObject(j)
                                val name = asset.optString("name", "")
                                if (name.equals("app-release.apk", ignoreCase = true) ||
                                    (name.endsWith(".apk", ignoreCase = true) && name.contains("release", ignoreCase = true))) {
                                    val downloadUrl = asset.optString("browser_download_url", "")
                                    if (downloadUrl.isNotBlank()) {
                                        return@withContext downloadUrl
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {}

        // Direct fallback: guaranteed app-release.apk from latest release
        "https://github.com/$owner/$repo/releases/latest/download/app-release.apk"
    }

    /**
     * Checks if a previously downloaded update APK exists in any of the update directories
     * and has a valid file size (greater than 5MB).
     */
    fun getCachedApkFile(): File? {
        val candidates = listOf(
            File(File(context.cacheDir, "apk_updates"), "dawah_to_jannah_update.apk"),
            File(File(context.filesDir, "apk_updates"), "dawah_to_jannah_update.apk"),
            File(File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir, "apk_updates"), "dawah_to_jannah_update.apk")
        )
        for (f in candidates) {
            if (f.exists() && f.length() > 5_000_000L) {
                return f
            }
        }
        return null
    }

    /**
     * Returns a cached APK only if it matches the expected remote version and is strictly
     * newer than the installed app. Automatically deletes any stale or older APKs.
     */
    fun getValidCachedApkFile(expectedCode: Long = 0L, expectedVer: String? = null): File? {
        val candidates = listOf(
            File(File(context.cacheDir, "apk_updates"), "dawah_to_jannah_update.apk"),
            File(File(context.filesDir, "apk_updates"), "dawah_to_jannah_update.apk"),
            File(File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir, "apk_updates"), "dawah_to_jannah_update.apk")
        )
        for (f in candidates) {
            if (f.exists() && f.length() > 5_000_000L) {
                try {
                    val archiveInfo = context.packageManager.getPackageArchiveInfo(f.absolutePath, 0)
                    if (archiveInfo != null) {
                        val apkCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            archiveInfo.longVersionCode
                        } else {
                            @Suppress("DEPRECATION")
                            archiveInfo.versionCode.toLong()
                        }
                        val apkVer = archiveInfo.versionName ?: ""

                        // Delete if it is same or older than installed version to prevent stale installation loop
                        if (apkCode <= installedAppVersionCode) {
                            f.delete()
                            continue
                        }

                        // Check if it matches expected target
                        if (expectedCode > 0 && apkCode == expectedCode) {
                            return f
                        }
                        if (!expectedVer.isNullOrBlank() && apkVer.isNotBlank()) {
                            val cleanApkVer = apkVer.trim().removePrefix("v").removePrefix("V")
                            val cleanExpected = expectedVer.trim().removePrefix("v").removePrefix("V")
                            if (cleanApkVer.equals(cleanExpected, ignoreCase = true)) {
                                return f
                            }
                        }
                        if (expectedCode == 0L && expectedVer.isNullOrBlank() && apkCode > installedAppVersionCode) {
                            return f
                        }
                    }
                } catch (_: Exception) {}
            }
        }
        return null
    }

    /**
     * Deletes all cached APK files and temporary downloads
     */
    fun clearCachedApks() {
        val dirs = listOf(
            File(context.cacheDir, "apk_updates"),
            File(context.filesDir, "apk_updates"),
            File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) ?: context.cacheDir, "apk_updates")
        )
        for (dir in dirs) {
            try {
                if (dir.exists() && dir.isDirectory) {
                    dir.listFiles()?.forEach { file ->
                        if (file.name.endsWith(".apk", ignoreCase = true) || file.name.endsWith(".tmp", ignoreCase = true)) {
                            file.delete()
                        }
                    }
                }
            } catch (_: Exception) {}
        }
    }

    /**
     * Downloads the full APK file with real-time progress callbacks for OTA installation.
     * Uses optimized download client, internal storage to prevent Scoped Storage blocks,
     * atomic file swap (.tmp -> .apk), 64KB buffer, and comprehensive URL fallbacks.
     * STRICTLY PRIORITIZES app-release.apk!
     */
    suspend fun downloadApkWithProgress(
        url: String,
        onProgress: (progress: Float, downloadedBytes: Long, totalBytes: Long) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        val candidates = linkedSetOf<String>()

        // 1. Primary target passed (prioritizes app-release.apk)
        val primaryTarget = resolveDirectApkUrl(url)
        if (primaryTarget.isNotBlank()) {
            candidates.add(primaryTarget)
        }

        // 2. Direct discovery from GitHub API / releases (prioritizes app-release.apk)
        try {
            val discoveredUrl = discoverLiveApkUrl(repoOwner, repoName)
            if (!discoveredUrl.isNullOrBlank()) {
                candidates.add(discoveredUrl)
            }
        } catch (_: Exception) {}

        // 3. Fallback standard release URL for latest app-release.apk
        candidates.add("https://github.com/$repoOwner/$repoName/releases/latest/download/app-release.apk")

        // 4. Tag specific fallback if URL contains a tag
        val tagMatch = Regex("""releases/download/([^/]+)/""").find(primaryTarget)
        if (tagMatch != null) {
            val tag = tagMatch.groupValues[1]
            candidates.add("https://github.com/$repoOwner/$repoName/releases/download/$tag/app-release.apk")
        }

        // 5. Secondary fallback: app-debug.apk if release apk is missing
        candidates.add("https://github.com/$repoOwner/$repoName/releases/latest/download/app-debug.apk")
        if (tagMatch != null) {
            val tag = tagMatch.groupValues[1]
            candidates.add("https://github.com/$repoOwner/$repoName/releases/download/$tag/app-debug.apk")
        }

        var lastError: Exception? = null

        val apkDir = File(context.cacheDir, "apk_updates").apply { mkdirs() }
        val apkFile = File(apkDir, "dawah_to_jannah_update.apk")
        val tempFile = File(apkDir, "dawah_to_jannah_update.apk.tmp")

        for (candidateUrl in candidates) {
            try {
                val request = Request.Builder()
                    .url(candidateUrl)
                    .header("User-Agent", "DawahToJannah-Android-App")
                    .header("Accept", "application/vnd.android.package-archive, application/octet-stream, */*")
                    .build()

                val response = downloadClient.newCall(request).execute()
                if (!response.isSuccessful) {
                    lastError = Exception("HTTP ${response.code} (URL: $candidateUrl)")
                    continue
                }

                val body = response.body ?: continue
                val contentLength = body.contentLength()
                val contentType = response.header("Content-Type", "") ?: ""

                if (contentType.contains("text/html", ignoreCase = true) && contentLength in 1..200000) {
                    lastError = Exception("HTML page returned instead of APK from $candidateUrl")
                    continue
                }

                if (tempFile.exists()) {
                    tempFile.delete()
                }

                body.byteStream().use { inputStream ->
                    FileOutputStream(tempFile).use { outputStream ->
                        val buffer = ByteArray(65536) // 64KB buffer for faster throughput
                        var bytesRead: Int
                        var totalRead: Long = 0

                        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                            outputStream.write(buffer, 0, bytesRead)
                            totalRead += bytesRead
                            val progress = if (contentLength > 0) (totalRead.toFloat() / contentLength.toFloat()).coerceIn(0f, 1f) else -1f
                            onProgress(progress, totalRead, contentLength)
                        }
                        outputStream.flush()
                    }
                }

                tempFile.setReadable(true, false)

                if (tempFile.length() < 1_000_000) {
                    val size = tempFile.length()
                    tempFile.delete()
                    lastError = Exception("ডাউনলোডকৃত ফাইলটি অসম্পূর্ণ বা ক্ষতিগ্রস্ত (সাইজ: $size বাইট)")
                    continue
                }

                // Verify that the downloaded file is a genuine, non-corrupted Android package
                val archiveInfo = context.packageManager.getPackageArchiveInfo(tempFile.absolutePath, 0)
                if (archiveInfo == null) {
                    tempFile.delete()
                    lastError = Exception("ডাউনলোডকৃত ফাইলটি সঠিক অ্যান্ড্রয়েড APK প্যাকেজ নয় বা ক্ষতিগ্রস্ত হয়েছে।")
                    continue
                }

                // Atomic rename to final APK file
                if (apkFile.exists()) {
                    apkFile.delete()
                }
                val renameSuccess = tempFile.renameTo(apkFile)
                val finalFile = if (renameSuccess) apkFile else tempFile
                finalFile.setReadable(true, false)

                // Successfully downloaded a valid APK!
                return@withContext Result.success(finalFile)
            } catch (e: Exception) {
                lastError = e
                try {
                    if (tempFile.exists()) tempFile.delete()
                } catch (_: Exception) {}
            }
        }

        // If all candidates failed, provide clear diagnostic message in Bengali
        val errorMessage = """
গিটহাব থেকে সরাসরি APK ডাউনলোড করা সম্ভব হয়নি (${lastError?.message ?: "সংযোগ সমস্যা"})।

💡 পরামর্শ:
১. রিপোজিটরিতে (github.com/$repoOwner/$repoName) 'app-release.apk' রিলিজ এসেটে সংযুক্ত রয়েছে কিনা যাচাই করুন।
২. আপনার ফোনে পর্যাপ্ত স্টোরেজ স্পেস এবং ইন্টারনেট সংযোগ নিশ্চিত করুন।
""".trimIndent()

        Result.failure(Exception(errorMessage))
    }

    /**
     * Triggers the Android package installer for a downloaded APK.
     * Grants permissions explicitly to package installers and handles Unknown Sources securely.
     * Returns true if installer intent was launched, false if permission setting is required.
     */
    fun installApk(apkFile: File): Result<Boolean> {
        return try {
            if (!apkFile.exists() || apkFile.length() < 500_000L) {
                return Result.failure(Exception("ইনস্টল করার জন্য কোনো সম্পূর্ণ APK ফাইল পাওয়া যায়নি। সাইজ: ${apkFile.length()} বাইট"))
            }

            // Android 8.0+ Unknown App Sources check
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!context.packageManager.canRequestPackageInstalls()) {
                    try {
                        val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                            data = Uri.parse("package:${context.packageName}")
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(intent)
                    } catch (_: Exception) {
                        try {
                            val intent = Intent(Settings.ACTION_SECURITY_SETTINGS).apply {
                                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            }
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    }
                    return Result.success(false)
                }
            }

            apkFile.setReadable(true, false)

            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            }

            // Explicitly grant URI permission to known package installer packages
            val knownInstallers = listOf(
                "com.google.android.packageinstaller",
                "com.android.packageinstaller",
                "com.samsung.android.packageinstaller"
            )
            for (pkg in knownInstallers) {
                try {
                    context.grantUriPermission(pkg, apkUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
                } catch (_: Exception) {}
            }

            // Explicitly grant URI permission to any queried package installer components
            try {
                val resolvedActivities = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    context.packageManager.queryIntentActivities(
                        installIntent,
                        PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_DEFAULT_ONLY.toLong())
                    )
                } else {
                    @Suppress("DEPRECATION")
                    context.packageManager.queryIntentActivities(installIntent, PackageManager.MATCH_DEFAULT_ONLY)
                }
                for (resolveInfo in resolvedActivities) {
                    val targetPackage = resolveInfo.activityInfo.packageName
                    context.grantUriPermission(
                        targetPackage,
                        apkUri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                }
            } catch (_: Exception) {}

            context.startActivity(installIntent)
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Downloads APK using Android DownloadManager or opens the browser download link
     */
    fun downloadApk(url: String, versionName: String): Result<String> {
        return try {
            val cleanUrl = url.trim()
            if (cleanUrl.isBlank()) {
                return Result.failure(Exception("ডাউনলোড লিঙ্ক পাওয়া যায়নি।"))
            }

            val isDirectApk = cleanUrl.endsWith(".apk", ignoreCase = true) || cleanUrl.contains("/download/")

            if (isDirectApk) {
                try {
                    val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as? android.app.DownloadManager
                    if (dm != null) {
                        val uri = Uri.parse(cleanUrl)
                        val fileName = "dawah-to-jannah-${versionName.replace(" ", "_")}.apk"
                        val request = android.app.DownloadManager.Request(uri)
                            .setTitle("Dawah to Jannah $versionName")
                            .setDescription("দা'ওয়াহ টু জান্নাহ APK ডাউনলোড হচ্ছে...")
                            .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            .setDestinationInExternalPublicDir(android.os.Environment.DIRECTORY_DOWNLOADS, fileName)
                            .setMimeType("application/vnd.android.package-archive")

                        dm.enqueue(request)
                    }
                } catch (_: Exception) {
                    // Fallback to browser
                }
            }

            // Launch browser / system viewer
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cleanUrl)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)

            Result.success(cleanUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Opens download link or browser for APK update
     */
    fun openDownloadPage(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (_: Exception) {
        }
    }

    fun isVersionNewer(remoteVer: String, localVer: String): Boolean {
        val cleanRemote = remoteVer.trim().removePrefix("v").removePrefix("V")
        val cleanLocal = localVer.trim().removePrefix("v").removePrefix("V")

        val remoteParts = cleanRemote.split(".").mapNotNull { it.toIntOrNull() }
        val localParts = cleanLocal.split(".").mapNotNull { it.toIntOrNull() }

        val length = maxOf(remoteParts.size, localParts.size)
        for (i in 0 until length) {
            val r = remoteParts.getOrElse(i) { 0 }
            val l = localParts.getOrElse(i) { 0 }
            if (r > l) return true
            if (r < l) return false
        }
        return false
    }

    fun parseVersionToCode(versionStr: String): Long {
        val clean = versionStr.trim().removePrefix("v").removePrefix("V").split(" ")[0].trim()
        val parts = clean.split(".").mapNotNull { it.toIntOrNull() }
        return when (parts.size) {
            0 -> 0L
            1 -> parts[0].toLong() * 100
            2 -> parts[0].toLong() * 100 + parts[1].toLong() * 10
            else -> parts[0].toLong() * 100 + parts[1].toLong() * 10 + parts[2].toLong()
        }
    }
}
