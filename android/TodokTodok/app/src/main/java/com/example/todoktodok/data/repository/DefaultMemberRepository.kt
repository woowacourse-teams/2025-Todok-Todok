package com.example.todoktodok.data.repository

import com.example.domain.model.Member
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.data.datasource.member.MemberDataSource

class DefaultMemberRepository(
    private val remoteMemberDataSource: MemberDataSource,
) : MemberRepository {
    override suspend fun signUp(request: String): Member {
        val response = remoteMemberDataSource.signUp(request)
        return response.toDomain()
    }
}
