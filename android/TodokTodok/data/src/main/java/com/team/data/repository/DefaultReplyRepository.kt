package com.team.data.repository

import com.team.data.datasource.reply.ReplyRemoteDataSource
import com.team.data.network.model.toStatus
import com.team.data.network.response.comment.toDomain
import com.team.domain.model.LikeStatus
import com.team.domain.model.Reply
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.map
import com.team.domain.repository.ReplyRepository
import javax.inject.Inject

class DefaultReplyRepository
    @Inject
    constructor(
        private val remoteDataSource: ReplyRemoteDataSource,
    ) : ReplyRepository {
        override suspend fun getReplies(
            discussionId: Long,
            commentId: Long,
        ): NetworkResult<List<Reply>> =
            remoteDataSource.fetchReplies(discussionId, commentId).map { replies ->
                replies.map { it.toDomain() }
            }

        override suspend fun saveReply(
            discussionId: Long,
            commentId: Long,
            content: String,
        ): NetworkResult<Unit> = remoteDataSource.saveReply(discussionId, commentId, content)

        override suspend fun toggleLike(
            discussionId: Long,
            commentId: Long,
            replyId: Long,
        ): NetworkResult<LikeStatus> = remoteDataSource.toggleLike(discussionId, commentId, replyId).map { it.toStatus() }

        override suspend fun updateReply(
            discussionId: Long,
            commentId: Long,
            replyId: Long,
            content: String,
        ): NetworkResult<Unit> = remoteDataSource.updateReply(discussionId, commentId, replyId, content)

        override suspend fun deleteReply(
            discussionId: Long,
            commentId: Long,
            replyId: Long,
        ): NetworkResult<Unit> = remoteDataSource.deleteReply(discussionId, commentId, replyId)

        override suspend fun report(
            discussionId: Long,
            commentId: Long,
            replyId: Long,
            reason: String,
        ): NetworkResult<Unit> = remoteDataSource.report(discussionId, commentId, replyId, reason)
    }
