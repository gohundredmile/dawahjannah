package com.example.data.repository

import android.content.Context
import com.example.data.datasource.hadithtopics.HadithTopicCatalog
import com.example.data.datasource.hadithtopics.HadithTopicPreferencesManager
import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.HadithTopicCategory
import com.example.data.model.hadithtopics.NeedBasedHadithEntry
import com.example.data.model.hadithtopics.TopicHadithRef
import kotlinx.coroutines.flow.StateFlow

class HadithTopicRepository(context: Context) {

    private val preferencesManager = HadithTopicPreferencesManager(context)

    val bookmarkedTopics: StateFlow<Set<String>> = preferencesManager.bookmarkedTopics
    val bookmarkedHadiths: StateFlow<Set<String>> = preferencesManager.bookmarkedHadiths

    fun getCategories(): List<HadithTopicCategory> = HadithTopicCatalog.categories

    fun getAllTopics(): List<HadithTopic> = HadithTopicCatalog.allTopics

    fun getFeaturedTopics(): List<HadithTopic> = HadithTopicCatalog.getFeaturedTopics()

    fun getDailyHadith(): TopicHadithRef = HadithTopicCatalog.getDailyHadith()

    fun getTopicById(id: String): HadithTopic? = HadithTopicCatalog.getTopicById(id)

    fun getTopicsByCategory(categoryId: String): List<HadithTopic> = HadithTopicCatalog.getTopicsByCategory(categoryId)

    fun searchTopics(query: String): Pair<List<HadithTopic>, List<TopicHadithRef>> = HadithTopicCatalog.searchTopicsAndHadiths(query)

    fun getNeedBasedEntries(): List<NeedBasedHadithEntry> = HadithTopicCatalog.needBasedEntries

    fun isTopicBookmarked(topicId: String): Boolean = preferencesManager.isTopicBookmarked(topicId)

    fun toggleTopicBookmark(topicId: String): Boolean = preferencesManager.toggleTopicBookmark(topicId)

    fun isHadithBookmarked(hadithId: String): Boolean = preferencesManager.isHadithBookmarked(hadithId)

    fun toggleHadithBookmark(hadithId: String): Boolean = preferencesManager.toggleHadithBookmark(hadithId)

    fun getBookmarkedTopics(): List<HadithTopic> {
        val bookmarkedIds = preferencesManager.bookmarkedTopics.value
        return HadithTopicCatalog.allTopics.filter { bookmarkedIds.contains(it.id) }
    }

    fun getBookmarkedHadiths(): List<TopicHadithRef> {
        val keys = preferencesManager.bookmarkedHadiths.value
        return HadithTopicCatalog.allHadiths.filter { keys.contains(it.hadithId) }
    }
}
