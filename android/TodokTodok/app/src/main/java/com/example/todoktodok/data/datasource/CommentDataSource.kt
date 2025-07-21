package com.example.todoktodok.data.datasource

import com.example.domain.model.Comment

interface CommentDataSource {
    fun getCommentsByDiscussionRoomId(id: Long): List<Comment>
}