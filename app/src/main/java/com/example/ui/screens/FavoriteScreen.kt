package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.entity.BookmarkEntity
import com.example.data.model.ScreenEffectMode
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.MainViewModel
import com.example.ui.viewmodel.MoreSubScreen
import com.example.util.CalendarHelper

@Composable
fun FavoriteScreen(
    viewModel: MainViewModel,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val allBookmarks by viewModel.allBookmarks.collectAsState()
    val fontScale by viewModel.fontScale.collectAsState()
    val screenEffectMode by viewModel.screenEffectMode.collectAsState()
    val isGlassMode = screenEffectMode == ScreenEffectMode.GLASS

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("ALL") }

    // Categories available from the current bookmarks
    val filterOptions = listOf(
        "ALL" to "সব (${CalendarHelper.toBanglaNumber(allBookmarks.size)})",
        "DUA" to "মাসনুন দোয়া",
        "ISLAMIC_LIFE" to "ইসলামী জীবন",
        "DUROOD" to "দরূদ ও আমল",
        "HEALTH_DUA" to "রোগ নিরাময়",
        "ALLAH_NAME" to "আল্লাহর নাম"
    )

    // Filter items based on selected category chip & search query
    val filteredBookmarks = allBookmarks.filter { item ->
        val matchesCategory = when (selectedCategoryFilter) {
            "ALL" -> true
            "DUA" -> item.type == "DUA" || item.categoryBn.contains("দোয়া")
            "ISLAMIC_LIFE" -> item.type == "ISLAMIC_LIFE"
            "DUROOD" -> item.type == "DUROOD" || item.categoryBn.contains("দরূদ")
            "HEALTH_DUA" -> item.type == "HEALTH_DUA" || item.categoryBn.contains("রোগ") || item.categoryBn.contains("শিফা")
            "ALLAH_NAME" -> item.type == "ALLAH_NAME" || item.categoryBn.contains("আসমাউল")
            else -> true
        }

        val query = searchQuery.trim()
        val matchesSearch = query.isBlank() ||
                item.titleBn.contains(query, ignoreCase = true) ||
                item.arabicText.contains(query) ||
                item.pronunciationBn.contains(query, ignoreCase = true) ||
                item.meaningBn.contains(query, ignoreCase = true) ||
                item.categoryBn.contains(query, ignoreCase = true) ||
                item.detailsBn.contains(query, ignoreCase = true)

        matchesCategory && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        contentPadding = PaddingValues(bottom = 90.dp, start = 16.dp, end = 16.dp, top = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Hero Header Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isGlassMode) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = IslamicGold.copy(alpha = 0.18f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Bookmark,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "সংরক্ষিত দু'আ ও আমল ভাণ্ডার",
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (allBookmarks.isEmpty()) {
                                "যেকোনো দু'আ বা প্রবন্ধ বুকমার্ক করে এখানে রাখুন"
                            } else {
                                "মোট ${CalendarHelper.toBanglaNumber(allBookmarks.size)} টি দু'আ ও প্রবন্ধ সংরক্ষিত রয়েছে"
                            },
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "সংরক্ষিত বিষয়বস্তু অনুসন্ধান করুন...",
                        fontFamily = banglaFont,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear search",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                singleLine = true
            )
        }

        // Category Filter Chips
        if (allBookmarks.isNotEmpty()) {
            item {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(filterOptions) { (key, label) ->
                        val isSelected = selectedCategoryFilter == key
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategoryFilter = key },
                            label = {
                                Text(
                                    text = label,
                                    fontFamily = banglaFont,
                                    fontSize = 12.5.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                containerColor = MaterialTheme.colorScheme.surface,
                                labelColor = MaterialTheme.colorScheme.onSurface
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f),
                                selectedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }
            }
        }

        // Bookmark Items List / Empty State
        if (filteredBookmarks.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Outlined.BookmarkBorder,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(34.dp)
                                )
                            }
                        }

                        Text(
                            text = if (searchQuery.isNotBlank()) {
                                "‘$searchQuery’ দিয়ে কোনো সংরক্ষিত আইটেম পাওয়া যায়নি"
                            } else if (allBookmarks.isEmpty()) {
                                "আপনার বুকমার্কে এখনো কোনো দু'আ বা প্রবন্ধ নেই"
                            } else {
                                "এই ক্যাটাগরিতে কোনো বুকমার্ক নেই"
                            },
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "অ্যাপের যেকোনো মাসনুন দু'আ, দরূদ শরীফ, আসমাউল হুসনা বা ইসলামী জীবন প্রবন্ধের পাশের বুকমার্ক (🔖) আইকনে চাপুন। সেগুলো দ্রুত ফিরে পাওয়ার জন্য এই পাতায় জমা থাকবে।",
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { viewModel.selectTab(AppTab.DUA) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.primary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "মাসনুন দোয়া",
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Button(
                                onClick = { viewModel.selectTab(AppTab.MORE) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Explore,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ইসলামী জীবন",
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        } else {
            items(filteredBookmarks, key = { it.id }) { item ->
                FavoriteItemCard(
                    item = item,
                    fontScale = fontScale.scale,
                    onRemoveBookmark = {
                        viewModel.removeBookmarkById(item.id)
                        Toast.makeText(context, "বুকমার্ক থেকে সরানো হয়েছে", Toast.LENGTH_SHORT).show()
                    },
                    onOpenTarget = {
                        when (item.targetScreen) {
                            "DUA" -> viewModel.selectTab(AppTab.DUA)
                            "HEALTH_DUAS" -> {
                                viewModel.selectTab(AppTab.MORE)
                                viewModel.navigateToMoreSubScreen(MoreSubScreen.HEALTH_DUAS)
                            }
                            "DUROOD_AMOL" -> {
                                viewModel.selectTab(AppTab.MORE)
                                viewModel.navigateToMoreSubScreen(MoreSubScreen.DUROOD_AMOL)
                            }
                            "NAMES_OF_ALLAH" -> {
                                viewModel.selectTab(AppTab.MORE)
                                viewModel.navigateToMoreSubScreen(MoreSubScreen.NAMES_OF_ALLAH)
                            }
                            "ISLAMIC_LIFE" -> {
                                viewModel.selectTab(AppTab.MORE)
                            }
                            else -> viewModel.selectTab(AppTab.MORE)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun FavoriteItemCard(
    item: BookmarkEntity,
    fontScale: Float,
    onRemoveBookmark: () -> Unit,
    onOpenTarget: () -> Unit
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.22f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header Row: Category Badge & Actions (Remove Bookmark, Copy, Share)
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = item.categoryBn.ifBlank { "সংরক্ষিত" },
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    if (item.subtitleBn.isNotBlank() && item.subtitleBn != item.categoryBn) {
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = item.subtitleBn,
                                fontFamily = banglaFont,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    // Remove Bookmark Button
                    IconButton(
                        onClick = onRemoveBookmark,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "Remove Bookmark",
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Copy Button
                    IconButton(
                        onClick = {
                            val copyPayload = buildString {
                                appendLine("【 ${item.titleBn} 】")
                                if (item.categoryBn.isNotBlank()) appendLine("ক্যাটাগরি: ${item.categoryBn}")
                                if (item.subtitleBn.isNotBlank()) appendLine("আমল/সময়: ${item.subtitleBn}")
                                if (item.arabicText.isNotBlank()) appendLine("\n${item.arabicText}")
                                if (item.pronunciationBn.isNotBlank()) appendLine("\nউচ্চারণ: ${item.pronunciationBn}")
                                if (item.meaningBn.isNotBlank()) appendLine("\nঅর্থ: ${item.meaningBn}")
                                if (item.detailsBn.isNotBlank()) appendLine("\nফজিলত ও আমল:\n${item.detailsBn}")
                                if (item.referenceBn.isNotBlank()) appendLine("\nসূত্র: ${item.referenceBn}")
                                appendLine("\n— দা'ওয়াহ টু জান্নাহ অ্যাপ")
                            }
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText(item.titleBn, copyPayload)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "ক্লিপবোর্ডে কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy Item",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Share Button
                    IconButton(
                        onClick = {
                            val sharePayload = buildString {
                                appendLine("🔹 ${item.titleBn}")
                                if (item.categoryBn.isNotBlank()) appendLine("ক্যাটাগরি: ${item.categoryBn}")
                                if (item.arabicText.isNotBlank()) appendLine("\n${item.arabicText}")
                                if (item.pronunciationBn.isNotBlank()) appendLine("\nউচ্চারণ: ${item.pronunciationBn}")
                                if (item.meaningBn.isNotBlank()) appendLine("\nঅর্থ: ${item.meaningBn}")
                                if (item.detailsBn.isNotBlank()) appendLine("\nফজিলত ও আমল:\n${item.detailsBn}")
                                if (item.referenceBn.isNotBlank()) appendLine("\nরেফারেন্স: ${item.referenceBn}")
                                appendLine("\n— দা'ওয়াহ টু জান্নাহ অ্যাপ")
                            }
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, sharePayload)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(intent, "শেয়ার করুন"))
                        },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share Item",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Title
            Text(
                text = item.titleBn,
                fontFamily = banglaFont,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = (16.5f * fontScale).sp,
                    lineHeight = (23 * fontScale).sp
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Arabic Text Block (if any)
            if (item.arabicText.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = item.arabicText,
                            fontFamily = arabicFont,
                            fontSize = (21 * fontScale).sp,
                            lineHeight = (36 * fontScale).sp,
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Pronunciation (উচ্চারণ)
            if (item.pronunciationBn.isNotBlank() && item.pronunciationBn != item.titleBn) {
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(
                        text = "উচ্চারণ: ",
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14 * fontScale).sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.pronunciationBn,
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (14 * fontScale).sp,
                            lineHeight = (21 * fontScale).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Meaning (অর্থ)
            if (item.meaningBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Row {
                    Text(
                        text = "অর্থ: ",
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = (14 * fontScale).sp),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = item.meaningBn,
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = (14 * fontScale).sp,
                            lineHeight = (21 * fontScale).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // Details / Fojilot / Amol Method
            if (item.detailsBn.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "আমল ও ফজিলত:",
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.detailsBn,
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = (13 * fontScale).sp,
                                lineHeight = (20 * fontScale).sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            // Reference (দলিল / সূত্র) and Quick Jump
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.referenceBn.isNotBlank()) {
                    Text(
                        text = "সূত্র: ${item.referenceBn}",
                        fontFamily = banglaFont,
                        style = MaterialTheme.typography.labelSmall.copy(fontSize = (11.5f * fontScale).sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier.weight(1f, fill = false)
                    )
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    modifier = Modifier.clickable { onOpenTarget() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "মূল বিভাগে যান",
                            fontFamily = banglaFont,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
