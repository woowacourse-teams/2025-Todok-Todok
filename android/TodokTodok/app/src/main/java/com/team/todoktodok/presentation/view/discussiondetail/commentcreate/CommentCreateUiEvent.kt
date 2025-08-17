package com.team.todoktodok.presentation.view.discussiondetail.commentcreate

sealed interface CommentCreateUiEvent {
    data class InitState(
        val content: String,
    ) : CommentCreateUiEvent

    data object SubmitComment : CommentCreateUiEvent

    data class OnCreateDismiss(
        val content: String,
    ) : CommentCreateUiEvent
}
