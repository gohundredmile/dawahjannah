package com.example.ui.components

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
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
import com.example.data.model.ForbiddenTimeInfo
import com.example.data.model.PRESET_SALAT_PLACES
import com.example.data.model.PrayerTimeItem
import com.example.data.model.SalatConfiguration
import com.example.data.model.SalatPlaceInfo
import com.example.util.CalendarHelper
import com.example.util.PrayerCalculator
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
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
                        Surface(
                            color = if (salatConfig.isGpsEnabled) Color(0xFF0284C7).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                            shape = CircleShape,
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (salatConfig.isGpsEnabled) Icons.Default.MyLocation else Icons.Default.LocationOn,
                                    contentDescription = "Location",
                                    tint = if (salatConfig.isGpsEnabled) Color(0xFF0284C7) else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = salatConfig.placeNameBn,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 14.sp
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

                            Text(
                                text = "${String.format(Locale.US, "%.2f°N, %.2f°E", salatConfig.latitude, salatConfig.longitude)} • ${if (salatConfig.isHanafiAsr) "হানাফী আসর" else "শাফেয়ী আসর"}${if (salatConfig.manualOffsetMinutes != 0) " (${if (salatConfig.manualOffsetMinutes > 0) "+" else ""}${salatConfig.manualOffsetMinutes} মি.)" else ""}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
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

                        // Place & Settings Button
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                            modifier = Modifier.clickable { showConfigDialog = true }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Tune,
                                    contentDescription = "Configure",
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "স্থান পরিবর্তন",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
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
            verticalArrangement = Arrangement.spacedBy(10.dp)
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

    val nonActiveBg = when (prayer.id) {
        "fajr" -> Color(0xFFF0F9FF)
        "dhuhr" -> Color(0xFFFEFCE8)
        "asr" -> Color(0xFFFFF7ED)
        "maghrib" -> Color(0xFFFFF1F2)
        "isha" -> Color(0xFFF8FAFC)
        else -> Color.White
    }

    val nonActiveBorder = when (prayer.id) {
        "fajr" -> Color(0xFFBAE6FD)
        "dhuhr" -> Color(0xFFFDE047)
        "asr" -> Color(0xFFFDBA74)
        "maghrib" -> Color(0xFFFECDD3)
        "isha" -> Color(0xFFCBD5E1)
        else -> Color(0xFFE2E8F0)
    }

    val tagColor = when {
        isHighlighted -> Color.White
        prayer.id == "fajr" -> Color(0xFF0284C7)
        prayer.id == "dhuhr" -> Color(0xFFD97706)
        prayer.id == "asr" -> Color(0xFFEA580C)
        prayer.id == "maghrib" -> Color(0xFFE11D48)
        prayer.id == "isha" -> Color(0xFF475569)
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isHighlighted) 6.dp else 1.5.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = if (isHighlighted) Color(0xFFEA580C) else Color.Black.copy(alpha = 0.05f)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = if (isHighlighted) Color.Transparent else nonActiveBg),
        border = if (isHighlighted) null else BorderStroke(1.2.dp, nonActiveBorder)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (isHighlighted) Modifier.background(activeBrush)
                    else Modifier.background(nonActiveBg)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Top Row: Waqt Bengali Name, English Tag & Live indicator
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = prayer.nameBn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = if (isHighlighted) Color.White else Color(0xFF0F172A),
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = if (isHighlighted) Color.White.copy(alpha = 0.2f) else tagColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = prayer.nameEn,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = if (isHighlighted) Color.White else tagColor,
                                fontSize = 10.5.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }

                    if (isHighlighted) {
                        Surface(
                            color = Color.White.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFFEF08A))
                                )
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "চলমান ওয়াক্ত",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    } else {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = prayer.durationBn,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B),
                                fontSize = 11.5.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "View Details",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(13.dp)
                            )
                        }
                    }
                }

                // Subtitle
                Text(
                    text = prayer.subtitleEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isHighlighted) Color.White.copy(alpha = 0.88f) else Color(0xFF64748B),
                    fontSize = 11.5.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Row: START time, END time, and Duration
                Surface(
                    color = if (isHighlighted) Color.Black.copy(alpha = 0.18f) else Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Start Time
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "START:",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHighlighted) Color.White.copy(alpha = 0.8f) else Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = prayer.startTimeFormatted.ifEmpty { prayer.timeFormatted },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = if (isHighlighted) Color(0xFFFEF08A) else Color(0xFF0F172A)
                            )
                        }

                        // Divider dot
                        Text(
                            text = "•",
                            color = if (isHighlighted) Color.White.copy(alpha = 0.5f) else Color(0xFFCBD5E1),
                            fontSize = 14.sp
                        )

                        // End Time
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "END:",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isHighlighted) Color.White.copy(alpha = 0.8f) else Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = prayer.endTimeFormatted,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.5.sp,
                                color = if (isHighlighted) Color(0xFFFEF08A) else Color(0xFF0F172A)
                            )
                        }
                    }
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
    val crimsonGradient = Brush.horizontalGradient(
        listOf(
            Color(0xFFE11D48),
            Color(0xFFB91C1C),
            Color(0xFF881337)
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(20.dp), ambientColor = Color(0xFFDC2626))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(crimsonGradient)
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "সালাতের নিষিদ্ধ সময়",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = "FORBIDDEN",
                                    color = Color.White,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Three specific intervals • ৩টি সময়ে সকল সালাত নিষিদ্ধ",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        color = Color(0xFF450A0A).copy(alpha = 0.85f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFF87171).copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚠ নামাজ নিষেধ",
                                color = Color(0xFFFCA5A5),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 3 Intervals Display in 3 Columns
                Surface(
                    color = Color.Black.copy(alpha = 0.35f),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Sunrise
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "সূর্যোদয়",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${forbiddenInfo.sunriseStart24} – ${forbiddenInfo.sunriseEnd24}",
                                color = Color(0xFFFEF08A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Zenith
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "দ্বিপ্রহর (জাওয়াল)",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${forbiddenInfo.zawalStart24} – ${forbiddenInfo.zawalEnd24}",
                                color = Color(0xFFFEF08A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        // Sunset
                        Column(
                            modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "সূর্যাস্ত",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 10.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${forbiddenInfo.sunsetStart24} – ${forbiddenInfo.sunsetEnd24}",
                                color = Color(0xFFFEF08A),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "বৈধ আমল: রকু-সিজদাহ ছাড়া জিকির, ইস্তিগফার ও দোয়া",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 10.5.sp
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "বিস্তারিত নিয়ম",
                            color = Color(0xFFFEF08A),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Details",
                            tint = Color(0xFFFEF08A),
                            modifier = Modifier.size(12.dp)
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

                    // 3. Juristic School (Asr Calculation) & Time Offset Settings
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
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
                                text = "ওয়াক্ত হিসাবের নিয়ম ও ফাইন-টিউনিং",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.5.sp,
                                color = MaterialTheme.colorScheme.primary
                            )

                            // Hanafi / Shafi'i Asr Toggle
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "আসর ওয়াক্ত হিসাব পদ্ধতি",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (currentConfig.isHanafiAsr) "হানাফী মাযহাব (ছায়া দ্বিগুণ হলে আসর শুরু)" else "শাফেয়ী/মালেকী/হাম্বলী (ছায়া এক গুণ হলে আসর)",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                Switch(
                                    checked = currentConfig.isHanafiAsr,
                                    onCheckedChange = { onToggleHanafi(it) },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = MaterialTheme.colorScheme.primary
                                    )
                                )
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
