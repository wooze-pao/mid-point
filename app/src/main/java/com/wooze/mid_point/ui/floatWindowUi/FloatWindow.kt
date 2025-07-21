package com.wooze.mid_point.ui.floatWindowUi

import android.util.Log
import android.view.DragAndDropPermissions
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.wooze.mid_point.data.Corner
import com.wooze.mid_point.data.WindowState.Collapsed
import com.wooze.mid_point.data.WindowState.Expand
import com.wooze.mid_point.data.WindowState.Hidden
import com.wooze.mid_point.tools.DataTools
import com.wooze.mid_point.viewModel.FloatViewModel


@OptIn(ExperimentalFoundationApi::class, ExperimentalGlideComposeApi::class)
@Composable
fun FloatWindow(viewModel: FloatViewModel) {
    val activity = LocalActivity.current
    val context = LocalContext.current
    var permission = remember { mutableStateOf<DragAndDropPermissions?>(null) }
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
                viewModel.collapsed()
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                permission.value = activity?.requestDragAndDropPermissions(event.toAndroidDragEvent())
                DataTools.extractAndSave(event, context)
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
            .clip(RoundedCornerShape(topEnd = Corner.Outer, bottomEnd = Corner.Outer))
            .background(Color.White)
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
                fadeIn(tween(durationMillis = 200, delayMillis = 400)).togetherWith(
                    fadeOut()
                )
            }
        ) { state ->
            when (state) {
                Hidden -> {}
                Collapsed -> {
                    CollapsedMode(context, viewModel)
                }

                Expand -> {
                    ExpandMode(context, viewModel)
                }
            }
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            permission.value?.release()
        }
    }

}



