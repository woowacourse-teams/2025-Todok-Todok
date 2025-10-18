package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.ImagePayload
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.BlockedMember
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.Nickname
import com.team.domain.model.member.Profile
import com.team.domain.model.member.ProfileMessage

interface MemberRepository {
    suspend fun login(idToken: String): NetworkResult<MemberType>

    suspend fun signUp(nickname: Nickname): NetworkResult<Unit>

    suspend fun getProfile(id: MemberId): NetworkResult<Profile>

    suspend fun getMemberDiscussionRooms(
        id: MemberId,
        type: MemberDiscussionType,
    ): NetworkResult<List<Discussion>>

    suspend fun supportMember(
        id: MemberId.OtherUser,
        type: Support,
        reason: String,
    ): NetworkResult<Unit>

    suspend fun getMemberBooks(id: MemberId): NetworkResult<List<Book>>

    suspend fun modifyProfile(
        nickname: Nickname,
        message: ProfileMessage,
    ): NetworkResult<Unit>

    suspend fun modifyProfileImage(imagePayload: ImagePayload): NetworkResult<String>

    suspend fun getBlockedMembers(): NetworkResult<List<BlockedMember>>

    suspend fun unblock(id: Long): NetworkResult<Unit>

    suspend fun withdraw(): NetworkResult<Unit>
}
