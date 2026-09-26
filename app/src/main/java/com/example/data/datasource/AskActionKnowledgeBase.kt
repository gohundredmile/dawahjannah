package com.example.data.datasource

import com.example.data.model.AskBeforeYouActReport
import com.example.data.model.AskScenario
import com.example.data.model.DiagnosticOption
import com.example.data.model.DiagnosticQuestion
import com.example.data.model.DocumentClauseAnalysis
import com.example.data.model.FiqhMaxim
import com.example.data.model.HalalAlternative
import com.example.data.model.ScholarlyPosition
import com.example.data.model.ShariahAssessmentStatus
import com.example.data.model.VerifiedHadithProof
import com.example.data.model.VerifiedQuranProof

object AskActionKnowledgeBase {

    val scenarios: List<AskScenario> = listOf(
        // SCENARIO 1: LOAN & BANK FINANCING
        AskScenario(
            id = "loan_finance",
            titleBn = "ঋণ ও ব্যাংক ফাইন্যান্সিং",
            titleEn = "Loan & Financing",
            categoryBn = "আর্থিক লেনদেন ও দায়বদ্ধতা",
            sampleQuery = "I am planning to take a loan (আমি ব্যাংক থেকে ঋণ বা লোন নেওয়ার কথা ভাবছি)",
            badgeIconName = "AccountBalance",
            shortSummaryBn = "সুদ, বিলম্ব জরিমানা, চুক্তির প্রকৃতি ও জরুরি অবস্থার শরঈ মানদণ্ড যাচাই।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_interest",
                    questionBn = "ঋণের ক্ষেত্রে কি অতিরিক্ত কোনো সুদ বা বাধ্যতামূলক উদ্বৃত্ত পরিশোধ করতে হবে?",
                    questionEn = "Is there interest (Riba) or mandatory excess on repayment?",
                    whyItMattersBn = "ইসলামী শরীয়তে মূলধনের অতিরিক্ত যেকোনো বাধ্যতামূলক পূর্বশর্তযুক্ত উদ্বৃত্তকে সর্বসম্মতভাবে 'রিবা' (সুদ) ঘোষণা করা হয়েছে, যা কবীরা গুনাহ।",
                    options = listOf(
                        DiagnosticOption(
                            id = "interest_yes_fixed",
                            labelBn = "হ্যাঁ, নির্দিষ্ট শতকরা হারে অতিরিক্ত সুদ পরিশোধ করতে হবে",
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
                            labelBn = "অতিরিক্ত নয়, কেবল প্রকৃত নথিপত্র খরচ (সার্ভিস চার্জ)",
                            riskWeight = 1,
                            impactExplanationBn = "সার্ভিস চার্জ যদি প্রকৃত নথিপত্র খরচের সমান হয় তবে বৈধ; শতকরা হারে লাভ হলে তা ছদ্মবেশী সুদ।"
                        ),
                        DiagnosticOption(
                            id = "interest_unknown",
                            labelBn = "নিশ্চিত নই / চুক্তির শর্তাবলি দেখতে হবে",
                            riskWeight = -1,
                            impactExplanationBn = "অতিরিক্ত অর্থ কীভাবে হিসাব করা হচ্ছে তা স্পষ্ট না হওয়া পর্যন্ত চূড়ান্ত শরঈ মূল্যায়ন স্থগিত থাকবে।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_contract_type",
                    questionBn = "চুক্তির কাঠামো বা ধরন কী রূপ?",
                    questionEn = "What is the contract structure?",
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
                            labelBn = "হোম লোন বা বন্ধকী ঋণ (Mortgage / Diminishing Musharakah)",
                            riskWeight = 1,
                            impactExplanationBn = "প্রথাগত বন্ধকী ঋণ সুদী; কিন্তু ডিমিনিশিং মুশারাকা (মুশারাকা মুতানাকিসাহ) ভিত্তিক বাড়ি অর্থায়ন বৈধ।"
                        ),
                        DiagnosticOption(
                            id = "contract_credit_card",
                            labelBn = "ক্রেডিট কার্ড (গ্রেস পিরিয়ডের মধ্যে পরিশোধের পরিকল্পনা)",
                            riskWeight = 1,
                            impactExplanationBn = "দেরি হলে সুদ যুক্ত হওয়ার চুক্তি থাকায় ঝুঁকি বিদ্যমান; নির্ধারিত সময়ে পরিশোধে সুদ না দিলেও সুদী শর্তে অঙ্গীকার থাকে।"
                        ),
                        DiagnosticOption(
                            id = "contract_skip",
                            labelBn = "জানি না / এড়িয়ে যান",
                            riskWeight = -1,
                            impactExplanationBn = "চুক্তির সঠিক কাঠামো জানা না থাকলে তা শরীয়াহসম্মত কিনা নির্ধারণ করা যায় না।"
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
                            impactExplanationBn = "আয়াওফি (AAOIFI) ও অধিকাংশ ইসলামিক ব্যাংকে শৃঙ্খলা রক্ষার জন্য এই শর্ত রাখা হয়।"
                        ),
                        DiagnosticOption(
                            id = "penalty_none",
                            labelBn = "না, কোনো বিলম্ব জরিমানা বা অতিরিক্ত শর্ত নেই",
                            riskWeight = 0,
                            impactExplanationBn = "এটি নিখাদ করজে হাসানার আদর্শ রূপ।"
                        ),
                        DiagnosticOption(
                            id = "penalty_not_sure",
                            labelBn = "নিশ্চিত নই / চুক্তিপত্র যাচাই করা প্রয়োজন",
                            riskWeight = -1,
                            impactExplanationBn = "বিলম্ব শর্তাবলী চুক্তিপত্রে উল্লিখিত থাকে; চুক্তিটি দেখে নিশ্চিত হতে হবে।"
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
                            impactExplanationBn = "জীবননাশের আশঙ্কাযুক্ত চরম সংকটে কোনো হালাল বিকল্প না থাকলে ফিকহি মূলনীতি 'জরুরাত' প্রযোজ্য হতে পারে।"
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
                            id = "purpose_other",
                            labelBn = "অন্যান্য ব্যক্তিগত খরচ",
                            riskWeight = 1,
                            impactExplanationBn = "সাধারণ প্রয়োজনে সুদের চুক্তিতে প্রবেশ করা নিষিদ্ধ।"
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
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "كُلُّ قَرْضٍ جَرَّ مَنْفَعَةً فَهُوَ رِبًا",
                    banglaTranslation = "যেকোনো ঋণ যা ঋণদাতার জন্য কোনো পূর্বশর্তযুক্ত অতিরিক্ত আর্থিক ফায়দা টেনে আনে, তা-ই সুদ।",
                    sourceOrOriginBn = "মুসান্নাফে ইবনে আবি শায়বা ও কাওয়াইদ ফিকহিয়্যাহ",
                    practicalApplicationBn = "ঋণ প্রদানের ক্ষেত্রে কোনো বাড়তি টাকা, উপহার বা বাধ্যতামূলক সুবিধা দাবি করা যাবে না।"
                ),
                FiqhMaxim(
                    arabicText = "الضَّرُورَاتُ تُبِيحُ الْمَحْظُورَاتِ بِقَدْرِهَا",
                    banglaTranslation = "চরম নিরুপায় অবস্থা নিষিদ্ধ বিষয়কে কেবল ততটুকুই বৈধ করে যতটুকু সংকট দূর করার জন্য অপরিহার্য।",
                    sourceOrOriginBn = "আল-আশবাহ ওয়ান নাযায়ির (ইমাম সুয়ুতী রহ.)",
                    practicalApplicationBn = "জীবননাশের ঝুঁকি না থাকলে সাধারণ আর্থিক সংকটে সুদ নেওয়া বা দেওয়া বৈধ হয় না।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "আন্তর্জাতিক ইসলামিক ফিকহ একাডেমি (OIC-IFA) ও আয়াওফি (AAOIFI)",
                    verdictSummaryBn = "বাণিজ্যিক ব্যাংকের প্রচলিত সুদী ঋণ সর্বসম্মতভাবে হারাম। সম্পদভিত্তিক ইসলামিক মুরাবাহা বা ইজারা বৈধ বিকল্প।",
                    argumentAndEvidenceBn = "টাকার লেনদেনে অতিরিক্ত ধার্য করা কুরআনের নিষিদ্ধ রিবার অন্তর্ভুক্ত। তবে ইসলামিক ব্যাংকের পণ্যভিত্তিক ক্রয়-বিক্রয়ে ঝুঁকি ও মালিকানা হস্তান্তরিত হলে তা বৈধ বাণিজ্য।",
                    conditionsBn = "ব্যাংককে সরাসরি পণ্যের মালিকানা ও ঝুঁকি গ্রহণ করতে হবে; নিছক কাগুজে চালান দিয়ে টাকা দেওয়া যাবে না।"
                ),
                ScholarlyPosition(
                    bodyOrSchoolBn = "চার মাযহাবের জমহুর ফুকাহায়ে কেরাম (হানাফী, মালিকী, শাফেয়ী ও হাম্বলী)",
                    verdictSummaryBn = "ঋণের উপর পূর্বশর্তযুক্ত যেকোনো অতিরিক্ত অর্থ সর্বসম্মতভাবে হারাম।",
                    argumentAndEvidenceBn = "ইজমাউস সাহাবা ও স্পষ্ট কুরআনিক নস দ্বারা প্রমাণিত।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "মুরাবাহা (Murabahah) পণ্য ক্রয়-বিক্রয়",
                    islamicContractBn = "বাইউল মুরাবাহা লিল আমির বিশ-শিরা",
                    howItWorksBn = "ব্যাংক বা অর্থায়নকারী আপনার চাহিদামতো সম্পদ বা কাঁচামাল নগদ মূল্যে কিনে তার সাথে নির্ধারিত মুনাফা যোগ করে আপনার কাছে কিস্তিতে বিক্রি করবে।",
                    whyItIsHalalBn = "টাকার কারবার নয়, বরং বাস্তব পণ্যের কেনাবেচা এবং পণ্য হস্তান্তরের ঝুঁকি অর্থায়নকারী বহন করে।"
                ),
                HalalAlternative(
                    titleBn = "মুশারাকা মুতানাকিসাহ (ডিমিনিশিং মুশারাকা - বাড়ি অর্থায়ন)",
                    islamicContractBn = "মুশারাকা মুতানাকিসাহ মা'য়াল ইজারা",
                    howItWorksBn = "আপনি ও ব্যাংক যৌথভাবে বাড়ি ক্রয় করবেন। আপনি ব্যাংকের অংশের জন্য ভাড়া প্রদান করবেন এবং ধাপে ধাপে ব্যাংকের শেয়ার কিনে নিয়ে একক মালিক হবেন।",
                    whyItIsHalalBn = "এখানে বাড়িটির যৌথ মালিকানা প্রতিষ্ঠিত হয় এবং ভাড়া কেবল ব্যবহৃত অংশের জন্য আদায় করা হয়।"
                ),
                HalalAlternative(
                    titleBn = "করজে হাসানা (উত্তম সুদমুক্ত ধার)",
                    islamicContractBn = "আল-কারদুল হাসান",
                    howItWorksBn = "পরিবার, আত্মীয় বা সুদমুক্ত সমবায় থেকে কোনো অতিরিক্ত বা মুনাফা ছাড়া কেবল আসল ফেরত দেওয়ার শর্তে অর্থ গ্রহণ।",
                    whyItIsHalalBn = "ইসলামে এই ঋণ প্রদানকে সদকার চেয়েও বেশি সাওয়াবপূর্ণ ঘোষণা করা হয়েছে।"
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
                    whyItMattersBn = "ইসলামে এমন কোনো কোম্পানিতে অংশীদার হওয়া হারাম যার মূল ব্যবসা হারাম (যেমন মদ, জুয়া, সুদভিত্তিক ব্যাংক, শূকর বা পর্নোগ্রাফি)।",
                    options = listOf(
                        DiagnosticOption(
                            id = "business_pure_halal",
                            labelBn = "সম্পূর্ণ হালাল পণ্য বা সেবা (প্রযুক্তি, ওষুধ, হালাল খাদ্য, বস্ত্র)",
                            riskWeight = 0,
                            impactExplanationBn = "মূল ব্যবসা হালাল হওয়া শরীয়াহসম্মত স্ক্রিনিংয়ের প্রথম ও প্রধান শর্ত।"
                        ),
                        DiagnosticOption(
                            id = "business_direct_haram",
                            labelBn = "প্রত্যক্ষ হারাম খাত (সুদভিত্তিক ব্যাংক, ক্যাসিনো, মদ বা মাদক)",
                            riskWeight = 2,
                            impactExplanationBn = "হারাম খাতের কোম্পানিতে ১টি শেয়ার কেনাও ওই হারামে সক্রিয় অংশীদারিত্ব হিসেবে গণ্য।"
                        ),
                        DiagnosticOption(
                            id = "business_mixed_income",
                            labelBn = "মিশ্র ব্যবসা (মূল ব্যবসা হালাল, তবে সামান্য সুদী আয় বা সুদী আমানত রয়েছে)",
                            riskWeight = 1,
                            impactExplanationBn = "আয়াওফি (AAOIFI) মানদণ্ড অনুযায়ী সুদী আয় ৫%-এর কম হতে হবে এবং লভ্যাংশ থেকে তা সদকা করতে হবে।"
                        ),
                        DiagnosticOption(
                            id = "business_unknown",
                            labelBn = "কোম্পানির ব্যালান্স শিট ও আয়ের উৎস জানা নেই",
                            riskWeight = -1,
                            impactExplanationBn = "আয়ের উৎস ও ব্যালান্স শিট পরীক্ষা না করে শেয়ার কেনা চরম আর্থিক ও শরঈ ঝুঁকিপূর্ণ।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_trading_method",
                    questionBn = "লেনদেনের পদ্ধতি কী (স্পট ক্যাশ নাকি মার্জিন / ফিউচার্স / শর্ট সেলিং)?",
                    questionEn = "What is the trading mechanism (Spot Cash, Margin, Futures, or Short Selling)?",
                    whyItMattersBn = "নিজের দখলে আসার আগে বিক্রি করা এবং ঋণের সুদ দিয়ে শেয়ার কেনা (মার্জিন লোন) সর্বসম্মতভাবে নিষিদ্ধ।",
                    options = listOf(
                        DiagnosticOption(
                            id = "trading_spot_delivery",
                            labelBn = "ক্যাশ একাউন্ট বা স্পট ডেলিভারি (মূল্য পরিশোধ করে মালিকানা ও ঝুঁকি গ্রহণ)",
                            riskWeight = 0,
                            impactExplanationBn = "প্রকৃত শেয়ারের মালিকানা ও ঝুঁকি বুঝে নিয়ে শেয়ার ধরে রাখা বৈধ।"
                        ),
                        DiagnosticOption(
                            id = "trading_margin_broker_loan",
                            labelBn = "মার্জিন ট্রেডিং (ব্রোকারের কাছ থেকে সুদে ঋণ নিয়ে লিভারেজ ট্রেড)",
                            riskWeight = 2,
                            impactExplanationBn = "ব্রোকারের ঋণ ও সুদের লেনদেন একই সাথে জড়িত থাকায় এটি সম্পূর্ণ হারাম।"
                        ),
                        DiagnosticOption(
                            id = "trading_short_selling",
                            labelBn = "শর্ট সেলিং (মালিকানাবিহীন ধার করা শেয়ার আগে বিক্রি করে পরে কেনার চুক্তি)",
                            riskWeight = 2,
                            impactExplanationBn = "রাসূলুল্লাহ ﷺ মালিকানা ছাড়া বিক্রয় করতে কঠোরভাবে নিষেধ করেছেন।"
                        ),
                        DiagnosticOption(
                            id = "trading_derivatives_options",
                            labelBn = "অপশনস / ফিউচার্স ডেরিভেটিভস (ভবিষ্যত দামের উপর নিছক বাজি ধরা)",
                            riskWeight = 2,
                            impactExplanationBn = "বাস্তব পণ্য হস্তান্তর ছাড়া মূল্যের ওঠানামায় অর্থ লেনদেন জুয়া ও ক্বিমারের পর্যায়ভুক্ত।"
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
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا لَا تَأْكُلُوا أَمْوَالَكُم بَيْنَكُم بِالْبَاطِلِ إِلَّا أَن تَكُونَ تِجَارَةً عَن تَرَاضٍ مِّنكُمْ",
                    banglaTranslation = "হে ঈমানদারগণ! তোমরা একে অপরের সম্পদ অন্যায়ভাবে গ্রাস করো না, তবে তোমাদের পারস্পরিক সন্তুষ্টির ভিত্তিতে যে ব্যবসা হয় তা ভিন্ন।",
                    englishTranslation = "O you who have believed, do not consume one another's wealth unjustly but only [in lawful] business by mutual consent.",
                    tafsirReferenceBn = "ইবনে কাসীর: বাতিল উপায়ে সম্পদ উপার্জন হলো সুদ, জুয়া, ধোঁকাবাজি এবং এমন চুক্তি যাতে বাস্তব কোনো দ্রব্যের বিনিময় নেই।",
                    legalSignificanceBn = "পুঁজিবাজারের প্রতিটি লেনদেনে বাস্তব সম্পদ ও স্বচ্ছ মালিকানার ভিত্তি থাকা অপরিহার্য।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সুনান আত-তিরমিজী",
                    hadithNumber = "১২৩২",
                    authenticityGradeBn = "সহীহ (আলবানী রহ.)",
                    narratorBn = "হাকীম ইবনে হিযাম (রা.)",
                    arabicText = "لَا تَبِعْ مَا لَيْسَ عِنْدَكَ",
                    banglaTranslation = "যা তোমার অধিকারে বা মালিকানায় নেই, তা তুমি বিক্রি করো না।",
                    legalSignificanceBn = "শর্ট সেলিং, দখলবিহীন দ্রুত কেনাবেচা এবং মালিকানা ছাড়া শেয়ার হস্তান্তরের অবৈধতার মূল ভিত্তি।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الْغُرْمُ بِالْغُنْمِ",
                    banglaTranslation = "যে ব্যক্তি লাভের হকদার হবে, তাকে ক্ষতির ঝুঁকিও বহন করতে হবে।",
                    sourceOrOriginBn = "মাজাল্লাতুল আহকাম আল-আদলিয়্যাহ (ধারা ৮৭)",
                    practicalApplicationBn = "শেয়ার ব্যবসায় আসল টাকার কোনো গ্যারান্টি বা ফিক্সড লাভ নেওয়া যাবে না; ঝুঁকির অংশীদার হতে হবে।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "আয়াওফি (AAOIFI) শরীয়াহ স্ট্যান্ডার্ড নং ২১",
                    verdictSummaryBn = "শেয়ারে বিনিয়োগ বৈধ যদি কোম্পানি শরীয়াহ স্ক্রিনিং পাস করে (সুদী ঋণ ৩৩% এর নিচে ও সুদী বিনিয়োগ ৩৩% এর নিচে)।",
                    argumentAndEvidenceBn = "সাধারণ অংশীদারি চুক্তি (শারিকাহ আল-ইনান) অনুযায়ী শেয়ারহোল্ডাররা কোম্পানির আনুপাতিক মালিক।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "শরীয়াহ সম্মত ইনডেক্স ফান্ড / সুকুক",
                    islamicContractBn = "শারিকাহ ও সুকুক আল-ইস্তিসমার",
                    howItWorksBn = "শুধুমাত্র শরীয়াহ অনুমোদিত কোম্পানিতে দীর্ঘমেয়াদী বিনিয়োগ যেখানে সুদ ও অনৈতিক আয়ের স্ক্রিনিং নিশ্চিত থাকে।",
                    whyItIsHalalBn = "প্রতিষ্ঠানভিত্তিক শরীয়াহ কমিটি দ্বারা প্রতিনিয়ত অডিট করা হয়।"
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
                    id = "q_dropship_contract",
                    questionBn = "গ্রাহক যখন অর্ডার দেয়, তখন পণ্যের মালিকানা ও দখল কার কাছে থাকে?",
                    questionEn = "Who owns and possesses the item when the customer orders?",
                    whyItMattersBn = "ইসলামে নিজের মালিকানা ও কবজা (দখল) ছাড়া নির্দিষ্ট পণ্য বিক্রেতা হিসেবে বিক্রি করা নিষেধ।",
                    options = listOf(
                        DiagnosticOption(
                            id = "dropship_commission_agent",
                            labelBn = "আমি সরবরাহকারীর অনুমোদিত কমিশন এজেন্ট (ওয়াকালা / দালালি)",
                            riskWeight = 0,
                            impactExplanationBn = "এজেন্ট হিসেবে সরবরাহকারীর পণ্য প্রদর্শন করে কমিশন নেওয়া সর্বসম্মতভাবে বৈধ।"
                        ),
                        DiagnosticOption(
                            id = "dropship_salam_contract",
                            labelBn = "পণ্য নির্দিষ্ট নয়, গুণাগুণ নির্ধারিত (বাইউস সালাম নীতি অনুসরণ)",
                            riskWeight = 0,
                            impactExplanationBn = "পণ্যের সুস্পষ্ট বিবরণ ও ডেলিভারির সময় নির্ধারণ করে অগ্রিম মূল্য নেওয়া সালাম চুক্তির অন্তর্ভুক্ত।"
                        ),
                        DiagnosticOption(
                            id = "dropship_unowned_resale",
                            labelBn = "সরবরাহকারীকে না জানিয়ে তার পণ্য নিজের বলে বিক্রি করে পরে অর্ডার প্লেস করি",
                            riskWeight = 2,
                            impactExplanationBn = "এটি স্পষ্ট 'বাইউ মা লা ইয়ামলিক' (মালিকানাবিহীন বিক্রয়) যা হাদীসে নিষিদ্ধ।"
                        )
                    )
                )
            ),
            quranProofs = listOf(
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 282,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا إِذَا تَدَايَنتُم بِدَيْنٍ إِلَىٰ أَجَلٍ مُّسَمًّى فَاكْتُبُوهُ",
                    banglaTranslation = "হে ঈমানদারগণ! যখন তোমরা কোনো নির্দিষ্ট মেয়াদের জন্য ঋণের (বা অগ্রিম মূল্যের) আদান-প্রদান করো, তখন তা লিখে রাখো।",
                    englishTranslation = "O you who have believed, when you contract a debt for a specified term, write it down.",
                    tafsirReferenceBn = "ইবনে আব্বাস (রা.) বলেন: এই আয়াতটি 'বাইউস সালাম' (অগ্রিম মূল্যে ভবিষ্যতের পণ্যের চুক্তি) বৈধতার দলিল।",
                    legalSignificanceBn = "পণ্য ভবিষ্যতে সরবরাহ করার শর্তে অগ্রিম মূল্য নিয়ে চুক্তি করার বৈধতা।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ বুখারী",
                    hadithNumber = "২২৪০",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "ইবনে আব্বাস (রা.)",
                    arabicText = "مَنْ أَسْلَفَ فِي شَيْءٍ فَلْيُسْلِفْ فِي كَيْلٍ مَعْلُومٍ، وَوَزْنٍ مَعْلُومٍ، إِلَى أَجَلٍ مَعْلُومٍ",
                    banglaTranslation = "যে ব্যক্তি কোনো পণ্যের অগ্রিম চুক্তি (সালাম) করতে চায়, সে যেন নির্দিষ্ট পরিমাপ, নির্দিষ্ট ওজন এবং নির্দিষ্ট মেয়াদের শর্তে চুক্তি করে।",
                    legalSignificanceBn = "অনলাইন বা ড্রপশিপিংয়ে পণ্যের স্পেসিফিকেশন ও পৌঁছানোর সময় সুনির্দিষ্ট করার নির্দেশ।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الْأَصْلُ فِي الْمُعَامَلَاتِ الْإِبَاحَةُ",
                    banglaTranslation = "লেনদেনের ক্ষেত্রে মূল বিধান হলো বৈধতা, যতক্ষণ না কোনো সুস্পষ্ট নিষেধাজ্ঞার প্রমাণ আসে।",
                    sourceOrOriginBn = "কাওয়াইদুল ফিকহিয়্যাহ",
                    practicalApplicationBn = "নতুন প্রযুক্তির ই-কমার্স চুক্তিগুলো বৈধ, যদি তাতে ধোঁকা, সুদ বা দখলবিহীন বিক্রি না থাকে।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "আন্তর্জাতিক ফিকহ একাডেমি (OIC-IFA) রেজ্যুলেশন নং ৬৩",
                    verdictSummaryBn = "ড্রপশিপিংকে শরীয়াহসম্মত করতে হলে মধ্যস্থতাকারীকে সরবরাহকারীর 'ওয়াকিল' (এজেন্ট) হতে হবে অথবা সালাম চুক্তি সম্পাদন করতে হবে।",
                    argumentAndEvidenceBn = "অন্যথায় গ্রাহকের সাথে চুক্তি করার সময় বিক্রেতার হাতে পণ্যের মালিকানা ও নিশ্চয়তা না থাকায় বিবাদের আশঙ্কা থাকে।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "ওয়াকালাহ বিল উজরাহ (কমিশন ভিত্তিক এজেন্সি)",
                    islamicContractBn = "চুক্তিভিত্তিক দালালি / এজেন্সি",
                    howItWorksBn = "সরাসরি প্রস্তুতকারক বা হোলসেলারের সাথে লিখিত চুক্তি করুন যে আপনি তাদের অনুমোদিত বিক্রেতা এবং প্রতি বিক্রয়ে নির্দিষ্ট কমিশন পাবেন।",
                    whyItIsHalalBn = "এখানে আপনি মালিকানা দাবি করছেন না, বরং প্রকৃত সেবার বিনিময়ে পারিশ্রমিক নিচ্ছেন।"
                )
            )
        ),

        // SCENARIO 4: INSURANCE & TAKAFUL
        AskScenario(
            id = "insurance_takaful",
            titleBn = "বীমা পলিসি ও তাকাফুল",
            titleEn = "Insurance & Takaful",
            categoryBn = "ঝুঁকি ব্যবস্থাপনা ও তাকাফুল",
            sampleQuery = "আমি লাইফ ইনস্যুরেন্স বা বীমা পলিসি নেওয়ার কথা ভাবছি",
            badgeIconName = "Security",
            shortSummaryBn = "বাণিজ্যিক বীমায় সুদের উপাদান, চরম গারার ও ইসলামী তাকাফুলের পারস্পরিক সাহায্য ব্যবস্থা।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_insurance_type",
                    questionBn = "বীমা কোম্পানিটির কাঠামো কী রকম?",
                    questionEn = "What is the operational structure of the insurance?",
                    whyItMattersBn = "বাণিজ্যিক বীমায় টাকা দিয়ে টাকা কেনা (সুদ) এবং ভবিষ্যৎ দুর্ঘটনার অনিশ্চয়তা (গারার ও কিমার) থাকে।",
                    options = listOf(
                        DiagnosticOption(
                            id = "ins_commercial_profit",
                            labelBn = "প্রথাগত বাণিজ্যিক বীমা (যেখানে কোম্পানি প্রিমিয়ামের টাকা সুদে বিনিয়োগ করে)",
                            riskWeight = 2,
                            impactExplanationBn = "আন্তর্জাতিক সকল ফিকহ কাউন্সিলের সর্বসম্মত সিদ্ধান্ত অনুযায়ী প্রচলিত কমার্শিয়াল বীমা হারাম।"
                        ),
                        DiagnosticOption(
                            id = "ins_islamic_takaful",
                            labelBn = "ইসলামী তাকাফুল (পারস্পরিক দান ও সহযোগিতার ওয়াকফ ফান্ড ভিত্তিক)",
                            riskWeight = 0,
                            impactExplanationBn = "তাকাফুল পারস্পরিক তাবাররু (দান) ভিত্তিক হওয়ায় এটি শরীয়াহসম্মত।"
                        ),
                        DiagnosticOption(
                            id = "ins_statutory_mandatory",
                            labelBn = "রাষ্ট্রীয় আইন অনুযায়ী বাধ্যতামূলক বীমা (যেমন গাড়ির থার্ড পার্টি বীমা বা কর্মস্থলের স্বাস্থ্য বীমা)",
                            riskWeight = 1,
                            impactExplanationBn = "আইনগত বাধ্যবাধকতায় সর্বনিম্ন বাধ্যতামূলক প্যাকেজ গ্রহণ ক্ষমাযোগ্য।"
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
                    banglaTranslation = "তোমরা সৎকর্ম ও তাকওয়ার কাজে পরস্পরকে সহযোগিতা করো, আর পাপ ও সীমালঙ্ঘনের কাজে একে অপরকে সহযোগিতা করো না।",
                    englishTranslation = "And cooperate in righteousness and piety, but do not cooperate in sin and aggression.",
                    tafsirReferenceBn = "মা'আরিফুল কুরআন: এই আয়াতটি ইসলামী তাকাফুল ও পারস্পরিক সামাজিক সহায়তার মূল দর্শন।",
                    legalSignificanceBn = "বাণিজ্যিক মুনাফার বদলে পারস্পরিক সহযোগিতামূলক বীমা বা তাকাফুলের শরঈ ভিত্তি।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ মুসলিম",
                    hadithNumber = "১৫১৩",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "আবু হুরায়রা (রা.)",
                    arabicText = "نَهَى رَسُولُ اللَّهِ ﷺ عَنْ بَيْعِ الْحَصَاةِ، وَعَنْ بَيْعِ الْغَرَرِ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ পাথর নিক্ষেপ করে বিক্রয় এবং চরম অনিশ্চয়তাপূর্ণ (গারার) বিক্রয় নিষিদ্ধ করেছেন।",
                    legalSignificanceBn = "বাণিজ্যিক বীমায় প্রিমিয়াম দিলে ক্ষতিপূরণ পাওয়া যাবে কি যাবে না তা অনিশ্চিত থাকায় এটি গারারের অন্তর্ভুক্ত।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الْغَرَرُ الْكَثِيرُ مُفْسِدٌ لِلْعُقُودِ",
                    banglaTranslation = "অতিরিক্ত অনিশ্চয়তা ও ধোঁয়াশা যেকোনো আর্থিক চুক্তিকে বাতিল ও অবৈধ করে দেয়।",
                    sourceOrOriginBn = "কাওয়াইদুল ফিকহ",
                    practicalApplicationBn = "যে চুক্তিতে কী পাওয়া যাবে তা ভাগ্যের উপর নির্ভরশীল, তা নিষিদ্ধ।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "আন্তর্জাতিক ইসলামিক ফিকহ একাডেমি (OIC-IFA) মক্কা আল-মুকাররামা",
                    verdictSummaryBn = "প্রথাগত বাণিজ্যিক জীবন বীমা ও সাধারণ বীমা সর্বসম্মতভাবে হারাম; এর শরঈ বিকল্প হলো ইসলামী তাকাফুল।",
                    argumentAndEvidenceBn = "বাণিজ্যিক বীমায় একই সাথে রিবা (সুদ), গারার (অনিশ্চয়তা) এবং কিমার (জুয়া) তিনটি কবীরা গুনাহ উপস্থিত থাকে।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "ইসলামী তাকাফুল (Islamic Takaful)",
                    islamicContractBn = "আত-তাকাফুল আল-ইসলামী আল-মুশতারাক",
                    howItWorksBn = "গ্রাহকদের জমাকৃত অর্থ একটি ওয়াকফ বা তাবাররু (দান) ফান্ডে জমা হয়। কোনো সদস্য ক্ষতিগ্রস্ত হলে ফান্ড থেকে সহায়তা দেওয়া হয় এবং অবশিষ্ট উদ্বৃত্ত সদস্যদের ফেরত দেওয়া হয়।",
                    whyItIsHalalBn = "কোম্পানি এখানে ফান্ডের ব্যবস্থাপক (মুদারিব), মুনাফাশিকারী মালিক নয়।"
                )
            )
        ),

        // SCENARIO 5: FREELANCING & TECH EMPLOYMENT [NEW]
        AskScenario(
            id = "freelance_employment",
            titleBn = "ফ্রিল্যান্সিং, আইটি ও কর্মসংস্থান",
            titleEn = "Freelancing & Tech Employment",
            categoryBn = "উপার্জন ও কর্মসংস্থান",
            sampleQuery = "আমি ক্লায়েন্টের জন্য সফটওয়্যার বা ডিজাইন তৈরি করছি, এই আয় কি হালাল?",
            badgeIconName = "Work",
            shortSummaryBn = "হারাম কাজে প্রত্যক্ষ সহযোগিতা, অনৈতিক কনটেন্ট ফিল্টারিং ও শরীয়াহসম্মত পারিশ্রমিকের নীতিমালা।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_freelance_nature",
                    questionBn = "আপনার তৈরি করা কাজের মূল ব্যবহার বা ক্লায়েন্টের প্রতিষ্ঠান কী?",
                    questionEn = "What is the primary usage of your work or client's business?",
                    whyItMattersBn = "ইসলামে কোনো সরাসরি হারাম বা পাপাচারপূর্ণ কাজে প্রত্যক্ষ সহায়তা করে উপার্জিত অর্থ হারাম।",
                    options = listOf(
                        DiagnosticOption(
                            id = "tech_pure_halal_client",
                            labelBn = "সম্পূর্ণ সাধারণ বা হালাল সেবা (শিক্ষা, ই-কমার্স, স্বাস্থ্য, ব্যবসায়িক সফটওয়্যার)",
                            riskWeight = 0,
                            impactExplanationBn = "বৈধ কাজের বিনিময়ে পারিশ্রমিক সম্পূর্ণ হালাল ও পবিত্র উপার্জন।"
                        ),
                        DiagnosticOption(
                            id = "tech_direct_haram_client",
                            labelBn = "সরাসরি হারাম প্রতিষ্ঠান (ক্যাসিনো/জুয়ার অ্যাপ, সুদী ব্যাংকিং কোর সিস্টেম, মদের কোম্পানি)",
                            riskWeight = 2,
                            impactExplanationBn = "হারাম প্রতিষ্ঠানের মূল পরিচালন ব্যবস্থা বা ডিজাইনে প্রত্যক্ষ সহায়তা করা গুনাহে অংশীদারিত্ব।"
                        ),
                        DiagnosticOption(
                            id = "tech_dual_use",
                            labelBn = "দ্বিমুখী ব্যবহারযোগ্য টুল (যা হালাল বা হারাম উভয় কাজে ব্যবহৃত হতে পারে)",
                            riskWeight = 1,
                            impactExplanationBn = "যদি টুলটি মূলত হালাল উদ্দেশ্যে হয় তবে বৈধ; কিন্তু যদি জানেন ক্লায়েন্ট হারামে ব্যবহার করবে তবে পরিহার্য।"
                        ),
                        DiagnosticOption(
                            id = "tech_not_sure",
                            labelBn = "ক্লায়েন্ট কী কাজে ব্যবহার করবে আমি নিশ্চিত নই",
                            riskWeight = -1,
                            impactExplanationBn = "সন্দেহজনক কাজ হলে ক্লায়েন্টের কাছে প্রকল্পটির উদ্দেশ্য জেনে নেওয়া তাকওয়ার দাবি।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_freelance_duties",
                    questionBn = "কাজের মধ্যে অনৈতিক ছবি, সংগীত বা নিষিদ্ধ উপাদান তৈরি/সম্পাদনা আছে কি?",
                    questionEn = "Does the task involve creating or editing prohibited media/content?",
                    whyItMattersBn = "অশ্লীলতা ও হারাম বিষয় প্রচারের জন্য কোনো পারিশ্রমিক নেওয়া বৈধ নয়।",
                    options = listOf(
                        DiagnosticOption(
                            id = "media_clean",
                            labelBn = "না, কোনো অশ্লীল বা অনৈতিক উপাদান নেই",
                            riskWeight = 0,
                            impactExplanationBn = "কাজের পরিবেশ ও আউটপুট সম্পূর্ণ শালীন।"
                        ),
                        DiagnosticOption(
                            id = "media_explicit",
                            labelBn = "হ্যাঁ, অনৈতিক কনটেন্ট বা হারাম প্রচারণামূলক উপাদান রয়েছে",
                            riskWeight = 2,
                            impactExplanationBn = "পাপ ছড়ানোর কাজে শ্রম দেওয়া শরীয়তে নিষিদ্ধ।"
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
                    arabicText = "وَلَا تَعَاوَنُوا عَلَى الْإِثْمِ وَالْعُدْوَانِ ۚ وَاتَّقُوا اللَّهَ",
                    banglaTranslation = "আর পাপ ও সীমালঙ্ঘনের কাজে একে অপরকে সহযোগিতা করো না। আর আল্লাহকে ভয় করো।",
                    englishTranslation = "And do not cooperate in sin and aggression. And fear Allah.",
                    tafsirReferenceBn = "ইবনে কাসীর: হারাম কাজে কোনো মুসলিমের জন্য শ্রম, মেধা বা প্রযুক্তিগত সহায়তা দেওয়া হারাম উপার্জনের কারণ।",
                    legalSignificanceBn = "পাপকর্মে প্রত্যক্ষ সহায়তার নিষিদ্ধতা।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "মুসনাদে আহমাদ",
                    hadithNumber = "৮২৮",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "রাফে' ইবনে খাদীজ (রা.)",
                    arabicText = "عَمَلُ الرَّجُلِ بِيَدِهِ، وَكُلُّ بَيْعٍ مَبْرُورٍ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ কে জিজ্ঞেস করা হলো: কোন উপার্জন সবচেয়ে উত্তম? তিনি বললেন: 'মানুষের নিজ হাতের শ্রম এবং প্রতিটি সৎ ও নির্ভেজাল ব্যবসা।'",
                    legalSignificanceBn = "হালাল হাতের শ্রম ও প্রযুক্তিগত মেধা প্রয়োগের শ্রেষ্ঠত্ব।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الإِعَانَةُ عَلَى الْمَعْصِيَةِ مَعْصِيَةٌ",
                    banglaTranslation = "পাপের কাজে প্রত্যক্ষ সহায়তা করা নিজেও একটি পাপ।",
                    sourceOrOriginBn = "কাওয়াইদুল ফিকহ",
                    practicalApplicationBn = "যে সফটওয়্যার বা ডিজাইন কেবল হারাম কাজে ব্যবহৃত হয়, তা তৈরি করা বা রক্ষণাবেক্ষণ করা নিষিদ্ধ।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "সমসাময়িক ফিকহ একাডেমি ও ফতোয়া বোর্ড",
                    verdictSummaryBn = "আইটি বিশেষজ্ঞ যদি প্রত্যক্ষ হারাম কাজে নিয়োজিত না হয়ে সাধারণ টুল ডেভেলপ করেন তবে উপার্জন হালাল; কিন্তু সরাসরি সুদী বা অনৈতিক অ্যালগরিদম প্রোগ্রাম করা হারাম।",
                    argumentAndEvidenceBn = "প্রত্যক্ষ পাপের অংশীদার ও পরোক্ষ নিরপেক্ষ কাজের মধ্যে ফিকহি পার্থক্য বিদ্যমান।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "হালাল সেক্টরে ফ্রিল্যান্সিং ও এন্টারপ্রাইজ ডেভেলপমেন্ট",
                    islamicContractBn = "ইজারা আলাল আমাল (শ্রমের চুক্তি)",
                    howItWorksBn = "এডটেক, হেলথকেয়ার, ফিনটেক (ইসলামিক), ই-কমার্স ও লজিস্টিকসের মতো কল্যাণকর খাতে মেধা প্রদান।",
                    whyItIsHalalBn = "উপকারমূলক হালাল কর্মে শ্রমের বিনিময়ে পারিশ্রমিক সর্বসম্মতভাবে সম্মানিত।"
                )
            )
        ),

        // SCENARIO 6: FOOD & INGREDIENTS [NEW]
        AskScenario(
            id = "food_ingredients",
            titleBn = "খাদ্যদ্রব্য ও হালাল উপাদান যাচাই",
            titleEn = "Food Ingredients & Halal Status",
            categoryBn = "আহার ও দৈনন্দিন ব্যবহার্য",
            sampleQuery = "এই খাদ্য উপাদান বা সম্পূরক কি খাওয়া হালাল?",
            badgeIconName = "Restaurant",
            shortSummaryBn = "জিলাটিনের উৎস, অ্যালকোহল নিষ্কাশন, প্রাণিজ চর্বি ও হালাল সার্টিফিকেশনের শরঈ বিধান।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_gelatin_source",
                    questionBn = "পণ্যে কি জিলাটিন বা প্রাণিজ এনজাইম রয়েছে?",
                    questionEn = "Does the product contain gelatin or animal-derived enzymes?",
                    whyItMattersBn = "শূকর বা অপবিত্রভাবে জবাইকৃত পশুর উপাদান ভক্ষণ করা সর্বসম্মতভাবে হারাম।",
                    options = listOf(
                        DiagnosticOption(
                            id = "gelatin_plant_fish",
                            labelBn = "না, সম্পূর্ণ উদ্ভিদজাত (Plant-based / Agar) অথবা মাছের জিলাটিন",
                            riskWeight = 0,
                            impactExplanationBn = "উদ্ভিদজাত বা জলজ প্রাণীর উপাদান সম্পূর্ণ হালাল ও পবিত্র।"
                        ),
                        DiagnosticOption(
                            id = "gelatin_pork",
                            labelBn = "হ্যাঁ, শূকরের জিলাটিন (Porcine Gelatin)",
                            riskWeight = 2,
                            impactExplanationBn = "শূকরের যেকোনো অংশ খাওয়া সম্পূর্ণ হারাম।"
                        ),
                        DiagnosticOption(
                            id = "gelatin_halal_bovine",
                            labelBn = "হ্যাঁ, শরীয়াহসম্মত জবাইকৃত গরুর জিলাটিন (Halal Certified Bovine)",
                            riskWeight = 0,
                            impactExplanationBn = "হালালভাবে জবাইকৃত পশুর জিলাটিন ব্যবহারে কোনো আপত্তি নেই।"
                        ),
                        DiagnosticOption(
                            id = "gelatin_unspecified",
                            labelBn = "শুধু 'Gelatin' লেখা আছে, উৎস উল্লেখ নেই",
                            riskWeight = 1,
                            impactExplanationBn = "অমুসলিম দেশে প্রস্তুত হলে সন্দেহজনক বিধায় পরিহার করা অথবা প্রস্তুতকারককে জিজ্ঞেস করা আবশ্যক।"
                        )
                    )
                ),
                DiagnosticQuestion(
                    id = "q_alcohol_extract",
                    questionBn = "প্রক্রিয়াজাতকরণে কোনো অ্যালকোহল বা মাদক রয়েছে কি?",
                    questionEn = "Is there alcohol used in processing or extraction?",
                    whyItMattersBn = "খামর (মাদক) হারাম; তবে ফ্লেভারে দ্রাবক হিসেবে ব্যবহৃত অতি সামান্য অ-মাদক অ্যালকোহলের বিষয়ে ফিকহি শর্ত রয়েছে।",
                    options = listOf(
                        DiagnosticOption(
                            id = "alcohol_none",
                            labelBn = "না, কোনো অ্যালকোহল নেই (Alcohol Free)",
                            riskWeight = 0,
                            impactExplanationBn = "সম্পূর্ণ নিরাপদ ও পবিত্র।"
                        ),
                        DiagnosticOption(
                            id = "alcohol_beverage",
                            labelBn = "হ্যাঁ, ওয়াইন, বিয়ার বা কোনো নেশাজাতীয় পানীয়ের উপাদান মেশানো",
                            riskWeight = 2,
                            impactExplanationBn = "যেকোনো পরিমাণের মাদকীয় পানীয় খাবারে মেশানো সর্বসম্মতভাবে হারাম।"
                        ),
                        DiagnosticOption(
                            id = "alcohol_trace_solvent",
                            labelBn = "ভ্যানিলা বা ফ্লেভার দ্রাবক হিসেবে অতি সামান্য সিন্থেটিক অ্যালকোহল যা নেশা সৃষ্টি করে না",
                            riskWeight = 1,
                            impactExplanationBn = "অনেক ফিকহ কাউন্সিলের মতে নেশা সৃষ্টি না করলে এবং রূপান্তরিত হলে শর্তসাপেক্ষে মার্জনীয়।"
                        )
                    )
                )
            ),
            quranProofs = listOf(
                VerifiedQuranProof(
                    surahNumber = 2,
                    ayahNumber = 173,
                    surahNameBn = "আল-বাক্বারাহ",
                    surahNameAr = "سُورَةُ البَقَرَةِ",
                    arabicText = "إِنَّمَا حَرَّمَ عَلَيْكُمُ الْمَيْتَةَ وَالدَّمَ وَلَحْمَ الْخِنزِيرِ وَمَا أُهِلَّ بِهِ لِغَيْرِ اللَّهِ",
                    banglaTranslation = "তিনি তোমাদের উপর কেবল হারাম করেছেন মৃত জীব, রক্ত, শূকরের মাংস এবং যার উপর আল্লাহ ছাড়া অন্য কারও নাম উচ্চারণ করা হয়েছে।",
                    englishTranslation = "He has only forbidden to you dead animals, blood, the flesh of swine, and that which has been dedicated to other than Allah.",
                    tafsirReferenceBn = "ইবনে কাসীর: হারাম খাদ্যের তালিকা সুস্পষ্ট; যা শরীরের জন্য ক্ষতিকর ও আত্মাকে অপবিত্র করে তা বর্জনীয়।",
                    legalSignificanceBn = "মৌলিক হারাম খাদ্য তালিকার প্রামাণ্য নস।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ বুখারী ও মুসলিম",
                    hadithNumber = "৫২ (বুখারী) / ১৫৯৯ (মুসলিম)",
                    authenticityGradeBn = "সহীহ (মুত্তাফাকুন আলাইহ)",
                    narratorBn = "নু'মান ইবনে বাশীর (রা.)",
                    arabicText = "إِنَّ الْحَلَالَ بَيِّنٌ، وَإِنَّ الْحَمَرَ بَيِّنٌ، وَبَيْنَهُمَا مُشْتَبَهَاتٌ لَا يَعْلَمُهُنَّ كَثِيرٌ مِنَ النَّاسِ، فَمَنِ اتَّقَى الشُّبُهَاتِ اسْتَبْرَأَ لِدِينِهِ وَعِرْضِهِ",
                    banglaTranslation = "নিশ্চয়ই হালাল স্পষ্ট এবং হারামও স্পষ্ট। আর এ দুটির মাঝে রয়েছে বহু সন্দেহজনক বিষয় যা অনেক মানুষই জানে না। যে ব্যক্তি সন্দেহজনক বিষয়গুলো থেকে বেঁচে থাকল, সে তার দ্বীন ও সম্মান রক্ষা করল।",
                    legalSignificanceBn = "খাদ্য ও উপাদানের ক্ষেত্রে সন্দেহযুক্ত বিষয় পরিহার করার নববী নির্দেশনা।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "الْأَصْلُ فِي الْأَشْيَاءِ الطَّهَارَةُ حَتَّى تَثْبُتَ النَّجَاسَةُ",
                    banglaTranslation = "যাবতীয় জিনিসের মূল অবস্থা হলো পবিত্রতা, যতক্ষণ না কোনো নাপাকির প্রমাণ নিশ্চিত হয়।",
                    sourceOrOriginBn = "কাওয়াইদুল ফিকহ",
                    practicalApplicationBn = "উদ্ভিদজাত বা রাসায়নিক উপাদান পবিত্র গণ্য হবে যদি না তাতে অপবিত্র কিছু মেশানো প্রমাণিত হয়।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "ইউরোপীয় ফতোয়া ও গবেষণা পরিষদ (ECFR) ও ওয়ার্ল্ড হালাল ট্রাস্ট",
                    verdictSummaryBn = "জিলাটিন যদি শূকর থেকে হয় তবে হারাম; রাসায়নিক রূপান্তর (ইস্তিহালাহ) হলেও শূকরের ক্ষেত্রে জমহুর উলামা তা গ্রহণ করেননি। নির্ভরযোগ্য হালাল সার্টিফিকেট যাচাই আবশ্যক।",
                    argumentAndEvidenceBn = "অপবিত্র প্রাণী থেকে আহরিত অংশ খাদ্য হিসেবে ব্যবহার করা আত্মশুদ্ধির পরিপন্থী।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "উদ্ভিদজাত বিকল্প (Agar-agar, Pectin, Cellulose)",
                    islamicContractBn = "হালাল প্রাকৃতিক খাদ্য",
                    howItWorksBn = "মিষ্টান্ন ও জেলির ক্ষেত্রে প্রাণিজ জিলাটিনের পরিবর্তে সমুদ্রের শৈবাল (আগার) বা ফলের পেকটিন ব্যবহার।",
                    whyItIsHalalBn = "উদ্ভিজ্জ উপাদান কোনো সন্দেহ ছাড়াই ১০০% হালাল ও স্বাস্থ্যসম্মত।"
                )
            )
        ),

        // SCENARIO 7: MULTI-LEVEL MARKETING (MLM) & PYRAMIDS [NEW]
        AskScenario(
            id = "mlm_pyramid",
            titleBn = "এমএলএম ও রেফারেল নেটওয়ার্ক",
            titleEn = "MLM & Referral Networks",
            categoryBn = "নেটওয়ার্ক মার্কেটিং ও দালালি",
            sampleQuery = "আমি একটি রেফারেল বা নেটওয়ার্ক মার্কেটিং কোম্পানিতে যুক্ত হতে চাই",
            badgeIconName = "Hub",
            shortSummaryBn = "সদস্য সংগ্রহে কমিশন, বাধ্যতামূলক জয়েনিং ফি, জুয়া ও গারারের শরঈ তাহকীক।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_mlm_income_source",
                    questionBn = "কোম্পানির প্রধান আয়ের উৎস ও আপনার কমিশন কিসের ওপর ভিত্তি করে?",
                    questionEn = "What is the primary driver of earnings (Product sales vs. Recruiting)?",
                    whyItMattersBn = "বাস্তব পণ্য বিক্রির বদলে নিছক নতুন সদস্য এনে তাদের ফিসের অংশ নেওয়া পিরামিড স্কিম ও জুয়া।",
                    options = listOf(
                        DiagnosticOption(
                            id = "mlm_recruitment_driven",
                            labelBn = "নতুন সদস্য ভর্তি করানো (রেফারেল ফি / মেম্বারশিপ ফি এর অংশ পাওয়া যায়)",
                            riskWeight = 2,
                            impactExplanationBn = "নতুন সদস্যের টাকায় পুরাতনদের মুনাফা দেওয়া স্পষ্ট জুয়া ও গারারের শামিল।"
                        ),
                        DiagnosticOption(
                            id = "mlm_genuine_product_sales",
                            labelBn = "প্রকৃত পণ্যের খুচরা বিক্রয় (পণ্য ছাড়া কোনো জয়েনিং ফি নেই)",
                            riskWeight = 1,
                            impactExplanationBn = "পণ্য বাস্তব হলেও মাল্টিলেভেল নেটওয়ার্কের শর্তাবলিতে এক চুক্তিতে একাধিক চুক্তি থাকলে সতর্কতা আবশ্যক।"
                        ),
                        DiagnosticOption(
                            id = "mlm_single_tier_affiliate",
                            labelBn = "একক স্তরের এফিলিয়েট (Single Tier: কেবল আমার সুপারিশে বিক্রিত পণ্যের কমিশন)",
                            riskWeight = 0,
                            impactExplanationBn = "এটি বৈধ দালালি (জুয়ালাহ / সামসারাহ) এর অন্তর্ভুক্ত।"
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
                    arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا لَا تَأْكُلُوا أَمْوَالَكُم بَيْنَكُم بِالْبَاطِلِ",
                    banglaTranslation = "হে ঈমানদারগণ! তোমরা একে অপরের সম্পদ অন্যায়ভাবে গ্রাস করো না।",
                    englishTranslation = "O you who have believed, do not consume one another's wealth unjustly.",
                    tafsirReferenceBn = "ইবনে কাসীর: পিরামিড স্কিমে নিচের স্তরের অধিকাংশ মানুষের অর্থ প্রতারণামূলকভাবে উপরের ব্যক্তিরা গ্রাস করে।",
                    legalSignificanceBn = "পরের হক অন্যায়ভাবে আত্মসাতের নিষিদ্ধতা।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "মুসনাদে আহমাদ ও তিরমিজী",
                    hadithNumber = "১২৩৪",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "আবু হুরায়রা (রা.)",
                    arabicText = "نَهَى رَسُولُ اللَّهِ ﷺ عَنْ بَيْعَتَيْنِ فِي بَيْعَةٍ",
                    banglaTranslation = "রাসূলুল্লাহ ﷺ এক চুক্তির মধ্যে অপর চুক্তিকে শর্তযুক্ত করতে নিষেধ করেছেন।",
                    legalSignificanceBn = "পণ্য কেনার শর্তে ডিস্ট্রিবিউটরশিপ বা সদস্য বানানোর বাধ্যবাধকতার নিষেধাজ্ঞা।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "كُلُّ مُعَامَلَةٍ دَارَتْ بَيْنَ الْغُنْمِ وَالْغُرْمِ فَهِيَ مَيْسِرٌ",
                    banglaTranslation = "যে লেনদেনের ভিত্তি কেবল লাভ ও চরম ক্ষতির দোলাচলে থাকে (ভাগ্যের খেলা), তা-ই জুয়া।",
                    sourceOrOriginBn = "কাওয়াইদুল ফিকহ",
                    practicalApplicationBn = "টাকা দিয়ে সদস্য হয়ে যদি লাভ অনিশ্চিত হয় এবং অধিকাংশ মানুষ ক্ষতিগ্রস্ত হয়, তা মায়সির।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "সৌদি আরবের স্থায়ী ফতোয়া বোর্ড (আল-লাজনাতুদ দাইমাহ) ও আন্তর্জাতিক ফিকহ একাডেমি",
                    verdictSummaryBn = "পিরামিড ও মাল্টিলেভেল মার্কেটিং (MLM) এর লেনদেন সর্বসম্মতভাবে হারাম।",
                    argumentAndEvidenceBn = "এখানে পণ্যের উদ্দেশ্য গৌণ, মূল উদ্দেশ্য থাকে সদস্য সংগ্রহের টাকার লেনদেন যা জুয়া ও রিবার সমন্বয়।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "একক স্তরের এফিলিয়েট মার্কেটিং (Single-Tier Affiliate)",
                    islamicContractBn = "জুয়ালাহ / সামসারাহ (বৈধ দালালি)",
                    howItWorksBn = "আপনি কোনো হালাল পণ্যের প্রচার করলেন এবং কেউ লিংক থেকে কিনলে বিক্রেতা আপনাকে একক কমিশন দিল। কোনো ডাউনলাইন বা মেম্বার ফি নেই।",
                    whyItIsHalalBn = "এখানে আপনি বাস্তব প্রচার সেবার বিনিময় পাচ্ছেন, কোনো পিরামিড তৈরি হচ্ছে না।"
                )
            )
        ),

        // SCENARIO 8: GOLD, FOREX & CURRENCY EXCHANGE [NEW]
        AskScenario(
            id = "gold_forex_trading",
            titleBn = "স্বর্ণ ও বৈদেশিক মুদ্রা বিনিময় (Forex / Sarf)",
            titleEn = "Gold, Currency & Forex Trading",
            categoryBn = "মুদ্রা ও বিনিময় বাজার",
            sampleQuery = "আমি অনলাইন ফরেক্স বা সোনা কেনাবেচা করতে চাই",
            badgeIconName = "Paid",
            shortSummaryBn = "সার্ফ চুক্তির শর্ত, হাতে হাতে দখল (তাকাবুদ), লেভারেজের সুদ ও বিলম্ব নিষ্পত্তির শরঈ বিধান।",
            diagnosticQuestions = listOf(
                DiagnosticQuestion(
                    id = "q_sarf_possession",
                    questionBn = "টাকা পরিশোধের সাথে সাথেই কি মুদ্রা বা স্বর্ণের প্রকৃত মালিকানা ও দখল (কবজা) পাওয়া যায়?",
                    questionEn = "Is there immediate physical or constructive possession (Taqabud)?",
                    whyItMattersBn = "ইসলামে স্বর্ণ বা মুদ্রার বিনিময়ে অন্য মুদ্রা কিনতে হলে একই মজলিসে হাতে হাতে দখল (তাকাবুদ ফিল মজলিস) আবশ্যক।",
                    options = listOf(
                        DiagnosticOption(
                            id = "sarf_immediate_spot",
                            labelBn = "হ্যাঁ, সাথে সাথেই পূর্ণ মূল্য দিয়ে মুদ্রা বা স্বর্ণের দখল/হস্তান্তর ঘটে (স্পট ক্যাশ)",
                            riskWeight = 0,
                            impactExplanationBn = "হাতে হাতে বিনিময় হলে এবং মূল্য সম্পূর্ণ পরিশোধিত হলে কারেন্সি এক্সচেঞ্জ হালাল।"
                        ),
                        DiagnosticOption(
                            id = "sarf_delayed_settlement",
                            labelBn = "না, নিষ্পত্তি ২ দিন পর (T+2) হয় কিংবা কেবল দামের ওঠানামায় কন্ট্রাক্ট বিক্রি করা হয়",
                            riskWeight = 2,
                            impactExplanationBn = "মুদ্রার লেনদেনে বিলম্ব ঘটলে তা সরাসরি 'রিবা আন-নাসিয়াহ' (বিলম্বে সুদ) হয়ে যায়।"
                        ),
                        DiagnosticOption(
                            id = "sarf_leveraged_cfd",
                            labelBn = "লেভারেজযুক্ত অনলাইন ফরেক্স ট্রেডিং (CFD / মার্জিন সোয়াপ ফি সহ)",
                            riskWeight = 2,
                            impactExplanationBn = "সোয়াপ ফি সুদ এবং কোনো বাস্তব দখল ছাড়া নিছক জুয়া খেলা।"
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
                    banglaTranslation = "আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে হারাম করেছেন।",
                    englishTranslation = "Allah has permitted trade and has forbidden interest.",
                    tafsirReferenceBn = "মুফাসসিরীন: মুদ্রার সাথে মুদ্রার লেনদেনে সামান্য শর্ত লঙ্ঘন হলেও তা সুদ হয়ে যায়।",
                    legalSignificanceBn = "বৈধ ব্যবসা ও রিবার মধ্যকার পার্থক্য।"
                )
            ),
            hadithProofs = listOf(
                VerifiedHadithProof(
                    sourceBookBn = "সহীহ মুসলিম",
                    hadithNumber = "১৫৮৭",
                    authenticityGradeBn = "সহীহ",
                    narratorBn = "উবাদাহ ইবনুস সামিত (রা.)",
                    arabicText = "الذَّهَبُ بِالذَّهَبِ، وَالْفِضَّةُ بِالْفِضَّةِ... يَدًا بِيَدٍ، فَإِذَا اخْتَلَفَتْ هَذِهِ الْأَصْنَافُ فَبِيعُوا كَيْفَ شِئْتُمْ إِذَا كَانَ يَدًا بِيَدٍ",
                    banglaTranslation = "স্বর্ণের বিনিময়ে স্বর্ণ, রূপার বিনিময়ে রূপা... সমানে সমান এবং হাতে হাতে হতে হবে। আর যখন এগুলোর প্রকারভেদ ভিন্ন হবে, তখন যেভাবে ইচ্ছা বিক্রি করো, তবে শর্ত হলো তা হাতে হাতে হতে হবে।",
                    legalSignificanceBn = "কারেন্সি ও স্বর্ণ বিনিময়ের মৌলিক আন্তর্জাতিক শরঈ আইন (বাইউস সার্ফ)।"
                )
            ),
            fiqhMaxims = listOf(
                FiqhMaxim(
                    arabicText = "التَّقَابُضُ شَرْطٌ فِي صِحَّةِ عَقْدِ الصَّرْفِ",
                    banglaTranslation = "মুদ্রা ও মূল্যবান ধাতুর বিনিময় চুক্তি বৈধ হওয়ার জন্য একই মজলিসে দখল গ্রহণ অপরিহার্য শর্ত।",
                    sourceOrOriginBn = "কাওয়াইদুল ফিকহিয়্যাহ",
                    practicalApplicationBn = "ডিজিটাল কারেন্সি ট্রেডিংয়ে ব্যালান্স তাৎক্ষণিক স্থানান্তরিত না হলে চুক্তিটি বাতিল হবে।"
                )
            ),
            scholarlyPositions = listOf(
                ScholarlyPosition(
                    bodyOrSchoolBn = "আয়াওফি (AAOIFI) শরীয়াহ স্ট্যান্ডার্ড নং ১ (মুদ্রা বাণিজ্য)",
                    verdictSummaryBn = "অনলাইন ফরেক্স ব্রোকারেজ মার্জিন ট্রেডিং ও লেভারেজ সর্বসম্মতভাবে হারাম; কেবল তাৎক্ষণিক দখলযুক্ত স্পট মুদ্রা রূপান্তর বৈধ।",
                    argumentAndEvidenceBn = "রোলওভার বা সোয়াপ ফি হলো প্রকাশ্য সুদ এবং ফিউচার্স কারেন্সি কন্ট্রাক্টে কোনো তাকাবুদ থাকে না।"
                )
            ),
            halalAlternatives = listOf(
                HalalAlternative(
                    titleBn = "বাস্তব স্পট কারেন্সি এক্সচেঞ্জ (Spot Currency Conversion)",
                    islamicContractBn = "বাইউস সার্ফ আস-সহীহ",
                    howItWorksBn = "এক দেশের মুদ্রা দিয়ে অন্য দেশের মুদ্রা সম্পূর্ণ মূল্য পরিশোধ করে তাৎক্ষণিক নিজের ওয়ালেটে বা ব্যাংক অ্যাকাউন্টে জমা নেওয়া।",
                    whyItIsHalalBn = "এখানে আধুনিক নিয়মানুযায়ী 'আল-ক্ববযুল হুকমী' (আইনগত গঠনমূলক দখল) তাৎক্ষণিক সম্পন্ন হয়।"
                )
            )
        )
    )

    fun findScenario(query: String): AskScenario? {
        val q = query.lowercase().trim()
        return scenarios.firstOrNull { sc ->
            sc.titleBn.lowercase().contains(q) ||
            sc.titleEn.lowercase().contains(q) ||
            sc.sampleQuery.lowercase().contains(q) ||
            q.contains(sc.id.lowercase()) ||
            (sc.id == "loan_finance" && (q.contains("loan") || q.contains("ঋণ") || q.contains("লোন") || q.contains("সুদ") || q.contains("finance") || q.contains("mortgage") || q.contains("কর্জ"))) ||
            (sc.id == "stocks_trading" && (q.contains("stock") || q.contains("শেয়ার") || q.contains("শেয়ার") || q.contains("trade") || q.contains("ট্রেড") || q.contains("crypto") || q.contains("ক্রিপ্টো"))) ||
            (sc.id == "ecommerce_dropshipping" && (q.contains("drop") || q.contains("শিপিং") || q.contains("ই-কমার্স") || q.contains("ecommerce") || q.contains("অনলাইন শপ"))) ||
            (sc.id == "insurance_takaful" && (q.contains("insurance") || q.contains("বীমা") || q.contains("বিমা") || q.contains("ইনস্যুরেন্স") || q.contains("তাকাফুল"))) ||
            (sc.id == "freelance_employment" && (q.contains("freelance") || q.contains("ফ্রিল্যান্স") || q.contains("চাকরি") || q.contains("job") || q.contains("client") || q.contains("সফটওয়্যার"))) ||
            (sc.id == "food_ingredients" && (q.contains("food") || q.contains("খাবার") || q.contains("gelatin") || q.contains("জিলাটিন") || q.contains("হালাল খাবার") || q.contains("alcohol"))) ||
            (sc.id == "mlm_pyramid" && (q.contains("mlm") || q.contains("এমএলএম") || q.contains("referral") || q.contains("রেফারেল") || q.contains("নেটওয়ার্ক") || q.contains("pyramid"))) ||
            (sc.id == "gold_forex_trading" && (q.contains("forex") || q.contains("ফরেক্স") || q.contains("gold") || q.contains("স্বর্ণ") || q.contains("সোনার") || q.contains("currency")))
        }
    }

    /**
     * Feature: Document & Contract Clause Analysis Engine (Section 17).
     * Quotes actual wording, extracts "The document says..." vs "The Islamic significance...",
     * and flags critical legal issues (Riba, late penalties, Gharar, possession, etc.).
     */
    fun analyzePastedDocument(documentText: String): List<DocumentClauseAnalysis> {
        val text = documentText.trim()
        if (text.isBlank()) return emptyList()

        val results = mutableListOf<DocumentClauseAnalysis>()
        val lines = text.split("\n", ".", ";").map { it.trim() }.filter { it.length > 10 }

        lines.forEach { chunk ->
            val lower = chunk.lowercase()
            when {
                lower.contains("interest") || lower.contains("সুদ") || lower.contains("apr") || lower.contains("markup") -> {
                    results.add(
                        DocumentClauseAnalysis(
                            clauseTitle = "সুদ বা অতিরিক্ত আর্থিক চার্জের ধারা",
                            quotedText = chunk,
                            documentMeaningBn = "নথিতে মূলধনের ওপর নির্দিষ্ট শতকরা হারে অতিরিক্ত সুদ বা চার্জ পরিশোধের অঙ্গীকার চাওয়া হয়েছে।",
                            islamicSignificanceBn = "ইসলামী শরীয়তে মূলধনের অতিরিক্ত যেকোনো বাধ্যবাধকতাকে সর্বসম্মতভাবে 'রিবা' গণ্য করা হয়, যা কবীরা গুনাহ।",
                            riskCategoryBn = "রিবা / সুদ",
                            isConcerning = true
                        )
                    )
                }
                lower.contains("penalty") || lower.contains("late fee") || lower.contains("বিলম্ব") || lower.contains("জরিমানা") || lower.contains("default") -> {
                    results.add(
                        DocumentClauseAnalysis(
                            clauseTitle = "বিলম্ব জরিমানা ও পেনাল্টি ধারা",
                            quotedText = chunk,
                            documentMeaningBn = "নির্ধারিত সময়ে কিস্তি বা অর্থ পরিশোধ না করলে অতিরিক্ত জরিমানা বা চক্রবৃদ্ধি চার্জ আরোপের বিধান রয়েছে।",
                            islamicSignificanceBn = "বিলম্বের বিনিময়ে অতিরিক্ত অর্থ গ্রহণ জাহেলিয়াত যুগের সুদের মূল রূপ। তবে অর্থ যদি ব্যাংকের আয় না হয়ে বাধ্যতামূলক সদকা ফান্ডে যায়, আয়াওফি তা শর্তসাপেক্ষে অনুমতি দেয়।",
                            riskCategoryBn = "বিলম্ব জরিমানা",
                            isConcerning = true
                        )
                    )
                }
                lower.contains("guarantee") || lower.contains("জামানত") || lower.contains("collateral") || lower.contains("বন্ধক") -> {
                    results.add(
                        DocumentClauseAnalysis(
                            clauseTitle = "জামানত ও বন্ধকী নিরাপত্তা ধারা",
                            quotedText = chunk,
                            documentMeaningBn = "ঋণ বা চুক্তি সুরক্ষার জন্য সম্পদ বন্ধক (রাহন) রাখার বিধান উল্লেখ করা হয়েছে।",
                            islamicSignificanceBn = "শরীয়াহ অনুযায়ী ঋণ সুরক্ষায় কোনো সম্পদ জামানত রাখা বৈধ, তবে ঋণদাতা ওই বন্ধকী সম্পদ নিজ প্রয়োজনে ভোগ করতে পারবে না।",
                            riskCategoryBn = "স্বাভাবিক ও অনুমোদিত শর্ত",
                            isConcerning = false
                        )
                    )
                }
                lower.contains("delivery") || lower.contains("ownership") || lower.contains("title") || lower.contains("হস্তান্তর") || lower.contains("দখল") -> {
                    results.add(
                        DocumentClauseAnalysis(
                            clauseTitle = "মালিকানা ও ঝুঁকি হস্তান্তর ধারা",
                            quotedText = chunk,
                            documentMeaningBn = "পণ্যের ঝুঁকি ও মালিকানা কখন ক্রেতার কাছে হস্তান্তরিত হবে তার শর্ত।",
                            islamicSignificanceBn = "ইসলামী বাণিজ্যে বিক্রেতার পক্ষ থেকে পণ্যের ঝুঁকি ও দায় গ্রাহকের কাছে স্পষ্টভাবে হস্তান্তরিত হওয়া অপরিহার্য।",
                            riskCategoryBn = "দখল ও মালিকানা",
                            isConcerning = false
                        )
                    )
                }
                lower.contains("subject to change") || lower.contains("discretion") || lower.contains("অনির্ধারিত") || lower.contains("পরিবর্তনশীল") -> {
                    results.add(
                        DocumentClauseAnalysis(
                            clauseTitle = "অস্পষ্টতা ও একতরফা শর্ত পরিবর্তনের ধারা",
                            quotedText = chunk,
                            documentMeaningBn = "একতরফাভাবে ফি বা শর্ত পরিবর্তন করার একচ্ছত্র অধিকার অপর পক্ষ সংরক্ষণ করেছে।",
                            islamicSignificanceBn = "চুক্তিতে মাত্রাতিরিক্ত অনিশ্চয়তা বা ধোঁয়াশা (গারার) সৃষ্টি করে যা ভবিষ্যতে বিরোধের কারণ হতে পারে।",
                            riskCategoryBn = "গারার / অস্পষ্টতা",
                            isConcerning = true
                        )
                    )
                }
            }
        }

        if (results.isEmpty()) {
            results.add(
                DocumentClauseAnalysis(
                    clauseTitle = "প্রদত্ত নথির প্রাথমিক পর্যালোচনা",
                    quotedText = text.take(120) + "...",
                    documentMeaningBn = "নথিটির প্রদত্ত অংশে কোনো প্রত্যক্ষ প্রচলিত সুদী পরিভাষা পাওয়া যায়নি।",
                    islamicSignificanceBn = "তবে পূর্ণ চুক্তিপত্র ও পরিশিষ্টসমূহ যাচাই ছাড়া কোনো চূড়ান্ত সিদ্ধান্তে পৌঁছানো সমীচীন নয়।",
                    riskCategoryBn = "নিরপেক্ষ / অতিরিক্ত পর্যালোচনা আবশ্যক",
                    isConcerning = false
                )
            )
        }

        return results.take(6)
    }

    /**
     * Evaluates diagnostic answers strictly adhering to Sections 9, 20 & 25.
     * ZERO HALAL SCORES OR PERCENTAGES. Produces a nuanced assessment and practical steps.
     */
    fun evaluateAnswers(
        scenario: AskScenario,
        userQuery: String,
        selectedOptionIds: Map<String, String>,
        documentClauses: List<DocumentClauseAnalysis> = emptyList()
    ): AskBeforeYouActReport {
        var highestRiskWeight = 0
        var hasUnknownOrMissing = false
        val selectedOptionLabels = mutableMapOf<String, String>()
        val importantFacts = mutableListOf<String>()
        val whatIsStillUnclear = mutableListOf<String>()

        scenario.diagnosticQuestions.forEach { question ->
            val selectedOptId = selectedOptionIds[question.id] ?: question.options.firstOrNull()?.id ?: ""
            val option = question.options.firstOrNull { it.id == selectedOptId } ?: question.options.first()
            selectedOptionLabels[question.questionBn] = option.labelBn

            if (option.riskWeight == -1) {
                hasUnknownOrMissing = true
                whatIsStillUnclear.add("${question.questionBn}: ব্যবহারকারীর পক্ষ থেকে নিশ্চিত তথ্য পাওয়া যায়নি (${option.labelBn})।")
            } else {
                if (option.riskWeight > highestRiskWeight) {
                    highestRiskWeight = option.riskWeight
                }
                if (option.riskWeight >= 2) {
                    importantFacts.add("সতর্কতা: ${question.questionBn} — ${option.labelBn} (${option.impactExplanationBn})")
                } else if (option.riskWeight == 1) {
                    importantFacts.add("শর্ত: ${question.questionBn} — ${option.labelBn} (${option.impactExplanationBn})")
                } else {
                    importantFacts.add("অনুকূল উপাদান: ${question.questionBn} — ${option.labelBn}")
                }
            }
        }

        // Incorporate document findings into facts
        documentClauses.forEach { clause ->
            if (clause.isConcerning) {
                importantFacts.add("নথির ধারা [${clause.clauseTitle}]: \"${clause.quotedText.take(80)}\" → ${clause.islamicSignificanceBn}")
                if (highestRiskWeight < 2 && clause.riskCategoryBn.contains("সুদ")) {
                    highestRiskWeight = 2
                }
            }
        }

        val assessmentStatus = when {
            hasUnknownOrMissing && highestRiskWeight < 2 -> ShariahAssessmentStatus.INSUFFICIENT_INFORMATION
            highestRiskWeight >= 2 -> ShariahAssessmentStatus.GENERALLY_PROHIBITED
            highestRiskWeight == 1 -> ShariahAssessmentStatus.CONDITIONALLY_PERMISSIBLE
            else -> ShariahAssessmentStatus.CLEARLY_SUPPORTED
        }

        val whatIUnderstand = buildString {
            append("আপনি \"${userQuery.ifBlank { scenario.sampleQuery }}\" পদক্ষেপটি গ্রহণের পরিকল্পনা বিবেচনা করছেন। ")
            append("আপনার প্রদত্ত উত্তরের ভিত্তিতে জানা গেছে যে বিষয়টি মূলত '${scenario.categoryBn}' এর সাথে সংশ্লিষ্ট। ")
            if (hasUnknownOrMissing) {
                append("তবে চুক্তির কিছু মৌলিক শর্তাবলি এখনো সম্পূর্ণরূপে চিহ্নিত করা যায়নি।")
            } else {
                append("চুক্তির প্রধান উপাদানসমূহ প্রাথমিক মূল্যায়নের আওতায় নেওয়া হয়েছে।")
            }
        }

        val assessmentSummary = when (assessmentStatus) {
            ShariahAssessmentStatus.GENERALLY_PROHIBITED ->
                "আপনার প্রদত্ত উত্তরমালার ভিত্তিতে এই চুক্তিতে সুস্পষ্ট শরীয়াহ পরিপন্থী উপাদান (যেমন সরাসরি রিবা/সুদ, অননুমোদিত বিলম্ব জরিমানা বা নিষিদ্ধ বিক্রয়) চিহ্নিত হয়েছে। এই পরিস্থিতিতে চুক্তিটি চূড়ান্ত করা থেকে বিরত থাকা এবং হালাল বিকল্প অনুসরণ করা ঈমান ও সম্পদের সুরক্ষার জন্য অপরিহার্য।"
            ShariahAssessmentStatus.CONDITIONALLY_PERMISSIBLE ->
                "এই চুক্তিটির মূল উদ্দেশ্য বৈধ হলেও এতে বেশ কিছু সূক্ষ্ম শরঈ সতর্কতা ও নির্দিষ্ট শর্ত বিদ্যমান। কোনো কোনো শর্ত পূরণ না হলে বা বিলম্ব হলে এটি সুদী দায় কিংবা গারারের ঝুঁকিতে পতিত হতে পারে। নির্দিষ্ট ধারাগুলো সংশোধন পূর্বক কোনো অভিজ্ঞ মুফতি বা শরীয়াহ উপদেষ্টার পরামর্শ গ্রহণ করুন।"
            ShariahAssessmentStatus.CLEARLY_SUPPORTED ->
                "আপনার নির্বাচিত শর্তাবলি ও কাঠামোর মধ্যে ইসলামী শরীয়াহর কোনো দৃশ্যমান নিষেধাজ্ঞা পরিলক্ষিত হয়নি। এটি বৈধ বাণিজ্য, করজে হাসানা কিংবা অনুমোদিত অংশীদারির নীতিমালার অনুকূল।"
            ShariahAssessmentStatus.INSUFFICIENT_INFORMATION ->
                "গুরুত্বপূর্ণ কয়েকটি শর্তের সঠিক তথ্য না থাকায় কোনো সিদ্ধান্তমূলক অভিমত দেওয়া সম্ভব নয়। চুক্তির সঠিক কাঠামো ও লাভ-লোকসান বণ্টনের নিয়ম স্পষ্ট করার পর পুনরায় যাচাই করা আবশ্যক।"
            ShariahAssessmentStatus.SCHOLARLY_DISAGREEMENT ->
                "এই বিষয়ে সমসাময়িক ফিকহ একাডেমি এবং উলামায়ে কেরামের মধ্যে স্বীকৃত বহুমুখী দৃষ্টিভঙ্গি বিদ্যমান। নিচের ফিকহি বিশ্লেষণটি অধ্যয়ন করুন।"
            ShariahAssessmentStatus.REQUIRES_SCHOLARLY_REVIEW ->
                "বিষয়টির জটিলতা ও ব্যক্তিগত পরিস্থিতির আলোকে একজন বিজ্ঞ মুফতি বা শরীয়াহ বোর্ডের ব্যক্তিগত পর্যালোচনা আবশ্যক।"
        }

        val practicalNextSteps = when (assessmentStatus) {
            ShariahAssessmentStatus.GENERALLY_PROHIBITED -> listOf(
                "বর্তমান সুদী বা ঝুঁকিপূর্ণ চুক্তিপত্রে কোনো অবস্থাতেই স্বাক্ষর করবেন না।",
                "শরীয়াহ স্বীকৃত বিকল্প উপায়গুলো (যেমন করজে হাসানা বা মুরাবাহা ক্রয়-বিক্রয়) বিবেচনা করুন।",
                "যদি চরম নিরুপায় জীবন সংকটে থাকেন, তবে স্থানীয় বিশ্বস্ত ও বিজ্ঞ মুফতির নিকট ব্যক্তিগত পরিস্থিতি উপস্থাপন করে বিশেষ বিধান জেনে নিন।"
            )
            ShariahAssessmentStatus.CONDITIONALLY_PERMISSIBLE -> listOf(
                "চুক্তির বিলম্ব জরিমানা বা চক্রবৃদ্ধি সুদের ধারাটি বাদ দেওয়ার জন্য অপর পক্ষের সাথে আলোচনা করুন।",
                "যদি ইসলামিক ব্যাংক বা তাকাফুল হয়, তবে তাদের স্বাধীন শরীয়াহ সুপারভাইজরি বোর্ডের অনুমোদনপত্র ও অডিট রিপোর্ট যাচাই করুন।",
                "ব্যক্তিগত লেনদেন হলে সুরা বাকারার ২৮২ নম্বর আয়াত অনুযায়ী সুস্পষ্টভাবে শর্তাবলি লিখিত ও সাক্ষ্যযুক্ত রাখুন।"
            )
            ShariahAssessmentStatus.CLEARLY_SUPPORTED -> listOf(
                "চুক্তির সমস্ত শর্ত লিখিতভাবে নথিভুক্ত রাখুন।",
                "নির্দিষ্ট সময়ে অঙ্গীকার পূরণ এবং আমানতদারিতা বজায় রাখার নিয়ত করুন।",
                "ব্যবসা বা উপার্জনে বরকতের জন্য নিয়মিত শুকরিয়া ও সদকা আদায় করুন।"
            )
            ShariahAssessmentStatus.INSUFFICIENT_INFORMATION -> listOf(
                "প্রতিষ্ঠান বা অপর পক্ষের নিকট থেকে সম্পূর্ণ শর্তাবলি (Terms & Conditions) লিখিতভাবে সংগ্রহ করুন।",
                "দেরি হলে বা ব্যর্থ হলে কী পরিমাণ অতিরিক্ত টাকা দিতে হবে তা স্পষ্ট জেনে নিন।",
                "নথির প্রাসঙ্গিক অংশগুলো এই টুলের 'চুক্তি ও শর্তাবলি স্ক্যানার' ট্যাবে পেস্ট করে পুনরায় পরীক্ষা করুন।"
            )
            else -> listOf(
                "স্থানীয় নির্ভরযোগ্য ফতোয়া বোর্ডের সাথে আপনার পরিস্থিতি নিয়ে বিস্তারিত কথা বলুন।",
                "তাকওয়ার দিকটিকে অগ্রাধিকার দিন।"
            )
        }

        val principles = listOf(
            "কুরআনিক মূলনীতি: আল্লাহ ব্যবসাকে হালাল করেছেন এবং সুদকে হারাম করেছেন (আল-বাক্বারাহ: ২৭৫)।",
            "হাদীসের মূলনীতি: যে সম্পদে মালিকানা ও ঝুঁকি নেই, তা বিক্রি করা নিষিদ্ধ (সুনান আত-তিরমিজী: ১২৩২)।",
            "ফিকহি মূলনীতি: ঋণের বিনিময়ে যেকোনো পূর্বশর্তযুক্ত অতিরিক্ত অর্থ সর্বসম্মতভাবে সুদ (কাওয়াইদুল ফিকহ)।",
            "মুআমালাতের মূলনীতি: লেনদেনে ধোঁয়াশা বা এমন অজ্ঞতা নিষিদ্ধ যা বিরোধ তৈরি করে (সহীহ মুসলিম: ১৫১৩)।"
        )

        val sourcesList = listOf(
            "পবিত্র কুরআন: সূরা আল-বাক্বারাহ (২:২৭৫, ২৭৮, ২৮০, ২৮২), সূরা আন-নিসা (৪:২৯), সূরা আল-মায়িদাহ (৫:২)",
            "সহীহ বুখারী: কিতাবুল বুয়ূ (হা/২০৭৯, ২২৪০)",
            "সহীহ মুসলিম: কিতাবুল মুসাকাত ও রিবাহ (হা/১৫৯৮, ১৫১৩, ১৫৮৭)",
            "আন্তর্জাতিক ফিকহ একাডেমি (OIC-IFA): রেজ্যুলেশন নং ৬৩ ও কমার্শিয়াল ইন্স্যুরেন্স রুলিং",
            "আয়াওফি (AAOIFI) শরীয়াহ স্ট্যান্ডার্ডস: স্ট্যান্ডার্ড নং ১ (সার্ফ/কারেন্সি), ৮ (মুরাবাহা), ২১ (পুঁজিবাজার)"
        )

        return AskBeforeYouActReport(
            id = "rep_${System.currentTimeMillis()}",
            query = userQuery.ifBlank { scenario.sampleQuery },
            matchedScenarioTitleBn = scenario.titleBn,
            whatIUnderstandBn = whatIUnderstand,
            importantFacts = importantFacts,
            assessment = assessmentStatus,
            assessmentSummaryBn = assessmentSummary,
            relevantIslamicPrinciples = principles,
            quranProofs = scenario.quranProofs,
            hadithProofs = scenario.hadithProofs,
            fiqhMaxims = scenario.fiqhMaxims,
            scholarlyPositions = scenario.scholarlyPositions,
            whatIsStillUnclear = whatIsStillUnclear,
            practicalNextSteps = practicalNextSteps,
            halalAlternatives = scenario.halalAlternatives,
            clauseAnalyses = documentClauses,
            selectedOptionLabels = selectedOptionLabels,
            authenticSourcesList = sourcesList,
            isAiGenerated = false
        )
    }
}
