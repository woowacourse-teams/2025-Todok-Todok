package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team.todoktodok.databinding.ItemUserTabBinding
import com.team.todoktodok.presentation.view.profile.UserProfileTab.Companion.UserProfileTab

class UserTabViewHolder private constructor(
    private val viewPagerAdapter: FragmentStateAdapter,
    binding: ItemUserTabBinding,
) : RecyclerView.ViewHolder(binding.root) {
    private val context = binding.root.context

    private val tabLayout: TabLayout = binding.tab
    private val viewPager: ViewPager2 = binding.viewPager

    fun bind() {
        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            val tabTitle = context.getString(UserProfileTab(position).titleResourceId)
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
