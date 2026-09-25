package com.example.data.datasource

import com.example.data.model.*

object QuranActionEngineExpandedData {

    val essentialLifeAyahs: List<AyahActionInsight> = listOf(
        // 1. Al-Fatiha 1:5 - Tawheed & seeking help only from Allah
        AyahActionInsight(
            ayahId = "action_1_5",
            surahNumber = 1,
            ayahNumber = 5,
            surahNameArabic = "سورة الفاتحة",
            surahNameBangla = "সূরা আল-ফাতিহা (১:৫)",
            surahNameEnglish = "Surah Al-Fatiha (The Opening)",
            revelationTypeBn = "মাক্কী",
            arabicText = "إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ",
            transliterationBn = "ইয়্যা-কা না‘বুদু ওয়া ইয়্যা-কা নাসতা‘ঈন।",
            banglaTranslation = "আমরা কেবল আপনারই ইবাদত করি এবং কেবল আপনারই কাছে সাহায্য চাই।",
            englishTranslation = "It is You we worship and You we ask for help.",
            whatDoesItTeachBn = "জীবনের একমাত্র উদ্দেশ্য আল্লাহর দাসত্ব এবং যে কোনো প্রয়োজন বা বিপদে কেবল তাঁরই কাছে মাথা নত করা। এটি শিরক ও অহংকার থেকে মুক্তি দেয়।",
            whatToNoticeBn = "ইবাদতের আগে আল্লাহর একত্ববাদ এবং মানুষের অক্ষমতার স্বীকৃতি। মানুষ যত বড় শক্তিধরই হোক, সে আল্লাহর সাহায্যের মুখাপেক্ষী।",
            whatToBeCarefulAboutBn = "কোনো সৃষ্টি বা মানুষের ওপর অন্ধ নির্ভরতা এবং আল্লাহ ছাড়া অন্য কারও কাছে অলৌকিক সাহায্য চাওয়া থেকে বেঁচে থাকা।",
            whatCanIPracticeBn = "আজ যেকোনো সমস্যা বা সিদ্ধান্তের শুরুতে মনে মনে আল্লাহর কাছে সাহায্য চেয়ে 'ইয়া আল্লাহ, আমাকে সঠিক দিকনির্দেশনা দিন' বলা।",
            quranSaysBn = "ইবাদত ও সাহায্য প্রার্থনা একান্তভাবেই আল্লাহর জন্য নির্ধারিত।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: এটি তাওহীদের মূল স্তম্ভ। বান্দা যখন ঘোষণা করে কেবল আল্লাহর ইবাদত ও সাহায্য চায়, তখন সে সকল প্রকার রিয়া (লোকদেখানো আমল) থেকে মুক্ত হয়।",
            possiblePersonalApplicationBn = "আজকের দিনে কোনো কাজ শুরু করার সময় নিজের মেধা বা অর্থের অহংকার না করে আল্লাহর সাহায্য স্মরণ করা।",
            reflectiveQuestions = listOf(
                "আমি কি সত্যিই সব ব্যাপারে আল্লাহর ওপর নির্ভর করি নাকি নিজের শক্তিতে অতিরিক্ত ভরসা করি?",
                "আজকের দিনে কোন সংকটে আমার একান্তভাবে আল্লাহর সাহায্য প্রয়োজন?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.WORSHIP to listOf("নামাজে এই আয়াতটি পড়ার সময় অর্থ বুঝে ধীরস্থিরভাবে তিলাওয়াত করুন।"),
                LifeSphere.MINDSET to listOf("যেকোনো উদ্বেগের মুহূর্তে বলুন: সাহায্যকারী একমাত্র আল্লাহ।")
            ),
            defaultTodayAction = "আজকের দিনের প্রতিটি কাজের শুরুতে এবং সালাতে 'ইয়্যাকা নাসতাঈন' অনুধাবন করে আল্লাহর সাহায্য প্রার্থনা করুন।",
            primaryThemes = listOf("তাওহীদ", "একনিষ্ঠ ইবাদত", "আল্লাহর সাহায্য"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/001005.mp3"
        ),

        // 2. Al-Baqarah 2:155-156 - Trials and Inna Lillahi
        AyahActionInsight(
            ayahId = "action_2_155",
            surahNumber = 2,
            ayahNumber = 155,
            surahNameArabic = "سورة البقرة",
            surahNameBangla = "সূরা আল-বাক্বারাহ (২:১৫৫-১৫৬)",
            surahNameEnglish = "Surah Al-Baqarah (The Cow)",
            revelationTypeBn = "মাদানী",
            arabicText = "وَلَنَبْلُوَنَّكُم بِشَيْءٍ مِّنَ الْخَوْفِ وَالْجُوعِ وَنَقْصٍ مِّنَ الْأَمْوَالِ وَالْأَنفُسِ وَالثَّمَرَاتِ ۗ وَبَشِّرِ الصَّابِرِينَ ۝ الَّذِينَ إِذَا أَصَابَتْهُم مُّصِيبَةٌ قَالُوا إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ",
            transliterationBn = "ওয়া লানাবলুওয়ান্নাকুম বিশাই’ইম মিনাল খাওফি ওয়াল জূ‘ই ওয়া নাক্বসিম মিনাল আমওয়া-লি ওয়াল আনফুসি ওয়াস সামারা-ত; ওয়া বাশশিরিস সা-বিরীন। আল্লাযীনা ইযা- আসা-বাতহুম মুসীবাতুন ক্বালূ ইন্না- লিল্লা-হি ওয়া ইন্না- ইলাইহি রাজি‘ঊন।",
            banglaTranslation = "আর আমি অবশ্যই তোমাদেরকে পরীক্ষা করব কিছুটা ভয়, ক্ষুধা, ধন-সম্পদ, জীবন ও ফসলাদির ক্ষতির মাধ্যমে। আর সুসংবাদ দিন ধৈর্যশীলদের, যারা কোনো বিপদে পড়লে বলে: নিশ্চয় আমরা আল্লাহরই এবং আমরা তাঁরই দিকে প্রত্যাবর্তনকারী।",
            englishTranslation = "And We will surely test you with something of fear and hunger and a loss of wealth and lives and fruits, but give good tidings to the patient, Who, when disaster strikes them, say, 'Indeed we belong to Allah, and indeed to Him we will return.'",
            whatDoesItTeachBn = "পৃথিবীর জীবন আরামের চিরস্থায়ী স্থান নয়, বরং পরীক্ষার ক্ষেত্র। দুঃখ, অভাব ও ক্ষতি জীবনের স্বাভাবিক নিয়ম, আর এতে ধৈর্য ধারণকারীদের জন্যই রয়েছে রবের পরম ক্ষমা ও রহমত।",
            whatToNoticeBn = "বিপদ কোনো স্থায়ী অভিশাপ নয়, বরং ঈমান পরীক্ষার উপলক্ষ। বিপদে বান্দা যখন 'ইন্না লিল্লাহ...' বলে, তখন সে তার সকল মালিকানা আল্লাহর হাতে সঁপে দেয়।",
            whatToBeCarefulAboutBn = "ক্ষতি বা শোকের মুহূর্তে আল্লাহর ফায়সালার বিরুদ্ধে অভিযোগ বা ক্ষোভ প্রকাশ করা থেকে বিরত থাকা।",
            whatCanIPracticeBn = "আজ যেকোনো ছোট বা বড় ক্ষতি, আঘাত বা অপ্রত্যাশিত খবরে সাথে সাথে মনে প্রাণে 'ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রাজি'উন' পাঠ করা।",
            quranSaysBn = "জীবন নানা সংকট ও পরীক্ষায় পূর্ণ, কিন্তু চূড়ান্ত সুসংবাদ ধৈর্যশীলদের জন্য।",
            scholarlyInterpretationBn = "তাফসীর আস-সা'দী: যে ব্যক্তি বিপদের প্রথম ধাক্কায় 'ইন্না লিল্লাহ' বলে নিজেকে আল্লাহর মালিকানাধীন মনে করে, আল্লাহ তার হৃদয়ে অপার প্রশান্তি ও হেদায়াত দান করেন।",
            possiblePersonalApplicationBn = "পার্থিব কোনো বস্তু হারালে বা পরিকল্পনা ভেস্তে গেলে মেজাজ না হারিয়ে আল্লাহর উত্তম প্রতিদানের আশা করা।",
            reflectiveQuestions = listOf(
                "কোন ক্ষতি বা ভয় আমাকে সম্প্রতি সবচেয়ে বেশি ভারাক্রান্ত করেছে?",
                "আমি কি বিশ্বাস করি এই পরীক্ষার পর আল্লাহ আমাকে আরও উত্তম কিছু দিতে পারেন?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.DIFFICULT_SITUATIONS to listOf("বিপদে হতাশ না হয়ে শান্ত চিত্তে আল্লাহর সিদ্ধান্তের ওপর সন্তুষ্ট থাকুন।"),
                LifeSphere.MINDSET to listOf("মনে রাখুন: এই পৃথিবীর সব সম্পদ ও মানুষ একদিল আল্লাহর কাছেই ফিরে যাবে।")
            ),
            defaultTodayAction = "আজ কোনো ক্ষতি বা অপ্রীতিকর পরিস্থিতি দেখা দিলে তাত্ক্ষণিকভাবে মুখে ও হৃদয়ে 'ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রাজি'উন' স্মরণ করুন।",
            primaryThemes = listOf("ধৈর্য ও পরীক্ষা", "সান্ত্বনা", "তাওয়াক্কুল"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002155.mp3"
        ),

        // 3. Al-Baqarah 2:186 - Nearness of Allah and Du'a
        AyahActionInsight(
            ayahId = "action_2_186",
            surahNumber = 2,
            ayahNumber = 186,
            surahNameArabic = "سورة البقرة",
            surahNameBangla = "সূরা আল-বাক্বারাহ (২:১৮৬)",
            surahNameEnglish = "Surah Al-Baqarah (The Cow)",
            revelationTypeBn = "মাদানী",
            arabicText = "وَإِذَا سَأَلَكَ عِبَادِي عَنِّي فَإِنِّي قَرِيبٌ ۖ أُجِيبُ دَعْوَةَ الدَّاعِ إِذَا دَعَانِ ۖ فَلْيَسْتَجِيبُوا لِي وَلْيُؤْمِنُوا بِي لَعَلَّهُمْ يَرْشُدُونَ",
            transliterationBn = "ওয়া ইযা- সা’আলাকা ‘ইবা-দী ‘আন্নী ফাইন্নী ক্বারীব; উজীবু দা‘ওয়াতাদ দা-‘ই ইযা- দা‘আ-নি, ফালইয়াস্তাজীবূ লী ওয়ালইউ’মিনূ বী লা‘আল্লাহুম ইয়ারশুদূন।",
            banglaTranslation = "আর আমার বান্দাগণ যখন আপনার কাছে আমার সম্পর্কে জিজ্ঞেস করে, তখন বলুন: নিশ্চয় আমি অতি নিকটেই আছি। কোনো প্রার্থনাকারী যখন আমাকে ডাকে, আমি তার ডাকে সাড়া দেই। অতএব তারাও যেন আমার ডাকে সাড়া দেয় এবং আমার প্রতি ঈমান আনে, যাতে তারা সঠিক পথ পায়।",
            englishTranslation = "And when My servants ask you concerning Me, indeed I am near. I respond to the invocation of the supplicant when he calls upon Me. So let them respond to Me and believe in Me that they may be guided.",
            whatDoesItTeachBn = "আল্লাহ কোনো দূরবর্তী সত্তা নন; তিনি বান্দার হৃদয়ের স্পন্দনের চেয়েও নিকটে। বান্দা যখনই আন্তরিকভাবে ডাকে, আল্লাহ তার দোয়া শোনেন ও কবুল করেন।",
            whatToNoticeBn = "দোয়া কবুলের সাথে আল্লাহর বিধান মেনে চলার (ফালইয়াস্তাজিবূ লী) গভীর সম্পর্ক রয়েছে। বান্দা রবের ডাকে সাড়া দিলে রবও বান্দার ডাক ফিরিয়ে দেন না।",
            whatToBeCarefulAboutBn = "দোয়া কবুল হতে দেরি হলে হতাশ হয়ে দোয়া করা ছেড়ে দেওয়া থেকে সাবধান থাকা।",
            whatCanIPracticeBn = "আজ নির্জনে অন্তত ৫ মিনিট চোখ বন্ধ করে মন খুলে নিজের সব অভাব, ভয় ও স্বপ্ন আল্লাহর কাছে নিবেদন করা।",
            quranSaysBn = "মহান আল্লাহ বান্দার অতি নিকটে এবং প্রার্থনাকারীর ডাকে সাড়া দেন।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: আয়াতে কোনো মাধ্যম ছাড়াই আল্লাহ সরাসরি বলেছেন 'আমি অতি নিকটে'। দোয়া হলো সর্বোত্তম ইবাদত।",
            possiblePersonalApplicationBn = "আজকের দিনে কাউকে অভিযোগ করার আগে সিজদায় গিয়ে আল্লাহর সাথে একান্তে কথা বলা।",
            reflectiveQuestions = listOf(
                "আমি কি সত্যিই অনুভব করি যে আল্লাহ আমার মনের প্রতিটি আকুতি শুনছেন?",
                "আমি কি আল্লাহর নির্দেশ পালনে আন্তরিক যেভাবে আমি তাঁর কাছ থেকে দোয়া কবুল আশা করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.WORSHIP to listOf("তাহাজ্জুদে বা সালাতের সিজদায় অত্যন্ত আকুলভাবে নিজের মনের কথা পেশ করুন।"),
                LifeSphere.MINDSET to listOf("একাকীত্ব বোধ করলে স্মরণ করুন আল্লাহ আপনার সাথেই আছেন।")
            ),
            defaultTodayAction = "আজ কোনো সময় অপচয় না করে নির্জনে হাত তুলে বা সিজদায় গিয়ে অন্তত ৩টি মনের গভীর আরজু আল্লাহর কাছে ব্যক্ত করুন।",
            primaryThemes = listOf("দোয়া কবুল", "আল্লাহর নৈকট্য", "আত্মিক শান্তি"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002186.mp3"
        ),

        // 4. Al-Baqarah 2:255 - Ayatul Kursi
        AyahActionInsight(
            ayahId = "action_2_255",
            surahNumber = 2,
            ayahNumber = 255,
            surahNameArabic = "سورة البقرة",
            surahNameBangla = "সূরা আল-বাক্বারাহ (২:২৫৫ - আয়াতুল কুরসী)",
            surahNameEnglish = "Surah Al-Baqarah (The Cow)",
            revelationTypeBn = "মাদানী",
            arabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ",
            transliterationBn = "আল্লা-হু লা- ইলা-হা ইল্লা- হুওয়াল হাইয়্যুল ক্বাইয়্যূম; লা- তা’খুযুহূ সিনাতুঁও ওয়ালা- নাওম; লাহূ মা- ফিসসামা-ওয়া-তি ওয়ামা- ফিল আরদ্ব; মান যাল্লাযী ইয়াশফা‘উ ‘ইনদাহূ ইল্লা- বিইযনিহী; ইয়া‘লামু মা- বাইনা আইদীহিম ওয়ামা- খালফাহুম; ওয়ালা- ইউহীতূনা বিশাই’ইম মিন ‘ইলমিহী ইল্লা- বিমা- শা-আ; ওয়াসি‘আ কুরসিয়্যুহুস সামা-ওয়া-তি ওয়াল আরদ্ব; ওয়ালা- ইয়াউদুহূ হিফযুহুমা-; ওয়াহুওয়াল ‘আলিয়্যুল ‘আযীম।",
            banglaTranslation = "আল্লাহ, তিনি ছাড়া কোনো সত্য ইলাহ নেই। তিনি চিরঞ্জীব, সর্বসত্তার ধারক। তন্দ্রা বা নিদ্রা তাঁকে স্পর্শ করে না। আসমান ও যমীনে যা কিছু আছে সবই তাঁর। তাঁর অনুমতি ছাড়া কে সুপারিশ করবে? তিনি তাদের অতীত ও ভবিষ্যৎ সব জানেন। তাঁর কুরসী আকাশ ও পৃথিবীকে পরিবেষ্টন করে আছে এবং এদের রক্ষণাবেক্ষণ তাঁকে ক্লান্ত করে না। তিনি সর্বোচ্চ, মহান।",
            englishTranslation = "Allah - there is no deity except Him, the Ever-Living, the Sustainer of all existence. Neither drowsiness overtakes Him nor sleep. To Him belongs whatever is in the heavens and whatever is on the earth. Who is it that can intercede with Him except by His permission? He knows what is before them and what will be after them, and they encompass not a thing of His knowledge except for what He wills. His Kursi extends over the heavens and the earth, and their preservation tires Him not. And He is the Most High, the Most Great.",
            whatDoesItTeachBn = "আল্লাহর সার্বভৌমত্ব, অসীম ক্ষমতা, ক্লান্তিহীন মহিমা ও সৃষ্টিজগতের ওপর তাঁর পূর্ণ নিয়ন্ত্রণ। এটি বান্দাকে নির্ভীক ও নিশ্চিন্ত করে।",
            whatToNoticeBn = "আল্লাহ কখনো ঘুমান না, কখনো অন্যমনস্ক হন না। তিনি প্রতিটি সেকেন্ডে আসমান ও জমিনের প্রতিটি অনু-পরমাণু পাহারা দিচ্ছেন।",
            whatToBeCarefulAboutBn = "পার্থিব কোনো পরাশক্তিকে ভয় পাওয়া বা আল্লাহর ক্ষমতাকে কোনো কিছুর সাথে তুলনা করা থেকে সতর্ক থাকা।",
            whatCanIPracticeBn = "ফরজ নামাজের পর এবং ঘুমানোর আগে অত্যন্ত ভক্তিভরে আয়াতুল কুরসী তিলাওয়াত করে আল্লাহর হেফাযত কামনা করা।",
            quranSaysBn = "কুরআনের শ্রেষ্ঠতম আয়াত, যা আল্লাহর একত্ববাদ ও মহত্ত্বের পূর্ণাঙ্গ পরিচয় তুলে ধরে।",
            scholarlyInterpretationBn = "সহীহ মুসলিম: রাসূলুল্লাহ ﷺ এটিকে কুরআনের মহানতম আয়াত বলেছেন। যে ব্যক্তি প্রতিটি ফরজ সালাত শেষে এটি পড়ে, তার জান্নাতে প্রবেশের পথে মৃত্যু ছাড়া কোনো বাধা থাকে না।",
            possiblePersonalApplicationBn = "মনে যেকোনো ভয়, আতঙ্ক বা দুঃস্বপ্ন দেখা দিলে আয়াতুল কুরসীর অর্থ স্মরণ করে আল্লাহর আশ্রয়ে আশ্রয় নেওয়া।",
            reflectiveQuestions = listOf(
                "যদি আল্লাহ সর্বক্ষণ সজাগ ও আসমান-জমিনের রক্ষক হন, তবে কেন আমি তুচ্ছ পার্থিব বিষয়ে ভয় পাই?",
                "আমি কি প্রতিদিন ফরজ নামাজের পর নিয়মিত এই মহিমান্বিত আয়াতটি পাঠ করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.WORSHIP to listOf("প্রতি ফরজ নামাজের পর আয়াতুল কুরসী তিলাওয়াত অভ্যাসে পরিণত করুন।"),
                LifeSphere.MINDSET to listOf("ভয় ও উদ্বেগমুক্ত থাকুন, কারণ আসমান-জমিনের স্রষ্টা আপনার সাথে আছেন।")
            ),
            defaultTodayAction = "আজকের ৫ ওয়াক্ত ফরজ সালাতের প্রতিটির পর ধীরস্থিরভাবে আয়াতুল কুরসী পাঠ করুন এবং এর তাওহীদী অর্থের ওপর গভীর ধ্যান করুন।",
            primaryThemes = listOf("তাওহীদ", "সুরক্ষা ও হেফাযত", "আল্লাহর মহিমা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002255.mp3"
        ),

        // 5. Al-Baqarah 2:286 - Burden within capacity and du'a
        AyahActionInsight(
            ayahId = "action_2_286",
            surahNumber = 2,
            ayahNumber = 286,
            surahNameArabic = "سورة البقرة",
            surahNameBangla = "সূরা আল-বাক্বারাহ (২:২৮৬)",
            surahNameEnglish = "Surah Al-Baqarah (The Cow)",
            revelationTypeBn = "মাদানী",
            arabicText = "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ",
            transliterationBn = "লা- ইউকাল্লিফুল্লা-হু নাফসান ইল্লা- উস‘আহা-; লাহা- মা- কাসাবাত ওয়া ‘আলাইহা- মাকতাসাবাত; রাব্বানা- লা- তুআ-খিযনা- ইন নাসীনা- আও আখত্বা’না-; রাব্বানা- ওয়ালা- তাহমিল ‘আলাইনা- ইসরান কামা- হামালতাহূ ‘আলাল্লাযীনা মিন ক্বাবলিনা-; রাব্বানা- ওয়ালা- তুহাম্মিলনা- মা- লা- ত্বা-ক্বাতা লানা- বিহী; ওয়া‘ফু ‘আন্না- ওয়াগফির লানা- ওয়ারহামনা-; আনতা মাওলা-না- ফানসুরনা- ‘আলাল ক্বাওমিল কা-ফিরীন।",
            banglaTranslation = "আল্লাহ কোনো ব্যক্তির ওপর তার সাধ্যের অতিরিক্ত বোঝা চাপিয়ে দেন না। সে যা ভালো অর্জন করেছে তা তার জন্য এবং সে যা মন্দ করেছে তা তার বিরুদ্ধেই যাবে। হে আমাদের রব! আমরা যদি ভুলে যাই বা ভুল করি, তবে আমাদের পাকড়াও করবেন না। হে আমাদের রব! আমাদের ওপর এমন ভারী বোঝা অর্পণ করবেন না যেমন আমাদের পূর্ববর্তীদের ওপর করেছিলেন। হে আমাদের রব! যা বহন করার শক্তি আমাদের নেই, তা আমাদের ওপর চাপাবেন না। আমাদের ক্ষমা করুন, মার্জনা করুন এবং দয়া করুন; আপনিই আমাদের অভিভাবক, অতএব কাফের সম্প্রদায়ের বিরুদ্ধে আমাদের বিজয়ী করুন।",
            englishTranslation = "Allah does not charge a soul except with that within its capacity. It will have what good it has gained, and it will bear what evil it has earned. 'Our Lord, do not impose blame upon us if we have forgotten or erred. Our Lord, and lay not upon us a burden like that which You laid upon those before us. Our Lord, and burden us not with that which we have no ability to bear. And pardon us; and forgive us; and have mercy upon us. You are our protector, so give us victory over the disbelieving people.'",
            whatDoesItTeachBn = "আল্লাহ পরম করুণাময়; মানুষের সীমাবদ্ধতা তিনি জানেন। বর্তমান জীবনের যেকোনো সংকট বা দায়িত্ব মানুষের সহনশীলতার ভেতরেরই একটি অংশ।",
            whatToNoticeBn = "নিজের ভুলের দায় এবং আল্লাহর রহমতের প্রশস্ততা। দোয়াটিতে রয়েছে জীবনের সার্বিক মাগফিরাত ও সাহায্যের নিখুঁত বিন্যাস।",
            whatToBeCarefulAboutBn = "জীবনে কোনো দায়িত্ব কঠিন লাগলে ভেঙে পড়ে বলা যে 'আমি পারছি না', বরং আল্লাহর কাছে রহমত ও শক্তি চাওয়া।",
            whatCanIPracticeBn = "প্রতি রাতে ঘুমানোর পূর্বে সূরা বাকারার শেষ দুই আয়াত পাঠ করা, যা সারা রাতের জন্য মানুষের নিরাপত্তার জন্য যথেষ্ট।",
            quranSaysBn = "সাধ্যের অতীত কোনো বোঝা আল্লাহ কারও ওপর চাপান না।",
            scholarlyInterpretationBn = "সহীহ বুখারী: যে ব্যক্তি রাতে সূরা বাকারার শেষ দুই আয়াত পাঠ করবে, তা তার সকল প্রকার ক্ষতি ও অনিষ্ট থেকে সুরক্ষার জন্য যথেষ্ট হবে।",
            possiblePersonalApplicationBn = "কাজের অতিরিক্ত চাপে হতাশ না হয়ে আল্লাহর ওপর আস্থা রেখে ছোট পদক্ষেপে এগোতে থাকা।",
            reflectiveQuestions = listOf(
                "আমি কি জীবনে কোনো দায়িত্বকে অসাধ্য ভেবে নিজেকে দোষারোপ করছি?",
                "আমি কি রাতে এই রক্ষাকারী আয়াতগুলো নিয়মিত পাঠ করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.DIFFICULT_SITUATIONS to listOf("মনে রাখুন: এই সংকটটি কাটিয়ে ওঠার শক্তি আল্লাহ আপনার ভেতরে দিয়েছেন।"),
                LifeSphere.WORSHIP to listOf("ঘুমানোর আগে প্রতি রাতে এই আয়াতটি পড়ার সুন্নাহ আমল বাস্তবায়ন করুন।")
            ),
            defaultTodayAction = "আজ রাতে বিছানায় যাওয়ার আগে সূরা বাকারার শেষ দুই আয়াত (২৮৫-২৮৬) অর্থসহ মনোযোগের সাথে তিলাওয়াত করুন।",
            primaryThemes = listOf("সহনশীলতা", "ক্ষমা ও রহমত", "রাতের সুরক্ষা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/002286.mp3"
        ),

        // 6. Ali 'Imran 3:173 - Hasbunallah wa Ni'mal Wakeel
        AyahActionInsight(
            ayahId = "action_3_173",
            surahNumber = 3,
            ayahNumber = 173,
            surahNameArabic = "سورة آل عمران",
            surahNameBangla = "সূরা আলে-ইমরান (৩:১৭৩)",
            surahNameEnglish = "Surah Ali 'Imran",
            revelationTypeBn = "মাদানী",
            arabicText = "الَّذِينَ قَالَ لَهُمُ النَّاسُ إِنَّ النَّاسَ قَدْ جَمَعُوا لَكُمْ فَاخْشَوْهُمْ فَزَادَهُمْ إِيمَانًا وَقَالُوا حَسْبُنَا اللَّهُ وَنِعْمَ الْوَكِيلُ",
            transliterationBn = "আল্লাযীনা ক্বা-লা লাহুমুন না-সু ইন্নান না-সা ক্বাদ জামা‘ঊ লাকুম ফাখশাওহুম ফাযা-দাহুম ঈমা-নাঁও ওয়া ক্বা-লূ হাসবুনাল্লা-হু ওয়া নি‘মাল ওয়াকীল।",
            banglaTranslation = "যাদেরকে লোকেরা বলেছিল: নিশ্চয়ই মানুষ তোমাদের বিরুদ্ধে সমবেত হয়েছে, অতএব তাদের ভয় করো; তখন এ কথা তাদের ঈমানকে আরও বাড়িয়ে দিয়েছিল এবং তারা বলেছিল: আল্লাহই আমাদের জন্য যথেষ্ট এবং তিনি কতই না উত্তম কর্মবিধায়ক!",
            englishTranslation = "Those to whom people said, 'Indeed, the people have gathered against you, so fear them.' But it only increased them in faith, and they said, 'Sufficient for us is Allah, and [He is] the best Disposer of affairs.'",
            whatDoesItTeachBn = "পৃথিবীর সকল শক্তি এক হয়েও কারও কোনো ক্ষতি করতে পারে না যদি আল্লাহ তা না চান। মানুষের ভয় ঈমানদারের ভরসাকে আরও মজবুত করে।",
            whatToNoticeBn = "ভয় ও হুমকির মুখে সাহাবায়ে কেরাম ভেঙে পড়েননি, বরং আল্লাহর ওপর তাওয়াক্কুল করে নির্ভীক হয়েছিলেন।",
            whatToBeCarefulAboutBn = "লোকভয়, সামাজিক চাপ বা সংকটের মুখে আদর্শ ও সত্য থেকে বিচ্যুত হওয়া থেকে সতর্ক থাকা।",
            whatCanIPracticeBn = "আজ যেকোনো আতঙ্ক, মামলা, শত্রুতা বা ভবিষ্যৎ উদ্বেগে জবান দিয়ে অত্যন্ত দৃঢ়তার সাথে 'হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকিল' পাঠ করা।",
            quranSaysBn = "ঈমানদারদের চিরন্তন আশ্রয়: আল্লাহই আমাদের জন্য যথেষ্ট।",
            scholarlyInterpretationBn = "সহীহ বুখারী: ইব্রাহিম (আ.)-কে যখন আগুনে নিক্ষেপ করা হয়েছিল, তিনি এই বাক্যটি বলেছিলেন; আর নবীজী ﷺ-ও এই বাক্য বলে শত্রুর ভয় কাটিয়েছিলেন।",
            possiblePersonalApplicationBn = "ভবিষ্যতের অনিশ্চয়তা বা ক্যারিয়ারের দুশ্চিন্তা এলে আল্লাহর কাছে বিষয়গুলো সঁপে দেওয়া।",
            reflectiveQuestions = listOf(
                "কোন ব্যক্তি বা পরিস্থিতি আমাকে সবচেয়ে বেশি শঙ্কিত করছে?",
                "আমি কি সত্যিই আল্লাহকে আমার জীবনের শ্রেষ্ঠ কর্মবিধায়ক মানতে পারছি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MINDSET to listOf("ভয় ও শঙ্কা ঝেড়ে ফেলে আল্লাহর নির্ভরযোগ্যতার ওপর অবিচল ভরসা রাখুন।"),
                LifeSphere.WORK to listOf("অফিসের রাজনীতি বা মানুষের অন্যায্য আচরণে বিচলিত না হয়ে সত্যের ওপর থাকুন।")
            ),
            defaultTodayAction = "আজকের দিনে যখনই কোনো অজানা ভয় বা মানুষের চাপের মুখোমুখি হবেন, অন্তত ৭ বার অন্তরের বিশ্বাস নিয়ে 'হাসবুনাল্লাহু ওয়া নি'মাল ওয়াকিল' পড়ুন।",
            primaryThemes = listOf("তাওয়াক্কুল", "নির্ভীক ঈমান", "আল্লাহর ওপর ভরসা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/003173.mp3"
        ),

        // 7. An-Nisa 4:135 - Standing firmly for Justice
        AyahActionInsight(
            ayahId = "action_4_135",
            surahNumber = 4,
            ayahNumber = 135,
            surahNameArabic = "سورة النساء",
            surahNameBangla = "সূরা আন-নিসা (৪:১৩৫)",
            surahNameEnglish = "Surah An-Nisa (The Women)",
            revelationTypeBn = "মাদানী",
            arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُونُوا قَوَّامِينَ بِالْقِسْطِ شُهَدَاءَ لِلَّهِ وَلَوْ عَلَىٰ أَنفُسِكُمْ أَوِ الْوَالِدَيْنِ وَالْأَقْرَبِينَ ۚ إِن يَكُنْ غَنِيًّا أَوْ فَقِيرًا فَاللَّهُ أَوْلَىٰ بِهِمَا ۖ فَلَا تَتَّبِعُوا الْهَوَىٰ أَن تَعْدِلُوا",
            transliterationBn = "ইয়া-আইয়্যুহাল্লাযীনা আ-মানূ কূনূ ক্বাওওয়া-মীনা বিলক্বিসত্বি শুহাদা-আ লিল্লা-হি ওয়ালাও ‘আলা- আনফুসিকুম আওয়িল ওয়া-লিদাইনি ওয়াল আক্বরাবীন; ইঁই ইয়াকুন গানিয়্যান আও ফাক্বীরান ফাল্লা-হু আওলা- বিহিমা-; ফালা- তাত্তাবি‘উল হাওয়া- আন তা‘দিলূ।",
            banglaTranslation = "হে মুমিনগণ! তোমরা ন্যায়ের ওপর দৃঢ়ভাবে প্রতিষ্ঠিত থাকো, আল্লাহর জন্য সাক্ষ্যদানকারী হও—যদিও তা তোমাদের নিজেদের অথবা পিতা-মাতা ও নিকটাত্মীয়দের বিরুদ্ধে যায়। সে ধনী হোক বা গরীব, আল্লাহ তাদের উভয়ের চেয়েও বড় অভিভাবক। অতএব তোমরা প্রবৃত্তি বা আবেগের অনুসরণ করে ন্যায়বিচার বর্জন করো না।",
            englishTranslation = "O you who have believed, be persistently standing firm in justice, witnesses for Allah, even if it be against yourselves or parents and relatives. Whether one is rich or poor, Allah is more worthy of both. So follow not [personal] inclination, lest you not be just.",
            whatDoesItTeachBn = "ইনসাফ ও সততার মানদণ্ডে কোনো আপস নেই। সত্যের সাক্ষ্য নিজের বা পরিবারের বিরুদ্ধে গেলেও তা দৃঢ়ভাবে প্রকাশ করতে হবে।",
            whatToNoticeBn = "পক্ষপাতিত্ব, স্বজনপ্রীতি বা আবেগের কারণে কোনো মানুষের হক নষ্ট করা মহাপাপ। ধনী-গরীব নির্বিশেষে আইনের চোখে সবাই সমান।",
            whatToBeCarefulAboutBn = "নিকটাত্মীয়কে বাঁচাতে মিথ্যা সাক্ষী দেওয়া বা নিজের দোষ ঢাকতে অন্যকে দায়ী করা থেকে বিরত থাকা।",
            whatCanIPracticeBn = "আজ পরিবার বা কর্মক্ষেত্রে কোনো বিতর্ক তৈরি হলে নিজের স্বার্থ না দেখে সম্পূর্ণ নিরপেক্ষ ও সত্য কথা বলা।",
            quranSaysBn = "আল্লাহর সন্তুষ্টির জন্য সবসময় ইনসাফ ও সত্য সাক্ষ্যের ওপর অটল থাকা বাধ্যতামূলক।",
            scholarlyInterpretationBn = "তাফসীর মা'আরিফুল কুরআন: ইসলামে ন্যায়বিচার সর্বজনীন। আত্মীয়তার মায়া যেন সুবিচার প্রতিষ্ঠায় বাধা না হয়।",
            possiblePersonalApplicationBn = "নিজের কোনো ভুল হলে তা লুকানোর চেষ্টা না করে সততার সাথে স্বীকার করে নেওয়া।",
            reflectiveQuestions = listOf(
                "আমি কি কখনো নিজের পরিবারের পক্ষে থাকতে গিয়ে কোনো অন্যায্য কাজ সমর্থন করেছি?",
                "নিজের ভুল অকপটে স্বীকার করার মতো আত্মিক সাহস কি আমার আছে?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.CHARACTER to listOf("সত্যবাদিতা ও সততাকে জীবনের সর্বোচ্চ নীতি হিসেবে ধারণ করুন।"),
                LifeSphere.WORK to listOf("অফিসের হিসাব বা সিদ্ধান্তে কোনো স্বজনপ্রীতি না করে শতভাগ ইনসাফ বজায় রাখুন।")
            ),
            defaultTodayAction = "আজ কোনো ভুল হয়ে থাকলে তা নির্দ্বিধায় স্বীকার করুন এবং যেকোনো বিবাদে কারও প্রতি পক্ষপাত না করে নিরপেক্ষ সত্য রায় দিন।",
            primaryThemes = listOf("ইনসাফ ও ন্যায়বিচার", "সত্য সাক্ষ্য", "সততা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/004135.mp3"
        ),

        // 8. At-Tawbah 9:51 - Divine Destiny
        AyahActionInsight(
            ayahId = "action_9_51",
            surahNumber = 9,
            ayahNumber = 51,
            surahNameArabic = "سورة التوبة",
            surahNameBangla = "সূরা আত-তাওবাহ (৯:৫১)",
            surahNameEnglish = "Surah At-Tawbah",
            revelationTypeBn = "মাদানী",
            arabicText = "قُل لَّن يُصِيبَنَا إِلَّا مَا كَتَبَ اللَّهُ لَنَا هُوَ مَوْلَانَا ۚ وَعَلَى اللَّهِ فَلْيَتَوَكَّلِ الْمُؤْمِنُونَ",
            transliterationBn = "ক্বুল লাইঁ ইউসীবানা- ইল্লা- মা- কাতাবাল্লা-হু লানা- হুওয়া মাওলা-না-, ওয়া ‘আলাল্লা-হি ফালইয়াতাওয়াক্কালিল মু’মিনূন।",
            banglaTranslation = "বলুন: আমাদের কিছুই স্পর্শ করবে না যা আল্লাহ আমাদের জন্য নির্ধারণ করে দেননি। তিনিই আমাদের অভিভাবক, আর মুমিনদের কেবল আল্লাহর ওপরই ভরসা করা উচিত।",
            englishTranslation = "Say, 'Never will we be struck except by what Allah has decreed for us; He is our protector.' And upon Allah let the believers rely.",
            whatDoesItTeachBn = "জীবনের প্রতিটি ঘটনা আল্লাহর পূর্বনির্ধারিত তাকদীরের অধীন। যা ঘটে গেছে তা কখনোই এড়ানো সম্ভব ছিল না, তাই আফসোস না করে সামনের দিকে এগোতে হবে।",
            whatToNoticeBn = "আল্লাহ যা নির্ধারণ করেছেন তা বান্দার মঙ্গলের জন্যই, যদিও তাৎক্ষণিকভাবে মানুষ তা বুঝতে না পারে।",
            whatToBeCarefulAboutBn = "কোনো ব্যর্থতায় 'যদি এমন করতাম তবে এমন হতো না' বলে শয়তানের আফসোসের ফাঁদে পা দেওয়া থেকে সতর্ক থাকা।",
            whatCanIPracticeBn = "আজ অতীতের কোনো ভুল সিদ্ধান্ত বা ক্ষতির কষ্ট মনে এলে বলুন: 'ক্বাদারুল্লাহু ওয়া মা শা-আ ফা'আলা' (আল্লাহ যা নির্ধারণ করেছেন তাই ঘটেছে)।",
            quranSaysBn = "আল্লাহ যা তাকদীরে লিখে রেখেছেন কেবল তাই ঘটবে; তিনিই আমাদের পরম অভিভাবক।",
            scholarlyInterpretationBn = "তাফসীর আস-সা'দী: তাকদীরে খাঁটি বিশ্বাস মানুষকে হতাশা ও অহংকার—উভয় মারাত্মক ব্যাধি থেকে মুক্ত রাখে।",
            possiblePersonalApplicationBn = "পছন্দের কিছু না পেলে মনে করা যে নিশ্চয় এতে আল্লাহর কোনো বৃহত্তর কল্যাণ লুকিয়ে আছে।",
            reflectiveQuestions = listOf(
                "অতীতের কোন ঘটনাটি আমাকে এখনো অপরাধবোধ বা আফসোসে কষ্ট দেয়?",
                "আমি কি বিশ্বাস করি আল্লাহর সিদ্ধান্তই আমার জন্য সর্বোত্তম?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MINDSET to listOf("অতীত নিয়ে অতিরিক্ত অনুশোচনা ছেড়ে বর্তমান কর্মে মনোনিবেশ করুন।"),
                LifeSphere.DIFFICULT_SITUATIONS to listOf("হৃদয়ে বিশ্বাস রাখুন: আল্লাহ আপনাকে কোনো বিপদে একা ফেলে দেবেন না।")
            ),
            defaultTodayAction = "অতীতের কোনো আফসোস বা অপূর্ণ আশা নিয়ে মন খারাপ থাকলে আজ তা সম্পূর্ণভাবে আল্লাহর তাকদীরের ওপর সন্তুষ্ট হয়ে ছেড়ে দিন।",
            primaryThemes = listOf("তাকদীর", "আস্থা ও সন্তুষ্টি", "অপ্রয়োজনীয় আফসোস বর্জন"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/009051.mp3"
        ),

        // 9. Hud 11:6 - Guarantee of Rizq
        AyahActionInsight(
            ayahId = "action_11_6",
            surahNumber = 11,
            ayahNumber = 6,
            surahNameArabic = "سورة هود",
            surahNameBangla = "সূরা হুদ (১১:৬)",
            surahNameEnglish = "Surah Hud",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَمَا مِن دَابَّةٍ فِي الْأَرْضِ إِلَّا عَلَى اللَّهِ رِزْقُهَا وَيَعْلَمُ مُسْتَقَرَّهَا وَمُسْتَوْدَعَهَا ۚ كُلٌّ فِي كِتَابٍ مُّبِينٍ",
            transliterationBn = "ওয়ামা- মিন দা-ব্বাতিন ফিল আরদ্বি ইল্লা- ‘আলাল্লা-হি রিযকুহা- ওয়া ইয়া‘লামু মুস্তাক্বার্রাহা- ওয়া মুস্তাওদা‘আহা-; কুল্লুন ফী কিতা-বিম মুবীন।",
            banglaTranslation = "আর জমিনে বিচরণকারী এমন কোনো প্রাণী নেই যার রিযিকের দায়িত্ব আল্লাহর ওপর নেই। আর তিনি জানেন তাদের থাকার জায়গা ও সমাহিত হওয়ার স্থান। সবকিছুই এক স্পষ্ট কিতাবে লিপিবদ্ধ আছে।",
            englishTranslation = "And there is no creature on earth but that upon Allah is its provision, and He knows its place of dwelling and place of storage. All is in a clear register.",
            whatDoesItTeachBn = "রিযিকের মালিক কোনো মানুষ বা প্রতিষ্ঠান নয়; স্বয়ং মহান আল্লাহ প্রতিটি সৃষ্টির আহারের ব্যবস্থা করেন। এই বিশ্বাস আর্থিক উদ্বেগ দূর করে।",
            whatToNoticeBn = "রিযিক শুধু টাকাপয়সা নয়; স্বাস্থ্য, নেক পরিবার, আত্মিক শান্তি ও সৎসঙ্গ—সবই আল্লাহর রিযিক।",
            whatToBeCarefulAboutBn = "ভবিষ্যতের রিযিক হারানোর ভয়ে হারাম উপার্জনে লিপ্ত হওয়া বা কৃপণতা করা থেকে সাবধান থাকা।",
            whatCanIPracticeBn = "আজ হালাল পথে আন্তরিক চেষ্টা চালানো এবং অতিরিক্ত আয়ের জন্য কোনো অবৈধ বা সন্দেহজনক পথকে দৃঢ়ভাবে প্রত্যাখ্যান করা।",
            quranSaysBn = "পৃথিবীর প্রতিটি ক্ষুদ্রাতিক্ষুদ্র প্রাণীর রিযিকের গ্যারান্টি আল্লাহর দায়িত্বে রয়েছে।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: পাখি সকালে খালি পেটে বাসা থেকে বের হয়ে সন্ধ্যায় ভরা পেটে ফিরে আসে। আল্লাহ যাকে সৃষ্টি করেছেন তার খাবারও প্রস্তুত রেখেছেন।",
            possiblePersonalApplicationBn = "আর্থিক টানাপোড়েনের সময় মানুষের কাছে হাত না পেতে রিযিকের মালিক আল্লাহর কাছেই চাওয়া।",
            reflectiveQuestions = listOf(
                "আমি কি রিযিক নিয়ে অতিরিক্ত দুশ্চিন্তা করে রাতের ঘুম নষ্ট করছি?",
                "আমি কি নিশ্চিত যে আমার ঘরে আসা প্রতিটি টাকা শতভাগ হালাল?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MONEY to listOf("হালাল উপার্জনে সন্তুষ্ট থাকুন এবং হারাম আয় থেকে সম্পূর্ণ দূরে থাকুন।"),
                LifeSphere.MINDSET to listOf("মনে রাখবেন: নির্ধারিত রিযিক সম্পূর্ণ ভোগ না করা পর্যন্ত কোনো মানুষের মৃত্যু হবে না।")
            ),
            defaultTodayAction = "আজ আপনার জীবিকার জন্য আল্লাহর শুকরিয়া আদায় করুন এবং সামান্য হলেও কিছু দান করে রিযিকে বরকতের দোয়া করুন।",
            primaryThemes = listOf("রিযিক ও বরকত", "তাওয়াক্কুল", "হালাল উপার্জন"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/011006.mp3"
        ),

        // 10. Yusuf 12:87 - Do not despair of Allah's mercy
        AyahActionInsight(
            ayahId = "action_12_87",
            surahNumber = 12,
            ayahNumber = 87,
            surahNameArabic = "سورة يوسف",
            surahNameBangla = "সূরা ইউসুফ (১২:৮৭)",
            surahNameEnglish = "Surah Yusuf (Joseph)",
            revelationTypeBn = "মাক্কী",
            arabicText = "يَا بَنِيَّ اذْهَبُوا فَتَحَسَّسُوا مِن يُوسُفَ وَأَخِيهِ وَلَا تَيْأَسُوا مِن رَّوْحِ اللَّهِ ۖ إِنَّهُ لَا يَيْأَسُ مِن رَّوْحِ اللَّهِ إِلَّا الْقَوْمُ الْكَافِرُونَ",
            transliterationBn = "ইয়া- বানিইয়্যায হাবূ ফাতাহাস্সাসূ মিঁই ইউ-সুফা ওয়া আখীহি ওয়ালা- তাই’আসূ মির রাওহিল্লা-হি; ইন্নাহূ লা- ইয়াই’আসু মির রাওহিল্লা-হি ইল্লাল ক্বাওমুল কা-ফিরূন।",
            banglaTranslation = "হে আমার ছেলেরা! তোমরা যাও এবং ইউসুফ ও তার ভাইয়ের অনুসন্ধান করো এবং আল্লাহর রহমত থেকে কখনো নিরাশ হয়ো না। নিশ্চয়ই আল্লাহর রহমত থেকে কাফের সম্প্রদায় ছাড়া কেউ নিরাশ হয় না।",
            englishTranslation = "O my sons, go and find out about Joseph and his brother and despair not of relief from Allah. Indeed, no one despairs of relief from Allah except the disbelieving people.",
            whatDoesItTeachBn = "হতাশা ও নিরাশা মুমিনের চরিত্রে থাকতে পারে না। পরিস্থিতি যত অন্ধকারই মনে হোক না কেন, আল্লাহর কুদরত মুহূর্তেই অসম্ভবকে সম্ভব করতে পারে।",
            whatToNoticeBn = "হযরত ইয়াকুব (আ.) বহু বছর সন্তান হারানোর গভীর বেদনায় কেঁদে অন্ধ হয়ে গিয়েছিলেন, তবুও আল্লাহর রহমতের ওপর তাঁর আশা ছিল অটুট।",
            whatToBeCarefulAboutBn = "জীবনে কোনো সংকট দীর্ঘায়িত হলে 'আমার ভাগ্যে আর ভালো কিছু নেই' এমন নিরাশাজনক বাক্য মুখে উচ্চারণ করা থেকে বেঁচে থাকা।",
            whatCanIPracticeBn = "আজ কোনো হতাশ বন্ধু বা পরিবারের সদস্যকে আশার বাণী শোনানো এবং নিজে কোনো কঠিন সমস্যা থাকলে মনে প্রাণে আল্লাহর দয়ার ওপর বিশ্বাস রাখা।",
            quranSaysBn = "আল্লাহর রহমত ও দয়া থেকে কখনোই নিরাশ হওয়া যাবে না।",
            scholarlyInterpretationBn = "তাফসীর আস-সা'দী: আশা হলো ঈমানের প্রাণ। মানুষ যখন আল্লাহর দয়ার ওপর আশা রাখে, তখন তার অন্তরে পরীক্ষা সহ্য করার সীমাহীন শক্তি জন্ম নেয়।",
            possiblePersonalApplicationBn = "আজ যেকোনো নেতিবাচক চিন্তা এলে তার বিপরীতে আল্লাহর অসংখ্য নিয়ামতের দিকে দৃষ্টি দেওয়া।",
            reflectiveQuestions = listOf(
                "কোন বিষয়ে আমি প্রায় আশা হারিয়ে ফেলতে বসেছি?",
                "আমি কি ভুলে গেছি যে আল্লাহর কুদরতের কোনো সীমা নেই?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MINDSET to listOf("হতাশাকে মুছে ফেলে আল্লাহর অফুরন্ত করুণা ও অলৌকিক সাহায্যের ওপর আশা রাখুন।"),
                LifeSphere.DIFFICULT_SITUATIONS to listOf("অন্ধকার রাতের পরেই ভোরের আলো ফোটে—ধৈর্য ধারণ করুন।")
            ),
            defaultTodayAction = "আজ এমন কাউকে ফোন বা মেসেজ দিন যিনি দুঃসময়ে আছেন, তাকে পরম আশার বাণী দিয়ে সান্ত্বনা দিন।",
            primaryThemes = listOf("আশা ও ইতিবাচকতা", "হতাশা বর্জন", "আল্লাহর অসীম রহমত"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/012087.mp3"
        ),

        // 11. Ibrahim 14:7 - Gratitude increases blessings
        AyahActionInsight(
            ayahId = "action_14_7",
            surahNumber = 14,
            ayahNumber = 7,
            surahNameArabic = "سورة إبراهيم",
            surahNameBangla = "সূরা ইবরাহিম (১৪:৭)",
            surahNameEnglish = "Surah Ibrahim",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَإِذْ تَأَذَّنَ رَبُّكُمْ لَئِن شَكَرْتُمْ لَأَزِيدَنَّكُمْ ۖ وَلَئِن كَفَرْتُمْ إِنَّ عَذَابِي لَشَدِيدٌ",
            transliterationBn = "ওয়া ইয তা’আয্যানা রাব্বুকুম লা’ইন শাকারতুম লা’আযীদান্নাকুম ওয়ালা’ইন কাফারতুম ইন্না ‘আযা-বী লাশাদীদ।",
            banglaTranslation = "আর স্মরণ করো, যখন তোমাদের রব ঘোষণা করেছিলেন: যদি তোমরা কৃতজ্ঞতা প্রকাশ করো, তবে আমি অবশ্যই তোমাদের বাড়িয়ে দেব; আর যদি অকৃতজ্ঞ হও, তবে নিশ্চয়ই আমার শাস্তি অতি কঠোর।",
            englishTranslation = "And [remember] when your Lord proclaimed, 'If you are grateful, I will surely increase you [in favor]; but if you show ingratitude, indeed, My punishment is severe.'",
            whatDoesItTeachBn = "শুকরিয়া বা কৃতজ্ঞতাবোধ নেয়ামতকে চিরস্থায়ী করে এবং আরও বৃদ্ধি করে। আর অকৃতজ্ঞতা প্রাপ্ত নেয়ামতকেও কেড়ে নেয়।",
            whatToNoticeBn = "কৃতজ্ঞতা কেবল মুখের বুলি নয়, বরং প্রাপ্ত সম্পদ ও স্বাস্থ্যকে আল্লাহর সন্তুষ্টির কাজে ব্যবহার করাই প্রকৃত শুকরিয়া।",
            whatToBeCarefulAboutBn = "সর্বদা না-পাওয়া জিনিসের জন্য অভিযোগ করা এবং ইতিমধ্যে যেসব লাখ লাখ নেয়ামত ভোগ করছি তা ভুলে থাকা থেকে বিরত থাকা।",
            whatCanIPracticeBn = "আজ সারাদিনে নিজের চোখ, কান, স্বাস্থ্য, পরিবার ও ঈমানের কথা মনে করে অন্তত ১০০ বার 'আলহামদুলিল্লাহ' বলা।",
            quranSaysBn = "শুকরিয়া আদায় করলে আল্লাহ অবশ্যই নেয়ামত বৃদ্ধি করে দেন।",
            scholarlyInterpretationBn = "তাফসীর কুরতুবী: হাসান বসরী (রহ.) বলেছেন, যে ব্যক্তি আল্লাহর নেয়ামতের শুকরিয়া আদায় করে, আল্লাহ তার নেয়ামতের ওপর হেফাজতের দুর্গ নির্মাণ করেন।",
            possiblePersonalApplicationBn = "আজ পরিবারের কেউ বা সহকর্মী কোনো উপকার করলে তাকেও মন খুলে ধন্যবাদ জানানো।",
            reflectiveQuestions = listOf(
                "আমি কি পাওয়ার চেয়ে না-পাওয়ার হিসেব মেলাতে গিয়ে বেশি কষ্ট পাই?",
                "আজকের দিনে আল্লাহর কোন নেয়ামতটি না থাকলে আমার জীবন থমকে যেত?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.CHARACTER to listOf("অভিযোগের স্বভাব পরিহার করে কৃতজ্ঞচিত্ত ব্যক্তিত্ব গড়ে তুলুন।"),
                LifeSphere.FAMILY to listOf("পরিবারের ছোট ছোট ত্যাগের জন্য জীবনসঙ্গী ও সন্তানদের ধন্যবাদ দিন।")
            ),
            defaultTodayAction = "আজ একটি কাগজে বা মনে মনে ৫টি এমন বড় নেয়ামতের তালিকা তৈরি করুন যার জন্য আপনি আল্লাহর কাছে আন্তরিক কৃতজ্ঞ।",
            primaryThemes = listOf("শুকরিয়া ও কৃতজ্ঞতা", "নেয়ামত বৃদ্ধি", "অভিযোগহীনতা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/014007.mp3"
        ),

        // 12. Al-Isra 17:23-24 - Parents kindness
        AyahActionInsight(
            ayahId = "action_17_23",
            surahNumber = 17,
            ayahNumber = 23,
            surahNameArabic = "سورة الإسراء",
            surahNameBangla = "সূরা আল-ইসরা (১৭:২৩-২৪)",
            surahNameEnglish = "Surah Al-Isra",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَقَضَىٰ رَبُّكَ أَلَّا تَعْبُدُوا إِلَّا إِيَّاهُ وَبِالْوَالِدَيْنِ إِحْسَانًا ۚ إِمَّا يَبْلُغَنَّ عِندَكَ الْكِبَرَ أَحَدُهُمَا أَوْ كِلَاهُمَا فَلَا تَقُل لَّهُمَا أُفٍّ وَلَا تَنْهَرْهُمَا وَقُل لَّهُمَا قَوْلًا كَرِيمًا ۝ وَاخْفِضْ لَهُمَا جَنَاحَ الذُّلِّ مِنَ الرَّحْمَةِ وَقُل رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            transliterationBn = "ওয়া ক্বাদ্বা- রাব্বুকা আল্লা- তা‘বুদূ ইল্লা- ইয়্যা-হু ওয়াবিল ওয়া-লিদাইনি ইহসা-না-; ইম্মা- ইয়াবলুগান্না ‘ইনদাকাল কিবারা আহাদুহুমা- আও কিলা-হুমা- ফালা- তাকুল্লাহুমা- উফফিঁও ওয়ালা- তানহারহুমা- ওয়া কুল্লাহুমা- ক্বাওলান কারীমা-। ওয়াখফিদ লাহূমা- জানা-হায যুল্লি মিনার রাহমাতি ওয়া কুর রাব্বির হামহুমা- কামা- রাব্বায়া-নী সাগীরা-।",
            banglaTranslation = "আর আপনার রব নির্দেশ দিয়েছেন যে, তোমরা তিনি ছাড়া আর কারও ইবাদত করবে না এবং পিতা-মাতার সাথে সর্বোত্তম সদ্ব্যবহার করবে। তাদের একজন বা উভয়েই যদি তোমার জীবদ্দশায় বার্ধক্যে উপনীত হয়, তবে তাদেরকে 'উহ' পর্যন্ত বলবে না, তাদেরকে ধমক দেবে না এবং তাদের সাথে সম্মানজনক কথা বলবে। আর ভালোবাসায় তাদের সামনে বিনয়ের ডানা বিছিয়ে দাও এবং বলো: হে আমার রব! তাদের প্রতি দয়া করুন যেভাবে শৈশবে তারা আমাকে লালন-পালন করেছেন।",
            englishTranslation = "And your Lord has decreed that you not worship except Him, and to parents, good treatment. Whether one or both of them reach old age [while] with you, say not to them [so much as], 'uff,' and do not repel them but speak to them a noble word. And lower to them the wing of humility out of mercy and say, 'My Lord, have mercy upon them as they brought me up [when I was] small.'",
            whatDoesItTeachBn = "তাওহীদের পরেই পিতা-মাতার হক্ব সবচেয়ে বড় ফরজ। তাঁদের বার্ধক্যে সামান্যতম বিরক্তি প্রকাশ না করে পরম শ্রদ্ধায় তাঁদের সেবা করা জান্নাতের শ্রেষ্ঠ সিঁড়ি।",
            whatToNoticeBn = "আল্লাহ 'উহ' বলাকেও নিষেধ করেছেন। বয়স বাড়লে পিতা-মাতা শিশুর মতো সংবেদনশীল হন, তাই মুখের প্রতিটি শব্দে সম্মান রাখা অপরিহার্য।",
            whatToBeCarefulAboutBn = "কাজের ব্যস্ততায় পিতা-মাতাকে অবহেলা করা, তাঁদের কথার জবাবে মেজাজ দেখানো বা পরোয়া না করা থেকে সাবধান থাকা।",
            whatCanIPracticeBn = "আজ মা-বাবার কাছে গিয়ে বা ফোনে অত্যন্ত মিষ্টি ভাষায় কথা বলা, তাঁদের কোনো বিশেষ ইচ্ছা পূরণ করা এবং তাঁদের জন্য দোয়া করা।",
            quranSaysBn = "পিতা-মাতার সাথে সর্বোত্তম ব্যবহার এবং তাঁদের জন্য রহমতের দোয়া করার স্পষ্ট ঐশী আদেশ।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: পিতা-মাতার সেবা হলো কবুল হজের সমতুল্য। পিতা জান্নাতের মধ্যবর্তী দরজা।",
            possiblePersonalApplicationBn = "পিতা-মাতা দুনিয়াতে না থাকলে তাঁদের মাগফিরাতের জন্য সাদাকাহ করা এবং এই বিশেষ দোয়াটি প্রতিদিন পাঠ করা।",
            reflectiveQuestions = listOf(
                "আমার কোনো কথা বা আচরণে কি আমার মা-বাবা সম্প্রতি অন্তরে কষ্ট পেয়েছেন?",
                "আমি কি প্রতিদিন নিয়মিত তাঁদের মাগফিরাতের জন্য দোয়া করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.FAMILY to listOf("পিতা-মাতার শারীরিক ও মানসিক যত্নে সময় দিন এবং তাঁদের কথা মনোযোগ দিয়ে শুনুন।"),
                LifeSphere.SPEECH to listOf("পিতা-মাতার সামনে নিজের গলার আওয়াজ নিচু রাখুন এবং নম্র ভাষায় কথা বলুন।")
            ),
            defaultTodayAction = "আজ পিতা-মাতাকে ফোন করে বা সরাসরি জড়িয়ে ধরে কুশল জিজ্ঞেস করুন এবং তাঁদের জন্য 'রাব্বির হামহুমা...' দোয়াটি অন্তত ৫ বার পাঠ করুন।",
            primaryThemes = listOf("পিতা-মাতার সেবা", "পারিবারিক সদ্ব্যবহার", "বিনম্র ভাষা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/017023.mp3"
        ),

        // 13. Al-Isra 17:37 - Humility vs Arrogance
        AyahActionInsight(
            ayahId = "action_17_37",
            surahNumber = 17,
            ayahNumber = 37,
            surahNameArabic = "سورة الإسراء",
            surahNameBangla = "সূরা আল-ইসরা (১৭:৩৭)",
            surahNameEnglish = "Surah Al-Isra",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَلَا تَمْشِ فِي الْأَرْضِ مَرَحًا ۖ إِنَّكَ لَن تَخْرِقَ الْأَرْضَ وَلَن تَبْلُغَ الْجِبَالَ طُولًا",
            transliterationBn = "ওয়ালা- তামশি ফিল আরদ্বি মারাহা-; ইন্নাকা লান তাখরিক্বাল আরদ্বা ওয়ালান তাবলুগাল জিবা-লা ত্বূলা-।",
            banglaTranslation = "আর জমিনে অহংকারভরে বিচরণ করো না; নিশ্চয় তুমি কখনো জমিনকে বিদীর্ণ করতে পারবে না এবং উচ্চতায় পাহাড়সম পৌঁছাতেও পারবে না।",
            englishTranslation = "And do not walk upon the earth exultantly. Indeed, you will never tear the earth [apart], and you will never reach the mountains in height.",
            whatDoesItTeachBn = "মানুষের ক্ষমতা অতি নগণ্য। অহংকার ও দম্ভ কেবল মানুষের পতন ডেকে আনে, আর বিনম্রতাই প্রকৃত সম্মানের প্রতীক।",
            whatToNoticeBn = "শারীরিক হাঁটাচলা ও অঙ্গভঙ্গিতেও যেন অহংকারের প্রকাশ না ঘটে। মাটির মানুষ মাটির ওপর বিনম্রভাবে পা ফেলবে।",
            whatToBeCarefulAboutBn = "নিজের পদবি, বংশ, জ্ঞান বা সৌন্দর্য নিয়ে অন্য কাউকে তুচ্ছ বা নিচু চোখে দেখা থেকে সম্পূর্ণ দূরে থাকা।",
            whatCanIPracticeBn = "আজ যেকোনো অধস্তন বা সাধারণ মানুষকে প্রথমে সালাম দেওয়া এবং বিনয়ের সাথে হাসিমুখে কথা বলা।",
            quranSaysBn = "অহংকারভরে জমিনে বিচরণ করা সম্পূর্ণরূপে নিষিদ্ধ।",
            scholarlyInterpretationBn = "তাফসীর আস-সা'দী: অহংকার সত্যকে প্রত্যাখ্যান করা এবং মানুষকে অবজ্ঞা করার নাম। যে আল্লাহর জন্য বিনয়ী হয়, আল্লাহ তাকে সম্মান বাড়িয়ে দেন।",
            possiblePersonalApplicationBn = "সামাজিক মাধ্যমে নিজের প্রাচুর্য বা বড়াই প্রদর্শন করা থেকে বিরত থাকা।",
            reflectiveQuestions = listOf(
                "আমার চালচলন বা পোশাকে কি কোনো অহংকারের ভাব প্রকাশ পায়?",
                "আমি কি সাধারণ মানুষের সাথে সহজে মিশতে পারি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.CHARACTER to listOf("সকল মানুষের সাথে বিনম্র ও নিরহংকার আচরণ বজায় রাখুন।"),
                LifeSphere.SPEECH to listOf("নিজের প্রশংসা করা বর্জন করুন এবং অন্যের ভালো গুণের প্রশংসা করুন।")
            ),
            defaultTodayAction = "আজ এমন কাউকে আগে বাড়িয়ে সালাম দিন এবং সম্মান দেখান যাকে সাধারণত সমাজ কম গুরুত্ব দেয়।",
            primaryThemes = listOf("বিনয় ও নম্রতা", "অহংকার বর্জন", "আখলাক"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/017037.mp3"
        ),

        // 14. An-Nur 24:22 - Pardoning others to gain Allah's forgiveness
        AyahActionInsight(
            ayahId = "action_24_22",
            surahNumber = 24,
            ayahNumber = 22,
            surahNameArabic = "سورة النور",
            surahNameBangla = "সূরা আন-নূর (২৪:২২)",
            surahNameEnglish = "Surah An-Nur (The Light)",
            revelationTypeBn = "মাদানী",
            arabicText = "وَلَا يَأْتَلِ أُولُو الْفَضْلِ مِنكُمْ وَالسَّعَةِ أَن يُؤْتُوا أُولِي الْقُرْبَىٰ وَالْمَسَاكِينَ وَالْمُهَاجِرِينَ فِي سَبِيلِ اللَّهِ ۖ وَلْيَعْفُوا وَلْيَصْفَحُوا ۗ أَلَا تُحِبُّونَ أَن يَغْفِرَ اللَّهُ لَكُمْ ۗ وَاللَّهُ غَفُورٌ رَّحِيمٌ",
            transliterationBn = "ওয়ালা- ইয়া’তালি উলুল ফাদ্বলি মিনকুম ওয়াস সা‘আতি আইঁ ইউ’তূ উলিল ক্বুরবা- ওয়াল মাসা-কীনা ওয়াল মুহা-জিরীনা ফী সাবীলিল্লা-হ; ওয়ালইয়া‘ফূ ওয়ালইয়াসফাহূ; আলা- তুহিব্বূনা আইঁ ইয়াগফিরাল্লা-হু লাকুম? ওয়াল্লা-হু গাফূরুর রাহীম।",
            banglaTranslation = "আর তোমাদের মধ্যে যারা মর্যাদাবান ও বিত্তশালী, তারা যেন নিকটাত্মীয়, মিসকিন ও আল্লাহর পথে হিজরতকারীদের কিছু না দেওয়ার শপথ না করে; বরং তারা যেন তাদের ক্ষমা করে ও তাদের দোষ উপেক্ষা করে। তোমরা কি চাও না যে আল্লাহ তোমাদের ক্ষমা করুন? আর আল্লাহ পরম ক্ষমাশীল, অতি দয়ালু।",
            englishTranslation = "And let not those of virtue among you and wealth swear not to give [aid] to their relatives and the needy and the emigrants for the cause of Allah, and let them pardon and overlook. Would you not like that Allah should forgive you? And Allah is Forgiving and Merciful.",
            whatDoesItTeachBn = "অন্যের ভুল ক্ষমা করার মাধ্যমেই আল্লাহর ক্ষমা লাভ করা সম্ভব। প্রতিশোধ গ্রহণের সুযোগ থাকা সত্ত্বেও মাফ করে দেওয়াই শ্রেষ্ঠ মুমিনের পরিচয়।",
            whatToNoticeBn = "হযরত আবু বকর (রা.) যখন তাঁর কন্যার ওপর অপবাদ দেওয়া ব্যক্তিকে আর্থিক সাহায্য বন্ধের কথা ভেবেছিলেন, তখন এই আয়াত নাযিল হয় এবং তিনি সাথে সাথে ক্ষমা করে দেন।",
            whatToBeCarefulAboutBn = "অন্তরে কারও প্রতি দীর্ঘমেয়াদী ক্ষোভ, হিংসা ও প্রতিশোধপরায়ণতা পুষে রাখা থেকে সতর্ক থাকা।",
            whatCanIPracticeBn = "আজ এমন একজন ব্যক্তির কথা স্মরণ করা যার প্রতি আপনার রাগ আছে, এবং আল্লাহর সন্তুষ্টির আশায় তাকে মন থেকে মাফ করে দেওয়া।",
            quranSaysBn = "তোমরা ক্ষমা ও উপেক্ষা করো; তোমরা কি পছন্দ করো না যে আল্লাহ তোমাদের ক্ষমা করে দিন?",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: প্রতিদান কাজের অনুরূপ হয়। যে মানুষকে ক্ষমা করে, আল্লাহও তাকে মাগফিরাত ও রহমতে ঢেকে দেন।",
            possiblePersonalApplicationBn = "নিজের কোনো হক বা পাওনা ছাড় দিয়ে সম্পর্ক টিকিয়ে রাখা।",
            reflectiveQuestions = listOf(
                "আমার অন্তরে কি এমন কোনো ব্যক্তির প্রতি ক্ষোভ জমে আছে যাকে আমি ক্ষমা করতে পারছি না?",
                "আমি কি আল্লাহর কাছে নিজের অসংখ্য ভুলের জন্য যে ক্ষমা চাই, তা কি অন্যের প্রতি প্রদর্শন করছি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.RELATIONSHIPS to listOf("পুরোনো তিক্ততা ভুলে গিয়ে সম্পর্ক জোড়া লাগাতে উদ্যোগী হোন।"),
                LifeSphere.ANGER to listOf("প্রতিশোধ নেওয়ার চিন্তা বাদ দিয়ে আল্লাহর ক্ষমার জন্য অন্যকে ক্ষমা করুন।")
            ),
            defaultTodayAction = "আজ এমন একজনকে অন্তরের অন্তঃস্থল থেকে ক্ষমা করে দিন যিনি আপনার সাথে অন্যায় করেছিলেন এবং তার কল্যাণের জন্য দোয়া করুন।",
            primaryThemes = listOf("ক্ষমা ও উদারতা", "আল্লাহর মাগফিরাত", "সম্পর্ক রক্ষা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/024022.mp3"
        ),

        // 15. Al-Furqan 25:63 - Servants of Ar-Rahman
        AyahActionInsight(
            ayahId = "action_25_63",
            surahNumber = 25,
            ayahNumber = 63,
            surahNameArabic = "سورة الفرقان",
            surahNameBangla = "সূরা আল-ফুরকান (২৫:৬৩)",
            surahNameEnglish = "Surah Al-Furqan",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَعِبَادُ الرَّحْمَٰنِ الَّذِينَ يَمْشُونَ عَلَى الْأَرْضِ هَوْنًا وَإِذَا خَاطَبَهُمُ الْجَاهِلُونَ قَالُوا سَلَامًا",
            transliterationBn = "ওয়া ‘ইবা-দুর রাহমা-নিল্লাযীনা ইয়ামশূনা ‘আলাল আরদ্বি হাওনাঁও ওয়া ইযা- খা-ত্বাবাহুমুল জা-হিলূনা ক্বা-লূ সালা-মা-।",
            banglaTranslation = "আর পরম দয়ালু রহমানের প্রকৃত বান্দা তারাই, যারা জমিনে অত্যন্ত বিনয়ের সাথে পদচারণা করে এবং অজ্ঞ বা মূর্খ লোকেরা যখন তাদের সাথে তর্কে লিপ্ত হয়, তখন তারা শান্তির বাক্য বলে এড়িয়ে যায়।",
            englishTranslation = "And the servants of the Most Merciful are those who walk upon the earth easily, and when the ignorant address them [harshly], they say [words of] peace.",
            whatDoesItTeachBn = "রহমানের বান্দারা কখনো উদ্ধত বা ঝগড়াটে হয় না। কটু কথা বা অবজ্ঞার জবাবে তারা অশান্তি না বাড়িয়ে সুন্দর ও মর্যাদাপূর্ণভাবে প্রস্থান করে।",
            whatToNoticeBn = "অজ্ঞদের সাথে তর্ক করে সময় ও মানসিক শক্তি নষ্ট না করা। নীরবতা এবং 'সালাম' বলাই প্রজ্ঞার লক্ষণ।",
            whatToBeCarefulAboutBn = "সামাজিক মাধ্যমে বা বাস্তবে উত্তপ্ত তর্কে জড়িয়ে গালিগালাজ বা আত্মমর্যাদা ক্ষুণ্ণকারী আচরণ থেকে বেঁচে থাকা।",
            whatCanIPracticeBn = "আজ কেউ কোনো উস্কানিমূলক কথা বললে তর্কে না গিয়ে মৃদু হেসে এড়িয়ে যাওয়া বা 'আল্লাহ আপনার মঙ্গল করুন' বলা।",
            quranSaysBn = "রহমানের বান্দারা বিনম্র চালচলন করে এবং মূর্খদের কটূক্তিতে শান্তির বাণী উচ্চারণ করে।",
            scholarlyInterpretationBn = "তাফসীর আস-সা'দী: 'হাওন' অর্থ গাম্ভীর্য, স্থিরতা ও শিষ্টাচার। তারা কোনো অহংকার ছাড়াই শান্ত চিত্তে জীবনযাপন করে।",
            possiblePersonalApplicationBn = "সোশ্যাল মিডিয়ার কমেন্ট বক্সে অপ্রয়োজনীয় বিতর্ক সম্পূর্ণ বর্জন করা।",
            reflectiveQuestions = listOf(
                "আমি কি সহজেই অন্যের কথায় উত্তেজিত হয়ে অযথা তর্কে জড়িয়ে পড়ি?",
                "আমার ব্যবহারে কি পরম দয়াময় আল্লাহর বান্দা হিসেবে পরিচয় ফুটে ওঠে?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.SPEECH to listOf("অপ্রয়োজনীয় তর্ক পরিহার করে শান্ত ও শালীন বাক্য প্রয়োগ করুন।"),
                LifeSphere.CHARACTER to listOf("উত্তেজনার মুখে সহনশীলতা ও মানসিক পরিপক্বতা প্রদর্শন করুন।")
            ),
            defaultTodayAction = "আজ কোনো উস্কানিমূলক মন্তব্য বা কটূক্তির মুখে কোনো তর্ক না করে হাসিমুখে নীরব থাকুন অথবা সালাম দিয়ে এড়িয়ে যান।",
            primaryThemes = listOf("বিনম্র চালচলন", "বিতর্ক বর্জন", "রহমানের বান্দা"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/025063.mp3"
        ),

        // 16. Al-Furqan 25:74 - Family comfort prayer
        AyahActionInsight(
            ayahId = "action_25_74",
            surahNumber = 25,
            ayahNumber = 74,
            surahNameArabic = "سورة الفرقان",
            surahNameBangla = "সূরা আল-ফুরকান (২৫:৭৪)",
            surahNameEnglish = "Surah Al-Furqan",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَالَّذِينَ يَقُولُونَ رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
            transliterationBn = "ওয়াল্লাযীনা ইয়াক্বূলূনা রাব্বানা- হাব লানা- মিন আযওয়া-জিনা- ওয়া যুররিইয়্যা-তিনা- ক্বুর্রাতা আ‘ইউনিঁও ওয়াজ‘আলনা- লিলমুত্তাক্বীনা ইমা-মা-।",
            banglaTranslation = "আর যারা প্রার্থনা করে: হে আমাদের রব! আমাদের জন্য আমাদের স্ত্রী ও সন্তানদের মধ্য থেকে নয়নপ্রীতিকর (চোখ জুড়ানো) সঙ্গী দান করুন এবং আমাদের মুত্তাকীদের নেতা বানিয়ে দিন।",
            englishTranslation = "And those who say, 'Our Lord, grant us from among our wives and offspring comfort to our eyes and make us an example for the righteous.'",
            whatDoesItTeachBn = "পারিবারিক শান্তি হলো পার্থিব জান্নাত। সন্তান ও জীবনসঙ্গী যখন দ্বীনদার ও সৎ হয়, তখন ঘরে অনাবিল সুখ নেমে আসে।",
            whatToNoticeBn = "কেবল সাধারণ মানুষ হওয়া নয়, বরং কল্যাণে ও তাকওয়ায় অন্যদের পথপ্রদর্শক হওয়ার উচ্চাকাঙ্ক্ষা রাখা উচিত।",
            whatToBeCarefulAboutBn = "পরিবারের শুধু বৈষয়িক চাহিদাকে প্রাধান্য দিয়ে তাদের আখিরাত ও নৈতিক শিক্ষাকে অবহেলা করা থেকে বিরত থাকা।",
            whatCanIPracticeBn = "আজ পরিবারকে সাথে নিয়ে কিছুক্ষণ ইসলামিক বই পড়া বা ভালো কথা আলোচনা করা এবং এই দোয়াটি নিয়মিত পাঠ করা।",
            quranSaysBn = "মুমিনরা তাঁদের পরিবারকে চোখের শীতলতা বানাতে এবং তাকওয়ায় অগ্রগামী হতে রবের কাছে প্রার্থনা করে।",
            scholarlyInterpretationBn = "তাফসীর কুরতুবী: হাসান বসরী (রহ.) বলেন, একজন মুসলমানের চোখ জুড়ায় যখন সে তার পরিবারের সদস্যদের আল্লাহর আনুগত্যে নিয়োজিত দেখতে পায়।",
            possiblePersonalApplicationBn = "ঘরে এমন কোনো আচরণ না করা যা সন্তানদের জন্য খারাপ দৃষ্টান্ত হয়।",
            reflectiveQuestions = listOf(
                "আমার পরিবার কি আমার সাথে থাকলে নিরাপদ ও আনন্দিত বোধ করে?",
                "আমি কি নিজে মুত্তাকী হয়ে আমার পরিবারের জন্য অনুকরণীয় রোল মডেল হতে পারছি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.FAMILY to listOf("পরিবারে ভালোবাসা ও দ্বীনি আবহ তৈরিতে প্রতিদিন কিছুটা আন্তরিক সময় দিন।"),
                LifeSphere.PERSONAL_GROWTH to listOf("চরিত্র ও আমলে এমন উচ্চতা অর্জন করুন যাতে অন্যরা অনুপ্রাণিত হয়।")
            ),
            defaultTodayAction = "আজ আপনার সন্তান বা জীবনসঙ্গীর মাথায় স্নেহের হাত রাখুন এবং এই বিশেষ কোরআনিক দোয়াটি পাঠ করে তাদের কপালে বরকত প্রার্থনা করুন।",
            primaryThemes = listOf("পারিবারিক শান্তি", "চোখের শীতলতা", "তাকওয়ার নেতৃত্ব"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/025074.mp3"
        ),

        // 17. Ar-Rum 30:21 - Love and mercy in marriage
        AyahActionInsight(
            ayahId = "action_30_21",
            surahNumber = 30,
            ayahNumber = 21,
            surahNameArabic = "سورة الروم",
            surahNameBangla = "সূরা আর-রূম (৩০:২১)",
            surahNameEnglish = "Surah Ar-Rum",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَمِنْ آيَاتِهِ أَنْ خَلَقَ لَكُم مِّنْ أَنفُسِكُمْ أَزْوَاجًا لِّتَسْكُنُوا إِلَيْهَا وَجَعَلَ بَيْنَكُم مَّوَدَّةً وَرَحْمَةً ۚ إِنَّ فِي ذَٰلِكَ لَآيَاتٍ لِّقَوْمٍ يَتَفَكَّرُونَ",
            transliterationBn = "ওয়া মিন আ-য়া-তিহী আন খালাক্বা লাকুম মিন আনফুসিকুম আযওয়া-জাল লিতাসকুনূ ইলাইহা- ওয়া জা‘আলা বাইনাকুম মাওয়াদ্দাতাঁও ওয়া রাহমাহ; ইন্না ফী যা-লিকা লাআ-য়া-তিল লিক্বাওমিঁই ইয়াতাফাক্কারূন।",
            banglaTranslation = "আর তাঁর এক অনন্য নিদর্শন হলো, তিনি তোমাদের নিজেদের মধ্য থেকেই তোমাদের জীবনসঙ্গী সৃষ্টি করেছেন যাতে তোমরা তাদের নিকট মানসিক প্রশান্তি লাভ করো এবং তিনি তোমাদের পরস্পরের মাঝে সৃষ্টি করেছেন গভীর ভালোবাসা ও মমতা। নিশ্চয় চিন্তাশীল সম্প্রদায়ের জন্য এতে অনেক নিদর্শন রয়েছে।",
            englishTranslation = "And of His signs is that He created for you from yourselves mates that you may find tranquility in them; and He placed between you affection and mercy. Indeed in that are signs for a people who give thought.",
            whatDoesItTeachBn = "দাম্পত্য জীবন কোনো দ্বন্দ্বের জায়গা নয়, বরং মানসিক প্রশান্তি ও আশ্রয়ের কেন্দ্রবিন্দু। ভালোবাসা ও দয়া হলো সুখের মূল রহস্য।",
            whatToNoticeBn = "আল্লাহ কেবল 'মাওয়াদ্দাহ' (ভালোবাসা) বলেননি, সাথে 'রাহমাহ' (দয়া ও মায়া) যুক্ত করেছেন। দুর্বলতা ও বার্ধক্যে মায়াই সম্পর্ককে টিকিয়ে রাখে।",
            whatToBeCarefulAboutBn = "ঘরের ছোটখাটো ভুলত্রুটি নিয়ে কটূক্তি করা বা জীবনসঙ্গীর মানসিক আবেগকে অবমূল্যায়ন করা থেকে সাবধান থাকা।",
            whatCanIPracticeBn = "আজ জীবনসঙ্গীকে একটি উপহার দেওয়া বা মিষ্টি করে তার অবদানের জন্য ধন্যবাদ জানানো।",
            quranSaysBn = "দাম্পত্যের মূল উদ্দেশ্য পরস্পরের নিকট আত্মিক প্রশান্তি লাভ এবং ভালোবাসা-মমতার বন্ধন।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: স্বামী-স্ত্রীর মধ্যকার ভালোবাসা ও আন্তরিক টান আল্লাহর কুদরতের অন্যতম বড় প্রমাণ।",
            possiblePersonalApplicationBn = "অফিস বা বাইরের চাপ কখনোই ঘরের ভেতরে নিয়ে এসে জীবনসঙ্গীর ওপর রাগ না ঝাড়া।",
            reflectiveQuestions = listOf(
                "আমার ঘর কি আমার ও আমার জীবনসঙ্গীর জন্য শান্তির আশ্রয়স্থল?",
                "আমি কি তাঁর প্রতি কেবল দায়িত্ব পালন করছি নাকি প্রকৃত মমতা ও সহানুভূতি দেখাচ্ছি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.FAMILY to listOf("দাম্পত্যে ছোটখাটো ত্রুটি উপেক্ষা করে ক্ষমা ও ভালোবাসার প্রকাশ ঘটান।"),
                LifeSphere.SPEECH to listOf("ঘরে সবসময় মিষ্টি ও সম্মানজনক সম্বোধন ব্যবহার করুন।")
            ),
            defaultTodayAction = "আজ আপনার জীবনসঙ্গীর কোনো একটি গুণ বা রান্নার মন খুলে প্রশংসা করুন এবং তার সাথে কিছু হাসিখুশি সময় কাটান।",
            primaryThemes = listOf("দাম্পত্য প্রশান্তি", "ভালোবাসা ও মমতা", "পারিবারিক সুখ"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/030021.mp3"
        ),

        // 18. Luqman 31:17 - Advice to son
        AyahActionInsight(
            ayahId = "action_31_17",
            surahNumber = 31,
            ayahNumber = 17,
            surahNameArabic = "سورة لقمان",
            surahNameBangla = "সূরা লুকমান (৩১:১৭-১৯)",
            surahNameEnglish = "Surah Luqman",
            revelationTypeBn = "মাক্কী",
            arabicText = "يَا بُنَيَّ أَقِمِ الصَّلَاةَ وَأْمُرْ بِالْمَعْرُوفِ وَانْهَ عَنِ الْمُنكَرِ وَاصْبِرْ عَلَىٰ مَا أَصَابَكَ ۖ إِنَّ ذَٰلِكَ مِنْ عَزْمِ الْأُمُورِ ۝ وَلَا تُصَعِّرْ خَدَّكَ لِلنَّاسِ وَلَا تَمْشِ فِي الْأَرْضِ مَرَحًا ۖ إِنَّ اللَّهَ لَا يُحِبُّ كُلَّ مُخْتَالٍ فَخُورٍ ۝ وَاقْصِدْ فِي مَشْيِكَ وَاغْضُضْ مِن صَوْتِكَ",
            transliterationBn = "ইয়া- বুনাইয়্যা আক্বিমিস সালা-তা ওয়া’মুর বিলমা‘রূফি ওয়ানহা ‘আনিল মুনকারি ওয়াসবির ‘আলা- মা- আসা-বাকা; ইন্না যা-লিকা মিন ‘আযমিল উমূর। ওয়ালা- তুসা‘ইর খাদ্দাকা লিন্না-সি ওয়ালা- তামশি ফিল আরদ্বি মারাহা-; ইন্নাল্লা-হা লা- ইউহিব্বু কুল্লা মুখতা-লিন ফাখূর। ওয়াক্বসিদ ফী মাশয়িকা ওয়াগদুদ মিন সাওতিক।",
            banglaTranslation = "হে আমার প্রিয় বৎস! সালাত কায়েম করো, সৎকাজের আদেশ দাও, অসৎকাজে নিষেধ করো এবং তোমার ওপর যে বিপদ আসে তাতে ধৈর্য ধারণ করো; নিশ্চয় এটি অত্যন্ত দৃঢ় সংকল্পের কাজ। আর অহংকার করে মানুষের দিক থেকে মুখ ফিরিয়ে নিয়ো না এবং জমিনে গর্বভরে চলো না; নিশ্চয় আল্লাহ কোনো দাম্ভিক অহংকারীকে ভালোবাসেন না। আর তোমার হাঁটায় মধ্যপন্থা অবলম্বন করো এবং তোমার কণ্ঠস্বর নিচু রাখো।",
            englishTranslation = "O my son, establish prayer, enjoin what is right, forbid what is wrong, and be patient over what befalls you. Indeed, [all] that is of the matters [requiring] determination. And do not turn your cheek [in contempt] toward people and do not walk through the earth exultantly. Indeed, Allah does not like everyone self-deluded and boastful. And be moderate in your pace and lower your voice.",
            whatDoesItTeachBn = "একজন পূর্ণাঙ্গ মানুষের জীবনের স্বর্ণালী রূপরেখা: আল্লাহর হক্ব (সালাত), সমাজের হক্ব (সৎকাজের আদেশ ও মন্দ বর্জন), ব্যক্তিগত স্থিতিশীলতা (ধৈর্য) এবং সামাজিক শিষ্টাচার (নম্রতা ও বিনয়ী কণ্ঠস্বর)।",
            whatToNoticeBn = "চিৎকার করে বা উদ্ধত সুরে কথা বলা শিষ্টাচার নয়। বিনম্র কণ্ঠস্বর ও শান্ত আচরণই প্রকৃত ব্যক্তিত্বের পরিচয়।",
            whatToBeCarefulAboutBn = "কথা বলার সময় অহংকারে মুখ ঘুরিয়ে নেওয়া বা অতিরিক্ত কর্কশ ও উচ্চৈঃস্বরে ধমক দিয়ে কথা বলা থেকে বিরত থাকা।",
            whatCanIPracticeBn = "আজ পরিবার ও অফিসে কথা বলার সময় কণ্ঠস্বর অত্যন্ত মার্জিত রাখা এবং কোনো পরিস্থিতিতে চিল্লাচিল্লি না করা।",
            quranSaysBn = "লুকমানের চিরন্তন নীতি: সালাত, ধৈর্য, সৎকাজের প্রেরণা এবং বিনম্র চালচলন ও কণ্ঠস্বর।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: জ্ঞানী লুকমান তাঁর পুত্রকে এমন সব গুণের উপদেশ দিয়েছেন যা একজন মানুষকে দুনিয়া ও আখিরাতে সম্মানিত করে।",
            possiblePersonalApplicationBn = "সন্তান বা ছোটদের কোনো বিষয়ে নসিহত করার সময় মমতা ও যুক্তি দিয়ে বোঝানো।",
            reflectiveQuestions = listOf(
                "আমার কণ্ঠস্বর কি কখনো অন্যের জন্য ভীতিকর বা বিরক্তিকর হয়ে ওঠে?",
                "আমি কি সমাজের অন্যায়ের সাথে আপস করে সৎকাজের কথা বলা বন্ধ করে দিয়েছি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.SPEECH to listOf("কথাবার্তায় অযথা চিৎকার বর্জন করুন এবং মার্জিত কণ্ঠস্বর বজায় রাখুন।"),
                LifeSphere.CHARACTER to listOf("মানুষের সাথে দেখা হলে আন্তরিক ও হাসিমুখে কথা বলুন।")
            ),
            defaultTodayAction = "আজকের দিনে কারও সাথে কথা বলার সময় গলার স্বর নিচু ও মোলায়েম রাখুন এবং সালাত ধীরস্থিরভাবে আদায় করুন।",
            primaryThemes = listOf("লুকমানের উপদেশ", "কণ্ঠস্বর সংযত রাখা", "সালাত ও সবর"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/031017.mp3"
        ),

        // 19. Az-Zumar 39:53 - Endless Mercy
        AyahActionInsight(
            ayahId = "action_39_53",
            surahNumber = 39,
            ayahNumber = 53,
            surahNameArabic = "سورة الزمر",
            surahNameBangla = "সূরা আয-যুমার (৩৯:৫৩)",
            surahNameEnglish = "Surah Az-Zumar (The Crowds)",
            revelationTypeBn = "মাক্কী",
            arabicText = "قُلْ يَا عِبَادِيَ الَّذِينَ أَسْرَفُوا عَلَىٰ أَنفُسِهِمْ لَا تَقْنَطُوا مِن رَّحْمَةِ اللَّهِ ۚ إِنَّ اللَّهَ يَغْفِرُ الذُّنُوبَ جَمِيعًا ۚ إِنَّهُ هُوَ الْغَفُورُ الرَّحِيمُ",
            transliterationBn = "ক্বুল ইয়া- ‘ইবা-দিয়াল্লাযীনা আসরাফূ ‘আলা- আনফুসিহিম লা- তাক্বনাতূ মির রাহমাতিল্লা-হ; ইন্নাল্লা-হা ইয়াগফিরুয যুনূবা জামী‘আ-; ইন্নাহূ হুওয়াল গাফূরুর রাহীম।",
            banglaTranslation = "বলুন: হে আমার বান্দাগণ! যারা নিজেদের ওপর বাড়াবাড়ি বা অবিচার করেছ, তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না। নিশ্চয়ই আল্লাহ সমস্ত গুনাহ ক্ষমা করে দেন। নিশ্চয় তিনি পরম ক্ষমাশীল, অতি দয়ালু।",
            englishTranslation = "Say, 'O My servants who have transgressed against themselves [by sinning], do not despair of the mercy of Allah. Indeed, Allah forgives all sins. Indeed, it is He who is the Forgiving, the Merciful.'",
            whatDoesItTeachBn = "পবিত্র কুরআনের সবচেয়ে আশাজাগানিয়া আয়াত। গুনাহ যত বড়ই হোক না কেন, আল্লাহর রহমত তার চেয়েও অনেক বড়। আন্তরিক তাওবার দরজা মৃত্যুর আগ পর্যন্ত উন্মুক্ত।",
            whatToNoticeBn = "আল্লাহ গুনাহগারদেরও স্নেহভরে ডেকেছেন: 'ইয়া ইবাদিয়া' (হে আমার বান্দারা!)। তিনি নিরাশ হতে নিষেধ করেছেন।",
            whatToBeCarefulAboutBn = "শয়তান মানুষের মাথায় ঢুকিয়ে দেয় যে 'তুমি এত গুনাহ করেছ যে আল্লাহ তোমাকে আর মাফ করবেন না'—এই শয়তানি ফাঁদ থেকে দূরে থাকা।",
            whatCanIPracticeBn = "আজ অতীতের সকল ভুলের জন্য লজ্জিত হয়ে ২ রাকাত সালাতুত তাওবা আদায় করে আল্লাহর কাছে ক্ষমা চাওয়া।",
            quranSaysBn = "আন্তরিক তাওবা করলে আল্লাহ বান্দার সমস্ত গুনাহ মুছে দেন।",
            scholarlyInterpretationBn = "তাফসীর কুরতুবী: এই আয়াতটি কুরআনের সর্বাধিক আশাব্যঞ্জক আয়াত। সাচ্চা তওবাকারীর অতীত জীবনের বড় বড় পাপকেও আল্লাহ পুণ্য দ্বারা প্রতিস্থাপিত করেন।",
            possiblePersonalApplicationBn = "পাপ হয়ে গেলে তাৎক্ষণিকভাবে ওজু করে ভালো কাজ করা যাতে গুনাহ মুছে যায়।",
            reflectiveQuestions = listOf(
                "কোন ভুলের অপরাধবোধ আমাকে আল্লাহর কাছে ফিরে আসতে বাধা দিচ্ছে?",
                "আমি কি সত্যিই উপলব্ধি করি যে তওবাকারীকে আল্লাহ কত বেশি ভালোবাসেন?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.WORSHIP to listOf("আজ গভীর রাতে সিজদায় অশ্রুসিক্ত চোখে নিজের সব গুনাহর জন্য ক্ষমা চান।"),
                LifeSphere.MINDSET to listOf("হতাশা ভেঙে আল্লাহর ক্ষমার বিশালতার ওপর বিশ্বাস রাখুন।")
            ),
            defaultTodayAction = "আজ দিনে অন্তত ১০০ বার আন্তরিক অনুশোচনায় 'আস্তাগফিরুল্লাহ ওয়া আতূবু ইলাইহি' পাঠ করুন।",
            primaryThemes = listOf("তাওবাহ ও ক্ষমা", "আল্লাহর রহমত", "পাপমুক্তি"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/039053.mp3"
        ),

        // 20. Fussilat 41:34 - Repel evil with good
        AyahActionInsight(
            ayahId = "action_41_34",
            surahNumber = 41,
            ayahNumber = 34,
            surahNameArabic = "سورة فصلت",
            surahNameBangla = "সূরা ফুসসিলাত (৪১:৩৪)",
            surahNameEnglish = "Surah Fussilat",
            revelationTypeBn = "মাক্কী",
            arabicText = "وَلَا تَسْتَوِي الْحَسَنَةُ وَلَا السَّيِّئَةُ ۚ ادْفَعْ بِالَّتِي هِيَ أَحْسَنُ فَإِذَا الَّذِي بَيْنَكَ وَبَيْنَهُ عَدَاوَةٌ كَأَنَّهُ وَلِيٌّ حَمِيمٌ",
            transliterationBn = "ওয়ালা- তাস্তাবিল হাসানাতু ওয়ালাস সাইয়্যিআহ; ইদফা‘ বিল্লাতী হিয়া আহসানু ফাইযাল্লাযী বাইনাকা ওয়া বাইনাহূ ‘আদা-ওয়াতুন কা’আন্নাহূ ওয়ালিইয়ুন হামীম।",
            banglaTranslation = "আর ভালো ও মন্দ কখনো সমান হতে পারে না। মন্দকে প্রতিহত করো উৎকৃষ্ট দ্বারা; ফলে তোমার সাথে যার শত্রুতা ছিল, সেও যেন এক অন্তরঙ্গ বন্ধুতে পরিণত হয়ে যাবে।",
            englishTranslation = "And not equal are the good deed and the bad. Repel [evil] by that [deed] which is better; and thereupon the one whom between you and him is enmity [will become] as though he was a devoted friend.",
            whatDoesItTeachBn = "আঘাতের বদলে ভালোবাসা ও সৌজন্য দেওয়াই মানুষের মন জয় করার শ্রেষ্ঠ অস্ত্র। দুর্ব্যবহারের জবাব দুর্ব্যবহারে দিলে শত্রুতা বাড়ে, কিন্তু সদ্ব্যবহার দিলে শত্রুও বন্ধু হয়ে যায়।",
            whatToNoticeBn = "মন্দ আচরণকারীর সাথে উত্তম ব্যবহার করা চরম আত্মসংযম ও মহান হৃদয়ের দাবি রাখে। এটি মুমিনের সর্বোচ্চ আখলাকের পরিচায়ক।",
            whatToBeCarefulAboutBn = "অন্যের তিক্ত কথায় মেজাজ হারিয়ে প্রতিশোধ নেওয়া বা সম্পর্ক চিরতরে নষ্ট করে দেওয়া থেকে সাবধান থাকা।",
            whatCanIPracticeBn = "আজ যে আপনার সাথে রূঢ় আচরণ করে, তাকে হাসিমুখে মিষ্টি ভাষায় অভ্যর্থনা জানানো।",
            quranSaysBn = "মন্দকে উৎকৃষ্ট আচরণ দিয়ে প্রতিহত করার সুস্পষ্ট ঐশী ফর্মুলা।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: হযরত উমর (রা.) বলতেন, যে ব্যক্তি তোমার ব্যাপারে আল্লাহকে ভয় করে না (অন্যায্য আচরণ করে), তার শ্রেষ্ঠ শাস্তি হলো তুমি তার ব্যাপারে আল্লাহকে ভয় করে ভালো আচরণ করো।",
            possiblePersonalApplicationBn = "কাজের জায়গায় কেউ অসহযোগিতা করলে তার কোনো কাজে নিজে এগিয়ে গিয়ে সাহায্য করা।",
            reflectiveQuestions = listOf(
                "কে আমার সাথে সম্প্রতি মন্দ আচরণ করেছে যাকে আমি ভালো ব্যবহার দিয়ে জয় করতে পারি?",
                "আমার মধ্যে কি ক্ষোভ হজম করে সুন্দর প্রতিক্রিয়া দেওয়ার ধৈর্য আছে?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.RELATIONSHIPS to listOf("শত্রুভাবাপন্ন ব্যক্তির সাথেও সদাচরণ বজায় রেখে শত্রুতাকে মৈত্রীতে রূপান্তর করুন।"),
                LifeSphere.ANGER to listOf("রাগের বদলা ভালো আচরণ দিয়ে দিন।")
            ),
            defaultTodayAction = "আজ এমন কাউকে সুন্দর একটি উপহার দিন বা মিষ্টি বার্তা পাঠান যার সাথে আপনার মনোমালিন্য চলছিল।",
            primaryThemes = listOf("সদাচরণ", "মন্দ প্রতিহত করা", "শত্রুকে বন্ধুকরণ"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/041034.mp3"
        ),

        // 21. Al-Hujurat 49:11-12 - Avoiding mockery, suspicion, spying and backbiting
        AyahActionInsight(
            ayahId = "action_49_11",
            surahNumber = 49,
            ayahNumber = 11,
            surahNameArabic = "سورة الحجرات",
            surahNameBangla = "সূরা আল-হুজুরাত (৪৯:১১-১২)",
            surahNameEnglish = "Surah Al-Hujurat",
            revelationTypeBn = "মাদানী",
            arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا لَا يَسْخَرْ قَوْمٌ مِّن قَوْمٍ عَسَىٰ أَن يَكُونُوا خَيْرًا مِّنْهُمْ... يَا أَيُّهَا الَّذِينَ آمَنُوا اجْتَنِبُوا كَثِيرًا مِّنَ الظَّنِّ إِنَّ بَعْضَ الظَّنِّ إِثْمٌ ۖ وَلَا تَجَسَّسُوا وَلَا يَغْتَب بَّعْضُكُم بَعْضًا",
            transliterationBn = "ইয়া-আইয়্যুহাল্লাযীনা আ-মানূ লা- ইয়াসখার ক্বাওমুম মিন ক্বাওমিন ‘আসা- আইঁ ইয়াকূনূ খাইরাম মিনহুম... ইয়া-আইয়্যুহাল্লাযীনা আ-মানুজ তানিবূ কাছীরাম মিনায জান্নি ইন্না বা‘দ্বায জান্নি ইছমুঁও ওয়ালা- তাজাস্সাসূ ওয়ালা- ইয়াগতাব বা‘দ্বুকুম বা‘দ্বা-।",
            banglaTranslation = "হে মুমিনগণ! কোনো সম্প্রদায় যেন অন্য কোনো সম্প্রদায়কে উপহাস না করে, হতে পারে তারা তাদের চেয়ে উত্তম... হে মুমিনগণ! তোমরা অধিকাংশ অনুমান থেকে দূরে থাকো, নিশ্চয় কোনো কোনো অনুমান তো পাপ; আর তোমরা একে অপরের গোপন দোষ অনুসন্ধান করো না এবং একে অপরের গীবত (পরনিন্দা) করো না।",
            englishTranslation = "O you who have believed, let not a people ridicule [another] people; perhaps they may be better than them... O you who have believed, avoid much [negative] assumption. Indeed, some assumption is sin. And do not spy or backbite each other.",
            whatDoesItTeachBn = "সামাজিক জীবনের চারটি মারাত্মক ব্যাধি: উপহাস, কুধারণা, গোয়েন্দাগিরি (অন্যের দোষ খোঁজা) এবং গীবত। এগুলো মানুষের সমস্ত নেক আমল নষ্ট করে ফেলে।",
            whatToNoticeBn = "গীবত করাকে কুরআন মৃত ভাইয়ের মাংস খাওয়ার মতো বীভৎস পাপের সাথে তুলনা করেছে। অন্যের গোপন দোষ খোঁজা সম্পূর্ণ নিষিদ্ধ।",
            whatToBeCarefulAboutBn = "চা-এর আড্ডায় বা ফোনে অন্যের ব্যক্তিগত জীবনের সমালোচনা ও হাসাহাসিতে অংশগ্রহণ করা থেকে কঠোরভাবে বিরত থাকা।",
            whatCanIPracticeBn = "আজ কারও অনুপস্থিতিতে তার সামান্যতম সমালোচনাও না করা এবং কেউ গীবত শুরু করলে প্রসঙ্গ পাল্টে দেওয়া।",
            quranSaysBn = "উপহাস, খারাপ ধারণা, অন্যের দোষ অনুসন্ধান ও গীবত সম্পূর্ণরূপে হারাম।",
            scholarlyInterpretationBn = "তাফসীর মা'আরিফুল কুরআন: মানুষের সম্মান রক্তের মতোই পবিত্র। যে ব্যক্তি ভাইয়ের দোষ ঢেকে রাখে, আল্লাহ কিয়ামতের দিন তার দোষ ঢেকে রাখবেন।",
            possiblePersonalApplicationBn = "কারও কোনো দুর্বলতা চোখে পড়লে তা নিয়ে বন্ধুদের সাথে কথা না বলে তার জন্য গোপনে হেদায়াতের দোয়া করা।",
            reflectiveQuestions = listOf(
                "আমি কি বন্ধুদের সাথে আড্ডায় অবচেতনে কারও সম্মানহানি করে ফেলছি?",
                "আমি কি মানুষের সম্পর্কে ভালো ধারণা পোষণ করি নাকি প্রথমেই খারাপ চিন্তা করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.SPEECH to listOf("জবানকে গীবত ও উপহাস থেকে সম্পূর্ণ পবিত্র রাখুন।"),
                LifeSphere.DIGITAL_LIFE to listOf("সোশ্যাল মিডিয়ায় কারও ব্যক্তিগত ছবি বা ভুল নিয়ে ট্রল ও কটাক্ষ করবেন না।")
            ),
            defaultTodayAction = "আজকে ২৪ ঘণ্টার একটি শপথ নিন: সারাদিনে কারও অনুপস্থিতিতে কোনো নেতিবাচক কথা উচ্চারণ করবেন না।",
            primaryThemes = listOf("গীবত বর্জন", "সুধারণা", "সামাজিক শিষ্টাচার"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/049011.mp3"
        ),

        // 22. At-Talaq 65:2-3 - Taqwa brings way out and Rizq
        AyahActionInsight(
            ayahId = "action_65_2",
            surahNumber = 65,
            ayahNumber = 2,
            surahNameArabic = "سورة الطلاق",
            surahNameBangla = "সূরা আত-ত্বালাক (৬৫:২-৩)",
            surahNameEnglish = "Surah At-Talaq",
            revelationTypeBn = "মাদানী",
            arabicText = "وَمَن يَتَّقِ اللَّهَ يَجْعَل لَّهُ مَخْرَجًا ۝ وَيَرْزُقْهُ مِنْ حَيْثُ لَا يَحْتَسِبُ ۚ وَمَن يَتَوَكَّلْ عَلَى اللَّهِ فَهُوَ حَسْبُهُ ۚ إِنَّ اللَّهَ بَالِغُ أَمْرِهِ",
            transliterationBn = "ওয়া মাইঁ ইয়াত্তাক্বিল্লা-হা ইয়াজ‘আল লাহূ মাখরাজা-। ওয়া ইয়ারযুক্বহু মিন হাইছু লা- ইয়াহ তাসিব; ওয়া মাইঁ ইয়াতাওয়াক্কাল ‘আলাল্লা-হি ফাহুওয়া হাসবুহ; ইন্নাল্লা-হা বা-লিগু আমরিহী।",
            banglaTranslation = "আর যে ব্যক্তি আল্লাহকে ভয় করে (তাকওয়া অবলম্বন করে), আল্লাহ তার জন্য সংকট থেকে বের হওয়ার পথ করে দেন এবং তাকে এমন উৎস থেকে রিযিক দান করেন যা সে কল্পনাও করতে পারে না। আর যে আল্লাহর ওপর ভরসা করে, তিনিই তার জন্য যথেষ্ট। নিশ্চয় আল্লাহ তাঁর কাজ সম্পন্ন করবেনই।",
            englishTranslation = "And whoever fears Allah - He will make for him a way out and will provide for him from where he does not expect. And whoever relies upon Allah - then He is sufficient for him. Indeed, Allah will accomplish His purpose.",
            whatDoesItTeachBn = "জীবনের যেকোনো বন্ধ দরজা খুলে যাওয়ার চাবিকাঠি হলো তাকওয়া (আল্লাহভীতি)। আল্লাহকে ভয় করে হারাম বর্জন করলে আল্লাহ অভাবনীয় উৎস থেকে বরকত ও পথ খুলে দেন।",
            whatToNoticeBn = "দুটি ঐশী ওয়াদা: সংকট মুক্তির পথ (মাখরাজা) এবং অকল্পনীয় রিযিক। শর্ত কেবল একটাই—আল্লাহর আনুগত্য ও তাঁর ওপর ভরসা।",
            whatToBeCarefulAboutBn = "কোনো আর্থিক সংকটে পথ খুঁজে না পেয়ে ধৈর্য হারিয়ে সুদী ঋণ বা ঘুষের পথে পা বাড়ানো থেকে সতর্ক থাকা।",
            whatCanIPracticeBn = "আজ যেকোনো লোভনীয় হারাম সুযোগকে কেবল আল্লাহর ভয়ে প্রত্যাখ্যান করা এবং আল্লাহর ওয়াদার ওপর অবিচল বিশ্বাস রাখা।",
            quranSaysBn = "যে আল্লাহকে ভয় করবে, আল্লাহ তার জন্য মুক্তির পথ করে দেবেন এবং অকল্পনীয় উৎস থেকে রিযিক দেবেন।",
            scholarlyInterpretationBn = "মুসনাদে আহমাদ: আবদুল্লাহ ইবনে মাসউদ (রা.) বলেন, কুরআনে সংকট থেকে মুক্তির জন্য এর চেয়ে স্পষ্ট এবং আশাব্যঞ্জক আয়াত আর নেই।",
            possiblePersonalApplicationBn = "ক্যারিয়ার বা ব্যবসার অনিশ্চয়তায় আল্লাহর ওপর সম্পূর্ণ তাওয়াক্কুল বজায় রাখা।",
            reflectiveQuestions = listOf(
                "আমি কি বর্তমানে কোনো বন্ধ গলিতে আটকা পড়ে আছি যেখানে আমার আল্লাহর সাহায্য খুব প্রয়োজন?",
                "আমি কি কোনো হারাম কাজ ছাড়তে ভয় পাচ্ছি এই ভেবে যে আমার ক্ষতি হয়ে যাবে?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MONEY to listOf("হারাম ছাড়ুন, আল্লাহ আপনাকে তার চেয়ে লক্ষ গুণ উত্তম হালাল রিযিক দেবেন।"),
                LifeSphere.DIFFICULT_SITUATIONS to listOf("তাকওয়া অবলম্বন করুন, সবচেয়ে জটিল সমস্যাও আল্লাহ সমাধান করে দেবেন।")
            ),
            defaultTodayAction = "আজ এমন একটি কাজ বা অভ্যাস ত্যাগ করুন যা আল্লাহ অসন্তুষ্ট হন এবং অন্তরে দৃঢ় বিশ্বাস রাখুন যে আল্লাহ আপনার পথ খুলে দেবেন।",
            primaryThemes = listOf("তাকওয়া", "সংকটমুক্তি", "অকল্পনীয় রিযিক"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/065002.mp3"
        ),

        // 23. Al-Mulk 67:2 - Purpose of Life and Death
        AyahActionInsight(
            ayahId = "action_67_2",
            surahNumber = 67,
            ayahNumber = 2,
            surahNameArabic = "سورة الملك",
            surahNameBangla = "সূরা আল-মুলক (৬৭:২)",
            surahNameEnglish = "Surah Al-Mulk (The Dominion)",
            revelationTypeBn = "মাক্কী",
            arabicText = "الَّذِي خَلَقَ الْمَوْتَ وَالْحَيَاةَ لِيَبْلُوَكُمْ أَيُّكُمْ أَحْسَنُ عَمَلًا ۚ وَهُوَ الْعَزِيزُ الْغَفُورُ",
            transliterationBn = "আল্লাযী খালাক্বাল মাওতা ওয়াল হায়া-তা লিইয়াবলুয়াকুম আইয়্যুকুম আহসানু ‘আমালা-; ওয়াহুওয়াল ‘আযীযুল গাফূর।",
            banglaTranslation = "যিনি সৃষ্টি করেছেন মৃত্যু ও জীবন, যাতে তোমাদের পরীক্ষা করতে পারেন যে—কাজে তোমাদের মধ্যে কে সর্বোত্তম? আর তিনি পরাক্রমশালী, পরম ক্ষমাশীল।",
            englishTranslation = "[He] who created death and life to test you [as to] which of you is best in deed - and He is the Exalted in Might, the Forgiving -",
            whatDoesItTeachBn = "জীবন নিছক খাওয়া-পরা আর বিনোদনের জন্য নয়; জীবন ও মৃত্যুর মূল উদ্দেশ্য হলো শ্রেষ্ঠ আমল সম্পন্ন করার পরীক্ষা দেওয়া। পরিমাণের চেয়ে কাজের মানই আসল।",
            whatToNoticeBn = "আল্লাহ বলেননি 'কে বেশি আমল করে', বরং বলেছেন 'কে সর্বোত্তম আমল করে' (আহসানু আমালা)। ইখলাস ও সুন্নাহসম্মত ছোট আমলও অনেক বড়।",
            whatToBeCarefulAboutBn = "জীবনকে চিরস্থায়ী মনে করে গাফেল থাকা এবং মৃত্যুর প্রস্তুতি ছাড়া পার্থিব মোহে ডুবে থাকা থেকে বেঁচে থাকা।",
            whatCanIPracticeBn = "আজ যেকোনো আমল—সালাত, সদকা বা মানুষের সেবা—লোকদেখানো মনোভাব ছাড়া একমাত্র আল্লাহর সন্তুষ্টিতে নিখুঁতভাবে করা।",
            quranSaysBn = "জীবন ও মৃত্যুর একমাত্র উদ্দেশ্য: সৎকর্মে কে সর্বোত্তম তা পরীক্ষা করা।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: ফুদাইল ইবনে ইয়াদ (রহ.) বলেন, 'আহসানু আমালা' অর্থ যা সবচেয়ে একনিষ্ঠ (কেবল আল্লাহর জন্য) এবং সবচেয়ে সঠিক (সুন্নাহর অনুগামী)।",
            possiblePersonalApplicationBn = "সারাদিনের প্রতিটি কাজকে মৃত্যুর স্মরণ দিয়ে অর্থবহ করে তোলা।",
            reflectiveQuestions = listOf(
                "আমার আজকের দিনের কাজগুলো কি আমাকে আল্লাহর সন্তুষ্টির জান্নাতের দিকে এগিয়ে নিচ্ছে?",
                "আমি কি কাজের সংখ্যার চেয়ে নিয়তের শুদ্ধতা ও সুন্নাহর অনুসরণের ওপর বেশি জোর দিই?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.WORK to listOf("অফিসের বা পড়াশোনার কাজে সর্বোচ্চ নিষ্ঠা ও পেশাদারিত্ব প্রদর্শন করুন।"),
                LifeSphere.WORSHIP to listOf("নামাজটি এমনভাবে পড়ুন যেন এটিই হতে পারে আপনার জীবনের শেষ নামাজ।")
            ),
            defaultTodayAction = "আজ এমন একটি নেক আমল করুন যা সম্পূর্ণ গোপনে থাকবে এবং যার খবর আল্লাহ ছাড়া দুনিয়ার আর কেউ জানবে না।",
            primaryThemes = listOf("জীবনের উদ্দেশ্য", "উত্তম আমল", "মৃত্যুর প্রস্তুতি"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/067002.mp3"
        ),

        // 24. Ad-Duha 93:3-5 - Solace in hardship
        AyahActionInsight(
            ayahId = "action_93_3",
            surahNumber = 93,
            ayahNumber = 3,
            surahNameArabic = "سورة الضحى",
            surahNameBangla = "সূরা আদ-দুহা (৯৩:৩-৫)",
            surahNameEnglish = "Surah Ad-Duha",
            revelationTypeBn = "মাক্কী",
            arabicText = "مَا وَدَّعَكَ رَبُّكَ وَمَا قَلَىٰ ۝ وَلَلْآخِرَةُ خَيْرٌ لَّكَ مِنَ الْأُولَىٰ ۝ وَلَسَوْفَ يُعْطِيكَ رَبُّكَ فَتَرْضَىٰ",
            transliterationBn = "মা- ওয়াদ্দা‘আকা রাব্বুকা ওয়ামা- ক্বালা-। ওয়ালাল আ-খিরাতু খাইরুল লাকা মিনাল ঊলা-। ওয়ালা সাওফা ইউ‘ত্বীকা রাব্বুকা ফাতারদ্বা-।",
            banglaTranslation = "আপনার রব আপনাকে কখনো পরিত্যাগ করেননি এবং অসন্তুষ্টও হননি। আর নিশ্চয়ই আপনার জন্য পরবর্তী সময় (পরকাল) পূর্ববর্তী সময়ের (ইহকালের) চেয়ে অনেক উত্তম। আর অচিরেই আপনার রব আপনাকে এত দান করবেন যে আপনি সন্তুষ্ট হয়ে যাবেন।",
            englishTranslation = "Your Lord has not taken leave of you, [O Muhammad], nor has He detested [you]. And the Hereafter is better for you than the first [life]. And your Lord is going to give you, and you will be satisfied.",
            whatDoesItTeachBn = "কঠিন সময়ে বা একাকীত্বে যখন মনে হয় চারপাশ নিস্তব্ধ, তখন আল্লাহ আপনাকে ভুলে যাননি। বর্তমান কষ্টের চেয়ে ভবিষ্যতের সুফল ও আখিরাত অনেক বেশি উজ্জ্বল।",
            whatToNoticeBn = "আল্লাহর প্রতিশ্রুতির গভীরতা: 'অচিরেই আপনার রব আপনাকে এত দেবেন যে আপনি সন্তুষ্ট হবেন'। মুমিনের জীবনে চিরস্থায়ী হতাশা বলে কিছু নেই।",
            whatToBeCarefulAboutBn = "কষ্টের মুহূর্তে ভাবা যে আল্লাহ হয়তো আমার ওপর রাগ করেছেন বা আমাকে ছেড়ে দিয়েছেন—এমন চিন্তা থেকে মনকে মুক্ত রাখা।",
            whatCanIPracticeBn = "আজ মন খারাপ লাগলে সূরা দুহার এই আয়াতগুলো অর্থসহ তিলাওয়াত করে হৃদয়ে আল্লাহর ভালোবাসার স্পর্শ অনুভব করা।",
            quranSaysBn = "আপনার রব আপনাকে ত্যাগ করেননি; অচিরেই তিনি আপনাকে অসীম প্রাপ্তিতে সন্তুষ্ট করবেন।",
            scholarlyInterpretationBn = "তাফসীর আস-সা'দী: বিষণ্ণতা ও মনোকষ্ট দূর করার জন্য সূরা দুহা এক মহৌষধ। এটি অন্তরে আশার প্রদীপ প্রজ্বলিত করে।",
            possiblePersonalApplicationBn = "বর্তমান পার্থিব অপ্রাপ্তিকে আখিরাতের চিরস্থায়ী প্রাপ্তির তুলনায় তুচ্ছ মনে করা।",
            reflectiveQuestions = listOf(
                "আমি কি সাময়িক একাকীত্বে নিজেকে নিঃসঙ্গ ভাবছি?",
                "আমি কি বিশ্বাস করি এই পরীক্ষার পর আল্লাহ আমাকে এমন কিছু দেবেন যাতে আমি আনন্দিত হব?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MINDSET to listOf("বিষণ্ণতা দূর করুন; আপনার রবের করুণার দরজা আপনার জন্য সর্বক্ষণ উন্মুক্ত।"),
                LifeSphere.DIFFICULT_SITUATIONS to listOf("ধৈর্য ধরুন, আপনার জীবনেও খুব শীঘ্রই সুসংবাদের ভোর ফুটে উঠবে।")
            ),
            defaultTodayAction = "আজ যখনই বিষণ্ণ বা একা লাগবে, সূরা আদ-দুহার অর্থ মনোযোগ দিয়ে পড়ুন এবং আল্লাহর পরম ভালোবাসার কথা স্মরণ করুন।",
            primaryThemes = listOf("সান্ত্বনা", "বিষণ্ণতা নিরাময়", "আল্লাহর অপার দান"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/093003.mp3"
        ),

        // 25. Al-Ikhlas 112:1-4 - Purity of Tawheed
        AyahActionInsight(
            ayahId = "action_112_1",
            surahNumber = 112,
            ayahNumber = 1,
            surahNameArabic = "سورة الإخلاص",
            surahNameBangla = "সূরা আল-ইখলাস (১১২:১-৪)",
            surahNameEnglish = "Surah Al-Ikhlas (Sincerity)",
            revelationTypeBn = "মাক্কী",
            arabicText = "قُلْ هُوَ اللَّهُ أَحَدٌ ۝ اللَّهُ الصَّمَدُ ۝ لَمْ يَلِدْ وَلَمْ يُولَدْ ۝ وَلَمْ يَكُن لَّهُ كُفُوًا أَحَدٌ",
            transliterationBn = "ক্বুল হুওয়াল্লা-হু আহাদ। আল্লা-হুস সামাদ। লাম ইয়ালিদ ওয়া লাম ইউ-লাদ। ওয়া লাম ইয়াকুল্লাহূ কুফুওয়ান আহাদ।",
            banglaTranslation = "বলুন: তিনিই আল্লাহ, একক ও অদ্বিতীয়। আল্লাহ কারো মুখাপেক্ষী নন, সকলেই তাঁর মুখাপেক্ষী। তিনি কাউকে জন্ম দেননি এবং তাঁকেও কেউ জন্ম দেয়নি। আর তাঁর সমকক্ষ কেউই নেই।",
            englishTranslation = "Say, 'He is Allah, [who is] One, Allah, the Eternal Refuge. He neither begets nor is born, Nor is there to Him any equivalent.'",
            whatDoesItTeachBn = "বিশুদ্ধ তাওহীদের মূল নির্যাস। আল্লাহ একক, অদ্বিতীয় এবং চিরন্তন আশ্রয়স্থল। সকল সৃষ্টি তাঁর ওপর নির্ভরশীল, অথচ তিনি কারও মুখাপেক্ষী নন।",
            whatToNoticeBn = "এই সূরা তিলাওয়াত কুরআনের এক-তৃতীয়াংশ পাঠের সমতুল্য সাওয়াব বহন করে। এটি সকল প্রকার শিরক ও অংশীদারিত্বের মূল উপড়ে ফেলে।",
            whatToBeCarefulAboutBn = "আল্লাহ ছাড়া অন্য কাউকে জীবনের ভাগ্যবিধাতা মনে করা বা কারও সামনে নত হওয়া থেকে সম্পূর্ণ বেঁচে থাকা।",
            whatCanIPracticeBn = "আজ সকাল-সন্ধ্যা এবং প্রতিটি সালাতের পর সুন্নাহ হিসেবে সূরা ইখলাস ৩ বার পাঠ করা।",
            quranSaysBn = "আল্লাহ অদ্বিতীয়, চিরন্তন আশ্রয়স্থল এবং তাঁর কোনো সমকক্ষ নেই।",
            scholarlyInterpretationBn = "সহীহ বুখারী: এক সাহাবী প্রতি রাকাতে এই সূরা ভালোবাসতেন; রাসূলুল্লাহ ﷺ বলেছিলেন, এই সূরার প্রতি তোমার ভালোবাসাই তোমাকে জান্নাতে প্রবেশ করাবে।",
            possiblePersonalApplicationBn = "নিজের অন্তরে একমাত্র আল্লাহর সন্তুষ্টিকে লক্ষ্য রেখে লোকদেখানো স্বভাব দূর করা।",
            reflectiveQuestions = listOf(
                "আমার প্রতিটি কাজে কি কেবল আল্লাহর সন্তুষ্টি থাকে নাকি মানুষের প্রশংসা পাওয়ার লোভ থাকে?",
                "আমি কি সর্বাবস্থায় কেবল আল্লাহকেই আমার পরম আশ্রয় মনে করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.WORSHIP to listOf("দৈনন্দিন জীবনে নিয়মিত সূরা ইখলাস পাঠের সুন্নাহ বজায় রাখুন।"),
                LifeSphere.PERSONAL_GROWTH to listOf("নিয়তের বিশুদ্ধতা (ইখলাস) বজায় রাখুন, লোকদেখানো আমল বর্জন করুন।")
            ),
            defaultTodayAction = "আজকে সকাল ও সন্ধ্যায় এবং ঘুমানোর আগে ৩ বার করে অত্যন্ত গভীর অনুধাবনের সাথে সূরা আল-ইখলাস তিলাওয়াত করুন।",
            primaryThemes = listOf("তাওহীদ", "একনিষ্ঠতা", "শিরকমুক্ত জীবন"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/112001.mp3"
        ),

        // 26. Al-Falaq 113:1-5 - Refuge from darkness, magic and jealousy
        AyahActionInsight(
            ayahId = "action_113_1",
            surahNumber = 113,
            ayahNumber = 1,
            surahNameArabic = "سورة الفلق",
            surahNameBangla = "সূরা আল-ফালাক (১১৩:১-৫)",
            surahNameEnglish = "Surah Al-Falaq (The Daybreak)",
            revelationTypeBn = "মাক্কী",
            arabicText = "قُلْ أَعُوذُ بِرَبِّ الْفَلَقِ ۝ مِن شَرِّ مَا خَلَقَ ۝ وَمِن شَرِّ غَاسِقٍ إِذَا وَقَبَ ۝ وَمِن شَرِّ النَّفَّاثَاتِ فِي الْعُقَدِ ۝ وَمِن شَرِّ حَاسِدٍ إِذَا حَسَدَ",
            transliterationBn = "ক্বুল আ‘ঊযু বিরাব্বিল ফালাক্ব। মিন শাররি মা- খালাক্ব। ওয়া মিন শাররি গা-সিক্বিন ইযা- ওয়াক্বাব। ওয়া মিন শাররিন নাফফা-সা-তি ফিল ‘উক্বাদ। ওয়া মিন শাররি হা-সিদিন ইযা- হাসাদ।",
            banglaTranslation = "বলুন: আমি আশ্রয় গ্রহণ করছি প্রভাতের রবের কাছে—তিনি যা সৃষ্টি করেছেন তার সমস্ত অনিষ্ট হতে, রাতের অন্ধকারের অনিষ্ট হতে যখন তা সমাগত হয়, গিরায় ফুঁকদানকারী জাদুকরদের অনিষ্ট হতে এবং হিংসুকের অনিষ্ট হতে যখন সে হিংসা করে।",
            englishTranslation = "Say, 'I seek refuge in the Lord of daybreak From the evil of that which He created And from the evil of darkness when it settles And from the evil of the blowers in knots And from the evil of an envier when he envies.'",
            whatDoesItTeachBn = "দৃশ্য ও অদৃশ্য সকল অনিষ্ট, কালাজাদু, বদনজর ও মানুষের হিংসা থেকে সুরক্ষার শ্রেষ্ঠ দুর্গ হলো আল্লাহর আশ্রয়।",
            whatToNoticeBn = "হিংসুক ব্যক্তি যখন হিংসা করে, তখন তার বিষাক্ত দৃষ্টি ও ষড়যন্ত্র অন্যের ক্ষতি করতে পারে; কেবল আল্লাহই এর থেকে পূর্ণ নিরাপত্তা দিতে পারেন।",
            whatToBeCarefulAboutBn = "নিজে কারও প্রতি অন্তরে হিংসা বা পরশ্রীকাতরতা পোষণ করা থেকে বেঁচে থাকা। কারও ভালো দেখলে 'মাশাআল্লাহ' বলা।",
            whatCanIPracticeBn = "আজ পরিবার, সন্তান ও নিজের নিরাপত্তার জন্য সকাল-সন্ধ্যা ৩ বার সূরা ফালাক পাঠ করে শরীরে ফুঁ দেওয়া।",
            quranSaysBn = "সৃষ্টিজগতের সমস্ত অনিষ্ট, অন্ধকার, জাদু ও হিংসা থেকে প্রভাতের রবের কাছে আশ্রয় চাওয়ার নির্দেশ।",
            scholarlyInterpretationBn = "তাফসীরে ইবনে কাসীর: রুকইয়াহ ও অনিষ্ট থেকে সুরক্ষার জন্য মু'আওওয়াযাতাইন (সূরা ফালাক ও নাস)-এর বিকল্প কোনো কিছু নেই।",
            possiblePersonalApplicationBn = "নিজের কোনো সাফল্য পেলে তা নিয়ে অতিরিক্ত অহংকারী প্রদর্শন না করা যাতে হিংসুকের দৃষ্টি না পড়ে।",
            reflectiveQuestions = listOf(
                "আমার অন্তরে কি কারও সাফল্যে কোনো সূক্ষ্ম হিংসা জন্মেছে?",
                "আমি কি নিয়মিত সুন্নাহ সম্মত সুরক্ষার দোয়া ও সূরা পাঠ করি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.CHARACTER to listOf("হিংসামুক্ত হৃদয় ধারণ করুন; অন্যের উন্নতিতে আনন্দিত হোন।"),
                LifeSphere.WORSHIP to listOf("সকাল-সন্ধ্যায় ৩ বার সূরা ফালাক পড়ার সুন্নাহ পালন করুন।")
            ),
            defaultTodayAction = "আজ কারও কোনো ভালো কিছু দেখলে মন থেকে 'বারাকাল্লাহু লাকা' (আল্লাহ আপনাকে বরকত দিন) বলে দোয়া করুন।",
            primaryThemes = listOf("সুরক্ষা ও হেফাযত", "হিংসা বর্জন", "রুকইয়াহ"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/113001.mp3"
        ),

        // 27. An-Nas 114:1-6 - Protection from whisperer
        AyahActionInsight(
            ayahId = "action_114_1",
            surahNumber = 114,
            ayahNumber = 1,
            surahNameArabic = "سورة الناس",
            surahNameBangla = "সূরা আন-নাস (১১৪:১-৬)",
            surahNameEnglish = "Surah An-Nas (Mankind)",
            revelationTypeBn = "মাক্কী",
            arabicText = "قُلْ أَعُوذُ بِرَبِّ النَّاسِ ۝ مَلِكِ النَّاسِ ۝ إِلَٰهِ النَّاسِ ۝ مِن شَرِّ الْوَسْوَاسِ الْخَنَّاسِ ۝ الَّذِي يُوَسْوِسُ فِي صُدُورِ النَّاسِ ۝ مِنَ الْجِنَّةِ وَالنَّاسِ",
            transliterationBn = "ক্বুল আ‘ঊযু বিরাব্বিন না-স। মালিকিন না-স। ইলা-হিন না-স। মিন শাররিল ওয়াসওয়া-সিল খান্না-স। আল্লাযী ইউওয়াসউিসু ফী সুদূরিন না-স। মিনাল জিন্নাতি ওয়ান-না-স।",
            banglaTranslation = "বলুন: আমি আশ্রয় চাচ্ছি মানুষের প্রতিপালকের, মানুষের অধিপতির, মানুষের একমাত্র সত্য মা‘বুদের কাছে—গোপনে কুমন্ত্রণাদানকারী সেই শয়তানের অনিষ্ট হতে যে বারবার পিছু হটে, যে মানুষের অন্তরে সংশয় ও কুমন্ত্রণা বিস্তার করে, জ্বিনের মধ্য হতে এবং মানুষের মধ্য হতেও।",
            englishTranslation = "Say, 'I seek refuge in the Lord of mankind, The Sovereign of mankind, The God of mankind, From the evil of the retreating whisperer - Who whispers into the breasts of mankind - From among the jinn and mankind.'",
            whatDoesItTeachBn = "শয়তানের সবচেয়ে বড় অস্ত্র হলো অন্তরে সংশয়, নেতিবাচক চিন্তা, কুপ্রবৃত্তি ও পাপের কুমন্ত্রণা ঢেলে দেওয়া। আল্লাহর জিকিরই শয়তানকে পরাভূত করার একমাত্র ঢাল।",
            whatToNoticeBn = "কুমন্ত্রণাদাতা কেবল অদৃশ্য শয়তান নয়, খারাপ বন্ধু বা অসৎ মানুষের সঙ্গও মানুষকে ধ্বংসের পথে নিয়ে যায়।",
            whatToBeCarefulAboutBn = "মনে কোনো পাপের কুচিন্তা এলে তাতে ডুবে না থেকে সাথে সাথে 'আউযুবিল্লাহ' পড়ে সচেতনভাবে মনকে সৎকাজে ফিরিয়ে নেওয়া।",
            whatCanIPracticeBn = "আজ যেকোনো অসৎ বা হতাশার ভাবনা মাথায় আসামাত্র 'আউযুবিল্লাহি মিনাশ শাইতানির রাজিম' পাঠ করে মনকে ঝেড়ে ফেলা।",
            quranSaysBn = "শয়তানের অদৃশ্য কুমন্ত্রণা ও পথভ্রষ্ট মানুষের অনিষ্ট থেকে মানুষের রবের কাছে আশ্রয় প্রার্থনা।",
            scholarlyInterpretationBn = "তাফসীর আহসানুল বায়ান: 'খান্নাস' হলো এমন শয়তান যে জিকির শুনলে পালিয়ে যায় এবং উদাসীন হলে পুনরায় আক্রমণ করে।",
            possiblePersonalApplicationBn = "খারাপ সঙ্গ ও অসৎ আড্ডা থেকে নিজেকে সযতনে বাঁচিয়ে রাখা।",
            reflectiveQuestions = listOf(
                "আমার মনের কোন নেতিবাচক চিন্তাগুলো আসলে শয়তানের অদৃশ্য কুমন্ত্রণা?",
                "আমি কি প্রতিদিন জিকিরের মাধ্যমে আমার আত্মাকে সুরক্ষিত রাখি?"
            ),
            applicationsBySphere = mapOf(
                LifeSphere.MINDSET to listOf("নেতিবাচক ভাবনা ও সংশয় এলে সাথে সাথে আল্লাহর আশ্রয় নিন।"),
                LifeSphere.RELATIONSHIPS to listOf("খারাপ সঙ্গ ত্যাগ করে ভালো ও নেককার মানুষের সাথে সময় কাটান।")
            ),
            defaultTodayAction = "ঘুমানোর পূর্বে সূরা ইখলাস, ফালাক ও নাস পড়ে দুই হাতের তালুতে ফুঁ দিয়ে সারা শরীরে ৩ বার হাত বুলিয়ে নিন।",
            primaryThemes = listOf("শয়তানের কুমন্ত্রণা থেকে মুক্তি", "আত্মিক দুর্গ", "জিকির"),
            audioUrl = "https://everyayah.com/data/Alafasy_128kbps/114001.mp3"
        )
    )
}
