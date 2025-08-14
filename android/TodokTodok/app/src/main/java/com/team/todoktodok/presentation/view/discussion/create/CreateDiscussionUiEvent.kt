package com.team.todoktodok.presentation.view.discussion.create

import com.team.todoktodok.presentation.view.book.ErrorSelectBookType

sealed interface CreateDiscussionUiEvent {
    data class NavigateToDiscussionDetail(
        val discussionRoomId: Long,
    ) : CreateDiscussionUiEvent

    data class ShowToast(
        val error: ErrorCreateDiscussionType,
    ) : CreateDiscussionUiEvent
}
