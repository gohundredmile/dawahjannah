package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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
        "রমضان", "شوال", "ذو القعدة", "ذو الحجة"
    )

    val hijriWeekdaysEn = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    val hijriWeekdaysAr = listOf("الأحد", "الإثنين", "الثلاثاء", "الأربعاء", "الخميس", "الجمعة", "السبت")

    fun getGregorianMonth(
        targetMonthIndex: Int,
        targetYear: Int,
        todayCal: Calendar = Calendar.getInstance()
    ): GregorianMonthDetail {
        val cal = Calendar.getInstance().apply {
            set(Calendar.YEAR, targetYear)
            set(Calendar.MONTH, targetMonthIndex)
            set(Calendar.DAY_OF_MONTH, 1)
        }

        val firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) // 1=Sunday, 2=Monday, 3=Tuesday...
        val maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)
        val todayMonth = todayCal.get(Calendar.MONTH)
        val todayYear = todayCal.get(Calendar.YEAR)

        val startCol = firstDayOfWeek - 1 // 0 for Sun, 1 for Mon, 2 for Tue...

        val list = mutableListOf<GregorianDayItem?>()
        for (i in 0 until startCol) {
            list.add(null)
        }

        for (day in 1..maxDays) {
            val col = (startCol + day - 1) % 7
            val isToday = (targetMonthIndex == todayMonth && targetYear == todayYear && day == todayDay)
            list.add(
                GregorianDayItem(
                    dayNumber = day,
                    isToday = isToday,
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

    private fun getBengaliMonthStartGregorian(monthIndex: Int, bengaliYear: Int): Calendar {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 12)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        when (monthIndex) {
            0 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.APRIL); cal.set(Calendar.DAY_OF_MONTH, 14) }
            1 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.MAY); cal.set(Calendar.DAY_OF_MONTH, 15) }
            2 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.JUNE); cal.set(Calendar.DAY_OF_MONTH, 15) }
            3 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.JULY); cal.set(Calendar.DAY_OF_MONTH, 16) }
            4 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.AUGUST); cal.set(Calendar.DAY_OF_MONTH, 16) }
            5 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.SEPTEMBER); cal.set(Calendar.DAY_OF_MONTH, 16) }
            6 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.OCTOBER); cal.set(Calendar.DAY_OF_MONTH, 17) }
            7 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.NOVEMBER); cal.set(Calendar.DAY_OF_MONTH, 16) }
            8 -> { cal.set(Calendar.YEAR, bengaliYear + 593); cal.set(Calendar.MONTH, Calendar.DECEMBER); cal.set(Calendar.DAY_OF_MONTH, 16) }
            9 -> { cal.set(Calendar.YEAR, bengaliYear + 594); cal.set(Calendar.MONTH, Calendar.JANUARY); cal.set(Calendar.DAY_OF_MONTH, 15) }
            10 -> { cal.set(Calendar.YEAR, bengaliYear + 594); cal.set(Calendar.MONTH, Calendar.FEBRUARY); cal.set(Calendar.DAY_OF_MONTH, 14) }
            else -> { cal.set(Calendar.YEAR, bengaliYear + 594); cal.set(Calendar.MONTH, Calendar.MARCH); cal.set(Calendar.DAY_OF_MONTH, 15) }
        }
        return cal
    }

    fun getBengaliMonth(
        targetMonthIndex: Int,
        targetYear: Int,
        todayCal: Calendar = Calendar.getInstance()
    ): BengaliMonthDetail {
        val todayBengali = CalendarHelper.getBengaliDateDetail(todayCal)

        val isLeapYear = (targetYear % 4 == 0 && targetYear % 100 != 0) || (targetYear % 400 == 0)
        val maxDays = when (targetMonthIndex) {
            in 0..5 -> 31
            in 6..9 -> 30
            10 -> if (isLeapYear) 30 else 29
            else -> 30
        }

        val startGregCal = getBengaliMonthStartGregorian(targetMonthIndex, targetYear)
        val startCol = startGregCal.get(Calendar.DAY_OF_WEEK) % 7 // Saturday is 0 (7 % 7)

        val list = mutableListOf<BengaliDayItem?>()
        for (i in 0 until startCol) {
            list.add(null)
        }

        for (day in 1..maxDays) {
            val col = (startCol + day - 1) % 7
            val dayCal = (startGregCal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, day - 1)
            }
            val gregDay = dayCal.get(Calendar.DAY_OF_MONTH)
            val isToday = (targetMonthIndex == todayBengali.monthIndex &&
                           targetYear == todayBengali.year &&
                           day == todayBengali.day)

            list.add(
                BengaliDayItem(
                    dayNumberBn = CalendarHelper.toBanglaNumber(day),
                    gregorianDayNumber = gregDay,
                    isToday = isToday,
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
            currentDayBn = todayBengali.day
        )
    }

    private fun getHijriMonthStartGregorian(
        targetMonthIndex: Int,
        targetYear: Int,
        todayCal: Calendar = Calendar.getInstance()
    ): Calendar {
        val todayHijri = CalendarHelper.getHijriDateDetail(todayCal)
        val monthDiff = (targetYear - todayHijri.year) * 12 + (targetMonthIndex - todayHijri.monthIndex)
        val approxDays = Math.round(monthDiff * 29.5305888) - (todayHijri.day - 1)
        val testCal = (todayCal.clone() as Calendar).apply {
            add(Calendar.DAY_OF_MONTH, approxDays.toInt())
        }

        for (offset in -3..3) {
            val candidate = (testCal.clone() as Calendar).apply { add(Calendar.DAY_OF_MONTH, offset) }
            val h = CalendarHelper.getHijriDateDetail(candidate)
            if (h.day == 1 && h.monthIndex == targetMonthIndex) {
                return candidate
            }
        }
        return testCal
    }

    fun getHijriMonth(
        targetMonthIndex: Int,
        targetYear: Int,
        todayCal: Calendar = Calendar.getInstance()
    ): HijriMonthDetail {
        val todayHijri = CalendarHelper.getHijriDateDetail(todayCal)

        val startGregCal = getHijriMonthStartGregorian(targetMonthIndex, targetYear, todayCal)
        // Sunday is 0 (Calendar.SUNDAY = 1)
        val startCol = (startGregCal.get(Calendar.DAY_OF_WEEK) - 1).let { if (it < 0) 6 else it }

        val list = mutableListOf<HijriDayItem?>()
        for (i in 0 until startCol) {
            list.add(null)
        }

        // Hijri lunar months generally have 29 or 30 days
        val maxDays = if (targetMonthIndex % 2 == 0) 30 else 29

        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        fun toArabicNumerals(n: Int): String {
            return n.toString().map { if (it in '0'..'9') arabicDigits[it - '0'] else it }.joinToString("")
        }

        val monthFmt = SimpleDateFormat("MMM", Locale.ENGLISH)

        for (day in 1..maxDays) {
            val col = (startCol + day - 1) % 7
            val dayCal = (startGregCal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_MONTH, day - 1)
            }
            val gregDay = dayCal.get(Calendar.DAY_OF_MONTH)
            val gregMonthName = monthFmt.format(dayCal.time)
            val gregSub = "$gregDay $gregMonthName"

            val isToday = (targetMonthIndex == todayHijri.monthIndex &&
                           targetYear == todayHijri.year &&
                           day == todayHijri.day)

            list.add(
                HijriDayItem(
                    hijriDayEng = day,
                    hijriDayArabic = toArabicNumerals(day),
                    hijriDayBn = CalendarHelper.toBanglaNumber(day),
                    gregorianSubDate = gregSub,
                    isToday = isToday,
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
            8 -> Pair("রমাদান (Ramadan)", "The Blessed Month of Fasting, Laylatul Qadr & Quran")
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
            currentDayHijri = todayHijri.day
        )
    }
}
