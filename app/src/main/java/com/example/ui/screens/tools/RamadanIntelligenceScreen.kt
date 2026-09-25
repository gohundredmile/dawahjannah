package com.example.ui.screens.tools

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.RamadanLoopTab
import com.example.ui.components.DetailedSehriIftarDialog
import com.example.ui.components.RamadanMoonScheduleDialog
import com.example.ui.screens.tools.ramadan.RamadanAfterTab
import com.example.ui.screens.tools.ramadan.RamadanCharityAndLearnTab
import com.example.ui.screens.tools.ramadan.RamadanDashboardTab
import com.example.ui.screens.tools.ramadan.RamadanDuaAndReflectionTab
import com.example.ui.screens.tools.ramadan.RamadanDuringTab
import com.example.ui.screens.tools.ramadan.RamadanPreparationTab
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamadanIntelligenceScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RamadanIntelligenceViewModel = viewModel()
) {
    val banglaFont = LocalBanglaFontFamily.current

    val currentLoop by viewModel.currentLoop.collectAsState()
    val salatConfig by viewModel.salatConfig.collectAsState()

    // Dialog state for "রমাদান সময়সূচী" & "সেহেরি ও ইফতারের বিস্তারিত সময়সূচী"
    var showMoonSchedule by remember { mutableStateOf(false) }
    var showDetailedSehriIftar by remember { mutableStateOf(false) }

    BackHandler(enabled = showMoonSchedule || showDetailedSehriIftar) {
        if (showMoonSchedule) showMoonSchedule = false
        if (showDetailedSehriIftar) showDetailedSehriIftar = false
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR WITH FLAGSHIP TITLE & SUBTITLE
        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "All in one Ramadan & Ramadan Intelligence",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "Your Complete Ramadan Companion — Before, During & After Ramadan",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // The Core Ramadan Loop Main Tab Bar
                ScrollableTabRow(
                    selectedTabIndex = currentLoop.ordinal,
                    edgePadding = 0.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                    divider = {}
                ) {
                    RamadanLoopTab.entries.forEach { tab ->
                        val isSelected = currentLoop == tab
                        Tab(
                            selected = isSelected,
                            onClick = { viewModel.setLoop(tab) },
                            text = {
                                Text(
                                    text = "${tab.iconEmoji} ${tab.titleBn}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp,
                                    fontFamily = banglaFont,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }
                }
            }
        }

        // TAB CONTENT
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            item {
                when (currentLoop) {
                    RamadanLoopTab.DASHBOARD -> RamadanDashboardTab(
                        viewModel = viewModel,
                        onOpenRamadanSchedule = { showMoonSchedule = true },
                        onOpenDetailedSehriIftar = { showDetailedSehriIftar = true },
                        onNavigateToLoop = { viewModel.setLoop(it) }
                    )
                    RamadanLoopTab.BEFORE_RAMADAN -> RamadanPreparationTab(
                        viewModel = viewModel
                    )
                    RamadanLoopTab.DURING_RAMADAN -> RamadanDuringTab(
                        viewModel = viewModel
                    )
                    RamadanLoopTab.DUA_AND_REFLECTION -> RamadanDuaAndReflectionTab(
                        viewModel = viewModel
                    )
                    RamadanLoopTab.LEARN_AND_VERIFY -> RamadanCharityAndLearnTab(
                        viewModel = viewModel
                    )
                    RamadanLoopTab.AFTER_RAMADAN -> RamadanAfterTab(
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    // MANDATORY LINKED DIALOG 1: রমাদান সময়সূচী
    if (showMoonSchedule) {
        RamadanMoonScheduleDialog(
            salatConfig = salatConfig,
            onDismiss = { showMoonSchedule = false }
        )
    }

    // MANDATORY LINKED DIALOG 2: সেহেরি ও ইফতারের বিস্তারিত সময়সূচী
    if (showDetailedSehriIftar) {
        DetailedSehriIftarDialog(
            salatConfig = salatConfig,
            onDismiss = { showDetailedSehriIftar = false }
        )
    }
}
