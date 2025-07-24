package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DiscussionService {
    @GET("discussions/{discussionId}")
    suspend fun fetchDiscussion(
        @Path("discussionId") discussionId: Long,
    ): DiscussionResponse

    @GET("discussions")
    suspend fun fetchDiscussions(): List<DiscussionResponse>
}
