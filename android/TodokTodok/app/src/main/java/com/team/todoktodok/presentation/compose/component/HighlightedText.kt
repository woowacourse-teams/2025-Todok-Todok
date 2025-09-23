package com.team.todoktodok.presentation.compose.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.team.todoktodok.presentation.compose.theme.Green1A

@Composable
fun highlightedText(
    text: String,
    keyword: String,
    contextLength: Int,
) = buildAnnotatedString {
    if (keyword.isEmpty()) {
        append(text)
        return@buildAnnotatedString
    }

    val firstIndex = text.indexOf(keyword, ignoreCase = true)
    if (firstIndex < 0) {
        append(text)
        return@buildAnnotatedString
    }

    val start = (firstIndex - contextLength).coerceAtLeast(0)
    val end = (firstIndex + keyword.length + contextLength).coerceAtMost(text.length)
    val contextText = text.substring(start, end)

    var searchIndex = 0
    while (true) {
        val index = contextText.indexOf(keyword, searchIndex, ignoreCase = true)
        if (index < 0) break
        if (index > searchIndex) {
            append(contextText.substring(searchIndex, index))
        }
        withStyle(style = SpanStyle(color = Green1A)) {
            append(contextText.substring(index, index + keyword.length))
        }
        searchIndex = index + keyword.length
    }

    if (searchIndex < contextText.length) {
        append(contextText.substring(searchIndex))
    }
}

@Preview(showBackground = true)
@Composable
fun HighlightedTextPreview() {
    highlightedText(
        text = "This is a sample text with a keyword.",
        keyword = "keyword",
        contextLength = 5,
    )
}
