package com.team.todoktodok.presentation.view.profile.joined

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.domain.model.member.MemberId.Companion.DEFAULT_MEMBER_ID
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentCreatedDiscussionsRoomBinding
import com.team.todoktodok.databinding.FragmentJoinedDiscussionsRoomBinding
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.ARG_MEMBER_ID
import com.team.todoktodok.presentation.view.profile.ProfileActivity.Companion.MEMBER_ID_NOT_FOUND
import com.team.todoktodok.presentation.view.profile.created.MemberDiscussionUiEvent
import com.team.todoktodok.presentation.view.profile.created.adapter.UserDiscussionAdapter
import com.team.todoktodok.presentation.view.profile.joined.vm.JoinedDiscussionsViewModel
import com.team.todoktodok.presentation.view.profile.joined.vm.JoinedDiscussionsViewModelFactory
import kotlin.getValue

class JoinedDiscussionsRoomFragment : Fragment(R.layout.fragment_joined_discussions_room) {
    private var _binding: FragmentJoinedDiscussionsRoomBinding? = null
    val binding get() = _binding!!

    private val viewModel: JoinedDiscussionsViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        JoinedDiscussionsViewModelFactory(repositoryModule.memberRepository)
    }

    private lateinit var discussionAdapter: UserDiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentJoinedDiscussionsRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentCreatedDiscussionsRoomBinding.bind(view)

        initView(binding)
        setUpUiState()
        setUpUiEvent()
    }

    private fun initView(binding: FragmentCreatedDiscussionsRoomBinding) {
        discussionAdapter = UserDiscussionAdapter(userDiscussionAdapterHandler)

        val memberId: Long? = arguments?.getLong(ARG_MEMBER_ID, DEFAULT_MEMBER_ID)
        requireNotNull(memberId) { MEMBER_ID_NOT_FOUND }
        viewModel.loadDiscussions(memberId)

        binding.rvDiscussions.adapter = discussionAdapter
    }

    private fun setUpUiState() {
        viewModel.discussion.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value)
        }
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
            when (event) {
                is MemberDiscussionUiEvent.NavigateToDetail -> {
                    val intent = DiscussionDetailActivity.Intent(requireContext(), event.discussionId)
                    startActivity(intent)
                }
            }
        }
    }

    private val userDiscussionAdapterHandler =
        object : UserDiscussionAdapter.Handler {
            override fun onSelectDiscussion(index: Int) {
                viewModel.findSelectedDiscussion(index)
            }
        }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(memberId: Long): JoinedDiscussionsRoomFragment =
            JoinedDiscussionsRoomFragment().apply {
                arguments = bundleOf(ARG_MEMBER_ID to memberId)
            }
    }
}
