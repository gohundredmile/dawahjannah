package com.example.util

import androidx.compose.ui.graphics.Color
import com.example.data.datasource.IslamicCalendarCatalog
import com.example.data.model.LiveDateAmolItem
import java.util.Calendar

object LiveTimeAndEventMatcher {

    /**
     * Resolves all matching date & time based amol, special days, duas and events
     * for the current exact moment so nothing is ever missed.
     */
    fun getMatchingItems(
        calendarInfo: CalendarHelper.TripleCalendarInfo? = null,
        cal: Calendar = Calendar.getInstance()
    ): List<LiveDateAmolItem> {
        val list = mutableListOf<LiveDateAmolItem>()

        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

        val hijriDetail = CalendarHelper.getHijriDateDetail(cal)
        val hijriDay = hijriDetail.day
        val hijriMonthIndex = hijriDetail.monthIndex
        val hijriMonthNameBn = hijriDetail.monthNameBn

        // ==========================================
        // 1. EXACT TIME-OF-DAY / WAQT BASED MATCHING
        // ==========================================
        when (hour) {
            in 0..4 -> {
                // Last 3rd of Night / Tahajjud
                list.add(
                    LiveDateAmolItem(
                        id = "live_tahajjud_night",
                        categoryBn = "রাতের শেষ প্রহর • তাহাজ্জুদ",
                        titleBn = "রাতের শেষ তৃতীয়াংশ ও তাহাজ্জুদ",
                        shortSubtitleBn = "আল্লাহ প্রথম আসমানে নেমে ডাকেন: কে দোয়া করবে? আমি কবুল করব!",
                        badgeBn = "দোয়া কবুল ক্ষণ",
                        iconKey = "MOON",
                        primaryColor = Color(0xFF6366F1),
                        arabicText = "لَا إِلَهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
                        pronunciationBn = "লা ইলা-হা ইল্লা আনতা সুবহা-নাকা ইন্নী কুনতু মিনায জোয়া-লিমীন।",
                        meaningBn = "হে আল্লাহ! তুমি ছাড়া সত্য কোনো উপাস্য নেই, তুমি পরম পবিত্র! নিশ্চয়ই আমি অপরাধীদের অন্তর্ভুক্ত।",
                        virtuesBn = "রাসূলুল্লাহ ﷺ বলেছেন: ‘আমাদের রব প্রতি রাতের শেষ তৃতীয়াংশে প্রথম আসমানে অবতরণ করেন এবং আহ্বান করেন: কে আছো যে আমাকে ডাকবে, আমি তার ডাকে সাড়া দেব! কে ক্ষমা চাইবে, আমি ক্ষমা করব!’ (সহীহ বুখারী ১১৪৫)",
                        referenceBn = "সহীহ আল-বুখারী: ১১৪৫, সহীহ মুসলিম: ৭৫৮",
                        timingContextBn = "মধ্যরাত থেকে ফজর পর্যন্ত সময় হলো মহান আল্লাহর সান্নিধ্য ও দোয়া কবুলের শ্রেষ্ঠতম মুহূর্ত।",
                        actionTarget = "nofol_salat",
                        actionButtonTextBn = "তাহাজ্জুদ ও নফল সালাত গাইড"
                    )
                )
            }
            in 5..6 -> {
                // Fajr / Dawn
                list.add(
                    LiveDateAmolItem(
                        id = "live_fajr_morning",
                        categoryBn = "ফজর পরবর্তী ভোর",
                        titleBn = "সকালের মাসনূন আযকার ও দিনের সুরক্ষা",
                        shortSubtitleBn = "আয়াতুল কুরসী, ৩ কুল ও সকালের শ্রেষ্ঠ সহীহ যিকরসমূহ",
                        badgeBn = "দিনের সূচনা আমল",
                        iconKey = "SUN",
                        primaryColor = Color(0xFFD97706),
                        arabicText = "أَصْبَحْنَا وَأَصْبَحَ الْمُلْكُ لِلَّهِ وَالْحَمْدُ لِلَّهِ، لَا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لَا شَرِيكَ لَهُ",
                        pronunciationBn = "আসবাহনা ওয়া আসবাহাল মুলকু লিল্লাহি ওয়ালহামদু লিল্লাহ, লা ইলা-হা ইল্লাল্লা-হু ওয়াহদাহু লা শারীকা লাহু।",
                        meaningBn = "আমরা সকালে উপনীত হয়েছি এবং সমস্ত রাজত্বও আল্লাহর জন্যই সকালে উপনীত হয়েছে, সমস্ত প্রশংসা আল্লাহর। আল্লাহ ছাড়া সত্য কোনো উপাস্য নেই, তিনি একক, তাঁর কোনো শরিক নেই।",
                        virtuesBn = "ফজরের পর সকালের মাসনূন আযকার পাঠকারীকে আল্লাহ তায়ালা দিনভর শয়তানের অনিষ্ট, বদনজর ও আকস্মিক বিপদ থেকে সার্বিক সুরক্ষায় রাখেন।",
                        referenceBn = "সহীহ মুসলিম: ২৭২৩, সুনান আবু দাউদ: ৫০৬৮",
                        timingContextBn = "ফজর সালাতের পর থেকে সূর্যোদয় পর্যন্ত সময় যিকির ও দোয়ার জন্য অতি বরকতময়।",
                        actionTarget = "morning_evening",
                        actionButtonTextBn = "সকাল-সন্ধ্যার দো'আ দেখুন"
                    )
                )
            }
            in 7..11 -> {
                // Ishraq / Salatud Duha (Chasht)
                list.add(
                    LiveDateAmolItem(
                        id = "live_duha_chasht",
                        categoryBn = "চাশতের ওয়াক্ত • সালাতুদ দুহা",
                        titleBn = "সালাতুদ দুহা (চাশতের আমল)",
                        shortSubtitleBn = "৩৬০ জোড়ার সদকা ও দিনভর রিযিকের বিশেষ জিম্মাদারি",
                        badgeBn = "রিযিক ও শেফা",
                        iconKey = "SUN",
                        primaryColor = Color(0xFFF59E0B),
                        arabicText = "اللَّهُمَّ اغْفِرْ لِي وَتُبْ عَلَيَّ إِنَّكَ أَنْتَ التَّوَّابُ الرَّحِيمُ",
                        pronunciationBn = "আল্লাহুম্মাগফিরলী ওয়া তুব ‘আলাইয়্যা ইন্নাকা আনতাত তাওয়্যাবুর রাহীম।",
                        meaningBn = "হে আল্লাহ! আমাকে ক্ষমা করুন এবং আমার তাওবা কবুল করুন, নিশ্চয় আপনি অতিশয় তাওবা কবুলকারী ও পরম দয়ালু।",
                        virtuesBn = "রাসূলুল্লাহ ﷺ বলেছেন: ‘প্রতিটি মানবদেহে ৩৬০টি জোড়া রয়েছে; চাশতের দুই রাকাত সালাত এর প্রত্যেকটির পক্ষ থেকে সদকা হিসেবে যথেষ্ট হয়ে যায়।’ (সহীহ মুসলিম ৭২০)",
                        referenceBn = "সহীহ মুসলিম: ৭২০, সুনান আবু দাউদ: ৫২৪২",
                        timingContextBn = "সূর্য ওঠার ১৫-২০ মিনিট পর থেকে যোহরের পূর্ব পর্যন্ত সালাতুদ দুহার মোক্ষম সময়।",
                        actionTarget = "nofol_salat",
                        actionButtonTextBn = "চাশতের সালাত বিস্তারিত"
                    )
                )
            }
            in 12..15 -> {
                // Zohr time - Gates of heaven open
                list.add(
                    LiveDateAmolItem(
                        id = "live_zohr_gates",
                        categoryBn = "যোহরের ওয়াক্ত • আসমানের দরজা",
                        titleBn = "আসমানের দরজা খোলার বরকতময় ক্ষণ",
                        shortSubtitleBn = "সূর্য ঢলে পড়ার পর আসমানের দরজাসমূহ উন্মুক্ত হওয়ার সময়",
                        badgeBn = "দোয়া কবুল ক্ষণ",
                        iconKey = "PRAYER",
                        primaryColor = Color(0xFF047857),
                        arabicText = "اللَّهُ أَكْبَرُ كَبِيرًا وَالْحَمْدُ لِلَّهِ كَثِيرًا وَسُبْحَانَ اللَّهِ بُكْرَةً وَأَصِيلاً",
                        pronunciationBn = "আল্লাহু আকবার কাবীরা, ওয়াল হামদু লিল্লাহি কাছীরা ওয়া সুবহানাল্লাহি বুকরাতান ওয়া আসীলা।",
                        meaningBn = "আল্লাহ মহান, অতি মহান। আল্লাহ তাআলার জন্য অজস্র প্রশংসা এবং সকাল-সন্ধ্যায় আমি আল্লাহর পবিত্রতা বর্ণনা করছি।",
                        virtuesBn = "রাসূলুল্লাহ ﷺ বলেন: ‘যোহরের পূর্বে সূর্য ঢলে পড়লে আসমানের দরজাসমূহ খুলে দেওয়া হয়। আমি চাই এ সময়ে আমার কোনো নেক আমল উপরে উঠুক।’ (তিরমিযী ৪৭৮)",
                        referenceBn = "জামে আত-তিরমিযী: ৪৭৮, সহীহ মুসলিম: হা/৯৪৮",
                        timingContextBn = "যোহরের ওয়াক্ত প্রবেশের সাথে সাথে জান্নাত ও রহমতের দরজাগুলো উন্মুক্ত করা হয়।",
                        actionTarget = "five_waqt",
                        actionButtonTextBn = "৫ ওয়াক্ত সালাত পরবর্তী দো'আ"
                    )
                )
            }
            in 16..17 -> {
                // Asr to Maghrib - Evening Azkar & Sayyidul Istighfar
                list.add(
                    LiveDateAmolItem(
                        id = "live_asr_evening",
                        categoryBn = "সন্ধ্যার ওয়াক্ত • জান্নাত লাভের আমল",
                        titleBn = "সন্ধ্যার আযকার ও সাইয়্যেদুল ইস্তিগফার",
                        shortSubtitleBn = "মাগরিবের পূর্বে রাতের নিরাপত্তা ও জান্নাত ওয়াজিবের শ্রেষ্ঠ দু'আ",
                        badgeBn = "সন্ধ্যায় পঠিতব্য",
                        iconKey = "SUN",
                        primaryColor = Color(0xFFEA580C),
                        arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي، فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
                        pronunciationBn = "আল্লাহুম্মা আনতা রব্বী, লা ইলা-হা ইল্লা আনতা, খালাক্বতানী ওয়া আনা ‘আবদুকা, ওয়া আনা ‘আলা ‘আহদিকা ওয়া ওয়া‘দিকা মাসতাত্বা‘তু, আ‘ঊযু বিকা মিন শার্রি মা সানা‘তু, আবূউ লাকা বিনি‘মাতিকা ‘আলাইয়্যা, ওয়া আবূউ লাকা বিযাম্বী, ফাগফির লী, ফাইন্নাহূ লা ইয়াগফিরুয যুনূবা ইল্লা আনতা।",
                        meaningBn = "হে আল্লাহ! আপনিই আমার রব। আপনি ছাড়া সত্য কোনো উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি আমার সাধ্যমতো আপনার সাথে কৃত অঙ্গীকার ও প্রতিশ্রুতির উপর কায়েম আছি...",
                        virtuesBn = "রাসূলুল্লাহ ﷺ বলেছেন: ‘যে ব্যক্তি সন্ধ্যায় বিশ্বাস সহকারে সাইয়্যেদুল ইস্তিগফার পড়বে আর সে রাতে মারা যাবে, সে নিশ্চিত জান্নাতবাসী হবে।’ (সহীহ বুখারী ৬৩০৬)",
                        referenceBn = "সহীহ আল-বুখারী: ৬৩০৬",
                        timingContextBn = "আসর সালাত শেষ করে মাগরিবের পূর্ব পর্যন্ত সন্ধ্যার আযকার ও সুরক্ষার জন্য সর্বোত্তম সময়।",
                        actionTarget = "sayyidul_istighfar",
                        actionButtonTextBn = "সাইয়্যেদুল ইস্তিগফার বিস্তারিত"
                    )
                )
            }
            in 18..19 -> {
                // Maghrib / Sunset / Iftar
                list.add(
                    LiveDateAmolItem(
                        id = "live_maghrib_iftar",
                        categoryBn = "মাগরিবের ওয়াক্ত • দোয়া কবুল",
                        titleBn = "মাগরিব ও ইফতারের আগমুহূর্ত",
                        shortSubtitleBn = "দোয়া নিশ্চিত কবুলের মোক্ষম সময় ও মাগরিবের সুন্নাত",
                        badgeBn = "দোয়া কবুল ক্ষণ",
                        iconKey = "MOON",
                        primaryColor = Color(0xFF0D9488),
                        arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِرَحْمَتِكَ الَّتِي وَسِعَتْ كُلَّ شَيْءٍ أَنْ تَغْفِرَ لِي",
                        pronunciationBn = "আল্লাহুম্মা ইন্নী আস-আলুকা বিরাহমাতিকাল্লাতী ওয়াসী‘আত কুল্লা শাইয়িন আন তাগফিরা লী।",
                        meaningBn = "হে আল্লাহ! আমি আপনার সেই সর্বব্যাপী রহমতের উসিলায় প্রার্থনা করছি—আমাকে ক্ষমা করে দিন।",
                        virtuesBn = "নবীজী ﷺ ইরশাদ করেছেন: ‘সিয়াম পালনকারীর ইফতারের মুহূর্তের দো'আ কখনো ফিরিয়ে দেওয়া হয় না।’ (সুনান ইবনে মাজাহ ১৭৫৩)",
                        referenceBn = "সুনান ইবন মাজাহ: ১৭৫৩, জামে আত-তিরমিযী: ৩৫৯৮",
                        timingContextBn = "সূর্যাস্ত এবং মাগরিবের আযানের সময় রহমতের দুয়ার খুলে যায়।",
                        actionTarget = "dua_acceptance",
                        actionButtonTextBn = "দোয়া কবুলের বিশেষ অধ্যায়"
                    )
                )
            }
            else -> {
                // 20..23 (Night / Isha to Sleep)
                list.add(
                    LiveDateAmolItem(
                        id = "live_night_protection",
                        categoryBn = "রাত্রিকালীন আমল • কবর সুরক্ষা",
                        titleBn = "সূরা মুলক ও সূরা বাকারার শেষ ২ আয়াত",
                        shortSubtitleBn = "কবরের আজাব থেকে মুক্তি ও সারারাত আল্লাহর বিশেষ নিরাপত্তা",
                        badgeBn = "রাতের ফরজ আমল",
                        iconKey = "BOOK",
                        primaryColor = Color(0xFF4338CA),
                        arabicText = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ",
                        pronunciationBn = "আ-মানার রাসূ-লু বিমা- উনঝিলা ইলাইহি মির রাব্বিহী ওয়াল মু'মিনূন, কুল্লুন আ-মানা বিল্লা-হি ওয়া মালা-ইকাতিহী...",
                        meaningBn = "রাসূল তার রবের পক্ষ থেকে যা নাযিল হয়েছে তার প্রতি ঈমান এনেছে এবং মুমিনগণও। প্রত্যেকে ঈমান এনেছে আল্লাহর উপর, তাঁর ফেরেশতাকুল, কিতাবসমূহ ও তাঁর রাসূলগণের উপর...",
                        virtuesBn = "নবী করীম ﷺ বলেছেন: ‘যে ব্যক্তি রাতের বেলা সূরা আল বাকারার শেষ দু'টি আয়াত পাঠ করবে, তা তার জন্য যথেষ্ট হবে।’ (বুখারী ৪০০৮) এবং সূরা মুলক কবরের আজাব থেকে প্রতিরোধক।",
                        referenceBn = "সহীহ আল-বুখারী: ৪০০৮, জামে আত-তিরমিযী: ২৮৯১",
                        timingContextBn = "এশার পর ঘুমানোর পূর্বে এই সূরা দুটি পাঠ করা রাসূলুল্লাহ ﷺ-এর স্থায়ী সুন্নাত ছিল।",
                        actionTarget = "surah_baqarah",
                        actionButtonTextBn = "সূরা বাকারার শেষ ২ আয়াত পড়ুন"
                    )
                )
            }
        }

        // ==========================================
        // 2. DAY OF WEEK MATCHING (FRIDAY, MON, THU, WED)
        // ==========================================
        val isFridayActive = FridayTimingHelper.isFridayModeActive(cal)
        if (isFridayActive) {
            // Holy Friday Primary Card
            list.add(
                0, // Place Friday front and center when Friday mode is active!
                LiveDateAmolItem(
                    id = "live_friday_primary",
                    categoryBn = if (dayOfWeek == Calendar.THURSDAY) "পবিত্র জুমার রজনী • সাইয়্যিদুল আইয়্যাম" else "আজ জুমার দিন • সাইয়্যিদুল আইয়্যাম",
                    titleBn = if (dayOfWeek == Calendar.THURSDAY) "পবিত্র জুমার রজনী — Friday Mode সক্রিয়" else "আজ পবিত্র জুমার দিন — Friday Mode সক্রিয়",
                    shortSubtitleBn = "সূরা কাহাফ পাঠ, নবীজি ﷺ-এর উপর অধিক দরূদ ও প্রধান ৫ সুন্নাত",
                    badgeBn = "জুমার বরকতময় সময়",
                    iconKey = "MOSQUE",
                    primaryColor = Color(0xFF047857),
                    arabicText = "إِنَّ اللَّهَ وَمَلَائِكَتَهُ يُصَلُّونَ عَلَى النَّبِيِّ ۚ يَا أَيُّهَا الَّذِينَ آمَنُوا صَلُّوا عَلَيْهِ وَسَلِّمُوا تَسْلِيمًا",
                    pronunciationBn = "ইন্নাল্লা-হা ওয়া মালা-ইকাতাহূ ইউসাল্লূনা ‘আলান নাবী, ইয়া আইয়্যূহাল্লাযীনা আ-মানূ সাল্লূ ‘আলাইহি ওয়া সাল্লিমূ তাসলীমা-।",
                    meaningBn = "নিশ্চয়ই আল্লাহ ও তাঁর ফেরেশতাগণ নবীর প্রতি দরূদ পাঠান। হে ঈমানদারগণ! তোমরাও তাঁর ওপর দরূদ পাঠ করো এবং যথাযথ সম্মান প্রদর্শনপূর্বক সালাম জানাও।",
                    virtuesBn = "রাসূলুল্লাহ ﷺ বলেছেন: ‘জুমার দিনে আমার ওপর অধিক পরিমাণে দরূদ পাঠ করো, কেননা তোমাদের দরূদ আমার নিকট পেশ করা হয়।’ (আবু দাউদ ১০৪৭) এবং ‘যে ব্যক্তি জুমায় সূরা কাহাফ পড়বে, তার জন্য দুই জুমার মধ্যবর্তী সময় নূর প্রজ্বলিত থাকবে।’",
                    referenceBn = "সুনান আবু দাঊদ: ১০৪৭, মুসতাদরাক হাকিম: ৩৩৯২",
                    timingContextBn = "বৃহস্পতিবার মাগরিব থেকে শুক্রবার মাগরিব পর্যন্ত পুরো জুমার সময়টি বরকতে পরিপূর্ণ।",
                    actionTarget = "friday_mode",
                    actionButtonTextBn = "🕌 Friday Mode খুলুন"
                )
            )

            // Friday Sa'atul Ijabah (Dua acceptance hour - featured on Friday)
            if (dayOfWeek == Calendar.FRIDAY) {
                list.add(
                    LiveDateAmolItem(
                        id = "live_friday_saatul_ijabah",
                        categoryBn = "জুমার দোয়া কবুল ক্ষণ",
                        titleBn = "সা'আতুল ইজাবাহ (দোয়া কবুলের সোনালী ক্ষণ)",
                        shortSubtitleBn = "আসরের শেষ প্রহরে দো'আ কবুলের নিশ্চিত প্রতিশ্রুত সময়",
                        badgeBn = "দোয়া কবুল ক্ষণ",
                        iconKey = "PRAYER",
                        primaryColor = Color(0xFF0D9488),
                        arabicText = "يَا حَيُّ يَا قَيُّومُ بِرَحْمَتِكَ أَسْتَغِيثُ، أَصْلِحْ لِي شَأْنِي كُلَّهُ وَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ",
                        pronunciationBn = "ইয়া হাইয়্যু ইয়া ক্বাইয়্যূমু বিরাহমাতিকা আস্তাগীছ, আসলিহ লী শা’নী কুল্লাহু ওয়ালা তাকিলনী ইলা নাফসী তারফাতা ‘আইন।",
                        meaningBn = "হে চিরঞ্জীব! হে সমস্ত সৃষ্টির পরিচালক ও রক্ষক! আপনারই রহমতের উসিলায় সাহায্য প্রার্থনা করছি। আমার সার্বিক অবস্থা সংশোধন করে দিন এবং পলকের জন্যও আমাকে আমার নিজের দায়িত্বে ছেড়ে দেবেন না।",
                        virtuesBn = "নবী করীম ﷺ ইরশাদ করেছেন: ‘জুমার দিনে এমন একটি মুহূর্ত রয়েছে, কোনো মুসলিম বান্দা সালাতরত অবস্থায় সে সময় আল্লাহর কাছে যা চাইবে, আল্লাহ তাকে তা দান করবেনই।’ তোমরা তা আসরের শেষ প্রহরে তালাশ করো।",
                        referenceBn = "সহীহ আল-বুখারী: ৯৩৫, সুনান আবু দাউদ: ১০৪৮",
                        timingContextBn = "বিশেষ করে আসরের সালাতের পর থেকে সূর্যাস্ত পর্যন্ত মন খুলে রবের দরবারে হাত তুলে দো'আ করুন।",
                        actionTarget = "friday_mode",
                        actionButtonTextBn = "জুমার বিশেষ দো'আ ও আমল"
                    )
                )
            }
        } else if (dayOfWeek == Calendar.THURSDAY) {
            list.add(
                LiveDateAmolItem(
                    id = "live_thursday_eve",
                    categoryBn = "বৃহস্পতিবারের আমল ও জুমার রাত",
                    titleBn = "আমল পেশের দিন ও জুমার রজনী",
                    shortSubtitleBn = "সূর্যাস্তের পর থেকেই শুরু হয় জুমার নূর ও অধিক দরূদ পাঠের সুন্নাত",
                    badgeBn = "জুমার আগমনী",
                    iconKey = "SPARKLE",
                    primaryColor = Color(0xFF0F766E),
                    arabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ إِنَّكَ حَمِيدٌ مَجِيدٌ",
                    pronunciationBn = "আল্লাহুম্মা সাল্লি ‘আলা মুহাম্মাদিঁও ওয়া ‘আলা আ-লি মুহাম্মাদ...",
                    meaningBn = "হে আল্লাহ! মুহাম্মদ ﷺ এবং তাঁর পরিবারের ওপর রহমত বর্ষণ করুন, যেমন আপনি ইব্রাহীম (আ.) ও তাঁর পরিবারের ওপর রহমত বর্ষণ করেছিলেন...",
                    virtuesBn = "বৃহস্পতিবার দিবাগত রাত থেকেই জুমার রজনী শুরু হয়। এ রাতে বেশি বেশি দরূদ পাঠ করা অত্যন্ত ফজিলতপূর্ণ সুন্নাত।",
                    referenceBn = "সুনান বায়হাকী: ৫৯৯৪",
                    timingContextBn = "বৃহস্পতিবার সূর্যাস্তের পর থেকেই জুমার নূর বর্ষিত হতে থাকে।",
                    actionTarget = "friday_mode",
                    actionButtonTextBn = "জুমার প্রস্তুতি ও আমল"
                )
            )
        } else if (dayOfWeek == Calendar.MONDAY) {
            list.add(
                LiveDateAmolItem(
                    id = "live_monday_fast",
                    categoryBn = "সোমবারের সুন্নাত",
                    titleBn = "আজ সোমবারের সুন্নাত সিয়াম ও আমল",
                    shortSubtitleBn = "নবীজী ﷺ-এর জন্ম ও নবুওয়াত লাভের দিন • নফল রোযার সুন্নাত",
                    badgeBn = "সুন্নাত সিয়াম",
                    iconKey = "HEART",
                    primaryColor = Color(0xFF059669),
                    arabicText = "رَبَّنَا تَقَبَّلْ مِنَّا ۖ إِنَّكَ أَنتَ السَّمِيعُ الْعَلِيمُ",
                    pronunciationBn = "রব্বানা তাক্বাব্বল মিন্না ইন্নাকা আনতাস সামী‘উল ‘আলীম।",
                    meaningBn = "হে আমাদের রব! আমাদের পক্ষ থেকে এটি কবুল করে নিন, নিশ্চয় আপনি সর্বশ্রোতা, সর্বজ্ঞানী।",
                    virtuesBn = "নবীজী ﷺ-কে সোমবারের রোযা সম্পর্কে জিজ্ঞেস করা হলে তিনি বলেন: ‘এ দিনে আমি জন্মগ্রহণ করেছি এবং এ দিনেই আমার ওপর অহী নাযিল হয়েছে।’ (সহীহ মুসলিম ১১৬২)",
                    referenceBn = "সহীহ মুসলিম: ১১৬২",
                    timingContextBn = "প্রতি সোমবারে রোযা রাখা ও বেশি বেশি কুরআন তিলাওয়াত করা নববী সুন্নাত।",
                    actionTarget = "islamic_habit",
                    actionButtonTextBn = "ইসলামিক অভ্যাস ও ট্র্যাকার"
                )
            )
        } else if (dayOfWeek == Calendar.WEDNESDAY) {
            list.add(
                LiveDateAmolItem(
                    id = "live_wednesday_dua",
                    categoryBn = "বুধবারের বিশেষ ক্ষণ",
                    titleBn = "বুধবার যোহর ও আসরের মধ্যবর্তী দো'আ",
                    shortSubtitleBn = "জাবের (রা.) বর্ণিত দু'আ কবুলের বরকতময় ঐতিহাসিক মুহূর্ত",
                    badgeBn = "দোয়া কবুল ক্ষণ",
                    iconKey = "PRAYER",
                    primaryColor = Color(0xFF16A34A),
                    arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنَ الْخَيْرِ كُلِّهِ عَاجِلِهِ وَآجِلِهِ مَا عَلِمْتُ مِنْهُ وَمَا لَمْ أَعْلَمْ",
                    pronunciationBn = "আল্লাহুম্মা ইন্নী আস-আলুকা মিনাল খাইরি কুল্লিহী ‘আজিলিহী ওয়া আজিলিহী মা ‘আলিমতু মিনহু ওয়ামা লাম আ‘লাম।",
                    meaningBn = "হে আল্লাহ! আমি আপনার নিকট পার্থিব ও পরকালীন সর্বপ্রকার কল্যাণ যাচনা করছি, যার কিছু আমি জানি এবং কিছু জানি না।",
                    virtuesBn = "জাবের (রা.) বলেন: ‘নবীজী ﷺ মসজিদে ফাতহে বুধবার যোহর ও আসরের মধ্যবর্তী সময়ে দো'আ করেছিলেন এবং তাঁর দো'আ কবুল হয়েছিল। আমি যখনই কোনো বিপদে পড়ে এ সময়ে দো'আ করেছি, আল্লাহর সাড়া পেয়েছি।’",
                    referenceBn = "আল-আদাবুল মুফরাদ: ৭০৪, মুসনাদে আহমাদ: ১৪৬০০",
                    timingContextBn = "বুধবার যোহরের পর থেকে আসর পর্যন্ত সময় দু'আ কবুলিয়তের ঐতিহাসিক মুহূর্ত।",
                    actionTarget = "dua_acceptance",
                    actionButtonTextBn = "দোয়া কবুল হওয়ার অধ্যায়"
                )
            )
        }

        // ==========================================
        // 3. ISLAMIC HIJRI DATE & MONTH MATCHING
        // ==========================================
        if (hijriDay in 13..15) {
            list.add(
                LiveDateAmolItem(
                    id = "live_ayyam_al_bidh",
                    categoryBn = "আইয়ামে বীজের সুন্নাত • চাঁদের ১৪/১৫",
                    titleBn = "আইয়ামে বীজের নফল সিয়াম ($hijriDay $hijriMonthNameBn)",
                    shortSubtitleBn = "প্রতি চান্দ্রমাসের ১৩, ১৪ ও ১৫ তারিখ রোযা রাখা সারা বছর রোযার সমান",
                    badgeBn = "সুন্নাত সিয়াম",
                    iconKey = "MOON",
                    primaryColor = Color(0xFF0284C7),
                    arabicText = "اللَّهُمَّ إِنِّي نَوَيْتُ أَنْ أَصُومَ تَطَوُّعًا لِلَّهِ تَعَالَى",
                    pronunciationBn = "আল্লাহুম্মা ইন্নী নাওয়াইতু আন আসূমা তাত্বাওউ‘আন লিল্লাহি তা‘আলা।",
                    meaningBn = "হে আল্লাহ! আমি আপনার সন্তুষ্টির উদ্দেশ্যে নফল রোযা রাখার সংকল্প করছি।",
                    virtuesBn = "রাসূলুল্লাহ ﷺ বলেছেন: ‘প্রতি চান্দ্রমাসের ১৩, ১৪ ও ১৫ তারিখ রোযা রাখা সারা বছর রোযা রাখার সমান।’ (সহীহ বুখারী ১৯৮১)",
                    referenceBn = "সহীহ আল-বুখারী: ১৯৮১, সহীহ মুসলিম: ১১৫৯",
                    timingContextBn = "পূর্ণিমার আলোকিত দিনগুলোতে রোযা রাখা আত্মিক ও শারীরিক স্বাস্থ্যের জন্য অত্যন্ত উপকারী।",
                    actionTarget = "triple_calendar",
                    actionButtonTextBn = "ইসলামিক ক্যালেন্ডার দেখুন"
                )
            )
        }

        // Ramadan Month (Month Index 8)
        if (hijriMonthIndex == 8) {
            list.add(
                LiveDateAmolItem(
                    id = "live_ramadan_special",
                    categoryBn = "পবিত্র মাহে রমাদান",
                    titleBn = "রমাদানের মোবারক দিন ও সিয়াম",
                    shortSubtitleBn = "রহমত, মাগফিরাত ও নাজাতের মাস • কুরআন ও সিয়ামের অফুরন্ত বরকত",
                    badgeBn = "রমাদানুল মুবারক",
                    iconKey = "MOON",
                    primaryColor = Color(0xFF047857),
                    arabicText = "اللَّهُمَّ لَكَ صُمْتُ وَعَلَى رِزْقِكَ أَفْطَرْتُ",
                    pronunciationBn = "আল্লাহুম্মা লাকা সুমতু ওয়া ‘আলা রিযক্বিকা আফতারতু।",
                    meaningBn = "হে আল্লাহ! আমি আপনার জন্যই সিয়াম পালন করেছি এবং আপনার রিযিক দিয়েই ইফতার করছি।",
                    virtuesBn = "রাসূলুল্লাহ ﷺ বলেছেন: ‘যে ব্যক্তি ঈমান ও সাওয়াবের আশায় রমাদানের রোযা রাখে, তার পূর্ববর্তী সমস্ত গুনাহ ক্ষমা করে দেওয়া হয়।’ (সহীহ বুখারী ৩৮)",
                    referenceBn = "সহীহ আল-বুখারী: ৩৮, সহীহ মুসলিম: ৭৬০",
                    timingContextBn = "রমাদানের প্রতি মুহূর্তই রহমত ও জাহান্নাম থেকে মুক্তির সুবর্ণ সুযোগ।",
                    actionTarget = "ramadan_intelligence",
                    actionButtonTextBn = "রমাদান ইন্টেলিজেন্স"
                )
            )
        }

        // Dhul-Hijjah 1-10
        if (hijriMonthIndex == 11 && hijriDay in 1..10) {
            val isArafah = hijriDay == 9
            list.add(
                LiveDateAmolItem(
                    id = "live_dhul_hijjah_10",
                    categoryBn = if (isArafah) "আজ ইয়াউমে আরাফাহ" else "যিলহজ্জের সেরা ১০ দিন",
                    titleBn = if (isArafah) "আরাফাত দিবসের আমল ও সিয়াম" else "যিলহজ্জের সেরা ১০ দিনের আমল ($hijriDay যিলহজ্জ)",
                    shortSubtitleBn = if (isArafah) "বিগত ও আগামী বছরের গুনাহের কাফফারা" else "বছরের শ্রেষ্ঠ দিনগুলোতে অধিক তাকবীর, তাহলীল ও নেক আমল",
                    badgeBn = "বছরের শ্রেষ্ঠ আমল",
                    iconKey = "MOSQUE",
                    primaryColor = Color(0xFFB45309),
                    arabicText = "لا إِلَهَ إِلَّا اللَّهُ وَحْدَهُ لا شَرِيكَ لَهُ، لَهُ الْمُلْكُ وَلَهُ الْحَمْدُ، وَهُوَ عَلَى كُلِّ شَيْءٍ قَدِيرٌ",
                    pronunciationBn = "লা ইলা-হা ইল্লাল্লা-হু ওয়াহদাহূ লা শারীকা লাহূ, লাহুল মুলকু ওয়ালাহুল হামদু, ওয়াহুওয়া ‘আলা কুল্লি শাইয়িন ক্বাদীর।",
                    meaningBn = "একমাত্র আল্লাহ ছাড়া সত্য কোনো উপাস্য নেই, তাঁর কোনো অংশীদার নেই; রাজত্ব একমাত্র তাঁরই, সমস্ত প্রশংসাও তাঁরই এবং তিনি সকল কিছুর ওপর ক্ষমতাবান।",
                    virtuesBn = if (isArafah) {
                        "নবীজী ﷺ বলেছেন: ‘আরাফাত দিবসের রোযা বিগত এক বছর ও আগামী এক বছরের গুনাহের কাফফারা হিসেবে কবুল হবে।’ (সহীহ মুসলিম ১১৬২)"
                    } else {
                        "রাসূলুল্লাহ ﷺ বলেছেন: ‘যিলহজ্জের প্রথম ১০ দিনের চেয়ে এমন কোনো দিন নেই যাতে নেক আমল আল্লাহর নিকট অধিক প্রিয়।’ (সহীহ বুখারী ৯৬৯)"
                    },
                    referenceBn = "সহীহ মুসলিম: ১১৬২, সহীহ আল-বুখারী: ৯৬৯",
                    timingContextBn = "যিলহজ্জের প্রথম দশ দিন আল্লাহর নিকট বছরের সকল দিন অপেক্ষা অধিক সম্মানিত।",
                    actionTarget = "dua_acceptance",
                    actionButtonTextBn = "আরাফাহ ও যিলহজ্জ দো'আ"
                )
            )
        }

        // Ashura (Muharram 9-10)
        if (hijriMonthIndex == 0 && hijriDay in 9..10) {
            list.add(
                LiveDateAmolItem(
                    id = "live_ashura",
                    categoryBn = "পবিত্র আশুরার আমল",
                    titleBn = "আশুরার সুন্নাত সিয়াম ও তাওবা",
                    shortSubtitleBn = "মুহাররমের ৯ ও ১০ তারিখের রোযা বিগত এক বছরের গুনাহ মোচন করে",
                    badgeBn = "গুনাহ মাফের সুসংবাদ",
                    iconKey = "HEART",
                    primaryColor = Color(0xFF4F46E5),
                    arabicText = "رَبَّنَا ظَلَمْنَا أَنفُسَنَا وَإِن لَّمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ",
                    pronunciationBn = "রব্বানা যালামনা আনফুসানা ওয়া ইল্লাম তাগফির লানা ওয়া তারহামনা লানাকূনান্না মিনাল খাসিরীন।",
                    meaningBn = "হে আমাদের রব! আমরা নিজেদের ওপর জুলুম করেছি; আপনি যদি ক্ষমা না করেন এবং দয়া না করেন তবে নিশ্চয় আমরা ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হব।",
                    virtuesBn = "রাসূলুল্লাহ ﷺ ইরশাদ করেছেন: ‘আশুরার দিনের রোযা সম্পর্কে আমি আল্লাহর নিকট আশা করি যে, তা বিগত এক বছরের গুনাহের কাফফারা হয়ে যাবে।’ (সহীহ মুসলিম ১১৬২)",
                    referenceBn = "সহীহ মুসলিম: ১১৬২",
                    timingContextBn = "আশুরার রোযা মুহাররমের ৯ এবং ১০ তারিখে পালন করা উত্তম সুন্নাত।",
                    actionTarget = "triple_calendar",
                    actionButtonTextBn = "ইসলামিক ক্যালেন্ডার"
                )
            )
        }

        // Today's Historical Event (if exists in catalog)
        if (IslamicCalendarCatalog.hasHistoricalEvent(hijriMonthIndex, hijriDay)) {
            val dayContext = IslamicCalendarCatalog.buildDayContext(hijriDay, hijriMonthIndex, 1448, cal)
            dayContext.historicalEvents.firstOrNull()?.let { event ->
                list.add(
                    LiveDateAmolItem(
                        id = "live_hist_event_${event.id}",
                        categoryBn = "আজকের ঐতিহাসিক দিবস",
                        titleBn = event.titleBn,
                        shortSubtitleBn = event.dateSummaryBn,
                        badgeBn = "স্মরণীয় দিবস",
                        iconKey = "BOOK",
                        primaryColor = Color(0xFF0F766E),
                        arabicText = null,
                        pronunciationBn = null,
                        meaningBn = event.descriptionBn,
                        virtuesBn = event.scholarlyNoteBn ?: "ইসলামিক ইতিহাস ও সীরাত পর্যালোচনা",
                        referenceBn = event.primarySourceBn,
                        timingContextBn = "আজকের হিজরি তারিখে সংঘটিত ঐতিহাসিক তাৎপর্যপূর্ণ ঘটনা।",
                        actionTarget = "triple_calendar",
                        actionButtonTextBn = "ক্যালেন্ডার ও ইতিহাস দেখুন"
                    )
                )
            }
        }

        // ==========================================
        // 4. GUARANTEED HIGH-YIELD DUA ESSENTIALS
        // (Ensures user always has at least 3-5 items)
        // ==========================================
        // 4.1 Isme Azam (Always active for prayer acceptance)
        list.add(
            LiveDateAmolItem(
                id = "live_always_isme_azam",
                categoryBn = "দো'আ কবুলের শ্রেষ্ঠ উসিলা",
                titleBn = "ইসমে আযমের মাধ্যমে দু'আ",
                shortSubtitleBn = "আল্লাহর সেই মহান নাম যার দ্বারা চাইলে নিশ্চিত দান করা হয়",
                badgeBn = "শ্রেষ্ঠ দো'আ",
                iconKey = "STAR",
                primaryColor = Color(0xFFB45309),
                arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ بِأَنِّي أَشْهَدُ أَنَّكَ أَنْتَ اللَّهُ لَا إِلَهَ إِلَّا أَنْتَ، الأَحَدُ الصَّمَدُ، الَّذِي لَمْ يَلِدْ وَلَمْ يُولَدْ، وَلَمْ يَكُنْ لَهُ كُفُوًا أَحَدٌ",
                pronunciationBn = "আল্লা-হুম্মা ইন্নী আস-আলুকা বি-আন্নী আশহাদু আন্নাকা আনতাল্লা-হু লা ইলা-হা ইল্লা আনতা, আল-আহাদুস সামাদ, আল্লাযী লাম ইয়ালিদ ওয়া লাম ইউলাদ, ওয়া লাম ইয়াকুল লাহু কুফুওয়ান আহাদ।",
                meaningBn = "হে আল্লাহ! আমি আপনার কাছে প্রার্থনা করছি এই সাক্ষ্য দিয়ে যে, নিশ্চয়ই আপনিই একমাত্র আল্লাহ, আপনি ছাড়া কোনো সত্য উপাস্য নেই। আপনি একক, অমুখাপেক্ষী...",
                virtuesBn = "হযরত বুরাইদাহ (রা.) থেকে বর্ণিত: রাসূলুল্লাহ ﷺ ইরশাদ করেছেন—‘সে আল্লাহর সেই ইসমে আযমের উসিলায় প্রার্থনা করেছে, যা দিয়ে দু‘আ করা হলে তিনি কবুল করেন এবং কোনো কিছু চাওয়া হলে তা দান করেন।’",
                referenceBn = "সুনান আবু দাঊদ: ১৪৯৩, জামে আত-তিরমিযী: ৩৪৭৫",
                timingContextBn = "সালাতের ভিতরে, সালামের পূর্বে ও যেকোনো নেক ইচ্ছা পূরণে দো'আর শুরুতে পাঠ্য।",
                actionTarget = "isme_azam",
                actionButtonTextBn = "ইসমে আযম পূর্ণাঙ্গ সংকলন"
            )
        )

        // 4.2 Surah Tawbah last 2 ayahs
        list.add(
            LiveDateAmolItem(
                id = "live_always_tawbah",
                categoryBn = "তাওয়াক্কুল ও সংকট মুক্তি",
                titleBn = "সূরা তাওবার শেষ ২ আয়াত",
                shortSubtitleBn = "সকাল-সন্ধ্যায় ৭ বার পাঠে সব কাজের সমাধান ও আল্লাহর সার্বিক জিম্মাদারি",
                badgeBn = "দৈনিক আমল",
                iconKey = "SHIELD",
                primaryColor = Color(0xFF047857),
                arabicText = "فَإِن تَوَلَّوْا فَقُلْ حَسْبِيَ اللَّهُ لَا إِلَهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
                pronunciationBn = "ফা-ইন তাওয়াল্লাও ফাক্বুল হাসবিয়াল্লাহু লা ইলাহা ইল্লা হুওয়া ‘আলাইহি তাওয়াক্কালতু ওয়াহুওয়া রাব্বুল ‘আরশিল ‘আযীম।",
                meaningBn = "অতঃপর যদি তারা মুখ ফিরিয়ে নেয়, তবে আপনি বলে দিন: আমার জন্য আল্লাহই যথেষ্ট! তিনি ছাড়া সত্য কোনো উপাস্য নেই। তাঁরই ওপর আমি নির্ভর করেছি এবং তিনিই মহান আরশের অধিপতি।",
                virtuesBn = "হযরত আবু দারদা (রা.) বলেন: ‘যে ব্যক্তি সকাল-সন্ধ্যায় সূরা তাওবার শেষ দু'টি আয়াত সাতবার করে পাঠ করবে, আল্লাহতায়ালা তার সব চিন্তা ও কাজের জন্য যথেষ্ট হয়ে যাবেন।’",
                referenceBn = "সূরা আত-তাওবাহ্: ১২৯, সুনান আবু দাউদ: ৫০৮১",
                timingContextBn = "প্রতিদিন সকাল ও সন্ধ্যায় এবং ফরজ সালাতের পর পাঠ অত্যন্ত বরকতময়।",
                actionTarget = "tawbah_last_two",
                actionButtonTextBn = "সূরা তাওবার শেষ ২ আয়াত দেখুন"
            )
        )

        return list
    }
}
