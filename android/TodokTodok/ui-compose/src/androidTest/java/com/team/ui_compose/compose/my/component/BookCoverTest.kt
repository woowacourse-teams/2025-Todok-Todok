package com.team.ui_compose.compose.my.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.domain.model.Book
import com.team.ui_compose.my.component.BookCover
import org.junit.Rule
import org.junit.Test

class BookCoverTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `책 제목과 저자가 보인다`() {
        // Given
        val book =
            Book(
                id = 1L,
                title = "오브젝트",
                author = "조영호",
                image = "https://example.com/book.png",
            )

        // When
        composeTestRule.setContent {
            BookCover(book, onActionClick = {})
        }

        // Then
        composeTestRule.onNodeWithText("오브젝트").assertIsDisplayed()
        composeTestRule.onNodeWithText("조영호").assertIsDisplayed()
    }
}
