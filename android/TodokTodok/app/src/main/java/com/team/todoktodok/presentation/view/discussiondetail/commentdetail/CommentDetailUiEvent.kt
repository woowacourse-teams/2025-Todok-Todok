package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

sealed interface CommentDetailUiEvent {
    data class ShowCommentUpdate(
        val discussionId: Long,
        val commentId: Long,
    ) : CommentDetailUiEvent

    data class ShowReplyCreate(
        val discussionId: Long,
        val commentId: Long,
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
}
