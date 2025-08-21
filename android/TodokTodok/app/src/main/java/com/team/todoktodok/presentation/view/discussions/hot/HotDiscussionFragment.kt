package com.team.todoktodok.presentation.view.discussions.hot

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentHotDiscussionBinding
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.hot.adapter.HotDiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory
import kotlin.getValue

class HotDiscussionFragment : Fragment(R.layout.fragment_hot_discussion) {
    private val viewModel: DiscussionsViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
        )
    }

    private lateinit var hotDiscussionAdapter: HotDiscussionAdapter

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHotDiscussionBinding.bind(view)
        initView(binding)
        setupUiState()
    }

    private fun initView(binding: FragmentHotDiscussionBinding) {
        hotDiscussionAdapter = HotDiscussionAdapter(discussionAdapterHandler)
        binding.rvDiscussions.adapter = hotDiscussionAdapter
    }

    private fun setupUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            hotDiscussionAdapter.submitList(value.hotDiscussionItems)
        }
    }

    private val discussionAdapterHandler =
        object : HotDiscussionAdapter.Handler {
            override fun onClickHotPopularDiscussion(discussionId: Long) {
                startActivity(DiscussionDetailActivity.Intent(requireContext(), discussionId))
            }

            override fun onClickHotActivatedDiscussion(discussionId: Long) {
                startActivity(DiscussionDetailActivity.Intent(requireContext(), discussionId))
            }
        }

    override fun onResume() {
        super.onResume()
        viewModel.loadHotDiscussions()
    }
}
