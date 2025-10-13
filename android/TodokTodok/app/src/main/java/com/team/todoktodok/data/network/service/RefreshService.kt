package com.team.todoktodok.data.network.service

import com.team.todoktodok.data.network.request.RefreshRequest
import com.team.todoktodok.data.network.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface RefreshService {
    @POST("v1/members/refresh")
    suspend fun refresh(
        @Body requestBody: RefreshRequest,
    ): Response<LoginResponse>
}
