package com.example.data.datasource.qurantopics

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranTopicPreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("quran_topic_prefs", Context.MODE_PRIVATE)

    private val _bookmarkedTopics = MutableStateFlow<Set<String>>(loadBookmarkedTopics())
    val bookmarkedTopics: StateFlow<Set<String>> = _bookmarkedTopics.asStateFlow()

    private val _bookmarkedAyahs = MutableStateFlow<Set<String>>(loadBookmarkedAyahs())
    val bookmarkedAyahs: StateFlow<Set<String>> = _bookmarkedAyahs.asStateFlow()

    private fun loadBookmarkedTopics(): Set<String> {
        return prefs.getStringSet("key_bookmarked_topics", emptySet()) ?: emptySet()
    }

    private fun loadBookmarkedAyahs(): Set<String> {
        return prefs.getStringSet("key_bookmarked_ayahs", emptySet()) ?: emptySet()
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
        prefs.edit().putStringSet("key_bookmarked_topics", current).apply()
        _bookmarkedTopics.value = current
        return isNowBookmarked
    }

    fun isAyahBookmarked(surahNumber: Int, ayahNumber: Int): Boolean {
        return _bookmarkedAyahs.value.contains("${surahNumber}_$ayahNumber")
    }

    fun toggleAyahBookmark(surahNumber: Int, ayahNumber: Int): Boolean {
        val key = "${surahNumber}_$ayahNumber"
        val current = _bookmarkedAyahs.value.toMutableSet()
        val isNowBookmarked = if (current.contains(key)) {
            current.remove(key)
            false
        } else {
            current.add(key)
            true
        }
        prefs.edit().putStringSet("key_bookmarked_ayahs", current).apply()
        _bookmarkedAyahs.value = current
        return isNowBookmarked
    }
}
