package com.team.todoktodok.data.repository

import com.team.domain.model.LikeStatus
import com.team.domain.model.Reply
import com.team.domain.repository.ReplyRepository
import com.team.todoktodok.data.datasource.reply.DefaultReplyRemoteDataSource
import com.team.todoktodok.data.network.model.toStatus
import com.team.todoktodok.data.network.response.comment.toDomain

class DefaultReplyRepository(
    private val remoteDataSource: DefaultReplyRemoteDataSource,
) : ReplyRepository {
    override suspend fun fetchReplies(
        discussionId: Long,
        commentId: Long,
    ): List<Reply> = remoteDataSource.fetchReplies(discussionId, commentId).map { it.toDomain() }

    override suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    ) {
        remoteDataSource.saveReply(discussionId, commentId, content)
    }

    override suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): LikeStatus = remoteDataSource.toggleLike(discussionId, commentId, replyId).toStatus()

    override suspend fun updateReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
    ) {
        remoteDataSource.updateReply(discussionId, commentId, replyId, content)
    }

    override suspend fun deleteReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ) {
        remoteDataSource.deleteReply(discussionId, commentId, replyId)
    }

    override suspend fun report(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ) {
        remoteDataSource.report(discussionId, commentId, replyId)
    }
}
