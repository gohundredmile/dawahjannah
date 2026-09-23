package com.example.data.model

/**
 * Data models for the "Dua by Situation" search engine.
 * Supports dual-axis selection: "I feel..." (Emotional State) & "I need..." (Spiritual Need)
 * and custom situation searches, while maintaining 100% authentic Islamic citations.
 */

enum class DuaSourceCategory {
    QURAN,
    HADITH,
    SUNNAH_ADAB
}

data class EmotiveFeeling(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val emoji: String,
    val descriptionBn: String
)

data class SpiritualNeed(
    val id: String,
    val titleEn: String,
    val titleBn: String,
    val emoji: String,
    val descriptionBn: String
)

data class SituationDuaItem(
    val id: String,
    val category: DuaSourceCategory,
    val titleBn: String,
    val arabicText: String,
    val banglaPronunciation: String,
    val banglaTranslation: String,
    val referenceCitation: String,
    val hadithBookBn: String? = null,
    val hadithNumber: String? = null,
    val gradingBn: String? = null,
    val narratorCompanionBn: String? = null,
    val surahNameBn: String? = null,
    val surahNumber: Int? = null,
    val ayahNumber: Int? = null,
    val contextAndTafsirBn: String,
    val sunnahPracticeMethodBn: String,
    val feelingTags: List<String>,
    val needTags: List<String>,
    val recommendedCount: Int = 1,
    val scholarlyNuanceBn: String? = null
)

data class SituationFilterState(
    val feelingId: String? = null,
    val needId: String? = null,
    val customFeelingQuery: String = "",
    val customNeedQuery: String = ""
)
