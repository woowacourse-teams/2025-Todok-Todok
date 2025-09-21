package com.team.todoktodok.presentation.compose.discussion.latest

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.team.todoktodok.presentation.compose.discussion.latest.vm.DiscussionsViewModel

@Composable
fun DiscussionsScreen(viewModel: DiscussionsViewModel) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
}
