package com.example.domain.repository

import com.example.domain.model.Comment

interface CommentRepository {
    fun getCommentsByDiscussionRoomId(id: Long): List<Comment>

    fun saveComment(comment: Comment)
}
