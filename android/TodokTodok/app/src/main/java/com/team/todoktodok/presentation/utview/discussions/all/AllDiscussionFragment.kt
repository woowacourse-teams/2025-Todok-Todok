package com.team.todoktodok.presentation.utview.discussions.all

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentAllDiscussionBinding
import com.team.todoktodok.presentation.utview.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.utview.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.utview.discussions.all.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.utview.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.utview.discussions.vm.DiscussionsViewModelFactory
import kotlin.getValue

class AllDiscussionFragment : Fragment(R.layout.fragment_all_discussion) {
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
        val binding = FragmentAllDiscussionBinding.bind(view)

        initView(binding)
        setUpUiState()
        setUpUiEvent(binding)
    }

    private fun initView(binding: FragmentAllDiscussionBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.hasFixedSize()
        }
    }

    private fun setUpUiState() {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.allDiscussions)
        }
    }

    private fun setUpUiEvent(binding: FragmentAllDiscussionBinding) =
        with(binding) {
            viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    DiscussionsUiEvent.ShowNotHasAllDiscussions -> {
                        tvNoResult.visibility = View.VISIBLE
                        binding.rvDiscussions.visibility = View.GONE
                    }

                    DiscussionsUiEvent.ShowHasAllDiscussions -> {
                        tvNoResult.visibility = View.GONE
                        binding.rvDiscussions.visibility = View.VISIBLE
                    }

                    else -> Unit
                }
            }
        }

    private val adapterHandler =
        object : DiscussionAdapter.Handler {
            override fun onItemClick(index: Int) {
                val discussion = discussionAdapter.currentList[index]
                startActivity(
                    DiscussionDetailActivity.Intent(
                        requireContext(),
                        discussion.id,
                    ),
                )
            }
        }
}
