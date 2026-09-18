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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.data.remote.MoonSightingApiService
import com.example.data.remote.MoonSightingLiveState
import com.example.data.remote.RamadanYearForecast
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalAppFontFamily
import com.example.util.CalendarHelper
import kotlinx.coroutines.launch

/**
 * রামাদান সময়সূচী (Ramadan Schedule based on Moon Sighting)
 * Aligned with international & local moon sighting data.
 * Features:
 * - Live data connection with Aladhan API & moon sighting observation database
 * - Shows upcoming Ramadan timing for upcoming years (1448 AH to 1453 AH)
 * - Automatic update / alignment upon moon sighting (29 Sha'ban)
 * - Toggle between Bangladesh (Local) and Saudi Arabia (International)
 * - Full 30-Day Ramadan timetable categorized into 3 Ashras (Rahmat, Magfirat, Najaat) with Lailatul Qadr
 * - Exceptional beautiful Islamic wallpaper and elements (Crescent, Lantern, Mosques)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamadanMoonScheduleDialog(
    salatConfig: SalatConfiguration = SalatConfiguration(),
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val moonService = remember { MoonSightingApiService(context) }

    var liveState by remember { mutableStateOf(MoonSightingLiveState()) }
    var isRefreshing by remember { mutableStateOf(false) }

    // Toggle: True = Local (Bangladesh / Islamic Foundation), False = International (Saudi Arabia / Umm al-Qura)
    var isLocalBangladesh by remember { mutableStateOf(true) }

    // Selected Ramadan Forecast Index (Default 0 = 1448 AH / 2027 CE)
    var selectedForecastIndex by remember { mutableIntStateOf(0) }
    val forecasts = moonService.upcomingRamadanForecasts
    val activeForecast = forecasts.getOrElse(selectedForecastIndex) { forecasts[0] }

    // Generate 30 days timetable for active forecast and location
    val timetable = remember(activeForecast, isLocalBangladesh, salatConfig) {
        moonService.generateRamadanTimetable(activeForecast, isLocalBangladesh, salatConfig)
    }

    // Load live status on entry
    LaunchedEffect(Unit) {
        isRefreshing = true
        liveState = moonService.fetchLiveMoonSightingStatus()
        isRefreshing = false
    }

    fun refreshLiveData() {
        scope.launch {
            isRefreshing = true
            liveState = moonService.fetchLiveMoonSightingStatus()
            isRefreshing = false
        }
    }

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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "রামাদান সময়সূচী",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E3A2F)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = Color(0xFFD97706)
                                ) {
                                    Text(
                                        text = "চাঁদ দেখা ভিত্তিক",
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "আন্তর্জাতিক ও স্থানীয় চাঁদ পর্যবেক্ষণ ভিত্তিক লাইভ সময়তালিকা",
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 11.sp,
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
                        FontSizeActionButtons()

                        IconButton(onClick = {
                            val shareBody = """
                                🌙 রামাদান সময়সূচী (${activeForecast.hijriTitleBn})
                                📍 স্থান: ${salatConfig.placeNameBn}
                                --------------------------------
                                📌 রোজা শুরুর সম্ভাব্য তারিখ: ${if (isLocalBangladesh) activeForecast.localStartDateBn else activeForecast.internationalStartDateBn}
                                🔭 চাঁদ দেখার রাত: ${activeForecast.moonSightingEveBn}
                                🕌 সম্ভাব্য ঈদুল ফিতর: ${activeForecast.approxEidDateBn}
                                📜 ভিত্তি: ${if (isLocalBangladesh) activeForecast.localSource else activeForecast.internationalSource}
                                
                                *চাঁদ দেখা সাপেক্ষে স্বয়ংক্রিয় রূপান্তর কার্যকর।*
                                
                                — দা'ওয়াহ টু জান্নাহ্ (Dawah to Jannah)
                            """.trimIndent()
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareBody)
                            }
                            context.startActivity(Intent.createChooser(intent, "রামাদান সময়সূচী শেয়ার করুন"))
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 36.dp)
            ) {
                // 1. Hero Islamic Wallpaper with Glowing Crescent Moon & Lanterns
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_ramadan_moon_bg),
                            contentDescription = "Ramadan Moon Wallpaper",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Twilight Gradient Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF091E16).copy(alpha = 0.5f),
                                            Color(0xFF042F24).copy(alpha = 0.85f)
                                        )
                                    )
                                )
                        )

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
                                    color = IslamicGold.copy(alpha = 0.25f),
                                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.55f))
                                ) {
                                    Text(
                                        text = "شَهْرُ رَمَضَانَ الْمُبَارَك",
                                        color = Color(0xFFFEF3C7),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
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
                                            modifier = Modifier.size(13.dp)
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
                                    text = "পবিত্র মাহে রমজান ও চাঁদ দেখা পর্যবেক্ষণ",
                                    color = Color(0xFFFDE68A),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = activeForecast.hijriTitleBn,
                                    color = Color.White,
                                    fontSize = 19.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "সম্ভাব্য শুরু: ${if (isLocalBangladesh) activeForecast.localStartDateBn else activeForecast.internationalStartDateBn}",
                                    color = Color(0xFFA7F3D0),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // 2. Live Moon Sighting API & Sync Banner
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.25f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (liveState.isLiveConnected) Color(0xFF10B981) else Color(0xFFD97706),
                                        modifier = Modifier.size(10.dp)
                                    ) {}
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = if (liveState.isLiveConnected) "অনলাইন লাইভ ডাটা সক্রিয়" else "সংরক্ষিত জ্যোতির্বৈজ্ঞানিক ডাটা",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (liveState.isLiveConnected) Color(0xFF047857) else Color(0xFFB45309)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isRefreshing) {
                                        CircularProgressIndicator(
                                            modifier = Modifier.size(16.dp),
                                            strokeWidth = 2.dp,
                                            color = Color(0xFF047857)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                    }
                                    Surface(
                                        modifier = Modifier.clickable { refreshLiveData() },
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFFECFDF5),
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Refresh,
                                                contentDescription = "Refresh",
                                                tint = Color(0xFF047857),
                                                modifier = Modifier.size(14.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "লাইভ রিফ্রেশ",
                                                fontSize = 12.5.sp,
                                                color = Color(0xFF047857),
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }
                                    }
                                }
                            }

                            if (liveState.lastCheckedTimeBn.isNotEmpty()) {
                                Text(
                                    text = liveState.lastCheckedTimeBn,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF6B7280),
                                    modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
                                )
                            }

                            Text(
                                text = liveState.headlineBn,
                                fontSize = 13.5.sp,
                                lineHeight = 19.sp,
                                color = Color(0xFF374151),
                                modifier = Modifier.padding(top = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFF8FAFC),
                                border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        text = "চাঁদ দেখার পূর্বাভাস ও বৈজ্ঞানিক প্যারামিটার:",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E293B)
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "• চাঁদের দৃশ্যমানতার স্থিতি: ${liveState.sightingProbabilityBn}",
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Text(
                                        text = "• জাতীয় সমন্বয়: ${liveState.islamicFoundationStatusBn}",
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF475569)
                                    )
                                    Text(
                                        text = "• আন্তর্জাতিক ডাটাবেজ: ${liveState.internationalStatusBn}",
                                        fontSize = 12.5.sp,
                                        color = Color(0xFF475569)
                                    )
                                }
                            }
                        }
                    }
                }

                // 3. International vs Local Sighting Alignment Selector
                item {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)) {
                        Text(
                            text = "চাঁদ দেখা সমন্বয় পদ্ধতি (Sighting Criteria):",
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Local Button
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isLocalBangladesh = true },
                                shape = RoundedCornerShape(12.dp),
                                color = if (isLocalBangladesh) Color(0xFF047857) else Color.White,
                                border = BorderStroke(1.dp, if (isLocalBangladesh) Color(0xFF047857) else Color(0xFFD1D5DB))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LocationOn,
                                        contentDescription = null,
                                        tint = if (isLocalBangladesh) Color.White else Color(0xFF4B5563),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "বাংলাদেশ ও স্থানীয়",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isLocalBangladesh) Color.White else Color(0xFF374151)
                                    )
                                }
                            }

                            // International Button
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isLocalBangladesh = false },
                                shape = RoundedCornerShape(12.dp),
                                color = if (!isLocalBangladesh) Color(0xFF1E40AF) else Color.White,
                                border = BorderStroke(1.dp, if (!isLocalBangladesh) Color(0xFF1E40AF) else Color(0xFFD1D5DB))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Public,
                                        contentDescription = null,
                                        tint = if (!isLocalBangladesh) Color.White else Color(0xFF4B5563),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "সৌদি ও আন্তর্জাতিক",
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (!isLocalBangladesh) Color.White else Color(0xFF374151)
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (isLocalBangladesh)
                                "ভিত্তি: জাতীয় চাঁদ দেখা কমিটি (বায়তুল মোকাররম) ও ইসলামিক ফাউন্ডেশন বাংলাদেশ।"
                            else
                                "ভিত্তি: উম্মুল কুরা বর্ষপঞ্জি, মক্কা মুকাররমা ও আন্তর্জাতিক ক্রিসেন্ট পর্যবেক্ষণ প্রকল্প (ICOP)।",
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(top = 4.dp, start = 2.dp)
                        )
                    }
                }

                // 4. Upcoming Years Selector Chips
                item {
                    Column(modifier = Modifier.padding(vertical = 8.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "আসন্ন বছরসমূহের রমজান (Upcoming Years)",
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1F2937)
                            )
                            Text(
                                text = "১৪৪৮-১৪৫৩ হিজরি",
                                fontSize = 12.5.sp,
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState())
                                .padding(horizontal = 14.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            forecasts.forEachIndexed { index, item ->
                                val isSelected = (index == selectedForecastIndex)
                                Surface(
                                    modifier = Modifier.clickable { selectedForecastIndex = index },
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isSelected) Color(0xFF047857) else Color.White,
                                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF047857) else Color(0xFFD1D5DB)),
                                    shadowElevation = if (isSelected) 2.dp else 0.dp
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = "${CalendarHelper.toBanglaNumber(item.hijriYear)} হিজরি",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color.White else Color(0xFF1F2937)
                                        )
                                        Text(
                                            text = "${CalendarHelper.toBanglaNumber(item.gregorianYear)} খ্রি.",
                                            fontSize = 12.sp,
                                            color = if (isSelected) Color(0xFFA7F3D0) else Color(0xFF6B7280)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // 5. Active Year Detailed Forecast Card
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 6.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.3f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = activeForecast.hijriTitleBn,
                                        fontSize = 17.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46)
                                    )
                                    Text(
                                        text = activeForecast.statusBn,
                                        fontSize = 12.5.sp,
                                        color = Color(0xFFD97706),
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color(0xFFFEF3C7)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DarkMode,
                                            contentDescription = null,
                                            tint = Color(0xFFB45309),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "হিলাল দৃশ্যমানতা",
                                            fontSize = 11.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Key Forecast Info Grid
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFFF0FDF4),
                                border = BorderStroke(1.dp, Color(0xFF86EFAC))
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("রোজা শুরুর সম্ভাব্য তারিখ:", fontSize = 13.sp, color = Color(0xFF374151))
                                        Text(
                                            text = if (isLocalBangladesh) activeForecast.localStartDateBn else activeForecast.internationalStartDateBn,
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF047857)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("চাঁদ দেখার সম্ভাব্য রাত:", fontSize = 13.sp, color = Color(0xFF374151))
                                        Text(
                                            text = activeForecast.moonSightingEveBn,
                                            fontSize = 12.5.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(0xFFB45309)
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("সম্ভাব্য ঈদুল ফিতর:", fontSize = 13.sp, color = Color(0xFF374151))
                                        Text(
                                            text = activeForecast.approxEidDateBn,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF1E40AF)
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "জ্যোতির্বৈজ্ঞানিক হিসাব ও চাঁদের অবস্থান: ${activeForecast.moonPhaseDescription}",
                                fontSize = 12.5.sp,
                                lineHeight = 18.sp,
                                color = Color(0xFF4B5563)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "★ স্বয়ংক্রিয় আপডেট: ${activeForecast.differenceNoteBn}",
                                fontSize = 12.sp,
                                color = Color(0xFF047857),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // 6. Full 30-Day Ramadan Timetable Header
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "৩০ দিনের পূর্ণাঙ্গ রমজান সময়সূচী (${salatConfig.placeNameBn})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937)
                        )
                        Text(
                            text = "৩টি দশক ভিত্তিক",
                            fontSize = 12.5.sp,
                            color = Color(0xFF047857),
                            fontWeight = FontWeight.Medium
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
                        color = Color(0xFF042F24)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "রমজান ও তারিখ",
                                modifier = Modifier.weight(1.4f),
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
                                text = "ইফতার",
                                modifier = Modifier.weight(1f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFDE68A),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "রোজার দৈর্ঘ্য",
                                modifier = Modifier.weight(1f),
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Timetable Rows
                items(timetable) { dayItem ->
                    val ashraColor = when {
                        dayItem.dayNumber <= 10 -> Color(0xFF047857) // Rahmat
                        dayItem.dayNumber <= 20 -> Color(0xFF1E40AF) // Magfirat
                        else -> Color(0xFF92400E) // Najaat
                    }

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 2.dp),
                        shape = RoundedCornerShape(10.dp),
                        color = if (dayItem.isLailatulQadrNight) Color(0xFFFEF3C7).copy(alpha = 0.7f) else Color.White,
                        border = BorderStroke(
                            if (dayItem.isLailatulQadrNight) 1.5.dp else 1.dp,
                            if (dayItem.isLailatulQadrNight) Color(0xFFD97706) else Color(0xFFE5E7EB)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1.4f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = dayItem.dayNumberBn,
                                        fontSize = 14.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ashraColor
                                    )
                                    if (dayItem.isLailatulQadrNight) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Surface(
                                            shape = RoundedCornerShape(4.dp),
                                            color = Color(0xFFD97706)
                                        ) {
                                            Text(
                                                text = "কদর",
                                                fontSize = 10.sp,
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = "${dayItem.dateBn} • ${dayItem.dayOfWeekBn}",
                                    fontSize = 12.sp,
                                    color = Color(0xFF4B5563)
                                )
                            }

                            Text(
                                text = dayItem.sehriEndBn,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047857),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = dayItem.iftarBn,
                                modifier = Modifier.weight(1f),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC2410C),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = dayItem.durationBn,
                                modifier = Modifier.weight(1f),
                                fontSize = 12.5.sp,
                                color = Color(0xFF374151),
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
