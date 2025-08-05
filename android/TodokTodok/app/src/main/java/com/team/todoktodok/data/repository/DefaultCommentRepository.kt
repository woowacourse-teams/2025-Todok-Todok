package com.team.todoktodok.data.repository

import com.team.domain.model.Comment
import com.team.domain.repository.CommentRepository
import com.team.todoktodok.data.datasource.comment.CommentRemoteDataSource
import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.toDomain

class DefaultCommentRepository(
    private val commentRemoteDataSource: CommentRemoteDataSource,
) : CommentRepository {
    override suspend fun getCommentsByDiscussionRoomId(id: Long): List<Comment> =
        commentRemoteDataSource
            .fetchCommentsByDiscussionRoomId(id)
            .map { it.toDomain() }
            .sortedByDescending { it.createAt }

    override suspend fun saveComment(
        discussionId: Long,
        content: String,
    ) {
        commentRemoteDataSource.saveComment(discussionId, CommentRequest(content))
    }
}
