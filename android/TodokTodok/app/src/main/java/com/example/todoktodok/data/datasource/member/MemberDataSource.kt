package com.example.todoktodok.data.datasource.member

import com.example.todoktodok.data.network.request.SignUpRequest
import com.example.todoktodok.data.network.response.SignUpResponse

interface MemberDataSource {
    suspend fun login(request: String)

    suspend fun signUp(request: SignUpRequest): SignUpResponse
}
