package com.team.todoktodok.data.network.service

import android.annotation.SuppressLint
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {
    private val firebaseInstallations = FirebaseInstallations.getInstance()
    private val firebaseMessaging = FirebaseMessaging.getInstance()

    suspend fun getFcmToken(): String = firebaseMessaging.token.await()

    fun getFId(): String = firebaseInstallations.id.result
}
