package com.team.core.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher

interface SettingRoute {
    fun navigateToSetting(fromActivity: Activity)

    fun navigateToSettingForResult(
        fromActivity: Activity,
        resultLauncher: ActivityResultLauncher<Intent>,
    )
}
