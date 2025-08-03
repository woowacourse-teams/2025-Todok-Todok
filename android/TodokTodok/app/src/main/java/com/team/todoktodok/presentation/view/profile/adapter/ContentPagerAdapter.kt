package com.team.todoktodok.presentation.view.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.todoktodok.presentation.view.profile.ActivatedBooksFragment
import com.team.todoktodok.presentation.view.profile.CreatedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.profile.JoinedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.profile.UserProfileTab
import com.team.todoktodok.presentation.view.profile.UserProfileTab.Companion.UserProfileTab

class ContentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val tab = UserProfileTab(position)
        return when (tab) {
            UserProfileTab.ACTIVATED_BOOKS -> ActivatedBooksFragment()
            UserProfileTab.CREATED_DISCUSSIONS -> CreatedDiscussionsRoomFragment()
            UserProfileTab.JOINED_DISCUSSIONS -> JoinedDiscussionsRoomFragment()
        }
    }

    override fun getItemCount(): Int = TAB_COUNT

    private companion object {
        private const val TAB_COUNT = 3
    }
}
