package com.team.todoktodok.data

import android.annotation.SuppressLint
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.tasks.await

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class FirebaseService : FirebaseMessagingService() {
    private val firebaseInstallations = FirebaseInstallations.getInstance()
    private val firebaseMessaging = FirebaseMessaging.getInstance()

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            val message = message.data
            message["discussionId"]
            message["commentId"]
            message["replyId"]
            message["memberNickname"]
            message["discussionTitle"]
            message["content"]
            message["type"]
            message["target"]
        }
    }

    suspend fun getFcmToken(): String = firebaseMessaging.token.await()

    fun getFId(): String = firebaseInstallations.id.result
}
