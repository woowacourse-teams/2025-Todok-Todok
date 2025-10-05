package com.team.todoktodok.compose.my.activated

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import com.team.todoktodok.presentation.compose.my.books.ActivatedBooksScreen
import com.team.todoktodok.presentation.compose.my.books.MyBooksUiModel
import org.junit.Rule
import org.junit.Test

class ActivatedBooksScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `책이 없을 경우 토론방으로 이동 화면이 표시된다`() {
        // given
        val uiState = MyBooksUiModel()

        // when
        composeTestRule.setContent {
            ActivatedBooksScreen(
                uiState = uiState,
                onChangeBottomNavigationTab = {},
                navController = NavHostController(LocalContext.current),
            )
        }

        // then
        composeTestRule
            .onNodeWithText(
                "아직 활동한 책이 없어요 !",
            ).assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "토론에 참여하고 활동한 책을 추가해보세요",
            ).assertIsDisplayed()

        composeTestRule
            .onNodeWithText(
                "토론방 보러가기 >",
            ).assertIsDisplayed()
    }
}
