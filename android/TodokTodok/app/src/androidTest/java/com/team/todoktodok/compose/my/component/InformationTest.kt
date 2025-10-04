package com.team.todoktodok.compose.my.component

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.todoktodok.presentation.compose.my.component.Information
import org.junit.Rule
import org.junit.Test

class InformationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `닉네임과 프로필 메시지가 비어있지 않을 때 올바르게 표시된다`() {
        // given
        val nickname = "테스트닉네임"
        val profileMessage = "테스트 프로필 메시지"

        // when
        composeTestRule.setContent {
            Information(nickname = nickname, profileMessage = profileMessage)
        }

        // then
        composeTestRule.onNodeWithText(nickname).assertIsDisplayed()
        composeTestRule.onNodeWithText(profileMessage).assertIsDisplayed()
    }

    @Test
    fun `프로필 메시지가 null일 경우 placeholder가 표시된다`() {
        // given
        val nickname = "테스트닉네임"

        // when
        composeTestRule.setContent {
            Information(nickname = nickname, profileMessage = null)
        }

        // then
        composeTestRule.onNodeWithText(nickname).assertIsDisplayed()
        composeTestRule
            .onNodeWithText(
                "상태 메시지를 입력해주세요",
            ).assertIsDisplayed()
    }

    @Test
    fun `특수문자와 이모지를 포함한 텍스트가 정상적으로 표시된다`() {
        // given
        val nickname = "테스트😀123!@#"
        val profileMessage = "메시지💡✨"

        // when
        composeTestRule.setContent {
            Information(nickname = nickname, profileMessage = profileMessage)
        }

        // then
        composeTestRule.onNodeWithText(nickname).assertIsDisplayed()
        composeTestRule.onNodeWithText(profileMessage).assertIsDisplayed()
    }
}
