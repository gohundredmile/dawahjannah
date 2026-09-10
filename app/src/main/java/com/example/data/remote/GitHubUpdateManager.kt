package com.example.data.remote

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.squareup.moshi.Json
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
    val isFromAppUpdatesJson: Boolean = false
)

@JsonClass(generateAdapter = true)
data class RemoteContentBundle(
    val version: Int,
    val announcement: String?,
    val extraDuas: List<RemoteDuaItem> = emptyList()
)

@JsonClass(generateAdapter = true)
data class RemoteDuaItem(
    val id: String,
    val titleBn: String,
    val arabic: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val reference: String
)

/**
 * GitHub Update Manager:
 * Handles checking releases from GitHub, comparing semver versions,
 * downloading or launching APK installations, and fetching over-the-air content bundles.
 */
class GitHubUpdateManager(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
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

    private val currentAppVersion = "1.0.0"

    /**
     * Checks GitHub Releases API first, and if unavailable, seamlessly falls back to app-updates.json
     * directly hosted on the repository's main or master branch.
     */
    suspend fun checkLatestRelease(
        owner: String = repoOwner,
        repo: String = repoName
    ): Result<GitHubReleaseInfo> = withContext(Dispatchers.IO) {
        try {
            // Attempt 1: GitHub Official Releases API
            val releaseUrl = "https://api.github.com/repos/$owner/$repo/releases/latest"
            val releaseReq = Request.Builder()
                .url(releaseUrl)
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "DawahToJannah-Android-App")
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

                    return@withContext Result.success(
                        GitHubReleaseInfo(
                            tagName = tagName,
                            versionName = releaseName,
                            releaseNotes = releaseNotes,
                            downloadUrl = apkDownloadUrl,
                            publishedAt = publishedAt,
                            hasNewerVersion = hasNewer,
                            announcement = null,
                            isFromAppUpdatesJson = false
                        )
                    )
                }
            }

            // Attempt 2: Direct raw app-updates.json on main or master branch
            val otaResult = fetchRawAppUpdatesJson(owner, repo)
            if (otaResult.isSuccess) {
                return@withContext Result.success(otaResult.getOrThrow())
            }

            // If neither worked, return the descriptive error
            val releaseCode = releaseResp.code
            Result.failure(
                Exception(
                    "GitHub API (HTTP $releaseCode): কোনো রিলিজ বা app-updates.json পাওয়া যায়নি।\n" +
                    "অনুগ্রহ করে নিশ্চিত করুন যে গিটহাবে $owner/$repo পাবলিক রিপোজিটরিতে 'app-updates.json' পুশ করা আছে।"
                )
            )
        } catch (e: Exception) {
            // Attempt fallback to raw app-updates.json on connection error if possible
            val otaFallback = fetchRawAppUpdatesJson(owner, repo)
            if (otaFallback.isSuccess) {
                Result.success(otaFallback.getOrThrow())
            } else {
                Result.failure(e)
            }
        }
    }

    /**
     * Fetches and parses app-updates.json from main or master branch
     */
    private fun fetchRawAppUpdatesJson(owner: String, repo: String): Result<GitHubReleaseInfo> {
        val branchUrls = listOf(
            "https://raw.githubusercontent.com/$owner/$repo/main/app-updates.json",
            "https://raw.githubusercontent.com/$owner/$repo/master/app-updates.json"
        )

        for (url in branchUrls) {
            try {
                val req = Request.Builder()
                    .url(url)
                    .header("User-Agent", "DawahToJannah-Android-App")
                    .build()
                val resp = client.newCall(req).execute()
                if (resp.isSuccessful) {
                    val body = resp.body?.string()
                    if (!body.isNullOrBlank()) {
                        val parsed = parseAppUpdatesJson(JSONObject(body), owner, repo)
                        return Result.success(parsed)
                    }
                }
            } catch (_: Exception) {
            }
        }
        return Result.failure(Exception("app-updates.json নথিতে পৌঁছানো যায়নি।"))
    }

    /**
     * Parses the standardized app-updates.json format
     */
    fun parseAppUpdatesJson(json: JSONObject, owner: String, repo: String): GitHubReleaseInfo {
        val versionName = json.optString("versionName", "1.0.1")
        val tagName = json.optString("tagName", "v$versionName")
        val releaseTitle = json.optString("releaseTitle", "দা'ওয়াহ টু জান্নাহ - সংস্করণ $versionName")
        val releaseNotes = json.optString("releaseNotes", "নিয়মিত আপডেট ও পারফরম্যান্স উন্নতি।")
        val publishedAt = json.optString("publishedAt", "")
        val apkDownloadUrl = json.optString("apkDownloadUrl", "https://github.com/$owner/$repo/releases")

        // Parse announcement object or string
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
        val hasNewer = isVersionNewer(cleanVersion, currentAppVersion)

        return GitHubReleaseInfo(
            tagName = tagName,
            versionName = releaseTitle,
            releaseNotes = releaseNotes,
            downloadUrl = apkDownloadUrl,
            publishedAt = publishedAt,
            hasNewerVersion = hasNewer,
            announcement = announcementText,
            isFromAppUpdatesJson = true
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
     * Checks a raw content JSON directly hosted on GitHub (e.g. raw.githubusercontent.com/{owner}/{repo}/main/app-updates.json)
     * This allows adding new Surahs, Duas, or announcements to users without requiring an APK re-install.
     */
    suspend fun checkOverTheAirContent(
        owner: String = repoOwner,
        repo: String = repoName
    ): Result<RemoteContentBundle> = withContext(Dispatchers.IO) {
        val branchUrls = listOf(
            "https://raw.githubusercontent.com/$owner/$repo/main/app-updates.json",
            "https://raw.githubusercontent.com/$owner/$repo/master/app-updates.json"
        )

        for (url in branchUrls) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("User-Agent", "DawahToJannah-Android-App")
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val body = response.body?.string() ?: ""
                    val json = JSONObject(body)
                    val version = json.optInt("versionCode", json.optInt("version", 1))

                    val announcementText = when {
                        json.has("announcement") -> {
                            val ann = json.opt("announcement")
                            if (ann is JSONObject) {
                                val title = ann.optString("title", "").trim()
                                val msg = ann.optString("message", "").trim()
                                if (title.isNotEmpty() && msg.isNotEmpty()) "$title: $msg" else msg.ifEmpty { title }
                            } else {
                                ann?.toString()
                            }
                        }
                        else -> null
                    }

                    // Parse optional extra duas
                    val extraDuas = mutableListOf<RemoteDuaItem>()
                    val contentUpdates = json.optJSONObject("contentUpdates")
                    val duasArray = contentUpdates?.optJSONArray("newDuas") ?: json.optJSONArray("extraDuas")
                    if (duasArray != null) {
                        for (i in 0 until duasArray.length()) {
                            val d = duasArray.getJSONObject(i)
                            extraDuas.add(
                                RemoteDuaItem(
                                    id = d.optString("id", "dua_$i"),
                                    titleBn = d.optString("titleBn", ""),
                                    arabic = d.optString("arabic", ""),
                                    pronunciationBn = d.optString("pronunciationBn", ""),
                                    meaningBn = d.optString("meaningBn", ""),
                                    reference = d.optString("reference", "")
                                )
                            )
                        }
                    }

                    return@withContext Result.success(
                        RemoteContentBundle(
                            version = version,
                            announcement = announcementText,
                            extraDuas = extraDuas
                        )
                    )
                }
            } catch (_: Exception) {
            }
        }

        Result.failure(Exception("গিটহাবে app-updates.json পাওয়া যায়নি।"))
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

    private fun isVersionNewer(remoteVer: String, localVer: String): Boolean {
        val remoteParts = remoteVer.split(".").mapNotNull { it.toIntOrNull() }
        val localParts = localVer.split(".").mapNotNull { it.toIntOrNull() }

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
