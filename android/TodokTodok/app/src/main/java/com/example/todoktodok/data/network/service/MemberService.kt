package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.request.LoginRequest
import com.example.todoktodok.data.network.request.SignUpRequest
import com.example.todoktodok.data.network.response.SignUpResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface MemberService {
    @POST("members/login")
    suspend fun login(
        @Body requestBody: LoginRequest,
    ): Response<Unit>

    @POST("members/signup")
    suspend fun signUp(
        @Query("email") request: String,
        @Body requestBody: SignUpRequest,
    ): SignUpResponse
}
