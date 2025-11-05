package com.team.ui_xml.notification.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.team.core.navigation.NotificationRoute
import com.team.ui_xml.notification.NotificationActivity
import javax.inject.Inject

class NotificationNavigation
    @Inject
    constructor() : NotificationRoute {
        override fun navigateToNotification(fromActivity: Activity) {
            val intent = NotificationActivity.Intent(fromActivity)
            fromActivity.startActivity(intent)
        }

        override fun navigateToNotificationForResult(
            fromActivity: Activity,
            resultLauncher: ActivityResultLauncher<Intent>,
        ) {
            val intent = NotificationActivity.Intent(fromActivity)
            resultLauncher.launch(intent)
        }
    }
