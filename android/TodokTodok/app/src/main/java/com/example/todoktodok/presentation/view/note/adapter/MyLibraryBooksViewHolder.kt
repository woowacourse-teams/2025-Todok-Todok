package com.example.todoktodok.presentation.view.note.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoktodok.databinding.ItemBookBinding
import com.example.todoktodok.state.BookState

class MyLibraryBooksViewHolder private constructor(
    private val binding: ItemBookBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            handler.onSelect(bindingAdapterPosition)
        }
    }

    fun bind(item: BookState) {
        with(binding) {
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
            item.bookImage(ivBookCover, root.context)
        }
    }

    companion object {
        fun MyLibraryBooksViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): MyLibraryBooksViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
            return MyLibraryBooksViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onSelect(index: Int)
    }
}
