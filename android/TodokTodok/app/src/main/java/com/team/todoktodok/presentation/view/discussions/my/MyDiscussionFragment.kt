package com.team.todoktodok.presentation.view.discussions.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentMyDiscussionBinding
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.my.adapter.MyDiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.view.profile.ProfileActivity
import com.team.todoktodok.presentation.view.profile.UserProfileTab

class MyDiscussionFragment : Fragment(R.layout.fragment_my_discussion) {
    private val discussionAdapter: MyDiscussionAdapter by lazy {
        MyDiscussionAdapter(adapterHandler)
    }
    private val viewModel: DiscussionsViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
        )
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

            override fun onClickMyCreatedDiscussionItem(discussionId: Long) {
                startActivity(DiscussionDetailActivity.Intent(requireContext(), discussionId))
            }

            override fun onClickMyParticipatedDiscussionHeader() {
                startActivity(
                    ProfileActivity.Intent(
                        requireContext(),
                        initialTab = UserProfileTab.PARTICIPATED_DISCUSSIONS,
                    ),
                )
            }

            override fun onClickMyParticipatedDiscussionItem(discussionId: Long) {
                startActivity(DiscussionDetailActivity.Intent(requireContext(), discussionId))
            }
        }
}
