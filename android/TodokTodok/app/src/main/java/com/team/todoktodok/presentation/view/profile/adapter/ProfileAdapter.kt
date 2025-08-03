package com.team.todoktodok.presentation.view.profile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.team.todoktodok.presentation.view.profile.viewholder.UserHistoryViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserHistoryViewHolder.Companion.UserHistoryViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserInformationViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserInformationViewHolder.Companion.UserInformationViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserProfileHeaderViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserProfileHeaderViewHolder.Companion.UserProfileHeaderViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserTabViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserTabViewHolder.Companion.UserTabViewHolder

class ProfileAdapter(
    private val handler: Handler,
) : ListAdapter<ProfileItems, RecyclerView.ViewHolder>(ProfileDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        val viewType = ProfileItems.ViewType.entries[viewType]
        return when (viewType) {
            ProfileItems.ViewType.VIEW_TYPE_HEADER ->
                UserProfileHeaderViewHolder(
                    parent,
                    handler,
                )

            ProfileItems.ViewType.VIEW_TYPE_INFORMATION ->
                UserInformationViewHolder(
                    parent,
                    handler,
                )

            ProfileItems.ViewType.VIEW_TYPE_TAB ->
                UserTabViewHolder(
                    parent,
                    handler,
                )

            ProfileItems.ViewType.VIEW_TYPE_HISTORY ->
                UserHistoryViewHolder(
                    parent,
                    handler,
                )
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.ordinal

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            ProfileItems.HeaderItem,
            ProfileItems.TabItem,
            -> Unit

            is ProfileItems.InformationItem -> (holder as UserInformationViewHolder).bind(item)
            is ProfileItems.HistoryItem -> (holder as UserHistoryViewHolder).bind(item)
        }
    }

    interface Handler :
        UserProfileHeaderViewHolder.Handler,
        UserInformationViewHolder.Handler,
        UserTabViewHolder.Handler,
        UserHistoryViewHolder.Handler
}
