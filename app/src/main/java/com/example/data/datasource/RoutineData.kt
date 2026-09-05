package com.example.data.datasource

import com.example.data.model.RoutineItem

object RoutineData {

    data class TimeSlot(val id: String, val titleBn: String, val timeRangeBn: String)

    val timeSlots = listOf(
        TimeSlot("all", "সকল আমল", "২৪ ঘণ্টা"),
        TimeSlot("top10", "শীর্ষ ১০ আমল", "সর্বোচ্চ অগ্রাধিকার"),
        TimeSlot("tahajjud", "তাহাজ্জুদ ও সেহরি", "রাত ৩:০০ - ভোর ৪:২০"),
        TimeSlot("fajr_sunrise", "ফজর ও প্রভাত", "ভোর ৪:৩০ - সকাল ৬:০০"),
        TimeSlot("ishraq_chasht", "ইশরাক ও চাশত", "সকাল ৬:২০ - সকাল ১০:৩০"),
        TimeSlot("work", "কর্মক্ষেত্র ও পড়াশোনা", "সকাল ৯:০০ - দুপুর ১:০০"),
        TimeSlot("dhuhr", "যোহর ও কাইলুলা", "দুপুর ১:০০ - বিকেল ৩:৩০"),
        TimeSlot("asr_evening", "আসর ও সন্ধ্যা", "বিকেল ৪:৩০ - সন্ধ্যা ৬:১০"),
        TimeSlot("maghrib", "মাগরিব ও আওয়াবিন", "সন্ধ্যা ৬:১৫ - রাত ৭:৩০"),
        TimeSlot("isha_sleep", "এশা ও নিদ্রা প্রস্তুতি", "রাত ৭:৪৫ - রাত ১১:০০")
    )

    val routineList: List<RoutineItem> = listOf(
        // Slot 1: Tahajjud
        RoutineItem(
            id = "routine_tahajjud_1",
            timeSlotId = "tahajjud",
            timeSlotTitleBn = "তাহাজ্জুদ ও সেহরি পূর্ববর্তী আমল",
            titleBn = "তাহাজ্জুদ সালাত ও একাকী মোনাজাত",
            subtitleBn = "রাতের শেষ তৃতীয়াংশে রবের সান্নিধ্য অর্জন",
            descriptionBn = "অজু করে কমপক্ষে ২ থেকে ৮ রাকাত তাহাজ্জুদের নামাজ ধীরস্থিরভাবে আদায় করা। সিজদায় অশ্রু ফেলে নিজের ও উম্মাহর জন্য ক্ষমা ও জান্নাত প্রার্থনা করা।",
            virtuesRewardBn = "ফরজ নামাজের পর সর্বোত্তম নামাজ হলো রাতের নামাজ। এ সময়ে মহান আল্লাহ প্রথম আসমানে নেমে এসে বান্দার দোয়া কবুল করেন।",
            reference = "সহীহ মুসলিম: ১১৬৩, বুখারী: ১১৪৫",
            isTopPriority = true,
            priorityRank = 1
        ),
        RoutineItem(
            id = "routine_tahajjud_2",
            timeSlotId = "tahajjud",
            timeSlotTitleBn = "তাহাজ্জুদ ও সেহরি পূর্ববর্তী আমল",
            titleBn = "সেহরির সময়ে ইস্তিগফার পাঠ",
            subtitleBn = "সাহার প্রহরে ক্ষমা প্রার্থনাকারীদের অন্তর্ভুক্ত হওয়া",
            descriptionBn = "সুবহে সাদিকের ঠিক পূর্বে অন্তত ৭০-১০০ বার 'আস্তাগফিরুল্লাহ' বা সাইয়্যিদুল ইস্তিগফার পাঠ করা।",
            virtuesRewardBn = "কুরআনে জান্নাতীদের গুণ বর্ণনা করে বলা হয়েছে: 'এবং তারা রাতের শেষ প্রহরে ক্ষমা প্রার্থনা করত।' (সূরা যারিয়াত: ১৮)",
            reference = "সূরা আয-যারিয়াত: ১৮, সূরা আল-ইমরান: ১৭",
            isTopPriority = false,
            priorityRank = 12
        ),

        // Slot 2: Fajr to Sunrise
        RoutineItem(
            id = "routine_fajr_1",
            timeSlotId = "fajr_sunrise",
            timeSlotTitleBn = "ফজর, সকালের মাসনুন আজকার ও তিলাওয়াত",
            titleBn = "ফজরের দুই রাকাত সুন্নাত ও জামাতে ফরজ",
            subtitleBn = "পৃথিবী ও তার মধ্যকার সবকিছুর চেয়ে উত্তম সম্পদ",
            descriptionBn = "আযানের জবাব দিয়ে ফজরের সুন্নাত ধীরেসুস্থে পড়া। এরপর মসজিদে গিয়ে জামাতের সাথে তাকবীরে উলার সাথে ফজরের ফরজ নামাজ আদায় করা।",
            virtuesRewardBn = "ফজরের দুই রাকাত সুন্নাত দুনিয়া ও তার মাঝে যা কিছু আছে তার চেয়ে উত্তম। আর যে ব্যক্তি জামাতে ফজর পড়ল সে যেন সারারাত দাঁড়িয়ে নামাজ পড়ল।",
            reference = "সহীহ মুসলিম: ৭২৫, ৬৫৬",
            isTopPriority = true,
            priorityRank = 2
        ),
        RoutineItem(
            id = "routine_fajr_2",
            timeSlotId = "fajr_sunrise",
            timeSlotTitleBn = "ফজর, সকালের মাসনুন আজকার ও তিলাওয়াত",
            titleBn = "সকালের মাসনুন আজকার ও আয়াতুল কুরসী",
            subtitleBn = "সারাদিনের আত্মিক বর্ম ও শয়তান থেকে নিরাপত্তা",
            descriptionBn = "ফরজ নামাজের সালাম ফিরিয়ে ৩ বার ইস্তিগফার, আয়াতুল কুরসী, তাসবিহে ফাতেমী (৩৩ বার সুবহানাল্লাহ, ৩৩ বার আলহামদুলিল্লাহ, ৩৪ বার আল্লাহু আকবার) ও সকালের হিফাজতি আজকার পড়া।",
            virtuesRewardBn = "যে ব্যক্তি সকালের আজকার পড়ে সে সারাদিন আল্লাহর পূর্ণ আশ্রয়ে ও ফেরেশতাদের পাহারায় থাকে।",
            reference = "সহীহ বুখারী: ২৩১১, সহীহ মুসলিম: ২৭০৯",
            isTopPriority = true,
            priorityRank = 3
        ),
        RoutineItem(
            id = "routine_fajr_3",
            timeSlotId = "fajr_sunrise",
            timeSlotTitleBn = "ফজর, সকালের মাসনুন আজকার ও তিলাওয়াত",
            titleBn = "দৈনিক কুরআন তিলাওয়াত ও তাদাব্বুর",
            subtitleBn = "প্রতিদিন অন্তত এক রুকু বা আধা পারা পাঠ",
            descriptionBn = "সকালে মন ও মস্তিষ্ক সতেজ থাকা অবস্থায় অর্থ ও চিন্তা-ভাবনা সহকারে কুরআন কারীম তিলাওয়াত করা।",
            virtuesRewardBn = "কুরআনের প্রতিটি হরফ তিলাওয়াতে ১০টি নেকী অর্জিত হয়। নিশ্চয়ই ফজরের কুরআন পাঠ ফেরেশতাদের সাক্ষ্যপ্রাপ্ত।",
            reference = "সূরা বনী ইসরাঈল: ৭৮, তিরমিযী: ২৯১০",
            isTopPriority = true,
            priorityRank = 4
        ),

        // Slot 3: Ishraq & Chasht
        RoutineItem(
            id = "routine_ishraq_1",
            timeSlotId = "ishraq_chasht",
            timeSlotTitleBn = "ইশরাক ও চাশতের সালাত",
            titleBn = "ইশরাক নামাজ (সূর্যোদয়ের ১৫-২০ মিনিট পর)",
            subtitleBn = "একটি পূর্ণ হজ্জ ও ওমরার সওয়াব লাভ",
            descriptionBn = "ফজরের পর জায়নামাজে বসে জিকির-তিলাওয়াতে সময় কাটিয়ে সূর্য পুরোপুরি উঠলে ২ রাকাত ইশরাকের নামাজ পড়া।",
            virtuesRewardBn = "যে ব্যক্তি ফজরের পর বসে জিকির করে সূর্য ওঠার পর দুই রাকাত নামাজ পড়ে, সে একটি পূর্ণ হজ্জ ও ওমরার সমান সওয়াব পায়।",
            reference = "জামে তিরমিযী: ৫৮৬ (হাসান)",
            isTopPriority = true,
            priorityRank = 5
        ),
        RoutineItem(
            id = "routine_ishraq_2",
            timeSlotId = "ishraq_chasht",
            timeSlotTitleBn = "ইশরাক ও চাশতের সালাত",
            titleBn = "সালাতুদ দুহা / চাশতের সালাত",
            subtitleBn = "দেহের ৩৬০টি গ্রন্থির সদকা আদায়",
            descriptionBn = "সকাল ৯টা থেকে ১১টার মধ্যে ২ থেকে ৮ রাকাত চাশতের নফল নামাজ আদায় করা।",
            virtuesRewardBn = "মানুষের শরীরে ৩৬০টি গ্রন্থি রয়েছে, যার প্রত্যেকটির জন্য প্রতিদিন সদকা দেওয়া আবশ্যক। দুহার দুই রাকাত নামাজ এ সবকিছুর বিকল্প হিসেবে যথেষ্ট।",
            reference = "সহীহ মুসলিম: ৭২০",
            isTopPriority = false,
            priorityRank = 11
        ),

        // Slot 4: Work & Study
        RoutineItem(
            id = "routine_work_1",
            timeSlotId = "work",
            timeSlotTitleBn = "হালাল রুজি ও কর্মক্ষেত্রের সুন্নাত",
            titleBn = "সততা ও ইখলাসের সাথে হালাল উপার্জন",
            subtitleBn = "রুজি অন্বেষণকে শ্রেষ্ঠ ইবাদতে রূপান্তর",
            descriptionBn = "কাজে যাওয়ার পূর্বে নিয়ত পরিশুদ্ধ করা, মিথ্যা ও ধোঁকাবাজি থেকে বিরত থাকা, মুচকি হাসি দিয়ে সহকর্মীদের সাথে আচরণ করা এবং আমানত রক্ষা করা।",
            virtuesRewardBn = "সত্যবাদী ও বিশ্বস্ত ব্যবসায়ী কিয়ামতের দিন নবী, সিদ্দিকীন ও শহীদদের সাথে থাকবে। নিজের হাতের উপার্জিত খাদ্যের চেয়ে উত্তম খাদ্য কেউ কখনো খায়নি।",
            reference = "তিরমিযী: ১২০৯, সহীহ বুখারী: ২০৭২",
            isTopPriority = false,
            priorityRank = 13
        ),

        // Slot 5: Dhuhr & Qailula
        RoutineItem(
            id = "routine_dhuhr_1",
            timeSlotId = "dhuhr",
            timeSlotTitleBn = "যোহর ও কাইলুলা",
            titleBn = "যোহরের ১২ রাকাত সালাত আদায়",
            subtitleBn = "৪ রাকাত সুন্নাতে মুয়াক্কাদা + ৪ রাকাত ফরজ + ২ রাকাত সুন্নাত + ২ রাকাত নফল",
            descriptionBn = "আযানের পর কাজের ব্যস্ততা থামিয়ে ওজু করে মসজিদে প্রথম কাতারে শরিক হওয়া।",
            virtuesRewardBn = "যে ব্যক্তি দিনে-রাতে ১২ রাকাত সুন্নাতে মুয়াক্কাদা আদায় করবে, তার জন্য জান্নাতে একটি সুরম্য প্রাসাদ তৈরি করা হবে।",
            reference = "সহীহ মুসলিম: ৭২৮",
            isTopPriority = true,
            priorityRank = 6
        ),
        RoutineItem(
            id = "routine_dhuhr_2",
            timeSlotId = "dhuhr",
            timeSlotTitleBn = "যোহর ও কাইলুলা",
            titleBn = "কাইলুলা (দুপুরে ক্ষণিক বিশ্রাম)",
            subtitleBn = "তাহাজ্জুদ ও কাজের শক্তি সঞ্চয়ের সুন্নাত",
            descriptionBn = "যোহরের পর অথবা দুপুরের খাবারের পর ১৫ থেকে ৩০ মিনিট চোখ বন্ধ করে বিশ্রাম নেওয়া।",
            virtuesRewardBn = "রাসূলুল্লাহ (সা.) বলেছেন: 'তোমরা কাইলুলা কর, কারণ শয়তান কাইলুলা করে না।' এটি মস্তিষ্কের কার্যক্ষমতা বাড়ায়।",
            reference = "তাবারানী আওসাত: ২৮৩৬, সিলসিলা সহীহাহ: ১৬৪৭",
            isTopPriority = false,
            priorityRank = 14
        ),

        // Slot 6: Asr & Evening
        RoutineItem(
            id = "routine_asr_1",
            timeSlotId = "asr_evening",
            timeSlotTitleBn = "আসর ও সন্ধ্যার হিফাজতি আমল",
            titleBn = "আসরের নামাজ সময়মতো আদায় ও আমল রক্ষা",
            subtitleBn = "আমল বিনষ্ট হওয়া থেকে সতর্কতা",
            descriptionBn = "আসরের পূর্বের ৪ রাকাত নফল আদায় ও জামাতে আসরের ফরজ সম্পন্ন করা। আসরের পর মাগরিব পর্যন্ত জিকিরে মগ্ন থাকা।",
            virtuesRewardBn = "যে ব্যক্তি আসরের নামাজ নষ্ট করল তার সমস্ত আমল বিনষ্ট হয়ে গেল। যে ব্যক্তি ফজর ও আসর হেফাজত করবে সে জাহান্নামে যাবে না।",
            reference = "সহীহ বুখারী: ৫৫৩, সহীহ মুসলিম: ৬৩৫",
            isTopPriority = true,
            priorityRank = 7
        ),
        RoutineItem(
            id = "routine_asr_2",
            timeSlotId = "asr_evening",
            timeSlotTitleBn = "আসর ও সন্ধ্যার হিফাজতি আমল",
            titleBn = "সন্ধ্যার মাসনুন আজকার ও তিন কুল",
            subtitleBn = "রাতব্যাপী হিংসা, জাদু ও অনিষ্ট থেকে নিরাপত্তা",
            descriptionBn = "সূর্যাস্তের পূর্বে সূরা ইখলাস, ফালাক, নাস ৩ বার করে পাঠ, সাইয়্যিদুল ইস্তিগফার এবং 'বিসমিল্লাহিল্লাযী লা ইয়াদুররু...' ৩ বার পাঠ।",
            virtuesRewardBn = "সন্ধ্যার আজকার পাঠকারী ব্যক্তি সকাল পর্যন্ত যেকোনো আকস্মিক বিপদ, বিষাক্ত প্রাণীর দংশন ও শয়তানের অনিষ্ট হতে সুরক্ষিত থাকে।",
            reference = "সুনানে আবু দাউদ: ৫০৮৮, তিরমিযী: ৩৫৭৫",
            isTopPriority = true,
            priorityRank = 8
        ),

        // Slot 7: Maghrib & Awwabin
        RoutineItem(
            id = "routine_maghrib_1",
            timeSlotId = "maghrib",
            timeSlotTitleBn = "মাগরিব ও রাতের শুরু",
            titleBn = "মাগরিবের সালাত ও আওয়াবিনের আমল",
            subtitleBn = "সন্ধ্যার সূচনাতে তওবাকারীদের নামাজ",
            descriptionBn = "মাগরিবের ৩ রাকাত ফরজ ও ২ রাকাত সুন্নাত শেষে ২ থেকে ৬ রাকাত আওয়াবিনের নফল নামাজ আদায় করা। শিশুদের ঘরে ফিরিয়ে আনা।",
            virtuesRewardBn = "যে ব্যক্তি মাগরিবের পর অনর্থক কথা না বলে ৬ রাকাত নফল পড়বে, তাকে ১২ বছরের নফল ইবাদতের সওয়াব দেওয়া হবে।",
            reference = "জামে তিরমিযী: ৪৩৫, ইবনে মাজাহ: ১৩৭৪",
            isTopPriority = false,
            priorityRank = 15
        ),

        // Slot 8: Isha & Sleep Prep
        RoutineItem(
            id = "routine_isha_1",
            timeSlotId = "isha_sleep",
            timeSlotTitleBn = "এশা, সুরা মুলক ও ঘুমানোর সুন্নাত",
            titleBn = "এশার নামাজ জামাতে ও বিতর সালাত",
            subtitleBn = "অর্ধরাত্রি ইবাদতের সওয়াব ও দিনের সমাপ্তি",
            descriptionBn = "এশার ফরজ জামাতে পড়ে বিতর নামাজ আদায় করা। বিতরের পর ঘুমানোর প্রস্তুতি নেওয়া।",
            virtuesRewardBn = "যে ব্যক্তি এশার নামাজ জামাতে আদায় করল সে যেন অর্ধরাত্রি জেগে নফল নামাজ পড়ল।",
            reference = "সহীহ মুসলিম: ৬৫৬",
            isTopPriority = true,
            priorityRank = 9
        ),
        RoutineItem(
            id = "routine_isha_2",
            timeSlotId = "isha_sleep",
            timeSlotTitleBn = "এশা, সুরা মুলক ও ঘুমানোর সুন্নাত",
            titleBn = "সূরা মুলক তিলাওয়াত ও কবরের আজাব থেকে মুক্তি",
            subtitleBn = "৩০ আয়াতের এক অলৌকিক সুপারিশকারী সূরা",
            descriptionBn = "বিছানায় যাওয়ার পূর্বে সূরা মুলক মনোযোগ দিয়ে তিলাওয়াত করা অথবা শোনা।",
            virtuesRewardBn = "কুরআনে ৩০ আয়াতের একটি সূরা রয়েছে, যা কোনো ব্যক্তির জন্য সুপারিশ করতে থাকবে যতক্ষণ না তাকে ক্ষমা করা হয়; তা হলো তাবারাকাল্লাযী বিয়াদিহিল মুলক।",
            reference = "সুনানে আবু দাউদ: ১৪০০, জামে তিরমিযী: ২৮৯১ (হাসান)",
            isTopPriority = true,
            priorityRank = 10
        ),
        RoutineItem(
            id = "routine_isha_3",
            timeSlotId = "isha_sleep",
            timeSlotTitleBn = "এশা, সুরা মুলক ও ঘুমানোর সুন্নাত",
            titleBn = "ঘুমানোর পূর্বে বিছানা ঝাড়া ও ওজু করা",
            subtitleBn = "পবিত্র অবস্থায় ঘুমিয়ে ফেরেশতার দোয়া লাভ",
            descriptionBn = "কাপড়ের কোনা দিয়ে বিছানা ৩ বার ঝেড়ে নেওয়া, ওজু করে ডান কাতে শোয়া, হাতের তালু ডান গালের নিচে রাখা এবং তাসবিহে ফাতেমী পাঠ করা।",
            virtuesRewardBn = "যে ব্যক্তি পবিত্র ওজু অবস্থায় ঘুমায়, তার মাথার কাছে একজন ফেরেশতা নিযুক্ত থাকেন এবং বলেন: 'হে আল্লাহ! আপনার এই বান্দাকে ক্ষমা করে দিন, কেননা সে পবিত্র অবস্থায় ঘুমিয়েছে।'",
            reference = "সহীহ ইবনে হিব্বান: ১০৫১, সহীহ বুখারী: ৬৩২০",
            isTopPriority = false,
            priorityRank = 16
        )
    )
}
