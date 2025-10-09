package com.team.todoktodok.presentation.compose.bookdiscussions

import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import com.team.todoktodok.App
import com.team.todoktodok.presentation.compose.bookdiscussions.components.BookDiscussionsContent
import com.team.todoktodok.presentation.compose.bookdiscussions.components.BookDiscussionsTopAppBar
import com.team.todoktodok.presentation.compose.bookdiscussions.model.BookDiscussionsUiState
import com.team.todoktodok.presentation.core.component.CloverProgressBar

@Composable
fun BookDetailEntry(
    backStackEntry: NavBackStackEntry,
    onNavigateToMain: () -> Unit,
    onNavigateToMyProfile: () -> Unit,
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
        onNavigateToMain,
        onNavigateToMyProfile,
        modifier,
    )
}

@Composable
fun BookDetailScreen(
    uiState: BookDiscussionsUiState,
    onNavigateToMain: () -> Unit,
    onNavigateToMyProfile: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(modifier, topBar = {
        BookDiscussionsTopAppBar(
            onNavigateToMain,
            onNavigateToMyProfile,
            Modifier.padding(horizontal = 24.dp),
        )
    }) { innerPadding ->
        when (uiState) {
            BookDiscussionsUiState.Loading -> CloverProgressBar(LocalContext.current)
            is BookDiscussionsUiState.Success ->
                BookDiscussionsContent(
                    uiState.book,
                    uiState.discussions,
                    Modifier.padding(innerPadding),
                )

            is BookDiscussionsUiState.Failure -> (LocalActivity.current as ComponentActivity).onBackPressedDispatcher.onBackPressed()
        }
    }
}
