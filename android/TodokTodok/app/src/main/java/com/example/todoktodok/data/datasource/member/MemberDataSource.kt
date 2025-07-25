package com.example.todoktodok.data.datasource.member

import com.example.todoktodok.data.network.request.SignUpRequest

interface MemberDataSource {
    suspend fun login(request: String): String

    suspend fun signUp(request: SignUpRequest)
}
