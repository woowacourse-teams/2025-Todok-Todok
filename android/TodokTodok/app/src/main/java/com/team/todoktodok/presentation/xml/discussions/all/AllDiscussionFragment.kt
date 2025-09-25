package com.team.todoktodok.presentation.xml.discussions.all

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentAllDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.search.SearchDiscussionScreen
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.xml.discussions.BaseDiscussionsFragment

class AllDiscussionFragment : BaseDiscussionsFragment(R.layout.fragment_all_discussion) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentAllDiscussionBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            binding.composeView.setContent {
                TodoktodokTheme {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
                    val allDiscussion = uiState.allDiscussionsUiState
                    when (allDiscussion.mode) {
                        AllDiscussionMode.LATEST -> {
                            LatestDiscussionsScreen(
                                onLoadMore = { viewModel.loadLatestDiscussions() },
                                latestDiscussionsUiState = allDiscussion.latestDiscussion,
                                isRefreshing = allDiscussion.latestDiscussion.isRefreshing,
                                onRefresh = { viewModel.refreshLatestDiscussions() },
                                onClick = { discussionId ->
                                    moveToDiscussionDetail(discussionId)
                                },
                            )
                        }

                        AllDiscussionMode.SEARCH -> {
                            SearchDiscussionScreen(
                                searchDiscussion = allDiscussion.searchDiscussion,
                                onClick = { discussionId ->
                                    moveToDiscussionDetail(discussionId)
                                },
                            )
                        }
                    }
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadLatestDiscussions()
    }
}
