package com.example.todoktodok.data.datasource.member

import com.example.todoktodok.data.network.request.LoginRequest
import com.example.todoktodok.data.network.request.SignUpRequest
import com.example.todoktodok.data.network.response.SignUpResponse
import com.example.todoktodok.data.network.service.MemberService

class RemoteMemberDataSource(
    private val memberService: MemberService,
) : MemberDataSource {
    override suspend fun login(email: String) = memberService.login(LoginRequest(email))

    override suspend fun signUp(request: SignUpRequest): SignUpResponse =
        memberService.signUp(
            request.email,
            request,
        )
}
