package com.example.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.QuranRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAudioManager
import com.example.util.QuranFullSearchResult
import com.example.util.QuranFullTextSearchEngine
import com.example.util.QuranSearchRevelationFilter
import com.example.util.QuranSearchSortMode
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val RECENT_SEARCHES_PREF = "search_within_quran_prefs"
private const val RECENT_SEARCHES_KEY = "recent_queries"

@Composable
fun SearchWithinQuranScreen(
    quranRepository: QuranRepository,
    audioManager: QuranAudioManager,
    initialQuery: String? = null,
    onOpenInQuran: (surahNumber: Int, ayahNumber: Int) -> Unit,
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val isDark = isSystemInDarkTheme()
    val coroutineScope = rememberCoroutineScope()
    val keyboardController = LocalSoftwareKeyboardController.current
    val listState = rememberLazyListState()

    BackHandler {
        onNavigateBack()
    }

    // Index state across all 114 Surahs
    val indexState by QuranFullTextSearchEngine.indexState.collectAsState()
    LaunchedEffect(Unit) {
        QuranFullTextSearchEngine.ensureIndexed(context)
    }

    // Audio player & Bookmark live states
    val playerState by audioManager.playerState.collectAsState()
    val bookmarks by quranRepository.getBookmarkedAyahs().collectAsState(initial = emptyList())
    val bookmarkedKeys = remember(bookmarks) {
        bookmarks.map { it.id }.toHashSet()
    }

    // Search UI state
    var query by rememberSaveable { mutableStateOf(initialQuery?.trim() ?: "") }
    LaunchedEffect(initialQuery) {
        if (!initialQuery.isNullOrBlank() && query != initialQuery.trim()) {
            query = initialQuery.trim()
        }
    }
    var revelationFilter by rememberSaveable { mutableStateOf(QuranSearchRevelationFilter.ALL) }
    var selectedSurahFilter by rememberSaveable { mutableStateOf<Int?>(null) }
    var sortMode by rememberSaveable { mutableStateOf(QuranSearchSortMode.RELEVANCE) }
    var includeTafsir by rememberSaveable { mutableStateOf(false) }
    var showPronunciation by rememberSaveable { mutableStateOf(true) }
    var visibleCount by remember { mutableIntStateOf(30) }

    var isSearching by remember { mutableStateOf(false) }
    var searchResult by remember { mutableStateOf<QuranFullSearchResult?>(null) }

    // Expanded & loaded Tafsir caches
    val expandedTafsirKeys = remember { mutableStateMapOf<String, Boolean>() }
    val loadedTafsirMap = remember { mutableStateMapOf<String, String>() }
    val loadingTafsirKeys = remember { mutableStateMapOf<String, Boolean>() }

    // Recent searches persisted in SharedPreferences
    val prefs = remember { context.getSharedPreferences(RECENT_SEARCHES_PREF, Context.MODE_PRIVATE) }
    val recentSearches = remember {
        mutableStateListOf<String>().apply {
            val saved = prefs.getString(RECENT_SEARCHES_KEY, "") ?: ""
            if (saved.isNotBlank()) {
                addAll(saved.split("|||").filter { it.isNotBlank() }.take(8))
            }
        }
    }

    fun recordRecentSearch(term: String) {
        val clean = term.trim()
        if (clean.length < 2) return
        recentSearches.remove(clean)
        recentSearches.add(0, clean)
        while (recentSearches.size > 8) {
            recentSearches.removeAt(recentSearches.lastIndex)
        }
        prefs.edit().putString(RECENT_SEARCHES_KEY, recentSearches.joinToString("|||")).apply()
    }

    // Immediate debounced lookup across all 114 Surahs whenever query or filters change
    LaunchedEffect(query, revelationFilter, selectedSurahFilter, sortMode, includeTafsir, indexState.isReady) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) {
            searchResult = null
            isSearching = false
            selectedSurahFilter = null
            visibleCount = 30
            return@LaunchedEffect
        }

        isSearching = true
        delay(130)
        val result = QuranFullTextSearchEngine.search(
            context = context,
            rawQuery = trimmed,
            revelationFilter = revelationFilter,
            surahFilterNumber = selectedSurahFilter,
            sortMode = sortMode,
            includeTafsir = includeTafsir
        )
        searchResult = result
        visibleCount = 30
        isSearching = false
    }

    // Reset Surah filter when user edits the query text itself
    LaunchedEffect(query) {
        selectedSurahFilter = null
    }

    val headerGradient = if (isDark) {
        listOf(Color(0xFF062C22), Color(0xFF0A3D2F), MaterialTheme.colorScheme.background)
    } else {
        listOf(Color(0xFF064E3B), Color(0xFF047857), Color(0xFF065F46))
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 760.dp)
        ) {
            // Emerald Editorial Header + Embedded Search Bar
            Surface(
                color = Color.Transparent,
                shadowElevation = 4.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Brush.verticalGradient(headerGradient))
                        .statusBarsPadding()
                        .padding(start = 10.dp, end = 12.dp, top = 6.dp, bottom = 14.dp)
                ) {
                    // Top Bar Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("search_within_quran_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পেছনে যান",
                                tint = Color.White
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Search within Quran",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 19.sp,
                                    color = Color.White
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "পূর্ণ কুরআন অনুসন্ধান • ১১৪ সূরা (৬,২৩৬ আয়াত)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontFamily = banglaFont,
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 11.5.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Toggle Pronunciation visibility
                        IconButton(
                            onClick = {
                                showPronunciation = !showPronunciation
                                Toast.makeText(
                                    context,
                                    if (showPronunciation) "বাংলা উচ্চারণ চালু" else "বাংলা উচ্চারণ বন্ধ",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("toggle_pronunciation_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = "উচ্চারণ অন/অফ",
                                tint = if (showPronunciation) IslamicGold else Color.White.copy(alpha = 0.55f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Instant Bangla Search Input Field
                    OutlinedTextField(
                        value = query,
                        onValueChange = { query = it },
                        placeholder = {
                            Text(
                                text = "বাংলায় বিষয়, শব্দ বা বাক্যাংশ লিখুন (যেমন: নামাজ, ধৈর্য, পিতামাতা)...",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontFamily = banglaFont,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingIcon = {
                            if (isSearching) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = IslamicGreen
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "অনুসন্ধান",
                                    tint = IslamicGreen
                                )
                            }
                        },
                        trailingIcon = {
                            if (query.isNotEmpty()) {
                                IconButton(
                                    onClick = { query = "" },
                                    modifier = Modifier
                                        .size(44.dp)
                                        .testTag("clear_quran_search_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "মুছুন",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(19.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                recordRecentSearch(query)
                                keyboardController?.hide()
                            }
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = IslamicGold,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.35f),
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = IslamicGreen
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = banglaFont,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp)
                            .testTag("quran_search_input_field")
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Live Indexing Status Strip
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (indexState.isIndexing && !indexState.isReady) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = "১১৪ সূরা ইনডেক্স হচ্ছে (${BanglaNumberUtils.toBanglaDigits(indexState.indexedSurahs)}/১১৪)...",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = banglaFont,
                                        color = Color(0xFFFDE68A),
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Color(0xFF6EE7B7),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "সম্পূর্ণ ১১৪ সূরা (${BanglaNumberUtils.toBanglaDigits(indexState.totalAyahs.takeIf { it > 0 } ?: 6236)} আয়াত) অফলাইন প্রস্তুত",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontFamily = banglaFont,
                                        color = Color.White.copy(alpha = 0.88f),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    AnimatedVisibility(visible = indexState.isIndexing && !indexState.isReady) {
                        LinearProgressIndicator(
                            progress = { (indexState.indexedSurahs / 114f).coerceIn(0f, 1f) },
                            color = IslamicGold,
                            trackColor = Color.White.copy(alpha = 0.2f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 6.dp, vertical = 5.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                    }
                }
            }

            // Main Scrollable Body
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    top = 8.dp,
                    bottom = contentPadding.calculateBottomPadding() + 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val activeResult = searchResult
                if (query.trim().isEmpty()) {
                    item(key = "discovery_empty_state") {
                        SearchDiscoveryEmptyState(
                            recentSearches = recentSearches,
                            onSelectTopic = { topic ->
                                query = topic
                                recordRecentSearch(topic)
                                keyboardController?.hide()
                            },
                            onClearRecentSearches = {
                                recentSearches.clear()
                                prefs.edit().remove(RECENT_SEARCHES_KEY).apply()
                            }
                        )
                    }
                } else if (activeResult != null) {
                    // Summary + Filters + Surah Distribution Bar
                    item(key = "results_summary_bar") {
                        SearchResultsSummaryBar(
                            result = activeResult,
                            revelationFilter = revelationFilter,
                            selectedSurahFilter = selectedSurahFilter,
                            sortMode = sortMode,
                            includeTafsir = includeTafsir,
                            onRevelationFilterChange = { revelationFilter = it },
                            onSurahFilterChange = { selectedSurahFilter = it },
                            onToggleSortMode = {
                                sortMode = if (sortMode == QuranSearchSortMode.RELEVANCE) {
                                    QuranSearchSortMode.QURAN_ORDER
                                } else {
                                    QuranSearchSortMode.RELEVANCE
                                }
                            },
                            onToggleIncludeTafsir = { includeTafsir = !includeTafsir }
                        )
                    }

                    if (activeResult.hits.isEmpty() && !isSearching) {
                        item(key = "no_results_card") {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(22.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = IslamicGold.copy(alpha = 0.16f),
                                        modifier = Modifier.size(56.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.SearchOff,
                                                contentDescription = null,
                                                tint = IslamicGold,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "“${activeResult.query}” নিয়ে সরাসরি কোনো আয়াত পাওয়া যায়নি",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = banglaFont
                                        ),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "বানান যাচাই করুন, মূল শব্দ দিয়ে খুঁজুন (যেমন: 'ধৈর্য', 'নামাজ', 'পিতামাতা', 'রিজিক'), অথবা তাফসীরের ভেতরেও অনুসন্ধান চালু করুন।",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = banglaFont,
                                            lineHeight = 19.sp
                                        ),
                                        textAlign = TextAlign.Center,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    if (!includeTafsir) {
                                        Spacer(modifier = Modifier.height(14.dp))
                                        Button(
                                            onClick = { includeTafsir = true },
                                            colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                                            shape = RoundedCornerShape(12.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.MenuBook,
                                                contentDescription = null,
                                                modifier = Modifier.size(18.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "তাফসীরসহ পূর্ণ ১১৪ সূরায় খুঁজুন",
                                                fontFamily = banglaFont,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        val visibleHits = activeResult.hits.take(visibleCount)
                        items(
                            items = visibleHits,
                            key = { "${it.surah.number}:${it.ayah.ayahNumber}" }
                        ) { hit ->
                            val key = "${hit.surah.number}:${hit.ayah.ayahNumber}"
                            val isPlayingThisAyah = playerState.isPlaying &&
                                playerState.surahNumber == hit.surah.number &&
                                playerState.activeAyahNumber == hit.ayah.ayahNumber
                            val isTafsirExpanded = expandedTafsirKeys[key] == true
                            val isTafsirLoading = loadingTafsirKeys[key] == true

                            QuranSearchResultCard(
                                hit = hit,
                                query = activeResult.query,
                                isBookmarked = key in bookmarkedKeys,
                                isAudioPlaying = isPlayingThisAyah,
                                isTafsirExpanded = isTafsirExpanded,
                                loadedTafsirText = loadedTafsirMap[key] ?: hit.ayah.tafsirText,
                                isTafsirLoading = isTafsirLoading,
                                showPronunciation = showPronunciation,
                                onPlayAudio = {
                                    recordRecentSearch(activeResult.query)
                                    if (isPlayingThisAyah) {
                                        audioManager.pause()
                                    } else {
                                        audioManager.playAyahAudio(
                                            surahNumber = hit.surah.number,
                                            ayahNumber = hit.ayah.ayahNumber,
                                            continuous = false,
                                            surahNameBn = hit.surah.nameBn
                                        )
                                    }
                                },
                                onToggleBookmark = {
                                    coroutineScope.launch {
                                        quranRepository.toggleBookmark(hit.ayah, hit.surah.nameBn)
                                        val nowBookmarked = key !in bookmarkedKeys
                                        Toast.makeText(
                                            context,
                                            if (nowBookmarked) "আয়াত বুকমার্ক করা হয়েছে" else "বুকমার্ক সরানো হয়েছে",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                },
                                onToggleTafsir = {
                                    val nextExpanded = !isTafsirExpanded
                                    expandedTafsirKeys[key] = nextExpanded
                                    if (nextExpanded && loadedTafsirMap[key] == null && hit.ayah.tafsirText == null) {
                                        loadingTafsirKeys[key] = true
                                        coroutineScope.launch {
                                            val tafsir = QuranFullTextSearchEngine.loadFullTafsir(
                                                context = context,
                                                surahNumber = hit.surah.number,
                                                ayahNumber = hit.ayah.ayahNumber
                                            )
                                            loadedTafsirMap[key] = tafsir
                                            loadingTafsirKeys[key] = false
                                        }
                                    }
                                },
                                onOpenInQuran = {
                                    recordRecentSearch(activeResult.query)
                                    onOpenInQuran(hit.surah.number, hit.ayah.ayahNumber)
                                },
                                onCopy = {
                                    val formatted = formatAyahShareText(hit)
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("Quran Ayah", formatted))
                                    Toast.makeText(context, "আয়াত ও অনুবাদ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                },
                                onShare = {
                                    val formatted = formatAyahShareText(hit)
                                    val sendIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_TEXT, formatted)
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "আয়াত শেয়ার করুন"))
                                },
                                modifier = Modifier.padding(horizontal = 14.dp)
                            )
                        }

                        // Pagination Load More Button
                        val remaining = activeResult.hits.size - visibleHits.size
                        if (remaining > 0) {
                            item(key = "load_more_button") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    OutlinedButton(
                                        onClick = { visibleCount += 30 },
                                        shape = RoundedCornerShape(14.dp),
                                        border = BorderStroke(1.dp, IslamicGreen),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(48.dp)
                                            .testTag("load_more_ayahs_button")
                                    ) {
                                        Text(
                                            text = "আরও আয়াত দেখুন (বাকি ${BanglaNumberUtils.toBanglaDigits(remaining)}টি)",
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontFamily = banglaFont,
                                                fontWeight = FontWeight.Bold,
                                                color = IslamicGreen
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else if (isSearching) {
                    item(key = "searching_loader") {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                CircularProgressIndicator(color = IslamicGreen)
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "১১৪ সূরা জুড়ে অনুসন্ধান চলছে...",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
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

private fun formatAyahShareText(hit: com.example.util.QuranAyahSearchHit): String {
    val sb = StringBuilder()
    sb.append("📖 সূরা ${hit.surah.nameBn} (${BanglaNumberUtils.toBanglaDigits(hit.surah.number)}:${BanglaNumberUtils.toBanglaDigits(hit.ayah.ayahNumber)})\n\n")
    sb.append("${hit.ayah.arabicText}\n\n")
    if (hit.ayah.pronunciationBn.isNotBlank()) {
        sb.append("উচ্চারণ: ${hit.ayah.pronunciationBn}\n\n")
    }
    sb.append("অনুবাদ: ${hit.ayah.translationBn}\n\n")
    sb.append("— আল-কুরআন (দা'ওয়াহ টু জান্নাহ্)")
    return sb.toString()
}
