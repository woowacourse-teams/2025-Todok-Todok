package com.example.todoktodok.presentation.view.discussion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.domain.model.DiscussionRoom
import com.example.todoktodok.databinding.ItemDiscussionRoomBinding

class DiscussionRoomViewHolder private constructor(private val binding: ItemDiscussionRoomBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(discussionRoom: DiscussionRoom) {
        with(binding) {
            Glide.with(binding.root.context).load(discussionRoom.book.image).into(ivImage)
            tvBookTitle.text = discussionRoom.book.title
            tvTitle.text = discussionRoom.discussionTitle
            tvOpinionSummary.text = discussionRoom.discussionOpinion
        }
    }

    companion object {
        fun create(parent: ViewGroup): DiscussionRoomViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemDiscussionRoomBinding.inflate(layoutInflater, parent, false)
            return DiscussionRoomViewHolder(binding)
        }
    }
}