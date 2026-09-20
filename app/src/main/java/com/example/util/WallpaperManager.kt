package com.example.util

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.annotation.DrawableRes
import com.example.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

data class BuiltInWallpaper(
    val id: String,
    val nameBn: String,
    @DrawableRes val resId: Int,
    val descriptionBn: String
)

data class WallpaperConfigState(
    val isCustomEnabled: Boolean = false,
    val customUrl: String = "",
    val cachedFilePath: String? = null,
    val cachedFileSizeBytes: Long = 0L,
    val cachedWidth: Int = 0,
    val cachedHeight: Int = 0,
    val cachedTimestamp: Long = 0L,
    val selectedBuiltInIndex: Int = 0
)

sealed class WallpaperDownloadResult {
    data class Success(val sizeBytes: Long, val width: Int, val height: Int) : WallpaperDownloadResult()
    data class Error(val messageBn: String) : WallpaperDownloadResult()
}

object WallpaperManager {

    private const val PREFS_NAME = "dawah_wallpaper_prefs"
    private const val KEY_CUSTOM_ENABLED = "is_custom_wallpaper_enabled"
    private const val KEY_CUSTOM_URL = "custom_wallpaper_url"
    private const val KEY_SELECTED_BUILTIN_IDX = "selected_builtin_wallpaper_idx"
    private const val KEY_CACHED_FILE_SIZE = "cached_file_size_bytes"
    private const val KEY_CACHED_WIDTH = "cached_image_width"
    private const val KEY_CACHED_HEIGHT = "cached_image_height"
    private const val KEY_CACHED_TIMESTAMP = "cached_image_timestamp"
    private const val CACHE_FILE_NAME = "cached_custom_wallpaper.webp"

    val builtInWallpapers: List<BuiltInWallpaper> = listOf(
        BuiltInWallpaper(
            id = "medina",
            nameBn = "মদীনা মুনাওয়ারা",
            resId = R.drawable.img_islamic_header_medina,
            descriptionBn = "মসজিদুল নববীর প্রশান্তিময় গম্বুজ ও প্রাঙ্গণ"
        ),
        BuiltInWallpaper(
            id = "twilight_mosque",
            nameBn = "গোধূলি মসজিদ",
            resId = R.drawable.img_islamic_header_wallpaper,
            descriptionBn = "গোধূলির সোনালী আলোয় দ্যুতিময় মিনার ও গম্বুজ"
        ),
        BuiltInWallpaper(
            id = "ramadan_moon",
            nameBn = "রমজান ও চাঁদ",
            resId = R.drawable.img_ramadan_moon_bg,
            descriptionBn = "পবিত্র রমজানের জ্যোতির্ময় বাঁকা চাঁদ ও নীলাকাশ"
        ),
        BuiltInWallpaper(
            id = "islamic_arch",
            nameBn = "ইসলামিক আর্চ",
            resId = R.drawable.img_sehri_iftar_bg,
            descriptionBn = "ঐতিহ্যবাহী ইসলামিক স্থাপত্য শৈলীর তোরণ ও নকশা"
        )
    )

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .build()
    }

    private val _configState = MutableStateFlow(WallpaperConfigState())
    val configState: StateFlow<WallpaperConfigState> = _configState.asStateFlow()

    fun init(context: Context) {
        val prefs = getPrefs(context)
        val file = getCacheFile(context)
        val fileExists = file.exists() && file.length() > 0

        val isCustom = prefs.getBoolean(KEY_CUSTOM_ENABLED, false) && fileExists
        val customUrl = prefs.getString(KEY_CUSTOM_URL, "") ?: ""
        val builtinIdx = prefs.getInt(KEY_SELECTED_BUILTIN_IDX, 0).coerceIn(0, builtInWallpapers.size - 1)
        val sizeBytes = if (fileExists) file.length() else 0L
        val width = prefs.getInt(KEY_CACHED_WIDTH, 0)
        val height = prefs.getInt(KEY_CACHED_HEIGHT, 0)
        val timestamp = prefs.getLong(KEY_CACHED_TIMESTAMP, 0L)

        _configState.value = WallpaperConfigState(
            isCustomEnabled = isCustom,
            customUrl = customUrl,
            cachedFilePath = if (fileExists) file.absolutePath else null,
            cachedFileSizeBytes = sizeBytes,
            cachedWidth = width,
            cachedHeight = height,
            cachedTimestamp = timestamp,
            selectedBuiltInIndex = builtinIdx
        )
    }

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getCacheFile(context: Context): File {
        return File(context.filesDir, CACHE_FILE_NAME)
    }

    fun isCustomWallpaperActive(context: Context): Boolean {
        val file = getCacheFile(context)
        return _configState.value.isCustomEnabled && file.exists() && file.length() > 0
    }

    fun getActiveWallpaperFile(context: Context): File? {
        val file = getCacheFile(context)
        return if (_configState.value.isCustomEnabled && file.exists() && file.length() > 0) file else null
    }

    fun getSelectedBuiltInWallpaper(): BuiltInWallpaper {
        val idx = _configState.value.selectedBuiltInIndex.coerceIn(0, builtInWallpapers.size - 1)
        return builtInWallpapers[idx]
    }

    fun selectBuiltInWallpaper(context: Context, index: Int) {
        val safeIndex = index.coerceIn(0, builtInWallpapers.size - 1)
        val prefs = getPrefs(context)
        prefs.edit()
            .putInt(KEY_SELECTED_BUILTIN_IDX, safeIndex)
            .putBoolean(KEY_CUSTOM_ENABLED, false)
            .apply()

        // Keep app_ui_prefs synced for legacy compatibility
        context.getSharedPreferences("app_ui_prefs", Context.MODE_PRIVATE)
            .edit()
            .putInt("selected_header_wallpaper_idx", safeIndex)
            .apply()

        _configState.value = _configState.value.copy(
            isCustomEnabled = false,
            selectedBuiltInIndex = safeIndex
        )
    }

    fun setCustomWallpaperEnabled(context: Context, enabled: Boolean) {
        val file = getCacheFile(context)
        val canEnable = enabled && file.exists() && file.length() > 0
        getPrefs(context).edit().putBoolean(KEY_CUSTOM_ENABLED, canEnable).apply()
        _configState.value = _configState.value.copy(isCustomEnabled = canEnable)
    }

    fun cycleNextWallpaper(context: Context) {
        val state = _configState.value
        val file = getCacheFile(context)
        val hasCustom = file.exists() && file.length() > 0

        if (hasCustom) {
            // Cycle through Built-ins, then Custom
            if (!state.isCustomEnabled) {
                val nextIdx = state.selectedBuiltInIndex + 1
                if (nextIdx < builtInWallpapers.size) {
                    selectBuiltInWallpaper(context, nextIdx)
                } else {
                    // Switch to custom
                    setCustomWallpaperEnabled(context, true)
                }
            } else {
                // From custom back to first built-in
                selectBuiltInWallpaper(context, 0)
            }
        } else {
            // Cycle built-ins only
            val nextIdx = (state.selectedBuiltInIndex + 1) % builtInWallpapers.size
            selectBuiltInWallpaper(context, nextIdx)
        }
    }

    fun extractGoogleDriveFileId(url: String): String? {
        val trimmed = url.trim()
        val p1 = Regex("""/file/d/([a-zA-Z0-9_-]{20,})""").find(trimmed)?.groupValues?.getOrNull(1)
        if (!p1.isNullOrBlank()) return p1

        val p2 = Regex("""/d/([a-zA-Z0-9_-]{20,})""").find(trimmed)?.groupValues?.getOrNull(1)
        if (!p2.isNullOrBlank()) return p2

        val p3 = Regex("""[?&]id=([a-zA-Z0-9_-]{20,})""").find(trimmed)?.groupValues?.getOrNull(1)
        if (!p3.isNullOrBlank()) return p3

        if (trimmed.length in 25..55 && trimmed.matches(Regex("""[a-zA-Z0-9_-]+"""))) {
            return trimmed
        }
        return null
    }

    suspend fun downloadAndCacheWallpaper(
        context: Context,
        inputUrlOrId: String
    ): WallpaperDownloadResult = withContext(Dispatchers.IO) {
        val trimmed = inputUrlOrId.trim()
        if (trimmed.isBlank()) {
            return@withContext WallpaperDownloadResult.Error("দয়া করে একটি সঠিক গুগল ড্রাইভ লিঙ্ক বা ছবির লিঙ্ক প্রবেশ করান।")
        }

        val driveId = extractGoogleDriveFileId(trimmed)
        val candidateUrls = mutableListOf<String>()

        if (driveId != null) {
            candidateUrls.add("https://lh3.googleusercontent.com/d/$driveId=w1920-h1080")
            candidateUrls.add("https://lh3.googleusercontent.com/d/$driveId")
            candidateUrls.add("https://drive.google.com/thumbnail?id=$driveId&sz=w1920")
            candidateUrls.add("https://drive.google.com/uc?export=download&id=$driveId")
        } else if (trimmed.startsWith("http://") || trimmed.startsWith("https://")) {
            candidateUrls.add(trimmed)
        } else {
            return@withContext WallpaperDownloadResult.Error("লিঙ্কটি সঠিক গুগল ড্রাইভ বা ইমেজ ফরম্যাটে নয়।")
        }

        var downloadedBytes: ByteArray? = null
        var lastExceptionMessage: String? = null

        for (candidate in candidateUrls) {
            try {
                val request = Request.Builder()
                    .url(candidate)
                    .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 14; Mobile) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
                    .build()

                val response = httpClient.newCall(request).execute()
                if (response.isSuccessful) {
                    val body = response.body
                    if (body != null) {
                        val bytes = body.bytes()
                        // Ensure it's not a Google Drive HTML error/sign-in page
                        if (bytes.isNotEmpty() && !isHtmlContent(bytes)) {
                            downloadedBytes = bytes
                            response.close()
                            break
                        }
                    }
                }
                response.close()
            } catch (e: Exception) {
                lastExceptionMessage = e.localizedMessage
            }
        }

        if (downloadedBytes == null || downloadedBytes.isEmpty()) {
            val errorMsg = if (driveId != null) {
                "গুগল ড্রাইভ থেকে ছবিটি অ্যাক্সেস করা যায়নি। নিশ্চিত করুন ফাইলের শেয়ার অপশন 'Anyone with the link can view' (যে কেউ দেখতে পারে) করা আছে।"
            } else {
                "ছবিটি ডাউনলোড করা যায়নি: ${lastExceptionMessage ?: "সার্ভার রেসপন্স দেয়নি"}"
            }
            return@withContext WallpaperDownloadResult.Error(errorMsg)
        }

        // Decode bitmap and validate
        try {
            val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeByteArray(downloadedBytes, 0, downloadedBytes.size, boundsOptions)

            val origWidth = boundsOptions.outWidth
            val origHeight = boundsOptions.outHeight

            if (origWidth <= 0 || origHeight <= 0) {
                return@withContext WallpaperDownloadResult.Error("ডাউনলোডকৃত ফাইলটি কোনো বৈধ ছবি (JPEG/PNG/WebP) নয়।")
            }

            // Downsample if huge (> 1920 px) to guarantee zero memory lag
            val maxDimension = 1920
            var inSampleSize = 1
            if (origWidth > maxDimension || origHeight > maxDimension) {
                val halfHeight = origHeight / 2
                val halfWidth = origWidth / 2
                while ((halfHeight / inSampleSize) >= maxDimension || (halfWidth / inSampleSize) >= maxDimension) {
                    inSampleSize *= 2
                }
            }

            val decodeOptions = BitmapFactory.Options().apply {
                this.inSampleSize = inSampleSize
                inPreferredConfig = Bitmap.Config.ARGB_8888
            }

            val decodedBitmap = BitmapFactory.decodeByteArray(
                downloadedBytes,
                0,
                downloadedBytes.size,
                decodeOptions
            ) ?: return@withContext WallpaperDownloadResult.Error("ছবিটি ডিকোড করতে সমস্যা হয়েছে।")

            // Scale to optimal max width (1920) if still slightly larger
            val finalBitmap = if (decodedBitmap.width > maxDimension) {
                val scale = maxDimension.toFloat() / decodedBitmap.width.toFloat()
                val matrix = Matrix().apply { postScale(scale, scale) }
                val scaled = Bitmap.createBitmap(decodedBitmap, 0, 0, decodedBitmap.width, decodedBitmap.height, matrix, true)
                if (scaled != decodedBitmap) decodedBitmap.recycle()
                scaled
            } else {
                decodedBitmap
            }

            // Save to local cache file (instant 0ms loading from disk)
            val cacheFile = getCacheFile(context)
            FileOutputStream(cacheFile).use { fos ->
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
                fos.flush()
            }

            val finalWidth = finalBitmap.width
            val finalHeight = finalBitmap.height
            val finalSize = cacheFile.length()
            finalBitmap.recycle()

            // Save preferences
            val now = System.currentTimeMillis()
            getPrefs(context).edit()
                .putBoolean(KEY_CUSTOM_ENABLED, true)
                .putString(KEY_CUSTOM_URL, trimmed)
                .putLong(KEY_CACHED_FILE_SIZE, finalSize)
                .putInt(KEY_CACHED_WIDTH, finalWidth)
                .putInt(KEY_CACHED_HEIGHT, finalHeight)
                .putLong(KEY_CACHED_TIMESTAMP, now)
                .apply()

            _configState.value = _configState.value.copy(
                isCustomEnabled = true,
                customUrl = trimmed,
                cachedFilePath = cacheFile.absolutePath,
                cachedFileSizeBytes = finalSize,
                cachedWidth = finalWidth,
                cachedHeight = finalHeight,
                cachedTimestamp = now
            )

            return@withContext WallpaperDownloadResult.Success(
                sizeBytes = finalSize,
                width = finalWidth,
                height = finalHeight
            )
        } catch (e: Exception) {
            return@withContext WallpaperDownloadResult.Error("ক্যাশ ফাইলে সংরক্ষণ ব্যর্থ হয়েছে: ${e.localizedMessage}")
        }
    }

    fun clearCustomWallpaper(context: Context) {
        val file = getCacheFile(context)
        if (file.exists()) {
            file.delete()
        }
        getPrefs(context).edit()
            .putBoolean(KEY_CUSTOM_ENABLED, false)
            .remove(KEY_CUSTOM_URL)
            .remove(KEY_CACHED_FILE_SIZE)
            .remove(KEY_CACHED_WIDTH)
            .remove(KEY_CACHED_HEIGHT)
            .remove(KEY_CACHED_TIMESTAMP)
            .apply()

        _configState.value = _configState.value.copy(
            isCustomEnabled = false,
            customUrl = "",
            cachedFilePath = null,
            cachedFileSizeBytes = 0L,
            cachedWidth = 0,
            cachedHeight = 0,
            cachedTimestamp = 0L
        )
    }

    private fun isHtmlContent(bytes: ByteArray): Boolean {
        val checkLen = minOf(bytes.size, 512)
        val prefix = String(bytes, 0, checkLen, Charsets.UTF_8).lowercase()
        return prefix.contains("<!doctype html") || prefix.contains("<html") || prefix.contains("<head")
    }
}
