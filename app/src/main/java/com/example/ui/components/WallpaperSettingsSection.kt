package com.example.ui.components

import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.CalendarHelper
import com.example.util.WallpaperDownloadResult
import com.example.util.WallpaperManager
import kotlinx.coroutines.launch
import java.io.File

@Composable
fun WallpaperSettingsSection(
    modifier: Modifier = Modifier,
    isExpandedDefault: Boolean = true
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val wallpaperState by WallpaperManager.configState.collectAsState()

    var isExpanded by remember { mutableStateOf(isExpandedDefault) }
    var selectedModeTab by remember {
        mutableStateOf(if (wallpaperState.isCustomEnabled) 1 else 0)
    }

    var driveInputUrl by remember {
        mutableStateOf(wallpaperState.customUrl)
    }
    var isDownloading by remember { mutableStateOf(false) }
    var downloadErrorMsg by remember { mutableStateOf<String?>(null) }
    var downloadSuccessMsg by remember { mutableStateOf<String?>(null) }

    val hasCachedCustomWallpaper = wallpaperState.cachedFilePath != null &&
            File(wallpaperState.cachedFilePath!!).exists() &&
            wallpaperState.cachedFileSizeBytes > 0

    val headerBadgeText = if (wallpaperState.isCustomEnabled && hasCachedCustomWallpaper) {
        "কাস্টম ড্রাইভ সক্রিয় • ০ms ক্যাশ"
    } else {
        "বিল্ট-ইন: ${WallpaperManager.getSelectedBuiltInWallpaper().nameBn}"
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Collapsible Header Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .clickable { isExpanded = !isExpanded },
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Image,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "শীর্ষ ব্যানার ওয়ালপেপার ও গুগল ড্রাইভ স্টোরেজ",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = headerBadgeText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (isExpanded) "সংকোচন" else "প্রসারণ",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.rotate(if (isExpanded) 180f else 0f)
                )
            }
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = expandVertically() + fadeIn(),
            exit = shrinkVertically() + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            ) {
                // Tab Selection: Built-in Wallpapers vs Google Drive Storage
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedModeTab == 0,
                        onClick = {
                            selectedModeTab = 0
                            if (wallpaperState.isCustomEnabled) {
                                WallpaperManager.setCustomWallpaperEnabled(context, false)
                            }
                        },
                        label = {
                            Text(
                                "অ্যাপের কালেকশন (Built-in)",
                                fontSize = 12.sp,
                                fontWeight = if (selectedModeTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )

                    FilterChip(
                        selected = selectedModeTab == 1,
                        onClick = {
                            selectedModeTab = 1
                            if (hasCachedCustomWallpaper && !wallpaperState.isCustomEnabled) {
                                WallpaperManager.setCustomWallpaperEnabled(context, true)
                            }
                        },
                        label = {
                            Text(
                                "গুগল ড্রাইভ স্টোরেজ",
                                fontSize = 12.sp,
                                fontWeight = if (selectedModeTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // SECTION 1: Built-in Collection
                if (selectedModeTab == 0) {
                    BuiltInWallpapersPicker(
                        selectedIndex = wallpaperState.selectedBuiltInIndex,
                        onSelect = { index ->
                            WallpaperManager.selectBuiltInWallpaper(context, index)
                        }
                    )
                } else {
                    // SECTION 2: Google Drive Custom Link & Cache Engine
                    GoogleDriveWallpaperEngine(
                        wallpaperState = wallpaperState,
                        hasCachedCustomWallpaper = hasCachedCustomWallpaper,
                        driveInputUrl = driveInputUrl,
                        onUrlChange = {
                            driveInputUrl = it
                            downloadErrorMsg = null
                            downloadSuccessMsg = null
                        },
                        isDownloading = isDownloading,
                        downloadErrorMsg = downloadErrorMsg,
                        downloadSuccessMsg = downloadSuccessMsg,
                        onVerifyAndCache = {
                            scope.launch {
                                isDownloading = true
                                downloadErrorMsg = null
                                downloadSuccessMsg = null

                                val result = WallpaperManager.downloadAndCacheWallpaper(context, driveInputUrl)
                                isDownloading = false

                                when (result) {
                                    is WallpaperDownloadResult.Success -> {
                                        val kb = result.sizeBytes / 1024
                                        downloadSuccessMsg = "সফলভাবে ডাউনলোড ও লোকাল ক্যাশে সংরক্ষিত হয়েছে! (${CalendarHelper.toBanglaNumber(kb.toInt())} KB, ${result.width}×${result.height} px)"
                                    }
                                    is WallpaperDownloadResult.Error -> {
                                        downloadErrorMsg = result.messageBn
                                    }
                                }
                            }
                        },
                        onClearCache = {
                            WallpaperManager.clearCustomWallpaper(context)
                            driveInputUrl = ""
                            downloadSuccessMsg = "ক্যাশ পরিষ্কার করা হয়েছে।"
                        },
                        onToggleActive = { active ->
                            WallpaperManager.setCustomWallpaperEnabled(context, active)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // SECTION 3: Dimension, Format & Size Optimization Guidelines Card
                WallpaperOptimizationGuidelinesCard()
            }
        }
    }
}

@Composable
private fun BuiltInWallpapersPicker(
    selectedIndex: Int,
    onSelect: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "পছন্দের ব্যানার বেছে নিন:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                itemsIndexed(WallpaperManager.builtInWallpapers) { index, item ->
                    val isSelected = selectedIndex == index
                    Column(
                        modifier = Modifier
                            .width(135.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelect(index) }
                            .border(
                                width = if (isSelected) 2.dp else 0.8.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                            .padding(6.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(76.dp)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            Image(
                                painter = painterResource(id = item.resId),
                                contentDescription = item.nameBn,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(4.dp)
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.primary),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "নির্বাচিত",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = item.nameBn,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = item.descriptionBn,
                            fontSize = 9.5.sp,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoogleDriveWallpaperEngine(
    wallpaperState: com.example.util.WallpaperConfigState,
    hasCachedCustomWallpaper: Boolean,
    driveInputUrl: String,
    onUrlChange: (String) -> Unit,
    isDownloading: Boolean,
    downloadErrorMsg: String?,
    downloadSuccessMsg: String?,
    onVerifyAndCache: () -> Unit,
    onClearCache: () -> Unit,
    onToggleActive: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = remember {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.CloudDownload,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "গুগল ড্রাইভ শেয়ার লিঙ্ক থেকে ওয়ালপেপার সেট করুন",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "গুগল ড্রাইভে সংরক্ষিত যেকোনো সুন্দর ইসলামিক ওয়ালপেপারের লিংক পেস্ট করুন। অ্যাপটি স্বয়ংক্রিয়ভাবে ছবিটি ডাউনলোড করে নিজস্ব ইন্টারনাল ক্যাশে সেভ করে রাখবে।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 11.5.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // URL Input Box with Paste Button
            OutlinedTextField(
                value = driveInputUrl,
                onValueChange = onUrlChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "https://drive.google.com/file/d/.../view",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                },
                label = { Text("গুগল ড্রাইভ বা ইমেজ লিংক", fontSize = 12.sp) },
                singleLine = true,
                trailingIcon = {
                    IconButton(onClick = {
                        try {
                            val clipData = clipboardManager.primaryClip
                            if (clipData != null && clipData.itemCount > 0) {
                                val text = clipData.getItemAt(0).coerceToText(context).toString()
                                if (text.isNotBlank()) {
                                    onUrlChange(text)
                                }
                            }
                        } catch (_: Exception) {}
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentPaste,
                            contentDescription = "পেস্ট করুন",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button: Verify and Cache
            Button(
                onClick = onVerifyAndCache,
                enabled = driveInputUrl.isNotBlank() && !isDownloading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                if (isDownloading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ডাউনলোড ও ক্যাশ প্রস্তুত হচ্ছে...", fontSize = 13.sp)
                } else {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("যাচাই ও লোকাল ক্যাশে সংরক্ষণ করুন", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Error message banner
            if (!downloadErrorMsg.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = downloadErrorMsg,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            // Success message banner
            if (!downloadSuccessMsg.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF047857).copy(alpha = 0.15f),
                    border = BorderStroke(0.8.dp, Color(0xFF047857).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = downloadSuccessMsg,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF047857),
                            fontWeight = FontWeight.Medium,
                            fontSize = 11.5.sp
                        )
                    }
                }
            }

            // ACTIVE CACHED WALLPAPER STATUS & PREVIEW CARD
            if (hasCachedCustomWallpaper && wallpaperState.cachedFilePath != null) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Speed,
                                    contentDescription = null,
                                    tint = Color(0xFF10B981),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "লোকাল ক্যাশ প্রস্তুত (০ms বিদ্যুৎ গতি)",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF10B981)
                                )
                            }

                            // Toggle custom active
                            Switch(
                                checked = wallpaperState.isCustomEnabled,
                                onCheckedChange = onToggleActive,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Preview Thumbnail with Scrim simulation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(110.dp)
                                .clip(RoundedCornerShape(10.dp))
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(File(wallpaperState.cachedFilePath))
                                    .crossfade(true)
                                    .build(),
                                contentDescription = "ক্যাশ করা ওয়ালপেপার",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Scrim preview
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                Color(0xFF021C16).copy(alpha = 0.5f),
                                                Color(0xFF010E0A).copy(alpha = 0.7f)
                                            )
                                        )
                                    )
                            )

                            Text(
                                text = "হেডার প্রিভিউ • আস-সালামু আলাইকুম",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Stats pills: size, resolution
                        val kb = wallpaperState.cachedFileSizeBytes / 1024
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "সাইজ: ${CalendarHelper.toBanglaNumber(kb.toInt())} KB • ${wallpaperState.cachedWidth}×${wallpaperState.cachedHeight} px",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedButton(
                                onClick = onClearCache,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.5f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DeleteOutline,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("ক্যাশ মুছুন", fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WallpaperOptimizationGuidelinesCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ওয়ালপেপার আপলোড ও সাইজ অপ্টিমাইজেশন নির্দেশিকা",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Specs Table Grid
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    SpecificationRow(
                        title = "সর্বোত্তম রেজোলিউশন (Dimensions):",
                        value = "1920 × 1080 px (16:9 ল্যান্ডস্কেপ) অথবা 1080 × 720 px",
                        note = "মোবাইলের টপ হেডার ব্যানার স্লটে এটি সবচেয়ে তীক্ষ্ণ ও স্ফটিক স্বচ্ছ দেখায়।"
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                    SpecificationRow(
                        title = "সর্বোত্তম ফরম্যাট (Format):",
                        value = "WebP (সর্বাধিক সুপারিশকৃত) অথবা JPEG",
                        note = "WebP ফাইল একই মানে JPEG-এর চেয়ে ৩০-৪০% ছোট হয়।"
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f)
                    )
                    SpecificationRow(
                        title = "সর্বোত্তম ফাইল সাইজ (File Size):",
                        value = "150 KB – 300 KB (সর্বোচ্চ ৫০০ KB)",
                        note = "অতিরিক্ত ভারী (> ১MB) ছবি স্মৃতি অপচয় করে, ৩০০ KB সাইজ নিখুঁত।"
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Blazing Fast Speed Guarantee Note
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF047857).copy(alpha = 0.12f),
                border = BorderStroke(0.8.dp, Color(0xFF047857).copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "বিদ্যুৎ গতির পারফরম্যান্স নিশ্চয়তা (Blazing Fast)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "গুগল ড্রাইভের ছবি অ্যাপের নিজস্ব ইন্টারনাল ফ্ল্যাশ স্টোরেজে সংরক্ষিত (ক্যাশ) থাকে। ফলে হোমস্ক্রিন খোলার সময় কোনো নেটওয়ার্ক বিলম্ব হয় না এবং ০ms গতিতে সম্পূর্ণ অফলাইনে প্রদর্শিত হয়।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SpecificationRow(
    title: String,
    value: String,
    note: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = note,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}
