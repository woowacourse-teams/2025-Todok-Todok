package com.team.domain.model.book

data class AladinBook(
    val id: ISBN,
    val title: BookTitle,
    val author: BookAuthor,
    val image: BookImage?,
) {
    companion object {
        fun AladinBook(
            id: String,
            title: String,
            author: String,
            image: String?,
        ): AladinBook = AladinBook(
            id = ISBN(id),
            title = BookTitle(title),
            author = BookAuthor(author),
            image = image?.let { BookImage(it) },
        )
    }
}







