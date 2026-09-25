package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BookmarkDao
import com.example.data.local.dao.ChecklistDao
import com.example.data.local.dao.HadithDao
import com.example.data.local.dao.QuranDao
import com.example.data.local.dao.RamadanDao
import com.example.data.local.dao.ScratchpadDao
import com.example.data.local.dao.SunnahHabitDao
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.HadithBookmarkEntity
import com.example.data.local.entity.HadithBookEntity
import com.example.data.local.entity.HadithChapterEntity
import com.example.data.local.entity.HadithEntity
import com.example.data.local.entity.QuranAyahEntity
import com.example.data.local.entity.QuranBookmarkEntity
import com.example.data.local.entity.QuranSurahEntity
import com.example.data.local.entity.RamadanCharityEntryEntity
import com.example.data.local.entity.RamadanChecklistEntity
import com.example.data.local.entity.RamadanDayLogEntity
import com.example.data.local.entity.RamadanMissedFastEntity
import com.example.data.local.entity.RamadanPersonalDuaEntity
import com.example.data.local.entity.RamadanReflectionEntity
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
        RamadanPersonalDuaEntity::class,
        RamadanReflectionEntity::class,
        RamadanSettingsEntity::class,
        QuranSurahEntity::class,
        QuranAyahEntity::class,
        QuranBookmarkEntity::class,
        HadithBookEntity::class,
        HadithChapterEntity::class,
        HadithEntity::class,
        HadithBookmarkEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun checklistDao(): ChecklistDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun scratchpadDao(): ScratchpadDao
    abstract fun sunnahHabitDao(): SunnahHabitDao
    abstract fun ramadanDao(): RamadanDao
    abstract fun quranDao(): QuranDao
    abstract fun hadithDao(): HadithDao

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
