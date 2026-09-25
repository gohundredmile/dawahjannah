package com.example.data.model

enum class IslamicContentType(
    val titleBn: String,
    val titleEn: String,
    val icon: String,
    val colorHex: Long = 0xFF047857
) {
    QURAN("পবিত্র কুরআন (Quran)", "Quran", "📖", 0xFF047857),
    HADITH("সহীহ হাদীস (Hadith)", "Hadith", "📜", 0xFFD97706),
    QUOTE("ইসলামিক বাণী ও হিকমাহ (Quote)", "Quote", "💬", 0xFF2563EB),
    SCHOLAR_STATEMENT("উলামায়ে কেরামের উক্তি / ফতোয়া (Scholar)", "Scholar Statement", "🏛️", 0xFF7C3AED)
}

enum class TargetSourceType(
    val titleBn: String,
    val titleEn: String,
    val icon: String
) {
    ISLAMIC_BOOK("ইসলামিক কিতাব / বই", "Islamic Book", "📚"),
    ARABIC_TEXT("আরবি ক্যালিগ্রাফি / ইবারত", "Arabic Text", "🖋️"),
    URDU_TEXT("উর্দু বয়ান / টেক্সট", "Urdu Text", "🇵🇰"),
    BANGLA_BOOK("বাংলা ইসলামিক গ্রন্থ", "Bangla Islamic Book", "🇧🇩"),
    MOSQUE_POSTER("মসজিদের নোটিশ / পোস্টার", "Mosque Poster", "🕌"),
    HADITH_POSTER("হাদীস বা ইসলামিক পোস্টার", "Hadith Poster", "📌"),
    GENERAL("সাধারণ ইসলামিক উপাদান", "General Material", "🔍")
}

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
    val scanDurationMs: Long = 32L,
    val contentType: IslamicContentType = IslamicContentType.QURAN,
    val targetSourceType: TargetSourceType = TargetSourceType.GENERAL,
    val extractedRawOcrText: String = "",
    val detectedLanguage: String = "আরবী / বাংলা",
    val sourceBookName: String = "",
    val sourceReferenceNumber: String = "",
    val scholarOrNarrator: String = "",
    val authenticityOrGrading: String = "",
    val scholarlyContext: String = ""
)
