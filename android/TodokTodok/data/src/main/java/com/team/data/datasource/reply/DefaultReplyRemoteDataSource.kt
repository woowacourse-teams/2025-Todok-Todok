package com.team.data.datasource.reply

import com.team.data.core.ext.mapToggleLikeResponse
import com.team.data.network.model.LikeAction
import com.team.data.network.request.ReplyRequest
import com.team.data.network.request.ReportRequest
import com.team.data.network.response.comment.ReplyResponse
import com.team.data.network.service.ReplyService
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.toDomain

class DefaultReplyRemoteDataSource(
    private val replyService: ReplyService,
) : ReplyRemoteDataSource {
    override suspend fun fetchReplies(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<List<ReplyResponse>> = replyService.fetchReplies(discussionId, commentId)

    override suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    ): NetworkResult<Unit> = replyService.saveReply(discussionId, commentId, ReplyRequest(content))

    override suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): NetworkResult<LikeAction> =
        runCatching {
            replyService.toggleLike(discussionId, commentId, replyId).mapToggleLikeResponse()
        }.getOrElse { NetworkResult.Failure(it.toDomain()) }

    override suspend fun updateReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
    ): NetworkResult<Unit> = replyService.updateReply(discussionId, commentId, replyId, ReplyRequest(content))

    override suspend fun deleteReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): NetworkResult<Unit> = replyService.deleteReply(discussionId, commentId, replyId)

    override suspend fun report(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        reason: String,
    ): NetworkResult<Unit> = replyService.report(discussionId, commentId, replyId, ReportRequest(reason))
}
