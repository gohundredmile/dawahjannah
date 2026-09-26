package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.HabitCategory
import com.example.data.model.HabitDevelopmentStage
import com.example.data.model.HabitSystemMode
import com.example.data.model.SunnahHabitItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.HabitJourneyStatus
import com.example.ui.viewmodel.IslamicHabitViewModel
import com.example.ui.viewmodel.SunnahHabitUiModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicHabitSystemScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    habitViewModel: IslamicHabitViewModel = viewModel()
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val systemMode by habitViewModel.systemMode.collectAsState()
    val weeklyStats by habitViewModel.weeklyStats.collectAsState()
    val habitsList by habitViewModel.habitUiList.collectAsState()
    val selectedCategory by habitViewModel.selectedCategory.collectAsState()
    val searchQuery by habitViewModel.searchQuery.collectAsState()
    val stageFilter by habitViewModel.stageFilter.collectAsState()
    val selectedHabitForDetail by habitViewModel.selectedHabitForDetail.collectAsState()
    val focusPreferences by habitViewModel.focusPreferences.collectAsState()

    var showInfoDialog by remember { mutableStateOf(false) }
    var showGoalSettingsDialog by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 50.dp)
        ) {
            // Header Top Bar
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পিছনে যান",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Islamic Habit System",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF0F766E).copy(alpha = 0.15f),
                                    border = BorderStroke(0.8.dp, Color(0xFF0F766E))
                                ) {
                                    Text(
                                        text = "গাইড + ট্র্যাকার",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E)
                                        ),
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "প্রিয় নবীর ﷺ সুন্নাহর প্রামাণ্য গাইডলাইন ও আত্মিক ধারাবাহিকতা",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }

                        IconButton(onClick = { showGoalSettingsDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "লক্ষ্য নির্ধারণ",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(onClick = { showInfoDialog = true }) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "সুন্নাহ পদ্ধতির দর্শন",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // UNIFIED MODE SELECTOR TABS (Section 3: Guide Mode + Tracker Mode)
            item {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        // Guide Mode Tab
                        val isGuide = systemMode == HabitSystemMode.GUIDE
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { habitViewModel.setSystemMode(HabitSystemMode.GUIDE) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isGuide) Color(0xFF0F766E) else Color.Transparent,
                            shadowElevation = if (isGuide) 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "📖 সুন্নাহ গাইড (Guide)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isGuide) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isGuide) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        // Tracker Mode Tab
                        val isTracker = systemMode == HabitSystemMode.TRACKER
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { habitViewModel.setSystemMode(HabitSystemMode.TRACKER) },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isTracker) Color(0xFF059669) else Color.Transparent,
                            shadowElevation = if (isTracker) 2.dp else 0.dp
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🌿 আমল ট্র্যাকার (Tracker)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isTracker) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isTracker) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }

            // GUIDE MODE SPECIFIC HIGHLIGHTS: "One Sunnah to Explore" (Section 35) & "Gentle Reminder" (Section 36)
            if (systemMode == HabitSystemMode.GUIDE) {
                item {
                    val featured = habitViewModel.featuredHabit
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.7f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF0F766E).copy(alpha = 0.12f),
                                            IslamicGold.copy(alpha = 0.08f),
                                            MaterialTheme.colorScheme.surface
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = "🌿", fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "One Sunnah to Explore • আজকের বিশেষ সুন্নাত",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F766E)
                                            ),
                                            fontFamily = banglaFont
                                        )
                                    }
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Text(
                                            text = featured.timeOfDay,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontFamily = banglaFont
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = featured.iconEmoji,
                                        fontSize = 24.sp
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = featured.titleBn,
                                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )
                                        if (featured.arabicTitle.isNotBlank()) {
                                            Text(
                                                text = featured.arabicTitle,
                                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.primary),
                                                fontFamily = arabicFont
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = featured.shortDescriptionBn,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = "📜 দলিল:", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = featured.sourceReference,
                                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { habitViewModel.openHabitDetail(featured) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        border = BorderStroke(1.dp, Color(0xFF0F766E))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp),
                                            tint = Color(0xFF0F766E)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("শিখুন ও পড়ুন", fontSize = 12.sp, color = Color(0xFF0F766E), fontFamily = banglaFont)
                                    }

                                    Button(
                                        onClick = {
                                            habitViewModel.setSystemMode(HabitSystemMode.TRACKER)
                                            habitViewModel.openHabitDetail(featured)
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Spa,
                                            contentDescription = null,
                                            modifier = Modifier.size(15.dp),
                                            tint = Color.White
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("আমলে যোগ করুন", fontSize = 12.sp, color = Color.White, fontFamily = banglaFont)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TRACKER MODE SPECIFIC HERO: This Week's Sunnah Journey (Section 11 & 17)
            if (systemMode == HabitSystemMode.TRACKER) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF059669).copy(alpha = 0.12f),
                                            IslamicGold.copy(alpha = 0.08f),
                                            MaterialTheme.colorScheme.surface
                                        )
                                    )
                                )
                                .padding(18.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Spa,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "This week's Sunnah journey",
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                                    ) {
                                        Text(
                                            text = habitViewModel.getDisplayDateBengali(),
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontFamily = banglaFont,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                // Three Gentle Metrics (Completed, Developing, Revisit)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    GentleStatTile(
                                        number = weeklyStats.completedPracticesCount.coerceAtLeast(0),
                                        labelBn = "practices completed\n(সম্পন্ন আমল)",
                                        iconEmoji = "🌟",
                                        color = Color(0xFF059669),
                                        modifier = Modifier.weight(1f)
                                    )

                                    GentleStatTile(
                                        number = weeklyStats.developingPracticesCount.coerceAtLeast(0),
                                        labelBn = "practices being\ndeveloped (চর্চারত)",
                                        iconEmoji = "🌱",
                                        color = Color(0xFFD97706),
                                        modifier = Modifier.weight(1f)
                                    )

                                    GentleStatTile(
                                        number = weeklyStats.revisitPracticesCount.coerceAtLeast(0),
                                        labelBn = "practices to revisit\n(পুনরায় শুরু)",
                                        iconEmoji = "🕊️",
                                        color = Color(0xFF0284C7),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                // Sincerity & Gentle Reminder Banner
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.65f),
                                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "« ${weeklyStats.inspiringHadithArabic} »",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.Center
                                            ),
                                            fontFamily = arabicFont,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "“${weeklyStats.inspiringHadithBn}”",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.5.sp,
                                                lineHeight = 17.sp,
                                                textAlign = TextAlign.Center
                                            ),
                                            fontFamily = banglaFont,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Stage filter chips (in Tracker mode)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = stageFilter == null,
                            onClick = { habitViewModel.setStageFilter(null) },
                            label = { Text("সকল আমল", fontFamily = banglaFont, fontSize = 12.sp) }
                        )
                        HabitDevelopmentStage.values().forEach { stage ->
                            val isSelected = stageFilter == stage
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    habitViewModel.setStageFilter(if (isSelected) null else stage)
                                },
                                label = {
                                    Text(stage.labelBn, fontFamily = banglaFont, fontSize = 12.sp)
                                }
                            )
                        }
                    }
                }
            }

            // Universal Search Bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { habitViewModel.setSearchQuery(it) },
                        placeholder = {
                            Text(
                                text = "সুন্নাহ খুঁজুন (মেসওয়াক, ডান হাত, সালাম, ঘুমানো, তাহাজ্জুদ...)",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 13.sp,
                                fontFamily = banglaFont
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { habitViewModel.setSearchQuery("") }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "মুছে ফেলুন",
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = if (systemMode == HabitSystemMode.GUIDE) Color(0xFF0F766E) else Color(0xFF059669),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Category Chips Row (Section 4 Categories)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HabitCategory.values().forEach { category ->
                        val isSelected = selectedCategory == category
                        FilterChip(
                            selected = isSelected,
                            onClick = { habitViewModel.selectCategory(category) },
                            label = {
                                Text(
                                    text = "${category.iconEmoji} ${category.titleBn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = banglaFont
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = if (systemMode == HabitSystemMode.GUIDE) Color(0xFF0F766E).copy(alpha = 0.2f) else Color(0xFF059669).copy(alpha = 0.2f),
                                selectedLabelColor = if (systemMode == HabitSystemMode.GUIDE) Color(0xFF0F766E) else Color(0xFF059669),
                                selectedLeadingIconColor = if (systemMode == HabitSystemMode.GUIDE) Color(0xFF0F766E) else Color(0xFF059669)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                selectedBorderColor = if (systemMode == HabitSystemMode.GUIDE) Color(0xFF0F766E) else Color(0xFF059669),
                                borderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }
            }

            // Section title with count
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (systemMode == HabitSystemMode.GUIDE) "সুন্নাহ নির্দেশিকা ও আদব (${habitsList.size})" else "দৈনন্দিন আমল চেকলিস্ট (${habitsList.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = if (systemMode == HabitSystemMode.GUIDE) "ট্যাপ করে দলিল ও ফযিলত জানুন" else "ট্যাপ করে আজ আমল সম্পন্ন করুন",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }

            // Habits List
            items(habitsList, key = { it.habit.id }) { uiModel ->
                UnifiedHabitCard(
                    uiModel = uiModel,
                    mode = systemMode,
                    onToggle = {
                        habitViewModel.toggleHabitToday(
                            habitId = uiModel.habit.id,
                            currentCompleted = uiModel.isCompletedToday
                        )
                    },
                    onOpenDetail = {
                        habitViewModel.openHabitDetail(uiModel.habit)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            // Weekly Reflection Prompts (at bottom in Tracker Mode - Section 17 & 48)
            if (systemMode == HabitSystemMode.TRACKER) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🌱", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "সাপ্তাহিক আত্ম-প্রতিফলন (Self-Reflection)",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "• কোন সুন্নাতটি পালন করা এই সপ্তাহে আপনার কাছে সবচেয়ে সহজ ও হৃদয়গ্রাহী মনে হয়েছে?\n• আগামী সপ্তাহে কোন একটি আমল আরও নিবিড়ভাবে ধরে রাখতে চান?\n• আল্লাহর ভালোবাসায় ছোট ছোট নিয়মিত আমলই আখিরাতের সেরা সম্বল।",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // UNIFIED HABIT DETAIL MODAL BOTTOM SHEET (Dual-tab: Guide vs Tracker)
        if (selectedHabitForDetail != null) {
            val detailHabit = selectedHabitForDetail!!
            val currentUiModel = habitsList.find { it.habit.id == detailHabit.id }
            val isCompleted = currentUiModel?.isCompletedToday == true
            var userNote by remember(detailHabit.id) { mutableStateOf(currentUiModel?.note ?: "") }
            var sheetTab by remember { mutableIntStateOf(if (systemMode == HabitSystemMode.GUIDE) 0 else 1) }

            ModalBottomSheet(
                onDismissRequest = { habitViewModel.openHabitDetail(null) },
                sheetState = bottomSheetState,
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    // Header: Icon, Title, and Classification Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            modifier = Modifier.weight(1f),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0F766E).copy(alpha = 0.15f),
                                modifier = Modifier.size(46.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(detailHabit.iconEmoji, fontSize = 24.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = detailHabit.titleBn,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                                if (detailHabit.arabicTitle.isNotBlank()) {
                                    Text(
                                        text = detailHabit.arabicTitle,
                                        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary),
                                        fontFamily = arabicFont
                                    )
                                }
                                Text(
                                    text = detailHabit.titleEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = { habitViewModel.openHabitDetail(null) }) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "বন্ধ করুন")
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Badges Row: Fiqh Classification, Evidence Type, Time of day
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0F766E).copy(alpha = 0.15f),
                            border = BorderStroke(0.8.dp, Color(0xFF0F766E))
                        ) {
                            Text(
                                text = detailHabit.legalClassification,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F766E)
                                ),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text(
                                text = "দলিল: ${detailHabit.evidenceType}",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IslamicGold.copy(alpha = 0.15f),
                            border = BorderStroke(0.8.dp, IslamicGold)
                        ) {
                            Text(
                                text = detailHabit.timeOfDay,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp, color = Color(0xFFB45309)),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // DUAL-TAB IN SHEET: 1. গাইড ও দলিল (Guide & Evidence), 2. ট্র্যাকিং ও জার্নি (Tracker & Journey)
                    TabRow(
                        selectedTabIndex = sheetTab,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[sheetTab]),
                                color = Color(0xFF0F766E)
                            )
                        }
                    ) {
                        Tab(
                            selected = sheetTab == 0,
                            onClick = { sheetTab = 0 },
                            text = {
                                Text(
                                    "📘 গাইড ও দলিল (Guide)",
                                    fontFamily = banglaFont,
                                    fontWeight = if (sheetTab == 0) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sheetTab == 0) Color(0xFF0F766E) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                        Tab(
                            selected = sheetTab == 1,
                            onClick = { sheetTab = 1 },
                            text = {
                                Text(
                                    "🌿 ট্র্যাকিং ও জার্নি (Tracker)",
                                    fontFamily = banglaFont,
                                    fontWeight = if (sheetTab == 1) FontWeight.Bold else FontWeight.Normal,
                                    color = if (sheetTab == 1) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (sheetTab == 0) {
                        // TAB 1: GUIDE & EVIDENCE
                        // 1. What is it & Why it matters
                        DetailSection(titleBn = "আমলটি কী ও কেন পালন করবেন?", icon = "🌟") {
                            Text(
                                text = detailHabit.whyItMattersBn.ifBlank { detailHabit.shortDescriptionBn },
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 2. How to perform & Etiquette
                        if (detailHabit.howToPerformBn.isNotBlank()) {
                            DetailSection(titleBn = "সঠিক পদ্ধতি ও আদব", icon = "📋") {
                                Text(
                                    text = detailHabit.howToPerformBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                        }

                        // 3. Authentic Evidence (Quran & Sahih Hadith)
                        DetailSection(titleBn = "প্রামাণ্য হাদিস ও দলিল", icon = "📜") {
                            Column {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = detailHabit.arabicEvidence.ifBlank { detailHabit.arabicText },
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = 19.sp,
                                                lineHeight = 32.sp,
                                                textAlign = TextAlign.Right
                                            ),
                                            fontFamily = arabicFont,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Spacer(modifier = Modifier.height(10.dp))
                                        HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outlineVariant)
                                        Spacer(modifier = Modifier.height(10.dp))

                                        Text(
                                            text = "“${detailHabit.translationBn}”",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontSize = 13.5.sp,
                                                lineHeight = 21.sp
                                            ),
                                            fontFamily = banglaFont,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "উৎস: ${detailHabit.sourceReference}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = Color(0xFF0F766E),
                                                fontFamily = banglaFont
                                            )
                                            Text(
                                                text = "মান: ${detailHabit.hadithGrade}",
                                                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                                fontFamily = banglaFont
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 4. Related Du'a if any (Section 23)
                        if (detailHabit.relatedDuaArabic.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            DetailSection(titleBn = "সংশ্লিষ্ট হিসনুল মুসলিম দো'আ ও জিকির", icon = "🤲") {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF0F766E).copy(alpha = 0.08f),
                                    border = BorderStroke(0.8.dp, Color(0xFF0F766E).copy(alpha = 0.3f))
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        if (detailHabit.relatedDuaTitleBn.isNotBlank()) {
                                            Text(
                                                text = detailHabit.relatedDuaTitleBn,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                                color = Color(0xFF0F766E),
                                                fontFamily = banglaFont
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                        }

                                        Text(
                                            text = detailHabit.relatedDuaArabic,
                                            style = MaterialTheme.typography.bodyLarge.copy(
                                                fontSize = 18.sp,
                                                lineHeight = 30.sp,
                                                textAlign = TextAlign.Right
                                            ),
                                            fontFamily = arabicFont,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        if (detailHabit.relatedDuaTransliteration.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "উচ্চারণ: ${detailHabit.relatedDuaTransliteration}",
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontFamily = banglaFont
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "অর্থ: ${detailHabit.relatedDuaTranslationBn}",
                                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, lineHeight = 18.sp),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )

                                        if (detailHabit.relatedDuaRepeatCount.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "পাঠের নিয়ম: ${detailHabit.relatedDuaRepeatCount}",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = IslamicGold),
                                                fontFamily = banglaFont
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // 5. Common Mistakes
                        if (detailHabit.commonMistakesBn.isNotBlank()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            DetailSection(titleBn = "বর্জনীয় ভুলত্রুটি ও সতর্কতা", icon = "⚠️") {
                                Text(
                                    text = detailHabit.commonMistakesBn,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Share Guidance Button
                        OutlinedButton(
                            onClick = {
                                val shareText = buildString {
                                    appendLine("【 প্রিয় নবীর ﷺ সুন্নাত: ${detailHabit.titleBn} 】")
                                    appendLine(detailHabit.shortDescriptionBn)
                                    appendLine("\nহাদিসের বাণী:")
                                    appendLine(detailHabit.arabicEvidence)
                                    appendLine("অনুবাদ: ${detailHabit.translationBn}")
                                    appendLine("উৎস: ${detailHabit.sourceReference}")
                                    appendLine("\nদা'ওয়াহ টু জান্নাহ - Islamic Habit System (Sunnah Guide & Tracker)")
                                }
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "সুন্নাহর বাণী শেয়ার করুন"))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("সুন্নাহ নির্দেশিকা শেয়ার করুন", fontFamily = banglaFont)
                        }
                    } else {
                        // TAB 2: TRACKING & JOURNEY
                        // Large 1-Tap Toggle
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    habitViewModel.toggleHabitToday(
                                        habitId = detailHabit.id,
                                        currentCompleted = isCompleted,
                                        note = userNote
                                    )
                                },
                            shape = RoundedCornerShape(16.dp),
                            color = if (isCompleted) Color(0xFF059669).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            border = BorderStroke(1.2.dp, if (isCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Row(
                                modifier = Modifier.padding(18.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = if (isCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.outlineVariant,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isCompleted) Icons.Default.Check else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = if (isCompleted) "আজ পালন করেছি • আলহামদুলিল্লাহ" else "আজ পালন করার জন্য ট্যাপ করুন",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = if (isCompleted) "ধারাবাহিকতা বজায় রাখতে আল্লাহ সাহায্য করুন" else "তাড়াহুড়ো নয়, মৃদুভাবে নিয়ত করে সম্পন্ন করুন",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Journey Development Status (Section 25)
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "ব্যক্তিগত আমল অভিযাত্রা (Development Status)",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val currentStage = currentUiModel?.developmentStage ?: HabitDevelopmentStage.DEVELOPING
                                    Text(
                                        text = "বর্তমান স্থিতি: ${currentStage.labelBn}",
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                        color = Color(currentStage.badgeColorHex),
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = "এই সপ্তাহে: ${currentUiModel?.completionsThisWeek ?: 0} দিন",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Personal Reflection Note (Section 3)
                        Text(
                            text = "ব্যক্তিগত প্রতিফলন বা অনুভূতি (ঐচ্ছিক)",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = userNote,
                            onValueChange = { userNote = it },
                            placeholder = {
                                Text(
                                    text = "যেমন: আজ জোহরের পূর্বে মেসওয়াক করেছি, মন খুব শান্ত ছিল...",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = banglaFont
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            minLines = 2,
                            maxLines = 4
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                habitViewModel.updateHabitNote(detailHabit.id, userNote)
                                Toast.makeText(context, "প্রতিফলন সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F766E))
                        ) {
                            Icon(imageVector = Icons.Default.EditNote, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("প্রতিফলন নোট সংরক্ষণ করুন", fontFamily = banglaFont)
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }

        // Philosophy & Sincerity Info Dialog (Section 2, 21, 22)
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌿", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "সুন্নাহ পদ্ধতির দর্শন ও নীতি",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "১. প্রতিযোগিতা নয়, ইখলাস ও ধারাবাহিকতা:\nইবাদত কোনো খেলা বা পয়েন্টের প্রতিযোগিতা নয়। তাই এখানে কোনো গ্লোবাল লিডারবোর্ড বা কৃত্রিম র‍্যাংকিং রাখা হয়নি।\n\n২. ট্র্যাকিং কোনো আধ্যাত্মিক মিটার নয়:\nএকটি নথিবদ্ধ আমল আন্তরিকতার চূড়ান্ত প্রমাণ নয়, আবার অ্যাপে রেকর্ড না করা মানেই কোনো ব্যক্তি সুন্নাহ ছাড়েননি। এটি কেবল আত্ম-শৃঙ্খলার মৃদু ডায়েরি।\n\n৩. মিস হওয়া মানেই পাপ বা লজ্জিত হওয়া নয়:\nকোনো দিন অভ্যাস মিস হলে লজ্জিত হওয়ার কিছু নেই। আল্লাহ অল্প কিন্তু নিয়মিত আমল সর্বাধিক ভালোবাসেন।\n\n৪. গাইড ও ট্র্যাকার এক সূত্রে গাঁথা:\nপ্রতিটি সুন্নাতের জন্য রয়েছে প্রামাণ্য সহীহ হাদিস ও কুরআন রেফারেন্স, আদব এবং সংশ্লিষ্ট দো'আ।",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showInfoDialog = false }) {
                        Text("বুঝেছি", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Focus Goals / Onboarding Dialog (Section 10 & 11)
        if (showGoalSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showGoalSettingsDialog = false },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎯", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "সুন্নাহ মনোযোগ ও লক্ষ্য নির্ধারণ",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = "একসাথে বহু আমলের বোঝা না নিয়ে প্রতিদিন ৩ থেকে ৫টি সুন্নাহতে গভীর মনোযোগ দেওয়া সর্বোত্তম। আপনি চাইলে যেকোনো ক্যাটাগরি বেছে নিতে পারেন।",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "ক্যাটাগরি বা বিষয় অনুযায়ী ফিল্টার করে আপনার সকাল, আহার ও রাতের রুটিন সাজিয়ে নিন।",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            fontFamily = banglaFont
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showGoalSettingsDialog = false }) {
                        Text("ঠিক আছে", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

/**
 * Unified Habit Card that adapts intelligently to Guide Mode or Tracker Mode.
 */
@Composable
private fun UnifiedHabitCard(
    uiModel: SunnahHabitUiModel,
    mode: HabitSystemMode,
    onToggle: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val habit = uiModel.habit
    val isCompleted = uiModel.isCompletedToday

    val containerBg by animateColorAsState(
        targetValue = if (isCompleted && mode == HabitSystemMode.TRACKER) {
            Color(0xFF059669).copy(alpha = 0.08f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        animationSpec = tween(300),
        label = "containerBg"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDetail() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerBg),
        border = BorderStroke(
            1.dp,
            if (isCompleted && mode == HabitSystemMode.TRACKER) Color(0xFF059669).copy(alpha = 0.6f)
            else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top Row: Emoji, Title, Badges & Toggle Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Emoji Surface
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(habit.iconEmoji, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Title and Categorical Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.titleBn,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF0F766E).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = habit.legalClassification,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F766E)
                                ),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = habit.timeOfDay,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Interactive 1-tap checkmark
                IconButton(
                    onClick = onToggle,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = "আমল সম্পন্ন করুন",
                        tint = if (isCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.outlineVariant,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Description
            Text(
                text = habit.shortDescriptionBn,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            // Evidence Snippet in Guide Mode
            if (mode == HabitSystemMode.GUIDE) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📜 ${habit.sourceReference}",
                            style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.primary),
                            fontFamily = banglaFont,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "বিস্তারিত দেখুন ›",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0F766E), fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                    }
                }
            } else {
                // In Tracker Mode: Show consistency status
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "এই সপ্তাহে: ${uiModel.completionsThisWeek} দিন চর্চা",
                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = uiModel.developmentStage.labelBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(uiModel.developmentStage.badgeColorHex)
                        ),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailSection(
    titleBn: String,
    icon: String,
    content: @Composable () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(icon, fontSize = 15.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = titleBn,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        content()
    }
}

@Composable
private fun GentleStatTile(
    number: Int,
    labelBn: String,
    iconEmoji: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.08f),
        border = BorderStroke(0.8.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(iconEmoji, fontSize = 13.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$number",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = labelBn,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.5.sp,
                    lineHeight = 13.sp,
                    textAlign = TextAlign.Center
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
    }
}
