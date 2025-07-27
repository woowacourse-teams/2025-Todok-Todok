package com.team.todoktodok.data.di

import com.team.todoktodok.data.network.service.BookService
import com.team.todoktodok.data.network.service.CommentService
import com.team.todoktodok.data.network.service.DiscussionService
import com.team.todoktodok.data.network.service.LibraryService
import com.team.todoktodok.data.network.service.MemberService
import com.team.todoktodok.data.network.service.NoteService

class ServiceModule(
    retrofit: RetrofitModule,
) {
    val noteService: NoteService by lazy {
        retrofit.instance.create(NoteService::class.java)
    }
    val discussionService: DiscussionService by lazy {
        retrofit.instance.create(DiscussionService::class.java)
    }

    val memberService: MemberService by lazy {
        retrofit.instance.create(MemberService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit.instance.create(CommentService::class.java)
    }

    val libraryService: LibraryService by lazy {
        retrofit.instance.create(LibraryService::class.java)
    }

    val bookService: BookService by lazy {
        retrofit.instance.create(BookService::class.java)
    }
}
