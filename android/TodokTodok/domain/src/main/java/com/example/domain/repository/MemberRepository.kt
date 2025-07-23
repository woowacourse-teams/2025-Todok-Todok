package com.example.domain.repository

import com.example.domain.model.Member

interface MemberRepository {
    suspend fun signUp(request: String): Member
}
