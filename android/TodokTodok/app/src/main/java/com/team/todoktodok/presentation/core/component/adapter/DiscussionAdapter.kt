package com.team.todoktodok.presentation.core.component.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.team.todoktodok.presentation.compose.discussion.model.DiscussionUiState
import com.team.todoktodok.presentation.core.component.adapter.DefaultDiscussionViewHolder.Companion.DefaultDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.QueryHighlightingViewHolder.Companion.QueryHighlightingViewHolder
import com.team.todoktodok.presentation.core.component.adapter.ResizingDiscussionViewHolder.Companion.ResizingDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.WriterHiddenDiscussionViewHolder.Companion.WriterHiddenDiscussionViewHolder

class DiscussionAdapter(
    private val handler: Handler,
    private val type: BaseDiscussionViewHolder.ViewHolderType,
) : ListAdapter<DiscussionUiState, BaseDiscussionViewHolder>(DiscussionDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseDiscussionViewHolder =
        when (type) {
            BaseDiscussionViewHolder.ViewHolderType.DEFAULT ->
                DefaultDiscussionViewHolder(parent, handler)

            BaseDiscussionViewHolder.ViewHolderType.QUERY_HIGHLIGHTING ->
                QueryHighlightingViewHolder(parent, handler)

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
