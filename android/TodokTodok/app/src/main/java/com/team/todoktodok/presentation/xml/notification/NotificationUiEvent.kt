package com.team.todoktodok.presentation.xml.notification

import com.team.domain.model.exception.TodokTodokExceptions

sealed class NotificationUiEvent {
    data class NavigateToDiscussionRoom(
        val discussionRoomId: Long,
    ) : NotificationUiEvent()

    data class ShowException(
        val exception: TodokTodokExceptions,
    ) : NotificationUiEvent()
}
