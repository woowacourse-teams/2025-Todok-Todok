package com.team.ui_xml.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team.ui_xml.databinding.ItemUserTabBinding
import com.team.ui_xml.profile.UserProfileTab

class UserTabViewHolder private constructor(
    private val viewPagerAdapter: FragmentStateAdapter,
    private val binding: ItemUserTabBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    private val tabLayout: TabLayout = binding.tab
    private val viewPager: ViewPager2 = binding.viewPager

    fun bind(initialTab: UserProfileTab) {
        viewPager.adapter = viewPagerAdapter
        binding.viewPager.currentItem = initialTab.ordinal

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabTitle = context.getString(UserProfileTab.Companion.UserProfileTab(position).titleResourceId)
            tab.text = tabTitle
        }.attach()
    }

    companion object {
        fun UserTabViewHolder(
            parent: ViewGroup,
            viewPagerAdapter: FragmentStateAdapter,
        ): UserTabViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserTabBinding.inflate(inflater, parent, false)
            return UserTabViewHolder(viewPagerAdapter, binding)
        }
    }
}
