package com.team.domain.model.book

class AladinBooks(
    val value: List<AladinBook>,
) {
    val size get() = value.size

    fun isEmpty(): Boolean = value.isEmpty()

    operator fun get(position: Int): AladinBook = value[position]

    operator fun contains(position: Int): Boolean = position in value.indices
}

inline fun <R> AladinBooks.map(transform: (AladinBook) -> R): List<R> = this.value.map(transform)
