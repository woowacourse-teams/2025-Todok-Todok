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
import com.team.todoktodok.presentation.compose.discussion.all.AllDiscussionsScreen
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
            setContent {
                TodoktodokTheme {
                    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                    AllDiscussionsScreen(
                        uiState = uiState.allDiscussions,
                        onLoadMore = { viewModel.loadLatestDiscussions() },
                        onClick = { discussionId -> moveToDiscussionDetail(discussionId) },
                        onRefresh = { viewModel.refreshLatestDiscussions() },
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
        viewModel.loadLatestDiscussions()
    }
}
