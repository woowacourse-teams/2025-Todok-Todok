package com.team.domain.model.book

class AladinBooks(
    val value: List<AladinBook>,
) {
    val size get() = value.size

    fun isExist(position: Int): Boolean = value.getOrNull(position) != null

    fun isEmpty(): Boolean = value.isEmpty()

    operator fun get(position: Int): AladinBook = value[position]
}
