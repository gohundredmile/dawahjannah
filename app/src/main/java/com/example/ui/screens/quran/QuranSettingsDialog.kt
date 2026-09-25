package com.example.ui.screens.quran

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DownloadForOffline
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.QuranReciter
import com.example.data.model.QuranTafsirSource
import com.example.data.model.QuranTranslator
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.IslamicGreen
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.BanglaNumberUtils
import com.example.util.QuranAudioManager
import com.example.util.QuranSettingsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuranSettingsDialog(
    settingsManager: QuranSettingsManager,
    audioManager: QuranAudioManager,
    onDismiss: () -> Unit,
    onOpenAudioManager: () -> Unit
) {
    val settings by settingsManager.settings.collectAsState()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Header Bar
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(IslamicGreen.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = null,
                                    tint = IslamicGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "কুরআন সেটিংস",
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = banglaFont
                                    )
                                )
                                Text(
                                    text = "ফন্ট, অনুবাদ, প্রদর্শন ও তিলাওয়াত কাস্টমাইজেশন",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontFamily = banglaFont
                                    )
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("btn_close_quran_settings")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "বন্ধ করুন",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // Settings Body List
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Quick Action: Audio Manager Shortcut
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onDismiss()
                                    onOpenAudioManager()
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = IslamicGreen.copy(alpha = 0.12f)
                            ),
                            border = BorderStroke(1.dp, IslamicGreen.copy(alpha = 0.35f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(IslamicGreen),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DownloadForOffline,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "কুরআন অডিও ডাউনলোড ম্যানেজার",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = banglaFont
                                        )
                                    )
                                    Text(
                                        text = "এক ক্লিকে ১১৪ সূরা ডাউনলোড ও অফলাইন সংরক্ষণ",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontFamily = banglaFont
                                        )
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.Headphones,
                                    contentDescription = null,
                                    tint = IslamicGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    // Section 1: Typography & Live Preview
                    item {
                        SettingsSectionCard(title = "ফন্ট ও সাইজ কাস্টমাইজেশন", icon = Icons.Default.FormatSize) {
                            // Live Preview Box
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(14.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ",
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontFamily = arabicFont,
                                            fontSize = settings.arabicFontSize.sp,
                                            lineHeight = (settings.arabicFontSize * 1.6f).sp,
                                            textDirection = TextDirection.Rtl,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "পরম করুণাময় অসীম দয়ালু আল্লাহর নামে",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontSize = settings.banglaFontSize.sp,
                                            lineHeight = (settings.banglaFontSize * 1.4f).sp,
                                            fontFamily = banglaFont,
                                            textAlign = TextAlign.Center
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Arabic Font Size Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "আরবি ফন্ট সাইজ",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = banglaFont)
                                )
                                Text(
                                    text = "${BanglaNumberUtils.toBanglaDigits(settings.arabicFontSize.toInt())} sp",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = IslamicGold
                                    )
                                )
                            }
                            Slider(
                                value = settings.arabicFontSize,
                                onValueChange = { settingsManager.updateArabicFontSize(it) },
                                valueRange = 18f..42f,
                                steps = 24
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Bangla Font Size Slider
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "বাংলা ফন্ট সাইজ",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontFamily = banglaFont)
                                )
                                Text(
                                    text = "${BanglaNumberUtils.toBanglaDigits(settings.banglaFontSize.toInt())} sp",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                            Slider(
                                value = settings.banglaFontSize,
                                onValueChange = { settingsManager.updateBanglaFontSize(it) },
                                valueRange = 12f..28f,
                                steps = 16
                            )
                        }
                    }

                    // Section 2: Display & Visibility Toggles
                    item {
                        SettingsSectionCard(title = "প্রদর্শন ও দৃশ্যমানতা (Display)", icon = Icons.Default.Visibility) {
                            SettingsSwitchRow(
                                title = "আরবি মূল পাঠ প্রদর্শন",
                                subtitle = "সূরার আয়াতে হরকতযুক্ত মূল আরবি টেক্সট দেখাবে",
                                checked = settings.showArabic,
                                onCheckedChange = { settingsManager.updateShowArabic(it) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            SettingsSwitchRow(
                                title = "বাংলা উচ্চারণ প্রদর্শন",
                                subtitle = "তাজবীদসম্মত সহজ বাংলা উচ্চারণ দেখাবে",
                                checked = settings.showPronunciation,
                                onCheckedChange = { settingsManager.updateShowPronunciation(it) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            SettingsSwitchRow(
                                title = "বাংলা অর্থ ও অনুবাদ প্রদর্শন",
                                subtitle = "কিং ফাহাদ কমপ্লেক্স ও ড. আবু বকর যাকারিয়া অনুদিত বাংলা অর্থ",
                                checked = settings.showTranslation,
                                onCheckedChange = { settingsManager.updateShowTranslation(it) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            SettingsSwitchRow(
                                title = "তাফসীর সর্বদা উন্মুক্ত রাখুন",
                                subtitle = "আয়াত পড়ার সময় তাফসীর বক্স স্বয়ংক্রিয়ভাবে খুলে থাকবে",
                                checked = settings.showTafsirByDefault,
                                onCheckedChange = { settingsManager.updateShowTafsirByDefault(it) }
                            )
                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                            SettingsSwitchRow(
                                title = "ইংরেজি অনুবাদ প্রদর্শন",
                                subtitle = "আন্তর্জাতিক প্রামাণ্য ইংরেজি সহীহ আন্তর্জাতিক অনুবাদ",
                                checked = settings.showEnglishTranslation,
                                onCheckedChange = { settingsManager.updateShowEnglishTranslation(it) }
                            )
                        }
                    }

                    // Section 3: Translation Sources
                    item {
                        SettingsSectionCard(title = "অনুবাদ সোর্স নির্বাচন", icon = Icons.Default.Translate) {
                            Text(
                                text = "পছন্দের বাংলা অনুবাদক সিলেক্ট করুন:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            val translators = listOf(
                                QuranTranslator.DR_ZAKARIA to "ড. আবু বকর মুহাম্মাদ যাকারিয়া (মদীনা প্রিন্ট ও কিং ফাহাদ কমপ্লেক্স)",
                                QuranTranslator.TAISIRUL_QURAN to "তাওহীদ পাবলিকেশন্স (তাইসীরুল কুরআন)",
                                QuranTranslator.MUHIBBUR_RAHMAN to "মাওলানা মুজিবুর রহমান (সহীহ অনুবাদ)"
                            )

                            translators.forEach { (translator, desc) ->
                                val isSelected = settings.preferredTranslator == translator.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { settingsManager.updatePreferredTranslator(translator.id) }
                                        .background(
                                            if (isSelected) IslamicGreen.copy(alpha = 0.12f)
                                            else Color.Transparent
                                        )
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.RadioButton(
                                        selected = isSelected,
                                        onClick = { settingsManager.updatePreferredTranslator(translator.id) },
                                        colors = androidx.compose.material3.RadioButtonDefaults.colors(selectedColor = IslamicGreen)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = translator.titleBn,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicGreen else MaterialTheme.colorScheme.onSurface,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontFamily = banglaFont
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section 4: Tafsir Source Selection (তাফসীর গ্রন্থ নির্বাচন)
                    item {
                        SettingsSectionCard(title = "তাফসীর গ্রন্থ নির্বাচন", icon = Icons.Default.MenuBook) {
                            Text(
                                text = "পছন্দের প্রামাণ্য তাফসীর গ্রন্থ বেছে নিন:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            QuranTafsirSource.entries.forEach { tafsirSource ->
                                val isSelected = settings.preferredTafsir == tafsirSource.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { settingsManager.updatePreferredTafsir(tafsirSource.id) }
                                        .background(
                                            if (isSelected) IslamicGold.copy(alpha = 0.14f)
                                            else Color.Transparent
                                        )
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.RadioButton(
                                        selected = isSelected,
                                        onClick = { settingsManager.updatePreferredTafsir(tafsirSource.id) },
                                        colors = androidx.compose.material3.RadioButtonDefaults.colors(selectedColor = IslamicGold)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = tafsirSource.titleBn,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicGold else MaterialTheme.colorScheme.onSurface,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        Text(
                                            text = "${tafsirSource.authorBn} • ${tafsirSource.descriptionBn}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 11.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontFamily = banglaFont
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Section 5: Reciter & Audio Quality
                    item {
                        SettingsSectionCard(title = "তিলাওয়াত ও অডিও কোয়ালিটি", icon = Icons.Default.RecordVoiceOver) {
                            Text(
                                text = "ডিফল্ট ক্বারী নির্বাচন করুন:",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontFamily = banglaFont
                                )
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            QuranReciter.entries.forEach { reciter ->
                                val isSelected = settings.defaultReciterId == reciter.id
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable {
                                            settingsManager.updateDefaultReciter(reciter.id)
                                            audioManager.selectReciter(reciter)
                                        }
                                        .background(
                                            if (isSelected) IslamicGold.copy(alpha = 0.15f)
                                            else Color.Transparent
                                        )
                                        .padding(horizontal = 8.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.material3.RadioButton(
                                        selected = isSelected,
                                        onClick = {
                                            settingsManager.updateDefaultReciter(reciter.id)
                                            audioManager.selectReciter(reciter)
                                        },
                                        colors = androidx.compose.material3.RadioButtonDefaults.colors(selectedColor = IslamicGold)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = if (reciter.isFavorite) "⭐" else "🎙️",
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = reciter.nameBn,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) IslamicGold else MaterialTheme.colorScheme.onSurface,
                                                fontFamily = banglaFont
                                            )
                                        )
                                        Text(
                                            text = "${reciter.nameEn} • ${reciter.audioQuality}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                fontSize = 10.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }
                                }
                            }

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                            SettingsSwitchRow(
                                title = "অডিও চলাকালে স্বয়ংক্রিয় স্ক্রলিং",
                                subtitle = "যে আয়াত তিলাওয়াত হচ্ছে স্বয়ংক্রিয়ভাবে স্ক্রিনে ফোকাস থাকবে",
                                checked = settings.autoScrollWithAudio,
                                onCheckedChange = { settingsManager.updateAutoScroll(it) }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))

                            SettingsSwitchRow(
                                title = "পড়ার সময় স্ক্রিন সর্বদা সচল রাখুন",
                                subtitle = "কুরআন পড়ার সময় ফোন স্ক্রিন নিজে থেকে বন্ধ হবে না",
                                checked = settings.keepScreenAwake,
                                onCheckedChange = { settingsManager.updateKeepScreenAwake(it) }
                            )
                        }
                    }

                    // Reset button
                    item {
                        OutlinedButton(
                            onClick = { settingsManager.resetToDefaults() },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "ডিফল্ট সেটিংসে ফিরুন (Reset Settings)", fontFamily = banglaFont)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = IslamicGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = banglaFont
                    )
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    fontFamily = banglaFont
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = banglaFont
                )
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = IslamicGreen
            )
        )
    }
}
