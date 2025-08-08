package com.team.todoktodok.presentation.view.profile.activated.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.domain.model.Book
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemActivatedBookBinding

class BooksViewHolder private constructor(
    private val binding: ItemActivatedBookBinding,
    handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        adjustItemSize()
        binding.root.setOnClickListener {
            handler.onSelectBook(absoluteAdapterPosition)
        }
    }

    fun bind(item: Book) {
        Glide
            .with(binding.root)
            .load(item.image)
            .into(binding.ivBook)
    }

    private fun adjustItemSize() {
        val resource = itemView.context.resources
        val displayMetrics = resource.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val spanCount = SPAN_COUNT
        val spacing = resource.getDimensionPixelSize(R.dimen.space_12)
        val totalSpacing = spacing * (spanCount + 1)
        val itemWidth = (screenWidth - totalSpacing) / spanCount

        itemView.layoutParams =
            itemView.layoutParams.apply {
                width = itemWidth
                height = (itemWidth * BOOK_ASPECT_RATIO).toInt()
            }
    }

    companion object {
        fun BooksViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): BooksViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemActivatedBookBinding.inflate(inflater, parent, false)
            return BooksViewHolder(binding, handler)
        }

        private const val BOOK_ASPECT_RATIO = 1.2f
        private const val SPAN_COUNT = 3
    }

    fun interface Handler {
        fun onSelectBook(index: Int)
    }
}
