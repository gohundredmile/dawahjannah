package com.example.util

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.QuranReciter
import com.example.data.model.QuranSettings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QuranSettingsManager(private val context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<QuranSettings> = _settings.asStateFlow()

    private fun loadSettings(): QuranSettings {
        return QuranSettings(
            showArabic = prefs.getBoolean(KEY_SHOW_ARABIC, true),
            showPronunciation = prefs.getBoolean(KEY_SHOW_PRONUNCIATION, true),
            showTranslation = prefs.getBoolean(KEY_SHOW_TRANSLATION, true),
            showTafsirByDefault = prefs.getBoolean(KEY_SHOW_TAFSIR_BY_DEFAULT, false),
            showEnglishTranslation = prefs.getBoolean(KEY_SHOW_ENGLISH, false),
            arabicFontSize = prefs.getFloat(KEY_ARABIC_FONT_SIZE, 26f),
            banglaFontSize = prefs.getFloat(KEY_BANGLA_FONT_SIZE, 16f),
            arabicFontFamily = prefs.getString(KEY_ARABIC_FONT_FAMILY, "indopak") ?: "indopak",
            preferredTranslator = prefs.getString(KEY_PREFERRED_TRANSLATOR, "zakaria") ?: "zakaria",
            preferredTafsir = prefs.getString(KEY_PREFERRED_TAFSIR, "ibn_kathir") ?: "ibn_kathir",
            defaultReciterId = prefs.getString(KEY_DEFAULT_RECITER, QuranReciter.MISHARY_ALAFASY.id)
                ?: QuranReciter.MISHARY_ALAFASY.id,
            audioQuality = prefs.getString(KEY_AUDIO_QUALITY, "HIGH") ?: "HIGH",
            keepScreenAwake = prefs.getBoolean(KEY_KEEP_SCREEN_AWAKE, true),
            autoScrollWithAudio = prefs.getBoolean(KEY_AUTO_SCROLL, true),
            mushafMode = prefs.getBoolean(KEY_MUSHAF_MODE, false)
        )
    }

    fun updateShowArabic(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_ARABIC, show).apply()
        _settings.value = _settings.value.copy(showArabic = show)
    }

    fun updateShowPronunciation(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_PRONUNCIATION, show).apply()
        _settings.value = _settings.value.copy(showPronunciation = show)
    }

    fun updateShowTranslation(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_TRANSLATION, show).apply()
        _settings.value = _settings.value.copy(showTranslation = show)
    }

    fun updateShowTafsirByDefault(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_TAFSIR_BY_DEFAULT, show).apply()
        _settings.value = _settings.value.copy(showTafsirByDefault = show)
    }

    fun updateShowEnglishTranslation(show: Boolean) {
        prefs.edit().putBoolean(KEY_SHOW_ENGLISH, show).apply()
        _settings.value = _settings.value.copy(showEnglishTranslation = show)
    }

    fun updateArabicFontSize(size: Float) {
        val clamped = size.coerceIn(18f, 42f)
        prefs.edit().putFloat(KEY_ARABIC_FONT_SIZE, clamped).apply()
        _settings.value = _settings.value.copy(arabicFontSize = clamped)
    }

    fun updateBanglaFontSize(size: Float) {
        val clamped = size.coerceIn(12f, 28f)
        prefs.edit().putFloat(KEY_BANGLA_FONT_SIZE, clamped).apply()
        _settings.value = _settings.value.copy(banglaFontSize = clamped)
    }

    fun updateArabicFontFamily(family: String) {
        prefs.edit().putString(KEY_ARABIC_FONT_FAMILY, family).apply()
        _settings.value = _settings.value.copy(arabicFontFamily = family)
    }

    fun updatePreferredTranslator(translatorId: String) {
        prefs.edit().putString(KEY_PREFERRED_TRANSLATOR, translatorId).apply()
        _settings.value = _settings.value.copy(preferredTranslator = translatorId)
    }

    fun updatePreferredTafsir(tafsirId: String) {
        prefs.edit().putString(KEY_PREFERRED_TAFSIR, tafsirId).apply()
        _settings.value = _settings.value.copy(preferredTafsir = tafsirId)
    }

    fun updateDefaultReciter(reciterId: String) {
        prefs.edit().putString(KEY_DEFAULT_RECITER, reciterId).apply()
        _settings.value = _settings.value.copy(defaultReciterId = reciterId)
    }

    fun updateAudioQuality(quality: String) {
        prefs.edit().putString(KEY_AUDIO_QUALITY, quality).apply()
        _settings.value = _settings.value.copy(audioQuality = quality)
    }

    fun updateKeepScreenAwake(keepAwake: Boolean) {
        prefs.edit().putBoolean(KEY_KEEP_SCREEN_AWAKE, keepAwake).apply()
        _settings.value = _settings.value.copy(keepScreenAwake = keepAwake)
    }

    fun updateAutoScroll(autoScroll: Boolean) {
        prefs.edit().putBoolean(KEY_AUTO_SCROLL, autoScroll).apply()
        _settings.value = _settings.value.copy(autoScrollWithAudio = autoScroll)
    }

    fun updateMushafMode(mushaf: Boolean) {
        prefs.edit().putBoolean(KEY_MUSHAF_MODE, mushaf).apply()
        _settings.value = _settings.value.copy(mushafMode = mushaf)
    }

    fun resetToDefaults() {
        prefs.edit().clear().apply()
        _settings.value = loadSettings()
    }

    companion object {
        private const val PREFS_NAME = "quran_reader_settings_prefs"
        private const val KEY_SHOW_ARABIC = "key_show_arabic"
        private const val KEY_SHOW_PRONUNCIATION = "key_show_pronunciation"
        private const val KEY_SHOW_TRANSLATION = "key_show_translation"
        private const val KEY_SHOW_TAFSIR_BY_DEFAULT = "key_show_tafsir_by_default"
        private const val KEY_SHOW_ENGLISH = "key_show_english"
        private const val KEY_ARABIC_FONT_SIZE = "key_arabic_font_size"
        private const val KEY_BANGLA_FONT_SIZE = "key_bangla_font_size"
        private const val KEY_ARABIC_FONT_FAMILY = "key_arabic_font_family"
        private const val KEY_PREFERRED_TRANSLATOR = "key_preferred_translator"
        private const val KEY_PREFERRED_TAFSIR = "key_preferred_tafsir"
        private const val KEY_DEFAULT_RECITER = "key_default_reciter"
        private const val KEY_AUDIO_QUALITY = "key_audio_quality"
        private const val KEY_KEEP_SCREEN_AWAKE = "key_keep_screen_awake"
        private const val KEY_AUTO_SCROLL = "key_auto_scroll"
        private const val KEY_MUSHAF_MODE = "key_mushaf_mode"
    }
}
