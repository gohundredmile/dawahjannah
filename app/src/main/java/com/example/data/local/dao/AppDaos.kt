package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.ScratchpadNote
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {
    @Query("SELECT * FROM checklist_records WHERE date = :date LIMIT 1")
    fun getRecordForDate(date: String): Flow<ChecklistRecord?>

    @Query("SELECT * FROM checklist_records WHERE date = :date LIMIT 1")
    suspend fun getRecordForDateDirect(date: String): ChecklistRecord?

    @Query("SELECT * FROM checklist_records ORDER BY date DESC")
    fun getAllRecords(): Flow<List<ChecklistRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(record: ChecklistRecord)
}

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM app_bookmarks ORDER BY bookmarkedAt DESC")
    fun getAllBookmarks(): Flow<List<BookmarkEntity>>

    @Query("SELECT id FROM app_bookmarks")
    fun getAllBookmarkedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM app_bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM app_bookmarks WHERE id = :id")
    suspend fun removeBookmark(id: String)
}

@Dao
interface ScratchpadDao {
    @Query("SELECT * FROM scratchpad_notes ORDER BY updatedAt DESC")
    fun getAllNotes(): Flow<List<ScratchpadNote>>

    @Query("SELECT * FROM scratchpad_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Int): ScratchpadNote?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: ScratchpadNote): Long

    @Update
    suspend fun updateNote(note: ScratchpadNote)

    @Query("DELETE FROM scratchpad_notes WHERE id = :id")
    suspend fun deleteNoteById(id: Int)
}

@Dao
interface SunnahHabitDao {
    @Query("SELECT * FROM sunnah_habit_logs WHERE date = :date")
    fun getLogsForDate(date: String): Flow<List<com.example.data.local.entity.SunnahHabitLog>>

    @Query("SELECT * FROM sunnah_habit_logs WHERE date BETWEEN :startDate AND :endDate")
    fun getLogsBetweenDates(startDate: String, endDate: String): Flow<List<com.example.data.local.entity.SunnahHabitLog>>

    @Query("SELECT * FROM sunnah_habit_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<com.example.data.local.entity.SunnahHabitLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(log: com.example.data.local.entity.SunnahHabitLog)

    @Query("DELETE FROM sunnah_habit_logs WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM sunnah_habit_logs WHERE habitId = :habitId AND date = :date")
    suspend fun deleteByHabitAndDate(habitId: String, date: String)
}

