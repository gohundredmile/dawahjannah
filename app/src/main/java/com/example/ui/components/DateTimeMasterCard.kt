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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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

    // Navigation states for expanded views
    var gregMonthIndex by remember { mutableIntStateOf(cal.get(Calendar.MONTH)) }
    var gregYear by remember { mutableIntStateOf(cal.get(Calendar.YEAR)) }

    var banglaMonthIndex by remember { mutableIntStateOf(4) } // Bhadra (4)
    var banglaYear by remember { mutableIntStateOf(1433) }

    var hijriMonthIndex by remember { mutableIntStateOf(2) } // Rabi' al-Awwal (2)
    var hijriYear by remember { mutableIntStateOf(1448) }

    val hourFormat = remember { SimpleDateFormat("hh", Locale.ENGLISH) }
    val minFormat = remember { SimpleDateFormat("mm", Locale.ENGLISH) }
    val secFormat = remember { SimpleDateFormat("ss", Locale.ENGLISH) }
    val amPmFormat = remember { SimpleDateFormat("a", Locale.ENGLISH) }

    val hours = hourFormat.format(currentTime)
    val minutes = minFormat.format(currentTime)
    val seconds = secFormat.format(currentTime)
    val amPm = amPmFormat.format(currentTime)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .animateContentSize()
            .testTag("date_time_master_card"),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. BIG DIGITAL CLOCK DISPLAY (Replicating screenshot)
            Row(
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Hours
                Text(
                    text = hours,
                    fontSize = 52.sp,
                    fontWeight = MaterialTheme.typography.displayLarge.fontWeight ?: FontWeight.SemiBold,
                    fontFamily = LocalAppFontFamily.current,
                    color = MaterialTheme.colorScheme.onSurface,
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
                    color = MaterialTheme.colorScheme.onSurface,
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
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
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
                color = Color(0xFFF3F0FF),
                border = BorderStroke(1.dp, Color(0xFFE0E7FF)),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 7.dp),
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
                        color = Color(0xFF312E81)
                    )
                }
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                modifier = Modifier.padding(bottom = 14.dp)
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
                                listOf(Color(0xFF0F172A), Color(0xFF1E293B), Color(0xFF1E3A8A))
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
                                    }
                                )
                            }
                        )

                        // B. BENGALI SAN CARD (VERTICAL)
                        DateInteractiveVerticalCard(
                            title = "বাংলা বর্ষপঞ্জি",
                            badgeText = "BENGALI SAN • বঙ্গাব্দ সন",
                            mainDate = calendarInfo.bengaliDateFormatted.substringBefore("(").trim(),
                            subtitle = "ঋতু: ${calendarInfo.bengaliSeason} • বাংলা সন ১৪৩৩",
                            icon = Icons.Default.AutoAwesome,
                            isSelected = expandedCalendar == CalendarViewType.BENGALI,
                            accentColor = Color(0xFFD97706),
                            selectedGradient = Brush.horizontalGradient(
                                listOf(Color(0xFF1E1B4B), Color(0xFF312E81), Color(0xFF4338CA))
                            ),
                            lightBgColor = Color(0xFFFEFCE8),
                            lightBorderColor = Color(0xFFFDE047),
                            infoItems = listOf(
                                "ঋতু" to calendarInfo.bengaliSeason,
                                "মাস" to "${calendarInfo.bengaliMonth} মাস",
                                "সন" to "১৪৩৩ বঙ্গাব্দ"
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
                                    }
                                )
                            }
                        )

                        // C. HIJRI ISLAMIC CARD (VERTICAL)
                        DateInteractiveVerticalCard(
                            title = "হিজরি ইসলামিক সন",
                            badgeText = "HIJRI ISLAMIC • চন্দ্রমাস",
                            mainDate = calendarInfo.hijriDateFormatted,
                            subtitle = "উম্মুল কুরা ভিত্তিক চন্দ্রমাস • হিজরি ১৪৪৮",
                            icon = Icons.Default.Explore,
                            isSelected = expandedCalendar == CalendarViewType.HIJRI,
                            accentColor = Color(0xFF059669),
                            selectedGradient = Brush.horizontalGradient(
                                listOf(Color(0xFF064E3B), Color(0xFF022C22), Color(0xFF065F46))
                            ),
                            lightBgColor = Color(0xFFF0FDF4),
                            lightBorderColor = Color(0xFFBBF7D0),
                            infoItems = listOf(
                                "বার" to calendarInfo.englishDay.substringBefore(" "),
                                "মাস" to "${calendarInfo.hijriMonth} মাস",
                                "সন" to "১৪৪৮ হিজরি"
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
                                    onSelectMonth = { hijriMonthIndex = it },
                                    onPrevMonth = {
                                        if (hijriMonthIndex == 0) {
                                            hijriMonthIndex = 11
                                            hijriYear--
                                        } else {
                                            hijriMonthIndex++
                                        }
                                    },
                                    onNextMonth = {
                                        if (hijriMonthIndex == 11) {
                                            hijriMonthIndex = 0
                                            hijriYear++
                                        } else {
                                            hijriMonthIndex++
                                        }
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
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.38f),
        border = BorderStroke(
            1.2.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.28f)
        ),
        shadowElevation = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("single_today_calendar_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
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
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = "Calendar",
                                tint = MaterialTheme.colorScheme.primary,
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
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "৩টি বর্ষপঞ্জি সংযুক্ত • ৩-ইন-১",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Primary Date
            Text(
                text = "$gregorianDateBn, $englishDay",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                letterSpacing = (-0.3).sp
            )
            Text(
                text = calendarInfo.englishDateFormatted,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 3 Calendar Horizontal Quick Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // 1. Gregorian pill
                TodayCalendarMiniBadge(
                    calendarName = "খ্রিস্টাব্দ",
                    dateText = gregorianDateBn.replace(" খ্রিস্টাব্দ", ""),
                    tagText = "ইংরেজি",
                    badgeColor = Color(0xFF0284C7),
                    bgColor = Color(0xFFF0F9FF),
                    borderColor = Color(0xFFBAE6FD),
                    modifier = Modifier.weight(1f)
                )

                // 2. Bengali pill
                TodayCalendarMiniBadge(
                    calendarName = "বঙ্গাব্দ সন",
                    dateText = calendarInfo.bengaliDateFormatted.substringBefore("(").trim().replace(" বঙ্গাব্দ", ""),
                    tagText = calendarInfo.bengaliSeason,
                    badgeColor = Color(0xFFD97706),
                    bgColor = Color(0xFFFEFCE8),
                    borderColor = Color(0xFFFDE047),
                    modifier = Modifier.weight(1f)
                )

                // 3. Hijri pill
                TodayCalendarMiniBadge(
                    calendarName = "হিজরি সন",
                    dateText = calendarInfo.hijriDateFormatted.replace(" হিজরি", "").trim(),
                    tagText = "চন্দ্রমাস",
                    badgeColor = Color(0xFF059669),
                    bgColor = Color(0xFFF0FDF4),
                    borderColor = Color(0xFFBBF7D0),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Tap Prompt Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.07f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.16f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ট্যাপ করে ৩টি ক্যালেন্ডার আলাদাভাবে দেখুন ও বিস্তারিত খুলুন",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
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
                        color = if (isSelected) Color.White.copy(alpha = 0.2f) else accentColor.copy(alpha = 0.14f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (isSelected) Color.White else accentColor,
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
                            color = if (isSelected) Color.White else Color(0xFF0F172A)
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (isSelected) Color.White.copy(alpha = 0.2f) else accentColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White.copy(alpha = 0.95f) else accentColor,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                // Trailing Action Pill
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color.White.copy(alpha = 0.25f) else accentColor.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSelected) "ক্যালেন্ডার বন্ধ" else "মাসিক ভিউ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else accentColor
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = if (isSelected) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (isSelected) "Collapse" else "Expand",
                            tint = if (isSelected) Color.White else accentColor,
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
                color = if (isSelected) Color.White else Color(0xFF0F172A),
                letterSpacing = (-0.3).sp
            )

            if (subtitle.isNotEmpty()) {
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = if (isSelected) Color.White.copy(alpha = 0.82f) else Color(0xFF475569),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom Contextual Strip
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Color.Black.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.9f),
                border = BorderStroke(
                    1.dp,
                    if (isSelected) Color.White.copy(alpha = 0.15f) else lightBorderColor.copy(alpha = 0.8f)
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
                                color = if (isSelected) Color.White.copy(alpha = 0.7f) else Color(0xFF64748B)
                            )
                            Text(
                                text = item.second,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color(0xFF0F172A)
                            )
                        }

                        if (idx < infoItems.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(18.dp)
                                    .background(
                                        if (isSelected) Color.White.copy(alpha = 0.2f)
                                        else Color(0xFFE2E8F0)
                                    )
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
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)),
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
    onSelectMonth: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthData = remember(monthIndex, year) {
        CalendarMonthProvider.getGregorianMonth(monthIndex, year, todayDay = 4)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header & Season Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation: < September 2026 >
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Month",
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = monthData.monthName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
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
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = monthData.monthNumberLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onNextMonth,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Month",
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Right side: Autumn (September) Season Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFEA580C),
                modifier = Modifier
                    .widthIn(max = 170.dp)
                    .padding(start = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Season",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = monthData.seasonTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = monthData.seasonDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.White.copy(alpha = 0.9f),
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
                    color = if (isSelected) Color(0xFF4F46E5) else Color.Transparent,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = code,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
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
            CalendarMonthProvider.gregorianWeekdays.forEach { day ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Calendar Days 7-column Grid
        val rows = monthData.days.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
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

    // Subtle column pastel tint matching screenshots
    val bgColor = when {
        isToday -> Color(0xFF4F46E5)
        item.colIndex == 0 -> Color(0xFFFFF1F2) // Sunday soft peach
        item.colIndex in 1..2 -> Color(0xFFFFFBEB) // Mon-Tue cream
        item.colIndex in 3..4 -> Color(0xFFF0FDFA) // Wed-Thu mint
        else -> Color(0xFFEFF6FF) // Fri-Sat soft blue
    }

    Surface(
        modifier = modifier.aspectRatio(0.95f),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(
            1.dp,
            if (isToday) Color(0xFF4F46E5) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${item.dayNumber}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                color = if (isToday) Color.White else MaterialTheme.colorScheme.onSurface
            )
            if (isToday) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
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
    onSelectMonth: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthData = remember(monthIndex, year) {
        CalendarMonthProvider.getBengaliMonth(monthIndex, year, todayDay = 20)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header & Season Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation: < ভাদ্র (১৪৩৩ বঙ্গাব্দ) >
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Month",
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = monthData.monthNameBn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEEF2FF)
                        ) {
                            Text(
                                text = "(${monthData.yearBn} বঙ্গাব্দ)",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4F46E5),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = monthData.monthNumberLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onNextMonth,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Month",
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Right side: শরৎকাল (শরৎ) Season Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF0284C7),
                modifier = Modifier
                    .widthIn(max = 170.dp)
                    .padding(start = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Season",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = monthData.seasonTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = monthData.seasonDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.White.copy(alpha = 0.9f),
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
                    color = if (isSelected) Color(0xFF4F46E5) else Color.Transparent,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
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
            CalendarMonthProvider.bengaliWeekdays.forEach { day ->
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = day,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 6.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Calendar Days 7-column Grid
        val rows = monthData.days.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
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

    val bgColor = when {
        isToday -> Color(0xFF4F46E5)
        item.colIndex in 0..1 -> Color(0xFFFFF1F2) // Shoni-Robi soft peach
        item.colIndex in 2..3 -> Color(0xFFFFFBEB) // Som-Mongol cream
        item.colIndex in 4..5 -> Color(0xFFF0FDFA) // Budh-Briho mint
        else -> Color(0xFFEFF6FF) // Shukro soft blue
    }

    Surface(
        modifier = modifier.aspectRatio(0.95f),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(
            1.dp,
            if (isToday) Color(0xFF4F46E5) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = item.dayNumberBn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                color = if (isToday) Color.White else MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isToday) {
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
                Text(
                    text = "${item.gregorianDayNumber}",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isToday) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
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
    onSelectMonth: (Int) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val monthData = remember(monthIndex, year) {
        CalendarMonthProvider.getHijriMonth(monthIndex, year, todayDay = 22)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header & Event Card Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Navigation: < Rabi' al-Awwal (ربيع الأول) (১৪৪৮ AH) >
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onPrevMonth,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Previous Month",
                        modifier = Modifier.size(12.dp)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${monthData.monthNameEn} (${monthData.monthNameAr})",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
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
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = monthData.monthNumberLabel,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = onNextMonth,
                    modifier = Modifier
                        .size(34.dp)
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "Next Month",
                        modifier = Modifier.size(12.dp)
                    )
                }
            }

            // Right side: Islamic Significance Event Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFF059669),
                modifier = Modifier
                    .widthIn(max = 170.dp)
                    .padding(start = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Event",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = monthData.eventTitle,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Text(
                            text = monthData.eventDescription,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            color = Color.White.copy(alpha = 0.9f),
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
                Surface(
                    onClick = { onSelectMonth(index) },
                    shape = RoundedCornerShape(12.dp),
                    color = if (isSelected) Color(0xFF4F46E5) else Color.Transparent,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
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
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = enDay,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = arDay,
                            fontSize = 8.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Calendar Days 7-column Grid
        val rows = monthData.days.chunked(7)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
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

    val bgColor = when {
        isToday -> Color(0xFF4F46E5)
        item.colIndex == 0 -> Color(0xFFFFF1F2) // Sunday soft peach
        item.colIndex in 1..2 -> Color(0xFFFFFBEB) // Mon-Tue cream
        item.colIndex in 3..4 -> Color(0xFFF0FDFA) // Wed-Thu mint
        else -> Color(0xFFEFF6FF) // Fri-Sat soft blue
    }

    Surface(
        modifier = modifier.aspectRatio(0.95f),
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        border = BorderStroke(
            1.dp,
            if (isToday) Color(0xFF4F46E5) else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 2.dp, vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${item.hijriDayEng}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                    color = if (isToday) Color.White else Color(0xFF047857)
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = "(${item.hijriDayBn})",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isToday) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                if (isToday) {
                    Spacer(modifier = Modifier.width(2.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    )
                }
            }

            Text(
                text = item.gregorianSubDate,
                fontSize = 9.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isToday) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f)
            )
        }
    }
}
