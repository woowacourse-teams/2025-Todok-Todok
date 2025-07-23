package com.example.todoktodok.fake.datasource

import com.example.todoktodok.data.datasource.member.MemberDataSource
import com.example.todoktodok.data.network.response.SignUpResponse

class FakeMemberDataSource : MemberDataSource {
    var expectedRequest: String? = null
    var response: SignUpResponse? = null

    override suspend fun signUp(request: String): SignUpResponse {
        check(request == expectedRequest) { "잘못된 요청값: $request" }
        return response ?: throw IllegalStateException("응답이 설정되지 않았음")
    }
}
