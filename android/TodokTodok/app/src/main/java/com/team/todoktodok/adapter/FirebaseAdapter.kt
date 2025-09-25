package com.team.todoktodok.adapter

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.team.domain.model.notification.FcmNotification.Companion.FcmNotification
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.discussion.DiscussionsActivity
import com.team.todoktodok.presentation.xml.serialization.toSerialization
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseAdapter : FirebaseMessagingService() {
    private val notificationRepository by lazy { (application as App).container.repositoryModule.notificationRepository }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        FirebaseInstallations.getInstance().id.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fId = task.result
                CoroutineScope(Dispatchers.IO).launch {
                    notificationRepository.registerPushNotification(token, fId)
                }
            }
        }
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.data["title"] ?: return
        val body = message.data["body"] ?: return

        if (message.data.isNotEmpty()) {
            val fcmNotification = FcmNotification(message.data)

            ensureChannel()

            val intent =
                DiscussionsActivity.Intent(this).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra(KEY_NOTIFICATION, true)
                    putExtra(KEY_NOTIFICATION_DATA, fcmNotification.toSerialization())
                }

            val pendingIntent =
                TaskStackBuilder
                    .create(this)
                    .addNextIntentWithParentStack(intent)
                    .getPendingIntent(
                        (fcmNotification.discussionId ?: 0L).hashCode(),
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                    )

            val alert =
                NotificationCompat
                    .Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification_overlay)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .build()

            if (NotificationManagerCompat.from(this).areNotificationsEnabled() &&
                (
                    Build.VERSION.SDK_INT < 33 ||
                        ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.POST_NOTIFICATIONS,
                        ) == PackageManager.PERMISSION_GRANTED
                )
            ) {
                NotificationManagerCompat
                    .from(this)
                    .notify(
                        fcmNotification.discussionId.toInt(),
                        alert,
                    )
            }
        }
    }

    private fun ensureChannel() {
        val manager = getSystemService(NotificationManager::class.java)
        val channel =
            NotificationChannel(
                CHANNEL_ID,
                APP_NAME,
                NotificationManager.IMPORTANCE_HIGH,
            )
        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val KEY_NOTIFICATION = "from_notification"
        private const val KEY_NOTIFICATION_DATA = "notification"
        private const val CHANNEL_ID = "todoktodok_notification_channel"
        private const val APP_NAME = "토독토독"
    }
}
