package com.team.todoktodok.data.datasource.comment

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.toDomain
import com.team.todoktodok.data.core.ext.mapToggleLikeResponse
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.request.ReportRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse
import com.team.todoktodok.data.network.service.CommentService
import javax.inject.Inject

class DefaultCommentRemoteDataSource
    @Inject
    constructor(
        private val commentService: CommentService,
    ) : CommentRemoteDataSource {
        override suspend fun fetchComment(
            discussionId: Long,
            commentId: Long,
        ): NetworkResult<CommentResponse> = commentService.fetchComment(discussionId, commentId)

        override suspend fun fetchCommentsByDiscussionId(id: Long): NetworkResult<List<CommentResponse>> = commentService.fetchComments(id)

        override suspend fun saveComment(
            discussionId: Long,
            comment: CommentRequest,
        ): NetworkResult<Unit> = commentService.saveComment(discussionId, comment)

        override suspend fun toggleLike(
            discussionId: Long,
            commentId: Long,
        ): NetworkResult<LikeAction> =
            runCatching {
                commentService.toggleLike(discussionId, commentId).mapToggleLikeResponse()
            }.getOrElse {
                NetworkResult.Failure(it.toDomain())
            }

        override suspend fun updateComment(
            discussionId: Long,
            commentId: Long,
            comment: String,
        ): NetworkResult<Unit> = commentService.updateComment(discussionId, commentId, CommentRequest(comment))

        override suspend fun deleteComment(
            discussionId: Long,
            commentId: Long,
        ): NetworkResult<Unit> = commentService.deleteComment(discussionId, commentId)

        override suspend fun report(
            discussionId: Long,
            commentId: Long,
            reason: String,
        ): NetworkResult<Unit> = commentService.report(discussionId, commentId, ReportRequest(reason))
    }
