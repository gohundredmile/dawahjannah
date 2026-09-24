package com.example.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranSurah
import com.example.data.model.SurahShaneNuzul
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.util.BanglaNumberUtils

enum class ShaneNuzulTab(val titleBn: String) {
    OVERVIEW("একনজরে"),
    NAMING("নামকরণ"),
    BACKGROUND("শানে নযুল ও পটভূমি"),
    THEMES("মূল শিক্ষা"),
    VIRTUES("ফজিলত ও হাদীস")
}

/**
 * Beautiful, interactive Card displaying the historical revelation context (শানে নযুল),
 * naming reasons, themes, and authentic virtues for every Surah, based on HadithBD / Tafsir.
 */
@Composable
fun SurahShaneNuzulCard(
    surah: QuranSurah,
    shaneNuzul: SurahShaneNuzul?,
    modifier: Modifier = Modifier,
    initiallyExpanded: Boolean = false
) {
    if (shaneNuzul == null) return

    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }
    var selectedTab by remember { mutableStateOf(ShaneNuzulTab.OVERVIEW) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) {
                Color(0xFF13201A)
            } else {
                Color(0xFFF7FAF7)
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = BorderStroke(
            1.dp,
            if (isExpanded) IslamicGreen.copy(alpha = 0.55f) else IslamicGreen.copy(alpha = 0.22f)
        ),
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize(animationSpec = tween(durationMillis = 300))
            .testTag("card_surah_shane_nuzul_${surah.number}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Title, Icon, Badge & Expand/Collapse Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Islamic Book Icon Medallion
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(IslamicGreen, IslamicGreen.copy(alpha = 0.75f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "শানে নযুল",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "শানে নযুল ও পরিচিতি",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color(0xFF86EFAC) else Color(0xFF0F5132)
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = IslamicGold.copy(alpha = 0.22f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(0.6.dp, IslamicGold.copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "হাদীসবিডি ও তাফসীর",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFFFDE68A) else Color(0xFFB45309)
                                ),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "নাযিল হওয়ার ঐতিহাসিক প্রেক্ষাপট, নামকরণ, মূল শিক্ষা ও ফজিলত",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                        ),
                        maxLines = if (isExpanded) 2 else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Expand/Collapse Toggle Button
                Surface(
                    onClick = { isExpanded = !isExpanded },
                    shape = RoundedCornerShape(10.dp),
                    color = if (isExpanded) IslamicGreen else IslamicGreen.copy(alpha = 0.12f),
                    modifier = Modifier.testTag("btn_toggle_shane_nuzul")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = if (isExpanded) "সংক্ষেপ" else "বিস্তারিত",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isExpanded) Color.White else IslamicGreen,
                                fontSize = 11.sp
                            )
                        )
                        Icon(
                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = if (isExpanded) "সংক্ষেপ করুন" else "বিস্তারিত পড়ুন",
                            tint = if (isExpanded) Color.White else IslamicGreen,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Collapsed Preview (2-3 line summary)
            if (!isExpanded) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "📜 প্রেক্ষাপট সংক্ষেপ: ${shaneNuzul.shaneNuzul}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 12.5.sp,
                                lineHeight = 19.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "সম্পূর্ণ শানে নযুল পড়ুন ▾",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGreen,
                                    fontSize = 11.5.sp
                                )
                            )
                        }
                    }
                }
            }

            // Expanded Full Rich Content
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f),
                        thickness = 0.8.dp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Section Filter Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        ShaneNuzulTab.entries.forEach { tab ->
                            val selected = selectedTab == tab
                            FilterChip(
                                selected = selected,
                                onClick = { selectedTab = tab },
                                label = {
                                    Text(
                                        text = tab.titleBn,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 11.sp
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
                                    borderColor = if (selected) IslamicGreen else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Tab 1: Overview (All Sections Sequentially)
                    when (selectedTab) {
                        ShaneNuzulTab.OVERVIEW -> {
                            ShaneNuzulSectionBox(
                                icon = Icons.Default.Info,
                                iconColor = Color(0xFF0284C7),
                                title = "নামকরণ (Naming)",
                                content = shaneNuzul.naming
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            ShaneNuzulSectionBox(
                                icon = Icons.Default.AutoStories,
                                iconColor = IslamicGreen,
                                title = "নাযিল হওয়ার সময়-কাল ও প্রেক্ষাপট",
                                content = shaneNuzul.period
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            ShaneNuzulSectionBox(
                                icon = Icons.Default.MenuBook,
                                iconColor = Color(0xFFD97706),
                                title = "শানে নযুল ও ঐতিহাসিক পটভূমি",
                                content = shaneNuzul.shaneNuzul
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            ShaneNuzulSectionBox(
                                icon = Icons.Default.Info,
                                iconColor = Color(0xFF7C3AED),
                                title = "মূল বিষয়বস্তু ও দিকনির্দেশনা",
                                content = shaneNuzul.themes
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            if (shaneNuzul.virtues.isNotBlank()) {
                                ShaneNuzulSectionBox(
                                    icon = Icons.Default.Star,
                                    iconColor = Color(0xFFEAB308),
                                    title = "বিশেষ ফজিলত ও প্রামাণ্য হাদীস",
                                    content = shaneNuzul.virtues
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                            }

                            if (shaneNuzul.tafsirPerspectives.isNotBlank()) {
                                ShaneNuzulSectionBox(
                                    icon = Icons.Default.AutoStories,
                                    iconColor = Color(0xFF059669),
                                    title = "তাফসীরকারকদের দৃষ্টিভঙ্গি (ড. আবু বকর যাকারিয়া ও আহসানুল বায়ান)",
                                    content = shaneNuzul.tafsirPerspectives
                                )
                            }
                        }

                        ShaneNuzulTab.NAMING -> {
                            ShaneNuzulSectionBox(
                                icon = Icons.Default.Info,
                                iconColor = Color(0xFF0284C7),
                                title = "নামকরণ (Naming)",
                                content = shaneNuzul.naming
                            )
                        }

                        ShaneNuzulTab.BACKGROUND -> {
                            ShaneNuzulSectionBox(
                                icon = Icons.Default.AutoStories,
                                iconColor = IslamicGreen,
                                title = "নাযিল হওয়ার সময়-কাল ও স্থান",
                                content = shaneNuzul.period
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            ShaneNuzulSectionBox(
                                icon = Icons.Default.MenuBook,
                                iconColor = Color(0xFFD97706),
                                title = "শানে নযুল ও ঐতিহাসিক পটভূমি",
                                content = shaneNuzul.shaneNuzul
                            )
                        }

                        ShaneNuzulTab.THEMES -> {
                            ShaneNuzulSectionBox(
                                icon = Icons.Default.Info,
                                iconColor = Color(0xFF7C3AED),
                                title = "মূল বিষয়বস্তু ও দিকনির্দেশনা",
                                content = shaneNuzul.themes
                            )
                        }

                        ShaneNuzulTab.VIRTUES -> {
                            ShaneNuzulSectionBox(
                                icon = Icons.Default.Star,
                                iconColor = Color(0xFFEAB308),
                                title = "বিশেষ ফজিলত ও প্রামাণ্য হাদীস",
                                content = shaneNuzul.virtues
                            )
                            if (shaneNuzul.tafsirPerspectives.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                ShaneNuzulSectionBox(
                                    icon = Icons.Default.AutoStories,
                                    iconColor = Color(0xFF059669),
                                    title = "তাফসীরকারকদের দৃষ্টিভঙ্গি",
                                    content = shaneNuzul.tafsirPerspectives
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bottom Utility Bar: Copy, Share, Source attribution
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "উৎস: হাদীসবিডি, ড. আবু বকর যাকারিয়া ও আহসানুল বায়ান",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 10.5.sp,
                                color = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.weight(1f)
                        )

                        // Copy Action
                        IconButton(
                            onClick = {
                                val fullText = buildString {
                                    appendLine("সূরা ${surah.nameBn} - শানে নযুল ও পরিচিতি")
                                    appendLine("================================")
                                    appendLine("[নামকরণ]")
                                    appendLine(shaneNuzul.naming)
                                    appendLine()
                                    appendLine("[নাযিল হওয়ার সময়-কাল]")
                                    appendLine(shaneNuzul.period)
                                    appendLine()
                                    appendLine("[শানে নযুল]")
                                    appendLine(shaneNuzul.shaneNuzul)
                                    appendLine()
                                    appendLine("[মূল বিষয়বস্তু]")
                                    appendLine(shaneNuzul.themes)
                                    appendLine()
                                    if (shaneNuzul.virtues.isNotBlank()) {
                                        appendLine("[ফজিলত ও হাদীস]")
                                        appendLine(shaneNuzul.virtues)
                                        appendLine()
                                    }
                                    appendLine("উৎস: দা'ওয়াহ টু জান্নাহ অ্যাপ (হাদীসবিডি ও ড. আবু বকর যাকারিয়া তাফসীর)")
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("শানে নযুল - সূরা ${surah.nameBn}", fullText)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "সূরা ${surah.nameBn}-এর শানে নযুল কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "কপি করুন",
                                tint = IslamicGreen,
                                modifier = Modifier.size(17.dp)
                            )
                        }

                        // Share Action
                        IconButton(
                            onClick = {
                                val shareText = buildString {
                                    appendLine("সূরা ${surah.nameBn}-এর শানে নযুল ও পরিচিতি:")
                                    appendLine(shaneNuzul.shaneNuzul)
                                    appendLine()
                                    appendLine("ফজিলত: ${shaneNuzul.virtues}")
                                    appendLine()
                                    appendLine("দা'ওয়াহ টু জান্নাহ - প্রামাণ্য ইসলামিক অ্যাপ")
                                }
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "সূরা ${surah.nameBn}-এর শানে নযুল শেয়ার করুন"))
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "শেয়ার করুন",
                                tint = IslamicGreen,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShaneNuzulSectionBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    title: String,
    content: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(0.7.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 13.5.sp,
                    lineHeight = 21.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            )
        }
    }
}
