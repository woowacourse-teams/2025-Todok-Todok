package com.team.todoktodok.data.local.discussion

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.team.domain.model.book.SearchedBook

@Entity(tableName = "book")
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "book_id") val bookId: Long,
    @ColumnInfo(name = "book_title") val bookTitle: String,
    @ColumnInfo(name = "book_author") val bookAuthor: String,
    @ColumnInfo(name = "book_image") val bookImage: String,
)

fun SearchedBook.toEntity() =
    BookEntity(
        bookId = isbn,
        bookTitle = title,
        bookAuthor = author,
        bookImage = image,
    )

fun BookEntity.toDomain() =
    SearchedBook.Companion.SearchedBook(
        isbn = bookId,
        title = bookTitle,
        author = bookAuthor,
        image = bookImage,
    )
