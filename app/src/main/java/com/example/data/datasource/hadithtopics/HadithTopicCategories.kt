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
        )
    )
}
