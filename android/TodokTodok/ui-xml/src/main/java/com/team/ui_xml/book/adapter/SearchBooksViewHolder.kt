package com.team.ui_xml.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.book.SearchedBook
import com.team.ui_xml.databinding.ItemBookBinding
import com.team.ui_xml.extension.loadImage

class SearchBooksViewHolder private constructor(
    private val binding: ItemBookBinding,
    private val selectBook: SelectBookClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            selectBook.onClick(bindingAdapterPosition - SEARCH_BOOKS_COUNT_LENGTH)
        }
    }

    fun bind(book: SearchedBook) {
        binding.apply {
            tvBookTitle.text = book.mainTitle
            tvBookAuthor.text = book.author
            ivBookImage.loadImage(book.image)
        }
    }

    companion object {
        private const val SEARCH_BOOKS_COUNT_LENGTH: Int = 1

        fun SearchBooksViewHolder(
            parent: ViewGroup,
            selectBook: SelectBookClickListener,
        ): SearchBooksViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(inflater, parent, false)
            return SearchBooksViewHolder(binding, selectBook)
        }
    }

    fun interface SelectBookClickListener {
        fun onClick(position: Int)
    }
}
