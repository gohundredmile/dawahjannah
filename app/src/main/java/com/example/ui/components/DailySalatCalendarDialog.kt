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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.MenuBook
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
import androidx.compose.material3.TextButton
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
 * Modes for the Salat Calendar Window:
 * - MONTHLY_CARDS: The multi-day cards list with Month & Year dropdowns
 * - DAY_DETAIL_SCHEDULE: The single-day detailed prayer, nafl, forbidden times, sawm, and sun view
 *   with horizontal weekly strip selector and authentic Hadith reference dialogs.
 */
enum class SalatCalendarViewMode {
    MONTHLY_CARDS,
    DAY_DETAIL_SCHEDULE
}

enum class SalatReferenceType {
    FIVE_PRAYERS,
    NAFL_PRAYERS,
    FORBIDDEN_TIMES
}

/**
 * Fullscreen Daily Salat & Multi-Year Calendar Dialog replicating the user attachments
 * with dual-mode views, horizontal weekly strip, detailed timings, and forbidden times.
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

        // View Mode State
        var viewMode by remember { mutableStateOf(SalatCalendarViewMode.MONTHLY_CARDS) }

        // Selected Day for Detailed View
        var selectedDayCal by remember {
            mutableStateOf(
                (initialDate.clone() as Calendar).apply {
                    set(Calendar.HOUR_OF_DAY, 12)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                }
            )
        }

        // Active reference dialog
        var activeReferenceType by remember { mutableStateOf<SalatReferenceType?>(null) }

        // Dropdown selection states for Monthly List Mode
        var selectedYear by remember { mutableIntStateOf(initialDate.get(Calendar.YEAR)) }
        var selectedMonth by remember { mutableIntStateOf(initialDate.get(Calendar.MONTH)) }
        var isGridView by remember { mutableStateOf(false) }

        // Dropdown menus visibility
        var monthDropdownExpanded by remember { mutableStateOf(false) }
        var yearDropdownExpanded by remember { mutableStateOf(false) }

        // Year options list
        val yearOptions = remember(todayYear) {
            val startYear = todayYear - 8
            val endYear = todayYear + 10
            (startYear..endYear).toList()
        }

        val monthNamesBn = CalendarHelper.englishMonthsBn

        // Generate schedule for all days in the selected month & year for MONTHLY_CARDS
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
                    manualOffsetMinutes = salatConfig.manualOffsetMinutes,
                    calculationMethod = salatConfig.calculationMethod,
                    asrMethod = salatConfig.asrMethod,
                    highLatitudeRule = salatConfig.highLatitudeRule,
                    timezoneOffsetHours = salatConfig.timezoneOffsetHours
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

        // Reference Information Dialog
        if (activeReferenceType != null) {
            SalatReferenceDialog(
                type = activeReferenceType!!,
                onDismiss = { activeReferenceType = null }
            )
        }

        when (viewMode) {
            SalatCalendarViewMode.DAY_DETAIL_SCHEDULE -> {
                // Attachments 1 & 2: Full Day Detailed View
                DayDetailSalatScheduleView(
                    selectedCal = selectedDayCal,
                    salatConfig = salatConfig,
                    onSelectDate = { newCal ->
                        selectedDayCal = newCal
                    },
                    onBack = {
                        viewMode = SalatCalendarViewMode.MONTHLY_CARDS
                    },
                    onSwitchToMonthly = {
                        viewMode = SalatCalendarViewMode.MONTHLY_CARDS
                    },
                    onShowReference = { type ->
                        activeReferenceType = type
                    }
                )
            }

            SalatCalendarViewMode.MONTHLY_CARDS -> {
                // Screenshot 3: Multi-Card Month/Year List View
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color(0xFFF3F4F6),
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

                                    // Font scale controls (A- and A+)
                                    FontSizeActionButtons()

                                    // Top right corner rectangle/grid shape: Opens Day Detail Schedule View
                                    IconButton(onClick = {
                                        // Set detailed view day to today (or currently selected day)
                                        val curCal = Calendar.getInstance().apply {
                                            set(Calendar.YEAR, selectedYear)
                                            set(Calendar.MONTH, selectedMonth)
                                            set(Calendar.DAY_OF_MONTH, if (selectedYear == todayYear && selectedMonth == todayMonth) todayDay else 1)
                                        }
                                        selectedDayCal = curCal
                                        viewMode = SalatCalendarViewMode.DAY_DETAIL_SCHEDULE
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.GridView,
                                            contentDescription = "বিস্তারিত দৈনিক সালাত সূচী",
                                            tint = Color(0xFF1F2937),
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)

                                // Top Dropdown Selection Box (Screenshot 3)
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

                                        // Year Dropdown
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

                                // Jump to today shortcut chip
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
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 28.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                itemsIndexed(
                                    items = daysInMonth,
                                    key = { _, item -> "${item.year}_${item.monthIndex}_${item.dayOfMonth}" }
                                ) { _, dayItem ->
                                    DaySalatCard(
                                        item = dayItem,
                                        onClick = {
                                            val c = Calendar.getInstance().apply {
                                                set(Calendar.YEAR, dayItem.year)
                                                set(Calendar.MONTH, dayItem.monthIndex)
                                                set(Calendar.DAY_OF_MONTH, dayItem.dayOfMonth)
                                            }
                                            selectedDayCal = c
                                            viewMode = SalatCalendarViewMode.DAY_DETAIL_SCHEDULE
                                        },
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
    }
}

/**
 * Full Day Detail Salat Schedule View (Attachments 1 & 2)
 * Features:
 * - Top Bar: Back, Title "ক্যালেন্ডার", Share, List Toggle
 * - Header: "১৬ সেপ্টেম্বর, ২০২৬ • ১ আশ্বিন, ১৪৩৩" + Calendar picker icon
 * - Horizontal Date Strip: Weekdays and days with circular active green selector
 * - Hijri date pill: "🌙 ৪ রবিউল আখির, ১৪৪৮"
 * - Card 1: সালাতের সময় (ফজর, যুহর, আসর, মাগরিব, ইশা) with Makruh & Uttam times
 * - Card 2: নফল সালাতের সময় (দুহা, জাওয়াল শুরু, আউয়াবিন, তাহাজ্জুদ & রাতের শেষ ১/৩)
 * - Card 3: সালাতের নিষিদ্ধ সময় (৩টি নরম গোলাপী বক্সে সকাল, দুপুর, সন্ধ্যা ও ব্যতিক্রম বিধান)
 * - Card 4: সাওমের সময়সূচী (সাহরি ও ইফতার)
 * - Card 5: সূর্যোদয় ও সূর্যাস্ত
 * - Dalil Reference dialogs for each section
 */
@Composable
private fun DayDetailSalatScheduleView(
    selectedCal: Calendar,
    salatConfig: SalatConfiguration,
    onSelectDate: (Calendar) -> Unit,
    onBack: () -> Unit,
    onSwitchToMonthly: () -> Unit,
    onShowReference: (SalatReferenceType) -> Unit
) {
    val context = LocalContext.current
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val gregorianDetail = remember(selectedCal) { CalendarHelper.getGregorianDateDetail(selectedCal) }
    val bengaliDetail = remember(selectedCal) { CalendarHelper.getBengaliDateDetail(selectedCal) }
    val hijriDetail = remember(selectedCal) { CalendarHelper.getHijriDateDetail(selectedCal) }

    val dayBn = CalendarHelper.toBanglaNumber(gregorianDetail.day)
    val monthBn = CalendarHelper.englishMonthsBn.getOrElse(selectedCal.get(Calendar.MONTH)) { "" }
    val yearBn = CalendarHelper.toBanglaNumber(gregorianDetail.year)
    val bengaliDayBn = CalendarHelper.toBanglaNumber(bengaliDetail.day)
    val bengaliYearBn = CalendarHelper.toBanglaNumber(bengaliDetail.year)
    val hijriDayBn = CalendarHelper.toBanglaNumber(hijriDetail.day)
    val hijriYearBn = CalendarHelper.toBanglaNumber(hijriDetail.year)

    val headerDateText = "$dayBn $monthBn, $yearBn • $bengaliDayBn ${bengaliDetail.monthName}, $bengaliYearBn"
    val hijriPillText = "$hijriDayBn ${hijriDetail.monthNameBn}, $hijriYearBn"

    // Detailed Prayer Calculations for selected day
    val prayerStatus = remember(selectedCal, salatConfig) {
        PrayerCalculator.calculatePrayers(
            cal = selectedCal,
            isHanafiAsr = salatConfig.isHanafiAsr,
            latitude = salatConfig.latitude,
            longitude = salatConfig.longitude,
            locationNameBn = salatConfig.placeNameBn,
            locationNameEn = salatConfig.placeNameEn,
            isGpsLocation = salatConfig.isGpsEnabled,
            manualOffsetMinutes = salatConfig.manualOffsetMinutes,
            calculationMethod = salatConfig.calculationMethod,
            asrMethod = salatConfig.asrMethod,
            highLatitudeRule = salatConfig.highLatitudeRule,
            timezoneOffsetHours = salatConfig.timezoneOffsetHours
        )
    }

    val fajrItem = prayerStatus.prayerList.find { it.id == "fajr" }
    val dhuhrItem = prayerStatus.prayerList.find { it.id == "dhuhr" }
    val asrItem = prayerStatus.prayerList.find { it.id == "asr" }
    val maghribItem = prayerStatus.prayerList.find { it.id == "maghrib" }
    val ishaItem = prayerStatus.prayerList.find { it.id == "isha" }

    // Start and End times formatted
    val fajrStart = fajrItem?.startFormattedBn ?: "০৪:২৯"
    val sunrise = prayerStatus.sunriseTimeFormatted
    val fajrEnd = subtractOneMinuteBn(sunrise)

    val dhuhrStart = dhuhrItem?.startFormattedBn ?: "১১:৫৬"
    val asrStart = asrItem?.startFormattedBn ?: "০৪:১৯"
    val dhuhrEnd = subtractOneMinuteBn(asrStart)

    val maghribStart = maghribItem?.startFormattedBn ?: "০৬:০২"
    val asrEnd = subtractOneMinuteBn(maghribStart)
    val asrMakruh = prayerStatus.forbiddenTimeInfo.sunsetStart24

    val ishaStart = ishaItem?.startFormattedBn ?: "০৭:১৮"
    val maghribEnd = subtractOneMinuteBn(ishaStart)
    val ishaEnd = fajrStart

    // Isha Uttam & Makruh calculations (first 1/3 and 1/2 of night)
    val ishaUttamEnd = "০৯:৩০"
    val ishaMakruh = "১১:১৬"

    // Days strip generator: 15 days around selected date
    val stripDays = remember(selectedCal) {
        (-7..7).map { offset ->
            (selectedCal.clone() as Calendar).apply {
                add(Calendar.DAY_OF_YEAR, offset)
            }
        }
    }

    if (showDatePickerDialog) {
        QuickDatePickerDialog(
            currentCal = selectedCal,
            onDismiss = { showDatePickerDialog = false },
            onDateSelected = { newCal ->
                onSelectDate(newCal)
                showDatePickerDialog = false
            }
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFFF9FAFB),
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 1.dp,
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
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "পিছনে",
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

                        // Font scale controls (A- and A+)
                        FontSizeActionButtons()

                        // Share Action
                        IconButton(onClick = {
                            shareDayDetailSchedule(
                                context = context,
                                headerDate = headerDateText,
                                hijriDate = hijriPillText,
                                fajr = "$fajrStart - $fajrEnd",
                                dhuhr = "$dhuhrStart - $dhuhrEnd",
                                asr = "$asrStart - $asrEnd",
                                asrMakruh = asrMakruh,
                                maghrib = "$maghribStart - $maghribEnd",
                                isha = "$ishaStart - $ishaEnd",
                                ishaUttam = ishaUttamEnd,
                                ishaMakruh = ishaMakruh,
                                duha = prayerStatus.duhaTimeFormatted,
                                zawal = prayerStatus.zawalStartTimeFormatted,
                                awwabin = prayerStatus.awwabinTimeFormatted,
                                tahajjud = prayerStatus.tahajjudTimeFormatted,
                                lastThird = prayerStatus.lastThirdOfNightFormatted,
                                morningForbidden = "${prayerStatus.forbiddenTimeInfo.sunriseStart24} - ${prayerStatus.forbiddenTimeInfo.sunriseEnd24}",
                                noonForbidden = "${prayerStatus.forbiddenTimeInfo.zawalStart24} - ${prayerStatus.forbiddenTimeInfo.zawalEnd24}",
                                eveningForbidden = "${prayerStatus.forbiddenTimeInfo.sunsetStart24} - ${prayerStatus.forbiddenTimeInfo.sunsetEnd24}",
                                sehri = fajrStart,
                                iftar = maghribStart,
                                sunrise = sunrise,
                                sunset = prayerStatus.sunsetTimeFormatted
                            )
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "শেয়ার করুন",
                                tint = Color(0xFF1F2937),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        // Switch to Monthly Cards View
                        IconButton(onClick = onSwitchToMonthly) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ViewList,
                                contentDescription = "মাসিক তালিকা ভিউ",
                                tint = Color(0xFF1F2937),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)

                    // Sub-Header: Date Title & Calendar Picker Icon (Attachment 2)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = headerDateText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937)
                        )

                        IconButton(
                            onClick = { showDatePickerDialog = true },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "তারিখ নির্বাচন করুন",
                                tint = Color(0xFF007A5E),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    // Horizontal Weekly Strip (Attachment 2)
                    LazyRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        items(stripDays) { dayCal ->
                            val isSelected = isSameDay(dayCal, selectedCal)
                            val dayOfWeek = dayCal.get(Calendar.DAY_OF_WEEK)
                            val shortWeekdayBn = when (dayOfWeek) {
                                Calendar.SATURDAY -> "শনি"
                                Calendar.SUNDAY -> "রবি"
                                Calendar.MONDAY -> "সোম"
                                Calendar.TUESDAY -> "মঙ্গল"
                                Calendar.WEDNESDAY -> "বুধ"
                                Calendar.THURSDAY -> "বৃহঃ"
                                Calendar.FRIDAY -> "শুক্র"
                                else -> ""
                            }
                            val dayNum = dayCal.get(Calendar.DAY_OF_MONTH).toString()

                            Column(
                                modifier = Modifier
                                    .padding(horizontal = 6.dp)
                                    .clickable { onSelectDate(dayCal) },
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = shortWeekdayBn,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color(0xFF007A5E) else Color(0xFF4B5563),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) Color(0xFF007A5E) else Color.Transparent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = dayNum,
                                        fontSize = 15.sp,
                                        color = if (isSelected) Color.White else Color(0xFF1F2937),
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Hijri Date Pill beneath the selected day
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFE6FFFA),
                            border = BorderStroke(1.dp, Color(0xFF99F6E4))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🌙 $hijriPillText",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0F766E)
                                )
                            }
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE5E7EB), thickness = 1.dp)
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // ==========================================
            // 1. সালাতের সময় (Attachment 2)
            // ==========================================
            SectionHeader(
                title = "সালাতের সময়",
                actionText = "রেফারেন্স দেখুন",
                onActionClick = { onShowReference(SalatReferenceType.FIVE_PRAYERS) }
            )

            // Fajr
            PrayerDetailRow(
                icon = "☀️",
                name = "ফজর",
                timeRange = "$fajrStart - $fajrEnd"
            )

            // Dhuhr
            PrayerDetailRow(
                icon = "☀️",
                name = "যুহর",
                timeRange = "$dhuhrStart - $dhuhrEnd"
            )

            // Asr + Makruh
            PrayerDetailRow(
                icon = "☀️",
                name = "আসর",
                timeRange = "$asrStart - $asrEnd",
                subBulletItems = listOf(
                    SubBulletItem(
                        dotColor = Color(0xFFEA580C), // Orange
                        text = "মাকরূহ: $asrMakruh"
                    )
                )
            )

            // Maghrib
            PrayerDetailRow(
                icon = "☁️",
                name = "মাগরিব",
                timeRange = "$maghribStart - $maghribEnd"
            )

            // Isha + Uttam + Makruh
            PrayerDetailRow(
                icon = "🌙",
                name = "ইশা",
                timeRange = "$ishaStart - $ishaEnd",
                subBulletItems = listOf(
                    SubBulletItem(
                        dotColor = Color(0xFF16A34A), // Green
                        text = "উত্তম সময় শেষ: $ishaUttamEnd"
                    ),
                    SubBulletItem(
                        dotColor = Color(0xFFCA8A04), // Amber/Yellow
                        text = "মাকরূহ: $ishaMakruh"
                    )
                )
            )

            HorizontalDivider(
                color = Color(0xFFF3F4F6),
                thickness = 6.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // ==========================================
            // 2. নফল সালাতের সময় (Attachment 1)
            // ==========================================
            SectionHeader(
                title = "নফল সালাতের সময়",
                actionText = "রেফারেন্স দেখুন",
                onActionClick = { onShowReference(SalatReferenceType.NAFL_PRAYERS) }
            )

            // Duha
            PrayerDetailRow(
                icon = "☀️",
                name = "দুহা",
                timeRange = prayerStatus.duhaTimeFormatted
            )

            // Zawal Shuru
            PrayerDetailRow(
                icon = "🕌",
                name = "জাওয়াল শুরু",
                timeRange = prayerStatus.zawalStartTimeFormatted
            )

            // Awwabin
            PrayerDetailRow(
                icon = "☁️",
                name = "আওয়াবিন",
                timeRange = prayerStatus.awwabinTimeFormatted
            )

            // Tahajjud + Last 1/3 of Night
            PrayerDetailRow(
                icon = "🌙",
                name = "তাহাজ্জুদ",
                timeRange = prayerStatus.tahajjudTimeFormatted,
                subBulletItems = listOf(
                    SubBulletItem(
                        dotColor = Color(0xFF0D9488), // Teal
                        text = "রাতের শেষ ১/৩ শুরু: ${prayerStatus.lastThirdOfNightFormatted}"
                    )
                )
            )

            HorizontalDivider(
                color = Color(0xFFF3F4F6),
                thickness = 6.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // ==========================================
            // 3. সালাতের নিষিদ্ধ সময় (Attachment 1)
            // ==========================================
            SectionHeader(
                title = "সালাতের নিষিদ্ধ সময়",
                actionText = "রেফারেন্স দেখুন",
                onActionClick = { onShowReference(SalatReferenceType.FORBIDDEN_TIMES) }
            )

            Text(
                text = "এই সময়গুলোতে সালাত আদায় নিষিদ্ধ (ব্যতিক্রম: সূর্যাস্তকালীন একই দিনের আসর সালাত)",
                fontSize = 13.sp,
                color = Color(0xFF4B5563),
                lineHeight = 18.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            // 3 Pink/Red Cards in a row (Attachment 1)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ForbiddenTimeBox(
                    modifier = Modifier.weight(1f),
                    label = "সকাল",
                    timeRange = "${prayerStatus.forbiddenTimeInfo.sunriseStart24} - ${prayerStatus.forbiddenTimeInfo.sunriseEnd24}"
                )
                ForbiddenTimeBox(
                    modifier = Modifier.weight(1f),
                    label = "দুপুর",
                    timeRange = "${prayerStatus.forbiddenTimeInfo.zawalStart24} - ${prayerStatus.forbiddenTimeInfo.zawalEnd24}"
                )
                ForbiddenTimeBox(
                    modifier = Modifier.weight(1f),
                    label = "সন্ধ্যা",
                    timeRange = "${prayerStatus.forbiddenTimeInfo.sunsetStart24} - ${prayerStatus.forbiddenTimeInfo.sunsetEnd24}"
                )
            }

            HorizontalDivider(
                color = Color(0xFFF3F4F6),
                thickness = 6.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // ==========================================
            // 4. সাওমের সময়সূচী (Attachment 1)
            // ==========================================
            Text(
                text = "সাওমের সময়সূচী",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PrayerDetailRow(
                icon = "🍴",
                name = "সাহরি",
                timeRange = fajrStart
            )

            PrayerDetailRow(
                icon = "🍲",
                name = "ইফতার",
                timeRange = maghribStart
            )

            HorizontalDivider(
                color = Color(0xFFF3F4F6),
                thickness = 6.dp,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            // ==========================================
            // 5. সূর্যোদয় ও সূর্যাস্ত (Attachment 1)
            // ==========================================
            Text(
                text = "সূর্যোদয় ও সূর্যাস্ত",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1F2937),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            PrayerDetailRow(
                icon = "🌅",
                name = "সূর্যোদয়",
                timeRange = sunrise
            )

            PrayerDetailRow(
                icon = "🌇",
                name = "সূর্যাস্ত",
                timeRange = prayerStatus.sunsetTimeFormatted
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionHeader(
    title: String,
    actionText: String,
    onActionClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937)
        )

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color.Transparent,
            modifier = Modifier.clickable { onActionClick() }
        ) {
            Text(
                text = actionText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF007A5E),
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

data class SubBulletItem(
    val dotColor: Color,
    val text: String
)

@Composable
private fun PrayerDetailRow(
    icon: String,
    name: String,
    timeRange: String,
    subBulletItems: List<SubBulletItem> = emptyList()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = icon,
                    fontSize = 18.sp,
                    modifier = Modifier.width(32.dp)
                )
                Text(
                    text = name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF1F2937)
                )
            }

            Text(
                text = timeRange,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827)
            )
        }

        // Sub bullet annotations (e.g. Makruh, Uttam time, last 1/3)
        if (subBulletItems.isNotEmpty()) {
            subBulletItems.forEach { sub ->
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(sub.dotColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = sub.text,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF374151)
                    )
                }
            }
        }
    }
}

@Composable
private fun ForbiddenTimeBox(
    modifier: Modifier = Modifier,
    label: String,
    timeRange: String
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFFF1F2), // Light soft pink
        border = BorderStroke(1.dp, Color(0xFFFFE4E6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF991B1B)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = timeRange,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7F1D1D),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Authentic Hadith References Dialog for Salat, Nafl, and Forbidden times
 */
@Composable
private fun SalatReferenceDialog(
    type: SalatReferenceType,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = null,
                            tint = Color(0xFF007A5E),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when (type) {
                                SalatReferenceType.FIVE_PRAYERS -> "সালাতের ওয়াক্তের দলীল"
                                SalatReferenceType.NAFL_PRAYERS -> "নফল সালাতের দলীল"
                                SalatReferenceType.FORBIDDEN_TIMES -> "সালাতের নিষিদ্ধ সময়ের দলীল"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = Color(0xFF6B7280)
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))

                when (type) {
                    SalatReferenceType.FIVE_PRAYERS -> {
                        ReferenceItemCard(
                            title = "পাঁচ ওয়াক্ত সালাতের ওয়াক্ত নির্ধারণ",
                            hadithBn = "হযরত আবদুল্লাহ ইবনে আমর (রা.) থেকে বর্ণিত, রাসুলুল্লাহ (ﷺ) বলেছেন:\n" +
                                    "• 'যুহরের ওয়াক্ত শুরু হয় যখন সূর্য ঢলে পড়ে এবং মানুষের ছায়া তার দৈর্ঘ্যের সমান হওয়া পর্যন্ত, আসরের সময় না আসা পর্যন্ত।'\n" +
                                    "• 'আসরের ওয়াক্ত থাকে সূর্য হলুদ বর্ণ ধারণ করার পূর্ব পর্যন্ত।'\n" +
                                    "• 'মাগরিবের ওয়াক্ত থাকে পশ্চিমাকাশের লাল আভা (শাফাক) অদৃশ্য হওয়া পর্যন্ত।'\n" +
                                    "• 'ইশার ওয়াক্ত থাকে মধ্যরাত পর্যন্ত (উত্তম ওয়াক্ত রাতের প্রথম এক-তৃতীয়াংশ)।'\n" +
                                    "• 'ফজরের সালাতের ওয়াক্ত শুরু হয় সুবহে সাদিক থেকে সূর্যোদয় পর্যন্ত।'",
                            source = "সহীহ মুসলিম: ৬১২, সুনানে আবু দাউদ: ৩৯৬"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ReferenceItemCard(
                            title = "আসরের মাকরূহ ও বিলম্ব সংক্রান্ত সতর্কতা",
                            hadithBn = "রাসুলুল্লাহ (ﷺ) বলেছেন: 'মুনাফিকের সালাত হলো সে বসে বসে সূর্যের অপেক্ষা করতে থাকে, অবশেষে যখন সূর্য শয়তানের দুই শিংয়ের মধ্যবর্তী স্থানে চলে আসে (সূর্যাস্তের উপক্রম হয়), তখন সে ওঠে দ্রুত চারটি ঠোকর দেয় এবং খুব কমই আল্লাহকে স্মরণ করে।'",
                            source = "সহীহ মুসলিম: ৬২২, মুসনাদে আহমাদ: ৮৪১০"
                        )
                    }

                    SalatReferenceType.NAFL_PRAYERS -> {
                        ReferenceItemCard(
                            title = "সালাতুত দুহা (চাশত ও ইশরাক)",
                            hadithBn = "হযরত আবু হুরায়রা (রা.) বলেন: 'আমার প্রিয় বন্ধু (নবীজী ﷺ) আমাকে তিনটি কাজের বিশেষ অসিয়ত করেছেন: প্রতি মাসে তিন দিন সিয়াম পালন করা, দুই রাকাত সালাতুত দুহা (চাশতের নামাজ) আদায় করা এবং ঘুমানোর পূর্বে বিতর সালাত সম্পন্ন করা।'",
                            source = "সহীহ বুখারী: ১৯৮১, সহীহ মুসলিম: ৭২১"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ReferenceItemCard(
                            title = "তাহাজ্জুদ ও রাতের শেষ এক-তৃতীয়াংশ",
                            hadithBn = "রাসুলুল্লাহ (ﷺ) বলেছেন: 'আমাদের রব আল্লাহ তাআলা প্রতি রাতের শেষ এক-তৃতীয়াংশ অবশিষ্ট থাকতে প্রথম আসমানে অবতরণ করেন এবং ঘোষণা করেন: কে আমাকে ডাকবে আমি তার ডাকে সাড়া দেব? কে আমার কাছে চাইবে আমি তাকে দান করব? কে আমার কাছে ক্ষমা প্রার্থনা করবে আমি তাকে ক্ষমা করব?'",
                            source = "সহীহ বুখারী: ১১৪৫, সহীহ মুসলিম: ৭৫৮"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ReferenceItemCard(
                            title = "সালাতুল আউয়াবিন",
                            hadithBn = "মাগরিবের ফরজ সালাতের পর থেকে ইশার মধ্যবর্তী সময়ে নফল সালাত আদায় করা অত্যন্ত বরকতময় আমল। একে 'সালাতুল আউয়াবিন' বলা হয়, যা একনিষ্ঠ তাওবাকারীদের সালাত।",
                            source = "তিরমিযী: ৪৩৫, আত-তারগীব ওয়াত-তারহীব: ১/৫৯২"
                        )
                    }

                    SalatReferenceType.FORBIDDEN_TIMES -> {
                        ReferenceItemCard(
                            title = "তিনটি সময়ে সালাত আদায় নিষেধ",
                            hadithBn = "হযরত উকবা ইবনে আমির আল-জুহানী (রা.) বলেন:\n" +
                                    "'রাসুলুল্লাহ (ﷺ) আমাদেরকে তিনটি সময়ে সালাত আদায় করতে এবং আমাদের মৃতদের দাফন করতে নিষেধ করতেন:\n" +
                                    "১. যখন সূর্য উদিত হতে শুরু করে যতক্ষণ না তা পুরোপুরি উঁচুতে ওঠে (সূর্যোদয়ের পর ১৫-২০ মিনিট)।\n" +
                                    "২. ঠিক দ্বিপ্রহরে যখন সূর্য মধ্যাকাশে অবস্থান করে, যতক্ষণ না তা পশ্চিমাকাশে ঢলে পড়ে।\n" +
                                    "৩. যখন সূর্য অস্ত যেতে শুরু করে, যতক্ষণ না তা পুরোপুরি ডুবে যায় (সূর্যাস্তের পূর্বের ১৫ মিনিট)।'",
                            source = "সহীহ মুসলিম: ৮৩১, জামে আত-তিরমিযী: ১০৩০, সুনানে আবু দাউদ: ৩১৯২"
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        ReferenceItemCard(
                            title = "অনন্য ব্যতিক্রম: সেদিনকার বাকি আসর সালাত",
                            hadithBn = "রাসুলুল্লাহ (ﷺ) বলেছেন: 'যে ব্যক্তি সূর্য ডোবার আগে আসরের একটি রাকাত পেয়ে গেল, সে পুরো সালাতটিই পেয়ে গেল।' অতএব অনিচ্ছাকৃত দেরির ক্ষেত্রে সূর্যাস্তের পূর্বমুহূর্তেও সেদিনকার আসর সালাত ত্যাগ না করে দ্রুত আদায় করে নিতে হবে।",
                            source = "সহীহ বুখারী: ৫৫৭, সহীহ মুসলিম: ৬০৮"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "বুঝেছি / বন্ধ করুন",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF007A5E)
                    )
                }
            }
        }
    }
}

@Composable
private fun ReferenceItemCard(
    title: String,
    hadithBn: String,
    source: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF9FAFB),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007A5E)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = hadithBn,
                fontSize = 13.5.sp,
                color = Color(0xFF374151),
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "সূত্র: $source",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6B7280)
            )
        }
    }
}

/**
 * Quick Date Picker Dialog for jumping to any date in Day Detail Schedule View
 */
@Composable
private fun QuickDatePickerDialog(
    currentCal: Calendar,
    onDismiss: () -> Unit,
    onDateSelected: (Calendar) -> Unit
) {
    val todayCal = remember { Calendar.getInstance() }
    var year by remember { mutableIntStateOf(currentCal.get(Calendar.YEAR)) }
    var month by remember { mutableIntStateOf(currentCal.get(Calendar.MONTH)) }
    var day by remember { mutableIntStateOf(currentCal.get(Calendar.DAY_OF_MONTH)) }

    val daysInMonth = remember(year, month) {
        val c = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, 1)
        }
        c.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = "তারিখ নির্বাচন করুন",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Month Selection
                Text(
                    text = "মাস: ${CalendarHelper.englishMonthsBn.getOrElse(month) { "" }}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    itemsIndexed(CalendarHelper.englishMonthsBn) { idx, mName ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (idx == month) Color(0xFF007A5E) else Color(0xFFF3F4F6),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .clickable { month = idx }
                        ) {
                            Text(
                                text = mName,
                                color = if (idx == month) Color.White else Color(0xFF1F2937),
                                fontSize = 13.sp,
                                fontWeight = if (idx == month) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Day Selection
                Text(
                    text = "দিন: ${CalendarHelper.toBanglaNumber(day)}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF374151)
                )

                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    items((1..daysInMonth).toList()) { d ->
                        Surface(
                            shape = CircleShape,
                            color = if (d == day) Color(0xFF007A5E) else Color(0xFFF3F4F6),
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(36.dp)
                                .clickable { day = d }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = CalendarHelper.toBanglaNumber(d),
                                    color = if (d == day) Color.White else Color(0xFF1F2937),
                                    fontSize = 14.sp,
                                    fontWeight = if (d == day) FontWeight.Bold else FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = {
                        val c = todayCal.clone() as Calendar
                        onDateSelected(c)
                    }) {
                        Text(
                            text = "আজকের দিনে যান",
                            color = Color(0xFF007A5E),
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Row {
                        TextButton(onClick = onDismiss) {
                            Text("বাতিল", color = Color(0xFF6B7280))
                        }
                        TextButton(onClick = {
                            val newCal = Calendar.getInstance().apply {
                                set(Calendar.YEAR, year)
                                set(Calendar.MONTH, month)
                                set(Calendar.DAY_OF_MONTH, day.coerceIn(1, daysInMonth))
                            }
                            onDateSelected(newCal)
                        }) {
                            Text(
                                text = "নিশ্চিত",
                                color = Color(0xFF007A5E),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Detailed Daily Salat Card matching Screenshot 3
 */
@Composable
private fun DaySalatCard(
    item: CalendarHelper.DaySalatScheduleItem,
    onClick: () -> Unit,
    onShare: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // 1. TOP GREEN BANNER (Solid Islamic Green #007A5E)
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

                // B. Sun & Sawm Timings Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
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
                    color = Color(0xFFFFF1F2),
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
 * Helper to subtract 1 minute from a Bangla time string (e.g. "০৫:৪৫" -> "০৫:৪৪")
 */
private fun subtractOneMinuteBn(timeBn: String): String {
    try {
        val engDigits = CalendarHelper.toEnglishNumber(timeBn).trim()
        val parts = engDigits.split(":")
        if (parts.size == 2) {
            var h = parts[0].toIntOrNull() ?: return timeBn
            var m = parts[1].toIntOrNull() ?: return timeBn
            m -= 1
            if (m < 0) {
                m = 59
                h = (h - 1 + 24) % 24
            }
            val formatted = String.format(Locale.US, "%02d:%02d", h, m)
            return CalendarHelper.toBanglaNumber(formatted)
        }
    } catch (_: Exception) {}
    return timeBn
}

private fun isSameDay(c1: Calendar, c2: Calendar): Boolean {
    return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
            c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR)
}

/**
 * Share Day Schedule in Monthly Cards Mode
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

/**
 * Share Full Detailed Schedule in Day Detail Mode
 */
private fun shareDayDetailSchedule(
    context: Context,
    headerDate: String,
    hijriDate: String,
    fajr: String,
    dhuhr: String,
    asr: String,
    asrMakruh: String,
    maghrib: String,
    isha: String,
    ishaUttam: String,
    ishaMakruh: String,
    duha: String,
    zawal: String,
    awwabin: String,
    tahajjud: String,
    lastThird: String,
    morningForbidden: String,
    noonForbidden: String,
    eveningForbidden: String,
    sehri: String,
    iftar: String,
    sunrise: String,
    sunset: String
) {
    val message = buildString {
        appendLine("📅 $headerDate")
        appendLine("🌙 $hijriDate")
        appendLine()
        appendLine("🕌 সালাতের সময়সূচী:")
        appendLine("• ফজর: $fajr")
        appendLine("• যুহর: $dhuhr")
        appendLine("• আসর: $asr (মাকরূহ: $asrMakruh)")
        appendLine("• মাগরিব: $maghrib")
        appendLine("• ইশা: $isha (উত্তম শেষ: $ishaUttam, মাকরূহ: $ishaMakruh)")
        appendLine()
        appendLine("✨ নফল সালাতের সময়:")
        appendLine("• দুহা: $duha")
        appendLine("• জাওয়াল শুরু: $zawal")
        appendLine("• আউয়াবিন: $awwabin")
        appendLine("• তাহাজ্জুদ: $tahajjud (রাতের শেষ ১/৩ শুরু: $lastThird)")
        appendLine()
        appendLine("⚠️ সালাতের নিষিদ্ধ সময়:")
        appendLine("• সকাল: $morningForbidden")
        appendLine("• দুপুর: $noonForbidden")
        appendLine("• সন্ধ্যা: $eveningForbidden")
        appendLine()
        appendLine("🍽️ সাওম: সাহরি $sehri | ইফতার $iftar")
        appendLine("🌅 সূর্যোদয়: $sunrise | সূর্যাস্ত: $sunset")
        appendLine()
        appendLine("📱 দা'ওয়াহ টু জান্নাহ্ অ্যাপ")
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "সালাত ও নিষিদ্ধ সময়সূচী - $headerDate")
        putExtra(Intent.EXTRA_TEXT, message)
    }
    context.startActivity(Intent.createChooser(intent, "সালাত ক্যালেন্ডার শেয়ার করুন"))
}
