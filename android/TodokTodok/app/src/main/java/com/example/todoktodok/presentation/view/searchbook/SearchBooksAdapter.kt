package com.example.todoktodok.presentation.view.searchbook

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Book
import com.example.todoktodok.presentation.view.searchbook.SearchBooksViewHolder.Companion.SearchBooksViewHolder

class SearchBooksAdapter(
    private val books: List<Book>,
    private val onSelectBookListener: OnSelectBookListener,
) : RecyclerView.Adapter<SearchBooksViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchBooksViewHolder = SearchBooksViewHolder(parent, onSelectBookListener)

    override fun onBindViewHolder(
        holder: SearchBooksViewHolder,
        position: Int,
    ) = holder.bind(books[position])

    override fun getItemCount(): Int = books.size
}
