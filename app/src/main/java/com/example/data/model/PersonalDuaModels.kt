package com.example.data.model

/**
 * Data models for "Personal Dua Builder".
 * Strictly maintains scholarly distinction between Quran, Hadith, General Permissible Supplication,
 * and Scholarly rulings without fabrication.
 */

data class QuranicDuaItem(
    val id: String,
    val surahNameBn: String,
    val surahNameAr: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String,
    val tafsirContextBn: String,
    val revelationReasonBn: String = "",
    val referenceText: String = "আল-কুরআন, সূরা $surahNameBn ($surahNumber:$ayahNumber)"
)

data class PropheticDuaItem(
    val id: String,
    val hadithBookBn: String,
    val hadithNumber: String,
    val narratorCompanionBn: String,
    val gradingBn: String = "সহীহ (Authentic)",
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String,
    val sunnahPracticeMethodBn: String,
    val occasionOfUsageBn: String,
    val repetitionRecommendation: String = "১ বা ৩ বার"
)

data class PermissibleSupplicationItem(
    val id: String,
    val titleBn: String,
    val heartfeltSupplicationBn: String,
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
