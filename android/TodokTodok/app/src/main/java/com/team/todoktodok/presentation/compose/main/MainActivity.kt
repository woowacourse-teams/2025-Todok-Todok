package com.team.todoktodok.presentation.compose.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.main.vm.MainViewModel
import com.team.todoktodok.presentation.compose.main.vm.MainViewModelFactory
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.view.serialization.SerializationNotificationType
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.serialization.SerializationFcmNotification

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by lazy {
        MainViewModelFactory((applicationContext as App).container).create(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoktodokTheme {
                val messageConverter = ExceptionMessageConverter()
                MainScreen(
                    messageConverter = messageConverter,
                    askPermission = ::askNotificationPermission,
                    viewModel = viewModel
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationDeepLink(intent)
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            viewModel.sendPushNotificationToken()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                viewModel.sendPushNotificationToken()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            viewModel.sendPushNotificationToken()
        }
    }

    private fun handleNotificationDeepLink(intent: Intent) {
        val notification: SerializationFcmNotification? =
            intent.getParcelableCompat<SerializationFcmNotification>(KEY_NOTIFICATION) as? SerializationFcmNotification

        triggerToMoveDiscussionDetail(notification)
    }

    private fun MainActivity.triggerToMoveDiscussionDetail(notification: SerializationFcmNotification?) {
        if (notification == null) return
        when (notification.type) {
            SerializationNotificationType.LIKE -> {
                val detailIntent =
                    DiscussionDetailActivity.Companion.Intent(
                        this,
                        notification.discussionId,
                    )
                startActivity(detailIntent)
            }

            SerializationNotificationType.COMMENT -> {
                val detailIntent =
                    DiscussionDetailActivity.Companion.Intent(
                        this,
                        notification.discussionId,
                    )
                startActivity(detailIntent)
            }

            SerializationNotificationType.REPLY -> {
                val detailIntent =
                    DiscussionDetailActivity.Companion.Intent(
                        this,
                        notification.discussionId,
                    )
                startActivity(detailIntent)
            }
        }
    }

    companion object {
        const val KEY_REFRESH_NOTIFICATION = "refresh_notification"
        private const val KEY_NOTIFICATION = "notification"

        fun Intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
