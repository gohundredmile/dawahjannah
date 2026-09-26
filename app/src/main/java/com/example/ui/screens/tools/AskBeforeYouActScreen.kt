package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.AskActionKnowledgeBase
import com.example.data.model.AskBeforeYouActReport
import com.example.data.model.AskScenario
import com.example.data.model.DiagnosticOption
import com.example.data.model.DiagnosticQuestion
import com.example.data.model.DocumentClauseAnalysis
import com.example.data.model.FiqhMaxim
import com.example.data.model.HalalAlternative
import com.example.data.model.IslamicSourceType
import com.example.data.model.ScholarlyPosition
import com.example.data.model.ShariahAssessmentStatus
import com.example.data.model.VerifiedHadithProof
import com.example.data.model.VerifiedQuranProof
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.AskBeforeYouActAiService
import com.example.util.CalendarHelper
import kotlinx.coroutines.launch

private enum class ScreenStage {
    INPUT_SCENARIO,
    DIAGNOSTIC_QUESTIONS,
    SCHOLARLY_REPORT
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AskBeforeYouActScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val aiService = remember { AskBeforeYouActAiService(context) }

    var currentStage by remember { mutableStateOf(ScreenStage.INPUT_SCENARIO) }
    var selectedHomeTab by remember { mutableIntStateOf(0) } // 0: Scenarios & Ask, 1: Document/Contract Analyzer, 2: Saved
    var userQueryText by remember { mutableStateOf("") }
    var documentInputText by remember { mutableStateOf("") }

    var selectedScenario by remember { mutableStateOf(AskActionKnowledgeBase.scenarios.first()) }
    var activeQuestions by remember { mutableStateOf(selectedScenario.diagnosticQuestions) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, String>() }

    var isAnalyzing by remember { mutableStateOf(false) }
    var analyzedClauses = remember { mutableStateListOf<DocumentClauseAnalysis>() }
    var generatedReport by remember { mutableStateOf<AskBeforeYouActReport?>(null) }
    var selectedReportTab by remember { mutableIntStateOf(0) }
    var isReportSaved by remember { mutableStateOf(false) }

    // BackHandler for custom stages
    BackHandler {
        when (currentStage) {
            ScreenStage.INPUT_SCENARIO -> onNavigateBack()
            ScreenStage.DIAGNOSTIC_QUESTIONS -> {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--
                } else {
                    currentStage = ScreenStage.INPUT_SCENARIO
                }
            }
            ScreenStage.SCHOLARLY_REPORT -> {
                currentStage = ScreenStage.DIAGNOSTIC_QUESTIONS
            }
        }
    }

    fun startDiagnosticForScenario(scenario: AskScenario, customQuery: String = "", pastedDoc: String = "") {
        selectedScenario = scenario
        userQueryText = customQuery.ifBlank { scenario.sampleQuery }
        documentInputText = pastedDoc
        userAnswers.clear()
        analyzedClauses.clear()

        isAnalyzing = true
        coroutineScope.launch {
            try {
                if (pastedDoc.isNotBlank()) {
                    val clauses = aiService.analyzeDocumentClauses(pastedDoc)
                    analyzedClauses.clear()
                    analyzedClauses.addAll(clauses)
                }

                // If user entered a custom novel query, let AI determine 2-4 targeted questions
                val dynamicQs = if (customQuery.isNotBlank() && customQuery != scenario.sampleQuery) {
                    aiService.generateDynamicQuestions(customQuery, pastedDoc)
                } else {
                    scenario.diagnosticQuestions
                }
                activeQuestions = dynamicQs

                activeQuestions.forEach { q ->
                    q.options.firstOrNull()?.let { firstOpt ->
                        userAnswers[q.id] = firstOpt.id
                    }
                }
                currentQuestionIndex = 0
                currentStage = ScreenStage.DIAGNOSTIC_QUESTIONS
            } finally {
                isAnalyzing = false
            }
        }
    }

    fun finishAndGenerateReport() {
        isAnalyzing = true
        coroutineScope.launch {
            try {
                val report = aiService.evaluateAction(
                    intendedAction = userQueryText,
                    answers = userAnswers,
                    documentText = documentInputText,
                    fallbackScenario = selectedScenario
                )
                generatedReport = report
                selectedReportTab = 0
                isReportSaved = false
                currentStage = ScreenStage.SCHOLARLY_REPORT
            } catch (e: Exception) {
                // Guaranteed fallback
                val fallback = AskActionKnowledgeBase.evaluateAnswers(
                    scenario = selectedScenario,
                    userQuery = userQueryText,
                    selectedOptionIds = userAnswers,
                    documentClauses = analyzedClauses
                )
                generatedReport = fallback
                selectedReportTab = 0
                isReportSaved = false
                currentStage = ScreenStage.SCHOLARLY_REPORT
            } finally {
                isAnalyzing = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Ask Before You Act",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF059669).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "শরঈ সিদ্ধান্ত-সহায়িকা",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669)
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Text(
                            text = when (currentStage) {
                                ScreenStage.INPUT_SCENARIO -> "পদক্ষেপ নেওয়ার আগে শরঈ যাচাই ও চুক্তি পরীক্ষা"
                                ScreenStage.DIAGNOSTIC_QUESTIONS -> "কাঠামোগত অনুসন্ধানী প্রশ্নমালা (${currentQuestionIndex + 1}/${activeQuestions.size})"
                                ScreenStage.SCHOLARLY_REPORT -> "সার্বিক শরঈ সিদ্ধান্ত-সহায়িকা প্রতিবেদন"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            when (currentStage) {
                                ScreenStage.INPUT_SCENARIO -> onNavigateBack()
                                ScreenStage.DIAGNOSTIC_QUESTIONS -> {
                                    if (currentQuestionIndex > 0) {
                                        currentQuestionIndex--
                                    } else {
                                        currentStage = ScreenStage.INPUT_SCENARIO
                                    }
                                }
                                ScreenStage.SCHOLARLY_REPORT -> {
                                    currentStage = ScreenStage.DIAGNOSTIC_QUESTIONS
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    if (currentStage == ScreenStage.SCHOLARLY_REPORT) {
                        // Bookmark / Save Action
                        IconButton(
                            onClick = {
                                isReportSaved = !isReportSaved
                                val msg = if (isReportSaved) "প্রতিবেদনটি সফলভাবে সংরক্ষিত হয়েছে" else "সংরক্ষণ বাতিল করা হয়েছে"
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        ) {
                            Icon(
                                imageVector = if (isReportSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "সংরক্ষণ করুন",
                                tint = if (isReportSaved) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Share Action
                        IconButton(
                            onClick = {
                                generatedReport?.let { rep ->
                                    val shareText = buildString {
                                        appendLine("【 Ask Before You Act • শরঈ বিশ্লেষণ প্রতিবেদন 】")
                                        appendLine("বিষয়: ${rep.query}")
                                        appendLine("মর্যাদা: ${rep.assessment.titleBn}")
                                        appendLine("----------------------------------------")
                                        appendLine(rep.assessmentSummaryBn)
                                        appendLine("\n[করণীয় পরবর্তী পদক্ষেপ]:")
                                        rep.practicalNextSteps.forEach { appendLine("• $it") }
                                        appendLine("\nউৎস: দা'ওয়াহ টু জান্নাহ - Ask Before You Act")
                                    }
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, shareText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(sendIntent, "প্রতিবেদন শেয়ার করুন"))
                                }
                            }
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "শেয়ার করুন")
                        }

                        // Copy Action
                        IconButton(
                            onClick = {
                                generatedReport?.let { rep ->
                                    val fullSummary = buildString {
                                        appendLine("【 Ask Before You Act • শরঈ সিদ্ধান্ত-সহায়িকা প্রতিবেদন 】")
                                        appendLine("বিষয়: ${rep.query}")
                                        appendLine("মর্যাদা: ${rep.assessment.titleBn} (${rep.assessment.labelBn})")
                                        appendLine("----------------------------------------")
                                        appendLine("\n[পরিস্থিতির সারসংক্ষেপ]:\n${rep.whatIUnderstandBn}")
                                        appendLine("\n[মূল্যায়ন]:\n${rep.assessmentSummaryBn}")
                                        if (rep.importantFacts.isNotEmpty()) {
                                            appendLine("\n[গুরুত্বপূর্ণ শর্ত ও উপাদানসমূহ]:")
                                            rep.importantFacts.forEach { appendLine("• $it") }
                                        }
                                        if (rep.whatIsStillUnclear.isNotEmpty()) {
                                            appendLine("\n[অস্পষ্ট বিষয়সমূহ]:")
                                            rep.whatIsStillUnclear.forEach { appendLine("• $it") }
                                        }
                                        appendLine("\n[পরবর্তী বাস্তব পদক্ষেপ]:")
                                        rep.practicalNextSteps.forEach { appendLine("• $it") }
                                        appendLine("\n[মূল কুরআন ও সুন্নাহর প্রামাণ্য দলিল]:")
                                        rep.quranProofs.take(2).forEach {
                                            appendLine("• ${it.surahNameBn} [${it.surahNumber}:${it.ayahNumber}]: ${it.banglaTranslation}")
                                        }
                                        appendLine("----------------------------------------")
                                        appendLine("দ্রষ্টব্য: এটি কোনো ব্যক্তিগত ফতোয়া নয়; জটিল বিষয়ে অভিজ্ঞ মুফতির শরণাপন্ন হোন।")
                                    }
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Ask Before You Act Report", fullSummary))
                                    Toast.makeText(context, "পূর্ণাঙ্গ প্রতিবেদন ক্লিপবোর্ডে কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                }
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "কপি করুন")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = currentStage,
                transitionSpec = {
                    (fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = tween(300)))
                        .togetherWith(fadeOut(animationSpec = tween(200)))
                },
                label = "AskBeforeYouActStage"
            ) { stage ->
                when (stage) {
                    ScreenStage.INPUT_SCENARIO -> InputAndScenarioSelectionView(
                        selectedTab = selectedHomeTab,
                        onTabChange = { selectedHomeTab = it },
                        userQuery = userQueryText,
                        onQueryChange = { userQueryText = it },
                        documentText = documentInputText,
                        onDocumentTextChange = { documentInputText = it },
                        onStartDiagnostic = {
                            val matched = AskActionKnowledgeBase.findScenario(userQueryText)
                                ?: AskActionKnowledgeBase.scenarios.first()
                            startDiagnosticForScenario(matched, userQueryText, documentInputText)
                        },
                        onSelectCuratedScenario = { scenario ->
                            startDiagnosticForScenario(scenario, scenario.sampleQuery, documentInputText)
                        },
                        onAnalyzeDocumentOnly = {
                            coroutineScope.launch {
                                isAnalyzing = true
                                val clauses = aiService.analyzeDocumentClauses(documentInputText)
                                analyzedClauses.clear()
                                analyzedClauses.addAll(clauses)
                                val matched = AskActionKnowledgeBase.findScenario(documentInputText)
                                    ?: AskActionKnowledgeBase.scenarios.first()
                                startDiagnosticForScenario(matched, "চুক্তিপত্র ও শর্তাবলি পরীক্ষা", documentInputText)
                                isAnalyzing = false
                            }
                        },
                        isAnalyzing = isAnalyzing
                    )

                    ScreenStage.DIAGNOSTIC_QUESTIONS -> StructuredQuestionnaireView(
                        scenario = selectedScenario,
                        questions = activeQuestions,
                        currentQuestionIndex = currentQuestionIndex,
                        userAnswers = userAnswers,
                        isAnalyzing = isAnalyzing,
                        onSelectOption = { questionId, optionId ->
                            userAnswers[questionId] = optionId
                        },
                        onPreviousQuestion = {
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        },
                        onNextQuestion = {
                            if (currentQuestionIndex < activeQuestions.size - 1) {
                                currentQuestionIndex++
                            } else {
                                finishAndGenerateReport()
                            }
                        }
                    )

                    ScreenStage.SCHOLARLY_REPORT -> generatedReport?.let { report ->
                        ComprehensiveScholarlyReportView(
                            report = report,
                            selectedTab = selectedReportTab,
                            onTabChange = { selectedReportTab = it },
                            onStartNewQuery = {
                                userQueryText = ""
                                documentInputText = ""
                                analyzedClauses.clear()
                                currentStage = ScreenStage.INPUT_SCENARIO
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Stage 1: Input Query, Curated Scenarios, and Document/Contract Clause Analyzer
 */
@Composable
private fun InputAndScenarioSelectionView(
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    userQuery: String,
    onQueryChange: (String) -> Unit,
    documentText: String,
    onDocumentTextChange: (String) -> Unit,
    onStartDiagnostic: () -> Unit,
    onSelectCuratedScenario: (AskScenario) -> Unit,
    onAnalyzeDocumentOnly: () -> Unit,
    isAnalyzing: Boolean
) {
    val banglaFont = LocalBanglaFontFamily.current

    Column(modifier = Modifier.fillMaxSize()) {
        // Tab Row: Scenarios vs Contract Clause Scanner
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color(0xFF059669)
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { onTabChange(0) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("অনুসন্ধান ও বিষয়সমূহ", fontFamily = banglaFont, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { onTabChange(1) },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("চুক্তি ও শর্ত স্ক্যানার", fontFamily = banglaFont, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            )
        }

        if (selectedTab == 0) {
            // TAB 0: Guided Scenario & Query
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Hero Card Explaining the Non-Binary Philosophy
                item {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.5f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF059669).copy(alpha = 0.12f),
                                            IslamicGold.copy(alpha = 0.04f)
                                        )
                                    )
                                )
                                .padding(18.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF059669),
                                        modifier = Modifier.size(36.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = Icons.Default.Psychology,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = "পদক্ষেপ নেওয়ার আগে জানুন",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )
                                        Text(
                                            text = "Islamic Decision-Support & Guidance Engine",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "বাস্তব জীবনের জটিল আর্থিক চুক্তি বা কর্মকাণ্ডকে সরাসরি কোনো বাইনারি 'হালাল/হারাম' বাটনে সংকুচিত করা অনুচিত। এই ইঞ্জিনটি প্রয়োজনীয় প্রশ্ন তুলে ধরে, সুপ্ত সুদী বা ঝুঁকিপূর্ণ ধারা চিহ্নিত করে এবং প্রামাণ্য কুরআন ও সুন্নাহর আলোকে সচেতন সিদ্ধান্ত নিতে সাহায্য করে।",
                                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "উসূলুল ফিকহ: \"الحكم على الشيء فرع عن تصوره\" — কোনো বিষয়ের হুকুম দেওয়ার পূর্বে তার পূর্ণ বাস্তবতা উপলব্ধি অপরিহার্য।",
                                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Custom Query Input Field
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "আপনার উদ্দিষ্ট পদক্ষেপ বা লেনদেন লিখুন:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = userQuery,
                                onValueChange = onQueryChange,
                                placeholder = {
                                    Text(
                                        text = "যেমন: \"আমি ক্লায়েন্টের জন্য ফিনটেক অ্যাপ বানাচ্ছি\" অথবা \"ব্যাংক থেকে হোম লোন নেওয়ার কথা ভাবছি\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                        fontFamily = banglaFont
                                    )
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF059669),
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                ),
                                trailingIcon = {
                                    if (userQuery.isNotBlank()) {
                                        IconButton(onClick = { onQueryChange("") }) {
                                            Icon(Icons.Default.Clear, contentDescription = "মুছুন")
                                        }
                                    }
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = onStartDiagnostic,
                                enabled = userQuery.isNotBlank() && !isAnalyzing,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                            ) {
                                if (isAnalyzing) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("শরঈ উপাদান বিশ্লেষণ হচ্ছে...", fontFamily = banglaFont)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "শরঈ অনুসন্ধানী চেকার চালু করুন",
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }
                }

                // Featured 8 Life Situations
                item {
                    Text(
                        text = "অথবা সর্বাধিক জিজ্ঞাসিত ৮টি মৌলিক ক্ষেত্র থেকে নির্বাচন করুন:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                }

                items(AskActionKnowledgeBase.scenarios) { scenario ->
                    ScenarioCard(
                        scenario = scenario,
                        onClick = { onSelectCuratedScenario(scenario) }
                    )
                }
            }
        } else {
            // TAB 1: Document & Contract Clause Analyzer (Section 17)
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.2.dp, Color(0xFF4F46E5).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFF4F46E5).copy(alpha = 0.15f),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Default.Description, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(20.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "চুক্তি ও শর্তাবলি স্ক্যানার (Contract Analyzer)",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = "Document-Aware Islamic Legal Inspector",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color(0xFF4F46E5)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "আপনার লোন চুক্তি, ক্রেডিট কার্ড পলিসি, ফ্রিল্যান্স কন্ট্রাক্ট, ই-কমার্স ড্রপশিপিং শর্তাবলি বা পণ্যের ব্রোশারের সংশ্লিষ্ট ধারাগুলো এখানে পেস্ট করুন। ইঞ্জিনটি প্রতিটি ধারা উদ্ধৃত করে তার শরঈ তাৎপর্য বিশ্লেষণ করবে।",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            // Sample Presets for Fast Testing
                            Text(
                                text = "দ্রুত পরীক্ষার জন্য নমুনা চুক্তি বেছে নিন:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                                    modifier = Modifier.clickable {
                                        onDocumentTextChange(
                                            "Clause 4.1: The Borrower agrees to repay the principal amount with an annual interest rate of 12.5% calculated monthly.\n" +
                                            "Clause 8.3: In case of any delayed installment, a late fee penalty of 3% per month will compound and be charged to the account.\n" +
                                            "Clause 11: The Bank reserves the right to amend fees and charges at its sole discretion."
                                        )
                                    }
                                ) {
                                    Text(
                                        text = "ব্যাংক লোন চুক্তি",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontFamily = banglaFont
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                                    modifier = Modifier.clickable {
                                        onDocumentTextChange(
                                            "Clause 2: Freelancer shall develop software modules for online lottery and casino simulation.\n" +
                                            "Clause 5: Payment shall be disbursed upon delivery, and all intellectual property transfers exclusively to Client."
                                        )
                                    }
                                ) {
                                    Text(
                                        text = "ফ্রিল্যান্স আইটি চুক্তি",
                                        style = MaterialTheme.typography.labelSmall,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = documentText,
                                onValueChange = onDocumentTextChange,
                                placeholder = {
                                    Text(
                                        text = "চুক্তির ধারা বা শর্তাবলি এখানে পেস্ট করুন...",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = banglaFont
                                    )
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF4F46E5),
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                                )
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = onAnalyzeDocumentOnly,
                                enabled = documentText.isNotBlank() && !isAnalyzing,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                            ) {
                                if (isAnalyzing) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("নথির ধারা পরীক্ষা হচ্ছে...", fontFamily = banglaFont)
                                } else {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "চুক্তির ধারা ও ঝুঁকি পরীক্ষা করুন",
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
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

@Composable
private fun ScenarioCard(
    scenario: AskScenario,
    onClick: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    val iconVector = when (scenario.badgeIconName) {
        "AccountBalance" -> Icons.Default.AccountBalance
        "TrendingUp" -> Icons.Default.TrendingUp
        "ShoppingCart" -> Icons.Default.ShoppingCart
        "Security" -> Icons.Default.Security
        "Work" -> Icons.Default.Work
        "Restaurant" -> Icons.Default.Restaurant
        "Hub" -> Icons.Default.Hub
        "Paid" -> Icons.Default.Paid
        else -> Icons.Default.Spa
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF059669).copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = iconVector,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = scenario.titleBn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "${scenario.diagnosticQuestions.size}টি প্রশ্ন",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontFamily = banglaFont
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = scenario.shortSummaryBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

/**
 * Stage 2: Structured Interactive Questionnaire
 */
@Composable
private fun StructuredQuestionnaireView(
    scenario: AskScenario,
    questions: List<DiagnosticQuestion>,
    currentQuestionIndex: Int,
    userAnswers: Map<String, String>,
    isAnalyzing: Boolean,
    onSelectOption: (String, String) -> Unit,
    onPreviousQuestion: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val currentQuestion = questions.getOrNull(currentQuestionIndex) ?: return
    val selectedOptionId = userAnswers[currentQuestion.id] ?: currentQuestion.options.firstOrNull()?.id ?: ""
    val progress = (currentQuestionIndex + 1).toFloat() / questions.size.toFloat()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Progress Indicator
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${scenario.titleBn} • প্রশ্ন ${CalendarHelper.toBanglaNumber(currentQuestionIndex + 1)} / ${CalendarHelper.toBanglaNumber(questions.size)}",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF059669),
                fontFamily = banglaFont
            )
            Text(
                text = "${(progress * 100).toInt()}% সম্পন্ন",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color(0xFF059669),
            trackColor = Color(0xFF059669).copy(alpha = 0.2f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Main Question Card
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.4f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF059669).copy(alpha = 0.15f),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Help,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "কাঠামোগত শরঈ জিজ্ঞাসা:",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF059669),
                                fontFamily = banglaFont
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = currentQuestion.questionBn,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 17.sp, lineHeight = 24.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currentQuestion.questionEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // "Why It Matters" Insight
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp)) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "কেন এই তথ্যটি জরুরি (শরঈ কারণ / 'ইল্লত):",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.secondary,
                                        fontFamily = banglaFont
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = currentQuestion.whyItMattersBn,
                                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Options List (including unknown / skip options)
            items(currentQuestion.options) { option ->
                QuestionOptionCard(
                    option = option,
                    isSelected = (option.id == selectedOptionId),
                    onSelect = { onSelectOption(currentQuestion.id, option.id) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Navigation Controls
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPreviousQuestion,
                enabled = currentQuestionIndex > 0 && !isAnalyzing,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "পূর্ববর্তী", fontFamily = banglaFont)
            }

            Button(
                onClick = onNextQuestion,
                enabled = !isAnalyzing,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("প্রতিবেদন তৈরি হচ্ছে...", fontFamily = banglaFont)
                } else {
                    Text(
                        text = if (currentQuestionIndex < questions.size - 1) "পরবর্তী প্রশ্ন" else "সম্পূর্ণ শরঈ প্রতিবেদন দেখুন",
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (currentQuestionIndex < questions.size - 1) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionOptionCard(
    option: DiagnosticOption,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF059669) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
        label = "BorderColor"
    )

    val containerColor = if (isSelected) {
        Color(0xFF059669).copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF059669))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = option.labelBn,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = option.impactExplanationBn,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

/**
 * Stage 3: Comprehensive Scholarly Decision-Support Report (Sections 9, 20 & 25)
 */
@Composable
private fun ComprehensiveScholarlyReportView(
    report: AskBeforeYouActReport,
    selectedTab: Int,
    onTabChange: (Int) -> Unit,
    onStartNewQuery: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val hasClauses = report.clauseAnalyses.isNotEmpty()
    val tabTitles = if (hasClauses) {
        listOf("🧭 সারসংক্ষেপ ও পরিস্থিতি", "📄 চুক্তির ধারা বিশ্লেষণ", "📖 কুরআন ও সুন্নাহ দলিল", "⚖️ ফিকহ ও মাযহাব", "💡 হালাল বিকল্প")
    } else {
        listOf("🧭 সারসংক্ষেপ ও পরিস্থিতি", "📖 কুরআন ও সুন্নাহ দলিল", "⚖️ ফিকহ ও মাযহাব", "💡 হালাল বিকল্প")
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Status Banner with Nuanced Classification (NO HALAL PERCENTAGES)
        Surface(
            color = Color(report.assessment.primaryColorHex).copy(alpha = 0.12f),
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
                        color = Color(report.assessment.primaryColorHex)
                    ) {
                        Text(
                            text = report.assessment.labelBn,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = banglaFont
                        )
                    }

                    Text(
                        text = report.matchedScenarioTitleBn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = report.assessment.titleBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(report.assessment.primaryColorHex),
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = report.assessment.subtitleBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab.coerceIn(0, tabTitles.size - 1),
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = Color(0xFF059669),
            edgePadding = 16.dp
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { onTabChange(index) },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal),
                            fontFamily = banglaFont
                        )
                    }
                )
            }
        }

        // Tab Content
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            val adjustedTab = if (hasClauses) selectedTab else if (selectedTab >= 1) selectedTab + 1 else selectedTab

            when (adjustedTab) {
                0 -> {
                    // TAB 0: What I Understand & Important Facts
                    item {
                        WhatIUnderstandCard(report = report)
                    }

                    item {
                        AssessmentSummaryCard(report = report)
                    }

                    if (report.importantFacts.isNotEmpty()) {
                        item {
                            ImportantFactsCard(facts = report.importantFacts)
                        }
                    }

                    if (report.whatIsStillUnclear.isNotEmpty()) {
                        item {
                            WhatIsStillUnclearCard(unclearPoints = report.whatIsStillUnclear)
                        }
                    }

                    item {
                        PracticalNextStepsCard(steps = report.practicalNextSteps)
                    }

                    item {
                        ScholarlyDisclaimerCard()
                    }
                }

                1 -> {
                    // TAB 1: Document Clause Analysis (Section 17)
                    item {
                        SectionHeaderBadge(
                            title = "চুক্তির ধারাসমূহ পরীক্ষা (Clause-by-Clause Inspection)",
                            badgeType = IslamicSourceType.AI_DIAGNOSTIC
                        )
                    }

                    items(report.clauseAnalyses) { clause ->
                        DocumentClauseCard(clause = clause)
                    }
                }

                2 -> {
                    // TAB 2: Quran & Hadith Proofs
                    item {
                        SectionHeaderBadge(
                            title = "পবিত্র কুরআনুল কারীম (কালামুল্লাহ)",
                            badgeType = IslamicSourceType.QURAN
                        )
                    }

                    items(report.quranProofs) { proof ->
                        VerifiedQuranProofCard(proof = proof)
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        SectionHeaderBadge(
                            title = "সহীহ হাদীস (নবুওয়াতী নির্দেশনা)",
                            badgeType = IslamicSourceType.HADITH
                        )
                    }

                    items(report.hadithProofs) { hadith ->
                        VerifiedHadithProofCard(hadith = hadith)
                    }
                }

                3 -> {
                    // TAB 3: Fiqh Maxims & Scholarly Positions
                    item {
                        SectionHeaderBadge(
                            title = "কাওয়াইদে ফিকহিয়্যাহ (শাস্ত্রীয় মূলনীতি)",
                            badgeType = IslamicSourceType.FIQH_MAXIM
                        )
                    }

                    items(report.fiqhMaxims) { maxim ->
                        FiqhMaximCard(maxim = maxim)
                    }

                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        SectionHeaderBadge(
                            title = "স্বীকৃত ফিকহ একাডেমি ও মাযহাবসমূহের পর্যবেক্ষণ",
                            badgeType = IslamicSourceType.SCHOLARLY_COUNCIL
                        )
                    }

                    items(report.scholarlyPositions) { pos ->
                        ScholarlyPositionCard(position = pos)
                    }
                }

                4 -> {
                    // TAB 4: Halal Alternatives
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF059669).copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(12.dp)) {
                                Icon(Icons.Default.Spa, contentDescription = null, tint = Color(0xFF059669))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "ইসলাম কেবল নিষেধ করেই ক্ষান্ত হয় না; প্রতিটি নিষিদ্ধ কাঠামোর বিপরীতে বরকতময় ও ইনসাফপূর্ণ হালাল বিকল্প নির্ধারণ করে দিয়েছে।",
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    items(report.halalAlternatives) { alt ->
                        HalalAlternativeCard(alternative = alt)
                    }
                }
            }

            // Bottom Action: Ask Another Query
            item {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = onStartNewQuery,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "নতুন বিষয় বা চুক্তি যাচাই করুন",
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component Cards for Scholarly Report
// -------------------------------------------------------------

@Composable
private fun SectionHeaderBadge(
    title: String,
    badgeType: IslamicSourceType
) {
    val banglaFont = LocalBanglaFontFamily.current

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(badgeType.badgeColorHex).copy(alpha = 0.12f),
        border = BorderStroke(0.8.dp, Color(badgeType.badgeColorHex).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                contentDescription = null,
                tint = Color(badgeType.badgeColorHex),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(badgeType.badgeColorHex),
                    fontFamily = banglaFont
                )
                Text(
                    text = badgeType.descriptionBn,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.5.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
private fun WhatIUnderstandCard(report: AskBeforeYouActReport) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Psychology,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "🧭 পরিস্থিতির বিবরণ (What I Understand):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = report.whatIUnderstandBn,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
private fun AssessmentSummaryCard(report: AskBeforeYouActReport) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(report.assessment.primaryColorHex).copy(alpha = 0.08f)),
        border = BorderStroke(1.2.dp, Color(report.assessment.primaryColorHex).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "📝 শরঈ মূল্যায়ন ও মূল বক্তব্য:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(report.assessment.primaryColorHex),
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = report.assessmentSummaryBn,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
private fun ImportantFactsCard(facts: List<String>) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "🔎 চিহ্নিত গুরুত্বপূর্ণ শরঈ উপাদানসমূহ:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(10.dp))

            facts.forEach { fact ->
                Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                    Text(text = "•", color = Color(0xFF059669), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = fact,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
private fun WhatIsStillUnclearCard(unclearPoints: List<String>) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0284C7).copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF0284C7), modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "⚠️ যা এখনো অস্পষ্ট ও যাচাই আবশ্যক (What Is Still Unclear):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7),
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            unclearPoints.forEach { point ->
                Text(
                    text = "❓ $point",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun PracticalNextStepsCard(steps: List<String>) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF059669),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "✅ বাস্তবসম্মত পরবর্তী পদক্ষেপ (Practical Next Steps):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            steps.forEachIndexed { index, step ->
                Row(modifier = Modifier.padding(vertical = 3.dp), verticalAlignment = Alignment.Top) {
                    Text(
                        text = "${CalendarHelper.toBanglaNumber(index + 1)}.",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = step,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
private fun DocumentClauseCard(clause: DocumentClauseAnalysis) {
    val banglaFont = LocalBanglaFontFamily.current

    val borderColor = if (clause.isConcerning) Color(0xFFDC2626).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    val cardBg = if (clause.isConcerning) Color(0xFFDC2626).copy(alpha = 0.05f) else MaterialTheme.colorScheme.surface

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = clause.clauseTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = if (clause.isConcerning) Color(0xFFDC2626) else MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (clause.isConcerning) Color(0xFFDC2626).copy(alpha = 0.15f) else Color(0xFF059669).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = clause.riskCategoryBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (clause.isConcerning) Color(0xFFDC2626) else Color(0xFF059669)
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quoted Text
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "\"${clause.quotedText}\"",
                    style = MaterialTheme.typography.bodySmall.copy(fontStyle = androidx.compose.ui.text.font.FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "নথির প্রত্যক্ষ অর্থ: ${clause.documentMeaningBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "শরঈ তাৎপর্য: ${clause.islamicSignificanceBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, fontWeight = FontWeight.Medium),
                color = if (clause.isConcerning) Color(0xFFDC2626) else Color(0xFF059669),
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
private fun VerifiedQuranProofCard(proof: VerifiedQuranProof) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val context = LocalContext.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.4f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
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
                        text = "${proof.surahNameBn} [${proof.surahNumber}:${proof.ayahNumber}]",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF059669),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontFamily = banglaFont
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = proof.surahNameAr,
                        style = MaterialTheme.typography.titleMedium,
                        color = IslamicGold,
                        fontFamily = arabicFont
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("Ayah Proof", proof.arabicText))
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

            // Arabic Text with Amiri typography
            val formattedArabic = "${proof.arabicText} ﴿${CalendarHelper.toArabicNumber(proof.ayahNumber)}﴾"
            Text(
                text = formattedArabic,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 23.sp,
                    lineHeight = 44.sp,
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                ),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = arabicFont,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bangla Translation
            Text(
                text = "বাংলা অনুবাদ:",
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = proof.banglaTranslation,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            // English Translation
            Text(
                text = proof.englishTranslation,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 17.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Tafsir Note
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "প্রামাণ্য তাফসীর প্রেক্ষাপট:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = proof.tafsirReferenceBn,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
private fun VerifiedHadithProofCard(hadith: VerifiedHadithProof) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, Color(0xFFB45309).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                    color = Color(0xFFB45309).copy(alpha = 0.15f),
                    border = BorderStroke(0.6.dp, Color(0xFFB45309).copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "${hadith.sourceBookBn} • ${hadith.hadithNumber}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFB45309),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        fontFamily = banglaFont
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = Color(0xFF059669).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = hadith.authenticityGradeBn,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669)
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "বর্ণনাকারী: ${hadith.narratorBn}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Arabic Hadith Text
            Text(
                text = hadith.arabicText,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontSize = 21.sp,
                    lineHeight = 38.sp,
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                ),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = arabicFont,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = hadith.banglaTranslation,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 21.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFB45309).copy(alpha = 0.08f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "হাদীসের ফিকহি নির্দেশ ও তাৎপর্য:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFB45309),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = hadith.legalSignificanceBn,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
private fun FiqhMaximCard(maxim: FiqhMaxim) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF0D9488).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = maxim.arabicText,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    lineHeight = 34.sp,
                    textDirection = TextDirection.Rtl,
                    textAlign = TextAlign.Right
                ),
                color = Color(0xFF0D9488),
                fontFamily = arabicFont,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = maxim.banglaTranslation,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "উৎস: ${maxim.sourceOrOriginBn}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "প্রয়োগ: ${maxim.practicalApplicationBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
private fun ScholarlyPositionCard(position: ScholarlyPosition) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF4F46E5).copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = position.bodyOrSchoolBn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4F46E5),
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = position.verdictSummaryBn,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "যুক্তি ও দলিল: ${position.argumentAndEvidenceBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            if (!position.conditionsBn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF4F46E5).copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "আবশ্যক শর্তাবলি: ${position.conditionsBn}",
                        style = MaterialTheme.typography.labelSmall.copy(lineHeight = 16.sp),
                        color = Color(0xFF4F46E5),
                        modifier = Modifier.padding(8.dp),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
private fun HalalAlternativeCard(alternative: HalalAlternative) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = alternative.titleBn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
                Text(
                    text = alternative.islamicContractBn,
                    style = MaterialTheme.typography.titleSmall,
                    color = IslamicGold,
                    fontFamily = arabicFont
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "পদ্ধতি: ${alternative.howItWorksBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "কেন এটি হালাল: ${alternative.whyItIsHalalBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp, fontWeight = FontWeight.Medium),
                color = Color(0xFF059669),
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
private fun ScholarlyDisclaimerCard() {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "শরঈ দায়বদ্ধতা ও শিক্ষামূলক ডিসক্লেইমার:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "এই তথ্য কেবল ইসলামিক মূলনীতি ও প্রাসঙ্গিক দলিল বোঝার জন্য প্রস্তুত করা হয়েছে। আপনার ব্যক্তিগত নির্দিষ্ট পরিস্থিতির উপর কোনো বাধ্যতামূলক ও চূড়ান্ত ফতোয়া গ্রহণের জন্য একজন নির্ভরযোগ্য বিজ্ঞ মুফতি বা শরীয়াহ কনসালট্যান্টের সাথে পরামর্শ করুন।",
                style = MaterialTheme.typography.labelSmall.copy(lineHeight = 17.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
    }
}
