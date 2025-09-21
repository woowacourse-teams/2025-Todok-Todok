package com.team.todoktodok.presentation.xml.setting.modify

import com.team.domain.model.member.Profile

data class ProfileUiState(
    val profile: Profile = Profile.EMPTY,
    val isLoading: Boolean = false,
) {
    fun toggleLoading(): ProfileUiState = copy(isLoading = !isLoading)

    fun modifyProfile(
        nickname: String,
        message: String,
    ): ProfileUiState =
        copy(
            profile.copy(
                nickname = nickname,
                message = message,
            ),
        )
}
