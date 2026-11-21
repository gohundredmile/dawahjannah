package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranTafsirAndTranslationProvider
import com.example.data.model.AyahExplanation
import com.example.data.model.AyahMatchSummary
import com.example.data.model.QuranAyah
import com.example.data.model.QuranTafsirSource
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.AyahAudioPlayerHelper
import com.example.util.LoopMode

/**
 * Result Dashboard for 'Explain This Ayah'.
 * Displays 100% locally verified Quran Ayah details & complete Tafsir from the in-app
 * Quran database (114 Surahs / 6,236 Ayahs), with Previous/Next Ayah navigation, alternate
 * matched candidate switching, editable OCR re-lookup, audio recitation, and Hifz mode.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AyahExplanationDetailView(
    ayah: AyahExplanation,
    audioPlayer: AyahAudioPlayerHelper,
    onRescan: () -> Unit,
    onNavigateBack: () -> Unit,
    onSelectSurahAyah: (surahNumber: Int, ayahNumber: Int) -> Unit,
    onRelookupText: (String) -> Unit,
    onOpenHolyQuran: ((Int?) -> Unit)? = null
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    // System back button returns to Camera Scanner first
    BackHandler { onRescan() }

    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val currentPositionMs by audioPlayer.currentPositionMs.collectAsState()
    val durationMs by audioPlayer.durationMs.collectAsState()
    val loopMode by audioPlayer.loopMode.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("আয়াত ও তাফসীর", "পূর্ণাঙ্গ তাফসীর", "শব্দার্থ ও সূত্র", "হিফয")

    var isMemorizeModeActive by remember { mutableStateOf(false) }
    var isAyahMemorized by remember(ayah.id) { mutableStateOf(false) }
    var fontSizeSp by remember { mutableFloatStateOf(26f) }
    val revealedWords = remember(ayah.id) { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // 1. Sticky Top Bar
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        IconButton(
                            onClick = onNavigateBack,
                            modifier = Modifier.testTag("explain_detail_back_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ফিরে যান")
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${ayah.surahNameBangla} (${ayah.surahNumber}:${ayah.ayahNumber})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${ayah.surahNameArabic} • ${ayah.revelationTypeBn} • মোট ${ayah.totalAyahsInSurah} আয়াত",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedButton(
                            onClick = onRescan,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("explain_rescan_btn")
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("পুনরায় স্ক্যান", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                        }

                        IconButton(
                            onClick = { shareAyahWithTafsir(context, ayah) },
                            modifier = Modifier.testTag("explain_share_btn")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "আয়াত ও তাফসীর শেয়ার")
                        }
                    }
                }
            }
        }

        // 2. Local Quran DB Verification Banner + Previous / Next Ayah Navigation
        item {
            VerifiedLocalDbBannerCard(
                ayah = ayah,
                banglaFont = banglaFont,
                onPreviousAyah = {
                    if (ayah.ayahNumber > 1) {
                        audioPlayer.stop()
                        onSelectSurahAyah(ayah.surahNumber, ayah.ayahNumber - 1)
                    }
                },
                onNextAyah = {
                    if (ayah.ayahNumber < ayah.totalAyahsInSurah) {
                        audioPlayer.stop()
                        onSelectSurahAyah(ayah.surahNumber, ayah.ayahNumber + 1)
                    }
                }
            )
        }

        // 3. Alternate Local Matches Strip (when frame contained multiple ayahs or close matches)
        if (ayah.alternateMatches.isNotEmpty()) {
            item {
                AlternateMatchesStrip(
                    candidates = ayah.alternateMatches,
                    banglaFont = banglaFont,
                    onSelectCandidate = { candidate ->
                        audioPlayer.stop()
                        onSelectSurahAyah(candidate.surahNumber, candidate.ayahNumber)
                    }
                )
            }
        }

        // 4. Extracted OCR Text Inspection + Inline Edit & Re-Lookup Card
        if (ayah.extractedRawOcrText.isNotBlank()) {
            item {
                EditableOcrVerificationCard(
                    rawOcrText = ayah.extractedRawOcrText,
                    banglaFont = banglaFont,
                    arabicFont = arabicFont,
                    onRelookup = { editedText ->
                        audioPlayer.stop()
                        onRelookupText(editedText)
                    }
                )
            }
        }

        // 5. Audio Recitation Player Strip
        if (ayah.audioUrl.isNotBlank()) {
            item {
                AyahAudioStripCard(
                    ayah = ayah,
                    audioPlayer = audioPlayer,
                    isPlaying = isPlaying,
                    currentPositionMs = currentPositionMs,
                    durationMs = durationMs,
                    loopMode = loopMode,
                    banglaFont = banglaFont
                )
            }
        }

        // 6. Primary Tab Bar
        item {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        modifier = Modifier.testTag("explain_tab_$index"),
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = banglaFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
        }

        // 7. Tab Content
        when (selectedTabIndex) {
            0 -> item {
                TabAyahAndQuickTafsirView(
                    ayah = ayah,
                    fontSizeSp = fontSizeSp,
                    onFontSizeChange = { fontSizeSp = it },
                    banglaFont = banglaFont,
                    arabicFont = arabicFont,
                    onOpenFullTafsirTab = { selectedTabIndex = 1 },
                    onOpenHolyQuran = onOpenHolyQuran
                )
            }
            1 -> item {
                TabFullTafsirAndContextView(
                    ayah = ayah,
                    banglaFont = banglaFont
                )
            }
            2 -> item {
                TabWordAndReferencesView(
                    ayah = ayah,
                    banglaFont = banglaFont,
                    arabicFont = arabicFont,
                    onSelectSurahAyah = { s, a ->
                        audioPlayer.stop()
                        onSelectSurahAyah(s, a)
                    }
                )
            }
            3 -> item {
                TabHifzMemorizationView(
                    ayah = ayah,
                    isMemorizeModeActive = isMemorizeModeActive,
                    onToggleMemorizeMode = { isMemorizeModeActive = !isMemorizeModeActive },
                    isAyahMemorized = isAyahMemorized,
                    onToggleMemorized = { isAyahMemorized = !isAyahMemorized },
                    revealedWords = revealedWords,
                    audioPlayer = audioPlayer,
                    loopMode = loopMode,
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
            }
        }
    }
}

@Composable
private fun VerifiedLocalDbBannerCard(
    ayah: AyahExplanation,
    banglaFont: FontFamily?,
    onPreviousAyah: () -> Unit,
    onNextAyah: () -> Unit
) {
    val emerald = Color(0xFF047857)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = emerald.copy(alpha = 0.10f)),
        border = BorderStroke(1.dp, emerald.copy(alpha = 0.42f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "লোকাল কুরআন ডাটাবেস যাচাইকৃত",
                        tint = emerald,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "লোকাল কুরআন ডাটাবেস থেকে যাচাইকৃত আয়াত",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = emerald,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "${ayah.matchSourceLabelBn} • ${ayah.scanDurationMs}ms",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                            fontFamily = banglaFont
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = emerald
                ) {
                    Text(
                        text = "${ayah.matchConfidencePercent}% ম্যাচ",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (ayah.surahNumber in 1..114 && ayah.totalAyahsInSurah > 1) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = emerald.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onPreviousAyah,
                        enabled = ayah.ayahNumber > 1,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("explain_prev_ayah_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "পূর্ববর্তী আয়াত",
                            modifier = Modifier.size(18.dp)
                        )
                        Text("পূর্ববর্তী আয়াত", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                    }

                    Text(
                        text = "আয়াত ${ayah.ayahNumber} / ${ayah.totalAyahsInSurah}",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )

                    OutlinedButton(
                        onClick = onNextAyah,
                        enabled = ayah.ayahNumber < ayah.totalAyahsInSurah,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("explain_next_ayah_btn")
                    ) {
                        Text("পরবর্তী আয়াত", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "পরবর্তী আয়াত",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlternateMatchesStrip(
    candidates: List<AyahMatchSummary>,
    banglaFont: FontFamily?,
    onSelectCandidate: (AyahMatchSummary) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Text(
            text = "পৃষ্ঠায় শনাক্তকৃত অন্যান্য কাছাকাছি আয়াত (ট্যাপ করে পরিবর্তন করুন):",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = banglaFont
        )
        Spacer(modifier = Modifier.height(6.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(candidates, key = { "${it.surahNumber}_${it.ayahNumber}" }) { candidate ->
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
                    modifier = Modifier
                        .clickable { onSelectCandidate(candidate) }
                        .testTag("explain_alt_match_${candidate.surahNumber}_${candidate.ayahNumber}")
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Text(
                            text = "সূরা ${candidate.surahNameBn} (${candidate.surahNumber}:${candidate.ayahNumber}) • ${candidate.confidencePercent}%",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                        if (candidate.banglaSnippet.isNotBlank()) {
                            Text(
                                text = candidate.banglaSnippet,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.width(210.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EditableOcrVerificationCard(
    rawOcrText: String,
    banglaFont: FontFamily?,
    arabicFont: FontFamily?,
    onRelookup: (String) -> Unit
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    var isEditing by remember { mutableStateOf(false) }
    var editableQuery by remember(rawOcrText) { mutableStateOf(rawOcrText) }

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "স্ক্যানকৃত টেক্সট দেখুন / সংশোধন করে পুনরায় খুঁজুন",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (isExpanded) "সংকুচিত করুন" else "বিস্তারিত দেখুন",
                    modifier = Modifier.size(20.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    if (isEditing) {
                        OutlinedTextField(
                            value = editableQuery,
                            onValueChange = { editableQuery = it },
                            label = { Text("আরবি / বাংলা / সূরা:আয়াত লিখুন", fontFamily = banglaFont) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("explain_relookup_input"),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(onClick = { isEditing = false }) {
                                Text("বাতিল", fontFamily = banglaFont)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    if (editableQuery.isNotBlank()) {
                                        isEditing = false
                                        onRelookup(editableQuery.trim())
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                                modifier = Modifier.testTag("explain_relookup_submit_btn")
                            ) {
                                Text("লোকাল কুরআনে খুঁজুন", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                            }
                        }
                    } else {
                        Text(
                            text = rawOcrText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = if (rawOcrText.any { it in '\u0600'..'\u06FF' }) arabicFont else banglaFont
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { isEditing = true },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("সংশোধন করে খুঁজুন", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                            }
                            OutlinedButton(
                                onClick = {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("OCR Text", rawOcrText))
                                    Toast.makeText(context, "স্ক্যানকৃত টেক্সট কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                },
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("কপি", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AyahAudioStripCard(
    ayah: AyahExplanation,
    audioPlayer: AyahAudioPlayerHelper,
    isPlaying: Boolean,
    currentPositionMs: Int,
    durationMs: Int,
    loopMode: LoopMode,
    banglaFont: FontFamily?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
        ),
        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = IslamicGold,
                        modifier = Modifier
                            .size(44.dp)
                            .clickable {
                                if (isPlaying) audioPlayer.pause() else audioPlayer.play(ayah.audioUrl)
                            }
                            .testTag("explain_audio_play_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "পজ" else "তিলাওয়াত শুনুন",
                                tint = Color.Black,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "তিলাওয়াত: ${ayah.reciterNameBn}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = if (isPlaying) "তিলাওয়াত চলছে..." else "সঠিক তাজবীদ শুনতে প্লে চাপুন",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                    modifier = Modifier.clickable {
                        val nextMode = when (loopMode) {
                            LoopMode.ONCE -> LoopMode.THREE_TIMES
                            LoopMode.THREE_TIMES -> LoopMode.FIVE_TIMES
                            LoopMode.FIVE_TIMES -> LoopMode.INFINITE
                            LoopMode.INFINITE -> LoopMode.ONCE
                        }
                        audioPlayer.setLoopMode(nextMode)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Repeat,
                            contentDescription = "লুপ মোড",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = loopMode.titleBn,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            if (durationMs > 0) {
                Slider(
                    value = currentPositionMs.toFloat().coerceIn(0f, durationMs.toFloat()),
                    onValueChange = { audioPlayer.seekTo(it.toInt()) },
                    valueRange = 0f..durationMs.toFloat(),
                    colors = SliderDefaults.colors(
                        thumbColor = IslamicGold,
                        activeTrackColor = IslamicGold
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(28.dp)
                )
            }
        }
    }
}

/**
 * Tab 0: Exact Local Ayah Details (Uthmani Arabic + Bengali Pronunciation + Bengali Translation
 * + Direct Local Tafsir Card immediately visible).
 */
@Composable
private fun TabAyahAndQuickTafsirView(
    ayah: AyahExplanation,
    fontSizeSp: Float,
    onFontSizeChange: (Float) -> Unit,
    banglaFont: FontFamily?,
    arabicFont: FontFamily?,
    onOpenFullTafsirTab: () -> Unit,
    onOpenHolyQuran: ((Int?) -> Unit)?
) {
    val context = LocalContext.current
    var isTafsirExpanded by remember(ayah.id) { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Exact Uthmani Arabic Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ),
            border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.48f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.16f)
                    ) {
                        Text(
                            text = "${ayah.surahNameBangla} • আয়াত ${ayah.ayahNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (onOpenHolyQuran != null && ayah.surahNumber in 1..114) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = IslamicGold.copy(alpha = 0.16f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
                                modifier = Modifier
                                    .clickable { onOpenHolyQuran(ayah.surahNumber) }
                                    .testTag("explain_open_quran_btn")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "কুরআনে পড়ুন",
                                        tint = IslamicGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "কুরআনে পড়ুন",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = IslamicGold,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText("Arabic Ayah", ayah.arabicText))
                                Toast.makeText(context, "আরবি আয়াত কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "আরবি কপি করুন", modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = ayah.arabicText,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = arabicFont,
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * 1.9f).sp,
                        textDirection = TextDirection.ContentOrRtl
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 10.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                ) {
                    Icon(Icons.Default.FormatSize, contentDescription = "হরফের আকার", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = fontSizeSp,
                        onValueChange = onFontSizeChange,
                        valueRange = 20f..38f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicGold,
                            activeTrackColor = IslamicGold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${fontSizeSp.toInt()} sp",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Verified Bengali Pronunciation Card
        if (ayah.transliterationBn.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "সহজ বাংলা উচ্চারণ:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ayah.transliterationBn,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Exact Local Bengali Translation Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            ),
            border = BorderStroke(0.9.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "বাংলা অনুবাদ (ড. আবু বকর মুহাম্মাদ যাকারিয়া — লোকাল ডাটাবেস):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.banglaTranslation,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // Immediate Local Tafsir Card (Directly visible on Tab 0!)
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("explain_quick_tafsir_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "তাফসীর ও ব্যাখ্যা (লোকাল কুরআন ডাটাবেস)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                    IconButton(
                        onClick = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Ayah Tafsir", ayah.tafsirBn))
                            Toast.makeText(context, "তাফসীর কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "তাফসীর কপি করুন", modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                val previewLimit = 480
                val isLong = ayah.tafsirBn.length > previewLimit
                val displayedTafsir = if (isTafsirExpanded || !isLong) {
                    ayah.tafsirBn
                } else {
                    ayah.tafsirBn.take(previewLimit).substringBeforeLast(" ") + "..."
                }

                Text(
                    text = displayedTafsir,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 25.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLong) {
                        TextButton(onClick = { isTafsirExpanded = !isTafsirExpanded }) {
                            Text(
                                text = if (isTafsirExpanded) "সংক্ষিপ্ত করুন" else "সম্পূর্ণ তাফসীর পড়ুন",
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    OutlinedButton(
                        onClick = onOpenFullTafsirTab,
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "অন্যান্য তাফসীর ও শানে নুযূল →",
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // English Translation Card (shown only when real English translation is available)
        if (ayah.englishTranslation.isNotBlank() && !ayah.englishTranslation.startsWith("[Saheeh International] Verse")) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "English Translation (Sahih International):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = ayah.englishTranslation,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

/**
 * Tab 1: Full Tafsir with 4 scholarly perspectives + authentic Surah Shan-e-Nuzul.
 */
@Composable
private fun TabFullTafsirAndContextView(
    ayah: AyahExplanation,
    banglaFont: FontFamily?
) {
    val context = LocalContext.current
    var selectedTafsirSource by remember(ayah.id) { mutableStateOf(QuranTafsirSource.DR_ZAKARIA) }

    val quranAyahModel = remember(ayah.id) {
        QuranAyah(
            surahNumber = ayah.surahNumber,
            ayahNumber = ayah.ayahNumber,
            arabicText = ayah.arabicText,
            pronunciationBn = ayah.transliterationBn,
            translationBn = ayah.banglaTranslation,
            translationZakaria = ayah.banglaTranslation,
            tafsirText = ayah.rawLocalTafsirBn.ifBlank { ayah.tafsirBn }
        )
    }

    val tafsirContent = remember(ayah.id, selectedTafsirSource) {
        if (selectedTafsirSource == QuranTafsirSource.DR_ZAKARIA) {
            ayah.tafsirBn
        } else {
            QuranTafsirAndTranslationProvider.getTafsirText(quranAyahModel, selectedTafsirSource)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tafsir Source Selector Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val sources = listOf(
                QuranTafsirSource.DR_ZAKARIA to "ড. যাকারিয়া (লোকাল)",
                QuranTafsirSource.IBN_KATHIR to "ইবনে কাসীর",
                QuranTafsirSource.AHSANUL_BAYAN to "আহসানুল বায়ান",
                QuranTafsirSource.JALALAYN to "তাফসীরে জালালাইন"
            )
            items(sources) { (src, label) ->
                FilterChip(
                    selected = selectedTafsirSource == src,
                    onClick = { selectedTafsirSource = src },
                    label = { Text(label, fontFamily = banglaFont, style = MaterialTheme.typography.labelSmall) },
                    leadingIcon = if (selectedTafsirSource == src) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicGold,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        // Complete Tafsir Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "পূর্ণাঙ্গ তাফসীর • ${ayah.surahNameBangla} (${ayah.surahNumber}:${ayah.ayahNumber})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                    IconButton(
                        onClick = {
                            val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            cm.setPrimaryClip(ClipData.newPlainText("Full Tafsir", tafsirContent))
                            Toast.makeText(context, "তাফসীর কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "কপি করুন", modifier = Modifier.size(18.dp))
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = tafsirContent,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 26.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // Authentic Shan-e-Nuzul & Context Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "নাযিলের প্রেক্ষাপট ও শানে নুযূল:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.contextBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

/**
 * Tab 2: Word-by-word breakdown + Adjacent / Related Verses + Related Hadiths.
 */
@Composable
private fun TabWordAndReferencesView(
    ayah: AyahExplanation,
    banglaFont: FontFamily?,
    arabicFont: FontFamily?,
    onSelectSurahAyah: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (ayah.wordByWord.isNotEmpty()) {
            Text(
                text = "আয়াতের শব্দভিত্তিক বিশ্লেষণ (${ayah.wordByWord.size}টি শব্দ):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )

            ayah.wordByWord.forEachIndexed { index, word ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = IslamicGold.copy(alpha = 0.16f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = word.bengaliMeaning,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                                if (word.englishMeaning.isNotBlank()) {
                                    Text(
                                        text = word.englishMeaning,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Text(
                            text = word.arabicWord,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = arabicFont,
                                fontSize = 24.sp,
                                textDirection = TextDirection.ContentOrRtl
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.End,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }
        }

        if (ayah.relatedVerses.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "প্রাসঙ্গিক ও পার্শ্ববর্তী আয়াতসমূহ:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )

            ayah.relatedVerses.forEach { relVerse ->
                val parsedRef = remember(relVerse.ayahRef) {
                    val parts = relVerse.ayahRef.split(":")
                    val s = parts.getOrNull(0)?.filter { it.isDigit() }?.toIntOrNull()
                    val a = parts.getOrNull(1)?.split("-")?.firstOrNull()?.filter { it.isDigit() }?.toIntOrNull()
                    if (s != null && s in 1..114 && a != null && a >= 1) s to a else null
                }

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (parsedRef != null) {
                                Modifier.clickable { onSelectSurahAyah(parsedRef.first, parsedRef.second) }
                            } else {
                                Modifier
                            }
                        )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = "${relVerse.surahNameBn} (${relVerse.ayahRef})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = relVerse.arabicText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontFamily = arabicFont,
                                fontSize = 20.sp,
                                lineHeight = 32.sp,
                                textDirection = TextDirection.ContentOrRtl
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = relVerse.translationBn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        if (ayah.relatedHadiths.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "সম্পর্কিত সহীহ হাদীস ও ফযীলত:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )

            ayah.relatedHadiths.forEach { hadith ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
                    ),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${hadith.sourceBn} • ${hadith.narratorBn}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = hadith.gradeBn,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = hadith.textBn,
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tab 3: Interactive Hifz & Memorization Trainer.
 */
@Composable
private fun TabHifzMemorizationView(
    ayah: AyahExplanation,
    isMemorizeModeActive: Boolean,
    onToggleMemorizeMode: () -> Unit,
    isAyahMemorized: Boolean,
    onToggleMemorized: () -> Unit,
    revealedWords: MutableMap<Int, Boolean>,
    audioPlayer: AyahAudioPlayerHelper,
    loopMode: LoopMode,
    banglaFont: FontFamily?,
    arabicFont: FontFamily?
) {
    val context = LocalContext.current
    val words = remember(ayah.arabicText) {
        ayah.arabicText.split(" ").filter { it.isNotBlank() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isMemorizeModeActive) IslamicGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.2.dp, if (isMemorizeModeActive) IslamicGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "হিফয ও মুখস্থ অনুশীলন মোড",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (isMemorizeModeActive) IslamicGold else MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "শব্দ ঢেকে পরীক্ষা করুন এবং লুপ অডিও শুনে সহজে মুখস্থ করুন",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }

                Button(
                    onClick = onToggleMemorizeMode,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isMemorizeModeActive) IslamicGold else MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = if (isMemorizeModeActive) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = null,
                        tint = if (isMemorizeModeActive) Color.Black else Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (isMemorizeModeActive) "ব্লার অন" else "চালু করুন",
                        color = if (isMemorizeModeActive) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isMemorizeModeActive) "শব্দের উপর চাপ দিলে স্পষ্ট হবে:" else "আরবি মূল পাঠ:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                    if (isMemorizeModeActive) {
                        Text(
                            text = "সব উন্মোচন করুন",
                            style = MaterialTheme.typography.labelSmall,
                            color = IslamicGold,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            modifier = Modifier.clickable {
                                words.indices.forEach { idx -> revealedWords[idx] = true }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState(), reverseScrolling = true),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    words.forEachIndexed { index, word ->
                        val isRevealed = revealedWords[index] == true || !isMemorizeModeActive
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isRevealed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                            else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            border = BorderStroke(
                                0.8.dp,
                                if (isRevealed) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else IslamicGold
                            ),
                            modifier = Modifier.clickable {
                                if (isMemorizeModeActive) {
                                    revealedWords[index] = !(revealedWords[index] ?: false)
                                }
                            }
                        ) {
                            Text(
                                text = word,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = arabicFont,
                                    fontSize = 24.sp,
                                    textDirection = TextDirection.ContentOrRtl
                                ),
                                color = if (isRevealed) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .then(if (!isRevealed) Modifier.blur(6.dp) else Modifier)
                            )
                        }
                    }
                }
            }
        }

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(LoopMode.entries) { mode ->
                FilterChip(
                    selected = loopMode == mode,
                    onClick = { audioPlayer.setLoopMode(mode) },
                    label = { Text(mode.titleBn, fontFamily = banglaFont) },
                    leadingIcon = if (loopMode == mode) {
                        { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                    } else null,
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = IslamicGold,
                        selectedLabelColor = Color.Black
                    )
                )
            }
        }

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isAyahMemorized) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.2.dp, if (isAyahMemorized) MaterialTheme.colorScheme.primary else IslamicGold),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onToggleMemorized()
                    if (!isAyahMemorized) {
                        Toast.makeText(context, "মাশাআল্লাহ! আয়াতটি আপনার মুখস্থ তালিকায় সংরক্ষিত হয়েছে।", Toast.LENGTH_LONG).show()
                    }
                }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isAyahMemorized) Icons.Default.CheckCircle else Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isAyahMemorized) Color.White else IslamicGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAyahMemorized) "আলহামদুলিল্লাহ! আয়াতটি মুখস্থ সম্পন্ন হয়েছে" else "মুখস্থ সম্পন্ন হিসেবে চিহ্নিত করুন",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isAyahMemorized) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

private fun shareAyahWithTafsir(context: Context, ayah: AyahExplanation) {
    val tafsirSnippet = if (ayah.tafsirBn.length > 900) {
        ayah.tafsirBn.take(900).substringBeforeLast(" ") + "..."
    } else {
        ayah.tafsirBn
    }
    val shareText = buildString {
        appendLine("${ayah.surahNameBangla} (${ayah.surahNumber}:${ayah.ayahNumber})")
        appendLine()
        appendLine(ayah.arabicText)
        if (ayah.transliterationBn.isNotBlank()) {
            appendLine()
            appendLine("উচ্চারণ: ${ayah.transliterationBn}")
        }
        appendLine()
        appendLine("বাংলা অর্থ: ${ayah.banglaTranslation}")
        if (tafsirSnippet.isNotBlank()) {
            appendLine()
            appendLine("তাফসীর: $tafsirSnippet")
        }
        appendLine()
        append("— দা'ওয়াহ টু জান্নাহ্ অ্যাপ")
    }
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, shareText)
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(sendIntent, "আয়াত ও তাফসীর শেয়ার করুন"))
}
