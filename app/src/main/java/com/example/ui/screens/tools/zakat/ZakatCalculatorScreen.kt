package com.example.ui.screens.tools.zakat

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ArabicFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily

/**
 * Revamped, comprehensive, evidence-based Zakat Calculator Screen adhering to MASTER PROMPT guidelines.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatCalculatorScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler { onNavigateBack() }
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current

    var selectedTab by remember { mutableIntStateOf(0) }
    var formState by remember { mutableStateOf(ZakatFormState()) }

    // Recompute result dynamically
    val calculationSummary by remember(formState) {
        derivedStateOf { ZakatCalculatorEngine.calculate(formState) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "স্মার্ট যাকাত ক্যালকুলেটর",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "প্রামাণ্য শরীয়াহ নিসাব, হাওল ও ফিকহ সমন্বয়",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            val summaryText = buildZakatClipboardText(calculationSummary)
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            clipboard.setPrimaryClip(ClipData.newPlainText("যাকাত হিসাব", summaryText))
                            Toast.makeText(context, "যাকাতের পূর্ণাঙ্গ হিসাব কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                        }
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "কপি করুন")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            // Live Quick Status Ribbon
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "নিট যাকাতযোগ্য সম্পদ: ${ZakatCalculatorEngine.formatMoney(calculationSummary.netZakatableWealth, calculationSummary.currency)}",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            fontFamily = banglaFont,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = if (calculationSummary.isZakatDue) {
                                "প্রদেয় যাকাত (২.৫%): ${ZakatCalculatorEngine.formatMoney(calculationSummary.zakatPayableAmount, calculationSummary.currency)}"
                            } else {
                                "যাকাত: প্রযোজ্য নয় (নিসাব বা হাওল অপূর্ণ)"
                            },
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont,
                            color = if (calculationSummary.isZakatDue) IslamicGreen else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Button(
                        onClick = { selectedTab = 1 },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "পূর্ণ বিবরণ ➔",
                            fontFamily = banglaFont,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Top Scrollable Category Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = IslamicGreen
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("📊 ইনপুট ও উইজার্ড", fontFamily = banglaFont, fontSize = 12.sp, fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("📜 ফলাফল ও সারসংক্ষেপ", fontFamily = banglaFont, fontSize = 12.sp, fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = { Text("📖 কুরআন ও হাদিসের দলিল", fontFamily = banglaFont, fontSize = 12.sp, fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = { Text("⚖️ মাযহাব ও ৮টি খাত", fontFamily = banglaFont, fontSize = 12.sp, fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Normal) }
                )
                Tab(
                    selected = selectedTab == 4,
                    onClick = { selectedTab = 4 },
                    text = { Text("🌾 উশর ও পশুসম্পদ", fontFamily = banglaFont, fontSize = 12.sp, fontWeight = if (selectedTab == 4) FontWeight.Bold else FontWeight.Normal) }
                )
            }

            when (selectedTab) {
                0 -> ZakatInputWizardTab(
                    formState = formState,
                    onFormChange = { formState = it },
                    onViewResults = { selectedTab = 1 },
                    summary = calculationSummary
                )
                1 -> ZakatResultsBreakdownTab(
                    summary = calculationSummary,
                    onEditInputs = { selectedTab = 0 }
                )
                2 -> ZakatQuranAndHadithEvidenceTab()
                3 -> ZakatFiqhAndRecipientsTab()
                4 -> ZakatAgriculturalAndLivestockTab()
            }
        }
    }
}

/**
 * Tab 0: Comprehensive Step-by-Step Input Wizard.
 */
@Composable
private fun ZakatInputWizardTab(
    formState: ZakatFormState,
    onFormChange: (ZakatFormState) -> Unit,
    onViewResults: () -> Unit,
    summary: ZakatCalculationSummary
) {
    val scrollState = rememberScrollState()
    val banglaFont = LocalBanglaFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome & Objective Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
            border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Balance, contentDescription = null, tint = IslamicGreen, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "শরীয়াহসম্মত প্রামাণ্য যাকাত নির্ণয়",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = IslamicGreen,
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "যাকাত কেবল 'মোট সম্পদ × ২.৫%' নয়। যাকাতযোগ্য সম্পদ চিহ্নিতকরণ, দেনা বিয়োগ, মাযহাবভেদে অলংকার ও শেয়ারের নিয়ম এবং স্বর্ণ-রৌপ্যের সঠিক নিসাব যাচাই করে নিখুঁত হিসাব করুন।",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
        }

        // STEP 1: হাওল ও যাকাতের বর্ষ
        ZakatSectionContainer(
            stepNumber = "১",
            titleBn = "যাকাত বর্ষ ও হাওল (এক চান্দ্র বছর)",
            icon = Icons.Default.CalendarMonth
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "এক চান্দ্র বছর (হাওল) অতিবাহিত হয়েছে?",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "নিসাব পরিমাণ সম্পদে চান্দ্র ৩৫৪ দিন পূর্ণ হলে যাকাত ওয়াজিব হয়।",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
                Switch(
                    checked = formState.isHawlCompleted,
                    onCheckedChange = { onFormChange(formState.copy(isHawlCompleted = it)) }
                )
            }
        }

        // STEP 2: মাযহাব ও ফিকহ পদ্ধতি
        ZakatSectionContainer(
            stepNumber = "২",
            titleBn = "মাযহাব ও গণনা পদ্ধতি নির্বাচন",
            icon = Icons.Default.AccountBalance
        ) {
            Text(
                text = "আপনার অনুসৃত ফিকহ অনুযায়ী অলংকার, শেয়ার ও ঋণের ক্ষেত্রে স্বয়ংক্রিয় বিধি প্রযোজ্য হবে:",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(8.dp))

            FiqhSchool.values().forEach { school ->
                val isSelected = formState.fiqhSchool == school
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onFormChange(formState.copy(fiqhSchool = school)) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) IslamicGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, if (isSelected) IslamicGreen else MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = isSelected,
                            onClick = { onFormChange(formState.copy(fiqhSchool = school)) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = school.titleBn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = school.subtitleBn,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // STEP 3: নিসাব স্ট্যান্ডার্ড ও বাজার দর
        ZakatSectionContainer(
            stepNumber = "৩",
            titleBn = "নিসাব নির্বাচন ও বর্তমান বাজারমূল্য",
            icon = Icons.Default.MonetizationOn
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Silver Nisab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onFormChange(formState.copy(nisabStandard = NisabStandard.SILVER)) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (formState.nisabStandard == NisabStandard.SILVER) IslamicGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, if (formState.nisabStandard == NisabStandard.SILVER) IslamicGreen else MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = formState.nisabStandard == NisabStandard.SILVER,
                                onClick = { onFormChange(formState.copy(nisabStandard = NisabStandard.SILVER)) }
                            )
                            Text("রৌপ্য নিসাব", fontWeight = FontWeight.Bold, fontFamily = banglaFont, fontSize = 13.sp)
                        }
                        Text("৫২.৫ তোলা / ৬১২.৩৬ গ্রাম", fontSize = 11.sp, fontFamily = banglaFont, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = ZakatCalculatorEngine.formatMoney(summary.silverNisabValue, formState.currency),
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen,
                            fontSize = 13.sp
                        )
                    }
                }

                // Gold Nisab
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onFormChange(formState.copy(nisabStandard = NisabStandard.GOLD)) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (formState.nisabStandard == NisabStandard.GOLD) IslamicGreen.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, if (formState.nisabStandard == NisabStandard.GOLD) IslamicGreen else MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            RadioButton(
                                selected = formState.nisabStandard == NisabStandard.GOLD,
                                onClick = { onFormChange(formState.copy(nisabStandard = NisabStandard.GOLD)) }
                            )
                            Text("স্বর্ণ নিসাব", fontWeight = FontWeight.Bold, fontFamily = banglaFont, fontSize = 13.sp)
                        }
                        Text("৭.৫ তোলা / ৮৭.৪৮ গ্রাম", fontSize = 11.sp, fontFamily = banglaFont, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = ZakatCalculatorEngine.formatMoney(summary.goldNisabValue, formState.currency),
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "বর্তমান বাজার দর (${formState.priceSourceDateBn}):\n• ২২ ক্যারেট স্বর্ণ: ৳${formState.goldPricePerGram22KBdt.toInt()}/গ্রাম\n• রৌপ্য: ৳${formState.silverPricePerGramBdt.toInt()}/গ্রাম",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )

            // Price Customizer Accordion
            var showPriceAdjuster by remember { mutableStateOf(false) }
            TextButton(
                onClick = { showPriceAdjuster = !showPriceAdjuster },
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(Icons.Default.Tune, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (showPriceAdjuster) "স্বর্ণ-রৌপ্যের বাজার দর লুকান" else "স্বর্ণ ও রৌপ্যের দর পরিবর্তন করুন",
                    fontFamily = banglaFont,
                    fontSize = 12.sp
                )
            }

            AnimatedVisibility(visible = showPriceAdjuster) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ZakatNumberField(
                        labelBn = "প্রতি গ্রাম ২২ ক্যারেট স্বর্ণের মূল্য (টাকা)",
                        value = formState.goldPricePerGram22KBdt,
                        onValueChange = { onFormChange(formState.copy(goldPricePerGram22KBdt = it, isCustomPriceUsed = true)) }
                    )
                    ZakatNumberField(
                        labelBn = "প্রতি গ্রাম রৌপ্যের মূল্য (টাকা)",
                        value = formState.silverPricePerGramBdt,
                        onValueChange = { onFormChange(formState.copy(silverPricePerGramBdt = it, isCustomPriceUsed = true)) }
                    )
                }
            }
        }

        // STEP 4: নগদ ও ব্যাংক ব্যালেন্স (সুদ আইসোলেশন সহ)
        ZakatSectionContainer(
            stepNumber = "৪",
            titleBn = "নগদ অর্থ ও ব্যাংক ব্যালেন্স",
            icon = Icons.Default.Wallet
        ) {
            ZakatNumberField(
                labelBn = "হাতে বা ঘরে রক্ষিত উদ্বৃত্ত নগদ অর্থ (টাকা)",
                value = formState.cashInHand,
                onValueChange = { onFormChange(formState.copy(cashInHand = it)) }
            )
            ZakatNumberField(
                labelBn = "ব্যাংক চলতি (Current) ও সেভিংস হালাল মূল জমা",
                value = formState.cashInBankCurrent,
                onValueChange = { onFormChange(formState.copy(cashInBankCurrent = it)) }
            )
            ZakatNumberField(
                labelBn = "ফিক্সড ডিপোজিট (FDR) / সঞ্চয়পত্র / ইসলামিক ব্যাংক জমা",
                value = formState.cashInIslamicBank,
                onValueChange = { onFormChange(formState.copy(cashInIslamicBank = it)) }
            )
            ZakatNumberField(
                labelBn = "মোবাইল ব্যাংকিং (বিকাশ/নগদ/রকেট) ও বৈদেশিক মুদ্রা সমমূল্য",
                value = formState.foreignCurrencyEquivalent,
                onValueChange = { onFormChange(formState.copy(foreignCurrencyEquivalent = it)) }
            )

            // SPECIAL WARNING: BANK INTEREST / RIBA
            Spacer(modifier = Modifier.height(4.dp))
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ব্যাংক সুদ (Interest/Riba) পৃথকীকরণ",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error,
                            fontFamily = banglaFont
                        )
                    }
                    Text(
                        text = "সুদ সম্পূর্ণ হারাম। এটি আপনার যাকাতযোগ্য ব্যক্তিগত সম্পদ নয়। সওয়াবের নিয়ত ছাড়া তা নিঃশর্তভাবে গরীবদের বিলিয়ে দিতে হবে।",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    ZakatNumberField(
                        labelBn = "ব্যাংক থেকে প্রাপ্ত অপরিশোধিত সুদের পরিমাণ (যাকাতমুক্ত রাখা হবে)",
                        value = formState.bankInterestRibaAmount,
                        onValueChange = { onFormChange(formState.copy(bankInterestRibaAmount = it)) }
                    )
                }
            }
        }

        // STEP 5: স্বর্ণ ও রৌপ্য (অলংকারের ফিকহ অপশন)
        ZakatSectionContainer(
            stepNumber = "৫",
            titleBn = "স্বর্ণ ও রৌপ্য (অলংকার ও সঞ্চয়)",
            icon = Icons.Default.Diamond
        ) {
            // Fiqh jewelry methodology selector
            Text(
                text = "ব্যক্তিগত ব্যবহারের স্বর্ণালংকারের বিধান:",
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont,
                fontSize = 12.sp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = formState.jewelryMethodology == JewelryMethodology.ALL_JEWELRY_ZAKATABLE,
                    onClick = { onFormChange(formState.copy(jewelryMethodology = JewelryMethodology.ALL_JEWELRY_ZAKATABLE)) }
                )
                Text(
                    text = "ব্যবহৃত অলংকারেও যাকাত অন্তর্ভুক্ত (হানাফী/সতর্কতামূলক)",
                    fontFamily = banglaFont,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onFormChange(formState.copy(jewelryMethodology = JewelryMethodology.ALL_JEWELRY_ZAKATABLE)) }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = formState.jewelryMethodology == JewelryMethodology.EXEMPT_CUSTOMARY_PERSONAL_USE,
                    onClick = { onFormChange(formState.copy(jewelryMethodology = JewelryMethodology.EXEMPT_CUSTOMARY_PERSONAL_USE)) }
                )
                Text(
                    text = "স্বাভাবিক ব্যবহারের অলংকার যাকাতমুক্ত (শাফেয়ী/মালেকী/হাম্বলী)",
                    fontFamily = banglaFont,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onFormChange(formState.copy(jewelryMethodology = JewelryMethodology.EXEMPT_CUSTOMARY_PERSONAL_USE)) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            ZakatNumberField(
                labelBn = "স্বর্ণালংকারের ওজন (গ্রাম)",
                value = formState.goldJewelryWeightGrams,
                onValueChange = { onFormChange(formState.copy(goldJewelryWeightGrams = it)) }
            )
            ZakatNumberField(
                labelBn = "সঞ্চিত স্বর্ণের বার / কয়েন / বিস্কুট (গ্রাম)",
                value = formState.goldBarsCoinsWeightGrams,
                onValueChange = { onFormChange(formState.copy(goldBarsCoinsWeightGrams = it)) }
            )
            ZakatNumberField(
                labelBn = "রৌপ্যালংকার ও রূপার পাত্র ইত্যাদির ওজন (গ্রাম)",
                value = formState.silverJewelryWeightGrams,
                onValueChange = { onFormChange(formState.copy(silverJewelryWeightGrams = it)) }
            )
            ZakatNumberField(
                labelBn = "সঞ্চিত রৌপ্য বার / কয়েনের ওজন (গ্রাম)",
                value = formState.silverBarsCoinsWeightGrams,
                onValueChange = { onFormChange(formState.copy(silverBarsCoinsWeightGrams = it)) }
            )
        }

        // STEP 6: শেয়ার, স্টক ও বিনিয়োগ
        ZakatSectionContainer(
            stepNumber = "৬",
            titleBn = "শেয়ার, স্টক ও ইসলামিক বিনিয়োগ",
            icon = Icons.Default.TrendingUp
        ) {
            ZakatNumberField(
                labelBn = "স্বল্পমেয়াদী ট্রেডিং শেয়ারের বাজারমূল্য (১০০% যাকাতযোগ্য)",
                value = formState.sharesForTradingMarketValue,
                onValueChange = { onFormChange(formState.copy(sharesForTradingMarketValue = it)) }
            )
            ZakatNumberField(
                labelBn = "দীর্ঘমেয়াদী ডিভিডেন্ড শেয়ারের বাজারমূল্য (২৫% প্রক্সি যাকাতযোগ্য)",
                value = formState.sharesForLongTermDividendsValue,
                onValueChange = { onFormChange(formState.copy(sharesForLongTermDividendsValue = it)) }
            )
            ZakatNumberField(
                labelBn = "সুকুক / ইসলামিক মিউচুয়াল ফান্ড ও যৌথ ব্যবসা মূলধন",
                value = formState.mutualFundsAndEtfsValue,
                onValueChange = { onFormChange(formState.copy(mutualFundsAndEtfsValue = it)) }
            )
        }

        // STEP 7: বাণিজ্যিক পণ্য (Trade Goods)
        ZakatSectionContainer(
            stepNumber = "৭",
            titleBn = "ব্যবসায়িক মজুদ পণ্য ও চলতি মূলধন",
            icon = Icons.Default.Store
        ) {
            Text(
                text = "⚠️ নির্দেশিকা: দোকানের আসবাবপত্র, কম্পিউটার, ডেলিভারি গাড়ি ও কারখানার মেশিনে কোনো যাকাত নেই। কেবল বিক্রয়ের জন্য রাখা পণ্যের পাইকারী বাজার দর লিখুন।",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = IslamicGreen),
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(8.dp))
            ZakatNumberField(
                labelBn = "বিক্রয়ের জন্য মজুদকৃত পণ্যের বর্তমান পাইকারী বাজারমূল্য",
                value = formState.tradeInventoryWholesaleValue,
                onValueChange = { onFormChange(formState.copy(tradeInventoryWholesaleValue = it)) }
            )
            ZakatNumberField(
                labelBn = "তৈরি পণ্য ও কাঁচামাল (উৎপাদনমুখী ব্যবসার ক্ষেত্রে)",
                value = formState.rawMaterialsForSaleValue,
                onValueChange = { onFormChange(formState.copy(rawMaterialsForSaleValue = it)) }
            )
            ZakatNumberField(
                labelBn = "ব্যবসার চলতি ক্যাশ ও ব্যাংক একাউন্ট ব্যালেন্স",
                value = formState.businessCashAndBankBalance,
                onValueChange = { onFormChange(formState.copy(businessCashAndBankBalance = it)) }
            )
        }

        // STEP 8: পাওনা অর্থ (Receivables)
        ZakatSectionContainer(
            stepNumber = "৮",
            titleBn = "অন্যের কাছে পাওনা অর্থ (Receivables)",
            icon = Icons.Default.Handshake
        ) {
            ZakatNumberField(
                labelBn = "নিশ্চিত আদায়যোগ্য পাওনা (চলতি বছরে অন্তর্ভুক্ত হবে)",
                value = formState.strongReceivablesLikelyToReceive,
                onValueChange = { onFormChange(formState.copy(strongReceivablesLikelyToReceive = it)) }
            )
            ZakatNumberField(
                labelBn = "সন্দেহজনক / অনদায়ী পাওনা (হস্তগত না হওয়া পর্যন্ত যাকাতমুক্ত)",
                value = formState.doubtfulReceivablesUnlikely,
                onValueChange = { onFormChange(formState.copy(doubtfulReceivablesUnlikely = it)) }
            )
        }

        // STEP 9: জমি, ক্রিপ্টো ও প্রভিডেন্ট ফান্ড
        ZakatSectionContainer(
            stepNumber = "৯",
            titleBn = "স্থাবর সম্পত্তি, ক্রিপ্টো ও প্রভিডেন্ট ফান্ড",
            icon = Icons.Default.Apartment
        ) {
            Text(
                text = "⚠️ নিজস্ব বাসস্থান ও স্বাভাবিক পারিবারিক গাড়িতে কোনো যাকাত নেই।",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(8.dp))
            ZakatNumberField(
                labelBn = "পুনর্বিক্রয়ের উদ্দেশ্যে ক্রয়কৃত জমি/ফ্ল্যাটের বর্তমান বাজারমূল্য",
                value = formState.propertyHeldForResaleMarketValue,
                onValueChange = { onFormChange(formState.copy(propertyHeldForResaleMarketValue = it)) }
            )
            ZakatNumberField(
                labelBn = "ভাড়াকৃত ভবন থেকে সঞ্চিত নিট ভাড়ার উদ্বৃত্ত",
                value = formState.netAccumulatedRentalIncomeSavings,
                onValueChange = { onFormChange(formState.copy(netAccumulatedRentalIncomeSavings = it)) }
            )
            ZakatNumberField(
                labelBn = "স্বেচ্ছাধীন বা উত্তোলনযোগ্য প্রভিডেন্ট ফান্ড",
                value = formState.accessibleWithdrawableProvidentFund,
                onValueChange = { onFormChange(formState.copy(accessibleWithdrawableProvidentFund = it)) }
            )
            ZakatNumberField(
                labelBn = "বাধ্যতামূলক পিএফ (যা চাকরি অবস্থায় উত্তোলন করা যায় না - যাকাতমুক্ত)",
                value = formState.nonAccessibleGovernmentProvidentFund,
                onValueChange = { onFormChange(formState.copy(nonAccessibleGovernmentProvidentFund = it)) }
            )
            ZakatNumberField(
                labelBn = "ট্রেডিংয়ের উদ্দেশ্যে রক্ষিত ক্রিপ্টোকারেন্সির বর্তমান মান",
                value = formState.cryptocurrencyTradingValue,
                onValueChange = { onFormChange(formState.copy(cryptocurrencyTradingValue = it)) }
            )
        }

        // STEP 10: ঋণ ও প্রদেয় দেনা (Liabilities & Deductions)
        ZakatSectionContainer(
            stepNumber = "১০",
            titleBn = "দেনা ও প্রদেয় দায় (বাদযোগ্য ঋণ)",
            icon = Icons.Default.MoneyOff
        ) {
            Text(
                text = "দীর্ঘমেয়াদী ঋণের ক্ষেত্রে আন্তর্জাতিক ফিকহ একাডেমি অনুযায়ী কেবল চলতি বছরের কিস্তি বাদ যাবে:",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = formState.debtDeductionMethodology == DebtDeductionMethodology.IMMEDIATE_DUE_ONLY,
                    onClick = { onFormChange(formState.copy(debtDeductionMethodology = DebtDeductionMethodology.IMMEDIATE_DUE_ONLY)) }
                )
                Text(
                    text = "কেবল তাৎক্ষণিক বকেয়া ও চলতি বছরের কিস্তি বাদ (ওআইসি ফিকহ একাডেমি)",
                    fontFamily = banglaFont,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onFormChange(formState.copy(debtDeductionMethodology = DebtDeductionMethodology.IMMEDIATE_DUE_ONLY)) }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = formState.debtDeductionMethodology == DebtDeductionMethodology.TOTAL_OUTSTANDING_DEBTS,
                    onClick = { onFormChange(formState.copy(debtDeductionMethodology = DebtDeductionMethodology.TOTAL_OUTSTANDING_DEBTS)) }
                )
                Text(
                    text = "মোট অবশিষ্ট ঋণের সম্পূর্ণ অংশ বাদ (ধ্রুপদী সাধারণ অভিমত)",
                    fontFamily = banglaFont,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable { onFormChange(formState.copy(debtDeductionMethodology = DebtDeductionMethodology.TOTAL_OUTSTANDING_DEBTS)) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            ZakatNumberField(
                labelBn = "তাত্ক্ষণিক প্রদেয় দেনা, কিস্তি, বিল ও কর্মচারীর বকেয়া বেতন",
                value = formState.immediateDueDebtsAndInstallments,
                onValueChange = { onFormChange(formState.copy(immediateDueDebtsAndInstallments = it)) }
            )
            ZakatNumberField(
                labelBn = "মোট অবশিষ্ট দীর্ঘমেয়াদী গৃহঋণ বা ব্যাংক লোন",
                value = formState.longTermTotalDebtOutstanding,
                onValueChange = { onFormChange(formState.copy(longTermTotalDebtOutstanding = it)) }
            )
        }

        // View Results Big Button
        Button(
            onClick = onViewResults,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen)
        ) {
            Icon(Icons.Default.Calculate, contentDescription = null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "হিসাবের সারসংক্ষেপ ও পূর্ণাঙ্গ ফলাফল দেখুন ➔",
                fontFamily = banglaFont,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

/**
 * Tab 1: Detailed Results and Itemized Audit Breakdown.
 */
@Composable
private fun ZakatResultsBreakdownTab(
    summary: ZakatCalculationSummary,
    onEditInputs: () -> Unit
) {
    val scrollState = rememberScrollState()
    val banglaFont = LocalBanglaFontFamily.current
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Master Hero Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (summary.isZakatDue) IslamicGreen else MaterialTheme.colorScheme.surfaceVariant
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "যাকাতুল মালের হিসাব",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (summary.isZakatDue) Color.White else MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0x30000000)
                    ) {
                        Text(
                            text = summary.fiqhSchool.titleBn.split(" ")[0],
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (summary.isZakatDue) {
                    Text(
                        text = "আপনার প্রদেয় যাকাতের পরিমাণ (২.৫%):",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = ZakatCalculatorEngine.formatMoney(summary.zakatPayableAmount, summary.currency),
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                        color = IslamicGoldLight
                    )
                    Text(
                        text = "আলহামদুলিল্লাহ! আপনার নিট সম্পদ নিসাব সীমা (${ZakatCalculatorEngine.formatMoney(summary.selectedNisabValue, summary.currency)}) স্পর্শ করেছে এবং যাকাত ওয়াজিব হয়েছে।",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                        color = Color.White.copy(alpha = 0.95f),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                } else if (!summary.isNisabReached) {
                    Text(
                        text = "যাকাত ফরজ নয়",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "আপনার নিট যাকাতযোগ্য সম্পদ (${ZakatCalculatorEngine.formatMoney(summary.netZakatableWealth, summary.currency)}) নির্বাচিত নিসাব সীমা (${ZakatCalculatorEngine.formatMoney(summary.selectedNisabValue, summary.currency)})-এ পৌঁছায়নি।",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                } else {
                    Text(
                        text = "হাওল অপূর্ণ থাকায় যাকাত ওয়াজিব নয়",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সম্পদ নিসাব অতিক্রম করলেও এক চান্দ্র বছর (হাওল) অতিবাহিত হওয়া পর্যন্ত যাকাত প্রদেয় হয় না।",
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }

        // Mathematical Formula Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Calculate, contentDescription = null, tint = IslamicGreen, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "গাণিতিক সূত্র ও হিসাবের ভিত্তি",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = summary.formulaExplanationBn,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont, lineHeight = 20.sp),
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        // Detected Methodology & Special Rules
        if (summary.detectedSpecialConditions.isNotEmpty()) {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Rule, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "প্রযুক্ত বিশেষ ফিকহ রুলিং ও সমন্বয়",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    summary.detectedSpecialConditions.forEach { condition ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("• ", color = IslamicGold, fontWeight = FontWeight.Bold)
                            Text(
                                text = condition,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // Itemized Audit Breakdown Rows
        Text(
            text = "সম্পদ ও দায়ের আইটেমাইজড অডিট বিবরণী:",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            fontFamily = banglaFont
        )

        summary.items.forEach { item ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, if (item.isDeduction) MaterialTheme.colorScheme.error.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.titleBn,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.weight(1f),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = if (item.isDeduction) "- ${ZakatCalculatorEngine.formatMoney(item.zakatableAmount, summary.currency)}" else ZakatCalculatorEngine.formatMoney(item.zakatableAmount, summary.currency),
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (item.isDeduction) MaterialTheme.colorScheme.error else IslamicGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.explanationBn,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "ফিকহ ভিত্তি: ${item.fiqhRuleBn}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp, color = IslamicGreen),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }
        }

        // Scholar Consultation Disclaimer
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "আলেম ও ফিকহবিদদের পরামর্শ গ্রহণের পরামর্শ",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "এই ক্যালকুলেটরটি আপনার প্রদত্ত তথ্যের ভিত্তিতে একটি প্রামাণ্য হিসাব প্রদান করে। জটিল অংশীদারি ব্যবসা, জটিল পারিবারিক উত্তরাধিকার, অনদায়ী ঋণ বা জটিল ট্রাস্ট থাকলে স্থানীয় যোগ্য মুফতির সাথে পরামর্শ করা উত্তম।",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, lineHeight = 16.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            }
        }

        // Action Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onEditInputs,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("ইনপুট পরিবর্তন", fontFamily = banglaFont, fontSize = 12.sp)
            }

            Button(
                onClick = {
                    val summaryText = buildZakatClipboardText(summary)
                    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    clipboard.setPrimaryClip(ClipData.newPlainText("যাকাত হিসাব", summaryText))
                    Toast.makeText(context, "পূর্ণ বিবরণ ক্লিপবোর্ডে কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGreen)
            ) {
                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("শেয়ার / কপি", fontFamily = banglaFont, fontSize = 12.sp)
            }
        }
    }
}

/**
 * Tab 2: Authentic Qur'an and Hadith Evidence.
 */
@Composable
private fun ZakatQuranAndHadithEvidenceTab() {
    val scrollState = rememberScrollState()
    val banglaFont = LocalBanglaFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Title Header
        Text(
            text = "পবিত্র কুরআন ও সহীহ সুন্নাহর প্রামাণ্য দলিল",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            fontFamily = banglaFont
        )

        // Quranic Verses
        Text(
            text = "পবিত্র কুরআনুল কারীম:",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IslamicGreen),
            fontFamily = banglaFont
        )

        ZakatKnowledgeBase.quranVerses.forEach { verse ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${verse.surahNameBn} (${verse.ayahNumber})",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = IslamicGreen,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = verse.surahNameEn,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = verse.arabicText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = ArabicFontFamily,
                            lineHeight = 30.sp,
                            textAlign = TextAlign.End
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "অনুবাদ: \"${verse.banglaTranslation}\"",
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = banglaFont, lineHeight = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "তাৎপর্য: ${verse.explanationBn}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Authentic Hadith References
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "সহীহ হাদীস সম্ভার:",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = IslamicGreen),
            fontFamily = banglaFont
        )

        ZakatKnowledgeBase.hadithReferences.forEach { hadith ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = hadith.subjectBn,
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                            color = IslamicGreen,
                            fontFamily = banglaFont
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IslamicGreen.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = hadith.authenticityGrade,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = IslamicGreen,
                                fontFamily = banglaFont
                            )
                        }
                    }
                    Text(
                        text = "${hadith.collectionName} • ${hadith.hadithNumber}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = hadith.narratorBn,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    hadith.arabicText?.let { ar ->
                        Text(
                            text = ar,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontFamily = ArabicFontFamily,
                                lineHeight = 26.sp,
                                textAlign = TextAlign.End
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Text(
                        text = hadith.banglaTranslation,
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = banglaFont, lineHeight = 20.sp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "ব্যাখ্যা ও ফিকহ: ${hadith.explanationBn}",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

/**
 * Tab 3: Fiqh Comparison and 8 Qur'anic Recipient Categories.
 */
@Composable
private fun ZakatFiqhAndRecipientsTab() {
    val scrollState = rememberScrollState()
    val banglaFont = LocalBanglaFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 8 Recipient Categories Title
        Text(
            text = "যাকাত ব্যয়ের ৮টি কুরআনিক খাত (সূরা আত-তাওবাহ ৯:৬০)",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            fontFamily = banglaFont
        )
        Text(
            text = "আল্লাহ তাআলা কুরআনে সরাসরি এই ৮টি খাত নির্ধারণ করে দিয়েছেন। এর বাইরে রাস্তাঘাট, হাসপাতাল নির্মাণ ইত্যাদিতে ফরজ যাকাতের অর্থ ব্যয় করা যায় না; হকদারের ব্যক্তিগত মালিকানা প্রতিষ্ঠা করা শর্ত।",
            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp, lineHeight = 18.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = banglaFont
        )

        ZakatKnowledgeBase.recipientCategories.forEach { category ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            modifier = Modifier.size(28.dp),
                            shape = CircleShape,
                            color = IslamicGreen.copy(alpha = 0.15f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${category.serial}",
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGreen,
                                    fontSize = 13.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = category.titleBn,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = category.arabicTitle,
                                style = MaterialTheme.typography.labelSmall.copy(fontFamily = ArabicFontFamily, color = IslamicGreen)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.descriptionBn,
                        style = MaterialTheme.typography.bodySmall.copy(fontFamily = banglaFont, lineHeight = 18.sp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "যোগ্যতার শর্ত: ${category.eligibilityNoteBn}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Fiqh Differences Table Card
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "স্বীকৃত চার মাযহাবের বৈধ মতভিন্নতা ও বিশ্লেষণ",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            fontFamily = banglaFont
        )

        ZakatKnowledgeBase.fiqhDifferencesSummary.forEach { (topic, desc) ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "• $topic",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium,
                        color = IslamicGreen,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = desc,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Bank Interest / Riba Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.2f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "ব্যাংক সুদ (Riba) ও সাধারণ সদকার পার্থক্য",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ZakatKnowledgeBase.ribaGuidanceBn,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ZakatKnowledgeBase.zakatVsSadaqahBn,
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    fontFamily = banglaFont
                )
            }
        }
    }
}

/**
 * Tab 4: Agricultural Ushur and Livestock Zakat Guidance.
 */
@Composable
private fun ZakatAgriculturalAndLivestockTab() {
    val scrollState = rememberScrollState()
    val banglaFont = LocalBanglaFontFamily.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Agricultural Produce (উশর)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Eco, contentDescription = null, tint = IslamicGreen, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "কৃষি ফসলের যাকাত (উশর / Ushur)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IslamicGreen,
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "পবিত্র কুরআনে আল্লাহ বলেন: 'আর তোমরা ফসল তোলার দিনে তার হক (উশর) প্রদান করো।' (সূরা আল-আন‘আম ৬:১৪১)\n\n" +
                            "ফসলের যাকাতকে বার্ষিক ২.৫% আর্থিক যাকাতের সাথে মেলানো যাবে না। এতে ১ বছর (হাওল) শর্ত নয়, বরং ফসল কাটার পরপরই প্রযোজ্য হয়।",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "১. বৃষ্টি বা প্রাকৃতিক জলাশয়ের ফসলে: ১০% (১/১০ অংশ)\n" +
                                    "২. সেচযন্ত্র ও খরচে উৎপাদিত ফসলে: ৫% (১/২০ অংশ)\n" +
                                    "৩. প্রাকৃতিক ও সেচ উভয়ের সংমিশ্রণে: ৭.৫%\n" +
                                    "৪. নিসাব: ৫ ওয়াসাক্ব (প্রায় ৬৫৩ কেজি শস্য)",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, lineHeight = 22.sp),
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        // Livestock Zakat (গবাদিপশুর যাকাত)
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Pets, contentDescription = null, tint = IslamicGreen, modifier = Modifier.size(22.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "গবাদি পশুসম্পদের যাকাত (Livestock)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = IslamicGreen,
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "গবাদি পশু (ছাগল, ভেড়া, গরু, মহিষ, উট) যদি বছরের অধিকাংশ সময় চারণভূমিতে চরে ঘাস খেয়ে লালিত-পালিত হয় (সায়েমাহ) এবং প্রজনন বা দুধের উদ্দেশ্যে রাখা হয়, তবে তাতে বিশেষ সংখ্যাভিত্তিক যাকাত প্রযোজ্য। এতে কোনো ২.৫% নগদ ফর্মুলা নেই।",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "• ছাগল/ভেড়া: ৪০ থেকে ১২০টিতে ১টি ছাগল।\n" +
                                    "• গরু/মহিষ: ৩০টিতে ১ বছর বয়সী ১টি বাছুর (তবী‘আ), ৪০টিতে ২ বছর বয়সী ১টি বাছুর।\n" +
                                    "• উট: ৫টি উটে ১টি ছাগল।\n" +
                                    "• বাণিজ্যিক ডেইরি/পোল্ট্রি ফার্ম: পশুসংখ্যা হিসেবে নয়, বিক্রয়যোগ্য পশু ও আয়ের ওপর সাধারণ ২.৫% বাণিজ্যিক যাকাত প্রযোজ্য।",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, lineHeight = 22.sp),
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

/**
 * Modular Form Section Container.
 */
@Composable
private fun ZakatSectionContainer(
    stepNumber: String,
    titleBn: String,
    icon: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = IslamicGreen.copy(alpha = 0.15f),
                    modifier = Modifier.size(30.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stepNumber,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen,
                            fontSize = 13.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = titleBn,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    fontFamily = banglaFont,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = IslamicGreen,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(14.dp))
            content()
        }
    }
}

/**
 * Number text field formatted for monetary values.
 */
@Composable
private fun ZakatNumberField(
    labelBn: String,
    value: Double,
    onValueChange: (Double) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var textValue by remember(value) {
        mutableStateOf(if (value <= 0.0) "" else if (value % 1.0 == 0.0) value.toLong().toString() else value.toString())
    }

    OutlinedTextField(
        value = textValue,
        onValueChange = { input ->
            val clean = input.filter { it.isDigit() || it == '.' }
            textValue = clean
            val parsed = clean.toDoubleOrNull() ?: 0.0
            onValueChange(parsed)
        },
        label = { Text(labelBn, fontFamily = banglaFont, fontSize = 12.sp) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = IslamicGreen,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
        )
    )
}

/**
 * Builds formatted text matching MASTER PROMPT requirement 45.
 */
private fun buildZakatClipboardText(summary: ZakatCalculationSummary): String {
    return buildString {
        append("যাকাতুল মালের হিসাব\n")
        append("${summary.calculationDateBn}\n")
        append("গণনার পদ্ধতি: ${summary.fiqhSchool.titleBn}\n")
        append("নিসাব স্ট্যান্ডার্ড: ${summary.nisabStandard.titleBn}\n")
        append("নিসাব মূল্য: ${ZakatCalculatorEngine.formatMoney(summary.selectedNisabValue, summary.currency)}\n\n")

        append("আপনার যাকাতযোগ্য সম্পদ:\n")
        summary.items.filterNot { it.isDeduction }.forEach { item ->
            append("• ${item.titleBn}: ${ZakatCalculatorEngine.formatMoney(item.zakatableAmount, summary.currency)}\n")
        }
        append("মোট যাকাতযোগ্য সম্পদ: ${ZakatCalculatorEngine.formatMoney(summary.totalZakatableAssets, summary.currency)}\n\n")

        append("বাদযোগ্য দায় ও দেনা:\n")
        summary.items.filter { it.isDeduction }.forEach { item ->
            append("• ${item.titleBn}: - ${ZakatCalculatorEngine.formatMoney(item.zakatableAmount, summary.currency)}\n")
        }
        append("মোট বাদযোগ্য দায়: - ${ZakatCalculatorEngine.formatMoney(summary.totalDeductibleLiabilities, summary.currency)}\n\n")

        append("নিট যাকাতযোগ্য সম্পদ: ${ZakatCalculatorEngine.formatMoney(summary.netZakatableWealth, summary.currency)}\n")
        append("যাকাতের হার: ২.৫% (১/৪০ অংশ)\n")
        if (summary.isZakatDue) {
            append("প্রদেয় যাকাত: ${ZakatCalculatorEngine.formatMoney(summary.zakatPayableAmount, summary.currency)}\n\n")
        } else {
            append("প্রদেয় যাকাত: ০ টাকা (নিসাব বা হাওল অপূর্ণ থাকায় যাকাত ফরজ নয়)\n\n")
        }

        append("ইসলামী ভিত্তি ও রেফারেন্স:\n")
        append("• কুরআন: সূরা আল-বাকারা ২:৪৩, সূরা আত-তাওবাহ ৯:৬০ ও ৯:১০৩\n")
        append("• হাদীস: সহীহ বুখারী ১৩৯৫, ১৪৫৪; সুনান আবু দাঊদ ১৫৭৩; তিরমিযী ৬২০\n\n")

        append("গুরুত্বপূর্ণ নোট ও ডিসক্লেইমার:\n")
        summary.importantNotesAndDisclaimers.forEach { note ->
            append("• $note\n")
        }
        append("\nদা'ওয়াহ টু জান্নাহ স্মার্ট যাকাত ক্যালকুলেটর")
    }
}
