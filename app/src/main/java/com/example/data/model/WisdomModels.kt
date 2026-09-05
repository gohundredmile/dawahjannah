package com.example.data.model

data class QuranWisdomItem(
    val arabicText: String,
    val bengaliText: String,
    val englishText: String,
    val surahNameEn: String,
    val surahNameAr: String,
    val ayahNumber: Int,
    val surahNumber: Int,
    val isLiveApi: Boolean = true
) {
    val surahAyahFormatted: String
        get() = "Surah: $surahNameEn ($surahNameAr)"

    val ayahFormatted: String
        get() = "Ayah: $surahNumber:$ayahNumber"
}

data class HadithWisdomItem(
    val bengaliText: String,
    val englishRef: String,
    val sourceName: String,
    val narratorOrNumber: String,
    val isLiveApi: Boolean = true
)

data class QuoteWisdomItem(
    val quoteBn: String,
    val quoteEn: String,
    val author: String,
    val tag: String = "INSPIRATION",
    val isLiveApi: Boolean = true
)

data class DailyWisdomState(
    val quran: QuranWisdomItem = DEFAULT_QURAN_WISDOM,
    val hadith: HadithWisdomItem = DEFAULT_HADITH_WISDOM,
    val quote: QuoteWisdomItem = DEFAULT_QUOTE_WISDOM,
    val isLiveConnected: Boolean = true,
    val isLoading: Boolean = false,
    val lastUpdatedMillis: Long = System.currentTimeMillis()
)

val DEFAULT_QURAN_WISDOM = QuranWisdomItem(
    arabicText = "أَفَأَنتَ تُسْمِعُ الصُّمَّ أَوْ تَهْدِي الْعُمْيَ وَمَن كَانَ فِي ضَلَالٍ مُّبِينٍ",
    bengaliText = "আপনি কি বধিরকে শোনাতে পারবেন? অথবা যে অন্ধ ও যে স্পষ্ট পথ ভ্রষ্টতায় লিপ্ত, তাকে পথ প্রদর্শন করতে পারবেন?",
    englishText = "\"Then will you make the deaf hear, [O Muhammad], or guide the blind or he who is in clear error?\"",
    surahNameEn = "Az-Zukhruf",
    surahNameAr = "سُوْرَةُ الزُّخْرُفِ",
    surahNumber = 43,
    ayahNumber = 40,
    isLiveApi = true
)

val DEFAULT_HADITH_WISDOM = HadithWisdomItem(
    bengaliText = "ইবনু মাস'উদ (রাযি.) হতে বর্ণিত। তিনি বলেন, কুরাইশরা যখন ইসলাম গ্রহণে দেরি করছিল, তখন নবী সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম তাদের বিরুদ্ধে দু'আ করলেন। পরিণামে তাদেরকে দুর্ভিক্ষ এমনভাবে গ্রাস করল যে, তারা ধ্বংস হতে লাগল এবং মৃত দেহ ও হাড়গোড় খেতে লাগল। তখন আবূ সুফইয়ান (ইসলাম গ্রহণের পূর্বে) নবী...",
    englishRef = "\"Sahih Al-Bukhari, Hadith: 1020\"",
    sourceName = "Sahih Al-Bukhari",
    narratorOrNumber = "Hadith No: 1020",
    isLiveApi = true
)

val DEFAULT_QUOTE_WISDOM = QuoteWisdomItem(
    quoteBn = "সত্যিকারের সাফল্য আমাদের মূল্যবোধের সাথে আপস না করে আমাদের সম্ভাবনায় পৌঁছানো।",
    quoteEn = "\"True success is reaching our potential without compromising our values.\"",
    author = "Muhammad Ali",
    tag = "INSPIRATION",
    isLiveApi = true
)
