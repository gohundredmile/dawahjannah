package com.example.data.model

/**
 * Islamic Decision-Support Assessment Statuses.
 * In accordance with the "Ask Before You Act" Engine, this explicitly avoids
 * binary "Halal / Haram" buttons, false numerical percentages, or manufactured fatwas.
 */
enum class ShariahAssessmentStatus(
    val labelBn: String,
    val titleBn: String,
    val subtitleBn: String,
    val primaryColorHex: Long
) {
    CLEARLY_SUPPORTED(
        labelBn = "সুস্পষ্টভাবে সমর্থিত",
        titleBn = "কুরআন ও সুন্নাহর প্রামাণ্য দলিলে সরাসরি অনুমোদিত",
        subtitleBn = "বর্ণিত শর্তে কোনো প্রকার রিবা, গারার বা নিষিদ্ধ উপাদানের সংশ্লিষ্টতা পাওয়া যায়নি",
        primaryColorHex = 0xFF059669 // Emerald
    ),
    CONDITIONALLY_PERMISSIBLE(
        labelBn = "শর্তসাপেক্ষ বৈধ",
        titleBn = "নির্দিষ্ট শর্ত পূরণ সাপেক্ষে শরীয়াহ অনুকূল",
        subtitleBn = "মূল চুক্তি বৈধ হলেও শর্তাবলি সূক্ষ্মভাবে পূরণ আবশ্যক; অন্যথায় ঝুঁকির আশঙ্কা রয়েছে",
        primaryColorHex = 0xFFD97706 // Amber / Gold
    ),
    SCHOLARLY_DISAGREEMENT(
        labelBn = "মতপার্থক্যপূর্ণ",
        titleBn = "উলামায়ে কেরাম ও ফিকহ একাডেমিসমূহের স্বীকৃত মতপার্থক্য বিদ্যমান",
        subtitleBn = "আন্তর্জাতিক ফিকহ পরিষদ ও মাযহাবসমূহে একাধিক গ্রহণযোগ্য দৃষ্টিভঙ্গি ও ইজতিহাদ রয়েছে",
        primaryColorHex = 0xFF6366F1 // Indigo
    ),
    GENERALLY_PROHIBITED(
        labelBn = "সাধারণত বর্জনীয়",
        titleBn = "সুস্পষ্ট রিবা, গারার বা শরীয়াহ পরিপন্থী উপাদান বিদ্যমান",
        subtitleBn = "বর্তমান কাঠামোতে চুক্তিটি সরাসরি কুরআন ও সুন্নাহর মৌলিক বিধিনিষেধের সাথে সাংঘর্ষিক",
        primaryColorHex = 0xFFDC2626 // Crimson Red
    ),
    INSUFFICIENT_INFORMATION(
        labelBn = "তথ্য অসম্পূর্ণ",
        titleBn = "গুরুত্বপূর্ণ তথ্যের অভাব — চূড়ান্ত শরঈ পর্যালোচনা স্থগিত",
        subtitleBn = "চুক্তির কাঠামো, লাভ-লোকসান বণ্টন বা শর্তাবলি পর্যাপ্ত না হওয়ায় মূল্যায়ন সম্ভব নয়",
        primaryColorHex = 0xFF0284C7 // Sky Blue
    ),
    REQUIRES_SCHOLARLY_REVIEW(
        labelBn = "মুফতির শরণাপন্ন হোন",
        titleBn = "ব্যক্তিগত পরিস্থিতি বিবেচনায় বিজ্ঞ মুফতির ফতোয়া আবশ্যক",
        subtitleBn = "জটিল আর্থিক বা আইনি বিষয় হওয়ায় প্রামাণ্য তথ্যাদিসহ স্থানীয় আলেমের নিকট উপস্থাপন কাম্য",
        primaryColorHex = 0xFF7C3AED // Purple
    )
}

// Backwards-compatible alias for existing references
typealias ShariahRiskLevel = ShariahAssessmentStatus

enum class IslamicSourceType(
    val titleBn: String,
    val descriptionBn: String,
    val badgeColorHex: Long
) {
    QURAN(
        titleBn = "পবিত্র কুরআন • কালামুল্লাহ",
        descriptionBn = "ঐশী অহী ও সর্বোচ্চ শরঈ দলিল",
        badgeColorHex = 0xFF059669
    ),
    HADITH(
        titleBn = "সহীহ হাদীস • সুন্নাতে রাসুলুল্লাহ ﷺ",
        descriptionBn = "নবুওয়াতী বিধান ও প্রামাণ্য ব্যাখ্যা",
        badgeColorHex = 0xFFB45309
    ),
    TAFSIR(
        titleBn = "তাফসীর • কুরআনের প্রামাণ্য ব্যাখ্যা",
        descriptionBn = "সাহাবা ও মুফাসসিরীনদের স্বীকৃত তাফসীর",
        badgeColorHex = 0xFF0D9488
    ),
    FIQH_MAXIM(
        titleBn = "কাওয়াইদ ফিকহিয়্যাহ • শাস্ত্রীয় মূলনীতি",
        descriptionBn = "মুআমালাতের সর্বসম্মত শাস্ত্রীয় নীতিমালা",
        badgeColorHex = 0xFF0891B2
    ),
    SCHOLARLY_COUNCIL(
        titleBn = "ফিকহ পরিষদ ও স্বীকৃত মাযহাব",
        descriptionBn = "আন্তর্জাতিক ফিকহ একাডেমি (OIC), আয়াওফি (AAOIFI) ও ৪ মাযহাবের পর্যবেক্ষণ",
        badgeColorHex = 0xFF4F46E5
    ),
    AI_DIAGNOSTIC(
        titleBn = "এআই সহায়িকা • শিক্ষামূলক বিশ্লেষণ",
        descriptionBn = "ব্যবহারকারীর তথ্যের ভিত্তিতে বিন্যস্ত পর্যালোচনা (কোনো চূড়ান্ত ফতোয়া বা শরঈ হুকুম নয়)",
        badgeColorHex = 0xFF0284C7
    )
}

data class VerifiedQuranProof(
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBn: String,
    val surahNameAr: String,
    val arabicText: String,
    val banglaTranslation: String,
    val englishTranslation: String,
    val tafsirReferenceBn: String,
    val legalSignificanceBn: String
)

data class VerifiedHadithProof(
    val sourceBookBn: String,
    val hadithNumber: String,
    val authenticityGradeBn: String,
    val narratorBn: String,
    val arabicText: String,
    val banglaTranslation: String,
    val legalSignificanceBn: String
)

data class FiqhMaxim(
    val arabicText: String,
    val banglaTranslation: String,
    val sourceOrOriginBn: String,
    val practicalApplicationBn: String
)

data class ScholarlyPosition(
    val bodyOrSchoolBn: String,
    val verdictSummaryBn: String,
    val argumentAndEvidenceBn: String,
    val conditionsBn: String? = null
)

data class HalalAlternative(
    val titleBn: String,
    val islamicContractBn: String,
    val howItWorksBn: String,
    val whyItIsHalalBn: String
)

data class DiagnosticOption(
    val id: String,
    val labelBn: String,
    val riskWeight: Int, // 0 = safe, 1 = caution, 2 = high risk, -1 = unknown / missing info
    val impactExplanationBn: String
)

data class DiagnosticQuestion(
    val id: String,
    val questionBn: String,
    val questionEn: String,
    val whyItMattersBn: String,
    val options: List<DiagnosticOption>
)

data class AskScenario(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val categoryBn: String,
    val sampleQuery: String,
    val badgeIconName: String,
    val shortSummaryBn: String,
    val diagnosticQuestions: List<DiagnosticQuestion>,
    val quranProofs: List<VerifiedQuranProof>,
    val hadithProofs: List<VerifiedHadithProof>,
    val fiqhMaxims: List<FiqhMaxim>,
    val scholarlyPositions: List<ScholarlyPosition>,
    val halalAlternatives: List<HalalAlternative>
)

/**
 * Feature: Document / Contract Clause Analysis
 * Identifies terms, quotes exact text, clarifies "The document says..." vs "The Islamic significance...",
 * and flags potential Riba, Gharar, late fees, ambiguous conditions.
 */
data class DocumentClauseAnalysis(
    val clauseTitle: String,
    val quotedText: String,
    val documentMeaningBn: String, // "The document says..."
    val islamicSignificanceBn: String, // "The Islamic significance of this clause may be..."
    val riskCategoryBn: String, // "রিবা / সুদ", "বিলম্ব জরিমানা", "গারার / অস্পষ্টতা", "দখলবিহীন বিক্রয়", "স্বাভাবিক ও অনুমোদিত শর্ত"
    val isConcerning: Boolean
)

/**
 * Comprehensive Islamic Decision-Support Output (Compliant with Sections 9, 20 & 25).
 */
data class AskBeforeYouActReport(
    val id: String,
    val query: String,
    val matchedScenarioTitleBn: String,
    val whatIUnderstandBn: String,
    val importantFacts: List<String>,
    val assessment: ShariahAssessmentStatus,
    val assessmentSummaryBn: String,
    val relevantIslamicPrinciples: List<String>,
    val quranProofs: List<VerifiedQuranProof>,
    val hadithProofs: List<VerifiedHadithProof>,
    val fiqhMaxims: List<FiqhMaxim>,
    val scholarlyPositions: List<ScholarlyPosition>,
    val whatIsStillUnclear: List<String>,
    val practicalNextSteps: List<String>,
    val halalAlternatives: List<HalalAlternative>,
    val clauseAnalyses: List<DocumentClauseAnalysis> = emptyList(),
    val selectedOptionLabels: Map<String, String> = emptyMap(),
    val authenticSourcesList: List<String> = emptyList(),
    val isAiGenerated: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) {
    // Convenience property for UI backwards compatibility
    val riskLevel: ShariahAssessmentStatus get() = assessment
    val executiveSummaryBn: String get() = assessmentSummaryBn
    val redFlagsIdentified: List<String> get() = importantFacts.filter { it.contains("সতর্কতা") || it.contains("হারাম") || it.contains("নিষিদ্ধ") }
    val criticalCheckpoints: List<String> get() = whatIsStillUnclear
    val recommendedNextSteps: List<String> get() = practicalNextSteps
    val riskScore: Int get() = 0 // Deprecated: No false numerical precision
}
