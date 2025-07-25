package com.example.todoktodok.data.repository

import com.example.domain.model.Member
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.example.todoktodok.data.network.request.toRequest

class DefaultMemberRepository(
    private val remoteMemberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {
    private var cachedMember: Member? = null

    override suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): String {
        cachedMember = Member(nickname, profileImage, email)

        return remoteMemberRemoteDataSource.login(email)
    }

    override suspend fun signUp(nickname: String) {
        cachedMember?.let {
            val request = it.copy(nickName = nickname).toRequest()
            remoteMemberRemoteDataSource.signUp(request)
        }
    }
}
