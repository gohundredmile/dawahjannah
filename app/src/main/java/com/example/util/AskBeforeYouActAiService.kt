package com.example.util

import android.content.Context
import com.example.BuildConfig
import com.example.data.datasource.AskActionKnowledgeBase
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

/**
 * Islamic Decision-Support & Guidance Engine (AI Service).
 * Follows strict anti-fabrication guidelines, source-first verification,
 * and structured non-binary decision assistance.
 */
class AskBeforeYouActAiService(private val context: Context) {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val prefs by lazy {
        context.getSharedPreferences("ask_action_prefs", Context.MODE_PRIVATE)
    }

    fun getEffectiveApiKey(): String {
        val userKey = prefs.getString("custom_gemini_api_key", null)?.trim()
        if (!userKey.isNullOrBlank() && !userKey.contains("your_api_key_here")) {
            return userKey
        }
        return try {
            val key = BuildConfig.GEMINI_API_KEY
            if (key.isNotBlank() && key != "your_api_key_here") key else ""
        } catch (_: Throwable) {
            ""
        }
    }

    /**
     * Generates 2-4 targeted, high-value diagnostic questions for custom situations.
     * Incorporates missing facts, contract structure, penalties, and allows "Not sure" / "Skip".
     */
    suspend fun generateDynamicQuestions(
        intendedAction: String,
        documentSnippet: String = ""
    ): List<DiagnosticQuestion> = withContext(Dispatchers.IO) {
        val cleanQuery = intendedAction.trim()
        val matchedScenario = AskActionKnowledgeBase.findScenario(cleanQuery)

        val apiKey = getEffectiveApiKey()
        if (apiKey.isBlank()) {
            return@withContext matchedScenario?.diagnosticQuestions
                ?: AskActionKnowledgeBase.scenarios.first().diagnosticQuestions
        }

        try {
            val questions = callGeminiForQuestions(cleanQuery, documentSnippet, apiKey)
            if (!questions.isNullOrEmpty()) {
                return@withContext questions
            }
        } catch (_: Exception) {
            // Graceful fallback to authentic offline catalogue
        }

        matchedScenario?.diagnosticQuestions ?: AskActionKnowledgeBase.scenarios.first().diagnosticQuestions
    }

    /**
     * Analyzes document / contract clauses (Section 17: Document-Aware Analysis).
     */
    suspend fun analyzeDocumentClauses(
        documentText: String
    ): List<DocumentClauseAnalysis> = withContext(Dispatchers.IO) {
        val cleanText = documentText.trim()
        if (cleanText.isBlank()) return@withContext emptyList()

        val apiKey = getEffectiveApiKey()
        if (apiKey.isNotBlank()) {
            try {
                val aiClauses = callGeminiForDocumentAnalysis(cleanText, apiKey)
                if (!aiClauses.isNullOrEmpty()) {
                    return@withContext aiClauses
                }
            } catch (_: Exception) {
                // Fallback to offline rule-based parser
            }
        }

        AskActionKnowledgeBase.analyzePastedDocument(cleanText)
    }

    /**
     * Produces comprehensive Islamic Decision-Support Report (Sections 9, 20 & 25).
     * Distinguishes Revelation (Quran), Hadith, Tafsir, Scholarly Views, and AI Synthesis.
     * ZERO false precision / percentages.
     */
    suspend fun evaluateAction(
        intendedAction: String,
        answers: Map<String, String>,
        documentText: String = "",
        fallbackScenario: AskScenario? = null
    ): AskBeforeYouActReport = withContext(Dispatchers.IO) {
        val cleanQuery = intendedAction.trim()
        val scenario = fallbackScenario ?: AskActionKnowledgeBase.findScenario(cleanQuery)
            ?: AskActionKnowledgeBase.scenarios.first()

        val clauses = if (documentText.isNotBlank()) {
            analyzeDocumentClauses(documentText)
        } else {
            emptyList()
        }

        val apiKey = getEffectiveApiKey()
        if (apiKey.isBlank()) {
            return@withContext AskActionKnowledgeBase.evaluateAnswers(
                scenario = scenario,
                userQuery = cleanQuery,
                selectedOptionIds = answers,
                documentClauses = clauses
            )
        }

        try {
            val aiReport = callGeminiForEvaluation(cleanQuery, answers, documentText, scenario, clauses, apiKey)
            if (aiReport != null) {
                return@withContext aiReport
            }
        } catch (_: Exception) {
            // Fallback to verified local database
        }

        AskActionKnowledgeBase.evaluateAnswers(
            scenario = scenario,
            userQuery = cleanQuery,
            selectedOptionIds = answers,
            documentClauses = clauses
        )
    }

    // --- Private Gemini API Implementations ---

    private fun callGeminiForQuestions(
        intendedAction: String,
        documentSnippet: String,
        apiKey: String
    ): List<DiagnosticQuestion>? {
        val systemInstruction = """
            You are "Ask Before You Act", an Islamic decision-support assistant embedded in an Android app.
            Your task is NOT to issue halal/haram verdicts.
            Instead, understand what the user wants to do and generate exactly 2 to 4 high-value follow-up questions to identify the material Islamic legal factors (such as Interest/riba, contract structure, ownership, late payment penalties, risk allocation, or purpose).
            
            RULES:
            1. Keep questions short, neutral, and clear.
            2. Each question MUST provide 3 to 4 realistic options, AND ALWAYS INCLUDE an option for "নিশ্চিত নই / চুক্তিপত্র দেখতে হবে" (Not sure / Need to check contract).
            3. Each option must have a riskWeight: 0 (safe/compliant), 1 (caution/conditional), 2 (high risk/prohibited), or -1 (unknown/missing info).
            4. Respond ONLY with valid JSON.
            
            JSON Schema:
            [
              {
                "id": "q1",
                "questionBn": "বাংলায় প্রশ্ন",
                "questionEn": "Question in English",
                "whyItMattersBn": "কেন এই তথ্যটি শরঈ দৃষ্টিকোণ থেকে গুরুত্বপূর্ণ",
                "options": [
                  { "id": "opt1", "labelBn": "অপশন ১", "riskWeight": 0, "impactExplanationBn": "শরঈ প্রভাব" },
                  { "id": "opt2", "labelBn": "নিশ্চিত নই / জানি না", "riskWeight": -1, "impactExplanationBn": "তথ্য অনুপস্থিত" }
                ]
              }
            ]
        """.trimIndent()

        val prompt = buildString {
            appendLine("User's intended action: $intendedAction")
            if (documentSnippet.isNotBlank()) {
                appendLine("Document snippet / contract context: $documentSnippet")
            }
            appendLine("Generate the 2-4 diagnostic questions in Bengali and English.")
        }

        val jsonResponse = executeGeminiJsonCall(prompt, systemInstruction, apiKey) ?: return null
        val jsonArray = JSONArray(jsonResponse)
        val result = mutableListOf<DiagnosticQuestion>()

        for (i in 0 until jsonArray.length()) {
            val qObj = jsonArray.getJSONObject(i)
            val optsArray = qObj.getJSONArray("options")
            val optionsList = mutableListOf<DiagnosticOption>()

            for (j in 0 until optsArray.length()) {
                val oObj = optsArray.getJSONObject(j)
                optionsList.add(
                    DiagnosticOption(
                        id = oObj.optString("id", "opt_${i}_$j"),
                        labelBn = oObj.optString("labelBn", ""),
                        riskWeight = oObj.optInt("riskWeight", 0),
                        impactExplanationBn = oObj.optString("impactExplanationBn", "")
                    )
                )
            }

            result.add(
                DiagnosticQuestion(
                    id = qObj.optString("id", "q_$i"),
                    questionBn = qObj.optString("questionBn", ""),
                    questionEn = qObj.optString("questionEn", ""),
                    whyItMattersBn = qObj.optString("whyItMattersBn", ""),
                    options = optionsList
                )
            )
        }

        return if (result.isNotEmpty()) result else null
    }

    private fun callGeminiForDocumentAnalysis(
        documentText: String,
        apiKey: String
    ): List<DocumentClauseAnalysis>? {
        val systemInstruction = """
            You are "Ask Before You Act: Document & Contract Analyzer".
            Analyze the provided contract clauses, terms and conditions, or agreement snippet.
            Identify key clauses affecting Islamic compliance: Interest/riba clauses, late-payment penalty charges, ambiguity/gharar, ownership transfer terms, or liability.
            
            RULES:
            1. Quote the exact snippet from the document.
            2. Clearly separate: "The document says..." (documentMeaningBn) from "The Islamic significance of this clause may be..." (islamicSignificanceBn).
            3. Do not claim this alone constitutes a binding fatwa.
            4. Respond ONLY with valid JSON.
            
            JSON Schema:
            [
              {
                "clauseTitle": "ধারার শিরোনাম (যেমন: বিলম্ব জরিমানা ধারা)",
                "quotedText": "নথি থেকে উদ্ধৃত অংশ",
                "documentMeaningBn": "নথির প্রত্যক্ষ অর্থ",
                "islamicSignificanceBn": "শরঈ তাৎপর্য ও সম্ভাব্য ঝুঁকি",
                "riskCategoryBn": "রিবা / সুদ, বিলম্ব জরিমানা, ইত্যাদি",
                "isConcerning": true
              }
            ]
        """.trimIndent()

        val jsonResponse = executeGeminiJsonCall(documentText.take(2000), systemInstruction, apiKey) ?: return null
        val jsonArray = JSONArray(jsonResponse)
        val result = mutableListOf<DocumentClauseAnalysis>()

        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            result.add(
                DocumentClauseAnalysis(
                    clauseTitle = obj.optString("clauseTitle", "ধারা পর্যালোচনা"),
                    quotedText = obj.optString("quotedText", ""),
                    documentMeaningBn = obj.optString("documentMeaningBn", ""),
                    islamicSignificanceBn = obj.optString("islamicSignificanceBn", ""),
                    riskCategoryBn = obj.optString("riskCategoryBn", "পর্যালোচনা"),
                    isConcerning = obj.optBoolean("isConcerning", false)
                )
            )
        }

        return if (result.isNotEmpty()) result else null
    }

    private fun callGeminiForEvaluation(
        userAction: String,
        answers: Map<String, String>,
        documentText: String,
        baseScenario: AskScenario,
        clauses: List<DocumentClauseAnalysis>,
        apiKey: String
    ): AskBeforeYouActReport? {
        val systemInstruction = """
            You are "Ask Before You Act", an Islamic guidance and decision-support engine.
            Your purpose is NOT to issue instant halal/haram verdicts or binary buttons.
            ABSOLUTE ANTI-FABRICATION RULE:
            - Never fabricate Qur'an verses, Hadith, Hadith numbers, or scholarly consensus.
            - If a source cannot be verified, state: "I cannot verify the exact source from available material."
            - Distinguish Revelation (Quran), Hadith, Tafsir, Fiqh Scholarly Views, and AI Synthesis.
            - Never output percentages or halal scores (NO "90% halal").
            
            Valid assessment categories:
            - CLEARLY_SUPPORTED
            - CONDITIONALLY_PERMISSIBLE
            - SCHOLARLY_DISAGREEMENT
            - GENERALLY_PROHIBITED
            - INSUFFICIENT_INFORMATION
            - REQUIRES_SCHOLARLY_REVIEW
            
            Respond ONLY in valid JSON matching this schema:
            {
              "whatIUnderstandBn": "পরিস্থিতির সারসংক্ষেপ",
              "importantFacts": ["গুরুত্বপূর্ণ শর্ত ১", "সতর্কতা ২"],
              "assessmentStatus": "CONDITIONALLY_PERMISSIBLE",
              "assessmentSummaryBn": "সার্বিক শরঈ মূল্যায়ন ও মূল বক্তব্য",
              "relevantIslamicPrinciples": ["কুরআন-সুন্নাহর মূলনীতি ১", "ফিকহি মূলনীতি ২"],
              "whatIsStillUnclear": ["অনুপস্থিত তথ্য বা শর্ত"],
              "practicalNextSteps": ["করণীয় পদক্ষেপ ১", "মুফতিকে জিজ্ঞাসা করার প্রশ্ন ২"],
              "authenticSources": ["কুরআন ও হাদিস রেফারেন্স"]
            }
        """.trimIndent()

        val prompt = buildString {
            appendLine("User's intended action: $userAction")
            appendLine("Answers to diagnostic questions:")
            answers.forEach { (q, a) ->
                appendLine("- $q: $a")
            }
            if (documentText.isNotBlank()) {
                appendLine("Document snippet:")
                appendLine(documentText.take(1000))
            }
        }

        val jsonResponse = executeGeminiJsonCall(prompt, systemInstruction, apiKey) ?: return null
        val root = JSONObject(jsonResponse)

        val statusStr = root.optString("assessmentStatus", "CONDITIONALLY_PERMISSIBLE")
        val assessment = try {
            ShariahAssessmentStatus.valueOf(statusStr)
        } catch (_: Exception) {
            ShariahAssessmentStatus.CONDITIONALLY_PERMISSIBLE
        }

        val importantFacts = mutableListOf<String>()
        root.optJSONArray("importantFacts")?.let { arr ->
            for (i in 0 until arr.length()) importantFacts.add(arr.getString(i))
        }

        val principles = mutableListOf<String>()
        root.optJSONArray("relevantIslamicPrinciples")?.let { arr ->
            for (i in 0 until arr.length()) principles.add(arr.getString(i))
        }

        val unclear = mutableListOf<String>()
        root.optJSONArray("whatIsStillUnclear")?.let { arr ->
            for (i in 0 until arr.length()) unclear.add(arr.getString(i))
        }

        val nextSteps = mutableListOf<String>()
        root.optJSONArray("practicalNextSteps")?.let { arr ->
            for (i in 0 until arr.length()) nextSteps.add(arr.getString(i))
        }

        val sources = mutableListOf<String>()
        root.optJSONArray("authenticSources")?.let { arr ->
            for (i in 0 until arr.length()) sources.add(arr.getString(i))
        }

        return AskBeforeYouActReport(
            id = "ai_rep_${System.currentTimeMillis()}",
            query = userAction,
            matchedScenarioTitleBn = baseScenario.titleBn,
            whatIUnderstandBn = root.optString("whatIUnderstandBn", "আপনার বর্ণিত পরিস্থিতি পর্যালোচনা করা হয়েছে।"),
            importantFacts = if (importantFacts.isNotEmpty()) importantFacts else listOf("চুক্তির শর্তাবলি ও ইনপুট পর্যবেক্ষণ করা হয়েছে।"),
            assessment = assessment,
            assessmentSummaryBn = root.optString("assessmentSummaryBn", "শরঈ সিদ্ধান্ত সহায়িকা প্রতিবেদন।"),
            relevantIslamicPrinciples = if (principles.isNotEmpty()) principles else listOf("লেনদেনে স্বচ্ছতা ও সুদের নিষেধাজ্ঞা।"),
            quranProofs = baseScenario.quranProofs,
            hadithProofs = baseScenario.hadithProofs,
            fiqhMaxims = baseScenario.fiqhMaxims,
            scholarlyPositions = baseScenario.scholarlyPositions,
            whatIsStillUnclear = unclear,
            practicalNextSteps = if (nextSteps.isNotEmpty()) nextSteps else listOf("অভিজ্ঞ আলেমের সাথে পরামর্শ করুন।"),
            halalAlternatives = baseScenario.halalAlternatives,
            clauseAnalyses = clauses,
            selectedOptionLabels = answers,
            authenticSourcesList = if (sources.isNotEmpty()) sources else listOf("পবিত্র কুরআন ও সহীহ হাদীস"),
            isAiGenerated = true
        )
    }

    private fun executeGeminiJsonCall(
        userPrompt: String,
        systemInstruction: String,
        apiKey: String
    ): String? {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

        val requestBodyJson = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "user")
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", userPrompt) })
                    })
                })
            })
            put("systemInstruction", JSONObject().apply {
                put("parts", JSONArray().apply {
                    put(JSONObject().apply { put("text", systemInstruction) })
                })
            })
            put("generationConfig", JSONObject().apply {
                put("responseMimeType", "application/json")
                put("temperature", 0.2)
            })
        }

        val request = Request.Builder()
            .url(url)
            .post(requestBodyJson.toString().toRequestBody("application/json".toMediaType()))
            .build()

        val response = httpClient.newCall(request).execute()
        if (!response.isSuccessful) return null

        val responseBody = response.body?.string() ?: return null
        val responseJson = JSONObject(responseBody)
        val text = responseJson.optJSONArray("candidates")
            ?.optJSONObject(0)
            ?.optJSONObject("content")
            ?.optJSONArray("parts")
            ?.optJSONObject(0)
            ?.optString("text")

        return text?.trim()
    }
}
