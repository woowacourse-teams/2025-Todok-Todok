package com.example.todoktodok.data.datasource.member

import com.example.todoktodok.data.network.response.SignUpResponse
import com.example.todoktodok.data.network.service.MemberService

class RemoteMemberDataSource(
    private val memberService: MemberService,
) : MemberDataSource {
    override suspend fun signUp(request: String): SignUpResponse = memberService.signUp(request)
}
