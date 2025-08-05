package com.team.domain.model.member

sealed interface MemberId {
    data object Mine : MemberId

    data class OtherUser(
        val id: String,
    ) : MemberId

    companion object {
        fun MemberId(memberId: String?): MemberId = memberId?.let { OtherUser(it) } ?: run { Mine }
    }
}
