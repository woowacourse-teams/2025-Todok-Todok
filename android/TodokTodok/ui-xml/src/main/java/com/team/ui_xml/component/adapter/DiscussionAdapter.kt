package com.team.ui_xml.component.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.domain.model.Discussion
import com.team.ui_xml.component.adapter.DefaultDiscussionViewHolder.Companion.DefaultDiscussionViewHolder
import com.team.ui_xml.component.adapter.ResizingDiscussionViewHolder.Companion.ResizingDiscussionViewHolder
import com.team.ui_xml.component.adapter.WriterHiddenDiscussionViewHolder.Companion.WriterHiddenDiscussionViewHolder

class DiscussionAdapter(
    private val handler: Handler,
    private val type: BaseDiscussionViewHolder.ViewHolderType,
) : ListAdapter<Discussion, BaseDiscussionViewHolder>(DiscussionDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseDiscussionViewHolder =
        when (type) {
            BaseDiscussionViewHolder.ViewHolderType.DEFAULT ->
                DefaultDiscussionViewHolder(parent, handler)

            BaseDiscussionViewHolder.ViewHolderType.WRITER_HIDDEN ->
                WriterHiddenDiscussionViewHolder(parent, handler)

            BaseDiscussionViewHolder.ViewHolderType.RESIZING ->
                ResizingDiscussionViewHolder(parent, handler)
        }

    override fun onBindViewHolder(
        holder: BaseDiscussionViewHolder,
        position: Int,
    ) {
        holder.bind(getItem(position))
    }

    interface Handler : BaseDiscussionViewHolder.Handler
}
