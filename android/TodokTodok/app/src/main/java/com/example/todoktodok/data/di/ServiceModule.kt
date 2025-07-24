package com.example.todoktodok.data.di

import com.example.todoktodok.data.network.service.DiscussionService
import com.example.todoktodok.data.network.service.MemberService
import com.example.todoktodok.data.network.service.NoteService

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
}
