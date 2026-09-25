package com.example.ui.screens.tools.ramadan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.RamadanDataCatalog
import com.example.data.model.GoalIntensity
import com.example.data.model.RamadanChecklistItem
import com.example.data.model.RamadanLifestyleMode
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.util.BanglaNumberUtils

@Composable
fun RamadanPreparationTab(
    viewModel: RamadanIntelligenceViewModel,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current

    val lifestyleMode by viewModel.lifestyleMode.collectAsState()
    val goalIntensity by viewModel.goalIntensity.collectAsState()
    val checklistStatuses by viewModel.checklistStatuses.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var showLifestyleDialog by remember { mutableStateOf(false) }
    var selectedCategoryFilter by remember { mutableStateOf<String?>(null) }
    var newCustomTaskTitle by remember { mutableStateOf("") }
    var isAddingTask by remember { mutableStateOf(false) }

    val categories = remember {
        RamadanDataCatalog.preparationChecklist.map { it.categoryBn }.distinct()
    }

    val completedKeys = remember(checklistStatuses) {
        checklistStatuses.filter { it.isChecked }.map { it.itemKey }.toSet()
    }

    val filteredChecklist = remember(selectedCategoryFilter) {
        if (selectedCategoryFilter == null) {
            RamadanDataCatalog.preparationChecklist
        } else {
            RamadanDataCatalog.preparationChecklist.filter { it.categoryBn == selectedCategoryFilter }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER HERO: BEFORE RAMADAN PREPARATION CENTER
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, Color(0xFF059669).copy(alpha = 0.5f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF059669).copy(alpha = 0.15f),
                                IslamicGold.copy(alpha = 0.08f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(18.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF059669)
                        ) {
                            Text(
                                text = "প্রস্তুতি কেন্দ্র • Before Ramadan",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Text("🌙", fontSize = 22.sp)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "রমাদানের প্রস্তুতি ও সংকল্প কেন্দ্র",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )

                    Text(
                        text = "রমাদান যেন হঠাৎ এসে চলে না যায়—আগে থেকেই নিজের রুটিন, ঘুম, কাজ, পরিবার ও লক্ষ্য গুছিয়ে নিন। সালাফগণ শাবান মাস থেকেই রমাদানের প্রস্তুতি সম্পন্ন করতেন।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // 1. RAMADAN FOR DIFFERENT USERS (LIFESTYLE MODES)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "আপনার রমাদান জীবনধারা (Lifestyle Mode)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "বাস্তবসম্মত সময়সূচী অনুযায়ী কাস্টমাইজ করুন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                    Text(lifestyleMode.iconEmoji, fontSize = 24.sp)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lifestyle Selector Buttons
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RamadanLifestyleMode.entries.forEach { mode ->
                        val isSelected = lifestyleMode == mode
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = if (isSelected) Color(0xFF047857).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = BorderStroke(1.2.dp, if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.setLifestyleMode(mode) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(mode.iconEmoji, fontSize = 22.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = mode.titleBn,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = mode.descriptionBn,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = Color(0xFF047857),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 2. RAMADAN GOAL BUILDER (MULTI-DIMENSIONAL & INTENSITY LEVELS)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "রমাদান গোল বিল্ডার (Goal Builder)",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Text(
                    text = "শুধু পৃষ্ঠা সংখ্যা নয়—৫টি মৌলিক মাত্রায় বাস্তবসম্মত লক্ষ্য নির্ধারণ",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Intensity Selection: Minimum, Target, Stretch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    GoalIntensity.entries.forEach { intensity ->
                        val isSelected = goalIntensity == intensity
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                            border = BorderStroke(1.dp, if (isSelected) IslamicGold else Color.Transparent),
                            modifier = Modifier.weight(1f).clickable { viewModel.setGoalIntensity(intensity) }
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = intensity.titleBn.split(" ")[0],
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = intensity.badgeBn,
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // The 5 Dimensions Cards
                listOf(
                    Triple("📖 কুরআন লক্ষ্য", "১ পারা / দিন (১ খতম টার্গেট) • অর্থ ও তাদাব্বুর", "ফজরের পর ও এশার পর নিবিড় পাঠ"),
                    Triple("🕌 সালাত ও ক্বিয়াম", "৫ ওয়াক্ত জামা'আত • তারাবীহ নিয়মিত • ২ রাকাত তাহাজ্জুদ", "শেষ দশকে রাত জাগরণের প্রস্তুতি"),
                    Triple("🤲 দো'আ ও প্রার্থনা", "দৈনিক ১০ মিনিট একাকী দো'আ • ব্যক্তিগত দো'আ তালিকা", "ইফতার ও সেহরির পূর্বে আরজি"),
                    Triple("💚 দান ও সদাকাহ", "সাপ্তাহিক নিয়মিত দান • যাকাতের পূর্ণ হিসাব ও বণ্টন", "ইফতার করানো ও অসহায়কে সাহায্য"),
                    Triple("🌱 চরিত্র ও আত্মশুদ্ধি", "ক্রোধ নিয়ন্ত্রণ • জিহ্বার সংযম • আত্মীয়তার সম্পর্ক জোড়া লাগানো", "গীবত ও সোশ্যাল মিডিয়া মুক্ত থাকা")
                ).forEach { (title, target, tip) ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, fontFamily = banglaFont)
                                Text(goalIntensity.badgeBn, fontSize = 11.sp, color = MaterialTheme.colorScheme.primary, fontFamily = banglaFont)
                            }
                            Text(target, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            Text("টিপস: $tip", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                        }
                    }
                }
            }
        }

        // 3. PERSONALIZED RAMADAN PLAN PREVIEW
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A).copy(alpha = 0.04f)),
            border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("📋", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "আপনার ব্যক্তিগত রমাদান রূপরেখা (Your Plan)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "• মোড: ${lifestyleMode.titleBn}\n" +
                           "• কুরআন: দৈনিক ${lifestyleMode.recommendedQuranJuz} পারা তাদাব্বুর সহ\n" +
                           "• তারাবীহ: ${lifestyleMode.defaultTaraweehRakah} রাকাত জামা'আতে উপস্থিত থাকার সর্বোচ্চ চেষ্টা\n" +
                           "• দো'আ: দৈনিক অন্তত ${lifestyleMode.dailyDuaMinutes} মিনিট একাকী মুনাজাত\n" +
                           "• চরিত্র: সচেতনভাবে জিহ্বার নিয়ন্ত্রণ ও ক্ষমাশীলতা প্রদর্শন\n" +
                           "• সদাকাহ: নিয়মিত সাপ্তাহিক বা দৈনিক ক্ষুদ্র হলেও নিশ্চিত দান",
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Non-punitive catch-up helper card
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF047857).copy(alpha = 0.1f)
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "কোনো কারণে লক্ষ্য ছুটে গেলে অ্যাপ কোনো চাপ সৃষ্টি করে না; বরং ধাপে ধাপে কভার করার নমনীয় বিকল্প দেয়।",
                            fontSize = 11.sp,
                            color = Color(0xFF065F46),
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        // 4. INTELLIGENT PREPARATION CHECKLIST (12 AREAS)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "প্রস্তুতি চেকলিস্ট (Checklist)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "সম্পন্ন: ${BanglaNumberUtils.toBanglaDigits(completedKeys.size)} / ${BanglaNumberUtils.toBanglaDigits(RamadanDataCatalog.preparationChecklist.size)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF059669),
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    }

                    OutlinedButton(
                        onClick = { isAddingTask = !isAddingTask },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("কাস্টম টাস্ক", fontSize = 11.sp, fontFamily = banglaFont)
                    }
                }

                // Add Custom Task Input
                AnimatedVisibility(visible = isAddingTask) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        OutlinedTextField(
                            value = newCustomTaskTitle,
                            onValueChange = { newCustomTaskTitle = it },
                            placeholder = { Text("নতুন প্রস্তুতি লক্ষ্য লিখুন...", fontSize = 12.sp, fontFamily = banglaFont) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Button(
                            onClick = {
                                if (newCustomTaskTitle.isNotBlank()) {
                                    val key = "custom_${System.currentTimeMillis()}"
                                    viewModel.toggleChecklist(key, false)
                                    newCustomTaskTitle = ""
                                    isAddingTask = false
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("যুক্ত করুন", fontSize = 12.sp, fontFamily = banglaFont)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Checklist Items
                filteredChecklist.forEach { item ->
                    val isChecked = completedKeys.contains(item.key)
                    var isExpanded by remember { mutableStateOf(false) }

                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isChecked) Color(0xFF059669).copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        border = BorderStroke(0.8.dp, if (isChecked) Color(0xFF059669).copy(alpha = 0.4f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth().clickable {
                                    viewModel.toggleChecklist(item.key, !isChecked)
                                }
                            ) {
                                Icon(
                                    imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isChecked) Color(0xFF059669) else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.titleBn,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (isChecked) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = item.categoryBn,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                }

                                IconButton(
                                    onClick = { isExpanded = !isExpanded },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            AnimatedVisibility(visible = isExpanded) {
                                Column(modifier = Modifier.padding(top = 8.dp, start = 30.dp)) {
                                    Text(
                                        text = item.descriptionBn,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "দলীল: ${item.referenceBn}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0284C7),
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
