package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Star
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
 * Dialog for "৯. আল্লাহর ৯৯ নাম" presenting two options:
 * 1. "৯৯ নাম ও ফজিলত" (Full 99 names list, meanings, and fojilot)
 * 2. "আসমাউল হুসনা প্র্যোজন ভিত্তিক দোয়া" (Needs-based duas and authentic amols)
 */
@Composable
fun AllahNamesOptionsDialog(
    onDismiss: () -> Unit,
    onSelectNamesAndFojilot: () -> Unit,
    onSelectNeedsBasedDua: () -> Unit
) {
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
                                color = IslamicGold.copy(alpha = 0.15f),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = "৯. আল্লাহর ৯৯ নাম",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "পছন্দের বিভাগ নির্বাচন করুন",
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

                    // Option 1: ৯৯ নাম ও ফজিলত
                    Surface(
                        onClick = {
                            onDismiss()
                            onSelectNamesAndFojilot()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFF0FDF4),
                        border = BorderStroke(1.5.dp, Color(0xFFBBF7D0)),
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
                                color = Color(0xFFDCFCE7),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.FormatListNumbered,
                                        contentDescription = null,
                                        tint = Color(0xFF15803D),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "৯৯ নাম ও ফজিলত",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "আসমাউল হুসনা ৯৯টি নামের বাংলা অর্থ, বিশুদ্ধ উচ্চারণ, গুরুত্ব ও বিস্তারিত ফজিলত",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = Color(0xFF4B5563)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color(0xFF15803D),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option 2: আসমাউল হুসনা প্র্যোজন ভিত্তিক দোয়া
                    Surface(
                        onClick = {
                            onDismiss()
                            onSelectNeedsBasedDua()
                        },
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFFBEB),
                        border = BorderStroke(1.5.dp, Color(0xFFFDE68A)),
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
                                color = Color(0xFFFEF3C7),
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFFB45309),
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "আসমাউল হুসনা প্র্যোজন ভিত্তিক দোয়া",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = Color(0xFF111827)
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = "পবিত্র কুরআন ও সিহাহ সিত্তাহ সহীহ হাদীস ভিত্তিক প্রয়োজনভিত্তিক দো'আ ও আমল",
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp,
                                    fontFamily = LocalBanglaFontFamily.current,
                                    color = Color(0xFF4B5563)
                                )
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                                contentDescription = null,
                                tint = Color(0xFFB45309),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
