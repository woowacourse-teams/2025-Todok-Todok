package com.team.ui_xml.profile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.ui_xml.profile.UserProfileTab
import com.team.ui_xml.profile.viewholder.UserInformationViewHolder
import com.team.ui_xml.profile.viewholder.UserProfileHeaderViewHolder
import com.team.ui_xml.profile.viewholder.UserTabViewHolder

class ProfileAdapter(
    private val handler: Handler,
    private val viewPagerAdapter: FragmentStateAdapter,
    private val initialTab: UserProfileTab,
) : ListAdapter<ProfileItems, RecyclerView.ViewHolder>(ProfileDiffUtil()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder =
        when (ProfileItems.ViewType.Companion.ViewType(viewType)) {
            ProfileItems.ViewType.HEADER ->
                UserProfileHeaderViewHolder.Companion.UserProfileHeaderViewHolder(
                    parent,
                    handler,
                )
            ProfileItems.ViewType.INFORMATION ->
                UserInformationViewHolder.Companion.UserInformationViewHolder(
                    parent,
                    handler,
                )
            ProfileItems.ViewType.TAB ->
                UserTabViewHolder.Companion.UserTabViewHolder(
                    parent,
                    viewPagerAdapter,
                )
        }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.sequence

    override fun onBindViewHolder(
        holder: RecyclerView.ViewHolder,
        position: Int,
    ) {
        when (val item = getItem(position)) {
            is ProfileItems.HeaderItem -> (holder as UserProfileHeaderViewHolder).bind(item)
            is ProfileItems.TabItem -> (holder as UserTabViewHolder).bind(initialTab)
            is ProfileItems.InformationItem -> (holder as UserInformationViewHolder).bind(item)
        }
    }

    interface Handler :
        UserProfileHeaderViewHolder.Handler,
        UserInformationViewHolder.Handler
}
