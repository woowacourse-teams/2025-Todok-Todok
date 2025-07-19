package com.example.todoktodok.presentation.view.library

import androidx.recyclerview.widget.DiffUtil
import com.example.domain.model.Book

val booksDiffUtil =
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
