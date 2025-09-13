package com.team.todoktodok.data.network.service

import com.team.domain.model.exception.NetworkResult
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.RefreshRequest
import com.team.todoktodok.data.network.request.ReportRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.LoginResponse
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface MemberService {
    @POST("v1/members/login")
    suspend fun login(
        @Body requestBody: LoginRequest,
    ): Response<LoginResponse>

    @POST("v1/members/signup")
    suspend fun signUp(
        @Query("memberEmail") request: String,
        @Body requestBody: SignUpRequest,
    ): Response<LoginResponse>

    @GET("v1/members/{memberId}/profile")
    suspend fun fetchProfile(
        @Path("memberId") memberId: Long,
    ): NetworkResult<ProfileResponse>

    @GET("v1/members/{memberId}/discussions")
    suspend fun fetchMemberDiscussionRooms(
        @Path("memberId") memberId: Long,
        @Query("type") type: String,
    ): NetworkResult<List<DiscussionResponse>>

    @POST("v1/members/{memberId}/report")
    suspend fun report(
        @Path("memberId") memberId: Long,
        @Body reportRequest: ReportRequest,
    ): NetworkResult<Unit>

    @POST("v1/members/{memberId}/block")
    suspend fun block(
        @Path("memberId") memberId: Long,
    ): NetworkResult<Unit>

    @GET("v1/members/{memberId}/books")
    suspend fun fetchMemberBooks(
        @Path("memberId") memberId: Long,
    ): NetworkResult<List<BookResponse>>

    @PUT("v1/members/profile")
    suspend fun modifyProfile(
        @Body requestBody: ModifyProfileRequest,
    ): NetworkResult<Unit>

    @GET("v1/members/block")
    suspend fun fetchBlockedMembers(): NetworkResult<List<BlockedMemberResponse>>

    @DELETE("v1/members/{memberId}/block")
    suspend fun unblock(
        @Path("memberId") memberId: Long,
    ): NetworkResult<Unit>

    @HTTP(method = "DELETE", path = "v1/members", hasBody = true)
    suspend fun withdraw(
        @Body requestBody: RefreshRequest,
    ): NetworkResult<Unit>
}
