package com.example.todoktodok.presentation.view.searchbooks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoktodok.databinding.ItemBookSearchBinding
import com.example.todoktodok.presentation.view.searchbooks.adapter.OnSelectBookListener
import com.example.todoktodok.state.BookState

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

    fun bind(book: BookState) {
        with(binding) {
            tvBookTitle.text = book.title
            tvBookAuthor.text = book.author
            book.bookImage(ivBookCover, root.context)
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
