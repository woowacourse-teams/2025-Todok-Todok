package com.team.todoktodok.presentation.xml.setting

import androidx.annotation.StringRes
import com.team.todoktodok.R

enum class SettingScreen(
    @field:StringRes val toolbarTitle: Int,
) {
    SETTING_MAIN(R.string.profile_setting),
    MODIFY_PROFILE(R.string.setting_modify_profile),
    MANAGE_BLOCKED_USERS(R.string.setting_block_members),
    WITHDRAW(R.string.setting_withdraw),
}
