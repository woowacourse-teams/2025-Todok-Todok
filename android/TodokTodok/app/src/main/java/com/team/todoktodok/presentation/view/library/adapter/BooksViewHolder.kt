package com.team.todoktodok.presentation.view.library.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemBookBinding
import com.team.todoktodok.state.BookState

class BooksViewHolder private constructor(
    private val binding: ItemBookBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BookState) {
        with(binding) {
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
            item.bookImage(ivBookCover, root.context)
        }
    }

    companion object {
        fun BooksViewHolder(parent: ViewGroup): BooksViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
            return BooksViewHolder(binding)
        }
    }
}
