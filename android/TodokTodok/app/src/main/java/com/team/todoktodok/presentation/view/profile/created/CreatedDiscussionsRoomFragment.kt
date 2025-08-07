package com.team.todoktodok.presentation.view.profile.created

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCreatedDiscussionsRoomBinding
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.ARG_MEMBER_ID
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.MEMBER_ID_NOT_FOUND
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionAdapter
import com.team.todoktodok.presentation.view.profile.created.vm.CreatedDiscussionsViewModel
import com.team.todoktodok.presentation.view.profile.created.vm.CreatedDiscussionsViewModelFactory

class CreatedDiscussionsRoomFragment : Fragment(R.layout.fragment_created_discussions_room) {
    private val viewModel: CreatedDiscussionsViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        CreatedDiscussionsViewModelFactory(repositoryModule.memberRepository)
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

        val memberId: Long? = arguments?.getLong(ARG_MEMBER_ID, INVALID_MEMBER_ID)
        requireNotNull(memberId) { MEMBER_ID_NOT_FOUND }
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
        fun newInstance(memberId: Long?): CreatedDiscussionsRoomFragment =
            CreatedDiscussionsRoomFragment().apply {
                memberId?.let {
                    arguments = bundleOf(ARG_MEMBER_ID to it)
                }
            }

        private const val INVALID_MEMBER_ID = -1L
    }
}
