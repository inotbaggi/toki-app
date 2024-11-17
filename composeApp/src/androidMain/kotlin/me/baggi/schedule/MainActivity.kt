package me.baggi.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import me.baggi.schedule.data.DataStore
import me.baggi.schedule.ui.ScheduleApp
import me.baggi.schedule.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        createNotificationChannel()
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                DataStore.metricParams["uid"] = "NOT REGISTERED"
                return@addOnCompleteListener
            }

            val token = task.result
            DataStore.metricParams["uid"] = token
        }
        FirebaseMessaging.getInstance().subscribeToTopic("schedule-update")
            .addOnCompleteListener { task ->
                DataStore.metricParams["schedule-update-notifications"] = task.isSuccessful.toString()
            }
        setContent {
            AppTheme {
                ScheduleApp()
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "scheduler_channel_1",
                "Получение обновлений расписания",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Обновление расписания"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}