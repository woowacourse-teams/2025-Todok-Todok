package com.team.todoktodok.presentation.core.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiModel

class DefaultDiscussionViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: DiscussionUiModel) = Unit

    companion object {
        fun DefaultDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): DefaultDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return DefaultDiscussionViewHolder(binding, handler)
        }
    }
}
