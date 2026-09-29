package com.example.data.datasource.qurantopics

import com.example.data.model.qurantopics.NeedBasedTopicEntry
import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.QuranTopicCategory
import java.util.Calendar

object QuranTopicCatalog {

    val categories: List<QuranTopicCategory>
        get() = QuranTopicCategories.categories

    val needBasedEntries: List<NeedBasedTopicEntry>
        get() = QuranTopicCategories.needBasedEntries

    val allTopics: List<QuranTopic> by lazy {
        QuranTopicsPart1.topics +
        QuranTopicsPart2.topics +
        QuranTopicsPart3.topics +
        QuranTopicsPart4.topics +
        QuranTopicsPart5.topics +
        QuranTopicsPart6.topics +
        QuranTopicsPart7.topics
    }

    val totalTopicCount: Int
        get() = allTopics.size

    val totalAyahCount: Int
        get() = allTopics.sumOf { it.totalAyahs }

    fun getTopicById(id: String): QuranTopic? {
        return allTopics.find { it.id.equals(id, ignoreCase = true) }
    }

    fun getTopicsByCategory(categoryId: String): List<QuranTopic> {
        return allTopics.filter { it.categoryId == categoryId }
    }

    fun getFeaturedTopics(): List<QuranTopic> {
        return allTopics.filter { it.isFeatured }
    }

    fun getDailyTopic(): QuranTopic {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = (dayOfYear % allTopics.size).coerceIn(0, allTopics.size - 1)
        return allTopics[index]
    }

    /**
     * Powerful multi-tier search across Bangla, English, Arabic, and synonyms.
     */
    fun searchTopics(query: String): List<QuranTopic> {
        val trimmed = query.trim().lowercase()
        if (trimmed.isBlank()) return emptyList()

        // Normalize Bangla synonyms (e.g. মা-বাবা, পিতা-মাতা, রিযিক, রিজিক)
        val normalized = trimmed
            .replace("-", " ")
            .replace("রিযিক", "রিজিক")
            .replace("নামায", "নামাজ")
            .replace("সালাহ", "সালাত")
            .replace("পিতামাতা", "পিতা মাতা")
            .replace("বাবা মা", "পিতা মাতা")
            .replace("মা বাবা", "পিতা মাতা")

        return allTopics.mapNotNull { topic ->
            var score = 0

            // 1. Exact match in Bangla or English
            if (topic.nameBn.lowercase() == trimmed || topic.nameEn.lowercase() == trimmed) {
                score += 100
            }
            // 2. Starts with query
            if (topic.nameBn.lowercase().startsWith(trimmed) || topic.nameEn.lowercase().startsWith(trimmed)) {
                score += 60
            }
            // 3. Contains in title
            if (topic.nameBn.lowercase().contains(trimmed) || topic.nameEn.lowercase().contains(trimmed)) {
                score += 40
            }
            // 4. Keyword matches
            val keywordMatch = topic.searchKeywordsBn.any { kw ->
                kw.lowercase().contains(trimmed) || kw.lowercase().contains(normalized)
            } || topic.searchKeywordsEn.any { kw ->
                kw.lowercase().contains(trimmed)
            }
            if (keywordMatch) {
                score += 30
            }
            // 5. Description matches
            if (topic.descriptionBn.lowercase().contains(trimmed)) {
                score += 15
            }
            // 6. Ayahs text matches
            val ayahMatch = topic.ayahs.any { 
                it.translationBn.lowercase().contains(trimmed) ||
                it.surahNameBn.lowercase().contains(trimmed)
            }
            if (ayahMatch) {
                score += 10
            }

            if (score > 0) Pair(topic, score) else null
        }
        .sortedByDescending { it.second }
        .map { it.first }
    }
}
