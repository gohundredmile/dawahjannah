package com.example.data.datasource

import com.example.data.model.HabitItem

object HabitData {

    val dailyHabits: List<HabitItem> = listOf(
        HabitItem(
            id = "habit_fajr",
            titleBn = "ফজর সালাত (জামাত / ওয়াক্তমতো)",
            descriptionBn = "ফজরের দুই রাকাত সুন্নাত ও তাকবীরে উলার সাথে ফরজ আদায়",
            categoryBn = "ফরজ সালাত"
        ),
        HabitItem(
            id = "habit_dhuhr",
            titleBn = "যোহর সালাত (সুন্নাত সহ জামাতে)",
            descriptionBn = "৪ রাকাত পূর্ব সুন্নাত + ৪ রাকাত ফরজ + ২ রাকাত সুন্নাত",
            categoryBn = "ফরজ সালাত"
        ),
        HabitItem(
            id = "habit_asr",
            titleBn = "আসর সালাত (ওয়াক্তমতো জামাতে)",
            descriptionBn = "আসরের পূর্ব সুন্নাত ও জামাতে ৪ রাকাত ফরজ সালাত",
            categoryBn = "ফরজ সালাত"
        ),
        HabitItem(
            id = "habit_maghrib",
            titleBn = "মাগরিব সালাত ও আওয়াবিন",
            descriptionBn = "৩ রাকাত ফরজ + ২ রাকাত সুন্নাত ও নফল আদায়",
            categoryBn = "ফরজ সালাত"
        ),
        HabitItem(
            id = "habit_isha",
            titleBn = "এশা ও বিতর সালাত",
            descriptionBn = "এশার ফরজ, সুন্নাত ও বিতর সালাত সম্পন্ন করা",
            categoryBn = "ফরজ সালাত"
        ),
        HabitItem(
            id = "habit_morning_adhkar",
            titleBn = "সকালের মাসনুন আজকার ও আয়াতুল কুরসী",
            descriptionBn = "সকাল বেলার হিফাজতি দোয়া ও তিন কুল পাঠ",
            categoryBn = "দৈনিক আজকার"
        ),
        HabitItem(
            id = "habit_evening_adhkar",
            titleBn = "সন্ধ্যার মাসনুন আজকার",
            descriptionBn = "সন্ধ্যাপূর্ব হিফাজতি দোয়া ও সাইয়্যিদুল ইস্তিগফার",
            categoryBn = "দৈনিক আজকার"
        ),
        HabitItem(
            id = "habit_quran",
            titleBn = "কুরআন তিলাওয়াত (সূরা মুলক / ইয়াসিন / কাহাফ)",
            descriptionBn = "প্রতিদিন অন্তত এক রুকু তিলাওয়াত ও রাতে সূরা মুলক পাঠ",
            categoryBn = "কুরআন তিলাওয়াত"
        ),
        HabitItem(
            id = "habit_nawafil",
            titleBn = "তাহাজ্জুদ / ইশরাক / চাশত সালাত",
            descriptionBn = "নফল নামাজের মাধ্যমে আল্লাহর নৈকট্য অন্বেষণ",
            categoryBn = "নফল সালাত"
        ),
        HabitItem(
            id = "habit_istighfar_100",
            titleBn = "১০০ বার ইস্তিগফার পাঠ",
            descriptionBn = "হৃদয় পরিশুদ্ধিতে একমনে ১০০ বার 'আস্তাগফিরুল্লাহ' পড়া",
            categoryBn = "তাসবীহ ও জিকির"
        ),
        HabitItem(
            id = "habit_durood_100",
            titleBn = "১০০ বার দরূদ শরীফ পাঠ",
            descriptionBn = "প্রিয় নবী (সা.)-এর প্রতি ভালোবাসায় ১০০ বার দরূদ পড়া",
            categoryBn = "তাসবীহ ও জিকির"
        ),
        HabitItem(
            id = "habit_subhanallah_100",
            titleBn = "১০০ বার সুবহানাল্লাহি ওয়া বিহামদিহী",
            descriptionBn = "সমুদ্রের ফেনা পরিমাণ পাপ ক্ষমা এবং জান্নাতে বৃক্ষরোপণ",
            categoryBn = "তাসবীহ ও জিকির"
        ),
        HabitItem(
            id = "habit_sadaqah_smile",
            titleBn = "সদকা ও মুচকি হাসির সুন্নাত",
            descriptionBn = "কারো মুখে হাসি ফোটানো, দান করা বা ভালো কথা বলা",
            categoryBn = "সামাজিক সুন্নাত"
        )
    )
}
