package com.example.data.model

import java.util.Calendar

/**
 * Fasting Type Classification with Islamic Jurisprudence & Etiquette
 */
enum class FastingCategory(
    val titleBn: String,
    val badgeColorHex: Long,
    val isFastingDay: Boolean
) {
    OBLIGATORY("ফরজ রোযা", 0xFF059669, true),
    SUNNAH_HIGH("গুরুত্বপূর্ণ সুন্নাত", 0xFF0284C7, true),
    SUNNAH_RECOMMENDED("মুস্তাহাব / নফল", 0xFF10B981, true),
    AYYAM_AL_BEED("আইয়ামে বীজ (পূর্ণিমা)", 0xFFF59E0B, true),
    FORBIDDEN("রোযা রাখা সম্পূর্ণ নিষিদ্ধ (হারাম)", 0xFFDC2626, false),
    DISLIKED_SINGLE("মাকরূহ তানযীহী (একক রোযা)", 0xFFEA580C, false),
    NONE_REGULAR("সাধারণ দিন (নফল জায়েয)", 0xFF6B7280, false)
}

/**
 * Historical Certainty & Authenticity Tag for Islamic Events
 */
enum class HistoricalCertainty(
    val labelBn: String,
    val badgeColorHex: Long
) {
    UNANIMOUS_CONSENSUS("অকাট্য ও সর্বসম্মত", 0xFF059669),
    AUTHENTIC_HADITH("সহীহ হাদীস দ্বারা প্রমাণিত", 0xFF0284C7),
    HISTORICAL_ESTABLISHED("ঐতিহাসিকভাবে সুপ্রতিষ্ঠিত", 0xFF6366F1),
    SCHOLARLY_DISCUSSION("প্রসিদ্ধ মতামত ও আলেমদের গবেষণা", 0xFFD97706)
}

/**
 * Authentic Historical Event in Islamic History
 */
data class IslamicHistoricalEvent(
    val id: String,
    val titleBn: String,
    val dateSummaryBn: String,
    val yearDescriptionBn: String,
    val certainty: HistoricalCertainty,
    val descriptionBn: String,
    val primarySourceBn: String,
    val scholarlyNoteBn: String? = null
)

/**
 * Detailed Lunar and Moon Phase Data for a given Hijri day
 */
data class LunarPhaseInfo(
    val lunarDay: Int,
    val phaseNameBn: String,
    val phaseNameEn: String,
    val phaseNameAr: String,
    val illuminationPercent: Int,
    val moonAgeDays: Double,
    val iconEmoji: String,
    val isPeakAyyamAlBeed: Boolean,
    val shariahSignificanceBn: String,
    val moonSightingDuaAr: String = "اللَّهُمَّ أَهِلَّهُ عَلَيْنَا بِالْيُمْنِ وَالإِيمَانِ وَالسَّلاَمَةِ وَالإِسْلاَمِ، رَبِّي وَرَبُّكَ اللَّهُ",
    val moonSightingDuaBn: String = "আল্লাহুম্মা আহিল্লাহু 'আলাইনা বিল-য়ুমনি ওয়াল-ঈমানি ওয়াস-সালামাতি ওয়াল-ইসলাম, রাব্বী ওয়া রাব্বুকাল্লাহ।",
    val moonSightingDuaMeaningBn: String = "হে আল্লাহ! বরকত, ঈমান, নিরাপত্তা ও শান্তির সাথে আমাদের ওপর এই নতুন চাঁদের উদয় করুন। (হে চাঁদ!) আমার প্রতিপালক ও তোমার প্রতিপালক আল্লাহ।"
)

/**
 * Recommended Islamic Worship / Sunnah for a particular day
 */
data class IslamicWorshipAction(
    val titleBn: String,
    val categoryBn: String,
    val instructionBn: String,
    val referenceBn: String,
    val virtueBn: String
)

/**
 * Profile of an Islamic Month (1 of 12)
 */
data class IslamicMonthProfile(
    val monthIndex: Int, // 0..11
    val nameEn: String,
    val nameAr: String,
    val nameBn: String,
    val isSacredMonth: Boolean, // Ashhurul Hurum (Surah At-Tawbah 9:36)
    val meaningAndEtymologyBn: String,
    val keySpiritualThemeBn: String,
    val sacredMonthVerseBn: String?,
    val specialRulingsBn: List<String>
)

/**
 * Complete Context for a specific Islamic Date
 */
data class IslamicDayContext(
    val hijriDay: Int,
    val hijriMonthIndex: Int,
    val hijriMonthNameBn: String,
    val hijriMonthNameAr: String,
    val hijriMonthNameEn: String,
    val hijriYear: Int,
    val gregorianDay: Int,
    val gregorianMonthNameBn: String,
    val gregorianYear: Int,
    val gregorianWeekdayBn: String,
    val bengaliDayBn: String,
    val bengaliMonthBn: String,
    val bengaliYearBn: String,
    val bengaliSeasonBn: String,
    val isToday: Boolean,
    val fastingStatus: FastingCategory,
    val fastingRulingDetailsBn: String,
    val fastingReferenceBn: String,
    val lunarInfo: LunarPhaseInfo,
    val historicalEvents: List<IslamicHistoricalEvent>,
    val recommendedWorship: List<IslamicWorshipAction>,
    val monthProfile: IslamicMonthProfile
)
