package com.team.domain.model.book

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class SearchedBookTest {
    @Test
    fun `SearchedBook은 내부의 값을 올바르게 노출한다`() {
        // given
        val book =
            SearchedBook(
                9791158391409L,
                BookTitle("오브젝트 - 코드로 이해하는 객체지향 설계"),
                BookAuthor("조영호 (지은이)"),
                BookImage("https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg"),
                "",
                "",
            )

        // when
        val isbn = book.isbn
        val mainTitle = book.mainTitle
        val author = book.author
        val image = book.image

        // then
        assertAll(
            { assertEquals(9791158391409L, isbn) },
            { assertEquals("오브젝트", mainTitle) },
            { assertEquals("조영호 (지은이)", author) },
            {
                assertEquals(
                    "https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg",
                    image,
                )
            },
        )
    }

    @Test
    fun `팩토리로 만든 SearchedBook은 내부의 값을 올바르게 노출한다`() {
        // given
        val book =
            SearchedBook.SearchedBook(
                9791158391409L,
                "오브젝트 - 코드로 이해하는 객체지향 설계",
                "조영호 (지은이)",
                "https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg",
                "",
                "",
            )

        // when
        val isbn = book.isbn
        val mainTitle = book.mainTitle
        val author = book.author
        val image = book.image

        // then
        assertAll(
            { assertEquals(9791158391409L, isbn) },
            { assertEquals("오브젝트", mainTitle) },
            { assertEquals("조영호 (지은이)", author) },
            {
                assertEquals(
                    "https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg",
                    image,
                )
            },
        )
    }
}
