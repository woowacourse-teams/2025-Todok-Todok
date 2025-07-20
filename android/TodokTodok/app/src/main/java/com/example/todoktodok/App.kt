package com.example.todoktodok

import android.app.Application

class App : Application() {
    val container: AppContainer by lazy {
        AppContainer()
    }
}
