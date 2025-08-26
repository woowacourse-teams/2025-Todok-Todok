package com.team.todoktodok.presentation.view.profile.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team.domain.model.Book
import com.team.domain.model.Discussion
import com.team.todoktodok.presentation.view.profile.UserProfileTab
import com.team.todoktodok.presentation.view.profile.UserProfileTab.Companion.UserProfileTab
import com.team.todoktodok.presentation.view.profile.activated.ActivatedBooksFragment
import com.team.todoktodok.presentation.view.profile.created.CreatedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.profile.participated.ParticipatedDiscussionsRoomFragment
import com.team.todoktodok.presentation.view.serialization.toSerialization

class ContentPagerAdapter(
    private val activatedBooks: List<Book>,
    private val createdDiscussions: List<Discussion>,
    private val participatedDiscussions: List<Discussion>,
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun createFragment(position: Int): Fragment {
        val tab = UserProfileTab(position)
        val serializedBook = activatedBooks.map { it.toSerialization() }
        val serializeCreatedDiscussion = createdDiscussions.map { it.toSerialization() }
        val serializeParticipatedDiscussion = participatedDiscussions.map { it.toSerialization() }

        return when (tab) {
            UserProfileTab.ACTIVATED_BOOKS ->
                ActivatedBooksFragment.newInstance(serializedBook)

            UserProfileTab.CREATED_DISCUSSIONS ->
                CreatedDiscussionsRoomFragment.newInstance(serializeCreatedDiscussion)

            UserProfileTab.PARTICIPATED_DISCUSSIONS ->
                ParticipatedDiscussionsRoomFragment.newInstance(serializeParticipatedDiscussion)
        }
    }

    override fun getItemCount(): Int = UserProfileTab.entries.size
}
