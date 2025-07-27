package com.team.todoktodok.presentation.view.library.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.todoktodok.state.BookState

val booksDiffUtil =
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
