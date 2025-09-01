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
        retrofit.instance.create(DiscussionService::class.java)
    }

    val memberService: MemberService by lazy {
        retrofit.instance.create(MemberService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit.instance.create(CommentService::class.java)
    }

    val bookService: BookService by lazy {
        retrofit.instance.create(BookService::class.java)
    }

    val replyService: ReplyService by lazy {
        retrofit.instance.create(ReplyService::class.java)
    }
}
