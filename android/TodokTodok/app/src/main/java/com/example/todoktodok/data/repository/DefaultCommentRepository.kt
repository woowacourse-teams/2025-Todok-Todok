package com.example.todoktodok.data.repository

import com.example.domain.model.Comment
import com.example.domain.repository.CommentRepository
import com.example.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.example.todoktodok.data.network.request.CommentRequest
import com.example.todoktodok.data.network.response.comment.toDomain

class DefaultCommentRepository(
    private val commentRemoteDataSource: CommentRemoteDataSource,
) : CommentRepository {
    override suspend fun getCommentsByDiscussionRoomId(id: Long): List<Comment> =
        commentRemoteDataSource.fetchCommentsByDiscussionRoomId(id).map { it.toDomain() }

    override suspend fun saveComment(
        discussionId: Long,
        content: String,
    ) {
        commentRemoteDataSource.saveComment(discussionId, CommentRequest(content))
    }
}
