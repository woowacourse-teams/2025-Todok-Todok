package com.team.todoktodok.presentation.view.profile.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.team.todoktodok.databinding.ItemUserTabBinding
import com.team.todoktodok.presentation.view.profile.UserProfileTab
import com.team.todoktodok.presentation.view.profile.UserProfileTab.Companion.UserProfileTab

class UserTabViewHolder private constructor(
    binding: ItemUserTabBinding,
    private val handler: Handler,
) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.tab.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val selectedTab = UserProfileTab(tab?.position)
                    handler.onSelectTab(selectedTab)
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            },
        )
    }

    companion object {
        fun UserTabViewHolder(
            parent: ViewGroup,
            handler: Handler,
        ): UserTabViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemUserTabBinding.inflate(inflater, parent, false)
            return UserTabViewHolder(binding, handler)
        }
    }

    interface Handler {
        fun onSelectTab(tab: UserProfileTab)
    }
}
