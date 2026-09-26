package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.PersonalDuaCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.entity.BookmarkEntity
import com.example.data.model.DuaConstructionMode
import com.example.data.model.DuaSourceMapItem
import com.example.data.model.DuaSourceType
import com.example.data.model.PersonalDuaBlueprint
import com.example.data.model.PropheticDuaItem
import com.example.data.model.QuranicDuaItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.PersonalDuaBuilderService
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDuaBuilderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val service = remember { PersonalDuaBuilderService(context) }
    val db = remember { AppDatabase.getDatabase(context) }
    val bookmarkDao = remember { db.bookmarkDao() }

    // Bookmark observation
    val allBookmarks by bookmarkDao.getAllBookmarks().collectAsState(initial = emptyList())
    val savedArchitectDuas = remember(allBookmarks) {
        allBookmarks.filter { it.type == "DUA_ARCHITECT" || it.categoryBn == "ব্যক্তিগত দু'আ আর্কিটেক্ট" }
    }

    // Search and blueprint state
    var userPromptInput by remember { mutableStateOf("My father is sick and I am worried.") }
    var activePrompt by remember { mutableStateOf("My father is sick and I am worried.") }
    var selectedMode by remember { mutableStateOf(DuaConstructionMode.CURATED_COLLECTION) }
    var isBuilding by remember { mutableStateOf(false) }
    var currentBlueprint by remember { mutableStateOf<PersonalDuaBlueprint?>(null) }

    // Display & Language Options
    var isEnglishLanguage by remember { mutableStateOf(false) } // false: Bengali, true: English
    var arabicFontSize by remember { mutableIntStateOf(24) }
    var selectedTabFilter by remember { mutableIntStateOf(0) } // 0: Architect Hero, 1: Quran, 2: Hadith, 3: Timings & Etiquettes

    // Modals and Dialogs
    var showHelpDialog by remember { mutableStateOf(false) }
    var showSavedDuasSheet by remember { mutableStateOf(false) }
    var inspectingSource by remember { mutableStateOf<DuaSourceMapItem?>(null) }

    // Practice counter dialog state
    var practiceDuaTitle by remember { mutableStateOf<String?>(null) }
    var practiceDuaArabic by remember { mutableStateOf<String?>(null) }
    var practiceDuaTarget by remember { mutableIntStateOf(3) }
    var practiceCurrentCount by remember { mutableIntStateOf(0) }

    // Text-To-Speech
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var isTtsPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        var speechRef: TextToSpeech? = null
        val speech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                // Try Arabic, fallback to English
                val res = speechRef?.setLanguage(Locale("ar"))
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    speechRef?.setLanguage(Locale.ENGLISH)
                }
            }
        }
        speechRef = speech
        tts = speech
        onDispose {
            speech.stop()
            speech.shutdown()
        }
    }

    fun playAudio(text: String) {
        tts?.let { speech ->
            if (speech.isSpeaking) {
                speech.stop()
                isTtsPlaying = false
            } else {
                speech.speak(text, TextToSpeech.QUEUE_FLUSH, null, "DuaTTS")
                isTtsPlaying = true
            }
        }
    }

    fun triggerVibration() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_CLICK))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator?.vibrate(VibrationEffect.createOneShot(35, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(35)
                }
            }
        } catch (_: Exception) {}
    }

    // Initial build
    LaunchedEffect(Unit) {
        isBuilding = true
        currentBlueprint = service.buildDuaBlueprint(activePrompt, selectedMode)
        isBuilding = false
    }

    fun executeBuild(query: String, mode: DuaConstructionMode = selectedMode) {
        if (query.isBlank()) return
        keyboardController?.hide()
        activePrompt = query
        selectedMode = mode
        isBuilding = true
        coroutineScope.launch {
            currentBlueprint = service.buildDuaBlueprint(query, mode)
            isBuilding = false
        }
    }

    val currentIsBookmarked = remember(currentBlueprint, savedArchitectDuas) {
        val bp = currentBlueprint ?: return@remember false
        savedArchitectDuas.any { it.id == "architect_${bp.id}" || it.titleBn == bp.scenarioTitleBn }
    }

    fun toggleSaveCurrentBlueprint() {
        val bp = currentBlueprint ?: return
        coroutineScope.launch {
            val entityId = "architect_${bp.id}"
            if (currentIsBookmarked) {
                bookmarkDao.removeBookmark(entityId)
                Toast.makeText(context, "সংরক্ষণ তালিকা থেকে অপসারিত", Toast.LENGTH_SHORT).show()
            } else {
                val entity = BookmarkEntity(
                    id = entityId,
                    type = "DUA_ARCHITECT",
                    titleBn = bp.scenarioTitleBn,
                    subtitleBn = "${bp.detectedIntentBn} • ${bp.mode.titleBn}",
                    categoryBn = "ব্যক্তিগত দু'আ আর্কিটেক্ট",
                    arabicText = bp.curatedArabicText,
                    pronunciationBn = "",
                    meaningBn = bp.curatedTranslationBn,
                    detailsBn = bp.spiritualComfortBn,
                    referenceBn = bp.sources.joinToString(", ") { it.referenceText },
                    bookmarkedAt = System.currentTimeMillis()
                )
                bookmarkDao.addBookmark(entity)
                Toast.makeText(context, "❤️ পার্সোনাল দো'আ সফলভাবে সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 3.dp,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Dua Architect",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = IslamicGold.copy(alpha = 0.2f),
                                border = BorderStroke(0.5.dp, IslamicGold)
                            ) {
                                Text(
                                    text = "প্রামাণ্য দো'আ",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Text(
                            text = if (isEnglishLanguage) "Personal Dua Builder — Quran & Sunnah" else "কুরআন ও সহীহ হাদীসের ভিত্তিতে ব্যক্তিগত দো'আ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    // Language Toggle
                    IconButton(onClick = { isEnglishLanguage = !isEnglishLanguage }) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                            modifier = Modifier.size(30.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = if (isEnglishLanguage) "বাং" else "EN",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Font Size Cycle Button
                    IconButton(onClick = {
                        arabicFontSize = if (arabicFontSize >= 32) 22 else arabicFontSize + 2
                    }) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "ফন্ট সাইজ পরিবর্তন",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Saved Duas Sheet Trigger
                    IconButton(onClick = { showSavedDuasSheet = true }) {
                        Box {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "সংরক্ষিত দো'আসমূহ",
                                tint = if (savedArchitectDuas.isNotEmpty()) Color(0xFFE11D48) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (savedArchitectDuas.isNotEmpty()) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFE11D48),
                                    modifier = Modifier
                                        .size(12.dp)
                                        .align(Alignment.TopEnd)
                                ) {}
                            }
                        }
                    }

                    // Info Dialog Trigger
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "নীতিমালা ও নির্দেশিকা",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp),
            contentPadding = PaddingValues(top = 10.dp, bottom = 54.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // PROMPT INPUT & SEARCH CARD
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isEnglishLanguage) "What would you like to make Dua for?" else "আপনার মনের আরজি বা পরিস্থিতি লিখুন:",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    text = "১০০% প্রামাণ্য উৎস",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isEnglishLanguage)
                                "Describe your trial, fear, goal or emotion. Authentic Duas will be structured exclusively from Quran & Sahih Hadith."
                            else
                                "আপনার জীবনের সংকট, ভয়, আবেগ বা প্রয়োজন লিখলে কুরআন ও সহীহ হাদীসের প্রামাণ্য দো'আ সমন্বয়ে প্রস্তুত হবে।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = userPromptInput,
                            onValueChange = { userPromptInput = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text(
                                    text = if (isEnglishLanguage)
                                        "e.g., I am worried about my future / looking for a job..."
                                    else
                                        "যেমন: I am worried about my future / বাবার অসুস্থতা / ঋণমুক্তি...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    fontFamily = banglaFont
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = false,
                            maxLines = 3,
                            trailingIcon = {
                                if (userPromptInput.isNotBlank()) {
                                    IconButton(onClick = { userPromptInput = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "মুছে ফেলুন")
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.primary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { executeBuild(userPromptInput, selectedMode) })
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { executeBuild(userPromptInput, selectedMode) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(10.dp),
                                enabled = !isBuilding && userPromptInput.isNotBlank(),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                if (isBuilding) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isEnglishLanguage) "Architecting..." else "দো'আ প্রস্তুত হচ্ছে...", fontFamily = banglaFont)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(if (isEnglishLanguage) "Build Personal Dua" else "দো'আ আর্কিটেক্ট প্রস্তুত করুন", fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                                }
                            }
                        }
                    }
                }
            }

            // POPULAR SITUATION PRESETS CHIPS
            item {
                Column {
                    Text(
                        text = if (isEnglishLanguage) "Popular Situations & Needs:" else "জনপ্রিয় পরিস্থিতি নির্বাচন করুন:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PersonalDuaCatalog.presets.forEach { preset ->
                            val isSelected = activePrompt == preset.samplePrompt
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    userPromptInput = preset.samplePrompt
                                    executeBuild(preset.samplePrompt, selectedMode)
                                },
                                label = {
                                    Text(
                                        text = if (isEnglishLanguage) preset.titleEn else preset.titleBn,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontFamily = banglaFont,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                leadingIcon = {
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp))
                                    }
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )
                        }
                    }
                }
            }

            // DUA CONSTRUCTION MODES SELECTOR (Mode A, Mode B, Mode C)
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isEnglishLanguage) "Dua Construction Mode:" else "দো'আ বিন্যাস কাঠামো (Mode):",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = if (isEnglishLanguage) selectedMode.titleEn else selectedMode.titleBn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = IslamicGold,
                                fontFamily = banglaFont
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            DuaConstructionMode.entries.forEach { mode ->
                                val isModeActive = selectedMode == mode
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isModeActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(
                                        0.6.dp,
                                        if (isModeActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable {
                                            selectedMode = mode
                                            currentBlueprint?.let { bp ->
                                                currentBlueprint = service.applyConstructionMode(bp, mode)
                                            }
                                        }
                                ) {
                                    Column(
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = when (mode) {
                                                DuaConstructionMode.AUTHENTIC_SINGLE -> if (isEnglishLanguage) "Mode A: Direct" else "মোড ক: একক"
                                                DuaConstructionMode.CURATED_COLLECTION -> if (isEnglishLanguage) "Mode B: Curated" else "মোড খ: সংকলিত"
                                                DuaConstructionMode.PERSONALIZED_FLOW -> if (isEnglishLanguage) "Mode C: Flow" else "মোড গ: বিন্যাস"
                                            },
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isModeActive) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            textAlign = TextAlign.Center,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }

                        Text(
                            text = if (isEnglishLanguage) {
                                when (selectedMode) {
                                    DuaConstructionMode.AUTHENTIC_SINGLE -> "Mode A: Returns an exact authentic Dua as found in Quran/Hadith without combining."
                                    DuaConstructionMode.CURATED_COLLECTION -> "Mode B: Combines multiple authentic supplications addressing various aspects of your need."
                                    DuaConstructionMode.PERSONALIZED_FLOW -> "Mode C: Frames authentic supplications in a structured personal devotional prayer."
                                }
                            } else {
                                selectedMode.descriptionBn
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            // DUA ARCHITECT BLUEPRINT CONTENT
            val blueprint = currentBlueprint
            if (blueprint != null) {

                // 1. HERO CARD: "🌙 Your Personalized Dua"
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, IslamicGold),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        listOf(
                                            IslamicGold.copy(alpha = 0.12f),
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.04f),
                                            MaterialTheme.colorScheme.surface
                                        )
                                    )
                                )
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Header Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "🌙",
                                        fontSize = 18.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isEnglishLanguage) "Your Personalized Dua" else "আপনার ব্যক্তিগত দু'আ",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = IslamicGold.copy(alpha = 0.25f),
                                    border = BorderStroke(0.6.dp, IslamicGold)
                                ) {
                                    Text(
                                        text = if (isEnglishLanguage) selectedMode.titleEn.substringBefore("—").trim() else selectedMode.titleBn.substringBefore("—").trim(),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            // Scenario Title
                            Text(
                                text = if (isEnglishLanguage) blueprint.scenarioTitleEn else blueprint.scenarioTitleBn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )

                            // Intent and Context Badges
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                ) {
                                    Text(
                                        text = "🎯 ${if (isEnglishLanguage) blueprint.detectedIntentEn else blueprint.detectedIntentBn}",
                                        fontSize = 10.sp,
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                ) {
                                    Text(
                                        text = "❤️ ${if (isEnglishLanguage) blueprint.emotionalContextEn else blueprint.emotionalContextBn}",
                                        fontSize = 10.sp,
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                                ) {
                                    Text(
                                        text = "👥 ${if (isEnglishLanguage) blueprint.peopleInvolvedEn else blueprint.peopleInvolvedBn}",
                                        fontSize = 10.sp,
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            // ARABIC DISPLAY BOX
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.6f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Arabic (মূল আরবী দো'আ)",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicGold,
                                            fontFamily = banglaFont
                                        )
                                        Row {
                                            IconButton(
                                                onClick = { playAudio(blueprint.curatedArabicText) },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    imageVector = if (isTtsPlaying) Icons.Default.Stop else Icons.Default.VolumeUp,
                                                    contentDescription = "উচ্চারণ শুনুন",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            IconButton(
                                                onClick = {
                                                    copyText(context, blueprint.curatedArabicText, "আরবী পাঠ কপি করা হয়েছে")
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.ContentCopy,
                                                    contentDescription = "কপি",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(15.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = blueprint.curatedArabicText,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontSize = arabicFontSize.sp,
                                            lineHeight = (arabicFontSize * 1.65).sp,
                                            textDirection = TextDirection.Rtl,
                                            textAlign = TextAlign.Right
                                        ),
                                        fontFamily = arabicFont,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

                            // MEANING DISPLAY BOX
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = if (isEnglishLanguage) "Meaning (Faithful Translation):" else "Meaning (অর্থ ও ভাবানুবাদ):",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = banglaFont
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "“${if (isEnglishLanguage && blueprint.curatedTranslationEn.isNotBlank()) blueprint.curatedTranslationEn else blueprint.curatedTranslationBn}”",
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            // WHY THESE DUAS WERE SELECTED
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = Color(0xFF0284C7),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (isEnglishLanguage) "Why These Duas Were Selected:" else "কেন এই দো'আগুলো নির্বাচন করা হয়েছে:",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0284C7),
                                            fontFamily = banglaFont
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (isEnglishLanguage && blueprint.whySelectedEn.isNotBlank()) blueprint.whySelectedEn else blueprint.whySelectedBn,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            // SOURCES SECTION WITH "VIEW SOURCE" TRIGGER
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = if (isEnglishLanguage) "Sources (Authentic Knowledge Base):" else "Sources (প্রামাণ্য মূল সনদ ও সূত্র):",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )

                                blueprint.sources.forEach { sourceItem ->
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.surface,
                                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 10.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Row(
                                                modifier = Modifier.weight(1f),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = if (sourceItem.sourceType == DuaSourceType.QURAN) Icons.Default.MenuBook else Icons.Default.Mosque,
                                                    contentDescription = null,
                                                    tint = if (sourceItem.sourceType == DuaSourceType.QURAN) Color(0xFF059669) else Color(0xFF4F46E5),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Column {
                                                    Text(
                                                        text = sourceItem.referenceText,
                                                        style = MaterialTheme.typography.labelSmall,
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.onSurface,
                                                        fontFamily = banglaFont
                                                    )
                                                    Text(
                                                        text = "${sourceItem.gradingBn} • ${if (isEnglishLanguage) sourceItem.titleEn else sourceItem.titleBn}",
                                                        fontSize = 10.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontFamily = banglaFont
                                                    )
                                                }
                                            }

                                            OutlinedButton(
                                                onClick = { inspectingSource = sourceItem },
                                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp),
                                                shape = RoundedCornerShape(6.dp)
                                            ) {
                                                Text(
                                                    text = if (isEnglishLanguage) "View Source" else "উৎস দেখুন",
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    fontFamily = banglaFont
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            // AUTHENTICITY NOTE
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF059669).copy(alpha = 0.08f),
                                border = BorderStroke(0.6.dp, Color(0xFF059669).copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = if (isEnglishLanguage) "Authenticity Note:" else "প্রামাণ্যতার নিশ্চয়তা নোট:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        fontFamily = banglaFont
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = if (isEnglishLanguage) {
                                            "This Dua was built exclusively from Qur’anic verses and authentic Prophetic supplications available in the application's verified offline database." +
                                                    if (blueprint.mode != DuaConstructionMode.AUTHENTIC_SINGLE) " This is a curated combination of authentic supplications; the complete combined wording is not presented as a single Qur’anic verse or Hadith." else ""
                                        } else {
                                            "এই দো'আটি অ্যাপের অফলাইন সংরক্ষিত পবিত্র কুরআন ও সহীহ হাদীসের প্রামাণ্য দলিলের ভিত্তিতে তৈরি করা হয়েছে।" +
                                                    if (blueprint.mode != DuaConstructionMode.AUTHENTIC_SINGLE) " এটি একাধিক প্রামাণ্য দো'আর সমন্বিত সংকলন; সম্পূর্ণ যৌথ বাক্যটি কোনো একক আয়াত বা হাদীস হিসেবে দাবি করা হয়নি।" else ""
                                        },
                                        style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                            // ACTION TOOLBAR
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Save / Bookmark
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { toggleSaveCurrentBlueprint() }
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (currentIsBookmarked) Icons.Default.Favorite else Icons.Default.BookmarkBorder,
                                        contentDescription = "Save",
                                        tint = if (currentIsBookmarked) Color(0xFFE11D48) else MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (currentIsBookmarked) (if (isEnglishLanguage) "Saved" else "সংরক্ষিত") else (if (isEnglishLanguage) "Save" else "সংরক্ষণ"),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentIsBookmarked) Color(0xFFE11D48) else MaterialTheme.colorScheme.primary,
                                        fontFamily = banglaFont
                                    )
                                }

                                // Practice Dhikr Counter
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            practiceDuaTitle = blueprint.scenarioTitleBn
                                            practiceDuaArabic = blueprint.curatedArabicText
                                            practiceDuaTarget = 3
                                            practiceCurrentCount = 0
                                        }
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TouchApp,
                                        contentDescription = "Practice",
                                        tint = IslamicGold,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isEnglishLanguage) "Practice (3x)" else "আমল ও তিলাওয়াত",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold,
                                        fontFamily = banglaFont
                                    )
                                }

                                // Copy All
                                Row(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            val fullCopy = buildString {
                                                append("🌙 Dua Architect — Personal Dua Builder\n")
                                                append("পরিস্থিতি: ${blueprint.scenarioTitleBn}\n")
                                                append("মোড: ${blueprint.mode.titleBn}\n\n")
                                                append("【 আরবী দো'আ 】\n")
                                                append("${blueprint.curatedArabicText}\n\n")
                                                append("【 অনুবাদ ও অর্থ 】\n")
                                                append("“${blueprint.curatedTranslationBn}”\n\n")
                                                append("【 নির্বাচনের কারণ 】\n")
                                                append("${blueprint.whySelectedBn}\n\n")
                                                append("【 মূল সূত্রসমূহ 】\n")
                                                blueprint.sources.forEach { s ->
                                                    append("• ${s.referenceText} (${s.gradingBn})\n")
                                                }
                                                append("\n[Dawah to Jannah - Verified Authentic Source]")
                                            }
                                            copyText(context, fullCopy, "সম্পূর্ণ দো'আ প্ল্যান কপি করা হয়েছে")
                                        }
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isEnglishLanguage) "Copy All" else "কপি করুন",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = banglaFont
                                    )
                                }

                                // Share
                                IconButton(
                                    onClick = {
                                        val shareText = "${blueprint.scenarioTitleBn}\n\n${blueprint.curatedArabicText}\n\n\"${blueprint.curatedTranslationBn}\"\n\nসূত্র: ${blueprint.sources.joinToString(", ") { it.referenceText }}\n[Dawah to Jannah — Personal Dua Builder]"
                                        shareText(context, shareText)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // 2. DETAILED BREAKDOWN TABS (Quranic, Prophetic, Timings & Fiqh)
                item {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        TabRow(
                            selectedTabIndex = selectedTabFilter,
                            containerColor = Color.Transparent,
                            divider = {},
                            indicator = { tabPositions ->
                                TabRowDefaults.SecondaryIndicator(
                                    Modifier.tabIndicatorOffset(tabPositions[selectedTabFilter]),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        ) {
                            listOf(
                                if (isEnglishLanguage) "Tawakkul & Comfort" else "তাওয়াক্কুল ও সান্ত্বনা",
                                if (isEnglishLanguage) "Quranic Duas (${blueprint.quranicDuas.size})" else "কুরআনী দু'আ (${blueprint.quranicDuas.size})",
                                if (isEnglishLanguage) "Prophetic (${blueprint.propheticDuas.size})" else "নববী দু'আ (${blueprint.propheticDuas.size})",
                                if (isEnglishLanguage) "Timings & Adab" else "সময় ও আদব"
                            ).forEachIndexed { index, title ->
                                Tab(
                                    selected = selectedTabFilter == index,
                                    onClick = { selectedTabFilter = index },
                                    text = {
                                        Text(
                                            text = title,
                                            fontSize = 11.sp,
                                            fontWeight = if (selectedTabFilter == index) FontWeight.Bold else FontWeight.Normal,
                                            fontFamily = banglaFont,
                                            color = if (selectedTabFilter == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                )
                            }
                        }
                    }
                }

                // TAB 0: TAWAKKUL & COMFORT + CHECKLIST
                if (selectedTabFilter == 0) {
                    item {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.3f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isEnglishLanguage) "Spiritual Comfort & Perspective:" else "হৃদয়ের সান্ত্বনা ও ইসলামি দৃষ্টিভঙ্গি:",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        fontFamily = banglaFont
                                    )
                                }
                                Text(
                                    text = if (isEnglishLanguage && blueprint.spiritualComfortEn.isNotBlank()) blueprint.spiritualComfortEn else blueprint.spiritualComfortBn,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 21.sp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    if (blueprint.practicalRemindersBn.isNotEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = if (isEnglishLanguage) "📌 Practical Action Steps & Reminders:" else "📌 আমল ও বাস্তবমুখী করণীয় চেকলিস্ট:",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )

                                    blueprint.practicalRemindersBn.forEach { reminder ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Text(
                                                text = "✔",
                                                color = Color(0xFF059669),
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp,
                                                modifier = Modifier.padding(end = 6.dp, top = 2.dp)
                                            )
                                            Text(
                                                text = reminder,
                                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
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

                // TAB 1: QURANIC DUAS BREAKDOWN
                if (selectedTabFilter == 1) {
                    items(blueprint.quranicDuas, key = { it.id }) { quranDua ->
                        QuranicDuaCard(
                            item = quranDua,
                            arabicFontSize = arabicFontSize,
                            isEnglish = isEnglishLanguage,
                            onCopy = {
                                val text = "${quranDua.arabicText}\n\n${quranDua.banglaTranslation}\n— ${quranDua.referenceText}"
                                copyText(context, text, "কুরআনী দু'আ কপি করা হয়েছে")
                            },
                            onShare = {
                                val text = "${quranDua.arabicText}\n\nউচ্চারণ: ${quranDua.banglaPronunciation}\n\nঅর্থ: ${quranDua.banglaTranslation}\n\nরেফারেন্স: ${quranDua.referenceText}\n[Dawah to Jannah - Personal Dua Builder]"
                                shareText(context, text)
                            },
                            onPractice = {
                                practiceDuaTitle = quranDua.referenceText
                                practiceDuaArabic = quranDua.arabicText
                                practiceDuaTarget = 3
                                practiceCurrentCount = 0
                            },
                            banglaFont = banglaFont,
                            arabicFont = arabicFont
                        )
                    }
                }

                // TAB 2: PROPHETIC DUAS BREAKDOWN
                if (selectedTabFilter == 2) {
                    items(blueprint.propheticDuas, key = { it.id }) { propheticDua ->
                        PropheticDuaCard(
                            item = propheticDua,
                            arabicFontSize = arabicFontSize,
                            isEnglish = isEnglishLanguage,
                            onCopy = {
                                val text = "${propheticDua.arabicText}\n\n${propheticDua.banglaTranslation}\n— ${propheticDua.hadithBookBn} (${propheticDua.hadithNumber})"
                                copyText(context, text, "নববী দু'আ কপি করা হয়েছে")
                            },
                            onShare = {
                                val text = "${propheticDua.arabicText}\n\nউচ্চারণ: ${propheticDua.banglaPronunciation}\n\nঅর্থ: ${propheticDua.banglaTranslation}\n\nরেফারেন্স: ${propheticDua.hadithBookBn} (${propheticDua.hadithNumber}) [${propheticDua.gradingBn}]\nসুন্নাতী নিয়ম: ${propheticDua.sunnahPracticeMethodBn}\n[Dawah to Jannah - Personal Dua Builder]"
                                shareText(context, text)
                            },
                            onPractice = {
                                practiceDuaTitle = "${propheticDua.hadithBookBn} (${propheticDua.hadithNumber})"
                                practiceDuaArabic = propheticDua.arabicText
                                practiceDuaTarget = if (propheticDua.repetitionRecommendation.contains("7") || propheticDua.repetitionRecommendation.contains("৭")) 7 else 3
                                practiceCurrentCount = 0
                            },
                            banglaFont = banglaFont,
                            arabicFont = arabicFont
                        )
                    }
                }

                // TAB 3: GOLDEN TIMINGS & ADAB
                if (selectedTabFilter == 3) {
                    items(blueprint.goldenTimingsAndEtiquettes, key = { it.id }) { etiquette ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = etiquette.timingTitleBn,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }

                                Text(
                                    text = etiquette.timingDescriptionBn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text(
                                            text = "📖 হাদীসের দলিল: ${etiquette.hadithEvidenceBn}",
                                            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "✨ আমলের টিপস: ${etiquette.practicalTipBn}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (blueprint.scholarlyClarifications.isNotEmpty()) {
                        item {
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(1.dp, Color(0xFF8B5CF6).copy(alpha = 0.35f))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Psychology,
                                            contentDescription = null,
                                            tint = Color(0xFF8B5CF6),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "ফিকহী সতর্কতা ও প্রখ্যাত আলেমদের অভিমত",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF8B5CF6),
                                            fontFamily = banglaFont
                                        )
                                    }

                                    blueprint.scholarlyClarifications.forEach { opinion ->
                                        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(
                                                text = opinion.issueTitleBn,
                                                style = MaterialTheme.typography.labelMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = banglaFont
                                            )
                                            Text(
                                                text = "• প্রধান অবস্থান: ${opinion.dominantScholarlyPositionBn}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontFamily = banglaFont
                                            )
                                            Text(
                                                text = "• দলিল: ${opinion.supportingEvidenceBn}",
                                                style = MaterialTheme.typography.bodySmall,
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
            }
        }
    }

    // ====================================================
    // SOURCE INSPECTOR DIALOG (Section 24 in User Brief)
    // ====================================================
    inspectingSource?.let { source ->
        AlertDialog(
            onDismissRequest = { inspectingSource = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (source.sourceType == DuaSourceType.QURAN) Icons.Default.MenuBook else Icons.Default.Mosque,
                        contentDescription = null,
                        tint = if (source.sourceType == DuaSourceType.QURAN) Color(0xFF059669) else Color(0xFF4F46E5),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (source.sourceType == DuaSourceType.QURAN) "পবিত্র কুরআনের মূল সনদ" else "সহীহ হাদীসের মূল সনদ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "রেফারেন্স: ${source.referenceText}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = "প্রামাণ্যতা: ${source.gradingBn}",
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    item {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(0.6.dp, IslamicGold.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = source.arabicSourceText,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 20.sp,
                                    lineHeight = 34.sp,
                                    textDirection = TextDirection.Rtl,
                                    textAlign = TextAlign.Right
                                ),
                                fontFamily = arabicFont,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }

                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "অর্থ ও অনুবাদ:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = source.translationBn,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    if (source.tafsirContextBn.isNotBlank()) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "তাফসীর ও শানে নুযুল:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF059669),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = source.tafsirContextBn,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    if (source.sunnahPracticeMethodBn.isNotBlank()) {
                        item {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Text(
                                    text = "সুন্নাতী প্রয়োগ পদ্ধতি:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF4F46E5),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = source.sunnahPracticeMethodBn,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    item {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 এটি অ্যাপের অন্তর্নির্মিত অফলাইন কুরআন ও সহীহ হাদিস ডাটাবেজের মূল অপরিবর্তিত রূপ। দো'আ আর্কিটেক্টে প্রস্তুতকৃত কাঠামোর সাথে এর মিল নিশ্চিত করা হয়েছে।",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { inspectingSource = null }) {
                    Text("বন্ধ করুন", fontFamily = banglaFont)
                }
            }
        )
    }

    // ====================================================
    // SAVED DUAS BOTTOM SHEET
    // ====================================================
    if (showSavedDuasSheet) {
        ModalBottomSheet(
            onDismissRequest = { showSavedDuasSheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFE11D48))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnglishLanguage) "Saved Personal Duas (${savedArchitectDuas.size})" else "সংরক্ষিত ব্যক্তিগত দো'আসমূহ (${savedArchitectDuas.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    }
                    IconButton(onClick = { showSavedDuasSheet = false }) {
                        Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                    }
                }

                if (savedArchitectDuas.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isEnglishLanguage) "No saved Duas yet. Tap ❤️ on any built Dua to save it here." else "কোনো সংরক্ষিত দো'আ নেই। যেকোনো দো'আ প্রস্তুত করে ❤️ বাটনে ট্যাপ করলে এখানে পাওয়া যাবে।",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(400.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(savedArchitectDuas, key = { it.id }) { savedItem ->
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
                            ) {
                                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = savedItem.titleBn,
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = banglaFont
                                        )

                                        Row {
                                            IconButton(
                                                onClick = {
                                                    val text = "${savedItem.arabicText}\n\n${savedItem.meaningBn}\n\nসূত্র: ${savedItem.referenceBn}"
                                                    copyText(context, text, "কপি করা হয়েছে")
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.ContentCopy, contentDescription = "কপি", modifier = Modifier.size(15.dp))
                                            }
                                            IconButton(
                                                onClick = {
                                                    coroutineScope.launch {
                                                        bookmarkDao.removeBookmark(savedItem.id)
                                                    }
                                                },
                                                modifier = Modifier.size(28.dp)
                                            ) {
                                                Icon(Icons.Default.Delete, contentDescription = "মুছুন", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(15.dp))
                                            }
                                        }
                                    }

                                    Text(
                                        text = savedItem.arabicText,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            textDirection = TextDirection.Rtl,
                                            textAlign = TextAlign.Right
                                        ),
                                        fontFamily = arabicFont,
                                        maxLines = 2
                                    )

                                    Text(
                                        text = savedItem.meaningBn,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // ====================================================
    // INTERACTIVE TASBIH / PRACTICE COUNTER MODAL
    // ====================================================
    if (practiceDuaTitle != null && practiceDuaArabic != null) {
        AlertDialog(
            onDismissRequest = { practiceDuaTitle = null },
            title = {
                Column {
                    Text(
                        text = if (isEnglishLanguage) "Sunnah Practice & Dhikr Counter" else "আমল ও তাসবীহ কাউন্টার",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = practiceDuaTitle ?: "",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = practiceDuaArabic ?: "",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = 21.sp,
                                lineHeight = 34.sp,
                                textDirection = TextDirection.Rtl,
                                textAlign = TextAlign.Center
                            ),
                            fontFamily = arabicFont,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    // Large Counter Circle
                    Surface(
                        shape = CircleShape,
                        color = if (practiceCurrentCount >= practiceDuaTarget) Color(0xFF059669) else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier
                            .size(110.dp)
                            .clickable {
                                practiceCurrentCount++
                                triggerVibration()
                            }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$practiceCurrentCount / $practiceDuaTarget",
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (practiceCurrentCount >= practiceDuaTarget) Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                Text(
                                    text = if (practiceCurrentCount >= practiceDuaTarget) (if (isEnglishLanguage) "Completed!" else "সম্পন্ন!") else (if (isEnglishLanguage) "Tap here" else "ট্যাপ করুন"),
                                    fontSize = 11.sp,
                                    fontFamily = banglaFont,
                                    color = if (practiceCurrentCount >= practiceDuaTarget) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    if (practiceCurrentCount >= practiceDuaTarget) {
                        Text(
                            text = if (isEnglishLanguage) "Alhamdulillah! Target recitation completed." else "আলহামদুলিল্লাহ! সুন্নাতী সংখ্যার তিলাওয়াত সম্পন্ন হয়েছে।",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669),
                            fontFamily = banglaFont
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { practiceDuaTitle = null }
                ) {
                    Text(if (isEnglishLanguage) "Close" else "বন্ধ করুন", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { practiceCurrentCount = 0 }
                ) {
                    Text(if (isEnglishLanguage) "Reset" else "রিসেট", fontFamily = banglaFont)
                }
            }
        )
    }

    // ====================================================
    // HELP & PRINCIPLES DIALOG
    // ====================================================
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEnglishLanguage) "Dua Architect Principles" else "Dua Architect কী ও এর মূলনীতি", fontFamily = banglaFont)
                }
            },
            text = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Text(
                            text = "১. কেবল পবিত্র কুরআন ও সহীহ হাদীসের প্রামাণ্য ভিত্তি:",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "Dua Architect-এর প্রতিটি দো'আ অ্যাপের অফলাইন কুরআন ও সহীহ হাদীসের সংরক্ষিত ডাটাবেজ থেকে সংকলিত। কোনো প্রকার মনগড়া, দুর্বল বা জাল হাদীস ব্যবহার করা হয়নি।",
                            fontFamily = banglaFont,
                            fontSize = 12.sp
                        )
                    }
                    item {
                        Text(
                            text = "২. ৩টি দো'আ বিন্যাস মোড:",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "• Mode A (Authentic Dua): হুবহু একক প্রামাণ্য হাদীস বা আয়াত।\n• Mode B (Curated Dua): একাধিক প্রামাণ্য দো'আর সুশৃঙ্খল সমন্বয়।\n• Mode C (Personalized Flow): প্রামাণ্য দো'আসমূহকে সংযুক্ত করে ব্যক্তিগত আরজি কাঠামো।",
                            fontFamily = banglaFont,
                            fontSize = 12.sp
                        )
                    }
                    item {
                        Text(
                            text = "৩. কঠোর প্রামাণ্যতা নীতি (Strict Authenticity):",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont,
                            fontSize = 13.sp
                        )
                        Text(
                            text = "নতুন কোনো আরবী বাক্য বা কথাকে স্বয়ং রাসুলুল্লাহ ﷺ-এর বাণী বা আল্লাহর কালাম বলে দাবি করা সম্পূর্ণ নিষিদ্ধ। দো'আ আর্কিটেক্ট এই নীতি অক্ষরে অক্ষরে মেনে চলে।",
                            fontFamily = banglaFont,
                            fontSize = 12.sp
                        )
                    }
                }
            },
            confirmButton = {
                Button(onClick = { showHelpDialog = false }) {
                    Text(if (isEnglishLanguage) "Understood" else "বুঝেছি", fontFamily = banglaFont)
                }
            }
        )
    }
}

// ----------------------------------------------------
// HELPER COMPOSABLES
// ----------------------------------------------------

@Composable
private fun QuranicDuaCard(
    item: QuranicDuaItem,
    arabicFontSize: Int,
    isEnglish: Boolean,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onPractice: () -> Unit,
    banglaFont: androidx.compose.ui.text.font.FontFamily,
    arabicFont: androidx.compose.ui.text.font.FontFamily
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF059669).copy(alpha = 0.15f),
                    border = BorderStroke(0.5.dp, Color(0xFF059669))
                ) {
                    Text(
                        text = item.referenceText,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onPractice, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Default.TouchApp,
                            contentDescription = "আমল করুন",
                            tint = Color(0xFF059669),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onCopy, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "কপি",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "শেয়ার",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Arabic Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.arabicText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = arabicFontSize.sp,
                        lineHeight = (arabicFontSize * 1.6).sp,
                        textDirection = TextDirection.Rtl,
                        textAlign = TextAlign.Right
                    ),
                    fontFamily = arabicFont,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                )
            }

            // Pronunciation & Translation
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (item.banglaPronunciation.isNotBlank()) {
                    Text(
                        text = "উচ্চারণ: ${item.banglaPronunciation}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
                Text(
                    text = "অর্থ: “${if (isEnglish && item.englishTranslation.isNotBlank()) item.englishTranslation else item.banglaTranslation}”",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }

            // Tafsir Context Expander
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isExpanded) "তাফসীর সংক্ষেপ করুন" else "তাফসীর প্রেক্ষাপট দেখুন...",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF059669),
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(18.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = item.tafsirContextBn,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PropheticDuaCard(
    item: PropheticDuaItem,
    arabicFontSize: Int,
    isEnglish: Boolean,
    onCopy: () -> Unit,
    onShare: () -> Unit,
    onPractice: () -> Unit,
    banglaFont: androidx.compose.ui.text.font.FontFamily,
    arabicFont: androidx.compose.ui.text.font.FontFamily
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF4F46E5).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header Row: Reference + Grading
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${item.hadithBookBn} (${item.hadithNumber})",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "রাবী: ${item.narratorCompanionBn} • মান: ${item.gradingBn}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onPractice, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Default.TouchApp,
                            contentDescription = "আমল করুন",
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    IconButton(onClick = onCopy, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "কপি",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(30.dp)) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "শেয়ার",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            // Arabic Box
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = item.arabicText,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontSize = arabicFontSize.sp,
                        lineHeight = (arabicFontSize * 1.6).sp,
                        textDirection = TextDirection.Rtl,
                        textAlign = TextAlign.Right
                    ),
                    fontFamily = arabicFont,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                )
            }

            // Pronunciation & Translation
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (item.banglaPronunciation.isNotBlank()) {
                    Text(
                        text = "উচ্চারণ: ${item.banglaPronunciation}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
                Text(
                    text = "অর্থ: “${if (isEnglish && item.englishTranslation.isNotBlank()) item.englishTranslation else item.banglaTranslation}”",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }

            // Sunnah Practice Method Expander
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF4F46E5).copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "🤲 সুন্নাতী প্রয়োগ পদ্ধতি:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.sunnahPracticeMethodBn,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// ----------------------------------------------------
// UTILITIES
// ----------------------------------------------------

private fun copyText(context: Context, text: String, toastMsg: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Dua", text)
    clipboard.setPrimaryClip(clip)
    Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
}

private fun shareText(context: Context, text: String) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, text)
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, "দো'আ শেয়ার করুন")
    context.startActivity(shareIntent)
}
