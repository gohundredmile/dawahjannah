package com.example.data.datasource

import com.example.data.model.DuaConstructionMode
import com.example.data.model.DuaEtiquetteItem
import com.example.data.model.DuaSourceMapItem
import com.example.data.model.DuaSourceType
import com.example.data.model.PermissibleSupplicationItem
import com.example.data.model.PersonalDuaBlueprint
import com.example.data.model.PersonalDuaScenarioPreset
import com.example.data.model.PropheticDuaItem
import com.example.data.model.QuranicDuaItem
import com.example.data.model.ScholarlyOpinionItem

/**
 * Authentic, verified Islamic knowledge base for "Dua Architect — Personal Dua Builder".
 * Grounded exclusively in the Holy Qur'an and Sahih Hadith.
 * Zero fabricated, weak, or unverified narrations.
 */
object PersonalDuaCatalog {

    val presets: List<PersonalDuaScenarioPreset> = listOf(
        PersonalDuaScenarioPreset(
            id = "parent_illness",
            titleBn = "পিতা-মাতার শেফা ও যত্ন",
            titleEn = "Parents Illness & Care",
            samplePrompt = "My father is sick and I am worried.",
            iconCategory = "health",
            badgeBn = "সিগনেচার",
            keywords = listOf(
                "father", "mother", "parent", "parents", "sick", "ill", "hospital", "worried", "worry", "care", "baba", "maa",
                "বাবা", "মা", "পিতা", "মাতা", "অসুস্থ", "অসুখ", "হাসপাতাল", "চিন্তিত", "শেফা", "আরোগ্য", "কষ্ট"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "future_worry",
            titleBn = "ভবিষ্যতের দুশ্চিন্তা ও তাওয়াক্কুল",
            titleEn = "Worried About Future",
            samplePrompt = "I am worried about my future.",
            iconCategory = "future",
            badgeBn = "তাওয়াক্কুল",
            keywords = listOf(
                "future", "worried about future", "career", "destiny", "anxious about future", "uncertain", "plan",
                "ভবিষ্যত", "ভবিষ্যৎ", "উদ্বেগ", "অনিশ্চয়তা", "আগামী", "ভবিষ্যত নিয়ে চিন্তিত", "ভাগ্যের ভয়"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "studies_exams",
            titleBn = "পড়াশোনা, মেধা ও পরীক্ষা",
            titleEn = "Studies & Exam Success",
            samplePrompt = "Make a Dua for success in my exam.",
            iconCategory = "education",
            badgeBn = "সাফল্য",
            keywords = listOf(
                "study", "studies", "exam", "test", "interview", "knowledge", "memory", "forget", "nervous", "result",
                "পড়াশোনা", "পরীক্ষা", "মেধা", "স্মৃতিশক্তি", "ইন্টারভিউ", "ভুলে যাওয়া", "রেজাল্ট", "জ্ঞান বৃদ্ধি"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "job_unemployment",
            titleBn = "চাকরি সন্ধান ও হালাল জীবিকা",
            titleEn = "Job & Employment",
            samplePrompt = "I am looking for a job and worried about provision.",
            iconCategory = "job",
            badgeBn = "কর্মসংস্থান",
            keywords = listOf(
                "job", "unemployed", "unemployment", "work", "career", "interview", "provision", "earn", "salary",
                "চাকরি", "বেকার", "কর্মসংস্থান", "রোজগার", "বেতন", "কাজ", "ব্যবসা", "কর্মহীন", "রিজিক"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "debt_finance",
            titleBn = "ঋণমুক্তি ও চরম আর্থিক সংকট",
            titleEn = "Financial Difficulty & Debt",
            samplePrompt = "Make a Dua for financial difficulty.",
            iconCategory = "finance",
            badgeBn = "ঋণমুক্তি",
            keywords = listOf(
                "debt", "financial", "money", "loan", "poverty", "broke", "income", "rizq", "financial difficulty",
                "ঋণ", "ধার", "আর্থিক", "টাকা", "অভাব", "দারিদ্র্য", "সংকট", "কর্জ", "লোন"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "marriage_spouse",
            titleBn = "নেক জীবনসঙ্গী ও পবিত্র দাম্পত্য",
            titleEn = "Marriage & Righteous Spouse",
            samplePrompt = "I am getting married and looking for a righteous spouse.",
            iconCategory = "marriage",
            badgeBn = "দাম্পত্য",
            keywords = listOf(
                "marriage", "getting married", "spouse", "wife", "husband", "nikah", "wedding", "partner",
                "বিয়ে", "বিবাহ", "স্বামী", "স্ত্রী", "জীবনসঙ্গী", "দাম্পত্য", "নিকাহ", "সংসার"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "forgiveness_repentance",
            titleBn = "পাপের অনুশোচনা ও ক্ষমা প্রার্থনা",
            titleEn = "Forgiveness of Sins",
            samplePrompt = "I want Allah to forgive my sins.",
            iconCategory = "forgiveness",
            badgeBn = "মাগফিরাত",
            keywords = listOf(
                "forgive", "forgiveness", "sin", "sins", "guilt", "guilty", "repent", "repentance", "tawbah",
                "পাপ", "গুনাহ", "ক্ষমা", "অনুশোচনা", "মাগফিরাত", "তাওবা", "ভুল", "অন্যায়", "ইস্তিগফার"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "anxiety_mental",
            titleBn = "মানসিক উদ্বেগ ও অন্তরের শান্তি",
            titleEn = "Anxiety & Peace of Heart",
            samplePrompt = "I feel anxious and my heart feels heavy.",
            iconCategory = "mental",
            badgeBn = "প্রশান্তি",
            keywords = listOf(
                "anxious", "anxiety", "depressed", "depression", "sad", "sadness", "stress", "panic", "heavy heart", "worry",
                "উদ্বেগ", "অস্থিরতা", "মানসিক", "কষ্ট", "হতাশা", "বিষণ্ণতা", "ভয়", "ভারাক্রান্ত", "শান্তি"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "family_peace",
            titleBn = "পারিবারিক বন্ধন ও হেদায়েত",
            titleEn = "Family Guidance & Peace",
            samplePrompt = "Make a Dua for my family.",
            iconCategory = "family",
            badgeBn = "পরিবার",
            keywords = listOf(
                "family", "household", "home", "relatives", "brother", "sister", "peace in family",
                "পরিবার", "পরিবারের", "সংসার", "ভাই", "বোন", "আত্মীয়", "শান্তি", "একতা"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "guidance_direction",
            titleBn = "হিদায়াত, দিশা ও সঠিক সিদ্ধান্ত",
            titleEn = "Guidance & Direction",
            samplePrompt = "I want guidance.",
            iconCategory = "guidance",
            badgeBn = "হিদায়াত",
            keywords = listOf(
                "guidance", "guide", "confused", "direction", "right path", "decision", "istikhara",
                "হিদায়াত", "হেদায়েত", "সঠিক পথ", "সিদ্ধান্ত", "দ্বিধাদ্বন্দ্ব", "দিশা", "আলো"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "difficult_trial",
            titleBn = "কঠিন পরিস্থিতি ও সবর",
            titleEn = "Difficult Time & Patience",
            samplePrompt = "I am going through a difficult time.",
            iconCategory = "hardship",
            badgeBn = "ধৈর্য",
            keywords = listOf(
                "difficult time", "hardship", "calamity", "trial", "pain", "struggle", "patience", "sabr",
                "কঠিন সময়", "বিপদ", "মুসিবত", "ধৈর্য", "সবর", "কষ্টের দিন", "পরীক্ষা", "সংকট"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "righteous_children",
            titleBn = "নেক সন্তান লাভ ও গর্ভধারণ",
            titleEn = "Righteous Children & Pregnancy",
            samplePrompt = "Praying for a righteous child and safe pregnancy.",
            iconCategory = "children",
            badgeBn = "সন্তান",
            keywords = listOf(
                "child", "children", "baby", "pregnant", "pregnancy", "infertility", "son", "daughter",
                "সন্তান", "বাচ্চা", "গর্ভবতী", "গর্ভধারণ", "নেক সন্তান", "ছেলে", "মেয়ে"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "injustice_protection",
            titleBn = "অন্যায়, জুলুম ও শত্রু থেকে হেফাজত",
            titleEn = "Protection from Injustice & Harm",
            samplePrompt = "Someone is doing injustice to me and I need protection.",
            iconCategory = "protection",
            badgeBn = "নিরাপত্তা",
            keywords = listOf(
                "injustice", "oppression", "enemy", "protection", "evil", "harm", "court", "oppressed",
                "জুলুম", "অত্যাচার", "শত্রু", "হেফাজত", "নিরাপত্তা", "মামলা", "অসহায়", "অন্যায়"
            )
        ),
        PersonalDuaScenarioPreset(
            id = "gratitude_blessings",
            titleBn = "আল্লাহর নিয়ামতের শুকরিয়া ও অবিচলতা",
            titleEn = "Gratitude for Blessings",
            samplePrompt = "I want to thank Allah for His blessings.",
            iconCategory = "gratitude",
            badgeBn = "শুকরিয়া",
            keywords = listOf(
                "thank", "gratitude", "blessing", "blessings", "praise", "alhamdulillah", "grateful",
                "শুকরিয়া", "ধন্যবাদ", "নিয়ামত", "আলহামদুলিল্লাহ", "কৃতজ্ঞতা", "প্রশংসা"
            )
        )
    )

    // Pre-compiled blueprints ensuring 100% authentic Quran & Sahih Hadith grounding
    val blueprints: Map<String, PersonalDuaBlueprint> = mapOf(

        // ==========================================
        // 1. PARENTS ILLNESS & CARE
        // ==========================================
        "parent_illness" to PersonalDuaBlueprint(
            id = "parent_illness",
            userQuery = "My father is sick and I am worried.",
            scenarioTitleBn = "পিতা-মাতার অসুস্থতা, আরোগ্য ও অন্তরের দুশ্চিন্তা নিবারণ",
            scenarioTitleEn = "Parents Illness, Healing & Alleviating Worry",
            spiritualComfortBn = "পিতা-মাতার অসুস্থতা সন্তানের হৃদয়ের জন্য এক গভীর পরীক্ষা। মুমিনের শরীরে রোগের সামান্যতম কষ্টও গুনাহ মাফ ও মর্যাদা বৃদ্ধির কারণ। রাসুলুল্লাহ ﷺ বলেছেন: 'মুমিনের গায়ে একটি কাঁটার সামান্য আঁচড় লাগলেও তার বিনিময়ে আল্লাহ তার পাপ ক্ষমা করে দেন' (সহীহ বুখারী ৫৬৪১)। অতএব, হতাশ না হয়ে আল্লাহর কাছে অশ্রুসজল দো'আ এবং সর্বাত্মক চিকিৎসা সেবাকে ইবাদত হিসেবে গ্রহণ করুন।",
            spiritualComfortEn = "The illness of parents is a profound trial of love and service. In Islam, serving sick parents and supplicating for them is among the greatest deeds. The Prophet ﷺ said: 'No fatigue, illness, anxiety, or sadness touches a believer, even the prick of a thorn, except that Allah expiates some of his sins by it' (Sahih al-Bukhari 5641). Combine sincere prayer with the best medical care.",
            detectedIntentBn = "পিতা-মাতার রোগমুক্তি ও সেবা",
            detectedIntentEn = "Parents' Health, Shifa & Well-being",
            emotionalContextBn = "গভীর দুশ্চিন্তা, ভালোবাসা ও রহমতের আকুতি",
            emotionalContextEn = "Deep anxiety, compassion and filial love",
            peopleInvolvedBn = "পিতা-মাতা ও সন্তান",
            peopleInvolvedEn = "Parents & Child",
            curatedArabicText = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لاَ شِفَاءَ إِلاَّ شِفَاؤُكَ، شِفَاءً لاَ يُغَادِرُ سَقَمًا ۝ رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
            curatedTranslationBn = "হে আল্লাহ! মানুষের প্রতিপালক! আপনি এ রোগ-কষ্ট দূর করে দিন এবং আরোগ্য দান করুন; আপনিই একমাত্র আরোগ্যকারী। আপনার আরোগ্য ব্যতীত অন্য কোনো আরোগ্য নেই—এমন আরোগ্য দিন যা কোনো রোগ অবশিষ্ট রাখে না। হে আমার প্রতিপালক! তাঁদের উভয়ের প্রতি দয়া করুন, যেমনটি তাঁরা শৈশবে আমাকে স্নেহ-মমতায় লালন-পালন করেছিলেন।",
            curatedTranslationEn = "O Allah, Lord of mankind, remove the hardship and cure him, for You are the Healer. There is no healing except Your healing, a cure that leaves behind no ailment. My Lord, have mercy upon them both as they brought me up when I was small.",
            whySelectedBn = "পিতা-মাতার রোগমুক্তির জন্য রাসুলুল্লাহ ﷺ-এর সুন্নাতী শেফার দু'আ (সহীহ বুখারী ৫৭৪৩) এবং কুরআনে কারীমে পিতা-মাতার প্রতি রহমত প্রার্থনার মৌলিক আয়াত (সূরা বনী ইসরাঈল ১৭:২৪) একত্রিত করা হয়েছে।",
            whySelectedEn = "Selected the authentic Prophetic supplication for illness (Sahih al-Bukhari 5743) combined with the Quranic verse of mercy for parents (Surah Al-Isra 17:24).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_parent_1",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "রোগীর জন্য রাসুলুল্লাহ ﷺ-এর শিফার দো'আ",
                    titleEn = "Prophetic Prayer for Healing",
                    referenceText = "সহীহ আল-বুখারী ৫৭৪৩, সহীহ মুসলিম ২১৯১",
                    hadithBookBn = "সহীহ আল-বুখারী ও সহীহ মুসলিম",
                    hadithBookEn = "Sahih al-Bukhari & Sahih Muslim",
                    hadithNumber = "বুখারী ৫৭৪৩, মুসলিম ২১৯১",
                    narratorCompanionBn = "উম্মুল মুমিনীন আয়েশা (রা.)",
                    gradingBn = "মুত্তাফাকুন আলাইহ (সর্বোচ্চ সহীহ)",
                    arabicSourceText = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لاَ شِفَاءَ إِلاَّ شِفَاؤُكَ، شِفَاءً لاَ يُغَادِرُ سَقَمًا",
                    translationBn = "হে আল্লাহ! মানুষের প্রতিপালক! আপনি এ রোগ-কষ্ট দূর করে দিন এবং আরোগ্য দান করুন; আপনিই একমাত্র আরোগ্যকারী। আপনার আরোগ্য ব্যতীত অন্য কোনো আরোগ্য নেই—এমন আরোগ্য দিন যা কোনো রোগ অবশিষ্ট রাখে না।",
                    translationEn = "O Allah, Lord of mankind, remove the suffering; heal him, for You are the Healer. There is no cure except Your cure, a cure that leaves no illness.",
                    pronunciationBn = "আল্লাহুম্মা রব্বান-নাসি আযহিবিল বা'স, ইশফিহি ওয়া আনতাশ-শাফী, লা শিফা-আ ইল্লা শিফা-উকা, শিফা-আন লা ইয়ুগাদিরু সাক্বমা।",
                    sunnahPracticeMethodBn = "রোগীর কপালে বা ব্যথাস্থানে ডান হাত রেখে দো'আটি পাঠ করা সুন্নাত।"
                ),
                DuaSourceMapItem(
                    id = "src_parent_2",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "পিতা-মাতার প্রতি রহমতের কুরআনী দো'আ",
                    titleEn = "Quranic Prayer for Parents",
                    referenceText = "আল-কুরআন, সূরা আল-ইসরা (১৭:২৪)",
                    surahNameBn = "বনী ইসরাঈল (আল-ইসরা)",
                    surahNameEn = "Al-Isra",
                    surahNameAr = "الإسراء",
                    surahNumber = 17,
                    ayahNumber = 24,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                    translationBn = "হে আমার প্রতিপালক! তাঁদের উভয়ের প্রতি দয়া করুন, যেমনটি তাঁরা শৈশবে আমাকে স্নেহ-মমতায় লালন-পালন করেছিলেন।",
                    translationEn = "My Lord, have mercy upon them as they brought me up when I was small.",
                    pronunciationBn = "রব্বির হামহুমা কামা রব্বায়ানী সগীরা।",
                    tafsirContextBn = "শৈশবে পিতা-মাতা যেভাবে প্রতিটি কষ্ট সহ্য করে লালন করেছেন, বার্ধক্যে ও অসুস্থতায় তাদের জন্য আল্লাহর রহমত চাওয়ার সর্বোত্তম আদেশ।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_parent_1",
                    surahNameBn = "বনী ইসরাঈল (আল-ইসরা)",
                    surahNameAr = "الإسراء",
                    surahNumber = 17,
                    ayahNumber = 24,
                    arabicText = "رَّبِّ ارْحَمْهُمَا كَمَا رَبَّيَانِي صَغِيرًا",
                    banglaPronunciation = "রব্বির হামহুমা কামা রব্বায়ানী সগীরা।",
                    banglaTranslation = "হে আমার প্রতিপালক! তাঁদের উভয়ের প্রতি দয়া করুন, যেমনটি তাঁরা শৈশবে আমাকে স্নেহ-মমতায় লালন-পালন করেছিলেন।",
                    englishTranslation = "My Lord, have mercy upon them as they brought me up when I was small.",
                    tafsirContextBn = "পিতা-মাতার শারীরিক দুর্বলতায় তাঁদের ওপর আল্লাহর বিশেষ দয়া ও রহমত চাওয়ার কুরআনী শিক্ষা।"
                ),
                QuranicDuaItem(
                    id = "q_parent_2",
                    surahNameBn = "ইবরাহীম",
                    surahNameAr = "إبراهيم",
                    surahNumber = 14,
                    ayahNumber = 41,
                    arabicText = "رَبَّنَا اغْفِرْ لِي وَلِوَالِدَيَّ وَلِلْمُؤْمِنِينَ يَوْمَ يَقُومُ الْحِسَابُ",
                    banglaPronunciation = "রব্বানাগ ফিরলী ওয়ালিওয়ালিদাইয়্যা ওয়ালিল মু'মিনীনা ইয়াওমা ইয়াকূমুল হিসাব।",
                    banglaTranslation = "হে আমাদের রব! যেদিন হিসাব প্রতিষ্ঠিত হবে, সেদিন আমাকে, আমার পিতা-মাতাকে এবং সমস্ত মুমিনকে ক্ষমা করে দিন।",
                    englishTranslation = "Our Lord, forgive me and my parents and the believers the Day the account is established.",
                    tafsirContextBn = "হযরত ইবরাহীম (আ.)-এর দো'আ। পিতা-মাতার মাগফিরাত ও আখেরাতের শান্তির জন্য অতি মর্যাদাপূর্ণ।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_parent_1",
                    hadithBookBn = "সহীহ আল-বুখারী ও সহীহ মুসলিম",
                    hadithBookEn = "Sahih al-Bukhari & Sahih Muslim",
                    hadithNumber = "বুখারী ৫৭৪৩, মুসলিম ২১৯১",
                    narratorCompanionBn = "উম্মুল মুমিনীন আয়েশা (রা.)",
                    gradingBn = "মুত্তাফাকুন আলাইহ (সর্বোচ্চ সহীহ)",
                    arabicText = "اللَّهُمَّ رَبَّ النَّاسِ أَذْهِبِ الْبَاسَ، اشْفِهِ وَأَنْتَ الشَّافِي، لاَ شِفَاءَ إِلاَّ شِفَاؤُكَ، شِفَاءً لاَ يُغَادِرُ সَقَمًا",
                    banglaPronunciation = "আল্লাহুম্মা রব্বান-নাসি আযহিবিল বা'স, ইশফিহি ওয়া আনতাশ-শাফী, লা শিফা-আ ইল্লা শিফা-উকা, শিফা-আন লা ইয়ুগাদিরু সাক্বমা।",
                    banglaTranslation = "হে আল্লাহ! মানুষের প্রতিপালক! আপনি এ রোগ-কষ্ট দূর করে দিন এবং আরোগ্য দান করুন; আপনিই একমাত্র আরোগ্যকারী। আপনার আরোগ্য ব্যতীত অন্য কোনো আরোগ্য নেই—এমন আরোগ্য দিন যা কোনো রোগ অবশিষ্ট রাখে না।",
                    englishTranslation = "O Allah, Lord of mankind, remove the suffering; heal him, for You are the Healer.",
                    sunnahPracticeMethodBn = "রোগীর কপালে বা ব্যথাস্থানে ডান হাত রেখে দো'আটি পাঠ করে হাত বুলানো সুন্নাত।",
                    occasionOfUsageBn = "অসুস্থ ব্যক্তিকে দেখতে গিয়ে বা সেবা করার সময়।"
                ),
                PropheticDuaItem(
                    id = "p_parent_2",
                    hadithBookBn = "সুনানে আবু দাউদ ও জামে তিরমিযী",
                    hadithBookEn = "Sunan Abi Dawud & Jami at-Tirmidhi",
                    hadithNumber = "আবু দাউদ ৩১০৬, তিরমিযী ২০৮৩",
                    narratorCompanionBn = "আবদুল্লাহ ইবনে আব্বাস (রা.)",
                    gradingBn = "সহীহ (Authentic)",
                    arabicText = "أَسْأَلُ اللَّهَ الْعَظِيمَ رَبَّ الْعَرْشِ الْعَظِيمِ أَنْ يَشْفِيَكَ",
                    banglaPronunciation = "আসআলুল্লাহাল আযীম, রব্বাল আরশিল আযীম, আঁই ইয়াশফিয়াক।",
                    banglaTranslation = "আমি মহান আল্লাহর কাছে প্রার্থনা করছি, যিনি মহা আরশের মালিক, তিনি যেন আপনাকে রোগমুক্তি দান করেন।",
                    englishTranslation = "I ask Allah Almighty, Lord of the Magnificent Throne, to heal you.",
                    sunnahPracticeMethodBn = "রাসুলুল্লাহ ﷺ বলেছেন: যে ব্যক্তি কোনো রোগীর কাছে ৭ বার এই দোয়া পাঠ করে, আল্লাহ তাকে আরোগ্য দান করেন।",
                    occasionOfUsageBn = "রোগীর পাশে বসে ৭ বার পাঠ্য।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_parent_1",
                    titleBn = "পিতার দ্রুত আরোগ্য ও দীর্ঘায়ুর জন্য বিনম্র ব্যক্তিগত আকুতি",
                    heartfeltSupplicationBn = "হে আল্লাহ! হে পরম স্নেহময় প্রতিপালক! আপনি তো জানেন আমার অন্তর আমার পিতার অসুস্থতায় কতটা ভারাক্রান্ত। যিনি আমাকে পরম স্নেহে আগলে রেখে বড় করেছেন, আজ তাঁর এই কষ্টে আপনি তাঁর সহায় হোন। হে শাফী! আপনার কুদরতের স্পর্শে তাঁর শরীর থেকে রোগ দূর করে দিন এবং তাঁকে নেক হায়াত দান করুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া শাফী (হে আরোগ্যকারী)", "ইয়া রাহমান (হে পরম দয়ালু)", "ইয়া আরহামার রাহিমীন")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_parent_1",
                    timingTitleBn = "তাহাজ্জুদের শেষ তৃতীয়াংশ ও সিজদারত অবস্থা",
                    timingDescriptionBn = "রাতের শেষ প্রহরে যখন আল্লাহ প্রথম আসমানে অবতরণ করে বান্দার ডাক শোনেন।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'প্রতি রাতের শেষ তৃতীয়াংশে আমাদের রব প্রথম আসমানে নেমে আসেন এবং বলেন—কে আমাকে ডাকবে আমি তার ডাকে সাড়া দেব?' (সহীহ বুখারী ১১৪৫)।",
                    practicalTipBn = "তাহাজ্জুদে নফল সালাতের সিজদায় পিতা-মাতার আরোগ্যের ফরিয়াদ জানান।"
                ),
                DuaEtiquetteItem(
                    id = "e_parent_2",
                    timingTitleBn = "রোগীর রোগমুক্তির নিয়তে গোপনে সদকা",
                    timingDescriptionBn = "অসুস্থ ব্যক্তির সুস্থতার জন্য গোপনে দান করা।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'তোমরা সদকা প্রদানের মাধ্যমে তোমাদের রোগীদের চিকিৎসা করো' (সহীহ আল-জামি ৩১৫১)।",
                    practicalTipBn = "পিতার সুস্থতার নিয়তে কোনো নিঃস্ব রোগীকে গোপনে ওষুধ বা অর্থ সাহায্য করে দো'আ করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "চিকিৎসা গ্রহণ বনাম তাওয়াক্কুল: ইসলামি শরীয়াহর অবস্থান",
                    dominantScholarlyPositionBn = "অভিজ্ঞ চিকিৎসকের কাছে চিকিৎসা নেওয়া ও ওষুধ সেবন করা তাওয়াক্কুলের পরিপন্থী নয়, বরং এটি স্বয়ং সুন্নাহ।",
                    supportingEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'হে আল্লাহর বান্দারা! তোমরা চিকিৎসা গ্রহণ করো। কেননা আল্লাহ এমন কোনো রোগ সৃষ্টি করেননি যার নিরাময় তিনি অবতীর্ণ করেননি' (সুনানে আবু দাউদ ৩৮৫৫)।",
                    consensusOrNuanceBn = "চিকিৎসা নেওয়া সুন্নাত ও কর্তব্য। শুধু দো'আ করে ওষুধ বর্জন করা শরীয়তসম্মত নয়।"
                )
            ),
            practicalRemindersBn = listOf(
                "ডাক্তারের পরামর্শ অনুযায়ী নিয়মিত ওষুধ ও পুষ্টিকর খাবার সেবন নিশ্চিত করুন।",
                "পিতা-মাতার শয্যাপাশে বসে ধৈর্য সহকারে তাঁদের সাথে কথা বলুন ও মানসিক সাহস দিন।",
                "তাহাজ্জুদে ও প্রতি ফরজ সালাতের পর তাঁদের মাগফিরাতের দো'আ জারি রাখুন।"
            )
        ),

        // ==========================================
        // 2. FUTURE WORRY & TAWAKKUL
        // ==========================================
        "future_worry" to PersonalDuaBlueprint(
            id = "future_worry",
            userQuery = "I am worried about my future.",
            scenarioTitleBn = "ভবিষ্যতের দুশ্চিন্তা নিবারণ, ভাগ্যের মঙ্গল ও পূর্ণ তাওয়াক্কুল",
            scenarioTitleEn = "Relieving Future Anxiety & Reliance on Allah",
            spiritualComfortBn = "ভবিষ্যতের সমস্ত কর্তৃত্ব একমাত্র পরম করুণাময় আল্লাহর হাতে। আপনি আগামীকাল কোথায় থাকবেন এবং কী অর্জন করবেন, তার কল্যাণ একমাত্র তিনিই নির্ধারণ করেন। আল্লাহ তা'আলা বলেন: 'যে ব্যক্তি আল্লাহর ওপর তাওয়াক্কুল বা ভরসা করে, তার জন্য আল্লাহই যথেষ্ট' (সূরা আত-তালাক ৬৫:৩)। অতীত নিয়ে আক্ষেপ আর ভবিষ্যৎ নিয়ে আতঙ্ক—এ দুটোই শয়তানের প্ররোচনা। আজ আপনার সাধ্যমতো চেষ্টা করুন এবং ফলাফল আল্লাহর হাতে অর্পণ করে হৃদয়ে প্রশান্তি আনুন।",
            spiritualComfortEn = "The future belongs entirely to Allah, the All-Wise. He says: 'And whoever relies upon Allah - then He is sufficient for him' (Surah At-Talaq 65:3). Do your utmost today and place your trust in the One who manages the universe.",
            detectedIntentBn = "ভবিষ্যতের দুশ্চিন্তা দূর ও তাওয়াক্কুল অর্জন",
            detectedIntentEn = "Relieving Future Anxiety & Reliance on Allah",
            emotionalContextBn = "অনিশ্চয়তা, ভয় ও আশ্বাসের আকাঙ্ক্ষা",
            emotionalContextEn = "Uncertainty, fear of unknown & desire for peace",
            peopleInvolvedBn = "নিজের জীবন ও নিয়তি",
            peopleInvolvedEn = "Self & Destiny",
            curatedArabicText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ ۝ اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنَ الْخَيْرِ كُلِّهِ عَاجِلِهِ وَآجِلِهِ، مَا عَلِمْتُ مِنْهُ وَمَا لَمْ أَعْلَمْ",
            curatedTranslationBn = "আমার জন্য আল্লাহই যথেষ্ট, তিনি ছাড়া কোনো সত্য ইলাহ নেই। আমি তাঁরই ওপর ভরসা করেছি এবং তিনি মহান আরশের অধিপতি। হে আল্লাহ! নিশ্চয়ই আমি আপনার কাছে সমস্ত কল্যাণ প্রার্থনা করছি—তার তাৎক্ষণিক কল্যাণ ও ভবিষ্যতের কল্যাণ, যা আমি জানি এবং যা আমি জানি না।",
            curatedTranslationEn = "Sufficient for me is Allah; there is no deity except Him. On Him I have relied, and He is the Lord of the Great Throne. O Allah, I ask You for all good, both immediate and in the future, what I know of it and what I do not know.",
            whySelectedBn = "ভবিষ্যতের অনিশ্চয়তা দূর করতে সূরা আত-তাওবার শেষ আয়াতের সর্বজনীন তাওয়াক্কুল (৯:১২৯) এবং রাসুলুল্লাহ ﷺ-এর সর্বাঙ্গীন ভবিষ্যৎ কল্যাণ প্রার্থনার সহীহ দু'আ (ইবনে মাজাহ ৩৮৪৬) নির্বাচিত হয়েছে।",
            whySelectedEn = "Combines the ultimate verse of reliance from Surah At-Tawbah (9:129) with the comprehensive Prophetic supplication for future good (Sunan Ibn Majah 3846).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_future_1",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "পরম তাওয়াক্কুল ও নিরাপত্তার কুরআনী ঘোষণা",
                    titleEn = "Quranic Declaration of Reliance",
                    referenceText = "আল-কুরআন, সূরা আত-তাওবাহ (৯:১২৯)",
                    surahNameBn = "আত-তাওবাহ",
                    surahNameEn = "At-Tawbah",
                    surahNameAr = "التوبة",
                    surahNumber = 9,
                    ayahNumber = 129,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
                    translationBn = "আমার জন্য আল্লাহই যথেষ্ট, তিনি ছাড়া কোনো সত্য ইলাহ নেই। আমি তাঁরই ওপর ভরসা করেছি এবং তিনি মহান আরশের অধিপতি।",
                    translationEn = "Sufficient for me is Allah; there is no deity except Him. On Him I have relied, and He is the Lord of the Great Throne.",
                    pronunciationBn = "হাসবিয়াল্লাহু লা ইলাহা ইল্লা হুওয়া আলাইহি তাওয়াক্কালতু ওয়া হুয়া রব্বুল আরশিল আযীম।",
                    tafsirContextBn = "সকাল ও সন্ধ্যায় ৭ বার পাঠ করলে আল্লাহ বান্দার সমস্ত দুশ্চিন্তা ও ভবিষ্যতের পেরেশানির দায়িত্ব গ্রহণ করেন (আবু দাউদ ৫০৮১)।"
                ),
                DuaSourceMapItem(
                    id = "src_future_2",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "ভবিষ্যতের সর্বাঙ্গীন কল্যাণ ও অনিষ্টমুক্তির জামে' দু'আ",
                    titleEn = "Comprehensive Prayer for Future Good",
                    referenceText = "সুনানে ইবনে মাজাহ ৩৮৪৬, মুসনাদে আহমাদ ২৫০১৯",
                    hadithBookBn = "সুনানে ইবনে মাজাহ ও মুসনাদে আহমাদ",
                    hadithBookEn = "Sunan Ibn Majah & Musnad Ahmad",
                    hadithNumber = "ইবনে মাজাহ ৩৮৪৬",
                    narratorCompanionBn = "উম্মুল মুমিনীন আয়েশা (রা.)",
                    gradingBn = "সহীহ (আলবানী সহীহ বলেছেন)",
                    arabicSourceText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ مِنَ الْخَيْرِ كُلِّهِ عَاجِلِهِ وَآجِلِهِ، مَا عَلِمْتُ مِنْهُ وَمَا لَمْ أَعْلَمْ، وَأَعُوذُ بِكَ مِنَ الشَّرِّ كُلِّهِ عَاجِلِهِ وَآجِلِهِ، مَا عَلِمْتُ مِنْهُ وَمَا لَمْ أَعْلَمْ",
                    translationBn = "হে আল্লাহ! আমি আপনার কাছে সমস্ত কল্যাণ চাই—বর্তমান ও ভবিষ্যতের, যা আমি জানি আর যা আমি জানি না। এবং আমি আপনার আশ্রয় চাই সমস্ত অকল্যাণ থেকে—বর্তমান ও ভবিষ্যতের, যা আমি জানি আর যা আমি জানি না।",
                    translationEn = "O Allah, I ask You for all good, immediate and future, what I know and what I do not know; and I seek refuge in You from all evil, immediate and future.",
                    pronunciationBn = "আল্লাহুম্মা ইন্নী আসআলুকা মিনাল খাইরি কুল্লিহী 'আজি lihী ওয়া আজিলিহী, মা 'আলিমতু মিনহু ওয়ামা লাম আ'লাম...",
                    sunnahPracticeMethodBn = "রাসুলুল্লাহ ﷺ উম্মুল মুমিনীন আয়েশা (রা.)-কে এই সর্বব্যাপী দু'আটি নিয়মিত পাঠের নির্দেশ দিয়েছিলেন।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_future_1",
                    surahNameBn = "আত-তাওবাহ",
                    surahNameAr = "التوبة",
                    surahNumber = 9,
                    ayahNumber = 129,
                    arabicText = "حَسْبِيَ اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ ۖ عَلَيْهِ تَوَكَّلْتُ ۖ وَهُوَ رَبُّ الْعَرْشِ الْعَظِيمِ",
                    banglaPronunciation = "হাসবিয়াল্লাহু লা ইলাহা ইল্লা হুওয়া আলাইহি তাওয়াক্কালতু ওয়া হুয়া রব্বুল আরশিল আযীম।",
                    banglaTranslation = "আমার জন্য আল্লাহই যথেষ্ট, তিনি ছাড়া কোনো সত্য ইলাহ নেই। আমি তাঁরই ওপর ভরসা করেছি এবং তিনি মহান আরশের অধিপতি।",
                    englishTranslation = "Sufficient for me is Allah; there is no deity except Him. On Him I have relied, and He is the Lord of the Great Throne.",
                    tafsirContextBn = "ভবিষ্যতের সমস্ত দায়িত্ব আল্লাহর হাতে সঁপে দেওয়ার মহা ঘোষণা।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_future_1",
                    hadithBookBn = "সুনানে আবু দাউদ ও জামে তিরমিযী",
                    hadithBookEn = "Sunan Abi Dawud & Jami at-Tirmidhi",
                    hadithNumber = "আবু দাউদ ১৫৫১, তিরমিযী ৩৪৮৪",
                    narratorCompanionBn = "আনাস ইবনে মালিক (রা.)",
                    gradingBn = "সহীহ (Authentic)",
                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
                    banglaPronunciation = "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিনাল হাম্মি ওয়াল হাযানি, ওয়াল 'আজযি ওয়াল কাসালি, ওয়াল বুখলি ওয়াল জুবনি, ওয়া দ্বলা'ইদ-দাইনি ওয়া গলাবাতির-রিজাল।",
                    banglaTranslation = "হে আল্লাহ! আমি আপনার আশ্রয় নিচ্ছি দুশ্চিন্তা ও দুঃখ-বেদনা থেকে, অক্ষমতা ও অলসতা থেকে, কৃপণতা ও কাপুরুষতা থেকে, ঋণের বোঝা ও মানুষের পরাভব থেকে।",
                    englishTranslation = "O Allah, I seek refuge in You from grief and anxiety, from weakness and laziness, from miserliness and cowardice, and from being overcome by debt and by men.",
                    sunnahPracticeMethodBn = "রাসুলুল্লাহ ﷺ সকাল ও সন্ধ্যায় এবং সালাতের শেষ বৈঠকে এই দু'আ সর্বাধিক পাঠ করতেন।",
                    occasionOfUsageBn = "ভবিষ্যতের আতঙ্ক ও মানসিক দুশ্চিন্তা দূর করতে।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_future_1",
                    titleBn = "আগামীর কল্যাণ ও রবের ফয়সালায় সন্তুষ্টির আকুতি",
                    heartfeltSupplicationBn = "হে আল্লাহ! আমার ভবিষ্যৎ আপনার কুদরতি হাতে। আপনি আমাকে সেই পথে পরিচালিত করুন যাতে আপনার সন্তুষ্টি রয়েছে। আমার অন্তরে রিযা বা সন্তুষ্টি দান করুন, যেন আপনার প্রতিটি তাকদীরে আমি কৃতজ্ঞ থাকতে পারি।",
                    invokedNamesOfAllahBn = listOf("ইয়া হালীম (হে পরম ধৈর্যশীল)", "ইয়া ফাত্তাহ (হে পথ উন্মোচনকারী)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_future_1",
                    timingTitleBn = "সকাল ও সন্ধ্যায় ফজর ও মাগরিবের পর",
                    timingDescriptionBn = "প্রতিদিন সকাল ও সন্ধ্যায় আগামী দিনের সুরক্ষা প্রার্থনার সোনালী সময়।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'যে ব্যক্তি সকাল ও সন্ধ্যায় ৭ বার পড়বে—হাসবিয়াল্লাহু লা ইলাহা ইল্লা হুওয়া আলাইহি তাওয়াক্কালতু... আল্লাহ তার দুনিয়া ও আখেরাতের সমস্ত দুশ্চিন্তা দূর করে দেবেন' (সুনানে আবু দাউদ ৫০৮১)।",
                    practicalTipBn = "প্রতিদিন ফজর ও মাগরিবের পর ৭ বার দো'আটি একনিষ্ঠভাবে পাঠ করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "তাকদীরের ওপর বিশ্বাস বনাম কর্মপ্রচেষ্টা",
                    dominantScholarlyPositionBn = "ভবিষ্যৎ নির্ধারিত হলেও চেষ্টা করা বান্দার ফরজ দায়িত্ব। চেষ্টা ত্যাগ করা তাওয়াক্কুল নয় বরং মূর্খতা।",
                    supportingEvidenceBn = "এক ব্যক্তি জিজ্ঞেস করল: 'আমি কি উট বেঁধে তাওয়াক্কুল করব নাকি ছেড়ে দিয়ে?' রাসুল ﷺ বললেন: 'আগে উট বাঁধো, তারপর আল্লাহর ওপর তাওয়াক্কুল করো' (জামে তিরমিযী ২৫১৭)।",
                    consensusOrNuanceBn = "মেধা ও পরিকল্পনার সর্বোচ্চ ব্যবহার করবেন, কিন্তু ভরসা রাখবেন একমাত্র আল্লাহর কুদরতের ওপর।"
                )
            ),
            practicalRemindersBn = listOf(
                "প্রতিদিনের কাজকে ছোট ছোট ভাগে ভাগ করে আজকেই সম্পন্ন করুন।",
                "অজানা ভবিষ্যত নিয়ে ভেবে সময় নষ্ট না করে বর্তমান সময়ের ইবাদত ও দায়িত্বে মনোযোগ দিন।",
                "ইস্তিখারার সালাতের মাধ্যমে যেকোনো বড় সিদ্ধান্ত নেওয়ার অভ্যাস গড়ে তুলুন।"
            )
        ),

        // ==========================================
        // 3. STUDIES, EXAMS & KNOWLEDGE
        // ==========================================
        "studies_exams" to PersonalDuaBlueprint(
            id = "studies_exams",
            userQuery = "Make a Dua for success in my exam.",
            scenarioTitleBn = "পড়াশোনায় মনোযোগ, মেধা বৃদ্ধি ও পরীক্ষায় কৃতকার্য হওয়া",
            scenarioTitleEn = "Knowledge, Memory & Exam Success",
            spiritualComfortBn = "জ্ঞান অর্জন ইসলামের অন্যতম শ্রেষ্ঠ ইবাদত। আল্লাহ তা'আলা রাসুলুল্লাহ ﷺ-কে দুনিয়ার কোনো সম্পদ বৃদ্ধির দো'আ করতে বলেননি, কেবল ইলম বা জ্ঞান বৃদ্ধির দো'আ শিখিয়েছেন: 'রাব্বি যিদনী ইলমা'। পরীক্ষার হলের ভীতি বা ভুলে যাওয়ার সংশয় দূর করতে আল্লাহর ওপর ভরসা রাখুন। মনে রাখবেন, মেধা ও স্মরণশক্তি সবই আল্লাহর নিয়ামত। নিষ্ঠার সাথে পরিশ্রম করুন এবং আল্লাহ আপনার মেহনতকে কখনো ব্যর্থ করবেন না।",
            spiritualComfortEn = "Seeking knowledge is an act of worship. Allah commanded the Prophet ﷺ to seek an increase in knowledge above all else. Study with diligence, purify your intentions, and rely upon Allah for mental clarity and success.",
            detectedIntentBn = "পড়াশোনায় একাগ্রতা, স্মৃতিশক্তি ও পরীক্ষায় সাফল্য",
            detectedIntentEn = "Memory retention, focus and exam success",
            emotionalContextBn = "পরীক্ষার ভীতি, নার্ভাসনেস ও মেধা বৃদ্ধির আকুতি",
            emotionalContextEn = "Exam anxiety, nervousness and seeking cognitive clarity",
            peopleInvolvedBn = "শিক্ষার্থী ও পরীক্ষার্থী",
            peopleInvolvedEn = "Student / Examinee",
            curatedArabicText = "رَبِّ اشْرَحْ لِي صَدْرِي ۝ وَيَسِّرْ لِي أَمْرِي ۝ وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي ۝ يَفْقَهُوا قَوْلِي ۝ رَّبِّ زِدْنِي عِلْمًا ۝ اللَّهُمَّ لاَ سَهْلَ إِلاَّ مَا جَعَلْتَهُ سَهْلاً، وَأَنْتَ تَجْعَلُ الْحَزْنَ إِذَا شِئْتَ سَهْلاً",
            curatedTranslationBn = "হে আমার প্রতিপালক! আমার বক্ষ প্রশস্ত করে দিন, আমার কাজ সহজ করে দিন, আমার জিহ্বার জড়তা দূর করে দিন যাতে তারা আমার কথা বুঝতে পারে। হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন। হে আল্লাহ! আপনি যা সহজ করেন তা ব্যতীত কোনো কিছুই সহজ নয়; আর আপনি চাইলে কঠিনকেও সহজ করে দেন।",
            curatedTranslationEn = "My Lord, expand for me my breast, and ease for me my task, and untie the knot from my tongue that they may understand my speech. My Lord, increase me in knowledge. O Allah, there is no ease except that which You make easy, and indeed You can make sorrow and difficulty easy if You will.",
            whySelectedBn = "মুসা (আ.)-এর মানসিক জড়তা দূর ও বক্ষ প্রশস্ত করার দো'আ (সূরা ত্ব-হা ২০:২৫-২৮), ইলম বৃদ্ধির কুরআনী দো'আ (সূরা ত্ব-হা ২০:১১৪) এবং কঠিন কাজ সহজ করার সহীহ নববী দো'আ (সহীহ ইবনে হিব্বান ৯৭৪) একত্রিত করা হয়েছে।",
            whySelectedEn = "Combines Prophet Musa's supplication for clarity (Surah Ta-Ha 20:25-28), the divine command to seek knowledge (Surah Ta-Ha 20:114), and the Prophetic prayer for overcoming difficulty (Sahih Ibn Hibban 974).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_study_1",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "বক্ষ প্রশস্ত ও কথা স্পষ্ট হওয়ার কুরআনী দো'আ",
                    titleEn = "Musa's Supplication for Clarity",
                    referenceText = "আল-কুরআন, সূরা ত্ব-হা (২০:২৫-২৮)",
                    surahNameBn = "ত্ব-হা",
                    surahNameEn = "Ta-Ha",
                    surahNameAr = "طه",
                    surahNumber = 20,
                    ayahNumber = 25,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "رَبِّ اشْرَحْ لِي صَدْرِي وَيَسِّرْ لِي أَمْرِي وَاحْلُلْ عُقْدَةً مِّن لِّسَانِي يَفْقَهُوا قَوْلِي",
                    translationBn = "হে আমার প্রতিপালক! আমার বক্ষ প্রশস্ত করে দিন, আমার কাজ সহজ করে দিন, এবং আমার জিহ্বার জড়তা দূর করে দিন যাতে তারা আমার কথা বুঝতে পারে।",
                    translationEn = "My Lord, expand for me my breast and ease for me my task and untie the knot from my tongue that they may understand my speech.",
                    pronunciationBn = "রব্বিশ রাহলী সদরী, ওয়া ইয়াসসির লী আমরী, ওয়াহলুল 'উকদাতাম মিল লিসানী, ইয়াফক্বাহূ ক্বওলী।",
                    tafsirContextBn = "পরীক্ষার হলে বা ভাইভায় জড়তা ও ভয় দূর করতে অত্যন্ত কার্যকরী।"
                ),
                DuaSourceMapItem(
                    id = "src_study_2",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "কঠিন কাজ সহজ হওয়ার সহীহ নববী দো'আ",
                    titleEn = "Prayer for Ease in Difficult Tasks",
                    referenceText = "সহীহ ইবনে হিব্বান ৯৭৪, আল-আদাবুল মুফরাদ ১৩৯",
                    hadithBookBn = "সহীহ ইবনে হিব্বান ও আল-আদাবুল মুফরাদ",
                    hadithBookEn = "Sahih Ibn Hibban & Al-Adab al-Mufrad",
                    hadithNumber = "ইবনে হিব্বান ৯৭৪",
                    narratorCompanionBn = "আনাস ইবনে মালিক (রা.)",
                    gradingBn = "সহীহ (শায়খ আলবানী সহীহ বলেছেন)",
                    arabicSourceText = "اللَّهُمَّ لاَ سَهْلَ إِلاَّ مَا جَعَلْتَهُ سَهْلاً، وَأَنْتَ تَجْعَلُ الْحَزْنَ إِذَا شِئْتَ سَهْلاً",
                    translationBn = "হে আল্লাহ! আপনি যা সহজ করেন তা ব্যতীত কোনো কিছুই সহজ নয়; আর আপনি চাইলে কঠিন বিষয়কেও সহজ করে দেন।",
                    translationEn = "O Allah, there is no ease except that which You make easy, and You can make difficulty easy if You will.",
                    pronunciationBn = "আল্লাহুম্মা লা সাহলা ইল্লা মা জা'আলতাহূ সাহলা, ওয়া আনতা তাজ'আলুল হাযনা ইযা শি'তা সাহলা।",
                    sunnahPracticeMethodBn = "পড়াশোনা শুরু করার পূর্বে বা পরীক্ষার প্রশ্নপত্র হাতে পেয়ে পাঠ্য।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_study_1",
                    surahNameBn = "ত্ব-হা",
                    surahNameAr = "طه",
                    surahNumber = 20,
                    ayahNumber = 114,
                    arabicText = "رَّبِّ زِدْنِي عِلْمًا",
                    banglaPronunciation = "রব্বি যিদনী 'ইলমা।",
                    banglaTranslation = "হে আমার রব! আমার জ্ঞান বৃদ্ধি করে দিন।",
                    englishTranslation = "My Lord, increase me in knowledge.",
                    tafsirContextBn = "মহান আল্লাহ তা'আলা বিশ্বনবী ﷺ-কে সরাসরি এই দো'আ করতে আদেশ করেছিলেন।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_study_1",
                    hadithBookBn = "সুনানে ইবনে মাজাহ",
                    hadithBookEn = "Sunan Ibn Majah",
                    hadithNumber = "হাদীস ৯২৫",
                    narratorCompanionBn = "উম্মে সালামাহ (রা.)",
                    gradingBn = "সহীহ (Authentic)",
                    arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ عِلْمًا نَافِعًا، وَرِزْقًا طَيِّبًا، وَعَمَلاً مُتَقَبَّلاً",
                    banglaPronunciation = "আল্লাহুম্মা ইন্নী আসআলুকা 'ইলমান নাফি'আ, ওয়া রিযক্বান ত্বইয়িবা, ওয়া 'আমালান মুতাক্বব্বালা।",
                    banglaTranslation = "হে আল্লাহ! নিশ্চয়ই আমি আপনার কাছে উপকারী জ্ঞান, পবিত্র জীবিকা এবং কবুলযোগ্য নেক আমল প্রার্থনা করছি।",
                    englishTranslation = "O Allah, I ask You for beneficial knowledge, good provision, and acceptable deeds.",
                    sunnahPracticeMethodBn = "রাসুলুল্লাহ ﷺ ফজরের সালাতের সালাম ফিরানোর পর প্রতিদিন এই দু'আ পাঠ করতেন।",
                    occasionOfUsageBn = "প্রতিদিন সকালে পড়াশোনা ও জ্ঞানার্জনের জন্য।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_study_1",
                    titleBn = "পরীক্ষায় মনোযোগ ও স্মৃতিশক্তি সুরক্ষার আন্তরিক আরজি",
                    heartfeltSupplicationBn = "হে আল্লাহ! হে সর্বজ্ঞানী রব! আমি যা পড়েছি তা প্রয়োজন মতো স্মরণ করিয়ে দিন এবং আমার মেধা ও স্মৃতিশক্তিকে কল্যাণকর ইলমে বরকত দান করুন। পরীক্ষার সময়ে আমার অন্তরকে শান্ত ও স্থির রাখুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া আলিম (হে সর্বজ্ঞানী)", "ইয়া ফাত্তাহ (হে উন্মোচনকারী)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_study_1",
                    timingTitleBn = "সালাতের সিজদা এবং পড়ার শুরুতে",
                    timingDescriptionBn = "সালাতের সিজদায় বিনম্রভাবে এবং পড়ার শুরুতে বিসমিল্লাহ সহ দো'আ করা।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'বান্দা সিজদারত অবস্থায় রবের সবচেয়ে নিকটবর্তী হয়, কাজেই তোমরা সিজদায় বেশি বেশি দু'আ করো' (সহীহ মুসলিম ৪৮২)।",
                    practicalTipBn = "পড়াশোনা শুরুর আগে দুই রাকাত নফল সালাত আদায় করে সিজদায় দো'আ করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "পরীক্ষার জন্য নির্দিষ্ট কোনো কাল্পনিক দু'আ আছে কি?",
                    dominantScholarlyPositionBn = "পরীক্ষার জন্য নির্দিষ্ট কোনো মনগড়া হাদিস নেই; বরং ইলম বৃদ্ধি, বক্ষ প্রশস্তকরণ ও কঠিন কাজ সহজ করার সাধারণ সহীহ দু'আগুলোই পাঠ্য।",
                    supportingEvidenceBn = "ফিকহী মূলনীতি: ইবাদত ও নির্দিষ্ট আমলের জন্য কুরআন ও সহীহ সুন্নাহর প্রামাণ্য দলিল থাকা অপরিহার্য।",
                    consensusOrNuanceBn = "ইন্টারনেটে প্রচলিত ভিত্তিহীন 'পরীক্ষার দোয়া' বর্জন করে সহীহ দোয়াগুলো পাঠ করুন।"
                )
            ),
            practicalRemindersBn = listOf(
                "পরীক্ষার আগের রাতে পর্যাপ্ত ঘুম ও মানসিক বিশ্রাম নিন।",
                "গুনাহ থেকে চোখ ও মনকে হেফাজত রাখুন, কারণ ইমাম শাফেয়ী (রহ.) বলেছেন: 'পাপ স্মৃতিশক্তি হ্রাস করে'।",
                "পরীক্ষার হলে পৌঁছানোর পর 'বিসমিল্লাহ' বলে আত্মবিশ্বাসের সাথে লেখা শুরু করুন।"
            )
        ),

        // ==========================================
        // 4. JOB & UNEMPLOYMENT
        // ==========================================
        "job_unemployment" to PersonalDuaBlueprint(
            id = "job_unemployment",
            userQuery = "I am looking for a job and worried about provision.",
            scenarioTitleBn = "হালাল কর্মসংস্থান, রিযিকের প্রাচুর্য ও বেকারত্ব থেকে মুক্তি",
            scenarioTitleEn = "Job, Employment & Halal Provision",
            spiritualComfortBn = "রিজিকের মালিক মানুষ বা কোনো প্রতিষ্ঠান নয়, রিজিকের মালিক একমাত্র আল্লাহ আস-সামাদ ও আর-রাযযাক্ব। আপনার জন্মের আগেই আল্লাহ আপনার নির্ধারিত রিযিক লিখে রেখেছেন। রাসুলুল্লাহ ﷺ বলেছেন: 'কোনো প্রাণী তার পূর্ণ রিযিক গ্রহণ না করা পর্যন্ত মৃত্যুবরণ করবে না; অতএব তোমরা আল্লাহকে ভয় করো এবং উত্তম ও হালাল উপায়ে তালাশ করো' (সহীহ ইবনে মাজাহ ২১৪৪)। বর্তমানের এই বেকারত্ব বা অপেক্ষা আপনার জন্য ধৈর্যের এক স্তর। হালাল চেষ্টা জারি রাখুন, নিশ্চয়ই আল্লাহ উত্তম ফয়সালাকারী।",
            spiritualComfortEn = "Provision is guaranteed by Allah, the Ultimate Provider (Ar-Razzaq). No soul will depart this world until it has fully received its decreed sustenance. Pursue every legitimate opportunity with trust that Allah opens the best doors.",
            detectedIntentBn = "হালাল কর্মসংস্থান ও রিজিকের বরকত লাভ",
            detectedIntentEn = "Halal employment, career opening & blessed provision",
            emotionalContextBn = "পরিবারের দায়িত্বের চাপ, দুশ্চিন্তা ও কর্মহীনতার কষ্ট",
            emotionalContextEn = "Weight of family duty, anxiety of unemployment & seeking self-reliance",
            peopleInvolvedBn = "চাকরিপ্রার্থী ও পরিবার",
            peopleInvolvedEn = "Job Seeker & Dependent Family",
            curatedArabicText = "رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ ۝ اللَّهُمَّ اكْفِنِي بِحَلاَلِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
            curatedTranslationBn = "হে আমার প্রতিপালক! আপনি আমার প্রতি যে অনুগ্রহই অবতীর্ণ করবেন, আমি তার চূড়ান্ত মুখাপেক্ষী। হে আল্লাহ! আমাকে আপনার হালাল রিজিক দ্বারা তৃপ্ত করে হারাম থেকে বাঁচিয়ে রাখুন এবং আপনার অনুগ্রহ দিয়ে আপনি ছাড়া অন্য সবার মুখাপেক্ষিতা থেকে আমাকে মুক্ত করে দিন।",
            curatedTranslationEn = "My Lord, indeed I am in need of whatever good You would send down to me. O Allah, make what is lawful enough for me, as opposed to what is unlawful, and spare me by Your grace from need of anyone besides You.",
            whySelectedBn = "হযরত মুসা (আ.) যখন সম্পূর্ণ নিঃস্ব ও কর্মহীন অবস্থায় আল্লাহর রহমত প্রার্থনা করেছিলেন সেই কুরআনী দো'আ (সূরা কাসাস ২৮:২৪) এবং স্বাবলম্বী ও হালাল জীবিকা অর্জনের বিখ্যাত সহীহ নববী দো'আ (তিরমিযী ৩৫৬৩) একত্রিত করা হয়েছে।",
            whySelectedEn = "Combines Prophet Musa's supplication when he was homeless and seeking work (Surah Al-Qasas 28:24) with the authentic Prophetic prayer for sufficiency through lawful means (Jami at-Tirmidhi 3563).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_job_1",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "মুসা (আ.)-এর আশ্রয়, কাজ ও অনুগ্রহ লাভের কুরআনী দো'আ",
                    titleEn = "Musa's Supplication for Work & Relief",
                    referenceText = "আল-কুরআন, সূরা আল-কাসাস (২৮:২৪)",
                    surahNameBn = "আল-কাসাস",
                    surahNameEn = "Al-Qasas",
                    surahNameAr = "القصص",
                    surahNumber = 28,
                    ayahNumber = 24,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ",
                    translationBn = "হে আমার প্রতিপালক! আপনি আমার প্রতি যে অনুগ্রহই অবতীর্ণ করবেন, আমি তার চূড়ান্ত মুখাপেক্ষী।",
                    translationEn = "My Lord, indeed I am, for whatever good You would send down to me, in need.",
                    pronunciationBn = "রব্বি ইন্নী লিমা- আনযালতা ইলাইয়্যা মিন খাইরিন ফাক্বীর।",
                    tafsirContextBn = "মুসা (আ.) যখন মাদইয়ানে নিঃস্ব অবস্থায় আশ্রয় ও জীবিকা ছাড়া ছিলেন, এই দো'আ করার পরপরই আল্লাহ তাঁকে সম্মানজনক কর্মসংস্থান ও বিবাহের ব্যবস্থা করে দেন।"
                ),
                DuaSourceMapItem(
                    id = "src_job_2",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "হালাল রিজিক ও স্বাবলম্বিতার সহীহ নববী দো'আ",
                    titleEn = "Prayer for Lawful Sufficiency",
                    referenceText = "জামে তিরমিযী ৩৫৬৩, মুসনাদে আহমাদ ১৩২১",
                    hadithBookBn = "জামে আত-তিরমিযী ও মুসনাদে আহমাদ",
                    hadithBookEn = "Jami at-Tirmidhi & Musnad Ahmad",
                    hadithNumber = "তিরমিযী ৩৫৬৩",
                    narratorCompanionBn = "আলী ইবনে আবি তালিব (রা.)",
                    gradingBn = "হাসান সহীহ (তিরমিযী)",
                    arabicSourceText = "اللَّهُمَّ اكْفِنِي بِحَلاَلِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
                    translationBn = "হে আল্লাহ! আমাকে আপনার হালাল রিজিক দ্বারা পরিতৃপ্ত রাখুন যাতে হারাম থেকে মুক্ত থাকতে পারি, এবং আপনার দয়া দিয়ে আপনি ছাড়া সবার মুখাপেক্ষিতা থেকে আমাকে অমুখাপেক্ষী করে দিন।",
                    translationEn = "O Allah, make what is lawful enough for me, as opposed to what is unlawful, and spare me by Your grace from need of anyone besides You.",
                    pronunciationBn = "আল্লাহুম্মাকফিনী বিহিলালিকা 'আন হারামিক, ওয়া আগনিনী বিফাদ্বলিকা 'আম্মান সিওয়াক।",
                    sunnahPracticeMethodBn = "চরম ঋণ বা কর্মসংকটের সময় রাসুলুল্লাহ ﷺ আলী (রা.)-কে এই দো'আ শিক্ষা দিয়েছিলেন।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_job_1",
                    surahNameBn = "আল-কাসাস",
                    surahNameAr = "القصص",
                    surahNumber = 28,
                    ayahNumber = 24,
                    arabicText = "رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ",
                    banglaPronunciation = "রব্বি ইন্নী লিমা আনযালতা ইলাইয়্যা মিন খাইরিন ফাক্বীর।",
                    banglaTranslation = "হে আমার প্রতিপালক! আপনি আমার প্রতি যে অনুগ্রহই অবতীর্ণ করবেন, আমি তার চূড়ান্ত মুখাপেক্ষী।",
                    englishTranslation = "My Lord, indeed I am in need of whatever good You would send down to me.",
                    tafsirContextBn = "কর্মহীন অবস্থায় আল্লাহর দরবারে পরম বিনয় প্রকাশের অনুপম কুরআনী আয়াত।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_job_1",
                    hadithBookBn = "জামে আত-তিরমিযী",
                    hadithBookEn = "Jami at-Tirmidhi",
                    hadithNumber = "হাদীস ৩৫৬৩",
                    narratorCompanionBn = "আলী (রা.)",
                    gradingBn = "হাসান সহীহ",
                    arabicText = "اللَّهُمَّ اكْفِنِي بِحَلاَلِكَ عَنْ حَرَامِكَ، وَأَغْنِنِي بِفَضْلِكَ عَمَّنْ سِوَاكَ",
                    banglaPronunciation = "আল্লাহুম্মাকফিনী বিহিলালিকা 'আন হারামিক, ওয়া আগনিনী বিফাদ্বলিকা 'আম্মান সিওয়াক।",
                    banglaTranslation = "হে আল্লাহ! আপনার হালাল দ্বারা আমাকে তুষ্ট রাখুন হারাম থেকে বাঁচিয়ে, এবং আপনার অনুগ্রহে আপনি ব্যতীত অন্য কারও মুখাপেক্ষী করবেন না।",
                    englishTranslation = "O Allah, suffice me with what is lawful against what is unlawful.",
                    sunnahPracticeMethodBn = "সালাতের পর ও সকাল-সন্ধ্যায় নিয়মিত পাঠ্য।",
                    occasionOfUsageBn = "কর্মসংস্থান ও অভাব দূর করতে।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_job_1",
                    titleBn = "মর্যাদাপূর্ণ হালাল উপার্জনের ব্যক্তিগত মিনতি",
                    heartfeltSupplicationBn = "হে আর-রাযযাক্ব! আমাকে এমন এক হালাল ও বরকতময় কর্মসংস্থান দান করুন যা আমার ও আমার পরিবারের জন্য সম্মানজনক হয় এবং যার মাধ্যমে আমি আপনার সন্তুষ্টি অনুযায়ী মানুষের খেদমত করতে পারি।",
                    invokedNamesOfAllahBn = listOf("ইয়া রাযযাক্ব (হে রিজিকদাতা)", "ইয়া ওয়াহহাব (হে মহান দাতা)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_job_1",
                    timingTitleBn = "জুমু'আর দিনের শেষ প্রহর (আসরের পর থেকে মাগরিব)",
                    timingDescriptionBn = "জুমু'আর দিনে 'সা'আতুল ইজাবাহ' বা দু'আ কবুলের নিশ্চিত মুহূর্ত।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'জুমু'আর দিনে এমন একটি মুহূর্ত রয়েছে, কোনো মুসলিম বান্দা সালাতরত বা দো'আরত অবস্থায় আল্লাহর কাছে কিছু চাইলে আল্লাহ তাকে তা দেন' (সহীহ বুখারী ৯৩৫)।",
                    practicalTipBn = "জুমু'আর দিন আসর সালাত শেষে মসজিদে বা নির্জনে বসে চাকরির জন্য দো'আ করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "হারাম চাকরির প্রলোভন ও তাকওয়া",
                    dominantScholarlyPositionBn = "সুদ বা হারামের সাথে জড়িত কোনো চাকরিতে যাওয়া জায়েজ নেই। আল্লাহর জন্য যে ব্যক্তি হারাম ছেড়ে দেয়, আল্লাহ তাকে তার চেয়ে বহুগুণ উত্তম বিকল্প দান করেন।",
                    supportingEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'তুমি আল্লাহর ভয়ে যা কিছু ত্যাগ করবে, আল্লাহ তোমাকে তার চেয়ে উত্তম কিছু প্রদান করবেন' (মুসনাদে আহমাদ ২০৭৩৯)।",
                    consensusOrNuanceBn = "সাময়িক অর্থকষ্ট হলেও হালাল রিজিকের ওপর অবিচল থাকা ঈমানের চূড়ান্ত পরীক্ষা।"
                )
            ),
            practicalRemindersBn = listOf(
                "সিভি বা আবেদনপত্র আধুনিক ও পেশাদার মানে প্রস্তুত করুন।",
                "বেশি বেশি ইস্তিগফার পাঠ করুন, কারণ পবিত্র কুরআনে বলা হয়েছে ইস্তিগফারের ফলে আল্লাহ ধন-সম্পদ বৃদ্ধি করেন (সূরা নূহ ৭১:১০-১২)।",
                "প্রতিদিন সাধ্যমতো আত্মীয়-স্বজনের সাথে সুসম্পর্ক রক্ষা করুন; এতে রিজিকে বরকত বাড়ে (সহীহ বুখারী ২০৬৭)।"
            )
        ),

        // ==========================================
        // 5. DEBT & FINANCIAL ANXIETY
        // ==========================================
        "debt_finance" to PersonalDuaBlueprint(
            id = "debt_finance",
            userQuery = "Make a Dua for financial difficulty.",
            scenarioTitleBn = "ঋণমুক্তি, অভাব দূরীকরণ ও আর্থিক পেরেশানি লাঘব",
            scenarioTitleEn = "Debt Relief & Freedom from Financial Anxiety",
            spiritualComfortBn = "ঋণ ও আর্থিক সংকট একজন আত্মমর্যাদাশীল মুমিনের জন্য গভীর মনস্তাত্ত্বিক বোঝা। রাসুলুল্লাহ ﷺ প্রতিদিন সালাতে ঋণের বোঝা থেকে আল্লাহর আশ্রয় চাইতেন। সাহাবী আবু উমামা (রা.) যখন মসজিদে ঋণের ভারে ক্লান্ত বসেছিলেন, আল্লাহর রাসুল ﷺ তাঁকে সকাল-সন্ধ্যার বিশেষ দো'আ শিখিয়েছিলেন এবং তাঁর সমস্ত ঋণ পরিশোধের পথ আল্লাহ সহজ করে দিয়েছিলেন। খাঁটি নিয়তে ঋণ পরিশোধের সংকল্প করুন, আল্লাহ স্বয়ং আপনার পক্ষ থেকে দায়িত্ব নেবেন।",
            spiritualComfortEn = "Debt and financial distress are heavy burdens, but Allah is the absolute Provider. Sincere intention to repay, avoidance of unlawful earnings, and consistent supplication invite unseen divine help. Trust that ease follows hardship.",
            detectedIntentBn = "ঋণ থেকে নিষ্কৃতি ও আর্থিক সংকটের অবসান",
            detectedIntentEn = "Relief from debt, poverty and economic distress",
            emotionalContextBn = "মানসিক চাপ, লাঞ্ছনার ভয় ও আর্থিক অস্থিরতা",
            emotionalContextEn = "Extreme stress, fear of humiliation & seeking independence",
            peopleInvolvedBn = "ঋণগ্রস্ত ব্যক্তি ও পরিবার",
            peopleInvolvedEn = "Debtor & Family",
            curatedArabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ ۝ اللَّهُمَّ مَالِكَ الْمُلْكِ تُؤْتِي الْمُلْكَ مَن تَشَاءُ... رَحْمَٰنَ الدُّنْيَا وَالْآخِرَةِ وَرَحِيمَهُمَا، ارْحَمْنِي رَحْمَةً تُغْنِينِي بِهَا عَنْ رَحْمَةِ مَنْ سِوَاكَ",
            curatedTranslationBn = "হে আল্লাহ! নিশ্চয়ই আমি আপনার আশ্রয় নিচ্ছি দুশ্চিন্তা ও দুঃখ থেকে, অক্ষমতা ও অলসতা থেকে, কৃপণতা ও কাপুরুষতা থেকে, ঋণের কঠিন বোঝা এবং মানুষের পরাভব থেকে। হে আল্লাহ, রাজাধিরাজ! আপনি যাকে ইচ্ছা রাজত্ব দেন... আপনি দুনিয়া ও আখেরাতের পরম দয়ালু; আমাকে এমন রহমত দান করুন যার মাধ্যমে আপনি ছাড়া সবার করুণা থেকে আমি অমুখাপেক্ষী হতে পারি।",
            curatedTranslationEn = "O Allah, I seek refuge in You from grief and worry, incapacity and laziness, cowardice and miserliness, the burden of debt and being overpowered by men. O Allah, Owner of Sovereignty... Grant me mercy that frees me from needing mercy from anyone else.",
            whySelectedBn = "ঋণমুক্তির জন্য রাসুলুল্লাহ ﷺ-এর নির্দেশিত প্রসিদ্ধ সহীহ নববী দো'আ (আবু দাউদ ১৫৫৫) এবং পাহাড়সম ঋণ থাকলেও তা পরিশোধের মু'আজ (রা.)-কে শেখানো হাদীসের দু'আ (তাবারানী, সহীহ আত-তারগীব ১৮২১) নির্বাচিত হয়েছে।",
            whySelectedEn = "Combines the famous daily Prophetic protection from crushing debt (Sunan Abi Dawud 1555) with the authentic supplication taught to Mu'adh ibn Jabal for insurmountable debts (Al-Mu'jam as-Saghir / Sahih at-Targhib 1821).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_debt_1",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "আবু উমামা (রা.)-কে রাসুল ﷺ-এর ঋণমুক্তির দো'আ",
                    titleEn = "Prophetic Supplication for Overcoming Debt",
                    referenceText = "সুনানে আবু দাউদ ১৫৫৫, সহীহ আল-বুখারী ২৮৯৩",
                    hadithBookBn = "সুনানে আবু দাউদ ও সহীহ বুখারী",
                    hadithBookEn = "Sunan Abi Dawud & Sahih al-Bukhari",
                    hadithNumber = "আবু দাউদ ১৫৫৫",
                    narratorCompanionBn = "আবু সাঈদ আল-খুদরী (রা.)",
                    gradingBn = "সহীহ (Authentic)",
                    arabicSourceText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
                    translationBn = "হে আল্লাহ! আমি আপনার আশ্রয় নিচ্ছি দুশ্চিন্তা ও শোক থেকে, অপারগতা ও অলসতা থেকে, কৃপণতা ও ভীরুতা থেকে, ঋণের তীব্র বোঝা ও মানুষের আধিপত্য থেকে।",
                    translationEn = "O Allah, I seek refuge in You from anxiety and grief, weakness and laziness, miserliness and cowardice, the burden of debt and the domination of men.",
                    pronunciationBn = "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিনাল হাম্মি ওয়াল হাযানি, ওয়াল 'আজযি ওয়াল কাসালি...",
                    sunnahPracticeMethodBn = "সকাল ও সন্ধ্যায় ৩ বার পাঠ্য।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_debt_1",
                    surahNameBn = "আলে ইমরান",
                    surahNameAr = "آل عمران",
                    surahNumber = 3,
                    ayahNumber = 26,
                    arabicText = "قُلِ اللَّهُمَّ مَالِكَ الْمُلْكِ تُؤْتِي الْمُلْكَ مَن تَشَاءُ وَتَنزِعُ الْمُلْكَ مِمَّن تَشَاءُ وَتُعِزُّ مَن تَشَاءُ وَتُذِلُّ مَن تَشَاءُ ۖ بِيَدِكَ الْخَيْرُ ۖ إِنَّكَ عَلَىٰ كُلِّ شَيْءٍ قَدِيرٌ",
                    banglaPronunciation = "কুলিল্লাহুম্মা মালিকাল মুলকি তু'তিল মুলকা মান তাশা-উ ওয়া তানযি'উল মুলকা মিম্মান তাশা-উ...",
                    banglaTranslation = "বলুন: হে আল্লাহ! আপনিই সার্বভৌম শক্তির মালিক। আপনি যাকে ইচ্ছা রাজ্য দান করেন এবং যার কাছ থেকে ইচ্ছা রাজ্য ছিনিয়ে নেন...",
                    englishTranslation = "Say, 'O Allah, Owner of Sovereignty, You give sovereignty to whom You will and You take sovereignty away from whom You will...'",
                    tafsirContextBn = "সমস্ত ধন-সম্পদের চূড়ান্ত মালিকানার স্বীকৃতি ও অচিন্তনীয় রিযিক লাভের আয়াত।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_debt_1",
                    hadithBookBn = "সুনানে আবু দাউদ",
                    hadithBookEn = "Sunan Abi Dawud",
                    hadithNumber = "১৫৫৫",
                    narratorCompanionBn = "আবু সাঈদ আল-খুদরী (রা.)",
                    gradingBn = "সহীহ",
                    arabicText = "اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْهَمِّ وَالْحَزَنِ، وَالْعَجْزِ وَالْكَسَلِ، وَالْبُخْلِ وَالْجُبْنِ، وَضَلَعِ الدَّيْنِ، وَغَلَبَةِ الرِّجَالِ",
                    banglaPronunciation = "আল্লাহুম্মা ইন্নী আ'ঊযু বিকা মিনাল হাম্মি ওয়াল হাযানি...",
                    banglaTranslation = "হে আল্লাহ! আমি দুশ্চিন্তা ও ঋণের বোঝা থেকে আপনার আশ্রয় চাই।",
                    englishTranslation = "O Allah, I seek refuge in You from grief and debt.",
                    sunnahPracticeMethodBn = "সকাল ও সন্ধ্যায় পাঠ্য।",
                    occasionOfUsageBn = "ঋণ ও দেনার সময়।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_debt_1",
                    titleBn = "ঋণ পরিশোধে আল্লাহর অদৃশ্য সাহায্যের আবেদন",
                    heartfeltSupplicationBn = "হে আল্লাহ! আপনি তো জানেন আমি কাউকে ঠকানোর জন্য ঋণ নিইনি। আমার পক্ষ থেকে এই ঋণ দ্রুত পরিশোধের সহজ পথ বের করে দিন এবং আমাকে সম্মানিত ও স্বাবলম্বী জীবন দান করুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া গণিয়্যু (হে অভাবহীন ধনী)", "ইয়া বাসিথ্ব (হে প্রশস্তকারী)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_debt_1",
                    timingTitleBn = "ফরজ সালাতের তাশাহহুদে সালাম ফেরানোর পূর্বে",
                    timingDescriptionBn = "সালাতের সালাম ফেরানোর আগে দু'আ মাসূরার সময়।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ তাশাহহুদে সালাম ফেরানোর পূর্বে বলতেন: 'হে আল্লাহ! আমি গুনাহ ও ঋণের বোঝা থেকে আপনার আশ্রয় চাই' (সহীহ বুখারী ৮৩২)।",
                    practicalTipBn = "প্রতি ফরজ সালাতে সালামের আগে ঋণমুক্তির এই দু'আ যুক্ত করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "আন্তরিক ঋণ পরিশোধের নিয়তের বরকত",
                    dominantScholarlyPositionBn = "যে ব্যক্তি ঋণ পরিশোধের খাঁটি নিয়ত রাখে, আল্লাহ নিজে তার ঋণ পরিশোধের ব্যবস্থা করে দেন।",
                    supportingEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'যে ব্যক্তি পরিশোধের নিয়তে মানুষের সম্পদ ঋণ নেয়, আল্লাহ তার পক্ষ থেকে তা আদায় করে দেন' (সহীহ বুখারী ২৩৮৭)।",
                    consensusOrNuanceBn = "ঋণ ফাঁকি দেওয়ার নিয়ত থাকলে বরকত নষ্ট হয় এবং আখেরাতে কঠিন শাস্তি রয়েছে।"
                )
            ),
            practicalRemindersBn = listOf(
                "পাওনাদারের সাথে সরাসরি যোগাযোগ করে বিনীতভাবে সময় চেয়ে নিন।",
                "অপ্রয়োজনীয় বিলাসিতা ও অপ্রয়োজনীয় খরচ অবিলম্বে বন্ধ করুন।",
                "বেশি বেশি সাইয়্যেদুল ইস্তিগফার পাঠ করুন।"
            )
        ),

        // ==========================================
        // 6. MARRIAGE & RIGHTEOUS SPOUSE
        // ==========================================
        "marriage_spouse" to PersonalDuaBlueprint(
            id = "marriage_spouse",
            userQuery = "I am getting married and looking for a righteous spouse.",
            scenarioTitleBn = "নেক জীবনসঙ্গী লাভ, পবিত্র বিবাহ ও দাম্পত্য সুখ",
            scenarioTitleEn = "Righteous Spouse, Pure Marriage & Family Peace",
            spiritualComfortBn = "বিবাহ ঈমানের অর্ধেক পূর্ণ করে এবং প্রশান্তির অন্যতম সেরা নিদর্শন। আল্লাহ তা'আলা পবিত্র কুরআনে বলেন: 'তিনি তোমাদের মধ্য থেকে তোমাদের সঙ্গিনীদের সৃষ্টি করেছেন যাতে তোমরা তাদের নিকট প্রশান্তি লাভ করতে পারো এবং তোমাদের মাঝে পারস্পরিক ভালোবাসা ও দয়া সৃষ্টি করেছেন' (সূরা আর-রূম ৩০:২১)। দ্বীনদার ও চরিত্রবান জীবনসঙ্গী নির্বাচনের জন্য আল্লাহর কাছে বিনম্র প্রার্থনা করুন।",
            spiritualComfortEn = "Marriage is half of faith and a sign of Allah's mercy. Seek a partner based on piety and character, and pray for affection and peace to fill your future home.",
            detectedIntentBn = "নেক জীবনসঙ্গী ও পবিত্র দাম্পত্য লাভ",
            detectedIntentEn = "Righteous spouse, blessed marriage & peaceful household",
            emotionalContextBn = "নতুন জীবনের সূচনা, ভালোবাসা ও সুরক্ষার প্রত্যাশা",
            emotionalContextEn = "Hope for righteous companionship, love and emotional safety",
            peopleInvolvedBn = "বিবাহপ্রার্থী ও ভবিষ্যৎ দম্পতি",
            peopleInvolvedEn = "Prospective Couple & Future Family",
            curatedArabicText = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا ۝ رَبِّ إِنِّي لِمَا أَنزَلْتَ إِلَيَّ مِنْ خَيْرٍ فَقِيرٌ",
            curatedTranslationBn = "হে আমাদের রব! আমাদের স্ত্রীদের (বা স্বামীদের) এবং সন্তানদের আমাদের চোখের শীতলতা বানিয়ে দিন এবং আমাদের মুত্তাকীদের নেতা বানিয়ে দিন। হে আমার প্রতিপালক! আপনি আমার প্রতি যে অনুগ্রহই অবতীর্ণ করবেন, আমি তার চূড়ান্ত মুখাপেক্ষী।",
            curatedTranslationEn = "Our Lord, grant us from among our wives and offspring comfort to our eyes and make us an example for the righteous. My Lord, indeed I am in need of whatever good You would send down to me.",
            whySelectedBn = "কুরআনে কারীমে নেক জীবনসঙ্গী ও পরিবারের শ্রেষ্ঠ আয়াত (সূরা আল-ফুরকান ২৫:৭৪) এবং বিবাহ ও আশ্রয় লাভের জন্য মুসা (আ.)-এর দো'আ (সূরা কাসাস ২৮:২৪) একত্রিত করা হয়েছে।",
            whySelectedEn = "Combines the ultimate Quranic supplication for spouse and family joy (Surah Al-Furqan 25:74) with Musa's prayer for righteous companionship and dwelling (Surah Al-Qasas 28:24).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_marriage_1",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "চোখের শীতলতাদানকারী জীবনসঙ্গীর কুরআনী দো'আ",
                    titleEn = "Quranic Supplication for Spouse and Children",
                    referenceText = "আল-কুরআন, সূরা আল-ফুরকান (২৫:৭৪)",
                    surahNameBn = "আল-ফুরকান",
                    surahNameEn = "Al-Furqan",
                    surahNameAr = "الفرقان",
                    surahNumber = 25,
                    ayahNumber = 74,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
                    translationBn = "হে আমাদের রব! আমাদের জন্য এমন জীবনসঙ্গী ও সন্তান-সন্ততি দান করুন যারা আমাদের চোখের শীতলতা হবে এবং আমাদের মুত্তাকীদের জন্য আদর্শ নেতা বানিয়ে দিন।",
                    translationEn = "Our Lord, grant us from among our spouses and offspring comfort to our eyes and make us leaders for the righteous.",
                    pronunciationBn = "রব্বানা হাব লানা মিন আযওয়াজিনা ওয়া যুররিইয়্যা-তিনা কুররাতা আ'ইয়ুনিওঁ ওয়াজ'আলনা লিলমুত্তাক্বীনা ইমামা।",
                    tafsirContextBn = "রহমানের খাঁটি বান্দাদের অন্যতম প্রধান গুণ হলো তারা পরিবারের পবিত্রতা ও চোখের শীতলতার জন্য সর্বদা প্রার্থনা করে।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_marriage_1",
                    surahNameBn = "আল-ফুরকান",
                    surahNameAr = "الفرقان",
                    surahNumber = 25,
                    ayahNumber = 74,
                    arabicText = "رَبَّنَا هَبْ لَنَا مِنْ أَزْوَاجِنَا وَذُرِّيَّاتِنَا قُرَّةَ أَعْيُنٍ وَاجْعَلْنَا لِلْمُتَّقِينَ إِمَامًا",
                    banglaPronunciation = "রব্বানা হাব লানা মিন আযওয়াজিনা ওয়া যুররিইয়্যা-তিনা কুররাতা আ'ইয়ুনিওঁ ওয়াজ'আলনা লিলমুত্তাক্বীনা ইমামা।",
                    banglaTranslation = "হে আমাদের রব! আমাদের স্ত্রীদের ও সন্তানদের চোখের শীতলতা বানিয়ে দিন...",
                    englishTranslation = "Our Lord, grant us comfort to our eyes...",
                    tafsirContextBn = "দাম্পত্য জীবনের শান্তি ও নেক বংশধরের জন্য শ্রেষ্ঠ কুরআনী মুনাজাত।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_marriage_1",
                    hadithBookBn = "সুনানে আবু দাউদ ও সুনানে ইবনে মাজাহ",
                    hadithBookEn = "Sunan Abi Dawud & Sunan Ibn Majah",
                    hadithNumber = "আবু দাউদ ২১৬০, ইবনে মাজাহ ১৯১৮",
                    narratorCompanionBn = "আবদুল্লাহ ইবনে আমর (রা.)",
                    gradingBn = "হাসান (আলবানী)",
                    arabicText = "اللَّهُمَّ إِنِّي أَسْأَلُكَ خَيْرَهَا وَخَيْرَ مَا جَبَلْتَهَا عَلَيْهِ، وَأَعُوذُ بِكَ مِنْ شَرِّهَا وَمِنْ شَرِّ مَا جَبَلْتَهَا عَلَيْهِ",
                    banglaPronunciation = "আল্লাহুম্মা ইন্নী আসআলুকা খাইরাহা ওয়া খাইরা মা জাবালতাহা 'আলাইহ, ওয়া আ'ঊযু বিকা মিন শাররিহা ওয়া শাররি মা জাবালতাহা 'আলাইহ।",
                    banglaTranslation = "হে আল্লাহ! আমি আপনার কাছে এর কল্যাণ এবং এর স্বভাব-চরিত্রের কল্যাণ প্রার্থনা করছি; আর এর অকল্যাণ ও খারাপ স্বভাব থেকে আপনার আশ্রয় চাই।",
                    englishTranslation = "O Allah, I ask You for the good in her and the good of what You have created in her...",
                    sunnahPracticeMethodBn = "বিবাহের পর স্ত্রীর মাথায় হাত রেখে দো'আটি পাঠ করা সুন্নাত।",
                    occasionOfUsageBn = "বিবাহ সম্পন্ন হওয়ার পর।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_marriage_1",
                    titleBn = "দ্বীনদার জীবনসঙ্গী ও পবিত্র বন্ধনের আবেদন",
                    heartfeltSupplicationBn = "হে আল্লাহ! আমাকে এমন একজন জীবনসঙ্গী দান করুন যার সাথে মিলে আমি জান্নাতের পথে এগিয়ে যেতে পারি, যিনি বিপদে আমার সান্ত্বনা হবেন এবং সুখে আল্লাহর শোকরকারী হবেন।",
                    invokedNamesOfAllahBn = listOf("ইয়া ওয়াদুদ (হে প্রেমময় বন্ধু)", "ইয়া কারীম (হে মহানুভব)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_marriage_1",
                    timingTitleBn = "তাহাজ্জুদ সালাতের সিজদায় ও আযান-ইকামতের মধ্যবর্তী সময়",
                    timingDescriptionBn = "জীবনসঙ্গী নির্বাচনের পূর্বে ইস্তিখারা ও তাহাজ্জুদে আন্তরিক দো'আ।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ সাহাবাদের যেকোনো গুরুত্বপূর্ণ কাজের পূর্বে ইস্তিখারার সালাত শেখাতেন যেভাবে কুরআনের সূরা শেখাতেন (সহীহ বুখারী ১১৬২)।",
                    practicalTipBn = "প্রস্তাব আসার পর দুই রাকাত ইস্তিখারা সালাত আদায় করে আল্লাহর সিদ্ধান্ত কামনা করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "পাত্র-পাত্রী নির্বাচনে দ্বীনদারীকে অগ্রাধিকার",
                    dominantScholarlyPositionBn = "ধন-সম্পদ, বংশ বা রূপের চেয়ে সততা ও দ্বীনদারীকে প্রাধান্য দেওয়াই সুন্নাহর চূড়ান্ত শিক্ষা।",
                    supportingEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'তোমরা দ্বীনদারীকে প্রাধান্য দাও, তবেই তোমরা সফলকাম হবে' (সহীহ বুখারী ৫০৯০)।",
                    consensusOrNuanceBn = "দ্বীনদার জীবনসঙ্গী নির্বাচন করলে দাম্পত্য জীবন উভয় জগতেই সফল হয়।"
                )
            ),
            practicalRemindersBn = listOf(
                "বিবাহের ক্ষেত্রে বাহুল্য খরচ ও অপচয় পরিহার করে সুন্নাতসম্মত সাদাসিধে আয়োজন করুন।",
                "নিজের চরিত্র ও আমলকে পরিশুদ্ধ রাখুন, কারণ নেক জীবনসঙ্গী আল্লাহর তরফ থেকে পবিত্রতার প্রতিদান।",
                "দাম্পত্য জীবনে ধৈর্য ও পারস্পরিক ক্ষমার মানসিকতা তৈরি করুন।"
            )
        ),

        // ==========================================
        // 7. FORGIVENESS & REPENTANCE
        // ==========================================
        "forgiveness_repentance" to PersonalDuaBlueprint(
            id = "forgiveness_repentance",
            userQuery = "I want Allah to forgive my sins.",
            scenarioTitleBn = "খাঁটি তাওবাহ, পাপের মোচন ও অন্তরের পবিত্রতা লাভ",
            scenarioTitleEn = "Repentance, Forgiveness & Spiritual Cleansing",
            spiritualComfortBn = "আল্লাহর রহমত থেকে নিরাশ হওয়া মহাপাপ। বান্দা যত গুনাহই করুক, আল্লাহর ক্ষমা তার চেয়ে অনন্তগুণে বিশাল। মহান আল্লাহ বলেন: 'বলুন, হে আমার বান্দাগণ যারা নিজেদের ওপর অবিচার করেছ! তোমরা আল্লাহর রহমত থেকে নিরাশ হয়ো না। নিশ্চয়ই আল্লাহ সমস্ত পাপ ক্ষমা করে দেন। নিশ্চয় তিনি অতি ক্ষমাশীল, পরম দয়ালু' (সূরা আয-যুমার ৩৯:৫৩)। অনুশোচনার এক ফোঁটা অশ্রু জাহান্নামের আগুনকে নিভিয়ে দেওয়ার ক্ষমতা রাখে। এখনই রবের দিকে ফিরে আসুন।",
            spiritualComfortEn = "Never despair of Allah's boundless mercy. He declares: 'Do not despair of the mercy of Allah; indeed, Allah forgives all sins' (Surah Az-Zumar 39:53). A single tear of sincere repentance washes away years of shortcomings.",
            detectedIntentBn = "তাওবাহ, ইস্তিগফার ও গুনাহ মোচন",
            detectedIntentEn = "Sincere repentance, forgiveness and cleansing of sins",
            emotionalContextBn = "অনুতাপ, গ্লানি ও রবের ক্ষমার আশাবাদ",
            emotionalContextEn = "Remorse, spiritual guilt and longing for divine mercy",
            peopleInvolvedBn = "পাপী বান্দা ও ক্ষমাশীল আল্লাহ",
            peopleInvolvedEn = "Repentant Servant & Al-Ghaffar",
            curatedArabicText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
            curatedTranslationBn = "হে আল্লাহ! আপনি আমার পালনকর্তা, আপনি ছাড়া কোনো সত্য উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি আমার সাধ্যমতো আপনার অঙ্গীকার ও প্রতিশ্রুতির ওপর অবিচল রয়েছি। আমি আমার কৃতকর্মের অনিষ্ট থেকে আপনার আশ্রয় চাই। আমার প্রতি আপনার অনুগ্রহ স্বীকার করছি এবং আমার পাপ আমি স্বীকার করছি; অতএব আপনি আমাকে ক্ষমা করুন। কারণ আপনি ছাড়া গুনাহ মাফ করার আর কেউ নেই।",
            curatedTranslationEn = "O Allah, You are my Lord; there is no deity except You. You created me, and I am Your servant, and I abide by Your covenant and promise as best I can. I seek refuge in You from the evil of what I have done. I acknowledge Your favors upon me, and I acknowledge my sin, so forgive me, for none forgives sins except You.",
            whySelectedBn = "রাসুলুল্লাহ ﷺ যাকে ক্ষমা প্রার্থনার সর্বশ্রেষ্ঠ রূপ 'সাইয়্যেদুল ইস্তিগফার' আখ্যা দিয়েছেন এবং দিনে ও রাতে পাঠকারীর জন্য জান্নাতের সুসংবাদ দিয়েছেন (সহীহ বুখারী ৬৩০৬), সেই সর্বোচ্চ প্রামাণ্য হাদীসটি মূল ভিত্তি হিসেবে নির্বাচিত হয়েছে।",
            whySelectedEn = "Features Sayyidul Istighfar (The Master Supplication for Forgiveness) from Sahih al-Bukhari (6306), declared by the Prophet ﷺ as the greatest formula of repentance guaranteeing Paradise when said with firm faith.",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_repent_1",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "সাইয়্যেদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমা প্রার্থনা)",
                    titleEn = "Sayyidul Istighfar (Chief of Forgiveness)",
                    referenceText = "সহীহ আল-বুখারী ৬৩০৬",
                    hadithBookBn = "সহীহ আল-বুখারী",
                    hadithBookEn = "Sahih al-Bukhari",
                    hadithNumber = "৬৩০৬",
                    narratorCompanionBn = "শাদ্দাদ ইবনে আউস (রা.)",
                    gradingBn = "সহীহ বুখারী (সর্বোচ্চ প্রামাণ্য)",
                    arabicSourceText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لاَ يَغْفِرُ الذُّنُوبَ إِلاَّ أَنْتَ",
                    translationBn = "হে আল্লাহ! আপনি আমার রব, আপনি ছাড়া সত্য কোনো ইলাহ নেই। আপনি আমাকে সৃষ্টি করেছেন আর আমি আপনার গোলাম...",
                    translationEn = "O Allah, You are my Lord; there is no deity except You...",
                    pronunciationBn = "আল্লাহুম্মা আনতা রব্বী লা ইলাহা ইল্লা আনতা, খালাক্বতানী ওয়া আনা 'আবদুকা...",
                    sunnahPracticeMethodBn = "সকাল ও সন্ধ্যায় পাঠ্য। যে ব্যক্তি সকালে একিনের সাথে পড়ে সন্ধ্যার আগে মারা যায় সে জান্নাতী, এবং সন্ধ্যায় পড়ে সকালের আগে মারা গেলেও জান্নাতী।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_repent_1",
                    surahNameBn = "আল-আ'রাফ",
                    surahNameAr = "الأعراف",
                    surahNumber = 7,
                    ayahNumber = 23,
                    arabicText = "رَبَّنَا ظَلَمْنَا أَنفُسَنَا وَإِن لَّمْ تَغْفِرْ لَنَا وَتَرْحَمْنَا لَنَكُونَنَّ مِنَ الْخَاسِرِينَ",
                    banglaPronunciation = "রব্বানা যালামনা- আনফুসানা- ওয়া ইল্লাম তাগফির লানা- ওয়া তারহামনা- লানাকূ নান্না মিনাল খাসিরীন।",
                    banglaTranslation = "হে আমাদের রব! আমরা নিজেদের প্রতি অবিচার করেছি। যদি আপনি আমাদের ক্ষমা না করেন এবং দয়া না করেন, তবে নিশ্চয়ই আমরা ক্ষতিগ্রস্তদের অন্তর্ভুক্ত হয়ে যাব।",
                    englishTranslation = "Our Lord, we have wronged ourselves, and if You do not forgive us and have mercy upon us, we will surely be among the losers.",
                    tafsirContextBn = "আদম (আ.) ও হাওয়া (আ.)-এর সেই তাওবার কান্না যার মাধ্যমে আল্লাহ তাঁদের ক্ষমা করেছিলেন।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_repent_1",
                    hadithBookBn = "সহীহ আল-বুখারী",
                    hadithBookEn = "Sahih al-Bukhari",
                    hadithNumber = "৬৩০৬",
                    narratorCompanionBn = "শাদ্দাদ ইবনে আউস (রা.)",
                    gradingBn = "সহীহ বুখারী",
                    arabicText = "اللَّهُمَّ أَنْتَ رَبِّي لاَ إِلَهَ إِلاَّ أَنْتَ...",
                    banglaPronunciation = "আল্লাহুম্মা আনতা রব্বী...",
                    banglaTranslation = "হে আল্লাহ! আপনি আমার প্রতিপালক...",
                    englishTranslation = "O Allah, You are my Lord...",
                    sunnahPracticeMethodBn = "সকাল ও সন্ধ্যায় একবার পাঠ্য।",
                    occasionOfUsageBn = "প্রতিদিন নিয়মিত তাওবাহ।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_repent_1",
                    titleBn = "লজ্জিত অন্তরের অশ্রুসজল ক্ষমা ভিক্ষা",
                    heartfeltSupplicationBn = "হে গাফুরুর রাহীম! আমি অত্যন্ত লজ্জিত ও অনুতপ্ত। শয়তান ও নিজের কুপ্রবৃত্তির ধোঁকায় আমি অন্যায় করে ফেলেছি। হে রব! আপনি আমাকে ত্যাগ করবেন না, আমার তাওবাহ কবুল করুন এবং আমার অন্তরকে পাপ থেকে পবিত্র রাখুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া তাওয়াব (হে তাওবাহ কবুলকারী)", "ইয়া গাফফার (হে পরম ক্ষমাশীল)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_repent_1",
                    timingTitleBn = "সালাতুত তাওবাহ আদায় করে নির্জনে দু'আ",
                    timingDescriptionBn = "উযু করে দুই রাকাত নফল সালাত আদায় করে সিজদায় কেঁদে ক্ষমা চাওয়া।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'যেকোনো বান্দা কোনো পাপ করার পর সুন্দরভাবে উযু করে দুই রাকাত সালাত পড়ে আল্লাহর কাছে ক্ষমা চাইলে, আল্লাহ তাকে ক্ষমা করে দেন' (জামে তিরমিযী ৪০৬, সুনানে আবু দাউদ ১৫২১)।",
                    practicalTipBn = "একান্তে দুই রাকাত সালাত পড়ে সিজদায় চোখের পানি ফেলে ক্ষমা চান।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "তাওবাহ কবুলের শর্তসমূহ",
                    dominantScholarlyPositionBn = "আল্লাহর হকের ক্ষেত্রে তাওবার শর্ত ৩টি: ১) পাপ অবিলম্বে বন্ধ করা, ২) কৃতকর্মের জন্য আন্তরিক অনুশোচনা, ৩) ভবিষ্যতে তা আর না করার দৃঢ় সংকল্প। মানুষের হকের ক্ষেত্রে পাওনা ফিরিয়ে দিতে হবে।",
                    supportingEvidenceBn = "ইমাম নববী (রহ.)-এর রিয়াদুস সলেহীনের তাওবাহ অধ্যায়।",
                    consensusOrNuanceBn = "মানুষের হক নষ্ট করে থাকলে কেবল দো'আ দিয়ে মাফ পাওয়া যায় না, পাওনাদারের পাওনা পরিশোধ আবশ্যক।"
                )
            ),
            practicalRemindersBn = listOf(
                "পাপের পরিবেশ ও খারাপ বন্ধুদের সঙ্গ অবিলম্বে ত্যাগ করুন।",
                "পাপের পরপরই ভালো কাজ বা সদকা করুন, কারণ নেক আমল পাপকে ধুয়ে ফেলে।",
                "প্রতিদিন ১০০ বার 'আস্তাগফিরুল্লাহ' পাঠের সুন্নাত জারি রাখুন।"
            )
        ),

        // ==========================================
        // 8. ANXIETY & PEACE OF HEART
        // ==========================================
        "anxiety_mental" to PersonalDuaBlueprint(
            id = "anxiety_mental",
            userQuery = "I feel anxious and my heart feels heavy.",
            scenarioTitleBn = "মানসিক বিষণ্নতা, উৎকণ্ঠা নিবারণ ও অন্তরের প্রশান্তি",
            scenarioTitleEn = "Relieving Depression, Anxiety & Heavy Heart",
            spiritualComfortBn = "মন অস্থির হওয়া বা বুক ভারাক্রান্ত হওয়া মানুষের এক সহজাত পরীক্ষা। আল্লাহ তা'আলা পবিত্র কুরআনে সান্ত্বনা দিয়ে বলেছেন: 'জেনে রেখো, একমাত্র আল্লাহর যিকিরেই অন্তরসমূহ শান্তি লাভ করে' (সূরা আর-রা'দ ১৩:২৮)। দুনিয়ার কোনো প্রাপ্তি বা সম্পদ অন্তরের আসল তৃষ্ণা মেটাতে পারে না। রাসুলুল্লাহ ﷺ তীব্র দুঃখ-কষ্টে পবিত্র কুরআনকে অন্তরের বসন্ত বানানোর দু'আ শিখিয়েছেন। এই দু'আ পাঠ করলে আল্লাহ বিষণ্নতাকে দূর করে আনন্দ দিয়ে প্রতিস্থাপন করেন।",
            spiritualComfortEn = "Verily, in the remembrance of Allah do hearts find rest (Surah Ar-Ra'd 13:28). When the chest tightens and anxiety builds, turn your heart towards the One who fashioned it. The Quran is the true spring of the believer's heart.",
            detectedIntentBn = "মানসিক অস্থিরতা দূর ও অন্তরের শান্তি লাভ",
            detectedIntentEn = "Relief from anxiety, sorrow and seeking inner serenity",
            emotionalContextBn = "বিষণ্নতা, শূন্যতা ও অস্থির চিত্ত",
            emotionalContextEn = "Mental distress, heaviness of chest & longing for solace",
            peopleInvolvedBn = "মানসিকভাবে বিপর্যস্ত বান্দা",
            peopleInvolvedEn = "Distressed Servant",
            curatedArabicText = "اللَّهُمَّ إِنِّي عَبْدُكَ، ابْنُ عَبْدِكَ، ابْنُ أَمَتِكَ، نَاصِيَتِي بِيَدِكَ، مَاضٍ فِيَّ حُكْمُكَ، عَدْلٌ فِيَّ قَضَاؤُكَ، أَسْأَلُكَ بِكُلِّ اسْمٍ هُوَ لَكَ... أَنْ تَجْعَلَ الْقُرْآنَ رَبِيعَ قَلْبِي، وَنُورَ صَدْرِي، وَجَلاَءَ حُزْنِي، وَذَهَابَ هَمِّي",
            curatedTranslationBn = "হে আল্লাহ! নিশ্চয়ই আমি আপনার বান্দা, আপনার বান্দার সন্তান এবং আপনার বাঁদীর সন্তান। আমার কপাল আপনারই হাতে, আমার ওপর আপনার নির্দেশ কার্যকর, আমার ব্যাপারে আপনার ফয়সালা ন্যায়সংগত। আমি আপনার সেই সমস্ত পবিত্র নামের ওসীলায় প্রার্থনা করছি... আপনি কুরআনকে আমার হৃদয়ের বসন্ত, আমার বক্ষের জ্যোতি, আমার দুঃখের অপসারক এবং আমার দুশ্চিন্তার অবসানকারী বানিয়ে দিন।",
            curatedTranslationEn = "O Allah, I am Your servant, son of Your servant, son of Your maidservant. My forelock is in Your hand, Your command over me is forever executed, and Your decree over me is just. I ask You by every name belonging to You... to make the Quran the spring of my heart, the light of my chest, the remover of my sadness, and the reliever of my distress.",
            whySelectedBn = "বিষণ্নতা ও দুশ্চিন্তা দূরীকরণে রাসুলুল্লাহ ﷺ-এর শপথযুক্ত সেই বিখ্যাত সহীহ দু'আ (মুসনাদে আহমাদ ৩৭১২) যা পাঠ করলে আল্লাহ মনোদুঃখ দূর করে প্রশান্তি দান করেন।",
            whySelectedEn = "Features the renowned Prophetic prayer for distress (Musnad Ahmad 3712 / Sahih Ibn Hibban 972) that turns deep grief into relief through the light of the Quran.",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_anxiety_1",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "কুরআনকে অন্তরের বসন্ত বানানোর সহীহ দু'আ",
                    titleEn = "Prayer for Making the Quran Spring of the Heart",
                    referenceText = "মুসনাদে আহমাদ ৩৭১২, সহীহ ইবনে হিব্বান ৯৭২",
                    hadithBookBn = "মুসনাদে আহমাদ ও সহীহ ইবনে হিব্বান",
                    hadithBookEn = "Musnad Ahmad & Sahih Ibn Hibban",
                    hadithNumber = "মুসনাদে আহমাদ ৩৭১২",
                    narratorCompanionBn = "আবদুল্লাহ ইবনে মাসউদ (রা.)",
                    gradingBn = "সহীহ (শায়খ আলবানী সিলসিলাহ সহীহাহ ১৯৯)",
                    arabicSourceText = "اللَّهُمَّ إِنِّي عَبْدُكَ، ابْنُ عَبْدِكَ، ابْنُ أَمَتِكَ، نَاصِيَتِي بِيَدِكَ، مَاضٍ فِيَّ حُكْمُكَ، عَدْلٌ فِيَّ قَضَاؤُكَ... أَنْ تَجْعَلَ الْقُرْآنَ رَبِيعَ قَلْبِي، وَنُورَ صَدْرِي، وَجَلاَءَ حُزْنِي، وَذَهَابَ هَمِّي",
                    translationBn = "হে আল্লাহ! কুরআনকে আমার হৃদয়ের বসন্ত, বক্ষের আলো, দুঃখের অপসারক ও দুশ্চিন্তার সমাপ্তি বানিয়ে দিন...",
                    translationEn = "Make the Quran the spring of my heart, the light of my chest...",
                    pronunciationBn = "আল্লাহুম্মা ইন্নী 'আবদুকা, ইবনু 'আবদিকা, ইবনু আমাতিকা...",
                    sunnahPracticeMethodBn = "রাসুলুল্লাহ ﷺ বলেছেন: যে কোনো ব্যক্তি যখন বিষণ্নতায় পড়ে এই দো'আ পাঠ করবে, আল্লাহ তার বিষণ্নতা দূর করে আনন্দ এনে দেবেন।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_anxiety_1",
                    surahNameBn = "আর-রা'দ",
                    surahNameAr = "الرعد",
                    surahNumber = 13,
                    ayahNumber = 28,
                    arabicText = "الَّذِينَ آمَنُوا وَتَطْمَئِنُّ قُلُوبُهُم بِذِكْرِ اللَّهِ ۗ أَلَا بِذِكْرِ اللَّهِ تَطْمَئِنُّ الْقُلُوبُ",
                    banglaPronunciation = "আল্লাযীনা আমানূ ওয়া তাত্বমাইন্নু ক্বুলূবুহুম বিযিকরিল্লাহ; আলা বিযিকরিল্লাহি তাত্বমাইন্নুল ক্বুলূব।",
                    banglaTranslation = "যারা ঈমান এনেছে এবং আল্লাহর স্মরণে যাদের চিত্ত প্রশান্ত হয়; জেনে রেখো, আল্লাহর স্মরণেই কেবল অন্তরসমূহ শান্তি পায়।",
                    englishTranslation = "Those who have believed and whose hearts are assured by the remembrance of Allah. Unquestionably, by the remembrance of Allah hearts are assured.",
                    tafsirContextBn = "মানসিক ব্যাকুলতা দূর করে অন্তরে প্রশান্তির প্রলেপ দেওয়ার ঐশী বার্তা।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_anxiety_1",
                    hadithBookBn = "মুসনাদে আহমাদ",
                    hadithBookEn = "Musnad Ahmad",
                    hadithNumber = "৩৭১২",
                    narratorCompanionBn = "ইবনে মাসউদ (রা.)",
                    gradingBn = "সহীহ",
                    arabicText = "اللَّهُمَّ إِنِّي عَبْدُكَ... أَنْ تَجْعَلَ الْقُرْآنَ رَبِيعَ قَلْبِي...",
                    banglaPronunciation = "আল্লাহুম্মা ইন্নী 'আবদুকা...",
                    banglaTranslation = "হে আল্লাহ! কুরআনকে আমার অন্তরের বসন্ত বানিয়ে দিন...",
                    englishTranslation = "O Allah, make the Quran the spring of my heart...",
                    sunnahPracticeMethodBn = "অস্থিরতা ও মনোদুঃখে পাঠ্য।",
                    occasionOfUsageBn = "উদ্বেগ ও বিষণ্নতার সময়।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_anxiety_1",
                    titleBn = "অন্তরের অশান্তি ও ভয় দূর করার আকুল মিনতি",
                    heartfeltSupplicationBn = "হে আল্লাহ! আমার মন খুব অস্থির ও ভারাক্রান্ত। আপনি আমার অন্তরে সুকীনাহ ও মানসিক স্বস্তি নামিয়ে দিন। শয়তানের অহেতুক ওয়াসওয়াসা থেকে আমাকে রক্ষা করুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া সালাম (হে শান্তিদানকারী)", "ইয়া মুহাইমিন (হে পরম অভিভাবক)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_anxiety_1",
                    timingTitleBn = "রাত গভীরে ঘুমানোর পূর্বে এবং সালাতে",
                    timingDescriptionBn = "উযু অবস্থায় বিছানায় শুয়ে এবং সালাতে দীর্ঘ সিজদায় কাতর প্রার্থনা।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ কোনো বিষয়ে চিন্তিত হলে তৎক্ষণাৎ সালাতে দাঁড়িয়ে যেতেন (সুনানে আবু দাউদ ১৩১৯)।",
                    practicalTipBn = "মন খারাপ লাগলেই উযু করে দুই রাকাত নফল সালাতে দাঁড়িয়ে যান।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "মানসিক স্বাস্থ্য ও চিকিৎসকের পরামর্শ",
                    dominantScholarlyPositionBn = "দু'আ ও যিকিরের পাশাপাশি প্রয়োজনে অভিজ্ঞ ক্লিনিক্যাল সাইকোলজিস্ট বা চিকিৎসকের শরণাপন্ন হওয়া সুন্নাহর পরিপন্থী নয়।",
                    supportingEvidenceBn = "ইসলাম রোগ ও নিরাময়ের পূর্ণ সমন্বয়ে বিশ্বাস করে; শারীরিক বা মানসিক অসুস্থতায় উপযুক্ত চিকিৎসা গ্রহণ বৈধ।",
                    consensusOrNuanceBn = "যিকির অন্তরের খোরাক, পাশাপাশি প্রয়োজন অনুযায়ী চিকিৎসকের বৈজ্ঞানিক থেরাপি গ্রহণ করা উচিত।"
                )
            ),
            practicalRemindersBn = listOf(
                "প্রতিদিন নিয়ম করে কিছু সময় পবিত্র কুরআন অর্থসহ তিলাওয়াত করুন বা শুনুন।",
                "সোশ্যাল মিডিয়ায় অতিরিক্ত সময় কাটানো এবং নেতিবাচক খবর দেখা থেকে বিরত থাকুন।",
                "খোলা বাতাসে বা প্রকৃতির সান্নিধ্যে কিছুক্ষণ হাঁটাহাঁটি করুন।"
            )
        ),

        // ==========================================
        // 9. DIFFICULT TIME & PATIENCE
        // ==========================================
        "difficult_trial" to PersonalDuaBlueprint(
            id = "difficult_trial",
            userQuery = "I am going through a difficult time.",
            scenarioTitleBn = "কঠিন পরীক্ষা, সংকট থেকে মুক্তি ও ধৈর্য ধারণ",
            scenarioTitleEn = "Trials, Hardship & Seeking Patience",
            spiritualComfortBn = "বিপদ-আপদ মুমিনের জীবনের অপরিহার্য অধ্যায়। আল্লাহ তা'আলা বলেন: 'নিশ্চয়ই কষ্টের সাথেই রয়েছে স্বস্তি, নিঃসন্দেহে কষ্টের সাথেই রয়েছে স্বস্তি' (সূরা আল-ইনশিরাহ ৯৪:৫-৬)। ইউনুস (আ.) যখন মাছের পেটে ঘোর অন্ধকারে নিপতিত হয়েছিলেন, তখন তাঁর দোয়ায়ে ইউনুসের উসীলায় আল্লাহ তাঁকে উদ্ধার করেছিলেন। রাসুলুল্লাহ ﷺ বলেছেন: এই দো'আ পাঠ করে কোনো মুসলিম বিপদে ডাকলে আল্লাহ অবশ্যই তার ডাকে সাড়া দেন (তিরমিযী ৩৫০৫)।",
            spiritualComfortEn = "Verily with hardship comes ease (Surah Ash-Sharh 94:5-6). Prophet Yunus was saved from the depth of darkness when he called upon Allah. No believer calls upon Allah with Yunus's prayer in distress except that Allah relieves him.",
            detectedIntentBn = "সংকট থেকে আসমানী নাজাত ও সবর",
            detectedIntentEn = "Deliverance from crisis and granting of patience",
            emotionalContextBn = "চরম অসহায়ত্ব, সংকট ও মুক্তির প্রত্যাশা",
            emotionalContextEn = "Helplessness, distress & yearning for divine deliverance",
            peopleInvolvedBn = "বিপদগ্রস্ত বান্দা",
            peopleInvolvedEn = "Tested Servant",
            curatedArabicText = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ ۝ إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي، وَأَخْلِفْ لِي خَيْرًا مِنْهَا",
            curatedTranslationBn = "আপনি ছাড়া কোনো সত্য ইলাহ নেই, আপনি অতি পবিত্র! নিশ্চয়ই আমি অপরাধীদের অন্তর্ভুক্ত হয়ে গিয়েছিলাম। নিশ্চয়ই আমরা আল্লাহর জন্যই এবং আমরা তাঁরই দিকে প্রত্যাবর্তনকারী। হে আল্লাহ! আমার এই বিপদে আমাকে প্রতিদান দিন এবং এর চেয়েও উত্তম বিকল্প আমাকে দান করুন।",
            curatedTranslationEn = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers. Indeed to Allah we belong and to Him we shall return. O Allah, reward me in my affliction and grant me better in its place.",
            whySelectedBn = "মাছের পেটের গভীর অন্ধকারের দোয়ায়ে ইউনুস (সূরা আল-আম্বিয়া ২১:৮৭) এবং মুসিবতের সময়ে রাসুলুল্লাহ ﷺ-এর শেখানো উম্মে সালামাহ (রা.)-এর সহীহ দো'আ (সহীহ মুসলিম ৯১৮) একত্রিত করা হয়েছে।",
            whySelectedEn = "Unites the supplication of Prophet Yunus in the whale's belly (Surah Al-Anbiya 21:87) with the authentic Prophetic prayer during calamity taught to Umm Salamah (Sahih Muslim 918).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_trial_1",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "দোয়ায়ে ইউনুস (কঠিন বিপদ মুক্তির কুরআনী দো'আ)",
                    titleEn = "Dua of Yunus in Calamity",
                    referenceText = "আল-কুরআন, সূরা আল-আম্বিয়া (২১:৮৭)",
                    surahNameBn = "আল-আম্বিয়া",
                    surahNameEn = "Al-Anbiya",
                    surahNameAr = "الأنبياء",
                    surahNumber = 21,
                    ayahNumber = 87,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
                    translationBn = "আপনি ছাড়া কোনো সত্য ইলাহ নেই, আপনি অতি পবিত্র! নিশ্চয়ই আমি জালিমদের (অপরাধীদের) অন্তর্ভুক্ত ছিলাম।",
                    translationEn = "There is no deity except You; exalted are You. Indeed, I have been of the wrongdoers.",
                    pronunciationBn = "লা- ইলা-হা ইল্লা- আনতা সুবহা-নাকা ইন্নী কুন্তু মিনায যোয়া-লিমীন।",
                    tafsirContextBn = "রাসুলুল্লাহ ﷺ বলেছেন: মাছের পেটে ইউনুস (আ.) যে দো'আ করেছিলেন, যে কোনো বিপদে কোনো মুসলিম এই দো'আ পড়লে আল্লাহ তা কবুল করেন (তিরমিযী ৩৫০৫)।"
                ),
                DuaSourceMapItem(
                    id = "src_trial_2",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "বিপদে উত্তম ক্ষতিপূরণ লাভের সহীহ নববী দো'আ",
                    titleEn = "Prayer for Calamity and Better Replacement",
                    referenceText = "সহীহ মুসলিম ৯১৮",
                    hadithBookBn = "সহীহ মুসলিম",
                    hadithBookEn = "Sahih Muslim",
                    hadithNumber = "৯১৮",
                    narratorCompanionBn = "উম্মে সালামাহ (রা.)",
                    gradingBn = "সহীহ মুসলিম (সর্বোচ্চ প্রামাণ্য)",
                    arabicSourceText = "إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي، وَأَخْلِفْ لِي خَيْرًا مِنْهَا",
                    translationBn = "নিশ্চয়ই আমরা আল্লাহর জন্য এবং আমরা তাঁরই কাছে ফিরে যাব। হে আল্লাহ! আমার এই মুসিবতে আমাকে সওয়াব দিন এবং এর চেয়ে উত্তম কিছু দিয়ে এর ক্ষতিপূরণ করে দিন।",
                    translationEn = "Indeed to Allah we belong and to Him we shall return. O Allah, reward me in my affliction and grant me better in its place.",
                    pronunciationBn = "ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রজি'ঊন, আল্লাহুম্মা'জুরনী ফী মুসীবাতী ওয়া আখলিফ লী খাইরাম মিনহা।",
                    sunnahPracticeMethodBn = "যেকোনো বিপদ, শোক বা ক্ষতির সম্মুখীন হলে পাঠ্য।"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_trial_1",
                    surahNameBn = "আল-আম্বিয়া",
                    surahNameAr = "الأنبياء",
                    surahNumber = 21,
                    ayahNumber = 87,
                    arabicText = "لَّا إِلَٰهَ إِلَّا أَنتَ سُبْحَانَكَ إِنِّي كُنتُ مِنَ الظَّالِمِينَ",
                    banglaPronunciation = "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নী কুন্তু মিনায যোয়ালিমীন।",
                    banglaTranslation = "আপনি ছাড়া কোনো সত্য উপাস্য নেই, আপনি পবিত্র...",
                    englishTranslation = "There is no deity except You; exalted are You...",
                    tafsirContextBn = "বিপদ মুক্তির শ্রেষ্ঠ কুরআনী স্বীকৃতি।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_trial_1",
                    hadithBookBn = "সহীহ মুসলিম",
                    hadithBookEn = "Sahih Muslim",
                    hadithNumber = "৯১৮",
                    narratorCompanionBn = "উম্মে সালামাহ (রা.)",
                    gradingBn = "সহীহ মুসলিম",
                    arabicText = "إِنَّا لِلَّهِ وَإِنَّا إِلَيْهِ رَاجِعُونَ، اللَّهُمَّ أْجُرْنِي فِي مُصِيبَتِي...",
                    banglaPronunciation = "ইন্না লিল্লাহি ওয়া ইন্না ইলাইহি রজি'ঊন...",
                    banglaTranslation = "হে আল্লাহ! আমার এই বিপদে প্রতিদান দিন...",
                    englishTranslation = "O Allah, reward me in my affliction...",
                    sunnahPracticeMethodBn = "বিপদে তাৎক্ষণিক পাঠ্য।",
                    occasionOfUsageBn = "যেকোনো মুসিবতের সময়ে।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_trial_1",
                    titleBn = "কঠিন সময়ে অবিচলতা ও আসমানী সাহায্যের ফরিয়াদ",
                    heartfeltSupplicationBn = "হে আল্লাহ! পরিস্থিতি আমার সাধ্যের বাইরে চলে গেছে। আপনি দুর্বল বান্দার সহায় হোন। আমার এই কঠিন সময়কে দ্রুত সহজতায় পরিণত করুন এবং আমাকে অধৈর্য হওয়া থেকে রক্ষা করুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া মুস্তাগীস (হে আশ্রয়প্রার্থীদের উদ্ধারকারী)", "ইয়া সাবূর (হে পরম ধৈর্যশীল)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_trial_1",
                    timingTitleBn = "বিপদের প্রথম আঘাতের সময় এবং রাতের তাহাজ্জুদে",
                    timingDescriptionBn = "সবরের আসল সময় হলো আঘাতের প্রথম মুহূর্তে ধৈর্য ধরা।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'প্রকৃত সবর হলো বিপদের প্রথম ধাক্কার সময়ে' (সহীহ বুখারী ১২৮৩)।",
                    practicalTipBn = "ক্ষতি বা খারাপ সংবাদ পাওয়ামাত্রই রাগ না করে 'ইন্না লিল্লাহ' পড়ে আল্লাহর ওপর সন্তুষ্ট থাকুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "বিপদে রবের ওপর অভিযোগ বনাম অশ্রু বিসর্জন",
                    dominantScholarlyPositionBn = "বিপদে স্বাভাবিকভাবে চোখে পানি আসা রহমতের লক্ষণ; তবে চিৎকার করে বিলাপ করা বা আল্লাহর ফয়সালা নিয়ে অসন্তোষ প্রকাশ করা হারাম।",
                    supportingEvidenceBn = "সন্তান ইবরাহীমের ইন্তেকালে রাসুলুল্লাহ ﷺ-এর চোখ দিয়ে পানি পড়ছিল। তিনি বললেন: 'চোখ অশ্রু ঝরায়, অন্তর ব্যথিত হয়, কিন্তু আমরা তা-ই বলি যাতে আমাদের রব সন্তুষ্ট হন' (সহীহ বুখারী ১৩০৩)।",
                    consensusOrNuanceBn = "হৃদয়ের কান্না বৈধ, কিন্তু মুখে কোনো ইসলামবিরোধী অভিযোগ আনা যাবে না।"
                )
            ),
            practicalRemindersBn = listOf(
                "মনে রাখবেন, দুনিয়ার কোনো পরীক্ষাই চিরস্থায়ী নয়; রাত যত গভীর হয় সকাল তত নিকটবর্তী হয়।",
                "কষ্টের সময় ধৈর্য ধারণকারীদের জন্য বেহিসাব প্রতিদানের সুসংবাদ রয়েছে (সূরা আয-যুমার ৩৯:১০)।",
                "বিপদে অন্যকে অভিশাপ না দিয়ে নিজের আমল পর্যালোচনা করে আল্লাহর কাছে ক্ষমা চান।"
            )
        ),

        // ==========================================
        // 10. GUIDANCE & DIRECTION
        // ==========================================
        "guidance_direction" to PersonalDuaBlueprint(
            id = "guidance_direction",
            userQuery = "I want guidance.",
            scenarioTitleBn = "হিদায়াতের আলো, সঠিক সিদ্ধান্ত ও দ্বীনের ওপর অবিচলতা",
            scenarioTitleEn = "Guidance, Right Direction & Steadfastness",
            spiritualComfortBn = "হিদায়াত হলো দুনিয়ার সবচেয়ে বড় নিয়ামত, যা আল্লাহ তাঁর প্রিয় বান্দাদের দান করেন। এজন্যই আমরা প্রতিদিন সালাতের প্রতি রাকাতে সূরা ফাতিহায় বলি: 'ইহদিনাস সিরাতাল মুস্তাক্বীম' (আমাদের সরল সঠিক পথ প্রদর্শন করুন)। আপনি যখন দিকভ্রান্ত বোধ করছেন, আল্লাহর কাছে অন্তরকে সত্যের ওপর স্থির রাখার দু'আ করুন। রাসুলুল্লাহ ﷺ সর্বাধিক যে দু'আ করতেন তা হলো অন্তরকে দ্বীনের ওপর প্রতিষ্ঠিত রাখার দু'আ।",
            spiritualComfortEn = "Guidance is the greatest treasure a soul can receive. That is why we repeat in every single unit of prayer: 'Guide us to the straight path'. When faced with confusion, turn to the Turner of Hearts.",
            detectedIntentBn = "সঠিক পথের দিশা ও অন্তরের অবিচলতা লাভ",
            detectedIntentEn = "Steadfast guidance, moral clarity and divine direction",
            emotionalContextBn = "সংশয়, দ্বিধা ও সঠিক পথের ব্যাকুলতা",
            emotionalContextEn = "Confusion, crossroads in life and yearning for truth",
            peopleInvolvedBn = "পথের দিশা সন্ধানী বান্দা",
            peopleInvolvedEn = "Seeker of Truth",
            curatedArabicText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ۝ يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ ۝ اللَّهُمَّ اهْدِنِي وَسَدِّدْنِي",
            curatedTranslationBn = "আমাদেরকে সরল সঠিক পথ প্রদর্শন করুন। হে অন্তরসমূহের পরিবর্তনকারী! আমার অন্তরকে আপনার দ্বীনের ওপর অবিচল ও দৃঢ় রাখুন। হে আল্লাহ! আমাকে সঠিক পথ প্রদর্শন করুন এবং আমাকে সঠিক পথে সুদৃঢ় রাখুন।",
            curatedTranslationEn = "Guide us to the straight path. O Turner of the hearts, make my heart firm upon Your religion. O Allah, guide me and keep me upright.",
            whySelectedBn = "সূরা আল-ফাতিহার শ্রেষ্ঠ হেদায়াতের আরজি (১:৬), রাসুলুল্লাহ ﷺ-এর সর্বাধিক পঠিত অন্তর সুদৃঢ় রাখার দু'আ (তিরমিযী ২১৪০) এবং হেদায়েত ও দৃঢ়তার সহীহ দু'আ (সহীহ মুসলিম ২৭২৫) সংকলিত হয়েছে।",
            whySelectedEn = "Combines Surah Al-Fatihah's core prayer for guidance (1:6) with the Prophet's most frequent prayer for steadfastness of the heart (Jami at-Tirmidhi 2140) and prayer for upright guidance (Sahih Muslim 2725).",
            sources = listOf(
                DuaSourceMapItem(
                    id = "src_guide_1",
                    sourceType = DuaSourceType.QURAN,
                    titleBn = "সরল সঠিক পথের মূল কুরআনী দু'আ",
                    titleEn = "The Ultimate Quranic Prayer for Guidance",
                    referenceText = "আল-কুরআন, সূরা আল-ফাতিহা (১:৬)",
                    surahNameBn = "আল-ফাতিহা",
                    surahNameEn = "Al-Fatihah",
                    surahNameAr = "الفاتحة",
                    surahNumber = 1,
                    ayahNumber = 6,
                    gradingBn = "কুরআনে কারীমের আয়াত (Divine Revelation)",
                    arabicSourceText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                    translationBn = "আমাদেরকে সরল সঠিক পথ প্রদর্শন করুন।",
                    translationEn = "Guide us to the straight path.",
                    pronunciationBn = "ইহদিনাস সিরাতাল মুস্তাক্বীম।",
                    tafsirContextBn = "উম্মুল কিতাব সূরা ফাতিহার মূল কেন্দ্রীয় প্রার্থনা।"
                ),
                DuaSourceMapItem(
                    id = "src_guide_2",
                    sourceType = DuaSourceType.HADITH,
                    titleBn = "অন্তর অবিচল রাখার সর্বাধিক পঠিত সহীহ দু'আ",
                    titleEn = "Prophet's Most Frequent Prayer for Steadfastness",
                    referenceText = "জামে তিরমিযী ২১৪০, সুনানে ইবনে মাজাহ ৩৮৩৪",
                    hadithBookBn = "জামে আত-তিরমিযী ও সুনানে ইবনে মাজাহ",
                    hadithBookEn = "Jami at-Tirmidhi & Sunan Ibn Majah",
                    hadithNumber = "তিরমিযী ২১৪০",
                    narratorCompanionBn = "উম্মুল মুমিনীন উম্মে সালামাহ (রা.) ও আনাস (রা.)",
                    gradingBn = "সহীহ (Authentic)",
                    arabicSourceText = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ",
                    translationBn = "হে অন্তরসমূহের পরিবর্তনকারী! আমার অন্তরকে আপনার দ্বীনের ওপর সুদৃঢ় রাখুন।",
                    translationEn = "O Turner of the hearts, make my heart firm upon Your religion.",
                    pronunciationBn = "ইয়া মুক্বাল্লিবাল ক্বুলূব, ছাব্বিত ক্বলবী 'আলা দীনিক।",
                    sunnahPracticeMethodBn = "উম্মে সালামাহ (রা.) রাসুলুল্লাহ ﷺ-কে জিজ্ঞেস করেছিলেন, 'আপনি এই দোয়াটি এত বেশি কেন পড়েন?' তিনি বললেন, 'প্রতিটি মানুষের অন্তর আল্লাহর দুই আঙ্গুলের মাঝে রয়েছে, তিনি যেভাবে ইচ্ছা তা পরিবর্তন করেন।' (তিরমিযী)"
                )
            ),
            quranicDuas = listOf(
                QuranicDuaItem(
                    id = "q_guide_1",
                    surahNameBn = "আল-ফাতিহা",
                    surahNameAr = "الفاتحة",
                    surahNumber = 1,
                    ayahNumber = 6,
                    arabicText = "اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ",
                    banglaPronunciation = "ইহদিনাস সিরাতাল মুস্তাক্বীম।",
                    banglaTranslation = "আমাদের সরল সঠিক পথ প্রদর্শন করুন।",
                    englishTranslation = "Guide us to the straight path.",
                    tafsirContextBn = "প্রতিদিন সালাতে বান্দার প্রধান প্রার্থনা।"
                )
            ),
            propheticDuas = listOf(
                PropheticDuaItem(
                    id = "p_guide_1",
                    hadithBookBn = "জামে আত-তিরমিযী",
                    hadithBookEn = "Jami at-Tirmidhi",
                    hadithNumber = "২১৪০",
                    narratorCompanionBn = "আনাস (রা.)",
                    gradingBn = "সহীহ",
                    arabicText = "يَا مُقَلِّبَ الْقُلُوبِ ثَبِّتْ قَلْبِي عَلَى دِينِكَ",
                    banglaPronunciation = "ইয়া মুক্বাল্লিবাল ক্বুলূব, ছাব্বিত ক্বলবী 'আলা দীনিক।",
                    banglaTranslation = "হে অন্তর পরিবর্তনকারী! আমার অন্তরকে আপনার দ্বীনে দৃঢ় রাখুন।",
                    englishTranslation = "O Turner of hearts, make my heart firm upon Your religion.",
                    sunnahPracticeMethodBn = "সালাতের পর ও দৈনন্দিন যেকোনো সময় পাঠ্য।",
                    occasionOfUsageBn = "দ্বীনের ওপর অটল থাকতে।"
                )
            ),
            generalSupplications = listOf(
                PermissibleSupplicationItem(
                    id = "g_guide_1",
                    titleBn = "সংশয় থেকে মুক্ত হয়ে সত্য চেনার আরজি",
                    heartfeltSupplicationBn = "হে আল্লাহ! সত্যকে সত্য হিসেবে চিনতে পারার এবং তা অনুসরণ করার তাওফিক দিন; আর মিথ্যা ও বিভ্রান্তিকে চিনে তা থেকে বেঁচে থাকার শক্তি দিন। আমার প্রতিটি সিদ্ধান্তে আপনার সন্তুষ্টি দান করুন।",
                    invokedNamesOfAllahBn = listOf("ইয়া হাদি (হে পথপ্রদর্শক)", "ইয়া নূর (হে জ্যোতির্ময় আলো)")
                )
            ),
            goldenTimingsAndEtiquettes = listOf(
                DuaEtiquetteItem(
                    id = "e_guide_1",
                    timingTitleBn = "তাহাজ্জুদ সালাতের সিজদায় ও সালাতুল ইস্তিখারায়",
                    timingDescriptionBn = "রাতে উঠে ইস্তিখারার বিশেষ দো'আ পাঠ করে সিদ্ধান্ত চাওয়া।",
                    hadithEvidenceBn = "রাসুলুল্লাহ ﷺ বলেছেন: 'যখন তোমাদের কেউ কোনো গুরুত্বপূর্ণ কাজের সংকল্প করে, সে যেন দুই রাকাত নফল সালাত আদায় করে ইস্তিখারার দো'আ পাঠ করে' (সহীহ বুখারী ১১৬২)।",
                    practicalTipBn = "যে কোনো সিদ্ধান্ত নেওয়ার আগে দুই রাকাত সালাতুল ইস্তিখারা আদায় করুন।"
                )
            ),
            scholarlyClarifications = listOf(
                ScholarlyOpinionItem(
                    issueTitleBn = "ইস্তিখারার পরে স্বপ্নের ওপর নির্ভরতা",
                    dominantScholarlyPositionBn = "ইস্তিখারা করার পর কোনো বিশেষ স্বপ্ন দেখা জরুরি নয়; বরং মন যে কাজের প্রতি প্রশান্ত হয় এবং যে পথ সহজ হয় সেদিকে অগ্রসর হওয়াই ইস্তিখারার ফলাফল।",
                    supportingEvidenceBn = "ইমাম নববী ও ইবনে হাজার (রহ.) উল্লেখ করেছেন, ইস্তিখারার পর কাজে বরকত হওয়া এবং আল্লাহ সে পথ উন্মুক্ত রাখাই কবুলিয়তের আলামত।",
                    consensusOrNuanceBn = "স্বপ্নে লাল বা সবুজ রঙ দেখার ধারণা ভিত্তিহীন।"
                )
            ),
            practicalRemindersBn = listOf(
                "দ্বীনদার অভিজ্ঞ আলেম বা বিশ্বস্ত মুরুব্বিদের সাথে পরামর্শ (মাশওয়ারা) করুন।",
                "নিয়মিত কুরআন তিলাওয়াত করুন, কারণ কুরআনই হেদায়াতের প্রধান উৎস।",
                "পাপের কাজ থেকে বেঁচে থাকুন, কারণ পাপ হেদায়াতের নূরকে ঢেকে দেয়।"
            )
        )
    )

    /**
     * Resolves matching blueprint using offline semantic analysis and keyword matching.
     */
    fun findBestMatch(userPrompt: String): PersonalDuaBlueprint {
        val clean = userPrompt.lowercase().trim()
        if (clean.isBlank()) return blueprints.values.first()

        // 1. Direct check against presets
        for (preset in presets) {
            for (kw in preset.keywords) {
                if (clean.contains(kw.lowercase())) {
                    blueprints[preset.id]?.let { return it }
                }
            }
        }

        // 2. Specific semantic matching fallbacks
        when {
            clean.contains("future") || clean.contains("ভবিষ্যত") || clean.contains("ভবিষ্যৎ") || clean.contains("career") ->
                return blueprints["future_worry"] ?: blueprints.values.first()

            clean.contains("sick") || clean.contains("ill") || clean.contains("health") ||
            clean.contains("বাবা") || clean.contains("মা") || clean.contains("পিতা") || clean.contains("মাতা") ||
            clean.contains("অসুস্থ") || clean.contains("রোগ") || clean.contains("father") || clean.contains("mother") ->
                return blueprints["parent_illness"] ?: blueprints.values.first()

            clean.contains("study") || clean.contains("exam") || clean.contains("test") || clean.contains("interview") ||
            clean.contains("পরীক্ষা") || clean.contains("পড়াশোনা") || clean.contains("মেধা") || clean.contains("স্মৃতি") ->
                return blueprints["studies_exams"] ?: blueprints.values.first()

            clean.contains("job") || clean.contains("unemployed") || clean.contains("কর্মসংস্থান") || clean.contains("চাকরি") || clean.contains("বেকার") ->
                return blueprints["job_unemployment"] ?: blueprints.values.first()

            clean.contains("debt") || clean.contains("loan") || clean.contains("ঋণ") || clean.contains("টাকা") || clean.contains("আর্থিক") || clean.contains("financial") ->
                return blueprints["debt_finance"] ?: blueprints.values.first()

            clean.contains("marry") || clean.contains("marriage") || clean.contains("spouse") || clean.contains("বিয়ে") || clean.contains("দাম্পত্য") || clean.contains("স্বামী") || clean.contains("স্ত্রী") ->
                return blueprints["marriage_spouse"] ?: blueprints.values.first()

            clean.contains("sin") || clean.contains("forgive") || clean.contains("পাপ") || clean.contains("গুনাহ") || clean.contains("ক্ষমা") || clean.contains("তাওবা") || clean.contains("repent") ->
                return blueprints["forgiveness_repentance"] ?: blueprints.values.first()

            clean.contains("anxious") || clean.contains("anxiety") || clean.contains("depress") || clean.contains("হতাশ") || clean.contains("বিষণ্ণ") || clean.contains("অস্থির") || clean.contains("অশান্তি") ->
                return blueprints["anxiety_mental"] ?: blueprints.values.first()

            clean.contains("difficult") || clean.contains("hardship") || clean.contains("বিপদ") || clean.contains("সবর") || clean.contains("কষ্ট") || clean.contains("সংকট") ->
                return blueprints["difficult_trial"] ?: blueprints.values.first()

            clean.contains("guide") || clean.contains("guidance") || clean.contains("হেদায়েত") || clean.contains("হিদায়াত") || clean.contains("দিশা") ->
                return blueprints["guidance_direction"] ?: blueprints.values.first()
        }

        // Return first default rich blueprint
        return blueprints.values.first()
    }
}
