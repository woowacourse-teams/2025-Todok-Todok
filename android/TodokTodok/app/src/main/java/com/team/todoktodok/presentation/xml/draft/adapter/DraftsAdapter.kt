package com.team.todoktodok.presentation.xml.draft.adapter

import android.view.ViewGroup
import com.team.domain.model.discussionroom.DiscussionRoom

class DraftsAdapter(
    private val selectDraft: DraftsViewHolder.SelectDraftClickListener,
) : androidx.recyclerview.widget.ListAdapter<DiscussionRoom, DraftsViewHolder>(DraftsDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DraftsViewHolder = DraftsViewHolder.Companion.DraftsViewHolder(parent, selectDraft)

    override fun onBindViewHolder(
        holder: DraftsViewHolder,
        position: Int,
    ) = holder.bind(getItem(position))
}
