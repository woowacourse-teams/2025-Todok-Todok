package com.team.todoktodok.presentation.view.book.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.book.AladinBook
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksViewHolder.Companion.SearchBooksViewHolder
import com.team.todoktodok.presentation.view.book.adapter.SearchBooksViewHolder.SelectBookClickListener

class SearchBooksAdapter(
    private val selectBook: SelectBookClickListener,
) : ListAdapter<AladinBook, SearchBooksViewHolder>(SearchedBookDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchBooksViewHolder = SearchBooksViewHolder(parent, selectBook)

    override fun onBindViewHolder(
        holder: SearchBooksViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
