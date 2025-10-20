package com.team.todoktodok.presentation.xml.serialization

import android.os.Parcelable
import com.team.domain.model.book.SearchedBook
import com.team.domain.model.book.SearchedBook.Companion.SearchedBook
import kotlinx.parcelize.Parcelize

@Parcelize
data class SerializationSearchedBook(
    val id: Long,
    val title: String,
    val author: String,
    val image: String,
) : Parcelable {
    fun toDomain(): SearchedBook = SearchedBook(id, title, author, image)
}

fun SearchedBook.toSerialization(): SerializationSearchedBook =
    SerializationSearchedBook(
        id = isbn,
        title = mainTitle,
        author = author,
        image = image,
    )
