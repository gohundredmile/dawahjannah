package com.example.data.model

/**
 * Categories for Islamic Habits & Sunnahs.
 * Expanded to cover all primary dimensions of Sunnah practice.
 */
enum class HabitCategory(val titleBn: String, val iconEmoji: String) {
    ALL("সকল সুন্নাহ", "✨"),
    MORNING("সকাল ও ফজর", "🌅"),
    DAILY_ETIQUETTE("দৈনন্দিন শিষ্টাচার", "☀️"),
    EATING_DRINKING("পানাহার ও খাদ্য", "🍽️"),
    PRAYER_MASJID("সালাত ও মসজিদ", "🕌"),
    EVENING_NIGHT("সন্ধ্যা ও রাত্রি", "🌙"),
    SLEEPING_WAKING("নিদ্রা ও জাগরণ", "😴"),
    FAMILY_SOCIAL("পরিবার ও আত্মীয়তা", "🤝"),
    CHARITY_COMMUNITY("সাদাকাহ ও কল্যাণ", "💰"),
    QURAN_DHIKR("কুরআন ও জিকির", "📖"),
    CLEANLINESS_CARE("পবিত্রতা ও মেসওয়াক", "🧼"),
    FRIDAY_SPECIAL("জুমার বিশেষ সুন্নাত", "🕋"),
    RAMADAN_SEASONAL("রমাদান ও রোজা", "🌙"),
    CHARACTER_AKHLAQ("উত্তম চরিত্র ও ক্ষমা", "❤️"),
    // Backward compatibility aliases
    ADHKAR_SPIRITUAL("জিকির ও আত্মিক প্রশান্তি", "📿"),
    VOLUNTARY_WORSHIP("নফল সালাত ও ইবাদত", "🕌"),
    NIGHT_ROUTINE("নিদ্রা ও রাতের আদব", "🌙")
}

/**
 * Mode of the Islamic Habit System
 */
enum class HabitSystemMode(val titleBn: String, val subtitleBn: String, val iconEmoji: String) {
    GUIDE("সুন্নাহ গাইড", "সুন্নাহর জ্ঞান, আদব ও দলিল", "📖"),
    TRACKER("আমল ট্র্যাকার", "ব্যক্তিগত আমল ও ধারাবাহিকতা", "🌿")
}

/**
 * Habit Developmental Stage (Section 25)
 * Non-punitive, describing consistency rather than religious worth.
 */
enum class HabitDevelopmentStage(val labelBn: String, val badgeColorHex: Long) {
    EXPLORE("অন্বেষণ (Explore)", 0xFF0284C7),
    TRYING("শুরু করেছি (Trying)", 0xFF7C3AED),
    DEVELOPING("চর্চারত (Developing)", 0xFFD97706),
    ESTABLISHED("নিয়মিত (Established)", 0xFF059669),
    REVISIT("পুনরায় শুরু (Revisit)", 0xFF64748B)
}

/**
 * Canonical Sunnah & Habit Item.
 * Powers both Guide Mode and Tracker Mode seamlessly.
 */
data class SunnahHabitItem(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val arabicTitle: String = "",
    val category: HabitCategory,
    val iconEmoji: String,
    val timeOfDay: String = "সারাদিন",
    val shortDescriptionBn: String = "",
    val whyItMattersBn: String = "",
    val howToPerformBn: String = "",
    val commonMistakesBn: String = "",
    val evidenceType: String = "সহীহ হাদিস", // কুরআন, সহীহ হাদিস, হিসনুল মুসলিম, ফিকহী আদব
    val arabicEvidence: String = "",
    val translationBn: String,
    val sourceReference: String,
    val hadithGrade: String = "সহীহ (Sahih)",
    val legalClassification: String = "সুন্নাতে মুয়াক্কাদাহ", // সুন্নাতে মুয়াক্কাদাহ, মুস্তাহাব / নফল, ওয়াজিব, ইসলামী আদব
    val relatedDuaTitleBn: String = "",
    val relatedDuaArabic: String = "",
    val relatedDuaTranslationBn: String = "",
    val relatedDuaTransliteration: String = "",
    val relatedDuaRepeatCount: String = "",
    val scholarlyDifferencesBn: String = "",
    val practicalStepBn: String = "",
    val recommendedFrequencyBn: String = "দৈনিক",
    // Backward compatibility properties:
    val arabicText: String = arabicEvidence,
    val fiqhStatusBn: String = legalClassification,
    val scholarlyContextBn: String = whyItMattersBn,
    val gentleReflectionBn: String = shortDescriptionBn
)

/**
 * Gentle Weekly Stats & Reflection
 */
data class SunnahWeeklyStats(
    val completedPracticesCount: Int = 0,
    val developingPracticesCount: Int = 0,
    val revisitPracticesCount: Int = 0,
    val totalLoggedThisWeek: Int = 0,
    val inspiringHadithArabic: String = "أَحَبُّ الأَعْمَالِ إِلَى اللَّهِ أَدْوَمُهَا وَإِنْ قَلَّ",
    val inspiringHadithBn: String = "আল্লাহর কাছে সর্বাধিক প্রিয় আমল তা-ই, যা নিয়মিত করা হয়—যদিও তা পরিমাণে অল্প হয়। (সহীহ বুখারী ৬৪৬৫)",
    val gentleAdviceBn: String = "আমল ট্র্যাকার কেবল ব্যক্তিগত শৃঙ্খলার জন্য; ইখলাস ও কবুলিয়াত একমাত্র আল্লাহর এখতিয়ারে। অল্প হলেও নিয়মিত চর্চাই বরকতের উৎস।"
)

/**
 * Onboarding / Focus Goal configuration
 */
data class HabitFocusPreferences(
    val selectedAreas: Set<String> = emptySet(),
    val habitDensityTarget: Int = 3, // 1-2, 3-5, or 10+
    val isOnboarded: Boolean = true
)
