package com.example.todoktodok.data.repository

import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.todoktodok.data.datasource.CommentDataSource

class DefaultCommentRepository(
    private val commentDataSource: CommentDataSource,
) : CommentRepository {
    override fun getCommentsByDiscussionRoomId(id: Long): List<Comment> = commentDataSource.getCommentsByDiscussionRoomId(id)
}
