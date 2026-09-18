package com.example.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.NofolCategory
import com.example.data.model.NofolSalatItem
import com.example.data.model.NofolSalatRepository
import com.example.data.model.SalatConfiguration
import com.example.util.PrayerCalculator

@Composable
fun NofolSalatTimingsSection(
    prayerStatus: PrayerCalculator.PrayerStatus,
    salatConfig: SalatConfiguration = SalatConfiguration(),
    onOpenNofolDetail: (NofolSalatItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val allSalats = remember(prayerStatus) {
        NofolSalatRepository.getAllNofolSalats(prayerStatus)
    }

    var selectedFilterIndex by remember { mutableIntStateOf(0) }
    val filterTabs = listOf("সবগুলো (${allSalats.size})", "দৈনিক সময়ভিত্তিক", "প্রয়োজন ও আমল", "বিশেষ ইবাদত")

    val filteredList = remember(selectedFilterIndex, allSalats) {
        when (selectedFilterIndex) {
            1 -> allSalats.filter { it.category == NofolCategory.DAILY_TIMED }
            2 -> allSalats.filter { it.category == NofolCategory.OCCASION_NEED }
            3 -> allSalats.filter { it.category == NofolCategory.SPECIAL_WORSHIP }
            else -> allSalats
        }
    }

    // Determine if any time-bound nafl is currently active
    val activeNofolItem = remember(prayerStatus.presentNofolNameBn, allSalats) {
        allSalats.firstOrNull {
            it.nameBn.contains(prayerStatus.presentNofolNameBn, ignoreCase = true) ||
            prayerStatus.presentNofolNameBn.contains(it.nameBn.replace("সালাতুত ", "").replace("সালাতুল ", ""), ignoreCase = true)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF047857))
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "নফল সালাতের সময়সূচী",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF047857),
                    fontSize = 17.5.sp
                )
            }

            Surface(
                color = Color(0xFF047857).copy(alpha = 0.12f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFF047857),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "সহীহ সুন্নাহ",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF047857),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "সহীহ হাদিস ও নির্ভরযোগ্য সূত্রের আলোকে নফল নামাজের সময়, নিয়ম ও দলিল",
            fontSize = 11.5.sp,
            color = Color(0xFF64748B),
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Active Nofol Highlight Banner (if any is active right now)
        activeNofolItem?.let { activeItem ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
                    .clickable { onOpenNofolDetail(activeItem) },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFECFDF5)
                ),
                border = BorderStroke(1.dp, Color(0xFF6EE7B7))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFF047857),
                            shape = CircleShape,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Spa,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "বর্তমানে আদায়যোগ্য:",
                                    fontSize = 10.5.sp,
                                    color = Color(0xFF047857),
                                    fontWeight = FontWeight.Medium
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = activeItem.nameBn,
                                    fontSize = 13.5.sp,
                                    color = Color(0xFF065F46),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = activeItem.timingSummaryBn,
                                fontSize = 11.sp,
                                color = Color(0xFF047857)
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFF047857),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "বিস্তারিত",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
        }

        // Filter Tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            filterTabs.forEachIndexed { idx, label ->
                val isSelected = idx == selectedFilterIndex
                Surface(
                    color = if (isSelected) Color(0xFF047857) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) Color(0xFF047857) else Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedFilterIndex = idx }
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isSelected) Color.White else Color(0xFF475569),
                            textAlign = TextAlign.Center,
                            maxLines = 1
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Vertical List of Thin Nofol Salat Cards
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            filteredList.forEach { nofolItem ->
                NofolSalatThinCard(
                    item = nofolItem,
                    onClick = { onOpenNofolDetail(nofolItem) }
                )
            }
        }
    }
}

@Composable
fun NofolSalatThinCard(
    item: NofolSalatItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categoryColor = when (item.category) {
        NofolCategory.DAILY_TIMED -> Color(0xFF047857) // Emerald
        NofolCategory.OCCASION_NEED -> Color(0xFF0284C7) // Sky blue
        NofolCategory.SPECIAL_WORSHIP -> Color(0xFF7C3AED) // Purple
    }

    val bgColor = when (item.category) {
        NofolCategory.DAILY_TIMED -> Color(0xFFF0FDF4)
        NofolCategory.OCCASION_NEED -> Color(0xFFF0F9FF)
        NofolCategory.SPECIAL_WORSHIP -> Color(0xFFFAF5FF)
    }

    val borderColor = when (item.category) {
        NofolCategory.DAILY_TIMED -> Color(0xFFBBF7D0)
        NofolCategory.OCCASION_NEED -> Color(0xFFBAE6FD)
        NofolCategory.SPECIAL_WORSHIP -> Color(0xFFE9D5FF)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        border = BorderStroke(1.1.dp, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 9.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Side: Dot Indicator + Salat Name + Category Pill
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.5.dp)
                        .clip(CircleShape)
                        .background(categoryColor)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = item.nameBn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    fontSize = 15.sp
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    color = categoryColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = item.rakatsSummaryBn.split("(").firstOrNull()?.trim() ?: "২ রাকাত",
                        color = categoryColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Right Side: Starting time / timing summary + Chevron
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (item.isTimeBound) item.startingTimeBn else item.timingSummaryBn.take(20),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    fontSize = 11.5.sp,
                    color = Color(0xFF334155),
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "বিস্তারিত",
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun NofolSalatDetailsDialog(
    initialItem: NofolSalatItem,
    allSalats: List<NofolSalatItem> = emptyList(),
    prayerStatus: PrayerCalculator.PrayerStatus? = null,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    var currentItem by remember { mutableStateOf(initialItem) }
    var fontScale by remember { mutableFloatStateOf(1.0f) }

    val salatList = remember(allSalats, prayerStatus) {
        if (allSalats.isNotEmpty()) allSalats
        else if (prayerStatus != null) NofolSalatRepository.getAllNofolSalats(prayerStatus)
        else listOf(initialItem)
    }

    val currentIndex = salatList.indexOfFirst { it.id == currentItem.id }.coerceAtLeast(0)
    val hasPrev = currentIndex > 0
    val hasNext = currentIndex < salatList.size - 1

    fun shareSalatInfo(item: NofolSalatItem) {
        val shareText = buildString {
            append("🕌 ${item.nameBn} (${item.nameAr})\n")
            append("━━━━━━━━━━━━━━━━━━━━\n")
            append("⏱️ ওয়াক্ত / সময়: ${item.timingSummaryBn}\n")
            append("📿 রাকাত: ${item.rakatsSummaryBn}\n")
            append("🌟 সর্বোত্তম সময়: ${item.bestTimeBn}\n\n")
            append("📖 সালাত আদায়ের নিয়ম:\n${item.rulesAndMethodBn}\n\n")
            append("📜 নির্ভরযোগ্য হাদিসের দলিল:\n${item.hadithReferenceBn}\n")
            append("— [${item.hadithSourceBn}]\n\n")
            append("💎 ফযিলত:\n${item.virtuesBn}\n")
            item.specialDuaArabic?.let { dua ->
                append("\n🤲 বিশেষ দো'আ:\n$dua\n")
                item.specialDuaTransliteration?.let { append("উচ্চারণ: $it\n") }
                item.specialDuaMeaningBn?.let { append("অর্থ: $it\n") }
            }
            append("\n📲 দা'ওয়াহ টু জান্নাহ অ্যাপ থেকে শেয়ারকৃত")
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(sendIntent, "${item.nameBn} শেয়ার করুন"))
    }

    fun copyToClipboard(label: String, text: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, text)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "$label কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .shadow(16.dp, RoundedCornerShape(24.dp)),
            shape = RoundedCornerShape(24.dp),
            color = Color(0xFFFAFAFA)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // DIALOG TOP BAR
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF065F46), Color(0xFF047857))
                            )
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Category Tag
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = currentItem.category.labelBn,
                                    color = Color.White,
                                    fontSize = (11 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }

                            // Right action buttons: A-, A+, Share, Close
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable {
                                        if (fontScale > 0.85f) fontScale -= 0.1f
                                    }
                                ) {
                                    Text(
                                        text = "A-",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Surface(
                                    color = Color.White.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.clickable {
                                        if (fontScale < 1.35f) fontScale += 0.1f
                                    }
                                ) {
                                    Text(
                                        text = "A+",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                IconButton(
                                    onClick = { shareSalatInfo(currentItem) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(2.dp))
                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Salat Title & Arabic Calligraphy
                        Text(
                            text = currentItem.nameBn,
                            fontSize = (21 * fontScale).sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = currentItem.nameAr,
                            fontSize = (15 * fontScale).sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFFD1FAE5)
                        )
                        Text(
                            text = currentItem.nameEn,
                            fontSize = (11.5 * fontScale).sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                // DIALOG CONTENT BODY (Scrollable)
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Quick Metric Cards: Timing & Rakats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFECFDF5)),
                            border = BorderStroke(1.dp, Color(0xFFA7F3D0))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = Color(0xFF047857),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "সময়কাল",
                                        fontSize = (11 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF047857)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentItem.timingSummaryBn,
                                    fontSize = (12.5 * fontScale).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF065F46)
                                )
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4)),
                            border = BorderStroke(1.dp, Color(0xFFBBF7D0))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Spa,
                                        contentDescription = null,
                                        tint = Color(0xFF15803D),
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "রাকাত সংখ্যা",
                                        fontSize = (11 * fontScale).sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF15803D)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = currentItem.rakatsSummaryBn,
                                    fontSize = (12.5 * fontScale).sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF166534)
                                )
                            }
                        }
                    }

                    // Card 1: ওয়াক্ত ও সময়সূচী (Timing & Best Time)
                    NofolDetailCard(
                        title = "ওয়াক্ত ও সর্বোত্তম সময়",
                        icon = Icons.Default.AccessTime,
                        headerColor = Color(0xFF047857),
                        fontScale = fontScale
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "• সময়সীমা: ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    fontSize = (13 * fontScale).sp
                                )
                                Text(
                                    text = "${currentItem.startingTimeBn} থেকে ${currentItem.endingTimeBn}",
                                    color = Color(0xFF1E293B),
                                    fontSize = (13 * fontScale).sp
                                )
                            }
                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "• উত্তম সময়: ",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF047857),
                                    fontSize = (13 * fontScale).sp
                                )
                                Text(
                                    text = currentItem.bestTimeBn,
                                    color = Color(0xFF065F46),
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = (13 * fontScale).sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentItem.detailedTimingBn,
                                color = Color(0xFF334155),
                                fontSize = (13 * fontScale).sp,
                                lineHeight = (20 * fontScale).sp
                            )
                        }
                    }

                    // Card 2: সালাত আদায়ের নিয়ম ও পদ্ধতি
                    NofolDetailCard(
                        title = "রাকাত ও আদায়ের সুন্নাহ পদ্ধতি",
                        icon = Icons.Default.MenuBook,
                        headerColor = Color(0xFF0284C7),
                        fontScale = fontScale
                    ) {
                        Text(
                            text = currentItem.rulesAndMethodBn,
                            color = Color(0xFF1E293B),
                            fontSize = (13.5 * fontScale).sp,
                            lineHeight = (21 * fontScale).sp
                        )
                    }

                    // Card 3: সহীহ হাদিসের নির্ভরযোগ্য দলিল ও সনদ
                    NofolDetailCard(
                        title = "সহীহ হাদিসের নির্ভরযোগ্য দলিল",
                        icon = Icons.Default.AutoAwesome,
                        headerColor = Color(0xFFD97706),
                        fontScale = fontScale
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(
                                color = Color(0xFFFEF3C7).copy(alpha = 0.6f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = currentItem.hadithReferenceBn,
                                    color = Color(0xFF78350F),
                                    fontSize = (13.5 * fontScale).sp,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = (21 * fontScale).sp,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "— সূত্র: ${currentItem.hadithSourceBn}",
                                    fontSize = (12 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFB45309)
                                )
                            }
                        }
                    }

                    // Card 4: ফযিলত ও মর্যাদা
                    NofolDetailCard(
                        title = "ফযিলত ও আত্মিক গুরুত্ব",
                        icon = Icons.Default.Spa,
                        headerColor = Color(0xFF7C3AED),
                        fontScale = fontScale
                    ) {
                        Text(
                            text = currentItem.virtuesBn,
                            color = Color(0xFF1E293B),
                            fontSize = (13.5 * fontScale).sp,
                            lineHeight = (21 * fontScale).sp
                        )
                    }

                    // Card 5: বিশেষ দো‘আ ও যিকির (যদি থাকে)
                    currentItem.specialDuaArabic?.let { duaArabic ->
                        NofolDetailCard(
                            title = "বিশেষ দো‘আ ও যিকির",
                            icon = Icons.Default.NightsStay,
                            headerColor = Color(0xFF0D9488),
                            fontScale = fontScale,
                            onCopy = {
                                val fullDua = "$duaArabic\n\n${currentItem.specialDuaTransliteration ?: ""}\n\n${currentItem.specialDuaMeaningBn ?: ""}"
                                copyToClipboard("${currentItem.nameBn} দো'আ", fullDua)
                            }
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                Surface(
                                    color = Color(0xFFCCFBF1).copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(10.dp),
                                    border = BorderStroke(1.dp, Color(0xFF99F6E4)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Text(
                                            text = duaArabic,
                                            color = Color(0xFF115E59),
                                            fontSize = (17 * fontScale).sp,
                                            fontWeight = FontWeight.Bold,
                                            textAlign = TextAlign.Right,
                                            lineHeight = (26 * fontScale).sp,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }

                                currentItem.specialDuaTransliteration?.let { trans ->
                                    Column {
                                        Text(
                                            text = "উচ্চারণ:",
                                            fontSize = (11.5 * fontScale).sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E)
                                        )
                                        Text(
                                            text = trans,
                                            fontSize = (13 * fontScale).sp,
                                            color = Color(0xFF334155),
                                            lineHeight = (19 * fontScale).sp
                                        )
                                    }
                                }

                                currentItem.specialDuaMeaningBn?.let { meaning ->
                                    Column {
                                        Text(
                                            text = "অর্থ ও অনুবাদ:",
                                            fontSize = (11.5 * fontScale).sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F766E)
                                        )
                                        Text(
                                            text = meaning,
                                            fontSize = (13 * fontScale).sp,
                                            color = Color(0xFF334155),
                                            lineHeight = (19 * fontScale).sp
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Card 6: সতর্কতা ও নিষিদ্ধ সময়
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                        border = BorderStroke(1.dp, Color(0xFFFECACA))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFDC2626),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "জরুরি সতর্কতা (সালাতের নিষিদ্ধ সময়):",
                                    fontSize = (12.5 * fontScale).sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF991B1B)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = currentItem.cautionsBn,
                                    fontSize = (12 * fontScale).sp,
                                    color = Color(0xFF7F1D1D),
                                    lineHeight = (17 * fontScale).sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }

                HorizontalDivider(color = Color(0xFFE2E8F0))

                // BOTTOM NAVIGATION BAR (Previous & Next Salat switcher)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (hasPrev) {
                        Surface(
                            color = Color(0xFFF1F5F9),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFCBD5E1)),
                            modifier = Modifier.clickable {
                                currentItem = salatList[currentIndex - 1]
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Previous",
                                    tint = Color(0xFF475569),
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = salatList[currentIndex - 1].nameBn.replace("সালাতুত ", "").replace("সালাতুল ", ""),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF334155)
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    Text(
                        text = "${currentIndex + 1} / ${salatList.size}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )

                    if (hasNext) {
                        Surface(
                            color = Color(0xFF047857),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.clickable {
                                currentItem = salatList[currentIndex + 1]
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = salatList[currentIndex + 1].nameBn.replace("সালাতুত ", "").replace("সালাতুল ", ""),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next",
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.width(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun NofolDetailCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    headerColor: Color,
    fontScale: Float = 1f,
    onCopy: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = headerColor,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = title,
                        fontSize = (14 * fontScale).sp,
                        fontWeight = FontWeight.Bold,
                        color = headerColor
                    )
                }

                onCopy?.let { copyAction ->
                    IconButton(
                        onClick = copyAction,
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = headerColor,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}
