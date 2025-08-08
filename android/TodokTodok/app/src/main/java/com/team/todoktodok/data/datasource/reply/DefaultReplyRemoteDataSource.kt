package com.team.todoktodok.data.datasource.reply

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
    ): List<ReplyResponse> = replyService.fetchReplies(discussionId, commentId)

    override suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    ) {
        replyService.saveReply(discussionId, commentId, ReplyRequest(content))
    }

    override suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): LikeAction =
        when (replyService.toggleLike(discussionId, commentId, replyId).code()) {
            201 -> LikeAction.LIKE
            204 -> LikeAction.UNLIKE
            else -> throw IllegalStateException()
        }

    override suspend fun updateReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
    ) {
        replyService.updateReply(discussionId, commentId, replyId, ReplyRequest(content))
    }

    override suspend fun deleteReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ) {
        replyService.deleteReply(discussionId, commentId, replyId)
    }

    override suspend fun report(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ) {
        replyService.report(discussionId, commentId, replyId)
    }
}
