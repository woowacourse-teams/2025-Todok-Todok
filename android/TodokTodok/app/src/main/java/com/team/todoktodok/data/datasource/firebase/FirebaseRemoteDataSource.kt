package com.team.todoktodok.data.datasource.firebase

interface FirebaseRemoteDataSource {
    suspend fun getFcmToken(): String

    fun getFId(): String
}
