package com.example.ui.screens.tools

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.datasource.MosqueDataCatalog
import com.example.data.model.MosqueAnnouncement
import com.example.data.model.MosqueCharityFund
import com.example.data.model.MosqueSalahStatus
import com.example.data.model.MosqueSalahTrackItem
import com.example.data.model.MosqueSurahItem
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import java.text.NumberFormat
import java.util.Locale

// --- 1. IN-MASJID QURAN READER DIALOG ---
@Composable
fun MosqueQuranReaderDialog(
    initialSurahId: String = "surah_kahf_first_10",
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var selectedSurahId by remember { mutableStateOf(initialSurahId) }
    val surahs = MosqueDataCatalog.mosqueSurahs
    val currentSurah = surahs.firstOrNull { it.id == selectedSurahId } ?: surahs.first()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = IslamicGold.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "পবিত্র কুরআন তিলাওয়াত",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "মসজিদে সুন্নাত ও ফযিলতপূর্ণ পাঠ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Surah Selection Chips
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(surahs) { surah ->
                        val isSelected = surah.id == selectedSurahId
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedSurahId = surah.id },
                            label = {
                                Text(
                                    text = surah.nameBn,
                                    fontFamily = banglaFont,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Surah Virtue Banner
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = currentSurah.nameAr,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "(${currentSurah.totalAyahs} আয়াত • ${currentSurah.readingReasonBn})",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = currentSurah.virtueHadithBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ayahs List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(currentSurah.verses) { verse ->
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
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
                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${verse.ayahNumber}",
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = verse.arabicText,
                                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 21.sp, lineHeight = 34.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Right,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "উচ্চারণ: ${verse.pronunciationBn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = banglaFont
                                )

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = "অর্থ: ${verse.meaningBn}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- 2. IN-MASJID ADHKAR READER DIALOG WITH COUNTER ---
@Composable
fun MosqueAdhkarDialog(onDismiss: () -> Unit) {
    val banglaFont = LocalBanglaFontFamily.current
    val adhkarList = MosqueDataCatalog.postSalahAdhkar
    var activeIndex by remember { mutableIntStateOf(0) }
    val currentDhikr = adhkarList[activeIndex]
    var tapCount by remember { mutableIntStateOf(0) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Top Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "সালাত-পরবর্তী মাসনূন আযকার",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "নামাজ সমাপ্তির পর রাসূলুল্লাহ ﷺ-এর আমল",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "বন্ধ করুন",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Dhikr Step Pills
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(adhkarList.size) { idx ->
                        val isSel = idx == activeIndex
                        FilterChip(
                            selected = isSel,
                            onClick = {
                                activeIndex = idx
                                tapCount = 0
                            },
                            label = {
                                Text(
                                    text = "${idx + 1}. ${adhkarList[idx].titleBn}",
                                    fontFamily = banglaFont,
                                    maxLines = 1
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Current Dhikr Big Display Card
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = currentDhikr.titleBn,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = currentDhikr.arabicText,
                            style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp, lineHeight = 34.sp),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "উচ্চারণ: ${currentDhikr.pronunciationBn}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "অর্থ: ${currentDhikr.meaningBn}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "রেফারেন্স: ${currentDhikr.referenceBn}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Big Tap Counter for this Dhikr
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f)
                    ),
                    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clickable {
                            if (tapCount < currentDhikr.repeatCount) {
                                tapCount++
                            } else {
                                if (activeIndex < adhkarList.size - 1) {
                                    activeIndex++
                                    tapCount = 0
                                }
                            }
                        }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$tapCount / ${currentDhikr.repeatCount}",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (tapCount >= currentDhikr.repeatCount) "সম্পন্ন! পরবর্তী যিকিরে ট্যাপ করুন" else "গণনা করতে স্ক্রিনে ট্যাপ করুন",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = if (tapCount >= currentDhikr.repeatCount) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Bottom Navigation Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedButton(
                        onClick = { tapCount = 0 },
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("রিসেট", fontFamily = banglaFont)
                    }

                    Button(
                        onClick = {
                            if (activeIndex < adhkarList.size - 1) {
                                activeIndex++
                                tapCount = 0
                            } else {
                                onDismiss()
                            }
                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = if (activeIndex < adhkarList.size - 1) "পরবর্তী যিকির" else "সম্পন্ন",
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }
}

// --- 3. PRAYER & JAMAT GUIDE DIALOG ---
@Composable
fun MosquePrayerGuideDialog(onDismiss: () -> Unit) {
    val banglaFont = LocalBanglaFontFamily.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "সালাত ও জামা'আত আদব গাইড",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont
            )
        },
        text = {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "১. তাহিয়্যাতুল মসজিদ (مسجد التحية):",
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "মসজিদে প্রবেশ করে বসার পূর্বে ২ রাকাত নফল সালাত আদায় করা সুন্নাত। রাসূলুল্লাহ ﷺ বলেছেন: «যখন তোমাদের কেউ মসজিদে প্রবেশ করে, সে যেন বসার পূর্বে দুই রাকাত সালাত আদায় করে»। (সহীহ বুখারী ৪৪৪)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }

                item {
                    Text(
                        text = "২. তাকবীরে তাহরীমার ফযীলত:",
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "রাসূলুল্লাহ ﷺ বলেছেন: «যে ব্যক্তি আল্লাহর উদ্দেশ্যে চল্লিশ দিন প্রথম তাকবীরের সাথে জামা'আতে সালাত আদায় করবে, তার জন্য দুটি মুক্তি পরোয়ানা লেখা হবে— জাহান্নাম থেকে মুক্তি এবং মুনাফেক্বী থেকে মুক্তি»। (সুনানে তিরমিযী ২৪১)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }

                item {
                    Text(
                        text = "৩. কাতার সোজা ও ফাঁকা পূরণ করার বিধান:",
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "কাতারের প্রথম ফাঁকগুলো আগে পূরণ করতে হবে। পায়ের গোড়ালি এবং কাঁধ বরাবর রেখে সোজা হতে হবে। রাসূলুল্লাহ ﷺ বলেছেন: «কাতার সোজা করো, কারণ কাতার সোজা করা সালাত পূর্ণাঙ্গ হওয়ার অন্যতম শর্ত»। (সহীহ বুখারী ৭২৩)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }

                item {
                    Text(
                        text = "৪. মাসবুকের নামায (দেরিতে যোগদানের নিয়ম):",
                        fontWeight = FontWeight.Bold,
                        color = IslamicGold,
                        style = MaterialTheme.typography.titleSmall,
                        fontFamily = banglaFont
                    )
                    Text(
                        text = "ইমামের সাথে যে রাকাতগুলোতে পাওয়া যায় তা আদায় করবেন। ইমাম উভয় সালাম ফেরানোর পর নিজে সালাম না ফিরিয়ে দাঁড়িয়ে যাবেন এবং ছুটে যাওয়া রাকাতগুলো তিলাওয়াত ও তাশাহহুদ সহ যথানিয়মে পূর্ণ করবেন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("বুঝেছি", fontFamily = banglaFont)
            }
        }
    )
}

// --- 4. DAILY PRAYER TRACKER DIALOG ---
@Composable
fun MosquePrayerTrackerDialog(
    currentStatuses: Map<String, MosqueSalahStatus>,
    onStatusChange: (String, MosqueSalahStatus) -> Unit,
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    val waqts = listOf(
        Triple("FAJR", "ফজর", "সুবহে সাদিক • ২ রাকাত ফরজ + ২ সুন্নত"),
        Triple("DHUHR", "যুহর / জুমু'আহ", "দ্বিপ্রহর • ৪ রাকাত ফরজ + সুন্নত"),
        Triple("ASR", "আসর", "অপরাহ্ন • ৪ রাকাত ফরজ"),
        Triple("MAGHRIB", "মাগরিব", "সূর্যাস্ত • ৩ রাকাত ফরজ + ২ সুন্নত"),
        Triple("ISHA", "এশা ও বিতর", "রাত্রি • ৪ রাকাত ফরজ + ৩ বিতর"),
        Triple("TAHAJJUD", "তাহাজ্জুদ", "রাত্রির শেষ তৃতীয়াংশ • ২-৮ রাকাত নফল")
    )

    val localMap = remember {
        mutableStateMapOf<String, MosqueSalahStatus>().apply {
            putAll(currentStatuses)
        }
    }

    val completedCount = localMap.values.count { it != MosqueSalahStatus.NOT_RECORDED }
    val jamatCount = localMap.values.count { it == MosqueSalahStatus.JAMAT || it == MosqueSalahStatus.JAMAT_WITH_TAKBIR }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "দৈনিক সালাত ও জামা'আত ট্র্যাকার",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "জামা'আতের সালাত একাকী সালাতের চেয়ে ২৭ গুণ শ্রেষ্ঠ",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF10B981),
                            fontFamily = banglaFont
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Banner
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "আদায়কৃত সালাত",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "$completedCount / 5",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                        }

                        Box(
                            modifier = Modifier
                                .width(1.dp)
                                .height(30.dp)
                                .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        )

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "জামা'আতে আদায়",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "$jamatCount ওয়াক্ত",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Waqt List
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(waqts) { (key, name, desc) ->
                        val current = localMap[key] ?: MosqueSalahStatus.NOT_RECORDED
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(
                                1.dp,
                                if (current != MosqueSalahStatus.NOT_RECORDED) Color(0xFF10B981).copy(alpha = 0.5f)
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = name,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = when (current) {
                                            MosqueSalahStatus.JAMAT_WITH_TAKBIR -> IslamicGold.copy(alpha = 0.2f)
                                            MosqueSalahStatus.JAMAT -> Color(0xFF10B981).copy(alpha = 0.2f)
                                            MosqueSalahStatus.INDIVIDUAL -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                            else -> MaterialTheme.colorScheme.surfaceVariant
                                        }
                                    ) {
                                        Text(
                                            text = current.titleBn,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = when (current) {
                                                MosqueSalahStatus.JAMAT_WITH_TAKBIR -> IslamicGold
                                                MosqueSalahStatus.JAMAT -> Color(0xFF10B981)
                                                MosqueSalahStatus.INDIVIDUAL -> MaterialTheme.colorScheme.primary
                                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                            fontFamily = banglaFont,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = desc,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Status Chips
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    FilterChip(
                                        selected = current == MosqueSalahStatus.JAMAT_WITH_TAKBIR,
                                        onClick = {
                                            val next = if (current == MosqueSalahStatus.JAMAT_WITH_TAKBIR) MosqueSalahStatus.NOT_RECORDED else MosqueSalahStatus.JAMAT_WITH_TAKBIR
                                            localMap[key] = next
                                            onStatusChange(key, next)
                                        },
                                        label = { Text("তাকবীরসহ", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont) }
                                    )
                                    FilterChip(
                                        selected = current == MosqueSalahStatus.JAMAT,
                                        onClick = {
                                            val next = if (current == MosqueSalahStatus.JAMAT) MosqueSalahStatus.NOT_RECORDED else MosqueSalahStatus.JAMAT
                                            localMap[key] = next
                                            onStatusChange(key, next)
                                        },
                                        label = { Text("জামা'আত", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont) }
                                    )
                                    FilterChip(
                                        selected = current == MosqueSalahStatus.INDIVIDUAL,
                                        onClick = {
                                            val next = if (current == MosqueSalahStatus.INDIVIDUAL) MosqueSalahStatus.NOT_RECORDED else MosqueSalahStatus.INDIVIDUAL
                                            localMap[key] = next
                                            onStatusChange(key, next)
                                        },
                                        label = { Text("একাকী", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("সংরক্ষণ সম্পন্ন", fontFamily = banglaFont)
                }
            }
        }
    }
}

// --- 5. ADD CUSTOM ANNOUNCEMENT DIALOG ---
@Composable
fun MosqueAddAnnouncementDialog(
    onSave: (MosqueAnnouncement) -> Unit,
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("নোটিশ") }
    var desc by remember { mutableStateOf("") }
    var isUrgent by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "নতুন মসজিদ নোটিশ প্রকাশ",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("ঘোষণার শিরোনাম", fontFamily = banglaFont) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("বিস্তারিত বিবরণ", fontFamily = banglaFont) },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("নোটিশ", "দ্বীনি মাহফিল", "উন্নয়ন", "রমাদান").forEach { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat, fontFamily = banglaFont) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank()) {
                        val ann = MosqueAnnouncement(
                            id = "custom_${System.currentTimeMillis()}",
                            titleBn = title.trim(),
                            categoryBn = category,
                            dateBn = "আজকের নোটিশ",
                            descriptionBn = desc.trim().ifEmpty { "মসজিদ পরিচালনা কমিটির সাধারণ ঘোষণা।" },
                            isUrgent = isUrgent
                        )
                        onSave(ann)
                        onDismiss()
                    }
                },
                enabled = title.isNotBlank()
            ) {
                Text("প্রকাশ করুন", fontFamily = banglaFont)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("বাতিল", fontFamily = banglaFont)
            }
        }
    )
}

// --- 6. CHARITY DONATION RECORD DIALOG ---
@Composable
fun MosqueCharityDonationDialog(
    fund: MosqueCharityFund,
    onConfirmDonation: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var selectedAmount by remember { mutableStateOf("500") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = fund.titleBn,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                fontFamily = banglaFont
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = fund.descriptionBn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                Text(
                    text = fund.accountInfoBn,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = IslamicGold,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "সাদাকাহর পরিমাণ নির্বাচন করুন (টাকা):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("100", "500", "1000", "5000").forEach { amt ->
                        FilterChip(
                            selected = selectedAmount == amt,
                            onClick = { selectedAmount = amt },
                            label = { Text("৳$amt", fontFamily = banglaFont) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amountLong = selectedAmount.toLongOrNull() ?: 500L
                    onConfirmDonation(amountLong)
                    onDismiss()
                }
            ) {
                Text("দান সম্পন্ন করেছি", fontFamily = banglaFont)
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("বাতিল", fontFamily = banglaFont)
            }
        }
    )
}
