package com.team.todoktodok.data.repository

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.ImagePayload
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.SignUpException
import com.team.domain.model.exception.map
import com.team.domain.model.member.BlockedMember
import com.team.domain.model.member.Member
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.Profile
import com.team.domain.repository.MemberRepository
import com.team.todoktodok.data.core.ext.toMultipartPart
import com.team.todoktodok.data.datasource.member.MemberRemoteDataSource
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.ProfileImageRequest
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
    ): NetworkResult<MemberType> {
        cachedMember = Member(nickname, profileImage, email)

        return remoteMemberRemoteDataSource.login(email)
    }

    override suspend fun signUp(nickname: String): NetworkResult<Unit> =
        cachedMember?.let {
            val request = it.copy(nickName = nickname).toRequest()
            remoteMemberRemoteDataSource.signUp(request)
        } ?: NetworkResult.Failure(SignUpException.InvalidTokenException)

    override suspend fun getProfile(id: MemberId): NetworkResult<Profile> =
        remoteMemberRemoteDataSource.fetchProfile(id).map { it.toDomain() }

    override suspend fun getMemberDiscussionRooms(
        id: MemberId,
        type: MemberDiscussionType,
    ): NetworkResult<List<Discussion>> =
        remoteMemberRemoteDataSource
            .fetchMemberDiscussionRooms(id, type)
            .map { discussions -> discussions.map { it.toDomain() } }

    override suspend fun supportMember(
        id: MemberId.OtherUser,
        type: Support,
        reason: String,
    ): NetworkResult<Unit> = remoteMemberRemoteDataSource.supportMember(id, type, reason)

    override suspend fun getMemberBooks(id: MemberId): NetworkResult<List<Book>> =
        remoteMemberRemoteDataSource
            .fetchMemberBooks(id)
            .map { books -> books.map { it.toDomain() } }

    override suspend fun modifyProfile(
        nickname: String,
        message: String,
    ): NetworkResult<Unit> = remoteMemberRemoteDataSource.modifyProfile(ModifyProfileRequest(nickname, message))

    override suspend fun modifyProfileImage(imagePayload: ImagePayload): NetworkResult<String> =
        remoteMemberRemoteDataSource
            .modifyProfileImage(
                ProfileImageRequest(
                    imagePayload.toMultipartPart(
                        PROFILE_IMAGE_PARAM_NAME,
                    ),
                ),
            ).map { it.profileImage }

    override suspend fun getBlockedMembers(): NetworkResult<List<BlockedMember>> =
        remoteMemberRemoteDataSource
            .fetchBlockedMembers()
            .map { members -> members.map { it.toDomain() } }

    override suspend fun unblock(id: Long): NetworkResult<Unit> = remoteMemberRemoteDataSource.unblock(id)

    override suspend fun withdraw(): NetworkResult<Unit> = remoteMemberRemoteDataSource.withdraw()

    companion object {
        private const val PROFILE_IMAGE_PARAM_NAME = "profileImage"
    }
}
