package com.team.todoktodok.data.di

import com.team.todoktodok.data.network.service.BookService
import com.team.todoktodok.data.network.service.CommentService
import com.team.todoktodok.data.network.service.DiscussionService
import com.team.todoktodok.data.network.service.MemberService
import com.team.todoktodok.data.network.service.ReplyService

class ServiceModule(
    retrofit: RetrofitModule,
) {
    val discussionService: DiscussionService by lazy {
        retrofit.createService(DiscussionService::class.java)
    }

    val memberService: MemberService by lazy {
        retrofit.createService(MemberService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit.createService(CommentService::class.java)
    }

    val bookService: BookService by lazy {
        retrofit.createService(BookService::class.java)
    }

    val replyService: ReplyService by lazy {
        retrofit.createService(ReplyService::class.java)
    }
}
