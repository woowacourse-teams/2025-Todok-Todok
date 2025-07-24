package com.example.todoktodok.data.di

import com.example.todoktodok.data.datasource.CommentDataSource
import com.example.todoktodok.data.datasource.DefaultCommentDataSource
import com.example.todoktodok.data.datasource.book.BookDataSource
import com.example.todoktodok.data.datasource.book.RemoteBookDataSource
import com.example.todoktodok.data.datasource.discussion.DefaultDiscussionDataSource
import com.example.todoktodok.data.datasource.discussion.DiscussionDataSource
import com.example.todoktodok.data.datasource.member.MemberDataSource
import com.example.todoktodok.data.datasource.member.RemoteMemberDataSource
import com.example.todoktodok.data.datasource.note.NoteDataSource
import com.example.todoktodok.data.datasource.note.RemoteNoteDataSource

class DataSourceModule(
    serviceModule: ServiceModule,
) {
    val bookDataSource: BookDataSource by lazy { RemoteBookDataSource() }

    val noteDataSource: NoteDataSource by lazy { RemoteNoteDataSource() }

    val discussionDataSource: DiscussionDataSource by lazy { DefaultDiscussionDataSource }

    val commentDataSource: CommentDataSource by lazy { DefaultCommentDataSource() }

    val remoteMemberDataSource: MemberDataSource by lazy { RemoteMemberDataSource(serviceModule.memberService) }
}