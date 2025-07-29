package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.request.CommentRequest
import com.team.todoktodok.data.network.response.comment.CommentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("v1/discussions/{discussionId}/comments")
    suspend fun fetchComments(
        @Path("discussionId") discussionId: Long,
    ): List<CommentResponse>

    @POST("v1/discussions/{discussionId}/comments")
    suspend fun saveComment(
        @Path("discussionId") discussionId: Long,
        @Body commentRequest: CommentRequest,
    ): Response<Unit>
}
