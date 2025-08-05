package com.team.todoktodok.data.datasource.member

import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse

interface MemberRemoteDataSource {
    suspend fun login(request: String): String

    suspend fun signUp(request: SignUpRequest)

    suspend fun fetchProfile(request: MemberId): ProfileResponse

    suspend fun fetchMemberDiscussionRooms(
        request: MemberId,
        type: MemberDiscussionType,
    ): List<DiscussionResponse>
}
