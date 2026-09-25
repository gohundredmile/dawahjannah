package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.IslamicLifeData
import com.example.data.model.NofolSalatItem
import com.example.data.model.NofolSalatRepository
import com.example.ui.components.AllFeaturesDialog
import com.example.ui.components.AllahNamesOptionsDialog
import com.example.ui.components.DailySalatCalendarDialog
import com.example.ui.components.DailyWisdomSection
import com.example.ui.components.DateTimeMasterCard
import com.example.ui.components.DetailedSehriIftarDialog
import com.example.ui.components.HomeFeatureItem
import com.example.ui.components.renumberedFeatures
import com.example.ui.components.IslamicHeaderCover
import com.example.ui.components.LiveAmolTickerBar
import com.example.ui.components.NamazGuideDialog
import com.example.ui.components.NamazModeDialog
import com.example.ui.components.NofolSalatDetailsDialog
import com.example.ui.components.NofolSalatIndependentCard
import com.example.ui.components.NofolSalatScheduleDialog
import com.example.ui.components.QuickActionCard
import com.example.ui.components.RamadanMoonScheduleDialog
import com.example.ui.components.SalatTimingsSection
import com.example.ui.components.SehriIftarFullScreenDialog
import com.example.ui.components.SehriIftarSummaryCard
import com.example.ui.components.TopFeaturesSection
import com.example.ui.components.TripleCalendarDialog
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen
import androidx.compose.ui.platform.LocalContext
import com.example.util.CalendarHelper
import com.example.util.FridayTimingHelper
import com.example.util.NamazModeManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
    val context = LocalContext.current
    val tripleCalendar by viewModel.tripleCalendar.collectAsState()
    val prayerStatus by viewModel.prayerStatus.collectAsState()
    val wisdomState by viewModel.wisdomState.collectAsState()
    val streak by viewModel.dailyStreak.collectAsState()
    val todayRecord by viewModel.todayChecklistRecord.collectAsState()
    val salatConfig by viewModel.salatConfig.collectAsState()
    val gpsStatusMessage by viewModel.gpsStatusMessage.collectAsState()
    // val announcement by viewModel.activeAnnouncement.collectAsState()

    val scorecardCompletedCount by viewModel.scorecardCompletedCount.collectAsState()
    val scorecardStreak by viewModel.scorecardStreak.collectAsState()
    val scorecardTotal = viewModel.scorecardTotalCount
    val isRefreshing by viewModel.isRefreshingHome.collectAsState()

    var showSehriIftarFullScreen by remember { mutableStateOf(false) }
    var showDetailedSehriIftar by remember { mutableStateOf(false) }
    var showRamadanSchedule by remember { mutableStateOf(false) }
    var showNofolScheduleDialog by remember { mutableStateOf(false) }
    var selectedNofolSalat by remember { mutableStateOf<NofolSalatItem?>(null) }
    var showAllFeaturesDialog by remember { mutableStateOf(false) }
    var showSalatCalendarDialog by remember { mutableStateOf(false) }
    var showTripleCalendarDialog by remember { mutableStateOf(false) }
    var showAllahNamesOptionsDialog by remember { mutableStateOf(false) }
    var showNamazGuideDialog by remember { mutableStateOf(false) }
    var showNamazModeDialog by remember { mutableStateOf(false) }

    val isAnyDialogOpen = showSehriIftarFullScreen || showDetailedSehriIftar ||
        showRamadanSchedule || showNofolScheduleDialog || (selectedNofolSalat != null) ||
        showAllFeaturesDialog || showSalatCalendarDialog || showTripleCalendarDialog ||
        showAllahNamesOptionsDialog || showNamazGuideDialog || showNamazModeDialog

    BackHandler(enabled = isAnyDialogOpen) {
        if (showSehriIftarFullScreen) showSehriIftarFullScreen = false
        else if (showDetailedSehriIftar) showDetailedSehriIftar = false
        else if (showRamadanSchedule) showRamadanSchedule = false
        else if (showNofolScheduleDialog) showNofolScheduleDialog = false
        else if (selectedNofolSalat != null) selectedNofolSalat = null
        else if (showAllFeaturesDialog) showAllFeaturesDialog = false
        else if (showSalatCalendarDialog) showSalatCalendarDialog = false
        else if (showTripleCalendarDialog) showTripleCalendarDialog = false
        else if (showAllahNamesOptionsDialog) showAllahNamesOptionsDialog = false
        else if (showNamazGuideDialog) showNamazGuideDialog = false
        else if (showNamazModeDialog) showNamazModeDialog = false
    }

    val exploreFeatureOrder by viewModel.exploreFeatureOrder.collectAsState()
    val exploreSortMode by viewModel.exploreSortMode.collectAsState()

    // Full 21 Features list with default order matching user's specimen
    val allAppFeatures = remember(prayerStatus, salatConfig, scorecardCompletedCount, scorecardStreak, showNamazModeDialog) {
        val isNamazModeActiveNow = NamazModeManager.isNamazModeActive(context)
        val namazMinutesLeft = NamazModeManager.getRemainingMinutes(context)
        val isFridayActive = FridayTimingHelper.isFridayModeActive(
            cal = java.util.Calendar.getInstance(),
            prayerList = prayerStatus.prayerList
        )
        listOf(
            // Friday Mode
            HomeFeatureItem(
                id = "friday_mode",
                serialNumberBn = "০০",
                titleBn = if (isFridayActive) "০. Friday Mode (সক্রিয়)" else "০. Friday Mode (জুমার মোড)",
                shortTitleBn = if (isFridayActive) "Friday Mode (সক্রিয়)" else "Friday Mode",
                subtitleBn = "বৃহস্পতিবার মাগরিব থেকে শুক্রবার মাগরিব • সূরা কাহাফ, ১০ সুন্নাত ও আমল",
                categoryBn = "সিগনেচার টুলস",
                icon = Icons.Default.Mosque,
                iconColor = if (isFridayActive) Color(0xFF047857) else IslamicGold,
                isTopEight = true,
                onClickAction = { viewModel.openFridayMode() }
            ),
            // পবিত্র কুরআন
            HomeFeatureItem(
                id = "holy_quran",
                serialNumberBn = "০১",
                titleBn = "১. পবিত্র কুরআন",
                shortTitleBn = "আল-কুরআন",
                subtitleBn = "১১৪ সূরার অনুবাদ, বাংলা উচ্চারণ, বিশদ তাফসীর ও ক্বারী তিলাওয়াত",
                categoryBn = "কুরআন ও তাফসীর",
                icon = Icons.Default.MenuBook,
                iconColor = IslamicGreen,
                isTopEight = true,
                onClickAction = { viewModel.openHolyQuran() }
            ),
            // সহীহ হাদীস সম্ভার
            HomeFeatureItem(
                id = "hadith_collection",
                serialNumberBn = "০২",
                titleBn = "২. সহীহ হাদীস সম্ভার",
                shortTitleBn = "সহীহ হাদীস",
                subtitleBn = "সিহাহ্ সিত্তাহ (বুখারী, মুসলিম, তিরমিজি...) ও অফলাইন হাদীস ডেটাবেজ",
                categoryBn = "হাদীস ও সুন্নাহ",
                icon = Icons.Default.CollectionsBookmark,
                iconColor = IslamicGold,
                isTopEight = true,
                onClickAction = { viewModel.openHadithCollection() }
            ),
            // ১. তাসবিহ
            HomeFeatureItem(
                id = "tasbih",
                serialNumberBn = "০২",
                titleBn = "২. তাসবিহ",
                shortTitleBn = "তাসবিহ",
                subtitleBn = "ডিজিটাল তাসবিহ কাউন্টার, তাসবিহ তালিকা ও জিকির",
                categoryBn = "দো‘আ ও যিকির",
                icon = Icons.Default.TouchApp,
                iconColor = IslamicGold,
                isTopEight = true,
                onClickAction = { viewModel.selectTab(AppTab.TASBIH) }
            ),
            // ২. দৈনিক আমল
            HomeFeatureItem(
                id = "twenty_four_hours",
                serialNumberBn = "০২",
                titleBn = "২. দৈনিক আমল",
                shortTitleBn = "দৈনিক আমল",
                subtitleBn = "সকাল থেকে রাত পর্যন্ত সুন্নাত আমল ও ২৪ ঘণ্টার রুটিন গাইড",
                categoryBn = "দৈনন্দিন আমল",
                icon = Icons.Default.HistoryToggleOff,
                iconColor = Color(0xFFD97706),
                isTopEight = true,
                onClickAction = { viewModel.selectTab(AppTab.ROUTINE) }
            ),
            // ৩. ক্যালেন্ডার
            HomeFeatureItem(
                id = "calendar",
                serialNumberBn = "০৩",
                titleBn = "৩. ক্যালেন্ডার",
                shortTitleBn = "ক্যালেন্ডার",
                subtitleBn = "হিজরি, বাংলা ও ইংরেজি সর্বজনীন ট্রিপল ক্যালেন্ডার",
                categoryBn = "ক্যালেন্ডার",
                icon = Icons.Default.CalendarMonth,
                iconColor = Color(0xFFEF4444),
                isTopEight = true,
                onClickAction = { showTripleCalendarDialog = true }
            ),
            // ৪. রমাদান
            HomeFeatureItem(
                id = "ramadan_schedule",
                serialNumberBn = "০৪",
                titleBn = "৪. রমাদান",
                shortTitleBn = "রমাদান",
                subtitleBn = "লাইভ চাঁদ দেখা, ৩০ দিনের রোজা, তারাবীহ ও হিজরি ক্যালেন্ডার",
                categoryBn = "সিয়াম ও রমাদান",
                icon = Icons.Default.Brightness2,
                iconColor = Color(0xFF0D9488),
                isTopEight = true,
                onClickAction = { showRamadanSchedule = true }
            ),
            // ৫. নামাজ গাইড
            HomeFeatureItem(
                id = "namaz_guide",
                serialNumberBn = "০৫",
                titleBn = "৫. নামাজ গাইড",
                shortTitleBn = "নামাজ গাইড",
                subtitleBn = "সহীহ সালাত শিক্ষা, ওয়াক্ত, সঠিক রাকাত ও ধারাবাহিক নিয়মাবলী",
                categoryBn = "সালাত ও সময়",
                icon = Icons.Default.Mosque,
                iconColor = Color(0xFF0284C7),
                isTopEight = true,
                onClickAction = { showNamazGuideDialog = true }
            ),
            // ৬. নামাজ মোড
            HomeFeatureItem(
                id = "namaz_mode",
                serialNumberBn = "০৬",
                titleBn = "৬. নামাজ মোড",
                shortTitleBn = "নামাজ মোড",
                subtitleBn = if (isNamazModeActiveNow) {
                    "নামাজ মোড সক্রিয় (ফোন সাইলেন্ট • বাকি ${CalendarHelper.toBanglaNumber(namazMinutesLeft)} মি.)"
                } else {
                    "নামাজে মোবাইল স্বয়ংক্রিয় সাইলেন্ট মোড ও ওয়াক্ত রিমাইন্ডার"
                },
                categoryBn = "সালাত ও সময়",
                icon = if (isNamazModeActiveNow) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                iconColor = if (isNamazModeActiveNow) IslamicGold else Color(0xFFE11D48),
                isTopEight = true,
                onClickAction = { showNamazModeDialog = true }
            ),
            // ৭. আমল ট্র্যাকার
            HomeFeatureItem(
                id = "tracker",
                serialNumberBn = "০৭",
                titleBn = "৭. আমল ট্র্যাকার",
                shortTitleBn = "আমল ট্র্যাকার",
                subtitleBn = "আজকের স্কোরকার্ড: ${CalendarHelper.toBanglaNumber(scorecardCompletedCount)}/${CalendarHelper.toBanglaNumber(scorecardTotal)} সম্পন্ন • ${CalendarHelper.toBanglaNumber(scorecardStreak)} দিন স্ট্রিক",
                categoryBn = "আমল ট্র্যাকার",
                icon = Icons.Default.CheckCircle,
                iconColor = Color(0xFF10B981),
                isTopEight = true,
                onClickAction = { viewModel.openScorecard() }
            ),
            // ৮. সেহেরি ও ইফতারের সময়
            HomeFeatureItem(
                id = "sehri_iftar",
                serialNumberBn = "০৮",
                titleBn = "৮. সেহেরি ও ইফতারের সময়",
                shortTitleBn = "সেহেরি-ইফতার",
                subtitleBn = "প্রতিদিনের সেহরি, সূর্যোদয় ও ইফতারের পূর্ণাঙ্গ সময়সূচী",
                categoryBn = "সিয়াম ও রমাদান",
                icon = Icons.Default.NightsStay,
                iconColor = Color(0xFF8B5CF6),
                isTopEight = true,
                onClickAction = { showDetailedSehriIftar = true }
            ),
            // ৯. ইসলামিক টুলস ও ল্যাব
            HomeFeatureItem(
                id = "tools",
                serialNumberBn = "০৯",
                titleBn = "৯. ইসলামিক টুলস ও ল্যাব",
                shortTitleBn = "টুলস",
                subtitleBn = "Explain This Ayah ক্যামেরা, শুদ্ধিকরণ ল্যাব ও ক্বিবলা কম্পাস",
                categoryBn = "স্মার্ট টুলস",
                icon = Icons.Default.Build,
                iconColor = Color(0xFF0D9488),
                isTopEight = true,
                onClickAction = { viewModel.selectTab(AppTab.TOOLS) }
            ),
            // ১০. সালাতের সময়সূচী
            HomeFeatureItem(
                id = "salat_timings",
                serialNumberBn = "১০",
                titleBn = "১০. সালাতের সময়সূচী",
                shortTitleBn = "সালাতের সময়",
                subtitleBn = "৫ ওয়াক্ত নামাজের সঠিক ওয়াক্ত, মাকরূহ ও নিষিদ্ধ সময়",
                categoryBn = "সালাত ও সময়",
                icon = Icons.Default.AccessTime,
                iconColor = Color(0xFF0284C7),
                onClickAction = { showSalatCalendarDialog = true }
            ),
            // ১১. নফল সালাত
            HomeFeatureItem(
                id = "nafl_salat",
                serialNumberBn = "১১",
                titleBn = "১১. নফল সালাত",
                shortTitleBn = "নফল সালাত",
                subtitleBn = "তাহাজ্জুদ, ইশরাক, চাশত, আওয়াবীনসহ ৮টি নফল সালাতের পূর্ণাঙ্গ সময়সূচী",
                categoryBn = "সালাত ও সময়",
                icon = Icons.Default.SelfImprovement,
                iconColor = Color(0xFFE11D48),
                onClickAction = { showNofolScheduleDialog = true }
            ),
            // ১২. মাসনুন দোয়া
            HomeFeatureItem(
                id = "masnun_dua",
                serialNumberBn = "১২",
                titleBn = "১২. মাসনুন দোয়া",
                shortTitleBn = "মাসনুন দোয়া",
                subtitleBn = "কুরআন ও সিহাহ সিত্তাহর ১০০০+ নির্ভরযোগ্য সহীহ দো‘আ",
                categoryBn = "দো‘আ ও যিকির",
                icon = Icons.Default.MenuBook,
                iconColor = Color(0xFF059669),
                onClickAction = { viewModel.selectTab(AppTab.DUA) }
            ),
            // ১৩. আল্লাহর ৯৯ নাম
            HomeFeatureItem(
                id = "names_of_allah",
                serialNumberBn = "১৩",
                titleBn = "১৩. আল্লাহর ৯৯ নাম",
                shortTitleBn = "আল্লাহর ৯৯ নাম",
                subtitleBn = "আসমাউল হুসনা, বাংলা অর্থ, গুরুত্ব ও প্রয়োজনভিত্তিক খাস আমল",
                categoryBn = "আল্লাহর নাম",
                icon = Icons.Default.Star,
                iconColor = IslamicGold,
                onClickAction = { showAllahNamesOptionsDialog = true }
            ),
            // ১৪. রুকিয়াহ
            HomeFeatureItem(
                id = "ruqyah",
                serialNumberBn = "১৪",
                titleBn = "১৪. রুকিয়াহ",
                shortTitleBn = "রুকিয়াহ",
                subtitleBn = "বদনজর, যাদু-টোনা, রোগব্যাধি ও শয়তানের অনিষ্ট থেকে সহীহ শিফা",
                categoryBn = "সুরক্ষা ও শিফা",
                icon = Icons.Default.Healing,
                iconColor = Color(0xFF059669),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    (viewModel.getIslamicLifeSection("ruqyah_shariah_special")
                        ?: IslamicLifeData.sections.find { it.id == "ruqyah_shariah_special" })?.let {
                        viewModel.openIslamicLifeSection(it)
                    }
                }
            ),
            // ১৫. সালাত ও দোয়া
            HomeFeatureItem(
                id = "salat_and_dua",
                serialNumberBn = "১৫",
                titleBn = "১৫. সালাত ও দোয়া",
                shortTitleBn = "সালাত ও দোয়া",
                subtitleBn = "সিজদা, কুনুত, সালামের পূর্বে ও ৫ ওয়াক্ত সালাতের সহীহ দো‘আ",
                categoryBn = "সালাত ও দো‘আ",
                icon = Icons.Default.Mosque,
                iconColor = Color(0xFF0D9488),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    (viewModel.getIslamicLifeSection("salat_and_dua_special")
                        ?: IslamicLifeData.sections.find { it.id == "salat_and_dua_special" })?.let {
                        viewModel.openIslamicLifeSection(it)
                    }
                }
            ),
            // ১৬. সকাল - সন্ধ্যার দোয়া
            HomeFeatureItem(
                id = "morning_evening_dua",
                serialNumberBn = "১৬",
                titleBn = "১৬. সকাল - সন্ধ্যার দোয়া",
                shortTitleBn = "সকাল-সন্ধ্যা",
                subtitleBn = "ফজর ও মাগরিব পরবর্তী শ্রেষ্ঠ সহীহ মাসনূন যিকর ও সুরক্ষার আমল",
                categoryBn = "দো‘আ ও যিকির",
                icon = Icons.Default.WbSunny,
                iconColor = Color(0xFFEA580C),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    (viewModel.getIslamicLifeSection("morning_evening_special")
                        ?: IslamicLifeData.sections.find { it.id == "morning_evening_special" })?.let {
                        viewModel.openIslamicLifeSection(it)
                    }
                }
            ),
            // ১৭. জুম্মাবারের আমল
            HomeFeatureItem(
                id = "jummah_amal",
                serialNumberBn = "১৭",
                titleBn = "১৭. জুম্মাবারের আমল",
                shortTitleBn = "জুম্মার আমল",
                subtitleBn = "জুমার দিনের ৫টি শ্রেষ্ঠ সুন্নাত, সূরা কাহাফ ও সা’আতুল ইজাবাহ",
                categoryBn = "বিশেষ আমল",
                icon = Icons.Default.MenuBook,
                iconColor = Color(0xFF10B981),
                onClickAction = {
                    viewModel.openFridayMode()
                }
            ),
            // ১৮. তওবা ও ইস্তিগফার
            HomeFeatureItem(
                id = "tawbah_istighfar",
                serialNumberBn = "১৮",
                titleBn = "১৮. তওবা ও ইস্তিগফার",
                shortTitleBn = "তওবা-ইস্তিগফার",
                subtitleBn = "সাইয়্যেদুল ইস্তিগফার, গুনাহ মাফের ৪টি শর্ত ও সংকট মুক্তির আমল",
                categoryBn = "ক্ষমা ও আমল",
                icon = Icons.Default.Refresh,
                iconColor = Color(0xFF6366F1),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    (viewModel.getIslamicLifeSection("tawbah_istighfar")
                        ?: viewModel.getIslamicLifeSection("sayyidul_istighfar_special")
                        ?: IslamicLifeData.sections.find { it.id == "tawbah_istighfar" || it.id == "sayyidul_istighfar_special" })?.let {
                        viewModel.openIslamicLifeSection(it)
                    }
                }
            ),
            // ১৯. দুরুদ শরিফের আমল
            HomeFeatureItem(
                id = "durood_amol",
                serialNumberBn = "১৯",
                titleBn = "১৯. দুরুদ শরিফের আমল",
                shortTitleBn = "দুরুদ শরিফ",
                subtitleBn = "দরূদে ইব্রাহীম, তাজ, নারিয়া ও বরকতময় দরূদের সুবিশাল সংকলন",
                categoryBn = "দরূদ ও মহব্বত",
                icon = Icons.Default.AutoAwesome,
                iconColor = Color(0xFFD97706),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    viewModel.navigateToMoreSubScreen(MoreSubScreen.DUROOD_AMOL)
                }
            ),
            // ২০. আরবী ফন্ট সমাধান টুল
            HomeFeatureItem(
                id = "arabic_font_tool",
                serialNumberBn = "২০",
                titleBn = "২০. আরবী ফন্ট সমাধান টুল",
                shortTitleBn = "আরবী ফন্ট টুল",
                subtitleBn = "ভাঙ্গা শব্দ ও ভুল হরকত সনাক্তকরণ ও বিশুদ্ধ আরবী ফন্ট কনভার্টার",
                categoryBn = "ইসলামিক টুলস",
                icon = Icons.Default.FindReplace,
                iconColor = Color(0xFF0284C7),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    viewModel.navigateToMoreSubScreen(MoreSubScreen.AYAT_DETECTOR_SOLVER)
                }
            ),
            // ২১. ইসমে আজম
            HomeFeatureItem(
                id = "isme_azam",
                serialNumberBn = "২১",
                titleBn = "২১. ইসমে আজম",
                shortTitleBn = "ইসমে আজম",
                subtitleBn = "দো‘আ কবুলের শ্রেষ্ঠ ইসমে আজম, সহীহ হাদিসের আমল ও নিয়মাবলী",
                categoryBn = "দো‘আ কবুল",
                icon = Icons.Default.WorkspacePremium,
                iconColor = IslamicGold,
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    (viewModel.getIslamicLifeSection("isme_azam")
                        ?: IslamicLifeData.sections.find { it.id == "isme_azam" })?.let {
                        viewModel.openIslamicLifeSection(it)
                    }
                }
            ),
            // ২২. ক্বিবলা কম্পাস
            HomeFeatureItem(
                id = "qibla_compass",
                serialNumberBn = "২২",
                titleBn = "২২. ক্বিবলা কম্পাস",
                shortTitleBn = "ক্বিবলা কম্পাস",
                subtitleBn = "ম্যাগনেটিক ও এক্সিলারোমিটার সেন্সরে কাবা শরিফের নির্ভুল দিক ও দূরত্ব",
                categoryBn = "ইসলামিক টুলস",
                icon = Icons.Default.Explore,
                iconColor = Color(0xFF059669),
                onClickAction = {
                    viewModel.openQibla()
                }
            )
        )
    }

    val orderedAppFeatures = remember(allAppFeatures, exploreFeatureOrder, exploreSortMode) {
        val sortedList = if (exploreFeatureOrder.isBlank() || exploreSortMode == "DEFAULT") {
            allAppFeatures
        } else {
            val orderMap = exploreFeatureOrder.split(",").mapIndexed { index, id -> id to index }.toMap()
            allAppFeatures.sortedBy { orderMap[it.id] ?: 999 }
        }
        sortedList.renumberedFeatures()
    }

    val isFridayActiveBanner = remember(prayerStatus) {
        FridayTimingHelper.isFridayModeActive(
            cal = java.util.Calendar.getInstance(),
            prayerList = prayerStatus.prayerList
        )
    }
    val fridayPhaseTitle = remember(prayerStatus) {
        FridayTimingHelper.getFridayPhaseTitleBn(
            cal = java.util.Calendar.getInstance(),
            prayerList = prayerStatus.prayerList
        )
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refreshHomeScreen() },
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Welcoming Cover & Revamped Salat Timing Card with Relocated Time & Date
            item {
                IslamicHeaderCover(
                    salutation = prayerStatus.salutationBn,
                    countdownFormatted = prayerStatus.timeRemainingFormatted,
                    nextPrayerName = prayerStatus.nextPrayer?.nameBn ?: "ওয়াক্ত",
                    presentPrayerName = prayerStatus.presentPrayerNameBn,
                    presentNofolName = prayerStatus.presentNofolNameBn,
                    remainingHours = prayerStatus.remainingHours,
                    remainingMinutes = prayerStatus.remainingMinutes,
                    remainingSeconds = prayerStatus.remainingSeconds,
                    forbiddenTimeInfo = prayerStatus.forbiddenTimeInfo,
                    calendarInfo = tripleCalendar,
                    onTapTimeDate = { showTripleCalendarDialog = true },
                    onOpenSettings = {
                        viewModel.openSettings(AppTab.HOME)
                    }
                )
            }

            // 1.05. Live Interactive Date & Time Based Amol Ticker Bar (Top Region)
            item {
                LiveAmolTickerBar(
                    viewModel = viewModel,
                    calendarInfo = tripleCalendar,
                    onOpenTripleCalendar = { showTripleCalendarDialog = true }
                )
            }

            // 1.1 Automatic Friday Mode Transform Banner (From Thursday Maghrib to Friday Maghrib)
            if (isFridayActiveBanner) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                            .clickable { viewModel.openFridayMode() },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                        border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.8f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = CircleShape,
                                        color = IslamicGold.copy(alpha = 0.2f),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("🕌", fontSize = 18.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = fridayPhaseTitle,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicGold,
                                            fontFamily = LocalBanglaFontFamily.current
                                        )
                                        Text(
                                            text = "বৃহস্পতিবার মাগরিব থেকে শুক্রবার মাগরিব • সূরা কাহাফ ও আমল",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White.copy(alpha = 0.85f),
                                            fontFamily = LocalBanglaFontFamily.current
                                        )
                                    }
                                }
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF047857),
                                    modifier = Modifier.clickable { viewModel.openFridayMode() }
                                ) {
                                    Text(
                                        text = "প্রবেশ ➔",
                                        fontSize = 12.sp,
                                        color = Color.White,
                                        fontFamily = LocalBanglaFontFamily.current,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("📖 সূরা কাহাফ", "🌿 ১০ সুন্নাত", "🤲 দো'আ কবুল ক্ষণ", "📝 খুতবা নোট").forEach { chip ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.12f)
                                    ) {
                                        Text(
                                            text = chip,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontFamily = LocalBanglaFontFamily.current,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

        // 2. Quick Action & Streak Highlights
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCard(
                    title = "ধারাবাহিকতা",
                    value = "${CalendarHelper.toBanglaNumber(scorecardStreak)} দিন স্ট্রিক",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.openScorecard() }
                )

                QuickActionCard(
                    title = "আজকের আমল",
                    value = "${CalendarHelper.toBanglaNumber(scorecardCompletedCount)}/${CalendarHelper.toBanglaNumber(scorecardTotal)} সম্পন্ন",
                    icon = Icons.Default.CheckCircle,
                    iconTint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.openScorecard() }
                )

                QuickActionCard(
                    title = "তাসবীহ",
                    value = "জিকির করুন",
                    icon = Icons.Default.TouchApp,
                    iconTint = IslamicGold,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.selectTab(AppTab.TASBIH)
                    }
                )
            }
        }

        // 2.3 Explain This Ayah ক্যামেরা Highlight Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 5.dp)
                    .clickable { viewModel.openExplainAyahCamera() },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.28f)
                ),
                border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.65f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = IslamicGold.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, IslamicGold),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Explain This Ayah",
                                tint = IslamicGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Explain This Ayah ক্যামেরা",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                fontFamily = LocalBanglaFontFamily.current,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = Color(0xFF059669)
                            ) {
                                Text(
                                    text = "AI স্মার্ট",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = LocalBanglaFontFamily.current
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(3.dp))

                        Text(
                            text = "কুরআনের যেকোনো পাতার ওপর ক্যামেরা তাক করুন — অর্থ, তফসির, শানে নুযূল ও অডিও তিলাওয়াত জানুন",
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                            fontFamily = LocalBanglaFontFamily.current,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // 2.5 টপ ফিচার (Top Features Section - 8 items in 2x4 grid as per specimen + 'আরও / More')
        item {
            TopFeaturesSection(
                topFeatures = orderedAppFeatures.filter { it.id != "articles" }.take(8),
                onOpenAllFeatures = { showAllFeaturesDialog = true }
            )
        }

        // 4. সেহরি এবং ইফতারের সময়সূচী (Sehri & Ifter Timing)
        item {
            SehriIftarSummaryCard(
                prayerStatus = prayerStatus,
                salatConfig = salatConfig,
                onOpenDetailedSchedule = { showDetailedSehriIftar = true },
                onOpenRamadanSchedule = { showRamadanSchedule = true },
                onOpenFullScreen = { showDetailedSehriIftar = true }
            )
        }

        // 5. Daily Prayer Times (Salat Timings & Forbidden Time)
        item {
            SalatTimingsSection(
                prayerStatus = prayerStatus,
                salatConfig = salatConfig,
                gpsStatusMessage = gpsStatusMessage,
                onTrackGps = { viewModel.trackCurrentLocationWithGps() },
                onSelectPlace = { place -> viewModel.updateSalatPlace(place) },
                onCustomPlace = { nameBn, nameEn, lat, lng -> viewModel.setCustomSalatLocation(nameBn, nameEn, lat, lng) },
                onSetManualOffset = { offset -> viewModel.setSalatManualOffset(offset) },
                onToggleHanafiAsr = { isHanafi -> viewModel.setHanafiAsr(isHanafi) },
                onSelectCalculationMethod = { method -> viewModel.setCalculationMethod(method) },
                onSelectAsrMethod = { asr -> viewModel.setAsrJuristicMethod(asr) },
                onSelectHighLatitudeRule = { rule -> viewModel.setHighLatitudeRule(rule) },
                onResetSalatPreferences = { viewModel.resetSalatPreferencesToStandard() },
                onClearGpsMessage = { viewModel.clearGpsMessage() },
                onOpenQibla = { viewModel.openQibla() }
            )
        }

        // 6. নফল সালাতের সময়সূচী (Independent Home Screen Card)
        item {
            NofolSalatIndependentCard(
                prayerStatus = prayerStatus,
                salatConfig = salatConfig,
                onOpenScheduleWindow = { showNofolScheduleDialog = true }
            )
        }

        // 7. Daily Light & Inspiration: Holy Quran, Hadith, and Inspirational Quotes
        item {
            DailyWisdomSection(
                wisdomState = wisdomState,
                onShuffle = { viewModel.shuffleWisdom() }
            )
        }
    }
    }

    if (showDetailedSehriIftar) {
        DetailedSehriIftarDialog(
            salatConfig = salatConfig,
            onDismiss = { showDetailedSehriIftar = false }
        )
    }

    if (showRamadanSchedule) {
        RamadanMoonScheduleDialog(
            salatConfig = salatConfig,
            onDismiss = { showRamadanSchedule = false }
        )
    }

    // Full Screen Sawm & Ramadan Timing Expanded Dialog
    if (showSehriIftarFullScreen) {
        SehriIftarFullScreenDialog(
            prayerStatus = prayerStatus,
            salatConfig = salatConfig,
            onDismiss = { showSehriIftarFullScreen = false }
        )
    }

    // Nofol Salat Full Dedicated Schedule Dialog
    if (showNofolScheduleDialog) {
        NofolSalatScheduleDialog(
            prayerStatus = prayerStatus,
            salatConfig = salatConfig,
            onDismiss = { showNofolScheduleDialog = false },
            onOpenNofolDetail = { item ->
                selectedNofolSalat = item
            }
        )
    }

    // Nofol Salat Details Dialog (Opens in a rich details window upon tapping any Nafl salat)
    selectedNofolSalat?.let { nofolItem ->
        NofolSalatDetailsDialog(
            initialItem = nofolItem,
            prayerStatus = prayerStatus,
            onDismiss = { selectedNofolSalat = null }
        )
    }

    // All Features Fullscreen Dialog (Lucrative window with all 22 features, search, categories, and customization)
    if (showAllFeaturesDialog) {
        AllFeaturesDialog(
            features = orderedAppFeatures,
            defaultFeatures = allAppFeatures,
            currentSortMode = exploreSortMode,
            onUpdateOrder = { ids, mode ->
                viewModel.saveExploreFeatureOrder(ids)
                viewModel.setExploreSortMode(mode)
            },
            onDismiss = { showAllFeaturesDialog = false }
        )
    }

    // Namaz Guide Dialog (Step-by-step Sahih Salat learning and instructions)
    if (showNamazGuideDialog) {
        NamazGuideDialog(
            onDismiss = { showNamazGuideDialog = false }
        )
    }

    // Namaz Mode Dialog (Automatic Silent Mode & Salat Mode settings)
    if (showNamazModeDialog) {
        NamazModeDialog(
            onDismiss = { showNamazModeDialog = false }
        )
    }

    // Daily Salat & Multi-Year Calendar Dialog
    if (showSalatCalendarDialog) {
        DailySalatCalendarDialog(
            onDismiss = { showSalatCalendarDialog = false },
            salatConfig = salatConfig
        )
    }

    // Standalone Triple Calendar Dialog (Triggered when tapping Time & Date or Feature 2)
    if (showTripleCalendarDialog) {
        TripleCalendarDialog(
            calendarInfo = tripleCalendar,
            salatConfig = salatConfig,
            onDismiss = { showTripleCalendarDialog = false }
        )
    }

    // Allah's 99 Names Options Dialog (Option 1: 99 Names & Fojilot, Option 2: Needs-based Duas)
    if (showAllahNamesOptionsDialog) {
        AllahNamesOptionsDialog(
            onDismiss = { showAllahNamesOptionsDialog = false },
            onSelectNamesAndFojilot = {
                viewModel.selectTab(AppTab.MORE)
                viewModel.navigateToMoreSubScreen(MoreSubScreen.NAMES_OF_ALLAH)
            },
            onSelectNeedsBasedDua = {
                viewModel.selectTab(AppTab.MORE)
                (viewModel.getIslamicLifeSection("asmaul_husna_special")
                    ?: IslamicLifeData.sections.find { it.id == "asmaul_husna_special" })?.let {
                    viewModel.openIslamicLifeSection(it)
                }
            }
        )
    }
}
