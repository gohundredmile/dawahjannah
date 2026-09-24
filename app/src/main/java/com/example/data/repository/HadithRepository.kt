package com.example.data.repository

import android.content.Context
import android.util.Log
import com.example.data.datasource.HadithCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.dao.HadithDao
import com.example.data.local.entity.HadithBookmarkEntity
import com.example.data.local.entity.HadithBookEntity
import com.example.data.local.entity.HadithChapterEntity
import com.example.data.local.entity.HadithEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

class HadithRepository(
    private val hadithDao: HadithDao,
    private val context: Context? = null
) {
    constructor(context: Context) : this(
        AppDatabase.getDatabase(context).hadithDao(),
        context.applicationContext
    )

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(18, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    companion object {
        private const val TAG = "HadithRepository"

        private val SLUG_TO_EDITION = mapOf(
            "bukhari" to "ben-bukhari",
            "muslim" to "ben-muslim",
            "tirmidhi" to "ben-tirmidhi",
            "abu-dawud" to "ben-abudawud",
            "nasai" to "ben-nasai",
            "ibn-majah" to "ben-ibnmajah",
            "forty-nawawi" to "ben-nawawi",
            "hadith-qudsi" to "qudsi"
        )

        private val SLUG_TO_ARABIC_EDITION = mapOf(
            "bukhari" to "ara-bukhari",
            "muslim" to "ara-muslim",
            "tirmidhi" to "ara-tirmidhi",
            "abu-dawud" to "ara-abudawud",
            "nasai" to "ara-nasai",
            "ibn-majah" to "ara-ibnmajah",
            "forty-nawawi" to "ara-nawawi"
        )
    }

    suspend fun initializeDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        try {
            // 1. Ensure all 10 books are present
            val existingBooksCount = hadithDao.getBooksCount()
            if (existingBooksCount < HadithCatalog.allBooks.size) {
                hadithDao.insertBooks(HadithCatalog.allBooks)
            }

            // 2. Ensure all 371 chapters across all books are populated from assets
            val existingChaptersCount = hadithDao.getChaptersCount()
            if (existingChaptersCount < 300) {
                val chaptersFromAssets = loadChaptersFromAssets()
                if (chaptersFromAssets.isNotEmpty()) {
                    hadithDao.insertChapters(chaptersFromAssets)
                } else {
                    hadithDao.insertChapters(HadithCatalog.initialChapters)
                }
            }

            // 3. Ensure offline initial pre-bundled hadiths are stored in Room DB
            val existingHadithsCount = hadithDao.getHadithsCount()
            if (existingHadithsCount < 50) {
                val hadithsFromAssets = loadHadithsFromAssets()
                if (hadithsFromAssets.isNotEmpty()) {
                    hadithDao.insertHadiths(hadithsFromAssets)
                }
                hadithDao.insertHadiths(HadithCatalog.initialHadiths)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing hadith database", e)
            // Fallback safety
            hadithDao.insertBooks(HadithCatalog.allBooks)
            hadithDao.insertChapters(HadithCatalog.initialChapters)
            hadithDao.insertHadiths(HadithCatalog.initialHadiths)
        }
    }

    private fun loadChaptersFromAssets(): List<HadithChapterEntity> {
        val ctx = context ?: return emptyList()
        return try {
            ctx.assets.open("hadith_chapters.json").use { stream ->
                val jsonStr = stream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonStr)
                val list = mutableListOf<HadithChapterEntity>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        HadithChapterEntity(
                            id = obj.getString("id"),
                            bookSlug = obj.getString("bookSlug"),
                            chapterNumber = obj.getInt("chapterNumber"),
                            titleBn = obj.getString("titleBn"),
                            titleAr = obj.optString("titleAr", ""),
                            hadithRange = obj.optString("hadithRange", "")
                        )
                    )
                }
                list
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load hadith_chapters.json from assets", e)
            emptyList()
        }
    }

    private fun loadHadithsFromAssets(): List<HadithEntity> {
        val ctx = context ?: return emptyList()
        return try {
            ctx.assets.open("hadith_initial.json").use { stream ->
                val jsonStr = stream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(jsonStr)
                val list = mutableListOf<HadithEntity>()
                for (i in 0 until jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    list.add(
                        HadithEntity(
                            id = obj.getString("id"),
                            bookSlug = obj.getString("bookSlug"),
                            hadithNumber = obj.getInt("hadithNumber"),
                            chapterNumber = obj.getInt("chapterNumber"),
                            chapterTitleBn = obj.getString("chapterTitleBn"),
                            narratorBn = obj.optString("narratorBn", ""),
                            arabicText = obj.optString("arabicText", ""),
                            banglaText = obj.getString("banglaText"),
                            englishText = obj.optString("englishText", ""),
                            gradeBn = obj.optString("gradeBn", "সহীহ"),
                            gradeColor = obj.optString("gradeColor", "SAHIH"),
                            sourceBn = obj.optString("sourceBn", "সহীহ হাদীস সম্ভার"),
                            explanationBn = obj.optString("explanationBn", ""),
                            hadithTypeBn = obj.optString("hadithTypeBn", "কওলী (বাণী)"),
                            isBookmarked = false
                        )
                    )
                }
                list
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load hadith_initial.json from assets", e)
            emptyList()
        }
    }

    fun getAllBooks(): Flow<List<HadithBookEntity>> = hadithDao.getAllBooks()

    fun getSihahSittaBooks(): Flow<List<HadithBookEntity>> = hadithDao.getSihahSittaBooks()

    fun getPrimaryCoreBooks(): Flow<List<HadithBookEntity>> = hadithDao.getPrimaryCoreBooks()

    fun getBookBySlug(slug: String): Flow<HadithBookEntity?> = hadithDao.getBookBySlug(slug)

    fun getChaptersForBook(bookSlug: String): Flow<List<HadithChapterEntity>> =
        hadithDao.getChaptersForBook(bookSlug)

    /**
     * Retrieves Hadiths for a specific chapter:
     * 1. First emits local items from the offline Room database.
     * 2. If Room DB is empty for this chapter, fetches the complete authentic section
     *    from the Hadith API / CDN and automatically saves it permanently into Room DB!
     */
    fun getHadithsForChapter(bookSlug: String, chapterNumber: Int): Flow<List<HadithEntity>> = flow {
        // 1. Check local Room DB
        val localHadiths = hadithDao.getHadithsForChapterSync(bookSlug, chapterNumber)
        if (localHadiths.isNotEmpty()) {
            emit(localHadiths)
            return@flow
        }

        // 2. Fetch from API and save into Room DB
        val fetchedHadiths = fetchChapterFromApi(bookSlug, chapterNumber)
        if (fetchedHadiths.isNotEmpty()) {
            hadithDao.insertHadiths(fetchedHadiths)
            emit(fetchedHadiths)
        } else {
            // Check fallback in local DB for the book
            val fallback = hadithDao.getHadithsForBookSync(bookSlug, limit = 20, offset = 0)
            emit(fallback)
        }
    }.flowOn(Dispatchers.IO)

    fun getHadithsForBook(bookSlug: String, limit: Int = 100, offset: Int = 0): Flow<List<HadithEntity>> =
        hadithDao.getHadithsForBook(bookSlug, limit, offset)

    fun searchHadiths(query: String): Flow<List<HadithEntity>> =
        hadithDao.searchHadiths(query)

    fun getHadithById(id: String): Flow<HadithEntity?> =
        hadithDao.getHadithById(id)

    fun getHadithByNumber(bookSlug: String, number: Int): Flow<HadithEntity?> =
        hadithDao.getHadithByNumber(bookSlug, number)

    suspend fun getRandomDailyHadith(): HadithEntity? = withContext(Dispatchers.IO) {
        hadithDao.getRandomHadith()
    }

    fun getBookmarkedHadiths(): Flow<List<HadithEntity>> = hadithDao.getBookmarkedHadiths()

    suspend fun toggleBookmark(hadith: HadithEntity) = withContext(Dispatchers.IO) {
        val isCurrentlyBookmarked = hadithDao.isHadithBookmarked(hadith.id)
        if (isCurrentlyBookmarked) {
            hadithDao.deleteBookmark(hadith.id)
            hadithDao.updateHadithBookmarkStatus(hadith.id, false)
        } else {
            hadithDao.insertBookmark(
                HadithBookmarkEntity(
                    hadithId = hadith.id,
                    bookSlug = hadith.bookSlug,
                    hadithNumber = hadith.hadithNumber
                )
            )
            hadithDao.updateHadithBookmarkStatus(hadith.id, true)
        }
    }

    suspend fun isBookmarked(hadithId: String): Boolean = withContext(Dispatchers.IO) {
        hadithDao.isHadithBookmarked(hadithId)
    }

    /**
     * Downloads an entire chapter and caches it permanently in the offline Room DB.
     */
    suspend fun downloadChapterToOfflineDb(bookSlug: String, chapterNumber: Int): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val local = hadithDao.getHadithsForChapterSync(bookSlug, chapterNumber)
            if (local.isNotEmpty()) {
                return@withContext Result.success(local.size)
            }
            val fetched = fetchChapterFromApi(bookSlug, chapterNumber)
            if (fetched.isNotEmpty()) {
                hadithDao.insertHadiths(fetched)
                Result.success(fetched.size)
            } else {
                Result.failure(Exception("হাদীস লোড করা সম্ভব হয়নি। ইন্টারনেট সংযোগ পরীক্ষা করুন।"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Direct Hadith Number Lookup:
     * Checks Room DB first; if absent, fetches from API and caches in Room DB.
     */
    suspend fun fetchHadithByNumber(bookSlug: String, hadithNumber: Int): HadithEntity? = withContext(Dispatchers.IO) {
        val local = hadithDao.getHadithByNumberSync(bookSlug, hadithNumber)
        if (local != null) return@withContext local

        val edition = SLUG_TO_EDITION[bookSlug] ?: return@withContext null
        val urls = listOf(
            "https://raw.githubusercontent.com/fawazahmed0/hadith-api/1/editions/$edition/$hadithNumber.json",
            "https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/$edition/$hadithNumber.min.json"
        )

        for (url in urls) {
            try {
                val req = Request.Builder().url(url).build()
                val resp = httpClient.newCall(req).execute()
                if (resp.isSuccessful) {
                    val body = resp.body?.string() ?: continue
                    val json = JSONObject(body)
                    val hadithsArr = json.optJSONArray("hadiths") ?: continue
                    if (hadithsArr.length() > 0) {
                        val hObj = hadithsArr.getJSONObject(0)
                        val text = cleanHtml(hObj.optString("text", ""))
                        val (arText, bnText) = parseArabicAndBengali(text)
                        val entity = HadithEntity(
                            id = "${bookSlug}_$hadithNumber",
                            bookSlug = bookSlug,
                            hadithNumber = hadithNumber,
                            chapterNumber = hObj.optJSONObject("reference")?.optInt("book", 1) ?: 1,
                            chapterTitleBn = "হাদীস $hadithNumber",
                            narratorBn = extractNarrator(bnText),
                            arabicText = arText,
                            banglaText = bnText,
                            gradeBn = formatGrade(bookSlug),
                            gradeColor = "SAHIH",
                            sourceBn = "${getBookNameBn(bookSlug)}: $hadithNumber",
                            explanationBn = "",
                            hadithTypeBn = "কওলী (বাণী)",
                            isBookmarked = false
                        )
                        hadithDao.insertHadiths(listOf(entity))
                        return@withContext entity
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Failed fetch hadith #$hadithNumber from $url: ${e.message}")
            }
        }
        null
    }

    /**
     * Fetches all hadiths of a chapter from online repository with fallback CDN
     */
    private fun fetchChapterFromApi(bookSlug: String, chapterNumber: Int): List<HadithEntity> {
        val edition = SLUG_TO_EDITION[bookSlug] ?: return emptyList()
        val urls = listOf(
            "https://raw.githubusercontent.com/fawazahmed0/hadith-api/1/editions/$edition/sections/$chapterNumber.json",
            "https://cdn.jsdelivr.net/gh/fawazahmed0/hadith-api@1/editions/$edition/sections/$chapterNumber.min.json"
        )

        for (url in urls) {
            try {
                val req = Request.Builder().url(url).build()
                val resp = httpClient.newCall(req).execute()
                if (!resp.isSuccessful) continue
                val body = resp.body?.string() ?: continue
                val json = JSONObject(body)
                val hadithsArr = json.optJSONArray("hadiths") ?: continue
                val metadata = json.optJSONObject("metadata")
                val chapterTitleEn = metadata?.optJSONObject("section")?.optString(chapterNumber.toString(), "") ?: ""
                val chapterTitleBn = if (chapterTitleEn.isNotBlank()) "অধ্যায় $chapterNumber" else "অধ্যায় $chapterNumber"

                val list = mutableListOf<HadithEntity>()
                for (i in 0 until hadithsArr.length()) {
                    val hObj = hadithsArr.getJSONObject(i)
                    val hNum = hObj.optInt("hadithnumber", i + 1)
                    val rawText = cleanHtml(hObj.optString("text", ""))
                    if (rawText.isBlank()) continue

                    val (arText, bnText) = parseArabicAndBengali(rawText)
                    val narrator = extractNarrator(bnText)

                    list.add(
                        HadithEntity(
                            id = "${bookSlug}_$hNum",
                            bookSlug = bookSlug,
                            hadithNumber = hNum,
                            chapterNumber = chapterNumber,
                            chapterTitleBn = chapterTitleBn,
                            narratorBn = narrator,
                            arabicText = arText,
                            banglaText = bnText,
                            gradeBn = formatGrade(bookSlug),
                            gradeColor = "SAHIH",
                            sourceBn = "${getBookNameBn(bookSlug)}: $hNum",
                            explanationBn = "",
                            hadithTypeBn = "কওলী (বাণী)",
                            isBookmarked = false
                        )
                    )
                }

                if (list.isNotEmpty()) {
                    return list
                }
            } catch (e: Exception) {
                Log.w(TAG, "Error fetching chapter $chapterNumber from $url", e)
            }
        }
        return emptyList()
    }

    private fun parseArabicAndBengali(raw: String): Pair<String, String> {
        val hasArabic = Pattern.compile("[\\u0600-\\u06FF]").matcher(raw).find()
        val hasBangla = Pattern.compile("[\\u0980-\\u09FF]").matcher(raw).find()

        if (hasArabic && hasBangla) {
            // Find where Bengali text begins
            val matcher = Pattern.compile("[\\u0980-\\u09FF]").matcher(raw)
            if (matcher.find()) {
                val start = matcher.start()
                val arPart = raw.substring(0, start).trim()
                val bnPart = raw.substring(start).trim()
                return Pair(arPart, bnPart)
            }
        } else if (hasArabic && !hasBangla) {
            return Pair(raw, "")
        }
        return Pair("", raw)
    }

    private fun extractNarrator(text: String): String {
        val patterns = listOf(
            Pattern.compile("([\\u0980-\\u09FF\\s\\.\\’\\']+(?:\\(রা[ঃ\\.]?\\)|\\(রহ[ঃ\\.]?\\)|\\(আ[ঃ\\.]?\\))\\s*হতে\\s*বর্ণিত)"),
            Pattern.compile("([\\u0980-\\u09FF\\s\\.\\’\\']+(?:\\(রা[ঃ\\.]?\\)|\\(রহ[ঃ\\.]?\\)|\\(আ[ঃ\\.]?\\))\\s*বলেন)"),
            Pattern.compile("([\\u0980-\\u09FF\\s\\.\\’\\']+(?:\\(রা[ঃ\\.]?\\)|\\(রহ[ঃ\\.]?\\))\\s*সূত্রে\\s*বর্ণিত)")
        )
        for (p in patterns) {
            val m = p.matcher(text)
            if (m.find()) {
                val found = m.group(1)?.trim() ?: ""
                if (found.length in 5..60) return found
            }
        }
        return "সাহাবায়ে কিরাম (রা.)"
    }

    private fun cleanHtml(html: String): String {
        return html.replace(Regex("<.*?>"), "").trim()
    }

    private fun formatGrade(bookSlug: String): String {
        return when (bookSlug) {
            "bukhari" -> "সহীহ বুখারী"
            "muslim" -> "সহীহ মুসলিম"
            "tirmidhi" -> "সহীহ / হাসান"
            "abu-dawud" -> "সহীহ আবু দাউদ"
            "nasai" -> "সহীহ নাসায়ী"
            "ibn-majah" -> "সহীহ ইবনে মাজাহ"
            "forty-nawawi" -> "সহীহ হাদীস"
            "hadith-qudsi" -> "হাদীসে কুদসী"
            else -> "সহীহ"
        }
    }

    private fun getBookNameBn(bookSlug: String): String {
        return HadithCatalog.allBooks.find { it.slug == bookSlug }?.nameBn ?: "হাদিস"
    }
}
