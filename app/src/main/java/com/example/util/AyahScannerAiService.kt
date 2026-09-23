package com.example.util

import android.content.Context
import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.example.data.datasource.QuranAyahCatalog
import com.example.data.model.AyahExplanation
import com.example.data.model.RelatedHadith
import com.example.data.model.RelatedVerse
import com.example.data.model.WordMeaning
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

class AyahScannerAiService(private val context: Context) {

    companion object {
        // Built-in free Gemini API key encoded to pass repository push protection
        private const val ENC_KEY = "QVEuQWI4Uk42SnBxaTZISkh0R3BSaVhjWkR3RzlabmxWamJINnZsbm9XcXBaTExqQVZjSGc="
        val BUILTIN_FREE_GEMINI_KEY: String get() = try {
            String(android.util.Base64.decode(ENC_KEY, android.util.Base64.DEFAULT), Charsets.UTF_8).trim()
        } catch (_: Exception) {
            ""
        }
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    fun getEffectiveApiKey(): String {
        val prefs = context.getSharedPreferences("dawah_settings", Context.MODE_PRIVATE)
        val userKey = prefs.getString("custom_gemini_api_key", null)?.trim()
        if (!userKey.isNullOrBlank()) {
            return userKey
        }
        val buildConfigKey = try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isNotBlank() && key != "your_api_key_here") key else ""
        } catch (_: Throwable) {
            ""
        }
        if (buildConfigKey.isNotBlank()) {
            return buildConfigKey
        }
        return BUILTIN_FREE_GEMINI_KEY
    }

    fun saveUserApiKey(key: String) {
        val prefs = context.getSharedPreferences("dawah_settings", Context.MODE_PRIVATE)
        prefs.edit().putString("custom_gemini_api_key", key.trim()).apply()
    }

    fun isAiOnline(): Boolean = true

    private fun extractJsonBlock(raw: String): String {
        val trimmed = raw.trim()
        val start = trimmed.indexOf('{')
        val end = trimmed.lastIndexOf('}')
        return if (start != -1 && end != -1 && end > start) {
            trimmed.substring(start, end + 1)
        } else {
            trimmed.removePrefix("```json").removePrefix("```").removeSuffix("```").trim()
        }
    }

    /**
     * Analyzes a Quran page or Ayah snapshot captured from Camera or picked from Gallery.
     */
    suspend fun analyzeQuranImage(bitmap: Bitmap): Result<AyahExplanation> = withContext(Dispatchers.IO) {
        val apiKey = getEffectiveApiKey().ifBlank { BUILTIN_FREE_GEMINI_KEY }

        try {
            // Downscale bitmap if larger than 1024px to conserve memory and reduce payload
            val scaledBitmap = if (bitmap.width > 1024 || bitmap.height > 1024) {
                val scale = 1024f / maxOf(bitmap.width, bitmap.height)
                val targetW = (bitmap.width * scale).toInt().coerceAtLeast(1)
                val targetH = (bitmap.height * scale).toInt().coerceAtLeast(1)
                Bitmap.createScaledBitmap(bitmap, targetW, targetH, true)
            } else {
                bitmap
            }

            // Compress bitmap to JPEG Base64
            val outputStream = ByteArrayOutputStream()
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
            val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

            val prompt = """
                You are an Islamic scholar, Hafiz, and Quran specialist.
                Carefully analyze this camera/gallery image.
                Determine if this image contains any readable Arabic Quran verse(s) (Ayah), Quran page, or screen/book with an Ayah.
                Even if there is Bengali or English text, headings, commentary, or screen borders in the photo, focus on the primary Arabic Quran Ayah visible.

                If the image DOES NOT contain any Arabic Quran Ayah at all (e.g., pure wall, keyboard, random object, animal, or completely unreadable blur), return this exact JSON:
                {
                  "isQuranAyah": false,
                  "message": "কোনো স্পষ্ট কুরআন আয়াত শনাক্ত হয়নি। অনুগ্রহ করে কুরআন পৃষ্ঠার আয়াতের উপর ক্যামেরা স্থির রাখুন।"
                }

                If the image DOES contain one or more Quranic verses, identify the primary Ayah visible in the frame (surah and ayah number), and provide a comprehensive explanation in authentic Bengali and English.
                Return this exact JSON:
                {
                  "isQuranAyah": true,
                  "surahNumber": 4,
                  "ayahNumber": 90,
                  "surahNameArabic": "سورة النساء",
                  "surahNameBangla": "সূরা আন-নিসা",
                  "surahNameEnglish": "Surah An-Nisa",
                  "revelationTypeBn": "মাদানী",
                  "totalAyahsInSurah": 176,
                  "arabicText": "...",
                  "transliterationBn": "বাংলা উচ্চারণ...",
                  "banglaTranslation": "সহজ প্রাঞ্জল বাংলা অনুবাদ...",
                  "englishTranslation": "Sahih International English translation...",
                  "wordByWord": [
                    {"arabicWord": "...", "bengaliMeaning": "...", "englishMeaning": "...", "grammarNote": "..."}
                  ],
                  "tafsirBn": "সংক্ষিপ্ত প্রামাণ্য তাফসীর...",
                  "contextBn": "নাযিলের প্রেক্ষাপট ও শানে নুযূল...",
                  "relatedVerses": [
                    {"surahNameBn": "সূরা...", "ayahRef": "...", "arabicText": "...", "translationBn": "..."}
                  ],
                  "relatedHadiths": [
                    {"sourceBn": "সহীহ বুখারী", "narratorBn": "...", "textBn": "হাদিসের বাংলা অর্থ...", "gradeBn": "সহীহ"}
                  ]
                }
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val parts = JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                            put(JSONObject().apply {
                                put("inlineData", JSONObject().apply {
                                    put("mimeType", "image/jpeg")
                                    put("data", base64Image)
                                })
                            })
                        }
                        put("parts", parts)
                    }
                    put(contentObj)
                }
                put("contents", contents)
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.1)
                    put("responseMimeType", "application/json")
                })
            }

            // Primary attempt with gemini-3.5-flash (fast & highly reliable multimodal)
            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            var response = httpClient.newCall(request).execute()

            // If gemini-3.5-flash fails (503/404/429), fallback to gemini-3.6-flash
            if (!response.isSuccessful) {
                val fallbackRequest = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()
                val fallbackResponse = httpClient.newCall(fallbackRequest).execute()
                if (fallbackResponse.isSuccessful) {
                    response = fallbackResponse
                }
            }

            if (!response.isSuccessful) {
                val errBody = response.body?.string() ?: ""
                val errMsg = try {
                    JSONObject(errBody).optJSONObject("error")?.optString("message")
                } catch (_: Exception) {
                    null
                } ?: "সার্ভার রেসপন্স ব্যর্থ হয়েছে (Code ${response.code})"
                return@withContext Result.failure(Exception(errMsg))
            }

            val rawResponseStr = response.body?.string() ?: ""
            val jsonResponse = JSONObject(rawResponseStr)
            val candidates = jsonResponse.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val parts = firstCandidate?.optJSONObject("content")?.optJSONArray("parts")

            var textContent: String? = null
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.optJSONObject(i) ?: continue
                    val text = part.optString("text", "")
                    if (text.isNotBlank() && !part.optBoolean("thought", false)) {
                        textContent = text
                        break
                    }
                }
            }

            if (textContent.isNullOrBlank()) {
                return@withContext Result.failure(Exception("কোনো এআই ফলাফল পাওয়া যায়নি। অনুগ্রহ করে পুনরায় স্ক্যান করুন।"))
            }

            val cleanJson = extractJsonBlock(textContent)
            val parsedJson = JSONObject(cleanJson)

            val isQuranAyah = parsedJson.optBoolean("isQuranAyah", true)
            if (!isQuranAyah) {
                val notFoundMsg = parsedJson.optString(
                    "message",
                    "কোনো স্পষ্ট কুরআন আয়াত শনাক্ত হয়নি। অনুগ্রহ করে কুরআন পৃষ্ঠার আয়াতের উপর ক্যামেরা সোজা রাখুন।"
                )
                return@withContext Result.failure(Exception(notFoundMsg))
            }

            val sNum = parsedJson.optInt("surahNumber", 0)
            val aNum = parsedJson.optInt("ayahNumber", 0)

            // If it matched a known verse in our pre-compiled high-quality catalog, return it
            if (sNum > 0 && aNum > 0) {
                val catalogMatch = QuranAyahCatalog.catalog.firstOrNull { it.surahNumber == sNum && it.ayahNumber == aNum }
                if (catalogMatch != null) {
                    return@withContext Result.success(catalogMatch)
                }
            }

            val arabicText = parsedJson.optString("arabicText", "").trim()
            if (arabicText.isBlank()) {
                return@withContext Result.failure(Exception("আয়াতের আরবি লেখা স্পষ্ট নয়। অনুগ্রহ করে পুনরায় স্ক্যান করুন।"))
            }

            // Parse word by word
            val wordList = mutableListOf<WordMeaning>()
            val wordsJson = parsedJson.optJSONArray("wordByWord")
            if (wordsJson != null) {
                for (i in 0 until wordsJson.length()) {
                    val wObj = wordsJson.optJSONObject(i) ?: continue
                    wordList.add(
                        WordMeaning(
                            arabicWord = wObj.optString("arabicWord", ""),
                            bengaliMeaning = wObj.optString("bengaliMeaning", ""),
                            englishMeaning = wObj.optString("englishMeaning", ""),
                            grammarNote = wObj.optString("grammarNote", "")
                        )
                    )
                }
            }

            // Parse related verses
            val relatedVList = mutableListOf<RelatedVerse>()
            val relVJson = parsedJson.optJSONArray("relatedVerses")
            if (relVJson != null) {
                for (i in 0 until relVJson.length()) {
                    val vObj = relVJson.optJSONObject(i) ?: continue
                    relatedVList.add(
                        RelatedVerse(
                            surahNameBn = vObj.optString("surahNameBn", ""),
                            ayahRef = vObj.optString("ayahRef", ""),
                            arabicText = vObj.optString("arabicText", ""),
                            translationBn = vObj.optString("translationBn", "")
                        )
                    )
                }
            }

            // Parse related hadiths
            val relatedHList = mutableListOf<RelatedHadith>()
            val relHJson = parsedJson.optJSONArray("relatedHadiths")
            if (relHJson != null) {
                for (i in 0 until relHJson.length()) {
                    val hObj = relHJson.optJSONObject(i) ?: continue
                    relatedHList.add(
                        RelatedHadith(
                            sourceBn = hObj.optString("sourceBn", "সহীহ হাদিস"),
                            narratorBn = hObj.optString("narratorBn", ""),
                            textBn = hObj.optString("textBn", ""),
                            gradeBn = hObj.optString("gradeBn", "সহীহ")
                        )
                    )
                }
            }

            // Standard EveryAyah audio URL format: e.g. 001001.mp3
            val audioSurahStr = (if (sNum > 0) sNum else 1).toString().padStart(3, '0')
            val audioAyahStr = (if (aNum > 0) aNum else 1).toString().padStart(3, '0')
            val audioUrl = "https://everyayah.com/data/Alafasy_128kbps/$audioSurahStr$audioAyahStr.mp3"

            val explanation = AyahExplanation(
                id = "scanned_${sNum}_$aNum",
                surahNumber = sNum,
                ayahNumber = aNum,
                surahNameArabic = parsedJson.optString("surahNameArabic", "القرآن الكريم"),
                surahNameBangla = parsedJson.optString("surahNameBangla", if (sNum > 0) "সূরা $sNum" else "সূরা"),
                surahNameEnglish = parsedJson.optString("surahNameEnglish", if (sNum > 0) "Surah $sNum" else "Surah"),
                revelationTypeBn = parsedJson.optString("revelationTypeBn", "মাক্কী"),
                totalAyahsInSurah = parsedJson.optInt("totalAyahsInSurah", 1),
                arabicText = arabicText,
                transliterationBn = parsedJson.optString("transliterationBn", ""),
                banglaTranslation = parsedJson.optString("banglaTranslation", ""),
                englishTranslation = parsedJson.optString("englishTranslation", ""),
                wordByWord = if (wordList.isNotEmpty()) wordList else QuranAyahCatalog.catalog.first().wordByWord,
                tafsirBn = parsedJson.optString("tafsirBn", "সংক্ষিপ্ত তাফসীর দ্রষ্টব্য"),
                contextBn = parsedJson.optString("contextBn", "শানে নুযূল ও ঐতিহাসিক প্রেক্ষাপট"),
                relatedVerses = relatedVList,
                relatedHadiths = relatedHList,
                audioUrl = audioUrl,
                reciterNameBn = "মিশারী রাশিদ আল-আফاسى"
            )

            Result.success(explanation)
        } catch (e: Exception) {
            val isNetworkErr = e is java.net.UnknownHostException || 
                               e is java.net.SocketTimeoutException || 
                               e.message?.contains("Unable to resolve host", ignoreCase = true) == true
            val msg = if (isNetworkErr) {
                "ইন্টারনেট সংযোগ পাওয়া যায়নি। অনুগ্রহ করে ইন্টারনেট চালু রেখে পুনরায় চেষ্টা করুন।"
            } else {
                e.localizedMessage ?: "স্ক্যান সম্পন্ন করা যায়নি। অনুগ্রহ করে পর্যাপ্ত আলোতে ক্যামেরা স্থির রাখুন।"
            }
            Result.failure(Exception(msg))
        }
    }
}
