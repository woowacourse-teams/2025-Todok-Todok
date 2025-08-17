package com.team.domain.model

data class Books(
    val items: List<Book>,
) {
    val size: Int get() = items.size

    operator fun get(index: Int): Book = items[index]
}
