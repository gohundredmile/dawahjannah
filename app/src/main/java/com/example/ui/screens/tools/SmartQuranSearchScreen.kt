package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.SmartQuranCatalog
import com.example.data.model.SemanticQuranAyah
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.AyahAudioPlayerHelper
import com.example.util.SmartQuranSearchAiService
import com.example.util.SmartQuranSearchResult
import kotlinx.coroutines.launch

@Composable
fun SmartQuranSearchScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val aiService = remember { SmartQuranSearchAiService(context) }
    val audioPlayer = remember { AyahAudioPlayerHelper(context) }

    // Search state
    var searchQuery by remember { mutableStateOf("Verses about people who lose hope") }
    var searchInputText by remember { mutableStateOf("Verses about people who lose hope") }
    var isSearching by remember { mutableStateOf(false) }
    var searchResult by remember {
        mutableStateOf<SmartQuranSearchResult?>(null)
    }

    // Bookmarked items state
    val bookmarkedAyahs = remember { mutableStateMapOf<String, Boolean>() }

    // Execute initial search for "Verses about people who lose hope"
    LaunchedEffect(Unit) {
        isSearching = true
        searchResult = aiService.search(searchQuery)
        isSearching = false
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.stop()
        }
    }

    val isAudioPlaying by audioPlayer.isPlaying.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Top App Bar / Header
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            audioPlayer.stop()
                            onNavigateBack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পিছনে যান",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Smart Quran Search",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = IslamicGold.copy(alpha = 0.2f),
                                    border = BorderStroke(0.8.dp, IslamicGold)
                                ) {
                                    Text(
                                        text = "AI Semantic",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicGold
                                        ),
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "ভাবার্থ, মানবিক আবেগ ও জীবনের বাস্তব সংকট অনুযায়ী কুরআন সন্ধান",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }

            // Hero Semantic Search Input Box
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.6f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "আপনার ভাবনা বা আবেগ প্রকাশ করুন:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Text Field
                        OutlinedTextField(
                            value = searchInputText,
                            onValueChange = { searchInputText = it },
                            placeholder = {
                                Text(
                                    text = "যেমন: 'Verses about people who lose hope' বা 'হতাশ হয়ে পড়া'",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    fontFamily = banglaFont
                                )
                            },
                            trailingIcon = {
                                if (searchInputText.isNotBlank()) {
                                    IconButton(onClick = { searchInputText = "" }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "মুছে ফেলুন",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = {
                                keyboardController?.hide()
                                if (searchInputText.isNotBlank()) {
                                    searchQuery = searchInputText
                                    isSearching = true
                                    audioPlayer.stop()
                                    coroutineScope.launch {
                                        searchResult = aiService.search(searchInputText)
                                        isSearching = false
                                    }
                                }
                            }),
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = IslamicGold,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Search Button & Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "অক্ষরিক নয়, গভীর অর্থভিত্তিক",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }

                            Button(
                                onClick = {
                                    keyboardController?.hide()
                                    if (searchInputText.isNotBlank()) {
                                        searchQuery = searchInputText
                                        isSearching = true
                                        audioPlayer.stop()
                                        coroutineScope.launch {
                                            searchResult = aiService.search(searchInputText)
                                            isSearching = false
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = IslamicGold,
                                    contentColor = Color.Black
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "অনুসন্ধান",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }

            // Semantic Quick Search Suggestions Row
            item {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(
                        text = "জনপ্রিয় ভাবার্থ অনুসন্ধানসমূহ:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(SmartQuranCatalog.suggestions) { suggestion ->
                            val isSelected = searchQuery.equals(suggestion.titleEn, ignoreCase = true) ||
                                    searchQuery.equals(suggestion.titleBn, ignoreCase = true)

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) IslamicGold.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) IslamicGold else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.clickable {
                                    searchInputText = suggestion.titleEn
                                    searchQuery = suggestion.titleEn
                                    isSearching = true
                                    audioPlayer.stop()
                                    coroutineScope.launch {
                                        searchResult = aiService.search(suggestion.titleEn)
                                        isSearching = false
                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = suggestion.iconEmoji, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = suggestion.titleEn,
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (isSelected) IslamicGold else MaterialTheme.colorScheme.onSurface
                                        )
                                        Text(
                                            text = suggestion.titleBn,
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Loading Indicator
            if (isSearching) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = IslamicGold,
                                modifier = Modifier.size(36.dp),
                                strokeWidth = 3.dp
                            )
                            Spacer(modifier = Modifier.height(14.dp))
                            Text(
                                text = "কুরআনিক ভাবার্থ ও ঐশী সমাধান অনুসন্ধান করা হচ্ছে...",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "মুমিনের আবেগ, পরীক্ষা ও জীবনের সমাধানসূত্র মেলাচ্ছে AI Semantic Engine",
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.5.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }

            // Results Section
            if (!isSearching && searchResult != null) {
                val res = searchResult!!

                // Semantic Query Insight Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                        ),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = IslamicGold.copy(alpha = 0.2f),
                                modifier = Modifier.size(34.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "ঐশী উপলব্ধি ও নির্দেশিকা",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )

                                    if (res.isAiEnhanced) {
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFF0D9488)
                                        ) {
                                            Text(
                                                text = "AI Enhanced",
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color.White
                                                ),
                                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = res.interpretedIntentBn,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }

                // Results Counter & Filter Summary
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "প্রাসঙ্গিক ${res.ayahs.size} টি আয়াত পাওয়া গেছে",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )

                        Text(
                            text = "অনুসন্ধান: \"${res.query}\"",
                            style = MaterialTheme.typography.labelSmall,
                            color = IslamicGold,
                            maxLines = 1
                        )
                    }
                }

                // List of Semantic Ayah Cards
                items(res.ayahs, key = { it.id }) { ayah ->
                    val isBookmarked = bookmarkedAyahs[ayah.id] == true

                    SemanticAyahResultCard(
                        ayah = ayah,
                        audioPlayer = audioPlayer,
                        isBookmarked = isBookmarked,
                        onToggleBookmark = {
                            val next = !isBookmarked
                            bookmarkedAyahs[ayah.id] = next
                            Toast.makeText(
                                context,
                                if (next) "আয়াতটি বুকমার্কে সংরক্ষিত হয়েছে" else "বুকমার্ক থেকে সরানো হয়েছে",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        onThemeClick = { theme ->
                            searchInputText = theme
                            searchQuery = theme
                            isSearching = true
                            audioPlayer.stop()
                            coroutineScope.launch {
                                searchResult = aiService.search(theme)
                                isSearching = false
                            }
                        }
                    )
                }
            }
        }
    }
}

/**
 * Exceptionally Crafted Card Component for Semantic Ayah Results
 */
@Composable
private fun SemanticAyahResultCard(
    ayah: SemanticQuranAyah,
    audioPlayer: AyahAudioPlayerHelper,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onThemeClick: (String) -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    var isTafsirExpanded by remember { mutableStateOf(false) }
    var showTransliteration by remember { mutableStateOf(false) }

    val isAudioPlaying by audioPlayer.isPlaying.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Header: Surah & Ayah Badge + Revelation Type + Topic Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = "${ayah.surahNumber}:${ayah.ayahNumber}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = IslamicGold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column {
                        Text(
                            text = ayah.surahNameBn,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${ayah.surahNameEn} • ",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = ayah.revelationTypeBn,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }

                // Bookmark & Share Buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক",
                            tint = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareText = buildString {
                                appendLine("আল-কুরআন | ${ayah.surahNameBn} (আয়াত: ${ayah.ayahNumber})")
                                appendLine()
                                appendLine(ayah.arabicText)
                                appendLine()
                                appendLine("অনুবাদ:")
                                appendLine(ayah.banglaTranslation)
                                appendLine()
                                appendLine("ঐশী সমাধান:")
                                appendLine(ayah.divineWisdomBn)
                                appendLine()
                                appendLine("— দা'ওয়াহ টু জান্নাহ্ (Smart Quran Search)")
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "আয়াত শেয়ার করুন"))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Text Block with Traditional Ornate Styling & Pristine Typography
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF059669).copy(alpha = 0.15f),
                            border = BorderStroke(0.6.dp, Color(0xFF059669).copy(alpha = 0.4f))
                        ) {
                            Text(
                                text = "পবিত্র কুরআনুল কারীম • কালামুল্লাহ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669)
                                ),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                                fontFamily = banglaFont
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = ayah.surahNameAr,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                color = IslamicGold,
                                fontFamily = arabicFont
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = ClipData.newPlainText("Ayah Arabic", ayah.arabicText)
                                    clipboard.setPrimaryClip(clip)
                                    Toast.makeText(context, "আরবি আয়াত কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "কপি করুন",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Excellent Classical Arabic Typography with Amiri font and Ayah Ornament
                    val decoratedArabic = "${ayah.arabicText} ﴿${com.example.util.CalendarHelper.toArabicNumber(ayah.ayahNumber)}﴾"
                    Text(
                        text = decoratedArabic,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 24.sp,
                            lineHeight = 48.sp,
                            textDirection = TextDirection.Rtl,
                            textAlign = TextAlign.Right
                        ),
                        fontFamily = arabicFont,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Transliteration Toggle & Text
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { showTransliteration = !showTransliteration }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (showTransliteration) "উচ্চারণ লুকান" else "বাংলা উচ্চারণ দেখুন",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = IslamicGold,
                    fontFamily = banglaFont
                )
                Icon(
                    imageVector = if (showTransliteration) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = IslamicGold,
                    modifier = Modifier.size(16.dp)
                )
            }

            AnimatedVisibility(visible = showTransliteration) {
                Text(
                    text = ayah.transliterationBn,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        lineHeight = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont,
                    modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bangla Translation (Clearly Sourced)
            Text(
                text = "বাংলা অনুবাদ (ইসলামিক ফাউন্ডেশন ও মহিউদ্দীন খান):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = ayah.banglaTranslation,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            // English Translation (Sahih International Sourced)
            Text(
                text = "English Translation (Sahih International):",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = ayah.englishTranslation,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 12.5.sp,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // AI Semantic Reflection (Clearly distinguished as AI-assisted topical insight)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp)) {
                    Icon(
                        imageVector = Icons.Default.Spa,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ঐশী উপলব্ধি ও বাস্তব জীবনের সমাধান:",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.secondary,
                                fontFamily = banglaFont
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.18f)
                            ) {
                                Text(
                                    text = "এআই ভাবার্থ বিশ্লেষণ",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    ),
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ayah.divineWisdomBn,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "* আয়াতভিত্তিক প্রজ্ঞাময় অনুধাবন — এটি কোনো শরঈ ফতোয়া বা নতুন ধর্মীয় বিধান নয়।",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            // Collapsible Classical Tafsir & Hadith Section (Authentic & Sourced)
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isTafsirExpanded = !isTafsirExpanded }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "প্রামাণ্য তাফসীর ও প্রেক্ষাপট (ইবনে কাসীর / মা'আরিফুল কুরআন)",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }

                        Icon(
                            imageVector = if (isTafsirExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    AnimatedVisibility(visible = isTafsirExpanded) {
                        Column(modifier = Modifier.padding(top = 8.dp)) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                            Text(
                                text = ayah.tafsirSummaryBn,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )

                            if (!ayah.relatedHadithBn.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = IslamicGold.copy(alpha = 0.12f),
                                    border = BorderStroke(0.6.dp, IslamicGold.copy(alpha = 0.3f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(
                                                imageVector = Icons.Default.AutoAwesome,
                                                contentDescription = null,
                                                tint = IslamicGold,
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = "সম্পর্কিত সহীহ হাদীস ও সূত্র:",
                                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                color = IslamicGold,
                                                fontFamily = banglaFont
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = ayah.relatedHadithBn,
                                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Related Themes interactive tags
            if (ayah.relatedThemes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "সম্পর্কিত থিম: ",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    ayah.relatedThemes.forEach { theme ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { onThemeClick(theme) }
                        ) {
                            Text(
                                text = "#$theme",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }

            // Bottom Audio Player Controller Bar
            if (ayah.audioUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable {
                            if (isAudioPlaying) {
                                audioPlayer.stop()
                            } else {
                                audioPlayer.play(ayah.audioUrl)
                            }
                        }
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = IslamicGold,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isAudioPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isAudioPlaying) "থামুন" else "শুনুন",
                                    tint = Color.Black,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Text(
                                text = if (isAudioPlaying) "তিলাওয়াত চলছে..." else "তিলাওয়াত শুনুন",
                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isAudioPlaying) IslamicGold else MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "মিশারী রাশিদ আল-আফাসী",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    // Copy all details
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val fullText = "${ayah.arabicText}\n\n${ayah.banglaTranslation}\n\n[সূরা ${ayah.surahNameBn}, আয়াত: ${ayah.ayahNumber}]"
                            val clip = ClipData.newPlainText("Ayah Full", fullText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "আয়াত ও অর্থ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "কপি করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
