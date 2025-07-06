package com.wooze.mid_point.ui

import android.content.ClipData
import android.util.Log
import android.view.View
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draganddrop.toAndroidDragEvent
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.wooze.mid_point.data.DragData
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.viewModel.FloatViewModel


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun FloatWindow(viewModel: FloatViewModel) {
    val clicked by viewModel.isOpen
    val activity = LocalActivity.current
    val context = LocalContext.current
    val height by animateDpAsState(
        targetValue = if (clicked) 300.dp else 75.dp,
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow), // 弹性 速度
        label = "height"
    )
    val width by animateDpAsState(
        targetValue = if (clicked) 150.dp else 30.dp,
        animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
        label = "width"
    )

    val dndTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                super.onStarted(event)
                Log.d("开始", "开始")
                viewModel.open()
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                val permission = activity?.requestDragAndDropPermissions(event.toAndroidDragEvent())
                val dataCount = event.toAndroidDragEvent().clipData.itemCount
                for (i in 0..dataCount - 1) {
                    // TODO 完成文件的分类给对应的图标
                    val clipData = event.toAndroidDragEvent().clipData
                    val uri = clipData.getItemAt(i).uri
                    val mem = context.contentResolver.getType(uri)
                    val dragData = DragData(uri, mem)
                    UiState.dragDataList.add(dragData)
                }
                return true
            }

            override fun onEntered(event: DragAndDropEvent) {
                super.onEntered(event)
                viewModel.open()
            }

            override fun onExited(event: DragAndDropEvent) {
                super.onEnded(event)
                viewModel.close()
            }
        }
    }


    Box(
        modifier = Modifier
            .height(height)
            .width(width)
            .clip(RoundedCornerShape(topEnd = 20.dp, bottomEnd = 20.dp))
            .background(Color.Red)
            .clickable(onClick = { viewModel.toggleOpen() })
            .dragAndDropTarget(
                shouldStartDragAndDrop = { return@dragAndDropTarget true },
                target = dndTarget
            )

    ) {
        LazyColumn {
            UiState.dragDataList.forEach { data ->
                item {// TODO 完善悬浮窗
                    if (data.mimetype != null) {
                        Box(
                            modifier = Modifier
                                .padding(10.dp)
                                .fillMaxWidth()
                                .height(100.dp)
                                .clip(RoundedCornerShape(10))
                                .dragAndDropSource {
                                    detectTapGestures(onLongPress = {
                                        startTransfer(
                                            DragAndDropTransferData(clipData = ClipData.newUri(context.contentResolver,"file",data.uri), flags = View.DRAG_FLAG_GLOBAL)
                                        )
                                    })
                                }
                        ) {
                            GlideImage(
                                model = typeCategory(data),
                                contentDescription = "picture",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }
    }

}

