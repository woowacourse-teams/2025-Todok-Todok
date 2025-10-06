package com.team.todoktodok.presentation.compose.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.view.serialization.SerializationNotificationType
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.serialization.SerializationFcmNotification

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoktodokTheme {
                val messageConverter = ExceptionMessageConverter()
                MainScreen(messageConverter)
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationDeepLink(intent)
    }

    private fun handleNotificationDeepLink(intent: Intent) {
        val notification: SerializationFcmNotification =
            intent.getParcelableCompat<SerializationFcmNotification>(KEY_NOTIFICATION)

        triggerToMoveDiscussionDetail(notification)
    }

    private fun MainActivity.triggerToMoveDiscussionDetail(notification: SerializationFcmNotification) {
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
        private const val KEY_NOTIFICATION = "notification"

        fun Intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
