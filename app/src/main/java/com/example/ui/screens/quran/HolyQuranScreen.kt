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
import androidx.compose.material.icons.automirrored.filled.VolumeUp
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
import androidx.compose.material.icons.filled.Tune
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
import com.example.data.model.QuranAyah
import com.example.data.model.QuranReciter
import com.example.data.model.QuranSettings
import com.example.data.model.QuranSurah
import com.example.data.model.QuranTranslator
import com.example.data.repository.QuranRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAudioManager
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
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val settingsManager = remember { QuranSettingsManager(context) }

    var activeSurahNumber by remember { mutableStateOf(initialSurahNumber) }
    var selectedTranslator by remember { mutableStateOf(QuranTranslator.DR_ZAKARIA) }
    var showReciterDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAudioManagerDialog by remember { mutableStateOf(false) }

    val reciter by audioManager.selectedReciter.collectAsState()
    val playerState by audioManager.playerState.collectAsState()
    val downloadStates by audioManager.downloadState.collectAsState()

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
            onSelectTranslator = { selectedTranslator = it },
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
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "ফিরে যান",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }

                // Quran Book Icon
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(IslamicGreen, IslamicGreen.copy(alpha = 0.8f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "আল-কুরআন",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "আল-কুরআন",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = "অনুবাদ, তাফসীর ও তিলাওয়াত — সূরার নামে চাপলে খুলবে।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(
                    onClick = onOpenAudioManager,
                    modifier = Modifier.testTag("btn_audio_manager")
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudDownload,
                        contentDescription = "কুরআন অডিও ডাউনলোড ম্যানেজার",
                        tint = IslamicGreen
                    )
                }

                IconButton(
                    onClick = onOpenReciterPicker,
                    modifier = Modifier.testTag("btn_reciter_picker")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "ক্বারী নির্বাচন",
                        tint = IslamicGold
                    )
                }

                IconButton(
                    onClick = onOpenSettings,
                    modifier = Modifier.testTag("btn_quran_settings")
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "কুরআন সেটিংস",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Prominent 2-Card Quick Access Banner for Quran Settings & 1-Click Audio Manager
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Card 1: কুরআন সেটিংস (Quran Settings)
            Surface(
                onClick = onOpenSettings,
                shape = RoundedCornerShape(12.dp),
                color = IslamicGold.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("banner_card_quran_settings")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(IslamicGold.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "কুরআন সেটিংস",
                            tint = IslamicGold,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "কুরআন সেটিংস",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "ফন্ট ও অনুবাদক",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Card 2: ১-ক্লিক অডিও ডাউনলোড ম্যানেজার (Audio Manager)
            Surface(
                onClick = onOpenAudioManager,
                shape = RoundedCornerShape(12.dp),
                color = IslamicGreen.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.45f)),
                modifier = Modifier
                    .weight(1f)
                    .testTag("banner_card_audio_manager")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(IslamicGreen.copy(alpha = 0.22f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "অডিও ডাউনলোড",
                            tint = IslamicGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "অডিও ম্যানেজার",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "১১৪ সূরার অফলাইন অডিও",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
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

        // Search Bar
        PaddingValues(horizontal = 16.dp, vertical = 8.dp).let {
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

        // Quick Filter Chips (সকল, মাক্কী, মাদানী, বুকমার্ক, অফলাইন)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                Text(
                    text = "ধরন",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.width(64.dp),
                    textAlign = TextAlign.Center
                )
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
            contentPadding = PaddingValues(bottom = 90.dp)
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
                    SurahListItemRow(
                        surah = surah,
                        isDownloaded = audioManager.isSurahDownloaded(surah.number),
                        isCurrentlyPlaying = playerState.isPlaying && playerState.surahNumber == surah.number,
                        onSurahClick = { onSurahSelected(surah) }
                    )
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
                        thickness = 0.8.dp
                    )
                }
            }
        }

        // Persistent Mini Audio Player if playing
        if (playerState.isPlaying || playerState.isBuffering) {
            MiniQuranAudioPlayer(
                playerState = playerState,
                reciter = reciter,
                onPlayPause = {
                    if (playerState.isPlaying) audioManager.pause() else audioManager.resume()
                },
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
    onSurahClick: () -> Unit
) {
    val serialBn = BanglaNumberUtils.toBanglaDigits(surah.number)
    val ayatsBn = BanglaNumberUtils.toBanglaDigits(surah.totalAyat)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSurahClick)
            .padding(vertical = 10.dp, horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Column: Serial. NameBn | NameEn | NameAr
        // Subtitle: MeaningBn | MeaningEn
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$serialBn.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreen
                    )
                )
                Text(
                    text = "${surah.nameBn} | ${surah.nameEn} | ${surah.nameAr}",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp,
                        color = if (isCurrentlyPlaying) IslamicGreen else MaterialTheme.colorScheme.onSurface
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
                text = "${surah.meaningBn} | ${surah.meaningEn}",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

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
                .padding(horizontal = 6.dp, vertical = 4.dp),
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

        Spacer(modifier = Modifier.width(8.dp))

        // Right Column: Total Ayats in Bengali digits
        Text(
            text = ayatsBn,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.width(42.dp),
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

    // Expanded Tafsir state per Ayah number
    var expandedTafsirs by remember { mutableStateOf(setOf<Int>()) }

    var isFullSurahPlaying by remember {
        mutableStateOf(playerState.isPlaying && playerState.surahNumber == surah.number && playerState.activeAyahNumber == null)
    }

    LaunchedEffect(playerState) {
        isFullSurahPlaying = playerState.isPlaying && playerState.surahNumber == surah.number && playerState.activeAyahNumber == null
    }

    val listState = rememberLazyListState()
    LaunchedEffect(playerState.activeAyahNumber) {
        val activeAyah = playerState.activeAyahNumber
        if (activeAyah != null && settings.autoScrollWithAudio) {
            val targetIndex = (activeAyah - 1).coerceAtLeast(0)
            listState.animateScrollToItem(targetIndex + (if (surah.number != 9) 1 else 0))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Surah Top Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackToIndex) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "সূচিপত্রে ফিরে যান",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "সূরা ${surah.nameBn} (${surah.nameAr}) | ${surah.meaningBn}",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${surah.nameEn} • ${surah.meaningEn}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(onClick = onOpenAudioManager) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = "কুরআন অডিও ম্যানেজার",
                            tint = IslamicGreen
                        )
                    }

                    IconButton(onClick = onOpenReciterPicker) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "ক্বারী নির্বাচন",
                            tint = IslamicGold
                        )
                    }

                    IconButton(onClick = onOpenSettings) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "কুরআন সেটিংস",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Header Badges Row: [মাক্কী] [মোট আয়াত: ৭] [▶ অডিও চালান] [⬇ ডাউনলোড]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Revelation badge
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = surah.revelationType,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    // Total Ayah badge
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "মোট আয়াত: ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Play Full Surah Audio Button
                    Surface(
                        color = if (isFullSurahPlaying) IslamicGreen else MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.clickable {
                            audioManager.playSurah(surah.number, surah.nameBn)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isFullSurahPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "সূরা তিলাওয়াত",
                                tint = if (isFullSurahPlaying) Color.White else MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = if (isFullSurahPlaying) "থামান" else "অডিও",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isFullSurahPlaying) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    // Offline Download Button
                    if (isDownloaded) {
                        Surface(
                            color = IslamicGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(14.dp),
                            border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.clickable {
                                Toast.makeText(context, "এই সূরাটি অফলাইনে সংরক্ষিত আছে।", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "অফলাইন সংরক্ষিত",
                                    tint = IslamicGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "অফলাইন",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = IslamicGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    } else if (currentDownload?.isDownloading == true) {
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    strokeWidth = 2.dp,
                                    color = IslamicGreen
                                )
                                Text(
                                    text = "${currentDownload.progressPercent}%",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = IslamicGreen,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    } else {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.clickable {
                                audioManager.downloadSurahAudio(surah.number) { success ->
                                    if (success) {
                                        Toast.makeText(context, "সূরা ${surah.nameBn} অফলাইনে ডাউনলোড সম্পন্ন হয়েছে!", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "ডাউনলোড ব্যর্থ হয়েছে। ইন্টারনেট সংযোগ চেক করুন।", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.FileDownload,
                                    contentDescription = "ডাউনলোড",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "ডাউনলোড",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                )
                            }
                        }
                    }
                }

                // Secondary Quick Actions Row: [⚙️ কুরআন সেটিংস] [📥 অডিও ম্যানেজার] [🎙️ ক্বারী]
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 3.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Settings Pill
                    Surface(
                        onClick = onOpenSettings,
                        shape = RoundedCornerShape(12.dp),
                        color = IslamicGold.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
                        modifier = Modifier.testTag("pill_quran_settings")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "কুরআন সেটিংস",
                                tint = IslamicGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "কুরআন সেটিংস",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                            )
                        }
                    }

                    // Audio Manager Pill
                    Surface(
                        onClick = onOpenAudioManager,
                        shape = RoundedCornerShape(12.dp),
                        color = IslamicGreen.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.45f)),
                        modifier = Modifier.testTag("pill_audio_manager")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CloudDownload,
                                contentDescription = "অডিও ডাউনলোড ম্যানেজার",
                                tint = IslamicGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "১-ক্লিক অডিও ম্যানেজার",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGreen
                                )
                            )
                        }
                    }

                    // Reciter Picker Pill
                    Surface(
                        onClick = onOpenReciterPicker,
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.testTag("pill_reciter_picker")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "ক্বারী নির্বাচন",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "ক্বারী নির্বাচন",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }
                }

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    thickness = 0.8.dp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Quick Surah Switcher Navigation Strip
                Surface(
                    color = IslamicGreen.copy(alpha = 0.08f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Previous Surah Button
                        TextButton(
                            onClick = {
                                if (surah.number > 1) {
                                    onSelectSurahNumber(surah.number - 1)
                                }
                            },
                            enabled = surah.number > 1,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পূর্ববর্তী সূরা",
                                modifier = Modifier.size(16.dp),
                                tint = if (surah.number > 1) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (surah.number > 1) {
                                    val prevSurah = QuranSurahCatalog.all114Surahs.find { it.number == surah.number - 1 }
                                    "পূর্ববর্তী: ${prevSurah?.nameBn ?: ""}"
                                } else "শুরু",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (surah.number > 1) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                            )
                        }

                        // Central Switch Surah Dialog trigger
                        Surface(
                            onClick = { showSurahQuickSwitchDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            color = IslamicGreen,
                            shadowElevation = 1.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "সূরা বদলান (${BanglaNumberUtils.toBanglaDigits(surah.number)}/১১৪) ▾",
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Next Surah Button
                        TextButton(
                            onClick = {
                                if (surah.number < 114) {
                                    onSelectSurahNumber(surah.number + 1)
                                }
                            },
                            enabled = surah.number < 114,
                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = if (surah.number < 114) {
                                    val nextSurah = QuranSurahCatalog.all114Surahs.find { it.number == surah.number + 1 }
                                    "পরবর্তী: ${nextSurah?.nameBn ?: ""}"
                                } else "শেষ",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (surah.number < 114) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "পরবর্তী সূরা",
                                modifier = Modifier.size(16.dp),
                                tint = if (surah.number < 114) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                            )
                        }
                    }
                }
            }
        }

        // Ayah List with Bismillah Card
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 100.dp)
        ) {
            // Bismillah Card (Except Surah 9 At-Tawbah)
            if (surah.number != 9) {
                item {
                    BismillahBannerCard()
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
                    val isPlayingThisAyah = playerState.isPlaying && playerState.surahNumber == ayah.surahNumber && playerState.activeAyahNumber == ayah.ayahNumber

                    AyahCardItem(
                        ayah = ayah,
                        surah = surah,
                        settings = settings,
                        selectedTranslator = selectedTranslator,
                        isBookmarked = isAyahBookmarked,
                        isTafsirExpanded = isTafsirExpanded,
                        isPlayingThisAyah = isPlayingThisAyah,
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
                            audioManager.playAyahAudio(ayah.surahNumber, ayah.ayahNumber)
                        },
                        onShareAyah = {
                            val shareText = buildString {
                                append("সূরা ${surah.nameBn} (${surah.number}:${ayah.ayahNumber})\n\n")
                                append("${ayah.arabicText}\n\n")
                                append("উচ্চারণ: ${ayah.pronunciationBn}\n\n")
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
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (surah.number > 1) {
                                    val prevSurah = QuranSurahCatalog.all114Surahs.find { it.number == surah.number - 1 }
                                    OutlinedButton(
                                        onClick = { onSelectSurahNumber(surah.number - 1) },
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "⏮ ${prevSurah?.nameBn ?: "পূর্ববর্তী"}",
                                            fontSize = 11.5.sp,
                                            maxLines = 1
                                        )
                                    }
                                }
                                if (surah.number < 114) {
                                    val nextSurah = QuranSurahCatalog.all114Surahs.find { it.number == surah.number + 1 }
                                    Button(
                                        onClick = { onSelectSurahNumber(surah.number + 1) },
                                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = "পরবর্তী: ${nextSurah?.nameBn ?: ""} ⏭",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 11.5.sp,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Persistent Mini Player if playing
        if (playerState.isPlaying || playerState.isBuffering) {
            MiniQuranAudioPlayer(
                playerState = playerState,
                reciter = audioManager.selectedReciter.value,
                onPlayPause = {
                    if (playerState.isPlaying) audioManager.pause() else audioManager.resume()
                },
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
 * Specimen Bismillah Card
 * Soft sage/mint green background, authentic calligraphy, Bengali meaning
 */
@Composable
fun BismillahBannerCard() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE8F3EE) // Exact Soft Mint Green from specimen
        ),
        border = BorderStroke(1.dp, Color(0xFFCEE5D8)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4D3E), // Deep Islamic Pine Green
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "পরম করুণাময় ও অসীম দয়ালু আল্লাহর নামে",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.sp,
                    color = Color(0xFF2C5E4E),
                    textAlign = TextAlign.Center
                )
            )
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
    isPlayingThisAyah: Boolean,
    onToggleTafsir: () -> Unit,
    onToggleBookmark: () -> Unit,
    onPlayAyahAudio: () -> Unit,
    onShareAyah: () -> Unit
) {
    val ayahNumberBn = BanglaNumberUtils.toBanglaDigits(ayah.ayahNumber)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(
            1.dp,
            if (isPlayingThisAyah) IslamicGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
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
            // Action Pills Bar (Exact as specimen screenshot 2)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Tafsir Pill
                Surface(
                    color = if (isTafsirExpanded) IslamicGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        0.8.dp,
                        if (isTafsirExpanded) IslamicGold else Color.Transparent
                    ),
                    modifier = Modifier.clickable(onClick = onToggleTafsir)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📖 তাফসীর",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (isTafsirExpanded) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Share Pill
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.clickable(onClick = onShareAyah)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "শেয়ার",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Ayah Number Center Pill
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(IslamicGreen.copy(alpha = 0.15f))
                        .border(1.dp, IslamicGreen.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = ayahNumberBn,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        )
                    )
                }

                // Bookmark Pill
                Surface(
                    color = if (isBookmarked) IslamicGold.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.clickable(onClick = onToggleBookmark)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক",
                            tint = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "বুকমার্ক",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }

                // Ayah Audio Pill
                Surface(
                    color = if (isPlayingThisAyah) IslamicGreen else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.clickable(onClick = onPlayAyahAudio)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = if (isPlayingThisAyah) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = "অডিও",
                            tint = if (isPlayingThisAyah) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "অডিও",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = if (isPlayingThisAyah) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Verse Text (Right aligned, large authentic calligraphy)
            if (settings.showArabic) {
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
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Bengali Pronunciation Pill Container (উচ্চারণ)
            if (settings.showPronunciation && ayah.pronunciationBn.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = ayah.pronunciationBn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (settings.banglaFontSize * 0.95f).coerceIn(12f, 22f).sp,
                            lineHeight = (settings.banglaFontSize * 1.4f).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Bengali Translation Text
            if (settings.showTranslation) {
                val activeTranslation = when (selectedTranslator) {
                    QuranTranslator.DR_ZAKARIA -> ayah.translationZakaria ?: ayah.translationBn
                    QuranTranslator.TAISIRUL_QURAN -> ayah.translationTaisirul ?: ayah.translationBn
                    QuranTranslator.MUHIBBUR_RAHMAN -> ayah.translationBn
                }

                Text(
                    text = activeTranslation,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = settings.banglaFontSize.sp,
                        lineHeight = (settings.banglaFontSize * 1.45f).sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // English Translation Text (Optional)
            if (settings.showEnglishTranslation && !ayah.translationEn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.translationEn,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (settings.banglaFontSize * 0.9f).sp,
                        lineHeight = (settings.banglaFontSize * 1.35f).sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Expandable Scholarly Tafsir Card
            AnimatedVisibility(
                visible = isTafsirExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
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
                                    .height(28.dp)
                                    .background(IslamicGold, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "তাফসীর ও ব্যাখ্যাঃ",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ayah.tafsirText ?: "এই আয়াতের বিস্তারিত তাফসীর ও শানে নুযূল প্রস্তুত রয়েছে।",
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
@Composable
fun MiniQuranAudioPlayer(
    playerState: com.example.data.model.SurahAudioPlayerState,
    reciter: QuranReciter,
    onPlayPause: () -> Unit,
    onStop: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (playerState.durationMs > 0) {
                val progress = (playerState.currentPositionMs.toFloat() / playerState.durationMs.toFloat()).coerceIn(0f, 1f)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp),
                    color = IslamicGreen,
                    trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(IslamicGreen),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Quran Audio",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    val surahLabel = if (playerState.surahNumber != null) {
                        "সূরা ${playerState.surahNameBn.ifBlank { BanglaNumberUtils.toBanglaDigits(playerState.surahNumber) }}"
                    } else "পবিত্র কুরআন তিলাওয়াত"

                    val ayahLabel = if (playerState.activeAyahNumber != null) {
                        " (আয়াত ${BanglaNumberUtils.toBanglaDigits(playerState.activeAyahNumber)})"
                    } else ""

                    Text(
                        text = "$surahLabel$ayahLabel",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${reciter.nameBn} ${if (playerState.isOfflineFile) "• অফলাইন" else ""}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                if (playerState.isBuffering) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.5.dp,
                        color = IslamicGreen
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }

                IconButton(onClick = onPlayPause) {
                    Icon(
                        imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = "Play/Pause",
                        tint = IslamicGreen,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = onStop) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Stop",
                        tint = MaterialTheme.colorScheme.outline
                    )
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
