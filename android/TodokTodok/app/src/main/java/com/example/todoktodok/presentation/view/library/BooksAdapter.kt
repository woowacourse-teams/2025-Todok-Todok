package com.example.todoktodok.presentation.view.library

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.todoktodok.state.BookState

class BooksAdapter : ListAdapter<BookState, BooksViewHolder>(booksDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BooksViewHolder = BooksViewHolder.create(parent)

    override fun onBindViewHolder(
        holder: BooksViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
