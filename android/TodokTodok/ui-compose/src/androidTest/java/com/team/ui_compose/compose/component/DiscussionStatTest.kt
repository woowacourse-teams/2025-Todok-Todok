package com.team.ui_compose.compose.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.team.ui_compose.component.DiscussionStat
import com.team.ui_compose.theme.RedFF
import org.junit.Rule
import org.junit.Test

class DiscussionStatTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `전달받은 수치가 보인다`() {
        // given
        val content = "100"

        // when
        composeTestRule.setContent {
            DiscussionStat(
                content = content,
                icon = {
                    Icon(
                        Icons.Default.Favorite,
                        contentDescription = null,
                        tint = RedFF,
                    )
                },
            )
        }

        composeTestRule.onNodeWithText(content).assertIsDisplayed()
    }
}
