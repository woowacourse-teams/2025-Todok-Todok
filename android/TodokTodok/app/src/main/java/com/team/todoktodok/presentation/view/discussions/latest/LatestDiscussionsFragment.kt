package com.team.todoktodok.presentation.view.discussions.latest

import android.os.Bundle
import android.view.View
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentLatestDiscussionsBinding
import com.team.todoktodok.presentation.core.ext.addOnScrollEndListener
import com.team.todoktodok.presentation.view.discussions.BaseDiscussionsFragment
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter

class LatestDiscussionsFragment : BaseDiscussionsFragment(R.layout.fragment_latest_discussions) {
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(handler = adapterHandler)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentLatestDiscussionsBinding.bind(view)
        initView(binding)
        setUpUiState(binding)
        viewModel.loadLatestDiscussions()
    }

    private fun initView(binding: FragmentLatestDiscussionsBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.setHasFixedSize(true)
        }
    }

    private fun setUpUiState(binding: FragmentLatestDiscussionsBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.latestDiscussion.items)
            binding.rvDiscussions.addOnScrollEndListener {
                viewModel.loadLatestDiscussions()
            }
        }
    }

    private val adapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                val discussionId = discussionAdapter.currentList[index].discussionId
                moveToDiscussionDetail(discussionId)
            }
        }

    companion object {
        const val TAG = "LatestDiscussionsFragment"
    }
}
