package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.data.datasource.IslamicCalendarCatalog
import com.example.data.model.FastingCategory
import com.example.data.model.HistoricalCertainty
import com.example.data.model.IslamicDayContext
import com.example.data.model.IslamicHistoricalEvent
import com.example.data.model.IslamicMonthProfile
import com.example.data.model.IslamicWorshipAction
import com.example.data.model.LunarPhaseInfo
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.theme.LocalEnglishFontFamily
import com.example.util.CalendarHelper
import com.example.util.CalendarMonthProvider
import com.example.util.HijriDayItem
import java.util.Calendar

private enum class CalendarDayFilter {
    ALL,
    FASTING,
    HISTORICAL,
    AYYAM_AL_BEED
}

private enum class ContextDetailTab(val titleBn: String, val iconEmoji: String) {
    HISTORY("ইতিহাস ও সনদ", "📜"),
    FASTING("রোযার বিধান", "✨"),
    LUNAR("চান্দ্র দশা ও দু'আ", "🌕"),
    WORSHIP("সুন্নাত আমল", "🤲"),
    CONVERTER("তারিখ রূপান্তর", "🔄")
}

/**
 * Optimized, Contextual Islamic Calendar with Meaning (অর্থবহ ইসলামিক ক্যালেন্ডার)
 */
@Composable
fun ContextualExpandedHijriCalendar(
    monthIndex: Int,
    year: Int,
    todayCal: Calendar = Calendar.getInstance(),
    onSelectMonth: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit
) {
    val monthData = remember(monthIndex, year, todayCal) {
        CalendarMonthProvider.getHijriMonth(monthIndex, year, todayCal)
    }
    val todayDetail = remember(todayCal) { CalendarHelper.getHijriDateDetail(todayCal) }
    val isViewingCurrentMonth = (monthIndex == todayDetail.monthIndex && year == todayDetail.year)

    // Current month profile from catalog
    val monthProfile = remember(monthIndex) {
        IslamicCalendarCatalog.islamicMonths.getOrElse(monthIndex) { IslamicCalendarCatalog.islamicMonths[0] }
    }

    // Selected Day in the calendar (default to today if in current month, or 1st day)
    var selectedHijriDay by remember(monthIndex, year) {
        mutableIntStateOf(if (isViewingCurrentMonth) todayDetail.day else 1)
    }

    var activeFilter by remember { mutableStateOf(CalendarDayFilter.ALL) }
    var showMonthOverview by remember { mutableStateOf(false) }

    val arabicFont = LocalArabicFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current

    // Find the item for the selected day
    val selectedDayItem = remember(monthData, selectedHijriDay) {
        monthData.days.filterNotNull().find { it.hijriDayEng == selectedHijriDay }
    }

    // Build complete context for selected day
    val selectedDayContext: IslamicDayContext = remember(selectedDayItem, monthIndex, year, todayCal) {
        val cal = selectedDayItem?.gregorianCal ?: todayCal
        val isToday = (monthIndex == todayDetail.monthIndex && year == todayDetail.year && selectedHijriDay == todayDetail.day)
        IslamicCalendarCatalog.buildDayContext(
            hijriDay = selectedHijriDay,
            hijriMonthIndex = monthIndex,
            hijriYear = year,
            gregorianCal = cal,
            isToday = isToday
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        // -------------------------------------------------------------
        // 1. TOP HEADER: NAVIGATION & MONTH BADGE
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "পূর্ববর্তী মাস",
                        modifier = Modifier.size(13.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${monthProfile.nameBn} (${monthProfile.nameAr})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "(${CalendarHelper.toBanglaNumber(year)} হিজরি)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Quick "আজ" button if navigated away
                        if (!isViewingCurrentMonth) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                onClick = onGoToToday,
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF059669).copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Today,
                                        contentDescription = "আজকের দিনে যান",
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "আজ",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF059669),
                                        fontFamily = banglaFont,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "মাস ${CalendarHelper.toBanglaNumber(monthIndex + 1)}/১২ • ${monthProfile.nameEn.uppercase()}",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            fontFamily = englishFont
                        )
                        if (monthProfile.isSacredMonth) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = IslamicGold.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "পবিত্র ৪ মাসের একটি",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onNextMonth,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "পরবর্তী মাস",
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Month Overview Button
            Surface(
                onClick = { showMonthOverview = !showMonthOverview },
                shape = RoundedCornerShape(12.dp),
                color = if (showMonthOverview) Color(0xFF059669) else Color(0xFF059669).copy(alpha = 0.12f),
                border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (showMonthOverview) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = "মাসের পরিচয়",
                        tint = if (showMonthOverview) Color.White else Color(0xFF059669),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (showMonthOverview) "সংক্ষেপ" else "মাসের তাৎপর্য",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = if (showMonthOverview) Color.White else Color(0xFF059669),
                        fontFamily = banglaFont,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Expandable Month Profile Overview Card
        AnimatedVisibility(visible = showMonthOverview) {
            Column(modifier = Modifier.padding(top = 10.dp)) {
                IslamicMonthOverviewCard(monthProfile = monthProfile)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // -------------------------------------------------------------
        // 2. HORIZONTAL MONTH SELECTOR PILLS (All 12 Months)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            IslamicCalendarCatalog.islamicMonths.forEachIndexed { index, profile ->
                val isSelected = monthIndex == index
                Surface(
                    onClick = { onSelectMonth(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFF059669) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF059669) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = profile.nameBn,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = profile.nameAr,
                            fontSize = 10.sp,
                            color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontFamily = arabicFont
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // -------------------------------------------------------------
        // 3. CONTEXTUAL FILTER CHIPS (Quick Highlighters)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            listOf(
                Triple(CalendarDayFilter.ALL, "সকল দিন", "📅"),
                Triple(CalendarDayFilter.FASTING, "রোযার দিন", "✨"),
                Triple(CalendarDayFilter.HISTORICAL, "ঐতিহাসিক দিন", "📜"),
                Triple(CalendarDayFilter.AYYAM_AL_BEED, "আইয়ামে বীজ (১৩, ১৪, ১৫)", "🌕")
            ).forEach { (filter, label, emoji) ->
                val isSelected = activeFilter == filter
                FilterChip(
                    selected = isSelected,
                    onClick = { activeFilter = filter },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(emoji, fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = banglaFont
                            )
                        }
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF059669),
                        selectedLabelColor = Color.White
                    ),
                    border = BorderStroke(
                        0.8.dp,
                        if (isSelected) Color(0xFF059669) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // -------------------------------------------------------------
        // 4. WEEKDAY HEADERS (Sun..Sat with Arabic names)
        // -------------------------------------------------------------
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            for (i in 0 until 7) {
                val enDay = CalendarMonthProvider.hijriWeekdaysEn[i]
                val arDay = CalendarMonthProvider.hijriWeekdaysAr[i]
                val isJummah = (i == 5) // Friday
                val isSunday = (i == 0) // Sunday

                val headerBg = when {
                    isJummah -> Color(0xFFECFDF5)
                    isSunday -> Color(0xFFFFF1F2)
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                }
                val headerColor = when {
                    isJummah -> Color(0xFF047857)
                    isSunday -> Color(0xFFBE123C)
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = headerBg,
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = enDay,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = headerColor,
                            fontFamily = englishFont
                        )
                        Text(
                            text = arDay,
                            fontSize = 8.sp,
                            color = headerColor.copy(alpha = 0.85f),
                            fontFamily = arabicFont
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // -------------------------------------------------------------
        // 5. CALENDAR DAYS GRID (Clickable with contextual markers)
        // -------------------------------------------------------------
        val rows = monthData.days.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in 0 until 7) {
                        val item = rowItems.getOrNull(i)
                        if (item != null) {
                            val isSelected = item.hijriDayEng == selectedHijriDay
                            val matchesFilter = when (activeFilter) {
                                CalendarDayFilter.ALL -> true
                                CalendarDayFilter.FASTING -> item.isFastingRecommended || item.isFastingForbidden
                                CalendarDayFilter.HISTORICAL -> item.hasHistoricalEvent
                                CalendarDayFilter.AYYAM_AL_BEED -> item.isAyyamAlBeed
                            }

                            ContextualHijriDayCell(
                                item = item,
                                isSelected = isSelected,
                                matchesFilter = matchesFilter,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable {
                                        selectedHijriDay = item.hijriDayEng
                                    }
                            )
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // -------------------------------------------------------------
        // 6. RICH CONTEXTUAL DETAIL CARD: "Islamic Calendar With Meaning"
        // -------------------------------------------------------------
        IslamicDayContextCard(
            context = selectedDayContext,
            onGoToToday = onGoToToday,
            onSelectOtherDay = { selectedHijriDay = it }
        )
    }
}

/**
 * Single Day Cell in the Hijri Grid
 */
@Composable
private fun ContextualHijriDayCell(
    item: HijriDayItem,
    isSelected: Boolean,
    matchesFilter: Boolean,
    modifier: Modifier = Modifier
) {
    val isToday = item.isToday
    val arabicFont = LocalArabicFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current

    val regularBg = when {
        item.colIndex == 0 -> Color(0xFFFFF1F2) // Sunday soft peach
        item.colIndex == 5 -> Color(0xFFECFDF5) // Friday soft mint
        item.colIndex in 1..4 -> Color(0xFFF7FDF9) // Sage ivory
        else -> Color(0xFFEFF6FF) // Saturday soft blue
    }

    val alpha = if (matchesFilter) 1f else 0.35f

    val borderColor = when {
        isSelected -> Color(0xFF059669)
        isToday -> Color(0xFF10B981)
        item.hasHistoricalEvent -> Color(0xFF8B5CF6).copy(alpha = 0.6f)
        item.isFastingRecommended -> Color(0xFF059669).copy(alpha = 0.4f)
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
    }

    val borderWidth = when {
        isSelected -> 2.dp
        isToday -> 1.5.dp
        else -> 1.dp
    }

    Surface(
        modifier = modifier
            .aspectRatio(0.92f)
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        color = if (isToday) Color.Transparent else regularBg.copy(alpha = alpha),
        border = BorderStroke(borderWidth, borderColor),
        shadowElevation = if (isSelected || isToday) 2.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isToday) {
                        Modifier.background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF065F46), Color(0xFF10B981))
                            )
                        )
                    } else if (isSelected) {
                        Modifier.background(Color(0xFF059669).copy(alpha = 0.14f))
                    } else Modifier
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp, vertical = 2.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Indicator dots row: Fasting, History, Full Moon
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, top = 1.dp, end = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left dot: Fasting or Prohibited Fast
                    when {
                        item.isFastingForbidden -> {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(Color(0xFFDC2626), CircleShape)
                            )
                        }
                        item.isFastingRecommended -> {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(if (isToday) Color.White else Color(0xFF059669), CircleShape)
                            )
                        }
                        else -> Spacer(modifier = Modifier.size(5.dp))
                    }

                    // Right dot: Major Historical Event
                    if (item.hasHistoricalEvent) {
                        Box(
                            modifier = Modifier
                                .size(5.dp)
                                .background(if (isToday) IslamicGold else Color(0xFF7C3AED), CircleShape)
                        )
                    } else if (item.isFullMoon) {
                        Text("🌕", fontSize = 6.sp)
                    } else {
                        Spacer(modifier = Modifier.size(5.dp))
                    }
                }

                // Middle: Arabic numeral & Bangla numeral
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.hijriDayArabic,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isToday || isSelected) FontWeight.Black else FontWeight.Bold,
                        color = when {
                            isToday -> Color.White
                            isSelected -> Color(0xFF065F46)
                            else -> Color(0xFF047857)
                        },
                        fontFamily = arabicFont
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "(${item.hijriDayBn})",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isToday) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        fontFamily = banglaFont
                    )
                }

                // Bottom row: "আজ" / "চিহ্নিত" badge & Gregorian sub date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 2.dp, bottom = 1.dp, end = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isToday) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color.White.copy(alpha = 0.35f)
                        ) {
                            Text(
                                text = "আজ",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                            )
                        }
                    } else if (isSelected) {
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFF059669).copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "✓",
                                fontSize = 7.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(horizontal = 3.dp, vertical = 0.5.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Text(
                        text = item.gregorianSubDate,
                        fontSize = 7.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isToday) Color.White.copy(alpha = 0.95f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = englishFont
                    )
                }
            }
        }
    }
}

/**
 * Deep Contextual Card: Meaning, History, Fiqh, Lunar & Worship for the Selected Day
 */
@Composable
private fun IslamicDayContextCard(
    context: IslamicDayContext,
    onGoToToday: () -> Unit,
    onSelectOtherDay: (Int) -> Unit
) {
    val arabicFont = LocalArabicFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current
    val ctx = LocalContext.current

    var selectedTab by remember { mutableStateOf(ContextDetailTab.HISTORY) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.2.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // ---------------------------------------------------------
            // BANNER: Triple Calendar Conversion & Fasting Status Header
            // ---------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF059669).copy(alpha = 0.12f),
                                MaterialTheme.colorScheme.surface
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    // Row with Day badge, Fasting Badge & Jump to Today
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(context.fastingStatus.badgeColorHex),
                            modifier = Modifier.padding(bottom = 4.dp)
                        ) {
                            Text(
                                text = context.fastingStatus.titleBn,
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (!context.isToday) {
                                Surface(
                                    onClick = onGoToToday,
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF059669).copy(alpha = 0.1f),
                                    border = BorderStroke(0.8.dp, Color(0xFF059669).copy(alpha = 0.3f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Today,
                                            contentDescription = null,
                                            tint = Color(0xFF059669),
                                            modifier = Modifier.size(11.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "আজকের দিনে যান",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color(0xFF059669),
                                            fontFamily = banglaFont,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            } else {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF10B981)
                                ) {
                                    Text(
                                        text = "আজকের দিন",
                                        color = Color.White,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Primary Hijri Title
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "${CalendarHelper.toBanglaNumber(context.hijriDay)} ${context.hijriMonthNameBn}, ${CalendarHelper.toBanglaNumber(context.hijriYear)} হিজরি",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "${CalendarHelper.toArabicNumber(context.hijriDay)} ${context.hijriMonthNameAr} ${CalendarHelper.toArabicNumber(context.hijriYear)} هـ",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                fontFamily = arabicFont
                            )
                        }

                        // Lunar Emoji Badge
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(context.lunarInfo.iconEmoji, fontSize = 22.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Triple Conversion Pill Card
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "খ্রিষ্টীয় তারিখ:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = "${CalendarHelper.toBanglaNumber(context.gregorianDay)} ${context.gregorianMonthNameBn} ${CalendarHelper.toBanglaNumber(context.gregorianYear)} (${context.gregorianWeekdayBn})",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(28.dp)
                                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            )

                            Column {
                                Text(
                                    text = "বাংলা সন:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontSize = 9.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = "${context.bengaliDayBn} ${context.bengaliMonthBn}, ${context.bengaliYearBn} (${context.bengaliSeasonBn})",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // ---------------------------------------------------------
            // TABS: History, Fasting, Lunar, Worship, Converter
            // ---------------------------------------------------------
            ScrollableTabRow(
                selectedTabIndex = selectedTab.ordinal,
                edgePadding = 12.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = Color(0xFF059669),
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab.ordinal]),
                        color = Color(0xFF059669),
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                ContextDetailTab.values().forEach { tab ->
                    val isSelected = selectedTab == tab
                    Tab(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(tab.iconEmoji, fontSize = 12.sp)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = tab.titleBn,
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

            // ---------------------------------------------------------
            // TAB CONTENT DISPLAY
            // ---------------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                when (selectedTab) {
                    ContextDetailTab.HISTORY -> {
                        HistoryTabContent(events = context.historicalEvents, dateContext = context)
                    }
                    ContextDetailTab.FASTING -> {
                        FastingTabContent(context = context)
                    }
                    ContextDetailTab.LUNAR -> {
                        LunarTabContent(lunar = context.lunarInfo, context = context)
                    }
                    ContextDetailTab.WORSHIP -> {
                        WorshipTabContent(worshipList = context.recommendedWorship, context = context)
                    }
                    ContextDetailTab.CONVERTER -> {
                        ConverterTabContent(context = context, onSelectOtherDay = onSelectOtherDay)
                    }
                }
            }
        }
    }
}

/**
 * 1. History Tab Content: Rigorous Islamic events with exact sources
 */
@Composable
private fun HistoryTabContent(
    events: List<IslamicHistoricalEvent>,
    dateContext: IslamicDayContext
) {
    val banglaFont = LocalBanglaFontFamily.current

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        if (events.isEmpty()) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "সাধারণ ঐতিহাসিক দিন",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "এই নির্দিষ্ট তারিখে সহীহ সূত্রে কোনো বিশেষ ঐতিহাসিক ঘটনার সনদ সাব্যস্ত নেই। তবে সাধারণ দ্বীনি আমল, নফল ইবাদত ও সিয়াম চালু থাকে।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        } else {
            // Banner indicating authentic sourcing
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF6366F1).copy(alpha = 0.08f),
                border = BorderStroke(0.8.dp, Color(0xFF6366F1).copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.HistoryEdu,
                        contentDescription = null,
                        tint = Color(0xFF6366F1),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ঐতিহাসিক প্রমাণক ও সনদভিত্তিক প্রামাণ্য তথ্য (${CalendarHelper.toBanglaNumber(events.size)}টি ঘটনা সংরক্ষিত)",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4F46E5),
                        fontFamily = banglaFont
                    )
                }
            }

            events.forEach { event ->
                HistoricalEventCard(event = event)
            }
        }
    }
}

/**
 * Historical Event Detailed Card with Certainty Badge and Primary Sources
 */
@Composable
private fun HistoricalEventCard(event: IslamicHistoricalEvent) {
    val banglaFont = LocalBanglaFontFamily.current

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, Color(event.certainty.badgeColorHex).copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header: Title & Certainty Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = event.titleBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(event.certainty.badgeColorHex)
                ) {
                    Text(
                        text = event.certainty.labelBn,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont,
                        fontSize = 9.5.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Date & Year summary
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "সময়কাল: ${event.dateSummaryBn} • ${event.yearDescriptionBn}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = banglaFont
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Body text
            Text(
                text = event.descriptionBn,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                fontFamily = banglaFont
            )

            // Scholarly Note if present
            if (!event.scholarlyNoteBn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = IslamicGold.copy(alpha = 0.12f),
                    border = BorderStroke(0.6.dp, IslamicGold.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("💡", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = event.scholarlyNoteBn,
                            style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                            color = Color(0xFF92400E),
                            fontFamily = banglaFont
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Sources Reference
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0284C7).copy(alpha = 0.10f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF0284C7),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "প্রামাণ্য মূলসূত্র: ${event.primarySourceBn}",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1),
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

/**
 * 2. Fasting Tab Content: Fiqh of fasting, rules, and Hadith
 */
@Composable
private fun FastingTabContent(context: IslamicDayContext) {
    val banglaFont = LocalBanglaFontFamily.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Status Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(context.fastingStatus.badgeColorHex).copy(alpha = 0.12f),
            border = BorderStroke(1.2.dp, Color(context.fastingStatus.badgeColorHex)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "রোযার শরয়ী বিধান:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(context.fastingStatus.badgeColorHex),
                        fontFamily = banglaFont
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(context.fastingStatus.badgeColorHex)
                    ) {
                        Text(
                            text = context.fastingStatus.titleBn,
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = context.fastingRulingDetailsBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("📖", fontSize = 12.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "রেফারেন্স: ${context.fastingReferenceBn}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        // Sunnah Fasting Rules & Etiquettes Reminder
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "রোযার সুন্নাত আদব ও মাসআলা:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                listOf(
                    "সাহরি খাওয়া সুন্নাত ও বরকতপূর্ণ; সুবহে সাদিকের নিকটবর্তী সময়ে সাহরি গ্রহণ করা উত্তম (সহীহ বুখারী ১৯২৩)।",
                    "সূর্যাস্তের সাথে সাথে কালবিলম্ব না করে খেজুর বা পানি দিয়ে ইফতার দ্রুত সম্পন্ন করা সুন্নাহ (সহীহ মুসলিম ১০৯৮)।",
                    "রোযাদার অবস্থায় জিহ্বা ও দৃষ্টির সংযম অপরিহার্য। মিথ্যা, পরনিন্দা ও অনর্থক বাকবিতণ্ডা পরিহার করা ওয়াজিব (সহীহ বুখারী ১৯০৩)।"
                ).forEach { rule ->
                    Text(
                        text = "• $rule",
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

/**
 * 3. Lunar Information Tab: Moon phase, Illumination, Sighting Dua
 */
@Composable
private fun LunarTabContent(lunar: LunarPhaseInfo, context: IslamicDayContext) {
    val arabicFont = LocalArabicFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Moon Phase Card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF0F172A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.radialGradient(
                            listOf(Color(0xFF1E293B), Color(0xFF0F172A))
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = lunar.phaseNameBn,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "${lunar.phaseNameEn} • ${lunar.phaseNameAr}",
                                style = MaterialTheme.typography.labelSmall,
                                color = IslamicGold,
                                fontFamily = arabicFont
                            )
                        }

                        Text(lunar.iconEmoji, fontSize = 38.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Metrics Row: Illumination & Moon Age
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.1f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "আলোকিত অংশ",
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = "${CalendarHelper.toBanglaNumber(lunar.illuminationPercent)}%",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold,
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.1f),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "চান্দ্র বয়স",
                                    fontSize = 9.sp,
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = "${CalendarHelper.toBanglaNumber(lunar.lunarDay)}ম দিন",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = lunar.shariahSignificanceBn,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        color = Color.White.copy(alpha = 0.9f),
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Sunnah Moon Sighting Dua Card
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Nightlight,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "চাঁদ দর্শনের মাসনূন দু'আ (তিরমিযী ৩৪৫১):",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = lunar.moonSightingDuaAr,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = arabicFont,
                    textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "উচ্চারণ: ${lunar.moonSightingDuaBn}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "অর্থ: «${lunar.moonSightingDuaMeaningBn}»",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

/**
 * 4. Recommended Worship Tab: Core Sunnah deeds for the day
 */
@Composable
private fun WorshipTabContent(
    worshipList: List<IslamicWorshipAction>,
    context: IslamicDayContext
) {
    val banglaFont = LocalBanglaFontFamily.current

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        worshipList.forEach { action ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = action.titleBn,
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
                                text = action.categoryBn,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = action.instructionBn,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                        fontFamily = banglaFont
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFF3E8FF),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text("✨", fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "ফযীলত: ${action.virtueBn} (${action.referenceBn})",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF6B21A8),
                                fontFamily = banglaFont,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 5. Interactive Date Converter Tab: Bidirectional Hijri ⇄ Gregorian ⇄ Bengali conversion
 */
@Composable
private fun ConverterTabContent(
    context: IslamicDayContext,
    onSelectOtherDay: (Int) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(
            shape = RoundedCornerShape(14.dp),
            color = Color(0xFF059669).copy(alpha = 0.08f),
            border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = null,
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "হিজরি ⇄ খ্রিষ্টীয় ⇄ বাংলা ত্রিমাত্রিক রূপান্তর",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Current day conversion row
                ConversionResultRow("হিজরি তারিখ:", "${CalendarHelper.toBanglaNumber(context.hijriDay)} ${context.hijriMonthNameBn}, ${CalendarHelper.toBanglaNumber(context.hijriYear)} হিজরি", "🌙")
                ConversionResultRow("খ্রিষ্টীয় তারিখ:", "${CalendarHelper.toBanglaNumber(context.gregorianDay)} ${context.gregorianMonthNameBn} ${CalendarHelper.toBanglaNumber(context.gregorianYear)} (${context.gregorianWeekdayBn})", "📅")
                ConversionResultRow("বাংলা তারিখ:", "${context.bengaliDayBn} ${context.bengaliMonthBn}, ${context.bengaliYearBn} বঙ্গাব্দ (${context.bengaliSeasonBn})", "🌾")
            }
        }

        // Quick Day Jump in this month
        Column {
            Text(
                text = "${context.hijriMonthNameBn} মাসের যেকোনো দিন দ্রুত দেখুন:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (d in 1..30) {
                    val isCur = d == context.hijriDay
                    Surface(
                        onClick = { onSelectOtherDay(d) },
                        shape = RoundedCornerShape(8.dp),
                        color = if (isCur) Color(0xFF059669) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = BorderStroke(0.6.dp, if (isCur) Color(0xFF059669) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = "${CalendarHelper.toBanglaNumber(d)}ই",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = if (isCur) FontWeight.Bold else FontWeight.Normal,
                            color = if (isCur) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversionResultRow(label: String, value: String, emoji: String) {
    val banglaFont = LocalBanglaFontFamily.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = banglaFont
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            fontFamily = banglaFont
        )
    }
}

/**
 * Month Overview Expandable Card explaining the Islamic Month's significance
 */
@Composable
private fun IslamicMonthOverviewCard(monthProfile: IslamicMonthProfile) {
    val arabicFont = LocalArabicFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, Color(0xFF059669).copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${monthProfile.nameBn} মাসের পরিচয় ও তাৎপর্য",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        fontFamily = banglaFont
                    )
                    Text(
                        text = monthProfile.nameAr,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = arabicFont
                    )
                }

                if (monthProfile.isSacredMonth) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.2f),
                        border = BorderStroke(0.6.dp, IslamicGold)
                    ) {
                        Text(
                            text = "সম্মানিত মাস (হুরুম)",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF92400E),
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Etymology
            Text(
                text = "নামকরণ ও ব্যুৎপত্তি: ${monthProfile.meaningAndEtymologyBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Spiritual Theme
            Text(
                text = "মূল আত্মিক প্রতিপাদ্য: ${monthProfile.keySpiritualThemeBn}",
                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF047857),
                fontFamily = banglaFont
            )

            // Sacred month verse if applicable
            if (!monthProfile.sacredMonthVerseBn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = monthProfile.sacredMonthVerseBn,
                        style = MaterialTheme.typography.labelSmall.copy(lineHeight = 18.sp),
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF14532D),
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Special Rulings
            Text(
                text = "মাসের বিশেষ বিধান ও ফযীলত:",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = banglaFont
            )
            Spacer(modifier = Modifier.height(4.dp))
            monthProfile.specialRulingsBn.forEach { ruling ->
                Text(
                    text = "• $ruling",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont,
                    modifier = Modifier.padding(vertical = 1.5.dp)
                )
            }
        }
    }
}
