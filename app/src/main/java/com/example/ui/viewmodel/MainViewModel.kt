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
import com.example.data.local.entity.BookmarkEntity
import com.example.data.local.entity.ChecklistRecord
import com.example.data.local.entity.ScratchpadNote
import com.example.data.model.AllahNameItem
import com.example.data.model.AsrJuristicMethod
import com.example.data.model.AuroraWallpaperConfig
import com.example.data.model.AuroraWavePreset
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.DailyInspiration
import com.example.data.model.DailyWisdomState
import com.example.data.model.DuaItem
import com.example.data.model.DuroodAmolItem
import com.example.data.model.DuroodItem
import com.example.data.model.EnglishFont
import com.example.data.model.FlipClockFont
import com.example.data.model.FontSizeScale
import com.example.data.model.HealthDuaItem
import com.example.data.model.HighLatitudeRule
import com.example.data.model.IslamicLifeCardItem
import com.example.data.model.IslamicLifeSection
import com.example.data.model.PRESET_SALAT_PLACES
import com.example.data.model.PrayerCalculationMethod
import com.example.data.model.PrimaryFontPreference
import com.example.data.model.RoutineItem
import com.example.data.model.SalatConfiguration
import com.example.data.model.SalatPlaceInfo
import com.example.data.model.ScreenEffectMode
import com.example.data.model.TasbihDhikrItem
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle
import com.example.data.repository.AppRepository
import com.example.data.repository.QuranRepository
import com.example.ui.components.ModalSectionTab
import com.example.util.CalendarHelper
import com.example.util.QuranAudioManager
import com.example.util.ExcludedIslamicLifeTopics
import com.example.util.PrayerCalculator
import com.example.util.VibrationHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
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
    DUA(1, "মাসনুন\u00A0দোয়া"),
    ROUTINE(2, "২৪ঘণ্টা\u00A0আমল"),
    TASBIH(3, "তাসবিহ"),
    FAVORITE(4, "ফেভারিট"),
    TOOLS(5, "টুলস"),
    MORE(6, "ইসলামী\u00A0জীবন")
}

enum class ToolsSubScreen(val titleBn: String) {
    MAIN("টুলস"),
    DUA_BY_SITUATION("Dua by Situation (অনুভূতি ও পরিস্থিতি অনুযায়ী দু'আ)"),
    PERSONAL_DUA_BUILDER("Personal Dua Builder (ব্যক্তিগত দো'আ আর্কিটেক্ট)"),
    ASK_BEFORE_YOU_ACT("Ask Before You Act (পদক্ষেপ নেওয়ার আগে জানুন)"),
    SMART_QURAN_SEARCH("Smart Quran Search (ভাবার্থভিত্তিক অনুসন্ধান)"),
    ISLAMIC_HABIT_SYSTEM("Islamic Habit System (সুন্নাহ ও অভ্যাস পদ্ধতি)"),
    RAMADAN_INTELLIGENCE("Ramadan Intelligence (রমাদান ইন্টেলিজেন্স ও পূর্ণাঙ্গ রমাদান পদ্ধতি)"),
    EXPLAIN_AYAH_CAMERA("Explain This Ayah ক্যামেরা"),
    AYAT_DETECTOR_SOLVER("আয়াত ও হাদীস শুদ্ধিকরণ ল্যাব"),
    QIBLA("ক্বিবলা কম্পাস"),
    TASBIH("ডিজিটাল তাসবীহ"),
    NAMES_OF_ALLAH("আসমাউল হুসনা"),
    MOSQUE_MODE("মসজিদ মোড (Mosque Mode)"),
    HOLY_QURAN("আল-কুরআন (অনুবাদ, তাফসীর ও তিলাওয়াত)")
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
    ISLAMIC_LIFE_SECTION_DETAIL("ইসলামী জীবন অধ্যায়"),
    QIBLA("ক্বিবলা কম্পাস (Qibla Direction)")
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
    val quranRepository = QuranRepository(application)
    val quranAudioManager = QuranAudioManager(application)

    // NAVIGATION
    private val _currentTab = MutableStateFlow(AppTab.HOME)
    val currentTab: StateFlow<AppTab> = _currentTab.asStateFlow()

    private val _targetQuranSurahNumber = MutableStateFlow<Int?>(null)
    val targetQuranSurahNumber: StateFlow<Int?> = _targetQuranSurahNumber.asStateFlow()

    private val _moreSubScreen = MutableStateFlow(MoreSubScreen.MAIN)
    val moreSubScreen: StateFlow<MoreSubScreen> = _moreSubScreen.asStateFlow()

    private val _toolsSubScreen = MutableStateFlow(ToolsSubScreen.MAIN)
    val toolsSubScreen: StateFlow<ToolsSubScreen> = _toolsSubScreen.asStateFlow()

    private val _selectedIslamicSection = MutableStateFlow<IslamicLifeSection?>(null)
    val selectedIslamicSection: StateFlow<IslamicLifeSection?> = _selectedIslamicSection.asStateFlow()

    private var previousTabBeforeSettings: AppTab? = null

    fun selectTab(tab: AppTab) {
        if (tab == AppTab.ROUTINE) {
            autoSelectCurrentRoutineTimeSlot()
        }
        _currentTab.value = tab
    }

    fun navigateToToolsSubScreen(sub: ToolsSubScreen) {
        _toolsSubScreen.value = sub
        _currentTab.value = AppTab.TOOLS
    }

    fun openDuaBySituation() {
        _toolsSubScreen.value = ToolsSubScreen.DUA_BY_SITUATION
        _currentTab.value = AppTab.TOOLS
    }

    fun openPersonalDuaBuilder() {
        _toolsSubScreen.value = ToolsSubScreen.PERSONAL_DUA_BUILDER
        _currentTab.value = AppTab.TOOLS
    }

    fun openExplainAyahCamera() {
        _toolsSubScreen.value = ToolsSubScreen.EXPLAIN_AYAH_CAMERA
        _currentTab.value = AppTab.TOOLS
    }

    fun openAskBeforeYouAct() {
        _toolsSubScreen.value = ToolsSubScreen.ASK_BEFORE_YOU_ACT
        _currentTab.value = AppTab.TOOLS
    }

    fun openIslamicHabitSystem() {
        _toolsSubScreen.value = ToolsSubScreen.ISLAMIC_HABIT_SYSTEM
        _currentTab.value = AppTab.TOOLS
    }

    fun openRamadanIntelligence() {
        _toolsSubScreen.value = ToolsSubScreen.RAMADAN_INTELLIGENCE
        _currentTab.value = AppTab.TOOLS
    }

    fun openMosqueMode() {
        _toolsSubScreen.value = ToolsSubScreen.MOSQUE_MODE
        _currentTab.value = AppTab.TOOLS
    }

    fun openHolyQuran(surahNumber: Int? = null) {
        _targetQuranSurahNumber.value = surahNumber
        _toolsSubScreen.value = ToolsSubScreen.HOLY_QURAN
        _currentTab.value = AppTab.TOOLS
    }

    fun navigateBackToTools() {
        _toolsSubScreen.value = ToolsSubScreen.MAIN
    }

    fun navigateToMoreSubScreen(sub: MoreSubScreen) {
        _moreSubScreen.value = sub
    }

    fun openIslamicLifeSection(section: IslamicLifeSection) {
        if (ExcludedIslamicLifeTopics.isExcluded(section.titleBn)) return
        _selectedIslamicSection.value = section
        _moreSubScreen.value = MoreSubScreen.ISLAMIC_LIFE_SECTION_DETAIL
    }

    fun openSettings(fromTab: AppTab = AppTab.HOME) {
        previousTabBeforeSettings = fromTab
        _moreSubScreen.value = MoreSubScreen.SETTINGS
        _currentTab.value = AppTab.MORE
    }

    fun openQibla(fromTab: AppTab = AppTab.HOME) {
        previousTabBeforeSettings = fromTab
        _moreSubScreen.value = MoreSubScreen.QIBLA
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

    fun setPrayerCalculationMethod(method: PrayerCalculationMethod) {
        viewModelScope.launch {
            repository.setCalculationMethod(method)
        }
    }

    fun setCalculationMethod(method: PrayerCalculationMethod) = setPrayerCalculationMethod(method)

    fun setAsrJuristicMethod(method: AsrJuristicMethod) {
        viewModelScope.launch {
            repository.setAsrJuristicMethod(method)
        }
    }

    fun setHighLatitudeRule(rule: HighLatitudeRule) {
        viewModelScope.launch {
            repository.setHighLatitudeRule(rule)
        }
    }

    fun resetSalatCalculationPreferences() {
        viewModelScope.launch {
            repository.resetSalatPreferences()
            _gpsStatusMessage.value = "নামাজের সময়সূচি ডিফল্ট (করাচি মানদণ্ড ও হানাফী) হিসেবে রিসেট হয়েছে"
        }
    }

    fun resetSalatPreferencesToStandard() = resetSalatCalculationPreferences()

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
        _prayerNotificationSettings,
        repository.salatConfigFlow
    ) { date, notifs, config ->
        val cal = Calendar.getInstance().apply { time = date }
        PrayerCalculator.calculatePrayers(
            cal = cal,
            isHanafiAsr = config.isHanafiAsr,
            notificationSettings = notifs,
            latitude = config.latitude,
            longitude = config.longitude,
            locationNameBn = config.placeNameBn,
            locationNameEn = config.placeNameEn,
            isGpsLocation = config.isGpsEnabled,
            manualOffsetMinutes = config.manualOffsetMinutes,
            calculationMethod = config.calculationMethod,
            asrMethod = config.asrMethod,
            highLatitudeRule = config.highLatitudeRule,
            timezoneOffsetHours = config.timezoneOffsetHours
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

    val allBookmarks: StateFlow<List<BookmarkEntity>> = repository.getAllBookmarks().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )

    val bookmarkedIds: StateFlow<Set<String>> = repository.getBookmarkedIds()
        .map { it.toSet() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptySet()
        )

    fun isBookmarked(id: String): Boolean {
        return bookmarkedIds.value.contains(id)
    }

    fun setDuaSearchQuery(query: String) {
        _duaSearchQuery.value = query
    }

    fun setDuaCategory(category: String) {
        _selectedDuaCategory.value = category
    }

    fun toggleBookmark(dua: DuaItem) {
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(dua.id)
        val entity = BookmarkEntity(
            id = dua.id,
            type = "DUA",
            titleBn = dua.titleBn,
            subtitleBn = dua.categoryNameBn,
            categoryBn = "মাসনুন দোয়া",
            arabicText = dua.arabicText,
            pronunciationBn = dua.pronunciationBn,
            meaningBn = dua.meaningBn,
            detailsBn = dua.virtuesBn,
            referenceBn = dua.reference,
            targetScreen = "DUA"
        )
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun toggleBookmark(item: IslamicLifeCardItem, sectionTitle: String = "ইসলামী জীবন") {
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(item.id)
        val entity = BookmarkEntity(
            id = item.id,
            type = "ISLAMIC_LIFE",
            titleBn = item.titleBn,
            subtitleBn = item.subtitleBn.ifBlank { item.repetitionOrTimeBn },
            categoryBn = sectionTitle,
            arabicText = item.arabicText,
            pronunciationBn = item.pronunciationBn,
            meaningBn = item.meaningBn,
            detailsBn = if (item.detailsBn.isNotBlank()) item.detailsBn else item.fojilotBn,
            referenceBn = item.referenceBn,
            targetScreen = "ISLAMIC_LIFE"
        )
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun toggleBookmark(item: HealthDuaItem) {
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(item.id)
        val entity = BookmarkEntity(
            id = item.id,
            type = "HEALTH_DUA",
            titleBn = item.titleBn,
            subtitleBn = item.ailmentCategoryBn,
            categoryBn = "রোগ নিরাময় ও শিফা",
            arabicText = item.arabicText,
            pronunciationBn = item.pronunciationBn,
            meaningBn = item.meaningBn,
            detailsBn = item.amalMethodBn,
            referenceBn = item.reference,
            targetScreen = "HEALTH_DUAS"
        )
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun toggleBookmark(item: DuroodItem, sectionTitle: String = "দরূদ ও আমল") {
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(item.id)
        val entity = BookmarkEntity(
            id = item.id,
            type = "DUROOD",
            titleBn = item.titleBn,
            subtitleBn = "দরূদ শরীফ",
            categoryBn = sectionTitle,
            arabicText = item.arabicText,
            pronunciationBn = item.pronunciationBn,
            meaningBn = item.meaningBn,
            detailsBn = item.virtuesRewardBn.ifBlank { item.backgroundStoryBn },
            referenceBn = item.reference,
            targetScreen = "DUROOD_AMOL"
        )
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun toggleBookmark(item: DuroodAmolItem) {
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(item.id)
        val entity = BookmarkEntity(
            id = item.id,
            type = "DUROOD",
            titleBn = item.titleBn,
            subtitleBn = item.serialNoBn,
            categoryBn = "দরূদ ও আমল",
            arabicText = item.arabicText,
            pronunciationBn = item.pronunciationBn,
            meaningBn = item.meaningBn,
            detailsBn = item.virtuesBn.ifBlank { item.notesBn },
            referenceBn = item.referenceBn,
            targetScreen = "DUROOD_AMOL"
        )
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun toggleBookmark(item: AllahNameItem) {
        val id = "allah_name_${item.number}"
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(id)
        val entity = BookmarkEntity(
            id = id,
            type = "ALLAH_NAME",
            titleBn = "${item.pronunciationBn} (${item.meaningBn})",
            subtitleBn = "আল্লাহ্‌র পবিত্র নাম #${CalendarHelper.toBanglaNumber(item.number)}",
            categoryBn = "আসমাউল হুসনা",
            arabicText = item.arabicName,
            pronunciationBn = item.pronunciationBn,
            meaningBn = item.meaningBn,
            detailsBn = buildString {
                if (item.fojilotBn.isNotBlank()) appendLine(item.fojilotBn)
                if (item.spiritualReflectionBn.isNotBlank()) appendLine(item.spiritualReflectionBn)
                if (item.amolBn.isNotBlank()) appendLine(item.amolBn)
            }.trim(),
            referenceBn = "তিরমিযী ও সহীহ হাদিস",
            targetScreen = "NAMES_OF_ALLAH"
        )
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun toggleBookmark(entity: BookmarkEntity) {
        val isCurrentlyBookmarked = bookmarkedIds.value.contains(entity.id)
        viewModelScope.launch {
            repository.toggleBookmark(entity, isCurrentlyBookmarked)
        }
    }

    fun removeBookmarkById(id: String) {
        viewModelScope.launch {
            repository.removeBookmark(id)
        }
    }

    fun toggleBookmark(duaId: String, currentStatus: Boolean) {
        val dua = (downloadedDuas.value + DuaVaultData.duas).find { it.id == duaId }
        if (dua != null) {
            toggleBookmark(dua)
            return
        }
        val lifeItem = _islamicLifeSections.value.flatMap { it.items }.find { it.id == duaId }
        if (lifeItem != null) {
            toggleBookmark(lifeItem)
            return
        }
        viewModelScope.launch {
            repository.toggleBookmark(duaId, currentStatus)
        }
    }

    private val _downloadedDuas = MutableStateFlow<List<DuaItem>>(emptyList())
    val downloadedDuas = _downloadedDuas.asStateFlow()

    private val _dynamicCategories = MutableStateFlow<List<DuaVaultData.Category>>(DuaVaultData.categories)
    val allDuaCategories = _dynamicCategories.asStateFlow()

    private val _islamicLifeSections = MutableStateFlow<List<IslamicLifeSection>>(
        IslamicLifeData.sections.filterNot { ExcludedIslamicLifeTopics.isExcluded(it.titleBn) }
    )
    val islamicLifeSections: StateFlow<List<IslamicLifeSection>> = _islamicLifeSections.asStateFlow()

    fun getIslamicLifeSection(id: String): IslamicLifeSection? {
        val found = _islamicLifeSections.value.find { it.id == id } ?: IslamicLifeData.sections.find { it.id == id }
        return if (found != null && !ExcludedIslamicLifeTopics.isExcluded(found.titleBn)) found else null
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

    // 24H ROUTINE & DAILY SCORECARD
    private val routinePrefs = application.getSharedPreferences("routine_checklist_prefs", Context.MODE_PRIVATE)

    private val _completedRoutineIds = MutableStateFlow<Set<String>>(emptySet())
    val completedRoutineIds: StateFlow<Set<String>> = _completedRoutineIds.asStateFlow()

    val scorecardTotalCount: Int = RoutineData.scorecardItems.size // 15 criteria

    val scorecardCompletedCount: StateFlow<Int> = _completedRoutineIds.map { set ->
        RoutineData.scorecardItems.count { it.id in set }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    private val _scorecardStreak = MutableStateFlow(0)
    val scorecardStreak: StateFlow<Int> = _scorecardStreak.asStateFlow()

    fun loadScorecardData() {
        val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val saved = routinePrefs.getStringSet("completed_ids_$todayDateStr", emptySet()) ?: emptySet()
        _completedRoutineIds.value = saved
        _scorecardStreak.value = calculateScorecardStreakFromPrefs(todayDateStr)
    }

    private fun calculateScorecardStreakFromPrefs(todayStr: String): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var streak = 0

        val todaySet = routinePrefs.getStringSet("completed_ids_$todayStr", emptySet()) ?: emptySet()
        val todayCount = RoutineData.scorecardItems.count { it.id in todaySet }
        if (todayCount > 0) {
            streak++
        }

        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        for (i in 0 until 365) {
            val dateStr = sdf.format(cal.time)
            val daySet = routinePrefs.getStringSet("completed_ids_$dateStr", emptySet()) ?: emptySet()
            val dayCount = RoutineData.scorecardItems.count { it.id in daySet }
            if (dayCount > 0) {
                streak++
                cal.add(Calendar.DAY_OF_YEAR, -1)
            } else {
                break
            }
        }
        return streak
    }

    fun toggleRoutineItem(id: String, isCompleted: Boolean) {
        val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val current = _completedRoutineIds.value.toMutableSet()
        if (isCompleted) {
            current.add(id)
        } else {
            current.remove(id)
        }
        _completedRoutineIds.value = current
        routinePrefs.edit().putStringSet("completed_ids_$todayDateStr", current).apply()
        _scorecardStreak.value = calculateScorecardStreakFromPrefs(todayDateStr)
    }

    fun toggleAllRoutineItems(ids: List<String>, isCompleted: Boolean) {
        val todayDateStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val current = _completedRoutineIds.value.toMutableSet()
        if (isCompleted) {
            current.addAll(ids)
        } else {
            current.removeAll(ids)
        }
        _completedRoutineIds.value = current
        routinePrefs.edit().putStringSet("completed_ids_$todayDateStr", current).apply()
        _scorecardStreak.value = calculateScorecardStreakFromPrefs(todayDateStr)
    }

    fun openScorecard() {
        _routineTimeSlotFilter.value = "scorecard"
        _currentTab.value = AppTab.ROUTINE
    }

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

    /**
     * Determines the appropriate 24-hour routine timeSlotId based on current time
     * and live astronomical prayer status (e.g. at 12:00 noon -> "dhuhr_waqt").
     */
    fun getCurrentRoutineTimeSlotId(cal: Calendar = Calendar.getInstance()): String {
        val totalMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)
        val prayers = prayerStatus.value.prayerList
        val fajrMin = prayers.find { it.id == "fajr" }?.timeMinutesFromMidnight ?: (4 * 60 + 30)
        val dhuhrMin = prayers.find { it.id == "dhuhr" }?.timeMinutesFromMidnight ?: (12 * 60)
        val asrMin = prayers.find { it.id == "asr" }?.timeMinutesFromMidnight ?: (16 * 60 + 15)
        val maghribMin = prayers.find { it.id == "maghrib" }?.timeMinutesFromMidnight ?: (18 * 60 + 15)
        val ishaMin = prayers.find { it.id == "isha" }?.timeMinutesFromMidnight ?: (20 * 60)

        return when {
            // Late night: Tahajjud (03:00 AM until Fajr)
            totalMinutes in (3 * 60) until fajrMin -> "last_part_night" // ১৫. তাহাজ্জুদ

            // Fajr period: Wake up, Wudu & Fajr prayer (from Fajr until Fajr + 75m / sunrise end)
            totalMinutes in fajrMin until (fajrMin + 75) -> "before_fajr" // ১. ঘুম থেকে ওঠার পর

            // Morning after Fajr: Morning adhkar (from ~Fajr+75 to 7:30 AM)
            totalMinutes in (fajrMin + 75) until (7 * 60 + 30) -> "after_fajr" // ২. ফজরের পর (সকাল)

            // Morning Quran session (7:30 AM - 9:30 AM)
            totalMinutes in (7 * 60 + 30) until (9 * 60 + 30) -> "morning_quran" // ৩. সকালের কুরআন

            // Workplace / Daily business & ethics (9:30 AM until Dhuhr preparation ~11:45 AM)
            totalMinutes in (9 * 60 + 30) until (dhuhrMin - 15) -> "daily_work" // ৪. কাজের মধ্যে আমল

            // Dhuhr prayer time (11:45 AM / 12:00 PM until 2:00 PM)
            totalMinutes in (dhuhrMin - 15) until (dhuhrMin + 120) -> "dhuhr_waqt" // ৫. যোহরের সময়

            // Afternoon study & Islamic education (2:00 PM until Asr start)
            totalMinutes in (dhuhrMin + 120) until asrMin -> "noon_education" // ৬. ইসলামিক শিক্ষা

            // Asr prayer time (Asr start until Asr + 60 mins)
            totalMinutes in asrMin until (asrMin + 60) -> "asr_waqt" // ৭. আসরের সময়

            // Evening adhkar before sunset (Asr + 60 mins until Maghrib)
            totalMinutes in (asrMin + 60) until maghribMin -> "after_asr" // ৮. সন্ধ্যার আমল

            // Maghrib prayer & family time (Maghrib until Maghrib + 60 mins)
            totalMinutes in maghribMin until (maghribMin + 60) -> "after_maghrib" // ৯. মাগরিবের পর

            // Extra Quran / preferred surahs (Maghrib + 60 mins until Isha)
            totalMinutes in (maghribMin + 60) until ishaMin -> "extra_quran" // ১০. পছন্দের সূরা

            // Isha prayer time (Isha start until Isha + 90 mins / ~9:30-10:00 PM)
            totalMinutes in ishaMin until (ishaMin + 90) -> "isha_waqt" // ১১. এশার সময়

            // Night deeds before sleep (Isha + 90 mins until 11:00 PM)
            totalMinutes in (ishaMin + 90) until (23 * 60) -> "before_sleep" // ১২. রাতের আমল

            // Bedtime duas (11:00 PM - 11:45 PM)
            totalMinutes in (23 * 60) until (23 * 60 + 45) -> "bedtime_dua" // ১৩. শয়নের দোয়া

            // Special Duas & Istighfar (11:45 PM - 00:30 AM)
            totalMinutes >= (23 * 60 + 45) || totalMinutes < (0 * 60 + 30) -> "special_duas" // ১৪. বিশেষ দোয়া

            // Night sleep (00:30 AM - 03:00 AM)
            else -> "sleep_bedtime" // ১৬. ঘুম
        }
    }

    fun autoSelectCurrentRoutineTimeSlot() {
        val slotId = getCurrentRoutineTimeSlotId()
        _routineTimeSlotFilter.value = slotId
    }

    val filteredRoutineList = combine(
        _routineTimeSlotFilter,
        _routineSearchQuery
    ) { slot, query ->
        RoutineData.routineList.filter { item ->
            val matchesSlot = when (slot) {
                "all" -> true
                "top10" -> item.isTopPriority
                "minimum" -> item.isMinimumRoutine
                "ideal" -> item.isIdealRoutine
                else -> item.timeSlotId == slot
            }
            val matchesQuery = query.isBlank() ||
                    item.titleBn.contains(query, ignoreCase = true) ||
                    item.subtitleBn.contains(query, ignoreCase = true) ||
                    item.descriptionBn.contains(query, ignoreCase = true) ||
                    item.virtuesRewardBn.contains(query, ignoreCase = true) ||
                    item.arabicText.contains(query) ||
                    item.pronunciationBn.contains(query, ignoreCase = true) ||
                    item.meaningBn.contains(query, ignoreCase = true) ||
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

    val screenEffectMode = repository.screenEffectModeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ScreenEffectMode.GLASS
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

    fun setScreenEffectMode(mode: ScreenEffectMode) {
        viewModelScope.launch { repository.setScreenEffectMode(mode) }
    }

    fun setFontScale(scale: FontSizeScale) {
        viewModelScope.launch { repository.setFontScale(scale) }
    }

    fun increaseFontScale() {
        val current = fontScale.value
        val entries = FontSizeScale.entries
        val idx = entries.indexOf(current).let { if (it == -1) 2 else it }
        if (idx < entries.lastIndex) {
            setFontScale(entries[idx + 1])
        }
    }

    fun decreaseFontScale() {
        val current = fontScale.value
        val entries = FontSizeScale.entries
        val idx = entries.indexOf(current).let { if (it == -1) 2 else it }
        if (idx > 0) {
            setFontScale(entries[idx - 1])
        }
    }

    fun resetFontScale() {
        setFontScale(FontSizeScale.NORMAL)
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

    val flipClockFont = repository.flipClockFontFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        FlipClockFont.RETRO_7SEGMENT
    )

    fun setFlipClockFont(font: FlipClockFont) {
        viewModelScope.launch { repository.setFlipClockFont(font) }
    }

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

    private val _themeModalInitialTab = MutableStateFlow(ModalSectionTab.THEMES)
    val themeModalInitialTab = _themeModalInitialTab.asStateFlow()

    fun openThemeModal(initialTab: ModalSectionTab = ModalSectionTab.THEMES) {
        _themeModalInitialTab.value = initialTab
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

    // LIVE AURORA WALLPAPER STATE & ACTIONS
    val auroraConfig = repository.auroraConfigFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        AuroraWallpaperConfig()
    )

    fun updateAuroraConfig(config: AuroraWallpaperConfig) {
        viewModelScope.launch { repository.updateAuroraConfig(config) }
    }

    fun setAuroraEnabled(enabled: Boolean) {
        viewModelScope.launch { repository.setAuroraEnabled(enabled) }
    }

    fun setAuroraPreset(preset: AuroraWavePreset) {
        viewModelScope.launch { repository.setAuroraPreset(preset) }
    }

    fun setAuroraIntensity(intensity: Float) {
        viewModelScope.launch { repository.setAuroraIntensity(intensity) }
    }

    fun setAuroraSpeed(speed: Float) {
        viewModelScope.launch { repository.setAuroraSpeed(speed) }
    }

    fun setAuroraParticles(show: Boolean) {
        viewModelScope.launch { repository.setAuroraParticles(show) }
    }

    // EXPLORE FEATURE CUSTOMIZATION & RELOCATION
    val exploreFeatureOrder: StateFlow<String> = repository.exploreFeatureOrderFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ""
    )

    val exploreSortMode: StateFlow<String> = repository.exploreSortModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "DEFAULT"
    )

    fun saveExploreFeatureOrder(orderedIds: List<String>) {
        viewModelScope.launch {
            repository.setExploreFeatureOrder(orderedIds.joinToString(","))
        }
    }

    fun setExploreSortMode(mode: String) {
        viewModelScope.launch {
            repository.setExploreSortMode(mode)
        }
    }

    fun resetExploreOrder() {
        viewModelScope.launch {
            repository.resetExploreOrder()
        }
    }

    // IN-APP PUSH UPDATE & GITHUB RELEASES ENGINE
    private val _updateAlertMessage = MutableStateFlow<String?>(null)
    val updateAlertMessage = _updateAlertMessage.asStateFlow()

    private val _latestReleaseInfo = MutableStateFlow<GitHubReleaseInfo?>(null)
    val latestReleaseInfo = _latestReleaseInfo.asStateFlow()

    private val _isDownloadingUpdate = MutableStateFlow(false)
    val isDownloadingUpdate = _isDownloadingUpdate.asStateFlow()

    data class ApkDownloadProgress(
        val isDownloading: Boolean = false,
        val progress: Float = 0f,
        val downloadedMb: Float = 0f,
        val totalMb: Float = 0f,
        val downloadedFile: java.io.File? = null,
        val error: String? = null,
        val waitingForInstallPermission: Boolean = false,
        val installCompleted: Boolean = false,
        val isAlreadyUpToDate: Boolean = false,
        val isSameVersionInstalled: Boolean = false,
        val upToDatePromptTitle: String = "This version already installed.",
        val upToDateMessage: String? = null
    )

    private val _apkDownloadState = MutableStateFlow(ApkDownloadProgress())
    val apkDownloadState = _apkDownloadState.asStateFlow()

    private val _activeAnnouncement = MutableStateFlow<AnnouncementData?>(null)
    val activeAnnouncement = _activeAnnouncement.asStateFlow()

    val appliedContentVersion: String
        get() = gitHubUpdateManager.appliedContentVersion

    val installedAppVersionName: String
        get() = gitHubUpdateManager.installedAppVersionName

    val installedAppVersionCode: Long
        get() = gitHubUpdateManager.installedAppVersionCode

    fun checkForAppUpdates() {
        viewModelScope.launch {
            _updateAlertMessage.value = "গিটহাব (GitHub) রিলিজ ও app-updates.json যাচাই করা হচ্ছে..."
            val result = gitHubUpdateManager.checkLatestRelease()
            if (result.isSuccess) {
                val release = result.getOrNull()
                _latestReleaseInfo.value = release

                val installedName = gitHubUpdateManager.installedAppVersionName
                val installedCode = gitHubUpdateManager.installedAppVersionCode
                val remoteName = release?.tagName?.removePrefix("v")?.removePrefix("V")?.trim() ?: ""
                val remoteCode = (release?.remoteVersionCode ?: 0).toLong()

                val isNewer = if (remoteCode > 0 && installedCode > 0) {
                    remoteCode > installedCode
                } else if (remoteName.isNotBlank()) {
                    gitHubUpdateManager.isVersionNewer(remoteName, installedName)
                } else {
                    release?.hasNewerVersion == true
                }

                if (release != null && isNewer) {
                    val ann = if (!release.announcement.isNullOrBlank()) "\n\nঘোষণা: ${release.announcement}" else ""
                    val source = if (release.isFromAppUpdatesJson) " (app-updates.json থেকে)" else " (GitHub Releases থেকে)"
                    _updateAlertMessage.value = "গিটহাবে নতুন সংস্করণ পাওয়া গেছে (${release.tagName})$source!\n${release.versionName}\n\nনতুন পরিবর্তন:\n${release.releaseNotes.take(300)}$ann\n\n'সম্পূর্ণ APK ওটিএ আপডেট' বাটনে চাপলে সরাসরি নতুন APK ডাউনলোড ও ইনস্টল হবে।"
                } else {
                    _updateAlertMessage.value = "This version already installed.\n\nআপনার ডিভাইসে ইতিমধ্যে সংস্করণ v$installedName (কোড: $installedCode) সফলভাবে ইনস্টল রয়েছে। ওটিএ আপডেট সম্পন্ন হয়েছে, কোনো নতুন সংস্করণ অবশিষ্ট নেই।"
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
     * Starts the full OTA APK update process:
     * 1. If same version is already installed on the phone, gives prompt: "This version already installed." and aborts update.
     * 2. Clears any stale or older cached APKs so old versions never hijack the update.
     * 3. Downloads the latest APK in-app with streaming byte & percentage progress
     * 4. Automatically launches Android package installer.
     * 5. Handles Unknown Sources permission if needed.
     */
    fun startFullOtaApkUpdate(forceDownload: Boolean = false) {
        viewModelScope.launch {
            _apkDownloadState.value = ApkDownloadProgress(isDownloading = true, progress = 0.05f)

            var release = _latestReleaseInfo.value
            if (release == null) {
                val checkResult = gitHubUpdateManager.checkLatestRelease()
                if (checkResult.isSuccess) {
                    release = checkResult.getOrNull()
                    _latestReleaseInfo.value = release
                }
            }

            val installedCode = gitHubUpdateManager.installedAppVersionCode
            val installedName = gitHubUpdateManager.installedAppVersionName
            val remoteVer = release?.tagName?.removePrefix("v")?.removePrefix("V")?.trim() ?: ""
            val remoteCode = (release?.remoteVersionCode ?: 0).toLong()

            // If same or older version is installed on the phone, prompt and abort!
            val isSameOrOlder = if (remoteCode > 0 && installedCode > 0) {
                remoteCode <= installedCode
            } else if (remoteVer.isNotBlank()) {
                !gitHubUpdateManager.isVersionNewer(remoteVer, installedName)
            } else {
                release?.hasNewerVersion == false
            }

            if (!forceDownload && isSameOrOlder) {
                // Clear any stale cached APK files so they never cause confusion
                gitHubUpdateManager.clearCachedApks()
                _apkDownloadState.value = ApkDownloadProgress(
                    isDownloading = false,
                    isAlreadyUpToDate = true,
                    isSameVersionInstalled = true,
                    upToDatePromptTitle = "This version already installed.",
                    upToDateMessage = "This version already installed.\n\nআপনার ডিভাইসে ইতিমধ্যে বর্তমান সংস্করণ v$installedName (কোড: $installedCode) সফলভাবে ইনস্টল রয়েছে। কোনো নতুন আপডেট নেই।\n\nওটিএ আপডেট প্রক্রিয়া বাতিল করা হয়েছে (Update Aborted)।"
                )
                return@launch
            }

            // Remote has a newer version: check if a valid cached APK matching this new version exists
            val existingApk = gitHubUpdateManager.getValidCachedApkFile(remoteCode, remoteVer)
            if (!forceDownload && existingApk != null && existingApk.length() > 5_000_000L) {
                val sizeMb = existingApk.length().toFloat() / (1024f * 1024f)
                _apkDownloadState.value = ApkDownloadProgress(
                    isDownloading = false,
                    progress = 1f,
                    downloadedMb = sizeMb,
                    totalMb = sizeMb,
                    downloadedFile = existingApk
                )
                val installResult = gitHubUpdateManager.installApk(existingApk)
                if (installResult.isSuccess) {
                    val launched = installResult.getOrThrow()
                    if (launched) {
                        _apkDownloadState.value = _apkDownloadState.value.copy(installCompleted = true)
                        return@launch
                    } else {
                        _apkDownloadState.value = _apkDownloadState.value.copy(waitingForInstallPermission = true)
                        return@launch
                    }
                }
            }

            val rawTargetUrl = release?.downloadUrl
                ?: "https://github.com/${gitHubUpdateManager.repoOwner}/${gitHubUpdateManager.repoName}/releases/latest/download/app-debug.apk"
            val targetUrl = if (rawTargetUrl.contains("app-release.apk", ignoreCase = true)) {
                rawTargetUrl.replace("app-release.apk", "app-debug.apk", ignoreCase = true)
            } else {
                rawTargetUrl
            }

            val downloadResult = gitHubUpdateManager.downloadApkWithProgress(targetUrl) { progress, downloadedBytes, totalBytes ->
                val dlMb = downloadedBytes.toFloat() / (1024f * 1024f)
                val totMb = if (totalBytes > 0) totalBytes.toFloat() / (1024f * 1024f) else 0f
                _apkDownloadState.value = ApkDownloadProgress(
                    isDownloading = true,
                    progress = if (progress >= 0f) progress else 0.5f,
                    downloadedMb = dlMb,
                    totalMb = totMb
                )
            }

            if (downloadResult.isSuccess) {
                val file = downloadResult.getOrThrow()
                val sizeMb = file.length().toFloat() / (1024f * 1024f)
                _apkDownloadState.value = ApkDownloadProgress(
                    isDownloading = false,
                    progress = 1f,
                    downloadedMb = sizeMb,
                    totalMb = sizeMb,
                    downloadedFile = file
                )

                // Trigger package installation
                val installResult = gitHubUpdateManager.installApk(file)
                if (installResult.isSuccess) {
                    val launched = installResult.getOrThrow()
                    if (launched) {
                        _apkDownloadState.value = _apkDownloadState.value.copy(installCompleted = true)
                    } else {
                        _apkDownloadState.value = _apkDownloadState.value.copy(waitingForInstallPermission = true)
                    }
                } else {
                    _apkDownloadState.value = _apkDownloadState.value.copy(
                        error = "ইনস্টলেশন শুরু করতে সমস্যা: ${installResult.exceptionOrNull()?.message}"
                    )
                }
            } else {
                val err = downloadResult.exceptionOrNull()?.message ?: "APK ডাউনলোড ব্যর্থ হয়েছে।"
                _apkDownloadState.value = ApkDownloadProgress(
                    isDownloading = false,
                    error = err
                )
            }
        }
    }

    fun retryInstallDownloadedApk() {
        val file = _apkDownloadState.value.downloadedFile ?: gitHubUpdateManager.getCachedApkFile()
        if (file == null) {
            startFullOtaApkUpdate(forceDownload = true)
            return
        }
        val installResult = gitHubUpdateManager.installApk(file)
        if (installResult.isSuccess) {
            val launched = installResult.getOrThrow()
            if (launched) {
                _apkDownloadState.value = _apkDownloadState.value.copy(
                    waitingForInstallPermission = false,
                    installCompleted = true
                )
            } else {
                _apkDownloadState.value = _apkDownloadState.value.copy(waitingForInstallPermission = true)
            }
        } else {
            _apkDownloadState.value = _apkDownloadState.value.copy(
                error = "ইনস্টল করা যায়নি: ${installResult.exceptionOrNull()?.message}"
            )
        }
    }

    fun dismissApkDownloadDialog() {
        _apkDownloadState.value = ApkDownloadProgress()
    }

    /**
     * Downloads the real APK for the new release via Android DownloadManager or browser
     */
    fun downloadNewApkVersion() {
        val release = _latestReleaseInfo.value
        val rawUrl = release?.downloadUrl
            ?: "https://github.com/${gitHubUpdateManager.repoOwner}/${gitHubUpdateManager.repoName}/releases/latest/download/app-debug.apk"
        val url = if (rawUrl.contains("app-release.apk", ignoreCase = true)) {
            rawUrl.replace("app-release.apk", "app-debug.apk", ignoreCase = true)
        } else {
            rawUrl
        }
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
            if (ExcludedIslamicLifeTopics.isExcluded(remote.category) || ExcludedIslamicLifeTopics.isExcluded(remote.titleBn)) {
                continue
            }
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
                remote.category.contains("৫ ওয়াক্ত", ignoreCase = true) || remote.category.contains("পাঁচ ওয়াক্ত", ignoreCase = true) || remote.category.contains("সালাত শেষে", ignoreCase = true) -> "five_waqt_after_salat"
                remote.category.contains("সালাম", ignoreCase = true) || remote.category.contains("salam", ignoreCase = true) -> "salam_before"
                remote.category.contains("ফরজ", ignoreCase = true) || remote.category.contains("farz", ignoreCase = true) || remote.category.contains("ফরয", ignoreCase = true) -> "farz_after"
                remote.category.contains("হাজত", ignoreCase = true) || remote.category.contains("hajat", ignoreCase = true) -> "salatul_hajat"
                remote.category.contains("স্বাস্থ্য", ignoreCase = true) || remote.category.contains("রোগ", ignoreCase = true) || remote.category.contains("শিফা", ignoreCase = true) -> "physical_health_dua"
                remote.category.contains("কবুল", ignoreCase = true) || remote.category.contains("সময়", ignoreCase = true) -> "dua_acceptance_times"
                remote.category.contains("তাসবীহ", ignoreCase = true) || remote.category.contains("তাহলীল", ignoreCase = true) || remote.category.contains("জিকির", ignoreCase = true) -> "daily_dhikr_tasbih_tahlil"
                remote.category.contains("ইমরান", ignoreCase = true) -> "surah_ali_imran_26_27"
                remote.category.contains("বাকারা", ignoreCase = true) -> "surah_baqarah_last_2"
                remote.category.contains("ফজর", ignoreCase = true) || remote.category.contains("মাগরিব", ignoreCase = true) || remote.category.contains("fajr", ignoreCase = true) -> "fajr_between_and_after"
                else -> ""
            }

            if (targetSecId.isNotBlank()) {
                extraItemsByTargetSection.getOrPut(targetSecId) { mutableListOf() }.add(cardItem)
            } else {
                val catName = if (remote.category.isNotBlank()) remote.category else "নতুন কনটেন্ট আমল"
                if (!ExcludedIslamicLifeTopics.isExcluded(catName)) {
                    unassignedCategoryItems.getOrPut(catName) { mutableListOf() }.add(cardItem)
                }
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
            if (ExcludedIslamicLifeTopics.isExcluded(catName)) continue
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

        // Strictly remove any section matching excluded titles
        updatedSections.removeAll { ExcludedIslamicLifeTopics.isExcluded(it.titleBn) }

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
        loadScorecardData()

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
                    loadScorecardData()
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
