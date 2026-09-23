package com.example.data.datasource

import com.example.data.model.AskBeforeYouActReport
import com.example.data.model.AskScenario
import com.example.data.model.DiagnosticOption
import com.example.data.model.DiagnosticQuestion
import com.example.data.model.FiqhMaxim
import com.example.data.model.HalalAlternative
import com.example.data.model.ScholarlyPosition
import com.example.data.model.ShariahRiskLevel
import com.example.data.model.VerifiedHadithProof
import com.example.data.model.VerifiedQuranProof

object AskActionKnowledgeBase {

    val scenarios: List<AskScenario> = listOf(
        // SCENARIO 1: LOAN & FINANCING (User's primary highlight)
        AskScenario(
            id = "loan_finance",
            titleBn = "ঋণ ও ব্যাংক ফাইন্যান্সিং",
            titleEn = "Loan & Financing",
            categoryBn = "আর্থিক লেনদেন ও দায়বদ্ধতা",
            sampleQuery = "I am planning to take a loan (আমি ঋণ বা ব্যাংক লোন নেওয়ার পরিকল্পনা করছি)",
            badgeIconName = "AccountBalance",
            shortSummaryBn = "সুদ, বিলম্ব জরিমানা, চুক্তির প্রকৃতি ও জরুরি অবস্থার শরঈ মানদণ্ড যাচাই।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_interest",
                    questionBn = "ঋণের ক্ষেত্রে কি অতিরিক্ত কোনো সুদ বা মুনাফা দিতে হবে?",
                    questionEn = "Is there interest (Riba) or mandatory excess on repayment?",
                    whyItMattersBn = "ইসলামী শরীয়তে মূলধনের অতিরিক্ত যেকোনো বাধ্যতামূলক পূর্বশর্তযুক্ত উদ্বৃত্তকে সর্বসম্মতভাবে 'রিবা' (সুদ) ঘোষণা করা হয়েছে, যা কবীরা গুনাহ।",
                    options = listOf(
                        DiagnosticOption(
                            id = "interest_yes_fixed",
                            labelBn = "হ্যাঁ, নির্দিষ্ট হারে অতিরিক্ত সুদ পরিশোধ করতে হবে",
                            riskWeight = 2,
                            impactExplanationBn = "চুক্তিতে প্রত্যক্ষ সুদের শর্ত রয়েছে — এটি শরীয়াহর দৃষ্টিতে সম্পূর্ণ হারাম ও কবীরা গুনাহ।"
                        ),
                        DiagnosticOption(
                            id = "interest_none_qard",
                            labelBn = "না, যতটুকু নিয়েছি ঠিক ততটুকুই ফেরত (সুদমুক্ত ধার / করজে হাসানা)",
                            riskWeight = 0,
                            impactExplanationBn = "এটি প্রশংসনীয় করজে হাসানা — কুরআন ও সুন্নাহ মোতাবেক অত্যন্ত সাওয়াবের কাজ।"
                        ),
                        DiagnosticOption(
                            id = "interest_profit_loss",
                            labelBn = "লাভ-লোকসান অংশীদারি (মুদারাবা / মুশারাকা লাভ বণ্টন)",
                            riskWeight = 0,
                            impactExplanationBn = "আসল টাকার নিশ্চয়তা ব্যতীত লাভ ও ঝুঁকির অংশীদারি হলে তা শরীয়াহসম্মত।"
                        ),
                        DiagnosticOption(
                            id = "interest_service_fee",
                            labelBn = "অতিরিক্ত নয়, কেবল প্রকৃত প্রশাসনিক সার্ভিস চার্জ",
                            riskWeight = 1,
                            impactExplanationBn = "সার্ভিস চার্জ যদি প্রকৃত নথিপত্র খরচের সমান হয় তবে বৈধ; শতকরা হারে লাভ হলে তা ছদ্মবেশী সুদ।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_contract_type",
                    questionBn = "চুক্তির কাঠামো বা ধরন কী রূপ?",
                    questionEn = "What type of contract is it?",
                    whyItMattersBn = "টাকা দিয়ে টাকা কেনা সুদ; কিন্তু বাস্তব পণ্য বা সম্পত্তি ক্রয় করে কিস্তিতে লাভে বিক্রি (মুরাবাহা/ইজারা) বৈধ বাণিজ্য।",
                    options = listOf(
                        DiagnosticOption(
                            id = "contract_cash_personal",
                            labelBn = "সরাসরি নগদ অর্থ ঋণ (Personal / Cash Loan)",
                            riskWeight = 2,
                            impactExplanationBn = "নগদ টাকার উপর সুদ নেওয়া প্রত্যক্ষ রিবা আল-কারদ (ঋণের সুদ)।"
                        ),
                        DiagnosticOption(
                            id = "contract_murabaha_asset",
                            labelBn = "পণ্য বা সম্পদের মুরাবাহা (ব্যাংক পণ্য কিনে লাভে কিস্তিতে বিক্রি করে)",
                            riskWeight = 0,
                            impactExplanationBn = "ব্যাংক যদি প্রকৃত মালিকানা ও ঝুঁকি গ্রহণ করে পণ্য বিক্রি করে, তবে এই লাভ হালাল।"
                        ),
                        DiagnosticOption(
                            id = "contract_mortgage_home",
                            labelBn = "হোম লোন বা বন্ধকী ঋণ (Mortgage)",
                            riskWeight = 1,
                            impactExplanationBn = "প্রথাগত বন্ধকী ঋণ সুদী; কিন্তু ডিমিনিশিং মুশারাকা (মুশারাকা মুতানাকিসাহ) ভিত্তিক বাড়ি অর্থায়ন বৈধ।"
                        ),
                        DiagnosticOption(
                            id = "contract_credit_card",
                            labelBn = "ক্রেডিট কার্ড (গ্রেস পিরিয়ডের মধ্যে পরিশোধের পরিকল্পনা)",
                            riskWeight = 1,
                            impactExplanationBn = "দেরি হলে সুদ যুক্ত হওয়ার চুক্তি থাকায় ঝুঁকি বিদ্যমান; নির্ধারিত সময়ে পরিশোধে সুদ না দিলেও সুদী চুক্তিতে স্বাক্ষর থাকে।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_purpose",
                    questionBn = "ঋণ গ্রহণের মূল উদ্দেশ্য কী?",
                    questionEn = "What is the purpose of the financing?",
                    whyItMattersBn = "ইসলামে সাধারণ প্রয়োজন ও চরম নিরুপায় অবস্থা (জরুরাত) ভিন্ন আইনি মর্যাদা বহন করে।",
                    options = listOf(
                        DiagnosticOption(
                            id = "purpose_emergency_survival",
                            labelBn = "চরম নিরুপায় জীবনধারণ / জীবনরক্ষাকারী চিকিৎসা (জরুরাত)",
                            riskWeight = 1,
                            impactExplanationBn = "জীবননাশের আশঙ্কাযুক্ত চরম সংকটে কোনো বিকল্প না থাকলে ফিকহি মূলনীতি 'জরুরাত' প্রযোজ্য হতে পারে।"
                        ),
                        DiagnosticOption(
                            id = "purpose_business_expansion",
                            labelBn = "ব্যবসার সম্প্রসারণ বা উৎপাদনশীল বিনিয়োগ",
                            riskWeight = 0,
                            impactExplanationBn = "ব্যবসার জন্য সুদমুক্ত পার্টনারশিপ বা ইসলামিক ট্রেড ফাইন্যান্সিং ব্যবহার কাম্য।"
                        ),
                        DiagnosticOption(
                            id = "purpose_luxury_consumer",
                            labelBn = "আরাম-আয়েশ, বিলাসিতা বা জীবনযাত্রার মান বৃদ্ধি",
                            riskWeight = 2,
                            impactExplanationBn = "বিলাসবহুল চাহিদার জন্য ঋণ গ্রহণ ইসলামী আত্মসংযমের পরিপন্থী এবং কোনোভাবেই নিষিদ্ধ চুক্তিকে বৈধ করে না।"
                        ),
                        DiagnosticOption(
                            id = "purpose_debt_refinancing",
                            labelBn = "পূর্ববর্তী ঋণ পরিশোধের জন্য নতুন ঋণ গ্রহণ",
                            riskWeight = 2,
                            impactExplanationBn = "এটি ঋণের ফাঁদ (Debt Trap) তৈরি করে যা মানুষকে আর্থিক দাসত্ব ও সুদে জড়িয়ে ফেলে।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_penalties",
                    questionBn = "পরিশোধে বিলম্ব হলে কোনো জরিমানা (Penalty Clause) আছে কি?",
                    questionEn = "Are there late payment penalties or compounding charges?",
                    whyItMattersBn = "দেরির কারণে অতিরিক্ত অর্থ দাবি করা জাহেলিয়াত যুগের সুদের (রিবাল জাহিলিয়্যাহ) মূল রূপ ছিল।",
                    options = listOf(
                        DiagnosticOption(
                            id = "penalty_compounding_bank_income",
                            labelBn = "হ্যাঁ, বিলম্ব হলে জরিমানা চক্রবৃদ্ধি হারে ব্যাংকের আয়ে যোগ হবে",
                            riskWeight = 2,
                            impactExplanationBn = "দেরির বিনিময়ে অতিরিক্ত অর্থ সরাসরি সুদের শামিল এবং সর্বসম্মতভাবে নিষিদ্ধ।"
                        ),
                        DiagnosticOption(
                            id = "penalty_charity_diverted",
                            labelBn = "হ্যাঁ, তবে জরিমানার অর্থ ব্যাংকের আয় হয় না, বাধ্যতামূলক সদকা ফান্ডে যায়",
                            riskWeight = 1,
                            impactExplanationBn = "আয়াওফি (AAOIFI) ও অধিকাংশ ইসলামিক ব্যাংকে শৃঙ্খলা রক্ষার জন্য এই শর্ত রাখা হয়, তবে এর অতিরিক্ত ব্যবহার সতর্কতামূলক।"
                        ),
                        DiagnosticOption(
                            id = "penalty_none",
                            labelBn = "না, কোনো বিলম্ব জরিমানা বা অতিরিক্ত শর্ত নেই",
                            riskWeight = 0,
                            impactExplanationBn = "এটি নিখাদ করজে হাসানার আদর্শ রূপ।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_institution",
                    questionBn = "প্রতিষ্ঠানটি কি প্রথাগত বাণিজ্যিক ব্যাংক নাকি ইসলামিক ব্যাংক?",
                    questionEn = "Is it a conventional or Islamic financing product?",
                    whyItMattersBn = "প্রথাগত ব্যাংক সরাসরি টাকার কারবার ও সুদ দিয়ে চলে; ইসলামিক ব্যাংক শরীয়াহ সুপারভাইজরি বোর্ডের অধীনে সম্পদভিত্তিক লেনদেন করে।",
                    options = listOf(
                        DiagnosticOption(
                            id = "inst_conventional_bank",
                            labelBn = "প্রথাগত বাণিজ্যিক ব্যাংক / সুদভিত্তিক এনজিও",
                            riskWeight = 2,
                            impactExplanationBn = "প্রথাগত ব্যাংকের মূল ভিত্তিই হলো আমানতের সুদ দেওয়া এবং ঋণে সুদ নেওয়া।"
                        ),
                        DiagnosticOption(
                            id = "inst_islamic_certified",
                            labelBn = "স্বীকৃত ও স্বাধীন শরীয়াহ বোর্ড কর্তৃক নিরীক্ষিত ইসলামিক ব্যাংক",
                            riskWeight = 0,
                            impactExplanationBn = "চুক্তিটি শরীয়াহসম্মত সম্পদভিত্তিক বিক্রয় বা ভাড়ার আইনি কাঠামোর আওতাধীন।"
                        ),
                        DiagnosticOption(
                            id = "inst_private_individual",
                            labelBn = "ব্যক্তিগত আত্মীয়-স্বজন বা বিশ্বস্ত পরিচিত ব্যক্তি",
                            riskWeight = 0,
                            impactExplanationBn = "ব্যক্তিগত পর্যায়ে সুদমুক্ত ধার প্রদান ইসলামে শ্রেষ্ঠ সদকার সমতুল্য।"
                        )
                    )
                )
            ),
            quranProofs = listOf(
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 275,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "الَّذِينَ يَأْكُلُونَ الرِّبَا لَا يَقُومُونَ إِلَّا كَمَا يَقُومُ الَّذِي يَتَخَبَّطُهُ الشَّيْطَانُ مِنَ الْمَسِّ ۚ ذَٰلِكَ بِأَنَّهُمْ قَالُوا إِنَّمَا الْبَيْعُ مِثْلُ الرِّبَا ۗ وَأَحَلَّ اللَّهُ الْبَيْعَ وَحَرَّمَ الرِّبَا",
                    banglaTranslation = "যারা সুদ খায়, তারা কেয়ামতের দিন দাঁড়াবে সেই ব্যক্তির মতো যাকে শয়তান স্পর্শ দ্বারা পাগল করে দিয়েছে। এর কারণ হলো তারা বলে: 'ব্যবসা তো সুদের মতোই।' অথচ আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে হারাম করেছেন।",
                    englishTranslation = "Those who consume interest cannot stand [on the Day of Resurrection] except as one stands whom the devil has beaten into insanity... But Allah has permitted trade and has forbidden interest.",
                    tafsirReferenceBn = "তাফসীর ইবনে কাসীর: আয়াতে স্পষ্ট মূলনীতি দেওয়া হয়েছে যে, বৈধ ক্রয়-বিক্রয়ে পণ্যের বিনিময় ঘটে এবং ঝুঁকি থাকে; কিন্তু ঋণের উপর নির্দিষ্ট অতিরিক্ত নেওয়া সম্পূর্ণ হারাম সুদ।",
                    legalSignificanceBn = "ব্যবসা ও সুদের মধ্যে সুস্পষ্ট সীমারেখা অঙ্কন — মূলধনের উপর শর্তযুক্ত নিশ্চিত মুনাফা বা জরিমানা হারাম।"
                ),
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 278,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا اتَّقُوا اللَّهَ وَذَرُوا مَا بَقِيَ مِنَ الرِّبَا إِن كُنتُم مُّؤْمِنِينَ ۝ فَإِن لَّمْ تَفْعَلُوا فَأْذَنُوا بِحَرْبٍ مِّنَ اللَّهِ وَرَسُولِهِ",
                    banglaTranslation = "হে ঈমানদারগণ! তোমরা আল্লাহকে ভয় করো এবং সুদের যা কিছু অবশিষ্ট রয়েছে তা বর্জন করো, যদি তোমরা প্রকৃত মুমিন হও। আর যদি তা না করো, তবে আল্লাহ ও তাঁর রাসূলের পক্ষ থেকে যুদ্ধের ঘোষণা জেনে নাও।",
                    englishTranslation = "O you who have believed, fear Allah and give up what remains [due to you] of interest, if you should be believers. And if you do not, then be informed of a war [against you] from Allah and His Messenger.",
                    tafsirReferenceBn = "মা'আরিফুল কুরআন (মুফতী শফী রহ.): সুদের অপরাধ এতটাই গুরুতর যে কুরআনুল কারীমে এর বিরুদ্ধেই কেবলমাত্র সরাসরি যুদ্ধ ঘোষণার মতো কঠোরতম ভাষা ব্যবহার করা হয়েছে।",
                    legalSignificanceBn = "সুদী চুক্তিতে ইচ্ছাকৃতভাবে জড়িত হওয়া সরাসরি কবিরা গুনাহ ও আল্লাহর অসন্তুষ্টির কারণ।"
                ),
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 280,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "وَإِن كَانَ ذُو عُسْرَةٍ فَنَظِرَةٌ إِلَىٰ مَيْسَرَةٍ ۚ وَأَن تَصَدَّقُوا خَيْرٌ لَّكُمْ ۖ إِن كُنتُمْ تَعْلَمُونَ",
                    banglaTranslation = "আর ঋণগ্রহীতা যদি অভাবগ্রস্ত হয়, তবে তাকে সচ্ছলতা আসা পর্যন্ত অবকাশ দাও। আর যদি সদকা করে ক্ষমা করে দাও, তবে তা তোমাদের জন্য আরও উত্তম, যদি তোমরা জানতে!",
                    englishTranslation = "And if someone is in hardship, then [let there be] postponement until [a time of] ease. But if you give [from your right as] charity, then it is better for you, if you only knew.",
                    tafsirReferenceBn = "তাফসীরে তাবারী: অভাবী ঋণগ্রহীতাকে সময় দেওয়া ওয়াজিব। জাহেলি যুগে সময় বাড়িয়ে সুদ দ্বিগুণ করা হতো, ইসলাম তা কঠোরভাবে রহিত করে সহানুভূতিকে ইবাদতে পরিণত করেছে।",
                    legalSignificanceBn = "ঋণ আদায়ে কোনো চাপিয়ে দেওয়া জরিমানা বা অতিরিক্ত আদায় না করে অবকাশ দেওয়ার ইসলামী বিধান।"
                ),
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 282,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا تَدَايَنتُم بِدَيْنٍ إِلَىٰ أَجَلٍ مُّسَمًّى فَاكْتُبُوهُ ۚ وَلْيَكْتُب بَّيْنَكُمْ كَاتِبٌ بِالْعَدْلِ",
                    banglaTranslation = "হে ঈমানদারগণ! যখন তোমরা কোনো নির্দিষ্ট মেয়াদের জন্য ঋণের আদান-প্রদান করো, তখন তা লিখে রাখো। আর কোনো লেখক যেন তোমাদের মাঝে ন্যায়ের সাথে তা লিখে দেয়।",
                    englishTranslation = "O you who have believed, when you contract a debt for a specified term, write it down. And let a scribe write [it] between you in justice.",
                    tafsirReferenceBn = "কুরআনের দীর্ঘতম আয়াত (আয়াতুল মুদায়ানাহ): চুক্তি লিখিত রাখা এবং শর্তে ইনসাফ বজায় রাখা বিরোধ দূর করার জন্য বাধ্যতামূলক ব্যবস্থাপনা।",
                    legalSignificanceBn = "আর্থিক চুক্তিতে স্বচ্ছতা, ন্যায়বিচার এবং অনির্দিষ্টতা দূর করার নির্দেশ।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ মুসলিম",
                    hadithNumber = "১৫৯৮",
                    authenticityGradeBn = "সহীহ (সর্বসম্মত গ্রহণযোগ্য)",
                    narratorBn = "জাবির ইবনে আব্দুল্লাহ (রা.)",
                    arabicText = "لَعَنَ رَسُولُ اللَّهِ ﷺ آكِلَ الرِّبَا، وَمُؤْكِلَهُ، وَكَاتِبَهُ، وَشَاهِدَيْهِ، وَقَالَ: هُمْ سَوَاءٌ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ সুদ ভক্ষণকারী (গ্রহীতা), সুদ প্রদানকারী, সুদের লেখক এবং তার দুই সাক্ষীর উপর অভিসম্পাত করেছেন। এবং তিনি বলেছেন: 'পাপের ক্ষেত্রে তারা সবাই সমান।'",
                    legalSignificanceBn = "শুধু সুদ নেওয়া নয়, জেনে-শুনে সুদের চুক্তি তৈরি করা, স্বাক্ষর করা বা সাক্ষী থাকাও সমান অপরাধ।"
                ),
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ বুখারী",
                    hadithNumber = "২০৭৯",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "আবু হুরায়রা (রা.)",
                    arabicText = "إِنَّ خِيَارَكُمْ أَحْسَنُكُمْ قَضَاءً",
                    banglaTranslation = "নিশ্চয়ই তোমাদের মধ্যে সর্বোত্তম মানুষ সে, যে ঋণ পরিশোধের ক্ষেত্রে সবচেয়ে সুন্দর আচরণ করে ও ভালোভাবে পরিশোধ করে।",
                    legalSignificanceBn = "ঋণগ্রহীতার কর্তব্য সচ্ছল হলে কোনো টালবাহানা না করে দ্রুততম সময়ে কৃতজ্ঞতার সাথে ঋণ পরিশোধ করা।"
                ),
                VerifiedHadithProof(
                    sourceBookBn = "সুনান আন-নাসায়ী",
                    hadithNumber = "৪৬৮৪",
                    authenticityGradeBn = "সহীহ (আলবানী রহ.)",
                    narratorBn = "আমর ইবনে শারীদ (রা.)",
                    arabicText = "لَيُّ الْوَاجِدِ يُحِلُّ عِرْضَهُ وَعُقُوبَتَهُ",
                    banglaTranslation = "সামর্থ্যবান ঋণগ্রহীতার পক্ষ থেকে ঋণ পরিশোধে টালবাহানা করা তার সম্মানহানি ও আইনি শাস্তিকে বৈধ করে দেয়।",
                    legalSignificanceBn = "ঋণ পরিশোধের সামর্থ্য থাকার পরও বিলম্ব করা জুলুম।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "كُلُّ قَرْضٍ جَرَّ مَنْفَعَةً فَهُوَ رِبًا",
                    banglaTranslation = "প্রত্যেক ঋণ যা কোনো শর্তযুক্ত অতিরিক্ত লাভ বা সুবিধা টেনে আনে, তা-ই সুদ।",
                    sourceOrOriginBn = "কাওয়াইদে ফিকহিয়্যাহ ও উলামায়ে কেরামের ঐকমত্য (ইবনু কুদামা আল-মুগনী)",
                    practicalApplicationBn = "ঋণদাতার যদি কোনো পূর্বশর্ত বা প্রত্যক্ষ সুবিধা অর্জনের চুক্তি থাকে, তবে মূলধনের সামান্যতম অতিরিক্তও হারাম রিবা।"
                ),
                FiqhMaxim(
                    arabicText = "الضَّرُورَاتُ تُبِيحُ الْمَحْظُورَاتِ تُقَدَّرُ بِقَدْرِهَا",
                    banglaTranslation = "চরম নিরুপায় অবস্থা নিষিদ্ধ বিষয়কে বৈধ করে — তবে তা কেবল নিরুপায়তার নির্দিষ্ট মাত্রা পর্যন্তই সীমিত।",
                    sourceOrOriginBn = "আল-আশবাহ ওয়ান-নাযাইর (ইবনে নুজাইম), সুরা বাক্বারাহ ১৭৩ আয়াতের ব্যাখ্যা",
                    practicalApplicationBn = "জীবননাশের মতো চরম সংকটেই কেবল আপদকালীন ছাড় পাওয়া যায়; ব্যবসা সম্প্রসারণ বা গাড়ি-বাড়ির জন্য এই মূলনীতি প্রয়োগের কোনো সুযোগ নেই।"
                ),
                FiqhMaxim(
                    arabicText = "الأَصْلُ فِي الْمُعَامَلَاتِ الإِبَاحَةُ",
                    banglaTranslation = "আর্থিক লেনদেন ও পারস্পরিক চুক্তিতে মূল বিধান হলো বৈধতা — যতক্ষণ না শরীয়াহর কোনো সুস্পষ্ট নিষেধাজ্ঞা পাওয়া যায়।",
                    sourceOrOriginBn = "ফিকহি কাওয়াইদ (ইমাম আহমাদ ও ইবনে তাইমিয়্যাহ রহ.)",
                    practicalApplicationBn = "যেসব ব্যবসায়িক চুক্তিতে সুদ, প্রতারণা (গারার) ও জুয়া নেই, তা স্বাভাবিকভাবেই হালাল ও বৈধ।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "ওআইসি আন্তর্জাতিক ইসলামিক ফিকহ একাডেমি (OIC Fiqh Academy)",
                    verdictSummaryBn = "দেরির কারণে প্রথাগত ব্যাংকের মতো আর্থিক জরিমানা ঋণদাতার আয়ে অন্তর্ভুক্ত করা সরাসরি হারাম সুদ।",
                    argumentAndEvidenceBn = "এটি জাহিলিয়্যাতের 'দেরি করো এবং বেশি দাও' ব্যবস্থার হুবহু পুনরাবৃত্তি।",
                    conditionsBn = "তবে গ্রাহকের অনিচ্ছাকৃত ক্ষতি এড়াতে এবং ইচ্ছাকৃত খেলাপ রোধে ইসলামিক ব্যাংকে শর্ত দেওয়া হয় যে, জরিমানার সমুদয় অর্থ ব্যাংক নিজের আয়ে নিতে পারবে না, বাধ্যতামূলকভাবে দাতব্য তহবিলে সদকা করতে হবে (AAOIFI Standard 3)।"
                ),
                ScholarlyPosition(
                    bodyOrSchoolBn = "হানাফী ও সমকালীন ফিকহ বোর্ডসমূহের পর্যালোচনা (দেওবন্দ ও আন্তর্জাতিক দারুল উলুম)",
                    verdictSummaryBn = "প্রথাগত ব্যাংকের পার্সোনাল লোন সুদের ভিত্তিতে হওয়ায় সম্পূর্ণ না-জায়েজ। কেবল জীবন বাঁচানোর মতো জরুরি ওষুধ বা খাদ্যের নিদারুণ সংকটে যখন কোনো সুদমুক্ত ধার বা সদকা পাওয়া যায় না, তখনই অনন্যোপায় অবস্থায় গুনাহ থেকে মার্জনা মিলতে পারে।",
                    argumentAndEvidenceBn = "সুদের চুক্তি লেখক ও প্রদানকারীর উপর রাসূলুল্লাহ ﷺ এর সরাসরি লানত বিদ্যমান থাকায় স্বেচ্ছায় এতে জড়ানো কোনো অবস্থাতেই জায়েজ নেই।",
                    conditionsBn = "বাধ্যবাধকতা শেষ হওয়ার সাথে সাথে অতি দ্রুত তওবা ও দায়মুক্ত হওয়া আবশ্যক।"
                ),
                ScholarlyPosition(
                    bodyOrSchoolBn = "ইসলামিক ব্যাংক ফাইন্যান্সিং (মুরাবাহা ও মুশারাকা)",
                    verdictSummaryBn = "ইসলামিক ব্যাংকের মাধ্যমে গাড়ি বা বাড়ির ফাইন্যান্সিং বৈধ, যদি ব্যাংক প্রকৃতপক্ষেই বিক্রেতার কাছ থেকে পণ্যটি কিনে নিজের মালিকানায় নিয়ে গ্রাহকের কাছে কিস্তিতে বিক্রি করে।",
                    argumentAndEvidenceBn = "কুরআনের নির্দেশ: 'আল্লাহ ব্যবসাকে হালাল করেছেন।' এখানে টাকা ধার দেওয়া হচ্ছে না, বরং পণ্য বেচাকেনা করা হচ্ছে।",
                    conditionsBn = "পণ্য সরবরাহের আগেই গ্রাহকের কাছ থেকে লাভ গ্রহণ করা যাবে না এবং কোনো ছদ্মবেশী কাগুজে ঋণ (তাবাররুক আল-মুনাযযাম) হওয়া যাবে না।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "করজে হাসানা (সুদমুক্ত ধার)",
                    islamicContractBn = "القرض الحسن (আল-কারদুল হাসান)",
                    howItWorksBn = "পরিবার, আত্মীয়, ইসলামিক সংস্থা বা বিশ্বস্ত বন্ধু থেকে সুদমুক্তভাবে ধার নেওয়া এবং নির্ধারিত সময়ে ঠিক সমপরিমাণ টাকা ফেরত দেওয়া।",
                    whyItIsHalalBn = "কুরআন ও হাদিসে এই ধারকে বিপুল সাওয়াবের আমল বলা হয়েছে। এতে কোনো মুনাফা বা অতিরিক্ত নেই।"
                ),
                HalalAlternative(
                    titleBn = "মুরাবাহা (পণ্যভিত্তিক বিক্রয় অর্থায়ন)",
                    islamicContractBn = "المرابحة للآمر بالشراء",
                    howItWorksBn = "আপনার প্রয়োজনীয় সম্পদ (যেমন গাড়ি, কাঁচামাল বা যন্ত্রপাতি) ব্যাংক নিজে বিক্রেতার কাছ থেকে ক্রয় করবে, এরপর আপনার কাছে নির্দিষ্ট লাভে কিস্তিতে বিক্রি করবে।",
                    whyItIsHalalBn = "এটি টাকার ঋণ নয়; এটি পণ্যের বাস্তব ক্রয়-বিক্রয়, যেখানে ব্যাংক পণ্যের প্রাথমিক ঝুঁকি বহন করে।"
                ),
                HalalAlternative(
                    titleBn = "মুশারাকা মুতানাকিসাহ (ক্রমহ্রাসমান অংশীদারি বাড়ি অর্থায়ন)",
                    islamicContractBn = "المشاركة المتناقصة",
                    howItWorksBn = "আপনি ও ব্যাংক যৌথভাবে বাড়ি কিনবেন (যেমন আপনার ২০%, ব্যাংকের ৮০%)। আপনি ব্যাংকের অংশের ভাড়া দেবেন এবং ধীরে ধীরে ব্যাংকের শেয়ার কিনে নিয়ে ১০০% মালিক হবেন।",
                    whyItIsHalalBn = "এটি সুদের বন্ধকী ঋণ নয়; এটি যৌথ মালিকানা ও বৈধ বাড়ি ভাড়ার সমন্বিত শরীয়াহ চুক্তি।"
                )
            )
        ),

        // SCENARIO 2: STOCK MARKET & EQUITY TRADING
        AskScenario(
            id = "stocks_trading",
            titleBn = "শেয়ার বাজার ও ইকুইটি বিনিয়োগ",
            titleEn = "Stock Market & Trading",
            categoryBn = "পুঁজিবাজার ও ব্যবসায়িক অংশীদারি",
            sampleQuery = "আমি শেয়ার বাজারে ট্রেডিং বা বিনিয়োগ করতে চাই",
            badgeIconName = "TrendingUp",
            shortSummaryBn = "কোম্পানির ব্যবসায়িক স্ক্রিনিং, ঋণ অনুপাত, শর্ট সেলিং ও ডেরিভেটিভসের শরঈ বিশ্লেষণ।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_core_business",
                    questionBn = "কোম্পানির মূল ব্যবসা বা আয়ের উৎস কী?",
                    questionEn = "What is the core business activity of the company?",
                    whyItMattersBn = "ইসলামে কোনো নিষিদ্ধ পণ্য (যেমন মদ, জুয়া, পর্ক, সুদভিত্তিক ব্যাংকিং, অশ্লীল মিডিয়া) উৎপাদনকারী প্রতিষ্ঠানের শেয়ারের অংশীদার হওয়া সরাসরি হারাম।",
                    options = listOf(
                        DiagnosticOption(
                            id = "business_halal",
                            labelBn = "প্রযুক্তি, উৎপাদন, হালাল খাদ্য, স্বাস্থ্যসেবা ও কৃষি ইত্যাদি বৈধ ব্যবসা",
                            riskWeight = 0,
                            impactExplanationBn = "মূল ব্যবসা সম্পূর্ণ হালাল ও জনকল্যাণমূলক।"
                        ),
                        DiagnosticOption(
                            id = "business_prohibited",
                            labelBn = "প্রথাগত সুদী ব্যাংক, ইনস্যুরেন্স, মদ, ক্যাসিনো বা জুয়া কোম্পানি",
                            riskWeight = 2,
                            impactExplanationBn = "মূল ব্যবসাই হারাম হওয়ায় এর শেয়ারে বিনিয়োগ করা সর্বসম্মতভাবে নিষিদ্ধ।"
                        ),
                        DiagnosticOption(
                            id = "business_mixed",
                            labelBn = "মূল ব্যবসা হালাল, তবে কিছু সুদী ডিপোজিট বা ঋণ রয়েছে",
                            riskWeight = 1,
                            impactExplanationBn = "আয়াওফি (AAOIFI) মানদণ্ডে নির্দিষ্ট অনুপাত (Debt < 33%, Interest < 5%) পূরণ ও মুনাফা পরিশুদ্ধকরণ আবশ্যক।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_trading_mechanism",
                    questionBn = "ট্রেডিংয়ের জন্য কোন পদ্ধতি ব্যবহার করছেন?",
                    questionEn = "What trading mechanism and instrument are you using?",
                    whyItMattersBn = "শেয়ার হস্তগত হওয়া ছাড়া বিক্রি করা বা ধার করা মার্জিন দিয়ে জুয়াসদৃশ ডেরিভেটিভস কেনাবেচা করা শরীয়াহতে নিষিদ্ধ।",
                    options = listOf(
                        DiagnosticOption(
                            id = "mech_cash_delivery",
                            labelBn = "নগদ অর্থে সাধারণ শেয়ার ডেলিভারি (Cash Settlement / Spot Share Holding)",
                            riskWeight = 0,
                            impactExplanationBn = "বাস্তব শেয়ারের মালিকানা হস্তগত হয় — শরীয়াহ অনুমোদিত।"
                        ),
                        DiagnosticOption(
                            id = "mech_short_selling",
                            labelBn = "শর্ট সেলিং (মালিকানাবিহীন শেয়ার ধার করে বিক্রি করা)",
                            riskWeight = 2,
                            impactExplanationBn = "হাদিসে স্পষ্ট নিষেধ: 'তোমার মালিকানায় যা নেই তা বিক্রি করো না' — শর্ট সেলিং হারাম।"
                        ),
                        DiagnosticOption(
                            id = "mech_derivatives",
                            labelBn = "ফিউচার্স, অপশনস ডেরিভেটিভস বা মার্জিন লোন (Futures & Options)",
                            riskWeight = 2,
                            impactExplanationBn = "আন্তর্জাতিক ফিকহ একাডেমি (OIC) এগুলোকে গারার ও জুয়ার অন্তর্ভুক্ত করায় নিষিদ্ধ করেছে।"
                        ),
                        DiagnosticOption(
                            id = "mech_day_trading",
                            labelBn = "ইন্ট্রা-ডে ডে-ট্রেডিং (T+0 ডেলিভারি হওয়ার আগেই বারবার কেনাবেচা)",
                            riskWeight = 1,
                            impactExplanationBn = "মালিকানা নিশ্চিত হওয়ার পূর্বে বিক্রির প্রশ্নে উলামাদের মধ্যে তীব্র বিতর্ক ও নিষেধ রয়েছে।"
                        )
                    )
                )
            ),
            quranProofs = listOf(
                VerifiedQuranProof(
                    surahNumber = 4,
                    ayahNumber = 29,
                    surahNameBn = "আন-নিসা",
                    surahNameAr = "سُورَةُ النِّسَاءِ",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا لَا تَأْكُلُوا أَمْوَالَكُم بَيْنَكُم بِالْبَاطِلِ إِلَّا أَن تَكُونَ تِجَارَةً عَن تَرَاضٍ مِّنكُم",
                    banglaTranslation = "হে ঈমানদারগণ! তোমরা অন্যায়ভাবে একে অপরের সম্পদ গ্রাস করো না, তবে তোমাদের পারস্পরিক সম্মতিক্রমে ব্যবসার মাধ্যমে লেনদেন বৈধ।",
                    englishTranslation = "O you who have believed, do not consume one another's wealth unjustly but only [in lawful] business by mutual consent.",
                    tafsirReferenceBn = "তাফসীরে মাআরিফুল কুরআন: জুয়া, ধোঁকা ও অনিশ্চয়তার লেনদেন সবই 'বাতিল' উপার্জনের অন্তর্ভুক্ত; কেবল বাস্তব পারস্পরিক বাণিজ্যই বৈধ।",
                    legalSignificanceBn = "পুঁজিবাজারে কৃত্রিম কারসাজি বা জুয়াসদৃশ বাজি ধরা নিষিদ্ধ।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সুনান আবু দাউদ",
                    hadithNumber = "৩৫০৩",
                    authenticityGradeBn = "সহীহ (তিরমিযী ১২৩২)",
                    narratorBn = "হাকীম ইবনে হিযাম (রা.)",
                    arabicText = "لَا تَبِعْ مَا لَيْسَ عِندَكَ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ বলেছেন: তোমার মালিকানা ও অধিকারে যা নেই, তা বিক্রি করো না।",
                    legalSignificanceBn = "শর্ট সেলিং এবং পণ্য/শেয়ার হস্তগত হওয়ার পূর্বেই বিক্রি করে মুনাফা নেওয়ার মৌলিক নিষেধাজ্ঞা।"
                ),
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ মুসলিম",
                    hadithNumber = "১৫১৩",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "আবু হুরায়রা (রা.)",
                    arabicText = "نَهَى رَسُولُ اللَّهِ ﷺ عَنْ بَيْعِ الْغَرَرِ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ গারার (অনিশ্চয়তা, ধোঁকাযুক্ত ও স্পষ্ট তথ্যহীন লেনদেন) নিষিদ্ধ করেছেন।",
                    legalSignificanceBn = "ডেরিভেটিভস ও ফিউচার্স চুক্তিতে অন্তর্নিহিত গারার থাকার কারণে এগুলো পরিত্যাজ্য।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الْخَرَاجُ بِالضَّمَانِ",
                    banglaTranslation = "মুনাফা অর্জনের অধিকার ঝুঁকির সাথে সংশ্লিষ্ট।",
                    sourceOrOriginBn = "সুনান আবু দাউদ (৩৫০৮), তিরমিযী (১২৮৫)",
                    practicalApplicationBn = "ব্যবসায়িক লোকসানের ঝুঁকি বহন না করে কেবল নিশ্চিত মুনাফা নেওয়া শরীয়তে জায়েজ নেই।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "আয়াওফি (AAOIFI Shariah Standard No. 21) ও মোটাদাগে আন্তর্জাতিক ফতোয়া বোর্ড",
                    verdictSummaryBn = "যদি কোনো কোম্পানির মূল ব্যবসা হালাল হয়, তবে তার শেয়ার ক্রয় বৈধ যদি: (১) মোট ঋণের পরিমাণ বাজার মূলধনের ৩৩% এর নিচে থাকে, এবং (২) মোট আয়ের ৫% এর কম অনাকাঙ্ক্ষিত সুদী আয় থাকে।",
                    argumentAndEvidenceBn = "বাস্তব প্রয়োজনের তাগিদে এবং সুদের সাধারণ ব্যাপকতার কারণে এই ছাড় দেওয়া হয়েছে, তবে ডিভিডেন্ডের সুদী অংশ ছাওয়াবের নিয়ত ছাড়া সদকা করে দিতে হবে।",
                    conditionsBn = "সুদী অংশ অবশ্যই হিসাব করে 'তাতহীর' (পরিশুদ্ধকরণ) করতে হবে।"
                ),
                ScholarlyPosition(
                    bodyOrSchoolBn = "তাকওয়াভিত্তিক আলেমগণ ও উপমহাদেশীয় দারুল উলুমসমূহ",
                    verdictSummaryBn = "যেসব কোম্পানি সুদী ঋণে জড়িত বা ব্যাংকে সুদে টাকা খাটায়, তাদের শেয়ারে জেনে-শুনে অর্থ বিনিয়োগ করা থেকে সম্পূর্ণ বেঁচে থাকা অধিকতর নিরাপদ।",
                    argumentAndEvidenceBn = "রাসূলুল্লাহ ﷺ বলেছেন: 'যে ব্যক্তি সন্দেহযুক্ত বিষয় থেকে বেঁচে রইল, সে তার দ্বীন ও সম্মান রক্ষা করল।' (সহীহ বুখারী ৫২)",
                    conditionsBn = "সম্পূর্ণ সুদমুক্ত শতভাগ হালাল ইসলামিক ইকুইটি বা সরাসরি অংশীদারি ব্যবসাকে অগ্রাধিকার দেওয়ার পরামর্শ।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "শরীয়াহ সম্মত ইনডেক্স ফান্ড / ইটিএফ (Shariah ETF)",
                    islamicContractBn = "صناديق الاستثمار الإسلامية",
                    howItWorksBn = "বিশ্বস্ত শরীয়াহ বোর্ড কর্তৃক স্ক্রিনকৃত কোম্পানিগুলোর ফান্ডে দীর্ঘমেয়াদী বিনিয়োগ করা।",
                    whyItIsHalalBn = "এতে হারাম ব্যবসা বাদ দিয়ে আন্তর্জাতিক শরীয়াহ মানদণ্ড কঠোরভাবে রক্ষা করা হয়।"
                )
            )
        ),

        // SCENARIO 3: DROPSHIPPING & E-COMMERCE
        AskScenario(
            id = "ecommerce_dropshipping",
            titleBn = "ড্রপশিপিং ও অনলাইন পণ্য বিপণন",
            titleEn = "Dropshipping & E-Commerce",
            categoryBn = "বাণিজ্য ও মধ্যস্থতা",
            sampleQuery = "আমি ড্রপশিপিং বা অনলাইন পণ্য বিক্রির ব্যবসা শুরু করতে চাই",
            badgeIconName = "ShoppingCart",
            shortSummaryBn = "মালিকানাবিহীন বিক্রয় নিষেধের বিধান, সালাম চুক্তি ও কমিশন এজেন্সীর শরঈ রূপরেখা।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_possession",
                    questionBn = "পণ্য বিক্রির সময় আপনি কি পণ্যের মালিক বা জিম্মাদার (Liability)?",
                    questionEn = "Do you have ownership or liability (Daman) when selling?",
                    whyItMattersBn = "নিজের দখলে আসার আগে বিক্রেতা হিসেবে পণ্যের দাম গ্রহণ করা হাদিসে নিষিদ্ধ বায় বি-মা লা তামলিকের আওতাভুক্ত।",
                    options = listOf(
                        DiagnosticOption(
                            id = "possession_none_resell",
                            labelBn = "আমার কোনো দখল বা দায় নেই; অর্ডার পেলে তৃতীয় পক্ষের ঠিকানা দিয়ে দিই",
                            riskWeight = 2,
                            impactExplanationBn = "নিজের দায়িত্বে আসার আগে মুনাফা রেখে অন্যের পণ্য সরাসরি বিক্রি করা হাদিসে সরাসরি নিষিদ্ধ।"
                        ),
                        DiagnosticOption(
                            id = "possession_agency_wakalah",
                            labelBn = "আমি মূল প্রস্তুতকারকের অনুমোদিত প্রতিনিধি (Agent) এবং কমিশন গ্রহণ করি",
                            riskWeight = 0,
                            impactExplanationBn = "ওয়াকালাহ বিল উজরাহ (Wakalah bil Ujrah) — অনুমোদিত প্রতিনিধি হিসেবে সেবা দিয়ে পারিশ্রমিক নেওয়া সম্পূর্ণ হালাল।"
                        ),
                        DiagnosticOption(
                            id = "possession_salam_contract",
                            labelBn = "অগ্রিম মূল্য গ্রহণ এবং সুনির্দিষ্ট মেয়াদে নির্ধারিত বৈশিষ্ট্যের পণ্য সরবরাহের প্রতিশ্রুতি",
                            riskWeight = 0,
                            impactExplanationBn = "বায়েস সালাম (Salam Contract) — সুনির্দিষ্ট পণ্যের শর্তাবলি ঠিক রেখে অগ্রিম লেনদেন রাসূলুল্লাহ ﷺ বৈধ করেছেন।"
                        )
                    )
                )
            ),
            quranProofs = listOf(
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 275,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "وَأَحَلَّ اللَّهُ الْبَيْعَ وَحَرَّمَ الرِّبَا",
                    banglaTranslation = "আর আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে হারাম করেছেন।",
                    englishTranslation = "Allah has permitted trade and forbidden interest.",
                    tafsirReferenceBn = "বৈধ ব্যবসা যেখানে ঝুঁকি ও সেবার বিনিময় থাকে তা অনুমোদিত।",
                    legalSignificanceBn = "সততাভিত্তিক ব্যবসায়িক মধ্যস্থতা বৈধ।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সুনান আবু দাউদ",
                    hadithNumber = "৩৫০৩",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "হাকীম ইবনে হিযাম (রা.)",
                    arabicText = "لَا تَبِعْ مَا لَيْسَ عِندَكَ",
                    banglaTranslation = "তোমার অধিকারে যা নেই, তা বিক্রি করো না।",
                    legalSignificanceBn = "ড্রপশিপিংয়ে অন্যের গুদামের পণ্য নিজের পণ্য বলে বিক্রয় ও মুনাফা দাবির প্রধান নিষেধাজ্ঞা।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الضَّمَانُ يَتْبَعُ الْمِلْكِيَّةَ",
                    banglaTranslation = "পণ্যের ঝুঁকি ও দায় মালিকানার অনুসারী।",
                    sourceOrOriginBn = "কাওয়াইদে ফিকহিয়্যাহ",
                    practicalApplicationBn = "পণ্য নষ্ট বা ক্ষতিগ্রস্ত হলে যে ক্ষতি বহন করবে, তারই কেবল বিক্রয়ের অধিকার থাকে।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "সমসাময়িক ফিকহ একাডেমি ও ফতোয়া কাউন্সিলসমূহ",
                    verdictSummaryBn = "সাধারণ ড্রপশিপিং যেখানে ব্যক্তি ক্রেতার কাছ থেকে সরাসরি খুচরা বিক্রেতা হিসেবে দাম নিয়ে পণ্যের ঝুঁকি ছাড়াই তৃতীয় পক্ষকে অর্ডার দেয়, তা হাদিসের সরাসরি নিষেধের মধ্যে পড়ে।",
                    argumentAndEvidenceBn = "তবে ড্রপশিপার যদি প্রস্তুতকারকের সাথে সুনির্দিষ্ট 'ওয়াকালাহ' (কমিশন এজেন্ট) চুক্তি করে, অথবা বায়েস সালামের পূর্ণ শর্ত পালন করে, তবে তা সম্পূর্ণ হালাল ও বৈধ রূপ লাভ করে।",
                    conditionsBn = "ক্রেতাকে সঠিক অবস্থান জানানো এবং রিটার্ন বা ত্রুটির দায় নিজের ঘাড়ে নেওয়া আবশ্যক।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "ওয়াকালাহ বিল উজরাহ (কমিশন ভিত্তিক মধ্যস্থতা)",
                    islamicContractBn = "الوكالة بالأجر",
                    howItWorksBn = "সরাসরি বিক্রেতা দাবি না করে সাপ্লায়ারের অনুমোদিত বিপণন প্রতিনিধি হয়ে কাজ করা এবং প্রতি বিক্রয়ে পূর্বনির্ধারিত ফি বা কমিশন পাওয়া।",
                    whyItIsHalalBn = "এতে কোনো মিথ্যা দাবি নেই, এবং সেবার বিনিময়ে নির্ধারিত পারিশ্রমিক নেওয়া ইসলামে স্বীকৃত।"
                )
            )
        ),

        // SCENARIO 4: INSURANCE & TAKAFUL
        AskScenario(
            id = "insurance_takaful",
            titleBn = "বীমা পলিসি ও জীবন বীমা",
            titleEn = "Insurance & Takaful",
            categoryBn = "ঝুঁকি ব্যবস্থাপনা ও তাকাফুল",
            sampleQuery = "আমি লাইফ ইনস্যুরেন্স বা বীমা পলিসি নেওয়ার কথা ভাবছি",
            badgeIconName = "Security",
            shortSummaryBn = "বাণিজ্যিক বীমায় সুদের উপাদান, চরম গারার ও ইসলামী তাকাফুলের পারস্পরিক সাহায্য ব্যবস্থা।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_insurance_type",
                    questionBn = "বীমাটি কি বাণিজ্যিক লাভজনক সাধারণ বীমা নাকি শরীয়াহসম্মত তাকাফুল?",
                    questionEn = "Is it conventional commercial insurance or Shariah-compliant Takaful?",
                    whyItMattersBn = "প্রথাগত বাণিজ্যিক বীমায় সুদ (রিবা), জুয়া (মাইসির) ও অনিশ্চয়তা (গারার) তিনটিই একত্রিত থাকে।",
                    options = listOf(
                        DiagnosticOption(
                            id = "ins_conventional_commercial",
                            labelBn = "প্রথাগত বাণিজ্যিক লাভজনক বীমা কোম্পানি",
                            riskWeight = 2,
                            impactExplanationBn = "বিশ্বব্যাপী ফিকহ পরিষদ সর্বসম্মতভাবে প্রথাগত বাণিজ্যিক বীমাকে নিষিদ্ধ করেছে।"
                        ),
                        DiagnosticOption(
                            id = "ins_islamic_takaful",
                            labelBn = "শরীয়াহসম্মত ইসলামিক তাকাফুল (পারস্পরিক ওয়াকফ ও অনুদান ফান্ড)",
                            riskWeight = 0,
                            impactExplanationBn = "তাকাফুল পারস্পরিক সহযোগিতার (তাবাররু) ভিত্তিতে পরিচালিত হওয়ায় সম্পূর্ণ অনুমোদিত।"
                        ),
                        DiagnosticOption(
                            id = "ins_statutory_mandatory",
                            labelBn = "রাষ্ট্রীয় আইনে বাধ্যতামূলক সাধারণ বীমা (যেমন গাড়ির থার্ড পার্টি বীমা)",
                            riskWeight = 1,
                            impactExplanationBn = "আইনের বাধ্যবাধকতার কারণে ন্যূনতম অংশ নেওয়া বৈধ, তবে অতিরিক্ত ক্ষতিপূরণ আসলে তা জনকল্যাণে দান করা উচিত।"
                        )
                    )
                )
            ),
            quranProofs = listOf(
                VerifiedQuranProof(
                    surahNumber = 5,
                    ayahNumber = 2,
                    surahNameBn = "আল-মায়িদাহ",
                    surahNameAr = "سُورَةُ المَائِدَةِ",
                    arabicText = "وَتَعَاوَنُوا عَلَى الْبِرِّ وَالتَّقْوَىٰ ۖ وَلَا تَعَاوَنُوا عَلَى الْإِثْمِ وَالْعُدْوَانِ",
                    banglaTranslation = "তোমরা সৎকর্ম ও আল্লাহভীতিতে একে অপরকে সহযোগিতা করো, এবং পাপ ও সীমালঙ্ঘনের কাজে পরস্পরকে সহযোগিতা করো না।",
                    englishTranslation = "And cooperate in righteousness and piety, but do not cooperate in sin and aggression.",
                    tafsirReferenceBn = "ইসলামী তাকাফুলের মূল ভিত্তি হলো পারস্পরিক সদকা ও আপদকালীন সময়ে একে অপরের পাশে দাঁড়ানো।",
                    legalSignificanceBn = "বাণিজ্যিক লাভ ও জুয়ার পরিবর্তে পারস্পরিক সহায়তা ভিত্তিক ঝুঁকি ভাগাভাগির বৈধতা।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ মুসলিম",
                    hadithNumber = "১৫১৩",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "আবু হুরায়রা (রা.)",
                    arabicText = "نَهَى رَسُولُ اللَّهِ ﷺ عَنْ بَيْعِ الْغَرَرِ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ গারার (অনিশ্চিত বা ধোঁকাযুক্ত চুক্তি) নিষিদ্ধ করেছেন।",
                    legalSignificanceBn = "বীমায় গ্রাহক টাকা দিয়ে কী পাবে তা সম্পূর্ণ অনিশ্চিত — বিপদ হলে বেশি পাবে, না হলে সব হারাবে — যা গারার ও জুয়ার রূপ।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الْغَرَمُ بِالْغُنْمِ",
                    banglaTranslation = "দায়-দায়িত্ব ও ক্ষতি সুবিধার সমান্তরাল।",
                    sourceOrOriginBn = "কাওয়াইদে ফিকহিয়্যাহ",
                    practicalApplicationBn = "ঝুঁকি আদান-প্রদান হতে হবে পারস্পরিক অংশীদারির ভিত্তিতে, কোনো একপক্ষের সুনিশ্চিত শোষণ নয়।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "ওআইসি ইসলামিক ফিকহ একাডেমি (Resolution No. 9)",
                    verdictSummaryBn = "বাণিজ্যিক বীমা চুক্তি সর্বসম্মতভাবে হারাম, কারণ এতে রিবা, গারার ও জুয়া বিদ্যমান। এর বিপরীতে সমবায়ভিত্তিক ইসলামিক তাকাফুল পুরোপুরি বৈধ।",
                    argumentAndEvidenceBn = "তাকাফুলে গ্রাহকদের কিস্তির টাকা একটি যৌথ ওয়াকফ/অনুদান তহবিলে জমা হয়, কোনো সদস্যের ক্ষতি হলে সেখান থেকে অনুদান দেওয়া হয়।",
                    conditionsBn = "তাকাফুল কোম্পানির তহবিল অবশ্যই হালাল ও সুদমুক্ত মাধ্যমে বিনিয়োগ হতে হবে।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "ইসলামিক তাকাফুল (Islamic Takaful)",
                    islamicContractBn = "التكافل الإسلامي القائم على التبرع",
                    howItWorksBn = "পারস্পরিক অনুদানের নিয়তে তাকাফুল ফান্ডে অংশ নেওয়া, যেখান থেকে কোনো অংশগ্রহণকারীর দুর্ঘটনা ঘটলে সবাই মিলে সহায়তা করা হয়।",
                    whyItIsHalalBn = "এটি বাণিজ্যিক ক্রয়-বিক্রয় নয়, বরং পারস্পরিক ভ্রাতৃত্ব ও সদকার চুক্তি।"
                )
            )
        )
    )

    /**
     * Finds the closest matching scenario for a query or returns null.
     */
    fun findScenario(query: String): AskScenario? {
        val q = query.lowercase().trim()
        return scenarios.firstOrNull { scenario ->
            q.contains("loan") || q.contains("ঋণ") || q.contains("ধার") || q.contains("mortgage") ||
            q.contains("সুদ") || q.contains("ব্যাংক লোন") || q.contains("ক্রেডিট কার্ড") || q.contains("কর্জ")
        } ?: scenarios.firstOrNull { scenario ->
            q.contains("share") || q.contains("stock") || q.contains("শেয়ার") || q.contains("ট্রেডিং") ||
            q.contains("invest") || q.contains("বিনিয়োগ") || q.contains("crypto")
        } ?: scenarios.firstOrNull { scenario ->
            q.contains("dropship") || q.contains("ড্রপশিপ") || q.contains("ecommerce") || q.contains("অনলাইন শপ")
        } ?: scenarios.firstOrNull { scenario ->
            q.contains("insurance") || q.contains("বীমা") || q.contains("ইনস্যুরেন্স") || q.contains("তাকাফুল")
        } ?: scenarios.firstOrNull()
    }

    /**
     * Synthesizes user diagnostic answers and produces a rigorous, scholarly structured report.
     */
    fun evaluateAnswers(
        scenario: AskScenario,
        userQuery: String,
        selectedOptionIds: Map<String, String>
    ): AskBeforeYouActReport {
        var totalRiskWeight = 0
        var highestOptionWeight = 0
        val selectedOptionLabels = mutableMapOf<String, String>()
        val criticalCheckpoints = mutableListOf<String>()
        val redFlags = mutableListOf<String>()

        scenario.diagnosticQuestions.forEach { question ->
            val selectedOptId = selectedOptionIds[question.id] ?: question.options.firstOrNull()?.id ?: ""
            val option = question.options.firstOrNull { it.id == selectedOptId } ?: question.options.first()
            selectedOptionLabels[question.questionBn] = option.labelBn

            totalRiskWeight += option.riskWeight
            if (option.riskWeight > highestOptionWeight) {
                highestOptionWeight = option.riskWeight
            }

            if (option.riskWeight >= 2) {
                redFlags.add("${question.questionBn}: ${option.impactExplanationBn}")
            } else if (option.riskWeight == 1) {
                criticalCheckpoints.add("${question.questionBn}: ${option.impactExplanationBn}")
            }
        }

        val riskLevel = when {
            highestOptionWeight >= 2 -> ShariahRiskLevel.HIGH_RISK_PROHIBITED
            highestOptionWeight == 1 -> ShariahRiskLevel.CONDITIONAL_CAUTION
            else -> ShariahRiskLevel.SAFE_COMPLIANT
        }

        val executiveSummary = when (riskLevel) {
            ShariahRiskLevel.HIGH_RISK_PROHIBITED -> {
                "আপনার প্রদত্ত উত্তরমালার ভিত্তিতে এই চুক্তিতে সুস্পষ্ট শরীয়াহ পরিপন্থী ও হারাম উপাদান (যেমন সুদের শর্ত, বিলম্ব জরিমানা বা অনিশ্চিত দখলবিহীন বিক্রয়) চিহ্নিত হয়েছে। এই পরিস্থিতিতে চুক্তিটি চূড়ান্ত করা থেকে বিরত থাকা এবং হালাল বিকল্প অনুসরণ করা ঈমান ও সম্পদের সুরক্ষার জন্য অপরিহার্য।"
            }
            ShariahRiskLevel.CONDITIONAL_CAUTION -> {
                "এই চুক্তিটির মূল উদ্দেশ্য বৈধ হলেও এতে বেশ কিছু সূক্ষ্ম শরঈ সতর্কতা ও শর্ত বিদ্যমান। কোনো কোনো শর্ত পূরণ না হলে বা বিলম্ব হলে এটি সুদী দায় কিংবা গারারের ঝুঁকিতে পতিত হতে পারে। নির্দিষ্ট ধারাগুলো সংশোধন পূর্বক কোনো অভিজ্ঞ মুফতি বা শরীয়াহ উপদেষ্টার পরামর্শ গ্রহণ করুন।"
            }
            ShariahRiskLevel.SAFE_COMPLIANT -> {
                "আপনার নির্বাচিত শর্তাবলি ও কাঠামোর মধ্যে ইসলামী শরীয়াহর কোনো দৃশ্যমান নিষেধাজ্ঞা পরিলক্ষিত হয়নি। এটি বৈধ বাণিজ্য, করজে হাসানা কিংবা অনুমোদিত অংশীদারির নীতিমালার অনুকূল।"
            }
            ShariahRiskLevel.SCHOLARLY_DEBATE -> {
                "এই বিষয়ে সমসাময়িক ফিকহ একাডেমি এবং উলামায়ে কেরামের মধ্যে স্বীকৃত বহুমুখী দৃষ্টিভঙ্গি বিদ্যমান। নিচের ফিকহি বিশ্লেষণটি অধ্যয়ন করুন।"
            }
        }

        val nextSteps = when (riskLevel) {
            ShariahRiskLevel.HIGH_RISK_PROHIBITED -> listOf(
                "বর্তমান সুদী বা ঝুঁকিপূর্ণ চুক্তিপত্রে কোনো অবস্থাতেই স্বাক্ষর করবেন না।",
                "শরীয়াহ স্বীকৃত বিকল্প উপায়গুলো (যেমন করজে হাসানা বা মুরাবাহা ক্রয়-বিক্রয়) বিবেচনা করুন।",
                "যদি চরম নিরুপায় জীবন সংকটে থাকেন, তবে স্থানীয় বিশ্বস্ত ও বিজ্ঞ মুফতির নিকট ব্যক্তিগত পরিস্থিতি উপস্থাপন করে বিশেষ বিধান জেনে নিন।"
            )
            ShariahRiskLevel.CONDITIONAL_CAUTION -> listOf(
                "চুক্তির বিলম্ব জরিমানা বা চক্রবৃদ্ধি সুদের ধারাটি বাদ দেওয়ার জন্য অপর পক্ষের সাথে আলোচনা করুন।",
                "যদি ইসলামিক ব্যাংক বা তাকাফুল হয়, তবে তাদের স্বাধীন শরীয়াহ সুপারভাইজরি বোর্ডের অনুমোদনপত্র যাচাই করুন।",
                "ব্যক্তিগত লেনদেন হলে সুরা বাকারার ২৮২ নম্বর আয়াত অনুযায়ী সুস্পষ্টভাবে শর্তাবলি লিখিত ও সাক্ষ্যযুক্ত রাখুন।"
            )
            ShariahRiskLevel.SAFE_COMPLIANT -> listOf(
                "চুক্তির সমস্ত শর্ত লিখিতভাবে নথিভুক্ত রাখুন।",
                "নির্দিষ্ট সময়ে অঙ্গীকার পূরণ এবং আমানতদারিতা বজায় রাখার নিয়ত করুন।",
                "ব্যবসা বা উপার্জনে বরকতের জন্য নিয়মিত শুকরিয়া ও সদকা আদায় করুন।"
            )
            ShariahRiskLevel.SCHOLARLY_DEBATE -> listOf(
                "উভয় দৃষ্টিভঙ্গির দলিল পর্যালোচনা করে তাকওয়ার দিকটিকে প্রাধান্য দিন।",
                "আপনার স্থানীয় নির্ভরযোগ্য ফতোয়া বোর্ডের শরণাপন্ন হোন।"
            )
        }

        return AskBeforeYouActReport(
            id = "rep_${System.currentTimeMillis()}",
            query = userQuery.ifBlank { scenario.sampleQuery },
            matchedScenarioTitleBn = scenario.titleBn,
            selectedOptionLabels = selectedOptionLabels,
            riskLevel = riskLevel,
            riskScore = totalRiskWeight,
            executiveSummaryBn = executiveSummary,
            criticalCheckpoints = criticalCheckpoints,
            redFlagsIdentified = redFlags,
            quranProofs = scenario.quranProofs,
            hadithProofs = scenario.hadithProofs,
            fiqhMaxims = scenario.fiqhMaxims,
            scholarlyPositions = scenario.scholarlyPositions,
            halalAlternatives = scenario.halalAlternatives,
            recommendedNextSteps = nextSteps
        )
    }
}
