package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart7 {

    val topics: List<QuranTopic> = listOf(
        // 50. ঋণ পরিশোধ, চুক্তি ও আমানত রক্ষা (HadithBD: মুআমালাত)
        QuranTopic(
            id = "topic_debt_contracts_trust",
            categoryId = "cat_wealth",
            nameBn = "ঋণ পরিশোধ, চুক্তি ও আমানত রক্ষা",
            nameEn = "Debt Clearance, Contracts & Fulfilling Trusts",
            nameAr = "أداء الديون والوفاء بالعقود والأمانات",
            descriptionBn = "হাদিসবিডি (HadithBD) ও কোরআনিক বিধান অনুযায়ী চুক্তি লিপিবদ্ধ করা, যথাসময়ে ঋণ পরিশোধ ও আমানত খিয়ানত না করার সুস্পষ্ট নির্দেশ।",
            searchKeywordsBn = listOf("ঋণ", "কর্জ", "চুক্তি", "আমানত", "দেনা", "হাদিসবিডি", "মুআমালাত", "হিসাব"),
            searchKeywordsEn = listOf("debt", "loan", "contracts", "trust", "amanah", "agreements"),
            isFeatured = true,
            iconEmoji = "📝",
            relatedTopicIds = listOf("topic_business_honesty", "topic_halal_wealth", "topic_justice_witness"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 282,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا تَدَايَنتُم بِدَيْنٍ إِلَىٰ أَجَلٍ مُّسَمًّى فَاكْتُبُوهُ ۚ وَلْيَكْتُب بَّيْنَكُمْ كَاتِبٌ بِالْعَدْلِ",
                    translationBn = "হে মুমিনগণ! যখন তোমরা নির্দিষ্ট মেয়াদের জন্য পরস্পর ঋণের লেনদেন করো, তখন তা লিখে রাখো। আর তোমাদের মধ্যে কোনো লেখক যেন ন্যায়ের সাথে লিখে দেয়।",
                    translationEn = "O you who have believed, when you contract a debt for a specified term, write it down. And let a scribe write [it] between you in justice.",
                    juzNumber = 3,
                    pageNumber = 48,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "কুরআনুল কারীমের দীর্ঘতম আয়াত (আয়াতুদ দাইয়ন), যা আর্থিক লেনদেনে স্বচ্ছতা ও লিখিত দলিলের ওপর অপরিসীম গুরুত্ব আরোপ করে।"
                ),
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 58,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "۞ إِنَّ اللَّهَ يَأْمُرُكُمْ أَن تُؤَدُّوا الْأَمَانَاتِ إِلَىٰ أَهْلِهَا وَإِذَا حَكَمْتُم بَيْنَ النَّاسِ أَن تَحْكُمُوا بِالْعَدْلِ",
                    translationBn = "নিশ্চয়ই আল্লাহ তোমাদেরকে নির্দেশ দিচ্ছেন আমানতসমূহ তার হকদারদের নিকট পৌঁছে দেওয়ার এবং মানুষের মাঝে যখন ফয়সালা করো তখন ন্যায়ের সাথে ফয়সালা করো।",
                    translationEn = "Indeed, Allah commands you to render trusts to whom they are due and when you judge between people to judge with justice.",
                    juzNumber = 5,
                    pageNumber = 87,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আমানত রক্ষা করা মুমিনের মৌলিক বৈশিষ্ট্যের একটি।"
                ),
                TopicAyah(
                    surahNumber = 5,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আল-মায়েদা",
                    surahNameEn = "Al-Ma'idah",
                    surahNameAr = "المائدة",
                    totalAyahsInSurah = 120,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا أَوْفُوا بِالْعُقُودِ",
                    translationBn = "হে মুমিনগণ! তোমরা প্রতিশ্রুতি ও চুক্তিসমূহ সম্পূর্ণরূপে পূরণ করো।",
                    translationEn = "O you who have believed, fulfill [all] contracts.",
                    juzNumber = 6,
                    pageNumber = 106,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহ ও বান্দার সাথে কৃত সকল ওয়াদা ও পারস্পরিক চুক্তি রক্ষা করা ফরজ।"
                )
            )
        ),

        // 51. মালাইকা (ফেরেশতাগণ) ও অদৃশ্যের জগত (HadithBD: আকীদা)
        QuranTopic(
            id = "topic_angels_unseen",
            categoryId = "cat_tawheed",
            nameBn = "মালাইকা (ফেরেশতাগণ) ও অদৃশ্যের জগত",
            nameEn = "Angels (Mala'ikah) & Realm of the Unseen",
            nameAr = "الملائكة الكرام وعالم الغيب",
            descriptionBn = "আল্লাহর অনুগত সৃষ্টি ফেরেশতাদের উপর ঈমান, জিবরাঈল (আ.)-এর ওহী বহন, কিরমান কাতিবিনের আমল লিখন ও আল্লাহর তাসবীহ পাঠ।",
            searchKeywordsBn = listOf("ফেরেশতা", "মালাইকা", "জিবরাঈল", "অদৃশ্য", "আমলনামা", "কেরামান কাতেবীন", "গাইব"),
            searchKeywordsEn = listOf("angels", "malaikah", "jibril", "unseen", "kiraman katibin", "ghayb"),
            isFeatured = true,
            iconEmoji = "✨",
            relatedTopicIds = listOf("topic_tawheed", "topic_names_of_allah", "topic_death_barzakh"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 35,
                    ayahNumber = 1,
                    surahNameBn = "সূরা ফাতির",
                    surahNameEn = "Fatir",
                    surahNameAr = "فاطر",
                    totalAyahsInSurah = 45,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "الْحَمْدُ لِلَّهِ فَاطِرِ السَّمَاوَاتِ وَالْأَرْضِ جَاعِلِ الْمَلَائِكَةِ رُسُلًا أُولِي أَجْنِحَةٍ مَّثْنَىٰ وَثُلَاثَ وَرُبَاعَ",
                    translationBn = "সমস্ত প্রশংসা আল্লাহর, যিনি আসমান ও জমিনের আদি স্রষ্টা এবং ফেরেশতাদের বানিয়েছেন বার্তাবাহক—যাঁরা দুই-দুই, তিন-তিন ও চার-চার ডানাবিশিষ্ট।",
                    translationEn = "[All] praise is [due] to Allah, Creator of the heavens and the earth, [who] made the angels messengers having wings, two or three or four.",
                    juzNumber = 22,
                    pageNumber = 434,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "ফেরেশতাগণের রূপ ও দায়িত্বের বিবরণ।"
                ),
                TopicAyah(
                    surahNumber = 50,
                    ayahNumber = 17,
                    surahNameBn = "সূরা ক্বাফ",
                    surahNameEn = "Qaf",
                    surahNameAr = "ق",
                    totalAyahsInSurah = 45,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "إِذْ يَتَلَقَّى الْمُتَلَقِّيَانِ عَنِ الْيَمِينِ وَعَنِ الشِّمَالِ قَعِيدٌ مَّا يَلْفِظُ مِن قَوْلٍ إِلَّا لَدَيْهِ رَقِيبٌ عَتِيدٌ",
                    translationBn = "স্মরণ রেখো, দুই গ্রহণকারী ফেরেশতা ডানে ও বামে বসে মানুষের কাজ গ্রহণ করে। মানুষ যে কথাই উচ্চারণ করে, তার কাছেই প্রস্তুত পাহারাদার রয়েছে।",
                    translationEn = "When the two receivers receive, seated on the right and on the left. Man does not utter any word except that with him is an observer prepared.",
                    juzNumber = 26,
                    pageNumber = 519,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "প্রতিটি কথা ও কাজের নির্ভুল হিসাব রক্ষায় সম্মানিত ফেরেশতাগণের সদা উপস্থিতি।"
                ),
                TopicAyah(
                    surahNumber = 66,
                    ayahNumber = 6,
                    surahNameBn = "সূরা আত-তাহরীম",
                    surahNameEn = "At-Tahrim",
                    surahNameAr = "التحريم",
                    totalAyahsInSurah = 12,
                    revelationTypeBn = "মাদানী",
                    arabicText = "عَلَيْهَا مَلَائِكَةٌ غِلَاظٌ شِدَادٌ لَّا يَعْصُونَ اللَّهَ مَا أَمَرَهُمْ وَيَفْعَلُونَ مَا يُؤْمَرُونَ",
                    translationBn = "তার (জাহান্নামের) ওপর নিয়োজিত রয়েছে কঠোর স্বভাবের শক্তিশালী ফেরেশতাগণ, যারা আল্লাহ যা আদেশ করেন তা অমান্য করে না এবং তারা যা আদেশ পায় তাই করে।",
                    translationEn = "Over which are [appointed] angels, harsh and severe; they do not disobey Allah in what He commands them but do what they are commanded.",
                    juzNumber = 28,
                    pageNumber = 560,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আল্লাহর নির্দেশ বাস্তবায়নে ফেরেশতাদের নিরঙ্কুশ আনুগত্য।"
                )
            )
        ),

        // 52. তাকদীর ও আল্লাহর চিরন্তন ফয়সালা (HadithBD: আকীদা)
        QuranTopic(
            id = "topic_qadar_predestination",
            categoryId = "cat_tawheed",
            nameBn = "তাকদীর ও আল্লাহর চিরন্তন ফয়সালা",
            nameEn = "Divine Decree (Qadar) & Absolute Will of Allah",
            nameAr = "الإيمان بالقدر خيره وشره وقضاء الله",
            descriptionBn = "সকল ভালো-মন্দ আল্লাহর নির্ধারিত তকদীরের অন্তর্ভুক্ত। বিপদে ব্যাকুল না হয়ে এবং সম্পদে অহংকারী না হয়ে আল্লাহর ফয়সালায় সন্তুষ্ট থাকা।",
            searchKeywordsBn = listOf("তাকদীর", "ভাগ্য", "ফয়সালা", "অদৃষ্ট", "লওহে মাহফুজ", "আল্লাহর ইচ্ছা", "তকদীর"),
            searchKeywordsEn = listOf("qadar", "destiny", "predestination", "fate", "decree", "lawh mahfuz"),
            isFeatured = true,
            iconEmoji = "⚖️",
            relatedTopicIds = listOf("topic_tawakkul", "topic_sabr", "topic_tawheed"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 57,
                    ayahNumber = 22,
                    surahNameBn = "সূরা আল-হাদীদ",
                    surahNameEn = "Al-Hadid",
                    surahNameAr = "الحديد",
                    totalAyahsInSurah = 29,
                    revelationTypeBn = "মাদানী",
                    arabicText = "مَا أَصَابَ مِن مُّصِيبَةٍ فِي الْأَرْضِ وَلَا فِي أَنفُسِكُمْ إِلَّا فِي كِتَابٍ مِّن قَبْلِ أَن نَّبْرَأَهَا ۚ إِنَّ ذَٰلِكَ عَلَى اللَّهِ يَسِيرٌ",
                    translationBn = "পৃথিবীতে অথবা তোমাদের নিজেদের ওপর যে কোনো বিপদ আসে, তা আমি সৃষ্টি করার পূর্বেই একটি কিতাবে (লওহে মাহফুজে) লিপিবদ্ধ রয়েছে। নিশ্চয়ই এটা আল্লাহর জন্য অত্যন্ত সহজ।",
                    translationEn = "No disaster strikes upon the earth or among yourselves except that it is in a register before We bring it into being - indeed that, for Allah, is easy.",
                    juzNumber = 27,
                    pageNumber = 540,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "বিপদ ঘটলে আফসোস না করে এবং সাফল্য পেলে অহংকার না করে আল্লাহকে স্মরণ করার ঐশী দিকনির্দেশনা।"
                ),
                TopicAyah(
                    surahNumber = 54,
                    ayahNumber = 49,
                    surahNameBn = "সূরা আল-ক্বামার",
                    surahNameEn = "Al-Qamar",
                    surahNameAr = "القمر",
                    totalAyahsInSurah = 55,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "إِنَّا كُلَّ شَيْءٍ خَلَقْنَاهُ بِقَدَرٍ",
                    translationBn = "নিশ্চয়ই আমি প্রত্যেক বস্তুকে এক সুনির্দিষ্ট পরিমাপে (তাকদীরে) সৃষ্টি করেছি।",
                    translationEn = "Indeed, all things We created with predestination.",
                    juzNumber = 27,
                    pageNumber = 530,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মহাবিশ্বের সৃষ্টি ও মানুষের জীবনপরিক্রমা এক নিখুঁত ঐশী পরিমাপ অনুযায়ী পরিচালিত।"
                ),
                TopicAyah(
                    surahNumber = 9,
                    ayahNumber = 51,
                    surahNameBn = "সূরা আত-তাওবাহ",
                    surahNameEn = "At-Tawbah",
                    surahNameAr = "التوبة",
                    totalAyahsInSurah = 129,
                    revelationTypeBn = "মাদানী",
                    arabicText = "قُل لَّن يُصِيبَنَا إِلَّا مَا كَتَبَ اللَّهُ لَنَا هُوَ مَوْلَانَا ۚ وَعَلَى اللَّهِ فَلْيَتَوَكَّلِ الْمُؤْمِنُونَ",
                    translationBn = "বলুন: 'আল্লাহ আমাদের জন্য যা নির্ধারণ করে রেখেছেন তা ছাড়া অন্য কিছুই আমাদের স্পর্শ করবে না। তিনিই আমাদের অভিভাবক, আর আল্লাহর ওপরই মুমিনদের ভরসা করা উচিত।' ",
                    translationEn = "Say, 'Never will we be struck except by what Allah has decreed for us; He is our protector.' And upon Allah let the believers rely.",
                    juzNumber = 10,
                    pageNumber = 195,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মুমিনের নির্ভীকতা ও অবিচলতার মূল ভিত্তি হলো আল্লাহর তাকদীরে পূর্ণ আত্মসমর্পণ।"
                )
            )
        ),

        // 53. প্রতিবেশীর অধিকার ও সামাজিক সম্প্রীতি (HadithBD: সমাজ ও ভ্রাতৃত্ব)
        QuranTopic(
            id = "topic_neighbors_social_rights",
            categoryId = "cat_family",
            nameBn = "প্রতিবেশীর অধিকার ও সামাজিক সম্প্রীতি",
            nameEn = "Rights of Neighbors & Social Harmony",
            nameAr = "حقوق الجيران والمعاملة الحسنة والتكافل",
            descriptionBn = "নিকট ও দূরবর্তী প্রতিবেশীর প্রতি সদাচরণ, তাদের সুখ-দুঃখে পাশে থাকা এবং নিরাপদ ও সহানুভূতিশীল সমাজ গঠন।",
            searchKeywordsBn = listOf("প্রতিবেশী", "হক", "পাড়া-প্রতিবেশী", "সামাজিক", "সম্প্রীতি", "সহানুভূতি", "অধিকার"),
            searchKeywordsEn = listOf("neighbors", "social harmony", "neighbor rights", "compassion", "community"),
            isFeatured = true,
            iconEmoji = "🏡",
            relatedTopicIds = listOf("topic_muslim_brotherhood", "topic_parents_rights", "topic_orphans_miskeen"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 36,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَاعْبُدُوا اللَّهَ وَلَا تُشْرِكُوا بِهِ شَيْئًا ۖ وَبِالْوَالِدَيْنِ إِحْسَانًا وَبِذِي الْقُرْبَىٰ وَالْيَتَامَىٰ وَالْمَسَاكِينِ وَالْجَارِ ذِي الْقُرْبَىٰ وَالْجَارِ الْجُنُبِ وَالصَّاحِبِ بِالْجَنبِ",
                    translationBn = "তোমরা আল্লাহর ইবাদত করো এবং তাঁর সাথে কাউকে শরিক কোরো না; পিতা-মাতার সাথে সদ্ব্যবহার করো, আর নিকটাত্মীয়, এতিম, অভাবগ্রস্ত, নিকট প্রতিবেশী, দূর প্রতিবেশী এবং সহচরদের সাথেও ভালো ব্যবহার করো।",
                    translationEn = "Worship Allah and associate nothing with Him, and to parents do good, and to relatives, orphans, the needy, the near neighbor, the neighbor farther away, the companion at your side.",
                    juzNumber = 5,
                    pageNumber = 84,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "তাওহীদের নির্দেশের পরপরই পিতামাতা, আত্মীয় ও উভয় ধরণের প্রতিবেশীর হক আদায়ের নির্দেশ দেওয়া হয়েছে।"
                ),
                TopicAyah(
                    surahNumber = 107,
                    ayahNumber = 7,
                    surahNameBn = "সূরা আল-মাউন",
                    surahNameEn = "Al-Ma'un",
                    surahNameAr = "الماعون",
                    totalAyahsInSurah = 7,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَيَمْنَعُونَ الْمَاعُونَ",
                    translationBn = "এবং যারা নিত্যপ্রয়োজনীয় ছোটখাটো সাহায্য-সহযোগিতা প্রদানে বিরত থাকে (প্রতিবেশীকে সাহায্য করে না)।",
                    translationEn = "And withhold [simple] assistance (household necessities to neighbors).",
                    juzNumber = 30,
                    pageNumber = 602,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "প্রতিবেশীর প্রয়োজনে গৃহস্থালী টুকিটাকি জিনিস বা সাহায্য না দেওয়াকে কপটতার লক্ষণ হিসেবে উল্লেখ করা হয়েছে।"
                )
            )
        ),

        // 54. আদম ও ইবলীসের ঘটনা এবং শয়তানের চিরশত্রুতা (HadithBD: আম্বিয়া ও আত্মরক্ষা)
        QuranTopic(
            id = "topic_adam_iblis_satan_enmity",
            categoryId = "cat_tawheed",
            nameBn = "আদম ও ইবলীসের ঘটনা এবং শয়তানের চিরশত্রুতা",
            nameEn = "Story of Adam, Iblis & Eternal Enmity of Satan",
            nameAr = "قصة آدم وإبليس وعداوة الشيطان المبينة",
            descriptionBn = "মানুষের সৃষ্টির সূচনা, ইবলীসের অহংকার ও অভিশাপ, শয়তানের প্রকাশ্য শত্রুতা থেকে আত্মরক্ষা ও সর্বদা সচেতন থাকার কোরআনিক আহ্বান।",
            searchKeywordsBn = listOf("আদম", "ইবলিস", "শয়তান", "অহংকার", "ধোঁকা", "সিজদা", "চিরশত্রু", "আত্মরক্ষা"),
            searchKeywordsEn = listOf("adam", "iblis", "satan", "devil", "enmity", "deceit", "arrogance"),
            isFeatured = true,
            iconEmoji = "🛡️",
            relatedTopicIds = listOf("topic_humility_pride", "topic_tawbah_forgiveness", "topic_tawheed"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 34,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَإِذْ قُلْنَا لِلْمَلَائِكَةِ اسْجُدُوا لِآدَمَ فَسَجَدُوا إِلَّا إِبْلِيسَ أَبَىٰ وَاسْتَكْبَرَ وَكَانَ مِنَ الْكَافِرِينَ",
                    translationBn = "আর যখন আমি ফেরেশতাদের বললাম: 'তোমরা আদমকে সিজদা করো', তখন ইবলীস ব্যতীত সকলেই সিজদা করল। সে অমান্য করল, অহংকার করল এবং কাফেরদের অন্তর্ভুক্ত হলো।",
                    translationEn = "And [mention] when We said to the angels, 'Prostrate before Adam'; so they prostrated, except for Iblees. He refused and was arrogant and became of the disbelievers.",
                    juzNumber = 1,
                    pageNumber = 6,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "অহংকারই ছিল প্রথম পাপ যার কারণে ইবলীস আল্লাহর রহমত থেকে চিরদিনের জন্য বিতাড়িত হয়।"
                ),
                TopicAyah(
                    surahNumber = 35,
                    ayahNumber = 6,
                    surahNameBn = "সূরা ফাতির",
                    surahNameEn = "Fatir",
                    surahNameAr = "فاطر",
                    totalAyahsInSurah = 45,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "إِنَّ الشَّيْطَانَ لَكُمْ عَدُوٌّ فَاتَّخِذُوهُ عَدُوًّا ۚ إِنَّمَا يَدْعُو حِزْبَهُ لِيَكُونُوا مِنْ أَصْحَابِ السَّعِيرِ",
                    translationBn = "নিশ্চয়ই শয়তান তোমাদের প্রকাশ্য শত্রু; অতএব তোমরাও তাকে শত্রু হিসেবেই গণ্য করো। সে তো তার দলকে কেবল এজন্যই আহ্বান করে যাতে তারা জাহান্নামের অধিবাসী হয়।",
                    translationEn = "Indeed, Satan is an enemy to you; so take him as an enemy. He only invites his party to be among the companions of the Blaze.",
                    juzNumber = 22,
                    pageNumber = 435,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "শয়তানের কুমন্ত্রণা ও পদাঙ্ক থেকে সতর্ক থাকার সরাসরি কোরআনিক আদেশ।"
                ),
                TopicAyah(
                    surahNumber = 7,
                    ayahNumber = 27,
                    surahNameBn = "সূরা আল-আ'রাফ",
                    surahNameEn = "Al-A'raf",
                    surahNameAr = "الأعراف",
                    totalAyahsInSurah = 206,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "يَا بَنِي آدَمَ لَا يَفْتِنَنَّكُمُ الشَّيْطَانُ كَمَا أَخْرَجَ أَبَوَيْكُم مِّنَ الْجَنَّةِ",
                    translationBn = "হে বনী আদম! শয়তান যেন কিছুতেই তোমাদেরকে বিভ্রান্ত না করে, যেভাবে সে তোমাদের পিতা-মাতাকে জান্নাত থেকে বের করে দিয়েছিল।",
                    translationEn = "O children of Adam, let not Satan tempt you as he removed your parents from Paradise.",
                    juzNumber = 8,
                    pageNumber = 153,
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "মানবজাতির পিতা আদম ও মাতা হাওয়া (আ.)-কে ধোঁকা দেওয়ার ঐতিহাসিক ঘটনা স্মরণ করিয়ে আত্মরক্ষার তাগিদ।"
                )
            )
        )
    )
}
