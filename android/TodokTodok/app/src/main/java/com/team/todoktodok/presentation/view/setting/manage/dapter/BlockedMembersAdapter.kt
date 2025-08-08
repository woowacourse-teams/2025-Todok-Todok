package com.team.todoktodok.presentation.view.setting.manage.dapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.member.BlockedMember
import com.team.todoktodok.presentation.view.setting.manage.dapter.BlockedMemberViewHolder.Companion.BlockedMemberViewHolder

class BlockedMembersAdapter(
    private val handler: Handler,
) : ListAdapter<BlockedMember, BlockedMemberViewHolder>(BlockedMembersDiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BlockedMemberViewHolder = BlockedMemberViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: BlockedMemberViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : BlockedMemberViewHolder.Handler
}
