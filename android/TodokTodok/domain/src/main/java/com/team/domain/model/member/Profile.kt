package com.team.domain.model.member

data class Profile(
    val memberId: Long,
    val nickname: String,
    val message: String?,
    val profileImage: String,
) {
    companion object {
        val EMPTY =
            Profile(
                memberId = -1,
                nickname = "",
                message = null,
                profileImage = "",
            )
    }
}
