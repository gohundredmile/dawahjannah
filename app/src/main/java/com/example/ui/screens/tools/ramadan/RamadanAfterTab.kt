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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.RamadanIntelligenceViewModel
import com.example.util.BanglaNumberUtils

@Composable
fun RamadanAfterTab(
    viewModel: RamadanIntelligenceViewModel,
    modifier: Modifier = Modifier
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val dayLogs by viewModel.dayLogs.collectAsState()
    val shawwalLogs by viewModel.shawwalLogs.collectAsState()
    val charityEntries by viewModel.charityEntries.collectAsState()
    val reflections by viewModel.reflections.collectAsState()
    val personalDuas by viewModel.personalDuas.collectAsState()

    var activeSubSection by remember { mutableStateOf("EID") } // EID, SHAWWAL, HABITS, MEMORY

    val totalFasted = remember(dayLogs) { dayLogs.count { it.isFasted } }
    val totalQuranPages = remember(dayLogs) { dayLogs.sumOf { it.quranPagesRead } }
    val totalCharityAmount = remember(charityEntries) { charityEntries.sumOf { it.amount } }
    val completedShawwalCount = remember(shawwalLogs) { shawwalLogs.count { it.isCompleted } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // NAVIGATION PILLS FOR AFTER RAMADAN
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                "EID" to "🎉 ঈদ প্রস্তুতি",
                "SHAWWAL" to "✨ শাওয়াল ৬ রোযা",
                "HABITS" to "🌱 ধারাবাহিক অভ্যাস",
                "MEMORY" to "📖 রমাদান জার্নি"
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

        // SUB-SECTION 1: EID TRANSITION MODE
        if (activeSubSection == "EID") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🎉", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "ঈদ ট্রানজিশন মোড (Eid Transition)",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "রমাদান যেন হঠাৎ থেমে না যায়—ঈদের প্রস্তুতি ও নূরের প্রবাহ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Zakat al-Fitr Reminder
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF047857).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("🌿 যাকাতুল ফিতর (সদাকাতুল ফিতর) আদায়:", fontWeight = FontWeight.Bold, color = Color(0xFF065F46), fontFamily = banglaFont)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "ঈদের নামাযের পূর্বেই মাথা পিছু ফিতরা আদায় করা ওয়াজিব। এটি রোজার অনাকাঙ্ক্ষিত ত্রুটি দূর করে এবং দরিদ্রদের মুখে ঈদের দিনে অন্ন নিশ্চিত করে (আবু দাউদ ১৬০৯)।",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Eid Takbirat Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("ঈদের তাকবীরাত (সুন্নাহ জিকির)", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IslamicGold, fontFamily = banglaFont)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "اللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، لاَ إِلَهَ إِلاَّ اللَّهُ، وَاللَّهُ أَكْبَرُ اللَّهُ أَكْبَرُ، وَلِلَّهِ الْحَمْدُ",
                                style = MaterialTheme.typography.titleMedium.copy(lineHeight = 30.sp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontFamily = arabicFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "আল্লাহু আকবার, আল্লাহু আকবার, লা ইলাহা ইল্লাল্লাহ, ওয়াল্লাহু আকবার, আল্লাহু আকবার, ওয়া লিল্লাহিল হামদ।",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Sunnahs of Eid Day
                    Text("ঈদের দিনের ৭টি মহিমান্বিত সুন্নাহ:", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                    Spacer(modifier = Modifier.height(6.dp))
                    listOf(
                        "১. ভোরে গোসল করা ও সুগন্ধি ব্যবহার করা",
                        "২. ঈদগাহে যাওয়ার পূর্বে কিছু মিষ্টান্ন (যেমন বেজোড় সংখ্যক খেজুর) আহার করা",
                        "৩. হেঁটে এক পথে ঈদগাহে যাওয়া এবং অন্য পথে প্রত্যাবর্তন করা",
                        "৪. ঈদগাহের পথে সশব্দে তাকবীর ধ্বনি দেওয়া",
                        "৫. পরস্পরকে শুভেচ্ছা জানানো: «তাক্বাব্বালাল্লাহু মিন্না ওয়া মিনকুম»"
                    ).forEach { sunnah ->
                        Text("• $sunnah", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                    }
                }
            }
        }

        // SUB-SECTION 2: SIX DAYS OF SHAWWAL PLANNER
        if (activeSubSection == "SHAWWAL") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF047857).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "শাওয়াল মাসের ৬ রোযা প্ল্যানার",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "সারা বছর রোযা রাখার পুণ্য অর্জনের সুবর্ণ সুযোগ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Text("✨", fontSize = 24.sp)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Hadith Evidence Banner
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF047857).copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "«যে ব্যক্তি রমাদানের রোযা রাখল, অতঃপর শাওয়াল মাসে ছয়টি রোযা রাখল, সে যেন সারা বছরই রোযা রাখল»",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF065F46),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("সহীহ মুসলিম ১১৬৪, সুনান আত-তিরমিযী ৭৫৯", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline, fontFamily = banglaFont)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Schedule Options (Consecutive vs Flexible)
                    Text("রোযা রাখার ফিকহী সুযোগ:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = IslamicGold, fontFamily = banglaFont)
                    Text("• একাধারে ৬ দিন রাখা জায়েয (ইমাম শাফেয়ী ও আহমদ রহ.)।\n• পুরো শাওয়াল মাসের মধ্যে সুবিধামতো পৃথকভাবে (যেমন সোম ও বৃহস্পতিবার বা আইয়ামে বীজ) রাখলেও পূর্ণ সাওয়াব অর্জিত হবে (ইমাম আবু হানিফা ও অধিকাংশ ফুকাহা)।", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)

                    Spacer(modifier = Modifier.height(14.dp))

                    // 6 Fasts Interactive Tracker
                    Text("আপনার ৬ রোযার অগ্রগতি (${BanglaNumberUtils.toBanglaDigits(completedShawwalCount)}/৬ সম্পন্ন):", fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        (1..6).forEach { num ->
                            val log = shawwalLogs.find { it.fastNumber == num }
                            val isDone = log?.isCompleted == true

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isDone) Color(0xFF047857) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.weight(1f).clickable {
                                    viewModel.toggleShawwalFast(num, !isDone)
                                }
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        imageVector = if (isDone) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                        contentDescription = null,
                                        tint = if (isDone) Color.White else MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "${BanglaNumberUtils.toBanglaDigits(num)}ম",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDone) Color.White else MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 3: HABIT CONTINUATION (SUSTAINABLE ROUTINES)
        if (activeSubSection == "HABITS") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🌱", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "রমাদান পরবর্তী অভ্যাস ধারাবাহিকতা",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "রমাদানের অভ্যাসগুলোকে সারাবছরের টেকসই রুটিনে রূপান্তর করুন",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    listOf(
                        Triple("📖 কুরআন তিলাওয়াত", "রমাদানে ১ পারা/দিন → সারাবছর অন্তত ১ পৃষ্ঠা/দিন", "প্রতিদিন ফজরের পর ৫ মিনিট কুরআন পড়া"),
                        Triple("💰 নিয়মিত সদাকাহ", "রমাদানের প্রতিদিনের দান → প্রতি জুমু'আয় নিয়মিত সদাকাহ", "অভাবী বা মসজিদে ক্ষুদ্র হলেও অবিরাম দান"),
                        Triple("🕌 রাতের সালাত", "তারাবীহ ও দীর্ঘ ক্বিয়াম → এশার পর বিতর বা ২ রাকাত তাহাজ্জুদ", "ঘুমের আগে বিতরের সালাত আদায়"),
                        Triple("🤲 দো'আর অভ্যাস", "ইফতারের পূর্বের দো'আ → আযান ও ইকামতের মধ্যবর্তী দো'আ", "প্রতিদিন আল্লাহর সাথে নিভৃতে কথা বলা"),
                        Triple("🌿 নফল রোযা", "রমাদানের ৩০ রোযা → প্রতি মাসে আইয়ামে বীজ (১৩, ১৪, ১৫)", "সারা বছর রোযার সাওয়াব নিশ্চিত করা")
                    ).forEach { (habit, transition, tip) ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(habit, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontFamily = banglaFont)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(transition, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface, fontFamily = banglaFont)
                                Text("টিপস: $tip", fontSize = 11.sp, color = Color(0xFF0284C7), fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }
        }

        // SUB-SECTION 4: RAMADAN MEMORY (PRIVATE JOURNEY SUMMARY - NOT A SCORE)
        if (activeSubSection == "MEMORY") {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.7f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    IslamicGold.copy(alpha = 0.12f),
                                    Color(0xFF047857).copy(alpha = 0.08f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("📖", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "আপনার ব্যক্তিগত রমাদান স্মৃতি (Your Ramadan Journey)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = "কোনো কৃত্রিম নম্বর বা স্কোর নয়—বরং কী শিখলেন ও অন্তরে ধারণ করলেন",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Closing Sincere Message Required by Philosophy
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFF047857).copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "« এখানে সংরক্ষিত হলো আপনি রমাদানে যা অনুশীলন করেছেন, শিখেছেন এবং উপলব্ধি করেছেন। মহান আল্লাহ আপনার প্রতিটি সিয়াম, ক্বিয়াম ও অশ্রু কবুল করুন। »",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF065F46),
                                fontWeight = FontWeight.Medium,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(14.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Grid
                        listOf(
                            Triple("🌙 সিয়াম পালনকৃত", "${BanglaNumberUtils.toBanglaDigits(totalFasted)} দিন রেকর্ড করা হয়েছে", "রোযার আত্মশুদ্ধি"),
                            Triple("📖 কুরআন অধ্যয়ন", "${BanglaNumberUtils.toBanglaDigits(totalQuranPages)} পৃষ্ঠা পাঠ সম্পন্ন", "${BanglaNumberUtils.toBanglaDigits(totalQuranPages / 20)} পারা সম্পন্ন"),
                            Triple("💰 সদাকাহ ও দান", "৳ ${BanglaNumberUtils.toBanglaDigits(totalCharityAmount.toInt())} মোট প্রদান", "${BanglaNumberUtils.toBanglaDigits(charityEntries.size)} বার দান রেকর্ড"),
                            Triple("🤲 ব্যক্তিগত দো'আ", "${BanglaNumberUtils.toBanglaDigits(personalDuas.size)}টি দো'আ সংরক্ষিত ও প্রার্থিত", "রব্বের সাথে সংযোগ"),
                            Triple("📝 আত্মদর্শন এন্ট্রি", "${BanglaNumberUtils.toBanglaDigits(reflections.size)}টি ভাবনার প্রতিফলন", "জীবনের শিক্ষা")
                        ).forEach { (title, stat, subtitle) ->
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp, fontFamily = banglaFont)
                                        Text(subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
                                    }
                                    Text(stat, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, fontFamily = banglaFont)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
