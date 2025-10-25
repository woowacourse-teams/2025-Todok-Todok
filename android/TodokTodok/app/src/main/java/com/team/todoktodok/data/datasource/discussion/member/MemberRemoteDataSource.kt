package com.team.todoktodok.data.datasource.discussion.member

import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.ProfileImageRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.ProfileImageResponse
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse

interface MemberRemoteDataSource {
    suspend fun login(request: String): NetworkResult<MemberType>

    suspend fun signUp(request: SignUpRequest): NetworkResult<Unit>

    suspend fun fetchProfile(request: MemberId): NetworkResult<ProfileResponse>

    suspend fun fetchMemberDiscussionRooms(
        request: MemberId,
        type: MemberDiscussionType,
    ): NetworkResult<List<DiscussionResponse>>

    suspend fun supportMember(
        request: MemberId.OtherUser,
        type: Support,
        reason: String,
    ): NetworkResult<Unit>

    suspend fun fetchMemberBooks(request: MemberId): NetworkResult<List<BookResponse>>

    suspend fun modifyProfile(request: ModifyProfileRequest): NetworkResult<Unit>

    suspend fun modifyProfileImage(request: ProfileImageRequest): NetworkResult<ProfileImageResponse>

    suspend fun fetchBlockedMembers(): NetworkResult<List<BlockedMemberResponse>>

    suspend fun unblock(request: Long): NetworkResult<Unit>

    suspend fun withdraw(): NetworkResult<Unit>
}
