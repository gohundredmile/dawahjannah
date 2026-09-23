package com.example.data.datasource

import com.example.data.model.FastingCategory
import com.example.data.model.HistoricalCertainty
import com.example.data.model.IslamicDayContext
import com.example.data.model.IslamicHistoricalEvent
import com.example.data.model.IslamicMonthProfile
import com.example.data.model.IslamicWorshipAction
import com.example.data.model.LunarPhaseInfo
import com.example.util.CalendarHelper
import java.util.Calendar

object IslamicCalendarCatalog {

    val islamicMonths: List<IslamicMonthProfile> = listOf(
        IslamicMonthProfile(
            monthIndex = 0,
            nameEn = "Muharram",
            nameAr = "محرم",
            nameBn = "মুহাররম",
            isSacredMonth = true,
            meaningAndEtymologyBn = "মুহাররম শব্দের অর্থ 'মর্যাদাপূর্ণ' বা 'নিষিদ্ধ'। প্রাক-ইসলামী যুগ থেকেই এই মাসে সব ধরণের যুদ্ধবিগ্রহ ও রক্তপাত কঠোরভাবে নিষিদ্ধ ও হারাম ছিল।",
            keySpiritualThemeBn = "হিজরি নববর্ষের সূচনা, আল্লাহর মাস (শাহরুল্লাহ), অতীতের গুনাহ মোচনে আশুরার সুন্নাত রোযা এবং আত্মিক নবায়ন।",
            sacredMonthVerseBn = "সূরা আত-তাহরীম ও আত-তাওবাহ ৯:৩৬ — «নিশ্চয়ই আল্লাহর নিকট গণনা অনুসারে মাস বারটি... তন্মধ্যে চারটি মাস সম্মানিত (আশহুরুল হুরুম)»।",
            specialRulingsBn = listOf(
                "চারটি সম্মানিত মাসের অন্যতম; এই মাসে পাপকর্মের ভয়াবহতা এবং নেক আমলের সওয়াব বহুগুণ বৃদ্ধি পায়।",
                "রাসূলুল্লাহ ﷺ বলেছেন: «রমাদানের পর সর্বাধিক উত্তম রোযা হলো আল্লাহর মাস মুহাররমের রোযা» (সহীহ মুসলিম ১১৬৩)।",
                "৯ ও ১০ই মুহাররম আশুরার রোযা রাখা সুন্নাত মুআক্কাদাহ।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 1,
            nameEn = "Safar",
            nameAr = "صفر",
            nameBn = "সফর",
            isSacredMonth = false,
            meaningAndEtymologyBn = "সফর শব্দের অর্থ 'রিক্ত' বা 'শূন্য হওয়া'। জাহেলী যুগে আরবরা মুহাররমের যুদ্ধবিরতি শেষে এই মাসে অভিযানে বের হতো এবং তাদের ঘরবাড়ি জনশূন্য হয়ে যেত।",
            keySpiritualThemeBn = "কুসংস্কার ও অশুভ লক্ষণ পরিহার করে সর্বাবস্থায় এক আল্লাহর ওপর তাওয়াক্কুল (ভরসা) স্থাপন।",
            sacredMonthVerseBn = null,
            specialRulingsBn = listOf(
                "জাহেলী যুগের লোকেরা সফর মাসকে অশুভ বা বালামুসিবতের মাস মনে করত; ইসলাম এই অমূলক কুসংস্কারকে সমূলে বাতিল করেছে।",
                "রাসূলুল্লাহ ﷺ দ্ব্যর্থহীনভাবে ঘোষণা করেছেন: «কোনো অশুভ লক্ষণ নেই, সফর মাসের কোনো অশুভ প্রভাব নেই» (সহীহ আল-বুখারী ৫৭৭)।",
                "অন্যান্য সাধারণ মাসের মতোই এতে আইয়ামে বীজ ও সোম-বৃহস্পতিবারের নফল রোযা রাখা সুন্নাত।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 2,
            nameEn = "Rabi' al-Awwal",
            nameAr = "ربيع الأول",
            nameBn = "রবিউল আউয়াল",
            isSacredMonth = false,
            meaningAndEtymologyBn = "রবিউল আউয়াল শব্দের অর্থ 'প্রথম বসন্ত'। আরবে বসন্তের সৌন্দর্য ও ফুলেল প্রকৃতির প্রস্ফুটনকালে এই মাসের নামকরণ করা হয়েছিল।",
            keySpiritualThemeBn = "বিশ্বজগতের রহমত রাসূলুল্লাহ ﷺ-এর জীবন, সুন্নাহর একনিষ্ঠ অনুসরণ, সীরাত চর্চা এবং দরূদ পাঠ।",
            sacredMonthVerseBn = "সূরা আল-আম্বিয়া ২১:১০৭ — «আমি আপনাকে সমগ্র বিশ্বজগতের জন্য কেবল রহমত স্বরূপ প্রেরণ করেছি»।",
            specialRulingsBn = listOf(
                "এই মাসের ১২ই রবিউল আউয়াল রাসূলুল্লাহ ﷺ ওফাত লাভ করেন এবং তিনি সোমবারে জন্মগ্রহণ করেছিলেন (সহীহ মুসলিম ১১৬২)।",
                "এই মাসে এবং সর্বদা রাসূলুল্লাহ ﷺ-এর সুন্নাহকে ব্যক্তি ও সমাজ জীবনে পুনরুজ্জীবিত করা সর্বাধিক মর্যাদাপূর্ণ আমল।",
                "কোরআন পাঠ, সীরাতুন্নবী চর্চা ও বেশি বেশি দরূদ পাঠের মাধ্যমে নবীপ্রেম প্রকাশ করা।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 3,
            nameEn = "Rabi' al-Thani",
            nameAr = "ربيع الثاني",
            nameBn = "রবিউস সানি",
            isSacredMonth = false,
            meaningAndEtymologyBn = "রবিউস সানি (বা রবিউল আখির) অর্থ 'দ্বিতীয় বসন্তকাল'। বসন্তের শেষভাগে এই মাসের অবস্থান ছিল।",
            keySpiritualThemeBn = "সুন্নাহর ওপর অবিচলতা (ইস্তিকামাত), দ্বীনি জ্ঞানার্জন এবং নিয়মিত নফল ইবাদতের ধারাবাহিকতা রক্ষা।",
            sacredMonthVerseBn = null,
            specialRulingsBn = listOf(
                "নিয়মিত ফারায়েয পালনের সাথে সাথে তাহাজ্জুদ ও সালাতুদ দুহার যত্ন নেওয়া।",
                "আইয়ামে বীজের (১৩, ১৪, ১৫ তারিখ) রোযা রাখা সারা বছর রোযা রাখার সমান (সহীহ আল-বুখারী ১৯৮১)।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 4,
            nameEn = "Jumada al-Awwal",
            nameAr = "جمادى الأولى",
            nameBn = "জমাদিউল আউয়াল",
            isSacredMonth = false,
            meaningAndEtymologyBn = "জমাদ শব্দের অর্থ বরফজমা বা তীব্র শীতল। এই মাসে আরবে প্রচণ্ড ঠান্ডায় পানি জমে যেত বলে এর নাম রাখা হয়েছিল জমাদিউল আউয়াল।",
            keySpiritualThemeBn = "অসহায় মানুষের পাশে দাঁড়ানো, পরিবার ও আত্মীয়তার বন্ধন সুদৃঢ় করা এবং তাকওয়ার অনুশীলন।",
            sacredMonthVerseBn = null,
            specialRulingsBn = listOf(
                "শীতকালীন ঠান্ডার দিনে রোযা রাখা মুমিনের জন্য সহজ ও অফুরন্ত বরকতের গনীমত।",
                "রাসূলুল্লাহ ﷺ বলেছেন: «শীতকাল হলো মুমিনের বসন্তকাল; এর দিনগুলো ছোট হওয়ায় সহজে রোযা রাখা যায় এবং রাত দীর্ঘ হওয়ায় সালাতুত তাহাজ্জুদ আদায় করা যায়» (মুসনাদে আহমাদ ১১৬৮২)।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 5,
            nameEn = "Jumada al-Thani",
            nameAr = "جمادى الآخرة",
            nameBn = "জমাদিউস সানি",
            isSacredMonth = false,
            meaningAndEtymologyBn = "জমাদিউস সানি অর্থ শীতের দ্বিতীয় বা শেষ পর্যায়।",
            keySpiritualThemeBn = "আসন্ন সম্মানিত মাসসমূহ (রজব, শাবান, রমাদান)-এর জন্য আত্মিক ও মানসিক প্রস্তুতি গ্রহণ।",
            sacredMonthVerseBn = null,
            specialRulingsBn = listOf(
                "সালাফগণ জমাদিউস সানি মাস থেকেই আত্মিক পরিশুদ্ধি ও রমাদানের রোযার প্রস্তুতি শুরু করতেন।",
                "পূর্ববর্তী কোনো কাযা রোযা বা মানতের রোযা থাকলে তা আদায় করে ফেলা।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 6,
            nameEn = "Rajab",
            nameAr = "رجب",
            nameBn = "রজব",
            isSacredMonth = true,
            meaningAndEtymologyBn = "রজব শব্দের অর্থ 'শ্রদ্ধা করা' বা 'সম্মানিত করা'। প্রাক-ইসলামী আরববাসী এই মাসকে অপরিসীম মর্যাদা দিত এবং যুদ্ধের অস্ত্র নামিয়ে রাখত।",
            keySpiritualThemeBn = "পাপ বর্জন, তাওবা-ইস্তিগফার এবং রমাদানের ক্ষেত্র প্রস্তুত করা (বীজ বপনের মাস)।",
            sacredMonthVerseBn = "সূরা আত-তাওবাহ ৯:৩৬ — «অতএব এই সম্মানিত চার মাসে তোমরা নিজেদের প্রতি কোনো জুলুম করো না»।",
            specialRulingsBn = listOf(
                "চারটি সম্মানিত মাসের অন্যতম; এ মাসে পাপের শাস্তি মারাত্মক এবং নেক আমলের মর্যাদা সুউচ্চ।",
                "আবু বকর আল-বালখী (রহ.) বলতেন: «রজব মাস হলো বীজ বপনের মাস, শাবান মাস ফসলে পানি সেচের মাস আর রমাদান হলো ফসল ঘরে তোলার মাস»।",
                "রজব মাসে সুনির্দিষ্ট দিন ধার্য করে কোনো নবউদ্ভাবিত বা বিদ'আতি রোযা না রেখে সাধারণ সুন্নাত রোযা রাখা উত্তম।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 7,
            nameEn = "Sha'ban",
            nameAr = "شعبان",
            nameBn = "শাবান",
            isSacredMonth = false,
            meaningAndEtymologyBn = "তাশা'উব বা ছড়িয়ে পড়া থেকে শাবান। জাহেলী যুগে আরবরা রজব মাসের যুদ্ধবিরতি শেষ হওয়ার পর পানির সন্ধানে ও অভিযানে বিভিন্ন গোত্রে ছড়িয়ে পড়ত।",
            keySpiritualThemeBn = "আল্লাহর কাছে বাৎসরিক আমলনামা পেশের মাস, বেশি বেশি নফল রোযা এবং রমাদানের চূড়ান্ত প্রস্তুতি।",
            sacredMonthVerseBn = null,
            specialRulingsBn = listOf(
                "রাসূলুল্লাহ ﷺ রমাদান ব্যতীত অন্য কোনো মাসে শাবানের মতো এত অধিক নফল রোযা রাখতেন না (সহীহ আল-বুখারী ১৯৬৯, সহীহ মুসলিম ১১৫৬)।",
                "উসামা ইবনে যায়েদ (রা.) রাসূলুল্লাহ ﷺ-কে এর কারণ জিজ্ঞাসা করলে তিনি বলেন: «এটি এমন এক মাস যা রজব ও রমাদানের মাঝে অবস্থিত এবং মানুষ এর ব্যাপারে উদাসীন থাকে। এই মাসে রাব্বুল আলামীনের কাছে বান্দার আমল পেশ করা হয়। আর আমি চাই রোযাদার অবস্থায় আমার আমল পেশ করা হোক» (সুনান আন-নাসায়ী ২৩৫৭)।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 8,
            nameEn = "Ramadan",
            nameAr = "رمضان",
            nameBn = "রমাদান",
            isSacredMonth = false,
            meaningAndEtymologyBn = "রমদ্ব বা রমদ্বা অর্থ তপ্ত পাথর বা জ্বালিয়ে দেওয়া। এ মাসে তীব্র আত্মসংযম ও রোযার মাধ্যমে বান্দার পাপরাশি ভস্মীভূত হয়।",
            keySpiritualThemeBn = "কুরআন নাযিল, সিয়াম সাধনা, তারাবীহ ও তাহাজ্জুদ, সহস্র মাসের চেয়ে সেরা কদরের রাত এবং জাহান্নাম থেকে মুক্তি।",
            sacredMonthVerseBn = "সূরা আল-বাকারাহ ২:১৮৫ — «রমাদান মাস, যাতে কুরআন অবতীর্ণ করা হয়েছে মানবজাতির পথপ্রদর্শক হিসেবে... কাজেই যে ব্যক্তি এই মাস পাবে, সে যেন অবশ্যই রোযা রাখে»।",
            specialRulingsBn = listOf(
                "সুস্থ ও মুকিম প্রাপ্তবয়স্ক প্রত্যেক মুসলিমের ওপর রমাদানের ৩০টি রোযা রাখা ইসলামের অন্যতম ফরজ রুকন।",
                "জান্নাতের দরজা উন্মুক্ত করে দেওয়া হয়, জাহান্নামের দরজা রুদ্ধ করা হয় এবং শয়তানকে শৃঙ্খলিত করা হয় (সহীহ বুখারী ১৮৯৮)।",
                "শেষ দশকের বিজোড় রাতগুলোতে লাইলাতুল কদর তালাশ করা।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 9,
            nameEn = "Shawwal",
            nameAr = "شوال",
            nameBn = "শাওয়াল",
            isSacredMonth = false,
            meaningAndEtymologyBn = "শাওয়াল শব্দের অর্থ উঠিয়ে নেওয়া বা বহন করা। উটের লেজ উঠানো বা গর্ভধারণের সময় থেকে এই নামকরণ করা হয়েছিল।",
            keySpiritualThemeBn = "ঈদুল ফিতরের আনন্দ, সাদাকাতুল ফিতর আদায় এবং শাওয়ালের ৬ রোযা রাখার মাধ্যমে সারা বছর রোযার সওয়াব অর্জন।",
            sacredMonthVerseBn = null,
            specialRulingsBn = listOf(
                "১লা শাওয়াল ঈদুল ফিতরের দিন রোযা রাখা সর্বসম্মতভাবে হারাম (সহীহ আল-বুখারী ১৯৯১, সহীহ মুসলিম ১১৩৮)।",
                "ঈদের পরবর্তী দিনগুলো থেকে পুরো শাওয়াল মাসের মধ্যে যেকোনো ৬টি নফল রোযা রাখা সুন্নাত মুআক্কাদাহ।",
                "রাসূলুল্লাহ ﷺ বলেছেন: «যে ব্যক্তি রমাদানের রোযা রাখল এবং এর পর শাওয়ালের ছয়টি রোযা রাখল, সে যেন সারা বছরই রোযা রাখল» (সহীহ মুসলিম ১১৬৪)।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 10,
            nameEn = "Dhu al-Qa'dah",
            nameAr = "ذو القعدة",
            nameBn = "যিলকদ",
            isSacredMonth = true,
            meaningAndEtymologyBn = "কু'উদ বা বসে থাকা থেকে যিলকদ। এই মাসে আরবরা যুদ্ধবিগ্রহ এবং শিকার ত্যাগ করে শান্ত হয়ে স্বগৃহে বসে থাকত এবং হজ্জের প্রস্তুতি গ্রহণ করত।",
            keySpiritualThemeBn = "হজ্জের পবিত্র মাসসমূহের সূচনা, পারস্পরিক শান্তি রক্ষা, হিংসা-বিদ্বেষ ত্যাগ এবং আত্মশুদ্ধি।",
            sacredMonthVerseBn = "সূরা আল-বাকারাহ ২:১৯৭ — «হজ্জের মাসসমূহ সুবিদিত (শাওয়াল, যিলকদ ও যিলহজ্জের প্রথম দশ দিন)»।",
            specialRulingsBn = listOf(
                "চারটি সম্মানিত মাসের অন্যতম; কোনো ধরণের কলহ-বিবাদ বা অন্যায় আচরণে লিপ্ত হওয়া কঠোরভাবে নিষিদ্ধ।",
                "হজ্জের সফর ও হজ্জের মাসায়েল শিক্ষা করা।"
            )
        ),
        IslamicMonthProfile(
            monthIndex = 11,
            nameEn = "Dhu al-Hijjah",
            nameAr = "ذو الحجة",
            nameBn = "যিলহজ্জ",
            isSacredMonth = true,
            meaningAndEtymologyBn = "হজ্জ পালন করার কারণে এই মাসের নাম রাখা হয়েছে যিলহজ্জ। এই মাসেই ইসলামের পঞ্চম রুকন পবিত্র হজ্জ পালিত হয়।",
            keySpiritualThemeBn = "বছরের শ্রেষ্ঠ ১০ দিন, আরাফাতের দিন, ঈদুল আযহার কুরবানী এবং তাওহীদের মহান স্মারক।",
            sacredMonthVerseBn = "সূরা আল-ফজর ৮৯:১-২ — «শপথ ভোরবেলার, এবং শপথ দশ রাতের (যিলহজ্জের প্রথম দশ দিন)»।",
            specialRulingsBn = listOf(
                "রাসূলুল্লাহ ﷺ বলেছেন: «যিলহজ্জের প্রথম দশ দিনের চেয়ে এমন কোনো দিন নেই যাতে নেক আমল আল্লাহর নিকট অধিক প্রিয়» (সহীহ আল-বুখারী ৯৬৯)।",
                "৯ই যিলহজ্জ আরাফাতের দিনের রোযা বিগত এক বছর ও আগামী এক বছরের গুনাহের কাফফারা (সহীহ মুসলিম ১১৬২)।",
                "১০ই যিলহজ্জ ঈদুল আযহা এবং ১১, ১২, ১৩ই যিলহজ্জ আইয়ামে তাশরীকে রোযা রাখা সম্পূর্ণ হারাম (সহীহ মুসলিম ১১৪১)।"
            )
        )
    )

    // Master Historical Events mapped to (MonthIndex to Map of Day to List<Events>)
    private val historicalEventsMap: Map<Pair<Int, Int>, List<IslamicHistoricalEvent>> = mapOf(
        // 1 Muharram
        Pair(0, 1) to listOf(
            IslamicHistoricalEvent(
                id = "hist_muharram_1",
                titleBn = "হিজরি ইসলামিক নববর্ষের সূচনা",
                dateSummaryBn = "১লা মুহাররম, হিজরি সন প্রবর্তন",
                yearDescriptionBn = "১৬-১৭ হিজরি (আমিরুল মুমিনীন উমর ফারুক রা.-এর খিলাফত)",
                certainty = HistoricalCertainty.HISTORICAL_ESTABLISHED,
                descriptionBn = "রাসূলুল্লাহ ﷺ-এর মক্কা থেকে মদিনায় ঐতিহাসিক হিজরতের বছরকে ভিত্তি করে হযরত উমর (রা.) সাহাবায়ে কেরামের সর্বসম্মত পরামর্শে হিজরি সন গণনা শুরু করেন এবং মুহাররম মাসকে বছরের প্রথম মাস নির্ধারণ করেন।",
                primarySourceBn = "সহীহ আল-বুখারী ৩৯৩৪ (তারীখুত তাবারী ২/৩৯০, আল-বিদায়াহ ওয়ান-নিহায়াহ ৭/৭৪)"
            )
        ),
        // 10 Muharram (Ashura)
        Pair(0, 10) to listOf(
            IslamicHistoricalEvent(
                id = "hist_ashura_musa",
                titleBn = "মুসা (আ.) ও বনী ইসরাঈলের ফেরাউন থেকে ঐতিহাসিক মুক্তি",
                dateSummaryBn = "১০ই মুহাররম (আশুরা দিবস)",
                yearDescriptionBn = "প্রাচীন কাল (নবী মুসা আ.-এর যুগ)",
                certainty = HistoricalCertainty.AUTHENTIC_HADITH,
                descriptionBn = "আল্লাহ তাআলা লোহিত সাগর দ্বিখণ্ডিত করে মুসা (আ.) ও বনী ইসরাঈলকে ফেরাউনের অত্যাচার থেকে উদ্ধার করেন এবং ফেরাউন ও তার বাহিনীকে সমুদ্রে ডুবিয়ে দেন। আল্লাহর কৃতজ্ঞতাস্বরূপ মুসা (আ.) এই দিনে রোযা রেখেছিলেন এবং রাসূলুল্লাহ ﷺ নিজেও এই দিনে রোযা রেখেছেন ও উম্মতকে রোযা রাখার নির্দেশ দিয়েছেন।",
                primarySourceBn = "সহীহ আল-বুখারী ২০০৪, সহীহ মুসলিম ১১৩০ (হাদিস: ইবনে আব্বাস রা.)"
            ),
            IslamicHistoricalEvent(
                id = "hist_karbala_husain",
                titleBn = "কারবালার ময়দানে হযরত হুসাইন (রা.)-এর শাহাদাত",
                dateSummaryBn = "১০ই মুহাররম, ৬১ হিজরি (শুক্রবার)",
                yearDescriptionBn = "৬১ হিজরি (১০ অক্টোবর ৬৮০ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.HISTORICAL_ESTABLISHED,
                descriptionBn = "রাসূলুল্লাহ ﷺ-এর দৌহিত্র হযরত হুসাইন ইবনে আলী (রা.) স্বৈরাচারী শাসনের বিরুদ্ধে সত্য ও ন্যায়ের পক্ষে অবস্থান নিয়ে কারবালার ময়দানে বীরোচিতভাবে সপরিবারে শাহাদাত বরণ করেন। এটি মুসলিম ইতিহাসের এক চরম বেদনাদায়ক ও শিক্ষণীয় ঘটনা।",
                primarySourceBn = "তারীখুত তাবারী ৫/৩৮৯, আল-বিদায়াহ ওয়ান-নিহায়াহ ৮/১৮৭ (ইবনে কাসীর)",
                scholarlyNoteBn = "আশুরার মূল শরয়ী তাৎপর্য ও রোযার বিধান মুসা (আ.)-এর মুক্তির সাথে সম্পৃক্ত, যা রাসূল ﷺ-এর সময় থেকেই প্রতিষ্ঠিত। আর কারবালার শাহাদাত এই দিনের বরকতময় মর্যাদাকে আরও সমুন্নত করেছে।"
            )
        ),
        // 12 Rabi al-Awwal (KEY FOCUS REQUESTED BY USER)
        Pair(2, 12) to listOf(
            IslamicHistoricalEvent(
                id = "hist_prophet_wafat",
                titleBn = "রাসূলুল্লাহ ﷺ-এর মহাপ্রয়াণ (ওফাত শরীফ)",
                dateSummaryBn = "১২ই রবিউল আউয়াল, ১১ হিজরি (সোমবার, দ্বিপ্রহর)",
                yearDescriptionBn = "১১ হিজরি (৮ জুন ৬৩২ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "রাসূলুল্লাহ ﷺ ৬৩ বছর বয়সে সোমবার চাশতের সময় উম্মুল মুমিনীন আয়েশা (রা.)-এর কোলে মাথা রেখে শেষ নিঃশ্বাস ত্যাগ করেন। তাঁর শেষ বাক্য ছিল: «بل الرفيق الأعلى» (বরং আমি রফীকুল আ'লা বা মহান বন্ধুর সান্নিধ্য চাই)। সাহাবায়ে কেরামের জন্য এটি ছিল পৃথিবীর সবচেয়ে অন্ধকার ও বেদনাবিধুর দিন।",
                primarySourceBn = "সহীহ আল-বুখারী ৪৪৪৯, সহীহ মুসলিম ৪১৮, আত-তবাকাাতুল কুবরা (ইবনে সা'দ) ২/২১৬",
                scholarlyNoteBn = "ঐতিহাসিক ও মুহাদ্দিসগণের সর্বসম্মত ঐকমত্য রয়েছে যে রাসূলুল্লাহ ﷺ-এর ওফাত ঘটেছিল রবিউল আউয়াল মাসের ১২ তারিখ সোমবার।"
            ),
            IslamicHistoricalEvent(
                id = "hist_prophet_hijrah_quba",
                titleBn = "মদিনায় আগমন ও ইসলামের প্রথম মসজিদ 'মসজিদে কুবা' প্রতিষ্ঠা",
                dateSummaryBn = "১২ই রবিউল আউয়াল, ১ হিজরি (সোমবার)",
                yearDescriptionBn = "১ হিজরি (২৪ সেপ্টেম্বর ৬২২ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.AUTHENTIC_HADITH,
                descriptionBn = "মক্কার দীর্ঘ নিপীড়ন শেষে রাসূলুল্লাহ ﷺ ও হযরত আবু বকর (রা.) হিজরতের কাফেলা নিয়ে ১২ই রবিউল আউয়াল সোমবার কুবা পল্লীতে পদার্পণ করেন। সেখানে তিনি ইসলামের প্রথম মসজিদ 'মসজিদে কুবা'-এর ভিত্তি স্থাপন করেন এবং চার দিন অবস্থান শেষে ইয়াসরিবে (মদিনা মুনাওয়ারা) প্রবেশ করেন।",
                primarySourceBn = "সহীহ আল-বুখারী ৩৯০৬, মুসনাদে আহমাদ, সীরাতে ইবনে হিশাম ১/৪৯৩"
            ),
            IslamicHistoricalEvent(
                id = "hist_prophet_birth_analysis",
                titleBn = "রাসূলুল্লাহ ﷺ-এর জন্মবার ও জন্মতারিখ সংক্রান্ত প্রামাণ্য গবেষণা",
                dateSummaryBn = "হস্তী বর্ষ (عام الفيل) / রবিউল আউয়াল মাস (সোমবার)",
                yearDescriptionBn = "৫৭০/৫৭১ খ্রিষ্টাব্দ (সাধারণ সন)",
                certainty = HistoricalCertainty.SCHOLARLY_DISCUSSION,
                descriptionBn = "রাসূলুল্লাহ ﷺ হস্তী বর্ষে সোমবারে জন্মগ্রহণ করেছিলেন — এটি সহীহ হাদীস দ্বারা সুনিশ্চিত (সহীহ মুসলিম ১১৬২: 'ঐ দিনে আমি জন্মগ্রহণ করেছি এবং ঐ দিনে আমার ওপর ওহী নাযিল হয়েছে')। তবে রবিউল আউয়ালের নির্দিষ্ট তারিখ নিয়ে প্রাচীন ঐতিহাসিক ও মুহাদ্দিসদের মধ্যে তিনটি প্রধান মতামত বিদ্যমান:\n" +
                        "১) ১২ই রবিউল আউয়াল: মুহাম্মদ ইবনে ইসহাক রচিত সীরাতে উল্লেখিত ও সর্বাধিক প্রচলিত অভিমত।\n" +
                        "২) ৮ই রবিউল আউয়াল: হযরত ইবনে আব্বাস (রা.) ও যুবায়ের ইবনে মুতইম (রা.)-এর বর্ণনা, যা ইমাম ইবনে আবদুল বার ও ইবনে হাযম অগ্রাধিকার দিয়েছেন।\n" +
                        "৩) ৯ই রবিউল আউয়াল: বিখ্যাত মিসরীয় জ্যোতির্বিজ্ঞানী মাহমুদ পাশা আল-ফালাকী এবং বিশ্বখ্যাত সীরাত গ্রন্থ 'আর-রাহীকুল মাখতুম'-এর প্রণেতা আল্লামা সফিউর রহমান মুবারকপুরী কর্তৃক গাণিতিক ও জ্যোতির্বৈজ্ঞানিক বিশ্লেষণে ২২ এপ্রিল ৫৭১ খ্রিষ্টাব্দ সোমবার হিসেবে সাব্যস্ত ও গ্রহণযোগ্য সিদ্ধান্ত।",
                primarySourceBn = "সহীহ মুসলিম ১১৬২, আল-বিদায়াহ ওয়ান-নিহায়াহ (ইবনে কাসীর) ২/২৬০, আর-রাহীকুল মাখতুম (পৃষ্ঠা ৫৪-৫৫)",
                scholarlyNoteBn = "ইসলামে জন্মদিনের আনুষ্ঠানিক উদযাপনের চেয়ে রাসূলুল্লাহ ﷺ-এর সুন্নাহ ও আদর্শের পূর্ণাঙ্গ অনুসরণই প্রকৃত নবীপ্রেম।"
            )
        ),
        // 27 Rajab (Al-Isra wal-Mi'raj)
        Pair(6, 27) to listOf(
            IslamicHistoricalEvent(
                id = "hist_isra_miraj",
                titleBn = "ঐতিহাসিক আল-ইসরা ও আল-মি'রাজ (ঊর্ধ্বাকাশ ভ্রমণ)",
                dateSummaryBn = "২৭শে রজব (প্রসিদ্ধ অভিমত), নবুওয়তের ১০ম-১১ম বর্ষ",
                yearDescriptionBn = "হিজরতের পূর্বে (মক্কী জীবন)",
                certainty = HistoricalCertainty.AUTHENTIC_HADITH,
                descriptionBn = "আল্লাহ তাআলা তাঁর প্রিয় হাবীব ﷺ-কে এক রাত্রিতে মসজিদে হারাম থেকে মসজিদে আকসা এবং সেখান থেকে সিদরাতুল মুনতাহা ও ঊর্ধ্বজগতে নিয়ে যান। এ রজনীতে উম্মতে মুহাম্মাদীর ওপর দৈনিক পাঁচ ওয়াক্ত সালাত ফরজ করা হয় এবং সূরা আল-বাকারার শেষ দুটি আয়াত উপহার দেওয়া হয়।",
                primarySourceBn = "সূরা আল-ইসরা ১৭:১, সূরা আন-নাজম ৫৩:১৩-১৮, সহীহ আল-বুখারী ৩৮৮৭, সহীহ মুসলিম ১৬২",
                scholarlyNoteBn = "মি'রাজ যে ঘটেছিল তা কুরআনের অকাট্য নাস দ্বারা প্রমাণিত। তবে এর সুনির্দিষ্ট তারিখ নিয়ে আলেমদের মতভেদ রয়েছে; অধিকাংশ মুসলিম ঐতিহাসিকভাবে রজবের ২৭ তারিখে এটি স্মরণ করে থাকেন।"
            )
        ),
        // 15 Sha'ban (Nisf Sha'ban)
        Pair(7, 15) to listOf(
            IslamicHistoricalEvent(
                id = "hist_nisf_shaban",
                titleBn = "শাবান মাসের মধ্যরজনী (নিসফ শাবান)",
                dateSummaryBn = "১৫ই শাবান দিবাগত রাত",
                yearDescriptionBn = "প্রতি বছর শাবান মাসে উদযাপিত",
                certainty = HistoricalCertainty.AUTHENTIC_HADITH,
                descriptionBn = "রাসূলুল্লাহ ﷺ ইরশাদ করেছেন: «আল্লাহ তাআলা মধ্য শাবানের রাতে তাঁর বান্দাদের প্রতি বিশেষ দৃষ্টি দেন এবং মুশরিক ও অন্তরে বিদ্বেষ পোষণকারী ব্যতীত সকল সৃষ্টিকে ক্ষমা করে দেন»।",
                primarySourceBn = "সুনান ইবনে মাজাহ ১৩৯০, সহীহ ইবনে হিব্বান ৫৬৬৫ (শায়খ আলবানী সিলসিলাতুল আহাদীসিস সহীহাহ ১১৪৪-এ সহীহ বলেছেন)",
                scholarlyNoteBn = "এ রাতে ব্যক্তিগতভাবে ইস্তিগফার ও ইবাদত করা মুস্তাহাব। তবে কোনো ধরণের সম্মিলিত রসম-রেওয়াজ বা বিদ'আতি আনুষ্ঠানিকতা সুন্নাহ পরিপন্থী।"
            )
        ),
        // 17 Ramadan (Battle of Badr)
        Pair(8, 17) to listOf(
            IslamicHistoricalEvent(
                id = "hist_ghazwa_badr",
                titleBn = "ঐতিহাসিক বদর যুদ্ধ (ইয়াওমুল ফুরকান)",
                dateSummaryBn = "১৭ই রমাদান, ২ হিজরি (শুক্রবার)",
                yearDescriptionBn = "২ হিজরি (১৩ মার্চ ৬২৪ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "সত্য ও মিথ্যার পার্থক্যকারী মহাগ্রন্থ আল-কুরআনে আখ্যায়িত 'ইয়াওমুল ফুরকান'। মাত্র ৩১৩ জন নিরস্ত্র প্রায় মুসলিম সাহাবী আবু জাহেলের নেতৃত্বে সুসজ্জিত এক হাজার কুরাইশ কাফের বাহিনীর বিরুদ্ধে আল্লাহর অলৌকিক নুসরত ও ফেরেশতাদের সাহায্যে ঐতিহাসিক মহাবিজয় অর্জন করেন।",
                primarySourceBn = "সূরা আল-আনফাল ৮:৪১, সূরা আলে ইমরান ৩:১২৩, সহীহ আল-বুখারী ৩৯৫৩"
            )
        ),
        // 20 Ramadan (Conquest of Makkah)
        Pair(8, 20) to listOf(
            IslamicHistoricalEvent(
                id = "hist_fath_makkah",
                titleBn = "মক্কা বিজয় (ফাতহে মক্কা)",
                dateSummaryBn = "২০শে রমাদান, ৮ হিজরি",
                yearDescriptionBn = "৮ হিজরি (১১ জানুয়ারি ৬৩০ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "রাসূলুল্লাহ ﷺ দশ হাজার সাহাবায়ে কেরামকে সাথে নিয়ে সম্পূর্ণ রক্তপাতহীন ও শান্তিময় উপায়ে পবিত্র মক্কা নগরী জয় করেন। তিনি বিনম্রচিত্তে কাবা ঘরে প্রবেশ করে ৩৬০টি মূর্তি অপসারণ করেন এবং পাঠ করেন: «جَاءَ الْحَقُّ وَزَهَقَ الْبَاطِلُ» (সত্য সমাগত এবং মিথ্যা বিলুপ্ত হয়েছে)। অতপর মক্কাবাসীদের সাধারণ ক্ষমা ঘোষণা করেন।",
                primarySourceBn = "সূরা আল-ইসরা ১৭:৮১, সূরা আন-নাসর ১১০:১-৩, সহীহ আল-বুখারী ৪২৮৭"
            )
        ),
        // 21, 23, 25, 27, 29 Ramadan (Laylatul Qadr nights)
        Pair(8, 21) to listOf(createLaylatulQadrEvent(21)),
        Pair(8, 23) to listOf(createLaylatulQadrEvent(23)),
        Pair(8, 25) to listOf(createLaylatulQadrEvent(25)),
        Pair(8, 27) to listOf(createLaylatulQadrEvent(27)),
        Pair(8, 29) to listOf(createLaylatulQadrEvent(29)),

        // 1 Shawwal (Eid al-Fitr)
        Pair(9, 1) to listOf(
            IslamicHistoricalEvent(
                id = "hist_eid_fitr",
                titleBn = "ঈদুল ফিতর (সিয়াম সমাপনের আনন্দ উৎসব)",
                dateSummaryBn = "১লা শাওয়াল (প্রতি বছর)",
                yearDescriptionBn = "২ হিজরি থেকে অদ্যাবধি পালিত",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "আল্লাহ তাআলা পুরো এক মাস সিয়াম সাধনা ও তারাবীহ সফলভাবে সমাপনের কৃতজ্ঞতাস্বরূপ উম্মতে মুহাম্মাদীর জন্য এই খুশির দিন নির্ধারণ করেছেন। এ দিনে রোযা রাখা হারাম এবং ঈদের নামাজের পূর্বে দরিদ্রের ঘরে সাদাকাতুল ফিতর পৌঁছে দেওয়া ওয়াজিব।",
                primarySourceBn = "সহীহ আল-বুখারী ৯৫৬, সহীহ মুসলিম ১১৩৮"
            )
        ),
        // 15 Shawwal (Battle of Uhud)
        Pair(9, 15) to listOf(
            IslamicHistoricalEvent(
                id = "hist_ghazwa_uhud",
                titleBn = "ঐতিহাসিক ওহুদের যুদ্ধ",
                dateSummaryBn = "১৫ই শাওয়াল, ৩ হিজরি (শনিবার)",
                yearDescriptionBn = "৩ হিজরি (২৩ মার্চ ৬২৫ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.AUTHENTIC_HADITH,
                descriptionBn = "মুসলিম বাহিনী ও মক্কার কুরাইশদের মধ্যে মদিনার উপকণ্ঠে ওহুদ পাহাড়ের পাদদেশে সংঘটিত যুদ্ধ। তীরন্দাজ দলের ভুলের কারণে প্রাথমিক বিজয়ের পর এক কঠিন পরীক্ষার সম্মুখীন হতে হয়। এতে সাইয়্যিদুশ শুহাদা হযরত হামযাহ (রা.) সহ ৭০ জন শ্রেষ্ঠ সাহাবী শাহাদাত বরণ করেন।",
                primarySourceBn = "সূরা আলে ইমরান ৩:১২১-১৭৫, সহীহ আল-বুখারী ৪০৪৩"
            )
        ),
        // 1 Dhu al-Qa'dah (Treaty of Hudaybiyyah)
        Pair(10, 1) to listOf(
            IslamicHistoricalEvent(
                id = "hist_sulh_hudaybiyyah",
                titleBn = "হুদায়বিয়ার ঐতিহাসিক সন্ধি",
                dateSummaryBn = "যিলকদ মাস, ৬ হিজরি",
                yearDescriptionBn = "৬ হিজরি (মার্চ ৬২৮ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "রাসূলুল্লাহ ﷺ ও ১৪০০ সাহাবী উমরার উদ্দেশ্যে রওয়ানা হয়ে হুদায়বিয়া নামক স্থানে কাফেরদের বাধার মুখে পড়েন। সেখানে সংঘটিত হয় 'বাইআতুর রিদওয়ান' এবং স্বাক্ষরিত হয় ঐতিহাসিক দশ বছরের শান্তিচুক্তি, যাকে মহান আল্লাহ 'ফাতহান মুবীনা' (সুস্পষ্ট বিজয়) হিসেবে ঘোষণা করেন।",
                primarySourceBn = "সূরা আল-ফাতহ ৪৮:১-১৮, সহীহ আল-বুখারী ২৭৩১"
            )
        ),
        // 8 Dhu al-Hijjah (Yawm al-Tarwiyah)
        Pair(11, 8) to listOf(
            IslamicHistoricalEvent(
                id = "hist_yawm_tarwiyah",
                titleBn = "ইয়াওমুত তারবিয়াহ (মিনা অভিমুখে যাত্রা)",
                dateSummaryBn = "৮ই যিলহজ্জ (হজ্জের আনুষ্ঠানিক সূচনা)",
                yearDescriptionBn = "প্রতি বছর হজ্জের সময়",
                certainty = HistoricalCertainty.AUTHENTIC_HADITH,
                descriptionBn = "হাজ্বীগণ মক্কার হারাম শরীফ থেকে ইহরাম বেঁধে তালবিয়া পাঠ করতে করতে মিনায় রওয়ানা হন এবং সেখানে যোহর, আসর, মাগরিব, ইশা ও পরদিনের ফজর নামাজ আদায় করেন।",
                primarySourceBn = "সহীহ আল-বুখারী ১৬৫১, সহীহ মুসলিম ১২১৮"
            )
        ),
        // 9 Dhu al-Hijjah (Day of Arafah & Farewell Sermon)
        Pair(11, 9) to listOf(
            IslamicHistoricalEvent(
                id = "hist_day_arafah",
                titleBn = "ঐতিহাসিক আরাফাত দিবস ও বিদায় হজ্জের ভাষণ",
                dateSummaryBn = "৯ই যিলহজ্জ, ১০ হিজরি (শুক্রবার)",
                yearDescriptionBn = "১০ হিজরি (৬ মার্চ ৬৩২ খ্রিষ্টাব্দ)",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "হজ্জের প্রধান রুকন উকুফে আরাফাত। লক্ষাধিক সাহাবীর সমাবেশে রাসূলুল্লাহ ﷺ তাঁর ঐতিহাসিক 'বিদায় হজ্জের ভাষণ' প্রদান করেন যাতে মানবজাতির মৌলিক অধিকার, রক্তের পবিত্রতা, নারীর মর্যাদা ও সুদমুক্ত অর্থব্যবস্থার চূড়ান্ত ঘোষণা দেওয়া হয়। এদিন নাযিল হয় দ্বীনের পূর্ণাঙ্গতার ঐতিহাসিক আয়াত (সূরা মায়িদাহ ৫:৩)।",
                primarySourceBn = "সূরা আল-মায়েদাহ ৫:৩, সহীহ মুসলিম ১২১৮ (জাবের রা. বর্ণিত দীর্ঘ হাদীস)"
            )
        ),
        // 10 Dhu al-Hijjah (Eid al-Adha)
        Pair(11, 10) to listOf(
            IslamicHistoricalEvent(
                id = "hist_eid_adha",
                titleBn = "ঈদুল আযহা ও ইবরাহীম (আ.)-এর মহান কুরবানী",
                dateSummaryBn = "১০ই যিলহজ্জ (ইয়াওমুন নাহার)",
                yearDescriptionBn = "প্রতি বছর পালিত",
                certainty = HistoricalCertainty.UNANIMOUS_CONSENSUS,
                descriptionBn = "হযরত ইবরাহীম (আ.) আল্লাহর আদেশে তাঁর পুত্র হযরত ইসমাঈল (আ.)-কে কুরবানী করার চরম পরীক্ষায় উত্তীর্ণ হওয়ার স্মারক। এদিন জামরাতুল আকাবায় কঙ্কর নিক্ষেপ, আল্লাহর সন্তুষ্টিতে পশু কুরবানী ও তাওয়াফে যিয়ারত সম্পন্ন করা হয়।",
                primarySourceBn = "সূরা আস-সাফফাত ৩৭:১০২-১০৭, সহীহ আল-বুখারী ৫৫৪৫"
            )
        ),
        // 11-13 Dhu al-Hijjah (Ayyam al-Tashreeq)
        Pair(11, 11) to listOf(createTashreeqEvent(11)),
        Pair(11, 12) to listOf(createTashreeqEvent(12)),
        Pair(11, 13) to listOf(createTashreeqEvent(13))
    )

    private fun createLaylatulQadrEvent(nightNum: Int): IslamicHistoricalEvent {
        return IslamicHistoricalEvent(
            id = "hist_qadr_$nightNum",
            titleBn = "লাইলাতুল কদরের সম্ভাব্য মহিমান্বিত বিজোড় রজনী ($nightNum রমাদান)",
            dateSummaryBn = "${nightNum}শে রমাদানের দিবাগত রাত",
            yearDescriptionBn = "রমাদানের শেষ দশক",
            certainty = HistoricalCertainty.AUTHENTIC_HADITH,
            descriptionBn = "রাসূলুল্লাহ ﷺ বলেছেন: «তোমরা রমাদানের শেষ দশকের বিজোড় রাতগুলোতে লাইলাতুল কদর অন্বেষণ করো»। এ রাত সহস্র মাসের চেয়েও শ্রেষ্ঠ, যাতে কুরআন নাযিল হয়েছে এবং ফেরেশতারা আল্লাহর রহমত ও শান্তি নিয়ে ধরণীতে অবতরণ করেন।",
            primarySourceBn = "সূরা আল-কদর ৯৭:১-৫, সহীহ আল-বুখারী ২০২০, সহীহ মুসলিম ১১৬৯"
        )
    }

    private fun createTashreeqEvent(dayNum: Int): IslamicHistoricalEvent {
        return IslamicHistoricalEvent(
            id = "hist_tashreeq_$dayNum",
            titleBn = "আইয়ামে তাশরীক ($dayNum যিলহজ্জ)",
            dateSummaryBn = "${dayNum}ই যিলহজ্জ (কুরবানীর দিনসমূহ)",
            yearDescriptionBn = "১০-১৩ই যিলহজ্জ",
            certainty = HistoricalCertainty.AUTHENTIC_HADITH,
            descriptionBn = "রাসূলুল্লাহ ﷺ ইরশাদ করেছেন: «আইয়ামে তাশরীক হলো পানাহার এবং আল্লাহর স্মরণের দিন»। এ দিনগুলোতে তাকবীরে তাশরীক পাঠ করা ওয়াজিব এবং রোযা রাখা সম্পূর্ণ হারাম।",
            primarySourceBn = "সহীহ মুসলিম ১১৪১ (হাদিস: নুবাইশাহ আল-হুজালী রা.)"
        )
    }

    /**
     * Compute Fasting Category and Ruling for a specific Islamic Date
     */
    fun getFastingStatus(
        hijriDay: Int,
        hijriMonthIndex: Int,
        dayOfWeek: Int // Calendar.SUNDAY = 1, Calendar.MONDAY = 2, ...
    ): Triple<FastingCategory, String, String> {
        // 1. Prohibited (Haram) Fasting Days:
        // Eid al-Fitr (1 Shawwal)
        if (hijriMonthIndex == 9 && hijriDay == 1) {
            return Triple(
                FastingCategory.FORBIDDEN,
                "আজ ঈদুল ফিতর। এ দিনে রোযা রাখা সর্বসম্মতিক্রমে হারাম ও কঠোরভাবে নিষিদ্ধ। আজ আনন্দ ও আল্লাহর নিয়ামতের শোকর আদায়ের দিন।",
                "সহীহ আল-বুখারী ১৯৯১, সহীহ মুসলিম ১১৩৮"
            )
        }
        // Eid al-Adha (10 Dhul Hijjah)
        if (hijriMonthIndex == 11 && hijriDay == 10) {
            return Triple(
                FastingCategory.FORBIDDEN,
                "আজ ঈদুল আযহা (কুরবানীর দিন)। এ দিনে রোযা রাখা হারাম ও নিষিদ্ধ। আজ আল্লাহর পক্ষ থেকে আতিথেয়তা ও পানাহারের দিন।",
                "সহীহ আল-বুখারী ১৯৯৩, সহীহ মুসলিম ১১৪০"
            )
        }
        // Ayyam al-Tashreeq (11, 12, 13 Dhul Hijjah)
        if (hijriMonthIndex == 11 && hijriDay in 11..13) {
            return Triple(
                FastingCategory.FORBIDDEN,
                "আজ আইয়ামে তাশরীকের দিন। রাসূলুল্লাহ ﷺ বলেছেন: «এগুলো পানাহার ও আল্লাহর যিকিরের দিন; এ দিনগুলোতে রোযা রাখা হারাম»।",
                "সহীহ মুসলিম ১১৪১"
            )
        }

        // 2. Obligatory Fasting Days:
        // Ramadan (all days)
        if (hijriMonthIndex == 8) {
            return Triple(
                FastingCategory.OBLIGATORY,
                "আজ রমাদানের পবিত্র ফরজ রোযা। সুস্থ ও মুকিম প্রাপ্তবয়স্ক প্রত্যেক মুসলিমের ওপর এ রোযা রাখা ইসলামের মৌলিক পঞ্চস্তম্ভের অন্যতম।",
                "সূরা আল-বাকারাহ ২:১৮৫, সহীহ আল-বুখারী ১৯০৪"
            )
        }

        // 3. High Sunnah Fasting Days:
        // Ashura (9 and 10 Muharram)
        if (hijriMonthIndex == 0 && (hijriDay == 9 || hijriDay == 10)) {
            val title = if (hijriDay == 10) "আশুরা (১০ই মুহাররম)" else "তাসূআ (৯ই মুহাররম)"
            return Triple(
                FastingCategory.SUNNAH_HIGH,
                "আজ $title-এর সুন্নাত রোযা। রাসূলুল্লাহ ﷺ বলেছেন: «আমি আল্লাহর কাছে আশা করি আশুরার রোযা পূর্ববর্তী এক বছরের গুনাহ মোচন করে দেবে»। ইহুদিদের ব্যতিক্রম করতে ৯ ও ১০ই মুহাররম রোযা রাখা মোস্তাহাব।",
                "সহীহ মুসলিম ১১৩৪, সহীহ আল-বুখারী ২০০৪"
            )
        }

        // Day of Arafah (9 Dhul Hijjah)
        if (hijriMonthIndex == 11 && hijriDay == 9) {
            return Triple(
                FastingCategory.SUNNAH_HIGH,
                "আজ আরাফাত দিবসের সুন্নাত রোযা (হাজ্বী ব্যতীত সাধারণ মুসলিমদের জন্য)। রাসূলুল্লাহ ﷺ বলেছেন: «আরাফার দিনের রোযা বিগত এক বছর ও আগামী এক বছরের গুনাহের কাফফারা হয়»।",
                "সহীহ মুসলিম ১১৬২"
            )
        }

        // 4. Ayyam al-Beed (13, 14, 15 of every month, except Tashreeq in Dhul Hijjah which was handled above)
        if (hijriDay in 13..15) {
            return Triple(
                FastingCategory.AYYAM_AL_BEED,
                "আজ আইয়ামে বীজের (চন্দ্রালোকিত দিন) রোযা। প্রতি হিজরি মাসের ১৩, ১৪ ও ১৫ তারিখে রোযা রাখা সারা বছর রোযা রাখার সমান সওয়াব বয়ে আনে।",
                "সহীহ আল-বুখারী ১৯৮১, সহীহ মুসলিম ১১৫৯"
            )
        }

        // 5. Monday and Thursday Sunnah Fasts
        if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.THURSDAY) {
            val dayName = if (dayOfWeek == Calendar.MONDAY) "সোমবার" else "বৃহস্পতিবার"
            return Triple(
                FastingCategory.SUNNAH_RECOMMENDED,
                "আজ $dayName-এর সুন্নাত রোযা। রাসূলুল্লাহ ﷺ বলেছেন: «সোম ও বৃহস্পতিবার আল্লাহর দরবারে বান্দার আমল পেশ করা হয়; আর আমি চাই রোযাদার অবস্থায় আমার আমল পেশ করা হোক»।",
                "জামে আত-তিরমিযী ৭৪৭, সহীহ মুসলিম ১১৬২"
            )
        }

        // 6. First 9 Days of Dhul Hijjah
        if (hijriMonthIndex == 11 && hijriDay in 1..8) {
            return Triple(
                FastingCategory.SUNNAH_RECOMMENDED,
                "আজ যিলহজ্জের প্রথম দশকের অন্যতম বরকতময় দিন। এ দিনগুলোতে নফল রোযা রাখা অত্যন্ত ফজিলতপূর্ণ ও প্রিয় আমল।",
                "সুনান আবু দাউদ ২৪৩৭, সহীহ আল-বুখারী ৯৬৯"
            )
        }

        // 7. Six Fasts of Shawwal
        if (hijriMonthIndex == 9 && hijriDay in 2..30) {
            return Triple(
                FastingCategory.SUNNAH_RECOMMENDED,
                "শাওয়াল মাসের বরকতময় নফল রোযার সুযোগ। যে ব্যক্তি রমাদানের পর শাওয়ালের যেকোনো ৬টি রোযা রাখবে, সে সারা বছর রোযা রাখার সওয়াব লাভ করবে।",
                "সহীহ মুসলিম ১১৬৪"
            )
        }

        // 8. General Permissible
        return Triple(
            FastingCategory.NONE_REGULAR,
            "আজ সাধারণ দিন। ইচ্ছা করলে নফল রোযা রাখা বা কাযা রোযা আদায় করা সম্পূর্ণ উত্তম ও জায়েয।",
            "সহীহ আল-বুখারী ১৯৭৩"
        )
    }

    /**
     * Calculate Lunar Phase Info accurately for a given lunar day (1..30)
     */
    fun getLunarPhaseInfo(lunarDay: Int): LunarPhaseInfo {
        val clampedDay = lunarDay.coerceIn(1, 30)
        return when (clampedDay) {
            1 -> LunarPhaseInfo(
                lunarDay = 1,
                phaseNameBn = "হিলাল / নতুন উদিত চাঁদ",
                phaseNameEn = "New Crescent (Hilal)",
                phaseNameAr = "الهلال الجديد",
                illuminationPercent = 2,
                moonAgeDays = 1.0,
                iconEmoji = "🌙",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "নতুন হিজরি মাসের সূচনা। পশ্চিমাকাশে নতুন চাঁদ দর্শনে সুন্নাত দু'আ পাঠ করা হয়।"
            )
            2, 3 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "ক্রমবর্ধমান চিকন চাঁদ",
                phaseNameEn = "Waxing Crescent",
                phaseNameAr = "الهلال المتزايد",
                illuminationPercent = clampedDay * 5,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌙",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "হিজরি মাসের প্রথমভাগের প্রারম্ভিক ইবাদত ও মাসব্যাপী নেক কাজের সূচনা।"
            )
            4, 5, 6 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "ক্রমবর্ধমান অর্ধচন্দ্রাকার",
                phaseNameEn = "Waxing Crescent",
                phaseNameAr = "الهلال",
                illuminationPercent = clampedDay * 7,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌛",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "চাঁদের আলো ও বিস্তার প্রতিদিন বৃদ্ধি পেতে থাকে।"
            )
            7, 8 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "প্রথম চতুর্থাংশ (অর্ধচন্দ্র)",
                phaseNameEn = "First Quarter",
                phaseNameAr = "التربيع الأول",
                illuminationPercent = 50,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌓",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "চাঁদের ঠিক অর্ধেক অংশ দৃশ্যমান। হিজরি মাসের প্রথম সপ্তাহ অতিক্রান্ত।"
            )
            9, 10, 11, 12 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "উজ্জ্বল কুঁজো চাঁদ",
                phaseNameEn = "Waxing Gibbous",
                phaseNameAr = "الأحدب المتزايد",
                illuminationPercent = 55 + (clampedDay - 8) * 8,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌔",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "পূর্ণিমার সন্নিকটে; আইয়ামে বীজের রোযার জন্য মানসিক প্রস্তুতি।"
            )
            13, 14, 15 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "বদর / পূর্ণিমা (আইয়ামে বীজ)",
                phaseNameEn = "Full Moon (Ayyam al-Beed)",
                phaseNameAr = "البدر الكامل",
                illuminationPercent = if (clampedDay == 14) 100 else 96,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌕",
                isPeakAyyamAlBeed = true,
                shariahSignificanceBn = "পূর্ণিমার আলোকময় রাত। সুন্নাত মুতাবিক ১৩, ১৪ ও ১৫ তারিখে আইয়ামে বীজের রোযা রাখা সারা বছর রোযার সমতুল্য।"
            )
            16, 17, 18 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "ক্ষীয়মাণ কুঁজো চাঁদ",
                phaseNameEn = "Waning Gibbous",
                phaseNameAr = "الأحدب المتناقص",
                illuminationPercent = 88 - (clampedDay - 16) * 10,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌖",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "পূর্ণিমার পর চাঁদের আলো ধীরে ধীরে হ্রাস পেতে থাকে।"
            )
            19, 20, 21 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "শেষ চতুর্থাংশ (অর্ধচন্দ্র)",
                phaseNameEn = "Third Quarter",
                phaseNameAr = "التربيع الثاني",
                illuminationPercent = 50,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌗",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "হিজরি মাসের শেষ দশকের সূচনা। রমাদানের ক্ষেত্রে এ সময়ে লাইলাতুল কদরের অনুসন্ধান শুরু হয়।"
            )
            22, 23, 24, 25, 26, 27 -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "ক্ষীয়মাণ বাঁকা চাঁদ",
                phaseNameEn = "Waning Crescent",
                phaseNameAr = "الهلال المتناقص",
                illuminationPercent = (28 - clampedDay) * 5,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌘",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "ভোররাতে পূর্বাকাশে বাঁকা চাঁদ দৃষ্টিগোচর হয়; তাহাজ্জুদ ও সাহরির উপযুক্ত সময়।"
            )
            else -> LunarPhaseInfo(
                lunarDay = clampedDay,
                phaseNameBn = "মুহাক / অদৃশ্য চাঁদ (অমাবস্যা)",
                phaseNameEn = "Dark Moon (New Moon Conjunction)",
                phaseNameAr = "المحاق",
                illuminationPercent = 0,
                moonAgeDays = clampedDay.toDouble(),
                iconEmoji = "🌑",
                isPeakAyyamAlBeed = false,
                shariahSignificanceBn = "চাঁদ ও সূর্যের সংযোগস্থল (কনজাংশন)। ২৯শে সন্ধ্যায় পশ্চিমাকাশে নতুন চাঁদের অনুসন্ধান (রু'ইয়াতুল হিলাল) করা সুন্নাত।"
            )
        }
    }

    /**
     * Recommended Islamic Worship for a specific Day Context
     */
    fun getRecommendedWorship(
        hijriDay: Int,
        hijriMonthIndex: Int,
        dayOfWeek: Int
    ): List<IslamicWorshipAction> {
        val actions = mutableListOf<IslamicWorshipAction>()

        // 1. Friday Special Actions
        if (dayOfWeek == Calendar.FRIDAY) {
            actions.add(
                IslamicWorshipAction(
                    titleBn = "সূরা আল-কাহাফ তেলাওয়াত",
                    categoryBn = "জুমু'আহ স্পেশাল সুন্নাত",
                    instructionBn = "জুমু'আর দিন (বৃহস্পতিবার সূর্যাস্ত থেকে শুক্রবার সূর্যাস্ত পর্যন্ত) সম্পূর্ণ সূরা আল-কাহাফ পাঠ করা।",
                    referenceBn = "সুনান আল-কুবরা লিল-বায়হাকী ৫৯৯৬, সহীহ আত-তারগীব ৭৩৬",
                    virtueBn = "যে ব্যক্তি জুমু'আর দিন সূরা আল-কাহাফ তেলাওয়াত করবে, তার জন্য এক জুমু'আহ থেকে পরবর্তী জুমু'আহ পর্যন্ত বিশেষ নূর চমকাতে থাকবে।"
                )
            )
            actions.add(
                IslamicWorshipAction(
                    titleBn = "রাসূলুল্লাহ ﷺ-এর ওপর অধিক দরূদ পাঠ",
                    categoryBn = "জুমু'আহ সুন্নাত",
                    instructionBn = "দিনে ও রাতে বেশি বেশি দরূদে ইবরাহীম পাঠ করা।",
                    referenceBn = "সুনান আবু দাউদ ১০৪৭, সুনান আন-নাসায়ী ১৩৭৪",
                    virtueBn = "রাসূলুল্লাহ ﷺ বলেছেন: «তোমাদের দিনসমূহের মধ্যে সর্বোত্তম দিন হলো জুমু'আর দিন... অতএব এ দিনে তোমরা আমার ওপর বেশি বেশি দরূদ পাঠ করো; কারণ তোমাদের দরূদ আমার সামনে পেশ করা হয়»।"
                )
            )
            actions.add(
                IslamicWorshipAction(
                    titleBn = "দো'আ কবুলের বিশেষ মুহূর্ত অনুসন্ধান (সা'আতুল ইজাবাহ)",
                    categoryBn = "জুমু'আহ সুন্নাত",
                    instructionBn = "বিশেষ করে আসরের পর থেকে মাগরিব পর্যন্ত কায়মনোবাক্যে একান্তে দো'আ করা।",
                    referenceBn = "সহীহ আল-বুখারী ৯৩৫, সুনান আবু দাউদ ১০৪৮",
                    virtueBn = "জুমু'আর দিনে এমন একটি মুহূর্ত রয়েছে, যে সময়ে কোনো মুসলিম বান্দা আল্লাহর নিকট কল্যাণ প্রার্থনা করলে আল্লাহ তা অবশ্যই দান করেন।"
                )
            )
        }

        // 2. Fasting Actions if Sunnah/Obligatory
        if (hijriMonthIndex == 8) {
            // Ramadan
            actions.add(
                IslamicWorshipAction(
                    titleBn = "সিয়াম ও কিয়ামুল লাইল (তারাবীহ)",
                    categoryBn = "রমাদানের প্রধান আমল",
                    instructionBn = "দিনের বেলায় রোযা পূর্ণ করা এবং রাতে ইখলাসের সাথে তারাবীহ ও তাহাজ্জুদ আদায় করা।",
                    referenceBn = "সহীহ আল-বুখারী ২০০৯",
                    virtueBn = "«যে ব্যক্তি ঈমান ও সাওয়াবের আশায় রমাদানে কিয়ামুল লাইল করবে, তার অতীতের সমস্ত গুনাহ ক্ষমা করে দেওয়া হবে»।"
                )
            )
            actions.add(
                IslamicWorshipAction(
                    titleBn = "কুরআন খতম ও গভীর তাদাব্বুর",
                    categoryBn = "রমাদান আমল",
                    instructionBn = "প্রতিদিন লক্ষ্য নির্ধারণ করে কুরআনের অর্থ ও তাফসীরসহ পাঠ করা।",
                    referenceBn = "সহীহ আল-বুখারী ৬",
                    virtueBn = "জিবরাঈল (আ.) রমাদানের প্রতি রাতে রাসূলুল্লাহ ﷺ-এর সাথে কুরআনের দাওর (পরস্পর পাঠ) করতেন।"
                )
            )
        } else if (hijriDay in 13..15 && hijriMonthIndex != 11) {
            // Ayyam al-Beed
            actions.add(
                IslamicWorshipAction(
                    titleBn = "আইয়ামে বীজের নফল রোযা",
                    categoryBn = "মাসিক সুন্নাত",
                    instructionBn = "১৩, ১৪ ও ১৫ তারিখে রোযা রাখার নিয়ত করা এবং শেষ রাতে সাহরি গ্রহণ করা।",
                    referenceBn = "সহীহ আল-বুখারী ১৯৮১",
                    virtueBn = "রাসূলুল্লাহ ﷺ হযরত আবু হুরায়রা (রা.)-কে তিনটি বিষয়ে অসিয়ত করেছিলেন, তন্মধ্যে অন্যতম হলো প্রতি মাসে তিন দিন রোযা রাখা।"
                )
            )
        } else if (dayOfWeek == Calendar.MONDAY || dayOfWeek == Calendar.THURSDAY) {
            // Monday/Thursday
            actions.add(
                IslamicWorshipAction(
                    titleBn = "সোম/বৃহস্পতিবারের সুন্নাত সিয়াম",
                    categoryBn = "সাপ্তাহিক সুন্নাত",
                    instructionBn = "আমল পেশ হওয়ার দিনে সিয়াম পালন করে আল্লাহর নৈকট্য অর্জন করা।",
                    referenceBn = "জামে আত-তিরমিযী ৭৪৭",
                    virtueBn = "রাসূলুল্লাহ ﷺ সোম ও বৃহস্পতিবার রোযা রাখতে ভালোবাসতেন।"
                )
            )
        }

        // 3. Daily Continuous Core Worship (Always present)
        actions.add(
            IslamicWorshipAction(
                titleBn = "ফরজ সালাত ও সুন্নাত সালাতে যত্নশীলতা",
                categoryBn = "প্রতিদিনের ভিত্তি",
                instructionBn = "ওয়াক্তমতো পাঁচ ওয়াক্ত সালাত জামাতে আদায় করা এবং ১২ রাকাত সুন্নতে মুআক্কাদাহ পূর্ণ করা।",
                referenceBn = "সহীহ মুসলিম ৭২৮",
                virtueBn = "যে ব্যক্তি দিনে-রাতে ১২ রাকাত সুন্নতে মুআক্কাদাহ আদায় করবে, তার জন্য জান্নাতে একটি প্রাসাদ নির্মাণ করা হবে।"
            )
        )
        actions.add(
            IslamicWorshipAction(
                titleBn = "সকাল-সন্ধ্যার প্রামাণ্য মাসনূন আযকার",
                categoryBn = "দৈনিক নিরাপত্তা",
                instructionBn = "ফজরের পর ও আসরের পর আয়াতুল কুরসী, তিন কুল ও সাইয়্যিদুল ইস্তিগফার নিয়মিত পাঠ করা।",
                referenceBn = "সহীহ আল-বুখারী ৬৩০৬",
                virtueBn = "সারাদিনের বালা-মুসিবত ও শয়তানের অনিষ্ট থেকে হেফাজত এবং অপরিসীম মানসিক প্রশান্তি।"
            )
        )

        return actions
    }

    /**
     * Builds complete IslamicDayContext for any selected date
     */
    fun buildDayContext(
        hijriDay: Int,
        hijriMonthIndex: Int,
        hijriYear: Int,
        gregorianCal: Calendar,
        isToday: Boolean = false
    ): IslamicDayContext {
        val monthProfile = islamicMonths.getOrElse(hijriMonthIndex) { islamicMonths[0] }
        val dayOfWeek = gregorianCal.get(Calendar.DAY_OF_WEEK)
        val weekdayBn = CalendarHelper.englishDaysBn[dayOfWeek] ?: "বুধবার"

        val gregDay = gregorianCal.get(Calendar.DAY_OF_MONTH)
        val gregMonth = CalendarHelper.englishMonthsBn.getOrElse(gregorianCal.get(Calendar.MONTH)) { "" }
        val gregYear = gregorianCal.get(Calendar.YEAR)

        val bengaliInfo = CalendarHelper.getBengaliDateDetail(gregorianCal)

        val fasting = getFastingStatus(hijriDay, hijriMonthIndex, dayOfWeek)
        val lunar = getLunarPhaseInfo(hijriDay)
        val events = historicalEventsMap[Pair(hijriMonthIndex, hijriDay)] ?: emptyList()
        val worship = getRecommendedWorship(hijriDay, hijriMonthIndex, dayOfWeek)

        return IslamicDayContext(
            hijriDay = hijriDay,
            hijriMonthIndex = hijriMonthIndex,
            hijriMonthNameBn = monthProfile.nameBn,
            hijriMonthNameAr = monthProfile.nameAr,
            hijriMonthNameEn = monthProfile.nameEn,
            hijriYear = hijriYear,
            gregorianDay = gregDay,
            gregorianMonthNameBn = gregMonth,
            gregorianYear = gregYear,
            gregorianWeekdayBn = weekdayBn,
            bengaliDayBn = CalendarHelper.toBanglaNumber(bengaliInfo.day),
            bengaliMonthBn = bengaliInfo.monthName,
            bengaliYearBn = CalendarHelper.toBanglaNumber(bengaliInfo.year),
            bengaliSeasonBn = bengaliInfo.season,
            isToday = isToday,
            fastingStatus = fasting.first,
            fastingRulingDetailsBn = fasting.second,
            fastingReferenceBn = fasting.third,
            lunarInfo = lunar,
            historicalEvents = events,
            recommendedWorship = worship,
            monthProfile = monthProfile
        )
    }

    fun hasHistoricalEvent(hijriMonthIndex: Int, hijriDay: Int): Boolean {
        return historicalEventsMap.containsKey(Pair(hijriMonthIndex, hijriDay))
    }
}
