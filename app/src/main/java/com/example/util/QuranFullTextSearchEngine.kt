package com.example.util

import android.content.Context
import android.util.JsonReader
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.model.QuranAyah
import com.example.data.model.QuranSurah
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.text.Normalizer

enum class QuranSearchMatchType(val labelBn: String) {
    DIRECT_REFERENCE("সরাসরি আয়াত রেফারেন্স"),
    EXACT_PHRASE("হুবহু বাক্যাংশ"),
    ALL_WORDS("সব শব্দ মিলেছে"),
    SMART_TOPIC("সম্পর্কিত বিষয় ও শব্দ"),
    ARABIC_OR_PRONUNCIATION("আরবি / উচ্চারণ"),
    TAFSIR_MATCH("তাফসীর থেকে প্রাপ্ত"),
    PARTIAL_MATCH("আংশিক মিল")
}

enum class QuranSearchSortMode(val labelBn: String) {
    RELEVANCE("প্রাসঙ্গিকতা"),
    QURAN_ORDER("কুরআন ক্রম (১-১১৪)")
}

enum class QuranSearchRevelationFilter(val labelBn: String) {
    ALL("সব (১১৪)"),
    MAKKI("মাক্কী"),
    MADANI("মাদানী")
}

data class QuranSearchIndexState(
    val isReady: Boolean = false,
    val isIndexing: Boolean = false,
    val indexedSurahs: Int = 0,
    val totalAyahs: Int = 0
)

data class IndexedQuranAyah(
    val surahNumber: Int,
    val ayahNumber: Int,
    val arabicText: String,
    val pronunciationBn: String,
    val displayTranslationBn: String,
    val normalizedBn: String,
    val canonicalBn: String,
    val normalizedPronunciation: String,
    val normalizedArabic: String
)

data class QuranAyahSearchHit(
    val surah: QuranSurah,
    val ayah: QuranAyah,
    val score: Int,
    val matchType: QuranSearchMatchType,
    val matchedTerms: List<String>,
    val tafsirMatchExcerpt: String? = null
)

data class SurahMatchSummary(
    val surahNumber: Int,
    val surahNameBn: String,
    val matchCount: Int
)

data class QuranFullSearchResult(
    val query: String,
    val hits: List<QuranAyahSearchHit>,
    val totalMatches: Int,
    val matchedSurahCount: Int,
    val surahDistribution: List<SurahMatchSummary>,
    val expandedTermsUsed: List<String>,
    val searchTimeMs: Long,
    val includedTafsir: Boolean
)

/**
 * High-speed, 100% offline full-Quran (114 Surahs / 6,236 Ayahs) Bangla search engine.
 * Features:
 * - Streaming JsonReader indexing across all 114 bundled Surah JSON assets (~300ms one-time build)
 * - Bengali Unicode NFC + ZWNJ/ZWJ + phonetic/orthographic canonicalization
 * - Extensive Bangla Islamic & Quranic synonym/topic thesaurus (e.g., নামাজ -> সালাত, রোজা -> সিয়াম)
 * - Direct Surah:Ayah reference lookup (e.g., 2:255 or ২:২৫৫)
 * - Optional deep Tafsir streaming search across all 114 Surahs
 */
object QuranFullTextSearchEngine {

    private val indexMutex = Mutex()
    @Volatile
    private var cachedIndex: List<IndexedQuranAyah> = emptyList()

    private val _indexState = MutableStateFlow(QuranSearchIndexState())
    val indexState: StateFlow<QuranSearchIndexState> = _indexState.asStateFlow()

    private val surahMap: Map<Int, QuranSurah> by lazy {
        QuranSurahCatalog.all114Surahs.associateBy { it.number }
    }

    private val footnoteMarkerRegex = Regex("\\s*\\[[০-৯0-9]+\\]\\s*")
    private val leadingAyahNumRegex = Regex("^[০-৯0-9]+[.)।\\s]+")
    private val htmlTagRegex = Regex("<[^>]+>")
    private val multiSpaceRegex = Regex("\\s+")
    private val arabicDiacriticsRegex = Regex("[\u064B-\u065F\u0670\u06D6-\u06ED]")
    private val referenceRegex = Regex("^([০-৯0-9]{1,3})\\s*[:\\-/।.]\\s*([০-৯0-9]{1,3})$")

    /**
     * Curated Bangla Quranic topic & synonym map grounded in Dr. Abu Bakr Zakaria's Bengali translation.
     * Ensures natural Bangla queries immediately discover all relevant Quranic verses even when
     * the translation uses classical Islamic terminology.
     */
    private val banglaQuranSynonyms: Map<String, List<String>> = mapOf(
        // Worship & Pillars
        "নামাজ" to listOf("সালাত", "সাজদা", "রুকু"),
        "নামায" to listOf("সালাত", "সাজদা", "রুকু"),
        "সালাত" to listOf("সালাত", "নামাজ", "সাজদা"),
        "ছালাত" to listOf("সালাত"),
        "রোজা" to listOf("সিয়াম", "রোযা"),
        "রোযা" to listOf("সিয়াম", "রোজা"),
        "সিয়াম" to listOf("সিয়াম", "রোজা"),
        "রমজান" to listOf("রমাদান", "সিয়াম"),
        "রমাদান" to listOf("রমাদান", "সিয়াম"),
        "জাকাত" to listOf("যাকাত", "সদকা", "দান"),
        "যাকাত" to listOf("যাকাত", "সদকা"),
        "হজ্জ" to listOf("হজ", "কাবা", "বায়তুল্লাহ", "ইহরাম"),
        "হজ" to listOf("হজ", "কাবা", "উমরাহ"),
        "ওযু" to listOf("পবিত্রতা", "মুখমণ্ডল", "তাইয়াম্মুম"),
        "অযু" to listOf("পবিত্রতা", "মুখমণ্ডল", "তাইয়াম্মুম"),
        "সিজদা" to listOf("সাজদা", "সিজদা"),
        "সেজদা" to listOf("সাজদা", "সিজদা"),
        "দোয়া" to listOf("প্রার্থনা", "ডাক", "আহ্বান"),
        "দোয়া" to listOf("প্রার্থনা", "ডাক", "আহ্বান"),
        "দুআ" to listOf("প্রার্থনা", "ডাক"),
        "মোনাজাত" to listOf("প্রার্থনা", "ডাক"),
        "জিকির" to listOf("স্মরণ", "তাসবীহ", "পবিত্রতা"),
        "যিকির" to listOf("স্মরণ", "তাসবীহ"),
        "কোরআন" to listOf("কুরআন", "কিতাব", "ফুরকান"),
        "কুরআন" to listOf("কুরআন", "কিতাব"),
        "কোরান" to listOf("কুরআন"),
        "তওবা" to listOf("তাওবা", "ক্ষমা", "অনুতপ্ত"),
        "তাওবা" to listOf("তাওবা", "ক্ষমা"),
        "মাফ" to listOf("ক্ষমা", "তাওবা", "মাগফিরাত"),
        "ইস্তেগফার" to listOf("ক্ষমা", "তাওবা"),
        "দান" to listOf("দান", "সদকা", "ব্যয়", "যাকাত"),
        "সদকা" to listOf("সদকা", "দান", "ব্যয়"),
        "কুরবানী" to listOf("কুরবানী", "পশু", "উৎসর্গ"),
        "কোরবানি" to listOf("কুরবানী", "পশু", "উৎসর্গ"),

        // Heart, Emotion & Spirituality
        "সবর" to listOf("ধৈর্য", "ধৈর্যশীল"),
        "ধৈর্য" to listOf("ধৈর্য", "ধৈর্যশীল", "সবর"),
        "ধৈর্য্য" to listOf("ধৈর্য", "ধৈর্যশীল"),
        "তাওয়াক্কুল" to listOf("নির্ভর", "ভরসা", "তাওয়াক্কুল"),
        "ভরসা" to listOf("নির্ভর", "তাওয়াক্কুল", "ভরসা"),
        "শোকর" to listOf("কৃতজ্ঞতা", "কৃতজ্ঞ", "শোকর"),
        "শুকরিয়া" to listOf("কৃতজ্ঞতা", "কৃতজ্ঞ"),
        "কৃতজ্ঞতা" to listOf("কৃতজ্ঞতা", "কৃতজ্ঞ", "শোকর"),
        "হতাশা" to listOf("নিরাশ", "হতাশ", "দুশ্চিন্তা", "ভয়"),
        "হতাশ" to listOf("নিরাশ", "হতাশ", "দুঃখ"),
        "দুশ্চিন্তা" to listOf("ভয়", "চিন্তিত", "দুঃখ", "প্রশান্তি"),
        "কষ্ট" to listOf("কষ্ট", "দুঃখ", "বিপদ", "পরীক্ষা"),
        "শান্তি" to listOf("শান্তি", "প্রশান্তি", "নিরাপত্তা"),
        "প্রশান্তি" to listOf("প্রশান্তি", "শান্তি", "অন্তর"),
        "রাগ" to listOf("ক্রোধ", "রাগ", "ক্ষমা"),
        "ক্রোধ" to listOf("ক্রোধ", "রাগ"),
        "ভয়" to listOf("ভয়", "তাকওয়া", "ভীত"),
        "তাকওয়া" to listOf("তাকওয়া", "মুত্তাকী", "সাবধান"),
        "ইখলাস" to listOf("একনিষ্ঠ", "খাঁটি", "আন্তরিক"),
        "রহমত" to listOf("রহমত", "দয়া", "অনুগ্রহ", "করুণা"),
        "দয়া" to listOf("দয়া", "রহমত", "করুণা"),
        "হেদায়েত" to listOf("হেদায়াত", "সৎপথ", "সঠিক পথ"),
        "হিদায়াত" to listOf("হেদায়াত", "সৎপথ", "সঠিক পথ"),
        "শিফা" to listOf("আরোগ্য", "রোগ", "নিরাময়"),
        "রোগ" to listOf("রোগ", "আরোগ্য", "ব্যাধি"),
        "অসুস্থ" to listOf("অসুস্থ", "রোগ", "আরোগ্য"),

        // Family, Society & Ethics
        "পিতামাতা" to listOf("পিতা-মাতা", "মাতা-পিতা", "পিতা", "মাতা"),
        "মা-বাবা" to listOf("পিতা-মাতা", "মাতা-পিতা", "পিতা", "মাতা"),
        "মা" to listOf("মাতা", "পিতা-মাতা", "জননী"),
        "বাবা" to listOf("পিতা", "পিতা-মাতা"),
        "বিবাহ" to listOf("বিয়ে", "বিবাহ", "স্ত্রী", "স্বামী"),
        "বিয়ে" to listOf("বিয়ে", "বিবাহ", "স্ত্রী", "স্বামী"),
        "তালাক" to listOf("তালাক", "বিচ্ছেদ", "স্ত্রী"),
        "পরিবার" to listOf("পরিবার", "স্ত্রী", "সন্তান", "পিতা-মাতা"),
        "এতিম" to listOf("ইয়াতীম", "এতিম"),
        "ইয়াতিম" to listOf("ইয়াতীম"),
        "প্রতিবেশী" to listOf("প্রতিবেশী", "আত্মীয়"),
        "হিজাব" to listOf("পর্দা", "চাদর", "দৃষ্টি", "সৌন্দর্য"),
        "পর্দা" to listOf("পর্দা", "চাদর", "দৃষ্টি"),
        "গীবত" to listOf("গীবত", "পরনিন্দা", "দোষ"),
        "পরনিন্দা" to listOf("গীবত", "নিন্দা", "অপবাদ"),
        "হিংসা" to listOf("হিংসা", "বিদ্বেষ"),
        "অহংকার" to listOf("অহংকার", "দম্ভ", "অহংকারী"),
        "মিথ্যা" to listOf("মিথ্যা", "মিথ্যাবাদী"),
        "জুলুম" to listOf("যুলুম", "যালিম", "অত্যাচার"),
        "অত্যাচার" to listOf("যুলুম", "যালিম", "অত্যাচার"),
        "ন্যায়বিচার" to listOf("ন্যায়", "ইনসাফ", "সুবিচার"),
        "ইনসাফ" to listOf("ইনসাফ", "ন্যায়"),
        "সত্য" to listOf("সত্য", "সত্যবাদী"),
        "আমানত" to listOf("আমানত", "অঙ্গীকার", "প্রতিশ্রুতি"),
        "ওয়াদা" to listOf("প্রতিশ্রুতি", "অঙ্গীকার", "ওয়াদা"),

        // Sustenance, Economy & Law
        "রিজিক" to listOf("রিযিক", "রিয্‌ক", "জীবিকা", "উপজীবিকা"),
        "রিযিক" to listOf("রিযিক", "জীবিকা"),
        "জীবিকা" to listOf("জীবিকা", "রিযিক"),
        "সুদ" to listOf("সুদ", "রিবা"),
        "রিবা" to listOf("সুদ", "রিবা"),
        "ব্যবসা" to listOf("ব্যবসা", "ক্রয়-বিক্রয়", "বাণিজ্য"),
        "মিরাস" to listOf("উত্তরাধিকার", "সম্পত্তি", "অংশ"),
        "উত্তরাধিকার" to listOf("উত্তরাধিকার", "ওয়ারিশ"),
        "মদ" to listOf("মদ", "নেশা", "জুয়া"),
        "জুয়া" to listOf("জুয়া", "মদ"),
        "ব্যভিচার" to listOf("ব্যভিচার", "অশ্লীলতা", "যিনা"),
        "জিনা" to listOf("ব্যভিচার", "অশ্লীলতা"),
        "চুরি" to listOf("চুরি", "চোর"),
        "হত্যা" to listOf("হত্যা", "প্রাণ"),
        "ঘুষ" to listOf("অন্যায়ভাবে", "সম্পদ", "গ্রাস"),
        "জ্ঞান" to listOf("জ্ঞান", "জ্ঞানী", "শিক্ষা", "প্রজ্ঞা"),
        "ইলম" to listOf("জ্ঞান", "প্রজ্ঞা"),

        // Faith, Prophets & Hereafter
        "ধর্ম" to listOf("দীন", "ধর্ম", "ইসলাম"),
        "দ্বীন" to listOf("দীন", "ইসলাম"),
        "আখিরাত" to listOf("আখিরাত", "পরকাল", "কিয়ামত"),
        "পরকাল" to listOf("আখিরাত", "কিয়ামত"),
        "বেহেশত" to listOf("জান্নাত", "উদ্যান"),
        "জান্নাত" to listOf("জান্নাত", "উদ্যান"),
        "দোজখ" to listOf("জাহান্নাম", "আগুন"),
        "দোযখ" to listOf("জাহান্নাম", "আগুন"),
        "জাহান্নাম" to listOf("জাহান্নাম", "আগুন", "জাহীম"),
        "কেয়ামত" to listOf("কিয়ামত", "পুনরুত্থান", "শেষ দিবস"),
        "কিয়ামত" to listOf("কিয়ামত", "পুনরুত্থান"),
        "মৃত্যু" to listOf("মৃত্যু", "মরণ", "প্রাণ"),
        "কবর" to listOf("কবর", "মৃত"),
        "জ্বিন" to listOf("জিন"),
        "জিন" to listOf("জিন"),
        "শয়তান" to listOf("শয়তান", "ইবলীস"),
        "ফেরেশতা" to listOf("ফেরেশতা", "জিবরীল"),
        "নবী" to listOf("নবী", "রাসূল"),
        "রসূল" to listOf("রাসূল", "নবী"),
        "মুসা" to listOf("মূসা", "মুসা"),
        "ইব্রাহিম" to listOf("ইবরাহীম", "ইব্রাহিম"),
        "ইসা" to listOf("ঈসা"),
        "নুহ" to listOf("নূহ"),
        "আদম" to listOf("আদম"),
        "ইউসুফ" to listOf("ইউসুফ"),
        "দাউদ" to listOf("দাউদ", "দাঊদ"),
        "সুলাইমান" to listOf("সুলাইমান", "সুলায়মান"),
        "মরিয়ম" to listOf("মারইয়াম", "মারয়াম"),
        "ফিরাউন" to listOf("ফিরআউন", "ফেরাউন"),
        "আয়াতুল কুরসি" to listOf("আল্লাহ, তিনি ছাড়া কোনো সত্য ইলাহ নেই, তিনি চিরঞ্জীব")
    )

    /**
     * Normalizes Bengali Unicode to NFC, removes ZWNJ/ZWJ, strips footnote markers, and collapses whitespace.
     */
    fun normalizeBangla(input: String): String {
        if (input.isEmpty()) return ""
        val nfc = Normalizer.normalize(input, Normalizer.Form.NFC)
        return nfc
            .replace("\u200C", "")
            .replace("\u200D", "")
            .replace("\uFEFF", "")
            .replace("‘", "")
            .replace("’", "")
            .replace("'", "")
            .replace("্ ", " ")
            .lowercase()
            .replace(multiSpaceRegex, " ")
            .trim()
    }

    /**
     * Phonetic/orthographic canonical form for resilient Bangla matching:
     * unifies long/short vowels (ী->ি, ূ->ু), জ/য, স/শ/ষ, ন/ণ, trailing hasanta (আল্লাহ্ -> আল্লাহ), etc.
     */
    fun canonicalizeBangla(input: String): String {
        val norm = normalizeBangla(input)
        val sb = StringBuilder(norm.length)
        for (i in norm.indices) {
            val c = norm[i]
            when (c) {
                'ী' -> sb.append('ি')
                'ূ' -> sb.append('ু')
                'ঈ' -> sb.append('ই')
                'ঊ' -> sb.append('উ')
                'য' -> sb.append('জ')
                'ণ' -> sb.append('ন')
                'শ', 'ষ' -> sb.append('স')
                'ঁ' -> {} // optional chandrabindu
                '্' -> {
                    // Keep conjunct hasanta only when followed by another Bengali consonant
                    val next = norm.getOrNull(i + 1)
                    if (next != null && next in '\u0995'..'\u09B9') {
                        sb.append(c)
                    }
                }
                '—', '–', '-', ',', '।', ';', ':', '?', '!', '(', ')', '"', '“', '”' -> sb.append(' ')
                else -> sb.append(c)
            }
        }
        return sb.toString().replace(multiSpaceRegex, " ").trim()
    }

    fun normalizeArabic(input: String): String {
        if (input.isEmpty()) return ""
        return input
            .replace(arabicDiacriticsRegex, "")
            .replace('أ', 'ا')
            .replace('إ', 'ا')
            .replace('آ', 'ا')
            .replace('ٱ', 'ا')
            .replace('ة', 'ه')
            .replace('ى', 'ي')
            .trim()
    }

    fun cleanTranslationDisplay(raw: String): String {
        val noFootnote = footnoteMarkerRegex.replace(raw, " ")
        val noLead = leadingAyahNumRegex.replace(noFootnote.trim(), "")
        return noLead
            .replace(" ,", ",")
            .replace(" ।", "।")
            .replace(multiSpaceRegex, " ")
            .trim()
    }

    /**
     * Ensures all 114 Surahs (6,236 Ayahs) are indexed in memory using fast streaming JsonReader.
     * Safe to call multiple times; builds only once per process lifecycle.
     */
    suspend fun ensureIndexed(context: Context) {
        if (cachedIndex.isNotEmpty() && _indexState.value.isReady) return

        indexMutex.withLock {
            if (cachedIndex.isNotEmpty() && _indexState.value.isReady) return

            _indexState.value = QuranSearchIndexState(
                isReady = false,
                isIndexing = true,
                indexedSurahs = 0,
                totalAyahs = 0
            )

            withContext(Dispatchers.IO) {
                val appContext = context.applicationContext
                val result = ArrayList<IndexedQuranAyah>(6240)

                for (surahNum in 1..114) {
                    try {
                        appContext.assets.open("quran/surah_$surahNum.json").use { stream ->
                            JsonReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                                reader.beginArray()
                                var fallbackAyah = 1
                                while (reader.hasNext()) {
                                    var ayaNum = fallbackAyah
                                    var ar = ""
                                    var pr = ""
                                    var bn = ""

                                    reader.beginObject()
                                    while (reader.hasNext()) {
                                        when (reader.nextName()) {
                                            "aya" -> ayaNum = reader.nextInt()
                                            "ar" -> ar = reader.nextString()
                                            "pr" -> pr = reader.nextString()
                                            "bn" -> bn = reader.nextString()
                                            else -> reader.skipValue() // Skip huge "tf" field for instant indexing
                                        }
                                    }
                                    reader.endObject()

                                    val cleanBn = cleanTranslationDisplay(bn)
                                    val cleanPr = if (pr.isNotBlank()) {
                                        QuranBengaliPhoneticTransliteration.cleanPronunciationText(pr)
                                    } else {
                                        ""
                                    }

                                    result.add(
                                        IndexedQuranAyah(
                                            surahNumber = surahNum,
                                            ayahNumber = ayaNum,
                                            arabicText = ar,
                                            pronunciationBn = cleanPr,
                                            displayTranslationBn = cleanBn,
                                            normalizedBn = normalizeBangla(cleanBn),
                                            canonicalBn = canonicalizeBangla(cleanBn),
                                            normalizedPronunciation = canonicalizeBangla(cleanPr),
                                            normalizedArabic = normalizeArabic(ar)
                                        )
                                    )
                                    fallbackAyah++
                                }
                                reader.endArray()
                            }
                        }
                    } catch (_: Exception) {
                        // Continue gracefully if any single asset fails
                    }

                    if (surahNum % 8 == 0 || surahNum == 114) {
                        _indexState.value = QuranSearchIndexState(
                            isReady = surahNum == 114,
                            isIndexing = surahNum < 114,
                            indexedSurahs = surahNum,
                            totalAyahs = result.size
                        )
                    }
                }

                cachedIndex = result
                _indexState.value = QuranSearchIndexState(
                    isReady = true,
                    isIndexing = false,
                    indexedSurahs = 114,
                    totalAyahs = result.size
                )
            }
        }
    }

    /**
     * Immediately searches all 114 Surahs (6,236 Ayahs) for any Bangla topic, word, phrase,
     * pronunciation, Arabic text, or Surah:Ayah reference.
     */
    suspend fun search(
        context: Context,
        rawQuery: String,
        revelationFilter: QuranSearchRevelationFilter = QuranSearchRevelationFilter.ALL,
        surahFilterNumber: Int? = null,
        sortMode: QuranSearchSortMode = QuranSearchSortMode.RELEVANCE,
        includeTafsir: Boolean = false
    ): QuranFullSearchResult = withContext(Dispatchers.Default) {
        val startTime = System.currentTimeMillis()
        val trimmed = rawQuery.trim()
        if (trimmed.isEmpty()) {
            return@withContext QuranFullSearchResult(
                query = "",
                hits = emptyList(),
                totalMatches = 0,
                matchedSurahCount = 0,
                surahDistribution = emptyList(),
                expandedTermsUsed = emptyList(),
                searchTimeMs = 0L,
                includedTafsir = includeTafsir
            )
        }

        ensureIndexed(context)
        val index = cachedIndex

        val normalizedQuery = normalizeBangla(trimmed)
        val canonicalQuery = canonicalizeBangla(trimmed)
        val normalizedArabicQuery = normalizeArabic(trimmed)

        // Check direct reference (e.g., 2:255 or ২:২৫৫)
        val directRef = parseDirectReference(trimmed)

        // Split query into meaningful words
        val rawTokens = normalizedQuery.split(" ").filter { it.length >= 2 }
        val canonicalTokens = rawTokens.map { canonicalizeBangla(it) }.filter { it.length >= 2 }

        // Expand synonyms for whole query and individual tokens
        val expandedSet = LinkedHashSet<String>()
        findSynonymsFor(normalizedQuery, canonicalQuery).forEach { expandedSet.add(it) }
        rawTokens.forEach { tok ->
            findSynonymsFor(tok, canonicalizeBangla(tok)).forEach { expandedSet.add(it) }
        }
        // Remove the raw query itself from "expanded" display list
        val expandedSynonymsNormalized = expandedSet
            .map { normalizeBangla(it) }
            .filter { it.isNotBlank() && it != normalizedQuery }
            .distinct()
        val expandedSynonymsCanonical = expandedSynonymsNormalized.map { it to canonicalizeBangla(it) }

        val usedSynonyms = LinkedHashSet<String>()
        val rawHits = ArrayList<QuranAyahSearchHit>(128)
        val matchedKeys = HashSet<Long>(256)

        for (item in index) {
            val surah = surahMap[item.surahNumber] ?: continue

            // Revelation filter
            if (revelationFilter == QuranSearchRevelationFilter.MAKKI && !surah.revelationType.contains("মাক্কী")) {
                continue
            }
            if (revelationFilter == QuranSearchRevelationFilter.MADANI && !surah.revelationType.contains("মাদানী")) {
                continue
            }

            var score = 0
            var matchType: QuranSearchMatchType? = null
            val matchedTerms = ArrayList<String>(4)

            // 1. Direct Surah:Ayah reference match
            if (directRef != null && item.surahNumber == directRef.first && item.ayahNumber == directRef.second) {
                score += 1200
                matchType = QuranSearchMatchType.DIRECT_REFERENCE
                matchedTerms.add("${surah.nameBn} ${directRef.first}:${directRef.second}")
            }

            // 2. Exact Bangla phrase in normalized translation
            if (normalizedQuery.length >= 2 && item.normalizedBn.contains(normalizedQuery)) {
                val occurrences = countOccurrences(item.normalizedBn, normalizedQuery)
                score += 520 + (occurrences.coerceAtMost(5) * 35)
                if (matchType == null) matchType = QuranSearchMatchType.EXACT_PHRASE
                matchedTerms.add(trimmed)
            } else if (canonicalQuery.length >= 2 && item.canonicalBn.contains(canonicalQuery)) {
                // Canonical orthographic/phonetic exact phrase match (e.g., কোরআন -> কুরআন, মুসা -> মূসা)
                score += 420
                if (matchType == null) matchType = QuranSearchMatchType.EXACT_PHRASE
                matchedTerms.add(trimmed)
            }

            // 3. Multi-word matching (all words present in translation)
            if (canonicalTokens.size >= 2) {
                var wordsMatched = 0
                for (i in canonicalTokens.indices) {
                    val ct = canonicalTokens[i]
                    if (item.canonicalBn.contains(ct)) {
                        wordsMatched++
                        matchedTerms.add(rawTokens[i])
                    }
                }
                if (wordsMatched == canonicalTokens.size) {
                    score += 360
                    if (matchType == null) matchType = QuranSearchMatchType.ALL_WORDS
                } else if (wordsMatched > 0 && score == 0) {
                    // Check if remaining missing tokens are satisfied via synonyms
                    var synCovered = 0
                    for (i in rawTokens.indices) {
                        val rt = rawTokens[i]
                        val ct = canonicalizeBangla(rt)
                        if (!item.canonicalBn.contains(ct)) {
                            val tokenSyns = findSynonymsFor(rt, ct)
                            val hitSyn = tokenSyns.firstOrNull { s ->
                                val cs = canonicalizeBangla(s)
                                cs.length >= 2 && item.canonicalBn.contains(cs)
                            }
                            if (hitSyn != null) {
                                synCovered++
                                matchedTerms.add(hitSyn)
                                usedSynonyms.add(hitSyn)
                            }
                        }
                    }
                    if (wordsMatched + synCovered == canonicalTokens.size) {
                        score += 290
                        if (matchType == null) matchType = QuranSearchMatchType.SMART_TOPIC
                    } else if (canonicalTokens.size <= 3 && wordsMatched >= canonicalTokens.size - 1 && wordsMatched >= 2) {
                        score += 120 + wordsMatched * 25
                        if (matchType == null) matchType = QuranSearchMatchType.PARTIAL_MATCH
                    }
                }
            }

            // 4. Smart Bangla Quranic Synonym / Topic match
            if (expandedSynonymsCanonical.isNotEmpty()) {
                for ((synDisplay, synCanon) in expandedSynonymsCanonical) {
                    if (synCanon.length >= 2 && item.canonicalBn.contains(synCanon)) {
                        score += 240
                        if (matchType == null) matchType = QuranSearchMatchType.SMART_TOPIC
                        matchedTerms.add(synDisplay)
                        usedSynonyms.add(synDisplay)
                        break
                    }
                }
            }

            // 5. Arabic script or Bengali pronunciation match
            if (score == 0) {
                val hasArabicChars = normalizedArabicQuery.any { it in '\u0600'..'\u06FF' }
                if (hasArabicChars && normalizedArabicQuery.length >= 2 && item.normalizedArabic.contains(normalizedArabicQuery)) {
                    score += 310
                    matchType = QuranSearchMatchType.ARABIC_OR_PRONUNCIATION
                    matchedTerms.add(trimmed)
                } else if (canonicalQuery.length >= 3 && item.normalizedPronunciation.contains(canonicalQuery)) {
                    score += 260
                    matchType = QuranSearchMatchType.ARABIC_OR_PRONUNCIATION
                    matchedTerms.add(trimmed)
                }
            }

            if (score > 0 && matchType != null) {
                val key = (item.surahNumber.toLong() shl 16) or item.ayahNumber.toLong()
                matchedKeys.add(key)
                rawHits.add(
                    QuranAyahSearchHit(
                        surah = surah,
                        ayah = QuranAyah(
                            surahNumber = item.surahNumber,
                            ayahNumber = item.ayahNumber,
                            arabicText = item.arabicText,
                            pronunciationBn = item.pronunciationBn,
                            translationBn = item.displayTranslationBn,
                            translationZakaria = item.displayTranslationBn,
                            tafsirText = null
                        ),
                        score = score,
                        matchType = matchType,
                        matchedTerms = matchedTerms.distinct()
                    )
                )
            }
        }

        // 6. Optional Deep Tafsir Search across all 114 Surahs (streams JSON directly to keep RAM low)
        if (includeTafsir && normalizedQuery.length >= 2) {
            val tafsirHits = searchInTafsirAssets(
                context = context,
                normalizedQuery = normalizedQuery,
                canonicalQuery = canonicalQuery,
                expandedSynonyms = expandedSynonymsCanonical.map { it.first },
                revelationFilter = revelationFilter,
                alreadyMatchedKeys = matchedKeys,
                maxAdditionalHits = 80
            )
            rawHits.addAll(tafsirHits)
        }

        // Compute Surah distribution BEFORE applying specific Surah filter chip
        val distribution = rawHits
            .groupingBy { it.surah.number }
            .eachCount()
            .entries
            .mapNotNull { (sNum, count) ->
                val s = surahMap[sNum] ?: return@mapNotNull null
                SurahMatchSummary(sNum, s.nameBn, count)
            }
            .sortedWith(compareByDescending<SurahMatchSummary> { it.matchCount }.thenBy { it.surahNumber })

        val filteredHits = if (surahFilterNumber != null) {
            rawHits.filter { it.surah.number == surahFilterNumber }
        } else {
            rawHits
        }

        val sortedHits = when (sortMode) {
            QuranSearchSortMode.RELEVANCE -> filteredHits.sortedWith(
                compareByDescending<QuranAyahSearchHit> { it.score }
                    .thenBy { it.surah.number }
                    .thenBy { it.ayah.ayahNumber }
            )
            QuranSearchSortMode.QURAN_ORDER -> filteredHits.sortedWith(
                compareBy<QuranAyahSearchHit> { it.surah.number }
                    .thenBy { it.ayah.ayahNumber }
            )
        }

        QuranFullSearchResult(
            query = trimmed,
            hits = sortedHits,
            totalMatches = rawHits.size,
            matchedSurahCount = distribution.size,
            surahDistribution = distribution,
            expandedTermsUsed = usedSynonyms.toList(),
            searchTimeMs = (System.currentTimeMillis() - startTime).coerceAtLeast(1L),
            includedTafsir = includeTafsir
        )
    }

    /**
     * Loads full authentic Tafsir (Dr. Abu Bakr Zakaria) for a single Ayah on demand from assets.
     */
    suspend fun loadFullTafsir(
        context: Context,
        surahNumber: Int,
        ayahNumber: Int
    ): String = withContext(Dispatchers.IO) {
        try {
            context.applicationContext.assets.open("quran/surah_$surahNumber.json").use { stream ->
                JsonReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                    reader.beginArray()
                    var idx = 1
                    while (reader.hasNext()) {
                        var aya = idx
                        var tf = ""
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "aya" -> aya = reader.nextInt()
                                "tf" -> tf = reader.nextString()
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                        if (aya == ayahNumber) {
                            return@withContext cleanHtmlTafsir(tf)
                        }
                        idx++
                    }
                    reader.endArray()
                }
            }
        } catch (_: Exception) {
        }
        "এই আয়াতের বিস্তারিত তাফসীর ও ব্যাখ্যার জন্য 'কুরআনে পড়ুন' বাটনে ট্যাপ করুন।"
    }

    private suspend fun searchInTafsirAssets(
        context: Context,
        normalizedQuery: String,
        canonicalQuery: String,
        expandedSynonyms: List<String>,
        revelationFilter: QuranSearchRevelationFilter,
        alreadyMatchedKeys: Set<Long>,
        maxAdditionalHits: Int
    ): List<QuranAyahSearchHit> = withContext(Dispatchers.IO) {
        val results = ArrayList<QuranAyahSearchHit>()
        val appContext = context.applicationContext
        val searchTerms = (listOf(normalizedQuery) + expandedSynonyms.take(2)).distinct()

        for (surahNum in 1..114) {
            if (results.size >= maxAdditionalHits) break
            val surah = surahMap[surahNum] ?: continue
            if (revelationFilter == QuranSearchRevelationFilter.MAKKI && !surah.revelationType.contains("মাক্কী")) continue
            if (revelationFilter == QuranSearchRevelationFilter.MADANI && !surah.revelationType.contains("মাদানী")) continue

            try {
                appContext.assets.open("quran/surah_$surahNum.json").use { stream ->
                    JsonReader(InputStreamReader(stream, Charsets.UTF_8)).use { reader ->
                        reader.beginArray()
                        var idx = 1
                        while (reader.hasNext() && results.size < maxAdditionalHits) {
                            var ayaNum = idx
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
                            idx++

                            val key = (surahNum.toLong() shl 16) or ayaNum.toLong()
                            if (key in alreadyMatchedKeys || tf.isBlank()) continue

                            val cleanTf = cleanHtmlTafsir(tf)
                            val normTf = normalizeBangla(cleanTf)

                            var matchedTerm: String? = null
                            var hitPos = -1
                            for (term in searchTerms) {
                                val p = normTf.indexOf(term)
                                if (p >= 0) {
                                    matchedTerm = term
                                    hitPos = p
                                    break
                                }
                            }

                            if (matchedTerm != null && hitPos >= 0) {
                                val start = (hitPos - 60).coerceAtLeast(0)
                                val end = (hitPos + 140).coerceAtMost(cleanTf.length)
                                val prefix = if (start > 0) "…" else ""
                                val suffix = if (end < cleanTf.length) "…" else ""
                                val excerpt = prefix + cleanTf.substring(start, end).trim() + suffix

                                val cleanBn = cleanTranslationDisplay(bn)
                                results.add(
                                    QuranAyahSearchHit(
                                        surah = surah,
                                        ayah = QuranAyah(
                                            surahNumber = surahNum,
                                            ayahNumber = ayaNum,
                                            arabicText = ar,
                                            pronunciationBn = QuranBengaliPhoneticTransliteration.cleanPronunciationText(pr),
                                            translationBn = cleanBn,
                                            translationZakaria = cleanBn,
                                            tafsirText = cleanTf
                                        ),
                                        score = 160,
                                        matchType = QuranSearchMatchType.TAFSIR_MATCH,
                                        matchedTerms = listOf(matchedTerm),
                                        tafsirMatchExcerpt = excerpt
                                    )
                                )
                            }
                        }
                        reader.endArray()
                    }
                }
            } catch (_: Exception) {
            }
        }
        results
    }

    private fun cleanHtmlTafsir(raw: String): String {
        if (raw.isBlank()) {
            return "এই আয়াতে মহান আল্লাহর হেদায়েত ও নির্দেশনা বর্ণিত হয়েছে।"
        }
        return htmlTagRegex.replace(raw, "")
            .replace("&nbsp;", " ")
            .replace("&quot;", "\"")
            .replace("&amp;", "&")
            .replace("\r\n", "\n")
            .replace(Regex("\n{3,}"), "\n\n")
            .trim()
    }

    private fun findSynonymsFor(normalizedTerm: String, canonicalTerm: String): List<String> {
        val matches = ArrayList<String>()
        banglaQuranSynonyms[normalizedTerm]?.let { matches.addAll(it) }
        if (matches.isEmpty()) {
            for ((key, values) in banglaQuranSynonyms) {
                if (canonicalizeBangla(key) == canonicalTerm) {
                    matches.addAll(values)
                    break
                }
            }
        }
        return matches
    }

    private fun parseDirectReference(query: String): Pair<Int, Int>? {
        val m = referenceRegex.matchEntire(query.trim()) ?: return null
        val s = banglaToAsciiDigits(m.groupValues[1]).toIntOrNull() ?: return null
        val a = banglaToAsciiDigits(m.groupValues[2]).toIntOrNull() ?: return null
        if (s in 1..114 && a >= 1) return s to a
        return null
    }

    private fun banglaToAsciiDigits(input: String): String {
        val sb = StringBuilder(input.length)
        for (c in input) {
            sb.append(
                when (c) {
                    '০' -> '0'
                    '১' -> '1'
                    '২' -> '2'
                    '৩' -> '3'
                    '৪' -> '4'
                    '৫' -> '5'
                    '৬' -> '6'
                    '৭' -> '7'
                    '৮' -> '8'
                    '৯' -> '9'
                    else -> c
                }
            )
        }
        return sb.toString()
    }

    private fun countOccurrences(haystack: String, needle: String): Int {
        if (needle.isEmpty()) return 0
        var count = 0
        var idx = haystack.indexOf(needle)
        while (idx >= 0) {
            count++
            idx = haystack.indexOf(needle, idx + needle.length)
        }
        return count
    }

    /**
     * Finds character highlight ranges inside [displayText] matching [query] or any [matchedTerms],
     * tolerating Bengali ZWNJ/ZWJ & NFC differences.
     */
    fun findHighlightRanges(
        displayText: String,
        query: String,
        matchedTerms: List<String>
    ): List<IntRange> {
        if (displayText.isEmpty()) return emptyList()

        // Build clean-to-original character index map (ignoring ZWNJ/ZWJ)
        val cleanChars = StringBuilder(displayText.length)
        val indexMap = IntArray(displayText.length + 1)
        for (i in displayText.indices) {
            val c = displayText[i]
            if (c != '\u200C' && c != '\u200D' && c != '\uFEFF') {
                indexMap[cleanChars.length] = i
                cleanChars.append(c.lowercaseChar())
            }
        }
        indexMap[cleanChars.length] = displayText.length
        val cleanStr = Normalizer.normalize(cleanChars.toString(), Normalizer.Form.NFC)

        val candidates = LinkedHashSet<String>()
        val normQuery = normalizeBangla(query)
        if (normQuery.length >= 2) candidates.add(normQuery)
        normQuery.split(" ").filter { it.length >= 2 }.forEach { candidates.add(it) }
        matchedTerms.map { normalizeBangla(it) }.filter { it.length >= 2 }.forEach { candidates.add(it) }

        val ranges = ArrayList<IntRange>()
        // Note: if NFC changed length relative to cleanChars, fallback to direct case-insensitive matching
        val useIndexMap = cleanStr.length == cleanChars.length

        for (term in candidates) {
            val target = if (useIndexMap) cleanStr else displayText.lowercase()
            var startIdx = target.indexOf(term)
            while (startIdx >= 0) {
                val endIdx = startIdx + term.length
                val mappedStart = if (useIndexMap) indexMap[startIdx] else startIdx
                val mappedEnd = if (useIndexMap) indexMap[endIdx.coerceAtMost(cleanChars.length)] else endIdx
                if (mappedEnd > mappedStart && mappedEnd <= displayText.length) {
                    ranges.add(mappedStart until mappedEnd)
                }
                startIdx = target.indexOf(term, endIdx)
            }
        }

        if (ranges.isEmpty()) return emptyList()
        // Merge overlapping ranges
        val sorted = ranges.sortedBy { it.first }
        val merged = ArrayList<IntRange>()
        var current = sorted[0]
        for (i in 1 until sorted.size) {
            val next = sorted[i]
            if (next.first <= current.last + 1) {
                current = current.first..maxOf(current.last, next.last)
            } else {
                merged.add(current)
                current = next
            }
        }
        merged.add(current)
        return merged
    }
}
