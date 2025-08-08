package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.request.EditDiscussionRoomRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscussionService {
    @GET("v1/discussions/{discussionId}")
    suspend fun fetchDiscussion(
        @Path("discussionId") discussionId: Long,
    ): DiscussionResponse

    @GET("v1/discussions")
    suspend fun fetchDiscussions(
        @Query("keyword") keyword: String?,
        @Query("type") type: String,
    ): List<DiscussionResponse>

    @POST("v1/discussions")
    suspend fun saveDiscussionRoom(
        @Body discussionRoomRequest: DiscussionRoomRequest,
    ): Response<Unit>

    @PATCH("v1/discussions/{discussionId}")
    suspend fun editDiscussionRoom(
        @Path("discussionId") discussionId: Long,
        @Body editDiscussionRoomRequest: EditDiscussionRoomRequest,
    ): Response<Unit>

    @DELETE("v1/discussions/{discussionId}")
    suspend fun deleteDiscussion(
        @Path("discussionId") discussionId: Long,
    )

    @POST("v1/discussions/{discussionId}/like")
    suspend fun toggleLike(
        @Path("discussionId") discussionId: Long,
    ): Response<Unit>

    @POST("v1/discussions/{discussionId}/report")
    suspend fun reportDiscussion(
        @Path("discussionId") discussionId: Long,
    )
}
