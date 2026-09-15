package com.example.ui.screens.sub

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.IslamicLifeCardItem
import com.example.data.model.IslamicLifeSection
import com.example.ui.components.DawahTopAppBar
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.util.CalendarHelper

@Composable
fun IslamicLifeSectionDetailScreen(
    section: IslamicLifeSection,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current

    val sectionIcon = when (section.id) {
        "morning_evening_special" -> Icons.Default.WbSunny
        "sayyidul_istighfar_special" -> Icons.Default.Star
        "asmaul_husna_special" -> Icons.Default.Star
        "five_waqt_after_salat", "friday_special_duas" -> Icons.Default.Mosque
        "dua_acceptance_times" -> Icons.Default.AutoAwesome
        "daily_dhikr_tasbih_tahlil" -> Icons.Default.AutoAwesome
        "salat_matters", "salam_before", "farz_after" -> Icons.Default.Mosque
        "fajr_maghrib", "fajr_maghrib_amols", "fajr_between_and_after" -> Icons.Default.WbSunny
        "baqarah_last_two", "tawbah_last_two", "surah_baqarah_last_2" -> Icons.Default.MenuBook
        "sleep_duas", "night_awaken" -> Icons.Default.NightsStay
        "tahajjud_guide", "tahajjud_nafl" -> Icons.Default.NightsStay
        "prophet_panah_duas" -> Icons.Default.Healing
        "tawbah_istighfar" -> Icons.Default.Star
        else -> Icons.Default.BookmarkBorder
    }

    val iconTint = when (section.id) {
        "ali_imran_rizq_honor" -> IslamicGold
        "morning_evening_special" -> Color(0xFFD97706)
        "sayyidul_istighfar_special" -> IslamicGold
        "asmaul_husna_special" -> IslamicGold
        "five_waqt_after_salat", "friday_special_duas" -> Color(0xFF0D9488)
        "dua_acceptance_times" -> IslamicGold
        "daily_dhikr_tasbih_tahlil" -> IslamicGold
        "tahajjud_guide" -> Color(0xFF2563EB)
        "baqarah_last_two", "surah_baqarah_last_2" -> IslamicGold
        "fajr_maghrib", "fajr_between_and_after" -> Color(0xFFD97706)
        "tawbah_last_two" -> Color(0xFF059669)
        "prophet_panah_duas" -> Color(0xFF7C3AED)
        "tawbah_istighfar" -> Color(0xFF0D9488)
        else -> MaterialTheme.colorScheme.primary
    }

    val isAliImranRizq = section.id == "ali_imran_rizq_honor"
    val isSalamBefore = section.id == "salam_before"
    val isFarzAfter = section.id == "farz_after"
    val isNightAwaken = section.id == "night_awaken"
    val isFajrBetweenAfter = section.id == "fajr_between_and_after"
    val isFiveWaqtAfter = section.id == "five_waqt_after_salat"
    val isFridaySpecial = section.id == "friday_special_duas"
    val isAsmaulHusna = section.id == "asmaul_husna_special"
    val isSayyidulIstighfar = section.id == "sayyidul_istighfar_special"
    val isMorningEvening = section.id == "morning_evening_special"
    val isDuaRichSection = isAliImranRizq || isSalamBefore || isFarzAfter || isNightAwaken || isFajrBetweenAfter || isFiveWaqtAfter || isFridaySpecial || isAsmaulHusna || isSayyidulIstighfar || isMorningEvening

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember(section.id) {
        mutableStateOf(
            when (section.id) {
                "ali_imran_rizq_honor" -> "সকল বিষয় ও দো'আ"
                "morning_evening_special" -> "সকল দো'আ ও আমল"
                "sayyidul_istighfar_special" -> "সকল অধ্যায়"
                "asmaul_husna_special" -> "সকল আমল ও নাম"
                "salam_before", "farz_after", "five_waqt_after_salat" -> "সকল দো'আ"
                "friday_special_duas" -> "সকল আমল ও দো'আ"
                "night_awaken" -> "সকল বাক্য ও আমল"
                else -> "সকল"
            }
        )
    }
    var showAurora by remember(section.id) { mutableStateOf(isDuaRichSection) }
    var fontScale by remember { mutableStateOf(1.0f) }

    val categoryFilters = remember(section.id) {
        when (section.id) {
            "ali_imran_rizq_honor" -> listOf("সকল বিষয় ও দো'আ", "সূরা আল ইমরান ও ঋণমুক্তি", "সূরা হাশরের শেষ ৩ আয়াত", "রিযিক ও ঋণমুক্তির সহীহ দো'আ", "তাহকীক ও তাওয়াক্কুল গাইড")
            "morning_evening_special" -> listOf("সকল দো'আ ও আমল", "ফরজ সালাত পরবর্তী", "হেফাজত ও নিরাপত্তা", "ক্ষমা ও জান্নাত লাভ", "তাওহীদ ও তাসবীহাত", "ফিকহ ও আদব গাইড")
            "sayyidul_istighfar_special" -> listOf("সকল অধ্যায়", "মূল দো'আ ও সনদ", "শ্রেষ্ঠত্ব ও শব্দার্থ", "কুরআন ও হাদিস", "আমলের নিয়ম ও রুটিন", "তুলনামূলক তালিকা ও FAQ")
            "asmaul_husna_special" -> listOf("সকল আমল ও নাম", "কুরআন, হাদিস ও গাইড", "প্রয়োজনভিত্তিক দো'আ", "নাম ১–২০", "নাম ২১–৫০", "নাম ৫১–৯৯", "রুটিন ও FAQ")
            "friday_special_duas" -> listOf("সকল আমল ও দো'আ", "মর্যাদা ও আদব", "নফল ও সুন্নাত সালাত", "কুরআনী সূরা ও দরূদ", "যিকির ও তাসবীহাত", "দো'আ কবুল ও খাস দো'আ", "তাহকীক, রুটিন ও FAQ")
            "five_waqt_after_salat" -> listOf("সকল দো'আ", "সংকট মুক্তি ও আশ্রয়", "দো'আ কবুল ও আসমান", "রিযিক বৃদ্ধি ও প্রাচুর্য", "হাসবুনাল্লাহ আমল")
            "night_awaken" -> listOf("সকল বাক্য ও আমল", "মূল দো'আ ও হাদিস", "একই সাথে পঠিতব্য দো'আ")
            "salam_before" -> listOf("সকল দো'আ", "সালাতে সালামের পূর্বে", "কোরআনের দো'আ", "সহীহ হাদিসের দো'আ")
            "farz_after" -> listOf("সকল দো'আ", "সালামের পর প্রাথমিক", "হিদায়াত ও দ্বীন", "পানাহ ও নিরাপত্তা", "রিযিক ও বরকত", "বিশেষ আমল ও ইস্তেগফার", "শেষ পরিণতি ও জান্নাত")
            "tawbah_istighfar" -> listOf("সকল", "মৌলিক ইস্তিগফার", "৫০টি ইস্তেগফার ও দু'আ", "১৬টি গুনাহ মোচনকারী আমল")
            "sleep_duas" -> listOf("সকল", "কুরআনী সূরা ও আয়াত", "মাসনূন ঘুমানোর দো'আ", "ঘুম ভাঙলে ও দুঃস্বপ্ন", "জাগ্রত হওয়ার দো'আ", "সুন্নাত রুটিন ও FAQ")
            else -> emptyList()
        }
    }

    val filteredItems = remember(section.items, searchQuery, selectedCategoryFilter) {
        val baseList = when (selectedCategoryFilter) {
            "সূরা আল ইমরান ও ঋণমুক্তি" -> section.items.filter { it.id in listOf("ai_guideline_tawakkul", "ai_main_verses_26_27", "ai_muadh_hadith_debt_dua", "ai_amal_rules_and_virtues") }
            "সূরা হাশরের শেষ ৩ আয়াত" -> section.items.filter { it.id in listOf("ai_hashr_taawwudh", "ai_hashr_ayah_22", "ai_hashr_ayah_23", "ai_hashr_ayah_24") }
            "রিযিক ও ঋণমুক্তির সহীহ দো'আ" -> section.items.filter { it.id in listOf("ai_ali_hadith_debt_dua", "ai_anxiety_debt_bukhari", "ai_durood_ibrahim_bridge", "ai_istighfar_surah_nuh", "ai_fajr_morning_rizq") }
            "তাহকীক ও তাওয়াক্কুল গাইড" -> section.items.filter { it.id in listOf("ai_guideline_tawakkul", "ai_amal_rules_and_virtues", "ai_tahqeeq_faq_guide") }
            "ফরজ সালাত পরবর্তী" -> section.items.filter { it.id in listOf("me_ayatul_kursi", "me_ajirni_minan_nar", "me_three_quls", "me_durood_sharif", "me_hasbiyallah", "me_subhanallahi_adada_khalqihi", "me_ilman_nafian") }
            "হেফাজত ও নিরাপত্তা" -> section.items.filter { it.id in listOf("me_ayatul_kursi", "me_three_quls", "me_bismillahilladhi", "me_audhu_bikalimatillah", "me_allahumma_afini_fi_badani", "me_allahumma_inni_as_alukal_afwa", "me_alimul_ghaybi", "me_shirk_protection", "me_hammi_wal_hazan") }
            "ক্ষমা ও জান্নাত লাভ" -> section.items.filter { it.id in listOf("me_ajirni_minan_nar", "me_durood_sharif", "me_radeetu_billah", "me_sayyidul_istighfar", "me_nasalukal_jannah", "me_allahumma_ushhiduka", "me_subhanallahi_wa_bihamdihi_hundred", "me_astaghfirullah_hundred") }
            "তাওহীদ ও তাসবীহাত" -> section.items.filter { it.id in listOf("me_hasbiyallah", "me_ya_hayyu_ya_qayyum", "me_subhanallahi_adada_khalqihi", "me_asbahna_wa_asbahal_mulk", "me_allahumma_bika_asbahna", "me_allahumma_ma_asbaha_bi", "me_la_ilaha_illallah_ten", "me_la_ilaha_illallah_hundred", "me_subhanallahi_wa_bihamdihi_hundred", "me_fitratil_islam", "me_khayra_hadhal_yawm") }
            "ফিকহ ও আদব গাইড" -> section.items.filter { it.id in listOf("me_fiqh_routine_guide") }
            "মূল দো'আ ও সনদ" -> section.items.filter { it.id in listOf("si_concept", "si_which_surah_clarification", "si_hadith_sanad", "si_full_dua_text", "si_virtues_jannah") }
            "শ্রেষ্ঠত্ব ও শব্দার্থ", "শব্দার্থ ও তাওবার রোকন" -> section.items.filter { it.id in listOf("si_why_sayyid", "si_word_by_word_analysis", "si_tawbah_pillars", "si_ahad_wad_tafseer") }
            "কুরআন ও হাদিস", "কুরআন ও হাদিসের ফযিলত" -> section.items.filter { it.id in listOf("si_quran_verses", "si_hadiths_forgiveness", "si_sahabi_profile") }
            "আমলের নিয়ম ও রুটিন", "রুটিন ও হিফজ গাইড" -> section.items.filter { it.id in listOf("si_practical_routine", "si_purity_rules_women", "si_memorization_parts", "si_schedule_table") }
            "তুলনামূলক তালিকা ও FAQ", "সতর্কতা ও অন্যান্য ইস্তেগফার" -> section.items.filter { it.id in listOf("si_comparative_table", "si_faq_answers", "si_warnings_fake_tawbah") }
            "কুরআন, হাদিস ও গাইড" -> section.items.filter { it.id in listOf("asma_quran_guide", "asma_hadith_jannat", "asma_virtues_4", "asma_memo_strategy") }
            "প্রয়োজনভিত্তিক দো'আ" -> section.items.filter { it.id.startsWith("asma_need_") }
            "নাম ১–২০" -> section.items.filter { it.id in listOf("asma_names_1_10", "asma_names_11_20") }
            "নাম ২১–৫০" -> section.items.filter { it.id in listOf("asma_names_21_35", "asma_names_36_50") }
            "নাম ৫১–৯৯" -> section.items.filter { it.id in listOf("asma_names_51_70", "asma_names_71_99") }
            "রুটিন ও FAQ" -> section.items.filter { it.id in listOf("asma_memo_strategy", "asma_faq") }
            "মর্যাদা ও আদব", "মর্যাদা ও সুন্নাত" -> section.items.filter { it.id in listOf("fa_quran_jumuah", "fa_virtue_concept", "fa_ghusl_miswak", "fa_early_to_masjid", "fa_sunnah_adab", "fa_walk_to_masjid", "fa_khutbah_adab", "fa_jumuah_death_virtue", "fa_adab_ihtiba_forbidden", "fa_adab_shift_place_drowsy", "fa_warning_fasting_friday_alone") }
            "নফল ও সুন্নাত সালাত" -> section.items.filter { it.id in listOf("fa_tahiyyatul_masjid_khutbah", "fa_salat_before_jumuah", "fa_salat_after_jumuah", "fa_salatut_tasbeeh") }
            "কুরআনী সূরা ও দরূদ" -> section.items.filter { it.id in listOf("fa_surah_kahaf", "fa_surah_fajr_jumuah", "fa_durood_sharif", "fa_surah_jumuah_reflection", "fa_surah_munafiqun_warning", "fa_surah_ala_ghashiyah", "fa_surah_three_quls") }
            "যিকির ও তাসবীহাত" -> section.items.filter { it.id in listOf("fa_dhikr_subhanallahi_wa_bihamdihi", "fa_dhikr_tahlil_hundred", "fa_dhikr_hawqalah", "fa_dhikr_sayyidul_tasbeeh") }
            "দো'আ কবুল ও খাস দো'আ" -> section.items.filter { it.id.startsWith("fa_dua_") || it.id == "fa_saatul_ijabah" }
            "তাহকীক, রুটিন ও FAQ", "সতর্কতা, রুটিন ও FAQ" -> section.items.filter { it.id in listOf("fa_warning_bidah_fabricated", "fa_checklist_sunnah_routine", "fa_faq_jumuah", "fa_advanced_tahqeeq_faq") }
            "সংকট মুক্তি ও আশ্রয়" -> section.items.filter { it.id in listOf("fwas_1", "fwas_3") }
            "দো'আ কবুল ও আসমান" -> section.items.filter { it.id in listOf("fwas_2") }
            "রিযিক বৃদ্ধি ও প্রাচুর্য" -> section.items.filter { it.id in listOf("fwas_4", "fwas_5", "fwas_6") }
            "হাসবুনাল্লাহ আমল" -> section.items.filter { it.id in listOf("fwas_7") }
            "মূল দো'আ ও হাদিস" -> section.items.filter { it.id == "na_1" }
            "একই সাথে পঠিতব্য দো'আ" -> section.items.filter { it.id != "na_1" }
            "সালাতে সালামের পূর্বে" -> section.items.filter { it.id.startsWith("sb_salam_") }
            "কোরআনের দো'আ" -> section.items.filter { it.id.startsWith("sb_quran_") }
            "সহীহ হাদিসের দো'আ" -> section.items.filter { it.id.startsWith("sb_hadith_") }
            "সালামের পর প্রাথমিক" -> section.items.filter { it.id.startsWith("fa_core_") }
            "হিদায়াত ও দ্বীন" -> section.items.filter { it.id.startsWith("fa_guidance_") }
            "পানাহ ও নিরাপত্তা" -> section.items.filter { it.id.startsWith("fa_refuge_") }
            "রিযিক ও বরকত" -> section.items.filter { it.id.startsWith("fa_rizq_") }
            "বিশেষ আমল ও ইস্তেগফার" -> section.items.filter { it.id.startsWith("fa_virtue_") }
            "শেষ পরিণতি ও জান্নাত" -> section.items.filter { it.id.startsWith("fa_end_") }
            "মৌলিক ইস্তিগফার" -> section.items.filter { it.id.startsWith("ti_") && !it.id.startsWith("ti_dua_") && !it.id.startsWith("ti_deed_") }
            "৫০টি ইস্তেগফার ও দু'আ" -> section.items.filter { it.id.startsWith("ti_dua_") }
            "১৬টি গুনাহ মোচনকারী আমল" -> section.items.filter { it.id.startsWith("ti_deed_") }
            "কুরআনী সূরা ও আয়াত" -> section.items.filter { it.id.startsWith("sd_quran_") }
            "মাসনূন ঘুমানোর দো'আ", "প্রধান মাসনূন দো'আ" -> section.items.filter { it.id.startsWith("sd_dua_") || it.id in listOf("sd_main_bukhari", "sd_body_protection_bukhari", "sd_qiyamah_azab_panah") }
            "ঘুম ভাঙলে ও দুঃস্বপ্ন", "অনিদ্রা ও দুঃস্বপ্ন" -> section.items.filter { it.id.startsWith("sd_night_") || it.id in listOf("sd_insomnia_sleeplessness", "sd_nightmare_actions", "sd_nightmare_and_fear") }
            "জাগ্রত হওয়ার দো'আ" -> section.items.filter { it.id.startsWith("sd_waking_up_") || it.id == "sd_waking_up_dua" }
            "সুন্নাত রুটিন ও FAQ" -> section.items.filter { it.id in listOf("sd_sunnah_deeds_10", "sd_summary_routine_13", "sd_children_sleep_protection", "sd_sleep_faq", "sd_nine_sunnah_routine") }
            else -> section.items
        }

        if (searchQuery.isBlank()) {
            baseList
        } else {
            val q = searchQuery.trim().lowercase()
            baseList.filter {
                it.titleBn.lowercase().contains(q) ||
                it.meaningBn.lowercase().contains(q) ||
                it.arabicText.contains(q) ||
                it.serialNumberBn.contains(q) ||
                it.fojilotBn.lowercase().contains(q) ||
                it.repetitionOrTimeBn.lowercase().contains(q)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showAurora) {
            LiveAuroraWallpaperBackground()
        }

        Column(modifier = Modifier.fillMaxSize()) {
            DawahTopAppBar(
                title = section.titleBn,
                canNavigateBack = true,
                onNavigateBack = onBack,
                actions = {
                    // Font Scale: Decrease (A-)
                    IconButton(
                        onClick = {
                            if (fontScale > 0.86f) {
                                fontScale = (fontScale - 0.12f).coerceAtLeast(0.85f)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text(
                            text = "A-",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Font Scale: Increase (A+)
                    IconButton(
                        onClick = {
                            if (fontScale < 1.39f) {
                                fontScale = (fontScale + 0.12f).coerceAtMost(1.40f)
                            }
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Text(
                            text = "A+",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Aurora Wallpaper live ambient toggle
                    IconButton(
                        onClick = { showAurora = !showAurora },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Toggle Aurora Wallpaper",
                            tint = if (showAurora) IslamicGold else MaterialTheme.colorScheme.outline
                        )
                    }

                    // Share Section
                    IconButton(
                        onClick = {
                            val shareText = buildString {
                                appendLine("📖 ${section.titleBn}")
                                if (section.subtitleBn.isNotBlank()) appendLine(section.subtitleBn)
                                if (section.noticeTextBn.isNotBlank()) {
                                    appendLine("\n📌 ${section.noticeHighlightBn}")
                                    appendLine(section.noticeTextBn)
                                }
                                appendLine("\n— মোট ${section.items.size} টি গুরুত্বপূর্ণ বিষয় সংকলিত")
                                appendLine("— দাওয়াহ টু জান্নাহ অ্যাপ")
                            }
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "শেয়ার করুন"))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Section",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )

            // User Friendly Quick Controls Banner (Font Scale & Aurora Status)
            if (isDuaRichSection) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = if (showAurora) Color.White.copy(alpha = 0.88f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showAurora) "লাইভ অরোরা ওয়ালপেপার: চালু" else "লাইভ অরোরা: বন্ধ",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "হরফের আকার: ${(fontScale * 100).toInt()}%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Overview Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = sectionIcon,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(28.dp)
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
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = section.subtitleBn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 17.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "মোট ${CalendarHelper.toBanglaNumber(section.items.size)} টি গুরুত্বপূর্ণ পাঠ",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Notice / Alert Box if present
            if (section.noticeTextBn.isNotBlank()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                if (section.noticeHighlightBn.isNotBlank()) {
                                    Text(
                                        text = section.noticeHighlightBn,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                }
                                Text(
                                    text = section.noticeTextBn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }
                }
            }

            // Search Field for sections with multiple items
            if (section.items.size > 6) {
                item {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("যিকির, দো'আ বা অর্থ খুঁজুন...") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
            }

            // Quick Category Filters (for Tawbah & Istighfar or other comprehensive sections)
            if (categoryFilters.isNotEmpty()) {
                item {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categoryFilters) { cat ->
                            val isSelected = selectedCategoryFilter == cat
                            val count = when (cat) {
                                "সকল বিষয়", "সকল দো'আ", "সকল আমল ও দো'আ", "সকল দো'আ ও আমল", "সকল অধ্যায়", "সকল আমল ও নাম", "সকল বাক্য ও আমল", "সকল বিষয় ও দো'আ" -> section.items.size
                                "সূরা আল ইমরান ও ঋণমুক্তি" -> section.items.count { it.id in listOf("ai_guideline_tawakkul", "ai_main_verses_26_27", "ai_muadh_hadith_debt_dua", "ai_amal_rules_and_virtues") }
                                "সূরা হাশরের শেষ ৩ আয়াত" -> section.items.count { it.id in listOf("ai_hashr_taawwudh", "ai_hashr_ayah_22", "ai_hashr_ayah_23", "ai_hashr_ayah_24") }
                                "রিযিক ও ঋণমুক্তির সহীহ দো'আ" -> section.items.count { it.id in listOf("ai_ali_hadith_debt_dua", "ai_anxiety_debt_bukhari", "ai_durood_ibrahim_bridge", "ai_istighfar_surah_nuh", "ai_fajr_morning_rizq") }
                                "তাহকীক ও তাওয়াক্কুল গাইড" -> section.items.count { it.id in listOf("ai_guideline_tawakkul", "ai_amal_rules_and_virtues", "ai_tahqeeq_faq_guide") }
                                "ফরজ সালাত পরবর্তী" -> section.items.count { it.id in listOf("me_ayatul_kursi", "me_ajirni_minan_nar", "me_three_quls", "me_durood_sharif", "me_hasbiyallah", "me_subhanallahi_adada_khalqihi", "me_ilman_nafian") }
                                "হেফাজত ও নিরাপত্তা" -> section.items.count { it.id in listOf("me_ayatul_kursi", "me_three_quls", "me_bismillahilladhi", "me_audhu_bikalimatillah", "me_allahumma_afini_fi_badani", "me_allahumma_inni_as_alukal_afwa", "me_alimul_ghaybi", "me_shirk_protection", "me_hammi_wal_hazan") }
                                "ক্ষমা ও জান্নাত লাভ" -> section.items.count { it.id in listOf("me_ajirni_minan_nar", "me_durood_sharif", "me_radeetu_billah", "me_sayyidul_istighfar", "me_nasalukal_jannah", "me_allahumma_ushhiduka", "me_subhanallahi_wa_bihamdihi_hundred", "me_astaghfirullah_hundred") }
                                "তাওহীদ ও তাসবীহাত" -> section.items.count { it.id in listOf("me_hasbiyallah", "me_ya_hayyu_ya_qayyum", "me_subhanallahi_adada_khalqihi", "me_asbahna_wa_asbahal_mulk", "me_allahumma_bika_asbahna", "me_allahumma_ma_asbaha_bi", "me_la_ilaha_illallah_ten", "me_la_ilaha_illallah_hundred", "me_subhanallahi_wa_bihamdihi_hundred", "me_fitratil_islam", "me_khayra_hadhal_yawm") }
                                "ফিকহ ও আদব গাইড" -> section.items.count { it.id in listOf("me_fiqh_routine_guide") }
                                "সালাতে সালামের পূর্বে" -> section.items.count { it.id.startsWith("sb_salam_") }
                                "কোরআনের দো'আ" -> section.items.count { it.id.startsWith("sb_quran_") }
                                "সহীহ হাদিসের দো'আ" -> section.items.count { it.id.startsWith("sb_hadith_") }
                                "সালামের পর প্রাথমিক" -> section.items.count { it.id.startsWith("fa_core_") }
                                "হিদায়াত ও দ্বীন" -> section.items.count { it.id.startsWith("fa_guidance_") }
                                "পানাহ ও নিরাপত্তা" -> section.items.count { it.id.startsWith("fa_refuge_") }
                                "রিযিক ও বরকত" -> section.items.count { it.id.startsWith("fa_rizq_") }
                                "বিশেষ আমল ও ইস্তেগফার" -> section.items.count { it.id.startsWith("fa_virtue_") }
                                "শেষ পরিণতি ও জান্নাত" -> section.items.count { it.id.startsWith("fa_end_") }
                                "মৌলিক ইস্তিগফার" -> section.items.count { it.id.startsWith("ti_") && !it.id.startsWith("ti_dua_") && !it.id.startsWith("ti_deed_") }
                                "৫০টি ইস্তেগফার ও দু'আ" -> section.items.count { it.id.startsWith("ti_dua_") }
                                "১৬টি গুনাহ মোচনকারী আমল" -> section.items.count { it.id.startsWith("ti_deed_") }
                                "মর্যাদা ও আদব", "মর্যাদা ও সুন্নাত" -> section.items.count { it.id in listOf("fa_quran_jumuah", "fa_virtue_concept", "fa_ghusl_miswak", "fa_early_to_masjid", "fa_sunnah_adab", "fa_walk_to_masjid", "fa_khutbah_adab", "fa_jumuah_death_virtue", "fa_adab_ihtiba_forbidden", "fa_adab_shift_place_drowsy", "fa_warning_fasting_friday_alone") }
                                "নফল ও সুন্নাত সালাত" -> section.items.count { it.id in listOf("fa_tahiyyatul_masjid_khutbah", "fa_salat_before_jumuah", "fa_salat_after_jumuah", "fa_salatut_tasbeeh") }
                                "কুরআনী সূরা ও দরূদ" -> section.items.count { it.id in listOf("fa_surah_kahaf", "fa_surah_fajr_jumuah", "fa_durood_sharif", "fa_surah_jumuah_reflection", "fa_surah_munafiqun_warning", "fa_surah_ala_ghashiyah", "fa_surah_three_quls") }
                                "যিকির ও তাসবীহাত" -> section.items.count { it.id in listOf("fa_dhikr_subhanallahi_wa_bihamdihi", "fa_dhikr_tahlil_hundred", "fa_dhikr_hawqalah", "fa_dhikr_sayyidul_tasbeeh") }
                                "দো'আ কবুল ও খাস দো'আ" -> section.items.count { it.id.startsWith("fa_dua_") || it.id == "fa_saatul_ijabah" }
                                "তাহকীক, রুটিন ও FAQ", "সতর্কতা, রুটিন ও FAQ" -> section.items.count { it.id in listOf("fa_warning_bidah_fabricated", "fa_checklist_sunnah_routine", "fa_faq_jumuah", "fa_advanced_tahqeeq_faq") }
                                "সকল" -> section.items.size
                                else -> 0
                            }
                            val displayLabel = if (count > 0) "$cat (${CalendarHelper.toBanglaNumber(count)})" else cat

                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCategoryFilter = cat },
                                label = {
                                    Text(
                                        text = displayLabel,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 13.sp
                                    )
                                },
                                leadingIcon = if (isSelected) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                } else null,
                                shape = RoundedCornerShape(10.dp),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    labelColor = MaterialTheme.colorScheme.onSurface
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
                                    borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    selectedBorderColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }

            // Section Items Full List
            if (filteredItems.isEmpty()) {
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ) {
                        Text(
                            text = "কোনো যিকির বা দো'আ পাওয়া যায়নি",
                            modifier = Modifier.padding(20.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                itemsIndexed(filteredItems, key = { index, item -> item.id.ifBlank { "item_$index" } }) { idx, item ->
                    val originalIndex = section.items.indexOf(item)
                    val displayIndex = if (originalIndex >= 0) originalIndex + 1 else idx + 1
                    IslamicLifeDetailCard(
                        item = item,
                        index = displayIndex,
                        fontScale = fontScale,
                        isAuroraActive = showAurora
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
}

@Composable
private fun IslamicLifeDetailCard(
    item: IslamicLifeCardItem,
    index: Int,
    fontScale: Float = 1.0f,
    isAuroraActive: Boolean = false
) {
    val context = LocalContext.current
    val itemSubtitle = item.subtitleBn.ifBlank { item.repetitionOrTimeBn }
    val fojilotText = item.fojilotBn
    val detailsText = item.detailsBn
    val hasDistinctDetails = detailsText.isNotBlank() && detailsText != fojilotText
    var isDetailsExpanded by remember(item.id) { mutableStateOf(true) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isAuroraActive) Color.White.copy(alpha = 0.92f) else MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            if (isAuroraActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Card Header with Serial, Repetition/Time, Copy and Share
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Surface(
                        modifier = Modifier.size(32.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = CalendarHelper.toBanglaNumber(index),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }

                    if (item.repetitionOrTimeBn.isNotBlank()) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f)
                        ) {
                            Text(
                                text = item.repetitionOrTimeBn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val copyPayload = buildString {
                                appendLine("【 ${item.titleBn} 】")
                                if (item.repetitionOrTimeBn.isNotBlank()) appendLine("আমল/সময়: ${item.repetitionOrTimeBn}")
                                if (item.arabicText.isNotBlank()) appendLine("\n${item.arabicText}")
                                if (item.pronunciationBn.isNotBlank() && item.pronunciationBn != item.titleBn) appendLine("\nউচ্চারণ: ${item.pronunciationBn}")
                                if (item.meaningBn.isNotBlank()) {
                                    val label = if (item.detailsBn.isBlank() && item.fojilotBn.isBlank()) "বাংলা অর্থ" else "অর্থ"
                                    appendLine("\n$label: ${item.meaningBn}")
                                }
                                if (fojilotText.isNotBlank()) appendLine("\nআমল ও ফজিলতের রূপরেখা:\n$fojilotText")
                                if (hasDistinctDetails) appendLine("\n$detailsText")
                                else if (fojilotText.isBlank() && detailsText.isNotBlank()) appendLine("\nফজিলত ও আমলের রূপরেখা:\n$detailsText")
                                if (item.referenceBn.isNotBlank()) appendLine("\nসূত্র: ${item.referenceBn}")
                                appendLine("\n— দাওয়াহ টু জান্নাহ অ্যাপ")
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Islamic Amol", copyPayload)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "দো'আটি ক্লিপবোর্ডে কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Item",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val sharePayload = buildString {
                                appendLine("🔹 ${item.titleBn}")
                                if (item.repetitionOrTimeBn.isNotBlank()) appendLine("সময়/আমল: ${item.repetitionOrTimeBn}")
                                if (item.arabicText.isNotBlank()) appendLine("\n${item.arabicText}")
                                if (item.pronunciationBn.isNotBlank() && item.pronunciationBn != item.titleBn) appendLine("\nউচ্চারণ: ${item.pronunciationBn}")
                                if (item.meaningBn.isNotBlank()) {
                                    val label = if (item.detailsBn.isBlank() && item.fojilotBn.isBlank()) "বাংলা অর্থ" else "অর্থ"
                                    appendLine("\n$label: ${item.meaningBn}")
                                }
                                if (fojilotText.isNotBlank()) appendLine("\nআমল ও ফজিলত:\n$fojilotText")
                                if (hasDistinctDetails) appendLine("\n$detailsText")
                                else if (fojilotText.isBlank() && detailsText.isNotBlank()) appendLine("\nবিস্তারিত ও ফজিলত:\n$detailsText")
                                if (item.referenceBn.isNotBlank()) appendLine("\nরেফারেন্স: ${item.referenceBn}")
                                appendLine("\n— দাওয়াহ টু জান্নাহ অ্যাপ")
                            }
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, sharePayload)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "শেয়ার করুন"))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Item",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = item.titleBn,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = (16 * fontScale).sp,
                    lineHeight = (24 * fontScale).sp
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Subtitle if different
            if (itemSubtitle.isNotBlank() && itemSubtitle != item.repetitionOrTimeBn) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = itemSubtitle,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontSize = (13 * fontScale).sp,
                        lineHeight = (18 * fontScale).sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Arabic Text Container
            if (item.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isAuroraActive) Color(0xFFF0FDF4).copy(alpha = 0.75f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = item.arabicText,
                            fontFamily = LocalArabicFontFamily.current,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = (23 * fontScale).sp,
                                lineHeight = (42 * fontScale).sp
                            ),
                            fontWeight = FontWeight.Normal,
                            color = IslamicGold,
                            textAlign = TextAlign.Right,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Pronunciation
            if (item.pronunciationBn.isNotBlank() && item.pronunciationBn != item.titleBn) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "উচ্চারণ:",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = (12 * fontScale).sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.pronunciationBn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = (15 * fontScale).sp,
                                lineHeight = (22 * fontScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Meaning
            if (item.meaningBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                RichIslamicTextLayout(
                    text = item.meaningBn,
                    defaultHeader = if (item.detailsBn.isBlank() && item.fojilotBn.isBlank()) "বাংলা অর্থ:" else "অর্থ ও তাৎপর্য:",
                    fontScale = fontScale
                )
            }

            // Fojilot / Amol Section
            val primaryVirtueText = if (fojilotText.isNotBlank()) fojilotText else detailsText
            if (primaryVirtueText.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                if (primaryVirtueText.startsWith("📌") || primaryVirtueText.contains("ফুটনোটঃ") || primaryVirtueText.contains("• (১)")) {
                    RichIslamicTextLayout(
                        text = primaryVirtueText,
                        defaultHeader = "ফজিলত ও আমলের রূপরেখা:",
                        fontScale = fontScale
                    )
                } else {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(10.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "আমল ও ফজিলতের রূপরেখা:",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = (12 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = primaryVirtueText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = (13.5f * fontScale).sp,
                                        lineHeight = (20 * fontScale).sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Dedicated Detailed Discussion / Article (when both fojilot and details are provided and distinct)
            if (hasDistinctDetails && detailsText.length > 50 && !fojilotText.contains(detailsText)) {
                Spacer(modifier = Modifier.height(12.dp))
                val detailsHeaderTitle = when {
                    detailsText.contains("ড. ইয়াসির কাদি") -> "বিস্তারিত তাৎপর্য ও ঘটনা (ড. ইয়াসির কাদি)"
                    detailsText.contains("হাদিস") || detailsText.contains("আমল") -> "বিস্তারিত আলোচনা ও আমলের নিয়ম"
                    else -> "বিস্তারিত তাৎপর্য ও বিবরণ"
                }
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { isDetailsExpanded = !isDetailsExpanded },
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = detailsHeaderTitle,
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = (13 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Icon(
                                imageVector = if (isDetailsExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = if (isDetailsExpanded) "সংক্ষেপ করুন" else "বিস্তারিত দেখুন",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        AnimatedVisibility(visible = isDetailsExpanded) {
                            Column(modifier = Modifier.padding(top = 10.dp)) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                    thickness = 0.8.dp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = detailsText,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = (14 * fontScale).sp,
                                        lineHeight = (22 * fontScale).sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }

            // Reference
            if (item.referenceBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "সূত্র: ${item.referenceBn}",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = (11.5f * fontScale).sp),
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RichIslamicTextLayout(
    text: String,
    defaultHeader: String = "অর্থ ও তাৎপর্য:",
    fontScale: Float = 1.0f
) {
    val hasRichMarkers = text.contains("📜") || text.contains("✨") || text.contains("🔍") ||
            text.contains("📌") || text.contains("❝") || text.contains("১.") ||
            text.contains("ফুটনোটঃ")

    if (!hasRichMarkers) {
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                if (defaultHeader.isNotBlank()) {
                    Text(
                        text = defaultHeader,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = (12 * fontScale).sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14.5f * fontScale).sp,
                        lineHeight = (22 * fontScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        return
    }

    val rawBlocks = text.split("\n\n").filter { it.isNotBlank() }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        for (block in rawBlocks) {
            val trimmed = block.trim()
            when {
                trimmed.startsWith("📜") -> {
                    // Hadith Narration Box
                    val headerAndBody = trimmed.substringAfter("📜").trim()
                    val firstColon = headerAndBody.indexOf(':')
                    val (title, body) = if (firstColon in 1..40) {
                        headerAndBody.substring(0, firstColon).trim() to headerAndBody.substring(firstColon + 1).trim()
                    } else {
                        "হাদীসের বর্ণনা ও প্রেক্ষাপট" to headerAndBody
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = (13 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = body,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (14.5f * fontScale).sp,
                                    lineHeight = (22 * fontScale).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                trimmed.startsWith("✨") -> {
                    // Sahabi Personal Practice Card
                    val headerAndBody = trimmed.substringAfter("✨").trim()
                    val firstColon = headerAndBody.indexOf(':')
                    val (title, body) = if (firstColon in 1..50) {
                        headerAndBody.substring(0, firstColon).trim() to headerAndBody.substring(firstColon + 1).trim()
                    } else {
                        "সাহাবীর নিজস্ব আমল ও ফলাফল" to headerAndBody
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = (13 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.tertiary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            RenderQuoteOrText(body, fontScale)
                        }
                    }
                }

                trimmed.startsWith("🔍") -> {
                    // Scholars' analysis and commentary
                    val headerAndBody = trimmed.substringAfter("🔍").trim()
                    val firstColon = headerAndBody.indexOf(':')
                    val (title, body) = if (firstColon in 1..60) {
                        headerAndBody.substring(0, firstColon).trim() to headerAndBody.substring(firstColon + 1).trim()
                    } else {
                        "মুহাদ্দিসীনদের তাহক্বীক ও ব্যাখ্যা" to headerAndBody
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = title,
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = (13 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            RenderQuoteOrText(body, fontScale)
                        }
                    }
                }

                trimmed.startsWith("📌") || trimmed.startsWith("ফুটনোটঃ") -> {
                    // Footnotes and Jurisprudential Guidance
                    val cleanText = trimmed.removePrefix("📌").trim()
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.BookmarkBorder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "তাহক্বীক ও ফিক্বহী জ্ঞাতব্য বিষয়াবলী:",
                                    style = MaterialTheme.typography.labelMedium.copy(fontSize = (13 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            val subBullets = cleanText.split("\n• ").map { it.removePrefix("• ").trim() }.filter { it.isNotBlank() }
                            if (subBullets.size > 1) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (bullet in subBullets) {
                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = MaterialTheme.colorScheme.surface,
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(modifier = Modifier.padding(10.dp)) {
                                                Text(
                                                    text = "• ",
                                                    color = MaterialTheme.colorScheme.primary,
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = bullet,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        fontSize = (13 * fontScale).sp,
                                                        lineHeight = (20 * fontScale).sp
                                                    ),
                                                    color = MaterialTheme.colorScheme.onSurface
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Text(
                                    text = cleanText,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = (13 * fontScale).sp,
                                        lineHeight = (20 * fontScale).sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }

                trimmed.matches(Regex("^[১-৯]\\..*", RegexOption.DOT_MATCHES_ALL)) -> {
                    // Numbered tip card (e.g. 1., 2., 3., 4.)
                    val stepNum = trimmed.substringBefore('.').trim()
                    val rest = trimmed.substringAfter('.').trim()
                    val titleAndBody = rest.split("\n", limit = 2)
                    val stepTitle = titleAndBody.firstOrNull()?.trim() ?: ""
                    val stepBody = titleAndBody.getOrNull(1)?.trim() ?: ""

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = stepNum,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = stepTitle.removeSuffix(":"),
                                    style = MaterialTheme.typography.titleSmall.copy(fontSize = (14 * fontScale).sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            if (stepBody.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                RenderQuoteOrText(stepBody, fontScale)
                            }
                        }
                    }
                }

                trimmed.startsWith("❝") && trimmed.endsWith("❞") -> {
                    // Standalone quote block
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(28.dp)
                                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = trimmed.removeSurrounding("❝", "❞").trim(),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (14 * fontScale).sp,
                                    lineHeight = (22 * fontScale).sp
                                ),
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                else -> {
                    // Regular paragraph block
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = trimmed,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = (14.5f * fontScale).sp,
                                    lineHeight = (22 * fontScale).sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderQuoteOrText(raw: String, fontScale: Float = 1.0f) {
    if (raw.contains("❝") && raw.contains("❞")) {
        val parts = raw.split("❝")
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            val before = parts[0].trim()
            if (before.isNotBlank()) {
                Text(
                    text = before,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = (14.5f * fontScale).sp,
                        lineHeight = (22 * fontScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            if (parts.size > 1) {
                val quoteAndAfter = parts[1].split("❞")
                val quote = quoteAndAfter[0].trim()
                val after = quoteAndAfter.getOrNull(1)?.trim() ?: ""

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.padding(10.dp)) {
                        Box(
                            modifier = Modifier
                                .width(3.dp)
                                .height(26.dp)
                                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = quote,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = (14.5f * fontScale).sp,
                                lineHeight = (22 * fontScale).sp
                            ),
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                if (after.isNotBlank()) {
                    Text(
                        text = after,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = (13 * fontScale).sp,
                            lineHeight = (20 * fontScale).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    } else {
        Text(
            text = raw,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = (14.5f * fontScale).sp,
                lineHeight = (22 * fontScale).sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
