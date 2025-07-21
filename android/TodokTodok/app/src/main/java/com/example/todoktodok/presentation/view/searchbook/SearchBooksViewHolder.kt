package com.example.todoktodok.presentation.view.searchbook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Book
import com.example.todoktodok.databinding.ItemBookSearchBinding

class SearchBooksViewHolder private constructor(
    private val binding: ItemBookSearchBinding,
    private val onSelectBookListener: OnSelectBookListener,
) : RecyclerView.ViewHolder(binding.root) {
    private var currentBookId: Long? = null

    init {
        binding.root.setOnClickListener {
            currentBookId?.let { id: Long ->
                onSelectBookListener.select(
                    id,
                )
            }
        }
    }

    fun bind(book: Book) {
        currentBookId = book.id
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
