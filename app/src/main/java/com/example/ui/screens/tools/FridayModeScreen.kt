package com.example.ui.screens.tools

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.FridaySpecialDuaAmolData
import com.example.data.model.IslamicLifeCardItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily
import java.text.SimpleDateFormat
import java.util.*

enum class FridayTab(val titleBn: String, val icon: String) {
    OVERVIEW("সংক্ষিপ্ত গাইড", "🕌"),
    SURAH_KAHF("সূরা আল-কাহফ", "📖"),
    SUNNAHS("জুমার সুন্নাত", "🌿"),
    CHECKLIST("আমল চেকলিস্ট", "✅"),
    KHUTBAH_NOTES("খুতবার নোটস", "📝"),
    SAAT_IJABAH("দোয়া কবুলের সময়", "🤲"),
    CHARITY("সাদাকাহ", "💰"),
    HADITHS("সহীহ হাদীস", "📜"),
    ALL_AMOLS("সকল দো'আ ও আমল", "💎")
}

data class KhutbahNote(
    val id: String = UUID.randomUUID().toString(),
    val date: String,
    val mosqueName: String,
    val topic: String,
    val keyPoints: String,
    val actionPlan: String
)

@Composable
fun FridayModeScreen(
    onNavigateBack: () -> Unit,
    onOpenHolyQuranSurah18: () -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = remember {
        try {
            androidx.compose.ui.text.font.Font(com.example.R.font.font_arabic_amiri)
                .let { FontFamily(it) }
        } catch (_: Exception) {
            FontFamily.Default
        }
    }

    val cal = remember { Calendar.getInstance() }
    val isTodayFriday = remember { cal.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY }
    val todayDateFormatted = remember {
        SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("bn", "BD")).format(Date())
    }

    var selectedTab by remember { mutableStateOf(FridayTab.OVERVIEW) }

    // Persistent checklist state via SharedPreferences
    val prefs = remember { context.getSharedPreferences("friday_mode_prefs", Context.MODE_PRIVATE) }
    val todayKey = remember { SimpleDateFormat("yyyy_MM_dd", Locale.US).format(Date()) }

    var checkedItems by remember {
        val savedSet = prefs.getStringSet("checklist_$todayKey", emptySet()) ?: emptySet()
        mutableStateOf(savedSet)
    }

    var surahKahfCompleted by remember {
        mutableStateOf(prefs.getBoolean("kahf_completed_$todayKey", false))
    }

    var charityGiven by remember {
        mutableStateOf(prefs.getBoolean("charity_given_$todayKey", false))
    }

    // Notes storage
    var notesList by remember {
        val raw = prefs.getString("khutbah_notes_json", "") ?: ""
        val list = mutableListOf<KhutbahNote>()
        if (raw.isNotBlank()) {
            try {
                val array = org.json.JSONArray(raw)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    list.add(
                        KhutbahNote(
                            id = obj.optString("id", UUID.randomUUID().toString()),
                            date = obj.optString("date", ""),
                            mosqueName = obj.optString("mosqueName", ""),
                            topic = obj.optString("topic", ""),
                            keyPoints = obj.optString("keyPoints", ""),
                            actionPlan = obj.optString("actionPlan", "")
                        )
                    )
                }
            } catch (_: Exception) {}
        }
        mutableStateOf(list.toList())
    }

    fun saveNotes(newList: List<KhutbahNote>) {
        notesList = newList
        val array = org.json.JSONArray()
        newList.forEach { n ->
            val obj = org.json.JSONObject()
            obj.put("id", n.id)
            obj.put("date", n.date)
            obj.put("mosqueName", n.mosqueName)
            obj.put("topic", n.topic)
            obj.put("keyPoints", n.keyPoints)
            obj.put("actionPlan", n.actionPlan)
            array.put(obj)
        }
        prefs.edit().putString("khutbah_notes_json", array.toString()).apply()
    }

    fun toggleChecklistItem(id: String) {
        val next = checkedItems.toMutableSet()
        if (next.contains(id)) next.remove(id) else next.add(id)
        checkedItems = next
        prefs.edit().putStringSet("checklist_$todayKey", next).apply()
    }

    val handleBackNavigation = {
        if (selectedTab != FridayTab.OVERVIEW) {
            selectedTab = FridayTab.OVERVIEW
        } else {
            onNavigateBack()
        }
    }

    BackHandler(onBack = handleBackNavigation)

    Scaffold(
        topBar = {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = handleBackNavigation) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Friday Mode (জুমার মোড)",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isTodayFriday) Color(0xFF047857) else IslamicGold
                                ) {
                                    Text(
                                        text = if (isTodayFriday) "আজ জুমা" else "প্রস্তুতি",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = todayDateFormatted,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    // Navigation Tabs Pill Row
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab.ordinal,
                        edgePadding = 12.dp,
                        containerColor = Color.Transparent,
                        divider = {}
                    ) {
                        FridayTab.entries.forEach { tab ->
                            Tab(
                                selected = selectedTab == tab,
                                onClick = { selectedTab = tab },
                                text = {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(tab.icon, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = tab.titleBn,
                                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Normal,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                FridayTab.OVERVIEW -> FridayOverviewContent(
                    isTodayFriday = isTodayFriday,
                    surahKahfCompleted = surahKahfCompleted,
                    charityGiven = charityGiven,
                    checkedCount = checkedItems.size,
                    totalCount = 10,
                    onSelectTab = { selectedTab = it },
                    onOpenHolyQuranSurah18 = onOpenHolyQuranSurah18,
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
                FridayTab.SURAH_KAHF -> FridaySurahKahfContent(
                    isCompleted = surahKahfCompleted,
                    onToggleComplete = {
                        surahKahfCompleted = !surahKahfCompleted
                        prefs.edit().putBoolean("kahf_completed_$todayKey", surahKahfCompleted).apply()
                    },
                    onOpenQuranReader = onOpenHolyQuranSurah18,
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
                FridayTab.SUNNAHS -> FridaySunnahsContent(
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
                FridayTab.CHECKLIST -> FridayChecklistContent(
                    checkedItems = checkedItems,
                    onToggleItem = ::toggleChecklistItem,
                    banglaFont = banglaFont
                )
                FridayTab.KHUTBAH_NOTES -> FridayKhutbahNotesContent(
                    notes = notesList,
                    onSaveNote = { newNote ->
                        saveNotes(listOf(newNote) + notesList)
                    },
                    onDeleteNote = { id ->
                        saveNotes(notesList.filterNot { it.id == id })
                    },
                    banglaFont = banglaFont
                )
                FridayTab.SAAT_IJABAH -> FridaySaatIjabahContent(
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
                FridayTab.CHARITY -> FridayCharityContent(
                    charityGiven = charityGiven,
                    onToggleCharity = {
                        charityGiven = !charityGiven
                        prefs.edit().putBoolean("charity_given_$todayKey", charityGiven).apply()
                    },
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
                FridayTab.HADITHS -> FridayHadithsContent(
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
                FridayTab.ALL_AMOLS -> FridayAllAmolsContent(
                    banglaFont = banglaFont,
                    arabicFont = arabicFont
                )
            }
        }
    }
}

@Composable
fun FridayOverviewContent(
    isTodayFriday: Boolean,
    surahKahfCompleted: Boolean,
    charityGiven: Boolean,
    checkedCount: Int,
    totalCount: Int,
    onSelectTab: (FridayTab) -> Unit,
    onOpenHolyQuranSurah18: () -> Unit,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isTodayFriday) Color(0xFF064E3B) else Color(0xFF1E293B)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = if (isTodayFriday) listOf(
                                    Color(0xFF064E3B),
                                    Color(0xFF047857),
                                    Color(0xFF0D9488)
                                ) else listOf(
                                    Color(0xFF1E293B),
                                    Color(0xFF334155),
                                    Color(0xFF475569)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isTodayFriday) "🕌 সাইয়্যিদুল আইয়্যাম (দিনের সরদার)" else "শুক্রবার প্রস্তুতি ও আমল গাইড",
                                color = IslamicGold,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                            Surface(
                                shape = CircleShape,
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text("✨", fontSize = 16.sp)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "خَيْرُ يَوْمٍ طَلَعَتْ عَلَيْهِ الشَّمْسُ يَوْمُ الْجُمُعَةِ",
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium,
                            fontFamily = arabicFont,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "\"সূর্য উদিত হওয়া দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন।\" (সহীহ মুসলিম: ৮৫৪)",
                            color = Color.White.copy(alpha = 0.9f),
                            style = MaterialTheme.typography.bodySmall,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Stats Summary Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("সূরা কাহাফ", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                                    Text(
                                        text = if (surahKahfCompleted) "পঠিত ✓" else "বাকি আছে",
                                        color = if (surahKahfCompleted) Color(0xFF6EE7B7) else Color(0xFFFDE68A),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("চেকলিস্ট", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                                    Text(
                                        text = "$checkedCount/$totalCount সম্পন্ন",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.15f),
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text("সাদাকাহ", color = Color.White.copy(alpha = 0.8f), style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                                    Text(
                                        text = if (charityGiven) "আদায় ✓" else "পরিকল্পনা",
                                        color = if (charityGiven) Color(0xFF6EE7B7) else Color(0xFFFDE68A),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Quick Access Grid Cards
        item {
            Text(
                text = "জুমার প্রধান কার্যক্রম ও আমলসমূহ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionFeatureCard(
                    icon = "📖",
                    title = "সূরা আল-কাহফ",
                    desc = "নূর ও দাজ্জাল মুক্তি",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(FridayTab.SURAH_KAHF) },
                    banglaFont = banglaFont
                )
                QuickActionFeatureCard(
                    icon = "🌿",
                    title = "জুমার সুন্নাত",
                    desc = "গোসল, সুগন্ধি ও আদব",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(FridayTab.SUNNAHS) },
                    banglaFont = banglaFont
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionFeatureCard(
                    icon = "🤲",
                    title = "সা'আতুল ইজাবাহ",
                    desc = "দোয়া কবুলের বিশেষ ক্ষণ",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(FridayTab.SAAT_IJABAH) },
                    banglaFont = banglaFont
                )
                QuickActionFeatureCard(
                    icon = "📝",
                    title = "খুতবার নোটস",
                    desc = "খতীবের আলোচনা লিপিবদ্ধ",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(FridayTab.KHUTBAH_NOTES) },
                    banglaFont = banglaFont
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickActionFeatureCard(
                    icon = "✅",
                    title = "আমল চেকলিস্ট",
                    desc = "ধাপে ধাপে জুমার আমল",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(FridayTab.CHECKLIST) },
                    banglaFont = banglaFont
                )
                QuickActionFeatureCard(
                    icon = "💰",
                    title = "জুমার সাদাকাহ",
                    desc = "সপ্তাহের শ্রেষ্ঠ দান",
                    modifier = Modifier.weight(1f),
                    onClick = { onSelectTab(FridayTab.CHARITY) },
                    banglaFont = banglaFont
                )
            }
        }

        // 1-Tap Read in Quran Reader Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "আল-কুরআন থেকে পূর্ণ সূরা কাহাফ",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "হরকতযুক্ত আরবী, বাংলা অনুবাদ, অডিও তিলাওয়াত ও তাফসীর সহ ১-১১০ আয়াত পড়ুন।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                    Button(
                        onClick = onOpenHolyQuranSurah18,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text("পড়ুন 📖", fontFamily = banglaFont, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun QuickActionFeatureCard(
    icon: String,
    title: String,
    desc: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    banglaFont: FontFamily
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(icon, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont,
                maxLines = 2
            )
        }
    }
}

// -------------------------------------------------------------------------
// 2. SURAH AL-KAHF CONTENT
// -------------------------------------------------------------------------
@Composable
fun FridaySurahKahfContent(
    isCompleted: Boolean,
    onToggleComplete: () -> Unit,
    onOpenQuranReader: () -> Unit,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    var selectedSection by remember { mutableStateOf(0) } // 0: ১ম ১০ আয়াত, 1: শেষ ১০ আয়াত, 2: ফজিলত ও তাৎপর্য

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tracker Header Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) Color(0xFF064E3B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.5.dp, if (isCompleted) Color(0xFF10B981) else IslamicGold.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isCompleted) "আলহামদুলিল্লাহ! সূরা কাহাফ পাঠ সম্পন্ন ✓" else "আজকের সূরা কাহাফ পাঠ ট্র্যাকার",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = if (isCompleted) "আল্লাহ আপনার পাঠ কবুল করুন ও নূর দ্বারা ধন্য করুন।" else "জুমার দিনে সূরা কাহাফ পাঠে দুই জুমার মধ্যবর্তী সময় নূরে উদ্ভাসিত থাকে।",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isCompleted) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                    Switch(
                        checked = isCompleted,
                        onCheckedChange = { onToggleComplete() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF10B981)
                        )
                    )
                }
            }
        }

        // Full Reader Launcher
        item {
            Button(
                onClick = onOpenQuranReader,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "আল-কুরআন থেকে পূর্ণ ১-১১০ আয়াত অডিও সহ পড়ুন",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontFamily = banglaFont
                )
            }
        }

        // Sub Tab Selector
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("১ম ১০ আয়াত (দাজ্জাল রক্ষা)", "শেষ ১০ আয়াত (১০১-১১০)", "ফজিলত ও শিক্ষা").forEachIndexed { idx, label ->
                    FilterChip(
                        selected = selectedSection == idx,
                        onClick = { selectedSection = idx },
                        label = { Text(label, fontSize = 11.sp, fontFamily = banglaFont) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
        }

        when (selectedSection) {
            0 -> {
                // First 10 Ayahs
                item {
                    AyahHighlightCard(
                        ayahNumber = "১",
                        arabic = "الْحَمْدُ لِلَّهِ الَّذِي أَنزَلَ عَلَىٰ عَبْدِهِ الْكِتَابَ وَلَمْ يَجْعَل لَّهُ عِوَجًا ۜ",
                        pronunciation = "আলহামদু লিল্লা-হিল্লাযী আনযালা ‘আলা- ‘আবদিহিল কিতা-বা ওয়া লাম ইয়াজ‘আল লাহূ ‘ইওয়াজা-।",
                        meaning = "সমস্ত প্রশংসা আল্লাহর জন্য যিনি তাঁর বান্দার প্রতি কিতাব অবতীর্ণ করেছেন এবং তাতে কোনো বক্রতা রাখেননি।",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
                item {
                    AyahHighlightCard(
                        ayahNumber = "২",
                        arabic = "قَيِّمًا لِّيُنذِرَ بَأْسًا شَدِيدًا مِّن لَّدُنْهُ وَيُبَشِّرَ الْمُؤْمِنِينَ الَّذِينَ يَعْمَلُونَ الصَّالِحَاتِ أَنَّ لَهُمْ أَجْرًا حَسَنًا",
                        pronunciation = "ক্বাইয়্যিমাল লিইউনযিরা বা’সান শাদীদাম মিল্লাদুনহু ওয়া ইউবাশশিরাল মু’মিনীনা আল্লাযীনা ইয়া‘মালূনাছ ছা-লিহা-তি আন্না লাহুম আজরান হাসানা-।",
                        meaning = "সুদৃঢ় ও সুপ্রতিষ্ঠিত কিতাব, যাতে তিনি তাঁর পক্ষ থেকে এক কঠিন শাস্তির ভীতি প্রদর্শন করেন এবং যারা সৎকর্ম করে সেই মুমিনদের সুসংবাদ দেন যে তাদের জন্য রয়েছে সর্বোত্তম প্রতিদান।",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
                item {
                    AyahHighlightCard(
                        ayahNumber = "৩-৪",
                        arabic = "مَّاكِثِينَ فِيهِ أَبَدًا ۝ وَيُنذِرَ الَّذِينَ قَالُوا اتَّخَذَ اللَّهُ وَلَدًا",
                        pronunciation = "মা-কিছীনা ফীহি আবাদা-। ওয়া ইউনযিরাল্লাযীনা ক্বা-লুত্তাখাযাল্লা-হু ওয়ালাদা-।",
                        meaning = "তারা সেখানে চিরকাল অবস্থান করবে; এবং যেন সতর্ক করে তাদের, যারা বলে: আল্লাহ সন্তান গ্রহণ করেছেন।",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
                item {
                    AyahHighlightCard(
                        ayahNumber = "১০",
                        arabic = "إِذْ أَوَى الْفِتْيَةُ إِلَى الْكَهْفِ فَقَالُوا رَبَّنَا آتِنَا مِن لَّدُنكَ رَحْمَةً وَهَيِّئْ لَنَا مِنْ أَمْرِنَا رَشَدًا",
                        pronunciation = "ইয আওয়াল ফিতইয়াতু ইলাল কাহফি ফাক্বা-লূ রাব্বানা- আ-তিনা- মিল্লাদুনকা রাহমাতাওঁ ওয়া হাইয়্যি’ লানা- মিন আমরিনা- রাশাদা-।",
                        meaning = "যখন যুবকেরা গুহায় আশ্রয় গ্রহণ করল এবং বলল: হে আমাদের প্রতিপালক! আপনার পক্ষ থেকে আমাদেরকে রহমত দান করুন এবং আমাদের কর্মকাণ্ড সঠিক পথে পরিচালিত করার ব্যবস্থা করে দিন!",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
            }
            1 -> {
                // Last 10 Ayahs Highlights
                item {
                    AyahHighlightCard(
                        ayahNumber = "১০৭",
                        arabic = "إِنَّ الَّذِينَ آمَنُوا وَعَمِلُوا الصَّالِحَاتِ كَانَتْ لَهُمْ جَنَّاتُ الْفِرْدَوْسِ نُزُلًا",
                        pronunciation = "ইন্নাল্লাযীনা আ-মানূ ওয়া ‘আমিলুছ ছা-লিহা-তি কা-নাত লাহুম জান্না-তুল ফিরদাউসি নুযুলা-।",
                        meaning = "নিশ্চয় যারা ঈমান আনে এবং সৎকর্ম করে, তাদের আপ্যায়নের জন্য রয়েছে জান্নাতুল ফিরদাউস।",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
                item {
                    AyahHighlightCard(
                        ayahNumber = "১০৯",
                        arabic = "قُل لَّوْ كَانَ الْبَحْرُ مِدَادًا لِّكَلِمَاتِ رَبِّي لَنَفِدَ الْبَحْرُ قَبْلَ أَن تَنفَدَ كَلِمَاتُ رَبِّي وَلَوْ جِئْنَا بِمِثْلِهِ مَدَدًا",
                        pronunciation = "ক্বুল লাও কা-নাল বাহরু মিদা-দাল লিকালিমা-তি রাব্বী লানাফিদাল বাহরু ক্বাবলা আন তানফাদা কালিমা-তু রাব্বী ওয়া লাও জি’না- বিমিছলিহী মাদাদা-।",
                        meaning = "বলুন: আমার রবের বাণীসমূহ লেখার জন্য যদি সমুদ্র কালি হয়ে যায়, তবে আমার রবের বাণী নিঃশেষ হওয়ার পূর্বেই সমুদ্র নিঃশেষ হয়ে যাবে—যদিও আমি এর সাহায্যার্থে অনুরূপ আরও সমুদ্র নিয়ে আসি।",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
                item {
                    AyahHighlightCard(
                        ayahNumber = "১১০",
                        arabic = "قُلْ إِنَّمَا أَنَا بَشَرٌ مِّثْلُكُمْ يُوحَىٰ إِلَيَّ أَنَّمَا إِلَٰهُكُمْ إِلَٰهٌ وَاحِدٌ ۖ فَمَن كَانَ يَرْجُو لِقَاءَ رَبِّهِ فَلْيَعْمَلْ عَمَلًا صَالِحًا وَلَا يُشْرِكْ بِعِبَادَةِ رَبِّهِ أَحَدًا",
                        pronunciation = "ক্বুল ইন্নামা- আনা বাশারুম মিছলুকুম ইয়ূহো- ইলাইয়্যা আন্নামা- ইলা-হুকুম ইলা-হুওঁ ওয়া-হিদ, ফামান কা-না ইয়ারজূ লিক্বা-আ রাব্বিহী ফালইয়া‘মাল ‘আমালান ছা-লিহাওঁ ওয়ালা- ইউশরিক বি‘ইবা-দাতি রাব্বিহী আহাদা-।",
                        meaning = "বলুন: আমি তো তোমাদের মতোই একজন মানুষ, আমার প্রতি ওহী পাঠানো হয় যে, তোমাদের উপাস্য তো এক উপাস্য। সুতরাং যে ব্যক্তি তার প্রতিপালকের সাক্ষাত কামনা করে, সে যেন সৎকর্ম করে এবং তার প্রতিপালকের ইবাদাতে কাউকে অংশীদার না করে।",
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
            }
            else -> {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "সূরা আল-কাহফের সহীহ ফজিলত ও উৎসসমূহ:",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "১. নূর প্রজ্বলিত থাকা:\nআবূ সাঈদ আল-খুদরী (রাঃ) থেকে বর্ণিত, রাসূলুল্লাহ ﷺ বলেছেন: ‘যে ব্যক্তি জুমার দিনে সূরা আল-কাহফ পাঠ করবে, তার জন্য এক জুমা থেকে অপর জুমা পর্যন্ত নূর প্রজ্বলিত থাকবে।’\n(মুসতাদরাক হাকিম: ৩৩৯২, সহীহ আত-তারগীব: ৭৩৬ — আলবানী সহীহ বলেছেন)।\n\n২. দাজ্জালের ফিতনা থেকে নিরাপত্তা:\nআবূ দারদা (রাঃ) থেকে বর্ণিত, নবী ﷺ বলেছেন: ‘যে ব্যক্তি সূরা আল-কাহফের প্রথম দশটি আয়াত মুখস্থ করবে, সে দাজ্জালের ফিতনা থেকে নিরাপদ থাকবে।’ (সহীহ মুসলিম: ৮০৯; সুনান আবু দাউদ: ৪৩২৩)।\nঅন্য বর্ণনায় শেষ দশটি আয়াতের কথাও এসেছে (সহীহ মুসলিম: ৮০৯/খ)।\n\n৩. পাঠের সময়সীমা:\nবৃহস্পতিবার সূর্যাস্তের পর (শুক্রবার রাত) থেকে শুক্রবার সূর্যাস্ত (মাগরিব) পর্যন্ত যেকোনো সময়ে সূরা কাহাফ পাঠ করা যায়।",
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AyahHighlightCard(
    ayahNumber: String,
    arabic: String,
    pronunciation: String,
    meaning: String,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "আয়াত $ayahNumber",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                    )
                }
                Text("সূরা ১৮: কাহাফ", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant, fontFamily = banglaFont)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = arabic,
                style = MaterialTheme.typography.titleMedium.copy(lineHeight = 32.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = arabicFont,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = pronunciation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = meaning,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
    }
}

// -------------------------------------------------------------------------
// 3. FRIDAY SUNNAHS
// -------------------------------------------------------------------------
data class FridaySunnahModel(
    val id: String,
    val title: String,
    val arabicQuote: String,
    val details: String,
    val reference: String
)

@Composable
fun FridaySunnahsContent(
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    val sunnahs = remember {
        listOf(
            FridaySunnahModel(
                id = "s1",
                title = "১. উত্তমরূপে গোসল করা (Ghusl)",
                arabicQuote = "غُسْلُ يَوْمِ الْجُمُعَةِ وَاجِبٌ عَلَى كُلِّ مُحْتَلِمٍ",
                details = "জুমার দিনে প্রাপ্তবয়স্ক প্রত্যেক মুসলমানের জন্য গোসল করা সুন্নাতে মুআক্কাদাহ (কিছু ফকীহের মতে ওয়াজিবতুল্য)। এর মাধ্যমে শরীর পরিচ্ছন্ন ও দুর্গন্ধমুক্ত হয়।",
                reference = "সহীহ বুখারী: ৮৫৮, ৮৭৯; সহীহ মুসলিম: ৮৪৬"
            ),
            FridaySunnahModel(
                id = "s2",
                title = "২. মিসওয়াক করা ও পরিষ্কার-পরিচ্ছন্ন হওয়া",
                arabicQuote = "السِّوَاكُ مَطْهَرَةٌ لِلْفَمِ مَرْضَاةٌ لِلرَّبِّ",
                details = "মুখের দুর্গন্ধ দূর ও জিহ্বার পবিত্রতার জন্য মিসওয়াক করা। জুমার দিন মেসওয়াক করা বিশেষভাবে তাকিদপূর্ণ সুন্নাত।",
                reference = "সুনান আন-নাসাঈ: ৫; সহীহ বুখারী: ৮৮০"
            ),
            FridaySunnahModel(
                id = "s3",
                title = "৩. পরিচ্ছন্ন ও মার্জিত উত্তম পোশাক পরিধান করা",
                arabicQuote = "مَا عَلَى أَحَدِكُمْ إِنْ وَجَدَ أَنْ يَتَّخِذَ ثَوْبَيْنِ لِيَوْمِ الْجُمُعَةِ سِوَى ثَوْبَيْ مِهْنَتِهِ",
                details = "কাজের কাপড়ের বাইরে জুমার সালাতের জন্য আলাদা পরিচ্ছন্ন সাদা বা উত্তম পোশাক নির্ধারণ করে রাখা রাসূলুল্লাহ ﷺ-এর সুন্নাত।",
                reference = "সুনান আবু দাউদ: ১০৭৮; সুনান ইবনে মাজাহ: ১০৯৫"
            ),
            FridaySunnahModel(
                id = "s4",
                title = "৪. তেল ও সুগন্ধি (আতর) ব্যবহার করা",
                arabicQuote = "وَيَمَسَّ مِنَ الطِّيبِ مَا قَدَرَ عَلَيْهِ",
                details = "সাধ্যমতো সুগন্ধি বা আতর ও মাথায় তেল ব্যবহার করা যাতে মসজিদের পরিবেশ সুরভিত ও প্রশান্তিময় থাকে।",
                reference = "সহীহ বুখারী: ৮৮৩"
            ),
            FridaySunnahModel(
                id = "s5",
                title = "৫. সকাল সকাল পায়ে হেঁটে মসজিদে যাওয়া",
                arabicQuote = "مَنْ غَسَّلَ يَوْمَ الْجُمُعَةِ وَاغْتَسَلَ، ثُمَّ بَكَّرَ وَابْتَكَرَ، وَمَشَى وَلَمْ يَرْكَبْ",
                details = "সকালে তাড়াতাড়ি মসজিদে রওনা হওয়া এবং সওয়ারিতে না চড়ে পায়ে হেঁটে মসজিদে যাওয়ার ক্ষেত্রে প্রতি পদক্ষেপে ১ বছরের নফল রোজা ও ১ বছরের তাহাজ্জুদের সওয়াব লাভের অনন্য ঘোষণা এসেছে।",
                reference = "জামে আত-তিরমিযী: ৪৯৬; সুনান আবু দাউদ: ৩৪৫ (সহীহ)"
            ),
            FridaySunnahModel(
                id = "s6",
                title = "৬. মুসল্লিদের কাঁধ বা ঘাড় না ডিঙিয়ে বসা",
                arabicQuote = "فَلَمْ يُفَرِّقْ بَيْنَ اثْنَيْنِ",
                details = "দেরিতে গিয়ে সামনের কাতারে যাওয়ার জন্য মুসল্লিদের ঘাড় ডিঙিয়ে বা কষ্ট দিয়ে যাওয়া কঠোরভাবে নিষিদ্ধ। যেখানে খালি জায়গা পাওয়া যায় সেখানেই ভদ্রভাবে বসা।",
                reference = "সহীহ বুখারী: ৮৮৩, ৯১১"
            ),
            FridaySunnahModel(
                id = "s7",
                title = "৭. তাহিয়্যাতুল মসজিদ ২ রাকাত সালাত আদায় করা",
                arabicQuote = "إِذَا جَاءَ أَحَدُكُمْ يَوْمَ الْجُمُعَةِ وَالإِمَامُ يَخْطُبُ فَلْيَرْكَعْ رَكْعَتَيْنِ",
                details = "মসজিদে প্রবেশ করে না বসে প্রথমে সংক্ষেপে দুই রাকাত তাহিয়্যাতুল মসজিদ আদায় করা। এমনকি ইমাম খুতবারত থাকলেও সংক্ষেপে এই দুই রাকাত পড়ার স্পষ্ট নববী নির্দেশ রয়েছে।",
                reference = "সহীহ মুসলিম: ৮৭৫; সহীহ বুখারী: ১১৬৬"
            ),
            FridaySunnahModel(
                id = "s8",
                title = "৮. সম্পূর্ণ নীরব থেকে গভীর মনোযোগে খুতবা শোনা",
                arabicQuote = "إِذَا قُلْتَ لِصَاحِبِكَ يَوْمَ الْجُمُعَةِ أَنْصِتْ وَالإِمَامُ يَخْطُبُ فَقَدْ لَغَوْتَ",
                details = "খুতবা চলাকালীন কোনো প্রকার কথা বলা, এমনকি অন্য কাউকে 'চুপ কর' বলা অথবা মোবাইলে বার্তা দেখা বা টেপা সম্পূর্ণ নিষিদ্ধ। এমন করলে জুমার মর্যাদা বিনষ্ট হয়ে সাধারণ সালাতে পরিণত হয়।",
                reference = "সহীহ বুখারী: ৯৩৪; সহীহ মুসলিম: ৮৫১"
            ),
            FridaySunnahModel(
                id = "s9",
                title = "৯. বেশি বেশি দরূদ শরীফ পাঠ করা",
                arabicQuote = "فَأَكْثِرُوا عَلَيَّ مِنَ الصَّلَاةِ فِيهِ، فَإِنَّ صَلَاتَكُمْ مَعْرُوضَةٌ عَلَيَّ",
                details = "জুমার দিন ও রাতে প্রিয় নবী মুহাম্মদ ﷺ-এর ওপর অধিক পরিমাণে দরূদ পাঠের সরাসরি নির্দেশ এসেছে। কারণ এই দিনে দরূদ তাঁর সামনে সরাসরি পেশ করা হয়।",
                reference = "সুনান আবু দাউদ: ১০৪৭; সুনান আন-নাসাঈ: ১৩৭৪ (সনদ সহীহ)"
            ),
            FridaySunnahModel(
                id = "s10",
                title = "১০. আসরের পর মাগরিব পর্যন্ত সা'আতুল ইজাবাহ সময়ে দু'আ করা",
                arabicQuote = "فَالْتَمِسُوهَا آخِرَ سَاعَةٍ بَعْدَ الْعَصْرِ",
                details = "জুমার দিন দো'আ কবুলের যে বিশেষ মুহূর্ত রয়েছে, তা আসরের পর শেষ প্রহরে অনুসন্ধান করা এবং একান্ত মনে আল্লাহর নিকট দো'আ করা।",
                reference = "সুনান আবু দাউদ: ১০৪৮; সুনান আন-নাসাঈ: ১৩৮৯"
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "জুমার দিন পালনীয় ১০টি মৌলিক সুন্নাত ও আদব",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }

        items(sunnahs) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.arabicQuote,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = arabicFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.details,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "রেফারেন্স: ${item.reference}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. FRIDAY CHECKLIST
// -------------------------------------------------------------------------
data class FridayChecklistItem(
    val id: String,
    val title: String,
    val desc: String,
    val timeSlot: String
)

@Composable
fun FridayChecklistContent(
    checkedItems: Set<String>,
    onToggleItem: (String) -> Unit,
    banglaFont: FontFamily
) {
    val items = remember {
        listOf(
            FridayChecklistItem("c1", "নখ কাটা ও ব্যক্তিগত পরিচ্ছন্নতা", "হাত-পায়ের নখ ও অবাঞ্ছিত লোম পরিষ্কার করা", "সকালে"),
            FridayChecklistItem("c2", "গোসল ও সুগন্ধি ব্যবহার", "উত্তমরূপে গোসল, মেসওয়াক ও আতর ব্যবহার", "সকালে / জুমার পূর্বে"),
            FridayChecklistItem("c3", "উত্তম পরিচ্ছন্ন জামা পরিধান", "সুন্দর ও ধৌত পরিচ্ছন্ন পোশাক নির্বাচন", "জুমার পূর্বে"),
            FridayChecklistItem("c4", "সূরা আল-কাহফ তিলাওয়াত", "সম্পূর্ণ ১-১১০ আয়াত অথবা অন্তত প্রথম ও শেষ ১০ আয়াত", "ফজর থেকে আসর"),
            FridayChecklistItem("c5", "অধিক পরিমাণে দরূদ পাঠ", "আল্লাহুম্মা সাল্লি আলা মুহাম্মাদ... অন্তত ১০০+ বার", "সারাদিন"),
            FridayChecklistItem("c6", "সকালে আগে আগে মসজিদে গমন", "পায়ে হেঁটে দ্রুততম সময়ে মসজিদে উপস্থিত হওয়া", "খুতবার পূর্বে"),
            FridayChecklistItem("c7", "তাহিয়্যাতুল মসজিদ ২ রাকাত সালাত", "মসজিদে প্রবেশ করে না বসে দুই রাকাত সালাত", "মসজিদে পৌঁছার পর"),
            FridayChecklistItem("c8", "পূর্ণ মনোযোগে খুতবা শ্রবণ", "নিঃশব্দে ও একাগ্রচিত্তে খতীবের নসীহত শোনা", "খুতবার সময়"),
            FridayChecklistItem("c9", "জুমার দিনে সাদাকাহ / দান প্রদান", "গরীব-মিসকীন বা দ্বীনি ফান্ডে সাধ্যমতো দান", "জুমার সময়"),
            FridayChecklistItem("c10", "সা'আতুল ইজাবাহ সময়ে দু'আ প্রার্থনা", "আসরের পর থেকে মাগরিব পর্যন্ত একাগ্রচিত্তে মোনাজাত", "আসরের পর")
        )
    }

    val progress = if (items.isNotEmpty()) checkedItems.size.toFloat() / items.size else 0f

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Progress Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "জুমার আমল অগ্রগতি",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "${checkedItems.size}/${items.size} সম্পন্ন (${(progress * 100).toInt()}%)",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857),
                            fontFamily = banglaFont
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF047857),
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        items(items) { item ->
            val isChecked = checkedItems.contains(item.id)
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isChecked) Color(0xFF064E3B).copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isChecked) Color(0xFF047857).copy(alpha = 0.6f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleItem(item.id) }
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = { onToggleItem(item.id) },
                        colors = CheckboxDefaults.colors(checkedColor = Color(0xFF047857))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = item.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = if (isChecked) Color(0xFF047857) else MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant
                            ) {
                                Text(
                                    text = item.timeSlot,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = item.desc,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. KHUTBAH NOTES
// -------------------------------------------------------------------------
@Composable
fun FridayKhutbahNotesContent(
    notes: List<KhutbahNote>,
    onSaveNote: (KhutbahNote) -> Unit,
    onDeleteNote: (String) -> Unit,
    banglaFont: FontFamily
) {
    var showAddDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Add Note Action Button
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "খুতবার আলোচনা ও তাদাব্বুর নোটবুক",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0284C7),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "জুমার খুতবায় খতীব সাহেবের আলোচিত মূল নসীহত, আয়াত, হাদীস ও এই সপ্তাহের ব্যক্তিগত আমল পরিকল্পনা লিখে রাখুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("নতুন খুতবার নোট লিখুন 📝", fontWeight = FontWeight.Bold, color = Color.White, fontFamily = banglaFont)
                    }
                }
            }
        }

        if (notes.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📓", fontSize = 42.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "এখনো কোনো খুতবার নোট লেখা হয়নি",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        } else {
            items(notes) { note ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = note.date,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                            IconButton(onClick = { onDeleteNote(note.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.6f))
                            }
                        }
                        if (note.mosqueName.isNotBlank()) {
                            Text(
                                text = "মসজিদ: ${note.mosqueName}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = note.topic.ifBlank { "খুতবার নসীহত" },
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        if (note.keyPoints.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "মূল শিক্ষণীয় বিষয়সমূহ:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = note.keyPoints,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                        }
                        if (note.actionPlan.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF047857).copy(alpha = 0.1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp)) {
                                    Text(
                                        text = "সপ্তাহের আমল ও অঙ্গীকার:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857),
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = note.actionPlan,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        var dateInput by remember {
            mutableStateOf(SimpleDateFormat("dd MMMM yyyy", Locale("bn", "BD")).format(Date()))
        }
        var mosqueInput by remember { mutableStateOf("") }
        var topicInput by remember { mutableStateOf("") }
        var keyPointsInput by remember { mutableStateOf("") }
        var actionPlanInput by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = {
                Text("খুতবার নোট লিপিবদ্ধ করুন", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = topicInput,
                        onValueChange = { topicInput = it },
                        label = { Text("খুতবার মূল বিষয় / শিরোনাম", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = mosqueInput,
                        onValueChange = { mosqueInput = it },
                        label = { Text("মসজিদ / খতীবের নাম (ঐচ্ছিক)", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = keyPointsInput,
                        onValueChange = { keyPointsInput = it },
                        label = { Text("গুরুত্বপূর্ণ আলোচনা ও শিক্ষা", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3
                    )
                    OutlinedTextField(
                        value = actionPlanInput,
                        onValueChange = { actionPlanInput = it },
                        label = { Text("ব্যক্তিগত আমল পরিকল্পনা", fontFamily = banglaFont) },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (topicInput.isNotBlank() || keyPointsInput.isNotBlank()) {
                            onSaveNote(
                                KhutbahNote(
                                    date = dateInput,
                                    mosqueName = mosqueInput,
                                    topic = topicInput,
                                    keyPoints = keyPointsInput,
                                    actionPlan = actionPlanInput
                                )
                            )
                            showAddDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                ) {
                    Text("সংরক্ষণ করুন", fontFamily = banglaFont)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) {
                    Text("বাতিল", fontFamily = banglaFont)
                }
            }
        )
    }
}

// -------------------------------------------------------------------------
// 6. SAAT AL-IJABAH (দোয়া কবুল হওয়ার সময়)
// -------------------------------------------------------------------------
@Composable
fun FridaySaatIjabahContent(
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF064E3B)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🤲 সা'আতুল ইজাবাহ (سَاعَةُ الإِجَابَةِ)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "জুমার দিনে একটি বিশেষ মুহূর্ত রয়েছে, যখন কোনো মুসলিম বান্দা আল্লাহর নিকট কল্যাণকর যা কিছুই প্রার্থনা করে, আল্লাহ তা অবশ্যই দান করেন।",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp),
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "মুহাদ্দিসীন ও ফকীহগণের প্রামাণ্য দুটি মত:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "১. আসরের পর থেকে সূর্যাস্ত (মাগরিব) পর্যন্ত শেষ প্রহরে:\nজাবের ইবনে আবদুল্লাহ (রা.) সূত্রে রাসূলুল্লাহ ﷺ বলেন: ‘জুমার দিনে বারটি ঘণ্টা রয়েছে, তন্মধ্যে এমন একটি মুহূর্ত রয়েছে যখন কোনো মুসলিম বান্দা আল্লাহর নিকট যা চায়, আল্লাহ তাকে তা দান করেন। অতএব তোমরা আসরের শেষ প্রহরে তা অনুসন্ধান করো।’ (সুনান আবু দাউদ: ১০৪৮; সুনান নাসাঈ: ১৩৮৯ — সনদ সহীহ)। এটি জমহুর (অধিকাংশ) সাহাবী, তাবেয়ী ও ইমাম আহমাদ ইবনে হাম্বল (রহ.)-এর প্রধান অভিমত।\n\n২. ইমাম মিম্বরে বসা থেকে সালাত শেষ হওয়া পর্যন্ত:\nআবূ মূসা আল-আশআরী (রা.) সূত্রে রাসূলুল্লাহ ﷺ বলেন: ‘এই মুহূর্তটি হলো ইমাম খুতবার জন্য বসা থেকে শুরু করে সালাত শেষ হওয়া পর্যন্ত।’ (সহীহ মুসলিম: ৮৫৩)।\n\n📌 বিজ্ঞ ওলামায়ে কেরামের সমন্বয়:\nহাফেজ ইবনে হাজার আল-আসকালানী ও ইমাম নববী (রহ.) বলেন, বুদ্ধিমান মুমিনের উচিত ইমামের খুতবা ও সালাতের সময়ে মনে মনে বিনম্র থাকা এবং আসরের পর মাগরিবের পূর্বমুহূর্তে বিশেষভাবে দু'আয় আত্মনিয়োগ করা—যাতে উভয় সহীহ বর্ণনার ফযিলত অর্জন নিশ্চিত হয়।",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        item {
            Text(
                text = "জুমার দিনে পঠিতব্য বিশেষ মাসনূন দো'আসমূহ",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
        }

        item {
            DuaSnippetCard(
                title = "১. সাইয়্যিদুল ইস্তিগফার (শ্রেষ্ঠ ক্ষমাপ্রার্থনা)",
                arabic = "اللَّهُمَّ أَنْتَ رَبِّي لَا إِلَهَ إِلَّا أَنْتَ، خَلَقْتَنِي وَأَنَا عَبْدُكَ، وَأَنَا عَلَى عَهْدِكَ وَوَعْدِكَ مَا اسْتَطَعْتُ، أَعُوذُ بِكَ مِنْ شَرِّ مَا صَنَعْتُ، أَبُوءُ لَكَ بِنِعْمَتِكَ عَلَيَّ، وَأَبُوءُ لَكَ بِذَنْبِي فَاغْفِرْ لِي فَإِنَّهُ لَا يَغْفِرُ الذُّنُوبَ إِلَّا أَنْتَ",
                meaning = "হে আল্লাহ! আপনিই আমার রব, আপনি ছাড়া কোনো উপাস্য নেই। আপনি আমাকে সৃষ্টি করেছেন এবং আমি আপনার বান্দা। আমি যথাসাধ্য আপনার অঙ্গীকারে প্রতিশ্রুতিবদ্ধ...",
                reference = "সহীহ বুখারী: ৬৩০৬",
                banglaFont = banglaFont,
                arabicFont = arabicFont
            )
        }

        item {
            DuaSnippetCard(
                title = "২. জান্নাত প্রার্থনা ও জাহান্নাম থেকে মুক্তি",
                arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْجَنَّةَ، وَأَعُوذُ بِكَ مِنَ النَّارِ",
                meaning = "হে আল্লাহ! আমি আপনার নিকট জান্নাত প্রার্থনা করছি এবং জাহান্নামের আগুন থেকে আশ্রয় প্রার্থনা করছি। (৩ বার পাঠে জান্নাত ও জাহান্নাম আল্লাহর নিকট সুপারিশ করে)।",
                reference = "জামে আত-তিরমিযী: ২৫৭২; সুনান নাসাঈ: ৫৫২১",
                banglaFont = banglaFont,
                arabicFont = arabicFont
            )
        }

        item {
            DuaSnippetCard(
                title = "৩. হেদায়েত, তাকওয়া ও পবিত্রতার দো'আ",
                arabic = "اللَّهُمَّ إِنِّي أَسْأَلُكَ الْهُدَى وَالتُّقَى وَالْعَفَافَ وَالْغِنَى",
                meaning = "হে আল্লাহ! আমি আপনার নিকট হেদায়েত, তাকওয়া, পবিত্রতা এবং অন্তরের সচ্ছলতা প্রার্থনা করছি।",
                reference = "সহীহ মুসলিম: ২৭১৭",
                banglaFont = banglaFont,
                arabicFont = arabicFont
            )
        }
    }
}

@Composable
fun DuaSnippetCard(
    title: String,
    arabic: String,
    meaning: String,
    reference: String,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF047857),
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = arabic,
                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = arabicFont,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = meaning,
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "উৎস: $reference",
                style = MaterialTheme.typography.labelSmall,
                color = IslamicGold,
                fontFamily = banglaFont
            )
        }
    }
}

// -------------------------------------------------------------------------
// 7. CHARITY REMINDER
// -------------------------------------------------------------------------
@Composable
fun FridayCharityContent(
    charityGiven: Boolean,
    onToggleCharity: () -> Unit,
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (charityGiven) Color(0xFF064E3B) else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.5.dp, if (charityGiven) Color(0xFF10B981) else IslamicGold.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (charityGiven) "আজ সাদাকাহ আদায় করা হয়েছে ✓" else "আজকের জুমার সাদাকাহ ট্র্যাকার",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (charityGiven) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = if (charityGiven) "আল্লাহ আপনার সাদাকাহ কবুল করুন ও সম্পদে বরকত দান করুন।" else "জুমার দিনে সাদাকাহ করার সওয়াব অন্যান্য দিনের তুলনায় বহুগুণ বেশি।",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (charityGiven) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                    Switch(
                        checked = charityGiven,
                        onCheckedChange = { onToggleCharity() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF10B981)
                        )
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "জুমার দিনে সাদাকাহ করার বিশেষ মর্যাদা:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ইমাম ইবনুল কাইয়্যিম (রহ.) তাঁর বিশ্বখ্যাত 'যাদুল মা'আদ' কিতাবে উল্লেখ করেছেন:\n\n«الصَّدَقَةُ فِيهِ خَيْرٌ مِنَ الصَّدَقَةِ فِي غَيْرِهِ مِنَ الأَيَّامِ، وَالصَّدَقَةُ فِيهِ بِالنِّسْبَةِ إِلَى سَائِرِ أَيَّامِ الأُسْبُوعِ كَالصَّدَقَةِ فِي شَهْرِ رَمَضَانَ بِالنِّسْبَةِ إِلَى سَائِرِ الشُّهُورِ»\n\n\"জুমার দিনে দান-সাদাকাহ করার মর্যাদা অন্যান্য দিনের চেয়ে শ্রেষ্ঠ। সপ্তাহের অন্যান্য দিনের তুলনায় জুমার দিনে সাদাকাহ করার দৃষ্টান্ত হলো বছরের অন্যান্য মাসের তুলনায় পবিত্র রমজান মাসে সাদাকাহ করার অনুরূপ!\" (যাদুল মা'আদ: ১/৩৯৫)।",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "💡 সাদাকাহের উত্তম ক্ষেত্রসমূহ:",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• মসজিদের উন্নয়ন ও পরিচ্ছন্নতা ফান্ডে অনুদান\n• অসহায় প্রতিবেশী ও অভাবী আত্মীয়স্বজনদের সহায়তা\n• অনাহারীকে খাদ্য প্রদান বা সুপেয় পানির ব্যবস্থা\n• সুবিধাবঞ্চিত শিশুদের কুরআন শিক্ষার ব্যয়ভার বহন",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. FRIDAY HADITHS
// -------------------------------------------------------------------------
@Composable
fun FridayHadithsContent(
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    val hadiths = remember {
        listOf(
            Triple(
                "১. দিনের সরদার ও শ্রেষ্ঠ দিন",
                "خَيْرُ يَوْمٍ طَلَعَتْ عَلَيْهِ الشَّمْسُ يَوْمُ الْجُمُعَةِ، فِيهِ خُلِقَ آدَمُ، وَفِيهِ أُدْخِلَ الْجَنَّةَ، وَفِيهِ أُخْرِجَ مِنْهَا، وَلَا تَقُومُ السَّاعَةُ إِلَّا فِي يَوْمِ الْجُمُعَةِ",
                "সূর্য উদিত হওয়া দিনগুলোর মধ্যে সর্বোত্তম দিন হলো জুমার দিন। এই দিনেই আদম (আ.)-কে সৃষ্টি করা হয়েছে, এই দিনেই তাঁকে জান্নাতে প্রবেশ করানো হয়েছে এবং এই দিনেই তাঁকে জান্নাত থেকে বের করা হয়েছে। আর জুমার দিন ছাড়া কিয়ামত সংঘটিত হবে না।\n(সহীহ মুসলিম: ৮৫৪)"
            ),
            Triple(
                "২. এক জুমা থেকে অপর জুমা পর্যন্ত গুনাহ মোচন",
                "الصَّلَوَاتُ الْخَمْسُ، وَالْجُمُعَةُ إِلَى الْجُمُعَةِ، وَرَمَضَانُ إِلَى رَمَضَانَ، مُكَفِّرَاتٌ مَا بَيْنَهُنَّ إِذَا اجْتَنَبَ الْكَبَائِرَ",
                "পাঁচ ওয়াক্ত সালাত, এক জুমা থেকে অপর জুমা এবং এক রমজান থেকে অপর রমজান—তাদের মধ্যবর্তী সময়ের সগীরা গুনাহসমূহের কাফফারা স্বরূপ, যদি বান্দা কবিরা গুনাহ থেকে বিরত থাকে।\n(সহীহ মুসলিম: ২৩৩)"
            ),
            Triple(
                "৩. প্রথম প্রহরে উপস্থিতির ৫টি সওয়াবের স্তর",
                "مَنِ اغْتَسَلَ يَوْمَ الْجُمُعَةِ غُسْلَ الْجَنَابَةِ ثُمَّ رَاحَ فَكَأَنَّمَا قَرَّبَ بَدَنَةً، وَمَنْ رَاحَ فِي السَّاعَةِ الثَّانِيَةِ فَكَأَنَّمَا قَرَّبَ بَقَرَةً...",
                "যে ব্যক্তি জুমার দিনে উত্তমরূপে গোসল করে প্রথম প্রহরে মসজিদে যায়, সে যেন একটি উট কুরবানী করল। দ্বিতীয় প্রহরে গেলে যেন একটি গরু, তৃতীয় প্রহরে গেলে যেন একটি দুম্বা, চতুর্থ প্রহরে গেলে যেন একটি মুরগী এবং পঞ্চম প্রহরে গেলে যেন একটি ডিম দান করল...\n(সহীহ বুখারী: ৮৮১; সহীহ মুসলিম: ৮৫০)"
            ),
            Triple(
                "৪. খুতবার সময় কথা বলার কঠোর নিষেধাজ্ঞা",
                "إِذَا قُلْتَ لِصَاحِبِكَ يَوْمَ الْجُمُعَةِ: أَنْصِتْ، وَالإِمَامُ يَخْطُبُ، فَقَدْ لَغَوْتَ",
                "জুমার দিন ইমাম যখন খুতবা দিচ্ছেন, তখন যদি তুমি তোমার পাশের সঙ্গীকে বলো: 'চুপ থাকো', তবে তুমিও অনর্থক কথা বললে (অর্থাৎ তোমার জুমার ফজিলত নষ্ট হয়ে গেল)।\n(সহীহ বুখারী: ৯৩৪; সহীহ মুসলিম: ৮৫১)"
            )
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(hadiths) { (title, ar, bn) ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = ar,
                        style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = arabicFont,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = bn,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 22.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. ALL 45 FRIDAY DEEDS & DUAS (FROM FridaySpecialDuaAmolData)
// -------------------------------------------------------------------------
@Composable
fun FridayAllAmolsContent(
    banglaFont: FontFamily,
    arabicFont: FontFamily
) {
    val allItems = remember { FridaySpecialDuaAmolData.section.items }
    var searchQuery by remember { mutableStateOf("") }

    val filtered = remember(searchQuery) {
        if (searchQuery.isBlank()) allItems
        else allItems.filter {
            it.titleBn.contains(searchQuery, ignoreCase = true) ||
            it.meaningBn.contains(searchQuery, ignoreCase = true) ||
            it.fojilotBn.contains(searchQuery, ignoreCase = true)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("৪৫টি আমল ও দো'আ অনুসন্ধান করুন", fontFamily = banglaFont) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = null)
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Text(
                text = "মোট ৪৫টি তাহকীককৃত আমল (${filtered.size}টি প্রদর্শিত)",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )
        }

        items(filtered) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = item.titleBn,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont
                    )
                    if (item.repetitionOrTimeBn.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.repetitionOrTimeBn,
                            style = MaterialTheme.typography.labelSmall,
                            color = IslamicGold,
                            fontFamily = banglaFont
                        )
                    }
                    if (item.arabicText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = item.arabicText,
                            style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 28.sp),
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = arabicFont,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    if (item.pronunciationBn.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.pronunciationBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                    if (item.meaningBn.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = item.meaningBn,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                    if (item.fojilotBn.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                        ) {
                            Text(
                                text = item.fojilotBn,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                    if (item.referenceBn.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "উৎস: ${item.referenceBn}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

