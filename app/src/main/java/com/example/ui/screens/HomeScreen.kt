package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
    val announcement by viewModel.activeAnnouncement.collectAsState()

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

        // 1.1 In-App Active Content & Release Announcement Banner
        if (announcement != null && announcement?.active == true) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Campaign,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = announcement?.title ?: "হালনাগাদ",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            IconButton(
                                onClick = { viewModel.dismissAnnouncement() },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "বন্ধ করুন",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        if (!announcement?.message.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = announcement!!.message,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = {
                                    viewModel.selectTab(AppTab.MORE)
                                    viewModel.openIslamicLifeSection(com.example.data.datasource.DuaAcceptanceTimesData.section)
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                            ) {
                                Text(
                                    "ইসলামী জীবন দেখুন →",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
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
