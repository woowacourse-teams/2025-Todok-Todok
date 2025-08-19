package com.team.todoktodok.data.repository

import com.team.domain.model.Comment
import com.team.domain.model.LikeStatus
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.data.network.model.toStatus
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.toDomain

class DefaultCommentRepository(
    private val commentRemoteDataSource: CommentRemoteDataSource,
) : CommentRepository {
    override suspend fun getComment(
        discussionId: Long,
        commentId: Long,
    ) = commentRemoteDataSource.fetchComment(discussionId, commentId).map { it.toDomain() }

    override suspend fun getCommentsByDiscussionId(id: Long): NetworkResult<List<Comment>> =
        commentRemoteDataSource
            .fetchCommentsByDiscussionId(id)
            .map { commentResponses ->
                commentResponses.map { it.toDomain() }.sortedBy { it.createAt }
            }

    override suspend fun saveComment(
        discussionId: Long,
        content: String,
    ) = commentRemoteDataSource.saveComment(discussionId, CommentRequest(content))

    override suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<LikeStatus> =
        commentRemoteDataSource
            .toggleLike(discussionId, commentId)
            .map { it.toStatus() }

    override suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        content: String,
    ) = commentRemoteDataSource.updateComment(discussionId, commentId, content)

    override suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    ) = commentRemoteDataSource.deleteComment(discussionId, commentId)

    override suspend fun report(
        discussionId: Long,
        commentId: Long,
    ) = commentRemoteDataSource.report(discussionId, commentId)
}
