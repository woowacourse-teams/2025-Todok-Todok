package com.team.todoktodok.presentation.compose.discussion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.discussion.latest.DiscussionsScreen
import com.team.todoktodok.presentation.compose.discussion.latest.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.discussion.latest.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.compose.theme.TodoktodokTheme
import kotlin.getValue

class DiscussionActivity : ComponentActivity() {
    private val viewModel: DiscussionsViewModel by viewModels {
        val container = (application as App).container
        val repositoryModule = container.repositoryModule
        DiscussionsViewModelFactory(
            repositoryModule.discussionRepository,
            repositoryModule.memberRepository,
            container.connectivityObserver,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoktodokTheme {
                DiscussionsScreen(viewModel)
            }
        }
    }
}
