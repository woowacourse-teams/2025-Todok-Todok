package com.example.todoktodok.data.repository

import com.example.domain.model.Member
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.data.datasource.member.MemberDataSource
import com.example.todoktodok.data.network.request.toRequest

class DefaultMemberRepository(
    private val remoteMemberDataSource: MemberDataSource,
) : MemberRepository {
    override suspend fun login(email: String): String = remoteMemberDataSource.login(email)

    override suspend fun signUp(request: Member): Member {
        val response = remoteMemberDataSource.signUp(request.toRequest())
        return response.toDomain()
    }
}
