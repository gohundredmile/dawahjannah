package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranActionEngineCatalog
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.datasource.SmartQuranCatalog
import com.example.data.model.AyahActionInsight
import com.example.data.model.QuranSurah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InteractiveAyahPickerSheet(
    currentInsight: AyahActionInsight,
    onSelectInsight: (AyahActionInsight) -> Unit,
    onDismiss: () -> Unit,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("সকল") }
    var selectedSurahNumber by remember { mutableStateOf<Int?>(null) }

    val categories = remember {
        listOf(
            "সকল",
            "ধৈর্য ও পরীক্ষা",
            "রিযিক ও তাওয়াক্কুল",
            "মানসিক প্রশান্তি",
            "তাওবাহ ও ক্ষমা",
            "পিতামাতা ও পরিবার",
            "আখলাক ও শিষ্টাচার",
            "তাওহীদ ও সুরক্ষা",
            "জীবনের উদ্দেশ্য"
        )
    }

    val allEssential = remember { QuranActionEngineCatalog.getAllAyahs() }

    val filteredEssential = remember(searchQuery, selectedCategoryFilter) {
        val baseList = if (searchQuery.isBlank()) allEssential else QuranActionEngineCatalog.searchAyahs(searchQuery)
        if (selectedCategoryFilter == "সকল") {
            baseList
        } else {
            baseList.filter { insight ->
                insight.primaryThemes.any { it.contains(selectedCategoryFilter, ignoreCase = true) } ||
                when (selectedCategoryFilter) {
                    "ধৈর্য ও পরীক্ষা" -> insight.primaryThemes.any { it.contains("ধৈর্য") || it.contains("পরীক্ষা") || it.contains("সবর") }
                    "রিযিক ও তাওয়াক্কুল" -> insight.primaryThemes.any { it.contains("রিযিক") || it.contains("তাওয়াক্কুল") || it.contains("উপার্জন") }
                    "মানসিক প্রশান্তি" -> insight.primaryThemes.any { it.contains("শান্তি") || it.contains("সান্ত্বনা") || it.contains("প্রশান্তি") }
                    "তাওবাহ ও ক্ষমা" -> insight.primaryThemes.any { it.contains("তাওবা") || it.contains("ক্ষমা") || it.contains("মাগফিরাত") }
                    "পিতামাতা ও পরিবার" -> insight.primaryThemes.any { it.contains("পিতা") || it.contains("মাতা") || it.contains("পরিবার") || it.contains("দাম্পত্য") }
                    "আখলাক ও শিষ্টাচার" -> insight.primaryThemes.any { it.contains("বিনয়") || it.contains("আখলাক") || it.contains("গীবত") || it.contains("শিষ্টাচার") }
                    "তাওহীদ ও সুরক্ষা" -> insight.primaryThemes.any { it.contains("তাওহীদ") || it.contains("সুরক্ষা") || it.contains("হেফাযত") || it.contains("রুকইয়াহ") }
                    "জীবনের উদ্দেশ্য" -> insight.primaryThemes.any { it.contains("উদ্দেশ্য") || it.contains("আমল") || it.contains("মৃত্যু") || it.contains("সময়ের") }
                    else -> true
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(MaterialTheme.colorScheme.surface)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "পবিত্র কুরআনের আয়াত নির্বাচন (অফলাইন)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
                Text(
                    text = "জীবনের গুরুত্বপূর্ণ আয়াতসমূহ ও ১১৪টি সূরা",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
            }
        }

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            placeholder = {
                Text(
                    text = "সূরা নাম, আয়াত নম্বর (যেমন: ২:১৫৩) বা বিষয় খুঁজুন...",
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont)
                )
            },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, contentDescription = "ক্লিয়ার")
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        // 3 Main Navigation Tabs
        TabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Text(
                        text = "জীবনঘনিষ্ঠ আয়াত (${filteredEssential.size})",
                        fontFamily = banglaFont,
                        fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Text(
                        text = "১১৪টি সূরা ব্রাউজ",
                        fontFamily = banglaFont,
                        fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = {
                    Text(
                        text = "জীবনের পরিস্থিতি",
                        fontFamily = banglaFont,
                        fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        when (selectedTab) {
            // TAB 0: Life-Essential Ayats
            0 -> {
                // Category Filter Chips
                val filterScroll = rememberScrollState()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(filterScroll)
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = cat == selectedCategoryFilter
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategoryFilter = cat },
                            label = {
                                Text(
                                    text = cat,
                                    fontFamily = banglaFont,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                // Ayahs List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    items(filteredEssential, key = { it.ayahId }) { item ->
                        val isCurrent = item.ayahId == currentInsight.ayahId
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            ),
                            border = if (isCurrent) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary)
                            else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectInsight(item)
                                    onDismiss()
                                }
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer
                                    ) {
                                        Text(
                                            text = item.surahNameBangla,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrent) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer,
                                                fontFamily = banglaFont
                                            )
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        item.primaryThemes.take(2).forEach { tag ->
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant
                                            ) {
                                                Text(
                                                    text = tag,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontSize = 10.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontFamily = banglaFont
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Arabic Snippet
                                Text(
                                    text = item.arabicText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = arabicFont,
                                        lineHeight = 28.sp,
                                        textAlign = TextAlign.Right
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Bangla Translation
                                Text(
                                    text = item.banglaTranslation,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 20.sp
                                    ),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                // Action Teaser
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🎯", fontSize = 12.sp)
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = item.defaultTodayAction,
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = banglaFont,
                                            fontSize = 11.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // TAB 1: 114 Surahs Browser (Offline)
            1 -> {
                val surahs = remember(searchQuery) {
                    val q = searchQuery.trim().lowercase()
                    if (q.isBlank()) QuranSurahCatalog.all114Surahs
                    else {
                        QuranSurahCatalog.all114Surahs.filter { s ->
                            s.nameBn.lowercase().contains(q) ||
                            s.nameEn.lowercase().contains(q) ||
                            s.nameAr.contains(q) ||
                            s.number.toString() == q ||
                            s.meaningBn.lowercase().contains(q)
                        }
                    }
                }

                if (selectedSurahNumber == null) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 24.dp)
                    ) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "যে কোনো সূরা নির্বাচন করুন; তার অফলাইনে সংরক্ষিত আয়াতসমূহ আমল ইঞ্জিনে তাদাব্বুরের জন্য খুলবে।",
                                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                        }

                        items(surahs, key = { it.number }) { surah ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedSurahNumber = surah.number }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = CircleShape,
                                            color = MaterialTheme.colorScheme.primaryContainer,
                                            modifier = Modifier.size(36.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = "${surah.number}",
                                                    style = MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                                    )
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "${surah.nameBn} (${surah.meaningBn})",
                                                style = MaterialTheme.typography.titleSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = banglaFont
                                                )
                                            )
                                            Text(
                                                text = "${surah.revelationType} • মোট আয়াত: ${surah.totalAyat}",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontFamily = banglaFont
                                                )
                                            )
                                        }
                                    }

                                    Text(
                                        text = surah.nameAr,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontFamily = arabicFont,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    )
                                }
                            }
                        }
                    }
                } else {
                    // Ayahs for selected surah
                    val surahNum = selectedSurahNumber!!
                    val surahObj = remember(surahNum) { QuranSurahCatalog.all114Surahs.find { it.number == surahNum } }
                    val bundledAyahs = remember(surahNum) {
                        QuranSurahCatalog.preBundledAyahs[surahNum] ?: emptyList()
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                        ) {
                            IconButton(onClick = { selectedSurahNumber = null }) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "পিছনে যান")
                            }
                            Column {
                                Text(
                                    text = "${surahObj?.nameBn} (${surahObj?.nameAr})",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                )
                                Text(
                                    text = "আয়াত নির্বাচন করে সরাসরি আমল ইঞ্জিন শুরু করুন",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                )
                            }
                        }

                        if (bundledAyahs.isEmpty()) {
                            // Check if present in allEssential
                            val essentialMatches = allEssential.filter { it.surahNumber == surahNum }
                            if (essentialMatches.isNotEmpty()) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    contentPadding = PaddingValues(bottom = 24.dp)
                                ) {
                                    items(essentialMatches) { insight ->
                                        Card(
                                            shape = RoundedCornerShape(12.dp),
                                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    onSelectInsight(insight)
                                                    onDismiss()
                                                }
                                        ) {
                                            Column(modifier = Modifier.padding(12.dp)) {
                                                Text(
                                                    text = insight.surahNameBangla,
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontFamily = banglaFont
                                                    )
                                                )
                                                Text(
                                                    text = insight.arabicText,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        fontFamily = arabicFont,
                                                        textAlign = TextAlign.Right
                                                    ),
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                                Text(
                                                    text = insight.banglaTranslation,
                                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont)
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(text = "📖", fontSize = 36.sp)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "এই সূরার মূল আয়াতসমূহ লোড করতে অনলাইন কুরআন ভিউয়ার ব্যবহার করুন অথবা ট্যাব ১-এ জীবনঘনিষ্ঠ আয়াতসমূহ ব্রাউজ করুন।",
                                            textAlign = TextAlign.Center,
                                            style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont)
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Button(onClick = { selectedSurahNumber = null }) {
                                            Text("অন্য সূরা দেখুন", fontFamily = banglaFont)
                                        }
                                    }
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                contentPadding = PaddingValues(bottom = 24.dp)
                            ) {
                                items(bundledAyahs) { ayah ->
                                    val generatedInsight = remember(ayah) {
                                        QuranActionEngineCatalog.fromQuranAyah(ayah, surahObj)
                                    }

                                    Card(
                                        shape = RoundedCornerShape(12.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                onSelectInsight(generatedInsight)
                                                onDismiss()
                                            }
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Surface(
                                                    shape = RoundedCornerShape(6.dp),
                                                    color = MaterialTheme.colorScheme.primaryContainer
                                                ) {
                                                    Text(
                                                        text = "আয়াত ${ayah.ayahNumber}",
                                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                                        style = MaterialTheme.typography.labelSmall.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                            fontFamily = banglaFont
                                                        )
                                                    )
                                                }

                                                Text(
                                                    text = "তাদাব্বুর করুন →",
                                                    style = MaterialTheme.typography.labelSmall.copy(
                                                        color = MaterialTheme.colorScheme.primary,
                                                        fontWeight = FontWeight.Bold,
                                                        fontFamily = banglaFont
                                                    )
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(6.dp))

                                            Text(
                                                text = ayah.arabicText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontFamily = arabicFont,
                                                    textAlign = TextAlign.Right,
                                                    lineHeight = 26.sp
                                                ),
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Spacer(modifier = Modifier.height(4.dp))

                                            Text(
                                                text = ayah.translationBn,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    fontFamily = banglaFont,
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // TAB 2: Life Situations Matcher
            2 -> {
                val situations = remember {
                    listOf(
                        Triple("উদ্বেগ, ভয় ও মানসিক অস্থিরতা", "🌿", "action_13_28"),
                        Triple("রিযিক ও জীবিকার দুশ্চিন্তা", "💰", "action_65_2"),
                        Triple("বিপদ, পরীক্ষা ও দুঃখ-কষ্ট", "🌧️", "action_2_155"),
                        Triple("হতাশা ও পাপের পর ক্ষমা", "🕊️", "action_39_53"),
                        Triple("রাগ ও মেজাজ নিয়ন্ত্রণ", "🧘", "action_3_133"),
                        Triple("পিতা-মাতার সম্মান ও সেবা", "👨‍👩‍👧", "action_17_23"),
                        Triple("দাম্পত্য ভালোবাসা ও শান্তি", "💍", "action_30_21"),
                        Triple("মানুষের শত্রুতা ও কটূক্তি", "🛡️", "action_41_34"),
                        Triple("গীবত, উপহাস ও বদনজর", "🚫", "action_49_11"),
                        Triple("সময়ের অপচয় ও অলসতা", "⏳", "action_103_1"),
                        Triple("আল্লাহর সাহায্য ও প্রার্থনা", "🤲", "action_2_186"),
                        Triple("নিঃসঙ্গতা ও সান্ত্বনা", "☀️", "action_93_3")
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        Text(
                            text = "আপনার মনের অবস্থা বা জীবনের প্রয়োজন নির্বাচন করুন:",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            ),
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    items(situations) { (title, emoji, targetId) ->
                        val targetInsight = allEssential.find { it.ayahId == targetId }
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (targetInsight != null) {
                                        onSelectInsight(targetInsight)
                                        onDismiss()
                                    }
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = emoji, fontSize = 24.sp)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = title,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        if (targetInsight != null) {
                                            Text(
                                                text = "${targetInsight.surahNameBangla}: ${targetInsight.banglaTranslation.take(35)}...",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontFamily = banglaFont
                                                )
                                            )
                                        }
                                    }
                                }
                                Icon(Icons.Default.ChevronRight, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }
    }
}
