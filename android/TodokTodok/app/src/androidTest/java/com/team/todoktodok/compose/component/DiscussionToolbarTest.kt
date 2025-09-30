package com.team.todoktodok.compose.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.team.todoktodok.presentation.compose.discussion.component.DiscussionToolbar
import org.junit.Rule
import org.junit.Test

class DiscussionToolbarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `알람 아이콘이 화면에 보이고 확인하지 않은 알람이 없으면 뱃지가 보이지 않는다`() {
        composeTestRule.setContent {
            DiscussionToolbar(
                isExistNotification = false,
                onClickNotification = {},
            )
        }

        composeTestRule
            .onNodeWithContentDescription("알람 아이콘")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithContentDescription("확인하지 않은 알람이 있어요")
            .assertIsNotDisplayed()
    }

    @Test
    fun `확인하지 않은 알람이 있으면 뱃지가 보인다`() {
        composeTestRule.setContent {
            DiscussionToolbar(
                isExistNotification = true,
                onClickNotification = {},
            )
        }

        // 뱃지 점의 contentDescription 확인
        composeTestRule
            .onNodeWithContentDescription(
                "확인하지 않은 알람이 있어요",
            ).assertIsDisplayed()
    }
}
