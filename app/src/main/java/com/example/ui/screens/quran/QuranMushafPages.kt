package com.example.ui.screens.quran

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuranAyah
import com.example.data.model.QuranSettings
import com.example.data.model.QuranSurah
import com.example.data.model.SurahAudioPlayerState
import com.example.ui.theme.ArabicFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.QuranMushafPage
import com.example.util.QuranPaginationHelper
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * 15-Line Madani Mushaf Page View
 * Features:
 * - Authentic 15-line page budget per Madani standard
 * - Ornate Golden Islamic border & header cartouche
 * - Interactive 3D Book-turn page curl / flip animation
 * - Auto-sync with playing Ayah
 * - Interactive Ayah tapping for quick translation & audio
 */
@Composable
fun Madani15LineMushafView(
    surah: QuranSurah,
    ayahs: List<QuranAyah>,
    settings: QuranSettings,
    playerState: SurahAudioPlayerState,
    initialAyahNumber: Int? = null,
    onPlayAyah: (QuranAyah) -> Unit,
    onToggleBookmark: (QuranAyah) -> Unit,
    modifier: Modifier = Modifier
) {
    MushafBasePageView(
        surah = surah,
        ayahs = ayahs,
        linesPerPage = 15,
        layoutTheme = MushafLayoutTheme.MADANI_GOLD,
        settings = settings,
        playerState = playerState,
        initialAyahNumber = initialAyahNumber,
        onPlayAyah = onPlayAyah,
        onToggleBookmark = onToggleBookmark,
        modifier = modifier
    )
}

/**
 * 16-Line Noorani Hafizi Mushaf View
 * Features:
 * - Authentic 16-line page budget (Indo-Pak / Bangladesh Hafizi standard)
 * - Deep emerald and gold Noorani border
 * - 3D page flip transition animation
 * - Quick jump and audio sync
 */
@Composable
fun Noorani16LineMushafView(
    surah: QuranSurah,
    ayahs: List<QuranAyah>,
    settings: QuranSettings,
    playerState: SurahAudioPlayerState,
    initialAyahNumber: Int? = null,
    onPlayAyah: (QuranAyah) -> Unit,
    onToggleBookmark: (QuranAyah) -> Unit,
    modifier: Modifier = Modifier
) {
    MushafBasePageView(
        surah = surah,
        ayahs = ayahs,
        linesPerPage = 16,
        layoutTheme = MushafLayoutTheme.NOORANI_EMERALD,
        settings = settings,
        playerState = playerState,
        initialAyahNumber = initialAyahNumber,
        onPlayAyah = onPlayAyah,
        onToggleBookmark = onToggleBookmark,
        modifier = modifier
    )
}

/**
 * Distraction-Free Pure Arabic Recitation Mode
 * Features:
 * - Continuous smooth reading flow without translations
 * - Clear, large Arabic typography
 * - Floating font size zoom (+ / -) controls
 * - Tap any Ayah to view translation sheet or play audio
 */
@Composable
fun PureArabicRecitationView(
    surah: QuranSurah,
    ayahs: List<QuranAyah>,
    settings: QuranSettings,
    playerState: SurahAudioPlayerState,
    onPlayAyah: (QuranAyah) -> Unit,
    onToggleBookmark: (QuranAyah) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    var fontSizeMultiplier by remember { mutableFloatStateOf(1f) }
    var selectedAyahForAction by remember { mutableStateOf<QuranAyah?>(null) }

    val baseSize = (settings.arabicFontSize * fontSizeMultiplier).coerceIn(20f, 44f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Ornate Surah Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = IslamicGreen.copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.2.dp, IslamicGreen.copy(alpha = 0.35f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "سُورَةُ ${surah.nameAr}",
                        fontFamily = ArabicFontFamily,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreen
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "সূরা ${surah.nameBn} • ${surah.revelationType} • মোট আয়াত: ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)}",
                        fontFamily = banglaFont,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bismillah Banner (except Surah 9)
            if (surah.number != 9 && surah.number != 1) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = ArabicFontFamily,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Flowing continuous Arabic Mushaf Text
            val annotatedString = buildAnnotatedString {
                ayahs.forEach { ayah ->
                    val isPlaying = playerState.isPlaying &&
                            playerState.surahNumber == surah.number &&
                            playerState.activeAyahNumber == ayah.ayahNumber

                    val ayahStart = length
                    append(ayah.arabicText.trim())
                    append(" ")

                    // Verse end ornament ۝ with Arabic numerals
                    val ornament = " ۝${QuranPaginationHelper.toArabicDigits(ayah.ayahNumber)} "
                    append(ornament)

                    addStringAnnotation(
                        tag = "AYAH_CLICK",
                        annotation = ayah.ayahNumber.toString(),
                        start = ayahStart,
                        end = length
                    )

                    if (isPlaying) {
                        addStyle(
                            style = SpanStyle(
                                background = IslamicGold.copy(alpha = 0.35f),
                                color = IslamicGreen,
                                fontWeight = FontWeight.Bold
                            ),
                            start = ayahStart,
                            end = length
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    ClickableText(
                        text = annotatedString,
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = ArabicFontFamily,
                            fontSize = baseSize.sp,
                            lineHeight = (baseSize * 1.9f).sp,
                            textAlign = TextAlign.Right,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "AYAH_CLICK",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                val ayahNum = annotation.item.toIntOrNull()
                                val clickedAyah = ayahs.find { it.ayahNumber == ayahNum }
                                if (clickedAyah != null) {
                                    selectedAyahForAction = clickedAyah
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Floating Zoom Controls (+ / -) in bottom-end corner
        Surface(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.95f),
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        fontSizeMultiplier = (fontSizeMultiplier - 0.1f).coerceAtLeast(0.8f)
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "ফন্ট ছোট করুন",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "${(baseSize).toInt()}sp",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                IconButton(
                    onClick = {
                        fontSizeMultiplier = (fontSizeMultiplier + 0.1f).coerceAtMost(1.5f)
                    },
                    modifier = Modifier.size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "ফন্ট বড় করুন",
                        tint = IslamicGreen,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // Quick Ayah Action Sheet when tapping a verse
        selectedAyahForAction?.let { ayah ->
            MushafAyahActionBottomSheet(
                surah = surah,
                ayah = ayah,
                onDismiss = { selectedAyahForAction = null },
                onPlay = {
                    onPlayAyah(ayah)
                    selectedAyahForAction = null
                },
                onToggleBookmark = {
                    onToggleBookmark(ayah)
                }
            )
        }
    }
}

private enum class MushafLayoutTheme {
    MADANI_GOLD,
    NOORANI_EMERALD
}

/**
 * Base Composable for Paginated 15-Line & 16-Line Mushaf Views
 * Features attractive 3D page curl / flip transition animation on HorizontalPager
 */
@Composable
private fun MushafBasePageView(
    surah: QuranSurah,
    ayahs: List<QuranAyah>,
    linesPerPage: Int,
    layoutTheme: MushafLayoutTheme,
    settings: QuranSettings,
    playerState: SurahAudioPlayerState,
    initialAyahNumber: Int? = null,
    onPlayAyah: (QuranAyah) -> Unit,
    onToggleBookmark: (QuranAyah) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current

    val pages = remember(surah.number, ayahs, linesPerPage) {
        QuranPaginationHelper.paginateSurah(surah, ayahs, linesPerPage)
    }

    if (pages.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = IslamicGreen)
        }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { pages.size }
    )

    var selectedAyahForAction by remember { mutableStateOf<QuranAyah?>(null) }
    var showJumpToPageDialog by remember { mutableStateOf(false) }

    // Jump to page containing initialAyahNumber if provided
    LaunchedEffect(initialAyahNumber, pages) {
        if (initialAyahNumber != null && initialAyahNumber > 0) {
            val targetPageIdx = pages.indexOfFirst { page ->
                initialAyahNumber in page.startAyahNumber..page.endAyahNumber
            }
            if (targetPageIdx >= 0) {
                pagerState.scrollToPage(targetPageIdx)
            }
        }
    }

    // Auto-turn page when audio is playing an Ayah on another page
    LaunchedEffect(playerState.activeAyahNumber, playerState.isPlaying, playerState.surahNumber, surah.number) {
        val activeAyahNum = playerState.activeAyahNumber
        if (playerState.isPlaying && playerState.surahNumber == surah.number && activeAyahNum != null && activeAyahNum > 0) {
            val targetPageIdx = pages.indexOfFirst { page ->
                activeAyahNum in page.startAyahNumber..page.endAyahNumber
            }
            if (targetPageIdx >= 0 && targetPageIdx != pagerState.currentPage) {
                pagerState.animateScrollToPage(targetPageIdx)
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Navigation Ribbon: Page indicators & Turn buttons
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Previous Page Button
                IconButton(
                    onClick = {
                        if (pagerState.currentPage > 0) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        }
                    },
                    enabled = pagerState.currentPage > 0,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "পূর্ববর্তী পৃষ্ঠা",
                        tint = if (pagerState.currentPage > 0) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        modifier = Modifier.size(19.dp)
                    )
                }

                // Page Indicator Pill (Clickable to jump)
                Surface(
                    onClick = { showJumpToPageDialog = true },
                    shape = RoundedCornerShape(12.dp),
                    color = when (layoutTheme) {
                        MushafLayoutTheme.MADANI_GOLD -> IslamicGold.copy(alpha = 0.15f)
                        MushafLayoutTheme.NOORANI_EMERALD -> IslamicGreen.copy(alpha = 0.12f)
                    },
                    border = BorderStroke(
                        1.dp,
                        when (layoutTheme) {
                            MushafLayoutTheme.MADANI_GOLD -> IslamicGold.copy(alpha = 0.4f)
                            MushafLayoutTheme.NOORANI_EMERALD -> IslamicGreen.copy(alpha = 0.35f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoStories,
                            contentDescription = null,
                            tint = when (layoutTheme) {
                                MushafLayoutTheme.MADANI_GOLD -> IslamicGold
                                MushafLayoutTheme.NOORANI_EMERALD -> IslamicGreen
                            },
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        val curPage = pages.getOrNull(pagerState.currentPage)
                        val pageNumBn = BanglaNumberUtils.toBanglaDigits((pagerState.currentPage + 1))
                        val totalPagesBn = BanglaNumberUtils.toBanglaDigits(pages.size)
                        val mushafGlobalBn = BanglaNumberUtils.toBanglaDigits(curPage?.globalMushafPageNumber ?: 1)
                        Text(
                            text = "পৃষ্ঠা $pageNumBn / $totalPagesBn (মুসহাফ: $mushafGlobalBn)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }

                // Next Page Button
                IconButton(
                    onClick = {
                        if (pagerState.currentPage < pages.size - 1) {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    },
                    enabled = pagerState.currentPage < pages.size - 1,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "পরবর্তী পৃষ্ঠা",
                        tint = if (pagerState.currentPage < pages.size - 1) IslamicGreen else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }

        // Horizontal Pager with 3D Book Page Curl Animation
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp)
        ) { pageIndex ->
            val page = pages[pageIndex]

            // 3D Curl / Flip Transformation Calculation
            val pageOffset = (pagerState.currentPage - pageIndex) + pagerState.currentPageOffsetFraction
            val pageOffsetAbs = abs(pageOffset)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        // 3D rotation around Y axis giving realistic page flip feel
                        cameraDistance = 14f * density
                        rotationY = pageOffset * -32f

                        // Subtle depth perspective scaling
                        val scale = (1f - pageOffsetAbs * 0.08f).coerceIn(0.92f, 1f)
                        scaleX = scale
                        scaleY = scale

                        // Soft fade for pages in the background
                        alpha = (1f - pageOffsetAbs * 0.35f).coerceIn(0.65f, 1f)
                    }
                    .shadow(
                        elevation = (4.dp * (1f - pageOffsetAbs)).coerceAtLeast(0.dp),
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                // Actual Mushaf Page Render
                MushafSinglePageCard(
                    surah = surah,
                    page = page,
                    layoutTheme = layoutTheme,
                    settings = settings,
                    playerState = playerState,
                    onAyahClick = { ayah ->
                        selectedAyahForAction = ayah
                    }
                )

                // Book spine subtle shadow overlay on the left/right depending on offset
                if (pageOffsetAbs > 0.01f) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = (0.25f * pageOffsetAbs).coerceAtMost(0.3f)),
                                        Color.Transparent,
                                        Color.Black.copy(alpha = (0.25f * pageOffsetAbs).coerceAtMost(0.3f))
                                    )
                                )
                            )
                    )
                }
            }
        }
    }

    // Jump to Page Dialog
    if (showJumpToPageDialog) {
        var sliderValue by remember { mutableFloatStateOf((pagerState.currentPage + 1).toFloat()) }

        AlertDialog(
            onDismissRequest = { showJumpToPageDialog = false },
            title = {
                Text(
                    text = "পৃষ্ঠা নির্বাচন করুন",
                    fontFamily = banglaFont,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "পৃষ্ঠা: ${BanglaNumberUtils.toBanglaDigits(sliderValue.toInt())} / ${BanglaNumberUtils.toBanglaDigits(pages.size)}",
                        fontFamily = banglaFont,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGreen
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        valueRange = 1f..pages.size.toFloat(),
                        steps = if (pages.size > 2) pages.size - 2 else 0,
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicGreen,
                            activeTrackColor = IslamicGreen
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val targetIdx = (sliderValue.toInt() - 1).coerceIn(0, pages.size - 1)
                        scope.launch { pagerState.animateScrollToPage(targetIdx) }
                        showJumpToPageDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen)
                ) {
                    Text("যাও", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpToPageDialog = false }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }

    // Quick Ayah Action Sheet when tapping a verse on the page
    selectedAyahForAction?.let { ayah ->
        MushafAyahActionBottomSheet(
            surah = surah,
            ayah = ayah,
            onDismiss = { selectedAyahForAction = null },
            onPlay = {
                onPlayAyah(ayah)
                selectedAyahForAction = null
            },
            onToggleBookmark = {
                onToggleBookmark(ayah)
            }
        )
    }
}

/**
 * Renders a single authentic Mushaf page with top header, ornate border, 15/16 justified lines,
 * and bottom footer with global page number.
 */
@Composable
private fun MushafSinglePageCard(
    surah: QuranSurah,
    page: QuranMushafPage,
    layoutTheme: MushafLayoutTheme,
    settings: QuranSettings,
    playerState: SurahAudioPlayerState,
    onAyahClick: (QuranAyah) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    val primaryBorderColor = when (layoutTheme) {
        MushafLayoutTheme.MADANI_GOLD -> IslamicGold
        MushafLayoutTheme.NOORANI_EMERALD -> IslamicGreen
    }
    val secondaryBorderColor = when (layoutTheme) {
        MushafLayoutTheme.MADANI_GOLD -> IslamicGold.copy(alpha = 0.35f)
        MushafLayoutTheme.NOORANI_EMERALD -> IslamicGold.copy(alpha = 0.35f)
    }
    val pageBgColor = MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = pageBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(2.dp, primaryBorderColor.copy(alpha = 0.75f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp)
                .border(1.dp, secondaryBorderColor, RoundedCornerShape(12.dp))
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Ornate Top Page Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Juz / Para Name
                    Text(
                        text = "جُزْءُ ${QuranPaginationHelper.toArabicDigits(page.juzNumber)} • পারা ${BanglaNumberUtils.toBanglaDigits(page.juzNumber)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold,
                            color = primaryBorderColor,
                            fontSize = 11.sp
                        )
                    )

                    // Center: Format Title
                    Text(
                        text = when (layoutTheme) {
                            MushafLayoutTheme.MADANI_GOLD -> "مُصْحَفُ المَدِينَةِ (১৫ লাইন)"
                            MushafLayoutTheme.NOORANI_EMERALD -> "نُورَانِي حَافِظِي (১৬ লাইন)"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = ArabicFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )

                    // Surah Name in Arabic
                    Text(
                        text = "سُورَةُ ${surah.nameAr}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = ArabicFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = primaryBorderColor,
                            fontSize = 12.5.sp
                        )
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = secondaryBorderColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Page Body Content with scrolling if necessary
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // If Page 1 of the Surah: Show ornate Surah cartouche
                    if (page.pageNumberInSurah == 1) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = primaryBorderColor.copy(alpha = 0.12f),
                            border = BorderStroke(1.2.dp, primaryBorderColor.copy(alpha = 0.5f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp, horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${surah.revelationType} • আয়াত ${BanglaNumberUtils.toBanglaDigits(surah.totalAyat)}",
                                    fontFamily = banglaFont,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "سُورَةُ ${surah.nameAr}",
                                    fontFamily = ArabicFontFamily,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBorderColor
                                )
                                Text(
                                    text = "সূরা ${surah.nameBn}",
                                    fontFamily = banglaFont,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Bismillah banner (except Surah 9 and Surah 1)
                        if (surah.number != 9 && surah.number != 1) {
                            Text(
                                text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                fontFamily = ArabicFontFamily,
                                fontSize = 19.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )
                        }
                    }

                    // Continuous verses text for this page
                    val annotatedString = buildAnnotatedString {
                        page.ayahs.forEach { ayah ->
                            val isPlaying = playerState.isPlaying &&
                                    playerState.surahNumber == surah.number &&
                                    playerState.activeAyahNumber == ayah.ayahNumber

                            val startOffset = length
                            append(ayah.arabicText.trim())
                            append(" ")

                            // Ornate Verse Ending Symbol with Arabic numeral
                            val ornament = " ۝${QuranPaginationHelper.toArabicDigits(ayah.ayahNumber)} "
                            append(ornament)

                            addStringAnnotation(
                                tag = "MUSHAF_AYAH_CLICK",
                                annotation = ayah.ayahNumber.toString(),
                                start = startOffset,
                                end = length
                            )

                            if (isPlaying) {
                                addStyle(
                                    style = SpanStyle(
                                        background = IslamicGold.copy(alpha = 0.35f),
                                        color = primaryBorderColor,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    start = startOffset,
                                    end = length
                                )
                            }
                        }
                    }

                    ClickableText(
                        text = annotatedString,
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = ArabicFontFamily,
                            fontSize = 21.sp,
                            lineHeight = 40.sp,
                            textAlign = TextAlign.Justify,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 4.dp, vertical = 4.dp),
                        onClick = { offset ->
                            annotatedString.getStringAnnotations(
                                tag = "MUSHAF_AYAH_CLICK",
                                start = offset,
                                end = offset
                            ).firstOrNull()?.let { annotation ->
                                val ayahNum = annotation.item.toIntOrNull()
                                val clickedAyah = page.ayahs.find { it.ayahNumber == ayahNum }
                                if (clickedAyah != null) {
                                    onAyahClick(clickedAyah)
                                }
                            }
                        }
                    )
                }

                HorizontalDivider(
                    thickness = 1.dp,
                    color = secondaryBorderColor,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                // Ornate Bottom Page Footer: Page Number in Center
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "আয়াত ${BanglaNumberUtils.toBanglaDigits(page.startAyahNumber)}-${BanglaNumberUtils.toBanglaDigits(page.endAyahNumber)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.5.sp
                        )
                    )

                    // Page Number badge
                    Surface(
                        shape = CircleShape,
                        color = primaryBorderColor.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, primaryBorderColor.copy(alpha = 0.4f)),
                        modifier = Modifier.size(28.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = QuranPaginationHelper.toArabicDigits(page.globalMushafPageNumber),
                                fontFamily = ArabicFontFamily,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBorderColor
                            )
                        }
                    }

                    Text(
                        text = "পৃষ্ঠা ${BanglaNumberUtils.toBanglaDigits(page.globalMushafPageNumber)}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 10.5.sp
                        )
                    )
                }
            }
        }
    }
}

/**
 * Bottom sheet shown when tapping any Ayah on the Mushaf or Pure Arabic view
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MushafAyahActionBottomSheet(
    surah: QuranSurah,
    ayah: QuranAyah,
    onDismiss: () -> Unit,
    onPlay: () -> Unit,
    onToggleBookmark: () -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "সূরা ${surah.nameBn} • আয়াত নং ${BanglaNumberUtils.toBanglaDigits(ayah.ayahNumber)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            color = IslamicGreen
                        )
                    )
                    Text(
                        text = "আয়াত কুইক অ্যাকশন ও অনুবাদ",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )
                }

                IconButton(onClick = onDismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "বন্ধ করুন")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic Text Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f))
                    .padding(14.dp)
            ) {
                Text(
                    text = "${ayah.arabicText} ۝${QuranPaginationHelper.toArabicDigits(ayah.ayahNumber)}",
                    fontFamily = ArabicFontFamily,
                    fontSize = 22.sp,
                    lineHeight = 36.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bangla Translation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "অনুবাদ:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            color = IslamicGreen
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = ayah.translationBn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontFamily = banglaFont,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Play Audio
                Button(
                    onClick = onPlay,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("শুনুন", fontFamily = banglaFont)
                }

                // Copy Text
                OutlinedButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Quran Ayah", "${ayah.arabicText}\n\n${ayah.translationBn}")
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "আয়াত কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("কপি", fontFamily = banglaFont)
                }

                // Bookmark
                IconButton(
                    onClick = {
                        onToggleBookmark()
                        Toast.makeText(context, "বুকমার্ক আপডেট করা হয়েছে", Toast.LENGTH_SHORT).show()
                        onDismiss()
                    },
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                ) {
                    Icon(
                        imageVector = if (ayah.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "বুকমার্ক",
                        tint = if (ayah.isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}
