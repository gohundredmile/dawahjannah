package com.example.ui.screens.tools.hadithtopics

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
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Share
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
import com.example.data.model.hadithtopics.TopicHadithRef
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TopicHadithCard(
    hadith: TopicHadithRef,
    isBookmarked: Boolean,
    onToggleBookmark: () -> Unit,
    onOpenInHadithCollection: ((bookSlug: String) -> Unit)? = null,
    onOpenQuranSurah: ((surahNumber: Int) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Book info badge & Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Book badge
                Surface(
                    color = Color(0xFF4338CA).copy(alpha = 0.12f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CollectionsBookmark,
                            contentDescription = null,
                            tint = Color(0xFF4338CA),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = hadith.referenceText,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4338CA)
                            )
                        )
                    }
                }

                // Grade Pill & Bookmark
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val gradeColor = when (hadith.gradeColor) {
                        "MUTTAFAAQ_ALAYH" -> Color(0xFF047857)
                        "HASAN" -> Color(0xFFD97706)
                        else -> Color(0xFF059669)
                    }
                    Surface(
                        color = gradeColor.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = hadith.gradeBn,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = gradeColor
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onToggleBookmark,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = if (isBookmarked) "বুকমার্ক মুছুন" else "বুকমার্ক করুন",
                            tint = if (isBookmarked) IslamicGold else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Chapter / Narrator
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "অধ্যায়: ${hadith.chapterTitleBn} • বর্ণনাকারী: ${hadith.narratorBn}",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Medium
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Arabic Text
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = hadith.arabicText,
                    modifier = Modifier.padding(14.dp),
                    style = MaterialTheme.typography.titleMedium.copy(
                        lineHeight = 32.sp,
                        textAlign = TextAlign.End,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Bangla Translation
            Text(
                text = hadith.banglaText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            // English Translation if present
            if (hadith.englishText.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = hadith.englishText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }

            // Explanation / Takheeq if present
            if (hadith.explanationBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    color = IslamicGreen.copy(alpha = 0.06f),
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.15f))
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "💡 শিক্ষা ও প্রেক্ষাপট:",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = IslamicGreen
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = hadith.explanationBn,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }

            // Associated Quran Ayah / Dua chips
            if (!hadith.relatedQuranAyahRef.isNullOrBlank() || !hadith.relatedDuaTitleBn.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    hadith.relatedQuranAyahRef?.let { quranRef ->
                        AssistChip(
                            onClick = {
                                hadith.relatedQuranSurahNumber?.let { surah ->
                                    onOpenQuranSurah?.invoke(surah)
                                }
                            },
                            label = { Text("কুরআনের আয়াত: $quranRef", style = MaterialTheme.typography.labelSmall) },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = IslamicGreen
                                )
                            },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = IslamicGreen.copy(alpha = 0.08f),
                                labelColor = IslamicGreen
                            )
                        )
                    }
                    hadith.relatedDuaTitleBn?.let { duaTitle ->
                        AssistChip(
                            onClick = { },
                            label = { Text("সংশ্লিষ্ট দো'আ: $duaTitle", style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer Actions (Copy, Share, Open Hadith Collection)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Secondary action: In Hadith Collection
                if (onOpenInHadithCollection != null) {
                    AssistChip(
                        onClick = { onOpenInHadithCollection(hadith.bookSlug) },
                        label = { Text("হাদিসগ্রন্থে দেখুন", style = MaterialTheme.typography.labelSmall) },
                        leadingIcon = {
                            Icon(
                                Icons.Default.CollectionsBookmark,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    )
                } else {
                    Spacer(modifier = Modifier.width(1.dp))
                }

                // Copy & Share buttons
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Hadith", hadith.formattedShareText)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "হাদিস কপি হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "কপি করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, hadith.formattedShareText)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "হাদিস শেয়ার করুন"))
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "শেয়ার করুন",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}
