package com.team.todoktodok.presentation.xml.discussions.hot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.R
import com.team.todoktodok.databinding.FragmentHotDiscussionBinding
import com.team.todoktodok.presentation.compose.discussion.hot.HotDiscussionScreen
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import com.team.todoktodok.presentation.xml.discussions.BaseDiscussionsFragment

class HotDiscussionFragment : BaseDiscussionsFragment(R.layout.fragment_hot_discussion) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentHotDiscussionBinding.inflate(inflater, container, false)

        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                TodoktodokTheme {
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
                    HotDiscussionScreen(
                        uiState = uiState.value.hotDiscussion,
                        onClick = { discussionId ->
                            moveToDiscussionDetail(discussionId)
                        },
                        onLoadMore = {
                            viewModel.loadActivatedDiscussions()
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
        super.onViewCreated(view, savedInstanceState)
        viewModel.loadHotDiscussions()
    }
}
