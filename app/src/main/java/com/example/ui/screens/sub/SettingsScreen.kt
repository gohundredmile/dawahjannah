package com.example.ui.screens.sub

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.EnglishFont
import com.example.data.model.FontSizeScale
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle
import com.example.ui.theme.getBanglaFontFamily
import com.example.ui.theme.getEnglishFontFamily
import com.example.ui.theme.CosmicPrimaryLight
import com.example.ui.theme.EmeraldPrimaryLight
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LavenderPrimaryLight
import com.example.ui.theme.SagePrimaryLight
import com.example.ui.theme.SolarPrimaryLight
import com.example.ui.viewmodel.MainViewModel

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val currentThemeStyle by viewModel.themeStyle.collectAsState()
    val currentThemeMode by viewModel.themeMode.collectAsState()
    val currentFontScale by viewModel.fontScale.collectAsState()
    val currentEnglishFont by viewModel.englishFont.collectAsState()
    val currentBanglaFont by viewModel.banglaFont.collectAsState()
    val currentBanglaWeight by viewModel.banglaFontWeight.collectAsState()
    val isHanafiAsr by viewModel.isHanafiAsr.collectAsState()
    val updateMessage by viewModel.updateAlertMessage.collectAsState()
    val latestReleaseInfo by viewModel.latestReleaseInfo.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        // 1. THEME STYLES (AURORA & EMERALD)
        item {
            SettingSectionHeader(title = "অরোরা থিম ও রঙ শৈলী", icon = Icons.Default.ColorLens)
            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { viewModel.openThemeModal() },
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Palette,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "সকল অরোরা থিমসমূহ (১৫টি থিম দেখুন ও বাছাই করুন)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    listOf(
                        Triple(ThemeStyle.EMERALD_JANNAH, "এমেরাল্ড জান্নাহ (সবুজ ও স্বর্ণালী - মূল ইসলামিক থিম)", EmeraldPrimaryLight),
                        Triple(ThemeStyle.SAGE_WHISPER, "সেইজ হুইস্পার (স্নিগ্ধ মৃদু প্রকৃতি)", SagePrimaryLight),
                        Triple(ThemeStyle.COSMIC_AURORA, "কসমিক অরোরা (নীলকান্তমণি ও মহাকাশ)", CosmicPrimaryLight),
                        Triple(ThemeStyle.SOLAR_DAWN, "সোলার ডন (সোনালী উষা ও অ্যাম্বার)", SolarPrimaryLight),
                        Triple(ThemeStyle.LAVENDER_MIST, "ল্যাভেন্ডার মিস্ট (প্রশান্ত বেগুনি কুয়াশা)", LavenderPrimaryLight)
                    ).forEach { (style, name, color) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setThemeStyle(style) }
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = name,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (currentThemeStyle == style) FontWeight.Bold else FontWeight.Normal,
                                    color = if (currentThemeStyle == style) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }

                            if (currentThemeStyle == style) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 2. THEME MODE (LIGHT / DARK / SYSTEM)
        item {
            SettingSectionHeader(title = "ডিসপ্লে মোড (ডার্ক / লাইট)", icon = Icons.Default.DarkMode)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    ThemeMode.SYSTEM to "সিস্টেম",
                    ThemeMode.LIGHT to "লাইট মোড",
                    ThemeMode.DARK to "ডার্ক মোড"
                ).forEach { (mode, label) ->
                    FilterChip(
                        selected = currentThemeMode == mode,
                        onClick = { viewModel.setThemeMode(mode) },
                        label = { Text(label) },
                        modifier = Modifier.weight(1f),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 3. ENGLISH & BENGALI FONT SELECTION & WEIGHTS
        item {
            SettingSectionHeader(title = "ফন্ট ও টাইপোগ্রাফি স্টুডিও (Fonts & Typography)", icon = Icons.Default.FontDownload)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // English Font Header
                    Text(
                        text = "ইংরেজি ফন্ট (Clean, Stunning & Thin English Fonts):",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    EnglishFont.entries.forEach { font ->
                        val isSelected = currentEnglishFont == font
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setEnglishFont(font) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = font.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontFamily = getEnglishFontFamily(font),
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${font.subtitle} • 04:52 AM, September 2026",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (font != EnglishFont.entries.last()) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Bengali Font Header
                    Text(
                        text = "বাংলা ফন্ট (Bengali Fonts):",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    BanglaFont.entries.forEach { font ->
                        val isSelected = currentBanglaFont == font
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setBanglaFont(font) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = font.displayNameEn,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "${font.displayNameBn} — বিসমিল্লাহির রাহমানির রাহিম",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontFamily = getBanglaFontFamily(font),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        if (font != BanglaFont.entries.last()) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.08f),
                                modifier = Modifier.padding(vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "ফন্ট ওয়েট (Font Weight):",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            BanglaFontWeight.THIN,
                            BanglaFontWeight.LIGHT,
                            BanglaFontWeight.NORMAL,
                            BanglaFontWeight.SEMI_BOLD,
                            BanglaFontWeight.BOLD
                        ).forEach { weight ->
                            FilterChip(
                                selected = currentBanglaWeight == weight,
                                onClick = { viewModel.setBanglaFontWeight(weight) },
                                label = { Text(weight.label.split(" ").first(), fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { viewModel.openFontMenu() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("ফন্ট ও টাইপোগ্রাফি স্টুডিও খুলুন (সম্পূর্ণ প্রিভিউ)")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. BENGALI FONT SCALING
        item {
            SettingSectionHeader(title = "বাংলা ফন্ট স্কেলিং", icon = Icons.Default.FormatSize)
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FontSizeScale.entries.forEach { scale ->
                    FilterChip(
                        selected = currentFontScale == scale,
                        onClick = { viewModel.setFontScale(scale) },
                        label = { Text(scale.titleBn, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 4. JURISTIC CALCULATION (HANAFI / SHAFI'I)
        item {
            SettingSectionHeader(title = "নামাজের মাযহাবি হিসাব পদ্ধতি", icon = Icons.Default.Schedule)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHanafiAsr) "হানাফি মাযহাব (আসরের মিসলে আওয়াল/সানি)" else "শাফেয়ী / মালেকী / হাম্বলী মাযহাব",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isHanafiAsr) "আসরের ওয়াক্ত বস্তুর ছায়া দ্বিগুণ হওয়ার পর শুরু হয়।" else "আসরের ওয়াক্ত বস্তুর ছায়া সমপরিমাণ হওয়ার পর শুরু হয়।",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = isHanafiAsr,
                        onCheckedChange = { viewModel.setHanafiAsr(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 5. IN-APP PUSH UPDATE ENGINE
        item {
            SettingSectionHeader(title = "ইন-অ্যাপ কনটেন্ট আপডেট ইঞ্জিন", icon = Icons.Default.SystemUpdate)
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "GitHub রিলিজ ও নতুন অধ্যায় পুশ আপডেট",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "গিটহাব (GitHub Releases) থেকে সর্বশেষ APK সংস্করণ যাচাই করুন এবং নতুন রমাদান/হজ্জ ও জরুরি দোয়াসমূহ ওভার-দ্য-এয়ার সিঙ্ক করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedButton(
                        onClick = { viewModel.checkForAppUpdates() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.SystemUpdate,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("গিটহাব আপডেট ও সিঙ্ক চেক করুন")
                    }

                    if (latestReleaseInfo?.hasNewerVersion == true && latestReleaseInfo?.downloadUrl != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { viewModel.gitHubUpdateManager.openDownloadPage(latestReleaseInfo!!.downloadUrl!!) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("নতুন ভার্সন (${latestReleaseInfo!!.tagName}) ডাউনলোড করুন")
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // 6. ABOUT THE APP
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "দাওয়াহ টু জান্নাহ — দা'ওয়াহ টু জান্নাহ",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "ভার্সন ১.০.০ (রিলিজ সংস্করণ) • প্যাকেজ: com.dawahtojannah.app",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "একটি স্বয়ংসম্পূর্ণ, বিজ্ঞাপনহীন, ১০০% অফলাইন ইসলামিক জীবন ও আত্মিক সঙ্গী। সকল তথ্য নির্ভরযোগ্য ও সহীহ হাদিস গ্রন্থসমূহ থেকে চয়নকৃত।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }

    // Update Alert Dialog
    if (updateMessage != null) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissUpdateAlert() },
            title = { Text("আপডেট স্ট্যাটাস", fontWeight = FontWeight.Bold) },
            text = { Text(updateMessage ?: "") },
            confirmButton = {
                if (latestReleaseInfo?.hasNewerVersion == true && latestReleaseInfo?.downloadUrl != null) {
                    Button(onClick = {
                        viewModel.gitHubUpdateManager.openDownloadPage(latestReleaseInfo!!.downloadUrl!!)
                        viewModel.dismissUpdateAlert()
                    }) {
                        Text("ডাউনলোড করুন")
                    }
                } else {
                    Button(onClick = { viewModel.dismissUpdateAlert() }) {
                        Text("ঠিক আছে")
                    }
                }
            },
            dismissButton = if (latestReleaseInfo?.hasNewerVersion == true) {
                {
                    TextButton(onClick = { viewModel.dismissUpdateAlert() }) {
                        Text("পরে")
                    }
                }
            } else null
        )
    }
}

@Composable
private fun SettingSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
