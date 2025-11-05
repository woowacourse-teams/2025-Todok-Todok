package com.team.ui_xml.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.team.domain.model.Discussion
import com.team.ui_xml.databinding.ItemDiscussionBinding

class DefaultDiscussionViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: Discussion) = Unit

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
