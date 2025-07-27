package com.team.todoktodok.presentation.view.searchbooks.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.todoktodok.presentation.view.searchbooks.adapter.SearchBooksViewHolder.Companion.SearchBooksViewHolder
import com.team.todoktodok.state.BookState

class SearchBooksAdapter(
    private val onSelectBookListener: OnSelectBookListener,
) : ListAdapter<BookState, SearchBooksViewHolder>(SearchBooksDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchBooksViewHolder = SearchBooksViewHolder(parent, onSelectBookListener)

    override fun onBindViewHolder(
        holder: SearchBooksViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
