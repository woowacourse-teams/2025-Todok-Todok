package com.team.todoktodok.presentation.core.component.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.team.todoktodok.databinding.ItemDiscussionBinding
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState

class ResizingDiscussionViewHolder private constructor(
    binding: ItemDiscussionBinding,
    handler: Handler,
) : BaseDiscussionViewHolder(binding, handler) {
    override fun bindExtra(item: DiscussionUiState) =
        with(binding) {
            tvDiscussionOpinion.text = item.discussionOpinion
            tvDiscussionOpinion.maxLines = 2
            tvDiscussionOpinion.minLines = 2
            adjustItemSize()
        }

    private fun adjustItemSize() {
        val resource = itemView.context.resources
        val displayMetrics = resource.displayMetrics
        val screenWidth = displayMetrics.widthPixels

        val params = itemView.layoutParams
        params.width = (screenWidth * 0.7).toInt()
        itemView.layoutParams = params
    }

    companion object {
        fun ResizingDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): ResizingDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(inflater, parent, false)
            return ResizingDiscussionViewHolder(binding, handler)
        }
    }
}
