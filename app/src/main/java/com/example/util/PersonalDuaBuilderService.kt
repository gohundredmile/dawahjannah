package com.example.util

import android.content.Context
import com.example.BuildConfig
import com.example.data.datasource.PersonalDuaCatalog
import com.example.data.model.DuaConstructionMode
import com.example.data.model.DuaSourceMapItem
import com.example.data.model.DuaSourceType
import com.example.data.model.PersonalDuaBlueprint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

/**
 * Dua Architect Service.
 * Helps users construct meaningful, personalized Duas grounded exclusively in
 * the Holy Qur'an and authentic Sahih Hadith from the local application database.
 * Strictly adheres to source authenticity and never fabricates religious text.
 */
class PersonalDuaBuilderService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(25, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Builds a structured, authentic Personal Dua blueprint for the user's situation.
     * Supports Mode A (Authentic Single), Mode B (Curated Collection), Mode C (Personalized Flow).
     */
    suspend fun buildDuaBlueprint(
        userQuery: String,
        targetMode: DuaConstructionMode = DuaConstructionMode.CURATED_COLLECTION
    ): PersonalDuaBlueprint = withContext(Dispatchers.IO) {
        val query = userQuery.trim()
        val baseBlueprint = PersonalDuaCatalog.findBestMatch(query)

        // Build mode-specific representations
        val adjustedBlueprint = applyConstructionMode(baseBlueprint.copy(userQuery = query), targetMode)

        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // If no Gemini API key or placeholder, return pure authentic local catalog
        if (apiKey.isBlank() || apiKey == "your_api_key_here") {
            return@withContext adjustedBlueprint
        }

        // If API key is present, enhance semantic empathy & whySelected explanation WITHOUT altering Arabic
        try {
            val systemPrompt = """
                You are Dua Architect, an Islamic personal-dua assistant.
                Rules:
                1. NEVER fabricate, modify or invent Quranic verses, Arabic text or Hadith.
                2. Analyze the user's situation:
                   - Identify intention (e.g. Health, Guidance, Provision, Forgiveness)
                   - Identify emotional context (e.g. Anxiety, Hope, Fear)
                   - Identify people involved (Self, Parents, Family, Spouse)
                3. Return JSON:
                   - "comfortBn": 2-3 sentences of Islamic spiritual comfort and Tawakkul in Bengali.
                   - "comfortEn": 2-3 sentences of Islamic spiritual comfort in English.
                   - "whySelectedBn": 1-2 sentences explaining why the selected authentic Quranic/Prophetic supplications relate to the user's request in Bengali.
                   - "whySelectedEn": 1-2 sentences explaining selection in English.
            """.trimIndent()

            val requestBodyJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "$systemPrompt\nUser query: $query\nScenario: ${baseBlueprint.scenarioTitleEn}"))
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("responseMimeType", "application/json")
                })
            }

            val request = Request.Builder()
                .url("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey")
                .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (!responseBody.isNullOrBlank()) {
                    val root = JSONObject(responseBody)
                    val text = root.getJSONArray("candidates")
                        .getJSONObject(0)
                        .getJSONObject("content")
                        .getJSONArray("parts")
                        .getJSONObject(0)
                        .getString("text")

                    val parsed = JSONObject(text)
                    val comfortBn = parsed.optString("comfortBn", adjustedBlueprint.spiritualComfortBn)
                    val comfortEn = parsed.optString("comfortEn", adjustedBlueprint.spiritualComfortEn)
                    val whySelectedBn = parsed.optString("whySelectedBn", adjustedBlueprint.whySelectedBn)
                    val whySelectedEn = parsed.optString("whySelectedEn", adjustedBlueprint.whySelectedEn)

                    return@withContext adjustedBlueprint.copy(
                        spiritualComfortBn = comfortBn,
                        spiritualComfortEn = comfortEn,
                        whySelectedBn = whySelectedBn,
                        whySelectedEn = whySelectedEn,
                        isAiEnhanced = true
                    )
                }
            }
        } catch (_: Exception) {
            // Graceful fallback to verified authentic local catalog
        }

        adjustedBlueprint
    }

    /**
     * Applies construction mode formatting (Mode A, Mode B, Mode C) to the blueprint
     * while strictly maintaining source boundaries and exact Arabic.
     */
    fun applyConstructionMode(
        blueprint: PersonalDuaBlueprint,
        mode: DuaConstructionMode
    ): PersonalDuaBlueprint {
        when (mode) {
            DuaConstructionMode.AUTHENTIC_SINGLE -> {
                // Priority 1: Pick the single most directly relevant Prophetic or Quranic Dua
                val primarySource = blueprint.sources.firstOrNull()
                val arabic = primarySource?.arabicSourceText
                    ?: blueprint.quranicDuas.firstOrNull()?.arabicText
                    ?: blueprint.propheticDuas.firstOrNull()?.arabicText
                    ?: ""
                val translationBn = primarySource?.translationBn
                    ?: blueprint.quranicDuas.firstOrNull()?.banglaTranslation
                    ?: blueprint.propheticDuas.firstOrNull()?.banglaTranslation
                    ?: ""
                val translationEn = primarySource?.translationEn
                    ?: blueprint.quranicDuas.firstOrNull()?.englishTranslation
                    ?: blueprint.propheticDuas.firstOrNull()?.englishTranslation
                    ?: ""

                val singleSourceList = if (primarySource != null) listOf(primarySource) else emptyList()

                return blueprint.copy(
                    mode = mode,
                    curatedArabicText = arabic,
                    curatedTranslationBn = translationBn,
                    curatedTranslationEn = translationEn,
                    whySelectedBn = "আপনার পরিস্থিতির জন্য একক সর্বাধিক প্রাসঙ্গিক প্রামাণ্য দো'আটি নির্বাচন করা হয়েছে।",
                    whySelectedEn = "Directly selected the single most relevant authentic supplication for your situation.",
                    sources = singleSourceList
                )
            }

            DuaConstructionMode.CURATED_COLLECTION -> {
                // Combine authentic Quranic and Prophetic supplications with clear boundaries
                val arabicBuilder = StringBuilder()
                val bnBuilder = StringBuilder()
                val enBuilder = StringBuilder()

                blueprint.sources.forEachIndexed { index, source ->
                    if (index > 0) {
                        arabicBuilder.append(" ۝ ")
                        bnBuilder.append(" এবং ")
                        enBuilder.append(" And: ")
                    }
                    arabicBuilder.append(source.arabicSourceText)
                    bnBuilder.append(source.translationBn)
                    if (source.translationEn.isNotBlank()) {
                        enBuilder.append(source.translationEn)
                    }
                }

                val arabic = if (arabicBuilder.isNotEmpty()) arabicBuilder.toString() else blueprint.curatedArabicText
                val transBn = if (bnBuilder.isNotEmpty()) bnBuilder.toString() else blueprint.curatedTranslationBn
                val transEn = if (enBuilder.isNotEmpty()) enBuilder.toString() else blueprint.curatedTranslationEn

                return blueprint.copy(
                    mode = mode,
                    curatedArabicText = arabic,
                    curatedTranslationBn = transBn,
                    curatedTranslationEn = transEn
                )
            }

            DuaConstructionMode.PERSONALIZED_FLOW -> {
                // Construct respectful personal framework around authentic Duas
                val arabicBuilder = StringBuilder()
                val bnBuilder = StringBuilder()
                val enBuilder = StringBuilder()

                bnBuilder.append("হে পরম দয়ালু আল্লাহ! ")
                enBuilder.append("O Allah, the Most Merciful! ")

                blueprint.sources.forEachIndexed { index, source ->
                    if (index > 0) {
                        arabicBuilder.append(" ۝ ")
                        bnBuilder.append(" হে রব! ")
                        enBuilder.append(" O Lord! ")
                    }
                    arabicBuilder.append(source.arabicSourceText)
                    bnBuilder.append(source.translationBn)
                    if (source.translationEn.isNotBlank()) {
                        enBuilder.append(source.translationEn)
                    }
                }

                return blueprint.copy(
                    mode = mode,
                    curatedArabicText = if (arabicBuilder.isNotEmpty()) arabicBuilder.toString() else blueprint.curatedArabicText,
                    curatedTranslationBn = bnBuilder.toString(),
                    curatedTranslationEn = enBuilder.toString(),
                    whySelectedBn = "আপনার ব্যক্তিগত পরিস্থিতির উপযোগী করে প্রামাণ্য দো'আসমূহকে বিনম্র আরজি কাঠামোয় সাজানো হয়েছে।",
                    whySelectedEn = "Arranged authentic supplications in a personalized devotional flow for your specific circumstance."
                )
            }
        }
    }
}
