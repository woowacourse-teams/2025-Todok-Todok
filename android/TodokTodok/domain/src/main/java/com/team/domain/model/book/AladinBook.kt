package com.team.domain.model.book

data class AladinBook(
    private val _id: ISBN,
    private val _title: BookTitle,
    private val _author: BookAuthor,
    private val _image: BookImage,
) {
    val id: String get() = _id.value

    val title: String get() = _title.mainTitle

    val author: String get() = _author.value

    val image: String get() = _image.value

    companion object {
        fun AladinBook(
            id: String,
            title: String,
            author: String,
            image: String,
        ): AladinBook =
            AladinBook(
                _id = ISBN(id),
                _title = BookTitle(title),
                _author = BookAuthor(author),
                _image = BookImage(image),
            )
    }
}
