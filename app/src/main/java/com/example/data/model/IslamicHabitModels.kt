package com.example.data.model

enum class HabitCategory(val titleBn: String, val iconEmoji: String) {
    ALL("সকল সুন্নাহ", "✨"),
    DAILY_ETIQUETTE("দৈনন্দিন শিষ্টাচার ও আহার", "🍽️"),
    ADHKAR_SPIRITUAL("জিকির ও আত্মিক প্রশান্তি", "📿"),
    FAMILY_SOCIAL("পরিবার ও সামাজিক সম্পর্ক", "🤝"),
    VOLUNTARY_WORSHIP("নফল সালাত ও ইবাদত", "🕌"),
    NIGHT_ROUTINE("নিদ্রা ও রাতের আদব", "🌙")
}

data class SunnahHabitItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val category: HabitCategory,
    val iconEmoji: String,
    val arabicText: String,
    val translationBn: String,
    val sourceReference: String,
    val fiqhStatusBn: String, // e.g. "সুন্নাতে মুয়াক্কাদাহ", "মুস্তাহাব / সুন্নাত", "ইসলামী আদব ও শিষ্টাচার"
    val scholarlyContextBn: String,
    val gentleReflectionBn: String,
    val practicalStepBn: String,
    val recommendedFrequencyBn: String
)

data class SunnahWeeklyStats(
    val completedPracticesCount: Int = 0,
    val developingPracticesCount: Int = 0,
    val revisitPracticesCount: Int = 0,
    val totalLoggedThisWeek: Int = 0,
    val inspiringHadithArabic: String = "أَحَبُّ الأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ",
    val inspiringHadithBn: String = "আল্লাহর কাছে সর্বাধিক প্রিয় আমল তা-ই, যা নিয়মিত করা হয়—যদিও তা পরিমাণে অল্প হয়। (সহীহ বুখারী ৬৪৬৫)",
    val gentleAdviceBn: String = "ইবাদত কোনো প্রতিযোগিতার বিষয় নয়; আল্লাহর ভালোবাসায় নিয়মিত ছোট একটি সুন্নাহ আঁকড়ে ধরাই প্রকৃত সাফল্য।"
)
