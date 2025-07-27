package com.team.todoktodok.presentation.view.library.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.todoktodok.state.BookState

class BooksAdapter : ListAdapter<BookState, BooksViewHolder>(booksDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BooksViewHolder = BooksViewHolder.Companion.BooksViewHolder(parent)

    override fun onBindViewHolder(
        holder: BooksViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }
}
