package com.example.util

import android.content.Context
import android.graphics.Bitmap
import com.example.BuildConfig
import com.example.data.datasource.IslamicContextVerifyCatalog
import com.example.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class IslamicContextVerifyAiService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val prefs by lazy {
        context.getSharedPreferences("islamic_context_verify_prefs", Context.MODE_PRIVATE)
    }

    fun getEffectiveApiKey(): String {
        val userKey = prefs.getString("custom_gemini_api_key", null)?.trim()
        if (!userKey.isNullOrBlank() && !userKey.contains("your_api_key_here")) {
            return userKey
        }
        val buildConfigKey = try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isNotBlank() && key != "your_api_key_here") key else ""
        } catch (_: Throwable) {
            ""
        }
        return buildConfigKey
    }

    /**
     * Extracts text from an image or screenshot using fast on-device ML Kit OCR.
     */
    suspend fun extractTextFromImage(bitmap: Bitmap): String = withContext(Dispatchers.Default) {
        LocalQuranAyahScannerEngine.recognizeTextFromBitmap(bitmap)
    }

    /**
     * Verifies and contextualizes an Islamic claim using Gemini 3.5 Flash,
     * gracefully falling back to the authentic local knowledge base if offline or without an API key.
     */
    suspend fun verifyClaim(
        claimText: String,
        sourcePlatformTag: String = "WhatsApp বার্তা"
    ): IslamicContextVerifyReport = withContext(Dispatchers.IO) {
        val cleanClaim = claimText.trim()
        if (cleanClaim.isBlank()) {
            return@withContext IslamicContextVerifyCatalog.presetClaims.first()
        }

        val apiKey = getEffectiveApiKey()
        if (apiKey.isBlank() || apiKey == "your_api_key_here") {
            // Local high-fidelity synthesis
            return@withContext IslamicContextVerifyCatalog.analyzeClaimLocally(cleanClaim, sourcePlatformTag)
        }

        try {
            val aiReport = callGeminiVerification(cleanClaim, sourcePlatformTag, apiKey)
            if (aiReport != null) {
                return@withContext aiReport
            }
        } catch (_: Exception) {
            // Graceful fallback to local intelligence
        }

        IslamicContextVerifyCatalog.analyzeClaimLocally(cleanClaim, sourcePlatformTag)
    }

    private fun callGeminiVerification(
        claimText: String,
        sourceTag: String,
        apiKey: String
    ): IslamicContextVerifyReport? {
        val systemPrompt = """
            You are a premier Islamic Context & Source Verification Scholar with deep expertise in Mustalah al-Hadith (Hadith sciences & gradings), Quranic Tafsir (Tabari, Ibn Kathir, Qurtubi), and Comparative Fiqh (Hanafi, Shafi'i, Maliki, Hanbali).
            Your purpose is to help users verify and understand Islamic claims encountered in WhatsApp messages, social media, videos, or articles.

            RULES:
            1. NEVER invent Quran verses, hadith numbers, or scholarly opinions.
            2. Avoid binary True/False. Use nuanced verdicts:
               - "ESTABLISHED" (Proven by Quran and Sahih Hadith)
               - "CONTEXT_NEEDED" (Evidence exists but context, scope, or nuance is missing)
               - "SCHOLARLY_DISAGREEMENT" (Legitimate difference among classical schools)
               - "INSUFFICIENT_EVIDENCE" (No clear authentic proof found)
               - "MISQUOTED_OR_UNSUPPORTED" (Fabricated, misattributed, or baseless)
               - "MORE_EVIDENCE_NEEDED" (Vague or incomplete)
            3. Clearly distinguish:
               - Explicit Direct Text vs Human Interpretation
               - Hadith Wording vs Later Interpretations
               - What Is Actually Established vs Uncertain
            4. Identify any red flags (Fabricated quote, context removed, selective quoting, etc.).
            5. Always output in fluent, respectful Bengali (বাংলা) for explanations, with Arabic for verses/hadiths.
            6. Respond ONLY in valid JSON matching this schema:
            {
              "verdict": "ESTABLISHED" | "CONTEXT_NEEDED" | "SCHOLARLY_DISAGREEMENT" | "INSUFFICIENT_EVIDENCE" | "MISQUOTED_OR_UNSUPPORTED" | "MORE_EVIDENCE_NEEDED",
              "confidence": "HIGH" | "MODERATE" | "LIMITED",
              "summaryHeadlineBn": "Concise 1-sentence verdict headline in Bengali",
              "evidenceSummaryBn": "Core evidence summary in Bengali",
              "individualClaims": ["Claim 1", "Claim 2"],
              "quranReferences": [
                {
                  "surahName": "সূরা আল-...",
                  "surahNumber": 1,
                  "ayahNumber": 1,
                  "arabicText": "Arabic verse text",
                  "translationBn": "Bengali translation",
                  "whatVerseActuallyAddresses": "What the verse actually discusses",
                  "isDirectEvidence": true
                }
              ],
              "hadithReferences": [
                {
                  "collection": "সহীহ বুখারী / সহীহ মুসলিম / তিরমিযী ইত্যাদি",
                  "hadithNumber": "হা/...",
                  "arabicText": "Arabic hadith text",
                  "translationBn": "Bengali translation",
                  "authenticityGrade": "সহীহ (Sahih) / হাসান / যয়ীফ / মাওযূ (জাল)",
                  "textVsInterpretation": "Distinction between exact text and human deduction"
                }
              ],
              "historicalContextBn": "Historical background & Asbab an-Nuzul / Wurud",
              "linguisticContextBn": "Arabic linguistic nuance where relevant",
              "audienceAndScopeBn": "Who was addressed, general vs situation-specific",
              "omittedSurroundingsBn": "Surrounding context that was omitted in viral post",
              "scholarlyInterpretations": [
                {
                  "schoolOrScholar": "হানাফী / শাফেয়ী / জমহুর উলামা",
                  "positionSummary": "Summary of opinion",
                  "textualBasis": "Basis of this view"
                }
              ],
              "potentialMisinformation": [
                {
                  "warningType": "Fabricated Hadith / Selective Quotation / Out of Context",
                  "warningDetail": "Specific explanation of the flaw in the claim"
                }
              ],
              "whatIsActuallyEstablished": {
                "explicitTextualFact": "What the primary texts explicitly declare",
                "scholarlyInference": "What scholars infer",
                "uncertainOrUnverified": "What cannot be established"
              },
              "actionableConclusionBn": "Practical and balanced guidance in Bengali"
            }
        """.trimIndent()

        val userMessage = """
            Platform: $sourceTag
            Analyze and verify this Islamic claim:
            "$claimText"
        """.trimIndent()

        val jsonPayload = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", userMessage))
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().put("text", systemPrompt))
                })
            })
            put("generationConfig", JSONObject().apply {
                put("temperature", 0.2)
                put("responseMimeType", "application/json")
            })
        }

        val requestUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"
        val request = Request.Builder()
            .url(requestUrl)
            .post(jsonPayload.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val responseBody = response.body?.string() ?: return null
        val rootObj = JSONObject(responseBody)
        val candidates = rootObj.optJSONArray("candidates") ?: return null
        if (candidates.length() == 0) return null

        val content = candidates.getJSONObject(0).optJSONObject("content") ?: return null
        val parts = content.optJSONArray("parts") ?: return null
        if (parts.length() == 0) return null

        val text = parts.getJSONObject(0).optString("text")
        if (text.isBlank()) return null

        val parsedJson = JSONObject(text)

        val verdict = ClaimVerificationVerdict.fromString(parsedJson.optString("verdict", "CONTEXT_NEEDED"))
        val confidence = ConfidenceLevel.fromString(parsedJson.optString("confidence", "HIGH"))

        val claimsList = mutableListOf<String>()
        parsedJson.optJSONArray("individualClaims")?.let { arr ->
            for (i in 0 until arr.length()) claimsList.add(arr.getString(i))
        }

        val quranRefs = mutableListOf<QuranReferenceItem>()
        parsedJson.optJSONArray("quranReferences")?.let { arr ->
            for (i in 0 until arr.length()) {
                val qObj = arr.getJSONObject(i)
                quranRefs.add(
                    QuranReferenceItem(
                        surahName = qObj.optString("surahName"),
                        surahNumber = qObj.optInt("surahNumber", 0),
                        ayahNumber = qObj.optInt("ayahNumber", 0),
                        arabicText = qObj.optString("arabicText"),
                        translationBn = qObj.optString("translationBn"),
                        whatVerseActuallyAddresses = qObj.optString("whatVerseActuallyAddresses"),
                        isDirectEvidence = qObj.optBoolean("isDirectEvidence", true)
                    )
                )
            }
        }

        val hadithRefs = mutableListOf<HadithReferenceItem>()
        parsedJson.optJSONArray("hadithReferences")?.let { arr ->
            for (i in 0 until arr.length()) {
                val hObj = arr.getJSONObject(i)
                hadithRefs.add(
                    HadithReferenceItem(
                        collection = hObj.optString("collection"),
                        hadithNumber = hObj.optString("hadithNumber"),
                        arabicText = hObj.optString("arabicText"),
                        translationBn = hObj.optString("translationBn"),
                        authenticityGrade = hObj.optString("authenticityGrade"),
                        textVsInterpretation = hObj.optString("textVsInterpretation")
                    )
                )
            }
        }

        val scholarlyList = mutableListOf<ClaimScholarlyOpinion>()
        parsedJson.optJSONArray("scholarlyInterpretations")?.let { arr ->
            for (i in 0 until arr.length()) {
                val sObj = arr.getJSONObject(i)
                scholarlyList.add(
                    ClaimScholarlyOpinion(
                        schoolOrScholar = sObj.optString("schoolOrScholar"),
                        positionSummary = sObj.optString("positionSummary"),
                        textualBasis = sObj.optString("textualBasis")
                    )
                )
            }
        }

        val warnings = mutableListOf<MisinformationWarning>()
        parsedJson.optJSONArray("potentialMisinformation")?.let { arr ->
            for (i in 0 until arr.length()) {
                val wObj = arr.getJSONObject(i)
                warnings.add(
                    MisinformationWarning(
                        warningType = wObj.optString("warningType"),
                        warningDetail = wObj.optString("warningDetail")
                    )
                )
            }
        }

        val estObj = parsedJson.optJSONObject("whatIsActuallyEstablished")
        val whatIsActuallyEstablished = WhatIsActuallyEstablished(
            explicitTextualFact = estObj?.optString("explicitTextualFact") ?: "",
            scholarlyInference = estObj?.optString("scholarlyInference") ?: "",
            uncertainOrUnverified = estObj?.optString("uncertainOrUnverified") ?: ""
        )

        return IslamicContextVerifyReport(
            originalClaim = claimText,
            sourcePlatformTag = sourceTag,
            individualClaims = if (claimsList.isNotEmpty()) claimsList else listOf(claimText),
            verdict = verdict,
            confidence = confidence,
            summaryHeadlineBn = parsedJson.optString("summaryHeadlineBn"),
            evidenceSummaryBn = parsedJson.optString("evidenceSummaryBn"),
            quranReferences = quranRefs,
            hadithReferences = hadithRefs,
            historicalContextBn = parsedJson.optString("historicalContextBn"),
            linguisticContextBn = parsedJson.optString("linguisticContextBn"),
            audienceAndScopeBn = parsedJson.optString("audienceAndScopeBn"),
            omittedSurroundingsBn = parsedJson.optString("omittedSurroundingsBn"),
            scholarlyInterpretations = scholarlyList,
            potentialMisinformation = warnings,
            whatIsActuallyEstablished = whatIsActuallyEstablished,
            actionableConclusionBn = parsedJson.optString("actionableConclusionBn")
        )
    }

    /**
     * Local bookmarks / saved verifications management.
     */
    fun saveReport(report: IslamicContextVerifyReport) {
        val saved = getSavedReports().toMutableList()
        saved.removeAll { it.id == report.id }
        saved.add(0, report.copy(isSaved = true))
        persistSavedReports(saved.take(50))
    }

    fun removeSavedReport(reportId: String) {
        val saved = getSavedReports().toMutableList()
        saved.removeAll { it.id == reportId }
        persistSavedReports(saved)
    }

    fun getSavedReports(): List<IslamicContextVerifyReport> {
        val jsonStr = prefs.getString("saved_reports_json", null) ?: return emptyList()
        return try {
            val arr = JSONArray(jsonStr)
            val list = mutableListOf<IslamicContextVerifyReport>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    IslamicContextVerifyReport(
                        id = obj.optString("id"),
                        originalClaim = obj.optString("originalClaim"),
                        sourcePlatformTag = obj.optString("sourcePlatformTag"),
                        summaryHeadlineBn = obj.optString("summaryHeadlineBn"),
                        verdict = ClaimVerificationVerdict.fromString(obj.optString("verdict")),
                        confidence = ConfidenceLevel.fromString(obj.optString("confidence")),
                        actionableConclusionBn = obj.optString("actionableConclusionBn"),
                        timestamp = obj.optLong("timestamp", System.currentTimeMillis()),
                        isSaved = true
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun persistSavedReports(list: List<IslamicContextVerifyReport>) {
        val arr = JSONArray()
        list.forEach { r ->
            arr.put(JSONObject().apply {
                put("id", r.id)
                put("originalClaim", r.originalClaim)
                put("sourcePlatformTag", r.sourcePlatformTag)
                put("summaryHeadlineBn", r.summaryHeadlineBn)
                put("verdict", r.verdict.name)
                put("confidence", r.confidence.name)
                put("actionableConclusionBn", r.actionableConclusionBn)
                put("timestamp", r.timestamp)
            })
        }
        prefs.edit().putString("saved_reports_json", arr.toString()).apply()
    }
}
