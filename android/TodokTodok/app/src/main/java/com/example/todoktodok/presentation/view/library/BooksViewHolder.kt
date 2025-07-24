package com.example.todoktodok.presentation.view.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoktodok.databinding.ItemBookBinding
import com.example.todoktodok.state.BookState

class BooksViewHolder private constructor(
    private val binding: ItemBookBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: BookState) {
        with(binding) {
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
            item.bookImage(ivBookCover, root.context)
        }
    }

    companion object {
        fun BooksViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): BooksViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
            return BooksViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onSelectBook(position: Int)
    }
}
