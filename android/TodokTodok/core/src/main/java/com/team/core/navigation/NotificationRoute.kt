package com.team.core.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface NotificationRoute {
    fun navigateToNotification(fromActivity: Activity)

    fun navigateToNotificationForResult(
        fromActivity: Activity,
        resultLauncher: ActivityResultLauncher<Intent>,
    )
}
