package com.team.domain.model

data class Book(
    val id: Long,
    val title: String,
    val author: String,
    val image: String,
    val publisher: String,
    val summary: String,
) {
    fun extractSubtitle(): String = title.split(SUBTITLE_SEPARATOR).first().trim()

    fun extractAuthor(): String = author.split(AUTHOR_SEPARATOR).first().trim()

    companion object {
        private const val SUBTITLE_SEPARATOR = "-"
        private const val AUTHOR_SEPARATOR = "("
    }
}
