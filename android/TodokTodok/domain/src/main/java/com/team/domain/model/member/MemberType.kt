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

        fun MemberType(memberId: Long): MemberType =
            when {
                memberId == 0L -> TEMP_USER
                memberId > 0L -> USER
                else -> throw IllegalArgumentException(INVALID_MEMBER_ID.format(memberId))
            }

        private const val INVALID_MEMBER_TYPE = "존재하지 않는 유저 타입입니다."
        private const val INVALID_MEMBER_ID = "유효하지 않은 memberId입니다: %d"
    }
}
