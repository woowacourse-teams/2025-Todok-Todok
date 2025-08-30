package com.team.domain.model.book

data class AladinBook(
    private val _isbn: ISBN,
    private val _title: BookTitle,
    private val _author: BookAuthor,
    private val _image: BookImage,
) {
    val isbn: Long get() = _isbn.value

    val mainTitle: String get() = _title.mainTitle

    val author: String get() = _author.value

    val image: String get() = _image.value

    companion object {
        fun AladinBook(
            isbn: Long,
            title: String,
            author: String,
            image: String,
        ): AladinBook =
            AladinBook(
                _isbn = ISBN(isbn),
                _title = BookTitle(title),
                _author = BookAuthor(author),
                _image = BookImage(image),
            )
    }
}
