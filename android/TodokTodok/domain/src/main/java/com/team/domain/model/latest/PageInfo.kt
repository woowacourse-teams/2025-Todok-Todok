package com.team.domain.model.latest

data class PageInfo(
    val hasNext: Boolean,
    val nextCursor: String,
) {
    companion object {
        val EMPTY = PageInfo(false, "")
    }
}
