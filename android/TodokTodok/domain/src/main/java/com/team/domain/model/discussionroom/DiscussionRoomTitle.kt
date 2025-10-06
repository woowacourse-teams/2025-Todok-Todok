package com.team.domain.model.discussionroom

@JvmInline
value class DiscussionRoomTitle(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "제목을 입력해주세요." }
        require(value.length in MIN_VALUE..MAX_VALUE) { "토론방 제목은 1자 이상, 50자 이하여야 합니다" }
    }

    companion object {
        private const val MIN_VALUE: Int = 1
        private const val MAX_VALUE: Int = 50
    }
}

fun DiscussionRoomTitle.isNotBlank(): Boolean = value.isNotBlank()

