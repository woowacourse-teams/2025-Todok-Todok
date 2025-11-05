package com.team.ui_xml.setting

import androidx.annotation.StringRes
import com.team.ui_xml.R
import com.team.core.R as coreR

enum class SettingScreen(
    @field:StringRes val toolbarTitle: Int,
) {
    SETTING_MAIN(coreR.string.all_setting),
    MODIFY_PROFILE(R.string.setting_modify_profile),
    MANAGE_BLOCKED_USERS(R.string.setting_block_members),
    WITHDRAW(R.string.setting_withdraw),
}
