package com.example.data.model

data class PrayerTimeItem(
    val id: String,
    val nameEn: String,
    val nameBn: String,
    val subtitleEn: String = "",
    val timeFormatted: String, // e.g. "4:24 AM"
    val startTimeFormatted: String = "",
    val endTimeFormatted: String = "",
    val startFormattedBn: String = "",
    val endFormattedBn: String = "",
    val durationBn: String = "",
    val timeMinutesFromMidnight: Int,
    val isPrayer: Boolean = true, // True for 5 daily prayers, false for sunrise/tahajjud info
    val isHighlighted: Boolean = false,
    val isNotificationEnabled: Boolean = true,
    val studyGuideSubtitleBn: String = "",
    val rakatsSummaryBn: String = "",
    val rakatsDetailBn: String = "",
    val benefitsBn: String = ""
)

data class ForbiddenTimeInfo(
    val sunriseStart24: String = "05:41",
    val sunriseEnd24: String = "05:56",
    val zawalStart24: String = "11:47",
    val zawalEnd24: String = "11:57",
    val sunsetStart24: String = "17:59",
    val sunsetEnd24: String = "18:14",
    val sunriseDisplay12: String = "5:41 AM - 5:56 AM",
    val zawalDisplay12: String = "11:47 AM - 11:57 AM",
    val sunsetDisplay12: String = "5:59 PM - 6:14 PM",
    val isCurrentlyForbidden: Boolean = false,
    val activeForbiddenName: String = "",
    val rulesBn: String = "এই তিন সময়গুলোতে যেকোনো ধরণের ফরজ, সুন্নত বা নফল সালাত আদায় করা এবং সিজদাহ করা সম্পূর্ণরূপে নিষিদ্ধ ও মাকরূহে তাহরীমী। তবে ঐ দিনের আসরের নামাজ আসর ওয়াক্ত অতি বিলম্বে হলেও সূর্যাস্তের পূর্বে পড়ে নিতে হবে।",
    val benefitsBn: String = "এই সময়গুলোতে রকু বা সিজদাহ না করে আল্লাহর জিকির, ইস্তিগফার (ক্ষমা প্রার্থনা), দুরুদ শরীফ পাঠ এবং দোয়া করা অত্যন্ত সওয়াবের কাজ ও বৈধ।"
)

data class DuaItem(
    val id: String,
    val categoryId: String,
    val categoryNameBn: String,
    val titleBn: String,
    val arabicText: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val virtuesBn: String,
    val reference: String,
    val isBookmarked: Boolean = false
)

data class RoutineItem(
    val id: String,
    val timeSlotId: String, // e.g. "tahajjud", "before_fajr", "fajr_waqt", "after_fajr", "morning", "morning_to_noon", "dhuhr_waqt", "after_dhuhr", "asr_waqt", "after_asr", "maghrib_waqt", "after_maghrib", "isha_waqt", "after_isha", "before_sleep", "last_part_night"
    val timeSlotTitleBn: String,
    val titleBn: String,
    val subtitleBn: String = "",
    val descriptionBn: String,
    val virtuesRewardBn: String = "",
    val reference: String = "",
    val isTopPriority: Boolean = false,
    val priorityRank: Int = 99, // Order in list
    val arabicText: String = "", // For Quranic verses/duas in routine
    val tagBn: String = "", // e.g. "সর্বশ্রেষ্ঠ আয়াত", "দৈনিক সুরক্ষা", "নফল সালাত", "সমাপনী সালাত", "সুন্নাত"
    val countBadgeBn: String = "", // e.g. "১ বার", "প্রতিটি ৩ বার", "২ থেকে ৮ রাকাত", "৩ রাকাত"
    val extraBadgeBn: String = "", // e.g. "সূরা আল-বাকারা: ২৫৫"
    val pronunciationBn: String = "",
    val meaningBn: String = "",
    val isMinimumRoutine: Boolean = false,
    val isIdealRoutine: Boolean = true,
    val sectionNumberBn: String = "",
    val targetCount: Int = 1
)

data class HabitItem(
    val id: String,
    val titleBn: String,
    val descriptionBn: String,
    val categoryBn: String,
    val iconResName: String = "ic_check"
)

data class AllahNameItem(
    val number: Int,
    val arabicName: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val spiritualReflectionBn: String,
    val fojilotBn: String = "",
    val amolBn: String = ""
)

data class IslamicLifeCardItem(
    val id: String,
    val serialNumberBn: String = "",
    val repetitionOrTimeBn: String = "",
    val titleBn: String = "",
    val subtitleBn: String = "",
    val arabicText: String = "",
    val pronunciationBn: String = "",
    val meaningBn: String = "",
    val fojilotBn: String = "",
    val detailsBn: String = "",
    val referenceBn: String = ""
)

data class IslamicLifeSection(
    val id: String,
    val titleBn: String,
    val subtitleBn: String,
    val noticeTextBn: String = "",
    val noticeHighlightBn: String = "",
    val items: List<IslamicLifeCardItem> = emptyList()
)

data class DuroodItem(
    val id: String,
    val titleBn: String,
    val arabicText: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val backgroundStoryBn: String,
    val virtuesRewardBn: String,
    val reference: String
)

data class HealthDuaItem(
    val id: String,
    val titleBn: String,
    val ailmentCategoryBn: String, // মানসিক শান্তি, ঋণমুক্তি, শারীরিক শিফা, বদনজর ও জাদু
    val arabicText: String,
    val pronunciationBn: String,
    val meaningBn: String,
    val amalMethodBn: String, // আমলের নিয়ম
    val reference: String
)

data class DailyInspiration(
    val quranArabic: String,
    val quranBengali: String,
    val quranEnglish: String,
    val quranSurahAyah: String,

    val hadithBengali: String,
    val hadithEnglish: String,
    val hadithBookSource: String,
    val hadithNarrator: String,

    val wisdomQuoteBn: String,
    val wisdomAuthorBn: String
)

enum class ThemeStyle(
    val displayNameEn: String,
    val displayNameBn: String,
    val descriptionBn: String,
    val slug: String,
    val isDark: Boolean = false
) {
    COSMIC_AURORA(
        "Cosmic Aurora",
        "কসমিক অরোরা",
        "গাঢ় রাতের আকাশ ও নিয়ন আভা",
        "#borealis",
        isDark = true
    ),
    AURORA_AUSTRALIS(
        "Aurora Australis",
        "অরোরা অস্ট্রালিস",
        "গোলাপী ও লালচে আভা",
        "#australis",
        isDark = false
    ),
    GLACIAL_AURORA(
        "Glacial Aurora",
        "গ্লেসিয়াল অরোরা",
        "হিমশীতল নীল ও সায়ান বরফ",
        "#arctic_ice",
        isDark = false
    ),
    SOLAR_DAWN(
        "Solar Dawn",
        "সোলার ডন",
        "সোনালী সূর্যোদয় ও প্রাতঃকালীন আলো",
        "#solar_wind",
        isDark = false
    ),
    ORCHID_DREAM(
        "Orchid Dream",
        "অর্কিড ড্রিম",
        "মার্জিত বেগুনি ও ভায়োলেট",
        "#orchid_iris",
        isDark = false
    ),
    SAGE_WHISPER(
        "Sage Whisper",
        "সেইজ উইস্পার",
        "শান্ত সবুজ ও এমারেল্ড স্নিগ্ধতা",
        "#sage_whisper",
        isDark = false
    ),
    LAVENDER_MIST(
        "Lavender Mist",
        "ল্যাভেন্ডার মিস্ট",
        "স্নিগ্ধ নীলাভ-বেগুনি কুয়াশা",
        "#lavender_mist",
        isDark = false
    ),
    PEACH_BLOSSOM(
        "Peach Blossom",
        "পীচ ব্লসম",
        "কোমল পীচ ও উষ্ণ বসন্ত",
        "#peach_blossom",
        isDark = false
    ),
    OCEAN_BREEZE(
        "Ocean Breeze",
        "ওশান ব্রিজ",
        "সমুদ্রের শীতল হাওয়া ও আকাশী",
        "#ocean_breeze",
        isDark = false
    ),
    CHAMOMILE_TEA(
        "Chamomile Tea",
        "ক্যামোমাইল টি",
        "উষ্ণ সোনালী ও অ্যাম্বার আভা",
        "#chamomile_tea",
        isDark = false
    ),
    FOREST_BATHING(
        "Forest Bathing",
        "ফরেস্ট বাথিং",
        "গভীর সবুজ অরণ্যের সজীবতা",
        "#forest_bathing",
        isDark = false
    ),
    ETHEREAL_SAND(
        "Ethereal Sand",
        "ইথেরিয়াল স্যান্ড",
        "উষ্ণ মরু বালুকা ও মার্বেল",
        "#ethereal_sand",
        isDark = false
    ),
    VELVET_PLUM(
        "Velvet Plum",
        "ভেলভেট প্লাম",
        "অভিজাত প্লাম ও মেজেন্টা",
        "#velvet_plum",
        isDark = false
    ),
    SILVER_BIRCH(
        "Silver Birch",
        "সিলভার বার্চ",
        "ধূসর রূপালী ও শান্ত নান্দনিকতা",
        "#silver_birch",
        isDark = false
    ),
    MINT_MATCHA(
        "Mint Matcha",
        "মিন্ট মাচা",
        "তাজা পুদিনা ও চা-পাতার সজীবতা",
        "#mint_matcha",
        isDark = false
    ),
    // Backward compatibility alias
    EMERALD_JANNAH(
        "Forest Bathing",
        "ফরেস্ট বাথিং",
        "গভীর সবুজ অরণ্যের সজীবতা",
        "#forest_bathing",
        isDark = false
    );

    val fullTitleBn: String
        get() = "$displayNameBn ($descriptionBn)"
}

enum class ThemeMode(val titleBn: String) {
    SYSTEM("সিস্টেম ডিফল্ট"),
    LIGHT("লাইট মোড"),
    DARK("ডার্ক মোড")
}

enum class ScreenEffectMode(
    val titleBn: String,
    val titleEn: String,
    val subtitleBn: String
) {
    NORMAL(
        titleBn = "স্বাভাবিক মোড",
        titleEn = "Normal Mode",
        subtitleBn = "ট্যাপ করলে পূর্বের সাধারণ হোম স্ক্রিন ফিরবে"
    ),
    GLASS(
        titleBn = "গ্লাস ইফেক্ট",
        titleEn = "Glass Effect",
        subtitleBn = "ফ্রস্টেড গ্লাস ও লিকুইড ওয়েভ ইফেক্ট"
    )
}

enum class FontSizeScale(val scale: Float, val titleBn: String) {
    EXTRA_SMALL(0.85f, "খুব ছোট"),
    SMALL(0.92f, "ছোট"),
    NORMAL(1.0f, "স্বাভাবিক"),
    MEDIUM_LARGE(1.12f, "মাঝারি বড়"),
    LARGE(1.24f, "বড়"),
    EXTRA_LARGE(1.38f, "অনেক বড়")
}

data class SalatPlaceInfo(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val category: String, // "division", "district", "international"
    val latitude: Double,
    val longitude: Double,
    val timezoneOffsetHours: Double = 6.0
)

enum class PrayerCalculationMethod(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val fajrAngle: Double,
    val ishaAngle: Double?, // null if interval
    val ishaIntervalMinutes: Int? = null,
    val maghribAngle: Double? = null, // null if sunset (-0.8333)
    val descriptionBn: String
) {
    KARACHI(
        id = "karachi",
        titleBn = "University of Islamic Sciences, Karachi",
        titleEn = "University of Islamic Sciences, Karachi",
        fajrAngle = 18.0,
        ishaAngle = 18.0,
        descriptionBn = "বাংলাদেশ, পাকিস্তান, ভারত ও আফগানিস্তানে সর্বাধিক অনুসৃত মানদণ্ড (ফজর ১৮°, এশা ১৮°)"
    ),
    MUSLIM_WORLD_LEAGUE(
        id = "mwl",
        titleBn = "Muslim World League",
        titleEn = "Muslim World League",
        fajrAngle = 18.0,
        ishaAngle = 17.0,
        descriptionBn = "ইউরোপ, দূরপ্রাচ্য ও আমেরিকার বিভিন্ন অঞ্চলে অনুসৃত (ফজর ১৮°, এশা ১৭°)"
    ),
    EGYPTIAN(
        id = "egyptian",
        titleBn = "Egyptian General Authority of Survey",
        titleEn = "Egyptian General Authority of Survey",
        fajrAngle = 19.5,
        ishaAngle = 17.5,
        descriptionBn = "মিশর, আফ্রিকা, সিরিয়া, ইরাক, লেবানন ও মালয়েশিয়ায় অনুসৃত (ফজর ১৯.৫°, এশা ১৭.৫°)"
    ),
    ISNA(
        id = "isna",
        titleBn = "Islamic Society of North America",
        titleEn = "Islamic Society of North America",
        fajrAngle = 15.0,
        ishaAngle = 15.0,
        descriptionBn = "উত্তর আমেরিকা—যুক্তরাষ্ট্র ও কানাডায় প্রচলিত (ফজর ১৫°, এশা ১৫°)"
    ),
    SHIA_ITHNA_ASHARI(
        id = "shia_qum",
        titleBn = "Shia Ithna Ashari, Leva Research Institute, Qum",
        titleEn = "Shia Ithna Ashari, Leva Research Institute, Qum",
        fajrAngle = 16.0,
        ishaAngle = 14.0,
        maghribAngle = 4.0,
        descriptionBn = "শিয়া ইথনা আশারি, লেভা গবেষণা ইনস্টিটিউট, কোম (ফজর ১৬°, মাগরিব ৪°, এশা ১৪°)"
    ),
    UMM_AL_QURA(
        id = "makkah",
        titleBn = "Umm al-Qura, Makkah",
        titleEn = "Umm al-Qura, Makkah",
        fajrAngle = 18.5,
        ishaAngle = null,
        ishaIntervalMinutes = 90,
        descriptionBn = "মক্কা মুকাররমা ও আরব উপদ্বীপে প্রচলিত (ফজর ১৮.৫°, এশা মাগরিবের ৯০ মিনিট পর)"
    ),
    TEHRAN_GEOPHYSICS(
        id = "tehran",
        titleBn = "Institute of Geophysics, University of Tehran",
        titleEn = "Institute of Geophysics, University of Tehran",
        fajrAngle = 17.7,
        ishaAngle = 14.0,
        maghribAngle = 4.5,
        descriptionBn = "ইনস্টিটিউট অব জিওফিজিক্স, তেহরান বিশ্ববিদ্যালয় (ফজর ১৭.৭°, মাগরিব ৪.৫°, এশা ১৪°)"
    ),
    ISLAMIC_FOUNDATION_BD(
        id = "ifb",
        titleBn = "Islamic Foundation Bangladesh",
        titleEn = "Islamic Foundation Bangladesh",
        fajrAngle = 18.0,
        ishaAngle = 18.0,
        descriptionBn = "ইসলামিক ফাউন্ডেশন বাংলাদেশ সরকারি মানদণ্ড (ফজর ১৮°, এশা ১৮°)"
    ),
    GULF_DUBAI(
        id = "dubai",
        titleBn = "Dubai / Gulf Region (Awqaf)",
        titleEn = "Dubai / Gulf Region (Awqaf)",
        fajrAngle = 18.2,
        ishaAngle = null,
        ishaIntervalMinutes = 90,
        descriptionBn = "দুবাই ও উপসাগরীয় আওকাফ মানদণ্ড (ফজর ১৮.২°, এশা ৯০ মিনিট পর)"
    );

    companion object {
        fun fromId(id: String?): PrayerCalculationMethod {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: KARACHI
        }
    }
}

enum class AsrJuristicMethod(
    val id: String,
    val titleBn: String,
    val titleEn: String,
    val shadowMultiplier: Double,
    val descriptionBn: String
) {
    HANAFI(
        id = "hanafi",
        titleBn = "Hanafi Juristic (হানাফী মাযহাব)",
        titleEn = "Hanafi Juristic",
        shadowMultiplier = 2.0,
        descriptionBn = "আসরের ওয়াক্ত বস্তুর মূল ছায়া ছাড়া দ্বিগুণ (মিসলে সানি) হলে শুরু হয়।"
    ),
    STANDARD(
        id = "standard",
        titleBn = "Standard (শাফেয়ী, মালেকী, হাম্বলী)",
        titleEn = "Standard (Shafi'i, Maliki, Hanbali)",
        shadowMultiplier = 1.0,
        descriptionBn = "আসরের ওয়াক্ত বস্তুর মূল ছায়া ছাড়া সমপরিমাণ (মিসলে আওয়াল) হলে শুরু হয়।"
    );

    companion object {
        fun fromId(id: String?): AsrJuristicMethod {
            return if (id?.equals("standard", ignoreCase = true) == true) STANDARD else HANAFI
        }
    }
}

enum class HighLatitudeRule(val id: String, val titleBn: String, val titleEn: String) {
    ANGLE_BASED("angle_based", "কোণভিত্তিক বিভাজন (Angle-Based / Night Portion)", "Angle-Based / Night Portion"),
    MIDNIGHT("midnight", "অর্ধরাত্রি নীতি (Midnight Rule)", "Midnight Rule"),
    ONE_SEVENTH("one_seventh", "রাতের এক-সপ্তমাংশ (One-Seventh Rule)", "One-Seventh Rule"),
    NONE("none", "কোনো সমন্বয় ছাড়া (Exact Sun Altitude)", "None");

    companion object {
        fun fromId(id: String?): HighLatitudeRule {
            return entries.find { it.id.equals(id, ignoreCase = true) } ?: ANGLE_BASED
        }
    }
}

data class SalatConfiguration(
    val placeNameBn: String = "ঢাকা, বাংলাদেশ",
    val placeNameEn: String = "Dhaka, Bangladesh",
    val latitude: Double = 23.8103,
    val longitude: Double = 90.4125,
    val timezoneOffsetHours: Double = 6.0,
    val isGpsEnabled: Boolean = false,
    val isHanafiAsr: Boolean = true,
    val calculationMethod: PrayerCalculationMethod = PrayerCalculationMethod.KARACHI,
    val asrMethod: AsrJuristicMethod = AsrJuristicMethod.HANAFI,
    val highLatitudeRule: HighLatitudeRule = HighLatitudeRule.ANGLE_BASED,
    val manualOffsetMinutes: Int = 0
)

val PRESET_SALAT_PLACES = listOf(
    // Bangladesh Divisions
    SalatPlaceInfo("dhaka", "ঢাকা", "Dhaka", "division", 23.8103, 90.4125),
    SalatPlaceInfo("chittagong", "চট্টগ্রাম", "Chattogram", "division", 22.3569, 91.7832),
    SalatPlaceInfo("sylhet", "সিলেট", "Sylhet", "division", 24.8949, 91.8687),
    SalatPlaceInfo("rajshahi", "রাজশাহী", "Rajshahi", "division", 24.3745, 88.6042),
    SalatPlaceInfo("khulna", "খুলনা", "Khulna", "division", 22.8456, 89.5403),
    SalatPlaceInfo("barishal", "বরিশাল", "Barishal", "division", 22.7010, 90.3535),
    SalatPlaceInfo("rangpur", "রংপুর", "Rangpur", "division", 25.7439, 89.2752),
    SalatPlaceInfo("mymensingh", "ময়মনসিংহ", "Mymensingh", "division", 24.7471, 90.4203),

    // Bangladesh Districts
    SalatPlaceInfo("cumilla", "কুমিল্লা", "Cumilla", "district", 23.4607, 91.1809),
    SalatPlaceInfo("coxsbazar", "কক্সবাজার", "Cox's Bazar", "district", 21.4272, 92.0058),
    SalatPlaceInfo("bogura", "বগুড়া", "Bogura", "district", 24.8465, 89.3777),
    SalatPlaceInfo("jashore", "যশোর", "Jashore", "district", 23.1664, 89.2081),
    SalatPlaceInfo("kushtia", "কুষ্টিয়া", "Kushtia", "district", 23.9013, 89.1205),
    SalatPlaceInfo("dinajpur", "দিনাজপুর", "Dinajpur", "district", 25.6217, 88.6354),
    SalatPlaceInfo("noakhali", "নোয়াখালী", "Noakhali", "district", 22.8696, 91.0998),
    SalatPlaceInfo("pabna", "পাবনা", "Pabna", "district", 24.0064, 89.2372),
    SalatPlaceInfo("tangail", "টাঙ্গাইল", "Tangail", "district", 24.2513, 89.9167),
    SalatPlaceInfo("gazipur", "গাজীপুর", "Gazipur", "district", 23.9999, 90.4203),
    SalatPlaceInfo("narayanganj", "নারায়ণগঞ্জ", "Narayanganj", "district", 23.6238, 90.5000),
    SalatPlaceInfo("brahmanbaria", "ব্রাহ্মণবাড়িয়া", "Brahmanbaria", "district", 23.9571, 91.1119),
    SalatPlaceInfo("feni", "ফেনী", "Feni", "district", 23.0159, 91.3976),
    SalatPlaceInfo("faridpur", "ফরিদপুর", "Faridpur", "district", 23.6071, 89.8429),

    // Holy & International Cities
    SalatPlaceInfo("makkah", "মক্কা মুকাররমা", "Makkah (KSA)", "international", 21.3891, 39.8579, 3.0),
    SalatPlaceInfo("madinah", "মদিনা মুনাওয়ারা", "Madinah (KSA)", "international", 24.5247, 39.5692, 3.0),
    SalatPlaceInfo("jerusalem", "আল-কুদস (জেরুজালেম)", "Al-Quds / Jerusalem", "international", 31.7683, 35.2137, 2.0),
    SalatPlaceInfo("dubai", "দুবাই", "Dubai (UAE)", "international", 25.2048, 55.2708, 4.0),
    SalatPlaceInfo("doha", "দোহা", "Doha (Qatar)", "international", 25.2854, 51.5310, 3.0),
    SalatPlaceInfo("kualalumpur", "কুয়ালালামপুর", "Kuala Lumpur", "international", 3.1390, 101.6869, 8.0),
    SalatPlaceInfo("london", "লন্ডন", "London (UK)", "international", 51.5074, -0.1278, 1.0),
    SalatPlaceInfo("newyork", "নিউ ইয়র্ক", "New York (USA)", "international", 40.7128, -74.0060, -4.0)
)
