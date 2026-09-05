package com.example.util

import com.example.data.model.ForbiddenTimeInfo
import com.example.data.model.PrayerTimeItem
import java.util.Calendar
import java.util.Locale

object PrayerCalculator {

    data class PrayerStatus(
        val activePrayer: PrayerTimeItem?,
        val nextPrayer: PrayerTimeItem?,
        val timeRemainingFormatted: String,
        val salutationBn: String,
        val prayerList: List<PrayerTimeItem>,
        val forbiddenTimeInfo: ForbiddenTimeInfo = ForbiddenTimeInfo(),
        val locationNameBn: String = "ঢাকা, বাংলাদেশ",
        val locationNameEn: String = "Dhaka, Bangladesh",
        val latitude: Double = 23.8103,
        val longitude: Double = 90.4125,
        val isGpsLocation: Boolean = false,
        val isHanafiAsr: Boolean = true
    )

    fun calculatePrayers(
        cal: Calendar = Calendar.getInstance(),
        isHanafiAsr: Boolean = true,
        notificationSettings: Map<String, Boolean> = emptyMap(),
        latitude: Double = 23.8103,
        longitude: Double = 90.4125,
        locationNameBn: String = "ঢাকা, বাংলাদেশ",
        locationNameEn: String = "Dhaka, Bangladesh",
        isGpsLocation: Boolean = false,
        manualOffsetMinutes: Int = 0
    ): PrayerStatus {
        val currentHour = cal.get(Calendar.HOUR_OF_DAY)
        val currentMinute = cal.get(Calendar.MINUTE)
        val currentSecond = cal.get(Calendar.SECOND)
        val currentTotalMinutes = currentHour * 60 + currentMinute
        val currentTotalSeconds = currentHour * 3600 + currentMinute * 60 + currentSecond

        // Seasonal calculation baseline for Dhaka / Subcontinent region
        // Reference values aligned to user's standard table:
        // Fajr: 4:24 AM - 5:41 AM
        // Sunrise forbidden: 05:41 - 05:56 (5:41 AM - 5:56 AM)
        // Zawal forbidden: 11:47 - 11:57 (11:47 AM - 11:57 AM)
        // Johr: 11:57 AM - 3:26 PM
        // Asr: 3:26 PM - 6:14 PM
        // Sunset forbidden: 17:59 - 18:14 (5:59 PM - 6:14 PM)
        // Maghrib: 6:14 PM - 7:30 PM
        // Isha: 7:30 PM - 4:24 AM

        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        // Geographic offset calculation relative to Dhaka standard meridian (90.4125° E, 23.8103° N)
        // 1 degree longitude = ~4 minutes time difference
        val geoLongitudeOffset = -(((longitude - 90.4125) * 4.0).toInt())
        val geoLatitudeOffset = (((latitude - 23.8103) * 1.5) * Math.sin(2 * Math.PI * (dayOfYear - 80) / 365.25)).toInt()
        val totalGeoOffset = geoLongitudeOffset + geoLatitudeOffset + manualOffsetMinutes

        // Slight seasonal variation offset centered around early September (day ~ 247)
        val daysFromReference = dayOfYear - 247
        val seasonalOffset = (15 * Math.sin(2 * Math.PI * daysFromReference / 365.25)).toInt()

        val fajrMin = 4 * 60 + 24 + seasonalOffset + totalGeoOffset
        val sunriseMin = 5 * 60 + 41 + seasonalOffset + totalGeoOffset
        val sunriseEndMin = sunriseMin + 15

        val dhuhrMin = 11 * 60 + 57 + totalGeoOffset
        val zawalStartMin = dhuhrMin - 10

        val asrMin = (if (isHanafiAsr) 15 * 60 + 26 - (seasonalOffset / 2) else 15 * 60 + 5 - (seasonalOffset / 2)) + totalGeoOffset

        val maghribMin = 18 * 60 + 14 - seasonalOffset + totalGeoOffset
        val sunsetStartMin = maghribMin - 15

        val ishaMin = 19 * 60 + 30 - seasonalOffset + totalGeoOffset
        val tahajjudMin = 2 * 60 + 45 + seasonalOffset + totalGeoOffset

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
            sunriseStart24 = format24Hour(sunriseMin),
            sunriseEnd24 = format24Hour(sunriseEndMin),
            zawalStart24 = format24Hour(zawalStartMin),
            zawalEnd24 = format24Hour(dhuhrMin),
            sunsetStart24 = format24Hour(sunsetStartMin),
            sunsetEnd24 = format24Hour(maghribMin),
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

        return PrayerStatus(
            activePrayer = activePrayer,
            nextPrayer = nextPrayer,
            timeRemainingFormatted = countdownFormatted,
            salutationBn = salutation,
            prayerList = highlightedList,
            forbiddenTimeInfo = forbiddenInfo,
            locationNameBn = locationNameBn,
            locationNameEn = locationNameEn,
            latitude = latitude,
            longitude = longitude,
            isGpsLocation = isGpsLocation,
            isHanafiAsr = isHanafiAsr
        )
    }
}

