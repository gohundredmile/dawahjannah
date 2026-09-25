package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.QuranActionEngineCatalog
import com.example.ui.components.InteractiveAyahPickerSheet
import com.example.data.model.ActionHistoryRecord
import com.example.data.model.ActionMode
import com.example.data.model.ActionPlanDuration
import com.example.data.model.AskAyahAnswer
import com.example.data.model.AyahActionInsight
import com.example.data.model.LifeSphere
import com.example.data.model.ReviewOutcome
import com.example.data.repository.QuranActionEngineRepository
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.QuranActionEngineAiService
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class EnginePhase(val titleBn: String, val icon: String) {
    READ("আয়াত ও পাঠ", "📖"),
    UNDERSTAND("উপলব্ধি", "🧠"),
    REFLECT("তাদাব্বুর", "❤️"),
    APPLY("প্রয়োগ", "🎯"),
    ACT("আজকের আমল", "✅"),
    REVIEW("সন্ধ্যা পর্যালোচনা", "🌙"),
    ASK("আয়াতকে প্রশ্ন", "💬"),
    JOURNEY("আমার জার্নি", "📜")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranActionEngineScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val repository = remember { QuranActionEngineRepository(context) }
    val aiService = remember { QuranActionEngineAiService(context) }

    val historyRecords by repository.history.collectAsState()
    val activeCommitment by repository.activeCommitment.collectAsState()
    val bookmarkedAyahIds by repository.bookmarkedAyahIds.collectAsState()

    val catalogList = remember { QuranActionEngineCatalog.getAllAyahs() }
    var selectedAyahIndex by remember { mutableIntStateOf(0) }
    var currentInsight by remember { mutableStateOf(catalogList[0]) }

    var currentPhase by remember { mutableStateOf(EnginePhase.READ) }

    // User Selection States
    var selectedSphere by remember { mutableStateOf(LifeSphere.CHARACTER) }
    var selectedActionMode by remember { mutableStateOf(ActionMode.PRACTICE) }
    var selectedPlanDuration by remember { mutableStateOf(ActionPlanDuration.TODAY) }

    // Action Customization
    var currentActionIdeaIndex by remember { mutableIntStateOf(0) }
    var currentActionText by remember { mutableStateOf(currentInsight.defaultTodayAction) }
    var isEditingAction by remember { mutableStateOf(false) }
    var userReflectionNote by remember { mutableStateOf("") }
    var selectedReflectionQuestion by remember { mutableStateOf<String?>(null) }

    // Sheets & Dialogs
    var showSourcesSheet by remember { mutableStateOf(false) }
    var showDeepDiveSheet by remember { mutableStateOf(false) }
    var showAyahPickerSheet by remember { mutableStateOf(false) }

    // Ask the Ayah state
    var askInput by remember { mutableStateOf("") }
    var isAskingAi by remember { mutableStateOf(false) }
    var askAnswer by remember { mutableStateOf<AskAyahAnswer?>(null) }

    // Evening Review state
    var reviewOutcome by remember { mutableStateOf(ReviewOutcome.DID_IT) }
    var reviewNoticeText by remember { mutableStateOf("") }
    var reviewChangeText by remember { mutableStateOf("") }

    // Sync insight when catalog index changes
    LaunchedEffect(selectedAyahIndex) {
        if (selectedAyahIndex in catalogList.indices) {
            val ins = catalogList[selectedAyahIndex]
            currentInsight = ins
            currentActionText = ins.defaultTodayAction
            currentActionIdeaIndex = 0
            selectedReflectionQuestion = ins.reflectiveQuestions.firstOrNull()
        }
    }

    val isBookmarked = bookmarkedAyahIds.contains(currentInsight.ayahId)
    val continuityPair = remember(historyRecords) { repository.getRecentContinuityInsight() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Quran → Action Engine",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = "Live the Ayah",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                        Text(
                            text = "পড়া • উপলব্ধি • তাদাব্বুর • প্রয়োগ • আমল • শিখন",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
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
                    IconButton(
                        onClick = { repository.toggleBookmark(currentInsight.ayahId) }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "বুকমার্ক",
                            tint = if (isBookmarked) Color(0xFFD97706) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { currentPhase = EnginePhase.JOURNEY }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "আমার জার্নি",
                            tint = if (currentPhase == EnginePhase.JOURNEY) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Horizontal Phase Tabs
            ScrollablePhaseTabs(
                currentPhase = currentPhase,
                onSelectPhase = { currentPhase = it },
                banglaFont = banglaFont
            )

            // Continuity Bridge Banner (if available)
            if (continuityPair != null && currentPhase != EnginePhase.JOURNEY) {
                ContinuityBridgeBanner(
                    headline = continuityPair.first,
                    message = continuityPair.second,
                    banglaFont = banglaFont
                )
            }

            // Phase Content
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (currentPhase) {
                    EnginePhase.READ -> {
                        ReadAyahPhaseContent(
                            insight = currentInsight,
                            catalog = catalogList,
                            selectedIndex = selectedAyahIndex,
                            onSelectAyah = { idx -> selectedAyahIndex = idx },
                            onSelectInsight = { chosen ->
                                currentInsight = chosen
                                val idx = catalogList.indexOfFirst { it.ayahId == chosen.ayahId }
                                if (idx != -1) selectedAyahIndex = idx
                                currentActionText = chosen.defaultTodayAction
                                currentActionIdeaIndex = 0
                                selectedReflectionQuestion = chosen.reflectiveQuestions.firstOrNull()
                            },
                            onOpenAyahPicker = { showAyahPickerSheet = true },
                            onApplyThisAyah = { currentPhase = EnginePhase.UNDERSTAND },
                            onOpenDeepDive = { showDeepDiveSheet = true },
                            banglaFont = banglaFont,
                            arabicFont = arabicFont
                        )
                    }
                    EnginePhase.UNDERSTAND -> {
                        UnderstandPhaseContent(
                            insight = currentInsight,
                            onProceedToReflect = { currentPhase = EnginePhase.REFLECT },
                            onOpenSources = { showSourcesSheet = true },
                            onOpenDeepDive = { showDeepDiveSheet = true },
                            banglaFont = banglaFont
                        )
                    }
                    EnginePhase.REFLECT -> {
                        ReflectPhaseContent(
                            insight = currentInsight,
                            selectedQuestion = selectedReflectionQuestion,
                            onSelectQuestion = { selectedReflectionQuestion = it },
                            userNote = userReflectionNote,
                            onUpdateUserNote = { userReflectionNote = it },
                            onProceedToApply = { currentPhase = EnginePhase.APPLY },
                            banglaFont = banglaFont
                        )
                    }
                    EnginePhase.APPLY -> {
                        ApplyPhaseContent(
                            insight = currentInsight,
                            selectedSphere = selectedSphere,
                            onSelectSphere = { selectedSphere = it },
                            onProceedToAct = { currentPhase = EnginePhase.ACT },
                            banglaFont = banglaFont
                        )
                    }
                    EnginePhase.ACT -> {
                        ActPhaseContent(
                            insight = currentInsight,
                            selectedSphere = selectedSphere,
                            selectedMode = selectedActionMode,
                            onSelectMode = { selectedActionMode = it },
                            selectedPlan = selectedPlanDuration,
                            onSelectPlan = { selectedPlanDuration = it },
                            actionText = currentActionText,
                            onUpdateActionText = { currentActionText = it },
                            isEditing = isEditingAction,
                            onToggleEditing = { isEditingAction = !isEditingAction },
                            onNextIdea = {
                                val alternatives = listOf(currentInsight.defaultTodayAction) + currentInsight.alternativeTodayActions
                                currentActionIdeaIndex = (currentActionIdeaIndex + 1) % alternatives.size
                                currentActionText = alternatives[currentActionIdeaIndex]
                            },
                            onCommitAction = {
                                val ansMap = if (selectedReflectionQuestion != null && userReflectionNote.isNotBlank()) {
                                    mapOf(selectedReflectionQuestion!! to userReflectionNote)
                                } else emptyMap()

                                repository.commitAction(
                                    insight = currentInsight,
                                    selectedSphere = selectedSphere,
                                    selectedMode = selectedActionMode,
                                    planDuration = selectedPlanDuration,
                                    actionText = currentActionText,
                                    userPersonalNote = userReflectionNote,
                                    reflectionAnswers = ansMap
                                )
                                Toast.makeText(context, "আলহামদুলিল্লাহ! আমলটি সংকল্পে যুক্ত হয়েছে।", Toast.LENGTH_SHORT).show()
                                currentPhase = EnginePhase.REVIEW
                            },
                            banglaFont = banglaFont
                        )
                    }
                    EnginePhase.REVIEW -> {
                        EveningReviewPhaseContent(
                            activeCommitment = activeCommitment,
                            recentHistory = historyRecords.take(5),
                            outcome = reviewOutcome,
                            onSelectOutcome = { reviewOutcome = it },
                            noticeText = reviewNoticeText,
                            onUpdateNotice = { reviewNoticeText = it },
                            changeText = reviewChangeText,
                            onUpdateChange = { reviewChangeText = it },
                            onSubmitReview = { recId ->
                                repository.submitEveningReview(
                                    recordId = recId,
                                    outcome = reviewOutcome,
                                    whatDidYouNotice = reviewNoticeText,
                                    didItChangeHandling = reviewChangeText,
                                    growthTakeaway = reviewNoticeText.ifBlank { "আজকের আয়াতের শিক্ষা প্রয়োগ করেছি।" }
                                )
                                Toast.makeText(context, "পর্যালোচনা সম্পন্ন হয়েছে। আল্লাহ কবুল করুন!", Toast.LENGTH_SHORT).show()
                                reviewNoticeText = ""
                                reviewChangeText = ""
                                currentPhase = EnginePhase.JOURNEY
                            },
                            onExploreNewAyah = { currentPhase = EnginePhase.READ },
                            banglaFont = banglaFont
                        )
                    }
                    EnginePhase.ASK -> {
                        AskAyahPhaseContent(
                            insight = currentInsight,
                            inputQuestion = askInput,
                            onUpdateInput = { askInput = it },
                            isLoading = isAskingAi,
                            answer = askAnswer,
                            onAsk = { q ->
                                scope.launch {
                                    isAskingAi = true
                                    askAnswer = aiService.askTheAyah(currentInsight, q)
                                    isAskingAi = false
                                }
                            },
                            banglaFont = banglaFont
                        )
                    }
                    EnginePhase.JOURNEY -> {
                        MyJourneyPhaseContent(
                            records = historyRecords,
                            themeCounts = repository.getEncounteredThemesMap(),
                            onToggleComplete = { id, done -> repository.completeAction(id, done) },
                            onSelectRecordAyah = { rec ->
                                val found = catalogList.find { it.ayahId == rec.ayahId }
                                if (found != null) {
                                    currentInsight = found
                                    currentPhase = EnginePhase.READ
                                }
                            },
                            banglaFont = banglaFont
                        )
                    }
                }
            }
        }

        // Sources Bottom Sheet
        if (showSourcesSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSourcesSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                SourcesBottomSheetContent(
                    sources = currentInsight.tafsirSources,
                    onDismiss = { showSourcesSheet = false },
                    banglaFont = banglaFont
                )
            }
        }

        // Deep Dive Bottom Sheet
        if (showDeepDiveSheet) {
            ModalBottomSheet(
                onDismissRequest = { showDeepDiveSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                DeepDiveBottomSheetContent(
                    insight = currentInsight,
                    onDismiss = { showDeepDiveSheet = false },
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
            }
        }

        // Interactive Ayah Picker Bottom Sheet (Life Essential & 114 Surahs Offline)
        if (showAyahPickerSheet) {
            ModalBottomSheet(
                onDismissRequest = { showAyahPickerSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ) {
                InteractiveAyahPickerSheet(
                    currentInsight = currentInsight,
                    onSelectInsight = { chosen ->
                        currentInsight = chosen
                        val idx = catalogList.indexOfFirst { it.ayahId == chosen.ayahId }
                        if (idx != -1) {
                            selectedAyahIndex = idx
                        }
                        currentActionText = chosen.defaultTodayAction
                        currentActionIdeaIndex = 0
                        selectedReflectionQuestion = chosen.reflectiveQuestions.firstOrNull()
                        showAyahPickerSheet = false
                    },
                    onDismiss = { showAyahPickerSheet = false },
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
            }
        }
    }
}

@Composable
fun ScrollablePhaseTabs(
    currentPhase: EnginePhase,
    onSelectPhase: (EnginePhase) -> Unit,
    banglaFont: FontFamily
) {
    val phases = EnginePhase.entries
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .horizontalScroll(scrollState)
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        phases.forEach { phase ->
            val isSelected = phase == currentPhase
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onSelectPhase(phase) }
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = phase.icon, fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = phase.titleBn,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun ContinuityBridgeBanner(
    headline: String,
    message: String,
    banglaFont: FontFamily
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF0FDF4),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBF7D0))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "🌱", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = headline,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534),
                        fontFamily = banglaFont
                    )
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF15803D),
                        fontFamily = banglaFont,
                        fontSize = 11.5.sp
                    )
                )
            }
        }
    }
}

// ==========================================
// 1. READ AYAH PHASE (Start With the Ayah - Optimized)
// ==========================================
@Composable
fun ReadAyahPhaseContent(
    insight: AyahActionInsight,
    catalog: List<AyahActionInsight>,
    selectedIndex: Int,
    onSelectAyah: (Int) -> Unit,
    onSelectInsight: (AyahActionInsight) -> Unit,
    onOpenAyahPicker: () -> Unit,
    onApplyThisAyah: () -> Unit,
    onOpenDeepDive: () -> Unit,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    val context = LocalContext.current
    var selectedCategoryFilter by remember { mutableStateOf("সকল") }

    val categories = remember {
        listOf(
            "সকল",
            "ধৈর্য ও পরীক্ষা",
            "রিযিক ও তাওয়াক্কুল",
            "মানসিক প্রশান্তি",
            "তাওবাহ ও ক্ষমা",
            "পিতামাতা ও পরিবার",
            "আখলাক ও শিষ্টাচার",
            "তাওহীদ ও সুরক্ষা"
        )
    }

    val filteredList = remember(selectedCategoryFilter, catalog) {
        if (selectedCategoryFilter == "সকল") catalog
        else {
            catalog.filter { item ->
                when (selectedCategoryFilter) {
                    "ধৈর্য ও পরীক্ষা" -> item.primaryThemes.any { it.contains("ধৈর্য") || it.contains("পরীক্ষা") || it.contains("সবর") }
                    "রিযিক ও তাওয়াক্কুল" -> item.primaryThemes.any { it.contains("রিযিক") || it.contains("তাওয়াক্কুল") || it.contains("উপার্জন") }
                    "মানসিক প্রশান্তি" -> item.primaryThemes.any { it.contains("শান্তি") || it.contains("সান্ত্বনা") || it.contains("প্রশান্তি") }
                    "তাওবাহ ও ক্ষমা" -> item.primaryThemes.any { it.contains("তাওবা") || it.contains("ক্ষমা") || it.contains("মাগফিরাত") }
                    "পিতামাতা ও পরিবার" -> item.primaryThemes.any { it.contains("পিতা") || it.contains("মাতা") || it.contains("পরিবার") || it.contains("দাম্পত্য") }
                    "আখলাক ও শিষ্টাচার" -> item.primaryThemes.any { it.contains("বিনয়") || it.contains("আখলাক") || it.contains("গীবত") || it.contains("শিষ্টাচার") }
                    "তাওহীদ ও সুরক্ষা" -> item.primaryThemes.any { it.contains("তাওহীদ") || it.contains("সুরক্ষা") || it.contains("হেফাযত") || it.contains("রুকইয়াহ") }
                    else -> true
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Optimized Interactive Control Header
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
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
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "📖", fontSize = 16.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "আয়াত নির্বাচন ও পাঠ",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                )
                            }
                            Text(
                                text = "${catalog.size}টি জীবনঘনিষ্ঠ আয়াত • ১১৪টি সূরা অফলাইনে উপলব্ধ",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // Lucky / Random Ayah Picker Button
                        FilledTonalButton(
                            onClick = {
                                if (catalog.isNotEmpty()) {
                                    val randomIdx = catalog.indices.random()
                                    onSelectAyah(randomIdx)
                                    Toast.makeText(context, "🎲 আজকের নির্বাচিত আয়াত লোড হয়েছে", Toast.LENGTH_SHORT).show()
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "🎲 দৈবচয়ন",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Open Full Ayah & 114 Surah Browser Button
                    Button(
                        onClick = onOpenAyahPicker,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        contentPadding = PaddingValues(vertical = 10.dp)
                    ) {
                        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "কুরআনের সকল আয়াত ও ১১৪টি সূরা ব্রাউজ করুন",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Quick Navigation Arrows Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                if (selectedIndex > 0) {
                                    onSelectAyah(selectedIndex - 1)
                                } else {
                                    onSelectAyah(catalog.size - 1)
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "আগের আয়াত", modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "আগের আয়াত", style = MaterialTheme.typography.labelSmall.copy(fontFamily = banglaFont))
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = "${selectedIndex + 1} / ${catalog.size}",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        TextButton(
                            onClick = {
                                if (selectedIndex < catalog.size - 1) {
                                    onSelectAyah(selectedIndex + 1)
                                } else {
                                    onSelectAyah(0)
                                }
                            },
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(text = "পরের আয়াত", style = MaterialTheme.typography.labelSmall.copy(fontFamily = banglaFont))
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "পরের আয়াত", modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            val filterRowScroll = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(filterRowScroll),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategoryFilter
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategoryFilter = cat },
                        label = {
                            Text(
                                text = cat,
                                fontFamily = banglaFont,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        // Ayah Selector Carousel (Filtered)
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredList.size) { idx ->
                    val item = filteredList[idx]
                    val isSelected = item.ayahId == insight.ayahId
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                onSelectInsight(item)
                            }
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = item.surahNameBangla,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            )
                            Text(
                                text = item.primaryThemes.take(2).joinToString(" • "),
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 10.sp,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                        }
                    }
                }
            }
        }

        // Primary Ayah Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    // Header tag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer
                        ) {
                            Text(
                                text = "${insight.surahNameBangla} (${insight.revelationTypeBn})",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontFamily = banglaFont
                                )
                            )
                        }

                        Row {
                            IconButton(
                                onClick = {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val text = "${insight.arabicText}\n\n${insight.banglaTranslation}\n— ${insight.surahNameBangla}"
                                    cm.setPrimaryClip(ClipData.newPlainText("Ayah", text))
                                    Toast.makeText(context, "আয়াত ও অর্থ কপি হয়েছে", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "কপি", modifier = Modifier.size(18.dp))
                            }
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, insight.surahNameBangla)
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "📖 ${insight.arabicText}\n\nঅর্থ: ${insight.banglaTranslation}\n\n— ${insight.surahNameBangla}\n\n[কুরআন → আমল ইঞ্জিন]"
                                        )
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন"))
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Share, contentDescription = "শেয়ার", modifier = Modifier.size(18.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Arabic Text
                    Text(
                        text = insight.arabicText,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontFamily = arabicFont,
                            lineHeight = 44.sp,
                            textAlign = TextAlign.Right
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Transliteration
                    Text(
                        text = insight.transliterationBn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            lineHeight = 20.sp
                        )
                    )

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )

                    // Bangla Translation
                    Text(
                        text = insight.banglaTranslation,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = banglaFont,
                            lineHeight = 26.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // English Translation
                    Text(
                        text = insight.englishTranslation,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }

        // Signature Core CTA: Apply This Ayah
        item {
            Button(
                onClick = onApplyThisAyah,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✨ Apply This Ayah (আয়াতটি জীবনে প্রয়োগ করুন)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
            }
        }

        // Secondary Action: Deep Dive Mode
        item {
            OutlinedButton(
                onClick = onOpenDeepDive,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🔬 ডিপ ডাইভ মোড (শব্দার্থ, প্রেক্ষাপট ও সংশ্লিষ্ট হাদিস)",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = banglaFont,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
        }

        // Product Loop Explanation Banner
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8FAFC)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "💡 'কুরআন → আমল ইঞ্জিন'-এর দর্শন",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B),
                            fontFamily = banglaFont
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "পবিত্র কুরআন শুধু তেলাওয়াতের জন্য নয়; প্রতিটি আয়াত পড়ে ভাবুন: 'আমি এটি পড়লাম। বুঝলাম। এখন আমার জীবনে এটি কীভাবে কার্যকর করব?' কুরআন দিকনির্দেশনা দেয়, নির্ভরযোগ্য আলেমগণ প্রেক্ষাপট ব্যাখ্যা করেন, আপনি তাদাব্বুর করেন এবং নিজের জীবনে একটি ছোট আমল বেছে নেন।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF475569),
                            fontFamily = banglaFont,
                            lineHeight = 18.sp
                        )
                    )
                }
            }
        }
    }
}

// ==========================================
// 2. UNDERSTAND PHASE (4 Core Questions + 3-Tier Truth)
// ==========================================
@Composable
fun UnderstandPhaseContent(
    insight: AyahActionInsight,
    onProceedToReflect: () -> Unit,
    onOpenSources: () -> Unit,
    onOpenDeepDive: () -> Unit,
    banglaFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "🧠 আয়াতের গভীর উপলব্ধি",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    )
                    Text(
                        text = "৪টি প্রধান মাত্রিক বিশ্লেষণ ও শরঈ প্রামাণ্যতা",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    )
                }

                TextButton(onClick = onOpenSources) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "উৎস দেখুন",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Pillar 1: What does this ayah teach?
        item {
            InsightPillarCard(
                icon = "💡",
                questionBn = "What does this ayah teach? (কেন্দ্রীয় বার্তা)",
                contentBn = insight.whatDoesItTeachBn,
                accentColor = Color(0xFF0284C7),
                containerColor = Color(0xFFF0F9FF),
                banglaFont = banglaFont
            )
        }

        // Pillar 2: What is this asking me to notice?
        item {
            InsightPillarCard(
                icon = "👁️",
                questionBn = "What is this asking me to notice? (মূল মূল্যবোধ ও দর্শন)",
                contentBn = insight.whatToNoticeBn,
                accentColor = Color(0xFF059669),
                containerColor = Color(0xFFECFDF5),
                banglaFont = banglaFont
            )
        }

        // Pillar 3: What should I be careful about?
        item {
            InsightPillarCard(
                icon = "⚠️",
                questionBn = "What should I be careful about? (সতর্কতা ও পরিহারযোগ্যতা)",
                contentBn = insight.whatToBeCarefulAboutBn,
                accentColor = Color(0xFFDC2626),
                containerColor = Color(0xFFFEF2F2),
                banglaFont = banglaFont
            )
        }

        // Pillar 4: What can I practice?
        item {
            InsightPillarCard(
                icon = "🎯",
                questionBn = "What can I practice? (বাস্তবসম্মত প্রয়োগ)",
                contentBn = insight.whatCanIPracticeBn,
                accentColor = Color(0xFF7C3AED),
                containerColor = Color(0xFFF5F3FF),
                banglaFont = banglaFont
            )
        }

        // Three-Tier Truth Distinction Card (MANDATORY SAFEGUARD)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⚖️ শরঈ স্বাতন্ত্র্য ও প্রামাণিক বিভাজন",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "কুরআনের নির্দেশ, তাফসীর ও ব্যক্তিগত আমলের মধ্যে সুস্পষ্ট পার্থক্য:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            fontSize = 11.5.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    TierItem(
                        badge = "১. কুরআন বলে (Quran says)",
                        badgeColor = Color(0xFF15803D),
                        content = insight.quranSaysBn,
                        banglaFont = banglaFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TierItem(
                        badge = "২. বিজ্ঞ মুফাসসিরগণের ব্যাখ্যা (Scholarly Tafsir)",
                        badgeColor = Color(0xFFB45309),
                        content = insight.scholarlyInterpretationBn,
                        banglaFont = banglaFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TierItem(
                        badge = "৩. সম্ভাব্য ব্যক্তিগত প্রয়োগ (Possible Personal Application)",
                        badgeColor = Color(0xFF2563EB),
                        content = insight.possiblePersonalApplicationBn,
                        banglaFont = banglaFont
                    )
                }
            }
        }

        // Proceed to Reflection CTA
        item {
            Button(
                onClick = onProceedToReflect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "পরবর্তী ধাপ: ব্যক্তিগত তাদাব্বুর (Personal Reflection) →",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
            }
        }
    }
}

@Composable
fun InsightPillarCard(
    icon: String,
    questionBn: String,
    contentBn: String,
    accentColor: Color,
    containerColor: Color,
    banglaFont: FontFamily
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = androidx.compose.foundation.BorderStroke(1.dp, accentColor.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = icon, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = questionBn,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        fontFamily = banglaFont
                    )
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = contentBn,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF1E293B),
                    fontFamily = banglaFont,
                    lineHeight = 22.sp
                )
            )
        }
    }
}

@Composable
fun TierItem(
    badge: String,
    badgeColor: Color,
    content: String,
    banglaFont: FontFamily
) {
    Column {
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = badgeColor.copy(alpha = 0.12f)
        ) {
            Text(
                text = badge,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = badgeColor,
                    fontSize = 11.sp
                )
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont,
                lineHeight = 18.sp
            )
        )
    }
}

// ==========================================
// 3. REFLECT PHASE (Conversational Reflection Lens)
// ==========================================
@Composable
fun ReflectPhaseContent(
    insight: AyahActionInsight,
    selectedQuestion: String?,
    onSelectQuestion: (String) -> Unit,
    userNote: String,
    onUpdateUserNote: (String) -> Unit,
    onProceedToApply: () -> Unit,
    banglaFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "❤️", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "ব্যক্তিগত তাদাব্বুর (Personal Reflection)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                        Text(
                            text = "এটি কোনো পরীক্ষা বা কুইজ নয়; এটি পবিত্র আয়াতের সাথে আপনার হৃদয়ের একটি নিভৃত কথোপকথন।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontFamily = banglaFont,
                                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                                fontSize = 11.5.sp
                            )
                        )
                    }
                }
            }
        }

        // Reflection Questions List
        item {
            Text(
                text = "একটি প্রশ্ন বেছে নিন যা আপনার মনে দাগ কেটেছে:",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                insight.reflectiveQuestions.forEach { question ->
                    val isChosen = question == selectedQuestion
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isChosen) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isChosen) MaterialTheme.colorScheme.primary else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { onSelectQuestion(question) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = if (isChosen) "🔹" else "▫️", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = question,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isChosen) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }

        // User's Private Reflection Notes Box
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "✍️ আপনার ব্যক্তিগত অনুভূতি বা ভাবনা লিখুন (ঐচ্ছিক):",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    )
                    Text(
                        text = "আপনার উত্তর সম্পূর্ণ আপনার ফোনে সংরক্ষিত ও ব্যক্তিগত থাকবে।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            fontSize = 11.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = userNote,
                        onValueChange = onUpdateUserNote,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        placeholder = {
                            Text(
                                text = "যেমন: আজকের সকালে এইরকম একটি পরিস্থিতির মুখে পড়েছিলাম...",
                                fontFamily = banglaFont,
                                fontSize = 13.sp
                            )
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }
        }

        // Proceed to Apply CTA
        item {
            Button(
                onClick = onProceedToApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "পরবর্তী ধাপ: জীবনের ১২টি ক্ষেত্রে প্রয়োগ (Apply) →",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
            }
        }
    }
}

// ==========================================
// 4. APPLY PHASE (12 Life Spheres)
// ==========================================
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ApplyPhaseContent(
    insight: AyahActionInsight,
    selectedSphere: LifeSphere,
    onSelectSphere: (LifeSphere) -> Unit,
    onProceedToAct: () -> Unit,
    banglaFont: FontFamily
) {
    val spheres = LifeSphere.entries
    val currentApplications = insight.applicationsBySphere[selectedSphere] ?: listOf(
        "এই ক্ষেত্রে আয়াতটির মূল নীতি হলো সততা, ন্যায়পরায়ণতা ও আল্লাহর ভয় বজায় রাখা।"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "🎯 Life Application Engine",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
                Text(
                    text = "জীবনের কোন ক্ষেত্রে আপনি এই আয়াতের নীতি প্রয়োগ করতে চান তা বেছে নিন:",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                )
            }
        }

        // 12 Spheres Chips Grid
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                spheres.forEach { sphere ->
                    val isSelected = sphere == selectedSphere
                    FilterChip(
                        selected = isSelected,
                        onClick = { onSelectSphere(sphere) },
                        label = {
                            Text(
                                text = "${sphere.emoji} ${sphere.titleBn}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = banglaFont
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(sphere.colorHex).copy(alpha = 0.18f),
                            selectedLabelColor = Color(sphere.colorHex)
                        ),
                        border = if (isSelected) FilterChipDefaults.filterChipBorder(
                            selected = true,
                            enabled = true,
                            borderColor = Color(sphere.colorHex),
                            borderWidth = 1.5.dp
                        ) else null
                    )
                }
            }
        }

        // Tailored Principle Card for Selected Sphere
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(selectedSphere.colorHex).copy(alpha = 0.08f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(selectedSphere.colorHex).copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = selectedSphere.emoji, fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "বাছাইকৃত ক্ষেত্র: ${selectedSphere.titleBn}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(selectedSphere.colorHex),
                                    fontFamily = banglaFont
                                )
                            )
                            Text(
                                text = selectedSphere.subtitleBn,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp),
                        color = Color(selectedSphere.colorHex).copy(alpha = 0.2f)
                    )

                    Text(
                        text = "বাস্তবসম্মত প্রয়োগের দিকনির্দেশনা:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    currentApplications.forEachIndexed { idx, appText ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                color = Color(selectedSphere.colorHex),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = appText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color(0xFF1E293B),
                                    fontFamily = banglaFont,
                                    lineHeight = 22.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Proceed to Act CTA
        item {
            Button(
                onClick = onProceedToAct,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(selectedSphere.colorHex)
                )
            ) {
                Text(
                    text = "পরবর্তী ধাপ: আজকের আমল তৈরি করুন (Act) →",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
            }
        }
    }
}

// ==========================================
// 5. ACT PHASE (Signature "One Ayah, One Action")
// ==========================================
@Composable
fun ActPhaseContent(
    insight: AyahActionInsight,
    selectedSphere: LifeSphere,
    selectedMode: ActionMode,
    onSelectMode: (ActionMode) -> Unit,
    selectedPlan: ActionPlanDuration,
    onSelectPlan: (ActionPlanDuration) -> Unit,
    actionText: String,
    onUpdateActionText: (String) -> Unit,
    isEditing: Boolean,
    onToggleEditing: () -> Unit,
    onNextIdea: () -> Unit,
    onCommitAction: () -> Unit,
    banglaFont: FontFamily
) {
    val modes = ActionMode.entries
    val plans = ActionPlanDuration.entries

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // The Signature Banner
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "⭐ The Signature Experience: “One Ayah, One Action”",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“আপনি আয়াতটি পড়লেন। অর্থ বুঝলেন। নিজের জীবন নিয়ে তাদাব্বুর করলেন। এখন আজ সারাদিনে বাস্তবায়ন করার মতো ১টি মাত্র ছোট আমল কী হতে পারে?”",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                            fontFamily = banglaFont,
                            lineHeight = 22.sp
                        )
                    )
                }
            }
        }

        // Action Modes selector (Learn, Reflect, Practice, Avoid, Share, Memorize, Teach)
        item {
            Text(
                text = "প্রয়োগ পদ্ধতি (Action Mode):",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(modes) { m ->
                    val isSel = m == selectedMode
                    FilterChip(
                        selected = isSel,
                        onClick = { onSelectMode(m) },
                        label = {
                            Text(
                                text = "${m.emoji} ${m.titleBn}",
                                fontFamily = banglaFont,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Plan Duration selector (Today, 3-day, 7-day, Weekly, Habit)
        item {
            Text(
                text = "পরিকল্পনার মেয়াদ (Action Plan):",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                items(plans) { p ->
                    val isSel = p == selectedPlan
                    FilterChip(
                        selected = isSel,
                        onClick = { onSelectPlan(p) },
                        label = {
                            Text(
                                text = "${p.titleBn} (${p.subtitleBn})",
                                fontFamily = banglaFont,
                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // Today's Action Concrete Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = "🎯 Today's Action (${selectedPlan.titleBn})",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }

                        IconButton(onClick = onToggleEditing) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "কাস্টমাইজ",
                                tint = if (isEditing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (isEditing) {
                        OutlinedTextField(
                            value = actionText,
                            onValueChange = onUpdateActionText,
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("আপনার পছন্দমতো আমল কাস্টমাইজ করুন", fontFamily = banglaFont) },
                            shape = RoundedCornerShape(12.dp)
                        )
                    } else {
                        Text(
                            text = actionText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = banglaFont,
                                lineHeight = 26.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Buttons: ✓ I'll do this, ✏️ Customize, 🔄 Give me another idea
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onCommitAction,
                            modifier = Modifier.weight(1.3f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "✓ I'll do this",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }

                        OutlinedButton(
                            onClick = onNextIdea,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "🔄 অন্য আইডিয়া",
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }
        }

        // Voluntary nature safeguard reminder
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "🛡️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "স্মরণীয়: এই অনুশীলনটি আয়াতের নীতিকে স্মরণে রাখার একটি ব্যক্তিগত মানসিক প্রচেষ্টা। এটিকে শরীয়তের কোনো নতুন ফরজ বা আবশ্যকীয় বিধান হিসেবে গণ্য করা যাবে না।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF92400E),
                            fontFamily = banglaFont,
                            fontSize = 11.5.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }
        }
    }
}

// ==========================================
// 6. EVENING REVIEW PHASE
// ==========================================
@Composable
fun EveningReviewPhaseContent(
    activeCommitment: ActionHistoryRecord?,
    recentHistory: List<ActionHistoryRecord>,
    outcome: ReviewOutcome,
    onSelectOutcome: (ReviewOutcome) -> Unit,
    noticeText: String,
    onUpdateNotice: (String) -> Unit,
    changeText: String,
    onUpdateChange: (String) -> Unit,
    onSubmitReview: (String) -> Unit,
    onExploreNewAyah: () -> Unit,
    banglaFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "🌙 দিনের শেষের আত্মপর্যালোচনা (Evening Review)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            )
            Text(
                text = "আজ সকালে আপনি যে আয়াত নিয়ে সংকল্প করেছিলেন, দিনশেষে তা কেমন গেল?",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            )
        }

        if (activeCommitment != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Text(
                                    text = activeCommitment.surahNameBangla,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontFamily = banglaFont
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "আজকের নির্ধারিত আমল:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = activeCommitment.actionText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = banglaFont,
                                lineHeight = 22.sp
                            )
                        )
                    }
                }
            }

            // How did it go?
            item {
                Text(
                    text = "How did it go? (আমলটি কেমন হলো?)",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ReviewOutcome.entries.forEach { out ->
                        val isSel = out == outcome
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSel) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.5.dp,
                                if (isSel) MaterialTheme.colorScheme.primary else Color.Transparent
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { onSelectOutcome(out) }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 12.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(text = out.emoji, fontSize = 20.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = out.titleBn,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                        fontFamily = banglaFont,
                                        textAlign = TextAlign.Center
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Follow-up question 1: What did you notice?
            item {
                OutlinedTextField(
                    value = noticeText,
                    onValueChange = onUpdateNotice,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("What did you notice? (আজকের অভিজ্ঞতা বা অনুভূতি)", fontFamily = banglaFont) },
                    placeholder = { Text("যেমন: শান্ত থাকার চেষ্টা করায় পরিস্থিতি জটিল হয়নি...", fontFamily = banglaFont) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Follow-up question 2: Did this ayah change how you handled anything today?
            item {
                OutlinedTextField(
                    value = changeText,
                    onValueChange = onUpdateChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Did this ayah change how you handled anything today?", fontFamily = banglaFont) },
                    placeholder = { Text("আয়াতটির কথা মনে থাকায় আমি উত্তর না দিয়ে চুপ ছিলাম...", fontFamily = banglaFont) },
                    shape = RoundedCornerShape(12.dp)
                )
            }

            // Submit Review Button
            item {
                Button(
                    onClick = { onSubmitReview(activeCommitment.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "পর্যালোচনা সংরক্ষণ ও শিখনে অন্তর্ভুক্ত করুন",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    )
                }
            }
        } else {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "✨", fontSize = 28.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "আজকের জন্য কোনো সক্রিয় আমল বাকি নেই!",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                        )
                        Text(
                            text = "নতুন একটি আয়াত পড়ে আজকের আমল নির্ধারণ করুন এবং সন্ধ্যায় তা পর্যালোচনা করুন।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont,
                                textAlign = TextAlign.Center
                            )
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(onClick = onExploreNewAyah) {
                            Text("নতুন আয়াত অন্বেষণ করুন", fontFamily = banglaFont)
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 7. ASK THE AYAH PHASE (Contextual Q&A)
// ==========================================
@Composable
fun AskAyahPhaseContent(
    insight: AyahActionInsight,
    inputQuestion: String,
    onUpdateInput: (String) -> Unit,
    isLoading: Boolean,
    answer: AskAyahAnswer?,
    onAsk: (String) -> Unit,
    banglaFont: FontFamily
) {
    val quickQuestions = listOf(
        "কর্মক্ষেত্রে এটি কীভাবে প্রয়োগ করব?",
        "রাগ বা ক্রোধ নিয়ন্ত্রণে আয়াতটির শিক্ষা কী?",
        "আয়াতটির শান-এ-নুযুল বা ঐতিহাসিক প্রেক্ষাপট কী?",
        "এটি কি আদেশ, উপদেশ, ঘটনা, না সতর্কবার্তা?"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "💬 Ask the Ayah (আয়াতভিত্তিক প্রাসঙ্গিক প্রশ্নোত্তর)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            )
            Text(
                text = "পবিত্র এই আয়াতের অর্থ, ব্যাখ্যা ও বাস্তব প্রয়োগ নিয়ে যেকোনো প্রশ্ন করুন:",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            )
        }

        // Quick Question Chips
        item {
            Text(
                text = "ঘন ঘন জিজ্ঞাসিত প্রশ্নসমূহ:",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            )
            Spacer(modifier = Modifier.height(6.dp))
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                quickQuestions.forEach { q ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                onUpdateInput(q)
                                onAsk(q)
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "❓", fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = q,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = banglaFont,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                }
            }
        }

        // Custom Input Box
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputQuestion,
                    onValueChange = onUpdateInput,
                    modifier = Modifier.weight(1f),
                    placeholder = { Text("আপনার নিজস্ব প্রশ্ন টাইপ করুন...", fontFamily = banglaFont) },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        if (inputQuestion.isNotBlank()) {
                            onAsk(inputQuestion)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    enabled = inputQuestion.isNotBlank() && !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White)
                    } else {
                        Text("জানুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Answer Card
        if (answer != null) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "প্রশ্ন: ${answer.question}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer
                            ) {
                                Text(
                                    text = answer.classificationBn,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 10.dp),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        Text(
                            text = answer.answerBn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = banglaFont,
                                lineHeight = 22.sp
                            )
                        )

                        if (answer.scholarlyBasisBn.isNotBlank()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "📚", fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "উৎস: ${answer.scholarlyBasisBn}",
                                        style = MaterialTheme.typography.labelSmall.copy(
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
}

// ==========================================
// 8. MY JOURNEY PHASE (Non-Numerical Qualitative Growth)
// ==========================================
@Composable
fun MyJourneyPhaseContent(
    records: List<ActionHistoryRecord>,
    themeCounts: Map<String, Int>,
    onToggleComplete: (String, Boolean) -> Unit,
    onSelectRecordAyah: (ActionHistoryRecord) -> Unit,
    banglaFont: FontFamily
) {
    val totalExplored = records.map { it.ayahId }.distinct().size
    val totalCompleted = records.count { it.isCompleted }
    val totalReflected = records.count { it.userPersonalNote.isNotBlank() || it.userReflectionAnswers.isNotEmpty() }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Milestone Cards (Non-numerical competitive scoring — purely qualitative summary)
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "📜 My Quran Journey (আমার কুরআন জার্নি)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Text(
                        text = "আধ্যাত্মিকতা কোনো প্রতিযোগিতামূলক স্কোর নয়; এটি জীবনের সাথে কুরআনের গভীর সম্পর্কের গল্প।",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        JourneyStatBadge(count = totalExplored.toString(), label = "আয়াত অন্বেষণ", banglaFont = banglaFont)
                        JourneyStatBadge(count = totalCompleted.toString(), label = "সম্পন্ন আমল", banglaFont = banglaFont)
                        JourneyStatBadge(count = totalReflected.toString(), label = "সংরক্ষিত তাদাব্বুর", banglaFont = banglaFont)
                    }
                }
            }
        }

        // Encountered Themes Qualitative Map
        if (themeCounts.isNotEmpty()) {
            item {
                Text(
                    text = "🌱 সম্প্রতি যেসব বিষয়ে আপনি তাদাব্বুর করেছেন:",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(themeCounts.entries.toList()) { entry ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "🌿", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = entry.key,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "${entry.value}",
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontSize = 10.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Timeline of Commitments
        item {
            Text(
                text = "আমলের ইতিহাস ও পর্যালোচনা ডায়েরি:",
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            )
        }

        if (records.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📖", fontSize = 32.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "এখনও কোনো আমল যুক্ত করা হয়নি",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = banglaFont
                            )
                        )
                        Text(
                            text = "প্রথম ট্যাবে গিয়ে একটি আয়াত নির্বাচন করে 'Apply This Ayah' ট্যাপ করুন।",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        )
                    }
                }
            }
        } else {
            items(records) { rec ->
                HistoryRecordCard(
                    record = rec,
                    onToggleComplete = { onToggleComplete(rec.id, !rec.isCompleted) },
                    onCardClick = { onSelectRecordAyah(rec) },
                    banglaFont = banglaFont
                )
            }
        }
    }
}

@Composable
fun JourneyStatBadge(
    count: String,
    label: String,
    banglaFont: FontFamily
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        )
    }
}

@Composable
fun HistoryRecordCard(
    record: ActionHistoryRecord,
    onToggleComplete: () -> Unit,
    onCardClick: () -> Unit,
    banglaFont: FontFamily
) {
    val dateStr = remember(record.createdAt) {
        SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(record.createdAt))
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (record.isCompleted) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (record.isCompleted) Color(0xFF16A34A).copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier.clickable { onCardClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = record.selectedSphere.emoji, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = record.surahNameBangla,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (record.isCompleted) Color(0xFFDCFCE7) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (record.isCompleted) "✓ সম্পন্ন" else "চলমান",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (record.isCompleted) Color(0xFF15803D) else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = record.actionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = banglaFont,
                    lineHeight = 20.sp
                )
            )

            if (record.eveningReview != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF8FAFC)
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = record.eveningReview.outcome.emoji, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "সন্ধ্যা পর্যালোচনা: ${record.eveningReview.outcome.titleBn} • '${record.eveningReview.whatDidYouNotice.ifBlank { "পর্যালোচনা সম্পন্ন" }}'",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF334155),
                                fontFamily = banglaFont,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = dateStr,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.5.sp
                    )
                )

                TextButton(onClick = onToggleComplete) {
                    Text(
                        text = if (record.isCompleted) "পুনরায় সক্রিয় করুন" else "✓ সম্পন্ন চিহ্নিত করুন",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }
}

// ==========================================
// 9. BOTTOM SHEETS (Sources & Deep Dive)
// ==========================================
@Composable
fun SourcesBottomSheetContent(
    sources: List<com.example.data.model.TafsirSourceItem>,
    onDismiss: () -> Unit,
    banglaFont: FontFamily
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📚 তাফসীর ও প্রামাণ্য উৎসসমূহ (View Sources)",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Check, contentDescription = "বন্ধ করুন")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (sources.isEmpty()) {
            Text(
                text = "উৎস: তাফসীরে ইবনে কাসীর, তাফসীরে মা'আরিফুল কুরআন ও তাফসীর আস-সা'দী।",
                fontFamily = banglaFont
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                sources.forEach { s ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = s.sourceNameBn,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = s.scholarOrBook,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = s.summaryBn,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontFamily = banglaFont,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@Composable
fun DeepDiveBottomSheetContent(
    insight: AyahActionInsight,
    onDismiss: () -> Unit,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🔬 ডিপ ডাইভ মোড (উচ্চতর গবেষণা)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Check, contentDescription = "বন্ধ করুন")
                }
            }
        }

        // Arabic Terms
        if (insight.keyArabicTerms.isNotEmpty()) {
            item {
                Text(
                    text = "📖 মূল আরবি শব্দ বিশ্লেষণ (Key Vocabulary):",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    insight.keyArabicTerms.forEach { term ->
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = term.arabicWord,
                                        style = MaterialTheme.typography.titleMedium.copy(fontFamily = arabicFont)
                                    )
                                    Text(
                                        text = "অর্থ: ${term.banglaMeaning}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontFamily = banglaFont,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                if (term.spiritualDepthBn.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = term.spiritualDepthBn,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontFamily = banglaFont,
                                            fontSize = 11.5.sp,
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

        // Immediate Context / Asbab al-Nuzul
        if (insight.immediateContextBn.isNotBlank()) {
            item {
                Text(
                    text = "📜 ঐতিহাসিক প্রেক্ষাপট ও শান-এ-নুযুল:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = insight.immediateContextBn,
                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont, lineHeight = 20.sp)
                )
            }
        }

        // Related Hadiths
        if (insight.relatedHadiths.isNotEmpty()) {
            item {
                Text(
                    text = "📚 প্রাসঙ্গিক সহীহ হাদিস:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    insight.relatedHadiths.forEach { h ->
                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = h.bookSource,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFDCFCE7)) {
                                        Text(
                                            text = h.gradeBn,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF15803D), fontSize = 10.sp)
                                        )
                                    }
                                }
                                Text(
                                    text = "বর্ণনাকারী: ${h.narrator}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = banglaFont, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = h.hadithTextBn,
                                    style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont, lineHeight = 18.sp)
                                )
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
