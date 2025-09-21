package com.team.todoktodok.presentation.xml.discussions.hot.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemHotActivatedDiscussionHeaderBinding

class HotActivatedDiscussionHeaderViewHolder private constructor(
    binding: ItemHotActivatedDiscussionHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun HotActivatedDiscussionHeaderViewHolder(parent: ViewGroup): HotActivatedDiscussionHeaderViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemHotActivatedDiscussionHeaderBinding.inflate(inflater, parent, false)
            return HotActivatedDiscussionHeaderViewHolder(binding)
        }
    }
}
