package com.example.data.datasource

import com.example.data.model.DuaSourceCategory
import com.example.data.model.EmotiveFeeling
import com.example.data.model.SituationDuaItem
import com.example.data.model.SpiritualNeed

object DuaBySituationCatalog {

    val feelings: List<EmotiveFeeling> = listOf(
        EmotiveFeeling("anxious", "anxious", "উদ্বিগ্ন / অস্থির", "🌪️", "অশান্ত মন, অতিরিক্ত চিন্তা ও অন্তরের অস্থিরতা"),
        EmotiveFeeling("angry", "angry", "রাগান্বিত / ক্ষুব্ধ", "🔥", "তীব্র ক্রোধ, উত্তেজনা ও ধৈর্যের অভাব"),
        EmotiveFeeling("afraid", "afraid", "ভীত / শঙ্কিত", "🛡️", "কোনো ব্যক্তি, ক্ষতি, অনাগত ভবিষ্যৎ বা বিপদের ভয়"),
        EmotiveFeeling("grateful", "grateful", "কৃতজ্ঞ / তৃপ্ত", "✨", "আল্লাহর নিয়ামত লাভের পর অন্তরের শুকরিয়া ও প্রশান্তি"),
        EmotiveFeeling("lonely", "lonely", "একাকী / নিঃসঙ্গ", "🕊️", "কাউকে পাশে না পাওয়া, বিচ্ছিন্নতা ও শূন্যতাবোধ"),
        EmotiveFeeling("confused", "confused", "বিভ্রান্ত / দ্বিধান্বিত", "🧭", "সঠিক পথ বেছে নেওয়া ও সিদ্ধান্তহীনতার দোলাচল"),
        EmotiveFeeling("hopeless", "hopeless", "হতাশ / নিরাশ", "🌅", "সব পথ বন্ধ মনে হওয়া ও চরম বিষাদগ্রস্ততা"),
        EmotiveFeeling("guilty", "guilty", "অপরাধবোধ / লজ্জিত", "💧", "পাপ ও ভুলের কারণে আত্মগ্লানি ও তাওবাহর ব্যাকুলতা")
    )

    val needs: List<SpiritualNeed> = listOf(
        SpiritualNeed("forgiveness", "forgiveness", "ক্ষমা ও মাগফিরাত", "🤲", "পাপ মোচন ও আল্লাহর রহমতের চাদরে আশ্রয়"),
        SpiritualNeed("guidance", "guidance", "হেদায়েত ও সঠিক পথ", "💡", "জীবনের সঠিক দিকনির্দেশনা ও অটল থাকা"),
        SpiritualNeed("patience", "patience", "সবর ও সহনশীলতা", "⛰️", "কঠিন পরীক্ষায় অটল থাকা ও হৃদয়ের স্থৈর্য"),
        SpiritualNeed("protection", "protection", "হেফাজত ও নিরাপত্তা", "🏰", "শয়তানের চক্রান্ত, বদনজর, শত্রু ও বিপদ থেকে রক্ষা"),
        SpiritualNeed("provision", "provision", "রিজিক ও বরকত", "🌾", "হালাল উপার্জন, ঋণমুক্তি ও প্রাচুর্য"),
        SpiritualNeed("healing", "healing", "শেফা ও আরোগ্য", "🌿", "শারীরিক ও মানসিক রোগব্যাধি থেকে রোগমুক্তি"),
        SpiritualNeed("knowledge", "knowledge", "জ্ঞান ও প্রজ্ঞা", "📖", "উপকারী ইলম, স্মৃতিশক্তি ও বুঝের গভীরতা")
    )

    val allDuas: List<SituationDuaItem> = listOf(

        // ========================================================
        // 1. ANXIOUS (উদ্বিগ্ন / অস্থির) + NEEDS: PATIENCE / PROTECTION / GUIDANCE
        // ========================================================
        SituationDuaItem(
            id = "dua_anxious_hamm",
            category = DuaSourceCategory.HADITH,
            titleBn = "দুশ্চিন্তা, অস্থিরতা ও ঋণের চাপ দূর করার নববী দো'আ",
            arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَأَعُوذُ بِكَ مِنَ الْعَجْزِ وَالْكَسَلِ، وَأَعُوذُ بِكَ مِنَ الْجُبْنِ وَالْبُخْلِ، وَأَعُوذُ بِكَ مِنْ غَلَبَةِ الدَّيْنِ وَقَهْرِ الرِّجَالِ",
            banglaPronunciation = "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিনাল হাম্মি ওয়াল হাযানি, ওয়া আ'ঊযু বিকা মিনাল 'আজযি ওয়াল কাসালি, ওয়া আ'ঊযু বিকা মিনাল জুবনি ওয়াল বুখলি, ওয়া আ'ঊযু বিকা মিন গালাবাতিদ দাইনি ওয়া ক্বাহরির রিজাল।",
            banglaTranslation = "হে আল্লাহ! নিশ্চয়ই আমি আপনার আশ্রয় চাই দুশ্চিন্তা ও শোক থেকে, অক্ষমতা ও অলসতা থেকে, কাপুরুষতা ও কৃপণতা থেকে এবং ঋণের আধিক্য ও মানুষের দমন-পীড়ন থেকে।",
            referenceCitation = "সুনানে আবু দাউদ (১৫৫৫) • মান: সহীহ",
            hadithBookBn = "সুনানে আবু দাউদ",
            hadithNumber = "১৫৫৫",
            gradingBn = "সহীহ",
            narratorCompanionBn = "আবু সাঈদ আল-খুদরী (রা.)",
            contextAndTafsirBn = "রাসুলুল্লাহ ﷺ মসজিদে এক আনসারী সাহাবী আবু উমামা (রা.)-কে অসময়ে বিষণ্ণ বসে থাকতে দেখে কারণ জিজ্ঞাসা করেন। তিনি দুশ্চিন্তা ও ঋণের কথা জানালে নবীজি ﷺ তাঁকে সকাল-সন্ধ্যায় এই দো'আটি পাঠের পরামর্শ দেন। আবু উমামা (রা.) বলেন: এর ফলে আল্লাহ আমার সব ঋণ ও উদ্বেগ দূর করে দেন।",
            sunnahPracticeMethodBn = "প্রতিদিন ফজর ও মাগরিবের পর এবং যেকোনো তীব্র মানসিক অস্থিরতায় একাগ্রচিত্তে পাঠ্য।",
            feelingTags = listOf("anxious", "hopeless", "lonely"),
            needTags = listOf("patience", "protection", "provision"),
            recommendedCount = 1
        ),

        SituationDuaItem(
            id = "dua_anxious_tarfata_ayn",
            category = DuaSourceCategory.HADITH,
            titleBn = "অস্থির মুহূর্তে আল্লাহর ওপর পূর্ণ তাওয়াক্কুলের আকুতি",
            arabicText = "اللَّهُمَّ رَحْمَتَكَ أَرْجُو، فَلَا تَكِلْنِي إِلَى نَفْسِي طَرْفَةَ عَيْنٍ، وَأَصْلِحْ لِي شَأْنِي كُلَّهُ، لَا إِلَهَ إِلَّا أَنْتَ",
            banglaPronunciation = "আল্লাহুম্মা রহমাতাকা আরজু, ফালা তাকিলনী ইলা নাফসী তারফাতা 'আইন, ওয়া আসলিহ লী শা'নী কুল্লাহ, লা ইলাহা ইল্লা আনতা।",
            banglaTranslation = "হে আল্লাহ! আমি কেবল আপনার রহমতেরই আশা করি। অতএব চোখের পলকের তরেও আমাকে আমার নিজের ওপর ছেড়ে দেবেন না। আর আমার যাবতীয় বিষয় সুন্দর করে দিন। আপনি ব্যতীত কোনো সত্য উপাস্য নেই।",
            referenceCitation = "সুনানে আবু দাউদ (৫০৯০), মুসনাদে আহমাদ (২০৪৩০) • মান: হাসান/সহীহ",
            hadithBookBn = "সুনানে আবু দাউদ",
            hadithNumber = "৫০৯০",
            gradingBn = "সহীহ (শায়খ আলবানী)",
            narratorCompanionBn = "আবু বকরাহ (রা.)",
            contextAndTafsirBn = "রাসুলুল্লাহ ﷺ একে 'দু'আউল মাকতুব' বা বিপদগ্রস্ত ও ব্যথাতুর ব্যক্তির বিশেষ উদ্ধারকারী প্রার্থনা হিসেবে উল্লেখ করেছেন। মানুষ যখন নিজের বুদ্ধিতে কুলিয়ে উঠতে পারে না, তখন এক চোখের পলকের জন্যও আল্লাহর কুদরতের আশ্রয় চাওয়াই মুমিনের পরম শক্তি।",
            sunnahPracticeMethodBn = "উদ্বেগে বুক ধড়ফড় করলে বা সিদ্ধান্তহীনতায় বারবার পাঠ করা সুন্নাহ।",
            feelingTags = listOf("anxious", "confused", "afraid"),
            needTags = listOf("guidance", "protection", "patience")
        ),

        SituationDuaItem(
            id = "dua_quran_tatmainnu",
            category = DuaSourceCategory.QURAN,
            titleBn = "কুরআনের ঘোষণা: আল্লাহর স্মরণে অন্তরের পরম প্রশান্তি",
            arabicText = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
            banglaPronunciation = "আল্লাযীনা আমানূ ওয়া তাত্বমাইন্নু ক্বুলূবুহুম বিযিকরিল্লাহ, আলা বিযিকরিল্লাহি তাত্বমাইন্নুল ক্বুলূব।",
            banglaTranslation = "যারা ঈমান আনে এবং আল্লাহর স্মরণে যাদের অন্তর পরম প্রশান্তি লাভ করে; জেনে রাখো, কেবল আল্লাহর স্মরণের মাধ্যমেই অন্তরসমূহ প্রশান্ত হয়।",
            referenceCitation = "আল-কুরআন • সূরা আর-রা'দ (১৩:২৮)",
            surahNameBn = "আর-রা'দ",
            surahNumber = 13,
            ayahNumber = 28,
            contextAndTafsirBn = "তাফসীরে ইবনে কাসীরে বলা হয়েছে: পার্থিব কোনো ধন-সম্পদ বা বিনোদন মানুষের রুহকে চিরস্থায়ী শান্তি দিতে পারে না। যখন বান্দা রবের মহত্ব স্মরণ করে, তখন যাবতীয় অস্থিরতা দূর হয়ে অন্তরে প্রশান্তির শীতলতা নেমে আসে।",
            sunnahPracticeMethodBn = "অস্থিরতার মুহূর্তে আয়াতটি তিলাওয়াত করে বারবার 'সুবহানাল্লাহ, আলহামদুলিল্লাহ, লা ইলাহা ইল্লাল্লাহ' জিকির করা।",
            feelingTags = listOf("anxious", "lonely", "hopeless"),
            needTags = listOf("healing", "patience")
        ),

        // ========================================================
        // 2. ANGRY (রাগান্বিত / ক্ষুব্ধ) + NEEDS: PATIENCE / FORGIVENESS / PROTECTION
        // ========================================================
        SituationDuaItem(
            id = "dua_angry_istiazah",
            category = DuaSourceCategory.HADITH,
            titleBn = "তীব্র রাগ ও ক্ষোভ দমনের সুন্নাতী ইস্তিয়াজাহ",
            arabicText = "أَعُوذُ بِاللَّهِ مِنَ الشَّيْطَانِ الرَّجِيمِ",
            banglaPronunciation = "আ'ঊযু বিল্লাহি মিনাশ শাইত্বানির রাজীম।",
            banglaTranslation = "আমি বিতাড়িত শয়তান থেকে আল্লাহর কাছে আশ্রয় প্রার্থনা করছি।",
            referenceCitation = "সহীহ আল-বুখারী (৩২৮২), সহীহ মুসলিম (২৬১০) • মান: মুত্তাফাকুন আলাইহ",
            hadithBookBn = "সহীহ আল-বুখারী",
            hadithNumber = "৩২৮২",
            gradingBn = "সহীহ",
            narratorCompanionBn = "সুলাইমান ইবনু সুরাদ (রা.)",
            contextAndTafsirBn = "রাসুলুল্লাহ ﷺ-এর সামনে দুই ব্যক্তি বাদানুবাদে লিপ্ত হলো এবং একজনের চেহারা রাগে রক্তিম হয়ে উঠল। তখন নবীজি ﷺ বললেন: 'নিশ্চয়ই আমি এমন একটি বাক্য জানি, যা পাঠ করলে তার এই রাগ তৎক্ষণাৎ দূর হয়ে যাবে; তা হলো—আ'ঊযু বিল্লাহি মিনাশ শাইত্বানির রাজীম।' কারণ ক্রোধ শয়তানের আগুনের প্ররোচনা।",
            sunnahPracticeMethodBn = "রাগ প্রকাশ পাওয়ার সাথে সাথে এটি মুখে পাঠ করা, চুপ থাকা, দাঁড়িয়ে থাকলে বসে পড়া এবং ঠান্ডা পানি দিয়ে উযু করা সুন্নাহ (আবু দাউদ ৪৭৮৪)।",
            feelingTags = listOf("angry"),
            needTags = listOf("patience", "protection"),
            scholarlyNuanceBn = "আলেমদের অভিমত: রাগের মাথায় কোনো চূড়ান্ত সিদ্ধান্ত বা শপথ নেওয়া থেকে বিরত থাকা ওয়াজিব।"
        ),

        SituationDuaItem(
            id = "dua_quran_al_kazimina",
            category = DuaSourceCategory.QURAN,
            titleBn = "রাগ সংবরণকারী ও ক্ষমাকারীদের জন্য কুরআনের প্রশংসা",
            arabicText = "وَالْكَاظِمِينَ الْغَيْظَ وَالْعَافِينَ عَنِ النَّاسِ ۗ وَاللَّهُ يُحِبُّ الْمُحْسِنِينَ",
            banglaPronunciation = "ওয়াল কাযিমীনাল গাইযা ওয়াল 'আফীনা 'ানিন-নাস, ওয়াল্লাহু ইউহিব্বুল মুহসিনীন।",
            banglaTranslation = "...এবং যারা নিজেদের রাগ সংবরণ করে ও মানুষের প্রতি ক্ষমা প্রদর্শন করে; আর আল্লাহ অনুগ্রহকারীদের ভালোবাসেন।",
            referenceCitation = "আল-কুরআন • সূরা আলে ইমরান (৩:১৩৪)",
            surahNameBn = "আলে ইমরান",
            surahNumber = 3,
            ayahNumber = 134,
            contextAndTafsirBn = "মুত্তাকীদের অন্যতম প্রধান গুণ হলো প্রতিকূল আচরণেও প্রতিশোধ না নিয়ে মনের আক্রোশকে হজম করা। নবীজি ﷺ বলেছেন: 'প্রকৃত বীর সে নয় যে কুস্তিতে অন্যকে আছড়ে ফেলে, বরং প্রকৃত বীর সে যে রাগের সময় নিজেকে নিয়ন্ত্রণে রাখতে পারে' (সহীহ বুখারী ৬১১৪)।",
            sunnahPracticeMethodBn = "কারো ওপর ক্ষোভ জাগলে ক্ষমা করে আল্লাহর সন্তুষ্টি লাভের নিয়ত করা।",
            feelingTags = listOf("angry", "guilty"),
            needTags = listOf("patience", "forgiveness")
        ),

        // ========================================================
        // 3. AFRAID (ভীত / শঙ্কিত) + NEEDS: PROTECTION / PATIENCE
        // ========================================================
        SituationDuaItem(
            id = "dua_afraid_khalid",
            category = DuaSourceCategory.HADITH,
            titleBn = "শঙ্কা, দুঃস্বপ্ন ও ভীতিকর পরিস্থিতি থেকে নিরাপত্তার আমল",
            arabicText = "أَعُوذُ بِكَلِمَاتِ اللَّهِ التَّامَّاتِ مِنْ غَضَبِهِ وَعِقَابِهِ وَشَرِّ عِبَادِهِ، وَمِنْ هَمَزَاتِ الشَّيَاطِينِ وَأَنْ يَحْضُرُونِ",
            banglaPronunciation = "আ'ঊযু বিকালিমা-তিল্লাহিত-তাম্মা-তি মিন গাদ্বাবিহী ওয়া 'ইক্বাবিহী ওয়া শাররি 'ইবাদিহী, ওয়া মিন হামাযাতিশ-শায়াত্বীনি ওয়া আঁই ইয়াহদুরূন।",
            banglaTranslation = "আমি আল্লাহর পরিপূর্ণ বাণীসমূহের আশ্রয় নিচ্ছি তাঁর ক্রোধ, তাঁর শাস্তি ও তাঁর বান্দাদের অনিষ্ট থেকে এবং শয়তানদের কুমন্ত্রণা ও আমার কাছে তাদের উপস্থিতি থেকে।",
            referenceCitation = "জামে আত-তিরমিযী (৩৫২৪), সুনানে আবু দাউদ (৩৮৯৩) • মান: হাসান",
            hadithBookBn = "জামে আত-তিরমিযী",
            hadithNumber = "৩৫২৪",
            gradingBn = "হাসান",
            narratorCompanionBn = "আবদুল্লাহ ইবনু আমর (রা.)",
            contextAndTafsirBn = "হযরত খালিদ ইবনুল ওয়ালীদ (রা.) যখন রাতে ভীতিকর দুঃস্বপ্ন ও আতঙ্কে ভুগতেন, তখন রাসুলুল্লাহ ﷺ তাঁকে এই বিশেষ দো'আটি শিক্ষা দেন। সাহাবায়ে কেরাম তাদের সন্তানদেরও এটি মুখস্থ করাতেন।",
            sunnahPracticeMethodBn = "রাতে ঘুমের পূর্বে অথবা যেকোনো দৃশ্য বা অদৃশ্য ভয়ের অনুভূতি হলে পাঠ্য।",
            feelingTags = listOf("afraid", "anxious"),
            needTags = listOf("protection")
        ),

        SituationDuaItem(
            id = "dua_quran_hasbunallah",
            category = DuaSourceCategory.QURAN,
            titleBn = "কুরআনী মহাশক্তি: শত্রু বা বিপদের মুখে পরম আস্থা",
            arabicText = "حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            banglaPronunciation = "হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকীল।",
            banglaTranslation = "আমাদের জন্য আল্লাহই যথেষ্ট এবং তিনি কতই না উত্তম কর্মবিধায়ক!",
            referenceCitation = "আল-কুরআন • সূরা আলে ইমরান (৩:১৭৩)",
            surahNameBn = "আলে ইমরান",
            surahNumber = 3,
            ayahNumber = 173,
            contextAndTafsirBn = "উহুদ যুদ্ধের পর যখন সাহাবীদের ভয় দেখানো হয়েছিল যে শত্রুরা বিশাল সৈন্য নিয়ে পুনরায় আক্রমণ করতে আসছে, তখন তাঁরা ভীত না হয়ে এই ঘোষণা দিয়েছিলেন। ইবনে আব্বাস (রা.) বলেন: ইবরাহীম (আ.)-কে যখন আগুনে নিক্ষেপ করা হয় তখনও তিনি এই বাক্য বলেছিলেন এবং রাসুলুল্লাহ ﷺ-ও এটি বলেছিলেন (সহীহ বুখারী ৪৫৬৩)।",
            sunnahPracticeMethodBn = "ভয় ও আশঙ্কাজনক মুহূর্তে তাওয়াক্কুলের সাথে বেশি বেশি পাঠ করা।",
            feelingTags = listOf("afraid", "hopeless", "anxious"),
            needTags = listOf("protection", "patience")
        ),

        // ========================================================
        // 4. GRATEFUL (কৃতজ্ঞ / তৃপ্ত) + NEEDS: GUIDANCE / KNOWLEDGE / PROVISION
        // ========================================================
        SituationDuaItem(
            id = "dua_quran_sulaiman_shukr",
            category = DuaSourceCategory.QURAN,
            titleBn = "সুলাইমান (আ.)-এর দো'আ: নিয়ামতের শুকরিয়া ও সৎকাজের তাওফিক",
            arabicText = "رَبِّ أَوْزِعْنِي أَنْ أَشْكُرَ نِعْمَتَكَ الَّتِي أَنْعَمْتَ عَلَيَّ وَعَلَىٰ وَالِدَيَّ وَأَنْ أَعْمَلَ صَالِحًا تَرْضَاهُ وَأَدْخِلْنِي بِرَحْمَتِكَ فِي عِبَادِكَ الصَّالِحِينَ",
            banglaPronunciation = "রব্বি আওযি'নী আন আশকুরা নি'মাতাকাল্লাতী আন'আমতা 'আলাইয়্যা ওয়া 'আলা ওয়ালিদাইয়্যা ওয়া আন আ'মালা সালিহান তারদ্বাহু, ওয়া আদখিলনী বিরাহমাতিকা ফী 'ইবাদিকাস সালিহীন।",
            banglaTranslation = "হে আমার রব! আমাকে সামর্থ্য দিন যাতে আমি আপনার সেই নিয়ামতের শুকরিয়া আদায় করতে পারি যা আপনি আমাকে ও আমার পিতা-মাতাকে দান করেছেন, এবং যেন এমন সৎকাজ করতে পারি যাতে আপনি সন্তুষ্ট হন; আর আপনার রহমতে আমাকে আপনার নেককার বান্দাদের অন্তর্ভুক্ত করুন।",
            referenceCitation = "আল-কুরআন • সূরা আন-নামল (২৭:১৯)",
            surahNameBn = "আন-নামল",
            surahNumber = 27,
            ayahNumber = 19,
            contextAndTafsirBn = "সুলাইমান (আ.) যখন পিঁপড়ার কথোপকথন বুঝতে পেরেছিলেন এবং নিজের বিশাল সাম্রাজ্য ও জ্ঞানের নেয়ামত প্রত্যক্ষ করেছিলেন, তখন অহংকার না করে বিনম্রভাবে এই কৃতজ্ঞতার দো'আ করেছিলেন।",
            sunnahPracticeMethodBn = "জীবনের যেকোনো বড় অর্জন, সন্তানের সাফল্য বা সুস্থতায় আল্লাহর শুকরিয়া আদায়ে পাঠ্য।",
            feelingTags = listOf("grateful"),
            needTags = listOf("guidance", "knowledge", "provision")
        ),

        SituationDuaItem(
            id = "dua_hadith_muadh_shukr",
            category = DuaSourceCategory.HADITH,
            titleBn = "মু'আয (রা.)-কে শেখানো সালাত-পরবর্তী শুকরিয়ার দো'আ",
            arabicText = "اللَّهُمَّ أَعِنِّي عَلَى ذِكْرِكَ وَشُكْرِكَ وَحُسْنِ عِبَادَتِكَ",
            banglaPronunciation = "আল্লাহুম্মা আ'ইন্নী 'আলা যিকরিকা ওয়া শুকরিকা ওয়া হুসনি 'ইবাদাতিকা।",
            banglaTranslation = "হে আল্লাহ! আপনার জিকির করতে, আপনার শুকরিয়া আদায় করতে এবং সুন্দরভাবে আপনার ইবাদত করতে আমাকে সাহায্য করুন।",
            referenceCitation = "সুনানে আবু দাউদ (১৫২২), সুনানে নাসাঈ (১৩০৩) • মান: সহীহ",
            hadithBookBn = "সুনানে আবু দাউদ",
            hadithNumber = "১৫২২",
            gradingBn = "সহীহ",
            narratorCompanionBn = "মু'আয ইবনু জাবাল (রা.)",
            contextAndTafsirBn = "রাসুলুল্লাহ ﷺ মু'আয (রা.)-এর হাত ধরে বললেন: 'হে মু'আয! আল্লাহর কসম, আমি তোমাকে ভালোবাসি। অতএব প্রতি সালাতের শেষে এই দো'আটি পড়তে কখনো ভুলে যেও না।' বান্দার কৃতজ্ঞতা প্রকাশের তাওফিকও আল্লাহর রহমতের ওপর নির্ভরশীল।",
            sunnahPracticeMethodBn = "প্রতিটি ফরজ সালাতের সালাম ফিরানোর পর পাঠ করা মুস্তাহাব।",
            feelingTags = listOf("grateful"),
            needTags = listOf("guidance", "knowledge")
        ),

        // ========================================================
        // 5. LONELY (একাকী / নিঃসঙ্গ) + NEEDS: HEALING / GUIDANCE / PATIENCE
        // ========================================================
        SituationDuaItem(
            id = "dua_quran_zakariyya_fardan",
            category = DuaSourceCategory.QURAN,
            titleBn = "যাকারিয়া (আ.)-এর প্রার্থনা: আমাকে একাকী ও নিঃসঙ্গ রাখবেন না",
            arabicText = "رَبِّ لَا تَذَرْنِي فَرْدًا وَأَنتَ خَيْرُ الْوَارِثِينَ",
            banglaPronunciation = "রব্বি লা তাযারনী ফারদাওঁ ওয়া আনতা খাইরুল ওয়ারিসীন।",
            banglaTranslation = "হে আমার রব! আমাকে একা (নিঃসন্তান ও নিঃসঙ্গ) ফেলে রাখবেন না; আর আপনি তো শ্রেষ্ঠতম উত্তরাধিকারী!",
            referenceCitation = "আল-কুরআন • সূরা আল-আম্বিয়া (২১:৮৯)",
            surahNameBn = "আল-আম্বিয়া",
            surahNumber = 21,
            ayahNumber = 89,
            contextAndTafsirBn = "হযরত যাকারিয়া (আ.) যখন বার্ধক্যে পৌঁছালেন এবং একাকীত্ব অনুভব করলেন, তখন তিনি রবের দরবারে অনুচ্চ ও আন্তরিক কণ্ঠে এই আকুতি জানিয়েছিলেন। আল্লাহ তাঁর ডাকে সাড়া দিয়ে ইয়াহইয়া (আ.)-কে দান করেছিলেন।",
            sunnahPracticeMethodBn = "একাকিত্ব, পরিবারহীনতা বা সন্তান কামনায় তাহাজ্জুদে পাঠ্য।",
            feelingTags = listOf("lonely", "hopeless"),
            needTags = listOf("healing", "patience", "provision")
        ),

        SituationDuaItem(
            id = "dua_quran_la_tahzan",
            category = DuaSourceCategory.QURAN,
            titleBn = "কুরআনের চিরন্তন সান্ত্বনা: নিশ্চয়ই আল্লাহ আমাদের সাথে আছেন",
            arabicText = "لَا تَحْزَنْ إِنَّ اللَّهَ مَعَنَا",
            banglaPronunciation = "লা তাহযান, ইন্নাল্লাহা মা'আনা।",
            banglaTranslation = "ভয় পেয়ো না বা বিষণ্ণ হয়ো না, নিশ্চয়ই আল্লাহ আমাদের সাথে আছেন।",
            referenceCitation = "আল-কুরআন • সূরা আত-তাওবাহ (৯:৪০)",
            surahNameBn = "আত-তাওবাহ",
            surahNumber = 9,
            ayahNumber = 40,
            contextAndTafsirBn = "হিজরতের সময় সওর পর্বতের অন্ধকার গুহায় যখন কুরাইশরা গুহামুখ পর্যন্ত পৌঁছে গিয়েছিল, তখন হযরত আবু বকর (রা.) শঙ্কিত হলে রাসুলুল্লাহ ﷺ পরম প্রশান্তিতে এই অবিচল বাণী শুনিয়েছিলেন। একজন মুমিন কখনো একা নয়; রবের সান্নিধ্য সর্বদা তাঁর সাথে থাকে।",
            sunnahPracticeMethodBn = "নিঃসঙ্গ ও অসহায় মুহূর্তে অন্তরে আল্লাহর সান্নিধ্য স্মরণ করার আয়না।",
            feelingTags = listOf("lonely", "afraid", "anxious"),
            needTags = listOf("protection", "patience")
        ),

        // ========================================================
        // 6. CONFUSED (বিভ্রান্ত / দ্বিধান্বিত) + NEEDS: GUIDANCE / KNOWLEDGE
        // ========================================================
        SituationDuaItem(
            id = "dua_hadith_istikhara",
            category = DuaSourceCategory.HADITH,
            titleBn = "ইস্তিখারা: দ্বিধা ও সিদ্ধান্তহীনতায় রবের ঐশী দিকনির্দেশনা",
            arabicText = "اللَّهُمَّ إِنِّي أَسْتَخِيرُكَ بِعِلْمِكَ، وَأَسْتَقْدِرُكَ بِقُدْرَتِكَ، وَأَسْأَلُكَ مِنْ فَضْلِكَ الْعَظِيمِ، فَإِنَّكَ تَقْدِرُ وَلاَ أَقْدِرُ، وَتَعْلَمُ وَلاَ أَعْلَمُ، وَأَنْتَ عَلاَّمُ الْغُيُوبِ",
            banglaPronunciation = "আল্লাহুম্মা ইন্নী আসতাখীরুকা বি'ইলমিকা, ওয়া আসতাক্বদিরুকা বিক্বুদরাতিকা, ওয়া আসআলুকা মিন ফাদ্বলিকাল 'আযীম, ফাইন্নাকা তাক্বদিরু ওয়ালা আক্বদিরু, ওয়া তা'লামু ওয়ালা আ'লামু, ওয়া আনতা 'আল্লামুল গুয়ূব।",
            banglaTranslation = "হে আল্লাহ! আমি আপনার জ্ঞানের মাধ্যমে আপনার কাছে কল্যাণের ফয়সালা চাচ্ছি, আপনার কুদরতের মাধ্যমে শক্তি চাচ্ছি এবং আপনার মহা অনুগ্রহ প্রার্থনা করছি। কেননা আপনিই ক্ষমতা রাখেন, আমার কোনো ক্ষমতা নেই; আপনি সবকিছু জানেন, আমি কিছুই জানি না; আর আপনিই সমস্ত অদৃশ্যের মহা জ্ঞানী।",
            referenceCitation = "সহীহ আল-বুখারী (১১৬২) • মান: সহীহ",
            hadithBookBn = "সহীহ আল-বুখারী",
            hadithNumber = "১১৬২",
            gradingBn = "সহীহ",
            narratorCompanionBn = "জাবির ইবনু আবদিল্লাহ (রা.)",
            contextAndTafsirBn = "জাবির (রা.) বলেন: রাসুলুল্লাহ ﷺ কুরআনের সূরা শেখানোর মতোই গুরুত্ব দিয়ে আমাদের জীবনের প্রতিটি সিদ্ধান্তে ইস্তিখারার সালাত ও দো'আ শেখাতেন। বিবাহ, চাকরি, পড়াশোনা বা যেকোনো দ্বিধাদ্বন্দ্বে নিজের মনগড়া অনুমানের ওপর ভরসা না করে সর্বজ্ঞ আল্লাহর কাছে দিকনির্দেশনা চাওয়াই এর শিক্ষা।",
            sunnahPracticeMethodBn = "দুই রাকাত নফল সালাত আদায় করে সালাম ফিরিয়ে পূর্ণ ইস্তিখারার দো'আ পাঠ করা সুন্নাহ।",
            feelingTags = listOf("confused", "anxious"),
            needTags = listOf("guidance", "knowledge"),
            scholarlyNuanceBn = "ইস্তিখারার পর স্বপ্ন দেখা জরুরি নয়; বরং যে কাজটি সহজ হয়ে যায় এবং অন্তরে স্বস্তি আসে, সেদিকে অগ্রসর হওয়াই সুন্নাহ।"
        ),

        SituationDuaItem(
            id = "dua_quran_ashabul_kahf",
            category = DuaSourceCategory.QURAN,
            titleBn = "আসহাবে কাহাফের দো'আ: সঠিক সিদ্ধান্ত ও রহমতের প্রার্থনা",
            arabicText = "رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا",
            banglaPronunciation = "রব্বানা আতিনা মিল্লাদুংকা রহমাতাওঁ ওয়া হাইয়্যি' লানা মিন আমরিনা রশাদা।",
            banglaTranslation = "হে আমাদের রব! আপনার পক্ষ থেকে আমাদের বিশেষ অনুগ্রহ দান করুন এবং আমাদের জন্য আমাদের কাজের সঠিক সমাধান ও দিকনির্দেশনা সহজ করে দিন।",
            referenceCitation = "আল-কুরআন • সূরা আল-কাহাফ (১৮:১০)",
            surahNameBn = "আল-কাহাফ",
            surahNumber = 18,
            ayahNumber = 10,
            contextAndTafsirBn = "কাহাফের একদল ঈমানদার যুবক যখন জালেম বাদশাহর ফেতনা থেকে বাঁচতে গুহায় আশ্রয় নিয়েছিল এবং তাদের সামনে ভবিষ্যৎ সম্পূর্ণ অন্ধকার ছিল, তখন তারা আল্লাহর কাছে এই ভারসাম্যপূর্ণ দো'আ করেছিলেন। আল্লাহ তাদের অলৌকিকভাবে সুরক্ষা দিয়েছিলেন।",
            sunnahPracticeMethodBn = "জটিল পরিস্থিতিতে সঠিক পথ খোঁজার জন্য নিয়মিত তিলাওয়াতযোগ্য।",
            feelingTags = listOf("confused", "afraid"),
            needTags = listOf("guidance", "protection")
        ),

        // ========================================================
        // 7. HOPELESS (হতাশ / নিরাশ) + NEEDS: FORGIVENESS / PROVISION / HEALING
        // ========================================================
        SituationDuaItem(
            id = "dua_quran_yunus",
            category = DuaSourceCategory.QURAN,
            titleBn = "ইউনুস (আ.)-এর দো'আয়ে ইউনুস: চরম নিরাশা ও সংকট থেকে মুক্তির অমিয় বাণী",
            arabicText = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
            banglaPronunciation = "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নী কুংতু মিনায-যালিমীন।",
            banglaTranslation = "আপনি ব্যতীত কোনো সত্য উপাস্য নেই, আপনি পরম পবিত্র! নিশ্চয়ই আমি অপরাধীদের অন্তর্ভুক্ত হয়ে গিয়েছি।",
            referenceCitation = "আল-কুরআন • সূরা আল-আম্বিয়া (২১:৮৭)",
            surahNameBn = "আল-আম্বিয়া",
            surahNumber = 21,
            ayahNumber = 87,
            contextAndTafsirBn = "ইউনুস (আ.) মাছের পেটের নিরেট অন্ধকারে যখন কূলকিনারাহীন অবস্থায় ছিলেন, তখন তিনি তাওহীদের সাক্ষ্য ও নিজের অপরাধ স্বীকার করে এই দো'আ করেন। রাসুলুল্লাহ ﷺ বলেছেন: 'যেকোনো মুসলিম বিপদে পতিত হয়ে এই দো'আ দ্বারা আল্লাহর কাছে প্রার্থনা করবে, আল্লাহ অবশ্যই তার প্রার্থনা কবুল করবেন' (জামে তিরমিযী ৩৫০৫ - সহীহ)।",
            sunnahPracticeMethodBn = "যেকোনো গভীর হতাশা, কান্না ও সংকটের মুহূর্তে বেশি বেশি পাঠ্য।",
            feelingTags = listOf("hopeless", "guilty", "anxious"),
            needTags = listOf("forgiveness", "healing", "patience")
        ),

        SituationDuaItem(
            id = "dua_quran_la_tayasu",
            category = DuaSourceCategory.QURAN,
            titleBn = "কুরআনের নির্দেশ: আল্লাহর রহমত থেকে কখনো নিরাশ হয়ো না",
            arabicText = "وَلَا تَيْأَسُوا مِن رَّوْحِ اللَّهِ ۖ إِنَّهُ لَا يَيْأَسُ مِن رَّوْحِ اللَّهِ إِلَّا الْقَوْمُ الْكَافِرُونَ",
            banglaPronunciation = "ওয়া লা তায়'আসূ মির-রাওহিল্লাহ, ইন্নাহূ লা ইয়ায়'আসু মির-রাওহিল্লাহি ইল্লাল ক্বাওমুল কাফিরূন।",
            banglaTranslation = "আর তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না; নিশ্চয়ই কাফের সম্প্রদায় ব্যতীত কেউই আল্লাহর রহমত থেকে নিরাশ হয় না।",
            referenceCitation = "আল-কুরআন • সূরা ইউসুফ (১২:৮৭)",
            surahNameBn = "ইউসুফ",
            surahNumber = 12,
            ayahNumber = 87,
            contextAndTafsirBn = "হযরত ইয়াকুব (আ.) ইউসুফ (আ.)-কে বহু বছর হারিয়ে অন্ধ হয়ে যাওয়ার পরও আশা হারাননি। তিনি তাঁর ছেলেদের বলেছিলেন রবের রহমত অপরিসীম। ইসলামে জীবনের যেকোনো কঠিনতম বিপর্যয়েও নিরাশ হওয়া নিষিদ্ধ।",
            sunnahPracticeMethodBn = "হতাশা দূর করতে মনকে কুরআনের আলোয় আলোকিত করতে তিলাওয়াতযোগ্য।",
            feelingTags = listOf("hopeless"),
            needTags = listOf("patience", "healing")
        ),

        // ========================================================
        // 8. GUILTY (অপরাধবোধ / অনুতপ্ত) + NEEDS: FORGIVENESS / GUIDANCE
        // ========================================================
        SituationDuaItem(
            id = "dua_hadith_sayyidul_istighfar",
            category = DuaSourceCategory.HADITH,
            titleBn = "সাইয়্যিদুল ইস্তিগফার: পাপের অনুশোচনা ও ক্ষমার শ্রেষ্ঠতম দু'আ",
            arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي، فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
            banglaPronunciation = "আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা, খালাক্বতানী ওয়া আনা 'আব্দুকা, ওয়া আনা 'আলা 'আহদিকা ওয়া ওয়া'দিকা মাসতাত্বা'তু... ফাগফিরলী ফাইন্নাহূ লা ইয়াগফিরুয যুনূবা ইল্লা আনতা।",
            banglaTranslation = "হে আল্লাহ! আপনিই আমার প্রতিপালক, আপনি ছাড়া কোনো উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি সাধ্যমতো আপনার অঙ্গীকার ও প্রতিশ্রুতির ওপর কায়েম আছি... আমি আমার পাপ স্বীকার করছি, অতএব আমাকে ক্ষমা করে দিন; নিশ্চয়ই আপনি ছাড়া পাপ ক্ষমা করার কেউ নেই।",
            referenceCitation = "সহীহ আল-বুখারী (৬৩০৬) • মান: সহীহ (সর্বশ্রেষ্ঠ ইস্তিগফার)",
            hadithBookBn = "সহীহ আল-বুখারী",
            hadithNumber = "৬৩০৬",
            gradingBn = "সহীহ",
            narratorCompanionBn = "শাদ্দাদ ইবনু আওস (রা.)",
            contextAndTafsirBn = "রাসুলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি দৃঢ় বিশ্বাসের সাথে দিনে এটি পাঠ করবে এবং সন্ধ্যার আগে মারা যাবে, সে জান্নাতী হবে। আর যে রাতে পাঠ করবে এবং সকালের আগে মারা যাবে, সেও জান্নাতী হবে। এটি বান্দার দাসত্ব, নিয়ামতের স্বীকৃতি এবং পাপের নিঃশর্ত স্বীকৃতির এক অনন্য সংমিশ্রণ।",
            sunnahPracticeMethodBn = "সকাল ও সন্ধ্যায় নিয়মিত একবার পাঠ করা সুন্নাহ।",
            feelingTags = listOf("guilty", "hopeless"),
            needTags = listOf("forgiveness"),
            recommendedCount = 1
        ),

        SituationDuaItem(
            id = "dua_quran_adam_tawbah",
            category = DuaSourceCategory.QURAN,
            titleBn = "আদম (আ.) ও হাওয়া (আ.)-এর ঐতিহাসিক তাওবাহর দো'আ",
            arabicText = "رَبَّنَا ظَلَمْنَا أَنفُسَنَا وَإِن لَّمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ",
            banglaPronunciation = "রব্বানা যালামনা আনফুসানা ওয়া ইল্লাম তাগফিরলানা ওয়া তারহামনা লানাকূনান্না মিনাল খাসিরীন।",
            banglaTranslation = "হে আমাদের রব! আমরা নিজেদের প্রতি জুলুম করেছি। এখন আপনি যদি আমাদের ক্ষমা না করেন এবং দয়া না করেন, তবে অবশ্যই আমরা ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হয়ে যাব।",
            referenceCitation = "আল-কুরআন • সূরা আল-আ'রাফ (৭:২৩)",
            surahNameBn = "আল-আ'রাফ",
            surahNumber = 7,
            ayahNumber = 23,
            contextAndTafsirBn = "ভুলবশত নিষিদ্ধ ফল খাওয়ার পর আদম (আ.) আল্লাহর শেখানো বাণী দ্বারা তাওবাহ করেছিলেন (সূরা বাকারা ২:৩৭)। ইবলিস অহংকার করে বিভ্রান্ত হয়েছিল, আর আদম (আ.) বিনম্র অনুশোচনা করে আল্লাহর ক্ষমা ও নৈকট্য লাভ করেছিলেন।",
            sunnahPracticeMethodBn = "পাপ সংঘটিত হওয়ার পরপরই সালাতুত তাওবাহ পড়ে সিজদায় পাঠ করা।",
            feelingTags = listOf("guilty"),
            needTags = listOf("forgiveness")
        ),

        // ========================================================
        // 9. NEEDS: HEALING (শেফা ও আরোগ্য)
        // ========================================================
        SituationDuaItem(
            id = "dua_hadith_ruqyah_shifa",
            category = DuaSourceCategory.HADITH,
            titleBn = "রোগমুক্তি ও শারীরিক কষ্টের মাসনূন শেফার দু'আ",
            arabicText = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لاَ شِفَاءَ إِلاَّ شِفَاؤُكَ، شِفَاءً لاَ يُغَادِرُ سَقَمًا",
            banglaPronunciation = "আল্লাহুম্মা রব্বান-নাসি আযহিবিল বা'স, ইশফিহি ওয়া আনতাশ-শাফী, লা শিফা-আ ইল্লা শিফা-উকা, শিফা-আন লা ইয়ুগাদিরু সাক্বমা।",
            banglaTranslation = "হে আল্লাহ! মানুষের রব! আপনি কষ্ট দূর করে দিন, আরোগ্য দান করুন; আপনিই প্রকৃত আরোগ্যদানকারী। আপনার শেফা ব্যতীত অন্য কোনো শেফা নেই—এমন শেফা দিন যা কোনো রোগ অবশিষ্ট রাখে না।",
            referenceCitation = "সহীহ আল-বুখারী (৫৭৪৩), সহীহ মুসলিম (২১৯১) • মান: মুত্তাফাকুন আলাইহ",
            hadithBookBn = "সহীহ আল-বুখারী",
            hadithNumber = "৫৭৪৩",
            gradingBn = "সহীহ",
            narratorCompanionBn = "আয়েশা (রা.)",
            contextAndTafsirBn = "রাসুলুল্লাহ ﷺ পরিবারের কেউ বা কোনো সাহাবী অসুস্থ হলে তাঁর ব্যথাস্থানে ডান হাত রেখে এই দো'আটি পড়ে ফুঁ দিতেন। আরোগ্য কেবলই আল্লাহর হাতে—চিকিৎসক ও ঔষধ কেবল বাহ্যিক উপায় মাত্র।",
            sunnahPracticeMethodBn = "রোগাক্রান্ত স্থানে ডান হাত রেখে বিনম্রভাবে পাঠ করা সুন্নাত।",
            feelingTags = listOf("anxious", "hopeless", "afraid"),
            needTags = listOf("healing")
        ),

        SituationDuaItem(
            id = "dua_quran_ayub_shifa",
            category = DuaSourceCategory.QURAN,
            titleBn = "আইয়ুব (আ.)-এর দো'আ: চরম রোগব্যাধি ও শারীরিক কষ্টের আর্তনাদ",
            arabicText = "أَنِّي مَسَّنِيَ الضُّرُّ وَأَنتَ أَرْحَمُ الرَّاحِمِينَ",
            banglaPronunciation = "আন্নী মাস্‌সানিয়াদ্‌ দুররু ওয়া আনতা আরহামুর রহিমীন।",
            banglaTranslation = "নিশ্চয়ই দুঃখ-কষ্ট ও ব্যাধি আমাকে স্পর্শ করেছে, আর আপনি তো সর্বশ্রেষ্ঠ দয়ালু!",
            referenceCitation = "আল-কুরআন • সূরা আল-আম্বিয়া (২১:৮৩)",
            surahNameBn = "আল-আম্বিয়া",
            surahNumber = 21,
            ayahNumber = 83,
            contextAndTafsirBn = "হযরত আইয়ুব (আ.) বহু বছর কঠিন রোগভোগ ও সহায়-সম্বলহীনতার পরও রবের প্রতি অভিযোগ করেননি। তিনি কেবল নিজের অসহায়ত্ব ও আল্লাহর দয়ার কথা উল্লেখ করে এই দো'আ করেছিলেন। তৎক্ষণাৎ আল্লাহ তাঁকে পূর্ণ সুস্থতা দান করেন।",
            sunnahPracticeMethodBn = "দীর্ঘস্থায়ী শারীরিক ব্যাধি বা অপারেশনের পূর্বে অশ্রুসজল হয়ে পাঠ্য।",
            feelingTags = listOf("hopeless", "lonely"),
            needTags = listOf("healing", "patience")
        ),

        // ========================================================
        // 10. NEEDS: PROVISION (রিজিক ও প্রাচুর্য)
        // ========================================================
        SituationDuaItem(
            id = "dua_hadith_rizq_fajr",
            category = DuaSourceCategory.HADITH,
            titleBn = "ফজরের পর উপকারী জ্ঞান, পবিত্র রিজিক ও আমল কবুলের দু'আ",
            arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلاً مُتَقَبَّلاً",
            banglaPronunciation = "আল্লাহুম্মা ইন্নী আসআলুকা 'ইলমান নাফি'আ, ওয়া রিযক্বান ত্বায়্যিবা, ওয়া 'আমালান মুতাক্বাব্বালা।",
            banglaTranslation = "হে আল্লাহ! নিশ্চয়ই আমি আপনার কাছে উপকারী জ্ঞান, পবিত্র ও হালাল রিজিক এবং গ্রহণযোগ্য আমল প্রার্থনা করছি।",
            referenceCitation = "সুনানে ইবনে মাজাহ (৯২৫), মুসনাদে আহমাদ (২৬৫২১) • মান: সহীহ",
            hadithBookBn = "সুনানে ইবনে মাজাহ",
            hadithNumber = "৯২৫",
            gradingBn = "সহীহ",
            narratorCompanionBn = "উম্মে সালামাহ (রা.)",
            contextAndTafsirBn = "উম্মে সালামাহ (রা.) বর্ণনা করেন, রাসুলুল্লাহ ﷺ প্রতিদিন ফজরের সালাতের সালাম ফিরানোর পরপরই এই তিনটি বিশেষ নিয়ামতের জন্য প্রার্থনা করতেন। দিনের শুরুতেই হালাল জীবিকা ও আমলের বরকত নিশ্চিত করার এটি এক আদর্শ পাঠ।",
            sunnahPracticeMethodBn = "প্রতিদিন ফজরের সালাতের সালাম ফিরানোর পর একবার পাঠ্য।",
            feelingTags = listOf("anxious", "grateful"),
            needTags = listOf("provision", "knowledge"),
            recommendedCount = 1
        ),

        SituationDuaItem(
            id = "dua_hadith_debt_halal",
            category = DuaSourceCategory.HADITH,
            titleBn = "পাহাড় সমান ঋণ ও অভাব থেকে মুক্তির বিশেষ দু'আ",
            arabicText = "اللَّهُمَّ اكْفِنِي بِحَلاَلِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
            banglaPronunciation = "আল্লাহুম্মাক ফিনী বিহালালিকা 'আন হারামিক, ওয়া আগনিনী বিফাদলিকা 'আম্মান সিওয়াক।",
            banglaTranslation = "হে আল্লাহ! আপনার হালালের মাধ্যমে আমাকে হারাম থেকে বাঁচিয়ে পরিতৃপ্ত রাখুন এবং আপনার দয়া ও অনুগ্রহের মাধ্যমে আপনি ছাড়া অন্য সবার থেকে আমাকে অমুখাপেক্ষী করে দিন।",
            referenceCitation = "জামে আত-তিরমিযী (৩৫৬৩) • মান: হাসান",
            hadithBookBn = "জামে আত-তিরমিযী",
            hadithNumber = "৩৫৬৩",
            gradingBn = "হাসান",
            narratorCompanionBn = "আলী ইবনু আবি তালিব (রা.)",
            contextAndTafsirBn = "এক ব্যক্তি এসে ঋণ পরিশোধের অক্ষমতার কথা জানালে আলী (রা.) বললেন: 'রাসুলুল্লাহ ﷺ আমাকে যে দোয়াটি শিখিয়েছিলেন তা কি তোমাকে শিখিয়ে দেব না? সীর পাহাড় পরিমাণ ঋণ থাকলেও আল্লাহ তোমার পক্ষ থেকে তা পরিশোধের ব্যবস্থা করে দেবেন।' (প্রতি নামাজের পর পাঠ্য)",
            sunnahPracticeMethodBn = "সালাতের পর এবং জুমার দিনে অধিক পরিমাণে পাঠ করা সুন্নাহ।",
            feelingTags = listOf("anxious", "hopeless"),
            needTags = listOf("provision")
        ),

        // ========================================================
        // 11. NEEDS: KNOWLEDGE (জ্ঞান ও প্রজ্ঞা)
        // ========================================================
        SituationDuaItem(
            id = "dua_quran_rabbi_zidni",
            category = DuaSourceCategory.QURAN,
            titleBn = "কুরআনের শ্রেষ্ঠ প্রার্থনা: হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন",
            arabicText = "رَّبِّ زِدْنِي عِلْمًا",
            banglaPronunciation = "রব্বি যিদনী 'ইলমা।",
            banglaTranslation = "হে আমার রব! আমার জ্ঞান বাড়িয়ে দিন।",
            referenceCitation = "আল-কুরআন • সূরা ত্বা-হা (২০:১১৪)",
            surahNameBn = "ত্বা-হা",
            surahNumber = 20,
            ayahNumber = 114,
            contextAndTafsirBn = "কুরআনে আল্লাহ তা'আলা তাঁর নবী ﷺ-কে জ্ঞান ছাড়া অন্য কোনো পার্থিব জিনিস বাড়ানোর দো'আ করার নির্দেশ দেননি। কারণ উপকারী জ্ঞানই মানুষের দুনিয়া ও আখেরাতের মর্যাদা বৃদ্ধি করে।",
            sunnahPracticeMethodBn = "পড়াশোনা, তিলাওয়াত, গবেষণা বা পরীক্ষার শুরুতে পাঠ্য।",
            feelingTags = listOf("confused", "grateful"),
            needTags = listOf("knowledge", "guidance")
        ),

        SituationDuaItem(
            id = "dua_quran_musa_sharah",
            category = DuaSourceCategory.QURAN,
            titleBn = "মুসা (আ.)-এর দো'আ: বক্ষ প্রশস্তকরণ ও বাকশক্তি স্পষ্ট করার প্রার্থনা",
            arabicText = "رَبِّ اشْرَحْ لِي صَدْرِي ۝ وَيَسِّرْ لِي أَمْرِي ۝ وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي ۝ يَفْقَهُوا قَوْلِي",
            banglaPronunciation = "রব্বিশ রাহ্‌লী সদরী, ওয়া ইয়াসসিরলী আমরী, ওয়াহলুল 'উক্বদাতাম মিল-লিসানী, ইয়াফক্বাহূ ক্বওলী।",
            banglaTranslation = "হে আমার রব! আমার বক্ষ প্রশস্ত করে দিন, আমার কাজ সহজ করে দিন এবং আমার জিহ্বার জড়তা দূর করে দিন যাতে তারা আমার কথা বুঝতে পারে।",
            referenceCitation = "আল-কুরআন • সূরা ত্বা-হা (২০:২৫-২৮)",
            surahNameBn = "ত্বা-হা",
            surahNumber = 20,
            ayahNumber = 25,
            contextAndTafsirBn = "ফেরাউনের সামনে যাওয়ার পূর্বে মুসা (আ.) ভয় ও মানসিক চাপ কাটিয়ে আত্মবিশ্বাস ও প্রাঞ্জল বক্তব্য প্রদানের জন্য আল্লাহর কাছে প্রার্থনা করেছিলেন। বক্তব্য, উপস্থাপনা বা পরীক্ষার ইন্টারভিউয়ের আগে এটি অতীব ফলপ্রসূ।",
            sunnahPracticeMethodBn = "ইন্টারভিউ, সভা, প্রেজেন্টেশন বা পরীক্ষার পূর্বে একাগ্রমনে পাঠ্য।",
            feelingTags = listOf("anxious", "afraid", "confused"),
            needTags = listOf("knowledge", "guidance", "patience")
        )
    )

    /**
     * Filters and matches supplications based on emotional state, spiritual need, or custom text.
     */
    fun filterDuas(
        feelingId: String?,
        needId: String?,
        customFeeling: String = "",
        customNeed: String = ""
    ): List<SituationDuaItem> {
        val fQuery = (feelingId ?: customFeeling).lowercase().trim()
        val nQuery = (needId ?: customNeed).lowercase().trim()

        if (fQuery.isBlank() && nQuery.isBlank()) {
            return allDuas
        }

        val filtered = allDuas.filter { item ->
            val matchFeeling = if (fQuery.isBlank()) true else {
                item.feelingTags.any { tag -> tag.contains(fQuery) || fQuery.contains(tag) } ||
                item.titleBn.lowercase().contains(fQuery) ||
                item.banglaTranslation.lowercase().contains(fQuery) ||
                item.contextAndTafsirBn.lowercase().contains(fQuery)
            }

            val matchNeed = if (nQuery.isBlank()) true else {
                item.needTags.any { tag -> tag.contains(nQuery) || nQuery.contains(tag) } ||
                item.titleBn.lowercase().contains(nQuery) ||
                item.banglaTranslation.lowercase().contains(nQuery) ||
                item.contextAndTafsirBn.lowercase().contains(nQuery)
            }

            matchFeeling && matchNeed
        }

        // If strict intersection yields nothing, fall back to matching either feeling or need
        if (filtered.isEmpty()) {
            return allDuas.filter { item ->
                (fQuery.isNotBlank() && item.feelingTags.any { it.contains(fQuery) || fQuery.contains(it) }) ||
                (nQuery.isNotBlank() && item.needTags.any { it.contains(nQuery) || nQuery.contains(it) })
            }.ifEmpty { allDuas.take(4) }
        }

        return filtered
    }
}
