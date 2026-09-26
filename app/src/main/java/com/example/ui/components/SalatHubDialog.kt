package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.CalendarHelper

/**
 * Data representation for each item in the "সালাত" Hub Dialog.
 */
data class SalatHubOption(
    val serialBn: String,
    val titleBn: String,
    val subtitleBn: String,
    val categoryBadgeBn: String,
    val icon: ImageVector,
    val themeColor: Color,
    val onClick: () -> Unit
)

/**
 * Hub Dialog for "সালাত" feature on the Home Screen / Top Features.
 * Shows the 9 essential Salat feature cards:
 * 1. "সালাতের সময়সূচী" (Salat Timings & Forbidden Time)
 * 2. "নামাজ গাইড" (Namaz Guide, Rakat, Rules)
 * 3. "নফল সালাত" (8 Nafl Salats Schedule & Rules)
 * 4. "সালাত ও দোয়া" (Salat & Dua Special Article)
 * 5. "৫ ওয়াক্ত সালাত শেষে দো‘আ সমুহ" (Dua After 5 Daily Prayers)
 * 6. "ফরজ নামাজের সালাম ফিরানোর আগে সালাতের ভিতর পঠিতব্য দোয়া" (Dua Before Salam in Prayer)
 * 7. "ফরজ নামাজের পর সালাম ফিরিয়ে দোয়া" (Dua After Salam)
 * 8. "ফজর ও মাগরিবের নামাযের মাঝে ও পরের আমল" (Fajr & Maghrib Special Amol)
 * 9. "তাহাজ্জুদের সালাত" (Tahajjud Prayer Complete Guide)
 */
@Composable
fun SalatHubDialog(
    onDismiss: () -> Unit,
    onOpenSalatCalendar: () -> Unit,
    onOpenNamazGuide: () -> Unit,
    onOpenNofolSalat: () -> Unit,
    onOpenSalatAndDua: () -> Unit,
    onOpenFiveWaqtAfterSalat: () -> Unit,
    onOpenSalamBeforeDua: () -> Unit,
    onOpenFarzAfterDua: () -> Unit,
    onOpenFajrMaghribAmol: () -> Unit,
    onOpenTahajjudGuide: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val banglaFont = LocalBanglaFontFamily.current

    val salatOptions = listOf(
        SalatHubOption(
            serialBn = "১",
            titleBn = "সালাতের সময়সূচী",
            subtitleBn = "৫ ওয়াক্ত নামাজের সঠিক ওয়াক্ত, নিষিদ্ধ ও মাকরূহ সময়সূচী",
            categoryBadgeBn = "সময়সূচী ও ওয়াক্ত",
            icon = Icons.Default.AccessTime,
            themeColor = Color(0xFF0284C7),
            onClick = {
                onDismiss()
                onOpenSalatCalendar()
            }
        ),
        SalatHubOption(
            serialBn = "২",
            titleBn = "নামাজ গাইড",
            subtitleBn = "সহীহ সালাত শিক্ষা, ওয়াক্ত, সঠিক রাকাত ও ধারাবাহিক নিয়মাবলী",
            categoryBadgeBn = "শিক্ষা ও নিয়মাবলী",
            icon = Icons.Default.Mosque,
            themeColor = Color(0xFF059669),
            onClick = {
                onDismiss()
                onOpenNamazGuide()
            }
        ),
        SalatHubOption(
            serialBn = "৩",
            titleBn = "নফল সালাত",
            subtitleBn = "তাহাজ্জুদ, ইশরাক, চাশত, আওয়াবীনসহ ৮টি নফল সালাতের পূর্ণাঙ্গ সময়সূচী",
            categoryBadgeBn = "নফল সালাত",
            icon = Icons.Default.SelfImprovement,
            themeColor = Color(0xFFE11D48),
            onClick = {
                onDismiss()
                onOpenNofolSalat()
            }
        ),
        SalatHubOption(
            serialBn = "৪",
            titleBn = "সালাত ও দোয়া",
            subtitleBn = "সিজদা, কুনুত, সালামের পূর্বে ও ৫ ওয়াক্ত সালাতের সহীহ দো‘আ",
            categoryBadgeBn = "প্রবন্ধ ও দো‘আ",
            icon = Icons.Default.MenuBook,
            themeColor = Color(0xFF0D9488),
            onClick = {
                onDismiss()
                onOpenSalatAndDua()
            }
        ),
        SalatHubOption(
            serialBn = "৫",
            titleBn = "৫ ওয়াক্ত সালাত শেষে দো‘আ সমুহ",
            subtitleBn = "ফরজ সালাত শেষে সংকট মুক্তি, আসমানের দরজা উন্মুক্তকরণ ও রিযিক বৃদ্ধির আমল",
            categoryBadgeBn = "সালাত পরবর্তী",
            icon = Icons.Default.AutoAwesome,
            themeColor = Color(0xFF8B5CF6),
            onClick = {
                onDismiss()
                onOpenFiveWaqtAfterSalat()
            }
        ),
        SalatHubOption(
            serialBn = "৬",
            titleBn = "ফরজ নামাজের সালাম ফিরানোর আগে সালাতের ভিতর পঠিতব্য দোয়া",
            subtitleBn = "সালাতের সালাম ফিরানোর পূর্বে কোরআন ও সহীহ হাদিসের শ্রেষ্ঠ মাসনূন দো‘আ",
            categoryBadgeBn = "সালাতের ভিতর",
            icon = Icons.Default.Favorite,
            themeColor = Color(0xFFD97706),
            onClick = {
                onDismiss()
                onOpenSalamBeforeDua()
            }
        ),
        SalatHubOption(
            serialBn = "৭",
            titleBn = "ফরজ নামাজের পর সালাম ফিরিয়ে দোয়া",
            subtitleBn = "সালাম ফিরানোর পরবর্তী তাসবীহ, ইস্তিগফার, আয়াতুল কুরসী ও বরকতময় দো‘আ",
            categoryBadgeBn = "সালাত পরবর্তী",
            icon = Icons.Default.Stars,
            themeColor = Color(0xFF10B981),
            onClick = {
                onDismiss()
                onOpenFarzAfterDua()
            }
        ),
        SalatHubOption(
            serialBn = "৮",
            titleBn = "ফজর ও মাগরিবের নামাযের মাঝে ও পরের আমল",
            subtitleBn = "ফজরের সুন্নত-ফরজের মাঝে বিশেষ আমল ও ফজর-মাগরিব পরবর্তী ১০ বার তাহলীল",
            categoryBadgeBn = "ফজর ও মাগরিব",
            icon = Icons.Default.WbSunny,
            themeColor = Color(0xFFEA580C),
            onClick = {
                onDismiss()
                onOpenFajrMaghribAmol()
            }
        ),
        SalatHubOption(
            serialBn = "৯",
            titleBn = "তাহাজ্জুদের সালাত",
            subtitleBn = "রাতের শ্রেষ্ঠ নফল সালাতের নিয়ম, ফযিলত, ওয়াক্ত, রাকাত ও বিশেষ দো‘আ",
            categoryBadgeBn = "ক্বিয়ামুল লাইল",
            icon = Icons.Default.NightsStay,
            themeColor = Color(0xFF4F46E5),
            onClick = {
                onDismiss()
                onOpenTahajjudGuide()
            }
        )
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.65f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .widthIn(max = 480.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 12.dp,
                border = BorderStroke(
                    1.2.dp,
                    if (isDark) IslamicGold.copy(alpha = 0.5f) else IslamicGold.copy(alpha = 0.35f)
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 18.dp)
                ) {
                    // Header Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0284C7).copy(alpha = if (isDark) 0.25f else 0.15f),
                                border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.45f)),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Mosque,
                                        contentDescription = null,
                                        tint = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "সালাত ও ইবাদত",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 17.sp
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF0284C7).copy(alpha = 0.15f)
                                    ) {
                                        Text(
                                            text = "৯টি সেবা",
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7),
                                            fontFamily = banglaFont,
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "ওয়াক্ত, গাইড, নফল ও সালাত-পরবর্তী মাসনূন দো‘আ",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = banglaFont,
                                    fontSize = 11.5.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "বন্ধ করুন",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Scrollable List of 9 Salat Feature Cards
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 520.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(vertical = 4.dp)
                    ) {
                        items(salatOptions) { option ->
                            SalatCardItem(
                                option = option,
                                isDark = isDark
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SalatCardItem(
    option: SalatHubOption,
    isDark: Boolean
) {
    val banglaFont = LocalBanglaFontFamily.current

    Surface(
        onClick = option.onClick,
        shape = RoundedCornerShape(16.dp),
        color = if (isDark) option.themeColor.copy(alpha = 0.12f) else option.themeColor.copy(alpha = 0.06f),
        border = BorderStroke(
            1.2.dp,
            if (isDark) option.themeColor.copy(alpha = 0.45f) else option.themeColor.copy(alpha = 0.30f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Icon Badge with Number
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = option.themeColor.copy(alpha = if (isDark) 0.28f else 0.18f),
                border = BorderStroke(1.dp, option.themeColor.copy(alpha = 0.55f)),
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = option.icon,
                        contentDescription = null,
                        tint = option.themeColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Texts
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "${option.serialBn}. ${option.titleBn}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = option.subtitleBn,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.5.sp,
                    fontFamily = banglaFont,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Arrow Action
            Surface(
                shape = CircleShape,
                color = option.themeColor.copy(alpha = if (isDark) 0.20f else 0.12f),
                modifier = Modifier.size(28.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "প্রবেশ করুন",
                        tint = option.themeColor,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
    }
}
