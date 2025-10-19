package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.bookdiscussions.components.BookDiscussionsContent
import com.team.todoktodok.presentation.compose.bookdiscussions.components.BookDiscussionsTopAppBar
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState

@Composable
fun BookDetailEntry(
    backStackEntry: NavBackStackEntry,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BookDiscussionsViewModel =
        viewModel(
            backStackEntry,
            factory = BookDiscussionsViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BookDetailScreen(
        uiState,
        viewModel::loadMoreItems,
        onNavigateToMain,
        modifier,
    )
}

@Composable
fun BookDetailScreen(
    uiState: BookDiscussionsUiState,
    loadMoreItems: () -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier, topBar = {
        BookDiscussionsTopAppBar(
            onNavigateToMain,
            Modifier.padding(horizontal = 24.dp),
        )
    }) { innerPadding ->
        BookDiscussionsContent(
            uiState.bookDetailSectionUiState,
            uiState.bookDiscussionsSectionUiState,
            loadMoreItems,
            Modifier.padding(innerPadding),
        )
    }
}
