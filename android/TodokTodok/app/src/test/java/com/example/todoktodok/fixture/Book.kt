package com.example.todoktodok.fixture

import com.example.domain.model.Book
import com.example.domain.model.Books

val BOOKS_FIXTURES =
    Books(
        listOf(
            Book(
                id = 1,
                title = "나무처럼 생각하기: 자연과 함께 살아가는 법",
                author = "피터 울렙",
                image = "https://example.com/book1.jpg",
            ),
            Book(
                id = 2,
                title = "숲의 지혜: 나무가 말해주는 삶의 방식",
                author = "수잔 심즈",
                image = "https://example.com/book2.jpg",
            ),
            Book(
                id = 3,
                title = "뿌리의 철학: 느리게 깊게 사는 삶",
                author = "조슈아 블로쉬",
                image = "https://example.com/book3.jpg",
            ),
            Book(
                id = 4,
                title = "잎사귀 수업: 나무에서 배우는 인간관계",
                author = "아야나 존슨",
                image = "https://example.com/book4.jpg",
            ),
            Book(
                id = 5,
                title = "가지와 뿌리: 연결과 공존의 생태학",
                author = "에드워드 윌슨",
                image = "https://example.com/book5.jpg",
            ),
        ),
    )
