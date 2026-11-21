package com.example.ui.screens.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
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
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.model.AyahExplanation
import com.example.ui.theme.IslamicGold
import com.example.ui.theme.LocalBanglaFontFamily
import com.example.util.ArabicOcrStatus
import com.example.util.ArabicTesseractOcrEngine
import com.example.util.AyahAudioPlayerHelper
import com.example.util.AyahScannerAiService
import com.example.util.LocalQuranAyahMatcher
import com.example.util.LocalQuranAyahScannerEngine
import com.example.util.QuranFullTextSearchEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.concurrent.TimeUnit

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

    val aiService = remember { AyahScannerAiService(context) }
    val audioPlayer = remember { AyahAudioPlayerHelper(context) }
    val indexState by QuranFullTextSearchEngine.indexState.collectAsState()
    val ocrEngineState by ArabicTesseractOcrEngine.state.collectAsState()

    // Pre-warm BOTH the 114-Surah (6,236-Ayah) local Quran index AND the on-device Arabic OCR model
    LaunchedEffect(Unit) {
        launch(Dispatchers.IO) { ArabicTesseractOcrEngine.ensureModelReady(context) }
        LocalQuranAyahMatcher.ensureReady(context)
    }

    DisposableEffect(Unit) {
        onDispose {
            audioPlayer.release()
        }
    }

    // Camera permission state
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

    // Screen state
    var recognizedAyah by remember { mutableStateOf<AyahExplanation?>(null) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var scanStatusText by remember { mutableStateOf("কুরআনের আয়াত ফ্রেমের মাঝখানে রেখে স্ক্যান করুন") }
    var scanErrorMessage by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchExpanded by remember { mutableStateOf(false) }
    var showApiKeyDialog by remember { mutableStateOf(false) }
    var showSurahAyahPicker by remember { mutableStateOf(false) }
    var apiKeyInput by remember { mutableStateOf(aiService.getEffectiveApiKey()) }
    var hasActiveVisionKey by remember { mutableStateOf(aiService.hasValidApiKey()) }

    // Camera control states
    var camera by remember { mutableStateOf<Camera?>(null) }
    var previewViewRef by remember { mutableStateOf<PreviewView?>(null) }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var isFlashOn by remember { mutableStateOf(false) }
    var isFrontCamera by remember { mutableStateOf(false) }
    var zoomRatio by remember { mutableFloatStateOf(1f) }
    var currentScanJob by remember { mutableStateOf<Job?>(null) }

    val stopScanning: () -> Unit = {
        currentScanJob?.cancel()
        currentScanJob = null
        isAnalyzing = false
        scanErrorMessage = null
        Toast.makeText(context, "স্ক্যান বন্ধ করা হয়েছে", Toast.LENGTH_SHORT).show()
    }

    val openLocalSurahAyah: (Int, Int) -> Unit = { surah, ayah ->
        currentScanJob?.cancel()
        currentScanJob = coroutineScope.launch {
            isAnalyzing = true
            scanErrorMessage = null
            scanStatusText = "লোকাল কুরআন ডাটাবেস থেকে আয়াত ও তাফসীর লোড হচ্ছে..."
            val result = LocalQuranAyahMatcher.loadAyahExplanationFromLocalDb(
                context = context,
                surahNumber = surah,
                ayahNumber = ayah
            )
            isAnalyzing = false
            recognizedAyah = result
        }
    }

    val relookupTextInLocalDb: (String) -> Unit = { queryText ->
        currentScanJob?.cancel()
        currentScanJob = coroutineScope.launch {
            isAnalyzing = true
            scanErrorMessage = null
            val found = LocalQuranAyahScannerEngine.searchInstant(queryText, context)
            isAnalyzing = false
            if (found != null) {
                recognizedAyah = found
            } else {
                Toast.makeText(context, "\"$queryText\" দিয়ে লোকাল কুরআনে কোনো আয়াত পাওয়া যায়নি।", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Unified camera capture -> crop center focus area -> OCR -> Local 6,236-Ayah Quran DB lookup
    val triggerCapture: () -> Unit = {
        if (!isAnalyzing) {
            isAnalyzing = true
            scanErrorMessage = null
            scanStatusText = "ছবি ধারণ ও লোকাল কুরআন ডাটাবেসে খোঁজা হচ্ছে..."
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
                                        val fullBitmap = imageProxyToBitmap(image)
                                        image.close()
                                        if (fullBitmap != null) {
                                            // Focus-crop to generous viewfinder window AND keep full frame for Pass 3 fallback
                                            val focusedBitmap = cropToViewfinderRegion(fullBitmap)
                                            processImageLookup(
                                                bitmap = focusedBitmap,
                                                fullFrameBitmap = fullBitmap,
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
                isAnalyzing = false
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // Zero-permission Android Photo Picker (Google Play Policy compliant)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let {
            currentScanJob?.cancel()
            currentScanJob = coroutineScope.launch {
                isAnalyzing = true
                scanErrorMessage = null
                scanStatusText = "ছবি বিশ্লেষণ করে লোকাল কুরআন ডাটাবেসে খোঁজা হচ্ছে..."
                try {
                    val bitmap = loadBitmapFromUri(context, it)
                    if (bitmap != null) {
                        processImageLookup(
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
            onSelectSurahAyah = openLocalSurahAyah,
            onRelookupText = relookupTextInLocalDb,
            onOpenHolyQuran = onOpenHolyQuran
        )
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            if (hasCameraPermission) {
                key(isFrontCamera, hasCameraPermission) {
                    AndroidView(
                        factory = { ctx ->
                            val previewView = PreviewView(ctx).also { previewViewRef = it }
                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener({
                                try {
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().also {
                                        it.setSurfaceProvider(previewView.surfaceProvider)
                                    }
                                    // Maximize quality for crisp Arabic dots (nuqat) and harakat
                                    val capture = ImageCapture.Builder()
                                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                                        .build()
                                    imageCapture = capture

                                    val cameraSelector = if (isFrontCamera) {
                                        CameraSelector.DEFAULT_FRONT_CAMERA
                                    } else {
                                        CameraSelector.DEFAULT_BACK_CAMERA
                                    }

                                    cameraProvider.unbindAll()
                                    val boundCamera = cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        cameraSelector,
                                        preview,
                                        capture
                                    )
                                    camera = boundCamera
                                    boundCamera.cameraControl.setZoomRatio(zoomRatio)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }, ContextCompat.getMainExecutor(ctx))
                            previewView
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(camera, previewViewRef) {
                                detectTapGestures { offset ->
                                    val pv = previewViewRef ?: return@detectTapGestures
                                    val cam = camera ?: return@detectTapGestures
                                    try {
                                        val point = pv.meteringPointFactory.createPoint(offset.x, offset.y)
                                        val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF or FocusMeteringAction.FLAG_AE)
                                            .setAutoCancelDuration(3, TimeUnit.SECONDS)
                                            .build()
                                        cam.cameraControl.startFocusAndMetering(action)
                                    } catch (_: Exception) {
                                    }
                                }
                            }
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
                        color = IslamicGold.copy(alpha = 0.16f),
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
                        text = "Explain This Ayah ক্যামেরা",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        fontFamily = banglaFont,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "পবিত্র কুরআনের যেকোনো আয়াতের ছবি তুললে অ্যাপের লোকাল কুরআন ডাটাবেস (১১৪ সূরা • ৬,২৩৬ আয়াত) থেকে হুবহু আয়াত, উচ্চারণ, অনুবাদ ও পূর্ণাঙ্গ তাফসীর দেখানো হবে।",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f),
                        fontFamily = banglaFont,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) },
                        colors = ButtonDefaults.buttonColors(containerColor = IslamicGold),
                        modifier = Modifier.testTag("explain_grant_camera_btn")
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ক্যামেরার অনুমতি দিন", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                    }
                }
            }

            // Viewfinder Reticle Overlay
            ScannerViewfinderOverlay(
                isAnalyzing = isAnalyzing,
                statusText = if (isAnalyzing) scanStatusText else "কুরআনের আয়াত ফ্রেমের মাঝখানে রাখুন • ট্যাপ করে ফোকাস করুন",
                onStopScanning = stopScanning,
                modifier = Modifier.fillMaxSize()
            )

            // Top Action Bar (Edge-to-edge safe)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 12.dp)
                    .align(Alignment.TopCenter),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.6f),
                    modifier = Modifier.size(48.dp)
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("explain_camera_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "ফিরে যান",
                            tint = Color.White
                        )
                    }
                }

                // Title & Local DB Status Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.72f),
                    border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.55f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = IslamicGold,
                            modifier = Modifier.size(15.dp)
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

                // Right Controls: Surah Picker, Torch, Flip, Vision Key
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        IconButton(
                            onClick = { showSurahAyahPicker = true },
                            modifier = Modifier.testTag("explain_top_surah_picker_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "সূরা ও আয়াত নির্বাচন",
                                tint = IslamicGold
                            )
                        }
                    }

                    if (hasCameraPermission) {
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    isFlashOn = !isFlashOn
                                    camera?.cameraControl?.enableTorch(isFlashOn)
                                },
                                modifier = Modifier.testTag("explain_torch_btn")
                            ) {
                                Icon(
                                    imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                                    contentDescription = "টর্চ লাইট",
                                    tint = if (isFlashOn) IslamicGold else Color.White
                                )
                            }
                        }
                        Surface(
                            shape = CircleShape,
                            color = Color.Black.copy(alpha = 0.6f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            IconButton(
                                onClick = { isFrontCamera = !isFrontCamera },
                                modifier = Modifier.testTag("explain_flip_camera_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FlipCameraAndroid,
                                    contentDescription = "ক্যামেরা পরিবর্তন",
                                    tint = Color.White
                                )
                            }
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color.Black.copy(alpha = 0.6f),
                        modifier = Modifier.size(44.dp)
                    ) {
                        IconButton(
                            onClick = {
                                apiKeyInput = aiService.getEffectiveApiKey()
                                showApiKeyDialog = true
                            },
                            modifier = Modifier.testTag("explain_api_key_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.VpnKey,
                                contentDescription = "ভিশন OCR সেটিংস",
                                tint = if (hasActiveVisionKey) IslamicGold else Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Actionable Error / Recovery Banner
            if (scanErrorMessage != null) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFF1A1C1E).copy(alpha = 0.95f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.75f)),
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .statusBarsPadding()
                        .padding(top = 72.dp, start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = scanErrorMessage!!,
                                style = MaterialTheme.typography.bodySmall.copy(lineHeight = 19.sp),
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
                                    tint = Color.White.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    scanErrorMessage = null
                                    triggerCapture()
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = IslamicGold),
                                border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.7f)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("পুনরায় স্ক্যান", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    scanErrorMessage = null
                                    isSearchExpanded = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("লিখে খুঁজুন", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                            }

                            OutlinedButton(
                                onClick = {
                                    scanErrorMessage = null
                                    showSurahAyahPicker = true
                                },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                                modifier = Modifier.height(34.dp)
                            ) {
                                Text("সূরা ১-১১৪", style = MaterialTheme.typography.labelSmall, fontFamily = banglaFont)
                            }
                        }
                    }
                }
            }

            // Bottom Control Panel (Edge-to-edge safe)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.86f), Color.Black)
                        )
                    )
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Status + Zoom Controls Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFF047857).copy(alpha = 0.28f),
                        border = BorderStroke(0.9.dp, Color(0xFF34D399).copy(alpha = 0.6f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF34D399),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = when {
                                    !indexState.isReady ->
                                        "লোকাল ডাটাবেস লোড হচ্ছে (${indexState.indexedSurahs}/১১৪)..."
                                    ocrEngineState.status == ArabicOcrStatus.PREPARING ->
                                        ocrEngineState.statusLabelBn
                                    ocrEngineState.status == ArabicOcrStatus.READY ->
                                        "আরবি OCR + ১১৪ সূরা প্রস্তুত"
                                    else ->
                                        "লোকাল ১১৪ সূরা প্রস্তুত (৬,২৩৬ আয়াত)"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontFamily = banglaFont
                            )
                        }
                    }

                    if (hasCameraPermission) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = Color.White.copy(alpha = 0.14f),
                            border = BorderStroke(0.8.dp, IslamicGold.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .clickable {
                                    zoomRatio = when {
                                        zoomRatio < 1.4f -> 1.5f
                                        zoomRatio < 1.9f -> 2.0f
                                        else -> 1.0f
                                    }
                                    camera?.cameraControl?.setZoomRatio(zoomRatio)
                                }
                                .testTag("explain_zoom_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.ZoomIn, contentDescription = "জুম", tint = IslamicGold, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${zoomRatio}x",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IslamicGold,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Quick Surah Picker + Popular Ayah Shortcuts Carousel (all backed by local DB!)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(vertical = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = IslamicGold.copy(alpha = 0.24f),
                            border = BorderStroke(1.2.dp, IslamicGold),
                            modifier = Modifier
                                .clickable { showSurahAyahPicker = true }
                                .testTag("explain_quick_surah_picker_chip")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MenuBook,
                                    contentDescription = null,
                                    tint = IslamicGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "সূরা ও আয়াত (১-১১৪)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = IslamicGold,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = banglaFont
                                )
                            }
                        }
                    }

                    val quickLocalShortcuts = listOf(
                        Triple(2, 255, "আয়াতুল কুরসী (২:২৫৫)"),
                        Triple(59, 22, "হাশর শেষ ৩ (৫৯:২২)"),
                        Triple(1, 1, "আল-ফাতিহা (১:১)"),
                        Triple(2, 285, "বাকারা শেষ ২ (২:২৮৫)"),
                        Triple(112, 1, "আল-ইখলাস (১১২:১)"),
                        Triple(36, 1, "ইয়াসীন (৩৬:১)"),
                        Triple(67, 1, "আল-মুলক (৬৭:১)"),
                        Triple(94, 5, "আল-ইনশিরাহ (৯৪:৫)")
                    )
                    items(quickLocalShortcuts) { (s, a, label) ->
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = Color.Black.copy(alpha = 0.72f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable { openLocalSurahAyah(s, a) }
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Primary Action Row: Gallery (Photo Picker) | Shutter / Stop | Local Search
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Photo Picker Gallery Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.16f),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .size(52.dp)
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .testTag("explain_gallery_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.PhotoLibrary,
                                    contentDescription = "গ্যালারি থেকে ছবি বাছুন",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("গ্যালারি", style = MaterialTheme.typography.labelSmall, color = Color.White, fontFamily = banglaFont)
                    }

                    // Primary Shutter / Stop Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = if (isAnalyzing) Color(0xFFDC2626) else IslamicGold,
                            border = BorderStroke(4.dp, Color.White.copy(alpha = 0.88f)),
                            modifier = Modifier
                                .size(76.dp)
                                .clickable {
                                    if (isAnalyzing) stopScanning() else triggerCapture()
                                }
                                .testTag("explain_shutter_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isAnalyzing) Icons.Default.Stop else Icons.Default.CameraAlt,
                                    contentDescription = if (isAnalyzing) "স্ক্যান থামান" else "আয়াত স্ক্যান করুন",
                                    tint = if (isAnalyzing) Color.White else Color.Black,
                                    modifier = Modifier.size(36.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isAnalyzing) "থামান" else "স্ক্যান করুন",
                            style = MaterialTheme.typography.labelSmall,
                            color = if (isAnalyzing) Color(0xFFEF4444) else Color.White,
                            fontFamily = banglaFont,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Direct Local Quran Search Button
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            shape = CircleShape,
                            color = if (isSearchExpanded) IslamicGold.copy(alpha = 0.3f) else Color.White.copy(alpha = 0.16f),
                            border = BorderStroke(1.dp, if (isSearchExpanded) IslamicGold else Color.White.copy(alpha = 0.35f)),
                            modifier = Modifier
                                .size(52.dp)
                                .clickable { isSearchExpanded = !isSearchExpanded }
                                .testTag("explain_search_toggle_btn")
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "লোকাল কুরআনে খুঁজুন",
                                    tint = if (isSearchExpanded) IslamicGold else Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("খুঁজুন", style = MaterialTheme.typography.labelSmall, color = Color.White, fontFamily = banglaFont)
                    }
                }

                // Local 6,236-Ayah Instant Search Drawer
                AnimatedVisibility(visible = isSearchExpanded) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "যেমন: 59:22, ৫৯:২২, আরবি শব্দ বা বাংলা অর্থ...",
                                    color = Color.White.copy(alpha = 0.65f),
                                    fontFamily = banglaFont,
                                    fontSize = 13.sp
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (searchQuery.isNotBlank()) {
                                            coroutineScope.launch {
                                                isAnalyzing = true
                                                val found = LocalQuranAyahScannerEngine.searchInstant(searchQuery, context)
                                                isAnalyzing = false
                                                if (found != null) {
                                                    recognizedAyah = found
                                                    scanErrorMessage = null
                                                    isSearchExpanded = false
                                                } else {
                                                    scanErrorMessage = "\"$searchQuery\" দিয়ে লোকাল কুরআন ডাটাবেসে কোনো আয়াত পাওয়া যায়নি।"
                                                }
                                            }
                                        }
                                    },
                                    modifier = Modifier.testTag("explain_search_submit_btn")
                                ) {
                                    Icon(Icons.Default.Search, contentDescription = "অনুসন্ধান করুন", tint = IslamicGold)
                                }
                            },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = IslamicGold,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.45f),
                                focusedContainerColor = Color.Black.copy(alpha = 0.85f),
                                unfocusedContainerColor = Color.Black.copy(alpha = 0.65f)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("explain_search_input")
                        )
                    }
                }
            }

            // Vision OCR & Local DB Settings Dialog
            if (showApiKeyDialog) {
                AlertDialog(
                    onDismissRequest = { showApiKeyDialog = false },
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = IslamicGold)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("আয়াত স্ক্যানার ও ভিশন OCR সেটিংস", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
                        }
                    },
                    text = {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color(0xFF047857).copy(alpha = 0.14f),
                                border = BorderStroke(1.dp, Color(0xFF047857).copy(alpha = 0.4f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF047857), modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "কাজের পদ্ধতি: ক্যামেরা ছবি থেকে আরবি আয়াত পড়ে সরাসরি অ্যাপের লোকাল কুরআন ডাটাবেসে (১১৪ সূরা • ৬,২৩৬ আয়াত) মিলিয়ে ১০০% নির্ভুল আয়াত, উচ্চারণ, অনুবাদ ও পূর্ণাঙ্গ তাফসীর দেখানো হয়।",
                                        style = MaterialTheme.typography.bodySmall,
                                        fontFamily = banglaFont
                                    )
                                }
                            }

                            Text(
                                text = "আরবি মুসহাফ ক্যামেরা OCR-এর জন্য Gemini Vision API Key:",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                fontFamily = banglaFont
                            )
                            OutlinedTextField(
                                value = apiKeyInput,
                                onValueChange = { apiKeyInput = it },
                                label = { Text("Gemini API Key", fontFamily = banglaFont) },
                                placeholder = { Text("Google AI Studio কী এখানে দিন") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (aiService.hasValidApiKey()) {
                                TextButton(onClick = {
                                    aiService.saveUserApiKey("")
                                    apiKeyInput = ""
                                    hasActiveVisionKey = aiService.hasValidApiKey()
                                    showApiKeyDialog = false
                                    Toast.makeText(context, "কাস্টম API Key মুছে ফেলা হয়েছে", Toast.LENGTH_SHORT).show()
                                }) {
                                    Text("মুছুন", color = MaterialTheme.colorScheme.error, fontFamily = banglaFont)
                                }
                            }
                            Button(
                                onClick = {
                                    aiService.saveUserApiKey(apiKeyInput.trim())
                                    hasActiveVisionKey = aiService.hasValidApiKey()
                                    showApiKeyDialog = false
                                    Toast.makeText(context, "ভিশন OCR সেটিংস সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
                            ) {
                                Text("সংরক্ষণ", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showApiKeyDialog = false }) {
                            Text("বাতিল", fontFamily = banglaFont)
                        }
                    }
                )
            }

            // Full 114-Surah Local DB Picker Dialog
            if (showSurahAyahPicker) {
                SurahAyahPickerDialog(
                    onDismiss = { showSurahAyahPicker = false },
                    onSelect = { surah, ayah ->
                        showSurahAyahPicker = false
                        openLocalSurahAyah(surah, ayah)
                    }
                )
            }
        }
    }
}

@Composable
private fun SurahAyahPickerDialog(
    onDismiss: () -> Unit,
    onSelect: (surahNumber: Int, ayahNumber: Int) -> Unit
) {
    val banglaFont = LocalBanglaFontFamily.current
    var selectedSurahNumber by remember { mutableIntStateOf(2) }
    var ayahInput by remember { mutableStateOf("255") }
    var filterText by remember { mutableStateOf("") }

    val selectedSurah = remember(selectedSurahNumber) {
        QuranSurahCatalog.all114Surahs.find { it.number == selectedSurahNumber }
            ?: QuranSurahCatalog.all114Surahs.first()
    }

    val filteredSurahs = remember(filterText) {
        if (filterText.isBlank()) {
            QuranSurahCatalog.all114Surahs
        } else {
            val q = filterText.trim().lowercase()
            QuranSurahCatalog.all114Surahs.filter {
                it.number.toString().contains(q) ||
                    it.nameBn.lowercase().contains(q) ||
                    it.nameEn.lowercase().contains(q) ||
                    it.nameAr.contains(q)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.MenuBook, contentDescription = null, tint = IslamicGold)
                Spacer(modifier = Modifier.width(8.dp))
                Text("লোকাল কুরআন: সূরা ও আয়াত (১-১১৪)", fontFamily = banglaFont, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth().height(420.dp)) {
                Text(
                    text = "জনপ্রিয় আয়াত শর্টকাট:",
                    style = MaterialTheme.typography.labelSmall,
                    color = IslamicGold,
                    fontWeight = FontWeight.Bold,
                    fontFamily = banglaFont
                )
                Spacer(modifier = Modifier.height(4.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val quickPicks = listOf(
                        Triple(2, 255, "আয়াতুল কুরসী (২:২৫৫)"),
                        Triple(59, 22, "হাশর (৫৯:২২)"),
                        Triple(2, 285, "বাকারা (২:২৮৫)"),
                        Triple(1, 1, "ফাতিহা (১:১)"),
                        Triple(112, 1, "ইখলাস (১১২:১)"),
                        Triple(36, 1, "ইয়াসীন (৩৬:১)"),
                        Triple(67, 1, "মুলক (৬৭:১)")
                    )
                    items(quickPicks) { pick ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IslamicGold.copy(alpha = 0.16f),
                            border = BorderStroke(1.dp, IslamicGold.copy(alpha = 0.5f)),
                            modifier = Modifier.clickable { onSelect(pick.first, pick.second) }
                        ) {
                            Text(
                                text = pick.third,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontFamily = banglaFont,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = filterText,
                    onValueChange = { filterText = it },
                    placeholder = { Text("সূরা খুঁজুন (যেমন: হাশর, 59, বাকারা...)", fontFamily = banglaFont, fontSize = 12.sp) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(filteredSurahs, key = { it.number }) { surah ->
                        val isSelected = surah.number == selectedSurahNumber
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isSelected) IslamicGold.copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                            border = BorderStroke(1.dp, if (isSelected) IslamicGold else Color.Transparent),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedSurahNumber = surah.number
                                    ayahInput = "1"
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${surah.number}. সূরা ${surah.nameBn} (${surah.totalAyat} আয়াত)",
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = banglaFont
                                )
                                Text(
                                    text = surah.nameAr,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = IslamicGold
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "আয়াত নং (১ - ${selectedSurah.totalAyat}):",
                        style = MaterialTheme.typography.bodySmall,
                        fontFamily = banglaFont,
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = ayahInput,
                        onValueChange = { input -> ayahInput = input.filter { it.isDigit() } },
                        singleLine = true,
                        modifier = Modifier.width(96.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ayahNum = ayahInput.toIntOrNull()?.coerceIn(1, selectedSurah.totalAyat) ?: 1
                    onSelect(selectedSurahNumber, ayahNum)
                },
                colors = ButtonDefaults.buttonColors(containerColor = IslamicGold)
            ) {
                Text("আয়াত ও তাফসীর দেখুন", color = Color.Black, fontWeight = FontWeight.Bold, fontFamily = banglaFont)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("বাতিল", fontFamily = banglaFont)
            }
        }
    )
}

@Composable
private fun ScannerViewfinderOverlay(
    isAnalyzing: Boolean,
    statusText: String,
    onStopScanning: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanner")
    val laserPosition by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val boxWidth = maxWidth * 0.86f
        val boxHeight = maxHeight * 0.36f

        Box(
            modifier = Modifier
                .offset(y = (-28).dp)
                .size(width = boxWidth, height = boxHeight)
                .border(1.dp, IslamicGold.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
        ) {
            val cornerSize = 26.dp
            val strokeWidth = 3.dp

            // Top-Left Bracket
            Box(modifier = Modifier.align(Alignment.TopStart).size(cornerSize)) {
                Box(modifier = Modifier.fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }
            // Top-Right Bracket
            Box(modifier = Modifier.align(Alignment.TopEnd).size(cornerSize)) {
                Box(modifier = Modifier.fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.align(Alignment.TopEnd).fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }
            // Bottom-Left Bracket
            Box(modifier = Modifier.align(Alignment.BottomStart).size(cornerSize)) {
                Box(modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }
            // Bottom-Right Bracket
            Box(modifier = Modifier.align(Alignment.BottomEnd).size(cornerSize)) {
                Box(modifier = Modifier.align(Alignment.BottomEnd).fillMaxWidth().height(strokeWidth).background(IslamicGold))
                Box(modifier = Modifier.align(Alignment.BottomEnd).fillMaxHeight().width(strokeWidth).background(IslamicGold))
            }

            // Laser Scan Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .offset(y = boxHeight * laserPosition)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                Color.Transparent,
                                IslamicGold.copy(alpha = 0.85f),
                                Color.White,
                                IslamicGold.copy(alpha = 0.85f),
                                Color.Transparent
                            )
                        )
                    )
            )

            if (isAnalyzing) {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFDC2626),
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
                        Icon(Icons.Default.Stop, contentDescription = "থামান", tint = Color.White, modifier = Modifier.size(20.dp))
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

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color.Black.copy(alpha = 0.72f),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp, start = 8.dp, end = 8.dp)
            ) {
                Text(
                    text = statusText,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

private suspend fun processImageLookup(
    bitmap: Bitmap,
    aiService: AyahScannerAiService,
    onSuccess: (AyahExplanation) -> Unit,
    onError: (String) -> Unit,
    fullFrameBitmap: Bitmap? = null
) = withContext(Dispatchers.Default) {
    val result = aiService.analyzeQuranImage(
        bitmap = bitmap,
        fullFrameBitmap = fullFrameBitmap
    )
    withContext(Dispatchers.Main) {
        result.fold(
            onSuccess = { ayah -> onSuccess(ayah) },
            onFailure = { error -> onError(error.localizedMessage ?: "কোনো স্পষ্ট কুরআনের আয়াত শনাক্ত করা যায়নি।") }
        )
    }
}

/**
 * Crops the captured camera bitmap to a generous window around the golden viewfinder reticle
 * (94% width x 62% height centered slightly above vertical middle) so wide Arabic lines on a
 * laptop screen or printed Mushaf are never clipped horizontally.
 */
private fun cropToViewfinderRegion(source: Bitmap): Bitmap {
    return try {
        if (source.width < 320 || source.height < 320) return source
        val cropW = (source.width * 0.94f).toInt().coerceIn(200, source.width)
        val cropH = (source.height * 0.62f).toInt().coerceIn(200, source.height)
        val left = ((source.width - cropW) / 2).coerceAtLeast(0)
        val top = ((source.height - cropH) * 0.45f).toInt().coerceIn(0, source.height - cropH)
        Bitmap.createBitmap(source, left, top, cropW, cropH)
    } catch (_: Throwable) {
        source
    }
}

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun imageProxyToBitmap(image: ImageProxy): Bitmap? {
    return try {
        val converted = image.toBitmap()
        val rotation = image.imageInfo.rotationDegrees
        if (rotation != 0 && converted != null) {
            val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
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
                val matrix = Matrix().apply { postRotate(rotation.toFloat()) }
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
        inputStream?.use { BitmapFactory.decodeStream(it) }
    } catch (_: Exception) {
        null
    }
}
