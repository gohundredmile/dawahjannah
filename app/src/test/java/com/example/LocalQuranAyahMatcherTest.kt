package com.example

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.test.core.app.ApplicationProvider
import com.example.util.ArabicTesseractOcrEngine
import com.example.util.ExtractedQuranOcrClues
import com.example.util.LocalQuranAyahMatcher
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class LocalQuranAyahMatcherTest {

    @Test
    fun `uthmani and imlai skeleton normalization matches classic spelling differences`() {
        val uthmani = LocalQuranAyahMatcher.toQuranArabicSkeleton(
            LocalQuranAyahMatcher.normalizeQuranArabic("ٱلۡحَمۡدُ لِلَّهِ رَبِّ ٱلۡعَٰلَمِينَ")
        )
        val imlai = LocalQuranAyahMatcher.toQuranArabicSkeleton(
            LocalQuranAyahMatcher.normalizeQuranArabic("الحمد لله رب العالمين")
        )
        assertEquals(uthmani, imlai)

        val uthmaniSalah = LocalQuranAyahMatcher.toQuranArabicSkeleton(
            LocalQuranAyahMatcher.normalizeQuranArabic("وَيُقِيمُونَ ٱلصَّلَوٰةَ")
        )
        val imlaiSalah = LocalQuranAyahMatcher.toQuranArabicSkeleton(
            LocalQuranAyahMatcher.normalizeQuranArabic("ويقيمون الصلاة")
        )
        assertEquals(uthmaniSalah, imlaiSalah)
    }

    @Test
    fun `arabic text match overrides hallucinated AI surah number and loads exact local tafsir`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val clues = ExtractedQuranOcrClues(
            arabicLines = listOf("هو الله الذي لا إله إلا هو عالم الغيب والشهادة هو الرحمن الرحيم"),
            // Intentionally wrong AI guess to prove deterministic local DB matching wins
            suspectedSurahNumber = 2,
            suspectedAyahNumber = 255
        )
        val result = LocalQuranAyahMatcher.matchCluesInLocalDatabase(context, clues)
        assertTrue(result.isSuccess)
        val ayah = result.getOrThrow()
        assertEquals(59, ayah.surahNumber)
        assertEquals(22, ayah.ayahNumber)
        assertTrue(ayah.isVerifiedFromLocalQuranDb)
        assertTrue(ayah.matchConfidencePercent >= 90)
        assertTrue(ayah.tafsirBn.isNotBlank())
        assertTrue(ayah.banglaTranslation.isNotBlank())
    }

    @Test
    fun `dotless rasm matching recovers heavy laptop-screen OCR dot confusions`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()

        // Simulated noisy Tesseract output with wrong dots: الجمد -> الحمد, رن -> رب, العالنين -> العالمين
        val noisyFatiha = ExtractedQuranOcrClues(
            arabicLines = listOf("الجمد لله رن العالنين")
        )
        val fatihaRes = LocalQuranAyahMatcher.matchCluesInLocalDatabase(context, noisyFatiha)
        assertTrue("Expected noisy Fatiha OCR to match via Rasm", fatihaRes.isSuccess)
        assertEquals(1, fatihaRes.getOrThrow().surahNumber)
        assertEquals(2, fatihaRes.getOrThrow().ayahNumber)

        // Simulated noisy Tesseract output: فل -> قل, اجد -> احد
        val noisyIkhlas = ExtractedQuranOcrClues(
            arabicLines = listOf("فل هو الله اجد")
        )
        val ikhlasRes = LocalQuranAyahMatcher.matchCluesInLocalDatabase(context, noisyIkhlas)
        assertTrue("Expected noisy Ikhlas OCR to match via Rasm", ikhlasRes.isSuccess)
        assertEquals(112, ikhlasRes.getOrThrow().surahNumber)
        assertEquals(1, ikhlasRes.getOrThrow().ayahNumber)

        // Simulated noisy Surah Hashr 59:22: الدي -> الذي, العيب -> الغيب
        val noisyHashr = ExtractedQuranOcrClues(
            arabicLines = listOf("هو الله الدي لا اله الا هو عالم العيب والشهاده")
        )
        val hashrRes = LocalQuranAyahMatcher.matchCluesInLocalDatabase(context, noisyHashr)
        assertTrue("Expected noisy Hashr 59:22 OCR to match", hashrRes.isSuccess)
        assertEquals(59, hashrRes.getOrThrow().surahNumber)
        assertEquals(22, hashrRes.getOrThrow().ayahNumber)
    }

    @Test
    fun `matches single distinctive Arabic word and partial ayah phrases`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()

        val singleWord = LocalQuranAyahMatcher.matchCluesInLocalDatabase(
            context,
            ExtractedQuranOcrClues(arabicLines = listOf("الكوثر"))
        )
        assertTrue("Expected single Arabic word الكوثر to match 108:1", singleWord.isSuccess)
        assertEquals(108, singleWord.getOrThrow().surahNumber)
        assertEquals(1, singleWord.getOrThrow().ayahNumber)

        val partialPhrase = LocalQuranAyahMatcher.matchCluesInLocalDatabase(
            context,
            ExtractedQuranOcrClues(arabicLines = listOf("اياك نعبد واياك نستعين"))
        )
        assertTrue(partialPhrase.isSuccess)
        assertEquals(1, partialPhrase.getOrThrow().surahNumber)
        assertEquals(5, partialPhrase.getOrThrow().ayahNumber)
    }

    @Test
    fun `matches ayah from noisy multi-ayah laptop screen capture`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val clues = ExtractedQuranOcrClues(
            arabicLines = listOf(
                "تبارك الذي بيده الملك وهو على كل شيء قدير",
                "الذي خلق الموت والحياة ليبلوكم أيكم أحسن عملا"
            ),
            otherText = "Quran.com Surah Al-Mulk 67:1"
        )
        val result = LocalQuranAyahMatcher.matchCluesInLocalDatabase(context, clues)
        assertTrue(result.isSuccess)
        val ayah = result.getOrThrow()
        assertEquals(67, ayah.surahNumber)
        assertEquals(1, ayah.ayahNumber)
        assertTrue(ayah.tafsirBn.length > 60)
    }

    @Test
    fun `dark mode laptop screen bitmap is auto-inverted for Tesseract OCR`() {
        // Simulate dark-mode online Quran website (dark background #121212 with white text stripe)
        val bmp = Bitmap.createBitmap(120, 80, Bitmap.Config.ARGB_8888)
        bmp.eraseColor(Color.rgb(18, 20, 24))
        for (x in 20..100) {
            for (y in 35..45) {
                bmp.setPixel(x, y, Color.WHITE)
            }
        }
        val (processed, wasInverted) = ArabicTesseractOcrEngine.preprocessForArabicOcr(bmp, binarize = false)
        assertTrue("Dark mode screen must be auto-inverted to dark-text-on-white", wasInverted)
        // Corner background pixel should now be light (near 255)
        val cornerPx = processed.getPixel(2, 2)
        val cornerLuma = (Color.red(cornerPx) * 299 + Color.green(cornerPx) * 587 + Color.blue(cornerPx) * 114) / 1000
        assertTrue("Inverted background should be light (>= 200), got $cornerLuma", cornerLuma >= 200)
        processed.recycle()
        bmp.recycle()
    }

    @Test
    fun `supports bengali reference and instant local search`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val refMatch = LocalQuranAyahMatcher.searchLocalDatabaseByQuery(context, "৫৯:২৩")
        assertNotNull(refMatch)
        assertEquals(59, refMatch!!.surahNumber)
        assertEquals(23, refMatch.ayahNumber)
        assertTrue(refMatch.tafsirBn.isNotBlank())
    }
}
