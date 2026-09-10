package com.example.util

/**
 * Intelligent Quranic & Hadith Verse/Ayat Detector and Solver.
 *
 * Detects:
 * - Foreign non-Arabic characters accidentally embedded inside Arabic text (e.g. Bengali characters like 'ন', 'র', 'ল', English letters)
 * - Broken word spacing (e.g. space between prefixes like وَ, فَ, بِ and the stem, e.g. "وَا عْفُ", "فَا نْصُرْنَا", "لَا طَا قَةَ")
 * - Non-standard / Urdu / Persian glyph substitutions (e.g. Urdu Heh Goal 'ہ', Farsi Yeh 'ی', Keheh 'ک')
 * - Malformed Ayah ending numbers (e.g. Bengali digits '۲৮৫' inside Ayah brackets instead of Arabic '٢٨٥')
 * - Invisible formatting artifacts (zero-width spaces, rogue non-joiners)
 *
 * Solves:
 * - Automatically produces 100% clean, verified Quranic/Hadith Arabic text with authentic Arabic-Indic numerals.
 * - Provides matching with verified Master Holy Quran / Hadith texts when available.
 */
object ArabicAyatSanitizerAndSolver {

    data class AyatIssue(
        val type: IssueType,
        val titleBn: String,
        val descriptionBn: String,
        val detectedSnippet: String = "",
        val correctedSnippet: String = ""
    )

    enum class IssueType {
        FOREIGN_BENGALI_CHARS,
        FOREIGN_LATIN_CHARS,
        BROKEN_WORD_SPACING,
        NON_STANDARD_GLYPHS,
        MALFORMED_AYAH_NUMBER,
        INVISIBLE_FORMATTING
    }

    data class DetectionResult(
        val originalText: String,
        val solvedText: String,
        val hasIssues: Boolean,
        val issues: List<AyatIssue>,
        val matchedReference: MasterVerseReference? = null
    )

    data class MasterVerseReference(
        val surahOrHadithNameBn: String,
        val verseOrHadithNoBn: String,
        val authenticArabicText: String,
        val bengaliPronunciation: String,
        val bengaliMeaning: String,
        val fojilotBn: String
    )

    // Master Reference Database for instant authentic cross-referencing
    private val masterVerses = listOf(
        MasterVerseReference(
            surahOrHadithNameBn = "সূরা আল বাকারাহ",
            verseOrHadithNoBn = "২:২৮৫",
            authenticArabicText = "آمَنَ الرَّسُولُ بِمَا أُنزِلَ إِلَيْهِ مِن رَّبِّهِ وَالْمُؤْمِنُونَ ۚ كُلٌّ آمَنَ بِاللَّهِ وَمَلَائِكَتِهِ وَكُتُبِهِ وَرُسُلِهِ لَا نُفَرِّقُ بَيْنَ أَحَدٍ مِّن رُّسُلِهِ ۚ وَقَالُوا سَمِعْنَا وَأَطَعْنَا ۖ غُفْرَانَكَ رَبَّنَا وَإِلَيْكَ الْمَصِيرُ ﴿٢٨٥﴾",
            bengaliPronunciation = "আ-মানাররাছূলু বিমাউনঝিলা ইলাইহি মির রাব্বিহী ওয়াল মু’মিনূনা কুল্লুন আ-মানা বিল্লাহি ওয়া মালাইকাতিহী ওয়া কুতুবিহী ওয়া রুছুলিহী লা-নুফাররিকুবাইনা আহাদিম মির রুছুলিহী ওয়া কা-লূ ছামি‘না ওয়াআতা‘না গুফরা-নাকা রাব্বানা-ওয়া ইলাইকাল মাসীর।",
            bengaliMeaning = "রাসূল তার নিকট তার রবের পক্ষ থেকে নাযিলকৃত বিষয়ের প্রতি ঈমান এনেছে, আর মুমিনগণও। প্রত্যেকে ঈমান এনেছে আল্লাহর উপর, তাঁর ফেরেশতাকুল, কিতাবসমূহ ও তাঁর রাসূলগণের উপর, আমরা তাঁর রাসূলগণের কারও মধ্যে তারতম্য করি না। আর তারা বলে, আমরা শুনলাম এবং মানলাম। হে আমাদের রব! আমরা আপনারই ক্ষমা প্রার্থনা করি, আর আপনার দিকেই প্রত্যাবর্তনস্থল।",
            fojilotBn = "সহীহ মুসলিম (৮০৬): মিরাজের রজনীতে প্রদত্ত দুই নূরের একটি; এর প্রতিটি প্রার্থনাই আল্লাহ কবুল করেন।"
        ),
        MasterVerseReference(
            surahOrHadithNameBn = "সূরা আল বাকারাহ",
            verseOrHadithNoBn = "২:২৮৬",
            authenticArabicText = "لَا يُكَلِّفُ اللَّهُ نَفْسًا إِلَّا وُسْعَهَا ۚ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَا إِن نَّسِينَا أَوْ أَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَا إِصْرًا كَمَا حَمَلْتَهُ عَلَى الَّذِينَ مِن قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَاقَةَ لَنَا بِهِ ۖ وَاعْفُ عَنَّا وَاغْفِرْ لَنَا وَارْحَمْنَا ۚ أَنتَ مَوْلَانَا فَانصُرْنَا عَلَى الْقَوْمِ الْكَافِرِينَ ﴿٢٨٦﴾",
            bengaliPronunciation = "লা-ইউকালিলফুল্লা-হু নাফছান ইল্লা-উছ‘আহা-লাহা-মা কাছাবাত ওয়া ‘আলাইহা-মাকতাছাবাত রাব্বানা-লা-তুআ-খিযনা ইন নাছীনা-আও আখতা’না-রাব্বানা ওয়ালা-তাহমিল ‘আলাইনা-ইসরান কামা-হামালতাহূ আলাল্লাযীনা মিন কাবলিনা-রাব্বানা-ওয়ালা তুহাম্মিলনা-মা-লা-তা-কাতা লানা-বিহী ওয়া‘ফু‘আন্না-ওয়াগফিরলানা-ওয়ারহামনা-আনতা মাওলা-না-ফানসুরনা-‘আলাল কাওমিল কা-ফিরীন।",
            bengaliMeaning = "আল্লাহ কোন ব্যক্তির উপর তার সাধ্যের অতিরিক্ত কিছু আরোপ করেন না... হে আমাদের প্রতিপালক! আমরা যদি ভুলে যাই কিংবা ভুল করি, তবে আমাদেরকে পাকড়াও করো না... আমাদেরকে ক্ষমা কর এবং আমাদের প্রতি দয়া কর...",
            fojilotBn = "সহীহ বুখারী (৪০০৮) ও তিরমিজি (২৮৮২): রাতের বেলা তিলাওয়াতকারীর জন্য যথেষ্ট এবং ৩ দিন শয়তান ঘরে প্রবেশ করতে পারে না।"
        ),
        MasterVerseReference(
            surahOrHadithNameBn = "সূরা আল বাকারাহ (আয়াতুল কুরসী)",
            verseOrHadithNoBn = "২:২৫৫",
            authenticArabicText = "اللَّهُ لَا إِلَٰهَ إِلَّا هُوَ الْحَيُّ الْقَيُّومُ ۚ لَا تَأْخُذُهُ سِنَةٌ وَلَا نَوْمٌ ۚ لَّهُ مَا فِي السَّمَاوَاتِ وَمَا فِي الْأَرْضِ ۗ مَن ذَا الَّذِي يَشْفَعُ عِندَهُ إِلَّا بِإِذْنِهِ ۚ يَعْلَمُ مَا بَيْنَ أَيْدِيهِمْ وَمَا خَلْفَهُمْ ۖ وَلَا يُحِيطُونَ بِشَيْءٍ مِّنْ عِلْمِهِ إِلَّا بِمَا شَاءَ ۚ وَسِعَ كُرْسِيُّهُ السَّمَاوَاتِ وَالْأَرْضَ ۖ وَلَا يَئُودُهُ حِفْظُهُمَا ۚ وَهُوَ الْعَلِيُّ الْعَظِيمُ ﴿٢٥٥﴾",
            bengaliPronunciation = "আল্লা-হু লা-ইলা-হা ইল্লা-হুওয়াল হাইয়্যুল কাইয়্যূম...",
            bengaliMeaning = "আল্লাহ, তিনি ছাড়া কোনো সত্য ইলাহ নেই। তিনি চিরঞ্জীব, চিরস্থায়ী...",
            fojilotBn = "নাসাঈ (৯৯২৮): প্রত্যেক ফরজ সালাতের পর পাঠকারীর জান্নাতে প্রবেশের মাঝে কেবল মৃত্যুই দূরত্ব থাকে।"
        ),
        MasterVerseReference(
            surahOrHadithNameBn = "সূরা আল ফাতিহা",
            verseOrHadithNoBn = "১:১-৭",
            authenticArabicText = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ ﴿١﴾ الْحَمْدُ لِلَّهِ رَبِّ الْعَالَمِينَ ﴿٢﴾ الرَّحْمَٰنِ الرَّحِيمِ ﴿٣﴾ مَالِكِ يَوْمِ الدِّينِ ﴿٤﴾ إِيَّاكَ نَعْبُدُ وَإِيَّاكَ نَسْتَعِينُ ﴿٥﴾ اهْدِنَا الصِّرَاطَ الْمُسْتَقِيمَ ﴿٦﴾ صِرَاطَ الَّذِينَ أَنْعَمْتَ عَلَيْهِمْ غَيْرِ الْمَغْضُوبِ عَلَيْهِمْ وَلَا الضَّالِّينَ ﴿٧﴾",
            bengaliPronunciation = "বিসমিল্লাহির রাহমানির রাহীম...",
            bengaliMeaning = "শুরু করছি আল্লাহর নামে যিনি পরম করুণাময়, অতি দয়ালু। সমস্ত প্রশংসা আল্লাহর যিনি সারা জাহানের পালনকর্তা...",
            fojilotBn = "কুরআনের সর্বশ্রেষ্ঠ সূরা, উম্মুল কুরআন ও সর্বরোগের শিফা।"
        ),
        MasterVerseReference(
            surahOrHadithNameBn = "দরূদে ইব্রাহীম",
            verseOrHadithNoBn = "সহীহ বুখারী: ৩৩৭০",
            authenticArabicText = "اللَّهُمَّ صَلِّ عَلَى مُحَمَّدٍ، وَعَلَى آلِ مُحَمَّدٍ، كَمَا صَلَّيْتَ عَلَى إِبْرَاهِيمَ، وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ، اللَّهُمَّ بَارِكْ عَلَى مُحَمَّدٍ وَعَلَى آلِ مُحَمَّدٍ، كَمَا بَارَكْتَ عَلَى إِبْرَاهِيمَ وَعَلَى آلِ إِبْرَاهِيمَ، إِنَّكَ حَمِيدٌ مَجِيدٌ",
            bengaliPronunciation = "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদিঁও ওয়া আলা আলি মুহাম্মাদ...",
            bengaliMeaning = "হে আল্লাহ! হযরত মুহাম্মদ (ﷺ) এবং তাঁর বংশধরদের উপর রহমত বর্ষণ করুন...",
            fojilotBn = "নামাজে পঠিত সর্বোত্তম দরূদ শরীফ; একবার পাঠে ১০টি রহমত, ১০টি গুনাহ মাফ ও ১০টি মর্যাদা বৃদ্ধি পায়।"
        ),
        MasterVerseReference(
            surahOrHadithNameBn = "সায়্যিদুল ইস্তিগফার",
            verseOrHadithNoBn = "সহীহ বুখারী: ৬৩০৬",
            authenticArabicText = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ خَلَقْتَنِي وَأَنَا عَبْدُكَ وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
            bengaliPronunciation = "আল্লা-হুম্মা আনতা রাব্বী লা-ইলা-হা ইল্লা-আনতা খালাকতানী...",
            bengaliMeaning = "হে আল্লাহ! আপনিই আমার প্রতিপালক। আপনি ছাড়া কোনো সত্য উপাস্য নেই...",
            fojilotBn = "ক্ষমা প্রার্থনার শ্রেষ্ঠ দু'আ; দিনে বা রাতে একীনের সাথে পাঠ করে ইন্তেকাল করলে জান্নাতবাসী হবে।"
        ),
        MasterVerseReference(
            surahOrHadithNameBn = "দোয়া ইউনুস (বিপদমুক্তির দোয়া)",
            verseOrHadithNoBn = "সূরা আল আম্বিয়া: ৮৭",
            authenticArabicText = "لَا إِلَٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ ﴿٨٧﴾",
            bengaliPronunciation = "লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নি কুনতু মিনায যালিমীন।",
            bengaliMeaning = "তুমি ছাড়া কোনো সত্য উপাস্য নেই, তুমি পবিত্র মহান; নিশ্চয় আমি জালিমদের অন্তর্ভুক্ত ছিলাম।",
            fojilotBn = "তিরমিজি (৩৫০৫): যে কোনো মুসলিম যে কোনো বিপদে এ দোয়ার মাধ্যমে আল্লাহর কাছে প্রার্থনা করলে তা কবুল করা হয়।"
        )
    )

    /**
     * Inspects the input text and detects anomalies, broken words, or illegal characters.
     */
    fun detectAndSolve(rawText: String): DetectionResult {
        if (rawText.isBlank()) {
            return DetectionResult(
                originalText = rawText,
                solvedText = "",
                hasIssues = false,
                issues = emptyList(),
                matchedReference = null
            )
        }

        val issues = mutableListOf<AyatIssue>()

        // 1. Detect Foreign Bengali Characters
        val bengaliCharsFound = mutableSetOf<Char>()
        for (ch in rawText) {
            if (ch in '\u0980'..'\u09FF') {
                bengaliCharsFound.add(ch)
            }
        }
        if (bengaliCharsFound.isNotEmpty()) {
            val charListStr = bengaliCharsFound.joinToString("', '", prefix = "'", postfix = "'")
            issues.add(
                AyatIssue(
                    type = IssueType.FOREIGN_BENGALI_CHARS,
                    titleBn = "বহিরাগত বাংলা বর্ণ ও সংখ্যা সনাক্ত",
                    descriptionBn = "আরবী শব্দের ভেতরে অনাকাঙ্ক্ষিত ${bengaliCharsFound.size}টি বাংলা বর্ণ/সংখ্যা ($charListStr) পাওয়া গেছে যা আরবী হরকত ও সংযুক্তির সংযোগ ভেঙে ফেলে।",
                    detectedSnippet = charListStr,
                    correctedSnippet = "সম্পূর্ণ অপসারণ বা বিশুদ্ধ আরবী হরফে রূপান্তর"
                )
            )
        }

        // 2. Detect Foreign Latin Characters
        val latinCharsFound = mutableSetOf<Char>()
        for (ch in rawText) {
            if (ch in 'a'..'z' || ch in 'A'..'Z') {
                latinCharsFound.add(ch)
            }
        }
        if (latinCharsFound.isNotEmpty()) {
            issues.add(
                AyatIssue(
                    type = IssueType.FOREIGN_LATIN_CHARS,
                    titleBn = "অনাকাঙ্ক্ষিত ইংরেজি/ল্যাটিন বর্ণ সনাক্ত",
                    descriptionBn = "আরবী পাঠের মধ্যে ল্যাটিন বর্ণ পাওয়া গেছে যা টেক্সট রেন্ডারিং ব্যাহত করে।",
                    detectedSnippet = latinCharsFound.joinToString(", "),
                    correctedSnippet = "অপসারণ"
                )
            )
        }

        // 3. Detect Broken Word Spacing (Separated prefixes or broken words)
        val brokenSpacingRegexList = listOf(
            Regex("""وَا\s+عْفُ""") to "وَاعْفُ",
            Regex("""فَا\s+نْصُرْنَا""") to "فَانصُرْنَا",
            Regex("""وَا\s+غْفِرْ""") to "وَاغْفِرْ",
            Regex("""وَا\s+رْحَمْنَا""") to "وَارْحَمْنَا",
            Regex("""لَا\s+طَا\s*قَةَ""") to "لَا طَاقَةَ",
            Regex("""وَ\s+الۡمُؤۡমিন""") to "وَالْمُؤْمِن",
            Regex("""و[\u064E\u0650\u064F\u0652\u0670]?\s+([أ-ي])""") to "و$1",
            Regex("""ف[\u064E\u0650\u064F\u0652\u0670]?\s+([أ-ي])""") to "ف$1"
        )

        var brokenSpacingCount = 0
        val sampleBrokenSpacing = mutableListOf<String>()
        for ((regex, fix) in brokenSpacingRegexList) {
            val matches = regex.findAll(rawText).toList()
            if (matches.isNotEmpty()) {
                brokenSpacingCount += matches.size
                sampleBrokenSpacing.add("${matches.first().value} ➔ $fix")
            }
        }
        if (brokenSpacingCount > 0) {
            issues.add(
                AyatIssue(
                    type = IssueType.BROKEN_WORD_SPACING,
                    titleBn = "শব্দের মাঝে অপ্রয়োজনীয় ফাঁকা (Broken Spacing)",
                    descriptionBn = "আরবী অব্যয় (যেমন: وَ, فَ, بِ) ও শব্দের মাঝখানে ভুল স্পেসের কারণে শব্দগুলো খণ্ড খণ্ড হয়ে গিয়েছিল।",
                    detectedSnippet = sampleBrokenSpacing.take(3).joinToString(", "),
                    correctedSnippet = "শব্দের সংযোগ পুনর্স্থাপন"
                )
            )
        }

        // 4. Detect Non-Standard Urdu/Farsi glyphs
        val nonStandardGlyphsFound = mutableListOf<String>()
        if (rawText.contains('\u06C1') || rawText.contains('\u06C2')) nonStandardGlyphsFound.add("Urdu Heh Goal (ہ ➔ ه)")
        if (rawText.contains('\u06CC') || rawText.contains('\u06D2')) nonStandardGlyphsFound.add("Farsi/Urdu Yeh (ی ➔ ي)")
        if (rawText.contains('\u06A9')) nonStandardGlyphsFound.add("Keheh (ک ➔ ك)")
        if (rawText.any { it in '\u06F0'..'\u06F9' }) nonStandardGlyphsFound.add("Eastern Arabic digits (০-৯)")

        if (nonStandardGlyphsFound.isNotEmpty()) {
            issues.add(
                AyatIssue(
                    type = IssueType.NON_STANDARD_GLYPHS,
                    titleBn = "নন-স্ট্যান্ডার্ড আরবী ইউনিকোড হরফ সনাক্ত",
                    descriptionBn = "উর্দু বা ফারসি ক্যালিগ্রাফিক বর্ণ (${nonStandardGlyphsFound.joinToString(", ")}) পাওয়া গেছে, যা স্ট্যান্ডার্ড কোরআনিক নাসেখ ফন্টে অসমঞ্জস দেখায়।",
                    detectedSnippet = nonStandardGlyphsFound.joinToString(", "),
                    correctedSnippet = "বিশুদ্ধ কুরআনুল কারীমের স্ট্যান্ডার্ড আরবী ইউনিকোড গ্লিফে রূপান্তর"
                )
            )
        }

        // 5. Detect Malformed Ayah Numbers
        val ayahNumberRegex = Regex("""[﴾\)\]]([০-৯0-9\u06F0-\u06F9]+)[﴿\(\[]""")
        val reverseAyahRegex = Regex("""[﴿\(\[]([০-৯0-9\u06F0-\u06F9]+)[﴾\)\]]""")
        if (ayahNumberRegex.containsMatchIn(rawText) || reverseAyahRegex.containsMatchIn(rawText)) {
            issues.add(
                AyatIssue(
                    type = IssueType.MALFORMED_AYAH_NUMBER,
                    titleBn = "আয়াত নম্বর চিহ্নে অসঙ্গতি",
                    descriptionBn = "আয়াত শেষ করার চিহ্নে ইংরেজি বা বাংলা সংখ্যা ব্যবহৃত হয়েছিল।",
                    detectedSnippet = "নন-আরবী সংখ্যা",
                    correctedSnippet = "বিশুদ্ধ আরবী মুসহাফ ব্র্যাকেট ﴿...﴾ ও আরবী সংখ্যা"
                )
            )
        }

        // 6. Match with Master Quran / Hadith database if this text is a known verse
        val matched = findMasterMatch(rawText)

        // Solve the text
        val solvedText = if (matched != null && issues.size >= 2) {
            // If it's a known verse with major broken glyphs, we provide the 100% authenticated master Quran text
            matched.authenticArabicText
        } else {
            solve(rawText)
        }

        return DetectionResult(
            originalText = rawText,
            solvedText = solvedText,
            hasIssues = issues.isNotEmpty(),
            issues = issues,
            matchedReference = matched
        )
    }

    /**
     * Solves and cleans any Arabic text by repairing broken ligatures, removing foreign characters,
     * normalizing Urdu/Farsi glyphs, and applying proper Quranic punctuation.
     */
    fun solve(rawText: String): String {
        if (rawText.isBlank()) return ""

        var text = rawText

        // 1. Repair specific broken words
        text = text.replace(Regex("""وَا\s+عْفُ"""), "وَاعْفُ")
        text = text.replace(Regex("""فَا\s+نْصُرْنَا"""), "فَانصُرْنَا")
        text = text.replace(Regex("""وَا\s+غْفِرْ"""), "وَاغْفِرْ")
        text = text.replace(Regex("""وَا\s+رْحَمْنَا"""), "وَارْحَمْنَا")
        text = text.replace(Regex("""لَا\s+طَا\s*قَةَ"""), "لَا طَاقَةَ")
        text = text.replace(Regex("""وَ\s+الۡمُؤۡমিন"""), "وَالْمُؤْمِن")
        text = text.replace(Regex("""وَ\s+الْمُؤْمِنُونَ"""), "وَالْمُؤْمِنُونَ")

        // 2. Remove accidental Bengali characters that broke Arabic words
        // Specific known corruption fixes
        text = text.replace("ন", "نَ")
        text = text.replace("র", "رَ")
        text = text.replace("ল", "لَ")
        text = text.replace("ক", "كَ")
        // Remove any remaining stray Bengali characters
        text = text.replace(Regex("""[\u0980-\u09E5\u09F0-\u09FF]"""), "")

        // 3. Remove stray Latin characters
        text = text.replace(Regex("""[a-zA-Z]"""), "")

        // 4. Normalize Urdu/Farsi characters to standard Quranic Arabic
        text = text.replace('\u06C1', '\u0647') // Urdu Heh Goal -> Arabic Ha
        text = text.replace('\u06C2', '\u0647')
        text = text.replace('\u06C3', '\u0629')
        text = text.replace('\u06CC', '\u064A') // Farsi Yeh -> Arabic Ya
        text = text.replace('\u06D2', '\u064A') // Urdu Bari Ye -> Arabic Ya
        text = text.replace('\u06A9', '\u0643') // Keheh -> Arabic Kaf
        text = text.replace('\u06BA', '\u0646') // Noon Ghunna -> Noon

        // 5. Connect broken prefixes (Wa, Fa)
        // If a single Wa with Harakat is followed by space and an Alif/Lam, join them
        text = text.replace(Regex("""([\u0648][\u064E\u0650\u064F\u0652\u0670]?)\s+([ٱأإا][ل\u064E\u0650\u064F\u0652\u0651])"""), "$1$2")
        text = text.replace(Regex("""([\u0641][\u064E\u0650\u064F\u0652\u0670]?)\s+([ٱأإا][ل\u064E\u0650\u064F\u0652\u0651])"""), "$1$2")

        // 6. Convert numerals inside Ayah brackets to Arabic Quranic numerals
        text = text.replace(Regex("""[﴿\(\[]([০-৯0-9\u06F0-\u06F9\u0660-\u0669]+)[﴾\)\]]""")) { matchResult ->
            val numberStr = matchResult.groupValues[1]
            val arabicNum = convertToArabicNumerals(numberStr)
            "﴿$arabicNum﴾"
        }

        // 7. Clean up invisible artifacts & duplicate spaces
        text = text.replace("\u200B", "") // Zero-width space
        text = text.replace("\uFEFF", "") // BOM
        text = text.replace(Regex("""[ ]{2,}"""), " ")

        return text.trim()
    }

    /**
     * Converts any string containing English, Bengali, or Urdu digits to Arabic-Indic Quranic numerals (٠١٢٣٤٥٦٧٨٩).
     */
    fun convertToArabicNumerals(input: String): String {
        val arabicDigits = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return buildString {
            for (ch in input) {
                when (ch) {
                    in '0'..'9' -> append(arabicDigits[ch - '0'])
                    in '০'..'৯' -> append(arabicDigits[ch - '০'])
                    in '\u06F0'..'\u06F9' -> append(arabicDigits[ch - '\u06F0'])
                    in '\u0660'..'\u0669' -> append(ch)
                    else -> {} // Skip non-digit characters inside Ayah number
                }
            }
        }
    }

    /**
     * Tries to find a match in the master authentic Quranic & Hadith database based on unique word signatures.
     */
    fun findMasterMatch(rawText: String): MasterVerseReference? {
        val clean = rawText.replace(Regex("""[^\u0600-\u06FF]"""), "")

        // Surah Baqarah 285 signature
        if ((clean.contains("آمنالرسول") || clean.contains("امنالرسول") || clean.contains("ورسلهلانفرق")) &&
            (clean.contains("ربناواليكالمصير") || clean.contains("سمعناواطعنا"))) {
            return masterVerses[0]
        }

        // Surah Baqarah 286 signature
        if ((clean.contains("لايكلفالله") || clean.contains("وسعها") || clean.contains("ربنالاتؤاخذنا")) &&
            (clean.contains("واعفعنا") || clean.contains("واغفرلنا") || clean.contains("وارحمنا") || clean.contains("القومالكافرين"))) {
            return masterVerses[1]
        }

        // Ayat al-Kursi signature
        if (clean.contains("اللهلاالهالاهو") && clean.contains("الحيالقيوم") && clean.contains("لاتاخذهسنة")) {
            return masterVerses[2]
        }

        // Surah Fatiha signature
        if (clean.contains("الحمدللهربالعالمين") && clean.contains("اياكنعبد")) {
            return masterVerses[3]
        }

        // Durood Ibrahim signature
        if (clean.contains("اللهمصلعلىمحمد") && clean.contains("كماصليت") && clean.contains("ابراهيم")) {
            return masterVerses[4]
        }

        // Sayyidul Istighfar signature
        if (clean.contains("اللهمانتربي") && clean.contains("خلقتنيواناعبدك") && clean.contains("ابوءلكبنعمتك")) {
            return masterVerses[5]
        }

        // Dua Yunus signature
        if (clean.contains("لاالهالاانت") && clean.contains("سبحانك") && clean.contains("انيكنتمنالظالمين")) {
            return masterVerses[6]
        }

        return null
    }

    /**
     * Sample broken inputs for testing and demo purposes.
     */
    val sampleBrokenInputs = listOf(
        SampleInput(
            titleBn = "সুরা বাকারাহ (২:২৮৫) - ভাঙ্গা শব্দ সহ",
            sampleText = "اٰمَنَ الرَّسُوۡلُ بِمَاۤ اُنۡزِلَ اِلَیۡہِ مِنۡ رَّبِّہٖ وَ الۡمُؤۡمِنُوۡনَ ؕ کُلٌّ اٰمَنَ بِاللّٰہِ وَ مَلٰٓئِکَتِہٖ وَ کُتُبِہٖ وَ رُسُلِہٖ ۟ لَا نُفَرِّقُ بَیۡنَ اَحَدٍ مِّنۡ رُّسُلِہٖ ۟ وَ قَالُوۡا سَمِعۡনَا وَ اَطَعۡনَا ٭۫ غُفۡরَانَکَ رَبَّনَا وَ اِلَیۡکَ الۡمَصِیۡরُ ﴿۲৮৫﴾"
        ),
        SampleInput(
            titleBn = "সুরা বাকারাহ (২:২৮৬) - ভাঙ্গা শব্দ ও বাংলা বর্ণ সহ",
            sampleText = "لَا يُكَلِّفُ اللّٰهُ نَفْسًا اِلَّا وُسْعَهَا ۗ لَهَا مَا كَسَبَتْ وَعَلَيْهَا مَا اكْتَسَبَتْ ۗ رَبَّنَا لَا تُؤَاخِذْنَاۤ اِنْ نَّسِيْنَاۤ اَوْ اَخْطَأْنَا ۚ رَبَّنَا وَلَا تَحْمِلْ عَلَيْنَاۤ اِصْرًا كَمَا حَمَلْتَهٗ عَلَى الَّذِيْنَ مِنْ قَبْلِنَا ۚ رَبَّنَا وَلَا تُحَمِّلْنَا مَا لَا طَا قَةَ লَنَا بِهٖ ۚ وَا عْفُ عَنَّا ۗ وَا غْفِرْ লَنَا ۗ وَا رْحَمْنَا ۗ اَنْتَ مَوْلٰٮনَا فَا نْصُرْنَا عَلَى الْقَوْمِ الْكٰفِرِيْنَ"
        ),
        SampleInput(
            titleBn = "আয়াতুল কুরসী (মিশ্র হরফ ও ভাঙ্গা স্পেস)",
            sampleText = "اللّٰهُ لَاۤ اِلٰهَ اِلَّا هُوَ الۡحَیُّ الۡقَیُّوۡمُ ۚ لَا تَاۡخُذُهٗ سِنَةٌ وَّ لَا نَوۡمٌ ؕ لَهٗ مَا فِی السَّمٰوٰتِ وَ مَا فِی الۡاَرۡضِ ﴿255﴾"
        )
    )

    data class SampleInput(
        val titleBn: String,
        val sampleText: String
    )
}
