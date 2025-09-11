package com.team.todoktodok.data.network.auth

import com.team.todoktodok.data.core.AuthorizationConstants
import com.team.todoktodok.data.core.ext.retryAttemptCount
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.atomic.AtomicReference

class TokenAuthenticator(
    private val tokenRefreshDelegate: () -> TokenRefreshDelegate,
    private val scope: CoroutineScope,
) : Authenticator {
    private val mutex = Mutex()
    private val ongoingRefresh = AtomicReference<Deferred<String?>>()

    override fun authenticate(
        route: Route?,
        response: Response,
    ): Request? {
        if (response.retryAttemptCount() >= MAX_ATTEMPT_COUNT) return null

        return runBlocking {
            val newAccessToken = getOrCreateRefreshTask().await()
            buildRequestWithToken(response.request, newAccessToken)
        }
    }

    private suspend fun getOrCreateRefreshTask(): Deferred<String?> {
        ongoingRefresh.get()?.let { return it }

        return mutex.withLock {
            ongoingRefresh.get() ?: createRefreshTask()
        }
    }

    private fun createRefreshTask(): Deferred<String?> {
        val deferred = CompletableDeferred<String?>()
        ongoingRefresh.set(deferred)

        scope.launch {
            val result = executeTokenRefresh()
            deferred.complete(result)
            ongoingRefresh.compareAndSet(deferred, null)
        }

        return deferred
    }

    private suspend fun executeTokenRefresh(): String? =
        withContext(Dispatchers.IO) {
            runCatching { tokenRefreshDelegate().refresh() }.getOrNull()
        }

    private fun buildRequestWithToken(
        originalRequest: Request,
        newAccessToken: String?,
    ): Request? {
        if (newAccessToken.isNullOrBlank()) return null
        return originalRequest
            .newBuilder()
            .header(
                AuthorizationConstants.HEADER_AUTHORIZATION,
                AuthorizationConstants.HEADER_AUTHORIZATION_TYPE.format(newAccessToken),
            ).build()
    }

    companion object {
        private const val MAX_ATTEMPT_COUNT = 3
    }
}
