package com.team.todoktodok.presentation.compose.my.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LibraryBooks
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BookErrorIllustration(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(120.dp, 160.dp),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = Icons.Filled.LibraryBooks,
            contentDescription = "Book Error",
            tint = MaterialTheme.colorScheme.outline,
            modifier = Modifier.size(64.dp),
        )
        Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = "Error",
            tint = MaterialTheme.colorScheme.error,
            modifier =
                Modifier
                    .align(Alignment.BottomEnd)
                    .size(28.dp),
        )
    }
}

@Preview
@Composable
private fun BookErrorIllustrationPreview() {
    BookErrorIllustration()
}
