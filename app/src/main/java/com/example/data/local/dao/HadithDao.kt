package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.HadithBookmarkEntity
import com.example.data.local.entity.HadithBookEntity
import com.example.data.local.entity.HadithChapterEntity
import com.example.data.local.entity.HadithEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HadithDao {

    // --- Book Queries ---
    @Query("SELECT * FROM hadith_books ORDER BY orderIndex ASC")
    fun getAllBooks(): Flow<List<HadithBookEntity>>

    @Query("SELECT * FROM hadith_books WHERE isSihahSitta = 1 ORDER BY orderIndex ASC")
    fun getSihahSittaBooks(): Flow<List<HadithBookEntity>>

    @Query("SELECT * FROM hadith_books WHERE isSihahSitta = 0 ORDER BY orderIndex ASC")
    fun getPrimaryCoreBooks(): Flow<List<HadithBookEntity>>

    @Query("SELECT * FROM hadith_books WHERE slug = :slug LIMIT 1")
    fun getBookBySlug(slug: String): Flow<HadithBookEntity?>

    @Query("SELECT COUNT(*) FROM hadith_books")
    suspend fun getBooksCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooks(books: List<HadithBookEntity>)

    // --- Chapter Queries ---
    @Query("SELECT * FROM hadith_chapters WHERE bookSlug = :bookSlug ORDER BY chapterNumber ASC")
    fun getChaptersForBook(bookSlug: String): Flow<List<HadithChapterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChapters(chapters: List<HadithChapterEntity>)

    // --- Hadith Queries ---
    @Query("SELECT * FROM hadith_items WHERE bookSlug = :bookSlug ORDER BY hadithNumber ASC LIMIT :limit OFFSET :offset")
    fun getHadithsForBook(bookSlug: String, limit: Int = 100, offset: Int = 0): Flow<List<HadithEntity>>

    @Query("SELECT * FROM hadith_items WHERE bookSlug = :bookSlug AND chapterNumber = :chapterNumber ORDER BY hadithNumber ASC")
    fun getHadithsForChapter(bookSlug: String, chapterNumber: Int): Flow<List<HadithEntity>>

    @Query("SELECT * FROM hadith_items WHERE id = :id LIMIT 1")
    fun getHadithById(id: String): Flow<HadithEntity?>

    @Query("SELECT * FROM hadith_items WHERE bookSlug = :bookSlug AND hadithNumber = :number LIMIT 1")
    fun getHadithByNumber(bookSlug: String, number: Int): Flow<HadithEntity?>

    @Query("SELECT COUNT(*) FROM hadith_items")
    suspend fun getHadithsCount(): Int

    @Query("SELECT COUNT(*) FROM hadith_items WHERE bookSlug = :bookSlug")
    suspend fun getHadithsCountForBook(bookSlug: String): Int

    @Query("""
        SELECT * FROM hadith_items 
        WHERE banglaText LIKE '%' || :query || '%' 
           OR narratorBn LIKE '%' || :query || '%' 
           OR arabicText LIKE '%' || :query || '%'
           OR chapterTitleBn LIKE '%' || :query || '%'
           OR CAST(hadithNumber AS TEXT) = :query
        ORDER BY hadithNumber ASC
        LIMIT 100
    """)
    fun searchHadiths(query: String): Flow<List<HadithEntity>>

    @Query("SELECT * FROM hadith_items ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomHadith(): HadithEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHadiths(hadiths: List<HadithEntity>)

    @Query("UPDATE hadith_items SET isBookmarked = :isBookmarked WHERE id = :hadithId")
    suspend fun updateHadithBookmarkStatus(hadithId: String, isBookmarked: Boolean)

    // --- Bookmark Queries ---
    @Query("SELECT * FROM hadith_bookmarks ORDER BY timestamp DESC")
    fun getAllBookmarks(): Flow<List<HadithBookmarkEntity>>

    @Query("""
        SELECT h.* FROM hadith_items h
        INNER JOIN hadith_bookmarks b ON h.id = b.hadithId
        ORDER BY b.timestamp DESC
    """)
    fun getBookmarkedHadiths(): Flow<List<HadithEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: HadithBookmarkEntity)

    @Query("DELETE FROM hadith_bookmarks WHERE hadithId = :hadithId")
    suspend fun deleteBookmark(hadithId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM hadith_bookmarks WHERE hadithId = :hadithId)")
    suspend fun isHadithBookmarked(hadithId: String): Boolean
}
