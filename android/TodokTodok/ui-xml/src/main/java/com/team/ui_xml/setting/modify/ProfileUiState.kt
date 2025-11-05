package com.team.ui_xml.setting.modify

import com.team.domain.model.member.Profile

data class ProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val isNicknameChange: Boolean = false,
    val isProfileMessageChange: Boolean = false,
    val isLoading: Boolean = false,
) {
    fun toggleLoading(): ProfileUiState = copy(isLoading = !isLoading)

    fun modifyProfile(
        nickname: String,
        message: String,
    ): ProfileUiState {
        val nicknameChanged = profile.nickname != nickname
        val messageChanged = (profile.message ?: "") != message

        return copy(
            profile =
                profile.copy(
                    nickname = nickname,
                    message = message,
                ),
            isNicknameChange = nicknameChanged,
            isProfileMessageChange = messageChanged,
        )
    }
}
