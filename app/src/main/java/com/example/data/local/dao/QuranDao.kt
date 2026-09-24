package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.QuranAyahEntity
import com.example.data.local.entity.QuranBookmarkEntity
import com.example.data.local.entity.QuranSurahEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuranDao {

    @Query("SELECT * FROM quran_surahs ORDER BY number ASC")
    fun getAllSurahs(): Flow<List<QuranSurahEntity>>

    @Query("SELECT * FROM quran_surahs ORDER BY number ASC")
    suspend fun getAllSurahsSync(): List<QuranSurahEntity>

    @Query("SELECT * FROM quran_surahs WHERE number = :number LIMIT 1")
    fun getSurah(number: Int): Flow<QuranSurahEntity?>

    @Query("SELECT * FROM quran_surahs WHERE number = :number LIMIT 1")
    suspend fun getSurahSync(number: Int): QuranSurahEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSurahs(surahs: List<QuranSurahEntity>)

    @Query("UPDATE quran_surahs SET isAudioDownloaded = :isDownloaded, localAudioPath = :localPath WHERE number = :number")
    suspend fun updateSurahAudio(number: Int, isDownloaded: Boolean, localPath: String?)

    @Query("SELECT * FROM quran_ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    fun getAyahsForSurah(surahNumber: Int): Flow<List<QuranAyahEntity>>

    @Query("SELECT * FROM quran_ayahs WHERE surahNumber = :surahNumber ORDER BY ayahNumber ASC")
    suspend fun getAyahsForSurahSync(surahNumber: Int): List<QuranAyahEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAyahs(ayahs: List<QuranAyahEntity>)

    @Update
    suspend fun updateAyah(ayah: QuranAyahEntity)

    @Query("SELECT * FROM quran_bookmarks ORDER BY timestamp DESC")
    fun getBookmarks(): Flow<List<QuranBookmarkEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(bookmark: QuranBookmarkEntity)

    @Query("DELETE FROM quran_bookmarks WHERE id = :id")
    suspend fun deleteBookmark(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM quran_bookmarks WHERE id = :id)")
    fun isBookmarked(id: String): Flow<Boolean>

    @Query("""
        SELECT * FROM quran_surahs 
        WHERE nameBn LIKE '%' || :query || '%' 
           OR nameEn LIKE '%' || :query || '%' 
           OR nameAr LIKE '%' || :query || '%'
           OR meaningBn LIKE '%' || :query || '%'
           OR CAST(number AS TEXT) LIKE :query
        ORDER BY number ASC
    """)
    fun searchSurahs(query: String): Flow<List<QuranSurahEntity>>

    @Query("""
        SELECT * FROM quran_ayahs 
        WHERE translationBn LIKE '%' || :query || '%' 
           OR pronunciationBn LIKE '%' || :query || '%' 
           OR arabicText LIKE '%' || :query || '%'
           OR tafsirText LIKE '%' || :query || '%'
        ORDER BY surahNumber ASC, ayahNumber ASC
        LIMIT 50
    """)
    fun searchAyahs(query: String): Flow<List<QuranAyahEntity>>
}
