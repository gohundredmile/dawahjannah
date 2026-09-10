package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.ui.theme.LocalAppFontFamily
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.theme.LocalEnglishFontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.util.BengaliDayItem
import com.example.util.CalendarHelper
import com.example.util.CalendarMonthProvider
import com.example.util.CalendarViewType
import com.example.util.GregorianDayItem
import com.example.util.HijriDayItem
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun DateTimeMasterCard(
    calendarInfo: CalendarHelper.TripleCalendarInfo,
    modifier: Modifier = Modifier
) {
    // Current time ticker state
    var currentTime by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

    // Active expanded calendar
    var expandedCalendar by remember { mutableStateOf(CalendarViewType.NONE) }

    // State controlling whether the calendar is split into 3 cards or collapsed into 1 unified card
    var isSplitIntoThree by remember { mutableStateOf(false) }

    // Dynamic date calculations based on currentTime
    val cal = remember(currentTime) { Calendar.getInstance().apply { time = currentTime } }
    val currentDayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
    val currentMonth = cal.get(Calendar.MONTH)
    val currentYear = cal.get(Calendar.YEAR)

    val banglaDayStr = CalendarHelper.toBanglaNumber(currentDayOfMonth)
    val bengaliMonthNames = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )
    val engMonthBn = bengaliMonthNames.getOrElse(currentMonth) { "সেপ্টেম্বর" }
    val banglaYearStr = CalendarHelper.toBanglaNumber(currentYear)
    val dynamicGregorianDateBn = "$banglaDayStr $engMonthBn $banglaYearStr খ্রিস্টাব্দ"

    val todayGreg = remember(currentTime) { CalendarHelper.getGregorianDateDetail(cal) }
    val todayBengali = remember(currentTime) { CalendarHelper.getBengaliDateDetail(cal) }
    val todayHijri = remember(currentTime) { CalendarHelper.getHijriDateDetail(cal) }

    // Navigation states for expanded views dynamically initialized to real today's date
    var gregMonthIndex by remember(currentTime) { mutableIntStateOf(todayGreg.monthIndex) }
    var gregYear by remember(currentTime) { mutableIntStateOf(todayGreg.year) }

    var banglaMonthIndex by remember(currentTime) { mutableIntStateOf(todayBengali.monthIndex) }
    var banglaYear by remember(currentTime) { mutableIntStateOf(todayBengali.year) }

    var hijriMonthIndex by remember(currentTime) { mutableIntStateOf(todayHijri.monthIndex) }
    var hijriYear by remember(currentTime) { mutableIntStateOf(todayHijri.year) }

    val hourFormat = remember { SimpleDateFormat("hh", Locale.ENGLISH) }
    val minFormat = remember { SimpleDateFormat("mm", Locale.ENGLISH) }
    val secFormat = remember { SimpleDateFormat("ss", Locale.ENGLISH) }
    val amPmFormat = remember { SimpleDateFormat("a", Locale.ENGLISH) }

    val hours = hourFormat.format(currentTime)
    val minutes = minFormat.format(currentTime)
    val seconds = secFormat.format(currentTime)
    val amPm = amPmFormat.format(currentTime)

    // Warm lite eye-soothing background matching screenshot (warm cream/ivory, never dark/black even in dark theme)
    val liteEyeSoothingBg = Color(0xFFFFFDF7)
    val soothingBorder = Color(0xFFF1E6D3)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .animateContentSize()
            .testTag("date_time_master_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = liteEyeSoothingBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, soothingBorder)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. BIG DIGITAL CLOCK DISPLAY (Replicating screenshot)
            Row(
                modifier = Modifier.padding(top = 6.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Hours
                Text(
                    text = hours,
                    fontSize = 52.sp,
                    fontWeight = MaterialTheme.typography.displayLarge.fontWeight ?: FontWeight.SemiBold,
                    fontFamily = LocalAppFontFamily.current,
                    color = Color(0xFF111827),
                    letterSpacing = (-1).sp
                )

                // Colon Separator
                ClockColonSeparator()

                // Minutes
                Text(
                    text = minutes,
                    fontSize = 52.sp,
                    fontWeight = MaterialTheme.typography.displayLarge.fontWeight ?: FontWeight.SemiBold,
                    fontFamily = LocalAppFontFamily.current,
                    color = Color(0xFF111827),
                    letterSpacing = (-1).sp
                )

                // Colon Separator
                ClockColonSeparator()

                // Seconds and AM/PM Stack
                Column(
                    modifier = Modifier.padding(start = 2.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = seconds,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B5563)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = amPm,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            // 2. MIDDLE BANGLA DAY & DATE PILL (Replicating screenshot)
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE8F4ED),
                border = BorderStroke(1.dp, Color(0xFFCBE5D6)),
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Star",
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "আজ ${calendarInfo.englishDay.replace(" (জুমাবার)", "")}, ${calendarInfo.bengaliMonth} ${calendarInfo.bengaliDateFormatted.substringBefore("(").trim()}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF166534)
                    )
                }
            }

            HorizontalDivider(
                color = soothingBorder.copy(alpha = 0.8f),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // 3. CALENDAR SECTION: 
            // Phase 1 (Collapsed): ONE single card showing current / today's date
            // Phase 2 (Split): Tapping expands / splits into 3 cards of three different calendars
            // Phase 3 (Detailed): Tapping each of the 3 cards expands into full monthly detailed calendar
            AnimatedVisibility(
                visible = !isSplitIntoThree,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                SingleTodayCalendarCard(
                    calendarInfo = calendarInfo,
                    gregorianDateBn = dynamicGregorianDateBn,
                    englishDay = calendarInfo.englishDay,
                    onClick = { isSplitIntoThree = true }
                )
            }

            AnimatedVisibility(
                visible = isSplitIntoThree,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("split_three_calendars_container")
                ) {
                    // Header with collapse back to single card button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "ত্রিমুখী বর্ষপঞ্জি (৩টি ক্যালেন্ডার)",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "যে কোনো কার্ডে ট্যাপ করে বিস্তারিত ক্যালেন্ডার দেখুন",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            onClick = {
                                isSplitIntoThree = false
                                expandedCalendar = CalendarViewType.NONE
                            },
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                            modifier = Modifier.testTag("collapse_to_single_calendar_button")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowUp,
                                    contentDescription = "Collapse",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "একক কার্ড",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // The 3 Cards of Three Different Calendars
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // A. GREGORIAN DATE CARD (VERTICAL)
                        DateInteractiveVerticalCard(
                            title = "ইংরেজি ক্যালেন্ডার",
                            badgeText = "GREGORIAN • খ্রিস্টাব্দ",
                            mainDate = dynamicGregorianDateBn,
                            subtitle = calendarInfo.englishDateFormatted,
                            icon = Icons.Default.CalendarMonth,
                            isSelected = expandedCalendar == CalendarViewType.GREGORIAN,
                            accentColor = Color(0xFF0284C7),
                            selectedGradient = Brush.horizontalGradient(
                                listOf(Color(0xFFE0F2FE), Color(0xFFF0F9FF), Color(0xFFE0F2FE))
                            ),
                            lightBgColor = Color(0xFFF0F9FF),
                            lightBorderColor = Color(0xFFBAE6FD),
                            infoItems = listOf(
                                "বার" to calendarInfo.englishDay.substringBefore(" "),
                                "মাস" to "$engMonthBn (${cal.getActualMaximum(Calendar.DAY_OF_MONTH)} দিন)",
                                "সাল" to "$banglaYearStr খ্রিস্টাব্দ"
                            ),
                            onClick = {
                                expandedCalendar = if (expandedCalendar == CalendarViewType.GREGORIAN) {
                                    CalendarViewType.NONE
                                } else {
                                    CalendarViewType.GREGORIAN
                                }
                            },
                            expandedContent = {
                                ExpandedGregorianCalendarView(
                                    monthIndex = gregMonthIndex,
                                    year = gregYear,
                                    todayCal = cal,
                                    onSelectMonth = { gregMonthIndex = it },
                                    onPrevMonth = {
                                        if (gregMonthIndex == 0) {
                                            gregMonthIndex = 11
                                            gregYear--
                                        } else {
                                            gregMonthIndex--
                                        }
                                    },
                                    onNextMonth = {
                                        if (gregMonthIndex == 11) {
                                            gregMonthIndex = 0
                                            gregYear++
                                        } else {
                                            gregMonthIndex++
                                        }
                                    },
                                    onGoToToday = {
                                        gregMonthIndex = todayGreg.monthIndex
                                        gregYear = todayGreg.year
                                    }
                                )
                            }
                        )

                        // B. BENGALI SAN CARD (VERTICAL)
                        DateInteractiveVerticalCard(
                            title = "বাংলা বর্ষপঞ্জি",
                            badgeText = "BENGALI SAN • বঙ্গাব্দ সন",
                            mainDate = calendarInfo.bengaliDateFormatted.substringBefore("(").trim(),
                            subtitle = "ঋতু: ${calendarInfo.bengaliSeason} • বাংলা সন ${todayBengali.year}",
                            icon = Icons.Default.AutoAwesome,
                            isSelected = expandedCalendar == CalendarViewType.BENGALI,
                            accentColor = Color(0xFFD97706),
                            selectedGradient = Brush.horizontalGradient(
                                listOf(Color(0xFFFEF3C7), Color(0xFFFFFBEB), Color(0xFFFEF3C7))
                            ),
                            lightBgColor = Color(0xFFFEFCE8),
                            lightBorderColor = Color(0xFFFDE047),
                            infoItems = listOf(
                                "ঋতু" to calendarInfo.bengaliSeason,
                                "মাস" to "${calendarInfo.bengaliMonth} মাস",
                                "সন" to "${CalendarHelper.toBanglaNumber(todayBengali.year)} বঙ্গাব্দ"
                            ),
                            onClick = {
                                expandedCalendar = if (expandedCalendar == CalendarViewType.BENGALI) {
                                    CalendarViewType.NONE
                                } else {
                                    CalendarViewType.BENGALI
                                }
                            },
                            expandedContent = {
                                ExpandedBengaliCalendarView(
                                    monthIndex = banglaMonthIndex,
                                    year = banglaYear,
                                    todayCal = cal,
                                    onSelectMonth = { banglaMonthIndex = it },
                                    onPrevMonth = {
                                        if (banglaMonthIndex == 0) {
                                            banglaMonthIndex = 11
                                            banglaYear--
                                        } else {
                                            banglaMonthIndex--
                                        }
                                    },
                                    onNextMonth = {
                                        if (banglaMonthIndex == 11) {
                                            banglaMonthIndex = 0
                                            banglaYear++
                                        } else {
                                            banglaMonthIndex++
                                        }
                                    },
                                    onGoToToday = {
                                        banglaMonthIndex = todayBengali.monthIndex
                                        banglaYear = todayBengali.year
                                    }
                                )
                            }
                        )

                        // C. HIJRI ISLAMIC CARD (VERTICAL)
                        DateInteractiveVerticalCard(
                            title = "হিজরি ইসলামিক সন",
                            badgeText = "HIJRI ISLAMIC • চন্দ্রমাস",
                            mainDate = calendarInfo.hijriDateFormatted,
                            subtitle = "উম্মুল কুরা ভিত্তিক চন্দ্রমাস • হিজরি ${todayHijri.year}",
                            icon = Icons.Default.Explore,
                            isSelected = expandedCalendar == CalendarViewType.HIJRI,
                            accentColor = Color(0xFF059669),
                            selectedGradient = Brush.horizontalGradient(
                                listOf(Color(0xFFDCFCE7), Color(0xFFF0FDF4), Color(0xFFDCFCE7))
                            ),
                            lightBgColor = Color(0xFFF0FDF4),
                            lightBorderColor = Color(0xFFBBF7D0),
                            infoItems = listOf(
                                "বার" to calendarInfo.englishDay.substringBefore(" "),
                                "মাস" to "${calendarInfo.hijriMonth} মাস",
                                "সন" to "${CalendarHelper.toBanglaNumber(todayHijri.year)} হিজরি"
                            ),
                            onClick = {
                                expandedCalendar = if (expandedCalendar == CalendarViewType.HIJRI) {
                                    CalendarViewType.NONE
                                } else {
                                    CalendarViewType.HIJRI
                                }
                            },
                            expandedContent = {
                                ExpandedHijriCalendarView(
                                    monthIndex = hijriMonthIndex,
                                    year = hijriYear,
                                    todayCal = cal,
                                    onSelectMonth = { hijriMonthIndex = it },
                                    onPrevMonth = {
                                        if (hijriMonthIndex == 0) {
                                            hijriMonthIndex = 11
                                            hijriYear--
                                        } else {
                                            hijriMonthIndex--
                                        }
                                    },
                                    onNextMonth = {
                                        if (hijriMonthIndex == 11) {
                                            hijriMonthIndex = 0
                                            hijriYear++
                                        } else {
                                            hijriMonthIndex++
                                        }
                                    },
                                    onGoToToday = {
                                        hijriMonthIndex = todayHijri.monthIndex
                                        hijriYear = todayHijri.year
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// SINGLE UNIFIED CARD FOR TODAY'S DATE (Collapsed state)
// -------------------------------------------------------------
@Composable
private fun SingleTodayCalendarCard(
    calendarInfo: CalendarHelper.TripleCalendarInfo,
    gregorianDateBn: String,
    englishDay: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Single unified warm lite eye-soothing background (warm cream/ivory, never dark/black even in dark theme)
    val warmCardBg = Color(0xFFFFFDF8)
    val warmBorderColor = Color(0xFFF1E5D2)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = warmCardBg,
        border = BorderStroke(1.2.dp, warmBorderColor),
        shadowElevation = 0.5.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("single_today_calendar_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header Row: Badge & "Tap to expand" pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF10B981).copy(alpha = 0.16f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "আজকের তারিখ (Today's Date)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111827)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF059669).copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = "৩টি বর্ষপঞ্জি সংযুক্ত • ৩-ইন-১",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main Primary Date
            Text(
                text = "$gregorianDateBn, $englishDay",
                fontSize = 18.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111827),
                letterSpacing = (-0.3).sp
            )
            Text(
                text = calendarInfo.englishDateFormatted,
                fontSize = 12.5.sp,
                color = Color(0xFF4B5563),
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3 Calendar Horizontal Quick Cards (always clean white cards on lite warm background)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val cardBg = Color.White

                // 1. Gregorian pill
                TodayCalendarMiniBadge(
                    calendarName = "খ্রিস্টাব্দ",
                    dateText = gregorianDateBn.replace(" খ্রিস্টাব্দ", ""),
                    tagText = "ইংরেজি",
                    badgeColor = Color(0xFF0284C7),
                    bgColor = cardBg,
                    borderColor = Color(0xFFBAE6FD).copy(alpha = 0.85f),
                    modifier = Modifier.weight(1f)
                )

                // 2. Bengali pill
                TodayCalendarMiniBadge(
                    calendarName = "বঙ্গাব্দ সন",
                    dateText = calendarInfo.bengaliDateFormatted.substringBefore("(").trim().replace(" বঙ্গাব্দ", ""),
                    tagText = calendarInfo.bengaliSeason,
                    badgeColor = Color(0xFFD97706),
                    bgColor = cardBg,
                    borderColor = Color(0xFFFDE047).copy(alpha = 0.85f),
                    modifier = Modifier.weight(1f)
                )

                // 3. Hijri pill
                TodayCalendarMiniBadge(
                    calendarName = "হিজরি সন",
                    dateText = calendarInfo.hijriDateFormatted.replace(" হিজরি", "").trim(),
                    tagText = "চন্দ্রমাস",
                    badgeColor = Color(0xFF059669),
                    bgColor = cardBg,
                    borderColor = Color(0xFFBBF7D0).copy(alpha = 0.85f),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Tap Prompt Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFFE5F3EC),
                border = BorderStroke(1.dp, Color(0xFFC8E3D3)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = Color(0xFF059669),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ট্যাপ করে ৩টি ক্যালেন্ডার আলাদাভাবে দেখুন ও বিস্তারিত খুলুন",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF047857)
                    )
                }
            }
        }
    }
}

@Composable
private fun TodayCalendarMiniBadge(
    calendarName: String,
    dateText: String,
    tagText: String,
    badgeColor: Color,
    bgColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = calendarName,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = badgeColor
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = dateText,
                fontSize = 11.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = badgeColor.copy(alpha = 0.12f)
            ) {
                Text(
                    text = tagText,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = badgeColor,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// -------------------------------------------------------------
// CLOCK COLON SEPARATOR WITH TWO SQUARES
// -------------------------------------------------------------
@Composable
private fun ClockColonSeparator() {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFFCBD5E1), RoundedCornerShape(2.dp))
        )
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(Color(0xFFCBD5E1), RoundedCornerShape(2.dp))
        )
    }
}

// -------------------------------------------------------------
// INTERACTIVE VERTICAL DATE CARD (Gregorian, Bengali, Hijri)
// -------------------------------------------------------------
@Composable
private fun DateInteractiveVerticalCard(
    title: String,
    badgeText: String,
    mainDate: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    accentColor: Color,
    selectedGradient: Brush,
    lightBgColor: Color,
    lightBorderColor: Color,
    infoItems: List<Pair<String, String>>,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expandedContent: (@Composable () -> Unit)? = null
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color.Transparent else lightBgColor,
        border = BorderStroke(
            1.2.dp,
            if (isSelected) accentColor.copy(alpha = 0.5f) else lightBorderColor
        ),
        shadowElevation = if (isSelected) 4.dp else 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .then(
                if (isSelected) {
                    Modifier.background(selectedGradient, RoundedCornerShape(20.dp))
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Icon + Title + Badge, and Trailing Toggle Pill
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
                        shape = CircleShape,
                        color = accentColor.copy(alpha = 0.14f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = accentColor,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = title,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = accentColor.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Trailing Action Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = accentColor.copy(alpha = 0.14f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSelected) "ক্যালেন্ডার বন্ধ" else "মাসিক ভিউ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = if (isSelected) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isSelected) "Collapse" else "Expand",
                            tint = accentColor,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Date display
            Text(
                text = mainDate,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                letterSpacing = (-0.3).sp
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color(0xFF475569),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Contextual Strip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.95f),
                border = BorderStroke(
                    1.dp,
                    lightBorderColor.copy(alpha = 0.85f)
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    infoItems.forEachIndexed { idx, item ->
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = item.first,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = item.second,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        if (idx < infoItems.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(18.dp)
                                    .background(Color(0xFFE2E8F0))
                            )
                        }
                    }
                }
            }

            // Expanded Monthly Calendar view directly inside this card!
            AnimatedVisibility(
                visible = isSelected && expandedContent != null,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(top = 12.dp)) {
                    Surface(
                        color = Color(0xFFFFFDF8),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color(0xFFF1E6D3)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.padding(12.dp)) {
                            expandedContent?.invoke()
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// EXPANDED GREGORIAN CALENDAR VIEW
// -------------------------------------------------------------
@Composable
private fun ExpandedGregorianCalendarView(
    monthIndex: Int,
    year: Int,
    todayCal: Calendar = Calendar.getInstance(),
    onSelectMonth: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit
) {
    val monthData = remember(monthIndex, year, todayCal) {
        CalendarMonthProvider.getGregorianMonth(monthIndex, year, todayCal)
    }
    val todayDetail = remember(todayCal) { CalendarHelper.getGregorianDateDetail(todayCal) }
    val isViewingCurrentMonth = (monthIndex == todayDetail.monthIndex && year == todayDetail.year)

    val englishFont = LocalEnglishFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header & Season Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation: < Month Year > + "আজ" jump button
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Month",
                        modifier = Modifier.size(13.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = monthData.monthName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = englishFont
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEEF2FF)
                        ) {
                            Text(
                                text = "${monthData.year}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5),
                                fontFamily = englishFont,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Quick "আজ" button if navigated away from today's month
                        if (!isViewingCurrentMonth) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                onClick = onGoToToday,
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF4F46E5).copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, Color(0xFF4F46E5).copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Today,
                                        contentDescription = "আজকের দিনে যান",
                                        tint = Color(0xFF4F46E5),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "আজ",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF4F46E5),
                                        fontFamily = banglaFont,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = monthData.monthNumberLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = englishFont
                    )
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
                        contentDescription = "Next Month",
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Right side: Autumn (September) Season Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFEA580C),
                modifier = Modifier
                    .widthIn(max = 160.dp)
                    .padding(start = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Season",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(
                            text = monthData.seasonTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = englishFont,
                            maxLines = 1
                        )
                        Text(
                            text = monthData.seasonDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontFamily = englishFont,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Month Selector Chips (Jan..Dec)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalendarMonthProvider.gregorianMonthShortNames.forEachIndexed { index, code ->
                val isSelected = monthIndex == index
                Surface(
                    onClick = { onSelectMonth(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFF4F46E5) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF4F46E5) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = code,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = englishFont,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday Headers (Sun..Sat)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalendarMonthProvider.gregorianWeekdays.forEachIndexed { colIdx, day ->
                val headerBg = when (colIdx) {
                    0 -> Color(0xFFFFF1F2) // Sunday soft peach
                    5 -> Color(0xFFECFDF5) // Friday soft mint
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                }
                val headerColor = when (colIdx) {
                    0 -> Color(0xFFBE123C) // Sunday rose
                    5 -> Color(0xFF047857) // Friday emerald
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = headerBg,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = headerColor,
                        fontFamily = englishFont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Calendar Days 7-column Grid
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
                            GregorianDayCell(item = item, modifier = Modifier.weight(1f))
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GregorianDayCell(
    item: GregorianDayItem,
    modifier: Modifier = Modifier
) {
    val isToday = item.isToday
    val englishFont = LocalEnglishFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current

    val regularBg = when {
        item.colIndex == 0 -> Color(0xFFFFF1F2) // Sunday soft peach
        item.colIndex in 1..4 -> Color(0xFFFFFDF8) // Mon-Thu warm cream
        item.colIndex == 5 -> Color(0xFFECFDF5) // Friday soft mint
        else -> Color(0xFFEFF6FF) // Saturday soft blue
    }

    Surface(
        modifier = modifier.aspectRatio(0.92f),
        shape = RoundedCornerShape(12.dp),
        color = if (isToday) Color.Transparent else regularBg,
        border = BorderStroke(
            if (isToday) 1.5.dp else 1.dp,
            if (isToday) Color(0xFF818CF8) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        ),
        shadowElevation = if (isToday) 2.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isToday) {
                        Modifier.background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF4338CA), Color(0xFF6366F1))
                            )
                        )
                    } else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.padding(vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${item.dayNumber}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                    color = when {
                        isToday -> Color.White
                        item.colIndex == 0 -> Color(0xFFBE123C)
                        item.colIndex == 5 -> Color(0xFF047857)
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontFamily = englishFont
                )
                if (isToday) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color.White.copy(alpha = 0.25f),
                        modifier = Modifier.padding(top = 1.dp)
                    ) {
                        Text(
                            text = "আজ",
                            fontSize = 7.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 0.5.dp)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// EXPANDED BENGALI CALENDAR VIEW
// -------------------------------------------------------------
@Composable
private fun ExpandedBengaliCalendarView(
    monthIndex: Int,
    year: Int,
    todayCal: Calendar = Calendar.getInstance(),
    onSelectMonth: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onGoToToday: () -> Unit
) {
    val monthData = remember(monthIndex, year, todayCal) {
        CalendarMonthProvider.getBengaliMonth(monthIndex, year, todayCal)
    }
    val todayDetail = remember(todayCal) { CalendarHelper.getBengaliDateDetail(todayCal) }
    val isViewingCurrentMonth = (monthIndex == todayDetail.monthIndex && year == todayDetail.year)

    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header & Season Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation: < ভাদ্র (১৪৩৩ বঙ্গাব্দ) > + "আজ" jump button
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Month",
                        modifier = Modifier.size(13.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = monthData.monthNameBn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Text(
                                text = "(${monthData.yearBn} বঙ্গাব্দ)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFB45309),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Quick "আজ" button if navigated away from today's month
                        if (!isViewingCurrentMonth) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                onClick = onGoToToday,
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFD97706).copy(alpha = 0.12f),
                                border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.35f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Today,
                                        contentDescription = "আজকের দিনে যান",
                                        tint = Color(0xFFD97706),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "আজ",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706),
                                        fontFamily = banglaFont,
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        }
                    }
                    Text(
                        text = monthData.monthNumberLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = banglaFont
                    )
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
                        contentDescription = "Next Month",
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Right side: শরৎকাল Season Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFD97706),
                modifier = Modifier
                    .widthIn(max = 160.dp)
                    .padding(start = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Season",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(
                            text = monthData.seasonTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = banglaFont,
                            maxLines = 1
                        )
                        Text(
                            text = monthData.seasonDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontFamily = banglaFont,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Bengali Month Selector Chips (বৈশাখ..চৈত্র)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalendarMonthProvider.bengaliMonthNames.forEachIndexed { index, name ->
                val isSelected = monthIndex == index
                Surface(
                    onClick = { onSelectMonth(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFFD97706) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFFD97706) else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                    ),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday Headers (শনি..শুক্র)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalendarMonthProvider.bengaliWeekdays.forEachIndexed { colIdx, day ->
                val headerBg = when (colIdx) {
                    1 -> Color(0xFFFFF1F2) // Robi soft peach
                    6 -> Color(0xFFECFDF5) // Shukro soft mint
                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                }
                val headerColor = when (colIdx) {
                    1 -> Color(0xFFBE123C) // Robi rose
                    6 -> Color(0xFF047857) // Shukro emerald
                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = headerBg,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = headerColor,
                        fontFamily = banglaFont,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Calendar Days 7-column Grid
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
                            BengaliDayCell(item = item, modifier = Modifier.weight(1f))
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BengaliDayCell(
    item: BengaliDayItem,
    modifier: Modifier = Modifier
) {
    val isToday = item.isToday
    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current

    val regularBg = when {
        item.colIndex == 1 -> Color(0xFFFFF1F2) // Robibar soft peach
        item.colIndex in 2..5 -> Color(0xFFFFFDF8) // Som-Briho warm cream
        item.colIndex == 6 -> Color(0xFFECFDF5) // Shukrobar soft mint
        else -> Color(0xFFEFF6FF) // Shonibar soft blue
    }

    Surface(
        modifier = modifier.aspectRatio(0.92f),
        shape = RoundedCornerShape(12.dp),
        color = if (isToday) Color.Transparent else regularBg,
        border = BorderStroke(
            if (isToday) 1.5.dp else 1.dp,
            if (isToday) Color(0xFFFCD34D) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        ),
        shadowElevation = if (isToday) 2.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .then(
                    if (isToday) {
                        Modifier.background(
                            Brush.verticalGradient(
                                listOf(Color(0xFFB45309), Color(0xFFF59E0B))
                            )
                        )
                    } else Modifier
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.dayNumberBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                    color = when {
                        isToday -> Color.White
                        item.colIndex == 1 -> Color(0xFFBE123C)
                        item.colIndex == 6 -> Color(0xFF047857)
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontFamily = banglaFont
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isToday) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.3f)
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
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Text(
                        text = "${item.gregorianDayNumber}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isToday) Color.White.copy(alpha = 0.95f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = englishFont
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// EXPANDED HIJRI CALENDAR VIEW
// -------------------------------------------------------------
@Composable
private fun ExpandedHijriCalendarView(
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

    val arabicFont = LocalArabicFontFamily.current
    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header & Event Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation: < Rabi' al-Awwal (ربيع الأول) (১৪৪৮ AH) > + "আজ" jump button
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Month",
                        modifier = Modifier.size(13.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${monthData.monthNameEn} (${monthData.monthNameAr})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            fontFamily = arabicFont,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFDCFCE7)
                        ) {
                            Text(
                                text = "(${monthData.yearBn} AH)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF15803D),
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        // Quick "আজ" button if navigated away from today's month
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
                    Text(
                        text = monthData.monthNumberLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = englishFont
                    )
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
                        contentDescription = "Next Month",
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            // Right side: Islamic Significance Event Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF059669),
                modifier = Modifier
                    .widthIn(max = 160.dp)
                    .padding(start = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Event",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Column {
                        Text(
                            text = monthData.eventTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = arabicFont,
                            maxLines = 1
                        )
                        Text(
                            text = monthData.eventDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            fontFamily = englishFont,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hijri Month Selector Chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            CalendarMonthProvider.hijriMonthNames.forEachIndexed { index, name ->
                val isSelected = monthIndex == index
                val arabicMonthName = CalendarMonthProvider.hijriMonthNamesArabic.getOrElse(index) { "" }
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
                            text = name,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = englishFont
                        )
                        if (arabicMonthName.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = arabicMonthName,
                                fontSize = 10.sp,
                                color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontFamily = arabicFont
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Weekday Headers (Sun..Sat with Arabic subtitle)
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

        // Calendar Days 7-column Grid
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
                            HijriDayCell(item = item, modifier = Modifier.weight(1f))
                        } else {
                            Box(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HijriDayCell(
    item: HijriDayItem,
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

    Surface(
        modifier = modifier.aspectRatio(0.92f),
        shape = RoundedCornerShape(12.dp),
        color = if (isToday) Color.Transparent else regularBg,
        border = BorderStroke(
            if (isToday) 1.5.dp else 1.dp,
            if (isToday) Color(0xFF6EE7B7) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        ),
        shadowElevation = if (isToday) 2.dp else 0.dp
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
                    } else Modifier
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp, vertical = 3.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Arabic numeral & Bangla numeral
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.hijriDayArabic,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                        color = if (isToday) Color.White else Color(0xFF047857),
                        fontFamily = arabicFont
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "(${item.hijriDayBn})",
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isToday) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                        fontFamily = banglaFont
                    )
                }

                // Bottom row: "আজ" badge and Gregorian sub date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isToday) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color.White.copy(alpha = 0.3f)
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
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Text(
                        text = item.gregorianSubDate,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isToday) Color.White.copy(alpha = 0.95f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontFamily = englishFont
                    )
                }
            }
        }
    }
}
