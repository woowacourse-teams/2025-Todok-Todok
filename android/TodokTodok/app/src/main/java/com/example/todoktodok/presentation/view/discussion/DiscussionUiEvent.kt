package com.example.todoktodok.presentation.view.discussion

sealed interface DiscussionUiEvent {
    data class NavigateDiscussionRoom(
        val discussionRoomId: Long,
    ) : DiscussionUiEvent

    data object NavigateAddDiscussionRoom : DiscussionUiEvent
}
