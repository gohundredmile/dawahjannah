package com.example.ui.screens.sub

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.MainViewModel
import com.example.util.ArabicAyatSanitizerAndSolver

@Composable
fun AyatDetectorAndSolverScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    var inputText by remember { mutableStateOf("") }
    var solvedOutput by remember { mutableStateOf("") }
    var fontSizeSp by remember { mutableFloatStateOf(24f) }

    // Run detection reactively on input
    val detectionResult by remember(inputText) {
        derivedStateOf {
            ArabicAyatSanitizerAndSolver.detectAndSolve(inputText)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IslamicGold.copy(alpha = 0.18f)
                        ) {
                            Text(
                                text = "কুরআন ও হাদিস টেক্সট ল্যাব",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.FindReplace,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "আয়াত ও হাদিস শুদ্ধিকরণ ও পরীক্ষণ",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "ভাঙ্গা আরবী শব্দ, অনাকাঙ্ক্ষিত বাংলা বা ইংরেজি বর্ণ, ভুল স্পেস ও নন-স্ট্যান্ডার্ড হরকত স্বয়ংক্রিয়ভাবে সনাক্ত করে বিশুদ্ধ কুরআনুল কারীমের মূল নুসখা ও আমীরী (Amiri) আরবী ফন্টে এক ক্লিকে সমাধান করুন।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 22.sp,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Quick Preset Buttons Row
        item {
            Column {
                Text(
                    text = "পরীক্ষা করার জন্য নমুনা নির্বাচন করুন:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ArabicAyatSanitizerAndSolver.sampleBrokenInputs.forEach { sample ->
                        FilledTonalButton(
                            onClick = {
                                inputText = sample.sampleText
                                solvedOutput = ArabicAyatSanitizerAndSolver.solve(sample.sampleText)
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = IslamicGold
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sample.titleBn,
                                style = MaterialTheme.typography.labelMedium,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // Input Text Area
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "আরবী আয়াত / হাদীস টেক্সট দিন:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Paste Button
                            IconButton(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    val clip = clipboard.primaryClip
                                    if (clip != null && clip.itemCount > 0) {
                                        val pasteText = clip.getItemAt(0).text?.toString() ?: ""
                                        if (pasteText.isNotBlank()) {
                                            inputText = pasteText
                                            solvedOutput = ArabicAyatSanitizerAndSolver.solve(pasteText)
                                            Toast.makeText(context, "ক্লিপবোর্ড থেকে পেস্ট করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentPaste,
                                    contentDescription = "Paste",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            // Clear Button
                            if (inputText.isNotBlank()) {
                                IconButton(
                                    onClick = {
                                        inputText = ""
                                        solvedOutput = ""
                                    },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = {
                            inputText = it
                            if (it.isBlank()) {
                                solvedOutput = ""
                            }
                        },
                        placeholder = {
                            Text(
                                text = "এখানে আপনার যে কোনো আরবী আয়াত, দোয়া বা হাদিসের টেক্সট পেস্ট করুন...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.outline,
                                fontFamily = banglaFont
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Primary Action Button
                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                solvedOutput = detectionResult.solvedText
                                Toast.makeText(context, "টেক্সট সম্পূর্ণ বিশ্লেষণ ও সমাধান সম্পন্ন!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "অনুগ্রহ করে টেক্সট ইনপুট দিন", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FindReplace,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "বিশ্লেষণ ও এক ক্লিকে সমাধান করুন",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        // Live Diagnostic Detector Card
        if (inputText.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (detectionResult.hasIssues) {
                            MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                        } else {
                            Color(0xFF059669).copy(alpha = 0.12f)
                        }
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (detectionResult.hasIssues) MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                        else Color(0xFF059669).copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = if (detectionResult.hasIssues) Icons.Default.Warning else Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = if (detectionResult.hasIssues) MaterialTheme.colorScheme.error else Color(0xFF059669),
                                modifier = Modifier.size(24.dp)
                            )

                            Text(
                                text = if (detectionResult.hasIssues) {
                                    "${detectionResult.issues.size} টি ত্রুটি বা অসঙ্গতি সনাক্ত হয়েছে"
                                } else {
                                    "আলহামদুলিল্লাহ! কোনো ভাঙ্গা শব্দ বা ত্রুটি নেই"
                                },
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (detectionResult.hasIssues) MaterialTheme.colorScheme.error else Color(0xFF059669),
                                fontFamily = banglaFont
                            )
                        }

                        if (detectionResult.hasIssues) {
                            Spacer(modifier = Modifier.height(10.dp))
                            detectionResult.issues.forEach { issue ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(
                                        text = "•",
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.padding(end = 6.dp)
                                    )
                                    Column {
                                        Text(
                                            text = issue.titleBn,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface,
                                            fontFamily = banglaFont
                                        )
                                        Text(
                                            text = issue.descriptionBn,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            lineHeight = 18.sp,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }

                        // Matched Verse Banner if found
                        detectionResult.matchedReference?.let { matched ->
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = IslamicGold.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.MenuBook,
                                            contentDescription = null,
                                            tint = IslamicGold,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Text(
                                            text = "কুরআন রেফারেন্স মিল পাওয়া গেছে: ${matched.surahOrHadithNameBn} (${matched.verseOrHadithNoBn})",
                                            style = MaterialTheme.typography.labelLarge,
                                            fontWeight = FontWeight.Bold,
                                            color = IslamicGold,
                                            fontFamily = banglaFont
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = matched.fojilotBn,
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

        // Solved Output Card (Beautiful Arabic Typography)
        val displaySolvedText = if (solvedOutput.isNotBlank()) solvedOutput else detectionResult.solvedText
        if (displaySolvedText.isNotBlank()) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.55f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Header with Font Resizing Tools
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFF059669).copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "বিশুদ্ধ সমাধান",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "আমীরী আরবী ফন্ট (Amiri)",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }

                            // Font size controls
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = { if (fontSizeSp > 18f) fontSizeSp -= 2f },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("A-", style = MaterialTheme.typography.labelSmall)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${fontSizeSp.toInt()}sp",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                OutlinedButton(
                                    onClick = { if (fontSizeSp < 36f) fontSizeSp += 2f },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                    modifier = Modifier.height(30.dp)
                                ) {
                                    Text("A+", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Classical Ornate Arabic Display Surface
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = displaySolvedText,
                                fontFamily = arabicFont,
                                fontSize = fontSizeSp.sp,
                                lineHeight = (fontSizeSp * 1.8f).sp,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Right,
                                modifier = Modifier.padding(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Actions Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Copy Button
                            Button(
                                onClick = {
                                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    clipboard.setPrimaryClip(ClipData.newPlainText("Solved Quranic Verse", displaySolvedText))
                                    Toast.makeText(context, "বিশুদ্ধ আরবী টেক্সট কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("কপি করুন", fontFamily = banglaFont)
                            }

                            // Share Button
                            FilledTonalButton(
                                onClick = {
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, displaySolvedText)
                                        type = "text/plain"
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "আরবী আয়াত শেয়ার করুন"))
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Share,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("শেয়ার করুন", fontFamily = banglaFont)
                            }

                            // Quran.com Online Verification
                            IconButton(
                                onClick = {
                                    val queryUri = Uri.parse("https://quran.com/search?q=${Uri.encode(displaySolvedText.take(50))}")
                                    val browserIntent = Intent(Intent.ACTION_VIEW, queryUri)
                                    try {
                                        context.startActivity(browserIntent)
                                    } catch (e: Exception) {
                                        Toast.makeText(context, "ব্রাউজার খোলা সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier.size(42.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.OpenInBrowser,
                                    contentDescription = "Verify in Quran.com",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Educational Diagnostic Guide Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.16f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "আরবী আয়াত ও শব্দের বিকৃতি কেন ঘটে?",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    GuideStepItem(
                        serial = "১",
                        title = "অনাকাঙ্ক্ষিত বাংলা বর্ণের অনুপ্রবেশ",
                        description = "ওয়েবসাইট বা মেসেঞ্জার থেকে কপি করার সময় বাংলা হরফ (যেমন: ন, র, ল) বা বাংলা সংখ্যা (৮, ৫) আরবী শব্দের ভেতরে ঢুকে যায়, যা মোবাইল ডিসপ্লেতে পুরো শব্দটিকে ভেঙে দেয়।"
                    )
                    GuideStepItem(
                        serial = "২",
                        title = "আরবী অব্যয় ও শব্দের মাঝে ভুল স্পেস",
                        description = "আরবীতে 'ওয়া' (وَ), 'ফা' (فَ), 'বি' (بِ) আলাদা কোনো শব্দ নয়; এগুলো শব্দের সাথেই যুক্ত থাকে। মাঝে স্পেস দিলে যেমন 'وَا عْفُ' তা ব্যাকরণগত ও ভিজ্যুয়ালি ভেঙে যায়।"
                    )
                    GuideStepItem(
                        serial = "৩",
                        title = "উর্দু ও ফারসি ইউনিকোড বর্ণ ব্যবহার",
                        description = "কুরআনের স্ট্যান্ডার্ড নাসেখ অক্ষরের বদলে উর্দু গোল হা (ہ) বা ফারসি ইয়া (ی) ব্যবহার করলে কিছু ফন্টে যুক্তাক্ষর গঠিত হয় না। এই টুলটি স্বয়ংক্রিয়ভাবে তা শুদ্ধ করে।"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun GuideStepItem(
    serial: String,
    title: String,
    description: String
) {
    val banglaFont = LocalBanglaFontFamily.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
            modifier = Modifier.size(24.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = serial,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
            }
        }
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                fontFamily = banglaFont
            )
        }
    }
}
