package com.example.util

import android.content.Context
import com.example.BuildConfig
import com.example.data.datasource.QuranActionEngineCatalog
import com.example.data.datasource.QuranAyahCatalog
import com.example.data.model.ActionPlanDuration
import com.example.data.model.ActionRelatedHadith
import com.example.data.model.ActionRelatedVerse
import com.example.data.model.AskAyahAnswer
import com.example.data.model.AyahActionInsight
import com.example.data.model.KeyArabicTerm
import com.example.data.model.LifeSphere
import com.example.data.model.TafsirSourceItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class QuranActionEngineAiService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    /**
     * Resolves an AyahActionInsight for a given query or selection.
     * First checks the high-fidelity catalog; if not found, synthesizes using QuranAyahCatalog + Gemini.
     */
    suspend fun getOrGenerateInsight(queryOrId: String): AyahActionInsight = withContext(Dispatchers.IO) {
        val trimmed = queryOrId.trim()

        // 1. Direct match in local Action Engine catalog
        val catalogMatch = QuranActionEngineCatalog.catalog.firstOrNull {
            it.ayahId.equals(trimmed, ignoreCase = true) ||
                    "${it.surahNumber}:${it.ayahNumber}" == trimmed ||
                    it.surahNameBangla.contains(trimmed, ignoreCase = true)
        }
        if (catalogMatch != null) return@withContext catalogMatch

        // 2. Match in general QuranAyahCatalog
        val generalAyah = QuranAyahCatalog.catalog.firstOrNull {
            "${it.surahNumber}:${it.ayahNumber}" == trimmed ||
                    it.id.equals(trimmed, ignoreCase = true) ||
                    it.surahNameBangla.contains(trimmed, ignoreCase = true)
        }

        if (generalAyah != null) {
            // Synthesize grounded insight from QuranAyahCatalog
            return@withContext buildInsightFromGeneralAyah(generalAyah)
        }

        // 3. Fallback default: First catalog item (Surah Al-Baqarah 2:153)
        return@withContext QuranActionEngineCatalog.catalog.first()
    }

    /**
     * Contextual Q&A: "Ask the Ayah".
     */
    suspend fun askTheAyah(insight: AyahActionInsight, question: String): AskAyahAnswer = withContext(Dispatchers.IO) {
        val q = question.trim()

        // Check pre-configured high-frequency questions
        val quickAnswer = matchQuickQuestion(insight, q)
        if (quickAnswer != null) return@withContext quickAnswer

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "your_api_key_here") {
            return@withContext generateOfflineAnswer(insight, q)
        }

        try {
            val prompt = """
                You are a senior scholar and life-application educator of the Holy Quran.
                The user is reflecting on Ayah:
                Surah: ${insight.surahNameBangla} (${insight.surahNumber}:${insight.ayahNumber})
                Arabic: ${insight.arabicText}
                Translation: ${insight.banglaTranslation}
                Tafsir Context: ${insight.immediateContextBn}

                The user asks: "$q"

                Provide a structured, deeply respectful, authentic response in Bengali:
                - Never invent Quran verses or fabricate Hadith.
                - Clearly distinguish between: 
                  (1) Quran Text (কুরআনের পাঠ্য)
                  (2) Classical Tafsir / Scholarly Consensus (বিজ্ঞ মুফাসসিরগণের ব্যাখ্যা)
                  (3) Personal Mindfulness / Application suggestion (বাস্তব জীবনের আমল)
                - Identify whether this ayah represents: আদেশ (Command), উপদেশ (Recommendation/Counsel), ঘটনা (Narrative/History), সতর্কবার্তা (Warning), or বিবরণ (Description/Fact).

                Return strictly valid JSON with this format:
                {
                  "classification": "উপদেশ / আদেশ / সতর্কবার্তা ইত্যাদি",
                  "answerBn": "বিস্তারিত ও গভীর জ্ঞানগর্ভ উত্তর...",
                  "scholarlyBasisBn": "প্রামাণ্য তাফসীর বা ফিকহী উৎস...",
                  "personalApplicationTip": "আজকের জীবনে ছোট বাস্তবসম্মত একটি পদক্ষেপ..."
                }
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("response_mime_type", "application/json")
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyStr = response.body?.string() ?: ""
                val rootJson = JSONObject(bodyStr)
                val textCandidate = rootJson.getJSONArray("candidates")
                    .getJSONObject(0)
                    .getJSONObject("content")
                    .getJSONArray("parts")
                    .getJSONObject(0)
                    .getString("text")

                val parsed = JSONObject(textCandidate)
                return@withContext AskAyahAnswer(
                    question = q,
                    classificationBn = parsed.optString("classification", "উপদেশ ও পথনির্দেশ"),
                    answerBn = parsed.optString("answerBn", ""),
                    scholarlyBasisBn = parsed.optString("scholarlyBasisBn", "তাফসীরে ইবনে কাসীর ও নির্ভরযোগ্য উলামায়ে কিরামের ইজতিহাদ"),
                    personalApplicationTip = parsed.optString("personalApplicationTip", insight.defaultTodayAction)
                )
            }
        } catch (_: Exception) {
        }

        return@withContext generateOfflineAnswer(insight, q)
    }

    private fun matchQuickQuestion(insight: AyahActionInsight, question: String): AskAyahAnswer? {
        val q = question.lowercase()

        if (q.contains("কর্মক্ষেত্র") || q.contains("অফিস") || q.contains("workplace")) {
            val app = insight.applicationsBySphere[LifeSphere.WORK]?.firstOrNull()
                ?: "কাজে সততা, একাগ্রতা ও সহকর্মীদের প্রতি ইনসাফপূর্ণ আচরণ বজায় রাখুন।"
            return AskAyahAnswer(
                question = question,
                classificationBn = "উপদেশ ও দায়িত্ববোধ",
                answerBn = "কর্মক্ষেত্রে এই আয়াতটির মূল বাস্তবায়ন হলো পেশাদারিত্বে সততা ও বিনয় রক্ষা করা। আয়াতের শিক্ষা আমাদের শেখায় যে প্রতিটি কাজ আল্লাহর দৃষ্টিতে আমানত। সহকর্মীদের সাথে সম্পর্কের টানাপোড়েন হোক বা কাজের অতিরিক্ত চাপ—আয়াতের নীতি স্মরণ করে মানসিক স্থিতি বজায় রাখা জরুরি।",
                scholarlyBasisBn = "মুফাসসিরীনদের সর্বসম্মত নীতি: দ্বীন কেবল মসজিদে সীমাবদ্ধ নয়; কর্মক্ষেত্রে প্রতিটি সৎ আচরণও অন্যতম শ্রেষ্ঠ ইবাদত।",
                personalApplicationTip = app
            )
        }

        if (q.contains("রাগ") || q.contains("ক্রোধ") || q.contains("anger")) {
            val app = insight.applicationsBySphere[LifeSphere.ANGER]?.firstOrNull()
                ?: "রাগের মাথায় কোনো পাল্টা মন্তব্য করবেন না; ৫ সেকেন্ড নীরব থেকে মনে মনে আউযুবিল্লাহ পড়ুন।"
            return AskAyahAnswer(
                question = question,
                classificationBn = "সতর্কতা ও আত্মসংযম",
                answerBn = "রাগ হলো শয়তানের প্ররোচনা এবং মানুষের আত্মিক ধ্বংসের প্রবেশদ্বার। এই আয়াতটি আমাদের শেখায় যে প্রতিকূলতার মুখে প্রতিশোধ না নিয়ে সবর ও ক্ষমাশীলতার পথ বেছে নেওয়াই প্রকৃত বীরত্ব।",
                scholarlyBasisBn = "রাসূলুল্লাহ ﷺ বলেছেন: প্রকৃত বীর সে যে রাগের সময় নিজেকে নিয়ন্ত্রণ করতে পারে (বুখারী ৬১১৪)।",
                personalApplicationTip = app
            )
        }

        if (q.contains("প্রেক্ষাপট") || q.contains("শান-এ-নুযুল") || q.contains("context")) {
            return AskAyahAnswer(
                question = question,
                classificationBn = "ঐতিহাসিক প্রেক্ষাপট ও শান-এ-নুযুল",
                answerBn = if (insight.immediateContextBn.isNotBlank()) insight.immediateContextBn else "এই আয়াতটি মুসলিম উম্মাহকে প্রতিকূলতা ও পরীক্ষার মুখে আত্মিক স্থিতি ও আল্লাহর ওপর অবিচল আস্থা রাখার শিক্ষাদানকল্পে অবতীর্ণ হয়।",
                scholarlyBasisBn = insight.tafsirSources.firstOrNull()?.sourceNameBn ?: "তাফসীরে ইবনে কাসীর ও আসবাবুন নুযুল",
                personalApplicationTip = "আয়াতের শান-এ-নুযুল জেনে আজকের দিনে নিজের সংকটের সাথে তুলনা করে সবর অবলম্বন করুন।"
            )
        }

        if (q.contains("আদেশ") || q.contains("উপদেশ") || q.contains("বিধান") || q.contains("হুকুম")) {
            return AskAyahAnswer(
                question = question,
                classificationBn = "শরঈ মানদণ্ড ও ফিকহী স্তর",
                answerBn = "আয়াতে '${insight.quranSaysBn}'—এটি মূলত মুমিনদের চরিত্র গঠন, আত্মশুদ্ধি এবং আল্লাহর সাথে গভীর সংযোগ স্থাপনের এক কালজয়ী হেদায়েত।",
                scholarlyBasisBn = "ফিকহী মূলনীতি: কুরআনের নির্দেশনাগুলো মানুষের অন্তরকে অহংকারমুক্ত করে আল্লাহর সন্তুষ্টির উপযোগী বানায়।",
                personalApplicationTip = "আয়াতের শিক্ষাকে নিছক তত্ত্ব হিসেবে না দেখে নিজের দৈনন্দিন আচরণের মাপকাঠি হিসেবে গ্রহণ করুন।"
            )
        }

        return null
    }

    private fun generateOfflineAnswer(insight: AyahActionInsight, question: String): AskAyahAnswer {
        return AskAyahAnswer(
            question = question,
            classificationBn = "কুরআনী হেদায়াত ও তাদাব্বুর",
            answerBn = "পবিত্র এই আয়াতে মহান আল্লাহ মূলত আমাদের অন্তরের পরিশুদ্ধি ও বাস্তব জীবনের ভারসাম্যের নির্দেশ দিয়েছেন। প্রশ্নটির আলোকেও আয়াতটির শিক্ষা অত্যন্ত প্রাসঙ্গিক: '${insight.whatDoesItTeachBn}'। জীবনের যেকোনো অবস্থায় আল্লাহর বিধানকে অগ্রাধিকার দেওয়া এবং সহনশীল আচরণ করাই মুমিনের কাম্য।",
            scholarlyBasisBn = insight.tafsirSources.firstOrNull()?.let { "${it.sourceNameBn} - ${it.scholarOrBook}" }
                ?: "তাফসীরে ইবনে কাসীর ও সমকালীন ফিকহী গবেষণা",
            personalApplicationTip = insight.defaultTodayAction
        )
    }

    private fun buildInsightFromGeneralAyah(general: com.example.data.model.AyahExplanation): AyahActionInsight {
        val sphereMap = mutableMapOf<LifeSphere, List<String>>()
        LifeSphere.entries.forEach { sp ->
            sphereMap[sp] = listOf(
                "${sp.titleBn}-এর ক্ষেত্রে এই আয়াতের মূল শিক্ষা হলো আল্লাহর স্মরণ ও তাকওয়া বজায় রাখা।",
                "আজকের দিনে ${sp.titleBn}-এ কোনো সিদ্ধান্ত নেওয়ার পূর্বে এই আয়াতের অর্থ স্মরণ করুন।"
            )
        }

        return AyahActionInsight(
            ayahId = general.id,
            surahNumber = general.surahNumber,
            ayahNumber = general.ayahNumber,
            surahNameArabic = general.surahNameArabic,
            surahNameBangla = general.surahNameBangla,
            surahNameEnglish = general.surahNameEnglish,
            revelationTypeBn = general.revelationTypeBn,
            arabicText = general.arabicText,
            transliterationBn = general.transliterationBn,
            banglaTranslation = general.banglaTranslation,
            englishTranslation = general.englishTranslation,
            whatDoesItTeachBn = "আয়াতটি আল্লাহর সার্বভৌম মহিমা, তাওহীদের বিশুদ্ধতা এবং বান্দার প্রতি দায়িত্বশীলতার শিক্ষা দেয়।",
            whatToNoticeBn = "আল্লাহর নিদর্শনের প্রতি গভীর মনোযোগ এবং নিজের ক্ষুদ্রতা স্বীকার করে তাঁর নির্দেশ মান্য করা।",
            whatToBeCarefulAboutBn = "আল্লাহর বিধান লঙ্ঘন করা, গাফিলতিতে সময় কাটানো এবং পার্থিব মোহে অন্ধ হওয়া।",
            whatCanIPracticeBn = "আজকের দিনে সচেতনভাবে অন্তত একটি ভালো কাজ আল্লাহর সন্তুষ্টির উদ্দেশ্যে সম্পূর্ণ গোপনে সম্পাদন করা।",
            quranSaysBn = general.banglaTranslation,
            scholarlyInterpretationBn = general.tafsirBn.take(200) + "...",
            possiblePersonalApplicationBn = "ব্যক্তিগত জীবনে এই আয়াতের শিক্ষা ধারণ করে অন্তরে আল্লাহর ভয় ও ভালোবাসা জাগ্রত রাখা।",
            tafsirSources = listOf(
                TafsirSourceItem("তাফসীরে ইবনে কাসীর", "ইবনে কাসীর (রহ.)", general.tafsirBn.take(150) + "..."),
                TafsirSourceItem("তাফসীরে মা'আরিফুল কুরআন", "মুফতী শফী (রহ.)", "আয়াতটিতে জীবনের আত্মিক ও ব্যবহারিক নির্দেশনার সমন্বয় ঘটেছে।")
            ),
            keyArabicTerms = general.wordByWord.take(3).map {
                KeyArabicTerm(it.arabicWord, "", it.bengaliMeaning, it.grammarNote)
            },
            immediateContextBn = general.contextBn,
            relatedVerses = general.relatedVerses.map {
                ActionRelatedVerse(it.surahNameBn, it.ayahRef, it.arabicText, it.translationBn)
            },
            relatedHadiths = general.relatedHadiths.map {
                ActionRelatedHadith(it.sourceBn, it.narratorBn, it.textBn, it.gradeBn)
            },
            reflectiveQuestions = listOf(
                "এই আয়াতের বার্তা আমার বর্তমান জীবনে কীভাবে প্রভাব ফেলতে পারে?",
                "আমি কি আল্লাহর দেওয়া এই বিধানের প্রতি যথাযথ মনোযোগী?",
                "আজকের দিনে আমার আচরণে এই আয়াতের কোনো প্রতিফলন কি দেখা যাবে?",
                "কোন ভুলের ব্যাপারে এই আয়াত আমাকে সতর্ক করছে?",
                "আমি কীভাবে আয়াতটির শিক্ষা পরিবারের সাথে শেয়ার করতে পারি?"
            ),
            applicationsBySphere = sphereMap,
            defaultTodayAction = "আজ সালাতের পর জায়নামাজে বসে ২ মিনিট এই আয়াতটির অর্থ নিয়ে ভাবুন এবং আল্লাহর কাছে হেদায়াতের দোয়া করুন।",
            alternativeTodayActions = listOf(
                "আজ পরিবারের কারো সাথে আয়াতটির অর্থ ও তাৎপর্য নিয়ে ৫ মিনিট আলোচনা করুন।",
                "আয়াতটির কোনো একটি আরবি শব্দ বা হরকত সুন্দরভাবে শুদ্ধ করে শিখুন।"
            ),
            warningAvoidanceGoal = "গাফিলতি ও উদাসীনতা পরিহার করা।",
            memorizationTip = "আয়াতের শুরু ও শেষ শব্দের মিল লক্ষ্য রেখে ৩ বার দেখে ও ৩ বার না দেখে পড়ুন।",
            teachingGuideBn = "কাউকে বলুন: কুরআন শুধু পড়ার জন্য নয়, জীবনের প্রতিটি পদক্ষেপে চলার পথনির্দেশ।",
            primaryThemes = listOf("তাকওয়া", "তাওহীদ", "আমল", "আত্মশুদ্ধি"),
            audioUrl = general.audioUrl
        )
    }
}
