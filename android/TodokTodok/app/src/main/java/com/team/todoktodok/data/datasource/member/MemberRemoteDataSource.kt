package com.team.todoktodok.data.datasource.member

import com.team.todoktodok.data.network.request.SignUpRequest

interface MemberRemoteDataSource {
    suspend fun login(request: String): String

    suspend fun signUp(request: SignUpRequest)

    suspend fun fetchProfile(request: String?): ProfileResponse
}
