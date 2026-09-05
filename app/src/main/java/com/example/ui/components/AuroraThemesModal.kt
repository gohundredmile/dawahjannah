package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ThemeStyle

enum class ThemeFilterTab(val title: String) {
    ALL("সব থিম (15)"),
    LIGHT("☼ লাইট থিম (14)"),
    DARK("☾ ডার্ক মোড (1)")
}

data class ThemeColorSpec(
    val gradientColors: List<Color>,
    val dotColors: List<Color>,
    val primaryColor: Color
)

fun getThemeColorSpec(style: ThemeStyle): ThemeColorSpec {
    return when (style) {
        ThemeStyle.COSMIC_AURORA -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFF0F172A), Color(0xFF0E7490), Color(0xFF10B981)),
            dotColors = listOf(Color(0xFF06B6D4), Color(0xFF10B981), Color(0xFF818CF8)),
            primaryColor = Color(0xFF06B6D4)
        )
        ThemeStyle.AURORA_AUSTRALIS -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFFF1F2), Color(0xFFFB7185), Color(0xFFFDA4AF)),
            dotColors = listOf(Color(0xFFF43F5E), Color(0xFFFB7185), Color(0xFFFDA4AF)),
            primaryColor = Color(0xFFE11D48)
        )
        ThemeStyle.GLACIAL_AURORA -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFE0F2FE), Color(0xFF7DD3FC), Color(0xFF38BDF8)),
            dotColors = listOf(Color(0xFF0284C7), Color(0xFF06B6D4), Color(0xFF38BDF8)),
            primaryColor = Color(0xFF0284C7)
        )
        ThemeStyle.SOLAR_DAWN -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFEF3C7), Color(0xFFFBBF24), Color(0xFFF59E0B)),
            dotColors = listOf(Color(0xFFD97706), Color(0xFFF59E0B), Color(0xFFFBBF24)),
            primaryColor = Color(0xFFD97706)
        )
        ThemeStyle.ORCHID_DREAM -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFF3E8FF), Color(0xFFD8B4FE), Color(0xFFC084FC)),
            dotColors = listOf(Color(0xFF9333EA), Color(0xFFA855F7), Color(0xFFC084FC)),
            primaryColor = Color(0xFF9333EA)
        )
        ThemeStyle.SAGE_WHISPER -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFECFDF5), Color(0xFFA7F3D0), Color(0xFF6EE7B7)),
            dotColors = listOf(Color(0xFF059669), Color(0xFF10B981), Color(0xFF34D399)),
            primaryColor = Color(0xFF059669)
        )
        ThemeStyle.LAVENDER_MIST -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFEDE9FE), Color(0xFFC4B5FD), Color(0xFFA78BFA)),
            dotColors = listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6), Color(0xFFA78BFA)),
            primaryColor = Color(0xFF7C3AED)
        )
        ThemeStyle.PEACH_BLOSSOM -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFFEDD5), Color(0xFFFDBA74), Color(0xFFFB923C)),
            dotColors = listOf(Color(0xFFEA580C), Color(0xFFF97316), Color(0xFFFB923C)),
            primaryColor = Color(0xFFEA580C)
        )
        ThemeStyle.OCEAN_BREEZE -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFE0F2FE), Color(0xFFBAE6FD), Color(0xFF7DD3FC)),
            dotColors = listOf(Color(0xFF0284C7), Color(0xFF38BDF8), Color(0xFF7DD3FC)),
            primaryColor = Color(0xFF0284C7)
        )
        ThemeStyle.CHAMOMILE_TEA -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFEF9C3), Color(0xFFFDE047), Color(0xFFFACC15)),
            dotColors = listOf(Color(0xFFB45309), Color(0xFFD97706), Color(0xFFF59E0B)),
            primaryColor = Color(0xFFB45309)
        )
        ThemeStyle.FOREST_BATHING, ThemeStyle.EMERALD_JANNAH -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFDCFCE7), Color(0xFF86EFAC), Color(0xFF4ADE80)),
            dotColors = listOf(Color(0xFF15803D), Color(0xFF16A34A), Color(0xFF22C55E)),
            primaryColor = Color(0xFF15803D)
        )
        ThemeStyle.ETHEREAL_SAND -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFEF3C7), Color(0xFFFDE68A), Color(0xFFFCD34D)),
            dotColors = listOf(Color(0xFF92400E), Color(0xFFB45309), Color(0xFFD97706)),
            primaryColor = Color(0xFF92400E)
        )
        ThemeStyle.VELVET_PLUM -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFAE8FF), Color(0xFFF0ABFC), Color(0xFFE879F9)),
            dotColors = listOf(Color(0xFFA21CAF), Color(0xFFC026D3), Color(0xFFE879F9)),
            primaryColor = Color(0xFFA21CAF)
        )
        ThemeStyle.SILVER_BIRCH -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFF1F5F9), Color(0xFFCBD5E1), Color(0xFF94A3B8)),
            dotColors = listOf(Color(0xFF334155), Color(0xFF475569), Color(0xFF64748B)),
            primaryColor = Color(0xFF334155)
        )
        ThemeStyle.MINT_MATCHA -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFDCFCE7), Color(0xFFBBF7D0), Color(0xFF86EFAC)),
            dotColors = listOf(Color(0xFF16A34A), Color(0xFF22C55E), Color(0xFF4ADE80)),
            primaryColor = Color(0xFF16A34A)
        )
    }
}

@Composable
fun AuroraThemesModal(
    currentTheme: ThemeStyle,
    onSelectTheme: (ThemeStyle) -> Unit,
    onSelectRandom: () -> Unit,
    onDismiss: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf(ThemeFilterTab.ALL) }

    // Unique list of 15 themes (excluding EMERALD_JANNAH which is aliased to FOREST_BATHING)
    val allThemes = remember {
        ThemeStyle.entries.filter { it != ThemeStyle.EMERALD_JANNAH }
    }

    val filteredThemes = remember(selectedFilter) {
        when (selectedFilter) {
            ThemeFilterTab.ALL -> allThemes
            ThemeFilterTab.LIGHT -> allThemes.filter { !it.isDark }
            ThemeFilterTab.DARK -> allThemes.filter { it.isDark }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("aurora_themes_modal"),
            color = Color.White,
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {
                // -------------------------------------------------------------
                // 1. TOP HEADER: Palette Icon, Title, Random Button, Close
                // -------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Mint Green Icon Capsule
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE8FDF3)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "সকল অরোরা থিমসমূহ (All 15 Themes)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                fontSize = 17.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "আপনার পছন্দের রঙ ও আবহের থিম নির্বাচন করুন • স্বয়ংক্রিয়ভাবে সংরক্ষিত থাকবে",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                fontSize = 11.5.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Random Button
                        OutlinedButton(
                            onClick = onSelectRandom,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = Color(0xFFF8FAFC)
                            ),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("theme_random_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Shuffle,
                                contentDescription = "Random",
                                tint = Color(0xFF475569),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "র‍্যান্ডম",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF475569)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        // Close Button
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // -------------------------------------------------------------
                // 2. FILTER TABS & CURRENT THEME PILL
                // -------------------------------------------------------------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ThemeFilterTab.entries.forEach { tab ->
                            val isSelected = selectedFilter == tab
                            Surface(
                                onClick = { selectedFilter = tab },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)
                                ),
                                modifier = Modifier.testTag("filter_${tab.name}")
                            ) {
                                Text(
                                    text = tab.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else Color(0xFF475569),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Current Active Theme Label
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF8FAFC))
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        val activeSpec = getThemeColorSpec(currentTheme)
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(activeSpec.primaryColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "বর্তমান: ${currentTheme.displayNameEn}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF334155)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // -------------------------------------------------------------
                // 3. THEME CARDS GRID
                // -------------------------------------------------------------
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 260.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(filteredThemes, key = { it.name }) { theme ->
                        val isSelected = (theme == currentTheme) || (theme == ThemeStyle.FOREST_BATHING && currentTheme == ThemeStyle.EMERALD_JANNAH)
                        ThemeCardItem(
                            theme = theme,
                            isSelected = isSelected,
                            onSelect = { onSelectTheme(theme) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // -------------------------------------------------------------
                // 4. FOOTER: Local storage indicator & Done Button
                // -------------------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "✨ আপনার ডিভাইসে স্বয়ংক্রিয়ভাবে সংরক্ষিত থাকে (Instant Local Storage)",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        color = Color(0xFF64748B),
                        modifier = Modifier.weight(1f)
                    )

                    Button(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF10B981)
                        ),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 8.dp),
                        modifier = Modifier.testTag("theme_modal_done_button")
                    ) {
                        Text(
                            text = "সম্পন্ন",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeCardItem(
    theme: ThemeStyle,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val spec = remember(theme) { getThemeColorSpec(theme) }
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF10B981) else Color(0xFFE2E8F0),
        label = "border_color"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onSelect() }
            .testTag("theme_card_${theme.slug.replace("#", "")}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (theme.isDark) Color(0xFF0F172A) else Color.White
        ),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 0.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Capsule banner at top with gradient and 3 color dots
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.horizontalGradient(spec.gradientColors)),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Translucent Capsule for 3 Color Preview Dots
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        spec.dotColors.forEach { dotColor ->
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .clip(CircleShape)
                                    .background(dotColor)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title + Active Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = theme.displayNameEn,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = if (theme.isDark) Color.White else Color(0xFF0F172A)
                )

                if (isSelected) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color(0xFFECFDF5),
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "সক্রিয়",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Bengali Subtitle / Description
            Text(
                text = "${theme.displayNameBn} (${theme.descriptionBn})",
                fontSize = 11.5.sp,
                color = if (theme.isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bottom Row: Slug + Selection status
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = theme.slug,
                    fontSize = 11.sp,
                    color = if (theme.isDark) Color(0xFF64748B) else Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )

                if (isSelected) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "নির্বাচিত",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }
                } else {
                    Text(
                        text = "ক্লিক করুন",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}
