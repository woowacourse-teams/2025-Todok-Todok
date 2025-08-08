package com.team.domain.repository

import com.team.domain.model.LikeStatus
import com.team.domain.model.Reply

interface ReplyRepository {
    suspend fun fetchReplies(
        discussionId: Long,
        commentId: Long,
    ): List<Reply>

    suspend fun saveReply(
        discussionId: Long,
        commentId: Long,
        content: String,
    )

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
        replyId: Long,
    ): LikeStatus

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
