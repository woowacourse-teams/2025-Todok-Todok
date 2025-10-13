package com.team.todoktodok.data.datasource.reply

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.comment.ReplyResponse

interface ReplyRemoteDataSource {
    suspend fun fetchReplies(
        discussionId: Long,
        commentId: Long,
    ): NetworkResult<List<ReplyResponse>>

    suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    ): NetworkResult<Unit>

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): NetworkResult<LikeAction>

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
        reason: String,
    ): NetworkResult<Unit>
}
