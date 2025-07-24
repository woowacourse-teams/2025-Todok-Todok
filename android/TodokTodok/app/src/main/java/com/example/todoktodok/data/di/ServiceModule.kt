package com.example.todoktodok.data.di

import com.example.todoktodok.data.network.service.LibraryService
import com.example.todoktodok.data.network.service.NoteService
import kotlin.getValue

class ServiceModule(
    retrofit: RetrofitModule,
) {
    val noteService: NoteService by lazy {
        retrofit.instance.create(NoteService::class.java)
    }

    val libraryService: LibraryService by lazy {
        retrofit.instance.create(LibraryService::class.java)
    }
}
