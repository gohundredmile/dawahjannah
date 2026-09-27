package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart1 {

    val topics: List<HadithTopic> = listOf(
        // 1. ঈমান ও তাওহীদের মূল ভিত্তি
        HadithTopic(
            id = "topic_iman_tawheed",
            categoryId = "cat_aqeedah",
            nameBn = "ঈমান ও তাওহীদের মূল ভিত্তি",
            nameEn = "Pillars of Iman, Islam & Tawheed",
            nameAr = "أركان الإيمان والإسلام والتوحيد",
            descriptionBn = "ইসলাম ও ঈমানের মৌলিক স্তম্ভসমূহ, জিবরীল (আ.)-এর ঐতিহাসিক হাদিস এবং আল্লাহর একত্ববাদের ঘোষণা।",
            searchKeywordsBn = listOf("ঈমান", "ইসলাম", "তাওহীদ", "জিবরীল", "আকীদাহ", "আল্লাহর ওপর বিশ্বাস", "কালেমা"),
            searchKeywordsEn = listOf("iman", "islam", "tawheed", "jibreel", "pillars", "creed", "faith"),
            isFeatured = true,
            iconEmoji = "☝️",
            relatedTopicIds = listOf("topic_salah_importance", "topic_istighfar_tawbah"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "forty-nawawi_2",
                    bookSlug = "forty-nawawi",
                    bookNameBn = "ইমাম নববীর ৪০ হাদীস",
                    hadithNumber = 2,
                    chapterTitleBn = "মৌলিক ৪০ হাদিস",
                    narratorBn = "হযরত উমর ইবনুল খাত্তাব (রা.)",
                    arabicText = "قَالَ: فَأَخْبِرْنِي عَنْ الْإِيمَانِ. قَالَ: أَنْ تُؤْمِنَ بِاَللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ وَالْيَوْمِ الْآخِرِ، وَتُؤْمِنَ بِالْقَدَرِ خَيْرِهِ وَشَرِّهِ. قَالَ: صَدَقْت.",
                    banglaText = "জিবরীল (আ.) বললেন: 'আমাকে ঈমান সম্পর্কে বলুন।' রাসুলুল্লাহ (সা.) বললেন: 'তা হচ্ছে এই—তুমি বিশ্বাস স্থাপন করবে আল্লাহর প্রতি, তাঁর ফেরেশতাগণের প্রতি, তাঁর কিতাবসমূহের প্রতি, তাঁর রাসুলগণের প্রতি ও পরকালের প্রতি এবং তাকদীরের ভালো-মন্দের প্রতি।' তিনি বললেন: 'আপনি সত্য বলেছেন।' [সহীহ মুসলিম: ৮, সহীহ বুখারী: ৫০]",
                    englishText = "He said: 'Tell me about Iman.' He said: 'It is to believe in Allah, His angels, His books, His messengers, the Last Day, and to believe in divine destiny, both the good and the evil thereof.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৮, সহীহ বুখারী ৫০, ইমাম নববীর ৪০ হাদীস ২",
                    explanationBn = "হাদিসে জিবরীল ইসলামের ধর্মতত্ত্ব ও ঈমানের বুনিয়াদ হিসেবে সুপরিচিত। এতে ইসলামের পঞ্চস্তম্ভ, ঈমানের ছয়টি রুকন এবং ইহসানের অনুপম সংজ্ঞা তুলে ধরা হয়েছে।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 285,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:২৮৫)",
                    relatedDuaTitleBn = "ঈমান নবায়নের দো'আ"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_8",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহুল বুখারী",
                    hadithNumber = 8,
                    chapterTitleBn = "ঈমান পর্ব",
                    narratorBn = "হযরত আব্দুল্লাহ ইবনু উমর (রা.)",
                    arabicText = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَإِقَامِ الصَّلاَةِ، وَإِيتَاءِ الزَّكَاةِ، وَالْحَجِّ، وَصَوْمِ رَمَضَانَ.",
                    banglaText = "ইসলামের ভিত্তি পাঁচটি বিষয়ের ওপর প্রতিষ্ঠিত: এই সাক্ষ্য দেওয়া যে, আল্লাহ ছাড়া কোনো সত্য ইলাহ নেই এবং নিশ্চয় মুহাম্মদ (সা.) আল্লাহর রাসুল; সালাত কায়েম করা; যাকাত প্রদান করা; হজ্জ আদায় করা এবং রমাদানের সিয়াম পালন করা।",
                    englishText = "Islam is based on five principles: To testify that none has the right to be worshipped but Allah and Muhammad is Allah's Messenger, to establish Salah, to pay Zakat, to perform Hajj, and to observe Saum of Ramadan.",
                    gradeBn = "সহীহ বুখারী: ৮",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৮, সহীহ মুসলিম ১৬",
                    explanationBn = "ইসলামের বাহ্যিক পাঁচটি মৌলিক স্তম্ভের অকাট্য দলীল যা প্রতিটি মুমিনের জীবনে অবিচ্ছেদ্য।",
                    relatedQuranSurahNumber = 3,
                    relatedQuranAyahNumber = 19,
                    relatedQuranAyahRef = "সূরা আলে-ইমরান (৩:১৯)"
                )
            )
        ),

        // 2. নিয়তের ইখলাস ও বিশুদ্ধতা
        HadithTopic(
            id = "topic_intention_iklas",
            categoryId = "cat_akhlaq",
            nameBn = "নিয়তের ইখলাস ও বিশুদ্ধতা",
            nameEn = "Sincerity of Intention (Ikhlas)",
            nameAr = "إخلاص النية لله تعالى",
            descriptionBn = "যাবতীয় আমল ও সৎকাজের মূল্যায়ন কেবল অন্তরের খাঁটি নিয়তের ওপর নির্ভরশীল হওয়ার শাশ্বত ঘোষণা।",
            searchKeywordsBn = listOf("নিয়ত", "ইখলাস", "আমল", "হিজরত", "লোকদেখানো", "খাঁটি নিয়ত", "ইন্না মাল আমালু"),
            searchKeywordsEn = listOf("intention", "niyyah", "ikhlas", "sincerity", "actions", "reward"),
            isFeatured = true,
            iconEmoji = "💎",
            relatedTopicIds = listOf("topic_iman_tawheed", "topic_salah_importance"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_1",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহুল বুখারী",
                    hadithNumber = 1,
                    chapterTitleBn = "ওহীর সূচনা",
                    narratorBn = "আমীরুল মুমিনীন হযরত উমর ইবনুল খাত্তাব (রা.)",
                    arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا، فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ.",
                    banglaText = "সকল কাজের ফলাফল নিয়তের ওপর নির্ভরশীল। প্রত্যেক মানুষ তাই পায়, যা সে নিয়ত করে। অতএব যে ব্যক্তির হিজরত দুনিয়া পাওয়ার উদ্দেশে কিংবা কোনো নারীকে বিবাহ করার নিয়তে হবে, তার হিজরত সেই উদ্দেশ্যেই গণ্য হবে যে উদ্দেশ্যে সে হিজরত করেছে।",
                    englishText = "Actions are according to intentions, and every person will get the reward according to what he has intended.",
                    gradeBn = "সহীহ বুখারী: ১",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ১, সহীহ মুসলিম ১৯০৭",
                    explanationBn = "ইমাম বুখারী ও ইমাম নববী এই হাদিস দিয়ে তাঁদের গ্রন্থ শুরু করেছেন। ইমাম শাফেয়ী বলেন: এই একটি হাদিসে দ্বীনের এক-তৃতীয়াংশ ইলম নিহিত। নিয়তের বিশুদ্ধতা ছাড়া কোনো ইবাদত কবুল হয় না।",
                    relatedQuranSurahNumber = 98,
                    relatedQuranAyahNumber = 5,
                    relatedQuranAyahRef = "সূরা আল-বাইয়্যিনাহ (৯৮:৫)"
                )
            )
        ),

        // 3. সালাতের গুরুত্ব ও ফজিলত
        HadithTopic(
            id = "topic_salah_importance",
            categoryId = "cat_ibadah",
            nameBn = "সালাতের গুরুত্ব ও ফজিলত",
            nameEn = "Importance & Virtues of Salah",
            nameAr = "فضل الصلاة والمحافظة عليها",
            descriptionBn = "দ্বীনের খুঁটি সালাত, পাঁচ ওয়াক্ত নামাজের মাধ্যমে গুনাহ মোচন এবং জান্নাতে প্রবেশের পথ সুগম করার হাদিসসমূহ।",
            searchKeywordsBn = listOf("সালাত", "নামাজ", "নামায", "পাঁচ ওয়াক্ত", "রুকু", "সিজদা", "দ্বীনের খুঁটি"),
            searchKeywordsEn = listOf("salah", "prayer", "namaz", "prostration", "five prayers", "sujood"),
            isFeatured = true,
            iconEmoji = "🕌",
            relatedTopicIds = listOf("topic_taharah_wudu", "topic_iman_tawheed"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "forty-nawawi_3",
                    bookSlug = "forty-nawawi",
                    bookNameBn = "ইমাম নববীর ৪০ হাদীস",
                    hadithNumber = 3,
                    chapterTitleBn = "মৌলিক ৪০ হাদিস",
                    narratorBn = "হযরত আবু আব্দুর রহমান আব্দুল্লাহ ইবনু উমর (রা.)",
                    arabicText = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا عَبْدُهُ وَرَسُولُهُ، وَإِقَامِ الصَّلاَةِ...",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেন: ইসলামের বুনিয়াদ পাঁচটি বিষয়ের ওপর স্থাপিত... তন্মধ্যে অন্যতম হলো সালাত কায়েম করা।",
                    englishText = "Islam is built upon five pillars... and establishing prayer.",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৮, সহীহ মুসলিম ১৬",
                    explanationBn = "সালাত হলো মুমিনের মেরুদণ্ড। কিয়ামতের দিন সর্বপ্রথম সালাতেরই হিসাব গ্রহণ করা হবে।",
                    relatedQuranSurahNumber = 29,
                    relatedQuranAyahNumber = 45,
                    relatedQuranAyahRef = "সূরা আল-আনকাবূত (২৯:৪৫)"
                )
            )
        ),

        // 4. পবিত্রতা ও ওযুর ফযীলত
        HadithTopic(
            id = "topic_taharah_wudu",
            categoryId = "cat_taharah",
            nameBn = "পবিত্রতা ঈমানের অঙ্গ ও ওযুর ফযীলত",
            nameEn = "Purity is Half of Faith & Virtues of Wudu",
            nameAr = "الطهور شطر الإيمان وفضل الوضوء",
            descriptionBn = "পবিত্রতা অর্জন, সুন্দরভাবে ওযু সম্পন্ন করা এবং ওযুর পানির ফোঁটার সাথে পাপ ঝরে যাওয়ার মহা পুরস্কার।",
            searchKeywordsBn = listOf("পবিত্রতা", "ওযু", "ওজু", "তাহারা", "পরিচ্ছন্নতা", "ঈমানের অঙ্গ", "মিসওয়াক"),
            searchKeywordsEn = listOf("taharah", "purity", "wudu", "ablution", "cleanliness", "half of faith"),
            isFeatured = true,
            iconEmoji = "💧",
            relatedTopicIds = listOf("topic_salah_importance", "topic_intention_iklas"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_223",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 223,
                    chapterTitleBn = "পবিত্রতা পর্ব",
                    narratorBn = "হযরত আবু মালিক আল-আশ'আরী (রা.)",
                    arabicText = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ، وَسُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ - أَوْ تَمْلأُ - مَا بَيْنَ السَّمَاوَاتِ وَالأَرْضِ، وَالصَّلاَةُ نُورٌ، وَالصَّدَقَةُ بُرْهَانٌ، وَالصَّبْرُ ضِيَاءٌ، وَالْقُرْآنُ حُجَّةٌ لَكَ أَوْ عَلَيْكَ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: পবিত্রতা হলো ঈমানের অর্ধেক। আর 'আলহামদুলিল্লাহ' পাঠ মিযানের পাল্লাকে পূর্ণ করে দেয়। 'সুবহানাল্লাহ' ও 'আলহামদুলিল্লাহ' আকাশ ও পৃথিবীর মধ্যবর্তী স্থানকে পূর্ণ করে দেয়। সালাত হলো নূর (জ্যোতি), সদকা হলো প্রমাণ, ধৈর্য হলো আলোকবর্তিকা এবং আল-কুরআন তোমার পক্ষে অথবা বিপক্ষে দলীল হবে।",
                    englishText = "Cleanliness is half of faith and Alhamdulillah fills the scale, and SubhanAllah and Alhamdulillah fill up what is between the heavens and the earth, and prayer is a light, and charity is proof, and patience is illumination, and the Quran is proof on your behalf or against you.",
                    gradeBn = "সহীহ মুসলিম: ২২৩",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ২২৩",
                    explanationBn = "ইসলামে অন্তরের এবং শরীরের পবিত্রতা অর্জনকে ঈমানের অর্ধেক বলে আখ্যা দেওয়া হয়েছে। সালাতের পূর্বে ওযু অপরিহার্য শর্ত।",
                    relatedQuranSurahNumber = 5,
                    relatedQuranAyahNumber = 6,
                    relatedQuranAyahRef = "সূরা আল-মায়িদাহ (৫:৬)",
                    relatedDuaTitleBn = "ওযুর পূর্বের ও পরের দো'আ"
                )
            )
        ),

        // 5. কুরআন শিক্ষা ও প্রচারের মর্যাদা
        HadithTopic(
            id = "topic_quran_virtue",
            categoryId = "cat_ibadah",
            nameBn = "কুরআন শিক্ষা ও তিলাওয়াতের মর্যাদা",
            nameEn = "Virtues of Learning & Teaching the Quran",
            nameAr = "فضل تعلم القرآن وتعليمه",
            descriptionBn = "উম্মতের মধ্যে সর্বোত্তম ব্যক্তি যিনি নিজে কুরআন শেখেন ও অন্যকে শিক্ষা দেন, এবং তিলাওয়াতের অপরিমেয় সওয়াব।",
            searchKeywordsBn = listOf("কুরআন", "কুরআন শিক্ষা", "তিলাওয়াত", "উত্তম ব্যক্তি", "কুরআনের ফযীলত"),
            searchKeywordsEn = listOf("quran", "learning quran", "teaching quran", "recitation", "best person"),
            isFeatured = true,
            iconEmoji = "📖",
            relatedTopicIds = listOf("topic_ilm_virtue", "topic_iman_tawheed"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_5027",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহুল বুখারী",
                    hadithNumber = 5027,
                    chapterTitleBn = "কুরআনের ফযীলত",
                    narratorBn = "আমীরুল মুমিনীন হযরত উসমান ইবনু আফফান (রা.)",
                    arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সেই, যে নিজে কুরআন শিখে এবং অন্যকে তা শিক্ষা দেয়।",
                    englishText = "The best among you (Muslims) are those who learn the Qur'an and teach it.",
                    gradeBn = "সহীহ বুখারী: ৫০২৭",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ বুখারী ৫০২৭, আবু দাউদ ১৪৫২, তিরমিজী ২৯০৭",
                    explanationBn = "পবিত্র কুরআন মহান আল্লাহর কালাম। এর শিক্ষার্থী ও শিক্ষক উভয়ই আল্লাহর দরবারে পরম সম্মানিত ও সর্বোত্তম মর্যাদাপ্রাপ্ত।",
                    relatedQuranSurahNumber = 35,
                    relatedQuranAyahNumber = 29,
                    relatedQuranAyahRef = "সূরা ফাতির (৩৫:২৯-৩০)"
                )
            )
        ),

        // 6. ধৈর্য, অবিচলতা ও বিপদমুক্তি
        HadithTopic(
            id = "topic_sabr_hardship",
            categoryId = "cat_akhlaq",
            nameBn = "ধৈর্য, অবিচলতা ও বিপদমুক্তি",
            nameEn = "Patience in Hardship & Steadfastness",
            nameAr = "الصبر عند الشدائد والاحتساب",
            descriptionBn = "বিপদ-আপদ ও দুঃখ-কষ্টে ধৈর্য ধারণ, মুমিনের প্রতিটি কাঁটা ফোটার বিনিময়েও গুনাহ মাফ হওয়ার সুসংবাদ।",
            searchKeywordsBn = listOf("ধৈর্য", "সবর", "বিপদ", "কষ্ট", "অবিচলতা", "বিপদে সান্ত্বনা", "পরীক্ষা"),
            searchKeywordsEn = listOf("patience", "sabr", "hardship", "trials", "steadfastness", "adversity"),
            isFeatured = true,
            iconEmoji = "🛡️",
            relatedTopicIds = listOf("topic_dua_acceptance", "topic_taharah_wudu"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "tirmidhi_2516",
                    bookSlug = "tirmidhi",
                    bookNameBn = "জামে' আত-তিরমিজী",
                    hadithNumber = 2516,
                    chapterTitleBn = "কিয়ামতের বর্ণনা",
                    narratorBn = "হযরত আব্দুল্লাহ ইবনু আব্বাস (রা.)",
                    arabicText = "احْفَظِ اللَّهَ يَحْفَظْكَ، احْفَظِ اللَّهَ تَجِدْهُ تُجَاهَكَ، إِذَا سَأَلْتَ فَاسْأَلِ اللَّهَ، وَإِذَا اسْتَعَنْتَ فَاسْتَعِنْ بِاللَّهِ... وَاعْلَمْ أَنَّ النَّصْرَ مَعَ الصَّبْرِ، وَأَنَّ الْفَرَجَ مَعَ الْكَرْبِ، وَأَنَّ مَعَ الْعُسْرِ يُسْرًا.",
                    banglaText = "রাসুলুল্লাহ (সা.) আমাকে বললেন: 'হে বৎস! তুমি আল্লাহর হুকুমসমূহের হেফাজত করো, আল্লাহ তোমাকে রক্ষা করবেন। তুমি আল্লাহর সন্তুষ্টির দিকে খেয়াল রাখো, তুমি তাঁকে তোমার সামনেই পাবে। যখন কোনো কিছু চাইবে কেবল আল্লাহর কাছেই চাইবে; আর যখন সাহায্য প্রার্থনা করবে কেবল আল্লাহর কাছেই সাহায্য চাইবে... আর জেনে রেখো! ধৈর্যের সাথেই রয়েছে চূড়ান্ত বিজয়, দুঃখ-কষ্টের পরই রয়েছে মুক্তি, আর নিশ্চয় কষ্টের সাথেই রয়েছে স্বস্তি।' [তিরমিজী: ২৫১৬, সহীহ]",
                    englishText = "Be mindful of Allah, and Allah will protect you. If you ask, then ask Allah [alone]; and if you seek help, then seek help from Allah [alone]... and know that victory comes with patience, relief with affliction, and ease with hardship.",
                    gradeBn = "সহীহ তিরমিজী: ২৫১৬",
                    gradeColor = "SAHIH",
                    sourceBn = "জামে' আত-তিরমিজী ২৫১৬, মুসনাদে আহমাদ ২৬৬৯",
                    explanationBn = "এই ঐতিহাসিক হাদিসটি মুমিনের জীবনের পূর্ণাঙ্গ তাওয়াক্কুল ও অবিচলতার পথপ্রদর্শক। বিপদের সময় ধৈর্য ধারণকারীকেই আল্লাহ সাহায্য ও বিজয় দান করেন।",
                    relatedQuranSurahNumber = 2,
                    relatedQuranAyahNumber = 153,
                    relatedQuranAyahRef = "সূরা আল-বাক্বারাহ (২:১৫৩)",
                    relatedDuaTitleBn = "বিপদ ও কঠিন সময়ের দো'আ"
                )
            )
        ),

        // 7. রাগ নিয়ন্ত্রণ ও কোমল ব্যবহার
        HadithTopic(
            id = "topic_anger_control",
            categoryId = "cat_akhlaq",
            nameBn = "রাগ নিয়ন্ত্রণ ও কোমল ব্যবহার",
            nameEn = "Controlling Anger & Gentleness",
            nameAr = "كظم الغيظ والرفق في المعاملة",
            descriptionBn = "প্রকৃত বীর সেই ব্যক্তি যে ক্রোধের মুহূর্তে নিজেকে সংযত রাখতে পারে, এবং কোমলতার মাধ্যমে সকল সৌন্দর্য অর্জিত হওয়ার শিক্ষা।",
            searchKeywordsBn = listOf("রাগ", "ক্ষোভ", "রাগ নিয়ন্ত্রণ", "ধৈর্য", "কোমলতা", "প্রকৃত বীর", "মেজাজ"),
            searchKeywordsEn = listOf("anger", "controlling anger", "gentleness", "strength", "patience"),
            isFeatured = true,
            iconEmoji = "🧘",
            relatedTopicIds = listOf("topic_sabr_hardship", "topic_brotherhood_society"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "abu-dawud_4941",
                    bookSlug = "abu-dawud",
                    bookNameBn = "সুনানে আবু দাউদ",
                    hadithNumber = 4941,
                    chapterTitleBn = "আদব ও শিষ্টাচার",
                    narratorBn = "হযরত আবু দারদা (রা.) ও আয়েশা (রা.)",
                    arabicText = "إِنَّ الرِّفْقَ لاَ يَكُونُ فِي شَىْءٍ إِلاَّ زَانَهُ وَلاَ يُنْزَعُ مِنْ شَىْءٍ إِلاَّ شَانَهُ.",
                    banglaText = "রাসুলুল্লাহ (সা.) ইরশাদ করেছেন: নিশ্চয় নম্রতা ও কোমলতা যে বিষয়েই থাকে, তা তাকে সৌন্দর্যমণ্ডিত করে তোলে; আর যে বিষয় থেকেই কোমলতা কেড়ে নেওয়া হয়, তা তাকে ত্রুটিযুক্ত ও কলুষিত করে দেয়।",
                    englishText = "Verily, gentleness is not in anything except that it beautifies it, and it is not removed from anything except that it disgraces it.",
                    gradeBn = "সহীহ মুসলিম ২৫৯৪, সুনানে আবু দাউদ ৪৯৪১",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ২৫৯৪, সুনানে আবু দাউদ ৪৯৪১",
                    explanationBn = "পারিবারিক ও সামাজিক জীবনে মেজাজ সংযত রাখা এবং কথাবার্তায় নম্রতা অবলম্বন করা রাসুলুল্লাহ (সা.)-এর সুন্নাহর অন্যতম ভূষণ।",
                    relatedQuranSurahNumber = 3,
                    relatedQuranAyahNumber = 134,
                    relatedQuranAyahRef = "সূরা আলে-ইমরান (৩:১৩৪)"
                )
            )
        )
    )
}
