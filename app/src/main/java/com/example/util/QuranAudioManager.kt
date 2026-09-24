package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.model.BatchDownloadState
import com.example.data.model.DownloadProgressState
import com.example.data.model.QuranReciter
import com.example.data.model.SurahAudioPlayerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

class QuranAudioManager(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var mediaPlayer: MediaPlayer? = null
    private var progressTrackingJob: Job? = null

    // Individual download jobs map
    private val activeDownloadJobs = ConcurrentHashMap<Int, Job>()
    private var batchDownloadJob: Job? = null
    @Volatile private var isBatchPausedFlag = false

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val _playerState = MutableStateFlow(SurahAudioPlayerState())
    val playerState: StateFlow<SurahAudioPlayerState> = _playerState.asStateFlow()

    private val _downloadState = MutableStateFlow<Map<Int, DownloadProgressState>>(emptyMap())
    val downloadState: StateFlow<Map<Int, DownloadProgressState>> = _downloadState.asStateFlow()

    private val _batchDownloadState = MutableStateFlow(BatchDownloadState())
    val batchDownloadState: StateFlow<BatchDownloadState> = _batchDownloadState.asStateFlow()

    private val _selectedReciter = MutableStateFlow(QuranReciter.MISHARY_ALAFASY)
    val selectedReciter: StateFlow<QuranReciter> = _selectedReciter.asStateFlow()

    fun selectReciter(reciter: QuranReciter) {
        _selectedReciter.value = reciter
        // If playing currently, switch stream
        val currentSurah = _playerState.value.surahNumber
        if (currentSurah != null && _playerState.value.isPlaying) {
            playSurah(currentSurah, _playerState.value.surahNameBn)
        }
    }

    fun playSurah(surahNumber: Int, surahNameBn: String) {
        val current = _playerState.value
        if (current.surahNumber == surahNumber && mediaPlayer != null) {
            if (current.isPlaying) {
                pause()
            } else {
                mediaPlayer?.start()
                _playerState.value = _playerState.value.copy(isPlaying = true)
                startProgressTracking()
            }
            return
        }

        stop()

        val offlineFile = getOfflineSurahFile(surahNumber)
        val isOffline = offlineFile.exists() && offlineFile.length() > 50000
        val audioSource = if (isOffline) {
            offlineFile.absolutePath
        } else {
            getHighQualitySurahUrl(surahNumber, _selectedReciter.value)
        }

        _playerState.value = SurahAudioPlayerState(
            surahNumber = surahNumber,
            surahNameBn = surahNameBn,
            reciter = _selectedReciter.value,
            isPlaying = false,
            isBuffering = true,
            isOfflineFile = isOffline
        )

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(audioSource)
                setOnPreparedListener { mp ->
                    _playerState.value = _playerState.value.copy(
                        isBuffering = false,
                        isPlaying = true,
                        durationMs = mp.duration
                    )
                    mp.start()
                    startProgressTracking()
                }
                setOnCompletionListener {
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        currentPositionMs = 0
                    )
                    stopProgressTracking()
                }
                setOnErrorListener { _, _, _ ->
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        isBuffering = false
                    )
                    stopProgressTracking()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (_: Exception) {
            _playerState.value = _playerState.value.copy(isPlaying = false, isBuffering = false)
        }
    }

    fun playAyahAudio(surahNumber: Int, ayahNumber: Int) {
        stop()
        val url = _selectedReciter.value.getAyahAudioUrl(surahNumber, ayahNumber)

        _playerState.value = SurahAudioPlayerState(
            surahNumber = surahNumber,
            activeAyahNumber = ayahNumber,
            reciter = _selectedReciter.value,
            isPlaying = false,
            isBuffering = true
        )

        try {
            val player = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { mp ->
                    _playerState.value = _playerState.value.copy(
                        isBuffering = false,
                        isPlaying = true,
                        durationMs = mp.duration
                    )
                    mp.start()
                    startProgressTracking()
                }
                setOnCompletionListener {
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        activeAyahNumber = null,
                        currentPositionMs = 0
                    )
                    stopProgressTracking()
                }
                setOnErrorListener { _, _, _ ->
                    _playerState.value = _playerState.value.copy(
                        isPlaying = false,
                        isBuffering = false,
                        activeAyahNumber = null
                    )
                    stopProgressTracking()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (_: Exception) {
            _playerState.value = _playerState.value.copy(isPlaying = false, isBuffering = false, activeAyahNumber = null)
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _playerState.value = _playerState.value.copy(isPlaying = false)
            }
        }
        stopProgressTracking()
    }

    fun resume() {
        mediaPlayer?.let {
            it.start()
            _playerState.value = _playerState.value.copy(isPlaying = true)
            startProgressTracking()
        }
    }

    fun stop() {
        stopProgressTracking()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null
        _playerState.value = _playerState.value.copy(
            isPlaying = false,
            isBuffering = false,
            activeAyahNumber = null
        )
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.let {
            it.seekTo(positionMs)
            _playerState.value = _playerState.value.copy(currentPositionMs = positionMs)
        }
    }

    fun seekForward(offsetMs: Int = 10000) {
        mediaPlayer?.let {
            val next = (it.currentPosition + offsetMs).coerceAtMost(it.duration)
            seekTo(next)
        }
    }

    fun seekBackward(offsetMs: Int = 10000) {
        mediaPlayer?.let {
            val prev = (it.currentPosition - offsetMs).coerceAtLeast(0)
            seekTo(prev)
        }
    }

    private fun startProgressTracking() {
        progressTrackingJob?.cancel()
        progressTrackingJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let { player ->
                    if (player.isPlaying) {
                        _playerState.value = _playerState.value.copy(
                            currentPositionMs = player.currentPosition,
                            durationMs = player.duration
                        )
                    }
                }
                delay(500)
            }
        }
    }

    private fun stopProgressTracking() {
        progressTrackingJob?.cancel()
        progressTrackingJob = null
    }

    // --- Offline Surah Download Feature ---

    fun isSurahDownloaded(surahNumber: Int): Boolean {
        val file = getOfflineSurahFile(surahNumber)
        return file.exists() && file.length() > 50000
    }

    fun getOfflineSurahFile(surahNumber: Int): File {
        val dir = context.getExternalFilesDir("quran_audio") ?: context.filesDir
        if (!dir.exists()) dir.mkdirs()
        val padded = surahNumber.toString().padStart(3, '0')
        return File(dir, "surah_$padded.mp3")
    }

    fun getHighQualitySurahUrl(surahNumber: Int, reciter: QuranReciter): String {
        return reciter.getSurahAudioUrl(surahNumber)
    }

    fun downloadSurahAudio(
        surahNumber: Int,
        onComplete: (Boolean) -> Unit
    ) {
        downloadSurahAudio(surahNumber, _selectedReciter.value, onComplete)
    }

    fun downloadSurahAudio(
        surahNumber: Int,
        reciter: QuranReciter = _selectedReciter.value,
        onComplete: (Boolean) -> Unit = {}
    ) {
        cancelSurahDownload(surahNumber)

        val targetFile = getOfflineSurahFile(surahNumber)
        val audioUrl = getHighQualitySurahUrl(surahNumber, reciter)

        updateDownloadState(surahNumber, DownloadProgressState(surahNumber, isDownloading = true, progressPercent = 0))

        val job = scope.launch(Dispatchers.IO) {
            val success = executeFileDownload(audioUrl, targetFile, surahNumber)
            withContext(Dispatchers.Main) {
                activeDownloadJobs.remove(surahNumber)
                onComplete(success)
            }
        }
        activeDownloadJobs[surahNumber] = job
    }

    fun cancelSurahDownload(surahNumber: Int) {
        activeDownloadJobs.remove(surahNumber)?.cancel()
        updateDownloadState(
            surahNumber,
            DownloadProgressState(surahNumber, isDownloading = false, progressPercent = 0)
        )
    }

    fun deleteDownloadedSurah(surahNumber: Int): Boolean {
        cancelSurahDownload(surahNumber)
        val file = getOfflineSurahFile(surahNumber)
        val deleted = if (file.exists()) file.delete() else false
        updateDownloadState(surahNumber, DownloadProgressState(surahNumber, isDownloading = false, progressPercent = 0))
        return deleted
    }

    fun deleteAllDownloadedAudio(): Int {
        var count = 0
        for (i in 1..114) {
            if (deleteDownloadedSurah(i)) {
                count++
            }
        }
        return count
    }

    fun getDownloadedSurahsCount(): Int {
        var count = 0
        for (i in 1..114) {
            if (isSurahDownloaded(i)) count++
        }
        return count
    }

    fun getTotalAudioStorageBytes(): Long {
        var total: Long = 0
        for (i in 1..114) {
            val f = getOfflineSurahFile(i)
            if (f.exists()) {
                total += f.length()
            }
        }
        return total
    }

    // --- Batch Download All 114 Surahs (1-Click Feature) ---

    fun startBatchDownloadAllSurahs(reciter: QuranReciter = _selectedReciter.value) {
        if (_batchDownloadState.value.isBatchRunning && !_batchDownloadState.value.isPaused) {
            return
        }

        isBatchPausedFlag = false

        batchDownloadJob?.cancel()
        batchDownloadJob = scope.launch(Dispatchers.IO) {
            var completedCount = getDownloadedSurahsCount()

            withContext(Dispatchers.Main) {
                _batchDownloadState.value = BatchDownloadState(
                    isBatchRunning = true,
                    isPaused = false,
                    completedSurahsCount = completedCount,
                    totalSurahsCount = 114,
                    overallPercent = ((completedCount * 100) / 114),
                    statusMessage = "ডাউনলোড প্রস্তুতি চলছে..."
                )
            }

            for (surahNum in 1..114) {
                if (!isActive) break

                while (isBatchPausedFlag) {
                    delay(500)
                    if (!isActive) break
                }
                if (!isActive) break

                val surahMeta = QuranSurahCatalog.all114Surahs.find { it.number == surahNum }
                val surahName = surahMeta?.nameBn ?: "সূরা $surahNum"

                if (isSurahDownloaded(surahNum)) {
                    continue
                }

                withContext(Dispatchers.Main) {
                    _batchDownloadState.value = _batchDownloadState.value.copy(
                        currentSurahNumber = surahNum,
                        currentSurahNameBn = surahName,
                        statusMessage = "ডাউনলোড হচ্ছে: $surahName (${surahNum}/১১৪)"
                    )
                }

                val targetFile = getOfflineSurahFile(surahNum)
                val audioUrl = getHighQualitySurahUrl(surahNum, reciter)

                val success = executeFileDownload(audioUrl, targetFile, surahNum)
                if (success) {
                    completedCount = getDownloadedSurahsCount()
                    withContext(Dispatchers.Main) {
                        _batchDownloadState.value = _batchDownloadState.value.copy(
                            completedSurahsCount = completedCount,
                            overallPercent = ((completedCount * 100) / 114),
                            currentSurahPercent = 100
                        )
                    }
                }
                delay(150)
            }

            withContext(Dispatchers.Main) {
                val finalCount = getDownloadedSurahsCount()
                _batchDownloadState.value = BatchDownloadState(
                    isBatchRunning = false,
                    isPaused = false,
                    completedSurahsCount = finalCount,
                    totalSurahsCount = 114,
                    overallPercent = ((finalCount * 100) / 114),
                    statusMessage = if (finalCount >= 114) "আলহামদুলিল্লাহ! সম্পূর্ণ ১১৪টি সূরা ডাউনলোড সম্পন্ন হয়েছে।"
                    else "ডাউনলোড সমাপ্ত (${finalCount}/১১৪ সূরা সংরক্ষিত)"
                )
            }
        }
    }

    fun pauseBatchDownload() {
        isBatchPausedFlag = true
        _batchDownloadState.value = _batchDownloadState.value.copy(
            isPaused = true,
            statusMessage = "ডাউনলোড স্থগিত রাখা হয়েছে"
        )
    }

    fun resumeBatchDownload() {
        isBatchPausedFlag = false
        _batchDownloadState.value = _batchDownloadState.value.copy(
            isPaused = false,
            statusMessage = "পুনরায় ডাউনলোড শুরু হচ্ছে..."
        )
    }

    fun cancelBatchDownload() {
        isBatchPausedFlag = false
        batchDownloadJob?.cancel()
        batchDownloadJob = null

        // Also cancel any currently active individual surah download
        activeDownloadJobs.values.forEach { it.cancel() }
        activeDownloadJobs.clear()

        _batchDownloadState.value = BatchDownloadState(
            isBatchRunning = false,
            isPaused = false,
            completedSurahsCount = getDownloadedSurahsCount(),
            statusMessage = "ডাউনলোড বাতিল করা হয়েছে"
        )
    }

    private suspend fun executeFileDownload(
        audioUrl: String,
        targetFile: File,
        surahNumber: Int
    ): Boolean {
        try {
            val request = Request.Builder()
                .url(audioUrl)
                .addHeader("User-Agent", "DawahToJannah/1.9.0")
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful || response.body == null) {
                withContext(Dispatchers.Main) {
                    updateDownloadState(
                        surahNumber,
                        DownloadProgressState(surahNumber, isDownloading = false, errorMessage = "ডাউনলোড ব্যর্থ")
                    )
                }
                return false
            }

            val body = response.body!!
            val totalBytes = body.contentLength()
            val inputStream = body.byteStream()
            val tempFile = File(targetFile.parentFile, "surah_${surahNumber}_temp.mp3")
            val outputStream = FileOutputStream(tempFile)

            val buffer = ByteArray(16 * 1024)
            var downloadedBytes: Long = 0
            var read: Int
            var lastProgress = 0

            while (inputStream.read(buffer).also { read = it } != -1) {
                outputStream.write(buffer, 0, read)
                downloadedBytes += read
                if (totalBytes > 0) {
                    val progress = ((downloadedBytes * 100) / totalBytes).toInt()
                    if (progress != lastProgress) {
                        lastProgress = progress
                        withContext(Dispatchers.Main) {
                            updateDownloadState(
                                surahNumber,
                                DownloadProgressState(
                                    surahNumber = surahNumber,
                                    isDownloading = true,
                                    progressPercent = progress,
                                    downloadedBytes = downloadedBytes,
                                    totalBytes = totalBytes
                                )
                            )
                            if (_batchDownloadState.value.isBatchRunning) {
                                _batchDownloadState.value = _batchDownloadState.value.copy(
                                    currentSurahPercent = progress
                                )
                            }
                        }
                    }
                }
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            if (tempFile.length() > 50000) {
                if (targetFile.exists()) targetFile.delete()
                val renamed = tempFile.renameTo(targetFile)
                if (renamed) {
                    withContext(Dispatchers.Main) {
                        updateDownloadState(
                            surahNumber,
                            DownloadProgressState(
                                surahNumber = surahNumber,
                                isDownloading = false,
                                progressPercent = 100,
                                downloadedBytes = downloadedBytes,
                                totalBytes = totalBytes
                            )
                        )
                    }
                    return true
                }
            }
            tempFile.delete()
            return false
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                updateDownloadState(
                    surahNumber,
                    DownloadProgressState(
                        surahNumber = surahNumber,
                        isDownloading = false,
                        errorMessage = e.localizedMessage ?: "সংযোগ ত্রুটি"
                    )
                )
            }
            return false
        }
    }

    private fun updateDownloadState(surahNumber: Int, state: DownloadProgressState) {
        val map = _downloadState.value.toMutableMap()
        map[surahNumber] = state
        _downloadState.value = map
    }
}
