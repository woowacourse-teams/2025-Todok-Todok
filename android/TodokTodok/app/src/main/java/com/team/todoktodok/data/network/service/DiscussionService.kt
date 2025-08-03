package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscussionService {
    @GET("v1/discussions/{discussionId}")
    suspend fun fetchDiscussion(
        @Path("discussionId") discussionId: Long,
    ): DiscussionResponse

    @GET("v2/discussions")
    suspend fun fetchDiscussions(
        @Query("keyword") keyword: String?,
        @Query("type") type: String,
    ): List<DiscussionResponse>
}
