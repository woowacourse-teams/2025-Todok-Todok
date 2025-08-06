package com.team.domain.model.member

sealed interface MemberId {
    data object Mine : MemberId

    data class OtherUser(
        val id: Long,
    ) : MemberId

    companion object {
        fun MemberId(memberId: Long?): MemberId = memberId?.let { OtherUser(it) } ?: run { Mine }
    }
}
