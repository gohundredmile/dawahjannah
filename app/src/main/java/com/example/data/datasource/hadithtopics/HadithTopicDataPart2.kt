package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart2 {

    val topics: List<HadithTopic> = listOf(
        // 8. পিতা-মাতার অধিকার ও সন্তুষ্টি
        HadithTopic(
            id = "topic_parents_rights",
            categoryId = "cat_family",
            nameBn = "পিতা-মাতার অধিকার ও সন্তুষ্টি",
            nameEn = "Rights of Parents & Serving Them",
            nameAr = "بر الوالدين ورضا الرب في رضاهما",
            descriptionBn = "পিতা-মাতার সন্তুষ্টিতে আল্লাহর সন্তুষ্টি, জান্নাতের দরজা মা-বাবার সেবা এবং তাঁদের সাথে সদ্ব্যবহারের মর্যাদা।",
            searchKeywordsBn = listOf("পিতা মাতা", "মা বাবা", "মা", "বাবা", "পিতা", "মাতা", "মা-বাবা", "পিতামাতা", "জান্নাতের দরজা"),
            searchKeywordsEn = listOf("parents", "mother", "father", "kindness to parents", "serving parents"),
            isFeatured = true,
            iconEmoji = "👨‍👩‍👧",
            relatedTopicIds = listOf("topic_brotherhood_society", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "riyadus-salihin_13",
                    bookSlug = "riyadus-salihin",
                    bookNameBn = "রিয়াযুস স্বা-লিহীন",
                    hadithNumber = 13,
                    chapterTitleBn = "পিতা-মাতার প্রতি সদ্ব্যবহার",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "جَاءَ رَجُلٌ إِلَى رَسُولِ اللَّهِ صلى الله عليه وسلم فَقَالَ: يَا رَسُولَ اللَّهِ، مَنْ أَحَقُّ النَّاسِ بِحُسْنِ صَحَابَتِي؟ قَالَ: «أُمُّكَ»، قَالَ: ثُمَّ مَنْ؟ قَالَ: «ثُمَّ أُمُّكَ»، قَالَ: ثُمَّ مَنْ؟ قَالَ: «ثُمَّ أُمُّكَ»، قَالَ: ثُمَّ مَنْ؟ قَالَ: «ثُمَّ أَبُوكَ».",
                    banglaText = "এক ব্যক্তি রাসুলুল্লাহ (সা.)-এর নিকট এসে জিজ্ঞেস করল: 'হে আল্লাহর রাসুল! আমার কাছে সর্বোত্তম সদাচরণ পাওয়ার সর্বাধিক হকদার কে?' রাসুলুল্লাহ (সা.) বললেন: 'তোমার মা।' সে বলল: 'তারপর কে?' তিনি বললেন: 'তোমার মা।' সে পুনরায় বলল: 'তারপর কে?' তিনি বললেন: 'তোমার মা।' সে পুনরায় বলল: 'তারপর কে?' রাসুলুল্লাহ (সা.) বললেন: 'তারপর তোমার পিতা।' [সহীহ বুখারী: ৫৯৭১, সহীহ মুসলিম: ২৫৪৮]",
                    englishText = "A man came to Allah's Messenger and said, 'O Allah's Messenger! Who is more entitled to be treated with the best companionship by me?' The Prophet said, 'Your mother.' The man said. 'Who is next?' The Prophet said, 'Your mother.' The man said, 'Who is next?' The Prophet said, 'Your mother.' The man further said, 'Who is next?' The Prophet said, 'Then your father.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৫৯৭১, সহীহ মুসলিম ২৫৪৮",
                    explanationBn = "ইসলামে মায়ের ত্যাগ ও ভালোবাসার স্বীকৃতিস্বরূপ তাঁকে তিন গুণ মর্যাদা প্রদান করা হয়েছে এবং এরপর পিতার সম্মান ও আনুগত্য অপরিহার্য করা হয়েছে।",
                    relatedQuranSurahNumber = 17,
                    relatedQuranAyahNumber = 23,
                    relatedQuranAyahRef = "সূরা আল-ইসরা (১৭:২৩-২৪)",
                    relatedDuaTitleBn = "পিতা-মাতার জন্য দো'আ"
                )
            )
        ),

        // 9. মুসলিম ভ্রাতৃত্ব ও সহমর্মিতা
        HadithTopic(
            id = "topic_brotherhood_society",
            categoryId = "cat_society",
            nameBn = "মুসলিম ভ্রাতৃত্ব ও সহমর্মিতা",
            nameEn = "Islamic Brotherhood & Compassion",
            nameAr = "الأخوة الإسلامية والتراحم بين المؤمنين",
            descriptionBn = "নিজের জন্য যা পছন্দ করা অন্যের জন্যও তা পছন্দ না করা পর্যন্ত প্রকৃত মুমিন না হওয়ার শাশ্বত নববী বাণী।",
            searchKeywordsBn = listOf("ভ্রাতৃত্ব", "সহমর্মিতা", "ভালোবাসা", "মুমিন", "এক দেহ", "মুসলিম ভাই", "অধিকার"),
            searchKeywordsEn = listOf("brotherhood", "empathy", "compassion", "love for brother", "community"),
            isFeatured = true,
            iconEmoji = "🤝",
            relatedTopicIds = listOf("topic_parents_rights", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_13",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহুল বুখারী",
                    hadithNumber = 13,
                    chapterTitleBn = "ঈমান পর্ব",
                    narratorBn = "হযরত আনাস ইবনু মালিক (রা.)",
                    arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: তোমাদের কেউ ততক্ষণ পর্যন্ত পূর্ণ ঈমানদার হতে পারবে না, যতক্ষণ না সে তার ভাইয়ের জন্য তাই পছন্দ করবে, যা সে নিজের জন্য পছন্দ করে।",
                    englishText = "None of you will believe until you love for your brother what you love for yourself.",
                    gradeBn = "সহীহ বুখারী: ১৩",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ১৩, সহীহ মুসলিম ৪৫",
                    explanationBn = "এই হাদিসটি পারস্পরিক সহানুভূতি ও সামাজিক সম্প্রীতির সর্বোচ্চ শিখর। স্বার্থপরতা ত্যাগ করে অন্যের কল্যাণ কামনা করাই খাঁটি ঈমানের প্রমাণ।",
                    relatedQuranSurahNumber = 49,
                    relatedQuranAyahNumber = 10,
                    relatedQuranAyahRef = "সূরা আল-হুজুরাত (৪৯:১০)"
                )
            )
        ),

        // 10. ইস্তিগফার ও তওবার মাহাত্ম্য
        HadithTopic(
            id = "topic_istighfar_tawbah",
            categoryId = "cat_dua_dhikr",
            nameBn = "ইস্তিগফার ও তওবার মাহাত্ম্য",
            nameEn = "Repentance (Tawbah) & Seeking Forgiveness",
            nameAr = "فضل التوبة والاستغفار وسعة رحمة الله",
            descriptionBn = "গুনাহ পাহাড় সমান হলেও আল্লাহর ক্ষমা তার চেয়েও বিশাল—হাদীসে কুদসীতে বান্দার প্রতি করুণার মহিমাময় বার্তা।",
            searchKeywordsBn = listOf("তওবা", "ইস্তিগফার", "ক্ষমা", "গুনাহ মাফ", "পাপ", "রহমত", "সাইয়েদুল ইস্তেগফার"),
            searchKeywordsEn = listOf("tawbah", "istighfar", "repentance", "forgiveness", "mercy", "sins"),
            isFeatured = true,
            iconEmoji = "🤲",
            relatedTopicIds = listOf("topic_iman_tawheed", "topic_sabr_hardship"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "hadith-qudsi_1",
                    bookSlug = "hadith-qudsi",
                    bookNameBn = "হাদীসে কুদসী",
                    hadithNumber = 1,
                    chapterTitleBn = "আল্লাহর রহমত ও মহত্ব",
                    narratorBn = "হযরত আনাস ইবনু মালিক (রা.)",
                    arabicText = "قَالَ اللَّهُ تَبَارَكَ وَتَعَالَى: يَا ابْنَ آدَمَ! إِنَّكَ مَا دَعَوْتَنِي وَرَجَوْتَنِي غَفَرْتُ لَكَ عَلَى مَا كَانَ فِيكَ وَلاَ أُبَالِي، يَا ابْنَ آدَمَ! لَوْ بَلَغَتْ ذُنُوبُكَ عَنَانَ السَّمَاءِ ثُمَّ اسْتَغْفَرْتَنِي غَفَرْتُ لَكَ، يَا ابْنَ آدَمَ! إِنَّكَ لَوْ أَتَيْتَنِي بِقُرَابِ الأَرْضِ خَطَايَا ثُمَّ لَقِيتَنِي لاَ تُشْرِكُ بِي شَيْئًا لأَتَيْتُكَ بِقُرَابِهَا مَغْفِرَةً.",
                    banglaText = "মহান আল্লাহ সুবহানাহু ওয়া তা'আলা ইরশাদ করেন: 'হে বনী আদম! যতক্ষণ তুমি আমাকে ডাকবে এবং আমার নিকট ক্ষমার আশা রাখবে, তোমার থেকে যত পাপই প্রকাশ পাক না কেন আমি তোমাকে ক্ষমা করে দেব, আমি কারও পরোয়া করি না। হে আদম সন্তান! যদি তোমার পাপরাশি আকাশের মেঘমালা পর্যন্তও পৌঁছে যায়, অতঃপর তুমি আমার নিকট ক্ষমা চাও, আমি তোমাকে ক্ষমা করে দেব। হে আদম সন্তান! তুমি যদি পৃথিবী পূর্ণ গুনাহ নিয়েও আমার দরবারে উপস্থিত হও এবং আমার সাথে কাউকে শরীক না করে থাকো, তবে আমিও ঠিক পৃথিবী পূর্ণ ক্ষমা ও অনুগ্রহ নিয়ে তোমার নিকট উপস্থিত হব।' [তিরমিজী: ৩৫৪০, সহীহ]",
                    englishText = "Allah the Almighty said: O son of Adam, so long as you call upon Me and ask of Me, I shall forgive you for what you have done, and I shall not mind. O son of Adam, were your sins to reach the clouds of the sky and were you then to ask forgiveness of Me, I would forgive you...",
                    gradeBn = "সহীহ তিরমিজী: ৩৫৪০",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে' আত-তিরমিজী ৩৫৪০, হাদীসে কুদসী ১",
                    explanationBn = "হাদীসে কুদসীর এই অতুলনীয় বাণীতে আল্লাহর সীমাহীন ক্ষমাশীলতা ও তাওহীদের একনিষ্ঠতার পুরস্কার তুলে ধরা হয়েছে। বান্দা যেন কখনোই নিরাশ না হয়।",
                    relatedQuranSurahNumber = 39,
                    relatedQuranAyahNumber = 53,
                    relatedQuranAyahRef = "সূরা আয-যুমার (৩৯:৫৩)",
                    relatedDuaTitleBn = "সাইয়েদুল ইস্তেগফার"
                )
            )
        ),

        // 11. ইলম অর্জন ও জ্ঞান প্রচারের মর্যাদা
        HadithTopic(
            id = "topic_ilm_virtue",
            categoryId = "cat_ilm",
            nameBn = "ইলম অর্জন ও জ্ঞান অন্বেষণের মর্যাদা",
            nameEn = "Obligation & Virtues of Seeking Knowledge",
            nameAr = "طلب العلم وفضل العلماء",
            descriptionBn = "প্রতিটি মুসলিমের ওপর জ্ঞান অর্জন ফরজ হওয়া এবং ইলম অন্বেষণকারীর জন্য জান্নাতের পথ সুগম হওয়ার শিক্ষা।",
            searchKeywordsBn = listOf("ইলম", "জ্ঞান", "শিক্ষা", "আলেম", "জ্ঞান অর্জন", "ফরজ", "তালিবুল ইলম"),
            searchKeywordsEn = listOf("ilm", "knowledge", "seeking knowledge", "scholars", "education", "virtue"),
            isFeatured = true,
            iconEmoji = "📚",
            relatedTopicIds = listOf("topic_quran_virtue", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "ibn-majah_224",
                    bookSlug = "ibn-majah",
                    bookNameBn = "সুনানে ইবনে মাজাহ",
                    hadithNumber = 224,
                    chapterTitleBn = "মুকাদ্দামাহ",
                    narratorBn = "হযরত আনাস ইবনু মালিক (রা.)",
                    arabicText = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: দ্বীনি জ্ঞান অন্বেষণ করা প্রত্যেক মুসলিমের ওপর ফরজ বা অবশ্য পালনীয় দায়িত্ব।",
                    englishText = "Seeking knowledge is an obligation upon every Muslim.",
                    gradeBn = "সুনানে ইবনে মাজাহ: ২২৪ (সহীহ লিগাইরিহী)",
                    gradeColor = "SAHIH",
                    sourceBn = "সুনানে ইবনে মাজাহ ২২৪, সহীহ আল-জামি' ৩৯১৩",
                    explanationBn = "নিজের আকীদাহ, সালাত, হালাল-হারাম ও দৈনন্দিন দ্বীনি দায়িত্ব সঠিকভাবে পালনের জন্য প্রয়োজনীয় জ্ঞান অর্জন করা প্রত্যেক মুমিনের ওপর ফরজ আইন।",
                    relatedQuranSurahNumber = 20,
                    relatedQuranAyahNumber = 114,
                    relatedQuranAyahRef = "সূরা ত্বা-হা (২০:১১৪)",
                    relatedDuaTitleBn = "জ্ঞান বৃদ্ধির দো'আ: রব্বি যিদনী ইলমা"
                )
            )
        ),

        // 12. হালাল উপার্জন ও ব্যবসায় সততা
        HadithTopic(
            id = "topic_halal_earning",
            categoryId = "cat_business",
            nameBn = "হালাল উপার্জন ও ব্যবসায় সততা",
            nameEn = "Halal Earning & Honesty in Trade",
            nameAr = "الكسب الحلال والصدق في التجارة",
            descriptionBn = "নিজের শ্রম দিয়ে হালাল জীবিকা উপার্জন, সত্যবাদী সৎ ব্যবসায়ীদের নবি ও সিদ্দিকীনদের সাথে জান্নাতে থাকার সুসংবাদ।",
            searchKeywordsBn = listOf("হালাল", "উপার্জন", "ব্যবসা", "সততা", "রুজি", "রিজিক", "শ্রম", "হালাল টাকা"),
            searchKeywordsEn = listOf("halal", "earning", "business", "trade", "honesty", "livelihood"),
            isFeatured = true,
            iconEmoji = "💰",
            relatedTopicIds = listOf("topic_intention_iklas", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_1209",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে' আত-তিরমিজী",
                    hadithNumber = 1209,
                    chapterTitleBn = "ক্রয়-বিক্রয় পর্ব",
                    narratorBn = "হযরত আবু সাঈদ আল-খুদরী (রা.)",
                    arabicText = "التَّاجِرُ الصَّدُوقُ الأَمِينُ مَعَ النَّبِيِّينَ وَالصِّدِّيقِينَ وَالشُّهَدَاءِ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: সত্যবাদী ও বিশ্বস্ত আমানতদার ব্যবসায়ী পরকালে নবীগণ, সিদ্দীকগণ ও শহীদগণের সাথে থাকবে।",
                    englishText = "The truthful and trustworthy merchant is with the prophets, the truthful, and the martyrs.",
                    gradeBn = "হাসান (তিরমিজী: ১২০৯)",
                    gradeColor = "HASAN",
                    sourceBn = "জামে' আত-তিরমিজী ১২০৯, দারেমী ২৫৩৯",
                    explanationBn = "ব্যবসা-বাণিজ্যে মিথ্যা শপথ, প্রতারণা ও কারচুপি পরিহার করে সৎভাবে অর্থ উপার্জন করা ইসলামের অন্যতম শ্রেষ্ঠ সম্মানিত আমল।",
                    relatedQuranSurahNumber = 62,
                    relatedQuranAyahNumber = 10,
                    relatedQuranAyahRef = "সূরা আল-জুমু'আহ (৬২:১০)"
                )
            )
        ),

        // 13. জান্নাত লাভের সহজ আমলসমূহ
        HadithTopic(
            id = "topic_jannah_deeds",
            categoryId = "cat_akhirah",
            nameBn = "জান্নাত লাভের সহজ আমলসমূহ",
            nameEn = "Good Deeds that Lead to Jannah",
            nameAr = "الأعمال الموجبة للجنة وفضلها",
            descriptionBn = "উত্তম চরিত্র, জিহ্বা ও লজ্জাস্থানের হেফাজত, এতিমের লালন-পালন এবং জান্নাতে রাসুলুল্লাহ (সা.)-এর নৈকট্য লাভের আমল।",
            searchKeywordsBn = listOf("জান্নাত", "আমল", "জান্নাতের পথ", "সহজ আমল", "উত্তম চরিত্র", "বেহেশত"),
            searchKeywordsEn = listOf("jannah", "paradise", "good deeds", "path to jannah", "heaven"),
            isFeatured = true,
            iconEmoji = "🌸",
            relatedTopicIds = listOf("topic_salah_importance", "topic_parents_rights"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_6005",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহুল বুখারী",
                    hadithNumber = 6005,
                    chapterTitleBn = "আদব ও শিষ্টাচার",
                    narratorBn = "হযরত সাহল ইবনু সা'দ (রা.)",
                    arabicText = "أَنَا وَكَافِلُ الْيَتِيمِ فِي الْجَنَّةِ هَكَذَا. وَأَشَارَ بِالسَّبَّابَةِ وَالْوُسْطَى، وَفَرَّجَ بَيْنَهُمَا شَيْئًا.",
                    banglaText = "রাসুলুল্লাহ (সা.) স্বীয় তর্জনী ও মধ্যমা আঙ্গুল একত্রিত করে সামান্য ফাঁক রেখে ইশারা করলেন এবং বললেন: 'আমি ও এতিমের দায়িত্ব গ্রহণকারী ব্যক্তি জান্নাতে ঠিক এই দুটি আঙ্গুলের মতোই পাশাপাশি অবস্থান করব।' [সহীহ বুখারী: ৬০০৫]",
                    englishText = "I and the person who looks after an orphan and provides for him, will be in Paradise like this, putting his index and middle fingers together and separating them slightly.",
                    gradeBn = "সহীহ বুখারী: ৬০০৫",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৬০০৫, আবু দাউদ ৫১৩০, তিরমিজী ১৯১৮",
                    explanationBn = "অসহায় এতিম শিশুদের অভিভাবকত্ব গ্রহণ ও ভালোবাসা জান্নাতুল ফিরদাউসে রাসুলুল্লাহ (সা.)-এর সান্নিধ্য লাভের অন্যতম সহজ ও শ্রেষ্ঠ উপায়।",
                    relatedQuranSurahNumber = 93,
                    relatedQuranAyahNumber = 9,
                    relatedQuranAyahRef = "সূরা আদ-দুহা (৯৩:৯)"
                )
            )
        ),

        // 14. কল্যাণকামিতা ও দ্বীনের দাওয়াত
        HadithTopic(
            id = "topic_nasiha_dawah",
            categoryId = "cat_ilm",
            nameBn = "কল্যাণকামিতা ও দ্বীনের উপদেশ",
            nameEn = "Ad-Deen an-Naseehah (Good Counsel & Da'wah)",
            nameAr = "الدين النصيحة والأمر بالمعروف",
            descriptionBn = "দ্বীন হলো মূলত আন্তরিক কল্যাণকামিতা—আল্লাহ, তাঁর রাসুল, কুরআন, মুসলিম শাসক ও সর্বসাধারণের প্রতি।",
            searchKeywordsBn = listOf("উপদেশ", "নাসীহা", "কল্যাণকামিতা", "দ্বীন", "দাওয়াত", "হিতোপদেশ"),
            searchKeywordsEn = listOf("nasiha", "sincerity", "advice", "dawah", "counsel", "deen"),
            isFeatured = false,
            iconEmoji = "📢",
            relatedTopicIds = listOf("topic_ilm_virtue", "topic_brotherhood_society"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "forty-nawawi_7",
                    bookSlug = "forty-nawawi",
                    bookNameBn = "ইমাম নববীর ৪০ হাদীস",
                    hadithNumber = 7,
                    chapterTitleBn = "মৌলিক ৪০ হাদিস",
                    narratorBn = "হযরত তামীম আদ-দারী (রা.)",
                    arabicText = "الدِّينُ النَّصِيحَةُ. قُلْنَا: لِمَنْ؟ قَالَ: لِلَّهِ وَلِكِتَابِهِ وَلِرَسُولِهِ وَلأَئِمَّةِ الْمُسْلِمِينَ وَعَامَّتِهِمْ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করলেন: 'দ্বীন হলো আন্তরিক কল্যাণকামিতা।' আমরা জিজ্ঞেস করলাম: 'কার জন্য?' তিনি বললেন: 'আল্লাহর জন্য, তাঁর কিতাবের জন্য, তাঁর রাসুলের জন্য এবং মুসলিম নেতৃবৃন্দ ও তাদের সর্বসাধারণের জন্য।' [সহীহ মুসলিম: ৫৫]",
                    englishText = "The religion is naseehah (sincerity/well-wishing). We said: 'To whom?' He said: 'To Allah, His Book, His Messenger, and to the leaders of the Muslims and their common folk.'",
                    gradeBn = "সহীহ মুসলিম: ৫৫",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৫৫, আবু দাউদ ৪৯৪৪, ইমাম নববীর ৪০ হাদীস ৭",
                    explanationBn = "একজন মুমিন সর্বদা নিজের জন্য এবং সমাজের সকল মানুষের জন্য অন্তরে কল্যাণ কামনা করবে। বিদ্বেষ বা ষড়যন্ত্র পরিহার করে হিতোপদেশ প্রদান দ্বীনের প্রাণ।",
                    relatedQuranSurahNumber = 16,
                    relatedQuranAyahNumber = 125,
                    relatedQuranAyahRef = "সূরা আন-নাহল (১৬:১২৫)"
                )
            )
        )
    )
}
