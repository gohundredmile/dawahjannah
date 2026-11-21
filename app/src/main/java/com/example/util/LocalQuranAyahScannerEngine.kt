package com.example.util

import android.content.Context
import android.graphics.Bitmap
import com.example.data.datasource.QuranAyahCatalog
import com.example.data.model.AyahExplanation
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Local In-App Quran Ayah Scanner & Lookup Engine (114 Surahs / 6,236 Ayahs).
 *
 * Uses the full local bundled Quran database (`assets/quran/surah_1..114.json`) via
 * [LocalQuranAyahMatcher] so every identified Ayah returns 100% authentic Uthmani Arabic,
 * verified Bengali pronunciation, Dr. Abu Bakr Zakaria translation, and complete local Tafsir.
 */
object LocalQuranAyahScannerEngine {

    val quickPresets: List<AyahExplanation> by lazy {
        QuranAyahCatalog.catalog
    }

    /**
     * Checks whether the captured frame is pitch dark or blank before running OCR.
     * Returns an error message in Bengali if invalid, or null if image has usable contrast/light.
     */
    fun validateBitmapQuality(bitmap: Bitmap): String? {
        if (bitmap.width <= 0 || bitmap.height <= 0) {
            return "ক্যামেরা কোনো ছবি ধারণ করতে পারেনি। অনুগ্রহ করে পুনরায় ছবি তুলুন।"
        }

        val sampleStepX = maxOf(1, bitmap.width / 16)
        val sampleStepY = maxOf(1, bitmap.height / 16)
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

        if (avgLuma < 12 && contrast < 15) {
            return "ক্যামেরা পর্যাপ্ত আলো পাচ্ছে না বা লেন্স ঢাকা রয়েছে। উজ্জ্বল আলোতে কুরআনের পৃষ্ঠার দিকে ক্যামেরা ধরুন।"
        }

        if (contrast < 8 && (avgLuma > 240 || avgLuma < 30)) {
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
     * On-device ML Kit OCR + strict deterministic lookup against the local 6,236-ayah Quran DB.
     * Works best for printed references (e.g. "2:255", "Surah Al-Hashr 22") or English Quran pages.
     * Never guesses a random Surah or returns fabricated Islamic quotes.
     */
    suspend fun scanImage(
        bitmap: Bitmap,
        context: Context,
        preferredSurah: Int? = null,
        preferredAyah: Int? = null
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

            val ocrText = recognizeTextFromBitmap(bitmap).trim()
            if (ocrText.isBlank()) {
                return@withContext Result.failure(
                    Exception(
                        "অফলাইন ML Kit স্ক্যানার ছবি থেকে আরবি/বাংলা লেখা পড়তে পারেনি। " +
                            "আরবি মুসহাফ ক্যামেরা স্ক্যানের জন্য উপরের কী (🔑) আইকন থেকে Gemini Vision চালু করুন, " +
                            "অথবা নিচের 'খুঁজুন' / 'সূরা নির্বাচন' থেকে সরাসরি ৬,২৩৬ আয়াতের তাফসীর দেখুন।"
                    )
                )
            }

            val arabicLines = ocrText.lines().filter { line -> line.any { it in '\u0600'..'\u06FF' } }
            val explicitRef = LocalQuranAyahMatcher.extractExplicitReference(ocrText)
            val surahHint = LocalQuranAyahMatcher.resolveSurahNumberHint(ocrText, "")

            val clues = ExtractedQuranOcrClues(
                arabicLines = arabicLines,
                otherText = ocrText,
                surahHeaderHint = if (surahHint != null) ocrText else "",
                suspectedSurahNumber = explicitRef?.first,
                suspectedAyahNumber = explicitRef?.second,
                rawCombinedText = ocrText,
                extractionEngineLabelBn = "অফলাইন অন-ডিভাইস OCR → লোকাল কুরআন ডাটাবেস"
            )

            val matchResult = LocalQuranAyahMatcher.matchCluesInLocalDatabase(
                context = context,
                clues = clues,
                startTimeMs = startTime
            )

            if (matchResult.isSuccess) {
                return@withContext matchResult
            }

            Result.failure(
                Exception(
                    "অন-ডিভাইস অফলাইন স্ক্যানার আরবি মুসহাফের অক্ষর সনাক্ত করতে পারেনি। " +
                        "সঠিক আরবি আয়াত ক্যামেরা স্ক্যানের জন্য ইন্টারনেট ও Vision AI (🔑) সক্রিয় রাখুন, " +
                        "অথবা 'খুঁজুন' / 'সূরা নির্বাচন' থেকে সরাসরি লোকাল ডাটাবেসে খুঁজুন।"
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
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
