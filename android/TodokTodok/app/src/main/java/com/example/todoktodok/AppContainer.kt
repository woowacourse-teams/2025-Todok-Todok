package com.example.todoktodok

import com.example.todoktodok.data.di.DataSourceModule
import com.example.todoktodok.data.di.RepositoryModule

class AppContainer {
    val dataSourceModule: DataSourceModule by lazy {
        DataSourceModule()
    }
    val repositoryModule: RepositoryModule by lazy {
        RepositoryModule(dataSourceModule)
    }
}
