package com.example.data.repository

import android.content.Context
import com.example.data.datasource.HadithCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.dao.HadithDao
import com.example.data.local.entity.HadithBookmarkEntity
import com.example.data.local.entity.HadithBookEntity
import com.example.data.local.entity.HadithChapterEntity
import com.example.data.local.entity.HadithEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class HadithRepository(
    private val hadithDao: HadithDao
) {
    constructor(context: Context) : this(AppDatabase.getDatabase(context).hadithDao())

    suspend fun initializeDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val existingBooksCount = hadithDao.getBooksCount()
        if (existingBooksCount == 0) {
            hadithDao.insertBooks(HadithCatalog.allBooks)
            hadithDao.insertChapters(HadithCatalog.initialChapters)
            hadithDao.insertHadiths(HadithCatalog.initialHadiths)
        } else {
            // Update book metadata or add missing hadiths
            hadithDao.insertBooks(HadithCatalog.allBooks)
            if (hadithDao.getHadithsCount() == 0) {
                hadithDao.insertChapters(HadithCatalog.initialChapters)
                hadithDao.insertHadiths(HadithCatalog.initialHadiths)
            }
        }
    }

    fun getAllBooks(): Flow<List<HadithBookEntity>> = hadithDao.getAllBooks()

    fun getSihahSittaBooks(): Flow<List<HadithBookEntity>> = hadithDao.getSihahSittaBooks()

    fun getPrimaryCoreBooks(): Flow<List<HadithBookEntity>> = hadithDao.getPrimaryCoreBooks()

    fun getBookBySlug(slug: String): Flow<HadithBookEntity?> = hadithDao.getBookBySlug(slug)

    fun getChaptersForBook(bookSlug: String): Flow<List<HadithChapterEntity>> =
        hadithDao.getChaptersForBook(bookSlug)

    fun getHadithsForChapter(bookSlug: String, chapterNumber: Int): Flow<List<HadithEntity>> =
        hadithDao.getHadithsForChapter(bookSlug, chapterNumber)

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
}
