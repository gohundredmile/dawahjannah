package com.example.data.model

/**
 * Data models for "Dua Architect — Personal Dua Builder".
 * Strictly maintains scholarly distinction between Quran, Hadith, General Permissible Supplication,
 * and Scholarly rulings without fabrication.
 * Every Dua is grounded exclusively in the Holy Qur'an and Sahih Hadith.
 */

enum class DuaConstructionMode(val titleBn: String, val titleEn: String, val descriptionBn: String) {
    AUTHENTIC_SINGLE(
        titleBn = "Mode A — একক প্রামাণ্য দু'আ",
        titleEn = "Mode A — Authentic Dua",
        descriptionBn = "কুরআন বা সহীহ হাদীসের হুবহু অপরিবর্তিত একক মূল দু'আ।"
    ),
    CURATED_COLLECTION(
        titleBn = "Mode B — সংকলিত দো'আমালা",
        titleEn = "Mode B — Curated Dua",
        descriptionBn = "পরিস্থিতির বিভিন্ন দিককে সম্বোধন করে একাধিক সহীহ দু'আর প্রামাণ্য সংকলন।"
    ),
    PERSONALIZED_FLOW(
        titleBn = "Mode C — ব্যক্তিগত কাঠামো",
        titleEn = "Mode C — Personalized Dua",
        descriptionBn = "প্রামাণ্য দো'আসমূহকে সংযুক্ত করে বিনম্র ব্যক্তিগত আরজি বিন্যাস।"
    )
}

enum class DuaSourceType {
    QURAN,
    HADITH
}

data class DuaSourceMapItem(
    val id: String,
    val sourceType: DuaSourceType,
    val titleBn: String,
    val titleEn: String = "",
    val referenceText: String,
    val surahNameBn: String? = null,
    val surahNameEn: String? = null,
    val surahNameAr: String? = null,
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val hadithBookBn: String? = null,
    val hadithBookEn: String? = null,
    val hadithNumber: String? = null,
    val narratorCompanionBn: String? = null,
    val gradingBn: String = "সহীহ (Authentic)",
    val arabicSourceText: String,
    val translationBn: String,
    val translationEn: String = "",
    val pronunciationBn: String = "",
    val tafsirContextBn: String = "",
    val sunnahPracticeMethodBn: String = "",
    val occasionOfUsageBn: String = "",
    val recommendedCount: Int = 1
)

data class QuranicDuaItem(
    val id: String,
    val surahNameBn: String,
    val surahNameAr: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String,
    val englishTranslation: String = "",
    val tafsirContextBn: String,
    val revelationReasonBn: String = "",
    val referenceText: String = "আল-কুরআন, সূরা $surahNameBn ($surahNumber:$ayahNumber)"
)

data class PropheticDuaItem(
    val id: String,
    val hadithBookBn: String,
    val hadithBookEn: String = "",
    val hadithNumber: String,
    val narratorCompanionBn: String,
    val gradingBn: String = "সহীহ (Authentic)",
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String,
    val englishTranslation: String = "",
    val sunnahPracticeMethodBn: String,
    val occasionOfUsageBn: String,
    val repetitionRecommendation: String = "১ বা ৩ বার"
)

data class PermissibleSupplicationItem(
    val id: String,
    val titleBn: String,
    val heartfeltSupplicationBn: String,
    val heartfeltSupplicationEn: String = "",
    val invokedNamesOfAllahBn: List<String> = emptyList(),
    val islamicGuidelineBn: String = "মাতৃভাষায় বিনম্র আবেগ দিয়ে আরজি পেশের বৈধতা রয়েছে, শর্ত হলো এতে কোনো শিরক বা গুনাহের আবেদন না থাকা।"
)

data class DuaEtiquetteItem(
    val id: String,
    val timingTitleBn: String,
    val timingDescriptionBn: String,
    val hadithEvidenceBn: String,
    val practicalTipBn: String
)

data class ScholarlyOpinionItem(
    val issueTitleBn: String,
    val dominantScholarlyPositionBn: String,
    val supportingEvidenceBn: String,
    val consensusOrNuanceBn: String
)

data class PersonalDuaBlueprint(
    val id: String,
    val userQuery: String,
    val scenarioTitleBn: String,
    val scenarioTitleEn: String,
    val spiritualComfortBn: String,
    val spiritualComfortEn: String = "",
    val detectedIntentBn: String = "সাধারণ দো'আ ও সাহায্য প্রার্থনা",
    val detectedIntentEn: String = "Supplication & Seeking Divine Assistance",
    val emotionalContextBn: String = "তাওয়াক্কুল ও আন্তরিক আশা",
    val emotionalContextEn: String = "Reliance on Allah & Sincere Hope",
    val peopleInvolvedBn: String = "নিজ ও পরিবার",
    val peopleInvolvedEn: String = "Self & Family",
    val mode: DuaConstructionMode = DuaConstructionMode.CURATED_COLLECTION,
    val curatedArabicText: String = "",
    val curatedTranslationBn: String = "",
    val curatedTranslationEn: String = "",
    val whySelectedBn: String = "",
    val whySelectedEn: String = "",
    val sources: List<DuaSourceMapItem> = emptyList(),
    val quranicDuas: List<QuranicDuaItem>,
    val propheticDuas: List<PropheticDuaItem>,
    val generalSupplications: List<PermissibleSupplicationItem>,
    val goldenTimingsAndEtiquettes: List<DuaEtiquetteItem>,
    val scholarlyClarifications: List<ScholarlyOpinionItem>,
    val practicalRemindersBn: List<String>,
    val isAiEnhanced: Boolean = false
)

data class PersonalDuaScenarioPreset(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val samplePrompt: String,
    val iconCategory: String,
    val badgeBn: String,
    val keywords: List<String>
)
