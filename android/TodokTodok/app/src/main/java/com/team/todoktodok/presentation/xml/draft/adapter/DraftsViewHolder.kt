package com.team.todoktodok.presentation.xml.draft.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.discussionroom.DiscussionRoom
import com.team.todoktodok.R
import com.team.todoktodok.databinding.ItemDraftBinding

class DraftsViewHolder private constructor(
    private val binding: ItemDraftBinding,
    private val selectDraft: SelectDraftClickListener,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            selectDraft.onClick(bindingAdapterPosition)
        }
    }

    fun bind(discussionRoom: DiscussionRoom) {
        binding.apply {
            tvDiscussionTitle.text =
                discussionRoom.title.ifBlank { binding.root.context.getString(R.string.drafts_no_title) }
            tvOpinion.text =
                discussionRoom.opinion.ifBlank { binding.root.context.getString(R.string.drafts_no_description) }
        }
    }

    companion object {
        fun DraftsViewHolder(
            parent: ViewGroup,
            selectDraft: SelectDraftClickListener,
        ): DraftsViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemDraftBinding.inflate(inflater, parent, false)
            return DraftsViewHolder(binding, selectDraft)
        }
    }

    fun interface SelectDraftClickListener {
        fun onClick(position: Int)
    }
}
