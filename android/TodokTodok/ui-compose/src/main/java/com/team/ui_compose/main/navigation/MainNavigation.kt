package com.team.ui_compose.main.navigation

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.team.core.navigation.MainRoute
import com.team.ui_compose.main.MainActivity
import com.team.ui_compose.main.MainActivity.Companion.KEY_REFRESH_NOTIFICATION
import javax.inject.Inject

class MainNavigation
    @Inject
    constructor() : MainRoute {
        override fun navigateToMain(
            fromActivity: Activity,
            extraBuilder: (Intent.() -> Unit)?,
        ) {
            val intent =
                MainActivity.Intent(fromActivity).apply {
                    extraBuilder?.invoke(this)
                }
            fromActivity.startActivity(intent)
            fromActivity.finish()
        }

        override fun navigateToMainForNotificationResult(
            fromActivity: Activity,
            launcher: ActivityResultLauncher<Intent>,
        ) {
            val intent =
                Intent().apply {
                    putExtra(KEY_REFRESH_NOTIFICATION, true)
                }
            fromActivity.setResult(RESULT_OK, intent)
            fromActivity.finish()
        }
    }
