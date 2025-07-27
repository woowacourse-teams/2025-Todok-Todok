package com.team.domain.model.member

enum class MemberType {
    USER,
    TEMP_USER,
    ;

    companion object {
        fun MemberType(role: String): MemberType =
            when (role) {
                "USER" -> USER
                "TEMP_USER" -> TEMP_USER
                else -> throw IllegalArgumentException(INVALID_MEMBER_TYPE)
            }

        private const val INVALID_MEMBER_TYPE = "존재하지 않는 유저 타입입니다."
    }
}
