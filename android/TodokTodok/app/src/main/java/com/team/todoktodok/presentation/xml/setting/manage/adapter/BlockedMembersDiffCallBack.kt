package com.team.todoktodok.presentation.xml.setting.manage.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.member.BlockedMember

class BlockedMembersDiffCallBack : DiffUtil.ItemCallback<BlockedMember>() {
    override fun areItemsTheSame(
        oldItem: BlockedMember,
        newItem: BlockedMember,
    ): Boolean = oldItem.memberId == newItem.memberId

    override fun areContentsTheSame(
        oldItem: BlockedMember,
        newItem: BlockedMember,
    ): Boolean = oldItem == newItem
}
