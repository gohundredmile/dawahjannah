package com.example.data.model

import androidx.compose.ui.graphics.Color

/**
 * Model representing an interactive date & time matched amol or event item
 * for the top live scrolling bar.
 */
data class LiveDateAmolItem(
    val id: String,
    val categoryBn: String,
    val titleBn: String,
    val shortSubtitleBn: String,
    val badgeBn: String,
    val iconKey: String = "STAR", // SUN, MOON, MOSQUE, BOOK, SHIELD, HEART, PRAYER, STAR, CALENDAR, CLOCK, SPARKLE
    val primaryColor: Color = Color(0xFF047857),
    val arabicText: String? = null,
    val pronunciationBn: String? = null,
    val meaningBn: String? = null,
    val virtuesBn: String? = null,
    val referenceBn: String? = null,
    val timingContextBn: String? = null,
    val actionTarget: String? = null, // friday_mode, morning_evening, sayyidul_istighfar, dua_acceptance, nofol_salat, surah_baqarah, tasbih, ruqyah, triple_calendar, five_waqt, isme_azam, tawbah_last_two, islamic_habit, ramadan_intelligence
    val actionButtonTextBn: String? = null
)
