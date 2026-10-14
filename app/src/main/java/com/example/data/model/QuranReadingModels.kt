package com.example.data.model

/**
 * Model representing the user's last read position in the Holy Quran.
 * Automatically bookmarked as the reader navigates through Surahs and Ayahs.
 */
data class QuranLastReadPosition(
    val surahNumber: Int = 1,
    val surahNameBn: String = "আল-ফাতিহা",
    val surahNameAr: String = "الفاتحة",
    val surahNameEn: String = "Al-Fatihah",
    val ayahNumber: Int = 1,
    val totalAyat: Int = 7,
    val revelationTypeBn: String = "মাক্কী",
    val ayahTextAr: String = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
    val ayahTranslationBn: String = "পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে শুরু করছি।",
    val timestamp: Long = System.currentTimeMillis()
) {
    val progressFraction: Float
        get() = if (totalAyat > 0) (ayahNumber.toFloat() / totalAyat.toFloat()).coerceIn(0f, 1f) else 0f

    val progressPercent: Int
        get() = (progressFraction * 100).toInt()
}
