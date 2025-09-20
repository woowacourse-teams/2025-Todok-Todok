package com.team.domain.model.book

import com.team.domain.model.fixture.SearchedBooksFixtures
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows

class SearchedBooksTest {
    @Test
    fun `SearchedBooks의 크기를 알 수 있다`() {
        // given
        val books = SearchedBooksFixtures.books

        // when
        val actual = books.size
        val expected = 16

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `SearchedBooks의 요소가 비어있으면 isEmpty() 실행시, true를 반환한다`() {
        // given
        val books = SearchedBooks(emptyList())

        // when & then
        assertTrue(books.isEmpty())
    }

    @Test
    fun `SearchedBooks의 요소가 비어있지 않으면 isEmpty() 실행시, false를 반환한다`() {
        // given
        val books = SearchedBooksFixtures.books

        // when & then
        assertFalse(books.isEmpty())
    }

    @Test
    fun `SearchedBooks의 요소가 16개가 있고 get(0)을 실행하면 해당 객체를 반환한다`() {
        // given
        val books = SearchedBooksFixtures.books
        val position = 0

        // when
        val actual = books.get(position)
        val expected =
            SearchedBook(
                ISBN(9791158391409L),
                BookTitle("오브젝트 - 코드로 이해하는 객체지향 설계"),
                BookAuthor("조영호 (지은이)"),
                BookImage("https://image.aladin.co.kr/product/19368/10/coversum/k972635015_1.jpg"),
            )

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `SearchedBooks의 요소가 16개가 있고 get(-1)과 get(17)을 실행하면 IndexOutOfBoundsException을 발생시킨다`() {
        // given
        val books = SearchedBooksFixtures.books

        // when & then
        assertAll(
            { assertThrows<IndexOutOfBoundsException> { books.get(-1) } },
            { assertThrows<IndexOutOfBoundsException> { books.get(17) } },
        )
    }

    @Test
    fun `SearchedBooks의 요소가 16개가 있고 contains(0)을 실행하면 true를 반환한다`() {
        // given
        val books = SearchedBooksFixtures.books
        val position = 0

        // when
        val actual = books.contains(position)
        val expected = true

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `SearchedBooks의 요소가 16개가 있고 contains(17)을 실행하면 false를 반환한다`() {
        // given
        val books = SearchedBooksFixtures.books
        val position = 17

        // when
        val actual = books.contains(position)
        val expected = false

        // then
        assertEquals(expected, actual)
    }

    @Test
    fun `map은 SearchedBooks의 요소 크기를 보장한다`() {
        // given
        val books = SearchedBooksFixtures.books

        // when
        val actual = books.map { it }.size
        val expected = books.size

        // then
        assertEquals(actual, expected)
    }
}
