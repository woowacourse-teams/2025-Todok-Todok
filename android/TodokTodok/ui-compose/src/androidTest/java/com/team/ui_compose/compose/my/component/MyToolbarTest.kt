package com.team.ui_compose.compose.my.component

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import com.team.ui_compose.my.component.MyToolbar
import org.junit.Rule
import org.junit.Test

class MyToolbarTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun `설정 아이콘이 보인다`() {
        composeTestRule.setContent {
            MyToolbar({})
        }

        composeTestRule.onNodeWithContentDescription("설정 아이콘").assertIsDisplayed()
    }
}
