package com.team.domain.repository

import com.team.domain.model.LikeStatus
import com.team.domain.model.Reply
import com.team.domain.model.exception.NetworkResult

interface ReplyRepository {
    suspend fun getReplies(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<List<Reply>>

    suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    ): NetworkResult<Unit>

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): NetworkResult<LikeStatus>

    suspend fun updateReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
    ): NetworkResult<Unit>

    suspend fun deleteReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): NetworkResult<Unit>

    suspend fun report(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): NetworkResult<Unit>
}
