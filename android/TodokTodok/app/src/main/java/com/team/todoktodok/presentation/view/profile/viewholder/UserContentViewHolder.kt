package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemUserContentBinding
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

class UserContentViewHolder private constructor(
    binding: ItemUserContentBinding,
    handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ProfileItems.ContentItem) {
    }

    companion object {
        fun UserContentViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserContentViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserContentBinding.inflate(inflater, parent, false)
            return UserContentViewHolder(binding, handler)
        }
    }

    interface Handler
}
