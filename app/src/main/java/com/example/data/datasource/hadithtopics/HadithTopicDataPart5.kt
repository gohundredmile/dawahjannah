package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.TopicHadithRef

object HadithTopicDataPart5 {

    val topics: List<HadithTopic> = listOf(
        // 1. কবিরা গুনাহ ও ধ্বংসাত্মক মহাপাপ
        HadithTopic(
            id = "topic_major_sins_kabair",
            categoryId = "cat_aqeedah",
            nameBn = "কবিরা গুনাহ ও ধ্বংসাত্মক মহাপাপ",
            nameEn = "Major Destructive Sins (Al-Kaba'ir)",
            nameAr = "الكبائر السبع الموبقات المهلكة",
            descriptionBn = "সাতটি ধ্বংসাত্মক মহাপাপ যার মধ্যে রয়েছে শিরক, জাদু, অন্যায়ভাবে হত্যা, সুদ খাওয়া, এতিমের মাল আত্মসাৎ, যুদ্ধের ময়দান থেকে পলায়ন এবং সতীসাধ্বী নারীর ওপর অপবাদ।",
            searchKeywordsBn = listOf("কবিরা গুনাহ", "মহাপাপ", "সাতটি ধ্বংসাত্মক", "শিরক", "সুদ", "হত্যা", "জাদু", "অপবাদ"),
            searchKeywordsEn = listOf("major sins", "kabair", "destructive sins", "murder", "magic", "usury", "false accusation"),
            isFeatured = true,
            iconEmoji = "⚠️",
            relatedTopicIds = listOf("topic_iman_tawheed", "topic_istighfar_tawbah"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_2766",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 2766,
                    chapterTitleBn = "ওসিয়ত অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "اجْتَنِبُوا السَّبْعَ المُوبِقَاتِ: الشِّرْكُ بِاللَّهِ، وَالسِّحْرُ، وَقَتْلُ النَّفْسِ الَّتِي حَرَّمَ اللَّهُ إِلَّا بِالحَقِّ، وَأَكْلُ الرِّبَا، وَأَكْلُ مَالِ اليَتِيمِ، وَالتَّوَلِّي يَوْمَ الزَّحْفِ، وَقَذْفُ المُحْصَنَاتِ المُؤْمِنَاتِ الغَافِلاَتِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমরা সাতটি ধ্বংসকারী মহাপাপ থেকে বেঁচে থাক।' সাহাবীগণ জিজ্ঞেস করলেন: 'হে আল্লাহর রাসুল! সেগুলো কী?' তিনি বললেন: '১. আল্লাহর সাথে কাউকে শরিক করা, ২. জাদু করা, ৩. অন্যায়ভাবে কোনো প্রাণ সংহার করা যা আল্লাহ হারাম করেছেন, ৪. সুদ খাওয়া, ৫. এতিমের সম্পদ গ্রাস করা, ৬. জিহাদের ময়দান থেকে রণভঙ্গ দিয়ে পলায়ন করা এবং ৭. সরলমনা সতীসাধ্বী মুমিন নারীর বিরুদ্ধে ব্যভিচারের মিথ্যা অপবাদ রটানো।' [সহীহ বুখারী: ২৭৬৬, সহীহ মুসলিম: ৮৯]",
                    englishText = "The Prophet (pbuh) said: 'Avoid the seven great destructive sins.' The people inquired, 'O Allah's Messenger! What are they?' He said, 'To join others in worship along with Allah, to practice sorcery, to kill the life which Allah has forbidden except for a just cause, to eat up Riba (usury), to eat up an orphan's wealth, to give back to the enemy and fleeing from the battlefield at the time of fighting, and to accuse chaste women, who never even think of anything touching their chastity and are good believers.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ২৭৬৬, সহীহ মুসলিম ৮৯",
                    explanationBn = "এই সাতটি অপরাধ দুনিয়া ও আখিরাতে মানুষের ঈমান ও জীবনকে সম্পূর্ণরূপে ধ্বংস করে দেয়; খাঁটি তওবা ছাড়া এগুলোর ক্ষমা হয় না।"
                )
            )
        ),

        // 2. ধোঁকাবাজি ও প্রতারণা বর্জন
        HadithTopic(
            id = "topic_cheating_deceit",
            categoryId = "cat_business",
            nameBn = "ধোঁকাবাজি ও প্রতারণা বর্জন",
            nameEn = "Prohibition of Cheating & Deceit",
            nameAr = "تحريم الغش والخديعة في المعاملات",
            descriptionBn = "ব্যবসা-বাণিজ্য বা যেকোনো লেনদেনে কাউকে ধোঁকা বা ফাঁকি দেওয়া মারাত্মক অপরাধ। ধোঁকাবাজ কখনো রাসুলুল্লাহ (সা.)-এর উম্মতের আদর্শের অংশ হতে পারে না।",
            searchKeywordsBn = listOf("ধোঁকা", "প্রতারণা", "ভেজাল", "ফাঁকি", "প্রবঞ্চনা", "আমাদের দলভুক্ত নয়"),
            searchKeywordsEn = listOf("cheating", "deceit", "dishonesty", "not of us", "fraud"),
            isFeatured = true,
            iconEmoji = "🚫",
            relatedTopicIds = listOf("topic_halal_earning", "topic_brotherhood_society"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_101",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 101,
                    chapterTitleBn = "ঈমান অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "مَنْ غَشَّنَا فَلَيْسَ مِنَّا",
                    banglaText = "রাসুলুল্লাহ (সা.) একদা বাজারে এক শস্যের স্তূপের ভেতর হাত প্রবেশ করালেন। তাঁর আঙ্গুলে ভেজা ভাব অনুভূত হলো। তিনি জিজ্ঞেস করলেন: 'হে শস্যের মালিক! একি?' সে বলল: 'হে আল্লাহর রাসুল! এতে বৃষ্টির পানি লেগেছিল।' তিনি বললেন: 'তাহলে তুমি এটাকে শস্যের ওপর রাখলে না কেন যাতে মানুষ দেখতে পেত? জেনে রেখো, যে ব্যক্তি আমাদের ধোঁকা দেয়, সে আমার দলভুক্ত নয়।' [সহীহ মুসলিম: ১০১, ১০২]",
                    englishText = "The Messenger of Allah (pbuh) passed by a pile of food and put his hand into it, and his fingers felt dampness. He said: 'What is this, O owner of the food?' He said: 'It was hit by rain, O Messenger of Allah.' He said: 'Why did you not put it on top of the food so that people could see it? Whoever deceives us is not of us.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ১০১, ১০২, তিরমিজি ১৩১৫",
                    explanationBn = "পণ্যদ্রব্যের দোষত্রুটি লুকিয়ে বিক্রি করা বা যে কোনো লেনদেনে মিথ্যাচার ইসলামে সম্পূর্ণ হারাম ও কবিরা গুনাহ।"
                )
            )
        ),

        // 3. অসুস্থ ব্যক্তির সেবা ও সুন্নাহ পথ্য
        HadithTopic(
            id = "topic_visiting_sick_sunnah_cure",
            categoryId = "cat_health",
            nameBn = "অসুস্থ ব্যক্তির সেবা ও সুন্নাহ পথ্য",
            nameEn = "Visiting the Sick & Sunnah Cure (Black Seed)",
            nameAr = "عیادة المريض وفضل الحبة السوداء والعسل",
            descriptionBn = "রোগীর সেবা করা এবং তাকে দেখতে যাওয়া জান্নাতের ফল বাগানে বিচরণের সমতুল্য। কালোজিরা ও মধুতে আল্লাহ তা'আলা রোগের নিরাময় রেখেছেন।",
            searchKeywordsBn = listOf("অসুস্থ", "রোগী দেখা", "কালোজিরা", "মধু", "শিফা", "সুস্থতা", "সুন্নাহ চিকিৎসা"),
            searchKeywordsEn = listOf("visiting sick", "black seed", "honey", "cure", "health", "shifa"),
            isFeatured = true,
            iconEmoji = "🌿",
            relatedTopicIds = listOf("topic_sabr_hardship", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_2568",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 2568,
                    chapterTitleBn = "সদাচরণ ও সৌজন্য",
                    narratorBn = "হযরত সাওবান (রা.)",
                    arabicText = "إِنَّ المُسْلِمَ إِذَا عَادَ أَخَاهُ المُسْلِمَ لَمْ يَزَلْ فِي خُرْفَةِ الجَنَّةِ حَتَّى يَرْجِعَ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'কোনো মুসলিম যখন তার অসুস্থ মুসলিম ভাইকে দেখতে যায়, সে ফিরে আসা পর্যন্ত অবিরাম জান্নাতের ফল আহরণে মগ্ন থাকে।' [সহীহ মুসলিম: ২৫৬৮]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Indeed, when a Muslim visits his sick Muslim brother, he remains among the harvested fruits of Paradise until he returns.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ২৫৬৮, তিরমিজি ৯৬৭",
                    explanationBn = "অসুস্থ ব্যক্তির সেবা করা ও তার সুস্থতার জন্য দো'আ করা ঈমানী ভ্রাতৃত্বের অন্যতম শ্রেষ্ঠ দায়িত্ব।"
                ),
                TopicHadithRef(
                    hadithId = "bukhari_5687",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 5687,
                    chapterTitleBn = "চিকিৎসা অধ্যায়",
                    narratorBn = "হযরত আবু হুরায়রা (রা.)",
                    arabicText = "فِي الحَبَّةِ السَّوْدَاءِ شِفَاءٌ مِنْ كُلِّ دَاءٍ، إِلَّا السَّامَ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'কালোজিরায় মৃত্যু ব্যতীত সমস্ত রোগের আরোগ্য ও শেফা রয়েছে।' [সহীহ বুখারী: ৫৬৮৭, সহীহ মুসলিম: ২২১৮]",
                    englishText = "Allah's Messenger (pbuh) said: 'There is healing in black cumin for all diseases except death.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৫৬৮৭, সহীহ মুসলিম ২২১৮",
                    explanationBn = "কালোজিরা দেহের রোগ প্রতিরোধ ক্ষমতা বৃদ্ধি করে এবং শারীরিক সুস্থতা রক্ষায় এক অনন্য প্রাকৃতিক নিয়ামত।"
                )
            )
        ),

        // 4. আহার ও পানাহারের সুন্নাত ও আদব
        HadithTopic(
            id = "topic_eating_drinking_etiquette",
            categoryId = "cat_health",
            nameBn = "আহার ও পানাহারের সুন্নাত ও আদব",
            nameEn = "Etiquettes of Eating & Drinking in Sunnah",
            nameAr = "آداب الطعام والشراب والتسمية والأكل باليمين",
            descriptionBn = "বিসমিল্লাহ বলে শুরু করা, ডান হাত দিয়ে খাওয়া, নিজের নিকটবর্তী অংশ থেকে খাওয়া এবং অপচয় রোধ করার অনুপম নববী শিষ্টাচার।",
            searchKeywordsBn = listOf("খাওয়ার আদব", "বিসমিল্লাহ", "ডান হাত", "পানি পান", "বসে খাওয়া", "অপচয় না করা"),
            searchKeywordsEn = listOf("eating etiquette", "bismillah", "right hand", "drinking water", "sunnah food"),
            isFeatured = false,
            iconEmoji = "🍽️",
            relatedTopicIds = listOf("topic_visiting_sick_sunnah_cure", "topic_jannah_deeds"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "bukhari_5376",
                    bookSlug = "bukhari",
                    bookNameBn = "সহীহ বুখারী",
                    hadithNumber = 5376,
                    chapterTitleBn = "খাদ্য অধ্যায়",
                    narratorBn = "হযরত উমর ইবনে আবু সালামাহ (রা.)",
                    arabicText = "يَا غُلاَمُ، سَمِّ اللَّهَ، وَكُلْ بِيَمِينِكَ، وَكُلْ مِمَّا يَلِيكَ",
                    banglaText = "রাসুলুল্লাহ (সা.) আমাকে বললেন: 'হে বৎস! বিসমিল্লাহ বলো, তোমার ডান হাতে খাও এবং পাত্রের তোমার নিকটবর্তী দিক থেকে আহার করো।' [সহীহ বুখারী: ৫৩৭৬, সহীহ মুসলিম: ২০২২]",
                    englishText = "The Messenger of Allah (pbuh) said to me: 'O boy! Mention the Name of Allah, eat with your right hand, and eat of the dish what is nearer to you.'",
                    gradeBn = "সহীহ (মুত্তাফাক্ব আলাইহ)",
                    gradeColor = "MUTTAFAAQ_ALAYH",
                    sourceBn = "সহীহ বুখারী ৫৩৭৬, সহীহ মুসলিম ২০২২",
                    explanationBn = "খাবারের শুরুতে আল্লাহর নাম নেওয়া বরকতের উৎস এবং ডান হাতে খাওয়া শয়তানের অনুকরণ থেকে মুক্ত রাখে।"
                )
            )
        ),

        // 5. সৎ কাজের আদেশ ও অন্যায় প্রতিরোধ
        HadithTopic(
            id = "topic_enjoining_good_stopping_evil",
            categoryId = "cat_ilm",
            nameBn = "সৎকাজে আদেশ ও অন্যায় প্রতিরোধ",
            nameEn = "Enjoining Good & Stopping Evil (Three Levels)",
            nameAr = "تغيير المنكر باليد واللسان والقلب",
            descriptionBn = "অন্যায় দেখে সাধ্যানুযায়ী হাত দিয়ে, না পারলে মুখ দিয়ে এবং তাও না পারলে অন্তত অন্তর দিয়ে ঘৃণা পোষণ করা। সমাজে অন্যায়ের প্রতিবাদ ঈমানের লক্ষণ।",
            searchKeywordsBn = listOf("অন্যায় প্রতিরোধ", "হাত দিয়ে", "মুখ দিয়ে", "অন্তর দিয়ে ঘৃণা", "ঈমানের স্তর", "নাহি আনিল মুনকার"),
            searchKeywordsEn = listOf("forbidding evil", "enjoining good", "changing munkar", "weakest faith"),
            isFeatured = true,
            iconEmoji = "📢",
            relatedTopicIds = listOf("topic_nasiha_advice", "topic_brotherhood_society"),
            hadiths = listOf(
                TopicHadithRef(
                    hadithId = "muslim_49",
                    bookSlug = "muslim",
                    bookNameBn = "সহীহ মুসলিম",
                    hadithNumber = 49,
                    chapterTitleBn = "ঈমান অধ্যায়",
                    narratorBn = "হযরত আবু সাঈদ আল-খুদরী (রা.)",
                    arabicText = "مَنْ رَأَى مِنْكُمْ مُنْكَرًا فَلْيُغَيِّرْهُ بِيَدِهِ، فَإِنْ لَمْ يَسْتَطِعْ فَبِلِسَانِهِ، فَإِنْ لَمْ يَسْتَطِعْ فَبِقَلْبِهِ، وَذَلِكَ أَضْعَفُ الإِيمَانِ",
                    banglaText = "রাসুলুল্লাহ (সা.) বলেছেন: 'তোমাদের মধ্যে যে ব্যক্তি কোনো অন্যায় বা গর্হিত কাজ হতে দেখবে, সে যেন স্বহস্তে (ক্ষমতা দ্বারা) তা প্রতিহত করে; যদি সে এতে অক্ষম হয়, তবে যেন নিজ জিহ্বা দ্বারা (উপদেশ দিয়ে) তা নিষেধ করে; আর যদি এতেও অক্ষম হয়, তবে যেন সে অন্তর দিয়ে তা ঘৃণা করে—আর এটিই হলো ঈমানের সর্বনিম্ন ও দুর্বলতম স্তর।' [সহীহ মুসলিম: ৪৯]",
                    englishText = "The Messenger of Allah (pbuh) said: 'Whoever among you sees an evil, let him change it with his hand; and if he is not able to do so, then with his tongue; and if he is not able to do so, then with his heart - and that is the weakest of faith.'",
                    gradeBn = "সহীহ",
                    gradeColor = "SAHIH",
                    sourceBn = "সহীহ মুসলিম ৪৯, তিরমিজি ২১৭২, নাসাঈ ৫০০৮",
                    explanationBn = "সমাজে অপরাধ দেখে নীরব দর্শক থাকা অন্যায়কে প্রশ্রয় দেওয়ার শামিল; প্রত্যেক মুমিনের দায়িত্ব সাধ্যমতো ন্যায়ের পক্ষে দাঁড়ানো।"
                )
            )
        )
    )
}
