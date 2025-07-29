package com.team.todoktodok.presentation.utview.discussions.all

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentAllDiscussionBinding
import com.team.todoktodok.presentation.utview.discussions.all.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.utview.discussions.all.vm.AllDiscussionViewModel
import com.team.todoktodok.presentation.utview.discussions.all.vm.AllDiscussionViewModelFactory
import kotlin.getValue

class AllDiscussionFragment : Fragment(R.layout.fragment_all_discussion) {
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(handler = adapterHandler)
    }
    private val viewModel: AllDiscussionViewModel by viewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule

        AllDiscussionViewModelFactory(repositoryModule.discussionRepository)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentAllDiscussionBinding.bind(view)

        initView(binding)
        setUpUiState()
        setUpUiEvent()
    }

    private fun initView(binding: FragmentAllDiscussionBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.hasFixedSize()
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value)
        }
    }

    private fun setUpUiEvent() {
        viewModel.uiEvent.observe(viewLifecycleOwner) { value ->
            when (value) {
                is AllDiscussionUiEvent.NavigateToDetail -> {
                    // TODO: 상세 페이지로 이동
                }
            }
        }
    }

    private val adapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                viewModel.findDiscussion(index)
            }
        }
}
