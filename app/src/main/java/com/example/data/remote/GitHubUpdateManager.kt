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
    val hasNewerVersion: Boolean
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

    // Default repository (can be customized by user or environment)
    var repoOwner: String = "dawah-app"
    var repoName: String = "dawah-to-jannah"

    private val currentAppVersion = "1.0.0"

    /**
     * Checks GitHub Releases API: https://api.github.com/repos/{owner}/{repo}/releases/latest
     */
    suspend fun checkLatestRelease(
        owner: String = repoOwner,
        repo: String = repoName
    ): Result<GitHubReleaseInfo> = withContext(Dispatchers.IO) {
        try {
            val url = "https://api.github.com/repos/$owner/$repo/releases/latest"
            val request = Request.Builder()
                .url(url)
                .header("Accept", "application/vnd.github.v3+json")
                .header("User-Agent", "DawahToJannah-Android-App")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                // If repository release is not yet published on GitHub, provide clear status
                return@withContext Result.failure(
                    Exception("GitHub API HTTP ${response.code}: রিলিজ পাওয়া যায়নি অথবা রিপোজিটরি সেট করা হয়নি।")
                )
            }

            val bodyString = response.body?.string() ?: return@withContext Result.failure(
                Exception("GitHub থেকে খালি রেসপন্স এসেছে।")
            )

            val json = JSONObject(bodyString)
            val tagName = json.optString("tag_name", "v1.0.0")
            val releaseName = json.optString("name", tagName)
            val releaseNotes = json.optString("body", "কোনো বিবরণ উল্লেখ নেই।")
            val publishedAt = json.optString("published_at", "")

            // Find APK in assets
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

            // Fallback to HTML release URL if no asset attached
            if (apkDownloadUrl == null) {
                apkDownloadUrl = json.optString("html_url", "https://github.com/$owner/$repo/releases")
            }

            val cleanVersion = tagName.removePrefix("v").trim()
            val hasNewer = isVersionNewer(cleanVersion, currentAppVersion)

            Result.success(
                GitHubReleaseInfo(
                    tagName = tagName,
                    versionName = releaseName,
                    releaseNotes = releaseNotes,
                    downloadUrl = apkDownloadUrl,
                    publishedAt = publishedAt,
                    hasNewerVersion = hasNewer
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Checks a raw content JSON directly hosted on GitHub (e.g. raw.githubusercontent.com/{owner}/{repo}/main/content-updates.json)
     * This allows adding new Surahs, Duas, or announcements to users without requiring an APK re-install.
     */
    suspend fun checkOverTheAirContent(
        owner: String = repoOwner,
        repo: String = repoName
    ): Result<RemoteContentBundle> = withContext(Dispatchers.IO) {
        try {
            val url = "https://raw.githubusercontent.com/$owner/$repo/main/app-updates.json"
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "DawahToJannah-Android-App")
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext Result.failure(
                    Exception("কনটেন্ট ওভার-দ্য-এয়ার পাওয়া যায়নি (HTTP ${response.code})")
                )
            }

            val body = response.body?.string() ?: ""
            val json = JSONObject(body)
            val version = json.optInt("version", 1)
            val announcement = json.optString("announcement", null)

            Result.success(
                RemoteContentBundle(
                    version = version,
                    announcement = announcement
                )
            )
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
