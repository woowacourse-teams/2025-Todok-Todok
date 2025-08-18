package com.team.todoktodok.data.datasource.member

import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.MemberDiscussionResponse

interface MemberRemoteDataSource {
    suspend fun login(request: String): NetworkResult<MemberType>

    suspend fun signUp(request: SignUpRequest): NetworkResult<Unit>

    suspend fun fetchProfile(request: MemberId): NetworkResult<ProfileResponse>

    suspend fun fetchMemberDiscussionRooms(
        request: MemberId,
        type: MemberDiscussionType,
    ): NetworkResult<List<MemberDiscussionResponse>>

    suspend fun supportMember(
        request: MemberId.OtherUser,
        type: Support,
    )

    suspend fun fetchMemberBooks(request: MemberId): NetworkResult<List<BookResponse>>

    suspend fun modifyProfile(request: ModifyProfileRequest)

    suspend fun fetchBlockedMembers(): List<BlockedMemberResponse>

    suspend fun unblock(request: Long)
}
