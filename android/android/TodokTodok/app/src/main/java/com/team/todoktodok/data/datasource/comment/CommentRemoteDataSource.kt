package com.team.todoktodok.data.datasource.comment

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse

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
