package com.team.ui_xml.book.adapter

import androidx.recyclerview.widget.DiffUtil

class SearchedBookDiffUtil : DiffUtil.ItemCallback<SearchBooksGroup>() {
    override fun areItemsTheSame(
        oldItem: SearchBooksGroup,
        newItem: SearchBooksGroup,
    ): Boolean =
        when {
            oldItem is SearchBooksGroup.Count && newItem is SearchBooksGroup.Count -> true
            oldItem is SearchBooksGroup.Book && newItem is SearchBooksGroup.Book ->
                oldItem.book.isbn ==
                    newItem.book.isbn

            else -> false
        }

    override fun areContentsTheSame(
        oldItem: SearchBooksGroup,
        newItem: SearchBooksGroup,
    ): Boolean =
        when {
            oldItem is SearchBooksGroup.Count && newItem is SearchBooksGroup.Count ->
                oldItem.size == newItem.size

            oldItem is SearchBooksGroup.Book && newItem is SearchBooksGroup.Book ->
                oldItem.book == newItem.book

            else -> false
        }
}
