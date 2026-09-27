package com.example.data.datasource.hadithtopics

import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.HadithTopicCategory
import com.example.data.model.hadithtopics.NeedBasedHadithEntry
import com.example.data.model.hadithtopics.TopicHadithRef
import java.util.Calendar

object HadithTopicCatalog {

    val categories: List<HadithTopicCategory>
        get() = HadithTopicCategories.categories

    val needBasedEntries: List<NeedBasedHadithEntry>
        get() = HadithTopicCategories.needBasedEntries

    val allTopics: List<HadithTopic> by lazy {
        HadithTopicDataPart1.topics + HadithTopicDataPart2.topics
    }

    val totalTopicCount: Int
        get() = allTopics.size

    val allHadiths: List<TopicHadithRef> by lazy {
        allTopics.flatMap { it.hadiths }.distinctBy { it.hadithId }
    }

    val totalHadithCount: Int
        get() = allHadiths.size

    fun getTopicById(id: String): HadithTopic? {
        return allTopics.find { it.id.equals(id, ignoreCase = true) }
    }

    fun getTopicsByCategory(categoryId: String): List<HadithTopic> {
        return allTopics.filter { it.categoryId == categoryId }
    }

    fun getFeaturedTopics(): List<HadithTopic> {
        return allTopics.filter { it.isFeatured }
    }

    fun getDailyHadith(): TopicHadithRef {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear % allHadiths.size).coerceIn(0, allHadiths.size - 1)
        return allHadiths[index]
    }

    /**
     * Search across Hadith topics, Hadith texts, translations, narrators, collections, and keywords.
     */
    fun searchTopicsAndHadiths(query: String): Pair<List<HadithTopic>, List<TopicHadithRef>> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isBlank()) return Pair(emptyList(), emptyList())

        val matchedTopics = allTopics.mapNotNull { topic ->
            var score = 0
            if (topic.nameBn.lowercase() == trimmed || topic.nameEn.lowercase() == trimmed) score += 100
            if (topic.nameBn.lowercase().contains(trimmed) || topic.nameEn.lowercase().contains(trimmed)) score += 50
            if (topic.searchKeywordsBn.any { it.lowercase().contains(trimmed) } ||
                topic.searchKeywordsEn.any { it.lowercase().contains(trimmed) }) score += 40
            if (topic.descriptionBn.lowercase().contains(trimmed)) score += 20
            if (score > 0) Pair(topic, score) else null
        }.sortedByDescending { it.second }.map { it.first }

        val matchedHadiths = allHadiths.filter { hadith ->
            hadith.banglaText.lowercase().contains(trimmed) ||
            hadith.arabicText.contains(trimmed) ||
            hadith.narratorBn.lowercase().contains(trimmed) ||
            hadith.bookNameBn.lowercase().contains(trimmed) ||
            hadith.hadithNumber.toString() == trimmed ||
            hadith.gradeBn.lowercase().contains(trimmed)
        }

        return Pair(matchedTopics, matchedHadiths)
    }
}
