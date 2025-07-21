package com.example.todoktodok.presentation.view.parcelable

import android.os.Parcelable
import com.example.domain.model.Book
import kotlinx.parcelize.Parcelize

@Parcelize
data class ParcelableBook(
    val id: Long,
    val title: String,
    val author: String,
    val image: String,
) : Parcelable {
    fun toDomain(): Book = Book(id, title, author, image)
}
