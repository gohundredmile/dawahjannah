package com.example.util

import android.content.Context
import android.media.AudioManager
import android.util.Log
import com.example.data.datasource.MosqueDataCatalog
import com.example.data.model.MosqueAnnouncement
import com.example.data.model.MosqueSalahStatus
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object MosqueModeManager {
    private const val TAG = "MosqueModeManager"
    private const val PREFS_NAME = "mosque_mode_prefs"

    private const val KEY_MOSQUE_MODE_ACTIVE = "mosque_mode_active"
    private const val KEY_TRANQUIL_OLED = "tranquil_oled_mode"
    private const val KEY_KEEP_SCREEN_ON = "keep_screen_on_in_mosque"
    private const val KEY_CUSTOM_MOSQUE_NAME = "custom_mosque_name"
    private const val KEY_CUSTOM_MOSQUE_AREA = "custom_mosque_area"
    private const val KEY_JUMUAH_KHUTBAH = "jumuah_khutbah_time"
    private const val KEY_JUMUAH_JAMAT = "jumuah_jamat_time"
    private const val KEY_CUSTOM_ANNOUNCEMENTS_JSON = "custom_announcements_json"
    private const val KEY_DONATIONS_LOG_JSON = "donations_log_json"

    private fun getTodayDateKey(): String {
        return SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())
    }

    // --- State Persistence ---

    fun isMosqueModeActive(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_MOSQUE_MODE_ACTIVE, false)
    }

    fun setMosqueModeActive(context: Context, active: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_MOSQUE_MODE_ACTIVE, active).apply()
        if (active) {
            // Check silent mode
            if (!NamazModeManager.isNamazModeActive(context)) {
                val duration = NamazModeManager.getSilentDurationMins(context).coerceAtLeast(30)
                val vib = NamazModeManager.isVibrationEnabled(context)
                NamazModeManager.activateNamazMode(context, duration, vib)
            }
        } else {
            // When exiting mosque mode, offer restoring ringer if active
            NamazModeManager.deactivateNamazMode(context)
        }
    }

    fun isTranquilOledMode(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_TRANQUIL_OLED, false)
    }

    fun setTranquilOledMode(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_TRANQUIL_OLED, enabled).apply()
    }

    fun isKeepScreenOn(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_KEEP_SCREEN_ON, false)
    }

    fun setKeepScreenOn(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_KEEP_SCREEN_ON, enabled).apply()
    }

    // --- Custom Mosque Metadata ---

    fun getMosqueName(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CUSTOM_MOSQUE_NAME, "বায়তুল মুকাররম জাতীয় মসজিদ") ?: "বায়তুল মুকাররম জাতীয় মসজিদ"
    }

    fun setMosqueName(context: Context, name: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_MOSQUE_NAME, name.trim()).apply()
    }

    fun getMosqueArea(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_CUSTOM_MOSQUE_AREA, "পল্টন, ঢাকা") ?: "পল্টন, ঢাকা"
    }

    fun setMosqueArea(context: Context, area: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_MOSQUE_AREA, area.trim()).apply()
    }

    fun getJumuahKhutbahTime(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_JUMUAH_KHUTBAH, "১২:৪৫ PM") ?: "১২:৪৫ PM"
    }

    fun setJumuahKhutbahTime(context: Context, time: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_JUMUAH_KHUTBAH, time.trim()).apply()
    }

    fun getJumuahJamatTime(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_JUMUAH_JAMAT, "০১:১৫ PM") ?: "০১:১৫ PM"
    }

    fun setJumuahJamatTime(context: Context, time: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_JUMUAH_JAMAT, time.trim()).apply()
    }

    // --- Daily Prayer Tracking for Mosque Mode ---

    fun getSalahStatus(context: Context, waqtKey: String): MosqueSalahStatus {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayKey = getTodayDateKey()
        val storedName = prefs.getString("salah_${todayKey}_${waqtKey}", MosqueSalahStatus.NOT_RECORDED.name)
        return try {
            MosqueSalahStatus.valueOf(storedName ?: MosqueSalahStatus.NOT_RECORDED.name)
        } catch (_: Exception) {
            MosqueSalahStatus.NOT_RECORDED
        }
    }

    fun setSalahStatus(context: Context, waqtKey: String, status: MosqueSalahStatus) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val todayKey = getTodayDateKey()
        prefs.edit().putString("salah_${todayKey}_${waqtKey}", status.name).apply()
    }

    fun getAllTodaySalahStatuses(context: Context): Map<String, MosqueSalahStatus> {
        val waqts = listOf("FAJR", "DHUHR", "ASR", "MAGHRIB", "ISHA", "TAHAJJUD", "ISHRAQ")
        return waqts.associateWith { getSalahStatus(context, it) }
    }

    // --- Custom Community Announcements ---

    fun getAllAnnouncements(context: Context): List<MosqueAnnouncement> {
        val defaultList = MosqueDataCatalog.announcements
        val customList = getCustomAnnouncements(context)
        return customList + defaultList
    }

    fun getCustomAnnouncements(context: Context): List<MosqueAnnouncement> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonStr = prefs.getString(KEY_CUSTOM_ANNOUNCEMENTS_JSON, "[]") ?: "[]"
        val result = mutableListOf<MosqueAnnouncement>()
        try {
            val arr = JSONArray(jsonStr)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                result.add(
                    MosqueAnnouncement(
                        id = obj.optString("id", "custom_${System.currentTimeMillis()}"),
                        titleBn = obj.optString("titleBn", ""),
                        categoryBn = obj.optString("categoryBn", "নোটিশ"),
                        dateBn = obj.optString("dateBn", "আজকের ঘোষণা"),
                        descriptionBn = obj.optString("descriptionBn", ""),
                        isUrgent = obj.optBoolean("isUrgent", false),
                        locationBn = obj.optString("locationBn", "স্থানীয় মসজিদ"),
                        organizerBn = obj.optString("organizerBn", "মসজিদ কমিটি")
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing custom announcements", e)
        }
        return result
    }

    fun addCustomAnnouncement(context: Context, announcement: MosqueAnnouncement) {
        val currentList = getCustomAnnouncements(context).toMutableList()
        currentList.add(0, announcement)
        val arr = JSONArray()
        for (item in currentList) {
            val obj = JSONObject().apply {
                put("id", item.id)
                put("titleBn", item.titleBn)
                put("categoryBn", item.categoryBn)
                put("dateBn", item.dateBn)
                put("descriptionBn", item.descriptionBn)
                put("isUrgent", item.isUrgent)
                put("locationBn", item.locationBn)
                put("organizerBn", item.organizerBn)
            }
            arr.put(obj)
        }
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CUSTOM_ANNOUNCEMENTS_JSON, arr.toString()).apply()
    }

    // --- Charity Donation Logging ---

    fun recordDonation(context: Context, fundId: String, amountTaka: Long): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val key = "fund_donated_$fundId"
        val current = prefs.getLong(key, 0L)
        val updated = current + amountTaka
        prefs.edit().putLong(key, updated).apply()
        return updated
    }

    fun getFundUserDonation(context: Context, fundId: String): Long {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getLong("fund_donated_$fundId", 0L)
    }
}
