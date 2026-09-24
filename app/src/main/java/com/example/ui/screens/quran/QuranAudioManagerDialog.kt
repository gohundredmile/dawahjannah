package com.example.ui.screens.quran

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.model.QuranReciter
import com.example.data.model.QuranSurah
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAudioManager

private enum class AudioFilter {
    ALL,
    DOWNLOADED,
    NOT_DOWNLOADED
}

@Composable
fun QuranAudioManagerDialog(
    audioManager: QuranAudioManager,
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val reciter by audioManager.selectedReciter.collectAsState()
    val batchState by audioManager.batchDownloadState.collectAsState()
    val downloadStates by audioManager.downloadState.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var currentFilter by remember { mutableStateOf(AudioFilter.ALL) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    // Re-trigger calculation of stats
    var refreshKey by remember { mutableStateOf(0) }
    val downloadedCount = remember(refreshKey, downloadStates, batchState) {
        audioManager.getDownloadedSurahsCount()
    }
    val totalStorageBytes = remember(refreshKey, downloadStates, batchState) {
        audioManager.getTotalAudioStorageBytes()
    }
    val totalStorageFormatted = remember(totalStorageBytes) {
        val mb = totalStorageBytes / (1024.0 * 1024.0)
        if (mb > 1024) {
            String.format("%.2f GB", mb / 1024.0)
        } else {
            String.format("%.1f MB", mb)
        }
    }

    val filteredSurahs = remember(QuranSurahCatalog.all114Surahs, searchQuery, currentFilter, downloadStates, batchState) {
        QuranSurahCatalog.all114Surahs.filter { surah ->
            val isDownloaded = audioManager.isSurahDownloaded(surah.number)
            val matchFilter = when (currentFilter) {
                AudioFilter.ALL -> true
                AudioFilter.DOWNLOADED -> isDownloaded
                AudioFilter.NOT_DOWNLOADED -> !isDownloaded
            }
            if (!matchFilter) return@filter false

            if (searchQuery.isBlank()) return@filter true
            val q = searchQuery.trim().lowercase()
            surah.nameBn.lowercase().contains(q) ||
                surah.nameEn.lowercase().contains(q) ||
                surah.nameAr.contains(q) ||
                surah.number.toString() == q ||
                BanglaNumberUtils.toBanglaDigits(surah.number) == q
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CloudDownload,
                                    contentDescription = null,
                                    tint = IslamicGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "কুরআন অডিও ম্যানেজার",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                )
                                Text(
                                    text = "উচ্চ মান (192kbps Crystal Clear) ও অফলাইন স্টোরেজ",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("btn_close_audio_manager")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "বন্ধ করুন",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Reciter Bar
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(IslamicGold.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Headphones,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = reciter.nameBn,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = banglaFont
                                        )
                                    )
                                    Text(
                                        text = "${reciter.nameEn} • ${reciter.audioQuality}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Storage summary & cleanup card
                    item {
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Storage,
                                        contentDescription = null,
                                        tint = IslamicGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "অফলাইন স্টোরেজ: $totalStorageFormatted",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        Text(
                                            text = "${BanglaNumberUtils.toBanglaDigits(downloadedCount)} / ১১৪ সূরা অফলাইনে প্রস্তুত",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontFamily = banglaFont
                                            )
                                        )
                                    }
                                }

                                if (downloadedCount > 0) {
                                    OutlinedButton(
                                        onClick = { showDeleteConfirmDialog = true },
                                        shape = RoundedCornerShape(8.dp),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "মুছুন",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = MaterialTheme.colorScheme.error,
                                                fontFamily = banglaFont
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 1-Click Batch Download Hero Card
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (batchState.isBatchRunning) IslamicGreen.copy(alpha = 0.15f)
                                else IslamicGreen.copy(alpha = 0.1f)
                            ),
                            border = BorderStroke(
                                1.5.dp,
                                if (batchState.isBatchRunning) IslamicGreen else IslamicGreen.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(IslamicGreen),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DownloadDone,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "এক ক্লিকে ১১৪ সূরা ডাউনলোড",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        Text(
                                            text = if (batchState.isBatchRunning) {
                                                batchState.statusMessage ?: "ডাউনলোড চলছে..."
                                            } else {
                                                "সম্পূর্ণ পবিত্র কুরআন অফলাইনে শুনতে একবারেই ডাউনলোড করুন (~৮৫০ MB)"
                                            },
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontFamily = banglaFont
                                            )
                                        )
                                    }
                                }

                                if (batchState.isBatchRunning) {
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${BanglaNumberUtils.toBanglaDigits(batchState.completedSurahsCount)}/১১৪ সূরা সম্পন্ন",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = IslamicGreen,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        Text(
                                            text = "${BanglaNumberUtils.toBanglaDigits(batchState.overallPercent)}%",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = IslamicGreen
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    LinearProgressIndicator(
                                        progress = { batchState.overallPercent / 100f },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(8.dp)
                                            .clip(RoundedCornerShape(4.dp)),
                                        color = IslamicGreen,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (batchState.isPaused) {
                                            Button(
                                                onClick = { audioManager.resumeBatchDownload() },
                                                modifier = Modifier.weight(1f),
                                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = "চালু করুন", fontFamily = banglaFont)
                                            }
                                        } else {
                                            OutlinedButton(
                                                onClick = { audioManager.pauseBatchDownload() },
                                                modifier = Modifier.weight(1f),
                                                shape = RoundedCornerShape(10.dp)
                                            ) {
                                                Icon(imageVector = Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(text = "বিরতি দিন", fontFamily = banglaFont)
                                            }
                                        }

                                        OutlinedButton(
                                            onClick = { audioManager.cancelBatchDownload() },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(10.dp),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                                        ) {
                                            Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(text = "বাতিল", color = MaterialTheme.colorScheme.error, fontFamily = banglaFont)
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Button(
                                        onClick = {
                                            audioManager.startBatchDownloadAllSurahs(reciter)
                                            refreshKey++
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .testTag("btn_batch_download_all"),
                                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(imageVector = Icons.Default.Download, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (downloadedCount >= 114) "সম্পূর্ণ কুরআন ডাউনলোড সম্পন্ন (আবার যাচাই)"
                                            else "সম্পূর্ণ ১১৪ সূরা একসাথে ডাউনলোড করুন",
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Search & Filter
                    item {
                        Column {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = {
                                    Text(
                                        text = "সূরা খুঁজুন (নাম বা নম্বর)...",
                                        fontFamily = banglaFont,
                                        fontSize = 13.sp
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                },
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("search_audio_surah"),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = currentFilter == AudioFilter.ALL,
                                    onClick = { currentFilter = AudioFilter.ALL },
                                    label = { Text("সকল (১১৪)", fontFamily = banglaFont, fontSize = 12.sp) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IslamicGreen.copy(alpha = 0.2f),
                                        selectedLabelColor = IslamicGreen
                                    )
                                )
                                FilterChip(
                                    selected = currentFilter == AudioFilter.DOWNLOADED,
                                    onClick = { currentFilter = AudioFilter.DOWNLOADED },
                                    label = {
                                        Text(
                                            "অফলাইন (${BanglaNumberUtils.toBanglaDigits(downloadedCount)})",
                                            fontFamily = banglaFont,
                                            fontSize = 12.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IslamicGreen.copy(alpha = 0.2f),
                                        selectedLabelColor = IslamicGreen
                                    )
                                )
                                FilterChip(
                                    selected = currentFilter == AudioFilter.NOT_DOWNLOADED,
                                    onClick = { currentFilter = AudioFilter.NOT_DOWNLOADED },
                                    label = {
                                        Text(
                                            "বাকি (${BanglaNumberUtils.toBanglaDigits(114 - downloadedCount)})",
                                            fontFamily = banglaFont,
                                            fontSize = 12.sp
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = IslamicGold.copy(alpha = 0.2f),
                                        selectedLabelColor = IslamicGold
                                    )
                                )
                            }
                        }
                    }

                    // List of 114 Surahs
                    items(filteredSurahs, key = { it.number }) { surah ->
                        val isDownloaded = audioManager.isSurahDownloaded(surah.number)
                        val downloadState = downloadStates[surah.number]
                        val isDownloading = downloadState?.isDownloading == true

                        SurahAudioItemCard(
                            surah = surah,
                            isDownloaded = isDownloaded,
                            isDownloading = isDownloading,
                            progressPercent = downloadState?.progressPercent ?: 0,
                            onDownload = {
                                audioManager.downloadSurahAudio(surah.number, reciter) {
                                    refreshKey++
                                }
                            },
                            onCancel = {
                                audioManager.cancelSurahDownload(surah.number)
                            },
                            onDelete = {
                                audioManager.deleteDownloadedSurah(surah.number)
                                refreshKey++
                            }
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = {
                Text(text = "সকল ডাউনলোড মুছে ফেলবেন?", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            },
            text = {
                Text(
                    text = "আপনার ফোন থেকে ডাউনলোড করা সকল সূরার অডিও ফাইল স্থায়ীভাবে মুছে যাবে। আপনি পরবর্তীতে আবার ডাউনলোড করতে পারবেন।",
                    fontFamily = banglaFont
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        audioManager.deleteAllDownloadedAudio()
                        refreshKey++
                        showDeleteConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text(text = "হ্যাঁ, মুছে ফেলুন", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(text = "বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }
}

@Composable
private fun SurahAudioItemCard(
    surah: QuranSurah,
    isDownloaded: Boolean,
    isDownloading: Boolean,
    progressPercent: Int,
    onDownload: () -> Unit,
    onCancel: () -> Unit,
    onDelete: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    // Estimate file size based on ayah count
    val estSizeMb = remember(surah.totalAyat) {
        val approxMb = (surah.totalAyat * 0.09f).coerceIn(0.4f, 28.5f)
        if (approxMb < 1f) "${(approxMb * 1024).toInt()} KB" else String.format("%.1f MB", approxMb)
    }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDownloaded) IslamicGreen.copy(alpha = 0.05f)
            else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isDownloaded) IslamicGreen.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Serial Circle
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isDownloaded) IslamicGreen else MaterialTheme.colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = BanglaNumberUtils.toBanglaDigits(surah.number),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isDownloaded) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = surah.nameBn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = surah.nameAr,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = arabicFont,
                            color = IslamicGold
                        )
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${surah.revelationType} • ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)} আয়াত",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    )
                    Text(
                        text = "• ~$estSizeMb",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.sp,
                            color = IslamicGreen
                        )
                    )
                }

                if (isDownloading) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        LinearProgressIndicator(
                            progress = { progressPercent / 100f },
                            modifier = Modifier
                                .weight(1f)
                                .height(4.dp)
                                .clip(RoundedCornerShape(2.dp)),
                            color = IslamicGreen,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${BanglaNumberUtils.toBanglaDigits(progressPercent)}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                color = IslamicGreen,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Button
            if (isDownloaded) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(IslamicGreen.copy(alpha = 0.15f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = IslamicGreen,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "সংরক্ষিত",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = IslamicGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "মুছুন",
                            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else if (isDownloading) {
                IconButton(
                    onClick = onCancel,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = "বাতিল",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                OutlinedButton(
                    onClick = onDownload,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.5f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = null,
                        tint = IslamicGreen,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "ডাউনলোড",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = IslamicGreen,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    )
                }
            }
        }
    }
}
