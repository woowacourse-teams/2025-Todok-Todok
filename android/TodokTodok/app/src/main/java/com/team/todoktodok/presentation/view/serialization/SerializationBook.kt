package com.team.todoktodok.presentation.view.serialization

import android.os.Parcelable
import com.team.domain.model.Book
import com.team.domain.model.book.SearchedBook
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerializationBook(
    val id: Long,
    val title: String,
    val author: String,
    val image: String,
) : Parcelable {
    fun toDomain(): Book = Book(id, title, author, image)
}

fun Book.toSerialization(): SerializationBook =
    SerializationBook(
        id = id,
        title = title,
        author = author,
        image = image,
    )

fun SearchedBook.toSerialization(): SerializationBook =
    SerializationBook(
        id = isbn,
        title = mainTitle,
        author = author,
        image = image,
    )
