package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.request.CommentRequest
import com.example.todoktodok.data.network.response.comment.CommentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("discussions/{discussionId}/comments")
    suspend fun fetchComments(
        @Header("Authorization") key: String,
        @Path("discussionId") discussionId: Long,
    ): List<CommentResponse>

    @POST("discussions/{discussionId}/comments")
    suspend fun saveComment(
        @Header("Authorization") key: String,
        @Path("discussionId") discussionId: Long,
        @Body commentRequest: CommentRequest,
    ): Response<Unit>
}
