package com.team.todoktodok.presentation.compose.discussion

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.team.todoktodok.App
import com.team.todoktodok.R
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.UiExceptionHandler
import com.team.todoktodok.presentation.compose.core.ObserveAsEvents
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionTab
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModel
import com.team.todoktodok.presentation.compose.discussion.vm.DiscussionsViewModelFactory
import com.team.todoktodok.presentation.compose.theme.White
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionsScreen(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    timeoutMillis: Long = 1500L,
    viewModel: DiscussionsViewModel =
        viewModel(
            factory = DiscussionsViewModelFactory((LocalContext.current.applicationContext as App).container),
        ),
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var snackbarJob by remember { mutableStateOf<Job?>(null) }

    val showMessage: (String, Long) -> Unit = { message, millis ->
        snackbarJob?.cancel()
        snackbarJob =
            coroutineScope.launch {
                val showJob =
                    launch {
                        snackbarHostState.showSnackbar(
                            message = message,
                            duration = SnackbarDuration.Indefinite,
                        )
                    }
                delay(millis)
                snackbarHostState.currentSnackbarData?.dismiss()
                showJob.cancel()
            }
    }

    var lastBackPressed by rememberSaveable { mutableLongStateOf(0L) }

    BackHandler(enabled = true) {
        val handled =
            viewModel.onBackPressed(timeoutMillis = 1500L, lastBackPressed = lastBackPressed)
        if (!handled) {
            lastBackPressed = System.currentTimeMillis()
            showMessage(context.getString(R.string.press_back_again_to_exit), 1500L)
        }
    }

//    ObserveAsEvents(viewModel.uiEvent) { event ->
//        when (event) {
//            is DiscussionsUiEvent.ShowErrorMessage -> {
//                val message = context.getString(exceptionHandler.messageConverter(event.exception))
//                showMessage(message, timeoutMillis)
//            }
//
//            DiscussionsUiEvent.ScrollToAllDiscussion ->
//                coroutineScope.launch {
//                    pagerState.animateScrollToPage(Destination.ALL.ordinal)
//                }
//        }
//    }

    ObserveAsEvents(viewModel.isRestoring) {
        coroutineScope.launch {
            snackbarHostState.showSnackbar(context.getString(R.string.network_try_connection))
        }
    }

    LaunchedEffect(Unit) {
//        viewModel.loadIsUnreadNotification()
//        viewModel.loadMyDiscussions()
    }

    CompositionLocalProvider(
        LocalUiExceptionHandler provides
            UiExceptionHandler(
                snackbarHostState = snackbarHostState,
                messageConverter = ExceptionMessageConverter(),
            ),
    ) {
        DiscussionsScreen(
            pagerState = pagerState,
            snackbarHostState = snackbarHostState,
            modifier = modifier,
        )
    }
}

@Composable
private fun DiscussionsScreen(
    pagerState: PagerState,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize(),
    ) {
        Column(
            modifier =
                Modifier
                    .background(color = White),
        ) {
            DiscussionTab(pagerState = pagerState)
        }
        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { AlertSnackBar(snackbarData = it) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
