package com.example.data.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Smart Presets designed for Quran memorization, revision, and spiritual contemplation.
 */
enum class GeneratorPresetType(
    val id: String,
    val titleBn: String,
    val subtitleBn: String,
    val descriptionBn: String,
    val emoji: String,
    val icon: ImageVector,
    val defaultRepeatCount: Int,
    val defaultPauseSeconds: Int,
    val defaultSpeed: Float,
    val defaultRangeLoops: Int,
    val badgeBn: String,
    val accentColor: Color
) {
    REVISION(
        id = "preset_revision",
        titleBn = "মুরাজা'আ ও রিভিশন",
        subtitleBn = "প্রতি আয়াত ৩ বার • ১ সে. বিরতি • স্বাভাবিক গতি",
        descriptionBn = "পূর্বে মুখস্থ করা সূরাসমূহ নিখুঁত ও মজবুতভাবে পুনরাবৃত্তি ও ধারাপাত করার জন্য বিশেষ মোড।",
        emoji = "🔁",
        icon = Icons.Default.Repeat,
        defaultRepeatCount = 3,
        defaultPauseSeconds = 1,
        defaultSpeed = 1.0f,
        defaultRangeLoops = 1,
        badgeBn = "হিফজ রিভিশন",
        accentColor = Color(0xFF047857)
    ),
    SLOW_LEARNING(
        id = "preset_slow_learning",
        titleBn = "ধীর শিক্ষণ ও নতুন হিফজ",
        subtitleBn = "প্রতি আয়াত ৭ বার • ২.৫ সে. বিরতি • ০.৮৫x গতি",
        descriptionBn = "নতুন আয়াত মুখস্থ করার জন্য দীর্ঘ পুনরাবৃত্তি ও প্রতিটি আয়াতের পর নিজে তিলাওয়াত করার বিরতি।",
        emoji = "🧠",
        icon = Icons.Default.Psychology,
        defaultRepeatCount = 7,
        defaultPauseSeconds = 2,
        defaultSpeed = 0.85f,
        defaultRangeLoops = 2,
        badgeBn = "নতুন হিফজ",
        accentColor = Color(0xFF0284C7)
    ),
    LISTENING(
        id = "preset_listening",
        titleBn = "শ্রবণ ও তাদাব্বুর",
        subtitleBn = "১ বার পাঠ • ১ সে. বিরতি • অনুবাদসহ অনুধাবন",
        descriptionBn = "একটানা তিলাওয়াত শোনার পাশাপাশি বাংলা অনুবাদ ও অর্থের গভীর ভাবার্থ হৃদয়ে ধারণ করার জন্য।",
        emoji = "🎧",
        icon = Icons.Default.Headphones,
        defaultRepeatCount = 1,
        defaultPauseSeconds = 1,
        defaultSpeed = 1.0f,
        defaultRangeLoops = 1,
        badgeBn = "ভাবার্থ শ্রবণ",
        accentColor = Color(0xFF7C3AED)
    ),
    RUQYAH(
        id = "preset_ruqyah",
        titleBn = "রুকিয়াহ ও আত্মিক শিফা",
        subtitleBn = "শিফার আয়াতসমূহ • ৩ বার আবৃত্তি • অবিরাম লুপ",
        descriptionBn = "আয়াতুল কুরসী, ইখলাস, ফালাক্ব ও নাসসহ আত্মিক সুস্থতা ও হেফাজতের বিশেষ আয়াতসমূহ।",
        emoji = "🛡️",
        icon = Icons.Default.Favorite,
        defaultRepeatCount = 3,
        defaultPauseSeconds = 2,
        defaultSpeed = 1.0f,
        defaultRangeLoops = 3,
        badgeBn = "আত্মিক প্রশান্তি",
        accentColor = Color(0xFFD97706)
    ),
    CUSTOM(
        id = "preset_custom",
        titleBn = "কাস্টম কনফিগারেশন",
        subtitleBn = "আপনার ইচ্ছেমত ক্বারী, গতি, বিরতি ও লুপ নির্ধারণ",
        descriptionBn = "আপনার ব্যক্তিগত প্রয়োজন অনুযায়ী যেকোনো সূরা, নির্দিষ্ট আয়াত সীমা ও প্লেব্যাক সেটিংস।",
        emoji = "⚙️",
        icon = Icons.Default.Tune,
        defaultRepeatCount = 1,
        defaultPauseSeconds = 0,
        defaultSpeed = 1.0f,
        defaultRangeLoops = 1,
        badgeBn = "সম্পূর্ণ কাস্টম",
        accentColor = Color(0xFF475569)
    )
}

/**
 * Configuration options for generating and controlling the Tilawat session.
 */
data class GeneratorConfig(
    val surahNumber: Int = 1,
    val startAyah: Int = 1,
    val endAyah: Int = 7,
    val reciter: QuranReciter = QuranReciter.MISHARY_ALAFASY,
    val ayahRepeatCount: Int = 3, // Number of times each ayah is repeated (1, 2, 3, 5, 7, 10, -1 for infinite)
    val pauseBetweenAyatSeconds: Int = 1, // Seconds of pause between ayah repetitions/advancement
    val playbackSpeed: Float = 1.0f, // 0.75f, 0.85f, 1.0f, 1.25f
    val rangeLoopCount: Int = 1, // Number of times the entire selected range loops (1, 2, 3, 5, -1)
    val includeBismillah: Boolean = true,
    val showPronunciation: Boolean = true,
    val showTranslation: Boolean = true,
    val sleepTimerMinutes: Int = 0, // 0 = off, 10, 15, 30, 45, 60, -1 = end of session
    val presetType: GeneratorPresetType = GeneratorPresetType.REVISION
)

/**
 * Realtime playback and state tracking for the generator engine.
 */
data class GeneratorPlayerStatus(
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val isBuffering: Boolean = false,
    val currentSurahNumber: Int = 1,
    val currentSurahNameBn: String = "আল-ফাতিহা",
    val currentSurahNameAr: String = "الفاتحة",
    val currentAyahNumber: Int = 1,
    val currentAyahRepeatIndex: Int = 1, // 1 to ayahRepeatCount
    val targetAyahRepeatCount: Int = 3,
    val currentRangeLoopIndex: Int = 1, // 1 to rangeLoopCount
    val targetRangeLoopCount: Int = 1,
    val isPauseIntervalActive: Boolean = false,
    val pauseRemainingSeconds: Int = 0,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
    val cachedAyatCount: Int = 0,
    val totalAyatInRange: Int = 7,
    val sleepTimerRemainingSeconds: Int? = null,
    val activeAyah: QuranAyah? = null,
    val errorMessage: String? = null
)

/**
 * Saved custom session or playlist stored locally for quick re-use.
 */
data class SavedGeneratorSession(
    val id: String,
    val titleBn: String,
    val createdAt: Long = System.currentTimeMillis(),
    val surahNumber: Int,
    val startAyah: Int,
    val endAyah: Int,
    val reciterId: String,
    val ayahRepeatCount: Int,
    val pauseBetweenAyatSeconds: Int,
    val playbackSpeed: Float,
    val rangeLoopCount: Int,
    val presetTypeId: String
)
