package com.team.todoktodok.presentation.view.discussiondetail.comments

sealed interface CommentsUiEvent {
    data class ShowCommentCreate(
        val discussionId: Long,
    ) : CommentsUiEvent

    data object ShowNewComment : CommentsUiEvent
}
