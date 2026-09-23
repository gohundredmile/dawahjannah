package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.IslamicHabitCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.entity.SunnahHabitLog
import com.example.data.model.HabitCategory
import com.example.data.model.SunnahHabitItem
import com.example.data.model.SunnahWeeklyStats
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class HabitJourneyStatus(val labelBn: String, val badgeColorHex: Long) {
    COMPLETED_TODAY("আজ সম্পন্ন • আলহামদুলিল্লাহ", 0xFF059669),
    BEING_DEVELOPED("ধারাবাহিকতার অনুশীলন চলছে", 0xFFD97706),
    TO_REVISIT("পুনরায় চর্চার সুবর্ণ সুযোগ", 0xFF0284C7)
}

data class SunnahHabitUiModel(
    val habit: SunnahHabitItem,
    val isCompletedToday: Boolean,
    val completionsThisWeek: Int,
    val journeyStatus: HabitJourneyStatus,
    val note: String = ""
)

class IslamicHabitViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val habitDao = db.sunnahHabitDao()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    private val displayDateFormat = SimpleDateFormat("EEEE, d MMMM yyyy", Locale("bn", "BD"))

    private val _selectedDate = MutableStateFlow(getTodayDateString())
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _selectedCategory = MutableStateFlow(HabitCategory.ALL)
    val selectedCategory: StateFlow<HabitCategory> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedHabitForDetail = MutableStateFlow<SunnahHabitItem?>(null)
    val selectedHabitForDetail: StateFlow<SunnahHabitItem?> = _selectedHabitForDetail.asStateFlow()

    // Week range strings
    private val weekStartDate = getWeekStartDateString()
    private val todayDate = getTodayDateString()

    // Observe logs for today
    private val todayLogsFlow = habitDao.getLogsForDate(todayDate)

    // Observe logs for the current 7-day rolling week
    private val weeklyLogsFlow = habitDao.getLogsBetweenDates(weekStartDate, todayDate)

    val weeklyStats: StateFlow<SunnahWeeklyStats> = weeklyLogsFlow.combine(todayLogsFlow) { weeklyLogs, _ ->
        calculateWeeklyStats(weeklyLogs)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SunnahWeeklyStats()
    )

    val habitUiList: StateFlow<List<SunnahHabitUiModel>> = combine(
        todayLogsFlow,
        weeklyLogsFlow,
        _selectedCategory,
        _searchQuery
    ) { todayLogs, weeklyLogs, category, query ->
        val todayLogMap = todayLogs.associateBy { it.habitId }
        val weeklyCountMap = weeklyLogs.groupingBy { it.habitId }.eachCount()

        IslamicHabitCatalog.habits
            .filter { item ->
                (category == HabitCategory.ALL || item.category == category) &&
                (query.isBlank() ||
                 item.titleBn.contains(query, ignoreCase = true) ||
                 item.titleEn.contains(query, ignoreCase = true) ||
                 item.translationBn.contains(query, ignoreCase = true) ||
                 item.fiqhStatusBn.contains(query, ignoreCase = true))
            }
            .map { item ->
                val isCompleted = todayLogMap.containsKey(item.id)
                val countThisWeek = weeklyCountMap[item.id] ?: 0
                val note = todayLogMap[item.id]?.reflectionNote ?: ""

                val status = when {
                    isCompleted -> HabitJourneyStatus.COMPLETED_TODAY
                    countThisWeek in 1..4 -> HabitJourneyStatus.BEING_DEVELOPED
                    else -> HabitJourneyStatus.TO_REVISIT
                }

                SunnahHabitUiModel(
                    habit = item,
                    isCompletedToday = isCompleted,
                    completionsThisWeek = countThisWeek,
                    journeyStatus = status,
                    note = note
                )
            }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectCategory(category: HabitCategory) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun openHabitDetail(habit: SunnahHabitItem?) {
        _selectedHabitForDetail.value = habit
    }

    fun toggleHabitToday(habitId: String, currentCompleted: Boolean, note: String = "") {
        viewModelScope.launch(Dispatchers.IO) {
            val date = todayDate
            if (currentCompleted) {
                habitDao.deleteByHabitAndDate(habitId, date)
            } else {
                val log = SunnahHabitLog(
                    id = "${habitId}_${date}",
                    habitId = habitId,
                    date = date,
                    timestamp = System.currentTimeMillis(),
                    reflectionNote = note
                )
                habitDao.insertOrUpdate(log)
            }
        }
    }

    fun updateHabitNote(habitId: String, note: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val date = todayDate
            val log = SunnahHabitLog(
                id = "${habitId}_${date}",
                habitId = habitId,
                date = date,
                timestamp = System.currentTimeMillis(),
                reflectionNote = note
            )
            habitDao.insertOrUpdate(log)
        }
    }

    private fun calculateWeeklyStats(weeklyLogs: List<SunnahHabitLog>): SunnahWeeklyStats {
        val totalLogs = weeklyLogs.size
        val weeklyCountMap = weeklyLogs.groupingBy { it.habitId }.eachCount()

        var completedCount = totalLogs
        var developingCount = 0
        var revisitCount = 0

        IslamicHabitCatalog.habits.forEach { habit ->
            val count = weeklyCountMap[habit.id] ?: 0
            when {
                count >= 3 -> {
                    // Solid habit
                }
                count in 1..2 -> {
                    developingCount++
                }
                else -> {
                    revisitCount++
                }
            }
        }

        return SunnahWeeklyStats(
            completedPracticesCount = completedCount,
            developingPracticesCount = developingCount,
            revisitPracticesCount = revisitCount,
            totalLoggedThisWeek = totalLogs
        )
    }

    fun getDisplayDateBengali(): String {
        return try {
            displayDateFormat.format(Date())
        } catch (_: Exception) {
            "আজকের সুন্নাহ অভিযাত্রা"
        }
    }

    private fun getTodayDateString(): String = dateFormat.format(Date())

    private fun getWeekStartDateString(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -6)
        return dateFormat.format(calendar.time)
    }
}
