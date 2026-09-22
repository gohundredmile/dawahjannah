package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

enum class LoopMode(val titleBn: String, val count: Int) {
    ONCE("১ বার", 1),
    THREE_TIMES("৩ বার", 3),
    FIVE_TIMES("৫ বার", 5),
    INFINITE("অবিরাম", -1)
}

class AyahAudioPlayerHelper(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var progressJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentPositionMs = MutableStateFlow(0)
    val currentPositionMs: StateFlow<Int> = _currentPositionMs.asStateFlow()

    private val _durationMs = MutableStateFlow(0)
    val durationMs: StateFlow<Int> = _durationMs.asStateFlow()

    private val _loopMode = MutableStateFlow(LoopMode.ONCE)
    val loopMode: StateFlow<LoopMode> = _loopMode.asStateFlow()

    private var repeatCounter = 0
    private var currentUrl: String = ""

    fun setLoopMode(mode: LoopMode) {
        _loopMode.value = mode
    }

    fun play(url: String) {
        if (currentUrl == url && mediaPlayer != null) {
            mediaPlayer?.start()
            _isPlaying.value = true
            startProgressTracking()
            return
        }

        stop()
        currentUrl = url
        repeatCounter = 0

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
                    _durationMs.value = mp.duration
                    mp.start()
                    _isPlaying.value = true
                    startProgressTracking()
                }
                setOnCompletionListener { mp ->
                    handleCompletion(mp)
                }
                setOnErrorListener { _, _, _ ->
                    _isPlaying.value = false
                    stopProgressTracking()
                    true
                }
                prepareAsync()
            }
            mediaPlayer = player
        } catch (e: Exception) {
            _isPlaying.value = false
        }
    }

    private fun handleCompletion(mp: MediaPlayer) {
        repeatCounter++
        val mode = _loopMode.value
        val shouldRepeat = when (mode) {
            LoopMode.ONCE -> false
            LoopMode.THREE_TIMES -> repeatCounter < 3
            LoopMode.FIVE_TIMES -> repeatCounter < 5
            LoopMode.INFINITE -> true
        }

        if (shouldRepeat) {
            mp.seekTo(0)
            mp.start()
            _isPlaying.value = true
        } else {
            _isPlaying.value = false
            _currentPositionMs.value = _durationMs.value
            stopProgressTracking()
        }
    }

    fun pause() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                _isPlaying.value = false
                stopProgressTracking()
            }
        }
    }

    fun seekTo(positionMs: Int) {
        mediaPlayer?.seekTo(positionMs)
        _currentPositionMs.value = positionMs
    }

    fun stop() {
        progressJob?.cancel()
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        _isPlaying.value = false
        _currentPositionMs.value = 0
    }

    private fun startProgressTracking() {
        progressJob?.cancel()
        progressJob = scope.launch {
            while (isActive && _isPlaying.value) {
                mediaPlayer?.let {
                    if (it.isPlaying) {
                        _currentPositionMs.value = it.currentPosition
                    }
                }
                delay(200)
            }
        }
    }

    private fun stopProgressTracking() {
        progressJob?.cancel()
    }

    fun release() {
        stop()
    }
}
