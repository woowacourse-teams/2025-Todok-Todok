package com.team.todoktodok.data.datasource.member

import com.team.domain.model.member.MemberDiscussionType
import com.team.todoktodok.data.core.JwtParser
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.response.ProfileResponse
import com.team.todoktodok.data.network.response.discussion.DiscussionResponse
import com.team.todoktodok.data.network.service.MemberService

class DefaultMemberRemoteDataSource(
    private val memberService: MemberService,
    private val tokenDataSource: TokenDataSource,
) : MemberRemoteDataSource {
    override suspend fun login(request: String): String {
        val response = memberService.login(LoginRequest(request))
        val token = response.headers()[AUTHORIZATION_NAME] ?: throw IllegalArgumentException()

        val parser = JwtParser(token)
        val memberType = parser.parseMemberType()
        val memberId = parser.parseMemberId()
        tokenDataSource.saveToken(accessToken = token, memberId = memberId)

        return memberType
    }

    override suspend fun signUp(request: SignUpRequest) {
        memberService.signUp(
            request.email,
            request,
        )
    }

    override suspend fun fetchProfile(request: String?): ProfileResponse =
        request?.let {
            memberService.fetchProfile(it)
        } ?: run {
            val memberId = tokenDataSource.getMemberId()
            memberService.fetchProfile(memberId)
        }

    override suspend fun fetchMemberDiscussionRooms(
        memberId: String?,
        type: MemberDiscussionType,
    ): List<DiscussionResponse> =
        memberId?.let {
            memberService.fetchMemberDiscussionRooms(it, type.name)
        } ?: run {
            val memberId = tokenDataSource.getMemberId()
            memberService.fetchMemberDiscussionRooms(memberId, type.name)
        }
}
