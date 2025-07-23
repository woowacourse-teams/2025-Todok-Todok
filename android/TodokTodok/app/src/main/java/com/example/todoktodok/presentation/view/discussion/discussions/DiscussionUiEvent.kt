package com.example.todoktodok.presentation.view.discussion.discussions

sealed interface DiscussionUiEvent {
    data class NavigateToDiscussionRoomDetail(
        val discussionRoomId: Long,
    ) : DiscussionUiEvent

    data object NavigateToAddDiscussionRoom : DiscussionUiEvent
}
