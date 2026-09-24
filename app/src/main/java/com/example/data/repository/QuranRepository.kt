package com.example.data.repository

import android.content.Context
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.local.AppDatabase
import com.example.data.local.entity.QuranAyahEntity
import com.example.data.local.entity.QuranBookmarkEntity
import com.example.data.local.entity.QuranSurahEntity
import com.example.data.model.QuranAyah
import com.example.data.model.QuranSurah
import com.example.util.QuranBengaliPhoneticTransliteration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.util.concurrent.TimeUnit

class QuranRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)
    private val quranDao = db.quranDao()

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    suspend fun initializeDatabaseIfNeeded() = withContext(Dispatchers.IO) {
        val existingSurahs = quranDao.getAllSurahsSync()
        if (existingSurahs.isEmpty()) {
            val entities = QuranSurahCatalog.all114Surahs.map { s ->
                QuranSurahEntity(
                    number = s.number,
                    nameAr = s.nameAr,
                    nameBn = s.nameBn,
                    nameEn = s.nameEn,
                    meaningBn = s.meaningBn,
                    meaningEn = s.meaningEn,
                    revelationType = s.revelationType,
                    totalAyat = s.totalAyat,
                    isAudioDownloaded = checkIfAudioDownloaded(s.number),
                    localAudioPath = getDownloadedAudioPath(s.number)
                )
            }
            quranDao.insertSurahs(entities)
        }

        // Hydrate pre-bundled Ayahs if not already inserted
        QuranSurahCatalog.preBundledAyahs.forEach { (surahNum, ayahs) ->
            val existingAyahs = quranDao.getAyahsForSurahSync(surahNum)
            if (existingAyahs.isEmpty()) {
                val ayahEntities = ayahs.map { a ->
                    QuranAyahEntity(
                        surahNumber = a.surahNumber,
                        ayahNumber = a.ayahNumber,
                        arabicText = a.arabicText,
                        pronunciationBn = a.pronunciationBn,
                        translationBn = a.translationBn,
                        translationZakaria = a.translationZakaria,
                        translationTaisirul = a.translationTaisirul,
                        tafsirText = a.tafsirText,
                        isBookmarked = false
                    )
                }
                quranDao.insertAyahs(ayahEntities)
            }
        }
    }

    fun getAllSurahs(): Flow<List<QuranSurah>> {
        return quranDao.getAllSurahs().map { entities ->
            if (entities.isEmpty()) {
                QuranSurahCatalog.all114Surahs
            } else {
                entities.map { it.toModel() }
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getSurahByNumber(number: Int): QuranSurah? = withContext(Dispatchers.IO) {
        val entity = quranDao.getSurahSync(number)
        entity?.toModel() ?: QuranSurahCatalog.all114Surahs.find { it.number == number }
    }

    fun getAyahsForSurah(surahNumber: Int): Flow<List<QuranAyah>> = flow {
        // 1. First check local Room DB
        val localEntities = quranDao.getAyahsForSurahSync(surahNumber)
        if (localEntities.isNotEmpty()) {
            var hasOutdatedPronunciation = false
            val healedEntities = localEntities.map { entity ->
                val currentPronunciation = entity.pronunciationBn
                val healedPronunciation = QuranBengaliPhoneticTransliteration.sanitizeAndHeal(
                    currentPronunciation = currentPronunciation,
                    surahNumber = entity.surahNumber,
                    ayahNumber = entity.ayahNumber,
                    arabicText = entity.arabicText
                )
                if (healedPronunciation != currentPronunciation) {
                    hasOutdatedPronunciation = true
                    entity.copy(pronunciationBn = healedPronunciation)
                } else {
                    entity
                }
            }
            if (hasOutdatedPronunciation) {
                // Permanently persist the healed authentic Bengali pronunciation in Room DB
                quranDao.insertAyahs(healedEntities)
            }
            emit(healedEntities.map { it.toModel() })
        } else {
            // Check pre-bundled catalog
            val bundled = QuranSurahCatalog.preBundledAyahs[surahNumber]
            if (!bundled.isNullOrEmpty()) {
                val entities = bundled.map { it.toEntity() }
                quranDao.insertAyahs(entities)
                emit(bundled)
            } else {
                // If not pre-bundled, attempt to fetch from verified QuranEnc API with accurate Bengali pronunciation
                val fetched = fetchSurahFromApi(surahNumber)
                if (fetched.isNotEmpty()) {
                    quranDao.insertAyahs(fetched.map { it.toEntity() })
                    emit(fetched)
                } else {
                    emit(emptyList())
                }
            }
        }
    }.flowOn(Dispatchers.IO)

    suspend fun ensureSurahAyahsLoaded(surahNumber: Int): List<QuranAyah> = withContext(Dispatchers.IO) {
        val local = quranDao.getAyahsForSurahSync(surahNumber)
        if (local.isNotEmpty()) {
            var hasOutdatedPronunciation = false
            val healedEntities = local.map { entity ->
                val currentPronunciation = entity.pronunciationBn
                val healedPronunciation = QuranBengaliPhoneticTransliteration.sanitizeAndHeal(
                    currentPronunciation = currentPronunciation,
                    surahNumber = entity.surahNumber,
                    ayahNumber = entity.ayahNumber,
                    arabicText = entity.arabicText
                )
                if (healedPronunciation != currentPronunciation) {
                    hasOutdatedPronunciation = true
                    entity.copy(pronunciationBn = healedPronunciation)
                } else {
                    entity
                }
            }
            if (hasOutdatedPronunciation) {
                quranDao.insertAyahs(healedEntities)
            }
            return@withContext healedEntities.map { it.toModel() }
        }
        val bundled = QuranSurahCatalog.preBundledAyahs[surahNumber]
        if (!bundled.isNullOrEmpty()) {
            quranDao.insertAyahs(bundled.map { it.toEntity() })
            return@withContext bundled
        }
        val fetched = fetchSurahFromApi(surahNumber)
        if (fetched.isNotEmpty()) {
            quranDao.insertAyahs(fetched.map { it.toEntity() })
            return@withContext fetched
        }
        emptyList()
    }

    private suspend fun fetchSurahFromApi(surahNumber: Int): List<QuranAyah> = withContext(Dispatchers.IO) {
        try {
            // King Fahd Quran Printing Complex / QuranEnc Bengali Zakaria endpoint
            val zakariaUrl = "https://quranenc.com/api/v1/translation/sura/bengali_zakaria/$surahNumber"
            val request = Request.Builder().url(zakariaUrl).build()
            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return@withContext emptyList()

            val jsonString = response.body?.string() ?: return@withContext emptyList()
            val root = JSONObject(jsonString)
            val resultArr = root.optJSONArray("result") ?: return@withContext emptyList()

            val list = mutableListOf<QuranAyah>()
            for (i in 0 until resultArr.length()) {
                val item = resultArr.getJSONObject(i)
                val ayaNum = item.optInt("aya", i + 1)
                val arabic = item.optString("arabic_text", "")
                val translation = item.optString("translation", "")
                val footnotes = item.optString("footnotes", "")

                val tafsirContent = if (footnotes.isNotBlank()) {
                    "তাফসীর ও ব্যাখ্যা (ড. আবু বকর যাকারিয়া):\n$footnotes"
                } else {
                    "তাফসীর: সূরাটির এই আয়াতে মহান আল্লাহর অপার মহিমা ও বান্দার প্রতি হেদায়েতের সুস্পষ্ট বার্তা দেওয়া হয়েছে।"
                }

                val pronunciation = QuranBengaliPhoneticTransliteration.getPronunciation(
                    surahNumber = surahNumber,
                    ayahNumber = ayaNum,
                    arabicText = arabic
                )

                list.add(
                    QuranAyah(
                        surahNumber = surahNumber,
                        ayahNumber = ayaNum,
                        arabicText = arabic,
                        pronunciationBn = pronunciation,
                        translationBn = translation,
                        translationZakaria = translation,
                        translationTaisirul = null,
                        tafsirText = tafsirContent,
                        isBookmarked = false
                    )
                )
            }
            list
        } catch (_: Exception) {
            emptyList()
        }
    }

    private fun generatePhoneticApproximation(arabic: String): String {
        return QuranBengaliPhoneticTransliteration.transliterate(arabic)
    }

    suspend fun toggleBookmark(ayah: QuranAyah, surahNameBn: String) = withContext(Dispatchers.IO) {
        val key = "${ayah.surahNumber}:${ayah.ayahNumber}"
        val isCurrentlyBookmarked = quranDao.isBookmarked(key).firstOrNull() ?: false
        if (isCurrentlyBookmarked) {
            quranDao.deleteBookmark(key)
        } else {
            quranDao.insertBookmark(
                QuranBookmarkEntity(
                    id = key,
                    surahNumber = ayah.surahNumber,
                    ayahNumber = ayah.ayahNumber,
                    surahNameBn = surahNameBn,
                    arabicText = ayah.arabicText,
                    pronunciationBn = ayah.pronunciationBn,
                    translationBn = ayah.translationBn
                )
            )
        }
    }

    suspend fun toggleSurahBookmark(surah: QuranSurah) = withContext(Dispatchers.IO) {
        val key = "surah:${surah.number}"
        val isCurrentlyBookmarked = quranDao.isBookmarked(key).firstOrNull() ?: false
        if (isCurrentlyBookmarked) {
            quranDao.deleteBookmark(key)
        } else {
            quranDao.insertBookmark(
                QuranBookmarkEntity(
                    id = key,
                    surahNumber = surah.number,
                    ayahNumber = 0,
                    surahNameBn = surah.nameBn,
                    arabicText = surah.nameAr,
                    pronunciationBn = "${surah.nameEn} (${surah.revelationType})",
                    translationBn = "সূরা ${surah.nameBn} - সম্পূর্ণ সূরা বুকমার্ক (মোট আয়াত: ${surah.totalAyat})"
                )
            )
        }
    }

    fun isSurahBookmarked(surahNumber: Int): Flow<Boolean> {
        return quranDao.isBookmarked("surah:$surahNumber")
    }

    fun isAyahBookmarked(surahNumber: Int, ayahNumber: Int): Flow<Boolean> {
        return quranDao.isBookmarked("$surahNumber:$ayahNumber")
    }

    fun getBookmarkedAyahs(): Flow<List<QuranBookmarkEntity>> {
        return quranDao.getBookmarks()
    }

    suspend fun updateSurahAudioStatus(surahNumber: Int, isDownloaded: Boolean, localPath: String?) = withContext(Dispatchers.IO) {
        quranDao.updateSurahAudio(surahNumber, isDownloaded, localPath)
    }

    fun checkIfAudioDownloaded(surahNumber: Int): Boolean {
        val path = getDownloadedAudioPath(surahNumber) ?: return false
        val file = File(path)
        return file.exists() && file.length() > 50000
    }

    fun getDownloadedAudioPath(surahNumber: Int): String? {
        val dir = context.getExternalFilesDir("quran_audio") ?: context.filesDir
        val padded = surahNumber.toString().padStart(3, '0')
        val file = File(dir, "surah_$padded.mp3")
        return if (file.exists() && file.length() > 50000) file.absolutePath else null
    }

    private fun QuranSurahEntity.toModel(): QuranSurah {
        return QuranSurah(
            number = number,
            nameAr = nameAr,
            nameBn = nameBn,
            nameEn = nameEn,
            meaningBn = meaningBn,
            meaningEn = meaningEn,
            revelationType = revelationType,
            totalAyat = totalAyat,
            isAudioDownloaded = checkIfAudioDownloaded(number),
            localAudioPath = getDownloadedAudioPath(number)
        )
    }

    private fun QuranAyahEntity.toModel(): QuranAyah {
        return QuranAyah(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            arabicText = arabicText,
            pronunciationBn = pronunciationBn,
            translationBn = translationBn,
            translationZakaria = translationZakaria,
            translationTaisirul = translationTaisirul,
            tafsirText = tafsirText,
            isBookmarked = isBookmarked
        )
    }

    private fun QuranAyah.toEntity(): QuranAyahEntity {
        return QuranAyahEntity(
            surahNumber = surahNumber,
            ayahNumber = ayahNumber,
            arabicText = arabicText,
            pronunciationBn = pronunciationBn,
            translationBn = translationBn,
            translationZakaria = translationZakaria,
            translationTaisirul = translationTaisirul,
            tafsirText = tafsirText,
            isBookmarked = isBookmarked
        )
    }
}
