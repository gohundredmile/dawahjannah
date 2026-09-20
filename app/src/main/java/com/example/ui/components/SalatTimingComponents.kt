package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.data.model.AsrJuristicMethod
import com.example.data.model.ForbiddenTimeInfo
import com.example.data.model.HighLatitudeRule
import com.example.data.model.PRESET_SALAT_PLACES
import com.example.data.model.PrayerCalculationMethod
import com.example.data.model.PrayerTimeItem
import com.example.data.model.SalatConfiguration
import com.example.data.model.SalatPlaceInfo
import com.example.data.model.ScreenEffectMode
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
import com.example.ui.theme.IslamicGold
import java.util.Locale

sealed class SalatDialogTarget {
    data class Prayer(val prayer: PrayerTimeItem) : SalatDialogTarget()
    data class Forbidden(val forbiddenInfo: ForbiddenTimeInfo) : SalatDialogTarget()
}

@Composable
fun SalatTimingsSection(
    prayerStatus: PrayerCalculator.PrayerStatus,
    salatConfig: SalatConfiguration = SalatConfiguration(),
    gpsStatusMessage: String? = null,
    onTrackGps: () -> Unit = {},
    onSelectPlace: (SalatPlaceInfo) -> Unit = {},
    onCustomPlace: (String, String, Double, Double) -> Unit = { _, _, _, _ -> },
    onSetManualOffset: (Int) -> Unit = {},
    onToggleHanafiAsr: (Boolean) -> Unit = {},
    onSelectCalculationMethod: (PrayerCalculationMethod) -> Unit = {},
    onSelectAsrMethod: (AsrJuristicMethod) -> Unit = {},
    onSelectHighLatitudeRule: (HighLatitudeRule) -> Unit = {},
    onResetSalatPreferences: () -> Unit = {},
    onClearGpsMessage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTarget by remember { mutableStateOf<SalatDialogTarget?>(null) }
    var showConfigDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false
        if (fineGranted || coarseGranted) {
            onTrackGps()
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "ওয়াক্ত ও নামাজের সময়সূচি",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "লাইভ ওয়াক্ত ট্র্যাকার",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // GPS Tracker & Place Configuration Control Bar
        val isDark = isSystemInDarkTheme()
        PureGlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(18.dp),
            accentBorderColor = if (salatConfig.isGpsEnabled) Color(0xFF0284C7) else IslamicGold,
            borderWidth = 1.2.dp,
            elevation = 2.dp,
            isDark = isDark,
            showWaveEffect = true
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .background(
                                    Brush.radialGradient(
                                        listOf(
                                            (if (salatConfig.isGpsEnabled) Color(0xFF0284C7) else MaterialTheme.colorScheme.primary).copy(alpha = if (isDark) 0.35f else 0.22f),
                                            (if (salatConfig.isGpsEnabled) Color(0xFF0284C7) else MaterialTheme.colorScheme.primary).copy(alpha = if (isDark) 0.12f else 0.06f)
                                        )
                                    ),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (salatConfig.isGpsEnabled) Icons.Default.MyLocation else Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = if (salatConfig.isGpsEnabled) Color(0xFF0284C7) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = salatConfig.placeNameBn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A),
                                    fontSize = 14.5.sp
                                )
                                if (salatConfig.isGpsEnabled) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Surface(
                                        color = Color(0xFF0284C7),
                                        shape = RoundedCornerShape(6.dp)
                                    ) {
                                        Text(
                                            text = "GPS",
                                            color = Color.White,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }

                            val methodShortName = when (salatConfig.calculationMethod) {
                                PrayerCalculationMethod.KARACHI -> "করাচি মানদণ্ড"
                                PrayerCalculationMethod.ISLAMIC_FOUNDATION_BD -> "ইসলামিক ফাউন্ডেশন"
                                PrayerCalculationMethod.MUSLIM_WORLD_LEAGUE -> "মুসলিম লীগ"
                                PrayerCalculationMethod.EGYPTIAN -> "মিশরীয় জরিপ"
                                PrayerCalculationMethod.ISNA -> "ISNA"
                                PrayerCalculationMethod.UMM_AL_QURA -> "উম্মুল কুরা"
                                PrayerCalculationMethod.GULF_DUBAI -> "উপসাগরীয়"
                                PrayerCalculationMethod.SHIA_ITHNA_ASHARI -> "লেভা/কোম"
                                PrayerCalculationMethod.TEHRAN_GEOPHYSICS -> "তেহরান"
                            }

                            Text(
                                text = "${String.format(Locale.US, "%.2f°N, %.2f°E", salatConfig.latitude, salatConfig.longitude)} • $methodShortName • ${if (salatConfig.isHanafiAsr) "হানাফী" else "শাফেয়ী"}${if (salatConfig.manualOffsetMinutes != 0) " (${if (salatConfig.manualOffsetMinutes > 0) "+" else ""}${salatConfig.manualOffsetMinutes} মি.)" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Medium,
                                color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569),
                                fontSize = 11.sp,
                                maxLines = 1
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // GPS Track Button
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.clickable {
                                val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                val hasCoarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                if (hasFine || coarseGranted(context)) {
                                    onTrackGps()
                                } else {
                                    permissionLauncher.launch(
                                        arrayOf(
                                            Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION
                                        )
                                    )
                                }
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = "GPS Track",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "GPS",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // Place & Settings Button - Seamless Translucent Glass Pill without harsh white box outline
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.18f else 0.10f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.clickable { showConfigDialog = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Configure",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "স্থান পরিবর্তন",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                // Animated GPS Status Message
                AnimatedVisibility(visible = gpsStatusMessage != null) {
                    gpsStatusMessage?.let { msg ->
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = onClearGpsMessage,
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Close",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // VERTICAL CARDS ARRANGEMENT (5 Salat Cards + 1 Forbidden Time Card)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            // 5 Daily Prayers arranged Vertically
            prayerStatus.prayerList.forEach { prayerItem ->
                SalatTimingVerticalCard(
                    prayer = prayerItem,
                    onClick = { selectedTarget = SalatDialogTarget.Prayer(prayerItem) }
                )
            }

            // 6th Card: Forbidden Time (সালাতের নিষিদ্ধ সময়) arranged Vertically after Esha
            ForbiddenTimeVerticalCard(
                forbiddenInfo = prayerStatus.forbiddenTimeInfo,
                onClick = { selectedTarget = SalatDialogTarget.Forbidden(prayerStatus.forbiddenTimeInfo) }
            )
        }
    }

    // Details Modal Dialog for Study Guide & Azkar
    selectedTarget?.let { target ->
        SalatDetailsDialog(
            target = target,
            onDismiss = { selectedTarget = null }
        )
    }

    // Salat Timing & Place Configuration Modal Dialog
    if (showConfigDialog) {
        SalatConfigDialog(
            currentConfig = salatConfig,
            onDismiss = { showConfigDialog = false },
            onSelectPlace = { place ->
                onSelectPlace(place)
                showConfigDialog = false
            },
            onCustomPlace = { nameBn, nameEn, lat, lng ->
                onCustomPlace(nameBn, nameEn, lat, lng)
                showConfigDialog = false
            },
            onSetOffset = onSetManualOffset,
            onToggleHanafi = onToggleHanafiAsr,
            onSelectCalculationMethod = onSelectCalculationMethod,
            onSelectAsrMethod = onSelectAsrMethod,
            onSelectHighLatitudeRule = onSelectHighLatitudeRule,
            onResetSalatPreferences = onResetSalatPreferences,
            onTrackGps = {
                showConfigDialog = false
                val hasFine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                if (hasFine || coarseGranted(context)) {
                    onTrackGps()
                } else {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            }
        )
    }
}

private fun coarseGranted(context: android.content.Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

@Composable
fun SalatTimingVerticalCard(
    prayer: PrayerTimeItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isHighlighted = prayer.isHighlighted

    val activeBrush = Brush.horizontalGradient(
        listOf(
            Color(0xFFFF6D00),
            Color(0xFFF97316),
            Color(0xFFEF4444),
            Color(0xFFDC2626)
        )
    )

    val tagColor = when {
        isHighlighted -> Color(0xFFEA580C)
        prayer.id == "fajr" -> Color(0xFF0284C7)
        prayer.id == "dhuhr" -> Color(0xFFD97706)
        prayer.id == "asr" -> Color(0xFFEA580C)
        prayer.id == "maghrib" -> Color(0xFFE11D48)
        prayer.id == "isha" -> Color(0xFF475569)
        else -> MaterialTheme.colorScheme.primary
    }

    val isDark = isSystemInDarkTheme()
    val effectMode = LocalScreenEffectMode.current
    val isGlassMode = effectMode == ScreenEffectMode.GLASS

    val prayerIcon = when (prayer.id) {
        "fajr" -> Icons.Default.WbTwilight
        "dhuhr" -> Icons.Default.WbSunny
        "asr" -> Icons.Default.LightMode
        "maghrib" -> Icons.Default.NightsStay
        "isha" -> Icons.Default.Bedtime
        else -> Icons.Default.AccessTime
    }

    val cardShape = RoundedCornerShape(16.dp)

    // Soft seamless border without harsh white box outline
    val cardBorder = if (isHighlighted) {
        BorderStroke(1.0.dp, Color(0xFFFEF08A).copy(alpha = 0.45f))
    } else {
        BorderStroke(0.8.dp, tagColor.copy(alpha = if (isDark) 0.35f else 0.22f))
    }

    // Translucent glass background: uniform smooth frosted glass without any vertical split, top/bottom white banding, or inner box
    val cardBgModifier = if (isHighlighted) {
        Modifier.background(activeBrush)
    } else if (isGlassMode) {
        Modifier.background(
            Brush.horizontalGradient(
                listOf(
                    (if (isDark) Color(0xFF1E293B) else Color.White).copy(alpha = if (isDark) 0.55f else 0.45f),
                    tagColor.copy(alpha = if (isDark) 0.12f else 0.08f),
                    (if (isDark) Color(0xFF1E293B) else Color.White).copy(alpha = if (isDark) 0.50f else 0.40f)
                )
            )
        )
    } else {
        Modifier.background(
            if (isDark) Color(0xFF1E293B) else Color.White
        )
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = cardShape,
        color = Color.Transparent,
        border = cardBorder,
        shadowElevation = if (isHighlighted) 2.dp else 0.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
                .then(if (!isHighlighted) cardBgModifier else Modifier)
        ) {
            if (isHighlighted) {
                Image(
                    painter = painterResource(id = R.drawable.img_islamic_header_wallpaper),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize()
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color(0xFFC2410C).copy(alpha = 0.88f),
                                    Color(0xFFEA580C).copy(alpha = 0.80f),
                                    Color(0xFFB45309).copy(alpha = 0.86f)
                                )
                            )
                        )
                )
                GlassTopHighlight(
                    modifier = Modifier.align(Alignment.TopCenter),
                    isDark = false,
                    opacity = 0.45f
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 13.dp, vertical = 9.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Side: Soft circular ambient glow badge (CircleShape with radial gradient tint) + Wakt Name + Seamless status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    // Soft, circular ambient glow (CircleShape with radial gradient tint) that integrates seamlessly into the glass card
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        (if (isHighlighted) Color(0xFFFEF08A) else tagColor).copy(alpha = if (isHighlighted) 0.35f else if (isDark) 0.30f else 0.22f),
                                        (if (isHighlighted) Color(0xFFFEF08A) else tagColor).copy(alpha = if (isHighlighted) 0.12f else if (isDark) 0.12f else 0.06f),
                                        Color.Transparent
                                    )
                                ),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = prayerIcon,
                            contentDescription = null,
                            tint = if (isHighlighted) Color(0xFFFEF08A) else (if (isDark) tagColor.copy(alpha = 0.95f) else tagColor),
                            modifier = Modifier.size(17.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(9.dp))

                    Text(
                        text = prayer.nameBn,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isHighlighted) Color.White else (if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A)),
                        fontSize = 15.5.sp
                    )

                    Spacer(modifier = Modifier.width(7.dp))

                    if (isHighlighted) {
                        // Soft seamless pill without harsh white box outline
                        Box(
                            modifier = Modifier
                                .background(
                                    Color(0xFFFEF08A).copy(alpha = 0.22f),
                                    shape = CircleShape
                                )
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "চলমান",
                                color = Color(0xFFFEF08A),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        // Clean typography without harsh rectangle box outline
                        Text(
                            text = prayer.nameEn,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isDark) tagColor.copy(alpha = 0.90f) else tagColor.copy(alpha = 0.85f),
                            fontSize = 9.5.sp,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                // Right Side: Starting time only + Chevron in the same line
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "শুরু",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (isHighlighted) Color.White.copy(alpha = 0.9f) else (if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = prayer.startTimeFormatted.ifEmpty { prayer.timeFormatted },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.5.sp,
                        color = if (isHighlighted) Color(0xFFFEF08A) else (if (isDark) Color(0xFFF8FAFC) else Color(0xFF0F172A))
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "বিস্তারিত",
                        tint = if (isHighlighted) Color.White.copy(alpha = 0.9f) else (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B)),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ForbiddenTimeVerticalCard(
    forbiddenInfo: ForbiddenTimeInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = isSystemInDarkTheme()
    val cardShape = RoundedCornerShape(18.dp)

    // Radiant Islamic border
    val cardBorder = BorderStroke(
        1.2.dp,
        Brush.horizontalGradient(
            listOf(
                Color(0xFFFDE047).copy(alpha = if (isDark) 0.60f else 0.50f),
                Color(0xFFE11D48).copy(alpha = if (isDark) 0.50f else 0.35f),
                Color(0xFFF59E0B).copy(alpha = if (isDark) 0.65f else 0.50f)
            )
        )
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .shadow(elevation = 3.dp, shape = cardShape),
        shape = cardShape,
        color = Color.Transparent,
        border = cardBorder
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(cardShape)
        ) {
            // Outstanding Islamic Wallpaper Background
            Image(
                painter = painterResource(id = R.drawable.img_ramadan_moon_bg),
                contentDescription = "সালাতের নিষিদ্ধ সময় ব্যাকগ্রাউন্ড",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            // Deep Atmospheric Velvet Twilight Scrim Overlay
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                (if (isDark) Color(0xFF0F172A) else Color(0xFF1E1B4B)).copy(alpha = 0.82f),
                                (if (isDark) Color(0xFF1E1B4B) else Color(0xFF312E81)).copy(alpha = 0.72f),
                                (if (isDark) Color(0xFF3B0715) else Color(0xFF4C0519)).copy(alpha = 0.85f)
                            )
                        )
                    )
            )

            // Specular glass top shimmer highlight
            GlassTopHighlight(
                modifier = Modifier.align(Alignment.TopCenter),
                isDark = isDark,
                opacity = 0.6f
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 11.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left Side: Soft circular ambient glow + Title + Subtitle + Soft tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE11D48).copy(alpha = 0.30f),
                        border = BorderStroke(1.dp, Color(0xFFFDA4AF).copy(alpha = 0.65f)),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = Color(0xFFFEF08A),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "সালাতের নিষিদ্ধ সময়",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // Soft seamless pill tag
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFFE11D48).copy(alpha = 0.45f),
                                border = BorderStroke(0.8.dp, Color(0xFFFDA4AF).copy(alpha = 0.55f))
                            ) {
                                Text(
                                    text = "৩টি সময়",
                                    color = Color(0xFFFFF1F2),
                                    fontSize = 9.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "সূর্যোদয়, দ্বিপ্রহর ও সূর্যাস্তের নিষিদ্ধ ওয়াক্ত",
                            color = Color(0xFFCBD5E1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Right Side: Guide text + Chevron Arrow marked glass pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.38f),
                    border = BorderStroke(0.9.dp, Color(0xFFFDA4AF).copy(alpha = 0.55f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "নিষিদ্ধ সময়সূচী",
                            fontSize = 10.5.sp,
                            color = Color(0xFFFEF08A),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "বিস্তারিত",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

// SALAT PLACE & GPS CONFIGURATION DIALOG
@Composable
fun SalatConfigDialog(
    currentConfig: SalatConfiguration,
    onDismiss: () -> Unit,
    onSelectPlace: (SalatPlaceInfo) -> Unit,
    onCustomPlace: (String, String, Double, Double) -> Unit,
    onSetOffset: (Int) -> Unit,
    onToggleHanafi: (Boolean) -> Unit,
    onSelectCalculationMethod: (PrayerCalculationMethod) -> Unit = {},
    onSelectAsrMethod: (AsrJuristicMethod) -> Unit = {},
    onSelectHighLatitudeRule: (HighLatitudeRule) -> Unit = {},
    onResetSalatPreferences: () -> Unit = {},
    onTrackGps: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") } // "all", "division", "district", "international", "custom"

    // Custom coordinates fields
    var customNameBn by remember { mutableStateOf("") }
    var customNameEn by remember { mutableStateOf("") }
    var customLatText by remember { mutableStateOf(String.format(Locale.US, "%.4f", currentConfig.latitude)) }
    var customLngText by remember { mutableStateOf(String.format(Locale.US, "%.4f", currentConfig.longitude)) }

    val filteredPlaces = remember(searchQuery, selectedCategory) {
        PRESET_SALAT_PLACES.filter { place ->
            val matchCategory = selectedCategory == "all" || place.category == selectedCategory
            val matchQuery = searchQuery.isBlank() ||
                    place.nameBn.contains(searchQuery, ignoreCase = true) ||
                    place.nameEn.contains(searchQuery, ignoreCase = true)
            matchCategory && matchQuery
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .widthIn(max = 620.dp)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "নামাজের সময় ও স্থান নির্ধারণ",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 19.sp
                            )
                            Text(
                                text = "GPS ট্র্যাকার ও বিভিন্ন কাস্টম স্থানের সময়সূচি",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 11.5.sp
                            )
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.25f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. GPS Auto-Tracker Banner
                    Surface(
                        color = Color(0xFF0284C7).copy(alpha = 0.08f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.2.dp, Color(0xFF0284C7).copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Surface(
                                    color = Color(0xFF0284C7),
                                    shape = CircleShape,
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.MyLocation,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "জিপিএস অটো-ট্র্যাকার",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = Color(0xFF0369A1)
                                    )
                                    Text(
                                        text = "বর্তমান অবস্থান অনুযায়ী ওয়াক্ত হিসাব করুন",
                                        fontSize = 11.sp,
                                        color = Color(0xFF0284C7)
                                    )
                                }
                            }

                            Button(
                                onClick = onTrackGps,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0284C7)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = "ট্র্যাক করুন",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // 2. Category Filter Chips
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val categories = listOf(
                            "all" to "সব স্থান",
                            "division" to "বিভাগসমূহ",
                            "district" to "অন্যান্য জেলা",
                            "international" to "পবিত্র ও আন্তর্জাতিক",
                            "custom" to "কাস্টম স্থানাঙ্ক"
                        )
                        items(categories) { (catId, catTitle) ->
                            FilterChip(
                                selected = selectedCategory == catId,
                                onClick = { selectedCategory = catId },
                                label = { Text(catTitle, fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    if (selectedCategory != "custom") {
                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("স্থানের নাম দিয়ে খুঁজুন (যেমন: ঢাকা, সিলেট, মক্কা...)", fontSize = 13.sp) },
                            leadingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search", tint = MaterialTheme.colorScheme.primary)
                            },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Close, contentDescription = "Clear", modifier = Modifier.size(16.dp))
                                    }
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Places List
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .verticalScroll(rememberScrollState()),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (filteredPlaces.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(32.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("কোনো স্থান পাওয়া যায়নি", color = MaterialTheme.colorScheme.onSurfaceVariant)
                                }
                            } else {
                                filteredPlaces.forEach { place ->
                                    val isCurrent = !currentConfig.isGpsEnabled &&
                                            (currentConfig.placeNameBn.contains(place.nameBn) ||
                                                    Math.abs(currentConfig.latitude - place.latitude) < 0.05 &&
                                                    Math.abs(currentConfig.longitude - place.longitude) < 0.05)

                                    Surface(
                                        color = if (isCurrent) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                        shape = RoundedCornerShape(12.dp),
                                        border = if (isCurrent) BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary) else null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectPlace(place) }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 9.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = place.nameBn,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Spacer(modifier = Modifier.width(6.dp))
                                                    Text(
                                                        text = "(${place.nameEn})",
                                                        fontSize = 12.sp,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }
                                                Text(
                                                    text = "${String.format(Locale.US, "%.2f°N, %.2f°E", place.latitude, place.longitude)}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }

                                            if (isCurrent) {
                                                Surface(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = CircleShape,
                                                    modifier = Modifier.size(24.dp)
                                                ) {
                                                    Box(contentAlignment = Alignment.Center) {
                                                        Icon(
                                                            imageVector = Icons.Default.Check,
                                                            contentDescription = "Selected",
                                                            tint = Color.White,
                                                            modifier = Modifier.size(15.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        // Custom Coordinates Form
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = "কাস্টম স্থানাঙ্ক এন্ট্রি (Custom Place Entry)",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.5.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                OutlinedTextField(
                                    value = customNameBn,
                                    onValueChange = { customNameBn = it },
                                    label = { Text("স্থানের নাম (বাংলা)", fontSize = 12.sp) },
                                    placeholder = { Text("যেমন: আমার গ্রাম বা এলাকা") },
                                    singleLine = true,
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = customLatText,
                                        onValueChange = { customLatText = it },
                                        label = { Text("অক্ষাংশ (Latitude)", fontSize = 12.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    )

                                    OutlinedTextField(
                                        value = customLngText,
                                        onValueChange = { customLngText = it },
                                        label = { Text("দ্রাঘিমাংশ (Longitude)", fontSize = 12.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f)
                                    )
                                }

                                Button(
                                    onClick = {
                                        val lat = customLatText.toDoubleOrNull() ?: 23.8103
                                        val lng = customLngText.toDoubleOrNull() ?: 90.4125
                                        val nameBn = customNameBn.ifBlank { "কাস্টম স্থান" }
                                        val nameEn = customNameEn.ifBlank { "Custom Place" }
                                        onCustomPlace(nameBn, nameEn, lat, lng)
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("কাস্টম স্থান সংরক্ষণ ও প্রয়োগ করুন")
                                }
                            }
                        }
                    }

                    // 3. Calculation Method & Basis, Juristic School & Time Offset Settings
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "ওয়াক্ত হিসাবের মানদণ্ড ও নিয়মাবলী",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Basis Information Banner
                            Surface(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Info",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .size(20.dp)
                                            .padding(top = 2.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "ওয়াক্ত নির্ধারণের জ্যোতির্বৈজ্ঞানিক ভিত্তি",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.5.sp,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "সূর্যের জেনিথ কোণ (Zenith Angle) ও বিষুবীয় স্থানাঙ্কের (Solar Declination & Equation of Time) আন্তর্জাতিক জ্যাঁ মিউস (Jean Meeus) সূত্রের ভিত্তিতে সময় গণনা করা হয়। বাংলাদেশ ও উপমহাদেশের নির্ভরযোগ্য ইসলামিক পঞ্জিকা এবং গুগল সার্চে 'University of Islamic Sciences, Karachi' এবং হানাফী আসর মানদণ্ড অনুসৃত হয়।",
                                            fontSize = 11.5.sp,
                                            lineHeight = 16.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }

                            // International Calculation Methods
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "আন্তর্জাতিক গণনা পদ্ধতি (Calculation Method)",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "নির্বাচিত: ${currentConfig.calculationMethod.name}",
                                        fontSize = 10.5.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                PrayerCalculationMethod.entries.forEach { method ->
                                    val isSelected = currentConfig.calculationMethod == method
                                    Surface(
                                        color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            width = if (isSelected) 1.5.dp else 1.dp,
                                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { onSelectCalculationMethod(method) }
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 12.dp, vertical = 9.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Text(
                                                        text = method.titleBn,
                                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                        fontSize = 13.sp,
                                                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                    )
                                                    if (method == PrayerCalculationMethod.KARACHI) {
                                                        Spacer(modifier = Modifier.width(6.dp))
                                                        Surface(
                                                            color = MaterialTheme.colorScheme.primary,
                                                            shape = RoundedCornerShape(4.dp)
                                                        ) {
                                                            Text(
                                                                text = "বাংলাদেশ/গুগল",
                                                                color = Color.White,
                                                                fontSize = 9.sp,
                                                                fontWeight = FontWeight.Bold,
                                                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                                            )
                                                        }
                                                    }
                                                }
                                                Spacer(modifier = Modifier.height(2.dp))
                                                Text(
                                                    text = "ফজর ${method.fajrAngle}° • এশা ${if ((method.ishaAngle ?: 0.0) > 0) "${method.ishaAngle}°" else "${method.ishaIntervalMinutes} মি. পর"} • ${method.descriptionBn}",
                                                    fontSize = 11.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    lineHeight = 15.sp
                                                )
                                            }

                                            if (isSelected) {
                                                Box(
                                                    modifier = Modifier
                                                        .size(22.dp)
                                                        .clip(CircleShape)
                                                        .background(MaterialTheme.colorScheme.primary),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = "Selected",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            // Asr Juristic Method
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "আসরের মাযহাবী মানদণ্ড (Asr Juristic Method)",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val isHanafi = currentConfig.isHanafiAsr
                                    // Hanafi Chip/Card
                                    Surface(
                                        color = if (isHanafi) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            width = if (isHanafi) 1.5.dp else 1.dp,
                                            color = if (isHanafi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                onToggleHanafi(true)
                                                onSelectAsrMethod(AsrJuristicMethod.HANAFI)
                                            }
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "হানাফী মাযহাব",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.5.sp,
                                                    color = if (isHanafi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                                if (isHanafi) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "ছায়া দ্বিগুণ হলে আসর (মিসলে সানি)",
                                                fontSize = 10.5.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    // Shafi'i / Standard Chip/Card
                                    Surface(
                                        color = if (!isHanafi) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f) else MaterialTheme.colorScheme.surface,
                                        shape = RoundedCornerShape(12.dp),
                                        border = BorderStroke(
                                            width = if (!isHanafi) 1.5.dp else 1.dp,
                                            color = if (!isHanafi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                        ),
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable {
                                                onToggleHanafi(false)
                                                onSelectAsrMethod(AsrJuristicMethod.STANDARD)
                                            }
                                    ) {
                                        Column(modifier = Modifier.padding(10.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "শাফেয়ী / সাধারণ",
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 12.5.sp,
                                                    color = if (!isHanafi) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                                )
                                                if (!isHanafi) {
                                                    Icon(
                                                        imageVector = Icons.Default.Check,
                                                        contentDescription = null,
                                                        tint = MaterialTheme.colorScheme.primary,
                                                        modifier = Modifier.size(15.dp)
                                                    )
                                                }
                                            }
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = "ছায়া এক গুণ হলে আসর (মিসলে আওয়াল)",
                                                fontSize = 10.5.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                }
                            }

                            // High Latitude Rule
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "উচ্চ অক্ষাংশ সমন্বয় (High Latitude Rule)",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    items(HighLatitudeRule.entries.toTypedArray()) { rule ->
                                        val isRuleSelected = currentConfig.highLatitudeRule == rule
                                        FilterChip(
                                            selected = isRuleSelected,
                                            onClick = { onSelectHighLatitudeRule(rule) },
                                            label = { Text(rule.titleBn, fontSize = 11.5.sp) },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = MaterialTheme.colorScheme.primary,
                                                selectedLabelColor = Color.White
                                            )
                                        )
                                    }
                                }
                            }

                            // Minute Adjustment Slider / Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "স্থানীয় মসজিদের সাথে মিনিট সমন্বয়",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "বর্তমান অফসেট: ${if (currentConfig.manualOffsetMinutes > 0) "+" else ""}${currentConfig.manualOffsetMinutes} মিনিট",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { onSetOffset((currentConfig.manualOffsetMinutes - 1).coerceAtLeast(-15)) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Remove, contentDescription = "Minus", modifier = Modifier.size(16.dp))
                                    }

                                    Text(
                                        text = "${currentConfig.manualOffsetMinutes}m",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp)
                                    )

                                    IconButton(
                                        onClick = { onSetOffset((currentConfig.manualOffsetMinutes + 1).coerceAtMost(15)) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = "Plus", modifier = Modifier.size(16.dp))
                                    }
                                }
                            }

                            // Reset to Standard Button
                            OutlinedButton(
                                onClick = onResetSalatPreferences,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ডিফল্ট মানদণ্ড (করাচি ও হানাফী) সেট করুন",
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

// DETAILS MODAL DIALOG (Study Guide & Azkar)
@Composable
fun SalatDetailsDialog(
    target: SalatDialogTarget,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .widthIn(max = 620.dp)
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                when (target) {
                    is SalatDialogTarget.Forbidden -> {
                        ForbiddenDetailsContent(
                            forbiddenInfo = target.forbiddenInfo,
                            onDismiss = onDismiss
                        )
                    }
                    is SalatDialogTarget.Prayer -> {
                        PrayerDetailsContent(
                            prayer = target.prayer,
                            onDismiss = onDismiss
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ForbiddenDetailsContent(
    forbiddenInfo: ForbiddenTimeInfo,
    onDismiss: () -> Unit
) {
    val headerBrush = Brush.verticalGradient(
        listOf(
            Color(0xFFE11D48),
            Color(0xFFB91C1C),
            Color(0xFF881337)
        )
    )

    // Header Banner
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBrush)
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Guide Pill + Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "✨ SALAH STUDY GUIDE & AZKAR",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.25f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Text(
                text = "সালাতের নিষিদ্ধ সময় (Salah Forbidden Time)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = "Three specific intervals when all prayers are strictly prohibited. • কোনো সালাত পড়া যাবে না (No Prayer Permitted)",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Time Bar (Darkened card with 3 columns)
            Surface(
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "সূর্যোদয় (Sunrise)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = forbiddenInfo.sunriseDisplay12,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A),
                            fontSize = 11.5.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "দ্বিপ্রহর (Zenith)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = forbiddenInfo.zawalDisplay12,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A),
                            fontSize = 11.5.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "সূর্যাস্ত (Sunset)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = forbiddenInfo.sunsetDisplay12,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A),
                            fontSize = 11.5.sp
                        )
                    }
                }
            }
        }
    }

    // Body Content Cards
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card 1: Rakats & Rules Breakdown
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "রাকাত ও নিয়ম (RAKATS BREAKDOWN)",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4338CA),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = forbiddenInfo.rulesBn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // Card 2: Physical & Mental/Spiritual Benefits & Permitted Dhikr
        Surface(
            color = Color(0xFFF0FDF4),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF0D9488),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "শারীরিক ও মানসিক উপকারিতা ও বৈধ আমল",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = forbiddenInfo.benefitsBn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1E293B),
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun PrayerDetailsContent(
    prayer: PrayerTimeItem,
    onDismiss: () -> Unit
) {
    val headerBrush = when (prayer.id) {
        "fajr" -> Brush.verticalGradient(listOf(Color(0xFF0284C7), Color(0xFF0369A1), Color(0xFF0F172A)))
        "dhuhr" -> Brush.verticalGradient(listOf(Color(0xFFD97706), Color(0xFFB45309), Color(0xFF78350F)))
        "asr" -> Brush.verticalGradient(listOf(Color(0xFFF97316), Color(0xFFEA580C), Color(0xFF991B1B)))
        "maghrib" -> Brush.verticalGradient(listOf(Color(0xFFE11D48), Color(0xFFBE123C), Color(0xFF881337)))
        "isha" -> Brush.verticalGradient(listOf(Color(0xFF4F46E5), Color(0xFF3730A3), Color(0xFF1E1B4B)))
        else -> Brush.verticalGradient(listOf(Color(0xFF059669), Color(0xFF047857), Color(0xFF064E3B)))
    }

    // Header Banner
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerBrush)
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Guide Pill + Close Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "✨ SALAH STUDY GUIDE & AZKAR",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.25f))
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Main Title
            Text(
                text = "${prayer.nameBn} সালাত (${prayer.nameEn} Prayer)",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtitle
            Text(
                text = prayer.studyGuideSubtitleBn.ifEmpty { prayer.subtitleEn },
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.88f),
                fontSize = 11.5.sp,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Time Bar (Darkened card with 3 columns: Start, End, Duration)
            Surface(
                color = Color.Black.copy(alpha = 0.35f),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "শুরু (Start Time)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = prayer.startTimeFormatted.ifEmpty { prayer.timeFormatted },
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A),
                            fontSize = 13.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "শেষ (End Time)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = prayer.endTimeFormatted,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A),
                            fontSize = 13.sp
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = "সময়সীমা (Duration)",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 9.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = prayer.durationBn,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    // Body Content Cards
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Card 1: Rakats Breakdown
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "রাকাত ও নিয়ম (RAKATS BREAKDOWN)",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4338CA),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "${prayer.rakatsSummaryBn}\n\n${prayer.rakatsDetailBn}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }
        }

        // Card 2: Physical & Mental/Spiritual Benefits
        Surface(
            color = Color(0xFFF0FDF4),
            shape = RoundedCornerShape(18.dp),
            border = BorderStroke(1.dp, Color(0xFFBBF7D0)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = null,
                        tint = Color(0xFF0D9488),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "শারীরিক ও মানসিক উপকারিতা",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F766E),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = prayer.benefitsBn,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1E293B),
                    fontSize = 12.5.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}
