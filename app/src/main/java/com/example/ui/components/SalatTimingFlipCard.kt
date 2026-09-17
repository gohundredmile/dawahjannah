package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalAppFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.theme.LocalEnglishFontFamily
import com.example.util.CalendarHelper
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Locale

/**
 * Data item for continuous informative rotation in Present Salat Split-Flap Board
 */
data class SalatContextInfo(
    val timeLabel: String,
    val salatInfo: String
)

/**
 * Revamped 3-Section Salat Timing Card
 * Section 1 (Left): 'Next Salat' (পরবর্তী সালাত) - Clean view without vertical dashed divider
 * Section 2 (Middle): 'Present Salat' (বর্তমান সালাত) - Wider Split-flap board with continuous 3s info rotation
 * Section 3 (Right): 'Remaining Time' (শেষ হতে বাকী) - Authentic HTC phone split-flap clock with Typography Studio font
 */
@Composable
fun SalatTimingFlipCard(
    nextPrayerName: String,
    presentPrayerName: String,
    presentNofolName: String,
    remainingHours: Int,
    remainingMinutes: Int,
    remainingSeconds: Int,
    countdownFormatted: String = "",
    modifier: Modifier = Modifier,
    onClickCard: () -> Unit = {}
) {
    var useEnglishDigits by remember { mutableStateOf(false) }
    // false = [মিনিট : সেকেন্ড] mode (ticks and flips every second), true = [ঘণ্টা : মিনিট] mode
    var showHoursMode by remember { mutableStateOf(false) }

    val banglaFont = LocalBanglaFontFamily.current
    val englishFont = LocalEnglishFontFamily.current
    val appFont = LocalAppFontFamily.current

    // Font selected from Typography Studio
    val activeTileFont = if (useEnglishDigits) {
        if (englishFont != FontFamily.Default) englishFont else appFont
    } else {
        if (banglaFont != FontFamily.Default) banglaFont else appFont
    }

    // -----------------------------------------------------------------
    // Contextual Nofol & Salat information list based on authentic timings
    // -----------------------------------------------------------------
    val infoList = remember(presentPrayerName, presentNofolName) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val totalMins = hour * 60 + minute

        val items = mutableListOf<SalatContextInfo>()

        // 1. Current Wakt & Nofol as primary base pair
        val waktTitle = if (presentPrayerName.isNotEmpty()) "বর্তমান ওয়াক্ত: $presentPrayerName" else "বর্তমান ওয়াক্ত"
        val nofolTitle = if (presentNofolName.isNotEmpty()) "$presentNofolName এর সময়" else "নফল ইবাদত"
        items.add(SalatContextInfo(waktTitle, nofolTitle))

        // 2. Add time-appropriate authentic Sunnah & Nofol salat entries
        when {
            // Fajr period (approx 4:00 - 5:45)
            totalMins in 240..345 -> {
                items.add(SalatContextInfo("ভোরবেলা", "ফজরের সুন্নাত ও জামাত"))
                items.add(SalatContextInfo("শুভ প্রভাত", "তাহিয়্যাতুল ওজু ও জিকির"))
            }
            // Sunrise forbidden window (approx 5:45 - 6:05)
            totalMins in 346..370 -> {
                items.add(SalatContextInfo("সূর্যোদয়ের সময়", "সালাত নিষিদ্ধ (১৫ মিনিট অপেক্ষা)"))
                items.add(SalatContextInfo("সূর্যোদয় প্রহর", "জিকির ও ইস্তিগফারের সর্বোত্তম সময়"))
            }
            // Morning Ishraq & Chasht / Salatud-Duha (approx 6:05 - 11:30)
            totalMins in 371..690 -> {
                items.add(SalatContextInfo("স্নিগ্ধ সকাল", "ইশরাকের নামাজ বা চাশতের সময়"))
                items.add(SalatContextInfo("স্নিগ্ধ সকাল", "সালাতুদ-দুহা র সময়"))
                items.add(SalatContextInfo("চাশতের নামায", "বরকতময় নফল ইবাদত (মুসলিম ৭৪৮)"))
            }
            // Midday Zawal forbidden window (approx 11:35 - 11:55)
            totalMins in 691..725 -> {
                items.add(SalatContextInfo("দুপুর বেলা", "সালাত জাওয়াল শুরু"))
                items.add(SalatContextInfo("দ্বিপ্রহর", "সূর্য মধ্যাকাশে থাকায় সালাত মাকরূহ"))
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
                items.add(SalatContextInfo("মাগরিবের পর", "২ রাকাত নিয়মিত সুন্নাত মুয়াক্কাদা"))
            }
            // Isha and Night / Tahajjud (approx 19:30 - 24:00 and 00:00 - 04:00)
            else -> {
                items.add(SalatContextInfo("শুভ রাত্রি", "একান্তে আল্লাহর সান্নিধ্যে আসুন"))
                items.add(SalatContextInfo("তাহাজ্জুদের সময়", "রাতের শ্রেষ্ঠ নফল ইবাদত (মুসলিম ৭৫৮)"))
                items.add(SalatContextInfo("রাতের শেষ প্রহর", "তাহাজ্জুদ ও দোয়া কবুলের শ্রেষ্ঠ সময়"))
                items.add(SalatContextInfo("বিতর সালাত", "রাতের শেষ সালাত হিসাবে বিতর আদায়"))
            }
        }

        items
    }

    var currentInfoIndex by remember { mutableIntStateOf(0) }
    val boardFlipProgress = remember { Animatable(1f) }

    // Continuous 3-second cycle for the middle split-flap board
    LaunchedEffect(infoList.size) {
        while (true) {
            delay(3000L)
            boardFlipProgress.snapTo(0f)
            val nextIdx = (currentInfoIndex + 1) % infoList.size
            currentInfoIndex = nextIdx
            boardFlipProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 480, easing = FastOutSlowInEasing)
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
                        // Upper Flap: Time Label (e.g. স্নিগ্ধ সকাল / শুভ রাত্রি / বর্তমান ওয়াক্ত: এশা)
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
                                    fontSize = 13.5.sp,
                                    letterSpacing = 0.2.sp
                                ),
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF0F172A),
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

                        // Lower Flap: Salat / Nofol Info (e.g. সালাতুদ-দুহা র সময় / তাহাজ্জুদের সময় / আওয়াবিন এর সময়)
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
                                    fontSize = 12.5.sp,
                                    letterSpacing = 0.1.sp
                                ),
                                fontFamily = banglaFont,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E293B),
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
        // Uses selected font from Typography Studio, larger bold size,
        // and 3D split-flap downward animation on every second/minute/hour change
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1.28f)
                .padding(start = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header: Title with optional hour remaining reminder if in MM:SS mode
            val hourBadgeText = if (remainingHours > 0 && !showHoursMode) {
                if (useEnglishDigits) "${remainingHours}h left" else "${CalendarHelper.toBanglaNumber(remainingHours)}ঘণ্টা বাকী"
            } else {
                "শেষ হতে বাকী"
            }

            Text(
                text = hourBadgeText,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                fontFamily = banglaFont,
                fontWeight = FontWeight.SemiBold,
                color = if (remainingHours > 0 && !showHoursMode) Color(0xFFB45309) else Color(0xFF374151),
                textAlign = TextAlign.Center,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(3.dp))

            // Compute digits for left and right tiles
            // In default MM:SS mode: left is minutes, right is seconds (ticks and flips every single second!)
            // In HH:MM mode: left is hours, right is minutes
            val leftRaw = if (showHoursMode) remainingHours else remainingMinutes
            val rightRaw = if (showHoursMode) remainingMinutes else remainingSeconds

            val leftStr = String.format(Locale.US, "%02d", leftRaw)
            val rightStr = String.format(Locale.US, "%02d", rightRaw)

            val leftDisplay = if (useEnglishDigits) leftStr else CalendarHelper.toBanglaNumber(leftStr)
            val rightDisplay = if (useEnglishDigits) rightStr else CalendarHelper.toBanglaNumber(rightStr)

            // Retro Mechanical HTC-style Flip Clock Board
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        // Tapping the board toggles between MM:SS mode and HH:MM mode,
                        // or long click / double tap toggles digits. We toggle mode on tap!
                        if (remainingHours > 0) {
                            showHoursMode = !showHoursMode
                        } else {
                            useEnglishDigits = !useEnglishDigits
                        }
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Left HTC Flip Tile (Minutes or Hours)
                HtcFlipDigitCard(
                    digits = leftDisplay,
                    fontFamily = activeTileFont,
                    hasLeftHinge = true,
                    hasRightHinge = true
                )

                // Central Vertical Colon Bracket with Two Pivot Screws
                FlipClockCenterBracket()

                // Right HTC Flip Tile (Seconds or Minutes) - live flips every second!
                HtcFlipDigitCard(
                    digits = rightDisplay,
                    fontFamily = activeTileFont,
                    hasLeftHinge = true,
                    hasRightHinge = true
                )
            }

            // Sub-label indicating unit (মি : সে or ঘণ্টা : মি)
            val unitLabel = if (showHoursMode) {
                if (useEnglishDigits) "hr : min" else "ঘণ্টা : মিনিট"
            } else {
                if (useEnglishDigits) "min : sec" else "মিনিট : সেকেন্ড"
            }
            Text(
                text = unitLabel,
                style = TextStyle(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF64748B),
                    fontFamily = banglaFont,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

/**
 * Authentic HTC Phone Sense 3D Flip Clock Digit Tile
 * Features 3D top-down split flap flip animation whenever [digits] changes
 * Aligned with Typography Studio active font, bold tabular typeface
 */
@Composable
private fun HtcFlipDigitCard(
    digits: String,
    fontFamily: FontFamily,
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
                animationSpec = tween(durationMillis = 440, easing = FastOutSlowInEasing)
            )
        }
    }

    Box(
        modifier = modifier.padding(horizontal = 1.dp),
        contentAlignment = Alignment.Center
    ) {
        // Multi-card Stack Layer Effect at the Bottom (representing stacked flap deck)
        Column(
            modifier = Modifier
                .width(48.dp)
                .height(58.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Under-layer 2
            Box(
                modifier = Modifier
                    .width(42.dp)
                    .height(1.dp)
                    .background(Color(0xFFD1D5DB), RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
            )
            // Under-layer 1
            Box(
                modifier = Modifier
                    .width(45.dp)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
            )
        }

        // Main Flap Card Assembly
        Box(
            modifier = Modifier
                .size(width = 48.dp, height = 55.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .border(BorderStroke(1.dp, Color(0xFFCBD5E1)), RoundedCornerShape(6.dp))
        ) {
            // BASE LAYER:
            // Top half displays the NEW digit (revealed when old flap falls down)
            // Bottom half displays OLD digit during phase 1 (<0.5), then switches to NEW digit
            val bottomBaseDigits = if (flipProgress.value < 0.5f) previousDigits else displayedDigits

            Column(modifier = Modifier.fillMaxSize()) {
                DigitHalf(digits = displayedDigits, isTopHalf = true, fontFamily = fontFamily)
                // Center Horizontal Split Groove
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF334155))
                )
                DigitHalf(digits = bottomBaseDigits, isTopHalf = false, fontFamily = fontFamily)
            }

            // FLIPPING LAYER (HTC Sense 3D Downward Flap):
            if (flipProgress.value < 0.5f) {
                // Phase 1 (0.0 .. 0.5): The old top flap flips DOWNWARDS from 0° to -90°
                val rotX = -180f * flipProgress.value
                Box(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .height(27.dp)
                        .graphicsLayer {
                            rotationX = rotX
                            cameraDistance = 18f * density
                            transformOrigin = TransformOrigin(0.5f, 1.0f) // Pivot at bottom edge of top half
                        }
                ) {
                    DigitHalf(digits = previousDigits, isTopHalf = true, fontFamily = fontFamily)
                    // Dynamic shadow overlay during downward fold
                    val shadowAlpha = (flipProgress.value * 0.9f).coerceIn(0f, 0.48f)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = shadowAlpha))
                    )
                }
            } else if (flipProgress.value < 1.0f) {
                // Phase 2 (0.5 .. 1.0): The new bottom flap lands from +90° to 0°
                val rotX = 90f - 180f * (flipProgress.value - 0.5f)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(27.dp)
                        .graphicsLayer {
                            rotationX = rotX
                            cameraDistance = 18f * density
                            transformOrigin = TransformOrigin(0.5f, 0.0f) // Pivot at top edge of bottom half
                        }
                ) {
                    DigitHalf(digits = displayedDigits, isTopHalf = false, fontFamily = fontFamily)
                    // Dynamic shadow fading away as card lands flat
                    val shadowAlpha = ((1f - flipProgress.value) * 0.9f).coerceIn(0f, 0.48f)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = shadowAlpha))
                    )
                }
            }

            // Left & Right Mechanical Hinge Clips on Card Edges (HTC phone look)
            if (hasLeftHinge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(width = 2.5.dp, height = 10.dp)
                        .background(Color(0xFF64748B), RoundedCornerShape(topEnd = 1.dp, bottomEnd = 1.dp))
                )
            }
            if (hasRightHinge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(width = 2.5.dp, height = 10.dp)
                        .background(Color(0xFF64748B), RoundedCornerShape(topStart = 1.dp, bottomStart = 1.dp))
                )
            }
        }
    }
}

/**
 * Renders either the top half or bottom half of the 2-digit number
 * Uses Typography Studio active font, bold and large sizing
 */
@Composable
private fun DigitHalf(
    digits: String,
    isTopHalf: Boolean,
    fontFamily: FontFamily,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(27.dp)
            .clipToBounds()
            .background(
                Brush.verticalGradient(
                    if (isTopHalf) {
                        listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC))
                    } else {
                        listOf(Color(0xFFF1F5F9), Color(0xFFE2E8F0))
                    }
                )
            ),
        contentAlignment = if (isTopHalf) Alignment.TopCenter else Alignment.BottomCenter
    ) {
        // Full height text container precisely cropped to top or bottom half
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .offset(y = if (isTopHalf) 0.dp else (-27).dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = digits,
                style = TextStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 26.sp,                  // Bigger, prominent font
                    lineHeight = 26.sp,
                    letterSpacing = 0.5.sp,
                    color = Color(0xFF0F172A),
                    textAlign = TextAlign.Center
                ),
                maxLines = 1
            )
        }
    }
}

/**
 * Mechanical Flip Clock Central Colon Bracket (matching flipboard specimen)
 */
@Composable
private fun FlipClockCenterBracket(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 1.dp)
            .size(width = 8.dp, height = 36.dp)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(2.dp))
            .clip(RoundedCornerShape(2.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF1F5F9)
                    )
                )
            )
            .border(BorderStroke(0.8.dp, Color(0xFFCBD5E1)), RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Upper Colon Pivot Screw Dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(Color(0xFF475569), CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            // Lower Colon Pivot Screw Dot
            Box(
                modifier = Modifier
                    .size(4.dp)
                    .background(Color(0xFF475569), CircleShape)
            )
        }
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
