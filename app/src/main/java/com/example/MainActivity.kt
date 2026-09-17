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
import com.example.data.model.FontSizeScale
import com.example.ui.components.AppOpeningSplashScreen
import com.example.ui.components.AuroraThemesModal
import com.example.ui.components.BanglaFontSettingsDialog
import com.example.ui.components.DawahBottomNavigationBar
import com.example.ui.components.DawahTopAppBar
import com.example.ui.components.FontScaleController
import com.example.ui.components.HomeIslamicTopAppBar
import com.example.ui.components.LiveAuroraWallpaperBackground
import com.example.ui.components.LocalFontScaleController
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MasnunDuaScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.RoutineScreen
import com.example.ui.screens.sub.TasbihScreen
import com.example.ui.theme.DawahTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val themeStyle by viewModel.themeStyle.collectAsState()
            val themeMode by viewModel.themeMode.collectAsState()
            val fontScale by viewModel.fontScale.collectAsState()
            val englishFont by viewModel.englishFont.collectAsState()
            val banglaFont by viewModel.banglaFont.collectAsState()
            val banglaFontWeight by viewModel.banglaFontWeight.collectAsState()
            val primaryFontPreference by viewModel.primaryFontPreference.collectAsState()
            val flipClockFont by viewModel.flipClockFont.collectAsState()
            val isFontMenuOpen by viewModel.isFontMenuOpen.collectAsState()
            val isThemeModalOpen by viewModel.isThemeModalOpen.collectAsState()
            val auroraConfig by viewModel.auroraConfig.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val currentMoreSub by viewModel.moreSubScreen.collectAsState()
            var showWelcomeSplash by remember { mutableStateOf(true) }

            val baseDensity = LocalDensity.current
            val adjustedDensity = Density(
                density = baseDensity.density,
                fontScale = baseDensity.fontScale * fontScale.scale
            )

            val fontScaleEntries = FontSizeScale.entries
            val fontScaleIndex = fontScaleEntries.indexOf(fontScale).let { if (it == -1) 2 else it }
            val canDecreaseFont = fontScaleIndex > 0
            val canIncreaseFont = fontScaleIndex < fontScaleEntries.lastIndex

            val fontScaleController = remember(fontScale, fontScaleIndex, canDecreaseFont, canIncreaseFont) {
                FontScaleController(
                    scale = fontScale.scale,
                    canDecrease = canDecreaseFont,
                    canIncrease = canIncreaseFont,
                    onDecrease = { viewModel.decreaseFontScale() },
                    onIncrease = { viewModel.increaseFontScale() },
                    onReset = { viewModel.resetFontScale() }
                )
            }

            CompositionLocalProvider(
                LocalDensity provides adjustedDensity,
                LocalFontScaleController provides fontScaleController
            ) {
                DawahTheme(
                    themeStyle = themeStyle,
                    themeMode = themeMode,
                    englishFont = englishFont,
                    banglaFont = banglaFont,
                    banglaWeight = banglaFontWeight,
                    primaryPreference = primaryFontPreference,
                    flipClockFont = flipClockFont
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Magnificent Lite Background with Live Aurora Wave Wallpaper throughout the app
                        LiveAuroraWallpaperBackground(
                            config = auroraConfig,
                            themeStyle = themeStyle,
                            isDark = themeStyle.isDark
                        )

                        Scaffold(
                            modifier = Modifier.fillMaxSize(),
                            containerColor = Color.Transparent,
                        topBar = {
                            if (currentTab == AppTab.HOME) {
                                HomeIslamicTopAppBar(
                                    onOpenFontMenu = { viewModel.openFontMenu() },
                                    onOpenThemeModal = { viewModel.openThemeModal() }
                                )
                            } else if (!(currentTab == AppTab.MORE && currentMoreSub != MoreSubScreen.MAIN)) {
                                DawahTopAppBar(
                                    title = when (currentTab) {
                                        AppTab.DUA -> "মাসনুন দোয়া"
                                        AppTab.ROUTINE -> "২৪ ঘণ্টার সুন্নাত আমল"
                                        AppTab.TASBIH -> "ডিজিটাল তাসবিহ ও জিকির"
                                        AppTab.MORE -> "ইসলামী জীবন"
                                        else -> "দা'ওয়াহ টু জান্নাহ্"
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
                            modifier = Modifier.fillMaxSize()
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
                                AppTab.TASBIH -> TasbihScreen(
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

                        // Aurora Themes Modal (All 15 Themes + Live Aurora Waves)
                        if (isThemeModalOpen) {
                            AuroraThemesModal(
                                currentTheme = themeStyle,
                                onSelectTheme = { theme -> viewModel.setThemeStyle(theme) },
                                onSelectRandom = { viewModel.selectRandomTheme() },
                                auroraConfig = auroraConfig,
                                onUpdateAuroraConfig = { newCfg -> viewModel.updateAuroraConfig(newCfg) },
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
