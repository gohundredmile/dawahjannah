package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.DailyInspiration
import com.example.data.model.DuaItem
import com.example.data.model.ForbiddenTimeInfo
import com.example.data.model.PrayerTimeItem
import com.example.data.model.RoutineItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.theme.IslamicIvory
import com.example.ui.theme.LocalAppFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.CalendarHelper

// -------------------------------------------------------------
// RELOCATED TIME & DATE CARD (Positioned right below "আস-সালামু আলাইকুম")
// -------------------------------------------------------------
@Composable
fun HeaderTimeDateCard(
    calendarInfo: CalendarHelper.TripleCalendarInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentTime by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Date()
            delay(1000)
        }
    }

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

    val hourFormat = remember { SimpleDateFormat("hh", Locale.ENGLISH) }
    val minFormat = remember { SimpleDateFormat("mm", Locale.ENGLISH) }
    val secFormat = remember { SimpleDateFormat("ss", Locale.ENGLISH) }
    val amPmFormat = remember { SimpleDateFormat("a", Locale.ENGLISH) }

    val hours = hourFormat.format(currentTime)
    val minutes = minFormat.format(currentTime)
    val seconds = secFormat.format(currentTime)
    val amPm = amPmFormat.format(currentTime)

    val liteEyeSoothingBg = Color(0xFFFFFDF7)
    val soothingBorder = Color(0xFFF1E6D3)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag("header_time_date_card"),
        shape = RoundedCornerShape(16.dp),
        color = liteEyeSoothingBg,
        border = BorderStroke(1.dp, soothingBorder),
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Digital Clock
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = hours,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = LocalAppFontFamily.current,
                    color = Color(0xFF111827),
                    letterSpacing = (-1).sp
                )

                // Colon Separator
                Column(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(6.dp),
                        shape = CircleShape,
                        color = Color(0xFF9CA3AF)
                    ) {}
                    Surface(
                        modifier = Modifier.size(6.dp),
                        shape = CircleShape,
                        color = Color(0xFF9CA3AF)
                    ) {}
                }

                Text(
                    text = minutes,
                    fontSize = 46.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = LocalAppFontFamily.current,
                    color = Color(0xFF111827),
                    letterSpacing = (-1).sp
                )

                // Colon Separator
                Column(
                    modifier = Modifier.padding(horizontal = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        modifier = Modifier.size(6.dp),
                        shape = CircleShape,
                        color = Color(0xFF9CA3AF)
                    ) {}
                    Surface(
                        modifier = Modifier.size(6.dp),
                        shape = CircleShape,
                        color = Color(0xFF9CA3AF)
                    ) {}
                }

                // Seconds and AM/PM Stack
                Column(
                    modifier = Modifier.padding(start = 2.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = seconds,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B5563)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(5.dp)
                    ) {
                        Text(
                            text = amPm,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF15803D),
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Primary Gregorian & Bangla Day Date (English calendar date in Bangla)
            Text(
                text = "$dynamicGregorianDateBn, ${calendarInfo.englishDay}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = LocalBanglaFontFamily.current,
                color = Color(0xFF111827),
                letterSpacing = (-0.2).sp,
                textAlign = TextAlign.Center
            )

            // Rest of the two calendars' current date in one line in Bangla (Bengali San & Hijri Islamic San)
            val cleanBengaliDate = remember(calendarInfo.bengaliDateFormatted) {
                calendarInfo.bengaliDateFormatted.substringBefore("(").trim()
            }
            val cleanHijriDate = remember(calendarInfo.hijriDateFormatted) {
                calendarInfo.hijriDateFormatted.trim()
            }
            Text(
                text = "$cleanBengaliDate  •  $cleanHijriDate",
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = LocalBanglaFontFamily.current,
                color = Color(0xFF15803D),
                modifier = Modifier.padding(top = 2.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun IslamicHeaderCover(
    salutation: String,
    countdownFormatted: String,
    nextPrayerName: String,
    presentPrayerName: String = "এশা",
    presentNofolName: String = "তাহাজ্জুদ",
    remainingHours: Int = 0,
    remainingMinutes: Int = 0,
    remainingSeconds: Int = 0,
    forbiddenTimeInfo: ForbiddenTimeInfo = ForbiddenTimeInfo(),
    calendarInfo: CalendarHelper.TripleCalendarInfo? = null,
    onTapTimeDate: () -> Unit = {},
    onOpenSettings: () -> Unit = {},
    onClickCard: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Elegant subtle geometric shading
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.92f)
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 10.dp)
            ) {
                // Header Top Bar
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                ) {
                    Column {
                        Text(
                            text = "আস-সালামু আলাইকুম",
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 19.sp),
                            fontFamily = LocalBanglaFontFamily.current,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.3.sp,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Relocated 'Time & Date' Section - immediately below 'আস-সালামু আলাইকুম'
                if (calendarInfo != null) {
                    HeaderTimeDateCard(
                        calendarInfo = calendarInfo,
                        onClick = onTapTimeDate
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Revamped 3-Part Salat Timing Card (Matching User Specimen)
                Surface(
                    color = IslamicIvory,
                    shape = RoundedCornerShape(16.dp),
                    shadowElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SalatTimingFlipCard(
                        nextPrayerName = nextPrayerName,
                        presentPrayerName = presentPrayerName,
                        presentNofolName = presentNofolName,
                        remainingHours = remainingHours,
                        remainingMinutes = remainingMinutes,
                        remainingSeconds = remainingSeconds,
                        countdownFormatted = countdownFormatted,
                        forbiddenTimeInfo = forbiddenTimeInfo,
                        onClickCard = onClickCard
                    )
                }
            }
        }
    }
}

@Composable
fun TripleCalendarCard(calendarInfo: CalendarHelper.TripleCalendarInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // Gregorian Card
        CalendarGridItem(
            label = "GREGORIAN",
            value = calendarInfo.englishDateFormatted,
            valueColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        // Bengali Card
        CalendarGridItem(
            label = "বঙ্গাব্দ",
            value = calendarInfo.bengaliDateFormatted,
            valueColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f)
        )

        // Hijri Card
        CalendarGridItem(
            label = "HIJRI",
            value = calendarInfo.hijriDateFormatted,
            valueColor = IslamicGold,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun CalendarGridItem(
    label: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold,
                color = valueColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun QuickActionCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDark) Color(0xFF1E293B) else Color.White
        ),
        border = BorderStroke(1.dp, iconTint.copy(alpha = if (isDark) 0.35f else 0.22f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            iconTint.copy(alpha = if (isDark) 0.16f else 0.08f),
                            if (isDark) Color(0xFF161F2C) else Color(0xFFFCFCFD)
                        )
                    )
                )
                .padding(horizontal = 4.dp, vertical = 7.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Compact eye-catchy squircle icon badge
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    iconTint.copy(alpha = 0.25f),
                                    iconTint.copy(alpha = 0.12f)
                                )
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 0.8.dp,
                            color = iconTint.copy(alpha = 0.35f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = value,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.1).sp
                    ),
                    fontFamily = LocalBanglaFontFamily.current,
                    color = if (isDark) Color.White else Color(0xFF1E293B),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    fontFamily = LocalBanglaFontFamily.current,
                    color = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                    maxLines = 1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun PrayerTimeItemCard(
    item: PrayerTimeItem,
    onToggleNotification: (String) -> Unit
) {
    val isHighlighted = item.isHighlighted
    val cardBg = if (isHighlighted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }

    val borderColor = if (isHighlighted) {
        IslamicGold
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(if (isHighlighted) 1.5.dp else 1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isHighlighted) {
                    Surface(
                        color = IslamicGold,
                        shape = CircleShape,
                        modifier = Modifier.size(8.dp)
                    ) {}
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Column {
                    Text(
                        text = item.nameBn,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.SemiBold,
                        color = if (isHighlighted) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.nameEn,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = item.timeFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighlighted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { onToggleNotification(item.id) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = if (item.isNotificationEnabled) Icons.Default.Notifications else Icons.Default.NotificationsOff,
                        contentDescription = "Toggle Prayer Notification",
                        tint = if (item.isNotificationEnabled) IslamicGold else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DailyInspirationCard(
    inspiration: DailyInspiration,
    context: Context = LocalContext.current
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 6.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.12f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Verse Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(IslamicGold)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "আজকের আয়াত",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Text(
                        text = inspiration.quranSurahAyah,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic text
            Text(
                text = inspiration.quranArabic,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontFamily = FontFamily.Default,
                    lineHeight = 30.sp
                ),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.End,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "\"${inspiration.quranBengali}\"",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
            )

            // Hadith Section
            Text(
                text = "হাদিসের আলো (${inspiration.hadithNarrator})",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = inspiration.hadithBengali,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 19.sp
            )
            Text(
                text = "সূত্র: ${inspiration.hadithBookSource}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 2.dp)
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
            )

            // Wisdom Quote Section
            Text(
                text = "ইসলামিক জীবনবোধ ও প্রজ্ঞা",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "\"${inspiration.wisdomQuoteBn}\"",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 21.sp
            )
            Text(
                text = "— ${inspiration.wisdomAuthorBn}",
                style = MaterialTheme.typography.labelSmall,
                color = IslamicGold,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        val shareText = """
                            *দা'ওয়াহ টু জান্নাহ - দৈনিক অনুপ্রেরণা*
                            
                            📖 আয়াত: ${inspiration.quranArabic}
                            অর্থ: ${inspiration.quranBengali} (${inspiration.quranSurahAyah})
                            
                            📜 হাদিস: ${inspiration.hadithBengali}
                            (${inspiration.hadithBookSource})
                            
                            💡 প্রজ্ঞা: "${inspiration.wisdomQuoteBn}"
                            — ${inspiration.wisdomAuthorBn}
                        """.trimIndent()

                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Islamic Inspiration", shareText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "অনুপ্রেরণা কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier.padding(end = 8.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Inspiration",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("কপি", style = MaterialTheme.typography.labelMedium)
                }

                OutlinedButton(
                    onClick = {
                        val shareText = """
                            *দা'ওয়াহ টু জান্নাহ - দৈনিক অনুপ্রেরণা*
                            
                            📖 আয়াত: ${inspiration.quranArabic}
                            অর্থ: ${inspiration.quranBengali} (${inspiration.quranSurahAyah})
                            
                            📜 হাদিস: ${inspiration.hadithBengali}
                            (${inspiration.hadithBookSource})
                            
                            💡 প্রজ্ঞা: "${inspiration.wisdomQuoteBn}"
                            — ${inspiration.wisdomAuthorBn}
                        """.trimIndent()

                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, "অনুপ্রেরণা শেয়ার করুন")
                        context.startActivity(shareIntent)
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = IslamicGold)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Inspiration",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("শেয়ার", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}

@Composable
fun DuaCard(
    dua: DuaItem,
    onToggleBookmark: (String, Boolean) -> Unit,
    context: Context = LocalContext.current
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.20f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp)
        ) {
            // Header with Category Badge and Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = dua.categoryNameBn,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                    if (dua.id.startsWith("dua_remote_")) {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "নতুন আপডেট",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { onToggleBookmark(dua.id, dua.isBookmarked) },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (dua.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark Dua",
                            tint = if (dua.isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val duaText = """
                                *${dua.titleBn}* (${dua.categoryNameBn})
                                
                                ${dua.arabicText}
                                
                                উচ্চারণ: ${dua.pronunciationBn}
                                
                                অর্থ: ${dua.meaningBn}
                                
                                ফজিলত: ${dua.virtuesBn}
                                সূত্র: ${dua.reference}
                                
                                — দা'ওয়াহ টু জান্নাহ অ্যাপ
                            """.trimIndent()

                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Dua", duaText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "দোয়া কপি হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Dua",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareText = """
                                *${dua.titleBn}* (${dua.categoryNameBn})
                                
                                ${dua.arabicText}
                                
                                উচ্চারণ: ${dua.pronunciationBn}
                                
                                অর্থ: ${dua.meaningBn}
                                
                                ফজিলত: ${dua.virtuesBn}
                                সূত্র: ${dua.reference}
                                
                                — দা'ওয়াহ টু জান্নাহ
                            """.trimIndent()

                            val sendIntent: Intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "দোয়া শেয়ার করুন"))
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Dua",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = dua.titleBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Arabic text in soft highlighted container
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.20f))
            ) {
                Text(
                    text = dua.arabicText,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily.Default,
                        lineHeight = 32.sp,
                        fontSize = 18.sp
                    ),
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Bengali Pronunciation
            Text(
                text = "উচ্চারণ: ${dua.pronunciationBn}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.primary,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Bengali Meaning
            Text(
                text = "অর্থ: ${dua.meaningBn}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 22.sp
            )

            // Collapsible Virtues & Reference
            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    if (dua.virtuesBn.isNotBlank()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.30f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.28f))
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(17.dp)
                                    )
                                    Text(
                                        text = "ফজিলত ও উপকারিতা:",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = dua.virtuesBn,
                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    lineHeight = 20.sp
                                )
                            }
                        }
                    }

                    if (dua.reference.isNotBlank()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            shape = RoundedCornerShape(10.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.55f),
                            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.20f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.Top,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(16.dp)
                                        .padding(top = 1.dp)
                                )
                                Column {
                                    Text(
                                        text = "রেফারেন্স ও সনদ সূত্র:",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = dua.reference,
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 17.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Expand / Collapse Action Button
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = if (isExpanded) {
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                } else {
                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f)
                },
                border = BorderStroke(
                    1.dp,
                    if (isExpanded) MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
                    else MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
                ),
                onClick = { isExpanded = !isExpanded }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = if (isExpanded) "সংক্ষিপ্ত করুন" else "ফজিলত ও হাদিস রেফারেন্স দেখুন",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RoutineCard(
    item: RoutineItem,
    context: Context = LocalContext.current
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            if (item.isTopPriority) 1.5.dp else 1.dp,
            if (item.isTopPriority) IslamicGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = if (item.isTopPriority) IslamicGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (item.isTopPriority) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "শীর্ষ আমল #${CalendarHelper.toBanglaNumber(item.priorityRank)}",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                        } else {
                            Text(
                                text = item.timeSlotTitleBn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                IconButton(
                    onClick = {
                        val shareText = """
                            *${item.titleBn}* (${item.timeSlotTitleBn})
                            
                            আমল নির্দেশিকা: ${item.descriptionBn}
                            
                            প্রতিদান ও ফজিলত: ${item.virtuesRewardBn}
                            
                            রেফারেন্স: ${item.reference}
                            
                            — দা'ওয়াহ টু জান্নাহ (২৪ ঘণ্টার সুন্নাত আমল)
                        """.trimIndent()
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(sendIntent, "আমল শেয়ার করুন"))
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Routine",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.titleBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = item.subtitleBn,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 2.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.descriptionBn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "ফজিলত ও সওয়াব:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.virtuesRewardBn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                    Text(
                        text = "সূত্র: ${item.reference}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
