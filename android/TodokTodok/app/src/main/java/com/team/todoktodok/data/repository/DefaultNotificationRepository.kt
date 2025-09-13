package com.team.todoktodok.data.repository

import android.util.Log
import com.team.domain.model.exception.NetworkResult
import com.team.domain.repository.NotificationRepository
import com.team.todoktodok.data.datasource.firebase.FirebaseRemoteDataSource
import com.team.todoktodok.data.datasource.notification.NotificationLocalDataSource
import com.team.todoktodok.data.datasource.notification.NotificationRemoteDataSource

class DefaultNotificationRepository(
    private val notificationRemoteDataSource: NotificationRemoteDataSource,
    private val notificationLocalDataSource: NotificationLocalDataSource,
    private val firebaseRemoteDataSource: FirebaseRemoteDataSource,
) : NotificationRepository {
    override suspend fun registerPushNotification(): NetworkResult<Unit> {
        notificationLocalDataSource.getFcmToken()
        notificationLocalDataSource.getFId()

        val freshFcmToken = firebaseRemoteDataSource.getFcmToken()
        val freshFcmFId = firebaseRemoteDataSource.getFId()

        Log.d("test", "freshFcmToken: $freshFcmToken")
        Log.d("test", "freshFcmToken: $freshFcmFId")
//        val isNeedRegister =
//            isNeedRegister(storedFcmToken, storedFcmFId, freshFcmToken, freshFcmFId)
//
//        if (isNeedRegister) {
//            saveNewPushNotificationToLocal(freshFcmToken, freshFcmFId)
//            return notificationRemoteDataSource.saveFcmToken(freshFcmToken, freshFcmFId)
//        }
        return NetworkResult.Success(Unit)
    }

    override suspend fun deletePushNotification() {
        notificationLocalDataSource.deletePushNotification()
    }

    private suspend fun saveNewPushNotificationToLocal(
        fcmToken: String,
        fId: String,
    ) {
        notificationLocalDataSource.saveFcmToken(fcmToken)
        notificationLocalDataSource.saveFId(fId)
    }

    private fun isNeedRegister(
        storedFcmToken: String?,
        storedFcmFId: String?,
        freshFcmToken: String,
        freshFcmFId: String,
    ): Boolean {
        if (storedFcmToken == null || storedFcmFId == null) return true
        if (storedFcmToken != freshFcmToken || storedFcmFId != freshFcmFId) return true
        return false
    }
}
