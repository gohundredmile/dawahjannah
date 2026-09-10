package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FindReplace
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.IslamicLifeData
import com.example.data.model.IslamicLifeSection
import com.example.ui.components.DawahTopAppBar
import com.example.ui.screens.sub.AsmaulHusnaScreen
import com.example.ui.screens.sub.AyatDetectorAndSolverScreen
import com.example.ui.screens.sub.DuroodAmolFullScreen
import com.example.ui.screens.sub.DuroodScreen
import com.example.ui.screens.sub.HealthDuaScreen
import com.example.ui.screens.sub.IslamicLifeSectionDetailScreen
import com.example.ui.screens.sub.ScratchpadScreen
import com.example.ui.screens.sub.SettingsScreen
import com.example.ui.screens.sub.TasbihScreen
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen
import com.example.util.CalendarHelper

@Composable
fun MoreScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues
) {
    val currentSubScreen by viewModel.moreSubScreen.collectAsState()

    if (currentSubScreen != MoreSubScreen.MAIN) {
        if (currentSubScreen == MoreSubScreen.DUROOD_AMOL) {
            DuroodAmolFullScreen(
                viewModel = viewModel,
                onBack = { viewModel.navigateBackToMore() }
            )
        } else if (currentSubScreen == MoreSubScreen.ISLAMIC_LIFE_SECTION_DETAIL) {
            val section by viewModel.selectedIslamicSection.collectAsState()
            section?.let {
                IslamicLifeSectionDetailScreen(
                    section = it,
                    onBack = { viewModel.navigateBackToMore() }
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                DawahTopAppBar(
                    title = currentSubScreen.titleBn,
                    canNavigateBack = true,
                    onNavigateBack = { viewModel.navigateBackToMore() }
                )

                when (currentSubScreen) {
                    MoreSubScreen.SURAH_BAQARAH_LAST_2 -> SurahBaqarahLastTwoScreen(viewModel = viewModel)
                    MoreSubScreen.NAMES_OF_ALLAH -> AsmaulHusnaScreen(viewModel = viewModel)
                    MoreSubScreen.TASBIH -> TasbihScreen(viewModel = viewModel)
                    MoreSubScreen.DUROOD_ISTIGHFAR -> DuroodScreen(viewModel = viewModel)
                    MoreSubScreen.HEALTH_DUAS -> HealthDuaScreen(viewModel = viewModel)
                    MoreSubScreen.SCRATCHPAD -> ScratchpadScreen(viewModel = viewModel)
                    MoreSubScreen.SETTINGS -> SettingsScreen(viewModel = viewModel)
                    MoreSubScreen.AYAT_DETECTOR_SOLVER -> AyatDetectorAndSolverScreen(viewModel = viewModel)
                    else -> {}
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "ইসলামী জীবন",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "সুন্নাহসম্মত ইবাদত, বিধিবিধান ও বিশেষ আমলসমূহ",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = "দৈনন্দিন গাইড",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Featured: Dua Acceptance Times & Duas (New)
            item {
                MoreFeatureItem(
                    title = "★দোয়া কবুল হওয়ার সময় ও দোয়া★",
                    subtitle = "যে উপায়ে দোয়া অতি দ্রুত কবুল হয়, আযান-ইকামতের বিশেষ আমল ও সিজদায় পাঠযোগ্য মাসনূন দোয়া",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = IslamicGold,
                    badge = "বিশেষ আমল • নতুন",
                    onClick = {
                        IslamicLifeData.sections.find { it.id == "dua_acceptance_times" }?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Physical Health Dua & Healing Amol (New)
            item {
                MoreFeatureItem(
                    title = "★শারীরিক সুস্থ্যতার জন্য দোয়া★",
                    subtitle = "দেহের প্রত্যেক অঙ্গ সুস্থ রাখা, আইয়ূব (আঃ)-এর আরোগ্য আমল, মাথা ব্যথা নিরাময়, শিফার আসমাউল হুসনা ও রোগী দেখার দোয়া",
                    icon = Icons.Default.Healing,
                    iconTint = Color(0xFF0D9488),
                    badge = "সুস্থতার আমল • নতুন",
                    onClick = {
                        IslamicLifeData.sections.find { it.id == "physical_health_dua" }?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Durood Sharif Amol with Live Aurora
            item {
                MoreFeatureItem(
                    title = "দুরুদ শরীফের আমল",
                    subtitle = "দরূদে ইব্রাহীম, তাজ, নারিয়া, মাহী, ফুতুহাত ও সকল বরকতময় দরূদের সুবিশাল সংকলন",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = Color(0xFF059669),
                    badge = "লাইভ অরোরা ভিউ • নতুন",
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.DUROOD_AMOL) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Daily Dhikr, Tasbih Tahlil
            item {
                MoreFeatureItem(
                    title = "সারাদিনের যিকির, তাসবিহ তাহলিল - আরবি - বাংলা অর্থ",
                    subtitle = "দৈনন্দিন ৪৫টি মোবারক যিকির, তাসবিহ ও তাহলিল আরবি উচ্চারণ ও বাংলা অর্থসহ",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = IslamicGold,
                    badge = "৪৫টি যিকির • নতুন",
                    onClick = {
                        IslamicLifeData.sections.find { it.id == "daily_dhikr_tasbih_tahlil" }?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Highlight 1: Asmaul Husna with Virtues & Individual Hyperlinked Cards
            item {
                MoreFeatureItem(
                    title = "আসমাউল হুসনা (আল্লাহ্‌র ৯৯টি নাম) বাংলা অর্থ সহ ফজিলত",
                    subtitle = "অর্থ, বিশুদ্ধ উচ্চারণ, স্বতন্ত্র কার্ডে বিস্তারিত আমল ও ফজিলত",
                    icon = Icons.Default.Star,
                    iconTint = IslamicGold,
                    badge = "৯৯টি পবিত্র নাম",
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.NAMES_OF_ALLAH) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Highlight 2: Digital Tasbih
            item {
                MoreFeatureItem(
                    title = "ডিজিটাল তাসবীহ ও জিকির",
                    subtitle = "২৭টি নির্বাচিত বরকতময় তাসবিহ, বাংলা অর্থ, ফজিলত, হ্যাপটিক ভাইব্রেশন ও স্মার্ট কাউন্টার",
                    icon = Icons.Default.TouchApp,
                    iconTint = MaterialTheme.colorScheme.primary,
                    badge = "২৭টি তাসবিহ • স্মার্ট কাউন্টার",
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.TASBIH) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Highlight 3: Health Duas
            item {
                MoreFeatureItem(
                    title = "শারীরিক ও মানসিক রোগের দোয়া (শিফা)",
                    subtitle = "উদ্বেগ, বিষণ্নতা, ঋণমুক্তি, বদনজর ও সুরক্ষার রুকইয়াহ",
                    icon = Icons.Default.Healing,
                    iconTint = Color(0xFF059669),
                    badge = "রুকইয়াহ ও শিফা",
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.HEALTH_DUAS) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Highlight: Ayat Detector and Solver
            item {
                MoreFeatureItem(
                    title = "আয়াত ও হাদিস শুদ্ধিকরণ (Detector & Solver)",
                    subtitle = "ভাঙ্গা শব্দ, অনাকাঙ্ক্ষিত বর্ণ ও হরকত সনাক্তকরণ ও বিশুদ্ধ আমীরী আরবী ফন্টে সমাধান",
                    icon = Icons.Default.FindReplace,
                    iconTint = Color(0xFF0284C7),
                    badge = "স্মার্ট টুল • নতুন",
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.AYAT_DETECTOR_SOLVER) }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Highlight 5: Scratchpad Journal
            item {
                MoreFeatureItem(
                    title = "ব্যক্তিগত খাস দোয়া ও মুহাসাবা জার্নাল",
                    subtitle = "১০০% অফলাইনে ব্যক্তিগত দোয়া ও আত্মসমালোচনা সংরক্ষণ",
                    icon = Icons.Default.EditNote,
                    iconTint = Color(0xFF7C3AED),
                    badge = "ব্যক্তিগত নোট",
                    onClick = { viewModel.navigateToMoreSubScreen(MoreSubScreen.SCRATCHPAD) }
                )
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Sections Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "বিশেষ অধ্যায় ও অনুচ্ছেদসমূহ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Sections List: On tapping Heading opens content on full screen
            items(IslamicLifeData.sections, key = { it.id }) { section ->
                IslamicLifeSectionNavCard(
                    section = section,
                    onClick = {
                        viewModel.openIslamicLifeSection(section)
                    }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun MoreFeatureItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    badge: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = iconTint.copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                }

                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 17.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = iconTint.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = badge,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = iconTint,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun IslamicLifeSectionNavCard(
    section: IslamicLifeSection,
    onClick: () -> Unit
) {
    val sectionIcon = when (section.id) {
        "daily_dhikr_tasbih_tahlil" -> Icons.Default.AutoAwesome
        "salat_matters", "salam_before", "farz_after" -> Icons.Default.Mosque
        "fajr_maghrib", "fajr_maghrib_amols" -> Icons.Default.WbSunny
        "baqarah_last_two", "tawbah_last_two", "surah_baqarah_last_2" -> Icons.Default.MenuBook
        "sleep_duas", "night_awaken" -> Icons.Default.NightsStay
        "tahajjud_guide", "tahajjud_nafl" -> Icons.Default.NightsStay
        "prophet_panah_duas" -> Icons.Default.Healing
        "tawbah_istighfar" -> Icons.Default.Star
        else -> Icons.Default.BookmarkBorder
    }

    val iconTint = when (section.id) {
        "daily_dhikr_tasbih_tahlil" -> IslamicGold
        "tahajjud_guide" -> Color(0xFF2563EB)
        "baqarah_last_two", "surah_baqarah_last_2" -> IslamicGold
        "fajr_maghrib" -> Color(0xFFD97706)
        "tawbah_last_two" -> Color(0xFF059669)
        "prophet_panah_duas" -> Color(0xFF7C3AED)
        "tawbah_istighfar" -> Color(0xFF0D9488)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.16f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(46.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconTint.copy(alpha = 0.12f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = sectionIcon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = section.titleBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 22.sp
                )
                if (section.subtitleBn.isNotBlank()) {
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = section.subtitleBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 17.sp
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ) {
                        Text(
                            text = "${CalendarHelper.toBanglaNumber(section.items.size)} টি বিষয় • বিস্তারিত পড়ুন",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open Full Screen",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
