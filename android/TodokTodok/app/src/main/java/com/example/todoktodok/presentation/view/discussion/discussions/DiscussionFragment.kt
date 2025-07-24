package com.example.todoktodok.presentation.view.discussion.discussions

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentDiscussionBinding
import com.example.todoktodok.presentation.view.discussion.create.DiscussionRoomCreateActivity
import com.example.todoktodok.presentation.view.discussion.detail.DiscussionRoomDetailActivity
import com.example.todoktodok.presentation.view.discussion.discussions.adapter.DiscussionRoomAdapter
import com.example.todoktodok.presentation.view.discussion.discussions.vm.DiscussionViewModel
import com.example.todoktodok.presentation.view.discussion.discussions.vm.DiscussionViewModelFactory

class DiscussionFragment : Fragment(R.layout.fragment_discussion) {
    private val viewModel: DiscussionViewModel by viewModels {
        val container = (requireActivity().application as App).container
        DiscussionViewModelFactory(container.repositoryModule.discussionRoomRepository)
    }
    private val adapter by lazy {
        DiscussionRoomAdapter { id ->
            viewModel.onUiEvent(DiscussionUiEvent.NavigateToDiscussionRoomDetail(id))
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDiscussionBinding.bind(view)
        binding.rvDiscussionRooms.adapter = adapter
        setupObservers()
        setupOnClick(binding)
    }

    private fun setupObservers() {
        viewModel.discussionRooms.observe(viewLifecycleOwner) { discussionRooms ->
            adapter.submitList(discussionRooms)
        }
        viewModel.uiEvent.observe(viewLifecycleOwner) { discussionUiEvent ->
            handleEvent(discussionUiEvent)
        }
    }

    private fun setupOnClick(binding: FragmentDiscussionBinding) {
        binding.ivAddDiscussionRoom.setOnClickListener {
            viewModel.onUiEvent(DiscussionUiEvent.NavigateToAddDiscussionRoom)
        }
    }

    private fun handleEvent(discussionUiEvent: DiscussionUiEvent) {
        when (discussionUiEvent) {
            DiscussionUiEvent.NavigateToAddDiscussionRoom ->
                {
                    val intent = DiscussionRoomCreateActivity.Intent(requireActivity())
                    startActivity(intent)
                }

            is DiscussionUiEvent.NavigateToDiscussionRoomDetail ->
                navigateToDiscussionRoomDetail(discussionUiEvent.discussionRoomId)
        }
    }

    private fun navigateToDiscussionRoomDetail(discussionRoomId: Long) {
        val intent = DiscussionRoomDetailActivity.Intent(requireContext(), discussionRoomId)
        startActivity(intent)
    }
}
