package com.team.domain.model.member

sealed interface MemberId {
    data object Mine : MemberId

    data class OtherUser(
        val id: Long,
    ) : MemberId

    companion object {
        fun MemberId(
            memberId: Long,
            myId: Long,
        ): MemberId {
            if (memberId == myId || memberId == DEFAULT_MEMBER_ID) return Mine
            return OtherUser(memberId)
        }

        const val DEFAULT_MEMBER_ID = -1L
    }
}
