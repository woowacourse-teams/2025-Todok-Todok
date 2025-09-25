package com.team.todoktodok.presentation.core.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.team.domain.ConnectivityObserver
import com.team.domain.model.exception.NetworkResult
import com.team.domain.model.exception.TodokTodokExceptions
import com.team.domain.model.exception.onFailure
import com.team.domain.model.exception.onSuccess
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap

abstract class BaseViewModel(
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {
    private val pendingActions = ConcurrentHashMap<String, suspend () -> Unit>()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading.asStateFlow()

    private val _isRestoring = Channel<Unit>(Channel.BUFFERED)
    val isRestoring get() = _isRestoring.receiveAsFlow()

    protected val recordExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            viewModelScope.launch(Dispatchers.IO) {
                FirebaseCrashlytics.getInstance().recordException(throwable)
            }
        }

    init {
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            connectivityObserver.subscribe().collect { status ->
                if (status == ConnectivityObserver.Status.Available && pendingActions.isNotEmpty()) {
                    _isRestoring.send(Unit)

                    val actionsToRetry = pendingActions.values.toList()
                    pendingActions.clear()
                    actionsToRetry.forEach { action ->
                        launch(recordExceptionHandler) { action() }
                    }
                }
            }
        }
    }

    protected fun <T> runAsync(
        key: String,
        action: suspend () -> NetworkResult<T>,
        handleSuccess: (T) -> Unit,
        handleFailure: (TodokTodokExceptions) -> Unit,
    ) {
        val job: suspend () -> Unit = {
            _isLoading.update { true }
            action()
                .onSuccess {
                    pendingActions.remove(key)
                    handleSuccess(it)
                }.onFailure { handleFailure(it) }
            _isLoading.update { false }
        }

        if (connectivityObserver.value() != ConnectivityObserver.Status.Available) {
            pendingActions[key] = job
            handleFailure(TodokTodokExceptions.UnknownHostError)
            return
        }

        viewModelScope.launch(recordExceptionHandler) { job() }
    }

    protected fun <T> runAsyncWithResult(
        key: String,
        action: suspend () -> NetworkResult<T>,
    ): Deferred<NetworkResult<T>> {
        val deferred = CompletableDeferred<NetworkResult<T>>()

        val job: suspend () -> Unit = {
            _isLoading.update { true }
            val result = action()
            deferred.complete(result)
            if (result is NetworkResult.Success) {
                pendingActions.remove(key)
            }
            _isLoading.update { false }
        }

        if (connectivityObserver.value() != ConnectivityObserver.Status.Available) {
            pendingActions[key] = job
        } else {
            viewModelScope.launch(recordExceptionHandler) { job() }
        }

        return deferred
    }
}
