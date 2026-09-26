package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Brightness2
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily

/**
 * Options Dialog for "রমাদান" presenting three related feature cards:
 * 1. "রমাদান সময়সূচী" (Ramadan Moon Schedule, 30 days fasting, Tarawih & Hijri calendar)
 * 2. "সেহেরি ও ইফতারের সময়সূচী" (Daily Sehri, Sunrise & Iftar detailed schedule and live countdown)
 * 3. "রমাদান ইন্টেলিজেন্স" (All in one Ramadan & Ramadan Intelligence dashboard and guides)
 */
@Composable
fun RamadanOptionsDialog(
    onDismiss: () -> Unit,
    onSelectRamadanSchedule: () -> Unit,
    onSelectSehriIftarSchedule: () -> Unit,
    onSelectRamadanIntelligence: () -> Unit
) {
    val isDark = isSystemInDarkTheme()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.55f))
                .clickable(onClick = onDismiss),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .widthIn(max = 440.dp)
                    .clickable(enabled = false) {},
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF0D9488).copy(alpha = if (isDark) 0.25f else 0.15f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Brightness2,
                                        contentDescription = null,
                                        tint = if (isDark) Color(0xFF2DD4BF) else Color(0xFF0D9488),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "রমাদান ও সিয়াম",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "পছন্দের সেবা নির্বাচন করুন",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = LocalBanglaFontFamily.current,
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

                    Spacer(modifier = Modifier.height(18.dp))

                    // Option 1: রমাদান সময়সূচী
                    Surface(
                        onClick = {
                            onDismiss()
                            onSelectRamadanSchedule()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDark) Color(0xFF042F2E).copy(alpha = 0.5f) else Color(0xFFF0FDFA),
                        border = BorderStroke(
                            1.5.dp,
                            if (isDark) Color(0xFF0D9488).copy(alpha = 0.6f) else Color(0xFF99F6E4)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isDark) Color(0xFF134E4A) else Color(0xFFCCFBF1),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Brightness2,
                                        contentDescription = null,
                                        tint = if (isDark) Color(0xFF2DD4BF) else Color(0xFF0F766E),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "১. রমাদান সময়সূচী",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "লাইভ চাঁদ দেখা, ৩০ দিনের রোজা, তারাবীহ ও হিজরি ক্যালেন্ডার",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF4B5563)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = if (isDark) Color(0xFF2DD4BF) else Color(0xFF0F766E),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 2: সেহেরি ও ইফতারের সময়সূচী
                    Surface(
                        onClick = {
                            onDismiss()
                            onSelectSehriIftarSchedule()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDark) Color(0xFF2E1065).copy(alpha = 0.5f) else Color(0xFFFAF5FF),
                        border = BorderStroke(
                            1.5.dp,
                            if (isDark) Color(0xFF7E22CE).copy(alpha = 0.6f) else Color(0xFFE9D5FF)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isDark) Color(0xFF4C1D95) else Color(0xFFF3E8FF),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.NightsStay,
                                        contentDescription = null,
                                        tint = if (isDark) Color(0xFFC084FC) else Color(0xFF7E22CE),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "২. সেহেরি ও ইফতারের সময়সূচী",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "প্রতিদিনের সেহরি, সূর্যোদয় ও ইফতারের পূর্ণাঙ্গ সময়সূচী ও রিয়েল-টাইম কাউন্টডাউন",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF4B5563)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = if (isDark) Color(0xFFC084FC) else Color(0xFF7E22CE),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 3: রমাদান ইন্টেলিজেন্স
                    Surface(
                        onClick = {
                            onDismiss()
                            onSelectRamadanIntelligence()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDark) Color(0xFF451A03).copy(alpha = 0.5f) else Color(0xFFFFFBEB),
                        border = BorderStroke(
                            1.5.dp,
                            if (isDark) IslamicGold.copy(alpha = 0.6f) else Color(0xFFFDE68A)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isDark) Color(0xFF78350F) else Color(0xFFFEF3C7),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = if (isDark) IslamicGold else Color(0xFFB45309),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "৩. রমাদান ইন্টেলিজেন্স",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "All in one Ramadan & Ramadan Intelligence — পরিকল্পনা, কুরআন খতম, লাইলাতুল কদর ও সদকা গাইড",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF4B5563)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = if (isDark) IslamicGold else Color(0xFFB45309),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
