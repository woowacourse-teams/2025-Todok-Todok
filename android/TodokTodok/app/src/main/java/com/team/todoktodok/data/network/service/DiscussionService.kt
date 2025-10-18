package com.team.todoktodok.data.network.service

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.DiscussionRoomRequest
import com.team.todoktodok.data.network.request.EditDiscussionRoomRequest
import com.team.todoktodok.data.network.request.ReportRequest
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.response.discussion.liked.LikedDiscussionPageResponse
import com.team.todoktodok.data.network.response.discussion.page.ActiveDiscussionPageResponse
import com.team.todoktodok.data.network.response.latest.LatestDiscussionsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface DiscussionService {
    @GET("v1/discussions/search")
    suspend fun fetchSearchDiscussions(
        @Query("keyword") query: String,
    ): NetworkResult<List<DiscussionResponse>>

    @GET("v1/discussions/active")
    suspend fun fetchActivatedDiscussions(
        @Query("period") period: Int,
        @Query("size") size: Int,
        @Query("cursor") cursor: String?,
    ): NetworkResult<ActiveDiscussionPageResponse>

    @GET("v1/discussions/liked")
    suspend fun fetchLikedDiscussions(
        @Query("size") size: Int,
        @Query("cursor") cursor: String?,
    ): NetworkResult<LikedDiscussionPageResponse>

    @GET("v1/discussions/hot")
    suspend fun fetchHotDiscussions(
        @Query("period") period: Int,
        @Query("count") count: Int,
    ): NetworkResult<List<DiscussionResponse>>

    @GET("v1/discussions")
    suspend fun fetchLatestDiscussions(
        @Query("size") size: Int,
        @Query("cursor") cursor: String?,
    ): NetworkResult<LatestDiscussionsResponse>

    @GET("v1/discussions/{discussionId}")
    suspend fun fetchDiscussion(
        @Path("discussionId") discussionId: Long,
    ): NetworkResult<DiscussionResponse>

    @POST("v1/discussions")
    suspend fun saveDiscussionRoom(
        @Body discussionRoomRequest: DiscussionRoomRequest,
    ): Response<Unit>

    @PATCH("v1/discussions/{discussionId}")
    suspend fun editDiscussionRoom(
        @Path("discussionId") discussionId: Long,
        @Body editDiscussionRoomRequest: EditDiscussionRoomRequest,
    ): NetworkResult<Unit>

    @DELETE("v1/discussions/{discussionId}")
    suspend fun deleteDiscussion(
        @Path("discussionId") discussionId: Long,
    ): NetworkResult<Unit>

    @POST("v1/discussions/{discussionId}/like")
    suspend fun toggleLike(
        @Path("discussionId") discussionId: Long,
    ): Response<Unit>

    @POST("v1/discussions/{discussionId}/report")
    suspend fun reportDiscussion(
        @Path("discussionId") discussionId: Long,
        @Body report: ReportRequest,
    ): NetworkResult<Unit>
}
