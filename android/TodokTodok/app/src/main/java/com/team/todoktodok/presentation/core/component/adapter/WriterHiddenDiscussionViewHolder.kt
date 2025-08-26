package com.team.todoktodok.presentation.core.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class WriterHiddenDiscussionViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: DiscussionUiState) =
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
