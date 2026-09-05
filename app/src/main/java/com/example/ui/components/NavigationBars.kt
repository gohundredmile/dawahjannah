package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.AppTab

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DawahTopAppBar(
    title: String,
    canNavigateBack: Boolean = false,
    onNavigateBack: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
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
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)),
        tonalElevation = 2.dp
    ) {
        NavigationBar(
            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            val navItemColors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primary,
                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )

            NavigationBarItem(
                selected = currentTab == AppTab.HOME,
                onClick = { onTabSelected(AppTab.HOME) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.HOME) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home"
                    )
                },
                label = {
                    Text(
                        text = AppTab.HOME.titleBn,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentTab == AppTab.HOME) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.DUA,
                onClick = { onTabSelected(AppTab.DUA) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.DUA) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                        contentDescription = "Masnun Dua"
                    )
                },
                label = {
                    Text(
                        text = AppTab.DUA.titleBn,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentTab == AppTab.DUA) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.ROUTINE,
                onClick = { onTabSelected(AppTab.ROUTINE) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.ROUTINE) Icons.Filled.AccessTime else Icons.Outlined.AccessTime,
                        contentDescription = "24h Routine"
                    )
                },
                label = {
                    Text(
                        text = AppTab.ROUTINE.titleBn,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentTab == AppTab.ROUTINE) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.CHECKLIST,
                onClick = { onTabSelected(AppTab.CHECKLIST) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.CHECKLIST) Icons.Filled.CheckCircle else Icons.Outlined.CheckCircleOutline,
                        contentDescription = "Checklist"
                    )
                },
                label = {
                    Text(
                        text = AppTab.CHECKLIST.titleBn,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentTab == AppTab.CHECKLIST) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = navItemColors
            )

            NavigationBarItem(
                selected = currentTab == AppTab.MORE,
                onClick = { onTabSelected(AppTab.MORE) },
                icon = {
                    Icon(
                        imageVector = if (currentTab == AppTab.MORE) Icons.Filled.MoreHoriz else Icons.Outlined.MoreHoriz,
                        contentDescription = "More"
                    )
                },
                label = {
                    Text(
                        text = AppTab.MORE.titleBn,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (currentTab == AppTab.MORE) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = navItemColors
            )
        }
    }
}
