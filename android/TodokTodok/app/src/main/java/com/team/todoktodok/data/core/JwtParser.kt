package com.team.todoktodok.data.core

import android.util.Base64
import org.json.JSONObject

class JwtParser(
    private val jwt: String,
) {
    fun parseMemberType(): String {
        val json = decodeJwt(jwt)
        return json.getString(KEY_MEMBER_TYPE)
    }

    fun parseMemberId(): String {
        val json = decodeJwt(jwt)
        return json.getString(KEY_MEMBER_ID)
    }

    fun decodeJwt(jwt: String): JSONObject {
        val parts = jwt.split(".")
        if (parts.size != VALID_PARTITION_SIZE) {
            throw IllegalArgumentException(INVALID_PARTITION_SIZE)
        }

        val payload = parts[1]
        val decoded =
            String(
                Base64.decode(
                    payload,
                    Base64.URL_SAFE or Base64.NO_PADDING or Base64.NO_WRAP,
                ),
            )
        return JSONObject(decoded)
    }

    companion object {
        private const val KEY_MEMBER_TYPE = "role"
        private const val KEY_MEMBER_ID = "sub"
        private const val VALID_PARTITION_SIZE = 3
        private const val INVALID_PARTITION_SIZE = "잘못된 배열의 JWT 토큰입니다"
    }
}
