package com.example.todoktodok.presentation.view.library

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.domain.model.Book
import com.example.todoktodok.databinding.ItemBookBinding

class BooksViewHolder(
    private val binding: ItemBookBinding,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Book) {
        with(binding) {
            tvBookTitle.text = item.title
            tvBookAuthor.text = item.author
        }
    }

    companion object {
        fun create(parent: ViewGroup): BooksViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemBookBinding.inflate(layoutInflater, parent, false)
            return BooksViewHolder(binding)
        }
    }
}
