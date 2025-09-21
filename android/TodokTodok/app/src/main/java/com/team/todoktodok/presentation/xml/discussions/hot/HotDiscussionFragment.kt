package com.team.todoktodok.presentation.xml.discussions.hot

import android.os.Bundle
import android.view.View
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentHotDiscussionBinding
import com.team.todoktodok.presentation.core.ext.addOnScrollEndListener
import com.team.todoktodok.presentation.xml.discussions.BaseDiscussionsFragment
import com.team.todoktodok.presentation.xml.discussions.hot.adapter.HotDiscussionAdapter

class HotDiscussionFragment : BaseDiscussionsFragment(R.layout.fragment_hot_discussion) {
    private lateinit var hotDiscussionAdapter: HotDiscussionAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHotDiscussionBinding.bind(view)

        viewModel.loadHotDiscussions()
        initView(binding)
        setupUiState()
    }

    private fun initView(binding: FragmentHotDiscussionBinding) {
        hotDiscussionAdapter = HotDiscussionAdapter(discussionAdapterHandler)
        with(binding.rvDiscussions) {
            adapter = hotDiscussionAdapter
            setHasFixedSize(true)
            addOnScrollEndListener { viewModel.loadActivatedDiscussions() }
        }
    }

    private fun setupUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            hotDiscussionAdapter.submitList(value.hotDiscussion.items)
        }
    }

    private val discussionAdapterHandler =
        object : HotDiscussionAdapter.Handler {
            override fun onClickHotPopularDiscussion(discussionId: Long) {
                moveToDiscussionDetail(discussionId)
            }

            override fun onClickHotActivatedDiscussion(discussionId: Long) {
                moveToDiscussionDetail(discussionId)
            }
        }
}
