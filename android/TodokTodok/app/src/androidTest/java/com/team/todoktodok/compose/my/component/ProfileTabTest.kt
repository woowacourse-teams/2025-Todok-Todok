package com.team.todoktodok.compose.my.component

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.NavHostController
import com.team.todoktodok.presentation.compose.my.MyProfileUiState
import com.team.todoktodok.presentation.compose.my.component.ProfileTab
import org.junit.Rule
import org.junit.Test

class ProfileTabTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `활동도서 좋아요 참여한 토론방 탭이 보인다`() {
        // Given
        composeTestRule.setContent {
            ProfileTab(
                uiState = MyProfileUiState(),
                onChangeBottomNavigationTab = {},
                onChangeShowMyDiscussion = {},
                onCompleteRemoveDiscussion = {},
                onCompleteModifyDiscussion = {},
                navController = NavHostController(LocalContext.current),
            )
        }

        composeTestRule.onNodeWithText("활동 도서").assertIsDisplayed()
        composeTestRule.onNodeWithText("좋아요").assertIsDisplayed()
        composeTestRule.onNodeWithText("참여한 토론방").assertIsDisplayed()
    }
}
