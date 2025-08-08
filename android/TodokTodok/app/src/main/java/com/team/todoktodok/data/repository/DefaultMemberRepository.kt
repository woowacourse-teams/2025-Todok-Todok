package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Member
import com.team.domain.model.Support
import com.team.domain.model.member.BlockedMember
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.data.core.ext.toLocalDate
import com.team.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.toRequest
import com.team.todoktodok.data.network.response.discussion.toDomain

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

    override suspend fun getProfile(id: MemberId): Profile = remoteMemberRemoteDataSource.fetchProfile(id).toDomain()

    override suspend fun getMemberDiscussionRooms(
        id: MemberId,
        type: MemberDiscussionType,
    ): List<MemberDiscussion> =
        remoteMemberRemoteDataSource
            .fetchMemberDiscussionRooms(id, type)
            .map { it.toDomain() }

    override suspend fun supportMember(
        id: MemberId.OtherUser,
        type: Support,
    ) {
        remoteMemberRemoteDataSource.supportMember(id, type)
    }

    override suspend fun getMemberBooks(id: MemberId): List<Book> =
        remoteMemberRemoteDataSource
            .fetchMemberBooks(id)
            .map { it.toDomain() }

    override suspend fun modifyProfile(
        nickname: String,
        message: String,
    ) = remoteMemberRemoteDataSource.modifyProfile(ModifyProfileRequest(nickname, message))

    override suspend fun getBlockedMembers(): List<BlockedMember> {
        return listOf(
            BlockedMember(
                5,
                "페토",
                "2025-07-30T07:54:24.604Z".toLocalDate(),
            ),
            BlockedMember(
                1,
                "페토",
                "2025-07-30T07:54:24.604Z".toLocalDate(),
            ),
            BlockedMember(
                2,
                "페토",
                "2025-07-30T07:54:24.604Z".toLocalDate(),
            ),
            BlockedMember(
                3,
                "페토",
                "2025-07-30T07:54:24.604Z".toLocalDate(),
            ),
            BlockedMember(
                4,
                "페토",
                "2025-07-30T07:54:24.604Z".toLocalDate(),
            ),
        )
        // remoteMemberRemoteDataSource.fetchBlockedMembers().map { it.toDomain() }
    }

    override suspend fun unblock(id: Long) = remoteMemberRemoteDataSource.unblock(id)
}
