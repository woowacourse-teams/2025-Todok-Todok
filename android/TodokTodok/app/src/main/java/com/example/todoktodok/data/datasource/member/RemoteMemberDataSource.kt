package com.example.todoktodok.data.datasource.member

import com.example.todoktodok.data.datasource.token.TokenDataSource
import com.example.todoktodok.data.network.auth.AuthInterceptor.Companion.AUTHORIZATION_NAME
import com.example.todoktodok.data.network.request.LoginRequest
import com.example.todoktodok.data.network.request.SignUpRequest
import com.example.todoktodok.data.network.service.MemberService
import com.example.todoktodok.data.util.JwtUtils

class RemoteMemberDataSource(
    private val memberService: MemberService,
    private val tokenDataSource: TokenDataSource,
) : MemberDataSource {
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
}
