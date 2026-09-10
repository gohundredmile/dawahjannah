package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.RoutineItem
import com.example.ui.theme.IslamicGold
import com.example.util.CalendarHelper

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveRoutineCard(
    item: RoutineItem,
    isCompleted: Boolean = false,
    onToggleCompleted: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }
    var tapCounter by rememberSaveable { mutableIntStateOf(0) }

    val borderColor = if (isCompleted) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    } else if (item.isTopPriority) {
        IslamicGold.copy(alpha = 0.65f)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
    }

    val containerColor = if (isCompleted) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.08f)
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(if (item.isTopPriority || isCompleted) 1.5.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isCompleted) 1.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Top Row: Badges, Priority, and Action Icons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    // Tag Badge (e.g., "নফল সালাত", "দৈনিক সুরক্ষা")
                    if (item.tagBn.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                        ) {
                            Text(
                                text = item.tagBn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Count Badge (e.g., "১ বার", "প্রতিটি ৩ বার", "২ থেকে ৮ রাকাত")
                    if (item.countBadgeBn.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = item.countBadgeBn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Extra badge (e.g. verse info)
                    if (item.extraBadgeBn.isNotEmpty()) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.45f)
                        ) {
                            Text(
                                text = item.extraBadgeBn,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Top Priority Badge
                    if (item.isTopPriority) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IslamicGold.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "শীর্ষ আমল",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold
                                )
                            }
                        }
                    }
                }

                // Completion Toggle + Action buttons
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    IconButton(
                        onClick = { onToggleCompleted(!isCompleted) },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Outlined.CheckCircleOutline,
                            contentDescription = if (isCompleted) "সম্পন্ন" else "অসম্পন্ন",
                            tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            val shareText = buildString {
                                appendLine("✨ *${item.titleBn}*")
                                if (item.subtitleBn.isNotEmpty()) appendLine("_${item.subtitleBn}_")
                                appendLine("সময়কাল: ${item.timeSlotTitleBn}")
                                if (item.countBadgeBn.isNotEmpty()) appendLine("সংখ্যা: ${item.countBadgeBn}")
                                if (item.arabicText.isNotEmpty()) {
                                    appendLine()
                                    appendLine(item.arabicText)
                                }
                                appendLine()
                                appendLine("আমল বিবরণ:")
                                appendLine(item.descriptionBn)
                                if (item.virtuesRewardBn.isNotEmpty()) {
                                    appendLine()
                                    appendLine("ফজিলত ও সওয়াব:")
                                    appendLine(item.virtuesRewardBn)
                                }
                                if (item.reference.isNotEmpty()) {
                                    appendLine()
                                    appendLine("রেফারেন্স: ${item.reference}")
                                }
                                appendLine()
                                appendLine("— দা'ওয়াহ টু জান্নাহ (বাস্তবসম্মত ২৪ ঘণ্টার আমল রুটিন)")
                            }
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
                            modifier = Modifier.size(17.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Title
            Text(
                text = item.titleBn,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                lineHeight = 24.sp
            )

            // Subtitle
            if (item.subtitleBn.isNotEmpty()) {
                Text(
                    text = item.subtitleBn,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            // Arabic text preview or banner if available
            if (item.arabicText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                    border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.arabicText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontFamily = FontFamily.Default,
                                fontSize = 18.sp,
                                lineHeight = 28.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 6.dp),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.surface,
                                modifier = Modifier.clickable {
                                    val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                    cm.setPrimaryClip(ClipData.newPlainText("Arabic", item.arabicText))
                                    Toast.makeText(context, "আরবি টেক্সট কপি হয়েছে", Toast.LENGTH_SHORT).show()
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "কপি",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Description
            Text(
                text = item.descriptionBn,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )

            // Interactive Repeat/Counter Tracker (for prayers or repetitious zikr)
            val isCountable = item.countBadgeBn.contains("বার") || item.countBadgeBn.contains("রাকাত")
            if (isCountable) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primary),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = CalendarHelper.toBanglaNumber(tapCounter),
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "আমল সম্পন্ন গণনা",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = "লক্ষ্য: ${item.countBadgeBn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            if (tapCounter > 0) {
                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.surface,
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                                    modifier = Modifier.clickable { tapCounter = 0 }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Reset",
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier
                                            .padding(6.dp)
                                            .size(16.dp)
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.clickable {
                                    tapCounter++
                                    if (!isCompleted && tapCounter >= 1) {
                                        onToggleCompleted(true)
                                    }
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Add",
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "+১ পাঠ",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Expandable details (Virtues & Reference)
            if (item.virtuesRewardBn.isNotEmpty() || item.reference.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { isExpanded = !isExpanded }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isExpanded) "ফজিলত ও রেফারেন্স লুকান" else "বিস্তারিত ফজিলত ও দলীল দেখুন...",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            if (item.virtuesRewardBn.isNotEmpty()) {
                                Text(
                                    text = "ফজিলত ও প্রতিদান:",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = item.virtuesRewardBn,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(top = 3.dp)
                                )
                            }
                            if (item.reference.isNotEmpty()) {
                                if (item.virtuesRewardBn.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(6.dp))
                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                                        thickness = 0.5.dp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                }
                                Text(
                                    text = "সহীহ সূত্র ও দলীল: ${item.reference}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
