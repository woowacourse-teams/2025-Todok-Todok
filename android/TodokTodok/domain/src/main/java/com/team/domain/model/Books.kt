package com.team.domain.model

data class Books(
    val items: List<Book>,
) {
    operator fun get(index: Int): Book = items[index]
}
