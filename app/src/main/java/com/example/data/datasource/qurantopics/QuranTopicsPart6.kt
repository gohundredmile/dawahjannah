package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart6 {

    val topics: List<QuranTopic> = listOf(
        // 1. জ্ঞান, প্রজ্ঞা ও চিন্তাশীলতা
        QuranTopic(
            id = "topic_knowledge_wisdom",
            categoryId = "cat_life_mind",
            nameBn = "জ্ঞান, প্রজ্ঞা ও চিন্তাশীলতা",
            nameEn = "Knowledge, Wisdom & Reflection",
            nameAr = "العلم والحكمة والتفكر والتدبر",
            descriptionBn = "ইসলামে জ্ঞানের মর্যাদা অনন্য। জ্ঞানীদের সাথে অজ্ঞদের কোনো তুলনা হতে পারে না। সৃষ্টিজগত নিয়ে গবেষণা ও জ্ঞান বৃদ্ধির দো'আ।",
            searchKeywordsBn = listOf("জ্ঞান", "ইলম", "প্রজ্ঞা", "চিন্তাশীলতা", "গবেষণা", "আলেম", "রব্বি যিদনি ইলমা"),
            searchKeywordsEn = listOf("knowledge", "wisdom", "reflection", "intellect", "ilm", "scholars"),
            isFeatured = true,
            iconEmoji = "💡",
            relatedTopicIds = listOf("topic_quran_guidance", "topic_nature_universe"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 39,
                    ayahNumber = 9,
                    surahNameBn = "সূরা আয-যুমার",
                    surahNameEn = "Az-Zumar",
                    surahNameAr = "الزمر",
                    totalAyahsInSurah = 75,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "قُلْ هَلْ يَسْتَوِي الَّذِينَ يَعْلَمُونَ وَالَّذِينَ لَا يَعْلَمُونَ ۗ إِنَّمَا يَتَذَكَّرُ أُولُو الْأَلْبَابِ",
                    translationBn = "বলুন, যারা জানে আর যারা জানে না তারা কি সমান হতে পারে? কেবল বিবেকবান লোকেরাই উপদেশ গ্রহণ করে।",
                    translationEn = "Say, 'Are those who know equal to those who do not know?' Only they will remember [who are] people of understanding.",
                    juzNumber = 23,
                    pageNumber = 459,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "জ্ঞান ও অজ্ঞতার মাঝে ব্যবধান রাত ও দিনের মতো স্পষ্ট।"
                ),
                TopicAyah(
                    surahNumber = 20,
                    ayahNumber = 114,
                    surahNameBn = "সূরা ত্বা-হা",
                    surahNameEn = "Ta-Ha",
                    surahNameAr = "طه",
                    totalAyahsInSurah = 135,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَقُل رَّبِّ زِدْنِي عِلْمًا",
                    translationBn = "এবং বলুন: 'হে আমার প্রতিপালক! আমার জ্ঞান বৃদ্ধি করে দিন।' ",
                    translationEn = "And say, 'My Lord, increase me in knowledge.'",
                    juzNumber = 16,
                    pageNumber = 320,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহ তা'আলা রাসুলুল্লাহ (সা.)-কে জ্ঞান বৃদ্ধির জন্য দো'আ করার নির্দেশ দেন।"
                )
            )
        ),

        // 2. কৃতজ্ঞতা ও শুকরিয়া আদায়
        QuranTopic(
            id = "topic_gratitude_shukr",
            categoryId = "cat_akhlaq",
            nameBn = "কৃতজ্ঞতা ও শুকরিয়া আদায়",
            nameEn = "Gratitude (Shukr) & Divine Blessings",
            nameAr = "الشكر والاعتراف بنعم الله",
            descriptionBn = "আল্লাহর দেওয়া অসংখ্য নিয়ামতের শুকরিয়া আদায় করলে নিয়ামত বৃদ্ধি পায়, আর অকৃতজ্ঞতার শাস্তি ভয়াবহ।",
            searchKeywordsBn = listOf("শুকরিয়া", "কৃতজ্ঞতা", "আলহামদুলিল্লাহ", "নিয়ামত", "শুকর", "অকৃতজ্ঞতা"),
            searchKeywordsEn = listOf("gratitude", "shukr", "thankfulness", "blessings", "praise"),
            isFeatured = true,
            iconEmoji = "💎",
            relatedTopicIds = listOf("topic_patience", "topic_names_of_allah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 14,
                    ayahNumber = 7,
                    surahNameBn = "সূরা ইবরাহীম",
                    surahNameEn = "Ibrahim",
                    surahNameAr = "إبراهيم",
                    totalAyahsInSurah = 52,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِي لَشَدِيدٌ",
                    translationBn = "এবং স্মরণ করুন, যখন তোমাদের প্রতিপালক ঘোষণা করেছিলেন: 'যদি তোমরা কৃতজ্ঞতা জ্ঞাপন কর, তবে আমি অবশ্যই তোমাদের নিয়ামত আরও বৃদ্ধি করে দেব; আর যদি অকৃতজ্ঞ হও, তবে নিশ্চয় আমার শাস্তি বড়ই কঠোর।' ",
                    translationEn = "And [remember] when your Lord proclaimed, 'If you are grateful, I will surely increase you [in favor]; but if you deny, indeed, My punishment is severe.'",
                    juzNumber = 13,
                    pageNumber = 256,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কৃতজ্ঞতা নিয়ামতকে স্থায়ী করে এবং তা ক্রমাগত বৃদ্ধি করতে থাকে।"
                ),
                TopicAyah(
                    surahNumber = 16,
                    ayahNumber = 18,
                    surahNameBn = "সূরা আন-নাহল",
                    surahNameEn = "An-Nahl",
                    surahNameAr = "النحل",
                    totalAyahsInSurah = 128,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَإِن تَعُدُّوا نِعْمَةَ اللَّهِ لَا تُحْصُوهَا ۗ إِنَّ اللَّهَ لَغَفُورٌ رَّحِيمٌ",
                    translationBn = "আর যদি তোমরা আল্লাহর নিয়ামত গণনা করতে চাও, তবে তার সংখ্যা নিরূপণ করতে পারবে না; নিশ্চয় আল্লাহ পরম ক্ষমাশীল, অসীম দয়ালু।",
                    translationEn = "And if you should count the favors of Allah, you could not enumerate them. Indeed, Allah is Forgiving and Merciful.",
                    juzNumber = 14,
                    pageNumber = 269,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মানবজীবন প্রতিটি মুহূর্তে আল্লাহর অগণিত অনুদানে সিক্ত।"
                )
            )
        ),

        // 3. রোগ নিরাময় ও শিফা
        QuranTopic(
            id = "topic_shifa_healing",
            categoryId = "cat_life_mind",
            nameBn = "কুরআনে শিফা ও রোগ নিরাময়",
            nameEn = "Healing (Shifa) & Cure in Quran",
            nameAr = "الشفاء والعافية في القرآن الكريم",
            descriptionBn = "কুরআনুল কারীম মুমিনদের অন্তরের ব্যাধি ও শারীরিক অসুস্থতা দূর করার জন্য এক মহা ঐশী আরোগ্য ও নিরাময় (শিফা)।",
            searchKeywordsBn = listOf("শিফা", "নিরাময়", "আরোগ্য", "অসুস্থতা", "রোগমুক্তি", "রুকইয়াহ", "ঔষধ"),
            searchKeywordsEn = listOf("shifa", "healing", "cure", "health", "illness", "ruqyah"),
            isFeatured = true,
            iconEmoji = "🌿",
            relatedTopicIds = listOf("topic_tranquility", "topic_dua"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 17,
                    ayahNumber = 82,
                    surahNameBn = "সূরা আল-ইসরা",
                    surahNameEn = "Al-Isra",
                    surahNameAr = "الإسراء",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَنُنَزِّلُ مِنَ الْقُرْآنِ مَا هُوَ شِفَاءٌ وَرَحْمَةٌ لِّلْمُؤْمِنِينَ ۙ وَلَا يَزِيدُ الظَّالِمِينَ إِلَّا خَسَارًا",
                    translationBn = "আর আমি কুরআন থেকে এমন জিনিস নাযিল করি যা মুমিনদের জন্য আরোগ্য (শিফা) ও রহমত; কিন্তু তা জালিমদের কেবল ক্ষতিই বৃদ্ধি করে।",
                    translationEn = "And We send down of the Quran that which is healing and mercy for the believers, but it does not increase the wrongdoers except in loss.",
                    juzNumber = 15,
                    pageNumber = 290,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কুরআন ঈমানদারদের মানসিক হতাশা, কুচিন্তা এবং আধ্যাত্মিক ও শারীরিক ক্লেশের শ্রেষ্ঠ প্রতিষেধক।"
                ),
                TopicAyah(
                    surahNumber = 26,
                    ayahNumber = 80,
                    surahNameBn = "সূরা আশ-শু'আরা",
                    surahNameEn = "Ash-Shu'ara",
                    surahNameAr = "الشعراء",
                    totalAyahsInSurah = 227,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَإِذَا مَرِضْتُ فَهُوَ يَشْفِينِ",
                    translationBn = "এবং 'যখন আমি অসুস্থ হই, তখন তিনিই আমাকে সুস্থতা ও আরোগ্য দান করেন।' ",
                    translationEn = "And when I am ill, it is He who cures me.",
                    juzNumber = 19,
                    pageNumber = 370,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "হযরত ইবরাহীম (আ.)-এর অবিচল বিশ্বাস: চিকিৎসক মাধ্যম মাত্র, কিন্তু প্রকৃত শিফাদাতা একমাত্র আল্লাহ।"
                )
            )
        ),

        // 4. মৃত্যু ও বারযাখের জীবন
        QuranTopic(
            id = "topic_death_barzakh",
            categoryId = "cat_akhirah",
            nameBn = "মৃত্যু ও কবরের বারযাখী জীবন",
            nameEn = "Death & Life of Barzakh",
            nameAr = "الموت والبرزخ وحقيقة الفناء",
            descriptionBn = "প্রতিটি প্রাণীকেই মৃত্যুর স্বাদ গ্রহণ করতে হবে। পার্থিব জীবন ক্ষণস্থায়ী এবং মৃত্যুর পর পুনরুত্থান পর্যন্ত বারযাখের জগত।",
            searchKeywordsBn = listOf("মৃত্যু", "কবর", "বারযাখ", "মউত", "জানাজা", "কুল্লু নাফসিন", "পার্থিব জীবন"),
            searchKeywordsEn = listOf("death", "barzakh", "grave", "mortality", "passing away"),
            isFeatured = true,
            iconEmoji = "⏳",
            relatedTopicIds = listOf("topic_day_of_judgment", "topic_jannah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 185,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "كُلُّ نَفْسٍ ذَائِقَةُ الْمَوْتِ ۗ وَإِنَّمَا تُوَفَّوْنَ أُجُورَكُمْ يَوْمَ الْقِيَامَةِ ۖ فَمَن زُحْزِحَ عَنِ النَّارِ وَأُدْخِلَ الْجَنَّةَ فَقَدْ فَازَ ۗ وَمَا الْحَيَاةُ الدُّنْيَا إِلَّا مَتَاعُ الْغُرُورِ",
                    translationBn = "প্রতিটি প্রাণীকেই মৃত্যুর স্বাদ আস্বাদন করতে হবে; আর তোমরা তো কিয়ামতের দিন নিজেদের কাজের পূর্ণ প্রতিফল পাবে। সুতরাং যাকে জাহান্নামের আগুন থেকে দূরে রাখা হলো এবং জান্নাতে প্রবেশ করানো হলো, সে-ই প্রকৃত সফলকাম। আর এই পার্থিব জীবন তো এক প্রতারণাময় ভোগ ছাড়া আর কিছুই নয়।",
                    translationEn = "Every soul will taste death, and you will only be given your [full] compensation on the Day of Resurrection. So he who is drawn away from the Fire and admitted to Paradise has attained [his desire]. And what is the life of this world except the enjoyment of delusion.",
                    juzNumber = 4,
                    pageNumber = 74,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মৃত্যুর বাস্তবতা স্মরণ রাখা মানুষকে অহংকারমুক্ত করে পরকালের প্রস্তুতিতে উদ্বুদ্ধ করে।"
                ),
                TopicAyah(
                    surahNumber = 23,
                    ayahNumber = 99,
                    surahNameBn = "সূরা আল-মুমিনূন",
                    surahNameEn = "Al-Mu'minun",
                    surahNameAr = "المؤمنون",
                    totalAyahsInSurah = 118,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "حَتَّىٰ إِذَا جَاءَ أَحَدَهُمُ الْمَوْتُ قَالَ رَبِّ ارْجِعُونِ ۝ لَعَلِّي أَعْمَلُ صَالِحًا فِيمَا تَرَكْتُ ۚ كَلَّا ۚ إِنَّهَا كَلِمَةٌ هُوَ قَائِلُهَا ۖ وَمِن وَرَائِهِم بَرْزَخٌ إِلَىٰ يَوْمِ يُبْعَثُونَ",
                    translationBn = "অবশেষে যখন তাদের কারো মৃত্যু উপস্থিত হয়, তখন সে বলে: 'হে আমার প্রতিপালক! আমাকে পুনরায় (দুনিয়ায়) ফেরত পাঠান; যেন আমি যা ছেড়ে এসেছি তাতে সৎকাজ করতে পারি।' কখনোই নয়! এটা তো কেবল একটা মুখের কথা যা সে বলবে। আর তাদের সামনে রয়েছে 'বারযাখ' (এক অন্তর্বর্তী পর্দা) পুনরুত্থান দিবস পর্যন্ত।",
                    translationEn = "[For such is the state of the disbelievers], until, when death comes to one of them, he says, 'My Lord, send me back that I might do righteousness in that which I left behind.' No! It is only a word he is saying; and behind them is a barrier until the Day they are resurrected.",
                    juzNumber = 18,
                    pageNumber = 348,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মৃত্যুর পর দুনিয়ায় প্রত্যাবর্তনের কোনো সুযোগ নেই; বারযাখে কৃতকর্মের প্রাথমিক প্রতিফল শুরু হয়।"
                )
            )
        ),

        // 5. কিয়ামতের প্রলয় ও ভয়াল দৃশ্য
        QuranTopic(
            id = "topic_day_of_judgment",
            categoryId = "cat_akhirah",
            nameBn = "কিয়ামতের প্রলয় ও ভয়াল দৃশ্য",
            nameEn = "Horrors of the Day of Judgment",
            nameAr = "أهوال يوم القيامة والنفخ في الصور",
            descriptionBn = "শিঙ্গায় ফুৎকারের সাথে সাথে নিখিল বিশ্বের মহাপ্রলয়, আসমান বিদীর্ণ হওয়া, সূর্য জ্যোতিহীন হওয়া এবং পর্বতমালা ধূলিকণায় পরিণত হওয়ার চিত্র।",
            searchKeywordsBn = listOf("কিয়ামত", "মহাপ্রলয়", "শিঙ্গায় ফুৎকার", "হাশর", "আসমান বিদীর্ণ", "শেষ দিবস"),
            searchKeywordsEn = listOf("qiyamah", "day of judgment", "doomsday", "apocalypse", "resurrection"),
            isFeatured = true,
            iconEmoji = "⚡",
            relatedTopicIds = listOf("topic_death_barzakh", "topic_balance_mizan"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 81,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আত-তাকভীর",
                    surahNameEn = "At-Takwir",
                    surahNameAr = "التكوير",
                    totalAyahsInSurah = 29,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "إِذَا الشَّمْسُ كُوِّرَتْ ۝ وَإِذَا النُّجُومُ انكَدَرَتْ ۝ وَإِذَا الْجِبَالُ سُيِّرَتْ ۝ وَإِذَا الْعِشَارُ عُطِّلَتْ ۝ وَإِذَا الْوُحُوشُ حُشِرَتْ ۝ وَإِذَا الْبِحَارُ سُجِّرَتْ",
                    translationBn = "যখন সূর্য জ্যোতিহীন হয়ে গুটিয়ে যাবে, যখন নক্ষত্ররাজি খসে পড়বে, যখন পর্বতমালা স্থানচ্যুত করে ধূলিসাৎ করা হবে, যখন পূর্ণগর্ভা উষ্ট্রীসমূহ উপেক্ষিত হবে, যখন বন্য পশুরা একত্রিত হবে এবং যখন মহাসমুদ্রসমূহ উত্তাল আগুনে রূপ নেবে...",
                    translationEn = "When the sun is wrapped up [in darkness], and when the stars fall, dispersing, and when the mountains are removed, and when full-term camels are neglected, and when the wild beasts are gathered, and when the seas are boiled over...",
                    juzNumber = 30,
                    pageNumber = 586,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কিয়ামতের ভয়াবহ দিনে সমগ্র মহাবিশ্ব এক অতুলনীয় রূপান্তরের মুখোমুখি হবে।"
                ),
                TopicAyah(
                    surahNumber = 99,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আয-যালযালাহ",
                    surahNameEn = "Az-Zalzalah",
                    surahNameAr = "الزلزلة",
                    totalAyahsInSurah = 8,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِذَا زُلْزِلَتِ الْأَرْضُ زِلْزَالَهَا ۝ وَأَخْرَجَتِ الْأَرْضُ أَثْقَالَهَا ۝ وَقَالَ الْإِنسَانُ مَا لَهَا",
                    translationBn = "যখন পৃথিবী তার প্রবল প্রকম্পনে প্রকম্পিত হবে, এবং পৃথিবী তার ভেতরের সমস্ত বোঝা বাইরে বের করে দেবে, এবং মানুষ (বিস্ময়ে) বলবে: 'এর কী হলো?' ",
                    translationEn = "When the earth is shaken with its [final] earthquake and the earth discharges its burdens and man says, 'What is [wrong] with it?'",
                    juzNumber = 30,
                    pageNumber = 599,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "পৃথিবীর চূড়ান্ত ভূমিকম্প ও মাটি থেকে সকল মৃত মানবের পুনরুত্থান।"
                )
            )
        ),

        // 6. হাশর, আমলনামা ও মীযান
        QuranTopic(
            id = "topic_balance_mizan",
            categoryId = "cat_akhirah",
            nameBn = "হাশর, আমলনামা ও মীযান",
            nameEn = "Resurrection, Record of Deeds & Balance (Mizan)",
            nameAr = "الحشر وتطاير الصحف والميزان",
            descriptionBn = "হাশরের ময়দানে সকল মানবজাতির সমবেত হওয়া, ডান ও বাম হাতে আমলনামা প্রদান এবং অণু পরিমাণ ভালো ও মন্দের ন্যায়সংগত হিসাব।",
            searchKeywordsBn = listOf("হাশর", "মীযান", "আমলনামা", "হিসাব", "পাল্লা", "ডান হাত", "যাররাহ পরিমাণ"),
            searchKeywordsEn = listOf("mizan", "balance", "scales", "book of deeds", "accounting", "reckoning"),
            isFeatured = true,
            iconEmoji = "⚖️",
            relatedTopicIds = listOf("topic_day_of_judgment", "topic_jannah", "topic_jahannam"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 99,
                    ayahNumber = 7,
                    surahNameBn = "সূরা আয-যালযালাহ",
                    surahNameEn = "Az-Zalzalah",
                    surahNameAr = "الزلزلة",
                    totalAyahsInSurah = 8,
                    revelationTypeBn = "মাদানী",
                    arabicText = "فَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ خَيْرًا يَرَهُ ۝ وَمَن يَعْمَلْ مِثْقَالَ ذَرَّةٍ شَرًّا يَرَهُ",
                    translationBn = "সুতরাং কেউ অণু পরিমাণ সৎকাজ করলে সে তা দেখতে পাবে, আর কেউ অণু পরিমাণ অসৎকাজ করলেও সে তা দেখতে পাবে।",
                    translationEn = "So whoever does an atom's weight of good will see it, and whoever does an atom's weight of evil will see it.",
                    juzNumber = 30,
                    pageNumber = 599,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহর আদালতে কোনো ক্ষুদ্রাতিক্ষুদ্র আমলও হারিয়ে যাবে না।"
                ),
                TopicAyah(
                    surahNumber = 21,
                    ayahNumber = 47,
                    surahNameBn = "সূরা আল-আম্বিয়া",
                    surahNameEn = "Al-Anbiya",
                    surahNameAr = "الأنبياء",
                    totalAyahsInSurah = 112,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَنَضَعُ الْمَوَازِينَ الْقِسْطَ لِيَوْمِ الْقِيَامَةِ فَلَا تُظْلَمُ نَفْسٌ شَيْئًا ۖ وَإِن كَانَ مِثْقَالَ حَبَّةٍ مِّنْ خَرْدَلٍ أَتَيْنَا بِهَا ۗ وَكَفَىٰ بِنَا حَاسِبِينَ",
                    translationBn = "আর আমি কিয়ামতের দিন স্থাপন করব সঠিক ও ন্যায়সঙ্গত পরিমাপের পাল্লা (মীযান); সুতরাং কারো প্রতি সামান্যতম অন্যায় করা হবে না। যদি একটি সরিষার দানা পরিমাণও কোনো কাজ থাকে, আমি তা উপস্থিত করব; আর হিসাবকারী হিসেবে আমিই যথেষ্ট।",
                    translationEn = "And We place the scales of justice for the Day of Resurrection, so no soul will be treated unjustly at all. And if there is [even] the weight of a mustard seed, We will bring it forth. And sufficient are We as accountant.",
                    juzNumber = 17,
                    pageNumber = 326,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহর পূর্ণাঙ্গ ন্যায়বিচারের চিত্র; কোনো সৃষ্টি সেদিন অবিচারের শিকার হবে না।"
                )
            )
        ),

        // 7. আল্লাহর রহমত ও নিরাশ না হওয়া
        QuranTopic(
            id = "topic_mercy_no_despair",
            categoryId = "cat_aqeedah",
            nameBn = "আল্লাহর অসীম রহমত ও নিরাশ না হওয়া",
            nameEn = "Vast Mercy of Allah & Never Despairing",
            nameAr = "رحمة الله الواسعة والنهي عن القنوط",
            descriptionBn = "যতই গুনাহ হোক না কেন আল্লাহর রহমত থেকে কখনো নিরাশ হওয়া যাবে না। সমস্ত পাপ ক্ষমার পরম আশ্বাস ও অনুশোচনা।",
            searchKeywordsBn = listOf("রহমত", "নিরাশ", "ক্ষমা", "দয়া", "পাপমোচন", "আল্লাহর রহমত", "আশা"),
            searchKeywordsEn = listOf("mercy of allah", "hope", "no despair", "forgiveness", "repentance"),
            isFeatured = true,
            iconEmoji = "🌸",
            relatedTopicIds = listOf("topic_tawbah", "topic_names_of_allah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 39,
                    ayahNumber = 53,
                    surahNameBn = "সূরা আয-যুমার",
                    surahNameEn = "Az-Zumar",
                    surahNameAr = "الزمر",
                    totalAyahsInSurah = 75,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا ۚ إِنَّهُ هُوَ الْغَفُورُ الرَّحِيمُ",
                    translationBn = "বলুন: 'হে আমার বান্দাগণ, যারা নিজেদের ওপর অবিচার করেছ! তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না। নিশ্চয় আল্লাহ সমস্ত গুনাহ ক্ষমা করে দেন। নিশ্চয় তিনি পরম ক্ষমাশীল, পরম দয়ালু।' ",
                    translationEn = "Say, 'O My servants who have transgressed against themselves [by sinning], do not despair of the mercy of Allah. Indeed, Allah forgives all sins. Indeed, it is He who is the Forgiving, the Merciful.'",
                    juzNumber = 24,
                    pageNumber = 464,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কুরআনের সবচেয়ে আশাব্যঞ্জক আয়াত; কোনো বান্দাই তওবা করলে আল্লাহর রহমত থেকে বঞ্চিত হয় না।"
                ),
                TopicAyah(
                    surahNumber = 12,
                    ayahNumber = 87,
                    surahNameBn = "সূরা ইউসূফ",
                    surahNameEn = "Yusuf",
                    surahNameAr = "يوسف",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلَا تَيْأَسُوا مِن رَّوْحِ اللَّهِ ۖ إِنَّهُ لَا يَيْأَسُ مِن رَّوْحِ اللَّهِ إِلَّا الْقَوْمُ الْكَافِرُونَ",
                    translationBn = "...এবং তোমরা আল্লাহর রহমত থেকে কখনো হতাশ হয়ো না; নিশ্চয় কাফের সম্প্রদায় ছাড়া আর কেউই আল্লাহর রহমত থেকে হতাশ হয় না।",
                    translationEn = "...and despair not of relief from Allah. Indeed, no one despairs of relief from Allah except the disbelieving people.",
                    juzNumber = 13,
                    pageNumber = 245,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "হযরত ইয়াকূব (আ.) চরম সংকটের মুখেও আল্লাহর অনুগ্রহে অবিচল আশাবাদী ছিলেন।"
                )
            )
        ),

        // 8. সৃষ্টিতত্ত্ব, বিজ্ঞান ও ভ্রূণতত্ত্ব
        QuranTopic(
            id = "topic_embryology_science",
            categoryId = "cat_nature_animals",
            nameBn = "সৃষ্টিতত্ত্ব ও ভ্রূণতত্ত্ব (কুরআনে বিজ্ঞান)",
            nameEn = "Creation, Embryology & Quranic Science",
            nameAr = "الإعجاز العلمي ومراحل خلق الإنسان في الرحم",
            descriptionBn = "মাতৃগর্ভে মানুষের পর্যায়ক্রমিক সৃষ্টি (বীর্যবিন্দু, রক্তপিণ্ড, মাংসপিণ্ড ও অস্থি নির্মাণ) এবং আধুনিক ভ্রূণতত্ত্বের সাথে কুরআনের অলৌকিক সামঞ্জস্য।",
            searchKeywordsBn = listOf("ভ্রূণতত্ত্ব", "বিজ্ঞান", "মানব সৃষ্টি", "মাতৃগর্ভ", "শুক্রবিন্দু", "কুরআনিক বিজ্ঞান", "মু'জিজা"),
            searchKeywordsEn = listOf("embryology", "science in quran", "human creation", "womb", "miracles"),
            isFeatured = true,
            iconEmoji = "🔬",
            relatedTopicIds = listOf("topic_nature_universe", "topic_knowledge_wisdom"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 23,
                    ayahNumber = 12,
                    surahNameBn = "সূরা আল-মুমিনূন",
                    surahNameEn = "Al-Mu'minun",
                    surahNameAr = "المؤمنون",
                    totalAyahsInSurah = 118,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلَقَدْ خَلَقْنَا الْإِنسَانَ مِن سُلَالَةٍ مِّن طِينٍ ۝ ثُمَّ جَعَلْنَاهُ نُطْفَةً فِي قَرَارٍ مَّكِينٍ ۝ ثُمَّ خَلَقْنَا النُّطْفَةَ عَلَقَةً فَخَلَقْنَا الْعَلَقَةَ مُضْغَةً فَخَلَقْنَا الْمُضْغَةَ عِظَامًا فَكَسَوْنَا الْعِظَامَ لَحْمًا ثُمَّ أَنشَأْنَاهُ خَلْقًا آخَرَ ۚ فَتَبَارَكَ اللَّهُ أَحْسَنُ الْخَالِقِينَ",
                    translationBn = "আর নিশ্চয় আমি মানুষকে মাটির সারাংশ থেকে সৃষ্টি করেছি। অতঃপর আমি তাকে এক শুক্রবিন্দুরূপে স্থাপন করেছি এক নিরাপদ আশ্রয়ে (জরায়ুতে)। এরপর আমি শুক্রবিন্দুকে পরিণত করেছি আলাকায় (ঝুলন্ত রক্তপিণ্ড), অতঃপর আলাকাকে পরিণত করেছি মাংসপিণ্ডে, অতঃপর সেই মাংসপিণ্ড থেকে সৃষ্টি করেছি অস্থিপঞ্জর এবং অস্থিপঞ্জরকে আবৃত করেছি গোশত দিয়ে; পরিশেষে তাকে গড়ে তুলেছি এক নতুন সৃষ্টিরূপে। অতএব বরকতময় আল্লাহ, সর্বোত্তম স্রষ্টা!",
                    translationEn = "And certainly did We create man from an extract of clay. Then We placed him as a sperm-drop in a firm lodging. Then We made the sperm-drop into a clinging clot, and We made the clot into a lump [of flesh], and We made [from] the lump, bones, and We covered the bones with flesh; then We developed him into another creation. So blessed is Allah, the best of creators.",
                    juzNumber = 18,
                    pageNumber = 342,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "চৌদ্দশত বছর আগে আধুনিক মাইক্রোস্কোপ ছাড়াই মানব ভ্রূণের পর্যায়ক্রমিক নিখুঁত বিবরণ।"
                )
            )
        ),

        // 9. আসহাবে কাহাফ ও অলৌকিক নিদ্রা
        QuranTopic(
            id = "topic_ashab_al_kahf",
            categoryId = "cat_history",
            nameBn = "আসহাবে কাহাফ ও অলৌকিক নিদ্রা",
            nameEn = "Companions of the Cave (Ashab al-Kahf)",
            nameAr = "أصحاب الكهف والفتية المؤمنون",
            descriptionBn = "ঈমান বাঁচাতে গুহায় আশ্রয় নেওয়া কয়েকজন আত্মত্যাগী যুবকের ঘটনা, তিনশত নয় বছর আল্লাহর কুদরতে নিদ্রিত থাকা এবং ঈমানের অপূর্ব বিজয়।",
            searchKeywordsBn = listOf("আসহাবে কাহাফ", "সূরা কাহাফ", "গুহাবাসী", "যুবক", "কুকুর", "রক্ষা", "ঈমান"),
            searchKeywordsEn = listOf("ashab kahf", "people of cave", "sleepers", "faith", "surah kahf"),
            isFeatured = true,
            iconEmoji = "⛰️",
            relatedTopicIds = listOf("topic_patience", "topic_tawakkul"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 18,
                    ayahNumber = 13,
                    surahNameBn = "সূরা আল-কাহাফ",
                    surahNameEn = "Al-Kahf",
                    surahNameAr = "الكهف",
                    totalAyahsInSurah = 110,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "نَّحْنُ نَقُصُّ عَلَيْكَ نَبَأَهُم بِالْحَقِّ ۚ إِنَّهُمْ فِتْيَةٌ آمَنُوا بِرَبِّهِمْ وَزِدْنَاهُمْ هُدًى ۝ وَرَبَطْنَا عَلَىٰ قُلُوبِهِمْ إِذْ قَامُوا فَقَالُوا رَبُّنَا رَبُّ السَّمَاوَاتِ وَالْأَرْضِ لَن نَّدْعُوَ مِن دُونِهِ إِلَٰهًا",
                    translationBn = "আমি আপনার কাছে তাদের সত্য বিবরণ বর্ণনা করছি: নিশ্চয় তারা ছিল কয়েকজন যুবক, যারা তাদের প্রতিপালকের প্রতি ঈমান এনেছিল এবং আমি তাদের হেদায়েত আরও বাড়িয়ে দিয়েছিলাম। আর আমি তাদের হৃদয়কে সুদৃঢ় করেছিলাম যখন তারা দাঁড়িয়ে ঘোষণা করেছিল: 'আমাদের প্রতিপালক তো আকাশমন্ডলী ও পৃথিবীর প্রতিপালক; আমরা কখনই তাঁকে ছাড়া অন্য কোনো উপাস্যকে ডাকব না।' ",
                    translationEn = "We narrate to you their story in truth. Indeed, they were youths who believed in their Lord, and We increased them in guidance. And We made firm their hearts when they stood up and said, 'Our Lord is the Lord of the heavens and the earth. Never will we invoke besides Him any deity.'",
                    juzNumber = 15,
                    pageNumber = 294,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "যেকোনো সমাজ বা প্রতিকূল পরিবেশে যুবকদের ঈমানদার হওয়া ও আল্লাহর কুদরতের ওপর ভরসা রাখার অনুপম দৃষ্টান্ত।"
                )
            )
        ),

        // 10. মুসলিম ভ্রাতৃত্ব ও উম্মাহর ঐক্য
        QuranTopic(
            id = "topic_brotherhood_unity",
            categoryId = "cat_society",
            nameBn = "মুসলিম ভ্রাতৃত্ব ও উম্মাহর ঐক্য",
            nameEn = "Islamic Brotherhood & Unity of the Ummah",
            nameAr = "الأخوة الإسلامية ووحدة الأمة والاعتصام بحبل الله",
            descriptionBn = "সকল মুমিন পরস্পর ভাই ভাই। আল্লাহর রজ্জু দৃঢ়ভাবে ধারণ করা এবং অনৈক্য ও দলাদলি বর্জনের নির্দেশ।",
            searchKeywordsBn = listOf("ভ্রাতৃত্ব", "ঐক্য", "উম্মাহ", "মুমিন ভাই", "এক দেহ", "আল্লাহর রজ্জু", "মিলন"),
            searchKeywordsEn = listOf("brotherhood", "unity", "ummah", "harmony", "rope of allah"),
            isFeatured = true,
            iconEmoji = "🤝",
            relatedTopicIds = listOf("topic_justice_witness", "topic_forgiveness_anger"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 49,
                    ayahNumber = 10,
                    surahNameBn = "সূরা আল-হুজুরাত",
                    surahNameEn = "Al-Hujurat",
                    surahNameAr = "الحجرات",
                    totalAyahsInSurah = 18,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِنَّمَا الْمُؤْمِنُونَ إِخْوَةٌ فَأَصْلِحُوا بَيْنَ أَخَوَيْكُمْ ۚ وَاتَّقُوا اللَّهَ لَعَلَّكُمْ تُرْحَمُونَ",
                    translationBn = "নিশ্চয় সমস্ত মুমিন তো পরস্পর ভাই ভাই; কাজেই তোমরা তোমাদের ভাইদের মধ্যে মীমাংসা ও শান্তি স্থাপন কর এবং আল্লাহকে ভয় কর, যেন তোমরা অনুগৃহীত হতে পার।",
                    translationEn = "The believers are but brothers, so make settlement between your brothers. And fear Allah that you may receive mercy.",
                    juzNumber = 26,
                    pageNumber = 516,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ঈমানি ভ্রাতৃত্ব রক্তসম্পর্কের চেয়েও দৃঢ়; পারস্পরিক বিরোধ মেটানো সমষ্টিগত দায়িত্ব।"
                ),
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 103,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَاعْتَصِمُوا بِحَبْلِ اللَّهِ جَمِيعًا وَلَا تَفَرَّقُوا ۚ وَاذْكُرُوا نِعْمَتَ اللَّهِ عَلَيْكُمْ إِذْ كُنتُمْ أَعْدَاءً فَأَلَّفَ بَيْنَ قُلُوبِكُمْ فَأَصْبَحْتُم بِنِعْمَتِهِ إِخْوَانًا",
                    translationBn = "আর তোমরা সকলে মিলে আল্লাহর রজ্জু (কুরআন ও দ্বীন) দৃঢ়ভাবে আঁকড়ে ধর এবং পরস্পরে বিভক্ত হয়ো না। আর স্মরণ কর তোমাদের ওপর আল্লাহর সেই নিয়ামত, যখন তোমরা একে অপরের শত্রু ছিলে, অতঃপর তিনি তোমাদের অন্তরে প্রীতি সঞ্চার করলেন, ফলে তাঁর অনুগ্রহে তোমরা পরস্পরে ভাই হয়ে গেলে।",
                    translationEn = "And hold firmly to the rope of Allah all together and do not become divided. And remember the favor of Allah upon you - when you were enemies and He brought your hearts together and you became, by His favor, brothers.",
                    juzNumber = 4,
                    pageNumber = 63,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ঐক্যবদ্ধ থাকার ঐশী নির্দেশ; অনৈক্য ও বিভক্তি জাতিকে ধ্বংসের মুখে ঠেলে দেয়।"
                )
            )
        )
    )
}
