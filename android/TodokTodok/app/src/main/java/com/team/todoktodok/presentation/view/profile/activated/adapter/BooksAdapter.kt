package com.team.todoktodok.presentation.view.profile.activated.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Book
import com.team.todoktodok.presentation.view.profile.activated.adapter.BooksViewHolder.Companion.BooksViewHolder

class BooksAdapter(
    private val handler: Handler,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = mutableListOf<Book>()

    fun submitList(newItems: List<Book>) {
        items.clear()
        items.addAll(newItems)
        notifyItemRangeInserted(0, newItems.size)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder = BooksViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        (holder as BooksViewHolder).bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    interface Handler : BooksViewHolder.Handler
}
