package com.team.todoktodok.data.datasource.firebase

import com.team.todoktodok.data.network.service.FirebaseService

class DefaultFirebaseRemoteDataSource(
    private val firebaseService: FirebaseService,
) : FirebaseRemoteDataSource {
    override suspend fun getFcmToken(): String = firebaseService.getFcmToken()

    override fun getFId(): String = firebaseService.getFId()
}
