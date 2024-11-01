package me.baggi.schedule.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import me.baggi.schedule.R

class MessagingService: FirebaseMessagingService() {
    private val descriptions = listOf(
        "Не зевай! Загляни в расписание, может, у тебя выходной!",
        "Вот бы 1 пару...",
        "Вдруг тебе на завтра проект, а ты не знаешь!"
    )
    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let {
            println(message.from)
            if (message.from == "/topics/schedule-update") {
                showNotification()
            }
        }
    }

    private fun showNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = NotificationCompat.Builder(this, "scheduler_channel_1")
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle("\uD83D\uDCC5 Расписание было обновлено!")
            .setContentText(descriptions.random())
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}