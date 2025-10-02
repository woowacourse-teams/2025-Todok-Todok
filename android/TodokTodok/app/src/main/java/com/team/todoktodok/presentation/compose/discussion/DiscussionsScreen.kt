package com.team.todoktodok.presentation.compose.discussion

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.team.todoktodok.presentation.compose.LocalUiExceptionHandler
import com.team.todoktodok.presentation.compose.UiExceptionHandler
import com.team.todoktodok.presentation.compose.core.component.AlertSnackBar
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionTab
import com.team.todoktodok.presentation.core.ExceptionMessageConverter
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscussionsScreen(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
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
        DiscussionTab(pagerState = pagerState)

        SnackbarHost(
            hostState = snackbarHostState,
            snackbar = { AlertSnackBar(snackbarData = it) },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}
