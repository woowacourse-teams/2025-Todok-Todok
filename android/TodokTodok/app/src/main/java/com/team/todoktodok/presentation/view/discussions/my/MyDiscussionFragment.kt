package com.team.todoktodok.presentation.view.discussions.my

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentMyDiscussionBinding
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory

class MyDiscussionFragment : Fragment(R.layout.fragment_my_discussion) {
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(handler = adapterHandler)
    }
    private val viewModel: DiscussionsViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        DiscussionsViewModelFactory(repositoryModule.discussionRepository)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentMyDiscussionBinding.bind(view)

        initView(binding)
        setUpUiEvent(binding)
        setUpUiState()
    }

    private fun initView(binding: FragmentMyDiscussionBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.hasFixedSize()
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.myDiscussions)
        }
    }

    private fun setUpUiEvent(binding: FragmentMyDiscussionBinding) =
        with(binding) {
            viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    DiscussionsUiEvent.ShowNotHasMyDiscussions -> {
                        displayNoResultsView(binding)
                    }

                    DiscussionsUiEvent.ShowHasMyDiscussions -> {
                        displayResultsView(binding)
                    }

                    else -> Unit
                }
            }
        }

    private fun displayResultsView(binding: FragmentMyDiscussionBinding) {
        binding.tvNoResult.visibility = View.GONE
        binding.rvDiscussions.visibility = View.VISIBLE
    }

    private fun displayNoResultsView(binding: FragmentMyDiscussionBinding) {
        binding.tvNoResult.visibility = View.VISIBLE
        binding.rvDiscussions.visibility = View.GONE
    }

    private val adapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                val discussion = discussionAdapter.currentList[index]
                startActivity(
                    DiscussionDetailActivity.Intent(
                        requireContext(),
                        discussion.item.id,
                    ),
                )
            }
        }
}
