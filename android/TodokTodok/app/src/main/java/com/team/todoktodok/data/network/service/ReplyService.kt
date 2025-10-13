package com.team.todoktodok.data.network.service

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.ReplyRequest
import com.team.todoktodok.data.network.request.ReportRequest
import com.team.todoktodok.data.network.response.comment.ReplyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ReplyService {
    @GET("v1/discussions/{discussionId}/comments/{commentId}/replies")
    suspend fun fetchReplies(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
    ): NetworkResult<List<ReplyResponse>>

    @POST("v1/discussions/{discussionId}/comments/{commentId}/replies")
    suspend fun saveReply(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Body replyRequest: ReplyRequest,
    ): NetworkResult<Unit>

    @POST("v1/discussions/{discussionId}/comments/{commentId}/replies/{replyId}/report")
    suspend fun report(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Path("replyId") replyId: Long,
        @Body report: ReportRequest,
    ): NetworkResult<Unit>

    @POST("v1/discussions/{discussionId}/comments/{commentId}/replies/{replyId}/like")
    suspend fun toggleLike(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Path("replyId") replyId: Long,
    ): Response<Unit>

    @PATCH("v1/discussions/{discussionId}/comments/{commentId}/replies/{replyId}")
    suspend fun updateReply(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Path("replyId") replyId: Long,
        @Body replyRequest: ReplyRequest,
    ): NetworkResult<Unit>

    @DELETE("v1/discussions/{discussionId}/comments/{commentId}/replies/{replyId}")
    suspend fun deleteReply(
        @Path("discussionId") discussionId: Long,
        @Path("commentId") commentId: Long,
        @Path("replyId") replyId: Long,
    ): NetworkResult<Unit>
}
