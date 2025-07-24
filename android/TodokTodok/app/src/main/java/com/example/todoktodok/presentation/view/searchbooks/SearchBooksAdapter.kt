package com.example.todoktodok.presentation.view.searchbooks

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Book
import com.example.todoktodok.presentation.view.searchbooks.SearchBooksViewHolder.Companion.SearchBooksViewHolder
import com.example.todoktodok.state.BookState

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
