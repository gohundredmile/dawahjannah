package com.example.util

import android.content.Context
import android.graphics.Bitmap
import com.example.data.datasource.QuranAyahCatalog
import com.example.data.model.AyahExplanation
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Local In-App Quran Ayah Scanner & Lookup Engine (114 Surahs / 6,236 Ayahs).
 *
 * Works 100% ON-DEVICE WITHOUT ANY API KEY by combining:
 * 1. [ArabicTesseractOcrEngine] (Tesseract 5 LSTM `ara.traineddata`) with automatic Dark-Mode
 *    screen inversion, LCD moiré suppression, contrast stretch, and Otsu binarization.
 * 2. ML Kit Latin OCR (for printed `Surah:Ayah` numbers like `2:255`, Surah headers, and English
 *    translations on online Quran pages like Quran.com).
 * 3. [LocalQuranAyahMatcher] across all 114 Surahs / 6,236 local Ayahs (`assets/quran/surah_1..114.json`),
 *    with Uthmani/IndoPak skeleton + dotless Arabic Rasm matching.
 */
object LocalQuranAyahScannerEngine {

    val quickPresets: List<AyahExplanation> by lazy {
        QuranAyahCatalog.catalog
    }

    /**
     * Checks whether the captured frame is pitch dark or completely blank before running OCR.
     * Note: Allows Dark-Mode laptop/phone screens (white text on dark background) as long as
     * there is visible text contrast!
     */
    fun validateBitmapQuality(bitmap: Bitmap): String? {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return "ক্যামেরা কোনো ছবি ধারণ করতে পারেনি। অনুগ্রহ করে পুনরায় ছবি তুলুন।"
        }

        val sampleStepX = maxOf(1, bitmap.width / 24)
        val sampleStepY = maxOf(1, bitmap.height / 24)
        var totalBrightness = 0L
        var minBrightness = 255
        var maxBrightness = 0
        var sampleCount = 0

        for (y in 0 until bitmap.height step sampleStepY) {
            for (x in 0 until bitmap.width step sampleStepX) {
                val pixel = bitmap.getPixel(x, y)
                val r = (pixel shr 16) and 0xFF
                val g = (pixel shr 8) and 0xFF
                val b = pixel and 0xFF
                val luma = (r * 299 + g * 587 + b * 114) / 1000
                totalBrightness += luma
                if (luma < minBrightness) minBrightness = luma
                if (luma > maxBrightness) maxBrightness = luma
                sampleCount++
            }
        }

        val avgLuma = if (sampleCount > 0) totalBrightness / sampleCount else 128
        val contrast = maxBrightness - minBrightness

        if (avgLuma < 8 && contrast < 12) {
            return "ক্যামেরা পর্যাপ্ত আলো পাচ্ছে না বা লেন্স ঢাকা রয়েছে। কুরআনের আয়াতের দিকে ক্যামেরা ধরুন।"
        }

        if (contrast < 6 && (avgLuma > 246 || avgLuma < 14)) {
            return "ছবিতে কোনো স্পষ্ট লেখা দেখা যাচ্ছে না। কুরআনের আয়াতের উপর ফোকাস করে আবার স্ক্যান করুন।"
        }

        return null
    }

    suspend fun recognizeTextFromBitmap(bitmap: Bitmap): String = suspendCancellableCoroutine { continuation ->
        try {
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    continuation.resume(visionText.text ?: "")
                }
                .addOnFailureListener {
                    continuation.resume("")
                }
        } catch (_: Throwable) {
            continuation.resume("")
        }
    }

    /**
     * Multi-pass On-Device Arabic + Latin OCR -> Deterministic Local 6,236-Ayah Quran DB Lookup.
     *
     * Pass 1: Grayscale contrast-stretched (+ auto dark-mode inverted) Arabic Tesseract OCR
     *         + ML Kit Latin OCR in parallel on the focused crop.
     * Pass 2: Otsu binarized Arabic Tesseract OCR on the focused crop (recovers laptop glare / shadows).
     * Pass 3: Full uncropped camera frame recovery (if the ayah extended outside the golden box).
     */
    suspend fun scanImage(
        bitmap: Bitmap,
        context: Context,
        preferredSurah: Int? = null,
        preferredAyah: Int? = null,
        fullFrameBitmap: Bitmap? = null
    ): Result<AyahExplanation> = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()

        try {
            val qualityErr = validateBitmapQuality(bitmap)
            if (qualityErr != null) {
                return@withContext Result.failure(Exception(qualityErr))
            }

            if (preferredSurah != null && preferredSurah in 1..114) {
                val ayahNum = (preferredAyah ?: 1).coerceAtLeast(1)
                val exact = LocalQuranAyahMatcher.loadAyahExplanationFromLocalDb(
                    context = context,
                    surahNumber = preferredSurah,
                    ayahNumber = ayahNum,
                    scanDurationMs = (System.currentTimeMillis() - startTime).coerceAtLeast(15L)
                )
                return@withContext Result.success(exact)
            }

            // Ensure 6,236-ayah local Quran index is ready
            LocalQuranAyahMatcher.ensureReady(context)

            var bestMatch: AyahExplanation? = null
            val detectedSnippets = LinkedHashSet<String>()

            // --- PASS 1: Focused Crop -> Parallel Arabic Tesseract (Grayscale + Auto Dark-Mode Invert) + ML Kit Latin ---
            val (pass1Arabic, pass1Latin) = coroutineScope {
                val araJob = async(Dispatchers.IO) { ArabicTesseractOcrEngine.recognizeArabicPass1(context, bitmap) }
                val latJob = async(Dispatchers.IO) { recognizeTextFromBitmap(bitmap).trim() }
                araJob.await() to latJob.await()
            }

            if (pass1Arabic.text.isNotBlank()) detectedSnippets.add(pass1Arabic.text)
            if (pass1Latin.isNotBlank()) detectedSnippets.add(pass1Latin)

            val pass1Match = matchExtractedTexts(
                context = context,
                arabicTexts = listOf(pass1Arabic.text),
                latinText = pass1Latin,
                engineLabelBn = if (pass1Arabic.text.any { it in '\u0600'..'\u06FF' }) {
                    "অন-ডিভাইস আরবি OCR → লোকাল কুরআন ডাটাবেস"
                } else {
                    "অন-ডিভাইস OCR → লোকাল কুরআন ডাটাবেস"
                },
                startTimeMs = startTime
            )
            if (pass1Match != null) {
                bestMatch = pass1Match
                if (pass1Match.matchConfidencePercent >= 80) {
                    return@withContext Result.success(pass1Match)
                }
            }

            // --- PASS 2: Otsu Binarized Crop (handles laptop screen glare, moiré, shadowed paper) ---
            val pass2Arabic = ArabicTesseractOcrEngine.recognizeArabicPass2Binarized(context, bitmap)
            if (pass2Arabic.text.isNotBlank()) {
                detectedSnippets.add(pass2Arabic.text)
                val pass2Match = matchExtractedTexts(
                    context = context,
                    arabicTexts = listOf(pass2Arabic.text, pass1Arabic.text),
                    latinText = pass1Latin,
                    engineLabelBn = "অন-ডিভাইস আরবি OCR (হাই-কন্ট্রাস্ট) → লোকাল কুরআন ডাটাবেস",
                    startTimeMs = startTime
                )
                if (pass2Match != null && (bestMatch == null || pass2Match.matchConfidencePercent > bestMatch.matchConfidencePercent)) {
                    bestMatch = pass2Match
                    if (bestMatch.matchConfidencePercent >= 80) {
                        return@withContext Result.success(bestMatch)
                    }
                }
            }

            // --- PASS 3: Full Uncropped Frame (if ayah lines extended outside the viewfinder box) ---
            if (fullFrameBitmap != null && fullFrameBitmap !== bitmap && !fullFrameBitmap.isRecycled) {
                val (fullArabic, fullLatin) = coroutineScope {
                    val araJob = async(Dispatchers.IO) { ArabicTesseractOcrEngine.recognizeArabicPass1(context, fullFrameBitmap) }
                    val latJob = async(Dispatchers.IO) { recognizeTextFromBitmap(fullFrameBitmap).trim() }
                    araJob.await() to latJob.await()
                }
                if (fullArabic.text.isNotBlank()) detectedSnippets.add(fullArabic.text)
                if (fullLatin.isNotBlank()) detectedSnippets.add(fullLatin)

                val fullMatch = matchExtractedTexts(
                    context = context,
                    arabicTexts = listOf(fullArabic.text, pass1Arabic.text, pass2Arabic.text),
                    latinText = listOf(fullLatin, pass1Latin).filter { it.isNotBlank() }.joinToString("\n"),
                    engineLabelBn = "ফুল-ফ্রেম আরবি OCR → লোকাল কুরআন ডাটাবেস",
                    startTimeMs = startTime
                )
                if (fullMatch != null && (bestMatch == null || fullMatch.matchConfidencePercent > bestMatch.matchConfidencePercent)) {
                    bestMatch = fullMatch
                }
            }

            if (bestMatch != null) {
                return@withContext Result.success(bestMatch)
            }

            val ocrReady = ArabicTesseractOcrEngine.state.value.status == ArabicOcrStatus.READY
            val samplePreview = detectedSnippets.firstOrNull()?.replace('\n', ' ')?.take(70)?.trim().orEmpty()
            val errorMsg = when {
                !ocrReady ->
                    "আরবি OCR মডেল (১.৪ MB) এখনো ডাউনলোড হয়নি। একবার ইন্টারনেট চালু করে পুনরায় স্ক্যান করুন — এরপর এটি সম্পূর্ণ অফলাইনে কাজ করবে। অথবা নিচের 'খুঁজুন' বা 'সূরা ১-১১৪' ব্যবহার করুন।"
                samplePreview.isNotBlank() ->
                    "ক্যামেরা থেকে পড়া লেখা (\"$samplePreview\") লোকাল কুরআনের কোনো আয়াতের সাথে নিশ্চিতভাবে মেলেনি। ফোনটি স্ক্রিন/পৃষ্ঠার আরেকটু কাছে নিয়ে ট্যাপ করে ফোকাস করুন এবং আবার স্ক্যান করুন।"
                else ->
                    "ছবিতে স্পষ্ট আরবি অক্ষর পড়া যায়নি। অনুগ্রহ করে আয়াতের লেখাটি বড় করে ফ্রেমের মাঝখানে রেখে স্ক্রিনে ট্যাপ করে ফোকাস করুন এবং আবার স্ক্যান করুন।"
            }

            Result.failure(Exception(errorMsg))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun matchExtractedTexts(
        context: Context,
        arabicTexts: List<String>,
        latinText: String,
        engineLabelBn: String,
        startTimeMs: Long
    ): AyahExplanation? {
        val arabicLines = ArrayList<String>()
        for (block in arabicTexts) {
            block.lines().forEach { line ->
                val trimmed = line.trim()
                if (trimmed.any { it in '\u0600'..'\u06FF' }) {
                    arabicLines.add(trimmed)
                }
            }
        }

        val combinedRaw = buildString {
            if (arabicLines.isNotEmpty()) append(arabicLines.joinToString("\n"))
            if (latinText.isNotBlank()) {
                if (isNotEmpty()) append("\n")
                append(latinText)
            }
        }.trim()

        if (combinedRaw.isBlank()) return null

        val explicitRef = LocalQuranAyahMatcher.extractExplicitReference(latinText)
            ?: LocalQuranAyahMatcher.extractExplicitReference(combinedRaw)
        val surahHint = LocalQuranAyahMatcher.resolveSurahNumberHint(latinText, combinedRaw)

        val clues = ExtractedQuranOcrClues(
            arabicLines = arabicLines,
            otherText = latinText,
            surahHeaderHint = if (surahHint != null) combinedRaw else "",
            suspectedSurahNumber = explicitRef?.first,
            suspectedAyahNumber = explicitRef?.second,
            rawCombinedText = combinedRaw,
            extractionEngineLabelBn = engineLabelBn
        )

        val matchResult = LocalQuranAyahMatcher.matchCluesInLocalDatabase(
            context = context,
            clues = clues,
            startTimeMs = startTimeMs
        )
        return matchResult.getOrNull()
    }

    /**
     * Loads exact verified Ayah + complete local Tafsir directly from `assets/quran/surah_1..114.json`
     * for any Surah (1–114) and any Ayah.
     */
    suspend fun getOrSynthesize(
        context: Context,
        surahNumber: Int,
        ayahNumber: Int
    ): AyahExplanation {
        return LocalQuranAyahMatcher.loadAyahExplanationFromLocalDb(
            context = context,
            surahNumber = surahNumber,
            ayahNumber = ayahNumber
        )
    }

    /**
     * Searches all 114 Surahs (6,236 Ayahs) locally by Surah:Ayah reference, Surah name,
     * Arabic script, Bengali translation, or Bengali pronunciation.
     */
    suspend fun searchInstant(query: String, context: Context): AyahExplanation? {
        return LocalQuranAyahMatcher.searchLocalDatabaseByQuery(context, query)
    }
}
