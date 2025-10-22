package com.team.todoktodok.presentation.compose.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography =
    Typography(
        titleMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 20.sp,
                color = Black21,
            ),
        bodyMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Gray68,
            ),
        labelLarge =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Gray66,
            ),
        labelMedium =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                lineHeight = 16.sp,
                color = Black21,
            ),
        labelSmall =
            TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = Black42,
            ),
    )

@Composable
fun TodoktodokTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        typography = Typography,
        content = content,
        colorScheme =
            MaterialTheme.colorScheme.copy(
                surface = Color.White,
                background = Color.White,
            ),
    )
}
