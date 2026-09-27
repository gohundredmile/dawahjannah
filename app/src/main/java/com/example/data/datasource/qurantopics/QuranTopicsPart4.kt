package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart4 {

    val topics: List<QuranTopic> = listOf(
        // 1. শিরক ও এর ভয়াবহতা
        QuranTopic(
            id = "topic_shirk",
            categoryId = "cat_aqeedah",
            nameBn = "শিরক ও এর ভয়াবহতা",
            nameEn = "Shirk (Associating Partners with Allah)",
            nameAr = "الشرك بالله وخطورته",
            descriptionBn = "ইসলামের সবচেয়ে গুরুতর অপরাধ ও ক্ষমার অযোগ্য পাপ হলো শিরক। আল্লাহর সাথে কাউকে শরিক না করার কঠোর তাগিদ ও পরিণাম।",
            searchKeywordsBn = listOf("শিরক", "মুশরিক", "অংশীদার", "মূর্তি পূজা", "তাওহীদ", "সবচেয়ে বড় গুনাহ", "ক্ষমার অযোগ্য"),
            searchKeywordsEn = listOf("shirk", "polytheism", "idolatry", "partners", "unforgivable sin"),
            isFeatured = true,
            iconEmoji = "⛔",
            relatedTopicIds = listOf("topic_tawheed", "topic_names_of_allah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 48,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِنَّ اللَّهَ لَا يَغْفِرُ أَن يُشْرَكَ بِهِ وَيَغْفِرُ مَا دُونَ ذَٰلِكَ لِمَن يَشَاءُ ۚ وَمَن يُشْرِكْ بِاللَّهِ فَقَدِ افْتَرَىٰ إِثْمًا عَظِيمًا",
                    translationBn = "নিশ্চয় আল্লাহ তাঁর সাথে শিরক করার অপরাধ ক্ষমা করেন না। তা ছাড়া অন্য যেকোনো পাপ যাকে ইচ্ছা তিনি ক্ষমা করে দেন। আর যে আল্লাহর সাথে শরিক করে, সে তো এক বিরাট অপবাদ ও মহাপাপে লিপ্ত হলো।",
                    translationEn = "Indeed, Allah does not forgive association with Him, but He forgives what is less than that for whom He wills. And he who associates others with Allah has certainly fabricated a tremendous sin.",
                    juzNumber = 5,
                    pageNumber = 86,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "শিরক হচ্ছে একমাত্র পাপ যা তওবা ছাড়া আল্লাহ তা'আলা কিয়ামতের দিন ক্ষমা করবেন না।"
                ),
                TopicAyah(
                    surahNumber = 31,
                    ayahNumber = 13,
                    surahNameBn = "সূরা লুকমান",
                    surahNameEn = "Luqman",
                    surahNameAr = "لقمان",
                    totalAyahsInSurah = 34,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَإِذْ قَالَ لُقْمَانُ لِابْنِهِ وَهُوَ يَعِظُهُ يَا بُنَيَّ لَا تُشْرِكْ بِاللَّهِ ۖ إِنَّ الشِّرْكَ لَظُلْمٌ عَظِيمٌ",
                    translationBn = "স্মরণ করুন, যখন লুকমান উপদেশ দিতে গিয়ে তার পুত্রকে বললেন: 'হে আমার বৎস! আল্লাহর সাথে শরিক করো না। নিশ্চয় শিরক এক চরম অন্যায় ও মহাপাপ।' ",
                    translationEn = "And [mention, O Muhammad], when Luqman said to his son while he was advising him, 'O my son, do not associate [anything] with Allah. Indeed, association [with Him] is great injustice.'",
                    juzNumber = 21,
                    pageNumber = 412,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "হযরত লুকমান (আ.) স্বীয় পুত্রকে সর্বপ্রথম তাওহীদের শিক্ষা দেন এবং শিরক থেকে সতর্ক করেন।"
                )
            )
        ),

        // 2. তাকওয়া ও আল্লাহভীতি
        QuranTopic(
            id = "topic_taqwa",
            categoryId = "cat_aqeedah",
            nameBn = "তাকওয়া ও আল্লাহভীতি",
            nameEn = "Taqwa (God-Consciousness & Piety)",
            nameAr = "التقوى ومخافة الله",
            descriptionBn = "আল্লাহভীতি অন্তরের সর্বোত্তম ভূষণ এবং ইহকাল ও পরকালে সফলতার মূল চাবিকাঠি। মুত্তাকীদের মর্যাদা ও পুরস্কার।",
            searchKeywordsBn = listOf("তাকওয়া", "মুত্তাকী", "আল্লাহভীতি", "পরহেজগারি", "খোদাভীতি", "পাথেয়"),
            searchKeywordsEn = listOf("taqwa", "piety", "god-fearing", "righteousness", "muttaqeen"),
            isFeatured = true,
            iconEmoji = "🛡️",
            relatedTopicIds = listOf("topic_tawheed", "topic_tawakkul", "topic_patience"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 49,
                    ayahNumber = 13,
                    surahNameBn = "সূরা আল-হুজুরাত",
                    surahNameEn = "Al-Hujurat",
                    surahNameAr = "الحجرات",
                    totalAyahsInSurah = 18,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا النَّاسُ إِنَّا خَلَقْنَاكُم مِّن ذَكَرٍ وَأُنثَىٰ وَجَعَلْنَاكُمْ شُعُوبًا وَقَبَائِلَ لِتَعَارَفُوا ۚ إِنَّ أَكْرَمَكُمْ عِندَ اللَّهِ أَتْقَاكُمْ ۚ إِنَّ اللَّهَ عَلِيمٌ خَبِيرٌ",
                    translationBn = "হে মানবজাতি! আমি তোমাদেরকে এক পুরুষ ও এক নারী থেকে সৃষ্টি করেছি এবং তোমাদেরকে বিভিন্ন জাতি ও গোত্রে বিভক্ত করেছি যেন তোমরা পরস্পরকে চিনতে পার। নিশ্চয় আল্লাহর কাছে তোমাদের মধ্যে সর্বাধিক সম্মানিত সেই ব্যক্তি, যে তোমাদের মধ্যে সবচেয়ে বেশি মুত্তাকী। নিশ্চয় আল্লাহ সর্বজ্ঞ, সম্যক অবহিত।",
                    translationEn = "O mankind, indeed We have created you from male and female and made you peoples and tribes that you may know one another. Indeed, the most noble of you in the sight of Allah is the most righteous of you. Indeed, Allah is Knowing and Acquainted.",
                    juzNumber = 26,
                    pageNumber = 517,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ইসলামে বংশমর্যাদা, বর্ণ বা সম্পদের কোনো শ্রেষ্ঠত্ব নেই; মানুষের আসল শ্রেষ্ঠত্ব কেবল তাকওয়ায়।"
                ),
                TopicAyah(
                    surahNumber = 65,
                    ayahNumber = 2,
                    surahNameBn = "সূরা আত-তালাক্ব",
                    surahNameEn = "At-Talaq",
                    surahNameAr = "الطلاق",
                    totalAyahsInSurah = 12,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا ۝ وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ",
                    translationBn = "আর যে আল্লাহকে ভয় করে, তিনি তার জন্য সংকট থেকে উত্তরণের পথ বের করে দেন এবং তাকে এমন উৎস থেকে রিজিক দান করেন, যা সে কল্পনাও করতে পারে না।",
                    translationEn = "And whoever fears Allah - He will make for him a way out, and will provide for him from where he does not expect.",
                    juzNumber = 28,
                    pageNumber = 558,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "তাকওয়ার অলৌকিক বরকত: যেকোনো বিপদ থেকে মুক্তি ও কল্পনাতীত বরকতময় রিজিক।"
                )
            )
        ),

        // 3. রমাদান ও সিয়াম (রোজা)
        QuranTopic(
            id = "topic_sawm_ramadan",
            categoryId = "cat_ibadah",
            nameBn = "রমাদান ও সিয়াম (রোজা)",
            nameEn = "Fasting (Sawm) & Ramadan",
            nameAr = "الصيام وشهر رمضان المبارك",
            descriptionBn = "রমাদান মাসের ফরজ রোজা, তাকওয়া অর্জনের পথ, কুরআন নাযিলের বরকতময় মাস ও রোজার বিধান।",
            searchKeywordsBn = listOf("রোজা", "রমাদান", "সিয়াম", "সাওম", "সেহরি", "ইফতার", "কুরআন নাযিল", "লাইলাতুল কদর"),
            searchKeywordsEn = listOf("sawm", "fasting", "ramadan", "quran revelation", "roza"),
            isFeatured = true,
            iconEmoji = "🌙",
            relatedTopicIds = listOf("topic_salah", "topic_quran_guidance"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 183,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ كَمَا كُتِبَ عَلَى الَّذِينَ مِن قَبْلِكُمْ لَعَلَّكُمْ تَتَّقُونَ",
                    translationBn = "হে মুমিনগণ! তোমাদের ওপর সিয়াম ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তীদের ওপর; যেন তোমরা তাকওয়া অর্জন করতে পার।",
                    translationEn = "O you who have believed, decreed upon you is fasting as it was decreed upon those before you that you may become righteous.",
                    juzNumber = 2,
                    pageNumber = 28,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "সিয়ামের মূল উদ্দেশ্য হলো আত্মার পরিশুদ্ধি ও আল্লাহভীতি অর্জন করা।"
                ),
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 185,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ هُدًى لِّلنَّاسِ وَبَيِّنَاتٍ مِّنَ الْهُدَىٰ وَالْفُرْقَانِ ۚ فَمَن شَهِدَ مِنكُمُ الشَّهْرَ فَلْيَصُمْهُ",
                    translationBn = "রমাদান মাস, যাতে নাযিল করা হয়েছে আল-কুরআন, যা মানুষের জন্য হেদায়েত এবং সৎপথের সুস্পষ্ট প্রমাণ ও সত্য-মিথ্যার পার্থক্যকারী। কাজেই তোমাদের মধ্যে যে এ মাস পাবে, সে যেন এতে সিয়াম পালন করে।",
                    translationEn = "The month of Ramadan [is that] in which was revealed the Quran, a guidance for the people and clear proofs of guidance and criterion. So whoever sights [the new moon of] the month, let him fast it.",
                    juzNumber = 2,
                    pageNumber = 28,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কুরআনের সাথে রমাদানের অবিচ্ছেদ্য যোগসূত্র এবং এ মাসের মহান সম্মান।"
                )
            )
        ),

        // 4. যাকাত ও দান-সাদাকাহ
        QuranTopic(
            id = "topic_zakat_sadaqah",
            categoryId = "cat_wealth",
            nameBn = "যাকাত ও দান-সাদাকাহ",
            nameEn = "Zakat, Charity & Sadaqah",
            nameAr = "الزكاة والصدقات والإنفاق",
            descriptionBn = "আল্লাহর রাস্তায় ব্যয়ের অসীম সওয়াব, অন্তরের কৃপণতা দূরীকরণ, যাকাতের খাতসমূহ ও দারিদ্র্য বিমোচন।",
            searchKeywordsBn = listOf("যাকাত", "সাদাকাহ", "দান", "ইনফাক", "গরিবের হক", "সাতশত গুণ", "বরকত"),
            searchKeywordsEn = listOf("zakat", "charity", "sadaqah", "infaq", "spending for allah"),
            isFeatured = true,
            iconEmoji = "🤝",
            relatedTopicIds = listOf("topic_rizq", "topic_usury_riba"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 261,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "مَّثَلُ الَّذِينَ يُنفِقُونَ أَمْوَالَهُمْ فِي سَبِيلِ اللَّهِ كَمَثَلِ حَبَّةٍ أَنبَتَتْ سَبْعَ سَنَابِلَ فِي كُلِّ سُنبُلَةٍ مِّائَةُ حَبَّةٍ ۗ وَاللَّهُ يُضَاعِفُ لِمَن يَشَاءُ ۗ وَاللَّهُ وَاسِعٌ عَلِيمٌ",
                    translationBn = "যারা আল্লাহর পথে নিজেদের ধন-সম্পদ ব্যয় করে, তাদের উপমা একটি শস্যবীজের মতো, যা থেকে সাতটি শীষ উৎপন্ন হয় এবং প্রতিটি শীষে থাকে একশত দানা। আর আল্লাহ যাকে ইচ্ছা বহুগুণ বৃদ্ধি করে দেন; আল্লাহ প্রাচুর্যময়, সর্বজ্ঞ।",
                    translationEn = "The example of those who spend their wealth in the way of Allah is like a seed of grain which grows seven spikes; in each spike is a hundred grains. And Allah multiplies [His reward] for whom He wills. And Allah is all-Encompassing and Knowing.",
                    juzNumber = 3,
                    pageNumber = 44,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহর সন্তুষ্টির উদ্দেশ্যে দান এক দানায় সাত শত গুণ বা তার চেয়েও বেশি সওয়াব বয়ে আনে।"
                ),
                TopicAyah(
                    surahNumber = 9,
                    ayahNumber = 60,
                    surahNameBn = "সূরা আত-তাওবাহ",
                    surahNameEn = "At-Tawbah",
                    surahNameAr = "التوبة",
                    totalAyahsInSurah = 129,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِنَّمَا الصَّدَقَاتُ لِلْفُقَرَاءِ وَالْمَسَاكِينِ وَالْعَامِلِينَ عَلَيْهَا وَالْمُؤَلَّفَةِ قُلُوبُهُمْ وَفِي الرِّقَابِ وَالْغَارِمِينَ وَفِي سَبِيلِ اللَّهِ وَابْنِ السَّبِيلِ ۖ فَرِيضَةً مِّنَ اللَّهِ ۗ وَاللَّهُ عَلِيمٌ حَكِيمٌ",
                    translationBn = "যাকাত কেবল ফকির, মিসকিন ও যাকাত আদায়কারী কর্মচারীদের জন্য, যাদের মন জয় করা প্রয়োজন তাদের জন্য, দাসমুক্তির জন্য, ঋণগ্রস্তদের ঋণ পরিশোধে, আল্লাহর পথে এবং মুসাফিরদের জন্য। এটা আল্লাহর নির্ধারিত বিধান। আর আল্লাহ সর্বজ্ঞ, প্রজ্ঞাময়।",
                    translationEn = "Zakah expenditures are only for the poor and for the needy and for those employed to collect [zakah] and for bringing hearts together [for Islam] and for freeing captives [or slaves] and for those in debt and for the cause of Allah and for the [stranded] traveler - an obligation [imposed] by Allah. And Allah is Knowing and Wise.",
                    juzNumber = 10,
                    pageNumber = 196,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কুরআনে সুস্পষ্টভাবে নির্ধারিত যাকাতের ৮টি অপরিহার্য খাত।"
                )
            )
        ),

        // 5. হজ্জ ও উমরাহ
        QuranTopic(
            id = "topic_hajj_umrah",
            categoryId = "cat_ibadah",
            nameBn = "হজ্জ ও উমরাহ",
            nameEn = "Hajj & Umrah Pilgrimage",
            nameAr = "الحج والعمرة والبيت الحرام",
            descriptionBn = "বাইতুল্লাহর হজ্জ, সামর্থ্যবানদের ওপর দায়িত্ব, আরাফাতের ময়দান, সাফা-মারওয়া এবং ইব্রাহীমী উত্তরাধিকার।",
            searchKeywordsBn = listOf("হজ্জ", "উমরাহ", "কাবা", "মক্কা", "বাইতুল্লাহ", "আরাফাত", "তাওয়াফ", "হজ্ব"),
            searchKeywordsEn = listOf("hajj", "umrah", "pilgrimage", "kaaba", "makkah", "arafah"),
            isFeatured = true,
            iconEmoji = "🕋",
            relatedTopicIds = listOf("topic_salah", "topic_prophet_ibrahim"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 97,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "فِيهِ آيَاتٌ بَيِّنَاتٌ مَّقَامُ إِبْرَاهِيمَ ۖ وَمَن دَخَلَهُ كَانَ آمِنًا ۗ وَلِلَّهِ عَلَى النَّاسِ حِجُّ الْبَيْتِ مَنِ اسْتَطَاعَ إِلَيْهِ سَبِيلًا ۚ وَمَن كَفَرَ فَإِنَّ اللَّهَ غَنِيٌّ عَنِ الْعَالَمِينَ",
                    translationBn = "তাতে রয়েছে সুস্পষ্ট নিদর্শনসমূহ এবং মাকামে ইবরাহীম। আর যে সেখানে প্রবেশ করে, সে নিরাপত্তা লাভ করে। আর মানুষের মধ্যে যার সেখানে যাওয়ার সামর্থ্য আছে, আল্লাহর উদ্দেশ্যে ঐ গৃহের হজ্জ করা তার ওপর ফরজ। আর যে অস্বীকার করে, তবে জেনে রাখুক আল্লাহ সমগ্র সৃষ্টিজগত থেকে অমুখাপেক্ষী।",
                    translationEn = "In it are clear signs [such as] the standing place of Abraham. And whoever enters it shall be safe. And [due] to Allah from the people is a pilgrimage to the House - for whoever is able to find thereto a way. But whoever disbelieves - then indeed, Allah is free from need of the worlds.",
                    juzNumber = 4,
                    pageNumber = 62,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "হজ্জ ইসলামের অন্যতম স্তম্ভ এবং সামর্থ্যবান ব্যক্তির জন্য জীবনে একবার আদায় করা ফরজ।"
                ),
                TopicAyah(
                    surahNumber = 22,
                    ayahNumber = 27,
                    surahNameBn = "সূরা আল-হাজ্জ",
                    surahNameEn = "Al-Hajj",
                    surahNameAr = "الحج",
                    totalAyahsInSurah = 78,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَأَذِّن فِي النَّاسِ بِالْحَجِّ يَأْتُوكَ رِجَالًا وَعَلَىٰ كُلِّ ضَامِرٍ يَأْتِينَ مِن كُلِّ فَجٍّ عَمِيقٍ",
                    translationBn = "এবং মানুষের মাঝে হজ্জের ঘোষণা প্রচার করে দাও; তারা তোমার কাছে আসবে পায়ে হেঁটে এবং সর্বপ্রকার কৃশকায় উটের পিঠে চড়ে—তারা আসবে দূর-দূরান্তের গভীর গিরিপথ অতিক্রম করে।",
                    translationEn = "And proclaim to the people the Hajj [pilgrimage]; they will come to you on foot and on every lean camel; they will come from every distant pass.",
                    juzNumber = 17,
                    pageNumber = 335,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহ তা'আলা হযরত ইবরাহীম (আ.)-কে সমগ্র মানবজাতির জন্য হজ্জের বৈশ্বিক আহ্বান প্রচারের নির্দেশ দেন।"
                )
            )
        ),

        // 6. মুনাফিকদের চরিত্র ও কপটতা
        QuranTopic(
            id = "topic_munafiq",
            categoryId = "cat_aqeedah",
            nameBn = "মুনাফিকদের চরিত্র ও কপটতা",
            nameEn = "Hypocrites (Munafiqoon) & Hypocrisy",
            nameAr = "صفات المنافقين وعاقبتهم",
            descriptionBn = "মুখে বিশ্বাস কিন্তু অন্তরে অবিশ্বাস পোষণকারী মুনাফিকদের বৈশিষ্ট্য, মিথ্যা শপথ এবং জাহান্নামের সর্বনিম্ন স্তরে তাদের পরিণতি।",
            searchKeywordsBn = listOf("মুনাফিক", "কপটতা", "দ্বিমুখী", "ধোঁকাবাজ", "মুনাফেক", "জাহান্নামের নিম্নস্তর"),
            searchKeywordsEn = listOf("hypocrisy", "hypocrites", "munafiq", "double-faced", "nifaq"),
            isFeatured = false,
            iconEmoji = "🎭",
            relatedTopicIds = listOf("topic_shirk", "topic_jahannam"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 145,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِنَّ الْمُنَافِقِينَ فِي الدَّرْكِ الْأَسْفَلِ مِنَ النَّارِ وَلَن تَجِدَ لَهُمْ نَصِيرًا",
                    translationBn = "নিশ্চয় মুনাফিকরা থাকবে জাহান্নামের সর্বনিম্ন স্তরে; আর তাদের জন্য কখনো কোনো সাহায্যকারী পাবে না।",
                    translationEn = "Indeed, the hypocrites will be in the lowest depths of the Fire - and never will you find for them a helper.",
                    juzNumber = 5,
                    pageNumber = 101,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কপটতার ভয়াবহ পরিণতি: কাফেরদের চেয়েও কঠোরতম শাস্তি মুনাফিকদের জন্য নির্ধারিত।"
                ),
                TopicAyah(
                    surahNumber = 63,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আল-মুনাফিকুন",
                    surahNameEn = "Al-Munafiqun",
                    surahNameAr = "المنافقون",
                    totalAyahsInSurah = 11,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِذَا جَاءَكَ الْمُنَافِقُونَ قَالُوا نَشْهَدُ إِنَّكَ لَرَسُولُ اللَّهِ ۗ وَاللَّهُ يَعْلَمُ إِنَّكَ لَرَسُولُهُ وَاللَّهُ يَشْهَدُ إِنَّ الْمُنَافِقِينَ لَكَاذِبُونَ",
                    translationBn = "যখন মুনাফিকরা আপনার কাছে আসে, তখন তারা বলে: 'আমরা সাক্ষ্য দিচ্ছি আপনি নিশ্চয় আল্লাহর রাসুল।' আল্লাহ তো জানেনই যে আপনি তাঁর রাসুল এবং আল্লাহ সাক্ষ্য দিচ্ছেন যে নিশ্চয় মুনাফিকরা পুরোপুরি মিথ্যাবাদী।",
                    translationEn = "When the hypocrites come to you, [O Muhammad], they say, 'We testify that you are the Messenger of Allah.' And Allah knows that you are His Messenger, and Allah testifies that the hypocrites are liars.",
                    juzNumber = 28,
                    pageNumber = 554,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মুনাফিকরা মুখে সততার মিষ্টি বুলি বললেও তাদের অন্তর কপটতা ও বিদ্বেষে ভরা থাকে।"
                )
            )
        ),

        // 7. সৎকাজের আদেশ ও অসৎকাজের নিষেধ
        QuranTopic(
            id = "topic_amr_bil_maruf",
            categoryId = "cat_dawah_peace",
            nameBn = "সৎকাজের আদেশ ও অসৎকাজের নিষেধ",
            nameEn = "Enjoining Good & Forbidding Evil",
            nameAr = "الأمر بالمعروف والنهي عن المنكر",
            descriptionBn = "উম্মতে মুহাম্মদীর শ্রেষ্ঠত্বের মূল কারণ হলো সমাজে ন্যায় ও কল্যাণের প্রসার এবং অন্যায় ও অনাচারের প্রতিরোধ করা।",
            searchKeywordsBn = listOf("সৎকাজ", "অসৎকাজ", "আমর বিল মারুফ", "নাহি আনিল মুনকার", "উম্মতের দায়িত্ব", "কল্যাণকামিতা"),
            searchKeywordsEn = listOf("enjoining good", "forbidding evil", "dawah", "amr bil maroof"),
            isFeatured = true,
            iconEmoji = "📢",
            relatedTopicIds = listOf("topic_dawah_wisdom", "topic_brotherhood_unity"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 110,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "كُنتُمْ خَيْرَ أُمَّةٍ أُخْرِجَتْ لِلنَّاسِ تَأْمُرُونَ بِالْمَعْرُوفِ وَتَنْهَوْنَ عَنِ الْمُنكَرِ وَتُؤْمِنُونَ بِاللَّهِ",
                    translationBn = "তোমরাই হলে সর্বোত্তম উম্মত, যাদের মানবজাতির কল্যাণের জন্য আবির্ভূত করা হয়েছে; তোমরা সৎকাজের আদেশ দাও, অসৎকাজে নিষেধ কর এবং আল্লাহর ওপর দৃঢ় বিশ্বাস স্থাপন কর।",
                    translationEn = "You are the best nation produced [as an example] for mankind. You enjoin what is right and forbid what is wrong and believe in Allah.",
                    juzNumber = 4,
                    pageNumber = 64,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মুসলিম উম্মাহর মর্যাদা টিকে থাকে তাদের সামাজিক দায়বদ্ধতা ও দাওয়াতের ভূমিকার ওপর।"
                )
            )
        )
    )
}
