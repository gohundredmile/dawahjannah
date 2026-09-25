package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.datasource.IslamicLifeData
import com.example.data.model.LiveDateAmolItem
import com.example.ui.theme.ArabicFontFamily
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CalendarHelper
import com.example.util.LiveTimeAndEventMatcher

/**
 * Top interactive horizontal scrolling bar for HomeScreen matching date & time based data:
 * - Current Waqt/Time of day amol & duas
 * - Special day of week (e.g. Friday Mode, Monday Sunnah fast, Wednesday dua)
 * - Islamic Calendar matches (Ayyam al-Bidh, Ramadan, 10 days of Dhul Hijjah, Ashura)
 * - Historical Islamic events & high-yield duas
 *
 * Tap on any card opens an interactive detail modal with Arabic, translation, copy & direct navigation.
 */
@Composable
fun LiveAmolTickerBar(
    viewModel: MainViewModel,
    calendarInfo: CalendarHelper.TripleCalendarInfo?,
    modifier: Modifier = Modifier,
    onOpenTripleCalendar: () -> Unit = {}
) {
    val context = LocalContext.current
    val matchingItems = remember(calendarInfo) {
        LiveTimeAndEventMatcher.getMatchingItems(calendarInfo)
    }

    if (matchingItems.isEmpty()) return

    // Repeat items to ensure smooth, infinite right-to-left seamless marquee
    val infiniteItems = remember(matchingItems) {
        List(14) { matchingItems }.flatten()
    }

    var selectedItem by remember { mutableStateOf<LiveDateAmolItem?>(null) }

    // Pulsing live indicator transition
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.82f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(850, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    // Sleek, ultra-attractive 2-line height Live Amol Marquee Bar
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 3.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF022C22) // Deep Islamic emerald night
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        border = BorderStroke(
            1.2.dp,
            Brush.horizontalGradient(
                listOf(
                    IslamicGold.copy(alpha = 0.95f),
                    Color(0xFFFDE68A),
                    IslamicGold.copy(alpha = 0.95f)
                )
            )
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF022C22),
                            Color(0xFF064E3B),
                            Color(0xFF065F46),
                            Color(0xFF042F2E)
                        )
                    )
                )
                .padding(vertical = 4.dp, horizontal = 6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pinned Left Live Badge (Anchored, eye-catching pulsing indicator)
                Surface(
                    shape = RoundedCornerShape(9.dp),
                    color = Color(0x38000000),
                    border = BorderStroke(0.9.dp, IslamicGold.copy(alpha = 0.65f)),
                    modifier = Modifier
                        .clickable { selectedItem = matchingItems.firstOrNull() }
                        .padding(end = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Pulsing red live beacon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(13.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(13.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444).copy(alpha = 0.35f))
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.5.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEF4444))
                            )
                        }
                        Spacer(modifier = Modifier.width(4.5.dp))
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "LIVE",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontSize = 8.5.sp,
                                    letterSpacing = 0.5.sp
                                ),
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = "আমল",
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.sp),
                                fontWeight = FontWeight.Bold,
                                color = IslamicGoldLight,
                                fontFamily = LocalBanglaFontFamily.current
                            )
                        }
                    }
                }

                // Vertical glowing divider
                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(26.dp)
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    Color.Transparent,
                                    IslamicGold.copy(alpha = 0.75f),
                                    Color.Transparent
                                )
                            )
                        )
                )

                Spacer(modifier = Modifier.width(6.dp))

                // Infinite Right-to-Left Auto-Scrolling 2-Line Ticker (Never stops)
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .basicMarquee(
                            iterations = Int.MAX_VALUE,
                            velocity = 38.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    infiniteItems.forEachIndexed { index, item ->
                        LiveAmolTwoLinePill(
                            item = item,
                            onClick = { selectedItem = item }
                        )

                        // Sparkling golden divider between items
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = "✦",
                                color = IslamicGoldLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog when tapped
    selectedItem?.let { item ->
        LiveAmolDetailDialog(
            item = item,
            onDismiss = { selectedItem = null },
            onNavigate = { target ->
                selectedItem = null
                handleLiveAmolNavigation(
                    target = target,
                    viewModel = viewModel,
                    onOpenTripleCalendar = onOpenTripleCalendar
                )
            }
        )
    }
}

/**
 * Compact 2-line height interactive pill for infinite marquee ticker.
 * Line 1: [Icon] [Title] • [Badge in gold]
 * Line 2: [Short Subtitle / Virtue] • [বিস্তারিত ➔]
 */
@Composable
private fun LiveAmolTwoLinePill(
    item: LiveDateAmolItem,
    onClick: () -> Unit
) {
    val icon = getAmolIcon(item.iconKey)

    Surface(
        modifier = Modifier
            .clickable { onClick() },
        shape = RoundedCornerShape(9.dp),
        color = Color(0x30000000),
        border = BorderStroke(0.8.dp, item.primaryColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mini Icon Medallion
            Surface(
                modifier = Modifier.size(24.dp),
                shape = CircleShape,
                color = item.primaryColor.copy(alpha = 0.22f),
                border = BorderStroke(0.8.dp, item.primaryColor.copy(alpha = 0.45f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = item.primaryColor,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(6.dp))

            Column(
                verticalArrangement = Arrangement.Center
            ) {
                // Line 1: Title + Golden Badge
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.titleBn,
                        style = MaterialTheme.typography.labelMedium.copy(fontSize = 11.5.sp),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = LocalBanglaFontFamily.current,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = item.primaryColor.copy(alpha = 0.35f)
                    ) {
                        Text(
                            text = item.badgeBn,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 8.5.sp),
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFFEF08A),
                            fontFamily = LocalBanglaFontFamily.current,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }

                // Line 2: Short virtue / instruction / Hadith note + "বিস্তারিত ➔"
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.shortSubtitleBn,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                        color = Color(0xFFE2E8F0),
                        fontFamily = LocalBanglaFontFamily.current,
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "• বিস্তারিত ➔",
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                        color = Color(0xFFFDE047),
                        fontWeight = FontWeight.Bold,
                        fontFamily = LocalBanglaFontFamily.current
                    )
                }
            }
        }
    }
}

@Composable
fun LiveAmolDetailDialog(
    item: LiveDateAmolItem,
    onDismiss: () -> Unit,
    onNavigate: (String?) -> Unit
) {
    val context = LocalContext.current
    val icon = getAmolIcon(item.iconKey)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            border = BorderStroke(1.5.dp, item.primaryColor.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Top Header Row with Icon & Dismiss button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            modifier = Modifier.size(44.dp),
                            shape = CircleShape,
                            color = item.primaryColor.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, item.primaryColor.copy(alpha = 0.4f))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.titleBn,
                                    tint = item.primaryColor,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = item.primaryColor.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = item.categoryBn,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = item.primaryColor,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = item.titleBn,
                                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = LocalBanglaFontFamily.current
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Timing & Context Badge Card
                item.timingContextBn?.let { ctx ->
                    Spacer(modifier = Modifier.height(14.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("⏱️", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ctx,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = LocalBanglaFontFamily.current,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Arabic Script Card (if provided)
                item.arabicText?.let { arabic ->
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                        ),
                        border = BorderStroke(1.dp, item.primaryColor.copy(alpha = 0.35f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = arabic,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontSize = 20.sp,
                                    lineHeight = 36.sp
                                ),
                                fontFamily = ArabicFontFamily,
                                textAlign = TextAlign.Center,
                                color = item.primaryColor,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Pronunciation
                item.pronunciationBn?.let { pron ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "বাংলা উচ্চারণ:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = item.primaryColor,
                        fontFamily = LocalBanglaFontFamily.current
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = pron,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = LocalBanglaFontFamily.current
                    )
                }

                // Meaning
                item.meaningBn?.let { mean ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "বাংলা অনুবাদ:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = item.primaryColor,
                        fontFamily = LocalBanglaFontFamily.current
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        text = mean,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 13.5.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = LocalBanglaFontFamily.current
                    )
                }

                // Virtues & References
                item.virtuesBn?.let { virtues ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFEF3C7).copy(alpha = 0.6f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("✨", fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "প্রামাণ্য ফজিলত ও তাৎপর্য:",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF92400E),
                                    fontFamily = LocalBanglaFontFamily.current
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = virtues,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.5.sp),
                                color = Color(0xFF78350F),
                                fontFamily = LocalBanglaFontFamily.current
                            )
                            item.referenceBn?.let { ref ->
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "সূত্র: $ref",
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309),
                                    fontFamily = LocalBanglaFontFamily.current
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(14.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Copy button (if text available)
                    if (item.arabicText != null || item.meaningBn != null) {
                        OutlinedButton(
                            onClick = {
                                val textToCopy = buildString {
                                    append(item.titleBn)
                                    append("\n\n")
                                    item.arabicText?.let { append("$it\n\n") }
                                    item.pronunciationBn?.let { append("উচ্চারণ: $it\n\n") }
                                    item.meaningBn?.let { append("অর্থ: $it\n\n") }
                                    item.referenceBn?.let { append("সূত্র: $it") }
                                }
                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                val clip = ClipData.newPlainText("দোয়া ও আমল", textToCopy)
                                clipboard.setPrimaryClip(clip)
                                Toast.makeText(context, "দো'আ ক্লিপবোর্ডে কপি করা হয়েছে!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "কপি করুন",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "কপি",
                                fontFamily = LocalBanglaFontFamily.current,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // Direct Action / Feature Navigation Button
                    if (item.actionTarget != null) {
                        Button(
                            onClick = { onNavigate(item.actionTarget) },
                            modifier = Modifier.weight(1.5f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = item.primaryColor)
                        ) {
                            Text(
                                text = item.actionButtonTextBn ?: "আমল করুন ➔",
                                fontFamily = LocalBanglaFontFamily.current,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    } else {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = item.primaryColor)
                        ) {
                            Text(
                                text = "ঠিক আছে",
                                fontFamily = LocalBanglaFontFamily.current,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getAmolIcon(key: String): ImageVector {
    return when (key) {
        "SUN" -> Icons.Default.WbSunny
        "MOON" -> Icons.Default.Brightness2
        "MOSQUE" -> Icons.Default.Mosque
        "BOOK" -> Icons.Default.MenuBook
        "SHIELD" -> Icons.Default.Security
        "HEART" -> Icons.Default.Favorite
        "PRAYER" -> Icons.Default.Mosque
        "CALENDAR" -> Icons.Default.CalendarMonth
        "SPARKLE" -> Icons.Default.AutoAwesome
        else -> Icons.Default.Star
    }
}

private fun handleLiveAmolNavigation(
    target: String?,
    viewModel: MainViewModel,
    onOpenTripleCalendar: () -> Unit
) {
    if (target == null) return
    when (target) {
        "friday_mode" -> viewModel.openFridayMode()
        "morning_evening" -> {
            val sec = viewModel.getIslamicLifeSection("morning_evening_special")
                ?: IslamicLifeData.sections.find { it.id == "morning_evening_special" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "sayyidul_istighfar" -> {
            val sec = viewModel.getIslamicLifeSection("sayyidul_istighfar_special")
                ?: IslamicLifeData.sections.find { it.id == "sayyidul_istighfar_special" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "dua_acceptance" -> {
            val sec = viewModel.getIslamicLifeSection("dua_acceptance_times")
                ?: IslamicLifeData.sections.find { it.id == "dua_acceptance_times" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "five_waqt" -> {
            val sec = viewModel.getIslamicLifeSection("five_waqt_after_salat")
                ?: IslamicLifeData.sections.find { it.id == "five_waqt_after_salat" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "isme_azam" -> {
            val sec = viewModel.getIslamicLifeSection("isme_azam")
                ?: IslamicLifeData.sections.find { it.id == "isme_azam" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "tawbah_last_two" -> {
            val sec = viewModel.getIslamicLifeSection("tawbah_last_two")
                ?: IslamicLifeData.sections.find { it.id == "tawbah_last_two" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "surah_baqarah" -> {
            val sec = viewModel.getIslamicLifeSection("surah_baqarah_last_2")
                ?: IslamicLifeData.sections.find { it.id == "surah_baqarah_last_2" }
            if (sec != null) {
                viewModel.openIslamicLifeSection(sec)
            } else {
                viewModel.selectTab(AppTab.MORE)
            }
        }
        "triple_calendar" -> onOpenTripleCalendar()
        "nofol_salat" -> viewModel.selectTab(AppTab.ROUTINE)
        "islamic_habit" -> viewModel.openIslamicHabitSystem()
        "ramadan_intelligence" -> viewModel.openRamadanIntelligence()
        else -> viewModel.selectTab(AppTab.MORE)
    }
}
