package com.example.todoktodok.presentation.view.discussion.discussions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentDiscussionBinding
import com.example.todoktodok.presentation.view.discussion.create.DiscussionCreateActivity
import com.example.todoktodok.presentation.view.discussion.detail.DiscussionDetailActivity
import com.example.todoktodok.presentation.view.discussion.discussions.adapter.DiscussionAdapter
import com.example.todoktodok.presentation.view.discussion.discussions.vm.DiscussionViewModel
import com.example.todoktodok.presentation.view.discussion.discussions.vm.DiscussionViewModelFactory

class DiscussionFragment : Fragment(R.layout.fragment_discussion) {
    private val viewModel: DiscussionViewModel by viewModels {
        val container = (requireActivity().application as App).container
        DiscussionViewModelFactory(container.repositoryModule.discussionRepository)
    }
    private val adapter by lazy {
        DiscussionAdapter { id ->
            viewModel.onUiEvent(DiscussionUiEvent.NavigateToDiscussionDetail(id))
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDiscussionBinding.bind(view)
        binding.rvDiscussions.adapter = adapter
        setupObservers()
        setupOnClick(binding)
    }

    private fun setupObservers() {
        viewModel.discussions.observe(viewLifecycleOwner) { discussions ->
            adapter.submitList(discussions)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { discussionUiEvent ->
            handleEvent(discussionUiEvent)
        }
    }

    private fun setupOnClick(binding: FragmentDiscussionBinding) {
        binding.ivAddDiscussion.setOnClickListener {
            viewModel.onUiEvent(DiscussionUiEvent.NavigateToAddDiscussion)
        }
    }

    private fun handleEvent(discussionUiEvent: DiscussionUiEvent) {
        when (discussionUiEvent) {
            DiscussionUiEvent.NavigateToAddDiscussion -> {
                val intent = DiscussionCreateActivity.Intent(requireActivity())
                startActivity(intent)
            }

            is DiscussionUiEvent.NavigateToDiscussionDetail ->
                navigateToDiscussionDetail(discussionUiEvent.discussionId)
        }
    }

    private fun navigateToDiscussionDetail(discussionId: Long) {
        val intent = DiscussionDetailActivity.Intent(requireContext(), discussionId)
        startActivity(intent)
    }
}
