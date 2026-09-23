package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.RamadanCharityEntryEntity
import com.example.data.local.entity.RamadanChecklistEntity
import com.example.data.local.entity.RamadanDayLogEntity
import com.example.data.local.entity.RamadanMissedFastEntity
import com.example.data.local.entity.RamadanSettingsEntity
import com.example.data.local.entity.RamadanShawwalLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RamadanDao {

    // --- DAY LOGS ---
    @Query("SELECT * FROM ramadan_day_logs ORDER BY dayNumber ASC")
    fun getAllDayLogs(): Flow<List<RamadanDayLogEntity>>

    @Query("SELECT * FROM ramadan_day_logs WHERE dayNumber = :dayNumber LIMIT 1")
    suspend fun getDayLog(dayNumber: Int): RamadanDayLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateDayLog(log: RamadanDayLogEntity)

    @Query("SELECT COUNT(*) FROM ramadan_day_logs WHERE isFasted = 1")
    fun getFastedDaysCount(): Flow<Int>

    @Query("SELECT SUM(quranPagesRead) FROM ramadan_day_logs")
    fun getTotalQuranPagesRead(): Flow<Int?>

    @Query("SELECT SUM(charityAmount) FROM ramadan_day_logs")
    fun getTotalCharityGiven(): Flow<Double?>

    // --- CHECKLIST ---
    @Query("SELECT * FROM ramadan_checklists")
    fun getAllChecklistStatuses(): Flow<List<RamadanChecklistEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateChecklist(item: RamadanChecklistEntity)

    // --- MISSED FASTS ---
    @Query("SELECT * FROM ramadan_missed_fasts ORDER BY id DESC")
    fun getAllMissedFasts(): Flow<List<RamadanMissedFastEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMissedFast(item: RamadanMissedFastEntity): Long

    @Update
    suspend fun updateMissedFast(item: RamadanMissedFastEntity)

    @Query("DELETE FROM ramadan_missed_fasts WHERE id = :id")
    suspend fun deleteMissedFast(id: Int)

    // --- SHAWWAL LOGS ---
    @Query("SELECT * FROM ramadan_shawwal_logs ORDER BY fastNumber ASC")
    fun getAllShawwalLogs(): Flow<List<RamadanShawwalLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateShawwalLog(item: RamadanShawwalLogEntity)

    // --- CHARITY ENTRIES ---
    @Query("SELECT * FROM ramadan_charity_entries ORDER BY timestamp DESC")
    fun getAllCharityEntries(): Flow<List<RamadanCharityEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharityEntry(item: RamadanCharityEntryEntity): Long

    @Query("DELETE FROM ramadan_charity_entries WHERE id = :id")
    suspend fun deleteCharityEntry(id: Int)

    // --- SETTINGS ---
    @Query("SELECT * FROM ramadan_settings WHERE id = 1 LIMIT 1")
    fun getSettings(): Flow<RamadanSettingsEntity?>

    @Query("SELECT * FROM ramadan_settings WHERE id = 1 LIMIT 1")
    suspend fun getSettingsDirect(): RamadanSettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSettings(settings: RamadanSettingsEntity)
}
