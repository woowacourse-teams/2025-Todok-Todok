package com.team.ui_compose.discussion.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.team.ui_compose.R
import com.team.ui_compose.theme.Black18
import com.team.ui_compose.theme.Green64
import com.team.ui_compose.theme.Green73
import com.team.ui_compose.theme.GreenA8
import com.team.ui_compose.theme.GreenF0
import com.team.ui_compose.theme.Pretendard

@Composable
fun SearchDiscussionBar(
    searchKeyword: String,
    onKeywordChange: (String) -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = searchKeyword,
        onValueChange = { onKeywordChange(it) },
        placeholder = {
            Text(
                text = stringResource(R.string.discussion_search_bar_hint),
                fontSize = 14.sp,
                color = Color.Gray,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth(),
            )
        },
        singleLine = true,
        trailingIcon = {
            if (searchKeyword.isNotEmpty()) {
                IconButton(onClick = { onKeywordChange("") }) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = stringResource(R.string.search_clear_keyword),
                    )
                }
            }
        },
        textStyle =
            TextStyle(
                color = Black18,
                fontSize = 14.sp,
                fontFamily = Pretendard,
            ),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions =
            KeyboardOptions(
                imeAction = ImeAction.Search,
            ),
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = GreenF0,
                unfocusedContainerColor = GreenF0,
                disabledContainerColor = GreenF0,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                cursorColor = Black18,
                errorCursorColor = Black18,
                selectionColors =
                    TextSelectionColors(
                        handleColor = Green64,
                        backgroundColor = GreenA8,
                    ),
                focusedLeadingIconColor = Black18,
                unfocusedLeadingIconColor = Black18,
                disabledLeadingIconColor = Black18,
                focusedTrailingIconColor = Black18,
                unfocusedTrailingIconColor = Black18,
                disabledTrailingIconColor = Black18,
            ),
        keyboardActions =
            KeyboardActions(
                onSearch = {
                    keyboardController?.hide()
                    if (searchKeyword.isNotBlank()) {
                        onSearch()
                    }
                },
            ),
        maxLines = 1,
        modifier =
            modifier
                .fillMaxWidth()
                .background(
                    color = Green73,
                    shape = RoundedCornerShape(8.dp),
                ),
    )
}

@Preview
@Composable
private fun SearchDiscussionBarPreview() {
    Column {
        SearchDiscussionBar(
            searchKeyword = "코틀린",
            onKeywordChange = {},
            onSearch = {},
        )
        SearchDiscussionBar(
            searchKeyword = "",
            onKeywordChange = {},
            onSearch = {},
        )
    }
}
