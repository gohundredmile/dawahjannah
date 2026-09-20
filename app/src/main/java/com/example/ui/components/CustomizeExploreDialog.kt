package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.IslamicGold

/**
 * 'এক্সপ্লোর কাস্টমাইজ করুন' (Customize Explore & Relocate Feature Icons)
 * Strictly designed matching the user's provided specimen layout:
 * - Header with Title & Close (X)
 * - 'যেভাবে সাজাবেন' sorting chips: [ডিফল্ট] [ঊর্ধ্বক্রমে] [নিম্নক্রমে] [কাস্টম]
 * - 'আইটেমসমূহ' list of feature cards with:
 *     - Feature icon
 *     - Bengali title
 *     - Reorder controls: Drag Handle (=), Move Up (^), Move Down (v)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomizeExploreDialog(
    initialFeatures: List<HomeFeatureItem>,
    defaultFeatures: List<HomeFeatureItem>,
    currentSortMode: String,
    onSaveOrder: (List<String>, String) -> Unit,
    onDismiss: () -> Unit
) {
    var itemsList by remember(initialFeatures) { mutableStateOf(initialFeatures) }
    var selectedSortMode by remember(currentSortMode) { mutableStateOf(currentSortMode) }
    val isDark = isSystemInDarkTheme()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = if (isDark) Color(0xFF071B12) else Color(0xFFF8FAFC)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // 1. Header: "এক্সপ্লোর কাস্টমাইজ করুন" & Close Button (X)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp, bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "এক্সপ্লোর কাস্টমাইজ করুন",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                    )

                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0x2EFFFFFF) else Color(0xFFE2E8F0),
                        modifier = Modifier.size(36.dp)
                    ) {
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "বন্ধ করুন",
                                tint = if (isDark) Color.White else Color(0xFF1E293B),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                // 2. Section Subheader: "যেভাবে সাজাবেন"
                Text(
                    text = "যেভাবে সাজাবেন",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155),
                    modifier = Modifier.padding(bottom = 10.dp)
                )

                // Sort Options: [ডিফল্ট] [ঊর্ধ্বক্রমে] [নিম্নক্রমে] [কাস্টম]
                val sortOptions = listOf(
                    "DEFAULT" to "ডিফল্ট",
                    "ASCENDING" to "ঊর্ধ্বক্রমে",
                    "DESCENDING" to "নিম্নক্রমে",
                    "CUSTOM" to "কাস্টম"
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sortOptions.forEach { (modeKey, labelBn) ->
                        val isSelected = selectedSortMode == modeKey
                        val pillBgColor = if (isSelected) {
                            if (isDark) Color(0xFF0F4737) else Color(0xFFCEECE6)
                        } else {
                            if (isDark) Color(0x1AFFFFFF) else Color.Transparent
                        }
                        val pillTextColor = if (isSelected) {
                            if (isDark) Color(0xFF5EEAD4) else Color(0xFF0F4E3E)
                        } else {
                            if (isDark) Color(0xFF94A3B8) else Color(0xFF475569)
                        }
                        val pillBorder = if (isSelected) {
                            BorderStroke(1.2.dp, if (isDark) Color(0xFF2DD4BF) else Color(0xFF14B8A6))
                        } else {
                            BorderStroke(1.dp, if (isDark) Color(0x3394A3B8) else Color(0xFFCBD5E1))
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = pillBgColor,
                            border = pillBorder,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    selectedSortMode = modeKey
                                    when (modeKey) {
                                        "DEFAULT" -> {
                                            itemsList = defaultFeatures
                                            onSaveOrder(defaultFeatures.map { it.id }, "DEFAULT")
                                        }
                                        "ASCENDING" -> {
                                            val sorted = itemsList.sortedBy { it.cleanTitleBn }
                                            itemsList = sorted
                                            onSaveOrder(sorted.map { it.id }, "ASCENDING")
                                        }
                                        "DESCENDING" -> {
                                            val sorted = itemsList.sortedByDescending { it.cleanTitleBn }
                                            itemsList = sorted
                                            onSaveOrder(sorted.map { it.id }, "DESCENDING")
                                        }
                                        "CUSTOM" -> {
                                            onSaveOrder(itemsList.map { it.id }, "CUSTOM")
                                        }
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = labelBn,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = pillTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                // 3. Section Subheader: "আইটেমসমূহ"
                Text(
                    text = "আইটেমসমূহ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF334155),
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                // 4. Feature Items Reorderable List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(itemsList, key = { _, item -> item.id }) { index, item ->
                        CustomizeFeatureCard(
                            item = item,
                            index = index,
                            totalCount = itemsList.size,
                            isDark = isDark,
                            onMoveUp = {
                                if (index > 0) {
                                    val mutable = itemsList.toMutableList()
                                    val temp = mutable[index]
                                    mutable[index] = mutable[index - 1]
                                    mutable[index - 1] = temp
                                    itemsList = mutable
                                    selectedSortMode = "CUSTOM"
                                    onSaveOrder(mutable.map { it.id }, "CUSTOM")
                                }
                            },
                            onMoveDown = {
                                if (index < itemsList.size - 1) {
                                    val mutable = itemsList.toMutableList()
                                    val temp = mutable[index]
                                    mutable[index] = mutable[index + 1]
                                    mutable[index + 1] = temp
                                    itemsList = mutable
                                    selectedSortMode = "CUSTOM"
                                    onSaveOrder(mutable.map { it.id }, "CUSTOM")
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual Feature Row Card strictly following the specimen attachment.
 * - Left: Colorful distinct feature icon
 * - Center: Bengali feature title
 * - Right: Action controls [= Drag handle] [^ Move Up] [v Move Down]
 */
@Composable
private fun CustomizeFeatureCard(
    item: HomeFeatureItem,
    index: Int,
    totalCount: Int,
    isDark: Boolean,
    onMoveUp: () -> Unit,
    onMoveDown: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isDark) Color(0xFF10281E) else Color(0xFFEEF3F6),
        border = BorderStroke(
            1.dp,
            if (isDark) Color(0x334ADE80) else Color(0xFFE2E8F0)
        ),
        shadowElevation = 0.5.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left: Feature Icon Container
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (isDark) Color(0x26FFFFFF) else item.iconColor.copy(alpha = 0.12f),
                border = BorderStroke(1.dp, item.iconColor.copy(alpha = if (isDark) 0.5f else 0.35f)),
                modifier = Modifier.size(42.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.cleanTitleBn,
                        tint = item.iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Center: Bengali Title
            Text(
                text = item.cleanTitleBn,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )

            // Right: Reorder Action Controls (= ^ v)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Drag / Equal Handle Icon
                Box(
                    modifier = Modifier
                        .size(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DragHandle,
                        contentDescription = "পুনর্বিন্যাস হ্যান্ডেল",
                        tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Up Chevron Button (^)
                IconButton(
                    onClick = onMoveUp,
                    enabled = index > 0,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "উপরে নিন",
                        tint = if (index > 0) {
                            if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                        } else {
                            if (isDark) Color(0x3394A3B8) else Color(0xFFCBD5E1)
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Down Chevron Button (v)
                IconButton(
                    onClick = onMoveDown,
                    enabled = index < totalCount - 1,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "নিচে নিন",
                        tint = if (index < totalCount - 1) {
                            if (isDark) Color(0xFF38BDF8) else Color(0xFF0284C7)
                        } else {
                            if (isDark) Color(0x3394A3B8) else Color(0xFFCBD5E1)
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
    }
}

/**
 * Extension property to retrieve the clean feature title without numeric prefixes (e.g. "১. ট্র্যাকার" -> "ট্র্যাকার").
 */
val HomeFeatureItem.cleanTitleBn: String
    get() = if (shortTitleBn.isNotBlank()) shortTitleBn else titleBn.replace(Regex("^[০-৯\\d]+\\.\\s*"), "")
