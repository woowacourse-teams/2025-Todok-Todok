package com.team.todoktodok.presentation.view.discussion.discussions.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.domain.model.Discussion
import com.team.todoktodok.databinding.ItemDiscussionBinding

class DiscussionViewHolder private constructor(
    private val binding: ItemDiscussionBinding,
    private val navigateDiscussionClick: OnDiscussionListener,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(discussion: Discussion) {
        with(binding) {
            Glide.with(binding.root.context).load(discussion.book.image).into(ivBookImage)
            tvBookTitle.text = discussion.book.title
            tvTitle.text = discussion.discussionTitle
            tvOpinionSummary.text = discussion.discussionOpinion
            root.setOnClickListener {
                navigateDiscussionClick.navigateToDiscussion(
                    discussion.id,
                )
            }
        }
    }

    companion object {
        fun DiscussionViewHolder(
            parent: ViewGroup,
            navigateDiscussionClick: OnDiscussionListener,
        ): DiscussionViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionBinding.inflate(layoutInflater, parent, false)
            return DiscussionViewHolder(binding, navigateDiscussionClick)
        }
    }
}
