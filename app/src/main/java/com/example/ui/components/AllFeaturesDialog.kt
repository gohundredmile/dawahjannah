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
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        var searchQuery by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf("সবগুলো") }

        val categories = remember {
            listOf("সবগুলো", "সালাত ও সময়", "দো‘আ ও আমল", "জীবন ও টুলস")
        }

        val filteredFeatures = remember(searchQuery, selectedCategory, features) {
            features.filter { item ->
                val matchesCategory = when (selectedCategory) {
                    "সবগুলো" -> true
                    "সালাত ও সময়" -> item.categoryBn.contains("সালাত") || item.categoryBn.contains("সময়") || item.categoryBn.contains("রমজান")
                    "দো‘আ ও আমল" -> item.categoryBn.contains("দো‘আ") || item.categoryBn.contains("আমল") || item.categoryBn.contains("দরূদ") || item.categoryBn.contains("ক্ষমা")
                    "জীবন ও টুলস" -> item.categoryBn.contains("টুলস") || item.categoryBn.contains("নাম") || item.categoryBn.contains("সুরক্ষা") || item.categoryBn.contains("ক্যালেন্ডার")
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
                .background(Color(0xFF03140C))
        ) {
            // Wallpaper Background
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
                            color = Color(0x40FFFFFF),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.6f)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "বন্ধ করুন",
                                    tint = Color.White,
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
                                    color = Color.White
                                )
                            }
                            Text(
                                text = "দা'ওয়াহ টু জান্নাহর ১৭টি প্রিমিয়াম বিভাগ",
                                fontSize = 11.5.sp,
                                color = Color(0xFFD1D5DB)
                            )
                        }
                    }

                    // Count Badge
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = IslamicGold.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.65f))
                    ) {
                        Text(
                            text = "${features.size}টি ফিচার",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        )
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
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 13.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "খুঁজুন",
                                tint = IslamicGold,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "মুছুন",
                                        tint = Color.White.copy(alpha = 0.7f),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0x66062719),
                            unfocusedContainerColor = Color(0x44062719),
                            focusedBorderColor = IslamicGold,
                            unfocusedBorderColor = Color(0x55FFFFFF),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
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
                            color = if (isSelected) IslamicGold else Color(0x33FFFFFF),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) IslamicGold else Color(0x44FFFFFF)
                            ),
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { selectedCategory = category }
                        ) {
                            Text(
                                text = category,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color(0xFF0F172A) else Color.White,
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
                                    color = Color.White.copy(alpha = 0.8f),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "ভিন্ন নামে বা বানানে অনুসন্ধান করে দেখুন",
                                    color = Color.White.copy(alpha = 0.5f),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Lucrative card displaying an individual feature item inside the All Features window.
 */
@Composable
private fun FeatureDetailCard(
    item: HomeFeatureItem,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color(0xD907291B),
        border = BorderStroke(1.1.dp, IslamicGold.copy(alpha = 0.45f)),
        shadowElevation = 3.dp,
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
                    color = Color.Transparent,
                    border = BorderStroke(1.2.dp, item.iconColor.copy(alpha = 0.6f)),
                    modifier = Modifier.size(52.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        item.iconColor.copy(alpha = 0.35f),
                                        item.iconColor.copy(alpha = 0.12f)
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
                    color = Color(0xFF0F172A),
                    border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.7f)),
                    modifier = Modifier.padding(end = 2.dp, bottom = 2.dp)
                ) {
                    Text(
                        text = item.serialNumberBn,
                        fontSize = 9.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
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
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = item.iconColor.copy(alpha = 0.2f),
                        border = BorderStroke(0.7.dp, item.iconColor.copy(alpha = 0.45f))
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
                    color = Color(0xFFD1D5DB),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Trailing Action Arrow
            Surface(
                shape = CircleShape,
                color = IslamicGold.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "প্রবেশ করুন",
                        tint = IslamicGold,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
