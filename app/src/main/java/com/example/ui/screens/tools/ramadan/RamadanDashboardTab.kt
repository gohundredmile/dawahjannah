package com.example.ui.screens.tools.ramadan

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.RamadanDataCatalog
import com.example.data.model.RamadanLoopTab
import com.example.data.model.RamadanTimeOfDay
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.util.BanglaNumberUtils

@Composable
fun RamadanDashboardTab(
    viewModel: RamadanIntelligenceViewModel,
    onOpenRamadanSchedule: () -> Unit,
    onOpenDetailedSehriIftar: () -> Unit,
    onNavigateToLoop: (RamadanLoopTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val selectedDay by viewModel.selectedDay.collectAsState()
    val timeOfDay by viewModel.timeOfDay.collectAsState()
    val lifestyleMode by viewModel.lifestyleMode.collectAsState()
    val prayerStatus by viewModel.prayerStatus.collectAsState()
    val currentLog by viewModel.currentDayLog.collectAsState()
    val currentReflection by viewModel.currentDayReflection.collectAsState()
    val quranPace by viewModel.quranPaceStatus.collectAsState()
    val adaptiveSuggestion by viewModel.adaptiveSuggestion.collectAsState()

    val dailyReflectionPrompt = remember(selectedDay) {
        RamadanDataCatalog.dailyReflections.find { it.dayNumber == selectedDay }
            ?: RamadanDataCatalog.dailyReflections.first()
    }

    var reflectionInput by remember(currentReflection.userAnswer) {
        mutableStateOf(currentReflection.userAnswer)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // 1. RAMADAN DAY SELECTOR & DAYS REMAINING
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "🌙 রমাদান ${BanglaNumberUtils.toBanglaDigits(selectedDay)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = IslamicGold.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = lifestyleMode.titleBn.split(" ")[0],
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IslamicGold,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "বাকি আছে ${BanglaNumberUtils.toBanglaDigits(30 - selectedDay)} দিন • ৩০ দিনের আত্মশুদ্ধি সফর",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    // Fasting Status Toggle
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (currentLog.isFasted) Color(0xFF059669).copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, if (currentLog.isFasted) Color(0xFF059669) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.clickable {
                            viewModel.updateDayLog(isFasted = !currentLog.isFasted)
                        }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (currentLog.isFasted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (currentLog.isFasted) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (currentLog.isFasted) "সিয়াম পালনকৃত" else "সিয়াম ট্র্যাকার",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (currentLog.isFasted) Color(0xFF059669) else MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Horizontal Day Scroller
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(30) { index ->
                        val day = index + 1
                        val isSelected = day == selectedDay
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = BorderStroke(1.dp, if (isSelected) IslamicGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                            modifier = Modifier.clickable { viewModel.setSelectedDay(day) }
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "দিন",
                                    fontSize = 10.sp,
                                    color = if (isSelected) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = BanglaNumberUtils.toBanglaDigits(day),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. LIVE SUHOOR & IFTAR HERO CARD + DIRECT SCHEDULE SHORTCUTS
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.6f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF047857).copy(alpha = 0.16f),
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
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("⏳", fontSize = 18.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "আজকের সেহেরি ও ইফতার মুহূর্ত",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant
                        ) {
                            Text(
                                text = prayerStatus.locationNameBn,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Sehri Time Box
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFF0F172A).copy(alpha = 0.06f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🥣 শেষ সেহরি", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = prayerStatus.nextSehriFormatted,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("ভোর / সুবহে সাদিক", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                            }
                        }

                        // Iftar Time Box
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color(0xFFD97706).copy(alpha = 0.1f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("🌇 ইফতারের সময়", style = MaterialTheme.typography.labelSmall, color = Color(0xFFB45309), fontFamily = banglaFont)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = prayerStatus.nextIftarFormatted,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFFB45309)
                                )
                                Text(
                                    text = "বাকি: ${prayerStatus.iftarRemainingHMS}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // MANDATORY LINKS: "রমাদান সময়সূচী" & "সেহেরি ও ইফতারের বিস্তারিত সময়সূচী"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onOpenRamadanSchedule,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("রমাদান সময়সূচী", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White, fontFamily = banglaFont)
                        }

                        Button(
                            onClick = onOpenDetailedSehriIftar,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color.Black)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("সেহেরি-ইফতার বিস্তারিত", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black, fontFamily = banglaFont)
                        }
                    }
                }
            }
        }

        // 3. ADAPTIVE TIME-OF-DAY FOCUS (Morning, Afternoon, Pre-Maghrib, Night)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(timeOfDay.iconEmoji, fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "এখনকার প্রাধান্য: ${timeOfDay.titleBn}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = timeOfDay.subtitleBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Time Slot Switcher
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    RamadanTimeOfDay.entries.forEach { slot ->
                        FilterChip(
                            selected = timeOfDay == slot,
                            onClick = { viewModel.setTimeOfDay(slot) },
                            label = { Text("${slot.iconEmoji} ${slot.titleBn.split(" ")[0]}", fontFamily = banglaFont) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(0xFF047857),
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Contextual Highlights depending on time
                when (timeOfDay) {
                    RamadanTimeOfDay.MORNING -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF0284C7).copy(alpha = 0.08f),
                            border = BorderStroke(0.6.dp, Color(0xFF0284C7).copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("☀️ সকালের আমল ও লক্ষ্য:", fontWeight = FontWeight.Bold, color = Color(0xFF0369A1), fontFamily = banglaFont)
                                Text("• ফজরের পর সুর্যোদয় পর্যন্ত কুরআন তিলাওয়াত ও সকালের মাসনুন আযকার।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Text("• রোযার নিয়তকে একনিষ্ঠ রাখা এবং সারাদিন জিহ্বাকে মিথ্যা ও গীবত থেকে মুক্ত রাখার সংকল্প।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            }
                        }
                    }
                    RamadanTimeOfDay.AFTERNOON -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFD97706).copy(alpha = 0.08f),
                            border = BorderStroke(0.6.dp, Color(0xFFD97706).copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("🌤️ দুপুরের সজীবতা ও দান:", fontWeight = FontWeight.Bold, color = Color(0xFFB45309), fontFamily = banglaFont)
                                Text("• যোহরের পর কিছু কুরআন অধ্যয়ন বা সংক্ষিপ্ত ক্বাইলুলাহ (দুপুরের সামান্য বিশ্রাম)।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Text("• কর্মক্ষেত্রে আমানতদারী ও সহকর্মীদের সাথে অমায়িক আচরণ—যা সিয়ামের প্রকৃত দাবি।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            }
                        }
                    }
                    RamadanTimeOfDay.PRE_MAGHRIB -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFEF4444).copy(alpha = 0.08f),
                            border = BorderStroke(0.6.dp, Color(0xFFEF4444).copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("🤲 ইফতারের পূর্বমুহূর্ত — দো'আ কবুলের মহাক্ষণ!", fontWeight = FontWeight.Bold, color = Color(0xFFB91C1C), fontFamily = banglaFont)
                                Text("• হাদিস: «তিন ব্যক্তির দো'আ ফিরিয়ে দেওয়া হয় না... তার মধ্যে অন্যতম রোজাদারের ইফতারের মুহূর্তের দো'আ» (তিরমিযী ৩৫৯৮)।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Text("• রান্না বা অতিরিক্ত আড্ডায় সময় নষ্ট না করে হাত তুলে রবের সমীপে নিজের ও উম্মাহর মাগফিরাত চান।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            }
                        }
                    }
                    RamadanTimeOfDay.NIGHT -> {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF4F46E5).copy(alpha = 0.08f),
                            border = BorderStroke(0.6.dp, Color(0xFF4F46E5).copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("🌙 রাতের নূর — সালাতুত তারাবীহ ও তাহাজ্জুদ:", fontWeight = FontWeight.Bold, color = Color(0xFF4338CA), fontFamily = banglaFont)
                                Text("• ইমামের সাথে একাগ্রতায় তারাবীহর সালাত সমাপ্ত করা সারারাত নামাযের সওয়াব এনে দেয় (তিরমিযী ৮০৬)।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Text("• শেষ দশকে বিশেষ করে বেজোড় রাতগুলোতে বেশি বেশি তাহাজ্জুদ ও কদরের সন্ধানে নিবেদিত থাকা।", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }

        // 4. TODAY'S PERSONAL RAMADAN FOCUS & ACTIONS (TODAY'S ACTION OS)
        Card(
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "আজকের ব্যক্তিগত রমাদান লক্ষ্য",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "Consistency > Intensity • ধারাবাহিক ছোট আমলই সর্বোত্তম",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    IconButton(onClick = { onNavigateToLoop(RamadanLoopTab.BEFORE_RAMADAN) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Goals", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Action 1: Quran Target
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📖", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "কুরআন তিলাওয়াত: ${BanglaNumberUtils.toBanglaDigits(currentLog.quranPagesRead)} / ${BanglaNumberUtils.toBanglaDigits(quranPace.todayRecommendedPages)} পৃষ্ঠা",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = quranPace.paceMessageBn,
                                fontSize = 11.sp,
                                color = if (quranPace.isBehind) Color(0xFFD97706) else Color(0xFF059669),
                                fontFamily = banglaFont
                            )
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                val current = currentLog.quranPagesRead
                                if (current > 0) viewModel.updateDayLog(quranPagesRead = current - 1)
                            }) {
                                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                            IconButton(onClick = {
                                viewModel.updateDayLog(quranPagesRead = currentLog.quranPagesRead + 1)
                            }) {
                                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 2: Taraweeh / Night Prayer
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.updateDayLog(isTaraweeh = !currentLog.isTaraweeh)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🕌", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "সালাতুত তারাবীহ ও রাতের ক্বিয়াম",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = if (currentLog.isTaraweeh) "সম্পন্ন হয়েছে আলহামদুলিল্লাহ (${BanglaNumberUtils.toBanglaDigits(currentLog.taraweehRakahs)} রাকাত)" else "আজকের জামা'আত বা একাকী পরিকল্পনা",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Icon(
                            imageVector = if (currentLog.isTaraweeh) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (currentLog.isTaraweeh) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 3: Focused Dua Session
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.updateDayLog(duaCompleted = !currentLog.duaCompleted)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🤲", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "১০ মিনিট একাকী নিবিড় দো'আ সেশন",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "ইফতারের পূর্বে অথবা তাহাজ্জুদে আল্লাহর কাছে আরজি পেশ",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Icon(
                            imageVector = if (currentLog.duaCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (currentLog.duaCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action 4: Character & Speech Restraint
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth().clickable {
                        viewModel.updateDayLog(characterGoalDone = !currentLog.characterGoalDone)
                    }
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🌱", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "জিহ্বা ও ক্রোধ সংযম (সচেতন নীরবতা)",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "অনর্থক তর্ক, রাগ বা কটু কথা থেকে জিহ্বাকে সম্পূর্ণ মুক্ত রাখা",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Icon(
                            imageVector = if (currentLog.characterGoalDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (currentLog.characterGoalDone) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // 5. TODAY'S DAILY REFLECTION (ENGINE & JOURNAL ENTRY)
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.45f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("📝", fontSize = 20.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "আজকের আত্মদর্শন প্রশ্ন (Reflection Engine)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0369A1),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = dailyReflectionPrompt.themeBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFF0284C7).copy(alpha = 0.06f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "« ${dailyReflectionPrompt.questionBn} »",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = reflectionInput,
                    onValueChange = { reflectionInput = it },
                    placeholder = { Text("আপনার আন্তরিক অনুভূতি ও উপলব্ধি লিখুন (সম্পূর্ণ ব্যক্তিগত)...", fontSize = 12.sp, fontFamily = banglaFont) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF0284C7),
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.saveDailyReflection(selectedDay, dailyReflectionPrompt.questionBn, reflectionInput)
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Text("জার্নালে সংরক্ষণ করুন", fontSize = 12.sp, color = Color.White, fontFamily = banglaFont)
                    }
                }
            }
        }

        // 6. ADAPTIVE SUGGESTION BANNER (IF TRIGGERED BY PROGRESS)
        adaptiveSuggestion?.let { suggestion ->
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFB45309), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(suggestion.titleBn, fontWeight = FontWeight.Bold, color = Color(0xFF92400E), fontFamily = banglaFont)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(suggestion.messageBn, fontSize = 12.sp, color = Color(0xFF78350F), fontFamily = banglaFont)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = suggestion.onAcceptAction,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                    ) {
                        Text(suggestion.actionLabelBn, fontSize = 12.sp, color = Color.White, fontFamily = banglaFont)
                    }
                }
            }
        }

        // 7. QUICK NAVIGATION TILES
        Text(
            text = "রমাদান অপারেটিং সিস্টেমের দ্রুত অধ্যায়সমূহ:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = banglaFont
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f).clickable { onNavigateToLoop(RamadanLoopTab.BEFORE_RAMADAN) }
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📋", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("প্রস্তুতি সেন্টার", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f).clickable { onNavigateToLoop(RamadanLoopTab.DURING_RAMADAN) }
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✨", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("সিয়াম ও ক্বিয়াম", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f).clickable { onNavigateToLoop(RamadanLoopTab.DUA_AND_REFLECTION) }
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🤲", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("দো'আ ও ভাবনা", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f).clickable { onNavigateToLoop(RamadanLoopTab.LEARN_AND_VERIFY) }
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ম্যাসেজ যাচাই ও দান", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.weight(1f).clickable { onNavigateToLoop(RamadanLoopTab.AFTER_RAMADAN) }
                ) {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("🕊️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("ঈদ, শাওয়াল ও জার্নি", fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }
            }
        }
    }
}
