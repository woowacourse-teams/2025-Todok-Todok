package com.team.domain.repository

import com.team.domain.model.Comment
import com.team.domain.model.LikeStatus

interface CommentRepository {
    suspend fun getCommentsByDiscussionRoomId(id: Long): List<Comment>

    suspend fun saveComment(
        discussionId: Long,
        content: String,
    )

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): LikeStatus

    suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        content: String,
    )

    suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    )

    suspend fun report(
        discussionId: Long,
        commentId: Long,
    )
}
