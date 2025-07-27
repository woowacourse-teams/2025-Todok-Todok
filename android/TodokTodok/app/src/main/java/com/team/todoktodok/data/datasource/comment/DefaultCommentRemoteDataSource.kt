package com.team.todoktodok.data.datasource.comment

import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse
import com.team.todoktodok.data.network.service.CommentService

class DefaultCommentRemoteDataSource(
    private val commentService: CommentService,
) : CommentRemoteDataSource {
    override suspend fun fetchCommentsByDiscussionRoomId(id: Long): List<CommentResponse> = commentService.fetchComments(id)

    override suspend fun saveComment(
        discussionId: Long,
        comment: CommentRequest,
    ) {
        commentService.saveComment(discussionId, comment)
    }
}
