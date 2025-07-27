package com.team.domain.repository

import com.team.domain.model.Comment

interface CommentRepository {
    suspend fun getCommentsByDiscussionRoomId(id: Long): List<Comment>

    suspend fun saveComment(
        discussionId: Long,
        content: String,
    )
}
