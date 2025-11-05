package com.team.data.core

import com.team.domain.model.member.MemberType
import com.team.domain.model.member.MemberType.Companion.MemberType
import org.json.JSONObject
import java.util.Base64

class JwtParser(
    jwt: String,
) {
    private val decodeJson = decodeJwt(jwt)

    fun parseToMemberType(): MemberType = MemberType(decodeJson.getString(KEY_MEMBER_TYPE))

    fun parseToMemberId(): Long = decodeJson.getLong(KEY_MEMBER_ID)

    fun decodeJwt(jwt: String): JSONObject {
        val parts = jwt.split(".")
        if (parts.size != VALID_PARTITION_SIZE) {
            throw IllegalArgumentException(INVALID_PARTITION_SIZE)
        }

        val payload = parts[1]
        val decoded = String(Base64.getUrlDecoder().decode(payload))
        return JSONObject(decoded)
    }

    companion object {
        private const val KEY_MEMBER_TYPE = "role"
        private const val KEY_MEMBER_ID = "sub"
        private const val VALID_PARTITION_SIZE = 3
        private const val INVALID_PARTITION_SIZE = "잘못된 배열의 JWT 토큰입니다"
    }
}
