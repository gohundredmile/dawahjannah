package com.example.data.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Ramadan Core Tabs / Loops
 */
enum class RamadanLoopTab(val titleBn: String, val subtitleBn: String, val iconEmoji: String) {
    DASHBOARD("হোম ড্যাশবোর্ড", "আজকের দিন ও তাৎক্ষণিক করণীয়", "🌙"),
    BEFORE_RAMADAN("রমাদানের পূর্বে", "প্রস্তুতি, সংকল্প ও প্ল্যানার", "📋"),
    DURING_RAMADAN("রমাদানের দিনগুলোতে", "সিয়াম, কুরআন, ক্বিয়াম ও রুটিন", "✨"),
    DUA_AND_REFLECTION("দো'আ ও ভাবনা", "রমাদান দোয়া ভল্ট ও দৈনিক মুহাসাবা", "🤲"),
    LEARN_AND_VERIFY("জ্ঞান ও যাচাই", "২-মিনিট পাঠ, ম্যাসেজ যাচাই ও দান", "💡"),
    AFTER_RAMADAN("রমাদানের পরে ও স্মৃতি", "ঈদ, শাওয়াল, ধারাবাহিকতা ও জার্নি", "🕊️")
}

/**
 * Adaptive Time of Day for Ramadan Home Dashboard
 */
enum class RamadanTimeOfDay(val titleBn: String, val subtitleBn: String, val iconEmoji: String) {
    MORNING("ফজর ও প্রভাত", "কুরআন টার্গেট, সকালের আযকার ও সিয়ামের সংকল্প", "🌅"),
    AFTERNOON("দুপুর ও অপরাহ্ন", "কুরআন অগ্রগতি, সদাকাহ ও ইফতারের প্রস্তুতি", "☀️"),
    PRE_MAGHRIB("ইফতারের পূর্ব মুহূর্ত", "দোয়া কবুলের বিশেষ ক্ষণ ও ইফতার কাউন্টডাউন", "🌇"),
    NIGHT("মাগরিব, এশা ও রাত", "তারাবীহ, বিতর, তাহাজ্জুদ ও কুরআন তিলাওয়াত", "🌙")
}

/**
 * Ramadan for Different Users (Lifestyle Modes)
 */
enum class RamadanLifestyleMode(
    val id: String,
    val titleBn: String,
    val subtitleBn: String,
    val iconEmoji: String,
    val descriptionBn: String,
    val recommendedQuranJuz: Float = 1.0f,
    val defaultTaraweehRakah: Int = 8,
    val dailyDuaMinutes: Int = 10
) {
    STUDENT(
        id = "student",
        titleBn = "শিক্ষার্থী মোড (Student Mode)",
        subtitleBn = "ক্লাস ও পড়ার ফাঁকে সংক্ষিপ্ত ও একাগ্র আমল",
        iconEmoji = "🎒",
        descriptionBn = "ক্লাস, অ্যাসাইনমেন্ট ও পরীক্ষার চাপের সাথে সমন্বয় রেখে ২০-২৫ মিনিটের সেশন, ফজরের পরে কুরআন ও ইফতারের পূর্বে দো'আর বিশেষ ফোকাস।",
        recommendedQuranJuz = 0.5f,
        defaultTaraweehRakah = 8,
        dailyDuaMinutes = 8
    ),
    PROFESSIONAL(
        id = "professional",
        titleBn = "কর্মজীবী মোড (Working Professional)",
        subtitleBn = "অফিস ও কাজের সাথে সকাল-সন্ধ্যা নিবিড় ইবাদত",
        iconEmoji = "💼",
        descriptionBn = "কর্মক্ষেত্রে আমানতদারী ও জিহ্বার নিয়ন্ত্রণ, যাতায়াতের সময় যিকির এবং ফজর ও তারাবীহ পরবর্তী সময়ে কুরআন তিলাওয়াত।",
        recommendedQuranJuz = 1.0f,
        defaultTaraweehRakah = 8,
        dailyDuaMinutes = 10
    ),
    PARENT(
        id = "parent",
        titleBn = "অভিভাবক মোড (Parent & Family Mode)",
        subtitleBn = "পরিবার ও সন্তানদের নিয়ে বরকতময় পরিবেশ",
        iconEmoji = "👨‍👩‍👧‍👦",
        descriptionBn = "সন্তানদের সাথে নিয়ে সাহরি-ইফতার, ছোটদের রোযার উৎসাহ, পরিবারের সম্মিলিত দো'আ ও ঘরোয়া ক্বিয়াম।",
        recommendedQuranJuz = 0.75f,
        defaultTaraweehRakah = 8,
        dailyDuaMinutes = 12
    ),
    BEGINNER(
        id = "beginner",
        titleBn = "সহজ মোড (Beginner Friendly)",
        subtitleBn = "সহজ ব্যাখ্যা, ছোট লক্ষ্য ও মৌলিক শিক্ষা",
        iconEmoji = "🌱",
        descriptionBn = "নতুন বা দুর্বলদের জন্য মৃদু সূচনা। কোনো চাপ ছাড়া ছোট ছোট সুন্নাত আমল ও ধারাবাহিকতার ওপর জোর দেওয়া হয়।",
        recommendedQuranJuz = 0.33f,
        defaultTaraweehRakah = 8,
        dailyDuaMinutes = 5
    ),
    ADVANCED(
        id = "advanced",
        titleBn = "উন্নত মোড (Advanced Seeker)",
        subtitleBn = "অধিক কুরআন, গভীর তাফসীর ও তাহকীককৃত রেফারেন্স",
        iconEmoji = "🏆",
        descriptionBn = "প্রতিদিন ১-২ পারা কুরআন তাদাব্বুরসহ পাঠ, দীর্ঘ তারাবীহ ও তাহাজ্জুদ, গভীর ইলম চর্চা ও অধিক দান।",
        recommendedQuranJuz = 1.5f,
        defaultTaraweehRakah = 20,
        dailyDuaMinutes = 20
    );

    companion object {
        fun fromId(id: String?): RamadanLifestyleMode =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: PROFESSIONAL
    }
}

/**
 * Goal Level Dimension (Minimum, Target, Stretch)
 */
enum class GoalIntensity(val titleBn: String, val badgeBn: String) {
    MINIMUM("নূন্যতম লক্ষ্য (Minimum)", "বাস্তবধর্মী"),
    TARGET("প্রধান লক্ষ্য (Target)", "ভারসাম্যপূর্ণ"),
    STRETCH("সর্বোচ্চ প্রচেষ্টা (Stretch)", "উচ্চাকাঙ্ক্ষী")
}

/**
 * Source Authority Tag for Islamic Authenticity
 */
enum class RamadanSourceType(val labelBn: String, val badgeColorHex: Long) {
    QURAN("আল-কুরআন", 0xFF059669),
    SAHIH_HADITH("সহীহ হাদীস", 0xFF0284C7),
    FIQH_JURISPRUDENCE("ফিকহ ও মাযাহিব", 0xFFD97706),
    SCHOLARLY_ADVICE("সালাফ ও উলামাদের নসীহত", 0xFF7C3AED)
}

/**
 * Preparation Checklist Item
 */
data class RamadanChecklistItem(
    val key: String,
    val titleBn: String,
    val categoryBn: String,
    val descriptionBn: String,
    val referenceBn: String,
    val sourceType: RamadanSourceType,
    val isDefaultChecked: Boolean = false,
    val isCustom: Boolean = false
)

/**
 * Ramadan Dua Category Model
 */
enum class RamadanDuaCategory(val id: String, val titleBn: String, val iconEmoji: String) {
    SUHOOR("suhoor", "সাহরি ও সিয়ামের সংকল্প", "🥣"),
    FASTING("fasting", "রোযাবস্থা ও আত্মসংযম", "🌿"),
    IFTAR("iftar", "ইফতার ও দো'আ কবুল মুহূর্ত", "🌇"),
    FORGIVENESS("forgiveness", "তাওবাহ ও ক্ষমা প্রার্থনা", "💧"),
    GUIDANCE("guidance", "হিদায়াত ও অবিচলতা", "🧭"),
    FAMILY("family", "পিতা-মাতা ও পরিবার", "🏡"),
    HEALTH("health", "সুস্থতা ও রোগমুক্তি", "❤️"),
    RIZQ("rizq", "হালাল রিযিক ও ঋণমুক্তি", "🌾"),
    MARRIAGE("marriage", "উত্তম জীবনসঙ্গী ও দাম্পত্য", "💍"),
    CHILDREN("children", "নেক সন্তান ও বংশধর", "👶"),
    PARENTS("parents", "পিতা-মাতার মাগফিরাত", "🤲"),
    DIFFICULTIES("difficulties", "বিপদ ও দুশ্চিন্তা মুক্তি", "🛡️"),
    AKHIRAH("akhirah", "জাহান্নাম থেকে মুক্তি ও জান্নাত", "✨"),
    LAYLATUL_QADR("laylatul_qadr", "লাইলাতুল কদরের বিশেষ দো'আ", "🌟"),
    PERSONAL("personal", "ব্যক্তিগত দো'আ সম্ভার", "📝");

    companion object {
        fun fromId(id: String?): RamadanDuaCategory =
            entries.find { it.id.equals(id, ignoreCase = true) } ?: SUHOOR
    }
}

/**
 * Authentic Ramadan Dua Model
 */
data class RamadanDuaItem(
    val id: String,
    val titleBn: String,
    val category: RamadanDuaCategory,
    val occasionBn: String,
    val arabicText: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val virtuesBn: String,
    val referenceBn: String,
    val sourceType: RamadanSourceType = RamadanSourceType.SAHIH_HADITH,
    val isUserSaved: Boolean = false
)

/**
 * Daily Ramadan Reflection Prompt (1 to 30)
 */
data class RamadanDailyReflection(
    val dayNumber: Int,
    val themeBn: String,
    val questionBn: String,
    val arabicAyahOrHadith: String,
    val translationBn: String,
    val referenceBn: String,
    val practicalAmolBn: String
)

/**
 * Laylatul Qadr Night Plan
 */
data class LaylatulQadrNightPlan(
    val nightNumber: Int,
    val titleBn: String,
    val hijriDateBn: String,
    val isOddNight: Boolean = true,
    val recommendedActions: List<String>,
    val specialDuaArabic: String,
    val specialDuaMeaningBn: String,
    val hadithReferenceBn: String
)

/**
 * 2-Minute Bite-Sized Ramadan Learning Lesson
 */
data class RamadanKnowledgeLesson(
    val id: String,
    val titleBn: String,
    val summaryBn: String,
    val coreAyahOrHadithArabic: String,
    val translationBn: String,
    val referenceBn: String,
    val practicalTakeawayBn: String,
    val scholarlyNoteBn: String
)

/**
 * Ramadan Verification Layer Item (Message Authenticity Checker)
 */
data class RamadanVerificationItem(
    val id: String,
    val viralClaimBn: String,
    val verdictLabelBn: String, // সহীহ, জাল / ভিত্তিহীন, দুর্বল, শর্তসাপেক্ষে সহীহ
    val verdictColorHex: Long,
    val authenticitySummaryBn: String,
    val hadithOrQuranSource: String,
    val scholarlyExplanationBn: String
)

/**
 * Charity Suggestion by Budget
 */
data class CharityIdeaItem(
    val titleBn: String,
    val tierBn: String, // ক্ষুদ্র (Small), মাঝারি (Medium), বৃহৎ (Large)
    val descriptionBn: String,
    val estimatedBudgetBn: String,
    val referenceBn: String
)

/**
 * Fasting Fiqh Topic Model
 */
data class FastingFiqhItem(
    val id: String,
    val titleBn: String,
    val summaryBn: String,
    val rulingsBn: String,
    val scholarlyDifferenceBn: String,
    val referencesBn: String
)
