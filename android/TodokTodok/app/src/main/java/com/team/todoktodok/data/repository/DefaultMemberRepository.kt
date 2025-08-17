package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Member
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TokdokTodokExceptions
import com.team.domain.model.member.BlockedMember
import com.team.domain.model.member.MemberDiscussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.toRequest
import com.team.todoktodok.data.network.response.discussion.toDomain
import java.time.LocalDate

class DefaultMemberRepository(
    private val remoteMemberRemoteDataSource: MemberRemoteDataSource,
) : MemberRepository {
    private var cachedMember: Member? = null

    override suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): NetworkResult<MemberType> {
        cachedMember = Member(nickname, profileImage, email)

        return remoteMemberRemoteDataSource.login(email)
    }

    override suspend fun signUp(nickname: String): NetworkResult<Unit> =
        cachedMember?.let {
            val request = it.copy(nickName = nickname).toRequest()
            remoteMemberRemoteDataSource.signUp(request)
        } ?: NetworkResult.Failure(TokdokTodokExceptions.SignUpException.InvalidTokenException)

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
        // remoteMemberRemoteDataSource.fetchBlockedMembers().map { it.toDomain() }
        return listOf(
            BlockedMember(0, "페토", LocalDate.of(2025, 2, 3)),
            BlockedMember(1, "페토", LocalDate.of(2025, 2, 3)),
            BlockedMember(2, "페토", LocalDate.of(2025, 2, 3)),
            BlockedMember(3, "페토", LocalDate.of(2025, 2, 3)),
        )
    }

    override suspend fun unblock(id: Long) = remoteMemberRemoteDataSource.unblock(id)
}
