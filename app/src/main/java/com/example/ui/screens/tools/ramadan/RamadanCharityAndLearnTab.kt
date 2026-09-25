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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
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
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.util.BanglaNumberUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RamadanCharityAndLearnTab(
    viewModel: RamadanIntelligenceViewModel,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val charityEntries by viewModel.charityEntries.collectAsState()
    val settings by viewModel.settings.collectAsState()
    val searchQuery by viewModel.verificationSearchQuery.collectAsState()
    val customVerifyResult by viewModel.customVerifyResult.collectAsState()

    var activeSubSection by remember { mutableStateOf("CHARITY") } // CHARITY, LEARN, VERIFY, FAMILY

    // Charity input states
    var showAddCharityDialog by remember { mutableStateOf(false) }
    var charityTitleInput by remember { mutableStateOf("") }
    var charityAmountInput by remember { mutableStateOf("") }
    var charityCategoryInput by remember { mutableStateOf("নফল সদাকাহ") }

    val totalCharityLogged = remember(charityEntries) {
        charityEntries.sumOf { it.amount }
    }
    val charityBudget = settings?.charityTargetBudget ?: 5000.0
    val charityProgress = if (charityBudget > 0) (totalCharityLogged / charityBudget).toFloat().coerceIn(0f, 1f) else 0f

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // NAVIGATION PILLS FOR CHARITY & LEARN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "CHARITY" to "💰 দান ও যাকাত",
                "LEARN" to "📖 ২-মিনিট পাঠ",
                "VERIFY" to "🛡️ ম্যাসেজ যাচাই",
                "FAMILY" to "👨‍👩‍👧‍👦 পরিবার মোড"
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

        // SUB-SECTION 1: CHARITY & ZAKAT PLANNER
        if (activeSubSection == "CHARITY") {
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
                                text = "সদাকাহ ও যাকাত প্ল্যানার (Charity Dashboard)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "রমাদানের প্রতিটি দিনে গোপন ও প্রকাশ্য দানের বরকত",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Text("💰", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress Overview
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF059669).copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("মোট প্রদত্ত দান:", fontSize = 12.sp, fontFamily = banglaFont)
                                Text("৳ ${BanglaNumberUtils.toBanglaDigits(totalCharityLogged.toInt())} / ৳ ${BanglaNumberUtils.toBanglaDigits(charityBudget.toInt())}", fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            LinearProgressIndicator(
                                progress = { charityProgress },
                                modifier = Modifier.fillMaxWidth().height(8.dp),
                                color = Color(0xFF059669),
                                trackColor = Color(0xFF059669).copy(alpha = 0.2f)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "রাসুলুল্লাহ ﷺ রমাদানে মুক্ত বাতাসের চেয়েও অধিক দানশীল হতেন (সহীহ বুখারী ৬)।",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Button to Log Charity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("দানের তালিকা:", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                        OutlinedButton(
                            onClick = { showAddCharityDialog = !showAddCharityDialog },
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("দান যুক্ত করুন", fontSize = 11.sp, fontFamily = banglaFont)
                        }
                    }

                    // Add Charity Form
                    AnimatedVisibility(visible = showAddCharityDialog) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            OutlinedTextField(
                                value = charityTitleInput,
                                onValueChange = { charityTitleInput = it },
                                placeholder = { Text("দানের বিবরণ (যেমন: এতিমখানায় ইফতার, পথচারীকে খাদ্য)", fontSize = 12.sp, fontFamily = banglaFont) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            OutlinedTextField(
                                value = charityAmountInput,
                                onValueChange = { charityAmountInput = it },
                                placeholder = { Text("পরিমাণ (টাকা)", fontSize = 12.sp, fontFamily = banglaFont) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    val amt = charityAmountInput.toDoubleOrNull() ?: 0.0
                                    if (charityTitleInput.isNotBlank() && amt > 0) {
                                        val dateStr = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(Date())
                                        viewModel.addCharityEntry(charityTitleInput, amt, charityCategoryInput, dateStr)
                                        charityTitleInput = ""
                                        charityAmountInput = ""
                                        showAddCharityDialog = false
                                    }
                                },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Text("সংরক্ষণ করুন", fontSize = 12.sp, fontFamily = banglaFont)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    charityEntries.take(5).forEach { entry ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(entry.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                                    Text("${entry.category} • ${entry.date}", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                                }
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("৳ ${BanglaNumberUtils.toBanglaDigits(entry.amount.toInt())}", fontWeight = FontWeight.Bold, color = Color(0xFF047857), fontFamily = banglaFont)
                                    IconButton(onClick = { viewModel.deleteCharityEntry(entry.id) }) {
                                        Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Budget-based practical charity ideas
                    Text("বাজেট অনুযায়ী বাস্তবসম্মত দানের ধারণা:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IslamicGold, fontFamily = banglaFont)
                    Spacer(modifier = Modifier.height(6.dp))
                    RamadanDataCatalog.charityIdeas.take(3).forEach { idea ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFD97706).copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text(idea.titleBn, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color(0xFF92400E), fontFamily = banglaFont)
                                    Text(idea.estimatedBudgetBn, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB45309), fontFamily = banglaFont)
                                }
                                Text(idea.descriptionBn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 2: 2-MINUTE BITE-SIZED RAMADAN KNOWLEDGE LESSONS
        if (activeSubSection == "LEARN") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "রমাদান জ্ঞান ও সংক্ষিপ্ত পাঠ (2-Min Lessons)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "সহীহ দলীলসহ ২-মিনিটের সংক্ষিপ্ত ও প্রামাণ্য ইসলামিক জ্ঞান",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    RamadanDataCatalog.knowledgeLessons.forEach { lesson ->
                        var isExpanded by remember { mutableStateOf(false) }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { isExpanded = !isExpanded }
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(lesson.titleBn, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF0369A1), fontFamily = banglaFont)
                                Text(lesson.summaryBn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)

                                AnimatedVisibility(visible = isExpanded) {
                                    Column(modifier = Modifier.padding(top = 10.dp)) {
                                        Text(lesson.coreAyahOrHadithArabic, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface, fontFamily = arabicFont)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("অনুবাদ: ${lesson.translationBn}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text("বাস্তব শিক্ষা: ${lesson.practicalTakeawayBn}", fontSize = 11.sp, color = Color(0xFF059669), fontWeight = FontWeight.Medium, fontFamily = banglaFont)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("ফিকহী নসীহত: ${lesson.scholarlyNoteBn}", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text("সূত্র: ${lesson.referenceBn}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7), fontFamily = banglaFont)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 3: RAMADAN VERIFICATION LAYER ("Verify This Ramadan Message")
        if (activeSubSection == "VERIFY") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFFDC2626).copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = Color(0xFFDC2626), modifier = Modifier.size(22.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "রমাদান ম্যাসেজ যাচাই (Verify This Message)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "সোশ্যাল মিডিয়ায় ছড়িয়ে পড়া অপপ্রচার ও জাল হাদীস থেকে বাঁচুন",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Search or Input Query
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = {
                            viewModel.setVerificationSearchQuery(it)
                            viewModel.verifyMessageText(it)
                        },
                        placeholder = { Text("কোনো বক্তব্য বা ম্যাসেজ লিখুন (যেমন: শুভেচ্ছা জানালে জাহান্নাম হারাম)...", fontSize = 12.sp, fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Verification Output if Searched
                    customVerifyResult?.let { result ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(result.verdictColorHex).copy(alpha = 0.08f),
                            border = BorderStroke(1.dp, Color(result.verdictColorHex).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("যাচাই ফলাফল:", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                                    Surface(shape = RoundedCornerShape(6.dp), color = Color(result.verdictColorHex)) {
                                        Text(result.verdictLabelBn, color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = banglaFont, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(result.authenticitySummaryBn, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(result.scholarlyExplanationBn, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text("সনদ ও সূত্র: ${result.hadithOrQuranSource}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0284C7), fontFamily = banglaFont)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Pre-analyzed Viral Claims List
                    Text("জনপ্রিয় বিতর্কিত বা প্রচলিত দাবিসমূহ:", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                    Spacer(modifier = Modifier.height(6.dp))

                    RamadanDataCatalog.verificationClaims.forEach { claim ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable {
                                viewModel.setVerificationSearchQuery(claim.viralClaimBn)
                                viewModel.verifyMessageText(claim.viralClaimBn)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(claim.viralClaimBn, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                    Text(claim.verdictLabelBn, fontSize = 10.sp, color = Color(claim.verdictColorHex), fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                                }
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 4: FAMILY RAMADAN MODE
        if (activeSubSection == "FAMILY") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF047857).copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "পারিবারিক রমাদান সার্কেল (Family Ramadan Circle)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "পরিবার ও সন্তানদের সাথে নিয়ে রমাদানের আধ্যাত্মিক আবহ সৃষ্টি করুন",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    listOf(
                        "👨‍👩‍👧‍👦 পরিবারের সম্মিলিত কুরআন খতম" to "পরিবারের সবাই প্রতিদিন অন্তত এক রুকু করে একসাথে বসে পাঠ করুন।",
                        "🥣 শিশুদের সেহরি ও ইফতারের উৎসাহ" to "ছোটদের আধাবেলা রোযার আনন্দ ও পুরস্কারের মাধ্যমে সিয়ামের প্রতি ভালোবাসা তৈরি।",
                        "🤲 যৌথ ইফতার পূর্ববর্তী দো'আ" to "ইফতারের ১০ মিনিট আগে পুরো পরিবার হাত তুলে মহান রবের দরবারে মোনাজাত করা।",
                        "🎁 ফিতরা ও ঈদের পোশাক প্রস্তুতি" to "অভাবী প্রতিবেশীর সন্তানদের জন্য ঈদের নতুন কাপড় উপহার দেওয়ার পারিবারিক সংকল্প।"
                    ).forEach { (activity, tip) ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(activity, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                                Text(tip, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }
    }
}
