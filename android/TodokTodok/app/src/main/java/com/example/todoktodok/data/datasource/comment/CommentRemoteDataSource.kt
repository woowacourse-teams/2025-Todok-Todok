package com.example.todoktodok.data.datasource.comment

import com.example.todoktodok.data.network.request.CommentRequest
import com.example.todoktodok.data.network.response.comment.CommentResponse

interface CommentRemoteDataSource {
    suspend fun fetchCommentsByDiscussionRoomId(id: Long): List<CommentResponse>

    suspend fun saveComment(
        discussionId: Long,
        comment: CommentRequest,
    )
}
