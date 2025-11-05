package com.team.ui_xml.profile.created

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.team.domain.model.Discussion
import com.team.ui_xml.R
import com.team.ui_xml.component.adapter.BaseDiscussionViewHolder
import com.team.ui_xml.component.adapter.DiscussionAdapter
import com.team.ui_xml.databinding.FragmentCreatedDiscussionsRoomBinding
import com.team.ui_xml.discussiondetail.DiscussionDetailActivity
import com.team.ui_xml.profile.BaseProfileFragment
import com.team.core.R as coreR

class CreatedDiscussionsRoomFragment : BaseProfileFragment(R.layout.fragment_created_discussions_room) {
    private var _binding: FragmentCreatedDiscussionsRoomBinding? = null
    val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCreatedDiscussionsRoomBinding.inflate(inflater, container, false)
        return binding.root
    }

    private lateinit var discussionAdapter: DiscussionAdapter

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
                userDiscussionAdapterHandler,
                BaseDiscussionViewHolder.ViewHolderType.WRITER_HIDDEN,
            )
        binding.rvDiscussions.visibility = View.VISIBLE
        binding.rvDiscussions.adapter = discussionAdapter
    }

    private fun setupUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            val discussions = value.createdDiscussions

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
                getString(coreR.string.all_not_has_created_discussion_title),
            )
        }
    }

    private fun showDiscussion(discussions: List<Discussion>) {
        binding.viewResourceNotFound.hide()
        binding.rvDiscussions.visibility = View.VISIBLE
        discussionAdapter.submitList(discussions)
    }

    private val userDiscussionAdapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                moveToDiscussionDetail(index)
            }
        }

    private fun moveToDiscussionDetail(index: Int) {
        val discussionId = discussionAdapter.currentList.getOrNull(index)?.id ?: return
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
