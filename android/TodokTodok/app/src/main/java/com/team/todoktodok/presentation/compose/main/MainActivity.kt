package com.team.todoktodok.presentation.compose.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import com.team.todoktodok.presentation.core.ext.getParcelableCompat
import com.team.todoktodok.presentation.view.serialization.SerializationNotificationType
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.xml.serialization.SerializationDiscussion
import com.team.todoktodok.presentation.xml.serialization.SerializationFcmNotification

class MainActivity : ComponentActivity() {
    private val discussionDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let { data ->
                    when {
                        data.hasExtra(EXTRA_DELETE_DISCUSSION) -> {
                            val deletedId =
                                data.getLongExtra(
                                    EXTRA_DELETE_DISCUSSION,
                                    DEFAULT_DISCUSSION_ID,
                                )
                            if (deletedId != DEFAULT_DISCUSSION_ID) {
                            }
                        }

                        data.hasExtra(EXTRA_WATCHED_DISCUSSION) -> {
                            data
                                .getParcelableCompat<SerializationDiscussion>(
                                    EXTRA_WATCHED_DISCUSSION,
                                )?.let {
                                }
                        }
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val exceptionMessageConverter = ExceptionMessageConverter()
        setContent {
            TodoktodokTheme {
                MainScreen(
                    messageConverter = exceptionMessageConverter,
                    isUnreadNotification = false,
                )
            }
        }
        handleNotificationDeepLink(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleNotificationDeepLink(intent)
    }

    private fun handleNotificationDeepLink(intent: Intent) {
        val notification: SerializationFcmNotification? =
            intent.getParcelableCompat<SerializationFcmNotification>("notification") as? SerializationFcmNotification

        triggerToMoveDiscussionDetail(notification)
    }

    private fun MainActivity.triggerToMoveDiscussionDetail(notification: SerializationFcmNotification?) {
        if (notification != null) {
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
    }

    companion object {
        const val EXTRA_DELETE_DISCUSSION = "delete_discussion"
        const val EXTRA_WATCHED_DISCUSSION = "watched_discussion"
        private const val DEFAULT_DISCUSSION_ID = -1L

        fun Intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
