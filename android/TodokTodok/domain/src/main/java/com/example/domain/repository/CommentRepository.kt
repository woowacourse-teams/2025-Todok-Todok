package com.example.domain.repository

import com.example.domain.model.Comment

interface CommentRepository {
    suspend fun getCommentsByDiscussionRoomId(id: Long): List<Comment>

    suspend fun saveComment(
        discussionId: Long,
        content: String,
    )
}
