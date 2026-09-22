package com.example.ui.screens.sub

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.ToneGenerator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ScreenRotation
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.SalatConfigDialog
import com.example.ui.theme.IslamicGold
import com.example.ui.viewmodel.MainViewModel
import com.example.util.QiblaHelper
import com.example.util.VibrationHelper
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaCompassScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val salatConfig by viewModel.salatConfig.collectAsState()
    val gpsStatusMessage by viewModel.gpsStatusMessage.collectAsState()

    // Location coordinates (defaults to Dhaka if not set)
    val userLat = salatConfig.latitude
    val userLng = salatConfig.longitude

    // True Qibla Bearing and distance
    val qiblaBearing = remember(userLat, userLng) {
        QiblaHelper.calculateQiblaBearing(userLat, userLng)
    }
    val distanceKm = remember(userLat, userLng) {
        QiblaHelper.calculateDistanceToKaabaKm(userLat, userLng)
    }
    val magneticDeclination = remember(userLat, userLng) {
        QiblaHelper.getMagneticDeclination(userLat, userLng)
    }

    // Sensor State
    var rawAzimuth by remember { mutableFloatStateOf(0f) }
    var currentHeading by remember { mutableFloatStateOf(0f) }
    var pitch by remember { mutableFloatStateOf(0f) }
    var roll by remember { mutableFloatStateOf(0f) }
    var sensorAccuracy by remember { mutableIntStateOf(SensorManager.SENSOR_STATUS_ACCURACY_HIGH) }
    var hasSensors by remember { mutableStateOf(true) }
    var isManualSimulation by remember { mutableStateOf(false) }
    var manualHeading by remember { mutableFloatStateOf(qiblaBearing) }

    // User settings
    var isVibrationEnabled by remember { mutableStateOf(true) }
    var isSoundEnabled by remember { mutableStateOf(false) }
    var isRotatingDialMode by remember { mutableStateOf(true) } // Rotating Dial vs Rotating Needle
    var isTrueNorthEnabled by remember { mutableStateOf(true) }

    // Dialog states
    var showCalibrationDialog by remember { mutableStateOf(false) }
    var showPlaceSelectionDialog by remember { mutableStateOf(false) }
    var showSolarQiblaDialog by remember { mutableStateOf(false) }
    var showRulingsDialog by remember { mutableStateOf(false) }

    // Audio Tone Generator for subtle click
    val toneGen = remember {
        try {
            ToneGenerator(AudioManager.STREAM_NOTIFICATION, 35)
        } catch (_: Exception) {
            null
        }
    }

    // Sensor Listener Setup
    DisposableEffect(Unit) {
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as? SensorManager
        val accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        if (sensorManager == null || accelerometer == null || magnetometer == null) {
            hasSensors = false
            isManualSimulation = true
        } else {
            hasSensors = true
            val gravity = FloatArray(3)
            val geomagnetic = FloatArray(3)
            var hasGravity = false
            var hasGeomagnetic = false

            val sensorListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val alpha = 0.18f
                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        gravity[0] += alpha * (event.values[0] - gravity[0])
                        gravity[1] += alpha * (event.values[1] - gravity[1])
                        gravity[2] += alpha * (event.values[2] - gravity[2])
                        hasGravity = true
                    } else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
                        geomagnetic[0] += alpha * (event.values[0] - geomagnetic[0])
                        geomagnetic[1] += alpha * (event.values[1] - geomagnetic[1])
                        geomagnetic[2] += alpha * (event.values[2] - geomagnetic[2])
                        hasGeomagnetic = true
                    }

                    if (hasGravity && hasGeomagnetic) {
                        val rMatrix = FloatArray(9)
                        val iMatrix = FloatArray(9)
                        if (SensorManager.getRotationMatrix(rMatrix, iMatrix, gravity, geomagnetic)) {
                            val orientation = FloatArray(3)
                            SensorManager.getOrientation(rMatrix, orientation)
                            val azimuthRad = orientation[0]
                            val pitchRad = orientation[1]
                            val rollRad = orientation[2]

                            val rawDeg = (Math.toDegrees(azimuthRad.toDouble()).toFloat() + 360f) % 360f
                            val pDeg = Math.toDegrees(pitchRad.toDouble()).toFloat()
                            val rDeg = Math.toDegrees(rollRad.toDouble()).toFloat()

                            rawAzimuth = rawDeg
                            pitch = pDeg
                            roll = rDeg

                            val declinationAdjusted = if (isTrueNorthEnabled) {
                                (rawDeg + magneticDeclination + 360f) % 360f
                            } else {
                                rawDeg
                            }

                            // Smooth interpolation to avoid jitter
                            currentHeading = QiblaHelper.interpolateDegrees(currentHeading, declinationAdjusted, 0.22f)
                        }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    sensorAccuracy = accuracy
                }
            }

            sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_UI)
            sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_UI)

            onDispose {
                sensorManager.unregisterListener(sensorListener)
                try {
                    toneGen?.release()
                } catch (_: Exception) {}
            }
        }

        onDispose {
            try {
                toneGen?.release()
            } catch (_: Exception) {}
        }
    }

    // Active heading based on real sensor or manual slider
    val activeHeading = if (isManualSimulation) manualHeading else currentHeading

    // Shortest relative angle to Kaaba (-180..180)
    val relativeAngle = remember(activeHeading, qiblaBearing) {
        QiblaHelper.calculateRelativeAngle(activeHeading, qiblaBearing)
    }

    // Alignment status: Within 2.5 degrees is considered perfectly facing Kaaba
    val isAligned = abs(relativeAngle) <= 2.5f
    val isNearAligned = abs(relativeAngle) <= 8.0f

    // Device Tilt / Level: Flat if pitch and roll are within 16 degrees
    val isDeviceLevel = abs(pitch) <= 16f && abs(roll) <= 16f

    // Haptic & Sound Feedback when entering alignment
    var lastFeedbackTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(isAligned) {
        if (isAligned) {
            val now = System.currentTimeMillis()
            if (now - lastFeedbackTime > 900L) {
                lastFeedbackTime = now
                if (isVibrationEnabled) {
                    VibrationHelper.vibrateGoalReached(context)
                }
                if (isSoundEnabled) {
                    try {
                        toneGen?.startTone(ToneGenerator.TONE_PROP_BEEP, 70)
                    } catch (_: Exception) {}
                }
            }
        }
    }

    // Infinite breathing glow animation for Kaaba beacon
    val infiniteTransition = rememberInfiniteTransition(label = "qibla_glow")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseGlow"
    )

    // Needle or dial smooth rotation angle
    val animatedDialRotation by animateFloatAsState(
        targetValue = if (isRotatingDialMode) -activeHeading else 0f,
        animationSpec = tween(durationMillis = 80, easing = LinearEasing),
        label = "dialRotation"
    )
    val animatedNeedleRotation by animateFloatAsState(
        targetValue = if (isRotatingDialMode) qiblaBearing - activeHeading else activeHeading,
        animationSpec = tween(durationMillis = 80, easing = LinearEasing),
        label = "needleRotation"
    )

    // Colors
    val midnightBackground = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF060B14),
            Color(0xFF0B1424),
            Color(0xFF0F1E36)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(midnightBackground)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // TOP CELESTIAL HEADER
            QiblaTopHeader(
                placeNameBn = salatConfig.placeNameBn,
                hasSensors = hasSensors,
                isManualMode = isManualSimulation,
                sensorAccuracy = sensorAccuracy,
                onBack = onBack,
                onOpenCalibration = { showCalibrationDialog = true },
                onSelectLocation = { showPlaceSelectionDialog = true },
                onToggleSimulation = { isManualSimulation = !isManualSimulation }
            )

            // SCROLLABLE CONTENT
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 1. LIVE DIRECTION STATUS CARD
                item {
                    QiblaStatusBanner(
                        activeHeading = activeHeading,
                        qiblaBearing = qiblaBearing,
                        relativeAngle = relativeAngle,
                        isAligned = isAligned,
                        isNearAligned = isNearAligned,
                        isDeviceLevel = isDeviceLevel,
                        distanceKm = distanceKm
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // 2. MAIN ASTROLABE COMPASS DIAL
                item {
                    Box(
                        modifier = Modifier
                            .size(310.dp)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val textMeasurer = rememberTextMeasurer()

                        // Astrolabe Dial Canvas
                        Canvas(
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(isManualSimulation) {
                                    if (isManualSimulation) {
                                        detectDragGestures { change, _ ->
                                            change.consume()
                                            val center = Offset(size.width / 2f, size.height / 2f)
                                            val pos = change.position
                                            val angleRad = Math.atan2((pos.y - center.y).toDouble(), (pos.x - center.x).toDouble())
                                            var deg = (Math.toDegrees(angleRad).toFloat() + 90f + 360f) % 360f
                                            manualHeading = deg
                                        }
                                    }
                                }
                        ) {
                            drawAstrolabeCompass(
                                dialRotation = animatedDialRotation,
                                qiblaBearing = qiblaBearing,
                                activeHeading = activeHeading,
                                isAligned = isAligned,
                                isRotatingDialMode = isRotatingDialMode,
                                pulseGlow = pulseGlow,
                                pitch = pitch,
                                roll = roll,
                                isDeviceLevel = isDeviceLevel,
                                textMeasurer = textMeasurer
                            )
                        }

                        // Center Kaaba Aligned Aura & Bubble Level
                        CenterLevelAndKaabaOverlay(
                            isAligned = isAligned,
                            isDeviceLevel = isDeviceLevel,
                            pitch = pitch,
                            roll = roll,
                            pulseGlow = pulseGlow
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 3. MANUAL SIMULATION SLIDER (For devices without magnetometer or testing)
                if (isManualSimulation) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B).copy(alpha = 0.9f)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            Icons.Default.Tune,
                                            contentDescription = null,
                                            tint = Color(0xFF38BDF8),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            "ম্যানুয়াল কম্পাস টেস্ট মোড",
                                            color = Color(0xFF38BDF8),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Text(
                                        "${QiblaHelper.toBengaliDigits(manualHeading.toInt())}°",
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Slider(
                                    value = manualHeading,
                                    onValueChange = { manualHeading = it },
                                    valueRange = 0f..360f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = Color(0xFF38BDF8),
                                        activeTrackColor = Color(0xFF38BDF8)
                                    )
                                )
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    TextButton(
                                        onClick = { manualHeading = qiblaBearing },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("সরাসরি ক্বিবলায় মিলান (${QiblaHelper.toBengaliDigits(qiblaBearing.toInt())}°)", color = IslamicGold, fontSize = 11.sp)
                                    }
                                    TextButton(
                                        onClick = { manualHeading = 0f },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text("উত্তর (০°)", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                // 4. QUICK TOGGLES & TOOLS BAR
                item {
                    CompassControlsRow(
                        isVibrationEnabled = isVibrationEnabled,
                        onToggleVibration = { isVibrationEnabled = !isVibrationEnabled },
                        isSoundEnabled = isSoundEnabled,
                        onToggleSound = { isSoundEnabled = !isSoundEnabled },
                        isRotatingDialMode = isRotatingDialMode,
                        onToggleDialMode = { isRotatingDialMode = !isRotatingDialMode },
                        onOpenCalibration = { showCalibrationDialog = true }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                // 5. RICH INFORMATION CARDS
                item {
                    QiblaDetailsSection(
                        userLat = userLat,
                        userLng = userLng,
                        placeNameBn = salatConfig.placeNameBn,
                        qiblaBearing = qiblaBearing,
                        distanceKm = distanceKm,
                        declination = magneticDeclination,
                        onOpenSolarQibla = { showSolarQiblaDialog = true },
                        onOpenRulings = { showRulingsDialog = true },
                        onSelectLocation = { showPlaceSelectionDialog = true }
                    )
                    Spacer(modifier = Modifier.height(18.dp))
                }
            }
        }
    }

    // DIALOGS
    if (showCalibrationDialog) {
        CompassCalibrationDialog(
            accuracy = sensorAccuracy,
            onDismiss = { showCalibrationDialog = false }
        )
    }

    if (showSolarQiblaDialog) {
        SolarQiblaGuideDialog(
            qiblaBearing = qiblaBearing,
            onDismiss = { showSolarQiblaDialog = false }
        )
    }

    if (showRulingsDialog) {
        QiblaRulingsDialog(
            onDismiss = { showRulingsDialog = false }
        )
    }

    if (showPlaceSelectionDialog) {
        SalatConfigDialog(
            currentConfig = salatConfig,
            onDismiss = { showPlaceSelectionDialog = false },
            onSelectPlace = { place ->
                viewModel.updateSalatPlace(place)
                showPlaceSelectionDialog = false
            },
            onCustomPlace = { nameBn, nameEn, lat, lng ->
                viewModel.setCustomSalatLocation(nameBn, nameEn, lat, lng)
                showPlaceSelectionDialog = false
            },
            onSetOffset = { offset -> viewModel.setSalatManualOffset(offset) },
            onToggleHanafi = { isHanafi -> viewModel.setHanafiAsr(isHanafi) },
            onSelectCalculationMethod = { method -> viewModel.setCalculationMethod(method) },
            onSelectAsrMethod = { asr -> viewModel.setAsrJuristicMethod(asr) },
            onSelectHighLatitudeRule = { rule -> viewModel.setHighLatitudeRule(rule) },
            onResetSalatPreferences = { viewModel.resetSalatPreferencesToStandard() },
            onTrackGps = {
                viewModel.trackCurrentLocationWithGps()
                showPlaceSelectionDialog = false
            }
        )
    }
}

// ------------------------------------------------------------------------------------------------
// TOP HEADER
// ------------------------------------------------------------------------------------------------
@Composable
private fun QiblaTopHeader(
    placeNameBn: String,
    hasSensors: Boolean,
    isManualMode: Boolean,
    sensorAccuracy: Int,
    onBack: () -> Unit,
    onOpenCalibration: () -> Unit,
    onSelectLocation: () -> Unit,
    onToggleSimulation: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "ক্বিবলা কম্পাস",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(IslamicGold.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            "সেন্সর চালিত",
                            color = IslamicGold,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Row(
                    modifier = Modifier.clickable { onSelectLocation() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        placeNameBn,
                        color = Color(0xFF94A3B8),
                        fontSize = 12.sp,
                        maxLines = 1
                    )
                }
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            // Accuracy Badge or Simulation toggle
            if (hasSensors && !isManualMode) {
                val (accText, accColor) = when (sensorAccuracy) {
                    SensorManager.SENSOR_STATUS_ACCURACY_HIGH -> "উচ্চ নির্ভুলতা" to Color(0xFF10B981)
                    SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM -> "মধ্যম নির্ভুলতা" to Color(0xFFF59E0B)
                    else -> "ক্যালিব্রেট" to Color(0xFFEF4444)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(accColor.copy(alpha = 0.15f))
                        .border(1.dp, accColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .clickable { onOpenCalibration() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(accColor)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            accText,
                            color = accColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            IconButton(onClick = onToggleSimulation) {
                Icon(
                    Icons.Default.RotateRight,
                    contentDescription = "Toggle Manual Mode",
                    tint = if (isManualMode) Color(0xFF38BDF8) else Color(0xFF64748B)
                )
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// LIVE DIRECTION STATUS BANNER
// ------------------------------------------------------------------------------------------------
@Composable
private fun QiblaStatusBanner(
    activeHeading: Float,
    qiblaBearing: Float,
    relativeAngle: Float,
    isAligned: Boolean,
    isNearAligned: Boolean,
    isDeviceLevel: Boolean,
    distanceKm: Double
) {
    val bannerColor by animateColorAsState(
        targetValue = when {
            isAligned -> Color(0xFF059669)
            isNearAligned -> Color(0xFFD97706)
            else -> Color(0xFF1E293B)
        },
        animationSpec = tween(400),
        label = "bannerColor"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = bannerColor.copy(alpha = 0.9f)),
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isAligned) Color(0xFF34D399) else Color(0xFF334155)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isAligned) 8.dp else 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Large Degree Display & Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "বর্তমান দিক",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                    Text(
                        "${QiblaHelper.toBengaliDigits(activeHeading.toInt())}° ${QiblaHelper.getDirectionBn(activeHeading).substringBefore(" ")}",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Kaaba Bearing Badge
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "পবিত্র কাবার কোণ",
                        color = Color(0xFFCBD5E1),
                        fontSize = 12.sp
                    )
                    Text(
                        "${QiblaHelper.toBengaliDigits(String.format("%.1f", qiblaBearing))}°",
                        color = IslamicGold,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color.White.copy(alpha = 0.15f), thickness = 0.8.dp)
            Spacer(modifier = Modifier.height(10.dp))

            // Main Turn Guidance Message
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (isAligned) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF6EE7B7),
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "আলহামদুলিল্লাহ! আপনি সরাসরি ক্বিবলামুখী",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                } else {
                    val turnDirection = if (relativeAngle > 0) "ডানে" else "বামে"
                    val turnDeg = abs(relativeAngle).toInt()
                    val turnArrow = if (relativeAngle > 0) "↻" else "↺"

                    Text(
                        "$turnArrow আর $turnDirection ${QiblaHelper.toBengaliDigits(turnDeg)}° ঘুরুন (ক্বিবলার জন্য)",
                        color = if (isNearAligned) Color(0xFFFEF08A) else Color(0xFFE2E8F0),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Tilt Warning if phone is not held flat
            if (!isDeviceLevel) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEF4444).copy(alpha = 0.2f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFFCA5A5),
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "সর্বোচ্চ নির্ভুলতার জন্য মোবাইলটি সমতলে বা হাতে সোজা রাখুন",
                        color = Color(0xFFFCA5A5),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

// ------------------------------------------------------------------------------------------------
// ASTROLABE COMPASS DRAWING FUNCTION
// ------------------------------------------------------------------------------------------------
private fun DrawScope.drawAstrolabeCompass(
    dialRotation: Float,
    qiblaBearing: Float,
    activeHeading: Float,
    isAligned: Boolean,
    isRotatingDialMode: Boolean,
    pulseGlow: Float,
    pitch: Float,
    roll: Float,
    isDeviceLevel: Boolean,
    textMeasurer: TextMeasurer
) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension / 2f - 6.dp.toPx()

    // 1. OUTER CELESTIAL RIM GRADIENT
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF0F172A),
                Color(0xFF0B132B),
                Color(0xFF030712)
            ),
            center = center,
            radius = radius
        ),
        radius = radius,
        center = center
    )

    // 2. METALLIC BRONZE & ISLAMIC GOLD BEZEL RINGS
    val bezelColor = if (isAligned) Color(0xFF10B981) else Color(0xFFD4AF37)
    drawCircle(
        color = bezelColor.copy(alpha = 0.85f),
        radius = radius,
        center = center,
        style = Stroke(width = 3.dp.toPx())
    )
    drawCircle(
        color = bezelColor.copy(alpha = 0.35f),
        radius = radius - 6.dp.toPx(),
        center = center,
        style = Stroke(width = 1.dp.toPx())
    )

    // Aligned Glow Aura
    if (isAligned) {
        drawCircle(
            color = Color(0xFF10B981).copy(alpha = 0.25f * pulseGlow),
            radius = radius + 4.dp.toPx(),
            center = center,
            style = Stroke(width = 6.dp.toPx())
        )
    }

    // ROTATING DIAL COMPONENT
    rotate(degrees = dialRotation, pivot = center) {
        // Degree ticks: Every 2° minor, every 10° medium, every 30° major
        for (angle in 0 until 360 step 2) {
            val angleRad = Math.toRadians(angle.toDouble())
            val cosA = cos(angleRad).toFloat()
            val sinA = sin(angleRad).toFloat()

            val isMajor = angle % 30 == 0
            val isMedium = angle % 10 == 0 && !isMajor

            val tickLength = when {
                isMajor -> 14.dp.toPx()
                isMedium -> 9.dp.toPx()
                else -> 4.dp.toPx()
            }
            val tickWidth = when {
                isMajor -> 2.dp.toPx()
                isMedium -> 1.2.dp.toPx()
                else -> 0.8.dp.toPx()
            }
            val tickColor = when {
                isMajor -> Color(0xFFF1F5F9)
                isMedium -> Color(0xFF94A3B8)
                else -> Color(0xFF475569)
            }

            val outerPoint = Offset(
                center.x + (radius - 8.dp.toPx()) * sinA,
                center.y - (radius - 8.dp.toPx()) * cosA
            )
            val innerPoint = Offset(
                center.x + (radius - 8.dp.toPx() - tickLength) * sinA,
                center.y - (radius - 8.dp.toPx() - tickLength) * cosA
            )

            drawLine(
                color = tickColor,
                start = outerPoint,
                end = innerPoint,
                strokeWidth = tickWidth,
                cap = StrokeCap.Round
            )

            // Degree Text for 30° intervals
            if (isMajor && angle % 30 == 0) {
                val textRadius = radius - 30.dp.toPx()
                val textPos = Offset(
                    center.x + textRadius * sinA,
                    center.y - textRadius * cosA
                )
                val degreeStr = QiblaHelper.toBengaliDigits(angle)
                val textLayout = textMeasurer.measure(
                    text = degreeStr,
                    style = TextStyle(
                        color = Color(0xFF94A3B8),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
                drawText(
                    textLayoutResult = textLayout,
                    topLeft = Offset(
                        textPos.x - textLayout.size.width / 2f,
                        textPos.y - textLayout.size.height / 2f
                    )
                )
            }
        }

        // Cardinal directions (North, East, South, West)
        drawCardinalBadge(center, radius, 0f, "উ / N", Color(0xFFEF4444), textMeasurer)
        drawCardinalBadge(center, radius, 90f, "পূ / E", Color(0xFFCBD5E1), textMeasurer)
        drawCardinalBadge(center, radius, 180f, "দ / S", Color(0xFFCBD5E1), textMeasurer)
        drawCardinalBadge(center, radius, 270f, "প / W", Color(0xFFCBD5E1), textMeasurer)

        // KAABA MARKER ON THE DIAL
        val kaabaAngleRad = Math.toRadians(qiblaBearing.toDouble())
        val kaabaSin = sin(kaabaAngleRad).toFloat()
        val kaabaCos = cos(kaabaAngleRad).toFloat()
        val kaabaDistance = radius - 18.dp.toPx()
        val kaabaPos = Offset(
            center.x + kaabaDistance * kaabaSin,
            center.y - kaabaDistance * kaabaCos
        )

        // Golden Laser Beam from Center to Kaaba
        drawLine(
            brush = Brush.radialGradient(
                colors = listOf(
                    (if (isAligned) Color(0xFF10B981) else IslamicGold).copy(alpha = 0.8f * pulseGlow),
                    Color.Transparent
                ),
                center = center,
                radius = kaabaDistance
            ),
            start = center,
            end = kaabaPos,
            strokeWidth = if (isAligned) 4.dp.toPx() else 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Draw Kaaba Cube Icon on Dial
        drawKaabaIcon(kaabaPos, isAligned, pulseGlow)
    }

    // STATIC OR ROTATING NEEDLE
    val needleAngle = if (isRotatingDialMode) 0f else activeHeading
    rotate(degrees = needleAngle, pivot = center) {
        drawCompassNeedle(center, radius * 0.72f, isAligned)
    }

    // TOP REFERENCE MARKER (Arrow pointing straight up from phone)
    val topArrowPath = Path().apply {
        moveTo(center.x, center.y - radius + 2.dp.toPx())
        lineTo(center.x - 7.dp.toPx(), center.y - radius - 8.dp.toPx())
        lineTo(center.x + 7.dp.toPx(), center.y - radius - 8.dp.toPx())
        close()
    }
    drawPath(
        path = topArrowPath,
        color = if (isAligned) Color(0xFF10B981) else Color(0xFF38BDF8)
    )
}

// Helper to draw cardinal badges
private fun DrawScope.drawCardinalBadge(
    center: Offset,
    radius: Float,
    angle: Float,
    label: String,
    color: Color,
    textMeasurer: TextMeasurer
) {
    val angleRad = Math.toRadians(angle.toDouble())
    val badgeRadius = radius - 44.dp.toPx()
    val pos = Offset(
        center.x + badgeRadius * sin(angleRad).toFloat(),
        center.y - badgeRadius * cos(angleRad).toFloat()
    )

    val textLayout = textMeasurer.measure(
        text = label,
        style = TextStyle(
            color = color,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    )
    drawText(
        textLayoutResult = textLayout,
        topLeft = Offset(pos.x - textLayout.size.width / 2f, pos.y - textLayout.size.height / 2f)
    )
}

// Draw Kaaba Icon on Canvas
private fun DrawScope.drawKaabaIcon(position: Offset, isAligned: Boolean, pulseGlow: Float) {
    val cubeSize = 22.dp.toPx()
    val halfSize = cubeSize / 2f

    // Outer radiant beacon
    drawCircle(
        color = (if (isAligned) Color(0xFF10B981) else Color(0xFFF59E0B)).copy(alpha = 0.35f * pulseGlow),
        radius = cubeSize * 0.9f,
        center = position
    )

    // Kaaba Black Cube Body
    drawRect(
        color = Color(0xFF18181B),
        topLeft = Offset(position.x - halfSize, position.y - halfSize),
        size = Size(cubeSize, cubeSize)
    )

    // Golden Kiswah Band
    drawRect(
        color = IslamicGold,
        topLeft = Offset(position.x - halfSize, position.y - halfSize + (cubeSize * 0.25f)),
        size = Size(cubeSize, cubeSize * 0.15f)
    )

    // Golden Door (Bab al-Kaaba)
    drawRect(
        color = IslamicGold,
        topLeft = Offset(position.x + (cubeSize * 0.15f), position.y),
        size = Size(cubeSize * 0.22f, cubeSize * 0.4f)
    )

    // Golden border
    drawRect(
        color = IslamicGold.copy(alpha = 0.8f),
        topLeft = Offset(position.x - halfSize, position.y - halfSize),
        size = Size(cubeSize, cubeSize),
        style = Stroke(width = 1.dp.toPx())
    )
}

// Precision double-tapered compass needle
private fun DrawScope.drawCompassNeedle(center: Offset, length: Float, isAligned: Boolean) {
    val needleWidth = 7.dp.toPx()

    // North Needle (Vivid Ruby Red / Emerald if aligned)
    val northColor = if (isAligned) Color(0xFF10B981) else Color(0xFFEF4444)
    val northPath = Path().apply {
        moveTo(center.x, center.y - length)
        lineTo(center.x + needleWidth, center.y)
        lineTo(center.x, center.y - 4.dp.toPx())
        lineTo(center.x - needleWidth, center.y)
        close()
    }
    drawPath(path = northPath, color = northColor)

    // South Needle (Silver / Slate)
    val southPath = Path().apply {
        moveTo(center.x, center.y + length)
        lineTo(center.x + needleWidth, center.y)
        lineTo(center.x, center.y + 4.dp.toPx())
        lineTo(center.x - needleWidth, center.y)
        close()
    }
    drawPath(path = southPath, color = Color(0xFF64748B))

    // Center Jeweled Brass Pivot
    drawCircle(
        color = IslamicGold,
        radius = 8.dp.toPx(),
        center = center
    )
    drawCircle(
        color = Color(0xFF1E293B),
        radius = 4.dp.toPx(),
        center = center
    )
}

// ------------------------------------------------------------------------------------------------
// CENTER BUBBLE LEVEL & ALIGNMENT OVERLAY
// ------------------------------------------------------------------------------------------------
@Composable
private fun CenterLevelAndKaabaOverlay(
    isAligned: Boolean,
    isDeviceLevel: Boolean,
    pitch: Float,
    roll: Float,
    pulseGlow: Float
) {
    // Bubble position mapping based on pitch & roll
    val maxTilt = 24f
    val bubbleOffsetFactor = 18.dp
    val bubbleX = (roll.coerceIn(-maxTilt, maxTilt) / maxTilt) * bubbleOffsetFactor.value
    val bubbleY = (pitch.coerceIn(-maxTilt, maxTilt) / maxTilt) * bubbleOffsetFactor.value

    Box(
        modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(Color(0xFF0F172A).copy(alpha = 0.85f))
            .border(
                1.5.dp,
                if (isAligned) Color(0xFF10B981) else IslamicGold.copy(alpha = 0.5f),
                CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        // Crosshairs
        Canvas(modifier = Modifier.fillMaxSize()) {
            val c = Offset(size.width / 2f, size.height / 2f)
            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(c.x - 18.dp.toPx(), c.y),
                end = Offset(c.x + 18.dp.toPx(), c.y),
                strokeWidth = 1.dp.toPx()
            )
            drawLine(
                color = Color.White.copy(alpha = 0.15f),
                start = Offset(c.x, c.y - 18.dp.toPx()),
                end = Offset(c.x, c.y + 18.dp.toPx()),
                strokeWidth = 1.dp.toPx()
            )
            drawCircle(
                color = (if (isDeviceLevel) Color(0xFF10B981) else Color(0xFFF59E0B)).copy(alpha = 0.3f),
                radius = 12.dp.toPx(),
                center = c,
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Floating Spirit Bubble (Green when flat, Amber when tilted)
        Box(
            modifier = Modifier
                .offset(x = bubbleX.dp, y = bubbleY.dp)
                .size(16.dp)
                .clip(CircleShape)
                .background(
                    if (isDeviceLevel) Color(0xFF10B981).copy(alpha = 0.85f)
                    else Color(0xFFF59E0B).copy(alpha = 0.85f)
                )
                .border(1.dp, Color.White.copy(alpha = 0.8f), CircleShape)
        )

        // Center Kaaba Emblem if Aligned
        if (isAligned) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// ------------------------------------------------------------------------------------------------
// CONTROLS ROW (Vibration, Sound, Dial Mode, Calibration)
// ------------------------------------------------------------------------------------------------
@Composable
private fun CompassControlsRow(
    isVibrationEnabled: Boolean,
    onToggleVibration: () -> Unit,
    isSoundEnabled: Boolean,
    onToggleSound: () -> Unit,
    isRotatingDialMode: Boolean,
    onToggleDialMode: () -> Unit,
    onOpenCalibration: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31).copy(alpha = 0.9f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Vibration Toggle
            ControlIconPill(
                icon = Icons.Default.Vibration,
                label = "কম্পন",
                isActive = isVibrationEnabled,
                onClick = onToggleVibration
            )

            // 2. Dial Mode Toggle (Rotating Dial vs Rotating Needle)
            ControlIconPill(
                icon = Icons.Default.Explore,
                label = if (isRotatingDialMode) "ডায়াল মোড" else "সুই মোড",
                isActive = isRotatingDialMode,
                onClick = onToggleDialMode
            )

            // 3. Sound Click Toggle
            ControlIconPill(
                icon = Icons.Default.VolumeUp,
                label = "সাউন্ড",
                isActive = isSoundEnabled,
                onClick = onToggleSound
            )

            // 4. Calibration Helper
            ControlIconPill(
                icon = Icons.Default.ScreenRotation,
                label = "ক্যালিব্রেট",
                isActive = true,
                onClick = onOpenCalibration
            )
        }
    }
}

@Composable
private fun ControlIconPill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(horizontal = 8.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(CircleShape)
                .background(if (isActive) IslamicGold.copy(alpha = 0.2f) else Color(0xFF1E293B))
                .border(
                    1.dp,
                    if (isActive) IslamicGold.copy(alpha = 0.6f) else Color(0xFF334155),
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = label,
                tint = if (isActive) IslamicGold else Color(0xFF94A3B8),
                modifier = Modifier.size(19.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            label,
            color = if (isActive) Color(0xFFE2E8F0) else Color(0xFF64748B),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ------------------------------------------------------------------------------------------------
// RICH DETAILS SECTION (Kaaba details, Solar Qibla, Quranic ayat)
// ------------------------------------------------------------------------------------------------
@Composable
private fun QiblaDetailsSection(
    userLat: Double,
    userLng: Double,
    placeNameBn: String,
    qiblaBearing: Float,
    distanceKm: Double,
    declination: Float,
    onOpenSolarQibla: () -> Unit,
    onOpenRulings: () -> Unit,
    onSelectLocation: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // 1. Geodesic Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Explore,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "ক্বিবলার ভৌগোলিক তথ্য",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    TextButton(
                        onClick = onSelectLocation,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("স্থান পরিবর্তন ➔", color = Color(0xFF38BDF8), fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = Color(0xFF334155), thickness = 0.5.dp)
                Spacer(modifier = Modifier.height(10.dp))

                InfoRowItem("আপনার বর্তমান অবস্থান", placeNameBn)
                InfoRowItem("অক্ষাংশ ও দ্রাঘিমাংশ", "${String.format("%.4f", userLat)}° N, ${String.format("%.4f", userLng)}° E")
                InfoRowItem("পবিত্র কাবার কোণ (Bearing)", "${QiblaHelper.toBengaliDigits(String.format("%.2f", qiblaBearing))}° (${QiblaHelper.getDirectionBn(qiblaBearing)})")
                InfoRowItem("মক্কা মুকাররমার দূরত্ব", "${QiblaHelper.toBengaliDigits(distanceKm.toInt())} কিলোমিটার")
                InfoRowItem("চৌম্বক বিচ্যুতি (Declination)", "${QiblaHelper.toBengaliDigits(String.format("%.1f", declination))}°")
            }
        }

        // 2. Action Shortcuts (Solar Qibla & Quranic Rulings)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenSolarQibla() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.WbSunny,
                        contentDescription = null,
                        tint = Color(0xFFF59E0B),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            "সূর্য দিয়ে যাচাই",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "ছায়া ও সৌর ক্বিবলা",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenRulings() },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF131D31)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E293B))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            "ক্বিবলার বিধান",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "কুরআন ও সুন্নাহ",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRowItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, color = Color(0xFF94A3B8), fontSize = 12.sp)
        Text(value, color = Color(0xFFE2E8F0), fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

// ------------------------------------------------------------------------------------------------
// CALIBRATION GUIDE DIALOG
// ------------------------------------------------------------------------------------------------
@Composable
private fun CompassCalibrationDialog(
    accuracy: Int,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ScreenRotation,
                    contentDescription = null,
                    tint = IslamicGold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "সেন্সর ক্যালিব্রেশন নির্দেশিকা",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "স্মার্টফোনের ম্যাগনেটিক সেন্সর (কম্পাস) ধাতু বা চৌম্বকীয় বস্তুর প্রভাবে কখনো কখনো বিচ্যুত হতে পারে। নির্ভুল দিক পেতে নিচের সহজ নিয়মে ক্যালিব্রেট করুন:",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 19.sp
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "∞ (8-Shape Motion)",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "মোবাইলটি হাতে নিয়ে বাতাসে ইংরেজি '৮' (আট) আকৃতিতে ২-৩ বার মসৃণভাবে ঘুরান।",
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Text(
                    "টিপস:\n• ফোন কভারের ম্যাগনেটিক স্ট্র্যাপ বা মেটাল রিং থাকলে তা সরিয়ে নিন।\n• ল্যাপটপ, স্পিকার বা বড় ইলেকট্রনিক্স ডিভাইস থেকে কিছুটা দূরে অবস্থান করুন।\n• ফোনটি হাতের তালুতে সমতলে অনুভূমিকভাবে রেখে দিক পরীক্ষা করুন।",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বুঝেছি (সমাপ্ত)", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    )
}

// ------------------------------------------------------------------------------------------------
// SOLAR QIBLA GUIDE DIALOG
// ------------------------------------------------------------------------------------------------
@Composable
private fun SolarQiblaGuideDialog(
    qiblaBearing: Float,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.WbSunny,
                    contentDescription = null,
                    tint = Color(0xFFF59E0B)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "সূর্যের সাহায্যে ক্বিবলা নির্ণয়",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "১. বার্ষিক বিশ্বজনীন ক্বিবলা দিবস:\nপ্রতি বছর ২৮ মে (বাংলাদেশ সময় দুপুর ৩:১৮) এবং ১৬ জুলাই (বাংলাদেশ সময় দুপুর ৩:২৭) সূর্য ঠিক কা'বা শরীফের মাথার উপরে অবস্থান করে। সে সময় পৃথিবীর যে কোনো স্থান থেকে সূর্যের দিকে মুখ করলেই সরাসরি ক্বিবলামুখী হওয়া যায়।",
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                Text(
                    "২. দৈনিক আনুমানিক দিক (বাংলাদেশ):\nবাংলাদেশে পবিত্র কা'বা পশ্চিম-উত্তর-পশ্চিম (কোণ প্রায় ২৭৮°) দিকে অবস্থিত।\n• সূর্য ঠিক পশ্চিমে অস্ত যাওয়ার সময় সূর্যাস্তের চেয়ে সামান্য ডানপাশে (উত্তর দিকে) ক্বিবলা অবস্থিত।\n• ছায়ার সাহায্যে: সমতলে একটি লাঠি খাড়া রাখলে লাঠির ছায়া পূর্ব দিকে পড়ে, যার উল্টো দিক প্রায় পশ্চিম বা ক্বিবলার নিকটবর্তী দিক।",
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}

// ------------------------------------------------------------------------------------------------
// QIBLA RULINGS & AYATS DIALOG
// ------------------------------------------------------------------------------------------------
@Composable
private fun QiblaRulingsDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = Color(0xFF10B981)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "ক্বিবলার বিধান ও ফযিলত",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    "قَدْ نَرَىٰ تَقَلُّبَ وَجْهِكَ فِي السَّمَاءِ ۖ فَلَنُوَلِّيَنَّكَ قِبْلَةً تَرْضَاهَا ۚ فَوَلِّ وَجْهَكَ شَطْرَ الْمَسْجِدِ الْحَرَامِ ۚ وَحَيْثُ مَا كُنتُمْ فَوَلُّوا وُجُوهَكُمْ شَطْرَهُ",
                    fontSize = 14.sp,
                    color = IslamicGold,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 22.sp
                )
                Text(
                    "\"নিশ্চয়ই আমি আপনার মুখের বারবার আকাশের দিকে ফেরানো লক্ষ্য করছি। অতএব আমি অবশ্যই আপনাকে এমন ক্বিবলার দিকে ফিরিয়ে দেব যা আপনি পছন্দ করেন। সুতরাং আপনি মসজিদুল হারামের দিকে মুখ ফেরান এবং তোমরা যেখানেই থাকো না কেন, তার দিকেই মুখ ফেরাও...\" (সূরা আল-বাক্বারাহ: ১৪৪)",
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )

                HorizontalDivider(thickness = 0.5.dp)

                Text(
                    "• সালাতের জন্য ক্বিবলামুখী হওয়া সালাতের অন্যতম শর্ত (শর্তে সালাত)।\n• যদি ক্বিবলার দিক নিশ্চিত না জানা থাকে, তবে আপ্রাণ চেষ্টা (ইজতিহাদ) বা কম্পাস/সূর্য দিয়ে যে দিকে মন সায় দেয় সেদিকে সালাত আদায় করলে সালাত আদায় হয়ে যাবে ইনশাআল্লাহ।\n• চলমান বাহনে (যেমন বিমান, ট্রেন, বাস) নফল সালাতের ক্ষেত্রে বাহন যেদিকে মুখ করে থাকে সেদিকেই সালাত পড়ার অবকাশ রয়েছে।",
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("বন্ধ করুন", color = MaterialTheme.colorScheme.primary)
            }
        }
    )
}
