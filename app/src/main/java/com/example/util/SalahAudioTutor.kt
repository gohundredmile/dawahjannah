package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Locale

class SalahAudioTutor(private val context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isTtsReady = false
    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private var loopJob: Job? = null

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _currentRecitationId = MutableStateFlow<String?>(null)
    val currentRecitationId: StateFlow<String?> = _currentRecitationId.asStateFlow()

    private val _repeatCount = MutableStateFlow(1)
    val repeatCount: StateFlow<Int> = _repeatCount.asStateFlow()

    private val _currentLoopIndex = MutableStateFlow(1)
    val currentLoopIndex: StateFlow<Int> = _currentLoopIndex.asStateFlow()

    private val _playbackSpeed = MutableStateFlow(1.0f)
    val playbackSpeed: StateFlow<Float> = _playbackSpeed.asStateFlow()

    init {
        try {
            tts = TextToSpeech(context.applicationContext, this)
        } catch (_: Exception) {
            isTtsReady = false
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val arLocale = Locale("ar")
            val result = tts?.setLanguage(arLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Fallback to default locale
                tts?.setLanguage(Locale.getDefault())
            }
            tts?.setSpeechRate(_playbackSpeed.value)
            isTtsReady = true

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isPlaying.value = true
                }

                override fun onDone(utteranceId: String?) {
                    // Handled in coroutine loop
                }

                override fun onError(utteranceId: String?) {
                    _isPlaying.value = false
                }
            })
        }
    }

    fun setPlaybackSpeed(speed: Float) {
        _playbackSpeed.value = speed
        tts?.setSpeechRate(speed)
    }

    fun setRepeatCount(count: Int) {
        _repeatCount.value = count
    }

    fun speakRecitation(id: String, textToSpeak: String) {
        stop()
        _currentRecitationId.value = id
        _isPlaying.value = true
        _currentLoopIndex.value = 1

        val totalLoops = _repeatCount.value

        loopJob = scope.launch {
            var loop = 1
            while (isActive && (totalLoops == -1 || loop <= totalLoops)) {
                _currentLoopIndex.value = loop
                if (isTtsReady && tts != null) {
                    val params = android.os.Bundle()
                    params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "salah_tutor_$id")
                    tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, params, "salah_tutor_$id")

                    // Estimate duration based on text length and speed
                    val wordCount = textToSpeak.split("\\s+".toRegex()).size.coerceAtLeast(3)
                    val estDurationMs = ((wordCount * 500L) / _playbackSpeed.value).toLong().coerceIn(2000L, 25000L)
                    delay(estDurationMs + 800L)
                } else {
                    // Visual simulation if TTS is not installed
                    delay(3000L)
                }

                loop++
                if (totalLoops != -1 && loop > totalLoops) {
                    break
                }
                delay(600L)
            }
            _isPlaying.value = false
            _currentRecitationId.value = null
        }
    }

    fun stop() {
        loopJob?.cancel()
        loopJob = null
        try {
            tts?.stop()
        } catch (_: Exception) {}
        _isPlaying.value = false
        _currentRecitationId.value = null
    }

    fun release() {
        stop()
        try {
            tts?.shutdown()
            tts = null
        } catch (_: Exception) {}
    }
}
