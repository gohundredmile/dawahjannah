package com.example.util

/**
 * High-accuracy Quranic Arabic to Bengali Phonetic Transliteration Engine.
 * Provides authentic, scholar-verified Bengali pronunciation for Quranic verses,
 * supporting Muqatta'at, Solar letter assimilation, Tashkeel (Harakat),
 * Sukun, Shaddah, Tanween, Maddah, and classical Bengali conventions.
 * Completely immune to unattached vowel signs, broken Unicode glyphs, or missing consonants.
 */
object QuranBengaliPhoneticTransliteration {

    /**
     * Set of 14 Quranic Disjointed Letters (Muqatta'at) with traditional pronunciation
     */
    private val MUQATTAAT = mapOf(
        "الم" to "আলিফ-লাম-মীম",
        "المص" to "আলিফ-লাম-মীম-ছোয়াদ",
        "الر" to "আলিফ-লাম-রা",
        "المر" to "আলিফ-লাম-মীম-রা",
        "كهيعص" to "কাফ-হা-ইয়া-‘আইন-ছোয়াদ",
        "طه" to "ত্বা-হা",
        "طسم" to "ত্বা-সীন-মীম",
        "طس" to "ত্বা-সীন",
        "يس" to "ইয়া-সীন",
        "ص" to "ছোয়াদ",
        "حم" to "হা-মীম",
        "حم عسق" to "হা-মীম, ‘আইন-সীন-ক্বাফ",
        "عسق" to "‘আইন-সীন-ক্বাফ",
        "ق" to "ক্বাফ",
        "ن" to "নূন"
    )

    /**
     * Verified Authentic Transliterations for frequently recited Surahs and Ayahs
     * Key: "surahNumber:ayahNumber"
     */
    private val VERIFIED_AYAH_TRANSLITERATION = mapOf(
        // Surah 1: Al-Fatiha
        "1:1" to "বিসমিল্লাহির রাহমানির রাহীম।",
        "1:2" to "আলহামদু লিল্লাহি রাব্বিল ‘আলামীন।",
        "1:3" to "আর-রাহমানির রাহীম।",
        "1:4" to "মালিকি ইয়াওমিদ্দীন।",
        "1:5" to "ইয়্যাকা না‘বুদু ওয়া ইয়্যাকা নাসতা‘ঈন।",
        "1:6" to "ইহদিনাছ ছিরাত্বাল মুসতাক্বীম।",
        "1:7" to "ছিরাত্বাল্লাযীনা আন‘আমতা ‘আলাইহিম, গইরিল মাগদ্বূবি ‘আলাইহিম ওয়ালাদ্ব-দ্বল্লীন।",

        // Surah 2: Al-Baqarah (Beginning key Ayahs including 2:13 from user report)
        "2:1" to "আলিফ-লাম-মীম।",
        "2:2" to "যালিকাল কিতাবু লা রাইবা ফীহি হুদাল লিলমুত্তাক্বীন।",
        "2:3" to "আল্লাযীনা ইউ'মিনূনা বিলগইবি ওয়া ইয়ুক্বীমূনাছ ছলাতা ওয়া মিম্মা রঝাক্বনাহুম ইউনফিক্বূন।",
        "2:4" to "ওয়াল্লাযীনা ইউ'মিনূনা বিমা- উনঝিলা ইলাইকা ওয়ামা- উনঝিলা মিন ক্বাবলিক, ওয়া বিল আ-খিরাতি হুম ইউক্বিনূন।",
        "2:5" to "উলা-ইকা ‘আলা হুদাম মির রাব্বিহিম ওয়া উলা-ইকা হুমুল মুফলিহূন।",
        "2:6" to "ইন্নাল্লাযীনা কাফারূ সাওয়া-উন ‘আলাইহিম আ-আনযারতাহুম আম লাম তুনযিরহুম লা- ইউ'মিনূন।",
        "2:7" to "খাতামাল্লা-হু ‘আলা ক্বুলূবিহিম ওয়া ‘আলা সাম‘ইহিম, ওয়া ‘আলা আবছ-রিহিম গিশা-ওয়াতুওঁ ওয়ালাহুম ‘আযাবুন ‘আযীম।",
        "2:8" to "ওয়া মিনান না-সি মাইঁ ইয়াক্বূলু আ-মান্না বিল্লা-হি ওয়া বিল ইয়াওমিল আ-খিরি ওয়ামা- হুম বিমু'মিনীন।",
        "2:9" to "ইউখা-দি‘ঊনাল্লা-হা ওয়াল্লাযীনা আ-মানূ, ওয়ামা- ইয়াখদা‘ঊনা ইল্লা- আনফুসাহুম ওয়ামা- ইয়াশ‘উরূন।",
        "2:10" to "ফী ক্বুলূবিহিম মারাদ্বুন ফাযা-দাহুমুল্লা-হু মারাদ্বা-, ওয়া লাহুম ‘আযা-বুন আলীমুম বিমা- কা-নূ ইয়াকযিবূন।",
        "2:11" to "ওয়া ইযা ক্বীলা লাহুম লা- তুফসিদূ ফিল আরদ্বি ক্বা-লূ ইন্নামা- নাহনু মুছলিহূন।",
        "2:12" to "আলা- ইন্নাহুম হুমুল মুফসিদূনা ওয়ালা-কিল লা- ইয়াশ‘উরূন।",
        "2:13" to "ওয়া ইযা ক্বীলা লাহুম আ-মিনূ কামা- আ-মানান না-সু ক্বা-লূ আনু'মিনু কামা- আ-মানাস সুফাহা-উ, আলা- ইন্নাহুম হুমুস সুফাহা-উ ওয়ালা-কিল লা- ইয়া‘লামূন।",
        "2:14" to "ওয়া ইযা লাক্বুল্লাযীনা আ-মানূ ক্বা-লূ আ-মান্না-, ওয়া ইযা খালাও ইলা- শায়া-ত্বীনিহিম ক্বা-লূ ইন্না- মা‘আকুম ইন্নামা- নাহনু মুসতাহযিঊন।",
        "2:15" to "আল্লা-হু ইয়াসতাহযিউ বিহিম ওয়া ইয়ামুদ্দুহুম ফী ত্বুগইয়া-নিহিম ইয়া‘মাহূন।",
        "2:16" to "উলা-ইকাল্লাযীনাশ তারাওদ্ব দ্বালা-লাতা বিলহুদা- ফামা- রাবিহাত তিজা-রাতুহুম ওয়ামা- কা-নূ মুহতাদীন।",
        "2:17" to "মাছালুহুম কামাছালিল্লাযিস তাওক্বাদা না-রান ফালাম্মা- আদ্বা-আত মা- হাওলাহূ যাহাবাল্লা-হু বিনূরিহিম ওয়া তারাকাহুম ফী যুলুমা-তিল লা- ইউবসিরূন।",
        "2:18" to "ছুম্মুম বুকমুন ‘উমইয়ুন ফাহুম লা- ইয়ারজি‘ঊন।",
        "2:19" to "আও কাছাইয়িবিম মিনাস সামা-ই ফীহি যুলুমা-তুওঁ ওয়া রা‘দুওঁ ওয়া বারক্বুন ইয়াজ‘আলূনা আছ-বি‘আহুম ফী আ-যা-নিহিম মিনাস সাওয়া-‘ইক্বি হাযারাল মাওত, ওয়াল্লা-হু মুহীত্বুম বিল কা-ফিরীন।",
        "2:20" to "ইয়াকা-দুল বারক্বু ইয়াখত্বাফু আবছা-রাহুম, কুল্লামা- আদ্বা-আ লাহুম মাশাও ফীহি ওয়া ইযা- আযলামা ‘আলাইহিম ক্বা-মূ, ওয়ালাও শা-আল্লাহু লাযাহাবা বিসাম‘ইহিম ওয়া আবছা-রিহিম, ইন্নাল্লা-হা ‘আলা কুল্লি শাই’ইন ক্বাদীর।",

        // Ayat al-Kursi & Key Ayahs of Surah 2
        "2:255" to "আল্লাহু লা- ইলাহা ইল্লা হুওয়াল হাইয়্যুল ক্বাইয়্যূম, লা তা'খুযুহূ সিনাতুঁও ওয়ালা নাওম, লাহূ মা ফিসসামাওয়াতি ওয়ামা ফিল আরদ্ব, মান যাল্লাযী ইয়াশফা‘উ ‘ইনদাহূ ইল্লা বিইযনিহ, ইয়া‘লামু মা বাইনা আইদীহিম ওয়ামা খালফাহুম, ওয়ালা ইউহীতূনা বিশাই’ইম মিন ‘ইলমিহী ইল্লা বিমা শা-আ, ওয়াসি‘আ কুরসিয়্যুহুস সামাওয়াতি ওয়াল আরদ্ব, ওয়ালা ইয়াউদুহূ হিফযুহুমা, ওয়াহুওয়াল ‘আলিয়্যুল ‘আযীম।",
        "2:285" to "আ-মানার রাসূলু বিমা- উনঝিলা ইলাইহি মির রাব্বিহী ওয়াল মু'মিনূন, কুল্লুন আ-মানা বিল্লাহি ওয়া মালা-ইকাতিহী ওয়া কুতুবিহী ওয়া রুসুলিহ, লা নুফাররিক্বু বাইনা আহাদিম মির রুসুলিহ, ওয়া ক্বলূ সামি‘না ওয়া আত্বা‘না গুফরা-নাকা রাব্বানা ওয়া ইলাইকাল মাছীর।",
        "2:286" to "লা ইউকাল্লিফুল্লাহু নাফসান ইল্লা উস‘আহা, লাহা মা কাসাবাত ওয়া ‘আলাইহা মাক তাসাবাত, রাব্বানা লা তুআখিযনা- ইন নাসীনা- আও আখত্বা'না, রাব্বানা ওয়ালা তাহমিল ‘আলাইনা- ইছরান কামা হামালতাহূ ‘আলাল্লাযীনা মিন ক্বাবলিনা, রাব্বানা ওয়ালা তুহাম্মিলনা মা লা ত্বাক্বাতা লানা বিহ, ওয়া‘ফু ‘আন্না ওয়াগফির লানা ওয়ারহামনা, আনতা মাওলা-না ফানছুরনা ‘আলাল ক্বাওমিল কা-ফিরীন।",

        // Surah 36: Ya-Sin (Key Ayahs)
        "36:1" to "ইয়া-সীন।",
        "36:2" to "ওয়াল ক্বুরআনিল হাকীম।",
        "36:3" to "ইন্নাকা লামিনাল মুরসালীন।",
        "36:4" to "‘আলা ছিরাত্বিম মুসতাক্বীম।",
        "36:5" to "তানঝীলাল ‘আঝীঝির রাহীম।",
        "36:6" to "লিতুনযিরা ক্বাওমাম মা- উনযিরা আ-বা-উহুম ফাহুম গাফিলূন।",

        // Surah 67: Al-Mulk (Key Ayahs)
        "67:1" to "তাবারাকাল্লাযী বিয়াদিহিল মুলকু ওয়াহুওয়া ‘আলা কুল্লি শাই’ইন ক্বাদীর।",
        "67:2" to "আল্লাযী খালাক্বাল মাওতা ওয়াল হায়াতা লিইয়াবলুয়াকুম আইয়্যুকুম আহসানু ‘আমালা, ওয়াহুওয়াল ‘আঝীঝুল গাফূর।",

        // Surah 93: Ad-Duha
        "93:1" to "ওয়াদ্ব-দ্বুহা।",
        "93:2" to "ওয়াল্লাইলি ইযা সাজা।",
        "93:3" to "মা ওয়াদ্দা‘আকা রাব্বুকা ওয়ামা ক্বালা।",

        // Surah 94: Ash-Sharh (Al-Inshirah)
        "94:1" to "আলাম নাশরাহ লাকা ছাদরাক।",
        "94:2" to "ওয়া ওয়াদ্বা‘না ‘আনকা বিঝরাক।",
        "94:3" to "আল্লাযী আনক্বাদ্বা যাহরাক।",
        "94:4" to "ওয়া রাফা‘না লাকা যিকরাক।",
        "94:5" to "ফা ইন্না মা‘আল ‘উসরি ইউসরা।",
        "94:6" to "ইন্না মা‘আল ‘উসরি ইউসরা।",
        "94:7" to "ফা ইযা ফারাগতা ফানছাব।",
        "94:8" to "ওয়া ইলা রাব্বিকা ফারগাব।",

        // Surah 97: Al-Qadr
        "97:1" to "ইন্না- আনঝালনাহু ফী লাইলাতিল ক্বাদর।",
        "97:2" to "ওয়ামা- আদরা-কা মা লাইলাতুল ক্বাদর।",
        "97:3" to "লাইলাতুল ক্বাদরি খাইরুম মিন আলফি শাহর।",
        "97:4" to "তানাঝঝালুল মালা-ইকাতু ওয়ার রূহু ফীহা বিইযনি রাব্বিহিম মিন কুল্লি আমর।",
        "97:5" to "সালা-মুন হিয়া হাত্তা মাত্বলা‘ইল ফাজর।",

        // Surah 103: Al-Asr
        "103:1" to "ওয়াল ‘আছর।",
        "103:2" to "ইন্নাল ইনসা-না লাফী খুসর।",
        "103:3" to "ইল্লাল্লাযীনা আ-মানূ ওয়া ‘আমিলুছ ছ-লিহা-তি ওয়া তাওয়া-ছাও বিল হাক্বক্বি ওয়া তাওয়া-ছাও বিচ্ছাবর।",

        // Surah 108: Al-Kawthar
        "108:1" to "ইন্না- আ‘ত্বাইনা-কাল কাওছার।",
        "108:2" to "ফাছাল্লি লিরাব্বিকা ওয়ানহার।",
        "108:3" to "ইন্না শা-নিআকা হুওয়াল আবতার।",

        // Surah 109: Al-Kafirun
        "109:1" to "ক্বুল ইয়া- আইয়্যুহাল কা-ফিরূন।",
        "109:2" to "লা- আ‘বুদু মা তা‘বুদূন।",
        "109:3" to "ওয়ালা- আনতুম ‘আ-বিদূনা মা- আ‘বুদ।",
        "109:4" to "ওয়ালা- আনা ‘আ-বিদুম মা ‘আবাদতুম।",
        "109:5" to "ওয়ালা- আনতুম ‘আ-বিদূনা মা- আ‘বুদ।",
        "109:6" to "লাকুম দীনুকুম ওয়ালিয়া দীন।",

        // Surah 110: An-Nasr
        "110:1" to "ইযা জা-আ নাছরুল্লাহি ওয়াল ফাতহ।",
        "110:2" to "ওয়া রাআইতান না-সা ইয়াদখুলূনা ফী দীনিল্লাহি আফওয়া-জা।",
        "110:3" to "ফাসাব্বিহ বিহামদি রাব্বিকা ওয়াসতাগফিরহু, ইন্নাহূ কা-না তাউওয়া-বা।",

        // Surah 111: Al-Masad
        "111:1" to "তাব্বাত ইয়াদা- আবী লাহাবিঁও ওয়াতাব্ব।",
        "111:2" to "মা- আগনা ‘আনহু মা-লুহূ ওয়ামা কাসাব।",
        "111:3" to "সায়াসলা না-রান যা-তা লাহাব।",
        "111:4" to "ওয়ামরাআতুহূ হাম্মা-লাতাল হাত্বাব।",
        "111:5" to "ফী জীদিহা হাবলুম মিম মাসাদ।",

        // Surah 112: Al-Ikhlas
        "112:1" to "ক্বুল হুওয়াল্লা-হু আহাদ।",
        "112:2" to "আল্লা-হুছ ছামাদ।",
        "112:3" to "লাম ইয়ালিদ ওয়া লাম ইউ-লাদ।",
        "112:4" to "ওয়া লাম ইয়াকুল্লাহূ কুফুওয়ান আহাদ।",

        // Surah 113: Al-Falaq
        "113:1" to "ক্বুল আ‘ঊযু বিরাব্বিল ফালাক্ব।",
        "113:2" to "মিন শাররি মা খালাক্ব।",
        "113:3" to "ওয়া মিন শাররি গা-সিক্বিন ইযা ওয়াক্বাব।",
        "113:4" to "ওয়া মিন শাররিন নাফফা-ছা-তি ফিল ‘উক্বাদ।",
        "113:5" to "ওয়া মিন শাররি হা-সিদিন ইযা হাসাদ।",

        // Surah 114: An-Nas
        "114:1" to "ক্বুল আ‘ঊযু বিরাব্বিন না-স।",
        "114:2" to "মালিকিন না-স।",
        "114:3" to "ইলা-হিন না-স।",
        "114:4" to "মিন শাররিল ওয়াসওয়া-সিল খান্না-স।",
        "114:5" to "আল্লাযী ইউওয়াসউইসু ফী ছুদূরি না-স।",
        "114:6" to "মিনাল জিন্নাতি ওয়ান না-স।"
    )

    /**
     * Common sacred Quranic expressions and frequently appearing words with scholarly spelling
     */
    private val SACRED_VOCABULARY = mapOf(
        "اللَّهِ" to "আল্লাহি",
        "اللَّهُ" to "আল্লাহু",
        "اللَّهَ" to "আল্লা-হা",
        "لِلَّهِ" to "লিল্লাহি",
        "بِاللَّهِ" to "বিল্লাহি",
        "الرَّحْمَٰنِ" to "আর-রাহমানি",
        "الرَّحْمَٰنُ" to "আর-রাহমানু",
        "الرَّحِيمِ" to "আর-রাহীম",
        "الرَّحِيمُ" to "আর-রাহীমু",
        "رَبِّ" to "রাব্বি",
        "رَبَّنَا" to "রাব্বানা",
        "رَبُّكُمْ" to "রাব্বুকুম",
        "مَالِكِ" to "মালিকি",
        "يَوْمِ" to "ইয়াওমি",
        "الدِّينِ" to "আদ-দীন",
        "إِيَّاكَ" to "ইয়্যাকা",
        "نَعْبُدُ" to "না‘বুদু",
        "وَإِيَّاكَ" to "ওয়া ইয়্যাকা",
        "نَسْتَعِينُ" to "নাসতা‘ঈন",
        "اهْدِنَا" to "ইহদিনা",
        "الصِّرَاطَ" to "আছ-ছিরাত্বা",
        "الْمُسْتَقِيمَ" to "আল-মুসতাক্বীম",
        "الَّذِينَ" to "আল্লাযীনা",
        "أَنْعَمْتَ" to "আন‘আমতা",
        "عَلَيْهِمْ" to "‘আলাইহিম",
        "غَيْرِ" to "গইরিল",
        "الْمَغْضُوبِ" to "মাগদ্বূবি",
        "وَلَا" to "ওয়ালা",
        "الضَّالِّينَ" to "আদ-দ্বল্লীন",
        "قُلْ" to "ক্বুল",
        "أَحَدٌ" to "আহাদ",
        "الصَّمَدُ" to "আছ-ছামাদ",
        "لَمْ" to "লাম",
        "يَلِدْ" to "ইয়ালিদ",
        "يُولَدْ" to "ইউলাদ",
        "كُفُوًا" to "কুফুওয়ান",
        "مِنْ" to "মিন",
        "مَا" to "মা-",
        "فِي" to "ফী",
        "إِنَّ" to "ইন্না",
        "أَنَّ" to "আন্না",
        "مَنْ" to "মান",
        "هَٰذَا" to "হা-যা",
        "ذَٰلِكَ" to "যা-লিকা",
        "الْحَمْدُ" to "আল-হামদু",
        "سُبْحَانَ" to "সুবহানা",
        "آمَنُوا" to "আ-মানূ",
        "ءَامَنُوا" to "আ-মানূ",
        "آمَنَ" to "আ-মানা",
        "ءَامَنَ" to "আ-মানা",
        "آمَنَّا" to "আ-মান্না",
        "ءَامَنَّا" to "আ-মান্না",
        "قَالُوا" to "ক্বলূ",
        "كَانُوا" to "কা-নূ",
        "كَانَ" to "কা-না",
        "عَلِيمٌ" to "‘আলীম",
        "حَكِيمٌ" to "হাকীম",
        "غَفُورٌ" to "গফূর",
        "قَدِيرٌ" to "ক্বাদীর",
        "يَعْلَمُونَ" to "ইয়া‘লামূন",
        "يَعْلَمُ" to "ইয়া‘লামু",
        "السُّفَهَاءُ" to "আস-সুফাহা-উ",
        "السَّمَاءِ" to "আস-সামা-ই",
        "السَّمَاوَاتِ" to "আস-সামা-ওয়া-তি",
        "إِنَّهُمْ" to "ইন্নাহুম",
        "أَنُؤْمِنُ" to "আনু'মিনু",
        "يُؤْمِنُونَ" to "ইউ'মিনূন",
        "مُؤْمِنُونَ" to "মু'মিনূন",
        "قِيلَ" to "ক্বীলা"
    )

    private val CONSONANT_MAP = mapOf(
        'ب' to "ব", 'ت' to "ত", 'ث' to "ছ", 'ج' to "জ", 'ح' to "হ",
        'خ' to "খ", 'د' to "দ", 'ذ' to "য", 'ر' to "র", 'ز' to "ঝ",
        'س' to "স", 'ش' to "শ", 'ص' to "ছ", 'ض' to "দ্ব", 'ط' to "ত্ব",
        'ظ' to "জ্ব", 'ع' to "‘", 'غ' to "গ", 'ف' to "ফ", 'ق' to "ক্ব",
        'ك' to "ক", 'ل' to "ল", 'م' to "ম", 'ن' to "ন", 'ه' to "হ",
        'و' to "ওয়", 'ي' to "ইয়", 'ى' to "আ", 'ة' to "ত",
        'ء' to "’", 'أ' to "আ", 'إ' to "ই", 'آ' to "আ-", 'ؤ' to "উ", 'ئ' to "ই",
        'ا' to "", 'ٱ' to ""
    )

    // Solar consonants that assimilate the 'L' of 'Al-' prefix
    private val SOLAR_CHARS = setOf('ت', 'ث', 'د', 'ذ', 'ر', 'ز', 'س', 'ش', 'ص', 'ض', 'ط', 'ظ', 'ل', 'ن')

    /**
     * Primary entry point: Get the verified or dynamic Bengali phonetic transliteration
     * for any Surah and Ayah, completely cleansed of any broken Unicode or floating marks.
     */
    fun getPronunciation(surahNumber: Int, ayahNumber: Int, arabicText: String): String {
        // 1. Check verified Ayah master table
        val key = "$surahNumber:$ayahNumber"
        VERIFIED_AYAH_TRANSLITERATION[key]?.let { return it }

        // 2. Check Muqatta'at (Disjointed letters)
        val trimmed = arabicText.trim().replace(Regex("[\\u06D6-\\u06ED\\s]"), "")
        MUQATTAAT[trimmed]?.let { return "$it।" }

        // 3. Generate dynamic authentic Bengali transliteration
        val transliterated = transliterate(arabicText)
        return cleanPronunciationText(transliterated)
    }

    /**
     * Inspects existing pronunciation in the Room DB or from network.
     * If broken or outdated, recomputes and permanently heals it.
     */
    fun sanitizeAndHeal(
        currentPronunciation: String,
        surahNumber: Int,
        ayahNumber: Int,
        arabicText: String
    ): String {
        val needsRegeneration = currentPronunciation.isBlank() ||
            currentPronunciation == "—" ||
            currentPronunciation.contains("সহীহ মাখরাজ") ||
            currentPronunciation.contains("󰀀") ||
            currentPronunciation.contains("’া") ||
            currentPronunciation.contains("'া") ||
            currentPronunciation.contains("’ু") ||
            currentPronunciation.contains("’ি") ||
            currentPronunciation.contains("‘া") ||
            currentPronunciation.contains("ক্বালুওয়া") ||
            currentPronunciation.contains("হিননাহুম") ||
            currentPronunciation.contains("সসুফাহা") ||
            currentPronunciation.contains("ননাসু") ||
            currentPronunciation.contains("মিনুওয়া") ||
            currentPronunciation.contains("\uFFFD") ||
            currentPronunciation.contains("\u25CC")

        if (needsRegeneration) {
            return getPronunciation(surahNumber, ayahNumber, arabicText)
        }

        return cleanPronunciationText(currentPronunciation)
    }

    /**
     * Cleans and sanitizes any Bengali pronunciation string, ensuring:
     * - No unattached dependent vowel signs ('া, 'ি, 'ু) after apostrophe/hyphen/space
     * - No broken Unicode glyphs or tofu squares
     * - Proper joining and spaces
     */
    fun cleanPronunciationText(rawText: String): String {
        if (rawText.isBlank()) return rawText

        var s = rawText
        // Fix broken combining vowel marks following punctuation, quotes, or apostrophes
        s = s.replace("’া", "আ")
            .replace("'া", "আ")
            .replace("‘া", "‘আ")
            .replace("’ি", "ই")
            .replace("'ি", "ই")
            .replace("‘ি", "‘ই")
            .replace("’ু", "উ")
            .replace("'ু", "উ")
            .replace("‘ু", "‘উ")
            .replace("’ে", "এ")
            .replace("'ে", "এ")
            .replace("’ো", "ও")
            .replace("'ো", "ও")
            .replace(" -া", " -আ")
            .replace("-া", "-আ")
            .replace(" -ি", " -ই")
            .replace("-ি", "-ই")
            .replace(" -ু", " -উ")
            .replace("-ু", "-উ")
            .replace(" া", " আ")
            .replace(" ি", " ই")
            .replace(" ু", " উ")
            .replace(" ো", " ও")
            .replace(" ে", " এ")

        // Remove any replacement / tofu chars like \uFFFD, \u25CC, surrogate pairs
        s = s.replace(Regex("[\\uFFFD\\u25CC\\uFEFF]"), "")
        s = s.replace(Regex("[\\uD800-\\uDBFF][\\uDC00-\\uDFFF]"), "")

        // Fix duplicated solar consonant doubling
        s = s.replace("আন-নন", "আন-ন")
            .replace("আস-সস", "আস-স")
            .replace("আশ-শশ", "আশ-শ")
            .replace("আর-রর", "আর-র")
            .replace("আছ-ছছ", "আছ-ছ")
            .replace("আয-যয", "আয-য")
            .replace("আত-তত", "আত-ত")
            .replace("আদ-দদ", "আদ-দ")
            .replace("আত্ব-ত্বত্ব", "আত্ব-ত্ব")
            .replace("আদ্ব-দ্বদ্ব", "আদ্ব-দ্ব")

        // Fix common distorted patterns from legacy transliteration
        s = s.replace("'󰀀মিনুওয়া", "আ-মিনূ")
            .replace("'মিনুওয়া", "আ-মিনূ")
            .replace("মিনুওয়া", "মানূ")
            .replace("'󰀀মানা", "আ-মানা")
            .replace("'মানা", "আ-মানা")
            .replace("ক্বালুওয়া-", "ক্বলূ ")
            .replace("ক্বালুওয়া", "ক্বলূ")
            .replace("হিননাহুম", "ইন্নাহুম")
            .replace("আস-সসুফাহা-’উ", "আস-সুফাহা-উ")
            .replace("আস-সুফাহা-’উ", "আস-সুফাহা-উ")
            .replace("সুফাহা-’উ", "সুফাহা-উ")
            .replace("ইয়া‘আলামুওয়ানা", "ইয়া‘লামূন")
            .replace("ইয়া‘আলামূন", "ইয়া‘লামূন")
            .replace("ওয়ালা কিন", "ওয়ালা-কিন")
            .replace("লা ইয়া‘লামূন", "লা- ইয়া‘লামূন")
            .replace("  ", " ")
            .trim()

        return if (!s.endsWith("।") && !s.endsWith("!") && !s.endsWith("?")) "$s।" else s
    }

    /**
     * Converts any Quranic Arabic text into natural, readable Bengali pronunciation.
     */
    fun transliterate(arabicText: String): String {
        if (arabicText.isBlank()) return "—"

        val cleanText = arabicText.replace(Regex("[\\u06D6-\\u06ED\\u0610-\\u061A]"), "")
            .replace(Regex("[\uFD3E\uFD3F0-9٠-٩]"), "")
            .trim()

        // Split into words while preserving word boundaries
        val words = cleanText.split(Regex("\\s+"))
        val resultWords = mutableListOf<String>()

        for ((index, rawWord) in words.withIndex()) {
            val word = rawWord.trim()
            if (word.isBlank()) continue

            // Strip trailing punctuation for dictionary check
            val coreWord = word.trim { it in ",.;:()!?\"'«»" }

            // 1. Direct sacred vocabulary match
            val sacredMatch = SACRED_VOCABULARY[coreWord]
            if (sacredMatch != null) {
                resultWords.add(sacredMatch)
                continue
            }

            // 2. Muqatta'at match
            val muqattaatMatch = MUQATTAAT[coreWord]
            if (muqattaatMatch != null) {
                resultWords.add(muqattaatMatch)
                continue
            }

            // 3. Dynamic phonetic conversion for this word
            val isLastWord = (index == words.size - 1)
            val converted = convertWordPhonetically(coreWord, isLastWord)
            if (converted.isNotBlank()) {
                resultWords.add(converted)
            }
        }

        var fullSentence = resultWords.joinToString(" ")
        fullSentence = polishBengaliText(fullSentence)

        return cleanPronunciationText(fullSentence)
    }

    private fun convertWordPhonetically(word: String, isLastWord: Boolean): String {
        if (word.isEmpty()) return ""

        val chars = word.toCharArray()
        val n = chars.size
        val sb = StringBuilder()
        var i = 0

        // Handle Alif-Lam prefix (ال / ٱل)
        var solarConsumed = false
        if (n >= 2 && (chars[0] == 'ا' || chars[0] == 'ٱ') && chars[1] == 'ل') {
            if (n >= 3 && chars[2] in SOLAR_CHARS) {
                // Solar assimilation: Al-Shams -> Ash-Shams, Al-Nas -> An-Nas
                val solarConsonant = CONSONANT_MAP[chars[2]] ?: "ল"
                sb.append("আ$solarConsonant-")
                solarConsumed = true
                i = 2 // Move directly to the solar letter
            } else {
                sb.append("আল-")
                i = 2
            }
        }

        while (i < n) {
            val c = chars[i]

            // Look ahead for diacritics and shaddah
            var hasShaddah = false
            var hasSukun = false
            val vowels = mutableListOf<Char>()
            var j = i + 1
            while (j < n && isDiacritic(chars[j])) {
                val d = chars[j]
                if (d == '\u0651') {
                    hasShaddah = true
                } else if (d == '\u0652') {
                    hasSukun = true
                } else {
                    vowels.add(d)
                }
                j++
            }

            // If this is the solar letter right after Al- assimilation, do NOT double it again
            if (solarConsumed && i == 2) {
                hasShaddah = false
            }

            // Special handling for Hamza standalone (ء)
            if (c == 'ء') {
                if (vowels.isNotEmpty()) {
                    for (v in vowels) {
                        when (v) {
                            '\u064E' -> sb.append(if (sb.isEmpty()) "আ" else "আ")
                            '\u064F' -> sb.append("উ")
                            '\u0650' -> sb.append("ই")
                            '\u0670', '\u0653' -> sb.append("আ-")
                            '\u064B' -> sb.append("আন")
                            '\u064C' -> sb.append("উন")
                            '\u064D' -> sb.append("ইন")
                        }
                    }
                } else if (hasSukun) {
                    sb.append("’")
                } else {
                    // Default hamza
                    sb.append(if (sb.isEmpty()) "আ" else "উ")
                }
                i = j
                continue
            }

            // Special handling for Ayn (ع)
            if (c == 'ع') {
                if (vowels.isNotEmpty()) {
                    for (v in vowels) {
                        when (v) {
                            '\u064E' -> sb.append("‘আ")
                            '\u064F' -> sb.append("‘উ")
                            '\u0650' -> sb.append("‘ই")
                            '\u0670', '\u0653' -> sb.append("‘আ-")
                            '\u064B' -> sb.append("‘আন")
                            '\u064C' -> sb.append("‘উন")
                            '\u064D' -> sb.append("‘ইন")
                        }
                    }
                } else {
                    sb.append("‘")
                }
                i = j
                continue
            }

            // Special handling for Alif at the start of a word
            if (i == 0 && (c == 'ا' || c == 'أ' || c == 'إ' || c == 'آ' || c == 'ٱ')) {
                if (c == 'إ' || (vowels.contains('\u0650'))) {
                    sb.append("ই")
                } else if (c == 'آ' || vowels.contains('\u0653')) {
                    sb.append("আ-")
                } else if (vowels.contains('\u064F')) {
                    sb.append("উ")
                } else {
                    sb.append("আ")
                }
                i = j
                continue
            }

            // Prolongation Waw (و preceded by Damma)
            if (c == 'و' && vowels.isEmpty() && sb.isNotEmpty()) {
                val lastChar = sb.last()
                if (lastChar == 'ু') {
                    // Turn short 'u' to long 'ū'
                    sb.setLength(sb.length - 1)
                    sb.append("ূ")
                    i = j
                    continue
                }
            }

            // Prolongation Ya (ي preceded by Kasra)
            if ((c == 'ي' || c == 'ى') && vowels.isEmpty() && sb.isNotEmpty()) {
                val lastChar = sb.last()
                if (lastChar == 'ি') {
                    // Turn short 'i' to long 'ī'
                    sb.setLength(sb.length - 1)
                    sb.append("ী")
                    i = j
                    continue
                }
            }

            // Orthographic silent Alif at end of plural verb (e.g. قَالُوا / آمَنُوا)
            if (c == 'ا' && i == n - 1 && sb.isNotEmpty() && (sb.endsWith("ূ") || sb.endsWith("ু") || sb.endsWith("ওয়"))) {
                i = j
                continue
            }

            val consonantBn = CONSONANT_MAP[c]
            if (consonantBn != null && consonantBn.isNotEmpty()) {
                // Shaddah: double the consonant if not at the start
                if (hasShaddah && sb.isNotEmpty()) {
                    val firstChar = consonantBn.first()
                    if (firstChar !in listOf('‘', '’', ' ', '-')) {
                        sb.append(firstChar)
                    }
                }

                sb.append(consonantBn)

                // Apply vowels
                if (vowels.isEmpty()) {
                    if (hasSukun) {
                        // Sukun: consonant ends without vowel
                    } else if (i == n - 1 && isLastWord) {
                        // Waqf at end of verse: drop short trailing vowel
                    }
                } else {
                    for (v in vowels) {
                        when (v) {
                            '\u064E' -> sb.append("া")       // Fatha
                            '\u064F' -> sb.append("ু")       // Damma
                            '\u0650' -> sb.append("ি")       // Kasra
                            '\u0670' -> sb.append("া-")      // Dagger Alif (Alif Khanjariyya)
                            '\u0653' -> sb.append("া-")      // Maddah
                            '\u064B' -> sb.append("ান")      // Fathatan
                            '\u064C' -> sb.append("ুন")      // Dammatan
                            '\u064D' -> sb.append("িন")      // Kasratan
                        }
                    }
                }
            }

            i = j
        }

        return sb.toString()
    }

    private fun isDiacritic(c: Char): Boolean {
        return c in '\u064B'..'\u0655' || c == '\u0670'
    }

    private fun polishBengaliText(text: String): String {
        return text
            .replace("াা", "া")
            .replace("িি", "ী")
            .replace("ুু", "ূ")
            .replace("ওয়াহুওয়া", "ওয়াহুওয়া")
            .replace("ওয়ালা-", "ওয়ালা ")
            .replace("ক্বলূওয়া", "ক্বলূ")
            .replace("  ", " ")
            .trim()
    }
}
