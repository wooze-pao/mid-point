package com.wooze.mid_point.ui.floatWindowUi

import android.util.Log
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.data.WindowState.Collapsed
import com.wooze.mid_point.data.WindowState.Expand
import com.wooze.mid_point.data.WindowState.Hidden
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.viewModel.FloatViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun FloatWindow(viewModel: FloatViewModel) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    val height by animateDpAsState(
        targetValue = viewModel.targetHeight.value,
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow), // 弹性 速度
        label = "height"
    )
    val width by animateDpAsState(
        targetValue = viewModel.targetWidth.value,
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
        label = "width"
    )

    val dndTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                super.onStarted(event)
                Log.d("开始", "开始")
                viewModel.collapsed()
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val permission = activity?.requestDragAndDropPermissions(event.toAndroidDragEvent())
                val dataCount = event.toAndroidDragEvent().clipData.itemCount
                for (i in 0..dataCount - 1) {
                    // TODO 完成文件的分类给对应的图标
                    val clipData = event.toAndroidDragEvent().clipData
                    val uri = clipData.getItemAt(i).uri
                    // 排除有时候拖入两种类型
                    // TODO 也许以后兼容粘贴板需要改逻辑
                    Log.d("hahah", "$uri")
                    if (uri != null) {
                        val mimetype = context.contentResolver.getType(uri)
                        val dragData = DragData(uri, mimetype)
                        UiState.dragDataList.add(dragData)
                    }
                }
                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                viewModel.collapsed()
            }

            override fun onExited(event: DragAndDropEvent) {
                super.onEnded(event)
                viewModel.hidden()
            }
        }
    }


    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(RoundedCornerShape(topEnd = 30.dp, bottomEnd = 30.dp))
            .background(Color.LightGray)
            .clickable(
                onClick = { viewModel.toggleState() }, indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .dragAndDropTarget(
                shouldStartDragAndDrop = { return@dragAndDropTarget true },
                target = dndTarget
            )

    ) {
        AnimatedContent(
            targetState = viewModel.windowState.value,
            transitionSpec = {
                (fadeIn(animationSpec = tween(500, delayMillis = 230)))
                    .togetherWith(fadeOut(animationSpec = tween(90)))
            }
        ) { state ->
            when (state) {
                Hidden -> {}
                Collapsed -> {
                    CollapsedMode(context, viewModel)
                }

                Expand -> {
                    ExpandMode(context)
                }
            }
        }

    }

}



