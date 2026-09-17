package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FlipClockFont
import com.example.data.model.ForbiddenTimeInfo
import com.example.ui.theme.LocalAppFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.theme.LocalEnglishFontFamily
import com.example.ui.theme.LocalFlipClockFont
import com.example.ui.theme.LocalFlipClockFontFamily
import com.example.util.CalendarHelper
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale

/**
 * Data item for continuous informative rotation in Present Salat Split-Flap Board
 */
data class SalatContextInfo(
    val timeLabel: String,
    val salatInfo: String,
    val isAlert: Boolean = false
)

/**
 * Revamped 3-Section Salat Timing Card
 * Section 1 (Left): 'Next Salat' (পরবর্তী সালাত) - Clean view without vertical dashed divider
 * Section 2 (Middle): 'Present Salat' (বর্তমান সালাত) - Wider Split-flap board with continuous 5s info rotation & Salat forbidden time alert (hh:mm)
 * Section 3 (Right): 'Remaining Time' (শেষ হতে বাকী) - Authentic split-flap clock matching video, bold English font, HH:MM display
 */
@Composable
fun SalatTimingFlipCard(
    nextPrayerName: String,
    presentPrayerName: String,
    presentNofolName: String,
    remainingHours: Int,
    remainingMinutes: Int,
    remainingSeconds: Int = 0,
    countdownFormatted: String = "",
    forbiddenTimeInfo: ForbiddenTimeInfo = ForbiddenTimeInfo(),
    modifier: Modifier = Modifier,
    onClickCard: () -> Unit = {}
) {
    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current

    // Video-exact English typography: bold, heavy grotesque sans-serif
    val clockDigitFont = if (englishFont != FontFamily.Default) englishFont else FontFamily.SansSerif

    // -----------------------------------------------------------------
    // Contextual Nofol, Salat & Forbidden Time list for 5-second continuous rotation
    // -----------------------------------------------------------------
    val infoList = remember(presentPrayerName, presentNofolName, forbiddenTimeInfo) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val totalMins = hour * 60 + minute

        val items = mutableListOf<SalatContextInfo>()

        // 1. Current Wakt & Nofol as primary base pair
        val waktTitle = if (presentPrayerName.isNotEmpty()) "বর্তমান ওয়াক্ত: $presentPrayerName" else "বর্তমান ওয়াক্ত"
        val nofolTitle = if (presentNofolName.isNotEmpty()) "$presentNofolName এর সময়" else "নফল ইবাদত"
        items.add(SalatContextInfo(waktTitle, nofolTitle))

        // 2. Salat Forbidden Time (সালাত নিষিদ্ধ সময়) - Alert for upcoming / active wakt
        if (forbiddenTimeInfo.isCurrentlyForbidden) {
            items.add(
                SalatContextInfo(
                    timeLabel = "⚠️ সালাত নিষিদ্ধ চলছে!",
                    salatInfo = "${forbiddenTimeInfo.activeForbiddenName} (সালাত মাকরূহ)",
                    isAlert = true
                )
            )
        } else {
            // Determine the upcoming forbidden time window with exact HH:MM
            val upcomingForbidden = when {
                totalMins < 356 -> { // Before Sunrise ends (~05:56)
                    SalatContextInfo(
                        timeLabel = "⚠️ আসন্ন সালাত নিষিদ্ধ সময়",
                        salatInfo = "সূর্যোদয়: ${forbiddenTimeInfo.sunriseStart24} - ${forbiddenTimeInfo.sunriseEnd24}",
                        isAlert = true
                    )
                }
                totalMins < 717 -> { // Before Midday Zawal ends (~11:57)
                    SalatContextInfo(
                        timeLabel = "⚠️ আসন্ন সালাত নিষিদ্ধ সময়",
                        salatInfo = "জাওয়াল: ${forbiddenTimeInfo.zawalStart24} - ${forbiddenTimeInfo.zawalEnd24}",
                        isAlert = true
                    )
                }
                totalMins < 1094 -> { // Before Sunset ends (~18:14)
                    SalatContextInfo(
                        timeLabel = "⚠️ আসন্ন সালাত নিষিদ্ধ সময়",
                        salatInfo = "সূর্যাস্ত: ${forbiddenTimeInfo.sunsetStart24} - ${forbiddenTimeInfo.sunsetEnd24}",
                        isAlert = true
                    )
                }
                else -> { // Night (Isha & Tahajjud) -> upcoming is tomorrow's sunrise
                    SalatContextInfo(
                        timeLabel = "⚠️ আসন্ন সালাত নিষিদ্ধ সময়",
                        salatInfo = "সূর্যোদয়: ${forbiddenTimeInfo.sunriseStart24} - ${forbiddenTimeInfo.sunriseEnd24}",
                        isAlert = true
                    )
                }
            }
            items.add(upcomingForbidden)
        }

        // 3. Time-appropriate authentic Sunnah & Nofol salat entries
        when {
            // Fajr period (approx 4:00 - 5:45)
            totalMins in 240..345 -> {
                items.add(SalatContextInfo("ভোরবেলা", "ফজরের সুন্নাত ও জামাত"))
                items.add(SalatContextInfo("শুভ প্রভাত", "তাহিয়্যাতুল ওজু ও জিকির"))
            }
            // Sunrise forbidden window (approx 5:45 - 6:05)
            totalMins in 346..370 -> {
                items.add(SalatContextInfo("সূর্যোদয় প্রহর", "জিকির ও ইস্তিগফারের সর্বোত্তম সময়"))
            }
            // Morning Ishraq & Chasht / Salatud-Duha (approx 6:05 - 11:30)
            totalMins in 371..690 -> {
                items.add(SalatContextInfo("স্নিগ্ধ সকাল", "ইশরাক ও সালাতুদ-দুহা র সময়"))
                items.add(SalatContextInfo("চাশতের নামায", "বরকতময় নফল ইবাদত (মুসলিম ৭৪৮)"))
            }
            // Midday Zawal forbidden window (approx 11:35 - 11:55)
            totalMins in 691..725 -> {
                items.add(SalatContextInfo("দ্বিপ্রহর সতর্কবার্তা", "সূর্য মধ্যাকাশে থাকায় সালাত মাকরূহ", isAlert = true))
            }
            // Dhuhr period (approx 12:00 - 15:30)
            totalMins in 726..930 -> {
                items.add(SalatContextInfo("দুপুর বেলা", "যোহরের ওয়াক্ত ও কাবলিয়া সুন্নাত"))
                items.add(SalatContextInfo("যোহরের নফল", "৪ রাকাত পূর্ব সুন্নাত ও ২ রাকাত বা'দিয়া"))
            }
            // Asr period (approx 15:30 - 18:00)
            totalMins in 931..1070 -> {
                items.add(SalatContextInfo("আসরের ওয়াক্ত", "আসরের পূর্ব ৪ রাকাত নফল বা সুন্নাত"))
                items.add(SalatContextInfo("আসরের সুন্নাত", "আল্লাহর বিশেষ রহমতের সুসংবাদ"))
            }
            // Sunset / Maghrib / Awwabin (approx 18:00 - 19:30)
            totalMins in 1071..1170 -> {
                items.add(SalatContextInfo("শুভ সন্ধ্যা", "আওয়াবিন এর সময়"))
                items.add(SalatContextInfo("সালাতুল আওয়াবিন", "মাগরিবের পর ৬ রাকাত নফল"))
            }
            // Isha and Night / Tahajjud (approx 19:30 - 24:00 and 00:00 - 04:00)
            else -> {
                items.add(SalatContextInfo("তাহাজ্জুদের সময়", "রাতের শ্রেষ্ঠ নফল ইবাদত (মুসলিম ৭৫৮)"))
                items.add(SalatContextInfo("রাতের শেষ প্রহর", "তাহাজ্জুদ ও দোয়া কবুলের শ্রেষ্ঠ সময়"))
                items.add(SalatContextInfo("বিতর সালাত", "রাতের শেষ সালাত হিসাবে বিতর আদায়"))
            }
        }

        items
    }

    var currentInfoIndex by remember { mutableIntStateOf(0) }
    val boardFlipProgress = remember { Animatable(1f) }

    // Continuous 5-second cycle for the middle split-flap board
    LaunchedEffect(infoList.size) {
        while (true) {
            delay(5000L)
            boardFlipProgress.snapTo(0f)
            val nextIdx = (currentInfoIndex + 1) % infoList.size
            currentInfoIndex = nextIdx
            boardFlipProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing)
            )
        }
    }

    val currentInfo = infoList.getOrElse(currentInfoIndex % infoList.size) {
        SalatContextInfo(
            if (presentPrayerName.isNotEmpty()) "বর্তমান ওয়াক্ত: $presentPrayerName" else "বর্তমান ওয়াক্ত",
            if (presentNofolName.isNotEmpty()) "$presentNofolName এর সময়" else "নফল ইবাদত"
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 5.dp)
            .testTag("revamped_salat_timing_card"),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // -------------------------------------------------------------
        // 1. LEFT SECTION: পরবর্তী সালাত (Next Salat)
        // (No vertical dashed divider, clean and balanced)
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .weight(0.68f)
                .padding(horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "পরবর্তী সালাত",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                fontFamily = banglaFont,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = nextPrayerName,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 22.sp),
                fontFamily = banglaFont,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // -------------------------------------------------------------
        // 2. MIDDLE SECTION: বর্তমান সালাত (Present Salat & Nofol Flip Board)
        // Made significantly wider (weight 1.85f) for full comfortable text
        // Flips continuously every 3 seconds with rich authentic Sunnah info
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1.85f)
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "বর্তমান সালাত ও নফল",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                fontFamily = banglaFont,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Flipping Board Outer Casing
            val rotAngle = if (boardFlipProgress.value < 0.5f) {
                -180f * boardFlipProgress.value
            } else {
                180f * (1f - boardFlipProgress.value)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .graphicsLayer {
                        rotationX = rotAngle
                        cameraDistance = 14f * density
                    }
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF2B303A),
                                Color(0xFF1E2229),
                                Color(0xFF15171B)
                            )
                        )
                    )
                    .border(BorderStroke(1.dp, Color(0xFF475569)), RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        currentInfoIndex = (currentInfoIndex + 1) % infoList.size
                        onClickCard()
                    }
            ) {
                // Outer Corner Screws / Rivets for realistic hardware look
                CornerRivet(modifier = Modifier.align(Alignment.TopStart).padding(3.dp))
                CornerRivet(modifier = Modifier.align(Alignment.TopEnd).padding(3.dp))
                CornerRivet(modifier = Modifier.align(Alignment.BottomStart).padding(3.dp))
                CornerRivet(modifier = Modifier.align(Alignment.BottomEnd).padding(3.dp))

                // Left & Right Metal Pivot / Hinge Clips
                SideHingeClip(modifier = Modifier.align(Alignment.CenterStart))
                SideHingeClip(modifier = Modifier.align(Alignment.CenterEnd))

                // Inner Split Flap Plate
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 6.dp, vertical = 3.5.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFFFFF),
                                    Color(0xFFF8FAFC),
                                    Color(0xFFEDF2F7)
                                )
                            )
                        )
                        .border(BorderStroke(0.5.dp, Color(0xFFCBD5E1)), RoundedCornerShape(4.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Upper Flap: Time Label (e.g. স্নিগ্ধ সকাল / শুভ রাত্রি / বর্তমান ওয়াক্ত: এশা / ⚠️ সালাত নিষিদ্ধ সময়)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentInfo.timeLabel,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 13.sp,
                                    letterSpacing = 0.2.sp
                                ),
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Black,
                                color = if (currentInfo.isAlert) Color(0xFFDC2626) else Color(0xFF0F172A),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        // Split Seam / Horizontal Line
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Color(0xFF334155))
                        )

                        // Lower Flap: Salat / Nofol Info (e.g. সালাতুদ-দুহা র সময় / তাহাজ্জুদের সময় / নিষিদ্ধ সময় hh:mm)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentInfo.salatInfo,
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontSize = 12.sp,
                                    letterSpacing = 0.1.sp
                                ),
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Bold,
                                color = if (currentInfo.isAlert) Color(0xFFB91C1C) else Color(0xFF1E293B),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // 3. RIGHT SECTION: শেষ হতে বাকী (Authentic HTC Sense Flip Clock Board)
        // Matches video: English bold font style, realistic 3D split-flap drop,
        // live second/minute countdown, knurled central roller
        // -------------------------------------------------------------
        var showHoursMode by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .weight(1.24f)
                .padding(start = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header title: displays hours remaining badge if in MM:SS mode and hours > 0
            val headerText = if (remainingHours > 0 && !showHoursMode) {
                "${CalendarHelper.toBanglaNumber(remainingHours)} ঘণ্টা বাকী"
            } else {
                "শেষ হতে বাকী"
            }

            Text(
                text = headerText,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                fontFamily = banglaFont,
                fontWeight = FontWeight.SemiBold,
                color = if (remainingHours > 0 && !showHoursMode) Color(0xFFB45309) else Color(0xFF374151),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Two flip tiles:
            // Default mode (!showHoursMode): Left = Minutes, Right = Seconds (ticks & flips every second in 3D!)
            // Hours mode (showHoursMode): Left = Hours, Right = Minutes
            val leftRaw = if (showHoursMode) remainingHours else remainingMinutes
            val rightRaw = if (showHoursMode) remainingMinutes else remainingSeconds

            val leftStr = String.format(Locale.US, "%02d", leftRaw)
            val rightStr = String.format(Locale.US, "%02d", rightRaw)
            val currentFlipClockFont = LocalFlipClockFont.current
            val currentFlipClockFamily = LocalFlipClockFontFamily.current

            // Retro Mechanical HTC-style Flip Clock Board (Tap to toggle mode)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        if (remainingHours > 0) {
                            showHoursMode = !showHoursMode
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Left Flip Tile: Minutes (or Hours)
                HtcFlipDigitCard(
                    digits = leftStr,
                    clockFont = currentFlipClockFont,
                    fontFamily = currentFlipClockFamily,
                    hasLeftHinge = true,
                    hasRightHinge = true
                )

                // Central colon post matching flipboard3.jpg
                FlipClockCenterPost()

                // Right Flip Tile: Seconds (or Minutes) - flips every second
                HtcFlipDigitCard(
                    digits = rightStr,
                    clockFont = currentFlipClockFont,
                    fontFamily = currentFlipClockFamily,
                    hasLeftHinge = true,
                    hasRightHinge = true
                )
            }

            // Sub-label indicating unit
            val unitText = if (showHoursMode) "ঘণ্টা : মিনিট" else "মিনিট : সেকেন্ড"
            Text(
                text = unitText,
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B),
                    fontFamily = banglaFont,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 3.dp)
            )
        }
    }
}

/**
 * Authentic HTC Phone Sense 3D Flip Clock Digit Tile
 * Features 3D top-down split flap flip animation whenever [digits] changes:
 * - In Phase 1: The old top half flap folds downward, revealing the new number behind it.
 * - In Phase 2: The new bottom half flap drops down into place with realistic perspective and shadow.
 */
@Composable
internal fun HtcFlipDigitCard(
    digits: String,
    clockFont: FlipClockFont = LocalFlipClockFont.current,
    fontFamily: FontFamily = LocalFlipClockFontFamily.current,
    hasLeftHinge: Boolean = true,
    hasRightHinge: Boolean = true,
    modifier: Modifier = Modifier
) {
    var displayedDigits by remember { mutableStateOf(digits) }
    var previousDigits by remember { mutableStateOf(digits) }
    val flipProgress = remember { Animatable(1f) }

    LaunchedEffect(digits) {
        if (digits != displayedDigits) {
            previousDigits = displayedDigits
            displayedDigits = digits
            flipProgress.snapTo(0f)
            flipProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 380, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(
        modifier = modifier.padding(horizontal = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        // Multi-card Stack Layer Effect at the Bottom (stacked flap deck)
        Column(
            modifier = Modifier
                .width(44.dp)
                .height(58.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Under-layer 2
            Box(
                modifier = Modifier
                    .width(38.dp)
                    .height(1.dp)
                    .background(Color(0xFFD1D5DB), RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
            )
            // Under-layer 1
            Box(
                modifier = Modifier
                    .width(41.dp)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
            )
        }

        // Main Flap Card Assembly
        Box(
            modifier = Modifier
                .size(width = 44.dp, height = 55.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0xFF1E2229))
                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(6.dp))
        ) {
            // BASE LAYER:
            // Top half displays the NEW digit (revealed when old flap falls down)
            // Bottom half displays OLD digit until the new bottom flap lands
            val bottomBaseDigits = if (flipProgress.value < 1.0f) previousDigits else displayedDigits

            Column(modifier = Modifier.fillMaxSize()) {
                DigitHalf(digits = displayedDigits, isTopHalf = true, clockFont = clockFont, fontFamily = fontFamily)
                // Center Horizontal Split Groove
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF334155))
                )
                DigitHalf(digits = bottomBaseDigits, isTopHalf = false, clockFont = clockFont, fontFamily = fontFamily)
            }

            // FLIPPING LAYER (HTC Sense 3D Downward Flap):
            if (flipProgress.value < 0.5f) {
                // Phase 1 (0.0 .. 0.5): The old top flap flips FORWARD & DOWNWARDS from 0° to -90°
                val p1 = flipProgress.value / 0.5f
                val rotX = -90f * p1
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(27.dp)
                        .graphicsLayer {
                            rotationX = rotX
                            cameraDistance = 16f * density
                            transformOrigin = TransformOrigin(0.5f, 1.0f) // Pivot at bottom edge of top half
                        }
                ) {
                    DigitHalf(digits = previousDigits, isTopHalf = true, clockFont = clockFont, fontFamily = fontFamily)
                    // Dynamic shadow overlay during downward fold
                    val shadowAlpha = (p1 * 0.55f).coerceIn(0f, 0.55f)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                            .background(Color.Black.copy(alpha = shadowAlpha))
                    )
                }
            } else if (flipProgress.value < 1.0f) {
                // Phase 2 (0.5 .. 1.0): The new bottom flap lands from +90° down to 0° flat
                val p2 = (flipProgress.value - 0.5f) / 0.5f
                val rotX = 90f * (1f - p2)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(27.dp)
                        .graphicsLayer {
                            rotationX = rotX
                            cameraDistance = 16f * density
                            transformOrigin = TransformOrigin(0.5f, 0.0f) // Pivot at top edge of bottom half
                        }
                ) {
                    DigitHalf(digits = displayedDigits, isTopHalf = false, clockFont = clockFont, fontFamily = fontFamily)
                    // Dynamic shadow fading away as card lands flat in place
                    val shadowAlpha = ((1f - p2) * 0.55f).coerceIn(0f, 0.55f)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(bottomStart = 5.dp, bottomEnd = 5.dp))
                            .background(Color.Black.copy(alpha = shadowAlpha))
                    )
                }
            }

            // Left & Right Mechanical Hinge Clips on Card Edges
            if (hasLeftHinge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(width = 2.5.dp, height = 9.dp)
                        .background(Color(0xFF64748B), RoundedCornerShape(topEnd = 1.dp, bottomEnd = 1.dp))
                )
            }
            if (hasRightHinge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(width = 2.5.dp, height = 9.dp)
                        .background(Color(0xFF64748B), RoundedCornerShape(topStart = 1.dp, bottomStart = 1.dp))
                )
            }
        }
    }
}

/**
 * Renders either the top half or bottom half of the 2-digit number (e.g. "12", "00"):
 * The digits are mathematically split into two distinct half-tiles across the horizontal centerline:
 * - Top Half (isTopHalf = true): Only draws the upper half of the digit (top horizontal bar, upper verticals, top half of center bar).
 * - Bottom Half (isTopHalf = false): Only draws the lower half of the digit (bottom half of center bar, lower verticals, bottom horizontal bar).
 * When top and bottom half-tiles meet at the centerline seam, they form the complete 7-segment digit matching flipboard3.jpg.
 */
@Composable
private fun DigitHalf(
    digits: String,
    isTopHalf: Boolean,
    clockFont: FlipClockFont = FlipClockFont.MONTSERRAT_THIN,
    fontFamily: FontFamily = FontFamily.SansSerif,
    modifier: Modifier = Modifier
) {
    val halfHeight = 27.dp
    val shape = if (isTopHalf) {
        RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp, bottomStart = 1.dp, bottomEnd = 1.dp)
    } else {
        RoundedCornerShape(topStart = 1.dp, topEnd = 1.dp, bottomStart = 5.dp, bottomEnd = 5.dp)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(halfHeight)
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    if (isTopHalf) {
                        listOf(Color(0xFFFFFFFF), Color(0xFFF3F4F6))
                    } else {
                        listOf(Color(0xFFEEF1F4), Color(0xFFE2E8F0))
                    }
                )
            )
            .border(
                BorderStroke(0.7.dp, if (isTopHalf) Color(0xFFD1D5DB) else Color(0xFFCBD5E1)),
                shape
            ),
        contentAlignment = if (isTopHalf) Alignment.TopCenter else Alignment.BottomCenter
    ) {
        if (clockFont == FlipClockFont.RETRO_7SEGMENT) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val paddedDigits = digits.padStart(2, '0')
                val d1 = paddedDigits[0]
                val d2 = paddedDigits[1]

                val w = size.width
                val h = size.height

                // Precise spacing for 2 digits on the card matching flipboard3.jpg
                val digit1Center = w * 0.29f
                val digit2Center = w * 0.71f
                val digitWidth = w * 0.35f

                drawSegmentHalfDigit(
                    char = d1,
                    isTopHalf = isTopHalf,
                    centerX = digit1Center,
                    cardHeight = h,
                    digitWidth = digitWidth
                )
                drawSegmentHalfDigit(
                    char = d2,
                    isTopHalf = isTopHalf,
                    centerX = digit2Center,
                    cardHeight = h,
                    digitWidth = digitWidth
                )
            }
        } else {
            // High-precision split-flap font renderer:
            // Slices the 54.dp text container vertically across the 27.dp half-tile bounds:
            // Top half aligns to TopCenter (shows upper 50%), bottom half aligns to BottomCenter (shows lower 50%)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = digits.padStart(2, '0'),
                    style = TextStyle(
                        fontFamily = fontFamily,
                        fontWeight = clockFont.fontWeight,
                        fontSize = 28.sp,
                        lineHeight = 28.sp,
                        letterSpacing = (-0.5).sp,
                        color = Color(0xFF0F172A),
                        textAlign = TextAlign.Center
                    ),
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Mathematically renders only the upper or lower half of a 7-segment digit.
 * Perfectly divides segments along the horizontal split line:
 * - Segment A (top horizontal bar): top half only
 * - Segment F (upper-left vertical): top half only
 * - Segment B (upper-right vertical): top half only
 * - Slant serif for '1': top half only
 * - Segment G (center horizontal bar): upper half in top tile, lower half in bottom tile
 * - Segment E (lower-left vertical): bottom half only
 * - Segment C (lower-right vertical): bottom half only
 * - Segment D (bottom horizontal bar): bottom half only
 */
private fun DrawScope.drawSegmentHalfDigit(
    char: Char,
    isTopHalf: Boolean,
    centerX: Float,
    cardHeight: Float,
    digitWidth: Float,
    color: Color = Color(0xFF111827)
) {
    val d = char.digitToIntOrNull() ?: 0

    // Standard 7-segment display logic
    val segA = d in listOf(0, 2, 3, 5, 6, 7, 8, 9)
    val segB = d in listOf(0, 1, 2, 3, 4, 7, 8, 9)
    val segC = d in listOf(0, 1, 3, 4, 5, 6, 7, 8, 9)
    val segD = d in listOf(0, 2, 3, 5, 6, 8, 9)
    val segE = d in listOf(0, 2, 6, 8)
    val segF = d in listOf(0, 4, 5, 6, 8, 9)
    val segG = d in listOf(2, 3, 4, 5, 6, 8, 9)
    val isOne = d == 1

    // Proportional dimensions
    val fullDigitH = cardHeight * 1.34f
    val halfDigitH = fullDigitH / 2f
    val t = (digitWidth * 0.19f).coerceAtLeast(3.2.dp.toPx())
    val cornerR = CornerRadius(t * 0.35f, t * 0.35f)
    val gap = (t * 0.35f).coerceAtLeast(1.2.dp.toPx())
    val halfT = t / 2f

    // For digit '1', align stem cleanly with slant top serif
    val left = if (isOne) centerX - t * 0.5f else centerX - digitWidth / 2f
    val right = if (isOne) centerX + t * 0.5f + t else centerX + digitWidth / 2f
    val effectiveW = right - left

    if (isTopHalf) {
        // TOP HALF:
        // Midline (seam) is at local Y = cardHeight
        // Top of the digit is at topY
        val topY = cardHeight - halfDigitH

        // Segment A: Top horizontal bar
        if (segA && !isOne) {
            drawRoundRect(
                color = color,
                topLeft = Offset(left + t * 0.7f, topY),
                size = Size((effectiveW - t * 1.4f).coerceAtLeast(1f), t),
                cornerRadius = cornerR
            )
        }

        // Segment F: Upper-left vertical bar
        if (segF && !isOne) {
            val h = (cardHeight - halfT - gap) - (topY + t + gap)
            if (h > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, topY + t + gap),
                    size = Size(t, h),
                    cornerRadius = cornerR
                )
            }
        }

        // Segment B: Upper-right vertical bar
        if (segB) {
            val h = (cardHeight - halfT - gap) - (topY + t + gap)
            if (h > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(right - t, topY + t + gap),
                    size = Size(t, h),
                    cornerRadius = cornerR
                )
            }
        }

        // Distinct top slant serif for digit '1' matching flipboard3.jpg
        if (isOne) {
            val serifW = t * 1.3f
            val path = Path().apply {
                moveTo(right - t, topY + t * 1.4f)
                lineTo(right - t - serifW, topY + t * 0.5f)
                lineTo(right - t - serifW + t * 0.5f, topY)
                lineTo(right - t, topY + t * 0.6f)
                close()
            }
            drawPath(path = path, color = color)
        }

        // Segment G (Upper half of center horizontal bar):
        // Positioned right against the bottom edge (seam) of the top tile
        if (segG && !isOne) {
            drawRoundRect(
                color = color,
                topLeft = Offset(left + t * 0.7f, cardHeight - halfT),
                size = Size((effectiveW - t * 1.4f).coerceAtLeast(1f), halfT),
                cornerRadius = CornerRadius(cornerR.x, 0f) // rounded top, flat at the seam
            )
        }
    } else {
        // BOTTOM HALF:
        // Midline (seam) is at local Y = 0
        // Bottom of the digit is at bottomY = halfDigitH
        val bottomY = halfDigitH

        // Segment G (Lower half of center horizontal bar):
        // Positioned right against the top edge (seam) of the bottom tile
        if (segG && !isOne) {
            drawRoundRect(
                color = color,
                topLeft = Offset(left + t * 0.7f, 0f),
                size = Size((effectiveW - t * 1.4f).coerceAtLeast(1f), halfT),
                cornerRadius = CornerRadius(0f, cornerR.x) // flat at seam, rounded bottom
            )
        }

        // Segment E: Lower-left vertical bar
        if (segE && !isOne) {
            val h = (bottomY - t - gap) - (halfT + gap)
            if (h > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, halfT + gap),
                    size = Size(t, h),
                    cornerRadius = cornerR
                )
            }
        }

        // Segment C: Lower-right vertical bar
        if (segC) {
            val h = (bottomY - t - gap) - (halfT + gap)
            if (h > 0) {
                drawRoundRect(
                    color = color,
                    topLeft = Offset(right - t, halfT + gap),
                    size = Size(t, h),
                    cornerRadius = cornerR
                )
            }
        }

        // Segment D: Bottom horizontal bar
        if (segD && !isOne) {
            drawRoundRect(
                color = color,
                topLeft = Offset(left + t * 0.7f, bottomY - t),
                size = Size((effectiveW - t * 1.4f).coerceAtLeast(1f), t),
                cornerRadius = cornerR
            )
        }
    }
}

/**
 * Center vertical post between the two flip card units matching flipboard3.jpg:
 * Features a clean white column with two circular metallic colon rivets/screws (`:`) and axle hinge brackets.
 */
@Composable
internal fun FlipClockCenterPost(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(horizontal = 1.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left hinge bracket connector
        Box(
            modifier = Modifier
                .size(width = 2.dp, height = 12.dp)
                .background(Color(0xFFCBD5E1))
        )
        // Center white column post with two colon rivet dots
        Box(
            modifier = Modifier
                .size(width = 11.dp, height = 36.dp)
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(3.dp))
                .clip(RoundedCornerShape(3.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFFFFFFFF), Color(0xFFF1F5F9))
                    )
                )
                .border(BorderStroke(0.8.dp, Color(0xFFCBD5E1)), RoundedCornerShape(3.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxHeight().padding(vertical = 6.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top colon screw/dot
                Box(
                    modifier = Modifier
                        .size(4.5.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF94A3B8), Color(0xFF475569))
                            )
                        )
                )
                // Bottom colon screw/dot
                Box(
                    modifier = Modifier
                        .size(4.5.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(Color(0xFF94A3B8), Color(0xFF475569))
                            )
                        )
                )
            }
        }
        // Right hinge bracket connector
        Box(
            modifier = Modifier
                .size(width = 2.dp, height = 12.dp)
                .background(Color(0xFFCBD5E1))
        )
    }
}

/**
 * Central cylindrical knurled roller gear (as seen in the flip clock video between the two tiles)
 */
@Composable
private fun FlipClockCenterRoller(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 2.dp)
            .size(width = 10.dp, height = 40.dp)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(3.dp))
            .clip(RoundedCornerShape(3.dp))
            .background(
                Brush.horizontalGradient(
                    listOf(
                        Color(0xFF1E2229),
                        Color(0xFF333945),
                        Color(0xFF475060),
                        Color(0xFF333945),
                        Color(0xFF1E2229)
                    )
                )
            )
            .border(BorderStroke(0.6.dp, Color(0xFF475569)), RoundedCornerShape(3.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Realistic knurled horizontal ridges/teeth
        Column(
            modifier = Modifier.fillMaxSize().padding(vertical = 3.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            repeat(7) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .height(1.dp)
                        .background(Color(0xFF64748B))
                )
            }
        }
        // Center horizontal split notch
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.5.dp)
                .background(Color(0xFF0F172A))
        )
    }
}

/**
 * Corner Rivet Dot for Mechanical Split Flap Board Frame
 */
@Composable
private fun CornerRivet(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(3.dp)
            .background(Color(0xFF94A3B8), CircleShape)
    )
}

/**
 * Side Metal Pivot Clip for Split Flap Board Frame
 */
@Composable
private fun SideHingeClip(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 3.dp, height = 12.dp)
            .background(Color(0xFFCBD5E1), RoundedCornerShape(1.dp))
    )
}
