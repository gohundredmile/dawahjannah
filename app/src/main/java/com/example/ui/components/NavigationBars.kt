package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.AppTab

val IslamicTitleFontFamily: FontFamily by lazy {
    try {
        FontFamily(
            Font(resId = R.font.font_tiro_bangla, weight = FontWeight.Bold),
            Font(resId = R.font.font_tiro_bangla, weight = FontWeight.Normal)
        )
    } catch (e: Throwable) {
        FontFamily.Serif
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeIslamicTopAppBar(
    onOpenFontMenu: () -> Unit = {},
    onOpenThemeModal: () -> Unit = {},
    onOpenAppSettings: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val goldDivider = IslamicGold.copy(alpha = if (isDark) 0.35f else 0.45f)
    var showSettingsMenu by remember { mutableStateOf(false) }
    val fontScaleController = LocalFontScaleController.current

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.2.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            goldDivider.copy(alpha = 0.05f),
                            goldDivider,
                            IslamicGoldLight.copy(alpha = 0.9f),
                            goldDivider,
                            goldDivider.copy(alpha = 0.05f)
                        )
                    ),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            },
        color = MaterialTheme.colorScheme.surface.copy(alpha = if (isDark) 0.92f else 0.96f),
        shadowElevation = 2.dp
    ) {
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    // Authentic Islamic Logo Medallion Badge
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0xFF06281E) else Color(0xFFE8F5E9),
                        border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.7f)),
                        shadowElevation = 2.dp,
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_logo_medallion),
                                contentDescription = "দা'ওয়াহ টু জান্নাহ্ লোগো",
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "দা'ওয়াহ টু জান্নাহ্",
                                fontFamily = IslamicTitleFontFamily,
                                fontWeight = FontWeight.Bold,
                                fontSize = 21.sp,
                                letterSpacing = 0.3.sp,
                                color = if (isDark) IslamicGoldLight else Color(0xFF064E3B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "✦",
                                fontSize = 11.sp,
                                color = IslamicGold,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = "দ্বীন ও সুন্নাহর নূরানি ডিজিটাল সঙ্গী",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f),
                            letterSpacing = 0.2.sp
                        )
                    }
                }
            },
            actions = {
                // High-Contrast "Settings" button at the top-right
                Box(
                    modifier = Modifier.padding(end = 6.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = if (showSettingsMenu) {
                            if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                        } else {
                            if (isDark) Color(0xFF1E293B) else MaterialTheme.colorScheme.primary
                        },
                        border = BorderStroke(
                            1.2.dp,
                            if (isDark) IslamicGold else IslamicGold.copy(alpha = 0.75f)
                        ),
                        shadowElevation = if (showSettingsMenu) 3.dp else 2.dp,
                        modifier = Modifier
                            .clip(RoundedCornerShape(18.dp))
                            .clickable { showSettingsMenu = !showSettingsMenu }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 11.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = if (showSettingsMenu && isDark) Color(0xFF0F172A) else Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Settings",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (showSettingsMenu && isDark) Color(0xFF0F172A) else Color.White
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = if (showSettingsMenu && isDark) Color(0xFF0F172A) else Color.White,
                                modifier = Modifier
                                    .size(16.dp)
                                    .rotate(if (showSettingsMenu) 180f else 0f)
                            )
                        }
                    }

                    // Folded Dropdown Menu containing App Settings and Visual items
                    DropdownMenu(
                        expanded = showSettingsMenu,
                        onDismissRequest = { showSettingsMenu = false },
                        modifier = Modifier
                            .widthIn(min = 275.dp, max = 305.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(16.dp))
                            .border(BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.45f)), RoundedCornerShape(16.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        // Header
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.45f else 0.7f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "সেটিংস ও কাস্টমাইজেশন",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "অ্যাপ সেটিংস্, ফন্ট ও থিম শৈলী",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )

                        // 1: অ্যাপ সেটিংস্ (App Settings)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    showSettingsMenu = false
                                    onOpenAppSettings()
                                }
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.45f else 0.7f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "অ্যাপ সেটিংস্",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "নামাজের হিসাব পদ্ধতি, জেলা ও কনফিগারেশন",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier.size(12.dp)
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )

                        // 1 & 2: Universal Font Scaling (A- and A+)
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.FormatSize,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ফন্ট সাইজ স্কেলিং",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Text(
                                    text = "${((fontScaleController?.scale ?: 1f) * 100).toInt()}%",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // A- Button
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (fontScaleController?.canDecrease == true) {
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.35f else 0.65f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        IslamicGold.copy(alpha = if (fontScaleController?.canDecrease == true) 0.55f else 0.2f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable(enabled = fontScaleController?.canDecrease == true) {
                                            fontScaleController?.onDecrease?.invoke()
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "A- ছোট",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (fontScaleController?.canDecrease == true) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                            }
                                        )
                                    }
                                }

                                // 100% Reset Button
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = if (isDark) 0.25f else 0.45f),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                    modifier = Modifier
                                        .weight(0.85f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            fontScaleController?.onReset?.invoke()
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "১০০%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                // A+ Button
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (fontScaleController?.canIncrease == true) {
                                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.35f else 0.65f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                                    },
                                    border = BorderStroke(
                                        1.dp,
                                        IslamicGold.copy(alpha = if (fontScaleController?.canIncrease == true) 0.55f else 0.2f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(38.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable(enabled = fontScaleController?.canIncrease == true) {
                                            fontScaleController?.onIncrease?.invoke()
                                        }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = "A+ বড়",
                                            style = MaterialTheme.typography.labelMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = if (fontScaleController?.canIncrease == true) {
                                                MaterialTheme.colorScheme.primary
                                            } else {
                                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        )

                        // 3: Bangla Font Picker Item
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    showSettingsMenu = false
                                    onOpenFontMenu()
                                }
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.45f else 0.7f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FontDownload,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "বাংলা ফন্ট নির্বাচন",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "নূরানি, আদর্শলিপি, সিয়াম রুপালী ইত্যাদি",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f)
                        )

                        // 4: Aurora Theme & Wallpaper Item
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable {
                                    showSettingsMenu = false
                                    onOpenThemeModal()
                                }
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isDark) 0.45f else 0.7f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Palette,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "থিম ও কালার শৈলী",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "নূরানি গোল্ড, অরোরা ও ব্যাকগ্রাউন্ড ওয়ালপেপার",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 10.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DawahTopAppBar(
    title: String,
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},
    showFontControls: Boolean = true,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            if (showFontControls) {
                FontSizeActionButtons()
            }
            actions()
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
        )
    )
}

@Composable
fun DawahBottomNavigationBar(
    currentTab: AppTab,
    onTabSelected: (AppTab) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        tonalElevation = 2.dp
    ) {
        NavigationBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            val navItemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            @Composable
            fun BottomTabItemLabel(text: String, isSelected: Boolean) {
                Text(
                    text = text.replace(" ", "\u00A0"),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        letterSpacing = (-0.35).sp,
                        lineHeight = 11.sp
                    ),
                    fontFamily = LocalBanglaFontFamily.current,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }

            NavigationBarItem(
                selected = currentTab == AppTab.HOME,
                onClick = { onTabSelected(AppTab.HOME) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home"
                    )
                },
                label = {
                    BottomTabItemLabel(AppTab.HOME.titleBn, currentTab == AppTab.HOME)
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.DUA,
                onClick = { onTabSelected(AppTab.DUA) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.DUA) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                        contentDescription = "Masnun Dua"
                    )
                },
                label = {
                    BottomTabItemLabel(AppTab.DUA.titleBn, currentTab == AppTab.DUA)
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.ROUTINE,
                onClick = { onTabSelected(AppTab.ROUTINE) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.ROUTINE) Icons.Filled.AccessTime else Icons.Outlined.AccessTime,
                        contentDescription = "24h Routine"
                    )
                },
                label = {
                    BottomTabItemLabel(AppTab.ROUTINE.titleBn, currentTab == AppTab.ROUTINE)
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.TASBIH,
                onClick = { onTabSelected(AppTab.TASBIH) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.TASBIH) Icons.Filled.TouchApp else Icons.Outlined.TouchApp,
                        contentDescription = "Tasbih"
                    )
                },
                label = {
                    BottomTabItemLabel(AppTab.TASBIH.titleBn, currentTab == AppTab.TASBIH)
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.MORE,
                onClick = { onTabSelected(AppTab.MORE) },
                alwaysShowLabel = true,
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.MORE) Icons.Filled.MoreHoriz else Icons.Outlined.MoreHoriz,
                        contentDescription = "More"
                    )
                },
                label = {
                    BottomTabItemLabel(AppTab.MORE.titleBn, currentTab == AppTab.MORE)
                },
                colors = navItemColors
            )
        }
    }
}
