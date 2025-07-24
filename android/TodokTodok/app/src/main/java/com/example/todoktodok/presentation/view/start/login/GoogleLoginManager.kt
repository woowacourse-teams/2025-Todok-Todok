package com.example.todoktodok.presentation.view.start.login

import android.content.Context
import android.net.Uri
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import com.example.todoktodok.BuildConfig
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleLoginManager(
    val context: Context,
) {
    private val credentialManager = CredentialManager.create(context)

    private val googleIdOption: GetSignInWithGoogleOption =
        GetSignInWithGoogleOption
            .Builder(BuildConfig.GOOGLE_CLIENT_ID)
            .build()

    private val credentialRequest = GetCredentialRequest(listOf(googleIdOption))

    suspend fun startLogin(
        onSuccessLogin: (String, String?, Uri?) -> Unit,
        onFailLogin: (String?) -> Unit,
    ) {
        runCatching {
            val result = credentialManager.getCredential(context, credentialRequest)
            handleSignIn(result, onSuccessLogin, onFailLogin)
        }
    }

    private fun handleSignIn(
        result: GetCredentialResponse,
        onSuccessLogin: (String, String?, Uri?) -> Unit,
        onFailLogin: (String?) -> Unit,
    ) {
        val credential = result.credential
        if (credential.isCustomAndRightType()) {
            runCatching {
                GoogleIdTokenCredential.createFrom(credential.data)
            }.onSuccess {
                onSuccessLogin(it.id, it.displayName, it.profilePictureUri)
            }.onFailure {
                onFailLogin(it.toString())
            }
        } else {
            onFailLogin(null)
        }
    }

    private fun Credential.isCustomAndRightType() =
        this is CustomCredential && type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
}
