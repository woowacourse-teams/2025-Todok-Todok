package com.team.todoktodok.adapter

import android.Manifest
import android.R
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team.domain.model.notification.Notification.Companion.Notification
import com.team.todoktodok.App
import com.team.todoktodok.presentation.view.discussions.DiscussionsActivity
import com.team.todoktodok.presentation.view.serialization.toSerialization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {
    private val notificationRepository by lazy { (application as App).container.repositoryModule.notificationRepository }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fId = task.result
                CoroutineScope(Dispatchers.IO).launch {
                    notificationRepository.registerPushNotification(token, fId)
                    Log.d("test", "$token")
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: return
        val body = message.notification?.body ?: message.data["body"] ?: return
        if (message.data.isNotEmpty()) {
            val notification = Notification(message.data)

            ensureChannel()

            val intent =
                DiscussionsActivity.Intent(this).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("from_notification", true)
                    putExtra("notification", notification.toSerialization())
                }

            CoroutineScope(Dispatchers.IO).launch {
                notificationRepository.saveNotification(notification)
            }

            val pendingIntent =
                PendingIntent.getActivity(
                    this,
                    notification.id.toInt(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                )

            val alert =
                NotificationCompat
                    .Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.btn_plus)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

            if (NotificationManagerCompat.from(this).areNotificationsEnabled() &&
                (
                    Build.VERSION.SDK_INT < 33 || ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS,
                    ) == PackageManager.PERMISSION_GRANTED
                )
            ) {
                NotificationManagerCompat
                    .from(this)
                    .notify(notification.id.toInt(), alert)
            }
        }
    }

    private fun ensureChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val manager = getSystemService(NotificationManager::class.java)
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    "토독토독",
                    NotificationManager.IMPORTANCE_HIGH,
                )
            manager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val CHANNEL_ID = "todoktodok_notification_channel"
    }
}
