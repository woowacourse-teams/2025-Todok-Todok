package com.team.domain.model.book

data class SearchedBook(
    val isbn: Long,
    private val _title: BookTitle,
    private val _author: BookAuthor,
    private val _image: BookImage,
) {
    val title: String get() = _title.value
    val mainTitle: String get() = _title.mainTitle

    val author: String get() = _author.value

    val image: String get() = _image.value

    companion object {
        fun SearchedBook(
            isbn: Long,
            title: String,
            author: String,
            image: String,
        ): SearchedBook =
            SearchedBook(
                isbn = isbn,
                _title = BookTitle(title),
                _author = BookAuthor(author),
                _image = BookImage(image),
            )
    }
}
