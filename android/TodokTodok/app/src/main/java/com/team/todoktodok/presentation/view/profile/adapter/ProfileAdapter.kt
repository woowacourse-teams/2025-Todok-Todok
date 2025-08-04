package com.team.todoktodok.presentation.view.profile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.todoktodok.presentation.view.profile.adapter.ProfileItems.ViewType.Companion.ViewType
import com.team.todoktodok.presentation.view.profile.viewholder.UserInformationViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserInformationViewHolder.Companion.UserInformationViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserProfileHeaderViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserProfileHeaderViewHolder.Companion.UserProfileHeaderViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserTabViewHolder
import com.team.todoktodok.presentation.view.profile.viewholder.UserTabViewHolder.Companion.UserTabViewHolder

class ProfileAdapter(
    private val handler: Handler,
    private val viewPagerAdapter: FragmentStateAdapter,
) : ListAdapter<ProfileItems, RecyclerView.ViewHolder>(ProfileDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (ViewType(viewType)) {
            ProfileItems.ViewType.HEADER -> UserProfileHeaderViewHolder(parent, handler)
            ProfileItems.ViewType.INFORMATION -> UserInformationViewHolder(parent, handler)
            ProfileItems.ViewType.TAB -> UserTabViewHolder(parent, viewPagerAdapter)
        }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.sequence

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            ProfileItems.HeaderItem -> return
            is ProfileItems.TabItem -> (holder as UserTabViewHolder).bind()
            is ProfileItems.InformationItem -> (holder as UserInformationViewHolder).bind(item)
        }
    }

    interface Handler :
        UserProfileHeaderViewHolder.Handler,
        UserInformationViewHolder.Handler
}
