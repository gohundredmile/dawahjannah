package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.squareup.moshi.JsonClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

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
    val remoteVersionCode: Int = 0
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
    val sourceDescription: String = "GitHub OTA"
)

@JsonClass(generateAdapter = true)
data class RemoteDuaItem(
    val id: String,
    val category: String = "",
    val titleBn: String,
    val arabic: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val reference: String
)

/**
 * GitHub Update Manager:
 * Handles checking releases from GitHub, comparing semver versions and versionCodes,
 * downloading or launching APK installations, and fetching over-the-air content bundles with CDN cache busting.
 */
class GitHubUpdateManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
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

    val currentAppVersion: String
        get() = appliedContentVersion

    /**
     * Builds candidate URLs with cache-busting timestamp parameter to bypass GitHub CDN's 5-minute cache.
     */
    private fun getCandidateUrls(owner: String, repo: String): List<String> {
        val ts = System.currentTimeMillis()
        return listOf(
            "https://raw.githubusercontent.com/$owner/$repo/main/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/main/app/src/main/assets/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/master/app-updates.json?ts=$ts",
            "https://raw.githubusercontent.com/$owner/$repo/master/app/src/main/assets/app-updates.json?ts=$ts"
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

        // 1. Check raw app-updates.json first (source of truth for OTA content updates)
        val otaResult = fetchRawAppUpdatesJson(owner, repo)
        if (otaResult.isSuccess) {
            otaUpdateInfo = otaResult.getOrNull()
        }

        // 2. Check GitHub Releases API (for full APK binary releases)
        try {
            val releaseUrl = "https://api.github.com/repos/$owner/$repo/releases/latest"
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
                    val json = JSONObject(bodyString)
                    val tagName = json.optString("tag_name", "v1.0.0")
                    val releaseName = json.optString("name", tagName)
                    val releaseNotes = json.optString("body", "কোনো বিবরণ উল্লেখ নেই।")
                    val publishedAt = json.optString("published_at", "")

                    var apkDownloadUrl: String? = null
                    val assetsArray = json.optJSONArray("assets")
                    if (assetsArray != null) {
                        for (i in 0 until assetsArray.length()) {
                            val asset = assetsArray.getJSONObject(i)
                            val name = asset.optString("name", "")
                            if (name.endsWith(".apk", ignoreCase = true)) {
                                apkDownloadUrl = asset.optString("browser_download_url", null)
                                break
                            }
                        }
                    }
                    if (apkDownloadUrl == null) {
                        apkDownloadUrl = json.optString("html_url", "https://github.com/$owner/$repo/releases")
                    }

                    val cleanVersion = tagName.removePrefix("v").trim()
                    val hasNewer = isVersionNewer(cleanVersion, currentAppVersion)

                    gitHubReleaseInfo = GitHubReleaseInfo(
                        tagName = tagName,
                        versionName = releaseName,
                        releaseNotes = releaseNotes,
                        downloadUrl = apkDownloadUrl,
                        publishedAt = publishedAt,
                        hasNewerVersion = hasNewer,
                        announcement = null,
                        isFromAppUpdatesJson = false,
                        isContentUpdateOnly = false
                    )
                }
            }
        } catch (_: Exception) {
            // Ignored, fallback to OTA info
        }

        // 3. Determine best response:
        // If OTA has newer version or newer content, prioritize it or merge with GitHub release
        if (otaUpdateInfo != null && otaUpdateInfo.hasNewerVersion) {
            val downloadUrl = gitHubReleaseInfo?.downloadUrl ?: otaUpdateInfo.downloadUrl
            return@withContext Result.success(
                otaUpdateInfo.copy(downloadUrl = downloadUrl)
            )
        }

        // If GitHub release has newer binary version
        if (gitHubReleaseInfo != null && gitHubReleaseInfo.hasNewerVersion) {
            return@withContext Result.success(gitHubReleaseInfo)
        }

        // If OTA info was fetched successfully (even if already up-to-date)
        if (otaUpdateInfo != null) {
            return@withContext Result.success(otaUpdateInfo)
        }

        // If GitHub release info was fetched (even if already up-to-date)
        if (gitHubReleaseInfo != null) {
            return@withContext Result.success(gitHubReleaseInfo)
        }

        // Fallback: Check local bundled asset so user always sees valid info
        val localBundled = getLocalBundledUpdates()
        if (localBundled != null) {
            return@withContext Result.success(
                localBundled.copy(
                    hasNewerVersion = false,
                    releaseNotes = localBundled.releaseNotes + "\n\n(লোকাল অফলাইন মোড: গিটহাব সংযোগ পাওয়া যায়নি, লোকাল ডাটাবেস সম্পূর্ণ প্রস্তুত।)"
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
                val req = Request.Builder()
                    .url(url)
                    .header("User-Agent", "DawahToJannah-Android-App")
                    .header("Cache-Control", "no-cache, no-store, must-revalidate")
                    .header("Pragma", "no-cache")
                    .build()
                val resp = client.newCall(req).execute()
                if (resp.isSuccessful) {
                    val body = resp.body?.string()
                    if (!body.isNullOrBlank()) {
                        val parsed = parseAppUpdatesJson(JSONObject(body), owner, repo)
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
     * Parses the standardized app-updates.json format
     */
    fun parseAppUpdatesJson(json: JSONObject, owner: String, repo: String): GitHubReleaseInfo {
        val versionCode = json.optInt("versionCode", 109)
        val versionName = json.optString("versionName", "1.0.9")
        val tagName = json.optString("tagName", "v$versionName")
        val releaseTitle = json.optString("releaseTitle", "দা'ওয়াহ টু জান্নাহ - সংস্করণ $versionName")
        val releaseNotes = json.optString("releaseNotes", "নিয়মিত আপডেট ও নতুন কনটেন্ট সংযোজন।")
        val publishedAt = json.optString("publishedAt", "")
        val apkDownloadUrl = json.optString("apkDownloadUrl", "https://github.com/$owner/$repo/releases")

        // Parse announcement
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

        val cleanVersion = versionName.removePrefix("v").trim()
        val hasNewerVer = isVersionNewer(cleanVersion, currentAppVersion)
        val hasHigherCode = versionCode > appliedVersionCode

        // Also check if remote has extra duas that might not be applied
        val remoteDuasCount = json.optJSONObject("contentUpdates")?.optJSONArray("newDuas")?.length() ?: 0
        val currentDuasCount = getPersistedDownloadedUpdates()?.extraDuas?.size ?: 0
        val hasMoreDuas = remoteDuasCount > currentDuasCount

        val hasNewer = hasNewerVer || hasHigherCode || (hasMoreDuas && remoteDuasCount > 0)

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
            remoteVersionCode = versionCode
        )
    }

    /**
     * Returns the local bundled app-updates.json for offline testing or pre-deployment preview
     */
    fun getLocalBundledUpdates(): GitHubReleaseInfo? {
        return try {
            val stream = context.assets.open("app-updates.json")
            val content = stream.bufferedReader().use { it.readText() }
            parseAppUpdatesJson(JSONObject(content), repoOwner, repoName)
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Downloads over-the-air content and saves it persistently inside the app's local storage.
     * Uses cache-busting HTTP headers and query params.
     * If remote fetch fails or is offline, intelligently syncs from local bundled asset if bundled has newer content.
     */
    suspend fun downloadAndApplyContentUpdates(
        owner: String = repoOwner,
        repo: String = repoName,
        allowLocalAssetFallback: Boolean = false
    ): Result<RemoteContentBundle> = withContext(Dispatchers.IO) {
        try {
            var rawJsonString: String? = null
            var sourceDescription = "গিটহাব অনলাইন (GitHub OTA)"
            var lastErrorMessage: String? = null

            // 1. Try remote candidate URLs first
            val candidateUrls = getCandidateUrls(owner, repo)
            for (url in candidateUrls) {
                try {
                    val request = Request.Builder()
                        .url(url)
                        .header("User-Agent", "DawahToJannah-Android-App")
                        .header("Cache-Control", "no-cache, no-store, must-revalidate")
                        .header("Pragma", "no-cache")
                        .build()
                    val response = client.newCall(request).execute()
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
                if (allowLocalAssetFallback || bundledVersionCode >= appliedVersionCode) {
                    try {
                        val stream = context.assets.open("app-updates.json")
                        rawJsonString = stream.bufferedReader().use { it.readText() }
                        sourceDescription = if (allowLocalAssetFallback) "লোকাল এসেট প্রিভিউ" else "ইন-অ্যাপ প্যাকেজ এসেট (অফলাইন মোড)"
                    } catch (e: Exception) {
                        return@withContext Result.failure(Exception("লোকাল এসেট কনটেন্ট পড়া যায়নি: ${e.message}"))
                    }
                } else {
                    val reason = lastErrorMessage ?: "গিটহাব সার্ভারের সাথে সংযোগ স্থাপন করা সম্ভব হয়নি।"
                    return@withContext Result.failure(
                        Exception(
                            "গিটহাব ($owner/$repo) থেকে রিমোট 'app-updates.json' ফাইল ডাউনলোড করা যায়নি।\n\n" +
                            "কারিগরি স্ট্যাটাস: $reason\n\n" +
                            "পরামর্শ: প্রজেক্টের রুটে এবং গিটহাবে 'app-updates.json' পুশ করা আছে কিনা নিশ্চিত করুন।"
                        )
                    )
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

            // Record applied versions in SharedPreferences
            appliedContentVersion = bundle.versionName
            appliedVersionCode = bundle.version
            prefs.edit()
                .putString("applied_tag_name", bundle.tagName)
                .putLong("last_download_time", System.currentTimeMillis())
                .apply()

            Result.success(bundle)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Reads locally persisted updates from filesDir or bundled asset.
     * Compares version codes: If bundled asset in the APK is newer than the saved file,
     * it automatically upgrades to the bundled asset so the app is never stuck on stale content.
     */
    fun getPersistedDownloadedUpdates(): RemoteContentBundle? {
        try {
            val bundledBundle = getBundledAssetBundle()
            val savedBundle = getSavedDownloadedBundle()

            // If bundled APK has a higher version code or newer versionName than saved disk cache,
            // upgrade immediately to bundled asset!
            if (bundledBundle != null && savedBundle != null) {
                val bundledIsNewer = bundledBundle.version > savedBundle.version ||
                        isVersionNewer(bundledBundle.versionName, savedBundle.versionName) ||
                        bundledBundle.extraDuas.size > savedBundle.extraDuas.size

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
                .apply()

            val bundled = getBundledAssetBundle()
            if (bundled != null) {
                appliedContentVersion = bundled.versionName
                appliedVersionCode = bundled.version
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
            val duasArray = contentUpdates?.optJSONArray("newDuas") ?: json.optJSONArray("extraDuas")
            if (duasArray != null) {
                for (i in 0 until duasArray.length()) {
                    val d = duasArray.getJSONObject(i)
                    extraDuas.add(
                        RemoteDuaItem(
                            id = d.optString("id", "dua_remote_$i"),
                            category = d.optString("category", "অন্যান্য দোয়া"),
                            titleBn = d.optString("titleBn", ""),
                            arabic = d.optString("arabic", ""),
                            pronunciationBn = d.optString("pronunciationBn", ""),
                            meaningBn = d.optString("meaningBn", ""),
                            reference = d.optString("reference", "")
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
                sourceDescription = source
            )
        } catch (_: Exception) {
            null
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
}
