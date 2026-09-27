package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart1 {

    val topics: List<QuranTopic> = listOf(
        // 1. তাওহীদ ও আল্লাহর একত্ববাদ
        QuranTopic(
            id = "topic_tawheed",
            categoryId = "cat_aqeedah",
            nameBn = "তাওহীদ ও আল্লাহর একত্ববাদ",
            nameEn = "Tawheed & Oneness of Allah",
            nameAr = "توحيد الله",
            descriptionBn = "মহান আল্লাহর একক সত্তা, সর্বময় কর্তৃত্ব ও উপাসনার একমাত্র হকদার হওয়ার অকাট্য দলীলসমূহ।",
            searchKeywordsBn = listOf("তাওহীদ", "একত্ববাদ", "আল্লাহ", "ইলাহ", "রব", "শিরক মুক্ত", "লা ইলাহা ইল্লাল্লাহ"),
            searchKeywordsEn = listOf("tawheed", "oneness", "monotheism", "allah", "deity", "creator"),
            isFeatured = true,
            iconEmoji = "☝️",
            relatedTopicIds = listOf("topic_names_of_allah", "topic_tawakkul", "topic_shirk"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 112,
                    ayahNumber = 1,
                    surahNameBn = "সূরা আল-ইখলাস",
                    surahNameEn = "Surah Al-Ikhlas",
                    surahNameAr = "الإخلاص",
                    totalAyahsInSurah = 4,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
                    translationBn = "বলুন, তিনিই আল্লাহ, একক। আল্লাহ কারও মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী। তিনি কাউকে জন্ম দেননি এবং তাঁকেও জন্ম দেওয়া হয়নি। আর তাঁর সমকক্ষ কেউই নেই।",
                    translationEn = "Say, 'He is Allah, [who is] One, Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.'",
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "তাওহীদের সবচেয়ে বিশদ ও বিশুদ্ধ ঘোষণা যা কুরআনের এক-তৃতীয়াংশের সমতুল্য।"
                ),
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 255,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ",
                    translationBn = "আল্লাহ, তিনি ছাড়া কোনো সত্য ইলাহ নেই। তিনি চিরঞ্জীব, সর্বসত্তার ধারক। তন্দ্রা বা নিদ্রা তাঁকে স্পর্শ করে না। আসমান ও যমীনে যা কিছু রয়েছে সবই তাঁর মালিকানাধীন।",
                    translationEn = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of [all] existence. Neither drowsiness overtakes Him nor sleep.",
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "আয়াতুল কুরসী—পবিত্র কুরআনের সর্বশ্রেষ্ঠ আয়াত যাতে আল্লাহর তাওহীদ ও সার্বভৌমত্ব প্রতিভাত হয়েছে।"
                ),
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 163,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَإِلَٰهُكُمْ إِلَٰهٌ وَاحِدٌ ۖ لَّا إِلَٰهَ إِلَّا هُوَ الرَّحْمَٰنُ الرَّحِيمُ",
                    translationBn = "আর তোমাদের ইলাহ এক ও একক ইলাহ। তিনি ছাড়া কোনো সত্য ইলাহ নেই; তিনি পরম করুণাময়, অসীম দয়ালু।",
                    translationEn = "And your god is one God. There is no deity [worthy of worship] except Him, the Entirely Merciful, the Especially Merciful.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 59,
                    ayahNumber = 22,
                    surahNameBn = "সূরা আল-হাশর",
                    surahNameEn = "Surah Al-Hashr",
                    surahNameAr = "الحشر",
                    totalAyahsInSurah = 24,
                    revelationTypeBn = "মাদানী",
                    arabicText = "هُوَ اللَّهُ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ ۖ عَالِمُ الْغَيْبِ وَالشَّهَادَةِ ۖ هُوَ الرَّحْمَٰنُ الرَّحِيمُ",
                    translationBn = "তিনিই আল্লাহ, যিনি ছাড়া কোনো সত্য ইলাহ নেই। তিনি অদৃশ্য ও দৃশ্যের জ্ঞানী, তিনি পরম করুণাময়, অসীম দয়ালু।",
                    translationEn = "He is Allah, other than whom there is no deity, Knower of the unseen and the witnessed. He is the Entirely Merciful, the Especially Merciful.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 2. আসমাউল হুসনা ও আল্লাহর গুণাবলী
        QuranTopic(
            id = "topic_names_of_allah",
            categoryId = "cat_aqeedah",
            nameBn = "আসমাউল হুসনা ও আল্লাহর গুণাবলী",
            nameEn = "Names & Attributes of Allah",
            nameAr = "أسماء الله الحسنى",
            descriptionBn = "পবিত্র কুরআনে বর্ণিত মহান আল্লাহর সুন্দরতম নামসমূহ এবং অনুপম সিফাত বা গুণাবলী।",
            searchKeywordsBn = listOf("আসমাউল হুসনা", "আল্লাহর নাম", "রহমান", "রহিম", "কুদ্দুস", "সালাম", "আজিজ", "হাকিম"),
            searchKeywordsEn = listOf("names of allah", "attributes", "asmaul husna", "merciful", "all-knowing"),
            isFeatured = true,
            iconEmoji = "✨",
            relatedTopicIds = listOf("topic_tawheed", "topic_mercy_of_allah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 7,
                    ayahNumber = 180,
                    surahNameBn = "সূরা আল-আ'রাফ",
                    surahNameEn = "Surah Al-A'raf",
                    surahNameAr = "الأعراف",
                    totalAyahsInSurah = 206,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلِلَّهِ الْأَسْمَاءُ الْحُسْنَىٰ فَادْعُوهُ بِهَا ۖ وَذَرُوا الَّذِينَ يُلْحِدُونَ فِي أَسْمَائِهِ",
                    translationBn = "আর আল্লাহর জন্যই রয়েছে সুন্দরতম নামসমূহ। অতএব তোমরা তাঁকে সেসব নামেই ডাকো; আর যারা তাঁর নামসমূহের ব্যাপারে সত্যপথ হতে বিচ্যুত হয় তাদের বর্জন করো।",
                    translationEn = "And to Allah belong the best names, so invoke Him by them. And leave [the company of] those who practice deviation concerning His names.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 59,
                    ayahNumber = 23,
                    surahNameBn = "সূরা আল-হাশর",
                    surahNameEn = "Surah Al-Hashr",
                    surahNameAr = "الحشر",
                    totalAyahsInSurah = 24,
                    revelationTypeBn = "মাদানী",
                    arabicText = "هُوَ اللَّهُ الَّذِي لَا إِلَٰهَ إِلَّا هُوَ الْمَلِكُ الْقُدُّوسُ السَّلَامُ الْمُؤْمِنُ الْمُهَيْمِنُ الْعَزِيزُ الْجَبَّارُ الْمُتَكَبِّرُ ۚ سُبْحَانَ اللَّهِ عَمَّا يُشْرِكُونَ",
                    translationBn = "তিনিই আল্লাহ, তিনি ছাড়া কোনো সত্য উপাস্য নেই। তিনি বাদশাহ, পরম পবিত্র, শান্তিদাতা, নিরাপত্তাদাতা, সংরক্ষক, পরাক্রমশালী, মহাপ্রতাপশালী, পরম শ্রেষ্ঠত্বের অধিকারী।",
                    translationEn = "He is Allah, other than whom there is no deity, the Sovereign, the Pure, the Perfection, the Bestower of Faith, the Overseer, the Exalted in Might, the Compeller, the Superior.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 3. তাওয়াক্কুল ও আল্লাহর ওপর ভরসা
        QuranTopic(
            id = "topic_tawakkul",
            categoryId = "cat_aqeedah",
            nameBn = "তাওয়াক্কুল ও আল্লাহর উপর ভরসা",
            nameEn = "Reliance & Trust in Allah (Tawakkul)",
            nameAr = "التوكل على الله",
            descriptionBn = "সর্বাবস্থায় সাধ্যমতো চেষ্টার পর যাবতীয় ফলাফলের জন্য একমাত্র আল্লাহর ওপর পরম ভরসা ও নির্ভরতা।",
            searchKeywordsBn = listOf("তাওয়াক্কুল", "ভরসা", "নির্ভরতা", "আস্থা", "আল্লাহই যথেষ্ট", "হাসবুনাল্লাহ"),
            searchKeywordsEn = listOf("tawakkul", "trust in allah", "reliance", "hasbunallah"),
            isFeatured = true,
            iconEmoji = "⚓",
            relatedTopicIds = listOf("topic_tawheed", "topic_patience", "topic_tranquility"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 65,
                    ayahNumber = 3,
                    surahNameBn = "সূরা আত-ত্বালাক্ব",
                    surahNameEn = "Surah At-Talaq",
                    surahNameAr = "الطلاق",
                    totalAyahsInSurah = 12,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ۚ وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ ۚ إِنَّ اللَّهَ بَالِغُ أَمْرِهِ",
                    translationBn = "এবং তিনি তাকে এমন উৎস হতে রিজিক দেবেন যা সে কল্পনাও করতে পারে না। আর যে ব্যক্তি আল্লাহর ওপর ভরসা করে, তার জন্য তিনিই যথেষ্ট। নিশ্চয় আল্লাহ তাঁর কাজ পূর্ণ করবেনই।",
                    translationEn = "And will provide for him from where he does not expect. And whoever relies upon Allah - then He is sufficient for him. Indeed, Allah will accomplish His purpose.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 159,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Surah Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "فَإِذَا عَزَمْتَ فَتَوَكَّلْ عَلَى اللَّهِ ۚ إِنَّ اللَّهَ يُحِبُّ الْمُتَوَكِّلِينَ",
                    translationBn = "অতঃপর যখন আপনি কোনো কাজের সংকল্প করবেন, তখন আল্লাহর ওপর ভরসা করুন। নিশ্চয় আল্লাহ তাওয়াক্কুলকারীদের (ভরসাকারীদের) ভালোবাসেন।",
                    translationEn = "Then when you have taken a decision, put your trust in Allah. Certainly, Allah loves those who put their trust in Him.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 9,
                    ayahNumber = 129,
                    surahNameBn = "সূরা আত-তাওবাহ",
                    surahNameEn = "Surah At-Tawbah",
                    surahNameAr = "التوبة",
                    totalAyahsInSurah = 129,
                    revelationTypeBn = "মাদানী",
                    arabicText = "فَإِن تَوَلَّوْا فَقُلْ حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
                    translationBn = "অতঃপর তারা যদি মুখ ফিরিয়ে নেয়, তবে বলুন: আমার জন্য আল্লাহই যথেষ্ট, তিনি ছাড়া কোনো সত্য ইলাহ নেই। আমি তাঁরই ওপর ভরসা করেছি এবং তিনিই মহান আরশের অধিপতি।",
                    translationEn = "But if they turn away, then say: 'Sufficient for me is Allah; there is no deity except Him. On Him I have relied, and He is the Lord of the Great Throne.'",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 4. কুরআনের হেদায়েত ও তাদাব্বুর
        QuranTopic(
            id = "topic_quran_guidance",
            categoryId = "cat_quran",
            nameBn = "কুরআনের হেদায়েত ও তাদাব্বুর",
            nameEn = "Quran's Guidance & Reflection (Tadabbur)",
            nameAr = "هدى القرآن وتدبره",
            descriptionBn = "কুরআনুল কারীমের সত্য পথনির্দেশনা, আলো, রোগের নিরাময় এবং গভীর অনুধাবন ও ভাবনার তাগিদ।",
            searchKeywordsBn = listOf("কুরআন", "হেদায়েত", "তাদাব্বুর", "চিন্তা", "শিফা", "রহমত", "আলো"),
            searchKeywordsEn = listOf("quran", "guidance", "reflection", "tadabbur", "healing", "mercy"),
            isFeatured = true,
            iconEmoji = "📖",
            relatedTopicIds = listOf("topic_tawheed", "topic_dawah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 2,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "ذَٰلِكَ الْكِتَابُ لَا رَيْبَ ۛ فِيهِ ۛ هُدًى لِّلْمُتَّقِينَ",
                    translationBn = "এ সেই মহাগ্রন্থ, যাতে কোনো সন্দেহ নেই; এটি মুত্তাকীদের জন্য সুস্পষ্ট পথনির্দেশক।",
                    translationEn = "This is the Book about which there is no doubt, a guidance for those conscious of Allah.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 82,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "Surah An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "أَفَلَا يَتَدَبَّرُونَ الْقُرْآنَ ۚ وَلَوْ كَانَ مِنْ عِندِ غَيْرِ اللَّهِ لَوَجَدُوا فِيهِ اخْتِلَافًا كَثِيرًا",
                    translationBn = "তবে কি তারা কুরআন নিয়ে গভীরভাবে চিন্তা করে না? আর যদি এটি আল্লাহ ছাড়া অন্য কারও পক্ষ থেকে হতো, তবে তারা এতে বহু অসঙ্গতি ও বৈপরীত্য দেখতে পেত।",
                    translationEn = "Then do they not reflect upon the Qur'an? If it had been from other than Allah, they would have found within it much contradiction.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 17,
                    ayahNumber = 82,
                    surahNameBn = "সূরা আল-ইসরা",
                    surahNameEn = "Surah Al-Isra",
                    surahNameAr = "الإسراء",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَنُنَزِّلُ مِنَ الْقُرْآنِ مَا هُوَ شِفَاءٌ وَرَحْمَةٌ لِّلْمُؤْمِنِينَ ۙ وَلَا يَزِيدُ الظَّالِمِينَ إِلَّا خَسَارًا",
                    translationBn = "আর আমি কুরআনে এমন কিছু নাযিল করি যা মুমিনদের জন্য রোগের শেফা ও রহমত, আর তা যালেমদের কেবল ক্ষতিই বৃদ্ধি করে।",
                    translationEn = "And We send down of the Qur'an that which is healing and mercy for the believers, but it does not increase the wrongdoers except in loss.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 5. হযরত মুহাম্মদ (ﷺ)—সর্বকালের শ্রেষ্ঠ রাসূল
        QuranTopic(
            id = "topic_prophet_muhammad",
            categoryId = "cat_prophets",
            nameBn = "হযরত মুহাম্মদ (ﷺ)—রহমাতুল্লিল আলামীন",
            nameEn = "Prophet Muhammad ﷺ - Mercy to All Worlds",
            nameAr = "محمد رسول الله ﷺ",
            descriptionBn = "শেষ নবী ও বিশ্বনবী হযরত মুহাম্মদ (ﷺ)-এর চরিত্র, মর্যাদা, অনুসরণ ও মানবজাতির প্রতি অপার করুণা।",
            searchKeywordsBn = listOf("মুহাম্মদ", "রাসূলুল্লাহ", "নবী", "দরূদ", "সুন্নাহ", "রহমাতুল্লিল আলামীন", "উসওয়াতুন হাসানাহ"),
            searchKeywordsEn = listOf("muhammad", "prophet", "messenger", "mercy", "rasulullah", "sunnah"),
            isFeatured = true,
            iconEmoji = "💚",
            relatedTopicIds = listOf("topic_prophets_all", "topic_dawah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 21,
                    ayahNumber = 107,
                    surahNameBn = "সূরা আল-আম্বিয়া",
                    surahNameEn = "Surah Al-Anbiya",
                    surahNameAr = "الأنبياء",
                    totalAyahsInSurah = 112,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَمَا أَرْسَلْنَاكَ إِلَّا رَحْمَةً لِّلْعَالَمِينَ",
                    translationBn = "আর আমি আপনাকে সমগ্র বিশ্বজগতের জন্য কেবল এক অফুরন্ত রহমত ও করুণারূপেই প্রেরণ করেছি।",
                    translationEn = "And We have not sent you, [O Muhammad], except as a mercy to the worlds.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 33,
                    ayahNumber = 21,
                    surahNameBn = "সূরা আল-আহযাব",
                    surahNameEn = "Surah Al-Ahzab",
                    surahNameAr = "الأحزاب",
                    totalAyahsInSurah = 73,
                    revelationTypeBn = "মাদানী",
                    arabicText = "لَّقَدْ كَانَ لَكُمْ فِي رَسُولِ اللَّهِ أُسْوَةٌ حَسَنَةٌ لِّمَن كَانَ يَرْجُو اللَّهَ وَالْيَوْمَ الْآخِرَ وَذَكَرَ اللَّهَ كَثِيرًا",
                    translationBn = "নিশ্চয় তোমাদের জন্য আল্লাহর রাসূলের মধ্যে রয়েছে সর্বোত্তম আদর্শ—তার জন্য, যে আল্লাহ ও শেষ দিবসের আশা রাখে এবং আল্লাহকে অধিক স্মরণ করে।",
                    translationEn = "There has certainly been for you in the Messenger of Allah an excellent pattern for anyone whose hope is in Allah and the Last Day and [who] remembers Allah often.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 68,
                    ayahNumber = 4,
                    surahNameBn = "সূরা আল-ক্বলম",
                    surahNameEn = "Surah Al-Qalam",
                    surahNameAr = "القلم",
                    totalAyahsInSurah = 52,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَإِنَّكَ لَعَلَىٰ خُلُقٍ عَظِيمٍ",
                    translationBn = "আর নিশ্চয় আপনি মহান চরিত্রের সুউচ্চ মর্যাদায় অধিষ্ঠিত।",
                    translationEn = "And indeed, you are of a great moral character.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 6. সালাত ও নামাজের বিধান
        QuranTopic(
            id = "topic_salah",
            categoryId = "cat_ibadah",
            nameBn = "সালাত ও নামাজের গুরুত্ব",
            nameEn = "Salah (Prayer) & Its Obligation",
            nameAr = "إقامة الصلاة",
            descriptionBn = "সালাত কায়েম করার নির্দেশ, সময়মতো আদায়ের বাধ্যবাধকতা এবং অশ্লীলতা ও পাপ থেকে বাঁচার সর্বোত্তম উপায়।",
            searchKeywordsBn = listOf("নামাজ", "সালাত", "নামায", "সেজদা", "রুকু", "জাকাত", "সালাত কায়েম"),
            searchKeywordsEn = listOf("salah", "prayer", "namaz", "prostration", "ruku", "sujood"),
            isFeatured = true,
            iconEmoji = "🕌",
            relatedTopicIds = listOf("topic_wudu", "topic_dhikr", "topic_patience"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 29,
                    ayahNumber = 45,
                    surahNameBn = "সূরা আল-আনকাবূত",
                    surahNameEn = "Surah Al-'Ankabut",
                    surahNameAr = "العنكبوت",
                    totalAyahsInSurah = 69,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "اتْلُ مَا أُوحِيَ إِلَيْكَ مِنَ الْكِتَابِ وَأَقِمِ الصَّلَاةَ ۖ إِنَّ الصَّلَاةَ تَنْهَىٰ عَنِ الْفَحْشَاءِ وَالْمُنكَرِ ۗ وَلَذِكْرُ اللَّهِ أَكْبَرُ",
                    translationBn = "কিতাব হতে যা আপনার প্রতি ওহী করা হয়েছে তা পাঠ করুন এবং সালাত কায়েম করুন। নিশ্চয় সালাত অশ্লীল ও গর্হিত কাজ থেকে বিরত রাখে। আর আল্লাহর যিকিরই তো সর্বশ্রেষ্ঠ।",
                    translationEn = "Recite, [O Muhammad], what has been revealed to you of the Book and establish prayer. Indeed, prayer prohibits immorality and wrongdoing, and the remembrance of Allah is greater.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 103,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "Surah An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "إِنَّ الصَّلَاةَ كَانَتْ عَلَى الْمُؤْمِنِينَ كِتَابًا مَّوْقُوتًا",
                    translationBn = "নিশ্চয় সালাত মুমিনদের ওপর নির্দিষ্ট সময়ে ফরজ করা হয়েছে।",
                    translationEn = "Indeed, prayer has been decreed upon the believers a decree of specified times.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 20,
                    ayahNumber = 14,
                    surahNameBn = "সূরা ত্বা-হা",
                    surahNameEn = "Surah Ta-Ha",
                    surahNameAr = "طه",
                    totalAyahsInSurah = 135,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "إِنَّنِي أَنَا اللَّهُ لَا إِلَٰهَ إِلَّا أَنَا فَاعْبُدْنِي وَأَقِمِ الصَّلَاةَ لِذِكْرِي",
                    translationBn = "নিশ্চয় আমিই আল্লাহ, আমি ছাড়া কোনো সত্য উপাস্য নেই; অতএব আমারই ইবাদত করুন এবং আমার স্মরণের উদ্দেশ্যে সালাত কায়েম করুন।",
                    translationEn = "Indeed, I am Allah. There is no deity except Me, so worship Me and establish prayer for My remembrance.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 7. দো'আ ও প্রার্থনা
        QuranTopic(
            id = "topic_dua",
            categoryId = "cat_ibadah",
            nameBn = "দো'আ ও আল্লাহর কাছে আকুতি",
            nameEn = "Dua (Supplication) & Calling Upon Allah",
            nameAr = "الدعاء والتضرع",
            descriptionBn = "বান্দার ডাকে আল্লাহর তাৎক্ষণিক সাড়া দেওয়ার ঘোষণা এবং বিনম্র মোনাজাতের শিক্ষণীয় আয়াতসমূহ।",
            searchKeywordsBn = listOf("দোয়া", "দো'আ", "প্রার্থনা", "মোনাজাত", "ডাক", "আল্লাহ কাছে চাওয়া"),
            searchKeywordsEn = listOf("dua", "supplication", "prayer", "asking allah", "munajat"),
            isFeatured = true,
            iconEmoji = "🤲",
            relatedTopicIds = listOf("topic_salah", "topic_dhikr", "topic_tawakkul"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 186,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ ۖ فَلْيَسْتَجِيبُوا لِي وَلْيُؤْمِنُوا بِي لَعَلَّهُمْ يَرْشُدُونَ",
                    translationBn = "আর যখন আমার বান্দাগণ আমার সম্পর্কে আপনাকে জিজ্ঞেস করে, তখন বলুন: নিশ্চয় আমি অতি নিকটে। আহ্বানকারী যখনই আমাকে ডাকে, আমি তার ডাকে সাড়া দিই।",
                    translationEn = "And when My servants ask you, [O Muhammad], concerning Me - indeed I am near. I respond to the invocation of the supplicant when he calls upon Me.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 40,
                    ayahNumber = 60,
                    surahNameBn = "সূরা গাফির",
                    surahNameEn = "Surah Ghafir",
                    surahNameAr = "غافر",
                    totalAyahsInSurah = 85,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَقَالَ رَبُّكُمُ ادْعُونِي أَسْتَجِبْ لَكُمْ ۚ إِنَّ الَّذِينَ يَسْتَكْبِرُونَ عَنْ عِبَادَتِي سَيَدْخُلُونَ جَهَنَّمَ دَاخِرِينَ",
                    translationBn = "আর তোমাদের পালনকর্তা বলেন: তোমরা আমাকে ডাকো, আমি তোমাদের ডাকে সাড়া দেব। নিশ্চয় যারা অহংকারবশত আমার ইবাদত হতে বিমুখ থাকে, তারা লাঞ্ছিত হয়ে জাহান্নামে প্রবেশ করবে।",
                    translationEn = "And your Lord says, 'Call upon Me; I will respond to you.' Indeed, those who disdain My worship will enter Hell [rendered] contemptible.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        )
    )
}
