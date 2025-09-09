package com.team.todoktodok.data.datasource.member

import com.team.domain.model.Support
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.toDomain
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.core.JwtParser
import com.team.todoktodok.data.core.ext.extractTokens
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.ReportRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.service.MemberService

class DefaultMemberRemoteDataSource(
    private val memberService: MemberService,
    private val tokenDataSource: TokenDataSource,
) : MemberRemoteDataSource {
    override suspend fun refresh(): NetworkResult<Unit> = memberService.refresh()

    override suspend fun login(request: String): NetworkResult<MemberType> =
        runCatching {
            val response = memberService.login(LoginRequest(request))
            response.extractTokens { accessToken, refreshToken ->
                val parser = JwtParser(accessToken)
                val memberType = parser.parseToMemberType()

                when (memberType) {
                    MemberType.USER -> saveMemberSetting(parser, accessToken, refreshToken)
                    MemberType.TEMP_USER -> tokenDataSource.saveToken(accessToken, refreshToken)
                }
                memberType
            }
        }.getOrElse { throwable ->
            NetworkResult.Failure(throwable.toDomain())
        }

    override suspend fun signUp(request: SignUpRequest): NetworkResult<Unit> =
        runCatching {
            memberService
                .signUp(request.email, request)
                .extractTokens { accessToken, refreshToken ->
                    val parser = JwtParser(accessToken)
                    saveMemberSetting(parser, accessToken, refreshToken)
                }
        }.getOrElse { throwable ->
            NetworkResult.Failure(throwable.toDomain())
        }

    private suspend fun saveMemberSetting(
        parser: JwtParser,
        accessToken: String,
        refreshToken: String,
    ) {
        val memberId = parser.parseToMemberId()
        tokenDataSource.saveToken(accessToken, refreshToken, memberId)
    }

    override suspend fun fetchProfile(request: MemberId): NetworkResult<ProfileResponse> {
        val memberId = adjustMemberType(request)
        return memberService.fetchProfile(memberId)
    }

    override suspend fun fetchMemberDiscussionRooms(
        request: MemberId,
        type: MemberDiscussionType,
    ): NetworkResult<List<DiscussionResponse>> {
        val memberId = adjustMemberType(request)
        return memberService.fetchMemberDiscussionRooms(memberId, type.name)
    }

    override suspend fun supportMember(
        request: MemberId.OtherUser,
        type: Support,
        reason: String,
    ): NetworkResult<Unit> =
        when (type) {
            Support.BLOCK -> memberService.block(request.id)
            Support.REPORT -> memberService.report(request.id, ReportRequest(reason))
        }

    override suspend fun fetchMemberBooks(request: MemberId): NetworkResult<List<BookResponse>> {
        val memberId = adjustMemberType(request)
        return memberService.fetchMemberBooks(memberId)
    }

    private suspend fun adjustMemberType(request: MemberId): Long =
        when (request) {
            MemberId.Mine -> tokenDataSource.getMemberId()
            is MemberId.OtherUser -> request.id
        }

    override suspend fun modifyProfile(request: ModifyProfileRequest): NetworkResult<Unit> = memberService.modifyProfile(request)

    override suspend fun fetchBlockedMembers(): NetworkResult<List<BlockedMemberResponse>> = memberService.fetchBlockedMembers()

    override suspend fun unblock(request: Long) = memberService.unblock(request)
}
