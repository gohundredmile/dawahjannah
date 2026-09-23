package com.example.data.model

enum class ShariahRiskLevel(
    val labelBn: String,
    val titleBn: String,
    val subtitleBn: String,
    val primaryColorHex: Long
) {
    SAFE_COMPLIANT(
        labelBn = "শরীয়াহসম্মত",
        titleBn = "সাধারণত অনুমোদিত ও শরীয়াহ অনুকূল",
        subtitleBn = "উল্লেখিত শর্তসমূহে কোনো দৃশ্যমান হারাম বা আপত্তিকর উপাদান পাওয়া যায়নি",
        primaryColorHex = 0xFF059669 // Emerald
    ),
    CONDITIONAL_CAUTION(
        labelBn = "শর্তসাপেক্ষ",
        titleBn = "সতর্কতা ও নির্দিষ্ট শর্ত পূরণ আবশ্যক",
        subtitleBn = "চুক্তির শর্তাবলি সূক্ষ্মভাবে যাচাই প্রয়োজন; অন্যথায় হারাম উপাদানের আশঙ্কা রয়েছে",
        primaryColorHex = 0xFFD97706 // Amber / Gold
    ),
    HIGH_RISK_PROHIBITED(
        labelBn = "উচ্চ ঝুঁকি / নিষিদ্ধ",
        titleBn = "সুস্পষ্ট নিষিদ্ধ বা রিবা/গারার উপাদান বিদ্যমান",
        subtitleBn = "বর্তমান কাঠামোর চুক্তি সরাসরি ইসলামী শরীয়াহর মূলনীতির পরিপন্থি",
        primaryColorHex = 0xFFDC2626 // Crimson Red
    ),
    SCHOLARLY_DEBATE(
        labelBn = "মতপার্থক্যপূর্ণ",
        titleBn = "উলামায়ে কেরামের স্বীকৃত মতপার্থক্য বিদ্যমান",
        subtitleBn = "সমসাময়িক ফিকহবিদ ও একাডেমিভেদে একাধিক স্বীকৃত বৈধ দৃষ্টিভঙ্গি রয়েছে",
        primaryColorHex = 0xFF6366F1 // Indigo
    )
}

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
    FIQH_MAXIM(
        titleBn = "কাওয়াইদ ফিকহিয়্যাহ • শাস্ত্রীয় মূলনীতি",
        descriptionBn = "মুআমালাতের সর্বসম্মত শাস্ত্রীয় আইনকানুন",
        badgeColorHex = 0xFF0D9488
    ),
    SCHOLARLY_COUNCIL(
        titleBn = "ফিকহ পরিষদ ও সমসাময়িক ফতোয়া",
        descriptionBn = "আন্তর্জাতিক ফিকহ একাডেমি ও মাযহাবসমূহের পর্যবেক্ষণ",
        badgeColorHex = 0xFF4F46E5
    ),
    AI_DIAGNOSTIC(
        titleBn = "এআই সহায়িকা • শিক্ষামূলক বিশ্লেষণ",
        descriptionBn = "ব্যবহারকারীর ইনপুটের ভিত্তিতে বিন্যস্ত পর্যালোচনা (কোনো চূড়ান্ত ফতোয়া নয়)",
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
    val riskWeight: Int, // 0 = safe, 1 = caution, 2 = high risk
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

data class AskBeforeYouActReport(
    val id: String,
    val query: String,
    val matchedScenarioTitleBn: String,
    val selectedOptionLabels: Map<String, String>,
    val riskLevel: ShariahRiskLevel,
    val riskScore: Int,
    val executiveSummaryBn: String,
    val criticalCheckpoints: List<String>,
    val redFlagsIdentified: List<String>,
    val quranProofs: List<VerifiedQuranProof>,
    val hadithProofs: List<VerifiedHadithProof>,
    val fiqhMaxims: List<FiqhMaxim>,
    val scholarlyPositions: List<ScholarlyPosition>,
    val halalAlternatives: List<HalalAlternative>,
    val recommendedNextSteps: List<String>,
    val timestamp: Long = System.currentTimeMillis()
)
