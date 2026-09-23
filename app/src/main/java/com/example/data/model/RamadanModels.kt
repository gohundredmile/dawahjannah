package com.example.data.model

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Ramadan Intelligence Core Phases
 */
enum class RamadanPhase(val titleBn: String, val subtitleBn: String, val iconEmoji: String) {
    BEFORE_RAMADAN("রমাদানের পূর্বে", "প্রস্তুতি ও সংকল্প পর্ব", "🌙"),
    DURING_RAMADAN("রমাদানের দিনগুলোতে", "সিয়াম, কুরআন ও বরকতময় আমল", "✨"),
    AFTER_RAMADAN("রমাদানের পরে", "কাযা, শাওয়াল ও নূর ধরে রাখা", "🕊️")
}

/**
 * Sub-modules for Before Ramadan
 */
enum class BeforeRamadanSection(val titleBn: String, val iconEmoji: String) {
    CHECKLIST("প্রস্তুতি চেকলিস্ট", "📋"),
    QURAN_TARGET("কুরআন টার্গেট", "📖"),
    FASTING_PREP("সিয়াম প্রস্তুতি ও ফিকহ", "🌿"),
    CHARITY_PLAN("দান ও সদাকাহ পরিকল্পনা", "💰")
}

/**
 * Sub-modules for During Ramadan
 */
enum class DuringRamadanSection(val titleBn: String, val iconEmoji: String) {
    FASTING_TRACKER("সিয়াম ট্র্যাকার (৩০ দিন)", "🌙"),
    QURAN_KHATM("কুরআন খতম ট্র্যাকার", "📖"),
    TARAWEEH("তারাবীহ ট্র্যাকার", "🕌"),
    SUHOOR_IFTAR("সাহরি ও ইফতার আদব", "🥣"),
    DAILY_DUAS("রমাদানের সকল মাসনুন দো'আ", "🤲"),
    CHARITY_TRACKER("সদাকাহ ট্র্যাকার", "💚"),
    LAYLATUL_QADR("লাইলাতুল কদর প্ল্যানার", "🌟")
}

/**
 * Sub-modules for After Ramadan
 */
enum class AfterRamadanSection(val titleBn: String, val iconEmoji: String) {
    MISSED_FASTS("কাযা রোযা রিকভারি", "⏳"),
    SHAWWAL_FASTS("শাওয়ালের ৬ রোযা", "✨"),
    HABIT_CONTINUATION("অভ্যাস ধারাবাহিকতা", "🌱")
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
    val isDefaultChecked: Boolean = false
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

/**
 * Ramadan Dua Model
 */
data class RamadanDuaItem(
    val id: String,
    val titleBn: String,
    val categoryBn: String,
    val occasionBn: String,
    val arabicText: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val virtuesBn: String,
    val referenceBn: String,
    val sourceType: RamadanSourceType
)

/**
 * Daily Ramadan Schedule & Spiritual Reflection
 */
data class RamadanDailyReflection(
    val dayNumber: Int,
    val themeBn: String,
    val arabicAyahOrHadith: String,
    val translationBn: String,
    val referenceBn: String,
    val practicalAmolBn: String
)

/**
 * Laylatul Qadr Night Plan Model
 */
data class LaylatulQadrNightPlan(
    val nightNumber: Int, // 21, 23, 25, 27, 29
    val titleBn: String,
    val hijriDateBn: String,
    val isOddNight: Boolean = true,
    val recommendedActions: List<String>,
    val specialDuaArabic: String,
    val specialDuaMeaningBn: String,
    val hadithReferenceBn: String
)
