package com.team.todoktodok.presentation.view.profile.activated

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
        binding.root.setOnClickListener {
            handler.onSelectBook(absoluteAdapterPosition)
        }
    }

    fun bind(item: Book) {
        adjustItemSize()

        Glide
            .with(binding.root)
            .load("https://image.aladin.co.kr/product/33413/26/coversum/k582938339_1.jpg")
            .into(binding.ivBook)
    }

    private fun adjustItemSize() {
        val resource = itemView.context.resources
        val displayMetrics = resource.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val spanCount = 3
        val spacing = resource.getDimensionPixelSize(R.dimen.space_12)
        val totalSpacing = spacing * (spanCount + 1)
        val itemWidth = (screenWidth - totalSpacing) / spanCount

        itemView.layoutParams =
            itemView.layoutParams.apply {
                width = itemWidth
                height = (itemWidth * 1.2).toInt()
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
    }

    fun interface Handler {
        fun onSelectBook(index: Int)
    }
}
