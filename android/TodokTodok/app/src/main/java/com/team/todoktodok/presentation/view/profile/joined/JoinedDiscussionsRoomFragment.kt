package com.team.todoktodok.presentation.view.profile.joined

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCreatedDiscussionsRoomBinding
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.ARG_MEMBER_ID
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionAdapter
import com.team.todoktodok.presentation.view.profile.joined.vm.JoinedDiscussionsViewModel
import com.team.todoktodok.presentation.view.profile.joined.vm.JoinedDiscussionsViewModelFactory
import kotlin.getValue

class JoinedDiscussionsRoomFragment : Fragment(R.layout.fragment_joined_discussions_room) {
    private val viewModel: JoinedDiscussionsViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        JoinedDiscussionsViewModelFactory(repositoryModule.memberRepository)
    }

    private lateinit var discussionAdapter: UserDiscussionAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCreatedDiscussionsRoomBinding.bind(view)
        initView(binding)
        setUpUiState()
    }

    private fun initView(binding: FragmentCreatedDiscussionsRoomBinding) {
        discussionAdapter = UserDiscussionAdapter(userDiscussionAdapterHandler)

        val memberId: String? = arguments?.getString(ARG_MEMBER_ID)
        viewModel.loadDiscussions(memberId)

        binding.rvDiscussions.adapter = discussionAdapter
    }

    private fun setUpUiState() {
        viewModel.discussion.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value)
        }
    }

    private val userDiscussionAdapterHandler =
        object : UserDiscussionAdapter.Handler {
            override fun onSelectDiscussion(index: Int) {
                TODO("Not yet implemented")
            }
        }

    companion object {
        // API 연동 완료시 제거
        fun newInstance(memberId: String? = "1"): JoinedDiscussionsRoomFragment =
            JoinedDiscussionsRoomFragment().apply {
                arguments = bundleOf(ARG_MEMBER_ID to memberId)
            }
    }
}
