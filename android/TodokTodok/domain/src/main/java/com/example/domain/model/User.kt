package com.example.domain.model

data class User(
    val id: Long,
    val nickname: Nickname,
) {

}

@JvmInline
value class Nickname(
    val value: String,
) {
    init {
        require(value.trim().length == value.length)
        require(value.all { it.isLetterOrDigit() && (it in '가'..'힣' || it in 'a'..'z' || it in 'A'..'Z' || it.isDigit()) })
        require(value.length in 1..8)
    }
}
