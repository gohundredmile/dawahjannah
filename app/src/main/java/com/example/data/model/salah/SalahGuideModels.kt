package com.example.data.model.salah

import androidx.compose.ui.graphics.Color

/**
 * Main navigation sections / tabs for Complete Salah Guide.
 */
enum class SalahSectionTab(val id: String, val titleBn: String, val iconEmoji: String) {
    OVERVIEW("overview", "গাইড হোম", "🕌"),
    STEP_BY_STEP("step_by_step", "ধাপে ধাপে নামাজ", "🧎"),
    DAILY_PRAYERS("daily_prayers", "৫ ওয়াক্ত ও রাকাত", "🔢"),
    RECITATIONS("recitations", "দোয়া ও সূরা", "🗣️"),
    PREPARATION("preparation", "পবিত্রতা ও ওযু", "💧"),
    CONDITIONS("conditions", "শর্ত ও ফরজ-ওয়াজিব", "✅"),
    MISTAKES("mistakes", "ভুল ও সাহু সিজদা", "❌"),
    SPECIAL_PRAYERS("special_prayers", "বিশেষ নামাজ", "🌙"),
    RULINGS("rulings", "মাসআলা ও বিধান", "📚"),
    FAQ("faq", "প্রশ্ন ও উত্তর", "❓")
}

/**
 * Fiqh methodology preference for nuanced scholarly differences.
 */
enum class SalahFiqhMethodology(val id: String, val titleBn: String, val descriptionBn: String) {
    HANAFI("hanafi", "হানাফী মাযহাব (ডিফল্ট)", "উপমহাদেশে বহুল অনুসৃত সহীহ প্রামাণ্য ফিকহী রূপরেখা"),
    SHAFI_OR_GENERAL("shafi", "অন্যান্য স্বীকৃত ফিকহী মতভেদ", "শাফেঈ, মালেকী ও হাম্বলী সহীহ হাদীসভিত্তিক দৃষ্টিভঙ্গি")
}

/**
 * Beginner progressive lesson model.
 */
data class SalahLesson(
    val id: String,
    val serialNumberBn: String,
    val titleBn: String,
    val subtitleBn: String,
    val summaryBn: String,
    val detailedContentBn: String,
    val keyPoints: List<String>,
    val quranHadithReference: String,
    val targetTab: SalahSectionTab? = null
)

/**
 * Daily Prayer Model (Fajr, Dhuhr, Asr, Maghrib, Isha, Jumu'ah).
 */
data class SalahPrayer(
    val id: String,
    val nameBn: String,
    val nameAr: String,
    val timeDescriptionBn: String,
    val totalRakatsBn: String,
    val breakdownBn: String,
    val fardRakats: Int,
    val sunnahMuakkadahRakats: Int,
    val sunnahGhairMuakkadahRakats: Int,
    val wajibRakats: Int,
    val naflRakats: Int,
    val sequenceGuideBn: String,
    val fojilotBn: String,
    val hadithRefBn: String
)

/**
 * Posture type in prayer.
 */
enum class SalahPosture(val titleBn: String, val emoji: String) {
    NIYYAT("নিয়ত ও প্রস্তুতি", "🤲"),
    TAKBIR("তাকবীরে তাহরীমা", "🙌"),
    QIYAM("কিয়াম (দাঁড়ানো)", "🧍"),
    RUKU("রুকু (ঝোঁকা)", "🧎"),
    QAWMAH("কাওমা (সোজা দাঁড়ানো)", "🧍"),
    SAJDAH("সিজদা (নতজানু)", "🙇"),
    JALSAH("জলসা (দুই সিজদার মাঝের বসা)", "🧘"),
    TASHAHHUD("ক্বাদা/বৈঠক (তাশাহহুদ)", "🧘"),
    SALAM("সালাম (সমাপ্তি)", "🕊️")
}

/**
 * Step in prayer execution.
 */
data class SalahStep(
    val stepNumber: Int,
    val posture: SalahPosture,
    val titleBn: String,
    val actionInstructionBn: String,
    val arabicText: String? = null,
    val transliterationBn: String? = null,
    val meaningBn: String? = null,
    val sunnahNoteBn: String? = null,
    val commonMistakeAlertBn: String? = null,
    val repetitionCountBn: String? = null,
    val applicableRakats: String = "প্রতি রাকাতে"
)

/**
 * Recitation items (Sana, Fatiha, Tashahhud, Durood, etc.).
 */
data class SalahRecitation(
    val id: String,
    val titleBn: String,
    val placeInSalahBn: String,
    val arabicText: String,
    val transliterationBn: String,
    val translationBn: String,
    val englishMeaning: String? = null,
    val referenceBn: String,
    val explanationBn: String,
    val isMandatory: Boolean = false,
    val audioDurationSec: Int = 10
)

/**
 * Memorization card status.
 */
enum class MemorizeStatus(val labelBn: String, val color: Color) {
    NEW("নতুন", Color(0xFF64748B)),
    LEARNING("শেখা হচ্ছে", Color(0xFFF59E0B)),
    PARTIALLY_MEMORIZED("আংশিক মুখস্থ", Color(0xFF3B82F6)),
    MEMORIZED("সম্পূর্ণ মুখস্থ", Color(0xFF10B981))
}

/**
 * Preparation & Taharah Guide.
 */
data class SalahPreparationItem(
    val id: String,
    val category: String, // ওযু, গোসল, তায়াম্মুম, সতর ও পোশাক, কিবলা ও স্থান
    val titleBn: String,
    val subtitleBn: String,
    val stepsOrRules: List<String>,
    val duas: List<SalahRecitation> = emptyList(),
    val invalidators: List<String> = emptyList(),
    val precautionsBn: String,
    val referenceBn: String
)

/**
 * Conditions, Fard, Wajib, Sunnah, Makruh.
 */
data class SalahConditionItem(
    val id: String,
    val categoryBn: String, // নামাজের পূর্বে ৭ শর্ত (আহকাম), নামাজের ভিতরের ৬ ফরজ (আরকান), ১৪টি ওয়াজিব, সুন্নাতসমূহ, মাকরূহাতসমূহ, নামাজ ভঙ্গের কারণ
    val titleBn: String,
    val ruleDescriptionBn: String,
    val whyImportantBn: String,
    val ifOmittedBn: String,
    val referenceBn: String
)

/**
 * Common mistake and its correction.
 */
data class SalahMistakeItem(
    val id: String,
    val categoryBn: String, // ওযু, নিয়ত, তাকবির, কিয়াম, কিরাত, রুকু, সিজদা, বৈঠক, সালাম, সময় ও পোশাক, জামাত
    val mistakeTitleBn: String,
    val whyProblematicBn: String,
    val howToCorrectBn: String,
    val evidenceReferenceBn: String
)

/**
 * Sajdah Sahw Guide.
 */
data class SajdahSahwRule(
    val situationBn: String,
    val isSahwRequired: Boolean,
    val reasonBn: String,
    val solutionBn: String,
    val hadithRefBn: String
)

/**
 * Special Prayer (Tahajjud, Witr, Eid, Janazah, Istikhara, etc.).
 */
data class SalahSpecialPrayerItem(
    val id: String,
    val nameBn: String,
    val nameAr: String,
    val timingBn: String,
    val rakatsSummaryBn: String,
    val importanceAndVirtueBn: String,
    val completeMethodBn: String,
    val specialDuaArabic: String? = null,
    val specialDuaTransliteration: String? = null,
    val specialDuaMeaningBn: String? = null,
    val rulesAndEtiquetteBn: String,
    val referencesBn: String
)

/**
 * Ruling / Masail (Musafir, Sick, Women, Jama'ah, Qada).
 */
data class SalahRulingItem(
    val id: String,
    val topicCategoryBn: String, // মুসাফির, অসুস্থ ব্যক্তির নামাজ, নারীদের নামাজ, জামাত ও মাসবূক, কাজা নামাজ
    val questionOrTopicBn: String,
    val directRulingBn: String,
    val detailedExplanationBn: String,
    val practicalExampleBn: String,
    val madhhabNoteBn: String? = null,
    val referenceBn: String
)

/**
 * Searchable FAQ.
 */
data class SalahFAQItem(
    val id: String,
    val questionBn: String,
    val answerBn: String,
    val practicalAdviceBn: String,
    val referenceBn: String,
    val relatedTopicTab: SalahSectionTab
)

/**
 * Quick Reference Card Item.
 */
data class SalahQuickRefItem(
    val id: String,
    val queryBn: String,
    val shortAnswerBn: String,
    val detailedTextBn: String,
    val targetTab: SalahSectionTab
)
