package com.example.data.repository

import android.content.Context
import com.example.data.datasource.qurantopics.QuranTopicCatalog
import com.example.data.datasource.qurantopics.QuranTopicPreferencesManager
import com.example.data.model.qurantopics.NeedBasedTopicEntry
import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.QuranTopicCategory
import com.example.data.model.qurantopics.TopicAyah
import kotlinx.coroutines.flow.StateFlow

class QuranTopicRepository(context: Context) {

    private val preferencesManager = QuranTopicPreferencesManager(context)

    val bookmarkedTopics: StateFlow<Set<String>> = preferencesManager.bookmarkedTopics
    val bookmarkedAyahs: StateFlow<Set<String>> = preferencesManager.bookmarkedAyahs

    fun getCategories(): List<QuranTopicCategory> = QuranTopicCatalog.categories

    fun getAllTopics(): List<QuranTopic> = QuranTopicCatalog.allTopics

    fun getFeaturedTopics(): List<QuranTopic> = QuranTopicCatalog.getFeaturedTopics()

    fun getDailyTopic(): QuranTopic = QuranTopicCatalog.getDailyTopic()

    fun getTopicById(id: String): QuranTopic? = QuranTopicCatalog.getTopicById(id)

    fun getTopicsByCategory(categoryId: String): List<QuranTopic> = QuranTopicCatalog.getTopicsByCategory(categoryId)

    fun searchTopics(query: String): List<QuranTopic> = QuranTopicCatalog.searchTopics(query)

    fun getNeedBasedEntries(): List<NeedBasedTopicEntry> = QuranTopicCatalog.needBasedEntries

    fun isTopicBookmarked(topicId: String): Boolean = preferencesManager.isTopicBookmarked(topicId)

    fun toggleTopicBookmark(topicId: String): Boolean = preferencesManager.toggleTopicBookmark(topicId)

    fun isAyahBookmarked(surahNumber: Int, ayahNumber: Int): Boolean = preferencesManager.isAyahBookmarked(surahNumber, ayahNumber)

    fun toggleAyahBookmark(surahNumber: Int, ayahNumber: Int): Boolean = preferencesManager.toggleAyahBookmark(surahNumber, ayahNumber)

    fun getBookmarkedTopics(): List<QuranTopic> {
        val bookmarkedIds = preferencesManager.bookmarkedTopics.value
        return QuranTopicCatalog.allTopics.filter { bookmarkedIds.contains(it.id) }
    }

    fun getBookmarkedAyahs(): List<TopicAyah> {
        val keys = preferencesManager.bookmarkedAyahs.value
        val allAyahs = QuranTopicCatalog.allTopics.flatMap { it.ayahs }.distinctBy { "${it.surahNumber}_${it.ayahNumber}" }
        return allAyahs.filter { keys.contains("${it.surahNumber}_${it.ayahNumber}") }
    }
}
