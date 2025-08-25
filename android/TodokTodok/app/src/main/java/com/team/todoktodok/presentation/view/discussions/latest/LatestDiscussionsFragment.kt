package com.team.todoktodok.presentation.view.discussions.latest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentLatestDiscussionsBinding
import com.team.todoktodok.presentation.core.ext.addOnScrollEndListener
import com.team.todoktodok.presentation.view.discussiondetail.DiscussionDetailActivity
import com.team.todoktodok.presentation.view.discussions.adapter.DiscussionAdapter
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.view.discussions.vm.DiscussionsViewModelFactory

class LatestDiscussionsFragment : Fragment(R.layout.fragment_latest_discussions) {
    private val viewModel: DiscussionsViewModel by activityViewModels {
        val repositoryModule = (requireActivity().application as App).container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
        )
    }

    private val discussionAdapter: DiscussionAdapter by lazy {
        DiscussionAdapter(handler = adapterHandler)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? = super.onCreateView(inflater, container, savedInstanceState)

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        val binding = FragmentLatestDiscussionsBinding.bind(view)
        initView(binding)
        setUpUiState(binding)
        viewModel.loadLatestDiscussions()
    }

    private fun initView(binding: FragmentLatestDiscussionsBinding) {
        with(binding) {
            rvDiscussions.adapter = discussionAdapter
            rvDiscussions.setHasFixedSize(true)
        }
    }

    private fun setUpUiState(binding: FragmentLatestDiscussionsBinding) {
        viewModel.uiState.observe(viewLifecycleOwner) { value ->
            discussionAdapter.submitList(value.latestDiscussion.items)
            binding.rvDiscussions.addOnScrollEndListener {
                viewModel.loadLatestDiscussions()
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
                        discussion.item.id,
                    ),
                )
            }
        }

    companion object {
        const val TAG = "LatestDiscussionsFragment"
    }
}
