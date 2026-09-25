package com.example.ui.screens.tools.ramadan

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.RamadanDataCatalog
import com.example.data.model.RamadanDuaCategory
import com.example.data.model.RamadanDuaItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.util.BanglaNumberUtils

@Composable
fun RamadanDuaAndReflectionTab(
    viewModel: RamadanIntelligenceViewModel,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val selectedCategory by viewModel.selectedDuaCategory.collectAsState()
    val personalDuas by viewModel.personalDuas.collectAsState()
    val reflections by viewModel.reflections.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()

    var showAddPersonalDuaDialog by remember { mutableStateOf(false) }
    var newDuaTitle by remember { mutableStateOf("") }
    var newDuaArabic by remember { mutableStateOf("") }
    var newDuaMeaning by remember { mutableStateOf("") }
    var newDuaCategory by remember { mutableStateOf("ব্যক্তিগত আরজি") }

    var isReflectionJournalView by remember { mutableStateOf(false) }

    val categoryDuas = remember(selectedCategory) {
        RamadanDataCatalog.authenticDuas.filter { it.category == selectedCategory }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // HEADER SWITCHER: DUA CENTER VS REFLECTION JOURNAL
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { isReflectionJournalView = false },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isReflectionJournalView) Color(0xFF047857) else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "🤲 রমাদান দো'আ সেন্টার",
                    color = if (!isReflectionJournalView) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            }

            Button(
                onClick = { isReflectionJournalView = true },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isReflectionJournalView) Color(0xFF0284C7) else MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "📝 আত্মদর্শন জার্নাল",
                    color = if (isReflectionJournalView) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            }
        }

        // VIEW 1: DUA CENTER
        if (!isReflectionJournalView) {
            // Authentic Dua Guidance Notice
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("দো'আ কবুল সংক্রান্ত শরয়ী নীতিমালা", fontWeight = FontWeight.Bold, color = IslamicGold, fontFamily = banglaFont)
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "রমাদানে উপস্থাপিত কোনো দো'আ কোনো যান্ত্রিক গ্যারান্টি নয়। দো'আ কবুল আল্লাহর হিকমত, একনিষ্ঠতা ও হালাল রিযিকের ওপর নির্ভরশীল। দো'আ কবুল তিনটি রূপে হতে পারে: দুনিয়াতে চাওয়া পূরণ, অনাগত বিপদ প্রতিহতকরণ, অথবা আখিরাতের মহা সঞ্চয় (মুসনাদ আহমাদ ১১১৩৩)।",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 16.sp,
                        fontFamily = banglaFont
                    )
                }
            }

            // 15+ Dua Categories Horizontal Scroller
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(RamadanDuaCategory.entries) { category ->
                    val isSelected = selectedCategory == category
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.setSelectedDuaCategory(category) },
                        label = { Text("${category.iconEmoji} ${category.titleBn}", fontSize = 11.sp, fontFamily = banglaFont) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF047857),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            // Category Duas List
            if (categoryDuas.isNotEmpty()) {
                categoryDuas.forEach { dua ->
                    Card(
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(dua.titleBn, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary, fontFamily = banglaFont)
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFF059669).copy(alpha = 0.15f)) {
                                    Text(dua.sourceType.labelBn, fontSize = 10.sp, color = Color(0xFF059669), fontWeight = FontWeight.Bold, fontFamily = banglaFont, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = dua.arabicText,
                                style = MaterialTheme.typography.titleMedium.copy(lineHeight = 30.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = arabicFont,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            Text("উচ্চারণ: ${dua.pronunciationBn}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("অর্থ: ${dua.meaningBn}", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("ফযিলত ও প্রেক্ষাপট: ${dua.virtuesBn}", fontSize = 11.sp, color = Color(0xFF0284C7), fontFamily = banglaFont)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("রেফারেন্স: ${dua.referenceBn}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline, fontFamily = banglaFont)
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "এই ক্যাটাগরিতে ব্যক্তিগত দো'আ যোগ করুন অথবা নিচের বাটনে ট্যাপ করুন।",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // MY DUA LIST SECTION (PERSONAL DUA LIST)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📝", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("আমার দো'আ তালিকা (My Dua List)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium, fontFamily = banglaFont)
                        }

                        Button(
                            onClick = { showAddPersonalDuaDialog = !showAddPersonalDuaDialog },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("নতুন দো'আ", fontSize = 11.sp, color = Color.White, fontFamily = banglaFont)
                        }
                    }

                    // Add personal dua inputs
                    AnimatedVisibility(visible = showAddPersonalDuaDialog) {
                        Column(modifier = Modifier.padding(top = 12.dp)) {
                            OutlinedTextField(
                                value = newDuaTitle,
                                onValueChange = { newDuaTitle = it },
                                placeholder = { Text("দো'আর বিষয় (যেমন: পিতা-মাতার সুস্থতা, রিজিকের বরকত)", fontSize = 12.sp, fontFamily = banglaFont) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = newDuaArabic,
                                onValueChange = { newDuaArabic = it },
                                placeholder = { Text("আরবি পাঠ (ঐচ্ছিক)", fontSize = 12.sp) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = newDuaMeaning,
                                onValueChange = { newDuaMeaning = it },
                                placeholder = { Text("দো'আর বাংলা অর্থ বা অন্তরের আরজি...", fontSize = 12.sp, fontFamily = banglaFont) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    if (newDuaTitle.isNotBlank()) {
                                        viewModel.addPersonalDua(newDuaTitle, newDuaArabic, newDuaMeaning, newDuaCategory)
                                        newDuaTitle = ""
                                        newDuaArabic = ""
                                        newDuaMeaning = ""
                                        showAddPersonalDuaDialog = false
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("তালিকায় সংরক্ষণ করুন", fontSize = 12.sp, fontFamily = banglaFont)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (personalDuas.isNotEmpty()) {
                        personalDuas.forEach { dua ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(dua.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, fontFamily = banglaFont)
                                        if (dua.arabicText.isNotBlank()) {
                                            Text(dua.arabicText, style = MaterialTheme.typography.bodyMedium, fontFamily = arabicFont)
                                        }
                                        if (dua.meaningBn.isNotBlank()) {
                                            Text(dua.meaningBn, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                                        }
                                    }
                                    IconButton(onClick = { viewModel.deletePersonalDua(dua.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "আপনার ব্যক্তিগত কোনো দো'আ এখনো যুক্ত করা হয়নি। রমাদানে যা যা চাইতে চান লিখে রাখুন।",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        // VIEW 2: REFLECTION JOURNAL
        if (isReflectionJournalView) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "রমাদান আত্মদর্শন জার্নাল (Reflection Journal)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "রমাদানের ৩০ দিনে আপনার অন্তরের পরিবর্তন ও উপলব্ধিগুলো সংরক্ষিত থাকে",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (reflections.isNotEmpty()) {
                        reflections.forEach { refl ->
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text("দিন ${BanglaNumberUtils.toBanglaDigits(refl.dayNumber)}: ${refl.promptQuestion}", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF0369A1), fontFamily = banglaFont)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(refl.userAnswer, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "এখনো কোনো জার্নাল এন্ট্রি নেই। ড্যাশবোর্ড থেকে প্রতিদিনের আত্মদর্শন প্রশ্নে উত্তর লিখে সংরক্ষণ করুন।",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
