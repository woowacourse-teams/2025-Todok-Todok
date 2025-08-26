package com.team.todoktodok.presentation.view.profile.activated.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.Book
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksViewHolder.Companion.BooksViewHolder

class BooksAdapter(
    private val handler: Handler,
) : ListAdapter<Book, BooksViewHolder>(BooksDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BooksViewHolder = BooksViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: BooksViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : BooksViewHolder.Handler
}
