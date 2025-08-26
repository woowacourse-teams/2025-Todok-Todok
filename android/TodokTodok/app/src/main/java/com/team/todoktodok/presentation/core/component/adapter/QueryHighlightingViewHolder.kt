package com.team.todoktodok.presentation.core.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class QueryHighlightingViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: DiscussionUiState) {
    }

    companion object {
        fun QueryHighlightingViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): QueryHighlightingViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return QueryHighlightingViewHolder(binding, handler)
        }
    }
}
