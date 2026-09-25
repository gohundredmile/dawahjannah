package com.example.util

import android.content.Context
import com.example.BuildConfig
import com.example.data.datasource.SmartQuranCatalog
import com.example.data.model.SemanticQuranAyah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class SmartQuranSearchResult(
    val query: String,
    val interpretedIntentBn: String,
    val ayahs: List<SemanticQuranAyah>,
    val isAiEnhanced: Boolean = false
)

class SmartQuranSearchAiService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    /**
     * Executes semantic Quran search. Combines local semantic graph with Gemini AI semantic expansion.
     */
    suspend fun search(rawQuery: String): SmartQuranSearchResult = withContext(Dispatchers.IO) {
        val query = rawQuery.trim()
        if (query.isBlank()) {
            return@withContext SmartQuranSearchResult(
                query = "",
                interpretedIntentBn = "যেকোনো আবেগ, জীবনের সংকট বা ভাবার্থ লিখে অনুসন্ধান করুন",
                ayahs = SmartQuranCatalog.catalog.take(4),
                isAiEnhanced = false
            )
        }

        // Step 1: Intelligent Offline Semantic Scoring
        val localMatches = rankLocalCatalog(query)
        val interpretedIntent = generateSemanticIntentSummary(query, localMatches.firstOrNull())

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // If no API key or placeholder, return high-precision local semantic results
        if (apiKey.isBlank() || apiKey == "your_api_key_here") {
            return@withContext SmartQuranSearchResult(
                query = query,
                interpretedIntentBn = interpretedIntent,
                ayahs = if (localMatches.isNotEmpty()) localMatches else SmartQuranCatalog.catalog.take(3),
                isAiEnhanced = false
            )
        }

        // Step 2: Try Online Gemini Semantic Expansion
        try {
            val aiResult = callGeminiSemanticSearch(query, apiKey)
            if (aiResult != null && aiResult.ayahs.isNotEmpty()) {
                // Merge AI results with local matches avoiding duplicates by id or surah:ayah
                val existingKeys = aiResult.ayahs.map { "${it.surahNumber}:${it.ayahNumber}" }.toSet()
                val merged = aiResult.ayahs.toMutableList()
                localMatches.forEach { local ->
                    val key = "${local.surahNumber}:${local.ayahNumber}"
                    if (key !in existingKeys) {
                        merged.add(local)
                    }
                }
                return@withContext SmartQuranSearchResult(
                    query = query,
                    interpretedIntentBn = aiResult.interpretedIntentBn.ifBlank { interpretedIntent },
                    ayahs = merged,
                    isAiEnhanced = true
                )
            }
        } catch (_: Exception) {
            // Fallback gracefully to high-precision local matches
        }

        return@withContext SmartQuranSearchResult(
            query = query,
            interpretedIntentBn = interpretedIntent,
            ayahs = if (localMatches.isNotEmpty()) localMatches else SmartQuranCatalog.catalog.take(3),
            isAiEnhanced = false
        )
    }

    /**
     * Scores and ranks the catalog using semantic topic mappings and multilingual fuzzy tokens.
     */
    private fun rankLocalCatalog(query: String): List<SemanticQuranAyah> {
        val normalized = query.lowercase()
        val tokens = normalized.split(Regex("[\\s,?.!-]+")).filter { it.length > 1 }

        val scoredList = SmartQuranCatalog.catalog.map { ayah ->
            var score = 0

            // 1. Check direct semantic topic mappings
            if (isHopeQuery(normalized) && (ayah.primaryTopicEn.contains("hope", true) || ayah.primaryTopicBn.contains("হতাশ"))) {
                score += 150
            }
            if (isForgivenessQuery(normalized) && (ayah.primaryTopicEn.contains("forgiveness", true) || ayah.primaryTopicBn.contains("ক্ষমা"))) {
                score += 150
            }
            if (isAngerQuery(normalized) && (ayah.primaryTopicEn.contains("anger", true) || ayah.primaryTopicBn.contains("রাগ"))) {
                score += 150
            }
            if (isPovertyQuery(normalized) && (ayah.primaryTopicEn.contains("poverty", true) || ayah.primaryTopicBn.contains("দারিদ্র্য") || ayah.primaryTopicBn.contains("রিজিক"))) {
                score += 150
            }
            if (isParentsQuery(normalized) && (ayah.primaryTopicEn.contains("parents", true) || ayah.primaryTopicBn.contains("পিতা") || ayah.primaryTopicBn.contains("মা"))) {
                score += 150
            }
            if (isMarriageQuery(normalized) && (ayah.primaryTopicEn.contains("marriage", true) || ayah.primaryTopicBn.contains("দাম্পত্য") || ayah.primaryTopicBn.contains("স্ত্রী"))) {
                score += 150
            }
            if (isHardTimesQuery(normalized) && (ayah.primaryTopicEn.contains("hard", true) || ayah.primaryTopicBn.contains("কঠিন") || ayah.primaryTopicBn.contains("কষ্ট") || ayah.primaryTopicBn.contains("ধৈর্য"))) {
                score += 150
            }
            if (isPeaceQuery(normalized) && (ayah.primaryTopicEn.contains("peace", true) || ayah.primaryTopicBn.contains("শান্তি") || ayah.primaryTopicBn.contains("অস্থিরতা"))) {
                score += 150
            }
            if (isGratitudeQuery(normalized) && (ayah.primaryTopicEn.contains("gratitude", true) || ayah.primaryTopicBn.contains("কৃতজ্ঞতা") || ayah.primaryTopicBn.contains("শুকরিয়া"))) {
                score += 150
            }

            // 2. Token overlap with text fields
            tokens.forEach { token ->
                if (ayah.primaryTopicEn.lowercase().contains(token)) score += 30
                if (ayah.primaryTopicBn.lowercase().contains(token)) score += 30
                if (ayah.surahNameBn.lowercase().contains(token)) score += 20
                if (ayah.surahNameEn.lowercase().contains(token)) score += 20
                if (ayah.banglaTranslation.lowercase().contains(token)) score += 15
                if (ayah.englishTranslation.lowercase().contains(token)) score += 15
                if (ayah.divineWisdomBn.lowercase().contains(token)) score += 25
                if (ayah.tafsirSummaryBn.lowercase().contains(token)) score += 15
                ayah.relatedThemes.forEach { theme ->
                    if (theme.lowercase().contains(token)) score += 20
                }
            }

            Pair(ayah, score)
        }

        return scoredList
            .filter { it.second > 0 }
            .sortedByDescending { it.second }
            .map { it.first }
    }

    private fun isHopeQuery(q: String) = q.contains("hope") || q.contains("despair") || q.contains("give up") || q.contains("হতাশ") || q.contains("নিরাশ") || q.contains("আশা")
    private fun isForgivenessQuery(q: String) = q.contains("forgiv") || q.contains("pardon") || q.contains("ক্ষমা") || q.contains("মাফ") || q.contains("গুনাহ") || q.contains("তওবা") || q.contains("ইস্তিগফার")
    private fun isAngerQuery(q: String) = q.contains("anger") || q.contains("angry") || q.contains("rage") || q.contains("রাগ") || q.contains("ক্রোধ") || q.contains("মেজাজ")
    private fun isPovertyQuery(q: String) = q.contains("poverty") || q.contains("poor") || q.contains("money") || q.contains("wealth") || q.contains("দারিদ্র্য") || q.contains("টাকা") || q.contains("অভাব") || q.contains("রিজিক")
    private fun isParentsQuery(q: String) = q.contains("parent") || q.contains("mother") || q.contains("father") || q.contains("পিতা") || q.contains("মাতা") || q.contains("বাবা") || q.contains("মা")
    private fun isMarriageQuery(q: String) = q.contains("marriage") || q.contains("spouse") || q.contains("wife") || q.contains("husband") || q.contains("দাম্পত্য") || q.contains("বিয়ে") || q.contains("সংসার") || q.contains("স্ত্রী") || q.contains("স্বামী")
    private fun isHardTimesQuery(q: String) = q.contains("hard") || q.contains("trial") || q.contains("difficult") || q.contains("suffering") || q.contains("pain") || q.contains("কঠিন") || q.contains("বিপদ") || q.contains("কষ্ট") || q.contains("ধৈর্য") || q.contains("মুসিবত")
    private fun isPeaceQuery(q: String) = q.contains("peace") || q.contains("anxiety") || q.contains("stress") || q.contains("শান্তি") || q.contains("অস্থিরতা") || q.contains("উদ্বেগ") || q.contains("হৃদয়")
    private fun isGratitudeQuery(q: String) = q.contains("gratitude") || q.contains("thank") || q.contains("শুকর") || q.contains("কৃতজ্ঞতা") || q.contains("আলহামদুলিল্লাহ")

    private fun generateSemanticIntentSummary(query: String, topAyah: SemanticQuranAyah?): String {
        return when {
            isHopeQuery(query) -> "কুরআনিক দিকদর্শন: মানবজীবনে হতাশা ও নৈরাশ্যকে দূর করে আল্লাহর অসীম রহমতের ওপর অবিচল আশা রাখার আহ্বান।"
            isForgivenessQuery(query) -> "কুরআনিক দিকদর্শন: বান্দার যাবতীয় গুনাহ ও ভুলের পরেও অনুতপ্ত হয়ে ফিরে এলে রবের ক্ষমা ও ভালোবাসার সুসংবাদ।"
            isAngerQuery(query) -> "কুরআনিক দিকদর্শন: ক্রোধ ও উত্তেজনার মুহূর্তে আত্মসংবরণ এবং ক্ষমা প্রদর্শনের মাধ্যমে জান্নাতের পরম মর্যাদা লাভ।"
            isPovertyQuery(query) -> "কুরআনিক দিকদর্শন: অভাব ও ভবিষ্যতের ভয় দূর করে রিজিকের একমাত্র মালিক আল্লাহর ওপর পূর্ণ তাওয়াক্কুল ও দানশীলতার শিক্ষা।"
            isParentsQuery(query) -> "কুরআনিক দিকদর্শন: বার্ধক্যে পিতামাতার সেবা, বিনম্র আচরণ ও অকৃত্রিম শ্রদ্ধার মাধ্যমে জান্নাতের পথ সুগম করা।"
            isMarriageQuery(query) -> "কুরআনিক দিকদর্শন: দাম্পত্যে পারস্পরিক প্রশান্তি, আন্তরিক ভালোবাসা ও মতভেদে ধৈর্যশীল আপস-মীমাংসা।"
            isHardTimesQuery(query) -> "কুরআনিক দিকদর্শন: বিপদ ও কঠিন পরিস্থিতিতে মুমিনের ধৈর্য, সাধ্যের বাইরের বোঝা না দেওয়ার নিশ্চয়তা ও স্বস্তির আগমন।"
            isPeaceQuery(query) -> "কুরআনিক দিকদর্শন: পার্থিব কোলাহলে অশান্ত মনের একমাত্র মহৌষধ হলো আল্লাহর একনিষ্ঠ জিকির ও সান্নিধ্য।"
            isGratitudeQuery(query) -> "কুরআনিক দিকদর্শন: জীবনের প্রতিটি নেয়ামতের জন্য আল্লাহর শুকরিয়া আদায় করলে নেয়ামত বৃদ্ধির ঐশী বিধান।"
            topAyah != null -> "কুরআনিক দিকদর্শন: আপনার অনুসন্ধানের প্রেক্ষিতে ${topAyah.surahNameBn}-এর আলোকে ঐশী প্রজ্ঞা ও নির্দেশনা।"
            else -> "কুরআনিক দিকদর্শন: আপনার মনোভাব ও বাস্তব জীবনের প্রশ্নের সমাধানে আল-কুরআনের সার্বজনীন আলোকবর্তিকা।"
        }
    }

    /**
     * Calls Gemini API with semantic search prompt.
     */
    private fun callGeminiSemanticSearch(query: String, apiKey: String): SmartQuranSearchResult? {
        val prompt = """
            You are a profound Quranic scholar, psychologist, and semantic search engine.
            The user is searching the Holy Quran semantically with this theme/emotion/situation:
            "$query"

            Do NOT perform literal keyword matching. Understand the deep psychological, spiritual, and situational context of the user.
            Find 2 to 4 of the most directly relevant Ayat from the Quran that comfort, guide, or address this exact state.
            
            Return a valid JSON object ONLY (no markdown fences, no code blocks, pure JSON) with the following structure:
            {
              "interpretedIntentBn": "সংক্ষিপ্ত বাংলা সারসংক্ষেপ: এই বিষয়ে কুরআনের মূল দৃষ্টিভঙ্গি",
              "ayahs": [
                {
                  "surahNumber": 39,
                  "ayahNumber": 53,
                  "surahNameBn": "সূরা আয-যুমার",
                  "surahNameAr": "سورة الزمر",
                  "surahNameEn": "Surah Az-Zumar",
                  "revelationTypeBn": "মাক্কী",
                  "arabicText": "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا ۚ إِنَّهُ هُوَ الْغَفُورُ الرَّحِيمُ",
                  "transliterationBn": "বাংলা উচ্চারণ",
                  "banglaTranslation": "সহজ বাংলা অনুবাদ",
                  "englishTranslation": "Sahih International English translation",
                  "tafsirSummaryBn": "প্রামাণ্য তাফসীর সংক্ষেপ (ইবনে কাসীর/মা'আরিফুল কুরআন)",
                  "divineWisdomBn": "কেন এই আয়াতটি এই পরিস্থিতি বা আবেগের মহৌষধ",
                  "relatedThemes": ["থিম ১", "থিম ২", "থিম ৩"],
                  "primaryTopicBn": "বিষয় শিরোনাম",
                  "primaryTopicEn": "Topic title in English",
                  "relatedHadithBn": "সম্পর্কিত সহীহ হাদীসের সনদ ও অর্থ"
                }
              ]
            }
        """.trimIndent()

        val jsonBody = JSONObject().apply {
            val contents = JSONArray().apply {
                val contentObj = JSONObject().apply {
                    val parts = JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", prompt)
                        })
                    }
                    put("parts", parts)
                }
                put(contentObj)
            }
            put("contents", contents)
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.3)
                put("responseMimeType", "application/json")
            })
        }

        val request = Request.Builder()
            .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val responseString = response.body?.string() ?: return null
        val rootObj = JSONObject(responseString)
        val candidates = rootObj.optJSONArray("candidates") ?: return null
        if (candidates.length() == 0) return null

        val candidate = candidates.getJSONObject(0)
        val content = candidate.optJSONObject("content") ?: return null
        val parts = content.optJSONArray("parts") ?: return null
        if (parts.length() == 0) return null

        val text = parts.getJSONObject(0).optString("text", "")
        if (text.isBlank()) return null

        val cleanJson = text.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()

        val parsedObj = JSONObject(cleanJson)
        val intentBn = parsedObj.optString("interpretedIntentBn", "")
        val ayahsArray = parsedObj.optJSONArray("ayahs") ?: return null

        val parsedAyahs = mutableListOf<SemanticQuranAyah>()
        for (i in 0 until ayahsArray.length()) {
            val a = ayahsArray.getJSONObject(i)
            val sNum = a.optInt("surahNumber", 1)
            val aNum = a.optInt("ayahNumber", 1)

            val sPadded = sNum.toString().padStart(3, '0')
            val aPadded = aNum.toString().padStart(3, '0')
            val audioUrl = "https://everyayah.com/data/Alafasy_128kbps/$sPadded$aPadded.mp3"

            val themesList = mutableListOf<String>()
            val themesArr = a.optJSONArray("relatedThemes")
            if (themesArr != null) {
                for (t in 0 until themesArr.length()) {
                    themesList.add(themesArr.getString(t))
                }
            }

            parsedAyahs.add(
                SemanticQuranAyah(
                    id = "ayah_${sNum}_${aNum}",
                    surahNumber = sNum,
                    ayahNumber = aNum,
                    surahNameBn = a.optString("surahNameBn", "সূরা"),
                    surahNameAr = a.optString("surahNameAr", ""),
                    surahNameEn = a.optString("surahNameEn", ""),
                    revelationTypeBn = a.optString("revelationTypeBn", "মাক্কী"),
                    arabicText = a.optString("arabicText", ""),
                    transliterationBn = a.optString("transliterationBn", ""),
                    banglaTranslation = a.optString("banglaTranslation", ""),
                    englishTranslation = a.optString("englishTranslation", ""),
                    tafsirSummaryBn = a.optString("tafsirSummaryBn", ""),
                    divineWisdomBn = a.optString("divineWisdomBn", ""),
                    relatedThemes = themesList,
                    primaryTopicBn = a.optString("primaryTopicBn", query),
                    primaryTopicEn = a.optString("primaryTopicEn", query),
                    relatedHadithBn = if (a.has("relatedHadithBn")) a.optString("relatedHadithBn") else null,
                    audioUrl = audioUrl
                )
            )
        }

        return SmartQuranSearchResult(
            query = query,
            interpretedIntentBn = intentBn,
            ayahs = parsedAyahs,
            isAiEnhanced = true
        )
    }
}
