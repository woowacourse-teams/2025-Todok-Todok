package com.team.todoktodok.presentation.view.discussions.all

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.team.todoktodok.R
import com.team.todoktodok.presentation.view.discussions.latest.LatestDiscussionsFragment
import com.team.todoktodok.presentation.view.discussions.search.SearchDiscussionsFragment

class AllDiscussionFragment : Fragment(R.layout.fragment_all_discussion) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.commit {
                add(R.id.fcv_all_discussion, LatestDiscussionsFragment(), LatestDiscussionsFragment.TAG)
            }
        }
    }

    fun showLatestDiscussions() {
        val current = childFragmentManager.findFragmentById(R.id.fcv_all_discussion)
        if (current is LatestDiscussionsFragment) return

        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fcv_all_discussion,
                LatestDiscussionsFragment(),
                LatestDiscussionsFragment.TAG,
            )
        }
    }

    fun showSearchResults() {
        val current = childFragmentManager.findFragmentById(R.id.fcv_all_discussion)
        if (current is SearchDiscussionsFragment) return

        childFragmentManager.commit {
            setReorderingAllowed(true)
            replace(
                R.id.fcv_all_discussion,
                SearchDiscussionsFragment(),
                SearchDiscussionsFragment.TAG,
            )
        }
    }
}
