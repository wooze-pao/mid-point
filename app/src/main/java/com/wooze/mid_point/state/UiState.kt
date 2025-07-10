package com.wooze.mid_point.state

import androidx.compose.runtime.mutableStateListOf
import com.wooze.mid_point.data.DragData


object UiState {
    var isShowing = false
    val dragDataList = mutableStateListOf<DragData>()
}