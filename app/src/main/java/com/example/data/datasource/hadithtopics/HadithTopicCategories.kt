package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopicCategory
import com.example.data.model.hadithtopics.NeedBasedHadithEntry

object HadithTopicCategories {

    val categories: List<HadithTopicCategory> = listOf(
        HadithTopicCategory(
            id = "cat_aqeedah",
            nameBn = "ঈমান ও আকীদাহ",
            nameEn = "Faith, Tawheed & Creed",
            iconEmoji = "☪️",
            colorHex = 0xFF0D9488,
            descriptionBn = "ঈমানের স্তম্ভ, আল্লাহর পরিচয়, তাওহীদ, তাকদীর ও শিরক থেকে সতর্কতা",
            sortOrder = 1
        ),
        HadithTopicCategory(
            id = "cat_ibadah",
            nameBn = "ইবাদত ও সালাত",
            nameEn = "Worship, Salah & Sawm",
            iconEmoji = "🕌",
            colorHex = 0xFF047857,
            descriptionBn = "পাঁচ ওয়াক্ত সালাত, জামাত, রমাদান ও সিয়াম, যাকাত, সদকা ও কুরআন শিক্ষা",
            sortOrder = 2
        ),
        HadithTopicCategory(
            id = "cat_taharah",
            nameBn = "পবিত্রতা ও পরিচ্ছন্নতা",
            nameEn = "Purity & Hygiene (Taharah)",
            iconEmoji = "💧",
            colorHex = 0xFF0284C7,
            descriptionBn = "ওযু, গোসল, মিসওয়াক, অপবিত্রতা দূরীকরণ ও পবিত্রতার মহান সওয়াব",
            sortOrder = 3
        ),
        HadithTopicCategory(
            id = "cat_akhlaq",
            nameBn = "আখলাক ও চরিত্র",
            nameEn = "Character, Manners & Akhlaq",
            iconEmoji = "💎",
            colorHex = 0xFF7C3AED,
            descriptionBn = "নিয়তের ইখলাস, সত্যবাদিতা, ধৈর্য, বিনয়, ক্ষমা, রাগ নিয়ন্ত্রণ ও অহংকার বর্জন",
            sortOrder = 4
        ),
        HadithTopicCategory(
            id = "cat_dua_dhikr",
            nameBn = "দোয়া ও যিকির",
            nameEn = "Dua, Dhikr & Istighfar",
            iconEmoji = "🤲",
            colorHex = 0xFF059669,
            descriptionBn = "দোয়া কবুল, সকাল-সন্ধ্যার যিকির, ক্ষমা প্রার্থনা (ইস্তিগফার) ও দরূদ শরীফ",
            sortOrder = 5
        ),
        HadithTopicCategory(
            id = "cat_family",
            nameBn = "পরিবার ও বিবাহ",
            nameEn = "Family, Marriage & Relatives",
            iconEmoji = "👨‍👩‍👧",
            colorHex = 0xFFE11D48,
            descriptionBn = "পিতা-মাতার হক, বিবাহ, স্বামী-স্ত্রীর অধিকার, সন্তান প্রতিপালন ও আত্মীয়তা",
            sortOrder = 6
        ),
        HadithTopicCategory(
            id = "cat_society",
            nameBn = "সমাজ ও মানবকল্যাণ",
            nameEn = "Society, Brotherhood & Helping",
            iconEmoji = "🤝",
            colorHex = 0xFFD97706,
            descriptionBn = "মুসলিম ভ্রাতৃত্ব, প্রতিবেশীর অধিকার, এতিম-অসহায়দের সেবা ও সালামের প্রসার",
            sortOrder = 7
        ),
        HadithTopicCategory(
            id = "cat_business",
            nameBn = "ব্যবসা ও হালাল জীবিকা",
            nameEn = "Business, Halal Earning & Wealth",
            iconEmoji = "💰",
            colorHex = 0xFF15803D,
            descriptionBn = "হালাল উপার্জন, ব্যবসায় সততা, ঋণ পরিশোধের গুরুত্ব ও সুদের নিষেধাজ্ঞা",
            sortOrder = 8
        ),
        HadithTopicCategory(
            id = "cat_ilm",
            nameBn = "জ্ঞান ও শিক্ষা",
            nameEn = "Knowledge, Education & Da'wah",
            iconEmoji = "📚",
            colorHex = 0xFF2563EB,
            descriptionBn = "দ্বীনি ইলম অন্বেষণ, আলেমদের মর্যাদা, শিক্ষা প্রচার ও কল্যাণকামিতা",
            sortOrder = 9
        ),
        HadithTopicCategory(
            id = "cat_health",
            nameBn = "স্বাস্থ্য, চিকিৎসা ও খাদ্য",
            nameEn = "Health, Medicine & Sunnah Food",
            iconEmoji = "🌿",
            colorHex = 0xFF16A34A,
            descriptionBn = "অসুস্থতা, রোগীর সেবা, রুকিয়াহ, কালোজিরা, মধু ও সুন্নাত অনুযায়ী আহার",
            sortOrder = 10
        ),
        HadithTopicCategory(
            id = "cat_akhirah",
            nameBn = "মৃত্যু ও আখিরাত",
            nameEn = "Death, Grave & Hereafter",
            iconEmoji = "⏳",
            colorHex = 0xFF475569,
            descriptionBn = "মৃত্যুর স্মরণ, কবরের জীবন, হাশরের ময়দান, জান্নাতের সুসংবাদ ও জাহান্নাম",
            sortOrder = 11
        ),
        HadithTopicCategory(
            id = "cat_fitnah",
            nameBn = "ফিতনা ও শেষ যমানা",
            nameEn = "Fitnah & Signs of the Last Hour",
            iconEmoji = "⚠️",
            colorHex = 0xFFDC2626,
            descriptionBn = "শেষ যুগের কঠিন পরিস্থিতি, কিয়ামতের আলামত ও ঈমান রক্ষার পথনির্দেশনা",
            sortOrder = 12
        )
    )

    val needBasedEntries: List<NeedBasedHadithEntry> = listOf(
        NeedBasedHadithEntry(
            id = "need_bipod",
            questionBn = "বিপদে পড়লে বা কঠিন সময়ে কী করব?",
            subtitleBn = "ধৈর্য, আল্লাহর ওপর তাওয়াক্কুল ও সুরক্ষার হাদিস",
            targetTopicId = "topic_sabr_hardship",
            iconEmoji = "🛡️"
        ),
        NeedBasedHadithEntry(
            id = "need_anxiety",
            questionBn = "দুশ্চিন্তা ও মানসিক অস্থিরতায় কী আমল?",
            subtitleBn = "হৃদয়ের প্রশান্তি, ইস্তিগফার ও দোয়ার হাদিস",
            targetTopicId = "topic_dua_acceptance",
            iconEmoji = "🌿"
        ),
        NeedBasedHadithEntry(
            id = "need_parents",
            questionBn = "পিতা-মাতার সাথে কেমন আচরণ করব?",
            subtitleBn = "মা-বাবার সন্তুষ্টি, সেবা ও জান্নাতের হাদিস",
            targetTopicId = "topic_parents_rights",
            iconEmoji = "👨‍👩‍👧"
        ),
        NeedBasedHadithEntry(
            id = "need_anger",
            questionBn = "রাগ ও ক্ষোভ নিয়ন্ত্রণ করার উপায় কী?",
            subtitleBn = "প্রকৃত বীর, ক্রোধ সংবরণ ও কোমলতার হাদিস",
            targetTopicId = "topic_anger_control",
            iconEmoji = "🧘"
        ),
        NeedBasedHadithEntry(
            id = "need_salah",
            questionBn = "সালাতে যত্নবান ও নিয়মিত হওয়ার হাদিস",
            subtitleBn = "সালাতের গুরুত্ব, জামাত ও পাপ মোচনের হাদিস",
            targetTopicId = "topic_salah_importance",
            iconEmoji = "🕌"
        ),
        NeedBasedHadithEntry(
            id = "need_tawbah",
            questionBn = "গুনাহ মাফ ও তওবা করার হাদিস",
            subtitleBn = "আল্লাহর ক্ষমা, রহমত ও খাঁটি তওবার হাদিস",
            targetTopicId = "topic_istighfar_tawbah",
            iconEmoji = "🤲"
        ),
        NeedBasedHadithEntry(
            id = "need_rizq",
            questionBn = "হালাল জীবিকা ও বরকত লাভের হাদিস",
            subtitleBn = "হালাল উপার্জন, সাদাকাহ ও সততার হাদিস",
            targetTopicId = "topic_halal_earning",
            iconEmoji = "🌾"
        ),
        NeedBasedHadithEntry(
            id = "need_jannah",
            questionBn = "জান্নাত লাভের সহজ আমলসমূহ কী কী?",
            subtitleBn = "উত্তম চরিত্র, কুরআন শিক্ষা ও নেক আমলের হাদিস",
            targetTopicId = "topic_jannah_deeds",
            iconEmoji = "🌸"
        ),
        NeedBasedHadithEntry(
            id = "need_sawm",
            questionBn = "রমাদান ও সিয়ামের ফযীলত কী?",
            subtitleBn = "অতীতের গুনাহ মাফ ও জাহান্নাম থেকে আত্মরক্ষার ঢাল",
            targetTopicId = "topic_sawm_ramadan_hadith",
            iconEmoji = "🌙"
        ),
        NeedBasedHadithEntry(
            id = "need_zakat",
            questionBn = "দান-সাদাকাহর বরকত ও ফজিলত কী?",
            subtitleBn = "সম্পদ বৃদ্ধি, বালা-মুসিবত দূর ও হাসিমুখে সদকা",
            targetTopicId = "topic_zakat_sadaqah_hadith",
            iconEmoji = "🤝"
        ),
        NeedBasedHadithEntry(
            id = "need_neighbor",
            questionBn = "প্রতিবেশীর হক ও অধিকার কী কী?",
            subtitleBn = "কষ্ট না দেওয়া, সমাদর ও জিবরীলের তাগিদের হাদিস",
            targetTopicId = "topic_neighbor_rights",
            iconEmoji = "🏡"
        ),
        NeedBasedHadithEntry(
            id = "need_kinship",
            questionBn = "আত্মীয়তার সম্পর্ক বজায় রাখার সুসংবাদ কী?",
            subtitleBn = "রিজিকে প্রাচুর্য, দীর্ঘায়ু ও সম্পর্ক না ভাঙার হাদিস",
            targetTopicId = "topic_kinship_silat_rahim",
            iconEmoji = "👨‍👩‍👧‍👦"
        ),
        NeedBasedHadithEntry(
            id = "need_marriage",
            questionBn = "স্ত্রীর সাথে কেমন আচরণ করব ও বিবাহ?",
            subtitleBn = "স্ত্রীর কাছে সর্বোত্তম হওয়া ও যুবসমাজের প্রতি নসীহত",
            targetTopicId = "topic_marriage_family_hadith",
            iconEmoji = "💍"
        ),
        NeedBasedHadithEntry(
            id = "need_sick",
            questionBn = "অসুস্থ হলে ও রোগমুক্তি (শিফা) লাভের সুন্নাত কী?",
            subtitleBn = "রোগীর সেবা, কালোজিরা ও সুন্নাহ চিকিৎসার হাদিস",
            targetTopicId = "topic_visiting_sick_sunnah_cure",
            iconEmoji = "🌿"
        ),
        NeedBasedHadithEntry(
            id = "need_death",
            questionBn = "মৃত্যুর স্মরণ ও কবরের প্রশ্নোত্তর প্রস্তুতি",
            subtitleBn = "মুনকার-নাকীর ও স্বাদ বিনষ্টকারী মউতের স্মরণ",
            targetTopicId = "topic_death_remembrance",
            iconEmoji = "⏳"
        ),
        NeedBasedHadithEntry(
            id = "need_hell",
            questionBn = "জাহান্নামের আগুন থেকে বাঁচার সহজ উপায় কী?",
            subtitleBn = "খেজুরের টুকরো বা ভালো কথার বিনিময়ে আত্মরক্ষা",
            targetTopicId = "topic_hell_torments",
            iconEmoji = "🔥"
        ),
        NeedBasedHadithEntry(
            id = "need_durood",
            questionBn = "দরূদ শরীফ পাঠ ও নবীজির প্রতি ভালোবাসা",
            subtitleBn = "দশটি রহমত নাযিল ও ঈমানের শ্রেষ্ঠ মাপকাঠি",
            targetTopicId = "topic_durood_love_prophet",
            iconEmoji = "💚"
        ),
        NeedBasedHadithEntry(
            id = "need_humility",
            questionBn = "বিনয় ও অহংকারমুক্ত জীবন যাপন করার উপায় কী?",
            subtitleBn = "মর্যাদা বৃদ্ধি, কোমল আচরণ ও অহংকারের কঠিন পরিণতি",
            targetTopicId = "topic_humility_tawadu",
            iconEmoji = "🌱"
        ),
        NeedBasedHadithEntry(
            id = "need_bidah",
            questionBn = "বিদ‘আত ও দ্বীনে নতুন আবিষ্কার থেকে বাঁচার উপায়?",
            subtitleBn = "সুন্নাতকে দাঁত দিয়ে আঁকড়ে ধরা ও প্রত্যাখ্যাত আমল",
            targetTopicId = "topic_bidah_rejection",
            iconEmoji = "🚫"
        ),
        NeedBasedHadithEntry(
            id = "need_tongue",
            questionBn = "জিহ্বা সংযত রাখা ও গীবতের ভয়াবহতা কী?",
            subtitleBn = "ভালো কথা বা নীরবতা এবং জান্নাতের জামিন লাভের হাদিস",
            targetTopicId = "topic_guarding_tongue_gheebat",
            iconEmoji = "🤐"
        ),
        NeedBasedHadithEntry(
            id = "need_tahajjud",
            questionBn = "তাহাজ্জুদ ও রাতের নিস্তব্ধতায় রবের সান্নিধ্য",
            subtitleBn = "ফরজের পর শ্রেষ্ঠ সালাত ও শেষ রাতে দোয়া কবুল",
            targetTopicId = "topic_tahajjud_qiyam",
            iconEmoji = "🌌"
        ),
        NeedBasedHadithEntry(
            id = "need_jumuah",
            questionBn = "জুমার দিনের মর্যাদা ও খুতবার সুন্নাত আদব",
            subtitleBn = "দিনের শ্রেষ্ঠত্ব, গোসল, সুবাস ও দোয়া কবুলের মুহূর্ত",
            targetTopicId = "topic_jumuah_virtues_adab",
            iconEmoji = "🕌"
        ),
        NeedBasedHadithEntry(
            id = "need_orphans",
            questionBn = "এতিম ও অসহায় বিধবাদের সাহায্য করার সওয়াব",
            subtitleBn = "জান্নাতে নবীজির সাথে থাকার দুর্লভ সুসংবাদ",
            targetTopicId = "topic_orphans_widows_rights",
            iconEmoji = "🤲"
        ),
        NeedBasedHadithEntry(
            id = "need_daughters",
            questionBn = "কন্যা সন্তান প্রতিপালন ও জান্নাতের সান্নিধ্য",
            subtitleBn = "স্নেহ-যত্নে বড় করার জান্নাতী পুরস্কার ও সুশিক্ষা",
            targetTopicId = "topic_raising_daughters_children",
            iconEmoji = "👧"
        ),
        NeedBasedHadithEntry(
            id = "need_youth",
            questionBn = "যুবসমাজের চারিত্রিক পবিত্রতা ও যৌবনের ইবাদত",
            subtitleBn = "আরশের ছায়াতলে আশ্রয় ও পাঁচটি মহামূল্যবান সুযোগ",
            targetTopicId = "topic_youth_chastity",
            iconEmoji = "⚡"
        ),
        NeedBasedHadithEntry(
            id = "need_riba",
            questionBn = "সুদের ভয়াবহ গুনাহ ও হারাম থেকে সুরক্ষার উপায়",
            subtitleBn = "সুদের বিরুদ্ধে লানত ও হালাল খাদ্যের অপরিহার্যতা",
            targetTopicId = "topic_riba_interest_severity",
            iconEmoji = "⚠️"
        ),
        NeedBasedHadithEntry(
            id = "need_tawakkul",
            questionBn = "আল্লাহর ওপর ভরসা (তাওয়াক্কুল) ও তাকদীরে সন্তুষ্টি",
            subtitleBn = "পাখির মতো রিযিক ও ভাগ্যের ফয়সালায় শান্তি খোঁজা",
            targetTopicId = "topic_tawakkul_qadar",
            iconEmoji = "🕊️"
        ),
        NeedBasedHadithEntry(
            id = "need_hasad",
            questionBn = "হিংসা-বিদ্বেষ থেকে অন্তরকে পবিত্র রাখার আমল",
            subtitleBn = "নেক আমল ভস্ম হওয়া থেকে রক্ষা ও ক্ষমা লাভের শর্ত",
            targetTopicId = "topic_hasad_kibr",
            iconEmoji = "🔥"
        ),
        NeedBasedHadithEntry(
            id = "need_janazah",
            questionBn = "জানাযার নামাজ ও কবর জিয়ারতের সওয়াব কত?",
            subtitleBn = "উহুদ পাহাড়সম দুই কিরাত নেকি ও আখিরাতের স্মরণ",
            targetTopicId = "topic_janazah_cemetery_rights",
            iconEmoji = "⚰️"
        ),
        NeedBasedHadithEntry(
            id = "need_shafaat",
            questionBn = "রাসুলুল্লাহ (ﷺ)-এর শাফায়াত ও কাউসারের সুসংবাদ",
            subtitleBn = "কিয়ামতের উত্তাপে স্নিগ্ধ সুধা পান ও সুপারিশের সুসংবাদ",
            targetTopicId = "topic_shafaat_kawthar",
            iconEmoji = "⛲"
        ),
        NeedBasedHadithEntry(
            id = "need_sleep",
            questionBn = "সুন্নাত অনুযায়ী ঘুমানো ও সত্য স্বপ্নের পথনির্দেশ",
            subtitleBn = "ঘুমানোর মাসনূন দোয়া ও দুঃস্বপ্ন দেখলে করণীয়",
            targetTopicId = "topic_sleeping_dream_sunnah",
            iconEmoji = "🛌"
        ),
        NeedBasedHadithEntry(
            id = "need_fitnah",
            questionBn = "দাজ্জাল ও শেষ যামানার ফিতনা থেকে মুক্তির আমল",
            subtitleBn = "সূরা কাহাফের প্রথম দশ আয়াত ও তাশাহহুদের চার আশ্রয়",
            targetTopicId = "topic_protection_fitnah_dajjal",
            iconEmoji = "🛡️"
        )
    )
}
