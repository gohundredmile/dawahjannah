package com.example.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.RoutineData
import com.example.ui.components.InteractiveRoutineCard
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CalendarHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoutineScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
    val context = LocalContext.current
    val todayDateStr = remember {
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
    val prefs = remember {
        context.getSharedPreferences("routine_checklist_prefs", Context.MODE_PRIVATE)
    }

    val searchQuery by viewModel.routineSearchQuery.collectAsState()
    val selectedSlot by viewModel.routineTimeSlotFilter.collectAsState()
    val routineList by viewModel.filteredRoutineList.collectAsState()

    val completedRoutineIds by viewModel.completedRoutineIds.collectAsState()

    // Persistent checklist state keyed by today's date
    val completedState = remember { mutableStateMapOf<String, Boolean>() }

    // Keep completedState in sync with ViewModel
    LaunchedEffect(completedRoutineIds) {
        completedState.clear()
        completedRoutineIds.forEach { id ->
            completedState[id] = true
        }
    }

    // Helper to persist checked items
    fun persistCompletion(id: String, isCompleted: Boolean) {
        completedState[id] = isCompleted
        viewModel.toggleRoutineItem(id, isCompleted)
    }

    // Helper for Select All / Deselect All
    fun toggleSelectAll(itemsToToggle: List<String>, selectAll: Boolean) {
        itemsToToggle.forEach { id ->
            completedState[id] = selectAll
        }
        viewModel.toggleAllRoutineItems(itemsToToggle, selectAll)
    }

    val completedCount = routineList.count { completedState[it.id] == true }
    val isAllSelected = routineList.isNotEmpty() && routineList.all { completedState[it.id] == true }

    val progressRatio = if (routineList.isNotEmpty()) completedCount.toFloat() / routineList.size.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progressRatio,
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "RoutineProgress"
    )

    val currentSlotData = remember(selectedSlot) {
        RoutineData.timeSlots.find { it.id == selectedSlot } ?: RoutineData.timeSlots.first()
    }

    val listState = rememberLazyListState()
    val filterRowState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var isBannerCollapsed by rememberSaveable { mutableStateOf(false) }

    // Auto-navigate to current time section on tab entry
    val currentWaqtSlotId = remember { viewModel.getCurrentRoutineTimeSlotId() }
    val currentWaqtSlotTitle = remember(currentWaqtSlotId) {
        RoutineData.timeSlots.find { it.id == currentWaqtSlotId }?.titleBn ?: ""
    }

    LaunchedEffect(Unit) {
        viewModel.autoSelectCurrentRoutineTimeSlot()
    }

    LaunchedEffect(selectedSlot) {
        val targetIndex = RoutineData.timeSlots.indexOfFirst { it.id == selectedSlot }
        if (targetIndex >= 0) {
            filterRowState.animateScrollToItem((targetIndex - 1).coerceAtLeast(0))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Slim, Modern Search Bar (Reshaped from thick to thin as requested)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 5.dp)
                .height(40.dp),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(19.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "সুন্নাত আমল, দোয়া, সূরা বা বিভাগ খুঁজুন...",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.70f),
                            maxLines = 1
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.setRoutineSearchQuery(it) },
                        singleLine = true,
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Medium
                        ),
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (searchQuery.isNotEmpty()) {
                    IconButton(
                        onClick = { viewModel.setRoutineSearchQuery("") },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        // Quick Jump to Current Waqt Indicator (if user browsed away to another section)
        if (selectedSlot != currentWaqtSlotId && searchQuery.isEmpty() && currentWaqtSlotTitle.isNotEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 2.dp)
                    .clickable {
                        viewModel.setRoutineTimeSlotFilter(currentWaqtSlotId)
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.50f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "বর্তমান সময়: $currentWaqtSlotTitle",
                            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "আমলে যান ➔",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Horizontal Phase / Time Slot Filter Bar (Auto-scrollable LazyRow)
        LazyRow(
            state = filterRowState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 3.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(RoutineData.timeSlots, key = { it.id }) { slot ->
                val isSelected = selectedSlot == slot.id
                val isCurrentWaqt = slot.id == currentWaqtSlotId
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        viewModel.setRoutineTimeSlotFilter(slot.id)
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = slot.titleBn,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                            if (isCurrentWaqt) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = if (isSelected) Color.White.copy(alpha = 0.28f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "এখন",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.5.sp, fontWeight = FontWeight.Bold),
                                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = if (isCurrentWaqt) Icons.Default.AccessTime else Icons.Default.CheckCircle,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else if (isCurrentWaqt) {
                        {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = "বর্তমান ওয়াক্ত",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else if (isCurrentWaqt) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Branch by Special Views (Scorecard, Weekly Amol, Self-Review) vs Standard Routine List
        when (selectedSlot) {
            "scorecard" -> {
                ScorecardView(
                    completedState = completedState,
                    onToggle = { id, checked -> persistCompletion(id, checked) },
                    onToggleAll = { items, selectAll -> toggleSelectAll(items, selectAll) }
                )
            }
            "weekly" -> {
                WeeklyAmolView(
                    completedState = completedState,
                    onToggle = { id, checked -> persistCompletion(id, checked) },
                    onToggleAll = { items, selectAll -> toggleSelectAll(items, selectAll) }
                )
            }
            "self_review" -> {
                SelfReviewView(
                    prefs = prefs,
                    todayDateStr = todayDateStr,
                    completedState = completedState,
                    onToggle = { id, checked -> persistCompletion(id, checked) },
                    onToggleAll = { items, selectAll -> toggleSelectAll(items, selectAll) }
                )
            }
            else -> {
                // Routine & Checklist View
                // Animated Routine Stage Banner (Pinned & Collapsible)
                if (isBannerCollapsed) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .clickable { isBannerCollapsed = false },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.90f)
                                        )
                                    )
                                )
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = currentSlotData.tagBn,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = currentSlotData.bannerTitleBn,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color.White.copy(alpha = 0.22f)
                                ) {
                                    Text(
                                        text = "${CalendarHelper.toBanglaNumber(completedCount)}/${CalendarHelper.toBanglaNumber(routineList.size)} (${CalendarHelper.toBanglaNumber((animatedProgress * 100).toInt())}%)",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }

                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand banner",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                            .animateContentSize(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.88f)
                                        )
                                    )
                                )
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White.copy(alpha = 0.2f)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.AccessTime,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(13.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = currentSlotData.tagBn,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color.White.copy(alpha = 0.2f)
                                        ) {
                                            Text(
                                                text = "${CalendarHelper.toBanglaNumber(completedCount)} / ${CalendarHelper.toBanglaNumber(routineList.size)} আমল সম্পন্ন",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        Surface(
                                            shape = CircleShape,
                                            color = Color.White.copy(alpha = 0.22f),
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable { isBannerCollapsed = true }
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.KeyboardArrowUp,
                                                contentDescription = "Collapse banner",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .padding(2.dp)
                                                    .size(20.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = currentSlotData.bannerTitleBn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    lineHeight = 20.sp
                                )

                                if (currentSlotData.bannerSubtitleBn.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = currentSlotData.bannerSubtitleBn,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        color = Color.White.copy(alpha = 0.9f),
                                        lineHeight = 16.sp
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Animated Progress Bar
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "আজকের আমল অগ্রগতি",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                            color = Color.White.copy(alpha = 0.85f)
                                        )
                                        Text(
                                            text = "${CalendarHelper.toBanglaNumber((animatedProgress * 100).toInt())}%",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(3.dp))
                                    LinearProgressIndicator(
                                        progress = { animatedProgress },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(5.dp)
                                            .clip(RoundedCornerShape(3.dp)),
                                        color = IslamicGold,
                                        trackColor = Color.White.copy(alpha = 0.25f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Master "All Items Checkbox" & Actions Toolbar
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 3.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Master Checkbox
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable {
                                    toggleSelectAll(routineList.map { it.id }, !isAllSelected)
                                }
                                .padding(end = 8.dp)
                        ) {
                            Checkbox(
                                checked = isAllSelected,
                                onCheckedChange = { checkVal ->
                                    toggleSelectAll(routineList.map { it.id }, checkVal)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = MaterialTheme.colorScheme.primary,
                                    uncheckedColor = MaterialTheme.colorScheme.outline
                                ),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isAllSelected) "সবগুলো আনচেক করুন" else "সবগুলো সম্পন্ন করুন",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        // Reset / Count Info
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (completedCount > 0) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                                    modifier = Modifier.clickable {
                                        toggleSelectAll(routineList.map { it.id }, false)
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Refresh,
                                            contentDescription = "Reset",
                                            tint = MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(13.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "রিসেট",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Text(
                                text = "${CalendarHelper.toBanglaNumber(routineList.size)} টি আমল",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (routineList.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.FilterList,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "অনুসন্ধানের সাথে মিলে এমন কোনো আমল পাওয়া যায়নি।",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 4.dp, bottom = 80.dp)
                    ) {
                        items(routineList, key = { it.id }) { item ->
                            val isChecked = completedState[item.id] == true
                            InteractiveRoutineCard(
                                item = item,
                                isCompleted = isChecked,
                                onToggleCompleted = { checkVal ->
                                    persistCompletion(item.id, checkVal)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 📊 দৈনিক আমলের স্কোরকার্ড (Daily Amol Scorecard)
 * Displays 15 core criteria with live score calculation, target counts, and category badges.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ScorecardView(
    completedState: Map<String, Boolean>,
    onToggle: (String, Boolean) -> Unit,
    onToggleAll: (List<String>, Boolean) -> Unit
) {
    val items = RoutineData.scorecardItems
    val completedScore = items.count { completedState[it.id] == true }
    val totalScore = items.size
    val scorePercentage = if (totalScore > 0) (completedScore * 100) / totalScore else 0
    val isAllSelected = items.isNotEmpty() && items.all { completedState[it.id] == true }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
    ) {
        // Scorecard Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "দৈনিক আমল স্কোরকার্ড",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "আজকের অর্জন: ${CalendarHelper.toBanglaNumber(completedScore)} / ${CalendarHelper.toBanglaNumber(totalScore)} (${CalendarHelper.toBanglaNumber(scorePercentage)}%)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when {
                                scorePercentage >= 80 -> "মাশাআল্লাহ! আপনি মুত্তাকীদের আদর্শ পথে চমৎকারভাবে অগ্রসর হচ্ছেন।"
                                scorePercentage >= 50 -> "আলহামদুলিল্লাহ! আপনার প্রচেষ্টা প্রশংসনীয়, বাকি আমলগুলোও সম্পন্ন করুন।"
                                else -> "প্রতিটি ছোট আমলও আল্লাহর কাছে মহামূল্যবান। শুরু করে দিন!"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier.size(64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { scorePercentage / 100f },
                            modifier = Modifier.size(64.dp),
                            color = IslamicGold,
                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                            strokeWidth = 6.dp
                        )
                        Text(
                            text = "${CalendarHelper.toBanglaNumber(scorePercentage)}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Master Select All for Scorecard
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .clickable { onToggleAll(items.map { it.id }, !isAllSelected) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAllSelected,
                        onCheckedChange = { onToggleAll(items.map { it.id }, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAllSelected) "সবগুলো আনচেক করুন" else "সবগুলো সম্পন্ন চিহ্নিত করুন",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                if (completedScore > 0) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.clickable { onToggleAll(items.map { it.id }, false) }
                    ) {
                        Text(
                            text = "রিসেট",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // Scorecard items list
        items(items, key = { it.id }) { item ->
            val isChecked = completedState[item.id] == true
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onToggle(item.id, !isChecked) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggle(item.id, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            ) {
                                Text(
                                    text = item.categoryBn,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f)
                            ) {
                                Text(
                                    text = item.targetCountBn,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.titleBn,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

/**
 * 📅 সাপ্তাহিক আমল ও শুক্রবারের বিশেষ আমল (Weekly Amol View)
 */
@Composable
private fun WeeklyAmolView(
    completedState: Map<String, Boolean>,
    onToggle: (String, Boolean) -> Unit,
    onToggleAll: (List<String>, Boolean) -> Unit
) {
    val items = RoutineData.weeklyAmolItems
    val fridayItems = items.filter { it.dayGroupBn == "শুক্রবার" }
    val regularItems = items.filter { it.dayGroupBn != "শুক্রবার" }
    val isAllSelected = items.isNotEmpty() && items.all { completedState[it.id] == true }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📅 সাপ্তাহিক আমল ও আত্মপর্যালোচনা",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "শুক্রবার দিনটি সপ্তাহের শ্রেষ্ঠ দিন। এছাড়াও প্রতি সপ্তাহে একবার নিজের আমলের হিসাব নিলে ঈমান সতেজ থাকে।",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Master Select All
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .clickable { onToggleAll(items.map { it.id }, !isAllSelected) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAllSelected,
                        onCheckedChange = { onToggleAll(items.map { it.id }, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAllSelected) "সবগুলো আনচেক করুন" else "সাপ্তাহিক সব আমল সম্পন্ন করুন",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "🕌 শুক্রবারের বিশেষ আমলসমূহ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(fridayItems, key = { it.id }) { item ->
            val isChecked = completedState[item.id] == true
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onToggle(item.id, !isChecked) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggle(item.id, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.titleBn,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.subtitleBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "📊 সপ্তাহে ১ দিন নিজের আমলের হিসাব (মুহাসাবা)",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        items(regularItems, key = { it.id }) { item ->
            val isChecked = completedState[item.id] == true
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onToggle(item.id, !isChecked) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggle(item.id, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.titleBn,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.subtitleBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}

/**
 * 📝 রাতের ২ মিনিটের Self-Review & ৩টি চাওয়া
 */
@Composable
private fun SelfReviewView(
    prefs: android.content.SharedPreferences,
    todayDateStr: String,
    completedState: Map<String, Boolean>,
    onToggle: (String, Boolean) -> Unit,
    onToggleAll: (List<String>, Boolean) -> Unit
) {
    val questions = RoutineData.selfReviewQuestions
    val isAllSelected = questions.isNotEmpty() && questions.all { completedState[it.id] == true }

    // 3 Daily Dua Wishes from User
    var wish1 by rememberSaveable { mutableStateOf(prefs.getString("daily_wish_1_$todayDateStr", "") ?: "") }
    var wish2 by rememberSaveable { mutableStateOf(prefs.getString("daily_wish_2_$todayDateStr", "") ?: "") }
    var wish3 by rememberSaveable { mutableStateOf(prefs.getString("daily_wish_3_$todayDateStr", "") ?: "") }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📝 রাতের ২ মিনিটের Self-Review",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ঘুমানোর পূর্বে বিছানায় শুয়ে মাত্র ২ মিনিট নিজেকে এই ৯টি প্রশ্ন করুন। এটি আপনাকে খাঁটি মুমিন হতে সাহায্য করবে ইনশাআল্লাহ।",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Master Select All
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    .clickable { onToggleAll(questions.map { it.id }, !isAllSelected) }
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isAllSelected,
                        onCheckedChange = { onToggleAll(questions.map { it.id }, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isAllSelected) "সবগুলো আনচেক করুন" else "সবগুলো পর্যালোচনা সম্পন্ন",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        // 9 Questions list
        items(questions, key = { it.id }) { q ->
            val isChecked = completedState[q.id] == true
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .clickable { onToggle(q.id, !isChecked) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isChecked) MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggle(q.id, it) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = q.questionBn,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isChecked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                        )
                        if (q.categoryBn.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = q.categoryBn,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }

        // 3 Wishes Section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "🤲",
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "আল্লাহর কাছে আমার ৩টি বিশেষ চাওয়া",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ঘুমানোর পূর্বে আপনার অন্তরের তিনটি বিশেষ প্রার্থনা লিখুন। প্রতিদিনের তারিখ অনুযায়ী এটি আপনার ফোনে সংরক্ষিত থাকে।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = wish1,
                        onValueChange = {
                            wish1 = it
                            prefs.edit().putString("daily_wish_1_$todayDateStr", it).apply()
                        },
                        placeholder = { Text("১ম চাওয়া (যেমন: গুনাহ মাফ ও হেদায়েত)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = wish2,
                        onValueChange = {
                            wish2 = it
                            prefs.edit().putString("daily_wish_2_$todayDateStr", it).apply()
                        },
                        placeholder = { Text("২য় চাওয়া (যেমন: পিতা-মাতার ক্ষমা ও সুস্থতা)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = wish3,
                        onValueChange = {
                            wish3 = it
                            prefs.edit().putString("daily_wish_3_$todayDateStr", it).apply()
                        },
                        placeholder = { Text("৩য় চাওয়া (যেমন: জান্নাতুল ফিরদাউস লাভ)") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}
