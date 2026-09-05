package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.DailyWisdomState
import com.example.data.model.HadithWisdomItem
import com.example.data.model.QuoteWisdomItem
import com.example.data.model.QuranWisdomItem
import kotlinx.coroutines.delay

@Composable
fun DailyWisdomSection(
    wisdomState: DailyWisdomState,
    onShuffle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Pulsing animation for "Live Connected" indicator dot
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_trans")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Rotation animation for Shuffle icon
    var isRotating by remember { mutableStateOf(false) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(wisdomState.isLoading) {
        if (wisdomState.isLoading) {
            isRotating = true
            rotationAngle += 360f
        } else {
            delay(350)
            isRotating = false
        }
    }

    val animatedRotation by animateFloatAsState(
        targetValue = rotationAngle,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "shuffle_rotation"
    )

    // Staggered vertical entrance animation for the three cards
    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationStarted = true
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .testTag("daily_wisdom_section")
    ) {
        // -------------------------------------------------------------
        // COMPACT HEADER ROW: "Daily Light & Inspiration" + "Suffle Wisdom"
        // -------------------------------------------------------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Title with subtle sparkle
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f, fill = false)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Daily Light & Inspiration",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-0.2).sp
                )
            }

            // Right Action: Only text button 'Suffle Wisdom'
            TextButton(
                onClick = {
                    rotationAngle += 360f
                    onShuffle()
                },
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier.testTag("shuffle_wisdom_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Suffle Wisdom",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(15.dp)
                        .rotate(animatedRotation)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Suffle Wisdom",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        // -------------------------------------------------------------
        // THREE CARDS ARRANGED VERTICALLY WITH SUPERB ANIMATION
        // -------------------------------------------------------------
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // CARD 1: HOLY AL-QURAN
            AnimatedWisdomContainer(
                index = 0,
                isLoaded = animationStarted
            ) {
                AnimatedContent(
                    targetState = wisdomState.quran,
                    transitionSpec = {
                        (fadeIn(tween(350))).togetherWith(fadeOut(tween(200)))
                    },
                    label = "quran_crossfade"
                ) { quran ->
                    HolyQuranWisdomCard(
                        quran = quran,
                        onCopy = {
                            val text = """
                                *আল-কুরআনুল কারীম*
                                ${quran.arabicText}

                                অর্থ: ${quran.bengaliText}
                                ${quran.englishText}

                                [${quran.surahAyahFormatted}, ${quran.ayahFormatted}]
                            """.trimIndent()
                            copyToClipboard(context, "Holy Quran Verse", text, "কুরআনের আয়াত কপি করা হয়েছে")
                        }
                    )
                }
            }

            // CARD 2: HADITH WISDOM
            AnimatedWisdomContainer(
                index = 1,
                isLoaded = animationStarted
            ) {
                AnimatedContent(
                    targetState = wisdomState.hadith,
                    transitionSpec = {
                        (fadeIn(tween(350))).togetherWith(fadeOut(tween(200)))
                    },
                    label = "hadith_crossfade"
                ) { hadith ->
                    HadithWisdomCard(
                        hadith = hadith,
                        onCopy = {
                            val text = """
                                *হাদিসের আলো (${hadith.sourceName})*
                                ${hadith.bengaliText}

                                ${hadith.englishRef}
                                সূত্র: ${hadith.sourceName} | ${hadith.narratorOrNumber}
                            """.trimIndent()
                            copyToClipboard(context, "Hadith Wisdom", text, "হাদিস কপি করা হয়েছে")
                        }
                    )
                }
            }

            // CARD 3: INSPIRATIONAL QUOTES
            AnimatedWisdomContainer(
                index = 2,
                isLoaded = animationStarted
            ) {
                AnimatedContent(
                    targetState = wisdomState.quote,
                    transitionSpec = {
                        (fadeIn(tween(350))).togetherWith(fadeOut(tween(200)))
                    },
                    label = "quote_crossfade"
                ) { quote ->
                    InspirationalQuoteCard(
                        quote = quote,
                        onCopy = {
                            val text = """
                                *অনুপ্রেরণাদায়ী উক্তি*
                                "${quote.quoteBn}"

                                ${quote.quoteEn}
                                — ${quote.author}
                            """.trimIndent()
                            copyToClipboard(context, "Inspirational Quote", text, "উক্তি কপি করা হয়েছে")
                        }
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// ANIMATED VERTICAL ENTRY WRAPPER
// -------------------------------------------------------------
@Composable
private fun AnimatedWisdomContainer(
    index: Int,
    isLoaded: Boolean,
    content: @Composable () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(isLoaded) {
        if (isLoaded) {
            delay(index * 90L)
            isVisible = true
        }
    }

    val animatedAlpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
        label = "card_alpha_$index"
    )

    val animatedOffsetY by animateFloatAsState(
        targetValue = if (isVisible) 0f else 35f,
        animationSpec = spring(dampingRatio = 0.8f, stiffness = 300f),
        label = "card_offset_$index"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .graphicsLayer {
                alpha = animatedAlpha
                translationY = animatedOffsetY
            }
    ) {
        content()
    }
}

// -------------------------------------------------------------
// 1. HOLY AL-QURAN CARD
// -------------------------------------------------------------
@Composable
private fun HolyQuranWisdomCard(
    quran: QuranWisdomItem,
    onCopy: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("holy_quran_wisdom_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Badge: Holy Al-Quran
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFECFDF5),
                        border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = null,
                                tint = Color(0xFF059669),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Holy Al-Quran",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669)
                            )
                        }
                    }

                    // Live API Badge & Copy
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFECFDF5),
                            border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                        ) {
                            Text(
                                text = "Live API",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Quran Verse",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Arabic Verse Inner Display Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.08f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = quran.arabicText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 36.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Bengali Translation with Left Accent Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                ) {
                    Box(
                        modifier = Modifier
                            .width(3.5.dp)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(2.dp))
                            .background(Color(0xFF10B981))
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = quran.bengaliText,
                        fontSize = 14.5.sp,
                        fontWeight = FontWeight.Normal,
                        lineHeight = 22.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // English Translation (Italic)
                Text(
                    text = quran.englishText,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Footer Row: Surah Details & Ayah
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${quran.surahAyahFormatted}...",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF065F46)
                    )
                    Text(
                        text = quran.ayahFormatted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bottom Green Accent Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Color(0xFF10B981),
                        shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
                    )
            )
        }
    }
}

// -------------------------------------------------------------
// 2. HADITH WISDOM CARD
// -------------------------------------------------------------
@Composable
private fun HadithWisdomCard(
    hadith: HadithWisdomItem,
    onCopy: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("hadith_wisdom_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Badge: Hadith Wisdom
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFEFF6FF),
                        border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.BookmarkBorder,
                                contentDescription = null,
                                tint = Color(0xFF0284C7),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Hadith Wisdom",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7)
                            )
                        }
                    }

                    // Live API Badge & Copy
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFEFF6FF),
                            border = BorderStroke(1.dp, Color(0xFFBFDBFE))
                        ) {
                            Text(
                                text = "Live API",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0284C7),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Hadith",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Hadith Bengali Text
                Text(
                    text = hadith.bengaliText,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 23.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                // English Reference / Text
                Text(
                    text = hadith.englishRef,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Footer Row: Source & Narrator / Number
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Source: ${hadith.sourceName}",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0369A1)
                    )
                    Text(
                        text = "Narrated by: ${hadith.narratorOrNumber}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Bottom Blue Accent Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Color(0xFF0EA5E9),
                        shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
                    )
            )
        }
    }
}

// -------------------------------------------------------------
// 3. INSPIRATIONAL QUOTES CARD
// -------------------------------------------------------------
@Composable
private fun InspirationalQuoteCard(
    quote: QuoteWisdomItem,
    onCopy: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.14f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("inspirational_quote_card")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Top Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Badge: Inspirational Quotes
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFF5F3FF),
                        border = BorderStroke(1.dp, Color(0xFFDDD6FE))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(0xFF7C3AED),
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "Inspirational Quotes",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C3AED)
                            )
                        }
                    }

                    // Live API Badge & Copy
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFF5F3FF),
                            border = BorderStroke(1.dp, Color(0xFFDDD6FE))
                        ) {
                            Text(
                                text = "Live API",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF7C3AED),
                                modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = onCopy,
                            modifier = Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy Quote",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Quotation Mark Symbol
                Text(
                    text = "“",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFDDD6FE),
                    lineHeight = 30.sp,
                    modifier = Modifier.padding(start = 2.dp)
                )

                // Bengali Quote
                Text(
                    text = quote.quoteBn,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 23.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                // English Quote (Italic)
                Text(
                    text = quote.quoteEn,
                    fontSize = 13.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 19.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Footer Row: Author & INSPIRATION Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "— ${quote.author}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF6D28D9)
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFF5F3FF)
                    ) {
                        Text(
                            text = quote.tag.uppercase(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF7C3AED),
                            letterSpacing = 0.8.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Bottom Purple Accent Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Color(0xFF8B5CF6),
                        shape = RoundedCornerShape(bottomStart = 22.dp, bottomEnd = 22.dp)
                    )
            )
        }
    }
}

// -------------------------------------------------------------
// HELPER: CLIPBOARD COPY WITH FEEDBACK
// -------------------------------------------------------------
private fun copyToClipboard(
    context: Context,
    label: String,
    text: String,
    toastMessage: String
) {
    try {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "কপি করা সম্ভব হয়নি", Toast.LENGTH_SHORT).show()
    }
}
