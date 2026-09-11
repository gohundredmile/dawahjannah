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
import com.example.data.datasource.IslamicLifeData
import com.example.data.datasource.RoutineData
import com.example.data.datasource.TasbihPresetsData
import com.example.data.datasource.WisdomApiService
import com.example.data.remote.GitHubReleaseInfo
import com.example.data.remote.GitHubUpdateManager
import com.example.data.remote.RemoteContentBundle
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
import com.example.data.model.IslamicLifeCardItem
import com.example.data.model.IslamicLifeSection
import com.example.data.model.PRESET_SALAT_PLACES
import com.example.data.model.PrimaryFontPreference
import com.example.data.model.RoutineItem
import com.example.data.model.SalatConfiguration
import com.example.data.model.SalatPlaceInfo
import com.example.data.model.TasbihDhikrItem
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle
import com.example.data.repository.AppRepository
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
import com.example.util.VibrationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class AnnouncementData(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val active: Boolean = true
)

enum class AppTab(val index: Int, val titleBn: String) {
    HOME(0, "হোম"),
    DUA(1, "মাসনুন দোয়া"),
    ROUTINE(2, "২৪ঘণ্টা আমল"),
    CHECKLIST(3, "চেকলিস্ট"),
    MORE(4, "ইসলামী জীবন")
}

enum class MoreSubScreen(val titleBn: String) {
    MAIN("ইসলামী জীবন"),
    SURAH_BAQARAH_LAST_2("সুরা বাকারাহ'র শেষ ২ আয়াত"),
    DUROOD_AMOL("দুরুদ শরীফের আমল"),
    NAMES_OF_ALLAH("আসমাউল হুসনা (আল্লাহ্‌র ৯৯টি নাম) বাংলা অর্থ সহ ফজিলত"),
    TASBIH("ডিজিটাল তাসবীহ"),
    DUROOD_ISTIGHFAR("দরূদ ও ইস্তিগফার"),
    HEALTH_DUAS("রোগ নিরাময় ও আশ্রয়"),
    SCRATCHPAD("ব্যক্তিগত দোয়া জার্নাল"),
    SETTINGS("সেটিংস ও অ্যাপ থিম"),
    AYAT_DETECTOR_SOLVER("আয়াত ও হাদীস শুদ্ধিকরণ (Detector & Solver)"),
    ISLAMIC_LIFE_SECTION_DETAIL("ইসলামী জীবন অধ্যায়")
}

data class TasbihState(
    val currentDhikr: String = "সুবহানাল্লাহ (سُبْحَانَ اللَّهِ)",
    val count: Int = 0,
    val target: Int = 33, // 33, 100, or 0 (Unlimited)
    val totalCount: Int = 0,
    val selectedItem: TasbihDhikrItem? = TasbihPresetsData.items.firstOrNull()
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

    private val _selectedIslamicSection = MutableStateFlow<IslamicLifeSection?>(null)
    val selectedIslamicSection: StateFlow<IslamicLifeSection?> = _selectedIslamicSection.asStateFlow()

    private var previousTabBeforeSettings: AppTab? = null

    fun selectTab(tab: AppTab) {
        _currentTab.value = tab
    }

    fun navigateToMoreSubScreen(sub: MoreSubScreen) {
        _moreSubScreen.value = sub
    }

    fun openIslamicLifeSection(section: IslamicLifeSection) {
        _selectedIslamicSection.value = section
        _moreSubScreen.value = MoreSubScreen.ISLAMIC_LIFE_SECTION_DETAIL
    }

    fun openSettings(fromTab: AppTab = AppTab.HOME) {
        previousTabBeforeSettings = fromTab
        _moreSubScreen.value = MoreSubScreen.SETTINGS
        _currentTab.value = AppTab.MORE
    }

    fun navigateBackToMore() {
        if (_moreSubScreen.value == MoreSubScreen.SETTINGS && previousTabBeforeSettings != null) {
            val returnTab = previousTabBeforeSettings ?: AppTab.HOME
            previousTabBeforeSettings = null
            _moreSubScreen.value = MoreSubScreen.MAIN
            _currentTab.value = returnTab
        } else {
            _selectedIslamicSection.value = null
            _moreSubScreen.value = MoreSubScreen.MAIN
        }
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

    private val _wisdomState = MutableStateFlow(wisdomApiService.getTodayWisdom())
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

    private val _downloadedDuas = MutableStateFlow<List<DuaItem>>(emptyList())
    val downloadedDuas = _downloadedDuas.asStateFlow()

    private val _dynamicCategories = MutableStateFlow<List<DuaVaultData.Category>>(DuaVaultData.categories)
    val allDuaCategories = _dynamicCategories.asStateFlow()

    private val _islamicLifeSections = MutableStateFlow<List<IslamicLifeSection>>(IslamicLifeData.sections)
    val islamicLifeSections: StateFlow<List<IslamicLifeSection>> = _islamicLifeSections.asStateFlow()

    fun getIslamicLifeSection(id: String): IslamicLifeSection? {
        return _islamicLifeSections.value.find { it.id == id } ?: IslamicLifeData.sections.find { it.id == id }
    }

    val filteredDuas = combine(
        _duaSearchQuery,
        _selectedDuaCategory,
        bookmarkedDuaIds,
        _downloadedDuas
    ) { query, cat, bookmarks, remoteDuas ->
        val bookmarkSet = bookmarks.toSet()
        val allDuasCombined = remoteDuas + DuaVaultData.duas
        allDuasCombined.map { dua ->
            dua.copy(isBookmarked = bookmarkSet.contains(dua.id))
        }.filter { dua ->
            val matchesCategory = if (cat == "all") true 
                else if (cat == "bookmarked") dua.isBookmarked 
                else dua.categoryId == cat || dua.categoryNameBn == cat
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
                    item.virtuesRewardBn.contains(query, ignoreCase = true) ||
                    item.arabicText.contains(query) ||
                    item.tagBn.contains(query, ignoreCase = true) ||
                    item.reference.contains(query, ignoreCase = true)
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
        val matched = TasbihPresetsData.items.find { 
            it.bengaliName == dhikr || "${it.bengaliName} (${it.arabicText})" == dhikr 
        }
        _tasbihState.value = _tasbihState.value.copy(
            currentDhikr = dhikr,
            count = 0,
            selectedItem = matched ?: _tasbihState.value.selectedItem
        )
    }

    fun selectTasbihItem(item: TasbihDhikrItem) {
        val displayTitle = if (item.arabicText.isNotBlank()) "${item.bengaliName} (${item.arabicText})" else item.bengaliName
        _tasbihState.value = _tasbihState.value.copy(
            currentDhikr = displayTitle,
            count = 0,
            selectedItem = item,
            target = item.recommendedCount
        )
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

    private val _isDownloadingUpdate = MutableStateFlow(false)
    val isDownloadingUpdate = _isDownloadingUpdate.asStateFlow()

    private val _activeAnnouncement = MutableStateFlow<AnnouncementData?>(null)
    val activeAnnouncement = _activeAnnouncement.asStateFlow()

    val appliedContentVersion: String
        get() = gitHubUpdateManager.appliedContentVersion

    fun checkForAppUpdates() {
        viewModelScope.launch {
            _updateAlertMessage.value = "গিটহাব (GitHub) রিলিজ ও app-updates.json যাচাই করা হচ্ছে..."
            val result = gitHubUpdateManager.checkLatestRelease()
            if (result.isSuccess) {
                val release = result.getOrNull()
                _latestReleaseInfo.value = release
                if (release != null && release.hasNewerVersion) {
                    val ann = if (!release.announcement.isNullOrBlank()) "\n\nঘোষণা: ${release.announcement}" else ""
                    val source = if (release.isFromAppUpdatesJson) " (app-updates.json থেকে)" else " (GitHub Releases থেকে)"
                    _updateAlertMessage.value = "গিটহাবে নতুন সংস্করণ পাওয়া গেছে (${release.tagName})$source!\n${release.versionName}\n\nনতুন পরিবর্তন:\n${release.releaseNotes.take(300)}$ann\n\n'অনলাইন কনটেন্ট সিঙ্ক' বাটনে চাপলে সমস্ত নতুন দো'আ ও আমল সরাসরি অ্যাপে আপডেট হবে।"
                } else if (release != null) {
                    val ann = if (!release.announcement.isNullOrBlank()) "\n\nঘোষণা/বার্তা:\n${release.announcement}" else ""
                    val source = if (release.isFromAppUpdatesJson) "app-updates.json" else "GitHub Releases"
                    _updateAlertMessage.value = "গিটহাব সিঙ্ক স্ট্যাটাস ($source):\nআপনার অ্যাপ ও কনটেন্ট সম্পূর্ণ হালনাগাদ রয়েছে (v${gitHubUpdateManager.appliedContentVersion} - কোড: ${gitHubUpdateManager.appliedVersionCode})। কোনো নতুন আপডেট বাকি নেই।$ann"
                } else {
                    _updateAlertMessage.value = "আপনার অ্যাপটি সর্বশেষ সংস্করণে (v${gitHubUpdateManager.appliedContentVersion}) আপডেট করা আছে।"
                }
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "অজ্ঞাত ত্রুটি"
                val owner = gitHubUpdateManager.repoOwner
                val repo = gitHubUpdateManager.repoName
                _updateAlertMessage.value = "গিটহাব সিঙ্ক স্ট্যাটাস ($owner/$repo):\n$errorMsg\n\nপ্রজেক্টের রুট ডিরেক্টরিতে 'app-updates.json' প্রস্তুত রয়েছে। এটি গিটহাবে পুশ করলেই ওভার-দ্য-এয়ার সিঙ্ক স্বয়ংক্রিয়ভাবে সক্রিয় হবে।"
            }
        }
    }

    /**
     * Downloads the real APK for the new release via Android DownloadManager or browser
     */
    fun downloadNewApkVersion() {
        val release = _latestReleaseInfo.value
        val url = release?.downloadUrl
            ?: "https://github.com/${gitHubUpdateManager.repoOwner}/${gitHubUpdateManager.repoName}/releases/latest"
        val versionName = release?.tagName ?: "latest"

        val result = gitHubUpdateManager.downloadApk(url, versionName)
        if (result.isSuccess) {
            val isDirectApk = url.endsWith(".apk", ignoreCase = true) || url.contains("/download/")
            _updateAlertMessage.value = if (isDirectApk) {
                "দা'ওয়াহ টু জান্নাহ ($versionName) APK ডাউনলোড শুরু হয়েছে!\n\n" +
                "ডাউনলোড নোটিফিকেশন বার থেকে প্রগ্রেস দেখতে পারবেন। ডাউনলোড শেষ হলে ফাইলটিতে ট্যাপ করে নতুন সংস্করণ ইনস্টল করুন।"
            } else {
                "গিটহাব অফিসিয়াল রিলিজ পেজ ব্রাউজারে খোলা হয়েছে:\n$url\n\n" +
                "সেখান থেকে 'Assets' সেকশন থেকে সর্বশেষ APK ফাইলটি ডাউনলোড করে ফোনে ইনস্টল করে নিন।"
            }
        } else {
            _updateAlertMessage.value = "APK ডাউনলোড লিঙ্ক খুলতে সমস্যা হয়েছে: ${result.exceptionOrNull()?.message}"
        }
    }

    /**
     * Downloads and applies updates directly in-app to the respective places (Dua Vault, Islamic Life, Announcements)
     * over-the-air from GitHub, strictly without fake notices.
     */
    fun downloadAndApplyInAppUpdate(isLocalPreview: Boolean = false) {
        viewModelScope.launch {
            _isDownloadingUpdate.value = true
            _updateAlertMessage.value = if (isLocalPreview) {
                "লোকাল প্যাকেজের app-updates.json লোড করা হচ্ছে..."
            } else {
                "গিটহাব থেকে সরাসরি ওভার-দ্য-এয়ার কনটেন্ট ডাউনলোড করা হচ্ছে... অনুগ্রহ করে অপেক্ষা করুন।"
            }
            val result = gitHubUpdateManager.downloadAndApplyContentUpdates(allowLocalAssetFallback = isLocalPreview)
            _isDownloadingUpdate.value = false
            if (result.isSuccess) {
                val bundle = result.getOrThrow()
                applyContentBundleToApp(bundle, notifyUser = true, isLocalPreview = isLocalPreview)
                _latestReleaseInfo.value = _latestReleaseInfo.value?.copy(hasNewerVersion = false)
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "অজ্ঞাত ত্রুটি"
                _updateAlertMessage.value = "কনটেন্ট ডাউনলোড সম্পন্ন করা যায়নি:\n\n$errorMsg"
            }
        }
    }

    /**
     * Forces clearing the persisted downloaded cache, re-reads fresh bundled updates,
     * and triggers a fresh sync check with cache-busting.
     */
    fun forceRefreshContentUpdates() {
        viewModelScope.launch {
            _isDownloadingUpdate.value = true
            val refreshed = gitHubUpdateManager.forceReloadAndResync()
            if (refreshed != null) {
                applyContentBundleToApp(refreshed, notifyUser = false)
            }
            val remoteResult = gitHubUpdateManager.downloadAndApplyContentUpdates(allowLocalAssetFallback = true)
            _isDownloadingUpdate.value = false
            if (remoteResult.isSuccess) {
                val bundle = remoteResult.getOrThrow()
                applyContentBundleToApp(bundle, notifyUser = true, isLocalPreview = false)
                _latestReleaseInfo.value = _latestReleaseInfo.value?.copy(hasNewerVersion = false)
            } else {
                _updateAlertMessage.value = "ক্যাশ রিফ্রেশ সম্পন্ন হয়েছে!\nসর্বশেষ বান্ডেল v${gitHubUpdateManager.appliedContentVersion} (কোড: ${gitHubUpdateManager.appliedVersionCode}) সক্রিয় করা হয়েছে (${refreshed?.extraDuas?.size ?: 0}টি দো'আ অন্তর্ভুক্ত)।"
            }
        }
    }

    private fun applyContentBundleToApp(bundle: RemoteContentBundle, notifyUser: Boolean, isLocalPreview: Boolean = false) {
        val convertedDuas = bundle.extraDuas.map { remote ->
            val catName = if (remote.category.isNotBlank()) remote.category else "অন্যান্য দোয়া"
            val safeCatId = "cat_remote_" + kotlin.math.abs(remote.category.hashCode())
            DuaItem(
                id = remote.id,
                categoryId = safeCatId,
                categoryNameBn = catName,
                titleBn = remote.titleBn,
                arabicText = remote.arabic,
                pronunciationBn = remote.pronunciationBn,
                meaningBn = remote.meaningBn,
                virtuesBn = if (remote.virtuesBn.isNotBlank()) remote.virtuesBn else "ইন-অ্যাপ ওটিএ কনটেন্ট আপডেট থেকে যুক্ত।",
                reference = remote.reference,
                isBookmarked = false
            )
        }
        _downloadedDuas.value = convertedDuas

        // Extract any new categories and append to DuaVaultData.categories
        val existingCatNames = DuaVaultData.categories.map { it.nameBn }.toSet()
        val newCategories = convertedDuas
            .map { it.categoryNameBn }
            .distinct()
            .filterNot { existingCatNames.contains(it) }
            .map { name ->
                val safeCatId = "cat_remote_" + kotlin.math.abs(name.hashCode())
                DuaVaultData.Category(id = safeCatId, nameBn = name)
            }
        _dynamicCategories.value = DuaVaultData.categories + newCategories

        // Merge extra duas into IslamicLife sections so MoreScreen immediately reflects new content
        val baseSections = IslamicLifeData.sections.map { it.copy(items = it.items.toMutableList()) }.toMutableList()
        val extraItemsByTargetSection = mutableMapOf<String, MutableList<IslamicLifeCardItem>>()
        val unassignedCategoryItems = mutableMapOf<String, MutableList<IslamicLifeCardItem>>()

        for (remote in bundle.extraDuas) {
            val cardItem = IslamicLifeCardItem(
                id = remote.id,
                serialNumberBn = if (remote.serialNumberBn.isNotBlank()) remote.serialNumberBn else "",
                titleBn = remote.titleBn,
                repetitionOrTimeBn = remote.repetitionOrTimeBn,
                arabicText = remote.arabic,
                pronunciationBn = remote.pronunciationBn,
                meaningBn = remote.meaningBn,
                fojilotBn = if (remote.virtuesBn.isNotBlank()) remote.virtuesBn else "ইন-অ্যাপ ওটিএ কনটেন্ট আপডেট থেকে যুক্ত।",
                detailsBn = remote.detailsBn,
                referenceBn = remote.reference
            )

            val targetSecId = when {
                remote.targetSectionId.isNotBlank() -> remote.targetSectionId
                remote.category.contains("সালাম", ignoreCase = true) || remote.category.contains("salam", ignoreCase = true) -> "salam_before"
                remote.category.contains("ফরজ", ignoreCase = true) || remote.category.contains("farz", ignoreCase = true) || remote.category.contains("ফরয", ignoreCase = true) -> "farz_after"
                remote.category.contains("হাজত", ignoreCase = true) || remote.category.contains("hajat", ignoreCase = true) -> "salatul_hajat"
                remote.category.contains("স্বাস্থ্য", ignoreCase = true) || remote.category.contains("রোগ", ignoreCase = true) || remote.category.contains("শিফা", ignoreCase = true) -> "physical_health_dua"
                remote.category.contains("কবুল", ignoreCase = true) || remote.category.contains("সময়", ignoreCase = true) -> "dua_acceptance_times"
                remote.category.contains("তাসবীহ", ignoreCase = true) || remote.category.contains("তাহলীল", ignoreCase = true) || remote.category.contains("জিকির", ignoreCase = true) -> "daily_dhikr_tasbih_tahlil"
                remote.category.contains("ইমরান", ignoreCase = true) -> "surah_ali_imran_26_27"
                remote.category.contains("বাকারা", ignoreCase = true) -> "surah_baqarah_last_2"
                remote.category.contains("ফজর", ignoreCase = true) || remote.category.contains("মাগরিব", ignoreCase = true) -> "fajr_maghrib"
                else -> ""
            }

            if (targetSecId.isNotBlank()) {
                extraItemsByTargetSection.getOrPut(targetSecId) { mutableListOf() }.add(cardItem)
            } else {
                val catName = if (remote.category.isNotBlank()) remote.category else "নতুন কনটেন্ট আমল"
                unassignedCategoryItems.getOrPut(catName) { mutableListOf() }.add(cardItem)
            }
        }

        // Apply to matching sections in baseSections
        val updatedSections = baseSections.map { section ->
            val addedForThis = extraItemsByTargetSection[section.id]
            if (!addedForThis.isNullOrEmpty()) {
                val mergedItems = section.items.toMutableList()
                for (newItem in addedForThis) {
                    val existingIndex = mergedItems.indexOfFirst { it.id == newItem.id }
                    if (existingIndex >= 0) {
                        mergedItems[existingIndex] = newItem
                    } else {
                        mergedItems.add(newItem)
                    }
                }
                section.copy(items = mergedItems)
            } else {
                section
            }
        }.toMutableList()

        // For unassigned categories, create dynamic sections so they are accessible in MoreScreen
        for ((catName, itemsList) in unassignedCategoryItems) {
            val dynamicSecId = "sec_ota_" + kotlin.math.abs(catName.hashCode())
            val existingSecIndex = updatedSections.indexOfFirst { it.id == dynamicSecId }
            if (existingSecIndex >= 0) {
                updatedSections[existingSecIndex] = updatedSections[existingSecIndex].copy(items = itemsList)
            } else {
                updatedSections.add(
                    IslamicLifeSection(
                        id = dynamicSecId,
                        titleBn = catName,
                        subtitleBn = "ওভার-দ্য-এয়ার কনটেন্ট আপডেট থেকে সংকলিত (${itemsList.size}টি আমল)",
                        items = itemsList
                    )
                )
            }
        }

        _islamicLifeSections.value = updatedSections

        // Refresh currently opened section if user is actively viewing it
        _selectedIslamicSection.value?.let { current ->
            val fresh = updatedSections.find { it.id == current.id }
            if (fresh != null) {
                _selectedIslamicSection.value = fresh
            }
        }

        // Handle announcement
        if (!bundle.announcement.isNullOrBlank()) {
            val parts = bundle.announcement.split("\n", limit = 2)
            val title = parts.getOrNull(0)?.trim() ?: "বিশেষ আপডেট"
            val message = parts.getOrNull(1)?.trim() ?: parts.getOrNull(0)?.trim() ?: ""
            _activeAnnouncement.value = AnnouncementData(
                id = "announcement_${bundle.version}",
                title = title,
                message = message,
                active = true
            )
        }

        if (notifyUser) {
            val sourceText = bundle.sourceDescription
            _updateAlertMessage.value = "আলহামদুলিল্লাহ! ($sourceText) থেকে সংস্করণ ${bundle.tagName} সফলভাবে প্রয়োগ করা হয়েছে!\n\n" +
                "• কনটেন্ট ভার্সন: v${bundle.versionName} (কোড: ${bundle.version})\n" +
                "• ওটিএ দো'আ ও আমল: ${convertedDuas.size}টি সক্রিয়\n" +
                "• ইসলামী জীবন অধ্যায়: ${updatedSections.size}টি বিভাগ হালনাগাদ\n" +
                if (!bundle.announcement.isNullOrBlank()) "• সক্রিয় ঘোষণা: ${bundle.announcement}\n" else "" +
                "\nঅ্যাপের সমস্ত কনটেন্ট ও নতুন আমল সফলভাবে তাজা করা হয়েছে।"
        }
    }

    fun dismissAnnouncement() {
        _activeAnnouncement.value = null
    }

    fun testLocalAppUpdatesSync() {
        downloadAndApplyInAppUpdate(isLocalPreview = true)
    }

    fun updateGitHubRepo(owner: String, repo: String) {
        gitHubUpdateManager.updateRepoConfig(owner, repo)
    }

    fun dismissUpdateAlert() {
        _updateAlertMessage.value = null
    }

    // TICKER COROUTINE FOR REALTIME CLOCK & COUNTDOWN & AUTOMATIC NEW DAY ROTATION
    private var lastRecordedDayOfYear: Int = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

    init {
        // Load persisted or bundled in-app content updates into the app state
        val initialUpdates = gitHubUpdateManager.getPersistedDownloadedUpdates()
        if (initialUpdates != null) {
            applyContentBundleToApp(initialUpdates, notifyUser = false)
        }

        // Automatic non-blocking OTA sync on startup
        viewModelScope.launch(Dispatchers.IO) {
            delay(1500)
            try {
                val releaseCheck = gitHubUpdateManager.checkLatestRelease()
                if (releaseCheck.isSuccess) {
                    val info = releaseCheck.getOrThrow()
                    withContext(Dispatchers.Main) {
                        _latestReleaseInfo.value = info
                    }
                    if (info.hasNewerVersion && info.isContentUpdateOnly) {
                        val dlResult = gitHubUpdateManager.downloadAndApplyContentUpdates(allowLocalAssetFallback = true)
                        if (dlResult.isSuccess) {
                            val bundle = dlResult.getOrThrow()
                            withContext(Dispatchers.Main) {
                                applyContentBundleToApp(bundle, notifyUser = false)
                            }
                        }
                    }
                }
            } catch (_: Exception) {
            }
        }

        viewModelScope.launch {
            while (true) {
                delay(1000)
                val now = Date()
                _currentDate.value = now

                val currentDayOfYear = Calendar.getInstance().apply { time = now }.get(Calendar.DAY_OF_YEAR)
                if (currentDayOfYear != lastRecordedDayOfYear) {
                    lastRecordedDayOfYear = currentDayOfYear
                    // Forcefully/automatically rotate to new day's Ayat, Hadith, Quote & Inspiration
                    _wisdomState.value = wisdomApiService.getTodayWisdom()
                    viewModelScope.launch {
                        try {
                            val liveBundle = wisdomApiService.fetchWisdomBundle(shuffle = false)
                            _wisdomState.value = liveBundle
                        } catch (e: Exception) {
                            // Keep today's offline wisdom
                        }
                    }
                }
            }
        }

        // Initial live API load for today's rotated Ayat, Hadith, Quote
        viewModelScope.launch {
            try {
                val liveBundle = wisdomApiService.fetchWisdomBundle(shuffle = false)
                _wisdomState.value = liveBundle
            } catch (e: Exception) {
                // Keep today's offline wisdom
            }
        }
    }
}
