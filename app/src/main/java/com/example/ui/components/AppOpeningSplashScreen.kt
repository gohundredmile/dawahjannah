package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicIvory
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import kotlinx.coroutines.delay

/**
 * App Opening Splash / Welcome Screen:
 * Displays the magnificent grand mosque dome photo full screen,
 * prominently featuring the App Name in the middle of the screen:
 * - Bengali: "দা'ওয়াহ টু জান্নাহ"
 * - English: "Da'wah To Jannah" in authentic Calligraphy style
 * - Arabic: "دَعْوَةٌ إِلَى الجَنَّة"
 * Smoothly transitions to dashboard after 3.0s or instantly upon tap anywhere.
 */
@Composable
fun AppOpeningSplashScreen(
    onFinish: () -> Unit
) {
    // Auto transition to main dashboard after 3.0 seconds
    LaunchedEffect(Unit) {
        delay(3000)
        onFinish()
    }

    val banglaFamily = LocalBanglaFontFamily.current
    val arabicFamily = LocalArabicFontFamily.current

    // Subtle breathing pulsation for the enter hint
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onFinish()
            }
    ) {
        // 1. Full-screen Grand Mosque Dome Ceiling Photo
        Image(
            painter = painterResource(id = R.drawable.img_splash_cover),
            contentDescription = "দা'ওয়াহ টু জান্নাহ - Grand Mosque Dome",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Soft Ambient Celestial Vignette for depth and text contrast
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0.0f to Color(0x33060C14),
                        0.30f to Color.Transparent,
                        0.60f to Color(0x22060C14),
                        1.0f to Color(0xD9040810)
                    )
                )
        )

        // 3. MIDDLE OF THE SCREEN: App Name in Calligraphy Style
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(32.dp),
                        ambientColor = Color.Black,
                        spotColor = IslamicGold.copy(alpha = 0.55f)
                    ),
                shape = RoundedCornerShape(32.dp),
                color = Color(0x9E081220), // Translucent royal midnight glass
                border = BorderStroke(
                    1.5.dp,
                    Brush.linearGradient(
                        colors = listOf(
                            IslamicGold.copy(alpha = 0.90f),
                            Color(0xFFFFF2A0),
                            Color(0xFFECC25A),
                            IslamicGold.copy(alpha = 0.70f)
                        )
                    )
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    // Authentic Mosque Dome Calligraphy Medallion Emblem
                    Image(
                        painter = painterResource(id = R.drawable.ic_logo_medallion),
                        contentDescription = "দা\'ওয়াহ টু জান্নাহ Emblem",
                        modifier = Modifier
                            .size(72.dp)
                            .shadow(
                                elevation = 12.dp,
                                shape = CircleShape,
                                spotColor = IslamicGold.copy(alpha = 0.6f)
                            )
                            .clip(CircleShape)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Golden Bismillah Header with authentic Arabic font
                    Text(
                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                        fontFamily = arabicFamily,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF7D982),
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Arabic Calligraphy: Dawah Ila Al-Jannah
                    Text(
                        text = "دَعْوَةٌ إِلَى الجَنَّة",
                        fontFamily = arabicFamily,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFE28A),
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Bengali App Name (Prominent & Majestic in the Middle)
                    Text(
                        text = "দা'ওয়াহ টু জান্নাহ",
                        fontFamily = banglaFamily,
                        fontWeight = FontWeight.Black,
                        fontSize = 34.sp,
                        letterSpacing = 0.5.sp,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.9f),
                                offset = Offset(2f, 3f),
                                blurRadius = 10f
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Ornamental Calligraphy Divider with Star Medallion
                    Row(
                        modifier = Modifier.fillMaxWidth(0.80f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.5.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color.Transparent, Color(0xFFECC25A))
                                    )
                                )
                        )
                        Text(
                            text = " ۞ ",
                            color = Color(0xFFFFF0A0),
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 6.dp)
                        )
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(1.5.dp)
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFECC25A), Color.Transparent)
                                    )
                                )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // English App Name in Authentic Calligraphy Style (Serif Italic, Spaced Tracking, Golden Radiance)
                    Text(
                        text = "Da'wah To Jannah",
                        fontFamily = FontFamily.Serif,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        letterSpacing = 2.5.sp,
                        color = Color(0xFFFFE082),
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.75f),
                                offset = Offset(1.5f, 2.5f),
                                blurRadius = 8f
                            )
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Spiritual Subtitle
                    Text(
                        text = "আখিরাতের পথে আত্মিক অভিযাত্রা",
                        fontFamily = banglaFamily,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = IslamicIvory.copy(alpha = 0.92f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // 4. Bottom Enter Affordance
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color.Black.copy(alpha = 0.55f),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f)),
                modifier = Modifier.alpha(pulseAlpha)
            ) {
                Text(
                    text = "প্রবেশ করতে স্পর্শ করুন • Tap to enter",
                    fontFamily = banglaFamily,
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 12.sp,
                    color = Color.White.copy(alpha = 0.95f),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                )
            }
        }
    }
}
