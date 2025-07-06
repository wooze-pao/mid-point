package com.wooze.mid_point.state

import androidx.compose.runtime.mutableStateListOf
import com.wooze.mid_point.data.DragData


object UiState {
    // TODO 快速点击状态不同步
    var isShowing = false
    val dragDataList = mutableStateListOf<DragData>()
}