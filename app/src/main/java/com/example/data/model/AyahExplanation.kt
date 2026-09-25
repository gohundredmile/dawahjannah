package com.example.data.model

data class WordMeaning(
    val arabicWord: String,
    val bengaliMeaning: String,
    val englishMeaning: String = "",
    val grammarNote: String = ""
)

data class RelatedVerse(
    val surahNameBn: String,
    val ayahRef: String,
    val arabicText: String,
    val translationBn: String
)

data class RelatedHadith(
    val sourceBn: String,
    val narratorBn: String,
    val textBn: String,
    val gradeBn: String = "সহীহ"
)

data class AyahExplanation(
    val id: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameArabic: String,
    val surahNameBangla: String,
    val surahNameEnglish: String,
    val revelationTypeBn: String, // মাক্কী / মাদানী
    val totalAyahsInSurah: Int,
    val arabicText: String,
    val transliterationBn: String,
    val banglaTranslation: String,
    val englishTranslation: String,
    val wordByWord: List<WordMeaning>,
    val tafsirBn: String,
    val contextBn: String, // শানে নুযূল
    val relatedVerses: List<RelatedVerse>,
    val relatedHadiths: List<RelatedHadith>,
    val audioUrl: String,
    val reciterNameBn: String = "মিশারী রাশিদ আল-আফাসী",
    val scanDurationMs: Long = 32L
)
