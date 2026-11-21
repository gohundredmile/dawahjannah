package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.example.data.model.AyahExplanation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

/**
 * Revamped 'Explain This Ayah' Vision OCR -> Local Quran Database Lookup Pipeline.
 *
 * Core Design Principle:
 * - Camera/AI NEVER fabricates Quranic Arabic, translations, or Tafsir.
 * - Vision AI is used strictly as a high-precision Arabic/Bangla/English OCR reader on the captured
 *   image (temperature = 0.0) to transcribe the visible Quranic lines verbatim.
 * - Every extracted text snippet is then deterministically matched against ALL 114 Surahs
 *   (6,236 Ayahs) inside the app's bundled local Quran database (`assets/quran/surah_1..114.json`),
 *   returning the exact verified Ayah details and authentic local Tafsir.
 */
class AyahScannerAiService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    fun getEffectiveApiKey(): String {
        val prefs = context.getSharedPreferences("dawah_settings", Context.MODE_PRIVATE)
        val userKey = prefs.getString("custom_gemini_api_key", null)?.trim()
        if (!userKey.isNullOrBlank() && !userKey.contains("AIzaSyDZWwThZha7c_tRfsSsQM-JgccgaRVHNd8")) {
            return userKey
        }
        val buildConfigKey = try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isNotBlank() && key != "your_api_key_here" && !key.contains("AIzaSyDZWwThZha7c_tRfsSsQM-JgccgaRVHNd8")) key else ""
        } catch (_: Throwable) {
            ""
        }
        return buildConfigKey
    }

    fun hasValidApiKey(): Boolean {
        val isOffline = try {
            com.example.data.repository.AppRepository(context).isOfflineMode()
        } catch (_: Exception) {
            false
        }
        if (isOffline) return false

        val key = getEffectiveApiKey()
        return key.isNotBlank() && key != "your_api_key_here"
    }

    fun saveUserApiKey(key: String) {
        val prefs = context.getSharedPreferences("dawah_settings", Context.MODE_PRIVATE)
        prefs.edit().putString("custom_gemini_api_key", key.trim()).apply()
    }

    fun isAiOnline(): Boolean = hasValidApiKey()

    /**
     * Analyzes a captured camera frame or gallery image:
     * 1. Validates bitmap lighting & contrast.
     * 2. Pre-warms the local 6,236-ayah Quran index.
     * 3. If online Vision AI key is configured, runs Gemini Vision OCR -> Local Quran DB lookup.
     * 4. Otherwise (default — ZERO API key needed!) or on Vision AI failure, runs real On-Device
     *    Arabic Tesseract OCR (with Dark-Mode screen auto-inversion + Otsu binarization) + ML Kit
     *    Latin OCR -> Local 6,236-Ayah Quran Database Lookup!
     */
    suspend fun analyzeQuranImage(
        bitmap: Bitmap,
        preferredSurah: Int? = null,
        preferredAyah: Int? = null,
        fullFrameBitmap: Bitmap? = null
    ): Result<AyahExplanation> = withContext(Dispatchers.IO) {
        val startTime = System.currentTimeMillis()

        // 1. Explicit Surah & Ayah selection bypasses OCR and loads straight from local Quran DB
        if (preferredSurah != null && preferredSurah in 1..114) {
            val ayahNum = (preferredAyah ?: 1).coerceAtLeast(1)
            val exact = LocalQuranAyahMatcher.loadAyahExplanationFromLocalDb(
                context = context,
                surahNumber = preferredSurah,
                ayahNumber = ayahNum,
                scanDurationMs = (System.currentTimeMillis() - startTime).coerceAtLeast(15L)
            )
            return@withContext Result.success(exact)
        }

        // 2. Validate image quality (pitch black / blank)
        val qualityErr = LocalQuranAyahScannerEngine.validateBitmapQuality(bitmap)
        if (qualityErr != null) {
            return@withContext Result.failure(Exception(qualityErr))
        }

        // Ensure local 6,236-ayah index is ready in RAM
        LocalQuranAyahMatcher.ensureReady(context)

        // 3. Optional Vision AI OCR boost (only when user/build has a valid Gemini API key & is online)
        val apiKey = getEffectiveApiKey()
        val isOfflineMode = try {
            com.example.data.repository.AppRepository(context).isOfflineMode()
        } catch (_: Exception) {
            false
        }

        if (!isOfflineMode && apiKey.isNotBlank() && apiKey != "your_api_key_here") {
            val ocrExtraction = extractQuranCluesWithVisionOcr(bitmap, apiKey)
            if (ocrExtraction.isSuccess) {
                val clues = ocrExtraction.getOrThrow()
                val localLookup = LocalQuranAyahMatcher.matchCluesInLocalDatabase(
                    context = context,
                    clues = clues,
                    startTimeMs = startTime
                )
                if (localLookup.isSuccess) {
                    return@withContext localLookup
                }
            }
        }

        // 4. Real On-Device Arabic Tesseract OCR + ML Kit Latin OCR -> Local 6,236-Ayah Quran DB Lookup
        // Works completely out-of-the-box WITHOUT any API key on laptop screens & printed Mushafs!
        val onDeviceLookup = LocalQuranAyahScannerEngine.scanImage(
            bitmap = bitmap,
            context = context,
            fullFrameBitmap = fullFrameBitmap
        )
        if (onDeviceLookup.isSuccess) {
            return@withContext onDeviceLookup
        }

        val finalErrorMessage = onDeviceLookup.exceptionOrNull()?.localizedMessage
            ?: "ছবি থেকে কুরআনের আয়াত স্পষ্টভাবে পড়া যায়নি। অনুগ্রহ করে আয়াতের লেখাটি বড় করে ফ্রেমের মাঝখানে রেখে স্ক্রিনে ট্যাপ করে ফোকাস করুন এবং আবার স্ক্যান করুন।"

        Result.failure(Exception(finalErrorMessage))
    }

    /**
     * Calls Gemini Vision STRICTLY as a verbatim Arabic/Bangla/English OCR transcriber for Quran pages.
     * It does NOT ask the model to invent translations or tafsir; all Ayah details and Tafsir are
     * subsequently fetched from the local in-app Quran database (`assets/quran/surah_1..114.json`).
     */
    private fun extractQuranCluesWithVisionOcr(
        bitmap: Bitmap,
        apiKey: String
    ): Result<ExtractedQuranOcrClues> {
        try {
            // Preserve crisp Arabic dots (nuqat) and letter shapes with 1600px max dimension & 92 JPEG quality
            val maxDim = 1600
            val scaledBitmap = if (bitmap.width > maxDim || bitmap.height > maxDim) {
                val scale = maxDim.toFloat() / maxOf(bitmap.width, bitmap.height)
                val newW = (bitmap.width * scale).toInt().coerceAtLeast(1)
                val newH = (bitmap.height * scale).toInt().coerceAtLeast(1)
                Bitmap.createScaledBitmap(bitmap, newW, newH, true)
            } else {
                bitmap
            }

            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 92, outputStream)
            val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

            val prompt = """
                You are a high-precision Quran Optical Character Recognition (OCR) transcriber.
                Your ONLY task is to accurately read and transcribe the Quranic text visible in this photo (especially inside the center/focused area) so our app can look it up in our local Quran database.

                CRITICAL RULES:
                1. Read the ACTUAL Arabic words printed in the image carefully letter-by-letter. Support Madinah/Uthmani, IndoPak/Hafezi/Calcutta, and modern Naskh fonts.
                2. Do NOT guess or fabricate words that are not in the image.
                3. Do NOT write translations or tafsir — only transcribe what is visually printed in the image.
                4. If multiple ayahs are visible, list the main/center focused ayah's Arabic lines FIRST in `arabicLines`.
                5. If a Surah header (e.g. سورة الحشر, সূরা আল-হাশর, Surah Al-Hashr), ayah end numbers (e.g. ٢٢, 22, 255), or printed Bangla/English translation lines are visible, include them in the fields below.
                6. Only set `suspectedSurahNumber` and `suspectedAyahNumber` if you are genuinely confident; leave them as 0 if unsure.

                Return ONLY raw JSON (no markdown backticks):
                {
                  "hasQuranText": true,
                  "arabicLines": ["first visible Arabic line verbatim", "second visible Arabic line verbatim"],
                  "surahHeaderText": "",
                  "visibleAyahNumbers": [22],
                  "otherVisibleText": "any visible Bangla or English text or reference",
                  "suspectedSurahNumber": 0,
                  "suspectedAyahNumber": 0
                }

                If the photo contains NO readable text at all, return:
                {"hasQuranText": false, "arabicLines": [], "surahHeaderText": "", "visibleAyahNumbers": [], "otherVisibleText": "", "suspectedSurahNumber": 0, "suspectedAyahNumber": 0}
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                            put(JSONObject().apply {
                                put("inlineData", JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64Image)
                                })
                            })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.0)
                    put("responseMimeType", "application/json")
                })
            }

            val models = listOf("gemini-2.5-flash", "gemini-2.5-flash-lite", "gemini-2.0-flash")
            var lastErrorMsg = "ভিশন OCR সংযোগে সমস্যা হয়েছে।"

            for (modelName in models) {
                try {
                    val request = Request.Builder()
                        .url("https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey")
                        .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                        .build()

                    val resp = httpClient.newCall(request).execute()
                    val respBody = resp.body?.string() ?: ""

                    if (!resp.isSuccessful) {
                        val errDetail = try {
                            JSONObject(respBody).optJSONObject("error")?.optString("message") ?: "HTTP ${resp.code}"
                        } catch (_: Exception) {
                            "HTTP ${resp.code}"
                        }
                        lastErrorMsg = "ভিশন OCR ত্রুটি ($errDetail)"
                        continue
                    }

                    val rootObj = JSONObject(respBody)
                    val candidates = rootObj.optJSONArray("candidates") ?: continue
                    if (candidates.length() == 0) continue

                    val parts = candidates.getJSONObject(0)
                        .optJSONObject("content")
                        ?.optJSONArray("parts") ?: continue
                    if (parts.length() == 0) continue

                    var textResp = parts.getJSONObject(0).optString("text", "").trim()
                    if (textResp.startsWith("```json")) {
                        textResp = textResp.removePrefix("```json").removeSuffix("```").trim()
                    } else if (textResp.startsWith("```")) {
                        textResp = textResp.removePrefix("```").removeSuffix("```").trim()
                    }

                    val parsed = JSONObject(textResp)
                    val hasText = parsed.optBoolean("hasQuranText", true)

                    val arabicLines = ArrayList<String>()
                    val linesArr = parsed.optJSONArray("arabicLines")
                    if (linesArr != null) {
                        for (i in 0 until linesArr.length()) {
                            val line = linesArr.optString(i, "").trim()
                            if (line.isNotBlank()) arabicLines.add(line)
                        }
                    }

                    val visibleNums = ArrayList<Int>()
                    val numsArr = parsed.optJSONArray("visibleAyahNumbers")
                    if (numsArr != null) {
                        for (i in 0 until numsArr.length()) {
                            val n = numsArr.optInt(i, 0)
                            if (n in 1..286) visibleNums.add(n)
                        }
                    }

                    val surahHeader = parsed.optString("surahHeaderText", "").trim()
                    val otherVisible = parsed.optString("otherVisibleText", "").trim()
                    val suspectedSurah = parsed.optInt("suspectedSurahNumber", 0).takeIf { it in 1..114 }
                    val suspectedAyah = parsed.optInt("suspectedAyahNumber", 0).takeIf { it in 1..286 }

                    if (!hasText && arabicLines.isEmpty() && otherVisible.isBlank() && suspectedSurah == null) {
                        return Result.failure(
                            Exception("ছবিতে কুরআনের কোনো স্পষ্ট লেখা শনাক্ত করা যায়নি। অনুগ্রহ করে আয়াতের লেখাটি ফ্রেমের মাঝখানে পরিষ্কারভাবে ধরে পুনরায় স্ক্যান করুন।")
                        )
                    }

                    val combinedRaw = buildString {
                        if (arabicLines.isNotEmpty()) append(arabicLines.joinToString("\n"))
                        if (otherVisible.isNotBlank()) {
                            if (isNotEmpty()) append("\n")
                            append(otherVisible)
                        }
                    }.trim()

                    return Result.success(
                        ExtractedQuranOcrClues(
                            arabicLines = arabicLines,
                            otherText = otherVisible,
                            surahHeaderHint = surahHeader,
                            visibleAyahNumbers = visibleNums,
                            suspectedSurahNumber = suspectedSurah,
                            suspectedAyahNumber = suspectedAyah,
                            rawCombinedText = combinedRaw,
                            extractionEngineLabelBn = "ক্যামেরা ভিশন OCR → লোকাল কুরআন ডাটাবেস"
                        )
                    )
                } catch (e: Exception) {
                    lastErrorMsg = e.localizedMessage ?: lastErrorMsg
                }
            }

            return Result.failure(Exception(lastErrorMsg))
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}
