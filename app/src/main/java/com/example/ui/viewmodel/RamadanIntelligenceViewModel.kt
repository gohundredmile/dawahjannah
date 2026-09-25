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
import com.example.data.local.entity.RamadanPersonalDuaEntity
import com.example.data.local.entity.RamadanReflectionEntity
import com.example.data.local.entity.RamadanSettingsEntity
import com.example.data.local.entity.RamadanShawwalLogEntity
import com.example.data.model.CharityIdeaItem
import com.example.data.model.GoalIntensity
import com.example.data.model.RamadanChecklistItem
import com.example.data.model.RamadanDailyReflection
import com.example.data.model.RamadanDuaCategory
import com.example.data.model.RamadanDuaItem
import com.example.data.model.RamadanKnowledgeLesson
import com.example.data.model.RamadanLifestyleMode
import com.example.data.model.RamadanLoopTab
import com.example.data.model.RamadanTimeOfDay
import com.example.data.model.RamadanVerificationItem
import com.example.data.model.SalatConfiguration
import com.example.util.PrayerCalculator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class QuranPaceStatus(
    val totalPagesRead: Int = 0,
    val targetTotalPages: Int = 604,
    val expectedPagesToDate: Int = 0,
    val pagesBehindOrAhead: Int = 0,
    val todayRecommendedPages: Int = 20,
    val projectedCompletionDay: Int = 30,
    val paceMessageBn: String = "আপনি স্বাভাবিক গতিতে আছেন",
    val isBehind: Boolean = false,
    val catchUpStrategy: String = "GRADUAL" // GRADUAL, RESET, KEEP
)

data class AdaptiveSuggestion(
    val titleBn: String,
    val messageBn: String,
    val actionLabelBn: String,
    val onAcceptAction: () -> Unit
)

class RamadanIntelligenceViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val dao = db.ramadanDao()

    // Active Navigation Loop
    private val _currentLoop = MutableStateFlow(RamadanLoopTab.DASHBOARD)
    val currentLoop: StateFlow<RamadanLoopTab> = _currentLoop.asStateFlow()

    // Current Selected Ramadan Day (1 to 30)
    private val _selectedDay = MutableStateFlow(1)
    val selectedDay: StateFlow<Int> = _selectedDay.asStateFlow()

    // Time of Day Adaptive Dashboard Mode
    private val _timeOfDay = MutableStateFlow(determineCurrentTimeOfDay())
    val timeOfDay: StateFlow<RamadanTimeOfDay> = _timeOfDay.asStateFlow()

    // Lifestyle Mode
    private val _lifestyleMode = MutableStateFlow(RamadanLifestyleMode.PROFESSIONAL)
    val lifestyleMode: StateFlow<RamadanLifestyleMode> = _lifestyleMode.asStateFlow()

    // Goal Level Selection
    private val _goalIntensity = MutableStateFlow(GoalIntensity.TARGET)
    val goalIntensity: StateFlow<GoalIntensity> = _goalIntensity.asStateFlow()

    // Selected Dua Category Filter
    private val _selectedDuaCategory = MutableStateFlow(RamadanDuaCategory.SUHOOR)
    val selectedDuaCategory: StateFlow<RamadanDuaCategory> = _selectedDuaCategory.asStateFlow()

    // Verification Query
    private val _verificationSearchQuery = MutableStateFlow("")
    val verificationSearchQuery: StateFlow<String> = _verificationSearchQuery.asStateFlow()

    // Verification Custom Text Test State
    private val _customVerifyResult = MutableStateFlow<RamadanVerificationItem?>(null)
    val customVerifyResult: StateFlow<RamadanVerificationItem?> = _customVerifyResult.asStateFlow()

    // Salat / Prayer Status
    val salatConfig = MutableStateFlow(SalatConfiguration())
    val prayerStatus = salatConfig.map { config ->
        PrayerCalculator.calculatePrayers(
            cal = Calendar.getInstance(),
            isHanafiAsr = config.isHanafiAsr,
            latitude = config.latitude,
            longitude = config.longitude,
            locationNameBn = config.placeNameBn,
            locationNameEn = config.placeNameEn,
            calculationMethod = config.calculationMethod,
            asrMethod = config.asrMethod,
            highLatitudeRule = config.highLatitudeRule,
            timezoneOffsetHours = config.timezoneOffsetHours
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, PrayerCalculator.calculatePrayers())

    // Database flows
    val dayLogs: StateFlow<List<RamadanDayLogEntity>> = dao.getAllDayLogs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val checklistStatuses: StateFlow<List<RamadanChecklistEntity>> = dao.getAllChecklistStatuses()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val missedFasts: StateFlow<List<RamadanMissedFastEntity>> = dao.getAllMissedFasts()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val shawwalLogs: StateFlow<List<RamadanShawwalLogEntity>> = dao.getAllShawwalLogs()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val charityEntries: StateFlow<List<RamadanCharityEntryEntity>> = dao.getAllCharityEntries()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val personalDuas: StateFlow<List<RamadanPersonalDuaEntity>> = dao.getAllPersonalDuas()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val reflections: StateFlow<List<RamadanReflectionEntity>> = dao.getAllReflections()
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    val settings: StateFlow<RamadanSettingsEntity?> = dao.getSettings()
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    // Current Selected Day Log
    val currentDayLog: StateFlow<RamadanDayLogEntity> = combine(selectedDay, dayLogs) { day, logs ->
        logs.find { it.dayNumber == day } ?: RamadanDayLogEntity(dayNumber = day)
    }.stateIn(viewModelScope, SharingStarted.Lazily, RamadanDayLogEntity(dayNumber = 1))

    // Current Day Reflection
    val currentDayReflection: StateFlow<RamadanReflectionEntity> = combine(selectedDay, reflections) { day, refls ->
        refls.find { it.dayNumber == day } ?: RamadanReflectionEntity(dayNumber = day)
    }.stateIn(viewModelScope, SharingStarted.Lazily, RamadanReflectionEntity(dayNumber = 1))

    // Quran Khatm & Pace Status (Non-punitive)
    val quranPaceStatus: StateFlow<QuranPaceStatus> = combine(
        selectedDay,
        dayLogs,
        settings
    ) { day, logs, sett ->
        val totalPagesRead = logs.sumOf { it.quranPagesRead }
        val targetKhatms = sett?.quranTargetKhatms ?: 1
        val targetTotalPages = 604 * targetKhatms
        val expectedPagesPerDay = (targetTotalPages / 30.0).toInt().coerceAtLeast(1)
        val expectedToDate = (expectedPagesPerDay * day).coerceAtMost(targetTotalPages)
        val diff = totalPagesRead - expectedToDate
        val isBehind = diff < -5
        val remainingDays = (30 - day).coerceAtLeast(1)
        val remainingPages = (targetTotalPages - totalPagesRead).coerceAtLeast(0)
        val catchUpDailyTarget = (remainingPages / remainingDays).coerceAtLeast(1)

        val message = when {
            diff >= 0 -> "মাশাআল্লাহ! আপনি নির্ধারিত লক্ষ্যের সাথে সুন্দর গতিতে আছেন।"
            diff >= -20 -> "সামান্য পেছনে আছেন (${-diff} পৃষ্ঠা)। প্রতিদিন ১-২ পৃষ্ঠা বেশি পড়ে সহজে পূরণ সম্ভব।"
            else -> "আপনি ${-diff / 20} পারা পেছনে আছেন। কোনো চাপ ছাড়া ধাপে ধাপে কভার করতে পারেন অথবা লক্ষ্য সমন্বয় করতে পারেন।"
        }

        QuranPaceStatus(
            totalPagesRead = totalPagesRead,
            targetTotalPages = targetTotalPages,
            expectedPagesToDate = expectedToDate,
            pagesBehindOrAhead = diff,
            todayRecommendedPages = catchUpDailyTarget,
            projectedCompletionDay = if (totalPagesRead > 0) ((day.toFloat() / totalPagesRead) * targetTotalPages).toInt().coerceIn(20, 45) else 30,
            paceMessageBn = message,
            isBehind = isBehind,
            catchUpStrategy = sett?.quranPaceStrategy ?: "GRADUAL"
        )
    }.stateIn(viewModelScope, SharingStarted.Lazily, QuranPaceStatus())

    // Adaptive System Suggestions (Learning from user habits)
    val adaptiveSuggestion: StateFlow<AdaptiveSuggestion?> = combine(
        dayLogs,
        selectedDay,
        quranPaceStatus
    ) { logs, day, pace ->
        val fastedConsecutive = logs.filter { it.dayNumber <= day && it.isFasted }.size
        if (pace.isBehind) {
            AdaptiveSuggestion(
                titleBn = "কুরআন পাঠ সমন্বয় নসীহত",
                messageBn = "সকালের কুরআন লক্ষ্য পূরণ করতে কষ্ট হলে, কিছুটা অংশ ইশার সালাতের পর বা তারাবীহর পূর্বে স্থানান্তরিত করতে চান?",
                actionLabelBn = "ধাপে ধাপে সমন্বয় করুন",
                onAcceptAction = {
                    setQuranPaceStrategy("GRADUAL")
                }
            )
        } else if (fastedConsecutive >= 3 && logs.none { it.spiritualScore >= 5 }) {
            AdaptiveSuggestion(
                titleBn = "ধারাবাহিকতা ও প্রশান্তি",
                messageBn = "মাশাআল্লাহ! আপনি পরপর কয়েকদিন সুন্দরভাবে সিয়াম বজায় রেখেছেন। আজ কি একটি সংক্ষিপ্ত আত্মদর্শন বা শোকর আদায় যুক্ত করবেন?",
                actionLabelBn = "আজকের প্রশ্ন লিখুন",
                onAcceptAction = {
                    setLoop(RamadanLoopTab.DASHBOARD)
                }
            )
        } else {
            null
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getSettingsDirect()
            if (existing == null) {
                dao.insertOrUpdateSettings(RamadanSettingsEntity())
            } else {
                _lifestyleMode.value = RamadanLifestyleMode.fromId(existing.userLifestyleModeId)
                _selectedDay.value = existing.currentSelectedRamadanDay.coerceIn(1, 30)
            }
        }
    }

    private fun determineCurrentTimeOfDay(): RamadanTimeOfDay {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour in 4..11 -> RamadanTimeOfDay.MORNING
            hour in 12..16 -> RamadanTimeOfDay.AFTERNOON
            hour in 17..18 -> RamadanTimeOfDay.PRE_MAGHRIB
            else -> RamadanTimeOfDay.NIGHT
        }
    }

    fun setLoop(loop: RamadanLoopTab) {
        _currentLoop.value = loop
    }

    fun setSelectedDay(day: Int) {
        val clamped = day.coerceIn(1, 30)
        _selectedDay.value = clamped
        viewModelScope.launch(Dispatchers.IO) {
            val s = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(s.copy(currentSelectedRamadanDay = clamped))
        }
    }

    fun setTimeOfDay(time: RamadanTimeOfDay) {
        _timeOfDay.value = time
    }

    fun setLifestyleMode(mode: RamadanLifestyleMode) {
        _lifestyleMode.value = mode
        viewModelScope.launch(Dispatchers.IO) {
            val s = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(s.copy(userLifestyleModeId = mode.id))
        }
    }

    fun setGoalIntensity(intensity: GoalIntensity) {
        _goalIntensity.value = intensity
    }

    fun setSelectedDuaCategory(category: RamadanDuaCategory) {
        _selectedDuaCategory.value = category
    }

    fun setVerificationSearchQuery(query: String) {
        _verificationSearchQuery.value = query
    }

    fun updateDayLog(
        isFasted: Boolean? = null,
        isTaraweeh: Boolean? = null,
        taraweehRakahs: Int? = null,
        quranPagesRead: Int? = null,
        charityAmount: Double? = null,
        duaCompleted: Boolean? = null,
        characterGoalDone: Boolean? = null,
        tahajjudCompleted: Boolean? = null,
        witrCompleted: Boolean? = null,
        notes: String? = null
    ) {
        val day = _selectedDay.value
        viewModelScope.launch(Dispatchers.IO) {
            val existing = dao.getDayLog(day) ?: RamadanDayLogEntity(dayNumber = day)
            val updated = existing.copy(
                isFasted = isFasted ?: existing.isFasted,
                isTaraweeh = isTaraweeh ?: existing.isTaraweeh,
                taraweehRakahs = taraweehRakahs ?: existing.taraweehRakahs,
                quranPagesRead = quranPagesRead ?: existing.quranPagesRead,
                charityAmount = charityAmount ?: existing.charityAmount,
                duaCompleted = duaCompleted ?: existing.duaCompleted,
                characterGoalDone = characterGoalDone ?: existing.characterGoalDone,
                tahajjudCompleted = tahajjudCompleted ?: existing.tahajjudCompleted,
                witrCompleted = witrCompleted ?: existing.witrCompleted,
                notes = notes ?: existing.notes,
                updatedAt = System.currentTimeMillis()
            )
            dao.insertOrUpdateDayLog(updated)
        }
    }

    fun saveDailyReflection(day: Int, question: String, answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertOrUpdateReflection(
                RamadanReflectionEntity(
                    dayNumber = day,
                    promptQuestion = question,
                    userAnswer = answer,
                    updatedAt = System.currentTimeMillis()
                )
            )
        }
    }

    fun toggleChecklist(itemKey: String, isChecked: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertOrUpdateChecklist(
                RamadanChecklistEntity(
                    itemKey = itemKey,
                    isChecked = isChecked,
                    completedAt = if (isChecked) System.currentTimeMillis() else 0L
                )
            )
        }
    }

    fun setQuranPaceStrategy(strategy: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val s = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(s.copy(quranPaceStrategy = strategy))
        }
    }

    fun setNotificationIntensity(intensity: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val s = dao.getSettingsDirect() ?: RamadanSettingsEntity()
            dao.insertOrUpdateSettings(s.copy(notificationIntensity = intensity))
        }
    }

    fun addPersonalDua(title: String, arabic: String, meaning: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertPersonalDua(
                RamadanPersonalDuaEntity(
                    title = title,
                    arabicText = arabic,
                    meaningBn = meaning,
                    categoryBn = category
                )
            )
        }
    }

    fun deletePersonalDua(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deletePersonalDua(id)
        }
    }

    fun addCharityEntry(title: String, amount: Double, category: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertCharityEntry(
                RamadanCharityEntryEntity(
                    title = title,
                    amount = amount,
                    category = category,
                    date = date
                )
            )
        }
    }

    fun deleteCharityEntry(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteCharityEntry(id)
        }
    }

    fun toggleShawwalFast(fastNumber: Int, isDone: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            val dateStr = if (isDone) SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date()) else ""
            dao.insertOrUpdateShawwalLog(
                RamadanShawwalLogEntity(
                    fastNumber = fastNumber,
                    isCompleted = isDone,
                    completedDate = dateStr
                )
            )
        }
    }

    fun addMissedFast(reason: String, days: Int, notes: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertMissedFast(
                RamadanMissedFastEntity(
                    reason = reason,
                    totalMissedDays = days,
                    recoveredDays = 0,
                    notes = notes
                )
            )
        }
    }

    fun recoverMissedFast(id: Int, increment: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentList = missedFasts.value
            val target = currentList.find { it.id == id } ?: return@launch
            val newRecovered = (target.recoveredDays + increment).coerceIn(0, target.totalMissedDays)
            dao.updateMissedFast(
                target.copy(
                    recoveredDays = newRecovered,
                    isFullyResolved = newRecovered >= target.totalMissedDays
                )
            )
        }
    }

    fun verifyMessageText(query: String) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            _customVerifyResult.value = null
            return
        }
        val match = RamadanDataCatalog.verificationClaims.find {
            it.viralClaimBn.contains(trimmed, ignoreCase = true) ||
            trimmed.contains(it.id, ignoreCase = true)
        }
        if (match != null) {
            _customVerifyResult.value = match
        } else {
            _customVerifyResult.value = RamadanVerificationItem(
                id = "custom_analysis",
                viralClaimBn = trimmed,
                verdictLabelBn = "তাহকীক ও প্রামাণ্য যাচাই পরামর্শ",
                verdictColorHex = 0xFF0284C7,
                authenticitySummaryBn = "উক্ত বক্তব্যটি যাচাইয়ের জন্য কুরআন ও নির্ভরযোগ্য হাদীস গ্রন্থ (বুখারী, মুসলিম, আবু দাউদ, তিরমিজি, নাসাঈ, ইবনে মাজাহ) অনুসন্ধান করুন।",
                hadithOrQuranSource = "সহীহ ইসলামী নীতি: «কোনো কথা শোনার পর নিশ্চিত না হয়ে বর্ণনা করাই মিথ্যাবাদী হওয়ার জন্য যথেষ্ট» (সহীহ মুসলিম ৫)",
                scholarlyExplanationBn = "রমাদানের কোনো ফযিলত বা বিশেষ সাওয়াবের কথা আসলে অবশ্যই নির্ভরযোগ্য সনদ ও উলামাদের তাহকীক নিশ্চিত করে আমল করা উচিত। মনগড়া বার্তা শেয়ার থেকে বিরত থাকুন।"
            )
        }
    }
}
