package com.team.todoktodok.data.di

import com.team.todoktodok.data.network.service.BookService
import com.team.todoktodok.data.network.service.CommentService
import com.team.todoktodok.data.network.service.DiscussionService
import com.team.todoktodok.data.network.service.MemberService
import com.team.todoktodok.data.network.service.NotificationService
import com.team.todoktodok.data.network.service.RefreshService
import com.team.todoktodok.data.network.service.ReplyService

class ServiceModule(
    retrofit: RetrofitModule,
) {
    val discussionService: DiscussionService by lazy {
        retrofit.createAuthService(DiscussionService::class.java)
    }

    val memberService: MemberService by lazy {
        retrofit.createAuthService(MemberService::class.java)
    }

    val commentService: CommentService by lazy {
        retrofit.createAuthService(CommentService::class.java)
    }

    val bookService: BookService by lazy {
        retrofit.createAuthService(BookService::class.java)
    }

    val replyService: ReplyService by lazy {
        retrofit.createAuthService(ReplyService::class.java)
    }

    val refreshService: RefreshService by lazy {
        retrofit.createService(RefreshService::class.java)
    }

    val notificationService: NotificationService by lazy {
        retrofit.createAuthService(NotificationService::class.java)
    }
}
