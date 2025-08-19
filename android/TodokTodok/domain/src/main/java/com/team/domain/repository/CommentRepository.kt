package com.team.domain.repository

import com.team.domain.model.Comment
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult

interface CommentRepository {
    suspend fun getComment(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<Comment>

    suspend fun getCommentsByDiscussionId(id: Long): NetworkResult<List<Comment>>

    suspend fun saveComment(
        discussionId: Long,
        content: String,
    ): NetworkResult<Unit>

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<LikeStatus>

    suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        content: String,
    ): NetworkResult<Unit>

    suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<Unit>

    suspend fun report(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<Unit>
}
