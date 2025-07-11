package com.wooze.mid_point.ui.floatWindowUi

import android.content.ClipData
import android.content.Context
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.wooze.mid_point.state.UiState
import com.wooze.mid_point.typeCategory
import com.wooze.mid_point.viewModel.FloatViewModel
import kotlinx.coroutines.delay

@Composable
@OptIn(ExperimentalFoundationApi::class)
fun CollapsedMode(context: Context, viewModel: FloatViewModel) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxSize()
            .clip(RoundedCornerShape(20))
            .background(Color.Gray)
            .dragAndDropSource {
                detectTapGestures(
                    onLongPress = {
                        val list = UiState.dragDataList
                        if (list.isNotEmpty()) {
                            val clipData = ClipData.newUri(
                                context.contentResolver, "${list[0].mimetype}",
                                list[0].uri
                            )
                            for (i in 1..list.size - 1) {
                                val item = ClipData.Item(list[i].uri)
                                clipData.addItem(item)
                            }
                            startTransfer(
                                DragAndDropTransferData(
                                    clipData = clipData,
                                    flags = View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ // 拖拽出去 和 读取uri权限
                                )
                            )
                        }
                    },
                    onTap = { viewModel.toggleState() })
            },
        contentAlignment = Alignment.Center
    ) {
        if (UiState.dragDataList.isNotEmpty()) {
            val lastData = UiState.dragDataList.last()
            var show by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) {
                delay(100)  // 给予延时再显示 以防太快图片比例错误
                show = true
            }
            if (show) {
                typeCategory(lastData)
            }
        } else {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("拖出以放置")
                Text("拖入以保存")
            }
        }
    }
}