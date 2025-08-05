package com.team.domain.repository

import com.team.domain.model.Discussion
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.Profile

interface MemberRepository {
    suspend fun login(
        email: String,
        nickname: String,
        profileImage: String,
    ): String

    suspend fun signUp(nickname: String)

    suspend fun getProfile(memberId: String? = SAVED_MY_MEMBER_ID): Profile

    suspend fun getMemberDiscussionRooms(
        memberId: String?,
        type: MemberDiscussionType,
    ): List<Discussion>

    companion object {
        private val SAVED_MY_MEMBER_ID = null
    }
}
