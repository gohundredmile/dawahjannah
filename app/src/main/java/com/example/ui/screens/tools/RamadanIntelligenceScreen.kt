package com.example.ui.screens.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.datasource.RamadanDataCatalog
import com.example.data.local.entity.RamadanDayLogEntity
import com.example.data.local.entity.RamadanMissedFastEntity
import com.example.data.model.AfterRamadanSection
import com.example.data.model.BeforeRamadanSection
import com.example.data.model.DuringRamadanSection
import com.example.data.model.FastingFiqhItem
import com.example.data.model.LaylatulQadrNightPlan
import com.example.data.model.RamadanChecklistItem
import com.example.data.model.RamadanDuaItem
import com.example.data.model.RamadanPhase
import com.example.data.model.RamadanSourceType
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.ui.viewmodel.RamadanOverallStats
import com.example.util.CalendarHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamadanIntelligenceScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RamadanIntelligenceViewModel = viewModel()
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val currentPhase by viewModel.currentPhase.collectAsState()
    val beforeSection by viewModel.beforeSection.collectAsState()
    val duringSection by viewModel.duringSection.collectAsState()
    val afterSection by viewModel.afterSection.collectAsState()

    val overallStats by viewModel.overallStats.collectAsState()
    val dayLogs by viewModel.dayLogs.collectAsState()
    val checklistStatuses by viewModel.checklistStatuses.collectAsState()
    val missedFasts by viewModel.missedFasts.collectAsState()
    val shawwalLogs by viewModel.shawwalLogs.collectAsState()
    val charityEntries by viewModel.charityEntries.collectAsState()
    val settings by viewModel.settings.collectAsState()

    // Modals & Dialog states
    var selectedDuaForDetail by remember { mutableStateOf<RamadanDuaItem?>(null) }
    var selectedFiqhForDetail by remember { mutableStateOf<FastingFiqhItem?>(null) }
    var selectedNightPlanForDetail by remember { mutableStateOf<LaylatulQadrNightPlan?>(null) }
    var selectedDayForEdit by remember { mutableStateOf<Int?>(null) }
    var showAddMissedFastDialog by remember { mutableStateOf(false) }
    var showAddCharityDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // TOP APP BAR
        Surface(
            tonalElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Ramadan Intelligence",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF059669).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "সিগনেচার সিস্টেম",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF059669),
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "পূর্ণাঙ্গ রমাদান ব্যবস্থা • পূর্বে, চলাকালীন ও পরবর্তী জীবন",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    Surface(
                        shape = CircleShape,
                        color = IslamicGold.copy(alpha = 0.15f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("🌙", fontSize = 18.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // PHASE SEGMENTED BUTTONS
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    RamadanPhase.entries.forEach { phase ->
                        val isSelected = currentPhase == phase
                        val bgColor by animateColorAsState(
                            targetValue = if (isSelected) Color(0xFF059669) else Color.Transparent,
                            animationSpec = tween(250), label = "phaseBg"
                        )
                        val textColor by animateColorAsState(
                            targetValue = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            animationSpec = tween(250), label = "phaseText"
                        )

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { viewModel.setPhase(phase) },
                            color = bgColor,
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = phase.iconEmoji,
                                    fontSize = 15.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = phase.titleBn,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = textColor,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        }

        // CONTENT BODY (LazyColumn)
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // HERO OVERVIEW STATS CARD
            item {
                RamadanHeroOverviewCard(
                    phase = currentPhase,
                    stats = overallStats,
                    settings = settings,
                    onOpenQuranTarget = {
                        viewModel.setPhase(RamadanPhase.BEFORE_RAMADAN)
                        viewModel.setBeforeSection(BeforeRamadanSection.QURAN_TARGET)
                    },
                    onOpenMissedRecovery = {
                        viewModel.setPhase(RamadanPhase.AFTER_RAMADAN)
                        viewModel.setAfterSection(AfterRamadanSection.MISSED_FASTS)
                    }
                )
            }

            // SUB-SECTION SELECTOR TABS
            item {
                when (currentPhase) {
                    RamadanPhase.BEFORE_RAMADAN -> {
                        SubSectionTabRow(
                            sections = BeforeRamadanSection.entries.map { it.titleBn to it.iconEmoji },
                            selectedIndex = BeforeRamadanSection.entries.indexOf(beforeSection),
                            onSelectIndex = { viewModel.setBeforeSection(BeforeRamadanSection.entries[it]) }
                        )
                    }
                    RamadanPhase.DURING_RAMADAN -> {
                        SubSectionTabRow(
                            sections = DuringRamadanSection.entries.map { it.titleBn to it.iconEmoji },
                            selectedIndex = DuringRamadanSection.entries.indexOf(duringSection),
                            onSelectIndex = { viewModel.setDuringSection(DuringRamadanSection.entries[it]) }
                        )
                    }
                    RamadanPhase.AFTER_RAMADAN -> {
                        SubSectionTabRow(
                            sections = AfterRamadanSection.entries.map { it.titleBn to it.iconEmoji },
                            selectedIndex = AfterRamadanSection.entries.indexOf(afterSection),
                            onSelectIndex = { viewModel.setAfterSection(AfterRamadanSection.entries[it]) }
                        )
                    }
                }
            }

            // =========================================================================
            // ACTIVE CONTENT ACCORDING TO PHASE & SUBSECTION
            // =========================================================================
            when (currentPhase) {
                RamadanPhase.BEFORE_RAMADAN -> {
                    when (beforeSection) {
                        BeforeRamadanSection.CHECKLIST -> {
                            item {
                                SectionHeaderTitle(
                                    titleBn = "প্রাক-রমাদান প্রস্তুতি চেকলিস্ট",
                                    subtitleBn = "সালাফদের আদর্শে রমাদানের বরকত অন্তরে ধারণের ৮টি মৌলিক প্রস্তুতি ধাপ।"
                                )
                            }
                            items(RamadanDataCatalog.preparationChecklist, key = { it.key }) { checkItem ->
                                val status = checklistStatuses.find { it.itemKey == checkItem.key }
                                val isChecked = status?.isChecked ?: false
                                PreparationChecklistCard(
                                    item = checkItem,
                                    isChecked = isChecked,
                                    onToggle = { viewModel.toggleChecklistItem(checkItem.key, isChecked) }
                                )
                            }
                        }

                        BeforeRamadanSection.QURAN_TARGET -> {
                            item {
                                QuranTargetPlannerCard(
                                    targetKhatms = settings?.quranTargetKhatms ?: 1,
                                    onUpdateTarget = { viewModel.updateQuranTargetKhatms(it) }
                                )
                            }
                        }

                        BeforeRamadanSection.FASTING_PREP -> {
                            item {
                                ShaBanPreparationCard(
                                    shaBanFastingDays = settings?.shaBanFastingCount ?: 0,
                                    onUpdateCount = { viewModel.updateShaBanFastingCount(it) }
                                )
                            }
                            item {
                                SectionHeaderTitle(
                                    titleBn = "ফিকহুস সিয়াম — আধুনিক মাসআলা ও সমাধান",
                                    subtitleBn = "ইনহেলার, ইনজেকশন, ড্রপ ইত্যাদি বিষয়ে সমসাময়িক ফিকহ একাডেমি ও চার মাযহাবের প্রামাণ্য বিশ্লেষণ।"
                                )
                            }
                            items(RamadanDataCatalog.fastingFiqhList, key = { it.id }) { fiqhItem ->
                                FastingFiqhCard(
                                    item = fiqhItem,
                                    onClick = { selectedFiqhForDetail = fiqhItem }
                                )
                            }
                        }

                        BeforeRamadanSection.CHARITY_PLAN -> {
                            item {
                                CharityBudgetPlannerCard(
                                    budget = settings?.charityTargetBudget ?: 5000.0,
                                    onUpdateBudget = { viewModel.updateCharityBudget(it) },
                                    onAddCharity = { showAddCharityDialog = true }
                                )
                            }
                        }
                    }
                }

                RamadanPhase.DURING_RAMADAN -> {
                    when (duringSection) {
                        DuringRamadanSection.FASTING_TRACKER -> {
                            item {
                                FastingSpiritualQualityBanner()
                            }
                            item {
                                SectionHeaderTitle(
                                    titleBn = "৩০ দিনের সিয়াম ট্র্যাকার",
                                    subtitleBn = "প্রতিদিনের রোযার অবস্থা, তারাবীহ ও আত্মিক পাহারা লগ করুন।"
                                )
                            }
                            // 30 Days Grid / Cards
                            items((1..30).toList()) { dayNum ->
                                val dayLog = dayLogs.find { it.dayNumber == dayNum }
                                RamadanDayCard(
                                    dayNumber = dayNum,
                                    dayLog = dayLog,
                                    onToggleFast = { viewModel.toggleFastingDay(dayNum, dayLog?.isFasted ?: false) },
                                    onToggleTaraweeh = { viewModel.toggleTaraweeh(dayNum, dayLog?.isTaraweeh ?: false, 8) },
                                    onEditDetail = { selectedDayForEdit = dayNum }
                                )
                            }
                        }

                        DuringRamadanSection.QURAN_KHATM -> {
                            item {
                                QuranKhatmProgressCard(
                                    totalPagesRead = overallStats.totalQuranPagesRead,
                                    targetPages = overallStats.targetQuranPages,
                                    progressPercent = overallStats.quranProgressPercent,
                                    onAddPages = { pages ->
                                        // add to day 1 or current day
                                        viewModel.addQuranPages(1, pages)
                                    }
                                )
                            }
                        }

                        DuringRamadanSection.TARAWEEH -> {
                            item {
                                TaraweehComprehensiveCard(
                                    taraweehCount = overallStats.taraweehNightsCount,
                                    dayLogs = dayLogs,
                                    onToggleNight = { dayNum, current ->
                                        viewModel.toggleTaraweeh(dayNum, current, 8)
                                    }
                                )
                            }
                        }

                        DuringRamadanSection.SUHOOR_IFTAR -> {
                            item {
                                SuhoorIftarProtocolCard(
                                    onOpenDua = { duaId ->
                                        selectedDuaForDetail = RamadanDataCatalog.ramadanDuas.find { it.id == duaId }
                                    }
                                )
                            }
                        }

                        DuringRamadanSection.DAILY_DUAS -> {
                            item {
                                SectionHeaderTitle(
                                    titleBn = "রমাদানের সহীহ মাসনুন দো'আ সংগ্রহ",
                                    subtitleBn = "চাঁদ দেখা, সাহরি, বিশুদ্ধ ইফতার ও লাইলাতুল কদরের শ্রেষ্ঠ প্রামাণ্য দো'আসমূহ।"
                                )
                            }
                            items(RamadanDataCatalog.ramadanDuas, key = { it.id }) { duaItem ->
                                RamadanDuaCard(
                                    item = duaItem,
                                    onClick = { selectedDuaForDetail = duaItem },
                                    onCopy = {
                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                        val text = "${duaItem.titleBn}\n\n${duaItem.arabicText}\n\nউচ্চারণ: ${duaItem.pronunciationBn}\n\nঅর্থ: ${duaItem.meaningBn}\n\nরেফারেন্স: ${duaItem.referenceBn}"
                                        clipboard.setPrimaryClip(ClipData.newPlainText("Ramadan Dua", text))
                                        Toast.makeText(context, "দো'আ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }

                        DuringRamadanSection.CHARITY_TRACKER -> {
                            item {
                                CharityTrackerSummaryCard(
                                    totalGiven = overallStats.totalCharityAmount,
                                    budget = overallStats.charityBudget,
                                    onAddCharity = { showAddCharityDialog = true }
                                )
                            }
                            items(charityEntries, key = { it.id }) { entry ->
                                CharityEntryRowCard(
                                    entry = entry,
                                    onDelete = { viewModel.deleteCharityEntry(entry.id) }
                                )
                            }
                        }

                        DuringRamadanSection.LAYLATUL_QADR -> {
                            item {
                                LaylatulQadrHeaderCard()
                            }
                            items(RamadanDataCatalog.laylatulQadrPlans, key = { it.nightNumber }) { plan ->
                                LaylatulQadrNightCard(
                                    plan = plan,
                                    onClick = { selectedNightPlanForDetail = plan }
                                )
                            }
                        }
                    }
                }

                RamadanPhase.AFTER_RAMADAN -> {
                    when (afterSection) {
                        AfterRamadanSection.MISSED_FASTS -> {
                            item {
                                MissedFastsHeaderCard(
                                    remaining = overallStats.missedFastsRemaining,
                                    recovered = overallStats.missedFastsRecovered,
                                    onAddMissed = { showAddMissedFastDialog = true }
                                )
                            }
                            if (missedFasts.isEmpty()) {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(20.dp),
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text("🕊️", fontSize = 28.sp)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = "কোনো কাযা রোযা যুক্ত করা হয়নি",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontFamily = banglaFont
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "অসুস্থতা বা ওজরের কারণে রোযা ছুটে গিয়ে থাকলে উপরের বাটনে চাপ দিয়ে যোগ করুন।",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                textAlign = TextAlign.Center,
                                                fontFamily = banglaFont
                                            )
                                        }
                                    }
                                }
                            }
                            items(missedFasts, key = { it.id }) { missedItem ->
                                MissedFastCard(
                                    item = missedItem,
                                    onRecoverOneDay = {
                                        val newRecovered = (missedItem.recoveredDays + 1).coerceAtMost(missedItem.totalMissedDays)
                                        viewModel.updateMissedFastRecord(
                                            missedItem.copy(
                                                recoveredDays = newRecovered,
                                                isFullyResolved = newRecovered >= missedItem.totalMissedDays
                                            )
                                        )
                                    },
                                    onDelete = { viewModel.deleteMissedFast(missedItem.id) }
                                )
                            }
                            item {
                                MissedFastFiqhCard()
                            }
                        }

                        AfterRamadanSection.SHAWWAL_FASTS -> {
                            item {
                                SixShawwalFastingCard(
                                    completedCount = overallStats.shawwalFastsCompleted,
                                    shawwalLogs = shawwalLogs,
                                    onToggleFast = { num, curr -> viewModel.toggleShawwalFast(num, curr) }
                                )
                            }
                        }

                        AfterRamadanSection.HABIT_CONTINUATION -> {
                            item {
                                HabitContinuationCard(
                                    settings = settings,
                                    onToggleHabit = { key, curr -> viewModel.toggleHabitSetting(key, curr) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // =========================================================================
    // BOTTOM SHEETS & DIALOGS
    // =========================================================================

    // DUA DETAIL BOTTOM SHEET
    selectedDuaForDetail?.let { dua ->
        ModalBottomSheet(
            onDismissRequest = { selectedDuaForDetail = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            RamadanDuaDetailSheetContent(
                dua = dua,
                onDismiss = { selectedDuaForDetail = null }
            )
        }
    }

    // FIQH DETAIL BOTTOM SHEET
    selectedFiqhForDetail?.let { fiqh ->
        ModalBottomSheet(
            onDismissRequest = { selectedFiqhForDetail = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fiqh.titleBn,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { selectedFiqhForDetail = null }) {
                        Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFD97706).copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = fiqh.summaryBn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFB45309),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "শরয়ী বিধান ও ঐকমত্য:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = fiqh.rulingsBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "স্বীকৃত ফিকহী মাযাহিব ও বিশ্লেষণ:",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0284C7),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = fiqh.scholarlyDifferenceBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "রেফারেন্স ও উৎস:",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = fiqh.referencesBn,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // LAYLATUL QADR NIGHT DETAIL SHEET
    selectedNightPlanForDetail?.let { plan ->
        ModalBottomSheet(
            onDismissRequest = { selectedNightPlanForDetail = null },
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = plan.titleBn,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = plan.hijriDateBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF059669),
                            fontFamily = banglaFont
                        )
                    }
                    IconButton(onClick = { selectedNightPlanForDetail = null }) {
                        Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Special Dua
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF059669).copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "এই রাতের বিশেষ মাগফিরাত দো'আ:",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669),
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = plan.specialDuaArabic,
                            style = MaterialTheme.typography.headlineSmall.copy(lineHeight = 36.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = arabicFont,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = plan.specialDuaMeaningBn,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "রাতে আমলের সুপারিশকৃত রূপরেখা:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(8.dp))

                plan.recommendedActions.forEachIndexed { idx, act ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF059669),
                            modifier = Modifier.size(20.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("${idx + 1}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = act,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "হাদীস রেফারেন্স: ${plan.hadithReferenceBn}",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF0284C7),
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // DAY EDIT DIALOG
    selectedDayForEdit?.let { dayNum ->
        val currentLog = dayLogs.find { it.dayNumber == dayNum }
        var fastedState by remember { mutableStateOf(currentLog?.isFasted ?: false) }
        var taraweehState by remember { mutableStateOf(currentLog?.isTaraweeh ?: false) }
        var rakahsState by remember { mutableIntStateOf(currentLog?.taraweehRakahs ?: 8) }
        var pagesState by remember { mutableStateOf((currentLog?.quranPagesRead ?: 0).toString()) }
        var charityState by remember { mutableStateOf((currentLog?.charityAmount ?: 0.0).toString()) }
        var noteState by remember { mutableStateOf(currentLog?.notes ?: "") }

        AlertDialog(
            onDismissRequest = { selectedDayForEdit = null },
            title = {
                Text(
                    text = "${dayNum}তম রমাদানের আমল বিবরণী",
                    fontFamily = banglaFont,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Fasted Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { fastedState = !fastedState },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (fastedState) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (fastedState) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (fastedState) "সিয়াম পালন সম্পন্ন হয়েছে • আলহামদুলিল্লাহ" else "রোযা রাখা হয়নি (কাযা বা ওজর)",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = banglaFont
                        )
                    }

                    // Taraweeh Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { taraweehState = !taraweehState },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (taraweehState) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                            contentDescription = null,
                            tint = if (taraweehState) Color(0xFF0284C7) else MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (taraweehState) "তারাবীহ / ক্বিয়াম আদায় হয়েছে" else "তারাবীহ আদায় করা হয়নি",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = banglaFont
                        )
                    }

                    if (taraweehState) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("রাকাত:", style = MaterialTheme.typography.bodySmall, fontFamily = banglaFont)
                            listOf(8, 20).forEach { r ->
                                FilterChip(
                                    selected = rakahsState == r,
                                    onClick = { rakahsState = r },
                                    label = { Text("$r রাকাত", fontFamily = banglaFont) }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = pagesState,
                        onValueChange = { pagesState = it },
                        label = { Text("আজ পঠিত কুরআন পৃষ্ঠা সংখ্যা", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = charityState,
                        onValueChange = { charityState = it },
                        label = { Text("আজকের সদাকাহ (টাকা)", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = noteState,
                        onValueChange = { noteState = it },
                        label = { Text("আত্মিক অনুভূতি ও দোয়া নোট", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val pages = pagesState.toIntOrNull() ?: 0
                        val charity = charityState.toDoubleOrNull() ?: 0.0
                        viewModel.updateDayLog(
                            dayNumber = dayNum,
                            isFasted = fastedState,
                            isTaraweeh = taraweehState,
                            taraweehRakahs = rakahsState,
                            quranPagesRead = pages,
                            charityAmount = charity,
                            notes = noteState
                        )
                        selectedDayForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Text("সংরক্ষণ করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedDayForEdit = null }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }

    // ADD MISSED FAST DIALOG
    if (showAddMissedFastDialog) {
        var reasonText by remember { mutableStateOf("অসুস্থতা") }
        var daysText by remember { mutableStateOf("1") }
        var notesText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddMissedFastDialog = false },
            title = { Text("কাযা রোযা হিসাব যুক্ত করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("কারণ নির্বাচন:", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf("অসুস্থতা", "সফর", "নারীদের ওজর", "অন্যান্য ওজর").forEach { r ->
                            FilterChip(
                                selected = reasonText == r,
                                onClick = { reasonText = r },
                                label = { Text(r, fontFamily = banglaFont) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = daysText,
                        onValueChange = { daysText = it },
                        label = { Text("ছুটে যাওয়া রোযার দিন সংখ্যা", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("অতিরিক্ত নোট (ঐচ্ছিক)", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val days = daysText.toIntOrNull() ?: 1
                        viewModel.addMissedFast(reasonText, days, notesText)
                        showAddMissedFastDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Text("যোগ করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddMissedFastDialog = false }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }

    // ADD CHARITY DIALOG
    if (showAddCharityDialog) {
        var titleText by remember { mutableStateOf("") }
        var amountText by remember { mutableStateOf("") }
        var categoryText by remember { mutableStateOf("সাধারণ সদাকাহ") }
        var notesText by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddCharityDialog = false },
            title = { Text("সদাকাহ বা দান এন্ট্রি করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = titleText,
                        onValueChange = { titleText = it },
                        label = { Text("খাত বা শিরোনাম (যেমন: ইফতার করানো)", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("টাকার পরিমাণ (৳)", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text("শ্রেণী:", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.horizontalScroll(rememberScrollState())
                    ) {
                        listOf("সাধারণ সদাকাহ", "ইফতার করানো", "যাকাত", "যাকাতুল ফিতর").forEach { c ->
                            FilterChip(
                                selected = categoryText == c,
                                onClick = { categoryText = c },
                                label = { Text(c, fontFamily = banglaFont) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = notesText,
                        onValueChange = { notesText = it },
                        label = { Text("নোট (ঐচ্ছিক)", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amount = amountText.toDoubleOrNull() ?: 0.0
                        if (titleText.isNotBlank() && amount > 0) {
                            viewModel.addCharityEntry(titleText, amount, categoryText, notesText)
                            showAddCharityDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                ) {
                    Text("সংরক্ষণ", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddCharityDialog = false }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }
}

// =========================================================================
// UI COMPONENTS
// =========================================================================

@Composable
fun RamadanHeroOverviewCard(
    phase: RamadanPhase,
    stats: RamadanOverallStats,
    settings: com.example.data.local.entity.RamadanSettingsEntity?,
    onOpenQuranTarget: () -> Unit,
    onOpenMissedRecovery: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF059669).copy(alpha = 0.12f),
                            IslamicGold.copy(alpha = 0.06f),
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
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF059669),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(phase.iconEmoji, fontSize = 14.sp)
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = phase.subtitleBn,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF059669),
                            fontFamily = banglaFont
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "১৪৪৬-১৪৪৭ হিজরি",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFB45309),
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Stats Metrics Grid
                when (phase) {
                    RamadanPhase.BEFORE_RAMADAN -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatMiniBox(
                                titleBn = "কুরআন টার্গেট",
                                valueBn = "${settings?.quranTargetKhatms ?: 1} খতম",
                                subBn = "দৈনিক ২০ পৃষ্ঠা",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatMiniBox(
                                titleBn = "শাবান রোযা",
                                valueBn = "${settings?.shaBanFastingCount ?: 0} দিন",
                                subBn = "সুন্নাহ অনুশীলন",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatMiniBox(
                                titleBn = "সদাকাহ বাজেট",
                                valueBn = "৳${(settings?.charityTargetBudget ?: 5000.0).toInt()}",
                                subBn = "পরিকল্পিত তহবিল",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    RamadanPhase.DURING_RAMADAN -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatMiniBox(
                                titleBn = "সিয়াম সম্পন্ন",
                                valueBn = "${stats.fastedDaysCount}/৩০ দিন",
                                subBn = "আলহামদুলিল্লাহ",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatMiniBox(
                                titleBn = "তারাবীহ ক্বিয়াম",
                                valueBn = "${stats.taraweehNightsCount} রাত",
                                subBn = "খুশু সহকারে",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatMiniBox(
                                titleBn = "কুরআন তিলাওয়াত",
                                valueBn = "${stats.totalQuranPagesRead} পৃষ্ঠা",
                                subBn = "${(stats.quranProgressPercent * 100).toInt()}% সম্পন্ন",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        LinearProgressIndicator(
                            progress = { stats.quranProgressPercent },
                            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                            color = Color(0xFF059669),
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    }

                    RamadanPhase.AFTER_RAMADAN -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            StatMiniBox(
                                titleBn = "কাযা বাকি",
                                valueBn = "${stats.missedFastsRemaining} দিন",
                                subBn = "রিকভারি চলমান",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatMiniBox(
                                titleBn = "শাওয়ালের রোযা",
                                valueBn = "${stats.shawwalFastsCompleted}/৬ দিন",
                                subBn = "সারা বছরের সওয়াব",
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            StatMiniBox(
                                titleBn = "মোট সদাকাহ",
                                valueBn = "৳${stats.totalCharityAmount.toInt()}",
                                subBn = "আল্লাহ কবুল করুন",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatMiniBox(
    titleBn: String,
    valueBn: String,
    subBn: String,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleBn,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = valueBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669),
                fontFamily = banglaFont,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subBn,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SubSectionTabRow(
    sections: List<Pair<String, String>>,
    selectedIndex: Int,
    onSelectIndex: (Int) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        sections.forEachIndexed { idx, item ->
            val isSelected = selectedIndex == idx
            FilterChip(
                selected = isSelected,
                onClick = { onSelectIndex(idx) },
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(item.second, fontSize = 13.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = item.first,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontFamily = banglaFont
                        )
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFF059669),
                    selectedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@Composable
fun SectionHeaderTitle(titleBn: String, subtitleBn: String) {
    val banglaFont = LocalBanglaFontFamily.current
    Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 2.dp)) {
        Text(
            text = titleBn,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = banglaFont
        )
        Text(
            text = subtitleBn,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = banglaFont
        )
    }
}

@Composable
fun PreparationChecklistCard(
    item: RamadanChecklistItem,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, if (isChecked) Color(0xFF059669).copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth().clickable { onToggle() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if (isChecked) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isChecked) Color(0xFF059669) else MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(24.dp).padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.titleBn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(item.sourceType.badgeColorHex).copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = item.sourceType.labelBn,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color(item.sourceType.badgeColorHex),
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.descriptionBn,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = item.referenceBn,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun QuranTargetPlannerCard(
    targetKhatms: Int,
    onUpdateTarget: (Int) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("📖", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "কুরআন খতম ক্যালকুলেটর ও পেসিং প্ল্যান",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সূরা আল-বাকারাহ ২:১৮৫ — রমাদান কুরআন নাজিলের বরকতময় মাস",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("আপনার খতম লক্ষ্য নির্বাচন করুন:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 2, 3).forEach { khatm ->
                    val isSel = targetKhatms == khatm
                    Button(
                        onClick = { onUpdateTarget(khatm) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSel) Color(0xFF059669) else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "$khatm খতম",
                            color = if (isSel) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Breakdown card
            val dailyPages = 20 * targetKhatms
            val pagesPerSalah = dailyPages / 5

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "সহজ পেসিং রুটিন ($targetKhatms খতম = দৈনিক $dailyPages পৃষ্ঠা):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    listOf(
                        "ফজরের পর: $pagesPerSalah পৃষ্ঠা",
                        "যোহরের পর: $pagesPerSalah পৃষ্ঠা",
                        "আসরের পর: $pagesPerSalah পৃষ্ঠা",
                        "মাগরিবের পর: $pagesPerSalah পৃষ্ঠা",
                        "তারাবীহর পর / সেহরিতে: $pagesPerSalah পৃষ্ঠা"
                    ).forEach { r ->
                        Text("• $r", style = MaterialTheme.typography.bodySmall, fontFamily = banglaFont)
                    }
                }
            }
        }
    }
}

@Composable
fun ShaBanPreparationCard(
    shaBanFastingDays: Int,
    onUpdateCount: (Int) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🌿", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "শাবান মাসে রোযার পূর্ব-অনুশীলন",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সহীহ আল-বুখারী ১৯৬৯ — রাসুল ﷺ শাবানে সর্বাধিক রোযা রাখতেন",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF0284C7),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "রমাদানের প্রথম কয়েকদিনের মাথা ব্যথা ও ক্লান্তি দূর করতে শাবান মাসে কিছু নফল রোযা রাখার অভ্যাস করুন।",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "শাবানে পালিত রোযা: $shaBanFastingDays দিন",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    OutlinedButton(
                        onClick = { if (shaBanFastingDays > 0) onUpdateCount(shaBanFastingDays - 1) },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("-১")
                    }
                    Button(
                        onClick = { onUpdateCount(shaBanFastingDays + 1) },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7))
                    ) {
                        Text("+১ দিন")
                    }
                }
            }
        }
    }
}

@Composable
fun FastingFiqhCard(
    item: FastingFiqhItem,
    onClick: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.titleBn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFD97706).copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "ফিকহী সমাধান",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFFB45309),
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.summaryBn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "বিস্তারিত শরয়ী দলীল ও চার মাযহাবের মতামত দেখুন →",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun CharityBudgetPlannerCard(
    budget: Double,
    onUpdateBudget: (Double) -> Unit,
    onAddCharity: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("💰", fontSize = 20.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "রমাদানের সদাকাহ ও যাকাত পরিকল্পনা",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সহীহ আল-বুখারী ৬ — রাসুল ﷺ রমাদানে প্রবাহিত বাতাসের চেয়েও দানশীল ছিলেন",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "রমাদানের মোট দান বাজেট: ৳${budget.toInt()}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                listOf(2000.0, 5000.0, 10000.0, 20000.0).forEach { b ->
                    FilterChip(
                        selected = budget == b,
                        onClick = { onUpdateBudget(b) },
                        label = { Text("৳${b.toInt()}", fontFamily = banglaFont) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAddCharity,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("সদাকাহ লগ বা নতুন পরিকল্পনা যোগ করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FastingSpiritualQualityBanner() {
    val banglaFont = LocalBanglaFontFamily.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF059669).copy(alpha = 0.1f),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.3f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🛡️", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "রোযার আত্মিক গুণমান পাহারা",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "সহীহ আল-বুখারী ১৯০৩: «যে ব্যক্তি মিথ্যা কথা ও মন্দ কাজ ছাড়ল না, তার পানাহার ত্যাগে আল্লাহর কোনো প্রয়োজন নেই»।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

@Composable
fun RamadanDayCard(
    dayNumber: Int,
    dayLog: RamadanDayLogEntity?,
    onToggleFast: () -> Unit,
    onToggleTaraweeh: () -> Unit,
    onEditDetail: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val isFasted = dayLog?.isFasted ?: false
    val isTaraweeh = dayLog?.isTaraweeh ?: false

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(
            1.dp,
            if (isFasted) Color(0xFF059669).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = if (isFasted) Color(0xFF059669) else MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "$dayNumber",
                        color = if (isFasted) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${dayNumber}তম রমাদান",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = if (isFasted) "✓ রোযা সম্পন্ন" else "○ রোযা বাকি",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isFasted) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = if (isTaraweeh) "✓ তারাবীহ (${dayLog?.taraweehRakahs ?: 8} রাকাত)" else "○ তারাবীহ বাকি",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isTaraweeh) Color(0xFF0284C7) else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }

            IconButton(onClick = onToggleFast) {
                Icon(
                    imageVector = if (isFasted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = "রোযা টগল",
                    tint = if (isFasted) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                )
            }

            IconButton(onClick = onEditDetail) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "সম্পাদনা",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun QuranKhatmProgressCard(
    totalPagesRead: Int,
    targetPages: Int,
    progressPercent: Float,
    onAddPages: (Int) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "রমাদানে কুরআন খতম অগ্রগতি",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "মোট পঠিত: $totalPagesRead / $targetPages পৃষ্ঠা (${(progressPercent * 100).toInt()}%)",
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFF059669),
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progressPercent },
                modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF059669),
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text("দ্রুত পৃষ্ঠা যোগ করুন:", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(1, 4, 10, 20).forEach { pages ->
                    Button(
                        onClick = { onAddPages(pages) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                    ) {
                        Text("+$pages", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun TaraweehComprehensiveCard(
    taraweehCount: Int,
    dayLogs: List<RamadanDayLogEntity>,
    onToggleNight: (Int, Boolean) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🕌", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "তারাবীহ ও ক্বিয়ামুল লাইল ট্র্যাকার",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সহীহ আল-বুখারী ২০০৯ — ঈমান ও সওয়াবের আশায় ক্বিয়াম করলে অতীত গুনাহ মাফ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF0284C7),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF0284C7).copy(alpha = 0.1f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "ফিকহী বৈচিত্র্য: তারাবীহ ৮ বা ২০ রাকাত—উভয়টিই সালাফ ও উলামাদের মাঝে অনুসৃত। রাকাত সংখ্যার চেয়ে খুশু, খুজু ও দীর্ঘ তিলাওয়াত অধিক তাৎপর্যপূর্ণ।",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF0369A1),
                    fontFamily = banglaFont,
                    modifier = Modifier.padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("মোট আদায়কৃত রাত: $taraweehCount রাত", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
        }
    }
}

@Composable
fun SuhoorIftarProtocolCard(
    onOpenDua: (String) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🥣", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "সাহরি ও ইফতারের সুন্নাহ আদব",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সুনান আত-তিরমিযী ৩৫৯৮ — ইফতারের সময় দোয়া কবুল হয়",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB45309),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            listOf(
                "সাহরি শেষ ওয়াক্তে খাওয়া সুন্নাত (সহীহ বুখারী ১৯২১)।",
                "সাহরিতে বরকত রয়েছে, অন্তত এক ঢোক পানি হলেও সাহরি খাওয়া (সহীহ মুসলিম ১০৯৫)।",
                "সূর্যাস্তের সাথে সাথে কালক্ষেপণ না করে দ্রুত ইফতার করা (সহীহ বুখারী ১৯৫৭)।",
                "তাজা খেজুর, শুকনো খেজুর বা পানি দিয়ে ইফতার শুরু করা (আবু দাউদ ২৩৫৫)।",
                "ইফতারের পূর্ব মুহূর্ত দো'আ কবুলের শ্রেষ্ঠ সময়—পরিবারসহ দো'আ করা।"
            ).forEach { item ->
                Text("• $item", style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp), fontFamily = banglaFont)
                Spacer(modifier = Modifier.height(3.dp))
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = { onOpenDua("dua_iftar_sahih") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Text("ইফতারের বিশুদ্ধ সুন্নাহ দো'আ পড়ুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RamadanDuaCard(
    item: RamadanDuaItem,
    onClick: () -> Unit,
    onCopy: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.titleBn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont,
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onCopy) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "কপি করুন",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.arabicText,
                style = MaterialTheme.typography.titleMedium.copy(lineHeight = 30.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = arabicFont,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.meaningBn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.referenceBn,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                color = Color(0xFF059669),
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun CharityTrackerSummaryCard(
    totalGiven: Double,
    budget: Double,
    onAddCharity: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = "রমাদানে দেওয়া মোট সদাকাহ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "৳${totalGiven.toInt()} / ৳${budget.toInt()}",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669),
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAddCharity,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("নতুন সদাকাহ এন্ট্রি করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun CharityEntryRowCard(
    entry: com.example.data.local.entity.RamadanCharityEntryEntity,
    onDelete: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                Text(
                    text = "${entry.category} • ${entry.date}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
            Text(
                text = "৳${entry.amount.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669),
                fontFamily = banglaFont
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "মুছুন", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
fun LaylatulQadrHeaderCard() {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🌟", fontSize = 24.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "লাইলাতুল কদর ও শেষ দশ রাতের প্ল্যানার",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সূরা আল-ক্বদর ৯৭:৩ — হাজার মাসের চেয়েও শ্রেষ্ঠ এক মহিমান্বিত রাত",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFB45309),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "সহীহ বুখারী ২০২৪: রাসুলুল্লাহ ﷺ শেষ দশকে কোমর বেঁধে নামতেন, সারা রাত জেগে ইবাদত করতেন এবং পরিবারের সবাইকে জাগাতেন। নিচে ৫টি বেজোড় রাতের বিশেষ পরিকল্পনা রয়েছে।",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun LaylatulQadrNightCard(
    plan: LaylatulQadrNightPlan,
    onClick: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = plan.titleBn,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF059669).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = "বেজোড় রাত",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF059669),
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = plan.hijriDateBn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "আমল পরিকল্পনা ও বিশেষ দো'আ দেখুন →",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF059669),
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun MissedFastsHeaderCard(
    remaining: Int,
    recovered: Int,
    onAddMissed: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⏳", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "কাযা রোযা রিকভারি ট্র্যাকার",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সূরা আল-বাকারাহ ২:১৮৫ — অন্য দিনগুলোতে এই সংখ্যা পূরণ করা ফরজ",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("অবশিষ্ট কাযা: $remaining দিন", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error, fontFamily = banglaFont)
                Text("আদায় হয়েছে: $recovered দিন", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF059669), fontFamily = banglaFont)
            }

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = onAddMissed,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("কাযা রোযা যুক্ত করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun MissedFastCard(
    item: RamadanMissedFastEntity,
    onRecoverOneDay: () -> Unit,
    onDelete: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "কারণ: ${item.reason}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                Text(
                    text = "মোট ${item.totalMissedDays} দিনের মধ্যে ${item.recoveredDays} দিন আদায় হয়েছে",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }

            Button(
                onClick = onRecoverOneDay,
                enabled = item.recoveredDays < item.totalMissedDays,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Text("কাযা +১ দিন", fontFamily = banglaFont, fontSize = 12.sp)
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "মুছুন", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
fun MissedFastFiqhCard() {
    val banglaFont = LocalBanglaFontFamily.current
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "কাযা বনাম ফিদইয়া — ফিকহী নীতিমালা:",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = RamadanDataCatalog.missedFastFiqhGuidance,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
    }
}

@Composable
fun SixShawwalFastingCard(
    completedCount: Int,
    shawwalLogs: List<com.example.data.local.entity.RamadanShawwalLogEntity>,
    onToggleFast: (Int, Boolean) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("✨", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "শাওয়ালের ৬টি রোযা প্ল্যানার",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সহীহ মুসলিম ১১৬৪ — সারা বছর রোযা রাখার সমান সওয়াব",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text("সম্পন্ন: $completedCount / ৬ দিন", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color(0xFF059669), fontFamily = banglaFont)
            Spacer(modifier = Modifier.height(10.dp))

            // 6 fast chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                (1..6).forEach { num ->
                    val log = shawwalLogs.find { it.fastNumber == num }
                    val done = log?.isCompleted ?: false
                    Surface(
                        shape = CircleShape,
                        color = if (done) Color(0xFF059669) else MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .clickable { onToggleFast(num, done) }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$num",
                                color = if (done) Color.White else MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "ফিকহী মাসআলা:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = RamadanDataCatalog.shawwalFastGuidance,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
fun HabitContinuationCard(
    settings: com.example.data.local.entity.RamadanSettingsEntity?,
    onToggleHabit: (String, Boolean) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("🌱", fontSize = 22.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "রমাদানের নূর ধরে রাখা — অভ্যাস ধারাবাহিকতা",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সালাফদের উক্তি: রমাদান কবুলের আলামত হলো এর পরেও নেক আমল অব্যাহত থাকা",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val habits = listOf(
                Triple("prayer", "৫ ওয়াক্ত সালাত জামাতে আদায়", settings?.habitPrayerOnTime ?: true),
                Triple("quran", "দৈনিক অন্তত ১ পৃষ্ঠা কুরআন তিলাওয়াত", settings?.habitDailyQuran ?: true),
                Triple("tahajjud", "রাতে বিতর বা ২ রাকাত তাহাজ্জুদ", settings?.habitTahajjudWitr ?: true),
                Triple("ayyam", "প্রতি মাসে আইয়ামে বীজের ৩ রোযা (১৩, ১৪, ১৫)", settings?.habitAyyamBeed ?: true),
                Triple("charity", "সাপ্তাহিক নিয়মিত ক্ষুদ্র সদাকাহ", settings?.habitWeeklyCharity ?: true)
            )

            habits.forEach { (key, title, status) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onToggleHabit(key, status) },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (status) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                        contentDescription = null,
                        tint = if (status) Color(0xFF059669) else MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

@Composable
fun RamadanDuaDetailSheetContent(
    dua: RamadanDuaItem,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = dua.titleBn,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Text(
                    text = "${dua.categoryBn} • ${dua.occasionBn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF059669),
                    fontFamily = banglaFont
                )
            }
            IconButton(onClick = onDismiss) {
                Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Arabic Container
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF059669).copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = dua.arabicText,
                    style = MaterialTheme.typography.headlineMedium.copy(lineHeight = 44.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = arabicFont,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = "উচ্চারণ:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF059669),
            fontFamily = banglaFont
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dua.pronunciationBn,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = banglaFont
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "অর্থ:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF059669),
            fontFamily = banglaFont
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dua.meaningBn,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = banglaFont
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "ফজিলত ও আমল:",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0284C7),
            fontFamily = banglaFont
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dua.virtuesBn,
            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = banglaFont
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "বিশুদ্ধ হাদীস রেফারেন্স: ${dua.referenceBn}",
            style = MaterialTheme.typography.labelSmall,
            color = Color(0xFF059669),
            fontFamily = banglaFont
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = {
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val text = "${dua.titleBn}\n\n${dua.arabicText}\n\nউচ্চারণ: ${dua.pronunciationBn}\n\nঅর্থ: ${dua.meaningBn}\n\nরেফারেন্স: ${dua.referenceBn}"
                    clipboard.setPrimaryClip(ClipData.newPlainText("Ramadan Dua", text))
                    Toast.makeText(context, "দো'আ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("কপি করুন", fontFamily = banglaFont)
            }

            Button(
                onClick = {
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(
                            Intent.EXTRA_TEXT,
                            "${dua.titleBn}\n\n${dua.arabicText}\n\nঅর্থ: ${dua.meaningBn}\n\nরেফারেন্স: ${dua.referenceBn}\n\n— দা'ওয়াহ টু জান্নাহ্ (Ramadan Intelligence)"
                        )
                        type = "text/plain"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "দো'আ শেয়ার করুন"))
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("শেয়ার করুন", fontFamily = banglaFont)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
