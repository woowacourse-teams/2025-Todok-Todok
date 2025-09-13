package com.team.todoktodok.data.network.service

import android.annotation.SuppressLint
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {
    private val firebaseInstallations = FirebaseInstallations.getInstance()
    private val firebaseMessaging = FirebaseMessaging.getInstance()

    fun getFcmToken(): String = firebaseMessaging.token.result

    fun getFId(): String = firebaseInstallations.id.result
}
