package com.example.ui.screens.tools.hadithtopics

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.model.hadithtopics.HadithTopicCategory
import com.example.data.repository.HadithTopicRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun HadithTopicsScreen(
    onNavigateBack: () -> Unit,
    onOpenInHadithCollection: ((bookSlug: String) -> Unit)? = null,
    onOpenQuranSurah: ((surahNumber: Int) -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val repository = remember { HadithTopicRepository(context) }

    var selectedTopic by remember { mutableStateOf<HadithTopic?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    if (selectedTopic != null) {
        HadithTopicDetailScreen(
            topic = selectedTopic!!,
            repository = repository,
            onNavigateBack = { selectedTopic = null },
            onOpenTopic = { newTopic -> selectedTopic = newTopic },
            onOpenInHadithCollection = onOpenInHadithCollection,
            onOpenQuranSurah = onOpenQuranSurah
        )
        return
    }

    BackHandler { onNavigateBack() }

    val categories = remember { repository.getCategories() }
    val featuredTopics = remember { repository.getFeaturedTopics() }
    val dailyHadith = remember { repository.getDailyHadith() }
    val needBasedEntries = remember { repository.getNeedBasedEntries() }
    val allTopics = remember { repository.getAllTopics() }

    val bookmarkedTopics by repository.bookmarkedTopics.collectAsState()
    val bookmarkedHadiths by repository.bookmarkedHadiths.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "বিষয় ভিত্তিক সহীহ হাদিস",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "সিহাহ সিত্তাহ ও প্রামাণ্য গ্রন্থভিত্তিক সূচীপত্র",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { selectedTab = 2 }) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "বুকমার্ক",
                            tint = if (selectedTab == 2) IslamicGold else MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                placeholder = {
                    Text(
                        "হাদিসের বিষয়, অর্থ, রাবী বা কি-ওয়ার্ড খুঁজুন...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = IslamicGreen
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "মুছুন")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    focusedIndicatorColor = IslamicGreen,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            // Tabs
            if (searchQuery.isBlank()) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    edgePadding = 16.dp,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IslamicGreen
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("বিষয়সমূহ (${allTopics.size})", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("জীবনঘনিষ্ঠ জিজ্ঞাসা (${needBasedEntries.size})", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("সংরক্ষিত (${bookmarkedTopics.size + bookmarkedHadiths.size})", fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            // Tab Content
            if (searchQuery.isNotBlank()) {
                // Search Results
                val (matchedTopics, matchedHadiths) = remember(searchQuery) {
                    repository.searchTopics(searchQuery)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "অনুসন্ধানের ফলাফল: ${matchedTopics.size}টি বিষয়, ${matchedHadiths.size}টি হাদিস",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen
                            )
                        )
                    }

                    if (matchedTopics.isNotEmpty()) {
                        item {
                            Text(
                                text = "সম্পর্কিত বিষয়সমূহ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        items(matchedTopics, key = { "search_topic_${it.id}" }) { topic ->
                            HadithTopicCardItem(
                                topic = topic,
                                onClick = { selectedTopic = topic }
                            )
                        }
                    }

                    if (matchedHadiths.isNotEmpty()) {
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "সরাসরি হাদিসসমূহ",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                        items(matchedHadiths, key = { "search_hadith_${it.hadithId}" }) { hadith ->
                            val isHadithBookmarked = bookmarkedHadiths.contains(hadith.hadithId)
                            TopicHadithCard(
                                hadith = hadith,
                                isBookmarked = isHadithBookmarked,
                                onToggleBookmark = { repository.toggleHadithBookmark(hadith.hadithId) },
                                onOpenInHadithCollection = onOpenInHadithCollection,
                                onOpenQuranSurah = onOpenQuranSurah
                            )
                        }
                    }

                    if (matchedTopics.isEmpty() && matchedHadiths.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "'$searchQuery' সম্পর্কিত কোনো বিষয় পাওয়া যায়নি।\nঅন্য কোনো শব্দ দিয়ে খুঁজুন।",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                when (selectedTab) {
                    0 -> {
                        // Category pills & Topics list
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Category Filter Chips
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    FilterChip(
                                        selected = selectedCategoryFilter == null,
                                        onClick = { selectedCategoryFilter = null },
                                        label = { Text("সকল (${allTopics.size})") }
                                    )
                                    categories.forEach { cat ->
                                        val count = allTopics.count { it.categoryId == cat.id }
                                        FilterChip(
                                            selected = selectedCategoryFilter == cat.id,
                                            onClick = {
                                                selectedCategoryFilter = if (selectedCategoryFilter == cat.id) null else cat.id
                                            },
                                            label = { Text("${cat.iconEmoji} ${cat.nameBn} ($count)") }
                                        )
                                    }
                                }
                            }

                            // Daily Hadith card
                            if (selectedCategoryFilter == null) {
                                item {
                                    Card(
                                        modifier = Modifier.fillMaxWidth(),
                                        shape = RoundedCornerShape(16.dp),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFF4338CA).copy(alpha = 0.08f)
                                        ),
                                        border = BorderStroke(1.dp, Color(0xFF4338CA).copy(alpha = 0.2f))
                                    ) {
                                        Column(modifier = Modifier.padding(14.dp)) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text("✨", fontSize = 18.sp)
                                                Text(
                                                    text = "আজকের নির্দেশনামূলক হাদিস",
                                                    style = MaterialTheme.typography.labelLarge.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFF4338CA)
                                                    )
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = dailyHadith.banglaText,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    lineHeight = 20.sp
                                                ),
                                                maxLines = 3
                                            )
                                            Spacer(modifier = Modifier.height(6.dp))
                                            Text(
                                                text = "— ${dailyHadith.referenceText} (${dailyHadith.gradeBn})",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            val filteredTopics = if (selectedCategoryFilter == null) {
                                allTopics
                            } else {
                                allTopics.filter { it.categoryId == selectedCategoryFilter }
                            }

                            items(filteredTopics, key = { it.id }) { topic ->
                                HadithTopicCardItem(
                                    topic = topic,
                                    onClick = { selectedTopic = topic }
                                )
                            }
                        }
                    }

                    1 -> {
                        // Life / Emotion based questions
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            item {
                                Text(
                                    text = "আপনার মানসিক ও জীবনঘনিষ্ঠ প্রশ্ন বা অনুভূতি নির্বাচন করুন:",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }

                            items(needBasedEntries, key = { it.id }) { entry ->
                                Card(
                                    onClick = {
                                        val topic = repository.getTopicById(entry.targetTopicId)
                                        if (topic != null) {
                                            selectedTopic = topic
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Text(text = entry.iconEmoji, fontSize = 24.sp)
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = entry.questionBn,
                                                style = MaterialTheme.typography.titleMedium.copy(
                                                    fontWeight = FontWeight.SemiBold
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = entry.subtitleBn,
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    2 -> {
                        // Bookmarks
                        val bookmarkedTopicList = remember(bookmarkedTopics) {
                            repository.getBookmarkedTopics()
                        }
                        val bookmarkedHadithList = remember(bookmarkedHadiths) {
                            repository.getBookmarkedHadiths()
                        }

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            if (bookmarkedTopicList.isEmpty() && bookmarkedHadithList.isEmpty()) {
                                item {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 40.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "এখনো কোনো বিষয় বা হাদিস সংরক্ষিত করেননি।\nপড়ার সময় বুকমার্ক আইকনে ক্লিক করে সংরক্ষণ করুন।",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }
                                }
                            } else {
                                if (bookmarkedTopicList.isNotEmpty()) {
                                    item {
                                        Text(
                                            text = "সংরক্ষিত বিষয়সমূহ (${bookmarkedTopicList.size})",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    items(bookmarkedTopicList, key = { "bm_topic_${it.id}" }) { topic ->
                                        HadithTopicCardItem(
                                            topic = topic,
                                            onClick = { selectedTopic = topic }
                                        )
                                    }
                                }

                                if (bookmarkedHadithList.isNotEmpty()) {
                                    item {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = "সংরক্ষিত হাদিসসমূহ (${bookmarkedHadithList.size})",
                                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                        )
                                    }
                                    items(bookmarkedHadithList, key = { "bm_hadith_${it.hadithId}" }) { hadith ->
                                        TopicHadithCard(
                                            hadith = hadith,
                                            isBookmarked = true,
                                            onToggleBookmark = { repository.toggleHadithBookmark(hadith.hadithId) },
                                            onOpenInHadithCollection = onOpenInHadithCollection,
                                            onOpenQuranSurah = onOpenQuranSurah
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
}

@Composable
private fun HadithTopicCardItem(
    topic: HadithTopic,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            topic.iconEmoji?.let { emoji ->
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = emoji, fontSize = 20.sp)
                    }
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = topic.nameBn,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = topic.descriptionBn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = IslamicGreen.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${topic.totalHadiths}টি হাদিস",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = IslamicGreen
                            )
                        )
                    }
                    Text(
                        text = "• ${topic.distinctBooksCount} গ্রন্থ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}
