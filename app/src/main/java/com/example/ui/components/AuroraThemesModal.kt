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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import com.example.data.model.AuroraWallpaperConfig
import com.example.data.model.AuroraWavePreset
import com.example.data.model.ScreenEffectMode
import com.example.data.model.ThemeStyle

enum class ModalSectionTab(val title: String) {
    THEMES("🎨 কালার প্যালেট"),
    AURORA_WAVES("🌊 অরোরা ওয়েভ"),
    SCREEN_EFFECT("🪟 স্ক্রিন ইফেক্ট")
}

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
            gradientColors = listOf(Color(0xFFFEFCE8), Color(0xFFFEF08A), Color(0xFFFDE047)),
            dotColors = listOf(Color(0xFFCA8A04), Color(0xFFEAB308), Color(0xFFFACC15)),
            primaryColor = Color(0xFFCA8A04)
        )
        ThemeStyle.FOREST_BATHING, ThemeStyle.EMERALD_JANNAH -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFF0FDF4), Color(0xFFA7F3D0), Color(0xFF6EE7B7)),
            dotColors = listOf(Color(0xFF047857), Color(0xFF059669), Color(0xFF10B981)),
            primaryColor = Color(0xFF047857)
        )
        ThemeStyle.ETHEREAL_SAND -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFFFBEB), Color(0xFFFDE68A), Color(0xFFFCD34D)),
            dotColors = listOf(Color(0xFFB45309), Color(0xFFD97706), Color(0xFFF59E0B)),
            primaryColor = Color(0xFFB45309)
        )
        ThemeStyle.VELVET_PLUM -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFFDF2F8), Color(0xFFFBCFE8), Color(0xFFF9A8D4)),
            dotColors = listOf(Color(0xFF9D174D), Color(0xFFBE185D), Color(0xFFDB2777)),
            primaryColor = Color(0xFF9D174D)
        )
        ThemeStyle.SILVER_BIRCH -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFF8FAFC), Color(0xFFE2E8F0), Color(0xFFCBD5E1)),
            dotColors = listOf(Color(0xFF475569), Color(0xFF64748B), Color(0xFF94A3B8)),
            primaryColor = Color(0xFF475569)
        )
        ThemeStyle.MINT_MATCHA -> ThemeColorSpec(
            gradientColors = listOf(Color(0xFFF0FDF4), Color(0xFFBBF7D0), Color(0xFF86EFAC)),
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
    auroraConfig: AuroraWallpaperConfig = AuroraWallpaperConfig(),
    onUpdateAuroraConfig: (AuroraWallpaperConfig) -> Unit = {},
    initialTab: ModalSectionTab = ModalSectionTab.THEMES,
    currentScreenEffectMode: ScreenEffectMode = ScreenEffectMode.GLASS,
    onSelectScreenEffectMode: (ScreenEffectMode) -> Unit = {},
    onDismiss: () -> Unit
) {
    var activeModalTab by remember(initialTab) { mutableStateOf(initialTab) }
    var selectedFilter by remember { mutableStateOf(ThemeFilterTab.ALL) }

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
                .fillMaxHeight(0.93f)
                .clip(RoundedCornerShape(24.dp))
                .testTag("aurora_themes_modal"),
            color = Color.White,
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp, vertical = 16.dp)
            ) {
                // -------------------------------------------------------------
                // 1. TOP HEADER: Palette Icon, Title, Random Button, Close
                // -------------------------------------------------------------
                val headerIcon = when (activeModalTab) {
                    ModalSectionTab.THEMES -> Icons.Default.Palette
                    ModalSectionTab.AURORA_WAVES -> Icons.Default.Waves
                    ModalSectionTab.SCREEN_EFFECT -> Icons.Default.Layers
                }
                val headerTitle = when (activeModalTab) {
                    ModalSectionTab.THEMES -> "সকল অরোরা থিমসমূহ"
                    ModalSectionTab.AURORA_WAVES -> "লাইভ অরোরা ওয়েভ ওয়ালপেপার"
                    ModalSectionTab.SCREEN_EFFECT -> "উইন্ডো / স্ক্রিন ইফেক্ট মোড"
                }
                val headerSubtitle = when (activeModalTab) {
                    ModalSectionTab.THEMES -> "১৫টি স্নিগ্ধ থিম ও রঙ নির্বাচন করুন"
                    ModalSectionTab.AURORA_WAVES -> "জীবন্ত সুদৃশ্য ঢেউ, নূরানি কণা ও উজ্জ্বলতা পরিবর্তন"
                    ModalSectionTab.SCREEN_EFFECT -> "স্বাভাবিক মোড (Normal) অথবা গ্লাস ইফেক্ট (Glass Effect)"
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(13.dp))
                                .background(Color(0xFFE8FDF3)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = headerIcon,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(23.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = headerTitle,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
                                fontSize = 16.5.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = headerSubtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                fontSize = 11.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (activeModalTab == ModalSectionTab.THEMES) {
                            OutlinedButton(
                                onClick = onSelectRandom,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = Color(0xFFF8FAFC)
                                ),
                                contentPadding = PaddingValues(horizontal = 9.dp, vertical = 5.dp),
                                modifier = Modifier.testTag("theme_random_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Shuffle,
                                    contentDescription = "Random",
                                    tint = Color(0xFF475569),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "র‍্যান্ডম",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF475569)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFF1F5F9))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // -------------------------------------------------------------
                // 2. MAIN MODAL SEGMENTED TABS: [🎨 থিম প্যালেট] | [🌊 লাইভ অরোরা ওয়েভ]
                // -------------------------------------------------------------
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFF1F5F9),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ModalSectionTab.entries.forEach { tab ->
                            val isSelected = activeModalTab == tab
                            Surface(
                                onClick = { activeModalTab = tab },
                                shape = RoundedCornerShape(11.dp),
                                color = if (isSelected) Color.White else Color.Transparent,
                                shadowElevation = if (isSelected) 2.dp else 0.dp,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("modal_tab_${tab.name}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tab.title,
                                        fontSize = 11.5.sp,
                                        maxLines = 1,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF0F172A) else Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // -------------------------------------------------------------
                // 3. TAB CONTENT
                // -------------------------------------------------------------
                if (activeModalTab == ModalSectionTab.THEMES) {
                    // Quick Aurora Status Strip on Themes tab
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { activeModalTab = ModalSectionTab.AURORA_WAVES }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .clip(CircleShape)
                                        .background(if (auroraConfig.isEnabled) Color(0xFF10B981) else Color(0xFF94A3B8))
                                )
                                Text(
                                    text = "লাইভ অরোরা ওয়েভ: ${if (auroraConfig.isEnabled) "চালু" else "বন্ধ"} (${auroraConfig.preset.titleBn})",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF334155)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "ওয়েভ কাস্টমাইজ ➔",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Filter tabs (All, Light, Dark)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            ThemeFilterTab.entries.forEach { tab ->
                                val isSelected = selectedFilter == tab
                                Surface(
                                    onClick = { selectedFilter = tab },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)
                                    ),
                                    modifier = Modifier.testTag("filter_${tab.name}")
                                ) {
                                    Text(
                                        text = tab.title,
                                        fontSize = 11.5.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color.White else Color(0xFF475569),
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Current theme badge
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF8FAFC))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            val activeSpec = getThemeColorSpec(currentTheme)
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .clip(CircleShape)
                                    .background(activeSpec.primaryColor)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = currentTheme.displayNameEn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF334155)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Theme Grid
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 250.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 6.dp)
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
                } else if (activeModalTab == ModalSectionTab.AURORA_WAVES) {
                    // =========================================================
                    // 🌊 LIVE AURORA WAVE WALLPAPER CUSTOMIZATION SECTION
                    // =========================================================
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        // A. Real-time Live Wallpaper Viewport Preview
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(135.dp),
                                shape = RoundedCornerShape(18.dp),
                                border = BorderStroke(1.5.dp, Color(0xFFE2E8F0)),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                            ) {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    // Render real LiveAuroraWallpaperBackground inside preview
                                    LiveAuroraWallpaperBackground(
                                        config = auroraConfig,
                                        themeStyle = currentTheme,
                                        isDark = currentTheme.isDark
                                    )

                                    // Top-left preview badge
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color.Black.copy(alpha = 0.55f),
                                        modifier = Modifier
                                            .align(Alignment.TopStart)
                                            .padding(10.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Waves,
                                                contentDescription = null,
                                                tint = Color(0xFF34D399),
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Text(
                                                text = "লাইভ প্রিভিউ (Live Wave Preview)",
                                                fontSize = 10.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    // Bottom status pill
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = Color.White.copy(alpha = 0.9f),
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .padding(10.dp)
                                    ) {
                                        Text(
                                            text = if (auroraConfig.isEnabled) "● তরঙ্গ চলমান" else "○ বন্ধ",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (auroraConfig.isEnabled) Color(0xFF059669) else Color(0xFF64748B),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }

                        // B. Master Switch Row
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(CircleShape)
                                                .background(if (auroraConfig.isEnabled) Color(0xFFECFDF5) else Color(0xFFF1F5F9)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Waves,
                                                contentDescription = null,
                                                tint = if (auroraConfig.isEnabled) Color(0xFF059669) else Color(0xFF64748B),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "লাইভ অরোরা ওয়েভ ওয়ালপেপার",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.5.sp,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = if (auroraConfig.isEnabled) "অ্যাপের সর্বত্র জীবন্ত আলো ও তরঙ্গের প্রবাহ সক্রিয়" else "ওয়ালপেপার স্থির ও বন্ধ",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = auroraConfig.isEnabled,
                                        onCheckedChange = { isChecked ->
                                            onUpdateAuroraConfig(auroraConfig.copy(isEnabled = isChecked))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }
                        }

                        // C. Wave Preset Styles
                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "ওয়েভ স্টাইল ও কালার প্যালেট",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1E293B)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    AuroraWavePreset.entries.forEach { preset ->
                                        val isSelected = auroraConfig.preset == preset
                                        val previewColors = if (preset == AuroraWavePreset.THEME_DYNAMIC) {
                                            auroraConfig.getEffectiveColors(currentTheme)
                                        } else {
                                            preset.colors
                                        }

                                        Surface(
                                            onClick = {
                                                onUpdateAuroraConfig(auroraConfig.copy(preset = preset))
                                            },
                                            shape = RoundedCornerShape(12.dp),
                                            color = if (isSelected) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
                                            border = BorderStroke(
                                                if (isSelected) 1.5.dp else 1.dp,
                                                if (isSelected) Color(0xFF10B981) else Color(0xFFE2E8F0)
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(horizontal = 12.dp, vertical = 9.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.SpaceBetween
                                            ) {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                                    modifier = Modifier.weight(1f)
                                                ) {
                                                    // Color Dot Capsule
                                                    Row(
                                                        horizontalArrangement = Arrangement.spacedBy(3.dp),
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        previewColors.take(3).forEach { color ->
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(9.dp)
                                                                    .clip(CircleShape)
                                                                    .background(color)
                                                            )
                                                        }
                                                    }

                                                    Column {
                                                        Text(
                                                            text = preset.titleBn,
                                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                            fontSize = 13.sp,
                                                            color = if (isSelected) Color(0xFF065F46) else Color(0xFF1E293B)
                                                        )
                                                        Text(
                                                            text = preset.descriptionBn,
                                                            fontSize = 11.sp,
                                                            color = Color(0xFF64748B),
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }
                                                }

                                                if (isSelected) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Selected",
                                                        tint = Color(0xFF10B981),
                                                        modifier = Modifier.size(18.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // D. Wave Visibility & Intensity
                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "তরঙ্গের দৃশ্যমানতা ও উজ্জ্বলতা",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1E293B)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val levels = listOf(
                                        Triple("মৃদু (35%)", 0.35f, "স্নিগ্ধ"),
                                        Triple("উজ্জ্বল (65%)", 0.65f, "স্পষ্ট দৃশ্যমান"),
                                        Triple("দীপ্তিময় (90%)", 0.90f, "প্রবল নূর")
                                    )
                                    levels.forEach { (label, value, sub) ->
                                        val isSelected = Math.abs(auroraConfig.intensity - value) < 0.1f
                                        Surface(
                                            onClick = {
                                                onUpdateAuroraConfig(auroraConfig.copy(intensity = value))
                                            },
                                            shape = RoundedCornerShape(11.dp),
                                            color = if (isSelected) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)
                                            ),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp, horizontal = 6.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontSize = 12.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color.White else Color(0xFF334155)
                                                )
                                                Text(
                                                    text = sub,
                                                    fontSize = 9.5.sp,
                                                    color = if (isSelected) Color(0xFF94A3B8) else Color(0xFF64748B)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // E. Wave Flow Speed
                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Speed,
                                        contentDescription = null,
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "ওয়েভ প্রবাহের গতি (Flow Speed)",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = Color(0xFF1E293B)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val speeds = listOf(
                                        Pair("শান্ত ধীর (0.6x)", 0.6f),
                                        Pair("স্বাভাবিক (1.0x)", 1.0f),
                                        Pair("গতিশীল (1.5x)", 1.5f)
                                    )
                                    speeds.forEach { (label, value) ->
                                        val isSelected = Math.abs(auroraConfig.speed - value) < 0.15f
                                        Surface(
                                            onClick = {
                                                onUpdateAuroraConfig(auroraConfig.copy(speed = value))
                                            },
                                            shape = RoundedCornerShape(11.dp),
                                            color = if (isSelected) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                                            border = BorderStroke(
                                                1.dp,
                                                if (isSelected) Color(0xFF0F172A) else Color(0xFFE2E8F0)
                                            ),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                                horizontalArrangement = Arrangement.Center,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color.White else Color(0xFF334155)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // F. Floating Celestial Noor Particles & Stars Toggle
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFFEF3C7)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AutoAwesome,
                                                contentDescription = null,
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "নূরানি আলোককণা ও নক্ষত্র প্রভাব",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "আকাশে আলতো ভাসমান আধ্যাত্মিক আলোর কণা",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = auroraConfig.showParticles,
                                        onCheckedChange = { isChecked ->
                                            onUpdateAuroraConfig(auroraConfig.copy(showParticles = isChecked))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }
                        }

                        // G. Glowing Wave Crest Contours Toggle
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFE0F2FE)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Waves,
                                                contentDescription = null,
                                                tint = Color(0xFF0284C7),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "তরঙ্গের উজ্জ্বল কিনারা ও আভা",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "তরঙ্গের বাঁকে স্পষ্ট দীপ্তিময় আউটলাইন রেখা",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = auroraConfig.showWaveContours,
                                        onCheckedChange = { isChecked ->
                                            onUpdateAuroraConfig(auroraConfig.copy(showWaveContours = isChecked))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }
                        }

                        // H. Northern Lights Aurora Ray Pillars Toggle
                        item {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(34.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEDE9FE)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Filled.AutoAwesome,
                                                contentDescription = null,
                                                tint = Color(0xFF7C3AED),
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Column {
                                            Text(
                                                text = "নর্দার্ন লাইটস আলোর স্তম্ভ (Aurora Pillars)",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "আকাশে ধীরগতির ঊর্ধ্বমুখী আলোর রশ্মি ও পর্দা",
                                                fontSize = 11.sp,
                                                color = Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = auroraConfig.showAuroraRays,
                                        onCheckedChange = { isChecked ->
                                            onUpdateAuroraConfig(auroraConfig.copy(showAuroraRays = isChecked))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // =========================================================
                    // 🪟 WINDOW / SCREEN EFFECT MODE SECTION
                    // =========================================================
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 8.dp)
                    ) {
                        // A. Explanatory Header Banner
                        item {
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
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
                                            .size(42.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFECFDF5)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Layers,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(22.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "উইন্ডো ও স্ক্রিন ভিজ্যুয়াল শৈলী",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = Color(0xFF0F172A)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = "অ্যাপের হোম স্ক্রিন ও কার্ডসমূহের জন্য আপনার পছন্দের মোড বেছে নিন।",
                                            fontSize = 11.5.sp,
                                            color = Color(0xFF64748B),
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        // B. Option 1: স্বাভাবিক মোড (Normal Mode)
                        item {
                            val isNormal = currentScreenEffectMode == ScreenEffectMode.NORMAL
                            Surface(
                                onClick = { onSelectScreenEffectMode(ScreenEffectMode.NORMAL) },
                                shape = RoundedCornerShape(18.dp),
                                color = if (isNormal) Color(0xFFF0FDF4) else Color.White,
                                border = BorderStroke(
                                    if (isNormal) 2.dp else 1.dp,
                                    if (isNormal) Color(0xFF10B981) else Color(0xFFE2E8F0)
                                ),
                                shadowElevation = if (isNormal) 3.dp else 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("screen_effect_normal_option")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(if (isNormal) Color(0xFF10B981) else Color(0xFFF1F5F9)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.PhoneAndroid,
                                                    contentDescription = null,
                                                    tint = if (isNormal) Color.White else Color(0xFF64748B),
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = "১. স্বাভাবিক মোড (Normal Mode)",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = if (isNormal) Color(0xFF065F46) else Color(0xFF0F172A)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "ট্যাপ করলে পূর্বের সাধারণ হোম স্ক্রিন ফিরবে",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (isNormal) Color(0xFF059669) else Color(0xFF64748B)
                                                )
                                            }
                                        }

                                        RadioButton(
                                            selected = isNormal,
                                            onClick = { onSelectScreenEffectMode(ScreenEffectMode.NORMAL) },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF10B981),
                                                unselectedColor = Color(0xFF94A3B8)
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "• ক্লাসিক সলিড ম্যাটেরিয়াল সারফেস\n• অতিরিক্ত স্বচ্ছতা বা তরঙ্গের রিফ্লেকশন ছাড়া সাধারণ পরিচ্ছন্ন ডিসপ্লে\n• সর্বোত্তম গতি ও পারফরম্যান্স ফ্রেন্ডলি শৈলী",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        // C. Option 2: গ্লাস ইফেক্ট (Glass Effect)
                        item {
                            val isGlass = currentScreenEffectMode == ScreenEffectMode.GLASS
                            Surface(
                                onClick = { onSelectScreenEffectMode(ScreenEffectMode.GLASS) },
                                shape = RoundedCornerShape(18.dp),
                                color = if (isGlass) Color(0xFFECFDF5) else Color.White,
                                border = BorderStroke(
                                    if (isGlass) 2.dp else 1.dp,
                                    if (isGlass) Color(0xFF10B981) else Color(0xFFE2E8F0)
                                ),
                                shadowElevation = if (isGlass) 3.dp else 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("screen_effect_glass_option")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(if (isGlass) Color(0xFF059669) else Color(0xFFF1F5F9)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.AutoAwesome,
                                                    contentDescription = null,
                                                    tint = if (isGlass) Color.White else Color(0xFF64748B),
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(12.dp))
                                            Column {
                                                Text(
                                                    text = "২. গ্লাস ইফেক্ট (Glass Effect)",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 14.sp,
                                                    color = if (isGlass) Color(0xFF065F46) else Color(0xFF0F172A)
                                                )
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "ফ্রস্টেড গ্লাস ও লিকুইড ওয়েভ ইফেক্ট",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = if (isGlass) Color(0xFF059669) else Color(0xFF64748B)
                                                )
                                            }
                                        }

                                        RadioButton(
                                            selected = isGlass,
                                            onClick = { onSelectScreenEffectMode(ScreenEffectMode.GLASS) },
                                            colors = RadioButtonDefaults.colors(
                                                selectedColor = Color(0xFF10B981),
                                                unselectedColor = Color(0xFF94A3B8)
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(10.dp))
                                    Text(
                                        text = "• অ্যারোরা ফ্রস্টেড গ্লাস ও স্পেকুলার গ্লেয়ার হাইলাইট\n• প্রতিটি প্রধান কার্ডের ভেতরে মনোরম জীবন্ত লিকুইড ওয়েভ মোশন\n• নূরানি আভা ও আধুনিক ইসলামিক গ্লাস ভিজ্যুয়াল",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF475569),
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }

                        // D. Live Real-Time Interactive Card Preview
                        item {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "কার্ডের নমুনা (Live Preview): ${currentScreenEffectMode.titleBn}",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF0F172A)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                CompositionLocalProvider(
                                    LocalScreenEffectMode provides currentScreenEffectMode
                                ) {
                                    PureGlassCard(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(18.dp),
                                        tint = Color(0xFFF0FDF4),
                                        accentBorderColor = Color(0xFF10B981),
                                        borderWidth = 1.0.dp,
                                        elevation = 2.dp,
                                        isDark = currentTheme.isDark,
                                        showWaveEffect = true
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Surface(
                                                    shape = CircleShape,
                                                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                                                    modifier = Modifier.size(38.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = if (currentScreenEffectMode == ScreenEffectMode.GLASS) Icons.Default.AutoAwesome else Icons.Default.PhoneAndroid,
                                                            contentDescription = null,
                                                            tint = Color(0xFF059669),
                                                            modifier = Modifier.size(20.dp)
                                                        )
                                                    }
                                                }
                                                Spacer(modifier = Modifier.width(10.dp))
                                                Column {
                                                    Text(
                                                        text = "নমুনা প্রিভিউ • " + currentScreenEffectMode.titleBn,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 13.5.sp,
                                                        color = Color(0xFF0F172A)
                                                    )
                                                    Text(
                                                        text = currentScreenEffectMode.subtitleBn,
                                                        fontSize = 11.5.sp,
                                                        color = Color(0xFF64748B)
                                                    )
                                                }
                                            }

                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF10B981)
                                            ) {
                                                Text(
                                                    text = if (currentScreenEffectMode == ScreenEffectMode.GLASS) "গ্লাস মোড" else "স্বাভাবিক মোড",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
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
                        text = "✨ সেটিংসে যেকোনো পরিবর্তন সাথে সাথে সংরক্ষিত হয়",
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
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 7.dp),
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Brush.horizontalGradient(spec.gradientColors)),
                contentAlignment = Alignment.CenterEnd
            ) {
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

            Text(
                text = "${theme.displayNameBn} (${theme.descriptionBn})",
                fontSize = 11.5.sp,
                color = if (theme.isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

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
