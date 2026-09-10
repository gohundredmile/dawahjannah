package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.RoutineData
import com.example.ui.components.InteractiveRoutineCard
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CalendarHelper
import kotlinx.coroutines.launch

@Composable
fun RoutineScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
    val searchQuery by viewModel.routineSearchQuery.collectAsState()
    val selectedSlot by viewModel.routineTimeSlotFilter.collectAsState()
    val routineList by viewModel.filteredRoutineList.collectAsState()

    val completedState = remember { mutableStateMapOf<String, Boolean>() }
    val completedCount = routineList.count { completedState[it.id] == true }
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
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        // Top Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setRoutineSearchQuery(it) },
            placeholder = { Text("সুন্নাত আমল, দোয়া বা সূরা খুঁজুন...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.primary
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setRoutineSearchQuery("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Horizontal Phase / Time Slot Filter Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            RoutineData.timeSlots.forEach { slot ->
                val isSelected = selectedSlot == slot.id
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        viewModel.setRoutineTimeSlotFilter(slot.id)
                        coroutineScope.launch {
                            listState.animateScrollToItem(0)
                        }
                    },
                    label = {
                        Text(
                            text = slot.titleBn,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    leadingIcon = if (isSelected) {
                        {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
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
                        borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        enabled = true,
                        selected = isSelected
                    )
                )
            }
        }

        // Animated Routine Stage Banner (Captures the exact banner style from screenshots)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                    .padding(16.dp)
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
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccessTime,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
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

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "${CalendarHelper.toBanglaNumber(completedCount)} / ${CalendarHelper.toBanglaNumber(routineList.size)} আমল সম্পন্ন",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = currentSlotData.bannerTitleBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = currentSlotData.bannerSubtitleBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Animated Progress Bar
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "আজকের অগ্রগতি",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Text(
                                text = "${(animatedProgress * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { animatedProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = IslamicGold,
                            trackColor = Color.White.copy(alpha = 0.25f)
                        )
                    }
                }
            }
        }

        // Subheader status
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "আমলের ধারাবাহিক তালিকা (তাহাজ্জুদ থেকে শয়ন)",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${CalendarHelper.toBanglaNumber(routineList.size)} টি আমল অন্তর্ভুক্ত",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
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
                            completedState[item.id] = checkVal
                        }
                    )
                }
            }
        }
    }
}
