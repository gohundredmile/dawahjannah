package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.data.model.PersonalDuaBlueprint
import com.example.data.model.PropheticDuaItem
import com.example.data.model.QuranicDuaItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.PersonalDuaBuilderService
import kotlinx.coroutines.launch

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

    // Search and blueprint state
    var userPromptInput by remember { mutableStateOf("My father is sick and I am worried.") }
    var activePrompt by remember { mutableStateOf("My father is sick and I am worried.") }
    var isBuilding by remember { mutableStateOf(false) }
    var currentBlueprint by remember { mutableStateOf<PersonalDuaBlueprint?>(null) }

    // UI Controls
    var arabicFontSize by remember { mutableIntStateOf(24) }
    var selectedFilterIndex by remember { mutableIntStateOf(0) } // 0: All, 1: Quran, 2: Hadith, 3: Personal, 4: Adab & Timing
    var showHelpDialog by remember { mutableStateOf(false) }

    // Practice counter dialog state
    var practiceDuaTitle by remember { mutableStateOf<String?>(null) }
    var practiceDuaArabic by remember { mutableStateOf<String?>(null) }
    var practiceDuaTarget by remember { mutableIntStateOf(7) }
    var practiceCurrentCount by remember { mutableIntStateOf(0) }

    // Initial build
    LaunchedEffect(Unit) {
        isBuilding = true
        currentBlueprint = service.buildDuaBlueprint(activePrompt)
        isBuilding = false
    }

    fun executeBuild(query: String) {
        if (query.isBlank()) return
        keyboardController?.hide()
        activePrompt = query
        isBuilding = true
        coroutineScope.launch {
            currentBlueprint = service.buildDuaBlueprint(query)
            isBuilding = false
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
                                text = "Personal Dua Builder",
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
                                    text = "সিগনেচার টুল",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Text(
                            text = "পরিস্থিতি অনুযায়ী কুরআনী ও সুন্নাহসম্মত দো'আ আর্কিটেক্ট",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    // Font Size Cycle Button
                    IconButton(onClick = {
                        arabicFontSize = if (arabicFontSize >= 30) 20 else arabicFontSize + 2
                    }) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "ফন্ট সাইজ পরিবর্তন",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Info Dialog Trigger
                    IconButton(onClick = { showHelpDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "টুল সম্পর্কে",
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
            contentPadding = PaddingValues(top = 12.dp, bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // PROMPT INPUT & SEARCH CARD
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "আপনার মনের অবস্থা বা পরিস্থিতি লিখুন:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "বাংলা বা ইংরেজিতে আপনার জীবনের সংকট বা প্রয়োজন লিখলে সঠিক রেফারেন্সসহ দু'আ সাজানো হবে।",
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
                                    text = "যেমন: My father is sick and I am worried / বাবা অসুস্থ ও দুশ্চিন্তা...",
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
                            keyboardActions = KeyboardActions(onSearch = { executeBuild(userPromptInput) })
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Quick Action Buttons
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { executeBuild(userPromptInput) },
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
                                    Text("তৈরি হচ্ছে...", fontFamily = banglaFont)
                                } else {
                                    Icon(Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("দো'আ আর্কিটেক্ট প্রস্তুত করুন", fontWeight = FontWeight.Bold, fontFamily = banglaFont)
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
                        text = "জনপ্রিয় পরিস্থিতি নির্বাচন করুন:",
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
                                    executeBuild(preset.samplePrompt)
                                },
                                label = {
                                    Text(
                                        text = preset.titleBn,
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

            // FILTER TABS (All / Quranic / Prophetic / General / Timings)
            item {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    TabRow(
                        selectedTabIndex = selectedFilterIndex,
                        containerColor = Color.Transparent,
                        divider = {},
                        indicator = { tabPositions ->
                            TabRowDefaults.SecondaryIndicator(
                                Modifier.tabIndicatorOffset(tabPositions[selectedFilterIndex]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    ) {
                        listOf("সকল অংশ", "কুরআনী দু'আ", "নববী দু'আ", "ব্যক্তিগত আরজি", "সময় ও আদব").forEachIndexed { index, title ->
                            Tab(
                                selected = selectedFilterIndex == index,
                                onClick = { selectedFilterIndex = index },
                                text = {
                                    Text(
                                        text = title,
                                        fontSize = 11.sp,
                                        fontWeight = if (selectedFilterIndex == index) FontWeight.Bold else FontWeight.Normal,
                                        fontFamily = banglaFont,
                                        color = if (selectedFilterIndex == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // BLUEPRINT CONTENT
            val blueprint = currentBlueprint
            if (blueprint != null) {
                // 1. HERO BLUEPRINT HEADER & SPIRITUAL COMFORT (Show when index == 0 or any)
                if (selectedFilterIndex == 0) {
                    item {
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.7f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(
                                                IslamicGold.copy(alpha = 0.12f),
                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                            )
                                        )
                                    )
                                    .padding(16.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = IslamicGold.copy(alpha = 0.25f),
                                            border = BorderStroke(0.5.dp, IslamicGold)
                                        ) {
                                            Text(
                                                text = "পরিস্থিতি বিশ্লেষণ ও তাওয়াক্কুল",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = banglaFont,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            IconButton(
                                                onClick = {
                                                    copyBlueprintToClipboard(context, blueprint)
                                                    Toast.makeText(context, "সম্পূর্ণ দো'আ প্ল্যান কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                                },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.ContentCopy,
                                                    contentDescription = "কপি করুন",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }

                                            IconButton(
                                                onClick = { shareBlueprint(context, blueprint) },
                                                modifier = Modifier.size(32.dp)
                                            ) {
                                                Icon(
                                                    Icons.Default.Share,
                                                    contentDescription = "শেয়ার করুন",
                                                    tint = MaterialTheme.colorScheme.primary,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }

                                    Text(
                                        text = blueprint.scenarioTitleBn,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    Icons.Default.Spa,
                                                    contentDescription = null,
                                                    tint = Color(0xFF059669),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "হৃদয়ের সান্ত্বনা ও ইসলামি দৃষ্টিভঙ্গি",
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF059669),
                                                    fontFamily = banglaFont
                                                )
                                            }
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = blueprint.spiritualComfortBn,
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

                // 2. QURANIC DUAS SECTION
                if (selectedFilterIndex == 0 || selectedFilterIndex == 1) {
                    item {
                        SectionHeader(
                            icon = Icons.Default.MenuBook,
                            title = "কুরআনী দু'আ (Quranic Supplications)",
                            badge = "${blueprint.quranicDuas.size}টি আয়াত",
                            badgeColor = Color(0xFF059669),
                            banglaFont = banglaFont
                        )
                    }

                    items(blueprint.quranicDuas, key = { it.id }) { quranDua ->
                        QuranicDuaCard(
                            item = quranDua,
                            arabicFontSize = arabicFontSize,
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

                // 3. PROPHETIC DUAS SECTION
                if (selectedFilterIndex == 0 || selectedFilterIndex == 2) {
                    item {
                        SectionHeader(
                            icon = Icons.Default.Mosque,
                            title = "মাসনূন নববী দু'আ (Prophetic Supplications)",
                            badge = "${blueprint.propheticDuas.size}টি সহীহ হাদীস",
                            badgeColor = Color(0xFF4F46E5),
                            banglaFont = banglaFont
                        )
                    }

                    items(blueprint.propheticDuas, key = { it.id }) { propheticDua ->
                        PropheticDuaCard(
                            item = propheticDua,
                            arabicFontSize = arabicFontSize,
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

                // 4. GENERAL PERMISSIBLE SUPPLICATIONS SECTION
                if (selectedFilterIndex == 0 || selectedFilterIndex == 3) {
                    item {
                        SectionHeader(
                            icon = Icons.Default.Favorite,
                            title = "সাধারণ বৈধ মিনতি (General Permissible Supplication)",
                            badge = "মাতৃভাষার আরজি",
                            badgeColor = Color(0xFFD97706),
                            banglaFont = banglaFont
                        )
                    }

                    items(blueprint.generalSupplications, key = { it.id }) { supplication ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.35f)),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = supplication.titleBn,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )

                                    Row {
                                        IconButton(
                                            onClick = {
                                                copyText(context, supplication.heartfeltSupplicationBn, "আরজি কপি করা হয়েছে")
                                            },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.ContentCopy,
                                                contentDescription = "কপি",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                        IconButton(
                                            onClick = {
                                                shareText(context, "${supplication.titleBn}:\n\n${supplication.heartfeltSupplicationBn}\n\n[Dawah to Jannah]")
                                            },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Share,
                                                contentDescription = "শেয়ার",
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(15.dp)
                                            )
                                        }
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFFD97706).copy(alpha = 0.08f),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "“${supplication.heartfeltSupplicationBn}”",
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }

                                if (supplication.invokedNamesOfAllahBn.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "উসীলাকৃত নামসমূহ:",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontFamily = banglaFont
                                        )
                                        supplication.invokedNamesOfAllahBn.forEach { name ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                                border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                            ) {
                                                Text(
                                                    text = name,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontFamily = banglaFont,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                Text(
                                    text = "💡 শরঈ নোট: ${supplication.islamicGuidelineBn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }

                // 5. GOLDEN TIMINGS & ETIQUETTES SECTION
                if (selectedFilterIndex == 0 || selectedFilterIndex == 4) {
                    item {
                        SectionHeader(
                            icon = Icons.Default.Schedule,
                            title = "দো'আ কবুলের সোনালী সময় ও আদব (When & How Traditionally Used)",
                            badge = "সুন্নাতী সময়",
                            badgeColor = Color(0xFF0284C7),
                            banglaFont = banglaFont
                        )
                    }

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
                }

                // 6. SCHOLARLY CLARIFICATIONS (Show in all or Adab)
                if (selectedFilterIndex == 0 || selectedFilterIndex == 4) {
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
                                        Text(
                                            text = "• ঐক্যমত ও সূক্ষ্মতা: ${opinion.consensusOrNuanceBn}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 7. PRACTICAL REMINDERS / ACTION STEPS
                if (selectedFilterIndex == 0) {
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
                                    text = "📌 আমল ও করণীয় চেকলিস্ট:",
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
        }
    }

    // INTERACTIVE TASBIH / PRACTICE COUNTER MODAL
    if (practiceDuaTitle != null && practiceDuaArabic != null) {
        AlertDialog(
            onDismissRequest = { practiceDuaTitle = null },
            title = {
                Column {
                    Text(
                        text = "আমল ও তাসবীহ কাউন্টার",
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
                                fontSize = 22.sp,
                                lineHeight = 36.sp,
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
                                    text = if (practiceCurrentCount >= practiceDuaTarget) "সম্পন্ন!" else "ট্যাপ করুন",
                                    fontSize = 11.sp,
                                    fontFamily = banglaFont,
                                    color = if (practiceCurrentCount >= practiceDuaTarget) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    if (practiceCurrentCount >= practiceDuaTarget) {
                        Text(
                            text = "আলহামদুলিল্লাহ! সুন্নাতী সংখ্যার তিলাওয়াত সম্পন্ন হয়েছে।",
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
                    Text("বন্ধ করুন", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { practiceCurrentCount = 0 }
                ) {
                    Text("রিসেট", fontFamily = banglaFont)
                }
            }
        )
    }

    // HELP DIALOG
    if (showHelpDialog) {
        AlertDialog(
            onDismissRequest = { showHelpDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Personal Dua Builder কী?", fontFamily = banglaFont)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "১. ব্যক্তিগত পরিস্থিতির জন্য দো'আ আর্কিটেক্ট:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "সাধারণ দু'আ লাইব্রেরির মতো শুধু তালিকা না দেখিয়ে এটি আপনার যে কোনো পরিস্থিতি (যেমন: 'My father is sick and I am worried', ঋণ, মানসিক অশান্তি, ইত্যাদি) বুঝে কুরআন ও সুন্নাহর প্রামাণ্য দু'আগুলো ক্রমান্বয়ে সাজিয়ে দেয়।",
                        fontFamily = banglaFont,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "২. ৪টি কাঠামোগত স্তর:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "• কুরআনী দু'আ (সূরা ও আয়াতের তাফসীরসহ)\n• মাসনূন নববী দু'আ (হাদীস নম্বর, মান ও সুন্নাতী প্রয়োগ পদ্ধতিসহ)\n• সাধারণ বৈধ মিনতি (মাতৃভাষায় শরঈ আরজি)\n• কখন ও কীভাবে দু'আ কবুল হয় (শ্রেষ্ঠ সময় ও আদব)",
                        fontFamily = banglaFont,
                        fontSize = 12.sp
                    )

                    Text(
                        text = "৩. নির্ভরযোগ্যতা ও সতর্কতা:",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont,
                        fontSize = 13.sp
                    )
                    Text(
                        text = "অ্যাপে কোনো প্রকার মনগড়া বা জাল দু'আ যুক্ত করা হয়নি। প্রতিটি আয়াত ও হাদীসের সুনির্দিষ্ট রেফারেন্স উল্লেখ রয়েছে।",
                        fontFamily = banglaFont,
                        fontSize = 12.sp
                    )
                }
            },
            confirmButton = {
                Button(onClick = { showHelpDialog = false }) {
                    Text("বুঝেছি", fontFamily = banglaFont)
                }
            }
        )
    }
}

// ----------------------------------------------------
// HELPER COMPOSABLES
// ----------------------------------------------------

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    badge: String,
    badgeColor: Color,
    banglaFont: androidx.compose.ui.text.font.FontFamily
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = badgeColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = badgeColor.copy(alpha = 0.15f),
            border = BorderStroke(0.6.dp, badgeColor.copy(alpha = 0.4f))
        ) {
            Text(
                text = badge,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = badgeColor,
                fontFamily = banglaFont,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun QuranicDuaCard(
    item: QuranicDuaItem,
    arabicFontSize: Int,
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
                Text(
                    text = "উচ্চারণ: ${item.banglaPronunciation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
                Text(
                    text = "অর্থ: “${item.banglaTranslation}”",
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
                    text = if (isExpanded) "তাফসীর প্রেক্ষাপট সংক্ষেপ করুন" else "তাফসীর প্রেক্ষাপট দেখুন...",
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
                Text(
                    text = "উচ্চারণ: ${item.banglaPronunciation}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
                Text(
                    text = "অর্থ: “${item.banglaTranslation}”",
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

private fun copyBlueprintToClipboard(context: Context, blueprint: PersonalDuaBlueprint) {
    val sb = StringBuilder()
    sb.append("দা'ওয়াহ টু জান্নাহ - Personal Dua Builder\n")
    sb.append("পরিস্থিতি: ${blueprint.scenarioTitleBn}\n")
    sb.append("তাওয়াক্কুল ও সান্ত্বনা: ${blueprint.spiritualComfortBn}\n\n")

    sb.append("=== ১. কুরআনী দু'আ ===\n")
    blueprint.quranicDuas.forEach { q ->
        sb.append("• ${q.arabicText}\n")
        sb.append("  অর্থ: ${q.banglaTranslation}\n")
        sb.append("  সূত্র: ${q.referenceText}\n\n")
    }

    sb.append("=== ২. মাসনূন নববী দু'আ ===\n")
    blueprint.propheticDuas.forEach { p ->
        sb.append("• ${p.arabicText}\n")
        sb.append("  অর্থ: ${p.banglaTranslation}\n")
        sb.append("  সূত্র: ${p.hadithBookBn} (${p.hadithNumber}) [${p.gradingBn}]\n")
        sb.append("  আমল: ${p.sunnahPracticeMethodBn}\n\n")
    }

    sb.append("=== ৩. সাধারণ বৈধ আরজি ===\n")
    blueprint.generalSupplications.forEach { g ->
        sb.append("• ${g.titleBn}: \"${g.heartfeltSupplicationBn}\"\n\n")
    }

    sb.append("=== ৪. শ্রেষ্ঠ সময় ও আদব ===\n")
    blueprint.goldenTimingsAndEtiquettes.forEach { e ->
        sb.append("• ${e.timingTitleBn}: ${e.practicalTipBn}\n")
    }

    copyText(context, sb.toString(), "সম্পূর্ণ দো'আ প্ল্যান কপি করা হয়েছে")
}

private fun shareBlueprint(context: Context, blueprint: PersonalDuaBlueprint) {
    val sb = StringBuilder()
    sb.append("🤲 ${blueprint.scenarioTitleBn}\n\n")
    sb.append("📖 কুরআনের দো'আ:\n")
    blueprint.quranicDuas.firstOrNull()?.let {
        sb.append("${it.arabicText}\n\"${it.banglaTranslation}\"\n— ${it.referenceText}\n\n")
    }
    sb.append("🕌 সুন্নাতী দো'আ:\n")
    blueprint.propheticDuas.firstOrNull()?.let {
        sb.append("${it.arabicText}\n\"${it.banglaTranslation}\"\n— ${it.hadithBookBn} (${it.hadithNumber})\n\n")
    }
    sb.append("[Dawah to Jannah - Personal Dua Builder]")

    shareText(context, sb.toString())
}

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
