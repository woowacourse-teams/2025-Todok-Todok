package com.team.todoktodok.presentation.view.discussion.create

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
}
