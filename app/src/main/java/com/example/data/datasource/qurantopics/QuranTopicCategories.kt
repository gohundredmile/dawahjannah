package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.NeedBasedTopicEntry
import com.example.data.model.qurantopics.QuranTopicCategory

object QuranTopicCategories {

    val categories: List<QuranTopicCategory> = listOf(
        QuranTopicCategory(
            id = "cat_aqeedah",
            nameBn = "ঈমান, তাওহীদ ও আকীদাহ",
            nameEn = "Faith, Tawheed & Aqeedah",
            iconEmoji = "☪️",
            colorHex = 0xFF0D9488,
            descriptionBn = "আল্লাহর একত্ববাদ, আসমাউল হুসনা, কুদরত, তাওয়াক্কুল ও তাকওয়ার আয়াতসমূহ",
            sortOrder = 1
        ),
        QuranTopicCategory(
            id = "cat_quran",
            nameBn = "আল-কুরআন ও ওহী",
            nameEn = "Holy Quran & Revelation",
            iconEmoji = "📖",
            colorHex = 0xFF2563EB,
            descriptionBn = "কুরআনের হেদায়েত, তাদাব্বুর, তিলাওয়াত, শিফা ও আসমানী কিতাব",
            sortOrder = 2
        ),
        QuranTopicCategory(
            id = "cat_prophets",
            nameBn = "আম্বিয়া ও রাসূলগণ",
            nameEn = "Prophets & Messengers",
            iconEmoji = "🕊️",
            colorHex = 0xFF059669,
            descriptionBn = "আদম, নূহ, ইবরাহীম, মূসা, ঈসা, মুহাম্মদ (ﷺ) সহ সকল নবীদের ঘটনা ও শিক্ষা",
            sortOrder = 3
        ),
        QuranTopicCategory(
            id = "cat_ibadah",
            nameBn = "ইবাদত ও সালাত",
            nameEn = "Worship, Prayer & Fasting",
            iconEmoji = "🕌",
            colorHex = 0xFF047857,
            descriptionBn = "সালাত, ওযু, রমাদান, সিয়াম, যাকাত, হজ্জ, দো'আ ও যিকির",
            sortOrder = 4
        ),
        QuranTopicCategory(
            id = "cat_akhlaq",
            nameBn = "চরিত্র ও আত্মশুদ্ধি",
            nameEn = "Character, Ethics & Akhlaq",
            iconEmoji = "💎",
            colorHex = 0xFF7C3AED,
            descriptionBn = "ধৈর্য, শুকরিয়া, বিনয়, সত্যবাদিতা, ক্ষমা ও অহংকার বর্জন",
            sortOrder = 5
        ),
        QuranTopicCategory(
            id = "cat_family",
            nameBn = "পরিবার, বিবাহ ও দাম্পত্য",
            nameEn = "Family, Marriage & Relations",
            iconEmoji = "👨‍👩‍👧",
            colorHex = 0xFFE11D48,
            descriptionBn = "পিতা-মাতার হক, বিবাহ, তালাক, সন্তান লালন ও এতিমের অধিকার",
            sortOrder = 6
        ),
        QuranTopicCategory(
            id = "cat_society",
            nameBn = "সমাজ ও ন্যায়বিচার",
            nameEn = "Society, Justice & Brotherhood",
            iconEmoji = "⚖️",
            colorHex = 0xFFD97706,
            descriptionBn = "ভ্রাতৃত্ব, ন্যায়বিচার, মানবমর্যাদা, শূরা ও জুলুমের পরিণতি",
            sortOrder = 7
        ),
        QuranTopicCategory(
            id = "cat_halal_haram",
            nameBn = "হালাল, হারাম ও শরীয়াহ বিধান",
            nameEn = "Halal, Haram & Law",
            iconEmoji = "📜",
            colorHex = 0xFFB45309,
            descriptionBn = "খাদ্য-পানীয়, মদ-জুয়া বর্জন, দেনা-পাওনা ও সাক্ষ্যের বিধান",
            sortOrder = 8
        ),
        QuranTopicCategory(
            id = "cat_wealth",
            nameBn = "সম্পদ, জীবিকা ও অর্থনীতি",
            nameEn = "Wealth, Rizq & Economy",
            iconEmoji = "💰",
            colorHex = 0xFF15803D,
            descriptionBn = "হালাল রিজিক, সাদাকাহ, অপচয় রোধ, সুদের কুফল ও মিসকিনের হক",
            sortOrder = 9
        ),
        QuranTopicCategory(
            id = "cat_life_mind",
            nameBn = "মানব সৃষ্টি ও আত্মিক বিষয়",
            nameEn = "Creation, Soul & Peace",
            iconEmoji = "🌱",
            colorHex = 0xFF0284C7,
            descriptionBn = "সৃষ্টির উদ্দেশ্য, প্রশান্তি, দুঃখমুক্তি, নিরাশ না হওয়া ও জ্ঞানার্হণ",
            sortOrder = 10
        ),
        QuranTopicCategory(
            id = "cat_nature_animals",
            nameBn = "সৃষ্টিজগত, প্রকৃতি ও প্রাণী",
            nameEn = "Nature, Universe & Animals",
            iconEmoji = "🌍",
            colorHex = 0xFF16A34A,
            descriptionBn = "মহাবিশ্ব, দিন-রাত, বৃষ্টি, পাহাড়, উদ্ভিদ ও কুরআনে বর্ণিত প্রাণীসমূহ",
            sortOrder = 11
        ),
        QuranTopicCategory(
            id = "cat_akhirah",
            nameBn = "মৃত্যু, কিয়ামত ও পরকাল",
            nameEn = "Death, Judgment & Akhirah",
            iconEmoji = "⏳",
            colorHex = 0xFF475569,
            descriptionBn = "মৃত্যুর স্বাদ, কিয়ামতের প্রলয়, হাশর, আমলনামা ও মীযান",
            sortOrder = 12
        ),
        QuranTopicCategory(
            id = "cat_jannah",
            nameBn = "জান্নাত ও অফুরন্ত নিয়ামত",
            nameEn = "Paradise / Jannah",
            iconEmoji = "🌸",
            colorHex = 0xFF10B981,
            descriptionBn = "জান্নাতের বিবরণ, জান্নাতবাসীদের গুণাবলী ও চিরস্থায়ী শান্তি",
            sortOrder = 13
        ),
        QuranTopicCategory(
            id = "cat_jahannam",
            nameBn = "জাহান্নাম ও কঠোর শাস্তি",
            nameEn = "Hell / Jahannam",
            iconEmoji = "🔥",
            colorHex = 0xFFDC2626,
            descriptionBn = "জাহান্নামের আগুন, আক্ষেপ এবং তা থেকে বাঁচার আকুতি",
            sortOrder = 14
        ),
        QuranTopicCategory(
            id = "cat_history",
            nameBn = "ঐতিহাসিক জাতি ও শিক্ষা",
            nameEn = "Nations & Lessons",
            iconEmoji = "🏛️",
            colorHex = 0xFF854D0E,
            descriptionBn = "আ'দ, সামূদ, বনী ইসরাঈল, আসহাবে কাহাফ ও ফির'আউনের পরিণতি",
            sortOrder = 15
        ),
        QuranTopicCategory(
            id = "cat_dawah_peace",
            nameBn = "দাওয়াহ, শান্তি ও আত্মরক্ষা",
            nameEn = "Da'wah, Peace & Defense",
            iconEmoji = "🤝",
            colorHex = 0xFF0891B2,
            descriptionBn = "হিকমতের সাথে দ্বীনের দাওয়াত, সুন্দর ব্যবহার ও শান্তি বজায় রাখা",
            sortOrder = 16
        )
    )

    val needBasedEntries: List<NeedBasedTopicEntry> = listOf(
        NeedBasedTopicEntry(
            id = "need_anxiety",
            emotionOrNeedBn = "আমি মানসিক কষ্টে আছি / অস্থির লাগছে",
            targetTopicId = "topic_tranquility",
            subtitleBn = "অন্তরের প্রশান্তি ও আল্লাহর সান্নিধ্য",
            iconEmoji = "🌿"
        ),
        NeedBasedTopicEntry(
            id = "need_tawbah",
            emotionOrNeedBn = "আমি ক্ষমা ও তওবা করতে চাই",
            targetTopicId = "topic_tawbah",
            subtitleBn = "আল্লাহর পরম ক্ষমা ও রহমতের আশ্বাস",
            iconEmoji = "🤲"
        ),
        NeedBasedTopicEntry(
            id = "need_rizq",
            emotionOrNeedBn = "আমি রিজিক ও অভাব নিয়ে চিন্তিত",
            targetTopicId = "topic_rizq",
            subtitleBn = "হালাল জীবিকা ও রিজিকের ওয়াদা",
            iconEmoji = "🌾"
        ),
        NeedBasedTopicEntry(
            id = "need_sabr",
            emotionOrNeedBn = "আমি ধৈর্য ধারণ করতে চাই",
            targetTopicId = "topic_patience",
            subtitleBn = "বিপদে অবিচলতা ও সুসংবাদ",
            iconEmoji = "🛡️"
        ),
        NeedBasedTopicEntry(
            id = "need_parents",
            emotionOrNeedBn = "পিতা-মাতার হক ও সেবা জানতে চাই",
            targetTopicId = "topic_parents",
            subtitleBn = "বাবা-মার প্রতি সদ্ব্যবহার ও দোয়া",
            iconEmoji = "👨‍👩‍👧"
        ),
        NeedBasedTopicEntry(
            id = "need_tawakkul",
            emotionOrNeedBn = "বিপদে আল্লাহর ওপর ভরসা রাখতে চাই",
            targetTopicId = "topic_tawakkul",
            subtitleBn = "তাওয়াক্কুল ও পরম আস্থা",
            iconEmoji = "⚓"
        ),
        NeedBasedTopicEntry(
            id = "need_jannah",
            emotionOrNeedBn = "জান্নাতের বিবরণ পড়তে চাই",
            targetTopicId = "topic_jannah",
            subtitleBn = "চিরশান্তির জান্নাত ও অফুরন্ত নেয়ামত",
            iconEmoji = "🌸"
        ),
        NeedBasedTopicEntry(
            id = "need_jahannam",
            emotionOrNeedBn = "জাহান্নাম থেকে মুক্তি ও সতর্কতা",
            targetTopicId = "topic_jahannam",
            subtitleBn = "আগুন থেকে বাঁচার আকুতি ও শিক্ষা",
            iconEmoji = "🔥"
        ),
        NeedBasedTopicEntry(
            id = "need_dua",
            emotionOrNeedBn = "দোয়ার নিয়ম ও আকুতি",
            targetTopicId = "topic_dua",
            subtitleBn = "আল্লাহর কাছে প্রার্থনা ও নিশ্চয়তা",
            iconEmoji = "📿"
        ),
        NeedBasedTopicEntry(
            id = "need_marriage",
            emotionOrNeedBn = "দাম্পত্য জীবন ও ভালোবাসা",
            targetTopicId = "topic_marriage",
            subtitleBn = "স্নেহ, মায়া ও পারস্পরিক শ্রদ্ধা",
            iconEmoji = "💍"
        )
    )
}
