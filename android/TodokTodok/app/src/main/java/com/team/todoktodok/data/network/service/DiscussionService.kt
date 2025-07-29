package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.request.DiscussionRequest
import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface DiscussionService {
    @GET("v1/discussions/{discussionId}")
    suspend fun fetchDiscussion(
        @Path("discussionId") discussionId: Long,
    ): DiscussionResponse

    @GET("v1/discussions")
    suspend fun fetchDiscussions(): List<DiscussionResponse>

    @POST("v2/discussions")
    suspend fun saveDiscussion(
        @Body request: DiscussionRequest,
    ): Response<Unit>

    @POST("v2/discussions")
    suspend fun saveDiscussionRoom(
        @Body request: DiscussionRoomRequest,
    )
}
