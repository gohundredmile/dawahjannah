package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.datasource.AsmaulHusnaData
import com.example.data.datasource.DuaVaultData
import com.example.data.datasource.DuroodData
import com.example.data.datasource.HabitData
import com.example.data.datasource.HealthDuaData
import com.example.data.datasource.InspirationData
import com.example.data.datasource.RoutineData
import com.example.data.datasource.WisdomApiService
import com.example.data.remote.GitHubReleaseInfo
import com.example.data.remote.GitHubUpdateManager
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.ScratchpadNote
import com.example.data.model.AllahNameItem
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.DailyInspiration
import com.example.data.model.DailyWisdomState
import com.example.data.model.DuaItem
import com.example.data.model.DuroodItem
import com.example.data.model.EnglishFont
import com.example.data.model.FontSizeScale
import com.example.data.model.HealthDuaItem
import com.example.data.model.PRESET_SALAT_PLACES
import com.example.data.model.PrimaryFontPreference
import com.example.data.model.RoutineItem
import com.example.data.model.SalatConfiguration
import com.example.data.model.SalatPlaceInfo
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle
import com.example.data.repository.AppRepository
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
import com.example.util.VibrationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class AppTab(val index: Int, val titleBn: String) {
    HOME(0, "হোম"),
    DUA(1, "মাসনুন দোয়া"),
    ROUTINE(2, "২৪ঘণ্টা আমল"),
    CHECKLIST(3, "চেকলিস্ট"),
    MORE(4, "আরও")
}

enum class MoreSubScreen(val titleBn: String) {
    MAIN("আরও বিষয়সমূহ"),
    NAMES_OF_ALLAH("আসমাউল হুসনা (৯৯ নাম)"),
    TASBIH("ডিজিটাল তাসবীহ"),
    DUROOD_ISTIGHFAR("দরূদ ও ইস্তিগফার"),
    HEALTH_DUAS("রোগ নিরাময় ও আশ্রয়"),
    SCRATCHPAD("ব্যক্তিগত দোয়া জার্নাল"),
    SETTINGS("সেটিংস ও অ্যাপ থিম")
}

data class TasbihState(
    val currentDhikr: String = "সুবহানাল্লাহ (سُبْحَانَ اللَّهِ)",
    val count: Int = 0,
    val target: Int = 33, // 33, 100, or 0 (Unlimited)
    val totalCount: Int = 0
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepository(application)
    private val wisdomApiService = WisdomApiService(application)
    val gitHubUpdateManager = GitHubUpdateManager(application)

    // NAVIGATION
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _moreSubScreen = MutableStateFlow(MoreSubScreen.MAIN)
    val moreSubScreen: StateFlow<MoreSubScreen> = _moreSubScreen.asStateFlow()

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun navigateToMoreSubScreen(sub: MoreSubScreen) {
        _moreSubScreen.value = sub
    }

    fun navigateBackToMore() {
        _moreSubScreen.value = MoreSubScreen.MAIN
    }

    // CALENDAR & CLOCK & PRAYERS
    private val _currentDate = MutableStateFlow(Date())
    val tripleCalendar = _currentDate.combine(_currentTab) { date, _ ->
        CalendarHelper.getTripleCalendar(date)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CalendarHelper.getTripleCalendar())

    private val _prayerNotificationSettings = MutableStateFlow(
        mapOf(
            "tahajjud" to true,
            "fajr" to true,
            "sunrise" to false,
            "dhuhr" to true,
            "asr" to true,
            "maghrib" to true,
            "isha" to true
        )
    )
    val prayerNotificationSettings = _prayerNotificationSettings.asStateFlow()

    fun togglePrayerNotification(prayerId: String) {
        val current = _prayerNotificationSettings.value.toMutableMap()
        current[prayerId] = !(current[prayerId] ?: true)
        _prayerNotificationSettings.value = current
    }

    val salatConfig = repository.salatConfigFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        SalatConfiguration()
    )

    private val _gpsStatusMessage = MutableStateFlow<String?>(null)
    val gpsStatusMessage = _gpsStatusMessage.asStateFlow()

    fun clearGpsMessage() {
        _gpsStatusMessage.value = null
    }

    fun updateSalatPlace(place: SalatPlaceInfo) {
        viewModelScope.launch {
            val fullNameBn = if (place.category != "international") "${place.nameBn}, বাংলাদেশ" else place.nameBn
            repository.updateSalatPlace(
                nameBn = fullNameBn,
                nameEn = place.nameEn,
                latitude = place.latitude,
                longitude = place.longitude,
                isGps = false
            )
            _gpsStatusMessage.value = "অবস্থান নির্ধারণ: ${place.nameBn}"
        }
    }

    fun setCustomSalatLocation(nameBn: String, nameEn: String, lat: Double, lng: Double) {
        viewModelScope.launch {
            repository.updateSalatPlace(
                nameBn = nameBn.ifBlank { "কাস্টম অবস্থান" },
                nameEn = nameEn.ifBlank { "Custom Location" },
                latitude = lat,
                longitude = lng,
                isGps = false
            )
            _gpsStatusMessage.value = "কাস্টম অবস্থান সেট করা হয়েছে"
        }
    }

    fun setSalatManualOffset(minutes: Int) {
        viewModelScope.launch {
            repository.setSalatOffsetMinutes(minutes)
        }
    }

    fun trackCurrentLocationWithGps() {
        val app = getApplication<Application>()
        val hasFine = ContextCompat.checkSelfPermission(app, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(app, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            _gpsStatusMessage.value = "GPS লোকেশন পারমিশন দিন"
            return
        }

        try {
            val locationManager = app.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            if (!isGpsEnabled && !isNetworkEnabled) {
                _gpsStatusMessage.value = "অনুগ্রহ করে ডিভাইসের GPS লোকেশন চালু করুন"
                return
            }

            var bestLocation: Location? = null
            if (isGpsEnabled) {
                bestLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            }
            if (bestLocation == null && isNetworkEnabled) {
                bestLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            if (bestLocation != null) {
                applyGpsCoordinates(bestLocation.latitude, bestLocation.longitude)
            } else {
                val provider = if (isGpsEnabled) LocationManager.GPS_PROVIDER else LocationManager.NETWORK_PROVIDER
                val locationListener = object : LocationListener {
                    override fun onLocationChanged(loc: Location) {
                        applyGpsCoordinates(loc.latitude, loc.longitude)
                        locationManager.removeUpdates(this)
                    }
                    @Deprecated("Deprecated in Java")
                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                    override fun onProviderEnabled(provider: String) {}
                    override fun onProviderDisabled(provider: String) {}
                }
                locationManager.requestSingleUpdate(provider, locationListener, Looper.getMainLooper())
                _gpsStatusMessage.value = "GPS সিগন্যাল ট্র্যাক করা হচ্ছে..."
            }
        } catch (e: SecurityException) {
            _gpsStatusMessage.value = "লোকেশন পারমিশন প্রয়োজন"
        } catch (e: Exception) {
            _gpsStatusMessage.value = "GPS ট্র্যাক করা সম্ভব হয়নি"
        }
    }

    private fun applyGpsCoordinates(lat: Double, lng: Double) {
        viewModelScope.launch {
            val nearest = PRESET_SALAT_PLACES.minByOrNull { place ->
                val dLat = place.latitude - lat
                val dLng = place.longitude - lng
                dLat * dLat + dLng * dLng
            }

            val formattedCoords = String.format(Locale.US, "%.2f°N, %.2f°E", lat, lng)
            val placeNameBn = if (nearest != null) {
                "জিপিএস: ${nearest.nameBn} ($formattedCoords)"
            } else {
                "জিপিএস ট্র্যাকার ($formattedCoords)"
            }
            val placeNameEn = if (nearest != null) "GPS: ${nearest.nameEn}" else "GPS Location"

            repository.updateSalatPlace(
                nameBn = placeNameBn,
                nameEn = placeNameEn,
                latitude = lat,
                longitude = lng,
                isGps = true
            )
            _gpsStatusMessage.value = "জিপিএস অবস্থান সফলভাবে ট্র্যাক করা হয়েছে!"
        }
    }

    val prayerStatus = combine(
        _currentDate,
        repository.hanafiAsrFlow,
        _prayerNotificationSettings,
        repository.salatConfigFlow
    ) { date, isHanafi, notifs, config ->
        val cal = Calendar.getInstance().apply { time = date }
        PrayerCalculator.calculatePrayers(
            cal = cal,
            isHanafiAsr = isHanafi,
            notificationSettings = notifs,
            latitude = config.latitude,
            longitude = config.longitude,
            locationNameBn = config.placeNameBn,
            locationNameEn = config.placeNameEn,
            isGpsLocation = config.isGpsEnabled,
            manualOffsetMinutes = config.manualOffsetMinutes
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PrayerCalculator.calculatePrayers()
    )

    // INSPIRATION (LEGACY & LIVE WISDOM)
    val dailyInspiration: StateFlow<DailyInspiration> = MutableStateFlow(InspirationData.getTodayInspiration()).asStateFlow()

    private val _wisdomState = MutableStateFlow(DailyWisdomState())
    val wisdomState: StateFlow<DailyWisdomState> = _wisdomState.asStateFlow()

    fun shuffleWisdom() {
        viewModelScope.launch {
            _wisdomState.value = _wisdomState.value.copy(isLoading = true)
            val updated = wisdomApiService.fetchWisdomBundle(shuffle = true)
            _wisdomState.value = updated
        }
    }

    // DUA VAULT
    private val _duaSearchQuery = MutableStateFlow("")
    val duaSearchQuery = _duaSearchQuery.asStateFlow()

    private val _selectedDuaCategory = MutableStateFlow("all")
    val selectedDuaCategory = _selectedDuaCategory.asStateFlow()

    val bookmarkedDuaIds = repository.getBookmarkedDuaIds().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun setDuaSearchQuery(query: String) {
        _duaSearchQuery.value = query
    }

    fun setDuaCategory(category: String) {
        _selectedDuaCategory.value = category
    }

    fun toggleBookmark(duaId: String, currentStatus: Boolean) {
        viewModelScope.launch {
            repository.toggleBookmark(duaId, currentStatus)
        }
    }

    val filteredDuas = combine(
        _duaSearchQuery,
        _selectedDuaCategory,
        bookmarkedDuaIds
    ) { query, cat, bookmarks ->
        val bookmarkSet = bookmarks.toSet()
        DuaVaultData.duas.map { dua ->
            dua.copy(isBookmarked = bookmarkSet.contains(dua.id))
        }.filter { dua ->
            val matchesCategory = if (cat == "all") true else if (cat == "bookmarked") dua.isBookmarked else dua.categoryId == cat
            val matchesQuery = query.isBlank() ||
                    dua.titleBn.contains(query, ignoreCase = true) ||
                    dua.meaningBn.contains(query, ignoreCase = true) ||
                    dua.pronunciationBn.contains(query, ignoreCase = true) ||
                    dua.arabicText.contains(query)
            matchesCategory && matchesQuery
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DuaVaultData.duas)

    // 24H ROUTINE
    private val _routineTimeSlotFilter = MutableStateFlow("all")
    val routineTimeSlotFilter = _routineTimeSlotFilter.asStateFlow()

    private val _routineSearchQuery = MutableStateFlow("")
    val routineSearchQuery = _routineSearchQuery.asStateFlow()

    fun setRoutineTimeSlotFilter(slot: String) {
        _routineTimeSlotFilter.value = slot
    }

    fun setRoutineSearchQuery(query: String) {
        _routineSearchQuery.value = query
    }

    val filteredRoutineList = combine(
        _routineTimeSlotFilter,
        _routineSearchQuery
    ) { slot, query ->
        RoutineData.routineList.filter { item ->
            val matchesSlot = when (slot) {
                "all" -> true
                "top10" -> item.isTopPriority
                else -> item.timeSlotId == slot
            }
            val matchesQuery = query.isBlank() ||
                    item.titleBn.contains(query, ignoreCase = true) ||
                    item.subtitleBn.contains(query, ignoreCase = true) ||
                    item.descriptionBn.contains(query, ignoreCase = true) ||
                    item.virtuesRewardBn.contains(query, ignoreCase = true)
            matchesSlot && matchesQuery
        }.sortedBy { it.priorityRank }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), RoutineData.routineList)

    // CHECKLIST
    val todayChecklistRecord = repository.getTodayChecklistRecord().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val allChecklistRecords = repository.getAllChecklistRecords().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val dailyStreak = allChecklistRecords.combine(todayChecklistRecord) { all, today ->
        val records = if (today != null) {
            val list = all.filter { it.date != today.date }.toMutableList()
            list.add(today)
            list
        } else {
            all
        }
        repository.calculateStreak(records)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val habitsList = HabitData.dailyHabits

    fun toggleHabit(habitId: String) {
        viewModelScope.launch {
            repository.toggleHabit(habitId, habitsList.size)
        }
    }

    // TASBIH COUNTER
    private val _tasbihState = MutableStateFlow(TasbihState())
    val tasbihState = _tasbihState.asStateFlow()

    fun incrementTasbih() {
        val current = _tasbihState.value
        val newCount = current.count + 1
        val newTotal = current.totalCount + 1

        val goalReached = current.target > 0 && newCount >= current.target
        if (goalReached) {
            VibrationHelper.vibrateGoalReached(getApplication())
            // Loop back to 0 or continue
            _tasbihState.value = current.copy(
                count = 0,
                totalCount = newTotal
            )
        } else {
            VibrationHelper.vibrate(getApplication(), 40)
            _tasbihState.value = current.copy(
                count = newCount,
                totalCount = newTotal
            )
        }
    }

    fun resetTasbih() {
        _tasbihState.value = _tasbihState.value.copy(count = 0)
    }

    fun setTasbihTarget(target: Int) {
        _tasbihState.value = _tasbihState.value.copy(target = target, count = 0)
    }

    fun selectTasbihDhikr(dhikr: String) {
        _tasbihState.value = _tasbihState.value.copy(currentDhikr = dhikr, count = 0)
    }

    // ASMAUL HUSNA
    private val _asmaulHusnaSearch = MutableStateFlow("")
    val asmaulHusnaSearch = _asmaulHusnaSearch.asStateFlow()

    fun setAsmaulHusnaSearch(query: String) {
        _asmaulHusnaSearch.value = query
    }

    val filteredAsmaulHusna = _asmaulHusnaSearch.combine(MutableStateFlow(AsmaulHusnaData.names)) { query, list ->
        if (query.isBlank()) list
        else list.filter {
            it.pronunciationBn.contains(query, ignoreCase = true) ||
            it.meaningBn.contains(query, ignoreCase = true) ||
            it.arabicName.contains(query) ||
            it.number.toString() == query
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AsmaulHusnaData.names)

    // DUROOD & ISTIGHFAR
    val duroodList = DuroodData.items

    // HEALTH DUAS
    private val _healthCategoryFilter = MutableStateFlow("সকল নিরাময়")
    val healthCategoryFilter = _healthCategoryFilter.asStateFlow()

    fun setHealthCategoryFilter(category: String) {
        _healthCategoryFilter.value = category
    }

    val filteredHealthDuas = _healthCategoryFilter.combine(MutableStateFlow(HealthDuaData.items)) { cat, list ->
        if (cat == "সকল নিরাময়") list
        else list.filter { it.ailmentCategoryBn == cat }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HealthDuaData.items)

    // SCRATCHPAD
    val scratchpadNotes = repository.getAllNotes().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    fun saveScratchpadNote(id: Int, title: String, content: String, category: String) {
        viewModelScope.launch {
            repository.saveNote(id, title, content, category)
        }
    }

    fun deleteScratchpadNote(id: Int) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    // SETTINGS & THEMES
    val themeStyle = repository.themeStyleFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ThemeStyle.EMERALD_JANNAH
    )

    val themeMode = repository.themeModeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ThemeMode.SYSTEM
    )

    val fontScale = repository.fontScaleFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FontSizeScale.NORMAL
    )

    val isHanafiAsr = repository.hanafiAsrFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        true
    )

    fun setThemeStyle(style: ThemeStyle) {
        viewModelScope.launch { repository.setThemeStyle(style) }
    }

    fun setThemeMode(mode: ThemeMode) {
        viewModelScope.launch { repository.setThemeMode(mode) }
    }

    fun setFontScale(scale: FontSizeScale) {
        viewModelScope.launch { repository.setFontScale(scale) }
    }

    fun setHanafiAsr(isHanafi: Boolean) {
        viewModelScope.launch { repository.setHanafiAsr(isHanafi) }
    }

    val englishFont = repository.englishFontFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        EnglishFont.ROBOTO
    )

    val banglaFont = repository.banglaFontFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BanglaFont.NOTO_SANS_BENGALI
    )

    val primaryFontPreference = repository.primaryFontPreferenceFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PrimaryFontPreference.BANGLA_PRIMARY
    )

    val banglaFontWeight = repository.banglaFontWeightFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        BanglaFontWeight.NORMAL
    )

    fun setEnglishFont(font: EnglishFont) {
        viewModelScope.launch { repository.setEnglishFont(font) }
    }

    fun setBanglaFont(font: BanglaFont) {
        viewModelScope.launch { repository.setBanglaFont(font) }
    }

    fun setPrimaryFontPreference(pref: PrimaryFontPreference) {
        viewModelScope.launch { repository.setPrimaryFontPreference(pref) }
    }

    fun setBanglaFontWeight(weight: BanglaFontWeight) {
        viewModelScope.launch { repository.setBanglaFontWeight(weight) }
    }

    private val _isFontMenuOpen = MutableStateFlow(false)
    val isFontMenuOpen = _isFontMenuOpen.asStateFlow()

    fun openFontMenu() {
        _isFontMenuOpen.value = true
    }

    fun closeFontMenu() {
        _isFontMenuOpen.value = false
    }

    private val _isThemeModalOpen = MutableStateFlow(false)
    val isThemeModalOpen = _isThemeModalOpen.asStateFlow()

    fun openThemeModal() {
        _isThemeModalOpen.value = true
    }

    fun closeThemeModal() {
        _isThemeModalOpen.value = false
    }

    fun selectRandomTheme() {
        val allThemes = ThemeStyle.entries.filter { it != ThemeStyle.EMERALD_JANNAH }
        val random = allThemes.random()
        setThemeStyle(random)
    }

    // IN-APP PUSH UPDATE & GITHUB RELEASES ENGINE
    private val _updateAlertMessage = MutableStateFlow<String?>(null)
    val updateAlertMessage = _updateAlertMessage.asStateFlow()

    private val _latestReleaseInfo = MutableStateFlow<GitHubReleaseInfo?>(null)
    val latestReleaseInfo = _latestReleaseInfo.asStateFlow()

    fun checkForAppUpdates() {
        viewModelScope.launch {
            _updateAlertMessage.value = "গিটহাব (GitHub) রিলিজ ও কনটেন্ট সার্ভার যাচাই করা হচ্ছে..."
            val result = gitHubUpdateManager.checkLatestRelease()
            if (result.isSuccess) {
                val release = result.getOrNull()
                _latestReleaseInfo.value = release
                if (release != null && release.hasNewerVersion) {
                    _updateAlertMessage.value = "গিটহাবে নতুন আপডেট পাওয়া গেছে (${release.tagName})!\n${release.versionName}\n\nনতুন পরিবর্তন:\n${release.releaseNotes.take(160)}..."
                } else {
                    _updateAlertMessage.value = "আপনার অ্যাপটি সর্বশেষ সংস্করণে (v1.0.0) আপডেট করা আছে। কোনো নতুন আপডেট নেই।"
                }
            } else {
                delay(800)
                _updateAlertMessage.value = "দা'ওয়াহ টু জান্নাহ সংস্করণে কোনো নতুন অধ্যায় আপডেট এসেছে কিনা পরীক্ষা সম্পন্ন হয়েছে। আপনার বর্তমান ভার্সন (v1.0.0) সর্বশেষ হালনাগাদ করা এবং সকল আমল অফলাইনে প্রস্তুত।"
            }
        }
    }

    fun dismissUpdateAlert() {
        _updateAlertMessage.value = null
    }

    // TICKER COROUTINE FOR REALTIME CLOCK & COUNTDOWN
    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _currentDate.value = Date()
            }
        }

        // Live API initial load
        viewModelScope.launch {
            try {
                val liveBundle = wisdomApiService.fetchWisdomBundle(shuffle = false)
                _wisdomState.value = liveBundle
            } catch (e: Exception) {
                // Keep default state
            }
        }
    }
}
