package com.team.todoktodok.presentation.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val connectivityObserver: ConnectivityObserver,
) : ViewModel() {
    private val pendingActions = mutableListOf<suspend () -> Unit>()

    private val _baseUiState = MutableLiveData(BaseUiState())
    val baseUiState: LiveData<BaseUiState> get() = _baseUiState

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
                    _baseUiState.value = _baseUiState.value?.copy(isRestoring = true)
                    val actionsToRetry = pendingActions.toList()
                    pendingActions.clear()
                    actionsToRetry.forEach { action ->
                        launch(recordExceptionHandler) { action() }
                    }
                    _baseUiState.value = _baseUiState.value?.copy(isRestoring = false)
                }
            }
        }
    }

    protected fun <T> runAsync(
        action: suspend () -> NetworkResult<T>,
        handleSuccess: (T) -> Unit,
        handleFailure: (TodokTodokExceptions) -> Unit,
    ) {
        viewModelScope.launch(recordExceptionHandler) {
            if (connectivityObserver.value() != ConnectivityObserver.Status.Available) {
                pendingActions.add { runAsync(action, handleSuccess, handleFailure) }
                handleFailure(TodokTodokExceptions.UnknownHostError)
                return@launch
            }

            _baseUiState.value = _baseUiState.value?.copy(isLoading = true)

            action()
                .onSuccess { handleSuccess(it) }
                .onFailure { handleFailure(it) }
            _baseUiState.value = _baseUiState.value?.copy(isLoading = false)
        }
    }

    protected fun <T> runAsyncWithResult(action: suspend () -> NetworkResult<T>): Deferred<NetworkResult<T>> {
        val deferred = CompletableDeferred<NetworkResult<T>>()

        viewModelScope.launch(recordExceptionHandler) {
            val job =
                suspend {
                    _baseUiState.value = _baseUiState.value?.copy(isLoading = true)
                    deferred.complete(action())
                    _baseUiState.value = _baseUiState.value?.copy(isLoading = false)
                }

            if (connectivityObserver.value() != ConnectivityObserver.Status.Available) {
                pendingActions.add(job)
            } else {
                job()
            }
        }

        return deferred
    }
}
