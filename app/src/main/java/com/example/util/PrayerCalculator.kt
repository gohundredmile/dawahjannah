package com.example.util

import com.example.data.model.AsrJuristicMethod
import com.example.data.model.ForbiddenTimeInfo
import com.example.data.model.HighLatitudeRule
import com.example.data.model.PrayerCalculationMethod
import com.example.data.model.PrayerTimeItem
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.asin
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.tan

object PrayerCalculator {

    data class PrayerStatus(
        val activePrayer: PrayerTimeItem?,
        val nextPrayer: PrayerTimeItem?,
        val timeRemainingFormatted: String,
        val countdownHMS: String = "০০:০০:০০",
        val salutationBn: String,
        val prayerList: List<PrayerTimeItem>,
        val forbiddenTimeInfo: ForbiddenTimeInfo = ForbiddenTimeInfo(),
        val locationNameBn: String = "ঢাকা, বাংলাদেশ",
        val locationNameEn: String = "Dhaka, Bangladesh",
        val latitude: Double = 23.8103,
        val longitude: Double = 90.4125,
        val isGpsLocation: Boolean = false,
        val isHanafiAsr: Boolean = true,
        val calculationMethod: PrayerCalculationMethod = PrayerCalculationMethod.KARACHI,
        val asrMethod: AsrJuristicMethod = AsrJuristicMethod.HANAFI,
        val highLatitudeRule: HighLatitudeRule = HighLatitudeRule.ANGLE_BASED,
        val calculationBasisSummaryBn: String = "University of Islamic Sciences, Karachi (১৮°/১৮°) • হানাফী",
        val sunriseTimeFormatted: String = "০৫:৪৬",
        val sunsetTimeFormatted: String = "০৫:৫৭",
        val dayProgressFraction: Float = 0.5f,
        val nextSehriFormatted: String = "০৪:৩১",
        val nextIftarFormatted: String = "০৫:৫৭",
        val iftarRemainingHMS: String = "০১:১৮:৪৩",
        val duhaTimeFormatted: String = "০৬:০১ - ১১:৪০",
        val zawalStartTimeFormatted: String = "১১:৪০",
        val awwabinTimeFormatted: String = "মাগরিবের পর - ০৭:১৩",
        val tahajjudTimeFormatted: String = "ইশার পর - ০৪:৩১",
        val lastThirdOfNightFormatted: String = "০১:১৮",
        val presentPrayerNameBn: String = "এশা",
        val presentNofolNameBn: String = "তাহাজ্জুদ",
        val remainingHours: Int = 0,
        val remainingMinutes: Int = 0,
        val remainingSeconds: Int = 0
    )

    /**
     * Calculates prayer times using precise Jean Meeus / NOAA astronomical solar equations
     * combined with standard international Islamic calculation conventions.
     */
    fun calculatePrayers(
        cal: Calendar = Calendar.getInstance(),
        isHanafiAsr: Boolean = true,
        notificationSettings: Map<String, Boolean> = emptyMap(),
        latitude: Double = 23.8103,
        longitude: Double = 90.4125,
        locationNameBn: String = "ঢাকা, বাংলাদেশ",
        locationNameEn: String = "Dhaka, Bangladesh",
        isGpsLocation: Boolean = false,
        manualOffsetMinutes: Int = 0,
        calculationMethod: PrayerCalculationMethod = PrayerCalculationMethod.KARACHI,
        asrMethod: AsrJuristicMethod = if (isHanafiAsr) AsrJuristicMethod.HANAFI else AsrJuristicMethod.STANDARD,
        highLatitudeRule: HighLatitudeRule = HighLatitudeRule.ANGLE_BASED,
        timezoneOffsetHours: Double? = null
    ): PrayerStatus {
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)
        val currentMinute = cal.get(Calendar.MINUTE)
        val currentSecond = cal.get(Calendar.SECOND)
        val currentTotalMinutes = currentHour * 60 + currentMinute
        val currentTotalSeconds = currentHour * 3600 + currentMinute * 60 + currentSecond

        // 1. Julian Day (Jean Meeus Astronomical Algorithms)
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1 // 1..12
        val day = cal.get(Calendar.DAY_OF_MONTH)

        var y = year
        var m = month
        if (m <= 2) {
            y -= 1
            m += 12
        }
        val a = floor(y / 100.0)
        val b = 2.0 - a + floor(a / 4.0)
        val jd = floor(365.25 * (y + 4716)) + floor(30.6001 * (m + 1)) + day + b - 1524.5

        // Days since J2000.0 epoch (2000 January 1.5)
        val d0 = jd - 2451545.0

        // Mean Anomaly (degrees)
        val g = ((357.529 + 0.98560028 * d0) % 360.0 + 360.0) % 360.0
        val gRad = Math.toRadians(g)

        // Mean Longitude (degrees)
        val q = ((280.459 + 0.98564736 * d0) % 360.0 + 360.0) % 360.0

        // Ecliptic Longitude L (degrees)
        val l = ((q + 1.915 * sin(gRad) + 0.020 * sin(2.0 * gRad)) % 360.0 + 360.0) % 360.0
        val lRad = Math.toRadians(l)

        // Obliquity of Ecliptic e (degrees)
        val e = 23.439 - 0.00000036 * d0
        val eRad = Math.toRadians(e)

        // Declination delta (radians & degrees)
        val sinDelta = sin(eRad) * sin(lRad)
        val deltaRad = asin(sinDelta)

        // Right Ascension RA (hours)
        val raDeg = Math.toDegrees(atan2(cos(eRad) * sin(lRad), cos(lRad)))
        var raHours = raDeg / 15.0
        while (raHours < 0.0) raHours += 24.0
        while (raHours >= 24.0) raHours -= 24.0

        // Equation of Time EqT (hours)
        var eqt = (q / 15.0) - raHours
        while (eqt > 12.0) eqt -= 24.0
        while (eqt < -12.0) eqt += 24.0

        // Timezone in hours
        val tzHours = timezoneOffsetHours ?: (cal.timeZone.getOffset(cal.timeInMillis) / 3600000.0)

        // Solar Noon (hours from local midnight)
        val solarNoonHours = 12.0 + tzHours - (longitude / 15.0) - eqt

        val phiRad = Math.toRadians(latitude)

        // Hour Angle function for a given solar altitude angle in degrees
        fun hourAngle(altitudeDeg: Double): Double? {
            val altRad = Math.toRadians(altitudeDeg)
            val cosW = (sin(altRad) - sin(phiRad) * sin(deltaRad)) / (cos(phiRad) * cos(deltaRad))
            if (cosW > 1.0 || cosW < -1.0) {
                return null // Altitude not reached (polar day/night)
            }
            return Math.toDegrees(acos(cosW)) / 15.0
        }

        // Sunrise & Sunset: Standard altitude -0.8333° (atmospheric refraction + solar disc semi-diameter)
        val wSun = hourAngle(-0.8333) ?: 6.0
        val sunriseHours = solarNoonHours - wSun
        val sunsetHours = solarNoonHours + wSun

        // Night length in hours
        val nightHours = ((24.0 - sunsetHours) + sunriseHours).coerceAtLeast(4.0)

        // Fajr Time
        val fajrAngle = calculationMethod.fajrAngle
        val wFajr = hourAngle(-fajrAngle)
        val fajrHours = if (wFajr != null) {
            solarNoonHours - wFajr
        } else {
            when (highLatitudeRule) {
                HighLatitudeRule.ANGLE_BASED -> sunriseHours - (fajrAngle / 60.0) * nightHours
                HighLatitudeRule.MIDNIGHT -> sunriseHours - 0.5 * nightHours
                HighLatitudeRule.ONE_SEVENTH -> sunriseHours - (nightHours / 7.0)
                HighLatitudeRule.NONE -> sunriseHours - 1.5
            }
        }

        // Asr Time: Sun altitude when shadow equals noon shadow + shadowFactor * object height
        val actualShadowFactor = if (asrMethod == AsrJuristicMethod.HANAFI || isHanafiAsr) 2.0 else 1.0
        val altAsrRad = atan(1.0 / (actualShadowFactor + tan(abs(phiRad - deltaRad))))
        val altAsrDeg = Math.toDegrees(altAsrRad)
        val wAsr = hourAngle(altAsrDeg) ?: (wSun * 0.58)
        val asrHours = solarNoonHours + wAsr

        // Maghrib Time: Standard sunset, or twilight angle (e.g. Shia Qum 4°, Tehran 4.5°)
        val maghribHours = if (calculationMethod.maghribAngle != null) {
            val wMaghrib = hourAngle(-calculationMethod.maghribAngle)
            if (wMaghrib != null) solarNoonHours + wMaghrib else sunsetHours
        } else {
            sunsetHours
        }

        // Isha Time: Angle-based (Karachi, MWL, Egypt, ISNA, etc.) or Fixed Interval (Makkah 90 min)
        val ishaHours = if (calculationMethod.ishaIntervalMinutes != null) {
            maghribHours + (calculationMethod.ishaIntervalMinutes.toDouble() / 60.0)
        } else {
            val ishaAngle = calculationMethod.ishaAngle ?: 18.0
            val wIsha = hourAngle(-ishaAngle)
            if (wIsha != null) {
                solarNoonHours + wIsha
            } else {
                when (highLatitudeRule) {
                    HighLatitudeRule.ANGLE_BASED -> maghribHours + (ishaAngle / 60.0) * nightHours
                    HighLatitudeRule.MIDNIGHT -> maghribHours + 0.5 * nightHours
                    HighLatitudeRule.ONE_SEVENTH -> maghribHours + (nightHours / 7.0)
                    HighLatitudeRule.NONE -> maghribHours + 1.5
                }
            }
        }

        // Convert calculated times to minute integers from midnight, applying fine-tuning offset
        val fajrMin = Math.round(fajrHours * 60.0).toInt() + manualOffsetMinutes
        val sunriseMin = Math.round(sunriseHours * 60.0).toInt() + manualOffsetMinutes
        val sunriseEndMin = sunriseMin + 15

        val dhuhrMin = Math.round(solarNoonHours * 60.0).toInt() + manualOffsetMinutes
        val zawalStartMin = dhuhrMin - 12

        val asrMin = Math.round(asrHours * 60.0).toInt() + manualOffsetMinutes

        val sunsetMin = floor(sunsetHours * 60.0).toInt() + manualOffsetMinutes
        val maghribMin = if (calculationMethod.maghribAngle != null) {
            Math.round(maghribHours * 60.0).toInt() + manualOffsetMinutes
        } else {
            sunsetMin
        }
        val sunsetStartMin = maghribMin - 15

        val ishaMin = Math.round(ishaHours * 60.0).toInt() + manualOffsetMinutes

        fun format12Hour(totalMins: Int): String {
            val normalized = ((totalMins % 1440) + 1440) % 1440
            val h24 = normalized / 60
            val m = normalized % 60
            val amPm = if (h24 < 12) "AM" else "PM"
            val h12 = when (val rem = h24 % 12) {
                0 -> 12
                else -> rem
            }
            return String.format(Locale.getDefault(), "%d:%02d %s", h12, m, amPm)
        }

        fun format24Hour(totalMins: Int): String {
            val normalized = ((totalMins % 1440) + 1440) % 1440
            val h24 = normalized / 60
            val m = normalized % 60
            return String.format(Locale.getDefault(), "%02d:%02d", h24, m)
        }

        fun format24HourBn(totalMins: Int): String {
            val normalized = ((totalMins % 1440) + 1440) % 1440
            val h24 = normalized / 60
            val m = normalized % 60
            val str = String.format(Locale.getDefault(), "%02d:%02d", h24, m)
            return CalendarHelper.toBanglaNumber(str)
        }

        fun formatDuration(startMins: Int, endMins: Int): String {
            var diff = endMins - startMins
            if (diff < 0) diff += 1440
            val h = diff / 60
            val m = diff % 60
            return when {
                h > 0 && m > 0 -> "${CalendarHelper.toBanglaNumber(h)} ঘণ্টা ${CalendarHelper.toBanglaNumber(m)} মিনিট"
                h > 0 -> "${CalendarHelper.toBanglaNumber(h)} ঘণ্টা"
                else -> "${CalendarHelper.toBanglaNumber(m)} মিনিট"
            }
        }

        // Check forbidden times
        val isSunriseForbidden = currentTotalMinutes in sunriseMin until sunriseEndMin
        val isZawalForbidden = currentTotalMinutes in zawalStartMin until dhuhrMin
        val isSunsetForbidden = currentTotalMinutes in sunsetStartMin until maghribMin
        val isCurrentlyForbidden = isSunriseForbidden || isZawalForbidden || isSunsetForbidden
        val activeForbiddenName = when {
            isSunriseForbidden -> "সূর্যোদয়কালীন নিষিদ্ধ সময়"
            isZawalForbidden -> "দ্বিপ্রহর (জাওয়াল) নিষিদ্ধ সময়"
            isSunsetForbidden -> "সূর্যাস্তকালীন নিষিদ্ধ সময়"
            else -> ""
        }

        val forbiddenInfo = ForbiddenTimeInfo(
            sunriseStart24 = format24HourBn(sunriseMin),
            sunriseEnd24 = format24HourBn(sunriseEndMin),
            zawalStart24 = format24HourBn(zawalStartMin),
            zawalEnd24 = format24HourBn(dhuhrMin),
            sunsetStart24 = format24HourBn(sunsetStartMin),
            sunsetEnd24 = format24HourBn(maghribMin),
            sunriseDisplay12 = "${format12Hour(sunriseMin)} - ${format12Hour(sunriseEndMin)}",
            zawalDisplay12 = "${format12Hour(zawalStartMin)} - ${format12Hour(dhuhrMin)}",
            sunsetDisplay12 = "${format12Hour(sunsetStartMin)} - ${format12Hour(maghribMin)}",
            isCurrentlyForbidden = isCurrentlyForbidden,
            activeForbiddenName = activeForbiddenName
        )

        // 5 Primary Salat Items
        val fajrItem = PrayerTimeItem(
            id = "fajr",
            nameEn = "FAJR",
            nameBn = "ফজর",
            subtitleEn = "The Dawn Prayer",
            timeFormatted = format12Hour(fajrMin),
            startTimeFormatted = format12Hour(fajrMin),
            endTimeFormatted = format12Hour(sunriseMin),
            startFormattedBn = format24HourBn(fajrMin),
            endFormattedBn = format24HourBn(sunriseMin),
            durationBn = formatDuration(fajrMin, sunriseMin),
            timeMinutesFromMidnight = fajrMin,
            isPrayer = true,
            isNotificationEnabled = notificationSettings["fajr"] ?: true,
            studyGuideSubtitleBn = "ভোরের প্রথম সালাত ও প্রশান্তির প্রহর • The Dawn Prayer of Serenity & Light",
            rakatsSummaryBn = "মোট ৪ রাকাত: ২ রাকাত সুন্নাতে মুয়াক্কাদা + ২ রাকাত ফরজ।",
            rakatsDetailBn = "রাসুলুল্লাহ (সা.) বলেছেন: 'ফজরের দুই রাকাত সুন্নত দুনিয়া ও তার মধ্যকার সবকিছুর চেয়ে উত্তম।' (সহীহ মুসলিম ৭২৫)\n\n• ফজরের ফরজ সালাতে কেরাত দীর্ঘ করে ধীরেসুস্থে তেলাওয়াত করা মোস্তাহাব। জামাতে প্রথম কাতারে সালাত আদায়ে মুনাফেকির অপবাদ থেকে মুক্তি লাভ হয়।",
            benefitsBn = "ভোরের তাজা বাতাসে ওজোন ও সেরোটোনিনের মাত্রা বেশি থাকে, যা হতাশা ও মানসিক অবসাদ দূর করে। সারাদিনের কাজে উদ্যম, বরকত ও আল্লাহর বিশেষ হেফাজত নিশ্চিত হয়।"
        )

        val dhuhrItem = PrayerTimeItem(
            id = "dhuhr",
            nameEn = "JOHR",
            nameBn = "যোহর",
            subtitleEn = "The Midday Prayer",
            timeFormatted = format12Hour(dhuhrMin),
            startTimeFormatted = format12Hour(dhuhrMin),
            endTimeFormatted = format12Hour(asrMin),
            startFormattedBn = format24HourBn(dhuhrMin),
            endFormattedBn = format24HourBn(asrMin),
            durationBn = formatDuration(dhuhrMin, asrMin),
            timeMinutesFromMidnight = dhuhrMin,
            isPrayer = true,
            isNotificationEnabled = notificationSettings["dhuhr"] ?: true,
            studyGuideSubtitleBn = "দিনের মধ্যভাগের সালাত ও আত্মশুদ্ধি • The Midday Prayer of Renewal",
            rakatsSummaryBn = "মোট ১২ রাকাত: ৪ রাকাত সুন্নাতে মুয়াক্কাদা + ৪ রাকাত ফরজ + ২ রাকাত সুন্নাতে মুয়াক্কাদা + ২ রাকাত নফল।",
            rakatsDetailBn = "রাসুলুল্লাহ (সা.) বলেছেন: 'সূর্য ঢলে পড়ার পর আসমানের দরজাসমূহ উন্মুক্ত করা হয় এবং আমি ভালোবাসি যে এ সময় আমার কোনো নেক আমল উপরে উঠুক।' (তিরমিযী ৪৭৮)\n\n• জুমার দিন সাধারণ যোহরের পরিবর্তে জামাতে খুতবাসহ ২ রাকাত জুমার ফরজ নামাজ আদায় করতে হয়।",
            benefitsBn = "কর্মব্যস্ত দিনের মাঝে মন ও শরীরকে বিশ্রাম দেয়, রক্তচাপ স্বাভাবিক রাখতে সহায়তা করে এবং কাজের মানসিক ক্লান্তি দূর করে নতুন আধ্যাত্মিক শক্তি জোগায়।"
        )

        val asrItem = PrayerTimeItem(
            id = "asr",
            nameEn = "ASR",
            nameBn = "আসর",
            subtitleEn = "The Afternoon Prayer",
            timeFormatted = format12Hour(asrMin),
            startTimeFormatted = format12Hour(asrMin),
            endTimeFormatted = format12Hour(maghribMin),
            startFormattedBn = format24HourBn(asrMin),
            endFormattedBn = format24HourBn(maghribMin),
            durationBn = formatDuration(asrMin, maghribMin),
            timeMinutesFromMidnight = asrMin,
            isPrayer = true,
            isNotificationEnabled = notificationSettings["asr"] ?: true,
            studyGuideSubtitleBn = "সালাতুল উসতা (মধ্যবর্তী সালাত) • The Afternoon Prayer of Remembrance",
            rakatsSummaryBn = "মোট ৮ রাকাত: ৪ রাকাত গায়রে মুয়াক্কাদা সুন্নত + ৪ রাকাত ফরজ।",
            rakatsDetailBn = "আল্লাহ তাআলা ইরশাদ করেছেন: 'তোমরা সমস্ত নামাজের প্রতি যত্নবান হও, বিশেষ করে মধ্যবর্তী নামাজ (আসর)-এর প্রতি।' (সূরা বাকারা: ২৩৮)\n\n• রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি দুই শীতল সময়ের সালাত (ফজর ও আসর) আদায় করবে সে জান্নাতে প্রবেশ করবে।' (সহীহ বুখারী ৫৭৪)",
            benefitsBn = "দিনের শেষভাগে শরীর সচল করে বিপাক প্রক্রিয়া (মেটাবলিজম) উন্নত করে। অলসতা ও গ্লানি দূর করে এবং অন্তরে আল্লাহর ভয় ও প্রশান্তি বৃদ্ধি করে।"
        )

        val maghribItem = PrayerTimeItem(
            id = "maghrib",
            nameEn = "MAGHRIB",
            nameBn = "মাগরিব",
            subtitleEn = "The Sunset Prayer",
            timeFormatted = format12Hour(maghribMin),
            startTimeFormatted = format12Hour(maghribMin),
            endTimeFormatted = format12Hour(ishaMin),
            startFormattedBn = format24HourBn(maghribMin),
            endFormattedBn = format24HourBn(ishaMin),
            durationBn = formatDuration(maghribMin, ishaMin),
            timeMinutesFromMidnight = maghribMin,
            isPrayer = true,
            isNotificationEnabled = notificationSettings["maghrib"] ?: true,
            studyGuideSubtitleBn = "সূর্যাস্তের সালাত ও কৃতজ্ঞতার ক্ষণ • The Sunset Prayer of Gratitude",
            rakatsSummaryBn = "মোট ৭ রাকাত: ৩ রাকাত ফরজ + ২ রাকাত সুন্নাতে মুয়াক্কাদা + ২ রাকাত নফল (আউয়াবিন)।",
            rakatsDetailBn = "সূর্যাস্তের সঙ্গে সঙ্গেই কালক্ষেপণ না করে দ্রুত মাগরিবের সালাত আদায় করা সুন্নাত।\n\n• মাগরিবের পর ৬ রাকাত আউয়াবিন সালাত আদায় করলে বারো বছর নফল ইবাদতের সওয়াব লাভ হয় বলে হাদিসে উল্লেখ রয়েছে।",
            benefitsBn = "দিন ও রাতের মিলনক্ষণে মানসিক শান্তি ও পরম স্রষ্টার প্রতি কৃতজ্ঞতা সৃষ্টি করে। সারাদিনের কাজের শোকরিয়া আদায়ে পরিবারে সুখ ও বরকত বয়ে আনে।"
        )

        val ishaItem = PrayerTimeItem(
            id = "isha",
            nameEn = "ISHA",
            nameBn = "এশা",
            subtitleEn = "The Night Prayer",
            timeFormatted = format12Hour(ishaMin),
            startTimeFormatted = format12Hour(ishaMin),
            endTimeFormatted = format12Hour(fajrMin),
            startFormattedBn = format24HourBn(ishaMin),
            endFormattedBn = format24HourBn(fajrMin),
            durationBn = formatDuration(ishaMin, fajrMin),
            timeMinutesFromMidnight = ishaMin,
            isPrayer = true,
            isNotificationEnabled = notificationSettings["isha"] ?: true,
            studyGuideSubtitleBn = "রাত্রিকালীন সালাত ও পরম নিশ্চিন্ততা • The Night Prayer of Peace & Tranquility",
            rakatsSummaryBn = "মোট ১৭ রাকাত: ৪ রাকাত সুন্নত + ৪ রাকাত ফরজ + ২ রাকাত সুন্নাতে মুয়াক্কাদা + ২ রাকাত নফল + ৩ রাকাত ওয়াজিব বিতর + ২ রাকাত নফল।",
            rakatsDetailBn = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি জামাতের সাথে এশার নামাজ আদায় করল, সে যেন অর্ধ রজনী ইবাদত করল।' (সহীহ মুসলিম ৬৫৬)\n\n• বিতর সালাত রাতের শেষ অংশে তাহাজ্জুদের পর আদায় করা উত্তম, তবে ঘুমানোর পূর্বে পড়ে নেওয়াও নিরাপদ।",
            benefitsBn = "রাতে ঘুমের পূর্বে মন থেকে সমস্ত মানসিক চাপ ও দুশ্চিন্তা দূর করে গভীর ও তৃপ্তিদায়ক ঘুমের আবহ তৈরি করে। রাতের নিরাপত্তায় আল্লাহর জিম্মাদারী পাওয়া যায়।"
        )

        val fiveSalats = listOf(fajrItem, dhuhrItem, asrItem, maghribItem, ishaItem)

        // Determine active prayer among the 5
        val activePrayer: PrayerTimeItem = when {
            currentTotalMinutes in fajrMin until sunriseMin -> fajrItem
            currentTotalMinutes in sunriseMin until dhuhrMin -> fajrItem // past fajr, awaiting dhuhr
            currentTotalMinutes in dhuhrMin until asrMin -> dhuhrItem
            currentTotalMinutes in asrMin until maghribMin -> asrItem
            currentTotalMinutes in maghribMin until ishaMin -> maghribItem
            else -> ishaItem
        }

        // Determine next prayer among the 5
        val nextPrayer: PrayerTimeItem = when {
            currentTotalMinutes < fajrMin -> fajrItem
            currentTotalMinutes in fajrMin until dhuhrMin -> dhuhrItem
            currentTotalMinutes in dhuhrMin until asrMin -> asrItem
            currentTotalMinutes in asrMin until maghribMin -> maghribItem
            currentTotalMinutes in maghribMin until ishaMin -> ishaItem
            else -> fajrItem
        }

        // Mark highlighted item
        val highlightedList = fiveSalats.map { item ->
            item.copy(isHighlighted = item.id == activePrayer.id)
        }

        // Calculate countdown to next prayer in seconds
        val nextSec = nextPrayer.timeMinutesFromMidnight * 60
        var diffSec = nextSec - currentTotalSeconds
        if (diffSec < 0) {
            diffSec += 24 * 3600 // wrap to next day
        }

        val hoursRemaining = diffSec / 3600
        val minsRemaining = (diffSec % 3600) / 60
        val secsRemaining = diffSec % 60

        val countdownFormatted = if (hoursRemaining > 0) {
            "${CalendarHelper.toBanglaNumber(hoursRemaining)} ঘণ্টা ${CalendarHelper.toBanglaNumber(minsRemaining)} মিনিট"
        } else {
            "${CalendarHelper.toBanglaNumber(minsRemaining)} মিনিট ${CalendarHelper.toBanglaNumber(secsRemaining)} সেকেন্ড"
        }

        val sunriseFormattedBn = format24HourBn(sunriseMin)
        val sunsetFormattedBn = format24HourBn(maghribMin)

        // Day progress fraction (0f to 1f)
        val dayProgress = when {
            currentTotalMinutes < sunriseMin -> 0.05f
            currentTotalMinutes > maghribMin -> 0.95f
            else -> {
                val totalDayMinutes = (maghribMin - sunriseMin).toFloat().coerceAtLeast(1f)
                ((currentTotalMinutes - sunriseMin).toFloat() / totalDayMinutes).coerceIn(0.05f, 0.95f)
            }
        }

        // Countdown HMS in Bangla digits (e.g., "০১:২০:৪৮")
        val hStr = String.format(Locale.US, "%02d", hoursRemaining)
        val mStr = String.format(Locale.US, "%02d", minsRemaining)
        val sStr = String.format(Locale.US, "%02d", secsRemaining)
        val countdownHMS = "${CalendarHelper.toBanglaNumber(hStr)}:${CalendarHelper.toBanglaNumber(mStr)}:${CalendarHelper.toBanglaNumber(sStr)}"

        // Next Sehri & Next Iftar
        val nextSehriBn = format24HourBn(fajrMin)
        val nextIftarBn = format24HourBn(maghribMin)

        // Iftar countdown
        val iftarTotalSec = maghribMin * 60
        var iftarDiffSec = iftarTotalSec - currentTotalSeconds
        if (iftarDiffSec < 0) {
            iftarDiffSec += 24 * 3600
        }
        val iftarH = iftarDiffSec / 3600
        val iftarM = (iftarDiffSec % 3600) / 60
        val iftarS = iftarDiffSec % 60
        val iftarHStr = String.format(Locale.US, "%02d", iftarH)
        val iftarMStr = String.format(Locale.US, "%02d", iftarM)
        val iftarSStr = String.format(Locale.US, "%02d", iftarS)
        val iftarRemainingHMS = "${CalendarHelper.toBanglaNumber(iftarHStr)}:${CalendarHelper.toBanglaNumber(iftarMStr)}:${CalendarHelper.toBanglaNumber(iftarSStr)}"

        // Nafl Prayers: Duha / Chasht begins 15 mins after sunrise, ends 12 mins before zawal
        val duhaStart = sunriseMin + 15
        val duhaEnd = dhuhrMin - 12
        val duhaFormatted = "${format24HourBn(duhaStart)} - ${format24HourBn(duhaEnd)}"
        val zawalStartFormatted = format24HourBn(zawalStartMin)
        val awwabinFormatted = "মাগরিবের পর - ${format24HourBn(ishaMin)}"
        val tahajjudFormatted = "ইশার পর - ${format24HourBn(fajrMin)}"

        // Last third of night calculation
        val nightDuration = (fajrMin + 1440) - maghribMin
        val lastThirdStartMin = (maghribMin + (2 * nightDuration / 3)) % 1440
        val lastThirdFormatted = format24HourBn(lastThirdStartMin)

        // Time-aware salutation
        val salutation = when {
            currentTotalMinutes < fajrMin -> "তাহাজ্জুদ ও নিশীথ ইবাদতের প্রহর — আসসালামু আলাইকুম"
            currentTotalMinutes < sunriseMin -> "শুভ ফজরের বরকতময় প্রভাত — আসসালামু আলাইকুম"
            currentTotalMinutes < dhuhrMin -> "স্নিগ্ধ সকাল ও চাশতের সময় — আসসালামু আলাইকুম"
            currentTotalMinutes < asrMin -> "যোহরের বরকতময় দ্বিপ্রহর — আসসালামু আলাইকুম"
            currentTotalMinutes < maghribMin -> "আসরের শান্ত বিকেল — আসসালামু আলাইকুম"
            currentTotalMinutes < ishaMin -> "মাগরিব ও সন্ধ্যার স্নিগ্ধ ক্ষণ — আসসালামু আলাইকুম"
            else -> "এশা ও বরকতময় রজনী — আসসালামু আলাইকুম"
        }

        // Present Wakt & Nofol Salat Information for Flip Board
        val (presentName, nofolName) = when {
            currentTotalMinutes in fajrMin until sunriseMin -> {
                Pair("ফজর", "তাহিয়্যাতুল ওজু")
            }
            currentTotalMinutes in sunriseMin until sunriseEndMin -> {
                Pair("সূর্যোদয়", "সালাত নিষিদ্ধ")
            }
            currentTotalMinutes in sunriseEndMin until zawalStartMin -> {
                Pair("চাশত", "সালাতুত দুহা")
            }
            currentTotalMinutes in zawalStartMin until dhuhrMin -> {
                Pair("জাওয়াল", "সালাত নিষিদ্ধ")
            }
            currentTotalMinutes in dhuhrMin until asrMin -> {
                Pair("যোহর", "যোহরের নফল")
            }
            currentTotalMinutes in asrMin until sunsetStartMin -> {
                Pair("আসর", "আসরের পূর্ব সুন্নাত")
            }
            currentTotalMinutes in sunsetStartMin until maghribMin -> {
                Pair("আসর", "সূর্যাস্ত (মাকরূহ)")
            }
            currentTotalMinutes in maghribMin until ishaMin -> {
                Pair("মাগরিব", "আওয়াবিন")
            }
            else -> {
                // Isha wakt & late night tahajjud period
                Pair("এশা", "তাহাজ্জুদ")
            }
        }

        val calculationBasisSummary = "${calculationMethod.titleBn} (${calculationMethod.fajrAngle.toInt()}°/${calculationMethod.ishaAngle?.toInt() ?: "${calculationMethod.ishaIntervalMinutes}মি."}°) • ${if (actualShadowFactor == 2.0) "হানাফী (মিসলে সানি)" else "শাফেয়ী/আদর্শ (মিসলে আওয়াল)"}"

        return PrayerStatus(
            activePrayer = activePrayer,
            nextPrayer = nextPrayer,
            timeRemainingFormatted = countdownFormatted,
            countdownHMS = countdownHMS,
            salutationBn = salutation,
            prayerList = highlightedList,
            forbiddenTimeInfo = forbiddenInfo,
            locationNameBn = locationNameBn,
            locationNameEn = locationNameEn,
            latitude = latitude,
            longitude = longitude,
            isGpsLocation = isGpsLocation,
            isHanafiAsr = actualShadowFactor == 2.0,
            calculationMethod = calculationMethod,
            asrMethod = if (actualShadowFactor == 2.0) AsrJuristicMethod.HANAFI else AsrJuristicMethod.STANDARD,
            highLatitudeRule = highLatitudeRule,
            calculationBasisSummaryBn = calculationBasisSummary,
            sunriseTimeFormatted = sunriseFormattedBn,
            sunsetTimeFormatted = sunsetFormattedBn,
            dayProgressFraction = dayProgress,
            nextSehriFormatted = nextSehriBn,
            nextIftarFormatted = nextIftarBn,
            iftarRemainingHMS = iftarRemainingHMS,
            duhaTimeFormatted = duhaFormatted,
            zawalStartTimeFormatted = zawalStartFormatted,
            awwabinTimeFormatted = awwabinFormatted,
            tahajjudTimeFormatted = tahajjudFormatted,
            lastThirdOfNightFormatted = lastThirdFormatted,
            presentPrayerNameBn = presentName,
            presentNofolNameBn = nofolName,
            remainingHours = hoursRemaining,
            remainingMinutes = minsRemaining,
            remainingSeconds = secsRemaining
        )
    }
}
