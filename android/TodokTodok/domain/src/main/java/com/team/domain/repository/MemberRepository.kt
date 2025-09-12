package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.BlockedMember
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.domain.model.member.Profile

interface MemberRepository {
    suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): NetworkResult<MemberType>

    suspend fun signUp(nickname: String): NetworkResult<Unit>

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
        nickname: String,
        message: String,
    ): NetworkResult<Unit>

    suspend fun getBlockedMembers(): NetworkResult<List<BlockedMember>>

    suspend fun unblock(id: Long): NetworkResult<Unit>

    suspend fun withdraw(): NetworkResult<Unit>
}
