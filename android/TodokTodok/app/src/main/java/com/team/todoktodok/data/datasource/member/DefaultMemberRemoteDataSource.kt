package com.team.todoktodok.data.datasource.member

import com.team.domain.model.Support
import com.team.domain.model.member.MemberDiscussionType
import com.team.domain.model.member.MemberId
import com.team.domain.model.member.MemberType
import com.team.todoktodok.data.core.JwtParser
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.ModifyProfileRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.BlockedMemberResponse
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.BookResponse
import com.team.todoktodok.data.network.response.discussion.MemberDiscussionResponse
import com.team.todoktodok.data.network.service.MemberService

class DefaultMemberRemoteDataSource(
    private val memberService: MemberService,
    private val tokenDataSource: TokenDataSource,
) : MemberRemoteDataSource {
    override suspend fun login(request: String): MemberType {
        val response = memberService.login(LoginRequest(request))
        val token = response.headers()[AUTHORIZATION_NAME] ?: throw IllegalArgumentException()

        val parser = JwtParser(token)
        val memberType = parser.parseToMemberType()

        when (memberType) {
            MemberType.USER -> saveMemberSetting(parser, token)
            MemberType.TEMP_USER -> {
                tokenDataSource.saveToken(accessToken = token)
            }
        }
        return memberType
    }

    override suspend fun signUp(request: SignUpRequest) {
        val response = memberService.signUp(request.email, request)
        val token = response.headers()[AUTHORIZATION_NAME] ?: throw IllegalArgumentException()

        val parser = JwtParser(token)
        saveMemberSetting(parser, token)
    }

    private suspend fun saveMemberSetting(
        parser: JwtParser,
        accessToken: String,
    ) {
        val memberId = parser.parseToMemberId()
        tokenDataSource.saveToken(accessToken = accessToken, memberId = memberId)
    }

    override suspend fun fetchProfile(request: MemberId): ProfileResponse {
        val memberId = adjustMemberType(request)
        return memberService.fetchProfile(memberId)
    }

    override suspend fun fetchMemberDiscussionRooms(
        request: MemberId,
        type: MemberDiscussionType,
    ): List<MemberDiscussionResponse> {
        val memberId = adjustMemberType(request)
        return memberService.fetchMemberDiscussionRooms(memberId, type.name)
    }

    override suspend fun supportMember(
        request: MemberId.OtherUser,
        type: Support,
    ) {
        when (type) {
            Support.BLOCK -> memberService.block(request.id)
            Support.REPORT -> memberService.report(request.id)
        }
    }

    override suspend fun fetchMemberBooks(request: MemberId): List<BookResponse> {
        val memberId = adjustMemberType(request)
        return memberService.fetchMemberBooks(memberId)
    }

    private suspend fun adjustMemberType(request: MemberId): Long =
        when (request) {
            MemberId.Mine -> tokenDataSource.getMemberId()
            is MemberId.OtherUser -> request.id
        }

    override suspend fun modifyProfile(request: ModifyProfileRequest) {
        memberService.modifyProfile(request)
    }

    override suspend fun fetchBlockedMembers(): List<BlockedMemberResponse> = memberService.fetchBlockedMembers()

    override suspend fun unblock(request: Long) = memberService.unblock(request)
}
