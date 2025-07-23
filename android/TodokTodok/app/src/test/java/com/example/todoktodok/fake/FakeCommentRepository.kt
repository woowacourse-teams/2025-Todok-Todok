package com.example.todoktodok.fake

import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.todoktodok.fixture.COMMENTS

class FakeCommentRepository : CommentRepository {
    private val comments = COMMENTS.toMutableList()

    override fun getCommentsByDiscussionRoomId(id: Long): List<Comment> = comments

    override fun saveComment(comment: Comment) {
        comments.add(comment)
    }
}
