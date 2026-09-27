package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart3 {

    val topics: List<HadithTopic> = listOf(
        // 1. রমাদান ও সিয়ামের ফযীলত
        HadithTopic(
            id = "topic_sawm_ramadan_hadith",
            categoryId = "cat_ibadah",
            nameBn = "রমাদান ও সিয়ামের ফযীলত",
            nameEn = "Virtues of Ramadan & Fasting (Sawm)",
            nameAr = "فضل الصيام وشهر رمضان ومغفرة الذنوب",
            descriptionBn = "রমাদানের রোজা অতীতের সমস্ত গুনাহ মোচন করে দেয়, রোজাদারের মুখের গন্ধ আল্লাহর নিকট মেশকের চেয়েও সুগন্ধিময় এবং সিয়াম হলো জাহান্নাম থেকে আত্মরক্ষার ঢাল।",
            searchKeywordsBn = listOf("রোজা", "রমাদান", "সিয়াম", "সাওম", "রোজার ফজিলত", "ইফতার", "সেহেরি", "গুনাহ মাফ"),
            searchKeywordsEn = listOf("ramadan", "fasting", "sawm", "iftar", "sehri", "forgiveness"),
            isFeatured = true,
            iconEmoji = "🌙",
            relatedTopicIds = listOf("topic_salah_importance", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_38",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 38,
                    chapterTitleBn = "ঈমান অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি ঈমানের সাথে ও সওয়াবের আশায় রমাদানের রোজা রাখবে, তার অতীতের সমস্ত গুনাহ ক্ষমা করে দেওয়া হবে।' [সহীহ বুখারী: ৩৮, সহীহ মুসলিম: ৭৬০]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Whoever fasts during Ramadan out of sincere faith and hoping for a reward from Allah, then all his previous sins will be forgiven.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৩৮, সহীহ মুসলিম ৭৬০",
                    explanationBn = "রমাদানের সিয়াম পালন কেবল আনুষ্ঠানিক না খেয়ে থাকা নয়, বরং পূর্ণ ঈমান ও আল্লাহর সন্তুষ্টির আকুল প্রত্যাশায় রোজা রাখলে জীবনের আগের সমস্ত সগিরা গুনাহ মোচন হয়ে যায়।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 183,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:১৮৩)",
                    relatedDuaTitleBn = "ইফতারের দো'আ"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_1904",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 1904,
                    chapterTitleBn = "রোজা অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "قَالَ اللَّهُ: كُلُّ عَمَلِ ابْنِ آدَمَ لَهُ إِلَّا الصِّيَامَ، فَإِنَّهُ لِي وَأَنَا أَجْزِي بِهِ، وَالصِّيَامُ جُنَّةٌ... وَلَخُلُوفُ فَمِ الصَّائِمِ أَطْيَبُ عِنْدَ اللَّهِ مِنْ رِيحِ الْمِسْكِ",
                    banglaText = "আল্লাহ তা'আলা বলেন: 'বনী আদমের প্রতিটি আমলই তার নিজের জন্য, শুধু রোজা ব্যতীত। কারণ তা কেবল আমারই জন্য এবং আমি নিজেই এর প্রতিদান দেব।' আর সিয়াম হচ্ছে ঢালস্বরূপ... আল্লাহর শপথ, রোজাদারের মুখের গন্ধ আল্লাহর নিকট কস্তুরীর সুবাসের চেয়েও অধিক সুগন্ধিময়। [সহীহ বুখারী: ১৯০৪, সহীহ মুসলিম: ১১৫১]",
                    englishText = "Allah said: 'Every deed of the son of Adam is for him except fasting; it is for Me and I shall reward for it.' Fasting is a shield... By Him in Whose Hand is the soul of Muhammad, the unpleasant smell of the mouth of a fasting person is sweeter to Allah than the scent of musk.",
                    gradeBn = "সহীহ (হাদিসে কুদসী)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ১৯০৪, সহীহ মুসলিম ১১৫১",
                    explanationBn = "হাদিসে কুদসীতে রোজার অনুপম মর্যাদা ব্যক্ত করা হয়েছে। কোনো আমলেই লোকদেখানোর সুযোগ নেই যেমন রোজায়; তাই আল্লাহ তা'আলা এর অফুরন্ত প্রতিদান নিজ হাতে প্রদান করবেন।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 185,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:১৮৫)"
                )
            )
        ),

        // 2. যাকাত ও দান-সাদাকাহ
        HadithTopic(
            id = "topic_zakat_sadaqah_hadith",
            categoryId = "cat_ibadah",
            nameBn = "যাকাত ও দান-সাদাকাহর বরকত",
            nameEn = "Zakat, Charity & Virtues of Sadaqah",
            nameAr = "فضل الزكاة والصدقة ونماء المال",
            descriptionBn = "দান-সাদাকাহ ধন-সম্পদ হ্রাস করে না বরং পবিত্র ও বহুগুণে বৃদ্ধি করে। মুমিনের মুখের এক চিলতে অমায়িক হাসিও একটি মূল্যবান সদকা।",
            searchKeywordsBn = listOf("যাকাত", "সদকা", "দান", "ইনফাক", "সম্পদ বৃদ্ধি", "হাসি সদকা", "গরিবের হক"),
            searchKeywordsEn = listOf("zakat", "sadaqah", "charity", "spending", "barakah", "poor"),
            isFeatured = true,
            iconEmoji = "🤝",
            relatedTopicIds = listOf("topic_halal_earning", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_2588",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 2588,
                    chapterTitleBn = "সদাচরণ ও সৌজন্য",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَا نَقَصَتْ صَدَقَةٌ مِنْ مَالٍ، وَمَا زَادَ اللَّهُ عَبْدًا بِعَفْوٍ إِلَّا عِزًّا، وَمَا تَوَاضَعَ أَحَدٌ لِلَّهِ إِلَّا رَفَعَهُ اللَّهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'সাদাকাহ কোনো সম্পদ হ্রাস করে না; ক্ষমা প্রদর্শনের দ্বারা আল্লাহ বান্দার সম্মান বৃদ্ধিই করেন এবং কেউ আল্লাহর সন্তুষ্টির জন্য বিনীত হলে আল্লাহ তাকে মর্যাদায় সমুন্নত করেন।' [সহীহ মুসলিম: ২৫৮৮, জামে তিরমিজি: ২০২৯]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Charity does not decrease wealth, no one forgives another except that Allah increases his honor, and no one humbles himself for the sake of Allah except that Allah raises his status.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ২৫৮৮, তিরমিজি ২০২৯",
                    explanationBn = "বাহ্যিকভাবে দান করলে অর্থ কমে গেছে মনে হলেও বাস্তবে আল্লাহ তাতে বরকত ঢেলে দেন, বালা-মুসিবত দূর করেন এবং পরকালে এর বিশাল ভাণ্ডার প্রস্তুত রাখেন।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 261,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:২৬১)"
                ),
                TopicHadithRef(
                    hadithId = "tirmidhi_1956",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজি",
                    hadithNumber = 1956,
                    chapterTitleBn = "সদাচার ও পুণ্য",
                    narratorBn = "হযরত আবু যার (রা.)",
                    arabicText = "تَبَسُّمُكَ فِي وَجْهِ أَخِيكَ لَكَ صَدَقَةٌ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমার (মুসলিম) ভাইয়ের মুখের সামনে তোমার একটু মুচকি হাসি দেওয়াও তোমার জন্য একটি সদকা।' [জামে আত-তিরমিজি: ১৯৫৬]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Your smiling in the face of your brother is charity for you.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে আত-তিরমিজি ১৯৫৬, সহীহ ইবনে হিব্বান ৫২৯",
                    explanationBn = "সদকা কেবল টাকা-পয়সার মধ্যে সীমাবদ্ধ নয়; মানুষকে আনন্দ দেওয়া, মিষ্টি বাক্য বলা এবং ভালো ব্যবহারও প্রভূত সওয়াবের মাধ্যম।"
                )
            )
        ),

        // 3. হজ্জ ও উমরাহ
        HadithTopic(
            id = "topic_hajj_umrah_hadith",
            categoryId = "cat_ibadah",
            nameBn = "হজ্জ ও উমরাহর ফযীলত",
            nameEn = "Virtues of Hajj & Umrah",
            nameAr = "فضل الحج المبرور والعمرة وتكفير الذنوب",
            descriptionBn = "মাকবুল হজ্জের একমাত্র পুরস্কার হলো জান্নাত। পাপ ও অশ্লীলতামুক্ত হজ্জ সমাপনকারী নিষ্পাপ নবজাতক শিশুর মতো পুণ্যবান হয়ে বাড়ি ফেরে।",
            searchKeywordsBn = listOf("হজ্জ", "উমরাহ", "কাবা", "মক্কা", "মাকবুল হজ্জ", "আরাফাত", "হজ্ব"),
            searchKeywordsEn = listOf("hajj", "umrah", "pilgrimage", "makkah", "kaaba", "sins wiped"),
            isFeatured = true,
            iconEmoji = "🕋",
            relatedTopicIds = listOf("topic_salah_importance", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_1521",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 1521,
                    chapterTitleBn = "হজ্জ অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنْ حَجَّ لِلَّهِ فَلَمْ يَرْفُثْ، وَلَمْ يَفْسُقْ، رَجَعَ كَيَوْمِ وَلَدَتْهُ أُمُّهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি একমাত্র আল্লাহর সন্তুষ্টির উদ্দেশ্যে হজ্জ করল এবং তাতে কোনো অশ্লীল কথা বা পাপকর্মে লিপ্ত হলো না, সে হজ্জ শেষে এমন নিষ্পাপ অবস্থায় ফিরে আসে যেন আজই তার মা তাকে প্রসব করেছে।' [সহীহ বুখারী: ১৫২১, সহীহ মুসলিম: ১৩৫০]",
                    englishText = "The Prophet (pbuh) said: 'Whoever performs Hajj for Allah's pleasure and does not have sexual relations with his wife, and does not do evil or sins then he will return (after Hajj free from all sins) as if he were born anew on that very day.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ১৫২১, সহীহ মুসলিম ১৩৫০",
                    explanationBn = "বিশুদ্ধ নিয়তে ও সুন্নাত পদ্ধতিতে হজ্জ আদায় করলে জীবনের বিগত সমস্ত পাপ মোচন হয়ে বান্দা সম্পূর্ণ নিষ্কলুষ হয়ে যায়।",
                    relatedQuranSurahNumber = 3,
                    relatedQuranAyahNumber = 97,
                    relatedQuranAyahRef = "সূরা আলে-ইমরান (৩:৯৭)"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_1773",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 1773,
                    chapterTitleBn = "উমরাহ অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "العُمْرَةُ إِلَى العُمْرَةِ كَفَّارَةٌ لِمَا بَيْنَهُمَا، وَالحَجُّ المَبْرُورُ لَيْسَ لَهُ جَزَاءٌ إِلَّا الجَنَّةُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'এক উমরাহ থেকে পরবর্তী উমরাহ—উভয়ের মধ্যবর্তী সময়ের সমস্ত গুনাহের কাফফারা (মোচনকারী); আর মাকবুল হজ্জের একমাত্র প্রতিদান হলো জান্নাত।' [সহীহ বুখারী: ১৭৭৩, সহীহ মুসলিম: ১৩৪৯]",
                    englishText = "Allah's Messenger (pbuh) said: '(The performance of) Umrah is an expiation for the sins committed (between it and the previous one). And the reward of Hajj Mabrur is nothing except Paradise.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ১৭৭৩, সহীহ মুসলিম ১৩৪৯",
                    explanationBn = "আল্লাহর নৈকট্য ও জান্নাতের স্থায়ী ঠিকানা অর্জনে মাকবুল হজ্জ এক অবিনশ্বর মাধ্যম।"
                )
            )
        ),

        // 4. দো'আ ও যিকিরের ফযীলত
        HadithTopic(
            id = "topic_dua_dhikr_hadith",
            categoryId = "cat_dua_dhikr",
            nameBn = "দো'আ ও যিকিরের ফযীলত",
            nameEn = "Virtues of Dua, Dhikr & Remembering Allah",
            nameAr = "فضل الدعاء والذكر وعظمة الذاكرين",
            descriptionBn = "দো'আ হলো সমগ্র ইবাদতের মূল নির্যাস। যে ব্যক্তি আল্লাহর স্মরণ করে আর যে করে না, তাদের উপমা জীবিত ও মৃতের মতো।",
            searchKeywordsBn = listOf("দোয়া", "যিকির", "দো'আ", "জিকির", "দোয়া কবুল", "তাসবীহ", "আল্লাহর স্মরণ"),
            searchKeywordsEn = listOf("dua", "dhikr", "remembrance", "supplication", "tasbih", "tirmidhi"),
            isFeatured = true,
            iconEmoji = "🤲",
            relatedTopicIds = listOf("topic_istighfar_tawbah", "topic_salah_importance"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_2969",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজি",
                    hadithNumber = 2969,
                    chapterTitleBn = "দোয়া অধ্যায়",
                    narratorBn = "হযরত নুমান ইবনে বাশীর (রা.)",
                    arabicText = "الدُّعَاءُ هُوَ العِبَادَةُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'দো'আ-ই হলো ইবাদতের মূল।' অতঃপর তিনি তিলাওয়াত করলেন: {তোমাদের প্রতিপালক বলেন, তোমরা আমাকে ডাক, আমি তোমাদের ডাকে সাড়া দেব} [জামে আত-তিরমিজি: ২৯৬৯, সুনানে আবু দাউদ: ১৪৭৯]",
                    englishText = "The Prophet (pbuh) said: 'Supplication (Dua) is worship itself.' Then he recited: {And your Lord says: Call upon Me; I will respond to you.}",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে আত-তিরমিজি ২৯৬৯, আবু দাউদ ১৪৭৯, ইবনে মাজাহ ৩৮২৮",
                    explanationBn = "দো'আর মাধ্যমে বান্দা নিজের অপারগতা ও অক্ষমতা স্বীকার করে এবং আল্লাহর সার্বভৌমত্ব ও অসীম ক্ষমতার স্বীকৃতি দেয়।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_6407",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 6407,
                    chapterTitleBn = "দাওয়া অধ্যায়",
                    narratorBn = "হযরত আবু মূসা আল-আশ'আরী (রা.)",
                    arabicText = "مَثَلُ الَّذِي يَذْكُرُ رَبَّهُ وَالَّذِي لاَ يَذْكُرُ رَبَّهُ، مَثَلُ الحَيِّ وَالمَيِّتِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি তার প্রতিপালককে স্মরণ করে আর যে ব্যক্তি তাঁর স্মরণ করে না, তাদের উপমা জীবিত ও মৃতের মতো।' [সহীহ বুখারী: ৬৪০৭, সহীহ মুসলিম: ৭৭৯]",
                    englishText = "The Prophet (pbuh) said: 'The example of the one who remembers his Lord in comparison to the one who does not remember his Lord is that of a living body in comparison to a dead body.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৬৪০৭, সহীহ মুসলিম ৭৭৯",
                    explanationBn = "হৃদয়ের আসল প্রাণশক্তি হলো আল্লাহর যিকির। যিকিরবিহীন হৃদয় মৃতের মতো অনুভূতিরহিত হয়ে পড়ে।"
                )
            )
        ),

        // 5. দরূদ শরীফ ও রাসুলুল্লাহ (ﷺ)-এর প্রতি ভালোবাসা
        HadithTopic(
            id = "topic_durood_love_prophet",
            categoryId = "cat_dua_dhikr",
            nameBn = "দরূদ শরীফ ও রাসুলুল্লাহ (ﷺ)-এর মহব্বত",
            nameEn = "Durood upon the Prophet & Loving Him",
            nameAr = "فضل الصلاة على النبي ﷺ ومحبته",
            descriptionBn = "রাসুলুল্লাহ (সা.)-এর ওপর একবার দরূদ পাঠে দশটি রহমত নাযিল হয়। নিজের পিতা, সন্তান ও সমগ্র মানুষ অপেক্ষা নবীজিকে বেশি ভালোবাসা ঈমানের অপরিহার্য শর্ত।",
            searchKeywordsBn = listOf("দরূদ", "দরুদ", "নবীজির ভালোবাসা", "সালাত আলা নবী", "রহমত", "মুহাম্মদ", "সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম"),
            searchKeywordsEn = listOf("durood", "salawat", "love for prophet", "muhammad pbuh", "blessings"),
            isFeatured = true,
            iconEmoji = "💚",
            relatedTopicIds = listOf("topic_dua_dhikr_hadith", "topic_iman_tawheed"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_408",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 408,
                    chapterTitleBn = "সালাত অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنْ صَلَّى عَلَيَّ وَاحِدَةً صَلَّى اللَّهُ عَلَيْهِ عَشْرًا",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি আমার প্রতি একবার দরূদ পাঠ করবে, আল্লাহ তা'আলা তার ওপর দশটি রহমত নাযিল করবেন।' [সহীহ মুসলিম: ৪০৮]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Whoever sends blessings upon me once, Allah will send blessings upon him tenfold.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৪০৮, তিরমিজি ৪৮৪",
                    explanationBn = "দরূদ পাঠের মাধ্যমে উম্মত নবীজির প্রতি কৃতজ্ঞতা ও সালাম পেশ করে এবং তার বিনিময়ে আল্লাহর অফুরন্ত রহমতে সিক্ত হয়।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_15",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 15,
                    chapterTitleBn = "ঈমান অধ্যায়",
                    narratorBn = "হযরত আনাস (রা.)",
                    arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى أَكُونَ أَحَبَّ إِلَيْهِ مِنْ وَالِدِهِ وَوَلَدِهِ وَالنَّاسِ أَجْمَعِينَ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমাদের কেউ ততক্ষণ পর্যন্ত পূর্ণ ঈমানদার হতে পারবে না, যতক্ষণ না আমি তার নিকট তার পিতা, সন্তান ও সমস্ত মানুষ অপেক্ষা অধিকতর প্রিয় হই।' [সহীহ বুখারী: ১৫, সহীহ মুসলিম: ৪৪]",
                    englishText = "The Prophet (pbuh) said: 'None of you will have complete faith until I am more beloved to him than his father, his children and all of mankind.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ১৫, সহীহ মুসলিম ৪৪",
                    explanationBn = "নবীপ্রেম ঈমানের প্রাণশক্তি; তাঁর আদর্শ ও সুন্নাতকে জীবনের সর্বক্ষেত্রে সর্বাধিক প্রাধান্য দেওয়াই এই ভালোবাসার দাবি।"
                )
            )
        )
    )
}
