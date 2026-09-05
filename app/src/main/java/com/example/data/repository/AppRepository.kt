package com.example.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.local.AppDatabase
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.ScratchpadNote
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.EnglishFont
import com.example.data.model.FontSizeScale
import com.example.data.model.PrimaryFontPreference
import com.example.data.model.SalatConfiguration
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private val Context.dataStore by preferencesDataStore(name = "dawah_settings")

class AppRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val checklistDao = db.checklistDao()
    private val bookmarkDao = db.bookmarkDao()
    private val scratchpadDao = db.scratchpadDao()

    companion object {
        val KEY_THEME_STYLE = stringPreferencesKey("theme_style")
        val KEY_THEME_MODE = stringPreferencesKey("theme_mode")
        val KEY_ENGLISH_FONT = stringPreferencesKey("english_font")
        val KEY_BANGLA_FONT = stringPreferencesKey("bangla_font")
        val KEY_PRIMARY_FONT_PREF = stringPreferencesKey("primary_font_pref")
        val KEY_BANGLA_WEIGHT = intPreferencesKey("bangla_font_weight")
        val KEY_FONT_SCALE = floatPreferencesKey("font_scale")
        val KEY_HANAFI_ASR = booleanPreferencesKey("is_hanafi_asr")
        val KEY_LAST_UPDATE_NOTIFICATION = stringPreferencesKey("last_update_notification")
        val KEY_TASBIH_TOTAL_COUNT = stringPreferencesKey("tasbih_total_count")
        val KEY_SALAT_PLACE_BN = stringPreferencesKey("salat_place_bn")
        val KEY_SALAT_PLACE_EN = stringPreferencesKey("salat_place_en")
        val KEY_SALAT_LAT = floatPreferencesKey("salat_lat")
        val KEY_SALAT_LNG = floatPreferencesKey("salat_lng")
        val KEY_SALAT_IS_GPS = booleanPreferencesKey("salat_is_gps")
        val KEY_SALAT_OFFSET_MINS = intPreferencesKey("salat_offset_mins")
    }

    fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        return sdf.format(Date())
    }

    // CHECKLIST
    fun getTodayChecklistRecord(): Flow<ChecklistRecord?> {
        return checklistDao.getRecordForDate(getTodayDateString())
    }

    fun getAllChecklistRecords(): Flow<List<ChecklistRecord>> {
        return checklistDao.getAllRecords()
    }

    suspend fun toggleHabit(habitId: String, totalHabits: Int) {
        val today = getTodayDateString()
        val currentRecord = checklistDao.getRecordForDateDirect(today)
        val currentChecked = currentRecord?.checkedHabitIds?.split(",")?.filter { it.isNotBlank() }?.toMutableSet()
            ?: mutableSetOf()

        if (currentChecked.contains(habitId)) {
            currentChecked.remove(habitId)
        } else {
            currentChecked.add(habitId)
        }

        val completed = currentChecked.size
        val score = if (totalHabits > 0) ((completed.toFloat() / totalHabits) * 100).toInt() else 0
        val updatedRecord = ChecklistRecord(
            date = today,
            checkedHabitIds = currentChecked.joinToString(","),
            totalHabits = totalHabits,
            completedCount = completed,
            scorePercentage = score
        )
        checklistDao.insertOrUpdate(updatedRecord)
    }

    fun calculateStreak(records: List<ChecklistRecord>): Int {
        if (records.isEmpty()) return 0
        val recordMap = records.associateBy { it.date }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val cal = Calendar.getInstance()

        var streak = 0
        // Check if today has at least 1 completed habit
        val todayStr = sdf.format(cal.time)
        val todayRecord = recordMap[todayStr]
        if (todayRecord != null && todayRecord.completedCount > 0) {
            streak++
        }
        // Count backwards
        cal.add(Calendar.DAY_OF_YEAR, -1)
        while (true) {
            val dateStr = sdf.format(cal.time)
            val record = recordMap[dateStr]
            if (record != null && record.completedCount >= 3) { // At least 3 habits to count streak
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }

    // BOOKMARKS
    fun getBookmarkedDuaIds(): Flow<List<String>> = bookmarkDao.getAllBookmarkedIds()

    suspend fun toggleBookmark(duaId: String, isBookmarked: Boolean) {
        if (isBookmarked) {
            bookmarkDao.removeBookmark(duaId)
        } else {
            bookmarkDao.addBookmark(BookmarkEntity(duaId = duaId))
        }
    }

    // SCRATCHPAD
    fun getAllNotes(): Flow<List<ScratchpadNote>> = scratchpadDao.getAllNotes()

    suspend fun saveNote(id: Int, title: String, content: String, category: String) {
        if (id == 0) {
            scratchpadDao.insertNote(
                ScratchpadNote(
                    title = title,
                    content = content,
                    category = category,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
            )
        } else {
            scratchpadDao.updateNote(
                ScratchpadNote(
                    id = id,
                    title = title,
                    content = content,
                    category = category,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun deleteNote(id: Int) {
        scratchpadDao.deleteNoteById(id)
    }

    // PREFERENCES
    val themeStyleFlow: Flow<ThemeStyle> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_THEME_STYLE] ?: ThemeStyle.EMERALD_JANNAH.name
        try {
            ThemeStyle.valueOf(name)
        } catch (_: Exception) {
            ThemeStyle.EMERALD_JANNAH
        }
    }

    suspend fun setThemeStyle(style: ThemeStyle) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_STYLE] = style.name
        }
    }

    val themeModeFlow: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_THEME_MODE] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(name)
        } catch (_: Exception) {
            ThemeMode.SYSTEM
        }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { prefs ->
            prefs[KEY_THEME_MODE] = mode.name
        }
    }

    val fontScaleFlow: Flow<FontSizeScale> = context.dataStore.data.map { prefs ->
        val scale = prefs[KEY_FONT_SCALE] ?: 1.0f
        FontSizeScale.entries.minByOrNull { Math.abs(it.scale - scale) } ?: FontSizeScale.NORMAL
    }

    suspend fun setFontScale(scale: FontSizeScale) {
        context.dataStore.edit { prefs ->
            prefs[KEY_FONT_SCALE] = scale.scale
        }
    }

    val hanafiAsrFlow: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[KEY_HANAFI_ASR] ?: true
    }

    suspend fun setHanafiAsr(isHanafi: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[KEY_HANAFI_ASR] = isHanafi
        }
    }

    val englishFontFlow: Flow<EnglishFont> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_ENGLISH_FONT] ?: EnglishFont.ROBOTO.name
        try {
            EnglishFont.valueOf(name)
        } catch (_: Exception) {
            EnglishFont.ROBOTO
        }
    }

    suspend fun setEnglishFont(font: EnglishFont) {
        context.dataStore.edit { prefs ->
            prefs[KEY_ENGLISH_FONT] = font.name
        }
    }

    val primaryFontPreferenceFlow: Flow<PrimaryFontPreference> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_PRIMARY_FONT_PREF] ?: PrimaryFontPreference.BANGLA_PRIMARY.name
        try {
            PrimaryFontPreference.valueOf(name)
        } catch (_: Exception) {
            PrimaryFontPreference.BANGLA_PRIMARY
        }
    }

    suspend fun setPrimaryFontPreference(pref: PrimaryFontPreference) {
        context.dataStore.edit { prefs ->
            prefs[KEY_PRIMARY_FONT_PREF] = pref.name
        }
    }

    val banglaFontFlow: Flow<BanglaFont> = context.dataStore.data.map { prefs ->
        val name = prefs[KEY_BANGLA_FONT] ?: BanglaFont.NOTO_SANS_BENGALI.name
        try {
            BanglaFont.valueOf(name)
        } catch (_: Exception) {
            BanglaFont.NOTO_SANS_BENGALI
        }
    }

    suspend fun setBanglaFont(font: BanglaFont) {
        context.dataStore.edit { prefs ->
            prefs[KEY_BANGLA_FONT] = font.name
        }
    }

    val banglaFontWeightFlow: Flow<BanglaFontWeight> = context.dataStore.data.map { prefs ->
        val weightVal = prefs[KEY_BANGLA_WEIGHT] ?: BanglaFontWeight.NORMAL.weightValue
        BanglaFontWeight.fromValue(weightVal)
    }

    suspend fun setBanglaFontWeight(weight: BanglaFontWeight) {
        context.dataStore.edit { prefs ->
            prefs[KEY_BANGLA_WEIGHT] = weight.weightValue
        }
    }

    val salatConfigFlow: Flow<SalatConfiguration> = context.dataStore.data.map { prefs ->
        SalatConfiguration(
            placeNameBn = prefs[KEY_SALAT_PLACE_BN] ?: "ঢাকা, বাংলাদেশ",
            placeNameEn = prefs[KEY_SALAT_PLACE_EN] ?: "Dhaka, Bangladesh",
            latitude = (prefs[KEY_SALAT_LAT] ?: 23.8103f).toDouble(),
            longitude = (prefs[KEY_SALAT_LNG] ?: 90.4125f).toDouble(),
            isGpsEnabled = prefs[KEY_SALAT_IS_GPS] ?: false,
            isHanafiAsr = prefs[KEY_HANAFI_ASR] ?: true,
            manualOffsetMinutes = prefs[KEY_SALAT_OFFSET_MINS] ?: 0
        )
    }

    suspend fun updateSalatPlace(
        nameBn: String,
        nameEn: String,
        latitude: Double,
        longitude: Double,
        isGps: Boolean = false
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SALAT_PLACE_BN] = nameBn
            prefs[KEY_SALAT_PLACE_EN] = nameEn
            prefs[KEY_SALAT_LAT] = latitude.toFloat()
            prefs[KEY_SALAT_LNG] = longitude.toFloat()
            prefs[KEY_SALAT_IS_GPS] = isGps
        }
    }

    suspend fun setSalatOffsetMinutes(offsetMinutes: Int) {
        context.dataStore.edit { prefs ->
            prefs[KEY_SALAT_OFFSET_MINS] = offsetMinutes
        }
    }
}
