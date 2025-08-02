package com.wooze.mid_point.ui.addon

import android.content.ClipData
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.draganddrop.dragAndDropSource
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropTransferData


@OptIn(ExperimentalFoundationApi::class)
fun Modifier.dragOutData(clipData: ClipData?, onClick: () -> Unit = {}): Modifier {
    if (clipData == null) {
        return this
    }

    return this.then(this.dragAndDropSource {
        detectTapGestures(
            onLongPress = {
                startTransfer(
                    DragAndDropTransferData(
                        clipData = clipData,
                        flags = View.DRAG_FLAG_GLOBAL or View.DRAG_FLAG_GLOBAL_URI_READ // 拖拽出去 和 读取uri权限
                    )
                )
            },
            onTap = { onClick() }
        )
    })
}