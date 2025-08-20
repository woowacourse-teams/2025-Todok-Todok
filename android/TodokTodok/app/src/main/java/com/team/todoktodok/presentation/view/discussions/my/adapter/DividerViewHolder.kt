package com.team.todoktodok.presentation.view.discussions.my.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.databinding.ItemMyDividerBinding

class DividerViewHolder private constructor(
    binding: ItemMyDividerBinding,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        fun DividerViewHolder(parent: ViewGroup): DividerViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemMyDividerBinding.inflate(inflater, parent, false)
            return DividerViewHolder(binding)
        }
    }
}
