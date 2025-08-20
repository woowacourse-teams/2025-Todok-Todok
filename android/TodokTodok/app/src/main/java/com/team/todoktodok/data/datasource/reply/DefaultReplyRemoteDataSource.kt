package com.team.todoktodok.data.datasource.reply

import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.toDomain
import com.team.todoktodok.data.core.ext.mapToggleLikeResponse
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.ReplyRequest
import com.team.todoktodok.data.network.response.comment.ReplyResponse
import com.team.todoktodok.data.network.service.ReplyService

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
    ): NetworkResult<Unit> = replyService.report(discussionId, commentId, replyId)
}
