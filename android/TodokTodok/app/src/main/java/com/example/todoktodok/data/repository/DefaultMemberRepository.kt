package com.example.todoktodok.data.repository

import com.example.domain.model.Member
import com.example.domain.repository.MemberRepository
import com.example.todoktodok.data.datasource.member.MemberDataSource
import com.example.todoktodok.data.network.request.toRequest

class DefaultMemberRepository(
    private val remoteMemberDataSource: MemberDataSource,
) : MemberRepository {
    private var cachedMember: Member? = null

    override suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): String {
        cachedMember = Member(nickname, profileImage, email)

        return remoteMemberDataSource.login(email)
    }

    override suspend fun signUp(nickname: String) {
        cachedMember?.let {
            val request = it.copy(nickName = nickname).toRequest()
            remoteMemberDataSource.signUp(request)
        }
    }
}
