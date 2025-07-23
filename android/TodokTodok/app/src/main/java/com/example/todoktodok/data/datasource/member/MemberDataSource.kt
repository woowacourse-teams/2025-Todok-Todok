package com.example.todoktodok.data.datasource.member

import com.example.todoktodok.data.network.response.SignUpResponse

interface MemberDataSource {
    suspend fun signUp(request: String): SignUpResponse
}
