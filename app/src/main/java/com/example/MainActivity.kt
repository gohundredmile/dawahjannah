package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.data.model.FontSizeScale
import com.example.ui.components.AuroraThemesModal
import com.example.ui.components.BanglaFontSettingsDialog
import com.example.ui.components.DawahBottomNavigationBar
import com.example.ui.components.DawahTopAppBar
import com.example.ui.components.FontScaleController
import com.example.ui.components.HomeIslamicTopAppBar
import com.example.ui.components.LiveAuroraWallpaperBackground
import com.example.ui.components.LocalFontScaleController
import com.example.ui.components.LocalScreenEffectMode
import com.example.ui.screens.FavoriteScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MasnunDuaScreen
import com.example.ui.screens.MoreScreen
import com.example.ui.screens.RoutineScreen
import com.example.ui.screens.sub.AsmaulHusnaScreen
import com.example.ui.screens.sub.AyatDetectorAndSolverScreen
import com.example.ui.screens.sub.QiblaCompassScreen
import com.example.ui.screens.sub.TasbihScreen
import com.example.ui.screens.tools.AskBeforeYouActScreen
import com.example.ui.screens.tools.DuaBySituationScreen
import com.example.ui.screens.tools.ExplainAyahCameraScreen
import com.example.ui.screens.tools.IslamicHabitSystemScreen
import com.example.ui.screens.tools.MosqueModeScreen
import com.example.ui.screens.tools.PersonalDuaBuilderScreen
import com.example.ui.screens.tools.RamadanIntelligenceScreen
import com.example.ui.screens.tools.SmartQuranSearchScreen
import com.example.ui.screens.tools.ToolsScreen
import com.example.ui.theme.DawahTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen
import com.example.ui.viewmodel.ToolsSubScreen

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        com.example.util.WallpaperManager.init(this)
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
            val themeModalInitialTab by viewModel.themeModalInitialTab.collectAsState()
            val screenEffectMode by viewModel.screenEffectMode.collectAsState()
            val auroraConfig by viewModel.auroraConfig.collectAsState()
            val currentTab by viewModel.currentTab.collectAsState()
            val currentMoreSub by viewModel.moreSubScreen.collectAsState()
            val currentToolsSub by viewModel.toolsSubScreen.collectAsState()

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
                LocalFontScaleController provides fontScaleController,
                LocalScreenEffectMode provides screenEffectMode
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
                                    onOpenThemeModal = { viewModel.openThemeModal() },
                                    onOpenAppSettings = { viewModel.openSettings(AppTab.HOME) }
                                )
                            } else if (currentTab == AppTab.TOOLS && currentToolsSub == ToolsSubScreen.EXPLAIN_AYAH_CAMERA) {
                                // ExplainAyahCameraScreen has its own full-screen immersive viewfinder & top controls
                            } else if (!(currentTab == AppTab.MORE && currentMoreSub != MoreSubScreen.MAIN)) {
                                val showTopBarBack = currentTab == AppTab.TASBIH || currentTab == AppTab.DUA || (currentTab == AppTab.TOOLS && currentToolsSub != ToolsSubScreen.MAIN)
                                DawahTopAppBar(
                                    title = when (currentTab) {
                                        AppTab.DUA -> "মাসনুন দোয়া"
                                        AppTab.ROUTINE -> "২৪ ঘণ্টার সুন্নাত আমল"
                                        AppTab.TASBIH -> "ডিজিটাল তাসবিহ ও জিকির"
                                        AppTab.FAVORITE -> "ফেভারিট (বুকমার্ক)"
                                        AppTab.TOOLS -> if (currentToolsSub == ToolsSubScreen.MAIN) "ইসলামিক টুলস ও ল্যাব" else currentToolsSub.titleBn
                                        AppTab.MORE -> "ইসলামী জীবন"
                                        else -> "দা'ওয়াহ টু জান্নাহ্"
                                    },
                                    canNavigateBack = showTopBarBack,
                                    onNavigateBack = {
                                        if (currentTab == AppTab.TOOLS && currentToolsSub != ToolsSubScreen.MAIN) {
                                            viewModel.navigateBackToTools()
                                        } else {
                                            viewModel.selectTab(AppTab.HOME)
                                        }
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
                            if (!(currentTab == AppTab.TOOLS && currentToolsSub == ToolsSubScreen.EXPLAIN_AYAH_CAMERA)) {
                                DawahBottomNavigationBar(
                                    currentTab = currentTab,
                                    onTabSelected = { tab ->
                                        viewModel.selectTab(tab)
                                        if (tab == AppTab.TOOLS) {
                                            viewModel.navigateBackToTools()
                                        } else if (tab == AppTab.MORE) {
                                            viewModel.navigateBackToMore()
                                        }
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        val canGoBackToHome = currentTab == AppTab.TASBIH || currentTab == AppTab.DUA || currentTab == AppTab.FAVORITE || (currentTab == AppTab.TOOLS && currentToolsSub == ToolsSubScreen.MAIN)
                        BackHandler(enabled = canGoBackToHome) {
                            viewModel.selectTab(AppTab.HOME)
                        }
                        val canGoBackToTools = currentTab == AppTab.TOOLS && currentToolsSub != ToolsSubScreen.MAIN
                        BackHandler(enabled = canGoBackToTools) {
                            viewModel.navigateBackToTools()
                        }
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
                                AppTab.FAVORITE -> FavoriteScreen(
                                    viewModel = viewModel,
                                    contentPadding = innerPadding
                                )
                                AppTab.TOOLS -> {
                                    when (currentToolsSub) {
                                        ToolsSubScreen.MAIN -> ToolsScreen(
                                            onOpenDuaBySituation = { viewModel.openDuaBySituation() },
                                            onOpenPersonalDuaBuilder = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.PERSONAL_DUA_BUILDER) },
                                            onOpenExplainAyahCamera = { viewModel.openExplainAyahCamera() },
                                            onOpenSmartQuranSearch = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.SMART_QURAN_SEARCH) },
                                            onOpenIslamicHabitSystem = { viewModel.openIslamicHabitSystem() },
                                            onOpenRamadanIntelligence = { viewModel.openRamadanIntelligence() },
                                            onOpenAskBeforeYouAct = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.ASK_BEFORE_YOU_ACT) },
                                            onOpenAyatDetector = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.AYAT_DETECTOR_SOLVER) },
                                            onOpenQibla = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.QIBLA) },
                                            onOpenTasbih = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.TASBIH) },
                                            onOpenNamesOfAllah = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.NAMES_OF_ALLAH) },
                                            onOpenMosqueMode = { viewModel.openMosqueMode() },
                                            contentPadding = innerPadding
                                        )
                                        ToolsSubScreen.MOSQUE_MODE -> MosqueModeScreen(
                                            viewModel = viewModel,
                                            onNavigateBack = { viewModel.navigateBackToTools() },
                                            onOpenQibla = { viewModel.navigateToToolsSubScreen(ToolsSubScreen.QIBLA) }
                                        )
                                        ToolsSubScreen.DUA_BY_SITUATION -> DuaBySituationScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.PERSONAL_DUA_BUILDER -> PersonalDuaBuilderScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.ASK_BEFORE_YOU_ACT -> AskBeforeYouActScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.EXPLAIN_AYAH_CAMERA -> ExplainAyahCameraScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.SMART_QURAN_SEARCH -> SmartQuranSearchScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.ISLAMIC_HABIT_SYSTEM -> IslamicHabitSystemScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.RAMADAN_INTELLIGENCE -> RamadanIntelligenceScreen(
                                            onNavigateBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.AYAT_DETECTOR_SOLVER -> AyatDetectorAndSolverScreen(
                                            viewModel = viewModel
                                        )
                                        ToolsSubScreen.QIBLA -> QiblaCompassScreen(
                                            viewModel = viewModel,
                                            onBack = { viewModel.navigateBackToTools() }
                                        )
                                        ToolsSubScreen.TASBIH -> TasbihScreen(
                                            viewModel = viewModel,
                                            contentPadding = innerPadding
                                        )
                                        ToolsSubScreen.NAMES_OF_ALLAH -> AsmaulHusnaScreen(
                                            viewModel = viewModel
                                        )
                                    }
                                }
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

                        // Aurora Themes Modal (All 15 Themes + Live Aurora Waves + Window/Screen Effect Mode)
                        if (isThemeModalOpen) {
                            AuroraThemesModal(
                                currentTheme = themeStyle,
                                onSelectTheme = { theme -> viewModel.setThemeStyle(theme) },
                                onSelectRandom = { viewModel.selectRandomTheme() },
                                auroraConfig = auroraConfig,
                                onUpdateAuroraConfig = { newCfg -> viewModel.updateAuroraConfig(newCfg) },
                                initialTab = themeModalInitialTab,
                                currentScreenEffectMode = screenEffectMode,
                                onSelectScreenEffectMode = { mode -> viewModel.setScreenEffectMode(mode) },
                                onDismiss = { viewModel.closeThemeModal() }
                            )
                        }
                    }
                    }
                }
            }
        }
    }
}
