package com.team.todoktodok.presentation.view.book.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemSearchedBooksCountBinding

class SearchBooksCountViewHolder private constructor(
    val binding: ItemSearchedBooksCountBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(size: Int) {
        binding.tvSearchedBooksCount.text =
            binding.root.context.getString(R.string.select_book_searched_books_count, size)
    }

    companion object {
        fun SearchBooksCountViewHolder(parent: ViewGroup): SearchBooksCountViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemSearchedBooksCountBinding.inflate(inflater, parent, false)
            return SearchBooksCountViewHolder(binding)
        }
    }
}
