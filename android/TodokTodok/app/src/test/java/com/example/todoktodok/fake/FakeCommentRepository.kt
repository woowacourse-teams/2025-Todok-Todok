package com.example.todoktodok.fake

import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.todoktodok.COMMENTS

class FakeCommentRepository : CommentRepository {
    private val comments = COMMENTS

    override fun getCommentsByDiscussionRoomId(id: Long): List<Comment> = COMMENTS
}