package com.team.todoktodok.presentation.view.discussions.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.Discussion

class DiscussionAdapter(
    private val handler: Handler,
) : ListAdapter<Discussion, DiscussionViewHolder>(DiscussionDiffUtil()) {
    private var searchKeyword: String? = null

    fun submitList(
        items: List<Discussion>,
        keyword: String,
    ) {
        val isKeywordChanged = searchKeyword != keyword
        val isItemsChanged = currentList != items

        searchKeyword = keyword
        when {
            isItemsChanged ->
                submitList(items) {
                    if (isKeywordChanged) notifyItemRangeChanged(0, itemCount)
                }
            isKeywordChanged -> notifyItemRangeChanged(0, itemCount)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DiscussionViewHolder = DiscussionViewHolder.DiscussionViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: DiscussionViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position), searchKeyword)
    }

    interface Handler : DiscussionViewHolder.Handler
}
