package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart9 {

    val topics: List<HadithTopic> = listOf(
        // 43. জানাযা, কাফন-দাফন ও কবর জিয়ারত (Janazah & Cemetery Rights)
        HadithTopic(
            id = "topic_janazah_cemetery_rights",
            categoryId = "cat_akhirah",
            nameBn = "জানাযা, কাফন-দাফন ও কবর জিয়ারত",
            nameEn = "Funeral (Janazah), Burial & Visiting Graves",
            nameAr = "الجنائز واتباعها وزيارة القبور",
            descriptionBn = "জানাযার নামাজে উপস্থিত হয়ে দুই কিরাত নেকি অর্জনের সুযোগ, দ্রুত দাফন এবং কবর জিয়ারতের মাধ্যমে আখিরাত স্মরণ।",
            searchKeywordsBn = listOf("জানাযা", "দাফন", "কাফন", "কবর জিয়ারত", "কিরাত", "মৃত", "কবরস্থান"),
            searchKeywordsEn = listOf("janazah", "funeral", "burial", "visiting graves", "qirat", "cemetery"),
            isFeatured = true,
            iconEmoji = "⚰️",
            relatedTopicIds = listOf("topic_death_remembrance", "topic_grave_questioning", "topic_brotherhood_unity"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_47",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 47,
                    chapterTitleBn = "কিতাবুল ঈমান (জানাযার অনুগমন ও সওয়াব)",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنِ اتَّبَعَ جَنَازَةَ مُسْلِمٍ، إِيمَانًا وَاحْتِسَابًا، وَكَانَ مَعَهُ حَتَّى يُصَلَّى عَلَيْهَا وَيُفْرَغَ مِنْ دَفْنِهَا، فَإِنَّهُ يَرْجِعُ مِنَ الأَجْرِ بِقِيرَاطَيْنِ، كُلُّ قِيرَاطٍ مِثْلُ أُحُدٍ، وَمَنْ صَلَّى عَلَيْهَا ثُمَّ رَجَعَ قَبْلَ أَنْ تُدْفَنَ، فَإِنَّهُ يَرْجِعُ بِقِيرَاطٍ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'যে ব্যক্তি ঈমানের সাথে ও সওয়াবের আশায় কোনো মুসলিমের জানাযার অনুগমন করে এবং জানাযার সালাত আদায় ও দাফন সম্পন্ন হওয়া পর্যন্ত সাথে থাকে, সে দুই কিরাত সমপরিমাণ সওয়াব নিয়ে ফিরে আসে; যার প্রতিটি কিরাত হলো উহুদ পাহাড়ের সমান বিশাল। আর যে ব্যক্তি শুধু জানাযার সালাত পড়ে দাফনের পূর্বে ফিরে আসে, সে এক কিরাত সওয়াব নিয়ে ফিরে আসে।' [সহীহ আল-বুখারী: ৪৭, সহীহ মুসলিম: ৯৪৫]",
                    englishText = "Whoever follows a funeral procession of a Muslim out of sincere faith and hoping for a reward from Allah, and remains with it until the prayer is offered and the burial is completed, will return with a reward of two Qirats, each Qirat being like Mount Uhud.",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৪৭, সহীহ মুসলিম ৯৪৫",
                    explanationBn = "মুসলিম ভাইয়ের শেষ বিদায়ে অংশ নেওয়া অন্যতম শ্রেষ্ঠ সামাজিক ও দ্বীনি হক, যা প্রচুর সওয়াব বয়ে আনে।",
                    relatedQuranSurahNumber = 9,
                    relatedQuranAyahNumber = 84,
                    relatedQuranAyahRef = "সূরা আত-তাওবাহ (৯:৮৪)"
                ),
                TopicHadithRef(
                    hadithId = "muslim_976",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 976,
                    chapterTitleBn = "কিতাবুল জানায়িজ (কবর জিয়ারতের হুকুম)",
                    narratorBn = "হযরত বুরাইদা (রা.)",
                    arabicText = "نَهَيْتُكُمْ عَنْ زِيَارَةِ الْقُبُورِ فَزُورُوهَا، فَإِنَّهَا تُذَكِّرُ الآخِرَةَ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'আমি তোমাদেরকে আগে কবর জিয়ারত করতে নিষেধ করেছিলাম, এখন তোমরা কবর জিয়ারত করো; কেননা কবর জিয়ারত তোমাদের আখিরাতের কথা স্মরণ করিয়ে দেয়।' [সহীহ মুসলিম: ৯৭৬]",
                    englishText = "I had forbidden you to visit graves, but now visit them, for they will remind you of the Hereafter.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৯৭৬, সুনানে আবু দাউদ ৩২৩২",
                    explanationBn = "কবর জিয়ারত মানুষের অন্তরকে নরম করে, দুনিয়ার মোহ হ্রাস করে এবং মৃত্যুর প্রস্তুতি নিতে উদ্বুদ্ধ করে।",
                    relatedQuranSurahNumber = 102,
                    relatedQuranAyahNumber = 1,
                    relatedQuranAyahRef = "সূরা আত-তাকাসুর (১০২:১-২)"
                )
            )
        ),

        // 44. রাসুলুল্লাহ (ﷺ)-এর শাফা‘আত ও হাউযে কাউসার (Shafa'at & Hawd al-Kawthar)
        HadithTopic(
            id = "topic_shafaat_kawthar",
            categoryId = "cat_akhirah",
            nameBn = "রাসুলুল্লাহ (ﷺ)-এর শাফা‘আত ও হাউযে কাউসার",
            nameEn = "Intercession (Shafa'ah) of the Prophet & Hawd al-Kawthar",
            nameAr = "الشفاعة العظمى والحوض المورود",
            descriptionBn = "কিয়ামতের কঠিন দিনে উম্মতের ক্ষমার জন্য নবীজির সিজদায় আকুল প্রার্থনা, মাকামে মাহমুদ এবং কাউসারের স্নিগ্ধ সুধা পানের সুসংবাদ।",
            searchKeywordsBn = listOf("শাফায়াত", "শাফাআত", "হাউযে কাউসার", "কাউসার", "নবীজি", "মাকামে মাহমুদ", "উম্মত", "কিয়ামত"),
            searchKeywordsEn = listOf("shafaa", "intercession", "kawthar", "basin", "prophet", "maqaam mahmood"),
            isFeatured = true,
            iconEmoji = "⛲",
            relatedTopicIds = listOf("topic_durood_love_prophet", "topic_jannah_deeds", "topic_hell_torments"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_6579",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 6579,
                    chapterTitleBn = "কিতাবুর রিকাক (হাউযে কাউসারের বর্ণনা)",
                    narratorBn = "হযরত আবদুল্লাহ ইবনে আমর (রা.)",
                    arabicText = "حَوْضِي مَسِيرَةُ شَهْرٍ، مَاؤُهُ أَبْيَضُ مِنَ اللَّبَنِ، وَرِيحُهُ أَطْيَبُ مِنَ المِسْكِ، وَكِيزَانُهُ كَنُجُومِ السَّمَاءِ، مَنْ شَرِبَ مِنْهَا فَلاَ يَظْمَأُ أَبَدًا.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) বর্ণনা করেছেন: 'আমার হাউযের বিস্তৃতি এক মাসের দূরত্বের সমান পথ। এর পানি দুধের চেয়েও অধিক শ্বেত-শুভ্র, এর সুবাস কস্তুরীর চেয়েও সুগন্ধিময়, আর এর পানপাত্রসমূহ আকাশের তারকারাজির ন্যায় অসংখ্য ও উজ্জ্বল। যে ব্যক্তি তা থেকে একবার পান করবে, সে আর কখনো তৃষ্ণার্ত হবে না।' [সহীহ আল-বুখারী: ৬৫৭৯, সহীহ মুসলিম: ২২৯২]",
                    englishText = "My lake-fount (Kawthar) is a month's journey wide; its water is whiter than milk and its smell is better than musk, and its drinking cups are as numerous as the stars of the sky. A person who drinks from it will never be thirsty again.",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৬৫৭৯, সহীহ মুসলিম ২২৯২",
                    explanationBn = "কিয়ামতের তীব্র উত্তাপে উম্মতে মুহাম্মাদীর জন্য হাউযে কাউসার হবে পরম শীতল ও চিরন্তন তৃপ্তি।",
                    relatedQuranSurahNumber = 108,
                    relatedQuranAyahNumber = 1,
                    relatedQuranAyahRef = "সূরা আল-কাউসার (১০৮:১-৩)"
                ),
                TopicHadithRef(
                    hadithId = "tirmidhi_2435",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজী",
                    hadithNumber = 2435,
                    chapterTitleBn = "কিতাবু সিফাতিল কিয়ামাহ (গুনাহগারদের শাফায়াত)",
                    narratorBn = "হযরত আনাস ইবনে মালিক (রা.)",
                    arabicText = "شَفَاعَتِي لِأَهْلِ الْكَبَائِرِ مِنْ أُمَّتِي.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) সুসংবাদ দিয়েছেন: 'আমার বিশেষ শাফা‘আত (সুপারিশ) সংরক্ষিত থাকবে আমার উম্মতের কবিরা গুনাহগারদের ক্ষমার জন্য।' [জামে আত-তিরমিজী: ২৪৩৫, সুনানে আবু দাউদ: ৪৭৩৯]",
                    englishText = "My intercession is for those among my Ummah who have committed major sins.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে আত-তিরমিজী ২৪৩৫, সুনানে আবু দাউদ ৪৭৩৯",
                    explanationBn = "রাসুলুল্লাহ (ﷺ)-এর অসীম রহমত ও সুপারিশের মাধ্যমে অসংখ্য তাওহীদবাদী গুনাহগার জাহান্নাম থেকে মুক্তি পেয়ে জান্নাতে প্রবেশ করবে।",
                    relatedQuranSurahNumber = 17,
                    relatedQuranAyahNumber = 79,
                    relatedQuranAyahRef = "সূরা আল-ইসরা (১৭:৭৯)"
                )
            )
        ),

        // 45. ঘুম, জাগ্রত হওয়া ও সত্য স্বপ্নের আদব (Sleep & Dreams)
        HadithTopic(
            id = "topic_sleeping_dream_sunnah",
            categoryId = "cat_health",
            nameBn = "ঘুম, জাগ্রত হওয়া ও সত্য স্বপ্নের আদব",
            nameEn = "Etiquette of Sleep, Waking Up & True Dreams",
            nameAr = "آداب النوم والاستيقاظ والرؤيا الصالحة",
            descriptionBn = "ডান কাতে ঘুমানো, ঘুমানোর ও ওঠার মাসনূন দোয়া, দুঃস্বপ্ন দেখলে করণীয় এবং মুমিনের সত্য স্বপ্নের তাৎপর্য।",
            searchKeywordsBn = listOf("ঘুম", "স্বপ্ন", "জেগে ওঠা", "ডান কাত", "দোয়া", "দুঃস্বপ্ন", "সত্য স্বপ্ন"),
            searchKeywordsEn = listOf("sleep", "waking", "dream", "ruya", "night", "supplication", "adab"),
            isFeatured = true,
            iconEmoji = "🛌",
            relatedTopicIds = listOf("topic_dua_dhikr_hadith", "topic_taharah_wudu", "topic_tahajjud_qiyam"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_6312",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 6312,
                    chapterTitleBn = "কিতাবুদ দাওয়াত (ঘুমানো ও ওঠার দোয়া)",
                    narratorBn = "হযরত হুযাইফা (রা.)",
                    arabicText = "كَانَ النَّبِيُّ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ إِذَا أَوَى إِلَى فِرَاشِهِ قَالَ: بِاسْمِكَ أَمُوتُ وَأَحْيَا. وَإِذَا اسْتَيْقَظَ قَالَ: الحَمْدُ لِلَّهِ الَّذِي أَحْيَانَا بَعْدَ مَا أَمَاتَنَا وَإِلَيْهِ النُّشُورُ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) যখন তাঁর বিছানায় শয়ন করতে যেতেন তখন বলতেন: 'বিসমিকা আমূতু ওয়া আহয়াহ' (হে আল্লাহ! আপনারই নামে আমি মরি ও বাঁচি)। আর যখন ঘুম থেকে জাগ্রত হতেন তখন বলতেন: 'আলহামদু লিল্লাহিল্লাযী আহইয়ানা বা'দা মা আমাতানা ওয়া ইলাইহিন নুশূর' (সমস্ত প্রশংসা সেই আল্লাহর যিনি আমাদেরকে মৃত্যুর মতো নিদ্রা দেওয়ার পর পুনরায় জীবিত করলেন, আর তাঁরই দিকে আমাদের পুনরুত্থান)। [সহীহ আল-বুখারী: ৬৩১২, সহীহ মুসলিম: ২৭১১]",
                    englishText = "When the Prophet went to bed, he would say: 'In Your Name I die and I live.' And when he woke up, he would say: 'All praise is due to Allah Who gave us life after having given us death and unto Him is the resurrection.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৬৩১২, সহীহ মুসলিম ২৭১১",
                    explanationBn = "ঘুম হলো মৃত্যুর একটি ক্ষুদ্র রূপ। শোয়ার সময় আল্লাহর আশ্রয়ে যাওয়া ও ওঠার পর শোকর আদায় করা সুন্নাত।",
                    relatedQuranSurahNumber = 39,
                    relatedQuranAyahNumber = 42,
                    relatedQuranAyahRef = "সূরা আয-যুমার (৩৯:৪২)"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_6984",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 6984,
                    chapterTitleBn = "কিতাবুত তা'বীর (ভালো স্বপ্ন ও দুঃস্বপ্ন)",
                    narratorBn = "হযরত আবু ক্বাতাদা (রা.)",
                    arabicText = "الرُّؤْيَا الصَّالِحَةُ مِنَ اللَّهِ، وَالحُلُمُ مِنَ الشَّيْطَانِ، فَمَنْ رَأَى شَيْئًا يَكْرَهُهُ فَلْيَنْفُثْ عَنْ شِمَالِهِ ثَلاَثًا، وَلْيَتَعَوَّذْ بِاللَّهِ مِنْ شَرِّهَا، فَإِنَّهَا لَنْ تَضُرَّهُ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'উত্তম ও সত্য স্বপ্ন আল্লাহর পক্ষ থেকে, আর দুঃস্বপ্ন শয়তানের পক্ষ থেকে। সুতরাং তোমাদের কেউ যদি কোনো অপছন্দনীয় স্বপ্ন দেখে, সে যেন তার বাম দিকে তিনবার হালকা থুতু নিক্ষেপ করে এবং এর অনিষ্ট থেকে আল্লাহর আশ্রয় চায়; তবে তা তার কোনো ক্ষতি করতে পারবে না।' [সহীহ আল-বুখারী: ৬৯৮৪, সহীহ মুসলিম: ২২৬১]",
                    englishText = "A good dream is from Allah, and a bad dream is from Satan. So whoever sees something he dislikes, let him spit to his left three times and seek refuge with Allah from its evil, for it will not harm him.",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৬৯৮৪, সহীহ মুসলিম ২২৬১",
                    explanationBn = "খারাপ স্বপ্ন দেখলে বিচলিত না হয়ে বাম দিকে তিনবার আউযুবিল্লাহ পড়ে থুতুর ভাব করে পাশ ফিরে শোয়া সুন্নাত এবং তা কারো কাছে প্রকাশ করা উচিত নয়।",
                    relatedQuranSurahNumber = 12,
                    relatedQuranAyahNumber = 4,
                    relatedQuranAyahRef = "সূরা ইউসুফ (১২:৪-৫)"
                )
            )
        ),

        // 46. পোশাক ও সৌন্দর্যের সুন্নাত ও আদব (Clothing & Modesty)
        HadithTopic(
            id = "topic_clothing_adornment_sunnah",
            categoryId = "cat_taharah",
            nameBn = "পোশাক ও সৌন্দর্যের সুন্নাত ও আদব",
            nameEn = "Sunnah Etiquette of Dress, Cleanliness & Adornment",
            nameAr = "آداب اللباس والزينة الشرعية",
            descriptionBn = "সাদা পোশাকের শ্রেষ্ঠত্ব, শালীনতা ও সৌন্দর্যচর্চার সুন্নাত বিধান এবং অহংকারবশত পোশাক ঝুলিয়ে পরার কঠোর নিষেধাজ্ঞা।",
            searchKeywordsBn = listOf("পোশাক", "কাপড়", "সাদা পোশাক", "টাকনুর নিচে", "লেবাস", "সৌন্দর্য", "সুগন্ধি"),
            searchKeywordsEn = listOf("clothing", "dress", "garments", "white dress", "ankles", "adornment", "sunnah"),
            isFeatured = true,
            iconEmoji = "👔",
            relatedTopicIds = listOf("topic_taharah_wudu", "topic_humility_tawadu", "topic_modesty_haya"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_994",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজী",
                    hadithNumber = 994,
                    chapterTitleBn = "কিতাবুল জানায়িজ (সাদা কাপড়ের ফযীলত)",
                    narratorBn = "হযরত সামুরা ইবনে জুনদুব (রা.)",
                    arabicText = "الْبَسُوا الثِّيَابَ الْبَيَاضَ، فَإِنَّهَا أَطْهَرُ وَأَطْيَبُ، وَكَفِّنُوا فِيهَا مَوْتَاكُمْ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) নির্দেশ দিয়েছেন: 'তোমরা সাদা রঙের পোশাক পরিধান করো; কেননা তা সর্বাধিক পবিত্র ও সুন্দর। আর সাদা কাপড় দিয়েই তোমাদের মৃতদের কাফন পরাও।' [জামে আত-তিরমিজী: ৯৯৪, সুনানে আবু দাউদ: ৪০৬১]",
                    englishText = "Wear white clothes, for they are purer and better, and shroud your dead in them.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে আত-তিরমিজী ৯৯৪, সুনানে আবু দাউদ ৪০৬১",
                    explanationBn = "সাদা পোশাকে পবিত্রতা ও নম্রতা ফুটে ওঠে।",
                    relatedQuranSurahNumber = 7,
                    relatedQuranAyahNumber = 26,
                    relatedQuranAyahRef = "সূরা আল-আ'রাফ (৭:২৬)"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_5787",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 5787,
                    chapterTitleBn = "কিতাবুল লিবাস (টাকনুর নিচে ঝুলানো কাপড়)",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَا أَسْفَلَ مِنَ الكَعْبَيْنِ مِنَ الإِزَارِ فَفِي النَّارِ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) কঠোর সতর্কবার্তা দিয়ে বলেছেন: 'লুঙ্গি বা কাপড়ের যে অংশ দুই টাকনুর নিচে ঝুলিয়ে পরা হয়, সে অংশ জাহান্নামের আগুনে পুড়বে।' [সহীহ আল-বুখারী: ৫৭৮৭]",
                    englishText = "Whatever of the lower garment is below the ankles is in the Fire.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৫৭৮৭, সুনানে নাসাঈ ৫৩৩০",
                    explanationBn = "পুরুষদের জন্য পোশাক পরিধানে অহংকার প্রকাশ পায় এমনভাবে ঝুলিয়ে পরা কঠোরভাবে নিষিদ্ধ। সর্বদা টাকনুর উপরে পোশাক রাখা সুন্নাত।",
                    relatedQuranSurahNumber = 7,
                    relatedQuranAyahNumber = 31,
                    relatedQuranAyahRef = "সূরা আল-আ'রাফ (৭:৩১)"
                )
            )
        ),

        // 47. দাওয়াত ও হিকমতের সাথে দ্বীনের প্রচার (Da'wah & Wisdom)
        HadithTopic(
            id = "topic_dawah_enjoining_good",
            categoryId = "cat_ilm",
            nameBn = "দাওয়াত ও হিকমতের সাথে দ্বীনের প্রচার",
            nameEn = "Calling to Allah (Da'wah), Wisdom & Guiding Others",
            nameAr = "الدعوة إلى الله بالحكمة والموعظة الحسنة",
            descriptionBn = "একটি আয়াত হলেও মানুষের কাছে পৌঁছে দেওয়ার তাগিদ, হেদায়েতের উসিলা হওয়ার অসীম সৌভাগ্য এবং ভালো কাজের দিশা দেওয়ার সওয়াব।",
            searchKeywordsBn = listOf("দাওয়াত", "প্রচার", "হেদায়েত", "তাবলীগ", "একটি আয়াত", "সওয়াব", "আলেম"),
            searchKeywordsEn = listOf("dawah", "conveying", "guiding", "one verse", "reward", "calling to islam"),
            isFeatured = true,
            iconEmoji = "📢",
            relatedTopicIds = listOf("topic_ilm_virtue", "topic_nasiha_dawah", "topic_enjoining_good_stopping_evil"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_3461",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 3461,
                    chapterTitleBn = "কিতাবুল আম্বিয়া (একটি আয়াত হলেও পৌঁছে দাও)",
                    narratorBn = "হযরত আবদুল্লাহ ইবনে আমর (রা.)",
                    arabicText = "بَلِّغُوا عَنِّي وَلَوْ آيَةً.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'আমার পক্ষ থেকে একটি আয়াত বা একটি বাণী হলেও মানুষের কাছে পৌঁছে দাও।' [সহীহ আল-বুখারী: ৩৪৬১]",
                    englishText = "Convey from me, even if it is only a single verse.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৩৪৬১, জামে আত-তিরমিজী ২৬৬৯",
                    explanationBn = "দ্বীনের সঠিক ইলম নিজে জানা মাত্রই অন্য ভাইয়ের কাছে পৌঁছে দেওয়া প্রতিটি মুসলিমের ঈমানী কর্তব্য।",
                    relatedQuranSurahNumber = 16,
                    relatedQuranAyahNumber = 125,
                    relatedQuranAyahRef = "সূরা আন-নাহল (১৬:১২৫)"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_3701",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 3701,
                    chapterTitleBn = "কিতাবুল মাগাযী (খায়বার যুদ্ধে আলীকে নসীহত)",
                    narratorBn = "হযরত সাহল ইবনে সা'দ (রা.)",
                    arabicText = "فَوَاللَّهِ لَأَنْ يَهْدِيَ اللَّهُ بِكَ رَجُلًا وَاحِدًا، خَيْرٌ لَكَ مِنْ أَنْ يَكُونَ لَكَ حُمْرُ النَّعَمِ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) হযরত আলী (রা.)-কে বললেন: 'আল্লাহর শপথ! তোমার মাধ্যমে যদি আল্লাহ কেবল একজন মানুষকেও হিদায়েতের আলো দান করেন, তবে তা তোমার জন্য সবচেয়ে মূল্যবান লাল উট (পার্থিব অতুলনীয় ঐশ্বর্য) থাকার চেয়েও অনেক বেশি কল্যাণকর ও শ্রেষ্ঠ।' [সহীহ আল-বুখারী: ৩৭০১, সহীহ মুসলিম: ২৪০৬]",
                    englishText = "By Allah, if Allah were to guide one man through you, it would be better for you than possessing red camels.",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৩৭০১, সহীহ মুসলিম ২৪০৬",
                    explanationBn = "কাউকে কল্যাণের দিকে পথ দেখানো ও গুনাহ থেকে ফেরানো দুনিয়ার তাবৎ সম্পদের চেয়ে শ্রেষ্ঠ অর্জন।",
                    relatedQuranSurahNumber = 41,
                    relatedQuranAyahNumber = 33,
                    relatedQuranAyahRef = "সূরা ফুসসিলাত (৪১:৩৩)"
                ),
                TopicHadithRef(
                    hadithId = "muslim_1893",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 1893,
                    chapterTitleBn = "কিতাবুল ইমারাহ (সৎকাজের পথপ্রদর্শকের সওয়াব)",
                    narratorBn = "হযরত আবু মাসউদ আল-আনসারী (রা.)",
                    arabicText = "مَنْ دَلَّ عَلَى خَيْرٍ فَلَهُ مِثْلُ أَجْرِ فَاعِلِهِ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'যে ব্যক্তি কোনো নেক বা ভালো কাজের পথ প্রদর্শন করে, সে ওই কাজ সম্পাদনকারীর সমান সওয়াব লাভ করবে।' [সহীহ মুসলিম: ১৮৯৩]",
                    englishText = "Whoever directs someone to a good deed will have the reward equal to the one who does it.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ১৮৯৩, জামে আত-তিরমিজী ২৬৭১",
                    explanationBn = "ভালো কাজের দিকনির্দেশনা দিলে যারা তা আমল করবে তাদের সমপরিমাণ নেকি দিশারীর আমলনামায় লিপিবদ্ধ হবে।",
                    relatedQuranSurahNumber = 3,
                    relatedQuranAyahNumber = 104,
                    relatedQuranAyahRef = "সূরা আলে ইমরান (৩:১০৪)"
                )
            )
        ),

        // 48. শোক প্রকাশ, মুসিবতে সবর ও অশ্রুপাত (Bereavement & Patience)
        HadithTopic(
            id = "topic_bereavement_musibah",
            categoryId = "cat_akhlaq",
            nameBn = "শোক প্রকাশ, মুসিবতে সবর ও অশ্রুপাত",
            nameEn = "Patience during Bereavement, Grief & Calamity",
            nameAr = "الصبر عند المصائب والبكاء بدون نياحة",
            descriptionBn = "স্বজন হারানোর বেদনায় চোখের পানি ফেলা রহমতের লক্ষণ কিন্তু বিলাপের নিষেধাজ্ঞা, প্রথম আঘাতেই সবরের গুরুত্ব।",
            searchKeywordsBn = listOf("শোক", "মৃত্যু", "সবর", "ধৈর্য", "বিপদ", "অশ্রু", "বিলাপ", "ইন্নালিল্লাহ"),
            searchKeywordsEn = listOf("bereavement", "grief", "patience", "tears", "loss", "mourning", "sabr"),
            isFeatured = true,
            iconEmoji = "💧",
            relatedTopicIds = listOf("topic_sabr_hardship", "topic_death_remembrance", "topic_tawakkul_qadar"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_1303",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 1303,
                    chapterTitleBn = "কিতাবুল জানায়িজ (সন্তানের মৃত্যুতে অশ্রুপাত)",
                    narratorBn = "হযরত আনাস ইবনে মালিক (রা.)",
                    arabicText = "تَدْمَعُ العَيْنُ وَيَحْزَنُ القَلْبُ، وَلاَ نَقُولُ إِلَّا مَا يَرْضَى رَبُّنَا، وَإِنَّا بِفِرَاقِكَ يَا إِبْرَاهِيمُ لَمَحْزُونُونَ.",
                    banglaText = "প্রিয় পুত্র ইব্রাহীমের ইন্তেকালে রাসুলুল্লাহ (ﷺ)-এর চোখ বেয়ে অশ্রু ঝরছিল। তখন তিনি বললেন: 'নিশ্চয়ই চোখ অশ্রুসজল হয় এবং অন্তর ব্যথাতুর হয়; কিন্তু আমরা মুখে কেবল এমন কথাই বলব যাতে আমাদের মহান রব সন্তুষ্ট হন। আর হে ইব্রাহীম! তোমার বিচ্ছেদে আমরা অবশ্যই গভীর শোকাহত।' [সহীহ আল-বুখারী: ১৩০৩, সহীহ মুসলিম: ২৩১৫]",
                    englishText = "The eyes weep and the heart grieves, but we do not say anything except that which pleases our Lord, and O Ibrahim, we are grieved by your departure.",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ১৩০৩, সহীহ মুসলিম ২৩১৫",
                    explanationBn = "প্রিয়জনের মৃত্যুতে কান্না করা মানবীয় ভালোবাসা ও রহমতের বহিঃপ্রকাশ। তবে বুক চাপড়ানো, চিৎকার করে বিলাপ করা বা আল্লাহর ফয়সালার বিরুদ্ধে অসন্তোষ প্রকাশ করা হারাম।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 155,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:১৫৫-১৫৬)"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_1283",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 1283,
                    chapterTitleBn = "কিতাবুল জানায়িজ (প্রথম আঘাতের সবর)",
                    narratorBn = "হযরত আনাস ইবনে মালিক (রা.)",
                    arabicText = "إِنَّمَا الصَّبْرُ عِنْدَ الصَّدْمَةِ الأُولَى.",
                    banglaText = "কবরের পাশে ক্রন্দনরত এক নারীকে রাসুলুল্লাহ (ﷺ) ধৈর্য ধারণের উপদেশ দিলে পরবর্তীতে তিনি তাকে বললেন: 'প্রকৃত সবর বা ধৈর্য হলো বিপদের প্রথম ধাক্কার মুহূর্তে।' [সহীহ আল-বুখারী: ১২৮৩, সহীহ মুসলিম: ৯২৬]",
                    englishText = "True patience is only at the first stroke of a calamity.",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ১২৮৩, সহীহ মুসলিম ৯২৬",
                    explanationBn = "বিপদ ঘটামাত্রই নিজেকে নিয়ন্ত্রণ করে আল্লাহর ওপর সমর্পণ করাই আসল সবর, যা অফুরন্ত পুরস্কার বয়ে আনে।",
                    relatedQuranSurahNumber = 39,
                    relatedQuranAyahNumber = 10,
                    relatedQuranAyahRef = "সূরা আয-যুমার (৩৯:১০)"
                )
            )
        ),

        // 49. ফিতনা থেকে নিরাপত্তা ও দাজ্জালের আক্রমণ থেকে সুরক্ষা (Protection from Fitnah & Dajjal)
        HadithTopic(
            id = "topic_protection_fitnah_dajjal",
            categoryId = "cat_fitnah",
            nameBn = "ফিতনা ও দাজ্জালের আক্রমণ থেকে সুরক্ষা",
            nameEn = "Protection from Fitnah & The Trials of Dajjal",
            nameAr = "الاعتصام من الفتن وعصمة الدجال",
            descriptionBn = "শেষ যুগের কঠিন ঈমানী বিপর্যয়, সূরা কাহাফের সুরক্ষাকবচ এবং সালাতের তাশাহহুদে চার ফিতনা থেকে মুক্তির আকুল প্রার্থনা।",
            searchKeywordsBn = listOf("দাজ্জাল", "ফিতনা", "কাহাফ", "সুরক্ষা", "শেষ যমানা", "ঈমান রক্ষা", "তাশাহহুদ"),
            searchKeywordsEn = listOf("fitnah", "dajjal", "protection", "surah kahf", "end times", "trials"),
            isFeatured = true,
            iconEmoji = "🛡️",
            relatedTopicIds = listOf("topic_qiyamah_signs", "topic_iman_tawheed", "topic_sabr_hardship"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_809",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 809,
                    chapterTitleBn = "কিতাবু সালাতিল মুসাফিরীন (সূরা কাহাফের ফযীলত)",
                    narratorBn = "হযরত আবু দারদা (রা.)",
                    arabicText = "مَنْ حَفِظَ عَشْرَ آيَاتٍ مِنْ أَوَّلِ سُورَةِ الْكَهْفِ عُصِمَ مِنَ الدَّجَّالِ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) ইরশাদ করেছেন: 'যে ব্যক্তি সূরা আল-কাহাফের প্রথম দশটি আয়াত মুখস্থ করবে ও তিলাওয়াত করবে, সে দাজ্জালের সর্বনাশা ফিতনা থেকে সুরক্ষিত থাকবে।' [সহীহ মুসলিম: ৮০৯]",
                    englishText = "Whoever commits to memory the first ten verses of Surat Al-Kahf will be protected from the Dajjal.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৮০৯, সুনানে আবু দাউদ ৪৩২৩",
                    explanationBn = "দাজ্জাল হবে মানব ইতিহাসের সবচেয়ে ভয়াবহ ফিতনা। সূরা কাহাফের তাওহীদ ও অলৌকিক ঘটনার পাঠ মুমিনের ঈমানকে অটল রাখে।",
                    relatedQuranSurahNumber = 18,
                    relatedQuranAyahNumber = 1,
                    relatedQuranAyahRef = "সূরা আল-কাহাফ (১৮:১-১০)"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_1377",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ আল-বুখারী",
                    hadithNumber = 1377,
                    chapterTitleBn = "কিতাবুল জানায়িজ (চার ফিতনা থেকে আশ্রয়)",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنْ عَذَابِ القَبْرِ، وَمِنْ عَذَابِ النَّارِ، وَمِنْ فِتْنَةِ المَحْيَا وَالمَمَاتِ، وَمِنْ فِتْنَةِ المَسِيحِ الدَّجَّالِ.",
                    banglaText = "রাসুলুল্লাহ (ﷺ) সালাতের তাশাহহুদ শেষে এই দো‘আ করার নির্দেশ দিতেন: 'হে আল্লাহ! আমি আপনার নিকট কবরের আযাব থেকে আশ্রয় চাই, জাহান্নামের আযাব থেকে আশ্রয় চাই, জীবন ও মৃত্যুর ফিতনা থেকে আশ্রয় চাই এবং কানা দাজ্জালের মারাত্মক ফিতনা থেকে আশ্রয় চাই।' [সহীহ আল-বুখারী: ১৩৭৭, সহীহ মুসলিম: ৫৮৮]",
                    englishText = "O Allah, I seek refuge with You from the punishment of the grave, from the punishment of the Fire, from the trials of life and death, and from the evil trial of the False Messiah (Dajjal).",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ১৩৭৭, সহীহ মুসলিম ৫৮৮",
                    explanationBn = "প্রতিটি সালাতে সালাম ফেরানোর পূর্বে এই চার বিপদ থেকে আশ্রয় চাওয়া সুন্নাতে মুয়াক্কাদাহ।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 217,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:২১৭)"
                )
            )
        )
    )
}
