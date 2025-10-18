package com.team.todoktodok.presentation.xml.setting.manage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.domain.model.member.BlockedMember
import com.team.todoktodok.databinding.ItemBlockedMemberBinding
import com.team.todoktodok.presentation.core.ext.formatDot

class BlockedMemberViewHolder private constructor(
    private val binding: ItemBlockedMemberBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.tvUnblock.setOnClickListener {
            handler.onUnblockClicked(absoluteAdapterPosition)
        }
    }

    fun bind(item: BlockedMember) {
        with(binding) {
            tvNickname.text = item.nickname
            tvDate.text = item.createdAt.formatDot()
        }
    }

    companion object {
        fun BlockedMemberViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): BlockedMemberViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemBlockedMemberBinding.inflate(inflater, parent, false)
            return BlockedMemberViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onUnblockClicked(index: Int)
    }
}
