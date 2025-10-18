package com.team.todoktodok.presentation.xml.profile.activated.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.Book

class BooksDiffUtil : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(
        oldItem: Book,
        newItem: Book,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Book,
        newItem: Book,
    ): Boolean = oldItem == newItem
}
