package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart6 {

    val topics: List<HadithTopic> = listOf(
        // 1. মৃত্যুর স্মরণ
        HadithTopic(
            id = "topic_death_remembrance",
            categoryId = "cat_akhirah",
            nameBn = "মৃত্যুর স্মরণ ও স্বাদ বিনষ্টকারী মউত",
            nameEn = "Remembering Death, the Destroyer of Pleasures",
            nameAr = "ذكر الموت هاذم اللذات والاستعداد للرحيل",
            descriptionBn = "সমস্ত পার্থিব স্বাদ ও ভোগ-বিলাসকে ছিন্নকারী মৃত্যুকে প্রতিনিয়ত বেশি বেশি স্মরণ করার নববী তাগিদ, যা অন্তরকে নরম ও পরকালমুখী করে।",
            searchKeywordsBn = listOf("মৃত্যু", "মউত", "স্বাদ বিনষ্টকারী", "কবর", "জানাজা", "মৃত্যুর স্মরণ", "পরকাল প্রস্তুতি"),
            searchKeywordsEn = listOf("death", "destroyer of pleasures", "remembrance of death", "grave", "janazah"),
            isFeatured = true,
            iconEmoji = "⏳",
            relatedTopicIds = listOf("topic_grave_questioning", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_2307",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজি",
                    hadithNumber = 2307,
                    chapterTitleBn = "যুহদ অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "أَكْثِرُوا ذِكْرَ هَاذِمِ اللَّذَّاتِ: المَوْتِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমরা জীবনের সমস্ত স্বাদ-আহ্লাদকে বিনষ্টকারী মৃত্যুকে বেশি বেশি স্মরণ করো।' [জামে আত-তিরমিজি: ২৩০৭, সুনানে নাসাঈ: ১৮২৪]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Frequently remember the destroyer of pleasures: death.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে আত-তিরমিজি ২৩০৭, সুনানে নাসাঈ ১৮২৪, ইবনে মাজাহ ৪২৫৮",
                    explanationBn = "মৃত্যুর অনুভূতি মানুষের মন থেকে অহংকার, লোভ ও গফনশীলতা দূর করে তাকে বিনম্র ও পুণ্যবান করে তোলে।"
                )
            )
        ),

        // 2. কবরের সওয়াল-জওয়াব ও পরীক্ষা
        HadithTopic(
            id = "topic_grave_questioning",
            categoryId = "cat_akhirah",
            nameBn = "কবরের সওয়াল-জওয়াব ও পরীক্ষা",
            nameEn = "Questioning in the Grave (Munkar & Nakir)",
            nameAr = "فتنة القبر وسؤال الملكين منكر ونكير",
            descriptionBn = "কবরে প্রবেশের পর দুই ফেরেশতার আগমন এবং তিনটি মৌলিক প্রশ্ন: তোমার প্রতিপালক কে? তোমার দ্বীন কী? এবং তোমার নবী কে? মুমিনের সফলতার সাক্ষ্য।",
            searchKeywordsBn = listOf("কবর", "সওয়াল জওয়াব", "মুনকার নাকির", "কবরের আযাব", "রাব্বুকা", "দ্বীনুকা", "নাবিইয়ুকা"),
            searchKeywordsEn = listOf("grave questioning", "munkar nakir", "torment of grave", "three questions"),
            isFeatured = true,
            iconEmoji = "⚰️",
            relatedTopicIds = listOf("topic_death_remembrance", "topic_iman_tawheed"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_1071",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে আত-তিরমিজি",
                    hadithNumber = 1071,
                    chapterTitleBn = "জানাযা অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "إِذَا قُبِرَ المَيِّتُ أَتَاهُ مَلَكَانِ أَسْوَدَانِ أَزْرَقَانِ، يُقَالُ لِأَحَدِهِمَا المُنْكَرُ، وَلِلآخَرِ النَّكِيرُ، فَيَقُولَانِ: مَا كُنْتَ تَقُولُ فِي هَذَا الرَّجُلِ؟ فَيَقُولُ مَا كَانَ يَقُولُ: هُوَ عَبْدُ اللَّهِ وَرَسُولُهُ...",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'মৃত ব্যক্তিকে যখন কবরে দাফন করা হয়, তখন তার কাছে নীল চোখের কৃষ্ণবর্ণের দুজন ফেরেশতা আসেন। একজনের নাম মুনকার এবং অন্যজনের নাম নাকীর। তারা মৃত ব্যক্তিকে জিজ্ঞেস করেন: এই ব্যক্তি (মুহাম্মদ সা.) সম্পর্কে তুমি কী বলতে? মুমিন ব্যক্তি বলবে: তিনি আল্লাহর বান্দা ও তাঁর রাসুল...' [জামে আত-তিরমিজি: ১০৭১]",
                    englishText = "The Messenger of Allah (pbuh) said: 'When the deceased is buried, two black-and-blue angels come to him. One is called Al-Munkar and the other An-Nakeer. They ask him: What did you use to say about this man? And he will say what he used to say: He is the servant of Allah and His Messenger...'",
                    gradeBn = "হাসান সহীহ",
                    gradeColor = "HASAN",
                    sourceBn = "জামে আত-তিরমিজি ১০৭১, সহীহ ইবনে হিব্বান ৩১১৭",
                    explanationBn = "কবরের জীবনে অবিচল ঈমানই মানুষকে সঠিক জবাব দেওয়ার তাওফিক দান করে এবং কবরকে জান্নাতের বাগানে পরিণত করে।"
                )
            )
        ),

        // 3. কিয়ামতের আলামত ও শেষ যমানা
        HadithTopic(
            id = "topic_qiyamah_signs",
            categoryId = "cat_fitnah",
            nameBn = "কিয়ামতের আলামত ও শেষ যমানা",
            nameEn = "Signs of the Last Hour & End Times",
            nameAr = "أشراط الساعة الصغرى والكبرى وفتن آخر الزمان",
            descriptionBn = "কিয়ামতের পূর্বে সমাজে ইলম বা জ্ঞান উঠে যাওয়া, মূর্খতা ও অজ্ঞতা ছড়িয়ে পড়া, প্রকাশ্য ব্যভিচার ও পাপাচার বৃদ্ধি এবং রক্তপাত ও হত্যাকাণ্ডের প্রাবল্য।",
            searchKeywordsBn = listOf("কিয়ামত", "কিয়ামতের আলামত", "শেষ যমানা", "ফিতনা", "ইলম উঠে যাওয়া", "অজ্ঞতা", "রক্তপাত"),
            searchKeywordsEn = listOf("qiyamah signs", "last hour", "end times", "fitnah", "loss of knowledge", "bloodshed"),
            isFeatured = true,
            iconEmoji = "⚡",
            relatedTopicIds = listOf("topic_death_remembrance", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_80",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 80,
                    chapterTitleBn = "জ্ঞান অধ্যায়",
                    narratorBn = "হযরত আনাস ইবনে মালিক (রা.)",
                    arabicText = "مِنْ أَشْرَاطِ السَّاعَةِ: أَنْ يُرْفَعَ العِلْمُ، وَيَثْبُتَ الجَهْلُ، وَيُشْرَبَ الخَمْرُ، وَيَظْهَرَ الزِّنَا",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'কিয়ামতের অন্যতম প্রধান আলামত হলো—দ্বীনি ইলম উঠিয়ে নেওয়া হবে, অজ্ঞতা ও মূর্খতা শিকড় গেড়ে বসবে, অবাধে মদ পান করা হবে এবং ব্যভিচার প্রকাশ্যে ছড়িয়ে পড়বে।' [সহীহ বুখারী: ৮০, সহীহ মুসলিম: ২৬৭১]",
                    englishText = "The Prophet (pbuh) said: 'From among the portents of the Hour are (the following): Religious knowledge will be taken away; general ignorance will appear; the drinking of alcoholic drinks will be common, and illegal sexual intercourse will be committed openly.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৮০, সহীহ মুসলিম ২৬৭১",
                    explanationBn = "কিয়ামতের নিকটবর্তী সময়ে নৈতিক অবক্ষয় চরম আকার ধারণ করবে; এই কঠিন সময়ে দ্বীনের ওপর অবিচল থাকাই মুমিনের সবচেয়ে বড় পরীক্ষা।"
                )
            )
        ),

        // 4. জাহান্নামের ভয়াবহ আজাব ও তা থেকে মুক্তি
        HadithTopic(
            id = "topic_hell_torments",
            categoryId = "cat_akhirah",
            nameBn = "জাহান্নামের ভয়াবহ আজাব ও মুক্তি",
            nameEn = "Torments of Hellfire (Jahannam) & Seeking Refuge",
            nameAr = "شدة عذاب جهنم والنجاة من النار ولو بشق تمرة",
            descriptionBn = "দুনিয়ার আগুনের চেয়ে জাহান্নামের আগুন সত্তর গুণ বেশি উত্তপ্ত। খেজুরের সামান্য টুকরো দান করে হলেও জাহান্নামের আগুন থেকে বাঁচার আকুল নববী আহ্বান।",
            searchKeywordsBn = listOf("জাহান্নাম", "দোজখ", "আগুন", "আজাব", "সত্তর গুণ", "খেজুরের টুকরো", "মুক্তি"),
            searchKeywordsEn = listOf("jahannam", "hellfire", "torment", "punishment", "charity saves"),
            isFeatured = true,
            iconEmoji = "🔥",
            relatedTopicIds = listOf("topic_jannah_deeds", "topic_istighfar_tawbah"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_3260",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 3260,
                    chapterTitleBn = "সৃষ্টির সূচনা অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "نَارُكُمْ هَذِهِ الَّتِي يُوقِدُ ابْنُ آدَمَ جُزْءٌ مِنْ سَبْعِينَ جُزْءًا، مِنْ حَرِّ جَهَنَّمَ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমাদের এই আগুন, যা আদম সন্তানরা জ্বালিয়ে থাকে, তা জাহান্নামের আগুনের তীব্র উত্তাপের সত্তুর ভাগের এক ভাগ মাত্র।' [সহীহ বুখারী: ৩২৬০, সহীহ মুসলিম: ২৮৪৩]",
                    englishText = "Allah's Messenger (pbuh) said: 'Your (ordinary) fire is one of seventy parts of the (hot) fire of Hell.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৩২৬০, সহীহ মুসলিম ২৮৪৩",
                    explanationBn = "জাহান্নামের আগুনের তীব্রতা মানুষের কল্পনার অতীত; কোনো সুস্থ বুদ্ধিমান মানুষ জেনে-বুঝে পাপের পথে পা বাড়াতে পারে না।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_1417",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 1417,
                    chapterTitleBn = "যাকাত অধ্যায়",
                    narratorBn = "হযরত আদী ইবনে হাতিম (রা.)",
                    arabicText = "اتَّقُوا النَّارَ وَلَوْ بِشِقِّ تَمْرَةٍ، فَمَنْ لَمْ يَجِدْ فَبِكَلِمَةٍ طَيِّبَةٍ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমরা জাহান্নামের আগুন থেকে আত্মরক্ষা করো, এমনকি একটি শুকনো খেজুরের অর্ধেক অংশ দান করার মাধ্যমে হলেও! আর যে ব্যক্তি এতেও সক্ষম নয়, সে যেন অন্তত একটি মিষ্টি ও ভালো কথা বলে বাঁচে।' [সহীহ বুখারী: ১৪১৭, সহীহ মুসলিম: ১০১৬]",
                    englishText = "The Prophet (pbuh) said: 'Save yourself from Hellfire even by giving half a date-fruit in charity. And if you cannot find that, then with a good pleasant word.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ১৪১৭, সহীহ মুসলিম ১০১৬",
                    explanationBn = "কোনো নেক আমলকেই তুচ্ছ জ্ঞান করা উচিত নয়; আন্তরিকতার সাথে ক্ষুদ্রাতিক্ষুদ্র ভালো কাজও মানুষকে জাহান্নাম থেকে রক্ষা করতে পারে।"
                )
            )
        ),

        // 5. সাহাবায়ে কেরামের মর্যাদা
        HadithTopic(
            id = "topic_virtues_sahabah",
            categoryId = "cat_aqeedah",
            nameBn = "সাহাবায়ে কেরামের সুউচ্চ মর্যাদা",
            nameEn = "Virtues & Sanctity of the Sahabah",
            nameAr = "فضل الصحابة الكرام والنهي عن سبهم",
            descriptionBn = "রাসুলুল্লাহ (সা.)-এর পবিত্র সাহাবাগণ উম্মতের শ্রেষ্ঠ মানুষ। তাঁদের কাউকে গালি বা কটূক্তি করা কঠোরভাবে নিষিদ্ধ। তাঁদের ত্যাগ ও সততা চিরভাস্বর।",
            searchKeywordsBn = listOf("সাহাবী", "সাহাবায়ে কেরাম", "মর্যাদা", "গালি না দেওয়া", "ওহুদ পাহাড়", "আবু বকর", "উমর"),
            searchKeywordsEn = listOf("sahabah", "companions", "virtues of companions", "respect for sahabah"),
            isFeatured = false,
            iconEmoji = "⭐",
            relatedTopicIds = listOf("topic_iman_tawheed", "topic_ilm_knowledge"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_3673",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 3673,
                    chapterTitleBn = "সাহাবাদের মর্যাদা অধ্যায়",
                    narratorBn = "হযরত আবু সাঈদ আল-খুদরী (রা.)",
                    arabicText = "لاَ تَسُبُّوا أَصْحَابِي، فَلَوْ أَنَّ أَحَدَكُمْ أَنْفَقَ مِثْلَ أُحُدٍ ذَهَبًا مَا بَلَغَ مُدَّ أَحَدِهِمْ، وَلاَ نَصِيفَهُ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমরা আমার সাহাবীদের কাউকে গালি বা কটূক্তি করো না। কারণ তোমাদের কেউ যদি ওহুদ পাহাড় পরিমাণ সোনাও আল্লাহর পথে ব্যয় করে, তবুও তা তাঁদের একজনের এক অঞ্জলি (এক মুদ) বা তার অর্ধ অর্ধেকেরও সমকক্ষ হতে পারবে না।' [সহীহ বুখারী: ৩৬৭৩, সহীহ মুসলিম: ২৫৪০]",
                    englishText = "The Prophet (pbuh) said: 'Do not abuse my companions for if any one of you spent gold equal to Uhud in Allah's Cause, it would not be equal to a Mud or even a half a Mud spent by one of them.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৩৬৭৩, সহীহ মুসলিম ২৫৪০",
                    explanationBn = "সাহাবায়ে কেরামের আত্মত্যাগ ও আন্তরিকতার মর্যাদা অতুলনীয়; তাঁদের প্রতি ভালোবাসা পোষণ করা সুন্নাতের অপরিহার্য অংশ।"
                )
            )
        ),

        // 6. সালামের প্রসার ও মুসাফাহার সওয়াব
        HadithTopic(
            id = "topic_salam_handshake",
            categoryId = "cat_society",
            nameBn = "সালামের প্রসার ও মুসাফাহার সওয়াব",
            nameEn = "Spreading Salam & Handshake (Musafahah)",
            nameAr = "إفشاء السلام والمصافحة ومغفرة الذنوب",
            descriptionBn = "পরস্পরে সালামের প্রসার ঈমান ও পারস্পরিক ভালোবাসার চাবিকাঠি। দুই মুসলিমের মুসাফাহায় বৃক্ষের পাতার মতো তাদের গুনাহ ঝরে পড়ে।",
            searchKeywordsBn = listOf("সালাম", "সালামের প্রসার", "মুসাফাহা", "শান্তি", "ভালোবাসা", "গুনাহ ঝরা"),
            searchKeywordsEn = listOf("salam", "spreading peace", "handshake", "musafahah", "brotherly love"),
            isFeatured = true,
            iconEmoji = "🤝",
            relatedTopicIds = listOf("topic_brotherhood_society", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_54",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 54,
                    chapterTitleBn = "ঈমান অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "لاَ تَدْخُلُونَ الجَنَّةَ حَتَّى تُؤْمِنُوا، وَلاَ تُؤْمِنُوا حَتَّى تَحَابُّوا، أَوَلاَ أَدُلُّكُمْ عَلَى شَيْءٍ إِذَا فَعَلْتُمُوهُ تَحَابَبْتُمْ؟ أَفْشُوا السَّلاَمَ بَيْنَكُمْ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমরা জান্নাতে প্রবেশ করতে পারবে না যতক্ষণ না পূর্ণ ঈমান আনবে, আর ঈমানদার হতে পারবে না যতক্ষণ না পরস্পরকে ভালোবাসবে। আমি কি তোমাদের এমন একটি কাজের কথা বলে দেব না, যা করলে তোমাদের মধ্যে পারস্পরিক ভালোবাসা সৃষ্টি হবে? তোমরা পরস্পরের মাঝে ব্যাপকভাবে সালামের প্রসার ঘটাও।' [সহীহ মুসলিম: ৫৪]",
                    englishText = "The Messenger of Allah (pbuh) said: 'You will not enter Paradise until you believe, and you will not believe until you love one another. Shall I not guide you to something which, if you do, will make you love one another? Spread peace (Salam) among yourselves.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৫৪, তিরমিজি ২৬৮৮, আবু দাউদ ৫১৯৩",
                    explanationBn = "সালাম শুধু সম্ভাষণ নয়, এটি পরস্পরের জন্য নিরাপত্তা ও রহমতের দো'আ; সমাজে সম্প্রীতি স্থাপনের শ্রেষ্ঠ হাতিয়ার।"
                )
            )
        )
    )
}
