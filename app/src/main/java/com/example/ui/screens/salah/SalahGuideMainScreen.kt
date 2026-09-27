package com.example.ui.screens.salah

import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.salah.SalahGuideCatalog
import com.example.data.model.salah.*
import com.example.data.repository.SalahGuideRepository
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.SalahAudioTutor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalahGuideMainScreen(
    onNavigateBack: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val isDark = isSystemInDarkTheme()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val repository = remember { SalahGuideRepository(context) }
    val audioTutor = remember { SalahAudioTutor(context) }

    DisposableEffect(Unit) {
        onDispose {
            audioTutor.release()
        }
    }

    val bookmarkedIds by repository.bookmarkedIds.collectAsState()
    val completedLessons by repository.completedLessonIds.collectAsState()
    val memorizeStatusMap by repository.memorizeStatusMap.collectAsState()
    val lastViewedTitle by repository.lastViewedTitle.collectAsState()
    val lastViewedTab by repository.lastViewedTab.collectAsState()
    val fiqhMethodology by repository.fiqhMethodology.collectAsState()

    var selectedTab by remember { mutableStateOf(SalahSectionTab.OVERVIEW) }
    var showSearchDialog by remember { mutableStateOf(false) }
    var showQuickRefSheet by remember { mutableStateOf(false) }
    var showBookmarksSheet by remember { mutableStateOf(false) }
    var showFiqhDialog by remember { mutableStateOf(false) }

    // Intercept back navigation to return to Overview tab first
    BackHandler {
        if (selectedTab != SalahSectionTab.OVERVIEW) {
            selectedTab = SalahSectionTab.OVERVIEW
        } else {
            onNavigateBack()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = contentPadding.calculateTopPadding()),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "নামাজের পূর্ণাঙ্গ গাইড",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF047857).copy(alpha = 0.15f),
                                border = BorderStroke(0.5.dp, Color(0xFF047857).copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = if (fiqhMethodology == SalahFiqhMethodology.HANAFI) "হানাফী" else "শাফেঈ/সাধারণ",
                                    fontSize = 10.sp,
                                    color = Color(0xFF047857),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier
                                        .clickable { showFiqhDialog = true }
                                        .padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Text(
                            text = "সহীহ নামাজ শিক্ষা, নিয়মাবলী ও রেফারেন্স",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            ),
                            maxLines = 1
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (selectedTab != SalahSectionTab.OVERVIEW) {
                                selectedTab = SalahSectionTab.OVERVIEW
                            } else {
                                onNavigateBack()
                            }
                        },
                        modifier = Modifier.testTag("salah_guide_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { showSearchDialog = true },
                        modifier = Modifier.testTag("salah_guide_search_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "অনুসন্ধান",
                            tint = Color(0xFF047857)
                        )
                    }
                    IconButton(
                        onClick = { showQuickRefSheet = true },
                        modifier = Modifier.testTag("salah_guide_quick_ref_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "দ্রুত দেখুন",
                            tint = Color(0xFFD97706)
                        )
                    }
                    IconButton(
                        onClick = { showBookmarksSheet = true },
                        modifier = Modifier.testTag("salah_guide_bookmarks_btn")
                    ) {
                        Box {
                            Icon(
                                imageVector = if (bookmarkedIds.isNotEmpty()) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "সংরক্ষিত",
                                tint = if (bookmarkedIds.isNotEmpty()) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            if (bookmarkedIds.isNotEmpty()) {
                                Surface(
                                    color = Color(0xFF047857),
                                    shape = CircleShape,
                                    modifier = Modifier
                                        .size(14.dp)
                                        .align(Alignment.TopEnd)
                                ) {
                                    Text(
                                        text = BanglaNumberUtils.toBanglaDigits(bookmarkedIds.size),
                                        fontSize = 9.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = scaffoldPadding.calculateTopPadding())
                .padding(bottom = contentPadding.calculateBottomPadding())
        ) {
            // Scrollable Category Tab Row
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color(0xFF047857),
                edgePadding = 12.dp
            ) {
                SalahSectionTab.entries.forEach { tab ->
                    val isSelected = selectedTab == tab
                    Tab(
                        selected = isSelected,
                        onClick = {
                            selectedTab = tab
                            repository.recordLastViewed(tab.titleBn, tab)
                        },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = tab.iconEmoji, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = tab.titleBn,
                                    fontFamily = banglaFont,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    )
                }
            }

            // Tab Content
            when (selectedTab) {
                SalahSectionTab.OVERVIEW -> SalahOverviewTab(
                    repository = repository,
                    onNavigateToTab = { tab ->
                        selectedTab = tab
                        repository.recordLastViewed(tab.titleBn, tab)
                    },
                    onOpenQuickRef = { showQuickRefSheet = true },
                    onOpenSearch = { showSearchDialog = true }
                )
                SalahSectionTab.STEP_BY_STEP -> SalahStepByStepTab(
                    audioTutor = audioTutor,
                    repository = repository
                )
                SalahSectionTab.DAILY_PRAYERS -> SalahDailyPrayersTab(
                    repository = repository,
                    onStartPractice = { selectedTab = SalahSectionTab.STEP_BY_STEP }
                )
                SalahSectionTab.RECITATIONS -> SalahRecitationsTab(
                    audioTutor = audioTutor,
                    repository = repository
                )
                SalahSectionTab.PREPARATION -> SalahPreparationTab(
                    repository = repository
                )
                SalahSectionTab.CONDITIONS -> SalahConditionsTab(
                    repository = repository
                )
                SalahSectionTab.MISTAKES -> SalahMistakesTab(
                    repository = repository
                )
                SalahSectionTab.SPECIAL_PRAYERS -> SalahSpecialPrayersTab(
                    repository = repository
                )
                SalahSectionTab.RULINGS -> SalahRulingsTab(
                    repository = repository
                )
                SalahSectionTab.FAQ -> SalahFAQTab(
                    repository = repository
                )
            }
        }
    }

    // Search Dialog
    if (showSearchDialog) {
        SalahSearchDialog(
            repository = repository,
            onDismiss = { showSearchDialog = false },
            onSelectResult = { tab ->
                selectedTab = tab
                showSearchDialog = false
            }
        )
    }

    // Quick Reference Sheet
    if (showQuickRefSheet) {
        SalahQuickRefSheet(
            onDismiss = { showQuickRefSheet = false },
            onNavigateToTab = { tab ->
                selectedTab = tab
                showQuickRefSheet = false
            }
        )
    }

    // Bookmarks Sheet
    if (showBookmarksSheet) {
        SalahBookmarksSheet(
            repository = repository,
            onDismiss = { showBookmarksSheet = false },
            onNavigateToTab = { tab ->
                selectedTab = tab
                showBookmarksSheet = false
            }
        )
    }

    // Fiqh Methodology Dialog
    if (showFiqhDialog) {
        SalahFiqhMethodologyDialog(
            currentMethodology = fiqhMethodology,
            onSelect = {
                repository.setFiqhMethodology(it)
                showFiqhDialog = false
            },
            onDismiss = { showFiqhDialog = false }
        )
    }
}

// ==========================================
// 1. OVERVIEW TAB (গাইড হোম)
// ==========================================
@Composable
private fun SalahOverviewTab(
    repository: SalahGuideRepository,
    onNavigateToTab: (SalahSectionTab) -> Unit,
    onOpenQuickRef: () -> Unit,
    onOpenSearch: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val progress = repository.getOverallProgressFraction()
    val completedLessons by repository.completedLessonIds.collectAsState()
    val lastTitle by repository.lastViewedTitle.collectAsState()
    val lastTab by repository.lastViewedTab.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF047857).copy(alpha = 0.08f)
                ),
                border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "নামাজের পূর্ণাঙ্গ গাইড",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont,
                                    color = Color(0xFF047857)
                                )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "নামাজ শিখুন, বুঝুন এবং সঠিকভাবে আদায় করুন",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF047857).copy(alpha = 0.15f),
                            modifier = Modifier.size(46.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Mosque,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Progress indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "শেখার অগ্রগতি: ${BanglaNumberUtils.toBanglaDigits((progress * 100).toInt())}% (${BanglaNumberUtils.toBanglaDigits(completedLessons.size)}/১২ পাঠ)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                color = Color(0xFF047857)
                            )
                        )
                        Text(
                            text = if (progress >= 1f) "মাশাআল্লাহ সম্পূর্ণ!" else "চলমান",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = banglaFont),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = Color(0xFF047857),
                        trackColor = Color(0xFF047857).copy(alpha = 0.2f)
                    )

                    // Continue learning banner
                    if (lastTitle.isNotBlank()) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.25f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToTab(lastTab) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "যেখানে শেষ করেছিলেন:",
                                        style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = lastTitle,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        fontFamily = banglaFont,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "চালিয়ে যান",
                                    tint = Color(0xFF047857)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Quick Actions Grid (১২টি বড় অ্যাকশন কার্ড)
        item {
            Text(
                text = "জরুরি বিভাগসমূহ (Quick Actions)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(10.dp))

            val quickActions = listOf(
                QuickActionData("১. নামাজ শিক্ষা শুরু", "🧎", Color(0xFF047857), SalahSectionTab.STEP_BY_STEP),
                QuickActionData("২. নামাজের নিয়ত", "🤲", Color(0xFF0284C7), SalahSectionTab.STEP_BY_STEP),
                QuickActionData("৩. ধাপে ধাপে নিয়ম", "👣", Color(0xFF059669), SalahSectionTab.STEP_BY_STEP),
                QuickActionData("৪. দোয়া ও সূরা", "🗣️", Color(0xFF7C3AED), SalahSectionTab.RECITATIONS),
                QuickActionData("৫. কত রাকাত?", "🔢", Color(0xFFB45309), SalahSectionTab.DAILY_PRAYERS),
                QuickActionData("৬. ভুল ও সংশোধন", "❌", Color(0xFFDC2626), SalahSectionTab.MISTAKES),
                QuickActionData("৭. নামাজ শুদ্ধির শর্ত", "✅", Color(0xFF0D9488), SalahSectionTab.CONDITIONS),
                QuickActionData("৮. নামাজের মাসআলা", "📚", Color(0xFF2563EB), SalahSectionTab.RULINGS),
                QuickActionData("৯. বিশেষ নামাজ", "🌙", Color(0xFF9333EA), SalahSectionTab.SPECIAL_PRAYERS),
                QuickActionData("১০. মুখস্থ অনুশীলন", "🧠", Color(0xFFEA580C), SalahSectionTab.RECITATIONS),
                QuickActionData("১১. দ্রুত দেখুন", "⚡", Color(0xFFCA8A04), null),
                QuickActionData("১২. প্রশ্ন ও উত্তর", "❓", Color(0xFF4B5563), SalahSectionTab.FAQ)
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                quickActions.chunked(2).forEach { rowPair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowPair.forEach { action ->
                            Card(
                                onClick = {
                                    if (action.targetTab != null) {
                                        onNavigateToTab(action.targetTab)
                                    } else {
                                        onOpenQuickRef()
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                ),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(58.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        color = action.color.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = action.emoji, fontSize = 16.sp)
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = action.title,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp
                                        ),
                                        fontFamily = banglaFont,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Beginner Progressive Lessons Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ধারাবাহিক নামাজ শিক্ষা (১২টি পাঠ)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    fontFamily = banglaFont
                )
                Text(
                    text = "নতুন ও রিভিশন",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF047857)),
                    fontFamily = banglaFont
                )
            }
        }

        // 12 Beginner Lessons Items
        items(SalahGuideCatalog.beginnerLessons) { lesson ->
            val isCompleted = completedLessons.contains(lesson.id)
            var isExpanded by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCompleted) Color(0xFF047857).copy(alpha = 0.05f)
                    else MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(
                    1.dp,
                    if (isCompleted) Color(0xFF047857).copy(alpha = 0.4f)
                    else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = if (isCompleted) Color(0xFF047857) else MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (isCompleted) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    } else {
                                        Text(
                                            text = lesson.serialNumberBn,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp,
                                            fontFamily = banglaFont
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = lesson.titleBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = lesson.subtitleBn,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        IconButton(onClick = { isExpanded = !isExpanded }) {
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "বিস্তারিত"
                            )
                        }
                    }

                    Text(
                        text = lesson.summaryBn,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(top = 6.dp)
                    )

                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = lesson.detailedContentBn,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    lineHeight = 22.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                fontFamily = banglaFont
                            )

                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "মূল শিক্ষণীয় বিষয়সমূহ:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                ),
                                fontFamily = banglaFont
                            )
                            lesson.keyPoints.forEach { point ->
                                Row(
                                    modifier = Modifier.padding(top = 4.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text(text = "• ", fontWeight = FontWeight.Bold, color = Color(0xFF047857))
                                    Text(
                                        text = point,
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "দলিল / সূত্র: ${lesson.quranHadithReference}",
                                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = { repository.toggleLessonCompleted(lesson.id) },
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, if (isCompleted) Color(0xFF047857) else MaterialTheme.colorScheme.outline)
                                ) {
                                    Icon(
                                        imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Check,
                                        contentDescription = null,
                                        tint = if (isCompleted) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (isCompleted) "সম্পন্ন হয়েছে" else "পড়া সম্পন্ন চিহ্নিত করুন",
                                        fontFamily = banglaFont,
                                        color = if (isCompleted) Color(0xFF047857) else MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                if (lesson.targetTab != null) {
                                    Button(
                                        onClick = { onNavigateToTab(lesson.targetTab) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
                                    ) {
                                        Text(text = "অনুশীলন", fontFamily = banglaFont)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class QuickActionData(
    val title: String,
    val emoji: String,
    val color: Color,
    val targetTab: SalahSectionTab?
)

// ==========================================
// 2. STEP-BY-STEP TAB (ধাপে ধাপে নামাজ)
// ==========================================
@Composable
private fun SalahStepByStepTab(
    audioTutor: SalahAudioTutor,
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val isPlaying by audioTutor.isPlaying.collectAsState()
    val currentAudioId by audioTutor.currentRecitationId.collectAsState()
    val repeatCount by audioTutor.repeatCount.collectAsState()
    val playbackSpeed by audioTutor.playbackSpeed.collectAsState()

    var selectedPrayerType by remember { mutableStateOf("fajr_2") }
    var currentStepIndex by remember { mutableIntStateOf(0) }

    val allSteps = SalahGuideCatalog.stepByStepGuide
    val currentStep = allSteps.getOrNull(currentStepIndex) ?: allSteps[0]

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Prayer Type Selector
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "কোন সালাতটি ধাপে ধাপে শিখতে চান?",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        val prayerOptions = listOf(
                            "fajr_2" to "ফজর (২ রাকাত)",
                            "dhuhr_4" to "যোহর/আসর/এশা (৪ রাকাত)",
                            "maghrib_3" to "মাগরিব (৩ রাকাত)",
                            "witr_3" to "বিতর (৩ রাকাত)"
                        )
                        items(prayerOptions) { (key, label) ->
                            FilterChip(
                                selected = selectedPrayerType == key,
                                onClick = {
                                    selectedPrayerType = key
                                    currentStepIndex = 0
                                },
                                label = { Text(text = label, fontFamily = banglaFont) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = Color(0xFF047857),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }

        // Stepper Progress Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ধাপ ${BanglaNumberUtils.toBanglaDigits(currentStepIndex + 1)} / ${BanglaNumberUtils.toBanglaDigits(allSteps.size)}: ${currentStep.titleBn}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857)
                    ),
                    fontFamily = banglaFont
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF047857).copy(alpha = 0.15f)
                ) {
                    Text(
                        text = currentStep.posture.emoji + " " + currentStep.posture.titleBn,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF047857),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { (currentStepIndex + 1).toFloat() / allSteps.size.toFloat() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = Color(0xFF047857),
                trackColor = Color(0xFF047857).copy(alpha = 0.2f)
            )
        }

        // Current Step Main Card
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Instruction Section (কী করবেন)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF047857),
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = BanglaNumberUtils.toBanglaDigits(currentStep.stepNumber),
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "শারীরিক অবস্থা ও করণীয়:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont,
                            color = Color(0xFF047857)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentStep.actionInstructionBn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        fontFamily = banglaFont
                    )

                    // What to Say (কী পড়বেন)
                    if (currentStep.arabicText != null) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "এই ধাপে পঠিতব্য দোয়া / তাসবীহ:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0284C7)
                                ),
                                fontFamily = banglaFont
                            )
                            if (currentStep.repetitionCountBn != null) {
                                Surface(
                                    color = Color(0xFF0284C7).copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = currentStep.repetitionCountBn,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0284C7),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        // Arabic Text
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF047857).copy(alpha = 0.05f),
                            border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = currentStep.arabicText,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontSize = 24.sp,
                                    lineHeight = 42.sp,
                                    textAlign = TextAlign.Right,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(14.dp)
                            )
                        }

                        // Pronunciation
                        if (currentStep.transliterationBn != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "উচ্চারণ:",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = Color(0xFF047857),
                                            fontWeight = FontWeight.Bold
                                        ),
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = currentStep.transliterationBn,
                                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }

                        // Meaning
                        if (currentStep.meaningBn != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Column {
                                Text(
                                    text = "অনুবাদ:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color(0xFF047857),
                                        fontWeight = FontWeight.Bold
                                    ),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = currentStep.meaningBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 22.sp
                                    ),
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        // Audio Assistant Controls
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val isThisPlaying = isPlaying && currentAudioId == "step_${currentStep.stepNumber}"
                            Button(
                                onClick = {
                                    if (isThisPlaying) {
                                        audioTutor.stop()
                                    } else {
                                        audioTutor.speakRecitation(
                                            id = "step_${currentStep.stepNumber}",
                                            textToSpeak = currentStep.arabicText
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isThisPlaying) Color(0xFFDC2626) else Color(0xFF047857)
                                )
                            ) {
                                Icon(
                                    imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isThisPlaying) "বিরতি দিন" else "উচ্চারণ শুনুন",
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                FilterChip(
                                    selected = playbackSpeed < 1.0f,
                                    onClick = {
                                        audioTutor.setPlaybackSpeed(if (playbackSpeed < 1.0f) 1.0f else 0.75f)
                                    },
                                    label = {
                                        Text(
                                            text = if (playbackSpeed < 1.0f) "ধীর (০.৭৫x)" else "স্বাভাবিক (১x)",
                                            fontSize = 11.sp,
                                            fontFamily = banglaFont
                                        )
                                    }
                                )
                            }
                        }
                    }

                    // Sunnah Note
                    if (currentStep.sunnahNoteBn != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF047857).copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFF047857),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "সুন্নাত টিপস: ${currentStep.sunnahNoteBn}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF047857)),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    // Common Mistake Alert
                    if (currentStep.commonMistakeAlertBn != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFDC2626).copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = Color(0xFFDC2626),
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "সাধারণ ভুল: ${currentStep.commonMistakeAlertBn}",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFDC2626)),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        }

        // Stepper Navigation Buttons
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentStepIndex > 0) {
                            currentStepIndex--
                        }
                    },
                    enabled = currentStepIndex > 0,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "পূর্ববর্তী ধাপ", fontFamily = banglaFont)
                }

                Button(
                    onClick = {
                        if (currentStepIndex < allSteps.size - 1) {
                            currentStepIndex++
                        } else {
                            // Completed full prayer cycle
                            currentStepIndex = 0
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                    modifier = Modifier
                        .weight(1.2f)
                        .height(50.dp)
                ) {
                    Text(
                        text = if (currentStepIndex < allSteps.size - 1) "পরবর্তী ধাপ →" else "পুনরায় শুরু করুন ↺",
                        fontFamily = banglaFont,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ==========================================
// 3. DAILY PRAYERS TAB (৫ ওয়াক্ত ও রাকাত)
// ==========================================
@Composable
private fun SalahDailyPrayersTab(
    repository: SalahGuideRepository,
    onStartPractice: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val dailyPrayers = SalahGuideCatalog.dailyPrayers

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF047857).copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "দৈনিক ৫ ওয়াক্ত সালাত হলো ইসলামের দ্বিতীয় রুকন। নিচে প্রতিটি ওয়াক্তের ওয়াক্ত বিবরণ, সঠিক রাকাত ও সুন্নাত-ফরজের ধারাবাহিক কাঠামো দেওয়া হলো:",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        items(dailyPrayers) { prayer ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = prayer.nameBn,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${prayer.nameAr})",
                                    style = MaterialTheme.typography.titleSmall.copy(color = Color(0xFF047857))
                                )
                            }
                            Text(
                                text = "ওয়াক্ত: ${prayer.timeDescriptionBn}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                fontFamily = banglaFont
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF047857),
                            modifier = Modifier.testTag("rakat_badge_${prayer.id}")
                        ) {
                            Text(
                                text = prayer.totalRakatsBn,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "রাকাতের সুবিন্যস্ত কাঠামো:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        ),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = prayer.breakdownBn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E293B)
                        ),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "ধারাবাহিক আদায়ের নিয়ম:",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = prayer.sequenceGuideBn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 18.sp
                        ),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "ফযীলত ও মাহাত্ম্য:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                ),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = prayer.fojilotBn,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "রেফারেন্স: ${prayer.hadithRefBn}",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 4. RECITATIONS TAB (দোয়া, সূরা ও মুখস্থ)
// ==========================================
@Composable
private fun SalahRecitationsTab(
    audioTutor: SalahAudioTutor,
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current
    val isPlaying by audioTutor.isPlaying.collectAsState()
    val currentAudioId by audioTutor.currentRecitationId.collectAsState()
    val repeatCount by audioTutor.repeatCount.collectAsState()
    val playbackSpeed by audioTutor.playbackSpeed.collectAsState()
    val bookmarkedIds by repository.bookmarkedIds.collectAsState()
    val memorizeStatusMap by repository.memorizeStatusMap.collectAsState()

    var activeSubMode by remember { mutableStateOf("library") } // "library" or "memorize"
    var hiddenArabicSet by remember { mutableStateOf(setOf<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Switcher
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = activeSubMode == "library",
                    onClick = { activeSubMode = "library" },
                    label = { Text("দোয়া ও সূরার ভাণ্ডার", fontFamily = banglaFont) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF047857),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = activeSubMode == "memorize",
                    onClick = { activeSubMode = "memorize" },
                    label = { Text("হিফজ / মুখস্থ অনুশীলন", fontFamily = banglaFont) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF7C3AED),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Global Audio Speed & Repeat Bar
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = null,
                            tint = Color(0xFF047857),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "অডিও টিউটর:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        FilterChip(
                            selected = playbackSpeed < 1.0f,
                            onClick = {
                                audioTutor.setPlaybackSpeed(if (playbackSpeed < 1.0f) 1.0f else 0.75f)
                            },
                            label = {
                                Text(
                                    text = if (playbackSpeed < 1.0f) "০.৭৫x" else "১.০x",
                                    fontSize = 11.sp,
                                    fontFamily = banglaFont
                                )
                            }
                        )
                        val repeats = listOf(1, 3, -1)
                        repeats.forEach { count ->
                            val label = if (count == -1) "∞ লুপ" else "${BanglaNumberUtils.toBanglaDigits(count)} বার"
                            FilterChip(
                                selected = repeatCount == count,
                                onClick = { audioTutor.setRepeatCount(count) },
                                label = { Text(text = label, fontSize = 11.sp, fontFamily = banglaFont) }
                            )
                        }
                    }
                }
            }
        }

        // Recitations List
        items(SalahGuideCatalog.recitationsLibrary) { recitation ->
            val isBookmarked = bookmarkedIds.contains(recitation.id)
            val memStatus = memorizeStatusMap[recitation.id] ?: MemorizeStatus.NEW
            val isArabicHidden = hiddenArabicSet.contains(recitation.id)
            val isThisPlaying = isPlaying && currentAudioId == recitation.id

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(
                    1.dp,
                    if (isThisPlaying) Color(0xFF047857) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = recitation.titleBn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = recitation.placeInSalahBn,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF047857)),
                                fontFamily = banglaFont
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { repository.toggleBookmark(recitation.id) }) {
                                Icon(
                                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                    contentDescription = "বুকমার্ক",
                                    tint = if (isBookmarked) Color(0xFF047857) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Arabic Text Box (with Memorization Hide/Reveal)
                    if (activeSubMode == "memorize" && isArabicHidden) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(90.dp)
                                .clickable {
                                    hiddenArabicSet = hiddenArabicSet - recitation.id
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = Icons.Default.VisibilityOff,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "আরবি লুকানো আছে (ট্যাপ করে প্রকাশ করুন)",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.Bold
                                        ),
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF047857).copy(alpha = 0.05f),
                            border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.2f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = recitation.arabicText,
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontSize = 24.sp,
                                    lineHeight = 42.sp,
                                    textAlign = TextAlign.Right,
                                    color = MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(14.dp)
                            )
                        }
                    }

                    // Pronunciation
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "বাংলা উচ্চারণ:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF047857),
                                    fontWeight = FontWeight.Bold
                                ),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = recitation.transliterationBn,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                fontFamily = banglaFont
                            )
                        }
                    }

                    // Meaning
                    Spacer(modifier = Modifier.height(8.dp))
                    Column {
                        Text(
                            text = "বাংলা অর্থ:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.Bold
                            ),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = recitation.translationBn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 22.sp
                            ),
                            fontFamily = banglaFont
                        )
                    }

                    if (recitation.englishMeaning != null) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "English: ${recitation.englishMeaning}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                fontSize = 11.5.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "দলিল / সূত্র: ${recitation.referenceBn}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(8.dp))

                    // Controls Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                if (isThisPlaying) {
                                    audioTutor.stop()
                                } else {
                                    audioTutor.speakRecitation(
                                        id = recitation.id,
                                        textToSpeak = recitation.arabicText
                                    )
                                }
                            },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isThisPlaying) Color(0xFFDC2626) else Color(0xFF047857)
                            )
                        ) {
                            Icon(
                                imageVector = if (isThisPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isThisPlaying) "বিরতি" else "তেলাওয়াত শুনুন",
                                fontFamily = banglaFont
                            )
                        }

                        // Memorize actions
                        if (activeSubMode == "memorize") {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = {
                                        hiddenArabicSet = if (isArabicHidden) {
                                            hiddenArabicSet - recitation.id
                                        } else {
                                            hiddenArabicSet + recitation.id
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isArabicHidden) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                        contentDescription = "আরবি লুকান/দেখান",
                                        tint = Color(0xFF7C3AED)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = memStatus.color.copy(alpha = 0.15f),
                                    border = BorderStroke(1.dp, memStatus.color.copy(alpha = 0.4f)),
                                    modifier = Modifier.clickable {
                                        val nextStatus = when (memStatus) {
                                            MemorizeStatus.NEW -> MemorizeStatus.LEARNING
                                            MemorizeStatus.LEARNING -> MemorizeStatus.PARTIALLY_MEMORIZED
                                            MemorizeStatus.PARTIALLY_MEMORIZED -> MemorizeStatus.MEMORIZED
                                            MemorizeStatus.MEMORIZED -> MemorizeStatus.NEW
                                        }
                                        repository.setMemorizeStatus(recitation.id, nextStatus)
                                    }
                                ) {
                                    Text(
                                        text = memStatus.labelBn,
                                        color = memStatus.color,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.5.sp,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 5. PREPARATION & TAHARAH TAB (পবিত্রতা)
// ==========================================
@Composable
private fun SalahPreparationTab(
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    val preparationItems = SalahGuideCatalog.preparationGuides

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0284C7).copy(alpha = 0.08f)),
                border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "💧", fontSize = 24.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "পবিত্রতা ব্যতীত সালাত কবুল হয় না। নিচে ওযু, গোসল, তায়াম্মুম, সতর ও কিবলার প্রামাণ্য বিধান দেওয়া হলো:",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        items(preparationItems) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.titleBn,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF0284C7).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = item.category,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.subtitleBn,
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "পদ্ধতি ও বিধানসমূহ:",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0284C7)
                        ),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    item.stepsOrRules.forEach { step ->
                        Row(
                            modifier = Modifier.padding(vertical = 3.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(text = "• ", fontWeight = FontWeight.Bold, color = Color(0xFF0284C7))
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                                fontFamily = banglaFont
                            )
                        }
                    }

                    if (item.invalidators.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "ভঙ্গের কারণসমূহ:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFDC2626)
                            ),
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        item.invalidators.forEach { inv ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(text = "✖ ", color = Color(0xFFDC2626), fontSize = 11.sp)
                                Text(
                                    text = inv,
                                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF047857).copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "সতর্কতা: ${item.precautionsBn}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF047857),
                                lineHeight = 18.sp
                            ),
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "দলিল: ${item.referenceBn}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// ==========================================
// 6. CONDITIONS TAB (শর্ত ও ফরজ-ওয়াজিব)
// ==========================================
@Composable
private fun SalahConditionsTab(
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    var selectedFilter by remember { mutableStateOf("all") }

    val allConditions = SalahGuideCatalog.conditionsAndRules
    val filteredList = when (selectedFilter) {
        "ahkam" -> allConditions.filter { it.categoryBn.contains("আহকাম") }
        "arkan" -> allConditions.filter { it.categoryBn.contains("আরকান") }
        "wajib" -> allConditions.filter { it.categoryBn.contains("ওয়াজিব") }
        else -> allConditions
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val filterOptions = listOf(
                    "all" to "সকল শর্ত ও ফরজ",
                    "ahkam" to "নামাজের পূর্বে ৭ শর্ত (আহকাম)",
                    "arkan" to "নামাজের ভেতরের ৬ ফরজ (আরকান)",
                    "wajib" to "নামাজের ১৪ ওয়াজিব"
                )
                items(filterOptions) { (key, label) ->
                    FilterChip(
                        selected = selectedFilter == key,
                        onClick = { selectedFilter = key },
                        label = { Text(label, fontFamily = banglaFont) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF047857),
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        items(filteredList) { item ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.titleBn,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF047857).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = item.categoryBn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.ruleDescriptionBn,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFDC2626).copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "বাদ পড়লে হুকুম:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626)
                                ),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = item.ifOmittedBn,
                                style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFFDC2626)),
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "দলিল: ${item.referenceBn}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// ==========================================
// 7. MISTAKES & SAHW TAB (ভুল ও সাহু সিজদা)
// ==========================================
@Composable
private fun SalahMistakesTab(
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    var activeSubSection by remember { mutableStateOf("mistakes") } // "mistakes" vs "sahw"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = activeSubSection == "mistakes",
                    onClick = { activeSubSection = "mistakes" },
                    label = { Text("নামাজের সাধারণ ভুলসমূহ", fontFamily = banglaFont) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFDC2626),
                        selectedLabelColor = Color.White
                    )
                )
                FilterChip(
                    selected = activeSubSection == "sahw",
                    onClick = { activeSubSection = "sahw" },
                    label = { Text("সাহু সিজদার পূর্ণ নিয়ম", fontFamily = banglaFont) },
                    modifier = Modifier.weight(1f),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF047857),
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        if (activeSubSection == "mistakes") {
            items(SalahGuideCatalog.commonMistakes) { mistake ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFDC2626).copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = mistake.mistakeTitleBn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626)
                                ),
                                fontFamily = banglaFont,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFDC2626).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = mistake.categoryBn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFDC2626),
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "কেন সমস্যা:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont
                        )
                        Text(
                            text = mistake.whyProblematicBn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp
                            ),
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF047857).copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "সঠিক পদ্ধতি ও সমাধান:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    ),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = mistake.howToCorrectBn,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF047857),
                                        lineHeight = 18.sp
                                    ),
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "দলিল: ${mistake.evidenceReferenceBn}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        } else {
            // Sahw Sajdah Complete Guide
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF047857).copy(alpha = 0.08f)),
                    border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "সাহু সিজদা আদায়ের ৩টি সহজ ধাপ:",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857)
                            ),
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "১. শেষ বৈঠকে শুধু 'তাশাহহুদ' (আত্তাহিয়্যাতু...) পাঠ করবেন।\n২. এরপর কেবল ডান দিকে একবার সালাম ফিরিয়ে 'আল্লাহু আকবার' বলে যথারীতি ২টি সিজদা দেবেন এবং তাসবীহ পড়বেন।\n৩. সিজদা শেষে পুনরায় বসে তাশাহহুদ, দরূদে ইব্রাহীম ও দোআয়ে মাসূরা পড়ে উভয় দিকে সালাম ফিরিয়ে সালাত শেষ করবেন।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            items(SalahGuideCatalog.sajdahSahwRules) { rule ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = rule.situationBn,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont,
                                modifier = Modifier.weight(1f)
                            )
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (rule.isSahwRequired) Color(0xFF047857).copy(alpha = 0.15f) else Color(0xFFDC2626).copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = if (rule.isSahwRequired) "সাহু সিজদা আবশ্যক" else "সাহু সিজদায় হবে না",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (rule.isSahwRequired) Color(0xFF047857) else Color(0xFFDC2626),
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "কারণ: ${rule.reasonBn}",
                            style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "সমাধান: ${rule.solutionBn}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (rule.isSahwRequired) Color(0xFF047857) else Color(0xFFDC2626),
                                lineHeight = 18.sp
                            ),
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "রেফারেন্স: ${rule.hadithRefBn}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

// ==========================================
// 8. SPECIAL PRAYERS TAB (বিশেষ নামাজ)
// ==========================================
@Composable
private fun SalahSpecialPrayersTab(
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    val specialPrayers = SalahGuideCatalog.specialPrayers

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(specialPrayers) { prayer ->
            var isExpanded by remember { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = prayer.nameBn,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "(${prayer.nameAr})",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF047857))
                                )
                            }
                            Text(
                                text = "ওয়াক্ত: ${prayer.timingBn}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                fontFamily = banglaFont
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF047857).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = prayer.rakatsSummaryBn,
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = prayer.importanceAndVirtueBn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 18.sp
                        ),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = { isExpanded = !isExpanded },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isExpanded) "নিয়মাবলী লুকান ▲" else "সম্পূর্ণ আদায় পদ্ধতি দেখুন ▼",
                            fontFamily = banglaFont
                        )
                    }

                    AnimatedVisibility(visible = isExpanded) {
                        Column(modifier = Modifier.padding(top = 10.dp)) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "আদায়ের পূর্ণ নিয়ম:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                ),
                                fontFamily = banglaFont
                            )
                            Text(
                                text = prayer.completeMethodBn,
                                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                                fontFamily = banglaFont
                            )

                            if (prayer.specialDuaArabic != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xFF047857).copy(alpha = 0.05f),
                                    border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.2f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = "বিশেষ দোয়া:",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = Color(0xFF047857),
                                                fontWeight = FontWeight.Bold
                                            ),
                                            fontFamily = banglaFont
                                        )
                                        Text(
                                            text = prayer.specialDuaArabic,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                textAlign = TextAlign.Right,
                                                lineHeight = 32.sp
                                            ),
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                        if (prayer.specialDuaTransliteration != null) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "উচ্চারণ: ${prayer.specialDuaTransliteration}",
                                                style = MaterialTheme.typography.bodySmall,
                                                fontFamily = banglaFont
                                            )
                                        }
                                        if (prayer.specialDuaMeaningBn != null) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "অর্থ: ${prayer.specialDuaMeaningBn}",
                                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                                fontFamily = banglaFont
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "আদব ও শর্তাবলী: ${prayer.rulesAndEtiquetteBn}",
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                fontFamily = banglaFont
                            )

                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "রেফারেন্স: ${prayer.referencesBn}",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 9. RULINGS TAB (মাসআলা ও বিধান)
// ==========================================
@Composable
private fun SalahRulingsTab(
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    val rulings = SalahGuideCatalog.salahRulings

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(rulings) { ruling ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ruling.questionOrTopicBn,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            fontFamily = banglaFont,
                            modifier = Modifier.weight(1f)
                        )
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF2563EB).copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = ruling.topicCategoryBn,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF047857).copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "সরাসরি ফতোয়া: ${ruling.directRulingBn}",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                lineHeight = 20.sp
                            ),
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = ruling.detailedExplanationBn,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        ),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "বাস্তব উদাহরণ: ${ruling.practicalExampleBn}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        fontFamily = banglaFont
                    )

                    if (ruling.madhhabNoteBn != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "মাযহাবগত পর্যবেক্ষণ: ${ruling.madhhabNoteBn}",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFFB45309)),
                            fontFamily = banglaFont
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "দলিল: ${ruling.referenceBn}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// ==========================================
// 10. FAQ TAB (প্রশ্নোত্তর)
// ==========================================
@Composable
private fun SalahFAQTab(
    repository: SalahGuideRepository
) {
    val banglaFont = LocalBanglaFontFamily.current
    var searchQuery by remember { mutableStateOf("") }

    val allFaqs = SalahGuideCatalog.salahFaqs
    val filteredFaqs = remember(searchQuery) {
        if (searchQuery.isBlank()) allFaqs
        else {
            val q = searchQuery.trim().lowercase()
            allFaqs.filter {
                it.questionBn.lowercase().contains(q) ||
                it.answerBn.lowercase().contains(q)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(top = 14.dp, bottom = 48.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("প্রশ্ন খুঁজুন (যেমন: সন্দেহ, মোবাইল, হাঁচি...)", fontFamily = banglaFont) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(filteredFaqs) { faq ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "প্রশ্ন: ${faq.questionBn}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF047857)
                        ),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "উত্তর: ${faq.answerBn}",
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "পরামর্শ: ${faq.practicalAdviceBn}",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "রেফারেন্স: ${faq.referenceBn}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF0284C7)),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

// ==========================================
// 11. SEARCH DIALOG
// ==========================================
@Composable
private fun SalahSearchDialog(
    repository: SalahGuideRepository,
    onDismiss: () -> Unit,
    onSelectResult: (SalahSectionTab) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var query by remember { mutableStateOf("") }
    val results = remember(query) { repository.search(query) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "নামাজ গাইডে অনুসন্ধান",
                fontFamily = banglaFont,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    placeholder = { Text("কী খুঁজতে চান? (যেমন: রুকু, সিজদা, বিতর, কাজা...)", fontFamily = banglaFont, fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (results.isEmpty() && query.isNotBlank()) {
                    Text(
                        text = "কোনো ফলাফল পাওয়া যায়নি। অন্য শব্দ দিয়ে চেষ্টা করুন।",
                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(results) { res ->
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onSelectResult(res.targetTab) }
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = res.titleBn,
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            fontFamily = banglaFont,
                                            maxLines = 1,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Text(
                                            text = res.targetTab.titleBn,
                                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF047857)),
                                            fontFamily = banglaFont
                                        )
                                    }
                                    Text(
                                        text = res.snippetBn,
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        fontFamily = banglaFont,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", fontFamily = banglaFont)
            }
        }
    )
}

// ==========================================
// 12. QUICK REFERENCE SHEET
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalahQuickRefSheet(
    onDismiss: () -> Unit,
    onNavigateToTab: (SalahSectionTab) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val lookups = SalahGuideCatalog.quickReferenceLookups

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = Color(0xFFD97706)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "দ্রুত দেখুন (Quick Reference)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.height(380.dp)
            ) {
                items(lookups) { item ->
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToTab(item.targetTab) }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = item.queryBn,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857)
                                ),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.shortAnswerBn,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = item.detailedTextBn,
                                style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 13. BOOKMARKS SHEET
// ==========================================
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SalahBookmarksSheet(
    repository: SalahGuideRepository,
    onDismiss: () -> Unit,
    onNavigateToTab: (SalahSectionTab) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val bookmarkedIds by repository.bookmarkedIds.collectAsState()
    val allRecitations = SalahGuideCatalog.recitationsLibrary.filter { bookmarkedIds.contains(it.id) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Color(0xFF047857)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "আমার সংরক্ষিত বিষয়সমূহ (${BanglaNumberUtils.toBanglaDigits(bookmarkedIds.size)})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        fontFamily = banglaFont
                    )
                }
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (allRecitations.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "কোনো বিষয় এখনো বুকমার্ক করা হয়নি। যেকোনো দোয়া বা সূরায় বুকমার্ক আইকনে ট্যাপ করে সংরক্ষণ করুন।",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        ),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(24.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.height(360.dp)
                ) {
                    items(allRecitations) { item ->
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onNavigateToTab(SalahSectionTab.RECITATIONS)
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.titleBn,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        fontFamily = banglaFont
                                    )
                                    Text(
                                        text = item.placeInSalahBn,
                                        style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                        fontFamily = banglaFont
                                    )
                                }
                                IconButton(onClick = { repository.toggleBookmark(item.id) }) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = "মুছে ফেলুন",
                                        tint = Color(0xFF047857)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 14. FIQH METHODOLOGY DIALOG
// ==========================================
@Composable
private fun SalahFiqhMethodologyDialog(
    currentMethodology: SalahFiqhMethodology,
    onSelect: (SalahFiqhMethodology) -> Unit,
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "ফিকহী পদ্ধতি ও দৃষ্টিভঙ্গি",
                fontFamily = banglaFont,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = "সালাতের মূল কাঠামো সর্বসম্মত। তবে কিছু খুঁটিনাটি বিষয়ে (যেমন: হাত বাঁধার স্থান, রফউল ইয়াদাইন, বিতরের পদ্ধতি) সম্মানিত ইমামগণের মধ্যে মতভেদ রয়েছে। আপনি কোন দৃষ্টিভঙ্গিতে দেখতে চান?",
                    style = MaterialTheme.typography.bodySmall,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(12.dp))

                SalahFiqhMethodology.entries.forEach { method ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (currentMethodology == method) Color(0xFF047857).copy(alpha = 0.1f)
                        else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        border = BorderStroke(
                            1.dp,
                            if (currentMethodology == method) Color(0xFF047857)
                            else Color.Transparent
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelect(method) }
                            .padding(vertical = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = currentMethodology == method,
                                onClick = { onSelect(method) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = method.titleBn,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = method.descriptionBn,
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant),
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857))
            ) {
                Text("ঠিক আছে", fontFamily = banglaFont)
            }
        }
    )
}
