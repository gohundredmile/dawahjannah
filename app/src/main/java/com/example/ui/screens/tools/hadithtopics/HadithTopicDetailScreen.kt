package com.example.ui.screens.tools.hadithtopics

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.hadithtopics.HadithTopic
import com.example.data.repository.HadithTopicRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HadithTopicDetailScreen(
    topic: HadithTopic,
    repository: HadithTopicRepository,
    onNavigateBack: () -> Unit,
    onOpenTopic: (HadithTopic) -> Unit,
    onOpenInHadithCollection: ((bookSlug: String) -> Unit)? = null,
    onOpenQuranSurah: ((surahNumber: Int) -> Unit)? = null
) {
    BackHandler { onNavigateBack() }

    val context = LocalContext.current
    val bookmarkedTopics by repository.bookmarkedTopics.collectAsState()
    val bookmarkedHadiths by repository.bookmarkedHadiths.collectAsState()
    val isTopicBookmarked = bookmarkedTopics.contains(topic.id)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = topic.nameBn,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            maxLines = 1
                        )
                        Text(
                            text = "${topic.totalHadiths}টি হাদিস • বিষয় ভিত্তিক হাদিস",
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
                    IconButton(onClick = { repository.toggleTopicBookmark(topic.id) }) {
                        Icon(
                            imageVector = if (isTopicBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক",
                            tint = if (isTopicBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareBody = buildString {
                                append("বিষয়: ").append(topic.nameBn).append("\n\n")
                                append(topic.descriptionBn).append("\n\n")
                                topic.hadiths.forEachIndexed { idx, hadith ->
                                    append("${idx + 1}. ").append(hadith.banglaText).append("\n")
                                    append("— [").append(hadith.referenceText).append(" • ").append(hadith.gradeBn).append("]\n\n")
                                }
                                append("দা'ওয়াহ টু জান্নাহ — বিষয় ভিত্তিক হাদিস")
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareBody)
                            }
                            context.startActivity(Intent.createChooser(intent, "শেয়ার করুন"))
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "শেয়ার করুন")
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
            // Header Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = IslamicGreen.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            topic.iconEmoji?.let { emoji ->
                                Text(text = emoji, fontSize = 28.sp)
                            }
                            Column {
                                Text(
                                    text = topic.nameBn,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGreen
                                    )
                                )
                                Text(
                                    text = topic.nameEn,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = topic.descriptionBn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                lineHeight = 22.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Stats pills
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CollectionsBookmark,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = IslamicGreen
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${topic.totalHadiths}টি হাদিস",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surface
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${topic.distinctBooksCount}টি প্রামাণ্য গ্রন্থ",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Hadith list items
            items(topic.hadiths, key = { it.hadithId }) { hadith ->
                val isHadithBookmarked = bookmarkedHadiths.contains(hadith.hadithId)
                TopicHadithCard(
                    hadith = hadith,
                    isBookmarked = isHadithBookmarked,
                    onToggleBookmark = { repository.toggleHadithBookmark(hadith.hadithId) },
                    onOpenInHadithCollection = onOpenInHadithCollection,
                    onOpenQuranSurah = onOpenQuranSurah
                )
            }

            // Related topics section
            if (topic.relatedTopicIds.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "সম্পর্কিত অন্যান্য বিষয়:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            topic.relatedTopicIds.forEach { relId ->
                                val relTopic = repository.getTopicById(relId)
                                if (relTopic != null) {
                                    Surface(
                                        onClick = { onOpenTopic(relTopic) },
                                        shape = RoundedCornerShape(10.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                                    ) {
                                        Text(
                                            text = "${relTopic.iconEmoji ?: "📌"} ${relTopic.nameBn}",
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
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
}
