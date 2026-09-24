package com.example.data.model

/**
 * Model representing the historical revelation context (শানে নযুল),
 * naming origin, period, themes, virtues, and tafsir insights of a Surah.
 */
data class SurahShaneNuzul(
    val surahNumber: Int,
    val surahNameBn: String,
    val naming: String,
    val period: String,
    val shaneNuzul: String,
    val themes: String,
    val virtues: String,
    val tafsirPerspectives: String = ""
)
