package com.example.ui.screens.hadith

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.HadithBookEntity
import com.example.data.local.entity.HadithChapterEntity
import com.example.data.local.entity.HadithEntity
import com.example.data.repository.HadithRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.util.BanglaNumberUtils
import kotlinx.coroutines.launch

enum class HadithTabCategory(val titleBn: String) {
    SIHAH_SITTA("সিহাহ্ সিত্তাহ (৬ গ্রন্থ)"),
    PRIMARY_CORE("প্রাইমারি কোর হাদিস"),
    BOOKMARKS("বুকমার্ককৃত")
}

@Composable
fun HadithMainScreen(
    hadithRepository: HadithRepository,
    initialBookSlug: String? = null,
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var selectedBookSlug by remember { mutableStateOf(initialBookSlug) }
    var selectedTab by remember { mutableStateOf(HadithTabCategory.SIHAH_SITTA) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }

    // Text scaling state
    var arabicFontSize by remember { mutableFloatStateOf(20f) }
    var banglaFontSize by remember { mutableFloatStateOf(14.5f) }
    var showFontSizeDialog by remember { mutableStateOf(false) }

    // Initialize DB on launch
    LaunchedEffect(Unit) {
        hadithRepository.initializeDatabaseIfNeeded()
    }

    val allBooks by hadithRepository.getAllBooks().collectAsState(initial = emptyList())
    val sihahBooks by hadithRepository.getSihahSittaBooks().collectAsState(initial = emptyList())
    val primaryBooks by hadithRepository.getPrimaryCoreBooks().collectAsState(initial = emptyList())
    val bookmarkedHadiths by hadithRepository.getBookmarkedHadiths().collectAsState(initial = emptyList())

    // If searching across all hadiths
    val searchResults by hadithRepository.searchHadiths(searchQuery).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(WindowInsets.statusBars)
    ) {
        // Top App Bar
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            if (selectedBookSlug != null) {
                                selectedBookSlug = null
                            } else {
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(listOf(IslamicGreen, IslamicGold))
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (selectedBookSlug != null) {
                                allBooks.find { it.slug == selectedBookSlug }?.nameBn ?: "হাদীস বিস্তারিত"
                            } else {
                                "সহীহ হাদীস সম্ভার"
                            },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = IslamicGreen,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "হাদিসবিডি (HadithBD) / IRD ফাউন্ডেশন",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = IslamicGreen
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = { isSearchExpanded = !isSearchExpanded },
                        modifier = Modifier.testTag("btn_hadith_search")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "অনুসন্ধান",
                            tint = if (isSearchExpanded || searchQuery.isNotBlank()) IslamicGold else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = { showFontSizeDialog = true },
                        modifier = Modifier.testTag("btn_hadith_font_size")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "ফন্ট সাইজ",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                // Expandable Search Bar
                AnimatedVisibility(visible = isSearchExpanded) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "হাদীসের বাংলা বা আরবি শব্দ, রাবী, নম্বর দিয়ে খুঁজুন...",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, contentDescription = "Clear")
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_hadith_search")
                        )
                    }
                }
            }
        }

        // Font Size Dialog
        if (showFontSizeDialog) {
            AlertDialog(
                onDismissRequest = { showFontSizeDialog = false },
                title = { Text("হাদীস ফন্ট সাইজ নিয়ন্ত্রণ", fontWeight = FontWeight.Bold) },
                text = {
                    Column {
                        Text("আরবি লেখার সাইজ: ${arabicFontSize.toInt()} sp", style = MaterialTheme.typography.bodySmall)
                        Slider(
                            value = arabicFontSize,
                            onValueChange = { arabicFontSize = it },
                            valueRange = 16f..32f,
                            steps = 8
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text("বাংলা অনুবাদের সাইজ: ${banglaFontSize.toInt()} sp", style = MaterialTheme.typography.bodySmall)
                        Slider(
                            value = banglaFontSize,
                            onValueChange = { banglaFontSize = it },
                            valueRange = 12f..24f,
                            steps = 6
                        )
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFontSizeDialog = false }) {
                        Text("ঠিক আছে", color = IslamicGreen, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        // Main Content Switch: Search Results vs Book Detail vs Book Catalog
        if (searchQuery.isNotBlank()) {
            // Live Search Results
            HadithSearchResultsView(
                query = searchQuery,
                results = searchResults,
                arabicFontSize = arabicFontSize,
                banglaFontSize = banglaFontSize,
                onToggleBookmark = { hadith ->
                    scope.launch { hadithRepository.toggleBookmark(hadith) }
                }
            )
        } else if (selectedBookSlug != null) {
            // Selected Hadith Book View
            val currentBook = allBooks.find { it.slug == selectedBookSlug }
            if (currentBook != null) {
                HadithBookDetailView(
                    book = currentBook,
                    allBooks = allBooks,
                    onSelectBook = { selectedBookSlug = it.slug },
                    hadithRepository = hadithRepository,
                    arabicFontSize = arabicFontSize,
                    banglaFontSize = banglaFontSize,
                    onToggleBookmark = { hadith ->
                        scope.launch { hadithRepository.toggleBookmark(hadith) }
                    },
                    onBackToCatalog = { selectedBookSlug = null }
                )
            }
        } else {
            // Catalog Index Screen
            HadithCatalogView(
                selectedTab = selectedTab,
                onSelectTab = { selectedTab = it },
                sihahBooks = sihahBooks,
                primaryBooks = primaryBooks,
                bookmarkedHadiths = bookmarkedHadiths,
                onSelectBook = { slug -> selectedBookSlug = slug },
                hadithRepository = hadithRepository,
                arabicFontSize = arabicFontSize,
                banglaFontSize = banglaFontSize
            )
        }
    }
}

@Composable
private fun HadithCatalogView(
    selectedTab: HadithTabCategory,
    onSelectTab: (HadithTabCategory) -> Unit,
    sihahBooks: List<HadithBookEntity>,
    primaryBooks: List<HadithBookEntity>,
    bookmarkedHadiths: List<HadithEntity>,
    onSelectBook: (String) -> Unit,
    hadithRepository: HadithRepository,
    arabicFontSize: Float,
    banglaFontSize: Float
) {
    val scope = rememberCoroutineScope()
    var dailyHadith by remember { mutableStateOf<HadithEntity?>(null) }

    LaunchedEffect(Unit) {
        dailyHadith = hadithRepository.getRandomDailyHadith()
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Featured Daily Hadith Card
        item {
            dailyHadith?.let { hadith ->
                DailyHadithFeaturedCard(
                    hadith = hadith,
                    arabicFontSize = arabicFontSize,
                    banglaFontSize = banglaFontSize,
                    onToggleBookmark = {
                        scope.launch {
                            hadithRepository.toggleBookmark(hadith)
                            dailyHadith = hadith.copy(isBookmarked = !hadith.isBookmarked)
                        }
                    }
                )
            }
        }

        // Category Pills (সিহাহ্ সিত্তাহ | প্রাইমারি কোর হাদিস | বুকমার্ক)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HadithTabCategory.entries.forEach { tab ->
                    val isSelected = tab == selectedTab
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectTab(tab) },
                        label = {
                            Text(
                                text = when (tab) {
                                    HadithTabCategory.SIHAH_SITTA -> "সিহাহ্ সিত্তাহ (${BanglaNumberUtils.toBanglaDigits(sihahBooks.size)})"
                                    HadithTabCategory.PRIMARY_CORE -> "প্রাইমারি কোর হাদিস (${BanglaNumberUtils.toBanglaDigits(primaryBooks.size)})"
                                    HadithTabCategory.BOOKMARKS -> "বুকমার্ক (${BanglaNumberUtils.toBanglaDigits(bookmarkedHadiths.size)})"
                                },
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = IslamicGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Book Cards or Bookmarks
        when (selectedTab) {
            HadithTabCategory.SIHAH_SITTA -> {
                items(sihahBooks, key = { it.slug }) { book ->
                    HadithBookCard(book = book, onClick = { onSelectBook(book.slug) })
                }
            }
            HadithTabCategory.PRIMARY_CORE -> {
                items(primaryBooks, key = { it.slug }) { book ->
                    HadithBookCard(book = book, onClick = { onSelectBook(book.slug) })
                }
            }
            HadithTabCategory.BOOKMARKS -> {
                if (bookmarkedHadiths.isEmpty()) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(40.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "কোনো হাদিস বুকমার্ক করা নেই",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "যেকোনো হাদিসের বুকমার্ক আইকনে চাপলে এখানে দ্রুত খুঁজে পাবেন।",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                } else {
                    items(bookmarkedHadiths, key = { it.id }) { hadith ->
                        HadithItemCard(
                            hadith = hadith,
                            arabicFontSize = arabicFontSize,
                            banglaFontSize = banglaFontSize,
                            onToggleBookmark = {
                                scope.launch { hadithRepository.toggleBookmark(hadith) }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyHadithFeaturedCard(
    hadith: HadithEntity,
    arabicFontSize: Float,
    banglaFontSize: Float,
    onToggleBookmark: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = IslamicGreen.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = IslamicGold.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "⭐ আজকের নির্বাচিত হাদীস",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    color = IslamicGreen.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = hadith.gradeBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic text snippet
            Text(
                text = hadith.arabicText,
                fontSize = (arabicFontSize * 0.9f).sp,
                fontWeight = FontWeight.Normal,
                lineHeight = (arabicFontSize * 1.5f).sp,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bengali Translation
            Text(
                text = hadith.banglaText,
                fontSize = banglaFontSize.sp,
                lineHeight = (banglaFontSize * 1.5f).sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Narrator
            Text(
                text = "— ${hadith.narratorBn}",
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = IslamicGreen.copy(alpha = 0.15f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        val textToCopy = "${hadith.banglaText}\n\n[${hadith.arabicText}]\n\nবর্ণনাকারী: ${hadith.narratorBn}\nমানদণ্ড: ${hadith.gradeBn}\nউৎস: ${hadith.sourceBn}"
                        clipboardManager.setText(AnnotatedString(textToCopy))
                        Toast.makeText(context, "হাদিসটি কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${hadith.banglaText}\n\n[${hadith.arabicText}]\n\nবর্ণনাকারী: ${hadith.narratorBn}\nমানদণ্ড: ${hadith.gradeBn}\nউৎস: ${hadith.sourceBn}\n\n— দা'ওয়াহ টু জান্নাহ্ অ্যাপ"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "হাদিস শেয়ার করুন"))
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "শেয়ার",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(onClick = onToggleBookmark) {
                    Icon(
                        imageVector = if (hadith.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "বুকমার্ক",
                        tint = if (hadith.isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun HadithBookCard(
    book: HadithBookEntity,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Book Icon Circle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                IslamicGreen.copy(alpha = 0.85f),
                                IslamicGreen
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = BanglaNumberUtils.toBanglaDigits(book.orderIndex),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = book.nameBn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    if (book.isSihahSitta) {
                        Surface(
                            color = IslamicGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "সিহাহ্ সিত্তাহ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGreen
                                ),
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "${book.nameAr} • ${book.authorBn}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "মোট হাদীস: ${BanglaNumberUtils.toBanglaDigits(book.totalHadiths)} টি",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = IslamicGold
                        )
                    )
                    Text(
                        text = "•",
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = "${BanglaNumberUtils.toBanglaDigits(book.totalChapters)} টি অধ্যায়",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun HadithBookDetailView(
    book: HadithBookEntity,
    allBooks: List<HadithBookEntity>,
    onSelectBook: (HadithBookEntity) -> Unit,
    hadithRepository: HadithRepository,
    arabicFontSize: Float,
    banglaFontSize: Float,
    onToggleBookmark: (HadithEntity) -> Unit,
    onBackToCatalog: () -> Unit
) {
    var selectedChapterNumber by remember(book.slug) { mutableIntStateOf(1) }
    var showHadithJumpDialog by remember { mutableStateOf(false) }
    var showBookSwitchDialog by remember { mutableStateOf(false) }
    var showChapterPickerDialog by remember { mutableStateOf(false) }
    var isDownloadingChapter by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val chapters by hadithRepository.getChaptersForBook(book.slug).collectAsState(initial = emptyList())
    val hadithsInChapter by hadithRepository.getHadithsForChapter(book.slug, selectedChapterNumber).collectAsState(initial = emptyList())
    val fallbackHadiths by hadithRepository.getHadithsForBook(book.slug, limit = 50, offset = 0).collectAsState(initial = emptyList())

    val activeHadiths = if (hadithsInChapter.isNotEmpty()) hadithsInChapter else fallbackHadiths

    Column(modifier = Modifier.fillMaxSize()) {
        // Book Header Banner with Book Switcher Button
        Surface(
            color = IslamicGreen.copy(alpha = 0.08f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${book.nameBn} (${book.nameAr})",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Text(
                        text = book.descriptionBn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Fast Book Switcher Pill Button
                Surface(
                    onClick = { showBookSwitchDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "📚 কিতাব পরিবর্তন ▾",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Chapters Strip (if available) with Fast Chapter Switcher
        if (chapters.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // All Chapters Button
                Surface(
                    onClick = { showChapterPickerDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    border = BorderStroke(1.dp, IslamicGreen)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = IslamicGreen,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "সকল অধ্যায় (${BanglaNumberUtils.toBanglaDigits(chapters.size)}) ▾",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                chapters.forEach { chapter ->
                    val isSelected = chapter.chapterNumber == selectedChapterNumber
                    Surface(
                        onClick = {
                            selectedChapterNumber = chapter.chapterNumber
                            coroutineScope.launch { listState.scrollToItem(0) }
                        },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.surfaceVariant,
                        border = if (isSelected) BorderStroke(1.dp, IslamicGreen) else null
                    ) {
                        Text(
                            text = "${BanglaNumberUtils.toBanglaDigits(chapter.chapterNumber)}. ${chapter.titleBn}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Offline Database Status Banner & Sync Action
        Surface(
            color = IslamicGreen.copy(alpha = 0.07f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 3.dp),
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(0.6.dp, IslamicGreen.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = IslamicGreen,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = if (activeHadiths.isNotEmpty()) {
                            "অফলাইন ডাটাবেজে প্রস্তুত: ${BanglaNumberUtils.toBanglaDigits(activeHadiths.size)} টি হাদীস"
                        } else {
                            "অধ্যায় $selectedChapterNumber এর হাদীস লোড হচ্ছে..."
                        },
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            isDownloadingChapter = true
                            val res = hadithRepository.downloadChapterToOfflineDb(book.slug, selectedChapterNumber)
                            isDownloadingChapter = false
                            res.onSuccess { count ->
                                Toast.makeText(context, "অধ্যায় $selectedChapterNumber-এর $count টি হাদীস অফলাইন ডাটাবেজে সংরক্ষিত হয়েছে ✅", Toast.LENGTH_SHORT).show()
                            }.onFailure { err ->
                                Toast.makeText(context, err.message ?: "ডাউনলোড ব্যর্থ হয়েছে", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    enabled = !isDownloadingChapter,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp)
                ) {
                    Text(
                        text = if (isDownloadingChapter) "সংরক্ষণ হচ্ছে..." else "💾 অফলাইনে সেভ করুন",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreen
                    )
                }
            }
        }

        // Quick Hadith Switcher Strip
        Surface(
            color = IslamicGreen.copy(alpha = 0.08f),
            modifier = Modifier.fillMaxWidth()
        ) {
            val totalHadiths = activeHadiths.size
            val currentIndex = listState.firstVisibleItemIndex.coerceIn(0, (totalHadiths - 1).coerceAtLeast(0))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Previous Hadith Button
                TextButton(
                    onClick = {
                        if (currentIndex > 0) {
                            coroutineScope.launch {
                                listState.animateScrollToItem(currentIndex - 1)
                            }
                        }
                    },
                    enabled = currentIndex > 0,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "পূর্ববর্তী হাদীস",
                        modifier = Modifier.size(16.dp),
                        tint = if (currentIndex > 0) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "পূর্ববর্তী",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (currentIndex > 0) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                    )
                }

                // Central Hadith Quick Switch / Jump Dialog trigger
                Surface(
                    onClick = { showHadithJumpDialog = true },
                    shape = RoundedCornerShape(10.dp),
                    color = IslamicGreen,
                    shadowElevation = 1.dp
                ) {
                    val currentHadith = activeHadiths.getOrNull(currentIndex)
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (currentHadith != null) {
                                "হাদীস নং ${BanglaNumberUtils.toBanglaDigits(currentHadith.hadithNumber)} ▾"
                            } else if (totalHadiths > 0) {
                                "হাদীস নং ${BanglaNumberUtils.toBanglaDigits(currentIndex + 1)} ▾"
                            } else {
                                "হাদীস নির্বাচন ▾"
                            },
                            color = Color.White,
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Next Hadith Button
                TextButton(
                    onClick = {
                        if (currentIndex < totalHadiths - 1) {
                            coroutineScope.launch {
                                listState.animateScrollToItem(currentIndex + 1)
                            }
                        }
                    },
                    enabled = currentIndex < totalHadiths - 1,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "পরবর্তী",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (currentIndex < totalHadiths - 1) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "পরবর্তী হাদীস",
                        modifier = Modifier.size(16.dp),
                        tint = if (currentIndex < totalHadiths - 1) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                    )
                }
            }
        }

        // Hadith List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(activeHadiths, key = { _, it -> it.id }) { index, hadith ->
                HadithItemCard(
                    hadith = hadith,
                    arabicFontSize = arabicFontSize,
                    banglaFontSize = banglaFontSize,
                    onToggleBookmark = { onToggleBookmark(hadith) },
                    hadithIndex = index,
                    totalCount = activeHadiths.size,
                    onPreviousHadith = if (index > 0) {
                        { coroutineScope.launch { listState.animateScrollToItem(index - 1) } }
                    } else null,
                    onNextHadith = if (index < activeHadiths.size - 1) {
                        { coroutineScope.launch { listState.animateScrollToItem(index + 1) } }
                    } else null
                )
            }

            // End of chapter card
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "✨ আলহামদুলিল্লাহ! এই অধ্যায়ের হাদীস পাঠ সমাপ্ত",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "গ্রন্থ: ${book.nameBn} • অধ্যায়ে হাদীস: ${BanglaNumberUtils.toBanglaDigits(activeHadiths.size)} টি",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val currentChapterIndex = chapters.indexOfFirst { it.chapterNumber == selectedChapterNumber }
                            if (currentChapterIndex > 0) {
                                val prevChapter = chapters[currentChapterIndex - 1]
                                OutlinedButton(
                                    onClick = {
                                        selectedChapterNumber = prevChapter.chapterNumber
                                        coroutineScope.launch { listState.scrollToItem(0) }
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("⏮ ${prevChapter.titleBn}", fontSize = 11.sp, maxLines = 1)
                                }
                            }
                            if (currentChapterIndex >= 0 && currentChapterIndex < chapters.size - 1) {
                                val nextChapter = chapters[currentChapterIndex + 1]
                                Button(
                                    onClick = {
                                        selectedChapterNumber = nextChapter.chapterNumber
                                        coroutineScope.launch { listState.scrollToItem(0) }
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("পরবর্তী অধ্যায় ⏭", fontSize = 11.sp, maxLines = 1, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                val currentBookIndex = allBooks.indexOfFirst { it.slug == book.slug }
                                if (currentBookIndex in 0 until allBooks.size - 1) {
                                    val nextBook = allBooks[currentBookIndex + 1]
                                    Button(
                                        onClick = { onSelectBook(nextBook) },
                                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("পরবর্তী কিতাব: ${nextBook.nameBn} ⏭", fontSize = 11.sp, maxLines = 1, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Quick Hadith Jump Dialog
    if (showHadithJumpDialog) {
        HadithQuickSwitchDialog(
            hadiths = activeHadiths,
            currentVisibleIndex = listState.firstVisibleItemIndex.coerceIn(0, (activeHadiths.size - 1).coerceAtLeast(0)),
            onSelectHadithIndex = { targetIndex ->
                coroutineScope.launch {
                    listState.animateScrollToItem(targetIndex)
                }
            },
            onDismiss = { showHadithJumpDialog = false }
        )
    }

    // Book Switch Dialog
    if (showBookSwitchDialog) {
        HadithBookSwitchDialog(
            allBooks = allBooks,
            currentBookSlug = book.slug,
            onSelectBook = { selected ->
                onSelectBook(selected)
                showBookSwitchDialog = false
            },
            onDismiss = { showBookSwitchDialog = false }
        )
    }

    // Chapter Switch Dialog
    if (showChapterPickerDialog) {
        HadithChapterSwitchDialog(
            chapters = chapters,
            currentChapterNumber = selectedChapterNumber,
            onSelectChapter = { targetChapter ->
                selectedChapterNumber = targetChapter
                coroutineScope.launch { listState.scrollToItem(0) }
                showChapterPickerDialog = false
            },
            onDismiss = { showChapterPickerDialog = false }
        )
    }
}

@Composable
private fun HadithSearchResultsView(
    query: String,
    results: List<HadithEntity>,
    arabicFontSize: Float,
    banglaFontSize: Float,
    onToggleBookmark: (HadithEntity) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp)
    ) {
        Surface(
            color = IslamicGold.copy(alpha = 0.12f),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "অনুসন্ধানের ফলাফল: \"$query\" (${BanglaNumberUtils.toBanglaDigits(results.size)} টি পাওয়া গেছে)",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        if (results.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "কোনো হাদিস পাওয়া যায়নি। সঠিক শব্দ দিয়ে পুনরায় খুঁজুন।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 40.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(results, key = { it.id }) { hadith ->
                    HadithItemCard(
                        hadith = hadith,
                        arabicFontSize = arabicFontSize,
                        banglaFontSize = banglaFontSize,
                        onToggleBookmark = { onToggleBookmark(hadith) }
                    )
                }
            }
        }
    }
}

@Composable
private fun HadithItemCard(
    hadith: HadithEntity,
    arabicFontSize: Float,
    banglaFontSize: Float,
    onToggleBookmark: () -> Unit,
    hadithIndex: Int? = null,
    totalCount: Int? = null,
    onPreviousHadith: (() -> Unit)? = null,
    onNextHadith: (() -> Unit)? = null
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.2.dp),
        border = BorderStroke(0.7.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row: [হাদিস নম্বর] | [অধ্যায়] | [মানদণ্ড/গ্রেড]
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = IslamicGreen.copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "হাদীস: ${BanglaNumberUtils.toBanglaDigits(hadith.hadithNumber)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        ),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Surface(
                    color = IslamicGold.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = hadith.gradeBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Bookmark Icon
                IconButton(
                    onClick = onToggleBookmark,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = if (hadith.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "বুকমার্ক",
                        tint = if (hadith.isBookmarked) IslamicGold else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Narrator
            if (hadith.narratorBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "🎙️ বর্ণনাকারী: ",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        )
                    )
                    Text(
                        text = hadith.narratorBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic Text
            if (hadith.arabicText.isNotBlank()) {
                Text(
                    text = hadith.arabicText,
                    fontSize = arabicFontSize.sp,
                    lineHeight = (arabicFontSize * 1.55f).sp,
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Bengali Translation
            Text(
                text = hadith.banglaText,
                fontSize = banglaFontSize.sp,
                lineHeight = (banglaFontSize * 1.55f).sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Hadith Explanation & Fiqh Note
            if (hadith.explanationBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💡 শিক্ষণীয় বিষয় ও প্রেক্ষাপট:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = hadith.explanationBn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = 11.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))

            // Bottom Actions: Copy, Share, Source
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = hadith.sourceBn,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.outline
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = {
                        val textToCopy = "${hadith.banglaText}\n\n[${hadith.arabicText}]\n\nবর্ণনাকারী: ${hadith.narratorBn}\nমানদণ্ড: ${hadith.gradeBn}\nউৎস: ${hadith.sourceBn}"
                        clipboardManager.setText(AnnotatedString(textToCopy))
                        Toast.makeText(context, "হাদিসটি কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(17.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "${hadith.banglaText}\n\n[${hadith.arabicText}]\n\nবর্ণনাকারী: ${hadith.narratorBn}\nমানদণ্ড: ${hadith.gradeBn}\nউৎস: ${hadith.sourceBn}\n\n— দা'ওয়াহ টু জান্নাহ্ অ্যাপ"
                            )
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "হাদিস শেয়ার করুন"))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "শেয়ার",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(17.dp)
                    )
                }
            }

            // Quick Navigation between Hadiths directly from the card
            if (hadithIndex != null && totalCount != null && totalCount > 1) {
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f),
                    modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = { onPreviousHadith?.invoke() },
                        enabled = onPreviousHadith != null,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "পূর্ববর্তী হাদীস",
                            modifier = Modifier.size(14.dp),
                            tint = if (onPreviousHadith != null) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "পূর্ববর্তী হাদীস",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (onPreviousHadith != null) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    }

                    Surface(
                        color = IslamicGreen.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "হাদীস: ${BanglaNumberUtils.toBanglaDigits(hadithIndex + 1)} / ${BanglaNumberUtils.toBanglaDigits(totalCount)}",
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    TextButton(
                        onClick = { onNextHadith?.invoke() },
                        enabled = onNextHadith != null,
                        contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "পরবর্তী হাদীস",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (onNextHadith != null) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "পরবর্তী হাদীস",
                            modifier = Modifier.size(14.dp),
                            tint = if (onNextHadith != null) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HadithQuickSwitchDialog(
    hadiths: List<HadithEntity>,
    currentVisibleIndex: Int,
    onSelectHadithIndex: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredHadiths = remember(hadiths, searchQuery) {
        if (searchQuery.isBlank()) {
            hadiths.mapIndexed { index, hadith -> index to hadith }
        } else {
            val q = searchQuery.trim().lowercase()
            hadiths.mapIndexedNotNull { index, hadith ->
                val numBn = BanglaNumberUtils.toBanglaDigits(hadith.hadithNumber)
                val matchesNum = hadith.hadithNumber.toString().contains(q) || numBn.contains(q)
                val matchesNarrator = hadith.narratorBn.lowercase().contains(q)
                val matchesBangla = hadith.banglaText.lowercase().contains(q)
                if (matchesNum || matchesNarrator || matchesBangla) index to hadith else null
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = IslamicGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "হাদীস দ্রুত পরিবর্তন",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("হাদিস নম্বর বা শব্দ দিয়ে খুঁজুন...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "মুছুন", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "মোট হাদীস: ${BanglaNumberUtils.toBanglaDigits(filteredHadiths.size)} টি",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(320.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filteredHadiths, key = { it.second.id }) { (index, hadith) ->
                        val isCurrent = index == currentVisibleIndex
                        Surface(
                            onClick = {
                                onSelectHadithIndex(index)
                                onDismiss()
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isCurrent) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (isCurrent) BorderStroke(1.2.dp, IslamicGreen) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (isCurrent) IslamicGreen else MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = "#${BanglaNumberUtils.toBanglaDigits(hadith.hadithNumber)}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCurrent) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = hadith.narratorBn.ifBlank { hadith.chapterTitleBn },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 11.5.sp
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = hadith.banglaText,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontSize = 10.5.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (isCurrent) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "বর্তমান",
                                        tint = IslamicGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", color = IslamicGreen, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun HadithBookSwitchDialog(
    allBooks: List<HadithBookEntity>,
    currentBookSlug: String,
    onSelectBook: (HadithBookEntity) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = IslamicGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "হাদীস কিতাব পরিবর্তন",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(340.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(allBooks, key = { it.slug }) { book ->
                    val isCurrent = book.slug == currentBookSlug
                    Surface(
                        onClick = { onSelectBook(book) },
                        shape = RoundedCornerShape(10.dp),
                        color = if (isCurrent) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = if (isCurrent) BorderStroke(1.2.dp, IslamicGreen) else null,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "${book.nameBn} (${book.nameAr})",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent) IslamicGreen else MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                val categoryText = if (book.isSihahSitta) "সিহাহ সিত্তাহ" else "প্রাইমারি কোর হাদিস"
                                Text(
                                    text = "$categoryText • মোট হাদিস: ${BanglaNumberUtils.toBanglaDigits(book.totalHadiths)} টি",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                            if (isCurrent) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "বর্তমান",
                                    tint = IslamicGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", color = IslamicGreen, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun HadithChapterSwitchDialog(
    chapters: List<HadithChapterEntity>,
    currentChapterNumber: Int,
    onSelectChapter: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val filtered = remember(chapters, searchQuery) {
        if (searchQuery.isBlank()) chapters
        else {
            val q = searchQuery.trim().lowercase()
            chapters.filter {
                it.titleBn.lowercase().contains(q) ||
                    it.titleAr.lowercase().contains(q) ||
                    it.chapterNumber.toString() == q ||
                    BanglaNumberUtils.toBanglaDigits(it.chapterNumber).contains(q) ||
                    it.hadithRange.contains(q)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = IslamicGreen,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "অধ্যায় নির্বাচন (${BanglaNumberUtils.toBanglaDigits(chapters.size)} টি অধ্যায়)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(420.dp)) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("অধ্যায়ের নাম বা নম্বর দিয়ে খুঁজুন...", fontSize = 12.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "মুছুন", modifier = Modifier.size(18.dp))
                            }
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                )

                Text(
                    text = "মোট প্রাপ্ত: ${BanglaNumberUtils.toBanglaDigits(filtered.size)} টি অধ্যায়",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(filtered, key = { it.id }) { ch ->
                        val isSelected = ch.chapterNumber == currentChapterNumber
                        Surface(
                            onClick = {
                                onSelectChapter(ch.chapterNumber)
                            },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) IslamicGreen.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = if (isSelected) BorderStroke(1.2.dp, IslamicGreen) else null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.primaryContainer,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = BanglaNumberUtils.toBanglaDigits(ch.chapterNumber),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                        ),
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = ch.titleBn,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                            color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.onSurface
                                        ),
                                        fontSize = 13.sp
                                    )
                                    if (ch.hadithRange.isNotBlank()) {
                                        Text(
                                            text = "হাদীস: ${ch.hadithRange}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "নির্বাচিত",
                                        tint = IslamicGreen,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", color = IslamicGreen, fontWeight = FontWeight.Bold)
            }
        }
    )
}
