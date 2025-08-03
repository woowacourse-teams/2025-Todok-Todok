package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemUserHistoryBinding
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems

class UserHistoryViewHolder private constructor(
    binding: ItemUserHistoryBinding,
    handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: ProfileItems.HistoryItem) {
    }

    companion object {
        fun UserHistoryViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserHistoryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserHistoryBinding.inflate(inflater, parent, false)
            return UserHistoryViewHolder(binding, handler)
        }
    }

    interface Handler
}
