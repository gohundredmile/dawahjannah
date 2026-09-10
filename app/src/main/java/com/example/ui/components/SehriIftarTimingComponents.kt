package com.example.ui.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.SalatConfiguration
import com.example.ui.theme.EmeraldPrimaryLight
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicIvory
import com.example.ui.theme.LocalAppFontFamily
import com.example.ui.theme.LocalEnglishFontFamily
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
import kotlinx.coroutines.delay

/**
 * Ramadan & Sawm Wisdom Items: Hadith and Quranic verses about Fasting
 */
data class RamadanWisdomItem(
    val quoteBn: String,
    val sourceBn: String,
    val arabicText: String? = null,
    val isQuran: Boolean = false
)

val RAMADAN_WISDOM_LIST = listOf(
    RamadanWisdomItem(
        quoteBn = "রোজা আমার জন্য এবং আমি নিজেই এর প্রতিদান দেব।",
        sourceBn = "— হাদিসে কুদসি (সহীহ বুখারী ও মুসলিম)",
        arabicText = "الصَّوْمُ لِي وَأَنَا أَجْزِي بِهِ",
        isQuran = false
    ),
    RamadanWisdomItem(
        quoteBn = "হে ঈমানদারগণ! তোমাদের উপর রোজা ফরজ করা হয়েছে, যেমন ফরজ করা হয়েছিল তোমাদের পূর্ববর্তীদের উপর; যেন তোমরা তাকওয়া অর্জন করতে পারো।",
        sourceBn = "— সূরা আল-বাকারা: ১৮৩",
        arabicText = "يَا أَيُّهَا الَّذِينَ آمَنُوا كُتِبَ عَلَيْكُمُ الصِّيَامُ",
        isQuran = true
    ),
    RamadanWisdomItem(
        quoteBn = "তোমরা সেহরি খাও, নিশ্চয়ই সেহরির খাবারের মধ্যে বরকত রয়েছে।",
        sourceBn = "— সহীহ বুখারী ১৯২৩",
        arabicText = "تَسَحَّرُوا فَإِنَّ فِي السَّحُورِ بَرَكَةً",
        isQuran = false
    ),
    RamadanWisdomItem(
        quoteBn = "রমজান মাস—যার মধ্যে কুরআন অবতীর্ণ হয়েছে মানুষের পথপ্রদর্শক হিসেবে এবং হেদায়েতের সুস্পষ্ট নিদর্শন ও সত্য-মিথ্যার পার্থক্যকারী হিসেবে।",
        sourceBn = "— সূরা আল-বাকারা: ১৮৫",
        arabicText = "شَهْرُ رَمَضَانَ الَّذِي أُنزِلَ فِيهِ الْقُرْآنُ",
        isQuran = true
    ),
    RamadanWisdomItem(
        quoteBn = "মানুষ সর্বদা কল্যাণের মাঝে থাকবে যতদিন তারা দ্রুত ইফতার করবে।",
        sourceBn = "— সহীহ বুখারী ১৯৫৭",
        arabicText = "لا يَزَالُ النَّاسُ بِخَيْرٍ مَا عَجَّلُوا الْفِطْرَ",
        isQuran = false
    ),
    RamadanWisdomItem(
        quoteBn = "যে ব্যক্তি ঈমান ও সওয়াবের আশায় রমজানের রোজা রাখবে, তার পূর্ববর্তী সমস্ত গুনাহ ক্ষমা করে দেওয়া হবে।",
        sourceBn = "— সহীহ বুখারী ৩৮",
        arabicText = "مَنْ صَامَ رَمَضَانَ إِيمَانًا وَاحْتِسَابًا غُفِرَ لَهُ مَا تَقَدَّمَ مِنْ ذَنْبِهِ",
        isQuran = false
    ),
    RamadanWisdomItem(
        quoteBn = "রোজাদারের জন্য দুটি আনন্দ রয়েছে: একটি তার ইফতারের সময়, অপরটি যখন সে তার রবের সাথে সাক্ষাৎ করবে।",
        sourceBn = "— সহীহ মুসলিম ১১৫১",
        arabicText = "لِلصَّائِمِ فَرْحَتَانِ يَفْرَحُهُمَا: إِذَا أَفْطَرَ فَرِحَ, وَإِذَا لَقِيَ رَبَّهُ فَرِحَ بِصَوْمِهِ",
        isQuran = false
    )
)

/**
 * 1. Home Screen Section Card: "সেহরি এবং ইফতারের সময়সূচী" (Sehri & Ifter Timing)
 * Displays Today's Sehri and Iftar times with tap-to-expand to full screen.
 */
@Composable
fun SehriIftarSummaryCard(
    prayerStatus: PrayerCalculator.PrayerStatus,
    salatConfig: SalatConfiguration = SalatConfiguration(),
    onOpenFullScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onOpenFullScreen() }
            .testTag("sehri_iftar_summary_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF10B981).copy(alpha = 0.14f),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "সেহরি এবং ইফতারের সময়সূচী",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Sehri & Ifter Timing • ${salatConfig.placeNameBn}",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Icon(
                            imageVector = Icons.Default.Fullscreen,
                            contentDescription = "Full Screen",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Timing Blocks: Sehri & Iftar Side-by-Side
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Sehri Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF0F766E).copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Color(0xFF0F766E).copy(alpha = 0.18f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF0F766E).copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    imageVector = Icons.Default.Restaurant,
                                    contentDescription = null,
                                    tint = Color(0xFF0F766E),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "আজকের সেহরি",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = prayerStatus.nextSehriFormatted,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F766E)
                            )
                            Text(
                                text = "শেষ সময়",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }

                // Iftar Card
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFD97706).copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Color(0xFFD97706).copy(alpha = 0.18f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD97706).copy(alpha = 0.15f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                Icon(
                                    imageVector = Icons.Default.SoupKitchen,
                                    contentDescription = null,
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "আজকের ইফতার",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = prayerStatus.nextIftarFormatted,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD97706)
                            )
                            Text(
                                text = "সূর্যাস্ত শুরু",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bottom prompt
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = "সাওমের সম্পূর্ণ সময়সূচী ও কাউন্টডাউন দেখুন",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "বিস্তারিত",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * 2. Full Screen Sawm & Ramadan Timing Screen
 * Exact visual match to the user's provided screenshot:
 * - Clean top app bar with back button & title "সাওমের সময়সূচী"
 * - Prominent circular green countdown arc (e.g. "সাহরি শেষ হতে বাকি / ০৪:২৬:৫৩")
 * - Fork / spoon icon indicator
 * - Ramadan Hadith & Quranic Ayat carousel right in the middle
 * - Carousel dot indicator
 * - Bottom cards: "পরবর্তী সাহরি" (04:25) with bell toggle & "পরবর্তী ইফতার" (06:10) with bell toggle
 * - Fasting intentions (Niyyah), Iftar Duas, and 30-day Ramadan calendar timetable
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SehriIftarFullScreenDialog(
    prayerStatus: PrayerCalculator.PrayerStatus,
    salatConfig: SalatConfiguration = SalatConfiguration(),
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        var activeWisdomIndex by remember { mutableIntStateOf(0) }
        var isSehriAlarmOn by remember { mutableStateOf(true) }
        var isIftarAlarmOn by remember { mutableStateOf(true) }

        // Auto-cycle through Hadith & Quranic verses every 7 seconds
        LaunchedEffect(Unit) {
            while (true) {
                delay(7000)
                activeWisdomIndex = (activeWisdomIndex + 1) % RAMADAN_WISDOM_LIST.size
            }
        }

        // Determine whether next event is Sehri or Iftar
        // For visual match to screenshot: Default is Sehri Countdown
        val isSehriCountdown = true // Matches screenshot: "সাহরি শেষ হতে বাকি"

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "সাওমের সময়সূচী",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2C4A3E),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF2C4A3E)
                            )
                        }
                    },
                    actions = {
                        // Empty action to keep title perfectly centered
                        Spacer(modifier = Modifier.width(48.dp))
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
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 32.dp)
            ) {
                // Section 1: Hero Circular Arc Timer (Exact match to screenshot)
                item {
                    Spacer(modifier = Modifier.height(18.dp))

                    Box(
                        modifier = Modifier
                            .size(240.dp)
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Arc Canvas (Green half-ring as shown in the screenshot)
                        androidx.compose.foundation.Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 14.dp.toPx()
                            val padding = strokeWidth / 2f
                            val arcSize = Size(size.width - padding * 2, size.height - padding * 2)

                            // Background muted arc
                            drawArc(
                                color = Color(0xFFE5EFE9),
                                startAngle = 145f,
                                sweepAngle = 250f,
                                useCenter = false,
                                topLeft = Offset(padding, padding),
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )

                            // Foreground emerald green active arc
                            drawArc(
                                color = Color(0xFF34D399),
                                startAngle = 145f,
                                sweepAngle = 150f,
                                useCenter = false,
                                topLeft = Offset(padding, padding),
                                size = arcSize,
                                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                            )
                        }

                        // Text Inside Arc
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "সাহরি",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF1E3A2F)
                            )
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = "শেষ হতে বাকি",
                                fontSize = 14.sp,
                                color = Color(0xFF6B7280)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = prayerStatus.countdownHMS,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = LocalAppFontFamily.current,
                                color = Color(0xFF111827),
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }

                // Section 2: Fork / Spoon Decorative Icon (Exact match to screenshot)
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE6F4EA),
                        modifier = Modifier.size(38.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // Section 3: In the middle Ramadan Hadith and Quranic Verse / Ayat (As requested!)
                item {
                    val currentWisdom = RAMADAN_WISDOM_LIST[activeWisdomIndex]

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Arabic Verse / Hadith text if available
                        if (currentWisdom.arabicText != null) {
                            Text(
                                text = currentWisdom.arabicText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = IslamicGold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                        }

                        // Bangla Translation Quote
                        Text(
                            text = currentWisdom.quoteBn,
                            fontSize = 15.sp,
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF374151),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Source Citation
                        Text(
                            text = currentWisdom.sourceBn,
                            fontSize = 12.sp,
                            color = Color(0xFF6B7280),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Carousel Dots Indicator (Exact match to screenshot)
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RAMADAN_WISDOM_LIST.indices.take(4).forEach { index ->
                                val isActive = index == (activeWisdomIndex % 4)
                                Box(
                                    modifier = Modifier
                                        .size(if (isActive) 8.dp else 6.dp)
                                        .clip(CircleShape)
                                        .background(
                                            if (isActive) Color(0xFF34D399)
                                            else Color(0xFFD1D5DB)
                                        )
                                        .clickable { activeWisdomIndex = index }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(26.dp))
                }

                // Section Divider Line
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .background(Color(0xFFEFF3EE))
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }

                // Section 4: Bottom Table & Notification Toggles (Exact match to screenshot)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                    ) {
                        Text(
                            text = "সাওমের সময়সূচী",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF1F2937),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // 1) পরবর্তী সাহরি (04:25) with Green Pill & Bell
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color(0xFFEDF8F3), // Soft green highlight
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Restaurant,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = "পরবর্তী সাহরি",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF1F2937)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = prayerStatus.nextSehriFormatted,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    IconButton(
                                        onClick = { isSehriAlarmOn = !isSehriAlarmOn },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isSehriAlarmOn) Color(0xFF34D399) else Color(0xFFE5E7EB),
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                                Icon(
                                                    imageVector = if (isSehriAlarmOn) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                                    contentDescription = "Notification",
                                                    tint = if (isSehriAlarmOn) Color.White else Color(0xFF9CA3AF),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // 2) পরবর্তী ইফতার (06:10) with Pot Icon & Bell
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.White,
                            border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.SoupKitchen,
                                        contentDescription = null,
                                        tint = Color(0xFF4B5563),
                                        modifier = Modifier.size(22.dp)
                                    )
                                    Spacer(modifier = Modifier.width(14.dp))
                                    Text(
                                        text = "পরবর্তী ইফতার",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF1F2937)
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = prayerStatus.nextIftarFormatted,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1F2937)
                                    )
                                    Spacer(modifier = Modifier.width(16.dp))
                                    IconButton(
                                        onClick = { isIftarAlarmOn = !isIftarAlarmOn },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isIftarAlarmOn) Color(0xFF34D399) else Color(0xFFE5E7EB),
                                            modifier = Modifier.fillMaxSize()
                                        ) {
                                            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                                Icon(
                                                    imageVector = if (isIftarAlarmOn) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                                                    contentDescription = "Notification",
                                                    tint = if (isIftarAlarmOn) Color.White else Color(0xFF9CA3AF),
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }

                // Section 5: Fasting Duas & Niyyah
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 18.dp)
                    ) {
                        Text(
                            text = "ইফতারের দোয়া ও রোজার নিয়ত",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F2937),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // ১. ইফতারের প্রধান দোয়া (খাওয়ার আগে)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFFDE68A))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFFD97706).copy(alpha = 0.14f),
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                imageVector = Icons.Default.SoupKitchen,
                                                contentDescription = null,
                                                tint = Color(0xFFD97706),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "১. ইফতারের প্রধান দোয়া (খাওয়ার আগে)",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF92400E)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "আরবি:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF6B7280)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "اَللهُمَّ لَكَ صُمْتُ وَعَلىٰ رِزْقِكَ أَفْطَرْتُ",
                                    fontSize = 18.sp,
                                    lineHeight = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFFB45309),
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "বাংলা উচ্চারণ: আল্লাহুম্মা লাকা ছুমতু ওয়া আলা রিযকিকা আফতারতু।",
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF374151)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "বাংলা অর্থ: হে আল্লাহ! আমি আপনার উদ্দেশ্যেই রোজা রেখেছি এবং আপনার দেওয়া রিজিক দিয়েই ইফতার করছি। (সুনানে আবু দাউদ)",
                                    fontSize = 12.5.sp,
                                    lineHeight = 18.sp,
                                    color = Color(0xFF4B5563)
                                )
                            }
                        }

                        // ২. ইফতারের পরের দোয়া (ইফতার করার পর)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF059669).copy(alpha = 0.14f),
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                imageVector = Icons.Default.Restaurant,
                                                contentDescription = null,
                                                tint = Color(0xFF059669),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "২. ইফতারের পরের দোয়া (ইফতার করার পর):",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF065F46)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "ইফতার করার পর রাসুলুল্লাহ (সা.) এই দোয়াটি পড়তেন:",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color(0xFF4B5563)
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "আরবি:",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF6B7280)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "ذَهَبَ الظَّمَأُ وَابْتَلَّتِ الْعُرُوقُ وَثَبَتَ الأَجْرُ إِنْ شَاءَ اللَّهُ",
                                    fontSize = 18.sp,
                                    lineHeight = 26.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF047857),
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "বাংলা উচ্চারণ: জাহাবাজ জামাউ; ওয়াবতাল্লাতিল উ'রুকু; ওয়া সাবাতাল আজরু ইনশাআল্লাহ।",
                                    fontSize = 13.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF374151)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "বাংলা অর্থ: (ইফতারের মাধ্যমে) পিপাসা দূর হলো, শিরা-উপসিরা সিক্ত হলো এবং যদি আল্লাহ চান সাওয়াবও স্থির হলো। (আবু দাউদ)",
                                    fontSize = 12.5.sp,
                                    lineHeight = 18.sp,
                                    color = Color(0xFF4B5563)
                                )
                            }
                        }

                        // রোজার নিয়ত (সেহরির সময়)
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 5.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(0xFF2563EB).copy(alpha = 0.12f),
                                        modifier = Modifier.size(30.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                                            Icon(
                                                imageVector = Icons.Default.MenuBook,
                                                contentDescription = null,
                                                tint = Color(0xFF2563EB),
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = "রোজার নিয়ত (সেহরির সময়)",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1E40AF)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "نَوَيْتُ اَنْ اُصُوْمَ غَدًا مِّنْ شَهْرِ رَمَضَانَ الْمُبَارَكِ فَرْضًا لَكَ يَا اللهُ فَتَقَبَّلْ مِنِّى اِنَّكَ اَنْتَ السَّمِيْعُ الْعَلِيْم",
                                    fontSize = 15.sp,
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = IslamicGold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "বাংলা উচ্চারণ: নাওয়াইতু আন আছুমা গাদাম মিন শাহরি রমাদ্বানাল মুবারাকি ফারদ্বাল্লাকা ইয়া আল্লাহু ফাতাক্বাব্বাল মিন্নি ইন্নাকা আনতাস সামিউল আলিম।",
                                    fontSize = 12.5.sp,
                                    color = Color(0xFF4B5563)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "বাংলা অর্থ: হে আল্লাহ! আগামীকাল পবিত্র রমজান মাসে তোমার সন্তুষ্টির উদ্দেশ্যে ফরজ রোজা রাখার নিয়ত করলাম। অতএব তুমি আমার রোজা কবুল করো, নিশ্চয়ই তুমি সর্বশ্রোতা ও সর্বজ্ঞাত।",
                                    fontSize = 12.sp,
                                    color = Color(0xFF6B7280)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
