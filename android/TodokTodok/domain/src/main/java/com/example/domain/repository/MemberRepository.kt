package com.example.domain.repository

interface MemberRepository {
    suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): String

    suspend fun signUp(nickname: String)
}
