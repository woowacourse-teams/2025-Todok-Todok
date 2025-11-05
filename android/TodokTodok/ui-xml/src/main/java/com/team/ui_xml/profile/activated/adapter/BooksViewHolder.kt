package com.team.ui_xml.profile.activated.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.domain.model.Book
import com.team.ui_xml.R
import com.team.ui_xml.databinding.ItemActivatedBookBinding

class BooksViewHolder private constructor(
    private val binding: ItemActivatedBookBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        adjustItemSize()
    }

    fun bind(item: Book) {
        with(binding) {
            tvBookTitle.text = item.extractSubtitle()
            tvBookAuthor.text = extractAuthorText(item.author)
            root.setOnClickListener {
                handler.onSelectBook(item.id)
            }

            Glide
                .with(root)
                .load(item.image)
                .into(ivBook)
        }
    }

    private fun extractAuthorText(text: String): String = text.split("(").first()

    private fun adjustItemSize() {
        val resource = itemView.context.resources
        val displayMetrics = resource.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val spanCount = SPAN_COUNT
        val spacing = resource.getDimensionPixelSize(R.dimen.space_4)
        val totalSpacing = spacing * (spanCount + 1)
        val itemWidth = (screenWidth - totalSpacing) / spanCount

        itemView.layoutParams =
            itemView.layoutParams.apply {
                width = itemWidth
                height = ViewGroup.LayoutParams.WRAP_CONTENT
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

        private const val SPAN_COUNT = 3
    }

    fun interface Handler {
        fun onSelectBook(bookId: Long)
    }
}
