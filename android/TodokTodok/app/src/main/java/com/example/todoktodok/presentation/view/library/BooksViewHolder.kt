package com.example.todoktodok.presentation.view.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Book
import com.example.todoktodok.databinding.ItemBookBinding

class BooksViewHolder private constructor(
    private val binding: ItemBookBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onSelectBook(bindingAdapterPosition)
        }
    }

    fun bind(item: Book) {
        with(binding) {
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
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
