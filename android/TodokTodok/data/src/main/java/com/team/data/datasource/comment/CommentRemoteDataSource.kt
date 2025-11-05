package com.team.data.datasource.comment

import com.team.data.network.model.LikeAction
import com.team.data.network.request.CommentRequest
import com.team.data.network.response.comment.CommentResponse
import com.team.domain.model.exception.NetworkResult

interface CommentRemoteDataSource {
    suspend fun fetchComment(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<CommentResponse>

    suspend fun fetchCommentsByDiscussionId(id: Long): NetworkResult<List<CommentResponse>>

    suspend fun saveComment(
        discussionId: Long,
        comment: CommentRequest,
    ): NetworkResult<Unit>

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<LikeAction>

    suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        comment: String,
    ): NetworkResult<Unit>

    suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<Unit>

    suspend fun report(
        discussionId: Long,
        commentId: Long,
        reason: String,
    ): NetworkResult<Unit>
}
