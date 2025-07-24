package com.example.domain.repository

import com.example.domain.model.Member

interface MemberRepository {
    suspend fun login(email: String): String

    suspend fun signUp(request: Member): Member
}
