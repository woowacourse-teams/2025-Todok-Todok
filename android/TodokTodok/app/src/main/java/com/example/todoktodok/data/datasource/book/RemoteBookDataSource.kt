package com.example.todoktodok.data.datasource.book

import com.example.domain.model.Book

class RemoteBookDataSource : BookDataSource {
    override suspend fun fetchBooks(): List<Book> =
        listOf(
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
            Book(
                id = 0,
                title = "나무처럼 생각하기 : 나무처럼 자연의 질서 속에서 다시 살아가는 방법에 대하여",
                author = "조슈아 블로쉬",
                image = "",
            ),
        )
}
