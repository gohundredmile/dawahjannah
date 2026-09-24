package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quran_surahs")
data class QuranSurahEntity(
    @PrimaryKey val number: Int,
    val nameAr: String,
    val nameBn: String,
    val nameEn: String,
    val meaningBn: String,
    val meaningEn: String,
    val revelationType: String, // "MAKKI" or "MADANI"
    val totalAyat: Int,
    val isAudioDownloaded: Boolean = false,
    val localAudioPath: String? = null
)

@Entity(
    tableName = "quran_ayahs",
    primaryKeys = ["surahNumber", "ayahNumber"]
)
data class QuranAyahEntity(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val pronunciationBn: String,
    val translationBn: String,
    val translationZakaria: String? = null,
    val translationTaisirul: String? = null,
    val tafsirText: String? = null,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "quran_bookmarks")
data class QuranBookmarkEntity(
    @PrimaryKey val id: String, // e.g. "1:1"
    val surahNumber: Int,
    val ayahNumber: Int,
    val surahNameBn: String,
    val arabicText: String,
    val pronunciationBn: String,
    val translationBn: String,
    val timestamp: Long = System.currentTimeMillis()
)
