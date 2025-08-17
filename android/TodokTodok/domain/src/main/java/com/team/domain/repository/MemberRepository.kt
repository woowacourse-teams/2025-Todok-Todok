package com.team.domain.repository

import com.team.domain.model.Book
import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.BlockedMember
import com.team.domain.model.member.MemberDiscussion
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

    suspend fun signUp(nickname: String)

    suspend fun getProfile(id: MemberId): Profile

    suspend fun getMemberDiscussionRooms(
        id: MemberId,
        type: MemberDiscussionType,
    ): List<MemberDiscussion>

    suspend fun supportMember(
        id: MemberId.OtherUser,
        type: Support,
    )

    suspend fun getMemberBooks(id: MemberId): List<Book>

    suspend fun modifyProfile(
        nickname: String,
        message: String,
    )

    suspend fun getBlockedMembers(): List<BlockedMember>

    suspend fun unblock(id: Long)
}
