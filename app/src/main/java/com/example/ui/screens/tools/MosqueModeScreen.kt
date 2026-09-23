package com.example.ui.screens.tools

import android.app.Activity
import android.media.AudioManager
import android.view.WindowManager
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.MosqueDataCatalog
import com.example.data.model.MosqueAnnouncement
import com.example.data.model.MosqueCharityFund
import com.example.data.model.MosqueInfo
import com.example.data.model.MosqueSalahStatus
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.MainViewModel
import com.example.util.MosqueModeManager
import com.example.util.NamazModeManager
import kotlinx.coroutines.delay

@Composable
fun MosqueModeScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit,
    onOpenQibla: () -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val prayerStatus by viewModel.prayerStatus.collectAsState()

    // State management
    var isMosqueModeActive by remember { mutableStateOf(MosqueModeManager.isMosqueModeActive(context)) }
    var isTranquilOled by remember { mutableStateOf(MosqueModeManager.isTranquilOledMode(context)) }
    var isKeepScreenOn by remember { mutableStateOf(MosqueModeManager.isKeepScreenOn(context)) }

    // Mosque details
    var mosqueName by remember { mutableStateOf(MosqueModeManager.getMosqueName(context)) }
    var mosqueArea by remember { mutableStateOf(MosqueModeManager.getMosqueArea(context)) }
    var showEditMosqueDialog by remember { mutableStateOf(false) }

    // Announcements and Charity Funds
    var announcementsList by remember { mutableStateOf(MosqueModeManager.getAllAnnouncements(context)) }
    val charityFundsList = remember { MosqueDataCatalog.charityFunds }

    // Hardware Audio state
    var currentRingerMode by remember { mutableIntStateOf(NamazModeManager.getCurrentRingerMode(context)) }
    var silentDurationMins by remember { mutableIntStateOf(NamazModeManager.getSilentDurationMins(context)) }
    var remainingSilentMins by remember { mutableIntStateOf(NamazModeManager.getRemainingMinutes(context)) }

    // Tab state (0: Prayer/Jamat, 1: Jumuah, 2: Announcements, 3: Classes, 4: Charity, 5: Etiquette)
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("ওয়াক্ত ও জামা'আত", "জুমু'আহ স্পেশাল", "নোটিশ বোর্ড", "দ্বীনি ক্লাস", "সাদাকাহ ফান্ড", "মসজিদের আদব")

    // Dialogs state
    var showQuranReader by remember { mutableStateOf(false) }
    var showAdhkarReader by remember { mutableStateOf(false) }
    var showPrayerGuide by remember { mutableStateOf(false) }
    var showPrayerTracker by remember { mutableStateOf(false) }
    var showAddAnnouncementDialog by remember { mutableStateOf(false) }
    var selectedDonationFund by remember { mutableStateOf<MosqueCharityFund?>(null) }
    var showDndGuideDialog by remember { mutableStateOf(false) }

    // Daily prayer status map
    var todaySalahStatuses by remember { mutableStateOf(MosqueModeManager.getAllTodaySalahStatuses(context)) }

    // Handle keep screen on
    val activity = context as? Activity
    DisposableEffect(isKeepScreenOn) {
        if (isKeepScreenOn) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    // Refresh remaining minutes periodically
    LaunchedEffect(isMosqueModeActive) {
        while (isMosqueModeActive) {
            remainingSilentMins = NamazModeManager.getRemainingMinutes(context)
            currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
            delay(15000L)
        }
    }

    // Colors according to Tranquil OLED Mode
    val bgColor = if (isTranquilOled) Color(0xFF050505) else MaterialTheme.colorScheme.background
    val topCardColor = if (isTranquilOled) Color(0xFF0F172A) else Color(0xFF063529)
    val cardBgColor = if (isTranquilOled) Color(0xFF111827) else MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // TOP BAR: Navigation & Atmosphere Controls
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "ফিরে যান",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        Column {
                            Text(
                                text = "মসজিদ মোড (Mosque Mode)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "একাগ্রতা ও সম্পূর্ণ বিভ্রান্তিমুক্ত পরিবেশ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // OLED Dimmer button
                        IconButton(onClick = {
                            val next = !isTranquilOled
                            isTranquilOled = next
                            MosqueModeManager.setTranquilOledMode(context, next)
                        }) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = "OLED মোড",
                                tint = if (isTranquilOled) IslamicGold else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                            )
                        }
                    }
                }
            }

            // MASTER SWITCH HERO CARD
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = topCardColor),
                    border = BorderStroke(
                        1.2.dp,
                        if (isMosqueModeActive) IslamicGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF059669).copy(alpha = if (isMosqueModeActive) 0.25f else 0.08f),
                                        IslamicGold.copy(alpha = if (isMosqueModeActive) 0.15f else 0.02f)
                                    )
                                )
                            )
                            .padding(18.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isMosqueModeActive) IslamicGold.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.1f),
                                        modifier = Modifier.size(46.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (isMosqueModeActive) Icons.Default.NotificationsOff else Icons.Default.Shield,
                                                contentDescription = null,
                                                tint = if (isMosqueModeActive) IslamicGold else Color.White,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = if (isMosqueModeActive) "মসজিদ মোড সক্রিয় আছে" else "মসজিদ মোড সক্রিয় করুন",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            fontFamily = banglaFont
                                        )
                                        Text(
                                            text = if (isMosqueModeActive) "ডিভাইস নিঃশব্দ • একাগ্রচিত্তে ইবাদত করুন" else "মসজিদে প্রবেশের সাথে সাথে এক ট্যাপে অন করুন",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.8f),
                                            fontFamily = banglaFont
                                        )
                                    }
                                }

                                Switch(
                                    checked = isMosqueModeActive,
                                    onCheckedChange = { active ->
                                        if (active && !NamazModeManager.hasDndPermission(context)) {
                                            showDndGuideDialog = true
                                        }
                                        isMosqueModeActive = active
                                        MosqueModeManager.setMosqueModeActive(context, active)
                                        currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
                                        remainingSilentMins = NamazModeManager.getRemainingMinutes(context)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = IslamicGold,
                                        checkedTrackColor = Color(0xFF047857)
                                    )
                                )
                            }

                            // Active Info & Dua Pill
                            AnimatedVisibility(visible = isMosqueModeActive) {
                                Column(modifier = Modifier.padding(top = 14.dp)) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color.Black.copy(alpha = 0.35f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.AccessTime,
                                                    contentDescription = null,
                                                    tint = IslamicGold,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = if (remainingSilentMins > 0) "সাইলেন্ট বাকি: $remainingSilentMins মিনিট" else "সাইলেন্ট মোড চালু",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = IslamicGold,
                                                    fontFamily = banglaFont
                                                )
                                            }

                                            Text(
                                                text = "+১৫ মি. বাড়ান",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF34D399),
                                                fontFamily = banglaFont,
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .clickable {
                                                        val duration = (remainingSilentMins + 15).coerceAtMost(120)
                                                        NamazModeManager.activateNamazMode(context, duration, true)
                                                        remainingSilentMins = NamazModeManager.getRemainingMinutes(context)
                                                    }
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))

                                    // Masnoon Entry Dua Reminder
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "প্রবেশের দু'আ: «اللَّهُمَّ افْتَحْ لِي أَبْوَابَ رَحْمَتِكَ»",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECTION: THE 6 LARGE BUTTONS (Quran, Adhkar, Prayer, Qibla, Silent, Prayer Tracker)
            item {
                Text(
                    text = "প্রয়োজনীয় কুইক একশন (Large Quick Actions)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = banglaFont,
                    modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
                )
            }

            // Grid Row 1: Quran & Adhkar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LargeMosqueActionButton(
                        title = "কুরআন",
                        subtitle = "কাহাফ, মুলক, ইয়াসীন",
                        icon = Icons.Default.MenuBook,
                        accentColor = Color(0xFF10B981),
                        modifier = Modifier.weight(1f),
                        isDarkOled = isTranquilOled,
                        onClick = { showQuranReader = true }
                    )

                    LargeMosqueActionButton(
                        title = "আযকার",
                        subtitle = "সালাত-পরবর্তী যিকির",
                        icon = Icons.Default.Fingerprint,
                        accentColor = IslamicGold,
                        modifier = Modifier.weight(1f),
                        isDarkOled = isTranquilOled,
                        onClick = { showAdhkarReader = true }
                    )
                }
            }

            // Grid Row 2: Prayer & Qibla
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LargeMosqueActionButton(
                        title = "সালাত গাইড",
                        subtitle = "তাহিয়্যাতুল মসজিদ ও আদব",
                        icon = Icons.Default.CheckCircle,
                        accentColor = Color(0xFF38BDF8),
                        modifier = Modifier.weight(1f),
                        isDarkOled = isTranquilOled,
                        onClick = { showPrayerGuide = true }
                    )

                    LargeMosqueActionButton(
                        title = "ক্বিবলা",
                        subtitle = "২৮৩° উ-প • পবিত্র কা'বা",
                        icon = Icons.Default.Explore,
                        accentColor = Color(0xFFF59E0B),
                        modifier = Modifier.weight(1f),
                        isDarkOled = isTranquilOled,
                        onClick = onOpenQibla
                    )
                }
            }

            // Grid Row 3: Silent & Prayer Tracker
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val isSilentNow = currentRingerMode == AudioManager.RINGER_MODE_SILENT || currentRingerMode == AudioManager.RINGER_MODE_VIBRATE
                    LargeMosqueActionButton(
                        title = "সাইলেন্ট",
                        subtitle = if (isSilentNow) "নিঃশব্দ • অটো-রিস্টোর" else "মিউট চালু করুন",
                        icon = if (isSilentNow) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                        accentColor = if (isSilentNow) Color(0xFF34D399) else Color(0xFFEF4444),
                        modifier = Modifier.weight(1f),
                        isDarkOled = isTranquilOled,
                        onClick = {
                            if (!NamazModeManager.hasDndPermission(context)) {
                                showDndGuideDialog = true
                            } else {
                                if (isSilentNow) {
                                    NamazModeManager.deactivateNamazMode(context)
                                } else {
                                    NamazModeManager.activateNamazMode(context, silentDurationMins, true)
                                }
                                currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
                                remainingSilentMins = NamazModeManager.getRemainingMinutes(context)
                            }
                        }
                    )

                    LargeMosqueActionButton(
                        title = "সালাত ট্র্যাকার",
                        subtitle = "আজকের ৫ ওয়াক্ত রেকর্ড",
                        icon = Icons.Default.Stars,
                        accentColor = Color(0xFFA78BFA),
                        modifier = Modifier.weight(1f),
                        isDarkOled = isTranquilOled,
                        onClick = { showPrayerTracker = true }
                    )
                }
            }

            // SECTION: NO UNNECESSARY NOTIFICATIONS SHIELD BANNER
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isTranquilOled) Color(0xFF1E293B) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "বিজ্ঞপ্তি ও বিভ্রান্তিমুক্ত সুরক্ষা শিল্ড",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "মসজিদ মোডে সকল প্রকার অপ্রয়োজনীয় পুশ ও শব্দ নিষ্ক্রিয় রাখা হয়।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }

            // SECTION: MOSQUE SPECIFIC INFORMATION HUB (Tabs)
            item {
                Text(
                    text = "মসজিদ কেন্দ্রিক তথ্য ও কার্যক্রম",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontFamily = banglaFont,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }

            // Tab Selector Row
            item {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    edgePadding = 0.dp,
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {}
                ) {
                    tabs.forEachIndexed { index, title ->
                        val isSelected = selectedTabIndex == index
                        Tab(
                            selected = isSelected,
                            onClick = { selectedTabIndex = index },
                            text = {
                                Text(
                                    text = title,
                                    fontFamily = banglaFont,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }

            // Tab Content
            item {
                val mosqueInfo = MosqueInfo(
                    nameBn = mosqueName,
                    areaBn = mosqueArea,
                    jumuahKhutbahTime = MosqueModeManager.getJumuahKhutbahTime(context),
                    jumuahJamatTime = MosqueModeManager.getJumuahJamatTime(context)
                )

                when (selectedTabIndex) {
                    0 -> MosquePrayerJamatTab(
                        prayerStatus = prayerStatus,
                        mosqueInfo = mosqueInfo,
                        onEditMosque = { showEditMosqueDialog = true },
                        isDarkOled = isTranquilOled
                    )
                    1 -> MosqueJumuahTab(
                        mosqueInfo = mosqueInfo,
                        onOpenSurahKahf = { showQuranReader = true },
                        isDarkOled = isTranquilOled
                    )
                    2 -> MosqueAnnouncementsTab(
                        announcements = announcementsList,
                        onAddAnnouncement = { showAddAnnouncementDialog = true },
                        isDarkOled = isTranquilOled
                    )
                    3 -> MosqueClassesTab(
                        classes = MosqueDataCatalog.classes,
                        isDarkOled = isTranquilOled
                    )
                    4 -> MosqueCharityTab(
                        charityFunds = charityFundsList,
                        onDonateClick = { selectedDonationFund = it },
                        isDarkOled = isTranquilOled
                    )
                    5 -> MosqueEtiquetteTab(isDarkOled = isTranquilOled)
                }
            }
        }
    }

    // --- DIALOGS ---

    if (showQuranReader) {
        MosqueQuranReaderDialog(onDismiss = { showQuranReader = false })
    }

    if (showAdhkarReader) {
        MosqueAdhkarDialog(onDismiss = { showAdhkarReader = false })
    }

    if (showPrayerGuide) {
        MosquePrayerGuideDialog(onDismiss = { showPrayerGuide = false })
    }

    if (showPrayerTracker) {
        MosquePrayerTrackerDialog(
            currentStatuses = todaySalahStatuses,
            onStatusChange = { key, status ->
                MosqueModeManager.setSalahStatus(context, key, status)
                todaySalahStatuses = MosqueModeManager.getAllTodaySalahStatuses(context)
            },
            onDismiss = { showPrayerTracker = false }
        )
    }

    if (showAddAnnouncementDialog) {
        MosqueAddAnnouncementDialog(
            onSave = { newAnn ->
                MosqueModeManager.addCustomAnnouncement(context, newAnn)
                announcementsList = MosqueModeManager.getAllAnnouncements(context)
            },
            onDismiss = { showAddAnnouncementDialog = false }
        )
    }

    selectedDonationFund?.let { fund ->
        MosqueCharityDonationDialog(
            fund = fund,
            onConfirmDonation = { amt ->
                MosqueModeManager.recordDonation(context, fund.id, amt)
            },
            onDismiss = { selectedDonationFund = null }
        )
    }

    // Mosque Name/Area Editor Dialog
    if (showEditMosqueDialog) {
        var tempName by remember { mutableStateOf(mosqueName) }
        var tempArea by remember { mutableStateOf(mosqueArea) }
        var tempKhutbah by remember { mutableStateOf(MosqueModeManager.getJumuahKhutbahTime(context)) }
        var tempJamat by remember { mutableStateOf(MosqueModeManager.getJumuahJamatTime(context)) }

        AlertDialog(
            onDismissRequest = { showEditMosqueDialog = false },
            title = {
                Text("মসজিদের তথ্য পরিবর্তন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("মসজিদের নাম", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tempArea,
                        onValueChange = { tempArea = it },
                        label = { Text("এলাকা / ঠিকানা", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = tempKhutbah,
                            onValueChange = { tempKhutbah = it },
                            label = { Text("খুতবা সময়", fontFamily = banglaFont) },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = tempJamat,
                            onValueChange = { tempJamat = it },
                            label = { Text("জামা'আত সময়", fontFamily = banglaFont) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    mosqueName = tempName.trim()
                    mosqueArea = tempArea.trim()
                    MosqueModeManager.setMosqueName(context, mosqueName)
                    MosqueModeManager.setMosqueArea(context, mosqueArea)
                    MosqueModeManager.setJumuahKhutbahTime(context, tempKhutbah)
                    MosqueModeManager.setJumuahJamatTime(context, tempJamat)
                    showEditMosqueDialog = false
                }) {
                    Text("সংরক্ষণ", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showEditMosqueDialog = false }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }

    // DND Permission Dialog
    if (showDndGuideDialog) {
        AlertDialog(
            onDismissRequest = { showDndGuideDialog = false },
            title = {
                Text("সাইলেন্ট মোড পারমিশন প্রয়োজন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "অ্যাপ থেকে স্বয়ংক্রিয়ভাবে ফোন সম্পূর্ণ নিঃশব্দ/সাইলেন্ট করতে সিস্টেমের Do Not Disturb (DND) পলিসি এক্সেস প্রয়োজন। অনুমতি দিতে 'সেটিংস খুলুন'-এ চাপুন।",
                    fontFamily = banglaFont,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(onClick = {
                    NamazModeManager.openDndSettings(context)
                    showDndGuideDialog = false
                }) {
                    Text("সেটিংস খুলুন", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDndGuideDialog = false }) {
                    Text("পরে করব", fontFamily = banglaFont)
                }
            }
        )
    }
}

@Composable
fun LargeMosqueActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier,
    isDarkOled: Boolean,
    onClick: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkOled) Color(0xFF111827) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, accentColor.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .height(115.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            accentColor.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
                .padding(14.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = accentColor.copy(alpha = 0.18f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = accentColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.2f),
                        modifier = Modifier.size(8.dp)
                    ) {}
                }

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}
