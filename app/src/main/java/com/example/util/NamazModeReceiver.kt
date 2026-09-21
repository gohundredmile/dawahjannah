package com.example.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.R

class NamazModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            NamazModeManager.deactivateNamazMode(context)

            // Post notification if supported
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            if (notificationManager != null) {
                val channelId = "namaz_mode_restore_channel"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "নামাজ মোড সমাপ্তি নোটিফিকেশন",
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = "সালাতের সময় শেষ হলে ফোনের সাউন্ড পুনরায় চালুর নোটিফিকেশন"
                    }
                    notificationManager.createNotificationChannel(channel)
                }

                val notification = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("সালাতের সময় সম্পন্ন হয়েছে")
                    .setContentText("মোবাইলের স্বাভাবিক সাউন্ড স্বয়ংক্রিয়ভাবে পুনরায় চালু করা হয়েছে।")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true)
                    .build()

                notificationManager.notify(78601, notification)
            }
        } catch (_: Exception) {
            // Safety fallback
        }
    }
}
