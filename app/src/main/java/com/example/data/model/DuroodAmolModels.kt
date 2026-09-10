package com.example.data.model

data class DuroodAmolItem(
    val id: String,
    val tabCategory: DuroodTabCategory,
    val serialNoBn: String,
    val titleBn: String,
    val arabicText: String,
    val pronunciationBn: String,
    val virtuesBn: String = "",
    val referenceBn: String = "",
    val recommendedCountBn: String = "",
    val meaningBn: String = "",
    val wordByWordMeaningsBn: List<Pair<String, String>> = emptyList(),
    val notesBn: String = ""
)

enum class DuroodTabCategory(val tabTitleBn: String, val badgeBn: String) {
    SPECIAL_ATTRACTION("আকর্ষণ ও ১০০০ আমল", "জুমার বিশেষ আমল"),
    SAHIH_DUROOD("সহীহ দরূদ ভাণ্ডার", "শব্দার্থ ও সহীহ হাদিস"),
    AMOL_1("আমল ০১", "১২টি দরূদ"),
    AMOL_2("আমল ০২", "১০টি দরূদ"),
    AMOL_3("আমল ০৩ ও বরকত", "২০টি বরকতময় দরূদ"),
    BEST_AND_VIRTUES("সর্বোত্তম দরূদ ও হাদিস", "১৪টি বিশেষ হাদিস"),
    TIMINGS("পড়ার মোক্ষম সময়", "১০টি বিশেষ সময়")
}

data class DuroodHadithItem(
    val numberBn: String,
    val narratorBn: String,
    val textBn: String,
    val sourceBn: String
)

data class DuroodAttractionPoint(
    val numberBn: String,
    val titleBn: String,
    val descriptionBn: String,
    val hadithRefBn: String
)
