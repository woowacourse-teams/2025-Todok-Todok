package com.team.todoktodok.presentation.view.discussiondetail.commentdetail

import com.team.domain.model.Comment
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.User
import com.team.todoktodok.presentation.view.discussiondetail.commentdetail.adapter.CommentDetailItems
import com.team.todoktodok.presentation.view.discussiondetail.model.CommentUiModel
import com.team.todoktodok.presentation.view.discussiondetail.model.ReplyUiModel
import java.time.LocalDateTime

data class CommentDetailUiState(
    val comment: CommentUiModel = INIT_COMMENT,
    val replies: List<ReplyUiModel> = emptyList(),
) {
    fun getCommentDetailItems() =
        listOf(CommentDetailItems.CommentItem(comment)) +
            replies.map {
                CommentDetailItems.ReplyItem(it)
            }

    companion object {
        val INIT_COMMENT =
            CommentUiModel(
                Comment(
                    id = 0,
                    "",
                    User(0, Nickname("기초값")),
                    LocalDateTime.of(1970, 1, 1, 0, 0),
                    0,
                    0,
                    false,
                ),
                false,
            )
    }
}
