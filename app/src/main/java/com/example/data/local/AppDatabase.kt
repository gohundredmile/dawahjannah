package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BookmarkDao
import com.example.data.local.dao.ChecklistDao
import com.example.data.local.dao.RamadanDao
import com.example.data.local.dao.ScratchpadDao
import com.example.data.local.dao.SunnahHabitDao
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.RamadanCharityEntryEntity
import com.example.data.local.entity.RamadanChecklistEntity
import com.example.data.local.entity.RamadanDayLogEntity
import com.example.data.local.entity.RamadanMissedFastEntity
import com.example.data.local.entity.RamadanSettingsEntity
import com.example.data.local.entity.RamadanShawwalLogEntity
import com.example.data.local.entity.ScratchpadNote
import com.example.data.local.entity.SunnahHabitLog

@Database(
    entities = [
        ChecklistRecord::class,
        BookmarkEntity::class,
        ScratchpadNote::class,
        SunnahHabitLog::class,
        RamadanDayLogEntity::class,
        RamadanChecklistEntity::class,
        RamadanMissedFastEntity::class,
        RamadanShawwalLogEntity::class,
        RamadanCharityEntryEntity::class,
        RamadanSettingsEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checklistDao(): ChecklistDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun scratchpadDao(): ScratchpadDao
    abstract fun sunnahHabitDao(): SunnahHabitDao
    abstract fun ramadanDao(): RamadanDao

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
