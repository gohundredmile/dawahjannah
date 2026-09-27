package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance

object QuranTopicsPart3 {

    val topics: List<QuranTopic> = listOf(
        // 14. মানসিক শান্তি ও হৃদয়ের প্রশান্তি
        QuranTopic(
            id = "topic_tranquility",
            categoryId = "cat_life_mind",
            nameBn = "মানসিক শান্তি ও হৃদয়ের প্রশান্তি",
            nameEn = "Inner Peace, Tranquility & Solace",
            nameAr = "طمأنينة القلوب وسكينة النفس",
            descriptionBn = "অস্থিরতা ও ভয়ের বিপরীতে আল্লাহর যিকির এবং তাঁর দেওয়া ঐশী প্রশান্তির সন্ধান।",
            searchKeywordsBn = listOf("শান্তি", "প্রশান্তি", "যিকির", "হতাশা", "মানসিক কষ্ট", "অস্থিরতা", "চিন্তা মুক্তি"),
            searchKeywordsEn = listOf("peace", "tranquility", "solace", "anxiety", "depression", "heart peace"),
            isFeatured = true,
            iconEmoji = "🌿",
            relatedTopicIds = listOf("topic_dua", "topic_patience", "topic_tawakkul"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 13,
                    ayahNumber = 28,
                    surahNameBn = "সূরা আর-রা'দ",
                    surahNameEn = "Surah Ar-Ra'd",
                    surahNameAr = "الرعد",
                    totalAyahsInSurah = 43,
                    revelationTypeBn = "মাদানী",
                    arabicText = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
                    translationBn = "যারা ঈমান আনে এবং আল্লাহর স্মরণে যাদের অন্তর প্রশান্ত হয়; জেনে রেখো, কেবল আল্লাহর স্মরণেই অন্তরসমূহ পরম প্রশান্তি লাভ করে।",
                    translationEn = "Those who have believed and whose hearts are assured by the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 94,
                    ayahNumber = 5,
                    surahNameBn = "সূরা আল-ইনশিরাহ",
                    surahNameEn = "Surah Ash-Sharh",
                    surahNameAr = "الشرح",
                    totalAyahsInSurah = 8,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا ۝ إِنَّ مَعَ الْعُسْرِ يُسْرًا",
                    translationBn = "নিশ্চয় কষ্টের সাথেই রয়েছে স্বস্তি। নিশ্চয় কষ্টের সাথেই রয়েছে স্বস্তি।",
                    translationEn = "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 39,
                    ayahNumber = 53,
                    surahNameBn = "সূরা আয-যুমার",
                    surahNameEn = "Surah Az-Zumar",
                    surahNameAr = "الزمر",
                    totalAyahsInSurah = 75,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا ۚ إِنَّهُ هُوَ الْغَفُورُ الرَّحِيمُ",
                    translationBn = "বলুন: হে আমার বান্দাগণ! যারা নিজেদের ওপর অবিচার করেছ, তোমরা আল্লাহর রহমত হতে নিরাশ হয়ো না। নিশ্চয় আল্লাহ সমস্ত গুনাহ ক্ষমা করে দেন। নিশ্চয় তিনি পরম ক্ষমাশীল, পরম দয়ালু।",
                    translationEn = "Say, 'O My servants who have transgressed against themselves, do not despair of the mercy of Allah. Indeed, Allah forgives all sins. Indeed, it is He who is the Forgiving, the Merciful.'",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 15. তওবা ও পাপমোচন
        QuranTopic(
            id = "topic_tawbah",
            categoryId = "cat_aqeedah",
            nameBn = "তওবা ও পাপমোচন",
            nameEn = "Repentance (Tawbah) & Forgiveness",
            nameAr = "التوبة والمغفرة",
            descriptionBn = "গুনাহের পর আল্লাহর দরবারে একনিষ্ঠ অনুশোচনা, ক্ষমা প্রার্থনা এবং সৎকাজে প্রত্যাবর্তনের আয়াতসমূহ।",
            searchKeywordsBn = listOf("তওবা", "ক্ষমা", "পাপ", "মাগফিরাত", "গুনাহ", "ইস্তিগফার", "অনুশোচনা"),
            searchKeywordsEn = listOf("tawbah", "repentance", "forgiveness", "istighfar", "sin", "mercy"),
            isFeatured = true,
            iconEmoji = "🤲",
            relatedTopicIds = listOf("topic_tranquility", "topic_names_of_allah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 66,
                    ayahNumber = 8,
                    surahNameBn = "সূরা আত-তাহরীম",
                    surahNameEn = "Surah At-Tahrim",
                    surahNameAr = "التحريم",
                    totalAyahsInSurah = 12,
                    revelationTypeBn = "মাদানী",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا تُوبُوا إِلَى اللَّهِ تَوْبَةً نَّصُوحًا عَسَىٰ رَبُّكُمْ أَن يُكَفِّرَ عَنكُمْ سَيِّئَاتِكُمْ وَيُدْخِلَكُمْ جَنَّاتٍ تَجْرِي مِن تَحْتِهَا الْأَنْهَارُ",
                    translationBn = "হে মুমিনগণ! তোমরা আল্লাহর নিকট খাঁটি তাওবা (তাওবাতুন নাসূহা) করো। আশা করা যায় তোমাদের রব তোমাদের পাপসমূহ মোচন করবেন এবং তোমাদের প্রবেশ করাবেন জান্নাতে, যার পাদদেশে নদীসমূহ প্রবাহিত।",
                    translationEn = "O you who have believed, repent to Allah with sincere repentance. Perhaps your Lord will remove from you your misdeeds and admit you into gardens beneath which rivers flow.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 4,
                    ayahNumber = 110,
                    surahNameBn = "সূরা আন-নিসা",
                    surahNameEn = "Surah An-Nisa",
                    surahNameAr = "النساء",
                    totalAyahsInSurah = 176,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَمَن يَعْمَلْ سُوءًا أَوْ يَظْلِمْ نَفْسَهُ ثُمَّ يَسْتَغْفِرِ اللَّهَ يَجِدِ اللَّهَ غَفُورًا رَّحِيمًا",
                    translationBn = "আর যে ব্যক্তি মন্দ কাজ করে কিংবা নিজের ওপর অবিচার করে, অতঃপর আল্লাহর কাছে ক্ষমা প্রার্থনা করে, সে আল্লাহকে পরম ক্ষমাশীল ও দয়ালু হিসেবে পাবে।",
                    translationEn = "And whoever does a wrong or wrongs himself but then seeks forgiveness of Allah will find Allah Forgiving and Merciful.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 16. জান্নাত ও অফুরন্ত নিয়ামত
        QuranTopic(
            id = "topic_jannah",
            categoryId = "cat_jannah",
            nameBn = "জান্নাত ও অফুরন্ত নিয়ামত",
            nameEn = "Paradise (Jannah) & Eternal Bliss",
            nameAr = "الجنة ونعيمها",
            descriptionBn = "মুত্তাকীদের চিরস্থায়ী ঠিকানা, সবুজ বাগান, ঝর্ণাধারা, সুস্বাদু পানীয় ও পরম শান্তির দৃশ্যপট।",
            searchKeywordsBn = listOf("জান্নাত", "বেহেশত", "নেয়ামত", "বাগান", "নহর", "চিরস্থায়ী শান্তি", "কাউসার"),
            searchKeywordsEn = listOf("jannah", "paradise", "gardens", "eternal bliss", "heaven"),
            isFeatured = true,
            iconEmoji = "🌸",
            relatedTopicIds = listOf("topic_jahannam", "topic_patience"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 3,
                    ayahNumber = 133,
                    surahNameBn = "সূরা আলে-ইমরান",
                    surahNameEn = "Surah Ali 'Imran",
                    surahNameAr = "آل عمران",
                    totalAyahsInSurah = 200,
                    revelationTypeBn = "মাদানী",
                    arabicText = "وَسَارِعُوا إِلَىٰ مَغْفِرَةٍ مِّن رَّبِّكُمْ وَجَنَّةٍ عَرْضُهَا السَّمَاوَاتُ وَالْأَرْضُ أُعِدَّتْ لِلْمُتَّقِينَ",
                    translationBn = "আর তোমরা দ্রুত অগ্রসর হও তোমাদের রবের ক্ষমার দিকে এবং সেই জান্নাতের দিকে যার পরিধি আসমানসমূহ ও যমীনব্যাপী, যা মুত্তাকীদের জন্য প্রস্তুত রাখা হয়েছে।",
                    translationEn = "And hasten to forgiveness from your Lord and a garden as wide as the heavens and earth, prepared for the righteous.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 47,
                    ayahNumber = 15,
                    surahNameBn = "সূরা মুহাম্মদ",
                    surahNameEn = "Surah Muhammad",
                    surahNameAr = "محمد",
                    totalAyahsInSurah = 38,
                    revelationTypeBn = "মাদানী",
                    arabicText = "مَّثَلُ الْجَنَّةِ الَّتِي وُعِدَ الْمُتَّقُونَ ۖ فِيهَا أَنْهَارٌ مِّن مَّاءٍ غَيْرِ آسِنٍ وَأَنْهَارٌ مِّن لَّبَنٍ لَّمْ يَتَغَيَّرْ طَعْمُهُ وَأَنْهَارٌ مِّنْ خَمْرٍ لَّذَّةٍ لِّلشَّارِبِينَ وَأَنْهَارٌ مِّنْ عَسَلٍ مُّصَفًّى",
                    translationBn = "মুত্তাকীদের যে জান্নাতের প্রতিশ্রুতি দেওয়া হয়েছে তার দৃষ্টান্ত হলো: এতে রয়েছে নির্মল পানির নদী, দুধের নদী যার স্বাদ অপরিবর্তনীয়, পানকারীদের জন্য সুস্বাদু পানীয়ের নদী এবং পরিশোধিত মধুর নদী।",
                    translationEn = "Is the description of Paradise, which the righteous are promised, wherein are rivers of water unaltered, rivers of milk the taste of which never changes, rivers of wine delicious to those who drink, and rivers of purified honey...",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 17. জাহান্নাম ও কঠোর শাস্তি
        QuranTopic(
            id = "topic_jahannam",
            categoryId = "cat_jahannam",
            nameBn = "জাহান্নাম ও কঠোর শাস্তি",
            nameEn = "Hell (Jahannam) & Its Warning",
            nameAr = "جهنم وعذابها",
            descriptionBn = "কাফের, অহংকারী ও পাপীদের কঠিন শাস্তির স্থান এবং জাহান্নামের আগুন থেকে বাঁচার সতর্কবার্তা।",
            searchKeywordsBn = listOf("জাহান্নাম", "দোযখ", "আগুন", "শাস্তি", "আযাব", "যাক্কুম", "জাহান্নামের আগুন"),
            searchKeywordsEn = listOf("jahannam", "hell", "fire", "punishment", "torment", "warning"),
            isFeatured = true,
            iconEmoji = "🔥",
            relatedTopicIds = listOf("topic_jannah", "topic_tawbah"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 25,
                    ayahNumber = 65,
                    surahNameBn = "সূরা আল-ফুরক্বান",
                    surahNameEn = "Surah Al-Furqan",
                    surahNameAr = "الفرقان",
                    totalAyahsInSurah = 77,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَالَّذِينَ يَقُولُونَ رَبَّنَا اصْرِفْ عَنَّا عَذَابَ جَهَنَّمَ ۖ إِنَّ عَذَابَهَا كَانَ غَرَامًا ۝ إِنَّهَا سَاءَتْ مُسْتَقَرًّا وَمُقَامًا",
                    translationBn = "আর যারা প্রার্থনা করে: হে আমাদের পালনকর্তা! আমাদের থেকে জাহান্নামের আযাব সরিয়ে দিন; নিশ্চয় তার শাস্তি হলো নিশ্চিত ধ্বংসাত্মক। নিশ্চয় তা আবাস ও বাসস্থান হিসেবে অত্যন্ত নিকৃষ্ট।",
                    translationEn = "And those who say, 'Our Lord, avert from us the punishment of Hell. Indeed, its punishment is ever adhering; Indeed, it is evil as a settlement and residence.'",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 67,
                    ayahNumber = 6,
                    surahNameBn = "সূরা আল-মুলক",
                    surahNameEn = "Surah Al-Mulk",
                    surahNameAr = "الملك",
                    totalAyahsInSurah = 30,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَلِلَّذِينَ كَفَرُوا بِرَبِّهِمْ عَذَابُ جَهَنَّمَ ۖ وَبِئْسَ الْمَصِيرُ ۝ إِذَا أُلْقُوا فِيهَا سَمِعُوا لَهَا شَهِيقًا وَهِيَ تَفُورُ",
                    translationBn = "আর যারা তাদের পালনকর্তাকে অস্বীকার করে তাদের জন্য রয়েছে জাহান্নামের শাস্তি; আর কতই না নিকৃষ্ট এই প্রত্যাবর্তনস্থল! যখন তাদের এতে নিক্ষেপ করা হবে, তখন তারা এর বিকট গর্জন শুনবে এবং তা উথলে উঠবে।",
                    translationEn = "And for those who disbelieved in their Lord is the punishment of Hell, and wretched is the destination. When they are thrown into it, they hear from it a dreadful inhaling while it boils up.",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 18. কুরআনে উল্লেখিত প্রাণী ও কীটপতঙ্গ
        QuranTopic(
            id = "topic_animals",
            categoryId = "cat_nature_animals",
            nameBn = "কুরআনে উল্লেখিত প্রাণী ও কীটপতঙ্গ",
            nameEn = "Animals & Insects in the Quran",
            nameAr = "الحيوانات والحشرات في القرآن",
            descriptionBn = "গরু, উট, হাতি, মৌমাছি, পিঁপড়া, মাকড়সা, মাছ সহ কুরআনের বিভিন্ন সূরায় উল্লেখিত প্রাণীদের তাৎপর্য।",
            searchKeywordsBn = listOf("প্রাণী", "পশু", "মৌমাছি", "পিঁপড়া", "মাকড়সা", "উট", "গরু", "হাতি", "মাছ", "পাখি"),
            searchKeywordsEn = listOf("animals", "insects", "bee", "ant", "spider", "cow", "camel", "elephant", "fish"),
            isFeatured = true,
            iconEmoji = "🐝",
            relatedTopicIds = listOf("topic_nature", "topic_quran_guidance"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 16,
                    ayahNumber = 68,
                    surahNameBn = "সূরা আন-নাহল",
                    surahNameEn = "Surah An-Nahl",
                    surahNameAr = "النحل",
                    totalAyahsInSurah = 128,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَأَوْحَىٰ رَبُّكَ إِلَى النَّحْلِ أَنِ اتَّخِذِي مِنَ الْجِبَالِ بُيُوتًا وَمِنَ الشَّجَرِ وَمِمَّا يَعْرِشُونَ ۝ ثُمَّ كُلِي مِن كُلِّ الثَّمَرَاتِ فَاسْلُكِي سُبُلَ رَبِّكِ ذُلُلًا ۚ يَخْرُجُ مِن بُطُونِهَا شَرَابٌ مُّخْتَلِفٌ أَلْوَانُهُ فِيهِ شِفَاءٌ لِّلنَّاسِ",
                    translationBn = "আর আপনার রব মৌমাছিকে নির্দেশ দিয়েছেন: পাহাড়ে, গাছে এবং মানুষের ঘরে তোমরা বাসা নির্মাণ করো। অতঃপর বিভিন্ন ফল থেকে আহার করো এবং তোমার রবের সহজ পথ অনুসরণ করো। তাদের পেট থেকে বের হয় বিভিন্ন রঙের পানীয় (মধু), যাতে মানুষের জন্য রয়েছে রোগের নিরাময়।",
                    translationEn = "And your Lord inspired to the bee, 'Take for yourself among the mountains, houses, and among the trees and [in] that which they construct. Then eat from all the fruits and follow the ways of your Lord laid down [for you].' There emerges from their bellies a drink, varying in colors, in which there is healing for people.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 27,
                    ayahNumber = 18,
                    surahNameBn = "সূরা আন-নামল",
                    surahNameEn = "Surah An-Naml",
                    surahNameAr = "النمل",
                    totalAyahsInSurah = 93,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "حَتَّىٰ إِذَا أَتَوْا عَلَىٰ وَادِ النَّمْلِ قَالَتْ نَمْلَةٌ يَا أَيُّهَا النَّمْلُ ادْخُلُوا مَسَاكِنَكُمْ لَا يَحْطِمَنَّكُمْ سُلَيْمَانُ وَجُنُودُهُ وَهُمْ لَا يَشْعُرُونَ",
                    translationBn = "অবশেষে যখন তারা পিঁপড়ার উপত্যকায় পৌঁছাল, তখন এক পিঁপড়া বলল: হে পিঁপড়ের দল! তোমরা নিজ নিজ ঘরে প্রবেশ করো, যাতে সুলাইমান ও তার বাহিনী অজ্ঞাতে তোমাদের পদতলে পিষ্ট না করে ফেলে।",
                    translationEn = "Until, when they came upon the valley of the ants, an ant said, 'O ants, enter your dwellings that you not be crushed by Solomon and his soldiers while they perceive not.'",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 88,
                    ayahNumber = 17,
                    surahNameBn = "সূরা আল-গাশিয়াহ",
                    surahNameEn = "Surah Al-Ghashiyah",
                    surahNameAr = "الغاشية",
                    totalAyahsInSurah = 26,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "أَفَلَا يَنظُرُونَ إِلَى الْإِبِلِ كَيْفَ خُلِقَتْ",
                    translationBn = "তবে কি তারা উটের প্রতি লক্ষ্য করে না যে, কীভাবে তাকে সৃষ্টি করা হয়েছে?",
                    translationEn = "Then do they not look at the camels - how they are created?",
                    relevance = TopicRelevance.DIRECT
                )
            )
        ),

        // 19. দাওয়াত ও হিকমতের সাথে প্রচার
        QuranTopic(
            id = "topic_dawah",
            categoryId = "cat_dawah_peace",
            nameBn = "দাওয়াহ ও হিকমতের সাথে প্রচার",
            nameEn = "Da'wah with Wisdom & Beautiful Preaching",
            nameAr = "الدعوة إلى الله بالحكمة",
            descriptionBn = "প্রজ্ঞা, উত্তম উপদেশ এবং সর্বোত্তম পদ্ধতিতে দ্বীনের দিকে মানুষকে আহ্বান করার মহান নির্দেশ।",
            searchKeywordsBn = listOf("দাওয়াত", "দাওয়াহ", "হিকমত", "প্রচার", "উপদেশ", "দ্বীন প্রচার"),
            searchKeywordsEn = listOf("dawah", "wisdom", "preaching", "calling to allah", "hikmah"),
            isFeatured = true,
            iconEmoji = "📢",
            relatedTopicIds = listOf("topic_prophet_muhammad", "topic_justice"),
            ayahs = listOf(
                TopicAyah(
                    surahNumber = 16,
                    ayahNumber = 125,
                    surahNameBn = "সূরা আন-নাহল",
                    surahNameEn = "Surah An-Nahl",
                    surahNameAr = "النحل",
                    totalAyahsInSurah = 128,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "ادْعُ إِلَىٰ سَبِيلِ رَبِّكَ بِالْحِكْمَةِ وَالْمَوْعِظَةِ الْحَسَنَةِ ۖ وَجَادِلْهُم بِالَّتِي هِيَ أَحْسَنُ ۚ إِنَّ رَبَّكَ هُوَ أَعْلَمُ بِمَن ضَلَّ عَن سَبِيلِهِ ۖ وَهُوَ أَعْلَمُ بِالْمُهْتَدِينَ",
                    translationBn = "আপনি আপনার রবের পথের দিকে আহ্বান করুন প্রজ্ঞা ও উত্তম উপদেশের মাধ্যমে এবং তাদের সাথে বিতর্ক করুন সর্বোত্তম পদ্ধতিতে। নিশ্চয় আপনার রবই সর্বাধিক জানেন কে তাঁর পথ হতে বিচ্যুত হয়েছে এবং তিনি হেদায়াতপ্রাপ্তদের সম্পর্কেও সম্যক জ্ঞাত।",
                    translationEn = "Invite to the way of your Lord with wisdom and good instruction, and argue with them in a way that is best. Indeed, your Lord is most knowing of who has strayed from His way, and He is most knowing of who is guided.",
                    relevance = TopicRelevance.DIRECT
                ),
                TopicAyah(
                    surahNumber = 41,
                    ayahNumber = 33,
                    surahNameBn = "সূরা ফুসসিলাত",
                    surahNameEn = "Surah Fussilat",
                    surahNameAr = "فصلت",
                    totalAyahsInSurah = 54,
                    revelationTypeBn = "মাক্কী",
                    arabicText = "وَمَنْ أَحْسَنُ قَوْلًا مِّمَّن دَعَا إِلَى اللَّهِ وَعَمِلَ صَالِحًا وَقَالَ إِنَّنِي مِنَ الْمُسْلِمِينَ",
                    translationBn = "আর তার চেয়ে কার কথা অধিক উত্তম, যে আল্লাহর দিকে আহ্বান করে, সৎকর্ম করে এবং ঘোষণা দেয়: নিশ্চয় আমি আত্মসমর্পণকারীদের (মুসলিমদের) অন্তর্ভুক্ত?",
                    translationEn = "And who is better in speech than one who invites to Allah and does righteousness and says, 'Indeed, I am of the Muslims'?",
                    relevance = TopicRelevance.DIRECT
                )
            )
        )
    )
}
