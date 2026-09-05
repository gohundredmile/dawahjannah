package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BookmarkDao
import com.example.data.local.dao.ChecklistDao
import com.example.data.local.dao.ScratchpadDao
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.ScratchpadNote

@Database(
    entities = [ChecklistRecord::class, BookmarkEntity::class, ScratchpadNote::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checklistDao(): ChecklistDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun scratchpadDao(): ScratchpadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dawah_to_jannah_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
