package com.example.todoktodok.presentation.view.discussion

import android.os.Bundle
import android.view.View
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentDiscussionBinding.bind(view)
        val adapter = DiscussionRoomAdapter()
        binding.rvDiscussionRooms.adapter = adapter
        viewModel.discussionRooms.observe(viewLifecycleOwner) { discussionRooms ->
            adapter.submitList(discussionRooms)
        }
    }
}
