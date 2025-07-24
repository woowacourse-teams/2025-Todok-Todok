package com.example.todoktodok.data.di

import com.example.todoktodok.data.network.service.MemberService
import com.example.todoktodok.data.network.service.NoteService
import kotlin.getValue
import kotlin.jvm.java

class ServiceModule(
    retrofit: RetrofitModule,
) {
    val noteService: NoteService by lazy {
        retrofit.instance.create(NoteService::class.java)
    }

    val memberService: MemberService by lazy {
        retrofit.instance.create(MemberService::class.java)
    }
}
