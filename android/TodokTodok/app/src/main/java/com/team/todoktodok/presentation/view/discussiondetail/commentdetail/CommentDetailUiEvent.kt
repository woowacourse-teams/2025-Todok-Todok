package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

sealed interface CommentDetailUiEvent {
    data class ShowReplyCreate(
        val discussionId: Long,
        val commentId: Long,
    ) : CommentDetailUiEvent
}
