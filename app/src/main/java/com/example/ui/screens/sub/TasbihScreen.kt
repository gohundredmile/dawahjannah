package com.example.ui.screens.sub

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.datasource.TasbihPresetsData
import com.example.data.model.TasbihDhikrItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGoldLight
import com.example.ui.viewmodel.MainViewModel
import com.example.util.CalendarHelper
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasbihScreen(viewModel: MainViewModel) {
    val state by viewModel.tasbihState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var selectedTab by remember { mutableIntStateOf(0) } // 0: কাউন্টার ও যিকির, 1: ২৭টি তাসবিহ ও দো'আ তালিকা
    var searchQuery by remember { mutableStateOf("") }
    var showDhikrPickerSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val allPresets = remember { TasbihPresetsData.items }
    val filteredPresets = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allPresets
        } else {
            val q = searchQuery.trim().lowercase()
            allPresets.filter {
                it.bengaliName.lowercase().contains(q) ||
                it.arabicText.contains(q) ||
                it.meaningBn.lowercase().contains(q) ||
                it.virtueBn.lowercase().contains(q)
            }
        }
    }

    // Active selected item
    val activeItem = state.selectedItem ?: allPresets.find {
        it.bengaliName == state.currentDhikr || "${it.bengaliName} (${it.arabicText})" == state.currentDhikr
    } ?: allPresets.first()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Row: ডিজিটাল তাসবীহ কাউন্টার vs ২৭টি পূর্ণ যিকির ও আমল তালিকা
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary,
            indicator = { tabPositions ->
                if (selectedTab < tabPositions.size) {
                    SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("স্মার্ট কাউন্টার", fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatListNumbered,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("সকল তাসবিহ (${allPresets.size})", fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            )
        }

        if (selectedTab == 0) {
            // ==================== TAB 0: COUNTER MODE ====================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Target Presets
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        listOf(33 to "৩৩ বার", 100 to "১০০ বার", 0 to "সীমাহীন").forEach { (target, label) ->
                            FilterChip(
                                selected = state.target == target,
                                onClick = { viewModel.setTasbihTarget(target) },
                                label = { Text(label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Dhikr selection chips horizontally with 'সকল তালিকা' quick button
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Quick Button to open full selector
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { showDhikrPickerSheet = true },
                            color = IslamicGold.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FormatListNumbered,
                                    contentDescription = "সকল তালিকা",
                                    tint = IslamicGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "সকল তাসবিহ (${allPresets.size}টি)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Presets chips
                        allPresets.forEach { item ->
                            val isSelected = activeItem.id == item.id
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.selectTasbihItem(item) },
                                label = {
                                    Text(
                                        text = item.bengaliName,
                                        fontSize = 12.sp,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Current Dhikr Active Display Card with Arabic, Meaning & Virtues
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                ) {
                                    Text(
                                        text = "বর্তমান তাসবিহ",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Copy
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val textToCopy = buildString {
                                                appendLine("【 ${activeItem.bengaliName} 】")
                                                if (activeItem.arabicText.isNotBlank()) appendLine("\n${activeItem.arabicText}")
                                                if (activeItem.meaningBn.isNotBlank()) appendLine("\nঅর্থ: ${activeItem.meaningBn}")
                                                if (activeItem.virtueBn.isNotBlank()) appendLine("\nফজিলত: ${activeItem.virtueBn}")
                                                appendLine("\n— দাওয়াহ টু জান্নাহ ডিজিটাল তাসবীহ")
                                            }
                                            clipboard.setPrimaryClip(ClipData.newPlainText("Dhikr", textToCopy))
                                            Toast.makeText(context, "যিকির কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "কপি",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Share
                                    IconButton(
                                        onClick = {
                                            val textToShare = buildString {
                                                appendLine("【 ${activeItem.bengaliName} 】")
                                                if (activeItem.arabicText.isNotBlank()) appendLine("\n${activeItem.arabicText}")
                                                if (activeItem.meaningBn.isNotBlank()) appendLine("\nঅর্থ: ${activeItem.meaningBn}")
                                                if (activeItem.virtueBn.isNotBlank()) appendLine("\nফজিলত: ${activeItem.virtueBn}")
                                                appendLine("\n— দাওয়াহ টু জান্নাহ ডিজিটাল তাসবীহ")
                                            }
                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_SUBJECT, activeItem.bengaliName)
                                                putExtra(Intent.EXTRA_TEXT, textToShare)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "যিকির শেয়ার করুন"))
                                        },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "শেয়ার",
                                            modifier = Modifier.size(16.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Bengali Title
                            Text(
                                text = activeItem.bengaliName,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )

                            // Arabic Script
                            if (activeItem.arabicText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(10.dp),
                                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.25f))
                                ) {
                                    Text(
                                        text = activeItem.arabicText,
                                        modifier = Modifier.padding(10.dp),
                                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp),
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 32.sp
                                    )
                                }
                            }

                            // Meaning
                            if (activeItem.meaningBn.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "অর্থ: ${activeItem.meaningBn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 18.sp
                                )
                            }

                            // Virtue
                            if (activeItem.virtueBn.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            IslamicGold.copy(alpha = 0.08f),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = activeItem.virtueBn,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Tactile Tap Button with Circular Progress
                    val progress = if (state.target > 0) (state.count.toFloat() / state.target).coerceIn(0f, 1f) else 1f

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(230.dp)
                    ) {
                        if (state.target > 0) {
                            CircularProgressIndicator(
                                progress = { progress },
                                modifier = Modifier.fillMaxSize(),
                                strokeWidth = 8.dp,
                                color = IslamicGold,
                                trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                            )
                        }

                        // Big Circular Tap Area
                        Box(
                            modifier = Modifier
                                .size(200.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.82f)
                                        )
                                    )
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    viewModel.incrementTasbih()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = CalendarHelper.toBanglaNumber(state.count),
                                    style = MaterialTheme.typography.displayMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                if (state.target > 0) {
                                    Text(
                                        text = "লক্ষ্য: ${CalendarHelper.toBanglaNumber(state.target)}",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = IslamicGoldLight
                                    )
                                } else {
                                    Text(
                                        text = "আনলিমিটেড",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = IslamicGoldLight
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "স্পর্শ করুন",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Total Counts & Actions
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "আজকের মোট জিকির গণনা:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "${CalendarHelper.toBanglaNumber(state.totalCount)} বার",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                OutlinedButton(
                                    onClick = { viewModel.resetTasbih() },
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Reset Count",
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "রিসেট",
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Vibration,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "প্রতিটি ট্যাপে মৃদু হ্যাপটিক ভাইব্রেশন ও টার্গেটে পৌঁছে দীর্ঘ কম্পন অনুভূত হবে।",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.padding(start = 6.dp)
                        )
                    }
                }
            }
        } else {
            // ==================== TAB 1: ALL 27 DHIKR/TASBIH LIST ====================
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Search box
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("যিকির, আরবি বা অর্থ খুঁজুন...") },
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
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Notice bar
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TouchApp,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "যেকোনো যিকিরে ট্যাপ করে সরাসরি কাউন্টারে লোড করুন এবং জিকির শুরু করুন।",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                itemsIndexed(filteredPresets, key = { _, item -> item.id }) { index, item ->
                    val isCurrent = activeItem.id == item.id
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 5.dp)
                            .clickable {
                                viewModel.selectTasbihItem(item)
                                selectedTab = 0 // Switch to counter
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCurrent) {
                                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                            } else {
                                MaterialTheme.colorScheme.surface
                            }
                        ),
                        border = BorderStroke(
                            width = if (isCurrent) 1.5.dp else 1.dp,
                            color = if (isCurrent) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary else IslamicGold.copy(alpha = 0.15f),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = CalendarHelper.toBanglaNumber(index + 1),
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isCurrent) Color.White else IslamicGold
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Text(
                                        text = item.bengaliName,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                if (isCurrent) {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = MaterialTheme.colorScheme.primary
                                    ) {
                                        Text(
                                            text = "সক্রিয়",
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            if (item.arabicText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                ) {
                                    Text(
                                        text = item.arabicText,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        lineHeight = 28.sp
                                    )
                                }
                            }

                            if (item.meaningBn.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "অর্থ: ${item.meaningBn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 18.sp
                                )
                            }

                            if (item.virtueBn.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "ফজিলত: ${item.virtueBn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = IslamicGold.copy(alpha = 0.12f)
                                ) {
                                    Text(
                                        text = "প্রস্তাবিত: ${CalendarHelper.toBanglaNumber(item.recommendedCount)} বার",
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        color = IslamicGold,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Copy
                                    IconButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val textToCopy = buildString {
                                                appendLine("【 ${item.bengaliName} 】")
                                                if (item.arabicText.isNotBlank()) appendLine("\n${item.arabicText}")
                                                if (item.meaningBn.isNotBlank()) appendLine("\nঅর্থ: ${item.meaningBn}")
                                                if (item.virtueBn.isNotBlank()) appendLine("\nফজিলত: ${item.virtueBn}")
                                                appendLine("\n— দাওয়াহ টু জান্নাহ ডিজিটাল তাসবীহ")
                                            }
                                            clipboard.setPrimaryClip(ClipData.newPlainText("Dhikr", textToCopy))
                                            Toast.makeText(context, "যিকির কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "কপি",
                                            modifier = Modifier.size(15.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    // Share
                                    IconButton(
                                        onClick = {
                                            val textToShare = buildString {
                                                appendLine("【 ${item.bengaliName} 】")
                                                if (item.arabicText.isNotBlank()) appendLine("\n${item.arabicText}")
                                                if (item.meaningBn.isNotBlank()) appendLine("\nঅর্থ: ${item.meaningBn}")
                                                if (item.virtueBn.isNotBlank()) appendLine("\nফজিলত: ${item.virtueBn}")
                                                appendLine("\n— দাওয়াহ টু জান্নাহ ডিজিটাল তাসবীহ")
                                            }
                                            val intent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_SUBJECT, item.bengaliName)
                                                putExtra(Intent.EXTRA_TEXT, textToShare)
                                            }
                                            context.startActivity(Intent.createChooser(intent, "যিকির শেয়ার করুন"))
                                        },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Share,
                                            contentDescription = "শেয়ার",
                                            modifier = Modifier.size(15.dp),
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Spacer(modifier = Modifier.width(6.dp))

                                    // Load to counter button
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.selectTasbihItem(item)
                                            selectedTab = 0
                                        },
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.TouchApp,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("কাউন্টারে নিন", fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    // Modal BottomSheet for Quick Selection
    if (showDhikrPickerSheet) {
        ModalBottomSheet(
            onDismissRequest = { showDhikrPickerSheet = false },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "তাসবিহ ও যিকির নির্বাচন করুন (${allPresets.size}টি)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { showDhikrPickerSheet = false }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    itemsIndexed(allPresets, key = { _, item -> item.id }) { idx, item ->
                        val isSelected = activeItem.id == item.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    viewModel.selectTasbihItem(item)
                                    coroutineScope.launch {
                                        sheetState.hide()
                                        showDhikrPickerSheet = false
                                    }
                                },
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = CalendarHelper.toBanglaNumber(idx + 1) + ".",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.width(28.dp)
                                )
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.bengaliName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (item.arabicText.isNotBlank()) {
                                        Text(
                                            text = item.arabicText,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }
    }
}
