package com.example.data.datasource

import com.example.data.local.entity.HadithBookEntity
import com.example.data.local.entity.HadithChapterEntity
import com.example.data.local.entity.HadithEntity

object HadithCatalog {

    /**
     * All 10 Major Hadith Books strictly based on HadithBD (hadithbd.com / IRD Foundation) standard.
     */
    val allBooks = listOf(
        HadithBookEntity(
            slug = "bukhari",
            nameBn = "সহীহুল বুখারী",
            nameAr = "صحيح البخاري",
            nameEn = "Sahih al-Bukhari",
            authorBn = "ইমাম মুহাম্মদ ইবনে ইসমাইল আল-বুখারী (রহ.) [মৃত ২৫৬ হি.]",
            totalHadiths = 7563,
            totalChapters = 97,
            descriptionBn = "কুরআনুল কারীমের পর মুসলিম উম্মাহর সর্বসম্মত বিশুদ্ধতম কিতাব। বিশুদ্ধ সনদ ও গভীর ফিকহী অনুচ্ছেদে বিন্যস্ত।",
            isSihahSitta = true,
            orderIndex = 1,
            colorHex = "#0A5C36"
        ),
        HadithBookEntity(
            slug = "muslim",
            nameBn = "সহীহ মুসলিম",
            nameAr = "صحيح مسلم",
            nameEn = "Sahih Muslim",
            authorBn = "ইমাম আবুল হুসাইন মুসলিম ইবনুল হাজ্জাজ আন-নিশাপুরী (রহ.) [মৃত ২৬১ হি.]",
            totalHadiths = 7500,
            totalChapters = 56,
            descriptionBn = "সহীহ বুখারীর পরেই সর্বাধিক বিশুদ্ধতম হাদিস সংকলন। একই বিষয়ের একাধিক সনদ এক স্থানে চমৎকারভাবে উপস্থাপনের জন্য অনন্য।",
            isSihahSitta = true,
            orderIndex = 2,
            colorHex = "#137547"
        ),
        HadithBookEntity(
            slug = "tirmidhi",
            nameBn = "জামে' আত-তিরমিজী",
            nameAr = "جامع الترمذي",
            nameEn = "Jami` at-Tirmidhi",
            authorBn = "ইমাম আবু ঈসা মুহাম্মদ ইবনে ঈসা আত-তিরমিজী (রহ.) [মৃত ২৭৯ হি.]",
            totalHadiths = 3956,
            totalChapters = 46,
            descriptionBn = "হাদিসের মানদণ্ড (সহীহ, হাসান, গরীব) এবং ফুকাহায়ে কিরামদের মাযহাব ও মতামতের সুবিন্যস্ত সংকলন।",
            isSihahSitta = true,
            orderIndex = 3,
            colorHex = "#B8860B"
        ),
        HadithBookEntity(
            slug = "abu-dawud",
            nameBn = "সুনানে আবু দাউদ",
            nameAr = "سنن أبي داود",
            nameEn = "Sunan Abi Dawud",
            authorBn = "ইমাম সুলায়মান ইবনুল আশ'আস আস-সিজিস্তানী (রহ.) [মৃত ২৭৫ হি.]",
            totalHadiths = 5274,
            totalChapters = 43,
            descriptionBn = "আহকাম বা শরীয়তের বিধিবিধান সম্পর্কিত সহীহ ও হাসান হাদিসের নির্ভরযোগ্য ও সুপ্রসিদ্ধ সংকলন।",
            isSihahSitta = true,
            orderIndex = 4,
            colorHex = "#2E7D32"
        ),
        HadithBookEntity(
            slug = "nasai",
            nameBn = "সুনানে আন-নাসায়ী",
            nameAr = "سنن النسائي",
            nameEn = "Sunan an-Nasa'i",
            authorBn = "ইমাম আহমদ ইবনে শু'আইব আন-নাসায়ী (রহ.) [মৃত ৩০৩ হি.]",
            totalHadiths = 5758,
            totalChapters = 50,
            descriptionBn = "সনদ বিচার ও সুক্ষ্ম ত্রুটি (ইলাল) বর্ণনায় অত্যন্ত সতর্কতার সাথে সংকলিত বিশুদ্ধ সুনান গ্রন্থ।",
            isSihahSitta = true,
            orderIndex = 5,
            colorHex = "#1E88E5"
        ),
        HadithBookEntity(
            slug = "ibn-majah",
            nameBn = "সুনানে ইবনে মাজাহ",
            nameAr = "سنن ابن ماجه",
            nameEn = "Sunan Ibn Majah",
            authorBn = "ইমাম আবু আব্দুল্লাহ মুহাম্মদ ইবনে ইয়াযীদ ইবনে মাজাহ আল-কাযভীনী (রহ.) [মৃত ২৭৩ হি.]",
            totalHadiths = 4341,
            totalChapters = 32,
            descriptionBn = "সিহাহ্ সিত্তাহর ষষ্ঠ স্তম্ভ। বিষয়ভিত্তিক অধ্যায় বিন্যাস ও বিরল গুরুত্বপূর্ণ হাদিসের জন্য সমাদৃত।",
            isSihahSitta = true,
            orderIndex = 6,
            colorHex = "#7B1FA2"
        ),
        HadithBookEntity(
            slug = "riyadus-salihin",
            nameBn = "রিয়াযুস স্বা-লিহীন",
            nameAr = "رياض الصالحين",
            nameEn = "Riyadh as-Salihin",
            authorBn = "ইমাম আবু যাকারিয়া মুহিউদ্দীন ইয়াহইয়া আন-নববী (রহ.) [মৃত ৬৭৬ হি.]",
            totalHadiths = 1905,
            totalChapters = 20,
            descriptionBn = "সৎকর্মশীলদের জান্নাতের বাগান। আখলাক, আত্মশুদ্ধি, দৈনন্দিন আমল ও চরিত্র গঠনের জন্য মুসলিম সমাজে সর্বাধিক পঠিত গ্রন্থ।",
            isSihahSitta = false,
            orderIndex = 7,
            colorHex = "#00796B"
        ),
        HadithBookEntity(
            slug = "bulugh-al-maram",
            nameBn = "বুলুগুল মারাম",
            nameAr = "بلوغ المرام",
            nameEn = "Bulugh al-Maram",
            authorBn = "হাফিজ ইবনে হাজার আল-আসকালানী (রহ.) [মৃত ৮৫২ হি.]",
            totalHadiths = 1596,
            totalChapters = 16,
            descriptionBn = "ফিকহী বিধিবিধানের দলীল হিসেবে ব্যবহৃত হাদিসের সারনির্যাস ও সনদের নির্ভরযোগ্য বিশ্লেষণ সংবলিত অতুলনীয় সংকলন।",
            isSihahSitta = false,
            orderIndex = 8,
            colorHex = "#5D4037"
        ),
        HadithBookEntity(
            slug = "forty-nawawi",
            nameBn = "ইমাম নববীর ৪০ হাদীস",
            nameAr = "الأربعون النووية",
            nameEn = "Al-Arba'in an-Nawawiyyah",
            authorBn = "ইমাম ইয়াহইয়া ইবনে শারাফ আন-নববী (রহ.) [মৃত ৬৭৬ হি.]",
            totalHadiths = 42,
            totalChapters = 1,
            descriptionBn = "ইসলামী জীবনবিধান ও মূলনীতির বুনিয়াদি ৪২টি সর্বাধিক তাৎপর্যপূর্ণ সহীহ হাদিসের সংকলন।",
            isSihahSitta = false,
            orderIndex = 9,
            colorHex = "#D97706"
        ),
        HadithBookEntity(
            slug = "hadith-qudsi",
            nameBn = "হাদীসে কুদসী",
            nameAr = "الأحاديث القدسية",
            nameEn = "Hadith Qudsi",
            authorBn = "মুহাদ্দিসীনগণের নির্বাচিত সংকলন",
            totalHadiths = 110,
            totalChapters = 1,
            descriptionBn = "যে হাদিসের বক্তব্য স্বয়ং আল্লাহ সুবহানাহু ওয়া তা'আলার পক্ষ থেকে এবং তা রাসুলুল্লাহ (সা.) স্বীয় জবানে প্রকাশ করেছেন।",
            isSihahSitta = false,
            orderIndex = 10,
            colorHex = "#059669"
        )
    )

    /**
     * Initial Chapters
     */
    val initialChapters = listOf(
        // Bukhari Chapters
        HadithChapterEntity("bukhari_1", "bukhari", 1, "ওহীর সূচনা (বই ১)", "بدء الوحي", "১ - ৭"),
        HadithChapterEntity("bukhari_2", "bukhari", 2, "ঈমান (বই ২)", "كتاب الإيمان", "৮ - ৫৮"),
        HadithChapterEntity("bukhari_3", "bukhari", 3, "ইলম বা জ্ঞান (বই ৩)", "كتاب العلم", "৫৯ - ১৩৪"),
        HadithChapterEntity("bukhari_4", "bukhari", 4, "অযু (বই ৪)", "كتاب الوضوء", "১৩৫ - ২৪৭"),
        HadithChapterEntity("bukhari_8", "bukhari", 8, "সালাত বা নামায (বই ৮)", "كتاب الصلاة", "৩৪৯ - ৫২০"),
        HadithChapterEntity("bukhari_24", "bukhari", 24, "যাকাত (বই ২৪)", "كتاب الزكاة", "১৩৯৫ - ১৪৯৮"),
        HadithChapterEntity("bukhari_30", "bukhari", 30, "রোজা বা সাওম (বই ৩১)", "كتاب الصوم", "১৮৯১ - ২০৬০"),

        // Muslim Chapters
        HadithChapterEntity("muslim_1", "muslim", 1, "ঈমান (কিতাবুল ঈমান)", "كتاب الإيمان", "১ - ৪৩৪"),
        HadithChapterEntity("muslim_2", "muslim", 2, "পবিত্রতা (কিতাবুত তাহারাত)", "كتاب الطهارة", "৪৩৫ - ৫৭৯"),
        HadithChapterEntity("muslim_4", "muslim", 4, "সালাত (কিতাবুস সালাত)", "كتاب الصلاة", "৭৪১ - ১২৫১"),

        // Tirmidhi Chapters
        HadithChapterEntity("tirmidhi_1", "tirmidhi", 1, "পবিত্রতা অধ্যায়", "أبواب الطهارة", "১ - ১৪৮"),
        HadithChapterEntity("tirmidhi_2", "tirmidhi", 2, "নামায অধ্যায়", "أبواب الصلاة", "১৪৯ - ৪৩৭"),
        HadithChapterEntity("tirmidhi_25", "tirmidhi", 25, "সদাচার ও আত্মীয়তার সম্পর্ক", "أبواب البر والصلة", "১৮৯৮ - ২০০৫"),

        // Abu Dawud Chapters
        HadithChapterEntity("abu-dawud_1", "abu-dawud", 1, "পবিত্রতা", "كتاب الطهارة", "১ - ৩৯০"),
        HadithChapterEntity("abu-dawud_2", "abu-dawud", 2, "সালাত", "كتاب الصلاة", "৩৯১ - ১১৬১"),

        // Nasai Chapters
        HadithChapterEntity("nasai_1", "nasai", 1, "পবিত্রতা", "كتاب الطهارة", "১ - ৪৪৮"),
        HadithChapterEntity("nasai_2", "nasai", 2, "পানি ও পবিত্রতা", "كتاب المياه", "৪৪৯ - ৫৩৫"),

        // Ibn Majah Chapters
        HadithChapterEntity("ibn-majah_1", "ibn-majah", 1, "সুন্নাহর অনুসরণ (ভূমিকা)", "افتتاح الكتاب في الإيمان وفضائل الصحابة والعلم", "১ - ২৬৬"),
        HadithChapterEntity("ibn-majah_2", "ibn-majah", 2, "পবিত্রতা ও তার নিয়মাবলি", "كتاب الطهارة وسننها", "২৬৭ - ৬৭৪"),

        // Bulugh al-Maram Chapters
        HadithChapterEntity("bulugh-al-maram_1", "bulugh-al-maram", 1, "পবিত্রতা অধ্যায় (কিতাবুত তাহারাত)", "كتاب الطهارة", "১ - ১৪৬"),
        HadithChapterEntity("bulugh-al-maram_2", "bulugh-al-maram", 2, "সালাত অধ্যায় (কিতাবুস সালাত)", "كتاب الصلاة", "১৪৭ - ৪৫৫"),

        // Riyadhus Salihin Chapters
        HadithChapterEntity("riyadus-salihin_1", "riyadus-salihin", 1, "ইখলাস ও নিয়ত", "باب الإخلاص وإحضار النية", "১ - ১২"),
        HadithChapterEntity("riyadus-salihin_2", "riyadus-salihin", 2, "তাওবাহ বা অনুশোচনা", "باب التوبة", "১৩ - ২৯"),
        HadithChapterEntity("riyadus-salihin_3", "riyadus-salihin", 3, "সবর বা ধৈর্য", "باب الصبر", "৩০ - ৫৩"),

        // 40 Hadith Nawawi
        HadithChapterEntity("forty-nawawi_1", "forty-nawawi", 1, "মৌলিক ৪০ হাদিস", "الأربعون النووية كاملة", "১ - ৪২"),

        // Hadith Qudsi
        HadithChapterEntity("hadith-qudsi_1", "hadith-qudsi", 1, "আল্লাহর রহমত ও মহত্ব", "أحاديث الرحمة والمغفرة", "১ - ৫০")
    )

    /**
     * Authentic offline bundled Hadith dataset verified from HadithBD (hadithbd.com / IRD Foundation).
     */
    val initialHadiths = listOf(
        // Bukhari Hadith 1
        HadithEntity(
            id = "bukhari_1",
            bookSlug = "bukhari",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "ওহীর সূচনা",
            narratorBn = "হযরত আলক্বামাহ ইবনু ওয়াক্কাস আল-লায়সী (রহ.) সূত্রে হযরত উমর ইবনুল খাত্তাব (রা.)",
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى دُنْيَا يُصِيبُهَا أَوْ إِلَى امْرَأَةٍ يَنْكِحُهَا، فَهِجْرَتُهُ إِلَى مَا هَاجَرَ إِلَيْهِ.",
            banglaText = "সকল কাজের ফলাফল নিয়তের ওপর নির্ভরশীল। প্রত্যেক মানুষ তাই পায়, যা সে নিয়ত করে। অতএব যে ব্যক্তির হিজরত দুনিয়া পাওয়ার উদ্দেশে কিংবা কোনো নারীকে বিবাহ করার নিয়তে হবে, তার হিজরত সেই উদ্দেশ্যেই গণ্য হবে যে উদ্দেশ্যে সে হিজরত করেছে।",
            englishText = "Actions are according to intentions, and every person will get the reward according to what he has intended.",
            gradeBn = "সহীহ বুখারী: ১",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ বুখারী ১, তাওহীদ পাবলিকেশন",
            explanationBn = "এই হাদিসটি ইসলামের এক-তৃতীয়াংশ ইলম হিসেবে স্বীকৃত। ইমাম শাফেয়ী (রহ.) বলেন, এই হাদিসটির অন্তর্ভুক্ত ইসলামি ফিকহের ৭০টি অধ্যায়। কোনো সৎকাজের সওয়াব নির্ভর করে খাঁটি ইখলাস ও নিয়তের ওপর।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Bukhari Hadith 2
        HadithEntity(
            id = "bukhari_2",
            bookSlug = "bukhari",
            hadithNumber = 2,
            chapterNumber = 1,
            chapterTitleBn = "ওহীর সূচনা",
            narratorBn = "উম্মুল মুমিনীন হযরত আয়েশা (রা.)",
            arabicText = "أَنَّ الْحَارِثَ بْنَ هِشَامٍ رَضِيَ اللَّهُ عَنْهُ سَأَلَ رَسُولَ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ فَقَالَ: يَا رَسُولَ اللَّهِ، كَيْفَ يَأْتِيكَ الْوَحْيُ؟ فَقَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسেলَّمَ: أَحْيَانًا يَأْتِينِي مِثْلَ صَلْصَلَةِ الْجَرَسِ وَهُوَ أَشَدُّهُ عَلَيَّ، فَيُفْصَمُ عَنِّي وَقَدْ وَعَيْتُ عَنْهُ مَا قَالَ، وَأَحْيَانًا يَتَمَثَّلُ لِي الْمَلَكُ رَجُلاً فَيُكَلِّمُنِي فَأَعِي مَا يَقُولُ.",
            banglaText = "হারিস ইবনু হিশাম (রা.) রাসুলুল্লাহ (সা.)-কে জিজ্ঞাসা করলেন: হে আল্লাহর রাসুল! আপনার নিকট ওহী কীভাবে আসে? রাসুলুল্লাহ (সা.) বললেন: কোনো কোনো সময় তা ঘণ্টার টুংটাং শব্দের ন্যায় আমার নিকট আসে। আর এটি আমার ওপর সবচেয়ে বেশি কঠিন ও কষ্টদায়ক হয়। অতঃপর ওহী সমাপ্ত হলে যা বলা হয়েছে তা আমি হৃদয়ঙ্গম করে নেই। আর কখনো ফিরিশতা মানুষের রূপ ধারণ করে আমার সামনে উপস্থিত হন এবং আমার সাথে কথা বলেন, তখন তিনি যা বলেন আমি তা মুখস্থ করে নেই।",
            englishText = "Al-Harith bin Hisham asked Allah's Messenger: O Allah's Messenger! How is the Divine Inspiration revealed to you?",
            gradeBn = "সহীহ বুখারী: ২",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ বুখারী ২",
            explanationBn = "রাসুলুল্লাহ (সা.)-এর নিকট ওহী নাজিলের বিভিন্ন ধরন ও তার তীব্রতা সম্পর্কে এটি একটি অত্যন্ত গুরুত্বপূর্ণ ওহীর প্রামাণ্য বিবরণ।",
            hadithTypeBn = "কওলী ও ফে'লী"
        ),

        // Bukhari Hadith 3
        HadithEntity(
            id = "bukhari_3",
            bookSlug = "bukhari",
            hadithNumber = 3,
            chapterNumber = 1,
            chapterTitleBn = "ওহীর সূচনা",
            narratorBn = "উম্মুল মুমিনীন হযরত আয়েশা (রা.)",
            arabicText = "أَوَّلُ مَا بُدِئَ بِهِ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ مِنَ الْوَحْيِ الرُّؤْيَا الصَّالِحَةُ فِي النَّوْمِ، فَكَانَ لاَ يَرَى رُؤْيَا إِلاَّ جَاءَتْ مِثْلَ فَلَقِ الصُّبْحِ، ثُمَّ حُبِّبَ إِلَيْهِ الْخَلاَءُ، وَكَانَ يَخْلُو بِغَارِ حِرَاءٍ فَيَتَحَنَّثُ فِيهِ... حَتَّى جَاءَهُ الْحَقُّ وَهُوَ فِي غَارِ حِرَاءٍ، فَجَاءَهُ الْمَلَكُ فَقَالَ: اقْرَأْ، قَالَ: مَا أَنَا بِقَارِئٍ.",
            banglaText = "রাসুলুল্লাহ (সা.)-এর প্রতি ওহী সূচনার প্রথম অবস্থা ছিল ঘুমের মধ্যে সত্য স্বপ্ন দর্শন। তিনি যে স্বপ্নই দেখতেন তা প্রভাতের শুভ্র আলোর ন্যায় সত্যে পরিণত হতো। অতঃপর নির্জনতা তাঁর নিকট প্রিয় হয়ে ওঠে এবং তিনি হেরা গুহায় নির্জনে ইবাদতে মগ্ন থাকতেন... অবশেষে তাঁর নিকট হেরা গুহায় সত্য ওহী নিয়ে জিবরীল (আ.) আগমন করলেন এবং বললেন: 'পড়ুন!' রাসুলুল্লাহ (সা.) বললেন: 'আমি তো পড়ুয়া নই।' অতঃপর তিনি তাঁকে জড়িয়ে ধরে আলিঙ্গন করলেন এবং বললেন: 'পড়ুন আপনার রবের নামে যিনি সৃষ্টি করেছেন।'",
            englishText = "The commencement of the Divine Inspiration to Allah's Messenger was in the form of good dreams which came like bright daylight.",
            gradeBn = "সহীহ বুখারী: ৩",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ বুখারী ৩",
            explanationBn = "নবুওয়াত লাভ ও হেরা গুহায় সূরা আলাকের প্রথম ৫টি আয়াত নাজিলের ঐতিহাসিক বিশদ বিবরণ।",
            hadithTypeBn = "কওলী ও ফে'লী"
        ),

        // Bukhari Hadith 8 (Five Pillars of Islam)
        HadithEntity(
            id = "bukhari_8",
            bookSlug = "bukhari",
            hadithNumber = 8,
            chapterNumber = 2,
            chapterTitleBn = "ঈমান",
            narratorBn = "হযরত আব্দুল্লাহ ইবনু উমর (রা.)",
            arabicText = "بُنِيَ الإِسْلاَمُ عَلَى خَمْسٍ: شَهَادَةِ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَإِقَامِ الصَّلاَةِ، وَإِيتَاءِ الزَّكَاةِ، وَالْحَجِّ، وَصَوْمِ رَمَضَانَ.",
            banglaText = "ইসলাম পাঁচটি স্তম্ভের ওপর প্রতিষ্ঠিত: এই সাক্ষ্য দেওয়া যে, আল্লাহ ছাড়া কোনো সত্য ইলাহ নেই এবং নিশ্চয়ই মুহাম্মদ (সা.) আল্লাহর রাসুল, সালাত কায়েম করা, যাকাত দেওয়া, হজ্ব করা এবং রমাদানের রোজা রাখা।",
            englishText = "Islam is based on five (principles): To testify that none has the right to be worshipped but Allah and Muhammad is Allah's Messenger, to offer prayers, to pay Zakat, to perform Hajj, and to observe fast during Ramadan.",
            gradeBn = "সহীহ বুখারী: ৮",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ বুখারী ৮",
            explanationBn = "ইসলামী জীবনব্যবস্থার বুনিয়াদী পাঁচটি খুঁটি বা ভিত্তি যার ওপর সমগ্র দ্বীন প্রতিষ্ঠিত।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Bukhari Hadith 13 (Love for Brother)
        HadithEntity(
            id = "bukhari_13",
            bookSlug = "bukhari",
            hadithNumber = 13,
            chapterNumber = 2,
            chapterTitleBn = "ঈমান",
            narratorBn = "হযরত আনাস ইবনু মালিক (রা.)",
            arabicText = "لاَ يُؤْمِنُ أَحَدُكُمْ حَتَّى يُحِبَّ لأَخِيهِ مَا يُحِبُّ لِنَفْسِهِ.",
            banglaText = "তোমাদের কেউ ততক্ষণ পর্যন্ত প্রকৃত মুমিন হতে পারবে না, যতক্ষণ না সে তার ভাইয়ের জন্য তাই পছন্দ করবে যা সে নিজের জন্য পছন্দ করে।",
            englishText = "None of you will have faith till he wishes for his brother what he likes for himself.",
            gradeBn = "সহীহ বুখারী: ১৩",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ বুখারী ১৩",
            explanationBn = "ইসলামী ভ্রাতৃত্ব ও সমাজব্যবস্থার ভিত্তি হলো অন্যের প্রতি নিঃস্বার্থ ভালোবাসা ও শুভকামনা পোষণ করা। ঈমানের পূর্ণতার অন্যতম প্রধান শর্ত এটি।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Bukhari Hadith 5027 (Best among you learns Quran)
        HadithEntity(
            id = "bukhari_5027",
            bookSlug = "bukhari",
            hadithNumber = 5027,
            chapterNumber = 66,
            chapterTitleBn = "কুরআনের ফযিলত",
            narratorBn = "হযরত উসমান ইবনু আফফান (রা.)",
            arabicText = "خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ.",
            banglaText = "তোমাদের মধ্যে সেই ব্যক্তিই সর্বোত্তম, যে নিজে কুরআন শেখে এবং অন্যকে তা শেখায়।",
            englishText = "The best among you (Muslims) are those who learn the Qur'an and teach it.",
            gradeBn = "সহীহ বুখারী: ৫০২৭",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ বুখারী ৫০২৭",
            explanationBn = "কুরআন শিক্ষা লাভ করা এবং তা সমাজের অন্যান্যদের মাঝে ছড়িয়ে দেওয়া উম্মতের সর্বোচ্চ মর্যাদাবান ও কল্যাণকর আমল।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Muslim Hadith 1 (Hadith Jibril)
        HadithEntity(
            id = "muslim_1",
            bookSlug = "muslim",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "ঈমান",
            narratorBn = "হযরত ইয়াহইয়া ইবনু ইয়া'মার সূত্রে হযরত উমর ইবনুল খাত্তাব (রা.)",
            arabicText = "قَالَ: فَأَخْبِرْنِي عَنِ الإِسْلاَمِ؟ قَالَ رَسُولُ اللَّهِ صَلَّى اللَّهُ عَلَيْهِ وَسَلَّمَ: الإِسْلاَمُ أَنْ تَشْهَدَ أَنْ لاَ إِلَهَ إِلاَّ اللَّهُ وَأَنَّ مُحَمَّدًا رَسُولُ اللَّهِ، وَتُقِيمَ الصَّلاَةَ، وَتُؤْتِيَ الزَّكَاةَ، وَتَصُومَ رَمَضَانَ، وَتَحُجَّ الْبَيْتَ إِنِ اسْتَطَعْتَ إِلَيْهِ سَبِيلاً. قَالَ: صَدَقْتَ. قَالَ: فَأَخْبِرْنِي عَنِ الإِيمَانِ؟ قَالَ: أَنْ تُؤْمِنَ بِاللَّهِ، وَمَلاَئِكَتِهِ، وَكُتُبِهِ، وَرُسُلِهِ، وَالْيَوْمِ الآخِرِ، وَتُؤْمِنَ بِالْقَدَرِ خَيْرِهِ وَشَرِّهِ. قَالَ: صَدَقْتَ. قَالَ: فَأَخْبِرْنِي عَنِ الإِحْسَانِ؟ قَالَ: أَنْ تَعْبُدَ اللَّهَ كَأَنَّكَ تَرَاهُ، فَإِنْ لَمْ تَكُنْ تَرَاهُ فَإِنَّهُ يَرَاكَ.",
            banglaText = "তিনি (জিবরীল আ.) বললেন: আমাকে ইসলাম সম্পর্কে বলুন। রাসুলুল্লাহ (সা.) বললেন: ইসলাম হলো তুমি এ সাক্ষ্য দিবে যে, আল্লাহ ছাড়া সত্য কোনো উপাস্য নেই এবং মুহাম্মদ (সা.) আল্লাহর রাসুল, সালাত কায়েম করবে, যাকাত প্রদান করবে, রমাদানের সিয়াম পালন করবে এবং সামর্থ্য থাকলে বায়তুল্লাহর হজ্ব করবে। তিনি বললেন: আপনি সত্য বলেছেন। অতঃপর বললেন: আমাকে ঈমান সম্পর্কে বলুন। রাসুলুল্লাহ (সা.) বললেন: আল্লাহ, তাঁর ফিরিশতাগণ, তাঁর কিতাবসমূহ, তাঁর রাসুলগণ ও পরকালের ওপর বিশ্বাস স্থাপন করবে এবং ভাগ্যের ভালো-মন্দের ওপর বিশ্বাস রাখবে। তিনি বললেন: সত্য বলেছেন। অতঃপর বললেন: আমাকে ইহসান সম্পর্কে বলুন। রাসুলুল্লাহ (সা.) বললেন: ইহসান হলো এমনভাবে আল্লাহর ইবাদত করবে যেন তুমি তাঁকে দেখছো; আর যদি তাঁকে দেখতে না পাও, তবে তিনি তোমাকে দেখছেন।",
            englishText = "Hadith Jibril: The definition of Islam, Iman, and Ihsan.",
            gradeBn = "সহীহ মুসলিম: ১",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ মুসলিম ১, ইসলামিক ফাউন্ডেশন",
            explanationBn = "এই হাদিসটিকে বলা হয় 'উম্মুস সুন্নাহ' বা সমস্ত সুন্নাহর মূল উৎস। এতে দ্বীনের তিনটি স্তর—ইসলাম (বাহ্যিক আমল), ঈমান (অভ্যন্তরীণ বিশ্বাস) এবং ইহসান (ইবাদতের সর্বোচ্চ আধ্যাত্মিক একাগ্রতা) সুস্পষ্টভাবে বিবৃত হয়েছে।",
            hadithTypeBn = "কওলী ও তাকরীরী"
        ),

        // Muslim Hadith 2
        HadithEntity(
            id = "muslim_2",
            bookSlug = "muslim",
            hadithNumber = 2,
            chapterNumber = 1,
            chapterTitleBn = "ঈমান",
            narratorBn = "হযরত আবু হুরায়রা (রা.)",
            arabicText = "الإِيمَانُ بِضْعٌ وَسَبْعُونَ أَوْ بِضْعٌ وَسِتُّونَ شُعْبَةً، فَأَفْضَلُهَا قَوْلُ لاَ إِلَهَ إِلاَّ اللَّهُ، وَأَدْنَاهَا إِمَاطَةُ الأَذَى عَنِ الطَّرِيقِ، وَالْحَيَاءُ شُعْبَةٌ مِنَ الإِيمَانِ.",
            banglaText = "ঈমানের সত্তরের অধিক অথবা ষাটের অধিক শাখা রয়েছে। তার মধ্যে সর্বোত্তম শাখা হলো 'লা ইলাহা ইল্লাল্লাহ' বলা, আর সর্বনিম্ন শাখা হলো পথ থেকে কষ্টদায়ক বস্তু সরিয়ে ফেলা। আর লজ্জা হলো ঈমানের একটি বিশেষ শাখা।",
            englishText = "Faith has over seventy or over sixty branches; the foremost of them is the declaration of 'There is no god but Allah' and the least of them is the removal of something harmful from the path, and modesty is a branch of faith.",
            gradeBn = "সহীহ মুসলিম: ৩৫",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ মুসলিম ৩৫",
            explanationBn = "ঈমানের সামগ্রিক ব্যাপ্তি—মৌখিক স্বীকৃতি, সামাজিক সেবা ও চারিত্রিক পবিত্রতা (লজ্জাশীলতা)।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Muslim Hadith 223 (Purification is half of faith)
        HadithEntity(
            id = "muslim_223",
            bookSlug = "muslim",
            hadithNumber = 223,
            chapterNumber = 2,
            chapterTitleBn = "পবিত্রতা",
            narratorBn = "হযরত আবু মালিক আল-আশ'আরী (রা.)",
            arabicText = "الطُّهُورُ شَطْرُ الإِيمَانِ، وَالْحَمْدُ لِلَّهِ تَمْلأُ الْمِيزَانَ، وَسُبْحَانَ اللَّهِ وَالْحَمْدُ لِلَّهِ تَمْلآنِ أَوْ تَمْلأُ مَا بَيْنَ السَّمَاوَاتِ وَالأَرْضِ، وَالصَّلاَةُ نُورٌ، وَالصَّدَقَةُ بُرْهَانٌ، وَالصَّبْرُ ضِيَاءٌ، وَالْقُرْآنُ حُجَّةٌ لَكَ أَوْ عَلَيْكَ، كُلُّ النَّاسِ يَغْدُو فَبَائِعٌ نَفْسَهُ فَمُعْتِقُهَا أَوْ مُوبِقُهَا.",
            banglaText = "পবিত্রতা ঈমানের অর্ধেক। 'আলহামদুলিল্লাহ' পাল্লা পূর্ণ করে দেয়। 'সুবহানাল্লাহ' ও 'আলহামদুলিল্লাহ' আকাশ ও জমিনের মধ্যবর্তী শূন্যস্থান পূর্ণ করে দেয়। সালাত হলো নূর (জ্যোতি), সাদাকাহ হলো দলীল (প্রমাণ), ধৈর্য হলো আলোকবর্তিকা, আর কুরআন তোমার পক্ষে কিংবা বিপক্ষে অকাট্য প্রমাণ হবে। প্রতিটি মানুষ প্রত্যুষে স্বীয় কর্মে বের হয়, অতঃপর সে নিজেকে বিক্রি করে—হয় সে নিজেকে মুক্ত করে অথবা ধ্বংস করে।",
            englishText = "Cleanliness is half of faith and Alhamdulillah fills the scale.",
            gradeBn = "সহীহ মুসলিম: ২২৩",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সহীহ মুসলিম ২২৩",
            explanationBn = "বাহ্যিক ও অভ্যন্তরীণ পবিত্রতার গুরুত্ব এবং সার্বক্ষণিক জিকির, সালাত ও ধৈর্যের অসীম সওয়াব ও প্রতিদানের পূর্ণাঙ্গ বিবরণ।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Tirmidhi Hadith 1
        HadithEntity(
            id = "tirmidhi_1",
            bookSlug = "tirmidhi",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "পবিত্রতা অধ্যায়",
            narratorBn = "হযরত আবু হুরায়রা (রা.) সূত্রে রাসুলুল্লাহ (সা.)",
            arabicText = "لاَ تُقْبَلُ صَلاَةُ مَنْ أَحْدَثَ حَتَّى يَتَوَضَّأَ.",
            banglaText = "অযু নষ্ট হয়ে যাওয়া ব্যক্তির কোনো সালাতই আল্লাহ তা'আলা কবুল করবেন না যতক্ষণ না সে নতুন করে অযু সম্পন্ন করে।",
            englishText = "The prayer of a person who does Hadath (passes wind, urine, etc.) is not accepted until he performs ablution.",
            gradeBn = "সহীহ (বুখারী ও মুসলিম)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / জামে আত-তিরমিজী ১",
            explanationBn = "সালাতের বিশুদ্ধতার প্রথম ও প্রধান শর্ত হলো অযু ও শারীরিক পবিত্রতা রক্ষা করা।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Tirmidhi Hadith 2516 (Safeguard Allah and He will safeguard you)
        HadithEntity(
            id = "tirmidhi_2516",
            bookSlug = "tirmidhi",
            hadithNumber = 2516,
            chapterNumber = 37,
            chapterTitleBn = "কিয়ামতের বর্ণনা ও অন্তরের কোমলতা",
            narratorBn = "হযরত আব্দুল্লাহ ইবনু আব্বাস (রা.)",
            arabicText = "يَا غُلاَمُ، إِنِّي أُعَلِّمُكَ كَلِمَاتٍ: احْفَظِ اللَّهَ يَحْفَظْكَ، احْفَظِ اللَّهَ تَجِدْهُ تُجَاهَكَ، إِذَا سَأَلْتَ فَاسْأَلِ اللَّهَ، وَإِذَا اسْتَعَنْتَ فَاسْتَعِنْ بِاللَّهِ، وَاعْلَمْ أَنَّ الأُمَّةَ لَوِ اجْتَمَعَتْ عَلَى أَنْ يَنْفَعُوكَ بِشَيْءٍ لَمْ يَنْفَعُوكَ إِلاَّ بِشَيْءٍ قَدْ كَتَبَهُ اللَّهُ لَكَ، وَلَوِ اجْتَمَعُوا عَلَى أَنْ يَضُرُّوكَ بِشَيْءٍ لَمْ يَضُرُّوكَ إِلاَّ بِشَيْءٍ قَدْ كَتَبَهُ اللَّهُ عَلَيْكَ، رُفِعَتِ الأَقْلاَمُ وَجَفَّتِ الصُّحُفُ.",
            banglaText = "হে বৎস! আমি তোমাকে কয়েকটি উপদেশমূলক বাক্য শিক্ষা দিচ্ছি: তুমি আল্লাহর বিধানসমূহের হিফাজত করো, আল্লাহ তোমাকে হিফাজত করবেন। তুমি আল্লাহর হকের হিফাজত করো, তুমি তাঁকে তোমার সামনেই পাবে। যখন কিছু চাইবে কেবল আল্লাহর কাছেই চাইবে; আর যখন কোনো সাহায্য প্রার্থনা করবে কেবল আল্লাহর কাছেই সাহায্য প্রার্থনা করবে। জেনে রেখো, সমগ্র জাতি একত্রিত হয়েও যদি তোমার কোনো উপকার করতে চায়, তবে ততটুকুই করতে পারবে যা আল্লাহ তোমার তাকদীরে লিখে রেখেছেন। আর তারা সবাই মিলেও যদি তোমার কোনো ক্ষতি করতে চায়, তবে ততটুকুই করতে পারবে যা আল্লাহ তোমার তাকদীরে লিখে রেখেছেন। কলম তুলে নেওয়া হয়েছে এবং লেখার কালির খাতা শুকিয়ে গেছে।",
            englishText = "O young man! I shall teach you some words: Be mindful of Allah and Allah will protect you...",
            gradeBn = "সহীহ (ইমাম তিরমিজী: হাসান সহীহ)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / জামে আত-তিরমিজী ২৫১৬",
            explanationBn = "তাওহীদ, তাকদীরের বিশ্বাস এবং একমাত্র আল্লাহর ওপর ভরসা (তাওয়াক্কুল) করার ক্ষেত্রে ইসলামের অন্যতম শ্রেষ্ঠ আধ্যাত্মিক দিকনির্দেশনা।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Abu Dawud Hadith 1
        HadithEntity(
            id = "abu-dawud_1",
            bookSlug = "abu-dawud",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "পবিত্রতা",
            narratorBn = "হযরত আনাস ইবনু মালিক (রা.)",
            arabicText = "كَانَ رَسُولُ اللَّهِ صلى الله عليه وسلم إِذَا دَخَلَ الْخَلاَءَ قَالَ: اللَّهُمَّ إِنِّي أَعُوذُ بِكَ مِنَ الْخُبْثِ وَالْخَبَائِثِ.",
            banglaText = "রাসুলুল্লাহ (সা.) যখন শৌচাগারে প্রবেশ করতেন, তখন বলতেন: 'হে আল্লাহ! নিশ্চয়ই আমি আপনার আশ্রয় প্রার্থনা করছি অপবিত্র পুরুষ ও নারী জিনদের অনিষ্ট থেকে।'",
            englishText = "When the Prophet entered the privy he said: O Allah, I seek refuge with Thee from male and female demons.",
            gradeBn = "সহীহ (বুখারী ও মুসলিম)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সুনানে আবু দাউদ ৪ ও সহীহ বুখারী ১৪২",
            explanationBn = "শৌচাগারে প্রবেশের আগে আল্লাহর পবিত্র আশ্রয় চাওয়ার মাসনুন দু'আ ও শিষ্টাচার।",
            hadithTypeBn = "ফে'লী ও কওলী"
        ),

        // Abu Dawud Hadith 4941 (Smiling & Good manners)
        HadithEntity(
            id = "abu-dawud_4941",
            bookSlug = "abu-dawud",
            hadithNumber = 4941,
            chapterNumber = 41,
            chapterTitleBn = "শিষ্টাচার (কিতাবুল আদব)",
            narratorBn = "হযরত আবু দারদা (রা.)",
            arabicText = "مَا مِنْ شَيْءٍ أَثْقَلُ فِي مِيزَانِ الْمُؤْمِنِ يَوْمَ الْقِيَامَةِ مِنْ حُسْنِ الْخُلُقِ، وَإِنَّ اللَّهَ لَيُبْغِضُ الْفَاحِشَ الْبَذِيءَ.",
            banglaText = "কিয়ামতের দিন মুমিনের দাঁড়িপাল্লায় সুন্দর চরিত্রের চেয়ে ভারী কোনো জিনিস রাখা হবে না। আর নিশ্চয়ই আল্লাহ তা'আলা অশ্লীল ও নির্লজ্জ বাক্যালাপকারী ব্যক্তিকে ঘৃণা করেন।",
            englishText = "Nothing is heavier on the believer's Scale on the Day of Judgment than good character.",
            gradeBn = "সহীহ (আলবানী)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সুনানে আবু দাউদ ৪৯৪১",
            explanationBn = "উত্তম চরিত্র ও নম্র ব্যবহার কিয়ামতের ময়দানে নেক আমলের পাল্লাকে সর্বাধিক ভারী করে দেবে।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Nasai Hadith 1 (Purity)
        HadithEntity(
            id = "nasai_1",
            bookSlug = "nasai",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "পবিত্রতা",
            narratorBn = "হযরত আবু হুরায়রা (রা.)",
            arabicText = "لاَ يَقْبَلُ اللَّهُ صَلاَةَ أَحَدِكُمْ إِذَا أَحْدَثَ حَتَّى يَتَوَضَّأَ.",
            banglaText = "তোমাদের কারো ওযু নষ্ট হলে, পুনরায় ওযু না করা পর্যন্ত আল্লাহ তার সালাত গ্রহণ করবেন না।",
            englishText = "Allah does not accept prayer of anyone of you if he does Hadath until he performs ablution.",
            gradeBn = "সহীহ (সুনানে নাসাঈ ১)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সুনানে আন-নাসাঈ ১",
            explanationBn = "পবিত্রতার প্রাথমিক ভিত্তি ও ওযুর অবশ্যম্ভাবিতা।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Ibn Majah Hadith 224 (Seeking Knowledge)
        HadithEntity(
            id = "ibn-majah_224",
            bookSlug = "ibn-majah",
            hadithNumber = 224,
            chapterNumber = 1,
            chapterTitleBn = "সুন্নাহর অনুসরণ (ভূমিকা)",
            narratorBn = "হযরত আনাস ইবনু মালিক (রা.)",
            arabicText = "طَلَبُ الْعِلْمِ فَرِيضَةٌ عَلَى كُلِّ مُسْلِمٍ.",
            banglaText = "দ্বীনি জ্ঞান অন্বেষণ করা প্রত্যেক মুসলিম (নর-নারী)-এর ওপর অবশ্য কর্তব্য বা ফরজ।",
            englishText = "Seeking knowledge is a duty upon every Muslim.",
            gradeBn = "সহীহ (আলবানী: সহীহুল জামে ৩৯১৪)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / সুনানে ইবনে মাজাহ ২২৪",
            explanationBn = "ইসলামী জ্ঞানার্জন সকল মুমিনের ব্যক্তিগত বাধ্যবাধকতা ও সর্বোচ্চ সফলতার চাবিকাঠি।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // 40 Hadith Nawawi Hadith 1 (Intention)
        HadithEntity(
            id = "forty-nawawi_1",
            bookSlug = "forty-nawawi",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "মৌলিক ৪০ হাদিস",
            narratorBn = "আমীরুল মুমিনীন হযরত উমর ইবনুল খাত্তাব (রা.)",
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى.",
            banglaText = "নিশ্চয়ই সমস্ত আমল নিয়ত বা সংকল্পের ওপর নির্ভরশীল। আর প্রত্যেক ব্যক্তি তাই পাবে যা সে নিয়ত করেছে।",
            englishText = "Actions are according to intentions, and everyone will get what was intended.",
            gradeBn = "সহীহ বুখারী: ১ ও সহীহ মুসলিম: ১৯০৭",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / ইমাম নববীর ৪০ হাদীস: ১",
            explanationBn = "দ্বীনের যাবতীয় আমলের গ্রহণযোগ্যতার প্রথম ভিত্তি খাঁটি নিয়ত।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // 40 Hadith Nawawi Hadith 7 (Pure Religion is Sincerity)
        HadithEntity(
            id = "forty-nawawi_7",
            bookSlug = "forty-nawawi",
            hadithNumber = 7,
            chapterNumber = 1,
            chapterTitleBn = "মৌলিক ৪০ হাদিস",
            narratorBn = "হযরত তামীম আদ-দারী (রা.)",
            arabicText = "الدِّينُ النَّصِيحَةُ. قُلْنَا: لِمَنْ؟ قَالَ: لِلَّهِ وَلِكِتَابِهِ وَلِرَسُولِهِ وَلأَئِمَّةِ الْمُسْلِمِينَ وَعَامَّتِهِمْ.",
            banglaText = "দ্বীন হলো আন্তরিক শুভকামনা (নসীহত)। আমরা জিজ্ঞাসা করলাম: কার জন্য? রাসুলুল্লাহ (সা.) বললেন: আল্লাহর জন্য, তাঁর কিতাবের জন্য, তাঁর রাসুলের জন্য, মুসলিম নেতৃবৃন্দের জন্য এবং সাধারণ মুসলিমদের জন্য।",
            englishText = "The religion is sincerity. We said, 'To whom?' He said, 'To Allah, His Book, His Messenger, and to the leaders of the Muslims and their common folk.'",
            gradeBn = "সহীহ মুসলিম: ৫৫",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / ইমাম নববীর ৪০ হাদীস: ৭",
            explanationBn = "ইসলামের সমস্ত আমল ও সম্পর্কের প্রাণ হচ্ছে ইখলাস ও আন্তরিকতা—আল্লাহর হকের প্রতি নিষ্ঠা এবং সকল সৃষ্টির কল্যাণ কামনা।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Hadith Qudsi Hadith 1 (Mercy overtakes Wrath)
        HadithEntity(
            id = "hadith-qudsi_1",
            bookSlug = "hadith-qudsi",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "আল্লাহর রহমত ও মহত্ব",
            narratorBn = "হযরত আবু হুরায়রা (রা.) সূত্রে রাসুলুল্লাহ (সা.) হতে বর্ণিত, মহান আল্লাহ বলেন",
            arabicText = "إِنَّ رَحْمَتِي غَلَبَتْ غَضَبِي.",
            banglaText = "আল্লাহ তা'আলা যখন সৃষ্টিজগত সৃষ্টি করলেন, তখন তিনি স্বীয় আরশের ওপর লিখিত কিতাবে নিজের জন্য লিখে রাখলেন: নিশ্চয়ই আমার রহমত আমার ক্রোধের ওপর প্রাধান্য বিস্তার করেছে।",
            englishText = "My Mercy has indeed overcome My Anger.",
            gradeBn = "সহীহ বুখারী: ৩১৯৪ ও সহীহ মুসলিম: ২৭৫১",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / হাদীসে কুদসী ১",
            explanationBn = "বান্দার প্রতি মহান আল্লাহর সীমাহীন দয়া ও ক্ষমাশীলতার সুস্পষ্ট সুসংবাদ। পাপ যতই বড় হোক, আল্লাহর রহমত তার চেয়েও অসীম।",
            hadithTypeBn = "কুদসী"
        ),

        // Hadith Qudsi Hadith 2
        HadithEntity(
            id = "hadith-qudsi_2",
            bookSlug = "hadith-qudsi",
            hadithNumber = 2,
            chapterNumber = 1,
            chapterTitleBn = "আল্লাহর রহমত ও মহত্ব",
            narratorBn = "হযরত আবু হুরায়রা (রা.) ও আবু সাঈদ খুদরী (রা.) হতে বর্ণিত, আল্লাহ বলেন",
            arabicText = "الْكِبْرِيَاءُ رِدَائِي، وَالْعَظَمَةُ إِزَارِي، فَمَنْ نَازَعَنِي وَاحِدًا مِنْهُمَا قَذَفْتُهُ فِي النَّارِ.",
            banglaText = "মহত্ত্ব ও অহংকার হলো আমার চাদর এবং পরাক্রমশালী শ্রেষ্ঠত্ব হলো আমার পরিধেয় বস্ত্র। অতএব যে ব্যক্তি এ দুটির কোনো একটি নিয়ে আমার সাথে টানাটানি করবে (অহংকার প্রদর্শন করবে), তাকে আমি জাহান্নামের আগুনে নিক্ষেপ করব।",
            englishText = "Pride is My cloak and greatness is My robe, and he who competes with Me in respect of either of them, I shall cast into the Fire.",
            gradeBn = "সহীহ মুসলিম: ২৬২০ ও আবু দাউদ: ৪০৯০",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / হাদীসে কুদসী ২",
            explanationBn = "অহংকার একমাত্র আল্লাহরই ভূষণ। সৃষ্টির জন্য অহংকার সর্বনাশা ধ্বংস ডেকে আনে।",
            hadithTypeBn = "কুদসী"
        ),

        // Riyadhus Salihin Hadith 1
        HadithEntity(
            id = "riyadus-salihin_1",
            bookSlug = "riyadus-salihin",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "ইখলাস ও নিয়ত",
            narratorBn = "আমীরুল মুমিনীন হযরত উমর ইবনুল খাত্তাব (রা.)",
            arabicText = "إِنَّمَا الأَعْمَالُ بِالنِّيَّاتِ، وَإِنَّمَا لِكُلِّ امْرِئٍ مَا نَوَى، فَمَنْ كَانَتْ هِجْرَتُهُ إِلَى اللَّهِ وَرَسُولِهِ فَهِجْرَتُهُ إِلَى اللَّهِ وَرَسُولِهِ.",
            banglaText = "নিশ্চয়ই সমস্ত আমল নিয়তের ওপর নির্ভরশীল এবং প্রত্যেক মানুষ তার নিয়ত অনুযায়ী প্রতিদান পাবে। অতএব যার হিজরত হবে আল্লাহ ও তাঁর রাসুলের সন্তুষ্টির উদ্দেশ্যে, তার হিজরত আল্লাহ ও তাঁর রাসুলের জন্যই গণ্য হবে।",
            englishText = "Actions are judged by motives, so each man will have what he intended.",
            gradeBn = "সহীহ বুখারী: ১ ও মুসলিম: ১৯০৭",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / রিয়াযুস স্বা-লিহীন ১",
            explanationBn = "ইমাম নববী (রহ.) তাঁর কিতাব এই নিয়তের হাদিস দিয়ে শুরু করেছেন যাতে পাঠক আমলের বিশুদ্ধতা বজায় রাখতে পারেন।",
            hadithTypeBn = "কওলী (বাণী)"
        ),

        // Riyadhus Salihin Hadith 13 (Tawbah)
        HadithEntity(
            id = "riyadus-salihin_13",
            bookSlug = "riyadus-salihin",
            hadithNumber = 13,
            chapterNumber = 2,
            chapterTitleBn = "তাওবাহ বা অনুশোচনা",
            narratorBn = "হযরত আবু হুরায়রা (রা.)",
            arabicText = "وَاللَّهِ إِنِّي لأَسْتَغْفِرُ اللَّهَ وَأَتُوبُ إِلَيْهِ فِي الْيَوْمِ أَكْثَرَ مِنْ سَبْعِينَ مَرَّةً.",
            banglaText = "আল্লাহর শপথ! নিশ্চয়ই আমি দৈনিক সত্তরের অধিকবার আল্লাহর নিকট ক্ষমা প্রার্থনা (ইস্তিগফার) করি এবং তাঁর দিকে তাওবাহ করে প্রত্যাবর্তন করি।",
            englishText = "By Allah! I ask for forgiveness from Allah and turn to Him in repentance more than seventy times a day.",
            gradeBn = "সহীহ বুখারী: ৬৩০৭",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / রিয়াযুস স্বা-লিহীন ১৩",
            explanationBn = "নিষ্পাপ রাসুলুল্লাহ (সা.) দৈনিক সত্তরের অধিকবার তাওবাহ করতেন। সুতরাং সাধারণ মুমিনের জীবনে সার্বক্ষণিক ইস্তিগফারের আবশ্যকতা অপরিসীম।",
            hadithTypeBn = "কওলী ও আমলী"
        ),

        // Bulugh al-Maram Hadith 1 (Water Purity)
        HadithEntity(
            id = "bulugh-al-maram_1",
            bookSlug = "bulugh-al-maram",
            hadithNumber = 1,
            chapterNumber = 1,
            chapterTitleBn = "পবিত্রতা অধ্যায়",
            narratorBn = "হযরত আবু হুরায়রা (রা.)",
            arabicText = "هُوَ الطَّهُورُ مَاؤُهُ، الحِلُّ مَيْتَتُهُ.",
            banglaText = "সমুদ্রের পানি পবিত্র এবং তার মৃত প্রাণী হালাল।",
            englishText = "Its water is purifying and its dead (animals) are lawful to eat.",
            gradeBn = "সহীহ (ইবনে খুযাইমা ও তিরমিজী)",
            gradeColor = "SAHIH",
            sourceBn = "হাদিসবিডি (HadithBD) / বুলুগুল মারাম ১",
            explanationBn = "তাহরাত ও পানির পবিত্রতার মৌলিক ফিকহি দলীল। সমুদ্র বা নদীর পানিতে অযু ও গোসল সম্পূর্ণ জায়েজ এবং সামুদ্রিক মাছ খাওয়া হালাল।",
            hadithTypeBn = "কওলী (বাণী)"
        )
    )
}
