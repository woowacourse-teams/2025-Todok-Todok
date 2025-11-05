package com.team.ui_xml.setting.manage.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.member.BlockedMember

class BlockedMembersAdapter(
    private val handler: Handler,
) : ListAdapter<BlockedMember, BlockedMemberViewHolder>(BlockedMembersDiffCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BlockedMemberViewHolder = BlockedMemberViewHolder.Companion.BlockedMemberViewHolder(parent, handler)

    override fun onBindViewHolder(
        holder: BlockedMemberViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : BlockedMemberViewHolder.Handler
}
