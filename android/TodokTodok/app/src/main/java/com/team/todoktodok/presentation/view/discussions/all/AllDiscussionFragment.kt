package com.team.todoktodok.presentation.view.discussions.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentAllDiscussionBinding
import com.team.todoktodok.presentation.core.ext.addOnScrollEndListener
import com.team.todoktodok.presentation.core.ext.clearOnScrollEndListener
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.DiscussionsUiEvent
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory

class AllDiscussionFragment : Fragment(R.layout.fragment_all_discussion) {
    private var _binding: FragmentAllDiscussionBinding? = null
    val binding get() = _binding!!
    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(handler = adapterHandler)
    }
    private val viewModel: DiscussionsViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentAllDiscussionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        initView(binding)
        setUpUiState(binding)
        setUpUiEvent(binding)
    }

    private fun initView(binding: FragmentAllDiscussionBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.setHasFixedSize(true)
        }
    }

    private fun setUpUiState(binding: FragmentAllDiscussionBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.latestDiscussions.toList())

            if (value.loadBySearch) {
                binding.rvDiscussions.clearOnScrollEndListener()
            } else {
                binding.rvDiscussions.addOnScrollEndListener {
                    viewModel.loadLatestDiscussions(false)
                }
            }
        }
    }

    private fun setUpUiEvent(binding: FragmentAllDiscussionBinding) =
        with(binding) {
            viewModel.uiEvent.observe(viewLifecycleOwner) { event ->
                when (event) {
                    DiscussionsUiEvent.ShowNotHasAllDiscussions -> {
                        displayNoResultsView(binding)
                    }

                    DiscussionsUiEvent.ShowHasAllDiscussions -> {
                        displayResultsView(binding)
                    }

                    else -> Unit
                }
            }
        }

    private fun displayResultsView(binding: FragmentAllDiscussionBinding) {
        binding.tvNoResult.visibility = View.GONE
        binding.rvDiscussions.visibility = View.VISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadLatestDiscussions(true)
        binding.rvDiscussions.scrollToPosition(0)
    }

    private fun displayNoResultsView(binding: FragmentAllDiscussionBinding) {
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
