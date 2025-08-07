package com.team.todoktodok.data.datasource.comment

import com.team.todoktodok.data.network.model.LikeAction
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse

interface CommentRemoteDataSource {
    suspend fun fetchCommentsByDiscussionRoomId(id: Long): List<CommentResponse>

    suspend fun saveComment(
        discussionId: Long,
        comment: CommentRequest,
    )

    suspend fun toggleLike(
        discussionId: Long,
        commentId: Long,
    ): LikeAction

    suspend fun updateComment(
        discussionId: Long,
        commentId: Long,
        comment: String,
    )

    suspend fun deleteComment(
        discussionId: Long,
        commentId: Long,
    )
}
