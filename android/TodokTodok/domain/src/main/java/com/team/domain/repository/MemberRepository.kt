package com.team.domain.repository

import com.team.domain.model.member.Profile

interface MemberRepository {
    suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): String

    suspend fun signUp(nickname: String)

    suspend fun getProfile(): Profile
}
