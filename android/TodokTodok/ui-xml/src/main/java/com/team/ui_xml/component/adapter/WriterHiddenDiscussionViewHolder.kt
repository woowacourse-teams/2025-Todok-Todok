package com.team.ui_xml.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.team.domain.model.Discussion
import com.team.ui_xml.databinding.ItemDiscussionBinding

class WriterHiddenDiscussionViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: Discussion) =
        with(binding) {
            ivWriterProfileImage.isVisible = false
            tvDiscussionWriterNickname.isVisible = false

            tvDiscussionOpinion.text = item.discussionOpinion
        }

    companion object {
        fun WriterHiddenDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): WriterHiddenDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return WriterHiddenDiscussionViewHolder(binding, handler)
        }
    }
}
