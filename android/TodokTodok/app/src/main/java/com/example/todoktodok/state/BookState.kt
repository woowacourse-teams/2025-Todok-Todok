package com.example.todoktodok.state

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.domain.model.Book

data class BookState(
    val id: Long,
    val title: String,
    val author: String,
    val image: String,
) {
    fun bookImage(
        imageView: ImageView,
        context: Context,
    ) = Glide.with(context).load(image).into(imageView)
}

fun BookState.toDomain(): Book =
    Book(
        id = id,
        title = title,
        author = author,
        image = image,
    )
