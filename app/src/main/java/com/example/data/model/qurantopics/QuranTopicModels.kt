package com.example.data.model.qurantopics

enum class TopicRelevance(val titleBn: String) {
    DIRECT("সরাসরি সম্পর্কিত"),
    SUPPORTING("সহায়ক আয়াত"),
    CONTEXTUAL("প্রাসঙ্গিক আয়াত")
}

data class TopicAyah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBn: String,
    val surahNameEn: String,
    val surahNameAr: String,
    val totalAyahsInSurah: Int,
    val revelationTypeBn: String, // "মাক্কী" or "মাদানী"
    val arabicText: String,
    val translationBn: String,
    val translationEn: String,
    val transliterationBn: String? = null,
    val juzNumber: Int? = null,
    val pageNumber: Int? = null,
    val relevance: TopicRelevance = TopicRelevance.DIRECT,
    val contextNoteBn: String? = null
) {
    val referenceText: String
        get() = "$surahNameBn ($surahNumber:$ayahNumber)"

    val formattedShareText: String
        get() = buildString {
            append("﴿ ").append(arabicText).append(" ﴾\n\n")
            append("অনুবাদ: ").append(translationBn).append("\n\n")
            if (translationEn.isNotBlank()) {
                append("English: ").append(translationEn).append("\n\n")
            }
            append("— ").append(surahNameBn).append(" [").append(surahNumber).append(":").append(ayahNumber).append("]")
            append("\n\nদা'ওয়াহ টু জান্নাহ — বিষয় ভিত্তিক কোরআনের আয়াত")
        }
}

data class QuranTopicCategory(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val iconEmoji: String,
    val colorHex: Long,
    val descriptionBn: String,
    val sortOrder: Int
)

data class QuranTopic(
    val id: String,
    val categoryId: String,
    val nameBn: String,
    val nameEn: String,
    val nameAr: String? = null,
    val descriptionBn: String,
    val searchKeywordsBn: List<String>,
    val searchKeywordsEn: List<String>,
    val isFeatured: Boolean = false,
    val iconEmoji: String? = null,
    val relatedTopicIds: List<String> = emptyList(),
    val ayahs: List<TopicAyah>
) {
    val totalAyahs: Int
        get() = ayahs.size

    val distinctSurahCount: Int
        get() = ayahs.map { it.surahNumber }.distinct().size

    val distinctSurahs: List<Pair<Int, String>>
        get() = ayahs.map { it.surahNumber to it.surahNameBn }.distinctBy { it.first }
}

data class NeedBasedTopicEntry(
    val id: String,
    val emotionOrNeedBn: String,
    val targetTopicId: String,
    val subtitleBn: String,
    val iconEmoji: String
)
