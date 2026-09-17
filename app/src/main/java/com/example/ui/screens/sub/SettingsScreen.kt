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
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import com.example.data.model.BanglaFont
import com.example.data.model.BanglaFontWeight
import com.example.data.model.EnglishFont
import com.example.data.model.FlipClockFont
import com.example.data.model.FontSizeScale
import com.example.data.model.ThemeMode
import com.example.data.model.ThemeStyle
import com.example.ui.theme.getBanglaFontFamily
import com.example.ui.theme.getEnglishFontFamily
import com.example.ui.theme.getFlipClockFontFamily
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
    val currentFlipClockFont by viewModel.flipClockFont.collectAsState()
    val isHanafiAsr by viewModel.isHanafiAsr.collectAsState()
    val updateMessage by viewModel.updateAlertMessage.collectAsState()
    val latestReleaseInfo by viewModel.latestReleaseInfo.collectAsState()
    val isDownloadingUpdate by viewModel.isDownloadingUpdate.collectAsState()
    val apkDownloadState by viewModel.apkDownloadState.collectAsState()

    var showRepoConfigDialog by remember { mutableStateOf(false) }
    var showSyncHelpDialog by remember { mutableStateOf(false) }
    var showUsbGuideDialog by remember { mutableStateOf(false) }
    var inputOwner by remember { mutableStateOf(viewModel.gitHubUpdateManager.repoOwner) }
    var inputRepo by remember { mutableStateOf(viewModel.gitHubUpdateManager.repoName) }

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
                    // Dual active fonts badge banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💡 যুগপৎ সক্রিয় ফন্ট: ইংরেজি ফন্ট নির্বাচন শুধুমাত্র ইংরেজি লেখা ও সংখ্যার ওপর প্রভাব ফেলবে এবং বাংলা ফন্ট নির্বাচন শুধুমাত্র বাংলা হরফের ওপর প্রভাব ফেলবে। উভয় ফন্ট একই সাথে সক্রিয় থাকে।",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.5.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))

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
                                    fontFamily = getEnglishFontFamily(font),
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
                                    text = font.displayNameBn,
                                    fontFamily = getBanglaFontFamily(font),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "বিসমিল্লাহির রাহমানির রাহিম • ${font.displayNameEn}",
                                    fontFamily = getBanglaFontFamily(font),
                                    style = MaterialTheme.typography.bodySmall,
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

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Dedicated Flip Clock Typography Section
                    Text(
                        text = "ফ্লিপ ক্লক ফন্ট (HTC Sense Flip Clock - ১০+ থিন ও লাইট ফন্ট):",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF1E2430),
                        border = BorderStroke(1.dp, Color(0xFF334155)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.openFontMenu() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = currentFlipClockFont.displayName,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFF1F5F9)
                                )
                                Text(
                                    text = "${currentFlipClockFont.subtitle} • ${currentFlipClockFont.googleFontName}",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF0F172A),
                                border = BorderStroke(0.5.dp, Color(0xFF475569))
                            ) {
                                Text(
                                    text = "08:45",
                                    fontFamily = getFlipClockFontFamily(currentFlipClockFont),
                                    fontWeight = currentFlipClockFont.fontWeight,
                                    fontSize = 15.sp,
                                    color = Color(0xFFF8FAFC),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
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
                        text = "গিটহাব (GitHub Releases ও Raw JSON) থেকে সর্বশেষ APK ও নতুন অধ্যায় ওভার-দ্য-এয়ার সিঙ্ক করুন।",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Current Repo Target & Real Installed Version Badge
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "ইনস্টলড অ্যাপ ভার্সন: v${viewModel.installedAppVersionName} (কোড: ${viewModel.installedAppVersionCode})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "কনটেন্ট ভার্সন: v${viewModel.appliedContentVersion} • রিপো: github.com/${viewModel.gitHubUpdateManager.repoOwner}/${viewModel.gitHubUpdateManager.repoName}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Action buttons: Configure, USB Guide & Help
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                inputOwner = viewModel.gitHubUpdateManager.repoOwner
                                inputRepo = viewModel.gitHubUpdateManager.repoName
                                showRepoConfigDialog = true
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("রিপো", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { showUsbGuideDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1.3f)
                        ) {
                            Icon(Icons.Default.PhoneAndroid, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("USB ইনস্টল গাইড", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { showSyncHelpDialog = true },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.HelpOutline, contentDescription = null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(3.dp))
                            Text("সাহায্য", fontSize = 11.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
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

                    Spacer(modifier = Modifier.height(10.dp))

                    // 1. Full APK OTA Auto-Update (Primary)
                    Button(
                        onClick = { viewModel.startFullOtaApkUpdate() },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudDownload,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "সম্পূর্ণ APK ওটিএ অটো-আপডেট (ফোনে সরাসরি)",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "পিসি/USB ক্যাবল ছাড়াই সরাসরি নতুন সংস্করণ ইনস্টল করুন",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 2. Quick OTA Content Sync
                    if (isDownloadingUpdate) {
                        Button(
                            onClick = { },
                            enabled = false,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("কনটেন্ট ডাউনলোড ও সিঙ্ক হচ্ছে...")
                        }
                    } else {
                        OutlinedButton(
                            onClick = { viewModel.downloadAndApplyInAppUpdate(isLocalPreview = false) },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column(horizontalAlignment = Alignment.Start) {
                                Text(
                                    text = "অনলাইন কনটেন্ট দ্রুত সিঙ্ক (Quick OTA Sync)",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "অ্যাপ রি-ইনস্টল ছাড়া শুধু নতুন দো'আ ও টেক্সট আপডেট",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3. Fallbacks: Browser Download & Local JSON Test
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.downloadNewApkVersion() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Download,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("ব্রাউজার ডাউনলোড", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { viewModel.testLocalAppUpdatesSync() },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("লোকাল টেস্ট", fontSize = 11.sp)
                        }
                    }
                }
            }

            // GitHub Repo Configuration Dialog
            if (showRepoConfigDialog) {
                AlertDialog(
                    onDismissRequest = { showRepoConfigDialog = false },
                    title = { Text("গিটহাব রিপোজিটরি কনফিগারেশন") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "আপনার নিজস্ব গিটহাব পাবলিক রিপোজিটরির নাম ও ইউজারনেম দিন যেখান থেকে কনটেন্ট সিঙ্ক হবে:",
                                style = MaterialTheme.typography.bodySmall
                            )
                            OutlinedTextField(
                                value = inputOwner,
                                onValueChange = { inputOwner = it },
                                label = { Text("GitHub Owner / Username") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = inputRepo,
                                onValueChange = { inputRepo = it },
                                label = { Text("Repository Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                viewModel.updateGitHubRepo(inputOwner, inputRepo)
                                showRepoConfigDialog = false
                            }
                        ) {
                            Text("সংরক্ষণ করুন")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showRepoConfigDialog = false }) {
                            Text("বাতিল")
                        }
                    }
                )
            }

            // Sync Explanation Dialog
            if (showSyncHelpDialog) {
                AlertDialog(
                    onDismissRequest = { showSyncHelpDialog = false },
                    title = { Text("অ্যাকাউন্ট সংযোগ ছাড়াই সিঙ্ক কিভাবে কাজ করে?") },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "১. পাবলিক রিড অ্যাক্সেস (No Account Needed):",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "গিটহাবের উন্মুক্ত (Public) রিপোজিটরি থেকে ডেটা পড়তে কোনো অ্যাকাউন্টে লগইন করার প্রয়োজন নেই। সাধারণ ইন্টারনেট ব্রাউজিংয়ের মতো অ্যাপটি সরাসরি ওপেন HTTP GET রিকোয়েস্টের মাধ্যমে পাবলিক ডেটা ডাউনলোড করে।",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "২. ডেভেলপার সেটআপ (One-time Setup):",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "আপনি আপনার গিটহাব অ্যাকাউন্টে একটি পাবলিক রিপোজিটরি তৈরি করে তাতে 'app-updates.json' ফাইল রাখবেন অথবা GitHub Releases-এ নতুন APK আপলোড করবেন।",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                "৩. অ্যাপ ব্যবহারকারীর জন্য সুবিধা:",
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "অ্যাপ ব্যবহারকারীরা শুধু 'সিঙ্ক' চাপলেই স্বয়ংক্রিয়ভাবে নতুন কনটেন্ট ও আপডেট ডাউনলোড হয়ে যাবে। ব্যবহারকারীর কোনো গিটহাব অ্যাকাউন্ট লাগবে না।",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showSyncHelpDialog = false }) {
                            Text("বুঝেছি")
                        }
                    }
                )
            }

            // USB Installation Guide Dialog (Matching AI Studio WebUSB Screen)
            if (showUsbGuideDialog) {
                AlertDialog(
                    onDismissRequest = { showUsbGuideDialog = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ইউএসবি (USB) দিয়ে ইনস্টল গাইড", fontSize = 16.sp)
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                "গুগল এআই স্টুডিও (AI Studio) থেকে কম্পিউটারে USB ক্যাবল দিয়ে সরাসরি ফোনে ইনস্টল করার ৪টি সহজ ধাপ:",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Bold
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(10.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        "১. ডেভেলপার মোড চালু করুন:\nফোনের Settings > About phone > 'Build number'-এ একটানা ৭ বার ট্যাপ করুন।",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "২. USB Debugging সক্রিয় করুন:\nSettings > System > Developer options-এ গিয়ে 'USB debugging' অন করুন।",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "৩. ইউএসবি ক্যাবল দিয়ে যুক্ত করুন:\nফোনটিকে ইউএসবি ক্যাবল দিয়ে কম্পিউটারে কানেক্ট করুন এবং ফোনে 'Allow USB debugging' আসলে টিক দিয়ে OK দিন।",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        "৪. 'Install via USB' চাপুন:\nএআই স্টুডিও ব্রাউজারে 'Install via USB' বাটনে ক্লিক করলেই সরাসরি ফোনে ইনস্টল হয়ে যাবে।",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(
                                        "★ ক্যাবল ছাড়া সরাসরি ফোনেই আপডেট চান?",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        "সেটিংসের 'সম্পূর্ণ APK ওটিএ অটো-আপডেট' বাটনে চাপ দিন। কোনো পিসি বা ক্যাবল ছাড়াই সরাসরি ইন্টারনেট দিয়ে ফোনেই সম্পূর্ণ অ্যাপটি আপডেট ও ইনস্টল হয়ে যাবে!",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = { showUsbGuideDialog = false }) {
                            Text("বুঝেছি")
                        }
                    }
                )
            }

            // Real-time In-App APK Download and Install Progress Dialog
            if (apkDownloadState.isDownloading || apkDownloadState.error != null || apkDownloadState.waitingForInstallPermission || apkDownloadState.installCompleted) {
                AlertDialog(
                    onDismissRequest = {
                        if (!apkDownloadState.isDownloading) {
                            viewModel.dismissApkDownloadDialog()
                        }
                    },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val icon = when {
                                apkDownloadState.error != null -> Icons.Default.Warning
                                apkDownloadState.waitingForInstallPermission -> Icons.Default.Warning
                                apkDownloadState.installCompleted -> Icons.Default.Check
                                else -> Icons.Default.CloudDownload
                            }
                            val tint = when {
                                apkDownloadState.error != null -> MaterialTheme.colorScheme.error
                                apkDownloadState.waitingForInstallPermission -> MaterialTheme.colorScheme.tertiary
                                else -> MaterialTheme.colorScheme.primary
                            }
                            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(22.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = when {
                                    apkDownloadState.isDownloading -> "APK ওটিএ ডাউনলোড হচ্ছে..."
                                    apkDownloadState.waitingForInstallPermission -> "অ্যাপ ইনস্টল পারমিশন প্রয়োজন"
                                    apkDownloadState.installCompleted -> "ইনস্টলার শুরু হয়েছে"
                                    apkDownloadState.error != null -> "আপডেট সমস্যা"
                                    else -> "অ্যাপ আপডেট"
                                },
                                fontSize = 16.sp
                            )
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            when {
                                apkDownloadState.isDownloading -> {
                                    Text(
                                        "গিটহাব থেকে সর্বশেষ সংস্করণের সম্পূর্ণ APK ফাইল ডাউনলোড হচ্ছে...",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    LinearProgressIndicator(
                                        progress = { apkDownloadState.progress.coerceIn(0f, 1f) },
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "${String.format(Locale.US, "%.1f", apkDownloadState.downloadedMb)} MB / ${if (apkDownloadState.totalMb > 0f) String.format(Locale.US, "%.1f MB", apkDownloadState.totalMb) else "..."}",
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                        Text(
                                            text = "${(apkDownloadState.progress * 100).toInt()}%",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        "ডাউনলোড শেষ হলেই স্বয়ংক্রিয়ভাবে প্যাকেজ ইনস্টলার ওপেন হবে।",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                apkDownloadState.waitingForInstallPermission -> {
                                    Text(
                                        "অ্যান্ড্রয়েড ৮.০+ সিকিউরিটির জন্য অজ্ঞাত উৎস (Unknown Sources) থেকে অ্যাপ ইনস্টলের অনুমতি প্রয়োজন।\n\nসেটিংসে 'Allow from this source' চালু করে নিচের বাটনে চাপ দিন।",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                apkDownloadState.installCompleted -> {
                                    Text(
                                        "আপনার ফোনের সিস্টেম প্যাকেজ ইনস্টলার উইন্ডো খোলা হয়েছে!\n\nস্ক্রিনে আসা প্রম্পটে 'Update' অথবা 'ইনস্টল' বাটনে চাপ দিয়ে সম্পূর্ণ আপডেট সম্পন্ন করুন। আপনার আগের কোনো ডেটা বা আমল মুছে যাবে না।",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                apkDownloadState.error != null -> {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                            Text(
                                                text = apkDownloadState.error ?: "অজ্ঞাত ত্রুটি",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        when {
                            apkDownloadState.waitingForInstallPermission -> {
                                Button(onClick = { viewModel.retryInstallDownloadedApk() }) {
                                    Text("এখনই ইনস্টল করুন")
                                }
                            }
                            apkDownloadState.error != null -> {
                                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Button(
                                        onClick = {
                                            viewModel.dismissApkDownloadDialog()
                                            viewModel.downloadAndApplyInAppUpdate(isLocalPreview = false)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                                    ) {
                                        Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("অনলাইন কনটেন্ট দ্রুত সিঙ্ক করুন (Quick OTA)")
                                    }

                                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.dismissApkDownloadDialog()
                                                showUsbGuideDialog = true
                                            },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("USB গাইড", fontSize = 11.sp)
                                        }
                                        OutlinedButton(
                                            onClick = {
                                                viewModel.dismissApkDownloadDialog()
                                                viewModel.downloadNewApkVersion()
                                            },
                                            modifier = Modifier.weight(1.2f)
                                        ) {
                                            Text("গিটহাব রিলিজ", fontSize = 11.sp)
                                        }
                                        Button(
                                            onClick = { viewModel.startFullOtaApkUpdate() },
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text("রিট্রাই", fontSize = 11.sp)
                                        }
                                    }
                                }
                            }
                            apkDownloadState.installCompleted -> {
                                Button(onClick = { viewModel.dismissApkDownloadDialog() }) {
                                    Text("ঠিক আছে")
                                }
                            }
                            else -> {}
                        }
                    },
                    dismissButton = {
                        if (!apkDownloadState.isDownloading) {
                            TextButton(onClick = { viewModel.dismissApkDownloadDialog() }) {
                                Text("বাতিল")
                            }
                        }
                    }
                )
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
                        text = "ভার্সন ১.০ (কনটেন্ট v${viewModel.appliedContentVersion}) • প্যাকেজ: com.dawahtojannah.app",
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
                if (latestReleaseInfo?.hasNewerVersion == true) {
                    Button(onClick = {
                        viewModel.downloadNewApkVersion()
                    }) {
                        Text("APK ডাউনলোড করুন")
                    }
                } else {
                    Button(onClick = { viewModel.dismissUpdateAlert() }) {
                        Text("ঠিক আছে")
                    }
                }
            },
            dismissButton = if (latestReleaseInfo?.hasNewerVersion == true) {
                {
                    Row {
                        OutlinedButton(onClick = {
                            viewModel.downloadAndApplyInAppUpdate(isLocalPreview = false)
                        }) {
                            Text("কনটেন্ট সিঙ্ক")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        TextButton(onClick = { viewModel.dismissUpdateAlert() }) {
                            Text("পরে")
                        }
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
