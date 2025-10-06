package com.team.domain.model.discussionroom

@JvmInline
value class Opinion(
    val value: String,
) {
    init {
        require(value.isNotBlank()) { "내용을 입력해주세요." }
        require(value.length in MIN_VALUE..MAX_VALUE) { "토론방 내용은 1자 이상, 2500자 이하여야 합니다" }
    }

    companion object {
        private const val MIN_VALUE: Int = 1
        private const val MAX_VALUE: Int = 2500
    }
}

fun Opinion.isNotBlank(): Boolean = value.isNotBlank()
