package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.ui.components.AppOpeningSplashScreen
import com.example.ui.components.AuroraThemesModal
import com.example.ui.components.BanglaFontSettingsDialog
import com.example.ui.components.DawahBottomNavigationBar
import com.example.ui.components.DawahTopAppBar
import com.example.ui.components.LiveAuroraWallpaperBackground
import com.example.ui.screens.ChecklistScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MasnunDuaScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.RoutineScreen
import com.example.ui.theme.DawahTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setBackgroundDrawableResource(R.drawable.img_splash_cover)

        setContent {
            val themeStyle by viewModel.themeStyle.collectAsState()
            val themeMode by viewModel.themeMode.collectAsState()
            val fontScale by viewModel.fontScale.collectAsState()
            val englishFont by viewModel.englishFont.collectAsState()
            val banglaFont by viewModel.banglaFont.collectAsState()
            val banglaFontWeight by viewModel.banglaFontWeight.collectAsState()
            val primaryFontPreference by viewModel.primaryFontPreference.collectAsState()
            val isFontMenuOpen by viewModel.isFontMenuOpen.collectAsState()
            val isThemeModalOpen by viewModel.isThemeModalOpen.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val currentMoreSub by viewModel.moreSubScreen.collectAsState()
            var showWelcomeSplash by remember { mutableStateOf(true) }

            val baseDensity = LocalDensity.current
            val adjustedDensity = Density(
                density = baseDensity.density,
                fontScale = baseDensity.fontScale * fontScale.scale
            )

            CompositionLocalProvider(LocalDensity provides adjustedDensity) {
                DawahTheme(
                    themeStyle = themeStyle,
                    themeMode = themeMode,
                    englishFont = englishFont,
                    banglaFont = banglaFont,
                    banglaWeight = banglaFontWeight,
                    primaryPreference = primaryFontPreference
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Magnificent Lite Background with Live Aurora Wallpaper throughout the app
                        LiveAuroraWallpaperBackground()

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = Color.Transparent,
                        topBar = {
                            // Only show top bar for tabs when not handled inside sub-screens
                            if (currentTab != AppTab.HOME && !(currentTab == AppTab.MORE && currentMoreSub != MoreSubScreen.MAIN)) {
                                DawahTopAppBar(
                                    title = when (currentTab) {
                                        AppTab.DUA -> "মাসনুন দোয়া ভল্ট"
                                        AppTab.ROUTINE -> "২৪ ঘণ্টার সুন্নাত আমল"
                                        AppTab.CHECKLIST -> "দৈনিক আমল চেকলিস্ট"
                                        AppTab.MORE -> "ইসলামী জীবন"
                                        else -> "দাওয়াহ টু জান্নাহ"
                                    },
                                    actions = {
                                        IconButton(onClick = { viewModel.openFontMenu() }) {
                                            Icon(
                                                imageVector = Icons.Default.FontDownload,
                                                contentDescription = "Bengali Fonts",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                )
                            }
                        },
                        bottomBar = {
                            DawahBottomNavigationBar(
                                currentTab = currentTab,
                                onTabSelected = { tab ->
                                    viewModel.selectTab(tab)
                                    if (tab == AppTab.MORE) {
                                        viewModel.navigateBackToMore()
                                    }
                                }
                            )
                        }
                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .then(
                                    if (currentTab == AppTab.HOME) {
                                        Modifier.windowInsetsPadding(WindowInsets.statusBars)
                                    } else {
                                        Modifier
                                    }
                                )
                        ) {
                            when (currentTab) {
                                AppTab.HOME -> HomeScreen(
                                    viewModel = viewModel,
                                    contentPadding = innerPadding
                                )
                                AppTab.DUA -> MasnunDuaScreen(
                                    viewModel = viewModel,
                                    contentPadding = innerPadding
                                )
                                AppTab.ROUTINE -> RoutineScreen(
                                    viewModel = viewModel,
                                    contentPadding = innerPadding
                                )
                                AppTab.CHECKLIST -> ChecklistScreen(
                                    viewModel = viewModel,
                                    contentPadding = innerPadding
                                )
                                AppTab.MORE -> MoreScreen(
                                    viewModel = viewModel,
                                    contentPadding = innerPadding
                                )
                            }
                        }

                        // Bangla Font Settings Popover Modal (from Web App)
                        if (isFontMenuOpen) {
                            BanglaFontSettingsDialog(
                                viewModel = viewModel,
                                onDismissRequest = { viewModel.closeFontMenu() }
                            )
                        }

                        // Aurora Themes Modal (All 15 Themes from Web App)
                        if (isThemeModalOpen) {
                            AuroraThemesModal(
                                currentTheme = themeStyle,
                                onSelectTheme = { theme -> viewModel.setThemeStyle(theme) },
                                onSelectRandom = { viewModel.selectRandomTheme() },
                                onDismiss = { viewModel.closeThemeModal() }
                            )
                        }
                    }

                    // Full-Screen Immersive Welcome / Splash Screen Overlay
                    // Placed outside Scaffold so TopBar & BottomBar are completely hidden
                    AnimatedVisibility(
                        visible = showWelcomeSplash,
                        enter = EnterTransition.None,
                        exit = fadeOut(animationSpec = tween(500)),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppOpeningSplashScreen(
                            onFinish = { showWelcomeSplash = false }
                        )
                    }
                    }
                }
            }
        }
    }
}
