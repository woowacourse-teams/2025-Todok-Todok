package com.example.todoktodok.data.network.service

import com.example.todoktodok.data.network.response.SignUpResponse
import retrofit2.http.POST

interface MemberService {
    @POST("members/signup")
    suspend fun signUp(request: String): SignUpResponse
}
