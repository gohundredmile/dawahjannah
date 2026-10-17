package com.example.ui.screens.quran

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatAlignRight
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Style
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranLayoutMode
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily

/**
 * Material 3 Selector Sheet / Dialog to switch between Quran Layout Modes:
 * 1. কার্ড ভিউ (Ayah Cards)
 * 2. ১৫-লাইন মদীনা পেজ ভিউ (15-Line Madani Mushaf)
 * 3. ১৬-লাইন নূরানী হাফেজী ভিউ (16-Line Noorani Hafizi)
 * 4. শুধু আরবি তিলাওয়াত মোড (Pure Arabic Recitation)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranLayoutModeSelectorSheet(
    currentMode: QuranLayoutMode,
    showConciseTafsirOverlay: Boolean = false,
    onSelectMode: (QuranLayoutMode) -> Unit,
    onToggleConciseTafsirOverlay: (Boolean) -> Unit = {},
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(IslamicGreen.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Style,
                            contentDescription = null,
                            tint = IslamicGreen,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "কুরআন পাঠের লেআউট ও সংস্করণ",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                fontSize = 17.sp
                            )
                        )
                        Text(
                            text = "পছন্দের প্রিন্ট বা ডিসপ্লে স্টাইল নির্বাচন করুন",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "বন্ধ করুন",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 4 Option Cards
            LayoutOptionCard(
                mode = QuranLayoutMode.AYAH_CARD,
                isSelected = currentMode == QuranLayoutMode.AYAH_CARD,
                icon = Icons.Default.ViewAgenda,
                badge = "পূর্ণাঙ্গ ভিউ",
                onClick = {
                    onSelectMode(QuranLayoutMode.AYAH_CARD)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            LayoutOptionCard(
                mode = QuranLayoutMode.MADANI_15_LINE,
                isSelected = currentMode == QuranLayoutMode.MADANI_15_LINE,
                icon = Icons.Default.AutoStories,
                badge = "১৫ লাইন • ৩ডি পেজ ফ্লিপ",
                onClick = {
                    onSelectMode(QuranLayoutMode.MADANI_15_LINE)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            LayoutOptionCard(
                mode = QuranLayoutMode.NOORANI_16_LINE,
                isSelected = currentMode == QuranLayoutMode.NOORANI_16_LINE,
                icon = Icons.Default.MenuBook,
                badge = "১৬ লাইন • নূরাণী হাফেজী",
                onClick = {
                    onSelectMode(QuranLayoutMode.NOORANI_16_LINE)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(10.dp))

            LayoutOptionCard(
                mode = QuranLayoutMode.PURE_ARABIC,
                isSelected = currentMode == QuranLayoutMode.PURE_ARABIC,
                icon = Icons.Default.FormatAlignRight,
                badge = "বিশুদ্ধ তিলাওয়াত মোড",
                onClick = {
                    onSelectMode(QuranLayoutMode.PURE_ARABIC)
                    onDismiss()
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Concise Tafsir Overlay Toggle Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onToggleConciseTafsirOverlay(!showConciseTafsirOverlay) }
                    .testTag("toggle_concise_tafsir_overlay_card"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (showConciseTafsirOverlay) {
                        IslamicGreen.copy(alpha = 0.08f)
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                    }
                ),
                border = BorderStroke(
                    if (showConciseTafsirOverlay) 1.5.dp else 1.dp,
                    if (showConciseTafsirOverlay) IslamicGreen.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (showConciseTafsirOverlay) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = if (showConciseTafsirOverlay) IslamicGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "সংক্ষিপ্ত তাফসীর ওভারলে",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    fontSize = 14.5.sp,
                                    color = if (showConciseTafsirOverlay) IslamicGreen else MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = if (showConciseTafsirOverlay) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "তাফসীর মোড",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (showConciseTafsirOverlay) IslamicGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "নির্বাচিত লেআউট মোডে সরাসরি প্রতিটি আয়াতের সংক্ষিপ্ত ব্যাখ্যা ও তাফসীর ওভারলে অন/অফ করুন",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.5.sp,
                                fontFamily = banglaFont
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Switch(
                        checked = showConciseTafsirOverlay,
                        onCheckedChange = { onToggleConciseTafsirOverlay(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = IslamicGreen
                        ),
                        modifier = Modifier.testTag("switch_concise_tafsir_overlay")
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun LayoutOptionCard(
    mode: QuranLayoutMode,
    isSelected: Boolean,
    icon: ImageVector,
    badge: String,
    onClick: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val borderColor = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    val containerColor = if (isSelected) {
        IslamicGreen.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("layout_option_${mode.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 1.8.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (isSelected) IslamicGreen else MaterialTheme.colorScheme.surface
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = mode.titleBn,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            fontSize = 15.sp,
                            color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = if (isSelected) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = badge,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = mode.descriptionBn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        fontFamily = banglaFont
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            AnimatedVisibility(
                visible = isSelected,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "সক্রিয়",
                    tint = IslamicGreen,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
