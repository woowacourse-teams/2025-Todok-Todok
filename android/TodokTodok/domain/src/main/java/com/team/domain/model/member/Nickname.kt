package com.team.domain.model.member

@JvmInline
value class Nickname(
    val value: String,
) {
    init {
        if (value.any { it == ' ' }) throw NickNameException.InvalidWhiteSpace
        if (!value.all { it in '가'..'힣' || it.isLetterOrDigit() }) throw NickNameException.InvalidCharacters
        if (value.length !in MIN_LENGTH..MAX_LENGTH) throw NickNameException.InvalidLength
    }

    companion object {
        private const val MIN_LENGTH = 1
        const val MAX_LENGTH = 8
    }
}
