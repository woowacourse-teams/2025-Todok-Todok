package com.team.todoktodok.presentation.xml.discussiondetail.commentdetail

import com.team.domain.model.exception.TodokTodokExceptions

sealed interface CommentDetailUiEvent {
    data class ShowCommentUpdate(
        val discussionId: Long,
        val commentId: Long,
        val comment: String,
    ) : CommentDetailUiEvent

    data class ShowReplyCreate(
        val discussionId: Long,
        val commentId: Long,
        val content: String,
    ) : CommentDetailUiEvent

    data class ShowReplyUpdate(
        val discussionId: Long,
        val commentId: Long,
        val replyId: Long,
        val content: String,
    ) : CommentDetailUiEvent

    data object ShowNewReply : CommentDetailUiEvent

    data object DeleteComment : CommentDetailUiEvent

    data object DeleteReply : CommentDetailUiEvent

    data object ToggleCommentLike : CommentDetailUiEvent

    data object CommentUpdate : CommentDetailUiEvent

    data class ShowError(
        val exception: TodokTodokExceptions,
    ) : CommentDetailUiEvent

    data object ShowReportCommentSuccessMessage : CommentDetailUiEvent

    data object ShowReportReplySuccessMessage : CommentDetailUiEvent

    data class NavigateToProfile(
        val memberId: Long,
    ) : CommentDetailUiEvent
}
