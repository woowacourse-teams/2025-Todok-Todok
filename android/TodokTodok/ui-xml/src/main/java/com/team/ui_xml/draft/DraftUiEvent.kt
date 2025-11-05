package com.team.ui_xml.draft

sealed interface DraftUiEvent {
    data class NavigateToCreateDiscussionRoom(
        val id: Long,
    ) : DraftUiEvent

    data class ShowToast(
        val error: Int,
    ) : DraftUiEvent
}
