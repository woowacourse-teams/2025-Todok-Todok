package com.example.todoktodok.presentation.view.searchbooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Book
import com.example.todoktodok.databinding.ItemBookSearchBinding

class SearchBooksViewHolder private constructor(
    private val binding: ItemBookSearchBinding,
    private val onSelectBookListener: OnSelectBookListener,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onSelectBookListener.select(
                absoluteAdapterPosition,
            )
        }
    }

    fun bind(book: Book) {
        with(binding) {
            tvBookTitle.text = book.title
            tvBookAuthor.text = book.author
        }
    }

    companion object {
        fun SearchBooksViewHolder(
            parent: ViewGroup,
            onSelectBookListener: OnSelectBookListener,
        ): SearchBooksViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBookSearchBinding.inflate(inflater, parent, false)
            return SearchBooksViewHolder(binding, onSelectBookListener)
        }
    }
}
