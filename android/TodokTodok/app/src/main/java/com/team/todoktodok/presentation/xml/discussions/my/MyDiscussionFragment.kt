package com.team.todoktodok.presentation.xml.discussions.my

import android.os.Bundle
import android.view.View
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentMyDiscussionBinding
import com.team.todoktodok.presentation.xml.discussions.BaseDiscussionsFragment
import com.team.todoktodok.presentation.xml.discussions.my.adapter.MyDiscussionAdapter
import com.team.todoktodok.presentation.xml.profile.ProfileActivity
import com.team.todoktodok.presentation.xml.profile.UserProfileTab

class MyDiscussionFragment : BaseDiscussionsFragment(R.layout.fragment_my_discussion) {
    private val discussionAdapter: MyDiscussionAdapter by lazy {
        MyDiscussionAdapter(adapterHandler)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentMyDiscussionBinding.bind(view)

        initView(binding)
        setUpUiState(binding)
    }

    private fun initView(binding: FragmentMyDiscussionBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.hasFixedSize()
        }
    }

    private fun setUpUiState(binding: FragmentMyDiscussionBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            if (value.myDiscussion.isEmpty()) {
                displayNoResultsView(binding)
            } else {
                displayResultsView(binding)
                discussionAdapter.submitList(value.myDiscussion.items)
            }
        }
    }

    private fun displayResultsView(binding: FragmentMyDiscussionBinding) =
        with(binding) {
            viewResourceNotFound.hide()
            rvDiscussions.visibility = View.VISIBLE
        }

    private fun displayNoResultsView(binding: FragmentMyDiscussionBinding) =
        with(binding) {
            viewResourceNotFound.show(
                getString(R.string.profile_not_has_created_discussion_title),
                getString(R.string.profile_not_has_created_discussion_subtitle),
            )
            rvDiscussions.visibility = View.GONE
        }

    override fun onResume() {
        super.onResume()
        viewModel.loadMyDiscussions()
    }

    private val adapterHandler =
        object : MyDiscussionAdapter.Handler {
            override fun onClickMyCreatedDiscussionHeader() {
                startActivity(
                    ProfileActivity.Intent(
                        requireContext(),
                        initialTab = UserProfileTab.CREATED_DISCUSSIONS,
                    ),
                )
            }

            override fun onClickMyParticipatedDiscussionHeader() {
                startActivity(
                    ProfileActivity.Intent(
                        requireContext(),
                        initialTab = UserProfileTab.PARTICIPATED_DISCUSSIONS,
                    ),
                )
            }

            override fun onClickMyCreatedDiscussionItem(discussionId: Long) {
                moveToDiscussionDetail(discussionId)
            }

            override fun onClickMyParticipatedDiscussionItem(discussionId: Long) {
                moveToDiscussionDetail(discussionId)
            }
        }
}
