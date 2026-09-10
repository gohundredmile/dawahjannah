package com.example.ui.screens.sub

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.DuroodAmolData
import com.example.data.model.DuroodAmolItem
import com.example.data.model.DuroodAttractionPoint
import com.example.data.model.DuroodHadithItem
import com.example.data.model.DuroodTabCategory
import com.example.ui.theme.LocalAppFontFamily
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DuroodAmolFullScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
    var fontScale by rememberSaveable { mutableFloatStateOf(1.0f) }
    var showFontSizeControls by rememberSaveable { mutableStateOf(false) }
    var isSearchActive by rememberSaveable { mutableStateOf(false) }
    var searchQuery by rememberSaveable { mutableStateOf("") }

    // Repetition counter map for each card
    val cardCounters = remember { mutableStateMapOf<String, Int>() }
    var friday1000Counter by rememberSaveable { mutableIntStateOf(0) }
    var morning10Counter by rememberSaveable { mutableIntStateOf(0) }
    var evening10Counter by rememberSaveable { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    val tabs = listOf(
        DuroodTabCategory.SPECIAL_ATTRACTION,
        DuroodTabCategory.SAHIH_DUROOD,
        DuroodTabCategory.AMOL_1,
        DuroodTabCategory.AMOL_2,
        DuroodTabCategory.AMOL_3,
        DuroodTabCategory.BEST_AND_VIRTUES,
        DuroodTabCategory.TIMINGS
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. LIVE AURORA WALLPAPER BACKGROUND (Magical Light Mode Effect)
        LiveAuroraWallpaperBackground()

        // 2. MAIN CONTENT OVERLAY
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP APP BAR
            Surface(
                color = Color.White.copy(alpha = 0.85f),
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "ফিরে যান",
                                tint = Color(0xFF047857)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "দুরুদ শরীফের আমল",
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalAppFontFamily.current,
                                    color = Color(0xFF064E3B)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "লাইভ অরোরা",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF047857),
                                            fontFamily = LocalAppFontFamily.current
                                        )
                                    }
                                }
                            }
                            Text(
                                text = "আমল, বিশুদ্ধ উচ্চারণ, ফযিলত ও বরকত ভাণ্ডার",
                                fontSize = 11.5.sp,
                                color = Color(0xFF4B5563),
                                fontFamily = LocalAppFontFamily.current
                            )
                        }

                        // Text Size Control Toggle Button
                        IconButton(
                            onClick = { showFontSizeControls = !showFontSizeControls }
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatSize,
                                contentDescription = "ফন্ট সাইজ পরিবর্তন",
                                tint = if (showFontSizeControls) Color(0xFF059669) else Color(0xFF374151)
                            )
                        }

                        // Search Toggle Button
                        IconButton(
                            onClick = {
                                isSearchActive = !isSearchActive
                                if (!isSearchActive) searchQuery = ""
                            }
                        ) {
                            Icon(
                                imageVector = if (isSearchActive) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "অনুসন্ধান",
                                tint = if (isSearchActive) Color(0xFFDC2626) else Color(0xFF374151)
                            )
                        }
                    }

                    // COLLAPSIBLE FONT SIZE CONTROL BAR (User-friendly: text large/small)
                    AnimatedVisibility(visible = showFontSizeControls) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFECFDF5).copy(alpha = 0.95f),
                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "টেক্সট সাইজ:",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF065F46),
                                        fontFamily = LocalAppFontFamily.current
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    val percentageBn = when {
                                        fontScale <= 0.85f -> "৮৫%"
                                        fontScale <= 1.0f -> "১০০% (স্বাভাবিক)"
                                        fontScale <= 1.15f -> "১১৫%"
                                        fontScale <= 1.30f -> "১৩০%"
                                        else -> "১৫০% (বড়)"
                                    }
                                    Text(
                                        text = percentageBn,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857),
                                        fontFamily = LocalAppFontFamily.current
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // A- (Small)
                                    Surface(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clickable {
                                                if (fontScale > 0.85f) fontScale -= 0.15f
                                            },
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "A-",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 13.sp,
                                                color = Color(0xFF065F46)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // Reset to 1.0
                                    Surface(
                                        modifier = Modifier
                                            .height(34.dp)
                                            .clickable { fontScale = 1.0f }
                                            .padding(horizontal = 4.dp),
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                                    ) {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = 8.dp)) {
                                            Text(
                                                text = "রিসেট",
                                                fontWeight = FontWeight.SemiBold,
                                                fontSize = 11.sp,
                                                color = Color(0xFF374151),
                                                fontFamily = LocalAppFontFamily.current
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // A+ (Large)
                                    Surface(
                                        modifier = Modifier
                                            .size(34.dp)
                                            .clickable {
                                                if (fontScale < 1.5f) fontScale += 0.15f
                                            },
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "A+",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = Color(0xFF065F46)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // COLLAPSIBLE SEARCH BAR
                    AnimatedVisibility(visible = isSearchActive) {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color.White.copy(alpha = 0.95f),
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = {
                                    Text(
                                        text = "দরূদের নাম বা ফজিলত খুঁজুন (যেমন: তাজ, নারিয়া, শিফা)...",
                                        fontSize = 13.sp,
                                        fontFamily = LocalAppFontFamily.current
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF10B981),
                                    unfocusedBorderColor = Color(0xFFD1D5DB)
                                ),
                                shape = RoundedCornerShape(12.dp),
                                trailingIcon = {
                                    if (searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { searchQuery = "" }) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = "মুছুন", tint = Color.Gray)
                                        }
                                    }
                                }
                            )
                        }
                    }

                    // SCROLLABLE TABS (5 Distinct Tabs / Sections)
                    ScrollableTabRow(
                        selectedTabIndex = selectedTabIndex,
                        edgePadding = 12.dp,
                        containerColor = Color.Transparent,
                        divider = {},
                        indicator = {}
                    ) {
                        tabs.forEachIndexed { index, tab ->
                            val isSelected = selectedTabIndex == index
                            Tab(
                                selected = isSelected,
                                onClick = {
                                    selectedTabIndex = index
                                    coroutineScope.launch {
                                        listState.scrollToItem(0)
                                    }
                                },
                                modifier = Modifier.padding(vertical = 6.dp, horizontal = 3.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (isSelected) Color(0xFF059669) else Color.White.copy(alpha = 0.8f),
                                    border = BorderStroke(
                                        1.dp,
                                        if (isSelected) Color(0xFF047857) else Color(0xFF10B981).copy(alpha = 0.25f)
                                    ),
                                    shadowElevation = if (isSelected) 3.dp else 0.dp
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = tab.tabTitleBn,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.White else Color(0xFF374151),
                                            fontSize = 13.sp,
                                            fontFamily = LocalAppFontFamily.current
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) Color.White.copy(alpha = 0.25f) else Color(0xFFF3F4F6)
                                        ) {
                                            Text(
                                                text = tab.badgeBn,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (isSelected) Color.White else Color(0xFF059669),
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                fontFamily = LocalAppFontFamily.current
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // LIST CONTENT FOR THE ACTIVE TAB
            val activeTab = tabs[selectedTabIndex]

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                // TAB 0: আকর্ষণ ও ১০০০ আমল (৪টি আত্মিক বিষয়, মিরাকেল দুআ ও জুমার ১০০০ আমল)
                if (activeTab == DuroodTabCategory.SPECIAL_ATTRACTION) {
                    item {
                        TabHeaderBanner(
                            title = "দুরুদের প্রতি আকর্ষণ ও জুমার বিশেষ আমল",
                            subtitle = "৪টি অন্তরের উপলব্ধি, বিপদ মুক্তির মিরাকেল দুআ ও জুমার ১০০০ বার দরূদ আমল",
                            countText = "আত্মিক প্রশান্তি ও হাজত পূরণ"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Section 1: দুরুদের প্রতি আকর্ষণ বাড়ান Header
                    item {
                        AttractionHeaderCard(fontScale = fontScale)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    val attractionItems = if (searchQuery.isBlank()) {
                        DuroodAmolData.attractionPoints
                    } else {
                        DuroodAmolData.attractionPoints.filter {
                            it.titleBn.contains(searchQuery, ignoreCase = true) ||
                                it.descriptionBn.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    items(attractionItems, key = { it.numberBn }) { point ->
                        AttractionPointCard(point = point, fontScale = fontScale, context = context)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Section 2: Miracle Companion: Istighfar & Dua Yunus
                    item {
                        MiracleCompanionCard(fontScale = fontScale, context = context)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Section 3: Friday 1000 Durood Amol & Live Interactive Tracker
                    item {
                        Friday1000AmolInteractiveCard(
                            count = friday1000Counter,
                            onAddCount = { addAmount ->
                                friday1000Counter = (friday1000Counter + addAmount).coerceAtLeast(0)
                                vibrateHaptic(context)
                            },
                            onResetCount = {
                                friday1000Counter = 0
                            },
                            fontScale = fontScale,
                            context = context
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // TAB 1: সহীহ দরূদ ভাণ্ডার (৬টি সহীহ দরূদ, অর্থ ও শব্দার্থ)
                else if (activeTab == DuroodTabCategory.SAHIH_DUROOD) {
                    val items = if (searchQuery.isBlank()) {
                        DuroodAmolData.sahihDuroodItems
                    } else {
                        DuroodAmolData.sahihDuroodItems.filter {
                            it.titleBn.contains(searchQuery, ignoreCase = true) ||
                                it.pronunciationBn.contains(searchQuery, ignoreCase = true) ||
                                it.meaningBn.contains(searchQuery, ignoreCase = true) ||
                                it.virtuesBn.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    item {
                        TabHeaderBanner(
                            title = "বিশুদ্ধ হাদীসসম্মত সহীহ দরূদ ভাণ্ডার",
                            subtitle = "সহীহ বুখারী, মুসলিম, নাসায়ী ও আবু দাউদের বর্ণিত বিশুদ্ধ দরূদ, বাংলা উচ্চারণ, অর্থ ও শব্দার্থ",
                            countText = "${items.size}টি সহীহ দরূদ"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Advice banner (দুরুদ ফারসী শব্দ... আরবী দেখে তিলাওয়াতের গুরুত্ব)
                    item {
                        SahihDuroodIntroCard(
                            text = DuroodAmolData.sahihDuroodIntro,
                            fontScale = fontScale,
                            context = context
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    items(items, key = { it.id }) { item ->
                        DuroodCard(
                            item = item,
                            fontScale = fontScale,
                            counter = cardCounters[item.id] ?: 0,
                            onIncrementCounter = {
                                val current = cardCounters[item.id] ?: 0
                                cardCounters[item.id] = current + 1
                                vibrateHaptic(context)
                            },
                            onResetCounter = {
                                cardCounters[item.id] = 0
                            },
                            context = context
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Morning & Evening 10 Times Shafa'at Hadith & Dual Tracker Card
                    item {
                        MorningEveningDuroodCard(
                            hadith = DuroodAmolData.morningEveningShafaatHadith,
                            morningCount = morning10Counter,
                            eveningCount = evening10Counter,
                            onIncrementMorning = {
                                morning10Counter = (morning10Counter + 1).coerceAtMost(100)
                                vibrateHaptic(context)
                            },
                            onResetMorning = { morning10Counter = 0 },
                            onIncrementEvening = {
                                evening10Counter = (evening10Counter + 1).coerceAtMost(100)
                                vibrateHaptic(context)
                            },
                            onResetEvening = { evening10Counter = 0 },
                            fontScale = fontScale,
                            context = context
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }

                // TAB 2: আমল ০১ (১২টি দরূদ)
                else if (activeTab == DuroodTabCategory.AMOL_1) {
                    val items = if (searchQuery.isBlank()) {
                        DuroodAmolData.amol1Items
                    } else {
                        DuroodAmolData.amol1Items.filter {
                            it.titleBn.contains(searchQuery, ignoreCase = true) ||
                                it.pronunciationBn.contains(searchQuery, ignoreCase = true) ||
                                it.virtuesBn.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    item {
                        TabHeaderBanner(
                            title = "দুরুদ শরীফ এর আমল ০১",
                            subtitle = "দরূদে ইব্রাহীম, বরকতময় দুরূদ, দরূদে তাজ, নারিয়া, ফাতিহ সহ ১২টি মর্যাদাপূর্ণ আমল",
                            countText = "${items.size}টি বরকতময় দরূদ"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    items(items, key = { it.id }) { item ->
                        DuroodCard(
                            item = item,
                            fontScale = fontScale,
                            counter = cardCounters[item.id] ?: 0,
                            onIncrementCounter = {
                                val current = cardCounters[item.id] ?: 0
                                cardCounters[item.id] = current + 1
                                vibrateHaptic(context)
                            },
                            onResetCounter = {
                                cardCounters[item.id] = 0
                            },
                            context = context
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                // TAB 1: আমল ০২ (১০টি দরূদ)
                else if (activeTab == DuroodTabCategory.AMOL_2) {
                    val items = if (searchQuery.isBlank()) {
                        DuroodAmolData.amol2Items
                    } else {
                        DuroodAmolData.amol2Items.filter {
                            it.titleBn.contains(searchQuery, ignoreCase = true) ||
                                it.pronunciationBn.contains(searchQuery, ignoreCase = true) ||
                                it.virtuesBn.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    item {
                        TabHeaderBanner(
                            title = "দুরুদ শরীফ এর আমল ০২",
                            subtitle = "বৃষ্টির সময়, অন্তরের নূর, ৮০ বছরের গুনাহ মাফ, দরূদে মাহী, তুনাজ্জিনা, খাইর ও ফুতুহাত",
                            countText = "${items.size}টি বিশেষ দরূদ"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    items(items, key = { it.id }) { item ->
                        DuroodCard(
                            item = item,
                            fontScale = fontScale,
                            counter = cardCounters[item.id] ?: 0,
                            onIncrementCounter = {
                                val current = cardCounters[item.id] ?: 0
                                cardCounters[item.id] = current + 1
                                vibrateHaptic(context)
                            },
                            onResetCounter = {
                                cardCounters[item.id] = 0
                            },
                            context = context
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                // TAB 2: আমল ০৩ ও বরকত (২০টি বরকতময় দরূদ)
                else if (activeTab == DuroodTabCategory.AMOL_3) {
                    val items = if (searchQuery.isBlank()) {
                        DuroodAmolData.amol3Items
                    } else {
                        DuroodAmolData.amol3Items.filter {
                            it.titleBn.contains(searchQuery, ignoreCase = true) ||
                                it.pronunciationBn.contains(searchQuery, ignoreCase = true) ||
                                it.virtuesBn.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    item {
                        TabHeaderBanner(
                            title = "দুরুদ শরীফ এর আমল ০৩ (নেয়ামত ও বরকত)",
                            subtitle = "দরুদে শিফা, শাফায়াত, লাখ সওয়াব, ধন-সম্পদ বৃদ্ধি, স্মরণ শক্তি বৃদ্ধি ও বালা-মুসিবত মুক্তি",
                            countText = "${items.size}টি অত্যন্ত ফজিলতপূর্ণ দরূদ"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    items(items, key = { it.id }) { item ->
                        DuroodCard(
                            item = item,
                            fontScale = fontScale,
                            counter = cardCounters[item.id] ?: 0,
                            onIncrementCounter = {
                                val current = cardCounters[item.id] ?: 0
                                cardCounters[item.id] = current + 1
                                vibrateHaptic(context)
                            },
                            onResetCounter = {
                                cardCounters[item.id] = 0
                            },
                            context = context
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }

                // TAB 3: সর্বোত্তম দরূদ ও হাদিস (১৪টি বিশেষ হাদিস ও ৪০ ফজিলত)
                else if (activeTab == DuroodTabCategory.BEST_AND_VIRTUES) {
                    item {
                        TabHeaderBanner(
                            title = "সর্বোত্তম দুরুদ ও হাদিস ভাণ্ডার",
                            subtitle = "দরূদে ইব্রাহীমের শ্রেষ্ঠত্ব, ১৪টি সহীহ হাদিস ও ৪০ প্রকার আধ্যাত্মিক সৌভাগ্য",
                            countText = "পূর্ণাঙ্গ দলিল ও শানে নুযুল"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // Best Durood Featured Card
                    item {
                        BestDuroodHeroCard(fontScale = fontScale, context = context)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // 14 Hadith Section Header
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "দরূদ শরীফের ১৪টি বিশেষ হাদীস ও মর্যাদা",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF065F46),
                                fontFamily = LocalAppFontFamily.current
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            HorizontalDivider(
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF10B981).copy(alpha = 0.3f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(DuroodAmolData.hadithList, key = { it.numberBn }) { hadith ->
                        HadithCard(hadith = hadith, fontScale = fontScale, context = context)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // 40 Virtues Explanation Card
                    item {
                        VirtuesSummaryCard(
                            title = "দরূদ শরীফ পাঠে ৪০ প্রকার মর্যাদা ও বরকত",
                            subtitle = "ইমাম ইবনুল কাইয়্যূম (রহ.)-এর বিশ্লেষণ",
                            content = DuroodAmolData.bestDuroodIntro,
                            fontScale = fontScale,
                            context = context
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    // General Virtues Card
                    item {
                        VirtuesSummaryCard(
                            title = "দরূদ শরীফ পাঠের সাধারণ ও বিশেষ ফযিলত",
                            subtitle = "কোরআন ও হাদিসের আলোকে রহমত ও শাফায়াত",
                            content = DuroodAmolData.generalVirtuesSummary,
                            fontScale = fontScale,
                            context = context
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                // TAB 4: পড়ার মোক্ষম সময় (১০টি বিশেষ সময়)
                else if (activeTab == DuroodTabCategory.TIMINGS) {
                    item {
                        TabHeaderBanner(
                            title = "দুরূদ পড়ার মোক্ষম ও বরকতময় সময়",
                            subtitle = "সুন্নাহর আলোকে যে ১০টি সময়ে দরূদ পাঠের বিশেষ তাগিদ ও ফজিলত রয়েছে",
                            countText = "১০টি বিশেষ সময়"
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                    }

                    items(DuroodAmolData.duroodRecitationTimes, key = { it.first }) { timing ->
                        TimingItemCard(
                            serial = timing.first,
                            text = timing.second,
                            fontScale = fontScale,
                            context = context
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFECFDF5).copy(alpha = 0.9f),
                            border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "আদব ও আন্তরিকতার নসিহত",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    fontFamily = LocalAppFontFamily.current
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "দরূদ শরীফ পাঠের সময় পূর্ণ তাযীম, একাগ্রতা ও ওজু অবস্থায় কিবলামুখী হয়ে পাঠ করা সর্বোত্তম। তবে যে কোনো হালে, হাঁটা-চলা কিংবা অবসরে মনের ভক্তি দিয়ে দরূদ পাঠ করলেও অফুরন্ত সওয়াব ও রহমত লাভ হয়।",
                                    fontSize = (13.5f * fontScale).sp,
                                    lineHeight = (21 * fontScale).sp,
                                    color = Color(0xFF1F2937),
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

/**
 * Live Aurora Shimmering Wallpaper:
 * An ethereal, living northern-lights atmosphere on a calm lite palette.
 * Floating glowing waves that slowly drift and breathe across the screen.
 */
@Composable
private fun LiveAuroraWallpaperBackground() {
    val infiniteTransition = rememberInfiniteTransition(label = "auroraTransition")

    // Slow organic rotating phases
    val phase1 by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(22000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase1"
    )

    val phase2 by infiniteTransition.animateFloat(
        initialValue = 2f * Math.PI.toFloat(),
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "phase2"
    )

    // Breathing pulse for ethereal glow
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.25f,
        targetValue = 0.45f,
        animationSpec = infiniteRepeatable(
            animation = tween(7000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        // 1. Serene lite background base (comforting mint-ivory)
        drawRect(
            color = Color(0xFFF7FAF8)
        )

        // 2. Aurora Wave 1: Soft Celestial Emerald (Top Left - Center)
        val center1 = Offset(
            x = width * 0.25f + cos(phase1) * (width * 0.2f),
            y = height * 0.2f + sin(phase1) * (height * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF6EE7B7).copy(alpha = pulse * 0.8f),
                    Color(0xFFA7F3D0).copy(alpha = pulse * 0.4f),
                    Color.Transparent
                ),
                center = center1,
                radius = width * 0.85f
            ),
            center = center1,
            radius = width * 0.85f
        )

        // 3. Aurora Wave 2: Celestial Cyan / Sky Breeze (Top Right)
        val center2 = Offset(
            x = width * 0.75f + sin(phase2) * (width * 0.22f),
            y = height * 0.4f + cos(phase2) * (height * 0.18f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFF7DD3FC).copy(alpha = pulse * 0.7f),
                    Color(0xFFBAE6FD).copy(alpha = pulse * 0.35f),
                    Color.Transparent
                ),
                center = center2,
                radius = width * 0.75f
            ),
            center = center2,
            radius = width * 0.75f
        )

        // 4. Aurora Wave 3: Warm Dawn Gold / Amber Glow (Bottom Center)
        val center3 = Offset(
            x = width * 0.45f + cos(phase2 * 0.8f) * (width * 0.25f),
            y = height * 0.75f + sin(phase1 * 0.8f) * (height * 0.15f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFDE68A).copy(alpha = pulse * 0.65f),
                    Color(0xFFFEF3C7).copy(alpha = pulse * 0.3f),
                    Color.Transparent
                ),
                center = center3,
                radius = width * 0.8f
            ),
            center = center3,
            radius = width * 0.8f
        )

        // 5. Aurora Wave 4: Lavender Spiritual Aura (Bottom Right)
        val center4 = Offset(
            x = width * 0.8f + sin(phase1 * 1.1f) * (width * 0.18f),
            y = height * 0.85f + cos(phase2 * 1.1f) * (height * 0.12f)
        )
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFE9D5FF).copy(alpha = pulse * 0.55f),
                    Color(0xFFF3E8FF).copy(alpha = pulse * 0.25f),
                    Color.Transparent
                ),
                center = center4,
                radius = width * 0.65f
            ),
            center = center4,
            radius = width * 0.65f
        )
    }
}

@Composable
private fun TabHeaderBanner(
    title: String,
    subtitle: String,
    countText: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.88f)),
        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46),
                    fontFamily = LocalAppFontFamily.current
                )
                Surface(
                    color = Color(0xFF10B981).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = countText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontFamily = LocalAppFontFamily.current
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 12.5.sp,
                color = Color(0xFF4B5563),
                lineHeight = 17.sp,
                fontFamily = LocalAppFontFamily.current
            )
        }
    }
}

@Composable
private fun DuroodCard(
    item: DuroodAmolItem,
    fontScale: Float,
    counter: Int,
    onIncrementCounter: () -> Unit,
    onResetCounter: () -> Unit,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.90f)
        ),
        border = BorderStroke(1.2.dp, Color(0xFF10B981).copy(alpha = 0.22f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // 1. Header: Serial Badge + Title + Action Icons (Copy, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = Color(0xFF059669),
                    shape = CircleShape,
                    modifier = Modifier.size(28.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = item.serialNoBn,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = LocalAppFontFamily.current
                        )
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = item.titleBn,
                    fontSize = (16 * fontScale).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF064E3B),
                    fontFamily = LocalAppFontFamily.current,
                    modifier = Modifier.weight(1f)
                )

                // Copy Action Button
                IconButton(
                    onClick = {
                        val fullText = buildString {
                            appendLine(item.titleBn)
                            appendLine()
                            appendLine(item.arabicText)
                            appendLine()
                            appendLine("বাংলা উচ্চারণ:")
                            appendLine(item.pronunciationBn)
                            if (item.meaningBn.isNotBlank()) {
                                appendLine()
                                appendLine("বাংলা অর্থ:")
                                appendLine(item.meaningBn)
                            }
                            if (item.virtuesBn.isNotBlank()) {
                                appendLine()
                                appendLine("ফযিলত:")
                                appendLine(item.virtuesBn)
                            }
                            if (item.referenceBn.isNotBlank()) {
                                appendLine()
                                appendLine("দলিল / সূত্র:")
                                appendLine(item.referenceBn)
                            }
                        }
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(item.titleBn, fullText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "${item.titleBn} কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি করুন",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Share Action Button
                IconButton(
                    onClick = {
                        val fullText = buildString {
                            appendLine(item.titleBn)
                            appendLine()
                            appendLine(item.arabicText)
                            appendLine()
                            appendLine("বাংলা উচ্চারণ:")
                            appendLine(item.pronunciationBn)
                            if (item.meaningBn.isNotBlank()) {
                                appendLine()
                                appendLine("বাংলা অর্থ:")
                                appendLine(item.meaningBn)
                            }
                            if (item.virtuesBn.isNotBlank()) {
                                appendLine()
                                appendLine("ফযিলত:")
                                appendLine(item.virtuesBn)
                            }
                            if (item.referenceBn.isNotBlank()) {
                                appendLine()
                                appendLine("দলিল / সূত্র:")
                                appendLine(item.referenceBn)
                            }
                        }
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, fullText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, item.titleBn)
                        context.startActivity(shareIntent)
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "শেয়ার করুন",
                        tint = Color(0xFF4B5563),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. Arabic Text Presentation (Elegant Islamic typography)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF0FDF4).copy(alpha = 0.9f),
                border = BorderStroke(1.dp, Color(0xFF86EFAC).copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    Text(
                        text = item.arabicText,
                        fontSize = (23 * fontScale).sp,
                        lineHeight = (40 * fontScale).sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Right,
                        color = Color(0xFF064E3B),
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 3. Bangla Pronunciation (বাংলা উচ্চারণ)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9FAFB), shape = RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = "বাংলা উচ্চারণ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    fontFamily = LocalAppFontFamily.current
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.pronunciationBn,
                    fontSize = (14.5f * fontScale).sp,
                    lineHeight = (22 * fontScale).sp,
                    color = Color(0xFF1F2937),
                    fontFamily = LocalAppFontFamily.current
                )
            }

            // 3.5. Bangla Meaning (বাংলা অর্থ)
            if (item.meaningBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "বাংলা অর্থ:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857),
                            fontFamily = LocalAppFontFamily.current
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.meaningBn,
                            fontSize = (14.5f * fontScale).sp,
                            lineHeight = (22 * fontScale).sp,
                            color = Color(0xFF1F2937),
                            fontFamily = LocalAppFontFamily.current
                        )
                    }
                }
            }

            // 3.6. Word by Word Meanings (শব্দার্থ বিশ্লেষণ)
            if (item.wordByWordMeaningsBn.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "শব্দের বাংলা অর্থ বিশ্লেষণ:",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46),
                            fontFamily = LocalAppFontFamily.current
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        item.wordByWordMeaningsBn.chunked(2).forEach { rowPairs ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowPairs.forEach { (word, meaning) ->
                                    Surface(
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color.White,
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = word,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF047857),
                                                fontFamily = LocalAppFontFamily.current
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "→",
                                                fontSize = 11.sp,
                                                color = Color(0xFF9CA3AF)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = meaning,
                                                fontSize = 11.sp,
                                                color = Color(0xFF374151),
                                                fontFamily = LocalAppFontFamily.current
                                            )
                                        }
                                    }
                                }
                                if (rowPairs.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // 4. Virtues & Benefits (ফযিলত)
            if (item.virtuesBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFEF3C7).copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "ফযিলত ও আমল:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E),
                                fontFamily = LocalAppFontFamily.current
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.virtuesBn,
                            fontSize = (13.5f * fontScale).sp,
                            lineHeight = (21 * fontScale).sp,
                            color = Color(0xFF78350F),
                            fontFamily = LocalAppFontFamily.current
                        )
                    }
                }
            }

            // Reference (দলিল ও সূত্র)
            if (item.referenceBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = Color(0xFFF3F4F6),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "দলিল: ${item.referenceBn}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4B5563),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontFamily = LocalAppFontFamily.current
                    )
                }
            }

            // Notes
            if (item.notesBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.notesBn,
                    fontSize = (12f * fontScale).sp,
                    lineHeight = 17.sp,
                    color = Color(0xFF6B7280),
                    fontFamily = LocalAppFontFamily.current
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 5. Interactive Practice & Tasbih Counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (item.recommendedCountBn.isNotBlank()) {
                    Surface(
                        color = Color(0xFFE0E7FF),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "লক্ষ্য: ${item.recommendedCountBn}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF3730A3),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = LocalAppFontFamily.current
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Interactive Counter Pill
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (counter > 0) {
                        IconButton(
                            onClick = onResetCounter,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "রিসেট",
                                tint = Color(0xFF9CA3AF),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                    }

                    Surface(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { onIncrementCounter() },
                        color = if (counter > 0) Color(0xFF059669) else Color(0xFFF3F4F6),
                        border = BorderStroke(
                            1.dp,
                            if (counter > 0) Color(0xFF047857) else Color(0xFFD1D5DB)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = if (counter > 0) Color.White else Color(0xFF059669),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = if (counter == 0) "জপ কাউন্ট (০)" else "পঠিত: $counter বার",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (counter > 0) Color.White else Color(0xFF374151),
                                fontFamily = LocalAppFontFamily.current
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BestDuroodHeroCard(
    fontScale: Float,
    context: Context
) {
    val bestArabic = "اَللّٰھُمَّ صَلِّ عَلٰی مُحَمَّدٍ وَّعَلٰٓی اٰلِ مُحَمَّدٍ کَمَا صَلَّیْتَ عَلٰٓی اِبْرَاھِیْمَ وَعَلٰٓی اٰلِ اِبْرَاھِیْمَ اِنَّکَ حَمِیْدٌ مَّجِیْدٌ اَللّٰھُمَّ بَارِکْ عَلٰی مُحَمَّدٍ وَّعَلٰٓی اٰلِ مُحَمَّدٍ کَمَا بَارَکْتَ عَلٰٓی اِبْرَاھِیْمَ وَعَلٰٓی اٰلِ اِبْرَاھِیْمَ اِنَّکَ حَمِیْدٌ مَّجِیْدٌ"
    val bestPronunciation = "আল্লাহুম্মা ছাল্লিআলা মুহাম্মাদিও ওয়া আলা আলি মুহাম্মাদিন কামা সাল্লাইতা আলা ইব্রাহীমা ওয়া আলা আলি ইব্রাহীম ইন্নাকা হামিদুম মাজীদ। আল্লাহুম্মা বারিক আলা মুহাম্মাদিও ওয়া আলাআলি মুহাম্মাঁদিন কামা বারকতা আলা ইব্রাহীমা ওয়া আলা আলি ইব্রাহীম ইন্নাকা হামিদুম মাজীদ।"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        border = BorderStroke(1.5.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color(0xFF059669),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "সবার সেরা ও সর্বোত্তম দরূদ",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val fullText = "সর্বোত্তম দরূদ: দরূদে ইব্রাহীম\n\n$bestArabic\n\nউচ্চারণ:\n$bestPronunciation"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("সর্বোত্তম দরূদ", fullText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "দরূদে ইব্রাহীম কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "দরূদে ইব্রাহীম (الصلاة الإبراهيمية)",
                fontSize = (18 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF064E3B),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFECFDF5),
                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
            ) {
                Text(
                    text = bestArabic,
                    fontSize = (24 * fontScale).sp,
                    lineHeight = (42 * fontScale).sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Right,
                    color = Color(0xFF064E3B),
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF9FAFB), shape = RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Text(
                    text = "বাংলা উচ্চারণ:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    fontFamily = LocalAppFontFamily.current
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = bestPronunciation,
                    fontSize = (14.5f * fontScale).sp,
                    lineHeight = (22 * fontScale).sp,
                    color = Color(0xFF1F2937),
                    fontFamily = LocalAppFontFamily.current
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFEF3C7).copy(alpha = 0.45f),
                border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.25f))
            ) {
                Text(
                    text = "এ দরুদ নবী করীম সাল্লাল্লাহু আলাইহি ওয়াসাল্লাম-এর পাক জবান হতে সরাসরি সাহাবায়ে কেরামকে শিক্ষা দেওয়া। অতএব, এ দরুদ সব দরুদের সেরা দরুদ। সকল উলামায়ে কেরাম একমত যে সর্বোত্তম দরূদ হল দরূদে ইব্রাহীমী যা নামাযের মধ্যে পড়া হয়। মহান আল্লাহ প্রিয় হাবীবকে সান্ত্বনা দিয়ে বলেন: 'আপনার উম্মতের যে কোনো দোয়া আমি কবুল করে নেব যদি তারা তার সাথে দরুদ শরীফ বেঁধে দেয়।' (ইবনে মাজাহ)",
                    fontSize = (13.5f * fontScale).sp,
                    lineHeight = (21 * fontScale).sp,
                    color = Color(0xFF78350F),
                    modifier = Modifier.padding(14.dp),
                    fontFamily = LocalAppFontFamily.current
                )
            }
        }
    }
}

@Composable
private fun HadithCard(
    hadith: DuroodHadithItem,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF047857),
                        shape = CircleShape,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = hadith.numberBn,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = LocalAppFontFamily.current
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = hadith.narratorBn,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val fullText = "হাদিস (${hadith.numberBn}): ${hadith.narratorBn}\n\n${hadith.textBn}\n\nসূত্র: ${hadith.sourceBn}"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("হাদিস ${hadith.numberBn}", fullText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "হাদিস কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = hadith.textBn,
                fontSize = (14.5f * fontScale).sp,
                lineHeight = (22 * fontScale).sp,
                color = Color(0xFF1F2937),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFFF3F4F6),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "সূত্র: ${hadith.sourceBn}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontFamily = LocalAppFontFamily.current
                )
            }
        }
    }
}

@Composable
private fun VirtuesSummaryCard(
    title: String,
    subtitle: String,
    content: String,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        border = BorderStroke(1.2.dp, Color(0xFF10B981).copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46),
                        fontFamily = LocalAppFontFamily.current
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val fullText = "$title\n$subtitle\n\n$content"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(title, fullText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "অনুলিপি কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFE5E7EB))
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = content.trim(),
                fontSize = (14 * fontScale).sp,
                lineHeight = (22 * fontScale).sp,
                color = Color(0xFF374151),
                fontFamily = LocalAppFontFamily.current
            )
        }
    }
}

@Composable
private fun TimingItemCard(
    serial: String,
    text: String,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.90f)),
        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = Color(0xFFECFDF5),
                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f)),
                shape = CircleShape,
                modifier = Modifier.size(34.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = serial,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = LocalAppFontFamily.current
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Text(
                text = text,
                fontSize = (14.5f * fontScale).sp,
                lineHeight = (21 * fontScale).sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                fontFamily = LocalAppFontFamily.current,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("দুরূদ পড়ার সময়", text)
                    clipboard.setPrimaryClip(clip)
                    Toast.makeText(context, "কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ContentCopy,
                    contentDescription = "কপি",
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun vibrateHaptic(context: Context) {
    try {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createOneShot(20, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(20)
            }
        }
    } catch (_: Exception) {}
}

private fun toBengaliNumber(num: Int): String {
    val bnDigits = charArrayOf('০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯')
    return num.toString().map { if (it in '0'..'9') bnDigits[it - '0'] else it }.joinToString("")
}

@Composable
private fun AttractionHeaderCard(fontScale: Float) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color(0xFF047857),
        shadowElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "দুরুদের প্রতি আকর্ষণ বাড়ান",
                        fontSize = (17 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = LocalAppFontFamily.current
                    )
                    Text(
                        text = "হৃদয় উজাড় করে নবীপ্রেম ও ভক্তির সাথে দরূদ পাঠের ৪টি কারণ",
                        fontSize = (12 * fontScale).sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontFamily = LocalAppFontFamily.current
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "চারটি বিষয় সর্বদা মাথায় রেখে দরূদ পড়ুন, আপনার দরূদ পাঠের ব্যাকুলতা ও আকর্ষণ ইনশাআল্লাহ বাড়তেই থাকবে।",
                fontSize = (13.5f * fontScale).sp,
                lineHeight = (20 * fontScale).sp,
                color = Color.White.copy(alpha = 0.95f),
                fontFamily = LocalAppFontFamily.current
            )
        }
    }
}

@Composable
private fun AttractionPointCard(
    point: DuroodAttractionPoint,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        border = BorderStroke(1.2.dp, Color(0xFF10B981).copy(alpha = 0.25f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        color = Color(0xFF059669),
                        shape = CircleShape,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = point.numberBn,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = LocalAppFontFamily.current
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = point.titleBn,
                        fontSize = (15.5f * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF064E3B),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val text = "${point.numberBn}) ${point.titleBn}\n\n${point.descriptionBn}\n\nসূত্র: ${point.hadithRefBn}"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText(point.titleBn, text)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = point.descriptionBn,
                fontSize = (14f * fontScale).sp,
                lineHeight = (22 * fontScale).sp,
                color = Color(0xFF1F2937),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = Color(0xFFECFDF5),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.25f)),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "দলিল: ${point.hadithRefBn}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF047857),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontFamily = LocalAppFontFamily.current
                )
            }
        }
    }
}

@Composable
private fun MiracleCompanionCard(
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
        border = BorderStroke(1.5.dp, Color(0xFF3B82F6).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.5.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = Color(0xFF2563EB),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "জীবন পরিবর্তনের পাথেয়",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            fontFamily = LocalAppFontFamily.current
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "দরূদের সাথে মিরাকেল আমল",
                        fontSize = (15 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E3A8A),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("মিরাকেল আমল", DuroodAmolData.miracleCompanionGuidance)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "আমলটি কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "সাথে যোগ করুন অবিরত ইস্তেগফার, এবং দুআ ইউনুস। আপনার জীবনে আল্লাহ তাআলার বিশেষ রহমত ও মিরাকেল আসতেই থাকবে ইনশাআল্লাহ।",
                fontSize = (13.5f * fontScale).sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1E40AF),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Istighfar Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFEFF6FF),
                border = BorderStroke(1.dp, Color(0xFF93C5FD))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "১. অবিরত ইস্তেগফার (ক্ষমা প্রার্থনা):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D4ED8),
                        fontFamily = LocalAppFontFamily.current
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "أَسْتَغْفِرُ اللّٰهَ الْعَظِيمَ وَأَتُوبُ إِلَيْهِ",
                        fontSize = (19 * fontScale).sp,
                        lineHeight = (32 * fontScale).sp,
                        color = Color(0xFF1E3A8A),
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "উচ্চারণ: আসতাগফিরুল্লাহাল আযীম ওয়া আতূবু ইলাইহি।",
                        fontSize = (13 * fontScale).sp,
                        color = Color(0xFF374151),
                        fontFamily = LocalAppFontFamily.current
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ফযিলত: নিয়মিত ইস্তিগফার করলে আল্লাহ সকল সংকট থেকে মুক্তির পথ করে দেন, সব দুশ্চিন্তা দূর করেন এবং অভাবনীয় রিযিক দেন। [আবু দাউদ: ১৫১৮]",
                        fontSize = (12 * fontScale).sp,
                        color = Color(0xFF4B5563),
                        fontFamily = LocalAppFontFamily.current
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Dua Yunus Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.dp, Color(0xFF86EFAC))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "২. দুআ ইউনুস (বিপদ ও কঠিন সমস্যা মুক্তির শ্রেষ্ঠ দোয়া):",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = LocalAppFontFamily.current
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "لَا إِلٰهَ إِلَّا أَنْتَ سُبْحَانَكَ إِنِّي كُنْتُ مِنَ الظَّالِمِينَ",
                        fontSize = (19 * fontScale).sp,
                        lineHeight = (32 * fontScale).sp,
                        color = Color(0xFF064E3B),
                        fontFamily = FontFamily.Serif,
                        textAlign = TextAlign.Right,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = "উচ্চারণ: লা ইলাহা ইল্লা আনতা সুবহানাকা ইন্নী কুনতু মিনায্ যা-লিমীন।",
                        fontSize = (13 * fontScale).sp,
                        color = Color(0xFF374151),
                        fontFamily = LocalAppFontFamily.current
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "ফযিলত: যেকোনো সমস্যায় এই দুআ পাঠ করে ফরিয়াদ করলে আল্লাহ অবশ্যই প্রার্থনা কবুল করেন। [তিরমিযী: ৩৫০৫]",
                        fontSize = (12 * fontScale).sp,
                        color = Color(0xFF4B5563),
                        fontFamily = LocalAppFontFamily.current
                    )
                }
            }
        }
    }
}

@Composable
private fun Friday1000AmolInteractiveCard(
    count: Int,
    onAddCount: (Int) -> Unit,
    onResetCount: () -> Unit,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
        border = BorderStroke(1.5.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color(0xFF047857),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "★★★ বিশেষ জুমার আমল ★★★",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("১০০০ বার দরূদ আমল", DuroodAmolData.friday1000AmolSummary)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "আমল কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "দুরুদ শরীফের আমল (১০০০ বার পাঠ)",
                fontSize = (17 * fontScale).sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF064E3B),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "শুক্রবার ফজরের ফরজ সালাতের পর থেকে মাগরিব পর্যন্ত আমলটি করতে হবে। নির্দিষ্ট নিয়ত করে, মনের নির্দিষ্ট আশা পূরণের লক্ষ্যে, হাজত পূরণের লক্ষ্যে, নির্দিষ্ট সমস্যার সমাধানের জন্য এবং কাঙ্ক্ষিত বস্তু পাওয়ার জন্য দৈনিক ও জুমার দিনে ১০০০ বার দরূদ পাঠ করলে মনের নেক ইচ্ছা ও বাসনা আল্লাহ পাক পূরণ করে দেন।",
                fontSize = (13.5f * fontScale).sp,
                lineHeight = (21 * fontScale).sp,
                color = Color(0xFF374151),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFFEF3C7).copy(alpha = 0.5f),
                border = BorderStroke(1.dp, Color(0xFFF59E0B).copy(alpha = 0.3f))
            ) {
                Text(
                    text = "আমলের সংখ্যা: কমপক্ষে ১০০০ বার। ছোট দুরূদ যেমন 'সাল্লাল্লাহু আলাইহি ওয়া সাল্লাম' পড়লেই হবে। আস্তাগফারের সাথে আল্লাহর কাছে চাইতে হবে।",
                    fontSize = (12.5f * fontScale).sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF92400E),
                    modifier = Modifier.padding(12.dp),
                    fontFamily = LocalAppFontFamily.current
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LIVE INTERACTIVE 1000 TRACKER
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF0FDF4),
                border = BorderStroke(1.2.dp, Color(0xFF10B981).copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "১০০০ বার পাঠ ট্র্যাকার",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857),
                            fontFamily = LocalAppFontFamily.current
                        )

                        if (count > 0) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onResetCount() },
                                color = Color(0xFFFEE2E2),
                                border = BorderStroke(1.dp, Color(0xFFF87171).copy(alpha = 0.4f))
                            ) {
                                Text(
                                    text = "রিসেট",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFDC2626),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Big Counter Display
                    Text(
                        text = "${toBengaliNumber(count)} / ১০০০",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (count >= 1000) Color(0xFF047857) else Color(0xFF065F46),
                        fontFamily = LocalAppFontFamily.current
                    )

                    val progress = (count.toFloat() / 1000f).coerceIn(0f, 1f)
                    val percentage = (progress * 100).toInt()

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF059669),
                        trackColor = Color(0xFFD1FAE5)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "অগ্রগতি: ${toBengaliNumber(percentage)}% সম্পন্ন",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF059669),
                        fontFamily = LocalAppFontFamily.current
                    )

                    if (count >= 1000) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFF10B981),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "মাশাআল্লাহ! আপনি ১০০০ বার পাঠ পূর্ণ করেছেন।",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Tap buttons: +1, +10, +100
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onAddCount(1) },
                            color = Color(0xFF059669),
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "+১ বার",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onAddCount(10) },
                            color = Color(0xFF047857),
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "+১০ বার",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onAddCount(100) },
                            color = Color(0xFF064E3B),
                            shadowElevation = 2.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "+১০০ বার",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SahihDuroodIntroCard(
    text: String,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.92f)),
        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "দুরুদের তাৎপর্য ও বিশুদ্ধ পাঠের নসীহত",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF065F46),
                    fontFamily = LocalAppFontFamily.current
                )

                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("দুরুদের তাৎপর্য", text)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "অনুলিপি কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text.trim(),
                fontSize = (13.5f * fontScale).sp,
                lineHeight = (21 * fontScale).sp,
                color = Color(0xFF374151),
                fontFamily = LocalAppFontFamily.current
            )
        }
    }
}

@Composable
private fun MorningEveningDuroodCard(
    hadith: DuroodHadithItem,
    morningCount: Int,
    eveningCount: Int,
    onIncrementMorning: () -> Unit,
    onResetMorning: () -> Unit,
    onIncrementEvening: () -> Unit,
    onResetEvening: () -> Unit,
    fontScale: Float,
    context: Context
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.94f)),
        border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = Color(0xFF047857),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "সকাল ও সন্ধ্যার সুন্নাত",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontFamily = LocalAppFontFamily.current
                    )
                }

                IconButton(
                    onClick = {
                        val text = "${hadith.narratorBn}\n\n${hadith.textBn}\n\nসূত্র: ${hadith.sourceBn}"
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("সকাল-সন্ধ্যা দরূদ হাদিস", text)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "হাদিস কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = hadith.textBn,
                fontSize = (14.5f * fontScale).sp,
                lineHeight = (22 * fontScale).sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1F2937),
                fontFamily = LocalAppFontFamily.current
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFFF3F4F6),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "সূত্র: ${hadith.sourceBn}",
                    fontSize = 11.sp,
                    color = Color(0xFF4B5563),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                    fontFamily = LocalAppFontFamily.current
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Two quick 10-count tracker boxes: Morning & Evening
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Morning Box
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFFBEB),
                    border = BorderStroke(1.dp, Color(0xFFFCD34D))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "সকাল (১০ বার)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            fontFamily = LocalAppFontFamily.current
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${toBengaliNumber(morningCount)} / ১০",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (morningCount >= 10) Color(0xFF059669) else Color(0xFFB45309),
                            fontFamily = LocalAppFontFamily.current
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onIncrementMorning() },
                                color = Color(0xFFD97706)
                            ) {
                                Text(
                                    text = "+১",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                            if (morningCount > 0) {
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onResetMorning() },
                                    color = Color(0xFFF3F4F6)
                                ) {
                                    Text(
                                        text = "রিসেট",
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B7280),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                        fontFamily = LocalAppFontFamily.current
                                    )
                                }
                            }
                        }
                    }
                }

                // Evening Box
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = BorderStroke(1.dp, Color(0xFF86EFAC))
                ) {
                    Column(
                        modifier = Modifier.padding(10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "সন্ধ্যা (১০ বার)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF065F46),
                            fontFamily = LocalAppFontFamily.current
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${toBengaliNumber(eveningCount)} / ১০",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (eveningCount >= 10) Color(0xFF059669) else Color(0xFF047857),
                            fontFamily = LocalAppFontFamily.current
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Surface(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { onIncrementEvening() },
                                color = Color(0xFF059669)
                            ) {
                                Text(
                                    text = "+১",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontFamily = LocalAppFontFamily.current
                                )
                            }
                            if (eveningCount > 0) {
                                Surface(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { onResetEvening() },
                                    color = Color(0xFFF3F4F6)
                                ) {
                                    Text(
                                        text = "রিসেট",
                                        fontSize = 11.sp,
                                        color = Color(0xFF6B7280),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                        fontFamily = LocalAppFontFamily.current
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
