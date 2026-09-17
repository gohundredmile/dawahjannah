package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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

/**
 * Controller interface provided to all screens via CompositionLocal to control
 * font size decrease (A-) and increase (A+) globally with persistent storage.
 */
data class FontScaleController(
    val scale: Float,
    val canDecrease: Boolean = true,
    val canIncrease: Boolean = true,
    val onDecrease: () -> Unit,
    val onIncrease: () -> Unit,
    val onReset: () -> Unit = {}
)

val LocalFontScaleController = staticCompositionLocalOf<FontScaleController?> { null }

/**
 * Standard font scaling action buttons (A- and A+) styled matching the app top bar.
 */
@Composable
fun FontSizeActionButtons(
    modifier: Modifier = Modifier,
    controller: FontScaleController? = LocalFontScaleController.current
) {
    if (controller == null) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        // Decrease Font (A-)
        IconButton(
            onClick = controller.onDecrease,
            enabled = controller.canDecrease,
            modifier = Modifier.size(36.dp)
        ) {
            Text(
                text = "A-",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (controller.canDecrease) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                }
            )
        }

        // Increase Font (A+)
        IconButton(
            onClick = controller.onIncrease,
            enabled = controller.canIncrease,
            modifier = Modifier.size(36.dp)
        ) {
            Text(
                text = "A+",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
                color = if (controller.canIncrease) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.38f)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeIslamicTopAppBar(
    onOpenFontMenu: () -> Unit = {},
    onOpenThemeModal: () -> Unit = {}
) {
    val isDark = isSystemInDarkTheme()
    val goldDivider = IslamicGold.copy(alpha = if (isDark) 0.35f else 0.45f)

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
                // Font Scale Controls (A- and A+)
                FontSizeActionButtons()

                // Bangla Fonts Picker
                IconButton(
                    onClick = onOpenFontMenu,
                    modifier = Modifier.size(40.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (isDark) 0.35f else 0.6f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FontDownload,
                                contentDescription = "বাংলা ফন্ট সেটিংস",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(2.dp))

                // Aurora Themes Picker
                IconButton(
                    onClick = onOpenThemeModal,
                    modifier = Modifier.size(40.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = if (isDark) 0.35f else 0.6f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Palette,
                                contentDescription = "থিম পরিবর্তন",
                                tint = IslamicGold,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))
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
