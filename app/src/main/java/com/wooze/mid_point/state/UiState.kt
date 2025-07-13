package com.wooze.mid_point.state

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.wooze.mid_point.data.DragData


object UiState {
    var isShowing = mutableStateOf(false)
    val dragDataList = mutableStateListOf<DragData>()
}