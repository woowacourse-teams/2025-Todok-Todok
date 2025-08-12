package com.team.todoktodok.presentation.view.discussiondetail.comments

sealed interface CommentsUiEvent {
    data class ShowCommentCreate(
        val discussionId: Long,
    ) : CommentsUiEvent

    data class ShowCommentUpdate(
        val discussionId: Long,
        val commentId: Long,
    ) : CommentsUiEvent

    data object ShowNewComment : CommentsUiEvent

    data object DeleteComment : CommentsUiEvent
}
