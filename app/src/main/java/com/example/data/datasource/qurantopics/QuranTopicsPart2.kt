package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart2 {

    val topics: List<QuranTopic> = listOf(
        // 8. ধৈর্য ও অবিচলতা (সবর)
        QuranTopic(
            id = "topic_patience",
            categoryId = "cat_akhlaq",
            nameBn = "ধৈর্য ও অবিচলতা (সবর)",
            nameEn = "Patience, Perseverance & Steadfastness (Sabr)",
            nameAr = "الصبر والثبات",
            descriptionBn = "বিপদ-আপদ, ক্ষুধা ও জীবনের কঠিনতম সংকটে ধৈর্য ধারণকারীদের জন্য আল্লাহর সঙ্গ ও অসীম পুরস্কার।",
            searchKeywordsBn = listOf("ধৈর্য", "সবর", "পরীক্ষা", "বিপদ", "অবিচলতা", "কষ্টে ধৈর্য", "ইন্না লিল্লাহ"),
            searchKeywordsEn = listOf("patience", "sabr", "steadfastness", "perseverance", "trials", "adversity"),
            isFeatured = true,
            iconEmoji = "🛡️",
            relatedTopicIds = listOf("topic_tawakkul", "topic_salah", "topic_tranquility"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 153,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا اسْتَعِينُوا بِالصَّبْرِ وَالصَّلَاةِ ۚ إِنَّ اللَّهَ مَعَ الصَّابِرِينَ",
                    translationBn = "হে মুমিনগণ! তোমরা ধৈর্য ও সালাতের মাধ্যমে সাহায্য প্রার্থনা করো। নিশ্চয় আল্লাহ ধৈর্যশীলদের সাথে আছেন।",
                    translationEn = "O you who have believed, seek help through patience and prayer. Indeed, Allah is with the patient.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 155,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَلَنَبْلُوَنَّكُم بِشَيْءٍ مِّنَ الْخَوْفِ وَالْجُوعِ وَنَقْصٍ مِّنَ الْأَمْوَالِ وَالْأَنفُسِ وَالثَّمَرَاتِ ۗ وَبَشِّرِ الصَّابِرِينَ ۝ الَّذِينَ إِذَا أَصَابَتْهُم مُّصِيبَةٌ قَالُوا إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ",
                    translationBn = "আর আমি অবশ্যই তোমাদের পরীক্ষা করব কিছুটা ভয়, ক্ষুধা, ধন-সম্পদ, জীবন ও ফসলাদির ক্ষতির মাধ্যমে। আর সুসংবাদ দিন ধৈর্যশীলদের, যারা বিপদে পড়লে বলে: নিশ্চয় আমরা আল্লাহরই এবং আমরা তাঁরই দিকে প্রত্যাবর্তনকারী।",
                    translationEn = "And We will surely test you with something of fear and hunger and a loss of wealth and lives and fruits, but give good tidings to the patient, Who, when disaster strikes them, say, 'Indeed we belong to Allah, and indeed to Him we will return.'",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 39,
                    ayahNumber = 10,
                    surahNameBn = "সূরা আয-যুমার",
                    surahNameEn = "Surah Az-Zumar",
                    surahNameAr = "الزمر",
                    totalAyahsInSurah = 75,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "إِنَّمَا يُوَفَّى الصَّابِرُونَ أَجْرَهُم بِغَيْرِ حِسَابٍ",
                    translationBn = "কেবল ধৈর্যশীলদেরই তাদের প্রতিফল অপরিমিত ও হিসাবহীনভাবে পুরোপুরি প্রদান করা হবে।",
                    translationEn = "Indeed, the patient will be given their reward without account.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 9. পিতা-মাতার অধিকার ও সেবা
        QuranTopic(
            id = "topic_parents",
            categoryId = "cat_family",
            nameBn = "পিতা-মাতার অধিকার ও সেবা",
            nameEn = "Rights of Parents & Kindness to Them",
            nameAr = "بر الوالدين",
            descriptionBn = "মা-বাবার প্রতি অসীম সদ্ব্যবহার, বার্ধক্যে তাদের সামনে 'উফ' শব্দটিও না বলা এবং বিনম্র দোয়ার বিধান।",
            searchKeywordsBn = listOf("পিতা মাতা", "মা বাবা", "মা", "বাবা", "পিতা", "মাতা", "বাবা-মা", "মা-বাবা", "পিতামাতা"),
            searchKeywordsEn = listOf("parents", "mother", "father", "kindness to parents", "filial piety"),
            isFeatured = true,
            iconEmoji = "👨‍👩‍👧",
            relatedTopicIds = listOf("topic_family", "topic_dua"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 17,
                    ayahNumber = 23,
                    surahNameBn = "সূরা আল-ইসরা",
                    surahNameEn = "Surah Al-Isra",
                    surahNameAr = "الإسراء",
                    totalAyahsInSurah = 111,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوا إِلَّا إِيَّاهُ وَبِالْوَالِدَيْنِ إِحْسَانًا ۚ إِمَّا يَبْلُغَنَّ عِندَكَ الْكِبَرَ أَحَدُهُمَا أَوْ كِلَاهُمَا فَلَا تَقُل لَّهُمَا أُفٍّ وَلَا تَنْهَرْهُمَا وَقُل لَّهُمَا قَوْلًا كَرِيمًا ۝ وَاخْفِضْ لَهُمَا جَنَاحَ الذُّلِّ مِنَ الرَّحْمَةِ وَقُل رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                    translationBn = "আর আপনার রব নির্দেশ দিয়েছেন যে, তোমরা তিনি ছাড়া অন্য কারও ইবাদত করো না এবং পিতা-মাতার সাথে সদ্ব্যবহার করো। তাদের একজন বা উভয়েই যদি তোমার জীবদ্দশায় বার্ধক্যে উপনীত হয়, তবে তাদের 'উহ্' পর্যন্ত বলো না এবং তাদের ধমক দিও না; বরং তাদের সাথে সম্মানজনক কথা বলো। আর ভালোবাসায় তাদের প্রতি বিনয়ের ডানা অবনমিত করো এবং বলো: হে আমার রব! তাদের প্রতি দয়া করুন, যেমন তারা শৈশবে আমাকে স্নেহভরে লালন-পালন করেছিলেন।",
                    translationEn = "And your Lord has decreed that you not worship except Him, and to parents, good treatment. Whether one or both of them reach old age [while] with you, say not to them [so much as], 'uff,' and do not repel them but speak to them a noble word.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 31,
                    ayahNumber = 14,
                    surahNameBn = "সূরা লোকমান",
                    surahNameEn = "Surah Luqman",
                    surahNameAr = "لقمان",
                    totalAyahsInSurah = 34,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَوَصَّيْنَا الْإِنسَانَ بِوَالِدَيْهِ حَمَلَتْهُ أُمُّهُ وَهْنًا عَلَىٰ وَهْنٍ وَفِصَالُهُ فِي عَامَيْنِ أَنِ اشْكُرْ لِي وَلِوَالِدَيْكَ إِلَيَّ الْمَصِيرُ",
                    translationBn = "আর আমি মানুষকে তার পিতা-মাতার ব্যাপারে নির্দেশ দিয়েছি—তার মা তাকে কষ্টের পর কষ্ট সহ্য করে গর্ভে ধারণ করেছে এবং তার দুধ ছাড়ানো হয় দুই বছরে—সুতরাং আমার প্রতি এবং তোমার পিতা-মাতার প্রতি কৃতজ্ঞ হও। অবশেষে আমারই কাছে ফিরে আসতে হবে।",
                    translationEn = "And We have enjoined upon man [care] for his parents. His mother carried him, [increasing her] in weakness upon weakness, and his weaning is in two years. Be grateful to Me and to your parents; to Me is the [final] destination.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 10. বিবাহ ও দাম্পত্য সম্পর্ক
        QuranTopic(
            id = "topic_marriage",
            categoryId = "cat_family",
            nameBn = "বিবাহ ও দাম্পত্য সম্পর্ক",
            nameEn = "Marriage, Love & Spousal Harmony",
            nameAr = "الزواج والمودة الزوجية",
            descriptionBn = "স্বামী-স্ত্রীর মাঝে পারস্পরিক প্রীতি, ভালোবাসা, প্রচ্ছদ ও শান্তির জন্য বিয়ের গুরুত্ব।",
            searchKeywordsBn = listOf("বিয়ে", "বিবাহ", "স্ত্রী", "স্বামী", "দাম্পত্য", "পরিবার", "ভালোবাসা", "মায়া"),
            searchKeywordsEn = listOf("marriage", "spouse", "husband", "wife", "family", "nikah", "love"),
            isFeatured = true,
            iconEmoji = "💍",
            relatedTopicIds = listOf("topic_parents", "topic_chastity"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 30,
                    ayahNumber = 21,
                    surahNameBn = "সূরা আর-রূম",
                    surahNameEn = "Surah Ar-Rum",
                    surahNameAr = "الروم",
                    totalAyahsInSurah = 60,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَمِنْ آيَاتِهِ أَنْ خَلَقَ لَكُم مِّنْ أَنفُسِكُمْ أَزْوَاجًا لِّتَسْكُنُوا إِلَيْهَا وَجَعَلَ بَيْنَكُم مَّوَدَّةً وَرَحْمَةً ۚ إِنَّ فِي ذَٰلِكَ لَآيَاتٍ لِّقَوْمٍ يَتَفَكَّرُونَ",
                    translationBn = "আর তাঁর এক অনুপম নিদর্শন এই যে, তিনি তোমাদের মধ্য হতে তোমাদের জন্য সঙ্গিনী সৃষ্টি করেছেন যাতে তোমরা তাদের নিকট প্রশান্তি লাভ করো এবং তিনি তোমাদের পরস্পরের মাঝে সৃষ্টি করেছেন ভালোবাসা ও দয়া। নিশ্চয় এতে চিন্তাশীলদের জন্য বহু নিদর্শন রয়েছে।",
                    translationEn = "And of His signs is that He created for you from yourselves mates that you may find tranquility in them; and He placed between you affection and mercy.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 187,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "هُنَّ لِبَاسٌ لَّكُمْ وَأَنتُمْ لِبَاسٌ لَّهُنَّ",
                    translationBn = "তারা তোমাদের পোশাক এবং তোমরা তাদের পোশাক।",
                    translationEn = "They are clothing for you and you are clothing for them.",
                    relevance = TopicRelevance.DIRECT,
                    contextNoteBn = "স্বামী ও স্ত্রীর পারস্পরিক সুরক্ষা, মর্যাদা ও সম্মানজনক আচ্ছাদনের অপূর্ব তুলনা।"
                )
            )
        ),

        // 11. রিজিক ও হালাল জীবিকা
        QuranTopic(
            id = "topic_rizq",
            categoryId = "cat_wealth",
            nameBn = "রিজিক ও হালাল জীবিকা",
            nameEn = "Rizq (Sustenance) & Halal Earning",
            nameAr = "الرزق والكسب الحلال",
            descriptionBn = "প্রতিটি সৃষ্টির রিজিকের দায়িত্ব আল্লাহর ওপর এবং হালালভাবে উপার্জন ও জীবিকা অন্বেষণের শিক্ষা।",
            searchKeywordsBn = listOf("রিজিক", "রিযিক", "জীবিকা", "উপার্জন", "দারিদ্র্য", "রুজি", "অভাব", "খাদ্য"),
            searchKeywordsEn = listOf("rizq", "sustenance", "provision", "earning", "wealth", "poverty"),
            isFeatured = true,
            iconEmoji = "🌾",
            relatedTopicIds = listOf("topic_tawakkul", "topic_charity", "topic_riba"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 11,
                    ayahNumber = 6,
                    surahNameBn = "সূরা হূদ",
                    surahNameEn = "Surah Hud",
                    surahNameAr = "هود",
                    totalAyahsInSurah = 123,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَمَا مِن دَابَّةٍ فِي الْأَرْضِ إِلَّا عَلَى اللَّهِ رِزْقُهَا وَيَعْلَمُ مُسْتَقَرَّهَا وَمُسْتَوْدَعَهَا ۚ كُلٌّ فِي كِتَابٍ مُّبِينٍ",
                    translationBn = "আর ভূ-পৃষ্ঠে বিচরণকারী এমন কোনো প্রাণী নেই যার রিজিকের দায়িত্ব আল্লাহর ওপর নয়। আর তিনি জানেন তাদের স্থায়ী ও অস্থায়ী বাসস্থান। সবকিছুই এক সুস্পষ্ট কিতাবে লিপিবদ্ধ আছে।",
                    translationEn = "And there is no creature on earth but that upon Allah is its provision, and He knows its place of dwelling and place of storage. All is in a clear register.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 67,
                    ayahNumber = 15,
                    surahNameBn = "সূরা আল-মুলক",
                    surahNameEn = "Surah Al-Mulk",
                    surahNameAr = "الملك",
                    totalAyahsInSurah = 30,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "هُوَ الَّذِي جَعَلَ لَكُمُ الْأَرْضَ ذَلُولًا فَامْشُوا فِي مَنَاكِبِهَا وَكُلُوا مِن رِّزْقِهِ ۖ وَإِلَيْهِ النُّشُورُ",
                    translationBn = "তিনিই তো যমীনকে তোমাদের জন্য বশীভূত করে দিয়েছেন, অতএব তোমরা এর পথে-প্রান্তরে বিচরণ করো এবং তাঁর দেওয়া রিজিক থেকে আহার করো; আর শেষ পর্যন্ত তাঁরই কাছে পুনরুজ্জীবন ঘটবে।",
                    translationEn = "It is He who made the earth tame for you - so walk among its slopes and eat of His provision - and to Him is the resurrection.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 12. সুদ (রিবা) এর নিষেধাজ্ঞা
        QuranTopic(
            id = "topic_riba",
            categoryId = "cat_wealth",
            nameBn = "সুদ (রিবা) এর ভয়াবহ নিষেধাজ্ঞা",
            nameEn = "Prohibition of Riba (Usury / Interest)",
            nameAr = "تحريم الربا",
            descriptionBn = "সুদখোরদের আল্লাহর সাথে যুদ্ধের ঘোষণা, ব্যবসার বৈধতা এবং সমাজের শোষণের চূড়ান্ত অবসান।",
            searchKeywordsBn = listOf("সুদ", "রিবা", "ইন্টারেস্ট", "সুদের শাস্তি", "ব্যবসা", "হারাম টাকা"),
            searchKeywordsEn = listOf("riba", "interest", "usury", "prohibition", "banking interest", "exploitation"),
            isFeatured = false,
            iconEmoji = "🚫",
            relatedTopicIds = listOf("topic_rizq", "topic_charity", "topic_halal_food"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 275,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "الَّذِينَ يَأْكُلُونَ الرِّبَا لَا يَقُومُونَ إِلَّا كَمَا يَقُومُ الَّذِي يَتَخَبَّطُهُ الشَّيْطَانُ مِنَ الْمَسِّ ۚ ذَٰلِكَ بِأَنَّهُمْ قَالُوا إِنَّمَا الْبَيْعُ مِثْلُ الرِّبَا ۗ وَأَحَلَّ اللَّهُ الْبَيْعَ وَحَرَّمَ الرِّبَا",
                    translationBn = "যারা সুদ খায়, তারা সেই ব্যক্তির ন্যায় দাঁড়াবে যাকে শয়তান স্পর্শ করে পাগল করে দিয়েছে। এটি এ কারণে যে তারা বলে: ব্যবসাতো সুদের মতোই। অথচ আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে হারাম করেছেন।",
                    translationEn = "Those who consume interest cannot stand [on the Day of Resurrection] except as one stands whom the devil by his touch has driven to madness. That is because they say, 'Trade is [just] like interest.' But Allah has permitted trade and has forbidden interest.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 2,
                    ayahNumber = 278,
                    surahNameBn = "সূরা আল-বাক্বারাহ",
                    surahNameEn = "Surah Al-Baqarah",
                    surahNameAr = "البقرة",
                    totalAyahsInSurah = 286,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا اتَّقُوا اللَّهَ وَذَرُوا مَا بَقِيَ مِنَ الرِّبَا إِن كُنتُم مُّؤْمِنِينَ ۝ فَإِن لَّمْ تَفْعَلُوا فَأْذَنُوا بِحَرْبٍ مِّنَ اللَّهِ وَرَسُولِهِ",
                    translationBn = "হে মুমিনগণ! তোমরা আল্লাহকে ভয় করো এবং সুদের যা বকেয়া আছে তা পরিত্যাগ করো, যদি তোমরা প্রকৃত মুমিন হও। অতঃপর যদি তোমরা তা না করো, তবে আল্লাহ ও তাঁর রাসূলের পক্ষ থেকে যুদ্ধের ঘোষণা জেনে নাও।",
                    translationEn = "O you who have believed, fear Allah and give up what remains of interest, if you should be believers. And if you do not, then be informed of a war [against you] from Allah and His Messenger.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 13. ন্যায়বিচার ও সাক্ষ্যদান
        QuranTopic(
            id = "topic_justice",
            categoryId = "cat_society",
            nameBn = "ন্যায়বিচার ও সত্য সাক্ষ্য",
            nameEn = "Justice, Fairness & Truthful Witness",
            nameAr = "العدل والشهادة بالقسط",
            descriptionBn = "নিজের কিংবা পিতা-মাতার বিরুদ্ধে গেলেও সত্য ও ন্যায়ের ওপর অটল থাকা এবং কারো প্রতি বিদ্বেষের কারণে অবিচার না করা।",
            searchKeywordsBn = listOf("ন্যায়বিচার", "ইনসাফ", "সাক্ষ্য", "আদালত", "সত্য সাক্ষ্য", "জুলুম মুক্ত"),
            searchKeywordsEn = listOf("justice", "fairness", "witness", "adl", "oppression"),
            isFeatured = true,
            iconEmoji = "⚖️",
            relatedTopicIds = listOf("topic_society", "topic_truthfulness"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 135,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "Surah An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُونُوا قَوَّامِينَ بِالْقِسْطِ شُهَدَاءَ لِلَّهِ وَلَوْ عَلَىٰ أَنفُسِكُمْ أَوِ الْوَالِدَيْنِ وَالْأَقْرَبِينَ",
                    translationBn = "হে ঈমানদারগণ! তোমরা ন্যায়ের ওপর দৃঢ়ভাবে প্রতিষ্ঠিত থাকো এবং আল্লাহর ওয়াস্তে সত্যের সাক্ষ্যদাতা হও, যদিও তা তোমাদের নিজেদের অথবা পিতা-মাতা ও নিকটাত্মীয়দের বিরুদ্ধে যায়।",
                    translationEn = "O you who have believed, be persistently standing firm in justice, witnesses for Allah, even if it be against yourselves or parents and relatives.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 5,
                    ayahNumber = 8,
                    surahNameBn = "সূরা আল-মায়িদাহ",
                    surahNameEn = "Surah Al-Ma'idah",
                    surahNameAr = "المائدة",
                    totalAyahsInSurah = 120,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَلَا يَجْرِمَنَّكُمْ شَنَآنُ قَوْمٍ عَلَىٰ أَلَّا تَعْدِلُوا ۚ اعْدِلُوا هُوَ أَقْرَبُ لِلتَّقْوَىٰ ۖ وَاتَّقُوا اللَّهَ",
                    translationBn = "কোনো কওমের প্রতি শত্রুতা যেন তোমাদের অবিচার করতে প্ররোচিত না করে। তোমরা ইনসাফ ও সুবিচার করো, এটাই তাকওয়ার অধিক নিকটবর্তী; এবং আল্লাহকে ভয় করো।",
                    translationEn = "And do not let the hatred of a people prevent you from being just. Be just; that is nearer to righteousness. And fear Allah.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        )
    )
}
