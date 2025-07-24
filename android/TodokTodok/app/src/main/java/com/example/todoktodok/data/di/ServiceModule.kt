package com.example.todoktodok.data.di

import com.example.todoktodok.data.network.service.BookService
import com.example.todoktodok.data.network.service.LibraryService
import com.example.todoktodok.data.network.service.MemberService
import com.example.todoktodok.data.network.service.NoteService
import kotlin.getValue

class ServiceModule(
    retrofit: RetrofitModule,
) {
    val noteService: NoteService by lazy {
        retrofit.instance.create(NoteService::class.java)
    }

    val memberService: MemberService by lazy {
        retrofit.instance.create(MemberService::class.java)
    }

    val libraryService: LibraryService by lazy {
        retrofit.instance.create(LibraryService::class.java)
    }

    val bookService: BookService by lazy {
        retrofit.instance.create(BookService::class.java)
    }
}
