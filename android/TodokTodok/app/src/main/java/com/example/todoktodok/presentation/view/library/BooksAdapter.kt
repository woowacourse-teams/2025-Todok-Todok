package com.example.todoktodok.presentation.view.library

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Book
import com.example.todoktodok.presentation.view.library.BooksViewHolder.Companion.BooksViewHolder

class BooksAdapter : ListAdapter<Book, BooksViewHolder>(booksDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BooksViewHolder = BooksViewHolder(parent)

    override fun onBindViewHolder(
        holder: BooksViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
