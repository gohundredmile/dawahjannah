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
import com.example.util.ExcludedIslamicLifeTopics
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
import com.example.ui.viewmodel.AppTab
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
        val dynamicSections by viewModel.islamicLifeSections.collectAsState()
        val visibleSections = remember(dynamicSections) {
            dynamicSections.filterNot { ExcludedIslamicLifeTopics.isExcluded(it.titleBn) }
        }
        val salamSec = viewModel.getIslamicLifeSection("salam_before")
        val farzSec = viewModel.getIslamicLifeSection("farz_after")

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

            // Featured: Ruqyah Special (★★★রুকিয়াহ - Ruqyah★★★)
            val ruqyahSec = viewModel.getIslamicLifeSection("ruqyah_shariah_special")
            item {
                val countBn = CalendarHelper.toBanglaNumber(ruqyahSec?.items?.size ?: 20)
                MoreFeatureItem(
                    title = "★★★রুকিয়াহ - Ruqyah★★★",
                    subtitle = "বদনজর, যাদু-টোনা, রোগব্যাধি ও শয়তানের অনিষ্ট থেকে সুরক্ষায় কুরআন ও সিহাহ সিত্তাহর সহীহ রুকইয়াহ ও দো'আ",
                    icon = Icons.Default.Healing,
                    iconTint = Color(0xFF059669),
                    badge = "${countBn}টি সহীহ রুকইয়াহ ও আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("ruqyah_shariah_special") ?: IslamicLifeData.sections.find { it.id == "ruqyah_shariah_special" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Salat and Dua Special (★★★সালাত ও দোয়া★★★)
            val salatDuaSec = viewModel.getIslamicLifeSection("salat_and_dua_special")
            item {
                val countBn = CalendarHelper.toBanglaNumber(salatDuaSec?.items?.size ?: 28)
                MoreFeatureItem(
                    title = "★★★সালাত ও দোয়া★★★",
                    subtitle = "সিজদায় দো'আ • বিত্‌রের কুনুতের দোয়া ও যিকির • জামা'আত ওয়াজিব ও কাযা বিধান • কিয়ামতে প্রথম হিসাব ও ফজরের ৭টি ফজিলত",
                    icon = Icons.Default.Mosque,
                    iconTint = Color(0xFF0D9488),
                    badge = "${countBn}টি প্রামাণ্য আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("salat_and_dua_special") ?: IslamicLifeData.sections.find { it.id == "salat_and_dua_special" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Surah Ali Imran 26-27 Special (★★★সূরা আল ইমরান ২৬-২৭ নং আয়াত পাঠের ফজিলত: রিজিক, সম্মান, ক্ষমতা ইজ্জত আল্লাহ দিতে পারেন★★★)
            val aliImranSec = viewModel.getIslamicLifeSection("ali_imran_rizq_honor")
            item {
                val countBn = CalendarHelper.toBanglaNumber(aliImranSec?.items?.size ?: 14)
                MoreFeatureItem(
                    title = "★★★সূরা আল ইমরান ২৬-২৭ নং আয়াত পাঠের ফজিলত: রিজিক, সম্মান, ক্ষমতা ইজ্জত আল্লাহ দিতে পারেন★★★",
                    subtitle = "রিজিক, সম্মান, ক্ষমতা লাভ ও পর্বতপ্রমাণ ঋণমুক্তি • সূরা আল ইমরান ২৬-২৭, সূরা হাশরের শেষ ৩ আয়াত ও সহীহ দো'আ",
                    icon = Icons.Default.MenuBook,
                    iconTint = IslamicGold,
                    badge = "${countBn}টি বিশেষ আয়াত ও দো'আ • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("ali_imran_rizq_honor") ?: IslamicLifeData.sections.find { it.id == "ali_imran_rizq_honor" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Morning-Evening Special Duas (★★★সকাল-সন্ধ্যার দোয়া★★★)
            val morningEveningSec = viewModel.getIslamicLifeSection("morning_evening_special")
            item {
                val countBn = CalendarHelper.toBanglaNumber(morningEveningSec?.items?.size ?: 24)
                MoreFeatureItem(
                    title = "★★★সকাল-সন্ধ্যার দোয়া★★★",
                    subtitle = "সকাল-সন্ধ্যায় (ফজর ও মাগরিবের পর) পড়ার শ্রেষ্ঠ সহীহ যিকর ও দু'আ • মূল আরবী, বাংলা উচ্চারণ, অর্থ ও প্রামাণ্য ফজিলত",
                    icon = Icons.Default.WbSunny,
                    iconTint = Color(0xFFD97706),
                    badge = "${countBn}টি দু'আ ও আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("morning_evening_special") ?: IslamicLifeData.sections.find { it.id == "morning_evening_special" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Sayyidul Istighfar (★★★সাইয়েদুল ইস্তেগফার আরবি★★★)
            val sayyidulSec = viewModel.getIslamicLifeSection("sayyidul_istighfar_special")
            item {
                val countBn = CalendarHelper.toBanglaNumber(sayyidulSec?.items?.size ?: 19)
                MoreFeatureItem(
                    title = "★★★সাইয়েদুল ইস্তেগফার আরবি★★★",
                    subtitle = "সাইয়েদুল ইস্তেগফার আরবি, বাংলা উচ্চারণ, অর্থ, ফজিলত ও আমল • সহীহ বুখারী ও সিহাহ সিত্তাহ ভিত্তিক বিশ্লেষণ",
                    icon = Icons.Default.Star,
                    iconTint = IslamicGold,
                    badge = "${countBn}টি পূর্ণ অধ্যায় • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("sayyidul_istighfar_special") ?: IslamicLifeData.sections.find { it.id == "sayyidul_istighfar_special" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Asmaul Husna Special (★★★আসমাউল হুসনা (আল্লাহর ৯৯টি পবিত্র নাম) বাংলা অর্থ সহ ফজিলত★★★)
            val asmaSec = viewModel.getIslamicLifeSection("asmaul_husna_special")
            item {
                val countBn = CalendarHelper.toBanglaNumber(asmaSec?.items?.size ?: 16)
                MoreFeatureItem(
                    title = "★★★আসমাউল হুসনা (আল্লাহর ৯৯টি পবিত্র নাম) বাংলা অর্থ সহ ফজিলত★★★",
                    subtitle = "পবিত্র কুরআন ও সিহাহ সিত্তাহ সহীহ হাদীস ভিত্তিক ৯৯টি পবিত্র নাম, অর্থ, বিশুদ্ধ ফজিলত ও প্রয়োজনভিত্তিক দো'আ",
                    icon = Icons.Default.Star,
                    iconTint = IslamicGold,
                    badge = "${countBn}টি প্রামাণ্য পাঠ • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("asmaul_husna_special") ?: IslamicLifeData.sections.find { it.id == "asmaul_husna_special" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        } ?: viewModel.navigateToMoreSubScreen(MoreSubScreen.NAMES_OF_ALLAH)
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Friday Special Duas & Amols (★★★শুক্রবারের বিশেষ দোয়া ও আমল★★★)
            val fridaySec = viewModel.getIslamicLifeSection("friday_special_duas")
            item {
                val countBn = CalendarHelper.toBanglaNumber(fridaySec?.items?.size ?: 16)
                MoreFeatureItem(
                    title = "★★★শুক্রবারের বিশেষ দোয়া ও আমল★★★",
                    subtitle = "জুমার দিনের মর্যাদা, প্রধান ৫ সুন্নাত, অকল্পনীয় সওয়াব, সূরা কাহাফ, সা'আতুল ইজাবাহ ও বিশেষ সহীহ দো'আ",
                    icon = Icons.Default.Mosque,
                    iconTint = Color(0xFF0D9488),
                    badge = "${countBn}টি আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("friday_special_duas") ?: IslamicLifeData.sections.find { it.id == "friday_special_duas" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Salam Before Duas (New - 56 Duas with Live Aurora & Tabs)
            item {
                val countBn = CalendarHelper.toBanglaNumber(salamSec?.items?.size ?: 56)
                MoreFeatureItem(
                    title = "সালাম ফিরানোর আগে সালাতের ভিতর পঠিতব্য দো'আ",
                    subtitle = "সালাতে সালামের পূর্বে ${countBn}টি মাসনূন ও সহীহ দো'আ — লাইভ অরোরা ভিউ, ফন্ট স্কেলিং ও কপি ফিচারসহ",
                    icon = Icons.Default.Mosque,
                    iconTint = Color(0xFF10B981),
                    badge = "${countBn}টি দো'আ • লাইভ অরোরা",
                    onClick = {
                        (viewModel.getIslamicLifeSection("salam_before") ?: IslamicLifeData.sections.find { it.id == "salam_before" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: 5 Waqt Salat Shesh e Duas (New)
            val fiveWaqtSec = viewModel.getIslamicLifeSection("five_waqt_after_salat")
            item {
                val countBn = CalendarHelper.toBanglaNumber(fiveWaqtSec?.items?.size ?: 7)
                MoreFeatureItem(
                    title = "★★★৫ ওয়াক্ত সালাত শেষে দো‘আ সমুহ★★★",
                    subtitle = "৫ ওয়াক্ত সালাতের পর সংকট মুক্তি, দো'আ কবুল, রিযিক বৃদ্ধি ও সাহায্য লাভের ${countBn}টি বিশেষ দো'আ ও আমল",
                    icon = Icons.Default.Mosque,
                    iconTint = Color(0xFF0D9488),
                    badge = "${countBn}টি দো'আ • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("five_waqt_after_salat") ?: IslamicLifeData.sections.find { it.id == "five_waqt_after_salat" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Farz After Duas (42 Duas with Live Aurora & Tabs)
            item {
                val countBn = CalendarHelper.toBanglaNumber(farzSec?.items?.size ?: 42)
                MoreFeatureItem(
                    title = "★★★ফরজ নামাজের পর, সালাম ফিরিয়ে দোয়া ★★★",
                    subtitle = "ফরজ সালাতের সালাম ফিরানোর পরবর্তী ${countBn}টি সহীহ ও মাসনূন দোয়াসমূহ, তাসবীহ, ইস্তিগফার ও রিযিকের আমল",
                    icon = Icons.Default.Mosque,
                    iconTint = Color(0xFF059669),
                    badge = "${countBn}টি দো'আ • লাইভ অরোরা",
                    onClick = {
                        (viewModel.getIslamicLifeSection("farz_after") ?: IslamicLifeData.sections.find { it.id == "farz_after" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Night Awakening Dua (★যদি রাত্রে ঘুম ভেঙ্গে যায় অতঃপর নিচের বাক্যগুলো পাঠ করবেন★)
            item {
                MoreFeatureItem(
                    title = "★যদি রাত্রে ঘুম ভেঙ্গে যায় অতঃপর নিচের বাক্যগুলো পাঠ করবেন★",
                    subtitle = "রাতে ঘুম ভাঙলে ও সেহরীর সময়ে দোয়া কবুল, ক্ষমা প্রার্থনা ও তাহাজ্জুদ সালাতের মুজাররব আমল",
                    icon = Icons.Default.NightsStay,
                    iconTint = Color(0xFF8B5CF6),
                    badge = "বিশেষ আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("night_awaken") ?: IslamicLifeData.sections.find { it.id == "night_awaken" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Isme Azam (★★★ইসমে আজম★★★)
            item {
                MoreFeatureItem(
                    title = "★★★ইসমে আজম★★★",
                    subtitle = "ইসমে আজম কি? হাদিসে বর্ণিত সঠিক ইসমে আজম ও ইসমে আজমের ফজিলত",
                    icon = Icons.Default.Star,
                    iconTint = IslamicGold,
                    badge = "শ্রেষ্ঠ দোয়া • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("isme_azam") ?: IslamicLifeData.sections.find { it.id == "isme_azam" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Dua Acceptance Times & Duas (New)
            item {
                MoreFeatureItem(
                    title = "★★★দোয়া কবুল হওয়ার সময় ও দোয়া★★★",
                    subtitle = "সহীহ হাদীস বর্ণিত বরকতময় সময়, স্থান, ইসমে আযম ও কবুলযোগ্য দো‘আ",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = IslamicGold,
                    badge = "বিশেষ আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("dua_acceptance_times") ?: IslamicLifeData.sections.find { it.id == "dua_acceptance_times" })?.let {
                            viewModel.openIslamicLifeSection(it)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Featured: Fajr Between & After Amols (★★★ফযর নামাজের মাঝে ও পরের আমল সমুহ★★★)
            item {
                MoreFeatureItem(
                    title = "★★★ফযর নামাজের মাঝে ও পরের আমল সমুহ★★★",
                    subtitle = "সুন্নত ও ফরজের মাঝে ~৪০ বার বিশেষ দোয়া এবং ফজর পরবর্তী মাসনূন যিকির ও ওজিফা",
                    icon = Icons.Default.WbSunny,
                    iconTint = Color(0xFFD97706),
                    badge = "ফজরের আমল • নতুন",
                    onClick = {
                        (viewModel.getIslamicLifeSection("fajr_between_and_after") ?: IslamicLifeData.sections.find { it.id == "fajr_between_and_after" })?.let {
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
            items(visibleSections, key = { it.id }) { section ->
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
        "ruqyah_shariah_special" -> Icons.Default.Healing
        "salat_and_dua_special" -> Icons.Default.Mosque
        "sayyidul_istighfar_special" -> Icons.Default.Star
        "asmaul_husna_special" -> Icons.Default.Star
        "five_waqt_after_salat" -> Icons.Default.Mosque
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
        "ruqyah_shariah_special" -> Color(0xFF059669)
        "salat_and_dua_special" -> Color(0xFF0D9488)
        "sayyidul_istighfar_special", "asmaul_husna_special" -> IslamicGold
        "five_waqt_after_salat" -> Color(0xFF0D9488)
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
