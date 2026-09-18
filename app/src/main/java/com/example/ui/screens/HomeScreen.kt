package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HistoryToggleOff
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.IslamicHeaderCover
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
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen
import com.example.util.CalendarHelper

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
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

    var showSehriIftarFullScreen by remember { mutableStateOf(false) }
    var showDetailedSehriIftar by remember { mutableStateOf(false) }
    var showRamadanSchedule by remember { mutableStateOf(false) }
    var showNofolScheduleDialog by remember { mutableStateOf(false) }
    var selectedNofolSalat by remember { mutableStateOf<NofolSalatItem?>(null) }
    var showAllFeaturesDialog by remember { mutableStateOf(false) }
    var showSalatCalendarDialog by remember { mutableStateOf(false) }
    var showTripleCalendarDialog by remember { mutableStateOf(false) }
    var showAllahNamesOptionsDialog by remember { mutableStateOf(false) }

    // Full 17 Features list as requested by user
    val allAppFeatures = remember(prayerStatus, salatConfig, scorecardCompletedCount, scorecardStreak) {
        listOf(
            // ১. ট্র্যাকার
            HomeFeatureItem(
                id = "tracker",
                serialNumberBn = "০১",
                titleBn = "১. ট্র্যাকার",
                shortTitleBn = "ট্র্যাকার",
                subtitleBn = "আজকের স্কোরকার্ড: ${CalendarHelper.toBanglaNumber(scorecardCompletedCount)}/${CalendarHelper.toBanglaNumber(scorecardTotal)} সম্পন্ন • ${CalendarHelper.toBanglaNumber(scorecardStreak)} দিন স্ট্রিক",
                categoryBn = "আমল ট্র্যাকার",
                icon = Icons.Default.CheckCircle,
                iconColor = Color(0xFF10B981),
                isTopEight = true,
                onClickAction = { viewModel.openScorecard() }
            ),
            // ২. ক্যালেন্ডার
            HomeFeatureItem(
                id = "calendar",
                serialNumberBn = "০২",
                titleBn = "২. ক্যালেন্ডার",
                shortTitleBn = "ক্যালেন্ডার",
                subtitleBn = "হিজরি, বাংলা ও ইংরেজি সর্বজনীন ট্রিপল ক্যালেন্ডার",
                categoryBn = "ক্যালেন্ডার",
                icon = Icons.Default.CalendarMonth,
                iconColor = Color(0xFFF59E0B),
                isTopEight = true,
                onClickAction = { showTripleCalendarDialog = true }
            ),
            // ৩. সেহেরি ও ইফতাএর সময়
            HomeFeatureItem(
                id = "sehri_iftar",
                serialNumberBn = "০৩",
                titleBn = "৩. সেহেরি ও ইফতারের সময়",
                shortTitleBn = "সেহেরি-ইফতার",
                subtitleBn = "প্রতিদিনের সেহরি, সূর্যোদয় ও ইফতারের পূর্ণাঙ্গ সময়সূচী",
                categoryBn = "সিয়াম ও রমজান",
                icon = Icons.Default.NightsStay,
                iconColor = Color(0xFF8B5CF6),
                isTopEight = true,
                onClickAction = { showDetailedSehriIftar = true }
            ),
            // ৪. রামাদানের সময়সূচী
            HomeFeatureItem(
                id = "ramadan_schedule",
                serialNumberBn = "০৪",
                titleBn = "৪. রামাদানের সময়সূচী",
                shortTitleBn = "রমজান সময়সূচী",
                subtitleBn = "লাইভ চাঁদ দেখা, ৩০ দিনের রোজা, তারাবীহ ও হিজরি ক্যালেন্ডার",
                categoryBn = "সিয়াম ও রমজান",
                icon = Icons.Default.Brightness2,
                iconColor = Color(0xFF0D9488),
                isTopEight = true,
                onClickAction = { showRamadanSchedule = true }
            ),
            // ৫. সালাতের সময়সূচী
            HomeFeatureItem(
                id = "salat_timings",
                serialNumberBn = "০৫",
                titleBn = "৫. সালাতের সময়সূচী",
                shortTitleBn = "সালাতের সময়",
                subtitleBn = "৫ ওয়াক্ত নামাজের সঠিক ওয়াক্ত, মাকরূহ ও নিষিদ্ধ সময়",
                categoryBn = "সালাত ও সময়",
                icon = Icons.Default.AccessTime,
                iconColor = Color(0xFF0284C7),
                isTopEight = true,
                onClickAction = { showSalatCalendarDialog = true }
            ),
            // ৬. নফল সালাত
            HomeFeatureItem(
                id = "nafl_salat",
                serialNumberBn = "০৬",
                titleBn = "৬. নফল সালাত",
                shortTitleBn = "নফল সালাত",
                subtitleBn = "তাহাজ্জুদ, ইশরাক, চাশত, আওয়াবীনসহ ৮টি নফল সালাতের পূর্ণাঙ্গ সময়সূচী",
                categoryBn = "সালাত ও সময়",
                icon = Icons.Default.SelfImprovement,
                iconColor = Color(0xFFE11D48),
                isTopEight = true,
                onClickAction = { showNofolScheduleDialog = true }
            ),
            // ৭. মাসনুন দোয়া
            HomeFeatureItem(
                id = "masnun_dua",
                serialNumberBn = "০৭",
                titleBn = "৭. মাসনুন দোয়া",
                shortTitleBn = "মাসনুন দোয়া",
                subtitleBn = "কুরআন ও সিহাহ সিত্তাহর ১০০০+ নির্ভরযোগ্য সহীহ দো‘আ",
                categoryBn = "দো‘আ ও যিকির",
                icon = Icons.Default.MenuBook,
                iconColor = Color(0xFF059669),
                isTopEight = true,
                onClickAction = { viewModel.selectTab(AppTab.DUA) }
            ),
            // ৮. ২৪ ঘন্টার আমল
            HomeFeatureItem(
                id = "twenty_four_hours",
                serialNumberBn = "০৮",
                titleBn = "৮. ২৪ ঘন্টার আমল",
                shortTitleBn = "২৪ ঘণ্টার আমল",
                subtitleBn = "সকাল থেকে রাত পর্যন্ত সুন্নাত আমলের পূর্ণাঙ্গ টাইমলাইন গাইড",
                categoryBn = "দৈনন্দিন আমল",
                icon = Icons.Default.HistoryToggleOff,
                iconColor = Color(0xFFD97706),
                isTopEight = true,
                onClickAction = { viewModel.selectTab(AppTab.ROUTINE) }
            ),
            // ৯. আল্লাহর ৯৯ নাম
            HomeFeatureItem(
                id = "names_of_allah",
                serialNumberBn = "০৯",
                titleBn = "৯. আল্লাহর ৯৯ নাম",
                shortTitleBn = "৯. আল্লাহর ৯৯ নাম",
                subtitleBn = "আসমাউল হুসনা, বাংলা অর্থ, গুরুত্ব ও প্রয়োজনভিত্তিক খাস আমল",
                categoryBn = "আল্লাহর নাম",
                icon = Icons.Default.Star,
                iconColor = IslamicGold,
                onClickAction = {
                    showAllahNamesOptionsDialog = true
                }
            ),
            // ১০. রুকিয়াহ
            HomeFeatureItem(
                id = "ruqyah",
                serialNumberBn = "১০",
                titleBn = "১০. রুকিয়াহ",
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
            // ১১. সালাত ও দোয়া
            HomeFeatureItem(
                id = "salat_and_dua",
                serialNumberBn = "১১",
                titleBn = "১১. সালাত ও দোয়া",
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
            // ১২. সকাল - সন্ধার দোয়া
            HomeFeatureItem(
                id = "morning_evening_dua",
                serialNumberBn = "১২",
                titleBn = "১২. সকাল - সন্ধ্যার দোয়া",
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
            // ১৩. জুম্মাবারের আমল
            HomeFeatureItem(
                id = "jummah_amal",
                serialNumberBn = "১৩",
                titleBn = "১৩. জুম্মাবারের আমল",
                shortTitleBn = "জুম্মার আমল",
                subtitleBn = "জুমার দিনের ৫টি শ্রেষ্ঠ সুন্নাত, সূরা কাহাফ ও সা’আতুল ইজাবাহ",
                categoryBn = "বিশেষ আমল",
                icon = Icons.Default.MenuBook,
                iconColor = Color(0xFF10B981),
                onClickAction = {
                    viewModel.selectTab(AppTab.MORE)
                    (viewModel.getIslamicLifeSection("friday_special_duas")
                        ?: IslamicLifeData.sections.find { it.id == "friday_special_duas" })?.let {
                        viewModel.openIslamicLifeSection(it)
                    }
                }
            ),
            // ১৪. তওবা ও ইস্তিগফার
            HomeFeatureItem(
                id = "tawbah_istighfar",
                serialNumberBn = "১৪",
                titleBn = "১৪. তওবা ও ইস্তিগফার",
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
            // ১৫. দুরুদ শরিফের আমল
            HomeFeatureItem(
                id = "durood_amol",
                serialNumberBn = "১৫",
                titleBn = "১৫. দুরুদ শরিফের আমল",
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
            // ১৬. আরবী ফন্ট সমাধান টুল
            HomeFeatureItem(
                id = "arabic_font_tool",
                serialNumberBn = "১৬",
                titleBn = "১৬. আরবী ফন্ট সমাধান টুল",
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
            // ১৭. ইসমে আজম
            HomeFeatureItem(
                id = "isme_azam",
                serialNumberBn = "১৭",
                titleBn = "১৭. ইসমে আজম",
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
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
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

        // 1.1 In-App Active Content & Release Announcement Banner (Hidden for optimized clean UI)

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

        // 2.5 টপ ফিচার (Top Features Section - 8 items in 2 lines + 'আরও / More')
        item {
            TopFeaturesSection(
                topFeatures = allAppFeatures.take(8),
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
                onClearGpsMessage = { viewModel.clearGpsMessage() }
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

    // All Features Fullscreen Dialog (Lucrative window with all 17 features, search, and categories)
    if (showAllFeaturesDialog) {
        AllFeaturesDialog(
            features = allAppFeatures,
            onDismiss = { showAllFeaturesDialog = false }
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
