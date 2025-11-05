package com.team.ui_xml.discussiondetail.commentcreate

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface CommentCreateUiEvent {
    data class InitState(
        val content: String,
    ) : CommentCreateUiEvent

    data object SubmitComment : CommentCreateUiEvent

    data class OnCreateDismiss(
        val content: String,
    ) : CommentCreateUiEvent

    data class ShowError(
        val exception: TodokTodokExceptions,
    ) : CommentCreateUiEvent
}
