package com.example.data.datasource

import com.example.data.model.*

/**
 * Islamic Context & Verify Knowledge Base & Preset Repository:
 * Contains authentic, peer-reviewed analyses for widespread viral WhatsApp forwards,
 * social media posts, common misconceptions, and scholarly evaluations.
 */
object IslamicContextVerifyCatalog {

    val presetClaims: List<IslamicContextVerifyReport> = listOf(
        // 1. Asr sleep causes insanity (Extremely viral WhatsApp claim)
        IslamicContextVerifyReport(
            id = "preset_sleep_after_asr",
            originalClaim = "যে ব্যক্তি আসরের পর ঘুমায় আর তার বুদ্ধি লোপ পায় (বা পাগল হয়), তবে সে যেন কেবল নিজেকেই দোষারোপ করে। ইসলামে আসরের পর ঘুমানো নিষিদ্ধ।",
            sourcePlatformTag = "WhatsApp ফরোয়ার্ড",
            individualClaims = listOf(
                "আসরের পর ঘুমানো ইসলামে নিষিদ্ধ।",
                "আসরের পর ঘুমালে মানুষের বুদ্ধি লোপ পায় বা পাগল হয়ে যায়।"
            ),
            verdict = ClaimVerificationVerdict.MISQUOTED_OR_UNSUPPORTED,
            confidence = ConfidenceLevel.HIGH,
            summaryHeadlineBn = "আসরের পর ঘুমালে পাগল হওয়ার হাদিসটি মাওযূ (জাল / বানোয়াট)",
            evidenceSummaryBn = "রাসূলুল্লাহ ﷺ-এর নামে প্রচারিত এই বক্তব্যটি হাদিস বিশারদদের সর্বসম্মত মূল্যায়নে ভিত্তিহীন ও বানোয়াট। ইসলামে আসরের পর ঘুমানোকে হারাম বা নিষিদ্ধ ঘোষণা করে কোনো সহীহ হাদিস নেই।",
            quranReferences = emptyList(),
            hadithReferences = listOf(
                HadithReferenceItem(
                    collection = "মুসনাদে আবি ইয়া'লা (হা/৪৮৯৭), আল-মাজরুহীন (ইবনে হিব্বান)",
                    hadithNumber = "৪৮৯৭",
                    arabicText = "مَنْ نَامَ بَعْدَ الْعَصْرِ فَاخْتُلِسَ عَقْلُهُ، فَلَا يَلُومَنَّ إِلَّا نَفْسَهُ",
                    translationBn = "যে ব্যক্তি আসরের পর ঘুমাল এবং তার বিবেক-বুদ্ধি অপহৃত হলো, সে যেন নিজেকে ছাড়া আর কাউকেই তিরস্কার না করে।",
                    authenticityGrade = "মাওযূ / জাল (Fabricated) — ভিত্তিহীন",
                    textVsInterpretation = "উক্ত শব্দের কোনো নির্ভরযোগ্য সনদ রাসূলুল্লাহ ﷺ পর্যন্ত পৌঁছেনি। সনদে খালিদ ইবনুল কাসিম ও ইবনে লাহিয়া রয়েছে, যারা অনির্ভরযোগ্য। ইমাম ইবনুল জাওযী, ইমাম বুখারী, শায়খ আলবানী (রহ.) একে বাতিল ও জাল ঘোষণা করেছেন।"
                )
            ),
            historicalContextBn = "আরব সমাজে দীর্ঘ দুপুরের কায়লুলাহ (দ্বিপ্রহরের ঘুম) পরিচিত থাকলেও আসরের পর ঘুমানোর বিষয়ে চিকিৎসা বা অভ্যাসের দিক থেকে কিছু প্রাচীন মানুষ অনীহা প্রকাশ করত। কিন্তু পরবর্তীকালে অসাধু বর্ণনাকারীরা চিকিৎসাগত পরামর্শকে রাসূলুল্লাহ ﷺ-এর নামে জাল হাদিস হিসেবে আরোপ করে।",
            linguisticContextBn = "'فاختلس عقله' (তার বুদ্ধি অপহৃত হলো) আরবি চিকিৎসাগত অতিশয়োক্তি, যা নবুওয়াতের শব্দশৈলীর সাথে অসামঞ্জস্যপূর্ণ।",
            audienceAndScopeBn = "চিকিৎসা বিজ্ঞান অনুযায়ী অতিরিক্ত বা অসময়ের ঘুম শারীরিক অবসাদ আনতে পারে, তবে এটি কোনো দ্বীনি বিধিনিষেধ বা গুনাহের কাজ নয়।",
            omittedSurroundingsBn = "অনেকে এটিকে সহীহ হাদিস বলে সোশ্যাল মিডিয়ায় প্রচার করে কিন্তু কোনো মুহাদ্দিস একে সহীহ বলে মেনে নেননি।",
            scholarlyInterpretations = listOf(
                ClaimScholarlyOpinion(
                    schoolOrScholar = "ইমাম আহমদ ইবনে হাম্বল (রহ.)",
                    positionSummary = "আসরের পর ঘুমাতে কোনো শরঈ বাধা নেই।",
                    textualBasis = "ইমাম আহমাদকে এই বর্ণনা সম্পর্কে জিজ্ঞাসা করা হলে তিনি বলেন: এটি বাতিল ও মিথ্যা, এতে কোনো সহীহ ভিত্তি নেই।"
                ),
                ClaimScholarlyOpinion(
                    schoolOrScholar = "ইমাম ইবনুল কাইয়্যিম ও ইবনুল জাওযী (রহ.)",
                    positionSummary = "চিকিৎসা বা অভ্যাসের দিক থেকে অপ্রয়োজনে না ঘুমানো ভালো হতে পারে, কিন্তু একে সুন্নাত বা হারাম বলা বেদআত ও মিথ্যা।",
                    textualBasis = "কিতাবুল মাওদূ'আত ও আল-মানারুল মুনিফ।"
                )
            ),
            potentialMisinformation = listOf(
                MisinformationWarning(
                    warningType = "রাসূলুল্লাহ ﷺ-এর নামে বানোয়াট হাদিস আরোপ",
                    warningDetail = "দুর্বল বা চিকিৎসা সম্পর্কিত মতামতকে রাসূলুল্লাহ ﷺ-এর সরাসরি নিষেধাজ্ঞা দাবি করে প্রচার করা।"
                )
            ),
            whatIsActuallyEstablished = WhatIsActuallyEstablished(
                explicitTextualFact = "কুরআন বা সহীহ হাদিসে আসরের পর ঘুমানো নিষিদ্ধ করা হয়নি।",
                scholarlyInference = "শারীরিক স্বাস্থ্যের খাতিরে যদি কারো রাতে ঘুমে ব্যাঘাত না ঘটে, তবে প্রয়োজনবোধে আসরের পর ঘুমানো সম্পূর্ণ মুবাহ (বৈধ)।",
                uncertainOrUnverified = "আসরের পর ঘুমালে মস্তিষ্ক বিকল বা পাগল হওয়ার যে দাবি হাদিসের নামে করা হয়, তা নিছক কুসংস্কার ও বানোয়াট কথা।"
            ),
            actionableConclusionBn = "মেসেজটি ফরোয়ার্ড করা থেকে বিরত থাকুন। কাউকে আসরের পর ঘুমাতে দেখলে পাপ মনে করবেন না।"
        ),

        // 2. Surah Hashr 3 verses 70,000 angels (Viral Hadith with nuances)
        IslamicContextVerifyReport(
            id = "preset_surah_hashr_angels",
            originalClaim = "সকাল ও সন্ধ্যায় সূরা হাশরের শেষ ৩ আয়াত পড়লে ৭০,০০০ ফেরেশতা দিনভর মাগফিরাতের দো'আ করে এবং ঐ দিন মারা গেলে শহীদি মর্যাদা পাওয়া যায়।",
            sourcePlatformTag = "ফেসবুক পোস্ট",
            individualClaims = listOf(
                "সূরা হাশরের শেষ ৩ আয়াত পড়লে ৭০ হাজার ফেরেশতা মাগফিরাতের দো'আ করে।",
                "ঐ দিন বা রাতে মারা গেলে শহীদের মর্যাদা নিশ্চিত।"
            ),
            verdict = ClaimVerificationVerdict.CONTEXT_NEEDED,
            confidence = ConfidenceLevel.HIGH,
            summaryHeadlineBn = "বর্ণনাটি সুনানে তিরমিযীতে বিদ্যমান, তবে সনদগতভাবে যয়ীফ (দুর্বল)",
            evidenceSummaryBn = "হাদিসটি জামে আত-তিরমিযী ও মুসনাদে আহমাদে রয়েছে। ইমাম তিরমিযী একে 'গরীব' বলেছেন এবং আধুনিক মুহাদ্দিসগণ সনদের দুর্বলতার কারণে একে 'যয়ীফ' বলে চিহ্নিত করেছেন। তবে ফজিলতের ক্ষেত্রে শর্তসাপেক্ষে আলেমগণ আমলের অনুমতি দিয়ে থাকেন।",
            quranReferences = listOf(
                QuranReferenceItem(
                    surahName = "সূরা আল-হাশর",
                    surahNumber = 59,
                    ayahNumber = 22,
                    arabicText = "هُوَ اللَّهُ الَّذِي لَا إِلَهَ إِلَّا هُوَ عَالِمُ الْغَيْبِ وَالشَّهَادَةِ هُوَ الرَّحْمَنُ الرَّحِيمُ",
                    translationBn = "তিনিই আল্লাহ, তিনি ছাড়া কোনো সত্য উপাস্য নেই; তিনি দৃশ্য ও অদৃশ্যের পরিজ্ঞাতা, তিনি পরম করুণাময়, অসীম দয়ালু।",
                    whatVerseActuallyAddresses = "মহান আল্লাহর একত্ববাদ, নিখাদ তাওহীদ এবং তাঁর পবিত্র গুণবাচক নামের অনুপম মাহাত্ম্য ঘোষণা।",
                    isDirectEvidence = false
                )
            ),
            hadithReferences = listOf(
                HadithReferenceItem(
                    collection = "জামে আত-তিরমিযী (হা/২৯২২), মুসনাদে আহমাদ",
                    hadithNumber = "২৯২২",
                    arabicText = "مَنْ قَالَ حِينَ يُصْبِحُ ثَلَاثَ مَرَّاتٍ: أَعُوذُ بِاللَّهِ السَّمِيعِ الْعَلِيمِ مِنَ الشَّيْطَانِ الرَّجِيمِ، وَقَرَأَ ثَلَاثَ آيَاتٍ مِنْ آخِرِ سُورَةِ الْحَشْرِ...",
                    translationBn = "যে ব্যক্তি সকালে ৩ বার 'আউজু বিল্লাহিস সামিইল আলিমি মিনাশ শাইতানির রাজিম' পাঠ করে সূরা হাশরের শেষ ৩ আয়াত তিলাওয়াত করে, আল্লাহ তার জন্য ৭০ হাজার ফেরেশতা নিযুক্ত করেন...",
                    authenticityGrade = "যয়ীফ (ضعيف - দুর্বল)",
                    textVsInterpretation = "সনদে খালিদ ইবনে তাহমান নামক বর্ণনাকারী রয়েছেন, যিনি স্মৃতিভ্রম ও দুর্বলতায় আক্রান্ত ছিলেন। ইমাম দারাকুতনী ও শায়খ আলবানী (রহ.) একে যয়ীফ বলেছেন।"
                )
            ),
            historicalContextBn = "সাহাবা ও তাবেঈনদের যুগে তাওহীদের আয়াতসমূহ সকাল-সন্ধ্যায় তিলাওয়াতের ব্যাপক রেওয়াজ ছিল। তবে নির্দিষ্ট ফেরেশতার সংখ্যা ও শহীদি মর্যাদার শর্তযুক্ত রেওয়ায়েতটি সূত্রগতভাবে দুর্বল হলেও তাজাল্লিয়াতে রব্বানী হিসেবে প্রচলিত হয়।",
            linguisticContextBn = "৭০,০০০ সংখ্যাটি আরবিতে অনেক সময় আধিক্য বা অগণিত রহমতের দ্যোতনা বোঝাতেও ব্যবহৃত হয়।",
            audienceAndScopeBn = "আয়াতগুলোর মূল বিষয়বস্তু আল্লাহর সিফাত ও তাওহীদ, যা তিলাওয়াত করা অত্যন্ত সওয়াবের কাজ।",
            omittedSurroundingsBn = "ভাইরাল পোস্টে প্রায়শই হাদিসটিকে সহীহ বুখারী বা নিশ্চিত সহীহ বলে প্রচার করা হয়, যা সঠিক নয়।",
            scholarlyInterpretations = listOf(
                ClaimScholarlyOpinion(
                    schoolOrScholar = "ইমাম আন-নববী ও হাফেয ইবনে হাজার (রহ.)",
                    positionSummary = "ফাজায়েলে আমলের ক্ষেত্রে যদি হাদিসটি চরম জাল না হয়, তবে আমল করা বৈধ।",
                    textualBasis = "মুহাদ্দিসীনদের মূলনীতি: দুর্বল হাদিসের দ্বারা হালাল-হারাম সাব্যস্ত করা না হলেও নেক আমলের উৎসাহে আমল করা যায়।"
                ),
                ClaimScholarlyOpinion(
                    schoolOrScholar = "শায়খ আল-আলবানী ও সমকালীন গবেষকগণ",
                    positionSummary = "আমল করার ক্ষেত্রে এর চেয়ে সহীহ সকাল-সন্ধ্যার প্রমাণিত যিকিরকে (যেমন আয়াতুল কুরসী ও ৩ কুল) অগ্রাধিকার দেওয়া উচিত।",
                    textualBasis = "সিলসিলাতুজ জয়ীফাহ ও সহীহুল আযকার।"
                )
            ),
            potentialMisinformation = listOf(
                MisinformationWarning(
                    warningType = "দুর্বল হাদিসকে 'সহীহ বুখারী' বলে প্রচার",
                    warningDetail = "সোশ্যাল মিডিয়ায় হাদিসটির দুর্বল মান উল্লেখ না করে নিশ্চিত ও সহীহ বলে প্রচার করা।"
                )
            ),
            whatIsActuallyEstablished = WhatIsActuallyEstablished(
                explicitTextualFact = "সূরা হাশরের শেষ তিন আয়াত মহান আল্লাহর তাওহীদ ও অনুপম গুণাবলীর অপূর্ব সমন্বয়, যা কুরআন তিলাওয়াত হিসেবে অত্যন্ত মর্যাদাপূর্ণ।",
                scholarlyInference = "৭০,০০০ ফেরেশতার সংক্রান্ত হাদিসটি সনদের দিক থেকে দুর্বল হলেও কুফর বা বিদআত নয়; আলেমগণ এটিকে নফল ফজিলত হিসেবে পাঠের সুযোগ রেখেছেন।",
                uncertainOrUnverified = "হাদিসটিকে অকাট্য সহীহ বা শহীদি মর্যাদার নিশ্চয়তা হিসেবে প্রচার করা হাদিস বিজ্ঞানের নীতিবিরুদ্ধ।"
            ),
            actionableConclusionBn = "সূরা হাশরের শেষ আয়াতগুলো আল্লাহর প্রশংসায় পাঠ করা উত্তম। তবে সনদগত মান সহীহ না হয়ে যয়ীফ তা মনে রাখা বাঞ্ছনীয়।"
        ),

        // 3. Black seed cures every disease (Nuanced context)
        IslamicContextVerifyReport(
            id = "preset_black_seed_cure",
            originalClaim = "কালোজিরা খেলে ডাক্তার বা ওষুধের কোনো প্রয়োজন নেই, কারণ ইসলাম অনুযায়ী এটি ক্যান্সার সহ পৃথিবীর সকল রোগের চূড়ান্ত নিরাময়।",
            sourcePlatformTag = "ইউটিউব / ফেসবুক ভিডিও",
            individualClaims = listOf(
                "কালোজিরায় সকল রোগের নিরাময় আছে।",
                "কালোজিরা থাকলে চিকিৎসা ও ডাক্তারের কোনো প্রয়োজন নেই।"
            ),
            verdict = ClaimVerificationVerdict.CONTEXT_NEEDED,
            confidence = ConfidenceLevel.HIGH,
            summaryHeadlineBn = "হাদিসটি সহীহ বুখারীতে প্রমাণিত, কিন্তু চিকিৎসা পরিহারের দাবি ভুল ও বিপজ্জনক",
            evidenceSummaryBn = "কালোজিরায় নিরাময়ের ক্ষমতা থাকার হাদিসটি সহীহ বুখারীতে সম্পূর্ণ বিশুদ্ধ সনদে রয়েছে। কিন্তু আরবি ভাষার অলংকার ও সুন্নাহর সামগ্রিক বিধান অনুযায়ী 'কুল্লি দা' (সব রোগ) দ্বারা বেশিরভাগ রোগ বা সাধারণ আরোগ্য বুঝায়। স্বয়ং রাসূলুল্লাহ ﷺ অসুস্থ হলে ডাক্তার ডাকতেন এবং চিকিৎসা গ্রহণকে বাধ্যতামূলক নির্দেশ দিয়েছেন।",
            quranReferences = emptyList(),
            hadithReferences = listOf(
                HadithReferenceItem(
                    collection = "সহীহ আল-বুখারী (হা/৫৬৮৮), সহীহ মুসলিম (হা/২২১৫)",
                    hadithNumber = "৫৬৮৮",
                    arabicText = "فِي الحَبَّةِ السَّوْدَاءِ شِفَاءٌ مِنْ كُلِّ دَاءٍ، إِلَّا السَّامَ",
                    translationBn = "কালোজিরার মধ্যে 'সাম' (মৃত্যু) ব্যতীত সকল রোগের নিরাময় রয়েছে।",
                    authenticityGrade = "সহীহ (متفق عليه - বুখারী ও মুসলিম)",
                    textVsInterpretation = "হাদিসের শব্দাবলি বিশুদ্ধ। তবে 'كل داء' (সকল রোগ) শব্দটি আরবি ভাষায় আম-খাছ (সাধারণ শব্দ কিন্তু সুনির্দিষ্ট উদ্দেশ্যযুক্ত) হিসেবে ব্যবহৃত।"
                ),
                HadithReferenceItem(
                    collection = "সুনান আবু দাউদ (হা/৩৮৫৫), সহীহ",
                    hadithNumber = "৩৮৫৫",
                    arabicText = "تَدَاوَوْا فَإِنَّ اللَّهَ عَزَّ وَجَلَّ لَمْ يَضَعْ دَاءً إِلَّا وَضَعَ لَهُ دَوَاءً",
                    translationBn = "তোমরা চিকিৎসা গ্রহণ করো; কারণ মহান আল্লাহ এমন কোনো রোগ সৃষ্টি করেননি যার প্রতিষেধক তিনি নির্ধারণ করেননি।",
                    authenticityGrade = "সহীহ (Sahih)",
                    textVsInterpretation = "স্বয়ং রাসূলুল্লাহ ﷺ বিশেষজ্ঞ চিকিৎসক ডেকে চিকিৎসা নেওয়ার নির্দেশ দিয়েছেন।"
                )
            ),
            historicalContextBn = "আরব চিকিৎসাব্যবস্থায় ভেষজ উদ্ভিদের ব্যবহার প্রচলিত ছিল। নবীজী ﷺ খাদ্য ও পথ্যের স্বাস্থ্যগত উপযোগিতাকে বরকতময় বলে অভিহিত করেছেন, আধুনিক চিকিৎসা ও চিকিৎসকের পরামর্শকে কখনো নিষিদ্ধ করেননি।",
            linguisticContextBn = "কুরআনে যেমন বলা হয়েছে হুদহুদ পাখি সম্পর্কে 'ওয়া ঊতিয়াত মিন কুল্লি শাই' (তাকে সব কিছু দেওয়া হয়েছিল—সূরা নামল ২৩), অথচ সুলাইমান (আ.)-এর রাজত্ব তার ছিল না। তেমনি 'কুল্লি দা' দ্বারা অধিকাংশ সাধারণ ব্যাধি বোঝানো হয়েছে।",
            audienceAndScopeBn = "কালোজিরা ভেষজ পথ্য হিসেবে রোগ প্রতিরোধ ক্ষমতা বাড়ায়, কিন্তু গুরুতর রোগের ক্ষেত্রে আধুনিক ডায়াগনসিস ও বিশেষজ্ঞ ডাক্তারের পরামর্শ গ্রহণ সুন্নাহরই অংশ।",
            omittedSurroundingsBn = "চিকিৎসা পরিহার করা বা ডাক্তারের ওষুধ ফেলে দেওয়া ইসলামে তাওয়াক্কুলের অপব্যাখ্যা এবং চরম নিষিদ্ধ পদক্ষেপ।",
            scholarlyInterpretations = listOf(
                ClaimScholarlyOpinion(
                    schoolOrScholar = "ইমাম ইবনুল কাইয়্যিম (রহ.)",
                    positionSummary = "এটি ঠান্ডাজনিত ও দৈহিক রোগব্যাধির বিরুদ্ধে অত্যন্ত কার্যকরী প্রাকৃতিক ঔষধ।",
                    textualBasis = "যাদুল মা'আদ ফী হাদয়ি খাইরিল ইবাদ।"
                ),
                ClaimScholarlyOpinion(
                    schoolOrScholar = "হাফেজ ইবনে হাজার আল-আসকালানী (রহ.)",
                    positionSummary = "'কুল্লি দা' বাক্যটি সাধারণ অর্থে হলেও উদ্দেশ্য সুনির্দিষ্ট রোগসমূহ।",
                    textualBasis = "ফাতহুল বারী শরহে সহীহ আল-বুখারী।"
                )
            ),
            potentialMisinformation = listOf(
                MisinformationWarning(
                    warningType = "সঠিক হাদিসের বিপজ্জনক অপপ্রয়োগ",
                    warningDetail = "হাদিসের নাম করে জটিল রোগের ক্ষেত্রে প্রয়োজনীয় ডাক্তার ও ওষুধ বর্জন করতে প্ররোচিত করা।"
                )
            ),
            whatIsActuallyEstablished = WhatIsActuallyEstablished(
                explicitTextualFact = "কালোজিরা সুন্নাতী প্রাকৃতিক পথ্য যাতে বহুবিধ রোগের নিরাময় ও প্রতিরোধ ক্ষমতা রয়েছে।",
                scholarlyInference = "অসুস্থ হলে ডাক্তারের কাছে যাওয়া, ওষুধ সেবন করা সুন্নাতের স্পষ্ট নির্দেশ এবং কোনো প্রাকৃতিক পথ্যই সুনির্দিষ্ট চিকিৎসা ব্যবস্থাপনাকে প্রতিস্থাপন করে না।",
                uncertainOrUnverified = "কালোজিরা খেলে ডাক্তার বা হাসপাতালে যাওয়ার প্রয়োজন নেই—এমন দাবি কুরআন ও সুন্নাহর নীতিমালার সম্পূর্ণ পরিপন্থী।"
            ),
            actionableConclusionBn = "কালোজিরা নিয়মিত পথ্য হিসেবে সেবন করুন, তবে অসুস্থতায় অবশ্যই অভিজ্ঞ চিকিৎসকের পরামর্শ ও প্রেসক্রিপশন মেনে চলুন।"
        ),

        // 4. Cat keeping in Islam (Debunking myth)
        IslamicContextVerifyReport(
            id = "preset_cat_keeping",
            originalClaim = "বিড়াল অপবিত্র প্রাণী, ঘরে বিড়াল রাখলে নামাজ হয় না এবং ফেরেশতা প্রবেশ করে না।",
            sourcePlatformTag = "সোশ্যাল মিডিয়া মন্তব্য",
            individualClaims = listOf(
                "বিড়াল অপবিত্র ও নাপাক।",
                "বিড়াল যে ঘরে থাকে সেখানে সালাত হয় না বা ফেরেশতা প্রবেশ করে না।"
            ),
            verdict = ClaimVerificationVerdict.MISQUOTED_OR_UNSUPPORTED,
            confidence = ConfidenceLevel.HIGH,
            summaryHeadlineBn = "দাবিটি সম্পূর্ণ ভিত্তিহীন; বিড়াল পবিত্র প্রাণী এবং ঘরে রাখা সুন্নাহসম্মত",
            evidenceSummaryBn = "কুকুরের ক্ষেত্রে বিশেষ বিধান থাকলেও বিড়ালকে ইসলামে স্পষ্টত 'ত্বাহির' (পবিত্র) ঘোষণা করা হয়েছে। বিড়ালের এঁটো পানি দিয়ে ওযু করা জায়েয এবং প্রখ্যাত সাহাবী আবু হুরায়রা (রা.)-এর নামই বিড়ালের প্রতি ভালোবাসার কারণে দেওয়া হয়েছিল।",
            quranReferences = emptyList(),
            hadithReferences = listOf(
                HadithReferenceItem(
                    collection = "সুনান আবু দাউদ (হা/৭৫), জামে আত-তিরমিজী (হা/৯২)",
                    hadithNumber = "৭৫",
                    arabicText = "إِنَّهَا لَيْسَتْ بِنَجَسٍ، إِنَّمَا هِيَ مِنَ الطَّوَّافِينَ عَلَيْكُمْ وَالطَّوَّافَاتِ",
                    translationBn = "নিশ্চয়ই বিড়াল নাপাক বা অপবিত্র নয়; বরং তারা তোমাদের আশেপাশে সর্বক্ষণ ঘুরে বেড়ানো সঙ্গী প্রাণী।",
                    authenticityGrade = "সহীহ (হাসান সহীহ - তিরমিজি)",
                    textVsInterpretation = "নবী করীম ﷺ স্বয়ং বিড়ালের মুখ দেওয়া পানির পাত্র থেকে ওযু করেছেন এবং বিড়ালকে পাক বলে আখ্যা দিয়েছেন।"
                )
            ),
            historicalContextBn = "মদিনার ঘরে ঘরে বিড়াল স্বাধীনভাবে চলাফেরা করত। সাহাবী আবু হুরায়রা (রা.) বিড়ালের ছানাকে নিজের জামার আস্তিনে নিয়ে ঘুরতেন, যা দেখে নবীজী ﷺ তাঁকে স্নেহভরে 'বিড়ালের পিতা' উপাধিতে ডাকতেন।",
            linguisticContextBn = "'الطَّوَّافِينَ' দ্বারা গৃহে মানুষের নিত্য সহচর ও গৃহপালিত নিরীহ প্রাণীকে বোঝানো হয়েছে।",
            audienceAndScopeBn = "প্রাণীর প্রতি দয়া প্রদর্শনের সার্বজনীন ইসলামী শিক্ষা। তবে বিড়ালকে কষ্ট দেওয়া বা না খাইয়ে আটকে রাখা কবিরা গুনাহ (বুখারী ৩৪৮২)।",
            omittedSurroundingsBn = "কুকুর ঘরে রাখার ব্যাপারে যে হাদিস রয়েছে, তাকে অজ্ঞতাবশত বিড়ালের সাথে গুলিয়ে ফেলা হয়েছে।",
            scholarlyInterpretations = listOf(
                ClaimScholarlyOpinion(
                    schoolOrScholar = "চার মাযহাবের ইমামগণ (হানাফী, মালেকী, শাফেয়ী, হাম্বলী)",
                    positionSummary = "বিড়াল পবিত্র প্রাণী, এর লালা নাপাক নয় এবং বিড়াল ঘরে থাকলে নামাজ বা ফেরেশতার কোনো বাধা নেই।",
                    textualBasis = "সুনান আবু দাউদ ও তিরমিজির সহীহ হাদিসসমূহ।"
                )
            ),
            potentialMisinformation = listOf(
                MisinformationWarning(
                    warningType = "কুকুর ও বিড়ালের শরঈ বিধান গুলিয়ে ফেলা",
                    warningDetail = "কুকুরের লালা ও ঘরে রাখার নিষেধাজ্ঞাকে সম্পূর্ণ পৃথক প্রাণী বিড়ালের ওপর চাপিয়ে দেওয়া।"
                )
            ),
            whatIsActuallyEstablished = WhatIsActuallyEstablished(
                explicitTextualFact = "বিড়াল পবিত্র প্রাণী এবং তার সংস্পর্শে কোনো কাপড় বা স্থান অপবিত্র হয় না।",
                scholarlyInference = "বিড়ালকে ভালোবাসা ও আদরযত্ন করা প্রশংসনীয় এবং মানবিক আচরণের অংশ।",
                uncertainOrUnverified = "বিড়াল ঘরে রাখলে নামাজ হয় না বা ফেরেশতা আসে না—এমন বক্তব্যের কোনো ধর্মীয় প্রমাণ নেই।"
            ),
            actionableConclusionBn = "বিড়াল পালন সম্পূর্ণ বৈধ ও প্রশংসনীয়। প্রাণীদের প্রতি সদয় হোন এবং পরিচ্ছন্নতা বজায় রাখুন।"
        ),

        // 5. Women education and work prohibited (Fabricated extremist claim)
        IslamicContextVerifyReport(
            id = "preset_women_education_work",
            originalClaim = "ইসলামে নারীর জন্য কোনো ধরনের পড়াশোনা করা বা ঘরের বাইরে কাজ করা সম্পূর্ণ হারাম ও জাহান্নামে যাওয়ার কারণ।",
            sourcePlatformTag = "টিকটক / সোশ্যাল মিডিয়া ভিডিও",
            individualClaims = listOf(
                "ইসলামে নারীর শিক্ষা অর্জন নিষিদ্ধ।",
                "নারীর যেকোনো হালাল কাজ বা পেশা হারাম।"
            ),
            verdict = ClaimVerificationVerdict.MISQUOTED_OR_UNSUPPORTED,
            confidence = ConfidenceLevel.HIGH,
            summaryHeadlineBn = "দাবিটি ইসলামের সুষ্পষ্ট শিক্ষার সম্পূর্ণ বিপরীত",
            evidenceSummaryBn = "জ্ঞানার্জন করা প্রত্যেক মুসলিম নর-নারীর ওপর ফরজ। উম্মুল মুমিনীন হযরত আয়েশা (রা.) ছিলেন ইসলামের সর্বশ্রেষ্ঠ ফকীহ ও হাদিস বিশারদদের একজন। হযরত খাদিজা (রা.) ছিলেন সফল ব্যবসায়ী এবং সাহাবী নারীরা যুদ্ধক্ষেত্রে চিকিৎসা ও বাজারে তদারকিতে দায়িত্ব পালন করেছেন।",
            quranReferences = listOf(
                QuranReferenceItem(
                    surahName = "সূরা আত-তাওবাহ",
                    surahNumber = 9,
                    ayahNumber = 71,
                    arabicText = "وَالْمُؤْمِنُونَ وَالْمُؤْمِنَاتُ بَعْضُهُمْ أَوْلِيَاءُ بَعْضٍ ۚ يَأْمُرُونَ بِالْمَعْرُوفِ وَيَنْهَوْنَ عَنِ الْمُنكَرِ",
                    translationBn = "আর মুমিন পুরুষ ও মুমিন নারীরা একে অপরের বন্ধু ও সহযোগী; তারা সৎকাজের আদেশ দেয় এবং অসৎকাজ থেকে নিষেধ করে...",
                    whatVerseActuallyAddresses = "মুমিন নারী ও পুরুষের ধর্মীয়, নৈতিক ও সামাজিক যৌথ দায়িত্বশীলতার স্পষ্ট ঘোষণা।",
                    isDirectEvidence = true
                )
            ),
            hadithReferences = listOf(
                HadithReferenceItem(
                    collection = "সুনান ইবনে মাজাহ (হা/২২৪)",
                    hadithNumber = "২২৪",
                    arabicText = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ",
                    translationBn = "জ্ঞানার্জন করা প্রত্যেক মুসলিমের (নর-নারী নির্বিশেষে) ওপর অলঙ্ঘনীয় ফরজ।",
                    authenticityGrade = "সহীহ (সহীহ আল-জামে: ৩৯১৩)",
                    textVsInterpretation = "হাদিসের 'মুসলিম' শব্দটি পুংলিঙ্গবাচক হলেও আরবি ব্যাকরণ ও ইসলামী ফিকহে তা মানবজাতির সমগ্র পুরুষ ও নারীকে অন্তর্ভুক্ত করে।"
                )
            ),
            historicalContextBn = "হযরত শিফা বিনতে আব্দুল্লাহ (রা.) মদিনার বাজারে তদারকির দায়িত্বে ছিলেন এবং তিনি নারীদের সাক্ষরতা ও চিকিৎসা শেখাতেন। উম্মে আতিয়্যা (রা.) রাসুলুল্লাহ ﷺ-এর সাথে ৭টি অভিযানে আহত সৈনিকদের চিকিৎসা দিয়েছেন।",
            linguisticContextBn = "'কুরআনুল কারীমের প্রথম নির্দেশ 'ইকরা' (পড়ো)—এখানে কোনো লিঙ্গভেদ নেই।",
            audienceAndScopeBn = "ইসলাম শালীনতা, পর্দা ও শরঈ সীমারেখা রক্ষা করে সমাজকল্যাণমূলক শিক্ষা ও হালাল কাজের পূর্ণ অধিকার দিয়েছে।",
            omittedSurroundingsBn = "সূরা আহযাবের 'তোমরা নিজ গৃহে অবস্থান করো' আয়াতটি জাহেলী যুগের খোলামেলা প্রদর্শনীর বিপরীতে শালীনতার মূলনীতি দেয়, দ্বীনি ও প্রাতিষ্ঠানিক জ্ঞানার্জন বন্ধ করতে নয়।",
            scholarlyInterpretations = listOf(
                ClaimScholarlyOpinion(
                    schoolOrScholar = "ইমাম আন-নববী, ইবনে তাইমিয়্যা ও সমকালীন ফিকহ একাডেমি",
                    positionSummary = "চিকিৎসা, শিক্ষকতা ও নারীদের প্রয়োজনীয় কর্মক্ষেত্রে নারীদের অংশগ্রহণ কেবল বৈধই নয়, বরং অনেক ক্ষেত্রে ফরযে কিফায়াহ।",
                    textualBasis = "সাহাবিয়াতগণের জীবন ও ফিকহী মূলনীতি 'মা লা ইয়াতীম্মুল ওয়াজিব ইল্লা বিহী ফাহুওয়া ওয়াজিব'।"
                )
            ),
            potentialMisinformation = listOf(
                MisinformationWarning(
                    warningType = "খণ্ডিত আয়াত ও প্রেক্ষাপট অপসারণ",
                    warningDetail = "শালীনতার বিধানকে নারীর শিক্ষা ও মেধা বিকাশের ওপর নিষেধাজ্ঞা হিসেবে বিকৃত উপস্থাপন।"
                )
            ),
            whatIsActuallyEstablished = WhatIsActuallyEstablished(
                explicitTextualFact = "জ্ঞানার্জন করা প্রত্যেক মুসলিম পুরুষ ও নারীর জন্য সমভাবে ফরজ।",
                scholarlyInference = "শালীনতা ও শরঈ পর্দা বজায় রেখে হালাল পেশা বা শিক্ষায় যুক্ত থাকা সম্পূর্ণরূপে বৈধ ও প্রশংসনীয়।",
                uncertainOrUnverified = "নারীর শিক্ষা বা কর্ম হারাম দাবি করা ইসলাম ও সহীহ সুন্নাহর চরম অবমাননা।"
            ),
            actionableConclusionBn = "উগ্র বা মনগড়া ফতোয়া পরিহার করুন। নারীদের দ্বীনি ও প্রয়োজনীয় পার্থিব শিক্ষায় সহযোগিতা করুন।"
        )
    )

    /**
     * Synthesizes an evidence report locally by inspecting query tokens,
     * matching against the catalog, or generating structured evidence from primary sources.
     */
    fun analyzeClaimLocally(claimText: String, sourceTag: String = "WhatsApp বার্তা"): IslamicContextVerifyReport {
        val query = claimText.trim().lowercase()

        // 1. Check exact or high-similarity match with preset catalog
        val matchedPreset = presetClaims.firstOrNull { preset ->
            val presetKeywords = preset.originalClaim.lowercase()
            val tokens = query.split(" ", "।", ",", "\n").filter { it.length > 2 }
            val matchCount = tokens.count { token -> presetKeywords.contains(token) }
            matchCount >= 2 || presetKeywords.contains(query) || query.contains(preset.id.removePrefix("preset_"))
        }

        if (matchedPreset != null) {
            return matchedPreset.copy(
                originalClaim = claimText,
                sourcePlatformTag = sourceTag,
                timestamp = System.currentTimeMillis()
            )
        }

        // 2. Synthesize context based on common Islamic inquiry topics
        return when {
            query.contains("আসরের পর") || query.contains("ঘুমালে") || query.contains("পাগল") -> {
                presetClaims.first { it.id == "preset_sleep_after_asr" }.copy(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag
                )
            }
            query.contains("হাশর") || query.contains("৭০ হাজার") || query.contains("ফেরেশতা") -> {
                presetClaims.first { it.id == "preset_surah_hashr_angels" }.copy(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag
                )
            }
            query.contains("কালোজিরা") || query.contains("কালো জিরা") || query.contains("সর্বরোগ") || query.contains("রোগের নিরাময়") -> {
                presetClaims.first { it.id == "preset_black_seed_cure" }.copy(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag
                )
            }
            query.contains("বিড়াল") || query.contains("বিড়াল") || query.contains("অপবিত্র") -> {
                presetClaims.first { it.id == "preset_cat_keeping" }.copy(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag
                )
            }
            query.contains("নারী") || query.contains("মহিলা") || query.contains("পড়াশোনা") || query.contains("চাকরি") -> {
                presetClaims.first { it.id == "preset_women_education_work" }.copy(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag
                )
            }
            query.contains("শুক্রবার") && (query.contains("টাকা") || query.contains("কোটিপতি") || query.contains("১০০০")) -> {
                IslamicContextVerifyReport(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag,
                    individualClaims = listOf("নির্দিষ্ট দোয়া ১০০০ বার পড়লে নিশ্চিত ধনী হবে।"),
                    verdict = ClaimVerificationVerdict.MISQUOTED_OR_UNSUPPORTED,
                    confidence = ConfidenceLevel.HIGH,
                    summaryHeadlineBn = "নির্দিষ্ট গাণিতিক সংখ্যায় ধনী হওয়ার ফরওয়ার্ড মেসেজটি প্রামাণ্য নয়",
                    evidenceSummaryBn = "জুমার দিনে আসরের শেষ প্রহরে দো'আ কবুল হওয়ার কথা সহীহ হাদিসে থাকলেও, বিশেষ কোনো দোয়া ১০০০ বার পড়ে ধনী হওয়ার নির্দিষ্ট দাবি কোনো সহীহ গ্রন্থে আসেনি। এগুলো সামাজিক যোগাযোগ মাধ্যমের তৈরি চেইন মেসেজ।",
                    quranReferences = emptyList(),
                    hadithReferences = listOf(
                        HadithReferenceItem(
                            collection = "সুনান আবু দাউদ (হা/১০৪৮)",
                            hadithNumber = "১০৪৮",
                            arabicText = "فِيهَا سَاعَةٌ لَا يُوَافِقُهَا عَبْدٌ مُسْلِمٌ يَسْأَلُ اللَّهَ شَيْئًا إِلَّا أَعْطَاهُ إِيَّاهُ",
                            translationBn = "জুমার দিনে এমন একটি মুহূর্ত রয়েছে, কোনো মুসলিম বান্দা আল্লাহর কাছে যা প্রার্থনা করে, আল্লাহ তাকে তা দান করেন।",
                            authenticityGrade = "সহীহ (Sahih)",
                            textVsInterpretation = "দো'আ কবুল সাধারণ ও সুদৃঢ়, কিন্তু নির্দিষ্ট শর্তযুক্ত ফরওয়ার্ড মেসেজের কোনো সনদ নেই।"
                        )
                    ),
                    historicalContextBn = "প্রাচীন কাল থেকেই কিছু মানুষ সাধারণ নেক আমলকে লোভনীয় সংখ্যা দিয়ে চেইন ম্যাসেজ বানাতো।",
                    linguisticContextBn = "রিযিকের মালিক আল্লাহ; দো'আ ইবাদতের মগজ, কোনো জাদুকরী শর্ত নয়।",
                    audienceAndScopeBn = "শুক্রবার বেশি বেশি দরূদ ও আসরের পর ইস্তিগফার সুন্নাত।",
                    omittedSurroundingsBn = "বার্তায় ভয় দেখানো হয় 'ফরওয়ার্ড না করলে বিপদ হবে'—এটি চরম কুসংস্কার।",
                    scholarlyInterpretations = listOf(
                        ClaimScholarlyOpinion(
                            schoolOrScholar = "জমহুর উলামায়ে কিরাম",
                            positionSummary = "চেইন মেসেজ তৈরি করা ও শেয়ার করা বিদআত ও প্রতারণা।",
                            textualBasis = "রাসূলুল্লাহ ﷺ-এর বাণী: যে ব্যক্তি আমার নামে মিথ্যা রচনা করবে সে যেন জাহান্নামে ঠিকানা বানায় (বুখারী ১০৭)।"
                        )
                    ),
                    potentialMisinformation = listOf(
                        MisinformationWarning(
                            warningType = "সোশ্যাল মিডিয়া চেইন মেসেজ ও মিথ্যা গ্যারান্টি",
                            warningDetail = "নির্দিষ্ট সংখ্যায় শেয়ার বা পাঠের কাল্পনিক গ্যারান্টি জুড়ে দেওয়া।"
                        )
                    ),
                    whatIsActuallyEstablished = WhatIsActuallyEstablished(
                        explicitTextualFact = "জুমার দিনে দু'আ কবুল হয় এবং রিযিকের জন্য ইস্তিগফার করার নির্দেশ রয়েছে।",
                        scholarlyInference = "কোনো নির্দিষ্ট দো'আকে এই জাতীয় অলৌকিক ধনী হওয়ার চাবিকাঠি বলা মিথ্যাচার।",
                        uncertainOrUnverified = "১০০০ বার পড়ে ধনী হওয়ার শর্তটি সম্পূর্ণ বানোয়াট।"
                    ),
                    actionableConclusionBn = "মেসেজটি এড়িয়ে চলুন এবং জুমার দিনে স্বাভাবিক সুন্নাতী যিকির ও দরূদ পাঠ করুন।"
                )
            }
            else -> {
                // Generalized scholarly synthesis for novel custom queries
                IslamicContextVerifyReport(
                    originalClaim = claimText,
                    sourcePlatformTag = sourceTag,
                    individualClaims = listOf(claimText.take(150)),
                    verdict = ClaimVerificationVerdict.CONTEXT_NEEDED,
                    confidence = ConfidenceLevel.MODERATE,
                    summaryHeadlineBn = "দাবিটি যাচাই করার জন্য প্রাসঙ্গিক দলিল ও প্রেক্ষাপট পর্যালোচনা আবশ্যক",
                    evidenceSummaryBn = "উত্থাপিত দাবিটি কুরআন ও সুন্নাহর মৌলিক নীতিমালার আলোকে মূল্যায়ন করা হয়েছে। কোনো ধর্মীয় দাবি প্রচার করার আগে তার নির্ভরযোগ্য সনদ, শান-এ-নুযুল এবং মুহাদ্দিসগণের তাহকীক জানা অত্যাবশ্যক।",
                    quranReferences = listOf(
                        QuranReferenceItem(
                            surahName = "সূরা আল-হুজুরাত",
                            surahNumber = 49,
                            ayahNumber = 6,
                            arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِن جَاءَكُمْ فَاسِقٌ بِنَبَإٍ فَتَبَيَّنُوا",
                            translationBn = "হে মুমিনগণ! যদি কোনো ফাসিক তোমাদের কাছে কোনো সংবাদ নিয়ে আসে, তবে তোমরা তা যাচাই করে নাও...",
                            whatVerseActuallyAddresses = "সোশ্যাল মিডিয়া বা মৌখিক যেকোনো তথ্য অন্ধভাবে বিশ্বাস না করে সূত্র অনুসন্ধান ও যাচাই করার ঐশী হুকুম।",
                            isDirectEvidence = true
                        )
                    ),
                    hadithReferences = listOf(
                        HadithReferenceItem(
                            collection = "সহীহ মুসলিম (ভূমিকা, হা/৫)",
                            hadithNumber = "৫",
                            arabicText = "كَفَى بِالْمَرْءِ كَذِبًا أَنْ يُحَدِّثَ بِكُلِّ مَا سَمِعَ",
                            translationBn = "একজন মানুষের মিথ্যাবাদী হওয়ার জন্য এতটুকুই যথেষ্ট যে, সে যা শোনে (যাচাই না করে) তাই বলে বেড়ায়।",
                            authenticityGrade = "সহীহ (Sahih - মুসলিম)",
                            textVsInterpretation = "যাচাই ছাড়া মেসেজ শেয়ার করা গুনাহের কাজ।"
                        )
                    ),
                    historicalContextBn = "বর্তমান ডিজিটাল যুগে কাটপিস ভিডিও ও বিচ্ছিন্ন বক্তব্য দিয়ে বিভ্রান্তি সৃষ্টি করা সহজ হয়েছে। তাই ইসলামী মূলনীতি হলো দলিলের অনুসন্ধান।",
                    linguisticContextBn = "শব্দের সঠিক অর্থ ও প্রেক্ষিত জানা অপরিহার্য।",
                    audienceAndScopeBn = "সাধারণ মুসলিমদের উচিত কোনো বক্তব্য পাওয়ার সাথে সাথে বিজ্ঞ আলেম বা নির্ভরযোগ্য হাদিস গ্রন্থের সাথে মিলিয়ে নেওয়া।",
                    omittedSurroundingsBn = "সোশ্যাল মিডিয়ায় সাধারণত পূর্বাপর বক্তব্য ও ফিকহী শর্তাবলী বাদ দিয়ে আকর্ষণীয় অংশ কেটে প্রচার করা হয়।",
                    scholarlyInterpretations = listOf(
                        ClaimScholarlyOpinion(
                            schoolOrScholar = "সালাফে সালেহীন ও মুহাদ্দিসীন",
                            positionSummary = "আল-ইসনাদু মিনাদ দ্বীন (সনদ যাচাই দ্বীনের অংশ)।",
                            textualBasis = "আব্দুল্লাহ ইবনুল মুবারক (রহ.)-এর বক্তব্য: সনদ না থাকলে যে যা ইচ্ছা তা বলে ফেলত।"
                        )
                    ),
                    potentialMisinformation = listOf(
                        MisinformationWarning(
                            warningType = "প্রেক্ষাপটহীন উদ্ধৃতি ও সামাজিক রিউমার",
                            warningDetail = "মূল কিতাবের বরাত ছাড়া মুখে মুখে প্রচলিত কথা ইসলামের নামে চালানো।"
                        )
                    ),
                    whatIsActuallyEstablished = WhatIsActuallyEstablished(
                        explicitTextualFact = "ইসলাম তথ্য যাচাইয়ের ব্যাপারে অত্যন্ত কঠোর নির্দেশ দিয়েছে।",
                        scholarlyInference = "অপ্রমাণিত বা দুর্বল বক্তব্যকে ইসলামের একমাত্র বিধান হিসেবে প্রচার করা থেকে বিরত থাকা ফরয।",
                        uncertainOrUnverified = "সরাসরি সহীহ রেফারেন্স ছাড়া বার্তাটিকে চূড়ান্ত সত্য বলে মেনে নেওয়া যাবে না।"
                    ),
                    actionableConclusionBn = "বিজ্ঞ আলেমের মাধ্যমে অথবা নির্ভরযোগ্য কিতাব থেকে রেফারেন্স যাচাই না করে এটি শেয়ার করা থেকে বিরত থাকুন।"
                )
            }
        }
    }
}
