package com.team.todoktodok.presentation.xml.discussions.latest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentLatestDiscussionsBinding
import com.team.todoktodok.presentation.compose.discussion.latest.LatestDiscussionsScreen
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.xml.discussions.BaseDiscussionsFragment

class LatestDiscussionsFragment : BaseDiscussionsFragment(R.layout.fragment_latest_discussions) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentLatestDiscussionsBinding.inflate(inflater, container, false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            binding.composeView.setContent {
                TodoktodokTheme {
                    val state by viewModel.uiState.collectAsStateWithLifecycle()
                    LatestDiscussionsScreen(
                        onLoadMore = { viewModel.loadLatestDiscussions() },
                        latestDiscussionsUiState = state.latestDiscussion,
                        onClick = { discussionId ->
                            moveToDiscussionDetail(discussionId)
                        },
                    )
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        viewModel.loadLatestDiscussions()
    }

    companion object {
        const val TAG = "LatestDiscussionsFragment"
    }
}
