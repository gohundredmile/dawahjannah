package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart4 {

    val topics: List<HadithTopic> = listOf(
        // 1. প্রতিবেশীর অধিকার ও মর্যাদা
        HadithTopic(
            id = "topic_neighbor_rights",
            categoryId = "cat_society",
            nameBn = "প্রতিবেশীর অধিকার ও মর্যাদা",
            nameEn = "Rights of Neighbors in Islam",
            nameAr = "حقوق الجار والإحسان إليه",
            descriptionBn = "প্রতিবেশীর হক এত বেশি যে জিবরীল (আ.)-এর বারবার তাগিদের কারণে নবীজি মনে করেছিলেন প্রতিবেশীকে হয়তো ওয়ারিশ বানিয়ে দেওয়া হবে। প্রতিবেশীকে কষ্ট দেওয়া ঈমানহীনতার লক্ষণ।",
            searchKeywordsBn = listOf("প্রতিবেশী", "পড়শী", "প্রতিবেশীর হক", "কষ্ট না দেওয়া", "খাবার পাঠানো", "ওয়ারিশ"),
            searchKeywordsEn = listOf("neighbor", "rights of neighbor", "kindness to neighbor", "bukhari 6014"),
            isFeatured = true,
            iconEmoji = "🏡",
            relatedTopicIds = listOf("topic_brotherhood_society", "topic_parents_rights"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_6014",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 6014,
                    chapterTitleBn = "শিষ্টাচার অধ্যায়",
                    narratorBn = "উম্মুল মুমিনীন হযরত আয়েশা (রা.)",
                    arabicText = "مَا زَالَ جِبْرِيلُ يُوصِينِي بِالجَارِ، حَتَّى ظَنَنْتُ أَنَّهُ سَيُوَرِّثُهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'জিবরীল (আ.) আমাকে প্রতিবেশীর হকের ব্যাপারে অনবরত এত বেশি উপদেশ ও তাগিদ দিচ্ছিলেন যে আমার মনে হচ্ছিল হয়তো তিনি অচিরেই প্রতিবেশীকে উত্তরাধিকারী (ওয়ারিশ) বানিয়ে দেবেন।' [সহীহ বুখারী: ৬০১৪, সহীহ মুসলিম: ২৬২৪]",
                    englishText = "The Prophet (pbuh) said: 'Gabriel continued to recommend me about treating the neighbor with kindness, until I thought he would assign him a share of inheritance.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৬০১৪, সহীহ মুসলিম ২৬২৪",
                    explanationBn = "ইসলামে প্রতিবেশীর গুরুত্ব অপরিসীম। মুসলিম হোক বা অমুসলিম, প্রতিবেশীর বিপদে পাশে দাঁড়ানো এবং তাকে কষ্ট না দেওয়া ঈমানের দাবি।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_6018",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 6018,
                    chapterTitleBn = "শিষ্টাচার অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَاليَوْمِ الآخِرِ فَلاَ يُؤْذِ جَارَهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি আল্লাহ ও শেষ দিবসে বিশ্বাস রাখে, সে যেন তার প্রতিবেশীকে কোনো প্রকার কষ্ট না দেয়।' [সহীহ বুখারী: ৬০১৮, সহীহ মুসলিম: ৪৭]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Whoever believes in Allah and the Last Day, let him not harm his neighbor.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৬০১৮, সহীহ মুসলিম ৪৭",
                    explanationBn = "প্রতিবেশীকে কথায় বা আচরণে কষ্ট দেওয়া প্রকৃত ঈমানের পরিপন্থী।"
                )
            )
        ),

        // 2. আত্মীয়তার সম্পর্ক রক্ষা (সিলাতুর রাহিম)
        HadithTopic(
            id = "topic_kinship_silat_rahim",
            categoryId = "cat_family",
            nameBn = "আত্মীয়তার সম্পর্ক রক্ষা (সিলাতুর রাহিম)",
            nameEn = "Maintaining Ties of Kinship (Silat ar-Rahim)",
            nameAr = "صلة الرحم والتحذير من قطيعتها",
            descriptionBn = "আত্মীয়তার সম্পর্ক বজায় রাখলে রিজিক ও আয়ুতে বরকত হয়। পক্ষান্তরে রক্তসম্পর্ক ছিন্নকারীর জন্য জান্নাত নিষিদ্ধ।",
            searchKeywordsBn = listOf("আত্মীয়তা", "রক্তের সম্পর্ক", "সিলাতুর রাহিম", "রিজিক বৃদ্ধি", "আয়ু বৃদ্ধি", "সম্পর্ক ছিন্ন"),
            searchKeywordsEn = listOf("kinship", "relatives", "silat rahim", "family ties", "longevity"),
            isFeatured = true,
            iconEmoji = "👨‍👩‍👧‍👦",
            relatedTopicIds = listOf("topic_parents_rights", "topic_brotherhood_society"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_2067",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 2067,
                    chapterTitleBn = "ক্রয়-বিক্রয় অধ্যায়",
                    narratorBn = "হযরত আনাস ইবনে মালিক (রা.)",
                    arabicText = "مَنْ أَحَبَّ أَنْ يُبْسَطَ لَهُ فِي رِزْقِهِ، وَيُنْسَأَ لَهُ فِي أَثَرِهِ، فَلْيَصِلْ رَحِمَهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি পছন্দ করে যে তার রিজিকে প্রাচুর্য হোক এবং তার আয়ু দীর্ঘায়িত করা হোক, সে যেন তার আত্মীয়তার সম্পর্ক বজায় রাখে।' [সহীহ বুখারী: ২০৬৭, সহীহ মুসলিম: ২৫৫৭]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Whoever loves that he be granted more wealth and that his life be prolonged, then let him keep good relations with his kith and kin.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ২০৬৭, সহীহ মুসলিম ২৫৫৭",
                    explanationBn = "আত্মীয়দের খোঁজখবর রাখা ও তাদের অর্থনৈতিক বা মানসিক সহযোগিতা প্রদান আয়ু ও রিজিকে বাস্তব বরকত নিয়ে আসে।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_5984",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 5984,
                    chapterTitleBn = "শিষ্টাচার অধ্যায়",
                    narratorBn = "হযরত জুবায়ের ইবনে মুতইম (রা.)",
                    arabicText = "لاَ يَدْخُلُ الجَنَّةَ قَاطِعٌ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'আত্মীয়তার সম্পর্ক ছিন্নকারী জান্নাতে প্রবেশ করবে না।' [সহীহ বুখারী: ৫৯৮৪, সহীহ মুসলিম: ২৫৫৬]",
                    englishText = "The Prophet (pbuh) said: 'The person who severs the bond of kinship will not enter Paradise.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৫৯৮৪, সহীহ মুসলিম ২৫৫৬",
                    explanationBn = "তুচ্ছ পার্থিব স্বার্থে আত্মীয়দের সাথে সম্পর্ক ছিন্ন করা কবিরা গুনাহ এবং পরকালে শাস্তির কারণ।"
                )
            )
        ),

        // 3. বিবাহ ও স্ত্রীর সাথে উত্তম আচরণ
        HadithTopic(
            id = "topic_marriage_family_hadith",
            categoryId = "cat_family",
            nameBn = "বিবাহ ও স্ত্রীর সাথে উত্তম আচরণ",
            nameEn = "Marriage & Gentle Treatment of Wives",
            nameAr = "الحث على النكاح وحسن معاشرة الزوجة",
            descriptionBn = "সামর্থ্য থাকলে বিবাহ করা সুন্নাত ও দৃষ্টির সুরক্ষা। সেই ব্যক্তিই উম্মতের মধ্যে সর্বোত্তম যে তার স্ত্রীর নিকট সর্বাধিক ভালো।",
            searchKeywordsBn = listOf("বিবাহ", "বিয়ে", "স্ত্রী", "স্বামী", "দাম্পত্য", "স্ত্রীর হক", "সর্বোত্তম পুরুষ"),
            searchKeywordsEn = listOf("marriage", "nikah", "wife", "husband", "kindness to spouse"),
            isFeatured = true,
            iconEmoji = "💍",
            relatedTopicIds = listOf("topic_parents_rights", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_3895",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজি",
                    hadithNumber = 3895,
                    chapterTitleBn = "মনাাকিব অধ্যায়",
                    narratorBn = "উম্মুল মুমিনীন হযরত আয়েশা (রা.)",
                    arabicText = "خَيْرُكُمْ خَيْرُكُمْ لِأَهْلِهِ وَأَنَا خَيْرُكُمْ لِأَهْلِي",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমাদের মধ্যে সেই ব্যক্তিই সর্বোত্তম, যে তার পরিবারের (স্ত্রীর) কাছে সর্বোত্তম। আর আমি আমার পরিবারের কাছে তোমাদের চেয়ে সবচেয়ে বেশি উত্তম।' [জামে আত-তিরমিজি: ৩৮৯৫, সুনানে ইবনে মাজাহ: ১৯৭৭]",
                    englishText = "The Messenger of Allah (pbuh) said: 'The best of you is the one who is best to his family, and I am the best among you to my family.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "তিরমিজি ৩৮৯৫, ইবনে মাজাহ ১৯৭৭, সহীহ ইবনে হিব্বান ৪১৭৭",
                    explanationBn = "বাইরে ভালো মানুষ সাজা সহজ, কিন্তু ঘরে স্ত্রীর সামনে অমায়িক, ধৈর্যশীল ও প্রেমময় আচরণ বজায় রাখাই আসল উন্নত চরিত্রের মাপকাঠি।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_5066",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 5066,
                    chapterTitleBn = "বিবাহ অধ্যায়",
                    narratorBn = "হযরত আবদুল্লাহ ইবনে মাসউদ (রা.)",
                    arabicText = "يَا مَعْشَرَ الشَّبَابِ، مَنِ اسْتَطَاعَ البَاءَةَ فَلْيَتَزَوَّجْ، فَإِنَّهُ أَغَضُّ لِلْبَصَرِ وَأَحْصَنُ لِلْفَرْجِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'হে যুবসমাজ! তোমাদের মধ্যে যার বিবাহের সামর্থ্য আছে, সে যেন বিবাহ করে নেয়। কারণ বিবাহ দৃষ্টিকে সবচেয়ে বেশি সংযত রাখে এবং লজ্জাস্থানকে পুণ্যবান ও সংরক্ষিত করে।' [সহীহ বুখারী: ৫০৬৬, সহীহ মুসলিম: ১৪০০]",
                    englishText = "The Prophet (pbuh) said: 'O young people! Whoever among you can marry, should marry, because it helps him lower his gaze and guard his modesty.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৫০৬৬, সহীহ মুসলিম ১৪০০",
                    explanationBn = "বিবাহ চরিত্র রক্ষার দুর্গ এবং যুবসমাজকে পাপাচার থেকে সুরক্ষার নববী বিধান।"
                )
            )
        ),

        // 4. মেহমানদারী ও আতিথেয়তা
        HadithTopic(
            id = "topic_hospitality_guests",
            categoryId = "cat_society",
            nameBn = "মেহমানদারী ও আতিথেয়তা",
            nameEn = "Hospitality & Generosity to Guests",
            nameAr = "إكرام الضيف وحق الضيافة",
            descriptionBn = "মেহমানের প্রতি সম্মান প্রদর্শন ও তাকে আতিথেয়তা করা আল্লাহ ও শেষ দিবসে বিশ্বাসের প্রকাশ্য আলামত।",
            searchKeywordsBn = listOf("মেহমান", "অতিথি", "মেহমানদারী", "আতিথেয়তা", "মেহমানের হক", "সম্মান"),
            searchKeywordsEn = listOf("hospitality", "guest", "generosity", "welcoming", "entertaining guest"),
            isFeatured = false,
            iconEmoji = "☕",
            relatedTopicIds = listOf("topic_neighbor_rights", "topic_brotherhood_society"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_6135",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 6135,
                    chapterTitleBn = "শিষ্টাচার অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنْ كَانَ يُؤْمِنُ بِاللَّهِ وَاليَوْمِ الآخِرِ فَلْيُكْرِمْ ضَيْفَهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি আল্লাহ ও শেষ দিবসে ঈমান রাখে, সে যেন তার মেহমানকে সম্মান করে ও সমাদর করে।' [সহীহ বুখারী: ৬১৩৫, সহীহ মুসলিম: ৪৮]",
                    englishText = "The Prophet (pbuh) said: 'Whoever believes in Allah and the Last Day should be hospitable and generous to his guest.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৬১৩৫, সহীহ মুসলিম ৪৮",
                    explanationBn = "মেহমান ঘরের জন্য বরকতস্বরূপ; তাকে আন্তরিকতার সাথে আপ্যায়ন করা ঈমানদারদের মহৎ ঐতিহ্য।"
                )
            )
        ),

        // 5. লজ্জাশীলতা ও সতীত্ব (হায়া)
        HadithTopic(
            id = "topic_modesty_haya",
            categoryId = "cat_akhlaq",
            nameBn = "লজ্জাশীলতা ও সতীত্ব (হায়া)",
            nameEn = "Modesty, Shame & Chastity (Haya)",
            nameAr = "الحياء شعبة من الإيمان وخير كله",
            descriptionBn = "লজ্জাশীলতা ঈমানের বিশেষ এক মহৎ অঙ্গ। লজ্জাশীলতা মানুষের জন্য কল্যাণ ছাড়া আর কোনো কিছুই বয়ে আনে না।",
            searchKeywordsBn = listOf("লজ্জা", "লজ্জাশীলতা", "হায়া", "সতীত্ব", "ঈমানের অঙ্গ", "চরিত্র"),
            searchKeywordsEn = listOf("haya", "modesty", "shame", "chastity", "branch of faith"),
            isFeatured = true,
            iconEmoji = "🌸",
            relatedTopicIds = listOf("topic_anger_control", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_9",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 9,
                    chapterTitleBn = "ঈমান অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "الإِيمَانُ بِضْعٌ وَسَبْعُونَ شُعْبَةً، وَالحَيَاءُ شُعْبَةٌ مِنَ الإِيمَانِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'ঈমানের সত্তরেরও অধিক শাখা রয়েছে; আর লজ্জাশীলতা হলো ঈমানের অন্যতম একটি বিশেষ শাখা।' [সহীহ বুখারী: ৯, সহীহ মুসলিম: ৩৫]",
                    englishText = "The Prophet (pbuh) said: 'Faith has over seventy branches, and modesty (Haya) is a branch of faith.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৯, সহীহ মুসলিম ৩৫",
                    explanationBn = "যার লজ্জা নেই তার ঈমানও দুর্বল হয়ে পড়ে। লজ্জা মানুষকে পাপ ও অসদাচরণ থেকে বিরত রাখে।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_6117",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 6117,
                    chapterTitleBn = "শিষ্টাচার অধ্যায়",
                    narratorBn = "হযরত ইমরান ইবনে হুসাইন (রা.)",
                    arabicText = "الحَيَاءُ لاَ يَأْتِي إِلَّا بِخَيْرٍ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'লজ্জাশীলতা কল্যাণ ব্যতীত আর কিছুই বয়ে আনে না।' [সহীহ বুখারী: ৬১১৭, সহীহ মুসলিম: ৩৭]",
                    englishText = "The Prophet (pbuh) said: 'Haya (modesty) does not bring anything except good.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৬১১৭, সহীহ মুসলিম ৩৭",
                    explanationBn = "লজ্জাশীল স্বভাব মানুষের মর্যাদা বৃদ্ধি করে এবং সকল প্রকার মন্দ থেকে তাকে রক্ষা করে।"
                )
            )
        )
    )
}
