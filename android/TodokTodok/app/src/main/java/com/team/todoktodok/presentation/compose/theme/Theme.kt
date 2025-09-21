package com.team.todoktodok.presentation.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        color = Black21,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = (-2.5).sp
    ),
    labelSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        color = Black,
        fontSize = 14.sp,
        lineHeight = 24.sp,
        letterSpacing = (-2.5).sp
    )
)

@Composable
fun TodoktodokTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        typography = Typography,
        content = content,
    )
}
