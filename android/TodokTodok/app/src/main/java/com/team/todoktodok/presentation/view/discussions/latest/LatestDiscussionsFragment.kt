package com.team.todoktodok.presentation.view.discussions.latest

import android.os.Bundle
import android.view.View
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentLatestDiscussionsBinding
import com.team.todoktodok.presentation.core.component.adapter.BaseDiscussionViewHolder
import com.team.todoktodok.presentation.core.component.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.core.ext.addOnScrollEndListener
import com.team.todoktodok.presentation.view.discussions.BaseDiscussionsFragment

class LatestDiscussionsFragment : BaseDiscussionsFragment(R.layout.fragment_latest_discussions) {
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(adapterHandler, BaseDiscussionViewHolder.ViewHolderType.DEFAULT)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentLatestDiscussionsBinding.bind(view)
        initView(binding)
        setUpUiState()
        viewModel.loadLatestDiscussions()
    }

    private fun initView(binding: FragmentLatestDiscussionsBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.setHasFixedSize(true)
            rvDiscussions.addOnScrollEndListener {
                viewModel.loadLatestDiscussions()
            }
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.latestDiscussion.items)
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
