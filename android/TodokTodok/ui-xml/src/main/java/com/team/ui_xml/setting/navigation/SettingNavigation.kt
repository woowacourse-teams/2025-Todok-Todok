package com.team.ui_xml.setting.navigation

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import com.team.core.navigation.SettingRoute
import com.team.ui_xml.setting.SettingActivity
import javax.inject.Inject

class SettingNavigation
    @Inject
    constructor() : SettingRoute {
        override fun navigateToSetting(fromActivity: Activity) {
            val intent = SettingActivity.Intent(fromActivity)
            fromActivity.startActivity(intent)
        }

        override fun navigateToSettingForResult(
            fromActivity: Activity,
            resultLauncher: ActivityResultLauncher<Intent>,
        ) {
            val intent = SettingActivity.Intent(fromActivity)
            resultLauncher.launch(intent)
        }
    }
