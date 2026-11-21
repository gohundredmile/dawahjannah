package com.example.util

import android.content.Context
import android.util.JsonReader
import com.example.data.datasource.QuranAyahCatalog
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.datasource.QuranTafsirAndTranslationProvider
import com.example.data.datasource.SurahShaneNuzulCatalog
import com.example.data.model.AyahExplanation
import com.example.data.model.AyahMatchSummary
import com.example.data.model.IslamicContentType
import com.example.data.model.QuranAyah
import com.example.data.model.QuranSurah
import com.example.data.model.QuranTafsirSource
import com.example.data.model.RelatedHadith
import com.example.data.model.RelatedVerse
import com.example.data.model.TargetSourceType
import com.example.data.model.WordMeaning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

/**
 * Structured OCR clues extracted from a camera capture or gallery photo before deterministic
 * lookup against the in-app local 114-Surah (6,236-Ayah) Quran database.
 */
data class ExtractedQuranOcrClues(
    val arabicLines: List<String> = emptyList(),
    val otherText: String = "",
    val surahHeaderHint: String = "",
    val visibleAyahNumbers: List<Int> = emptyList(),
    val suspectedSurahNumber: Int? = null,
    val suspectedAyahNumber: Int? = null,
    val rawCombinedText: String = "",
    val extractionEngineLabelBn: String = "লোকাল কুরআন ডাটাবেস"
)

private data class QuranFingerprintEntry(
    val surahNumber: Int,
    val ayahNumber: Int,
    val rawArabic: String,
    val normalizedArabic: String,
    val skeletonArabic: String,
    val arabicWords: List<String>,
    val skeletonWords: List<String>,
    val skeletonTrigrams: Set<String>,
    val pronunciationBn: String,
    val displayTranslationBn: String,
    val canonicalBn: String,
    val normalizedPronunciation: String
)

private data class ScoredLocalMatch(
    val entry: QuranFingerprintEntry,
    val score: Double,
    val confidencePercent: Int,
    val matchReasonBn: String
)

private data class RawLocalSurahAyah(
    val ayahNumber: Int,
    val arabicText: String,
    val pronunciationBn: String,
    val translationBn: String,
    val tafsirRaw: String
)

/**
 * Deterministic Local Quran Database Matching & Tafsir Loader (114 Surahs / 6,236 Ayahs).
 *
 * Solves camera/AI ayah misidentification by NEVER trusting AI-hallucinated verse numbers or
 * generated translations/tafsir. Instead:
 * 1. Normalizes Arabic across Uthmani (Madinah), IndoPak (Hafezi/Calcutta), and Imla'i scripts,
 *    including consonant-skeleton matching so dagger-alef differences (العلمين vs العالمين,
 *    السموت vs السماوات, الصلوة vs الصلاة) match accurately.
 * 2. Scores all 6,236 local ayahs using multi-signal fuzzy matching (exact substring, skeleton
 *    substring, word/bigram overlap, character 3-gram Dice similarity, Bengali translation &
 *    pronunciation matching, and verified reference/header boosts).
 * 3. Loads exact verified Ayah text, Bengali pronunciation, Dr. Abu Bakr Zakaria translation,
 *    complete local Tafsir (`tf`), and Surah Shan-e-Nuzul directly from bundled in-app assets
 *    (`assets/quran/surah_1..114.json` and `assets/surah_shane_nuzul.json`).
 */
object LocalQuranAyahMatcher {

    private val fingerprintMutex = Mutex()
    @Volatile
    private var fingerprintIndex: List<QuranFingerprintEntry> = emptyList()

    private val surahCacheLock = Any()
    private val surahAssetCache = HashMap<Int, List<RawLocalSurahAyah>>()

    private val surahMap: Map<Int, QuranSurah> by lazy {
        QuranSurahCatalog.all114Surahs.associateBy { it.number }
    }

    // All Arabic diacritics, small Quranic annotations, rub-el-hizb, sajdah, tatweel, pause marks
    private val arabicStripRegex = Regex(
        "[\u0610-\u061A\u0640\u064B-\u065F\u0670\u06D6-\u06ED\u08D3-\u08FF\uFD3E\uFD3F\u06DD\u06DE\u06E9]"
    )
    private val nonArabicKeepSpaceRegex = Regex("[^\u0621-\u064A\\s]+")
    private val multiSpaceRegex = Regex("\\s+")
    private val htmlBoldOpenRegex = Regex("<\\s*b\\s*>", RegexOption.IGNORE_CASE)
    private val htmlBoldCloseRegex = Regex("<\\s*/\\s*b\\s*>", RegexOption.IGNORE_CASE)
    private val htmlBreakRegex = Regex("<\\s*br\\s*/?\\s*>", RegexOption.IGNORE_CASE)
    private val htmlAnyTagRegex = Regex("<[^>]+>")

    /**
     * Deep normalization for Arabic Quranic text covering Uthmani, IndoPak, Persian/Urdu keyboards,
     * and standard Imla'i OCR output.
     */
    fun normalizeQuranArabic(input: String): String {
        if (input.isBlank()) return ""
        val stripped = arabicStripRegex.replace(input, "")
        val sb = StringBuilder(stripped.length)
        for (ch in stripped) {
            when (ch) {
                'أ', 'إ', 'آ', 'ٱ', 'ٲ', 'ٳ', 'ٵ' -> sb.append('ا')
                'ة', 'ۀ', 'ہ', 'ھ' -> sb.append('ه')
                'ى', 'ی', 'ے', 'ئ', 'ێ', 'ې' -> sb.append('ي')
                'ؤ', 'ٶ' -> sb.append('و')
                'ک', 'ګ', 'گ' -> sb.append('ك')
                'ء' -> {} // omit standalone hamza for Uthmani/Imla'i consistency
                else -> sb.append(ch)
            }
        }
        return nonArabicKeepSpaceRegex
            .replace(sb.toString(), " ")
            .replace(multiSpaceRegex, " ")
            .trim()
    }

    /**
     * Consonant-skeleton form that unifies Uthmani script spelling vs Imla'i OCR spelling:
     * e.g., العلمين == العالمين, السموت == السماوات, الصلوه == الصلاه, ذلك == ذالك, الرحمن == الرحمان.
     */
    fun toQuranArabicSkeleton(normalizedArabic: String): String {
        if (normalizedArabic.isBlank()) return ""
        val words = normalizedArabic.split(' ').filter { it.isNotEmpty() }
        val out = ArrayList<String>(words.size)
        for (w in words) {
            val sb = StringBuilder(w.length)
            for (i in w.indices) {
                val c = w[i]
                // Drop medial/trailing alef (keep initial alef for words like الله / ان / ال)
                if (c == 'ا' && i > 0) continue
                // Normalize classic Uthmani waw-for-alef before teh-marbuta (صلوه -> صله, زكوه -> زكه, حيوه -> حيه)
                if (c == 'و' && i == w.length - 2 && w.last() == 'ه' && w.length >= 4) continue
                sb.append(c)
            }
            val cleaned = sb.toString()
            if (cleaned.isNotEmpty()) out.add(cleaned)
        }
        return out.joinToString(" ")
    }

    fun normalizeDigitsToAscii(input: String): String {
        val bengali = "০১২৩৪৫৬৭৮৯"
        val arabicIndic = "٠١٢٣٤٥٦٧٨٩"
        val persianIndic = "۰۱۲۳۴۵۶۷۸۹"
        val sb = StringBuilder(input.length)
        for (ch in input) {
            val bIdx = bengali.indexOf(ch)
            if (bIdx >= 0) {
                sb.append(('0'.code + bIdx).toChar())
                continue
            }
            val aIdx = arabicIndic.indexOf(ch)
            if (aIdx >= 0) {
                sb.append(('0'.code + aIdx).toChar())
                continue
            }
            val pIdx = persianIndic.indexOf(ch)
            if (pIdx >= 0) {
                sb.append(('0'.code + pIdx).toChar())
                continue
            }
            sb.append(ch)
        }
        return sb.toString()
    }

    private fun buildTrigrams(text: String): Set<String> {
        val compact = text.replace(" ", "")
        if (compact.length < 3) return if (compact.isNotEmpty()) setOf(compact) else emptySet()
        val set = HashSet<String>(compact.length)
        for (i in 0..compact.length - 3) {
            set.add(compact.substring(i, i + 3))
        }
        return set
    }

    /**
     * Ensures the 6,236-ayah fingerprint index is built in RAM (shared with QuranFullTextSearchEngine).
     */
    suspend fun ensureReady(context: Context) {
        if (fingerprintIndex.isNotEmpty()) return
        fingerprintMutex.withLock {
            if (fingerprintIndex.isNotEmpty()) return
            val indexed = QuranFullTextSearchEngine.getIndexedAyahs(context)
            withContext(Dispatchers.Default) {
                val built = ArrayList<QuranFingerprintEntry>(indexed.size)
                for (item in indexed) {
                    val normAr = normalizeQuranArabic(item.arabicText)
                    val skelAr = toQuranArabicSkeleton(normAr)
                    val arWords = normAr.split(' ').filter { it.length >= 2 }
                    val skelWords = skelAr.split(' ').filter { it.length >= 2 }
                    built.add(
                        QuranFingerprintEntry(
                            surahNumber = item.surahNumber,
                            ayahNumber = item.ayahNumber,
                            rawArabic = item.arabicText,
                            normalizedArabic = normAr,
                            skeletonArabic = skelAr,
                            arabicWords = arWords,
                            skeletonWords = skelWords,
                            skeletonTrigrams = buildTrigrams(skelAr),
                            pronunciationBn = item.pronunciationBn,
                            displayTranslationBn = item.displayTranslationBn,
                            canonicalBn = item.canonicalBn,
                            normalizedPronunciation = item.normalizedPronunciation
                        )
                    )
                }
                fingerprintIndex = built
            }
        }
    }

    /**
     * Core deterministic lookup against all 6,236 local Quran ayahs.
     *
     * Priority:
     * 1. Verified Arabic Quran text matching (highest accuracy — immune to AI Surah/Ayah hallucinations!)
     * 2. Bengali translation / pronunciation text matching across all 6,236 local ayahs
     * 3. Explicit Surah:Ayah or Surah-name + Ayah-number reference matching
     * 4. Returns primary exact local AyahExplanation + full local Tafsir + up to 4 alternate local candidates.
     */
    suspend fun matchCluesInLocalDatabase(
        context: Context,
        clues: ExtractedQuranOcrClues,
        startTimeMs: Long = System.currentTimeMillis()
    ): Result<AyahExplanation> = withContext(Dispatchers.Default) {
        ensureReady(context)
        val entries = fingerprintIndex
        if (entries.isEmpty()) {
            return@withContext Result.failure(
                Exception("লোকাল কুরআন ডাটাবেস লোড করা যায়নি। অনুগ্রহ করে পুনরায় চেষ্টা করুন।")
            )
        }

        val combinedArabicRaw = buildString {
            clues.arabicLines.forEach { line ->
                if (line.isNotBlank()) {
                    append(line.trim())
                    append(' ')
                }
            }
            // Also extract any Arabic characters embedded in otherText or rawCombinedText
            if (isBlank() && clues.rawCombinedText.any { it in '\u0600'..'\u06FF' }) {
                append(clues.rawCombinedText)
            }
        }.trim()

        val normArabicQuery = normalizeQuranArabic(combinedArabicRaw)
        val skelArabicQuery = toQuranArabicSkeleton(normArabicQuery)
        val queryArabicWords = normArabicQuery.split(' ').filter { it.length >= 2 }
        val querySkeletonWords = skelArabicQuery.split(' ').filter { it.length >= 2 }
        val queryTrigrams = buildTrigrams(skelArabicQuery)

        // Check if query has substantial Arabic (beyond standalone Bismillah)
        val bismillahSkeleton = toQuranArabicSkeleton(normalizeQuranArabic("بسم الله الرحمن الرحيم"))
        val isOnlyBismillahQuery = skelArabicQuery.isNotBlank() &&
            (skelArabicQuery == bismillahSkeleton ||
                (skelArabicQuery.contains(bismillahSkeleton) && skelArabicQuery.length <= bismillahSkeleton.length + 4))

        // Strip leading Bismillah from a multi-line scan if substantial ayah text follows it
        val strippedNormArabic = stripLeadingBismillahIfFollowedByAyah(normArabicQuery)
        val strippedSkelArabic = toQuranArabicSkeleton(strippedNormArabic)
        val effectiveNormArabic = if (strippedNormArabic.length >= 6) strippedNormArabic else normArabicQuery
        val effectiveSkelArabic = if (strippedSkelArabic.length >= 5) strippedSkelArabic else skelArabicQuery
        val effectiveSkelWords = effectiveSkelArabic.split(' ').filter { it.length >= 2 }
        val effectiveTrigrams = buildTrigrams(effectiveSkelArabic)
        val hasStrongArabic = effectiveSkelArabic.replace(" ", "").length >= 6

        // Per-line Arabic variants (crucial when a photo shows 2-3 consecutive ayahs on a Mushaf page!)
        val cleanedArabicLines = clues.arabicLines
            .map { normalizeQuranArabic(it) }
            .filter { it.replace(" ", "").length >= 6 }
        val cleanedSkeletonLines = cleanedArabicLines.map { toQuranArabicSkeleton(it) }

        // Bengali / English / Reference signals
        val otherTextClean = clues.otherText.ifBlank { clues.rawCombinedText }.trim()
        val canonicalBanglaQuery = QuranFullTextSearchEngine.canonicalizeBangla(otherTextClean)
        val banglaTokens = canonicalBanglaQuery
            .split(' ')
            .filter { it.length >= 3 && it.any { c -> c in '\u0980'..'\u09FF' } }
            .distinct()
        val hasBanglaText = banglaTokens.size >= 2

        val detectedSurahHint = resolveSurahNumberHint(clues.surahHeaderHint, otherTextClean)
            ?: clues.suspectedSurahNumber?.takeIf { it in 1..114 }

        val explicitRef = extractExplicitReference(otherTextClean)
            ?: if (clues.suspectedSurahNumber in 1..114 && (clues.suspectedAyahNumber ?: 0) >= 1) {
                clues.suspectedSurahNumber!! to clues.suspectedAyahNumber!!
            } else {
                null
            }

        val visibleAyahNums = (clues.visibleAyahNumbers + extractVisibleAyahNumbers(clues.rawCombinedText))
            .filter { it in 1..286 }
            .toSet()

        val scored = ArrayList<ScoredLocalMatch>(32)

        for (entry in entries) {
            var score = 0.0
            var reason = ""

            // --- SIGNAL 1: Arabic Quranic Matching (Primary & Authoritative) ---
            if (hasStrongArabic) {
                val entryCompactNorm = entry.normalizedArabic.replace(" ", "")
                val entryCompactSkel = entry.skeletonArabic.replace(" ", "")
                val queryCompactNorm = effectiveNormArabic.replace(" ", "")
                val queryCompactSkel = effectiveSkelArabic.replace(" ", "")

                // 1A. Exact normalized Arabic match or containment
                if (entry.normalizedArabic == effectiveNormArabic) {
                    score += 1500.0
                    reason = "হুবহু আরবি আয়াত মিলেছে"
                } else if (entryCompactNorm.length >= 6 && queryCompactNorm.length >= 6) {
                    when {
                        entryCompactNorm.contains(queryCompactNorm) -> {
                            val coverage = queryCompactNorm.length.toDouble() / entryCompactNorm.length.coerceAtLeast(1)
                            score += 980.0 + (coverage * 360.0)
                            reason = "আরবি আয়াতের অংশ হুবহু মিলেছে"
                        }
                        queryCompactNorm.contains(entryCompactNorm) && entryCompactNorm.length >= 10 -> {
                            val coverage = entryCompactNorm.length.toDouble() / queryCompactNorm.length.coerceAtLeast(1)
                            score += 940.0 + (coverage * 340.0)
                            reason = "সম্পূর্ণ আরবি আয়াত ফ্রেমে মিলেছে"
                        }
                    }
                }

                // 1B. Uthmani-skeleton exact / containment match
                if (score < 900.0 && entryCompactSkel.length >= 5 && queryCompactSkel.length >= 5) {
                    when {
                        entry.skeletonArabic == effectiveSkelArabic -> {
                            score = maxOf(score, 1420.0)
                            reason = "হুবহু উসমানী আরবি আয়াত মিলেছে"
                        }
                        entryCompactSkel.contains(queryCompactSkel) -> {
                            val coverage = queryCompactSkel.length.toDouble() / entryCompactSkel.length.coerceAtLeast(1)
                            val s = 920.0 + (coverage * 320.0)
                            if (s > score) {
                                score = s
                                reason = "উসমানী/হাফেজী আরবি টেক্সট মিলেছে"
                            }
                        }
                        queryCompactSkel.contains(entryCompactSkel) && entryCompactSkel.length >= 9 -> {
                            val coverage = entryCompactSkel.length.toDouble() / queryCompactSkel.length.coerceAtLeast(1)
                            val s = 880.0 + (coverage * 300.0)
                            if (s > score) {
                                score = s
                                reason = "পৃষ্ঠার স্ক্যানকৃত আয়াতে মিলেছে"
                            }
                        }
                    }
                }

                // 1C. Individual Arabic line containment (handles multi-ayah photo frames!)
                if (score < 850.0 && cleanedSkeletonLines.size >= 2) {
                    var bestLineScore = 0.0
                    for (lineSkel in cleanedSkeletonLines) {
                        val compactLine = lineSkel.replace(" ", "")
                        if (compactLine.length < 7) continue
                        if (entryCompactSkel.contains(compactLine)) {
                            val cov = compactLine.length.toDouble() / entryCompactSkel.length.coerceAtLeast(1)
                            bestLineScore = maxOf(bestLineScore, 780.0 + cov * 260.0)
                        } else if (compactLine.contains(entryCompactSkel) && entryCompactSkel.length >= 9) {
                            bestLineScore = maxOf(bestLineScore, 800.0)
                        }
                    }
                    if (bestLineScore > score) {
                        score = bestLineScore
                        reason = "আয়াতের লাইন লোকাল কুরআনে মিলেছে"
                    }
                }

                // 1D. Word overlap + ordered bigram overlap on skeleton words (tolerant to OCR noise)
                if (effectiveSkelWords.isNotEmpty() && entry.skeletonWords.isNotEmpty()) {
                    val entryWordSet = entry.skeletonWords.toHashSet()
                    var matchedWords = 0
                    for (qw in effectiveSkelWords) {
                        if (qw in entryWordSet) matchedWords++
                    }
                    if (matchedWords >= 2 || (effectiveSkelWords.size == 1 && matchedWords == 1)) {
                        val precision = matchedWords.toDouble() / effectiveSkelWords.size
                        val recall = matchedWords.toDouble() / entry.skeletonWords.size
                        val f1 = if (precision + recall > 0) (2.0 * precision * recall) / (precision + recall) else 0.0
                        val bigramBonus = computeOrderedBigramBonus(effectiveSkelWords, entry.skeletonWords)
                        val wordScore = (f1 * 720.0) + (matchedWords.coerceAtMost(8) * 32.0) + bigramBonus
                        if (wordScore > score) {
                            score = wordScore
                            reason = "আরবি শব্দের ধারাবাহিক মিল পাওয়া গেছে"
                        } else if (score > 0) {
                            score += bigramBonus * 0.4
                        }
                    }
                }

                // 1E. Character 3-gram Dice similarity (tolerant to merged/split Arabic OCR words)
                if (score < 480.0 && effectiveTrigrams.size >= 4 && entry.skeletonTrigrams.size >= 4) {
                    var common = 0
                    for (tg in effectiveTrigrams) {
                        if (tg in entry.skeletonTrigrams) common++
                    }
                    if (common >= 4) {
                        val dice = (2.0 * common) / (effectiveTrigrams.size + entry.skeletonTrigrams.size)
                        val queryContainRatio = common.toDouble() / effectiveTrigrams.size
                        val combinedSim = maxOf(dice, queryContainRatio * 0.85)
                        if (combinedSim >= 0.42) {
                            val trigramScore = combinedSim * 660.0
                            if (trigramScore > score) {
                                score = trigramScore
                                reason = "আরবি অক্ষর বিশ্লেষণে কাছাকাছি মিল"
                            }
                        }
                    }
                }

                // Prevent Surah 1:1 (Bismillah) from hijacking a real ayah unless user scanned only Bismillah
                if (!isOnlyBismillahQuery && entry.surahNumber == 1 && entry.ayahNumber == 1 && effectiveSkelWords.size > 4) {
                    score *= 0.35
                }
            }

            // --- SIGNAL 2: Bengali Translation / Pronunciation Matching ---
            if (hasBanglaText && score < 700.0) {
                var bnWordHits = 0
                for (tok in banglaTokens) {
                    if (entry.canonicalBn.contains(tok) || entry.normalizedPronunciation.contains(tok)) {
                        bnWordHits++
                    }
                }
                if (bnWordHits >= 2) {
                    val ratio = bnWordHits.toDouble() / banglaTokens.size.coerceAtMost(10)
                    val bnScore = (ratio * 580.0) + (bnWordHits.coerceAtMost(8) * 38.0)
                    if (bnScore > score) {
                        score = bnScore
                        reason = "লোকাল কুরআনের বাংলা অনুবাদ/উচ্চারণে মিলেছে"
                    } else if (score > 200.0) {
                        score += 120.0
                    }
                }
            }

            // --- SIGNAL 3: English Translation Matching (for English Quran pages / offline ML Kit) ---
            if (!hasStrongArabic && !hasBanglaText && otherTextClean.length >= 12) {
                val engTrans = QuranTafsirAndTranslationProvider.getEnglishTranslation(
                    entry.surahNumber,
                    entry.ayahNumber,
                    null
                )
                if (!engTrans.startsWith("[Saheeh International]")) {
                    val engScore = scoreEnglishTextSimilarity(otherTextClean, engTrans)
                    if (engScore > score) {
                        score = engScore
                        reason = "ইংরেজি অনুবাদ লোকাল কুরআনে মিলেছে"
                    }
                }
            }

            // --- SIGNAL 4: Verified Context Hint Boosts (Never blindly overrides a strong Arabic match!) ---
            val hasTextScore = score >= 190.0
            if (hasTextScore) {
                if (detectedSurahHint != null && entry.surahNumber == detectedSurahHint) {
                    score += 180.0
                }
                if (entry.ayahNumber in visibleAyahNums) {
                    score += 140.0
                }
                if (explicitRef != null && entry.surahNumber == explicitRef.first && entry.ayahNumber == explicitRef.second) {
                    score += 240.0
                }
            } else if (!hasStrongArabic && explicitRef != null &&
                entry.surahNumber == explicitRef.first &&
                entry.ayahNumber == explicitRef.second
            ) {
                // Only use bare Surah:Ayah reference when no conflicting Arabic text exists
                score = 860.0
                reason = "সূরা ও আয়াত নম্বর (${explicitRef.first}:${explicitRef.second}) মিলেছে"
            }

            if (score >= 240.0) {
                val confidence = computeConfidencePercent(score)
                scored.add(
                    ScoredLocalMatch(
                        entry = entry,
                        score = score,
                        confidencePercent = confidence,
                        matchReasonBn = reason.ifBlank { "লোকাল কুরআন ডাটাবেসে মিলেছে" }
                    )
                )
            }
        }

        // Fallback: If Arabic OCR was slightly noisy and we have an explicit Surah:Ayah hint from Vision AI,
        // verify it only if it's a valid ayah in that Surah AND no strong text conflict won.
        if (scored.isEmpty() && explicitRef != null) {
            val refEntry = entries.find {
                it.surahNumber == explicitRef.first && it.ayahNumber == explicitRef.second
            }
            if (refEntry != null) {
                scored.add(
                    ScoredLocalMatch(
                        entry = refEntry,
                        score = 620.0,
                        confidencePercent = 76,
                        matchReasonBn = "রেফারেন্স ও পৃষ্ঠার সূত্র অনুযায়ী লোকাল ডাটাবেস থেকে প্রাপ্ত"
                    )
                )
            }
        }

        if (scored.isEmpty()) {
            return@withContext Result.failure(
                Exception(
                    "ছবি থেকে প্রাপ্ত লেখা লোকাল কুরআন ডাটাবেসের (১১৪ সূরা • ৬,২৩৬ আয়াত) কোনো আয়াতের সাথে নিশ্চিতভাবে মেলেনি। " +
                        "অনুগ্রহ করে আয়াতের লেখাটি ফ্রেমের মাঝখানে পরিষ্কার ও উজ্জ্বল আলোতে রেখে আবার স্ক্যান করুন, অথবা নিচের 'খুঁজুন' বা 'সূরা নির্বাচন' ব্যবহার করুন।"
                )
            )
        }

        scored.sortByDescending { it.score }
        val topMatches = scored.distinctBy { it.entry.surahNumber to it.entry.ayahNumber }.take(5)
        val best = topMatches.first()

        val alternates = topMatches.drop(1).map { candidate ->
            val meta = surahMap[candidate.entry.surahNumber]
            AyahMatchSummary(
                surahNumber = candidate.entry.surahNumber,
                ayahNumber = candidate.entry.ayahNumber,
                surahNameBn = meta?.nameBn ?: "সূরা ${candidate.entry.surahNumber}",
                surahNameAr = meta?.nameAr ?: "",
                arabicSnippet = candidate.entry.rawArabic.take(90),
                banglaSnippet = candidate.entry.displayTranslationBn.take(110),
                confidencePercent = candidate.confidencePercent,
                matchReasonBn = candidate.matchReasonBn
            )
        }

        val durationMs = (System.currentTimeMillis() - startTimeMs).coerceAtLeast(18L)
        val explanation = loadAyahExplanationFromLocalDb(
            context = context,
            surahNumber = best.entry.surahNumber,
            ayahNumber = best.entry.ayahNumber,
            scanDurationMs = durationMs,
            extractedRawOcrText = combinedArabicRaw.ifBlank { clues.rawCombinedText },
            matchConfidencePercent = best.confidencePercent,
            matchSourceLabelBn = "${best.matchReasonBn} • ${clues.extractionEngineLabelBn}",
            alternateMatches = alternates
        )

        Result.success(explanation)
    }

    /**
     * Loads exact, 100% locally verified Ayah details + complete Bengali Tafsir directly from
     * `assets/quran/surah_{surahNumber}.json` and `assets/surah_shane_nuzul.json`, enriched with
     * `QuranAyahCatalog` word-by-word meanings and related hadiths when available.
     *
     * Works for ALL 114 Surahs (all 6,236 Ayahs) completely offline!
     */
    suspend fun loadAyahExplanationFromLocalDb(
        context: Context,
        surahNumber: Int,
        ayahNumber: Int,
        scanDurationMs: Long = 20L,
        extractedRawOcrText: String = "",
        matchConfidencePercent: Int = 100,
        matchSourceLabelBn: String = "লোকাল কুরআন ডাটাবেস (১১৪ সূরা • ৬,২৩৬ আয়াত)",
        alternateMatches: List<AyahMatchSummary> = emptyList()
    ): AyahExplanation = withContext(Dispatchers.IO) {
        val safeSurahNum = surahNumber.coerceIn(1, 114)
        val surahMeta = surahMap[safeSurahNum] ?: QuranSurahCatalog.all114Surahs.first()
        val safeAyahNum = ayahNumber.coerceIn(1, surahMeta.totalAyat)

        // 1. Load EXACT ayah + complete Tafsir from local assets/quran/surah_{n}.json
        val surahAyahs = loadSurahRawFromAssets(context, safeSurahNum)
        val localAyah = surahAyahs.find { it.ayahNumber == safeAyahNum }
            ?: surahAyahs.getOrNull(safeAyahNum - 1)

        // 2. Check optional curated rich catalog enrichment (for word-by-word & related hadiths)
        val catalogEntry = QuranAyahCatalog.catalog.find {
            it.surahNumber == safeSurahNum && it.ayahNumber == safeAyahNum
        }

        val exactArabic = localAyah?.arabicText?.takeIf { it.isNotBlank() }
            ?: catalogEntry?.arabicText
            ?: ""

        val exactPronunciation = when {
            !localAyah?.pronunciationBn.isNullOrBlank() ->
                QuranBengaliPhoneticTransliteration.cleanPronunciationText(localAyah!!.pronunciationBn)
            !catalogEntry?.transliterationBn.isNullOrBlank() -> catalogEntry!!.transliterationBn
            exactArabic.isNotBlank() ->
                QuranBengaliPhoneticTransliteration.getPronunciation(safeSurahNum, safeAyahNum, exactArabic)
            else -> ""
        }

        val exactBanglaTranslation = localAyah?.translationBn?.takeIf { it.isNotBlank() }
            ?.let { QuranFullTextSearchEngine.cleanTranslationDisplay(it) }
            ?: catalogEntry?.banglaTranslation
            ?: ""

        val formattedLocalTafsir = cleanLocalTafsirHtml(localAyah?.tafsirRaw.orEmpty())

        // Load real Surah Shan-e-Nuzul from bundled assets/surah_shane_nuzul.json
        val shaneNuzulObj = SurahShaneNuzulCatalog.getShaneNuzul(context, safeSurahNum)
        val authenticContextBn = buildString {
            if (!catalogEntry?.contextBn.isNullOrBlank()) {
                append(catalogEntry!!.contextBn.trim())
            }
            if (shaneNuzulObj != null) {
                if (isNotEmpty()) append("\n\n")
                if (shaneNuzulObj.shaneNuzul.isNotBlank()) {
                    append("শানে নুযূল (সূরা ${surahMeta.nameBn}):\n")
                    append(shaneNuzulObj.shaneNuzul.trim())
                }
                if (shaneNuzulObj.themes.isNotBlank()) {
                    append("\n\nমূল আলোচ্য বিষয়:\n")
                    append(shaneNuzulObj.themes.trim())
                }
                if (shaneNuzulObj.virtues.isNotBlank()) {
                    append("\n\nফযীলত ও গুরুত্ব:\n")
                    append(shaneNuzulObj.virtues.trim())
                }
            }
            if (isBlank()) {
                append("সূরা ${surahMeta.nameBn} (${surahMeta.nameAr}) পবিত্র কুরআনের $safeSurahNum নং সূরা। এটি ${surahMeta.revelationType} এবং এর মোট আয়াত সংখ্যা ${surahMeta.totalAyat}।")
            }
        }

        val quranAyahModel = QuranAyah(
            surahNumber = safeSurahNum,
            ayahNumber = safeAyahNum,
            arabicText = exactArabic,
            pronunciationBn = exactPronunciation,
            translationBn = exactBanglaTranslation,
            translationZakaria = exactBanglaTranslation,
            tafsirText = formattedLocalTafsir
        )

        val fullTafsirBn = when {
            formattedLocalTafsir.isNotBlank() && !catalogEntry?.tafsirBn.isNullOrBlank() -> {
                "তাফসীর ও ব্যাখ্যা (ড. আবু বকর মুহাম্মাদ যাকারিয়া — লোকাল কুরআন ডাটাবেস):\n$formattedLocalTafsir\n\n— সংক্ষিপ্ত সারসংক্ষেপ (তাফসীরে ইবনে কাসীর) —\n${catalogEntry!!.tafsirBn}"
            }
            formattedLocalTafsir.isNotBlank() -> {
                "তাফসীর ও ব্যাখ্যা (ড. আবু বকর মুহাম্মাদ যাকারিয়া — লোকাল কুরআন ডাটাবেস):\n$formattedLocalTafsir"
            }
            !catalogEntry?.tafsirBn.isNullOrBlank() -> catalogEntry!!.tafsirBn
            else -> {
                // If this short ayah's footnote in surah_X.json is empty, also check nearby verse context
                val nearbyNote = findNearbyTafsirNote(surahAyahs, safeAyahNum)
                if (nearbyNote.isNotBlank()) {
                    "তাফসীর ও প্রাসঙ্গিক ব্যাখ্যা (ড. আবু বকর যাকারিয়া — লোকাল ডাটাবেস):\n$nearbyNote"
                } else {
                    QuranTafsirAndTranslationProvider.getTafsirText(
                        ayah = quranAyahModel,
                        source = QuranTafsirSource.DR_ZAKARIA
                    )
                }
            }
        }

        val englishTranslation = catalogEntry?.englishTranslation?.takeIf { it.isNotBlank() }
            ?: QuranTafsirAndTranslationProvider.getEnglishTranslation(safeSurahNum, safeAyahNum, null)

        val wordByWord = if (!catalogEntry?.wordByWord.isNullOrEmpty()) {
            catalogEntry!!.wordByWord
        } else {
            exactArabic.split(multiSpaceRegex)
                .filter { it.isNotBlank() }
                .map { w ->
                    WordMeaning(
                        arabicWord = w,
                        bengaliMeaning = QuranBengaliPhoneticTransliteration.transliterate(w),
                        englishMeaning = "",
                        grammarNote = "কুরআনী শব্দ"
                    )
                }
        }

        // Build real adjacent/contextual verses from the SAME surah in local DB!
        val contextualRelatedVerses = if (!catalogEntry?.relatedVerses.isNullOrEmpty()) {
            catalogEntry!!.relatedVerses
        } else {
            buildAdjacentVersesFromSurah(surahMeta, surahAyahs, safeAyahNum)
        }

        val relatedHadiths = catalogEntry?.relatedHadiths?.takeIf { it.isNotEmpty() } ?: listOf(
            RelatedHadith(
                "সহীহ আল-বুখারী (৫০২৭)",
                "উসমান ইবনে আফফান (রা.)",
                "তোমাদের মধ্যে সর্বোত্তম সেই ব্যক্তি, যে নিজে কুরআন শিখে এবং অপরকে শিক্ষা দেয়।",
                "সহীহ"
            ),
            RelatedHadith(
                "সহীহ মুসলিম (৮০৪)",
                "আবু উমামা (রা.)",
                "তোমরা কুরআন তিলাওয়াত করো; কেননা কিয়ামতের দিন তা তার পাঠকদের জন্য সুপারিশকারী হয়ে আগমন করবে।",
                "সহীহ"
            )
        )

        val audioSurahStr = safeSurahNum.toString().padStart(3, '0')
        val audioAyahStr = safeAyahNum.toString().padStart(3, '0')
        val audioUrl = "https://everyayah.com/data/Alafasy_128kbps/$audioSurahStr$audioAyahStr.mp3"

        AyahExplanation(
            id = "local_quran_${safeSurahNum}_$safeAyahNum",
            surahNumber = safeSurahNum,
            ayahNumber = safeAyahNum,
            surahNameArabic = surahMeta.nameAr,
            surahNameBangla = "সূরা ${surahMeta.nameBn}",
            surahNameEnglish = "Surah ${surahMeta.nameEn}",
            revelationTypeBn = surahMeta.revelationType,
            totalAyahsInSurah = surahMeta.totalAyat,
            arabicText = exactArabic,
            transliterationBn = exactPronunciation,
            banglaTranslation = exactBanglaTranslation,
            englishTranslation = englishTranslation,
            wordByWord = wordByWord,
            tafsirBn = fullTafsirBn,
            contextBn = authenticContextBn,
            relatedVerses = contextualRelatedVerses,
            relatedHadiths = relatedHadiths,
            audioUrl = audioUrl,
            reciterNameBn = "মিশারী রাশিদ আল-আফাসী",
            scanDurationMs = scanDurationMs,
            contentType = IslamicContentType.QURAN,
            targetSourceType = TargetSourceType.ISLAMIC_BOOK,
            extractedRawOcrText = extractedRawOcrText,
            detectedLanguage = "আরবী ও বাংলা (লোকাল কুরআন ডাটাবেস)",
            sourceBookName = "পবিত্র আল-কুরআনুল কারীম (লোকাল ইন-অ্যাপ ডাটাবেস)",
            sourceReferenceNumber = "সূরা ${surahMeta.nameBn} ($safeSurahNum:$safeAyahNum) • মোট ${surahMeta.totalAyat} আয়াত",
            scholarOrNarrator = "ড. আবু বকর মুহাম্মাদ যাকারিয়া (তাফসীর ও অনুবাদ)",
            authenticityOrGrading = "লোকাল কুরআন ডাটাবেস থেকে ১০০% যাচাইকৃত",
            scholarlyContext = shaneNuzulObj?.tafsirPerspectives?.takeIf { it.isNotBlank() } ?: "",
            matchConfidencePercent = matchConfidencePercent,
            matchSourceLabelBn = matchSourceLabelBn,
            alternateMatches = alternateMatches,
            isVerifiedFromLocalQuranDb = true,
            rawLocalTafsirBn = formattedLocalTafsir
        )
    }

    /**
     * Instant search across ALL 6,236 local ayahs for user-typed Arabic, Bengali translation,
     * Bengali pronunciation, Surah name, or Surah:Ayah reference (e.g., "59:22", "৫৯:২২", "আয়াতুল কুরসী").
     */
    suspend fun searchLocalDatabaseByQuery(
        context: Context,
        rawQuery: String
    ): AyahExplanation? = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        val trimmed = rawQuery.trim()
        if (trimmed.isBlank()) return@withContext null

        ensureReady(context)

        // 1. Direct Surah:Ayah reference (e.g. "59:22", "৫৯:২২", "2 255", "সূরা বাকারা ২৫৫")
        val directRef = extractExplicitReference(trimmed)
        if (directRef != null) {
            return@withContext loadAyahExplanationFromLocalDb(
                context = context,
                surahNumber = directRef.first,
                ayahNumber = directRef.second,
                scanDurationMs = (System.currentTimeMillis() - startTime).coerceAtLeast(10L),
                extractedRawOcrText = trimmed,
                matchConfidencePercent = 100,
                matchSourceLabelBn = "সরাসরি সূরা ও আয়াত রেফারেন্স (${directRef.first}:${directRef.second})"
            )
        }

        // 2. If query contains Arabic script, run our deep Uthmani/IndoPak Arabic matcher
        if (trimmed.any { it in '\u0600'..'\u06FF' }) {
            val arabicMatch = matchCluesInLocalDatabase(
                context = context,
                clues = ExtractedQuranOcrClues(
                    arabicLines = listOf(trimmed),
                    rawCombinedText = trimmed,
                    extractionEngineLabelBn = "লোকাল আরবি কুরআন সার্চ"
                ),
                startTimeMs = startTime
            )
            if (arabicMatch.isSuccess) return@withContext arabicMatch.getOrNull()
        }

        // 3. Check exact Surah name or bare Surah number (1-114)
        val asciiDigits = normalizeDigitsToAscii(trimmed)
        val bareSurahNum = asciiDigits.toIntOrNull()
        if (bareSurahNum != null && bareSurahNum in 1..114) {
            return@withContext loadAyahExplanationFromLocalDb(
                context = context,
                surahNumber = bareSurahNum,
                ayahNumber = 1,
                scanDurationMs = (System.currentTimeMillis() - startTime).coerceAtLeast(10L),
                extractedRawOcrText = trimmed,
                matchConfidencePercent = 100,
                matchSourceLabelBn = "সূরা নম্বর ($bareSurahNum) অনুযায়ী লোকাল ডাটাবেস"
            )
        }

        val surahByName = resolveSurahNumberHint(trimmed, "")
        if (surahByName != null && trimmed.split(multiSpaceRegex).size <= 3) {
            val ayahInQuery = Regex("""\b(\d{1,3})\b""").find(asciiDigits)?.groupValues?.get(1)?.toIntOrNull() ?: 1
            return@withContext loadAyahExplanationFromLocalDb(
                context = context,
                surahNumber = surahByName,
                ayahNumber = ayahInQuery,
                scanDurationMs = (System.currentTimeMillis() - startTime).coerceAtLeast(10L),
                extractedRawOcrText = trimmed,
                matchConfidencePercent = 100,
                matchSourceLabelBn = "সূরার নাম অনুযায়ী লোকাল ডাটাবেস"
            )
        }

        // 4. Full-Quran 6,236-ayah Bengali/Pronunciation/Topic search via QuranFullTextSearchEngine
        val fullSearch = QuranFullTextSearchEngine.search(
            context = context,
            rawQuery = trimmed,
            includeTafsir = false
        )
        if (fullSearch.hits.isNotEmpty()) {
            val bestHit = fullSearch.hits.first()
            val alternates = fullSearch.hits.drop(1).take(4).map { hit ->
                AyahMatchSummary(
                    surahNumber = hit.ayah.surahNumber,
                    ayahNumber = hit.ayah.ayahNumber,
                    surahNameBn = hit.surah.nameBn,
                    surahNameAr = hit.surah.nameAr,
                    arabicSnippet = hit.ayah.arabicText.take(90),
                    banglaSnippet = hit.ayah.translationBn.take(110),
                    confidencePercent = (hit.score / 6).coerceIn(72, 99),
                    matchReasonBn = hit.matchType.labelBn
                )
            }
            return@withContext loadAyahExplanationFromLocalDb(
                context = context,
                surahNumber = bestHit.ayah.surahNumber,
                ayahNumber = bestHit.ayah.ayahNumber,
                scanDurationMs = (System.currentTimeMillis() - startTime).coerceAtLeast(15L),
                extractedRawOcrText = trimmed,
                matchConfidencePercent = (bestHit.score / 5).coerceIn(78, 99),
                matchSourceLabelBn = "${bestHit.matchType.labelBn} • লোকাল কুরআন ডাটাবেস",
                alternateMatches = alternates
            )
        }

        null
    }

    private fun loadSurahRawFromAssets(context: Context, surahNumber: Int): List<RawLocalSurahAyah> {
        synchronized(surahCacheLock) {
            surahAssetCache[surahNumber]?.let { return it }
        }
        val list = ArrayList<RawLocalSurahAyah>()
        try {
            context.applicationContext.assets.open("quran/surah_$surahNumber.json").use { stream ->
                JsonReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                    reader.beginArray()
                    var fallbackAyah = 1
                    while (reader.hasNext()) {
                        var ayaNum = fallbackAyah
                        var ar = ""
                        var pr = ""
                        var bn = ""
                        var tf = ""
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "aya" -> ayaNum = reader.nextInt()
                                "ar" -> ar = reader.nextString()
                                "pr" -> pr = reader.nextString()
                                "bn" -> bn = reader.nextString()
                                "tf" -> tf = reader.nextString()
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                        list.add(
                            RawLocalSurahAyah(
                                ayahNumber = ayaNum,
                                arabicText = ar,
                                pronunciationBn = pr,
                                translationBn = bn,
                                tafsirRaw = tf
                            )
                        )
                        fallbackAyah++
                    }
                    reader.endArray()
                }
            }
        } catch (_: Exception) {
            // Gracefully return empty if unreadable
        }
        if (list.isNotEmpty()) {
            synchronized(surahCacheLock) {
                // Keep LRU-like bounded size (up to 16 surahs cached in memory)
                if (surahAssetCache.size >= 16) {
                    surahAssetCache.clear()
                }
                surahAssetCache[surahNumber] = list
            }
        }
        return list
    }

    private fun cleanLocalTafsirHtml(raw: String): String {
        if (raw.isBlank()) return ""
        return raw
            .replace(htmlBoldOpenRegex, "◆ ")
            .replace(htmlBoldCloseRegex, "")
            .replace(htmlBreakRegex, "\n")
            .replace(htmlAnyTagRegex, "")
            .replace("&nbsp;", " ")
            .replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }

    private fun findNearbyTafsirNote(surahAyahs: List<RawLocalSurahAyah>, targetAyah: Int): String {
        // Check immediate preceding or following ayah in case tafsir groups consecutive verses
        val offsets = listOf(-1, 1, -2, 2)
        for (offset in offsets) {
            val candidate = surahAyahs.find { it.ayahNumber == targetAyah + offset }
            val cleaned = cleanLocalTafsirHtml(candidate?.tafsirRaw.orEmpty())
            if (cleaned.length >= 40) {
                return "(আয়াত ${candidate!!.ayahNumber}-এর প্রাসঙ্গিক তাফসীর থেকে):\n$cleaned"
            }
        }
        return ""
    }

    private fun buildAdjacentVersesFromSurah(
        surahMeta: QuranSurah,
        surahAyahs: List<RawLocalSurahAyah>,
        currentAyah: Int
    ): List<RelatedVerse> {
        val result = ArrayList<RelatedVerse>(2)
        val prev = surahAyahs.find { it.ayahNumber == currentAyah - 1 }
        val next = surahAyahs.find { it.ayahNumber == currentAyah + 1 }
        if (prev != null) {
            result.add(
                RelatedVerse(
                    surahNameBn = "পূর্ববর্তী আয়াত • সূরা ${surahMeta.nameBn}",
                    ayahRef = "${surahMeta.number}:${prev.ayahNumber}",
                    arabicText = prev.arabicText,
                    translationBn = QuranFullTextSearchEngine.cleanTranslationDisplay(prev.translationBn)
                )
            )
        }
        if (next != null) {
            result.add(
                RelatedVerse(
                    surahNameBn = "পরবর্তী আয়াত • সূরা ${surahMeta.nameBn}",
                    ayahRef = "${surahMeta.number}:${next.ayahNumber}",
                    arabicText = next.arabicText,
                    translationBn = QuranFullTextSearchEngine.cleanTranslationDisplay(next.translationBn)
                )
            )
        }
        return result
    }

    private fun stripLeadingBismillahIfFollowedByAyah(normArabic: String): String {
        val bismillahPrefix = "بسم الله الرحمن الرحيم"
        if (normArabic.startsWith(bismillahPrefix)) {
            val remainder = normArabic.removePrefix(bismillahPrefix).trim()
            if (remainder.replace(" ", "").length >= 6) {
                return remainder
            }
        }
        return normArabic
    }

    private fun computeOrderedBigramBonus(queryWords: List<String>, entryWords: List<String>): Double {
        if (queryWords.size < 2 || entryWords.size < 2) return 0.0
        val entryBigrams = HashSet<String>(entryWords.size)
        for (i in 0 until entryWords.size - 1) {
            entryBigrams.add("${entryWords[i]} ${entryWords[i + 1]}")
        }
        var hits = 0
        for (i in 0 until queryWords.size - 1) {
            if ("${queryWords[i]} ${queryWords[i + 1]}" in entryBigrams) {
                hits++
            }
        }
        return hits.coerceAtMost(6) * 65.0
    }

    private fun scoreEnglishTextSimilarity(queryRaw: String, englishTranslation: String): Double {
        val stopWords = setOf(
            "the", "and", "for", "that", "with", "from", "this", "have", "are", "not", "you", "your",
            "who", "will", "they", "them", "their", "what", "which", "upon", "into", "has", "had"
        )
        val qWords = queryRaw.lowercase()
            .replace(Regex("[^a-z\\s]"), " ")
            .split(multiSpaceRegex)
            .filter { it.length >= 4 && it !in stopWords }
            .distinct()
        if (qWords.size < 2) return 0.0

        val lowerTrans = englishTranslation.lowercase()
        var hits = 0
        for (w in qWords) {
            if (lowerTrans.contains(w)) hits++
        }
        if (hits < 2) return 0.0
        val ratio = hits.toDouble() / qWords.size.coerceAtMost(8)
        return if (ratio >= 0.45) (ratio * 560.0) + (hits * 30.0) else 0.0
    }

    fun extractExplicitReference(text: String): Pair<Int, Int>? {
        if (text.isBlank()) return null
        val ascii = normalizeDigitsToAscii(text)
        // Strict Surah:Ayah with colon or slash (e.g., 2:255, 59:22, 112/1)
        val strictColon = Regex("""\b(\d{1,3})\s*[:/]\s*(\d{1,3})\b""").find(ascii)
        if (strictColon != null) {
            val s = strictColon.groupValues[1].toIntOrNull()
            val a = strictColon.groupValues[2].toIntOrNull()
            if (s != null && s in 1..114 && a != null) {
                val maxAyah = surahMap[s]?.totalAyat ?: 286
                if (a in 1..maxAyah) return s to a
            }
        }
        // Surah name + ayah number (e.g., "সূরা হাশর ২২", "Surah Al-Baqarah 255", "বাqারা 255")
        val surahNum = resolveSurahNumberHint(text, "")
        if (surahNum != null) {
            val maxAyah = surahMap[surahNum]?.totalAyat ?: 286
            val ayahMatch = Regex("""(?:আয়াত|আয়াত|ayah|ayat|verse|v\.?|\(|\[|:|\s)(\d{1,3})\b""", RegexOption.IGNORE_CASE)
                .findAll(ascii)
                .mapNotNull { it.groupValues[1].toIntOrNull() }
                .firstOrNull { it in 1..maxAyah && it != surahNum }
            if (ayahMatch != null) {
                return surahNum to ayahMatch
            }
        }
        return null
    }

    fun resolveSurahNumberHint(headerText: String, secondaryText: String): Int? {
        val combined = "$headerText $secondaryText".trim()
        if (combined.isBlank()) return null
        val lower = combined.lowercase()
        val normAr = normalizeQuranArabic(combined)

        for (surah in QuranSurahCatalog.all114Surahs) {
            val bn = surah.nameBn.lowercase()
            val en = surah.nameEn.lowercase()
            val enNoPrefix = en.removePrefix("al-").removePrefix("an-").removePrefix("as-")
                .removePrefix("at-").removePrefix("az-").removePrefix("ar-").removePrefix("ash-")
            val arNorm = normalizeQuranArabic(surah.nameAr)
            if ((bn.length >= 3 && lower.contains(bn)) ||
                (en.length >= 4 && lower.contains(en)) ||
                (enNoPrefix.length >= 4 && lower.contains(enNoPrefix)) ||
                (arNorm.length >= 3 && normAr.contains(arNorm))
            ) {
                return surah.number
            }
        }
        return null
    }

    private fun extractVisibleAyahNumbers(text: String): List<Int> {
        if (text.isBlank()) return emptyList()
        val ascii = normalizeDigitsToAscii(text)
        val nums = LinkedHashSet<Int>()
        // Bracketed or ornate Quranic markers like ﴿٢٢﴾, (22), [22]
        Regex("""[﴿\(\[\{]\s*(\d{1,3})\s*[﴾\)\]\}]""").findAll(ascii).forEach { m ->
            m.groupValues[1].toIntOrNull()?.takeIf { it in 1..286 }?.let { nums.add(it) }
        }
        return nums.toList()
    }

    private fun computeConfidencePercent(score: Double): Int {
        return when {
            score >= 1350.0 -> 99
            score >= 1150.0 -> 97
            score >= 950.0 -> 94
            score >= 780.0 -> 90
            score >= 620.0 -> 85
            score >= 460.0 -> 79
            score >= 320.0 -> 72
            else -> 65
        }
    }
}
