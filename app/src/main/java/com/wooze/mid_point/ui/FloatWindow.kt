package com.wooze.mid_point.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.state.UiState

@Composable
@Preview
fun FloatWindow() {
    val clicked by UiState.isOpen
    val activity = LocalActivity.current
    val context = LocalContext.current
    val dragDataList = remember { mutableStateListOf<DragData>() }
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

    val dndTarget = remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                val permission = activity?.requestDragAndDropPermissions(event.toAndroidDragEvent())
                val dataCount = event.toAndroidDragEvent().clipData.itemCount
                for (i in 0..dataCount - 1) {
                    // TODO 完成文件的分类给对应的图标
                    val uri = event.toAndroidDragEvent().clipData.getItemAt(i).uri
                    val clipData = event.toAndroidDragEvent().clipData
                    val mem = context.contentResolver.getType(uri)
                    val dragData = DragData(uri,clipData,mem)
                    dragDataList.add(dragData)
                }
                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                UiState.isOpen.value = true
            }

            override fun onEnded(event: DragAndDropEvent) {
                super.onEnded(event)
                UiState.isOpen.value = false
            }
        }
    }

    Box(
        modifier = Modifier
            .dragAndDropTarget(
                shouldStartDragAndDrop = { return@dragAndDropTarget true },
                target = dndTarget
            )
    ) {
        Box(
            modifier = Modifier
                .height(height)
                .width(width)
                .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
                .background(Color.Red)
                .clickable(onClick = { UiState.isOpen.value = !UiState.isOpen.value })

        ) {
            LazyColumn {
                dragDataList.forEach { data ->
                    item {// TODO 完善悬浮窗
                        Box(modifier = Modifier
                            .height(50.dp)
                            .fillMaxWidth()
                            .background(Color.Blue)) {
                            Text("${data.mimetype}")
                        }
                    }
                }
            }
        }
    }

}