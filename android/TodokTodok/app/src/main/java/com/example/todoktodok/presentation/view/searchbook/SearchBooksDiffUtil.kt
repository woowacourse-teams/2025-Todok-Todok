package com.example.todoktodok.presentation.view.searchbook

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Book

val SearchBooksDiffUtil =
    object : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(
            oldItem: Book,
            newItem: Book,
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Book,
            newItem: Book,
        ): Boolean = oldItem == newItem
    }
