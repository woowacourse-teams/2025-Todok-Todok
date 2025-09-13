package com.team.todoktodok.presentation.view.auth.login

import android.net.Uri

sealed interface GoogleCredentialResult {
    data class Success(
        val email: String,
        val userName: String?,
        val profileImageUri: Uri?,
    ) : GoogleCredentialResult

    data object Suspending : GoogleCredentialResult

    data class Failure(
        val exception: Throwable?,
    ) : GoogleCredentialResult

    data object Cancel : GoogleCredentialResult
}
