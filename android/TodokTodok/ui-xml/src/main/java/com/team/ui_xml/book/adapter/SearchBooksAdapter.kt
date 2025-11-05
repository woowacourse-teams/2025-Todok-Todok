package com.team.ui_xml.book.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.ui_xml.R

class SearchBooksAdapter(
    private val selectBook: SearchBooksViewHolder.SelectBookClickListener,
) : ListAdapter<SearchBooksGroup, RecyclerView.ViewHolder>(SearchedBookDiffUtil()) {
    override fun getItemViewType(position: Int): Int =
        when (getItem(position)) {
            is SearchBooksGroup.Count -> R.layout.item_searched_books_count
            is SearchBooksGroup.Book -> R.layout.item_book
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (viewType) {
            R.layout.item_searched_books_count ->
                SearchBooksCountViewHolder.Companion.SearchBooksCountViewHolder(
                    parent,
                )
            R.layout.item_book ->
                SearchBooksViewHolder.Companion.SearchBooksViewHolder(
                    parent,
                    selectBook,
                )
            else -> throw IllegalArgumentException("Invalid view type")
        }

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is SearchBooksGroup.Count ->
                (holder as SearchBooksCountViewHolder).bind(item.size)

            is SearchBooksGroup.Book ->
                (holder as SearchBooksViewHolder).bind(
                    item.book,
                )
        }
    }
}
