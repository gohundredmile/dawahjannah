package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.IslamicContextVerifyCatalog
import com.example.data.model.ClaimVerificationVerdict
import com.example.data.model.ConfidenceLevel
import com.example.data.model.HadithReferenceItem
import com.example.data.model.IslamicContextVerifyReport
import com.example.data.model.MisinformationWarning
import com.example.data.model.QuranReferenceItem
import com.example.data.model.ClaimScholarlyOpinion
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.IslamicContextVerifyAiService
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IslamicContextVerifyScreen(
    onNavigateBack: () -> Unit,
    initialClaim: String? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current
    val aiService = remember { IslamicContextVerifyAiService(context) }

    var claimInputText by remember { mutableStateOf(initialClaim ?: "") }
    var selectedPlatformTag by remember { mutableStateOf("WhatsApp বার্তা") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var ocrInProgress by remember { mutableStateOf(false) }
    var currentReport by remember { mutableStateOf<IslamicContextVerifyReport?>(null) }
    var showPrinciplesDialog by remember { mutableStateOf(false) }
    var showSavedHistorySheet by remember { mutableStateOf(false) }
    var savedReportsList by remember { mutableStateOf(emptyList<IslamicContextVerifyReport>()) }

    // Refresh saved reports
    fun refreshSavedList() {
        savedReportsList = aiService.getSavedReports()
    }

    LaunchedEffect(Unit) {
        refreshSavedList()
        // If initial claim passed, analyze immediately
        if (!initialClaim.isNullOrBlank()) {
            isAnalyzing = true
            scope.launch {
                currentReport = aiService.verifyClaim(initialClaim, selectedPlatformTag)
                isAnalyzing = false
            }
        }
    }

    // Photo picker launcher for screenshots or images
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            ocrInProgress = true
            scope.launch {
                try {
                    val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
                    } else {
                        @Suppress("DEPRECATION")
                        MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    }
                    val recognized = aiService.extractTextFromImage(bitmap)
                    if (recognized.isNotBlank()) {
                        claimInputText = recognized.trim()
                        Toast.makeText(context, "স্ক্রিনশট থেকে লেখা শনাক্ত করা হয়েছে", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "ছবিতে কোনো স্পষ্ট লেখা পাওয়া যায়নি", Toast.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "ছবি লোড করতে সমস্যা হয়েছে: ${e.message}", Toast.LENGTH_SHORT).show()
                } finally {
                    ocrInProgress = false
                }
            }
        }
    }

    val platformOptions = listOf(
        "WhatsApp বার্তা",
        "ফেসবুক পোস্ট",
        "ইউটিউব / রিল",
        "ওয়েবসাইট / আর্টিকেল",
        "প্রচলিত বক্তব্য"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Islamic Context & Verify",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF047857).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "AI ফ্ল্যাগশিপ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Text(
                            text = "ইসলামিক কনটেক্সট ও প্রামাণ্য যাচাইকরণ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {
                        refreshSavedList()
                        showSavedHistorySheet = true
                    }) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "Saved History",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { showPrinciplesDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Principles & Methodology",
                            tint = Color(0xFF047857)
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            // Hero Spotlight Card
            HeroVerificationBanner(banglaFont = banglaFont)

            Spacer(modifier = Modifier.height(12.dp))

            // Main Input Container
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header inside card
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("🔍", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "দাবি বা বার্তাটি ইনপুট করুন",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }

                        // Fast Paste & Clear buttons
                        Row {
                            TextButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = clipboard.primaryClip
                                    if (clip != null && clip.itemCount > 0) {
                                        val text = clip.getItemAt(0).text?.toString() ?: ""
                                        if (text.isNotBlank()) {
                                            claimInputText = text
                                            Toast.makeText(context, "ক্লিপবোর্ড থেকে পেস্ট করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Icon(Icons.Default.ContentPaste, contentDescription = "Paste", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("পেস্ট", fontSize = 12.sp, fontFamily = banglaFont)
                            }

                            if (claimInputText.isNotBlank()) {
                                IconButton(
                                    onClick = { claimInputText = "" },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", modifier = Modifier.size(18.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Source Platform Tags
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        platformOptions.forEach { tag ->
                            FilterChip(
                                selected = selectedPlatformTag == tag,
                                onClick = { selectedPlatformTag = tag },
                                label = { Text(tag, fontSize = 12.sp, fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF047857).copy(alpha = 0.15f),
                                    selectedLabelColor = Color(0xFF047857)
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Outlined Text Field
                    OutlinedTextField(
                        value = claimInputText,
                        onValueChange = { claimInputText = it },
                        placeholder = {
                            Text(
                                text = "WhatsApp মেসেজ, ফেসবুক পোস্ট, বা 'ইসলামে বলা হয়েছে...' এমন যেকোনো দাবি এখানে পেস্ট করুন...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                fontFamily = banglaFont
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF047857),
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Media upload & OCR trigger row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    androidx.activity.result.PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.4f)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF047857))
                        ) {
                            if (ocrInProgress) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp,
                                    color = Color(0xFF047857)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("ছবি স্ক্যান হচ্ছে...", fontSize = 13.sp, fontFamily = banglaFont)
                            } else {
                                Icon(Icons.Default.Image, contentDescription = "Upload screenshot", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("স্ক্রিনশট / ছবি স্ক্যান", fontSize = 13.sp, fontFamily = banglaFont)
                            }
                        }

                        // Primary Action Button: 🔍 Verify & Explain
                        Button(
                            onClick = {
                                if (claimInputText.isBlank()) {
                                    Toast.makeText(context, "দয়া করে একটি দাবি বা বার্তা লিখুন বা পেস্ট করুন", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }
                                isAnalyzing = true
                                scope.launch {
                                    currentReport = aiService.verifyClaim(claimInputText, selectedPlatformTag)
                                    isAnalyzing = false
                                }
                            },
                            enabled = !isAnalyzing && claimInputText.isNotBlank(),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF047857)
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(18.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("যাচাই হচ্ছে...", fontSize = 14.sp, fontFamily = banglaFont, color = Color.White)
                            } else {
                                Icon(Icons.Default.Search, contentDescription = "Verify", modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "🔍 Verify & Explain",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Quick Preset Viral Claims Section
            Text(
                text = "জনপ্রিয় ভাইরাল দাবি ও বার্তা (এক ট্যাপে দেখুন):",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 18.dp),
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IslamicContextVerifyCatalog.presetClaims.forEach { preset ->
                    PresetClaimChip(
                        preset = preset,
                        banglaFont = banglaFont,
                        onClick = {
                            claimInputText = preset.originalClaim
                            selectedPlatformTag = preset.sourcePlatformTag
                            currentReport = preset
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Animated Progress while analyzing
            AnimatedVisibility(visible = isAnalyzing) {
                AnalysisLoadingCard(banglaFont = banglaFont)
            }

            // Report Output Section
            if (currentReport != null && !isAnalyzing) {
                val report = currentReport!!
                VerificationReportContainer(
                    report = report,
                    banglaFont = banglaFont,
                    onSaveToggle = {
                        if (report.isSaved) {
                            aiService.removeSavedReport(report.id)
                            currentReport = report.copy(isSaved = false)
                            Toast.makeText(context, "সংরক্ষণ থেকে অপসারিত", Toast.LENGTH_SHORT).show()
                        } else {
                            aiService.saveReport(report)
                            currentReport = report.copy(isSaved = true)
                            Toast.makeText(context, "রিপোর্ট সফলভাবে সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                        }
                        refreshSavedList()
                    },
                    onShareReport = {
                        val shareText = buildShareReportText(report)
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, "Islamic Context & Verify Report")
                            putExtra(Intent.EXTRA_TEXT, shareText)
                        }
                        context.startActivity(Intent.createChooser(intent, "রিপোর্ট শেয়ার করুন"))
                    },
                    onCopyReport = {
                        val shareText = buildShareReportText(report)
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Islamic Context & Verify Report", shareText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "সম্পূর্ণ রিপোর্ট ক্লিপবোর্ডে কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // Principles & Methodology Dialog
    if (showPrinciplesDialog) {
        PrinciplesAndMethodologyDialog(
            banglaFont = banglaFont,
            onDismiss = { showPrinciplesDialog = false }
        )
    }

    // Saved History Bottom Sheet
    if (showSavedHistorySheet) {
        ModalBottomSheet(
            onDismissRequest = { showSavedHistorySheet = false },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            containerColor = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            SavedReportsSheetContent(
                reports = savedReportsList,
                banglaFont = banglaFont,
                onSelectReport = { selected ->
                    claimInputText = selected.originalClaim
                    selectedPlatformTag = selected.sourcePlatformTag
                    currentReport = selected
                    showSavedHistorySheet = false
                },
                onDeleteReport = { toDelete ->
                    aiService.removeSavedReport(toDelete.id)
                    refreshSavedList()
                },
                onClose = { showSavedHistorySheet = false }
            )
        }
    }
}

@Composable
fun HeroVerificationBanner(banglaFont: FontFamily) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.25f)),
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF047857).copy(alpha = 0.12f),
                            IslamicGold.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(18.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "See the claim. Check the evidence. Understand the context.",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "দাবি দেখুন • প্রমাণ যাচাই করুন • প্রেক্ষাপট বুঝুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF047857),
                            fontWeight = FontWeight.Medium,
                            fontFamily = banglaFont
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF047857).copy(alpha = 0.15f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🛡️", fontSize = 22.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "WhatsApp, ফেসবুক বা সোশ্যাল মিডিয়ায় ছড়িয়ে পড়া চেইন মেসেজ ও ধর্মীয় দাবিসমূহকে কোনো অন্ধ হুজুগে না নিয়ে মূল কুরআন, সিহাহ্ সিত্তাহ সহীহ হাদিস ও বিজ্ঞ ফকীহগণের মতামতের আলোকে প্রামাণ্যভাবে যাচাই করুন।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun PresetClaimChip(
    preset: IslamicContextVerifyReport,
    banglaFont: FontFamily,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, preset.verdict.primaryColor.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = preset.verdict.containerColor
                ) {
                    Text(
                        text = "${preset.verdict.iconEmoji} ${preset.verdict.titleBn}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = preset.verdict.primaryColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = banglaFont
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = preset.sourcePlatformTag,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = preset.originalClaim,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "যাচাই ফলাফল দেখুন ➔",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = preset.verdict.primaryColor,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun AnalysisLoadingCard(banglaFont: FontFamily) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .scale(scale)
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF047857).copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🔎", fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "ইসলামিক সোর্স ও প্রেক্ষাপট পর্যালোচনা করা হচ্ছে...",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "কুরআনিক আয়াত, সিহাহ সিত্তাহ সহীহ হাদিস, রাবীগণের নির্ভরযোগ্যতা এবং চার মাযহাবের ফিকহী নীতিমালা মিলিয়ে দেখা হচ্ছে।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VerificationReportContainer(
    report: IslamicContextVerifyReport,
    banglaFont: FontFamily,
    onSaveToggle: () -> Unit,
    onShareReport: () -> Unit,
    onCopyReport: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Verdict & Confidence Master Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = report.verdict.containerColor),
            border = BorderStroke(1.5.dp, report.verdict.primaryColor.copy(alpha = 0.4f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Top row with Verdict Badge & Confidence Level
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = report.verdict.primaryColor
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(report.verdict.iconEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${report.verdict.titleBn} (${report.verdict.titleEn})",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    // Confidence Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = report.confidence.color.copy(alpha = 0.15f),
                        border = BorderStroke(1.dp, report.confidence.color.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = report.confidence.labelBn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = report.confidence.color,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Headline
                Text(
                    text = report.summaryHeadlineBn.ifBlank { report.verdict.descriptionBn },
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF1E293B),
                    fontFamily = banglaFont,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Evidence Summary
                if (report.evidenceSummaryBn.isNotBlank()) {
                    Text(
                        text = report.evidenceSummaryBn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF334155),
                        fontFamily = banglaFont,
                        lineHeight = 22.sp
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = report.verdict.primaryColor.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(10.dp))

                // Quick Action Bar: Copy, Share, Save
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "প্ল্যাটফর্ম: ${report.sourcePlatformTag}",
                        fontSize = 11.sp,
                        color = Color(0xFF475569),
                        fontFamily = banglaFont
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        IconButton(onClick = onCopyReport, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Report", tint = report.verdict.primaryColor)
                        }
                        IconButton(onClick = onShareReport, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Share, contentDescription = "Share Report", tint = report.verdict.primaryColor)
                        }
                        IconButton(onClick = onSaveToggle, modifier = Modifier.size(36.dp)) {
                            Icon(
                                imageVector = if (report.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Save Report",
                                tint = report.verdict.primaryColor
                            )
                        }
                    }
                }
            }
        }

        // 2. Individual Claims Breakdown
        if (report.individualClaims.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeaderWithEmoji(emoji = "📋", title = "বার্তার সুনির্দিষ্ট দাবিসমূহ (The Claims)", banglaFont = banglaFont)
                    Spacer(modifier = Modifier.height(10.dp))
                    report.individualClaims.forEachIndexed { index, claim ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("${index + 1}. ", fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                            Text(
                                text = claim,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont,
                                lineHeight = 20.sp
                            )
                        }
                    }
                }
            }
        }

        // 3. Quranic Evidence
        if (report.quranReferences.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFF0F766E).copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeaderWithEmoji(emoji = "📖", title = "পবিত্র কুরআনের দলিল ও রেফারেন্স", banglaFont = banglaFont)
                    Spacer(modifier = Modifier.height(12.dp))

                    report.quranReferences.forEach { qRef ->
                        QuranReferenceItemCard(qRef = qRef, banglaFont = banglaFont)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // 4. Hadith Evidence
        if (report.hadithReferences.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFF4338CA).copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeaderWithEmoji(emoji = "📚", title = "হাদীস রেফারেন্স ও তাহকীক (Hadith Reference)", banglaFont = banglaFont)
                    Spacer(modifier = Modifier.height(12.dp))

                    report.hadithReferences.forEach { hRef ->
                        HadithReferenceItemCard(hRef = hRef, banglaFont = banglaFont)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }

        // 5. Context & Background (Historical, Linguistic, Scope)
        if (report.historicalContextBn.isNotBlank() || report.linguisticContextBn.isNotBlank() || report.audienceAndScopeBn.isNotBlank()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFFB45309).copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeaderWithEmoji(emoji = "⏳", title = "প্রেক্ষাপট বিশ্লেষণ (Context & Background)", banglaFont = banglaFont)
                    Spacer(modifier = Modifier.height(10.dp))

                    if (report.historicalContextBn.isNotBlank()) {
                        SubContextBlock(
                            label = "ঐতিহাসিক পটভূমি ও শান-এ-নুযুল:",
                            content = report.historicalContextBn,
                            banglaFont = banglaFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (report.linguisticContextBn.isNotBlank()) {
                        SubContextBlock(
                            label = "ভাষাগত ও পরিভাষাগত দ্যোতনা (Linguistic Context):",
                            content = report.linguisticContextBn,
                            banglaFont = banglaFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (report.audienceAndScopeBn.isNotBlank()) {
                        SubContextBlock(
                            label = "সম্বোধিত ব্যক্তি ও বিধির ক্ষেত্র (General vs Situation-Specific):",
                            content = report.audienceAndScopeBn,
                            banglaFont = banglaFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (report.omittedSurroundingsBn.isNotBlank()) {
                        SubContextBlock(
                            label = "ভাইরাল পোস্ট থেকে বাদ পড়া পূর্বাপর তথ্য:",
                            content = report.omittedSurroundingsBn,
                            banglaFont = banglaFont,
                            isWarningStyle = true
                        )
                    }
                }
            }
        }

        // 6. Scholarly Interpretations
        if (report.scholarlyInterpretations.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFF6D28D9).copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeaderWithEmoji(emoji = "⚖️", title = "প্রখ্যাত আলেম ও ফকীহগণের অভিমত (Scholarly Views)", banglaFont = banglaFont)
                    Spacer(modifier = Modifier.height(12.dp))

                    report.scholarlyInterpretations.forEach { scholarItem ->
                        ScholarlyOpinionCard(item = scholarItem, banglaFont = banglaFont)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }

        // 7. Potential Misinformation & Warning Flags
        if (report.potentialMisinformation.isNotEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                border = BorderStroke(1.dp, Color(0xFFE11D48).copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeaderWithEmoji(emoji = "🚨", title = "ভ্রান্তির লক্ষণ ও সতর্কতা (Potential Misinformation)", banglaFont = banglaFont, titleColor = Color(0xFFBE123C))
                    Spacer(modifier = Modifier.height(10.dp))

                    report.potentialMisinformation.forEach { warn ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text("⚠️ ", fontSize = 14.sp)
                            Column {
                                Text(
                                    text = warn.warningType,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = Color(0xFF9F1239),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = warn.warningDetail,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF881337),
                                    lineHeight = 18.sp,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        }

        // 8. What Is Actually Established (Crown Summary)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
            border = BorderStroke(1.5.dp, Color(0xFF16A34A).copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                SectionHeaderWithEmoji(emoji = "✨", title = "প্রমাণ সাপেক্ষে চূড়ান্ত সিদ্ধান্ত (What Is Actually Established)", banglaFont = banglaFont, titleColor = Color(0xFF15803D))
                Spacer(modifier = Modifier.height(12.dp))

                if (report.whatIsActuallyEstablished.explicitTextualFact.isNotBlank()) {
                    EstablishedSubCard(
                        tag = "মূল দলিলে স্পষ্টভাবে যা আছে:",
                        text = report.whatIsActuallyEstablished.explicitTextualFact,
                        badgeColor = Color(0xFF15803D),
                        banglaFont = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (report.whatIsActuallyEstablished.scholarlyInference.isNotBlank()) {
                    EstablishedSubCard(
                        tag = "আলেমগণের ইজতিহাদ ও সিদ্ধান্ত:",
                        text = report.whatIsActuallyEstablished.scholarlyInference,
                        badgeColor = Color(0xFF2563EB),
                        banglaFont = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (report.whatIsActuallyEstablished.uncertainOrUnverified.isNotBlank()) {
                    EstablishedSubCard(
                        tag = "যা প্রমাণিত নয় বা অপ্রাসঙ্গিক:",
                        text = report.whatIsActuallyEstablished.uncertainOrUnverified,
                        badgeColor = Color(0xFFDC2626),
                        banglaFont = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (report.actionableConclusionBn.isNotBlank()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFF16A34A).copy(alpha = 0.25f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "💡 ব্যবহারিক সুন্নাহসম্মত উপদেশ:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFF15803D),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = report.actionableConclusionBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF1E293B),
                                lineHeight = 19.sp,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuranReferenceItemCard(qRef: QuranReferenceItem, banglaFont: FontFamily) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${qRef.surahName} (${qRef.surahNumber}:${qRef.ayahNumber})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF0F766E),
                    fontFamily = banglaFont
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (qRef.isDirectEvidence) Color(0xFF059669).copy(alpha = 0.15f) else Color(0xFF2563EB).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (qRef.isDirectEvidence) "সরাসরি বিধান" else "তাফসীরভিত্তিক ইজতিহাদ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (qRef.isDirectEvidence) Color(0xFF059669) else Color(0xFF2563EB),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = banglaFont
                    )
                }
            }

            if (qRef.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = qRef.arabicText,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (qRef.translationBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "অনুবাদ: \"${qRef.translationBn}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                    fontFamily = banglaFont
                )
            }

            if (qRef.whatVerseActuallyAddresses.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "আয়াতে প্রকৃতপক্ষে যা বলা হয়েছে: ${qRef.whatVerseActuallyAddresses}",
                    fontSize = 12.sp,
                    color = Color(0xFF0F766E),
                    lineHeight = 17.sp,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun HadithReferenceItemCard(hRef: HadithReferenceItem, banglaFont: FontFamily) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${hRef.collection} (${hRef.hadithNumber})",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF4338CA),
                    fontFamily = banglaFont
                )

                // Grading badge
                val gradeColor = when {
                    hRef.authenticityGrade.contains("সহীহ") || hRef.authenticityGrade.contains("Sahih") -> Color(0xFF059669)
                    hRef.authenticityGrade.contains("হাসান") || hRef.authenticityGrade.contains("Hasan") -> Color(0xFFD97706)
                    hRef.authenticityGrade.contains("যয়ীফ") || hRef.authenticityGrade.contains("দুর্বল") -> Color(0xFFEA580C)
                    else -> Color(0xFFDC2626)
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = gradeColor.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = hRef.authenticityGrade,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = gradeColor,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontFamily = banglaFont
                    )
                }
            }

            if (hRef.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hRef.arabicText,
                    fontSize = 16.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (hRef.translationBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "অনুবাদ: \"${hRef.translationBn}\"",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp,
                    fontFamily = banglaFont
                )
            }

            if (hRef.textVsInterpretation.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "শব্দরূপ বনাম ব্যাখ্যা: ${hRef.textVsInterpretation}",
                    fontSize = 12.sp,
                    color = Color(0xFF4338CA),
                    lineHeight = 17.sp,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun ScholarlyOpinionCard(item: ClaimScholarlyOpinion, banglaFont: FontFamily) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = "📌 ${item.schoolOrScholar}",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF6D28D9),
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.positionSummary,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp,
                fontFamily = banglaFont
            )
            if (item.textualBasis.isNotBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "দলিল ও উৎস: ${item.textualBasis}",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun SubContextBlock(label: String, content: String, banglaFont: FontFamily, isWarningStyle: Boolean = false) {
    Column {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = if (isWarningStyle) Color(0xFFDC2626) else Color(0xFFB45309),
            fontFamily = banglaFont
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 18.sp,
            fontFamily = banglaFont
        )
    }
}

@Composable
fun EstablishedSubCard(tag: String, text: String, badgeColor: Color, banglaFont: FontFamily) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = badgeColor.copy(alpha = 0.15f),
            modifier = Modifier.padding(top = 2.dp)
        ) {
            Box(modifier = Modifier.size(6.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = tag,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 18.sp,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun SectionHeaderWithEmoji(
    emoji: String,
    title: String,
    banglaFont: FontFamily,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(emoji, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = titleColor,
            fontFamily = banglaFont
        )
    }
}

fun buildShareReportText(report: IslamicContextVerifyReport): String {
    return buildString {
        appendLine("🛡️ Islamic Context & Verify রিপোর্ট")
        appendLine("দাবি: \"${report.originalClaim}\"")
        appendLine("───────────────")
        appendLine("সিদ্ধান্ত: ${report.verdict.iconEmoji} ${report.verdict.titleBn} (${report.verdict.titleEn})")
        appendLine("প্রামাণিকতা: ${report.confidence.labelBn}")
        appendLine()
        appendLine("সারসংক্ষেপ:")
        appendLine(report.summaryHeadlineBn)
        if (report.evidenceSummaryBn.isNotBlank()) {
            appendLine(report.evidenceSummaryBn)
        }
        appendLine()
        if (report.quranReferences.isNotEmpty()) {
            appendLine("📖 কুরআন রেফারেন্স:")
            report.quranReferences.forEach {
                appendLine("• ${it.surahName} (${it.surahNumber}:${it.ayahNumber}): ${it.translationBn}")
            }
            appendLine()
        }
        if (report.hadithReferences.isNotEmpty()) {
            appendLine("📚 হাদীস রেফারেন্স:")
            report.hadithReferences.forEach {
                appendLine("• ${it.collection} (${it.hadithNumber}) [মান: ${it.authenticityGrade}]: ${it.translationBn}")
            }
            appendLine()
        }
        if (report.whatIsActuallyEstablished.explicitTextualFact.isNotBlank()) {
            appendLine("✨ মূল দলিলে যা প্রমাণিত:")
            appendLine(report.whatIsActuallyEstablished.explicitTextualFact)
            appendLine()
        }
        if (report.actionableConclusionBn.isNotBlank()) {
            appendLine("💡 পরামর্শ:")
            appendLine(report.actionableConclusionBn)
            appendLine()
        }
        appendLine("যাচাইকৃত অ্যাপ: দা'ওয়াহ টু জান্নাহ্ (Dawah to Jannah)")
    }
}

@Composable
fun PrinciplesAndMethodologyDialog(banglaFont: FontFamily, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🛡️", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ইসলামিক যাচাইকরণের মূলনীতি",
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            }
        },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Text(
                    text = "১. বাইনারি (সত্য/মিথ্যা) দৃষ্টিভঙ্গি পরিহার:\nইসলামের বিধান অনেক ক্ষেত্রে কালো বা সাদার মতো চরম নয়। অনেক বক্তব্য আংশিক সঠিক কিন্তু প্রেক্ষাপট ছাড়া বিপজ্জনক। তাই আমরা 'প্রেক্ষাপট প্রয়োজন' বা 'আলেমগণের মতভেদ'-এর মতো ভারসাম্যপূর্ণ মানদণ্ড ব্যবহার করি।",
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 19.sp,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "২. আসমাউর রিজাল ও মুসতালাহুল হাদিস:\nহাদিসের নামে যা প্রচলিত তা-ই নবীজীর বাণী নয়। সনদের ধারাবাহিকতা, বর্ণনাকারীদের স্মৃতিশক্তি ও সততা যাচাই (জারহ ও তা'দীল) ছাড়া কোনো কথা নিশ্চিত বলে মানা নিষেধ।",
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 19.sp,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "৩. পাঠ্য বনাম মানবীয় ইজতিহাদ:\nকুরআন ও হাদিসের অকাট্য স্পষ্ট শব্দের সাথে পরবর্তী আলেমগণের গবেষণামূলক অনুমানকে গুলিয়ে ফেলা যাবে না।",
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 19.sp,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "৪. তথ্য পাওয়ার পর সতর্কতার ঐশী নির্দেশ:\n\"হে মুমিনগণ! কোনো পাপাচারী যদি কোনো সংবাদ নিয়ে আসে, তবে তা যাচাই করে নাও...\" (সূরা হুজুরাত: ৬)।",
                    style = MaterialTheme.typography.bodySmall,
                    lineHeight = 19.sp,
                    fontFamily = banglaFont
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
            ) {
                Text("বুঝেছি", fontFamily = banglaFont)
            }
        }
    )
}

@Composable
fun SavedReportsSheetContent(
    reports: List<IslamicContextVerifyReport>,
    banglaFont: FontFamily,
    onSelectReport: (IslamicContextVerifyReport) -> Unit,
    onDeleteReport: (IslamicContextVerifyReport) -> Unit,
    onClose: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🔖", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সংরক্ষিত যাচাই রিপোর্টসমূহ",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (reports.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "কোনো সংরক্ষিত রিপোর্ট নেই। যেকোনো দাবি যাচাই করে বুকমার্ক আইকনে ট্যাপ করে সংরক্ষণ করুন।",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    fontFamily = banglaFont
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(reports, key = { it.id }) { item ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelectReport(item) },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(item.verdict.iconEmoji, fontSize = 14.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = item.verdict.titleBn,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = item.verdict.primaryColor,
                                        fontFamily = banglaFont
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.originalClaim,
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis,
                                    fontFamily = banglaFont
                                )
                            }
                            IconButton(onClick = { onDeleteReport(item) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
