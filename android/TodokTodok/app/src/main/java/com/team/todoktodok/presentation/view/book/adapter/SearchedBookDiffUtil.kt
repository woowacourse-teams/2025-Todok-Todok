package com.team.todoktodok.presentation.view.book.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.Book

class SearchedBookDiffUtil : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(
        oldItem: Book,
        newItem: Book,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Book,
        newItem: Book,
    ): Boolean = oldItem == newItem
}
