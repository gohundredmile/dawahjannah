package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.ui.theme.IslamicGold

/**
 * Fullscreen Window displaying all 17 features (and future features)
 * with wallpaper decoration, categorized chips, quick search,
 * and high-contrast, lucrative layout.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFeaturesDialog(
    features: List<HomeFeatureItem>,
    defaultFeatures: List<HomeFeatureItem> = features,
    currentSortMode: String = "DEFAULT",
    onUpdateOrder: (List<String>, String) -> Unit = { _, _ -> },
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        val isDark = isSystemInDarkTheme()
        var searchQuery by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf("সবগুলো") }
        var showCustomizeDialog by remember { mutableStateOf(false) }

        val effectiveFeatures = remember(features) {
            features.renumberedFeatures()
        }

        val categories = remember {
            listOf("সবগুলো", "সালাত ও সময়", "দো‘আ ও আমল", "জীবন ও টুলস")
        }

        val filteredFeatures = remember(searchQuery, selectedCategory, effectiveFeatures) {
            effectiveFeatures.filter { item ->
                val matchesCategory = when (selectedCategory) {
                    "সবগুলো" -> true
                    "সালাত ও সময়" -> item.categoryBn.contains("সালাত") || item.categoryBn.contains("সময়") || item.categoryBn.contains("রমজান") || item.categoryBn.contains("রমাদান") || item.categoryBn.contains("সিয়াম")
                    "দো‘আ ও আমল" -> item.categoryBn.contains("দো‘আ") || item.categoryBn.contains("আমল") || item.categoryBn.contains("দরূদ") || item.categoryBn.contains("ক্ষমা")
                    "জীবন ও টুলস" -> item.categoryBn.contains("টুলস") || item.categoryBn.contains("নাম") || item.categoryBn.contains("সুরক্ষা") || item.categoryBn.contains("ক্যালেন্ডার") || item.categoryBn.contains("জ্ঞান")
                    else -> true
                }
                val matchesQuery = searchQuery.isBlank() ||
                        item.titleBn.contains(searchQuery, ignoreCase = true) ||
                        item.subtitleBn.contains(searchQuery, ignoreCase = true) ||
                        item.categoryBn.contains(searchQuery, ignoreCase = true)

                matchesCategory && matchesQuery
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isDark) Color(0xFF03140C) else MaterialTheme.colorScheme.background)
        ) {
            if (isDark) {
                // Dark Mode Wallpaper Background
                Image(
                    painter = painterResource(id = R.drawable.img_sehri_iftar_bg),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Multi-layer Islamic emerald gradient scrim for rich contrast & readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xEE041C11),
                                    Color(0xF602120A),
                                    Color(0xFB010A06)
                                )
                            )
                        )
                )
            } else {
                // Light Mode Clean, Bright, Luminous Canvas aligned with MaterialTheme
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.surface,
                                    MaterialTheme.colorScheme.background,
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
                                )
                            )
                        )
                )
            }

            // Main Content Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .navigationBarsPadding()
            ) {
                // 1. Top Bar with Back Action & Title
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isDark) Color(0x40FFFFFF) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f),
                            border = BorderStroke(1.dp, if (isDark) IslamicGold.copy(alpha = 0.6f) else IslamicGold.copy(alpha = 0.45f)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "বন্ধ করুন",
                                    tint = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Stars,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "সকল ফিচার ও আমল",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "দা'ওয়াহ টু জান্নাহর ${features.size}টি প্রিমিয়াম বিভাগ ও আমল",
                                fontSize = 11.5.sp,
                                color = if (isDark) Color(0xFFD1D5DB) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Action Buttons Row: [সাজান / কাস্টমাইজ] + [কাউন্ট ব্যাজ]
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDark) Color(0xFF0F4737) else Color(0xFFCEECE6),
                            border = BorderStroke(1.dp, if (isDark) Color(0xFF2DD4BF) else Color(0xFF14B8A6)),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { showCustomizeDialog = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "সাজান",
                                    tint = if (isDark) Color(0xFF5EEAD4) else Color(0xFF0F4E3E),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "সাজান",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFF5EEAD4) else Color(0xFF0F4E3E)
                                )
                            }
                        }

                        // Count Badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = IslamicGold.copy(alpha = if (isDark) 0.2f else 0.15f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.65f))
                        ) {
                            Text(
                                text = "${features.size}টি ফিচার",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) IslamicGold else Color(0xFF92400E),
                                modifier = Modifier.padding(horizontal = 9.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                // 2. Search Field
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                text = "ফিচার বা আমল খুঁজুন...",
                                color = if (isDark) Color.White.copy(alpha = 0.6f) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "খুঁজুন",
                                tint = if (isDark) IslamicGold else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "মুছুন",
                                        tint = if (isDark) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = if (isDark) Color(0x66062719) else MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = if (isDark) Color(0x44062719) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            focusedBorderColor = if (isDark) IslamicGold else MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = if (isDark) Color(0x55FFFFFF) else MaterialTheme.colorScheme.outline.copy(alpha = 0.35f),
                            focusedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // 3. Category Filter Chips
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = category == selectedCategory
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) {
                                if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                            } else {
                                if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
                            },
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) {
                                    if (isDark) IslamicGold else MaterialTheme.colorScheme.primary
                                } else {
                                    if (isDark) Color(0x44FFFFFF) else MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
                                }
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectedCategory = category }
                        ) {
                            Text(
                                text = category,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) {
                                    if (isDark) Color(0xFF0F172A) else Color.White
                                } else {
                                    if (isDark) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // 4. Feature Cards List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(filteredFeatures, key = { it.id }) { item ->
                        FeatureDetailCard(
                            item = item,
                            isDark = isDark,
                            onClick = {
                                onDismiss()
                                item.onClickAction()
                            }
                        )
                    }

                    if (filteredFeatures.isEmpty()) {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 48.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null,
                                    tint = IslamicGold.copy(alpha = 0.5f),
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "কোনো ফিচার পাওয়া যায়নি",
                                    color = if (isDark) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ভিন্ন নামে বা বানানে অনুসন্ধান করে দেখুন",
                                    color = if (isDark) Color.White.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showCustomizeDialog) {
            CustomizeExploreDialog(
                initialFeatures = effectiveFeatures,
                defaultFeatures = defaultFeatures.renumberedFeatures(),
                currentSortMode = currentSortMode,
                onSaveOrder = { orderIds, mode ->
                    onUpdateOrder(orderIds, mode)
                },
                onDismiss = { showCustomizeDialog = false }
            )
        }
    }
}

/**
 * Lucrative card displaying an individual feature item inside the All Features window.
 */
@Composable
private fun FeatureDetailCard(
    item: HomeFeatureItem,
    isDark: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = if (isDark) Color(0xD907291B) else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            1.1.dp,
            if (isDark) IslamicGold.copy(alpha = 0.45f) else IslamicGold.copy(alpha = 0.35f)
        ),
        shadowElevation = if (isDark) 3.dp else 2.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Serial Number + Glowing Icon
            Box(contentAlignment = Alignment.BottomEnd) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isDark) Color.Transparent else item.iconColor.copy(alpha = 0.1f),
                    border = BorderStroke(1.2.dp, item.iconColor.copy(alpha = if (isDark) 0.6f else 0.4f)),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        item.iconColor.copy(alpha = if (isDark) 0.35f else 0.25f),
                                        item.iconColor.copy(alpha = if (isDark) 0.12f else 0.04f)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.titleBn,
                            tint = item.iconColor,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }

                // Number Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isDark) Color(0xFF0F172A) else Color(0xFFF1F5F9),
                    border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.7f)),
                    modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)
                ) {
                    Text(
                        text = item.serialNumberBn,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) IslamicGold else Color(0xFF92400E),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Details (Title, Subtitle, Category Badge)
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.titleBn,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = item.iconColor.copy(alpha = if (isDark) 0.2f else 0.12f),
                        border = BorderStroke(0.7.dp, item.iconColor.copy(alpha = if (isDark) 0.45f else 0.35f))
                    ) {
                        Text(
                            text = item.categoryBn,
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = item.iconColor,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.subtitleBn,
                    fontSize = 11.5.sp,
                    color = if (isDark) Color(0xFFD1D5DB) else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Trailing Action Arrow
            Surface(
                shape = CircleShape,
                color = if (isDark) IslamicGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, if (isDark) IslamicGold.copy(alpha = 0.4f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "প্রবেশ করুন",
                        tint = if (isDark) IslamicGold else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
