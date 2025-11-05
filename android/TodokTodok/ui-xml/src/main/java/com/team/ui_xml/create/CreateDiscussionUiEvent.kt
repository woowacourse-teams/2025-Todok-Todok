package com.team.ui_xml.create

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface CreateDiscussionUiEvent {
    data class NavigateToDiscussionDetail(
        val discussionRoomId: Long,
        val mode: SerializationCreateDiscussionRoomMode,
    ) : CreateDiscussionUiEvent

    data class ShowToast(
        val error: ErrorCreateDiscussionType,
    ) : CreateDiscussionUiEvent

    data class SaveDraft(
        val possible: Boolean,
    ) : CreateDiscussionUiEvent

    data object Finish : CreateDiscussionUiEvent

    data class ShowNetworkErrorMessage(
        val exception: TodokTodokExceptions,
    ) : CreateDiscussionUiEvent
}
