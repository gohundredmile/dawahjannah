package com.example.util

import android.content.Context
import android.content.SharedPreferences
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.PlaybackParams
import android.os.Build
import com.example.data.datasource.QuranSurahCatalog
import com.example.data.model.GeneratorConfig
import com.example.data.model.GeneratorPlayerStatus
import com.example.data.model.GeneratorPresetType
import com.example.data.model.QuranAyah
import com.example.data.model.QuranReciter
import com.example.data.model.SavedGeneratorSession
import com.example.data.repository.QuranRepository
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
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.util.UUID
import java.util.concurrent.TimeUnit

class QuranAudioGeneratorEngine(
    private val context: Context,
    private val quranRepository: QuranRepository
) {
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var mediaPlayer: MediaPlayer? = null
    private var progressTrackingJob: Job? = null
    private var pauseIntervalJob: Job? = null
    private var sleepTimerJob: Job? = null
    private var batchDownloadJob: Job? = null

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val prefs: SharedPreferences =
        context.getSharedPreferences("quran_audio_generator_prefs", Context.MODE_PRIVATE)

    // Current Config State
    private val _config = MutableStateFlow(GeneratorConfig())
    val config: StateFlow<GeneratorConfig> = _config.asStateFlow()

    // Realtime Player Status
    private val _playerStatus = MutableStateFlow(GeneratorPlayerStatus())
    val playerStatus: StateFlow<GeneratorPlayerStatus> = _playerStatus.asStateFlow()

    // Loaded Ayahs for the active Surah
    private val _currentSurahAyahs = MutableStateFlow<List<QuranAyah>>(emptyList())
    val currentSurahAyahs: StateFlow<List<QuranAyah>> = _currentSurahAyahs.asStateFlow()

    // Saved Sessions list
    private val _savedSessions = MutableStateFlow<List<SavedGeneratorSession>>(emptyList())
    val savedSessions: StateFlow<List<SavedGeneratorSession>> = _savedSessions.asStateFlow()

    // Download/Cache progress
    private val _isDownloading = MutableStateFlow(false)
    val isDownloading: StateFlow<Boolean> = _isDownloading.asStateFlow()

    private val _downloadProgress = MutableStateFlow(0f)
    val downloadProgress: StateFlow<Float> = _downloadProgress.asStateFlow()

    init {
        loadSavedSessions()
        // Load default surah (Al-Fatiha)
        loadSurahData(1)
    }

    fun loadSurahData(surahNumber: Int) {
        scope.launch {
            val surah = QuranSurahCatalog.all114Surahs.find { it.number == surahNumber }
            val ayahs = quranRepository.ensureSurahAyahsLoaded(surahNumber)
            _currentSurahAyahs.value = ayahs

            val validStart = 1
            val validEnd = if (ayahs.isNotEmpty()) ayahs.size else (surah?.totalAyat ?: 7)

            _config.value = _config.value.copy(
                surahNumber = surahNumber,
                startAyah = validStart,
                endAyah = validEnd
            )

            val currentAyahObj = ayahs.find { it.ayahNumber == validStart }

            _playerStatus.value = _playerStatus.value.copy(
                currentSurahNumber = surahNumber,
                currentSurahNameBn = surah?.nameBn ?: "আল-ফাতিহা",
                currentSurahNameAr = surah?.nameAr ?: "الفاتحة",
                currentAyahNumber = validStart,
                activeAyah = currentAyahObj,
                totalAyatInRange = (validEnd - validStart + 1).coerceAtLeast(1),
                cachedAyatCount = countCachedAyat(surahNumber, validStart, validEnd, _config.value.reciter)
            )
        }
    }

    fun applyPreset(preset: GeneratorPresetType) {
        _config.value = _config.value.copy(
            presetType = preset,
            ayahRepeatCount = preset.defaultRepeatCount,
            pauseBetweenAyatSeconds = preset.defaultPauseSeconds,
            playbackSpeed = preset.defaultSpeed,
            rangeLoopCount = preset.defaultRangeLoops
        )
        // If it's Ruqyah, set to Ayat al-Kursi (2:255) by default if current is 1
        if (preset == GeneratorPresetType.RUQYAH && _config.value.surahNumber == 1) {
            setAyahRange(surahNumber = 2, startAyah = 255, endAyah = 255)
        }
        applyPlaybackSpeedToActivePlayer(_config.value.playbackSpeed)
    }

    fun setAyahRange(surahNumber: Int, startAyah: Int, endAyah: Int) {
        val total = QuranSurahCatalog.all114Surahs.find { it.number == surahNumber }?.totalAyat ?: 7
        val s = startAyah.coerceIn(1, total)
        val e = endAyah.coerceIn(s, total)

        if (_config.value.surahNumber != surahNumber) {
            loadSurahData(surahNumber)
        }

        _config.value = _config.value.copy(
            surahNumber = surahNumber,
            startAyah = s,
            endAyah = e
        )

        val surah = QuranSurahCatalog.all114Surahs.find { it.number == surahNumber }
        val currentAyahObj = _currentSurahAyahs.value.find { it.ayahNumber == s }

        _playerStatus.value = _playerStatus.value.copy(
            currentSurahNumber = surahNumber,
            currentSurahNameBn = surah?.nameBn ?: _playerStatus.value.currentSurahNameBn,
            currentSurahNameAr = surah?.nameAr ?: _playerStatus.value.currentSurahNameAr,
            currentAyahNumber = s,
            activeAyah = currentAyahObj,
            totalAyatInRange = (e - s + 1).coerceAtLeast(1),
            cachedAyatCount = countCachedAyat(surahNumber, s, e, _config.value.reciter)
        )
    }

    fun selectReciter(reciter: QuranReciter) {
        _config.value = _config.value.copy(reciter = reciter)
        _playerStatus.value = _playerStatus.value.copy(
            cachedAyatCount = countCachedAyat(_config.value.surahNumber, _config.value.startAyah, _config.value.endAyah, reciter)
        )
        if (_playerStatus.value.isPlaying) {
            playCurrentAyah()
        }
    }

    fun setAyahRepeatCount(count: Int) {
        _config.value = _config.value.copy(ayahRepeatCount = count)
        _playerStatus.value = _playerStatus.value.copy(targetAyahRepeatCount = count)
    }

    fun setPauseBetweenAyatSeconds(seconds: Int) {
        _config.value = _config.value.copy(pauseBetweenAyatSeconds = seconds)
    }

    fun setPlaybackSpeed(speed: Float) {
        _config.value = _config.value.copy(playbackSpeed = speed)
        applyPlaybackSpeedToActivePlayer(speed)
    }

    fun setRangeLoopCount(count: Int) {
        _config.value = _config.value.copy(rangeLoopCount = count)
        _playerStatus.value = _playerStatus.value.copy(targetRangeLoopCount = count)
    }

    fun setIncludeBismillah(include: Boolean) {
        _config.value = _config.value.copy(includeBismillah = include)
    }

    fun toggleShowPronunciation() {
        _config.value = _config.value.copy(showPronunciation = !_config.value.showPronunciation)
    }

    fun toggleShowTranslation() {
        _config.value = _config.value.copy(showTranslation = !_config.value.showTranslation)
    }

    fun setSleepTimer(minutes: Int) {
        _config.value = _config.value.copy(sleepTimerMinutes = minutes)
        sleepTimerJob?.cancel()

        if (minutes > 0) {
            val totalSeconds = minutes * 60
            _playerStatus.value = _playerStatus.value.copy(sleepTimerRemainingSeconds = totalSeconds)
            sleepTimerJob = scope.launch {
                var remaining = totalSeconds
                while (remaining > 0 && isActive) {
                    delay(1000)
                    remaining--
                    _playerStatus.value = _playerStatus.value.copy(sleepTimerRemainingSeconds = remaining)
                }
                if (isActive) {
                    pause()
                    _playerStatus.value = _playerStatus.value.copy(sleepTimerRemainingSeconds = null)
                }
            }
        } else {
            _playerStatus.value = _playerStatus.value.copy(sleepTimerRemainingSeconds = null)
        }
    }

    // Playback Execution
    fun startSession() {
        pauseIntervalJob?.cancel()
        _playerStatus.value = _playerStatus.value.copy(
            currentAyahNumber = _config.value.startAyah,
            currentAyahRepeatIndex = 1,
            targetAyahRepeatCount = _config.value.ayahRepeatCount,
            currentRangeLoopIndex = 1,
            targetRangeLoopCount = _config.value.rangeLoopCount,
            isPauseIntervalActive = false,
            pauseRemainingSeconds = 0,
            errorMessage = null
        )
        playCurrentAyah()
    }

    fun resume() {
        if (_playerStatus.value.isPauseIntervalActive) {
            // Already waiting in pause countdown, let it continue
            _playerStatus.value = _playerStatus.value.copy(isPlaying = true, isPaused = false)
            return
        }
        if (mediaPlayer != null) {
            mediaPlayer?.start()
            _playerStatus.value = _playerStatus.value.copy(isPlaying = true, isPaused = false)
            startProgressTracking()
        } else {
            playCurrentAyah()
        }
    }

    fun pause() {
        pauseIntervalJob?.cancel()
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
            }
        }
        stopProgressTracking()
        _playerStatus.value = _playerStatus.value.copy(
            isPlaying = false,
            isPaused = true,
            isPauseIntervalActive = false
        )
    }

    fun stop() {
        pauseIntervalJob?.cancel()
        sleepTimerJob?.cancel()
        stopProgressTracking()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null

        _playerStatus.value = _playerStatus.value.copy(
            isPlaying = false,
            isPaused = false,
            isBuffering = false,
            isPauseIntervalActive = false,
            pauseRemainingSeconds = 0,
            currentPositionMs = 0,
            durationMs = 0
        )
    }

    fun nextAyah() {
        pauseIntervalJob?.cancel()
        val next = _playerStatus.value.currentAyahNumber + 1
        if (next <= _config.value.endAyah) {
            _playerStatus.value = _playerStatus.value.copy(
                currentAyahNumber = next,
                currentAyahRepeatIndex = 1,
                isPauseIntervalActive = false
            )
            playCurrentAyah()
        }
    }

    fun previousAyah() {
        pauseIntervalJob?.cancel()
        val prev = _playerStatus.value.currentAyahNumber - 1
        if (prev >= _config.value.startAyah) {
            _playerStatus.value = _playerStatus.value.copy(
                currentAyahNumber = prev,
                currentAyahRepeatIndex = 1,
                isPauseIntervalActive = false
            )
            playCurrentAyah()
        }
    }

    fun replayCurrentAyah() {
        pauseIntervalJob?.cancel()
        _playerStatus.value = _playerStatus.value.copy(
            isPauseIntervalActive = false,
            currentPositionMs = 0
        )
        playCurrentAyah()
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.seekTo(positionMs)
        _playerStatus.value = _playerStatus.value.copy(currentPositionMs = positionMs)
    }

    private fun playCurrentAyah() {
        pauseIntervalJob?.cancel()
        stopProgressTracking()
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {}
        mediaPlayer = null

        val surahNum = _playerStatus.value.currentSurahNumber
        val ayahNum = _playerStatus.value.currentAyahNumber
        val reciter = _config.value.reciter

        val activeAyahObj = _currentSurahAyahs.value.find { it.ayahNumber == ayahNum }

        _playerStatus.value = _playerStatus.value.copy(
            isPlaying = true,
            isPaused = false,
            isBuffering = true,
            activeAyah = activeAyahObj,
            isPauseIntervalActive = false,
            currentPositionMs = 0
        )

        // Check if offline cached file exists
        val cachedFile = getCachedAyahFile(surahNum, ayahNum, reciter)
        val audioSource = if (cachedFile.exists() && cachedFile.length() > 5000) {
            cachedFile.absolutePath
        } else {
            reciter.getAyahAudioUrl(surahNum, ayahNum)
        }

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
                    _playerStatus.value = _playerStatus.value.copy(
                        isBuffering = false,
                        durationMs = mp.duration
                    )
                    applyPlaybackSpeedToActivePlayer(_config.value.playbackSpeed, mp)
                    mp.start()
                    startProgressTracking()
                }
                setOnCompletionListener {
                    handleAyahCompletion()
                }
                setOnErrorListener { _, _, _ ->
                    _playerStatus.value = _playerStatus.value.copy(
                        isPlaying = false,
                        isBuffering = false,
                        errorMessage = "অডিও লোড করা যায়নি। ইন্টারনেট সংযোগ চেক করুন।"
                    )
                    stopProgressTracking()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            _playerStatus.value = _playerStatus.value.copy(
                isPlaying = false,
                isBuffering = false,
                errorMessage = e.localizedMessage ?: "অডিও প্লেয়ার শুরু হতে ব্যর্থ হয়েছে"
            )
        }
    }

    private fun handleAyahCompletion() {
        stopProgressTracking()
        val currentRepeat = _playerStatus.value.currentAyahRepeatIndex
        val targetRepeat = _config.value.ayahRepeatCount
        val pauseSeconds = _config.value.pauseBetweenAyatSeconds

        // Case 1: Infinite repeat on single ayah
        if (targetRepeat == -1) {
            triggerPauseOrNext(pauseSeconds) {
                playCurrentAyah()
            }
            return
        }

        // Case 2: Ayah has repeats remaining
        if (currentRepeat < targetRepeat) {
            _playerStatus.value = _playerStatus.value.copy(
                currentAyahRepeatIndex = currentRepeat + 1
            )
            triggerPauseOrNext(pauseSeconds) {
                playCurrentAyah()
            }
            return
        }

        // Case 3: Ayah repeats completed. Can we advance to next Ayah?
        val currentAyah = _playerStatus.value.currentAyahNumber
        val endAyah = _config.value.endAyah

        if (currentAyah < endAyah) {
            _playerStatus.value = _playerStatus.value.copy(
                currentAyahNumber = currentAyah + 1,
                currentAyahRepeatIndex = 1
            )
            triggerPauseOrNext(pauseSeconds) {
                playCurrentAyah()
            }
            return
        }

        // Case 4: Reached end of Ayah range. Can we loop the range?
        val currentRangeLoop = _playerStatus.value.currentRangeLoopIndex
        val targetRangeLoop = _config.value.rangeLoopCount

        if (targetRangeLoop == -1 || currentRangeLoop < targetRangeLoop) {
            _playerStatus.value = _playerStatus.value.copy(
                currentAyahNumber = _config.value.startAyah,
                currentAyahRepeatIndex = 1,
                currentRangeLoopIndex = currentRangeLoop + 1
            )
            triggerPauseOrNext(pauseSeconds) {
                playCurrentAyah()
            }
            return
        }

        // Case 5: Session fully complete!
        _playerStatus.value = _playerStatus.value.copy(
            isPlaying = false,
            isPaused = false,
            isBuffering = false,
            isPauseIntervalActive = false,
            pauseRemainingSeconds = 0,
            currentPositionMs = 0
        )
    }

    private fun triggerPauseOrNext(pauseSeconds: Int, onComplete: () -> Unit) {
        if (pauseSeconds <= 0) {
            onComplete()
            return
        }

        pauseIntervalJob?.cancel()
        _playerStatus.value = _playerStatus.value.copy(
            isPauseIntervalActive = true,
            pauseRemainingSeconds = pauseSeconds
        )

        pauseIntervalJob = scope.launch {
            var remaining = pauseSeconds
            while (remaining > 0 && isActive) {
                delay(1000)
                remaining--
                _playerStatus.value = _playerStatus.value.copy(pauseRemainingSeconds = remaining)
            }
            if (isActive) {
                _playerStatus.value = _playerStatus.value.copy(
                    isPauseIntervalActive = false,
                    pauseRemainingSeconds = 0
                )
                onComplete()
            }
        }
    }

    private fun applyPlaybackSpeedToActivePlayer(speed: Float, targetPlayer: MediaPlayer? = mediaPlayer) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && targetPlayer != null) {
            try {
                val params = targetPlayer.playbackParams ?: PlaybackParams()
                params.speed = speed
                targetPlayer.playbackParams = params
            } catch (_: Exception) {}
        }
    }

    private fun startProgressTracking() {
        stopProgressTracking()
        progressTrackingJob = scope.launch {
            while (isActive) {
                mediaPlayer?.let { player ->
                    try {
                        if (player.isPlaying) {
                            _playerStatus.value = _playerStatus.value.copy(
                                currentPositionMs = player.currentPosition,
                                durationMs = player.duration
                            )
                        }
                    } catch (_: Exception) {}
                }
                delay(200)
            }
        }
    }

    private fun stopProgressTracking() {
        progressTrackingJob?.cancel()
        progressTrackingJob = null
    }

    // Offline Caching & Downloading
    fun getCachedAyahFile(surahNumber: Int, ayahNumber: Int, reciter: QuranReciter): File {
        val dir = File(context.filesDir, "quran_generator/${reciter.id}")
        if (!dir.exists()) dir.mkdirs()
        val s = surahNumber.toString().padStart(3, '0')
        val a = ayahNumber.toString().padStart(3, '0')
        return File(dir, "${s}_$a.mp3")
    }

    private fun countCachedAyat(surahNumber: Int, start: Int, end: Int, reciter: QuranReciter): Int {
        var count = 0
        for (i in start..end) {
            val file = getCachedAyahFile(surahNumber, i, reciter)
            if (file.exists() && file.length() > 5000) {
                count++
            }
        }
        return count
    }

    fun downloadRangeAyat() {
        if (_isDownloading.value) return
        val surahNum = _config.value.surahNumber
        val start = _config.value.startAyah
        val end = _config.value.endAyah
        val reciter = _config.value.reciter
        val totalToDownload = end - start + 1

        batchDownloadJob?.cancel()
        _isDownloading.value = true
        _downloadProgress.value = 0f

        batchDownloadJob = scope.launch(Dispatchers.IO) {
            var completed = 0
            for (ayahNum in start..end) {
                if (!isActive) break
                val targetFile = getCachedAyahFile(surahNum, ayahNum, reciter)
                if (!targetFile.exists() || targetFile.length() < 5000) {
                    val url = reciter.getAyahAudioUrl(surahNum, ayahNum)
                    downloadSingleAyah(url, targetFile)
                }
                completed++
                withContext(Dispatchers.Main) {
                    _downloadProgress.value = completed.toFloat() / totalToDownload
                    _playerStatus.value = _playerStatus.value.copy(
                        cachedAyatCount = countCachedAyat(surahNum, start, end, reciter)
                    )
                }
            }
            withContext(Dispatchers.Main) {
                _isDownloading.value = false
            }
        }
    }

    private fun downloadSingleAyah(url: String, targetFile: File): Boolean {
        return try {
            val request = Request.Builder().url(url).build()
            val response = httpClient.newCall(request).execute()
            if (response.isSuccessful && response.body != null) {
                val temp = File(targetFile.parentFile, "${targetFile.name}.tmp")
                val fos = FileOutputStream(temp)
                response.body!!.byteStream().copyTo(fos)
                fos.flush()
                fos.close()
                if (temp.length() > 5000) {
                    if (targetFile.exists()) targetFile.delete()
                    temp.renameTo(targetFile)
                    true
                } else {
                    temp.delete()
                    false
                }
            } else {
                false
            }
        } catch (_: Exception) {
            false
        }
    }

    // Sessions Management (Persistence)
    fun saveSession(title: String) {
        val newSession = SavedGeneratorSession(
            id = UUID.randomUUID().toString(),
            titleBn = title.trim(),
            surahNumber = _config.value.surahNumber,
            startAyah = _config.value.startAyah,
            endAyah = _config.value.endAyah,
            reciterId = _config.value.reciter.id,
            ayahRepeatCount = _config.value.ayahRepeatCount,
            pauseBetweenAyatSeconds = _config.value.pauseBetweenAyatSeconds,
            playbackSpeed = _config.value.playbackSpeed,
            rangeLoopCount = _config.value.rangeLoopCount,
            presetTypeId = _config.value.presetType.id
        )

        val updated = listOf(newSession) + _savedSessions.value
        _savedSessions.value = updated
        persistSavedSessions(updated)
    }

    fun loadSession(session: SavedGeneratorSession) {
        val reciter = QuranReciter.entries.find { it.id == session.reciterId } ?: QuranReciter.MISHARY_ALAFASY
        val preset = GeneratorPresetType.entries.find { it.id == session.presetTypeId } ?: GeneratorPresetType.CUSTOM

        _config.value = GeneratorConfig(
            surahNumber = session.surahNumber,
            startAyah = session.startAyah,
            endAyah = session.endAyah,
            reciter = reciter,
            ayahRepeatCount = session.ayahRepeatCount,
            pauseBetweenAyatSeconds = session.pauseBetweenAyatSeconds,
            playbackSpeed = session.playbackSpeed,
            rangeLoopCount = session.rangeLoopCount,
            presetType = preset
        )

        loadSurahData(session.surahNumber)
    }

    fun deleteSession(sessionId: String) {
        val updated = _savedSessions.value.filter { it.id != sessionId }
        _savedSessions.value = updated
        persistSavedSessions(updated)
    }

    private fun persistSavedSessions(sessions: List<SavedGeneratorSession>) {
        val array = JSONArray()
        sessions.forEach { s ->
            val obj = JSONObject().apply {
                put("id", s.id)
                put("titleBn", s.titleBn)
                put("createdAt", s.createdAt)
                put("surahNumber", s.surahNumber)
                put("startAyah", s.startAyah)
                put("endAyah", s.endAyah)
                put("reciterId", s.reciterId)
                put("ayahRepeatCount", s.ayahRepeatCount)
                put("pauseBetweenAyatSeconds", s.pauseBetweenAyatSeconds)
                put("playbackSpeed", s.playbackSpeed.toDouble())
                put("rangeLoopCount", s.rangeLoopCount)
                put("presetTypeId", s.presetTypeId)
            }
            array.put(obj)
        }
        prefs.edit().putString("saved_generator_sessions", array.toString()).apply()
    }

    private fun loadSavedSessions() {
        val json = prefs.getString("saved_generator_sessions", null)
        if (json.isNullOrBlank()) {
            // Provide default recommended presets
            _savedSessions.value = listOf(
                SavedGeneratorSession(
                    id = "def_1",
                    titleBn = "সূরা আল-মুলক (রাতের হিফজ)",
                    surahNumber = 67,
                    startAyah = 1,
                    endAyah = 30,
                    reciterId = QuranReciter.MISHARY_ALAFASY.id,
                    ayahRepeatCount = 3,
                    pauseBetweenAyatSeconds = 1,
                    playbackSpeed = 1.0f,
                    rangeLoopCount = 1,
                    presetTypeId = GeneratorPresetType.REVISION.id
                ),
                SavedGeneratorSession(
                    id = "def_2",
                    titleBn = "সূরা কাহাফ (প্রথম ১০ আয়াত)",
                    surahNumber = 18,
                    startAyah = 1,
                    endAyah = 10,
                    reciterId = QuranReciter.ABDUL_BASIT.id,
                    ayahRepeatCount = 5,
                    pauseBetweenAyatSeconds = 2,
                    playbackSpeed = 0.85f,
                    rangeLoopCount = 2,
                    presetTypeId = GeneratorPresetType.SLOW_LEARNING.id
                ),
                SavedGeneratorSession(
                    id = "def_3",
                    titleBn = "আয়াতুল কুরসী ও ইখলাস (রুকিয়াহ)",
                    surahNumber = 2,
                    startAyah = 255,
                    endAyah = 255,
                    reciterId = QuranReciter.MAHER_AL_MUAIQLY.id,
                    ayahRepeatCount = 7,
                    pauseBetweenAyatSeconds = 2,
                    playbackSpeed = 1.0f,
                    rangeLoopCount = 3,
                    presetTypeId = GeneratorPresetType.RUQYAH.id
                )
            )
            return
        }

        try {
            val array = JSONArray(json)
            val list = mutableListOf<SavedGeneratorSession>()
            for (i in 0 until array.length()) {
                val o = array.getJSONObject(i)
                list.add(
                    SavedGeneratorSession(
                        id = o.getString("id"),
                        titleBn = o.getString("titleBn"),
                        createdAt = o.optLong("createdAt", System.currentTimeMillis()),
                        surahNumber = o.getInt("surahNumber"),
                        startAyah = o.getInt("startAyah"),
                        endAyah = o.getInt("endAyah"),
                        reciterId = o.getString("reciterId"),
                        ayahRepeatCount = o.getInt("ayahRepeatCount"),
                        pauseBetweenAyatSeconds = o.getInt("pauseBetweenAyatSeconds"),
                        playbackSpeed = o.getDouble("playbackSpeed").toFloat(),
                        rangeLoopCount = o.getInt("rangeLoopCount"),
                        presetTypeId = o.getString("presetTypeId")
                    )
                )
            }
            _savedSessions.value = list
        } catch (_: Exception) {
            _savedSessions.value = emptyList()
        }
    }
}
