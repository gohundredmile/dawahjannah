package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.ui.components.DailyWisdomSection
import com.example.ui.components.DateTimeMasterCard
import com.example.ui.components.IslamicHeaderCover
import com.example.ui.components.QuickActionCard
import com.example.ui.components.SalatTimingsSection
import com.example.ui.components.SehriIftarFullScreenDialog
import com.example.ui.components.SehriIftarSummaryCard
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen
import com.example.util.CalendarHelper

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
    val tripleCalendar by viewModel.tripleCalendar.collectAsState()
    val prayerStatus by viewModel.prayerStatus.collectAsState()
    val wisdomState by viewModel.wisdomState.collectAsState()
    val streak by viewModel.dailyStreak.collectAsState()
    val todayRecord by viewModel.todayChecklistRecord.collectAsState()
    val salatConfig by viewModel.salatConfig.collectAsState()
    val gpsStatusMessage by viewModel.gpsStatusMessage.collectAsState()

    var showSehriIftarFullScreen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // 1. Welcoming Cover & Clock
        item {
            IslamicHeaderCover(
                salutation = prayerStatus.salutationBn,
                countdownFormatted = prayerStatus.timeRemainingFormatted,
                nextPrayerName = prayerStatus.nextPrayer?.nameBn ?: "ওয়াক্ত",
                onOpenSettings = {
                    viewModel.openSettings(AppTab.HOME)
                }
            )
        }

        // 2. Quick Action & Streak Highlights
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionCard(
                    title = "ধারাবাহিকতা",
                    value = "${CalendarHelper.toBanglaNumber(streak)} দিন স্ট্রিক",
                    icon = Icons.Default.LocalFireDepartment,
                    iconTint = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.selectTab(AppTab.CHECKLIST) }
                )

                val completedCount = todayRecord?.completedCount ?: 0
                val totalHabits = viewModel.habitsList.size
                QuickActionCard(
                    title = "আজকের আমল",
                    value = "${CalendarHelper.toBanglaNumber(completedCount)}/${CalendarHelper.toBanglaNumber(totalHabits)} সম্পন্ন",
                    icon = Icons.Default.CheckCircle,
                    iconTint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f),
                    onClick = { viewModel.selectTab(AppTab.CHECKLIST) }
                )

                QuickActionCard(
                    title = "তাসবীহ",
                    value = "জিকির করুন",
                    icon = Icons.Default.TouchApp,
                    iconTint = IslamicGold,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        viewModel.selectTab(AppTab.MORE)
                        viewModel.navigateToMoreSubScreen(MoreSubScreen.TASBIH)
                    }
                )
            }
        }

        // 3. Date & Time Master Card with Expandable Triple Calendars
        item {
            DateTimeMasterCard(calendarInfo = tripleCalendar)
        }

        // 4. সেহরি এবং ইফতারের সময়সূচী (Sehri & Ifter Timing)
        item {
            SehriIftarSummaryCard(
                prayerStatus = prayerStatus,
                salatConfig = salatConfig,
                onOpenFullScreen = { showSehriIftarFullScreen = true }
            )
        }

        // 5. Daily Prayer Times (Salat Timings & Forbidden Time)
        item {
            SalatTimingsSection(
                prayerStatus = prayerStatus,
                salatConfig = salatConfig,
                gpsStatusMessage = gpsStatusMessage,
                onTrackGps = { viewModel.trackCurrentLocationWithGps() },
                onSelectPlace = { place -> viewModel.updateSalatPlace(place) },
                onCustomPlace = { nameBn, nameEn, lat, lng -> viewModel.setCustomSalatLocation(nameBn, nameEn, lat, lng) },
                onSetManualOffset = { offset -> viewModel.setSalatManualOffset(offset) },
                onToggleHanafiAsr = { isHanafi -> viewModel.setHanafiAsr(isHanafi) },
                onClearGpsMessage = { viewModel.clearGpsMessage() }
            )
        }

        // 6. Daily Light & Inspiration: Holy Quran, Hadith, and Inspirational Quotes
        item {
            DailyWisdomSection(
                wisdomState = wisdomState,
                onShuffle = { viewModel.shuffleWisdom() }
            )
        }
    }

    // Full Screen Sawm & Ramadan Timing Expanded Dialog
    if (showSehriIftarFullScreen) {
        SehriIftarFullScreenDialog(
            prayerStatus = prayerStatus,
            salatConfig = salatConfig,
            onDismiss = { showSehriIftarFullScreen = false }
        )
    }
}
