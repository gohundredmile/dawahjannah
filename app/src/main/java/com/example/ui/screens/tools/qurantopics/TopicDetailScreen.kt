package com.example.ui.screens.tools.qurantopics

import android.content.Intent
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.data.datasource.qurantopics.QuranTopicCatalog
import com.example.data.model.qurantopics.QuranTopic
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance
import com.example.data.repository.QuranTopicRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TopicDetailScreen(
    topic: QuranTopic,
    repository: QuranTopicRepository,
    onNavigateBack: () -> Unit,
    onOpenTopic: (QuranTopic) -> Unit,
    onOpenInQuran: (surahNumber: Int) -> Unit
) {
    BackHandler { onNavigateBack() }

    val context = LocalContext.current
    val bookmarkedTopics by repository.bookmarkedTopics.collectAsState()
    val bookmarkedAyahs by repository.bookmarkedAyahs.collectAsState()

    val isTopicBookmarked = bookmarkedTopics.contains(topic.id)

    var searchQuery by remember { mutableStateOf("") }
    var selectedRelevanceFilter by remember { mutableStateOf<TopicRelevance?>(null) }
    var selectedSurahFilter by remember { mutableStateOf<Int?>(null) }
    var activeContextAyah by remember { mutableStateOf<TopicAyah?>(null) }

    // Filtered ayahs
    val filteredAyahs = remember(topic, searchQuery, selectedRelevanceFilter, selectedSurahFilter) {
        topic.ayahs.filter { ayah ->
            val matchQuery = searchQuery.isBlank() ||
                ayah.translationBn.contains(searchQuery, ignoreCase = true) ||
                ayah.arabicText.contains(searchQuery, ignoreCase = true) ||
                ayah.surahNameBn.contains(searchQuery, ignoreCase = true)

            val matchRelevance = selectedRelevanceFilter == null || ayah.relevance == selectedRelevanceFilter
            val matchSurah = selectedSurahFilter == null || ayah.surahNumber == selectedSurahFilter

            matchQuery && matchRelevance && matchSurah
        }
    }

    val relatedTopics = remember(topic) {
        topic.relatedTopicIds.mapNotNull { QuranTopicCatalog.getTopicById(it) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topic.nameBn,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = topic.nameEn,
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
                actions = {
                    IconButton(onClick = { repository.toggleTopicBookmark(topic.id) }) {
                        Icon(
                            imageVector = if (isTopicBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক বিষয়",
                            tint = if (isTopicBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurface
                        )
                    }
                    IconButton(onClick = {
                        val shareText = buildString {
                            append(topic.nameBn).append(" (").append(topic.nameEn).append(")\n\n")
                            append(topic.descriptionBn).append("\n\n")
                            append("মোট আয়াত: ").append(topic.totalAyahs).append(" টি (").append(topic.distinctSurahCount).append(" সূরা)\n\n")
                            topic.ayahs.take(2).forEach {
                                append("• ").append(it.surahNameBn).append(" [").append(it.surahNumber).append(":").append(it.ayahNumber).append("]\n")
                                append(it.translationBn).append("\n\n")
                            }
                            append("দা'ওয়াহ টু জান্নাহ — বিষয় ভিত্তিক কোরআনের আয়াত")
                        }
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, topic.nameBn)
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "শেয়ার বিষয়"))
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "শেয়ার বিষয়")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Topic Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = IslamicGreen.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = IslamicGreen.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.3f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = topic.iconEmoji ?: "📖",
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "বিষয় পরিচিতি",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGreen
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = "${topic.totalAyahs}টি আয়াত • ${topic.distinctSurahCount} সূরা",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = topic.nameBn,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        if (!topic.nameAr.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = topic.nameAr,
                                fontSize = 16.sp,
                                color = IslamicGold,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = topic.descriptionBn,
                            fontSize = 13.5.sp,
                            lineHeight = 21.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // In-Topic Search & Filters
            item {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("এই বিষয়ের আয়াত বা অনুবাদে খুঁজুন...", fontSize = 13.5.sp) },
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
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = IslamicGreen,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    // Relevance Filter Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            selected = selectedRelevanceFilter == null && selectedSurahFilter == null,
                            onClick = {
                                selectedRelevanceFilter = null
                                selectedSurahFilter = null
                            },
                            label = { Text("সব আয়াত (${topic.totalAyahs})", fontSize = 12.sp) }
                        )

                        FilterChip(
                            selected = selectedRelevanceFilter == TopicRelevance.DIRECT,
                            onClick = {
                                selectedRelevanceFilter = if (selectedRelevanceFilter == TopicRelevance.DIRECT) null else TopicRelevance.DIRECT
                            },
                            label = { Text("সরাসরি সম্পর্কিত", fontSize = 12.sp) }
                        )

                        FilterChip(
                            selected = selectedRelevanceFilter == TopicRelevance.SUPPORTING,
                            onClick = {
                                selectedRelevanceFilter = if (selectedRelevanceFilter == TopicRelevance.SUPPORTING) null else TopicRelevance.SUPPORTING
                            },
                            label = { Text("সহায়ক আয়াত", fontSize = 12.sp) }
                        )

                        // Surah quick filter chips
                        topic.distinctSurahs.forEach { (surahNum, surahName) ->
                            FilterChip(
                                selected = selectedSurahFilter == surahNum,
                                onClick = {
                                    selectedSurahFilter = if (selectedSurahFilter == surahNum) null else surahNum
                                },
                                label = { Text(surahName, fontSize = 12.sp) }
                            )
                        }
                    }
                }
            }

            // Empty state if search has no results
            if (filteredAyahs.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 30.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 32.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "এই ফিল্টারের জন্য কোনো আয়াত পাওয়া যায়নি",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "অনুগ্রহ করে অন্য শব্দ দিয়ে অনুসন্ধান করুন অথবা ফিল্টার রিসেট করুন।",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            } else {
                items(filteredAyahs, key = { "${it.surahNumber}_${it.ayahNumber}" }) { ayah ->
                    val isAyahSaved = bookmarkedAyahs.contains("${ayah.surahNumber}_${ayah.ayahNumber}")
                    TopicAyahCard(
                        ayah = ayah,
                        isBookmarked = isAyahSaved,
                        onToggleBookmark = { repository.toggleAyahBookmark(ayah.surahNumber, ayah.ayahNumber) },
                        onOpenInQuran = onOpenInQuran,
                        onOpenContextView = { activeContextAyah = it }
                    )
                }
            }

            // Related Topics Section
            if (relatedTopics.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "সম্পর্কিত অন্যান্য বিষয়",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreen
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        relatedTopics.forEach { related ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                                onClick = { onOpenTopic(related) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = related.iconEmoji ?: "🔗", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = related.nameBn,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Context View Bottom Sheet
    activeContextAyah?.let { contextAyah ->
        AyahContextBottomSheet(
            ayah = contextAyah,
            onDismiss = { activeContextAyah = null },
            onOpenInQuran = onOpenInQuran
        )
    }
}
