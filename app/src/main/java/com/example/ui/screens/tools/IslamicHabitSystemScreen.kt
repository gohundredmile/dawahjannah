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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

    val weeklyStats by habitViewModel.weeklyStats.collectAsState()
    val habitsList by habitViewModel.habitUiList.collectAsState()
    val selectedCategory by habitViewModel.selectedCategory.collectAsState()
    val searchQuery by habitViewModel.searchQuery.collectAsState()
    val selectedHabitForDetail by habitViewModel.selectedHabitForDetail.collectAsState()

    var showInfoDialog by remember { mutableStateOf(false) }

    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
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
                                    color = Color(0xFF059669).copy(alpha = 0.15f),
                                    border = BorderStroke(0.8.dp, Color(0xFF059669))
                                ) {
                                    Text(
                                        text = "সুন্নাহ ট্র্যাকার",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF059669)
                                        ),
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "প্রতিযোগিতা নয়, আত্মিক প্রশান্তিময় সুন্নাহ চর্চার মৃদু অভ্যাস",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
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

            // GENTLE WEEKLY JOURNEY HERO BANNER (This week's Sunnah journey)
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
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
                            // Section header
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
                                // 1. Practices completed
                                GentleStatTile(
                                    number = weeklyStats.completedPracticesCount.coerceAtLeast(0),
                                    labelBn = "practices completed\n(সম্পন্ন আমল)",
                                    iconEmoji = "🌟",
                                    color = Color(0xFF059669),
                                    modifier = Modifier.weight(1f)
                                )

                                // 2. Being developed
                                GentleStatTile(
                                    number = weeklyStats.developingPracticesCount.coerceAtLeast(0),
                                    labelBn = "practices being\ndeveloped (চর্চারত)",
                                    iconEmoji = "🌱",
                                    color = Color(0xFFD97706),
                                    modifier = Modifier.weight(1f)
                                )

                                // 3. To revisit
                                GentleStatTile(
                                    number = weeklyStats.revisitPracticesCount.coerceAtLeast(0),
                                    labelBn = "practices to revisit\n(পুনরায় শুরু)",
                                    iconEmoji = "🕊️",
                                    color = Color(0xFF0284C7),
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Gentle Prophet's ﷺ Hadith on Consistency
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

            // Search Bar
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
                                text = "সুন্নাহ খুঁজুন (যেমন: মেসওয়াক, ডান হাত, সালাম, ঘুমানো...)",
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
                            focusedBorderColor = Color(0xFF059669),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Category Chips Row
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
                                selectedContainerColor = Color(0xFF059669).copy(alpha = 0.2f),
                                selectedLabelColor = Color(0xFF059669),
                                selectedLeadingIconColor = Color(0xFF059669)
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                selectedBorderColor = Color(0xFF059669),
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
                        text = "দৈনন্দিন সুন্নাহ ও অভ্যাসমালা (${habitsList.size})",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "কার্ডে ট্যাপ করে ফযিলত ও আদব জানুন",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }

            // Habits List
            items(habitsList, key = { it.habit.id }) { uiModel ->
                SunnahHabitCard(
                    uiModel = uiModel,
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
        }

        // Habit Detail Bottom Sheet
        if (selectedHabitForDetail != null) {
            val detailHabit = selectedHabitForDetail!!
            val currentUiModel = habitsList.find { it.habit.id == detailHabit.id }
            val isCompleted = currentUiModel?.isCompletedToday == true
            var userNote by remember(detailHabit.id) { mutableStateOf(currentUiModel?.note ?: "") }

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
                    // Title and Badges
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
                                color = Color(0xFF059669).copy(alpha = 0.15f),
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
                                Text(
                                    text = detailHabit.titleEn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(onClick = { habitViewModel.openHabitDetail(null) }) {
                            Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Fiqh Status & Frequency Badges
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF059669).copy(alpha = 0.15f),
                            border = BorderStroke(0.6.dp, Color(0xFF059669))
                        ) {
                            Text(
                                text = "শরঈ মর্যাদা: ${detailHabit.fiqhStatusBn}",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF059669),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant)
                        ) {
                            Text(
                                text = "সময়: ${detailHabit.recommendedFrequencyBn}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pristine Arabic Box
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = detailHabit.arabicText,
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
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "অর্থ: ${detailHabit.translationBn}",
                                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp, lineHeight = 21.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "রেফারেন্স: ${detailHabit.sourceReference}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = IslamicGold,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Scholarly Context & Fiqh Nuance
                    Text(
                        text = "প্রামাণ্য হাদিস ও ফিকহি বিবরণ:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = detailHabit.scholarlyContextBn,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp, lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Gentle Reflection
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0284C7).copy(alpha = 0.08f),
                        border = BorderStroke(0.8.dp, Color(0xFF0284C7).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("🌱", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "আত্মিক প্রশান্তি ও উপলব্ধি:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF0284C7),
                                    fontFamily = banglaFont
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = detailHabit.gentleReflectionBn,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, lineHeight = 19.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Practical Step
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF059669).copy(alpha = 0.08f),
                        border = BorderStroke(0.8.dp, Color(0xFF059669).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("💡", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "সহজ প্রয়োগ পদ্ধতি:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF059669),
                                    fontFamily = banglaFont
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = detailHabit.practicalStepBn,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp, lineHeight = 19.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Personal Reflection / Niyyah Note Field
                    OutlinedTextField(
                        value = userNote,
                        onValueChange = {
                            userNote = it
                            habitViewModel.updateHabitNote(detailHabit.id, it)
                        },
                        label = {
                            Text("ব্যক্তিগত নিয়ত বা অনুভূতি নোট (ঐচ্ছিক)", fontFamily = banglaFont, fontSize = 12.sp)
                        },
                        placeholder = {
                            Text("যেমন: 'আজ মাগরিবের পর থেকে মেসওয়াক শুরু করার নিয়ত করেছি'", fontFamily = banglaFont, fontSize = 12.sp)
                        },
                        leadingIcon = {
                            Icon(Icons.Default.EditNote, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF059669)
                        )
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Action Buttons Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Share Button
                        OutlinedButton(
                            onClick = {
                                val shareText = buildString {
                                    appendLine("✨ ${detailHabit.titleBn} (${detailHabit.titleEn})")
                                    appendLine()
                                    appendLine(detailHabit.arabicText)
                                    appendLine()
                                    appendLine("অর্থ: ${detailHabit.translationBn}")
                                    appendLine("রেফারেন্স: ${detailHabit.sourceReference}")
                                    appendLine()
                                    appendLine("সহজ আমল: ${detailHabit.practicalStepBn}")
                                    appendLine("\n— Dawah to Jannah (Islamic Habit System)")
                                }
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(sendIntent, "সুন্নাতটি শেয়ার করুন"))
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("শেয়ার", fontFamily = banglaFont)
                        }

                        // Complete / Toggle Button
                        Button(
                            onClick = {
                                habitViewModel.toggleHabitToday(
                                    habitId = detailHabit.id,
                                    currentCompleted = isCompleted,
                                    note = userNote
                                )
                                Toast.makeText(
                                    context,
                                    if (!isCompleted) "আলহামদুলিল্লাহ! সুন্নাতটি আজকের জন্য সম্পন্ন হয়েছে" else "সুন্নাতটির স্ট্যাটাস আপডেট হয়েছে",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.weight(1.5f)
                        ) {
                            Icon(
                                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCompleted) "আজ সম্পন্ন হয়েছে" else "আজকের জন্য সম্পন্ন করুন",
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }

        // Philosophy Info Dialog
        if (showInfoDialog) {
            AlertDialog(
                onDismissRequest = { showInfoDialog = false },
                icon = { Text("🌿", fontSize = 28.sp) },
                title = {
                    Text(
                        text = "ইসলামিক হ্যাবিট সিস্টেমের দর্শন",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(
                            text = "১. ধর্মের অতিরিক্ত গ্যামিফিকেশন বর্জন:\n" +
                                    "ইসলামে ইবাদত কোনো প্রতিযোগিতা বা সোশ্যাল মিডিয়া লিডারবোর্ড নয়। এখানে কাউকে পেছনে ফেলার চাপ নেই।\n\n" +
                            "২. ধারাবাহিকতার মাহাত্ম্য:\n" +
                            "রাসূলুল্লাহ ﷺ বলেছেন: 'আল্লাহর নিকট সর্বাধিক প্রিয় আমল তা-ই, যা নিয়মিত করা হয়—যদিও তা পরিমাণে অল্প হয়।' (বুখারী ৬৪৬৫)।\n\n" +
                            "৩. শূন্য অপরাধবোধ, পূর্ণ রহমত:\n" +
                            "কোনো দিন আমল ছুটে গেলে মন খারাপ না করে পরবর্তী সময়ে যেকোনো মুহূর্তে কোমল মনে পুনরায় শুরু করুন। প্রতিটি সুন্নাহ প্রিয় নবীজির ﷺ ভালোবাসার এক একটি মুক্তা।\n\n" +
                            "৪. প্রামাণ্য রেফারেন্স ও ফিকহ:\n" +
                            "অ্যাপে উল্লেখিত প্রতিটি সুন্নাহর বিশুদ্ধ উৎস (বুখারী, মুসলিম, আবু দাউদ, তিরমিযী) যাচাইকৃত।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
                            fontFamily = banglaFont
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
    }
}

@Composable
fun GentleStatTile(
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
        border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(iconEmoji, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "$number",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
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
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun SunnahHabitCard(
    uiModel: SunnahHabitUiModel,
    onToggle: () -> Unit,
    onOpenDetail: () -> Unit,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val habit = uiModel.habit

    val borderColor by animateColorAsState(
        targetValue = if (uiModel.isCompletedToday) Color(0xFF059669) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
        animationSpec = tween(300)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onOpenDetail() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (uiModel.isCompletedToday) {
                Color(0xFF059669).copy(alpha = 0.05f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = BorderStroke(if (uiModel.isCompletedToday) 1.5.dp else 0.8.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Top row: Emoji, Title, Category Badge, and Toggle Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (uiModel.isCompletedToday) Color(0xFF059669).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(habit.iconEmoji, fontSize = 20.sp)
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.titleBn,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = habit.titleEn,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Gentle 1-touch Checkmark Button
                IconButton(
                    onClick = onToggle,
                    modifier = Modifier.size(42.dp)
                ) {
                    if (uiModel.isCompletedToday) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF059669),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "সম্পন্ন হয়েছে",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.RadioButtonUnchecked,
                            contentDescription = "আজকের জন্য সম্পন্ন করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Short Arabic Preview
            Text(
                text = habit.arabicText.take(65) + if (habit.arabicText.length > 65) "..." else "",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Right
                ),
                fontFamily = arabicFont,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Meaning Preview
            Text(
                text = habit.translationBn,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Footer row: Source Reference & Journey Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                ) {
                    Text(
                        text = habit.sourceReference,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp, fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                // Journey Status Pill
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(uiModel.journeyStatus.badgeColorHex).copy(alpha = 0.12f),
                    border = BorderStroke(0.6.dp, Color(uiModel.journeyStatus.badgeColorHex).copy(alpha = 0.6f))
                ) {
                    Text(
                        text = uiModel.journeyStatus.labelBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(uiModel.journeyStatus.badgeColorHex)
                        ),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }
    }
}
