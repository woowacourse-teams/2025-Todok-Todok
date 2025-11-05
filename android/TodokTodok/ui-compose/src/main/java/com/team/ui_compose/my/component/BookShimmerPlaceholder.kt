package com.team.ui_compose.my.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ShimmerPlaceholder(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val shimmer =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(durationMillis = 1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "shimmer",
        )

    val shimmerColors =
        listOf(
            Color(0xFFEEEEEE),
            Color(0xFFDDDDDD),
            Color(0xFFEEEEEE),
        )

    Box(
        modifier =
            modifier
                .size(120.dp, 160.dp)
                .background(
                    brush =
                        Brush.linearGradient(
                            colors = shimmerColors,
                            start = Offset(shimmer.value - 200f, 0f),
                            end = Offset(shimmer.value, 160f),
                        ),
                ),
    )
}

@Preview
@Composable
private fun ShimmerPlaceholderPreview() {
    ShimmerPlaceholder()
}
