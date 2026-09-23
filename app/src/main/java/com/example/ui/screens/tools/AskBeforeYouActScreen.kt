package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.data.model.FiqhMaxim
import com.example.data.model.HalalAlternative
import com.example.data.model.IslamicSourceType
import com.example.data.model.ScholarlyPosition
import com.example.data.model.ShariahRiskLevel
import com.example.data.model.VerifiedHadithProof
import com.example.data.model.VerifiedQuranProof
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.CalendarHelper

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
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    var currentStage by remember { mutableStateOf(ScreenStage.INPUT_SCENARIO) }
    var userQueryText by remember { mutableStateOf("") }
    var selectedScenario by remember { mutableStateOf(AskActionKnowledgeBase.scenarios.first()) }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val userAnswers = remember { mutableStateMapOf<String, String>() }
    var generatedReport by remember { mutableStateOf<AskBeforeYouActReport?>(null) }
    var selectedReportTab by remember { mutableIntStateOf(0) }

    fun startDiagnosticForScenario(scenario: AskScenario, customQuery: String = "") {
        selectedScenario = scenario
        userQueryText = customQuery.ifBlank { scenario.sampleQuery }
        userAnswers.clear()
        scenario.diagnosticQuestions.forEach { q ->
            q.options.firstOrNull()?.let { firstOpt ->
                userAnswers[q.id] = firstOpt.id
            }
        }
        currentQuestionIndex = 0
        currentStage = ScreenStage.DIAGNOSTIC_QUESTIONS
    }

    fun finishAndGenerateReport() {
        val report = AskActionKnowledgeBase.evaluateAnswers(
            scenario = selectedScenario,
            userQuery = userQueryText,
            selectedOptionIds = userAnswers
        )
        generatedReport = report
        currentStage = ScreenStage.SCHOLARLY_REPORT
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
                                    text = "শরঈ অনুসন্ধান",
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
                                ScreenStage.INPUT_SCENARIO -> "পদক্ষেপ নেওয়ার আগে শরঈ যাচাই"
                                ScreenStage.DIAGNOSTIC_QUESTIONS -> "কাঠামোগত অনুসন্ধানী প্রশ্নমালা (${currentQuestionIndex + 1}/${selectedScenario.diagnosticQuestions.size})"
                                ScreenStage.SCHOLARLY_REPORT -> "সার্বিক শরঈ পর্যালোচনা ও দলিল"
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
                        IconButton(
                            onClick = {
                                generatedReport?.let { rep ->
                                    val fullSummary = buildString {
                                        appendLine("【 Ask Before You Act • শরঈ বিশ্লেষণ প্রতিবেদন 】")
                                        appendLine("অনুসন্ধান: ${rep.query}")
                                        appendLine("মর্যাদা: ${rep.riskLevel.titleBn}")
                                        appendLine("----------------------------------------")
                                        appendLine(rep.executiveSummaryBn)
                                        if (rep.redFlagsIdentified.isNotEmpty()) {
                                            appendLine("\n[চিহ্নিত সতর্কতা / রেড ফ্ল্যাগ]:")
                                            rep.redFlagsIdentified.forEach { appendLine("• $it") }
                                        }
                                        appendLine("\n[মূল কুরআন ও সুন্নাহর দলিল]:")
                                        rep.quranProofs.take(2).forEach {
                                            appendLine("• ${it.surahNameBn} [${it.surahNumber}:${it.ayahNumber}]: ${it.banglaTranslation}")
                                        }
                                    }
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Ask Before You Act Report", fullSummary))
                                    Toast.makeText(context, "প্রতিবেদন ক্লিপবোর্ডে কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
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
                        userQuery = userQueryText,
                        onQueryChange = { userQueryText = it },
                        onStartDiagnostic = {
                            val matched = AskActionKnowledgeBase.findScenario(userQueryText)
                                ?: AskActionKnowledgeBase.scenarios.first()
                            startDiagnosticForScenario(matched, userQueryText)
                        },
                        onSelectCuratedScenario = { scenario ->
                            startDiagnosticForScenario(scenario, scenario.sampleQuery)
                        }
                    )

                    ScreenStage.DIAGNOSTIC_QUESTIONS -> StructuredQuestionnaireView(
                        scenario = selectedScenario,
                        currentQuestionIndex = currentQuestionIndex,
                        userAnswers = userAnswers,
                        onSelectOption = { questionId, optionId ->
                            userAnswers[questionId] = optionId
                        },
                        onPreviousQuestion = {
                            if (currentQuestionIndex > 0) currentQuestionIndex--
                        },
                        onNextQuestion = {
                            if (currentQuestionIndex < selectedScenario.diagnosticQuestions.size - 1) {
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
 * Stage 1: Input Query & Featured Curated Scenarios
 */
@Composable
private fun InputAndScenarioSelectionView(
    userQuery: String,
    onQueryChange: (String) -> Unit,
    onStartDiagnostic: () -> Unit,
    onSelectCuratedScenario: (AskScenario) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Card Explaining the Philosophy
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
                                    text = "Ask Before You Act • শরঈ পূর্ব-অনুসন্ধান",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "কোনো আর্থিক সিদ্ধান্ত বা চুক্তিতে জড়ানোর আগে সরাসরি একটি 'হালাল' বা 'হারাম' বাটন চাপা বিভ্রান্তিকর। ইসলামী ফিকহে হুকুম দেওয়ার আগে চুক্তির প্রকৃতি, শর্তাবলি ও গোপন বিষয়গুলো স্পষ্ট জানা আবশ্যক।",
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
                        text = "আপনার পরিকল্পনা বা চুক্তিটি লিখুন:",
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
                                text = "যেমন: \"I am planning to take a loan\" অথবা \"আমি ব্যাংক লোন নেওয়ার চিন্তা করছি\"",
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
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onStartDiagnostic,
                        enabled = userQuery.isNotBlank(),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                    ) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White)
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

        // Featured Life Situations
        item {
            Text(
                text = "অথবা সর্বাধিক জিজ্ঞাসিত বিষয়সমূহ থেকে নির্বাচন করুন:",
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
    currentQuestionIndex: Int,
    userAnswers: Map<String, String>,
    onSelectOption: (String, String) -> Unit,
    onPreviousQuestion: () -> Unit,
    onNextQuestion: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val currentQuestion = scenario.diagnosticQuestions.getOrNull(currentQuestionIndex) ?: return
    val selectedOptionId = userAnswers[currentQuestion.id] ?: currentQuestion.options.firstOrNull()?.id ?: ""
    val progress = (currentQuestionIndex + 1).toFloat() / scenario.diagnosticQuestions.size.toFloat()

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
                text = "${scenario.titleBn} • প্রশ্ন ${CalendarHelper.toBanglaNumber(currentQuestionIndex + 1)} / ${CalendarHelper.toBanglaNumber(scenario.diagnosticQuestions.size)}",
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

            // Options List
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
                enabled = currentQuestionIndex > 0,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "পূর্ববর্তী", fontFamily = banglaFont)
            }

            Button(
                onClick = onNextQuestion,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Text(
                    text = if (currentQuestionIndex < scenario.diagnosticQuestions.size - 1) "পরবর্তী প্রশ্ন" else "সম্পূর্ণ শরঈ প্রতিবেদন দেখুন",
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = if (currentQuestionIndex < scenario.diagnosticQuestions.size - 1) Icons.AutoMirrored.Filled.ArrowForward else Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
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
 * Stage 3: Comprehensive Scholarly Report & Verified Sources
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

    val tabTitles = listOf(
        "সারসংক্ষেপ ও সতর্কতা",
        "কুরআন ও হাদীস দলিল",
        "ফিকহি মূলনীতি ও মাযহাব",
        "হালাল বিকল্পসমূহ"
    )

    Column(modifier = Modifier.fillMaxSize()) {
        // Status Banner
        Surface(
            color = Color(report.riskLevel.primaryColorHex).copy(alpha = 0.12f),
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
                        color = Color(report.riskLevel.primaryColorHex)
                    ) {
                        Text(
                            text = report.riskLevel.labelBn,
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
                    text = report.riskLevel.titleBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(report.riskLevel.primaryColorHex),
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = report.riskLevel.subtitleBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // Scrollable Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
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
            when (selectedTab) {
                0 -> {
                    // TAB 0: Executive Breakdown & Red Flags
                    item {
                        ExecutiveSummaryCard(report = report)
                    }

                    if (report.redFlagsIdentified.isNotEmpty()) {
                        item {
                            RedFlagsCard(redFlags = report.redFlagsIdentified)
                        }
                    }

                    if (report.criticalCheckpoints.isNotEmpty()) {
                        item {
                            CriticalCheckpointsCard(checkpoints = report.criticalCheckpoints)
                        }
                    }

                    item {
                        RecommendedNextStepsCard(steps = report.recommendedNextSteps)
                    }

                    item {
                        ScholarlyDisclaimerCard()
                    }
                }

                1 -> {
                    // TAB 1: Quran & Hadith Proofs
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

                2 -> {
                    // TAB 2: Fiqh Maxims & Scholarly Positions
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

                3 -> {
                    // TAB 3: Halal Alternatives
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
                imageVector = Icons.Default.MenuBook,
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
private fun ExecutiveSummaryCard(report: AskBeforeYouActReport) {
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
                    text = "বিশ্লেষণ সারাংশ (Executive Synthesis):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = report.executiveSummaryBn,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "আপনার নির্বাচিত প্রধান উপাদানসমূহ:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    report.selectedOptionLabels.forEach { (question, answer) ->
                        Text(
                            text = "• $question: $answer",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RedFlagsCard(redFlags: List<String>) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDC2626).copy(alpha = 0.08f)),
        border = BorderStroke(1.2.dp, Color(0xFFDC2626).copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null,
                    tint = Color(0xFFDC2626),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "চিহ্নিত চরম ঝুঁকি / নিষিদ্ধ শর্ত (Red Flags):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDC2626),
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            redFlags.forEach { flag ->
                Row(
                    modifier = Modifier.padding(vertical = 4.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(text = "⚠️", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = flag,
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
private fun CriticalCheckpointsCard(checkpoints: List<String>) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD97706).copy(alpha = 0.08f)),
        border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = Color(0xFFD97706),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সতর্কতামূলক শর্ত ও যাচাই চেকলিস্ট:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD97706),
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            checkpoints.forEach { cp ->
                Text(
                    text = "🔍 $cp",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
private fun RecommendedNextStepsCard(steps: List<String>) {
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
                    text = "পরবর্তী করণীয় পদক্ষেপ (Next Steps):",
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
    val context = LocalContext.current

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
                text = "এই টুলটি কোনো ব্যক্তিগত আইনি ফতোয়া নয়; এটি কুরআন, সুন্নাহ ও নির্ভরযোগ্য ফিকহ একাডেমিগুলোর নীতিমালার আলোকে ব্যবহারকারীকে সঠিক প্রশ্ন করতে এবং সুপ্ত হারাম/ঝুঁকিপূর্ণ ধারা চিহ্নিত করতে সাহায্য করার একটি শিক্ষামূলক মাধ্যম। নির্দিষ্ট ও জটিল চুক্তিপত্র চূড়ান্ত করার পূর্বে বিশ্বস্ত যোগ্য মুফতি বা শরীয়াহ কনসালট্যান্টের সাথে ব্যক্তিগত নথিপত্র প্রদর্শনপূর্বক পরামর্শ নেওয়া বাঞ্ছনীয়।",
                style = MaterialTheme.typography.labelSmall.copy(lineHeight = 17.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
    }
}
