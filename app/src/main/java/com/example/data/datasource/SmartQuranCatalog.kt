package com.example.data.datasource

import com.example.data.model.SemanticQuranAyah
import com.example.data.model.SemanticSearchSuggestion

object SmartQuranCatalog {

    val suggestions: List<SemanticSearchSuggestion> = listOf(
        SemanticSearchSuggestion(
            titleEn = "Verses about people who lose hope",
            titleBn = "হতাশ বা নিরাশ হওয়া সম্পর্কে আয়াত",
            categoryBn = "আশা ও সান্ত্বনা",
            iconEmoji = "🌟"
        ),
        SemanticSearchSuggestion(
            titleEn = "Allah's forgiveness",
            titleBn = "আল্লাহর ক্ষমা ও মাগফিরাত",
            categoryBn = "অনুশোচনা ও রহমত",
            iconEmoji = "🕊️"
        ),
        SemanticSearchSuggestion(
            titleEn = "Dealing with anger",
            titleBn = "রাগ ও ক্রোধ নিয়ন্ত্রণ",
            categoryBn = "চরিত্র ও আত্মনিয়ন্ত্রণ",
            iconEmoji = "🧘"
        ),
        SemanticSearchSuggestion(
            titleEn = "Fear of poverty",
            titleBn = "দারিদ্র্যের ভয় ও রিজিকের নিশ্চয়তা",
            categoryBn = "তাওয়াক্কুল ও জীবিকা",
            iconEmoji = "💰"
        ),
        SemanticSearchSuggestion(
            titleEn = "Parents",
            titleBn = "পিতা-মাতার সম্মান ও সেবা",
            categoryBn = "পারিবারিক বন্ধন",
            iconEmoji = "👨‍👩‍👧"
        ),
        SemanticSearchSuggestion(
            titleEn = "Marriage problems",
            titleBn = "দাম্পত্য সমস্যা ও পারস্পরিক সমঝোতা",
            categoryBn = "দাম্পত্য জীবন",
            iconEmoji = "💍"
        ),
        SemanticSearchSuggestion(
            titleEn = "Hard times",
            titleBn = "কঠিন সময়, দুঃখ-কষ্ট ও ধৈর্য",
            categoryBn = "ধৈর্য ও পরীক্ষা",
            iconEmoji = "🌧️"
        ),
        SemanticSearchSuggestion(
            titleEn = "Anxiety and peace of mind",
            titleBn = "মানসিক অস্থিরতা ও হৃদয়ের প্রশান্তি",
            categoryBn = "আত্মিক শান্তি",
            iconEmoji = "🌿"
        ),
        SemanticSearchSuggestion(
            titleEn = "Gratitude and thankfulness",
            titleBn = "শুকরিয়া ও কৃতজ্ঞতাবোধ",
            categoryBn = "ঈমানি গুণাবলী",
            iconEmoji = "🤲"
        ),
        SemanticSearchSuggestion(
            titleEn = "Loneliness and feeling abandoned",
            titleBn = "একাকিত্ব ও নিঃসঙ্গতায় আল্লাহর সান্নিধ্য",
            categoryBn = "আশা ও সান্ত্বনা",
            iconEmoji = "🛡️"
        )
    )

    val catalog: List<SemanticQuranAyah> = listOf(
        // -------------------------------------------------------------
        // 1. LOSING HOPE / DESPAIR (হতাশা বা নিরাশ হওয়া)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_39_53",
            surahNumber = 39,
            ayahNumber = 53,
            surahNameBn = "সূরা আয-যুমার",
            surahNameAr = "سورة الزمر",
            surahNameEn = "Surah Az-Zumar",
            revelationTypeBn = "মাক্কী",
            arabicText = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا ۚ إِنَّهُ هُوَ الْغَفُورُ الرَّحِيمُ",
            transliterationBn = "কুল ইয়া ‘ইবাদিয়াল্লাযীনা আসরাফু ‘আলা আনফুসিহিম লা তাকনাতু মির রাহমাতিল্লাহ; ইন্নাল্লাহা ইয়াগফিরুয যুনূবা জামী‘আ; ইন্নাহূ হুওয়াল গাফূরুর রাহীম।",
            banglaTranslation = "বলুন, ‘হে আমার বান্দাগণ! যারা নিজেদের ওপর অবিচার করেছ, তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না। নিশ্চয় আল্লাহ সমস্ত গুনাহ ক্ষমা করে দেন। নিশ্চয় তিনি অতীব ক্ষমাশীল, পরম দয়ালু।’",
            englishTranslation = "Say, 'O My servants who have transgressed against themselves [by sinning], do not despair of the mercy of Allah. Indeed, Allah forgives all sins. Indeed, it is He who is the Forgiving, the Merciful.'",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: কুরআনুল কারীমের সর্বাধিক আশাব্যঞ্জক আয়াত। বান্দা যত বড় পাপই করুক না কেন বা পাপের বোঝায় যত বেশি হতাশ হোক না কেন, আন্তরিকভাবে তওবা করে ফিরে এলে আল্লাহ তায়ালা তার অতীত সমস্ত পাপ ক্ষমা করে দেন। হতাশ হওয়া শয়তানের প্ররোচনা, মুমিনের নয়।",
            divineWisdomBn = "মানুষ যখন নিজের ভুল, ব্যর্থতা বা পাপে ডুবে মনে করে তার উদ্ধারের আর কোনো পথ নেই, তখন এই আয়াতে আল্লাহ তাকে অত্যন্ত স্নেহের সাথে 'হে আমার প্রিয় বান্দাগণ' বলে সম্বোধন করে আশার দুয়ার উন্মুক্ত করে দেন।",
            relatedThemes = listOf("হতাশা দূরীকরণ", "আল্লাহর রহমত", "তওবা", "পাপমুক্তি", "মানসিক সান্ত্বনা"),
            primaryTopicBn = "হতাশ হওয়া থেকে মুক্তি",
            primaryTopicEn = "Verses about people who lose hope",
            relatedHadithBn = "হযরত আনাস (রা.) হতে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'আল্লাহ বলেন, হে আদম সন্তান! তুমি যতদিন আমাকে ডাকবে এবং আমার নিকট আশা রাখবে, আমি তোমার পূর্বের সমস্ত গুনাহ ক্ষমা করে দেব, আমি কোনো কিছুর পরোয়া করি না।' (জামে আত-তিরমিযী ৩৫৪০)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/039053.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_12_87",
            surahNumber = 12,
            ayahNumber = 87,
            surahNameBn = "সূরা ইউসুফ",
            surahNameAr = "سورة يوسف",
            surahNameEn = "Surah Yusuf",
            revelationTypeBn = "মাক্কী",
            arabicText = "يَا بَنِيَّ اذْهَبُوا فَتَحَسَّسُوا مِن يُوسُفَ وَأَخِيهِ وَلَا تَيْأَسُوا مِن رَّوْحِ اللَّهِ ۖ إِنَّهُ لَا يَيْأَسُ مِن رَّوْحِ اللَّهِ إِلَّا الْقَوْمُ الْكَافِرُونَ",
            transliterationBn = "ইয়া বানিয়্যাযহাবু ফাতাহাস্সাসূ মিঁইউসুফা ওয়া আখীহি ওয়ালা তাই’য়াসূ মির রাওহিল্লাহ; ইন্নাহূ লা ইয়াই’য়াসু মির রাওহিল্লাহি ইল্লাল কাওমুল কাফিরূন।",
            banglaTranslation = "হে আমার ছেলেরা! তোমরা যাও, ইউসুফ ও তার ভাইয়ের সন্ধান নাও এবং আল্লাহর রহমত থেকে নিরাশ হয়ো না। কেননা কাফের সম্প্রদায় ছাড়া অন্য কেউ আল্লাহর রহমত থেকে নিরাশ হয় না।",
            englishTranslation = "O my sons, go and find out about Joseph and his brother and despair not of relief from Allah. Indeed, no one despairs of relief from Allah except the disbelieving people.",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: হযরত ইয়াকুব (আ.) দীর্ঘ কয়েক দশক সন্তান হারানোর সীমাহীন বেদনা সহ্য করেও মুহূর্তের জন্য আল্লাহর রহমত থেকে নিরাশ হননি। তিনি সন্তানদেরও তাগিদ দিয়েছেন আল্লাহর সাহায্য কখনোই অসম্ভব নয়।",
            divineWisdomBn = "পরিস্থিতি যত দীর্ঘকাল প্রতিকূল থাকুক না কেন, বিশ্বাসীর অন্তরে নিরাশার স্থান নেই। আল্লাহ যেকোনো চরম সংকট মুহূর্তের মধ্যে আনন্দের মোড়কে পরিবর্তন করতে পারেন।",
            relatedThemes = listOf("ধৈর্য", "আশার আলো", "আল্লাহর পরিকল্পনা", "সন্তান ও পরিবার"),
            primaryTopicBn = "কখনোই নিরাশ না হওয়া",
            primaryTopicEn = "Verses about people who lose hope",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'জেনে রেখো, ধৈর্যের সাথেই আসে সাহায্য, আর কষ্টের পরেই আসে স্বস্তি এবং কঠিন অবস্থার পরই আসে প্রশস্ততা।' (মুসনাদে আহমাদ ২৮০৩)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/012087.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_15_56",
            surahNumber = 15,
            ayahNumber = 56,
            surahNameBn = "সূরা আল-হিজর",
            surahNameAr = "سورة الحجر",
            surahNameEn = "Surah Al-Hijr",
            revelationTypeBn = "মাক্কী",
            arabicText = "قَالَ وَمَن يَقْنَطُ مِن رَّحْمَةِ رَبِّهِ إِلَّا الضَّالُّونَ",
            transliterationBn = "ক্বালা ওয়া মাইঁ ইয়াকনাতু মির রাহমাতি রাব্বিহী ইল্লাদ দোয়াল্লূন।",
            banglaTranslation = "তিনি বললেন, ‘পথভ্রষ্টরা ছাড়া আর কে তার রবের অনুগ্রহ থেকে নিরাশ হতে পারে?’",
            englishTranslation = "He said, 'And who despairs of the mercy of his Lord except for those astray?'",
            tafsirSummaryBn = "তাফসীরে আত-তাবারী: হযরত ইব্রাহিম (আ.) অতি বার্ধক্যে সন্তান লাভের সুসংবাদ শুনে বিস্মিত হয়েছিলেন, কিন্তু কখনোই আল্লাহর কুদরত ও করুণা থেকে হতাশ হননি। তিনি স্পষ্ট করে দেন যে আল্লাহর অসীম শক্তি সম্পর্কে অজ্ঞরাই নিরাশ হয়।",
            divineWisdomBn = "মানুষের পার্থিব হিসাব যখন শেষ হয়ে যায়, ঠিক সেখান থেকেই আল্লাহর কুদরতের শুরু হয়। আল্লাহর প্রতি দৃঢ় ঈমান হতাশার প্রাচীর ভেঙে দেয়।",
            relatedThemes = listOf("আল্লাহর কুদরত", "পথপ্রদর্শন", "হতাশা বর্জন"),
            primaryTopicBn = "আল্লাহর অনুগ্রহে অগাধ আস্থা",
            primaryTopicEn = "Verses about people who lose hope",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'তোমাদের কেউ যেন আল্লাহর প্রতি সুধারণা পোষণ না করে মৃত্যুবরণ না করে।' (সহীহ মুসলিম ২৮৭৭)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/015056.mp3"
        ),

        // -------------------------------------------------------------
        // 2. ALLAH'S FORGIVENESS (আল্লাহর ক্ষমা)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_3_135",
            surahNumber = 3,
            ayahNumber = 135,
            surahNameBn = "সূরা আলে ইমরান",
            surahNameAr = "سورة آل عمران",
            surahNameEn = "Surah Ali 'Imran",
            revelationTypeBn = "মাদানী",
            arabicText = "وَالَّذِينَ إِذَا فَعَلُوا فَاحِشَةً أَوْ ظَلَمُوا أَنفُسَهُمْ ذَكَرُوا اللَّهَ فَاسْتَغْفَرُوا لِذُنُوبِهِمْ وَمَن يَغْفِرُ الذُّنُوبَ إِلَّا اللَّهُ وَلَمْ يُصِرُّوا عَلَىٰ مَا فَعَلُوا وَهُمْ يَعْلَمُونَ",
            transliterationBn = "ওয়াল্লাযীনা ইযা ফা‘আলূ ফাহিশাতান আও যালামূ আনফুসাহুম যাকারুল্লাহা ফাস্তাগফারূ লিযুনূবিহিম; ওয়া মাইঁ ইয়াগফিরুয যুনূবা ইল্লাল্লাহু ওয়া লাম ইউসিররূ ‘আলা মা ফা‘আলূ ওয়া হুম ইয়া‘লামূন।",
            banglaTranslation = "আর যারা কোনো অশ্লীল কাজ করে ফেললে বা নিজেদের প্রতি অন্যায় করলে আল্লাহকে স্মরণ করে এবং নিজেদের পাপের জন্য ক্ষমা প্রার্থনা করে—আর আল্লাহ ছাড়া কে পাপ ক্ষমা করবে? এবং তারা যা করে ফেলেছে জেনে-বুঝে সেটার ওপর জেদ ধরে থাকে না।",
            englishTranslation = "And those who, when they commit an immorality or wrong themselves, remember Allah and seek forgiveness for their sins - and who can forgive sins except Allah? - and [who] do not persist in what they have done while they know.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: মুত্তাকীদের অন্যতম প্রধান বৈশিষ্ট্য হলো তারা নিষ্পাপ নয়, বরং কোনো ত্রুটি হয়ে গেলে কালবিলম্ব না করে সাথে সাথে রবের দরবারে অনুতপ্ত হয়ে ইস্তিগফার করে এবং পাপের পুনরাবৃত্তি পরিহার করে।",
            divineWisdomBn = "মানুষ ভুলপ্রবণ, কিন্তু আল্লাহর সান্নিধ্য পাওয়ার পথ সবসময় খোলা। লজ্জাবোধ ও আন্তরিক ইস্তিগফার অতীতের সমস্ত কালিমা মুছে দিয়ে রবের প্রিয়পাত্র বানিয়ে দেয়।",
            relatedThemes = listOf("ইস্তিগফার", "তওবা", "আল্লাহর ক্ষমা", "আত্মশুদ্ধি"),
            primaryTopicBn = "আন্তরিক ক্ষমা প্রার্থনা",
            primaryTopicEn = "Allah's forgiveness",
            relatedHadithBn = "হযরত আবু হুরায়রা (রা.) থেকে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'সেই সত্তার কসম যার হাতে আমার প্রাণ, তোমরা যদি গুনাহ না করতে, তবে আল্লাহ তোমাদের উঠিয়ে নিয়ে এমন জাতিকে আনতেন যারা গুনাহ করে আল্লাহর কাছে ক্ষমা চাইতো আর আল্লাহ তাদের ক্ষমা করে দিতেন।' (সহীহ মুসলিম ২৭৪৯)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/003135.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_4_110",
            surahNumber = 4,
            ayahNumber = 110,
            surahNameBn = "সূরা আন-নিসা",
            surahNameAr = "سورة النساء",
            surahNameEn = "Surah An-Nisa",
            revelationTypeBn = "মাদানী",
            arabicText = "وَمَن يَعْمَلْ سُوءًا أَوْ يَظْلِمْ نَفْسَهُ ثُمَّ يَسْتَغْفِرِ اللَّهَ يَجِدِ اللَّهَ غَفُورًا رَّحِيمًا",
            transliterationBn = "ওয়া মাইঁ ইয়া‘মাল সূ-আন আও ইয়াযলিম নাফসাহূ ছুম্মা ইয়াস্তাগফিরিল্লাহি ইয়াজিদিল্লাহা গাফূরার রাহীমা।",
            banglaTranslation = "আর যে ব্যক্তি মন্দ কাজ করে কিংবা নিজের ওপর জুলুম করে, অতঃপর আল্লাহর কাছে ক্ষমা চায়, সে আল্লাহকে অতি ক্ষমাশীল, পরম দয়ালু হিসেবে পাবে।",
            englishTranslation = "And whoever does a wrong or wrongs himself but then seeks forgiveness of Allah will find Allah Forgiving and Merciful.",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: আল্লাহর ক্ষমার প্রতিশ্রুতি সুনিশ্চিত। বান্দা আন্তরিকভাবে অনুশোচনা করলে আল্লাহ তার পূর্বের সব অন্যায় ক্ষমা করে দেন এবং শাস্তি উঠিয়ে নেন।",
            divineWisdomBn = "পাপবোধে নিজেকে জর্জরিত না করে তৎক্ষণাৎ ক্ষমাপ্রার্থনায় নত হওয়া মুমিনের শক্তি। আল্লাহর রহমত তাঁর ক্রোধের ওপর সর্বদা বিজয়ী।",
            relatedThemes = listOf("মাগফিরাত", "করুণা", "তওবা"),
            primaryTopicBn = "ক্ষমাশীলতার সুনিশ্চিত অঙ্গীকার",
            primaryTopicEn = "Allah's forgiveness",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি নিয়মিত ইস্তিগফার করে, আল্লাহ তার প্রতিটি সংকট থেকে মুক্তির পথ করে দেন এবং অভাবনীয় উৎস থেকে রিজিক দান করেন।' (আবু দাউদ ১৫১৮)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/004110.mp3"
        ),

        // -------------------------------------------------------------
        // 3. DEALING WITH ANGER (রাগ ও ক্রোধ নিয়ন্ত্রণ)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_3_134",
            surahNumber = 3,
            ayahNumber = 134,
            surahNameBn = "সূরা আলে ইমরান",
            surahNameAr = "سورة آل عمران",
            surahNameEn = "Surah Ali 'Imran",
            revelationTypeBn = "মাদানী",
            arabicText = "الَّذِينَ يُنفِقُونَ فِي السَّرَّاءِ وَالضَّرَّاءِ وَالْكَاظِمِينَ الْغَيْظَ وَالْعَافِينَ عَنِ النَّاسِ ۗ وَاللَّهُ يُحِبُّ الْمُحْسِنِينَ",
            transliterationBn = "আল্লাযীনা ইউনফিকূনা ফিস সাররা-ই ওয়াদ্ দাররা-ই ওয়াল কাযিমীনাল গাইযা ওয়াল ‘আফীনা ‘ানিন নাস; ওয়াল্লাহু ইউহিব্বুল মুহসিনীন।",
            banglaTranslation = "যারা সচ্ছলতায় ও অভাবের সময় ব্যয় করে এবং যারা ক্রোধ সংবরণকারী আর মানুষের প্রতি ক্ষমাশীল; আর আল্লাহ অনুগ্রহকারীদের ভালোবাসেন।",
            englishTranslation = "Who spend [in the cause of Allah] during ease and hardship and who restrain anger and who pardon the people - and Allah loves the doers of good.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: 'ওয়াল কাযিমীনাল গাইয'—যার অর্থ প্রচণ্ড রাগ হওয়া সত্ত্বেও তা গ্রাস করে শান্ত হওয়া এবং প্রতিশোধ নেওয়ার সুযোগ পেয়েও মানুষকে নিঃশর্ত ক্ষমা করে দেওয়া। এটি জান্নাতবাসীদের মহৎ গুণ।",
            divineWisdomBn = "রাগ মানুষকে অন্ধ ও হিতাহিত জ্ঞানশূন্য করে। রাগ নিয়ন্ত্রণ করা দুর্বলতা নয়, বরং তা প্রকৃত বীরত্ব ও আত্মিক বিজয়ের নিদর্শন যা আল্লাহর গভীর ভালোবাসা এনে দেয়।",
            relatedThemes = listOf("রাগ দমন", "ক্ষমাশীলতা", "সদাচার", "মুহসিনীন"),
            primaryTopicBn = "ক্রোধ সংবরণ ও পরোপকার",
            primaryTopicEn = "Dealing with anger",
            relatedHadithBn = "হযরত আবু হুরায়রা (রা.) থেকে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'কুস্তিতে যে অপরকে পরাভূত করে সে প্রকৃত বীর নয়; বরং প্রকৃত বীর সে, যে রাগের সময় নিজেকে নিয়ন্ত্রণে রাখতে পারে।' (সহীহ বুখারী ৬১১৪, সহীহ মুসলিম ২৬০৯)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/003134.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_41_34",
            surahNumber = 41,
            ayahNumber = 34,
            surahNameBn = "সূরা ফুসসিলাত",
            surahNameAr = "سورة فصلت",
            surahNameEn = "Surah Fussilat",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَلَا تَسْتَوِي الْحَسَنَةُ وَلَا السَّيِّئَةُ ۚ ادْفَعْ بِالَّتِي هِيَ أَحْسَنُ فَإِذَا الَّذِي بَيْنَكَ وَبَيْنَهُ عَدَاوَةٌ كَأَنَّهُ وَلِيٌّ حَمِيمٌ",
            transliterationBn = "ওয়ালা তাস্তাভিল হাসানাতু ওয়ালাস সাইয়িআহ্; ইদফা‘ বিল্লাতী হিয়া আহসানু ফাইযাল্লাযী বাইনাকা ওয়া বাইনাহূ ‘আদাওয়াতুন কাআন্নাহূ ওয়ালিইয়ুন হামীম।",
            banglaTranslation = "আর ভালো ও মন্দ সমান হতে পারে না। তুমি মন্দকে প্রতিহত করো তা দ্বারা যা উৎকৃষ্ট; ফলে তোমার ও যার মাঝে শত্রুতা ছিল, সে অকস্মাৎ অন্তরঙ্গ বন্ধুর মতো হয়ে যাবে।",
            englishTranslation = "And not equal are the good deed and the bad. Repel [evil] by that [deed] which is better; and thereupon the one whom between you and him is enmity [will become] as though he was a devoted friend.",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: যখন কেউ তোমাকে লক্ষ্য করে কটু কথা বলবে বা রাগান্বিত আচরণ করবে, তখন তুমি প্রতিউত্তরে মিষ্টি কথা ও কোমলতা উপহার দাও। মহৎ ধৈর্য শত্রুকেও পরম বান্ধবে পরিণত করতে পারে।",
            divineWisdomBn = "আগুনের ওপর আগুন ঢাললে তা নেভে না, পানি ঢালতে হয়। রাগের প্রত্যুত্তরে ক্রোধ প্রকাশ না করে নম্রতা প্রদর্শন করাই ইসলামি দূরদর্শিতা।",
            relatedThemes = listOf("উদারতা", "মধুর আচরণ", "শত্রুতা নিরসন", "ধৈর্য"),
            primaryTopicBn = "মন্দের বদলে ভালো দিয়ে জয়",
            primaryTopicEn = "Dealing with anger",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'তোমাদের কারো যখন রাগ হয়, সে যদি দাঁড়ানো থাকে তবে যেন বসে পড়ে। আর যদি তাতেও রাগ দূর না হয়, তবে যেন শুয়ে পড়ে।' (সুনান আবু দাউদ ৪৭৮২)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/041034.mp3"
        ),

        // -------------------------------------------------------------
        // 4. FEAR OF POVERTY (দারিদ্র্যের ভয় ও জীবিকা)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_2_268",
            surahNumber = 2,
            ayahNumber = 268,
            surahNameBn = "সূরা আল-বাক্বারাহ",
            surahNameAr = "سورة البقرة",
            surahNameEn = "Surah Al-Baqarah",
            revelationTypeBn = "মাদানী",
            arabicText = "الشَّيْطَانُ يَعِدُكُمُ الْفَقْرَ وَيَأْمُرُكُم بِالْفَحْشَاءِ ۖ وَاللَّهُ يَعِدُكُم مَّغْفِرَةً مِّنْهُ وَفَضْلًا ۗ وَاللَّهُ وَاسِعٌ عَلِيمٌ",
            transliterationBn = "আশ-শাইত্বানু ইয়া‘ইদুকুমুল ফাক্বরা ওয়া ইয়ামুরুকুম বিল ফাহশা-ই ওয়াল্লাহু ইয়া‘ইদুকুম মাগফিরাতাম মিনহু ওয়া ফাদ্বলা; ওয়াল্লাহু ওয়াসি‘উন ‘আলীম।",
            banglaTranslation = "শয়তান তোমাদেরকে দারিদ্র্যের ভয় দেখায় এবং অশ্লীলতার নির্দেশ দেয়; আর আল্লাহ তোমাদেরকে তাঁর পক্ষ থেকে ক্ষমা ও প্রাচুর্যের প্রতিশ্রুতি দেন। আর আল্লাহ প্রাচুর্যময়, সর্বজ্ঞ।",
            englishTranslation = "Satan threatens you with poverty and orders you to immorality, while Allah promises you forgiveness from Him and bounty. And Allah is all-Encompassing and Knowing.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: যখনই মানুষ ভালো কাজে ব্যয় করতে যায় বা ভবিষ্যতের কথা চিন্তা করে, শয়তান তাকে অভাব ও নিঃস্ব হয়ে যাওয়ার ভীতি প্রদর্শন করে কৃপণ বানাতে চায়। বিপরীতে আল্লাহ তায়ালা বরকত ও প্রাচুর্যের প্রতিশ্রুতি দেন।",
            divineWisdomBn = "ভবিষ্যতের অর্থনৈতিক অনিশ্চয়তা নিয়ে অতিরিক্ত উদ্বেগ শয়তানের মনস্তাত্ত্বিক ফাঁদ। আল্লাহর করুণার ওপর ভরসা রাখলে মনের দারিদ্র্য দূর হয় এবং আত্মতৃপ্তি লাভ হয়।",
            relatedThemes = listOf("তাওয়াক্কুল", "রিজিক", "দান-সদকা", "শয়তানের চক্রান্ত"),
            primaryTopicBn = "দারিদ্র্যের ভীতি পরাভূত করা",
            primaryTopicEn = "Fear of poverty",
            relatedHadithBn = "হযরত আবু হুরায়রা (রা.) হতে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'সদকা কখনো সম্পদ হ্রাস করে না।' (সহীহ মুসলিম ২৫৮৮)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002268.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_65_2_3",
            surahNumber = 65,
            ayahNumber = 2,
            surahNameBn = "সূরা আত-ত্বালাক্ব",
            surahNameAr = "سورة الطلاق",
            surahNameEn = "Surah At-Talaq",
            revelationTypeBn = "মাদানী",
            arabicText = "وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ۚ وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ",
            transliterationBn = "ওয়া মাইঁ ইয়াত্তাক্বিল্লাহা ইয়াজ‘আল লাহূ মাখরাজা; ওয়া ইয়ারযুক্বহু মিন হাইছু লা ইয়াহ্‌তাসিব; ওয়া মাইঁ ইয়াতাওয়াক্কাল ‘আলাল্লাহি ফাহুওয়া হাসবুহ্।",
            banglaTranslation = "আর যে ব্যক্তি আল্লাহকে ভয় করে, তিনি তার জন্য উত্তরণের পথ বের করে দেন এবং তাকে তার ধারণাতীত উৎস থেকে রিজিক দান করেন। আর যে ব্যক্তি আল্লাহর ওপর তাওয়াক্কুল (ভরসা) করে, তার জন্য তিনিই যথেষ্ট।",
            englishTranslation = "And whoever fears Allah - He will make for him a way out and will provide for him from where he does not expect. And whoever relies upon Allah - then He is sufficient for him.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর ও মা'আরিফুল কুরআন: তাকওয়া এবং তাওয়াক্কুল হলো জীবনের প্রতিটি সংকীর্ণতা ও অভাব থেকে মুক্তির মহাশক্তি। মানুষের সীমাবদ্ধ বুদ্ধিতে যেখানে পথ শেষ হয়ে যায়, আল্লাহর গায়েবী সাহায্য সেখান থেকেই অবারিত হয়।",
            divineWisdomBn = "মানুষ যখন সমস্ত জাগতিক অবলম্বন বাদ দিয়ে সর্বান্তঃকরণে আল্লাহর ওপর নির্ভরশীল হয়, তখন আল্লাহ তার যাবতীয় পার্থিব ও মানসিক অভাব পূরণে একাই স্বয়ংসম্পূর্ণ হয়ে যান।",
            relatedThemes = listOf("তাকওয়া", "তাওয়াক্কুল", "রিজিক বৃদ্ধি", "সংকট মুক্তি"),
            primaryTopicBn = "অপ্রত্যাশিত রিজিক ও নির্ভরতা",
            primaryTopicEn = "Fear of poverty",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা যদি আল্লাহর প্রতি যথাযথ তাওয়াক্কুল করতে, তবে তিনি তোমাদেরকে পাখিদের মতো রিজিক দিতেন—যারা সকালে খালি পেটে বের হয় এবং সন্ধ্যায় পূর্ণ পেটে নীড়ে ফেরে।' (তিরমিযী ২৩৪৪)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/065002.mp3"
        ),

        // -------------------------------------------------------------
        // 5. PARENTS (পিতা-মাতা)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_17_23_24",
            surahNumber = 17,
            ayahNumber = 23,
            surahNameBn = "সূরা আল-ইসরা",
            surahNameAr = "سورة الإسراء",
            surahNameEn = "Surah Al-Isra",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوا إِلَّا إِيَّاهُ وَبِالْوَالِدَيْنِ إِحْسَانًا ۚ إِمَّا يَبْلُغَنَّ عِندَكَ الْكِبَرَ أَحَدُهُمَا أَوْ كِلَاهُمَا فَلَا تَقُل لَّهُمَا أُفٍّ وَلَا تَنْهَرْهُمَا وَقُل لَّهُمَا قَوْلًا كَرِيمًا وَاخْفِضْ لَهُمَا جَنَاحَ الذُّلِّ مِنَ الرَّحْمَةِ وَقُل رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            transliterationBn = "ওয়া ক্বাদ্বা রাব্বুকা আল্লা তা‘বুদূ ইল্লা ইয়্যাহু ওয়াবিল ওয়ালিদাইনি ইহসানা; ইম্মা ইয়াবলুগান্না ‘ইনদাকাল কিবারা আহাদুহুমা আও কিলাহুমা ফালা তাকুল লাহূমা উফফিঁও ওয়ালা তানহারহুমা ওয়া কুল লাহূমা কাওলান কারীমা। ওয়াখফিদ লাহূমা জানাহায যুল্লি মিনার রাহমাতি ওয়া কুর রাব্বির হামহুমা কামা রাব্বায়ানী সাগীরা।",
            banglaTranslation = "আর আপনার রব নির্দেশ দিয়েছেন যে, তোমরা তিনি ছাড়া অন্য কারো ইবাদত করবে না এবং পিতা-মাতার সাথে উত্তম আচরণ করবে। তাদের একজন বা উভয়েই যদি তোমার জীবদ্দশায় বার্ধক্যে উপনীত হয়, তবে তাদেরকে ‘উফ’ (বিরক্তি প্রকাশসূচক শব্দ) পর্যন্ত বলবে না, তাদেরকে ধমক দেবে না এবং তাদের সাথে সম্মানজনক কথা বলবে। আর মমতাবশে তাদের প্রতি বিনয়ের ডানা অবনমিত করো এবং বলো, ‘হে আমার রব! তাদের প্রতি দয়া করুন যেভাবে শৈশবে তারা আমাকে লালন-পালন করেছেন।’",
            englishTranslation = "And your Lord has decreed that you not worship except Him, and to parents, good treatment. Whether one or both of them reach old age [while] with you, say not to them [so much as], 'uff,' and do not repel them but speak to them a noble word. And lower to them the wing of humility out of mercy and say, 'My Lord, have mercy upon them as they brought me up [when I was] small.'",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: তাওহীদের পরপরই আল্লাহ তায়ালা পিতা-মাতার অধিকারকে স্থান দিয়েছেন। বার্ধক্যে উপনীত পিতা-মাতার মেজাজ বা স্বাস্থ্যের কারণে কোনো বিরক্তি প্রকাশ করা কঠোরভাবে নিষেধ করা হয়েছে।",
            divineWisdomBn = "শৈশবে পিতামাতা যেভাবে নিঃস্বার্থ স্নেহে নিজের আরাম বিসর্জন দিয়েছেন, বার্ধক্যে তাদের তেমনই সর্বোচ্চ সম্মান ও কোমল সেবা দেওয়া সন্তানের ঈমানি দায়িত্ব ও জান্নাতের প্রবেশদ্বার।",
            relatedThemes = listOf("পিতা-মাতার খেদমত", "দোয়া", "পারিবারিক শিষ্টাচার", "বিনয়"),
            primaryTopicBn = "পিতা-মাতার প্রতি অকৃত্রিম শ্রদ্ধা",
            primaryTopicEn = "Parents",
            relatedHadithBn = "এক ব্যক্তি রাসূলুল্লাহ (সা.)-এর নিকট এসে জিজ্ঞেস করল: 'আমার সর্বোত্তম সান্নিধ্য পাওয়ার সর্বাধিক হকদার কে?' তিনি বললেন: 'তোমার মা।' লোকটি বলল: 'তারপর কে?' তিনি বললেন: 'তোমার মা।' লোকটি বলল: 'তারপর কে?' তিনি বললেন: 'তোমার মা।' লোকটি বলল: 'তারপর কে?' তিনি বললেন: 'তোমার পিতা।' (সহীহ বুখারী ৫৯৭১, সহীহ মুসলিম ২৫৪৮)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/017023.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_31_14",
            surahNumber = 31,
            ayahNumber = 14,
            surahNameBn = "সূরা লুক্বমান",
            surahNameAr = "سورة لقمان",
            surahNameEn = "Surah Luqman",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَوَصَّيْنَا الْإِنسَانَ بِوَالِدَيْهِ حَمَلَتْهُ أُمُّهُ وَهْنًا عَلَىٰ وَهْنٍ وَفِصَالُهُ فِي عَامَيْنِ أَنِ اشْكُرْ لِي وَلِوَالِدَيْكَ إِلَيَّ الْمَصِيرُ",
            transliterationBn = "ওয়া ওয়াস্সাইনাল ইনসানা বিওয়ালিদাইহি হামালাতহু উম্মুহূ ওয়াহনান ‘আলা ওয়াহনিঁও ওয়া ফিসালুহূ ফী ‘আমাইনি আনিশকুর লী ওয়ালিওয়ালিদাইকা ইলাইয়াল মাসীর।",
            banglaTranslation = "আর আমি মানুষকে তার পিতা-মাতার প্রতি সদয় হওয়ার নির্দেশ দিয়েছি। তার মা কষ্টের ওপর কষ্ট সহ্য করে তাকে গর্ভে ধারণ করেছে এবং তার দুধ ছাড়ানো হয় দুই বছরে। সুতরাং আমার প্রতি এবং তোমার পিতা-মাতার প্রতি কৃতজ্ঞ হও। অবশেষে প্রত্যাবর্তন তো আমারই কাছে।",
            englishTranslation = "And We have enjoined upon man [care] for his parents. His mother carried him, [increasing her] in weakness upon weakness, and his weaning is in two years. Be grateful to Me and to your parents; to Me is the [final] destination.",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: সন্তানের জন্য মায়ের অবর্ণনীয় ত্যাগের কথা স্মরণ করিয়ে দিয়ে আল্লাহ তা'আলা নিজের প্রতি কৃতজ্ঞতার পরেই পিতা-মাতার প্রতি কৃতজ্ঞ হওয়াকে বাধ্যতামূলক করেছেন।",
            divineWisdomBn = "মানুষের সফলতার ভিত্তি পিতা-মাতার সন্তুষ্টির ওপর নির্ভরশীল। রবের কৃতজ্ঞতা অপূর্ণ থেকে যায় যদি পিতা-মাতার ঋণ স্বীকার ও সেবা না করা হয়।",
            relatedThemes = listOf("মায়ের মর্যাদা", "কৃতজ্ঞতা", "পরিবার"),
            primaryTopicBn = "মায়ের অপরিসীম আত্মত্যাগ",
            primaryTopicEn = "Parents",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'পিতার সন্তুষ্টিতে রবের সন্তুষ্টি এবং পিতার অসন্তুষ্টিতে রবের অসন্তুষ্টি।' (জামে আত-তিরমিযী ১৮৯৯)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/031014.mp3"
        ),

        // -------------------------------------------------------------
        // 6. MARRIAGE PROBLEMS (দাম্পত্য সমস্যা ও পারস্পরিক শান্তি)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_30_21",
            surahNumber = 30,
            ayahNumber = 21,
            surahNameBn = "সূরা আর-রূম",
            surahNameAr = "سورة الروم",
            surahNameEn = "Surah Ar-Rum",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَمِنْ آيَاتِهِ أَنْ خَلَقَ لَكُم مِّنْ أَنفُسِكُمْ أَزْوَاجًا لِّتَسْكُنُوا إِلَيْهَا وَجَعَلَ بَيْنَكُم مَّوَدَّةً وَرَحْمَةً ۚ إِنَّ فِي ذَٰلِكَ لَآيَاتٍ لِّقَوْمٍ يَتَفَكَّرُونَ",
            transliterationBn = "ওয়া মিন আয়াতিহী আন খালাকা লাকুম মিন আনফুসিকুম আযওয়াজাল লিতাসকুনূ ইলাইহা ওয়া জা‘আলা বাইনাকুম মাওয়াদ্দাতাওঁ ওয়া রাহমাহ্; ইন্না ফী যালিকা লাআ-য়াতিল লিকাওমিঁই ইয়াতাফাক্কারূন।",
            banglaTranslation = "আর তাঁর নিদর্শনাবলীর মধ্যে একটি হলো যে, তিনি তোমাদের জন্য তোমাদের মধ্য থেকেই সৃষ্টি করেছেন তোমাদের সঙ্গিনীদের, যাতে তোমরা তাদের কাছে প্রশান্তি পাও এবং তিনি তোমাদের মাঝে সৃষ্টি করেছেন ভালোবাসা ও দয়া। নিশ্চয় এতে চিন্তাশীল সম্প্রদায়ের জন্য বহু নিদর্শন রয়েছে।",
            englishTranslation = "And of His signs is that He created for you from yourselves mates that you may find tranquility in them; and He placed between you affection and mercy. Indeed in that are signs for a people who give thought.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: দাম্পত্যের মূল ভিত্তি হলো তিনটি—সুকূন (মানসিক প্রশান্তি), মাওয়াদ্দাহ (আন্তরিক ভালোবাসা) এবং রাহমাহ (পরস্পরের প্রতি সহানুভূতি ও ক্ষমাশীলতা)। এই তিনটির সমন্বয়ে যেকোনো দাম্পত্য সংকট দূর হয়।",
            divineWisdomBn = "দাম্পত্য কলহ দেখা দিলে একে অপরকে শত্রু না ভেবে বরং স্মরণ করা উচিত যে আল্লাহ এই সম্পর্কটি দিয়েছেন হৃদয়ের আশ্রয় ও প্রশান্তি হিসেবে। মতপার্থক্য ভালোবাসার চেয়ে বড় হতে পারে না।",
            relatedThemes = listOf("দাম্পত্য শান্তি", "পারস্পরিক ভালোবাসা", "দয়া", "সংসার"),
            primaryTopicBn = "দাম্পত্যের মূল ভিত্তি ভালোবাসা ও প্রশান্তি",
            primaryTopicEn = "Marriage problems",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'তোমাদের মধ্যে সর্বোত্তম ব্যক্তি সে, যে তার পরিবারের নিকট উত্তম। আর আমি আমার পরিবারের নিকট তোমাদের মধ্যে সর্বাধিক উত্তম।' (জামে আত-তিরমিযী ৩৮৯৫)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/030021.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_4_19",
            surahNumber = 4,
            ayahNumber = 19,
            surahNameBn = "সূরা আন-নিসা",
            surahNameAr = "سورة النساء",
            surahNameEn = "Surah An-Nisa",
            revelationTypeBn = "মাদানী",
            arabicText = "وَعَاشِرُوهُنَّ بِالْمَعْرُوفِ ۚ فَإِن كَرِهْتُمُوهُنَّ فَعَسَىٰ أَن تَكْرَهُوا شَيْئًا وَيَجْعَلَ اللَّهُ فِيهِ خَيْرًا كَثِيرًا",
            transliterationBn = "ওয়া ‘আশিরূহুন্না বিল মা‘রূফ; ফাতিন কারিহতুমূহুন্না ফা‘আসা- আন তাকরাহূ শাই’আওঁ ওয়া ইয়াজ‘আলাল্লাহু ফীহি খাইরান কাছীরা।",
            banglaTranslation = "তোমরা নারীদের সাথে সদ্ভাবে ও সুন্দর শিষ্টাচারে জীবনযাপন করো। অতঃপর যদি তোমরা তাদেরকে অপছন্দও করো, তবে হয়তো তোমরা এমন কিছুকে অপছন্দ করছ যাতে আল্লাহ বিপুল কল্যাণ নিহিত রেখেছেন।",
            englishTranslation = "And live with them in kindness. For if you dislike them - perhaps you dislike a thing and Allah makes therein much good.",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: কোনো ত্রুটি বা মেজাজের অমিলের কারণে সঙ্গীকে তৎক্ষণাৎ বর্জন না করে ধৈর্য ধারণ করা উচিত। মানুষের পছন্দের সীমাবদ্ধ দৃষ্টি যেখানে খারাপ দেখে, আল্লাহ সেখানে পরম কল্যাণ লুকায়িত রাখতে পারেন।",
            divineWisdomBn = "নিখুঁত মানুষ পৃথিবীতে নেই। সঙ্গীর কোনো একটি স্বভাব অপছন্দ হলেও তার অন্য অসংখ্য ভালো গুণাবলীকে মূল্যায়ন করাই দাম্পত্য রক্ষার সোনালী সূত্র।",
            relatedThemes = listOf("দাম্পত্য ধৈর্য", "সদ্ব্যবহার", "কল্যাণ দর্শন"),
            primaryTopicBn = "মতভেদে ধৈর্য ও সদ্ব্যবহার",
            primaryTopicEn = "Marriage problems",
            relatedHadithBn = "হযরত আবু হুরায়রা (রা.) থেকে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'কোনো মুমিন পুরুষ যেন কোনো মুমিন নারীকে সম্পূর্ণরূপে অপছন্দ না করে। তার একটি স্বভাব অপছন্দ হলেও অন্য স্বভাবে সে সন্তুষ্ট হবে।' (সহীহ মুসলিম ১৪৬৮)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/004019.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_4_128",
            surahNumber = 4,
            ayahNumber = 128,
            surahNameBn = "সূরা আন-নিসা",
            surahNameAr = "سورة النساء",
            surahNameEn = "Surah An-Nisa",
            revelationTypeBn = "মাদানী",
            arabicText = "وَالصُّلْحُ خَيْرٌ ۗ وَأُحْضِرَتِ الْأَنفُسُ الشُّحَّ ۚ وَإِن تُحْسِنُوا وَتَتَّقُوا فَإِنَّ اللَّهَ كَانَ بِمَا تَعْمَلُونَ خَبِيرًا",
            transliterationBn = "ওয়াস সুলহু খাইর; ওয়া উহদিরতিল আনফুসুশ শুহ্হ; ওয়া ইন তুহসিনূ ওয়া তাত্তাক্বূ ফাইন্নাল্লাহা কানা বিমা তা‘মালূনা খাবীরা।",
            banglaTranslation = "আর আপস-মীমাংসাই সর্বোত্তম। মানুষের মন স্বভাবতই স্বার্থপরতায় আচ্ছন্ন থাকে। তবে তোমরা যদি পারস্পরিক সদাচরণ করো এবং আল্লাহকে ভয় করো, তবে নিশ্চয় আল্লাহ তোমাদের যাবতীয় কর্ম সম্পর্কে পূর্ণ অবগত।",
            englishTranslation = "And settlement is best. And present in [human] souls is stinginess. But if you do good and fear Allah - then indeed Allah is ever, with what you do, Acquainted.",
            tafsirSummaryBn = "তাফসীরে আত-তাবারী: দাম্পত্যে তিক্ততা দূরীকরণের প্রধান চাবিকাঠি হলো পারস্পরিক আপস ও ছাড় দেওয়ার মানসিকতা। জেদ বজায় রাখার চেয়ে সমঝোতা করে সংসার টিকিয়ে রাখা আল্লাহর কাছে অত্যন্ত পছন্দনীয়।",
            divineWisdomBn = "জিদ ও অহমিকা সম্পর্ক ভেঙে দেয়। ভালোবাসা ও আল্লাহভীতি থেকে এক ধাপ পিছু হটে মীমাংসা করাই পরিপক্বতা ও সর্বোত্তম পথ।",
            relatedThemes = listOf("আপস-মীমাংসা", "সংসার রক্ষা", "তাকওয়া"),
            primaryTopicBn = "সমঝোতা ও আপস সর্বোত্তম",
            primaryTopicEn = "Marriage problems",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'ইবলিসের কাছে সবচেয়ে প্রিয় কাজ হলো স্বামী ও স্ত্রীর মধ্যে বিচ্ছেদ ঘটানো।' (সহীহ মুসলিম ২৮১৩)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/004128.mp3"
        ),

        // -------------------------------------------------------------
        // 7. HARD TIMES / ADVERSITY (কঠিন সময় ও দুঃখ-কষ্ট)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_94_5_6",
            surahNumber = 94,
            ayahNumber = 5,
            surahNameBn = "সূরা আল-ইনশিরাহ",
            surahNameAr = "سورة الشرح",
            surahNameEn = "Surah Ash-Sharh",
            revelationTypeBn = "মাক্কী",
            arabicText = "فَإِنَّ مَعَ الْعُسْرِ يُسْرًا إِنَّ مَعَ الْعُسْرِ يُسْرًا",
            transliterationBn = "ফাইন্না মা‘আল ‘উসরি ইউসরা; ইন্না মা‘আল ‘উসরি ইউসরা।",
            banglaTranslation = "নিশ্চয় কষ্টের সাথেই স্বস্তি রয়েছে। নিশ্চয় কষ্টের সাথেই স্বস্তি রয়েছে।",
            englishTranslation = "For indeed, with hardship [will be] ease. Indeed, with hardship [will be] ease.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: আরবি ব্যাকরণ অনুযায়ী 'আল-উসর' (কষ্ট) নির্দিষ্ট একবারের, আর 'ইউসর' (স্বস্তি) অনির্দিষ্ট ও ব্যাপক। এর অর্থ এক কষ্টের বিপরীতে আল্লাহ বহুগুণে স্বস্তি ও প্রাচুর্য অবতীর্ণ করেন। কষ্ট কখনোই চিরস্থায়ী নয়।",
            divineWisdomBn = "আল্লাহ বলেননি কষ্টের 'পরে' স্বস্তি, বরং বলেছেন কষ্টের 'সাথেই' স্বস্তি। চরম সংকটের গর্ভেই নিহিত থাকে বিজয়ের বীজ। রাত যত গভীর হয়, সুবহে সাদিক তত নিকটে আসে।",
            relatedThemes = listOf("স্বস্তি", "কষ্টের অবসান", "আশা", "ধৈর্য"),
            primaryTopicBn = "কষ্টের সাথেই দ্বিগুণ স্বস্তি",
            primaryTopicEn = "Hard times",
            relatedHadithBn = "আব্দুল্লাহ ইবনে আব্বাস (রা.) থেকে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'জেনে রেখো, বিপদের পরই আসে উদ্ধার এবং কষ্টের পরই আসে স্বস্তি।' (মুসনাদে আহমাদ ২৮০৩)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/094005.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_2_155_156",
            surahNumber = 2,
            ayahNumber = 155,
            surahNameBn = "সূরা আল-বাক্বারাহ",
            surahNameAr = "سورة البقرة",
            surahNameEn = "Surah Al-Baqarah",
            revelationTypeBn = "মাদানী",
            arabicText = "وَلَنَبْلُوَنَّكُم بِشَيْءٍ مِّنَ الْخَوْفِ وَالْجُوعِ وَنَقْصٍ مِّنَ الْأَمْوَالِ وَالْأَنفُسِ وَالثَّمَرَاتِ ۗ وَبَشِّرِ الصَّابِرِينَ الَّذِينَ إِذَا أَصَابَتْهُم مُّصِيبَةٌ قَالُوا إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ",
            transliterationBn = "ওয়া লানাবলুওয়ান্নাকুম বিশাই’ইম মিনাল খাউফি ওয়াল জু‘ই ওয়া নাক্বসিম মিনাল আমওয়ালি ওয়াল আনফুসি ওয়াস ছামারাত; ওয়া বাশশিরিস সাবিরীন। আল্লাযীনা ইযা আসাবাতহুম মুসীবাতুন ক্বালূ ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রাজি‘ঊন।",
            banglaTranslation = "আর আমি অবশ্যই তোমাদেরকে পরীক্ষা করব কিছু ভয়, ক্ষুধা এবং ধন-সম্পদ, জীবন ও ফসলের ক্ষয়ক্ষতি দিয়ে; আর ধৈর্যশীলদের সুসংবাদ দিন—যারা কোনো বিপদে পড়লে বলে, ‘নিশ্চয় আমরা আল্লাহরই এবং নিশ্চয় আমরা তাঁরই দিকে প্রত্যাবর্তনকারী।’",
            englishTranslation = "And We will surely test you with something of fear and hunger and a loss of wealth and lives and fruits, but give good tidings to the patient, Who, when disaster strikes them, say, 'Indeed we belong to Allah, and indeed to Him we will return.'",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: পার্থিব জীবন পরীক্ষার ক্ষেত্র। বিপদ মুমিনের ওপর শাস্তি নয়, বরং মর্যাদা বৃদ্ধি ও পাপ মোচনের উপায়। ধৈর্যশীলদের জন্য রয়েছে আল্লাহর বিশেষ রহমত ও মাগফিরাত।",
            divineWisdomBn = "'ইন্না লিল্লাহ' উপলব্ধি মানুষের অহংকার ও শোক নিমিষেই দূর করে দেয়। আমরা ও আমাদের সব সম্পদ আল্লাহরই আমানত, তাই হারানোর বেদনায় মুষড়ে পড়ার বদলে রবের ফয়সালায় আত্মসমর্পণ করাই শান্তির উপায়।",
            relatedThemes = listOf("ধৈর্য", "ইন্না লিল্লাহ", "পরীক্ষা", "বিপদমুক্তি"),
            primaryTopicBn = "পরীক্ষায় ধৈর্য ও রবের ফয়সালায় সন্তুষ্টি",
            primaryTopicEn = "Hard times",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'মুমিনের বিষয়টি কতই না চমৎকার! তার প্রতিটি অবস্থাতেই কল্যাণ নিহিত। আনন্দের কিছু ঘটলে সে শুকরিয়া আদায় করে, ফলে তা কল্যাণকর হয়; আর কষ্টের কিছু ঘটলে সে ধৈর্য ধারণ করে, ফলে তাও তার জন্য কল্যাণকর হয়।' (সহীহ মুসলিম ২৯৯৯)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002155.mp3"
        ),
        SemanticQuranAyah(
            id = "ayah_2_286",
            surahNumber = 2,
            ayahNumber = 286,
            surahNameBn = "সূরা আল-বাক্বারাহ",
            surahNameAr = "سورة البقرة",
            surahNameEn = "Surah Al-Baqarah",
            revelationTypeBn = "মাদানী",
            arabicText = "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ",
            transliterationBn = "লা ইউকাল্লিফুল্লাহু নাফসান ইল্লা উস‘আহা; লাহা মা কাসাবাত ওয়া ‘আলাইহা মাকতাসাবাত; রাব্বানা লা তুআখিযনা ইন নাসীনা আও আখতানা; রাব্বানা ওয়ালা তাহমিল ‘আলাইনা ইসরান কামা হামালতাহূ ‘আলাল্লাযীনা মিন ক্বাবলিনা; রাব্বানা ওয়ালা তুহাম্মিলনা মা লা তায়ক্বাতা লানা বিহ; ওয়া‘ফু ‘আন্না ওয়াগফির লানা ওয়ারহামনা; আনতা মাওলানা ফানসুরনা ‘আলাল ক্বাওমিল কাফিরীন।",
            banglaTranslation = "আল্লাহ কোনো ব্যক্তিকে তার সাধ্যের অতিরিক্ত দায়িত্ব অর্পণ করেন না। সে যা ভালো অর্জন করে তা তার নিজের জন্যই এবং সে যা মন্দ অর্জন করে তাও তার ওপরই বর্তায়। ‘হে আমাদের রব! আমরা যদি ভুলে যাই কিংবা ভুল করি, তবে আমাদেরকে পাকড়াও করবেন না। হে আমাদের রব! আমাদের ওপর এমন ভারী বোঝা চাপিয়ে দেবেন না, যেমন আপনি আমাদের পূর্ববর্তীদের ওপর চাপিয়ে দিয়েছিলেন। হে আমাদের রব! এমন বোঝা আমাদের ওপর চাপাবেন না যা বহন করার ক্ষমতা আমাদের নেই। আপনি আমাদের ক্ষমা করুন, আমাদের গুনাহ মাফ করুন এবং আমাদের প্রতি দয়া করুন। আপনিই আমাদের অভিভাবক।’",
            englishTranslation = "Allah does not charge a soul except [with that within] its capacity. It will have [the consequence of] what [good] it has gained, and it will bear [the consequence of] what [evil] it has earned. 'Our Lord, do not impose blame upon us if we have forgotten or erred. Our Lord, and lay not upon us a burden like that which You laid upon those before us. Our Lord, and burden us not with that which we have no ability to bear. And pardon us; and forgive us; and have mercy upon us. You are our protector, so give us victory over the disbelieving people.'",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: আল্লাহ তায়ালার ন্যায়পরায়ণতা ও পরম করুণার চূড়ান্ত প্রকাশ। কোনো বান্দাকেই তার সামর্থ্যের বাইরে বোঝা দেওয়া হয় না। কোনো কষ্ট যখন আসে, বিশ্বাসীকে বুঝতে হবে যে তা অতিক্রম করার শক্তি আল্লাহ তার মাঝে ইতিমধ্যে দিয়েছেন।",
            divineWisdomBn = "মানুষ যখন তীব্র মানসিক ক্লান্তিতে মনে করে 'আমি আর সহ্য করতে পারছি না', এই আয়াত তাকে স্মরণ করিয়ে দেয় যে আল্লাহ তাকে এই পরীক্ষায় উত্তীর্ণ হওয়ার মতো যথেষ্ট ধৈর্যশীল মনে করেই এই অবস্থায় রেখেছেন।",
            relatedThemes = listOf("সাধ্যের সীমানা", "দোয়া", "দয়া", "আমানত"),
            primaryTopicBn = "সাধ্যের অতীত কোনো বোঝা আল্লাহ চাপান না",
            primaryTopicEn = "Hard times",
            relatedHadithBn = "হযরত আবু মাসউদ (রা.) হতে বর্ণিত, রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি রাতের বেলা সূরা বাকারাহর শেষ দুটি আয়াত পাঠ করবে, তা তার সুরক্ষার জন্য যথেষ্ট হবে।' (সহীহ বুখারী ৫০০৯)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002286.mp3"
        ),

        // -------------------------------------------------------------
        // 8. ANXIETY & INNER PEACE (মানসিক অস্থিরতা ও হৃদয়ের প্রশান্তি)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_13_28",
            surahNumber = 13,
            ayahNumber = 28,
            surahNameBn = "সূরা আর-রা'দ",
            surahNameAr = "سورة الرعد",
            surahNameEn = "Surah Ar-Ra'd",
            revelationTypeBn = "মাদানী",
            arabicText = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            transliterationBn = "আল্লাযীনা আমানূ ওয়া তাত্বমাইন্নু কুলূবুহুম বিযিকরিল্লাহ; আলা বিযিকরিল্লাহি তাত্বমাইন্নুল কুলূব।",
            banglaTranslation = "যারা ঈমান আনে এবং আল্লাহর স্মরণে যাদের অন্তর প্রশান্ত হয়; জেনে রেখো, আল্লাহর স্মরণেই কেবল হৃদয়সমূহ শান্তি পায়।",
            englishTranslation = "Those who have believed and whose hearts are assured by the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.",
            tafsirSummaryBn = "তাফসীরে ইবনে কাসীর: মানব হৃদয় একমাত্র তখনই সত্যিকারের স্থিরতা লাভ করে যখন সে তার সৃষ্টিকর্তার স্মরণে নিয়োজিত থাকে। পার্থিব সম্পদ বা বিনোদন কেবল ক্ষণিকের বিভ্রান্তি দেয়, কিন্তু চিরস্থায়ী প্রশান্তি আসে জিকির ও সালাতে।",
            divineWisdomBn = "অস্থিরতা, উদ্বেগ বা ডিপ্রেশনের মহৌষধ হলো আল্লাহর সান্নিধ্য। হৃদয় যার সৃষ্টি, তাঁর সাথে সংযোগ স্থাপন ছাড়া অন্য কোথাও প্রশান্তি খোঁজা বৃথা।",
            relatedThemes = listOf("জিকির", "হৃদয়ের শান্তি", "মানসিক সুস্থতা", "ঈমান"),
            primaryTopicBn = "আল্লাহর জিকিরে পরম শান্তি",
            primaryTopicEn = "Anxiety and peace of mind",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'যে ব্যক্তি তার রবকে স্মরণ করে এবং যে স্মরণ করে না, তাদের উপমা হলো জীবিত ও মৃতের মতো।' (সহীহ বুখারী ৬৪০৭)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/013028.mp3"
        ),

        // -------------------------------------------------------------
        // 9. GRATITUDE (কৃতজ্ঞতা ও শুকরিয়া)
        // -------------------------------------------------------------
        SemanticQuranAyah(
            id = "ayah_14_7",
            surahNumber = 14,
            ayahNumber = 7,
            surahNameBn = "সূরা ইব্রাহীম",
            surahNameAr = "سورة إبراهيم",
            surahNameEn = "Surah Ibrahim",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِي لَشَدِيدٌ",
            transliterationBn = "ওয়া ইয তাআয্যানা রাব্বুকুম লাইঁন শাকারতুম লাআযীদান্নাকুম; ওয়া লাইঁন কাফারতুম ইন্না ‘আযাবী লাশাদীদ।",
            banglaTranslation = "আর স্মরণ করো, যখন তোমাদের রব ঘোষণা করেছিলেন: ‘তোমরা যদি কৃতজ্ঞতা প্রকাশ করো, তবে আমি অবশ্যই তোমাদের জন্য নেয়ামত বাড়িয়ে দেব; আর যদি অকৃতজ্ঞ হও, তবে নিশ্চয় আমার শাস্তি অত্যন্ত কঠোর।’",
            englishTranslation = "And [remember] when your Lord proclaimed, 'If you are grateful, I will surely increase you [in favor]; but if you deny, indeed, My punishment is severe.'",
            tafsirSummaryBn = "মা'আরিফুল কুরআন: আল্লাহর দেওয়া প্রতিটি ক্ষুদ্র ও বৃহৎ নেয়ামতের কৃতজ্ঞতা স্বীকার করা নেয়ামত বৃদ্ধির ঐশী চাবিকাঠি। মুখে আলহামদুলিল্লাহ বলা এবং অঙ্গপ্রত্যঙ্গ দিয়ে রবের আনুগত্য করাই পূর্ণাঙ্গ শুকরিয়া।",
            divineWisdomBn = "যা পাইনি তা নিয়ে হা-হুতাশ না করে যা পেয়েছি তার জন্য কৃতজ্ঞ হওয়া মানুষের দৃষ্টিভঙ্গি বদলে দেয় এবং অন্তরে অপার সমৃদ্ধি নিয়ে আসে।",
            relatedThemes = listOf("শুকরিয়া", "নেয়ামত বৃদ্ধি", "আলহামদুলিল্লাহ"),
            primaryTopicBn = "কৃতজ্ঞতায় নেয়ামতের ক্রমবৃদ্ধি",
            primaryTopicEn = "Gratitude and thankfulness",
            relatedHadithBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'আল্লাহ তাঁর বান্দার প্রতি অত্যন্ত সন্তুষ্ট হন, যখন সে একটি লোকমা খেয়ে তাঁর প্রশংসা করে কিংবা এক ঢোক পানীয় পান করে তাঁর প্রশংসা করে।' (সহীহ মুসলিম ২৭৩৪)",
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/014007.mp3"
        )
    )
}
