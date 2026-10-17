package com.example.util

import com.example.data.model.QuranAyah
import com.example.data.model.QuranSurah

/**
 * Page model for authentic Mushaf pagination (15-Line Madani and 16-Line Noorani)
 */
data class QuranMushafPage(
    val pageIndex: Int, // 0-based index in this Surah
    val surahNumber: Int,
    val pageNumberInSurah: Int, // 1-based page in this Surah
    val totalPagesInSurah: Int,
    val globalMushafPageNumber: Int, // Realistic 1-604 page number
    val juzNumber: Int,
    val ayahs: List<QuranAyah>,
    val startAyahNumber: Int,
    val endAyahNumber: Int
)

object QuranPaginationHelper {

    /**
     * Converts western digits to Arabic digits (e.g. 1 -> ١, 2 -> ٢)
     */
    fun toArabicDigits(number: Int): String {
        val arabicNumerals = charArrayOf('٠', '١', '٢', '٣', '٤', '٥', '٦', '٧', '٨', '٩')
        return number.toString().map { char ->
            if (char.isDigit()) arabicNumerals[char - '0'] else char
        }.joinToString("")
    }

    /**
     * Estimate starting global page number in 604-page Madani Mushaf for each Surah
     */
    private val surahStartPages = intArrayOf(
        0, // placeholder 0 index
        1,   2,   50,  77,  106, 128, 151, 177, 187, 208, // 1-10
        221, 235, 249, 255, 262, 267, 282, 293, 305, 312, // 11-20
        322, 332, 342, 350, 359, 367, 377, 385, 396, 404, // 21-30
        411, 415, 418, 428, 434, 440, 446, 453, 458, 467, // 31-40
        477, 483, 489, 496, 499, 502, 507, 511, 515, 518, // 41-50
        520, 523, 526, 528, 531, 534, 537, 542, 545, 549, // 51-60
        551, 553, 554, 556, 558, 560, 562, 564, 566, 568, // 61-70
        570, 572, 574, 575, 577, 578, 580, 582, 583, 585, // 71-80
        586, 587, 587, 589, 590, 591, 591, 592, 593, 594, // 81-90
        595, 595, 596, 596, 597, 597, 598, 598, 599, 599, // 91-100
        600, 600, 601, 601, 601, 602, 602, 602, 603, 603, // 101-110
        603, 604, 604, 604                                // 111-114
    )

    /**
     * Map a Surah and Ayah to its respective Juz (Para) 1-30
     */
    fun getJuzNumber(surahNumber: Int, ayahNumber: Int): Int {
        return when {
            surahNumber == 1 -> 1
            surahNumber == 2 && ayahNumber <= 141 -> 1
            surahNumber == 2 && ayahNumber <= 252 -> 2
            surahNumber == 2 -> 3
            surahNumber == 3 && ayahNumber <= 92 -> 3
            surahNumber == 3 -> 4
            surahNumber == 4 && ayahNumber <= 23 -> 4
            surahNumber == 4 && ayahNumber <= 147 -> 5
            surahNumber == 4 -> 6
            surahNumber == 5 && ayahNumber <= 81 -> 6
            surahNumber == 5 -> 7
            surahNumber == 6 && ayahNumber <= 110 -> 7
            surahNumber == 6 -> 8
            surahNumber == 7 && ayahNumber <= 87 -> 8
            surahNumber == 7 -> 9
            surahNumber == 8 && ayahNumber <= 40 -> 9
            surahNumber == 8 -> 10
            surahNumber == 9 && ayahNumber <= 92 -> 10
            surahNumber == 9 -> 11
            surahNumber == 10 -> 11
            surahNumber == 11 && ayahNumber <= 5 -> 11
            surahNumber == 11 -> 12
            surahNumber == 12 && ayahNumber <= 52 -> 12
            surahNumber == 12 -> 13
            surahNumber in 13..14 -> 13
            surahNumber == 15 && ayahNumber <= 1 -> 13
            surahNumber == 15 -> 14
            surahNumber == 16 -> 14
            surahNumber == 17 && ayahNumber <= 1 -> 15
            surahNumber == 17 -> 15
            surahNumber == 18 && ayahNumber <= 74 -> 15
            surahNumber == 18 -> 16
            surahNumber in 19..20 -> 16
            surahNumber in 21..22 -> 17
            surahNumber in 23..24 -> 18
            surahNumber == 25 && ayahNumber <= 20 -> 18
            surahNumber == 25 -> 19
            surahNumber == 26 -> 19
            surahNumber == 27 && ayahNumber <= 55 -> 19
            surahNumber == 27 -> 20
            surahNumber == 28 -> 20
            surahNumber == 29 && ayahNumber <= 45 -> 20
            surahNumber == 29 -> 21
            surahNumber in 30..32 -> 21
            surahNumber == 33 && ayahNumber <= 30 -> 21
            surahNumber == 33 -> 22
            surahNumber in 34..35 -> 22
            surahNumber == 36 && ayahNumber <= 27 -> 22
            surahNumber == 36 -> 23
            surahNumber in 37..38 -> 23
            surahNumber == 39 && ayahNumber <= 31 -> 23
            surahNumber == 39 -> 24
            surahNumber in 40..41 && ayahNumber <= 46 -> 24
            surahNumber == 41 -> 25
            surahNumber in 42..45 -> 25
            surahNumber in 46..51 && ayahNumber <= 30 -> 26
            surahNumber in 51..57 -> 27
            surahNumber in 58..66 -> 28
            surahNumber in 67..77 -> 29
            surahNumber >= 78 -> 30
            else -> 1
        }
    }

    /**
     * Segments a Surah's Ayahs into natural Mushaf pages based on lines per page (15 or 16).
     */
    fun paginateSurah(
        surah: QuranSurah,
        ayahs: List<QuranAyah>,
        linesPerPage: Int = 15
    ): List<QuranMushafPage> {
        if (ayahs.isEmpty()) return emptyList()

        // Surah 1 (Al-Fatihah) is always exactly 1 page
        if (surah.number == 1) {
            val juz = 1
            val page = QuranMushafPage(
                pageIndex = 0,
                surahNumber = 1,
                pageNumberInSurah = 1,
                totalPagesInSurah = 1,
                globalMushafPageNumber = 1,
                juzNumber = juz,
                ayahs = ayahs,
                startAyahNumber = 1,
                endAyahNumber = ayahs.size
            )
            return listOf(page)
        }

        val baseGlobalPage = surahStartPages.getOrElse(surah.number) { 1 }
        val pages = mutableListOf<QuranMushafPage>()
        var currentPageAyahs = mutableListOf<QuranAyah>()
        var currentLines = 0
        var isFirstPage = true

        // Effective line capacity: first page of a Surah accommodates the Header Banner (~2.5 lines)
        // and Bismillah (~2 lines) unless it's Surah 9 (At-Tawbah) which has no Bismillah.
        fun getLineCapacity(first: Boolean): Int {
            return if (first) {
                if (surah.number == 9) linesPerPage - 3 else linesPerPage - 4
            } else {
                linesPerPage
            }
        }

        var capacity = getLineCapacity(isFirstPage)

        for (ayah in ayahs) {
            val words = ayah.arabicText.trim().split(Regex("\\s+")).filter { it.isNotBlank() }
            val wordCount = words.size
            // Average 8.5 words per line in Madani/Noorani Mushaf
            val estimatedLines = maxOf(1, kotlin.math.ceil(wordCount / 8.5).toInt())

            if (currentLines + estimatedLines > capacity && currentPageAyahs.isNotEmpty()) {
                // Finalize current page
                val pIdx = pages.size
                pages.add(
                    QuranMushafPage(
                        pageIndex = pIdx,
                        surahNumber = surah.number,
                        pageNumberInSurah = pIdx + 1,
                        totalPagesInSurah = 0, // updated afterwards
                        globalMushafPageNumber = baseGlobalPage + pIdx,
                        juzNumber = getJuzNumber(surah.number, currentPageAyahs.first().ayahNumber),
                        ayahs = currentPageAyahs.toList(),
                        startAyahNumber = currentPageAyahs.first().ayahNumber,
                        endAyahNumber = currentPageAyahs.last().ayahNumber
                    )
                )
                currentPageAyahs = mutableListOf()
                currentLines = 0
                isFirstPage = false
                capacity = getLineCapacity(false)
            }

            currentPageAyahs.add(ayah)
            currentLines += estimatedLines
        }

        // Add trailing page
        if (currentPageAyahs.isNotEmpty()) {
            val pIdx = pages.size
            pages.add(
                QuranMushafPage(
                    pageIndex = pIdx,
                    surahNumber = surah.number,
                    pageNumberInSurah = pIdx + 1,
                    totalPagesInSurah = 0,
                    globalMushafPageNumber = baseGlobalPage + pIdx,
                    juzNumber = getJuzNumber(surah.number, currentPageAyahs.first().ayahNumber),
                    ayahs = currentPageAyahs.toList(),
                    startAyahNumber = currentPageAyahs.first().ayahNumber,
                    endAyahNumber = currentPageAyahs.last().ayahNumber
                )
            )
        }

        // Fill totalPagesInSurah
        val total = pages.size
        return pages.map { it.copy(totalPagesInSurah = total) }
    }
}
