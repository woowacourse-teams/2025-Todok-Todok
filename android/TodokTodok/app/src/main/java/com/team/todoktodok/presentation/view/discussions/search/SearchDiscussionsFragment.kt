package com.team.todoktodok.presentation.view.discussions.search

import android.os.Bundle
import android.view.View
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentSearchDiscussionsBinding
import com.team.todoktodok.presentation.view.discussions.BaseDiscussionsFragment
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter

class SearchDiscussionsFragment : BaseDiscussionsFragment(R.layout.fragment_search_discussions) {
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(handler = adapterHandler)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSearchDiscussionsBinding.bind(view)
        initView(binding)
        setUpUiState()
    }

    private fun initView(binding: FragmentSearchDiscussionsBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.setHasFixedSize(true)
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.searchDiscussion.items)
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
        const val TAG = "SearchDiscussionsFragment"
    }
}
