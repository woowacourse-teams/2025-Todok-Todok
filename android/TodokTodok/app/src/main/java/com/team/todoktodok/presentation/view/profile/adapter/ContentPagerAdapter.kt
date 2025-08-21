package com.team.todoktodok.presentation.view.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.todoktodok.presentation.view.profile.UserProfileTab
import com.team.todoktodok.presentation.view.profile.UserProfileTab.Companion.UserProfileTab
import com.team.todoktodok.presentation.view.profile.activated.ActivatedBooksFragment
import com.team.todoktodok.presentation.view.profile.created.CreatedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.profile.participated.ParticipatedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.serialization.SerializationBook
import com.team.todoktodok.presentation.view.serialization.SerializationDiscussion

class ContentPagerAdapter(
    private val activatedBooks: List<SerializationBook>,
    private val createdDiscussions: List<SerializationDiscussion>,
    private val participatedDiscussions: List<SerializationDiscussion>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val tab = UserProfileTab(position)
        return when (tab) {
            UserProfileTab.ACTIVATED_BOOKS -> ActivatedBooksFragment.newInstance(activatedBooks)
            UserProfileTab.CREATED_DISCUSSIONS -> CreatedDiscussionsRoomFragment.newInstance(createdDiscussions)
            UserProfileTab.PARTICIPATED_DISCUSSIONS -> ParticipatedDiscussionsRoomFragment.newInstance(participatedDiscussions)
        }
    }

    override fun getItemCount(): Int = UserProfileTab.entries.size
}
