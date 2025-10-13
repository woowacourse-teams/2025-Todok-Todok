package com.team.todoktodok.presentation.xml.setting.manage

import com.team.domain.model.member.BlockedMember

data class ManageBlockedMembersUiState(
    val members: List<BlockedMember> = emptyList(),
    val selectedMemberId: Long = NOT_HAS_SELECTED_MEMBER,
    val isLoading: Boolean = false,
) {
    fun modifySelectedMember(memberId: Long) = copy(selectedMemberId = memberId)

    fun toggleLoading() = copy(isLoading = !isLoading)

    fun removeSelectedMember() =
        copy(
            members = members.filter { it.memberId != selectedMemberId },
            selectedMemberId = NOT_HAS_SELECTED_MEMBER,
        )

    companion object {
        const val NOT_HAS_SELECTED_MEMBER = -1L
    }
}
