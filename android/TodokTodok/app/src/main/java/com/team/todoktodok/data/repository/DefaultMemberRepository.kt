package com.team.todoktodok.data.repository

import com.team.domain.model.Member
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.team.todoktodok.data.network.request.toRequest

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

    override suspend fun getProfile(request: String?): Profile {
        return remoteMemberRemoteDataSource.fetchProfile(request).toDomain()
    }
}
