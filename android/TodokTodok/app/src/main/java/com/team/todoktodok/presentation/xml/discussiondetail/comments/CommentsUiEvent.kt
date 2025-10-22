package com.team.todoktodok.presentation.xml.discussiondetail.comments

import com.team.domain.model.exception.TodokTodokExceptions
import com.team.todoktodok.presentation.xml.discussiondetail.DiscussionDetailUiEvent

sealed interface CommentsUiEvent {
    data class ShowCommentCreate(
        val discussionId: Long,
        val content: String,
    ) : CommentsUiEvent

    data class ShowCommentUpdate(
        val discussionId: Long,
        val commentId: Long,
        val content: String,
    ) : CommentsUiEvent

    data object ShowNewComment : CommentsUiEvent

    data object DeleteComment : CommentsUiEvent

    data class ShowError(
        val exception: TodokTodokExceptions,
    ) : CommentsUiEvent

    data object ShowReportCommentSuccessMessage : CommentsUiEvent

    data class NavigateToProfile(
        val memberId: Long,
    ) : CommentsUiEvent
}
