package com.team.core.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface MainRoute {
    fun navigateToMain(
        fromActivity: Activity,
        extraBuilder: (Intent.() -> Unit)? = null,
    )

    fun navigateToMainForNotificationResult(
        fromActivity: Activity,
        launcher: ActivityResultLauncher<Intent>,
    )
}
