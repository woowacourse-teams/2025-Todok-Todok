package com.team.ui_xml.component.adapter

import androidx.recyclerview.widget.DiffUtil
import com.team.domain.model.Discussion

class DiscussionDiffUtil : DiffUtil.ItemCallback<Discussion>() {
    override fun areItemsTheSame(
        oldItem: Discussion,
        newItem: Discussion,
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Discussion,
        newItem: Discussion,
    ): Boolean = oldItem == newItem
}
