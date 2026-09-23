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
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.DuaBySituationCatalog
import com.example.data.model.DuaSourceCategory
import com.example.data.model.SituationDuaItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DuaBySituationScreen(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    // State for selections
    var selectedFeelingId by remember { mutableStateOf<String?>("anxious") }
    var selectedNeedId by remember { mutableStateOf<String?>("patience") }
    var customFeelingInput by remember { mutableStateOf("") }
    var customNeedInput by remember { mutableStateOf("") }
    var showCustomInputBox by remember { mutableStateOf(false) }

    // Category filter: ALL, QURAN, HADITH
    var selectedCategoryFilter by remember { mutableStateOf<DuaSourceCategory?>(null) }

    // Arabic Font Size Controller
    var arabicFontSize by remember { mutableFloatStateOf(24f) }

    // Practice counter state per dua id
    val counterState = remember { mutableStateMapOf<String, Int>() }

    // Calculate matched results
    val matchedDuas by remember(selectedFeelingId, selectedNeedId, customFeelingInput, customNeedInput, selectedCategoryFilter) {
        derivedStateOf {
            val base = DuaBySituationCatalog.filterDuas(
                feelingId = selectedFeelingId,
                needId = selectedNeedId,
                customFeeling = customFeelingInput,
                customNeed = customNeedInput
            )
            if (selectedCategoryFilter != null) {
                base.filter { it.category == selectedCategoryFilter }
            } else {
                base
            }
        }
    }

    val selectedFeelingObj = DuaBySituationCatalog.feelings.find { it.id == selectedFeelingId }
    val selectedNeedObj = DuaBySituationCatalog.needs.find { it.id == selectedNeedId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Dua by Situation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "অনুভূতি ও প্রয়োজন অনুযায়ী প্রামাণ্য দু'আ",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    // Font Size Cycle
                    IconButton(onClick = {
                        arabicFontSize = if (arabicFontSize >= 30f) 22f else arabicFontSize + 3f
                    }) {
                        Icon(
                            Icons.Default.FormatSize,
                            contentDescription = "আরবি হরফ সাইজ",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Reset Filters
                    IconButton(onClick = {
                        selectedFeelingId = null
                        selectedNeedId = null
                        customFeelingInput = ""
                        customNeedInput = ""
                        selectedCategoryFilter = null
                    }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "রিসেট করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ========================================================
            // 1. DUAL-AXIS SEARCH SYSTEM: "I feel..." & "I need..."
            // ========================================================
            item {
                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Section 1: "I feel..."
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("১", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 13.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "I feel... (আমার অনুভূতি)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "আপনার বর্তমান মানসিক অবস্থা বা আবেগ স্পর্শ করুন:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(start = 36.dp, bottom = 8.dp)
                        )

                        // FlowRow of Feeling Chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DuaBySituationCatalog.feelings.forEach { feeling ->
                                val isSelected = selectedFeelingId == feeling.id
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 0.5.dp,
                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.clickable {
                                        selectedFeelingId = if (isSelected) null else feeling.id
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = feeling.emoji, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = feeling.titleEn,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "(${feeling.titleBn.split("/").first().trim()})",
                                            fontFamily = banglaFont,
                                            color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                        Spacer(modifier = Modifier.height(14.dp))

                        // Section 2: "I need..."
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = IslamicGold.copy(alpha = 0.2f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("২", fontWeight = FontWeight.Bold, color = IslamicGold, fontSize = 13.sp)
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "I need... (আমার প্রয়োজন)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = "আল্লাহর কাছে আপনি এই মুহূর্তে যা কামনা করছেন:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(start = 36.dp, bottom = 8.dp)
                        )

                        // FlowRow of Need Chips
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            DuaBySituationCatalog.needs.forEach { need ->
                                val isSelected = selectedNeedId == need.id
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) IslamicGold else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    border = BorderStroke(
                                        width = if (isSelected) 1.5.dp else 0.5.dp,
                                        color = if (isSelected) IslamicGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    modifier = Modifier.clickable {
                                        selectedNeedId = if (isSelected) null else need.id
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(text = need.emoji, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = need.titleEn,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) Color.Black else MaterialTheme.colorScheme.onSurface,
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "(${need.titleBn.split(" ").first().trim()})",
                                            fontFamily = banglaFont,
                                            color = if (isSelected) Color.Black.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Toggle for Custom Situation / Need
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (showCustomInputBox) "কাস্টম অনুসন্ধান সংক্ষেপ করুন" else "+ কাস্টম অনুভূতি বা প্রয়োজন লিখুন",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { showCustomInputBox = !showCustomInputBox }
                                    .padding(vertical = 4.dp, horizontal = 4.dp)
                            )

                            if (selectedFeelingId != null || selectedNeedId != null || customFeelingInput.isNotBlank() || customNeedInput.isNotBlank()) {
                                Text(
                                    text = "ফিল্টার মুছুন",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.error,
                                    fontFamily = banglaFont,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            selectedFeelingId = null
                                            selectedNeedId = null
                                            customFeelingInput = ""
                                            customNeedInput = ""
                                        }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = showCustomInputBox) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = customFeelingInput,
                                    onValueChange = { customFeelingInput = it },
                                    label = { Text("কাস্টম অনুভূতি (যেমন: অস্থিরতা, বাবা অসুস্থ, ভয়)", fontFamily = banglaFont) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
                                    trailingIcon = {
                                        if (customFeelingInput.isNotBlank()) {
                                            IconButton(onClick = { customFeelingInput = "" }) {
                                                Icon(Icons.Default.Close, contentDescription = "মুছুন")
                                            }
                                        }
                                    }
                                )

                                OutlinedTextField(
                                    value = customNeedInput,
                                    onValueChange = { customNeedInput = it },
                                    label = { Text("কাস্টম প্রয়োজন (যেমন: শেফা, ঋণমুক্তি, হেদায়েত)", fontFamily = banglaFont) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    leadingIcon = { Icon(Icons.Default.FilterAlt, contentDescription = null, tint = IslamicGold) },
                                    trailingIcon = {
                                        if (customNeedInput.isNotBlank()) {
                                            IconButton(onClick = { customNeedInput = "" }) {
                                                Icon(Icons.Default.Close, contentDescription = "মুছুন")
                                            }
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }

            // ========================================================
            // 2. ACTIVE SYNTHESIS BANNER
            // ========================================================
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "নির্বাচিত পরিস্থিতি ও প্রয়োজন:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = banglaFont
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary
                            ) {
                                Text(
                                    text = "${matchedDuas.size}টি প্রামাণ্য দু'আ",
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        val feelingDesc = selectedFeelingObj?.let { "অনুভূতি: ${it.emoji} ${it.titleEn} (${it.titleBn})" }
                            ?: if (customFeelingInput.isNotBlank()) "অনুভূতি: $customFeelingInput" else "যেকোনো অনুভূতি"

                        val needDesc = selectedNeedObj?.let { "প্রয়োজন: ${it.emoji} ${it.titleEn} (${it.titleBn})" }
                            ?: if (customNeedInput.isNotBlank()) "প্রয়োজন: $customNeedInput" else "যেকোনো সাহায্য"

                        Text(
                            text = "“$feelingDesc  ✦  $needDesc”",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            // ========================================================
            // 3. SOURCE CATEGORY FILTER CHIPS: ALL, QURAN, HADITH
            // ========================================================
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategoryFilter == null,
                        onClick = { selectedCategoryFilter = null },
                        label = { Text("সকল উৎস (${matchedDuas.size})", fontFamily = banglaFont) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    FilterChip(
                        selected = selectedCategoryFilter == DuaSourceCategory.QURAN,
                        onClick = { selectedCategoryFilter = if (selectedCategoryFilter == DuaSourceCategory.QURAN) null else DuaSourceCategory.QURAN },
                        label = { Text("কুরআনী দু'আ", fontFamily = banglaFont) },
                        leadingIcon = { Text("📖", fontSize = 12.sp) }
                    )
                    FilterChip(
                        selected = selectedCategoryFilter == DuaSourceCategory.HADITH,
                        onClick = { selectedCategoryFilter = if (selectedCategoryFilter == DuaSourceCategory.HADITH) null else DuaSourceCategory.HADITH },
                        label = { Text("সহীহ হাদীসের দু'আ", fontFamily = banglaFont) },
                        leadingIcon = { Text("📜", fontSize = 12.sp) }
                    )
                }
            }

            // ========================================================
            // 4. AUTHENTIC SUPPLICATION CARDS
            // ========================================================
            if (matchedDuas.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🤲", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "এই ফিল্টারে সরাসরি কোনো দু'আ মেলেনি",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "অনুগ্রহ করে অনুভূতি বা প্রয়োজনে কিছুটা ভিন্ন নির্বাচন করুন অথবা 'সকল উৎস' দেখুন।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Button(
                                onClick = {
                                    selectedFeelingId = null
                                    selectedNeedId = null
                                    customFeelingInput = ""
                                    customNeedInput = ""
                                    selectedCategoryFilter = null
                                }
                            ) {
                                Text("সকল প্রামাণ্য দু'আ দেখুন", fontFamily = banglaFont)
                            }
                        }
                    }
                }
            } else {
                items(matchedDuas, key = { it.id }) { duaItem ->
                    val currentCount = counterState[duaItem.id] ?: 0

                    SituationDuaCard(
                        duaItem = duaItem,
                        arabicFontSize = arabicFontSize,
                        currentPracticeCount = currentCount,
                        onIncrementCount = {
                            counterState[duaItem.id] = currentCount + 1
                        },
                        onResetCount = {
                            counterState[duaItem.id] = 0
                        },
                        onCopy = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipText = buildString {
                                appendLine("【 ${duaItem.titleBn} 】")
                                appendLine(duaItem.arabicText)
                                appendLine()
                                appendLine("উচ্চারণ: ${duaItem.banglaPronunciation}")
                                appendLine()
                                appendLine("অর্থ: ${duaItem.banglaTranslation}")
                                appendLine()
                                appendLine("রেফারেন্স: ${duaItem.referenceCitation}")
                                appendLine("তাফসীর ও প্রেক্ষাপট: ${duaItem.contextAndTafsirBn}")
                                appendLine("আমল: ${duaItem.sunnahPracticeMethodBn}")
                                appendLine("— দা'ওয়াহ টু জান্নাহ অ্যাপ")
                            }
                            clipboard.setPrimaryClip(ClipData.newPlainText("Dua by Situation", clipText))
                            Toast.makeText(context, "দো'আ ও রেফারেন্স কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        onShare = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, duaItem.titleBn)
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
                                    【 ${duaItem.titleBn} 】
                                    
                                    ${duaItem.arabicText}
                                    
                                    উচ্চারণ: ${duaItem.banglaPronunciation}
                                    
                                    অর্থ: ${duaItem.banglaTranslation}
                                    
                                    রেফারেন্স: ${duaItem.referenceCitation}
                                    আমল ও তাৎপর্য: ${duaItem.contextAndTafsirBn}
                                    
                                    — দা'ওয়াহ টু জান্নাহ (Dua by Situation)
                                    """.trimIndent()
                                )
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন"))
                        }
                    )
                }
            }

            // Bottom Spacing & Integrity Assurance Footer
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Info, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "প্রামাণ্য রেফারেন্স ও শরঈ নীতিমালা",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "এই টুলের প্রতিটি দু'আ পবিত্র কুরআন এবং সিহাহ সিত্তাহ সহ বিশুদ্ধ হাদীস গ্রন্থ থেকে সংকলিত। কুরআনের আয়াত, সহীহ হাদীসের সনদ, তাফসীর ও উলামাদের ব্যাখ্যার মাঝে সুস্পষ্ট সীমারেখা বজায় রাখা হয়েছে। কোনো মনগড়া বা জাল বক্তব্য সংযোজন করা হয়নি।",
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun SituationDuaCard(
    duaItem: SituationDuaItem,
    arabicFontSize: Float,
    currentPracticeCount: Int,
    onIncrementCount: () -> Unit,
    onResetCount: () -> Unit,
    onCopy: () -> Unit,
    onShare: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val isQuran = duaItem.category == DuaSourceCategory.QURAN
    val categoryBadgeColor = if (isQuran) Color(0xFF059669) else Color(0xFF2563EB)
    val categoryLabel = if (isQuran) "পবিত্র কুরআন" else "সহীহ হাদীস"

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header: Category Pill & Source Attribution
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = categoryBadgeColor.copy(alpha = 0.12f),
                    border = BorderStroke(0.8.dp, categoryBadgeColor.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(if (isQuran) "📖" else "📜", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = categoryLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = categoryBadgeColor,
                            fontFamily = banglaFont
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onCopy, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = "কপি করুন",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "শেয়ার করুন",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = duaItem.titleBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            // Reference Subtitle
            Text(
                text = duaItem.referenceCitation + (duaItem.narratorCompanionBn?.let { " • বর্ণনাকারী: $it" } ?: ""),
                style = MaterialTheme.typography.labelSmall,
                color = categoryBadgeColor,
                fontWeight = FontWeight.SemiBold,
                fontFamily = banglaFont,
                modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
            )

            // ARABIC TEXT BOX (Outstanding Arabic Typography)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = duaItem.arabicText,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = arabicFontSize.sp,
                            lineHeight = (arabicFontSize * 1.75f).sp
                        ),
                        fontFamily = arabicFont,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bangla Pronunciation
            Text(
                text = "উচ্চারণ: ${duaItem.banglaPronunciation}",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Bangla Translation
            Text(
                text = "অর্থ: ${duaItem.banglaTranslation}",
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 23.sp),
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Context & Tafsir (Clearly Distinguished)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "তাফসীর ও প্রেক্ষাপট",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = duaItem.contextAndTafsirBn,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sunnah Practice Method
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = IslamicGold.copy(alpha = 0.15f),
                    modifier = Modifier.padding(top = 2.dp)
                ) {
                    Text(
                        text = "সুন্নাতী আমল",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = duaItem.sunnahPracticeMethodBn,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont,
                    modifier = Modifier.weight(1f)
                )
            }

            // Scholarly Nuance (If present)
            duaItem.scholarlyNuanceBn?.let { nuance ->
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.12f),
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = "উলামাদের ব্যাখ্যা",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = nuance,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Practice Counter
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "আমল কাউন্টার: ",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "$currentPracticeCount বার",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.ExtraBold,
                            color = if (currentPracticeCount > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (currentPracticeCount > 0) {
                            OutlinedButton(
                                onClick = onResetCount,
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("রিসেট", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                            }
                        }

                        Button(
                            onClick = onIncrementCount,
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 4.dp),
                            modifier = Modifier.height(34.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Text("+১ আমল", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                        }
                    }
                }
            }
        }
    }
}
