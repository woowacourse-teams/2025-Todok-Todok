package com.team.todoktodok.presentation.view.book.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.book.AladinBook

class SearchedBookDiffUtil : DiffUtil.ItemCallback<AladinBook>() {
    override fun areItemsTheSame(
        oldItem: AladinBook,
        newItem: AladinBook,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: AladinBook,
        newItem: AladinBook,
    ): Boolean = oldItem == newItem
}
