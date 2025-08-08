package com.team.todoktodok.data.datasource.reply

import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.response.comment.ReplyResponse

interface ReplyRemoteDataSource {
    suspend fun fetchReplies(
        discussionId: Long,
        commentId: Long,
    ): List<ReplyResponse>

    suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    )

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): LikeAction

    suspend fun updateReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
        content: String,
    )

    suspend fun deleteReply(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    )

    suspend fun report(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    )
}
