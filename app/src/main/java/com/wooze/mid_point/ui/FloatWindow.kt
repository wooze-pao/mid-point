package com.wooze.mid_point.ui

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wooze.mid_point.state.UiState

@Composable
@Preview
fun FloatWindow() {
    val clicked by UiState.isOpen
    val height by animateDpAsState(
        targetValue = if (clicked) 500.dp else 75.dp,
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow), // 弹性 速度
        label = "height"
    )
    val width by animateDpAsState(
        targetValue = if (clicked) 150.dp else 20.dp,
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
        label = "width"
    )


    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .background(Color.Red)
            .clickable(onClick = { UiState.isOpen.value = !UiState.isOpen.value })
    )
}