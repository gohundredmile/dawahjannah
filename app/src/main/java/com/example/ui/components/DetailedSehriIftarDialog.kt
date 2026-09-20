package com.example.ui.components

import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.SalatConfiguration
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalAppFontFamily
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
import java.util.Calendar

data class DateWiseTimingItem(
    val dayOfMonth: Int,
    val dayOfMonthBn: String,
    val dateBn: String,
    val dayOfWeekBn: String,
    val hijriDateBn: String,
    val isToday: Boolean,
    val sehriEndBn: String,
    val fajrStartBn: String,
    val sunriseBn: String,
    val iftarBn: String,
    val durationBn: String
)

/**
 * সেহেরি ও ইফতারের বিস্তারিত সময়সূচী (Detailed Date-wise Sehri & Iftar Timetable Window)
 * Features:
 * - Month & Date dropdown lists for easy jumping to any day of the year
 * - Exceptional beautiful layout designed with Islamic wallpaper & elements
 * - Hero card for the chosen date showing Sehri end, Iftar, Sunrise, and duration
 * - Full month date-wise timetable table with today highlighted
 * - Quick day-navigation buttons (Previous, Today, Next)
 * - Share functionality
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedSehriIftarDialog(
    salatConfig: SalatConfiguration = SalatConfiguration(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val todayCal = remember { Calendar.getInstance() }
    val todayYear = todayCal.get(Calendar.YEAR)
    val todayMonth = todayCal.get(Calendar.MONTH)
    val todayDay = todayCal.get(Calendar.DAY_OF_MONTH)

    var selectedYear by remember { mutableIntStateOf(todayYear) }
    var selectedMonth by remember { mutableIntStateOf(todayMonth) }
    var selectedDay by remember { mutableIntStateOf(todayDay) }

    var isMonthDropdownOpen by remember { mutableStateOf(false) }
    var isDateDropdownOpen by remember { mutableStateOf(false) }

    val monthsBn = listOf(
        "জানুয়ারি", "ফেব্রুয়ারি", "মার্চ", "এপ্রিল", "মে", "জুন",
        "জুলাই", "আগস্ট", "সেপ্টেম্বর", "অক্টোবর", "নভেম্বর", "ডিসেম্বর"
    )

    val daysOfWeekBn = listOf("রবিবার", "সোমবার", "মঙ্গলবার", "বুধবার", "বৃহস্পতিবার", "শুক্রবার", "শনিবার")

    // Calculate maximum days in selected month
    val tempCal = remember(selectedYear, selectedMonth) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }
    val maxDaysInMonth = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Clamp selectedDay if exceeds maxDaysInMonth
    LaunchedEffect(maxDaysInMonth) {
        if (selectedDay > maxDaysInMonth) {
            selectedDay = maxDaysInMonth
        }
    }

    // Active day calculation
    val activeDayCal = remember(selectedYear, selectedMonth, selectedDay) {
        Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
            set(Calendar.DAY_OF_MONTH, selectedDay)
        }
    }

    val activeDayPrayerStatus = remember(activeDayCal, salatConfig) {
        PrayerCalculator.calculatePrayers(
            cal = activeDayCal,
            isHanafiAsr = salatConfig.isHanafiAsr,
            latitude = salatConfig.latitude,
            longitude = salatConfig.longitude,
            locationNameBn = salatConfig.placeNameBn,
            manualOffsetMinutes = salatConfig.manualOffsetMinutes,
            calculationMethod = salatConfig.calculationMethod,
            asrMethod = salatConfig.asrMethod,
            highLatitudeRule = salatConfig.highLatitudeRule,
            timezoneOffsetHours = salatConfig.timezoneOffsetHours
        )
    }

    val activeDayOfWeek = daysOfWeekBn.getOrElse(activeDayCal.get(Calendar.DAY_OF_WEEK) - 1) { "" }
    val activeDateBn = "${CalendarHelper.toBanglaNumber(selectedDay)} ${monthsBn[selectedMonth]} ${CalendarHelper.toBanglaNumber(selectedYear)}, $activeDayOfWeek"

    // Active day duration
    val activeSehriMins = activeDayPrayerStatus.prayerList.firstOrNull { it.id == "fajr" }?.timeMinutesFromMidnight ?: (4 * 60 + 20)
    val activeIftarMins = activeDayPrayerStatus.prayerList.firstOrNull { it.id == "maghrib" }?.timeMinutesFromMidnight ?: (18 * 60 + 15)
    val activeDiff = (activeIftarMins - activeSehriMins).coerceAtLeast(0)
    val activeDurationBn = "${CalendarHelper.toBanglaNumber(activeDiff / 60)} ঘণ্টা ${CalendarHelper.toBanglaNumber(activeDiff % 60)} মিনিট"

    // Generate month's list of dates
    val monthDaysList = remember(selectedYear, selectedMonth, salatConfig) {
        val list = mutableListOf<DateWiseTimingItem>()
        val iterateCal = Calendar.getInstance().apply {
            set(Calendar.YEAR, selectedYear)
            set(Calendar.MONTH, selectedMonth)
        }
        val count = iterateCal.getActualMaximum(Calendar.DAY_OF_MONTH)
        for (day in 1..count) {
            iterateCal.set(Calendar.DAY_OF_MONTH, day)
            val st = PrayerCalculator.calculatePrayers(
                cal = iterateCal,
                isHanafiAsr = salatConfig.isHanafiAsr,
                latitude = salatConfig.latitude,
                longitude = salatConfig.longitude,
                locationNameBn = salatConfig.placeNameBn,
                manualOffsetMinutes = salatConfig.manualOffsetMinutes,
                calculationMethod = salatConfig.calculationMethod,
                asrMethod = salatConfig.asrMethod,
                highLatitudeRule = salatConfig.highLatitudeRule,
                timezoneOffsetHours = salatConfig.timezoneOffsetHours
            )
            val sMins = st.prayerList.firstOrNull { it.id == "fajr" }?.timeMinutesFromMidnight ?: (4 * 60 + 20)
            val iMins = st.prayerList.firstOrNull { it.id == "maghrib" }?.timeMinutesFromMidnight ?: (18 * 60 + 15)
            val dDiff = (iMins - sMins).coerceAtLeast(0)
            val durStr = "${CalendarHelper.toBanglaNumber(dDiff / 60)}ঘণ্টা ${CalendarHelper.toBanglaNumber(dDiff % 60)}মি."
            val isItToday = (selectedYear == todayYear && selectedMonth == todayMonth && day == todayDay)
            val dayName = daysOfWeekBn.getOrElse(iterateCal.get(Calendar.DAY_OF_WEEK) - 1) { "" }
            val hijriDet = CalendarHelper.getHijriDateDetail(iterateCal)

            list.add(
                DateWiseTimingItem(
                    dayOfMonth = day,
                    dayOfMonthBn = CalendarHelper.toBanglaNumber(day),
                    dateBn = "${CalendarHelper.toBanglaNumber(day)} ${monthsBn[selectedMonth]}",
                    dayOfWeekBn = dayName,
                    hijriDateBn = "${CalendarHelper.toBanglaNumber(hijriDet.day)} ${hijriDet.monthNameBn}",
                    isToday = isItToday,
                    sehriEndBn = st.nextSehriFormatted,
                    fajrStartBn = st.nextSehriFormatted,
                    sunriseBn = st.sunriseTimeFormatted,
                    iftarBn = st.nextIftarFormatted,
                    durationBn = durStr
                )
            )
        }
        list
    }

    val listState = rememberLazyListState()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val globalFontController = LocalFontScaleController.current
        var localFontScale by remember { mutableFloatStateOf(globalFontController?.scale ?: 1.0f) }

        LaunchedEffect(globalFontController?.scale) {
            globalFontController?.scale?.let { localFontScale = it }
        }

        val dialogFontController = remember(globalFontController, localFontScale) {
            FontScaleController(
                scale = localFontScale,
                canDecrease = localFontScale > 0.85f,
                canIncrease = localFontScale < 1.6f,
                onDecrease = {
                    localFontScale = (localFontScale - 0.1f).coerceAtLeast(0.85f)
                    globalFontController?.onDecrease?.invoke()
                },
                onIncrease = {
                    localFontScale = (localFontScale + 0.1f).coerceAtMost(1.6f)
                    globalFontController?.onIncrease?.invoke()
                },
                onReset = {
                    localFontScale = 1.0f
                    globalFontController?.onReset?.invoke()
                }
            )
        }

        val baseDensity = LocalDensity.current
        val scaledDensity = remember(baseDensity, localFontScale) {
            Density(
                density = baseDensity.density,
                fontScale = baseDensity.fontScale * localFontScale
            )
        }

        CompositionLocalProvider(
            LocalDensity provides scaledDensity,
            LocalFontScaleController provides dialogFontController
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column {
                                Text(
                                    text = "সেহেরি ও ইফতারের বিস্তারিত সময়সূচী",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A2F)
                                )
                                Text(
                                    text = "${salatConfig.placeNameBn} • তারিখ অনুযায়ী পূর্ণাঙ্গ সময়তালিকা",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 12.sp,
                                    color = Color(0xFF047857)
                                )
                            }
                        },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Close",
                                tint = Color(0xFF1E3A2F)
                            )
                        }
                    },
                    actions = {
                        // Global font size controls
                        FontSizeActionButtons()
                        // Share button
                        IconButton(onClick = {
                            val shareText = """
                                🕋 সেহেরি ও ইফতারের সময়সূচী (${salatConfig.placeNameBn})
                                📅 $activeDateBn
                                --------------------------------
                                🥣 সেহরি শেষ সময়: ${activeDayPrayerStatus.nextSehriFormatted}
                                🌅 সূর্যোদয়: ${activeDayPrayerStatus.sunriseTimeFormatted}
                                🍲 ইফতারের সময়: ${activeDayPrayerStatus.nextIftarFormatted}
                                ⏱️ মোট রোজার দৈর্ঘ্য: $activeDurationBn
                                
                                দো'আ: اَللهُمَّ لَكَ صُمْتُ وَعَلىٰ رِزْقِكَ أَفْطَرْتُ
                                
                                — দা'ওয়াহ টু জান্নাহ্ (Dawah to Jannah)
                            """.trimIndent()
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                            }
                            context.startActivity(Intent.createChooser(intent, "সেহরি ও ইফতারের সময়সূচী শেয়ার করুন"))
                        }) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                tint = Color(0xFF1E3A2F)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFFBFDFB)
                    )
                )
            },
            containerColor = Color(0xFFF7FAF7)
        ) { innerPadding ->
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // 1. Hero Wallpaper Header with Islamic Art & Motifs
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_sehri_iftar_bg),
                            contentDescription = "Islamic Wallpaper",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Gradient Overlay for readability
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF0F2A1D).copy(alpha = 0.55f),
                                            Color(0xFF064E3B).copy(alpha = 0.88f)
                                        )
                                    )
                                )
                        )

                        // Hero Content
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = IslamicGold.copy(alpha = 0.22f),
                                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
                                ) {
                                    Text(
                                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                        color = Color(0xFFFEF3C7),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocationOn,
                                            contentDescription = null,
                                            tint = Color(0xFF6EE7B7),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = salatConfig.placeNameBn,
                                            color = Color.White,
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }

                            Column {
                                Text(
                                    text = "সাওম ও সিয়ামের বরকতময় প্রহর",
                                    color = Color(0xFFFDE68A),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = activeDateBn,
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                val hijriActive = CalendarHelper.getHijriDateDetail(activeDayCal)
                                Text(
                                    text = "হিজরি: ${CalendarHelper.toBanglaNumber(hijriActive.day)} ${hijriActive.monthNameBn}, ${CalendarHelper.toBanglaNumber(hijriActive.year)} হিজরি",
                                    color = Color(0xFFD1FAE5),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                // 2. Month & Date Dropdown Selector Row
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "মাস ও তারিখ নির্বাচন করুন (Dropdown Selection)",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(bottom = 10.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // 1) Month Dropdown Box
                                Box(modifier = Modifier.weight(1.3f)) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { isMonthDropdownOpen = true },
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFF0FDF4),
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.35f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.CalendarMonth,
                                                    contentDescription = null,
                                                    tint = Color(0xFF047857),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = monthsBn[selectedMonth],
                                                    fontSize = 14.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF065F46)
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = Color(0xFF047857)
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = isMonthDropdownOpen,
                                        onDismissRequest = { isMonthDropdownOpen = false }
                                    ) {
                                        monthsBn.forEachIndexed { index, monthName ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = monthName,
                                                        fontSize = 14.sp,
                                                        fontWeight = if (index == selectedMonth) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (index == selectedMonth) Color(0xFF047857) else Color.Unspecified
                                                    )
                                                },
                                                onClick = {
                                                    selectedMonth = index
                                                    isMonthDropdownOpen = false
                                                }
                                            )
                                        }
                                    }
                                }

                                // 2) Date Dropdown Box
                                Box(modifier = Modifier.weight(1f)) {
                                    Surface(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { isDateDropdownOpen = true },
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFFEF3C7).copy(alpha = 0.45f),
                                        border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.35f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Icon(
                                                    imageVector = Icons.Default.Today,
                                                    contentDescription = null,
                                                    tint = Color(0xFFB45309),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = "${CalendarHelper.toBanglaNumber(selectedDay)} তারিখ",
                                                    fontSize = 14.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF92400E)
                                                )
                                            }
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = Color(0xFFB45309)
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = isDateDropdownOpen,
                                        onDismissRequest = { isDateDropdownOpen = false }
                                    ) {
                                        (1..maxDaysInMonth).forEach { d ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = "${CalendarHelper.toBanglaNumber(d)} তারিখ",
                                                        fontSize = 14.sp,
                                                        fontWeight = if (d == selectedDay) FontWeight.Bold else FontWeight.Normal,
                                                        color = if (d == selectedDay) Color(0xFFB45309) else Color.Unspecified
                                                    )
                                                },
                                                onClick = {
                                                    selectedDay = d
                                                    isDateDropdownOpen = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Quick Navigation Chips: Prev Day, Today, Next Day
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        if (selectedDay > 1) {
                                            selectedDay--
                                        } else if (selectedMonth > 0) {
                                            selectedMonth--
                                            selectedDay = 28
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Icon(Icons.Default.ChevronLeft, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Text("আগের দিন", fontSize = 13.sp)
                                }

                                Button(
                                    onClick = {
                                        selectedYear = todayYear
                                        selectedMonth = todayMonth
                                        selectedDay = todayDay
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF047857)),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Text("আজকের দিন", fontSize = 13.sp, color = Color.White)
                                }

                                OutlinedButton(
                                    onClick = {
                                        if (selectedDay < maxDaysInMonth) {
                                            selectedDay++
                                        } else if (selectedMonth < 11) {
                                            selectedMonth++
                                            selectedDay = 1
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                                ) {
                                    Text("পরের দিন", fontSize = 13.sp)
                                    Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // 3. Featured Highlight Card for the Selected Day
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "নির্বাচিত দিনের সময়সূচী",
                                    fontSize = 16.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A2F)
                                )
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFF10B981).copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "রোজার দৈর্ঘ্য: $activeDurationBn",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF047857),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Timing Blocks
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Sehri Block
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFF047857).copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.2f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFF047857).copy(alpha = 0.14f),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                                Icon(
                                                    imageVector = Icons.Default.Restaurant,
                                                    contentDescription = null,
                                                    tint = Color(0xFF047857),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "সেহরি শেষ সময়", fontSize = 13.sp, color = Color(0xFF4B5563))
                                        Text(
                                            text = activeDayPrayerStatus.nextSehriFormatted,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF047857),
                                            fontFamily = LocalAppFontFamily.current
                                        )
                                        Text(text = "ফজর শুরু", fontSize = 11.5.sp, color = Color(0xFF6B7280))
                                    }
                                }

                                // Sunrise Block
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFD97706).copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.2f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFFD97706).copy(alpha = 0.14f),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                                Icon(
                                                    imageVector = Icons.Default.WbSunny,
                                                    contentDescription = null,
                                                    tint = Color(0xFFD97706),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "সূর্যোদয়", fontSize = 13.sp, color = Color(0xFF4B5563))
                                        Text(
                                            text = activeDayPrayerStatus.sunriseTimeFormatted,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFD97706),
                                            fontFamily = LocalAppFontFamily.current
                                        )
                                        Text(text = "ইশরাক শুরু", fontSize = 11.5.sp, color = Color(0xFF6B7280))
                                    }
                                }

                                // Iftar Block
                                Surface(
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFEA580C).copy(alpha = 0.08f),
                                    border = BorderStroke(1.dp, Color(0xFFEA580C).copy(alpha = 0.2f))
                                ) {
                                    Column(
                                        modifier = Modifier.padding(12.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(0xFFEA580C).copy(alpha = 0.14f),
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                                Icon(
                                                    imageVector = Icons.Default.SoupKitchen,
                                                    contentDescription = null,
                                                    tint = Color(0xFFEA580C),
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(text = "ইফতারের সময়", fontSize = 13.sp, color = Color(0xFF4B5563))
                                        Text(
                                            text = activeDayPrayerStatus.nextIftarFormatted,
                                            fontSize = 22.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFEA580C),
                                            fontFamily = LocalAppFontFamily.current
                                        )
                                        Text(text = "মাগরিব শুরু", fontSize = 11.5.sp, color = Color(0xFF6B7280))
                                    }
                                }
                            }
                        }
                    }
                }

                // 4. Full Month Timetable Section Title
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${monthsBn[selectedMonth]} মাসের পূর্ণাঙ্গ সময়তালিকা",
                            fontSize = 16.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "মোট ${CalendarHelper.toBanglaNumber(monthDaysList.size)} দিন",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280)
                        )
                    }
                }

                // Table Header
                item {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF0F2A1D)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "তারিখ ও দিন",
                                modifier = Modifier.weight(1.3f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "সেহরি শেষ",
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFA7F3D0),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "সূর্যোদয়",
                                modifier = Modifier.weight(0.9f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFDE68A),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "ইফতার",
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFED7AA),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Month Table Rows
                itemsIndexed(monthDaysList) { index, item ->
                    val isSelected = (item.dayOfMonth == selectedDay)
                    val bgColor = when {
                        isSelected -> Color(0xFFECFDF5)
                        item.isToday -> Color(0xFFFEF3C7).copy(alpha = 0.6f)
                        index % 2 == 0 -> Color.White
                        else -> Color(0xFFFAFAFA)
                    }
                    val borderColor = when {
                        isSelected -> Color(0xFF10B981)
                        item.isToday -> Color(0xFFD97706).copy(alpha = 0.5f)
                        else -> Color(0xFFE5E7EB).copy(alpha = 0.6f)
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 2.dp)
                            .clickable {
                                selectedDay = item.dayOfMonth
                            },
                        shape = RoundedCornerShape(10.dp),
                        color = bgColor,
                        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.3f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = item.dateBn,
                                        fontSize = 14.5.sp,
                                        fontWeight = if (isSelected || item.isToday) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSelected) Color(0xFF065F46) else Color(0xFF1F2937)
                                    )
                                    if (item.isToday) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFD97706)
                                        ) {
                                            Text(
                                                text = "আজ",
                                                fontSize = 10.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = item.dayOfWeekBn,
                                    fontSize = 12.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }

                            Text(
                                text = item.sehriEndBn,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = item.sunriseBn,
                                modifier = Modifier.weight(0.9f),
                                fontSize = 13.5.sp,
                                color = Color(0xFF92400E),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = item.iftarBn,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC2410C),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
}
