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
    @Query("SELECT duaId FROM dua_bookmarks")
    fun getAllBookmarkedIds(): Flow<List<String>>

    @Query("SELECT EXISTS(SELECT 1 FROM dua_bookmarks WHERE duaId = :duaId)")
    fun isBookmarked(duaId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("DELETE FROM dua_bookmarks WHERE duaId = :duaId")
    suspend fun removeBookmark(duaId: String)
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
