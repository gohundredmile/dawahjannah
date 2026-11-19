package com.example.ui.screens.quran

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TipsAndUpdates
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAyahSearchHit
import com.example.util.QuranFullSearchResult
import com.example.util.QuranFullTextSearchEngine
import com.example.util.QuranSearchMatchType
import com.example.util.QuranSearchRevelationFilter
import com.example.util.QuranSearchSortMode

private data class QuickTopicCategory(
    val titleBn: String,
    val emoji: String,
    val accent: Color,
    val topics: List<String>
)

private val quickTopicCategories = listOf(
    QuickTopicCategory(
        titleBn = "ইবাদত ও আমল",
        emoji = "🕌",
        accent = Color(0xFF047857),
        topics = listOf("নামাজ", "রোজা", "যাকাত", "হজ", "দোয়া", "দান", "জিকির", "কুরআন")
    ),
    QuickTopicCategory(
        titleBn = "অন্তর, ধৈর্য ও প্রশান্তি",
        emoji = "🌿",
        accent = Color(0xFF0D9488),
        topics = listOf("ধৈর্য", "ক্ষমা ও তওবা", "তাওয়াক্কুল", "হতাশা", "শান্তি", "কৃতজ্ঞতা", "রহমত", "রোগ ও আরোগ্য")
    ),
    QuickTopicCategory(
        titleBn = "পরিবার, চরিত্র ও সমাজ",
        emoji = "🤝",
        accent = Color(0xFFD97706),
        topics = listOf("পিতামাতা", "বিবাহ", "সন্তান", "এতিম", "ন্যায়বিচার", "সত্য", "অহংকার", "গীবত")
    ),
    QuickTopicCategory(
        titleBn = "রিযিক, হালাল-হারাম ও আখিরাত",
        emoji = "✨",
        accent = Color(0xFF4338CA),
        topics = listOf("রিজিক", "সুদ", "হালাল", "জান্নাত", "জাহান্নাম", "মৃত্যু", "কিয়ামত", "ফেরেশতা")
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchDiscoveryEmptyState(
    recentSearches: List<String>,
    onSelectTopic: (String) -> Unit,
    onClearRecentSearches: () -> Unit,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Recent Searches
        if (recentSearches.isNotEmpty()) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "সাম্প্রতিক অনুসন্ধান",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "মুছুন",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.error
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onClearRecentSearches() }
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        recentSearches.forEach { query ->
                            Surface(
                                onClick = { onSelectTopic(query) },
                                shape = RoundedCornerShape(20.dp),
                                color = IslamicGreen.copy(alpha = if (isDark) 0.16f else 0.09f),
                                border = BorderStroke(0.8.dp, IslamicGreen.copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = IslamicGreen,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        text = query,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = banglaFont,
                                            fontWeight = FontWeight.SemiBold,
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

        // Quick Search Tips Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isDark) Color(0xFF0C241C) else Color(0xFFF0FDF4),
            border = BorderStroke(1.dp, IslamicGreen.copy(alpha = if (isDark) 0.45f else 0.28f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = CircleShape,
                    color = IslamicGold.copy(alpha = 0.2f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.TipsAndUpdates,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "বাংলায় যেকোনো বিষয়, শব্দ বা বাক্যাংশ খুঁজুন",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            color = if (isDark) Color(0xFF6EE7B7) else Color(0xFF065F46)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "সমগ্র কুরআনের ১১৪ সূরা (৬,২৩৬ আয়াত) থেকে মুহূর্তেই সংশ্লিষ্ট আয়াত খুঁজে পাবেন। যেমন: “নামাজ”, “ধৈর্য ধারণ”, “পিতা-মাতার প্রতি”, “রিজিক”, “রাব্বানা” অথবা সরাসরি আয়াত নম্বর “২:২৫৫”।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = banglaFont,
                            lineHeight = 19.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }

        // Categorized Bangla Quick Topics
        Text(
            text = "জনপ্রিয় বিষয়ভিত্তিক দ্রুত অনুসন্ধান",
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                fontFamily = banglaFont,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(top = 2.dp, start = 2.dp)
        )

        quickTopicCategories.forEach { category ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(0.8.dp, category.accent.copy(alpha = if (isDark) 0.35f else 0.22f)),
                tonalElevation = 1.dp
            ) {
                Column(modifier = Modifier.padding(13.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = category.emoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(7.dp))
                        Text(
                            text = category.titleBn,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                color = if (isDark) category.accent.copy(alpha = 0.95f) else category.accent
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(9.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        category.topics.forEach { topic ->
                            Surface(
                                onClick = { onSelectTopic(topic) },
                                shape = RoundedCornerShape(12.dp),
                                color = category.accent.copy(alpha = if (isDark) 0.16f else 0.08f),
                                border = BorderStroke(0.8.dp, category.accent.copy(alpha = 0.3f)),
                                modifier = Modifier.testTag("quick_topic_$topic")
                            ) {
                                Text(
                                    text = topic,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurface
                                    ),
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
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
fun SearchResultsSummaryBar(
    result: QuranFullSearchResult,
    revelationFilter: QuranSearchRevelationFilter,
    selectedSurahFilter: Int?,
    sortMode: QuranSearchSortMode,
    includeTafsir: Boolean,
    onRevelationFilterChange: (QuranSearchRevelationFilter) -> Unit,
    onSurahFilterChange: (Int?) -> Unit,
    onToggleSortMode: () -> Unit,
    onToggleIncludeTafsir: () -> Unit,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val isDark = isSystemInDarkTheme()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Result Count & Timing Strip
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = if (isDark) Color(0xFF0E2920) else Color(0xFFECFDF5),
            border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (result.totalMatches > 0) {
                                "${BanglaNumberUtils.toBanglaDigits(result.matchedSurahCount)}টি সূরায় ${BanglaNumberUtils.toBanglaDigits(result.totalMatches)}টি সংশ্লিষ্ট আয়াত পাওয়া গেছে"
                            } else {
                                "কোনো সরাসরি আয়াত পাওয়া যায়নি"
                            },
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                fontSize = 13.5.sp,
                                color = if (isDark) Color(0xFF6EE7B7) else Color(0xFF065F46)
                            )
                        )
                        Text(
                            text = "সমগ্র ১১৪ সূরা অনুসন্ধান • সময়: ${BanglaNumberUtils.toBanglaDigits(result.searchTimeMs.toInt())} মিলি-সেকেন্ড",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = banglaFont,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    // Sort Toggle Chip
                    Surface(
                        onClick = onToggleSortMode,
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.55f)),
                        modifier = Modifier.testTag("toggle_sort_mode")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Sort,
                                contentDescription = "ক্রম পরিবর্তন",
                                tint = IslamicGold,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = sortMode.labelBn,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }

                // Expanded Smart Synonyms Banner
                if (result.expandedTermsUsed.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "স্মার্ট কুরআনিক শব্দমিল: ${result.expandedTermsUsed.take(4).joinToString(" • ")}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.SemiBold,
                                color = IslamicGold
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        // Filter Chips: Revelation Type + Deep Tafsir Search
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(7.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(QuranSearchRevelationFilter.entries) { filter ->
                val isSelected = revelationFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { onRevelationFilterChange(filter) },
                    label = {
                        Text(
                            text = filter.labelBn,
                            fontFamily = banglaFont,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicGreen.copy(alpha = 0.16f),
                        selectedLabelColor = if (isDark) Color(0xFF6EE7B7) else IslamicGreen
                    ),
                    modifier = Modifier.testTag("filter_revelation_${filter.name.lowercase()}")
                )
            }

            item {
                FilterChip(
                    selected = includeTafsir,
                    onClick = onToggleIncludeTafsir,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp)
                        )
                    },
                    label = {
                        Text(
                            text = "তাফসীরসহ খুঁজুন",
                            fontFamily = banglaFont,
                            fontSize = 12.sp,
                            fontWeight = if (includeTafsir) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicGold.copy(alpha = 0.2f),
                        selectedLabelColor = if (isDark) Color(0xFFFDE68A) else Color(0xFF92400E),
                        selectedLeadingIconColor = IslamicGold
                    ),
                    modifier = Modifier.testTag("filter_include_tafsir")
                )
            }
        }

        // Dynamic Surah Distribution Chips (when matches span multiple surahs)
        if (result.surahDistribution.size > 1) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(7.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                item(key = "surah_all") {
                    val allSelected = selectedSurahFilter == null
                    Surface(
                        onClick = { onSurahFilterChange(null) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (allSelected) IslamicGreen else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                        border = BorderStroke(
                            0.8.dp,
                            if (allSelected) IslamicGreen else MaterialTheme.colorScheme.outlineVariant
                        )
                    ) {
                        Text(
                            text = "সব সূরা (${BanglaNumberUtils.toBanglaDigits(result.totalMatches)})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Bold,
                                color = if (allSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                items(result.surahDistribution.take(25), key = { it.surahNumber }) { summary ->
                    val isSelected = selectedSurahFilter == summary.surahNumber
                    Surface(
                        onClick = {
                            onSurahFilterChange(if (isSelected) null else summary.surahNumber)
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.surface,
                        border = BorderStroke(
                            0.9.dp,
                            if (isSelected) IslamicGreen else IslamicGold.copy(alpha = 0.4f)
                        )
                    ) {
                        Text(
                            text = "${BanglaNumberUtils.toBanglaDigits(summary.surahNumber)}. ${summary.surahNameBn} (${BanglaNumberUtils.toBanglaDigits(summary.matchCount)})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = banglaFont,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun QuranSearchResultCard(
    hit: QuranAyahSearchHit,
    query: String,
    isBookmarked: Boolean,
    isAudioPlaying: Boolean,
    isTafsirExpanded: Boolean,
    loadedTafsirText: String?,
    isTafsirLoading: Boolean,
    showPronunciation: Boolean,
    onPlayAudio: () -> Unit,
    onToggleBookmark: () -> Unit,
    onToggleTafsir: () -> Unit,
    onOpenInQuran: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val isDark = isSystemInDarkTheme()

    val highlightBg = if (isDark) Color(0xFF92400E).copy(alpha = 0.55f) else Color(0xFFFEF08A)
    val highlightTextColor = if (isDark) Color(0xFFFEF3C7) else Color(0xFF713F12)

    val highlightedTranslation = remember(hit.ayah.translationBn, query, hit.matchedTerms, isDark) {
        val text = hit.ayah.translationBn
        val ranges = QuranFullTextSearchEngine.findHighlightRanges(text, query, hit.matchedTerms)
        buildAnnotatedString {
            append(text)
            ranges.forEach { range ->
                addStyle(
                    style = SpanStyle(
                        background = highlightBg,
                        color = highlightTextColor,
                        fontWeight = FontWeight.Bold
                    ),
                    start = range.first,
                    end = (range.last + 1).coerceAtMost(text.length)
                )
            }
        }
    }

    val matchBadgeColor = when (hit.matchType) {
        QuranSearchMatchType.DIRECT_REFERENCE -> Color(0xFF2563EB)
        QuranSearchMatchType.EXACT_PHRASE -> IslamicGreen
        QuranSearchMatchType.ALL_WORDS -> Color(0xFF0D9488)
        QuranSearchMatchType.SMART_TOPIC -> IslamicGold
        QuranSearchMatchType.TAFSIR_MATCH -> Color(0xFF7C3AED)
        else -> MaterialTheme.colorScheme.secondary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .animateContentSize()
            .testTag("search_ayah_card_${hit.surah.number}_${hit.ayah.ayahNumber}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            width = if (isAudioPlaying) 1.5.dp else 0.9.dp,
            color = if (isAudioPlaying) IslamicGold else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.55f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Card Top Header: Surah + Ayah Number + Match Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = IslamicGreen.copy(alpha = if (isDark) 0.22f else 0.11f),
                        border = BorderStroke(0.8.dp, IslamicGreen.copy(alpha = 0.35f))
                    ) {
                        Text(
                            text = "${BanglaNumberUtils.toBanglaDigits(hit.surah.number)}:${BanglaNumberUtils.toBanglaDigits(hit.ayah.ayahNumber)}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontFamily = banglaFont,
                                color = if (isDark) Color(0xFF6EE7B7) else IslamicGreen
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "সূরা ${hit.surah.nameBn}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "${hit.surah.meaningBn} • ${hit.surah.revelationType}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = banglaFont,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 10.5.sp
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = matchBadgeColor.copy(alpha = if (isDark) 0.2f else 0.11f),
                    border = BorderStroke(0.7.dp, matchBadgeColor.copy(alpha = 0.4f))
                ) {
                    val label = if (hit.matchType == QuranSearchMatchType.SMART_TOPIC && hit.matchedTerms.isNotEmpty()) {
                        "মিল: ${hit.matchedTerms.first()}"
                    } else {
                        hit.matchType.labelBn
                    }
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            color = if (isDark) matchBadgeColor.copy(alpha = 0.95f) else matchBadgeColor
                        ),
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic Uthmani Text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                IslamicGreen.copy(alpha = if (isDark) 0.12f else 0.04f),
                                IslamicGold.copy(alpha = if (isDark) 0.10f else 0.05f)
                            )
                        )
                    )
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = hit.ayah.arabicText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontFamily = arabicFont,
                        fontSize = 23.sp,
                        lineHeight = 40.sp,
                        textDirection = TextDirection.Rtl,
                        textAlign = TextAlign.Right,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Optional Bengali Pronunciation
            if (showPronunciation && hit.ayah.pronunciationBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "উচ্চারণ: ${hit.ayah.pronunciationBn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = banglaFont,
                        fontSize = 12.5.sp,
                        lineHeight = 19.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Highlighted Bangla Translation
            Text(
                text = highlightedTranslation,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = banglaFont,
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Tafsir Excerpt Preview (if matched via Deep Tafsir Search)
            if (!hit.tafsirMatchExcerpt.isNullOrBlank() && !isTafsirExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF7C3AED).copy(alpha = if (isDark) 0.14f else 0.07f),
                    border = BorderStroke(0.7.dp, Color(0xFF7C3AED).copy(alpha = 0.3f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleTafsir() }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "তাফসীর অংশবিশেষ:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C3AED)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = hit.tafsirMatchExcerpt,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = banglaFont,
                                lineHeight = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            // Expandable Full Authentic Tafsir
            AnimatedVisibility(
                visible = isTafsirExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isDark) Color(0xFF1F1A12) else Color(0xFFFFFBEB),
                    border = BorderStroke(0.9.dp, IslamicGold.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "প্রামাণ্য তাফসীর (ড. আবু বকর যাকারিয়া)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                            )
                            IconButton(
                                onClick = onToggleTafsir,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "তাফসীর বন্ধ করুন",
                                    tint = IslamicGold,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        if (isTafsirLoading) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = IslamicGold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "তাফসীর লোড হচ্ছে...",
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont)
                                )
                            }
                        } else {
                            Text(
                                text = loadedTafsirText ?: "তাফসীর লোড করা যাচ্ছে না।",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = banglaFont,
                                    lineHeight = 21.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.35f))
            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Interactive Action Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Left Action Icons: Audio, Bookmark, Copy, Share
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = onPlayAudio,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("play_ayah_${hit.surah.number}_${hit.ayah.ayahNumber}")
                    ) {
                        Icon(
                            imageVector = if (isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isAudioPlaying) "অডিও বিরতি" else "আয়াত তিলাওয়াত শুনুন",
                            tint = if (isAudioPlaying) IslamicGold else IslamicGreen,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier
                            .size(44.dp)
                            .testTag("bookmark_ayah_${hit.surah.number}_${hit.ayah.ayahNumber}")
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক",
                            tint = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = onCopy,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "কপি করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp)
                        )
                    }

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }

                // Right Chips: Tafsir + Read in Quran
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        onClick = onToggleTafsir,
                        shape = RoundedCornerShape(10.dp),
                        color = IslamicGold.copy(alpha = if (isTafsirExpanded) 0.22f else 0.12f),
                        border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .heightIn(min = 36.dp)
                            .testTag("tafsir_ayah_${hit.surah.number}_${hit.ayah.ayahNumber}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "তাফসীর",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFFFDE68A) else Color(0xFF92400E)
                                )
                            )
                            Icon(
                                imageVector = if (isTafsirExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Surface(
                        onClick = onOpenInQuran,
                        shape = RoundedCornerShape(10.dp),
                        color = IslamicGreen,
                        modifier = Modifier
                            .heightIn(min = 36.dp)
                            .testTag("open_in_quran_${hit.surah.number}_${hit.ayah.ayahNumber}")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "কুরআনে পড়ুন",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
