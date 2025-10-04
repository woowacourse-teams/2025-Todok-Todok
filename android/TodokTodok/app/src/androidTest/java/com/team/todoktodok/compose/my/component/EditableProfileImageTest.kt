package com.team.todoktodok.compose.my.component

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.team.todoktodok.presentation.compose.my.component.EditableProfileImage
import org.junit.Rule
import org.junit.Test

class EditableProfileImageTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `프로필 이미지가 보인다`() {
        // given
        composeTestRule.setContent {
            EditableProfileImage(profileImageUrl = "https://example.com/image.png")
        }

        // then
        composeTestRule
            .onNodeWithContentDescription(
                "프로필 사진",
            ).assertExists()
    }

    @Test
    fun `카메라 아이콘이 보인다`() {
        // given
        composeTestRule.setContent {
            EditableProfileImage(profileImageUrl = "")
        }

        // then
        composeTestRule
            .onNodeWithContentDescription(
                "프로필 사진 수정 아이콘",
            ).assertExists()
    }
}
