package com.example.ui.screens.tools

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import java.text.NumberFormat
import java.util.Locale

/**
 * Data model representing each tool feature in the 3-column grid.
 */
data class ToolFeatureItem(
    val id: String,
    val nameBn: String,          // Visible on the card (clean 1-2 line title)
    val fullNameBn: String,      // Full title shown inside the card
    val subtitleBn: String,      // Subtitle shown inside the card
    val descriptionBn: String,   // Comprehensive details shown inside the card
    val icon: ImageVector,
    val emoji: String,           // Eye-catching visual emblem
    val badgeBn: String,         // Category badge
    val primaryColor: Color,     // Accent color
    val softContainerColor: Color, // Eye-soothing pastel background color
    val highlights: List<String> = emptyList(), // Highlight tags shown inside the card
    val isAvailable: Boolean = true,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ToolsScreen(
    onOpenFridayMode: () -> Unit = {},
    onOpenHolyQuran: () -> Unit = {},
    onOpenHadithCollection: () -> Unit = {},
    onOpenDuaBySituation: () -> Unit = {},
    onOpenPersonalDuaBuilder: () -> Unit = {},
    onOpenExplainAyahCamera: () -> Unit,
    onOpenSmartQuranSearch: () -> Unit = {},
    onOpenIslamicHabitSystem: () -> Unit = {},
    onOpenRamadanIntelligence: () -> Unit = {},
    onOpenAskBeforeYouAct: () -> Unit = {},
    onOpenAyatDetector: () -> Unit,
    onOpenQibla: () -> Unit,
    onOpenTasbih: () -> Unit,
    onOpenNamesOfAllah: () -> Unit,
    onOpenMosqueMode: () -> Unit = {},
    onOpenIslamicContextVerify: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val banglaFont = LocalBanglaFontFamily.current

    // Zakat calculator dialog state
    var showZakatDialog by remember { mutableStateOf(false) }

    // Currently selected feature for displaying all its rich details inside the sheet
    var selectedFeatureForDetails by remember { mutableStateOf<ToolFeatureItem?>(null) }

    // Search query for filtering features
    var searchQuery by remember { mutableStateOf("") }

    // All features list — exactly 21 tools (divisible by 3 = 7 balanced rows)
    val allFeatures = remember {
        listOf(
            ToolFeatureItem(
                id = "tool_friday_mode",
                nameBn = "জুমার মোড",
                fullNameBn = "Friday Mode (জুমার মোড)",
                subtitleBn = "আপনার জুমার প্রস্তুতি, খুতবা ও বরকতময় আমল সমূহের সহচর",
                descriptionBn = "পবিত্র জুমার দিনের বিশেষ মর্যাদা, ৪টি প্রধান স্তর, গোসল, মিসওয়াক, আগে গমন, সূরা কাহাফ, সা'আতুল ইজাবাহ, তাহিয়্যাতুল মসজিদ, সালাতুত তাসবীহ, সুন্নাত ও নফল সালাত এবং বিশেষ তাসবীহাত সহ পূর্ণাঙ্গ ৪৫টি প্রামাণ্য আমল।",
                icon = Icons.Default.AutoAwesome,
                emoji = "🕌",
                badgeBn = "নতুন পূর্ণাঙ্গ মোড",
                primaryColor = Color(0xFF047857),
                softContainerColor = Color(0xFFE8F5E9),
                highlights = listOf("খুতবার আদব", "সূরা কাহাফ", "সা'আতুল ইজাবাহ", "৪০+ আমল", "স্বয়ংক্রিয় সক্রিয়"),
                onClick = onOpenFridayMode
            ),
            ToolFeatureItem(
                id = "tool_ramadan_intelligence",
                nameBn = "রমাদান ইন্টেলিজেন্স",
                fullNameBn = "All in one Ramadan & Ramadan Intelligence",
                subtitleBn = "Your Complete Ramadan Companion — Before, During & After Ramadan",
                descriptionBn = "একটি সামগ্রিক রমাদান অপারেটিং সিস্টেম: প্রস্তুতি → ইবাদত → কুরআন → দো'আ → সিয়াম → দান → আত্মদর্শন → কদর → ঈদ → শাওয়াল → ধারাবাহিকতা। ১টি প্ল্যাটফর্মেই রমাদানের প্রতিটি মুহূর্তকে সচেতন ও বরকতময় করার পূর্ণাঙ্গ ব্যবস্থা।",
                icon = Icons.Default.Nightlight,
                emoji = "🌙",
                badgeBn = "সিগনেচার",
                primaryColor = Color(0xFF059669),
                softContainerColor = Color(0xFFE0F2F1),
                highlights = listOf("হোম ড্যাশবোর্ড", "১২-মাত্রিক চেকলিস্ট", "খতম ইন্টেলিজেন্স", "দোয়া ভল্ট", "কদর প্ল্যানার", "শাওয়াল রোজা"),
                onClick = onOpenRamadanIntelligence
            ),
            ToolFeatureItem(
                id = "tool_holy_quran",
                nameBn = "পবিত্র কুরআন",
                fullNameBn = "The Holy Quran (পবিত্র কুরআনুল কারীম)",
                subtitleBn = "১১৪ সূরার প্রমিত আরবি, বিশুদ্ধ অনুবাদ ও বিশদ তাফসীর",
                descriptionBn = "১১৪ সূরার প্রমিত আরবি পাঠ, বিশুদ্ধ বাংলা উচ্চারণ, প্রামাণ্য অনুবাদ (ড. আবু বকর যাকারিয়া), বিশদ তাফসীর ও অফলাইন অডিও তিলাওয়াত। আয়াতভিত্তিক বুকমার্ক ও শব্দে শব্দে পড়ার সুবিধা।",
                icon = Icons.Default.MenuBook,
                emoji = "📖",
                badgeBn = "কুরআনুল কারীম",
                primaryColor = Color(0xFF0F766E),
                softContainerColor = Color(0xFFE0F7FA),
                highlights = listOf("১১৪ সূরা", "আবু বকর যাকারিয়া অনুবাদ", "বিশদ তাফসীর", "অফলাইন অডিও", "শব্দার্থ"),
                onClick = onOpenHolyQuran
            ),
            ToolFeatureItem(
                id = "tool_hadith_collection",
                nameBn = "হাদীস সম্ভার",
                fullNameBn = "সহীহ হাদীস সম্ভার (HadithBD / IRD)",
                subtitleBn = "সিহাহ্ সিত্তাহ ও বিশ্বস্ত হাদীস গ্রন্থের সুবিশাল অফলাইন ডেটাবেজ",
                descriptionBn = "সিহাহ্ সিত্তাহ (সহীহ বুখারী, সহীহ মুসলিম, সুনানে তিরমিজি, আবু দাউদ, নাসাঈ, ইবনে মাজাহ) এবং রিয়াযুস স্বা-লিহীন, বুলুগুল মারাম ও ৪০ হাদীসের পূর্ণাঙ্গ প্রামাণ্য অফলাইন ডেটাবেজ।",
                icon = Icons.Default.CollectionsBookmark,
                emoji = "📚",
                badgeBn = "হাদিসবিডি মানদণ্ড",
                primaryColor = Color(0xFF4338CA),
                softContainerColor = Color(0xFFEEF2FF),
                highlights = listOf("সিহাহ্ সিত্তাহ", "বুলুগুল মারাম", "রিয়াযুস স্বা-লিহীন", "৪০ হাদীস", "অধ্যায়ভিত্তিক সার্চ"),
                onClick = onOpenHadithCollection
            ),
            ToolFeatureItem(
                id = "tool_mosque_mode",
                nameBn = "মসজিদ মোড",
                fullNameBn = "Mosque Mode (মসজিদ মোড)",
                subtitleBn = "মসজিদে প্রবেশের সাথে সাথে সম্পূর্ণ নিঃশব্দ ও একাগ্রতা",
                descriptionBn = "মসজিদে প্রবেশের সাথে সাথে স্বয়ংক্রিয় নিঃশব্দ মোড, বিভ্রান্তিমুক্ত একাগ্রতা, জামা'আত সূচী, তাহিয়্যাতুল মসজিদ সালাত নির্দেশিকা, কুরআন তিলাওয়াত, আযকার ও সালাত ট্র্যাকার।",
                icon = Icons.Default.NotificationsOff,
                emoji = "🔇",
                badgeBn = "নতুন সিগনেচার",
                primaryColor = Color(0xFF15803D),
                softContainerColor = Color(0xFFE8F5E9),
                highlights = listOf("স্বয়ংক্রিয় সাইলেন্ট", "তাহিয়্যাতুল মসজিদ", "জামা'আত ঘড়ি", "ডিজিটাল একাগ্রতা"),
                onClick = onOpenMosqueMode
            ),
            ToolFeatureItem(
                id = "tool_dua_by_situation",
                nameBn = "পরিস্থিতির দু'আ",
                fullNameBn = "Dua by Situation (অনুভূতি ও পরিস্থিতি অনুযায়ী দু'আ)",
                subtitleBn = "I feel... I need... দ্বিমুখী আত্মিক অনুসন্ধান ও নববী সমাধান",
                descriptionBn = "মনের কষ্ট, হতাশা, ভয়, রোগব্যাধি, ক্ষমা প্রার্থনা বা যেকোনো মানবিক পরিস্থিতিতে কুরআন ও সহীহ সুন্নাহর প্রামাণ্য দু'আ সম্ভার। বিশুদ্ধ আরবী হরকত, উচ্চারণ ও অর্থ।",
                icon = Icons.Default.Favorite,
                emoji = "🤲",
                badgeBn = "নতুন সিগনেচার",
                primaryColor = Color(0xFFBE123C),
                softContainerColor = Color(0xFFFFF1F2),
                highlights = listOf("আবেগভিত্তিক অনুসন্ধান", "কুরআনী সমাধান", "সহীহ সুন্নাহ", "অর্থ ও উচ্চারণ"),
                onClick = onOpenDuaBySituation
            ),
            ToolFeatureItem(
                id = "tool_personal_dua_builder",
                nameBn = "দো'আ আর্কিটেক্ট",
                fullNameBn = "Personal Dua Builder (ব্যক্তিগত দো'আ আর্কিটেক্ট)",
                subtitleBn = "কুরআনী আয়াত ও সহীহ নববী দু'আর কাঠামোগত বিন্যাস",
                descriptionBn = "আল্লাহর হামদ-সানা, দরূদ শরীফ, তওবা-ইস্তেগফার ও সুন্নাতী আদবের সমন্বয়ে নিজের জন্য নিখুঁত ব্যক্তিগত দো'আ সংকলন তৈরি ও সংরক্ষণ করুন।",
                icon = Icons.Default.EditNote,
                emoji = "✍️",
                badgeBn = "নতুন সিগনেচার",
                primaryColor = Color(0xFFB45309),
                softContainerColor = Color(0xFFFFFBEB),
                highlights = listOf("সুন্নাতী কাঠামো", "দরূদ ও হামদ", "ব্যক্তিগত নোট", "বুকমার্ক সংগ্রহ"),
                onClick = onOpenPersonalDuaBuilder
            ),
            ToolFeatureItem(
                id = "tool_islamic_habit_system",
                nameBn = "সুন্নাহ ট্র্যাকার",
                fullNameBn = "Islamic Habit System (সুন্নাহ ট্র্যাকার)",
                subtitleBn = "অতিরিক্ত গ্যামিফিকেশন মুক্ত মৃদু বরকতময় সুন্নাত অভ্যাস",
                descriptionBn = "মেসওয়াক, ডান হাত ব্যবহার, সালামের প্রসার, অজু অবস্থায় নিদ্রা সহ রাসুলুল্লাহ ﷺ এর বরকতময় দৈনন্দিন সুন্নাহ মৃদুভাবে অভ্যাসে পরিণত করার ট্র্যাকার।",
                icon = Icons.Default.Spa,
                emoji = "🌱",
                badgeBn = "সিগনেচার",
                primaryColor = Color(0xFF0F766E),
                softContainerColor = Color(0xFFE0F2F1),
                highlights = listOf("মৃদু ট্র্যাকিং", "দৈনন্দিন সুন্নাহ", "ধারাবাহিকতা", "মানসিক প্রশান্তি"),
                onClick = onOpenIslamicHabitSystem
            ),
            ToolFeatureItem(
                id = "tool_ask_before_you_act",
                nameBn = "পদক্ষেপের আগে",
                fullNameBn = "Ask Before You Act (পদক্ষেপ নেওয়ার আগে জানুন)",
                subtitleBn = "আর্থিক সিদ্ধান্ত বা চুক্তির পূর্বে কাঠামোগত শরঈ প্রশ্নমালা",
                descriptionBn = "লেনদেন, চাকরি, চুক্তি, বিনিয়োগ বা সোশ্যাল মিডিয়া আচরণের পূর্বে শরীয়াহ সম্মত বিশুদ্ধতা নিশ্চিত করতে প্রামাণ্য দলীলভিত্তিক প্রশ্ন ও গাইডলাইন।",
                icon = Icons.Default.Psychology,
                emoji = "⚖️",
                badgeBn = "সিগনেচার",
                primaryColor = Color(0xFF6D28D9),
                softContainerColor = Color(0xFFF3E8FF),
                highlights = listOf("হালাল-হারাম ফিল্টার", "আর্থিক চুক্তি", "দলিলভিত্তিক", "শরঈ সতর্কতা"),
                onClick = onOpenAskBeforeYouAct
            ),
            ToolFeatureItem(
                id = "tool_smart_quran_search",
                nameBn = "স্মার্ট কুরআন সার্চ",
                fullNameBn = "স্মার্ট কুরআন সার্চ (ভাবার্থভিত্তিক অনুসন্ধান)",
                subtitleBn = "বাংলা ভাষায় যে কোনো বিষয় লিখে প্রাসঙ্গিক আয়াত খুঁজুন",
                descriptionBn = "সহজ বাংলা ভাষায় যেকোনো প্রশ্ন, মনের অবস্থা বা বিষয় টাইপ করুন—কুরআনের সংশ্লিষ্ট আয়াত, অনুবাদ ও প্রেক্ষাপট তাৎক্ষণিক পেয়ে যাবেন।",
                icon = Icons.Default.Search,
                emoji = "🔍",
                badgeBn = "এআই সার্চ",
                primaryColor = Color(0xFF0369A1),
                softContainerColor = Color(0xFFE0F2FE),
                highlights = listOf("ভাবার্থ সার্চ", "বাংলা কি-ওয়ার্ড", "আয়াত ম্যাপিং", "তাৎক্ষণিক ফলাফল"),
                onClick = onOpenSmartQuranSearch
            ),
            ToolFeatureItem(
                id = "tool_camera_lens",
                nameBn = "আয়াত ক্যামেরা",
                fullNameBn = "ক্যামেরায় আয়াত বিশ্লেষণ (Ayah Camera Lens)",
                subtitleBn = "ক্যামেরা দিয়ে আরবী আয়াত স্ক্যান করে তাৎক্ষণিক তাফসীর ও অনুবাদ",
                descriptionBn = "মুসহাফ বা বই থেকে যেকোনো আরবী আয়াতের ছবি তুলুন বা স্ক্যান করুন—অ্যাপ তাৎক্ষণিকভাবে আয়াত শনাক্ত করে সঠিক বাংলা অনুবাদ ও নির্ভরযোগ্য তাফসীর উপস্থাপন করবে।",
                icon = Icons.Default.CameraAlt,
                emoji = "📷",
                badgeBn = "স্মার্ট লেন্স",
                primaryColor = Color(0xFF0D9488),
                softContainerColor = Color(0xFFE0F2F1),
                highlights = listOf("অপটিক্যাল স্ক্যান", "স্বয়ংক্রিয় আয়াত ম্যাচ", "তাফসীর ও অনুবাদ", "ক্যামেরা সাপোর্ট"),
                onClick = onOpenExplainAyahCamera
            ),
            ToolFeatureItem(
                id = "tool_ayat_solver",
                nameBn = "আয়াত শুদ্ধিকরণ",
                fullNameBn = "আয়াত ও হাদীস শুদ্ধিকরণ ল্যাব",
                subtitleBn = "আরবি হরকত-নুকতা শুদ্ধিকরণ ও সহীহ রেফারেন্স ম্যাচিং",
                descriptionBn = "অশুদ্ধ বা বিকৃত আরবি পাঠ্য দিলে তা স্বয়ংক্রিয়ভাবে বিশুদ্ধ হরকত ও নুকতা দিয়ে বিন্যস্ত করে এবং নির্ভরযোগ্য কুরআন ও হাদীস গ্রন্থ থেকে রেফারেন্স মিলিয়ে দেয়।",
                icon = Icons.Default.Spellcheck,
                emoji = "🔬",
                badgeBn = "সক্রিয় ল্যাব",
                primaryColor = Color(0xFFC2410C),
                softContainerColor = Color(0xFFFFEDD5),
                highlights = listOf("হরকত শুদ্ধিকরণ", "সহীহ ভেরিফিকেশন", "নুকতা ফিক্সিং", "রেফারেন্স ট্র্যাকার"),
                onClick = onOpenAyatDetector
            ),
            ToolFeatureItem(
                id = "tool_qibla",
                nameBn = "ক্বিবলা কম্পাস",
                fullNameBn = "ক্বিবলা কম্পাস ও দিক নির্দেশক",
                subtitleBn = "সেন্সর ও জিপিএস ভিত্তিক পবিত্র কা'বা শরীফের নিখুঁত দিক",
                descriptionBn = "স্মার্টফোনের ম্যাগনেটিক সেন্সর ও অবস্থান ব্যবহার করে পবিত্র কা'বা শরীফের সঠিক দিক ও নিখুঁত ডিগ্রি কোণ তাৎক্ষণিক প্রদর্শন করে।",
                icon = Icons.Default.Explore,
                emoji = "🧭",
                badgeBn = "সক্রিয়",
                primaryColor = Color(0xFF047857),
                softContainerColor = Color(0xFFE8F5E9),
                highlights = listOf("সেন্সর কম্পাস", "ডিগ্রি ও দিক", "কা'বার দূরত্ব", "অফলাইন সাপোর্ট"),
                onClick = onOpenQibla
            ),
            ToolFeatureItem(
                id = "tool_tasbih",
                nameBn = "ডিজিটাল তাসবীহ",
                fullNameBn = "ডিজিটাল তাসবীহ ও জিকির কাউন্টার",
                subtitleBn = "সালাত-পরবর্তী তাসবীহাত ও নিজস্ব জিকির লক্ষ্যমাত্রা",
                descriptionBn = "সুবহানাল্লাহ, আলহামদুলিল্লাহ, আল্লাহু আকবার ৩৩/১০০ তাসবীহ, ভাইব্রেশন ফিডব্যাক, সাউন্ড ও কাস্টম জিকির গণনার আধুনিক ইসলামিক কাউন্টার।",
                icon = Icons.Default.Fingerprint,
                emoji = "📿",
                badgeBn = "সক্রিয়",
                primaryColor = Color(0xFF0D9488),
                softContainerColor = Color(0xFFE0F2F1),
                highlights = listOf("৩৩/১০০ কাউন্ট", "হ্যাপটিক ভাইব্রেশন", "সাউন্ড অন/অফ", "দৈনিক রেকর্ড"),
                onClick = onOpenTasbih
            ),
            ToolFeatureItem(
                id = "tool_zakat",
                nameBn = "যাকাত ক্যালকুলেটর",
                fullNameBn = "যাকাত ও নিসাব ক্যালকুলেটর",
                subtitleBn = "স্বর্ণ, রূপা, নগদ অর্থ ও পণ্যের শরীয়াহ সম্মত ২.৫% হিসাব",
                descriptionBn = "সোনা, রূপা, নগদ টাকা, ব্যাংক ব্যালেন্স ও বাণিজ্যিক পণ্যের মূল্য থেকে ঋণ বাদ দিয়ে উদ্বৃত্ত মালের ওপর নিসাব অনুযায়ী ২.৫% প্রদেয় যাকাত নিখুঁতভাবে গণনা করুন।",
                icon = Icons.Default.Calculate,
                emoji = "💰",
                badgeBn = "ক্যালকুলেটর",
                primaryColor = Color(0xFFD97706),
                softContainerColor = Color(0xFFFFFBEB),
                highlights = listOf("নিসাব যাচাই", "স্বর্ণ-রৌপ্য মূল্য", "ঋণ সমন্বয়", "তাৎক্ষণিক ফলাফল"),
                onClick = { showZakatDialog = true }
            ),
            ToolFeatureItem(
                id = "tool_allah_names",
                nameBn = "আল্লাহর ৯৯ নাম",
                fullNameBn = "আল্লাহর ৯৯টি পবিত্র নাম (আসমাউল হুসনা)",
                subtitleBn = "অর্থ, বিশদ ব্যাখ্যা, হিফজ গাইড ও বিশুদ্ধ ফযিলত",
                descriptionBn = "পবিত্র কুরআন ও সুন্নাহ ভিত্তিক মহান আল্লাহর ৯৯টি নামের অর্থ, তাৎপর্য, ফযিলত এবং জীবনের বিভিন্ন প্রয়োজনে কোন নামে দু'আ করবেন তার সুন্দর নির্দেশিকা।",
                icon = Icons.Default.Stars,
                emoji = "✨",
                badgeBn = "সক্রিয়",
                primaryColor = Color(0xFF7C3AED),
                softContainerColor = Color(0xFFF5F3FF),
                highlights = listOf("আরবি ও অর্থ", "আমল ও ফযিলত", "হিফজ চার্ট", "প্রয়োজনীয় দু'আ"),
                onClick = onOpenNamesOfAllah
            ),
            ToolFeatureItem(
                id = "tool_ramadan_companion_shortcut",
                nameBn = "রমাদান সময়সূচী",
                fullNameBn = "সেহেরি ও ইফতারের পূর্ণাঙ্গ সময়সূচী",
                subtitleBn = "৬৪ জেলা ভিত্তিক প্রতিদিনের সেহেরি ও ইফতারের ক্যালেন্ডার",
                descriptionBn = "ইসলামিক ফাউন্ডেশন অনুমোদিত পদ্ধতি অনুযায়ী বাংলাদেশের সকল জেলার জন্য রমাদানের সেহেরির শেষ সময় ও ইফতারের নির্ভুল সময়সূচী এবং বিশেষ দো'আ।",
                icon = Icons.Default.DateRange,
                emoji = "⏰",
                badgeBn = "রমাদান সূচী",
                primaryColor = Color(0xFF047857),
                softContainerColor = Color(0xFFE8F5E9),
                highlights = listOf("৬৪ জেলা সূচী", "কাউন্টডাউন", "দো'আ ও নিয়ত", "বিজ্ঞপ্তি"),
                onClick = onOpenRamadanIntelligence
            ),
            ToolFeatureItem(
                id = "tool_sunnah_guidance",
                nameBn = "সুন্নাহ গাইড",
                fullNameBn = "দৈনন্দিন জীবনে প্রিয় নবীর ﷺ সুন্নাত",
                subtitleBn = "সকাল থেকে রাত পর্যন্ত রাসূলুল্লাহ ﷺ এর জীবনাদর্শ",
                descriptionBn = "ঘুম থেকে উঠা, অযু, সালাত, আহার, পথচলা, পোশাক ও সামাজিক আচরণের ক্ষেত্রে প্রিয় নবী হযরত মুহাম্মদ ﷺ এর প্রামাণ্য সুন্নাত আমলসমূহ।",
                icon = Icons.Default.MenuBook,
                emoji = "🌿",
                badgeBn = "সুন্নাত নির্দেশিকা",
                primaryColor = Color(0xFF059669),
                softContainerColor = Color(0xFFECFDF5),
                highlights = listOf("দৈনন্দিন আমল", "সহীহ রেফারেন্স", "সুন্নাহর আলো", "আমল চেকলিস্ট"),
                onClick = onOpenIslamicHabitSystem
            ),
            ToolFeatureItem(
                id = "tool_islamic_context_verify",
                nameBn = "কনটেক্সট ও যাচাই",
                fullNameBn = "Islamic Context & Verify (ইসলামিক কনটেক্সট ও যাচাই)",
                subtitleBn = "See the claim. Check the evidence. Understand the context.",
                descriptionBn = "হোয়াটসঅ্যাপ, ফেসবুক, ইউটিউব বা সোশ্যাল মিডিয়ায় ছড়িয়ে পড়া চেইন মেসেজ ও ধর্মীয় দাবিসমূহ কুরআন, সিহাহ্ সিত্তাহ সহীহ হাদিস ও বিজ্ঞ ফকীহগণের মতামতের আলোকে প্রামাণ্যভাবে যাচাই ও প্রেক্ষাপট উন্মোচন করুন।",
                icon = Icons.Default.AutoAwesome,
                emoji = "🛡️",
                badgeBn = "AI ফ্ল্যাগশিপ",
                primaryColor = Color(0xFF047857),
                softContainerColor = Color(0xFFE8F5E9),
                highlights = listOf("WhatsApp দাবি যাচাই", "কুরআন রেফারেন্স", "হাদিস তাহকীক", "প্রেক্ষাপট ও শান-এ-নুযুল", "ভুল তথ্য শনাক্ত"),
                onClick = onOpenIslamicContextVerify
            ),
            ToolFeatureItem(
                id = "up_audio_gen",
                nameBn = "কুরআন অডিও",
                fullNameBn = "কুরআন অডিও তিলাওয়াত জেনারেটর",
                subtitleBn = "পছন্দের ক্বারী, গতি ও পুনরাবৃত্তি নির্ধারণ করে নিজস্ব অফলাইন অডিও",
                descriptionBn = "বিশ্ববিখ্যাত ক্বারীদের কণ্ঠ একত্র করে নির্দিষ্ট আয়াতের জন্য অডিও ক্লিপ তৈরি, পুনরাবৃত্তি লুপ ও অফলাইনে শোনার সুবিধা যুক্ত হচ্ছে।",
                icon = Icons.Default.Tune,
                emoji = "🎙️",
                badgeBn = "শীঘ্রই আসছে",
                primaryColor = Color(0xFF0284C7),
                softContainerColor = Color(0xFFF0F9FF),
                highlights = listOf("বিখ্যাত ক্বারী", "রিপিট লুপ", "অফলাইন প্লে", "আসন্ন ফিচার"),
                isAvailable = false,
                onClick = {}
            ),
            ToolFeatureItem(
                id = "up_inheritance",
                nameBn = "মিরাস বণ্টন",
                fullNameBn = "মিরাস ও উত্তরাধিকার বণ্টন ক্যালকুলেটর",
                subtitleBn = "সূরা নিসার ফারায়িজ বিধি অনুযায়ী নিখুঁত সম্পত্তি বণ্টন",
                descriptionBn = "পবিত্র কুরআন ও সুন্নাহর ফারায়িজ বিধি মোতাবেক সন্তান, পিতামাতা, স্ত্রী ও আত্মীয়দের মধ্যে স্বয়ংক্রিয় শরঈ শতাংশ বণ্টন ব্যবস্থা।",
                icon = Icons.Default.Calculate,
                emoji = "⚖️",
                badgeBn = "শীঘ্রই আসছে",
                primaryColor = Color(0xFF57534E),
                softContainerColor = Color(0xFFF5F5F4),
                highlights = listOf("সূরা নিসা", "ফারায়িজ হিসাব", "ওয়ারিশদের অংশ", "আসন্ন ফিচার"),
                isAvailable = false,
                onClick = {}
            )
        )
    }

    // Filter tools according to search query
    val filteredFeatures = remember(searchQuery, allFeatures) {
        if (searchQuery.isBlank()) {
            allFeatures
        } else {
            val q = searchQuery.trim().lowercase()
            allFeatures.filter {
                it.nameBn.lowercase().contains(q) ||
                it.fullNameBn.lowercase().contains(q) ||
                it.subtitleBn.lowercase().contains(q) ||
                it.descriptionBn.lowercase().contains(q)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 10.dp,
                bottom = contentPadding.calculateBottomPadding() + 24.dp,
                start = 12.dp,
                end = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // TOP HEADER ITEM: Title & Eye-soothing Banner
            item(span = { GridItemSpan(3) }) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.6.dp, Color(0xFF047857).copy(alpha = 0.25f)),
                    tonalElevation = 1.dp
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF047857).copy(alpha = 0.12f),
                                        IslamicGold.copy(alpha = 0.05f),
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                            .padding(horizontal = 14.dp, vertical = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "ইসলামিক টুলস ও সেবা",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "আপনার দ্বীনি জীবনকে সহজ ও বরকতময় করার পূর্ণাঙ্গ সম্ভার",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF047857).copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🛠️", fontSize = 16.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Compact Search Input
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "টুল বা ফিচার অনুসন্ধান করুন...",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = banglaFont
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(
                                        onClick = { searchQuery = "" },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        )
                    }
                }
            }

            // FLAGSHIP AI TOOL HERO BANNER: Islamic Context & Verify
            item(span = { GridItemSpan(3) }) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .clickable(onClick = onOpenIslamicContextVerify),
                    shape = RoundedCornerShape(18.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.2.dp, Color(0xFF047857).copy(alpha = 0.35f)),
                    tonalElevation = 2.dp
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF047857).copy(alpha = 0.14f),
                                        Color(0xFFD97706).copy(alpha = 0.08f),
                                        MaterialTheme.colorScheme.surface
                                    )
                                )
                            )
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF047857).copy(alpha = 0.18f),
                                border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f)),
                                modifier = Modifier.size(50.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("🛡️", fontSize = 24.sp)
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Islamic Context & Verify",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF047857)
                                    ) {
                                        Text(
                                            text = "AI ফ্ল্যাগশিপ",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "See the claim. Check the evidence. Understand the context.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color(0xFF047857),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "হোয়াটসঅ্যাপ ও সোশ্যাল মিডিয়ার ধর্মীয় দাবি কুরআন ও সহীহ হাদিস দ্বারা এক ট্যাপে যাচাই করুন",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 11.sp,
                                    maxLines = 2,
                                    lineHeight = 16.sp,
                                    fontFamily = banglaFont
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF047857),
                                modifier = Modifier.size(32.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "Open",
                                        tint = Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // GRID ITEMS: Exactly 3 cards per row
            items(filteredFeatures, key = { it.id }) { item ->
                ToolGridCard(
                    item = item,
                    banglaFont = banglaFont,
                    onClick = {
                        // Clicking opens the card to display everything inside it
                        selectedFeatureForDetails = item
                    }
                )
            }
        }

        // FEATURE DETAILS BOTTOM SHEET: Displays "everything now written on it" inside the card
        if (selectedFeatureForDetails != null) {
            val feature = selectedFeatureForDetails!!
            ModalBottomSheet(
                onDismissRequest = { selectedFeatureForDetails = null },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Row: Emblem + Title + Badge
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = feature.softContainerColor,
                            border = BorderStroke(1.dp, feature.primaryColor.copy(alpha = 0.35f)),
                            modifier = Modifier.size(54.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(feature.emoji, fontSize = 26.sp)
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = feature.primaryColor.copy(alpha = 0.12f),
                                modifier = Modifier.padding(bottom = 4.dp)
                            ) {
                                Text(
                                    text = feature.badgeBn,
                                    color = feature.primaryColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Text(
                                text = feature.fullNameBn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Subtitle
                    Text(
                        text = feature.subtitleBn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = feature.primaryColor,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Full Detailed Description (Everything written previously)
                    Text(
                        text = "ফিচারের বিস্তারিত বিবরণ:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = feature.descriptionBn,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Highlights Pills (if available)
                    if (feature.highlights.isNotEmpty()) {
                        Text(
                            text = "প্রধান বৈশিষ্ট্যসমূহ:",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            feature.highlights.forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = feature.softContainerColor,
                                    border = BorderStroke(0.6.dp, feature.primaryColor.copy(alpha = 0.25f))
                                ) {
                                    Text(
                                        text = "✦ $tag",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = feature.primaryColor,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(18.dp))
                    }

                    // Main Action Button
                    Button(
                        onClick = {
                            val action = feature.onClick
                            selectedFeatureForDetails = null
                            action()
                        },
                        enabled = feature.isAvailable,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = feature.primaryColor,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(feature.emoji, fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (feature.isAvailable) "ফিচারে প্রবেশ করুন" else "শীঘ্রই উন্মুক্ত হবে",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (feature.isAvailable) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                        if (feature.isAvailable) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }
            }
        }

        // Interactive Zakat Calculator Dialog
        if (showZakatDialog) {
            ZakatCalculatorDialog(
                onDismiss = { showZakatDialog = false },
                banglaFont = banglaFont
            )
        }
    }
}

/**
 * Individual Card in the 3-Column Grid.
 * Displays strictly TWO things as requested:
 * 1. Logo / Picture (Icon / Emblem)
 * 2. Feature Name
 * Styled in eye-soothing, beautiful colors.
 */
@Composable
private fun ToolGridCard(
    item: ToolFeatureItem,
    banglaFont: androidx.compose.ui.text.font.FontFamily?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, item.primaryColor.copy(alpha = 0.22f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp),
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.92f) // Beautiful proportion for 3-in-a-row
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            item.softContainerColor.copy(alpha = 0.85f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
                .padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 1. LOGO / PICTURE (Emblem Container)
            Surface(
                shape = CircleShape,
                color = item.softContainerColor,
                border = BorderStroke(1.dp, item.primaryColor.copy(alpha = 0.35f)),
                modifier = Modifier.size(46.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = item.emoji,
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(7.dp))

            // 2. FEATURE NAME
            Text(
                text = item.nameBn,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 15.sp,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

/**
 * Interactive Zakat Calculator Dialog with Shariah Nisab calculation
 */
@Composable
private fun ZakatCalculatorDialog(
    onDismiss: () -> Unit,
    banglaFont: androidx.compose.ui.text.font.FontFamily?
) {
    var goldAmountStr by remember { mutableStateOf("") }
    var silverAmountStr by remember { mutableStateOf("") }
    var cashAmountStr by remember { mutableStateOf("") }
    var businessGoodsStr by remember { mutableStateOf("") }
    var debtStr by remember { mutableStateOf("") }

    val goldVal = goldAmountStr.toDoubleOrNull() ?: 0.0
    val silverVal = silverAmountStr.toDoubleOrNull() ?: 0.0
    val cashVal = cashAmountStr.toDoubleOrNull() ?: 0.0
    val businessVal = businessGoodsStr.toDoubleOrNull() ?: 0.0
    val debtVal = debtStr.toDoubleOrNull() ?: 0.0

    val totalAssets = (goldVal + silverVal + cashVal + businessVal) - debtVal
    val zakatPayable = if (totalAssets > 0) totalAssets * 0.025 else 0.0

    val formatter = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "যাকাতুল মাল ক্যালকুলেটর",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "এক বছর সঞ্চিত উদ্বৃত্ত সম্পদের ওপর শরীয়াহ অনুযায়ী ২.৫% যাকাত প্রযোজ্য।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                OutlinedTextField(
                    value = cashAmountStr,
                    onValueChange = { cashAmountStr = it },
                    label = { Text("নগদ অর্থ ও ব্যাংক ব্যালেন্স (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = goldAmountStr,
                    onValueChange = { goldAmountStr = it },
                    label = { Text("স্বর্ণের বর্তমান মূল্য (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = businessGoodsStr,
                    onValueChange = { businessGoodsStr = it },
                    label = { Text("ব্যবসায়িক পণ্যের মূল্য (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = debtStr,
                    onValueChange = { debtStr = it },
                    label = { Text("তাত্ক্ষণিক পরিশোধযোগ্য ঋণ (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Calculated Results Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    ),
                    border = BorderStroke(1.dp, IslamicGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "মোট যাকাতযোগ্য সম্পদ:",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "৳ ${formatter.format(totalAssets.coerceAtLeast(0.0))}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "প্রদেয় যাকাত (২.৫%):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "৳ ${formatter.format(zakatPayable)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
            ) {
                Text("সম্পন্ন", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
            }
        }
    )
}
