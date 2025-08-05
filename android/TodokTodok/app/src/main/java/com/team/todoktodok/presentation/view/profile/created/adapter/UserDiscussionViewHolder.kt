package com.team.todoktodok.presentation.view.profile.created.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.Discussion
import com.team.todoktodok.databinding.ItemUserDiscussionBinding

class UserDiscussionViewHolder private constructor(
    private val binding: ItemUserDiscussionBinding,
    val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Discussion) {
        with(binding) {
            tvDiscussionTitle.text = item.discussionTitle
            tvBookTitle.text = item.book.title
            tvDiscussionContent.text = item.discussionOpinion
        }
    }

    companion object {
        fun UserDiscussionViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserDiscussionViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserDiscussionBinding.inflate(inflater, parent, false)
            return UserDiscussionViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onSelectDiscussion(index: Int)
    }
}
