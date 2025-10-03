package com.team.todoktodok.presentation.compose

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import com.team.todoktodok.presentation.core.ExceptionMessageConverter

data class UiExceptionHandler(
    val snackbarHostState: SnackbarHostState,
    val messageConverter: ExceptionMessageConverter,
)

val LocalUiExceptionHandler =
    staticCompositionLocalOf<UiExceptionHandler> {
        error("No UiExceptionHandler provided")
    }
