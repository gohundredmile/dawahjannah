package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_records")
data class ChecklistRecord(
    @PrimaryKey val date: String, // Format: yyyy-MM-dd
    val checkedHabitIds: String = "", // Comma-separated list of checked habit IDs
    val totalHabits: Int = 12,
    val completedCount: Int = 0,
    val scorePercentage: Int = 0
)

@Entity(tableName = "dua_bookmarks")
data class BookmarkEntity(
    @PrimaryKey val duaId: String,
    val bookmarkedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "scratchpad_notes")
data class ScratchpadNote(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val category: String = "দোয়া", // দোয়া, ইস্তিগফার, লক্ষ্য, শুকরিয়া
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
