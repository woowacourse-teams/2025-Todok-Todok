package com.team.ui_xml.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.ui_xml.profile.UserProfileTab
import com.team.ui_xml.profile.activated.ActivatedBooksFragment
import com.team.ui_xml.profile.created.CreatedDiscussionsRoomFragment
import com.team.ui_xml.profile.participated.ParticipatedDiscussionsRoomFragment

class ContentPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val tab = UserProfileTab.Companion.UserProfileTab(position)

        return when (tab) {
            UserProfileTab.ACTIVATED_BOOKS ->
                ActivatedBooksFragment()

            UserProfileTab.CREATED_DISCUSSIONS ->
                CreatedDiscussionsRoomFragment()

            UserProfileTab.PARTICIPATED_DISCUSSIONS ->
                ParticipatedDiscussionsRoomFragment()
        }
    }

    override fun getItemCount(): Int = UserProfileTab.entries.size
}
