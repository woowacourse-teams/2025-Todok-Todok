package com.team.data.network.model

import com.team.domain.model.LikeStatus

enum class LikeAction(
    val value: String,
) {
    LIKE("like"),
    UNLIKE("unlike"),
}

fun LikeAction.toStatus(): LikeStatus =
    when (this) {
        LikeAction.LIKE -> LikeStatus.LIKE
        LikeAction.UNLIKE -> LikeStatus.UNLIKE
    }
