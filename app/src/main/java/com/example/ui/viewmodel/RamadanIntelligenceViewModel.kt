package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.RamadanDataCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.entity.RamadanCharityEntryEntity
import com.example.data.local.entity.RamadanChecklistEntity
import com.example.data.local.entity.RamadanDayLogEntity
import com.example.data.local.entity.RamadanMissedFastEntity
import com.example.data.local.entity.RamadanSettingsEntity
import com.example.data.local.entity.RamadanShawwalLogEntity
import com.example.data.model.AfterRamadanSection
import com.example.data.model.BeforeRamadanSection
import com.example.data.model.DuringRamadanSection
import com.example.data.model.RamadanPhase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RamadanOverallStats(
    val fastedDaysCount: Int = 0,
    val taraweehNightsCount: Int = 0,
    val totalQuranPagesRead: Int = 0,
    val targetQuranPages: Int = 604,
    val quranProgressPercent: Float = 0f,
    val totalCharityAmount: Double = 0.0,
    val charityBudget: Double = 5000.0,
    val shawwalFastsCompleted: Int = 0,
    val missedFastsRemaining: Int = 0,
    val missedFastsRecovered: Int = 0
)

class RamadanIntelligenceViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.ramadanDao()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    private val _currentPhase = MutableStateFlow(RamadanPhase.BEFORE_RAMADAN)
    val currentPhase: StateFlow<RamadanPhase> = _currentPhase.asStateFlow()

    private val _beforeSection = MutableStateFlow(BeforeRamadanSection.CHECKLIST)
    val beforeSection: StateFlow<BeforeRamadanSection> = _beforeSection.asStateFlow()

    private val _duringSection = MutableStateFlow(DuringRamadanSection.FASTING_TRACKER)
    val duringSection: StateFlow<DuringRamadanSection> = _duringSection.asStateFlow()

    private val _afterSection = MutableStateFlow(AfterRamadanSection.MISSED_FASTS)
    val afterSection: StateFlow<AfterRamadanSection> = _afterSection.asStateFlow()

    // 30 Days Database Logs
    val dayLogs: StateFlow<List<RamadanDayLogEntity>> = dao.getAllDayLogs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Checklist from DB
    val checklistStatuses: StateFlow<List<RamadanChecklistEntity>> = dao.getAllChecklistStatuses()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Missed fasts
    val missedFasts: StateFlow<List<RamadanMissedFastEntity>> = dao.getAllMissedFasts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Shawwal fasts
    val shawwalLogs: StateFlow<List<RamadanShawwalLogEntity>> = dao.getAllShawwalLogs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Charity entries
    val charityEntries: StateFlow<List<RamadanCharityEntryEntity>> = dao.getAllCharityEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    // Settings
    val settings: StateFlow<RamadanSettingsEntity?> = dao.getSettings()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Combined stats
    val overallStats: StateFlow<RamadanOverallStats> = combine(
        dayLogs,
        charityEntries,
        shawwalLogs,
        missedFasts,
        settings
    ) { logs, charities, shawwal, missed, sett ->
        val fasted = logs.count { it.isFasted }
        val taraweeh = logs.count { it.isTaraweeh }
        val pages = logs.sumOf { it.quranPagesRead }
        val targetKhatms = sett?.quranTargetKhatms ?: 1
        val targetPages = 604 * targetKhatms
        val progress = if (targetPages > 0) (pages.toFloat() / targetPages.toFloat()).coerceIn(0f, 1f) else 0f
        val charityLogged = charities.sumOf { it.amount } + logs.sumOf { it.charityAmount }
        val charityBudget = sett?.charityTargetBudget ?: 5000.0
        val shawwalDone = shawwal.count { it.isCompleted }
        val totalMissed = missed.sumOf { it.totalMissedDays }
        val recovered = missed.sumOf { it.recoveredDays }
        val remainingMissed = (totalMissed - recovered).coerceAtLeast(0)

        RamadanOverallStats(
            fastedDaysCount = fasted,
            taraweehNightsCount = taraweeh,
            totalQuranPagesRead = pages,
            targetQuranPages = targetPages,
            quranProgressPercent = progress,
            totalCharityAmount = charityLogged,
            charityBudget = charityBudget,
            shawwalFastsCompleted = shawwalDone,
            missedFastsRemaining = remainingMissed,
            missedFastsRecovered = recovered
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, RamadanOverallStats())

    init {
        // Seed default settings if empty
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getSettingsDirect()
            if (existing == null) {
                dao.insertOrUpdateSettings(RamadanSettingsEntity())
            }
        }
    }

    fun setPhase(phase: RamadanPhase) {
        _currentPhase.value = phase
    }

    fun setBeforeSection(section: BeforeRamadanSection) {
        _beforeSection.value = section
    }

    fun setDuringSection(section: DuringRamadanSection) {
        _duringSection.value = section
    }

    fun setAfterSection(section: AfterRamadanSection) {
        _afterSection.value = section
    }

    // --- CHECKLIST ACTIONS ---
    fun toggleChecklistItem(itemKey: String, currentChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertOrUpdateChecklist(
                RamadanChecklistEntity(
                    itemKey = itemKey,
                    isChecked = !currentChecked,
                    completedAt = if (!currentChecked) System.currentTimeMillis() else 0L
                )
            )
        }
    }

    // --- FASTING & DAY LOG ACTIONS ---
    fun toggleFastingDay(dayNumber: Int, currentFasted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getDayLog(dayNumber)
            val updated = existing?.copy(
                isFasted = !currentFasted,
                updatedAt = System.currentTimeMillis()
            ) ?: RamadanDayLogEntity(
                dayNumber = dayNumber,
                isFasted = !currentFasted
            )
            dao.insertOrUpdateDayLog(updated)
        }
    }

    fun toggleTaraweeh(dayNumber: Int, currentTaraweeh: Boolean, rakahs: Int = 8) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getDayLog(dayNumber)
            val updated = existing?.copy(
                isTaraweeh = !currentTaraweeh,
                taraweehRakahs = rakahs,
                updatedAt = System.currentTimeMillis()
            ) ?: RamadanDayLogEntity(
                dayNumber = dayNumber,
                isTaraweeh = !currentTaraweeh,
                taraweehRakahs = rakahs
            )
            dao.insertOrUpdateDayLog(updated)
        }
    }

    fun addQuranPages(dayNumber: Int, addedPages: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getDayLog(dayNumber)
            val currentPages = existing?.quranPagesRead ?: 0
            val newPages = (currentPages + addedPages).coerceAtLeast(0)
            val updated = existing?.copy(
                quranPagesRead = newPages,
                updatedAt = System.currentTimeMillis()
            ) ?: RamadanDayLogEntity(
                dayNumber = dayNumber,
                quranPagesRead = newPages
            )
            dao.insertOrUpdateDayLog(updated)
        }
    }

    fun updateDayLog(
        dayNumber: Int,
        isFasted: Boolean,
        isTaraweeh: Boolean,
        taraweehRakahs: Int,
        quranPagesRead: Int,
        charityAmount: Double,
        notes: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getDayLog(dayNumber)
            val updated = existing?.copy(
                isFasted = isFasted,
                isTaraweeh = isTaraweeh,
                taraweehRakahs = taraweehRakahs,
                quranPagesRead = quranPagesRead,
                charityAmount = charityAmount,
                notes = notes,
                updatedAt = System.currentTimeMillis()
            ) ?: RamadanDayLogEntity(
                dayNumber = dayNumber,
                isFasted = isFasted,
                isTaraweeh = isTaraweeh,
                taraweehRakahs = taraweehRakahs,
                quranPagesRead = quranPagesRead,
                charityAmount = charityAmount,
                notes = notes
            )
            dao.insertOrUpdateDayLog(updated)
        }
    }

    // --- CHARITY ACTIONS ---
    fun addCharityEntry(title: String, amount: Double, category: String, notes: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCharityEntry(
                RamadanCharityEntryEntity(
                    title = title,
                    amount = amount,
                    date = dateFormat.format(Date()),
                    category = category,
                    notes = notes
                )
            )
        }
    }

    fun deleteCharityEntry(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCharityEntry(id)
        }
    }

    // --- MISSED FAST ACTIONS ---
    fun addMissedFast(reason: String, daysCount: Int, notes: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertMissedFast(
                RamadanMissedFastEntity(
                    reason = reason,
                    totalMissedDays = daysCount,
                    recoveredDays = 0,
                    notes = notes
                )
            )
        }
    }

    fun incrementMissedRecovered(id: Int, currentRecovered: Int, total: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val newRecovered = (currentRecovered + 1).coerceAtMost(total)
            dao.updateMissedFast(
                RamadanMissedFastEntity(
                    id = id,
                    reason = "", // overridden by room if done via query or fetch
                    totalMissedDays = total,
                    recoveredDays = newRecovered,
                    isFullyResolved = newRecovered >= total
                )
            )
        }
    }

    fun updateMissedFastRecord(item: RamadanMissedFastEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateMissedFast(item)
        }
    }

    fun deleteMissedFast(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteMissedFast(id)
        }
    }

    // --- SHAWWAL ACTIONS ---
    fun toggleShawwalFast(fastNumber: Int, currentStatus: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertOrUpdateShawwalLog(
                RamadanShawwalLogEntity(
                    fastNumber = fastNumber,
                    isCompleted = !currentStatus,
                    completedDate = if (!currentStatus) dateFormat.format(Date()) else ""
                )
            )
        }
    }

    // --- SETTINGS ACTIONS ---
    fun updateQuranTargetKhatms(target: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(current.copy(quranTargetKhatms = target))
        }
    }

    fun updateCharityBudget(budget: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(current.copy(charityTargetBudget = budget))
        }
    }

    fun updateShaBanFastingCount(count: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(current.copy(shaBanFastingCount = count))
        }
    }

    fun toggleHabitSetting(habitName: String, currentValue: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val current = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            val updated = when (habitName) {
                "prayer" -> current.copy(habitPrayerOnTime = !currentValue)
                "quran" -> current.copy(habitDailyQuran = !currentValue)
                "tahajjud" -> current.copy(habitTahajjudWitr = !currentValue)
                "ayyam" -> current.copy(habitAyyamBeed = !currentValue)
                "charity" -> current.copy(habitWeeklyCharity = !currentValue)
                else -> current
            }
            dao.insertOrUpdateSettings(updated)
        }
    }
}
