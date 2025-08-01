package com.wooze.mid_point

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

data class BoxItem(
    val zIndex: Float,
    val scale: Float,
    val color: Color,
    val offset: Dp,
    val contentBox: Boolean = false
)

val BoxItems = arrayOf(
    BoxItem(10f, 1f, Color.Red, 0.dp, true), // 1
    BoxItem(5f, 0.9f, Color.White, (-10).dp),
    BoxItem(1f, 0.8f, Color.White, (-20).dp)
)

@Composable
fun StackBox(content: @Composable () -> Unit) {
    Box(modifier = Modifier.padding(top = 20.dp)) {
        BoxItems.forEach { item ->
            if (item.contentBox) {
                BoxExp(
                    Modifier
                        .zIndex(item.zIndex)
                        .offset(y = item.offset)
                        .scale(item.scale)
                        .shadow(10.dp),
                ) {
                    content()
                }
            } else {
                BoxExp(
                    Modifier
                        .zIndex(item.zIndex)
                        .offset(y = item.offset)
                        .scale(item.scale)
                        .border(width = 1.dp,color = Color.DarkGray, shape = RoundedCornerShape(20.dp)),
                    item.color
                )
            }
        }
    }

}


@Composable
fun BoxExp(
    modifier: Modifier = Modifier,
    color: Color? = null,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp))
            .then(if (color != null) Modifier.background(color) else Modifier)
    ) {
        content()
    }
}