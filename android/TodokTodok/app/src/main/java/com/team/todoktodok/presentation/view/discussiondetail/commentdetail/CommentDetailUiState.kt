package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import com.team.domain.model.Comment
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailItems
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentItemUiState
import com.team.todoktodok.presentation.view.discussiondetail.model.ReplyItemUiState
import java.time.LocalDateTime

data class CommentDetailUiState(
    val comment: CommentItemUiState = INIT_COMMENT,
    val replies: List<ReplyItemUiState> = emptyList(),
    val content: String = "",
) {
    fun getCommentDetailItems() =
        listOf(CommentDetailItems.CommentItem(comment)) +
            replies.map {
                CommentDetailItems.ReplyItem(it)
            }

    companion object {
        val INIT_COMMENT =
            CommentItemUiState(
                Comment(
                    id = 0,
                    "",
                    User(0, Nickname("기초값"), ""),
                    LocalDateTime.of(1970, 1, 1, 0, 0),
                    0,
                    0,
                    false,
                ),
                false,
            )
    }
}
