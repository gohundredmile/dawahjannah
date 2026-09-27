package com.example.data.datasource.hadithtopics

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HadithTopicPreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("hadith_topic_prefs", Context.MODE_PRIVATE)

    private val _bookmarkedHadiths = MutableStateFlow<Set<String>>(loadBookmarkedHadiths())
    val bookmarkedHadiths: StateFlow<Set<String>> = _bookmarkedHadiths.asStateFlow()

    private val _bookmarkedTopics = MutableStateFlow<Set<String>>(loadBookmarkedTopics())
    val bookmarkedTopics: StateFlow<Set<String>> = _bookmarkedTopics.asStateFlow()

    private val _readingHistory = MutableStateFlow<List<String>>(loadReadingHistory())
    val readingHistory: StateFlow<List<String>> = _readingHistory.asStateFlow()

    private fun loadBookmarkedHadiths(): Set<String> {
        return prefs.getStringSet("key_bookmarked_hadiths", emptySet()) ?: emptySet()
    }

    private fun loadBookmarkedTopics(): Set<String> {
        return prefs.getStringSet("key_bookmarked_hadith_topics", emptySet()) ?: emptySet()
    }

    private fun loadReadingHistory(): List<String> {
        val raw = prefs.getString("key_reading_history", "") ?: ""
        if (raw.isBlank()) return emptyList()
        return raw.split(",").filter { it.isNotBlank() }
    }

    fun isTopicBookmarked(topicId: String): Boolean {
        return _bookmarkedTopics.value.contains(topicId)
    }

    fun toggleTopicBookmark(topicId: String): Boolean {
        val current = _bookmarkedTopics.value.toMutableSet()
        val isNowBookmarked = if (current.contains(topicId)) {
            current.remove(topicId)
            false
        } else {
            current.add(topicId)
            true
        }
        prefs.edit().putStringSet("key_bookmarked_hadith_topics", current).apply()
        _bookmarkedTopics.value = current
        return isNowBookmarked
    }

    fun isHadithBookmarked(hadithId: String): Boolean {
        return _bookmarkedHadiths.value.contains(hadithId)
    }

    fun toggleHadithBookmark(hadithId: String): Boolean {
        val current = _bookmarkedHadiths.value.toMutableSet()
        val isNowBookmarked = if (current.contains(hadithId)) {
            current.remove(hadithId)
            false
        } else {
            current.add(hadithId)
            true
        }
        prefs.edit().putStringSet("key_bookmarked_hadiths", current).apply()
        _bookmarkedHadiths.value = current
        return isNowBookmarked
    }

    fun addReadingHistory(hadithId: String) {
        val current = _readingHistory.value.toMutableList()
        current.remove(hadithId)
        current.add(0, hadithId) // Most recent first
        val trimmed = current.take(50) // Keep last 50
        prefs.edit().putString("key_reading_history", trimmed.joinToString(",")).apply()
        _readingHistory.value = trimmed
    }

    fun clearReadingHistory() {
        prefs.edit().remove("key_reading_history").apply()
        _readingHistory.value = emptyList()
    }
}
