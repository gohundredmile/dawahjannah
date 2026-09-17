package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.CalendarHelper
import java.util.Locale

/**
 * Revamped 3-Section Salat Timing Card
 * Section 1 (Left): 'Next Salat' (পরবর্তী সালাত)
 * Section 2 (Middle): 'Present Salat' (বর্তমান সালাত) split-flap board showing Present Wakt and Nofol Salat info
 * Section 3 (Right): 'Remaining Time' (শেষ হতে বাকী) retro flip clock board
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
    var flipTrigger by remember { mutableStateOf(0) }

    val flipAngle by animateFloatAsState(
        targetValue = if (flipTrigger % 2 == 0) 0f else 360f,
        animationSpec = tween(durationMillis = 400),
        label = "boardFlip"
    )

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
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .weight(0.72f)
                .padding(end = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "পরবর্তী সালাত",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                fontFamily = LocalBanglaFontFamily.current,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = nextPrayerName,
                style = MaterialTheme.typography.headlineMedium.copy(fontSize = 23.sp),
                fontFamily = LocalBanglaFontFamily.current,
                fontWeight = FontWeight.Black,
                color = Color(0xFF0F172A),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // -------------------------------------------------------------
        // VERTICAL DASHED DIVIDER
        // -------------------------------------------------------------
        Canvas(
            modifier = Modifier
                .width(1.dp)
                .height(60.dp)
                .padding(horizontal = 1.dp)
        ) {
            drawLine(
                color = Color(0xFFD1D5DB),
                start = Offset(0f, 0f),
                end = Offset(0f, size.height),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            )
        }

        // -------------------------------------------------------------
        // 2. MIDDLE SECTION: বর্তমান সালাত (Present Salat & Nofol Flip Board)
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1.08f)
                .padding(horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "বর্তমান সালাত",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                fontFamily = LocalBanglaFontFamily.current,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Flipping Board Outer Casing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .graphicsLayer {
                        rotationX = if (flipAngle in 90f..270f) 180f - flipAngle else flipAngle
                    }
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Color(0xFF2C323B),
                                Color(0xFF1E2228),
                                Color(0xFF15181C)
                            )
                        )
                    )
                    .border(BorderStroke(1.dp, Color(0xFF4B5563)), RoundedCornerShape(8.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        flipTrigger++
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
                        // Upper Flap: Present Wakt Salat Name (e.g. এশা / যোহর / ফজর)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = presentPrayerName.ifEmpty { "ওয়াক্ত" },
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                fontFamily = LocalBanglaFontFamily.current,
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

                        // Lower Flap: Nofol Salat Information (e.g. তাহাজ্জুদ / আওয়াবিন / চাশত)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = presentNofolName.ifEmpty { "নফল ইবাদত" },
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 13.sp),
                                fontFamily = LocalBanglaFontFamily.current,
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
        // 3. RIGHT SECTION: শেষ হতে বাকী (Wider Flip Clock Board)
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .weight(1.40f)
                .padding(start = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "শেষ হতে বাকী",
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.5.sp),
                fontFamily = LocalBanglaFontFamily.current,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Compute digits for flip clock
            val isHoursDisplay = remainingHours > 0
            val leftRaw = if (isHoursDisplay) remainingHours else remainingMinutes
            val rightRaw = if (isHoursDisplay) remainingMinutes else remainingSeconds

            val leftStr = String.format(Locale.US, "%02d", leftRaw)
            val rightStr = String.format(Locale.US, "%02d", rightRaw)

            val leftDisplay = if (useEnglishDigits) leftStr else CalendarHelper.toBanglaNumber(leftStr)
            val rightDisplay = if (useEnglishDigits) rightStr else CalendarHelper.toBanglaNumber(rightStr)

            // Retro Mechanical Flip Clock Board as per Specimen
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        useEnglishDigits = !useEnglishDigits
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Left Flip Tile (Hours or Minutes)
                FlipDigitCard(
                    digits = leftDisplay,
                    hasLeftHinge = true,
                    hasRightHinge = true
                )

                // Central Vertical Colon Bracket with Two Pivot Screws
                FlipClockCenterBracket()

                // Right Flip Tile (Minutes or Seconds)
                FlipDigitCard(
                    digits = rightDisplay,
                    hasLeftHinge = true,
                    hasRightHinge = true
                )
            }
        }
    }
}

/**
 * Split Flap Digit Card for Flip Clock (Wide 2-Digit Tile matching flipboard3.jpg)
 */
@Composable
private fun FlipDigitCard(
    digits: String,
    hasLeftHinge: Boolean = true,
    hasRightHinge: Boolean = true,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 1.5.dp),
        contentAlignment = Alignment.Center
    ) {
        // Multi-card Stack Layer Effect at the Bottom (representing stacked flap deck)
        Column(
            modifier = Modifier
                .width(49.dp)
                .height(57.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            // Under-layer 2
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(1.dp)
                    .background(Color(0xFFD1D5DB), RoundedCornerShape(bottomStart = 2.dp, bottomEnd = 2.dp))
            )
            // Under-layer 1
            Box(
                modifier = Modifier
                    .width(46.5.dp)
                    .height(1.dp)
                    .background(Color(0xFFE2E8F0), RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
            )
        }

        // Main Flap Card Faceplate
        Box(
            modifier = Modifier
                .size(width = 49.dp, height = 55.dp)
                .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFFFFF),
                            Color(0xFFF9FAFB),
                            Color(0xFFF3F4F6)
                        )
                    )
                )
                .border(BorderStroke(1.dp, Color(0xFFD1D5DB)), RoundedCornerShape(6.dp))
        ) {
            // Upper and Lower Flap Shading Separation with Center Split Line
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color.White.copy(alpha = 0.6f))
                )
                // Center Horizontal Split Groove Line
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFF4B5563))
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .background(Color(0xFFE5E7EB).copy(alpha = 0.35f))
                )
            }

            // Left & Right Mechanical Hinge Clips on Card Edges
            if (hasLeftHinge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(width = 2.5.dp, height = 11.dp)
                        .background(Color(0xFF6B7280), RoundedCornerShape(topEnd = 1.dp, bottomEnd = 1.dp))
                )
            }
            if (hasRightHinge) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(width = 2.5.dp, height = 11.dp)
                        .background(Color(0xFF6B7280), RoundedCornerShape(topStart = 1.dp, bottomStart = 1.dp))
                )
            }

            // Two Digits Text prominently and comfortably centered
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = digits,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = 21.sp,
                        letterSpacing = 0.5.sp
                    ),
                    fontFamily = LocalBanglaFontFamily.current,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF111827),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Mechanical Flip Clock Central Colon Bracket (matching flipboard3.jpg)
 */
@Composable
private fun FlipClockCenterBracket(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(horizontal = 1.5.dp)
            .size(width = 9.dp, height = 38.dp)
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(2.dp))
            .clip(RoundedCornerShape(2.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF3F4F6)
                    )
                )
            )
            .border(BorderStroke(0.8.dp, Color(0xFFD1D5DB)), RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Upper Colon Pivot Screw Dot
            Box(
                modifier = Modifier
                    .size(4.5.dp)
                    .background(Color(0xFF4B5563), CircleShape)
            )
            Spacer(modifier = Modifier.height(7.dp))
            // Lower Colon Pivot Screw Dot
            Box(
                modifier = Modifier
                    .size(4.5.dp)
                    .background(Color(0xFF4B5563), CircleShape)
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
