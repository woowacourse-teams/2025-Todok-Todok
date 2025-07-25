package com.example.todoktodok.presentation.view.searchbooks.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.todoktodok.state.BookState

val SearchBooksDiffUtil =
    object : DiffUtil.ItemCallback<BookState>() {
        override fun areItemsTheSame(
            oldItem: BookState,
            newItem: BookState,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: BookState,
            newItem: BookState,
        ): Boolean = oldItem == newItem
    }
