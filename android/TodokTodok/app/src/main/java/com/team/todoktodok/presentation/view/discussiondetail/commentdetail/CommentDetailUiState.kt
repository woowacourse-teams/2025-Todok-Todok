package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import com.team.domain.model.Comment
import com.team.domain.model.Reply
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailItems

data class CommentDetailUiState(
    val comment: Comment,
    val replies: List<Reply> = emptyList(),
) {
    fun getCommentDetailItems() =
        listOf(CommentDetailItems.CommentItem(comment)) +
            replies.map {
                CommentDetailItems.ReplyItem(it)
            }
}
