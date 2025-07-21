package com.example.todoktodok.presentation.view.searchbook

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.domain.model.Book
import com.example.todoktodok.presentation.view.searchbook.SearchBooksViewHolder.Companion.SearchBooksViewHolder

class SearchBooksAdapter(
    private val onSelectBookListener: OnSelectBookListener,
) : ListAdapter<Book, SearchBooksViewHolder>(SearchBooksDiffUtil) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchBooksViewHolder = SearchBooksViewHolder(parent, onSelectBookListener)

    override fun onBindViewHolder(
        holder: SearchBooksViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
