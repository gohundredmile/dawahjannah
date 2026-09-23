package com.example.data.model

data class SemanticQuranAyah(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBn: String,
    val surahNameAr: String,
    val surahNameEn: String,
    val revelationTypeBn: String, // "মাক্কী" or "মাদানী"
    val arabicText: String,
    val transliterationBn: String,
    val banglaTranslation: String,
    val englishTranslation: String,
    val tafsirSummaryBn: String,
    val divineWisdomBn: String, // How this Ayah resolves the emotion/situation
    val relatedThemes: List<String>,
    val primaryTopicBn: String,
    val primaryTopicEn: String,
    val relatedHadithBn: String? = null,
    val audioUrl: String = ""
)

data class SemanticSearchSuggestion(
    val titleEn: String,
    val titleBn: String,
    val categoryBn: String,
    val iconEmoji: String
)
