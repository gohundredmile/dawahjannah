package com.example.ui.screens.tools.qurantopics

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.qurantopics.QuranTopicCatalog
import com.example.data.model.qurantopics.NeedBasedTopicEntry
import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.QuranTopicCategory
import com.example.data.repository.QuranTopicRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun QuranTopicsScreen(
    onNavigateBack: () -> Unit,
    onOpenInQuran: (surahNumber: Int) -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val repository = remember { QuranTopicRepository(context) }

    var selectedTopic by remember { mutableStateOf<QuranTopic?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }

    // If a topic is opened, show TopicDetailScreen
    if (selectedTopic != null) {
        TopicDetailScreen(
            topic = selectedTopic!!,
            repository = repository,
            onNavigateBack = { selectedTopic = null },
            onOpenTopic = { newTopic -> selectedTopic = newTopic },
            onOpenInQuran = onOpenInQuran
        )
        return
    }

    BackHandler { onNavigateBack() }

    val categories = remember { repository.getCategories() }
    val featuredTopics = remember { repository.getFeaturedTopics() }
    val dailyTopic = remember { repository.getDailyTopic() }
    val needBasedEntries = remember { repository.getNeedBasedEntries() }
    val allTopics = remember { repository.getAllTopics() }

    val bookmarkedTopics by repository.bookmarkedTopics.collectAsState()
    val bookmarkedAyahs by repository.bookmarkedAyahs.collectAsState()

    // Live search results
    val searchResults = remember(searchQuery) {
        if (searchQuery.isNotBlank()) repository.searchTopics(searchQuery) else emptyList()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "বিষয় ভিত্তিক কোরআনের আয়াত",
                            fontSize = 17.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "কুরআনের পূর্ণাঙ্গ বিষয়ভিত্তিক ইনডেক্স ও রেফারেন্স",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ফিরে যান")
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
                .padding(contentPadding)
        ) {
            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("বিষয় খুঁজুন (যেমন: নামাজ, ধৈর্য, রিজিক, মা-বাবা)...", fontSize = 13.5.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = IslamicGreen)
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "মুছুন")
                            }
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = IslamicGreen,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    )
                )
            }

            // If Search Query active, display search results directly
            if (searchQuery.isNotBlank()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Text(
                            text = "অনুসন্ধানের ফলাফল (${searchResults.size}টি বিষয় পাওয়া গেছে)",
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        )
                    }

                    if (searchResults.isEmpty()) {
                        item {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 40.dp),
                                shape = RoundedCornerShape(16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = "📖", fontSize = 34.sp)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "'$searchQuery' সম্পর্কিত কোনো বিষয় পাওয়া যায়নি",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "অন্য কোনো শব্দ যেমন: ধৈর্য, দোয়া, তওবা, মা-বাবা দিয়ে চেষ্টা করুন।",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        items(searchResults, key = { it.id }) { topic ->
                            TopicListItemCard(
                                topic = topic,
                                isBookmarked = bookmarkedTopics.contains(topic.id),
                                onClick = { selectedTopic = topic }
                            )
                        }
                    }
                }
            } else {
                // Navigation Tabs
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = IslamicGreen
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = { Text("ড্যাশবোর্ড", fontSize = 13.sp, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = { Text("সকল বিষয়", fontSize = 13.sp, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTab == 2,
                        onClick = { selectedTab = 2 },
                        text = { Text("প্রয়োজনে", fontSize = 13.sp, fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTab == 3,
                        onClick = { selectedTab = 3 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("সংরক্ষিত", fontSize = 13.sp, fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal)
                                if (bookmarkedTopics.isNotEmpty() || bookmarkedAyahs.isNotEmpty()) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        shape = CircleShape,
                                        color = IslamicGold,
                                        modifier = Modifier.size(16.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${bookmarkedTopics.size + bookmarkedAyahs.size}",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    )
                }

                when (selectedTab) {
                    0 -> DashboardTab(
                        dailyTopic = dailyTopic,
                        featuredTopics = featuredTopics,
                        categories = categories,
                        bookmarkedTopics = bookmarkedTopics,
                        onOpenTopic = { selectedTopic = it },
                        onSelectCategory = { catId ->
                            selectedCategoryFilter = catId
                            selectedTab = 1
                        }
                    )
                    1 -> AllTopicsTab(
                        allTopics = allTopics,
                        categories = categories,
                        selectedCategoryId = selectedCategoryFilter,
                        onSelectCategory = { selectedCategoryFilter = it },
                        bookmarkedTopics = bookmarkedTopics,
                        onOpenTopic = { selectedTopic = it }
                    )
                    2 -> NeedBasedTab(
                        entries = needBasedEntries,
                        onOpenNeedTopic = { targetId ->
                            QuranTopicCatalog.getTopicById(targetId)?.let {
                                selectedTopic = it
                            }
                        }
                    )
                    3 -> BookmarksTab(
                        repository = repository,
                        onOpenTopic = { selectedTopic = it },
                        onOpenInQuran = onOpenInQuran
                    )
                }
            }
        }
    }
}

@Composable
private fun DashboardTab(
    dailyTopic: QuranTopic,
    featuredTopics: List<QuranTopic>,
    categories: List<QuranTopicCategory>,
    bookmarkedTopics: Set<String>,
    onOpenTopic: (QuranTopic) -> Unit,
    onSelectCategory: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Daily Topic Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenTopic(dailyTopic) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = IslamicGreen.copy(alpha = 0.1f)
                ),
                border = BorderStroke(1.2.dp, IslamicGreen.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IslamicGold.copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("আজকের বিষয়", fontSize = 11.5.sp, fontWeight = FontWeight.Bold, color = IslamicGold)
                            }
                        }

                        Text(
                            text = "${dailyTopic.totalAyahs}টি আয়াত",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = dailyTopic.nameBn,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = dailyTopic.descriptionBn,
                        fontSize = 13.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "বিস্তারিত পড়ুন ও আয়াত দেখুন",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = IslamicGreen, modifier = Modifier.size(14.dp))
                    }
                }
            }
        }

        // Featured Topics
        item {
            Column {
                Text(
                    text = "গুরুত্বপূর্ণ ও নির্বাচিত বিষয়সমূহ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    featuredTopics.take(6).forEach { topic ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .width(160.dp)
                                .clickable { onOpenTopic(topic) }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(text = topic.iconEmoji ?: "📖", fontSize = 22.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = topic.nameBn,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${topic.totalAyahs}টি আয়াত",
                                    fontSize = 11.5.sp,
                                    color = IslamicGreen,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Main Categories Grid
        item {
            Text(
                text = "বিষয়ভিত্তিক প্রধান ক্যাটাগরি (${categories.size}টি)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        items(categories) { category ->
            val count = QuranTopicCatalog.getTopicsByCategory(category.id).size
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectCategory(category.id) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(category.colorHex).copy(alpha = 0.12f),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(text = category.iconEmoji, fontSize = 22.sp)
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = category.nameBn,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "$count বিষয়",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = category.descriptionBn,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AllTopicsTab(
    allTopics: List<QuranTopic>,
    categories: List<QuranTopicCategory>,
    selectedCategoryId: String?,
    onSelectCategory: (String?) -> Unit,
    bookmarkedTopics: Set<String>,
    onOpenTopic: (QuranTopic) -> Unit
) {
    val filtered = remember(allTopics, selectedCategoryId) {
        if (selectedCategoryId == null) allTopics else allTopics.filter { it.categoryId == selectedCategoryId }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Category horizontal filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedCategoryId == null,
                onClick = { onSelectCategory(null) },
                label = { Text("সকল ক্যাটাগরি (${allTopics.size})", fontSize = 12.sp) }
            )
            categories.forEach { cat ->
                FilterChip(
                    selected = selectedCategoryId == cat.id,
                    onClick = { onSelectCategory(if (selectedCategoryId == cat.id) null else cat.id) },
                    label = { Text("${cat.iconEmoji} ${cat.nameBn}", fontSize = 12.sp) }
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(filtered, key = { it.id }) { topic ->
                TopicListItemCard(
                    topic = topic,
                    isBookmarked = bookmarkedTopics.contains(topic.id),
                    onClick = { onOpenTopic(topic) }
                )
            }
        }
    }
}

@Composable
private fun NeedBasedTab(
    entries: List<NeedBasedTopicEntry>,
    onOpenNeedTopic: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = IslamicGreen.copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "আপনার মনের অবস্থা বা অনুভূতির আয়াত",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "যেকোনো সময়ে আপনার অবস্থা বা প্রয়োজন নির্বাচন করে পবিত্র কুরআন থেকে সরাসরি নির্দেশনা ও আত্মিক সান্ত্বনা লাভ করুন।",
                        fontSize = 12.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        items(entries) { entry ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenNeedTopic(entry.targetTopicId) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = entry.iconEmoji, fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = entry.emotionOrNeedBn,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = entry.subtitleBn,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = IslamicGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookmarksTab(
    repository: QuranTopicRepository,
    onOpenTopic: (QuranTopic) -> Unit,
    onOpenInQuran: (surahNumber: Int) -> Unit
) {
    var subTab by remember { mutableIntStateOf(0) }
    val bookmarkedTopics = repository.getBookmarkedTopics()
    val bookmarkedAyahs = repository.getBookmarkedAyahs()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = subTab == 0,
                onClick = { subTab = 0 },
                label = { Text("বুকমার্ককৃত বিষয় (${bookmarkedTopics.size})", fontSize = 12.sp) }
            )
            FilterChip(
                selected = subTab == 1,
                onClick = { subTab = 1 },
                label = { Text("সংরক্ষিত আয়াত (${bookmarkedAyahs.size})", fontSize = 12.sp) }
            )
        }

        if (subTab == 0) {
            if (bookmarkedTopics.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "⭐", fontSize = 34.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("কোনো বিষয় বুকমার্ক করা নেই", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("যেকোনো বিষয়ের পাশে থাকা বুকমার্ক আইকনে চাপ দিয়ে সংরক্ষণ করুন।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(bookmarkedTopics, key = { it.id }) { topic ->
                        TopicListItemCard(
                            topic = topic,
                            isBookmarked = true,
                            onClick = { onOpenTopic(topic) }
                        )
                    }
                }
            }
        } else {
            if (bookmarkedAyahs.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔖", fontSize = 34.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("কোনো আয়াত সংরক্ষিত নেই", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("যেকোনো আয়াতের বুকমার্ক আইকনে চাপ দিয়ে সহজে সংরক্ষণ করুন।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookmarkedAyahs, key = { "${it.surahNumber}_${it.ayahNumber}" }) { ayah ->
                        TopicAyahCard(
                            ayah = ayah,
                            isBookmarked = true,
                            onToggleBookmark = { repository.toggleAyahBookmark(ayah.surahNumber, ayah.ayahNumber) },
                            onOpenInQuran = onOpenInQuran,
                            onOpenContextView = { /* handled */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TopicListItemCard(
    topic: QuranTopic,
    isBookmarked: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = IslamicGreen.copy(alpha = 0.1f),
                modifier = Modifier.size(42.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = topic.iconEmoji ?: "📖", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = topic.nameBn,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (isBookmarked) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = topic.descriptionBn,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${topic.totalAyahs}টি আয়াত • ${topic.distinctSurahCount} সূরা",
                    fontSize = 11.5.sp,
                    color = IslamicGreen,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
        }
    }
}
