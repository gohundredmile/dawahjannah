package com.example.ui.screens.tools

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.FlashAuto
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.datasource.QuranAyahCatalog
import com.example.data.model.AyahExplanation
import com.example.data.model.WordMeaning
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalArabicFontFamily
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.AyahAudioPlayerHelper
import com.example.util.AyahScannerAiService
import com.example.util.LoopMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.concurrent.Executors

@Composable
fun ExplainAyahCameraScreen(
    onNavigateBack: () -> Unit,
    onOpenHolyQuran: ((Int?) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val aiService = remember { AyahScannerAiService(context) }
    val audioPlayer = remember { AyahAudioPlayerHelper(context) }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
        }
    }

    // Permission state
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    // Screen state: Camera Viewfinder vs Result Dashboard
    var recognizedAyah by remember { mutableStateOf<AyahExplanation?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var scanErrorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var isAutoScanEnabled by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var apiKeyInput by remember { mutableStateOf(aiService.getEffectiveApiKey()) }

    // Camera control states
    var camera by remember { mutableStateOf<Camera?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isFlashOn by remember { mutableStateOf(false) }
    var isFrontCamera by remember { mutableStateOf(false) }
    var currentScanJob by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }

    // Instant stop scanning function that cancels any in-flight coroutine, resets UI state and disables auto-scan
    val stopScanning: () -> Unit = {
        currentScanJob?.cancel()
        currentScanJob = null
        isAnalyzing = false
        isAutoScanEnabled = false
        scanErrorMessage = null
        Toast.makeText(context, "স্ক্যান বন্ধ করা হয়েছে", Toast.LENGTH_SHORT).show()
    }

    // Unified safe capture and AI analysis trigger
    val triggerCapture: () -> Unit = {
        if (!isAnalyzing) {
            isAnalyzing = true
            scanErrorMessage = null
            val capture = imageCapture
            if (hasCameraPermission && capture != null) {
                val executor = ContextCompat.getMainExecutor(context)
                try {
                    capture.takePicture(
                        executor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                currentScanJob?.cancel()
                                currentScanJob = coroutineScope.launch(Dispatchers.Default) {
                                    try {
                                        val bitmap = imageProxyToBitmap(image)
                                        image.close()
                                        if (bitmap != null) {
                                            processImage(
                                                bitmap = bitmap,
                                                aiService = aiService,
                                                onSuccess = { result ->
                                                    isAnalyzing = false
                                                    scanErrorMessage = null
                                                    recognizedAyah = result
                                                },
                                                onError = { err ->
                                                    isAnalyzing = false
                                                    scanErrorMessage = err
                                                }
                                            )
                                        } else {
                                            withContext(Dispatchers.Main) {
                                                isAnalyzing = false
                                                scanErrorMessage = "ক্যামেরা ছবি পড়তে ব্যর্থ হয়েছে। পুনরায় চেষ্টা করুন।"
                                            }
                                        }
                                    } catch (e: Exception) {
                                        try { image.close() } catch (_: Exception) {}
                                        withContext(Dispatchers.Main) {
                                            isAnalyzing = false
                                            scanErrorMessage = e.localizedMessage ?: "স্ক্যান করতে সমস্যা হয়েছে।"
                                        }
                                    }
                                }
                            }

                            override fun onError(exception: ImageCaptureException) {
                                coroutineScope.launch(Dispatchers.Main) {
                                    isAnalyzing = false
                                    scanErrorMessage = "ক্যামেরা ক্যাপচার ত্রুটি: ${exception.localizedMessage ?: "অজ্ঞাত ত্রুটি"}"
                                }
                            }
                        }
                    )
                } catch (e: Exception) {
                    coroutineScope.launch(Dispatchers.Main) {
                        isAnalyzing = false
                        scanErrorMessage = e.localizedMessage ?: "ক্যামেরা প্রস্তুত করা যায়নি।"
                    }
                }
            } else {
                coroutineScope.launch(Dispatchers.Main) {
                    delay(400)
                    isAnalyzing = false
                }
            }
        }
    }

    // Auto-Scan interval trigger when viewfinder is steady
    LaunchedEffect(isAutoScanEnabled, hasCameraPermission, imageCapture, recognizedAyah, isAnalyzing) {
        if (isAutoScanEnabled && hasCameraPermission && imageCapture != null && recognizedAyah == null && !isAnalyzing) {
            delay(4000)
            if (recognizedAyah == null && !isAnalyzing && isAutoScanEnabled) {
                triggerCapture()
            }
        }
    }

    // Gallery Picker
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentScanJob?.cancel()
            currentScanJob = coroutineScope.launch {
                isAnalyzing = true
                scanErrorMessage = null
                try {
                    val bitmap = loadBitmapFromUri(context, it)
                    if (bitmap != null) {
                        processImage(
                            bitmap = bitmap,
                            aiService = aiService,
                            onSuccess = { result ->
                                isAnalyzing = false
                                scanErrorMessage = null
                                recognizedAyah = result
                            },
                            onError = { err ->
                                isAnalyzing = false
                                scanErrorMessage = err
                            }
                        )
                    } else {
                        isAnalyzing = false
                        scanErrorMessage = "ছবিটি লোড করা যায়নি।"
                    }
                } catch (e: Exception) {
                    isAnalyzing = false
                    scanErrorMessage = e.localizedMessage ?: "গ্যালারি ছবি প্রসেস করতে ব্যর্থ হয়েছে।"
                }
            }
        }
    }

    if (recognizedAyah != null) {
        // Result Dashboard View
        AyahExplanationDetailView(
            ayah = recognizedAyah!!,
            audioPlayer = audioPlayer,
            onRescan = {
                audioPlayer.stop()
                recognizedAyah = null
            },
            onNavigateBack = {
                audioPlayer.stop()
                recognizedAyah = null
                onNavigateBack()
            },
            onOpenHolyQuran = onOpenHolyQuran
        )
    } else {
        // Live Camera / Scanner Viewfinder
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (hasCameraPermission) {
                // CameraX Preview View
                key(isFrontCamera, hasCameraPermission) {
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx)
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                try {
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                    val capture = ImageCapture.Builder()
                                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                                        .build()
                                    imageCapture = capture

                                    val cameraSelector = if (isFrontCamera) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }

                                    cameraProvider.unbindAll()
                                    camera = cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        capture
                                    )
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }, ContextCompat.getMainExecutor(ctx))
                            previewView
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                // Friendly Camera Permission Request Layout
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Surface(
                        shape = CircleShape,
                        color = IslamicGold.copy(alpha = 0.15f),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = IslamicGold,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "কুরআন আয়াত স্ক্যানার ক্যামেরা",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = banglaFont,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "পবিত্র কুরআনের যে কোনো আরবি পৃষ্ঠার দিকে ফোন ধরলে ক্যামেরা স্বয়ংক্রিয়ভাবে আয়াত সনাক্ত করবে। এ জন্য ক্যামেরার অনুমতি প্রয়োজন।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontFamily = banglaFont,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ক্যামেরার অনুমতি দিন", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }
            }

            // Scanning Overlay with Animated Laser Reticle and Instant Stop Button
            ScannerViewfinderOverlay(
                isAnalyzing = isAnalyzing,
                onStopScanning = stopScanning,
                modifier = Modifier.fillMaxSize()
            )

            // Top Action Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 20.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.55f),
                    modifier = Modifier.size(44.dp)
                ) {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = Color.White
                        )
                    }
                }

                // Title Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.65f),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Explain This Ayah",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Torch and Flip controls
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (hasCameraPermission) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.55f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(onClick = {
                                isFlashOn = !isFlashOn
                                camera?.cameraControl?.enableTorch(isFlashOn)
                            }) {
                                Icon(
                                    imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                    contentDescription = "টর্চ",
                                    tint = if (isFlashOn) IslamicGold else Color.White
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.55f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(onClick = {
                                isFrontCamera = !isFrontCamera
                            }) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraAndroid,
                                    contentDescription = "ক্যামেরা পরিবর্তন",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // Error / Guidance Floating Banner
            if (scanErrorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFF1E1E1E).copy(alpha = 0.92f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.7f)),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 84.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.2f),
                                modifier = Modifier.size(28.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = scanErrorMessage!!,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White,
                                fontFamily = banglaFont,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { scanErrorMessage = null },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "বন্ধ করুন",
                                    tint = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            OutlinedButton(
                                onClick = {
                                    scanErrorMessage = null
                                    triggerCapture()
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = IslamicGold
                                ),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.6f)),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                modifier = Modifier.height(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Replay,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "পুনরায় স্ক্যান করুন",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontFamily = banglaFont,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Bottom Controls Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f), Color.Black)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Auto-Scan Toggle Chip
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isAutoScanEnabled) IslamicGold.copy(alpha = 0.22f) else Color.White.copy(alpha = 0.12f),
                    border = BorderStroke(1.dp, if (isAutoScanEnabled) IslamicGold else Color.White.copy(alpha = 0.3f)),
                    modifier = Modifier
                        .clickable { isAutoScanEnabled = !isAutoScanEnabled }
                        .padding(bottom = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isAutoScanEnabled) Icons.Default.FlashAuto else Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = if (isAutoScanEnabled) IslamicGold else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAutoScanEnabled) "অটো স্ক্যান: চালু ⚡" else "ম্যানুয়াল মোড (বোতাম চাপুন)",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAutoScanEnabled) IslamicGold else Color.White,
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Live Real-Time Scanning Instructions
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.55f),
                    border = BorderStroke(0.8.dp, Color.White.copy(alpha = 0.25f)),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isAnalyzing) "এআই ভিশন আয়াত বিশ্লেষণ করছে..." else "কুরআন বা দো'আর পৃষ্ঠার উপর ক্যামেরা সোজা রাখুন",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.9f),
                            fontFamily = banglaFont
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row: Gallery / Shutter / Direct Search
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Gallery Upload
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    galleryLauncher.launch("image/*")
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = "গ্যালারি",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("গ্যালারি", style = MaterialTheme.typography.labelSmall, color = Color.White, fontFamily = banglaFont)
                    }

                    // Primary Shutter Scan / Instant Stop Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = if (isAnalyzing) Color(0xFFDC2626) else IslamicGold,
                            border = BorderStroke(4.dp, Color.White.copy(alpha = 0.85f)),
                            modifier = Modifier
                                .size(76.dp)
                                .clickable {
                                    if (isAnalyzing) {
                                        stopScanning()
                                    } else {
                                        triggerCapture()
                                    }
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (isAnalyzing) {
                                    Icon(
                                        imageVector = Icons.Default.Stop,
                                        contentDescription = "স্ক্যান থামান (Stop)",
                                        tint = Color.White,
                                        modifier = Modifier.size(38.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.CameraAlt,
                                        contentDescription = "স্ক্যান করুন",
                                        tint = Color.Black,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isAnalyzing) "থামান (Stop)" else "স্ক্যান",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAnalyzing) Color(0xFFEF4444) else Color.White,
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Direct Search by Ayah Ref (e.g. 2:255)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                            modifier = Modifier
                                .size(50.dp)
                                .clickable {
                                    isSearchExpanded = !isSearchExpanded
                                }
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "খুঁজুন",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("খুঁজুন", style = MaterialTheme.typography.labelSmall, color = Color.White, fontFamily = banglaFont)
                    }
                }

                // Direct Search Drawer
                AnimatedVisibility(visible = isSearchExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("যেমন: 2:255 অথবা সূরা ইখলাস...", color = Color.White.copy(alpha = 0.6f)) },
                            trailingIcon = {
                                IconButton(onClick = {
                                    if (searchQuery.isNotBlank()) {
                                        recognizedAyah = QuranAyahCatalog.findByQueryOrSnippet(searchQuery)
                                    }
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = null, tint = IslamicGold)
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = IslamicGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                                focusedContainerColor = Color.Black.copy(alpha = 0.8f),
                                unfocusedContainerColor = Color.Black.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // API Key & Settings Dialog
            if (showApiKeyDialog) {
                AlertDialog(
                    onDismissRequest = { showApiKeyDialog = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IslamicGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("এআই ভিশন ও এপিআই সেটিংস", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                        }
                    },
                    text = {
                        Column {
                            Text(
                                text = if (aiService.isAiOnline()) {
                                    "✅ গুগল জেমিনি ৩.৫ ভিশন এআই সক্রিয় রয়েছে। যে কোনো আরবি কুরআন পৃষ্ঠা স্ক্যান করলে রিয়েল-টাইম তাফসীর তৈরি হবে।"
                                } else {
                                    "📖 অফলাইন ক্যাটালগ মোড সক্রিয়। কোনো API Key ছাড়াই প্রধান প্রধান সূরা ও আয়াতের তাফসীর, অডিও ও শব্দার্থ সম্পূর্ণ অফলাইনে কাজ করে।"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = banglaFont
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedTextField(
                                value = apiKeyInput,
                                onValueChange = { apiKeyInput = it },
                                label = { Text("Gemini API Key (ঐচ্ছিক)", fontFamily = banglaFont) },
                                placeholder = { Text("AI Studio থেকে প্রাপ্ত কী পেস্ট করুন") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            aiService.saveUserApiKey(apiKeyInput.trim())
                            showApiKeyDialog = false
                            Toast.makeText(context, "API Key সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                        }) {
                            Text("সংরক্ষণ করুন", color = IslamicGold, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showApiKeyDialog = false }) {
                            Text("বাতিল", fontFamily = banglaFont)
                        }
                    }
                )
            }
        }
    }
}

/**
 * Animated Viewfinder Overlay with Golden Corner Brackets and Scanning Laser
 */
@Composable
private fun ScannerViewfinderOverlay(
    isAnalyzing: Boolean,
    onStopScanning: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val boxWidth = maxWidth * 0.82f
        val boxHeight = maxHeight * 0.42f

        // Center Viewfinder Target
        Box(
            modifier = Modifier
                .size(width = boxWidth, height = boxHeight)
                .border(1.dp, IslamicGold.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
        ) {
            // Corner Golden L-Brackets
            val cornerSize = 24.dp
            val strokeWidth = 3.dp

            // Top-Left
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(cornerSize)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }

            // Top-Right
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(cornerSize)
            ) {
                Box(modifier = Modifier.fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }

            // Bottom-Left
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(cornerSize)
            ) {
                Box(modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }

            // Bottom-Right
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(cornerSize)
            ) {
                Box(modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.align(Alignment.BottomEnd).fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }

            // Laser Beam Animation
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .offset(y = boxHeight * laserPosition)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                IslamicGold.copy(alpha = 0.8f),
                                Color.White,
                                IslamicGold.copy(alpha = 0.8f),
                                Color.Transparent
                            )
                        )
                    )
            )

            // Prominent Instant Stop Button centered inside the viewfinder when analyzing
            if (isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFDC2626), // High-visibility red
                    border = BorderStroke(1.5.dp, Color.White),
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .clickable { onStopScanning() }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Stop,
                            contentDescription = "Stop",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "স্ক্যান থামান (Stop)",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Prompt Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.65f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 12.dp)
            ) {
                Text(
                    text = if (isAnalyzing) "আয়াত বিশ্লেষণ ও শানে নুযূল অনুসন্ধান হচ্ছে..." else "কুরআন পৃষ্ঠার আয়াতের উপর ক্যামেরা স্থির রাখুন",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

/**
 * Result Detail View: The comprehensive Islamic Learning & Memorization Dashboard
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AyahExplanationDetailView(
    ayah: AyahExplanation,
    audioPlayer: AyahAudioPlayerHelper,
    onRescan: () -> Unit,
    onNavigateBack: () -> Unit,
    onOpenHolyQuran: ((Int?) -> Unit)? = null
) {
    val context = LocalContext.current
    val banglaFont = LocalBanglaFontFamily.current
    val arabicFont = LocalArabicFontFamily.current

    val isPlaying by audioPlayer.isPlaying.collectAsState()
    val currentPositionMs by audioPlayer.currentPositionMs.collectAsState()
    val durationMs by audioPlayer.durationMs.collectAsState()
    val loopMode by audioPlayer.loopMode.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("মূল ও অনুবাদ", "শব্দে শব্দে অর্থ", "তাফসীর ও প্রেক্ষাপট", "সম্পর্কিত সূত্র", "হিফয ও অডিও")

    // Memorization Mode states
    var isMemorizeModeActive by remember { mutableStateOf(false) }
    var isAyahMemorized by remember { mutableStateOf(false) }
    var fontSizeSp by remember { mutableFloatStateOf(24f) }

    // Masked words state (word index to revealed boolean)
    val revealedWords = remember { mutableStateMapOf<Int, Boolean>() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Sticky Header / App Bar
        item {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 3.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "ফিরে যান")
                        }
                        Column {
                            Text(
                                text = ayah.surahNameBangla,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                fontFamily = banglaFont
                            )
                            Text(
                                text = "${ayah.surahNameArabic} • ${ayah.revelationTypeBn} • আয়াত: ${ayah.ayahNumber}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Rescan Button
                        OutlinedButton(
                            onClick = onRescan,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("পুনরায় স্ক্যান", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                        }

                        // Share
                        IconButton(onClick = {
                            val shareText = """
                                ${ayah.surahNameBangla} (আয়াত ${ayah.ayahNumber})
                                
                                ${ayah.arabicText}
                                
                                উচ্চারণ: ${ayah.transliterationBn}
                                
                                বাংলা অর্থ: ${ayah.banglaTranslation}
                                
                                তাফসীর সংক্ষেপ: ${ayah.tafsirBn}
                                
                                — দা'ওয়াহ টু জান্নাহ্ অ্যাপ
                            """.trimIndent()
                            val sendIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                type = "text/plain"
                            }
                            context.startActivity(Intent.createChooser(sendIntent, "আয়াত শেয়ার করুন"))
                        }) {
                            Icon(Icons.Default.Share, contentDescription = "শেয়ার")
                        }
                    }
                }
            }
        }

        // Verification & Authenticity Banner
        item {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "আল-কুরআনুল কারীম ও বিশুদ্ধ তাফসীর ভিত্তিক ব্যাখ্যা",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "পারা / সূরা নং ${ayah.surahNumber}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Quick Audio Player Strip (Always accessible)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ),
                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = IslamicGold,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clickable {
                                        if (isPlaying) {
                                            audioPlayer.pause()
                                        } else {
                                            audioPlayer.play(ayah.audioUrl)
                                        }
                                    }
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                        contentDescription = if (isPlaying) "পজ" else "প্লে",
                                        tint = Color.Black,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "তিলাওয়াত: ${ayah.reciterNameBn}",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = if (isPlaying) "লাইভ প্লে হচ্ছে..." else "শুনতে প্লে বাটনে চাপুন",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontFamily = banglaFont
                                )
                            }
                        }

                        // Loop selector button
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                modifier = Modifier.clickable {
                                    val nextMode = when (loopMode) {
                                        LoopMode.ONCE -> LoopMode.THREE_TIMES
                                        LoopMode.THREE_TIMES -> LoopMode.FIVE_TIMES
                                        LoopMode.FIVE_TIMES -> LoopMode.INFINITE
                                        LoopMode.INFINITE -> LoopMode.ONCE
                                    }
                                    audioPlayer.setLoopMode(nextMode)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Repeat,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = loopMode.titleBn,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontFamily = banglaFont
                                    )
                                }
                            }
                        }
                    }

                    if (durationMs > 0) {
                        Slider(
                            value = currentPositionMs.toFloat().coerceIn(0f, durationMs.toFloat()),
                            onValueChange = { audioPlayer.seekTo(it.toInt()) },
                            valueRange = 0f..durationMs.toFloat(),
                            colors = SliderDefaults.colors(
                                thumbColor = IslamicGold,
                                activeTrackColor = IslamicGold
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(28.dp)
                        )
                    }
                }
            }
        }

        // Primary Tab Bar
        item {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                fontFamily = banglaFont
                            )
                        }
                    )
                }
            }
        }

        // Tab Content Switching
        when (selectedTabIndex) {
            0 -> {
                // TAB 1: মূল ও অনুবাদ (Arabic, Pronunciation, Bangla, English)
                item {
                    TabCoreTextView(
                        ayah = ayah,
                        fontSizeSp = fontSizeSp,
                        onFontSizeChange = { fontSizeSp = it },
                        banglaFont = banglaFont,
                        arabicFont = arabicFont,
                        onOpenHolyQuran = onOpenHolyQuran
                    )
                }
            }
            1 -> {
                // TAB 2: শব্দে শব্দে অর্থ (Word-by-Word Meaning Cards)
                item {
                    TabWordByWordView(
                        words = ayah.wordByWord,
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
            }
            2 -> {
                // TAB 3: তাফসীর ও প্রেক্ষাপট (Tafsir & Shan-e-Nuzul)
                item {
                    TabTafsirAndContextView(
                        ayah = ayah,
                        banglaFont = banglaFont
                    )
                }
            }
            3 -> {
                // TAB 4: সম্পর্কিত সূত্র (Related Verses & Hadiths)
                item {
                    TabCrossReferencesView(
                        ayah = ayah,
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
            }
            4 -> {
                // TAB 5: হিফয ও মুখস্থ করার অনুশীলন (Memorization Mode)
                item {
                    TabMemorizationView(
                        ayah = ayah,
                        isMemorizeModeActive = isMemorizeModeActive,
                        onToggleMemorizeMode = { isMemorizeModeActive = !isMemorizeModeActive },
                        isAyahMemorized = isAyahMemorized,
                        onToggleMemorized = { isAyahMemorized = !isAyahMemorized },
                        revealedWords = revealedWords,
                        audioPlayer = audioPlayer,
                        isPlaying = isPlaying,
                        loopMode = loopMode,
                        banglaFont = banglaFont,
                        arabicFont = arabicFont
                    )
                }
            }
        }
    }
}

/**
 * Tab 1: মূল ও অনুবাদ
 */
@Composable
private fun TabCoreTextView(
    ayah: AyahExplanation,
    fontSizeSp: Float,
    onFontSizeChange: (Float) -> Unit,
    banglaFont: androidx.compose.ui.text.font.FontFamily?,
    arabicFont: androidx.compose.ui.text.font.FontFamily?,
    onOpenHolyQuran: ((Int?) -> Unit)? = null
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Arabic Master Container
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
            ),
            border = BorderStroke(1.2.dp, IslamicGold.copy(alpha = 0.45f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Top control bar inside card
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = IslamicGold.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "পবিত্র কুরআনুল কারীম",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = IslamicGold,
                            fontFamily = banglaFont,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }

                    // Copy Arabic
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (onOpenHolyQuran != null) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = IslamicGold.copy(alpha = 0.15f),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
                                modifier = Modifier.clickable {
                                    onOpenHolyQuran(ayah.surahNumber)
                                }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "কুরআনে পড়ুন",
                                        tint = IslamicGold,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "কুরআনে পড়ুন",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = IslamicGold,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }

                        IconButton(
                            onClick = {
                                val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                cm.setPrimaryClip(ClipData.newPlainText("Arabic Ayah", ayah.arabicText))
                                Toast.makeText(context, "আরবি টেক্সট কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "কপি", modifier = Modifier.size(18.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Arabic Calligraphic Text
                Text(
                    text = ayah.arabicText,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = arabicFont,
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * 1.85f).sp,
                        textDirection = TextDirection.ContentOrRtl
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 12.dp)
                )

                // Font Size Slider
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                ) {
                    Icon(Icons.Default.FormatSize, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Slider(
                        value = fontSizeSp,
                        onValueChange = onFontSizeChange,
                        valueRange = 18f..36f,
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            thumbColor = IslamicGold,
                            activeTrackColor = IslamicGold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "${fontSizeSp.toInt()} sp",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        // Bengali Pronunciation Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "সহজ বাংলা উচ্চারণ:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.transliterationBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // Bengali Translation Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
            ),
            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "বাংলা অনুবাদ (মুহিউদ্দীন খান ও ইসলামিক ফাউন্ডেশন):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.banglaTranslation,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 26.sp),
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // English Translation Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "English Translation (Sahih International):",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = ayah.englishTranslation,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

/**
 * Tab 2: শব্দে শব্দে অর্থ (Word-by-Word Meaning)
 */
@Composable
private fun TabWordByWordView(
    words: List<WordMeaning>,
    banglaFont: androidx.compose.ui.text.font.FontFamily?,
    arabicFont: androidx.compose.ui.text.font.FontFamily?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = "আয়াতের প্রতিটি শব্দের অর্থ ও ব্যাকরণিক বিশ্লেষণ (${words.size}টি শব্দ):",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = banglaFont
        )

        words.forEachIndexed { index, word ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Left: Word Index & Bengali + English Meaning
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = IslamicGold.copy(alpha = 0.15f),
                            modifier = Modifier.size(28.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${index + 1}",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = IslamicGold,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = word.bengaliMeaning,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont
                            )
                            if (word.englishMeaning.isNotBlank()) {
                                Text(
                                    text = word.englishMeaning,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (word.grammarNote.isNotBlank()) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f),
                                    modifier = Modifier.padding(top = 3.dp)
                                ) {
                                    Text(
                                        text = word.grammarNote,
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        fontFamily = banglaFont,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Right: Arabic Calligraphic Word
                    Text(
                        text = word.arabicWord,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontFamily = arabicFont,
                            fontSize = 24.sp,
                            textDirection = TextDirection.ContentOrRtl
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        textAlign = TextAlign.End,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
            }
        }
    }
}

/**
 * Tab 3: তাফসীর ও প্রেক্ষাপট (Tafsir & Shan-e-Nuzul)
 */
@Composable
private fun TabTafsirAndContextView(
    ayah: AyahExplanation,
    banglaFont: androidx.compose.ui.text.font.FontFamily?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Context / Shan-e-Nuzul Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "নাযিলের প্রেক্ষাপট ও শানে নুযূল:",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = ayah.contextBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }

        // Deep Scholarly Tafsir Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null,
                        tint = IslamicGold,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "প্রামাণ্য তাফসীর (ইবনে কাসীর ও মা'আরিফুল কুরআন সংক্ষেপ):",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = ayah.tafsirBn,
                    style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 25.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

/**
 * Tab 4: সম্পর্কিত সূত্র (Related Verses & Hadiths)
 */
@Composable
private fun TabCrossReferencesView(
    ayah: AyahExplanation,
    banglaFont: androidx.compose.ui.text.font.FontFamily?,
    arabicFont: androidx.compose.ui.text.font.FontFamily?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Related Verses Section
        Text(
            text = "কুরআনের সম্পর্কিত অন্যান্য আয়াত:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = banglaFont
        )

        ayah.relatedVerses.forEach { relVerse ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                        ) {
                            Text(
                                text = "${relVerse.surahNameBn} (${relVerse.ayahRef})",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = relVerse.arabicText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontFamily = arabicFont,
                            fontSize = 20.sp,
                            lineHeight = 32.sp,
                            textDirection = TextDirection.ContentOrRtl
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.End,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = relVerse.translationBn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = banglaFont
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Related Hadiths Section
        Text(
            text = "সম্পর্কিত সহীহ হাদীস ও ফযিলত:",
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            fontFamily = banglaFont
        )

        ayah.relatedHadiths.forEach { hadith ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.18f)
                ),
                border = BorderStroke(0.8.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${hadith.sourceBn} • বর্ণনাকারী: ${hadith.narratorBn}",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontFamily = banglaFont
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primary
                        ) {
                            Text(
                                text = hadith.gradeBn,
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = hadith.textBn,
                        style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 24.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        fontFamily = banglaFont
                    )
                }
            }
        }
    }
}

/**
 * Tab 5: হিফয ও অডিও (Memorization Trainer & Audio Player)
 */
@Composable
private fun TabMemorizationView(
    ayah: AyahExplanation,
    isMemorizeModeActive: Boolean,
    onToggleMemorizeMode: () -> Unit,
    isAyahMemorized: Boolean,
    onToggleMemorized: () -> Unit,
    revealedWords: MutableMap<Int, Boolean>,
    audioPlayer: AyahAudioPlayerHelper,
    isPlaying: Boolean,
    loopMode: LoopMode,
    banglaFont: androidx.compose.ui.text.font.FontFamily?,
    arabicFont: androidx.compose.ui.text.font.FontFamily?
) {
    val context = LocalContext.current
    val words = remember(ayah.arabicText) {
        ayah.arabicText.split(" ").filter { it.isNotBlank() }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Mode Switcher Card
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isMemorizeModeActive) IslamicGold.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(1.2.dp, if (isMemorizeModeActive) IslamicGold else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "হিফয ও মুখস্থ অনুশীলন মোড",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = if (isMemorizeModeActive) IslamicGold else MaterialTheme.colorScheme.onSurface,
                            fontFamily = banglaFont
                        )
                        Text(
                            text = "শব্দ ঢেকে পরীক্ষা করুন এবং বারবার শুনে সহজে মুখস্থ করুন",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontFamily = banglaFont
                        )
                    }

                    Button(
                        onClick = onToggleMemorizeMode,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isMemorizeModeActive) IslamicGold else MaterialTheme.colorScheme.primary
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = if (isMemorizeModeActive) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            tint = if (isMemorizeModeActive) Color.Black else Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isMemorizeModeActive) "ব্লার অন" else "চালু করুন",
                            color = if (isMemorizeModeActive) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont
                        )
                    }
                }
            }
        }

        // Interactive Word-Masking Canvas
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isMemorizeModeActive) "শব্দের উপর চাপ দিলে স্পষ্ট হবে:" else "আরবি মূল পাঠ:",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = banglaFont
                    )

                    if (isMemorizeModeActive) {
                        Text(
                            text = "সব উন্মোচন করুন",
                            style = MaterialTheme.typography.labelSmall,
                            color = IslamicGold,
                            fontWeight = FontWeight.Bold,
                            fontFamily = banglaFont,
                            modifier = Modifier.clickable {
                                words.indices.forEach { idx ->
                                    revealedWords[idx] = true
                                }
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Words Flow (RTL Layout)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState(), reverseScrolling = true),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    words.forEachIndexed { index, word ->
                        val isRevealed = revealedWords[index] == true || !isMemorizeModeActive

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isRevealed) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                            border = BorderStroke(
                                0.8.dp,
                                if (isRevealed) MaterialTheme.colorScheme.outline.copy(alpha = 0.3f) else IslamicGold
                            ),
                            modifier = Modifier.clickable {
                                if (isMemorizeModeActive) {
                                    revealedWords[index] = !(revealedWords[index] ?: false)
                                }
                            }
                        ) {
                            Text(
                                text = word,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontFamily = arabicFont,
                                    fontSize = 24.sp,
                                    textDirection = TextDirection.ContentOrRtl
                                ),
                                color = if (isRevealed) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                    .then(
                                        if (!isRevealed) Modifier.blur(6.dp) else Modifier
                                    )
                            )
                        }
                    }
                }
            }
        }

        // Memorization Repetition & Loop Audio Guide
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "হিফযের অডিও লুপ নির্দেশিকা:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "১. প্রথমে ৩ থেকে ৫ বার মনোযোগ দিয়ে অডিও শুনুন এবং সাথে মনে মনে মিলিয়ে নিন।\n২. এরপর 'হিফয মোড' অন করে শব্দগুলো ঢেকে নিন এবং এক এক শব্দ নিজে উচ্চারণ করে মেলাতে থাকুন।\n৩. অবিরাম লুপ দিয়ে সালাতের পূর্বে কয়েকবার চর্চা করুন।",
                    style = MaterialTheme.typography.bodySmall.copy(lineHeight = 20.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LoopMode.values().forEach { mode ->
                        FilterChip(
                            selected = loopMode == mode,
                            onClick = { audioPlayer.setLoopMode(mode) },
                            label = { Text(mode.titleBn, fontFamily = banglaFont) },
                            leadingIcon = if (loopMode == mode) {
                                { Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = IslamicGold,
                                selectedLabelColor = Color.Black
                            )
                        )
                    }
                }
            }
        }

        // Memorized Status Completion Button
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = if (isAyahMemorized) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.2.dp, if (isAyahMemorized) MaterialTheme.colorScheme.primary else IslamicGold),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    onToggleMemorized()
                    if (!isAyahMemorized) {
                        Toast.makeText(context, "মাশাআল্লাহ! আয়াতটি আপনার মুখস্থ তালিকায় সংরক্ষিত হয়েছে।", Toast.LENGTH_LONG).show()
                    }
                }
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (isAyahMemorized) Icons.Default.CheckCircle else Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isAyahMemorized) Color.White else IslamicGold,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isAyahMemorized) "আলহামদুলিল্লাহ! আয়াতটি মুখস্থ সম্পন্ন হয়েছে" else "মুখস্থ সম্পন্ন হিসেবে চিহ্নিত করুন",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isAyahMemorized) Color.White else MaterialTheme.colorScheme.onSurface,
                    fontFamily = banglaFont
                )
            }
        }
    }
}

// Helpers
private suspend fun processImage(
    bitmap: Bitmap,
    aiService: AyahScannerAiService,
    onSuccess: (AyahExplanation) -> Unit,
    onError: (String) -> Unit
) = withContext(Dispatchers.IO) {
    val result = aiService.analyzeQuranImage(bitmap)
    withContext(Dispatchers.Main) {
        result.fold(
            onSuccess = { ayah -> onSuccess(ayah) },
            onFailure = { error -> onError(error.localizedMessage ?: "কোনো স্পষ্ট আয়াত শনাক্ত করা যায়নি।") }
        )
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
    return try {
        val converted = image.toBitmap()
        val rotation = image.imageInfo.rotationDegrees
        if (rotation != 0 && converted != null) {
            val matrix = android.graphics.Matrix().apply { postRotate(rotation.toFloat()) }
            Bitmap.createBitmap(converted, 0, 0, converted.width, converted.height, matrix, true)
        } else {
            converted
        }
    } catch (_: Throwable) {
        try {
            val planeProxy = image.planes[0]
            val buffer = planeProxy.buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)
            val decoded = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val rotation = image.imageInfo.rotationDegrees
            if (rotation != 0 && decoded != null) {
                val matrix = android.graphics.Matrix().apply { postRotate(rotation.toFloat()) }
                Bitmap.createBitmap(decoded, 0, 0, decoded.width, decoded.height, matrix, true)
            } else {
                decoded
            }
        } catch (_: Throwable) {
            null
        }
    }
}

private suspend fun loadBitmapFromUri(context: Context, uri: Uri): Bitmap? = withContext(Dispatchers.IO) {
    try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (_: Exception) {
        null
    }
}
