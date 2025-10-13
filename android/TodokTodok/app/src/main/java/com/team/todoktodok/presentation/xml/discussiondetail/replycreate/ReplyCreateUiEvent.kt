package com.team.todoktodok.presentation.xml.discussiondetail.replycreate

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface ReplyCreateUiEvent {
    data class SaveContent(
        val content: String,
    ) : ReplyCreateUiEvent

    data object CreateReply : ReplyCreateUiEvent

    data class ShowErrorMessage(
        val exception: TodokTodokExceptions,
    ) : ReplyCreateUiEvent
}
