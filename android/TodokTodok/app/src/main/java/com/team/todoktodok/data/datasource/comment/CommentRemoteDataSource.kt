package com.team.todoktodok.data.datasource.comment

import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse

interface CommentRemoteDataSource {
    suspend fun fetchCommentsByDiscussionRoomId(id: Long): List<CommentResponse>

    suspend fun saveComment(
        discussionId: Long,
        comment: CommentRequest,
    )
}
