package com.team.domain.model

data class PageInfo(
    val hasNext: Boolean,
    val nextCursor: String?,
) {
    val isLastPage get() = !hasNext && nextCursor.isNullOrBlank()

    fun modify(
        hasNext: Boolean,
        nextCursor: String?,
    ): PageInfo = copy(hasNext = hasNext, nextCursor = nextCursor)

    companion object {
        val EMPTY = PageInfo(true, "")
    }
}
