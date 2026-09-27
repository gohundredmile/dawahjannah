package com.example.ui.screens.tools.qurantopics

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.qurantopics.TopicAyah
import com.example.data.model.qurantopics.TopicRelevance
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicAyahCard(
    ayah: TopicAyah,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onOpenInQuran: (surahNumber: Int) -> Unit,
    onOpenContextView: (TopicAyah) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Surah and Ayah reference badge + Bookmark button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = IslamicGreen.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${ayah.surahNameBn} [${ayah.surahNumber}:${ayah.ayahNumber}]",
                            fontSize = 12.5.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGreen
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "• ${ayah.revelationTypeBn}",
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (ayah.relevance == TopicRelevance.SUPPORTING) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = IslamicGold.copy(alpha = 0.15f),
                            modifier = Modifier.padding(end = 6.dp)
                        ) {
                            Text(
                                text = "সহায়ক আয়াত",
                                fontSize = 11.sp,
                                color = IslamicGold,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "বুকমার্ক সরানো" else "বুকমার্ক সংরক্ষণ",
                            tint = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Arabic text (RTL, large, high readability)
            Text(
                text = ayah.arabicText,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                lineHeight = 38.sp,
                fontFamily = FontFamily.Serif,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bangla Translation
            Text(
                text = ayah.translationBn,
                fontSize = 14.5.sp,
                lineHeight = 22.sp,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Normal
            )

            if (ayah.translationEn.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.translationEn,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                )
            }

            if (!ayah.contextNoteBn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "প্রাসঙ্গিক প্রেক্ষাপট: ${ayah.contextNoteBn}",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Action row: Copy, Share, Context view, Open in Holy Quran
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Open in full Quran
                AssistChip(
                    onClick = { onOpenInQuran(ayah.surahNumber) },
                    label = { Text("কোরআনে দেখুন", fontSize = 11.5.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.MenuBook,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = IslamicGreen
                        )
                    },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = IslamicGreen.copy(alpha = 0.08f),
                        labelColor = IslamicGreen
                    ),
                    border = BorderStroke(0.8.dp, IslamicGreen.copy(alpha = 0.3f))
                )

                // Context view
                AssistChip(
                    onClick = { onOpenContextView(ayah) },
                    label = { Text("প্রসঙ্গসহ দেখুন", fontSize = 11.5.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                // Copy
                AssistChip(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Quran Ayah", ayah.formattedShareText)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "আয়াত ও অর্থ কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    label = { Text("কপি", fontSize = 11.5.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.ContentCopy,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                )

                // Share
                AssistChip(
                    onClick = {
                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_SUBJECT, ayah.referenceText)
                            putExtra(Intent.EXTRA_TEXT, ayah.formattedShareText)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "শেয়ার করুন"))
                    },
                    label = { Text("শেয়ার", fontSize = 11.5.sp) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                )
            }
        }
    }
}
