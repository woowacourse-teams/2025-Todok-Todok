package com.team.ui_compose.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.team.core.ExceptionMessageConverter
import com.team.core.extension.getParcelableCompat
import com.team.core.navigation.DiscussionDetailRoute
import com.team.core.navigation.NotificationRoute
import com.team.core.navigation.SelectBookRoute
import com.team.core.navigation.SettingRoute
import com.team.core.serialization.SerializationFcmNotification
import com.team.ui_compose.theme.TodoktodokTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var messageConverter: ExceptionMessageConverter

    @Inject
    lateinit var discussionDetailNavigation: DiscussionDetailRoute

    @Inject
    lateinit var notificationNavigation: NotificationRoute

    @Inject
    lateinit var selectBookNavigation: SelectBookRoute

    @Inject
    lateinit var settingNavigation: SettingRoute

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoktodokTheme {
                MainScreen(
                    discussionDetailNavigation = discussionDetailNavigation,
                    notificationNavigation = notificationNavigation,
                    selectBookNavigation = selectBookNavigation,
                    settingNavigation = settingNavigation,
                    messageConverter = messageConverter,
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
            intent.getParcelableCompat<SerializationFcmNotification>(KEY_NOTIFICATION) as? SerializationFcmNotification
        triggerToMoveDiscussionDetail(notification)
    }

    private fun MainActivity.triggerToMoveDiscussionDetail(notification: SerializationFcmNotification?) {
        if (notification == null) return
        discussionDetailNavigation.navigateToDiscussionDetail(
            this,
            notification.discussionId,
        )
    }

    companion object {
        const val KEY_REFRESH_NOTIFICATION = "refresh_notification"
        private const val KEY_NOTIFICATION = "notification"

        fun Intent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
