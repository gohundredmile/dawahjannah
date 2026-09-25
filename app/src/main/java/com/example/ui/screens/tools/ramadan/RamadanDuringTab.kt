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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.data.model.LaylatulQadrNightPlan
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.util.BanglaNumberUtils

@Composable
fun RamadanDuringTab(
    viewModel: RamadanIntelligenceViewModel,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val selectedDay by viewModel.selectedDay.collectAsState()
    val currentLog by viewModel.currentDayLog.collectAsState()
    val dayLogs by viewModel.dayLogs.collectAsState()
    val quranPace by viewModel.quranPaceStatus.collectAsState()
    val settings by viewModel.settings.collectAsState()

    var activeSubSection by remember { mutableStateOf("TIMELINE") } // TIMELINE, QURAN_KHATM, TARAWEEH, LAYLATUL_QADR
    var selectedNightPlan by remember { mutableStateOf(27) }

    val fastedCount = remember(dayLogs) { dayLogs.count { it.isFasted } }
    val taraweehCount = remember(dayLogs) { dayLogs.count { it.isTaraweeh } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // NAVIGATION PILLS FOR DURING RAMADAN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "TIMELINE" to "🌅 দৈনিক মোড",
                "QURAN_KHATM" to "📖 কুরআন খতম",
                "TARAWEEH" to "🕌 ক্বিয়াম ও তারাবীহ",
                "LAYLATUL_QADR" to "🌟 শেষ ১০ রাত ও কদর"
            ).forEach { (key, label) ->
                val isSelected = activeSubSection == key
                FilterChip(
                    selected = isSelected,
                    onClick = { activeSubSection = key },
                    label = { Text(label, fontSize = 11.sp, fontFamily = banglaFont) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF047857),
                        selectedLabelColor = Color.White
                    ),
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // SUB-SECTION 1: DAILY RAMADAN TIMELINE & NOTIFICATION INTENSITY
        if (activeSubSection == "TIMELINE") {
            // Notification Intensity Setting Card
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("নোটিফিকেশন তীব্রতা (Notification Intensity)", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, fontFamily = banglaFont)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            "MINIMAL" to "মৃদু (Minimal)",
                            "BALANCED" to "ভারসাম্য (Balanced)",
                            "FULL" to "পূর্ণাঙ্গ (Full)"
                        ).forEach { (mode, label) ->
                            val isSelected = (settings?.notificationIntensity ?: "BALANCED") == mode
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable { viewModel.setNotificationIntensity(mode) }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Daily 5 Guided Phases Timeline
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "রমাদানের ২৪ ঘণ্টার সুন্নাহ নির্দেশিকা",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "দিনের প্রতিটি মুহূর্তকে রহমত ও বরকতে রূপান্তর করুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    listOf(
                        Triple("🌅 সেহরি (Suhoor)", "সাহরি খাওয়া সুন্নাহ • প্রচুর পানি ও পুষ্টিকর খাবার গ্রহণ • রোযার নিয়ত অন্তরে দৃঢ় রাখা", "তাহাজ্জুদ ও সায়্যিদুল ইস্তিগফার"),
                        Triple("🕌 ফজর (Fajr)", "ফজরের জামা'আত • সূর্যোদয় পর্যন্ত কুরআন পাঠ ও সকালের মাসনুন আযকার", "ইশরাকের সালাত আদায়"),
                        Triple("☀️ দুপুর ও অপরাহ্ন (Daytime)", "সিয়ামের পবিত্রতা রক্ষা • কর্মক্ষেত্রে সততা ও হাসিমুখে কথা বলা • অনর্থক কথা পরিহার", "যোহরের পর সংক্ষিপ্ত ক্বাইলুলাহ"),
                        Triple("🌇 ইফতার (Iftar)", "ইফতারের ১৫ মিনিট পূর্বে দো'আয় মগ্ন হওয়া • খেজুর ও পানি দিয়ে দ্রুত ইফতার করা", "«যাহাবায যামাউ...» পাঠ"),
                        Triple("🌙 রাত (Night & Taraweeh)", "মাগরিবের পর আউয়াবীন • এশা ও তারাবীহর সালাত • বিতর ও ঘুম", "ক্বিয়ামুল লাইল ও কুরআন")
                    ).forEach { (phase, details, sunnah) ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(phase, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontFamily = banglaFont)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(details, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Text("আমল: $sunnah", fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.Medium, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 2: QURAN KHATM INTELLIGENCE
        if (activeSubSection == "QURAN_KHATM") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "কুরআন খতম ইন্টেলিজেন্স (Khatm Intelligence)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0369A1),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "শুধু গণনা নয়—গতি, ভারসাম্য ও তাদাব্বুরের বিশ্লেষণ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Text("📖", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Overview
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF0284C7).copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("মোট পঠিত পৃষ্ঠা:", fontSize = 12.sp, fontFamily = banglaFont)
                                Text("${BanglaNumberUtils.toBanglaDigits(quranPace.totalPagesRead)} / ${BanglaNumberUtils.toBanglaDigits(quranPace.targetTotalPages)} পৃষ্ঠা", fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            val progressFraction = (quranPace.totalPagesRead.toFloat() / quranPace.targetTotalPages.toFloat()).coerceIn(0f, 1f)
                            LinearProgressIndicator(
                                progress = { progressFraction },
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                                color = Color(0xFF0284C7),
                                trackColor = Color(0xFF0284C7).copy(alpha = 0.2f)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("পারা সম্পন্ন: ${BanglaNumberUtils.toBanglaDigits(quranPace.totalPagesRead / 20)} পারা", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                                Text("আজকের টার্গেট: ${BanglaNumberUtils.toBanglaDigits(quranPace.todayRecommendedPages)} পৃষ্ঠা", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0369A1), fontFamily = banglaFont)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Non-punitive Pace Guidance
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (quranPace.isBehind) Color(0xFFFEF3C7) else Color(0xFFECFDF5),
                        border = BorderStroke(1.dp, if (quranPace.isBehind) Color(0xFFF59E0B) else Color(0xFF10B981)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (quranPace.isBehind) "গতি সমন্বয় নসীহত (Gentle Pace Adjustment)" else "অগ্রগতি চমৎকার!",
                                fontWeight = FontWeight.Bold,
                                color = if (quranPace.isBehind) Color(0xFF92400E) else Color(0xFF065F46),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = quranPace.paceMessageBn,
                                fontSize = 12.sp,
                                color = if (quranPace.isBehind) Color(0xFF78350F) else Color(0xFF047857),
                                fontFamily = banglaFont
                            )

                            if (quranPace.isBehind) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { viewModel.setQuranPaceStrategy("GRADUAL") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("ধাপে ধাপে পূরণ", fontSize = 10.sp, fontFamily = banglaFont)
                                    }
                                    OutlinedButton(
                                        onClick = { viewModel.setQuranPaceStrategy("RESET") },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("আজকের থেকে নতুন", fontSize = 10.sp, fontFamily = banglaFont)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 3: TARAWEEH & NIGHT WORSHIP
        if (activeSubSection == "TARAWEEH") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "সালাতুত তারাবীহ ও রাতের ক্বিয়াম ট্র্যাকার",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "প্রতিযোগিতা নয়—ব্যক্তিগত নিষ্ঠা ও আল্লাহর সন্তুষ্টির জন্য ধারাবাহিকতা",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF059669).copy(alpha = 0.08f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("তারাবীহ রাত", fontSize = 11.sp, fontFamily = banglaFont)
                                Text("${BanglaNumberUtils.toBanglaDigits(taraweehCount)} রাত", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF059669), fontFamily = banglaFont)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF0284C7).copy(alpha = 0.08f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("সিয়াম পালন", fontSize = 11.sp, fontFamily = banglaFont)
                                Text("${BanglaNumberUtils.toBanglaDigits(fastedCount)} দিন", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF0284C7), fontFamily = banglaFont)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Today's Night Worship Checklist
                    Text("আজকের রাতের ইবাদত সম্পন্ন করুন:", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                    Spacer(modifier = Modifier.height(6.dp))

                    listOf(
                        Triple("তারাবীহর সালাত (${BanglaNumberUtils.toBanglaDigits(currentLog.taraweehRakahs)} রাকাত)", currentLog.isTaraweeh) {
                            viewModel.updateDayLog(isTaraweeh = !currentLog.isTaraweeh)
                        },
                        Triple("বিতরের সালাত একাগ্রতায় আদায়", currentLog.witrCompleted) {
                            viewModel.updateDayLog(witrCompleted = !currentLog.witrCompleted)
                        },
                        Triple("সেহরির পূর্বে তাহাজ্জুদের ক্বিয়াম", currentLog.tahajjudCompleted) {
                            viewModel.updateDayLog(tahajjudCompleted = !currentLog.tahajjudCompleted)
                        }
                    ).forEach { (title, isDone, onToggle) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onToggle() }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (isDone) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(title, style = MaterialTheme.typography.bodyMedium, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 4: LAYLATUL QADR INTELLIGENCE (THE LAST 10 NIGHTS)
        if (activeSubSection == "LAYLATUL_QADR") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.7f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "লাইলাতুল কদর ইন্টেলিজেন্স (শেষ ১০ রাত)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "হাজার মাসের চেয়েও শ্রেষ্ঠ এক রাতের তালাশ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Text("🌟", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Pre-Last 10 Nights Preparation Reminder
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = IslamicGold.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🌿 শেষ দশকের পূর্বপ্রস্তুতি:", fontWeight = FontWeight.Bold, color = Color(0xFFB45309), fontFamily = banglaFont)
                            Text("১. দুনিয়ার যাবতীয় অপ্রয়োজনীয় মিটিং ও কেনাকাটা ২০ রমাদানের আগেই শেষ করুন।\n২. পরিবারকে জাগ্রত রাখুন এবং নিভৃতে ই'তিকাফ বা দীর্ঘ তাহাজ্জুদের প্রস্তুতি নিন।\n৩. ক্ষমা প্রার্থনার দো'আটি মুখস্থ করে দিনরাত জপতে থাকুন।", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // The 5 Odd Nights Tabs
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(21, 23, 25, 27, 29).forEach { night ->
                            val isSelected = selectedNightPlan == night
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) Color(0xFF047857) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable { selectedNightPlan = night }
                            ) {
                                Text(
                                    text = "${BanglaNumberUtils.toBanglaDigits(night)}তম",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Details of Chosen Night
                    val currentNightPlan = remember(selectedNightPlan) {
                        RamadanDataCatalog.laylatulQadrPlans.find { it.nightNumber == selectedNightPlan }
                            ?: RamadanDataCatalog.laylatulQadrPlans.first()
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(currentNightPlan.titleBn, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, fontFamily = banglaFont)
                            Text(currentNightPlan.hijriDateBn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("আজকের রাতের বিশেষ দো'আ:", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = IslamicGold, fontFamily = banglaFont)
                            Text(
                                text = currentNightPlan.specialDuaArabic,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = arabicFont,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Text(currentNightPlan.specialDuaMeaningBn, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)

                            Spacer(modifier = Modifier.height(10.dp))

                            Text("প্রস্তাবিত আমলসমূহ:", fontWeight = FontWeight.Bold, fontSize = 11.sp, fontFamily = banglaFont)
                            for (action in currentNightPlan.recommendedActions) {
                                Text("• $action", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("দলীল: ${currentNightPlan.hadithReferenceBn}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7), fontFamily = banglaFont)
                        }
                    }
                }
            }
        }
    }
}
