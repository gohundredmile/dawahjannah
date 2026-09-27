package com.example.data.model.hadithtopics

enum class HadithAuthenticityGrade(val titleBn: String, val colorHex: Long) {
    SAHIH("সহীহ", 0xFF059669),
    HASAN("হাসান", 0xFFD97706),
    MUTTAFAAQ_ALAYH("মুত্তাফাক্ব আলাইহ", 0xFF047857)
}

data class TopicHadithRef(
    val hadithId: String,
    val bookSlug: String,
    val bookNameBn: String,
    val hadithNumber: Int,
    val chapterTitleBn: String,
    val narratorBn: String,
    val arabicText: String,
    val banglaText: String,
    val englishText: String = "",
    val gradeBn: String = "সহীহ",
    val gradeColor: String = "SAHIH",
    val sourceBn: String,
    val explanationBn: String = "",
    val relatedQuranSurahNumber: Int? = null,
    val relatedQuranAyahNumber: Int? = null,
    val relatedQuranAyahRef: String? = null,
    val relatedDuaTitleBn: String? = null
) {
    val referenceText: String
        get() = "$bookNameBn: $hadithNumber"

    val formattedShareText: String
        get() = buildString {
            append("« ").append(arabicText).append(" »\n\n")
            append("অনুবাদ: ").append(banglaText).append("\n\n")
            if (englishText.isNotBlank()) {
                append("English: ").append(englishText).append("\n\n")
            }
            append("— বর্ণনাকারী: ").append(narratorBn).append("\n")
            append("— গ্রন্থ: ").append(bookNameBn).append(" (হাদিস নং: ").append(hadithNumber).append(")\n")
            append("— মান: ").append(gradeBn).append("\n\n")
            if (!relatedQuranAyahRef.isNullOrBlank()) {
                append("কুরআনের সংশ্লিষ্ট আয়াত: ").append(relatedQuranAyahRef).append("\n\n")
            }
            append("দা'ওয়াহ টু জান্নাহ — বিষয় ভিত্তিক হাদিস")
        }
}

data class HadithTopicCategory(
    val id: String,
    val nameBn: String,
    val nameEn: String,
    val iconEmoji: String,
    val colorHex: Long,
    val descriptionBn: String,
    val sortOrder: Int
)

data class HadithTopic(
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
    val hadiths: List<TopicHadithRef>
) {
    val totalHadiths: Int
        get() = hadiths.size

    val distinctBooksCount: Int
        get() = hadiths.map { it.bookSlug }.distinct().size

    val distinctBooks: List<Pair<String, String>>
        get() = hadiths.map { it.bookSlug to it.bookNameBn }.distinctBy { it.first }
}

data class NeedBasedHadithEntry(
    val id: String,
    val questionBn: String,
    val subtitleBn: String,
    val targetTopicId: String,
    val iconEmoji: String
)
