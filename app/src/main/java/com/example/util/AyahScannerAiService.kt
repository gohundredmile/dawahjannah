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
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key != "your_api_key_here") key else ""
        } catch (_: Exception) {
            ""
        }
    }

    fun saveUserApiKey(key: String) {
        val prefs = context.getSharedPreferences("dawah_settings", Context.MODE_PRIVATE)
        prefs.edit().putString("custom_gemini_api_key", key.trim()).apply()
    }

    fun isAiOnline(): Boolean {
        val key = getEffectiveApiKey()
        return key.isNotBlank() && key != "your_api_key_here"
    }

    /**
     * Analyzes a Quran page or Ayah snapshot captured from Camera or picked from Gallery.
     */
    suspend fun analyzeQuranImage(bitmap: Bitmap): Result<AyahExplanation> = withContext(Dispatchers.IO) {
        val apiKey = getEffectiveApiKey()

        if (apiKey.isBlank() || apiKey == "your_api_key_here") {
            return@withContext Result.failure(
                Exception("এআই ভিশন সক্রিয় করতে ইন্টারনেট বা API কী প্রয়োজন। উপরের কী (Key) আইকনে ট্যাপ করে API Key দিন অথবা নিচের নমুনা আয়াতসমূহ নির্বাচন করুন।")
            )
        }

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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            val base64Image = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

            val prompt = """
                You are an Islamic scholar, Hafiz, and Quran specialist.
                Carefully analyze this camera/gallery image.
                Determine if this image contains any readable Arabic Quran verse(s) (Ayah) or a Quran page.

                If the image DOES NOT contain an Arabic Quran Ayah (for example: a face, desk, blank screen, keyboard, wall, random non-Quranic text, or unreadable blur), return this exact JSON:
                {
                  "isQuranAyah": false,
                  "message": "কোনো স্পষ্ট কুরআন আয়াত শনাক্ত হয়নি। অনুগ্রহ করে পর্যাপ্ত আলোতে কুরআন শরীফের আরবি আয়াতের উপর ক্যামেরা স্থির রাখুন।"
                }

                If the image DOES contain one or more Quranic verses, identify the primary Ayah visible in the frame (surah and ayah number), and provide a comprehensive explanation in authentic Bengali and English.
                Return this exact JSON:
                {
                  "isQuranAyah": true,
                  "surahNumber": 1,
                  "ayahNumber": 1,
                  "surahNameArabic": "سورة الفاتحة",
                  "surahNameBangla": "সূরা আল-ফাতিহা",
                  "surahNameEnglish": "Surah Al-Fatihah",
                  "revelationTypeBn": "মাক্কী",
                  "totalAyahsInSurah": 7,
                  "arabicText": "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                  "transliterationBn": "বিসমিল্লাহির রাহমানির রাহীম",
                  "banglaTranslation": "পরম করুণাময় অসীম দয়ালু আল্লাহর নামে শুরু করছি।",
                  "englishTranslation": "In the name of Allah, the Entirely Merciful, the Especially Merciful.",
                  "wordByWord": [
                    {"arabicWord": "بِسْمِ", "bengaliMeaning": "নামে", "englishMeaning": "In the name", "grammarNote": "জার-মাজরুর"},
                    {"arabicWord": "اللَّهِ", "bengaliMeaning": "আল্লাহর", "englishMeaning": "of Allah", "grammarNote": "মুযাফ ইলাইহি"}
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

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.6-flash:generateContent?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            var response = httpClient.newCall(request).execute()
            if (!response.isSuccessful && response.code == 404) {
                // Fallback to gemini-3.5-flash
                val fallbackRequest = Request.Builder()
                    .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
                    .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                    .build()
                response = httpClient.newCall(fallbackRequest).execute()
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

            val cleanJson = textContent.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()
            val parsedJson = JSONObject(cleanJson)

            val isQuranAyah = parsedJson.optBoolean("isQuranAyah", true)
            if (!isQuranAyah) {
                val notFoundMsg = parsedJson.optString(
                    "message",
                    "কোনো স্পষ্ট কুরআন আয়াত শনাক্ত হয়নি। অনুগ্রহ করে কুরআন শরীফের আরবি আয়াতের উপর ক্যামেরা সোজা রাখুন।"
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
            Result.failure(Exception(e.localizedMessage ?: "স্ক্যান করতে সমস্যা হয়েছে। পুনরায় চেষ্টা করুন।"))
        }
    }
}
