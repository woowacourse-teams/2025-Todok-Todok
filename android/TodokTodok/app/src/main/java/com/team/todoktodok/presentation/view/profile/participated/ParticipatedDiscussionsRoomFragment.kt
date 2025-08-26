package com.team.todoktodok.presentation.view.profile.participated

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentParticipatedDiscussionsRoomBinding
import com.team.todoktodok.presentation.core.component.adapter.BaseDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.DiscussionUiState
import com.team.todoktodok.presentation.view.profile.BaseProfileFragment

class ParticipatedDiscussionsRoomFragment : BaseProfileFragment(R.layout.fragment_participated_discussions_room) {
    private var _binding: FragmentParticipatedDiscussionsRoomBinding? = null
    val binding get() = _binding!!

    private lateinit var discussionAdapter: DiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentParticipatedDiscussionsRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setupUiState()
    }

    private fun initView() {
        discussionAdapter =
            DiscussionAdapter(
                discussionAdapterHandler,
                BaseDiscussionViewHolder.ViewHolderType.DEFAULT,
            )
        binding.rvDiscussions.adapter = discussionAdapter
    }

    private fun setupUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            val discussions = value.participatedDiscussions
            if (discussions.isEmpty()) {
                showEmptyResourceView()
            } else {
                showDiscussion(discussions)
            }
        }
    }

    private fun showEmptyResourceView() {
        with(binding) {
            rvDiscussions.visibility = View.GONE
            viewResourceNotFound.show(
                getString(R.string.profile_not_has_participated_discussions_title),
                getString(R.string.profile_not_has_participated_discussions_subtitle),
                getString(R.string.profile_action_participated_discussion),
            ) { moveToDiscussions() }
        }
    }

    private fun showDiscussion(discussions: List<DiscussionUiState>) {
        binding.viewResourceNotFound.hide()
        binding.rvDiscussions.visibility = View.VISIBLE
        discussionAdapter.submitList(discussions)
    }

    private fun moveToDiscussions() {
        requireActivity().finish()
    }

    private val discussionAdapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                moveToDiscussionDetail(index)
            }
        }

    private fun moveToDiscussionDetail(index: Int) {
        val discussionId = discussionAdapter.currentList[index].discussionId
        val intent = DiscussionDetailActivity.Intent(requireContext(), discussionId)
        discussionDetailLauncher.launch(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
