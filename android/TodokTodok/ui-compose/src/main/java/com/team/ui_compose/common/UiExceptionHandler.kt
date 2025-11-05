package com.team.ui_compose.common

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import com.team.core.ExceptionMessageConverter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

data class UiExceptionHandler(
    val snackbarHostState: SnackbarHostState,
    val messageConverter: ExceptionMessageConverter,
) {
    private var currentJob: Job? = null

    fun showErrorMessage(
        scope: CoroutineScope,
        message: String,
    ) {
        if (currentJob?.isActive == true) return

        currentJob =
            scope.launch {
                try {
                    snackbarHostState.showSnackbar(message)
                } finally {
                    currentJob = null
                }
            }
    }
}

val LocalUiExceptionHandler =
    staticCompositionLocalOf<UiExceptionHandler> {
        error("No UiExceptionHandler provided")
    }
