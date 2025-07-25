package com.example.todoktodok.presentation.view.auth.login

import android.content.Context
import android.net.Uri
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.example.todoktodok.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

/**
 * Google 계정 기반 로그인 처리를 담당하는 매니저 클래스.
 *
 * Credential Manager API와 Google ID Token 옵션을 활용해,
 * 사용자 인증 및 Google 계정 연동 로그인을 비동기적으로 처리한다.
 *
 * @property context 애플리케이션 또는 액티비티의 컨텍스트
 */
class GoogleLoginManager(
    val context: Context,
) {
    /**
     * CredentialManager는 Android에서 자격 증명(credential)을 안전하게 관리하고
     * 로그인 흐름을 단일 API로 통합할 수 있도록 지원하는 Jetpack 라이브러리의 핵심 클래스이다.
     */
    private val credentialManager = CredentialManager.create(context)

    /**
     * Google ID 토큰을 요청하기 위한 구성 객체.
     * 이 객체는 로그인할 수 있는 Google 계정 목록을 구성하고,
     * 사용자가 선택한 계정의 ID 토큰을 받아오는 데 사용된다.
     *
     * 구성 옵션:
     * - `setFilterByAuthorizedAccounts(false)`:
     *     사용자가 이전에 로그인한 적이 없는 계정도 로그인 선택지에 포함됨
     * - `setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)`:
     *     Firebase 또는 서버 측에서 검증 가능한 웹 클라이언트 ID 지정
     * - `setAutoSelectEnabled(true)`:
     *     사용 가능한 계정이 하나뿐인 경우 자동으로 선택되도록 설정
     */
    private val googleIdOption: GetGoogleIdOption =
        GetGoogleIdOption
            .Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.GOOGLE_CLIENT_ID)
            .setAutoSelectEnabled(true)
            .build()

    /**
     * Credential 요청 객체.
     * 위에서 설정한 Google ID 옵션을 바탕으로 CredentialManager에 전달할 요청을 구성한다.
     */
    private val credentialRequest: GetCredentialRequest =
        GetCredentialRequest
            .Builder()
            .addCredentialOption(googleIdOption)
            .build()

    /**
     * Google 로그인 흐름을 시작하는 suspend 함수.
     * CredentialManager를 통해 사용자로부터 자격 증명을 받아오고, 성공/실패에 따라 콜백을 실행한다.
     *
     * @param onSuccessLogin 로그인 성공 시 호출되는 콜백. (사용자 ID, 이름, 프로필 이미지)
     * @param onFailLogin 로그인 실패 시 호출되는 콜백. 예외 메시지를 전달하거나 null 전달
     */
    suspend fun startLogin(
        onSuccessLogin: (String, String?, Uri?) -> Unit,
        onFailLogin: (String?) -> Unit,
    ) {
        try {
            val result = credentialManager.getCredential(context, credentialRequest)
            handleSignIn(result, onSuccessLogin, onFailLogin)
        } catch (e: NoCredentialException) {
            e.printStackTrace()
        } catch (e: GetCredentialException) {
            e.printStackTrace()
            onFailLogin(e.toString())
        }
    }

    /**
     * 로그인 결과를 처리하는 함수.
     * Credential이 Google ID Token 타입인지 확인하고,
     * 올바른 형식이면 ID 토큰을 추출해 성공 콜백을 호출한다.
     *
     * @param result CredentialManager 로부터 받아온 자격 증명 응답 객체
     * @param onSuccessLogin 로그인 성공 콜백
     * @param onFailLogin 로그인 실패 콜백
     */
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

    /**
     * Credential 객체가 Google ID Token 타입의 CustomCredential 인지 확인한다.
     *
     * @receiver Credential CredentialManager로부터 반환된 자격 증명 객체
     * @return Google ID Token 형식의 CustomCredential이면 true
     */
    private fun Credential.isCustomAndRightType(): Boolean =
        this is CustomCredential &&
            type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
}
