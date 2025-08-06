package com.team.todoktodok.presentation.view.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.todoktodok.presentation.view.profile.UserProfileTab
import com.team.todoktodok.presentation.view.profile.UserProfileTab.Companion.UserProfileTab
import com.team.todoktodok.presentation.view.profile.activated.ActivatedBooksFragment
import com.team.todoktodok.presentation.view.profile.created.CreatedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.profile.joined.JoinedDiscussionsRoomFragment

class ContentPagerAdapter(
    private val memberId: Long?,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val tab = UserProfileTab(position)
        return when (tab) {
            UserProfileTab.ACTIVATED_BOOKS -> ActivatedBooksFragment()
            UserProfileTab.CREATED_DISCUSSIONS -> CreatedDiscussionsRoomFragment.newInstance(memberId)
            UserProfileTab.JOINED_DISCUSSIONS -> JoinedDiscussionsRoomFragment.newInstance(memberId)
        }
    }

    override fun getItemCount(): Int = UserProfileTab.entries.size
}
