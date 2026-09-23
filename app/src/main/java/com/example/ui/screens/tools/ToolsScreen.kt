package com.example.ui.screens.tools

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Spellcheck
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import java.text.NumberFormat
import java.util.Locale

data class IslamicToolItem(
    val id: String,
    val titleBn: String,
    val subtitleBn: String,
    val icon: ImageVector,
    val badgeBn: String,
    val isAvailable: Boolean = true,
    val isFeatured: Boolean = false,
    val onClick: () -> Unit = {}
)

@Composable
fun ToolsScreen(
    onOpenExplainAyahCamera: () -> Unit,
    onOpenSmartQuranSearch: () -> Unit = {},
    onOpenAskBeforeYouAct: () -> Unit = {},
    onOpenAyatDetector: () -> Unit,
    onOpenQibla: () -> Unit,
    onOpenTasbih: () -> Unit,
    onOpenNamesOfAllah: () -> Unit,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val banglaFont = LocalBanglaFontFamily.current

    // Zakat calculator dialog state
    var showZakatDialog by remember { mutableStateOf(false) }

    // Upcoming Tool Info dialog state
    var upcomingToolTitle by remember { mutableStateOf<String?>(null) }
    var upcomingToolDesc by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(
            top = contentPadding.calculateTopPadding() + 8.dp,
            bottom = contentPadding.calculateBottomPadding() + 24.dp,
            start = 16.dp,
            end = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // TOP FEATURED HERO BANNER: "Explain This Ayah" Camera
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, IslamicGold.copy(alpha = 0.65f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenExplainAyahCamera() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    IslamicGold.copy(alpha = 0.12f),
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        // Badge Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = IslamicGold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color.Black,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "নতুন ফিচার • স্মার্ট এআই ল্যাব",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.Black,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title
                        Text(
                            text = "\"Explain This Ayah\" ক্যামেরা",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Description
                        Text(
                            text = "পবিত্র কুরআনের যে কোনো আরবি পৃষ্ঠার ওপর ক্যামেরা ধরুন — মুহূর্তেই সনাক্ত হবে পূর্ণ আয়াত, হরকত, বাংলা ও ইংরেজি অর্থ, শব্দে শব্দে অর্থ (Word-by-word), তাফসীর, শানে নুযূল, সম্পর্কিত আয়াত, সহীহ হাদিস, অডিও ও হিফয অনুশীলন মোড।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Feature highlights tags
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("শব্দে শব্দে অর্থ", "তাফসীর ও শানে নুযূল", "হিফয লার্নার").forEach { tag ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Big Action Button
                        Button(
                            onClick = onOpenExplainAyahCamera,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ক্যামেরা স্ক্যানার চালু করুন",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // SIGNATURE SPOTLIGHT CARD: "Smart Quran Search" (Semantic AI)
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, Color(0xFF059669).copy(alpha = 0.65f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenSmartQuranSearch() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF059669).copy(alpha = 0.12f),
                                    IslamicGold.copy(alpha = 0.05f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        // Badge Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF059669),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "সিগনেচার ফিচার • সেমান্টিক এআই",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF059669).copy(alpha = 0.18f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = Color(0xFF059669),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title
                        Text(
                            text = "Smart Quran Search",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Description
                        Text(
                            text = "সাধারণ সার্চের মতো শুধু আক্ষরিক শব্দ নয় — মানুষের বাস্তব অনুভূতি, আবেগ বা সংকট লিখে খুঁজুন কুরআনের প্রাসঙ্গিক আয়াত, সহীহ অনুবাদ, তাফসীর ও প্রজ্ঞাপূর্ণ সমাধান।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Search Examples Pills
                        Text(
                            text = "জনপ্রিয় অনুসন্ধান উদাহরণ:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("নিরাশ হওয়া", "আল্লাহর ক্ষমা", "রাগ নিয়ন্ত্রণ").forEach { pill ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = pill,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Big Action Button
                        Button(
                            onClick = onOpenSmartQuranSearch,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF059669))
                        ) {
                            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "স্মার্ট কুরআন সার্চ চালু করুন",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // SIGNATURE SPOTLIGHT CARD: "Ask Before You Act" (Structured Jurisprudential Diagnostic)
        item {
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.5.dp, Color(0xFF4F46E5).copy(alpha = 0.65f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onOpenAskBeforeYouAct() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF4F46E5).copy(alpha = 0.12f),
                                    Color(0xFF059669).copy(alpha = 0.05f)
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        // Badge Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF4F46E5),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "অনন্য সিগনেচার ফিচার • ফিকহি বিশ্লেষণ",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF4F46E5).copy(alpha = 0.18f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = Color(0xFF4F46E5),
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Title
                        Text(
                            text = "Ask Before You Act",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Description
                        Text(
                            text = "কোনো আর্থিক পদক্ষেপ বা চুক্তিতে জড়ানোর আগে সরাসরি স্থূল হ্যাঁ/না নয় — বরং কাঠামোগত প্রশ্নের মাধ্যমে চুক্তির স্বরূপ, সুপ্ত সুদ, জরিমানা ও শর্তাবলি স্পষ্ট করে প্রামাণ্য কুরআন-সুন্নাহ ও ফিকহি উসূলভিত্তিক সমাধান জানুন।",
                            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick Search Examples Pills
                        Text(
                            text = "উদাহরণ ও ক্ষেত্রসমূহ:",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF4F46E5),
                            fontFamily = banglaFont
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("ঋণ ও ব্যাংক লোন", "শেয়ার বাজার ট্রেডিং", "ড্রপশিপিং", "জীবন বীমা").forEach { pill ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                                    border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = pill,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Big Action Button
                        Button(
                            onClick = onOpenAskBeforeYouAct,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "শরঈ অনুসন্ধান শুরু করুন",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontFamily = banglaFont
                            )
                        }
                    }
                }
            }
        }

        // SECTION: সক্রিয় ইসলামিক টুলস (Active Tools)
        item {
            Text(
                text = "সক্রিয় ইসলামিক টুলস ও ল্যাব",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = banglaFont,
                modifier = Modifier.padding(top = 8.dp, bottom = 2.dp)
            )
        }

        // Active tools list
        val activeTools = listOf(
            IslamicToolItem(
                id = "tool_ask_before_you_act",
                titleBn = "Ask Before You Act (পদক্ষেপ নেওয়ার আগে জানুন)",
                subtitleBn = "আর্থিক সিদ্ধান্ত বা চুক্তির পূর্বে কাঠামোগত শরঈ প্রশ্নমালা ও প্রামাণ্য দলিলভিত্তিক দিকনির্দেশনা।",
                icon = Icons.Default.Psychology,
                badgeBn = "সিগনেচার",
                isFeatured = true,
                onClick = onOpenAskBeforeYouAct
            ),
            IslamicToolItem(
                id = "tool_smart_quran_search",
                titleBn = "স্মার্ট কুরআন সার্চ (ভাবার্থভিত্তিক অনুসন্ধান)",
                subtitleBn = "বাংলা ভাষায় যে কোনো বিষয়, পরিস্থিতি বা আবেগ লিখে কুরআনের প্রাসঙ্গিক আয়াত ও সমাধান খুঁজুন।",
                icon = Icons.Default.Search,
                badgeBn = "এআই সার্চ",
                onClick = onOpenSmartQuranSearch
            ),
            IslamicToolItem(
                id = "tool_ayat_solver",
                titleBn = "আয়াত ও হাদীস শুদ্ধিকরণ ল্যাব",
                subtitleBn = "আরবি হরকত ও নুকতা শুদ্ধিকরণ, বাংলা/ইংরেজি বর্ণ অপসারণ ও সহীহ রেফারেন্স ম্যাচিং।",
                icon = Icons.Default.Spellcheck,
                badgeBn = "সক্রিয়",
                onClick = onOpenAyatDetector
            ),
            IslamicToolItem(
                id = "tool_qibla",
                titleBn = "ক্বিবলা কম্পাস ও দিক নির্দেশক",
                subtitleBn = "ডিভাইসের সেন্সর ও জিপিএস ব্যবহার করে পবিত্র কা'বা শরীফের সঠিক দিক ও কোণ।",
                icon = Icons.Default.Explore,
                badgeBn = "সক্রিয়",
                onClick = onOpenQibla
            ),
            IslamicToolItem(
                id = "tool_tasbih",
                titleBn = "ডিজিটাল তাসবীহ ও জিকির কাউন্টার",
                subtitleBn = "সালাত-পরবর্তী ১০০ তাসবীহ, সুবহানাল্লাহ, আলহামদুলিল্লাহ, আল্লাহু আকবার ও নিজস্ব লক্ষ্যমাত্রা।",
                icon = Icons.Default.Fingerprint,
                badgeBn = "সক্রিয়",
                onClick = onOpenTasbih
            ),
            IslamicToolItem(
                id = "tool_zakat",
                titleBn = "যাকাত ও নিসাব ক্যালকুলেটর",
                subtitleBn = "সোনা, রূপা, নগদ অর্থ ও ব্যবসায়িক সম্পদের ওপর শরীয়াহ সম্মত ২.৫% যাকাত গণনা।",
                icon = Icons.Default.Calculate,
                badgeBn = "ক্যালকুলেটর",
                onClick = { showZakatDialog = true }
            ),
            IslamicToolItem(
                id = "tool_allah_names",
                titleBn = "আল্লাহর ৯৯টি গুণবাচক নাম (আসমাউল হুসনা)",
                subtitleBn = "মহান আল্লাহর বরকতময় নামসমূহ, অর্থ, ব্যাখ্যা ও বিশুদ্ধ ফযিলত।",
                icon = Icons.Default.Stars,
                badgeBn = "সক্রিয়",
                onClick = onOpenNamesOfAllah
            )
        )

        items(activeTools.size) { index ->
            val tool = activeTools[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { tool.onClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.size(46.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = tool.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = tool.titleBn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = tool.badgeBn,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = tool.subtitleBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }

        // SECTION: আসন্ন নতুন ইসলামিক টুলস (Upcoming Tools)
        item {
            Text(
                text = "আসন্ন নতুন টুলস (শীঘ্রই আসছে)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary,
                fontFamily = banglaFont,
                modifier = Modifier.padding(top = 12.dp, bottom = 2.dp)
            )
        }

        val upcomingTools = listOf(
            IslamicToolItem(
                id = "up_fatwa",
                titleBn = "ইসলামিক ফতোয়া ও মাসআলা গাইড",
                subtitleBn = "চার মাজহাব ও সালাফে সালেহীনদের প্রামাণ্য দলীলভিত্তিক দৈনন্দিন মাসআলা নির্দেশিকা।",
                icon = Icons.Default.MenuBook,
                badgeBn = "আসন্ন",
                isAvailable = false,
                onClick = {
                    upcomingToolTitle = "ইসলামিক ফতোয়া ও মাসআলা গাইড"
                    upcomingToolDesc = "দারুল ইফতা ও প্রখ্যাত ফকীহদের নির্ভরযোগ্য কিতাব থেকে বিশুদ্ধ মাসআলা সার্চ ও ক্যাটাগরিভিত্তিক সমাধান যুক্ত হচ্ছে।"
                }
            ),
            IslamicToolItem(
                id = "up_audio_gen",
                titleBn = "কুরআন অডিও তিলাওয়াত জেনারেটর",
                subtitleBn = "পছন্দের ক্বারী, গতি ও পুনরাবৃত্তি নির্ধারণ করে নিজস্ব অফলাইন অডিও ফাইল তৈরি।",
                icon = Icons.Default.Tune,
                badgeBn = "আসন্ন",
                isAvailable = false,
                onClick = {
                    upcomingToolTitle = "কুরআন অডিও তিলাওয়াত জেনারেটর"
                    upcomingToolDesc = "বিশ্ববিখ্যাত ক্বারীদের কণ্ঠ একত্র করে নির্দিষ্ট আয়াতের জন্য অডিও ক্লিপ তৈরি ও ডাউনলোডের সুবিধা যুক্ত হচ্ছে।"
                }
            ),
            IslamicToolItem(
                id = "up_inheritance",
                titleBn = "মিরাস ও উত্তরাধিকার বণ্টন ক্যালকুলেটর",
                subtitleBn = "পবিত্র কুরআনের সূরা নিসার বিধি অনুযায়ী ওয়ারিশদের মধ্যে নিখুঁত সম্পত্তি বণ্টন হিসাব।",
                icon = Icons.Default.DateRange,
                badgeBn = "আসন্ন",
                isAvailable = false,
                onClick = {
                    upcomingToolTitle = "মিরাস ও উত্তরাধিকার বণ্টন ক্যালকুলেটর"
                    upcomingToolDesc = "পবিত্র কুরআন ও সুন্নাহর ফারায়িজ বিধি মোতাবেক সন্তান, পিতামাতা ও আত্মীয়দের মধ্যে স্বয়ংক্রিয় শতাংশ বণ্টন।"
                }
            )
        )

        items(upcomingTools.size) { index ->
            val upTool = upcomingTools[index]
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
                ),
                border = BorderStroke(0.6.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { upTool.onClick() }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = upTool.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = upTool.titleBn,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f)
                            ) {
                                Text(
                                    text = upTool.badgeBn,
                                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    fontFamily = banglaFont,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = upTool.subtitleBn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }
    }

    // Interactive Zakat Calculator Dialog
    if (showZakatDialog) {
        ZakatCalculatorDialog(
            onDismiss = { showZakatDialog = false },
            banglaFont = banglaFont
        )
    }

    // Upcoming Tool Dialog
    if (upcomingToolTitle != null) {
        AlertDialog(
            onDismissRequest = {
                upcomingToolTitle = null
                upcomingToolDesc = null
            },
            title = {
                Text(
                    text = upcomingToolTitle ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
            },
            text = {
                Text(
                    text = upcomingToolDesc ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontFamily = banglaFont
                )
            },
            confirmButton = {
                Button(onClick = {
                    upcomingToolTitle = null
                    upcomingToolDesc = null
                }) {
                    Text("ঠিক আছে", fontFamily = banglaFont)
                }
            }
        )
    }
}

/**
 * Interactive Zakat Calculator Dialog with Shariah Nisab calculation
 */
@Composable
private fun ZakatCalculatorDialog(
    onDismiss: () -> Unit,
    banglaFont: androidx.compose.ui.text.font.FontFamily?
) {
    var goldAmountStr by remember { mutableStateOf("") }
    var silverAmountStr by remember { mutableStateOf("") }
    var cashAmountStr by remember { mutableStateOf("") }
    var businessGoodsStr by remember { mutableStateOf("") }
    var debtStr by remember { mutableStateOf("") }

    val goldVal = goldAmountStr.toDoubleOrNull() ?: 0.0
    val silverVal = silverAmountStr.toDoubleOrNull() ?: 0.0
    val cashVal = cashAmountStr.toDoubleOrNull() ?: 0.0
    val businessVal = businessGoodsStr.toDoubleOrNull() ?: 0.0
    val debtVal = debtStr.toDoubleOrNull() ?: 0.0

    val totalAssets = (goldVal + silverVal + cashVal + businessVal) - debtVal
    val zakatPayable = if (totalAssets > 0) totalAssets * 0.025 else 0.0

    val formatter = remember { NumberFormat.getNumberInstance(Locale.getDefault()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "যাকাতুল মাল ক্যালকুলেটর",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "বন্ধ করুন")
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "এক বছর সঞ্চিত উদ্বৃত্ত সম্পদের ওপর শরীয়াহ অনুযায়ী ২.৫% যাকাত প্রযোজ্য।",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )

                OutlinedTextField(
                    value = cashAmountStr,
                    onValueChange = { cashAmountStr = it },
                    label = { Text("নগদ অর্থ ও ব্যাংক ব্যালেন্স (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = goldAmountStr,
                    onValueChange = { goldAmountStr = it },
                    label = { Text("স্বর্ণের বর্তমান মূল্য (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = businessGoodsStr,
                    onValueChange = { businessGoodsStr = it },
                    label = { Text("ব্যবসায়িক পণ্যের মূল্য (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = debtStr,
                    onValueChange = { debtStr = it },
                    label = { Text("তাত্ক্ষণিক পরিশোধযোগ্য ঋণ (টাকা)", fontFamily = banglaFont) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Calculated Results Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                    ),
                    border = BorderStroke(1.dp, IslamicGold),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "মোট যাকাতযোগ্য সম্পদ:",
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "৳ ${formatter.format(totalAssets.coerceAtLeast(0.0))}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "প্রদেয় যাকাত (২.৫%):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "৳ ${formatter.format(zakatPayable)}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = IslamicGold
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
            ) {
                Text("সম্পন্ন", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
            }
        }
    )
}
