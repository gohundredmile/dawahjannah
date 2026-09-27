package com.example.ui.screens.tools

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.model.GeneratorPresetType
import com.example.data.model.QuranReciter
import com.example.data.model.QuranSurah
import com.example.data.model.SavedGeneratorSession
import com.example.data.repository.QuranRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAudioGeneratorEngine

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuranAudioGeneratorScreen(
    onNavigateBack: () -> Unit,
    generatorEngine: QuranAudioGeneratorEngine
) {
    val banglaFont = LocalBanglaFontFamily.current

    val config by generatorEngine.config.collectAsState()
    val playerStatus by generatorEngine.playerStatus.collectAsState()
    val currentSurahAyahs by generatorEngine.currentSurahAyahs.collectAsState()
    val savedSessions by generatorEngine.savedSessions.collectAsState()
    val isDownloading by generatorEngine.isDownloading.collectAsState()
    val downloadProgress by generatorEngine.downloadProgress.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var showSurahPickerSheet by remember { mutableStateOf(false) }
    var showInfoDialog by remember { mutableStateOf(false) }
    var showSaveSessionDialog by remember { mutableStateOf(false) }
    var newSessionTitle by remember { mutableStateOf("") }

    // Intercept back navigation
    BackHandler {
        if (selectedTab != 0) {
            selectedTab = 0
        } else {
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "কুরআন অডিও তিলাওয়াত জেনারেটর",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "হিফজ, মুরাজা'আ ও কাস্টম অডিও সেশন",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedTab != 0) {
                                selectedTab = 0
                            } else {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.testTag("audio_gen_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showInfoDialog = true },
                        modifier = Modifier.testTag("audio_gen_info_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.HelpOutline,
                            contentDescription = "তথ্য ও নির্দেশিকা",
                            tint = Color(0xFF047857)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color(0xFF047857)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "স্মার্ট সেটআপ",
                            fontFamily = banglaFont,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "লাইভ প্লেয়ার",
                                fontFamily = banglaFont,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                            if (playerStatus.isPlaying) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(Color(0xFF10B981), CircleShape)
                                )
                            }
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = "সংরক্ষিত (${BanglaNumberUtils.toBanglaDigits(savedSessions.size)})",
                            fontFamily = banglaFont,
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
            }

            when (selectedTab) {
                0 -> GeneratorSetupTab(
                    config = config,
                    playerStatus = playerStatus,
                    isDownloading = isDownloading,
                    downloadProgress = downloadProgress,
                    onOpenSurahPicker = { showSurahPickerSheet = true },
                    onApplyPreset = { generatorEngine.applyPreset(it) },
                    onSetAyahRange = { s, e -> generatorEngine.setAyahRange(config.surahNumber, s, e) },
                    onSelectReciter = { generatorEngine.selectReciter(it) },
                    onSetAyahRepeat = { generatorEngine.setAyahRepeatCount(it) },
                    onSetPauseSeconds = { generatorEngine.setPauseBetweenAyatSeconds(it) },
                    onSetSpeed = { generatorEngine.setPlaybackSpeed(it) },
                    onSetRangeLoops = { generatorEngine.setRangeLoopCount(it) },
                    onSetSleepTimer = { generatorEngine.setSleepTimer(it) },
                    onDownloadRange = { generatorEngine.downloadRangeAyat() },
                    onStartSession = {
                        generatorEngine.startSession()
                        selectedTab = 1
                    },
                    onOpenSaveDialog = {
                        newSessionTitle = "${playerStatus.currentSurahNameBn} (${BanglaNumberUtils.toBanglaDigits(config.startAyah)}-${BanglaNumberUtils.toBanglaDigits(config.endAyah)})"
                        showSaveSessionDialog = true
                    }
                )
                1 -> GeneratorPlayerTab(
                    playerStatus = playerStatus,
                    config = config,
                    onPlay = { generatorEngine.resume() },
                    onPause = { generatorEngine.pause() },
                    onNext = { generatorEngine.nextAyah() },
                    onPrevious = { generatorEngine.previousAyah() },
                    onReplay = { generatorEngine.replayCurrentAyah() },
                    onSeek = { generatorEngine.seekTo(it) },
                    onTogglePronunciation = { generatorEngine.toggleShowPronunciation() },
                    onToggleTranslation = { generatorEngine.toggleShowTranslation() },
                    onCycleSpeed = {
                        val speeds = listOf(0.75f, 0.85f, 1.0f, 1.25f)
                        val nextIdx = (speeds.indexOf(config.playbackSpeed) + 1) % speeds.size
                        generatorEngine.setPlaybackSpeed(speeds[nextIdx])
                    },
                    onCycleRepeat = {
                        val repeats = listOf(1, 2, 3, 5, 7, 10, -1)
                        val nextIdx = (repeats.indexOf(config.ayahRepeatCount) + 1) % repeats.size
                        generatorEngine.setAyahRepeatCount(repeats[nextIdx])
                    },
                    onOpenSaveDialog = {
                        newSessionTitle = "${playerStatus.currentSurahNameBn} (${BanglaNumberUtils.toBanglaDigits(config.startAyah)}-${BanglaNumberUtils.toBanglaDigits(config.endAyah)})"
                        showSaveSessionDialog = true
                    }
                )
                2 -> SavedSessionsTab(
                    savedSessions = savedSessions,
                    onLoadSession = { session ->
                        generatorEngine.loadSession(session)
                        selectedTab = 0
                    },
                    onPlaySessionImmediately = { session ->
                        generatorEngine.loadSession(session)
                        generatorEngine.startSession()
                        selectedTab = 1
                    },
                    onDeleteSession = { generatorEngine.deleteSession(it) }
                )
            }
        }
    }

    // Surah Picker Sheet
    if (showSurahPickerSheet) {
        SurahPickerBottomSheet(
            selectedSurahNumber = config.surahNumber,
            onSelectSurah = { surah ->
                generatorEngine.loadSurahData(surah.number)
                showSurahPickerSheet = false
            },
            onDismiss = { showSurahPickerSheet = false }
        )
    }

    // Save Session Dialog
    if (showSaveSessionDialog) {
        AlertDialog(
            onDismissRequest = { showSaveSessionDialog = false },
            title = {
                Text(
                    text = "সেশন সংরক্ষণ করুন",
                    fontFamily = banglaFont,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {
                    Text(
                        text = "এই সেশনটির একটি সুন্দর নাম দিন যাতে পরে এক ট্যাপেই প্লে করতে পারেন:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = newSessionTitle,
                        onValueChange = { newSessionTitle = it },
                        label = { Text("সেশনের শিরোনাম", fontFamily = banglaFont) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("save_session_title_field"),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSessionTitle.isNotBlank()) {
                            generatorEngine.saveSession(newSessionTitle)
                            showSaveSessionDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                    modifier = Modifier.testTag("save_session_confirm_button")
                ) {
                    Text("সংরক্ষণ করুন", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveSessionDialog = false }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }

    // Info & Guidelines Dialog
    if (showInfoDialog) {
        AlertDialog(
            onDismissRequest = { showInfoDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF047857)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "তিলাওয়াত জেনারেটর গাইড",
                        fontFamily = banglaFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            text = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "পবিত্র কুরআন হিফজ, মুরাজা'আ (রিভিশন) এবং গভীর ধ্যানের জন্য এই টুলটি বিশেষভাবে ডিজাইন করা হয়েছে:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• মুরাজা'আ মোড: মুখস্থ সূরা পাকা করতে প্রতি আয়াত ৩ বার স্বাভাবিক গতিতে আবৃত্তি করে।",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• ধীর শিক্ষণ (হিফজ): নতুন আয়াত মুখস্থের জন্য প্রতি আয়াত ৭ বার পুনরাবৃত্তি এবং সাথে সাথে নিজে পড়ার ২-৩ সেকেন্ড বিরতি দেয়।",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• বিরতি সুবিধা: ক্বারীর তিলাওয়াতের পর বিরতিতে নিজে তিলাওয়াত বা বাংলা উচ্চারণ/অনুবাদ স্মরণ করতে পারবেন।",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "• অফলাইন সুবিধা: 'ডাউনলোড' বাটনে চাপ দিয়ে ইন্টারনেট ছাড়াই যেকোনো জায়গায় নিরবচ্ছিন্ন তিলাওয়াত উপভোগ করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = banglaFont
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showInfoDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                ) {
                    Text("বুঝেছি", fontFamily = banglaFont)
                }
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun GeneratorSetupTab(
    config: com.example.data.model.GeneratorConfig,
    playerStatus: com.example.data.model.GeneratorPlayerStatus,
    isDownloading: Boolean,
    downloadProgress: Float,
    onOpenSurahPicker: () -> Unit,
    onApplyPreset: (GeneratorPresetType) -> Unit,
    onSetAyahRange: (Int, Int) -> Unit,
    onSelectReciter: (QuranReciter) -> Unit,
    onSetAyahRepeat: (Int) -> Unit,
    onSetPauseSeconds: (Int) -> Unit,
    onSetSpeed: (Float) -> Unit,
    onSetRangeLoops: (Int) -> Unit,
    onSetSleepTimer: (Int) -> Unit,
    onDownloadRange: () -> Unit,
    onStartSession: () -> Unit,
    onOpenSaveDialog: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val totalSurahAyat = QuranSurahCatalog.all114Surahs.find { it.number == config.surahNumber }?.totalAyat ?: 7

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Smart Presets Carousel
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "স্মার্ট হিফজ ও অনুশীলন প্রিসেট",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "১-ট্যাপে সক্রিয়",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(GeneratorPresetType.entries) { preset ->
                        val isSelected = config.presetType == preset
                        Card(
                            onClick = { onApplyPreset(preset) },
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) preset.accentColor.copy(alpha = 0.12f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) preset.accentColor else Color.Transparent
                            ),
                            modifier = Modifier
                                .width(180.dp)
                                .testTag("preset_card_${preset.id}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = preset.emoji, fontSize = 20.sp)
                                    Surface(
                                        color = preset.accentColor.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text(
                                            text = preset.badgeBn,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = preset.accentColor,
                                                fontWeight = FontWeight.Bold
                                            ),
                                            fontFamily = banglaFont,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = preset.titleBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = preset.subtitleBn,
                                    style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    fontFamily = banglaFont,
                                    maxLines = 2,
                                    lineHeight = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Surah & Ayah Range Selector Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "সূরা ও আয়াত নির্বাচন",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Surah Picker Trigger Card
                    Surface(
                        onClick = onOpenSurahPicker,
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF047857).copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("surah_picker_trigger")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = Color(0xFF047857),
                                        shape = CircleShape,
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = BanglaNumberUtils.toBanglaDigits(config.surahNumber),
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = Color.White,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = playerStatus.currentSurahNameBn,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        fontFamily = banglaFont
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "মোট আয়াত: ${BanglaNumberUtils.toBanglaDigits(totalSurahAyat)}টি • সূরা বদলাতে ট্যাপ করুন",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    fontFamily = banglaFont
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = playerStatus.currentSurahNameAr,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowRight,
                                    contentDescription = null,
                                    tint = Color(0xFF047857)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Ayah Range Range Controls
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "আয়াত সীমা: ${BanglaNumberUtils.toBanglaDigits(config.startAyah)} হতে ${BanglaNumberUtils.toBanglaDigits(config.endAyah)}",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "(${BanglaNumberUtils.toBanglaDigits(config.endAyah - config.startAyah + 1)}টি আয়াত)",
                            style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF047857)),
                            fontFamily = banglaFont
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Range Slider
                    if (totalSurahAyat > 1) {
                        var sliderPosition by remember(config.startAyah, config.endAyah, totalSurahAyat) {
                            mutableStateOf(config.startAyah.toFloat()..config.endAyah.toFloat())
                        }
                        RangeSlider(
                            value = sliderPosition,
                            onValueChange = { range ->
                                sliderPosition = range
                                onSetAyahRange(range.start.toInt(), range.endInclusive.toInt())
                            },
                            valueRange = 1f..totalSurahAyat.toFloat(),
                            steps = (totalSurahAyat - 2).coerceAtLeast(0),
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF047857),
                                activeTrackColor = Color(0xFF047857),
                                inactiveTrackColor = Color(0xFF047857).copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ayah_range_slider")
                        )
                    }

                    // Quick Range Chips
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        FilterChip(
                            selected = config.startAyah == 1 && config.endAyah == totalSurahAyat,
                            onClick = { onSetAyahRange(1, totalSurahAyat) },
                            label = { Text("সম্পূর্ণ সূরা", fontFamily = banglaFont) },
                            colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f))
                        )
                        if (totalSurahAyat >= 5) {
                            FilterChip(
                                selected = config.startAyah == 1 && config.endAyah == 5,
                                onClick = { onSetAyahRange(1, 5) },
                                label = { Text("১-৫ আয়াত", fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f))
                            )
                        }
                        if (totalSurahAyat >= 10) {
                            FilterChip(
                                selected = config.startAyah == 1 && config.endAyah == 10,
                                onClick = { onSetAyahRange(1, 10) },
                                label = { Text("১-১০ আয়াত", fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f))
                            )
                        }
                        if (totalSurahAyat >= 15) {
                            val last10Start = (totalSurahAyat - 9).coerceAtLeast(1)
                            FilterChip(
                                selected = config.startAyah == last10Start && config.endAyah == totalSurahAyat,
                                onClick = { onSetAyahRange(last10Start, totalSurahAyat) },
                                label = { Text("শেষ ১০ আয়াত", fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f))
                            )
                        }
                        if (config.surahNumber == 2) {
                            FilterChip(
                                selected = config.startAyah == 255 && config.endAyah == 255,
                                onClick = { onSetAyahRange(255, 255) },
                                label = { Text("আয়াতুল কুরসী (২৫৫)", fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f))
                            )
                        }
                    }
                }
            }
        }

        // Reciter Selection Carousel
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "ক্বারী নির্বাচন (বিশ্ববিখ্যাত ক্বারীবৃন্দ)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = null,
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(QuranReciter.entries) { reciter ->
                            val isSelected = config.reciter == reciter
                            Card(
                                onClick = { onSelectReciter(reciter) },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) Color(0xFF047857).copy(alpha = 0.12f)
                                    else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) Color(0xFF047857) else Color.Transparent
                                ),
                                modifier = Modifier
                                    .width(220.dp)
                                    .testTag("reciter_chip_${reciter.id}")
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                            shape = CircleShape,
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.GraphicEq,
                                                    contentDescription = null,
                                                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = reciter.nameBn,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            ),
                                            fontFamily = banglaFont,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = reciter.audioQuality,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Repetition, Intervals & Speed Controls
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "পুনরাবৃত্তি, বিরতি ও গতি নিয়ন্ত্রণ",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Ayah Repeat count
                    Text(
                        text = "প্রতি আয়াতের পুনরাবৃত্তি সংখ্যা:",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val repeats = listOf(1, 2, 3, 5, 7, 10, -1)
                        repeats.forEach { count ->
                            val label = if (count == -1) "অবিরাম (∞)" else "${BanglaNumberUtils.toBanglaDigits(count)} বার"
                            FilterChip(
                                selected = config.ayahRepeatCount == count,
                                onClick = { onSetAyahRepeat(count) },
                                label = { Text(label, fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF047857),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Pause interval between ayahs
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "আয়াত মধ্যবর্তী বিরতি (নিজে তিলাওয়াত করার সুযোগ):",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val pauses = listOf(0, 1, 2, 3, 5)
                            pauses.forEach { sec ->
                                val label = if (sec == 0) "০ সে. (বিরতিহীন)" else "${BanglaNumberUtils.toBanglaDigits(sec)} সেকেন্ড"
                                FilterChip(
                                    selected = config.pauseBetweenAyatSeconds == sec,
                                    onClick = { onSetPauseSeconds(sec) },
                                    label = { Text(label, fontFamily = banglaFont) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(0xFF0284C7),
                                        selectedLabelColor = Color.White
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Playback speed
                    Text(
                        text = "তিলাওয়াতের গতি (Speed):",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val speeds = listOf(0.75f, 0.85f, 1.0f, 1.25f)
                        speeds.forEach { speed ->
                            val label = when (speed) {
                                0.75f -> "০.৭৫x (ধীর)"
                                0.85f -> "০.৮৫x (শান্ত)"
                                1.0f -> "১.০x (স্বাভাবিক)"
                                else -> "১.২৫x (দ্রুত)"
                            }
                            FilterChip(
                                selected = config.playbackSpeed == speed,
                                onClick = { onSetSpeed(speed) },
                                label = { Text(label, fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF7C3AED),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Range Loop count & Sleep timer
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "সম্পূর্ণ রেঞ্জ লুপ:",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf(1, 2, 3, -1).forEach { loop ->
                                    val label = if (loop == -1) "∞" else "${BanglaNumberUtils.toBanglaDigits(loop)}x"
                                    FilterChip(
                                        selected = config.rangeLoopCount == loop,
                                        onClick = { onSetRangeLoops(loop) },
                                        label = { Text(label, fontFamily = banglaFont) }
                                    )
                                }
                            }
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "স্লিপ টাইমার:",
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                listOf(0, 15, 30, 60).forEach { mins ->
                                    val label = if (mins == 0) "বন্ধ" else "${BanglaNumberUtils.toBanglaDigits(mins)} মি."
                                    FilterChip(
                                        selected = config.sleepTimerMinutes == mins,
                                        onClick = { onSetSleepTimer(mins) },
                                        label = { Text(label, fontFamily = banglaFont) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Offline Caching & Session Save Buttons
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF047857).copy(alpha = 0.06f)
                ),
                border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "অফলাইন ক্যাশিং ও স্টোরেজ",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "ডাউনলোড হয়েছে: ${BanglaNumberUtils.toBanglaDigits(playerStatus.cachedAyatCount)}/${BanglaNumberUtils.toBanglaDigits(config.endAyah - config.startAyah + 1)} আয়াত",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                fontFamily = banglaFont
                            )
                        }
                        if (isDownloading) {
                            CircularProgressIndicator(
                                progress = { downloadProgress },
                                modifier = Modifier.size(36.dp),
                                color = Color(0xFF047857)
                            )
                        } else {
                            OutlinedButton(
                                onClick = onDownloadRange,
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, Color(0xFF047857)),
                                modifier = Modifier.testTag("download_range_button")
                            ) {
                                Icon(
                                    imageVector = if (playerStatus.cachedAyatCount >= (config.endAyah - config.startAyah + 1)) Icons.Default.DownloadDone else Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (playerStatus.cachedAyatCount >= (config.endAyah - config.startAyah + 1)) "সংরক্ষিত" else "ডাউনলোড",
                                    color = Color(0xFF047857),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                    if (isDownloading) {
                        Spacer(modifier = Modifier.height(8.dp))
                        LinearProgressIndicator(
                            progress = { downloadProgress },
                            color = Color(0xFF047857),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Action Buttons: Start Session & Save Preset
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onOpenSaveDialog,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Color(0xFF047857)),
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .testTag("save_session_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = null,
                        tint = Color(0xFF047857)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "সেশন সেভ",
                        fontFamily = banglaFont,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    )
                }

                Button(
                    onClick = onStartSession,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                    modifier = Modifier
                        .weight(1.5f)
                        .height(52.dp)
                        .testTag("start_tilawat_session_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "তিলাওয়াত শুরু করুন",
                        fontFamily = banglaFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun GeneratorPlayerTab(
    playerStatus: com.example.data.model.GeneratorPlayerStatus,
    config: com.example.data.model.GeneratorConfig,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onNext: () -> Unit,
    onPrevious: () -> Unit,
    onReplay: () -> Unit,
    onSeek: (Int) -> Unit,
    onTogglePronunciation: () -> Unit,
    onToggleTranslation: () -> Unit,
    onCycleSpeed: () -> Unit,
    onCycleRepeat: () -> Unit,
    onOpenSaveDialog: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Status & Session Tracker Banner
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF047857).copy(alpha = 0.08f)
            ),
            border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${playerStatus.currentSurahNameBn} • আয়াত ${BanglaNumberUtils.toBanglaDigits(playerStatus.currentAyahNumber)}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "ক্বারী: ${config.reciter.nameBn}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            fontFamily = banglaFont
                        )
                    }

                    // Loop count pills
                    Surface(
                        color = Color(0xFF047857),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        val repeatLabel = if (config.ayahRepeatCount == -1) "অবিরাম লুপ"
                        else "আবৃত্তি: ${BanglaNumberUtils.toBanglaDigits(playerStatus.currentAyahRepeatIndex)}/${BanglaNumberUtils.toBanglaDigits(config.ayahRepeatCount)}"
                        Text(
                            text = repeatLabel,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            ),
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                // If Interval Pause is Active
                AnimatedVisibility(visible = playerStatus.isPauseIntervalActive) {
                    Surface(
                        color = Color(0xFF0284C7).copy(alpha = 0.15f),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.HourglassTop,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "এখন আপনার পড়ার পালা... বিরতি বাকি: ${BanglaNumberUtils.toBanglaDigits(playerStatus.pauseRemainingSeconds)} সেকেন্ড",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0284C7)
                                ),
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Active Ayah Content Box (Arabic + Pronunciation + Meaning)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header with Memorization Toggle Eye Icons
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "আয়াত নং ${BanglaNumberUtils.toBanglaDigits(playerStatus.currentAyahNumber)}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.Bold
                            ),
                            fontFamily = banglaFont
                        )
                        Row {
                            IconButton(
                                onClick = onTogglePronunciation,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (config.showPronunciation) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "উচ্চারণ প্রদর্শন/লুকান",
                                    tint = if (config.showPronunciation) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            IconButton(
                                onClick = onToggleTranslation,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = if (config.showTranslation) Icons.Default.FormatQuote else Icons.Default.VisibilityOff,
                                    contentDescription = "অনুবাদ প্রদর্শন/লুকান",
                                    tint = if (config.showTranslation) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Arabic Text
                item {
                    val arabic = playerStatus.activeAyah?.arabicText ?: "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ"
                    Text(
                        text = arabic,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = 26.sp,
                            lineHeight = 44.sp,
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Pronunciation
                if (config.showPronunciation) {
                    item {
                        val pronunciation = playerStatus.activeAyah?.pronunciationBn
                        if (!pronunciation.isNullOrBlank()) {
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = pronunciation,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Color(0xFF1E293B),
                                        lineHeight = 22.sp
                                    ),
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                    }
                }

                // Translation
                if (config.showTranslation) {
                    item {
                        val translation = playerStatus.activeAyah?.translationBn
                        if (!translation.isNullOrBlank()) {
                            Column {
                                Text(
                                    text = "অনুবাদ:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF047857),
                                        fontWeight = FontWeight.Bold
                                    ),
                                    fontFamily = banglaFont
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = translation,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 22.sp
                                    ),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress Bar & Duration
        Column(modifier = Modifier.fillMaxWidth()) {
            val progress = if (playerStatus.durationMs > 0) {
                playerStatus.currentPositionMs.toFloat() / playerStatus.durationMs.toFloat()
            } else 0f

            Slider(
                value = progress.coerceIn(0f, 1f),
                onValueChange = { frac ->
                    val pos = (frac * playerStatus.durationMs).toInt()
                    onSeek(pos)
                },
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF047857),
                    activeTrackColor = Color(0xFF047857),
                    inactiveTrackColor = Color(0xFF047857).copy(alpha = 0.2f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatMs(playerStatus.currentPositionMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatMs(playerStatus.durationMs),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Player Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onPrevious,
                modifier = Modifier.testTag("player_prev_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SkipPrevious,
                    contentDescription = "পূর্ববর্তী আয়াত",
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = onReplay,
                modifier = Modifier.testTag("player_replay_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "পুনরায় শুনুন",
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF047857)
                )
            }

            // Big Play/Pause Button
            Surface(
                onClick = {
                    if (playerStatus.isPlaying) onPause() else onPlay()
                },
                shape = CircleShape,
                color = Color(0xFF047857),
                shadowElevation = 4.dp,
                modifier = Modifier
                    .size(64.dp)
                    .testTag("player_play_pause_button")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (playerStatus.isBuffering) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(30.dp),
                            strokeWidth = 3.dp
                        )
                    } else {
                        Icon(
                            imageVector = if (playerStatus.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (playerStatus.isPlaying) "বিরতি দিন" else "চালিয়ে যান",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            IconButton(
                onClick = onNext,
                modifier = Modifier.testTag("player_next_button")
            ) {
                Icon(
                    imageVector = Icons.Default.SkipNext,
                    contentDescription = "পরবর্তী আয়াত",
                    modifier = Modifier.size(32.dp)
                )
            }

            IconButton(
                onClick = onOpenSaveDialog,
                modifier = Modifier.testTag("player_save_button")
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = "সংরক্ষণ করুন",
                    modifier = Modifier.size(26.dp),
                    tint = Color(0xFF047857)
                )
            }
        }

        // Quick Cycle Pills: Speed & Repeat
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = true,
                onClick = onCycleSpeed,
                label = {
                    Text(
                        text = "গতি: ${config.playbackSpeed}x",
                        fontFamily = banglaFont,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF7C3AED).copy(alpha = 0.15f),
                    selectedLabelColor = Color(0xFF7C3AED)
                )
            )

            Spacer(modifier = Modifier.width(10.dp))

            FilterChip(
                selected = true,
                onClick = onCycleRepeat,
                label = {
                    val repText = if (config.ayahRepeatCount == -1) "অবিরাম" else "${BanglaNumberUtils.toBanglaDigits(config.ayahRepeatCount)} বার"
                    Text(
                        text = "লুপ: $repText",
                        fontFamily = banglaFont,
                        fontSize = 12.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f),
                    selectedLabelColor = Color(0xFF047857)
                )
            )
        }
    }
}

@Composable
private fun SavedSessionsTab(
    savedSessions: List<SavedGeneratorSession>,
    onLoadSession: (SavedGeneratorSession) -> Unit,
    onPlaySessionImmediately: (SavedGeneratorSession) -> Unit,
    onDeleteSession: (String) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    if (savedSessions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.BookmarkAdd,
                    contentDescription = null,
                    tint = Color(0xFF047857).copy(alpha = 0.5f),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "কোনো সংরক্ষিত সেশন নেই",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "স্মার্ট সেটআপ থেকে যেকোনো সূরা ও আয়াত নির্বাচন করে 'সেশন সেভ' চাপুন।",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    ),
                    fontFamily = banglaFont
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(savedSessions, key = { it.id }) { session ->
                val surah = QuranSurahCatalog.all114Surahs.find { it.number == session.surahNumber }
                val reciter = QuranReciter.entries.find { it.id == session.reciterId } ?: QuranReciter.MISHARY_ALAFASY

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("saved_session_item_${session.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = session.titleBn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${surah?.nameBn ?: "সূরা"} • আয়াত: ${BanglaNumberUtils.toBanglaDigits(session.startAyah)}-${BanglaNumberUtils.toBanglaDigits(session.endAyah)} • ${reciter.nameBn}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                fontFamily = banglaFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "রিপিট: ${if (session.ayahRepeatCount == -1) "অবিরাম" else "${BanglaNumberUtils.toBanglaDigits(session.ayahRepeatCount)} বার"} • বিরতি: ${BanglaNumberUtils.toBanglaDigits(session.pauseBetweenAyatSeconds)} সে. • গতি: ${session.playbackSpeed}x",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF047857)),
                                fontFamily = banglaFont
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { onPlaySessionImmediately(session) },
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFF047857), CircleShape)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "সরাসরি শুনুন",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            IconButton(
                                onClick = { onDeleteSession(session.id) },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "মুছে ফেলুন",
                                    tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SurahPickerBottomSheet(
    selectedSurahNumber: Int,
    onSelectSurah: (QuranSurah) -> Unit,
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var searchQuery by remember { mutableStateOf("") }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranSurahCatalog.all114Surahs
        } else {
            val q = searchQuery.trim().lowercase()
            QuranSurahCatalog.all114Surahs.filter {
                it.number.toString().contains(q) ||
                BanglaNumberUtils.toBanglaDigits(it.number).contains(q) ||
                it.nameBn.lowercase().contains(q) ||
                it.nameEn.lowercase().contains(q) ||
                it.nameAr.contains(q)
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "সূরা নির্বাচন করুন (১১৪টি সূরা)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("সূরার নাম বা নম্বর দিয়ে খুঁজুন...", fontFamily = banglaFont) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("surah_search_field"),
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(filteredSurahs, key = { it.number }) { surah ->
                    val isSelected = surah.number == selectedSurahNumber
                    Surface(
                        onClick = { onSelectSurah(surah) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) Color(0xFF047857).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF047857) else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("surah_item_${surah.number}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                                    shape = CircleShape,
                                    modifier = Modifier.size(30.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = BanglaNumberUtils.toBanglaDigits(surah.number),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = surah.nameBn,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = "${surah.meaningBn} • ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)} আয়াত • ${surah.revelationType}",
                                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                            Text(
                                text = surah.nameAr,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.onSurface,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun formatMs(ms: Int): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d", minutes, seconds)
}
