package com.team.todoktodok.compose.component

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.unit.dp
import com.team.todoktodok.presentation.compose.core.component.InfinityLazyColumn
import org.junit.Rule
import org.junit.Test

class InfinityLazyColumnTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `모든_아이템을_렌더링한다`() {
        // given
        val items = List(10) { "Item $it" }

        // when
        composeTestRule.setContent {
            InfinityLazyColumn {
                items(items) { item ->
                    Text(text = item)
                }
            }
        }

        // then
        composeTestRule.onNodeWithText("Item 0").assertIsDisplayed()

        composeTestRule
            .onNodeWithText("Item 9")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun `아이템이_limitCount보다_적으면_loadMore가_호출되지_않는다`() {
        var loadMoreCalled = false

        // given
        val items = List(20) { "Item $it" }

        // when
        composeTestRule.setContent {
            InfinityLazyColumn(loadMoreLimitCount = 3, loadMore = {
                loadMoreCalled = true
            }) {
                items(items) { item ->
                    Text(text = item, modifier = Modifier.height(150.dp))
                }
            }
        }

        // then
        composeTestRule.onNodeWithText("Item 5").performScrollTo().assertIsDisplayed()
        assert(!loadMoreCalled)
    }

    @Test
    fun `마지막에_도달하면_loadMore가_호출된다`() {
        var loadMoreCount = 0
        val items = List(10) { "Item $it" }

        composeTestRule.setContent {
            InfinityLazyColumn(
                modifier =
                    Modifier
                        .height(500.dp)
                        .testTag("lazyColumn"),
                loadMoreLimitCount = 2,
                loadMore = { loadMoreCount++ },
            ) {
                items(items) { item ->
                    Text(
                        text = item,
                        modifier =
                            Modifier
                                .height(200.dp)
                                .testTag(item),
                    )
                }
            }
        }

        composeTestRule
            .onNodeWithTag("lazyColumn")
            .performScrollToIndex(8)

        composeTestRule
            .onNodeWithTag("Item 8")
            .assertIsDisplayed()

        assert(loadMoreCount > 0)
    }
}
