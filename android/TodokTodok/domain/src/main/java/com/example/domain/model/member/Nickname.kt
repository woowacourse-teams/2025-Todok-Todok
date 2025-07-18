package com.example.domain.model.member

@JvmInline
value class Nickname(
    val value: String,
) {
    init {
        if (value.any { it == ' ' }) throw NickNameException.InvalidWhiteSpace
        require(value.all { it.isLetterOrDigit() && (it in '가'..'힣' || it in 'a'..'z' || it in 'A'..'Z' || it.isDigit()) })
        require(value.length in 1..8)
    }
}
