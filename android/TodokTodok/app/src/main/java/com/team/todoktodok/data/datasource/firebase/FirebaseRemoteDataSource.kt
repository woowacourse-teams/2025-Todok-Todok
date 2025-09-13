package com.team.todoktodok.data.datasource.firebase

interface FirebaseRemoteDataSource {
    fun getFcmToken(): String

    fun getFId(): String
}
