package com.team.todoktodok.presentation.view.discussion.create

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface CreateDiscussionUiEvent {
    data class NavigateToDiscussionDetail(
        val discussionRoomId: Long,
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
