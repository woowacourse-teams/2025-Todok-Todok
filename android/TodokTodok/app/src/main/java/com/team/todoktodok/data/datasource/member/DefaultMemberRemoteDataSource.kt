package com.team.todoktodok.data.datasource.member

import com.team.domain.model.member.Profile
import com.team.todoktodok.data.core.JwtUtils
import com.team.todoktodok.data.datasource.token.TokenDataSource
import com.team.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.team.todoktodok.data.network.request.LoginRequest
import com.team.todoktodok.data.network.request.SignUpRequest
import com.team.todoktodok.data.network.service.MemberService

class DefaultMemberRemoteDataSource(
    private val memberService: MemberService,
    private val tokenDataSource: TokenDataSource,
) : MemberRemoteDataSource {
    override suspend fun login(email: String): String {
        val response = memberService.login(LoginRequest(email))
        val token = response.headers()[AUTHORIZATION_NAME] ?: throw IllegalArgumentException()
        tokenDataSource.saveToken(accessToken = token)
        val role = JwtUtils.getRoleFromJwt(token) ?: throw IllegalArgumentException()

        return role
    }

    override suspend fun signUp(request: SignUpRequest) {
        memberService.signUp(
            request.email,
            request,
        )
    }

    override suspend fun fetchProfile(): Profile {
        val dummyProfile =
            Profile(
                nickname = "페토페토정페토",
                profileImageUrl = "",
                description = "안녕 나는 페토야",
            )
        return dummyProfile
    }
}
