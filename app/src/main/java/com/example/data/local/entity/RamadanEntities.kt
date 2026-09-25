package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ramadan_day_logs")
data class RamadanDayLogEntity(
    @PrimaryKey val dayNumber: Int, // 1 to 30
    val isFasted: Boolean = false,
    val isTaraweeh: Boolean = false,
    val taraweehRakahs: Int = 8,
    val quranPagesRead: Int = 0,
    val charityAmount: Double = 0.0,
    val spiritualScore: Int = 5, // 1 to 5 scale
    val guardedTongueAndGaze: Boolean = true,
    val attendedCongregation: Boolean = true,
    val witrCompleted: Boolean = false,
    val tahajjudCompleted: Boolean = false,
    val characterGoalDone: Boolean = false,
    val duaTargetMinutes: Int = 10,
    val duaCompleted: Boolean = false,
    val notes: String = "",
    val reflectionAnswer: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ramadan_checklists")
data class RamadanChecklistEntity(
    @PrimaryKey val itemKey: String,
    val isChecked: Boolean = false,
    val customNotes: String = "",
    val completedAt: Long = 0L
)

@Entity(tableName = "ramadan_missed_fasts")
data class RamadanMissedFastEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reason: String, // অসুস্থতা, সফর, ওজর, অন্যান্য
    val totalMissedDays: Int = 1,
    val recoveredDays: Int = 0,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val isFullyResolved: Boolean = false
)

@Entity(tableName = "ramadan_shawwal_logs")
data class RamadanShawwalLogEntity(
    @PrimaryKey val fastNumber: Int, // 1 to 6
    val isCompleted: Boolean = false,
    val completedDate: String = "",
    val notes: String = ""
)

@Entity(tableName = "ramadan_charity_entries")
data class RamadanCharityEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String = "নফল সদাকাহ", // নফল সদাকাহ, ফরজ যাকাত, যাকাতুল ফিতর, ইফতার করানো, খাদ্য বিতরণ
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "ramadan_personal_duas")
data class RamadanPersonalDuaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val arabicText: String = "",
    val meaningBn: String = "",
    val categoryBn: String = "ব্যক্তিগত",
    val isFavorite: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ramadan_reflections")
data class RamadanReflectionEntity(
    @PrimaryKey val dayNumber: Int, // 1 to 30
    val promptQuestion: String = "",
    val userAnswer: String = "",
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ramadan_settings")
data class RamadanSettingsEntity(
    @PrimaryKey val id: Int = 1,
    val userLifestyleModeId: String = "professional", // student, professional, parent, beginner, advanced
    val quranTargetKhatms: Int = 1,
    val quranTargetPagesPerDay: Int = 20,
    val quranPaceStrategy: String = "GRADUAL", // GRADUAL, RESET, KEEP
    val taraweehGoalPreference: String = "REGULAR", // REGULAR, ALL, HOME
    val charityTargetBudget: Double = 5000.0,
    val charityFrequency: String = "WEEKLY", // DAILY, WEEKLY
    val notificationIntensity: String = "BALANCED", // MINIMAL, BALANCED, FULL
    val characterFocus: String = "সবর ও ক্রোধ নিয়ন্ত্রণ",
    val currentSelectedRamadanDay: Int = 1,
    val shaBanFastingCount: Int = 0,
    val habitPrayerOnTime: Boolean = true,
    val habitDailyQuran: Boolean = true,
    val habitTahajjudWitr: Boolean = true,
    val habitAyyamBeed: Boolean = true,
    val habitWeeklyCharity: Boolean = true
)
