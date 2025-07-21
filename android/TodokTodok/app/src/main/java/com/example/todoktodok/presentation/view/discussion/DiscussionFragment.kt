package com.example.todoktodok.presentation.view.discussion

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.todoktodok.App
import com.example.todoktodok.R
import com.example.todoktodok.databinding.FragmentDiscussionBinding
import com.example.todoktodok.presentation.view.discussion.adapter.DiscussionRoomAdapter
import com.example.todoktodok.presentation.view.discussion.vm.DiscussionViewModel
import com.example.todoktodok.presentation.view.discussion.vm.DiscussionViewModelFactory

class DiscussionFragment : Fragment(R.layout.fragment_discussion) {
    private val viewModel: DiscussionViewModel by viewModels {
        val container = (requireActivity().application as App).container
        DiscussionViewModelFactory(container.repositoryModule.discussionRoomRepository)
    }
    private val adapter by lazy {
        DiscussionRoomAdapter { id ->
            viewModel.onUiEvent(DiscussionUiEvent.NavigateDiscussionRoom(id))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
            viewModel.onUiEvent(DiscussionUiEvent.NavigateAddDiscussionRoom)
        }
    }

    private fun handleEvent(discussionUiEvent: DiscussionUiEvent) {
        when (discussionUiEvent) {
            DiscussionUiEvent.NavigateAddDiscussionRoom -> Toast.makeText(
                requireContext(),
                "토론방 추가",
                Toast.LENGTH_SHORT
            ).show()

            is DiscussionUiEvent.NavigateDiscussionRoom -> Toast.makeText(
                requireContext(),
                "토론방 이동",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
