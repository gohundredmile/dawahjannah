package com.example.ui.components

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.ViewAgenda
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.SalatConfiguration
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Locale

/**
 * Fullscreen Daily Salat & Multi-Year Calendar Dialog replicating the user attachment
 * with Month & Year dropdowns, detailed prayer timings, sun/sawm timings, and forbidden times.
 */
@Composable
fun DailySalatCalendarDialog(
    onDismiss: () -> Unit,
    initialDate: Calendar = Calendar.getInstance(),
    salatConfig: SalatConfiguration = SalatConfiguration()
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val context = LocalContext.current
        val coroutineScope = rememberCoroutineScope()

        val todayCal = remember { Calendar.getInstance() }
        val todayYear = todayCal.get(Calendar.YEAR)
        val todayMonth = todayCal.get(Calendar.MONTH)
        val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)

        // Dropdown selection states
        var selectedYear by remember { mutableIntStateOf(initialDate.get(Calendar.YEAR)) }
        var selectedMonth by remember { mutableIntStateOf(initialDate.get(Calendar.MONTH)) }
        var isGridView by remember { mutableStateOf(false) }

        // Dropdown menus visibility
        var monthDropdownExpanded by remember { mutableStateOf(false) }
        var yearDropdownExpanded by remember { mutableStateOf(false) }

        // Year options list: covering previous years and next years
        val yearOptions = remember(todayYear) {
            val startYear = todayYear - 8 // e.g. 2018
            val endYear = todayYear + 10 // e.g. 2036
            (startYear..endYear).toList()
        }

        val monthNamesBn = CalendarHelper.englishMonthsBn

        // Generate schedule for all days in the selected month & year
        val daysInMonth = remember(selectedYear, selectedMonth, salatConfig) {
            val cal = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, 1)
            }
            val maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
            (1..maxDay).map { d ->
                val dayCal = Calendar.getInstance().apply {
                    set(Calendar.YEAR, selectedYear)
                    set(Calendar.MONTH, selectedMonth)
                    set(Calendar.DAY_OF_MONTH, d)
                }

                val dayOfWeek = dayCal.get(Calendar.DAY_OF_WEEK)
                val weekdayBn = CalendarHelper.englishDaysBn[dayOfWeek] ?: ""

                val gregorianDetail = CalendarHelper.getGregorianDateDetail(dayCal)
                val bengaliDetail = CalendarHelper.getBengaliDateDetail(dayCal)
                val hijriDetail = CalendarHelper.getHijriDateDetail(dayCal)

                val dayBn = CalendarHelper.toBanglaNumber(gregorianDetail.day)
                val monthBn = monthNamesBn.getOrElse(selectedMonth) { "" }
                val yearBn = CalendarHelper.toBanglaNumber(gregorianDetail.year)

                val gregorianHeader = "$dayBn $monthBn, $yearBn – $weekdayBn"

                val hijriDayBn = CalendarHelper.toBanglaNumber(hijriDetail.day)
                val hijriYearBn = CalendarHelper.toBanglaNumber(hijriDetail.year)
                val bengaliDayBn = CalendarHelper.toBanglaNumber(bengaliDetail.day)
                val bengaliYearBn = CalendarHelper.toBanglaNumber(bengaliDetail.year)

                val hijriBengaliSubtitle = "$hijriDayBn ${hijriDetail.monthNameBn}, $hijriYearBn • $bengaliDayBn ${bengaliDetail.monthName}, $bengaliYearBn"

                val isDayToday = (selectedYear == todayYear && selectedMonth == todayMonth && d == todayDay)

                // Prayer timings for this day
                val prayerStatus = PrayerCalculator.calculatePrayers(
                    cal = dayCal,
                    isHanafiAsr = salatConfig.isHanafiAsr,
                    latitude = salatConfig.latitude,
                    longitude = salatConfig.longitude,
                    locationNameBn = salatConfig.placeNameBn,
                    locationNameEn = salatConfig.placeNameEn,
                    isGpsLocation = salatConfig.isGpsEnabled,
                    manualOffsetMinutes = salatConfig.manualOffsetMinutes
                )

                val fajrTime = prayerStatus.prayerList.find { it.id == "fajr" }?.startFormattedBn ?: "০৪:২৯"
                val dhuhrTime = prayerStatus.prayerList.find { it.id == "dhuhr" }?.startFormattedBn ?: "১১:৫৬"
                val asrTime = prayerStatus.prayerList.find { it.id == "asr" }?.startFormattedBn ?: "০৪:১৯"
                val maghribTime = prayerStatus.prayerList.find { it.id == "maghrib" }?.startFormattedBn ?: "০৬:০২"
                val ishaTime = prayerStatus.prayerList.find { it.id == "isha" }?.startFormattedBn ?: "০৭:১৮"

                CalendarHelper.DaySalatScheduleItem(
                    dayOfMonth = d,
                    monthIndex = selectedMonth,
                    year = selectedYear,
                    dayOfWeekBn = weekdayBn,
                    gregorianHeaderDateBn = gregorianHeader,
                    hijriBengaliSubtitleBn = hijriBengaliSubtitle,
                    isToday = isDayToday,
                    fajrTimeBn = fajrTime,
                    dhuhrTimeBn = dhuhrTime,
                    asrTimeBn = asrTime,
                    maghribTimeBn = maghribTime,
                    ishaTimeBn = ishaTime,
                    sunriseTimeBn = prayerStatus.sunriseTimeFormatted,
                    sunsetTimeBn = prayerStatus.sunsetTimeFormatted,
                    sehriTimeBn = fajrTime,
                    iftarTimeBn = prayerStatus.sunsetTimeFormatted,
                    morningForbiddenTimeBn = "${prayerStatus.forbiddenTimeInfo.sunriseStart24} - ${prayerStatus.forbiddenTimeInfo.sunriseEnd24}",
                    noonForbiddenTimeBn = "${prayerStatus.forbiddenTimeInfo.zawalStart24} - ${prayerStatus.forbiddenTimeInfo.zawalEnd24}",
                    eveningForbiddenTimeBn = "${prayerStatus.forbiddenTimeInfo.sunsetStart24} - ${prayerStatus.forbiddenTimeInfo.sunsetEnd24}"
                )
            }
        }

        val listState = rememberLazyListState()

        // Auto-scroll to today if viewing today's month/year
        LaunchedEffect(selectedYear, selectedMonth) {
            if (selectedYear == todayYear && selectedMonth == todayMonth) {
                val targetIndex = (todayDay - 1).coerceAtLeast(0)
                listState.scrollToItem(targetIndex)
            } else {
                listState.scrollToItem(0)
            }
        }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color(0xFFF3F4F6), // Soft light backdrop
            topBar = {
                Surface(
                    color = Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.statusBarsPadding()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "বন্ধ করুন",
                                    tint = Color(0xFF1F2937),
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Text(
                                text = "ক্যালেন্ডার",
                                fontSize = 21.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF111827),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = { isGridView = !isGridView }) {
                                Icon(
                                    imageVector = if (isGridView) Icons.Default.ViewAgenda else Icons.Default.GridView,
                                    contentDescription = if (isGridView) "তালিকা ভিউ" else "মাসিক গ্রিড ভিউ",
                                    tint = Color(0xFF1F2937),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)

                        // Top Dropdown Selection Box (Replicating user attachment)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFF9FAFB),
                            border = BorderStroke(1.dp, Color(0xFFD1D5DB)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Month Dropdown
                                Box(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { monthDropdownExpanded = true }
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = monthNamesBn.getOrElse(selectedMonth) { "সেপ্টেম্বর" },
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1F2937)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select Month",
                                            tint = Color(0xFF374151),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = monthDropdownExpanded,
                                        onDismissRequest = { monthDropdownExpanded = false }
                                    ) {
                                        monthNamesBn.forEachIndexed { index, mName ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = mName,
                                                        fontWeight = if (index == selectedMonth) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (index == selectedMonth) Color(0xFF007A5E) else Color(0xFF111827)
                                                    )
                                                },
                                                onClick = {
                                                    selectedMonth = index
                                                    monthDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }

                                VerticalDivider(
                                    modifier = Modifier
                                        .height(30.dp)
                                        .padding(horizontal = 4.dp),
                                    color = Color(0xFFD1D5DB)
                                )

                                // Year Dropdown (Supports previous years and next years)
                                Box(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { yearDropdownExpanded = true }
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "$selectedYear",
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFF1F2937)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = "Select Year",
                                            tint = Color(0xFF374151),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    DropdownMenu(
                                        expanded = yearDropdownExpanded,
                                        onDismissRequest = { yearDropdownExpanded = false }
                                    ) {
                                        yearOptions.forEach { yr ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "$yr (${CalendarHelper.toBanglaNumber(yr)})",
                                                        fontWeight = if (yr == selectedYear) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (yr == selectedYear) Color(0xFF007A5E) else Color(0xFF111827)
                                                    )
                                                },
                                                onClick = {
                                                    selectedYear = yr
                                                    yearDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Jump to today shortcut chip if not current month/year
                        val isNotCurrentMonthOrYear = (selectedYear != todayYear || selectedMonth != todayMonth)
                        AnimatedVisibility(visible = isNotCurrentMonthOrYear) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 2.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFFDCFCE7),
                                    border = BorderStroke(1.dp, Color(0xFF86EFAC)),
                                    modifier = Modifier.clickable {
                                        selectedYear = todayYear
                                        selectedMonth = todayMonth
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Today,
                                            contentDescription = null,
                                            tint = Color(0xFF15803D),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "আজকের তারিখে যান",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF15803D)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (isGridView) {
                    // Quick Monthly Calendar Grid
                    MonthlyCalendarGridView(
                        year = selectedYear,
                        month = selectedMonth,
                        days = daysInMonth,
                        onDayClick = { dayIndex ->
                            isGridView = false
                            coroutineScope.launch {
                                listState.animateScrollToItem(dayIndex)
                            }
                        }
                    )
                } else {
                    // Detailed Day Cards List (Exactly replicating user attachment)
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        itemsIndexed(
                            items = daysInMonth,
                            key = { _, item -> "${item.year}_${item.monthIndex}_${item.dayOfMonth}" }
                        ) { index, dayItem ->
                            DaySalatCard(
                                item = dayItem,
                                onShare = {
                                    shareDaySchedule(context, dayItem)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Detailed Daily Salat Card matching the user's attachment screenshot
 */
@Composable
private fun DaySalatCard(
    item: CalendarHelper.DaySalatScheduleItem,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. TOP GREEN BANNER (Solid Islamic Green #007A5E / #0D9488)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF007A5E))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                // Row 1: Date, Bangla Day, "আজ" Badge, Share Icon
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.gregorianHeaderDateBn,
                        color = Color.White,
                        fontSize = 16.5.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.2).sp
                    )

                    if (item.isToday) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = CircleShape,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 1.dp)
                        ) {
                            Text(
                                text = "আজ",
                                color = Color(0xFF007A5E),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(
                        onClick = onShare,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার করুন",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Row 2: Hijri Date • Bengali Date
                Text(
                    text = item.hijriBengaliSubtitleBn,
                    color = Color(0xFFE6FFFA),
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            // 2. MAIN BODY PRAYER & SUN TIMINGS
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // A. 5 Waqt Salat Container (ফজর, যুহর, আসর, মাগরিব, ইশা)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        WaqtItemColumn(name = "ফজর", time = item.fajrTimeBn)
                        WaqtItemColumn(name = "যুহর", time = item.dhuhrTimeBn)
                        WaqtItemColumn(name = "আসর", time = item.asrTimeBn)
                        WaqtItemColumn(name = "মাগরিব", time = item.maghribTimeBn)
                        WaqtItemColumn(name = "ইশা", time = item.ishaTimeBn)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // B. Sun & Sawm Timings Row (Two cards side by side)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Card: সূর্যোদয় ও সূর্যাস্ত
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF9FAFB),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DualTimeColumn(label = "সূর্যোদয়", time = item.sunriseTimeBn)
                            VerticalDivider(
                                modifier = Modifier
                                    .height(24.dp)
                                    .padding(horizontal = 2.dp),
                                color = Color(0xFFE5E7EB)
                            )
                            DualTimeColumn(label = "সূর্যাস্ত", time = item.sunsetTimeBn)
                        }
                    }

                    // Right Card: সাহরি ও ইফতার
                    Surface(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF9FAFB),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            DualTimeColumn(label = "সাহরি", time = item.sehriTimeBn)
                            VerticalDivider(
                                modifier = Modifier
                                    .height(24.dp)
                                    .padding(horizontal = 2.dp),
                                color = Color(0xFFE5E7EB)
                            )
                            DualTimeColumn(label = "ইফতার", time = item.iftarTimeBn)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // C. সালাতের নিষিদ্ধ সময় (Forbidden Times)
                Text(
                    text = "সালাতের নিষিদ্ধ সময়",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    modifier = Modifier.padding(bottom = 6.dp)
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF1F2), // Light soft pink
                    border = BorderStroke(1.dp, Color(0xFFFFE4E6)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ForbiddenTimeColumn(label = "সকাল", timeRange = item.morningForbiddenTimeBn)
                        ForbiddenTimeColumn(label = "দুপুর", timeRange = item.noonForbiddenTimeBn)
                        ForbiddenTimeColumn(label = "সন্ধ্যা", timeRange = item.eveningForbiddenTimeBn)
                    }
                }
            }
        }
    }
}

@Composable
private fun WaqtItemColumn(name: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            fontSize = 13.sp,
            color = Color(0xFF4B5563),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = time,
            fontSize = 15.sp,
            color = Color(0xFF111827),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DualTimeColumn(label: String, time: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF6B7280),
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = time,
            fontSize = 14.5.sp,
            color = Color(0xFF111827),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun ForbiddenTimeColumn(label: String, timeRange: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF991B1B),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = timeRange,
            fontSize = 13.sp,
            color = Color(0xFF7F1D1D),
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Clean Calendar Grid view for quick day selection
 */
@Composable
private fun MonthlyCalendarGridView(
    year: Int,
    month: Int,
    days: List<CalendarHelper.DaySalatScheduleItem>,
    onDayClick: (Int) -> Unit
) {
    val weekDaysBn = listOf("রবি", "সোম", "মঙ্গল", "বুধ", "বৃহঃ", "শুক্র", "শনি")

    // Determine first day of week offset
    val firstDayCal = remember(year, month) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val startOffset = (firstDayCal.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY + 7) % 7

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // Weekday Headers
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    weekDaysBn.forEach { w ->
                        Text(
                            text = w,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (w == "শুক্র") Color(0xFF007A5E) else Color(0xFF6B7280),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(36.dp)
                        )
                    }
                }

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = Color(0xFFE5E7EB)
                )

                // Calendar Day Cells
                val totalCells = startOffset + days.size
                val rows = (totalCells + 6) / 7

                for (r in 0 until rows) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        for (c in 0 until 7) {
                            val cellIndex = r * 7 + c
                            val dayIndex = cellIndex - startOffset
                            if (dayIndex in days.indices) {
                                val item = days[dayIndex]
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when {
                                                item.isToday -> Color(0xFF007A5E)
                                                else -> Color.Transparent
                                            }
                                        )
                                        .clickable { onDayClick(dayIndex) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = CalendarHelper.toBanglaNumber(item.dayOfMonth),
                                        fontSize = 15.sp,
                                        fontWeight = if (item.isToday) FontWeight.Bold else FontWeight.Medium,
                                        color = if (item.isToday) Color.White else Color(0xFF111827)
                                    )
                                }
                            } else {
                                Spacer(modifier = Modifier.size(40.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Share day's prayer & calendar timings via Android standard share sheet
 */
private fun shareDaySchedule(context: Context, item: CalendarHelper.DaySalatScheduleItem) {
    val message = buildString {
        appendLine("📅 ${item.gregorianHeaderDateBn}")
        appendLine("🌙 ${item.hijriBengaliSubtitleBn}")
        appendLine()
        appendLine("🕌 সালাতের সময়সূচী:")
        appendLine("• ফজর: ${item.fajrTimeBn}")
        appendLine("• যুহর: ${item.dhuhrTimeBn}")
        appendLine("• আসর: ${item.asrTimeBn}")
        appendLine("• মাগরিব: ${item.maghribTimeBn}")
        appendLine("• ইশা: ${item.ishaTimeBn}")
        appendLine()
        appendLine("🌅 সূর্যোদয়: ${item.sunriseTimeBn} | সূর্যাস্ত: ${item.sunsetTimeBn}")
        appendLine("🌙 সাহরি: ${item.sehriTimeBn} | ইফতার: ${item.iftarTimeBn}")
        appendLine()
        appendLine("⚠️ সালাতের নিষিদ্ধ সময়:")
        appendLine("• সকাল: ${item.morningForbiddenTimeBn}")
        appendLine("• দুপুর: ${item.noonForbiddenTimeBn}")
        appendLine("• সন্ধ্যা: ${item.eveningForbiddenTimeBn}")
        appendLine()
        appendLine("📱 দা'ওয়াহ টু জান্নাহ্ অ্যাপ")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "সালাতের সময়সূচী - ${item.gregorianHeaderDateBn}")
        putExtra(Intent.EXTRA_TEXT, message)
    }
    context.startActivity(Intent.createChooser(intent, "সালাতের ক্যালেন্ডার শেয়ার করুন"))
}
