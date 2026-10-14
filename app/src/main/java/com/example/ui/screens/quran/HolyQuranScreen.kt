package com.example.ui.screens.quran

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.RepeatOne
import androidx.compose.material.icons.filled.Tune
import com.example.data.model.QuranAudioLoopMode
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.datasource.QuranTafsirAndTranslationProvider
import com.example.data.datasource.SurahShaneNuzulCatalog
import com.example.data.model.DownloadProgressState
import com.example.data.model.QuranAyah
import com.example.data.model.QuranReciter
import com.example.data.model.QuranSettings
import com.example.data.model.QuranSurah
import com.example.data.model.QuranTafsirSource
import com.example.data.model.QuranTranslator
import com.example.data.repository.QuranRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAudioManager
import com.example.util.QuranBengaliPhoneticTransliteration
import com.example.util.QuranSettingsManager
import kotlinx.coroutines.launch

enum class QuranFilter(val titleBn: String) {
    ALL("সকল (১১৪)"),
    MAKKI("মাক্কী (৮৬)"),
    MADANI("মাদানী (২৮)"),
    BOOKMARKS("বুকমার্ক"),
    DOWNLOADED("অফলাইন")
}

@Composable
fun HolyQuranScreen(
    quranRepository: QuranRepository,
    audioManager: QuranAudioManager,
    initialSurahNumber: Int? = null,
    onOpenQuranTopics: (() -> Unit)? = null,
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsManager = remember { QuranSettingsManager(context) }
    val settings by settingsManager.settings.collectAsState()

    var activeSurahNumber by remember { mutableStateOf(initialSurahNumber) }
    val selectedTranslator = remember(settings.preferredTranslator) {
        QuranTranslator.entries.find { it.id == settings.preferredTranslator } ?: QuranTranslator.DR_ZAKARIA
    }
    var showReciterDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAudioManagerDialog by remember { mutableStateOf(false) }

    val reciter by audioManager.selectedReciter.collectAsState()
    val playerState by audioManager.playerState.collectAsState()
    val downloadStates by audioManager.downloadState.collectAsState()

    // Sync preferred reciter from settings
    LaunchedEffect(settings.defaultReciterId) {
        val preferred = QuranReciter.entries.find { it.id == settings.defaultReciterId }
        if (preferred != null && audioManager.selectedReciter.value.id != preferred.id) {
            audioManager.selectReciter(preferred)
        }
    }

    LaunchedEffect(Unit) {
        quranRepository.initializeDatabaseIfNeeded()
    }

    if (activeSurahNumber == null) {
        // Specimen Screenshot 1: Surah Index / Catalog
        HolyQuranIndexScreen(
            quranRepository = quranRepository,
            audioManager = audioManager,
            onSurahSelected = { surah ->
                activeSurahNumber = surah.number
            },
            onOpenReciterPicker = { showReciterDialog = true },
            onOpenSettings = { showSettingsDialog = true },
            onOpenAudioManager = { showAudioManagerDialog = true },
            onOpenQuranTopics = onOpenQuranTopics,
            onNavigateBack = onNavigateBack,
            contentPadding = contentPadding
        )
    } else {
        // Specimen Screenshot 2: Surah Reading & Tafsir Page
        val surah = remember(activeSurahNumber) {
            QuranSurahCatalog.all114Surahs.find { it.number == activeSurahNumber }
                ?: QuranSurah(activeSurahNumber!!, "", "সূরা", "", "", "", "মাক্কী", 7)
        }

        SurahDetailScreen(
            surah = surah,
            quranRepository = quranRepository,
            audioManager = audioManager,
            settingsManager = settingsManager,
            selectedTranslator = selectedTranslator,
            onSelectTranslator = { settingsManager.updatePreferredTranslator(it.id) },
            onOpenReciterPicker = { showReciterDialog = true },
            onOpenSettings = { showSettingsDialog = true },
            onOpenAudioManager = { showAudioManagerDialog = true },
            onBackToIndex = { activeSurahNumber = null },
            onSelectSurahNumber = { activeSurahNumber = it },
            contentPadding = contentPadding
        )
    }

    if (showReciterDialog) {
        ReciterSelectionDialog(
            currentReciter = reciter,
            onSelectReciter = { newReciter ->
                settingsManager.updateDefaultReciter(newReciter.id)
                audioManager.selectReciter(newReciter)
                showReciterDialog = false
                Toast.makeText(context, "ক্বারী নির্বাচিত: ${newReciter.nameBn}", Toast.LENGTH_SHORT).show()
            },
            onDismiss = { showReciterDialog = false }
        )
    }

    if (showSettingsDialog) {
        QuranSettingsDialog(
            settingsManager = settingsManager,
            audioManager = audioManager,
            onDismiss = { showSettingsDialog = false },
            onOpenAudioManager = {
                showSettingsDialog = false
                showAudioManagerDialog = true
            }
        )
    }

    if (showAudioManagerDialog) {
        QuranAudioManagerDialog(
            audioManager = audioManager,
            onDismiss = { showAudioManagerDialog = false }
        )
    }
}

/**
 * Specimen Screenshot 1 Layout:
 * Top Header: Green Holy Quran Book + "আল-কুরআন" + Subtitle
 * Search Bar: "সূরার নাম, নম্বর বা ধরন দিয়ে খুঁজুন..."
 * Table Header: সূরার নাম | ধরন | আয়াত
 * Surah Items: ১. আল-ফাতিহা | Al-Fatiha | الفاتحة ... সূচনা | The Opening ... 🕋 মাক্কী ... ৭
 */
@Composable
fun HolyQuranIndexScreen(
    quranRepository: QuranRepository,
    audioManager: QuranAudioManager,
    onSurahSelected: (QuranSurah) -> Unit,
    onOpenReciterPicker: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAudioManager: () -> Unit,
    onOpenQuranTopics: (() -> Unit)? = null,
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues
) {
    val context = LocalContext.current
    val surahs by quranRepository.getAllSurahs().collectAsState(initial = QuranSurahCatalog.all114Surahs)
    val bookmarks by quranRepository.getBookmarkedAyahs().collectAsState(initial = emptyList())
    val playerState by audioManager.playerState.collectAsState()
    val reciter by audioManager.selectedReciter.collectAsState()
    val batchState by audioManager.batchDownloadState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(QuranFilter.ALL) }

    val filteredSurahs = remember(surahs, searchQuery, selectedFilter, bookmarks) {
        surahs.filter { surah ->
            val matchesFilter = when (selectedFilter) {
                QuranFilter.ALL -> true
                QuranFilter.MAKKI -> surah.revelationType == "মাক্কী"
                QuranFilter.MADANI -> surah.revelationType == "মাদানী"
                QuranFilter.BOOKMARKS -> bookmarks.any { it.surahNumber == surah.number }
                QuranFilter.DOWNLOADED -> audioManager.isSurahDownloaded(surah.number)
            }
            if (!matchesFilter) return@filter false

            if (searchQuery.isBlank()) return@filter true

            val q = searchQuery.trim().lowercase()
            surah.nameBn.lowercase().contains(q) ||
                surah.nameEn.lowercase().contains(q) ||
                surah.nameAr.contains(q) ||
                surah.meaningBn.lowercase().contains(q) ||
                surah.meaningEn.lowercase().contains(q) ||
                surah.number.toString() == q ||
                BanglaNumberUtils.toBanglaDigits(surah.number) == q ||
                surah.revelationType.contains(q)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(bottom = contentPadding.calculateBottomPadding())
    ) {
        // Top Action Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ফিরে যান",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 4.dp)
                ) {
                    Text(
                        text = "আল-কুরআন",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "অনুবাদ, তাফসীর ও তিলাওয়াত",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Compact action buttons with ample breathing room
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onOpenReciterPicker,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("btn_reciter_picker")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "ক্বারী নির্বাচন",
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onOpenAudioManager,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("btn_audio_manager")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "কুরআন অডিও ডাউনলোড ম্যানেজার",
                            tint = IslamicGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(38.dp)
                            .testTag("btn_quran_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "কুরআন সেটিংস",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Active Batch Download Banner (if running)
        if (batchState.isBatchRunning) {
            Surface(
                color = IslamicGreen.copy(alpha = 0.12f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAudioManager() }
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        progress = { batchState.overallPercent / 100f },
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = IslamicGreen
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "১১৪ সূরা ডাউনলোড হচ্ছে: ${BanglaNumberUtils.toBanglaDigits(batchState.completedSurahsCount)}/১১৪ (${BanglaNumberUtils.toBanglaDigits(batchState.overallPercent)}%)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "বিস্তারিত",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = IslamicGreen,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Search Bar (Immediately below Top Bar without redundant extra spacing)
        PaddingValues(horizontal = 16.dp, vertical = 6.dp).let {
            Box(modifier = Modifier.padding(it)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "সূরার নাম, নম্বর বা ধরন দিয়ে খুঁজুন...",
                            style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.outline)
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IslamicGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("quran_search_bar")
                )
            }
        }

        // Quick Filter Chips (Scrollable horizontally for 100% responsiveness on all screen sizes)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (onOpenQuranTopics != null) {
                FilterChip(
                    selected = false,
                    onClick = onOpenQuranTopics,
                    label = {
                        Text(
                            text = "📖 বিষয়ভিত্তিক আয়াত (৫৪)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen
                            )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = IslamicGreen.copy(alpha = 0.14f)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = false,
                        borderColor = IslamicGreen
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
            QuranFilter.entries.forEach { filter ->
                val selected = selectedFilter == filter
                FilterChip(
                    selected = selected,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter.titleBn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicGreen.copy(alpha = 0.15f),
                        selectedLabelColor = IslamicGreen
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = if (selected) IslamicGreen else MaterialTheme.colorScheme.outlineVariant
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Table Header: সূরার নাম (Left) | ধরন (Middle) | আয়াত (Right)
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "সূরার নাম",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "ধরন",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.width(68.dp),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "আয়াত",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.width(48.dp),
                    textAlign = TextAlign.End
                )
            }
        }

        // Surah Items List
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
        ) {
            if (filteredSurahs.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "কোনো সূরা পাওয়া যায়নি",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.outline
                            )
                        )
                    }
                }
            } else {
                items(filteredSurahs, key = { it.number }) { surah ->
                    val isSurahBookmarked = bookmarks.any { it.surahNumber == surah.number }
                    SurahListItemRow(
                        surah = surah,
                        isDownloaded = audioManager.isSurahDownloaded(surah.number),
                        isCurrentlyPlaying = playerState.isPlaying && playerState.surahNumber == surah.number,
                        isBookmarked = isSurahBookmarked,
                        onSurahClick = { onSurahSelected(surah) }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
                        thickness = 0.8.dp
                    )
                }
            }
        }

        // Persistent Revamped Audio Player if playing or paused with active surah
        if (playerState.isPlaying || playerState.isBuffering || playerState.surahNumber != null) {
            MiniQuranAudioPlayer(
                playerState = playerState,
                reciter = reciter,
                totalAyat = 0,
                autoScrollEnabled = false,
                onToggleAutoScroll = null,
                onPlayPause = {
                    if (playerState.isPlaying) audioManager.pause() else audioManager.resume()
                },
                onRestartFromBeginning = { audioManager.restartFromBeginning() },
                onPreviousAyah = { audioManager.playPreviousAyah() },
                onNextAyah = { audioManager.playNextAyah() },
                onSeekForward = { audioManager.seekForward(5000) },
                onSeekBackward = { audioManager.seekBackward(5000) },
                onToggleLoopMode = { audioManager.toggleLoopMode() },
                onSetSpeed = { speed -> audioManager.setPlaybackSpeed(speed) },
                onSeekTo = { pos -> audioManager.seekTo(pos) },
                onOpenReciterSelector = onOpenReciterPicker,
                onStop = { audioManager.stop() }
            )
        }
    }
}

@Composable
fun SurahListItemRow(
    surah: QuranSurah,
    isDownloaded: Boolean,
    isCurrentlyPlaying: Boolean,
    isBookmarked: Boolean = false,
    onSurahClick: () -> Unit
) {
    val serialBn = BanglaNumberUtils.toBanglaDigits(surah.number)
    val ayatsBn = BanglaNumberUtils.toBanglaDigits(surah.totalAyat)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSurahClick)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: Serial + Surah Name (Line 1)
        // Subtitle: English Name & Meaning (Line 2 below)
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$serialBn. সূরা ${surah.nameBn}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isCurrentlyPlaying) IslamicGreen else MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis
                )
                if (isBookmarked) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "বুকমার্ক করা",
                        tint = IslamicGold,
                        modifier = Modifier.size(15.dp)
                    )
                }
                if (isDownloaded) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "ডাউনলোডকৃত",
                        tint = IslamicGreen,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${surah.nameEn} (${surah.nameAr}) • ${surah.meaningBn}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Middle Column: Revelation Type Badge (মাক্কী / মাদানী)
        Box(
            modifier = Modifier
                .width(68.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    if (surah.revelationType == "মাক্কী")
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                    else
                        MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                )
                .padding(horizontal = 4.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (surah.revelationType == "মাক্কী") "🕋 মাক্কী" else "🕌 মাদানী",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        // Right Column: Total Ayats in Bengali digits
        Text(
            text = ayatsBn,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.End
        )
    }
}

/**
 * Specimen Screenshot 2 Layout (Surah Reading & Tafsir Page):
 * Header:
 *   - Back Arrow
 *   - Title: সূরা আল ফাতিহা (الفاتحة) | সূচনা
 *   - Badges: মাক্কী | মোট আয়াত: ৭ | ▶ অডিও | ⬇ ডাউনলোড
 * Bismillah Card:
 *   - Soft mint green background
 *   - Arabic calligraphy: بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ
 *   - Bengali: পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে
 * Ayah Card:
 *   - Action Pills Bar: 📖 তাফসীর | 🔗 শেয়ার | [১] | 🔖 বুকমার্ক | ▶ অডিও
 *   - Arabic Ayah Text (large Uthmani right-aligned)
 *   - Bengali Pronunciation (soft pill container)
 *   - Bengali Translation (Dr. Abu Bakr Zakaria / Taisirul Quran)
 *   - Expandable Scholarly Tafsir Card
 */
@Composable
fun SurahDetailScreen(
    surah: QuranSurah,
    quranRepository: QuranRepository,
    audioManager: QuranAudioManager,
    settingsManager: QuranSettingsManager,
    selectedTranslator: QuranTranslator,
    onSelectTranslator: (QuranTranslator) -> Unit,
    onOpenReciterPicker: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenAudioManager: () -> Unit,
    onBackToIndex: () -> Unit,
    onSelectSurahNumber: (Int) -> Unit = {},
    contentPadding: PaddingValues
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settings by settingsManager.settings.collectAsState()
    var showSurahQuickSwitchDialog by remember { mutableStateOf(false) }

    val activity = context as? android.app.Activity
    DisposableEffect(settings.keepScreenAwake) {
        if (settings.keepScreenAwake) {
            activity?.window?.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
        onDispose {
            activity?.window?.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    val ayahsState by quranRepository.getAyahsForSurah(surah.number).collectAsState(initial = emptyList())
    val bookmarks by quranRepository.getBookmarkedAyahs().collectAsState(initial = emptyList())
    val playerState by audioManager.playerState.collectAsState()
    val downloadStates by audioManager.downloadState.collectAsState()
    val currentDownload = downloadStates[surah.number]
    val isDownloaded = audioManager.isSurahDownloaded(surah.number)

    val isSurahBookmarked = remember(bookmarks, surah.number) {
        bookmarks.any { it.id == "surah:${surah.number}" || (it.surahNumber == surah.number && it.ayahNumber == 0) }
    }

    // Expanded Tafsir state per Ayah number
    var expandedTafsirs by remember { mutableStateOf(setOf<Int>()) }

    val isFullSurahPlaying = playerState.isPlaying && playerState.surahNumber == surah.number

    val listState = rememberLazyListState()
    LaunchedEffect(playerState.activeAyahNumber, playerState.isPlaying, playerState.surahNumber, surah.number, ayahsState.size, settings.autoScrollWithAudio) {
        val activeAyah = playerState.activeAyahNumber
        if (playerState.surahNumber == surah.number && activeAyah != null && settings.autoScrollWithAudio) {
            val targetIndex = if (activeAyah == 0) {
                // Scroll to Bismillah card (Index 2 in LazyColumn: Item 0 is HeaderCard, Item 1 is ShaneNuzulCard)
                2
            } else {
                // Surah 1 has no separate BismillahBannerCard (Item 0: HeaderCard, Item 1: ShaneNuzulCard, Item 2: Ayah 1)
                // Surahs 2-114 have BismillahBannerCard (Item 0: HeaderCard, Item 1: ShaneNuzulCard, Item 2: Bismillah, Item 3: Ayah 1)
                val baseOffset = if (surah.number != 1) 2 else 1
                (baseOffset + activeAyah).coerceIn(0, (ayahsState.size + baseOffset).coerceAtLeast(0))
            }
            try {
                listState.animateScrollToItem(index = targetIndex, scrollOffset = -60)
            } catch (_: Exception) {
                listState.scrollToItem(targetIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(bottom = contentPadding.calculateBottomPadding())
    ) {
        // Clean, Compact Surah Top Bar & Switcher
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top App Bar: Back Button, Surah Name Dropdown, Prev/Next, and Bookmark
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBackToIndex,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "সূচিপত্রে ফিরে যান",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(4.dp))

                    // Quick Surah Dropdown Selector in Top Bar
                    Surface(
                        onClick = { showSurahQuickSwitchDialog = true },
                        shape = RoundedCornerShape(10.dp),
                        color = IslamicGreen.copy(alpha = 0.08f),
                        border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.25f)),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_top_bar_surah_dropdown")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "সূরা ${surah.nameBn}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "সূরা পরিবর্তন ড্রপডাউন",
                                        tint = IslamicGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Text(
                                    text = "${surah.revelationType} • আয়াত: ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = IslamicGreen
                                    ),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Surface(
                                color = IslamicGreen,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.padding(start = 4.dp)
                            ) {
                                Text(
                                    text = "${BanglaNumberUtils.toBanglaDigits(surah.number)}/১১৪",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontSize = 11.sp
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(2.dp))

                    // Previous Surah Button
                    IconButton(
                        onClick = {
                            if (surah.number > 1) {
                                onSelectSurahNumber(surah.number - 1)
                            }
                        },
                        enabled = surah.number > 1,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "পূর্ববর্তী সূরা",
                            tint = if (surah.number > 1) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Next Surah Button
                    IconButton(
                        onClick = {
                            if (surah.number < 114) {
                                onSelectSurahNumber(surah.number + 1)
                            }
                        },
                        enabled = surah.number < 114,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "পরবর্তী সূরা",
                            tint = if (surah.number < 114) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Quran Settings Button in Top Bar
                    IconButton(
                        onClick = onOpenSettings,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("btn_top_bar_quran_settings")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "কুরআন সেটিংস",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    // Bookmark Button for Surah Itself in Top Bar
                    IconButton(
                        onClick = {
                            scope.launch {
                                quranRepository.toggleSurahBookmark(surah)
                                val msg = if (isSurahBookmarked) "সূরা ${surah.nameBn} বুকমার্ক থেকে সরানো হয়েছে" else "সূরা ${surah.nameBn} বুকমার্কে যুক্ত করা হয়েছে"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("btn_top_bar_surah_bookmark")
                    ) {
                        Icon(
                            imageVector = if (isSurahBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isSurahBookmarked) "সূরা বুকমার্ক থেকে সরান" else "সূরা বুকমার্ক করুন",
                            tint = if (isSurahBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Ayah List with Surah Header Card and Bismillah Card
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 24.dp)
        ) {
            // Item 1: Revamped Beautiful Surah Header Card (Organized from blue marked area)
            item {
                SurahHeaderCard(
                    surah = surah,
                    isFullSurahPlaying = isFullSurahPlaying,
                    isDownloaded = isDownloaded,
                    currentDownload = currentDownload,
                    isSurahBookmarked = isSurahBookmarked,
                    onToggleSurahBookmark = {
                        scope.launch {
                            quranRepository.toggleSurahBookmark(surah)
                            val msg = if (isSurahBookmarked) "সূরা ${surah.nameBn} বুকমার্ক থেকে সরানো হয়েছে" else "সূরা ${surah.nameBn} বুকমার্কে যুক্ত করা হয়েছে"
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    },
                    onPlayFullSurah = {
                        if (isFullSurahPlaying) {
                            audioManager.pause()
                        } else {
                            audioManager.playSurah(
                                surahNumber = surah.number,
                                surahNameBn = surah.nameBn,
                                totalAyat = surah.totalAyat,
                                restartFromStart = true
                            )
                        }
                    },
                    onDownloadSurah = {
                        audioManager.downloadSurahAudio(surah.number) { success ->
                            if (success) {
                                Toast.makeText(context, "সূরা ${surah.nameBn} অফলাইনে ডাউনলোড সম্পন্ন হয়েছে!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "ডাউনলোড ব্যর্থ হয়েছে। ইন্টারনেট সংযোগ চেক করুন।", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onOfflineInfo = {
                        Toast.makeText(context, "এই সূরাটি অফলাইনে সংরক্ষিত আছে।", Toast.LENGTH_SHORT).show()
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Item 2: শানে নযুল ও পরিচিতি (Shan-e-Nuzul Card for every Surah)
            item {
                val shaneNuzul = remember(surah.number) {
                    SurahShaneNuzulCatalog.getShaneNuzul(context, surah.number)
                }
                SurahShaneNuzulCard(
                    surah = surah,
                    shaneNuzul = shaneNuzul,
                    initiallyExpanded = false
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Bismillah Card (For Surahs 2-114, recitation begins with Bismillah)
            val isBismillahActive = playerState.surahNumber == surah.number && playerState.activeAyahNumber == 0
            val isBismillahPlaying = isBismillahActive && playerState.isPlaying
            if (surah.number != 1) {
                item {
                    BismillahBannerCard(
                        isBismillahPlaying = isBismillahPlaying,
                        isBismillahActive = isBismillahActive,
                        currentPositionMs = if (isBismillahActive) playerState.currentPositionMs else 0,
                        durationMs = if (isBismillahActive) playerState.durationMs else 0,
                        onPlayBismillah = {
                            if (isBismillahPlaying) {
                                audioManager.pause()
                            } else {
                                audioManager.playAyahAudio(
                                    surahNumber = surah.number,
                                    ayahNumber = 0,
                                    continuous = true,
                                    surahNameBn = surah.nameBn
                                )
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }

            if (ayahsState.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(
                                color = IslamicGreen,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "সূরা ${surah.nameBn}-এর আয়াতসমূহ লোড হচ্ছে...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.outline
                                )
                            )
                        }
                    }
                }
            } else {
                items(ayahsState, key = { "${it.surahNumber}_${it.ayahNumber}" }) { ayah ->
                    val isAyahBookmarked = bookmarks.any { it.surahNumber == ayah.surahNumber && it.ayahNumber == ayah.ayahNumber }
                    val isTafsirExpanded = expandedTafsirs.contains(ayah.ayahNumber) || settings.showTafsirByDefault
                    val isCurrentActiveAyah = playerState.surahNumber == ayah.surahNumber && playerState.activeAyahNumber == ayah.ayahNumber
                    val isPlayingThisAyah = isCurrentActiveAyah && playerState.isPlaying

                    AyahCardItem(
                        ayah = ayah,
                        surah = surah,
                        settings = settings,
                        selectedTranslator = selectedTranslator,
                        isBookmarked = isAyahBookmarked,
                        isTafsirExpanded = isTafsirExpanded,
                        isActiveAyah = isCurrentActiveAyah,
                        isPlayingThisAyah = isPlayingThisAyah,
                        currentPositionMs = if (isCurrentActiveAyah) playerState.currentPositionMs else 0,
                        durationMs = if (isCurrentActiveAyah) playerState.durationMs else 0,
                        onToggleTafsir = {
                            expandedTafsirs = if (isTafsirExpanded) {
                                expandedTafsirs - ayah.ayahNumber
                            } else {
                                expandedTafsirs + ayah.ayahNumber
                            }
                        },
                        onToggleBookmark = {
                            scope.launch {
                                quranRepository.toggleBookmark(ayah, surah.nameBn)
                                val msg = if (isAyahBookmarked) "বুকমার্ক থেকে সরানো হয়েছে" else "বুকমার্কে যুক্ত করা হয়েছে"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        },
                        onPlayAyahAudio = {
                            if (isPlayingThisAyah) {
                                audioManager.pause()
                            } else {
                                audioManager.playAyahAudio(ayah.surahNumber, ayah.ayahNumber, continuous = true, surahNameBn = surah.nameBn)
                            }
                        },
                        onShareAyah = {
                            val healedPronunciation = QuranBengaliPhoneticTransliteration.sanitizeAndHeal(
                                currentPronunciation = ayah.pronunciationBn,
                                surahNumber = ayah.surahNumber,
                                ayahNumber = ayah.ayahNumber,
                                arabicText = ayah.arabicText
                            )
                            val shareText = buildString {
                                append("সূরা ${surah.nameBn} (${surah.number}:${ayah.ayahNumber})\n\n")
                                append("${ayah.arabicText}\n\n")
                                append("উচ্চারণ: ${healedPronunciation}\n\n")
                                append("অর্থ: ${ayah.translationBn}\n")
                                append("— [Dawah to Jannah পবিত্র কুরআন]")
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "সূরা ${surah.nameBn} আয়াত ${ayah.ayahNumber}")
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "আয়াত শেয়ার করুন"))
                        }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // End of Surah Switcher & Navigation Card
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "✨ আলহামদুলিল্লাহ! সূরা ${surah.nameBn} সমাপ্ত",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGreen
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "সূরা নম্বর: ${BanglaNumberUtils.toBanglaDigits(surah.number)} • মোট আয়াত: ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)} টি",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(14.dp))

                            // Navigation Buttons placed vertically with full width for seamless horizontal text flow
                            if (surah.number < 114) {
                                val nextSurah = QuranSurahCatalog.all114Surahs.find { it.number == surah.number + 1 }
                                Button(
                                    onClick = { onSelectSurahNumber(surah.number + 1) },
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "পরবর্তী সূরা: ${BanglaNumberUtils.toBanglaDigits(nextSurah?.number ?: (surah.number + 1))}. ${nextSurah?.nameBn ?: ""}",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 13.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                            if (surah.number > 1) {
                                val prevSurah = QuranSurahCatalog.all114Surahs.find { it.number == surah.number - 1 }
                                if (surah.number < 114) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                                OutlinedButton(
                                    onClick = { onSelectSurahNumber(surah.number - 1) },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "পূর্ববর্তী সূরা: ${BanglaNumberUtils.toBanglaDigits(prevSurah?.number ?: (surah.number - 1))}. ${prevSurah?.nameBn ?: "পূর্ববর্তী"}",
                                            fontSize = 12.5.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Persistent Revamped Audio Player if playing or paused with active surah
        if (playerState.isPlaying || playerState.isBuffering || playerState.surahNumber != null) {
            MiniQuranAudioPlayer(
                playerState = playerState,
                reciter = audioManager.selectedReciter.value,
                totalAyat = surah.totalAyat,
                autoScrollEnabled = settings.autoScrollWithAudio,
                onToggleAutoScroll = {
                    val newAutoScroll = !settings.autoScrollWithAudio
                    settingsManager.updateAutoScroll(newAutoScroll)
                    Toast.makeText(
                        context,
                        if (newAutoScroll) "তিলাওয়াত অনুযায়ী অটো-স্ক্রল চালু হয়েছে" else "অটো-স্ক্রল বন্ধ করা হয়েছে",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onPlayPause = {
                    if (playerState.isPlaying) audioManager.pause() else audioManager.resume()
                },
                onRestartFromBeginning = { audioManager.restartFromBeginning() },
                onPreviousAyah = { audioManager.playPreviousAyah() },
                onNextAyah = { audioManager.playNextAyah() },
                onSeekForward = { audioManager.seekForward(5000) },
                onSeekBackward = { audioManager.seekBackward(5000) },
                onToggleLoopMode = { audioManager.toggleLoopMode() },
                onSetSpeed = { speed -> audioManager.setPlaybackSpeed(speed) },
                onSeekTo = { pos -> audioManager.seekTo(pos) },
                onOpenReciterSelector = onOpenReciterPicker,
                onStop = { audioManager.stop() }
            )
        }

        if (showSurahQuickSwitchDialog) {
            SurahQuickSwitchDialog(
                currentSurahNumber = surah.number,
                onSelectSurahNumber = onSelectSurahNumber,
                onDismiss = { showSurahQuickSwitchDialog = false }
            )
        }
    }
}

/**
 * Quick Surah Switcher Dialog (Allows instant jump between all 114 Surahs from the reader UI)
 */
@Composable
fun SurahQuickSwitchDialog(
    currentSurahNumber: Int,
    onSelectSurahNumber: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filteredSurahs = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            QuranSurahCatalog.all114Surahs
        } else {
            val q = searchQuery.trim().lowercase()
            QuranSurahCatalog.all114Surahs.filter { s ->
                s.nameBn.lowercase().contains(q) ||
                    s.nameEn.lowercase().contains(q) ||
                    s.nameAr.contains(q) ||
                    s.number.toString() == q ||
                    BanglaNumberUtils.toBanglaDigits(s.number).contains(q) ||
                    s.meaningBn.lowercase().contains(q)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "সূরা নির্বাচন (১১৪)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "বর্তমান: ${BanglaNumberUtils.toBanglaDigits(currentSurahNumber)}",
                    style = MaterialTheme.typography.labelSmall,
                    color = IslamicGreen,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(420.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("সূরার নাম বা নম্বর দিয়ে খুঁজুন...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "মুছুন",
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredSurahs, key = { it.number }) { surahItem ->
                        val isCurrent = surahItem.number == currentSurahNumber
                        Surface(
                            onClick = {
                                onSelectSurahNumber(surahItem.number)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isCurrent) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = if (isCurrent) BorderStroke(1.2.dp, IslamicGreen) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(if (isCurrent) IslamicGreen else MaterialTheme.colorScheme.primaryContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = BanglaNumberUtils.toBanglaDigits(surahItem.number),
                                        color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "সূরা ${surahItem.nameBn}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.SemiBold,
                                        color = if (isCurrent) IslamicGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${surahItem.meaningBn} • আয়াত ${BanglaNumberUtils.toBanglaDigits(surahItem.totalAyat)}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Text(
                                    text = surahItem.nameAr,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCurrent) IslamicGreen else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", color = MaterialTheme.colorScheme.outline)
            }
        }
    )
}

/**
 * Beautiful Revamped Surah Header Card:
 * Displays Surah Name in Arabic & Bengali, Meaning, English Name & Meaning,
 * Revelation Type (Makki/Madani), Total Ayahs, Surah Number,
 * Full Surah Audio control, and Offline status/download.
 */
@Composable
fun SurahHeaderCard(
    surah: QuranSurah,
    isFullSurahPlaying: Boolean,
    isDownloaded: Boolean,
    currentDownload: DownloadProgressState?,
    isSurahBookmarked: Boolean = false,
    onToggleSurahBookmark: () -> Unit = {},
    onPlayFullSurah: () -> Unit,
    onDownloadSurah: () -> Unit,
    onOfflineInfo: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.5f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("surah_header_card")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = if (isDark) {
                            listOf(Color(0xFF09291E), Color(0xFF103A2B), Color(0xFF144735))
                        } else {
                            listOf(Color(0xFF0D4B37), Color(0xFF145E45), Color(0xFF1D7858))
                        }
                    )
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Meta Row: [সূরা নং ৩]  [মাদানী / মাক্কী]  [মোট আয়াত: ২০০]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Surah Number
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "সূরা নং ${BanglaNumberUtils.toBanglaDigits(surah.number)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFDE68A)
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Revelation Badge (মাক্কী / মাদানী)
                    Surface(
                        color = IslamicGold.copy(alpha = 0.28f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.65f))
                    ) {
                        Text(
                            text = surah.revelationType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFFBEA)
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Total Ayats
                    Surface(
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "মোট আয়াত: ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Arabic Name Calligraphy (large & prominent)
                Text(
                    text = surah.nameAr,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFEE685),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Bengali Name
                Text(
                    text = "সূরা ${surah.nameBn}",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Bengali Meaning
                Text(
                    text = "অর্থ: ${surah.meaningBn}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 13.5.sp,
                        color = Color.White.copy(alpha = 0.92f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // English Transliteration & Meaning
                Text(
                    text = "${surah.nameEn} • ${surah.meaningEn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.5.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Bottom Actions Row: Full Surah Audio & Offline buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Full Surah Audio Button
                    Surface(
                        color = if (isFullSurahPlaying) Color(0xFFDC2626) else Color.White,
                        shape = RoundedCornerShape(20.dp),
                        shadowElevation = 2.dp,
                        modifier = Modifier.clickable(onClick = onPlayFullSurah)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (isFullSurahPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "সূরা তিলাওয়াত",
                                tint = if (isFullSurahPlaying) Color.White else Color(0xFF0D4B37),
                                modifier = Modifier.size(17.dp)
                            )
                            Text(
                                text = if (isFullSurahPlaying) "থামান" else "সূরা তিলাওয়াত",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.5.sp,
                                    color = if (isFullSurahPlaying) Color.White else Color(0xFF0D4B37)
                                )
                            )
                        }
                    }

                    // Offline Download Button
                    if (isDownloaded) {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f)),
                            modifier = Modifier.clickable(onClick = onOfflineInfo)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "অফলাইন সংরক্ষিত",
                                    tint = Color(0xFF86EFAC),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "অফলাইন সংরক্ষিত",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    } else if (currentDownload?.isDownloading == true) {
                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(15.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Text(
                                    text = "${currentDownload.progressPercent}% ডাউনলোড হচ্ছে",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    } else {
                        Surface(
                            color = Color.White.copy(alpha = 0.18f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                            modifier = Modifier.clickable(onClick = onDownloadSurah)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FileDownload,
                                    contentDescription = "ডাউনলোড",
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "অডিও ডাউনলোড",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Specimen Bismillah Card
 * Soft sage/mint green background, authentic calligraphy, Bengali meaning
 */
/**
 * Specimen Bismillah Card with Live Recitation Highlighting & Real-Time Progress Bar
 * Soft sage/mint green background, authentic Uthmani calligraphy, Bengali pronunciation & meaning
 */
@Composable
fun BismillahBannerCard(
    isBismillahPlaying: Boolean = false,
    isBismillahActive: Boolean = false,
    currentPositionMs: Int = 0,
    durationMs: Int = 0,
    onPlayBismillah: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val borderColor = if (isBismillahPlaying) IslamicGold else if (isBismillahActive) IslamicGreen else if (isDark) Color(0xFF224838) else Color(0xFFCEE5D8)
    val borderWidth = if (isBismillahPlaying || isBismillahActive) 2.dp else 1.dp

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isBismillahPlaying) {
                if (isDark) Color(0xFF133527) else Color(0xFFE2F4EB)
            } else if (isBismillahActive) {
                if (isDark) Color(0xFF112E22) else Color(0xFFE9F5EF)
            } else {
                if (isDark) Color(0xFF10281E) else Color(0xFFE8F3EE)
            }
        ),
        border = BorderStroke(borderWidth, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isBismillahPlaying || isBismillahActive) 4.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("bismillah_banner_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Live Reciting Indicator Badge if active
            if (isBismillahActive) {
                Surface(
                    color = IslamicGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.6f)),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        AudioEqualizerBars(isPlaying = isBismillahPlaying)
                        Text(
                            text = if (isBismillahPlaying) "🎙️ বিসমিল্লাহ তিলাওয়াত চলছে..." else "⏸️ বিসমিল্লাহ তিলাওয়াত বিরতি",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color(0xFFFDE68A) else Color(0xFF92400E)
                            )
                        )
                        if (durationMs > 0) {
                            Text(
                                text = "• ${formatAudioTime(currentPositionMs)} / ${formatAudioTime(durationMs)}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isDark) Color(0xFFFDE68A) else Color(0xFF92400E)
                                )
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFFE2F4EB) else Color(0xFF1B4D3E),
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.weight(1f)
                )

                // Quick Play/Replay Bismillah Icon Button
                IconButton(
                    onClick = onPlayBismillah,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isBismillahPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "বিসমিল্লাহ তিলাওয়াত",
                        tint = if (isBismillahPlaying) IslamicGold else IslamicGreen,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "উচ্চারণ: বিসমিল্লাহির রাহমানির রাহিম",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isDark) Color(0xFF9AE6B4) else Color(0xFF22543D),
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "অর্থ: পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে (শুরু করছি)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = if (isDark) Color(0xFFB4D8C8) else Color(0xFF2C5E4E),
                    textAlign = TextAlign.Center
                )
            )

            // Real-Time Visual Progress Bar if playing or active
            if (isBismillahActive && durationMs > 0) {
                Spacer(modifier = Modifier.height(10.dp))
                val progress = (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = IslamicGold,
                    trackColor = IslamicGold.copy(alpha = 0.2f)
                )
            }
        }
    }
}

/**
 * Specimen Screenshot 2 Ayah Card Layout:
 * Action Pills: [📖 তাফসীর] [🔗 শেয়ার] [১] [🔖 বুকমার্ক] [▶ অডিও]
 * Arabic Text (Large Right-Aligned Uthmani)
 * Pronunciation container (উচ্চারণ)
 * Translation
 * Expandable Scholarly Tafsir Card
 */
@Composable
fun AyahCardItem(
    ayah: QuranAyah,
    surah: QuranSurah,
    settings: QuranSettings,
    selectedTranslator: QuranTranslator,
    isBookmarked: Boolean,
    isTafsirExpanded: Boolean,
    isActiveAyah: Boolean = false,
    isPlayingThisAyah: Boolean = false,
    currentPositionMs: Int = 0,
    durationMs: Int = 0,
    onToggleTafsir: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPlayAyahAudio: () -> Unit,
    onShareAyah: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val ayahNumberBn = BanglaNumberUtils.toBanglaDigits(ayah.ayahNumber)

    // Cleaned & healed authentic pronunciation without any broken Unicode marks
    val displayPronunciation = remember(ayah.pronunciationBn, ayah.arabicText) {
        QuranBengaliPhoneticTransliteration.sanitizeAndHeal(
            currentPronunciation = ayah.pronunciationBn,
            surahNumber = ayah.surahNumber,
            ayahNumber = ayah.ayahNumber,
            arabicText = ayah.arabicText
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isPlayingThisAyah) {
                if (isDark) Color(0xFF132A1F) else Color(0xFFEDF8F2)
            } else if (isActiveAyah) {
                if (isDark) Color(0xFF10271D) else Color(0xFFF1F9F4)
            } else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPlayingThisAyah || isActiveAyah) 4.dp else 1.dp),
        border = BorderStroke(
            if (isPlayingThisAyah || isActiveAyah) 2.dp else 1.dp,
            if (isPlayingThisAyah) IslamicGreen else if (isActiveAyah) IslamicGreen.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Dedicated Active Recitation Status Header with Real-Time Progress Bar
            if (isActiveAyah) {
                Surface(
                    color = IslamicGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 7.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                AudioEqualizerBars(isPlaying = isPlayingThisAyah)
                                Text(
                                    text = if (isPlayingThisAyah) "🎙️ বর্তমান তিলাওয়াত (আয়াত $ayahNumberBn)" else "⏸️ তিলাওয়াত বিরতি (আয়াত $ayahNumberBn)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGreen
                                    )
                                )
                            }
                            if (durationMs > 0) {
                                Text(
                                    text = "${formatAudioTime(currentPositionMs)} / ${formatAudioTime(durationMs)}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGreen
                                    )
                                )
                            }
                        }

                        if (durationMs > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            val progress = (currentPositionMs.toFloat() / durationMs.toFloat()).coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = IslamicGreen,
                                trackColor = IslamicGreen.copy(alpha = 0.25f)
                            )
                        }
                    }
                }
            }
            // Action Bar: Left (Ayah Number Badge) | Right (Tafsir, Audio, Bookmark, Share) with uncompromised horizontal flow
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left: Ayah Number Badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(IslamicGreen.copy(alpha = 0.14f))
                        .border(1.dp, IslamicGreen.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ayahNumberBn,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen,
                            fontSize = 13.sp
                        )
                    )
                }

                // Right: Action Pills & Buttons (Guaranteed single-line horizontal text flow)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Tafsir Pill
                    Surface(
                        color = if (isTafsirExpanded) IslamicGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(
                            0.8.dp,
                            if (isTafsirExpanded) IslamicGold else Color.Transparent
                        ),
                        modifier = Modifier.clickable(onClick = onToggleTafsir)
                    ) {
                        Text(
                            text = "📖 তাফসীর",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (isTafsirExpanded) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }

                    // Ayah Audio Pill
                    Surface(
                        color = if (isPlayingThisAyah) IslamicGreen else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable(onClick = onPlayAyahAudio)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Icon(
                                imageVector = if (isPlayingThisAyah) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "অডিও",
                                tint = if (isPlayingThisAyah) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isPlayingThisAyah) "থামান" else "তিলাওয়াত",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (isPlayingThisAyah) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }

                    // Bookmark Button
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক",
                            tint = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = onShareAyah,
                        modifier = Modifier.size(30.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Part 1: Arabic Ayat Card (Very Lite Mint / Sage Green Tint)
            if (settings.showArabic) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) Color(0xFF13231A) else Color(0xFFF1F8F5),
                    border = BorderStroke(0.8.dp, if (isDark) Color(0xFF223E2E) else Color(0xFFD4EAE0)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "${ayah.arabicText} ۝$ayahNumberBn",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = settings.arabicFontSize.sp,
                                lineHeight = (settings.arabicFontSize * 1.6f).sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.End,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Part 2: Bangla Pronunciation Card (Very Lite Warm Honey/Sand Cream Tint)
            if (settings.showPronunciation && displayPronunciation.isNotBlank()) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) Color(0xFF272113) else Color(0xFFFFFBEA),
                    border = BorderStroke(0.8.dp, if (isDark) Color(0xFF453A20) else Color(0xFFF5E3B3)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFB57C1E).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "উচ্চারণ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFFE5B25D) else Color(0xFF9E6814),
                                    fontSize = 10.5.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = displayPronunciation,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = (settings.banglaFontSize * 0.95f).coerceIn(13f, 22f).sp,
                                lineHeight = (settings.banglaFontSize * 1.45f).sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Part 3: Bangla Meaning Card (Very Lite Soft Sky / Ice Blue Tint)
            if (settings.showTranslation) {
                val activeTranslation = remember(ayah, selectedTranslator) {
                    QuranTafsirAndTranslationProvider.getTranslationText(ayah, selectedTranslator)
                }
                val translatorName = selectedTranslator.shortNameBn
                val englishTranslation = remember(ayah.surahNumber, ayah.ayahNumber, ayah.translationEn) {
                    QuranTafsirAndTranslationProvider.getEnglishTranslation(
                        ayah.surahNumber,
                        ayah.ayahNumber,
                        ayah.translationEn
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (isDark) Color(0xFF14202E) else Color(0xFFF2F6FC),
                    border = BorderStroke(0.8.dp, if (isDark) Color(0xFF20354C) else Color(0xFFD4E2F2)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF1976D2).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "অর্থ ($translatorName)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFF64B5F6) else Color(0xFF1565C0),
                                    fontSize = 10.5.sp
                                ),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = activeTranslation,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = settings.banglaFontSize.sp,
                                lineHeight = (settings.banglaFontSize * 1.5f).sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // English Translation Text (Optional, if enabled in settings)
                        if (settings.showEnglishTranslation && englishTranslation.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                thickness = 0.8.dp
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                            ) {
                                Text(
                                    text = "English (Saheeh International)",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.5.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = englishTranslation,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (settings.banglaFontSize * 0.9f).sp,
                                    lineHeight = (settings.banglaFontSize * 1.35f).sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // Expandable Scholarly Tafsir Card
            AnimatedVisibility(
                visible = isTafsirExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                val tafsirSource = remember(settings.preferredTafsir) {
                    QuranTafsirSource.fromId(settings.preferredTafsir)
                }
                val tafsirContent = remember(ayah, tafsirSource) {
                    QuranTafsirAndTranslationProvider.getTafsirText(ayah, tafsirSource)
                }

                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            // Gold bar on left
                            Box(
                                modifier = Modifier
                                    .width(3.5.dp)
                                    .height(36.dp)
                                    .background(IslamicGold, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = tafsirSource.titleBn,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicGold
                                        )
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = IslamicGold.copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = tafsirSource.shortNameBn,
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontSize = 9.5.sp,
                                                color = IslamicGold,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "${tafsirSource.authorBn} • ${tafsirSource.descriptionBn}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
                                    )
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = tafsirContent,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 13.sp,
                                        lineHeight = 20.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Bottom Floating / Sticky Mini Quran Audio Player
 */
/**
 * Audio Equalizer Bar Animation for live recitation indicator
 */
@Composable
fun AudioEqualizerBars(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    barColor: Color = IslamicGreen
) {
    Row(
        modifier = modifier.size(width = 16.dp, height = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "equalizer")
        val h1 by infiniteTransition.animateFloat(
            initialValue = 0.25f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(380, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "h1"
        )
        val h2 by infiniteTransition.animateFloat(
            initialValue = 0.85f,
            targetValue = 0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(520, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "h2"
        )
        val h3 by infiniteTransition.animateFloat(
            initialValue = 0.35f,
            targetValue = 0.95f,
            animationSpec = infiniteRepeatable(
                animation = tween(440, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "h3"
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(if (isPlaying) h1 else 0.3f)
                .clip(RoundedCornerShape(1.dp))
                .background(barColor)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(if (isPlaying) h2 else 0.6f)
                .clip(RoundedCornerShape(1.dp))
                .background(barColor)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(if (isPlaying) h3 else 0.4f)
                .clip(RoundedCornerShape(1.dp))
                .background(barColor)
        )
    }
}

fun formatAudioTime(ms: Int): String {
    if (ms <= 0) return "০০:০০"
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    val mStr = if (minutes < 10) "0$minutes" else "$minutes"
    val sStr = if (seconds < 10) "0$seconds" else "$seconds"
    return BanglaNumberUtils.toBanglaDigits("$mStr:$sStr")
}

/**
 * Revamped High-End Quran Audio Player Dock
 * Features:
 * - Real-time smooth interactive scrubber
 * - Play / Pause with buffering animation
 * - 'শুরু থেকে' (Start Again / Replay from beginning)
 * - Previous Ayah (including returning to Bismillah)
 * - Next Ayah
 * - Loop Modes: Off, Repeat Ayah (memorization / Hifz), Repeat Surah
 * - Playback Speed: 0.75x, 1.0x, 1.25x, 1.5x
 * - Instant Qari Switcher dialog trigger
 */
@Composable
fun MiniQuranAudioPlayer(
    playerState: com.example.data.model.SurahAudioPlayerState,
    reciter: QuranReciter,
    totalAyat: Int = 0,
    autoScrollEnabled: Boolean = true,
    onToggleAutoScroll: (() -> Unit)? = null,
    onPlayPause: () -> Unit,
    onRestartFromBeginning: () -> Unit = {},
    onPreviousAyah: () -> Unit = {},
    onNextAyah: () -> Unit = {},
    onSeekForward: () -> Unit = {},
    onSeekBackward: () -> Unit = {},
    onToggleLoopMode: () -> Unit = {},
    onSetSpeed: (Float) -> Unit = {},
    onSeekTo: (Int) -> Unit = {},
    onOpenReciterSelector: () -> Unit = {},
    onStop: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    var isUserScrubbing by remember { mutableStateOf(false) }
    var scrubPositionMs by remember { mutableStateOf(0f) }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp,
        shadowElevation = 14.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("revamped_quran_audio_player")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Section 1: Interactive Scrubber / Seek Bar with Time Stamps
            if (playerState.durationMs > 0) {
                val currentMs = if (isUserScrubbing) scrubPositionMs.toInt() else playerState.currentPositionMs
                val durationMs = playerState.durationMs

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = formatAudioTime(currentMs),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        )
                    )
                    Text(
                        text = formatAudioTime(durationMs),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                Slider(
                    value = currentMs.toFloat().coerceIn(0f, durationMs.toFloat()),
                    onValueChange = { newVal ->
                        isUserScrubbing = true
                        scrubPositionMs = newVal
                    },
                    onValueChangeFinished = {
                        isUserScrubbing = false
                        onSeekTo(scrubPositionMs.toInt())
                    },
                    valueRange = 0f..durationMs.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = IslamicGreen,
                        activeTrackColor = IslamicGreen,
                        inactiveTrackColor = IslamicGreen.copy(alpha = 0.2f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(24.dp)
                )
            } else {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(1.5.dp)),
                    color = IslamicGreen,
                    trackColor = IslamicGreen.copy(alpha = 0.2f)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Section 2: Reciting Verse Meta Information
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated Equalizer / Book Medallion
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(IslamicGreen, Color(0xFF147A57))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (playerState.isPlaying) {
                        AudioEqualizerBars(isPlaying = true, barColor = Color.White)
                    } else {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "কুরআন অডিও",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    val titleText = when {
                        playerState.activeAyahNumber == 0 -> "بِسْمِ ٱللَّٰهِ ٱلرَّحْمَٰنِ ٱلرَّحِيمِ"
                        playerState.activeAyahNumber != null -> {
                            val surahName = playerState.surahNameBn.ifBlank {
                                playerState.surahNumber?.let { BanglaNumberUtils.toBanglaDigits(it) } ?: ""
                            }
                            val totalText = if (totalAyat > 0) " / ${BanglaNumberUtils.toBanglaDigits(totalAyat)}" else ""
                            "সূরা $surahName • আয়াত ${BanglaNumberUtils.toBanglaDigits(playerState.activeAyahNumber)}$totalText"
                        }
                        playerState.surahNumber != null -> "সূরা ${playerState.surahNameBn}"
                        else -> "পবিত্র কুরআন তিলাওয়াত"
                    }

                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Subtitle
                        val subtitleText = if (playerState.activeAyahNumber == 0) {
                            "বিসমিল্লাহির রাহমানির রাহিম"
                        } else {
                            reciter.nameBn
                        }
                        Text(
                            text = subtitleText,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        // Loop mode badge
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (playerState.loopMode) {
                                QuranAudioLoopMode.REPEAT_AYAH -> IslamicGold.copy(alpha = 0.2f)
                                QuranAudioLoopMode.REPEAT_SURAH -> IslamicGreen.copy(alpha = 0.2f)
                                QuranAudioLoopMode.OFF -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            }
                        ) {
                            Text(
                                text = playerState.loopMode.shortLabel,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = when (playerState.loopMode) {
                                        QuranAudioLoopMode.REPEAT_AYAH -> if (isDark) Color(0xFFFDE68A) else Color(0xFF92400E)
                                        QuranAudioLoopMode.REPEAT_SURAH -> IslamicGreen
                                        QuranAudioLoopMode.OFF -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                    }
                                ),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                            )
                        }

                        if (playerState.isOfflineFile) {
                            Text(
                                text = "• অফলাইন",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = IslamicGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                // Stop / Close player button
                IconButton(
                    onClick = onStop,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "প্লেয়ার বন্ধ করুন",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Section 3: Primary Audio Controls Row (Loop, Prev, Rewind10, Play/Pause, Forward10, Next, Speed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Loop Mode Button
                IconButton(
                    onClick = onToggleLoopMode,
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (playerState.loopMode) {
                                QuranAudioLoopMode.REPEAT_AYAH -> Icons.Default.RepeatOne
                                else -> Icons.Default.Repeat
                            },
                            contentDescription = "লুপ মোড: ${playerState.loopMode.titleBn}",
                            tint = when (playerState.loopMode) {
                                QuranAudioLoopMode.OFF -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                else -> IslamicGold
                            },
                            modifier = Modifier.size(21.dp)
                        )
                    }
                }

                // 2. Previous Ayah Button
                IconButton(
                    onClick = onPreviousAyah,
                    modifier = Modifier.size(38.dp),
                    enabled = (playerState.activeAyahNumber ?: 1) > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "পূর্ববর্তী আয়াত",
                        tint = if ((playerState.activeAyahNumber ?: 1) > 0) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        },
                        modifier = Modifier.size(21.dp)
                    )
                }

                // 3. Rewind 10 Seconds
                IconButton(
                    onClick = onSeekBackward,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "১০ সেকেন্ড পেছনে যান",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 4. Play / Pause Main FAB Button
                Surface(
                    shape = CircleShape,
                    color = IslamicGreen,
                    shadowElevation = 5.dp,
                    modifier = Modifier
                        .size(50.dp)
                        .clickable(onClick = onPlayPause)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (playerState.isBuffering) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                strokeWidth = 2.5.dp,
                                color = Color.White
                            )
                        } else {
                            Icon(
                                imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (playerState.isPlaying) "বিরতি" else "চালান",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }

                // 5. Forward 10 Seconds
                IconButton(
                    onClick = onSeekForward,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "১০ সেকেন্ড সামনে যান",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }

                // 6. Next Ayah Button
                IconButton(
                    onClick = onNextAyah,
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "পরবর্তী আয়াত",
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(21.dp)
                    )
                }

                // 7. Playback Speed Selector Button (0.75x, 1.0x, 1.25x, 1.5x)
                val nextSpeed = when (playerState.playbackSpeed) {
                    1.0f -> 1.25f
                    1.25f -> 1.5f
                    1.5f -> 0.75f
                    else -> 1.0f
                }
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    modifier = Modifier.clickable { onSetSpeed(nextSpeed) }
                ) {
                    Text(
                        text = "${BanglaNumberUtils.toBanglaDigits(playerState.playbackSpeed.toString())}x",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp
                        ),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Section 4: Sophisticated Value Actions (Start Again from Bismillah, Auto-Scroll Toggle, Qari Picker)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Start Again ("শুরু থেকে") Button - Restarts Surah from Bismillah
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = IslamicGreen.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable(onClick = onRestartFromBeginning)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Replay,
                            contentDescription = "শুরু থেকে চালান",
                            tint = IslamicGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "শুরু থেকে (বিসমিল্লাহ)",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                // Auto-Scroll Toggle Pill
                if (onToggleAutoScroll != null) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = if (autoScrollEnabled) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, if (autoScrollEnabled) IslamicGreen.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable(onClick = onToggleAutoScroll)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "অটো স্ক্রল পরিবর্তন",
                                tint = if (autoScrollEnabled) IslamicGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (autoScrollEnabled) "স্ক্রল সিঙ্ক: চালু" else "স্ক্রল সিঙ্ক: বন্ধ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (autoScrollEnabled) IslamicGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Qari Switcher Button
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.clickable(onClick = onOpenReciterSelector)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "ক্বারী পরিবর্তন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = reciter.nameBn,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MaterialTheme.surfaceColorAtElevation(elevation: androidx.compose.ui.unit.Dp): Color {
    return MaterialTheme.colorScheme.surface
}

/**
 * Reciter Selection Dialog
 */
@Composable
fun ReciterSelectionDialog(
    currentReciter: QuranReciter,
    onSelectReciter: (QuranReciter) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ক্বারী নির্বাচন করুন",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                QuranReciter.entries.forEach { reciter ->
                    val isSelected = reciter == currentReciter
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { onSelectReciter(reciter) }
                            .background(if (isSelected) IslamicGreen.copy(alpha = 0.1f) else Color.Transparent)
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onSelectReciter(reciter) },
                            colors = RadioButtonDefaults.colors(selectedColor = IslamicGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = reciter.nameBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                if (reciter.isFavorite) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "★ প্রিয়",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = IslamicGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                            Text(
                                text = reciter.nameEn,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.outline
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "বন্ধ করুন", color = IslamicGreen)
            }
        }
    )
}
