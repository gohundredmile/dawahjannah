package com.example.data.model

/**
 * 12 Spheres of Life for the Life Application Engine.
 */
enum class LifeSphere(
    val id: String,
    val titleBn: String,
    val emoji: String,
    val subtitleBn: String,
    val colorHex: Long
) {
    WORSHIP("worship", "ইবাদত", "🕌", "সালাত, দোয়া ও আল্লাহর নিবিড় স্মরণ", 0xFF047857),
    CHARACTER("character", "আখলাক ও চরিত্র", "❤️", "সত্যবাদিতা, বিনয়, আমানতদারী ও তাকওয়া", 0xFFBE123C),
    FAMILY("family", "পরিবার ও দাম্পত্য", "👨‍👩‍👧", "পিতা-মাতা, স্ত্রী-সন্তান ও আত্মীয়তার হক", 0xFFB45309),
    RELATIONSHIPS("relationships", "সম্পর্ক ও বন্ধুত্ব", "🤝", "সৌহার্দ্য, পারস্পরিক আস্থা ও ভ্রাতৃত্ববোধ", 0xFF0284C7),
    WORK("work", "কর্মক্ষেত্র ও পেশা", "💼", "সততা, কর্মে নিষ্ঠা ও ইনসাফভিত্তিক আচরণ", 0xFF4338CA),
    MONEY("money", "অর্থ ও লেনদেন", "💰", "হালাল উপার্জন, অপচয় পরিহার ও সাদাকাহ", 0xFF15803D),
    MINDSET("mindset", "মানসিকতা ও চিন্তা", "🧠", "সবর, ইতিবাচক মনোভাব ও তাওয়াক্কুল", 0xFF7C3AED),
    SPEECH("speech", "কথা ও ভাষা", "🗣️", "মার্জিত ভাষা, সত্যবাদিতা ও গীবত বর্জন", 0xFF0D9488),
    ANGER("anger", "রাগ ও আবেগ নিয়ন্ত্রণ", "😡", "ক্রোধ সংবরণ, সহনশীলতা ও ক্ষমাশীলতা", 0xFFC026D3),
    DIFFICULT_SITUATIONS("difficult_situations", "কঠিন পরিস্থিতি ও বিপদ", "❤️‍🩹", "বিপদে অবিচলতা, আশা ও আল্লাহর রহমত", 0xFFDC2626),
    DIGITAL_LIFE("digital_life", "ডিজিটাল জীবন ও মিডিয়া", "📱", "সময়ের সুরক্ষা, দৃষ্টি সংযত রাখা ও বিভ্রান্তিমুক্তি", 0xFF2563EB),
    PERSONAL_GROWTH("personal_growth", "আত্মোন্নয়ন ও পরিশুদ্ধি", "🌱", "নফস নিয়ন্ত্রণ, নিয়তের পরিশুদ্ধি ও অগ্রগতি", 0xFF059669);

    companion object {
        fun fromId(id: String): LifeSphere = entries.find { it.id == id } ?: MINDSET
    }
}

/**
 * Multiple Action Modes to apply an ayah in varied learning styles.
 */
enum class ActionMode(
    val id: String,
    val titleBn: String,
    val emoji: String,
    val descriptionBn: String
) {
    LEARN("learn", "শিখুন", "📖", "আয়াতের গভীর তত্ত্ব, বিধান ও শিক্ষা উপলব্ধি"),
    REFLECT("reflect", "তাদাব্বুর", "❤️", "নিজের জীবনের সাথে বার্তার আত্মিক সম্পর্ক অন্বেষণ"),
    PRACTICE("practice", "আমল", "🎯", "আজকের দিনে একটি বাস্তবসম্মত ছোট অনুশীলন"),
    AVOID("avoid", "বর্জন ও সতর্কতা", "🛡️", "সতর্কবার্তা চিহ্নিত করে সচেতনতা তৈরি"),
    SHARE("share", "শেয়ার ও দা'ওয়াহ", "📤", "কল্যাণকর বার্তা অন্যের কাছে পৌঁছে দেওয়া"),
    MEMORIZE("memorize", "হিফজ", "🧠", "আয়াতটি মুখস্থ ও অন্তরে স্থায়ী ধারণ"),
    TEACH("teach", "শিক্ষা দিন", "🗣️", "নিজের ভাষায় পরিবার বা বন্ধুকে বুঝিয়ে বলা");

    companion object {
        fun fromId(id: String): ActionMode = entries.find { it.id == id } ?: PRACTICE
    }
}

/**
 * Plan duration options.
 */
enum class ActionPlanDuration(
    val id: String,
    val titleBn: String,
    val daysCount: Int,
    val subtitleBn: String
) {
    TODAY("today", "আজকের আমল", 1, "১ দিনের বাস্তব পদক্ষেপ"),
    THREE_DAYS("three_days", "৩ দিনের অনুশীলন", 3, "৩ দিনের ধারাবাহিক সাধনা"),
    SEVEN_DAYS("seven_days", "৭ দিনের চ্যালেঞ্জ", 7, "৭ দিনের আত্মশুদ্ধি রূপান্তর"),
    WEEKLY("weekly", "সাপ্তাহিক তাদাব্বুর", 7, "সপ্তাহব্যাপী গভীর মনোযোগ"),
    HABIT("habit", "স্থায়ী সুন্নাহ অভ্যাস", 30, "দৈনন্দিন জীবনে স্থায়ী অভ্যাসে রূপান্তর");

    companion object {
        fun fromId(id: String): ActionPlanDuration = entries.find { it.id == id } ?: TODAY
    }
}

/**
 * Classical tafsir source item.
 */
data class TafsirSourceItem(
    val sourceNameBn: String,
    val scholarOrBook: String,
    val summaryBn: String
)

/**
 * Key Arabic vocabulary term and its spiritual significance.
 */
data class KeyArabicTerm(
    val arabicWord: String,
    val rootWord: String,
    val banglaMeaning: String,
    val spiritualDepthBn: String
)

/**
 * Related Quranic verse for deep dive.
 */
data class ActionRelatedVerse(
    val surahNameBn: String,
    val referenceNumber: String,
    val arabicText: String,
    val translationBn: String
)

/**
 * Related authentic Hadith.
 */
data class ActionRelatedHadith(
    val bookSource: String,
    val narrator: String,
    val hadithTextBn: String,
    val gradeBn: String = "সহীহ"
)

/**
 * Core model containing complete AI + scholarly insight for an Ayah.
 */
data class AyahActionInsight(
    val ayahId: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameArabic: String,
    val surahNameBangla: String,
    val surahNameEnglish: String,
    val revelationTypeBn: String,
    val arabicText: String,
    val transliterationBn: String,
    val banglaTranslation: String,
    val englishTranslation: String,

    // The 4 Core Questions
    val whatDoesItTeachBn: String,       // Central message in simple language
    val whatToNoticeBn: String,          // Values, attitudes, responsibilities, principles
    val whatToBeCarefulAboutBn: String,  // Warnings, behaviors or mistakes warned against
    val whatCanIPracticeBn: String,      // Realistic practical applications inspired by the ayah

    // Three-tier truth distinction
    val quranSaysBn: String,                     // Direct textual command/statement
    val scholarlyInterpretationBn: String,       // Classical/contemporary scholarly tafsir consensus
    val possiblePersonalApplicationBn: String,   // Voluntary AI/personal mindful practice suggestion

    // Deep dive & scholarship
    val tafsirSources: List<TafsirSourceItem> = emptyList(),
    val keyArabicTerms: List<KeyArabicTerm> = emptyList(),
    val immediateContextBn: String = "",
    val relatedVerses: List<ActionRelatedVerse> = emptyList(),
    val relatedHadiths: List<ActionRelatedHadith> = emptyList(),

    // Personal Reflection Lens (conversational prompts)
    val reflectiveQuestions: List<String> = emptyList(),

    // Life Application Engine (12 spheres mapped)
    val applicationsBySphere: Map<LifeSphere, List<String>> = emptyMap(),

    // The Signature "Today's Action"
    val defaultTodayAction: String = "",
    val alternativeTodayActions: List<String> = emptyList(),

    // Mode-specific notes
    val warningAvoidanceGoal: String = "",
    val memorizationTip: String = "",
    val teachingGuideBn: String = "",

    // Spiritual Themes for non-numerical qualitative journey tracking
    val primaryThemes: List<String> = emptyList(),
    val audioUrl: String = ""
)

/**
 * User's active or past action commitment.
 */
data class ActionHistoryRecord(
    val id: String,
    val createdAt: Long,
    val ayahId: String,
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBangla: String,
    val arabicShort: String,
    val translationShort: String,
    val selectedSphere: LifeSphere,
    val selectedMode: ActionMode,
    val planDuration: ActionPlanDuration,
    val actionText: String,
    val userPersonalNote: String = "",
    val userReflectionAnswers: Map<String, String> = emptyMap(),
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val eveningReview: EveningReviewRecord? = null,
    val themes: List<String> = emptyList()
)

/**
 * Lightweight Evening Review entry.
 */
enum class ReviewOutcome(val titleBn: String, val emoji: String) {
    DID_IT("আলহামদুলিল্লাহ, করেছি", "✅"),
    PARTIALLY("আংশিক করতে পেরেছি", "◐"),
    NOT_YET("আজ সুযোগ হয়নি", "⏳")
}

data class EveningReviewRecord(
    val reviewedAt: Long,
    val outcome: ReviewOutcome,
    val whatDidYouNotice: String = "",
    val didItChangeHandling: String = "",
    val growthTakeaway: String = ""
)

/**
 * Ask the Ayah response model.
 */
data class AskAyahAnswer(
    val question: String,
    val answerBn: String,
    val classificationBn: String = "উপদেশ ও পথনির্দেশ", // আদেশ, উপদেশ, ঘটনা, সতর্কবার্তা, বিবরণ
    val scholarlyBasisBn: String = "",
    val personalApplicationTip: String = ""
)
