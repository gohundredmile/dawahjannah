package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
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
    fun `loads exact local ayah and tafsir for non-catalog surah 67 ayah 1`() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val clues = ExtractedQuranOcrClues(
            arabicLines = listOf("تبارك الذي بيده الملك وهو على كل شيء قدير")
        )
        val result = LocalQuranAyahMatcher.matchCluesInLocalDatabase(context, clues)
        assertTrue(result.isSuccess)
        val ayah = result.getOrThrow()
        assertEquals(67, ayah.surahNumber)
        assertEquals(1, ayah.ayahNumber)
        assertTrue(ayah.arabicText.contains("تَبَٰرَكَ"))
        assertTrue(ayah.tafsirBn.length > 60)
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
