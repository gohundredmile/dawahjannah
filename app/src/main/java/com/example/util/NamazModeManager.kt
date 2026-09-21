package com.example.util

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log

object NamazModeManager {
    private const val TAG = "NamazModeManager"
    private const val PREFS_NAME = "namaz_mode_preferences"
    private const val KEY_AUTO_SILENT = "key_auto_silent"
    private const val KEY_VIBRATION = "key_vibration"
    private const val KEY_AZAN_ALERT = "key_azan_alert"
    private const val KEY_DURATION_MINS = "key_duration_mins"
    private const val KEY_ACTIVE_UNTIL = "key_active_until_ms"
    private const val KEY_ORIGINAL_RINGER = "key_original_ringer_mode"

    private const val ACTION_RESTORE_RINGER = "com.dawahtojannah.app.ACTION_RESTORE_RINGER"
    private const val REQUEST_CODE_ALARM = 8801

    /**
     * Checks if the app has permission to adjust Do Not Disturb (DND) / Silent mode.
     * On Android M (API 23)+, this requires Notification Policy Access.
     */
    fun hasDndPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            notificationManager?.isNotificationPolicyAccessGranted == true
        } else {
            true
        }
    }

    /**
     * Opens the system settings screen for granting Do Not Disturb (DND) permission.
     */
    fun openDndSettings(context: Context): Boolean {
        return try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                true
            } else {
                val intent = Intent(Settings.ACTION_SOUND_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to open DND settings", e)
            try {
                val fallbackIntent = Intent(Settings.ACTION_SETTINGS).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(fallbackIntent)
                true
            } catch (_: Exception) {
                false
            }
        }
    }

    /**
     * Gets current ringer mode of the device:
     * - AudioManager.RINGER_MODE_NORMAL (2)
     * - AudioManager.RINGER_MODE_VIBRATE (1)
     * - AudioManager.RINGER_MODE_SILENT (0)
     */
    fun getCurrentRingerMode(context: Context): Int {
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
        return audioManager?.ringerMode ?: AudioManager.RINGER_MODE_NORMAL
    }

    fun getRingerModeTitleBn(ringerMode: Int): String {
        return when (ringerMode) {
            AudioManager.RINGER_MODE_SILENT -> "সম্পূর্ণ সাইলেন্ট (নিঃশব্দ)"
            AudioManager.RINGER_MODE_VIBRATE -> "ভাইব্রেশন মোড (কম্পন)"
            else -> "সাউন্ড অন (স্বাভাবিক)"
        }
    }

    // --- Preferences ---

    fun isAutoSilentEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AUTO_SILENT, true)
    }

    fun setAutoSilentEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_AUTO_SILENT, enabled).apply()
    }

    fun isVibrationEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_VIBRATION, true)
    }

    fun setVibrationEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_VIBRATION, enabled).apply()
    }

    fun isAzanAlertEnabled(context: Context): Boolean {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(KEY_AZAN_ALERT, true)
    }

    fun setAzanAlertEnabled(context: Context, enabled: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(KEY_AZAN_ALERT, enabled).apply()
    }

    fun getSilentDurationMins(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(KEY_DURATION_MINS, 30)
    }

    fun setSilentDurationMins(context: Context, mins: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putInt(KEY_DURATION_MINS, mins).apply()
    }

    fun isNamazModeActive(context: Context): Boolean {
        checkAndAutoRestore(context)
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val activeUntil = prefs.getLong(KEY_ACTIVE_UNTIL, 0L)
        return activeUntil > System.currentTimeMillis()
    }

    fun getRemainingMinutes(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val activeUntil = prefs.getLong(KEY_ACTIVE_UNTIL, 0L)
        val remainingMs = activeUntil - System.currentTimeMillis()
        return if (remainingMs > 0) ((remainingMs / 60000L) + 1).toInt() else 0
    }

    // --- Actions ---

    /**
     * Activates Namaz Silent Mode on the device hardware.
     * @return Result.success if changed successfully, Result.failure if permission missing or failed.
     */
    fun activateNamazMode(context: Context, durationMins: Int, useVibration: Boolean): Result<Unit> {
        if (!hasDndPermission(context)) {
            return Result.failure(SecurityException("DND_PERMISSION_REQUIRED"))
        }

        return try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
                ?: return Result.failure(IllegalStateException("Audio service unavailable"))

            val currentMode = audioManager.ringerMode
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

            // Save original mode if not already active
            if (!isNamazModeActive(context)) {
                prefs.edit().putInt(KEY_ORIGINAL_RINGER, currentMode).apply()
            }

            // Set new ringer mode
            val targetMode = if (useVibration) {
                AudioManager.RINGER_MODE_VIBRATE
            } else {
                AudioManager.RINGER_MODE_SILENT
            }
            audioManager.ringerMode = targetMode

            // Schedule auto restore
            val activeUntil = System.currentTimeMillis() + (durationMins * 60 * 1000L)
            prefs.edit()
                .putLong(KEY_ACTIVE_UNTIL, activeUntil)
                .putInt(KEY_DURATION_MINS, durationMins)
                .apply()

            scheduleRestoreAlarm(context, activeUntil)

            // Haptic feedback to confirm activation
            VibrationHelper.vibrate(context, 75)

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to activate namaz mode", e)
            Result.failure(e)
        }
    }

    /**
     * Restores device to normal ringer mode.
     */
    fun deactivateNamazMode(context: Context): Boolean {
        return try {
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val originalMode = prefs.getInt(KEY_ORIGINAL_RINGER, AudioManager.RINGER_MODE_NORMAL)

            // Cancel scheduled alarm
            cancelRestoreAlarm(context)

            // Restore ringer
            if (hasDndPermission(context) && audioManager != null) {
                audioManager.ringerMode = if (originalMode == AudioManager.RINGER_MODE_SILENT) {
                    AudioManager.RINGER_MODE_NORMAL
                } else {
                    originalMode
                }
            } else if (audioManager != null) {
                try {
                    audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
                } catch (_: Exception) {
                    // Fallback
                }
            }

            // Clear active timestamp
            prefs.edit().remove(KEY_ACTIVE_UNTIL).apply()

            // Confirm haptic
            VibrationHelper.vibrate(context, 40)
            true
        } catch (e: Exception) {
            Log.e(TAG, "Failed to deactivate namaz mode", e)
            false
        }
    }

    fun testVibration(context: Context) {
        VibrationHelper.vibrateGoalReached(context)
    }

    fun testSoundAlert(context: Context) {
        try {
            val notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, notificationUri)
            ringtone?.play()
        } catch (_: Exception) {
            // Fallback haptic
            VibrationHelper.vibrate(context, 100)
        }
    }

    fun checkAndAutoRestore(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val activeUntil = prefs.getLong(KEY_ACTIVE_UNTIL, 0L)
        if (activeUntil > 0L && System.currentTimeMillis() >= activeUntil) {
            deactivateNamazMode(context)
        }
    }

    private fun scheduleRestoreAlarm(context: Context, triggerAtMillis: Long) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, NamazModeReceiver::class.java).apply {
                action = ACTION_RESTORE_RINGER
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_ALARM,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not schedule restore alarm", e)
        }
    }

    private fun cancelRestoreAlarm(context: Context) {
        try {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager ?: return
            val intent = Intent(context, NamazModeReceiver::class.java).apply {
                action = ACTION_RESTORE_RINGER
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_ALARM,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        } catch (_: Exception) {
        }
    }
}
