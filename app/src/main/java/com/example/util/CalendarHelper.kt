package com.example.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object CalendarHelper {

    private val banglaDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')

    fun toBanglaNumber(number: Int): String {
        return number.toString().map { ch ->
            if (ch in '0'..'9') banglaDigits[ch - '0'] else ch
        }.joinToString("")
    }

    fun toBanglaNumber(str: String): String {
        return str.map { ch ->
            if (ch in '0'..'9') banglaDigits[ch - '0'] else ch
        }.joinToString("")
    }

    data class TripleCalendarInfo(
        val englishDateFormatted: String, // e.g., "Friday, 04 September 2026"
        val englishDay: String,
        val bengaliDateFormatted: String, // e.g., "২০ ভাদ্র, ১৪৩৩ বঙ্গাব্দ (শরৎকাল)"
        val bengaliMonth: String,
        val bengaliSeason: String,
        val hijriDateFormatted: String,   // e.g., "২১ সফর, ১৪৪৮ হিজরি"
        val hijriMonth: String
    )

    private val banglaMonths = arrayOf(
        "বৈশাখ", "জ্যৈষ্ঠ", "আষাঢ়", "শ্রাবণ", "ভাদ্র", "আশ্বিন",
        "কার্তিক", "অগ্রহায়ণ", "পৌষ", "মাঘ", "ফাল্গুন", "চৈত্র"
    )

    private val banglaSeasons = arrayOf(
        "গ্রীষ্মকাল", "বর্ষাকাল", "শরৎকাল", "হেমন্তকাল", "শীতকাল", "বসন্তকাল"
    )

    private val hijriMonths = arrayOf(
        "মুহররম", "সফর", "রবিউল আউয়াল", "রবিউস সানি",
        "জমাদিউল আউয়াল", "জমাদিউস সানি", "রজব", "শাবান",
        "রমাদান", "শাওয়াল", "জিলকদ", "জিলহজ্জ"
    )

    private val englishDaysBn = mapOf(
        Calendar.SUNDAY to "রবিবার",
        Calendar.MONDAY to "সোমবার",
        Calendar.TUESDAY to "মঙ্গলবার",
        Calendar.WEDNESDAY to "বুধবার",
        Calendar.THURSDAY to "বৃহস্পতিবার",
        Calendar.FRIDAY to "শুক্রবার (জুমাবার)",
        Calendar.SATURDAY to "শনিবার"
    )

    fun getTripleCalendar(date: Date = Date()): TripleCalendarInfo {
        val cal = Calendar.getInstance().apply { time = date }
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val dayNameBn = englishDaysBn[dayOfWeek] ?: ""

        val engDateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH)
        val englishDateFormatted = engDateFormat.format(date)

        // Bengali Date approximation (Bangladesh revised calendar):
        // Boishakh starts on April 14
        val bengaliInfo = calculateBengaliDate(cal)

        // Hijri Date approximation (Kuwaiti algorithm / Um Al Qura base)
        val hijriInfo = calculateHijriDate(cal)

        return TripleCalendarInfo(
            englishDateFormatted = englishDateFormatted,
            englishDay = dayNameBn,
            bengaliDateFormatted = "${toBanglaNumber(bengaliInfo.first)} ${bengaliInfo.second}, ${toBanglaNumber(bengaliInfo.third)} বঙ্গাব্দ (${bengaliInfo.fourth})",
            bengaliMonth = bengaliInfo.second,
            bengaliSeason = bengaliInfo.fourth,
            hijriDateFormatted = "${toBanglaNumber(hijriInfo.first)} ${hijriInfo.second}, ${toBanglaNumber(hijriInfo.third)} হিজরি",
            hijriMonth = hijriInfo.second
        )
    }

    private fun calculateBengaliDate(cal: Calendar): Quadruple<Int, String, Int, String> {
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) // 0-indexed (0=Jan, 3=Apr, etc.)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)

        // April 14 is Day 104 in non-leap year (or 105 in leap year)
        val isLeapYear = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
        val boishakhStartDayOfYear = if (isLeapYear) 105 else 104

        val bYear = if (dayOfYear >= boishakhStartDayOfYear) year - 593 else year - 594

        // Days in revised Bengali calendar:
        // First 6 months (Boishakh to Ashwin) = 31 days
        // Next 5 months (Kartik to Magh) = 30 days
        // Phalgun = 29 days (30 in leap year), Chaitra = 30 days
        val monthDays = intArrayOf(31, 31, 31, 31, 31, 31, 30, 30, 30, 30, if (isLeapYear) 30 else 29, 30)

        var daysPassedSinceBoishakh = if (dayOfYear >= boishakhStartDayOfYear) {
            dayOfYear - boishakhStartDayOfYear
        } else {
            val totalDaysPrevYear = if (((year - 1) % 4 == 0 && (year - 1) % 100 != 0) || ((year - 1) % 400 == 0)) 366 else 365
            totalDaysPrevYear - (if (totalDaysPrevYear == 366) 105 else 104) + dayOfYear
        }

        var bMonthIndex = 0
        while (bMonthIndex < 12 && daysPassedSinceBoishakh >= monthDays[bMonthIndex]) {
            daysPassedSinceBoishakh -= monthDays[bMonthIndex]
            bMonthIndex++
        }
        if (bMonthIndex >= 12) bMonthIndex = 11

        val bDay = daysPassedSinceBoishakh + 1
        val bMonthName = banglaMonths[bMonthIndex]
        val seasonIndex = (bMonthIndex / 2) % 6
        val bSeason = banglaSeasons[seasonIndex]

        return Quadruple(bDay, bMonthName, bYear, bSeason)
    }

    private fun calculateHijriDate(cal: Calendar): Triple<Int, String, Int> {
        // High accuracy Julian Day Number calculation
        var y = cal.get(Calendar.YEAR)
        var m = cal.get(Calendar.MONTH) + 1
        val d = cal.get(Calendar.DAY_OF_MONTH)

        if (m < 3) {
            y -= 1
            m += 12
        }

        val a = y / 100
        val b = 2 - a + (a / 4)
        val jd = (365.25 * (y + 4716)).toInt() + (30.6001 * (m + 1)).toInt() + d + b - 1524

        // Julian Day to Hijri conversion
        val l = jd - 1948440 + 10632
        val n = ((l - 1) / 10631).toInt()
        val l2 = l - 10631 * n + 354
        val j = (((10985 - l2) / 5316).toInt()) * (((50 * l2) / 17719).toInt()) +
                ((l2 / 5670).toInt()) * (((43 * l2) / 15238).toInt())
        val l3 = l2 - (((30 - j) / 15).toInt()) * (((17719 * j) / 50).toInt()) -
                ((j / 16).toInt()) * (((15238 * j) / 43).toInt()) + 29
        val mHijri = ((24 * l3) / 709).toInt()
        val dHijri = (l3 - ((709 * mHijri) / 24).toInt()).toInt()
        val yHijri = (30 * n + j - 30).toInt()

        val validMonth = ((mHijri - 1) % 12 + 12) % 12
        val monthName = hijriMonths[validMonth]

        return Triple(dHijri, monthName, yHijri)
    }

    private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
