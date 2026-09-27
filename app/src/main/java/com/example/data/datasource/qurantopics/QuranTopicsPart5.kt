package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart5 {

    val topics: List<QuranTopic> = listOf(
        // 1. সত্যবাদিতা ও প্রতিশ্রুতি রক্ষা
        QuranTopic(
            id = "topic_truthfulness_promises",
            categoryId = "cat_akhlaq",
            nameBn = "সত্যবাদিতা ও প্রতিশ্রুতি রক্ষা",
            nameEn = "Truthfulness & Fulfilling Pledges",
            nameAr = "الصدق والوفاء بالعهود",
            descriptionBn = "সততা মুমিনের ভূষণ। সর্বাবস্থায় সত্য বলা, মিথ্যা থেকে বিরত থাকা এবং প্রদত্ত ওয়াদা ও চুক্তি নিষ্ঠার সাথে পূরণ করার তাগিদ।",
            searchKeywordsBn = listOf("সত্য", "সত্যবাদী", "সততা", "অঙ্গীকার", "ওয়াদা", "প্রতিশ্রুতি", "আমানত", "চুক্তি"),
            searchKeywordsEn = listOf("truthfulness", "honesty", "promises", "covenants", "pledges", "integrity"),
            isFeatured = true,
            iconEmoji = "🎯",
            relatedTopicIds = listOf("topic_patience", "topic_justice_witness"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 9,
                    ayahNumber = 119,
                    surahNameBn = "সূরা আত-তাওবাহ",
                    surahNameEn = "At-Tawbah",
                    surahNameAr = "التوبة",
                    totalAyahsInSurah = 129,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا اتَّقُوا اللَّهَ وَكُونُوا مَعَ الصَّادِقِينَ",
                    translationBn = "হে ঈমানদারগণ! তোমরা আল্লাহকে ভয় কর এবং সত্যবাদীদের সঙ্গী হও।",
                    translationEn = "O you who have believed, fear Allah and be with those who are true.",
                    juzNumber = 11,
                    pageNumber = 206,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "তাকওয়া এবং সততা একে অপরের পরিপূরক; সৎ সঙ্গ মানুষকে সত্যের ওপর অবিচল রাখে।"
                ),
                TopicAyah(
                    surahNumber = 17,
                    ayahNumber = 34,
                    surahNameBn = "সূরা আল-ইসরা",
                    surahNameEn = "Al-Isra",
                    surahNameAr = "الإسراء",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَأَوْفُوا بِالْعَهْدِ ۖ إِنَّ الْعَهْدَ كَانَ مَسْئُولًا",
                    translationBn = "এবং প্রতিশ্রুতি পূর্ণ কর; নিশ্চয় প্রতিশ্রুতি সম্পর্কে (কিয়ামতের দিন) জিজ্ঞাসাবাদ করা হবে।",
                    translationEn = "And fulfill [every] commitment. Indeed, the commitment is ever [that about which one will be] questioned.",
                    juzNumber = 15,
                    pageNumber = 285,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কথা দিয়ে কথা রাখা মুমিনের অপরিহার্য চরিত্র; ভঙ্গ করা মুনাফিকের আলামত।"
                )
            )
        ),

        // 2. বিনয় ও অহংকার বর্জন
        QuranTopic(
            id = "topic_humility_arrogance",
            categoryId = "cat_akhlaq",
            nameBn = "বিনয় ও অহংকার বর্জন",
            nameEn = "Humility vs Arrogance",
            nameAr = "التواضع والنهي عن الكبر والخيلاء",
            descriptionBn = "অহংকার জান্নাত থেকে বঞ্চিতকারী ধ্বংসাত্মক ব্যাধি। রহমানের প্রিয় বান্দাদের বৈশিষ্ট্য হলো জমিনে বিনম্রভাবে চলাফেরা করা।",
            searchKeywordsBn = listOf("বিনয়", "নম্রতা", "অহংকার", "দম্ভ", "গর্ব", "তাকাব্বুর", "উদ্ধত"),
            searchKeywordsEn = listOf("humility", "arrogance", "pride", "modesty", "haughtiness"),
            isFeatured = true,
            iconEmoji = "🌱",
            relatedTopicIds = listOf("topic_patience", "topic_forgiveness_anger"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 25,
                    ayahNumber = 63,
                    surahNameBn = "সূরা আল-ফুরকান",
                    surahNameEn = "Al-Furqan",
                    surahNameAr = "الفرقان",
                    totalAyahsInSurah = 77,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَعِبَادُ الرَّحْمَٰنِ الَّذِينَ يَمْشُونَ عَلَى الْأَرْضِ هَوْنًا وَإِذَا خَاطَبَهُمُ الْجَاهِلُونَ قَالُوا سَلَامًا",
                    translationBn = "আর দয়াময় রহমানের খাঁটি বান্দা তারাই, যারা পৃথিবীতে অত্যন্ত বিনম্রভাবে চলাফেরা করে এবং অজ্ঞ লোকেরা যখন তাদের কটূক্তি করে কথা বলে, তখন তারা শান্তভাবে বলে 'সালাম' (শান্তি)।",
                    translationEn = "And the servants of the Most Merciful are those who walk upon the earth easily, and when the ignorant address them [harshly], they say [words of] peace.",
                    juzNumber = 19,
                    pageNumber = 365,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ইবাদুর রহমানের প্রধান চিহ্ন বিনম্র আচরণ ও মূর্খদের প্ররোচনায় সংযম রক্ষা করা।"
                ),
                TopicAyah(
                    surahNumber = 31,
                    ayahNumber = 18,
                    surahNameBn = "সূরা লুকমান",
                    surahNameEn = "Luqman",
                    surahNameAr = "لقمان",
                    totalAyahsInSurah = 34,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلَا تُصَعِّرْ خَدَّكَ لِلنَّاسِ وَلَا تَمْشِ فِي الْأَرْضِ مَرَحًا ۖ إِنَّ اللَّهَ لَا يُحِبُّ كُلَّ مُخْتَالٍ فَخُورٍ",
                    translationBn = "আর মানুষের দিক থেকে অহংকারে তোমার মুখ ফিরিয়ে নিয়ো না এবং পৃথিবীতে গর্বভরে পদচারণা করো না; নিশ্চয় আল্লাহ কোনো দাম্ভিক ও অহংকারীকে ভালোবাসেন না।",
                    translationEn = "And do not turn your cheek [in contempt] toward people and do not walk through the earth exultantly. Indeed, Allah does not like everyone self-deluded and boastful.",
                    juzNumber = 21,
                    pageNumber = 412,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "হযরত লুকমানের নসীহত: সাধারণ মানুষের সাথে আচরণে অহংকার প্রদর্শন সম্পূর্ণ বর্জনীয়।"
                )
            )
        ),

        // 3. ক্ষমা, সহনশীলতা ও রাগ নিয়ন্ত্রণ
        QuranTopic(
            id = "topic_forgiveness_anger",
            categoryId = "cat_akhlaq",
            nameBn = "ক্ষমা, সহনশীলতা ও রাগ নিয়ন্ত্রণ",
            nameEn = "Forgiveness & Controlling Anger",
            nameAr = "العفو وكظم الغيظ والإحسان",
            descriptionBn = "অন্যায়কে ক্ষমার চাদরে ঢেকে দেওয়া এবং ক্রোধের মুহূর্তে নিজেকে সংযত রাখা মহৎ চরিত্রের পরিচয়, যা আল্লাহর বিশেষ ভালোবাসা এনে দেয়।",
            searchKeywordsBn = listOf("ক্ষমা", "রাগ নিয়ন্ত্রণ", "সহনশীলতা", "ধৈর্য", "ইহসান", "মাফ", "ক্রোধ"),
            searchKeywordsEn = listOf("forgiveness", "anger control", "patience", "pardoning", "clemency"),
            isFeatured = true,
            iconEmoji = "🕊️",
            relatedTopicIds = listOf("topic_patience", "topic_humility_arrogance"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 134,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "الَّذِينَ يُنفِقُونَ فِي السَّرَّاءِ وَالضَّرَّاءِ وَالْكَاظِمِينَ الْغَيْظَ وَالْعَافِينَ عَنِ النَّاسِ ۗ وَاللَّهُ يُحِبُّ الْمُحْسِنِينَ",
                    translationBn = "যারা সচ্ছল ও অসচ্ছল উভয় অবস্থায় আল্লাহর পথে ব্যয় করে, যারা নিজেদের ক্রোধ সংবরণ করে এবং মানুষের অপরাধ ক্ষমা করে দেয়; আর আল্লাহ সৎকর্মশীলদের ভালোবাসেন।",
                    translationEn = "Who spend [in the cause of Allah] during ease and hardship and who restrain anger and who pardon the people - and Allah loves the doers of good.",
                    juzNumber = 4,
                    pageNumber = 67,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মুত্তাকীদের সর্বোচ্চ গুণ রাগ দমন করে শত্রুকেও উদারচিত্তে ক্ষমা করে দেওয়া।"
                ),
                TopicAyah(
                    surahNumber = 41,
                    ayahNumber = 34,
                    surahNameBn = "সূরা ফুসসিলাত",
                    surahNameEn = "Fussilat",
                    surahNameAr = "فصلت",
                    totalAyahsInSurah = 54,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلَا تَسْتَوِي الْحَسَنَةُ وَلَا السَّيِّئَةُ ۚ ادْفَعْ بِالَّتِي هِيَ أَحْسَنُ فَإِذَا الَّذِي بَيْنَكَ وَبَيْنَهُ عَدَاوَةٌ كَأَنَّهُ وَلِيٌّ حَمِيمٌ",
                    translationBn = "ভালো ও মন্দ সমান হতে পারে না। তুমি মন্দকে প্রতিহত কর তা দিয়ে যা সর্বোৎকৃষ্ট; ফলে তোমার সাথে যার শত্রুতা ছিল, সে অকস্মাৎ অন্তরঙ্গ বন্ধুর মতো হয়ে যাবে।",
                    translationEn = "And not equal are the good deed and the bad. Repel [evil] by that [deed] which is better; and thereupon the one whom between you and him was enmity [will become] as though he was a devoted friend.",
                    juzNumber = 24,
                    pageNumber = 480,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মন্দের বদলে ভালো ব্যবহারের মাধ্যমে চরম শত্রুও পরম মিত্রে পরিণত হয়।"
                )
            )
        ),

        // 4. গীবত, অপবাদ ও মিথ্যা রটনা বর্জন
        QuranTopic(
            id = "topic_backbiting_slander",
            categoryId = "cat_akhlaq",
            nameBn = "গীবত, অপবাদ ও উপহাস বর্জন",
            nameEn = "Prohibition of Backbiting, Slander & Mockery",
            nameAr = "النهي عن الغيبة والنميمة والسخرية",
            descriptionBn = "কারো অনুপস্থিতিতে নিন্দা (গীবত) মৃত ভাইয়ের গোশত খাওয়ার মতো ঘৃণ্য। মানুষকে নিয়ে উপহাস, কুধারণা ও গোয়েন্দাগিরি নিষিদ্ধ।",
            searchKeywordsBn = listOf("গীবত", "চোগলখোরী", "অপবাদ", "মিথ্যা", "উপহাস", "কুধারণা", "দোষ খোঁজা"),
            searchKeywordsEn = listOf("backbiting", "slander", "mockery", "gheebah", "suspicion", "spying"),
            isFeatured = true,
            iconEmoji = "🚫",
            relatedTopicIds = listOf("topic_truthfulness_promises", "topic_brotherhood_unity"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 49,
                    ayahNumber = 12,
                    surahNameBn = "সূরা আল-হুজুরাত",
                    surahNameEn = "Al-Hujurat",
                    surahNameAr = "الحجرات",
                    totalAyahsInSurah = 18,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا اجْتَنِبُوا كَثِيرًا مِّنَ الظَّنِّ إِنَّ بَعْضَ الظَّنِّ إِثْمٌ ۖ وَلَا تَجَسَّسُوا وَلَا يَغْتَب بَّعْضُكُم بَعْضًا ۚ أَيُحِبُّ أَحَدُكُمْ أَن يَأْكُلَ لَحْمَ أَخِيهِ مَيْتًا فَكَرِهْتُمُوهُ ۚ وَاتَّقُوا اللَّهَ",
                    translationBn = "হে ঈমানদারগণ! তোমরা অধিকাংশ অনুমান থেকে দূরে থাক; কারণ কোনো কোনো অনুমান তো পাপ। আর তোমরা একে অপরের গোপন দোষ অনুসন্ধান করো না এবং কেউ যেন কারো পেছনে গীবত না করে। তোমাদের কেউ কি তার মৃত ভাইয়ের গোশত খেতে পছন্দ করবে? তোমরা তো তা অপছন্দই কর। আর তোমরা আল্লাহকে ভয় কর।",
                    translationEn = "O you who have believed, avoid much [negative] assumption. Indeed, some assumption is sin. And do not spy or backbite each other. Would one of you like to eat the flesh of his brother when dead? You would detest it. And fear Allah.",
                    juzNumber = 26,
                    pageNumber = 517,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কুরআনে গীবতের এমন এক জীবন্ত ও লোমহর্ষক তুলনা দেওয়া হয়েছে যা মানুষের বিবেককে নাড়া দেয়।"
                ),
                TopicAyah(
                    surahNumber = 104,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আল-হুমাযাহ",
                    surahNameEn = "Al-Humazah",
                    surahNameAr = "الهمزة",
                    totalAyahsInSurah = 9,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَيْلٌ لِّكُلِّ هُمَزَةٍ لُّمَزَةٍ",
                    translationBn = "ধ্বংস ও দুর্ভোগ প্রত্যেকের জন্য, যে সামনাসামনি মানুষকে খোঁটা দেয় ও পেছনে পরনিন্দা করে।",
                    translationEn = "Woe to every scorner and mocker.",
                    juzNumber = 30,
                    pageNumber = 601,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মানুষের আত্মমর্যাদায় আঘাতকারী ও পেছনে কূটনামিকারীদের জন্য জাহান্নামের সতর্কবার্তা।"
                )
            )
        ),

        // 5. এতিম ও মিসকিনের অধিকার
        QuranTopic(
            id = "topic_orphans_needy",
            categoryId = "cat_family",
            nameBn = "এতিম ও মিসকিনের অধিকার",
            nameEn = "Rights of Orphans & The Needy",
            nameAr = "حقوق اليتامى والمساكين",
            descriptionBn = "এতিমের সম্পদ আত্মসাৎ করা পেটে আগুন ভরার সমতুল্য। অসহায়, অনাথ ও মিসকিনের প্রতি কোমল আচরণ ও পূর্ণাঙ্গ সুরক্ষার নির্দেশ।",
            searchKeywordsBn = listOf("এতিম", "অনাথ", "মিসকিন", "সম্পদ গ্রাস", "সহায় সম্বলহীন", "দরিদ্র"),
            searchKeywordsEn = listOf("orphans", "needy", "yatim", "charity", "vulnerable"),
            isFeatured = true,
            iconEmoji = "🤲",
            relatedTopicIds = listOf("topic_zakat_sadaqah", "topic_parents"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 10,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِنَّ الَّذِينَ يَأْكُلُونَ أَمْوَالَ الْيَتَامَىٰ ظُلْمًا إِنَّمَا يَأْكُلُونَ فِي بُطُونِهِمْ نَارًا ۖ وَسَيَصْلَوْنَ سَعِيرًا",
                    translationBn = "নিশ্চয় যারা অন্যায়ভাবে এতিমদের ধন-সম্পদ ভক্ষণ করে, তারা নিজেদের পেটে আগুনই ভর্তি করছে; এবং অচিরেই তারা জ্বলন্ত আগুনে প্রবেশ করবে।",
                    translationEn = "Indeed, those who devour the property of orphans unjustly are only consuming into their bellies fire. And they will be burned in a Blaze.",
                    juzNumber = 4,
                    pageNumber = 78,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "এতিমের হক নষ্ট করার কঠোরতম পরিণাম কুরআনে সুস্পষ্টভাবে সতর্ক করা হয়েছে।"
                ),
                TopicAyah(
                    surahNumber = 93,
                    ayahNumber = 9,
                    surahNameBn = "সূরা আদ-দুহা",
                    surahNameEn = "Ad-Duha",
                    surahNameAr = "الضحى",
                    totalAyahsInSurah = 11,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "فَأَمَّا الْيَتِيمَ فَلَا تَقْهَرْ ۝ وَأَمَّا السَّائِلَ فَلَا تَنْهَرْ",
                    translationBn = "অতএব আপনি এতিমের প্রতি কঠোর হবেন না, আর সাহায্যপ্রার্থীকে ধমক দেবেন না।",
                    translationEn = "So as for the orphan, do not oppress him. And as for the petitioner, do not repel [him].",
                    juzNumber = 30,
                    pageNumber = 596,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "রাসুলুল্লাহ (সা.) নিজেও এতিম ছিলেন; ফলে এতিম ও দরিদ্রের ব্যথায় সান্ত্বনা দেওয়া ইসলামের মূল চেতনা।"
                )
            )
        ),

        // 6. নারীর সম্মান, মর্যাদা ও পর্দা
        QuranTopic(
            id = "topic_women_rights_hijab",
            categoryId = "cat_family",
            nameBn = "নারীর মর্যাদা, অধিকার ও পর্দা",
            nameEn = "Women's Dignity, Rights & Modesty (Hijab)",
            nameAr = "مكانة المرأة وحقوقها والحجاب",
            descriptionBn = "ইসলামে নারীর স্বতন্ত্র মর্যাদা, সম্পদে অধিকার, বিবাহে সম্মতি, সুরক্ষা এবং নারী ও পুরুষ উভয়ের জন্য শালীনতা ও পর্দার বিধান।",
            searchKeywordsBn = listOf("নারী", "মা", "স্ত্রী", "কন্যা", "পর্দা", "হিজাব", "শালীনতা", "নারীর অধিকার"),
            searchKeywordsEn = listOf("women in islam", "hijab", "modesty", "women rights", "chastity"),
            isFeatured = true,
            iconEmoji = "🧕",
            relatedTopicIds = listOf("topic_marriage", "topic_parents"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 33,
                    ayahNumber = 59,
                    surahNameBn = "সূরা আল-আহযাব",
                    surahNameEn = "Al-Ahzab",
                    surahNameAr = "الأحزاب",
                    totalAyahsInSurah = 73,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا النَّبِيُّ قُل لِّأَزْوَاجِكَ وَبَنَاتِكَ وَنِسَاءِ الْمُؤْمِنِينَ يُدْنِينَ عَلَيْهِنَّ مِن جَلَابِيبِهِنَّ ۚ ذَٰلِكَ أَدْنَىٰ أَن يُعْرَفْنَ فَلَا يُؤْذَيْنَ ۗ وَكَانَ اللَّهُ غَفُورًا رَّحِيمًا",
                    translationBn = "হে নবী! আপনি আপনার স্ত্রীগণকে, কন্যাগণকে ও মুমিনদের নারীদেরকে বলুন, তারা যেন নিজেদের ওপর তাদের চাদরের কিয়দংশ নামিয়ে দেয়। এতে তাদেরকে সহজে চেনা যাবে, ফলে তাদেরকে উত্ত্যক্ত করা হবে না। আর আল্লাহ ক্ষমাশীল, পরম দয়ালু।",
                    translationEn = "O Prophet, tell your wives and your daughters and the women of the believers to bring down over themselves [part] of their outer garments. That is more suitable that they will be known and not be abused. And ever is Allah Forgiving and Merciful.",
                    juzNumber = 22,
                    pageNumber = 426,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "পর্দা নারীর জন্য কোনো বন্দিদশা নয়, বরং এটি তার সম্মান, নিরাপত্তা ও স্বাতন্ত্র্যের সুরক্ষাকবচ।"
                ),
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 19,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَعَاشِرُوهُنَّ بِالْمَعْرُوفِ ۚ فَإِن كَرِهْتُمُوهُنَّ فَعَسَىٰ أَن تَكْرَهُوا شَيْئًا وَيَجْعَلَ اللَّهُ فِيهِ خَيْرًا كَثِيرًا",
                    translationBn = "এবং তোমরা তাদের (নারীদের) সাথে সদ্ভাবে ও সুন্দরভাবে জীবনযাপন কর। আর যদি তোমরা তাদেরকে অপছন্দ কর, তবে হতে পারে তোমরা এমন একটি জিনিসকে অপছন্দ করছ যাতে আল্লাহ বিপুল কল্যাণ রেখেছেন।",
                    translationEn = "And live with them in kindness. For if you dislike them - perhaps you dislike a thing and Allah makes therein much good.",
                    juzNumber = 4,
                    pageNumber = 80,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "স্ত্রীদের সাথে উত্তম আচরণ ও সদাচারের নির্দেশ; পারিবারিক সম্প্রীতির মূলভিত্তি।"
                )
            )
        ),

        // 7. হালাল খাদ্য ও হারাম বর্জন
        QuranTopic(
            id = "topic_halal_food",
            categoryId = "cat_halal_haram",
            nameBn = "হালাল খাদ্য ও হারাম বর্জন",
            nameEn = "Halal Food & Lawful Sustenance",
            nameAr = "الطيبات من الرزق والمحرمات",
            descriptionBn = "পবিত্র ও হালাল বস্তু ভক্ষণ মানব দেহের আধ্যাত্মিক উন্নতির জন্য অপরিহার্য। মৃত প্রাণী, রক্ত, শূকরের মাংস এবং আল্লাহর নাম ছাড়া জবেহকৃত পশুর নিষেধাজ্ঞা।",
            searchKeywordsBn = listOf("হালাল খাদ্য", "হারাম খাবার", "শূকর", "রক্ত", "মৃত প্রাণী", "তৈয়ব", "পবিত্র খাবার"),
            searchKeywordsEn = listOf("halal food", "dietary laws", "pork prohibition", "carrion", "pure food"),
            isFeatured = false,
            iconEmoji = "🥗",
            relatedTopicIds = listOf("topic_rizq", "topic_alcohol_gambling"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 168,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا النَّاسُ كُلُوا مِمَّا فِي الْأَرْضِ حَلَالًا طَيِّبًا وَلَا تَتَّبِعُوا خُطُوَاتِ الشَّيْطَانِ ۚ إِنَّهُ لَكُمْ عَدُوٌّ مُّبِينٌ",
                    translationBn = "হে মানবজাতি! জমিনে যা কিছু হালাল ও পবিত্র রয়েছে তা থেকে আহার কর এবং শয়তানের পদচিহ্ন অনুসরণ করো না; নিশ্চয় সে তোমাদের প্রকাশ্য শত্রু।",
                    translationEn = "O mankind, eat from whatever is on earth [that is] lawful and good and do not follow the footsteps of Satan. Indeed, he is to you a clear enemy.",
                    juzNumber = 2,
                    pageNumber = 25,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "হালাল ও পবিত্র খাদ্য অন্তরে তাকওয়া সৃষ্টি করে এবং দো'আ কবুলের প্রধান শর্ত।"
                ),
                TopicAyah(
                    surahNumber = 5,
                    ayahNumber = 3,
                    surahNameBn = "সূরা আল-মায়িদাহ",
                    surahNameEn = "Al-Ma'idah",
                    surahNameAr = "المائدة",
                    totalAyahsInSurah = 120,
                    revelationTypeBn = "মাদানী",
                    arabicText = "حُرِّمَتْ عَلَيْكُمُ الْمَيْتَةُ وَالدَّمُ وَلَحْمُ الْخِنزِيرِ وَمَا أُهِلَّ لِغَيْرِ اللَّهِ بِهِ",
                    translationBn = "তোমাদের জন্য হারাম করা হয়েছে মৃত জন্তু, রক্ত, শূকরের গোশত এবং যেসব পশু আল্লাহর নাম ছাড়া অন্যের নামে জবেহ করা হয়েছে...",
                    translationEn = "Prohibited to you are dead animals, blood, the flesh of swine, and that which has been dedicated to other than Allah...",
                    juzNumber = 6,
                    pageNumber = 106,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ইসলামে নিষিদ্ধ খাদ্যসমূহের সুস্পষ্ট তালিকা, যা মানব স্বাস্থ্যের জন্যও ক্ষতিকর।"
                )
            )
        ),

        // 8. মদ ও জুয়ার নিষেধাজ্ঞা
        QuranTopic(
            id = "topic_alcohol_gambling",
            categoryId = "cat_halal_haram",
            nameBn = "মদ ও জুয়ার নিষেধাজ্ঞা",
            nameEn = "Prohibition of Alcohol & Gambling",
            nameAr = "تحريم الخمر والميسر والأنصاب",
            descriptionBn = "মাদকদ্রব্য ও জুয়া শয়তানের অপবিত্র চক্রান্ত, যা মানুষের মধ্যে পারস্পরিক শত্রুতা ও বিদ্বেষ সৃষ্টি করে এবং আল্লাহর স্মরণ থেকে গাফেল রাখে।",
            searchKeywordsBn = listOf("মদ", "জুয়া", "মাদক", "নেশা", "লটারি", "শয়তানের কাজ", "খামর"),
            searchKeywordsEn = listOf("alcohol prohibition", "gambling", "intoxicants", "khamr", "maysir"),
            isFeatured = false,
            iconEmoji = "🚫",
            relatedTopicIds = listOf("topic_halal_food", "topic_chastity_zina"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 5,
                    ayahNumber = 90,
                    surahNameBn = "সূরা আল-মায়িদাহ",
                    surahNameEn = "Al-Ma'idah",
                    surahNameAr = "المائدة",
                    totalAyahsInSurah = 120,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِنَّمَا الْخَمْرُ وَالْمَيْسِرُ وَالْأَنصَابُ وَالْأَزْلَامُ رِجْسٌ مِّنْ عَمَلِ الشَّيْطَانِ فَاجْتَنِبُوهُ لَعَلَّكُمْ تُفْلِحُونَ",
                    translationBn = "হে ঈমানদারগণ! মদ, জুয়া, পূজার বেদি ও ভাগ্য নির্ণায়ক তীর—এসবই ঘৃণ্য ও শয়তানের কাজ। সুতরাং তোমরা এসব থেকে সম্পূর্ণরূপে বিরত থাক, যেন তোমরা সফলকাম হতে পার।",
                    translationEn = "O you who have believed, indeed, intoxicants, gambling, [sacrificing on] stone alters [to other than Allah], and divining arrows are but defilement from the work of Satan, so avoid it that you may be successful.",
                    juzNumber = 7,
                    pageNumber = 123,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মদ ও জুয়াকে কুরআনে স্পষ্ট ভাষায় শয়তানের নোংরা কাজ বলে ঘোষণা করে চিরতরে বর্জনের নির্দেশ দেওয়া হয়েছে।"
                )
            )
        ),

        // 9. ব্যভিচার বর্জন ও সতীত্ব রক্ষা
        QuranTopic(
            id = "topic_chastity_zina",
            categoryId = "cat_halal_haram",
            nameBn = "ব্যভিচার বর্জন ও সতীত্ব রক্ষা",
            nameEn = "Chastity & Prohibition of Zina (Fornication)",
            nameAr = "حفظ الفروج والنهي عن الزنا والفواحش",
            descriptionBn = "ব্যভিচার চরম জঘন্য পাপ ও পারিবারিক ধ্বংসের পথ। দৃষ্টি সংযত রাখা এবং ব্যভিচারের নিকটবর্তী না হওয়ার সুস্পষ্ট নির্দেশ।",
            searchKeywordsBn = listOf("ব্যভিচার", "জিনা", "সতীত্ব", "চরিত্র রক্ষা", "দৃষ্টি সংযম", "অশ্লীলতা"),
            searchKeywordsEn = listOf("chastity", "zina", "fornication", "adultery", "lowering gaze", "modesty"),
            isFeatured = true,
            iconEmoji = "🛡️",
            relatedTopicIds = listOf("topic_marriage", "topic_women_rights_hijab"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 17,
                    ayahNumber = 32,
                    surahNameBn = "সূরা আল-ইসরা",
                    surahNameEn = "Al-Isra",
                    surahNameAr = "الإسراء",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلَا تَقْرَبُوا الزِّنَا ۖ إِنَّهُ كَانَ فَاحِشَةً وَسَاءَ سَبِيلًا",
                    translationBn = "আর তোমরা ব্যভিচারের কাছেও যেও না; নিশ্চয় তা এক নির্লজ্জ অশ্লীল কাজ এবং নিকৃষ্টতম পথ।",
                    translationEn = "And do not approach unlawful sexual intercourse. Indeed, it is ever an immorality and is evil as a way.",
                    juzNumber = 15,
                    pageNumber = 285,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ব্যভিচার কেবল সংঘটন নয়, এর দিকে নিয়ে যায় এমন সব পথ ও উপকরণ থেকেও ইসলাম কঠোরভাবে নিষেধ করেছে।"
                ),
                TopicAyah(
                    surahNumber = 24,
                    ayahNumber = 30,
                    surahNameBn = "সূরা আন-নূর",
                    surahNameEn = "An-Nur",
                    surahNameAr = "النور",
                    totalAyahsInSurah = 64,
                    revelationTypeBn = "মাদানী",
                    arabicText = "قُل لِّلْمُؤْمِنِينَ يَغُضُّوا مِنْ أَبْصَارِهِمْ وَيَحْفَظُوا فُرُوجَهُمْ ۚ ذَٰلِكَ أَزْكَىٰ لَهُمْ ۗ إِنَّ اللَّهَ خَبِيرٌ بِمَا يَصْنَعُونَ",
                    translationBn = "মুমিন পুরুষদেরকে বলুন, তারা যেন তাদের দৃষ্টিকে সংযত রাখে এবং তাদের লজ্জাস্থানের হেফাজত করে; এটা তাদের জন্য অধিকতর পবিত্র। তারা যা করে নিশ্চয় আল্লাহ সে বিষয়ে সম্যক অবহিত।",
                    translationEn = "Tell the believing men to reduce [some] of their vision and guard their private parts. That is purer for them. Indeed, Allah is Acquainted with what they do.",
                    juzNumber = 18,
                    pageNumber = 353,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "সতীত্ব রক্ষার প্রথম ও প্রধান শর্ত হলো চোখের দৃষ্টির পবিত্রতা রক্ষা করা।"
                )
            )
        ),

        // 10. ব্যবসায় সততা ও মাপে সঠিক থাকা
        QuranTopic(
            id = "topic_trade_weights",
            categoryId = "cat_wealth",
            nameBn = "ব্যবসায় সততা ও মাপে সঠিক থাকা",
            nameEn = "Honesty in Trade & Fair Weights",
            nameAr = "التجارة الصادقة وإيفاء الكيل والميزان",
            descriptionBn = "ব্যবসায়ীদের জন্য সততা অপরিহার্য। ওজনে কম দেওয়া মহাপাপ ও পূর্ববর্তী জাতিদের ধ্বংসের কারণ।",
            searchKeywordsBn = listOf("ব্যবসা", "মাপ", "ওজন", "ন্যায্য মূল্য", "প্রতারণা বর্জন", "সৎ ব্যবসায়ী"),
            searchKeywordsEn = listOf("trade", "business ethics", "fair weights", "mutaffifeen", "scales"),
            isFeatured = false,
            iconEmoji = "⚖️",
            relatedTopicIds = listOf("topic_rizq", "topic_usury_riba"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 83,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আল-মুতাফফিফীন",
                    surahNameEn = "Al-Mutaffifin",
                    surahNameAr = "المطففين",
                    totalAyahsInSurah = 36,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَيْلٌ لِّلْمُطَفِّفِينَ ۝ الَّذِينَ إِذَا اكْتَالُوا عَلَى النَّاسِ يَسْتَوْفُونَ ۝ وَإِذَا كَالُوهُمْ أَو وَّزَنُوهُمْ يُخْسِرُونَ",
                    translationBn = "ধ্বংস তাদের জন্য যারা মাপে কম দেয়! যারা মানুষের কাছ থেকে মেপে নেওয়ার সময় পূর্ণমাত্রায় নেয়, আর যখন অন্যদের মেপে বা ওজন করে দেয়, তখন কম দেয়।",
                    translationEn = "Woe to those who give less [than due], Who, when they take a measure from people, take in full. But if they give by measure or by weight to them, they cause loss.",
                    juzNumber = 30,
                    pageNumber = 587,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ব্যবসায়ে পণ্যদ্রব্য কম দেওয়া আত্মসাতের শামিল এবং এর জন্য কঠিন শাস্তির ঘোষণা।"
                ),
                TopicAyah(
                    surahNumber = 17,
                    ayahNumber = 35,
                    surahNameBn = "সূরা আল-ইসরা",
                    surahNameEn = "Al-Isra",
                    surahNameAr = "الإسراء",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَأَوْفُوا الْكَيْلَ إِذَا كِلْتُمْ وَزِنُوا بِالْقِسْطَاسِ الْمُسْتَقِيمِ ۚ ذَٰلِكَ خَيْرٌ وَأَحْسَنُ تَأْوِيلًا",
                    translationBn = "আর যখন মেপে দেবে তখন পূর্ণ মাপে দেবে এবং সঠিক পাল্লায় ওজন করবে; এটা সর্বোত্তম এবং পরিণামে উৎকৃষ্টতর।",
                    translationEn = "And give full measure when you measure, and weigh with an even balance. That is the best [way] and best in result.",
                    juzNumber = 15,
                    pageNumber = 285,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ন্যায্য ওজন সমাজে শান্তি ও ব্যবসায়িক বরকত নিশ্চিত করে।"
                )
            )
        )
    )
}
