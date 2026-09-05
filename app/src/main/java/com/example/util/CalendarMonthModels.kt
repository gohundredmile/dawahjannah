package com.example.util

import java.util.Calendar

enum class CalendarViewType {
    NONE,
    GREGORIAN,
    BENGALI,
    HIJRI
}

data class GregorianDayItem(
    val dayNumber: Int,
    val isToday: Boolean,
    val isCurrentMonth: Boolean = true,
    val colIndex: Int // 0..6
)

data class BengaliDayItem(
    val dayNumberBn: String,
    val gregorianDayNumber: Int,
    val isToday: Boolean,
    val isCurrentMonth: Boolean = true,
    val colIndex: Int // 0..6
)

data class HijriDayItem(
    val hijriDayEng: Int,
    val hijriDayArabic: String,
    val hijriDayBn: String,
    val gregorianSubDate: String,
    val isToday: Boolean,
    val isCurrentMonth: Boolean = true,
    val colIndex: Int // 0..6
)

data class GregorianMonthDetail(
    val monthIndex: Int, // 0..11
    val monthName: String, // e.g. "September"
    val year: Int, // 2026
    val monthCode: String, // "Sep"
    val monthNumberLabel: String, // "MONTH 9 OF 12 • STANDARD SOLAR GREGORIAN"
    val seasonTitle: String, // "Autumn (September)"
    val seasonDescription: String, // "Falling leaves, pleasant transitions & autumn gold"
    val days: List<GregorianDayItem?>, // nulls for padding
    val currentDay: Int
)

data class BengaliMonthDetail(
    val monthIndex: Int, // 0..11
    val monthNameBn: String, // "ভাদ্র"
    val yearBn: String, // "১৪৩৩"
    val monthNumberLabel: String, // "মাস ৫/১২ • শরৎকাল (শরৎ)"
    val seasonTitle: String, // "শরৎকাল (শরৎ)"
    val seasonDescription: String, // "নীল আকাশে সাদা মেঘের ভেলা ও শিউলি ফুলের গন্ধ"
    val days: List<BengaliDayItem?>, // nulls for padding
    val currentDayBn: Int
)

data class HijriMonthDetail(
    val monthIndex: Int, // 0..11
    val monthNameEn: String, // "Rabi' al-Awwal"
    val monthNameAr: String, // "ربيع الأول"
    val yearBn: String, // "১৪৪৮"
    val monthNumberLabel: String, // "MONTH 3 OF 12 • RABI' AL-AWWAL"
    val eventTitle: String, // "ربيع الأول (Rabi' al-Awwal)"
    val eventDescription: String, // "Mawlid al-Nabi (Birth of Prophet Muhammad PBUH)"
    val days: List<HijriDayItem?>, // nulls for padding
    val currentDayHijri: Int
)

object CalendarMonthProvider {

    val gregorianMonthShortNames = listOf(
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    )

    val gregorianMonthFullNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val gregorianWeekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

    val bengaliMonthNames = listOf(
        "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন",
        "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    )

    val bengaliWeekdays = listOf("শনি", "রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র")

    val hijriMonthNames = listOf(
        "Muharram", "Safar", "Rabi' al-Awwal", "Rabi' al-Thani",
        "Jumada al-Awwal", "Jumada al-Thani", "Rajab", "Sha'ban",
        "Ramadan", "Shawwal", "Dhu al-Qa'dah", "Dhu al-Hijjah"
    )

    val hijriMonthNamesArabic = listOf(
        "محرم", "صفر", "ربيع الأول", "ربيع الثاني",
        "جمادى الأولى", "جمادى الآخرة", "رجب", "شعبان",
        "رمضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    val hijriWeekdaysEn = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val hijriWeekdaysAr = listOf("الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")

    fun getGregorianMonth(
        targetMonthIndex: Int = 8, // September (0-based)
        targetYear: Int = 2026,
        todayDay: Int = 4
    ): GregorianMonthDetail {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, targetYear)
            set(Calendar.MONTH, targetMonthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1=Sunday, 2=Monday, 3=Tuesday...
        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Starting column index (0 for Sun, 1 for Mon, 2 for Tue...)
        val startCol = firstDayOfWeek - 1

        val list = mutableListOf<GregorianDayItem?>()
        for (i in 0 until startCol) {
            list.add(null)
        }

        for (day in 1..maxDays) {
            val col = (startCol + day - 1) % 7
            list.add(
                GregorianDayItem(
                    dayNumber = day,
                    isToday = (targetMonthIndex == 8 && targetYear == 2026 && day == todayDay),
                    colIndex = col
                )
            )
        }

        val season = when (targetMonthIndex) {
            11, 0, 1 -> Pair("Winter (${gregorianMonthFullNames[targetMonthIndex]})", "Cool breezes, clear skies & winter serenity")
            2, 3, 4 -> Pair("Spring (${gregorianMonthFullNames[targetMonthIndex]})", "Fresh blooms, gentle winds & pleasant climate")
            5, 6, 7 -> Pair("Summer (${gregorianMonthFullNames[targetMonthIndex]})", "Warm sunshine, long golden days & vibrant energy")
            else -> Pair("Autumn (${gregorianMonthFullNames[targetMonthIndex]})", "Falling leaves, pleasant transitions & autumn gold")
        }

        return GregorianMonthDetail(
            monthIndex = targetMonthIndex,
            monthName = gregorianMonthFullNames[targetMonthIndex],
            year = targetYear,
            monthCode = gregorianMonthShortNames[targetMonthIndex],
            monthNumberLabel = "MONTH ${targetMonthIndex + 1} OF 12 • STANDARD SOLAR GREGORIAN",
            seasonTitle = season.first,
            seasonDescription = season.second,
            days = list,
            currentDay = todayDay
        )
    }

    fun getBengaliMonth(
        targetMonthIndex: Int = 4, // ভাদ্র (5th month, index 4)
        targetYear: Int = 1433,
        todayDay: Int = 20
    ): BengaliMonthDetail {
        // Bengali calendar: First 6 months (0..5) have 31 days.
        // Kartik to Magh (6..9) have 30 days. Phalgun (10) has 29/30. Chaitra (11) has 30.
        val maxDays = if (targetMonthIndex in 0..5) 31 else if (targetMonthIndex in 6..9) 30 else 30

        // Weekday calculation for Bengali months:
        // Weekdays in order: শনি(0), রবি(1), সোম(2), মঙ্গল(3), বুধ(4), বৃহঃ(5), শুক্র(6)
        // For 1433 Bhadra (Index 4), 1st Bhadra = August 16, 2026 = Sunday (রবি) => Col 1!
        val startCol = when (targetMonthIndex) {
            0 -> 2 // Boishakh starts Tuesday (সোম/মঙ্গল)
            1 -> 5 // Joistho
            2 -> 1 // Ashar
            3 -> 4 // Srabon
            4 -> 1 // Bhadra: 1st is Sunday (রবি -> Col 1)
            5 -> 4 // Ashwin
            6 -> 0 // Kartik
            7 -> 2 // Agrahayan
            8 -> 4 // Poush
            9 -> 6 // Magh
            10 -> 1 // Falgun
            else -> 3 // Chaitra
        }

        val list = mutableListOf<BengaliDayItem?>()
        for (i in 0 until startCol) {
            list.add(null)
        }

        // For Bhadra 1433: 1st Bhadra = 16 Aug. So day 1 -> 16. Day 17 -> 1 Sep. Day 20 -> 4 Sep.
        for (day in 1..maxDays) {
            val col = (startCol + day - 1) % 7
            val gregDay = when {
                targetMonthIndex == 4 && day <= 16 -> 15 + day // 16 Aug .. 31 Aug
                targetMonthIndex == 4 && day > 16 -> day - 16   // 1 Sep .. 15 Sep
                else -> (day % 30) + 1
            }
            list.add(
                BengaliDayItem(
                    dayNumberBn = CalendarHelper.toBanglaNumber(day),
                    gregorianDayNumber = gregDay,
                    isToday = (targetMonthIndex == 4 && day == todayDay),
                    colIndex = col
                )
            )
        }

        val season = when (targetMonthIndex / 2) {
            0 -> Pair("গ্রীষ্মকাল (গ্রীষ্ম)", "তপ্ত রোদ, আম-কাঁঠালের মিষ্টি সুবাস ও বৈশাখী হাওয়া")
            1 -> Pair("বর্ষাকাল (বর্ষা)", "রিমঝিম বৃষ্টির ধারা, কদম ফুল ও সজীব শ্যামল প্রকৃতি")
            2 -> Pair("শরৎকাল (শরৎ)", "নীল আকাশে সাদা মেঘের ভেলা ও শিউলি ফুলের গন্ধ")
            3 -> Pair("হেমন্তকাল (হেমন্ত)", "সোনালী ধানের শিষ, নবান্ন উৎসব ও হালকা কুয়াশা")
            4 -> Pair("শীতকাল (শীত)", "মিঠে রোদের সকাল, খেজুরের রস ও পিঠাপুলির আমেজ")
            else -> Pair("বসন্তকাল (বসন্ত)", "কোকিলের কুহু ডাক, পলাশ-শিমুল ও দখিনা বাতাস")
        }

        val banglaMonthNumberLabel = "মাস ${CalendarHelper.toBanglaNumber(targetMonthIndex + 1)}/১২ • ${season.first}"

        return BengaliMonthDetail(
            monthIndex = targetMonthIndex,
            monthNameBn = bengaliMonthNames[targetMonthIndex],
            yearBn = CalendarHelper.toBanglaNumber(targetYear),
            monthNumberLabel = banglaMonthNumberLabel,
            seasonTitle = season.first,
            seasonDescription = season.second,
            days = list,
            currentDayBn = todayDay
        )
    }

    fun getHijriMonth(
        targetMonthIndex: Int = 2, // Rabi' al-Awwal (3rd month, index 2)
        targetYear: Int = 1448,
        todayDay: Int = 22
    ): HijriMonthDetail {
        val maxDays = 29 // or 30

        // Weekday order: Sun(0), Mon(1), Tue(2), Wed(3), Thu(4), Fri(5), Sat(6)
        // For 1448 Rabi' al-Awwal, 1st Rabi' al-Awwal was Friday August 14, 2026 => Col 5
        // 15 Rabi' al-Awwal = 28 Aug (Friday, col 5)
        // 22 Rabi' al-Awwal = 4 Sep (Friday, col 5)
        val startCol = 5 // Friday

        val list = mutableListOf<HijriDayItem?>()
        for (i in 0 until startCol) {
            list.add(null)
        }

        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        fun toArabicNumerals(n: Int): String {
            return n.toString().map { if (it in '0'..'9') arabicDigits[it - '0'] else it }.joinToString("")
        }

        for (day in 1..maxDays) {
            val col = (startCol + day - 1) % 7
            val gregSub = when {
                targetMonthIndex == 2 && day <= 18 -> "${13 + day} Aug"
                targetMonthIndex == 2 && day > 18 -> "${day - 18} Sep"
                else -> "${(day % 28) + 1} Date"
            }
            list.add(
                HijriDayItem(
                    hijriDayEng = day,
                    hijriDayArabic = toArabicNumerals(day),
                    hijriDayBn = CalendarHelper.toBanglaNumber(day),
                    gregorianSubDate = gregSub,
                    isToday = (targetMonthIndex == 2 && day == todayDay),
                    colIndex = col
                )
            )
        }

        val events = when (targetMonthIndex) {
            0 -> Pair("محرم (Muharram)", "Ashura & Month of Allah (Al-Muharram)")
            1 -> Pair("صفر (Safar)", "Good deeds, patience & reliance on Allah")
            2 -> Pair("ربيع الأول (Rabi' al-Awwal)", "Mawlid al-Nabi (Birth of Prophet Muhammad PBUH)")
            3 -> Pair("ربيع الثاني (Rabi' al-Thani)", "Quranic contemplation, steadfastness & charity")
            4 -> Pair("جمادى الأولى (Jumada al-Awwal)", "Devotion, family bonds & constant dhikr")
            5 -> Pair("جمادى الآخرة (Jumada al-Thani)", "Preparing soul and mind for sacred months")
            6 -> Pair("رجب (Rajab)", "Al-Isra wal-Mi'raj & Sacred Month of forgiveness")
            7 -> Pair("شعبان (Sha'ban)", "Shab-e-Barat & Month of fasting preparation")
            8 -> Pair("رمضان (Ramadan)", "The Blessed Month of Fasting, Laylatul Qadr & Quran")
            9 -> Pair("شوال (Shawwal)", "Eid al-Fitr & Sunnah Fasts of Shawwal")
            10 -> Pair("ذو القعدة (Dhu al-Qa'dah)", "Sacred Month of peace, unity & contemplation")
            else -> Pair("ذو الحجة (Dhu al-Hijjah)", "10 Blessed Days of Dhul Hijjah, Day of Arafah & Eid al-Adha")
        }

        return HijriMonthDetail(
            monthIndex = targetMonthIndex,
            monthNameEn = hijriMonthNames[targetMonthIndex],
            monthNameAr = hijriMonthNamesArabic[targetMonthIndex],
            yearBn = CalendarHelper.toBanglaNumber(targetYear),
            monthNumberLabel = "MONTH ${targetMonthIndex + 1} OF 12 • ${hijriMonthNames[targetMonthIndex].uppercase()}",
            eventTitle = events.first,
            eventDescription = events.second,
            days = list,
            currentDayHijri = todayDay
        )
    }
}
