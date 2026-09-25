package com.example.util

import com.example.data.model.PrayerTimeItem
import java.util.Calendar

/**
 * Islamic Friday (Jumu'ah) timing utility.
 * In Islamic tradition, the day of Jumu'ah begins immediately with the start of Maghrib waqt
 * on Thursday evening (Laylat al-Jumu'ah) and concludes immediately with the start of Maghrib waqt
 * on Friday evening (when Saturday / Laylat al-Sabt begins).
 */
object FridayTimingHelper {

    /**
     * Checks if Friday Mode is currently active based on Islamic timing.
     * Starts at Thursday Maghrib waqt and ends at Friday Maghrib waqt.
     */
    fun isFridayModeActive(
        cal: Calendar = Calendar.getInstance(),
        prayerList: List<PrayerTimeItem>? = null,
        maghribMinuteOfDay: Int? = null
    ): Boolean {
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val currentMinutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE)

        // Determine Maghrib start minute of the day (defaults to 18:00 if not available)
        val maghribMin = maghribMinuteOfDay
            ?: prayerList?.find { it.id == "maghrib" }?.timeMinutesFromMidnight
            ?: (18 * 60)

        return when (dayOfWeek) {
            Calendar.THURSDAY -> {
                // Starts immediately along with starting Maghrib Waqt on Thursday
                currentMinutes >= maghribMin
            }
            Calendar.FRIDAY -> {
                // Continues until starting Maghrib Waqt on Friday
                currentMinutes < maghribMin
            }
            else -> false
        }
    }

    /**
     * Returns a human-friendly Bangla description of the current Friday phase.
     */
    fun getFridayPhaseTitleBn(
        cal: Calendar = Calendar.getInstance(),
        prayerList: List<PrayerTimeItem>? = null
    ): String {
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.THURSDAY -> "পবিত্র জুমার রজনী (Laylat al-Jumu'ah) — Friday Mode সক্রিয়"
            Calendar.FRIDAY -> "আজ পবিত্র জুমার দিন — Friday Mode সক্রিয়"
            else -> "Friday Mode (জুমার মোড)"
        }
    }

    /**
     * Returns subtitle explaining the exact Islamic time window.
     */
    fun getFridayWindowSubtitleBn(): String {
        return "বৃহস্পতিবার মাগরিব ওয়াক্ত শুরু থেকে শুক্রবার মাগরিব ওয়াক্ত শুরু পর্যন্ত জুমার বরকতময় সময়"
    }
}
