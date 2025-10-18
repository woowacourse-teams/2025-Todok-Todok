package com.team.domain.model.member

@JvmInline
value class ProfileMessage(
    val value: String,
) {
    companion object {
        const val MAX_LENGTH = 40
    }
}
