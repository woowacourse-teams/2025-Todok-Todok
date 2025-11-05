package com.team.ui_compose.bookdiscussions

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.team.core.navigation.DiscussionDetailRoute
import com.team.ui_compose.bookdiscussions.components.BookDiscussionsContent
import com.team.ui_compose.bookdiscussions.components.BookDiscussionsTopAppBar
import com.team.ui_compose.bookdiscussions.model.BookDiscussionsUiEvent
import com.team.ui_compose.bookdiscussions.model.BookDiscussionsUiState
import com.team.ui_compose.common.LocalUiExceptionHandler
import com.team.ui_compose.component.AlertSnackBar

@Composable
fun BookDetailEntry(
    backStackEntry: NavBackStackEntry,
    onNavigateToMain: () -> Unit,
    discussionDetailNavigation: DiscussionDetailRoute,
    modifier: Modifier = Modifier,
    viewModel: BookDiscussionsViewModel = hiltViewModel(backStackEntry),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val discussionDetailLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                viewModel.loadBookDiscussions()
            }
        }

    val onDiscussionClick: (Long) -> Unit = { discussionId ->
        discussionDetailNavigation.navigateToDiscussionDetailForResult(
            context as androidx.activity.ComponentActivity,
            discussionId,
            discussionDetailLauncher,
        )
    }

    val exceptionHandler = LocalUiExceptionHandler.current
    LaunchedEffect(viewModel) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is BookDiscussionsUiEvent.ShowError -> {
                    val message =
                        context.getString(exceptionHandler.messageConverter(event.exceptions))
                    exceptionHandler.snackbarHostState.showSnackbar(message)
                }
            }
        }
    }
    BookDetailScreen(
        uiState,
        viewModel::loadMoreItems,
        onDiscussionClick,
        onNavigateToMain,
        modifier,
    )
}

@Composable
fun BookDetailScreen(
    uiState: BookDiscussionsUiState,
    loadMoreItems: () -> Unit,
    onDiscussionClick: (Long) -> Unit,
    onNavigateToMain: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier,
        snackbarHost = {
            SnackbarHost(
                hostState = LocalUiExceptionHandler.current.snackbarHostState,
                snackbar = { AlertSnackBar(snackbarData = it) },
            )
        },
        topBar = {
            BookDiscussionsTopAppBar(
                onNavigateToMain,
                Modifier.padding(horizontal = 24.dp),
            )
        },
    ) { innerPadding ->
        BookDiscussionsContent(
            uiState.bookDetailSectionUiState,
            uiState.bookDiscussionsSectionUiState,
            loadMoreItems,
            onDiscussionClick,
            Modifier.padding(innerPadding),
        )
    }
}
