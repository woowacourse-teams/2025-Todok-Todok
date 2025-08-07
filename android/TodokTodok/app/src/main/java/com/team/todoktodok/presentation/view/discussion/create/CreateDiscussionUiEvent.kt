package com.team.todoktodok.presentation.view.discussion.create

sealed interface CreateDiscussionUiEvent {
    data class NavigateToDiscussionDetail(
        val discussionRoomId: Long,
    ) : CreateDiscussionUiEvent

    data class ShowToast(
        val error: String,
    ) : CreateDiscussionUiEvent
}
