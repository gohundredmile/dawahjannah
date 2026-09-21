package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.ui.theme.IslamicGold
import com.example.util.NamazModeManager
import kotlinx.coroutines.delay

@Composable
fun NamazModeDialog(
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val isDark = isSystemInDarkTheme()

    // Real hardware & preference states
    var hasDndPermission by remember { mutableStateOf(NamazModeManager.hasDndPermission(context)) }
    var currentRingerMode by remember { mutableIntStateOf(NamazModeManager.getCurrentRingerMode(context)) }
    var isNamazActive by remember { mutableStateOf(NamazModeManager.isNamazModeActive(context)) }
    var remainingMinutes by remember { mutableIntStateOf(NamazModeManager.getRemainingMinutes(context)) }

    var isAutoSilentEnabled by remember { mutableStateOf(NamazModeManager.isAutoSilentEnabled(context)) }
    var isVibrationEnabled by remember { mutableStateOf(NamazModeManager.isVibrationEnabled(context)) }
    var isAzanAlertEnabled by remember { mutableStateOf(NamazModeManager.isAzanAlertEnabled(context)) }
    var silentDurationMins by remember { mutableIntStateOf(NamazModeManager.getSilentDurationMins(context)) }

    // Re-check hardware permission & mode on resume (when returning from phone settings)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasDndPermission = NamazModeManager.hasDndPermission(context)
                currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
                isNamazActive = NamazModeManager.isNamazModeActive(context)
                remainingMinutes = NamazModeManager.getRemainingMinutes(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Refresh countdown timer every 10 seconds if active
    LaunchedEffect(isNamazActive) {
        while (isNamazActive) {
            remainingMinutes = NamazModeManager.getRemainingMinutes(context)
            isNamazActive = NamazModeManager.isNamazModeActive(context)
            currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
            delay(10000)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false, decorFitsSystemWindows = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding(),
            color = if (isDark) Color(0xFF041910) else MaterialTheme.colorScheme.background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (isDark) Color(0x33FFFFFF) else MaterialTheme.colorScheme.surfaceVariant,
                        border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                        modifier = Modifier.size(38.dp)
                    ) {
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "ফিরে যান",
                                tint = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VolumeOff,
                                contentDescription = null,
                                tint = Color(0xFFE11D48),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "নামাজ মোড ও সাইলেন্ট সেটিংস",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isDark) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = "সালাতের সময় মোবাইল স্বয়ংক্রিয় নিঃশব্দ ও ডিভাইস সংযোগ",
                            fontSize = 11.5.sp,
                            color = if (isDark) Color(0xFFCBD5E1) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // --- 1. Phone Hardware Connection & DND Permission Status Card ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (hasDndPermission) {
                                if (isDark) Color(0xFF063321) else Color(0xFFECFDF5)
                            } else {
                                if (isDark) Color(0xFF331518) else Color(0xFFFFF1F2)
                            },
                            border = BorderStroke(
                                1.2.dp,
                                if (hasDndPermission) Color(0xFF10B981) else Color(0xFFE11D48)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (hasDndPermission) Color(0xFF10B981).copy(alpha = 0.2f) else Color(0xFFE11D48).copy(alpha = 0.2f),
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (hasDndPermission) Icons.Default.CheckCircle else Icons.Default.Warning,
                                                contentDescription = null,
                                                tint = if (hasDndPermission) Color(0xFF10B981) else Color(0xFFE11D48),
                                                modifier = Modifier.size(22.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(12.dp))

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = if (hasDndPermission) "ফোনের সাথে সফলভাবে সংযুক্ত" else "ফোনের সাথে সংযোগ স্থাপন প্রয়োজন",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (hasDndPermission) {
                                                if (isDark) Color(0xFF6EE7B7) else Color(0xFF047857)
                                            } else {
                                                if (isDark) Color(0xFFFDA4AF) else Color(0xFFBE123C)
                                            }
                                        )
                                        Text(
                                            text = if (hasDndPermission) {
                                                "ডিভাইস অডিও কন্ট্রোল ও সাইলেন্ট পারমিশন প্রস্তুত"
                                            } else {
                                                "নামাজে ফোন নিঃশব্দ করতে DND (Do Not Disturb) পারমিশন দিন"
                                            },
                                            fontSize = 12.sp,
                                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Current phone ringer status display
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isDark) Color(0x33000000) else Color(0xFFF1F5F9),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PhoneAndroid,
                                            contentDescription = null,
                                            tint = if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "বর্তমান ফোনের রিংগার: ${NamazModeManager.getRingerModeTitleBn(currentRingerMode)}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = if (isDark) Color(0xFFE2E8F0) else Color(0xFF334155)
                                        )
                                    }
                                }

                                if (!hasDndPermission) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "অ্যান্ড্রয়েডের নিরাপত্তা পলিসির কারণে ফোনকে সাইলেন্ট/মিউট করতে অ্যাপকে একবারের জন্য 'Do Not Disturb' অনুমতি দিতে হবে। নিচের বাটনে চাপ দিয়ে ফোনে অনুমতি দিন:",
                                        fontSize = 11.5.sp,
                                        color = if (isDark) Color(0xFFFCA5A5) else Color(0xFF991B1B),
                                        lineHeight = 16.sp
                                    )
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Button(
                                        onClick = {
                                            NamazModeManager.openDndSettings(context)
                                            Toast.makeText(context, "তালিকায় 'দা'ওয়াহ টু জান্নাহ' অ্যাপ সিলেক্ট করে অনুমতি অন করুন", Toast.LENGTH_LONG).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE11D48)),
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "ফোনের সাথে সংযোগ স্থাপন করুন (DND অনুমতি দিন)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // --- 2. Live Namaz Mode Toggle Controller Card ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isNamazActive) {
                                if (isDark) Color(0xFF0F382A) else Color(0xFFF0FDF4)
                            } else {
                                if (isDark) Color(0xFF0A281C) else MaterialTheme.colorScheme.surface
                            },
                            border = BorderStroke(
                                1.5.dp,
                                if (isNamazActive) IslamicGold else (if (isDark) IslamicGold.copy(alpha = 0.3f) else Color(0xFFCBD5E1))
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            shape = CircleShape,
                                            color = if (isNamazActive) IslamicGold else Color(0xFFE11D48).copy(alpha = 0.15f),
                                            modifier = Modifier.size(42.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = if (isNamazActive) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                                                    contentDescription = null,
                                                    tint = if (isNamazActive) Color(0xFF041910) else Color(0xFFE11D48),
                                                    modifier = Modifier.size(22.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(12.dp))

                                        Column {
                                            Text(
                                                text = if (isNamazActive) "নামাজ মোড চালু আছে (সাইলেন্ট)" else "তাত্ক্ষণিক নামাজ মোড কন্ট্রোলার",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDark) Color.White else Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = if (isNamazActive) "বাকি সময়: প্রায় $remainingMinutes মিনিট" else "নামাজে প্রবেশের পূর্বে ফোনটি এখনই নিঃশব্দ করুন",
                                                fontSize = 12.sp,
                                                color = if (isNamazActive) IslamicGold else (if (isDark) Color(0xFF94A3B8) else Color(0xFF64748B))
                                            )
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                if (isNamazActive) {
                                    Button(
                                        onClick = {
                                            NamazModeManager.deactivateNamazMode(context)
                                            isNamazActive = false
                                            currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
                                            Toast.makeText(context, "স্বাভাবিক সাউন্ড পুনরায় চালু করা হয়েছে", Toast.LENGTH_SHORT).show()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stop,
                                            contentDescription = null,
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "স্বাভাবিক সাউন্ডে ফিরুন (Normal Mode)",
                                            fontSize = 13.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Button(
                                        onClick = {
                                            if (!hasDndPermission) {
                                                NamazModeManager.openDndSettings(context)
                                                Toast.makeText(context, "ফোনকে সাইলেন্ট করার জন্য প্রথমে DND অনুমতি দিন", Toast.LENGTH_LONG).show()
                                            } else {
                                                val res = NamazModeManager.activateNamazMode(context, silentDurationMins, isVibrationEnabled)
                                                if (res.isSuccess) {
                                                    isNamazActive = true
                                                    remainingMinutes = silentDurationMins
                                                    currentRingerMode = NamazModeManager.getCurrentRingerMode(context)
                                                    Toast.makeText(context, "নামাজ মোড সক্রিয়: $silentDurationMins মিনিটের জন্য ফোন সাইলেন্ট করা হয়েছে", Toast.LENGTH_LONG).show()
                                                } else {
                                                    Toast.makeText(context, "ত্রুটি: ফোনের পারমিশন চেক করুন", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.PlayArrow,
                                            contentDescription = null,
                                            tint = Color(0xFF041910),
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "এখনই নামাজ মোড (সাইলেন্ট) সক্রিয় করুন ($silentDurationMins মিনিট)",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF041910)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // --- 3. Silent Duration Selector ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isDark) Color(0xFF0A281C) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isDark) IslamicGold.copy(alpha = 0.3f) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Timer,
                                        contentDescription = null,
                                        tint = IslamicGold,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "নামাজ মোডের সময়কাল (নিঃশব্দ স্থায়িত্ব)",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isDark) Color.White else Color(0xFF0F172A)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "সালাত চলাকালীন নির্বাচিত সময় শেষ হলে ফোন স্বাভাবিক শব্দে ফিরে আসবে:",
                                    fontSize = 12.sp,
                                    color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val durations = listOf(15, 20, 30, 45, 60)
                                    durations.forEach { mins ->
                                        val isSelected = silentDurationMins == mins
                                        Surface(
                                            shape = RoundedCornerShape(10.dp),
                                            color = if (isSelected) IslamicGold else (if (isDark) Color(0x33FFFFFF) else Color(0xFFF1F5F9)),
                                            border = BorderStroke(1.dp, if (isSelected) IslamicGold else Color(0x33888888)),
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable {
                                                    silentDurationMins = mins
                                                    NamazModeManager.setSilentDurationMins(context, mins)
                                                }
                                        ) {
                                            Box(
                                                contentAlignment = Alignment.Center,
                                                modifier = Modifier.padding(vertical = 10.dp)
                                            ) {
                                                Text(
                                                    text = "${mins}মি.",
                                                    fontSize = 12.5.sp,
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isSelected) Color(0xFF041910) else (if (isDark) Color.White else Color(0xFF334155))
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // --- 4. Auto Silent Mode Setting ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isDark) Color(0xFF0A281C) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isDark) IslamicGold.copy(alpha = 0.3f) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.weight(1f),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFFE11D48).copy(alpha = 0.15f),
                                        modifier = Modifier.size(44.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Icon(
                                                imageVector = if (isAutoSilentEnabled) Icons.Default.NotificationsOff else Icons.Default.NotificationsActive,
                                                contentDescription = null,
                                                tint = Color(0xFFE11D48),
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "স্বয়ংক্রিয় সাইলেন্ট মোড",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDark) Color.White else Color(0xFF0F172A)
                                        )
                                        Text(
                                            text = "নামাজের ওয়াক্তে স্বয়ংক্রিয় সাইলেন্ট অ্যালার্ম সমন্বয়",
                                            fontSize = 12.sp,
                                            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)
                                        )
                                    }
                                }

                                Switch(
                                    checked = isAutoSilentEnabled,
                                    onCheckedChange = {
                                        isAutoSilentEnabled = it
                                        NamazModeManager.setAutoSilentEnabled(context, it)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = IslamicGold
                                    )
                                )
                            }
                        }
                    }

                    // --- 5. Mosque Jamat Vibration Mode with Real Vibration Test ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isDark) Color(0xFF0A281C) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isDark) Color(0x334ADE80) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF10B981).copy(alpha = 0.15f),
                                            modifier = Modifier.size(44.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Vibration,
                                                    contentDescription = null,
                                                    tint = Color(0xFF10B981),
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "মসজিদ জামাত ভাইব্রেশন",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDark) Color.White else Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "সম্পূর্ণ নিঃশব্দের পাশাপাশি সতর্কতামূলক কম্পন",
                                                fontSize = 12.sp,
                                                color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = isVibrationEnabled,
                                        onCheckedChange = {
                                            isVibrationEnabled = it
                                            NamazModeManager.setVibrationEnabled(context, it)
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF10B981)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                // Real hardware test button
                                OutlinedButton(
                                    onClick = {
                                        NamazModeManager.testVibration(context)
                                        Toast.makeText(context, "ভাইব্রেশন সম্পন্ন হয়েছে", Toast.LENGTH_SHORT).show()
                                    },
                                    border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Vibration,
                                        contentDescription = null,
                                        tint = Color(0xFF10B981),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "ফোনের ভাইব্রেশন পরীক্ষা করুন (Test Vibration)",
                                        fontSize = 12.sp,
                                        color = if (isDark) Color(0xFF6EE7B7) else Color(0xFF047857)
                                    )
                                }
                            }
                        }
                    }

                    // --- 6. Waqt Alert Reminder with Sound Test ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = if (isDark) Color(0xFF0A281C) else MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.dp, if (isDark) Color(0x3338BDF8) else Color(0xFFE2E8F0)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.weight(1f),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF0284C7).copy(alpha = 0.15f),
                                            modifier = Modifier.size(44.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.VolumeUp,
                                                    contentDescription = null,
                                                    tint = Color(0xFF0284C7),
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "ওয়াক্ত শুরু সতর্কবার্তা",
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isDark) Color.White else Color(0xFF0F172A)
                                            )
                                            Text(
                                                text = "ফজর, যোহর, আসর, মাগরিব ও এশার ওয়াক্তে নোটিফিকেশন",
                                                fontSize = 12.sp,
                                                color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF64748B)
                                            )
                                        }
                                    }

                                    Switch(
                                        checked = isAzanAlertEnabled,
                                        onCheckedChange = {
                                            isAzanAlertEnabled = it
                                            NamazModeManager.setAzanAlertEnabled(context, it)
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedThumbColor = Color.White,
                                            checkedTrackColor = Color(0xFF0284C7)
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                OutlinedButton(
                                    onClick = {
                                        NamazModeManager.testSoundAlert(context)
                                        Toast.makeText(context, "অ্যালার্ট টোন বাজানো হয়েছে", Toast.LENGTH_SHORT).show()
                                    },
                                    border = BorderStroke(1.dp, Color(0xFF0284C7).copy(alpha = 0.5f)),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = null,
                                        tint = Color(0xFF0284C7),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "নোটিফিকেশন চিম পরীক্ষা করুন (Test Sound)",
                                        fontSize = 12.sp,
                                        color = if (isDark) Color(0xFF7DD3FC) else Color(0xFF0284C7)
                                    )
                                }
                            }
                        }
                    }

                    // --- 7. Mosque Etiquette Guidance Card ---
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = IslamicGold.copy(alpha = if (isDark) 0.15f else 0.1f),
                            border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "মসজিদের পবিত্রতা ও সালাতে একাগ্রতা",
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isDark) IslamicGold else Color(0xFF92400E)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "সালাতের ওয়াক্তে বা মসজিদে প্রবেশের পূর্বে ফোনকে সাইলেন্ট রাখা সুন্নাতসম্মত আদব। নামাজ মোড অন থাকলে নির্ধারিত সময় পার হওয়া মাত্রই ফোন নিজে থেকেই তার আগের স্বাভাবিক সাউন্ড মোডে ফিরে যাবে।",
                                    fontSize = 12.sp,
                                    color = if (isDark) Color(0xFFE2E8F0) else Color(0xFF334155),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
