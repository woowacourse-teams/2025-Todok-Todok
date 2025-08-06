package com.team.domain.repository

import com.team.domain.model.Discussion
import com.team.domain.model.Support
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.Profile

interface MemberRepository {
    suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): String

    suspend fun signUp(nickname: String)

    suspend fun getProfile(id: MemberId): Profile

    suspend fun getMemberDiscussionRooms(
        id: MemberId,
        type: MemberDiscussionType,
    ): List<Discussion>

    suspend fun supportMember(
        id: MemberId.OtherUser,
        type: Support,
    )
}
