package com.team.data.network.service

import com.team.data.network.request.CommentRequest
import com.team.data.network.request.ReportRequest
import com.team.data.network.response.comment.CommentResponse
import com.team.domain.model.exception.NetworkResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentService {
    @GET("v1/discussions/{discussionId}/comments/{commentId}")
    suspend fun fetchComment(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
    ): NetworkResult<CommentResponse>

    @GET("v1/discussions/{discussionId}/comments")
    suspend fun fetchComments(
        @Path("discussionId") discussionId: Long,
    ): NetworkResult<List<CommentResponse>>

    @POST("v1/discussions/{discussionId}/comments")
    suspend fun saveComment(
        @Path("discussionId") discussionId: Long,
        @Body commentRequest: CommentRequest,
    ): NetworkResult<Unit>

    @POST("v1/discussions/{discussionId}/comments/{commentId}/report")
    suspend fun report(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Body report: ReportRequest,
    ): NetworkResult<Unit>

    @POST("v1/discussions/{discussionId}/comments/{commentId}/like")
    suspend fun toggleLike(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
    ): Response<Unit>

    @PATCH("v1/discussions/{discussionId}/comments/{commentId}")
    suspend fun updateComment(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Body commentRequest: CommentRequest,
    ): NetworkResult<Unit>

    @DELETE("v1/discussions/{discussionId}/comments/{commentId}")
    suspend fun deleteComment(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
    ): NetworkResult<Unit>
}
